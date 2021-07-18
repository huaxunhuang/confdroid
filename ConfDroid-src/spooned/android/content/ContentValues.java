/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.content;


/**
 * This class is used to store a set of values that the {@link ContentResolver}
 * can process.
 */
public final class ContentValues implements android.os.Parcelable {
    public static final java.lang.String TAG = "ContentValues";

    /**
     *
     *
     * @unknown 
     * @deprecated kept around for lame people doing reflection
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    private java.util.HashMap<java.lang.String, java.lang.Object> mValues;

    private final android.util.ArrayMap<java.lang.String, java.lang.Object> mMap;

    /**
     * Creates an empty set of values using the default initial size
     */
    public ContentValues() {
        mMap = new android.util.ArrayMap();
    }

    /**
     * Creates an empty set of values using the given initial size
     *
     * @param size
     * 		the initial size of the set of values
     */
    public ContentValues(int size) {
        com.android.internal.util.Preconditions.checkArgumentNonnegative(size);
        mMap = new android.util.ArrayMap(size);
    }

    /**
     * Creates a set of values copied from the given set
     *
     * @param from
     * 		the values to copy
     */
    public ContentValues(android.content.ContentValues from) {
        java.util.Objects.requireNonNull(from);
        mMap = new android.util.ArrayMap(from.mMap);
    }

