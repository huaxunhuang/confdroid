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
package android.util;


/**
 * ArrayMap is a generic key->value mapping data structure that is
 * designed to be more memory efficient than a traditional {@link java.util.HashMap}.
 * It keeps its mappings in an array data structure -- an integer array of hash
 * codes for each item, and an Object array of the key/value pairs.  This allows it to
 * avoid having to create an extra object for every entry put in to the map, and it
 * also tries to control the growth of the size of these arrays more aggressively
 * (since growing them only requires copying the entries in the array, not rebuilding
 * a hash map).
 *
 * <p>Note that this implementation is not intended to be appropriate for data structures
 * that may contain large numbers of items.  It is generally slower than a traditional
 * HashMap, since lookups require a binary search and adds and removes require inserting
 * and deleting entries in the array.  For containers holding up to hundreds of items,
 * the performance difference is not significant, less than 50%.</p>
 *
 * <p>Because this container is intended to better balance memory use, unlike most other
 * standard Java containers it will shrink its array as items are removed from it.  Currently
 * you have no control over this shrinking -- if you set a capacity and then remove an
 * item, it may reduce the capacity to better match the current size.  In the future an
 * explicit call to set the capacity should turn off this aggressive shrinking behavior.</p>
 */
public final class ArrayMap<K, V> implements java.util.Map<K, V> {
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
     * Special hash array value that indicates the container is immutable.
     */
    static final int[] EMPTY_IMMUTABLE_INTS = new int[0];

    /**
     *
     *
     * @unknown Special immutable empty ArrayMap.
     */
    public static final android.util.ArrayMap EMPTY = new android.util.ArrayMap<>(-1);

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

    final boolean mIdentityHashCode;

    int[] mHashes;

    java.lang.Object[] mArray;

    int mSize;

    android.util.MapCollections<K, V> mCollections;

    int indexOf(java.lang.Object key, int hash) {
        final int N = mSize;
        // Important fast case: if nothing is in here, nothing to look for.
        if (N == 0) {
            return ~0;
        }
        int index = android.util.ContainerHelpers.binarySearch(mHashes, N, hash);
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
        int index = android.util.ContainerHelpers.binarySearch(mHashes, N, 0);
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
        if (mHashes == android.util.ArrayMap.EMPTY_IMMUTABLE_INTS) {
            throw new java.lang.UnsupportedOperationException("ArrayMap is immutable");
        }
        if (size == (android.util.ArrayMap.BASE_SIZE * 2)) {
            synchronized(android.util.ArrayMap.class) {
                if (android.util.ArrayMap.mTwiceBaseCache != null) {
                    final java.lang.Object[] array = android.util.ArrayMap.mTwiceBaseCache;
                    mArray = array;
                    android.util.ArrayMap.mTwiceBaseCache = ((java.lang.Object[]) (array[0]));
                    mHashes = ((int[]) (array[1]));
                    array[0] = array[1] = null;
                    android.util.ArrayMap.mTwiceBaseCacheSize--;
                    if (android.util.ArrayMap.DEBUG)
                        android.util.Log.d(android.util.ArrayMap.TAG, ((("Retrieving 2x cache " + mHashes) + " now have ") + android.util.ArrayMap.mTwiceBaseCacheSize) + " entries");

                    return;
                }
            }
        } else
            if (size == android.util.ArrayMap.BASE_SIZE) {
                synchronized(android.util.ArrayMap.class) {
                    if (android.util.ArrayMap.mBaseCache != null) {
                        final java.lang.Object[] array = android.util.ArrayMap.mBaseCache;
                        mArray = array;
                        android.util.ArrayMap.mBaseCache = ((java.lang.Object[]) (array[0]));
                        mHashes = ((int[]) (array[1]));
                        array[0] = array[1] = null;
                        android.util.ArrayMap.mBaseCacheSize--;
                        if (android.util.ArrayMap.DEBUG)
                            android.util.Log.d(android.util.ArrayMap.TAG, ((("Retrieving 1x cache " + mHashes) + " now have ") + android.util.ArrayMap.mBaseCacheSize) + " entries");

                        return;
                    }
                }
            }

        mHashes = new int[size];
        mArray = new java.lang.Object[size << 1];
    }

