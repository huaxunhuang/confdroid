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
package android.os;


/**
 * A mapping from String keys to values of various types. In most cases, you
 * should work directly with either the {@link Bundle} or
 * {@link PersistableBundle} subclass.
 */
public class BaseBundle {
    private static final java.lang.String TAG = "Bundle";

    static final boolean DEBUG = false;

    // Keep in sync with frameworks/native/libs/binder/PersistableBundle.cpp.
    static final int BUNDLE_MAGIC = 0x4c444e42;// 'B' 'N' 'D' 'L'


    /**
     * Flag indicating that this Bundle is okay to "defuse." That is, it's okay
     * for system processes to ignore any {@link BadParcelableException}
     * encountered when unparceling it, leaving an empty bundle in its place.
     * <p>
     * This should <em>only</em> be set when the Bundle reaches its final
     * destination, otherwise a system process may clobber contents that were
     * destined for an app that could have unparceled them.
     */
    static final int FLAG_DEFUSABLE = 1 << 0;

    private static final boolean LOG_DEFUSABLE = false;

    private static volatile boolean sShouldDefuse = false;

    /**
     * Set global variable indicating that any Bundles parsed in this process
     * should be "defused." That is, any {@link BadParcelableException}
     * encountered will be suppressed and logged, leaving an empty Bundle
     * instead of crashing.
     *
     * @unknown 
     */
    public static void setShouldDefuse(boolean shouldDefuse) {
        android.os.BaseBundle.sShouldDefuse = shouldDefuse;
    }

    // A parcel cannot be obtained during compile-time initialization. Put the
    // empty parcel into an inner class that can be initialized separately. This
    // allows to initialize BaseBundle, and classes depending on it.
    /**
     * {@hide }
     */
    static final class NoImagePreloadHolder {
        public static final android.os.Parcel EMPTY_PARCEL = android.os.Parcel.obtain();
    }

    // Invariant - exactly one of mMap / mParcelledData will be null
    // (except inside a call to unparcel)
    android.util.ArrayMap<java.lang.String, java.lang.Object> mMap = null;

    /* If mParcelledData is non-null, then mMap will be null and the
    data are stored as a Parcel containing a Bundle.  When the data
    are unparcelled, mParcelledData willbe set to null.
     */
    android.os.Parcel mParcelledData = null;

    /**
     * The ClassLoader used when unparcelling data from mParcelledData.
     */
    private java.lang.ClassLoader mClassLoader;

    /**
     * {@hide }
     */
    int mFlags;

    /**
     * Constructs a new, empty Bundle that uses a specific ClassLoader for
     * instantiating Parcelable and Serializable objects.
     *
     * @param loader
     * 		An explicit ClassLoader to use when instantiating objects
     * 		inside of the Bundle.
     * @param capacity
     * 		Initial size of the ArrayMap.
     */
    BaseBundle(@android.annotation.Nullable
    java.lang.ClassLoader loader, int capacity) {
        mMap = (capacity > 0) ? new android.util.ArrayMap<java.lang.String, java.lang.Object>(capacity) : new android.util.ArrayMap<java.lang.String, java.lang.Object>();
        mClassLoader = (loader == null) ? getClass().getClassLoader() : loader;
    }

    /**
     * Constructs a new, empty Bundle.
     */
    BaseBundle() {
        this(((java.lang.ClassLoader) (null)), 0);
    }

    /**
     * Constructs a Bundle whose data is stored as a Parcel.  The data
     * will be unparcelled on first contact, using the assigned ClassLoader.
     *
     * @param parcelledData
     * 		a Parcel containing a Bundle
     */
    BaseBundle(android.os.Parcel parcelledData) {
        readFromParcelInner(parcelledData);
    }

    BaseBundle(android.os.Parcel parcelledData, int length) {
        readFromParcelInner(parcelledData, length);
    }

