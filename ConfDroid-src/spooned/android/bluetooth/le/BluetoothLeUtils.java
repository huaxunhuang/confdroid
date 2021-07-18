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
package android.bluetooth.le;


/**
 * Helper class for Bluetooth LE utils.
 *
 * @unknown 
 */
public class BluetoothLeUtils {
    /**
     * Returns a string composed from a {@link SparseArray}.
     */
    static java.lang.String toString(android.util.SparseArray<byte[]> array) {
        if (array == null) {
            return "null";
        }
        if (array.size() == 0) {
            return "{}";
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append('{');
        for (int i = 0; i < array.size(); ++i) {
            buffer.append(array.keyAt(i)).append("=").append(java.util.Arrays.toString(array.valueAt(i)));
        }
        buffer.append('}');
        return buffer.toString();
    }

    /**
     * Returns a string composed from a {@link Map}.
     */
    static <T> java.lang.String toString(java.util.Map<T, byte[]> map) {
        if (map == null) {
            return "null";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append('{');
        java.util.Iterator<java.util.Map.Entry<T, byte[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<T, byte[]> entry = it.next();
            java.lang.Object key = entry.getKey();
            buffer.append(key).append("=").append(java.util.Arrays.toString(map.get(key)));
            if (it.hasNext()) {
                buffer.append(", ");
            }
        } 
        buffer.append('}');
        return buffer.toString();
    }

    /**
     * Check whether two {@link SparseArray} equal.
     */
    static boolean equals(android.util.SparseArray<byte[]> array, android.util.SparseArray<byte[]> otherArray) {
        if (array == otherArray) {
            return true;
        }
        if ((array == null) || (otherArray == null)) {
            return false;
        }
        if (array.size() != otherArray.size()) {
            return false;
        }
        // Keys are guaranteed in ascending order when indices are in ascending order.
        for (int i = 0; i < array.size(); ++i) {
            if ((array.keyAt(i) != otherArray.keyAt(i)) || (!java.util.Arrays.equals(array.valueAt(i), otherArray.valueAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether two {@link Map} equal.
     */
    static <T> boolean equals(java.util.Map<T, byte[]> map, java.util.Map<T, byte[]> otherMap) {
        if (map == otherMap) {
            return true;
        }
        if ((map == null) || (otherMap == null)) {
            return false;
        }
        if (map.size() != otherMap.size()) {
            return false;
        }
        java.util.Set<T> keys = map.keySet();
        if (!keys.equals(otherMap.keySet())) {
            return false;
        }
        for (T key : keys) {
            if (!java.util.Objects.deepEquals(map.get(key), otherMap.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ensure Bluetooth is turned on.
     *
     * @throws IllegalStateException
     * 		If {@code adapter} is null or Bluetooth state is not
     * 		{@link BluetoothAdapter#STATE_ON}.
     */
    static void checkAdapterStateOn(android.bluetooth.BluetoothAdapter adapter) {
        if ((adapter == null) || (!adapter.isLeEnabled())) {
            // adapter.getState() != BluetoothAdapter.STATE_ON) {
            throw new java.lang.IllegalStateException("BT Adapter is not turned ON");
        }
    }
}

