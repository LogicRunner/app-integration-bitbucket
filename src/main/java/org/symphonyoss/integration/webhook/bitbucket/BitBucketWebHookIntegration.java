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
package org.symphonyoss.integration.webhook.bitbucket;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.symphonyoss.integration.model.config.IntegrationSettings;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.webhook.WebHookIntegration;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.bitbucket.parser.BitBucketParserFactory;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;

/**
 * Implementation of
 */
@Component("bitbucket")
public class BitBucketWebHookIntegration extends WebHookIntegration {

    private BitBucketParserFactory parserFactory;

    @Autowired
    public BitBucketWebHookIntegration(BitBucketParserFactory factory) {
        this.parserFactory = factory;
    }

    @PostConstruct
    public void init() {
    }

    /**
     * Parse the message received from Bitbucket.
     *
     * @param input Payload received from the Bitbucket feed.
     * @return Message to the posted
     * @throws WebHookParseException Failed to parse the incoming payload
     */
    @Override
    public Message parse(WebHookPayload input) throws WebHookParseException {
        return parserFactory.getParser(input).parse(input);
    }

    /**
     * Callback to update the integration settings in the parser classes.
     *
     * @param settings Integration settings
     */
    @Override
    public void onConfigChange(IntegrationSettings settings) {
        super.onConfigChange(settings);
        parserFactory.onConfigChange(settings);
    }

    /**
     * @see WebHookIntegration#getSupportedContentTypes()
     */
    @Override
    public List<MediaType> getSupportedContentTypes() {
        List<MediaType> supportedContentTypes = new ArrayList<>();
        supportedContentTypes.add(MediaType.APPLICATION_JSON_TYPE);
        return supportedContentTypes;
    }
}