    /**
     * Constructs a new, empty Bundle that uses a specific ClassLoader for
     * instantiating Parcelable and Serializable objects.
     *
     * @param loader
     * 		An explicit ClassLoader to use when instantiating objects
     * 		inside of the Bundle.
     */
    BaseBundle(java.lang.ClassLoader loader) {
        this(loader, 0);
    }

    /**
     * Constructs a new, empty Bundle sized to hold the given number of
     * elements. The Bundle will grow as needed.
     *
     * @param capacity
     * 		the initial capacity of the Bundle
     */
    BaseBundle(int capacity) {
        this(((java.lang.ClassLoader) (null)), capacity);
    }

    /**
     * Constructs a Bundle containing a copy of the mappings from the given
     * Bundle.
     *
     * @param b
     * 		a Bundle to be copied.
     */
    BaseBundle(android.os.BaseBundle b) {
        if (b.mParcelledData != null) {
            if (b.isEmptyParcel()) {
                mParcelledData = android.os.BaseBundle.NoImagePreloadHolder.EMPTY_PARCEL;
            } else {
                mParcelledData = android.os.Parcel.obtain();
                mParcelledData.appendFrom(b.mParcelledData, 0, b.mParcelledData.dataSize());
                mParcelledData.setDataPosition(0);
            }
        } else {
            mParcelledData = null;
        }
        if (b.mMap != null) {
            mMap = new android.util.ArrayMap<>(b.mMap);
        } else {
            mMap = null;
        }
        mClassLoader = b.mClassLoader;
    }

