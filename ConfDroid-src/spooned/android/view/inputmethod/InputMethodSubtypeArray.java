/**
 * Copyright (C) 2007-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.view.inputmethod;


/**
 * An array-like container that stores multiple instances of {@link InputMethodSubtype}.
 *
 * <p>This container is designed to reduce the risk of {@link TransactionTooLargeException}
 * when one or more instancess of {@link InputMethodInfo} are transferred through IPC.
 * Basically this class does following three tasks.</p>
 * <ul>
 * <li>Applying compression for the marshalled data</li>
 * <li>Lazily unmarshalling objects</li>
 * <li>Caching the marshalled data when appropriate</li>
 * </ul>
 *
 * @unknown 
 */
public class InputMethodSubtypeArray {
    private static final java.lang.String TAG = "InputMethodSubtypeArray";

    /**
     * Create a new instance of {@link InputMethodSubtypeArray} from an existing list of
     * {@link InputMethodSubtype}.
     *
     * @param subtypes
     * 		A list of {@link InputMethodSubtype} from which
     * 		{@link InputMethodSubtypeArray} will be created.
     */
    @android.annotation.UnsupportedAppUsage
    public InputMethodSubtypeArray(final java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes) {
        if (subtypes == null) {
            mCount = 0;
            return;
        }
        mCount = subtypes.size();
        mInstance = subtypes.toArray(new android.view.inputmethod.InputMethodSubtype[mCount]);
    }

    /**
     * Unmarshall an instance of {@link InputMethodSubtypeArray} from a given {@link Parcel}
     * object.
     *
     * @param source
     * 		A {@link Parcel} object from which {@link InputMethodSubtypeArray} will be
     * 		unmarshalled.
     */
    public InputMethodSubtypeArray(final android.os.Parcel source) {
        mCount = source.readInt();
        if (mCount > 0) {
            mDecompressedSize = source.readInt();
            mCompressedData = source.createByteArray();
        }
    }

    /**
     * Marshall the instance into a given {@link Parcel} object.
     *
     * <p>This methods may take a bit additional time to compress data lazily when called
     * first time.</p>
     *
     * @param source
     * 		A {@link Parcel} object to which {@link InputMethodSubtypeArray} will be
     * 		marshalled.
     */
    public void writeToParcel(final android.os.Parcel dest) {
        if (mCount == 0) {
            dest.writeInt(mCount);
            return;
        }
        byte[] compressedData = mCompressedData;
        int decompressedSize = mDecompressedSize;
        if ((compressedData == null) && (decompressedSize == 0)) {
            synchronized(mLockObject) {
                compressedData = mCompressedData;
                decompressedSize = mDecompressedSize;
                if ((compressedData == null) && (decompressedSize == 0)) {
                    final byte[] decompressedData = android.view.inputmethod.InputMethodSubtypeArray.marshall(mInstance);
                    compressedData = android.view.inputmethod.InputMethodSubtypeArray.compress(decompressedData);
                    if (compressedData == null) {
                        decompressedSize = -1;
                        android.util.Slog.i(android.view.inputmethod.InputMethodSubtypeArray.TAG, "Failed to compress data.");
                    } else {
                        decompressedSize = decompressedData.length;
                    }
                    mDecompressedSize = decompressedSize;
                    mCompressedData = compressedData;
                }
            }
        }
        if ((compressedData != null) && (decompressedSize > 0)) {
            dest.writeInt(mCount);
            dest.writeInt(decompressedSize);
            dest.writeByteArray(compressedData);
        } else {
            android.util.Slog.i(android.view.inputmethod.InputMethodSubtypeArray.TAG, "Unexpected state. Behaving as an empty array.");
            dest.writeInt(0);
        }
    }

    /**
     * Return {@link InputMethodSubtype} specified with the given index.
     *
     * <p>This methods may take a bit additional time to decompress data lazily when called
     * first time.</p>
     *
     * @param index
     * 		The index of {@link InputMethodSubtype}.
     */
    public android.view.inputmethod.InputMethodSubtype get(final int index) {
        if ((index < 0) || (mCount <= index)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        android.view.inputmethod.InputMethodSubtype[] instance = mInstance;
        if (instance == null) {
            synchronized(mLockObject) {
                instance = mInstance;
                if (instance == null) {
                    final byte[] decompressedData = android.view.inputmethod.InputMethodSubtypeArray.decompress(mCompressedData, mDecompressedSize);
                    // Clear the compressed data until {@link #getMarshalled()} is called.
                    mCompressedData = null;
                    mDecompressedSize = 0;
                    if (decompressedData != null) {
                        instance = android.view.inputmethod.InputMethodSubtypeArray.unmarshall(decompressedData);
                    } else {
                        android.util.Slog.e(android.view.inputmethod.InputMethodSubtypeArray.TAG, "Failed to decompress data. Returns null as fallback.");
                        instance = new android.view.inputmethod.InputMethodSubtype[mCount];
                    }
                    mInstance = instance;
                }
            }
        }
        return instance[index];
    }

    /**
     * Return the number of {@link InputMethodSubtype} objects.
     */
    public int getCount() {
        return mCount;
    }

    private final java.lang.Object mLockObject = new java.lang.Object();

    private final int mCount;

    private volatile android.view.inputmethod.InputMethodSubtype[] mInstance;

    private volatile byte[] mCompressedData;

    private volatile int mDecompressedSize;

    private static byte[] marshall(final android.view.inputmethod.InputMethodSubtype[] array) {
        android.os.Parcel parcel = null;
        try {
            parcel = android.os.Parcel.obtain();
            parcel.writeTypedArray(array, 0);
            return parcel.marshall();
        } finally {
            if (parcel != null) {
                parcel.recycle();
                parcel = null;
            }
        }
    }

    private static android.view.inputmethod.InputMethodSubtype[] unmarshall(final byte[] data) {
        android.os.Parcel parcel = null;
        try {
            parcel = android.os.Parcel.obtain();
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            return parcel.createTypedArray(this.CREATOR);
        } finally {
            if (parcel != null) {
                parcel.recycle();
                parcel = null;
            }
        }
    }

    private static byte[] compress(final byte[] data) {
        try (final java.io.ByteArrayOutputStream resultStream = new java.io.ByteArrayOutputStream();final java.util.zip.GZIPOutputStream zipper = new java.util.zip.GZIPOutputStream(resultStream)) {
            zipper.write(data);
            zipper.finish();
            return resultStream.toByteArray();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.view.inputmethod.InputMethodSubtypeArray.TAG, "Failed to compress the data.", e);
            return null;
        }
    }

    private static byte[] decompress(final byte[] data, final int expectedSize) {
        try (final java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(data);final java.util.zip.GZIPInputStream unzipper = new java.util.zip.GZIPInputStream(inputStream)) {
            final byte[] result = new byte[expectedSize];
            int totalReadBytes = 0;
            while (totalReadBytes < result.length) {
                final int restBytes = result.length - totalReadBytes;
                final int readBytes = unzipper.read(result, totalReadBytes, restBytes);
                if (readBytes < 0) {
                    break;
                }
                totalReadBytes += readBytes;
            } 
            if (expectedSize != totalReadBytes) {
                return null;
            }
            return result;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.view.inputmethod.InputMethodSubtypeArray.TAG, "Failed to decompress the data.", e);
            return null;
        }
    }
}

