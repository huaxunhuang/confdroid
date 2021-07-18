/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.testapp;


public class TableLayoutBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.TableLayoutAdapterTestBinding, android.databinding.testapp.vo.TableLayoutBindingObject> {
    android.widget.TableLayout mView;

    public TableLayoutBindingAdapterTest() {
        super(android.databinding.testapp.databinding.TableLayoutAdapterTestBinding.class, android.databinding.testapp.vo.TableLayoutBindingObject.class, R.layout.table_layout_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testDivider() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            junit.framework.TestCase.assertEquals(mBindingObject.getDivider(), ((android.graphics.drawable.ColorDrawable) (mView.getDividerDrawable())).getColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getDivider(), ((android.graphics.drawable.ColorDrawable) (mView.getDividerDrawable())).getColor());
        }
    }

    public void testColumns() throws java.lang.Throwable {
        junit.framework.TestCase.assertFalse(mView.isColumnCollapsed(0));
        junit.framework.TestCase.assertTrue(mView.isColumnCollapsed(1));
        junit.framework.TestCase.assertFalse(mView.isColumnCollapsed(2));
        junit.framework.TestCase.assertFalse(mView.isColumnShrinkable(0));
        junit.framework.TestCase.assertTrue(mView.isColumnShrinkable(1));
        junit.framework.TestCase.assertFalse(mView.isColumnShrinkable(2));
        junit.framework.TestCase.assertFalse(mView.isColumnStretchable(0));
        junit.framework.TestCase.assertTrue(mView.isColumnStretchable(1));
        junit.framework.TestCase.assertFalse(mView.isColumnStretchable(2));
        changeValues();
        junit.framework.TestCase.assertFalse(mView.isColumnCollapsed(0));
        junit.framework.TestCase.assertFalse(mView.isColumnCollapsed(1));
        junit.framework.TestCase.assertFalse(mView.isColumnCollapsed(2));
        junit.framework.TestCase.assertTrue(mView.isColumnShrinkable(0));
        junit.framework.TestCase.assertTrue(mView.isColumnShrinkable(1));
        junit.framework.TestCase.assertFalse(mView.isColumnShrinkable(2));
        junit.framework.TestCase.assertTrue(mView.isColumnStretchable(0));
        junit.framework.TestCase.assertTrue(mView.isColumnStretchable(1));
        junit.framework.TestCase.assertTrue(mView.isColumnStretchable(2));
    }
}

