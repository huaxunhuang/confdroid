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


public class ViewStubTest extends android.test.ActivityInstrumentationTestCase<android.view.StubbedView> {
    public ViewStubTest() {
        super("com.android.frameworks.coretests", android.view.StubbedView.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testStubbed() throws java.lang.Exception {
        final android.view.StubbedView activity = getActivity();
        final android.view.View stub = activity.findViewById(R.id.viewStub);
        assertNotNull("The ViewStub does not exist", stub);
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testInflated() throws java.lang.Exception {
        final android.view.StubbedView activity = getActivity();
        final android.view.ViewStub stub = ((android.view.ViewStub) (activity.findViewById(R.id.viewStub)));
        final android.view.View swapped = stub.inflate();
        assertNotNull("The inflated view is null", swapped);
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testInflatedId() throws java.lang.Exception {
        final android.view.StubbedView activity = getActivity();
        final android.view.ViewStub stub = ((android.view.ViewStub) (activity.findViewById(R.id.viewStubWithId)));
        final android.view.View swapped = stub.inflate();
        assertNotNull("The inflated view is null", swapped);
        assertTrue("The inflated view has no id", swapped.getId() != android.view.View.NO_ID);
        assertTrue("The inflated view has the wrong id", swapped.getId() == R.id.stub_inflated);
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testInflatedLayoutParams() throws java.lang.Exception {
        final android.view.StubbedView activity = getActivity();
        final android.view.ViewStub stub = ((android.view.ViewStub) (activity.findViewById(R.id.viewStubWithId)));
        final android.view.View swapped = stub.inflate();
        assertNotNull("The inflated view is null", swapped);
        assertEquals("Both stub and inflated should same width", stub.getLayoutParams().width, swapped.getLayoutParams().width);
        assertEquals("Both stub and inflated should same height", stub.getLayoutParams().height, swapped.getLayoutParams().height);
    }
}

