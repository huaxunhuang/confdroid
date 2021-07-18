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
package android.os;


/**
 * A mapping from String keys to various {@link Parcelable} values.
 *
 * @see PersistableBundle
 */
public final class Bundle extends android.os.BaseBundle implements android.os.Parcelable , java.lang.Cloneable {
    private static final int FLAG_HAS_FDS = 1 << 8;

    private static final int FLAG_HAS_FDS_KNOWN = 1 << 9;

    private static final int FLAG_ALLOW_FDS = 1 << 10;

    public static final android.os.Bundle EMPTY;

    static {
        EMPTY = new android.os.Bundle();
        android.os.Bundle.EMPTY.mMap = android.util.ArrayMap.EMPTY;
    }

    /**
     * Constructs a new, empty Bundle.
     */
    public Bundle() {
        super();
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
    }

    /**
     * Constructs a Bundle whose data is stored as a Parcel.  The data
     * will be unparcelled on first contact, using the assigned ClassLoader.
     *
     * @param parcelledData
     * 		a Parcel containing a Bundle
     */
    Bundle(android.os.Parcel parcelledData) {
        super(parcelledData);
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
        if (mParcelledData.hasFileDescriptors()) {
            mFlags |= android.os.Bundle.FLAG_HAS_FDS;
        }
    }

    /* package */
    Bundle(android.os.Parcel parcelledData, int length) {
        super(parcelledData, length);
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
        if (mParcelledData.hasFileDescriptors()) {
            mFlags |= android.os.Bundle.FLAG_HAS_FDS;
        }
    }

    /**
     * Constructs a new, empty Bundle that uses a specific ClassLoader for
     * instantiating Parcelable and Serializable objects.
     *
     * @param loader
     * 		An explicit ClassLoader to use when instantiating objects
     * 		inside of the Bundle.
     */
    public Bundle(java.lang.ClassLoader loader) {
        super(loader);
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
    }

    /**
     * Constructs a new, empty Bundle sized to hold the given number of
     * elements. The Bundle will grow as needed.
     *
     * @param capacity
     * 		the initial capacity of the Bundle
     */
    public Bundle(int capacity) {
        super(capacity);
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
    }

    /**
     * Constructs a Bundle containing a copy of the mappings from the given
     * Bundle.
     *
     * @param b
     * 		a Bundle to be copied.
     */
    public Bundle(android.os.Bundle b) {
        super(b);
        mFlags = b.mFlags;
    }

    /**
     * Constructs a Bundle containing a copy of the mappings from the given
     * PersistableBundle.
     *
     * @param b
     * 		a Bundle to be copied.
     */
    public Bundle(android.os.PersistableBundle b) {
        super(b);
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
    }

    /**
     * Make a Bundle for a single key/value pair.
     *
     * @unknown 
     */
    public static android.os.Bundle forPair(java.lang.String key, java.lang.String value) {
        android.os.Bundle b = new android.os.Bundle(1);
        b.putString(key, value);
        return b;
    }

    /**
     * Changes the ClassLoader this Bundle uses when instantiating objects.
     *
     * @param loader
     * 		An explicit ClassLoader to use when instantiating objects
     * 		inside of the Bundle.
     */
    @java.lang.Override
    public void setClassLoader(java.lang.ClassLoader loader) {
        super.setClassLoader(loader);
    }

    /**
     * Return the ClassLoader currently associated with this Bundle.
     */
    @java.lang.Override
    public java.lang.ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    /**
     * {@hide }
     */
    public boolean setAllowFds(boolean allowFds) {
        final boolean orig = (mFlags & android.os.Bundle.FLAG_ALLOW_FDS) != 0;
        if (allowFds) {
            mFlags |= android.os.Bundle.FLAG_ALLOW_FDS;
        } else {
            mFlags &= ~android.os.Bundle.FLAG_ALLOW_FDS;
        }
        return orig;
    }

    /**
     * Mark if this Bundle is okay to "defuse." That is, it's okay for system
     * processes to ignore any {@link BadParcelableException} encountered when
     * unparceling it, leaving an empty bundle in its place.
     * <p>
     * This should <em>only</em> be set when the Bundle reaches its final
     * destination, otherwise a system process may clobber contents that were
     * destined for an app that could have unparceled them.
     *
     * @unknown 
     */
    public void setDefusable(boolean defusable) {
        if (defusable) {
            mFlags |= android.os.BaseBundle.FLAG_DEFUSABLE;
        } else {
            mFlags &= ~android.os.BaseBundle.FLAG_DEFUSABLE;
        }
    }

