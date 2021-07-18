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
 * Helper for writing standard Java collection interfaces to a data
 * structure like {@link ArrayMap}.
 */
abstract class MapCollections<K, V> {
    android.support.v4.util.MapCollections<K, V>.EntrySet mEntrySet;

    android.support.v4.util.MapCollections<K, V>.KeySet mKeySet;

    android.support.v4.util.MapCollections<K, V>.ValuesCollection mValues;

    final class ArrayIterator<T> implements java.util.Iterator<T> {
        final int mOffset;

        int mSize;

        int mIndex;

        boolean mCanRemove = false;

        ArrayIterator(int offset) {
            mOffset = offset;
            mSize = colGetSize();
        }

        @java.lang.Override
        public boolean hasNext() {
            return mIndex < mSize;
        }

        @java.lang.Override
        public T next() {
            java.lang.Object res = colGetEntry(mIndex, mOffset);
            mIndex++;
            mCanRemove = true;
            return ((T) (res));
        }

        @java.lang.Override
        public void remove() {
            if (!mCanRemove) {
                throw new java.lang.IllegalStateException();
            }
            mIndex--;
            mSize--;
            mCanRemove = false;
            colRemoveAt(mIndex);
        }
    }

    final class MapIterator implements java.util.Iterator<java.util.Map.Entry<K, V>> , java.util.Map.Entry<K, V> {
        int mEnd;

        int mIndex;

        boolean mEntryValid = false;

        MapIterator() {
            mEnd = colGetSize() - 1;
            mIndex = -1;
        }

        @java.lang.Override
        public boolean hasNext() {
            return mIndex < mEnd;
        }

        @java.lang.Override
        public java.util.Map.Entry<K, V> next() {
            mIndex++;
            mEntryValid = true;
            return this;
        }

        @java.lang.Override
        public void remove() {
            if (!mEntryValid) {
                throw new java.lang.IllegalStateException();
            }
            colRemoveAt(mIndex);
            mIndex--;
            mEnd--;
            mEntryValid = false;
        }

