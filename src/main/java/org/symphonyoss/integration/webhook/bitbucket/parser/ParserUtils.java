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

import java.net.URI;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.symphonyoss.integration.parser.SafeString;
import com.fasterxml.jackson.databind.JsonNode;

import static org.apache.commons.lang3.time.DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
import static org.symphonyoss.integration.messageml.MessageMLFormatConstants.MESSAGEML_MENTION_EMAIL_FORMAT;
import static org.symphonyoss.integration.parser.ParserUtils.newUri;

/**
 * A set of common utilities to look up data within the bitbucket JSON node.
 */
public class ParserUtils {
    private ParserUtils() {
    }

    private static final String A_HREF_BEGIN = "<a href=\"";
    private static final String A_HREF_END = "\"/>";


    public static String getAction(PullRequestAction action) {
        return action.toString();
    }

    public static String getState(JsonNode node) {
        return node.path("state").asText();
    }

    public static String getDescription(JsonNode node) {
        return node.path("description").asText();
    }

    public static String getAuthorAsDisplay(JsonNode node) {
        return node.path("author").path("user").path("displayName").asText();
    }

    public static String getAuthorAsMention(JsonNode node) {
        String emailAddress = node.path("author").path("user").path("emailAddress").asText();
        return wrapEmailWithMention(emailAddress);
    }

    private static String wrapEmailWithMention(final String emailAddress) {
        return StringUtils.isEmpty(emailAddress) ? "" : String.format(MESSAGEML_MENTION_EMAIL_FORMAT, new SafeString(emailAddress));
    }

    public static String getPullRequestTitle(JsonNode node) {
        return node.path("title").asText();
    }

    public static Integer getPullRequestNumber(JsonNode node) {
        return node.path("id").asInt();
    }

    public static String getToReference(JsonNode node) {
        return node.path("toRef").path("repository").path("name").asText()
                + ":" + node.path("toRef").path("id").asText();
    }

    public static String getFromReference(JsonNode node) {
        return node.path("fromRef").path("repository").path("name").asText()
                + ":" + node.path("fromRef").path("id").asText();
    }

    public static URI getLink(JsonNode node) {
        Iterator<JsonNode> iterator = node.path("links").path("self").iterator();
        String prLink = "";
        while (iterator.hasNext()) {
            prLink = iterator.next().path("href").asText();
        }
        return StringUtils.isEmpty(prLink) ? null : newUri(prLink);
    }

    public static String getLinkAsHref(JsonNode node) {
        URI link = getLink(node);
        return link == null ? "" : A_HREF_BEGIN + link.toString() + A_HREF_END;
    }

    public static String getCreatedDateTime(JsonNode node) {
        return getDateTime(node.path("createdDate").asLong());
    }

    public static String getUpdatedDateTime(JsonNode node) {
        return getDateTime(node.path("updatedDate").asLong());
    }

    public static String getClosedDateTime(JsonNode node) {
        return getDateTime(node.path("closedDate").asLong());
    }

    public static String getDateTime(Long timeInMillis) {
        String dateString = "";
        if (timeInMillis > 0) {
            dateString = DateFormatUtils.format(new Date(timeInMillis), ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
        }
        return dateString;
    }

    public static String getApproversAsMentions(JsonNode node) {
        Iterator<JsonNode> iterator = node.path("reviewers").iterator();
        String approvers = "";
        while (iterator.hasNext()) {
            JsonNode approver = iterator.next();
            if (!approver.path("approved").asBoolean(true)) {
                String email = approver.path("user").path("emailAddress").asText();
                if (!StringUtils.isEmpty(email)) {
                    approvers += wrapEmailWithMention(email) + ",";
                }
            }
        }
        return approvers;
    }

    public static String getApproversText(JsonNode node) {
        String approvers = getApproversAsMentions(node);
        if (StringUtils.isEmpty(approvers)) {
            return "All approvals are complete";
        } else {
            return "Outstanding approvers are " + approvers;
        }
    }
}
