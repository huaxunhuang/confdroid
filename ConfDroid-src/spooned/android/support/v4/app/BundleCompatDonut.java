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
package android.support.v4.app;


/**
 *
 *
 * @unknown 
 */
class BundleCompatDonut {
    private static final java.lang.String TAG = "BundleCompatDonut";

    private static java.lang.reflect.Method sGetIBinderMethod;

    private static boolean sGetIBinderMethodFetched;

    private static java.lang.reflect.Method sPutIBinderMethod;

    private static boolean sPutIBinderMethodFetched;

    public static android.os.IBinder getBinder(android.os.Bundle bundle, java.lang.String key) {
        if (!android.support.v4.app.BundleCompatDonut.sGetIBinderMethodFetched) {
            try {
                android.support.v4.app.BundleCompatDonut.sGetIBinderMethod = android.os.Bundle.class.getMethod("getIBinder", java.lang.String.class);
                android.support.v4.app.BundleCompatDonut.sGetIBinderMethod.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.i(android.support.v4.app.BundleCompatDonut.TAG, "Failed to retrieve getIBinder method", e);
            }
            android.support.v4.app.BundleCompatDonut.sGetIBinderMethodFetched = true;
        }
        if (android.support.v4.app.BundleCompatDonut.sGetIBinderMethod != null) {
            try {
                return ((android.os.IBinder) (android.support.v4.app.BundleCompatDonut.sGetIBinderMethod.invoke(bundle, key)));
            } catch (java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException | java.lang.IllegalArgumentException e) {
                android.util.Log.i(android.support.v4.app.BundleCompatDonut.TAG, "Failed to invoke getIBinder via reflection", e);
                android.support.v4.app.BundleCompatDonut.sGetIBinderMethod = null;
            }
        }
        return null;
    }

    public static void putBinder(android.os.Bundle bundle, java.lang.String key, android.os.IBinder binder) {
        if (!android.support.v4.app.BundleCompatDonut.sPutIBinderMethodFetched) {
            try {
                android.support.v4.app.BundleCompatDonut.sPutIBinderMethod = android.os.Bundle.class.getMethod("putIBinder", java.lang.String.class, android.os.IBinder.class);
                android.support.v4.app.BundleCompatDonut.sPutIBinderMethod.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.i(android.support.v4.app.BundleCompatDonut.TAG, "Failed to retrieve putIBinder method", e);
            }
            android.support.v4.app.BundleCompatDonut.sPutIBinderMethodFetched = true;
        }
        if (android.support.v4.app.BundleCompatDonut.sPutIBinderMethod != null) {
            try {
                android.support.v4.app.BundleCompatDonut.sPutIBinderMethod.invoke(bundle, key, binder);
            } catch (java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException | java.lang.IllegalArgumentException e) {
                android.util.Log.i(android.support.v4.app.BundleCompatDonut.TAG, "Failed to invoke putIBinder via reflection", e);
                android.support.v4.app.BundleCompatDonut.sPutIBinderMethod = null;
            }
        }
    }
}

