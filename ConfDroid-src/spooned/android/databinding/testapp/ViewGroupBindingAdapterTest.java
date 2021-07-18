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


public class ViewGroupBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.ViewGroupAdapterTestBinding, android.databinding.testapp.vo.ViewGroupBindingObject> {
    android.view.ViewGroup mView;

    public ViewGroupBindingAdapterTest() {
        super(android.databinding.testapp.databinding.ViewGroupAdapterTestBinding.class, android.databinding.testapp.vo.ViewGroupBindingObject.class, R.layout.view_group_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testDrawnWithCache() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.isAlwaysDrawnWithCache(), mView.isAlwaysDrawnWithCacheEnabled());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.isAlwaysDrawnWithCache(), mView.isAlwaysDrawnWithCacheEnabled());
    }

    public void testAnimationCache() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.isAnimationCache(), mView.isAnimationCacheEnabled());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.isAnimationCache(), mView.isAnimationCacheEnabled());
    }

    public void testSplitMotionEvents() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            junit.framework.TestCase.assertEquals(mBindingObject.isSplitMotionEvents(), mView.isMotionEventSplittingEnabled());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.isSplitMotionEvents(), mView.isMotionEventSplittingEnabled());
        }
    }

    public void testAnimateLayoutChanges() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            junit.framework.TestCase.assertEquals(mBindingObject.isAnimateLayoutChanges(), mView.getLayoutTransition() != null);
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.isAnimateLayoutChanges(), mView.getLayoutTransition() != null);
        }
    }
}

