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


public class BindingAdapterTestBase<T extends android.databinding.ViewDataBinding, V extends android.databinding.testapp.vo.BindingAdapterBindingObject> extends android.databinding.testapp.BaseDataBinderTest<T> {
    private java.lang.Class<V> mBindingObjectClass;

    protected V mBindingObject;

    private java.lang.reflect.Method mSetMethod;

    public BindingAdapterTestBase(java.lang.Class<T> binderClass, java.lang.Class<V> observableClass, int layoutId) {
        super(binderClass);
        mBindingObjectClass = observableClass;
        try {
            mSetMethod = binderClass.getDeclaredMethod("setObj", observableClass);
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try {
                    mBindingObject = mBindingObjectClass.newInstance();
                    mSetMethod.invoke(getBinder(), mBindingObject);
                    getBinder().executePendingBindings();
                } catch (java.lang.IllegalAccessException e) {
                    throw new java.lang.RuntimeException(e);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    throw new java.lang.RuntimeException(e);
                } catch (java.lang.InstantiationException e) {
                    throw new java.lang.RuntimeException(e);
                }
            }
        });
    }

    protected void changeValues() throws java.lang.Throwable {
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBindingObject.changeValues();
                getBinder().executePendingBindings();
            }
        });
    }
}

