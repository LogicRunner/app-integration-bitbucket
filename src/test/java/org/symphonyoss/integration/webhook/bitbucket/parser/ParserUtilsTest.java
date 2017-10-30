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
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.symphonyoss.integration.json.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link ParserUtils}.
 */
public class ParserUtilsTest {
    @Test
    public void testGetAction() throws IOException {
        assertEquals("OPENED", ParserUtils.getAction(PullRequestAction.OPENED));
    }

    @Test
    public void testGetState() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"state\": \"OPEN\",\n" +
                "  \"open\": true,\n" +
                "  \"closed\": false\n}");
        assertEquals("OPEN", ParserUtils.getState(node));
    }

    @Test
    public void testGetDescription() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"id\": 6,\n" +
                "  \"title\": \"Add a file\",\n" +
                "  \"description\": \"This is a simple pull request to add a new file\",\n" +
                "  \"state\": \"OPEN\"\n}");
        assertEquals("This is a simple pull request to add a new file", ParserUtils.getDescription(node));

        assertEquals("", ParserUtils.getDescription(MissingNode.getInstance()));
    }

    @Test
    public void testGetAuthorAsDisplay() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n   \"author\": {\n" +
                "    \"user\": {\n" +
                "      \"name\": \"Fred\",\n" +
                "      \"emailAddress\": \"fred.smith@example.com\",\n" +
                "      \"id\": 9876,\n" +
                "      \"displayName\": \"FRED SMITH\",\n" +
                "      \"active\": true,\n" +
                "      \"slug\": \"X9876\",\n" +
                "      \"type\": \"NORMAL\",\n" +
                "      \"links\": {\n" +
                "        \"self\": [\n" +
                "          {\n" +
                "            \"href\": \"https://bitbucket.example.com/users/fredsmith\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"role\": \"AUTHOR\",\n" +
                "    \"approved\": false,\n" +
                "    \"status\": \"UNAPPROVED\"\n" +
                "  }\n" +
                "}");
        assertEquals("FRED SMITH", ParserUtils.getAuthorAsDisplay(node));
        assertEquals("", ParserUtils.getAuthorAsDisplay(MissingNode.getInstance()));
    }

    @Test
    public void testGetAuthorAsMention() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n   \"author\": {\n" +
                "    \"user\": {\n" +
                "      \"name\": \"Fred\",\n" +
                "      \"emailAddress\": \"fred.smith@example.com\",\n" +
                "      \"id\": 9876,\n" +
                "      \"displayName\": \"FRED SMITH\",\n" +
                "      \"active\": true,\n" +
                "      \"slug\": \"X9876\",\n" +
                "      \"type\": \"NORMAL\",\n" +
                "      \"links\": {\n" +
                "        \"self\": [\n" +
                "          {\n" +
                "            \"href\": \"https://bitbucket.example.com/users/fredsmith\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"role\": \"AUTHOR\",\n" +
                "    \"approved\": false,\n" +
                "    \"status\": \"UNAPPROVED\"\n" +
                "  }\n" +
                "}");
        assertEquals("<mention email=\"fred.smith@example.com\"/>", ParserUtils.getAuthorAsMention(node));
        assertEquals("", ParserUtils.getAuthorAsMention(MissingNode.getInstance()));
    }

    @Test
    public void testGetPullRequestTitle() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"id\": 6,\n" +
                "  \"title\": \"Add a file\",\n" +
                "  \"description\": \"This is a simple pull request to add a new file\",\n" +
                "  \"state\": \"OPEN\"\n}");
        assertEquals("Add a file", ParserUtils.getPullRequestTitle(node));

        assertEquals("", ParserUtils.getPullRequestTitle(MissingNode.getInstance()));
    }

    @Test
    public void testGetPullRequestNumber() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"id\": 6,\n" +
                "  \"title\": \"Add a file\",\n" +
                "  \"description\": \"This is a simple pull request to add a new file\",\n" +
                "  \"state\": \"OPEN\"\n}");
        assertEquals(Integer.valueOf(6), ParserUtils.getPullRequestNumber(node));
        assertEquals(Integer.valueOf(0), ParserUtils.getPullRequestNumber(MissingNode.getInstance()));
    }

    @Test
    public void testGetToReference() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "\"toRef\": {\n" +
                "    \"id\": \"refs/heads/master\",\n" +
                "    \"displayId\": \"master\",\n" +
                "    \"repository\": {\n" +
                "      \"slug\": \"test-repo\",\n" +
                "      \"id\": 2570,\n" +
                "      \"name\": \"test-repo\",\n" +
                "      \"scmId\": \"git\",\n" +
                "      \"state\": \"AVAILABLE\",\n" +
                "      \"statusMessage\": \"Available\",\n" +
                "      \"forkable\": true\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
        assertEquals("test-repo:refs/heads/master", ParserUtils.getToReference(node));
        assertEquals(":", ParserUtils.getToReference(MissingNode.getInstance()));
    }

    @Test
    public void testGetFromReference() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "\"fromRef\": {\n" +
                "    \"id\": \"refs/heads/develop\",\n" +
                "    \"displayId\": \"develop\",\n" +
                "    \"repository\": {\n" +
                "      \"slug\": \"test-repo\",\n" +
                "      \"id\": 2570,\n" +
                "      \"name\": \"test-repo\",\n" +
                "      \"scmId\": \"git\",\n" +
                "      \"state\": \"AVAILABLE\",\n" +
                "      \"statusMessage\": \"Available\",\n" +
                "      \"forkable\": true\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
        assertEquals("test-repo:refs/heads/develop", ParserUtils.getFromReference(node));
        assertEquals(":", ParserUtils.getFromReference(MissingNode.getInstance()));
    }

    @Test
    public void testGetCreatedDateTime() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"id\": 6,\n" +
                "  \"createdDate\": 1494867470989,\n" +
                "  \"updatedDate\": 1494867550989,\n" +
                "  \"state\": \"OPEN\"\n}");
        assertEquals("2017-05-15T17:57:50+01:00", ParserUtils.getCreatedDateTime(node));
        assertEquals("", ParserUtils.getCreatedDateTime(MissingNode.getInstance()));
    }

    @Test
    public void testGetUpdateDateTime() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"id\": 6,\n" +
                "  \"createdDate\": 1494867470989,\n" +
                "  \"updatedDate\": 1494867550989,\n" +
                "  \"state\": \"OPEN\"\n}");
        assertEquals("2017-05-15T17:59:10+01:00", ParserUtils.getUpdatedDateTime(node));
        assertEquals("", ParserUtils.getUpdatedDateTime(MissingNode.getInstance()));
    }

    @Test
    public void testGetLink() throws IOException, URISyntaxException {
        JsonNode node = JsonUtils.readTree("{\n" +
                "  \"id\": 6,\n" +
                "  \"state\": \"OPEN\",\n" +
                "  \"links\": {\n" +
                "    \"self\": [\n" +
                "      {\n" +
                "        \"href\": \"https://bitbucket.example.com/projects/SYMP/repos/test-repo/pull-requests/6\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        assertEquals(new URI("https://bitbucket.example.com/projects/SYMP/repos/test-repo/pull-requests/6"), ParserUtils.getLink(node));
        assertNull(ParserUtils.getLink(MissingNode.getInstance()));
    }

    @Test
    public void testApproversAsMentions() throws IOException {
        JsonNode node = JsonUtils.readTree("{\n   \"reviewers\": [{\n" +
                "    \"user\": {\n" +
                "      \"name\": \"Fred\",\n" +
                "      \"emailAddress\": \"fred.smith@example.com\"\n" +
                "    },\n" +
                "    \"role\": \"REVIEWER\",\n" +
                "    \"approved\": false,\n" +
                "    \"status\": \"UNAPPROVED\"\n" +
                "  }," + "{\n" +
                "  \"user\": {\n" +
                "      \"name\": \"Robert\",\n" +
                "      \"emailAddress\": \"robert.johnson@example.com\"\n" +
                "    },\n" +
                "    \"role\": \"REVIEWER\",\n" +
                "    \"approved\": false,\n" +
                "    \"status\": \"UNAPPROVED\"\n" +
                "  }," + "{\n" +
                "  \"user\": {\n" +
                "      \"name\": \"Sophie\",\n" +
                "      \"emailAddress\": \"sophie.muller@example.com\"\n" +
                "    },\n" +
                "    \"role\": \"REVIEWER\",\n" +
                "    \"approved\": true,\n" +
                "    \"status\": \"APPROVED\"\n" +
                "  }]\n" +
                "}");
        assertEquals("<mention email=\"fred.smith@example.com\"/>,<mention email=\"robert.johnson@example.com\"/>,",
                ParserUtils.getApproversAsMentions(node));
        assertEquals("", ParserUtils.getApproversAsMentions(MissingNode.getInstance()));
    }

}