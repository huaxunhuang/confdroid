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


public class ObservableArrayMapTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    private android.databinding.ObservableArrayMap<java.lang.String, java.lang.String> mObservable;

    private java.util.ArrayList<java.lang.String> mNotifications = new java.util.ArrayList<>();

    private android.databinding.ObservableMap.OnMapChangedCallback mListener = new android.databinding.ObservableMap.OnMapChangedCallback() {
        @java.lang.Override
        public void onMapChanged(android.databinding.ObservableMap observableMap, java.lang.Object o) {
            junit.framework.TestCase.assertEquals(mObservable, observableMap);
            mNotifications.add(((java.lang.String) (o)));
        }
    };

    public ObservableArrayMapTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        mNotifications.clear();
        mObservable = new android.databinding.ObservableArrayMap<>();
    }

    public void testAddListener() {
        mObservable.put("Hello", "World");
        junit.framework.TestCase.assertTrue(mNotifications.isEmpty());
        mObservable.addOnMapChangedCallback(mListener);
        mObservable.put("Hello", "Goodbye");
        junit.framework.TestCase.assertFalse(mNotifications.isEmpty());
    }

    public void testRemoveListener() {
        // test there is no exception when the listener isn't there
        mObservable.removeOnMapChangedCallback(mListener);
        mObservable.addOnMapChangedCallback(mListener);
        mObservable.put("Hello", "World");
        mNotifications.clear();
        mObservable.removeOnMapChangedCallback(mListener);
        mObservable.put("World", "Hello");
        junit.framework.TestCase.assertTrue(mNotifications.isEmpty());
        // test there is no exception when the listener isn't there
        mObservable.removeOnMapChangedCallback(mListener);
    }

    public void testClear() {
        mObservable.put("Hello", "World");
        mObservable.put("World", "Hello");
        mObservable.addOnMapChangedCallback(mListener);
        mObservable.clear();
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertNull(mNotifications.get(0));
        junit.framework.TestCase.assertEquals(0, mObservable.size());
        junit.framework.TestCase.assertTrue(mObservable.isEmpty());
        mObservable.clear();
        // No notification when nothing is cleared.
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
    }

    public void testPut() {
        mObservable.addOnMapChangedCallback(mListener);
        mObservable.put("Hello", "World");
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals("Hello", mNotifications.get(0));
        junit.framework.TestCase.assertEquals("World", mObservable.get("Hello"));
        mObservable.put("Hello", "World2");
        junit.framework.TestCase.assertEquals(2, mNotifications.size());
        junit.framework.TestCase.assertEquals("Hello", mNotifications.get(1));
        junit.framework.TestCase.assertEquals("World2", mObservable.get("Hello"));
        mObservable.put("World", "Hello");
        junit.framework.TestCase.assertEquals(3, mNotifications.size());
        junit.framework.TestCase.assertEquals("World", mNotifications.get(2));
        junit.framework.TestCase.assertEquals("Hello", mObservable.get("World"));
    }

    public void testPutAll() {
        java.util.Map<java.lang.String, java.lang.String> toAdd = new android.support.v4.util.ArrayMap<>();
        toAdd.put("Hello", "World");
        toAdd.put("Goodbye", "Cruel World");
        mObservable.put("Cruel", "World");
        mObservable.addOnMapChangedCallback(mListener);
        mObservable.putAll(toAdd);
        junit.framework.TestCase.assertEquals(3, mObservable.size());
        junit.framework.TestCase.assertEquals("World", mObservable.get("Hello"));
        junit.framework.TestCase.assertEquals("Cruel World", mObservable.get("Goodbye"));
        junit.framework.TestCase.assertEquals(2, mNotifications.size());
        // order is not guaranteed
        junit.framework.TestCase.assertTrue(mNotifications.contains("Hello"));
        junit.framework.TestCase.assertTrue(mNotifications.contains("Goodbye"));
    }

    public void testPutAllSimpleArrayMap() {
        android.support.v4.util.SimpleArrayMap<java.lang.String, java.lang.String> toAdd = new android.support.v4.util.ArrayMap<>();
        toAdd.put("Hello", "World");
        toAdd.put("Goodbye", "Cruel World");
        mObservable.put("Cruel", "World");
        mObservable.addOnMapChangedCallback(mListener);
        mObservable.putAll(toAdd);
        junit.framework.TestCase.assertEquals(3, mObservable.size());
        junit.framework.TestCase.assertEquals("World", mObservable.get("Hello"));
        junit.framework.TestCase.assertEquals("Cruel World", mObservable.get("Goodbye"));
        junit.framework.TestCase.assertEquals(2, mNotifications.size());
        // order is not guaranteed
        junit.framework.TestCase.assertTrue(mNotifications.contains("Hello"));
        junit.framework.TestCase.assertTrue(mNotifications.contains("Goodbye"));
    }

    public void testRemove() {
        mObservable.put("Hello", "World");
        mObservable.put("Goodbye", "Cruel World");
        mObservable.addOnMapChangedCallback(mListener);
        junit.framework.TestCase.assertEquals("World", mObservable.remove("Hello"));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals("Hello", mNotifications.get(0));
        junit.framework.TestCase.assertNull(mObservable.remove("Hello"));
        // nothing removed, don't notify
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
    }

    public void testRemoveAll() {
        java.util.ArrayList<java.lang.String> toRemove = new java.util.ArrayList<>();
        toRemove.add("Hello");
        toRemove.add("Goodbye");
        mObservable.put("Hello", "World");
        mObservable.put("Goodbye", "Cruel World");
        mObservable.put("Cruel", "World");
        mObservable.addOnMapChangedCallback(mListener);
        junit.framework.TestCase.assertTrue(mObservable.removeAll(toRemove));
        junit.framework.TestCase.assertEquals(2, mNotifications.size());
        // order is not guaranteed
        junit.framework.TestCase.assertTrue(mNotifications.contains("Hello"));
        junit.framework.TestCase.assertTrue(mNotifications.contains("Goodbye"));
        junit.framework.TestCase.assertTrue(mObservable.containsKey("Cruel"));
        // Test nothing removed
        junit.framework.TestCase.assertFalse(mObservable.removeAll(toRemove));
        junit.framework.TestCase.assertEquals(2, mNotifications.size());
    }

    public void testRetainAll() {
        java.util.ArrayList<java.lang.String> toRetain = new java.util.ArrayList<>();
        toRetain.add("Hello");
        toRetain.add("Goodbye");
        mObservable.put("Hello", "World");
        mObservable.put("Goodbye", "Cruel World");
        mObservable.put("Cruel", "World");
        mObservable.addOnMapChangedCallback(mListener);
        junit.framework.TestCase.assertTrue(mObservable.retainAll(toRetain));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals("Cruel", mNotifications.get(0));
        junit.framework.TestCase.assertTrue(mObservable.containsKey("Hello"));
        junit.framework.TestCase.assertTrue(mObservable.containsKey("Goodbye"));
        // Test nothing removed
        junit.framework.TestCase.assertFalse(mObservable.retainAll(toRetain));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
    }

    public void testRemoveAt() {
        mObservable.put("Hello", "World");
        mObservable.put("Goodbye", "Cruel World");
        mObservable.addOnMapChangedCallback(mListener);
        java.lang.String key = mObservable.keyAt(0);
        java.lang.String value = mObservable.valueAt(0);
        junit.framework.TestCase.assertTrue("Hello".equals(key) || "Goodbye".equals(key));
        junit.framework.TestCase.assertEquals(value, mObservable.removeAt(0));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals(key, mNotifications.get(0));
    }

    public void testSetValueAt() {
        mObservable.put("Hello", "World");
        mObservable.addOnMapChangedCallback(mListener);
        junit.framework.TestCase.assertEquals("World", mObservable.setValueAt(0, "Cruel World"));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        junit.framework.TestCase.assertEquals("Hello", mNotifications.get(0));
    }
}

