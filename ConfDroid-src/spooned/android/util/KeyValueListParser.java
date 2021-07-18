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
package android.util;


/**
 * Parses a list of key=value pairs, separated by some delimiter, and puts the results in
 * an internal Map. Values can be then queried by key, or if not found, a default value
 * can be used.
 *
 * @unknown 
 */
public class KeyValueListParser {
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mValues = new android.util.ArrayMap<>();

    private final android.text.TextUtils.StringSplitter mSplitter;

    /**
     * Constructs a new KeyValueListParser. This can be reused for different strings
     * by calling {@link #setString(String)}.
     *
     * @param delim
     * 		The delimiter that separates key=value pairs.
     */
    public KeyValueListParser(char delim) {
        mSplitter = new android.text.TextUtils.SimpleStringSplitter(delim);
    }

    /**
     * Resets the parser with a new string to parse. The string is expected to be in the following
     * format:
     * <pre>key1=value,key2=value,key3=value</pre>
     *
     * where the delimiter is a comma.
     *
     * @param str
     * 		the string to parse.
     * @throws IllegalArgumentException
     * 		if the string is malformed.
     */
    public void setString(java.lang.String str) throws java.lang.IllegalArgumentException {
        mValues.clear();
        if (str != null) {
            mSplitter.setString(str);
            for (java.lang.String pair : mSplitter) {
                int sep = pair.indexOf('=');
                if (sep < 0) {
                    mValues.clear();
                    throw new java.lang.IllegalArgumentException(((("'" + pair) + "' in '") + str) + "' is not a valid key-value pair");
                }
                mValues.put(pair.substring(0, sep).trim(), pair.substring(sep + 1).trim());
            }
        }
    }

    /**
     * Get the value for key as an int.
     *
     * @param key
     * 		The key to lookup.
     * @param def
     * 		The value to return if the key was not found, or the value was not a long.
     * @return the int value associated with the key.
     */
    public int getInt(java.lang.String key, int def) {
        java.lang.String value = mValues.get(key);
        if (value != null) {
            try {
                return java.lang.Integer.parseInt(value);
            } catch (java.lang.NumberFormatException e) {
                // fallthrough
            }
        }
        return def;
    }

    /**
     * Get the value for key as a long.
     *
     * @param key
     * 		The key to lookup.
     * @param def
     * 		The value to return if the key was not found, or the value was not a long.
     * @return the long value associated with the key.
     */
    public long getLong(java.lang.String key, long def) {
        java.lang.String value = mValues.get(key);
        if (value != null) {
            try {
                return java.lang.Long.parseLong(value);
            } catch (java.lang.NumberFormatException e) {
                // fallthrough
            }
        }
        return def;
    }

    /**
     * Get the value for key as a float.
     *
     * @param key
     * 		The key to lookup.
     * @param def
     * 		The value to return if the key was not found, or the value was not a float.
     * @return the float value associated with the key.
     */
    public float getFloat(java.lang.String key, float def) {
        java.lang.String value = mValues.get(key);
        if (value != null) {
            try {
                return java.lang.Float.parseFloat(value);
            } catch (java.lang.NumberFormatException e) {
                // fallthrough
            }
        }
        return def;
    }

    /**
     * Get the value for key as a string.
     *
     * @param key
     * 		The key to lookup.
     * @param def
     * 		The value to return if the key was not found.
     * @return the string value associated with the key.
     */
    public java.lang.String getString(java.lang.String key, java.lang.String def) {
        java.lang.String value = mValues.get(key);
        if (value != null) {
            return value;
        }
        return def;
    }
}

