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
package android.widget.gridview;


public class GridPaddingTest extends android.test.ActivityInstrumentationTestCase2<android.widget.gridview.GridPadding> {
    private android.widget.GridView mGridView;

    public GridPaddingTest() {
        super("com.android.frameworks.coretests", android.widget.gridview.GridPadding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mGridView = getActivity().getGridView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mGridView);
        assertTrue("Not in touch mode", mGridView.isInTouchMode());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testResurrectSelection() {
        sendKeys("DPAD_DOWN");
        assertEquals("The first item should be selected", mGridView.getSelectedItemPosition(), 0);
    }
}

