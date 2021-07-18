/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.os;


/**
 * Gives access to the system properties store.  The system properties
 * store contains a list of string key-value pairs.
 *
 * {@hide }
 */
public class SystemProperties {
    public static final int PROP_NAME_MAX = 31;

    public static final int PROP_VALUE_MAX = 91;

    private static final java.util.ArrayList<java.lang.Runnable> sChangeCallbacks = new java.util.ArrayList<java.lang.Runnable>();

    private static native java.lang.String native_get(java.lang.String key);

    private static native java.lang.String native_get(java.lang.String key, java.lang.String def);

    private static native int native_get_int(java.lang.String key, int def);

    private static native long native_get_long(java.lang.String key, long def);

    private static native boolean native_get_boolean(java.lang.String key, boolean def);

    private static native void native_set(java.lang.String key, java.lang.String def);

    private static native void native_add_change_callback();

    /**
     * Get the value for the given key.
     *
     * @return an empty string if the key isn't found
     * @throws IllegalArgumentException
     * 		if the key exceeds 32 characters
     */
    public static java.lang.String get(java.lang.String key) {
        if (key.length() > android.os.SystemProperties.PROP_NAME_MAX) {
            throw new java.lang.IllegalArgumentException("key.length > " + android.os.SystemProperties.PROP_NAME_MAX);
        }
        return android.os.SystemProperties.native_get(key);
    }

    /**
     * Get the value for the given key.
     *
     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
     * @throws IllegalArgumentException
     * 		if the key exceeds 32 characters
     */
    public static java.lang.String get(java.lang.String key, java.lang.String def) {
        if (key.length() > android.os.SystemProperties.PROP_NAME_MAX) {
            throw new java.lang.IllegalArgumentException("key.length > " + android.os.SystemProperties.PROP_NAME_MAX);
        }
        return android.os.SystemProperties.native_get(key, def);
    }

    /**
     * Get the value for the given key, and return as an integer.
     *
     * @param key
     * 		the key to lookup
     * @param def
     * 		a default value to return
     * @return the key parsed as an integer, or def if the key isn't found or
    cannot be parsed
     * @throws IllegalArgumentException
     * 		if the key exceeds 32 characters
     */
    public static int getInt(java.lang.String key, int def) {
        if (key.length() > android.os.SystemProperties.PROP_NAME_MAX) {
            throw new java.lang.IllegalArgumentException("key.length > " + android.os.SystemProperties.PROP_NAME_MAX);
        }
        return android.os.SystemProperties.native_get_int(key, def);
    }

    /**
     * Get the value for the given key, and return as a long.
     *
     * @param key
     * 		the key to lookup
     * @param def
     * 		a default value to return
     * @return the key parsed as a long, or def if the key isn't found or
    cannot be parsed
     * @throws IllegalArgumentException
     * 		if the key exceeds 32 characters
     */
    public static long getLong(java.lang.String key, long def) {
        if (key.length() > android.os.SystemProperties.PROP_NAME_MAX) {
            throw new java.lang.IllegalArgumentException("key.length > " + android.os.SystemProperties.PROP_NAME_MAX);
        }
        return android.os.SystemProperties.native_get_long(key, def);
    }

    /**
     * Get the value for the given key, returned as a boolean.
     * Values 'n', 'no', '0', 'false' or 'off' are considered false.
     * Values 'y', 'yes', '1', 'true' or 'on' are considered true.
     * (case sensitive).
     * If the key does not exist, or has any other value, then the default
     * result is returned.
     *
     * @param key
     * 		the key to lookup
     * @param def
     * 		a default value to return
     * @return the key parsed as a boolean, or def if the key isn't found or is
    not able to be parsed as a boolean.
     * @throws IllegalArgumentException
     * 		if the key exceeds 32 characters
     */
    public static boolean getBoolean(java.lang.String key, boolean def) {
        if (key.length() > android.os.SystemProperties.PROP_NAME_MAX) {
            throw new java.lang.IllegalArgumentException("key.length > " + android.os.SystemProperties.PROP_NAME_MAX);
        }
        return android.os.SystemProperties.native_get_boolean(key, def);
    }

    /**
     * Set the value for the given key.
     *
     * @throws IllegalArgumentException
     * 		if the key exceeds 32 characters
     * @throws IllegalArgumentException
     * 		if the value exceeds 92 characters
     */
    public static void set(java.lang.String key, java.lang.String val) {
        if (key.length() > android.os.SystemProperties.PROP_NAME_MAX) {
            throw new java.lang.IllegalArgumentException("key.length > " + android.os.SystemProperties.PROP_NAME_MAX);
        }
        if ((val != null) && (val.length() > android.os.SystemProperties.PROP_VALUE_MAX)) {
            throw new java.lang.IllegalArgumentException("val.length > " + android.os.SystemProperties.PROP_VALUE_MAX);
        }
        android.os.SystemProperties.native_set(key, val);
    }

    public static void addChangeCallback(java.lang.Runnable callback) {
        synchronized(android.os.SystemProperties.sChangeCallbacks) {
            if (android.os.SystemProperties.sChangeCallbacks.size() == 0) {
                android.os.SystemProperties.native_add_change_callback();
            }
            android.os.SystemProperties.sChangeCallbacks.add(callback);
        }
    }

    static void callChangeCallbacks() {
        synchronized(android.os.SystemProperties.sChangeCallbacks) {
            // Log.i("foo", "Calling " + sChangeCallbacks.size() + " change callbacks!");
            if (android.os.SystemProperties.sChangeCallbacks.size() == 0) {
                return;
            }
            java.util.ArrayList<java.lang.Runnable> callbacks = new java.util.ArrayList<java.lang.Runnable>(android.os.SystemProperties.sChangeCallbacks);
            for (int i = 0; i < callbacks.size(); i++) {
                callbacks.get(i).run();
            }
        }
    }
}

