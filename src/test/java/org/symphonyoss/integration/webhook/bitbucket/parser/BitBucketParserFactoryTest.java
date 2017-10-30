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

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.symphonyoss.integration.model.config.IntegrationSettings;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.bitbucket.BitBucketParserConstants;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test caces for {@link BitBucketParserFactory}.
 */
@RunWith(MockitoJUnitRunner.class)
public class BitBucketParserFactoryTest {
    @Mock
    private BitBucketParser createParser;
    @Mock
    private DefaultBitBucketParser defaultParser;
    @Mock
    private WebHookPayload createdPayload;
    @Mock
    private WebHookPayload unknownPayload;

    private BitBucketParserFactory factory;

    @Before
    public void setup() {
        Message createdMessage = new Message();
        when(createParser.getEvents()).thenReturn(Collections.singletonList(BitBucketParserConstants.BITBUCKET_CREATE_EVENT));
        when(createParser.parse(any(WebHookPayload.class))).thenReturn(createdMessage);

        when(defaultParser.getEvents()).thenReturn(Collections.<String>emptyList());
        when(defaultParser.parse(any(WebHookPayload.class))).thenReturn(null);

        when(createdPayload.getHeaders()).thenReturn(
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, BitBucketParserConstants.BITBUCKET_CREATE_EVENT));
        when(unknownPayload.getHeaders()).thenReturn(
                Collections.singletonMap(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME, "NEW_EVENT"));

        factory = new BitBucketParserFactory(Arrays.asList(createParser, defaultParser));
    }

    @Test
    public void testOnlyProcessMlVersion2() {
        BitBucketParserFactory factory = new BitBucketParserFactory(Collections.<BitBucketParser>emptyList());
        assertTrue(factory.accept(MessageMLVersion.V2));
        assertFalse(factory.accept(MessageMLVersion.V1));
        assertFalse(factory.accept(null));
    }

    @Test
    public void testMappingCreateEventToParser() {
        verify(createParser, times(1)).getEvents();
        verify(defaultParser, times(1)).getEvents();

        assertEquals(createParser, factory.getParser(createdPayload));
    }

    @Test
    public void testMappingUnknownEventToDefaultParser() {
        verify(createParser, times(1)).getEvents();
        verify(defaultParser, times(1)).getEvents();

        assertEquals(defaultParser, factory.getParser(unknownPayload));
    }

    @Test
    public void testFindingDefaultParserIfNotSentIn() {
        BitBucketParserFactory factory = new BitBucketParserFactory(Collections.<BitBucketParser>emptyList());
        assertThat(factory.getParser(unknownPayload), instanceOf(DefaultBitBucketParser.class));
    }

    @Test
    public void testOnConfigChangeDoesNothing() {
        IntegrationSettings integrationSettings = mock(IntegrationSettings.class);
        factory.onConfigChange(integrationSettings);
        verifyZeroInteractions(integrationSettings);
    }
}