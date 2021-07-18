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
package android.util;


/**
 * SparseArrays map integers to Objects.  Unlike a normal array of Objects,
 * there can be gaps in the indices.  It is intended to be more memory efficient
 * than using a HashMap to map Integers to Objects, both because it avoids
 * auto-boxing keys and its data structure doesn't rely on an extra entry object
 * for each mapping.
 *
 * <p>Note that this container keeps its mappings in an array data structure,
 * using a binary search to find keys.  The implementation is not intended to be appropriate for
 * data structures
 * that may contain large numbers of items.  It is generally slower than a traditional
 * HashMap, since lookups require a binary search and adds and removes require inserting
 * and deleting entries in the array.  For containers holding up to hundreds of items,
 * the performance difference is not significant, less than 50%.</p>
 *
 * <p>To help with performance, the container includes an optimization when removing
 * keys: instead of compacting its array immediately, it leaves the removed entry marked
 * as deleted.  The entry can then be re-used for the same key, or compacted later in
 * a single garbage collection step of all removed entries.  This garbage collection will
 * need to be performed at any time the array needs to be grown or the the map size or
 * entry values are retrieved.</p>
 *
 * <p>It is possible to iterate over the items in this container using
 * {@link #keyAt(int)} and {@link #valueAt(int)}. Iterating over the keys using
 * <code>keyAt(int)</code> with ascending values of the index will return the
 * keys in ascending order, or the values corresponding to the keys in ascending
 * order in the case of <code>valueAt(int)</code>.</p>
 */
public class SparseArray<E> implements java.lang.Cloneable {
    private static final java.lang.Object DELETED = new java.lang.Object();

    private boolean mGarbage = false;

    private int[] mKeys;

    private java.lang.Object[] mValues;

    private int mSize;

    /**
     * Creates a new SparseArray containing no mappings.
     */
    public SparseArray() {
        this(10);
    }

    /**
     * Creates a new SparseArray containing no mappings that will not
     * require any additional memory allocation to store the specified
     * number of mappings.  If you supply an initial capacity of 0, the
     * sparse array will be initialized with a light-weight representation
     * not requiring any additional array allocations.
     */
    public SparseArray(int initialCapacity) {
        if (initialCapacity == 0) {
            mKeys = libcore.util.EmptyArray.INT;
            mValues = libcore.util.EmptyArray.OBJECT;
        } else {
            mValues = com.android.internal.util.ArrayUtils.newUnpaddedObjectArray(initialCapacity);
            mKeys = new int[mValues.length];
        }
        mSize = 0;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public android.util.SparseArray<E> clone() {
        android.util.SparseArray<E> clone = null;
        try {
            clone = ((android.util.SparseArray<E>) (super.clone()));
            clone.mKeys = mKeys.clone();
            clone.mValues = mValues.clone();
        } catch (java.lang.CloneNotSupportedException cnse) {
            /* ignore */
        }
        return clone;
    }

    /**
     * Gets the Object mapped from the specified key, or <code>null</code>
     * if no such mapping has been made.
     */
    public E get(int key) {
        return get(key, null);
    }

    /**
     * Gets the Object mapped from the specified key, or the specified Object
     * if no such mapping has been made.
     */
    @java.lang.SuppressWarnings("unchecked")
    public E get(int key, E valueIfKeyNotFound) {
        int i = android.util.ContainerHelpers.binarySearch(mKeys, mSize, key);
        if ((i < 0) || (mValues[i] == android.util.SparseArray.DELETED)) {
            return valueIfKeyNotFound;
        } else {
            return ((E) (mValues[i]));
        }
    }

    /**
     * Removes the mapping from the specified key, if there was any.
     */
    public void delete(int key) {
        int i = android.util.ContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            if (mValues[i] != android.util.SparseArray.DELETED) {
                mValues[i] = android.util.SparseArray.DELETED;
                mGarbage = true;
            }
        }
    }

