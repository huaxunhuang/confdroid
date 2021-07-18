/**
 * Copyright (C) 2011 The Android Open Source Project
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


public class ManifestDigestTest extends android.test.AndroidTestCase {
    private static final byte[] MESSAGE_1 = new byte[]{ ((byte) (0x0)), ((byte) (0xaa)), ((byte) (0x55)), ((byte) (0xff)) };

    public void testManifestDigest_FromInputStream_Null() {
        assertNull("Attributes were null, so ManifestDigest.fromAttributes should return null", android.content.pm.ManifestDigest.fromInputStream(null));
    }

    public void testManifestDigest_FromInputStream_ThrowsIoException() {
        java.io.InputStream is = new java.io.InputStream() {
            @java.lang.Override
            public int read() throws java.io.IOException {
                throw new java.io.IOException();
            }
        };
        assertNull("InputStream threw exception, so ManifestDigest should be null", android.content.pm.ManifestDigest.fromInputStream(is));
    }

    public void testManifestDigest_Equals() throws java.lang.Exception {
        java.io.InputStream is = new java.io.ByteArrayInputStream(android.content.pm.ManifestDigestTest.MESSAGE_1);
        android.content.pm.ManifestDigest expected = new android.content.pm.ManifestDigest(java.security.MessageDigest.getInstance("SHA-256").digest(android.content.pm.ManifestDigestTest.MESSAGE_1));
        android.content.pm.ManifestDigest actual = android.content.pm.ManifestDigest.fromInputStream(is);
        assertEquals(expected, actual);
        android.content.pm.ManifestDigest unexpected = new android.content.pm.ManifestDigest(new byte[0]);
        assertFalse(unexpected.equals(actual));
    }

    public void testManifestDigest_Parcel() throws java.lang.Exception {
        java.io.InputStream is = new java.io.ByteArrayInputStream(android.content.pm.ManifestDigestTest.MESSAGE_1);
        android.content.pm.ManifestDigest digest = android.content.pm.ManifestDigest.fromInputStream(is);
        android.os.Parcel p = android.os.Parcel.obtain();
        digest.writeToParcel(p, 0);
        p.setDataPosition(0);
        android.content.pm.ManifestDigest fromParcel = this.CREATOR.createFromParcel(p);
        assertEquals((("ManifestDigest going through parceling should be the same as before: " + digest.toString()) + " and ") + fromParcel.toString(), digest, fromParcel);
    }
}

