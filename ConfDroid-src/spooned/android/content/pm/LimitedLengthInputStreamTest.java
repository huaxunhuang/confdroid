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


public class LimitedLengthInputStreamTest extends android.test.AndroidTestCase {
    private final byte[] TEST_STRING1 = "This is a test".getBytes();

    private java.io.InputStream mTestStream1;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mTestStream1 = new java.io.ByteArrayInputStream(TEST_STRING1);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testConstructor_NegativeOffset_Failure() throws java.lang.Exception {
        try {
            java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, -1, TEST_STRING1.length);
            fail("Should throw IOException on negative index");
        } catch (java.io.IOException e) {
            // success
        }
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testConstructor_NegativeLength_Failure() throws java.lang.Exception {
        try {
            java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, 0, -1);
            fail("Should throw IOException on negative length");
        } catch (java.io.IOException e) {
            // success
        }
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testConstructor_NullInputStream_Failure() throws java.lang.Exception {
        try {
            java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(null, 0, 1);
            fail("Should throw IOException on null input stream");
        } catch (java.io.IOException e) {
            // success
        }
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testConstructor_OffsetLengthOverflow_Fail() throws java.lang.Exception {
        try {
            java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, java.lang.Long.MAX_VALUE - 1, java.lang.Long.MAX_VALUE - 1);
            fail("Should fail when offset + length is > Long.MAX_VALUE");
        } catch (java.io.IOException e) {
            // success
        }
    }

    private void checkReadBytesWithOffsetAndLength_WithString1(int offset, int length) throws java.lang.Exception {
        byte[] temp = new byte[TEST_STRING1.length];
        byte[] expected = new byte[length];
        byte[] actual = new byte[length];
        java.lang.System.arraycopy(TEST_STRING1, offset, expected, 0, length);
        java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, offset, length);
        assertEquals(length, is.read(temp, 0, temp.length));
        java.lang.System.arraycopy(temp, 0, actual, 0, length);
        assertTrue(java.util.Arrays.equals(expected, actual));
        assertEquals(-1, is.read(temp, 0, temp.length));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytesWithOffsetAndLength_ZeroOffset_PartialLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(0, 2);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytesWithOffsetAndLength_NonZeroOffset_PartialLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(3, 2);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytesWithOffsetAndLength_ZeroOffset_FullLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(0, TEST_STRING1.length);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytesWithOffsetAndLength_NonZeroOffset_FullLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(3, TEST_STRING1.length - 3);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytesWithOffsetAndLength_ZeroOffset_PastEnd_Success() throws java.lang.Exception {
        byte[] temp = new byte[TEST_STRING1.length + 10];
        java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, 0, TEST_STRING1.length + 10);
        assertEquals(TEST_STRING1.length, is.read(temp, 0, TEST_STRING1.length + 10));
        byte[] actual = new byte[TEST_STRING1.length];
        java.lang.System.arraycopy(temp, 0, actual, 0, actual.length);
        assertTrue(java.util.Arrays.equals(TEST_STRING1, actual));
    }

    private void checkReadBytes_WithString1(int offset, int length) throws java.lang.Exception {
        byte[] temp = new byte[TEST_STRING1.length];
        byte[] expected = new byte[length];
        byte[] actual = new byte[length];
        java.lang.System.arraycopy(TEST_STRING1, offset, expected, 0, length);
        java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, offset, length);
        assertEquals(length, is.read(temp));
        java.lang.System.arraycopy(temp, 0, actual, 0, length);
        assertTrue(java.util.Arrays.equals(expected, actual));
        assertEquals(-1, is.read(temp));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytes_ZeroOffset_PartialLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(0, 2);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytes_NonZeroOffset_PartialLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(3, 2);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytes_ZeroOffset_FullLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(0, TEST_STRING1.length);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testReadBytes_NonZeroOffset_FullLength_Success() throws java.lang.Exception {
        checkReadBytesWithOffsetAndLength_WithString1(3, TEST_STRING1.length - 3);
    }

    private void checkSingleByteRead_WithString1(int offset, int length) throws java.lang.Exception {
        java.io.InputStream is = new android.content.pm.LimitedLengthInputStream(mTestStream1, offset, length);
        for (int i = 0; i < length; i++) {
            assertEquals(TEST_STRING1[offset + i], is.read());
        }
        assertEquals(-1, is.read());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSingleByteRead_ZeroOffset_PartialLength_Success() throws java.lang.Exception {
        checkSingleByteRead_WithString1(0, 2);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSingleByteRead_NonZeroOffset_PartialLength_Success() throws java.lang.Exception {
        checkSingleByteRead_WithString1(3, 2);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSingleByteRead_ZeroOffset_FullLength_Success() throws java.lang.Exception {
        checkSingleByteRead_WithString1(0, TEST_STRING1.length);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSingleByteRead_NonZeroOffset_FullLength_Success() throws java.lang.Exception {
        checkSingleByteRead_WithString1(3, TEST_STRING1.length - 3);
    }
}

