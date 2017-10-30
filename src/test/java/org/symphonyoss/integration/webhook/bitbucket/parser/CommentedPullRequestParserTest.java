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
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.webhook.WebHookPayload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.symphonyoss.integration.webhook.bitbucket.BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME;

/**
 * Test cases for {@link PullRequestParser}.
 */
public class CommentedPullRequestParserTest {
    @Test
    public void testPullRequestCommentedRequest() throws IOException {
        String data = PullRequestParserTest.getResourceFileAsString("payload_bitbucket_pull_request_commented.json");
        String expectedMessage = PullRequestParserTest.getResourceFileAsString("payload_bitbucket_pull_request_commented_expected.xml");

        Message result = new CommentedPullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.COMMENTED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void testEvents() {
        List<String> actions = Collections.singletonList(PullRequestAction.COMMENTED.toString());
        assertEquals(actions, new CommentedPullRequestParser().getEvents());
    }
}