        @java.lang.Override
        public K getKey() {
            if (!mEntryValid) {
                throw new java.lang.IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return ((K) (colGetEntry(mIndex, 0)));
        }

        @java.lang.Override
        public V getValue() {
            if (!mEntryValid) {
                throw new java.lang.IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return ((V) (colGetEntry(mIndex, 1)));
        }

        @java.lang.Override
        public V setValue(V object) {
            if (!mEntryValid) {
                throw new java.lang.IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return colSetValue(mIndex, object);
        }

        @java.lang.Override
        public final boolean equals(java.lang.Object o) {
            if (!mEntryValid) {
                throw new java.lang.IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry<?, ?> e = ((java.util.Map.Entry<?, ?>) (o));
            return android.support.v4.util.ContainerHelpers.equal(e.getKey(), colGetEntry(mIndex, 0)) && android.support.v4.util.ContainerHelpers.equal(e.getValue(), colGetEntry(mIndex, 1));
        }

        @java.lang.Override
        public final int hashCode() {
            if (!mEntryValid) {
                throw new java.lang.IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            final java.lang.Object key = colGetEntry(mIndex, 0);
            final java.lang.Object value = colGetEntry(mIndex, 1);
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        @java.lang.Override
        public final java.lang.String toString() {
            return (getKey() + "=") + getValue();
        }
    }

    final class EntrySet implements java.util.Set<java.util.Map.Entry<K, V>> {
        @java.lang.Override
        public boolean add(java.util.Map.Entry<K, V> object) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public boolean addAll(java.util.Collection<? extends java.util.Map.Entry<K, V>> collection) {
            int oldSize = colGetSize();
            for (java.util.Map.Entry<K, V> entry : collection) {
                colPut(entry.getKey(), entry.getValue());
            }
            return oldSize != colGetSize();
        }

        @java.lang.Override
        public void clear() {
            colClear();
        }

        @java.lang.Override
        public boolean contains(java.lang.Object o) {
            if (!(o instanceof java.util.Map.Entry))
                return false;

            java.util.Map.Entry<?, ?> e = ((java.util.Map.Entry<?, ?>) (o));
            int index = colIndexOfKey(e.getKey());
            if (index < 0) {
                return false;
            }
            java.lang.Object foundVal = colGetEntry(index, 1);
            return android.support.v4.util.ContainerHelpers.equal(foundVal, e.getValue());
        }

        @java.lang.Override
        public boolean containsAll(java.util.Collection<?> collection) {
            java.util.Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            } 
            return true;
        }

        @java.lang.Override
        public boolean isEmpty() {
            return colGetSize() == 0;
        }

        @java.lang.Override
        public java.util.Iterator<java.util.Map.Entry<K, V>> iterator() {
            return new MapIterator();
        }

        @java.lang.Override
        public boolean remove(java.lang.Object object) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public boolean removeAll(java.util.Collection<?> collection) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public boolean retainAll(java.util.Collection<?> collection) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public int size() {
            return colGetSize();
        }

        @java.lang.Override
        public java.lang.Object[] toArray() {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public <T> T[] toArray(T[] array) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object object) {
            return android.support.v4.util.MapCollections.equalsSetHelper(this, object);
        }

        @java.lang.Override
        public int hashCode() {
            int result = 0;
            for (int i = colGetSize() - 1; i >= 0; i--) {
                final java.lang.Object key = colGetEntry(i, 0);
                final java.lang.Object value = colGetEntry(i, 1);
                result += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }
            return result;
        }
    }

    final class KeySet implements java.util.Set<K> {
        @java.lang.Override
        public boolean add(K object) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public boolean addAll(java.util.Collection<? extends K> collection) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public void clear() {
            colClear();
        }

        @java.lang.Override
        public boolean contains(java.lang.Object object) {
            return colIndexOfKey(object) >= 0;
        }

        @java.lang.Override
        public boolean containsAll(java.util.Collection<?> collection) {
            return android.support.v4.util.MapCollections.containsAllHelper(colGetMap(), collection);
        }

        @java.lang.Override
        public boolean isEmpty() {
            return colGetSize() == 0;
        }

        @java.lang.Override
        public java.util.Iterator<K> iterator() {
            return new ArrayIterator<K>(0);
        }

        @java.lang.Override
        public boolean remove(java.lang.Object object) {
            int index = colIndexOfKey(object);
            if (index >= 0) {
                colRemoveAt(index);
                return true;
            }
            return false;
        }

        @java.lang.Override
        public boolean removeAll(java.util.Collection<?> collection) {
            return android.support.v4.util.MapCollections.removeAllHelper(colGetMap(), collection);
        }

        @java.lang.Override
        public boolean retainAll(java.util.Collection<?> collection) {
            return android.support.v4.util.MapCollections.retainAllHelper(colGetMap(), collection);
        }

        @java.lang.Override
        public int size() {
            return colGetSize();
        }

        @java.lang.Override
        public java.lang.Object[] toArray() {
            return toArrayHelper(0);
        }

        @java.lang.Override
        public <T> T[] toArray(T[] array) {
            return toArrayHelper(array, 0);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object object) {
            return android.support.v4.util.MapCollections.equalsSetHelper(this, object);
        }

        @java.lang.Override
        public int hashCode() {
            int result = 0;
            for (int i = colGetSize() - 1; i >= 0; i--) {
                java.lang.Object obj = colGetEntry(i, 0);
                result += (obj == null) ? 0 : obj.hashCode();
            }
            return result;
        }
    }

    final class ValuesCollection implements java.util.Collection<V> {
        @java.lang.Override
        public boolean add(V object) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public boolean addAll(java.util.Collection<? extends V> collection) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public void clear() {
            colClear();
        }

        @java.lang.Override
        public boolean contains(java.lang.Object object) {
            return colIndexOfValue(object) >= 0;
        }

        @java.lang.Override
        public boolean containsAll(java.util.Collection<?> collection) {
            java.util.Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            } 
            return true;
        }

        @java.lang.Override
        public boolean isEmpty() {
            return colGetSize() == 0;
        }

        @java.lang.Override
        public java.util.Iterator<V> iterator() {
            return new ArrayIterator<V>(1);
        }

        @java.lang.Override
        public boolean remove(java.lang.Object object) {
            int index = colIndexOfValue(object);
            if (index >= 0) {
                colRemoveAt(index);
                return true;
            }
            return false;
        }

        @java.lang.Override
        public boolean removeAll(java.util.Collection<?> collection) {
            int N = colGetSize();
            boolean changed = false;
            for (int i = 0; i < N; i++) {
                java.lang.Object cur = colGetEntry(i, 1);
                if (collection.contains(cur)) {
                    colRemoveAt(i);
                    i--;
                    N--;
                    changed = true;
                }
            }
            return changed;
        }

        @java.lang.Override
        public boolean retainAll(java.util.Collection<?> collection) {
            int N = colGetSize();
            boolean changed = false;
            for (int i = 0; i < N; i++) {
                java.lang.Object cur = colGetEntry(i, 1);
                if (!collection.contains(cur)) {
                    colRemoveAt(i);
                    i--;
                    N--;
                    changed = true;
                }
            }
            return changed;
        }

        @java.lang.Override
        public int size() {
            return colGetSize();
        }

        @java.lang.Override
        public java.lang.Object[] toArray() {
            return toArrayHelper(1);
        }

        @java.lang.Override
        public <T> T[] toArray(T[] array) {
            return toArrayHelper(array, 1);
        }
    }

    public static <K, V> boolean containsAllHelper(java.util.Map<K, V> map, java.util.Collection<?> collection) {
        java.util.Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!map.containsKey(it.next())) {
                return false;
            }
        } 
        return true;
    }

    public static <K, V> boolean removeAllHelper(java.util.Map<K, V> map, java.util.Collection<?> collection) {
        int oldSize = map.size();
        java.util.Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        } 
        return oldSize != map.size();
    }

