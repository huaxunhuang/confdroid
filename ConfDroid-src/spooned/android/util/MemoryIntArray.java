/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * This class is an array of integers that is backed by shared memory.
 * It is useful for efficiently sharing state between processes. The
 * write and read operations are guaranteed to not result in read/
 * write memory tear, i.e. they are atomic. However, multiple read/
 * write operations are <strong>not</strong> synchronized between
 * each other.
 * <p>
 * The data structure is designed to have one owner process that can
 * read/write. There may be multiple client processes that can only read or
 * read/write depending how the data structure was configured when
 * instantiated. The owner process is the process that created the array.
 * The shared memory is pinned (not reclaimed by the system) until the
 * owning process dies or the data structure is closed. This class
 * is <strong>not</strong> thread safe. You should not interact with
 * an instance of this class once it is closed.
 * </p>
 *
 * @unknown 
 */
public final class MemoryIntArray implements android.os.Parcelable , java.io.Closeable {
    private static final java.lang.String TAG = "MemoryIntArray";

    private static final int MAX_SIZE = 1024;

    private final int mOwnerPid;

    private final boolean mClientWritable;

    private final long mMemoryAddr;

    private int mFd;

    /**
     * Creates a new instance.
     *
     * @param size
     * 		The size of the array in terms of integer slots. Cannot be
     * 		more than {@link #getMaxSize()}.
     * @param clientWritable
     * 		Whether other processes can write to the array.
     * @throws IOException
     * 		If an error occurs while accessing the shared memory.
     */
    public MemoryIntArray(int size, boolean clientWritable) throws java.io.IOException {
        if (size > android.util.MemoryIntArray.MAX_SIZE) {
            throw new java.lang.IllegalArgumentException("Max size is " + android.util.MemoryIntArray.MAX_SIZE);
        }
        mOwnerPid = android.os.Process.myPid();
        mClientWritable = clientWritable;
        final java.lang.String name = java.util.UUID.randomUUID().toString();
        mFd = nativeCreate(name, size);
        mMemoryAddr = nativeOpen(mFd, true, clientWritable);
    }

    private MemoryIntArray(android.os.Parcel parcel) throws java.io.IOException {
        mOwnerPid = parcel.readInt();
        mClientWritable = parcel.readInt() == 1;
        android.os.ParcelFileDescriptor pfd = parcel.readParcelable(null);
        if (pfd == null) {
            throw new java.io.IOException("No backing file descriptor");
        }
        mFd = pfd.detachFd();
        final long memoryAddress = parcel.readLong();
        if (isOwner()) {
            mMemoryAddr = memoryAddress;
        } else {
            mMemoryAddr = nativeOpen(mFd, false, mClientWritable);
        }
    }

    /**
     *
     *
     * @return Gets whether this array is mutable.
     */
    public boolean isWritable() {
        enforceNotClosed();
        return isOwner() || mClientWritable;
    }

    /**
     * Gets the value at a given index.
     *
     * @param index
     * 		The index.
     * @return The value at this index.
     * @throws IOException
     * 		If an error occurs while accessing the shared memory.
     */
    public int get(int index) throws java.io.IOException {
        enforceNotClosed();
        enforceValidIndex(index);
        return nativeGet(mFd, mMemoryAddr, index, isOwner());
    }

    /**
     * Sets the value at a given index. This method can be called only if
     * {@link #isWritable()} returns true which means your process is the
     * owner.
     *
     * @param index
     * 		The index.
     * @param value
     * 		The value to set.
     * @throws IOException
     * 		If an error occurs while accessing the shared memory.
     */
    public void set(int index, int value) throws java.io.IOException {
        enforceNotClosed();
        enforceWritable();
        enforceValidIndex(index);
        nativeSet(mFd, mMemoryAddr, index, value, isOwner());
    }

    /**
     * Gets the array size.
     *
     * @throws IOException
     * 		If an error occurs while accessing the shared memory.
     */
    public int size() throws java.io.IOException {
        enforceNotClosed();
        return nativeSize(mFd);
    }

    /**
     * Closes the array releasing resources.
     *
     * @throws IOException
     * 		If an error occurs while accessing the shared memory.
     */
    @java.lang.Override
    public void close() throws java.io.IOException {
        if (!isClosed()) {
            nativeClose(mFd, mMemoryAddr, isOwner());
            mFd = -1;
        }
    }

    /**
     *
     *
     * @return Whether this array is closed and shouldn't be used.
     */
    public boolean isClosed() {
        return mFd == (-1);
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        libcore.io.IoUtils.closeQuietly(this);
        super.finalize();
    }

    @java.lang.Override
    public int describeContents() {
        return android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.adoptFd(mFd);
        try {
            parcel.writeInt(mOwnerPid);
            parcel.writeInt(mClientWritable ? 1 : 0);
            parcel.writeParcelable(pfd, flags & (~android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE));
            parcel.writeLong(mMemoryAddr);
        } finally {
            pfd.detachFd();
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.util.MemoryIntArray other = ((android.util.MemoryIntArray) (obj));
        return mFd == other.mFd;
    }

    @java.lang.Override
    public int hashCode() {
        return mFd;
    }

    private boolean isOwner() {
        return mOwnerPid == android.os.Process.myPid();
    }

    private void enforceNotClosed() {
        if (isClosed()) {
            throw new java.lang.IllegalStateException("cannot interact with a closed instance");
        }
    }

    private void enforceValidIndex(int index) throws java.io.IOException {
        final int size = size();
        if ((index < 0) || (index > (size - 1))) {
            throw new java.lang.IndexOutOfBoundsException((index + " not between 0 and ") + (size - 1));
        }
    }

    private void enforceWritable() {
        if (!isWritable()) {
            throw new java.lang.UnsupportedOperationException("array is not writable");
        }
    }

    private native int nativeCreate(java.lang.String name, int size);

    private native long nativeOpen(int fd, boolean owner, boolean writable);

    private native void nativeClose(int fd, long memoryAddr, boolean owner);

    private native int nativeGet(int fd, long memoryAddr, int index, boolean owner);

    private native void nativeSet(int fd, long memoryAddr, int index, int value, boolean owner);

    private native int nativeSize(int fd);

    /**
     *
     *
     * @return The max array size.
     */
    public static int getMaxSize() {
        return android.util.MemoryIntArray.MAX_SIZE;
    }

    public static final android.os.Parcelable.Creator<android.util.MemoryIntArray> CREATOR = new android.os.Parcelable.Creator<android.util.MemoryIntArray>() {
        @java.lang.Override
        public android.util.MemoryIntArray createFromParcel(android.os.Parcel parcel) {
            try {
                return new android.util.MemoryIntArray(parcel);
            } catch (java.io.IOException ioe) {
                android.util.Log.e(android.util.MemoryIntArray.TAG, "Error unparceling MemoryIntArray");
                return null;
            }
        }

        @java.lang.Override
        public android.util.MemoryIntArray[] newArray(int size) {
            return new android.util.MemoryIntArray[size];
        }
    };
}

