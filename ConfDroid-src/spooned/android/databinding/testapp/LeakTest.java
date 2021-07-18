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


import static binding.textView.getText;


public class LeakTest extends android.test.ActivityInstrumentationTestCase2<android.databinding.testapp.TestActivity> {
    java.lang.ref.WeakReference<android.databinding.testapp.databinding.LeakTestBinding> mWeakReference = new java.lang.ref.WeakReference<android.databinding.testapp.databinding.LeakTestBinding>(null);

    public LeakTest() {
        super(android.databinding.testapp.TestActivity.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        try {
            getActivity().runOnUiThread(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        android.databinding.testapp.databinding.LeakTestBinding binding = android.databinding.testapp.databinding.LeakTestBinding.inflate(getActivity().getLayoutInflater());
                        getActivity().setContentView(binding.getRoot());
                        mWeakReference = new java.lang.ref.WeakReference<android.databinding.testapp.databinding.LeakTestBinding>(binding);
                        binding.setName("hello world");
                        binding.executePendingBindings();
                    } catch (java.lang.Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            });
            getInstrumentation().waitForIdleSync();
        } catch (java.lang.Throwable t) {
            throw new java.lang.Exception(t);
        }
    }

    public void testBindingLeak() throws java.lang.Throwable {
        junit.framework.TestCase.assertNotNull(mWeakReference.get());
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                getActivity().setContentView(new android.widget.FrameLayout(getActivity()));
            }
        });
        java.lang.ref.WeakReference<java.lang.Object> canary = new java.lang.ref.WeakReference<java.lang.Object>(new java.lang.Object());
        while (canary.get() != null) {
            byte[] b = new byte[1024 * 1024];
            java.lang.System.gc();
        } 
        junit.framework.TestCase.assertNull(mWeakReference.get());
    }

    // Test to ensure that when the View is detached that it doesn't rebind
    // the dirty Views. The rebind should happen only after the root view is
    // reattached.
    public void testNoChangeWhenDetached() throws java.lang.Throwable {
        final android.databinding.testapp.databinding.LeakTestBinding binding = mWeakReference.get();
        final android.databinding.testapp.LeakTest.AnimationWatcher watcher = new android.databinding.testapp.LeakTest.AnimationWatcher();
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                getActivity().setContentView(new android.widget.FrameLayout(getActivity()));
                binding.setName("goodbye world");
                getActivity().getWindow().getDecorView().postOnAnimation(watcher);
            }
        });
        watcher.waitForAnimationThread();
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                junit.framework.TestCase.assertEquals("hello world", getText().toString());
                getActivity().setContentView(binding.getRoot());
                getActivity().getWindow().getDecorView().postOnAnimation(watcher);
            }
        });
        watcher.waitForAnimationThread();
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                junit.framework.TestCase.assertEquals("goodbye world", getText().toString());
            }
        });
    }

    private static class AnimationWatcher implements java.lang.Runnable {
        private boolean mWaiting = true;

        public void waitForAnimationThread() throws java.lang.InterruptedException {
            synchronized(this) {
                while (mWaiting) {
                    this.wait();
                } 
                mWaiting = true;
            }
        }

        @java.lang.Override
        public void run() {
            synchronized(this) {
                mWaiting = false;
                this.notifyAll();
            }
        }
    }
}

