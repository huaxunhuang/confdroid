/**
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.content.pm;


public class MacAuthenticatedInputStreamTest extends android.test.AndroidTestCase {
    private static final javax.crypto.SecretKey HMAC_KEY_1 = new javax.crypto.spec.SecretKeySpec("test_key_1".getBytes(), "HMAC");

    private static final byte[] TEST_STRING_1 = "Hello, World!".getBytes();

    /**
     * Generated with:
     *
     * echo -n 'Hello, World!' | openssl dgst -hmac 'test_key_1' -binary -sha1 | recode ..//x1 |
     *   sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] TEST_STRING_1_MAC = new byte[]{ ((byte) (0x29)), ((byte) (0xb1)), ((byte) (0x87)), ((byte) (0x6b)), ((byte) (0xfe)), ((byte) (0x83)), ((byte) (0x96)), ((byte) (0x51)), ((byte) (0x61)), ((byte) (0x2)), ((byte) (0xaf)), ((byte) (0x7b)), ((byte) (0xba)), ((byte) (0x5)), ((byte) (0xe6)), ((byte) (0xa4)), ((byte) (0xab)), ((byte) (0x36)), ((byte) (0x18)), ((byte) (0x2)) };

    /**
     * Same as TEST_STRING_1_MAC but with the first byte as 0x28 instead of
     * 0x29.
     */
    private static final byte[] TEST_STRING_1_MAC_BROKEN = new byte[]{ ((byte) (0x28)), ((byte) (0xb1)), ((byte) (0x87)), ((byte) (0x6b)), ((byte) (0xfe)), ((byte) (0x83)), ((byte) (0x96)), ((byte) (0x51)), ((byte) (0x61)), ((byte) (0x2)), ((byte) (0xaf)), ((byte) (0x7b)), ((byte) (0xba)), ((byte) (0x5)), ((byte) (0xe6)), ((byte) (0xa4)), ((byte) (0xab)), ((byte) (0x36)), ((byte) (0x18)), ((byte) (0x2)) };

    private java.io.ByteArrayInputStream mTestStream1;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mTestStream1 = new java.io.ByteArrayInputStream(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1);
    }

    public void testString1Authenticate_Success() throws java.lang.Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HMAC-SHA1");
        mac.init(android.content.pm.MacAuthenticatedInputStreamTest.HMAC_KEY_1);
        android.content.pm.MacAuthenticatedInputStream is = new android.content.pm.MacAuthenticatedInputStream(mTestStream1, mac);
        assertTrue(java.util.Arrays.equals(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1, libcore.io.Streams.readFully(is)));
        assertTrue(is.isTagEqual(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1_MAC));
    }

    public void testString1Authenticate_WrongTag_Failure() throws java.lang.Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HMAC-SHA1");
        mac.init(android.content.pm.MacAuthenticatedInputStreamTest.HMAC_KEY_1);
        android.content.pm.MacAuthenticatedInputStream is = new android.content.pm.MacAuthenticatedInputStream(mTestStream1, mac);
        assertTrue(java.util.Arrays.equals(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1, libcore.io.Streams.readFully(is)));
        assertFalse(is.isTagEqual(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1_MAC_BROKEN));
    }

    public void testString1Authenticate_NullTag_Failure() throws java.lang.Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HMAC-SHA1");
        mac.init(android.content.pm.MacAuthenticatedInputStreamTest.HMAC_KEY_1);
        android.content.pm.MacAuthenticatedInputStream is = new android.content.pm.MacAuthenticatedInputStream(mTestStream1, mac);
        assertTrue(java.util.Arrays.equals(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1, libcore.io.Streams.readFully(is)));
        assertFalse(is.isTagEqual(null));
    }

    public void testString1Authenticate_ReadSingleByte_Success() throws java.lang.Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HMAC-SHA1");
        mac.init(android.content.pm.MacAuthenticatedInputStreamTest.HMAC_KEY_1);
        android.content.pm.MacAuthenticatedInputStream is = new android.content.pm.MacAuthenticatedInputStream(mTestStream1, mac);
        int numRead = 0;
        while (is.read() != (-1)) {
            numRead++;
            if (numRead > android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1.length) {
                fail("read too many bytes");
            }
        } 
        assertEquals(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1.length, numRead);
        assertTrue(is.isTagEqual(android.content.pm.MacAuthenticatedInputStreamTest.TEST_STRING_1_MAC));
    }
}

