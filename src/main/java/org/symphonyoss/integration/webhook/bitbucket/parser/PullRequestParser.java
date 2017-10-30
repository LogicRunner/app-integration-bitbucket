/**
 * Copyright 2016-2017 Symphony Integrations - Symphony LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.symphonyoss.integration.webhook.bitbucket.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.symphonyoss.integration.entity.Entity;
import org.symphonyoss.integration.entity.EntityBuilder;
import org.symphonyoss.integration.entity.MessageML;
import org.symphonyoss.integration.exception.EntityXMLGeneratorException;
import org.symphonyoss.integration.json.JsonUtils;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;
import com.fasterxml.jackson.databind.JsonNode;

import static org.symphonyoss.integration.messageml.MessageMLFormatConstants.MESSAGEML_END;
import static org.symphonyoss.integration.messageml.MessageMLFormatConstants.MESSAGEML_START;
import static org.symphonyoss.integration.webhook.bitbucket.BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME;

/**
 * The base {@link BitBucketParser} to convert the received {@link WebHookPayload} from BitBucket into a
 * formatted {@link MessageML}.
 */
@Component
public class PullRequestParser implements BitBucketParser {
    private static final String INTEGRATION_TAG = "bitbucket";
    private static List<String> actions = Arrays.asList(
            PullRequestAction.DECLINED.toString(),
            PullRequestAction.MERGED.toString(),
            PullRequestAction.OPENED.toString(),
            PullRequestAction.REOPENED.toString(),
            PullRequestAction.RESCOPED.toString(),
            PullRequestAction.UPDATED.toString(),
            PullRequestAction.APPROVED.toString(),
            PullRequestAction.UNAPPROVED.toString(),
            PullRequestAction.REVIEWED.toString()
    );

    private static final String PRESENTATIONML_MESSAGE_FORMAT =
            "Pull request by %s has been %s<br/>"
                    + "[%s]<br/>"
                    + "Merging PR %d into \"%s\" from \"%s\"<br/>"
                    + "%s<br/>"
                    + "You can check this pull request at: %s<br/>"
                    + "Summary:%s";


    @Override
    public List<String> getEvents() {
        return actions;
    }

    @Override
    public Message parse(final WebHookPayload payload) throws WebHookParseException {
        try {
            PullRequestAction event = PullRequestAction.fromString(payload.getHeaders().get(BITBUCKET_HEADER_EVENT_NAME));
            return toMessage(buildPresentationMl(event, JsonUtils.readTree(payload.getBody())), payload.getBody());
        } catch (IOException e) {
            throw new BitBucketParserException("Error processing the PullRequest form Bitbucket.", e);
        }
    }

    @Override
    public Message parseForMlV1(final WebHookPayload payload) throws WebHookParseException {
        try {
            PullRequestAction event = PullRequestAction.fromString(payload.getHeaders().get(BITBUCKET_HEADER_EVENT_NAME));
            return toMessage(buildPresentationMl(event, JsonUtils.readTree(payload.getBody())), null);
        } catch (IOException e) {
            throw new BitBucketParserException("Error processing the PullRequest form Bitbucket.", e);
        }
    }

    private Message toMessage(String formattedMessage, String entityJson) {
        if (StringUtils.isNotEmpty(formattedMessage)) {
            String messageML = MESSAGEML_START + formattedMessage + MESSAGEML_END;

            Message message = new Message();
            message.setFormat(Message.FormatEnum.MESSAGEML);
            message.setMessage(messageML);
            if (StringUtils.isNotEmpty(entityJson)) {
                message.setData(entityJson);
            }
            message.setVersion(MessageMLVersion.V2);

            return message;
        }
        return null;
    }

    private String buildPullRequestEntity(PullRequestAction action, JsonNode node) throws EntityXMLGeneratorException {
        String presentationMl = buildPresentationMl(action, node);

        return EntityBuilder
                .forIntegrationEvent(INTEGRATION_TAG, "pull_request")
                .presentationML(presentationMl)
                .attribute("action", ParserUtils.getAction(action))
                .attribute("merged", node.path("pull_request").path("merged").asText())
                .attribute("number", node.path("pull_request").path("number").asInt())
                .attribute("commits", node.path("pull_request").path("commits").asInt())
                .attribute("repo_branch_head", ParserUtils.getFromReference(node))
                .attribute("repo_branch_base", ParserUtils.getToReference(node))
                .attribute("id", ParserUtils.getPullRequestNumber(node))
                .attribute("state", node.path("pull_request").path("state").asText())
                .attribute("title", ParserUtils.getPullRequestTitle(node))
                .attribute("body", node.path("pull_request").path("body").asText())
                .nestedEntity(buildApproversEntity(node, "approvers"))
                .attributeIfNotNull("html_url", ParserUtils.getLink(node))
                .attributeIfNotEmpty("label", node.path("label").path("name").asText())
                .dateAttributeIfNotBlank("created_at", ParserUtils.getCreatedDateTime(node))
                .dateAttributeIfNotBlank("updated_at", ParserUtils.getUpdatedDateTime(node))
                .dateAttributeIfNotBlank("closed_at", ParserUtils.getClosedDateTime(node))
                .generateXML();
    }

    private Entity buildApproversEntity(JsonNode node, String entityNodeName) {
        EntityBuilder approversBuilder = EntityBuilder.forNestedEntity(INTEGRATION_TAG, entityNodeName);

        Iterator<JsonNode> iterator = node.path("reviewers").iterator();
        while (iterator.hasNext()) {
            JsonNode approver = iterator.next();
            approversBuilder.nestedEntity(buildApproverEntity(approver));
        }
        return approversBuilder.build();
    }

    private Entity buildApproverEntity(JsonNode node) {
        EntityBuilder approverBuilder = EntityBuilder.forNestedEntity(INTEGRATION_TAG, "approver");
        approverBuilder.attribute("full_name", node.path("user").path("displayName").asText());
        approverBuilder.attribute("email", node.path("user").path("emailAddress").asText());
        approverBuilder.attribute("status", node.path("status").asText());
        return approverBuilder.build();
    }

    protected String buildPresentationMl(PullRequestAction action, JsonNode node) {
        return String.format(getPresentationMlMessageFormat(),
                ParserUtils.getAuthorAsMention(node),
                ParserUtils.getAction(action),
                ParserUtils.getPullRequestTitle(node),
                ParserUtils.getPullRequestNumber(node),
                ParserUtils.getToReference(node),
                ParserUtils.getFromReference(node),
                ParserUtils.getApproversText(node),
                ParserUtils.getLinkAsHref(node),
                ParserUtils.getDescription(node));
    }

    protected static String getPresentationMlMessageFormat() {
        return PRESENTATIONML_MESSAGE_FORMAT;
    }
}
