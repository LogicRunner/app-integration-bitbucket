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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link DefaultBitBucketParser}.
 */
public class DefaultBitBucketParserTest {
    @Test
    public void getEventsIsEmpty() {
        DefaultBitBucketParser defaultBitBucketParser = new DefaultBitBucketParser();
        assertEquals(Collections.<String>emptyList(), defaultBitBucketParser.getEvents());
    }

    @Test
    public void parseAlwaysGivesNull() {
        DefaultBitBucketParser defaultBitBucketParser = new DefaultBitBucketParser();
        assertNull(defaultBitBucketParser.parse(null));
        assertNull(defaultBitBucketParser.parseForMlV1(null));
    }
}