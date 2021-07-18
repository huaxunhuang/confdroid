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


public class MapChangeRegistryTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    private int notificationCount = 0;

    public MapChangeRegistryTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    public void testNotifyAllChanged() {
        android.databinding.MapChangeRegistry mapChangeRegistry = new android.databinding.MapChangeRegistry();
        final android.databinding.ObservableMap<java.lang.String, java.lang.Integer> observableObj = new android.databinding.ObservableArrayMap<>();
        final java.lang.String expectedKey = "key";
        android.databinding.ObservableMap.OnMapChangedCallback listener = new android.databinding.ObservableMap.OnMapChangedCallback<android.databinding.ObservableMap<java.lang.String, java.lang.Integer>, java.lang.String, java.lang.Integer>() {
            @java.lang.Override
            public void onMapChanged(android.databinding.ObservableMap sender, java.lang.String key) {
                notificationCount++;
                junit.framework.TestCase.assertEquals(observableObj, sender);
                junit.framework.TestCase.assertEquals(key, expectedKey);
            }
        };
        mapChangeRegistry.add(listener);
        junit.framework.TestCase.assertEquals(0, notificationCount);
        mapChangeRegistry.notifyChange(observableObj, expectedKey);
        junit.framework.TestCase.assertEquals(1, notificationCount);
    }
}

