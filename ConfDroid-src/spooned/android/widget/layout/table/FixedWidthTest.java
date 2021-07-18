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
package android.widget.layout.table;


/**
 * {@link android.widget.layout.table.FixedWidth} is
 * setup to exercise tables in which cells use fixed width and height.
 */
public class FixedWidthTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.table.FixedWidth> {
    private android.view.View mFixedWidth;

    private android.view.View mFixedHeight;

    private android.view.View mNonFixedWidth;

    public FixedWidthTest() {
        super("com.android.frameworks.coretests", android.widget.layout.table.FixedWidth.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.layout.table.FixedWidth activity = getActivity();
        mFixedWidth = activity.findViewById(R.id.fixed_width);
        mNonFixedWidth = activity.findViewById(R.id.non_fixed_width);
        mFixedHeight = activity.findViewById(R.id.fixed_height);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mFixedWidth);
        assertNotNull(mFixedHeight);
        assertNotNull(mNonFixedWidth);
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @MediumTest
    public void testFixedWidth() throws java.lang.Exception {
        assertEquals(150, mFixedWidth.getWidth());
        assertEquals(mFixedWidth.getWidth(), mNonFixedWidth.getWidth());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @MediumTest
    public void testFixedHeight() throws java.lang.Exception {
        assertEquals(48, mFixedHeight.getHeight());
    }
}

