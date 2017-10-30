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

import org.symphonyoss.integration.webhook.WebHookPayload;

/**
 * This class contains the constants used by the BitBucket parsers to access data in the {@link WebHookPayload}.
 */
public class BitBucketParserConstants {
    private BitBucketParserConstants() {
    }

    public static final String BITBUCKET_HEADER_EVENT_NAME = "x-bitbucket-event";
    public static final String BITBUCKET_HEADER_PROJECT = "x-bitbucket-project";
    public static final String BITBUCKET_HEADER_REPOSITORY = "x-bitbucket-repository";
    public static final String BITBUCKET_PULL_REQUEST_EVENT = "pull_request";
    public static final String BITBUCKET_PULL_REQUEST_COMMENTED_EVENT = "pull_request_commented";
    public static final String BITBUCKET_CREATE_EVENT = "create";
}
