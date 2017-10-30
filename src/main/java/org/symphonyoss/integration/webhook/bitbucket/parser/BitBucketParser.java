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

import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;
import org.symphonyoss.integration.webhook.parser.WebHookParser;

/**
 * Interface which defines methods to parse incoming Bitbucket messages.
 */
public interface BitBucketParser extends WebHookParser {
    /**
     * Returns the message resulting from parsing the webhook payload.
     * In a MessageML Version 1 format, just including the text
     *
     * @param payload Webhook payload
     * @return message resulting from the payload parsing
     */
    Message parseForMlV1(WebHookPayload payload) throws WebHookParseException;
}