    /**
     *
     *
     * @unknown 
     * @deprecated kept around for lame people doing reflection
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    private ContentValues(java.util.HashMap<java.lang.String, java.lang.Object> from) {
        mMap = new android.util.ArrayMap();
        mMap.putAll(from);
    }

    /**
     * {@hide }
     */
    private ContentValues(android.os.Parcel in) {
        mMap = new android.util.ArrayMap(in.readInt());
        in.readArrayMap(mMap, null);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof android.content.ContentValues)) {
            return false;
        }
        return mMap.equals(((android.content.ContentValues) (object)).mMap);
    }

    /**
     * {@hide }
     */
    public android.util.ArrayMap<java.lang.String, java.lang.Object> getValues() {
        return mMap;
    }

    @java.lang.Override
    public int hashCode() {
        return mMap.hashCode();
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.String value) {
        mMap.put(key, value);
    }

    /**
     * Adds all values from the passed in ContentValues.
     *
     * @param other
     * 		the ContentValues from which to copy
     */
    public void putAll(android.content.ContentValues other) {
        mMap.putAll(other.mMap);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Byte value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Short value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Integer value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Long value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Float value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Double value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, java.lang.Boolean value) {
        mMap.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * 		the name of the value to put
     * @param value
     * 		the data for the value to put
     */
    public void put(java.lang.String key, byte[] value) {
        mMap.put(key, value);
    }

    /**
     * Adds a null value to the set.
     *
     * @param key
     * 		the name of the value to make null
     */
    public void putNull(java.lang.String key) {
        mMap.put(key, null);
    }

    /**
     * Returns the number of values.
     *
     * @return the number of values
     */
    public int size() {
        return mMap.size();
    }

    /**
     * Indicates whether this collection is empty.
     *
     * @return true iff size == 0
    {@hide }
    TODO: consider exposing this new method publicly
     */
    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    /**
     * Remove a single value.
     *
     * @param key
     * 		the name of the value to remove
     */
    public void remove(java.lang.String key) {
        mMap.remove(key);
    }

    /**
     * Removes all values.
     */
    public void clear() {
        mMap.clear();
    }

    /**
     * Returns true if this object has the named value.
     *
     * @param key
     * 		the value to check for
     * @return {@code true} if the value is present, {@code false} otherwise
     */
    public boolean containsKey(java.lang.String key) {
        return mMap.containsKey(key);
    }

    /**
     * Gets a value. Valid value types are {@link String}, {@link Boolean},
     * {@link Number}, and {@code byte[]} implementations.
     *
     * @param key
     * 		the value to get
     * @return the data for the value, or {@code null} if the value is missing or if {@code null}
    was previously added with the given {@code key}
     */
    public java.lang.Object get(java.lang.String key) {
        return mMap.get(key);
    }

    /**
     * Gets a value and converts it to a String.
     *
     * @param key
     * 		the value to get
     * @return the String for the value
     */
    public java.lang.String getAsString(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Gets a value and converts it to a Long.
     *
     * @param key
     * 		the value to get
     * @return the Long value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Long getAsLong(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return value != null ? ((java.lang.Number) (value)).longValue() : null;
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                try {
                    return java.lang.Long.valueOf(value.toString());
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot parse Long value for " + value) + " at key ") + key);
                    return null;
                }
            } else {
                android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Long: ") + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to an Integer.
     *
     * @param key
     * 		the value to get
     * @return the Integer value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Integer getAsInteger(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return value != null ? ((java.lang.Number) (value)).intValue() : null;
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                try {
                    return java.lang.Integer.valueOf(value.toString());
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot parse Integer value for " + value) + " at key ") + key);
                    return null;
                }
            } else {
                android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Integer: ") + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Short.
     *
     * @param key
     * 		the value to get
     * @return the Short value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Short getAsShort(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return value != null ? ((java.lang.Number) (value)).shortValue() : null;
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                try {
                    return java.lang.Short.valueOf(value.toString());
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot parse Short value for " + value) + " at key ") + key);
                    return null;
                }
            } else {
                android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Short: ") + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Byte.
     *
     * @param key
     * 		the value to get
     * @return the Byte value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Byte getAsByte(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return value != null ? ((java.lang.Number) (value)).byteValue() : null;
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                try {
                    return java.lang.Byte.valueOf(value.toString());
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot parse Byte value for " + value) + " at key ") + key);
                    return null;
                }
            } else {
                android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Byte: ") + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Double.
     *
     * @param key
     * 		the value to get
     * @return the Double value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Double getAsDouble(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return value != null ? ((java.lang.Number) (value)).doubleValue() : null;
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                try {
                    return java.lang.Double.valueOf(value.toString());
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot parse Double value for " + value) + " at key ") + key);
                    return null;
                }
            } else {
                android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Double: ") + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Float.
     *
     * @param key
     * 		the value to get
     * @return the Float value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Float getAsFloat(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return value != null ? ((java.lang.Number) (value)).floatValue() : null;
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                try {
                    return java.lang.Float.valueOf(value.toString());
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot parse Float value for " + value) + " at key ") + key);
                    return null;
                }
            } else {
                android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Float: ") + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Boolean.
     *
     * @param key
     * 		the value to get
     * @return the Boolean value, or {@code null} if the value is missing or cannot be converted
     */
    public java.lang.Boolean getAsBoolean(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        try {
            return ((java.lang.Boolean) (value));
        } catch (java.lang.ClassCastException e) {
            if (value instanceof java.lang.CharSequence) {
                // Note that we also check against 1 here because SQLite's internal representation
                // for booleans is an integer with a value of 0 or 1. Without this check, boolean
                // values obtained via DatabaseUtils#cursorRowToContentValues will always return
                // false.
                return java.lang.Boolean.valueOf(value.toString()) || "1".equals(value);
            } else
                if (value instanceof java.lang.Number) {
                    return ((java.lang.Number) (value)).intValue() != 0;
                } else {
                    android.util.Log.e(android.content.ContentValues.TAG, (("Cannot cast value for " + key) + " to a Boolean: ") + value, e);
                    return null;
                }

        }
    }

    /**
     * Gets a value that is a byte array. Note that this method will not convert
     * any other types to byte arrays.
     *
     * @param key
     * 		the value to get
     * @return the {@code byte[]} value, or {@code null} is the value is missing or not a
    {@code byte[]}
     */
    public byte[] getAsByteArray(java.lang.String key) {
        java.lang.Object value = mMap.get(key);
        if (value instanceof byte[]) {
            return ((byte[]) (value));
        } else {
            return null;
        }
    }

    /**
     * Returns a set of all of the keys and values
     *
     * @return a set of all of the keys and values
     */
    public java.util.Set<java.util.Map.Entry<java.lang.String, java.lang.Object>> valueSet() {
        return mMap.entrySet();
    }

    /**
     * Returns a set of all of the keys
     *
     * @return a set of all of the keys
     */
    public java.util.Set<java.lang.String> keySet() {
        return mMap.keySet();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.ContentValues> CREATOR = new android.os.Parcelable.Creator<android.content.ContentValues>() {
        @java.lang.Override
        public android.content.ContentValues createFromParcel(android.os.Parcel in) {
            return new android.content.ContentValues(in);
        }

        @java.lang.Override
        public android.content.ContentValues[] newArray(int size) {
            return new android.content.ContentValues[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mMap.size());
        parcel.writeArrayMap(mMap);
    }

    /**
     * Unsupported, here until we get proper bulk insert APIs.
     * {@hide }
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public void putStringArrayList(java.lang.String key, java.util.ArrayList<java.lang.String> value) {
        mMap.put(key, value);
    }

    /**
     * Unsupported, here until we get proper bulk insert APIs.
     * {@hide }
     */
    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public java.util.ArrayList<java.lang.String> getStringArrayList(java.lang.String key) {
        return ((java.util.ArrayList<java.lang.String>) (mMap.get(key)));
    }

    /**
     * Returns a string containing a concise, human-readable description of this object.
     *
     * @return a printable representation of this object.
     */
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String name : mMap.keySet()) {
            java.lang.String value = getAsString(name);
            if (sb.length() > 0)
                sb.append(" ");

            sb.append((name + "=") + value);
        }
        return sb.toString();
    }
}

