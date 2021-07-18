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


import android.databinding.testapp.databinding.MergeLayoutBinding;

import static mergeLayoutBinding.innerTextView1.getText;


public class IncludeTagTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.LayoutWithIncludeBinding> {
    public IncludeTagTest() {
        super(android.databinding.testapp.databinding.LayoutWithIncludeBinding.class);
    }

    @android.test.UiThreadTest
    public void testIncludeTag() {
        initBinder();
        junit.framework.TestCase.assertNotNull(mBinder.includedPlainLayout);
        junit.framework.TestCase.assertTrue(mBinder.includedPlainLayout instanceof android.widget.FrameLayout);
        android.databinding.testapp.vo.NotBindableVo vo = new android.databinding.testapp.vo.NotBindableVo(3, "a");
        mBinder.setOuterObject(vo);
        mBinder.executePendingBindings();
        final android.widget.TextView outerText = ((android.widget.TextView) (mBinder.getRoot().findViewById(R.id.outerTextView)));
        junit.framework.TestCase.assertEquals("a", outerText.getText());
        final android.widget.TextView innerText = ((android.widget.TextView) (mBinder.getRoot().findViewById(R.id.innerTextView)));
        junit.framework.TestCase.assertEquals("modified 3a", innerText.getText().toString());
        android.widget.TextView textView1 = ((android.widget.TextView) (mBinder.getRoot().findViewById(R.id.innerTextView1)));
        junit.framework.TestCase.assertEquals(mBinder.getRoot(), textView1.getParent().getParent());
        android.widget.TextView textView2 = ((android.widget.TextView) (mBinder.getRoot().findViewById(R.id.innerTextView2)));
        junit.framework.TestCase.assertEquals(mBinder.getRoot(), textView2.getParent().getParent());
        junit.framework.TestCase.assertEquals("a hello 3a", textView1.getText().toString());
        junit.framework.TestCase.assertEquals("b hello 3a", textView2.getText().toString());
        MergeLayoutBinding mergeLayoutBinding = mBinder.secondMerge;
        junit.framework.TestCase.assertNotSame(textView1, mergeLayoutBinding.innerTextView1);
        junit.framework.TestCase.assertNotSame(textView2, mergeLayoutBinding.innerTextView2);
        junit.framework.TestCase.assertEquals("a goodbye 3a", getText().toString());
        junit.framework.TestCase.assertEquals("b goodbye 3a", mergeLayoutBinding.innerTextView2.getText().toString());
        android.databinding.testapp.databinding.MergeContainingMergeBinding mergeContainingMergeBinding = mBinder.thirdMerge;
        MergeLayoutBinding merge1 = mergeContainingMergeBinding.merge1;
        MergeLayoutBinding merge2 = mergeContainingMergeBinding.merge2;
        junit.framework.TestCase.assertEquals("a 1 third 3a", merge1.innerTextView1.getText().toString());
        junit.framework.TestCase.assertEquals("b 1 third 3a", merge1.innerTextView2.getText().toString());
        junit.framework.TestCase.assertEquals("a 2 third 3a", merge2.innerTextView1.getText().toString());
        junit.framework.TestCase.assertEquals("b 2 third 3a", merge2.innerTextView2.getText().toString());
        vo.setIntValue(5);
        vo.setStringValue("b");
        mBinder.invalidateAll();
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("b", outerText.getText());
        junit.framework.TestCase.assertEquals("modified 5b", innerText.getText().toString());
        junit.framework.TestCase.assertEquals("a hello 5b", textView1.getText().toString());
        junit.framework.TestCase.assertEquals("b hello 5b", textView2.getText().toString());
        junit.framework.TestCase.assertEquals("a goodbye 5b", getText().toString());
        junit.framework.TestCase.assertEquals("b goodbye 5b", mergeLayoutBinding.innerTextView2.getText().toString());
        junit.framework.TestCase.assertEquals("a 1 third 5b", merge1.innerTextView1.getText().toString());
        junit.framework.TestCase.assertEquals("b 1 third 5b", merge1.innerTextView2.getText().toString());
        junit.framework.TestCase.assertEquals("a 2 third 5b", merge2.innerTextView1.getText().toString());
        junit.framework.TestCase.assertEquals("b 2 third 5b", merge2.innerTextView2.getText().toString());
    }
}