    /**
     *
     *
     * @unknown Removes the mapping from the specified key, if there was any, returning the old value.
     */
    public E removeReturnOld(int key) {
        int i = android.util.ContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            if (mValues[i] != android.util.SparseArray.DELETED) {
                final E old = ((E) (mValues[i]));
                mValues[i] = android.util.SparseArray.DELETED;
                mGarbage = true;
                return old;
            }
        }
        return null;
    }

    /**
     * Alias for {@link #delete(int)}.
     */
    public void remove(int key) {
        delete(key);
    }

    /**
     * Removes the mapping at the specified index.
     *
     * <p>For indices outside of the range <code>0...size()-1</code>,
     * the behavior is undefined.</p>
     */
    public void removeAt(int index) {
        if (mValues[index] != android.util.SparseArray.DELETED) {
            mValues[index] = android.util.SparseArray.DELETED;
            mGarbage = true;
        }
    }

    /**
     * Remove a range of mappings as a batch.
     *
     * @param index
     * 		Index to begin at
     * @param size
     * 		Number of mappings to remove
     * 		
     * 		<p>For indices outside of the range <code>0...size()-1</code>,
     * 		the behavior is undefined.</p>
     */
    public void removeAtRange(int index, int size) {
        final int end = java.lang.Math.min(mSize, index + size);
        for (int i = index; i < end; i++) {
            removeAt(i);
        }
    }

    private void gc() {
        // Log.e("SparseArray", "gc start with " + mSize);
        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        java.lang.Object[] values = mValues;
        for (int i = 0; i < n; i++) {
            java.lang.Object val = values[i];
            if (val != android.util.SparseArray.DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        mGarbage = false;
        mSize = o;
        // Log.e("SparseArray", "gc end with " + mSize);
    }

    /**
     * Adds a mapping from the specified key to the specified value,
     * replacing the previous mapping from the specified key if there
     * was one.
     */
    public void put(int key, E value) {
        int i = android.util.ContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;
            if ((i < mSize) && (mValues[i] == android.util.SparseArray.DELETED)) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }
            if (mGarbage && (mSize >= mKeys.length)) {
                gc();
                // Search again because indices may have changed.
                i = ~android.util.ContainerHelpers.binarySearch(mKeys, mSize, key);
            }
            mKeys = com.android.internal.util.GrowingArrayUtils.insert(mKeys, mSize, i, key);
            mValues = com.android.internal.util.GrowingArrayUtils.insert(mValues, mSize, i, value);
            mSize++;
        }
    }

    /**
     * Returns the number of key-value mappings that this SparseArray
     * currently stores.
     */
    public int size() {
        if (mGarbage) {
            gc();
        }
        return mSize;
    }

    /**
     * Given an index in the range <code>0...size()-1</code>, returns
     * the key from the <code>index</code>th key-value mapping that this
     * SparseArray stores.
     *
     * <p>The keys corresponding to indices in ascending order are guaranteed to
     * be in ascending order, e.g., <code>keyAt(0)</code> will return the
     * smallest key and <code>keyAt(size()-1)</code> will return the largest
     * key.</p>
     *
     * <p>For indices outside of the range <code>0...size()-1</code>,
     * the behavior is undefined.</p>
     */
    public int keyAt(int index) {
        if (mGarbage) {
            gc();
        }
        return mKeys[index];
    }

    /**
     * Given an index in the range <code>0...size()-1</code>, returns
     * the value from the <code>index</code>th key-value mapping that this
     * SparseArray stores.
     *
     * <p>The values corresponding to indices in ascending order are guaranteed
     * to be associated with keys in ascending order, e.g.,
     * <code>valueAt(0)</code> will return the value associated with the
     * smallest key and <code>valueAt(size()-1)</code> will return the value
     * associated with the largest key.</p>
     *
     * <p>For indices outside of the range <code>0...size()-1</code>,
     * the behavior is undefined.</p>
     */
    @java.lang.SuppressWarnings("unchecked")
    public E valueAt(int index) {
        if (mGarbage) {
            gc();
        }
        return ((E) (mValues[index]));
    }

    /**
     * Given an index in the range <code>0...size()-1</code>, sets a new
     * value for the <code>index</code>th key-value mapping that this
     * SparseArray stores.
     *
     * <p>For indices outside of the range <code>0...size()-1</code>, the behavior is undefined.</p>
     */
    public void setValueAt(int index, E value) {
        if (mGarbage) {
            gc();
        }
        mValues[index] = value;
    }

    /**
     * Returns the index for which {@link #keyAt} would return the
     * specified key, or a negative number if the specified
     * key is not mapped.
     */
    public int indexOfKey(int key) {
        if (mGarbage) {
            gc();
        }
        return android.util.ContainerHelpers.binarySearch(mKeys, mSize, key);
    }

    /**
     * Returns an index for which {@link #valueAt} would return the
     * specified key, or a negative number if no keys map to the
     * specified value.
     * <p>Beware that this is a linear search, unlike lookups by key,
     * and that multiple keys can map to the same value and this will
     * find only one of them.
     * <p>Note also that unlike most collections' {@code indexOf} methods,
     * this method compares values using {@code ==} rather than {@code equals}.
     */
    public int indexOfValue(E value) {
        if (mGarbage) {
            gc();
        }
        for (int i = 0; i < mSize; i++)
            if (mValues[i] == value)
                return i;


        return -1;
    }

    /**
     * Removes all key-value mappings from this SparseArray.
     */
    public void clear() {
        int n = mSize;
        java.lang.Object[] values = mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        mSize = 0;
        mGarbage = false;
    }

    /**
     * Puts a key/value pair into the array, optimizing for the case where
     * the key is greater than all existing keys in the array.
     */
    public void append(int key, E value) {
        if ((mSize != 0) && (key <= mKeys[mSize - 1])) {
            put(key, value);
            return;
        }
        if (mGarbage && (mSize >= mKeys.length)) {
            gc();
        }
        mKeys = com.android.internal.util.GrowingArrayUtils.append(mKeys, mSize, key);
        mValues = com.android.internal.util.GrowingArrayUtils.append(mValues, mSize, value);
        mSize++;
    }

    /**
     * {@inheritDoc }
     *
     * <p>This implementation composes a string by iterating over its mappings. If
     * this map contains itself as a value, the string "(this Map)"
     * will appear in its place.
     */
    @java.lang.Override
    public java.lang.String toString() {
        if (size() <= 0) {
            return "{}";
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(mSize * 28);
        buffer.append('{');
        for (int i = 0; i < mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            int key = keyAt(i);
            buffer.append(key);
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

