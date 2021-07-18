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


public class NewApiTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.NewApiLayoutBinding> {
    public NewApiTest() {
        super(android.databinding.testapp.databinding.NewApiLayoutBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
    }

    @android.test.UiThreadTest
    public void testSetElevation() {
        initBinder();
        mBinder.setElevation(3);
        mBinder.setName("foo");
        mBinder.setChildren(new java.util.ArrayList<android.view.View>());
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("foo", mBinder.textView.getText().toString());
        junit.framework.TestCase.assertEquals(3.0F, mBinder.textView.getElevation());
    }

    @android.test.UiThreadTest
    public void testSetElevationOlderAPI() {
        initBinder();
        android.databinding.DataBinderTrojan.setBuildSdkInt(1);
        try {
            android.widget.TextView textView = mBinder.textView;
            float originalElevation = textView.getElevation();
            mBinder.setElevation(3);
            mBinder.setName("foo2");
            mBinder.executePendingBindings();
            junit.framework.TestCase.assertEquals("foo2", textView.getText().toString());
            junit.framework.TestCase.assertEquals(originalElevation, textView.getElevation());
        } finally {
            android.databinding.DataBinderTrojan.setBuildSdkInt(android.os.Build.VERSION.SDK_INT);
        }
    }

    @android.test.UiThreadTest
    public void testGeneric() {
        initBinder();
        java.util.ArrayList<android.view.View> views = new java.util.ArrayList<>();
        mBinder.setChildren(views);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(1, views.size());
        junit.framework.TestCase.assertSame(mBinder.textView, views.get(0));
    }

    @android.test.UiThreadTest
    public void testGenericOlderApi() {
        initBinder();
        android.databinding.DataBinderTrojan.setBuildSdkInt(1);
        try {
            java.util.ArrayList<android.view.View> views = new java.util.ArrayList<>();
            mBinder.setChildren(views);
            mBinder.executePendingBindings();
            // we should not call the api on older platforms.
            junit.framework.TestCase.assertEquals(0, views.size());
        } finally {
            android.databinding.DataBinderTrojan.setBuildSdkInt(android.os.Build.VERSION.SDK_INT);
        }
    }
}

