/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.widget.layout.linear;


public class LinearLayoutEditTextsTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.linear.LinearLayoutEditTexts> {
    private android.view.View mChild;

    private android.view.View mContainer;

    public LinearLayoutEditTextsTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.LinearLayoutEditTexts.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.app.Activity activity = getActivity();
        mChild = activity.findViewById(R.id.editText1);
        mContainer = activity.findViewById(R.id.layout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mChild);
        assertNotNull(mContainer);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLayout() {
        final int childHeight = mChild.getHeight();
        final int containerHeight = mContainer.getHeight();
        assertEquals(containerHeight, childHeight);
    }
}

