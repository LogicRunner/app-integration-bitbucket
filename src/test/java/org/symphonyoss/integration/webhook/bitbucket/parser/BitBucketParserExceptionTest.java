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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test Cases for {@link BitBucketParserException}.
 */
public class BitBucketParserExceptionTest {
    private static final String EXPECTED_ERROR_MESSAGE = "Component: BitBucket Webhook Dispatcher" +
            "Message: Error Message" +
            "Solutions: " +
            "No solution has been cataloged for troubleshooting this problem.";

    @Test
    public void testBitBucketParserExceptionMessage() {
        BitBucketParserException bitBucketParserException = new BitBucketParserException("Error Message");
        assertNotNull(bitBucketParserException);
        assertEquals(EXPECTED_ERROR_MESSAGE,
                bitBucketParserException.getMessage().replaceAll("\\r|\\n", ""));
    }

    @Test
    public void testBitBucketParserExceptionMessageAndException() {
        BitBucketParserException bitBucketParserException =
                new BitBucketParserException("Error Message", new Exception("UnderlyingException"));
        assertNotNull(bitBucketParserException);
        assertEquals(EXPECTED_ERROR_MESSAGE + "Stack trace: UnderlyingException",
                bitBucketParserException.getMessage().replaceAll("\\r|\\n", ""));
    }
}