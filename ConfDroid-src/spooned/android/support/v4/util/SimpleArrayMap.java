/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.util;


/**
 * Base implementation of {@link ArrayMap} that doesn't include any standard Java
 * container API interoperability.  These features are generally heavier-weight ways
 * to interact with the container, so discouraged, but they can be useful to make it
 * easier to use as a drop-in replacement for HashMap.  If you don't need them, this
 * class can be preferrable since it doesn't bring in any of the implementation of those
 * APIs, allowing that code to be stripped by ProGuard.
 */
public class SimpleArrayMap<K, V> {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "ArrayMap";

    /**
     * The minimum amount by which the capacity of a ArrayMap will increase.
     * This is tuned to be relatively space-efficient.
     */
    private static final int BASE_SIZE = 4;

    /**
     * Maximum number of entries to have in array caches.
     */
    private static final int CACHE_SIZE = 10;

    /**
     * Caches of small array objects to avoid spamming garbage.  The cache
     * Object[] variable is a pointer to a linked list of array objects.
     * The first entry in the array is a pointer to the next array in the
     * list; the second entry is a pointer to the int[] hash code array for it.
     */
    static java.lang.Object[] mBaseCache;

    static int mBaseCacheSize;

    static java.lang.Object[] mTwiceBaseCache;

    static int mTwiceBaseCacheSize;

    int[] mHashes;

    java.lang.Object[] mArray;

    int mSize;

    int indexOf(java.lang.Object key, int hash) {
        final int N = mSize;
        // Important fast case: if nothing is in here, nothing to look for.
        if (N == 0) {
            return ~0;
        }
        int index = android.support.v4.util.ContainerHelpers.binarySearch(mHashes, N, hash);
        // If the hash code wasn't found, then we have no entry for this key.
        if (index < 0) {
            return index;
        }
        // If the key at the returned index matches, that's what we want.
        if (key.equals(mArray[index << 1])) {
            return index;
        }
        // Search for a matching key after the index.
        int end;
        for (end = index + 1; (end < N) && (mHashes[end] == hash); end++) {
            if (key.equals(mArray[end << 1]))
                return end;

        }
        // Search for a matching key before the index.
        for (int i = index - 1; (i >= 0) && (mHashes[i] == hash); i--) {
            if (key.equals(mArray[i << 1]))
                return i;

        }
        // Key not found -- return negative value indicating where a
        // new entry for this key should go.  We use the end of the
        // hash chain to reduce the number of array entries that will
        // need to be copied when inserting.
        return ~end;
    }

    int indexOfNull() {
        final int N = mSize;
        // Important fast case: if nothing is in here, nothing to look for.
        if (N == 0) {
            return ~0;
        }
        int index = android.support.v4.util.ContainerHelpers.binarySearch(mHashes, N, 0);
        // If the hash code wasn't found, then we have no entry for this key.
        if (index < 0) {
            return index;
        }
        // If the key at the returned index matches, that's what we want.
        if (null == mArray[index << 1]) {
            return index;
        }
        // Search for a matching key after the index.
        int end;
        for (end = index + 1; (end < N) && (mHashes[end] == 0); end++) {
            if (null == mArray[end << 1])
                return end;

        }
        // Search for a matching key before the index.
        for (int i = index - 1; (i >= 0) && (mHashes[i] == 0); i--) {
            if (null == mArray[i << 1])
                return i;

        }
        // Key not found -- return negative value indicating where a
        // new entry for this key should go.  We use the end of the
        // hash chain to reduce the number of array entries that will
        // need to be copied when inserting.
        return ~end;
    }

