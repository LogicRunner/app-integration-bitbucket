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

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.symphonyoss.integration.entity.MessageML;
import org.symphonyoss.integration.webhook.WebHookPayload;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link BitBucketParser} to convert any {@link PullRequestAction@COMMENTED} the received {@link WebHookPayload} from
 * BitBucket into a formatted {@link MessageML}.
 */
@Component
public class CommentedPullRequestParser extends PullRequestParser {
    private static List<String> actions = Collections.singletonList(PullRequestAction.COMMENTED.toString());

    private static final String COMMENTED_ML_MESSAGE_FORMAT =
            "Pull request by %s has been commented on <br/>"
                    + "[%s]<br/>"
                    + "%s<br/>"
                    + "You can check this pull request at:%s<br/>"
                    + "Summary:%s";

    protected String buildPresentationMl(PullRequestAction action, JsonNode node) {
        return String.format(getPresentationMlMessageFormat(),
                ParserUtils.getAuthorAsMention(node),
                ParserUtils.getPullRequestTitle(node),
                ParserUtils.getApproversText(node),
                ParserUtils.getLink(node),
                ParserUtils.getDescription(node));
    }

    @Override
    public List<String> getEvents() {
        return actions;
    }

    protected static String getPresentationMlMessageFormat() {
        return COMMENTED_ML_MESSAGE_FORMAT;
    }
}
