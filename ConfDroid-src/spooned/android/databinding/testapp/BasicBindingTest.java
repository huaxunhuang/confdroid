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


public class BasicBindingTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    public BasicBindingTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder();
    }

    @android.test.UiThreadTest
    public void testTextViewContentInInitialization() {
        assertAB("X", "Y");
    }

    @android.test.UiThreadTest
    public void testNullValuesInInitialization() {
        assertAB(null, null);
    }

    @android.test.UiThreadTest
    public void testSecondIsNullInInitialization() {
        assertAB(null, "y");
    }

    @android.test.UiThreadTest
    public void testFirstIsNullInInitialization() {
        assertAB("x", null);
    }

    @android.test.UiThreadTest
    public void testTextViewContent() {
        assertAB("X", "Y");
    }

    @android.test.UiThreadTest
    public void testNullValues() {
        assertAB(null, null);
    }

    @android.test.UiThreadTest
    public void testSecondIsNull() {
        assertAB(null, "y");
    }

    @android.test.UiThreadTest
    public void testFirstIsNull() {
        assertAB("x", null);
    }

    public void testStopBinding() throws java.lang.Throwable {
        final android.databinding.testapp.BasicBindingTest.NoRebind noRebind = new android.databinding.testapp.BasicBindingTest.NoRebind();
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                assertAB("X", "Y");
                mBinder.addOnRebindCallback(noRebind);
            }
        });
        mBinder.setA("Q");
        android.databinding.testapp.BasicBindingTest.WaitForRun waitForRun = new android.databinding.testapp.BasicBindingTest.WaitForRun();
        android.view.View root = mBinder.getRoot();
        root.postOnAnimation(waitForRun);
        waitForRun.waitForRun();
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                junit.framework.TestCase.assertEquals(1, noRebind.rebindAttempts);
                junit.framework.TestCase.assertEquals(1, noRebind.rebindHalted);
                junit.framework.TestCase.assertEquals(0, noRebind.rebindWillEvaluate);
                junit.framework.TestCase.assertEquals("XY", mBinder.textView.getText().toString());
            }
        });
        mBinder.removeOnRebindCallback(noRebind);
        final android.databinding.testapp.BasicBindingTest.AllowRebind allowRebind = new android.databinding.testapp.BasicBindingTest.AllowRebind();
        mBinder.addOnRebindCallback(allowRebind);
        mBinder.setB("R");
        root.postOnAnimation(waitForRun);
        waitForRun.waitForRun();
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                junit.framework.TestCase.assertEquals(1, noRebind.rebindAttempts);
                junit.framework.TestCase.assertEquals(1, noRebind.rebindHalted);
                junit.framework.TestCase.assertEquals(0, noRebind.rebindWillEvaluate);
                junit.framework.TestCase.assertEquals(1, allowRebind.rebindAttempts);
                junit.framework.TestCase.assertEquals(0, allowRebind.rebindHalted);
                junit.framework.TestCase.assertEquals(1, allowRebind.rebindWillEvaluate);
                junit.framework.TestCase.assertEquals("QR", mBinder.textView.getText().toString());
            }
        });
    }

    @android.test.UiThreadTest
    public void testNoExpressionBinding() throws java.lang.Throwable {
        android.databinding.testapp.databinding.NoExpressionsBinding binding = android.databinding.testapp.databinding.NoExpressionsBinding.inflate(getActivity().getLayoutInflater());
        junit.framework.TestCase.assertNotNull(binding);
    }

    @android.test.UiThreadTest
    public void testNoDataElement() throws java.lang.Throwable {
        android.databinding.testapp.databinding.NoDataElementBinding binding = android.databinding.testapp.databinding.NoDataElementBinding.inflate(getActivity().getLayoutInflater());
        junit.framework.TestCase.assertNotNull(binding);
    }

    @android.test.UiThreadTest
    public void testJustIds() throws java.lang.Throwable {
        android.databinding.testapp.databinding.JustIdBinding binding = android.databinding.testapp.databinding.JustIdBinding.inflate(getActivity().getLayoutInflater());
        junit.framework.TestCase.assertNotNull(binding);
        junit.framework.TestCase.assertNotNull(binding.textView);
    }

    @android.test.UiThreadTest
    public void testNoBinding() throws java.lang.Throwable {
        junit.framework.TestCase.assertNull(android.databinding.DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.plain_layout, null, false));
    }

    @android.test.UiThreadTest
    public void testInflation() throws java.lang.Throwable {
        android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (mBinder.getRoot()));
        android.databinding.testapp.databinding.BasicBindingBinding binding = android.databinding.testapp.databinding.BasicBindingBinding.inflate(getActivity().getLayoutInflater(), viewGroup, true);
        junit.framework.TestCase.assertNotNull(binding);
        junit.framework.TestCase.assertNotNull(binding.textView);
        junit.framework.TestCase.assertNotSame(binding.textView, mBinder.textView);
    }

    @android.test.UiThreadTest
    public void testAndroidId() throws java.lang.Throwable {
        android.databinding.testapp.databinding.JustIdBinding binding = android.databinding.testapp.databinding.JustIdBinding.inflate(getActivity().getLayoutInflater());
        junit.framework.TestCase.assertNotNull(binding);
        junit.framework.TestCase.assertNotNull(binding.empty);
        junit.framework.TestCase.assertTrue(binding.empty instanceof android.widget.TextView);
    }

    private void assertAB(java.lang.String a, java.lang.String b) {
        mBinder.setA(a);
        mBinder.setB(b);
        rebindAndAssert(a + b);
    }

    private void rebindAndAssert(java.lang.String text) {
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(text, mBinder.textView.getText().toString());
    }

    private class AllowRebind extends android.databinding.OnRebindCallback<android.databinding.testapp.databinding.BasicBindingBinding> {
        public int rebindAttempts;

        public int rebindHalted;

        public int rebindWillEvaluate;

        @java.lang.Override
        public boolean onPreBind(android.databinding.testapp.databinding.BasicBindingBinding binding) {
            rebindAttempts++;
            return true;
        }

        @java.lang.Override
        public void onCanceled(android.databinding.testapp.databinding.BasicBindingBinding binding) {
            rebindHalted++;
        }

        @java.lang.Override
        public void onBound(android.databinding.testapp.databinding.BasicBindingBinding binding) {
            rebindWillEvaluate++;
        }
    }

    private class NoRebind extends android.databinding.testapp.BasicBindingTest.AllowRebind {
        @java.lang.Override
        public boolean onPreBind(android.databinding.testapp.databinding.BasicBindingBinding binding) {
            super.onPreBind(binding);
            return false;
        }
    }

    private static class WaitForRun implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            synchronized(this) {
                this.notifyAll();
            }
        }

        public void waitForRun() {
            synchronized(this) {
                try {
                    this.wait(1000);
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

