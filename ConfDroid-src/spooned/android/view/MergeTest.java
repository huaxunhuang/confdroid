/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.view;


public class MergeTest extends android.test.ActivityInstrumentationTestCase<android.view.Merge> {
    public MergeTest() {
        super("com.android.frameworks.coretests", android.view.Merge.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testMerged() throws java.lang.Exception {
        final android.view.Merge activity = getActivity();
        final android.view.ViewGroup layout = activity.getLayout();
        assertEquals("The layout wasn't merged", 7, layout.getChildCount());
    }
}