    private static void freeArrays(final int[] hashes, final java.lang.Object[] array, final int size) {
        if (hashes.length == (android.util.ArrayMap.BASE_SIZE * 2)) {
            synchronized(android.util.ArrayMap.class) {
                if (android.util.ArrayMap.mTwiceBaseCacheSize < android.util.ArrayMap.CACHE_SIZE) {
                    array[0] = android.util.ArrayMap.mTwiceBaseCache;
                    array[1] = hashes;
                    for (int i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    android.util.ArrayMap.mTwiceBaseCache = array;
                    android.util.ArrayMap.mTwiceBaseCacheSize++;
                    if (android.util.ArrayMap.DEBUG)
                        android.util.Log.d(android.util.ArrayMap.TAG, ((("Storing 2x cache " + array) + " now have ") + android.util.ArrayMap.mTwiceBaseCacheSize) + " entries");

                }
            }
        } else
            if (hashes.length == android.util.ArrayMap.BASE_SIZE) {
                synchronized(android.util.ArrayMap.class) {
                    if (android.util.ArrayMap.mBaseCacheSize < android.util.ArrayMap.CACHE_SIZE) {
                        array[0] = android.util.ArrayMap.mBaseCache;
                        array[1] = hashes;
                        for (int i = (size << 1) - 1; i >= 2; i--) {
                            array[i] = null;
                        }
                        android.util.ArrayMap.mBaseCache = array;
                        android.util.ArrayMap.mBaseCacheSize++;
                        if (android.util.ArrayMap.DEBUG)
                            android.util.Log.d(android.util.ArrayMap.TAG, ((("Storing 1x cache " + array) + " now have ") + android.util.ArrayMap.mBaseCacheSize) + " entries");

                    }
                }
            }

    }

    /**
     * Create a new empty ArrayMap.  The default capacity of an array map is 0, and
     * will grow once items are added to it.
     */
    public ArrayMap() {
        this(0, false);
    }

    /**
     * Create a new ArrayMap with a given initial capacity.
     */
    public ArrayMap(int capacity) {
        this(capacity, false);
    }

    /**
     * {@hide }
     */
    public ArrayMap(int capacity, boolean identityHashCode) {
        mIdentityHashCode = identityHashCode;
        // If this is immutable, use the sentinal EMPTY_IMMUTABLE_INTS
        // instance instead of the usual EmptyArray.INT. The reference
        // is checked later to see if the array is allowed to grow.
        if (capacity < 0) {
            mHashes = android.util.ArrayMap.EMPTY_IMMUTABLE_INTS;
            mArray = libcore.util.EmptyArray.OBJECT;
        } else
            if (capacity == 0) {
                mHashes = libcore.util.EmptyArray.INT;
                mArray = libcore.util.EmptyArray.OBJECT;
            } else {
                allocArrays(capacity);
            }

        mSize = 0;
    }

    /**
     * Create a new ArrayMap with the mappings from the given ArrayMap.
     */
    public ArrayMap(android.util.ArrayMap<K, V> map) {
        this();
        if (map != null) {
            putAll(map);
        }
    }

    /**
     * Make the array map empty.  All storage is released.
     */
    @java.lang.Override
    public void clear() {
        if (mSize > 0) {
            android.util.ArrayMap.freeArrays(mHashes, mArray, mSize);
            mHashes = libcore.util.EmptyArray.INT;
            mArray = libcore.util.EmptyArray.OBJECT;
            mSize = 0;
        }
    }

    /**
     *
     *
     * @unknown Like {@link #clear}, but doesn't reduce the capacity of the ArrayMap.
     */
    public void erase() {
        if (mSize > 0) {
            final int N = mSize << 1;
            final java.lang.Object[] array = mArray;
            for (int i = 0; i < N; i++) {
                array[i] = null;
            }
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
            android.util.ArrayMap.freeArrays(ohashes, oarray, mSize);
        }
    }

    /**
     * Check whether a key exists in the array.
     *
     * @param key
     * 		The key to search for.
     * @return Returns true if the key exists, else false.
     */
    @java.lang.Override
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
        return key == null ? indexOfNull() : indexOf(key, mIdentityHashCode ? java.lang.System.identityHashCode(key) : key.hashCode());
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
    @java.lang.Override
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
    @java.lang.Override
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
    @java.lang.Override
    public boolean isEmpty() {
        return mSize <= 0;
    }

    /**
     * Add a new value to the array map.
     *
     * @param key
     * 		The key under which to store the value.  If
     * 		this key already exists in the array, its value will be replaced.
     * @param value
     * 		The value to store for the given key.
     * @return Returns the old value that was stored for the given key, or null if there
    was no such key.
     */
    @java.lang.Override
    public V put(K key, V value) {
        final int hash;
        int index;
        if (key == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = (mIdentityHashCode) ? java.lang.System.identityHashCode(key) : key.hashCode();
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
            final int n = (mSize >= (android.util.ArrayMap.BASE_SIZE * 2)) ? mSize + (mSize >> 1) : mSize >= android.util.ArrayMap.BASE_SIZE ? android.util.ArrayMap.BASE_SIZE * 2 : android.util.ArrayMap.BASE_SIZE;
            if (android.util.ArrayMap.DEBUG)
                android.util.Log.d(android.util.ArrayMap.TAG, (("put: grow from " + mHashes.length) + " to ") + n);

            final int[] ohashes = mHashes;
            final java.lang.Object[] oarray = mArray;
            allocArrays(n);
            if (mHashes.length > 0) {
                if (android.util.ArrayMap.DEBUG)
                    android.util.Log.d(android.util.ArrayMap.TAG, ("put: copy 0-" + mSize) + " to 0");

                java.lang.System.arraycopy(ohashes, 0, mHashes, 0, ohashes.length);
                java.lang.System.arraycopy(oarray, 0, mArray, 0, oarray.length);
            }
            android.util.ArrayMap.freeArrays(ohashes, oarray, mSize);
        }
        if (index < mSize) {
            if (android.util.ArrayMap.DEBUG)
                android.util.Log.d(android.util.ArrayMap.TAG, (((("put: move " + index) + "-") + (mSize - index)) + " to ") + (index + 1));

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
     * Special fast path for appending items to the end of the array without validation.
     * The array must already be large enough to contain the item.
     *
     * @unknown 
     */
    public void append(K key, V value) {
        int index = mSize;
        final int hash = (key == null) ? 0 : mIdentityHashCode ? java.lang.System.identityHashCode(key) : key.hashCode();
        if (index >= mHashes.length) {
            throw new java.lang.IllegalStateException("Array is full");
        }
        if ((index > 0) && (mHashes[index - 1] > hash)) {
            java.lang.RuntimeException e = new java.lang.RuntimeException("here");
            e.fillInStackTrace();
            android.util.Log.w(android.util.ArrayMap.TAG, (((((("New hash " + hash) + " is before end of array hash ") + mHashes[index - 1]) + " at index ") + index) + " key ") + key, e);
            put(key, value);
            return;
        }
        mSize = index + 1;
        mHashes[index] = hash;
        index <<= 1;
        mArray[index] = key;
        mArray[index + 1] = value;
    }

    /**
     * The use of the {@link #append} function can result in invalid array maps, in particular
     * an array map where the same key appears multiple times.  This function verifies that
     * the array map is valid, throwing IllegalArgumentException if a problem is found.  The
     * main use for this method is validating an array map after unpacking from an IPC, to
     * protect against malicious callers.
     *
     * @unknown 
     */
    public void validate() {
        final int N = mSize;
        if (N <= 1) {
            // There can't be dups.
            return;
        }
        int basehash = mHashes[0];
        int basei = 0;
        for (int i = 1; i < N; i++) {
            int hash = mHashes[i];
            if (hash != basehash) {
                basehash = hash;
                basei = i;
                continue;
            }
            // We are in a run of entries with the same hash code.  Go backwards through
            // the array to see if any keys are the same.
            final java.lang.Object cur = mArray[i << 1];
            for (int j = i - 1; j >= basei; j--) {
                final java.lang.Object prev = mArray[j << 1];
                if (cur == prev) {
                    throw new java.lang.IllegalArgumentException("Duplicate key in ArrayMap: " + cur);
                }
                if (((cur != null) && (prev != null)) && cur.equals(prev)) {
                    throw new java.lang.IllegalArgumentException("Duplicate key in ArrayMap: " + cur);
                }
            }
        }
    }

    /**
     * Perform a {@link #put(Object, Object)} of all key/value pairs in <var>array</var>
     *
     * @param array
     * 		The array whose contents are to be retrieved.
     */
    public void putAll(android.util.ArrayMap<? extends K, ? extends V> array) {
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
    @java.lang.Override
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
            if (android.util.ArrayMap.DEBUG)
                android.util.Log.d(android.util.ArrayMap.TAG, ("remove: shrink from " + mHashes.length) + " to 0");

            android.util.ArrayMap.freeArrays(mHashes, mArray, mSize);
            mHashes = libcore.util.EmptyArray.INT;
            mArray = libcore.util.EmptyArray.OBJECT;
            mSize = 0;
        } else {
            if ((mHashes.length > (android.util.ArrayMap.BASE_SIZE * 2)) && (mSize < (mHashes.length / 3))) {
                // Shrunk enough to reduce size of arrays.  We don't allow it to
                // shrink smaller than (BASE_SIZE*2) to avoid flapping between
                // that and BASE_SIZE.
                final int n = (mSize > (android.util.ArrayMap.BASE_SIZE * 2)) ? mSize + (mSize >> 1) : android.util.ArrayMap.BASE_SIZE * 2;
                if (android.util.ArrayMap.DEBUG)
                    android.util.Log.d(android.util.ArrayMap.TAG, (("remove: shrink from " + mHashes.length) + " to ") + n);

                final int[] ohashes = mHashes;
                final java.lang.Object[] oarray = mArray;
                allocArrays(n);
                mSize--;
                if (index > 0) {
                    if (android.util.ArrayMap.DEBUG)
                        android.util.Log.d(android.util.ArrayMap.TAG, ("remove: copy from 0-" + index) + " to 0");

                    java.lang.System.arraycopy(ohashes, 0, mHashes, 0, index);
                    java.lang.System.arraycopy(oarray, 0, mArray, 0, index << 1);
                }
                if (index < mSize) {
                    if (android.util.ArrayMap.DEBUG)
                        android.util.Log.d(android.util.ArrayMap.TAG, (((("remove: copy from " + (index + 1)) + "-") + mSize) + " to ") + index);

                    java.lang.System.arraycopy(ohashes, index + 1, mHashes, index, mSize - index);
                    java.lang.System.arraycopy(oarray, (index + 1) << 1, mArray, index << 1, (mSize - index) << 1);
                }
            } else {
                mSize--;
                if (index < mSize) {
                    if (android.util.ArrayMap.DEBUG)
                        android.util.Log.d(android.util.ArrayMap.TAG, (((("remove: move " + (index + 1)) + "-") + mSize) + " to ") + index);

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
    @java.lang.Override
    public int size() {
        return mSize;
    }

    /**
     * {@inheritDoc }
     *
     * <p>This implementation returns false if the object is not a map, or
     * if the maps have different sizes. Otherwise, for each key in this map,
     * values of both maps are compared. If the values for any key are not
     * equal, the method returns false, otherwise it returns true.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
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

    // ------------------------------------------------------------------------
    // Interop with traditional Java containers.  Not as efficient as using
    // specialized collection APIs.
    // ------------------------------------------------------------------------
    private android.util.MapCollections<K, V> getCollection() {
        if (mCollections == null) {
            mCollections = new android.util.MapCollections<K, V>() {
                @java.lang.Override
                protected int colGetSize() {
                    return mSize;
                }

                @java.lang.Override
                protected java.lang.Object colGetEntry(int index, int offset) {
                    return mArray[(index << 1) + offset];
                }

                @java.lang.Override
                protected int colIndexOfKey(java.lang.Object key) {
                    return indexOfKey(key);
                }

                @java.lang.Override
                protected int colIndexOfValue(java.lang.Object value) {
                    return indexOfValue(value);
                }

                @java.lang.Override
                protected java.util.Map<K, V> colGetMap() {
                    return android.util.ArrayMap.this;
                }

                @java.lang.Override
                protected void colPut(K key, V value) {
                    put(key, value);
                }

                @java.lang.Override
                protected V colSetValue(int index, V value) {
                    return setValueAt(index, value);
                }

                @java.lang.Override
                protected void colRemoveAt(int index) {
                    removeAt(index);
                }

                @java.lang.Override
                protected void colClear() {
                    clear();
                }
            };
        }
        return mCollections;
    }

    /**
     * Determine if the array map contains all of the keys in the given collection.
     *
     * @param collection
     * 		The collection whose contents are to be checked against.
     * @return Returns true if this array map contains a key for every entry
    in <var>collection</var>, else returns false.
     */
    public boolean containsAll(java.util.Collection<?> collection) {
        return android.util.MapCollections.containsAllHelper(this, collection);
    }

    /**
     * Perform a {@link #put(Object, Object)} of all key/value pairs in <var>map</var>
     *
     * @param map
     * 		The map whose contents are to be retrieved.
     */
    @java.lang.Override
    public void putAll(java.util.Map<? extends K, ? extends V> map) {
        ensureCapacity(mSize + map.size());
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Remove all keys in the array map that exist in the given collection.
     *
     * @param collection
     * 		The collection whose contents are to be used to remove keys.
     * @return Returns true if any keys were removed from the array map, else false.
     */
    public boolean removeAll(java.util.Collection<?> collection) {
        return android.util.MapCollections.removeAllHelper(this, collection);
    }

    /**
     * Remove all keys in the array map that do <b>not</b> exist in the given collection.
     *
     * @param collection
     * 		The collection whose contents are to be used to determine which
     * 		keys to keep.
     * @return Returns true if any keys were removed from the array map, else false.
     */
    public boolean retainAll(java.util.Collection<?> collection) {
        return android.util.MapCollections.retainAllHelper(this, collection);
    }

    /**
     * Return a {@link java.util.Set} for iterating over and interacting with all mappings
     * in the array map.
     *
     * <p><b>Note:</b> this is a very inefficient way to access the array contents, it
     * requires generating a number of temporary objects and allocates additional state
     * information associated with the container that will remain for the life of the container.</p>
     *
     * <p><b>Note:</b></p> the semantics of this
     * Set are subtly different than that of a {@link java.util.HashMap}: most important,
     * the {@link java.util.Map.Entry Map.Entry} object returned by its iterator is a single
     * object that exists for the entire iterator, so you can <b>not</b> hold on to it
     * after calling {@link java.util.Iterator#next() Iterator.next}.</p>
     */
    @java.lang.Override
    public java.util.Set<java.util.Map.Entry<K, V>> entrySet() {
        return getCollection().getEntrySet();
    }

    /**
     * Return a {@link java.util.Set} for iterating over and interacting with all keys
     * in the array map.
     *
     * <p><b>Note:</b> this is a fairly inefficient way to access the array contents, it
     * requires generating a number of temporary objects and allocates additional state
     * information associated with the container that will remain for the life of the container.</p>
     */
    @java.lang.Override
    public java.util.Set<K> keySet() {
        return getCollection().getKeySet();
    }

    /**
     * Return a {@link java.util.Collection} for iterating over and interacting with all values
     * in the array map.
     *
     * <p><b>Note:</b> this is a fairly inefficient way to access the array contents, it
     * requires generating a number of temporary objects and allocates additional state
     * information associated with the container that will remain for the life of the container.</p>
     */
    @java.lang.Override
    public java.util.Collection<V> values() {
        return getCollection().getValues();
    }
}