    public static <K, V> boolean retainAllHelper(java.util.Map<K, V> map, java.util.Collection<?> collection) {
        int oldSize = map.size();
        java.util.Iterator<K> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        } 
        return oldSize != map.size();
    }

    public java.lang.Object[] toArrayHelper(int offset) {
        final int N = colGetSize();
        java.lang.Object[] result = new java.lang.Object[N];
        for (int i = 0; i < N; i++) {
            result[i] = colGetEntry(i, offset);
        }
        return result;
    }

    public <T> T[] toArrayHelper(T[] array, int offset) {
        final int N = colGetSize();
        if (array.length < N) {
            @java.lang.SuppressWarnings("unchecked")
            T[] newArray = ((T[]) (java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), N)));
            array = newArray;
        }
        for (int i = 0; i < N; i++) {
            array[i] = ((T) (colGetEntry(i, offset)));
        }
        if (array.length > N) {
            array[N] = null;
        }
        return array;
    }

    public static <T> boolean equalsSetHelper(java.util.Set<T> set, java.lang.Object object) {
        if (set == object) {
            return true;
        }
        if (object instanceof java.util.Set) {
            java.util.Set<?> s = ((java.util.Set<?>) (object));
            try {
                return (set.size() == s.size()) && set.containsAll(s);
            } catch (java.lang.NullPointerException ignored) {
                return false;
            } catch (java.lang.ClassCastException ignored) {
                return false;
            }
        }
        return false;
    }

    public java.util.Set<java.util.Map.Entry<K, V>> getEntrySet() {
        if (mEntrySet == null) {
            mEntrySet = new EntrySet();
        }
        return mEntrySet;
    }

    public java.util.Set<K> getKeySet() {
        if (mKeySet == null) {
            mKeySet = new KeySet();
        }
        return mKeySet;
    }

    public java.util.Collection<V> getValues() {
        if (mValues == null) {
            mValues = new ValuesCollection();
        }
        return mValues;
    }

    protected abstract int colGetSize();

    protected abstract java.lang.Object colGetEntry(int index, int offset);

    protected abstract int colIndexOfKey(java.lang.Object key);

    protected abstract int colIndexOfValue(java.lang.Object key);

    protected abstract java.util.Map<K, V> colGetMap();

    protected abstract void colPut(K key, V value);

    protected abstract V colSetValue(int index, V value);

    protected abstract void colRemoveAt(int index);

    protected abstract void colClear();
}

