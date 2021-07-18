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
 * {@link android.widget.layout.table.Weight} is
 * setup to exercise tables in which cells use a weight.
 */
public class WeightTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.table.Weight> {
    private android.view.View mCell1;

    private android.view.View mCell2;

    private android.view.View mCell3;

    private android.view.View mRow;

    public WeightTest() {
        super("com.android.frameworks.coretests", android.widget.layout.table.Weight.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.layout.table.Weight activity = getActivity();
        mCell1 = activity.findViewById(R.id.cell1);
        mCell3 = activity.findViewById(R.id.cell2);
        mCell2 = activity.findViewById(R.id.cell3);
        mRow = activity.findViewById(R.id.row);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mCell1);
        assertNotNull(mCell2);
        assertNotNull(mCell3);
        assertNotNull(mRow);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAllCellsFillParent() throws java.lang.Exception {
        assertEquals((mCell1.getWidth() + mCell2.getWidth()) + mCell3.getWidth(), mRow.getWidth());
    }
}

