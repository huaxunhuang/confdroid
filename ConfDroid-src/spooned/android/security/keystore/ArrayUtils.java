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
package android.security.keystore;


/**
 *
 *
 * @unknown 
 */
public abstract class ArrayUtils {
    private ArrayUtils() {
    }

    public static java.lang.String[] nullToEmpty(java.lang.String[] array) {
        return array != null ? array : libcore.util.EmptyArray.STRING;
    }

    public static java.lang.String[] cloneIfNotEmpty(java.lang.String[] array) {
        return (array != null) && (array.length > 0) ? array.clone() : array;
    }

    public static byte[] cloneIfNotEmpty(byte[] array) {
        return (array != null) && (array.length > 0) ? array.clone() : array;
    }

    public static byte[] concat(byte[] arr1, byte[] arr2) {
        return android.security.keystore.ArrayUtils.concat(arr1, 0, arr1 != null ? arr1.length : 0, arr2, 0, arr2 != null ? arr2.length : 0);
    }

    public static byte[] concat(byte[] arr1, int offset1, int len1, byte[] arr2, int offset2, int len2) {
        if (len1 == 0) {
            return android.security.keystore.ArrayUtils.subarray(arr2, offset2, len2);
        } else
            if (len2 == 0) {
                return android.security.keystore.ArrayUtils.subarray(arr1, offset1, len1);
            } else {
                byte[] result = new byte[len1 + len2];
                java.lang.System.arraycopy(arr1, offset1, result, 0, len1);
                java.lang.System.arraycopy(arr2, offset2, result, len1, len2);
                return result;
            }

    }

    public static byte[] subarray(byte[] arr, int offset, int len) {
        if (len == 0) {
            return libcore.util.EmptyArray.BYTE;
        }
        if ((offset == 0) && (len == arr.length)) {
            return arr;
        }
        byte[] result = new byte[len];
        java.lang.System.arraycopy(arr, offset, result, 0, len);
        return result;
    }

    public static int[] concat(int[] arr1, int[] arr2) {
        if ((arr1 == null) || (arr1.length == 0)) {
            return arr2;
        } else
            if ((arr2 == null) || (arr2.length == 0)) {
                return arr1;
            } else {
                int[] result = new int[arr1.length + arr2.length];
                java.lang.System.arraycopy(arr1, 0, result, 0, arr1.length);
                java.lang.System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
                return result;
            }

    }
}

