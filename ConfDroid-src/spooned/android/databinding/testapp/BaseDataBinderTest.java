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


public class BaseDataBinderTest<T extends android.databinding.ViewDataBinding> extends android.test.ActivityInstrumentationTestCase2<android.databinding.testapp.TestActivity> {
    protected java.lang.Class<T> mBinderClass;

    private int mOrientation;

    protected T mBinder;

    public BaseDataBinderTest(final java.lang.Class<T> binderClass) {
        this(binderClass, android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public BaseDataBinderTest(final java.lang.Class<T> binderClass, final int orientation) {
        super(android.databinding.testapp.TestActivity.class);
        mBinderClass = binderClass;
        mOrientation = orientation;
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        getActivity().setRequestedOrientation(mOrientation);
    }

    public boolean isMainThread() {
        return android.os.Looper.myLooper() == android.os.Looper.getMainLooper();
    }

    protected T getBinder() {
        return mBinder;
    }

    protected T initBinder() {
        return initBinder(null);
    }

    @java.lang.Override
    public void runTestOnUiThread(java.lang.Runnable r) throws java.lang.Throwable {
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            r.run();
        } else {
            // ensure activity is created
            getActivity();
            super.runTestOnUiThread(r);
        }
    }

    protected T initBinder(final java.lang.Runnable init) {
        junit.framework.TestCase.assertNull("should not initialize binder twice", mBinder);
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            getActivity();// ensure activity is created

            getInstrumentation().waitForIdleSync();
        }
        final java.lang.reflect.Method[] method = new java.lang.reflect.Method[]{ null };
        java.lang.Throwable[] initError = new java.lang.Throwable[1];
        try {
            runTestOnUiThread(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        method[0] = mBinderClass.getMethod("inflate", android.view.LayoutInflater.class);
                        mBinder = ((T) (method[0].invoke(null, getActivity().getLayoutInflater())));
                        getActivity().setContentView(mBinder.getRoot());
                        if (init != null) {
                            init.run();
                        }
                    } catch (java.lang.Exception e) {
                        java.io.StringWriter sw = new java.io.StringWriter();
                        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                        e.printStackTrace(pw);
                        junit.framework.TestCase.fail("Error creating binder: " + sw.toString());
                    }
                }
            });
        } catch (java.lang.Throwable throwable) {
            initError[0] = throwable;
        }
        junit.framework.TestCase.assertNull(initError[0]);
        junit.framework.TestCase.assertNotNull(mBinder);
        return mBinder;
    }

    protected void reCreateBinder(java.lang.Runnable init) {
        mBinder = null;
        initBinder(init);
    }

    protected void assertMethod(java.lang.Class<?> klass, java.lang.String methodName) throws java.lang.NoSuchMethodException {
        junit.framework.TestCase.assertEquals(klass, mBinder.getClass().getDeclaredMethod(methodName).getReturnType());
    }

    protected void assertField(java.lang.Class<?> klass, java.lang.String fieldName) throws java.lang.NoSuchFieldException {
        junit.framework.TestCase.assertEquals(klass, mBinder.getClass().getDeclaredField(fieldName).getType());
    }

    protected void assertPublicField(java.lang.Class<?> klass, java.lang.String fieldName) throws java.lang.NoSuchFieldException {
        junit.framework.TestCase.assertEquals(klass, mBinder.getClass().getField(fieldName).getType());
    }

    protected void assertNoField(java.lang.String fieldName) {
        java.lang.Exception[] ex = new java.lang.Exception[1];
        try {
            mBinder.getClass().getField(fieldName);
        } catch (java.lang.NoSuchFieldException e) {
            ex[0] = e;
        }
        junit.framework.TestCase.assertNotNull(ex[0]);
    }
}

