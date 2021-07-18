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


/**
 * Exercises {@link android.view.View}'s tags property.
 */
public class SetTagsTest extends android.test.ActivityInstrumentationTestCase2<android.view.Disabled> {
    private android.widget.Button mView;

    public SetTagsTest() {
        super("com.android.frameworks.coretests", android.view.Disabled.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        mView = ((android.widget.Button) (getActivity().findViewById(R.id.disabledButton)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetTag() throws java.lang.Exception {
        mView.setTag("1");
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGetTag() throws java.lang.Exception {
        java.lang.Object o = new java.lang.Object();
        mView.setTag(o);
        final java.lang.Object stored = mView.getTag();
        assertNotNull(stored);
        assertSame("The stored tag is inccorect", o, stored);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetTagWithKey() throws java.lang.Exception {
        mView.setTag(R.id.a, "2");
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGetTagWithKey() throws java.lang.Exception {
        java.lang.Object o = new java.lang.Object();
        mView.setTag(R.id.a, o);
        final java.lang.Object stored = mView.getTag(R.id.a);
        assertNotNull(stored);
        assertSame("The stored tag is inccorect", o, stored);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetTagWithFrameworkId() throws java.lang.Exception {
        boolean result = false;
        try {
            mView.setTag(android.R.id.list, "2");
        } catch (java.lang.IllegalArgumentException e) {
            result = true;
        }
        assertTrue("Setting a tag with a framework id did not throw an exception", result);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetTagWithNoPackageId() throws java.lang.Exception {
        boolean result = false;
        try {
            mView.setTag(0xaa, "2");
        } catch (java.lang.IllegalArgumentException e) {
            result = true;
        }
        assertTrue("Setting a tag with an id with no package did not throw an exception", result);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetTagInternalWithFrameworkId() throws java.lang.Exception {
        mView.setTagInternal(android.R.id.list, "2");
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetTagInternalWithApplicationId() throws java.lang.Exception {
        boolean result = false;
        try {
            mView.setTagInternal(R.id.a, "2");
        } catch (java.lang.IllegalArgumentException e) {
            result = true;
        }
        assertTrue("Setting a tag with an id with app package did not throw an exception", result);
    }
}

