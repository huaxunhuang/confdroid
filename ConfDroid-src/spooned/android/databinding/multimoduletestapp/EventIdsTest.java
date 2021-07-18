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
package android.databinding.multimoduletestapp;


public class EventIdsTest extends android.test.AndroidTestCase {
    public void testLibraryObservable() {
        android.databinding.testlibrary.ObservableInLibrary observableInLibrary = new android.databinding.testlibrary.ObservableInLibrary();
        android.databinding.multimoduletestapp.EventIdsTest.EventCounter ec = new android.databinding.multimoduletestapp.EventIdsTest.EventCounter();
        observableInLibrary.addOnPropertyChangedCallback(ec);
        ec.assertProperty(BR.libField1, 0);
        ec.assertProperty(BR.libField2, 0);
        ec.assertProperty(BR.sharedField, 0);
        observableInLibrary.setLibField1("a");
        ec.assertProperty(BR.libField1, 1);
        ec.assertProperty(BR.libField2, 0);
        ec.assertProperty(BR.sharedField, 0);
        observableInLibrary.setLibField2("b");
        ec.assertProperty(BR.libField1, 1);
        ec.assertProperty(BR.libField2, 1);
        ec.assertProperty(BR.sharedField, 0);
        observableInLibrary.setSharedField(3);
        ec.assertProperty(BR.libField1, 1);
        ec.assertProperty(BR.libField2, 1);
        ec.assertProperty(BR.sharedField, 1);
    }

    public void testAppObservable() {
        android.databinding.multimoduletestapp.ObservableInMainApp observableInMainApp = new android.databinding.multimoduletestapp.ObservableInMainApp();
        android.databinding.multimoduletestapp.EventIdsTest.EventCounter ec = new android.databinding.multimoduletestapp.EventIdsTest.EventCounter();
        observableInMainApp.addOnPropertyChangedCallback(ec);
        ec.assertProperty(BR.appField1, 0);
        ec.assertProperty(BR.appField2, 0);
        ec.assertProperty(BR.sharedField, 0);
        observableInMainApp.setAppField2(3);
        ec.assertProperty(BR.appField1, 0);
        ec.assertProperty(BR.appField2, 1);
        ec.assertProperty(BR.sharedField, 0);
        observableInMainApp.setAppField1("b");
        ec.assertProperty(BR.appField1, 1);
        ec.assertProperty(BR.appField2, 1);
        ec.assertProperty(BR.sharedField, 0);
        observableInMainApp.setSharedField(5);
        ec.assertProperty(BR.appField1, 1);
        ec.assertProperty(BR.appField2, 1);
        ec.assertProperty(BR.sharedField, 1);
    }

    public void testExtendingObservable() {
        android.databinding.multimoduletestapp.ObservableExtendingLib observable = new android.databinding.multimoduletestapp.ObservableExtendingLib();
        android.databinding.multimoduletestapp.EventIdsTest.EventCounter ec = new android.databinding.multimoduletestapp.EventIdsTest.EventCounter();
        observable.addOnPropertyChangedCallback(ec);
        ec.assertProperty(BR.childClassField, 0);
        ec.assertProperty(BR.libField1, 0);
        ec.assertProperty(BR.libField2, 0);
        ec.assertProperty(BR.sharedField, 0);
        observable.setChildClassField("a");
        ec.assertProperty(BR.childClassField, 1);
        ec.assertProperty(BR.libField1, 0);
        ec.assertProperty(BR.libField2, 0);
        ec.assertProperty(BR.sharedField, 0);
        observable.setLibField1("b");
        ec.assertProperty(BR.childClassField, 1);
        ec.assertProperty(BR.libField1, 1);
        ec.assertProperty(BR.libField2, 0);
        ec.assertProperty(BR.sharedField, 0);
        observable.setLibField2("c");
        ec.assertProperty(BR.childClassField, 1);
        ec.assertProperty(BR.libField1, 1);
        ec.assertProperty(BR.libField2, 1);
        ec.assertProperty(BR.sharedField, 0);
        observable.setSharedField(2);
        ec.assertProperty(BR.childClassField, 1);
        ec.assertProperty(BR.libField1, 1);
        ec.assertProperty(BR.libField2, 1);
        ec.assertProperty(BR.sharedField, 1);
    }

    private static class EventCounter extends android.databinding.Observable.OnPropertyChangedCallback {
        java.util.Map<java.lang.Integer, java.lang.Integer> mCounter = new java.util.HashMap<java.lang.Integer, java.lang.Integer>();

        @java.lang.Override
        public void onPropertyChanged(android.databinding.Observable observable, int propertyId) {
            mCounter.put(propertyId, get(propertyId) + 1);
        }

        public int get(int propertyId) {
            java.lang.Integer val = mCounter.get(propertyId);
            return val == null ? 0 : val;
        }

        private void assertProperty(int propertyId, int value) {
            junit.framework.TestCase.assertEquals(get(propertyId), value);
        }
    }
}