    private void allocArrays(final int size) {
        if (size == (android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2)) {
            synchronized(android.support.v4.util.ArrayMap.class) {
                if (android.support.v4.util.SimpleArrayMap.mTwiceBaseCache != null) {
                    final java.lang.Object[] array = android.support.v4.util.SimpleArrayMap.mTwiceBaseCache;
                    mArray = array;
                    android.support.v4.util.SimpleArrayMap.mTwiceBaseCache = ((java.lang.Object[]) (array[0]));
                    mHashes = ((int[]) (array[1]));
                    array[0] = array[1] = null;
                    android.support.v4.util.SimpleArrayMap.mTwiceBaseCacheSize--;
                    if (android.support.v4.util.SimpleArrayMap.DEBUG)
                        android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ((("Retrieving 2x cache " + mHashes) + " now have ") + android.support.v4.util.SimpleArrayMap.mTwiceBaseCacheSize) + " entries");

                    return;
                }
            }
        } else
            if (size == android.support.v4.util.SimpleArrayMap.BASE_SIZE) {
                synchronized(android.support.v4.util.ArrayMap.class) {
                    if (android.support.v4.util.SimpleArrayMap.mBaseCache != null) {
                        final java.lang.Object[] array = android.support.v4.util.SimpleArrayMap.mBaseCache;
                        mArray = array;
                        android.support.v4.util.SimpleArrayMap.mBaseCache = ((java.lang.Object[]) (array[0]));
                        mHashes = ((int[]) (array[1]));
                        array[0] = array[1] = null;
                        android.support.v4.util.SimpleArrayMap.mBaseCacheSize--;
                        if (android.support.v4.util.SimpleArrayMap.DEBUG)
                            android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ((("Retrieving 1x cache " + mHashes) + " now have ") + android.support.v4.util.SimpleArrayMap.mBaseCacheSize) + " entries");

                        return;
                    }
                }
            }

        mHashes = new int[size];
        mArray = new java.lang.Object[size << 1];
    }

    private static void freeArrays(final int[] hashes, final java.lang.Object[] array, final int size) {
        if (hashes.length == (android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2)) {
            synchronized(android.support.v4.util.ArrayMap.class) {
                if (android.support.v4.util.SimpleArrayMap.mTwiceBaseCacheSize < android.support.v4.util.SimpleArrayMap.CACHE_SIZE) {
                    array[0] = android.support.v4.util.SimpleArrayMap.mTwiceBaseCache;
                    array[1] = hashes;
                    for (int i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    android.support.v4.util.SimpleArrayMap.mTwiceBaseCache = array;
                    android.support.v4.util.SimpleArrayMap.mTwiceBaseCacheSize++;
                    if (android.support.v4.util.SimpleArrayMap.DEBUG)
                        android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ((("Storing 2x cache " + array) + " now have ") + android.support.v4.util.SimpleArrayMap.mTwiceBaseCacheSize) + " entries");

                }
            }
        } else
            if (hashes.length == android.support.v4.util.SimpleArrayMap.BASE_SIZE) {
                synchronized(android.support.v4.util.ArrayMap.class) {
                    if (android.support.v4.util.SimpleArrayMap.mBaseCacheSize < android.support.v4.util.SimpleArrayMap.CACHE_SIZE) {
                        array[0] = android.support.v4.util.SimpleArrayMap.mBaseCache;
                        array[1] = hashes;
                        for (int i = (size << 1) - 1; i >= 2; i--) {
                            array[i] = null;
                        }
                        android.support.v4.util.SimpleArrayMap.mBaseCache = array;
                        android.support.v4.util.SimpleArrayMap.mBaseCacheSize++;
                        if (android.support.v4.util.SimpleArrayMap.DEBUG)
                            android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ((("Storing 1x cache " + array) + " now have ") + android.support.v4.util.SimpleArrayMap.mBaseCacheSize) + " entries");

                    }
                }
            }

    }

    /**
     * Create a new empty ArrayMap.  The default capacity of an array map is 0, and
     * will grow once items are added to it.
     */
    public SimpleArrayMap() {
        mHashes = android.support.v4.util.ContainerHelpers.EMPTY_INTS;
        mArray = android.support.v4.util.ContainerHelpers.EMPTY_OBJECTS;
        mSize = 0;
    }

    /**
     * Create a new ArrayMap with a given initial capacity.
     */
    public SimpleArrayMap(int capacity) {
        if (capacity == 0) {
            mHashes = android.support.v4.util.ContainerHelpers.EMPTY_INTS;
            mArray = android.support.v4.util.ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(capacity);
        }
        mSize = 0;
    }

    /**
     * Create a new ArrayMap with the mappings from the given ArrayMap.
     */
    public SimpleArrayMap(android.support.v4.util.SimpleArrayMap map) {
        this();
        if (map != null) {
            putAll(map);
        }
    }

    /**
     * Make the array map empty.  All storage is released.
     */
    public void clear() {
        if (mSize != 0) {
            android.support.v4.util.SimpleArrayMap.freeArrays(mHashes, mArray, mSize);
            mHashes = android.support.v4.util.ContainerHelpers.EMPTY_INTS;
            mArray = android.support.v4.util.ContainerHelpers.EMPTY_OBJECTS;
            mSize = 0;
        }
    }

    /**
     * Ensure the array map can hold at least <var>minimumCapacity</var>
     * items.
     */
    public void ensureCapacity(int minimumCapacity) {
        if (mHashes.length < minimumCapacity) {
            final int[] ohashes = mHashes;
            final java.lang.Object[] oarray = mArray;
            allocArrays(minimumCapacity);
            if (mSize > 0) {
                java.lang.System.arraycopy(ohashes, 0, mHashes, 0, mSize);
                java.lang.System.arraycopy(oarray, 0, mArray, 0, mSize << 1);
            }
            android.support.v4.util.SimpleArrayMap.freeArrays(ohashes, oarray, mSize);
        }
    }

    /**
     * Check whether a key exists in the array.
     *
     * @param key
     * 		The key to search for.
     * @return Returns true if the key exists, else false.
     */
    public boolean containsKey(java.lang.Object key) {
        return indexOfKey(key) >= 0;
    }

    /**
     * Returns the index of a key in the set.
     *
     * @param key
     * 		The key to search for.
     * @return Returns the index of the key if it exists, else a negative integer.
     */
    public int indexOfKey(java.lang.Object key) {
        return key == null ? indexOfNull() : indexOf(key, key.hashCode());
    }

    int indexOfValue(java.lang.Object value) {
        final int N = mSize * 2;
        final java.lang.Object[] array = mArray;
        if (value == null) {
            for (int i = 1; i < N; i += 2) {
                if (array[i] == null) {
                    return i >> 1;
                }
            }
        } else {
            for (int i = 1; i < N; i += 2) {
                if (value.equals(array[i])) {
                    return i >> 1;
                }
            }
        }
        return -1;
    }

    /**
     * Check whether a value exists in the array.  This requires a linear search
     * through the entire array.
     *
     * @param value
     * 		The value to search for.
     * @return Returns true if the value exists, else false.
     */
    public boolean containsValue(java.lang.Object value) {
        return indexOfValue(value) >= 0;
    }

    /**
     * Retrieve a value from the array.
     *
     * @param key
     * 		The key of the value to retrieve.
     * @return Returns the value associated with the given key,
    or null if there is no such key.
     */
    public V get(java.lang.Object key) {
        final int index = indexOfKey(key);
        return index >= 0 ? ((V) (mArray[(index << 1) + 1])) : null;
    }

    /**
     * Return the key at the given index in the array.
     *
     * @param index
     * 		The desired index, must be between 0 and {@link #size()}-1.
     * @return Returns the key stored at the given index.
     */
    public K keyAt(int index) {
        return ((K) (mArray[index << 1]));
    }

    /**
     * Return the value at the given index in the array.
     *
     * @param index
     * 		The desired index, must be between 0 and {@link #size()}-1.
     * @return Returns the value stored at the given index.
     */
    public V valueAt(int index) {
        return ((V) (mArray[(index << 1) + 1]));
    }

    /**
     * Set the value at a given index in the array.
     *
     * @param index
     * 		The desired index, must be between 0 and {@link #size()}-1.
     * @param value
     * 		The new value to store at this index.
     * @return Returns the previous value at the given index.
     */
    public V setValueAt(int index, V value) {
        index = (index << 1) + 1;
        V old = ((V) (mArray[index]));
        mArray[index] = value;
        return old;
    }

    /**
     * Return true if the array map contains no items.
     */
    public boolean isEmpty() {
        return mSize <= 0;
    }

    /**
     * Add a new value to the array map.
     *
     * @param key
     * 		The key under which to store the value.  <b>Must not be null.</b>  If
     * 		this key already exists in the array, its value will be replaced.
     * @param value
     * 		The value to store for the given key.
     * @return Returns the old value that was stored for the given key, or null if there
    was no such key.
     */
    public V put(K key, V value) {
        final int hash;
        int index;
        if (key == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = key.hashCode();
            index = indexOf(key, hash);
        }
        if (index >= 0) {
            index = (index << 1) + 1;
            final V old = ((V) (mArray[index]));
            mArray[index] = value;
            return old;
        }
        index = ~index;
        if (mSize >= mHashes.length) {
            final int n = (mSize >= (android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2)) ? mSize + (mSize >> 1) : mSize >= android.support.v4.util.SimpleArrayMap.BASE_SIZE ? android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2 : android.support.v4.util.SimpleArrayMap.BASE_SIZE;
            if (android.support.v4.util.SimpleArrayMap.DEBUG)
                android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, (("put: grow from " + mHashes.length) + " to ") + n);

            final int[] ohashes = mHashes;
            final java.lang.Object[] oarray = mArray;
            allocArrays(n);
            if (mHashes.length > 0) {
                if (android.support.v4.util.SimpleArrayMap.DEBUG)
                    android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ("put: copy 0-" + mSize) + " to 0");

                java.lang.System.arraycopy(ohashes, 0, mHashes, 0, ohashes.length);
                java.lang.System.arraycopy(oarray, 0, mArray, 0, oarray.length);
            }
            android.support.v4.util.SimpleArrayMap.freeArrays(ohashes, oarray, mSize);
        }
        if (index < mSize) {
            if (android.support.v4.util.SimpleArrayMap.DEBUG)
                android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, (((("put: move " + index) + "-") + (mSize - index)) + " to ") + (index + 1));

            java.lang.System.arraycopy(mHashes, index, mHashes, index + 1, mSize - index);
            java.lang.System.arraycopy(mArray, index << 1, mArray, (index + 1) << 1, (mSize - index) << 1);
        }
        mHashes[index] = hash;
        mArray[index << 1] = key;
        mArray[(index << 1) + 1] = value;
        mSize++;
        return null;
    }

    /**
     * Perform a {@link #put(Object, Object)} of all key/value pairs in <var>array</var>
     *
     * @param array
     * 		The array whose contents are to be retrieved.
     */
    public void putAll(android.support.v4.util.SimpleArrayMap<? extends K, ? extends V> array) {
        final int N = array.mSize;
        ensureCapacity(mSize + N);
        if (mSize == 0) {
            if (N > 0) {
                java.lang.System.arraycopy(array.mHashes, 0, mHashes, 0, N);
                java.lang.System.arraycopy(array.mArray, 0, mArray, 0, N << 1);
                mSize = N;
            }
        } else {
            for (int i = 0; i < N; i++) {
                put(array.keyAt(i), array.valueAt(i));
            }
        }
    }

    /**
     * Remove an existing key from the array map.
     *
     * @param key
     * 		The key of the mapping to remove.
     * @return Returns the value that was stored under the key, or null if there
    was no such key.
     */
    public V remove(java.lang.Object key) {
        final int index = indexOfKey(key);
        if (index >= 0) {
            return removeAt(index);
        }
        return null;
    }

    /**
     * Remove the key/value mapping at the given index.
     *
     * @param index
     * 		The desired index, must be between 0 and {@link #size()}-1.
     * @return Returns the value that was stored at this index.
     */
    public V removeAt(int index) {
        final java.lang.Object old = mArray[(index << 1) + 1];
        if (mSize <= 1) {
            // Now empty.
            if (android.support.v4.util.SimpleArrayMap.DEBUG)
                android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ("remove: shrink from " + mHashes.length) + " to 0");

            android.support.v4.util.SimpleArrayMap.freeArrays(mHashes, mArray, mSize);
            mHashes = android.support.v4.util.ContainerHelpers.EMPTY_INTS;
            mArray = android.support.v4.util.ContainerHelpers.EMPTY_OBJECTS;
            mSize = 0;
        } else {
            if ((mHashes.length > (android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2)) && (mSize < (mHashes.length / 3))) {
                // Shrunk enough to reduce size of arrays.  We don't allow it to
                // shrink smaller than (BASE_SIZE*2) to avoid flapping between
                // that and BASE_SIZE.
                final int n = (mSize > (android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2)) ? mSize + (mSize >> 1) : android.support.v4.util.SimpleArrayMap.BASE_SIZE * 2;
                if (android.support.v4.util.SimpleArrayMap.DEBUG)
                    android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, (("remove: shrink from " + mHashes.length) + " to ") + n);

                final int[] ohashes = mHashes;
                final java.lang.Object[] oarray = mArray;
                allocArrays(n);
                mSize--;
                if (index > 0) {
                    if (android.support.v4.util.SimpleArrayMap.DEBUG)
                        android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, ("remove: copy from 0-" + index) + " to 0");

                    java.lang.System.arraycopy(ohashes, 0, mHashes, 0, index);
                    java.lang.System.arraycopy(oarray, 0, mArray, 0, index << 1);
                }
                if (index < mSize) {
                    if (android.support.v4.util.SimpleArrayMap.DEBUG)
                        android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, (((("remove: copy from " + (index + 1)) + "-") + mSize) + " to ") + index);

                    java.lang.System.arraycopy(ohashes, index + 1, mHashes, index, mSize - index);
                    java.lang.System.arraycopy(oarray, (index + 1) << 1, mArray, index << 1, (mSize - index) << 1);
                }
            } else {
                mSize--;
                if (index < mSize) {
                    if (android.support.v4.util.SimpleArrayMap.DEBUG)
                        android.util.Log.d(android.support.v4.util.SimpleArrayMap.TAG, (((("remove: move " + (index + 1)) + "-") + mSize) + " to ") + index);

                    java.lang.System.arraycopy(mHashes, index + 1, mHashes, index, mSize - index);
                    java.lang.System.arraycopy(mArray, (index + 1) << 1, mArray, index << 1, (mSize - index) << 1);
                }
                mArray[mSize << 1] = null;
                mArray[(mSize << 1) + 1] = null;
            }
        }
        return ((V) (old));
    }

    /**
     * Return the number of items in this array map.
     */
    public int size() {
        return mSize;
    }

    /**
     * {@inheritDoc }
     *
     * <p>This implementation returns false if the object is not a Map or
     * SimpleArrayMap, or if the maps have different sizes. Otherwise, for each
     * key in this map, values of both maps are compared. If the values for any
     * key are not equal, the method returns false, otherwise it returns true.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof android.support.v4.util.SimpleArrayMap) {
            android.support.v4.util.SimpleArrayMap<?, ?> map = ((android.support.v4.util.SimpleArrayMap<?, ?>) (object));
            if (size() != map.size()) {
                return false;
            }
            try {
                for (int i = 0; i < mSize; i++) {
                    K key = keyAt(i);
                    V mine = valueAt(i);
                    java.lang.Object theirs = map.get(key);
                    if (mine == null) {
                        if ((theirs != null) || (!map.containsKey(key))) {
                            return false;
                        }
                    } else
                        if (!mine.equals(theirs)) {
                            return false;
                        }

                }
            } catch (java.lang.NullPointerException ignored) {
                return false;
            } catch (java.lang.ClassCastException ignored) {
                return false;
            }
            return true;
        } else
            if (object instanceof java.util.Map) {
                java.util.Map<?, ?> map = ((java.util.Map<?, ?>) (object));
                if (size() != map.size()) {
                    return false;
                }
                try {
                    for (int i = 0; i < mSize; i++) {
                        K key = keyAt(i);
                        V mine = valueAt(i);
                        java.lang.Object theirs = map.get(key);
                        if (mine == null) {
                            if ((theirs != null) || (!map.containsKey(key))) {
                                return false;
                            }
                        } else
                            if (!mine.equals(theirs)) {
                                return false;
                            }

                    }
                } catch (java.lang.NullPointerException ignored) {
                    return false;
                } catch (java.lang.ClassCastException ignored) {
                    return false;
                }
                return true;
            }

        return false;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int hashCode() {
        final int[] hashes = mHashes;
        final java.lang.Object[] array = mArray;
        int result = 0;
        for (int i = 0, v = 1, s = mSize; i < s; i++ , v += 2) {
            java.lang.Object value = array[v];
            result += hashes[i] ^ (value == null ? 0 : value.hashCode());
        }
        return result;
    }

    /**
     * {@inheritDoc }
     *
     * <p>This implementation composes a string by iterating over its mappings. If
     * this map contains itself as a key or a value, the string "(this Map)"
     * will appear in its place.
     */
    @java.lang.Override
    public java.lang.String toString() {
        if (isEmpty()) {
            return "{}";
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(mSize * 28);
        buffer.append('{');
        for (int i = 0; i < mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            java.lang.Object key = keyAt(i);
            if (key != this) {
                buffer.append(key);
            } else {
                buffer.append("(this Map)");
            }
            buffer.append('=');
            java.lang.Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
}

