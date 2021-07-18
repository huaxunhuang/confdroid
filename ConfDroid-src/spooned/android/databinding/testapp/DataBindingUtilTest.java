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


public class DataBindingUtilTest extends android.test.ActivityInstrumentationTestCase2<android.databinding.testapp.TestActivity> {
    public DataBindingUtilTest() {
        super(android.databinding.testapp.TestActivity.class);
    }

    @android.test.UiThreadTest
    public void testFindBinding() throws java.lang.Throwable {
        android.databinding.testapp.databinding.BasicBindingBinding binding = android.databinding.testapp.databinding.BasicBindingBinding.inflate(getActivity().getLayoutInflater());
        junit.framework.TestCase.assertEquals(binding, android.databinding.DataBindingUtil.findBinding(binding.textView));
        junit.framework.TestCase.assertEquals(binding, android.databinding.DataBindingUtil.findBinding(binding.getRoot()));
        android.view.ViewGroup root = ((android.view.ViewGroup) (binding.getRoot()));
        getActivity().getLayoutInflater().inflate(R.layout.basic_binding, root, true);
        android.view.View inflated = root.getChildAt(1);
        junit.framework.TestCase.assertNull(android.databinding.DataBindingUtil.findBinding(inflated));
        android.databinding.testapp.databinding.BasicBindingBinding innerBinding = android.databinding.DataBindingUtil.bind(inflated);
        junit.framework.TestCase.assertEquals(innerBinding, android.databinding.DataBindingUtil.findBinding(inflated));
        junit.framework.TestCase.assertEquals(innerBinding, android.databinding.DataBindingUtil.findBinding(innerBinding.textView));
    }

    @android.test.UiThreadTest
    public void testGetBinding() throws java.lang.Throwable {
        android.databinding.testapp.databinding.BasicBindingBinding binding = android.databinding.testapp.databinding.BasicBindingBinding.inflate(getActivity().getLayoutInflater());
        junit.framework.TestCase.assertNull(android.databinding.DataBindingUtil.getBinding(binding.textView));
        junit.framework.TestCase.assertEquals(binding, android.databinding.DataBindingUtil.getBinding(binding.getRoot()));
    }

    @android.test.UiThreadTest
    public void testSetContentView() throws java.lang.Throwable {
        android.databinding.testapp.databinding.CenteredContentBinding binding = android.databinding.DataBindingUtil.setContentView(getActivity(), R.layout.centered_content);
        junit.framework.TestCase.assertNotNull(binding);
        android.view.ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        junit.framework.TestCase.assertEquals(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, layoutParams.width);
        junit.framework.TestCase.assertEquals(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, layoutParams.height);
    }

    @android.test.UiThreadTest
    public void testBind() throws java.lang.Throwable {
        getActivity().setContentView(R.layout.basic_binding);
        android.view.ViewGroup content = ((android.view.ViewGroup) (getActivity().findViewById(android.R.id.content)));
        junit.framework.TestCase.assertEquals(1, content.getChildCount());
        android.view.View view = content.getChildAt(0);
        android.databinding.testapp.databinding.BasicBindingBinding binding = android.databinding.DataBindingUtil.bind(view);
        junit.framework.TestCase.assertNotNull(binding);
        junit.framework.TestCase.assertEquals(binding, android.databinding.DataBindingUtil.<android.databinding.testapp.databinding.BasicBindingBinding>bind(view));
    }

    @android.test.UiThreadTest
    public void testInflate() throws java.lang.Throwable {
        getActivity().getWindow().getDecorView();// force a content to exist.

        android.view.ViewGroup content = ((android.view.ViewGroup) (getActivity().findViewById(android.R.id.content)));
        android.databinding.testapp.databinding.BasicBindingBinding binding = android.databinding.DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.basic_binding, content, false);
        junit.framework.TestCase.assertNotNull(binding);
        junit.framework.TestCase.assertNotNull(binding.getRoot().getLayoutParams());
        binding = android.databinding.DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.basic_binding, null, false);
        junit.framework.TestCase.assertNotNull(binding);
        junit.framework.TestCase.assertNull(binding.getRoot().getLayoutParams());
        junit.framework.TestCase.assertNull(android.databinding.DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.plain_layout, null, false));
        android.databinding.testapp.databinding.MergeLayoutBinding mergeBinding = android.databinding.DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.merge_layout, content, true);
        junit.framework.TestCase.assertNotNull(mergeBinding);
        junit.framework.TestCase.assertNotNull(mergeBinding.innerTextView1);
        junit.framework.TestCase.assertNotNull(mergeBinding.innerTextView2);
        try {
            android.databinding.DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.merge_layout, content, false);
            junit.framework.TestCase.fail("Inflating a merge layout without a root should fail");
        } catch (android.view.InflateException e) {
            // You can't inflate a merge layout without a root.
        }
    }
}

