/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.content;


public class ContentResolverTest extends android.test.AndroidTestCase {
    private android.content.ContentResolver mContentResolver;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mContentResolver = mContext.getContentResolver();
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCursorFinalizer() throws java.lang.Exception {
        // TODO: Want a test case that more predictably reproduce this issue. Selected
        // 600 as this causes the problem 100% of the runs on current hw, it might not
        // do so on some other configuration though.
        for (int i = 0; i < 600; i++) {
            mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }
    }
}

