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


public class BaseObservableTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    private android.databinding.BaseObservable mObservable;

    private java.util.ArrayList<java.lang.Integer> mNotifications = new java.util.ArrayList<>();

    private android.databinding.Observable.OnPropertyChangedCallback mCallback = new android.databinding.Observable.OnPropertyChangedCallback() {
        @java.lang.Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            junit.framework.TestCase.assertEquals(mObservable, observable);
            mNotifications.add(i);
        }
    };

    public BaseObservableTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        mNotifications.clear();
        mObservable = new android.databinding.BaseObservable();
        initBinder(null);
    }

    public void testAddCallback() {
        mObservable.notifyChange();
        junit.framework.TestCase.assertTrue(mNotifications.isEmpty());
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        junit.framework.TestCase.assertFalse(mNotifications.isEmpty());
    }

    public void testRemoveCallback() {
        // test there is no exception when the Callback isn't there
        mObservable.removeOnPropertyChangedCallback(mCallback);
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        mNotifications.clear();
        mObservable.removeOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        junit.framework.TestCase.assertTrue(mNotifications.isEmpty());
        // test there is no exception when the Callback isn't there
        mObservable.removeOnPropertyChangedCallback(mCallback);
    }

    public void testNotifyChange() {
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals(0, ((int) (mNotifications.get(0))));
    }

    public void testNotifyPropertyChanged() {
        final int expectedId = 100;
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyPropertyChanged(expectedId);
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals(expectedId, ((int) (mNotifications.get(0))));
    }
}

