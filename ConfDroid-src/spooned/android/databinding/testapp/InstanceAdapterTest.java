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


import static binding.includedLayout.textView1.getText;
import static binding.viewStub.getRoot;
import static binding.viewStub.getViewStub;


public class InstanceAdapterTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.InstanceAdapterBinding> {
    public InstanceAdapterTest() {
        super(android.databinding.testapp.databinding.InstanceAdapterBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
    }

    private void initNormal() {
        android.databinding.DataBindingUtil.setDefaultComponent(new android.databinding.testapp.TestComponent() {
            private android.databinding.testapp.adapter.InstanceAdapter mInstanceAdapter = new android.databinding.testapp.adapter.InstanceAdapter("Hello %s %s %s %s");

            @java.lang.Override
            public android.databinding.testapp.adapter.NameClashAdapter.MyAdapter getMyAdapter1() {
                return null;
            }

            @java.lang.Override
            public android.databinding.testapp.adapter2.NameClashAdapter.MyAdapter getMyAdapter2() {
                return null;
            }

            @java.lang.Override
            public android.databinding.testapp.adapter.NameClashAdapter getNameClashAdapter1() {
                return null;
            }

            @java.lang.Override
            public android.databinding.testapp.adapter2.NameClashAdapter getNameClashAdapter2() {
                return null;
            }

            @java.lang.Override
            public android.databinding.testapp.adapter.InstanceAdapter getInstanceAdapter() {
                return mInstanceAdapter;
            }
        });
        initBinder();
        mBinder.executePendingBindings();
    }

    @android.test.UiThreadTest
    public void testOneAttr() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World foo bar baz", mBinder.textView1.getText().toString());
    }

    @android.test.UiThreadTest
    public void testTwoAttr() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World baz foo bar", mBinder.textView2.getText().toString());
    }

    @android.test.UiThreadTest
    public void testOneAttrOld() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello null World foo bar", mBinder.textView3.getText().toString());
        mBinder.setStr("Android");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World Android foo bar", mBinder.textView3.getText().toString());
    }

    @android.test.UiThreadTest
    public void testTwoAttrOld() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello null baz World baz", mBinder.textView4.getText().toString());
        mBinder.setStr("Android");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World baz Android baz", mBinder.textView4.getText().toString());
    }

    @android.test.UiThreadTest
    public void testRequiredBinding() throws java.lang.Throwable {
        try {
            android.databinding.testapp.databinding.InstanceAdapterBinding.inflate(getActivity().getLayoutInflater(), null);
            junit.framework.TestCase.fail("Binding should fail if a required BindingAdapter is missing.");
        } catch (java.lang.IllegalStateException e) {
            // Expected exception
        }
    }

    @android.test.UiThreadTest
    public void testInclude() throws java.lang.Throwable {
        initNormal();
        android.databinding.DataBindingComponent component = android.databinding.DataBindingUtil.getDefaultComponent();
        android.databinding.DataBindingUtil.setDefaultComponent(null);
        android.databinding.testapp.databinding.IncludeInstanceAdapterBinding binding = android.databinding.testapp.databinding.IncludeInstanceAdapterBinding.inflate(getActivity().getLayoutInflater(), component);
        binding.setStr("World");
        binding.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World foo bar baz", getText().toString());
    }

    @android.test.UiThreadTest
    public void testViewStub() throws java.lang.Throwable {
        initNormal();
        android.databinding.DataBindingComponent component = android.databinding.DataBindingUtil.getDefaultComponent();
        android.databinding.DataBindingUtil.setDefaultComponent(null);
        android.databinding.testapp.databinding.IncludeInstanceAdapterBinding binding = android.databinding.DataBindingUtil.setContentView(getActivity(), R.layout.include_instance_adapter, component);
        binding.setStr("World");
        binding.executePendingBindings();
        getViewStub().inflate();
        android.widget.TextView view = ((android.widget.TextView) (getRoot().findViewById(R.id.textView1)));
        junit.framework.TestCase.assertEquals("Hello World foo bar baz", view.getText().toString());
    }

    @android.test.UiThreadTest
    public void testOneAttrWithComponentStatic() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("World component", mBinder.textView6.getText().toString());
    }

    @android.test.UiThreadTest
    public void testOneAttrWithComponentInstance() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World component bar baz", mBinder.textView7.getText().toString());
    }

    @android.test.UiThreadTest
    public void testTwoAttrsWithComponentInstance() throws java.lang.Throwable {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World foo component bar", mBinder.textView8.getText().toString());
    }
}

