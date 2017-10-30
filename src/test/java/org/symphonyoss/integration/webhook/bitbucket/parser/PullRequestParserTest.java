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
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.bitbucket.BitBucketParserConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for {@link PullRequestParser}.
 */
public class PullRequestParserTest {

    @Test
    public void testPullRequestOpenedMessage() throws IOException {
        compareMessages("payload_bitbucket_pull_request_opened.json",
                "payload_bitbucket_pull_request_opened_expected_ml.xml",
                "payload_bitbucket_pull_request_opened_expected_message.xml",
                PullRequestAction.OPENED);
    }

    private void compareMessages(final String jsonFileName, final String expectedML1FileName,
                                 final String expectedML2FileName,
                                 PullRequestAction action) {
        String data = getResourceFileAsString(jsonFileName);
        String expectedML1 = getResourceFileAsString(expectedML1FileName);
        String expectedML2 = getResourceFileAsString(expectedML2FileName);

        if (!StringUtils.isEmpty(expectedML1)) {
            Message result = new PullRequestParser().parseForMlV1(new WebHookPayload(
                    Collections.<String, String>emptyMap(),
                    Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, action.toString()),
                    data));
            assertNotNull(result);
            assertEquals(expectedML1, result.getMessage());
        }
        if (!StringUtils.isEmpty(expectedML2)) {
            Message result = new PullRequestParser().parse(new WebHookPayload(
                    Collections.<String, String>emptyMap(),
                    Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, action.toString()),
                    data));
            assertNotNull(result);
            assertEquals(expectedML2, result.getMessage());
        }
    }

    @Test
    public void testPullRequestApprovedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_approved.json");
        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_approved_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.APPROVED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }


    @Test
    public void testPullRequestDeclinedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_declined.json");
//        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_declined_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.DECLINED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void testPullRequestMergedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_merged.json");
//        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_merged_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.MERGED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void testPullRequestReOpendedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_reopened.json");
//        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_reopended_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.REOPENED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void testPullRequestReviewedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_reviewed.json");
//        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_reviewed_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.REVIEWED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void testPullRequestUnApprovedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_unapproved.json");
//        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_unapproved_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.UNAPPROVED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void testPullRequestUpdatedRequest() throws IOException {
        String data = getResourceFileAsString("payload_bitbucket_pull_request_updated.json");
//        String expectedMessage = getResourceFileAsString("payload_bitbucket_pull_request_updated_expected.xml");

        Message result = new PullRequestParser().parse(new WebHookPayload(
                Collections.<String, String>emptyMap(),
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, PullRequestAction.UPDATED.toString()),
                data));

        assertNotNull(result);
//        assertEquals(expectedMessage, result.getMessage());
    }


    protected static String getResourceFileAsString(String expectedMessageFileName) {
        StringBuilder expectedMessage = new StringBuilder();
        try (Scanner scan = new Scanner(
                PullRequestParserTest.class.getClassLoader().getResourceAsStream(expectedMessageFileName), "UTF-8")
                .useDelimiter(System.lineSeparator())) {
            while (scan.hasNextLine()) {
                expectedMessage.append(scan.nextLine().trim());
            }
        }
        return expectedMessage.toString();
    }

    @Test
    public void testEvents() {
        List<String> actions = Arrays.asList(
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
        assertEquals(actions, new PullRequestParser().getEvents());
    }
}