    /**
     * {@hide }
     */
    public static android.os.Bundle setDefusable(android.os.Bundle bundle, boolean defusable) {
        if (bundle != null) {
            bundle.setDefusable(defusable);
        }
        return bundle;
    }

    /**
     * Clones the current Bundle. The internal map is cloned, but the keys and
     * values to which it refers are copied by reference.
     */
    @java.lang.Override
    public java.lang.Object clone() {
        return new android.os.Bundle(this);
    }

    /**
     * Removes all elements from the mapping of this Bundle.
     */
    @java.lang.Override
    public void clear() {
        super.clear();
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
    }

    /**
     * Removes any entry with the given key from the mapping of this Bundle.
     *
     * @param key
     * 		a String key
     */
    public void remove(java.lang.String key) {
        super.remove(key);
        if ((mFlags & android.os.Bundle.FLAG_HAS_FDS) != 0) {
            mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
        }
    }

    /**
     * Inserts all mappings from the given Bundle into this Bundle.
     *
     * @param bundle
     * 		a Bundle
     */
    public void putAll(android.os.Bundle bundle) {
        unparcel();
        bundle.unparcel();
        mMap.putAll(bundle.mMap);
        // FD state is now known if and only if both bundles already knew
        if ((bundle.mFlags & android.os.Bundle.FLAG_HAS_FDS) != 0) {
            mFlags |= android.os.Bundle.FLAG_HAS_FDS;
        }
        if ((bundle.mFlags & android.os.Bundle.FLAG_HAS_FDS_KNOWN) == 0) {
            mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
        }
    }

    /**
     * Reports whether the bundle contains any parcelled file descriptors.
     */
    public boolean hasFileDescriptors() {
        if ((mFlags & android.os.Bundle.FLAG_HAS_FDS_KNOWN) == 0) {
            boolean fdFound = false;// keep going until we find one or run out of data

            if (mParcelledData != null) {
                if (mParcelledData.hasFileDescriptors()) {
                    fdFound = true;
                }
            } else {
                // It's been unparcelled, so we need to walk the map
                for (int i = mMap.size() - 1; i >= 0; i--) {
                    java.lang.Object obj = mMap.valueAt(i);
                    if (obj instanceof android.os.Parcelable) {
                        if ((((android.os.Parcelable) (obj)).describeContents() & android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR) != 0) {
                            fdFound = true;
                            break;
                        }
                    } else
                        if (obj instanceof android.os.Parcelable[]) {
                            android.os.Parcelable[] array = ((android.os.Parcelable[]) (obj));
                            for (int n = array.length - 1; n >= 0; n--) {
                                android.os.Parcelable p = array[n];
                                if ((p != null) && ((p.describeContents() & android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR) != 0)) {
                                    fdFound = true;
                                    break;
                                }
                            }
                        } else
                            if (obj instanceof android.util.SparseArray) {
                                android.util.SparseArray<? extends android.os.Parcelable> array = ((android.util.SparseArray<? extends android.os.Parcelable>) (obj));
                                for (int n = array.size() - 1; n >= 0; n--) {
                                    android.os.Parcelable p = array.valueAt(n);
                                    if ((p != null) && ((p.describeContents() & android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR) != 0)) {
                                        fdFound = true;
                                        break;
                                    }
                                }
                            } else
                                if (obj instanceof java.util.ArrayList) {
                                    java.util.ArrayList array = ((java.util.ArrayList) (obj));
                                    // an ArrayList here might contain either Strings or
                                    // Parcelables; only look inside for Parcelables
                                    if ((!array.isEmpty()) && (array.get(0) instanceof android.os.Parcelable)) {
                                        for (int n = array.size() - 1; n >= 0; n--) {
                                            android.os.Parcelable p = ((android.os.Parcelable) (array.get(n)));
                                            if ((p != null) && ((p.describeContents() & android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR) != 0)) {
                                                fdFound = true;
                                                break;
                                            }
                                        }
                                    }
                                }



                }
            }
            if (fdFound) {
                mFlags |= android.os.Bundle.FLAG_HAS_FDS;
            } else {
                mFlags &= ~android.os.Bundle.FLAG_HAS_FDS;
            }
            mFlags |= android.os.Bundle.FLAG_HAS_FDS_KNOWN;
        }
        return (mFlags & android.os.Bundle.FLAG_HAS_FDS) != 0;
    }

