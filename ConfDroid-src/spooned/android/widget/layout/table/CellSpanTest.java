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
 * {@link android.widget.layout.table.CellSpan} is
 * setup to exercise tables in which cells use spanning.
 */
public class CellSpanTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.table.CellSpan> {
    private android.view.View mA;

    private android.view.View mB;

    private android.view.View mC;

    private android.view.View mSpanThenCell;

    private android.view.View mCellThenSpan;

    private android.view.View mSpan;

    public CellSpanTest() {
        super("com.android.frameworks.coretests", android.widget.layout.table.CellSpan.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.layout.table.CellSpan activity = getActivity();
        mA = activity.findViewById(R.id.a);
        mB = activity.findViewById(R.id.b);
        mC = activity.findViewById(R.id.c);
        mSpanThenCell = activity.findViewById(R.id.spanThenCell);
        mCellThenSpan = activity.findViewById(R.id.cellThenSpan);
        mSpan = activity.findViewById(R.id.span);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mA);
        assertNotNull(mB);
        assertNotNull(mC);
        assertNotNull(mSpanThenCell);
        assertNotNull(mCellThenSpan);
        assertNotNull(mSpan);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSpanThenCell() throws java.lang.Exception {
        int spanWidth = mA.getMeasuredWidth() + mB.getMeasuredWidth();
        assertEquals("span followed by cell is broken", spanWidth, mSpanThenCell.getMeasuredWidth());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testCellThenSpan() throws java.lang.Exception {
        int spanWidth = mB.getMeasuredWidth() + mC.getMeasuredWidth();
        assertEquals("cell followed by span is broken", spanWidth, mCellThenSpan.getMeasuredWidth());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSpan() throws java.lang.Exception {
        int spanWidth = (mA.getMeasuredWidth() + mB.getMeasuredWidth()) + mC.getMeasuredWidth();
        assertEquals("span is broken", spanWidth, mSpan.getMeasuredWidth());
    }
}

