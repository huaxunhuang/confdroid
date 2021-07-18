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


/**
 * Tests the android.content.pm.VerificationParams class
 *
 * To test run:
 * ./development/testrunner/runtest.py frameworks-core -c android.content.pm.VerificationParamsTest
 */
public class VerificationParamsTest extends android.test.AndroidTestCase {
    private static final java.lang.String VERIFICATION_URI_STRING = "http://verification.uri/path";

    private static final java.lang.String ORIGINATING_URI_STRING = "http://originating.uri/path";

    private static final java.lang.String REFERRER_STRING = "http://referrer.uri/path";

    private static final byte[] DIGEST_BYTES = "fake digest".getBytes();

    private static final int INSTALLER_UID = 42;

    private static final android.net.Uri VERIFICATION_URI = android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING);

    private static final android.net.Uri ORIGINATING_URI = android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING);

    private static final android.net.Uri REFERRER = android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING);

    private static final int ORIGINATING_UID = 10042;

    private static final android.content.pm.ManifestDigest MANIFEST_DIGEST = new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES);

    public void testParcel() throws java.lang.Exception {
        android.content.pm.VerificationParams expected = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.os.Parcel parcel = android.os.Parcel.obtain();
        expected.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        android.content.pm.VerificationParams actual = this.CREATOR.createFromParcel(parcel);
        assertEquals(android.content.pm.VerificationParamsTest.VERIFICATION_URI, actual.getVerificationURI());
        assertEquals(android.content.pm.VerificationParamsTest.ORIGINATING_URI, actual.getOriginatingURI());
        assertEquals(android.content.pm.VerificationParamsTest.REFERRER, actual.getReferrer());
        assertEquals(android.content.pm.VerificationParamsTest.ORIGINATING_UID, actual.getOriginatingUid());
        assertEquals(android.content.pm.VerificationParamsTest.MANIFEST_DIGEST, actual.getManifestDigest());
    }

    public void testEquals_Success() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertEquals(params1, params2);
    }

    public void testEquals_VerificationUri_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse("http://a.different.uri/"), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.equals(params2));
    }

    public void testEquals_OriginatingUri_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse("http://a.different.uri/"), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.equals(params2));
    }

    public void testEquals_Referrer_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse("http://a.different.uri/"), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.equals(params2));
    }

    public void testEquals_Originating_Uid_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), 12345, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.equals(params2));
    }

    public void testEquals_ManifestDigest_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest("a different digest".getBytes()));
        assertFalse(params1.equals(params2));
    }

    public void testEquals_InstallerUid_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        params2.setInstallerUid(android.content.pm.VerificationParamsTest.INSTALLER_UID);
        assertFalse(params1.equals(params2));
    }

    public void testHashCode_Success() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertEquals(params1.hashCode(), params2.hashCode());
    }

    public void testHashCode_VerificationUri_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(null, android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.hashCode() == params2.hashCode());
    }

    public void testHashCode_OriginatingUri_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse("http://a.different.uri/"), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.hashCode() == params2.hashCode());
    }

    public void testHashCode_Referrer_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), null, android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.hashCode() == params2.hashCode());
    }

    public void testHashCode_Originating_Uid_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), 12345, new android.content.pm.ManifestDigest(android.content.pm.VerificationParamsTest.DIGEST_BYTES));
        assertFalse(params1.hashCode() == params2.hashCode());
    }

    public void testHashCode_ManifestDigest_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest("a different digest".getBytes()));
        assertFalse(params1.hashCode() == params2.hashCode());
    }

    public void testHashCode_InstallerUid_Failure() throws java.lang.Exception {
        android.content.pm.VerificationParams params1 = new android.content.pm.VerificationParams(android.content.pm.VerificationParamsTest.VERIFICATION_URI, android.content.pm.VerificationParamsTest.ORIGINATING_URI, android.content.pm.VerificationParamsTest.REFERRER, android.content.pm.VerificationParamsTest.ORIGINATING_UID, android.content.pm.VerificationParamsTest.MANIFEST_DIGEST);
        android.content.pm.VerificationParams params2 = new android.content.pm.VerificationParams(android.net.Uri.parse(android.content.pm.VerificationParamsTest.VERIFICATION_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.ORIGINATING_URI_STRING), android.net.Uri.parse(android.content.pm.VerificationParamsTest.REFERRER_STRING), android.content.pm.VerificationParamsTest.ORIGINATING_UID, new android.content.pm.ManifestDigest("a different digest".getBytes()));
        params2.setInstallerUid(android.content.pm.VerificationParamsTest.INSTALLER_UID);
        assertFalse(params1.hashCode() == params2.hashCode());
    }
}

