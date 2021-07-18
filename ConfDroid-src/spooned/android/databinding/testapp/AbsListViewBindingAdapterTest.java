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


public class AbsListViewBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.AbsListViewAdapterTestBinding, android.databinding.testapp.vo.AbsListViewBindingObject> {
    android.widget.ListView mView;

    public AbsListViewBindingAdapterTest() {
        super(android.databinding.testapp.databinding.AbsListViewAdapterTestBinding.class, android.databinding.testapp.vo.AbsListViewBindingObject.class, R.layout.abs_list_view_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = getBinder().view;
    }

    public void testListSelector() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            junit.framework.TestCase.assertEquals(mBindingObject.getListSelector().getColor(), ((android.graphics.drawable.ColorDrawable) (mView.getSelector())).getColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getListSelector().getColor(), ((android.graphics.drawable.ColorDrawable) (mView.getSelector())).getColor());
        }
    }

    public void testScrollingCache() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.isScrollingCache(), mView.isScrollingCacheEnabled());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.isScrollingCache(), mView.isScrollingCacheEnabled());
    }

    public void testSmoothScrollbar() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.isSmoothScrollbar(), mView.isSmoothScrollbarEnabled());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.isSmoothScrollbar(), mView.isSmoothScrollbarEnabled());
    }
}

