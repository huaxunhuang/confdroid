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


public class ViewWithTagTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ViewWithTagBinding> {
    public ViewWithTagTest() {
        super(android.databinding.testapp.databinding.ViewWithTagBinding.class);
    }

    @android.support.annotation.UiThread
    public void test() {
        android.databinding.testapp.databinding.ViewWithTagBinding binder = initBinder();
        binder.setStr("i don't have tag");
        binder.executePendingBindings();
        android.view.ViewGroup root = ((android.view.ViewGroup) (binder.getRoot()));
        android.view.View view1 = root.getChildAt(0);
        android.view.View view2 = root.getChildAt(1);
        junit.framework.TestCase.assertTrue(view2 instanceof android.widget.TextView);
        junit.framework.TestCase.assertEquals("i don't have tag", ((android.widget.TextView) (view2)).getText().toString());
        junit.framework.TestCase.assertEquals("i have a tag", view1.getTag().toString());
        junit.framework.TestCase.assertEquals("Hello", view2.getTag(R.id.customTag));
    }
}

