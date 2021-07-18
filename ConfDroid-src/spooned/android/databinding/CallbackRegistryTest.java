/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.databinding;


public class CallbackRegistryTest {
    final java.lang.Integer callback1 = 1;

    final java.lang.Integer callback2 = 2;

    final java.lang.Integer callback3 = 3;

    android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> registry;

    int notify1;

    int notify2;

    int notify3;

    int[] deepNotifyCount = new int[300];

    java.lang.Integer argValue;

    private void addNotifyCount(java.lang.Integer callback) {
        if (callback == callback1) {
            notify1++;
        } else
            if (callback == callback2) {
                notify2++;
            } else
                if (callback == callback3) {
                    notify3++;
                }


        deepNotifyCount[callback]++;
    }

    @org.junit.Test
    public void testAddListener() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg, java.lang.Integer arg2) {
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        java.lang.Integer callback = 0;
        org.junit.Assert.assertNotNull(registry.copyCallbacks());
        org.junit.Assert.assertEquals(0, registry.copyCallbacks().size());
        registry.add(callback);
        java.util.ArrayList<java.lang.Integer> callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(1, callbacks.size());
        org.junit.Assert.assertEquals(callback, callbacks.get(0));
        registry.add(callback);
        callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(1, callbacks.size());
        org.junit.Assert.assertEquals(callback, callbacks.get(0));
        java.lang.Integer otherListener = 1;
        registry.add(otherListener);
        callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(2, callbacks.size());
        org.junit.Assert.assertEquals(callback, callbacks.get(0));
        org.junit.Assert.assertEquals(otherListener, callbacks.get(1));
        registry.remove(callback);
        registry.add(callback);
        callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(2, callbacks.size());
        org.junit.Assert.assertEquals(callback, callbacks.get(1));
        org.junit.Assert.assertEquals(otherListener, callbacks.get(0));
    }

    @org.junit.Test
    public void testSimpleNotify() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                org.junit.Assert.assertEquals(arg1, ((int) (arg)));
                addNotifyCount(callback);
                argValue = arg;
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        registry.add(callback2);
        java.lang.Integer arg = 1;
        registry.notifyCallbacks(this, arg, arg);
        org.junit.Assert.assertEquals(arg, argValue);
        org.junit.Assert.assertEquals(1, notify2);
    }

    @org.junit.Test
    public void testRemoveWhileNotifying() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                addNotifyCount(callback);
                if (callback == callback1) {
                    registry.remove(callback1);
                    registry.remove(callback2);
                }
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        registry.add(callback1);
        registry.add(callback2);
        registry.add(callback3);
        registry.notifyCallbacks(this, 0, null);
        org.junit.Assert.assertEquals(1, notify1);
        org.junit.Assert.assertEquals(1, notify2);
        org.junit.Assert.assertEquals(1, notify3);
        java.util.ArrayList<java.lang.Integer> callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(1, callbacks.size());
        org.junit.Assert.assertEquals(callback3, callbacks.get(0));
    }

    @org.junit.Test
    public void testDeepRemoveWhileNotifying() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                addNotifyCount(callback);
                registry.remove(callback);
                registry.notifyCallbacks(android.databinding.CallbackRegistryTest.this, arg1, null);
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        registry.add(callback1);
        registry.add(callback2);
        registry.add(callback3);
        registry.notifyCallbacks(this, 0, null);
        org.junit.Assert.assertEquals(1, notify1);
        org.junit.Assert.assertEquals(2, notify2);
        org.junit.Assert.assertEquals(3, notify3);
        java.util.ArrayList<java.lang.Integer> callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(0, callbacks.size());
    }

    @org.junit.Test
    public void testAddRemovedListener() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                addNotifyCount(callback);
                if (callback == callback1) {
                    registry.remove(callback2);
                } else
                    if (callback == callback3) {
                        registry.add(callback2);
                    }

            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        registry.add(callback1);
        registry.add(callback2);
        registry.add(callback3);
        registry.notifyCallbacks(this, 0, null);
        java.util.ArrayList<java.lang.Integer> callbacks = registry.copyCallbacks();
        org.junit.Assert.assertEquals(3, callbacks.size());
        org.junit.Assert.assertEquals(callback1, callbacks.get(0));
        org.junit.Assert.assertEquals(callback3, callbacks.get(1));
        org.junit.Assert.assertEquals(callback2, callbacks.get(2));
        org.junit.Assert.assertEquals(1, notify1);
        org.junit.Assert.assertEquals(1, notify2);
        org.junit.Assert.assertEquals(1, notify3);
    }

    @org.junit.Test
    public void testVeryDeepRemoveWhileNotifying() {
        final java.lang.Integer[] callbacks = new java.lang.Integer[deepNotifyCount.length];
        for (int i = 0; i < callbacks.length; i++) {
            callbacks[i] = i;
        }
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                addNotifyCount(callback);
                registry.remove(callback);
                registry.remove(callbacks[(callbacks.length - callback) - 1]);
                registry.notifyCallbacks(android.databinding.CallbackRegistryTest.this, arg1, null);
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        for (int i = 0; i < callbacks.length; i++) {
            registry.add(callbacks[i]);
        }
        registry.notifyCallbacks(this, 0, null);
        for (int i = 0; i < deepNotifyCount.length; i++) {
            int expectedCount = java.lang.Math.min(i + 1, deepNotifyCount.length - i);
            org.junit.Assert.assertEquals(expectedCount, deepNotifyCount[i]);
        }
        java.util.ArrayList<java.lang.Integer> callbackList = registry.copyCallbacks();
        org.junit.Assert.assertEquals(0, callbackList.size());
    }

    @org.junit.Test
    public void testClear() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                addNotifyCount(callback);
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        for (int i = 0; i < deepNotifyCount.length; i++) {
            registry.add(i);
        }
        registry.clear();
        java.util.ArrayList<java.lang.Integer> callbackList = registry.copyCallbacks();
        org.junit.Assert.assertEquals(0, callbackList.size());
        registry.notifyCallbacks(this, 0, null);
        for (int i = 0; i < deepNotifyCount.length; i++) {
            org.junit.Assert.assertEquals(0, deepNotifyCount[i]);
        }
    }

    @org.junit.Test
    public void testNestedClear() {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg1, java.lang.Integer arg) {
                addNotifyCount(callback);
                registry.clear();
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        for (int i = 0; i < deepNotifyCount.length; i++) {
            registry.add(i);
        }
        registry.notifyCallbacks(this, 0, null);
        for (int i = 0; i < deepNotifyCount.length; i++) {
            org.junit.Assert.assertEquals(1, deepNotifyCount[i]);
        }
        java.util.ArrayList<java.lang.Integer> callbackList = registry.copyCallbacks();
        org.junit.Assert.assertEquals(0, callbackList.size());
    }

    @org.junit.Test
    public void testIsEmpty() throws java.lang.Exception {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg, java.lang.Integer arg2) {
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        java.lang.Integer callback = 0;
        org.junit.Assert.assertTrue(registry.isEmpty());
        registry.add(callback);
        org.junit.Assert.assertFalse(registry.isEmpty());
    }

    @org.junit.Test
    public void testClone() throws java.lang.Exception {
        android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> notifier = new android.databinding.CallbackRegistry.NotifierCallback<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>() {
            @java.lang.Override
            public void onNotifyCallback(java.lang.Integer callback, android.databinding.CallbackRegistryTest sender, int arg, java.lang.Integer arg2) {
            }
        };
        registry = new android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer>(notifier);
        org.junit.Assert.assertTrue(registry.isEmpty());
        android.databinding.CallbackRegistry<java.lang.Integer, android.databinding.CallbackRegistryTest, java.lang.Integer> registry2 = registry.clone();
        java.lang.Integer callback = 0;
        registry.add(callback);
        org.junit.Assert.assertFalse(registry.isEmpty());
        org.junit.Assert.assertTrue(registry2.isEmpty());
        registry2 = registry.clone();
        org.junit.Assert.assertFalse(registry2.isEmpty());
    }
}

