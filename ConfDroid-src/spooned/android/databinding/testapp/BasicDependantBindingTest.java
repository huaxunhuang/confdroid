/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.testapp;


public class BasicDependantBindingTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicDependantBindingBinding> {
    public BasicDependantBindingTest() {
        super(android.databinding.testapp.databinding.BasicDependantBindingBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder();
    }

    public java.util.List<android.databinding.testapp.vo.NotBindableVo> permutations(java.lang.String value) {
        java.util.List<android.databinding.testapp.vo.NotBindableVo> result = new java.util.ArrayList<>();
        result.add(null);
        result.add(new android.databinding.testapp.vo.NotBindableVo(null));
        result.add(new android.databinding.testapp.vo.NotBindableVo(value));
        return result;
    }

    @android.test.UiThreadTest
    public void testAllPermutations() {
        java.util.List<android.databinding.testapp.vo.NotBindableVo> obj1s = permutations("a");
        java.util.List<android.databinding.testapp.vo.NotBindableVo> obj2s = permutations("b");
        for (android.databinding.testapp.vo.NotBindableVo obj1 : obj1s) {
            for (android.databinding.testapp.vo.NotBindableVo obj2 : obj2s) {
                reCreateBinder(null);// get a new one

                testWith(obj1, obj2);
                reCreateBinder(null);
                mBinder.executePendingBindings();
                testWith(obj1, obj2);
            }
        }
    }

    private void testWith(android.databinding.testapp.vo.NotBindableVo obj1, android.databinding.testapp.vo.NotBindableVo obj2) {
        mBinder.setObj1(obj1);
        mBinder.setObj2(obj2);
        mBinder.executePendingBindings();
        assertValues(safeGet(obj1), safeGet(obj2), obj1 == null ? "" : obj1.mergeStringFields(obj2), obj2 == null ? "" : obj2.mergeStringFields(obj1), (obj1 == null ? null : obj1.getStringValue()) + (obj2 == null ? null : obj2.getStringValue()));
    }

    private java.lang.String safeGet(android.databinding.testapp.vo.NotBindableVo vo) {
        if ((vo == null) || (vo.getStringValue() == null)) {
            return "";
        }
        return vo.getStringValue();
    }

    private void assertValues(java.lang.String textView1, java.lang.String textView2, java.lang.String mergedView1, java.lang.String mergedView2, java.lang.String rawMerge) {
        junit.framework.TestCase.assertEquals(textView1, mBinder.textView1.getText().toString());
        junit.framework.TestCase.assertEquals(textView2, mBinder.textView2.getText().toString());
        junit.framework.TestCase.assertEquals(mergedView1, mBinder.mergedTextView1.getText().toString());
        junit.framework.TestCase.assertEquals(mergedView2, mBinder.mergedTextView2.getText().toString());
        junit.framework.TestCase.assertEquals(rawMerge, mBinder.rawStringMerge.getText().toString());
    }
}