    /**
     * TODO: optimize this later (getting just the value part of a Bundle
     * with a single pair) once Bundle.forPair() above is implemented
     * with a special single-value Map implementation/serialization.
     *
     * Note: value in single-pair Bundle may be null.
     *
     * @unknown 
     */
    public java.lang.String getPairValue() {
        unparcel();
        int size = mMap.size();
        if (size > 1) {
            android.util.Log.w(android.os.BaseBundle.TAG, "getPairValue() used on Bundle with multiple pairs.");
        }
        if (size == 0) {
            return null;
        }
        java.lang.Object o = mMap.valueAt(0);
        try {
            return ((java.lang.String) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning("getPairValue()", o, "String", e);
            return null;
        }
    }

    /**
     * Changes the ClassLoader this Bundle uses when instantiating objects.
     *
     * @param loader
     * 		An explicit ClassLoader to use when instantiating objects
     * 		inside of the Bundle.
     */
    void setClassLoader(java.lang.ClassLoader loader) {
        mClassLoader = loader;
    }

    /**
     * Return the ClassLoader currently associated with this Bundle.
     */
    java.lang.ClassLoader getClassLoader() {
        return mClassLoader;
    }

    /**
     * If the underlying data are stored as a Parcel, unparcel them
     * using the currently assigned class loader.
     */
    /* package */
    synchronized void unparcel() {
        synchronized(this) {
            final android.os.Parcel parcelledData = mParcelledData;
            if (parcelledData == null) {
                if (android.os.BaseBundle.DEBUG)
                    android.util.Log.d(android.os.BaseBundle.TAG, ("unparcel " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + ": no parcelled data");

                return;
            }
            if ((android.os.BaseBundle.LOG_DEFUSABLE && android.os.BaseBundle.sShouldDefuse) && ((mFlags & android.os.BaseBundle.FLAG_DEFUSABLE) == 0)) {
                android.util.Slog.wtf(android.os.BaseBundle.TAG, "Attempting to unparcel a Bundle while in transit; this may " + "clobber all data inside!", new java.lang.Throwable());
            }
            if (isEmptyParcel()) {
                if (android.os.BaseBundle.DEBUG)
                    android.util.Log.d(android.os.BaseBundle.TAG, ("unparcel " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + ": empty");

                if (mMap == null) {
                    mMap = new android.util.ArrayMap<>(1);
                } else {
                    mMap.erase();
                }
                mParcelledData = null;
                return;
            }
            int N = parcelledData.readInt();
            if (android.os.BaseBundle.DEBUG)
                android.util.Log.d(android.os.BaseBundle.TAG, ((("unparcel " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + ": reading ") + N) + " maps");

            if (N < 0) {
                return;
            }
            android.util.ArrayMap<java.lang.String, java.lang.Object> map = mMap;
            if (map == null) {
                map = new android.util.ArrayMap<>(N);
            } else {
                map.erase();
                map.ensureCapacity(N);
            }
            try {
                parcelledData.readArrayMapInternal(map, N, mClassLoader);
            } catch (android.os.BadParcelableException e) {
                if (android.os.BaseBundle.sShouldDefuse) {
                    android.util.Log.w(android.os.BaseBundle.TAG, "Failed to parse Bundle, but defusing quietly", e);
                    map.erase();
                } else {
                    throw e;
                }
            } finally {
                mMap = map;
                parcelledData.recycle();
                mParcelledData = null;
            }
            if (android.os.BaseBundle.DEBUG)
                android.util.Log.d(android.os.BaseBundle.TAG, (("unparcel " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " final map: ") + mMap);

        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isParcelled() {
        return mParcelledData != null;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isEmptyParcel() {
        return mParcelledData == android.os.BaseBundle.NoImagePreloadHolder.EMPTY_PARCEL;
    }

    /**
     *
     *
     * @unknown 
     */
    android.util.ArrayMap<java.lang.String, java.lang.Object> getMap() {
        unparcel();
        return mMap;
    }

    /**
     * Returns the number of mappings contained in this Bundle.
     *
     * @return the number of mappings as an int.
     */
    public int size() {
        unparcel();
        return mMap.size();
    }

    /**
     * Returns true if the mapping of this Bundle is empty, false otherwise.
     */
    public boolean isEmpty() {
        unparcel();
        return mMap.isEmpty();
    }

    /**
     * Removes all elements from the mapping of this Bundle.
     */
    public void clear() {
        unparcel();
        mMap.clear();
    }

    /**
     * Returns true if the given key is contained in the mapping
     * of this Bundle.
     *
     * @param key
     * 		a String key
     * @return true if the key is part of the mapping, false otherwise
     */
    public boolean containsKey(java.lang.String key) {
        unparcel();
        return mMap.containsKey(key);
    }

    /**
     * Returns the entry with the given key as an object.
     *
     * @param key
     * 		a String key
     * @return an Object, or null
     */
    @android.annotation.Nullable
    public java.lang.Object get(java.lang.String key) {
        unparcel();
        return mMap.get(key);
    }

    /**
     * Removes any entry with the given key from the mapping of this Bundle.
     *
     * @param key
     * 		a String key
     */
    public void remove(java.lang.String key) {
        unparcel();
        mMap.remove(key);
    }

    /**
     * Inserts all mappings from the given PersistableBundle into this BaseBundle.
     *
     * @param bundle
     * 		a PersistableBundle
     */
    public void putAll(android.os.PersistableBundle bundle) {
        unparcel();
        bundle.unparcel();
        mMap.putAll(bundle.mMap);
    }

    /**
     * Inserts all mappings from the given Map into this BaseBundle.
     *
     * @param map
     * 		a Map
     */
    void putAll(android.util.ArrayMap map) {
        unparcel();
        mMap.putAll(map);
    }

    /**
     * Returns a Set containing the Strings used as keys in this Bundle.
     *
     * @return a Set of String keys
     */
    public java.util.Set<java.lang.String> keySet() {
        unparcel();
        return mMap.keySet();
    }

    /**
     * Inserts a Boolean value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a boolean
     */
    public void putBoolean(@android.annotation.Nullable
    java.lang.String key, boolean value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a byte value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a byte
     */
    void putByte(@android.annotation.Nullable
    java.lang.String key, byte value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a char value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a char
     */
    void putChar(@android.annotation.Nullable
    java.lang.String key, char value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a short value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a short
     */
    void putShort(@android.annotation.Nullable
    java.lang.String key, short value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an int value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an int
     */
    public void putInt(@android.annotation.Nullable
    java.lang.String key, int value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a long value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a long
     */
    public void putLong(@android.annotation.Nullable
    java.lang.String key, long value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a float value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a float
     */
    void putFloat(@android.annotation.Nullable
    java.lang.String key, float value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a double value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a double
     */
    public void putDouble(@android.annotation.Nullable
    java.lang.String key, double value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a String value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a String, or null
     */
    public void putString(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.lang.String value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a CharSequence value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a CharSequence, or null
     */
    void putCharSequence(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.lang.CharSequence value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an ArrayList<Integer> value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an ArrayList<Integer> object, or null
     */
    void putIntegerArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<java.lang.Integer> value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an ArrayList<String> value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an ArrayList<String> object, or null
     */
    void putStringArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<java.lang.String> value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an ArrayList<CharSequence> value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an ArrayList<CharSequence> object, or null
     */
    void putCharSequenceArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<java.lang.CharSequence> value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a Serializable value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a Serializable object, or null
     */
    void putSerializable(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.io.Serializable value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a boolean array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a boolean array object, or null
     */
    public void putBooleanArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    boolean[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a byte array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a byte array object, or null
     */
    void putByteArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    byte[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a short array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a short array object, or null
     */
    void putShortArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    short[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a char array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a char array object, or null
     */
    void putCharArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    char[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an int array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an int array object, or null
     */
    public void putIntArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    int[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a long array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a long array object, or null
     */
    public void putLongArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    long[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a float array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a float array object, or null
     */
    void putFloatArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    float[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a double array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a double array object, or null
     */
    public void putDoubleArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    double[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a String array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a String array object, or null
     */
    public void putStringArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.lang.String[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a CharSequence array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a CharSequence array object, or null
     */
    void putCharSequenceArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.lang.CharSequence[] value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Returns the value associated with the given key, or false if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a boolean value
     */
    public boolean getBoolean(java.lang.String key) {
        unparcel();
        if (android.os.BaseBundle.DEBUG)
            android.util.Log.d(android.os.BaseBundle.TAG, "Getting boolean in " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));

        return getBoolean(key, false);
    }

    // Log a message if the value was non-null but not of the expected type
    void typeWarning(java.lang.String key, java.lang.Object value, java.lang.String className, java.lang.Object defaultValue, java.lang.ClassCastException e) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        sb.append(" but value was a ");
        sb.append(value.getClass().getName());
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        android.util.Log.w(android.os.BaseBundle.TAG, sb.toString());
        android.util.Log.w(android.os.BaseBundle.TAG, "Attempt to cast generated internal exception:", e);
    }

    void typeWarning(java.lang.String key, java.lang.Object value, java.lang.String className, java.lang.ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a boolean value
     */
    public boolean getBoolean(java.lang.String key, boolean defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Boolean) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Boolean", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or (byte) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a byte value
     */
    byte getByte(java.lang.String key) {
        unparcel();
        return getByte(key, ((byte) (0)));
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a byte value
     */
    java.lang.Byte getByte(java.lang.String key, byte defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Byte) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Byte", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or (char) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a char value
     */
    char getChar(java.lang.String key) {
        unparcel();
        return getChar(key, ((char) (0)));
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a char value
     */
    char getChar(java.lang.String key, char defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Character) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Character", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or (short) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a short value
     */
    short getShort(java.lang.String key) {
        unparcel();
        return getShort(key, ((short) (0)));
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a short value
     */
    short getShort(java.lang.String key, short defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Short) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Short", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return an int value
     */
    public int getInt(java.lang.String key) {
        unparcel();
        return getInt(key, 0);
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return an int value
     */
    public int getInt(java.lang.String key, int defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Integer) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Integer", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or 0L if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a long value
     */
    public long getLong(java.lang.String key) {
        unparcel();
        return getLong(key, 0L);
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a long value
     */
    public long getLong(java.lang.String key, long defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Long) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Long", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or 0.0f if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a float value
     */
    float getFloat(java.lang.String key) {
        unparcel();
        return getFloat(key, 0.0F);
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a float value
     */
    float getFloat(java.lang.String key, float defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Float) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Float", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or 0.0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a double value
     */
    public double getDouble(java.lang.String key) {
        unparcel();
        return getDouble(key, 0.0);
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @param defaultValue
     * 		Value to return if key does not exist
     * @return a double value
     */
    public double getDouble(java.lang.String key, double defaultValue) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((java.lang.Double) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Double", defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a String value, or null
     */
    @android.annotation.Nullable
    public java.lang.String getString(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        final java.lang.Object o = mMap.get(key);
        try {
            return ((java.lang.String) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "String", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key or if a null
     * value is explicitly associated with the given key.
     *
     * @param key
     * 		a String, or null
     * @param defaultValue
     * 		Value to return if key does not exist or if a null
     * 		value is associated with the given key.
     * @return the String value associated with the given key, or defaultValue
    if no valid String object is currently mapped to that key.
     */
    public java.lang.String getString(@android.annotation.Nullable
    java.lang.String key, java.lang.String defaultValue) {
        final java.lang.String s = getString(key);
        return s == null ? defaultValue : s;
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a CharSequence value, or null
     */
    @android.annotation.Nullable
    java.lang.CharSequence getCharSequence(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        final java.lang.Object o = mMap.get(key);
        try {
            return ((java.lang.CharSequence) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "CharSequence", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key or if a null
     * value is explicitly associated with the given key.
     *
     * @param key
     * 		a String, or null
     * @param defaultValue
     * 		Value to return if key does not exist or if a null
     * 		value is associated with the given key.
     * @return the CharSequence value associated with the given key, or defaultValue
    if no valid CharSequence object is currently mapped to that key.
     */
    java.lang.CharSequence getCharSequence(@android.annotation.Nullable
    java.lang.String key, java.lang.CharSequence defaultValue) {
        final java.lang.CharSequence cs = getCharSequence(key);
        return cs == null ? defaultValue : cs;
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a Serializable value, or null
     */
    @android.annotation.Nullable
    java.io.Serializable getSerializable(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.io.Serializable) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Serializable", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return an ArrayList<String> value, or null
     */
    @android.annotation.Nullable
    java.util.ArrayList<java.lang.Integer> getIntegerArrayList(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.util.ArrayList<java.lang.Integer>) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "ArrayList<Integer>", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return an ArrayList<String> value, or null
     */
    @android.annotation.Nullable
    java.util.ArrayList<java.lang.String> getStringArrayList(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.util.ArrayList<java.lang.String>) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "ArrayList<String>", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return an ArrayList<CharSequence> value, or null
     */
    @android.annotation.Nullable
    java.util.ArrayList<java.lang.CharSequence> getCharSequenceArrayList(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.util.ArrayList<java.lang.CharSequence>) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "ArrayList<CharSequence>", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a boolean[] value, or null
     */
    @android.annotation.Nullable
    public boolean[] getBooleanArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((boolean[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a byte[] value, or null
     */
    @android.annotation.Nullable
    byte[] getByteArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((byte[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a short[] value, or null
     */
    @android.annotation.Nullable
    short[] getShortArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((short[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "short[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a char[] value, or null
     */
    @android.annotation.Nullable
    char[] getCharArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((char[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "char[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return an int[] value, or null
     */
    @android.annotation.Nullable
    public int[] getIntArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((int[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "int[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a long[] value, or null
     */
    @android.annotation.Nullable
    public long[] getLongArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((long[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "long[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a float[] value, or null
     */
    @android.annotation.Nullable
    float[] getFloatArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((float[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "float[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a double[] value, or null
     */
    @android.annotation.Nullable
    public double[] getDoubleArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((double[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "double[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a String[] value, or null
     */
    @android.annotation.Nullable
    public java.lang.String[] getStringArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.lang.String[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "String[]", e);
            return null;
        }
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a CharSequence[] value, or null
     */
    @android.annotation.Nullable
    java.lang.CharSequence[] getCharSequenceArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.lang.CharSequence[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "CharSequence[]", e);
            return null;
        }
    }

    /**
     * Writes the Bundle contents to a Parcel, typically in order for
     * it to be passed through an IBinder connection.
     *
     * @param parcel
     * 		The parcel to copy this bundle to.
     */
    void writeToParcelInner(android.os.Parcel parcel, int flags) {
        // Keep implementation in sync with writeToParcel() in
        // frameworks/native/libs/binder/PersistableBundle.cpp.
        final android.os.Parcel parcelledData;
        synchronized(this) {
            parcelledData = mParcelledData;
        }
        if (parcelledData != null) {
            if (isEmptyParcel()) {
                parcel.writeInt(0);
            } else {
                int length = parcelledData.dataSize();
                parcel.writeInt(length);
                parcel.writeInt(android.os.BaseBundle.BUNDLE_MAGIC);
                parcel.appendFrom(parcelledData, 0, length);
            }
        } else {
            // Special case for empty bundles.
            if ((mMap == null) || (mMap.size() <= 0)) {
                parcel.writeInt(0);
                return;
            }
            int lengthPos = parcel.dataPosition();
            parcel.writeInt(-1);// dummy, will hold length

            parcel.writeInt(android.os.BaseBundle.BUNDLE_MAGIC);
            int startPos = parcel.dataPosition();
            parcel.writeArrayMapInternal(mMap);
            int endPos = parcel.dataPosition();
            // Backpatch length
            parcel.setDataPosition(lengthPos);
            int length = endPos - startPos;
            parcel.writeInt(length);
            parcel.setDataPosition(endPos);
        }
    }

    /**
     * Reads the Parcel contents into this Bundle, typically in order for
     * it to be passed through an IBinder connection.
     *
     * @param parcel
     * 		The parcel to overwrite this bundle from.
     */
    void readFromParcelInner(android.os.Parcel parcel) {
        // Keep implementation in sync with readFromParcel() in
        // frameworks/native/libs/binder/PersistableBundle.cpp.
        int length = parcel.readInt();
        readFromParcelInner(parcel, length);
    }

    private void readFromParcelInner(android.os.Parcel parcel, int length) {
        if (length < 0) {
            throw new java.lang.RuntimeException("Bad length in parcel: " + length);
        } else
            if (length == 0) {
                // Empty Bundle or end of data.
                mParcelledData = android.os.BaseBundle.NoImagePreloadHolder.EMPTY_PARCEL;
                return;
            }

        final int magic = parcel.readInt();
        if (magic != android.os.BaseBundle.BUNDLE_MAGIC) {
            throw new java.lang.IllegalStateException("Bad magic number for Bundle: 0x" + java.lang.Integer.toHexString(magic));
        }
        // Advance within this Parcel
        int offset = parcel.dataPosition();
        parcel.setDataPosition(android.util.MathUtils.addOrThrow(offset, length));
        android.os.Parcel p = android.os.Parcel.obtain();
        p.setDataPosition(0);
        p.appendFrom(parcel, offset, length);
        if (android.os.BaseBundle.DEBUG)
            android.util.Log.d(android.os.BaseBundle.TAG, (((("Retrieving " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + ": ") + length) + " bundle bytes starting at ") + offset);

        p.setDataPosition(0);
        mParcelledData = p;
    }
}

