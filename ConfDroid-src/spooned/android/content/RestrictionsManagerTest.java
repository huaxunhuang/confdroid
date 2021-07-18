/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */
package android.content;


public class RestrictionsManagerTest extends android.test.AndroidTestCase {
    private android.content.RestrictionsManager mRm;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mRm = ((android.content.RestrictionsManager) (mContext.getSystemService(android.content.Context.RESTRICTIONS_SERVICE)));
    }

    public void testGetManifestRestrictions() {
        java.lang.String packageName = getContext().getPackageName();
        java.util.List<android.content.RestrictionEntry> manifestRestrictions = mRm.getManifestRestrictions(packageName);
        assertEquals(6, manifestRestrictions.size());
        java.util.Set<java.lang.String> verifiedKeys = new java.util.HashSet<>(java.util.Arrays.asList("bundle_key", "bundle_array_key", "bundle_array_bundle_key"));
        for (android.content.RestrictionEntry entry : manifestRestrictions) {
            if ("bundle_key".equals(entry.getKey())) {
                assertEquals("bundle_key entry should have 2 children entries", 2, entry.getRestrictions().length);
                verifiedKeys.remove(entry.getKey());
            } else
                if ("bundle_array_key".equals(entry.getKey())) {
                    assertEquals("bundle_array_key should have 2 children entries", 2, entry.getRestrictions().length);
                    assertNotNull(entry.getRestrictions());
                    for (android.content.RestrictionEntry childEntry : entry.getRestrictions()) {
                        if ("bundle_array_bundle_key".equals(childEntry.getKey())) {
                            assertNotNull(childEntry.getRestrictions());
                            assertEquals("bundle_array_bundle_key should have 1 child entry", 1, childEntry.getRestrictions().length);
                            verifiedKeys.remove(childEntry.getKey());
                        }
                    }
                    verifiedKeys.remove(entry.getKey());
                }

        }
        assertTrue(("Entries" + verifiedKeys) + " were not found", verifiedKeys.isEmpty());
    }

    public void testConvertRestrictionsToBundle() {
        java.lang.String packageName = getContext().getPackageName();
        java.util.List<android.content.RestrictionEntry> manifestRestrictions = mRm.getManifestRestrictions(packageName);
        android.os.Bundle bundle = android.content.RestrictionsManager.convertRestrictionsToBundle(manifestRestrictions);
        assertEquals(6, bundle.size());
        android.os.Bundle childBundle = bundle.getBundle("bundle_key");
        assertNotNull(childBundle);
        assertEquals(2, childBundle.size());
        android.os.Parcelable[] childBundleArray = bundle.getParcelableArray("bundle_array_key");
        assertEquals(2, childBundleArray.length);
    }
}

