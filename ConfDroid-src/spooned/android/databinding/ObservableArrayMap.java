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
package android.databinding;


public class ObservableArrayMap<K, V> extends android.support.v4.util.ArrayMap<K, V> implements android.databinding.ObservableMap<K, V> {
    private transient android.databinding.MapChangeRegistry mListeners;

    @java.lang.Override
    public void addOnMapChangedCallback(android.databinding.ObservableMap.OnMapChangedCallback<? extends android.databinding.ObservableMap<K, V>, K, V> listener) {
        if (mListeners == null) {
            mListeners = new android.databinding.MapChangeRegistry();
        }
        mListeners.add(listener);
    }

    @java.lang.Override
    public void removeOnMapChangedCallback(android.databinding.ObservableMap.OnMapChangedCallback<? extends android.databinding.ObservableMap<K, V>, K, V> listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    @java.lang.Override
    public void clear() {
        boolean wasEmpty = isEmpty();
        if (!wasEmpty) {
            super.clear();
            notifyChange(null);
        }
    }

    public V put(K k, V v) {
        V val = super.put(k, v);
        notifyChange(k);
        return v;
    }

    @java.lang.Override
    public boolean removeAll(java.util.Collection<?> collection) {
        boolean removed = false;
        for (java.lang.Object key : collection) {
            int index = indexOfKey(key);
            if (index >= 0) {
                removed = true;
                removeAt(index);
            }
        }
        return removed;
    }

    @java.lang.Override
    public boolean retainAll(java.util.Collection<?> collection) {
        boolean removed = false;
        for (int i = size() - 1; i >= 0; i--) {
            java.lang.Object key = keyAt(i);
            if (!collection.contains(key)) {
                removeAt(i);
                removed = true;
            }
        }
        return removed;
    }

    @java.lang.Override
    public V removeAt(int index) {
        K key = keyAt(index);
        V value = super.removeAt(index);
        if (value != null) {
            notifyChange(key);
        }
        return value;
    }

    @java.lang.Override
    public V setValueAt(int index, V value) {
        K key = keyAt(index);
        V oldValue = super.setValueAt(index, value);
        notifyChange(key);
        return oldValue;
    }

    private void notifyChange(java.lang.Object key) {
        if (mListeners != null) {
            mListeners.notifyCallbacks(this, 0, key);
        }
    }
}