    /**
     * Filter values in Bundle to only basic types.
     *
     * @unknown 
     */
    public android.os.Bundle filterValues() {
        unparcel();
        android.os.Bundle bundle = this;
        if (mMap != null) {
            android.util.ArrayMap<java.lang.String, java.lang.Object> map = mMap;
            for (int i = map.size() - 1; i >= 0; i--) {
                java.lang.Object value = map.valueAt(i);
                if (android.os.PersistableBundle.isValidType(value)) {
                    continue;
                }
                if (value instanceof android.os.Bundle) {
                    android.os.Bundle newBundle = ((android.os.Bundle) (value)).filterValues();
                    if (newBundle != value) {
                        if (map == mMap) {
                            // The filter had to generate a new bundle, but we have not yet
                            // created a new one here.  Do that now.
                            bundle = new android.os.Bundle(this);
                            // Note the ArrayMap<> constructor is guaranteed to generate
                            // a new object with items in the same order as the original.
                            map = bundle.mMap;
                        }
                        // Replace this current entry with the new child bundle.
                        map.setValueAt(i, newBundle);
                    }
                    continue;
                }
                if (value.getClass().getName().startsWith("android.")) {
                    continue;
                }
                if (map == mMap) {
                    // This is the first time we have had to remove something, that means we
                    // need to switch to a new Bundle.
                    bundle = new android.os.Bundle(this);
                    // Note the ArrayMap<> constructor is guaranteed to generate
                    // a new object with items in the same order as the original.
                    map = bundle.mMap;
                }
                map.removeAt(i);
            }
        }
        mFlags |= android.os.Bundle.FLAG_HAS_FDS_KNOWN;
        mFlags &= ~android.os.Bundle.FLAG_HAS_FDS;
        return bundle;
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
    @java.lang.Override
    public void putByte(@android.annotation.Nullable
    java.lang.String key, byte value) {
        super.putByte(key, value);
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
    @java.lang.Override
    public void putChar(@android.annotation.Nullable
    java.lang.String key, char value) {
        super.putChar(key, value);
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
    @java.lang.Override
    public void putShort(@android.annotation.Nullable
    java.lang.String key, short value) {
        super.putShort(key, value);
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
    @java.lang.Override
    public void putFloat(@android.annotation.Nullable
    java.lang.String key, float value) {
        super.putFloat(key, value);
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
    @java.lang.Override
    public void putCharSequence(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.lang.CharSequence value) {
        super.putCharSequence(key, value);
    }

    /**
     * Inserts a Parcelable value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a Parcelable object, or null
     */
    public void putParcelable(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.os.Parcelable value) {
        unparcel();
        mMap.put(key, value);
        mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
    }

    /**
     * Inserts a Size value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a Size object, or null
     */
    public void putSize(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.util.Size value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts a SizeF value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a SizeF object, or null
     */
    public void putSizeF(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.util.SizeF value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an array of Parcelable values into the mapping of this Bundle,
     * replacing any existing value for the given key.  Either key or value may
     * be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an array of Parcelable objects, or null
     */
    public void putParcelableArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.os.Parcelable[] value) {
        unparcel();
        mMap.put(key, value);
        mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
    }

    /**
     * Inserts a List of Parcelable values into the mapping of this Bundle,
     * replacing any existing value for the given key.  Either key or value may
     * be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an ArrayList of Parcelable objects, or null
     */
    public void putParcelableArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<? extends android.os.Parcelable> value) {
        unparcel();
        mMap.put(key, value);
        mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
    }

    /**
     * {@hide }
     */
    public void putParcelableList(java.lang.String key, java.util.List<? extends android.os.Parcelable> value) {
        unparcel();
        mMap.put(key, value);
        mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
    }

    /**
     * Inserts a SparceArray of Parcelable values into the mapping of this
     * Bundle, replacing any existing value for the given key.  Either key
     * or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a SparseArray of Parcelable objects, or null
     */
    public void putSparseParcelableArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.util.SparseArray<? extends android.os.Parcelable> value) {
        unparcel();
        mMap.put(key, value);
        mFlags &= ~android.os.Bundle.FLAG_HAS_FDS_KNOWN;
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
    @java.lang.Override
    public void putIntegerArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<java.lang.Integer> value) {
        super.putIntegerArrayList(key, value);
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
    @java.lang.Override
    public void putStringArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<java.lang.String> value) {
        super.putStringArrayList(key, value);
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
    @java.lang.Override
    public void putCharSequenceArrayList(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.util.ArrayList<java.lang.CharSequence> value) {
        super.putCharSequenceArrayList(key, value);
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
    @java.lang.Override
    public void putSerializable(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.io.Serializable value) {
        super.putSerializable(key, value);
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
    @java.lang.Override
    public void putByteArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    byte[] value) {
        super.putByteArray(key, value);
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
    @java.lang.Override
    public void putShortArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    short[] value) {
        super.putShortArray(key, value);
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
    @java.lang.Override
    public void putCharArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    char[] value) {
        super.putCharArray(key, value);
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
    @java.lang.Override
    public void putFloatArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    float[] value) {
        super.putFloatArray(key, value);
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
    @java.lang.Override
    public void putCharSequenceArray(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    java.lang.CharSequence[] value) {
        super.putCharSequenceArray(key, value);
    }

    /**
     * Inserts a Bundle value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		a Bundle object, or null
     */
    public void putBundle(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.os.Bundle value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an {@link IBinder} value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * <p class="note">You should be very careful when using this function.  In many
     * places where Bundles are used (such as inside of Intent objects), the Bundle
     * can live longer inside of another process than the process that had originally
     * created it.  In that case, the IBinder you supply here will become invalid
     * when your process goes away, and no longer usable, even if a new process is
     * created for you later on.</p>
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an IBinder object, or null
     */
    public void putBinder(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.os.IBinder value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Inserts an IBinder value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key
     * 		a String, or null
     * @param value
     * 		an IBinder object, or null
     * @deprecated 
     * @unknown This is the old name of the function.
     */
    @java.lang.Deprecated
    public void putIBinder(@android.annotation.Nullable
    java.lang.String key, @android.annotation.Nullable
    android.os.IBinder value) {
        unparcel();
        mMap.put(key, value);
    }

    /**
     * Returns the value associated with the given key, or (byte) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a byte value
     */
    @java.lang.Override
    public byte getByte(java.lang.String key) {
        return super.getByte(key);
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
    @java.lang.Override
    public java.lang.Byte getByte(java.lang.String key, byte defaultValue) {
        return super.getByte(key, defaultValue);
    }

    /**
     * Returns the value associated with the given key, or (char) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a char value
     */
    @java.lang.Override
    public char getChar(java.lang.String key) {
        return super.getChar(key);
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
    @java.lang.Override
    public char getChar(java.lang.String key, char defaultValue) {
        return super.getChar(key, defaultValue);
    }

    /**
     * Returns the value associated with the given key, or (short) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a short value
     */
    @java.lang.Override
    public short getShort(java.lang.String key) {
        return super.getShort(key);
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
    @java.lang.Override
    public short getShort(java.lang.String key, short defaultValue) {
        return super.getShort(key, defaultValue);
    }

    /**
     * Returns the value associated with the given key, or 0.0f if
     * no mapping of the desired type exists for the given key.
     *
     * @param key
     * 		a String
     * @return a float value
     */
    @java.lang.Override
    public float getFloat(java.lang.String key) {
        return super.getFloat(key);
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
    @java.lang.Override
    public float getFloat(java.lang.String key, float defaultValue) {
        return super.getFloat(key, defaultValue);
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
    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.CharSequence getCharSequence(@android.annotation.Nullable
    java.lang.String key) {
        return super.getCharSequence(key);
    }

    /**
     * Returns the value associated with the given key, or defaultValue if
     * no mapping of the desired type exists for the given key or if a null
     * value is explicitly associatd with the given key.
     *
     * @param key
     * 		a String, or null
     * @param defaultValue
     * 		Value to return if key does not exist or if a null
     * 		value is associated with the given key.
     * @return the CharSequence value associated with the given key, or defaultValue
    if no valid CharSequence object is currently mapped to that key.
     */
    @java.lang.Override
    public java.lang.CharSequence getCharSequence(@android.annotation.Nullable
    java.lang.String key, java.lang.CharSequence defaultValue) {
        return super.getCharSequence(key, defaultValue);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return a Size value, or null
     */
    @android.annotation.Nullable
    public android.util.Size getSize(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        final java.lang.Object o = mMap.get(key);
        try {
            return ((android.util.Size) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Size", e);
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
     * @return a Size value, or null
     */
    @android.annotation.Nullable
    public android.util.SizeF getSizeF(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        final java.lang.Object o = mMap.get(key);
        try {
            return ((android.util.SizeF) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "SizeF", e);
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
     * @return a Bundle value, or null
     */
    @android.annotation.Nullable
    public android.os.Bundle getBundle(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((android.os.Bundle) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Bundle", e);
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
     * @return a Parcelable value, or null
     */
    @android.annotation.Nullable
    public <T extends android.os.Parcelable> T getParcelable(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((T) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Parcelable", e);
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
     * @return a Parcelable[] value, or null
     */
    @android.annotation.Nullable
    public android.os.Parcelable[] getParcelableArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((android.os.Parcelable[]) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "Parcelable[]", e);
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
     * @return an ArrayList<T> value, or null
     */
    @android.annotation.Nullable
    public <T extends android.os.Parcelable> java.util.ArrayList<T> getParcelableArrayList(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((java.util.ArrayList<T>) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "ArrayList", e);
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
     * @return a SparseArray of T values, or null
     */
    @android.annotation.Nullable
    public <T extends android.os.Parcelable> android.util.SparseArray<T> getSparseParcelableArray(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((android.util.SparseArray<T>) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "SparseArray", e);
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
     * @return a Serializable value, or null
     */
    @java.lang.Override
    @android.annotation.Nullable
    public java.io.Serializable getSerializable(@android.annotation.Nullable
    java.lang.String key) {
        return super.getSerializable(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public java.util.ArrayList<java.lang.Integer> getIntegerArrayList(@android.annotation.Nullable
    java.lang.String key) {
        return super.getIntegerArrayList(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public java.util.ArrayList<java.lang.String> getStringArrayList(@android.annotation.Nullable
    java.lang.String key) {
        return super.getStringArrayList(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public java.util.ArrayList<java.lang.CharSequence> getCharSequenceArrayList(@android.annotation.Nullable
    java.lang.String key) {
        return super.getCharSequenceArrayList(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public byte[] getByteArray(@android.annotation.Nullable
    java.lang.String key) {
        return super.getByteArray(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public short[] getShortArray(@android.annotation.Nullable
    java.lang.String key) {
        return super.getShortArray(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public char[] getCharArray(@android.annotation.Nullable
    java.lang.String key) {
        return super.getCharArray(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public float[] getFloatArray(@android.annotation.Nullable
    java.lang.String key) {
        return super.getFloatArray(key);
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
    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.CharSequence[] getCharSequenceArray(@android.annotation.Nullable
    java.lang.String key) {
        return super.getCharSequenceArray(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key
     * 		a String, or null
     * @return an IBinder value, or null
     */
    @android.annotation.Nullable
    public android.os.IBinder getBinder(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((android.os.IBinder) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "IBinder", e);
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
     * @return an IBinder value, or null
     * @deprecated 
     * @unknown This is the old name of the function.
     */
    @java.lang.Deprecated
    @android.annotation.Nullable
    public android.os.IBinder getIBinder(@android.annotation.Nullable
    java.lang.String key) {
        unparcel();
        java.lang.Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return ((android.os.IBinder) (o));
        } catch (java.lang.ClassCastException e) {
            typeWarning(key, o, "IBinder", e);
            return null;
        }
    }

    public static final android.os.Parcelable.Creator<android.os.Bundle> CREATOR = new android.os.Parcelable.Creator<android.os.Bundle>() {
        @java.lang.Override
        public android.os.Bundle createFromParcel(android.os.Parcel in) {
            return in.readBundle();
        }

        @java.lang.Override
        public android.os.Bundle[] newArray(int size) {
            return new android.os.Bundle[size];
        }
    };

    /**
     * Report the nature of this Parcelable's contents
     */
    @java.lang.Override
    public int describeContents() {
        int mask = 0;
        if (hasFileDescriptors()) {
            mask |= android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;
        }
        return mask;
    }

    /**
     * Writes the Bundle contents to a Parcel, typically in order for
     * it to be passed through an IBinder connection.
     *
     * @param parcel
     * 		The parcel to copy this bundle to.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        final boolean oldAllowFds = parcel.pushAllowFds((mFlags & android.os.Bundle.FLAG_ALLOW_FDS) != 0);
        try {
            super.writeToParcelInner(parcel, flags);
        } finally {
            parcel.restoreAllowFds(oldAllowFds);
        }
    }

    /**
     * Reads the Parcel contents into this Bundle, typically in order for
     * it to be passed through an IBinder connection.
     *
     * @param parcel
     * 		The parcel to overwrite this bundle from.
     */
    public void readFromParcel(android.os.Parcel parcel) {
        super.readFromParcelInner(parcel);
        mFlags = android.os.Bundle.FLAG_HAS_FDS_KNOWN | android.os.Bundle.FLAG_ALLOW_FDS;
        if (mParcelledData.hasFileDescriptors()) {
            mFlags |= android.os.Bundle.FLAG_HAS_FDS;
        }
    }

    @java.lang.Override
    public synchronized java.lang.String toString() {
        if (mParcelledData != null) {
            if (isEmptyParcel()) {
                return "Bundle[EMPTY_PARCEL]";
            } else {
                return ("Bundle[mParcelledData.dataSize=" + mParcelledData.dataSize()) + "]";
            }
        }
        return ("Bundle[" + mMap.toString()) + "]";
    }
}

