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
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;

/**
 * Default {@link BitBucketParser} which returns a null message.
 *
 * <p>This is used as a default parser to ignore any events which the other parsers cannot process.
 */
@Component
public class DefaultBitBucketParser implements BitBucketParser {
    @Override
    public List<String> getEvents() {
        return Collections.emptyList();
    }

    @Override
    public Message parse(final WebHookPayload payload) throws WebHookParseException {
        return null;
    }

    @Override
    public Message parseForMlV1(final WebHookPayload payload) throws WebHookParseException {
        return null;
    }
}
