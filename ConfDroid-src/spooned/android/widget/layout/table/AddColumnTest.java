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
 * {@link android.widget.layout.table.AddColumn} is
 * setup to exercise the case of adding row programmatically in a table.
 */
public class AddColumnTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.table.AddColumn> {
    private android.widget.Button mAddRow;

    private android.widget.TableLayout mTable;

    public AddColumnTest() {
        super("com.android.frameworks.coretests", android.widget.layout.table.AddColumn.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.layout.table.AddColumn activity = getActivity();
        mAddRow = ((android.widget.Button) (activity.findViewById(R.id.add_row_button)));
        mTable = ((android.widget.TableLayout) (activity.findViewById(R.id.table)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mAddRow);
        assertNotNull(mTable);
        assertTrue(mAddRow.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testWidths() throws java.lang.Exception {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();
        android.widget.TableRow row1 = ((android.widget.TableRow) (mTable.getChildAt(0)));
        android.widget.TableRow row2 = ((android.widget.TableRow) (mTable.getChildAt(1)));
        assertTrue(row1.getChildCount() < row2.getChildCount());
        for (int i = 0; i < row1.getChildCount(); i++) {
            assertEquals(row2.getChildAt(i).getWidth(), row1.getChildAt(i).getWidth());
        }
    }
}

