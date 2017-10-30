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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.symphonyoss.integration.model.config.IntegrationSettings;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.bitbucket.BitBucketParserConstants;
import org.symphonyoss.integration.webhook.parser.WebHookParser;
import org.symphonyoss.integration.webhook.parser.WebHookParserFactory;

/**
 * Provides the relevant {@link WebHookParser} to process the provided {@link WebHookPayload}.
 */
@Component
public class BitBucketParserFactory implements WebHookParserFactory {
    private DefaultBitBucketParser defaultParser = new DefaultBitBucketParser();

    private Map<String, BitBucketParser> parserMap = new HashMap<>();

    @Autowired
    public BitBucketParserFactory(List<BitBucketParser> parsers) {
        for (BitBucketParser parser : parsers) {
            if (parser instanceof DefaultBitBucketParser) {
                this.defaultParser = (DefaultBitBucketParser) parser;
            }
            for (String event : parser.getEvents()) {
                this.parserMap.put(event, parser);
            }
        }
    }

    @Override
    public boolean accept(final MessageMLVersion version) {
        return MessageMLVersion.V2.equals(version);
    }

    @Override
    public void onConfigChange(final IntegrationSettings settings) {
        // Do nothing
    }

    @Override
    public WebHookParser getParser(final WebHookPayload payload) {
        String headerEvent = payload.getHeaders().get(BitBucketParserConstants.BITBUCKET_HEADER_EVENT_NAME);
        BitBucketParser eventParser = parserMap.get(headerEvent);
        if (eventParser != null) {
            return eventParser;
        }
        return defaultParser;
    }
}
