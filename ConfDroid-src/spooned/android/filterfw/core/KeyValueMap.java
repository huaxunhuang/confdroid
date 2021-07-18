/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class KeyValueMap extends java.util.HashMap<java.lang.String, java.lang.Object> {
    public void setKeyValues(java.lang.Object... keyValues) {
        if ((keyValues.length % 2) != 0) {
            throw new java.lang.RuntimeException("Key-Value arguments passed into setKeyValues must be " + "an alternating list of keys and values!");
        }
        for (int i = 0; i < keyValues.length; i += 2) {
            if (!(keyValues[i] instanceof java.lang.String)) {
                throw new java.lang.RuntimeException((((("Key-value argument " + i) + " must be a key of type ") + "String, but found an object of type ") + keyValues[i].getClass()) + "!");
            }
            java.lang.String key = ((java.lang.String) (keyValues[i]));
            java.lang.Object value = keyValues[i + 1];
            put(key, value);
        }
    }

    public static android.filterfw.core.KeyValueMap fromKeyValues(java.lang.Object... keyValues) {
        android.filterfw.core.KeyValueMap result = new android.filterfw.core.KeyValueMap();
        result.setKeyValues(keyValues);
        return result;
    }

    public java.lang.String getString(java.lang.String key) {
        java.lang.Object result = get(key);
        return result != null ? ((java.lang.String) (result)) : null;
    }

    public int getInt(java.lang.String key) {
        java.lang.Object result = get(key);
        return result != null ? ((java.lang.Integer) (result)) : null;
    }

    public float getFloat(java.lang.String key) {
        java.lang.Object result = get(key);
        return result != null ? ((java.lang.Float) (result)) : null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.io.StringWriter writer = new java.io.StringWriter();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : entrySet()) {
            java.lang.String valueString;
            java.lang.Object value = entry.getValue();
            if (value instanceof java.lang.String) {
                valueString = ("\"" + value) + "\"";
            } else {
                valueString = value.toString();
            }
            writer.write(((entry.getKey() + " = ") + valueString) + ";\n");
        }
        return writer.toString();
    }
}

