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
package android.os;


/**
 * Container for a message (data and object references) that can
 * be sent through an IBinder.  A Parcel can contain both flattened data
 * that will be unflattened on the other side of the IPC (using the various
 * methods here for writing specific types, or the general
 * {@link Parcelable} interface), and references to live {@link IBinder}
 * objects that will result in the other side receiving a proxy IBinder
 * connected with the original IBinder in the Parcel.
 *
 * <p class="note">Parcel is <strong>not</strong> a general-purpose
 * serialization mechanism.  This class (and the corresponding
 * {@link Parcelable} API for placing arbitrary objects into a Parcel) is
 * designed as a high-performance IPC transport.  As such, it is not
 * appropriate to place any Parcel data in to persistent storage: changes
 * in the underlying implementation of any of the data in the Parcel can
 * render older data unreadable.</p>
 *
 * <p>The bulk of the Parcel API revolves around reading and writing data
 * of various types.  There are six major classes of such functions available.</p>
 *
 * <h3>Primitives</h3>
 *
 * <p>The most basic data functions are for writing and reading primitive
 * data types: {@link #writeByte}, {@link #readByte}, {@link #writeDouble},
 * {@link #readDouble}, {@link #writeFloat}, {@link #readFloat}, {@link #writeInt},
 * {@link #readInt}, {@link #writeLong}, {@link #readLong},
 * {@link #writeString}, {@link #readString}.  Most other
 * data operations are built on top of these.  The given data is written and
 * read using the endianess of the host CPU.</p>
 *
 * <h3>Primitive Arrays</h3>
 *
 * <p>There are a variety of methods for reading and writing raw arrays
 * of primitive objects, which generally result in writing a 4-byte length
 * followed by the primitive data items.  The methods for reading can either
 * read the data into an existing array, or create and return a new array.
 * These available types are:</p>
 *
 * <ul>
 * <li> {@link #writeBooleanArray(boolean[])},
 * {@link #readBooleanArray(boolean[])}, {@link #createBooleanArray()}
 * <li> {@link #writeByteArray(byte[])},
 * {@link #writeByteArray(byte[], int, int)}, {@link #readByteArray(byte[])},
 * {@link #createByteArray()}
 * <li> {@link #writeCharArray(char[])}, {@link #readCharArray(char[])},
 * {@link #createCharArray()}
 * <li> {@link #writeDoubleArray(double[])}, {@link #readDoubleArray(double[])},
 * {@link #createDoubleArray()}
 * <li> {@link #writeFloatArray(float[])}, {@link #readFloatArray(float[])},
 * {@link #createFloatArray()}
 * <li> {@link #writeIntArray(int[])}, {@link #readIntArray(int[])},
 * {@link #createIntArray()}
 * <li> {@link #writeLongArray(long[])}, {@link #readLongArray(long[])},
 * {@link #createLongArray()}
 * <li> {@link #writeStringArray(String[])}, {@link #readStringArray(String[])},
 * {@link #createStringArray()}.
 * <li> {@link #writeSparseBooleanArray(SparseBooleanArray)},
 * {@link #readSparseBooleanArray()}.
 * </ul>
 *
 * <h3>Parcelables</h3>
 *
 * <p>The {@link Parcelable} protocol provides an extremely efficient (but
 * low-level) protocol for objects to write and read themselves from Parcels.
 * You can use the direct methods {@link #writeParcelable(Parcelable, int)}
 * and {@link #readParcelable(ClassLoader)} or
 * {@link #writeParcelableArray} and
 * {@link #readParcelableArray(ClassLoader)} to write or read.  These
 * methods write both the class type and its data to the Parcel, allowing
 * that class to be reconstructed from the appropriate class loader when
 * later reading.</p>
 *
 * <p>There are also some methods that provide a more efficient way to work
 * with Parcelables: {@link #writeTypedObject}, {@link #writeTypedArray},
 * {@link #writeTypedList}, {@link #readTypedObject},
 * {@link #createTypedArray} and {@link #createTypedArrayList}.  These methods
 * do not write the class information of the original object: instead, the
 * caller of the read function must know what type to expect and pass in the
 * appropriate {@link Parcelable.Creator Parcelable.Creator} instead to
 * properly construct the new object and read its data.  (To more efficient
 * write and read a single Parceable object that is not null, you can directly
 * call {@link Parcelable#writeToParcel Parcelable.writeToParcel} and
 * {@link Parcelable.Creator#createFromParcel Parcelable.Creator.createFromParcel}
 * yourself.)</p>
 *
 * <h3>Bundles</h3>
 *
 * <p>A special type-safe container, called {@link Bundle}, is available
 * for key/value maps of heterogeneous values.  This has many optimizations
 * for improved performance when reading and writing data, and its type-safe
 * API avoids difficult to debug type errors when finally marshalling the
 * data contents into a Parcel.  The methods to use are
 * {@link #writeBundle(Bundle)}, {@link #readBundle()}, and
 * {@link #readBundle(ClassLoader)}.
 *
 * <h3>Active Objects</h3>
 *
 * <p>An unusual feature of Parcel is the ability to read and write active
 * objects.  For these objects the actual contents of the object is not
 * written, rather a special token referencing the object is written.  When
 * reading the object back from the Parcel, you do not get a new instance of
 * the object, but rather a handle that operates on the exact same object that
 * was originally written.  There are two forms of active objects available.</p>
 *
 * <p>{@link Binder} objects are a core facility of Android's general cross-process
 * communication system.  The {@link IBinder} interface describes an abstract
 * protocol with a Binder object.  Any such interface can be written in to
 * a Parcel, and upon reading you will receive either the original object
 * implementing that interface or a special proxy implementation
 * that communicates calls back to the original object.  The methods to use are
 * {@link #writeStrongBinder(IBinder)},
 * {@link #writeStrongInterface(IInterface)}, {@link #readStrongBinder()},
 * {@link #writeBinderArray(IBinder[])}, {@link #readBinderArray(IBinder[])},
 * {@link #createBinderArray()},
 * {@link #writeBinderList(List)}, {@link #readBinderList(List)},
 * {@link #createBinderArrayList()}.</p>
 *
 * <p>FileDescriptor objects, representing raw Linux file descriptor identifiers,
 * can be written and {@link ParcelFileDescriptor} objects returned to operate
 * on the original file descriptor.  The returned file descriptor is a dup
 * of the original file descriptor: the object and fd is different, but
 * operating on the same underlying file stream, with the same position, etc.
 * The methods to use are {@link #writeFileDescriptor(FileDescriptor)},
 * {@link #readFileDescriptor()}.
 *
 * <h3>Untyped Containers</h3>
 *
 * <p>A final class of methods are for writing and reading standard Java
 * containers of arbitrary types.  These all revolve around the
 * {@link #writeValue(Object)} and {@link #readValue(ClassLoader)} methods
 * which define the types of objects allowed.  The container methods are
 * {@link #writeArray(Object[])}, {@link #readArray(ClassLoader)},
 * {@link #writeList(List)}, {@link #readList(List, ClassLoader)},
 * {@link #readArrayList(ClassLoader)},
 * {@link #writeMap(Map)}, {@link #readMap(Map, ClassLoader)},
 * {@link #writeSparseArray(SparseArray)},
 * {@link #readSparseArray(ClassLoader)}.
 */
public final class Parcel {
    private static final boolean DEBUG_RECYCLE = false;

    private static final boolean DEBUG_ARRAY_MAP = false;

    private static final java.lang.String TAG = "Parcel";

    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    private long mNativePtr;// used by native code


    /**
     * Flag indicating if {@link #mNativePtr} was allocated by this object,
     * indicating that we're responsible for its lifecycle.
     */
    private boolean mOwnsNativeParcelObject;

    private long mNativeSize;

    private java.lang.RuntimeException mStack;

    private static final int POOL_SIZE = 6;

    private static final android.os.Parcel[] sOwnedPool = new android.os.Parcel[android.os.Parcel.POOL_SIZE];

    private static final android.os.Parcel[] sHolderPool = new android.os.Parcel[android.os.Parcel.POOL_SIZE];

    // Keep in sync with frameworks/native/libs/binder/PersistableBundle.cpp.
    private static final int VAL_NULL = -1;

    private static final int VAL_STRING = 0;

    private static final int VAL_INTEGER = 1;

    private static final int VAL_MAP = 2;

    private static final int VAL_BUNDLE = 3;

    private static final int VAL_PARCELABLE = 4;

    private static final int VAL_SHORT = 5;

    private static final int VAL_LONG = 6;

    private static final int VAL_FLOAT = 7;

    private static final int VAL_DOUBLE = 8;

    private static final int VAL_BOOLEAN = 9;

    private static final int VAL_CHARSEQUENCE = 10;

    private static final int VAL_LIST = 11;

    private static final int VAL_SPARSEARRAY = 12;

    private static final int VAL_BYTEARRAY = 13;

    private static final int VAL_STRINGARRAY = 14;

    private static final int VAL_IBINDER = 15;

    private static final int VAL_PARCELABLEARRAY = 16;

    private static final int VAL_OBJECTARRAY = 17;

    private static final int VAL_INTARRAY = 18;

    private static final int VAL_LONGARRAY = 19;

    private static final int VAL_BYTE = 20;

    private static final int VAL_SERIALIZABLE = 21;

    private static final int VAL_SPARSEBOOLEANARRAY = 22;

    private static final int VAL_BOOLEANARRAY = 23;

    private static final int VAL_CHARSEQUENCEARRAY = 24;

    private static final int VAL_PERSISTABLEBUNDLE = 25;

    private static final int VAL_SIZE = 26;

    private static final int VAL_SIZEF = 27;

    private static final int VAL_DOUBLEARRAY = 28;

    // The initial int32 in a Binder call's reply Parcel header:
    // Keep these in sync with libbinder's binder/Status.h.
    private static final int EX_SECURITY = -1;

    private static final int EX_BAD_PARCELABLE = -2;

    private static final int EX_ILLEGAL_ARGUMENT = -3;

    private static final int EX_NULL_POINTER = -4;

    private static final int EX_ILLEGAL_STATE = -5;

    private static final int EX_NETWORK_MAIN_THREAD = -6;

    private static final int EX_UNSUPPORTED_OPERATION = -7;

    private static final int EX_SERVICE_SPECIFIC = -8;

    private static final int EX_HAS_REPLY_HEADER = -128;// special; see below


    // EX_TRANSACTION_FAILED is used exclusively in native code.
    // see libbinder's binder/Status.h
    private static final int EX_TRANSACTION_FAILED = -129;

    private static native int nativeDataSize(long nativePtr);

    private static native int nativeDataAvail(long nativePtr);

    private static native int nativeDataPosition(long nativePtr);

    private static native int nativeDataCapacity(long nativePtr);

    private static native long nativeSetDataSize(long nativePtr, int size);

    private static native void nativeSetDataPosition(long nativePtr, int pos);

    private static native void nativeSetDataCapacity(long nativePtr, int size);

    private static native boolean nativePushAllowFds(long nativePtr, boolean allowFds);

    private static native void nativeRestoreAllowFds(long nativePtr, boolean lastValue);

    private static native void nativeWriteByteArray(long nativePtr, byte[] b, int offset, int len);

    private static native void nativeWriteBlob(long nativePtr, byte[] b, int offset, int len);

    private static native void nativeWriteInt(long nativePtr, int val);

    private static native void nativeWriteLong(long nativePtr, long val);

    private static native void nativeWriteFloat(long nativePtr, float val);

    private static native void nativeWriteDouble(long nativePtr, double val);

    private static native void nativeWriteString(long nativePtr, java.lang.String val);

    private static native void nativeWriteStrongBinder(long nativePtr, android.os.IBinder val);

    private static native long nativeWriteFileDescriptor(long nativePtr, java.io.FileDescriptor val);

    private static native byte[] nativeCreateByteArray(long nativePtr);

    private static native byte[] nativeReadBlob(long nativePtr);

    private static native int nativeReadInt(long nativePtr);

    private static native long nativeReadLong(long nativePtr);

    private static native float nativeReadFloat(long nativePtr);

    private static native double nativeReadDouble(long nativePtr);

    private static native java.lang.String nativeReadString(long nativePtr);

    private static native android.os.IBinder nativeReadStrongBinder(long nativePtr);

    private static native java.io.FileDescriptor nativeReadFileDescriptor(long nativePtr);

    private static native long nativeCreate();

    private static native long nativeFreeBuffer(long nativePtr);

    private static native void nativeDestroy(long nativePtr);

    private static native byte[] nativeMarshall(long nativePtr);

    private static native long nativeUnmarshall(long nativePtr, byte[] data, int offset, int length);

    private static native long nativeAppendFrom(long thisNativePtr, long otherNativePtr, int offset, int length);

    private static native boolean nativeHasFileDescriptors(long nativePtr);

    private static native void nativeWriteInterfaceToken(long nativePtr, java.lang.String interfaceName);

    private static native void nativeEnforceInterface(long nativePtr, java.lang.String interfaceName);

    private static native long nativeGetBlobAshmemSize(long nativePtr);

    public static final android.os.Parcelable.Creator<java.lang.String> STRING_CREATOR = new android.os.Parcelable.Creator<java.lang.String>() {
        public java.lang.String createFromParcel(android.os.Parcel source) {
            return source.readString();
        }

        public java.lang.String[] newArray(int size) {
            return new java.lang.String[size];
        }
    };

    /**
     * Retrieve a new Parcel object from the pool.
     */
    public static android.os.Parcel obtain() {
        final android.os.Parcel[] pool = android.os.Parcel.sOwnedPool;
        synchronized(pool) {
            android.os.Parcel p;
            for (int i = 0; i < android.os.Parcel.POOL_SIZE; i++) {
                p = pool[i];
                if (p != null) {
                    pool[i] = null;
                    if (android.os.Parcel.DEBUG_RECYCLE) {
                        p.mStack = new java.lang.RuntimeException();
                    }
                    return p;
                }
            }
        }
        return new android.os.Parcel(0);
    }

    /**
     * Put a Parcel object back into the pool.  You must not touch
     * the object after this call.
     */
    public final void recycle() {
        if (android.os.Parcel.DEBUG_RECYCLE)
            mStack = null;

        freeBuffer();
        final android.os.Parcel[] pool;
        if (mOwnsNativeParcelObject) {
            pool = android.os.Parcel.sOwnedPool;
        } else {
            mNativePtr = 0;
            pool = android.os.Parcel.sHolderPool;
        }
        synchronized(pool) {
            for (int i = 0; i < android.os.Parcel.POOL_SIZE; i++) {
                if (pool[i] == null) {
                    pool[i] = this;
                    return;
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static native long getGlobalAllocSize();

    /**
     *
     *
     * @unknown 
     */
    public static native long getGlobalAllocCount();

    /**
     * Returns the total amount of data contained in the parcel.
     */
    public final int dataSize() {
        return android.os.Parcel.nativeDataSize(mNativePtr);
    }

    /**
     * Returns the amount of data remaining to be read from the
     * parcel.  That is, {@link #dataSize}-{@link #dataPosition}.
     */
    public final int dataAvail() {
        return android.os.Parcel.nativeDataAvail(mNativePtr);
    }

    /**
     * Returns the current position in the parcel data.  Never
     * more than {@link #dataSize}.
     */
    public final int dataPosition() {
        return android.os.Parcel.nativeDataPosition(mNativePtr);
    }

    /**
     * Returns the total amount of space in the parcel.  This is always
     * >= {@link #dataSize}.  The difference between it and dataSize() is the
     * amount of room left until the parcel needs to re-allocate its
     * data buffer.
     */
    public final int dataCapacity() {
        return android.os.Parcel.nativeDataCapacity(mNativePtr);
    }

    /**
     * Change the amount of data in the parcel.  Can be either smaller or
     * larger than the current size.  If larger than the current capacity,
     * more memory will be allocated.
     *
     * @param size
     * 		The new number of bytes in the Parcel.
     */
    public final void setDataSize(int size) {
        updateNativeSize(android.os.Parcel.nativeSetDataSize(mNativePtr, size));
    }

    /**
     * Move the current read/write position in the parcel.
     *
     * @param pos
     * 		New offset in the parcel; must be between 0 and
     * 		{@link #dataSize}.
     */
    public final void setDataPosition(int pos) {
        android.os.Parcel.nativeSetDataPosition(mNativePtr, pos);
    }

    /**
     * Change the capacity (current available space) of the parcel.
     *
     * @param size
     * 		The new capacity of the parcel, in bytes.  Can not be
     * 		less than {@link #dataSize} -- that is, you can not drop existing data
     * 		with this method.
     */
    public final void setDataCapacity(int size) {
        android.os.Parcel.nativeSetDataCapacity(mNativePtr, size);
    }

    /**
     *
     *
     * @unknown 
     */
    public final boolean pushAllowFds(boolean allowFds) {
        return android.os.Parcel.nativePushAllowFds(mNativePtr, allowFds);
    }

    /**
     *
     *
     * @unknown 
     */
    public final void restoreAllowFds(boolean lastValue) {
        android.os.Parcel.nativeRestoreAllowFds(mNativePtr, lastValue);
    }

    /**
     * Returns the raw bytes of the parcel.
     *
     * <p class="note">The data you retrieve here <strong>must not</strong>
     * be placed in any kind of persistent storage (on local disk, across
     * a network, etc).  For that, you should use standard serialization
     * or another kind of general serialization mechanism.  The Parcel
     * marshalled representation is highly optimized for local IPC, and as
     * such does not attempt to maintain compatibility with data created
     * in different versions of the platform.
     */
    public final byte[] marshall() {
        return android.os.Parcel.nativeMarshall(mNativePtr);
    }

    /**
     * Set the bytes in data to be the raw bytes of this Parcel.
     */
    public final void unmarshall(byte[] data, int offset, int length) {
        updateNativeSize(android.os.Parcel.nativeUnmarshall(mNativePtr, data, offset, length));
    }

    public final void appendFrom(android.os.Parcel parcel, int offset, int length) {
        updateNativeSize(android.os.Parcel.nativeAppendFrom(mNativePtr, parcel.mNativePtr, offset, length));
    }

    /**
     * Report whether the parcel contains any marshalled file descriptors.
     */
    public final boolean hasFileDescriptors() {
        return android.os.Parcel.nativeHasFileDescriptors(mNativePtr);
    }

    /**
     * Store or read an IBinder interface token in the parcel at the current
     * {@link #dataPosition}.  This is used to validate that the marshalled
     * transaction is intended for the target interface.
     */
    public final void writeInterfaceToken(java.lang.String interfaceName) {
        android.os.Parcel.nativeWriteInterfaceToken(mNativePtr, interfaceName);
    }

    public final void enforceInterface(java.lang.String interfaceName) {
        android.os.Parcel.nativeEnforceInterface(mNativePtr, interfaceName);
    }

    /**
     * Write a byte array into the parcel at the current {@link #dataPosition},
     * growing {@link #dataCapacity} if needed.
     *
     * @param b
     * 		Bytes to place into the parcel.
     */
    public final void writeByteArray(byte[] b) {
        writeByteArray(b, 0, b != null ? b.length : 0);
    }

    /**
     * Write a byte array into the parcel at the current {@link #dataPosition},
     * growing {@link #dataCapacity} if needed.
     *
     * @param b
     * 		Bytes to place into the parcel.
     * @param offset
     * 		Index of first byte to be written.
     * @param len
     * 		Number of bytes to write.
     */
    public final void writeByteArray(byte[] b, int offset, int len) {
        if (b == null) {
            writeInt(-1);
            return;
        }
        java.util.Arrays.checkOffsetAndCount(b.length, offset, len);
        android.os.Parcel.nativeWriteByteArray(mNativePtr, b, offset, len);
    }

    /**
     * Write a blob of data into the parcel at the current {@link #dataPosition},
     * growing {@link #dataCapacity} if needed.
     *
     * @param b
     * 		Bytes to place into the parcel.
     * 		{@hide }
     * 		{@SystemApi }
     */
    public final void writeBlob(byte[] b) {
        writeBlob(b, 0, b != null ? b.length : 0);
    }

    /**
     * Write a blob of data into the parcel at the current {@link #dataPosition},
     * growing {@link #dataCapacity} if needed.
     *
     * @param b
     * 		Bytes to place into the parcel.
     * @param offset
     * 		Index of first byte to be written.
     * @param len
     * 		Number of bytes to write.
     * 		{@hide }
     * 		{@SystemApi }
     */
    public final void writeBlob(byte[] b, int offset, int len) {
        if (b == null) {
            writeInt(-1);
            return;
        }
        java.util.Arrays.checkOffsetAndCount(b.length, offset, len);
        android.os.Parcel.nativeWriteBlob(mNativePtr, b, offset, len);
    }

    /**
     * Write an integer value into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeInt(int val) {
        android.os.Parcel.nativeWriteInt(mNativePtr, val);
    }

    /**
     * Write a long integer value into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeLong(long val) {
        android.os.Parcel.nativeWriteLong(mNativePtr, val);
    }

    /**
     * Write a floating point value into the parcel at the current
     * dataPosition(), growing dataCapacity() if needed.
     */
    public final void writeFloat(float val) {
        android.os.Parcel.nativeWriteFloat(mNativePtr, val);
    }

    /**
     * Write a double precision floating point value into the parcel at the
     * current dataPosition(), growing dataCapacity() if needed.
     */
    public final void writeDouble(double val) {
        android.os.Parcel.nativeWriteDouble(mNativePtr, val);
    }

    /**
     * Write a string value into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeString(java.lang.String val) {
        android.os.Parcel.nativeWriteString(mNativePtr, val);
    }

    /**
     * Write a CharSequence value into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     *
     * @unknown 
     */
    public final void writeCharSequence(java.lang.CharSequence val) {
        android.text.TextUtils.writeToParcel(val, this, 0);
    }

    /**
     * Write an object into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeStrongBinder(android.os.IBinder val) {
        android.os.Parcel.nativeWriteStrongBinder(mNativePtr, val);
    }

    /**
     * Write an object into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeStrongInterface(android.os.IInterface val) {
        writeStrongBinder(val == null ? null : val.asBinder());
    }

    /**
     * Write a FileDescriptor into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     *
     * <p class="caution">The file descriptor will not be closed, which may
     * result in file descriptor leaks when objects are returned from Binder
     * calls.  Use {@link ParcelFileDescriptor#writeToParcel} instead, which
     * accepts contextual flags and will close the original file descriptor
     * if {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE} is set.</p>
     */
    public final void writeFileDescriptor(java.io.FileDescriptor val) {
        updateNativeSize(android.os.Parcel.nativeWriteFileDescriptor(mNativePtr, val));
    }

    private void updateNativeSize(long newNativeSize) {
        if (mOwnsNativeParcelObject) {
            if (newNativeSize > java.lang.Integer.MAX_VALUE) {
                newNativeSize = java.lang.Integer.MAX_VALUE;
            }
            if (newNativeSize != mNativeSize) {
                int delta = ((int) (newNativeSize - mNativeSize));
                if (delta > 0) {
                    dalvik.system.VMRuntime.getRuntime().registerNativeAllocation(delta);
                } else {
                    dalvik.system.VMRuntime.getRuntime().registerNativeFree(-delta);
                }
                mNativeSize = newNativeSize;
            }
        }
    }

    /**
     * {@hide }
     * This will be the new name for writeFileDescriptor, for consistency.
     */
    public final void writeRawFileDescriptor(java.io.FileDescriptor val) {
        android.os.Parcel.nativeWriteFileDescriptor(mNativePtr, val);
    }

    /**
     * {@hide }
     * Write an array of FileDescriptor objects into the Parcel.
     *
     * @param value
     * 		The array of objects to be written.
     */
    public final void writeRawFileDescriptorArray(java.io.FileDescriptor[] value) {
        if (value != null) {
            int N = value.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeRawFileDescriptor(value[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    /**
     * Write a byte value into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeByte(byte val) {
        writeInt(val);
    }

    /**
     * Please use {@link #writeBundle} instead.  Flattens a Map into the parcel
     * at the current dataPosition(),
     * growing dataCapacity() if needed.  The Map keys must be String objects.
     * The Map values are written using {@link #writeValue} and must follow
     * the specification there.
     *
     * <p>It is strongly recommended to use {@link #writeBundle} instead of
     * this method, since the Bundle class provides a type-safe API that
     * allows you to avoid mysterious type errors at the point of marshalling.
     */
    public final void writeMap(java.util.Map val) {
        writeMapInternal(((java.util.Map<java.lang.String, java.lang.Object>) (val)));
    }

    /**
     * Flatten a Map into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.  The Map keys must be String objects.
     */
    /* package */
    void writeMapInternal(java.util.Map<java.lang.String, java.lang.Object> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        java.util.Set<java.util.Map.Entry<java.lang.String, java.lang.Object>> entries = val.entrySet();
        writeInt(entries.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> e : entries) {
            writeValue(e.getKey());
            writeValue(e.getValue());
        }
    }

    /**
     * Flatten an ArrayMap into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.  The Map keys must be String objects.
     */
    /* package */
    void writeArrayMapInternal(android.util.ArrayMap<java.lang.String, java.lang.Object> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        // Keep the format of this Parcel in sync with writeToParcelInner() in
        // frameworks/native/libs/binder/PersistableBundle.cpp.
        final int N = val.size();
        writeInt(N);
        if (android.os.Parcel.DEBUG_ARRAY_MAP) {
            java.lang.RuntimeException here = new java.lang.RuntimeException("here");
            here.fillInStackTrace();
            android.util.Log.d(android.os.Parcel.TAG, ("Writing " + N) + " ArrayMap entries", here);
        }
        int startPos;
        for (int i = 0; i < N; i++) {
            if (android.os.Parcel.DEBUG_ARRAY_MAP)
                startPos = dataPosition();

            writeString(val.keyAt(i));
            writeValue(val.valueAt(i));
            if (android.os.Parcel.DEBUG_ARRAY_MAP)
                android.util.Log.d(android.os.Parcel.TAG, (((((("  Write #" + i) + " ") + (dataPosition() - startPos)) + " bytes: key=0x") + java.lang.Integer.toHexString(val.keyAt(i) != null ? val.keyAt(i).hashCode() : 0)) + " ") + val.keyAt(i));

        }
    }

    /**
     *
     *
     * @unknown For testing only.
     */
    public void writeArrayMap(android.util.ArrayMap<java.lang.String, java.lang.Object> val) {
        writeArrayMapInternal(val);
    }

    /**
     * Write an array set to the parcel.
     *
     * @param val
     * 		The array set to write.
     * @unknown 
     */
    public void writeArraySet(@android.annotation.Nullable
    android.util.ArraySet<? extends java.lang.Object> val) {
        final int size = (val != null) ? val.size() : -1;
        writeInt(size);
        for (int i = 0; i < size; i++) {
            writeValue(val.valueAt(i));
        }
    }

    /**
     * Flatten a Bundle into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeBundle(android.os.Bundle val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        val.writeToParcel(this, 0);
    }

    /**
     * Flatten a PersistableBundle into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writePersistableBundle(android.os.PersistableBundle val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        val.writeToParcel(this, 0);
    }

    /**
     * Flatten a Size into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeSize(android.util.Size val) {
        writeInt(val.getWidth());
        writeInt(val.getHeight());
    }

    /**
     * Flatten a SizeF into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.
     */
    public final void writeSizeF(android.util.SizeF val) {
        writeFloat(val.getWidth());
        writeFloat(val.getHeight());
    }

    /**
     * Flatten a List into the parcel at the current dataPosition(), growing
     * dataCapacity() if needed.  The List values are written using
     * {@link #writeValue} and must follow the specification there.
     */
    public final void writeList(java.util.List val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        int i = 0;
        writeInt(N);
        while (i < N) {
            writeValue(val.get(i));
            i++;
        } 
    }

    /**
     * Flatten an Object array into the parcel at the current dataPosition(),
     * growing dataCapacity() if needed.  The array values are written using
     * {@link #writeValue} and must follow the specification there.
     */
    public final void writeArray(java.lang.Object[] val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.length;
        int i = 0;
        writeInt(N);
        while (i < N) {
            writeValue(val[i]);
            i++;
        } 
    }

    /**
     * Flatten a generic SparseArray into the parcel at the current
     * dataPosition(), growing dataCapacity() if needed.  The SparseArray
     * values are written using {@link #writeValue} and must follow the
     * specification there.
     */
    public final void writeSparseArray(android.util.SparseArray<java.lang.Object> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        int i = 0;
        while (i < N) {
            writeInt(val.keyAt(i));
            writeValue(val.valueAt(i));
            i++;
        } 
    }

    public final void writeSparseBooleanArray(android.util.SparseBooleanArray val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        int i = 0;
        while (i < N) {
            writeInt(val.keyAt(i));
            writeByte(((byte) (val.valueAt(i) ? 1 : 0)));
            i++;
        } 
    }

    public final void writeBooleanArray(boolean[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeInt(val[i] ? 1 : 0);
            }
        } else {
            writeInt(-1);
        }
    }

    public final boolean[] createBooleanArray() {
        int N = readInt();
        // >>2 as a fast divide-by-4 works in the create*Array() functions
        // because dataAvail() will never return a negative number.  4 is
        // the size of a stored boolean in the stream.
        if ((N >= 0) && (N <= (dataAvail() >> 2))) {
            boolean[] val = new boolean[N];
            for (int i = 0; i < N; i++) {
                val[i] = readInt() != 0;
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readBooleanArray(boolean[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readInt() != 0;
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeCharArray(char[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeInt(((int) (val[i])));
            }
        } else {
            writeInt(-1);
        }
    }

    public final char[] createCharArray() {
        int N = readInt();
        if ((N >= 0) && (N <= (dataAvail() >> 2))) {
            char[] val = new char[N];
            for (int i = 0; i < N; i++) {
                val[i] = ((char) (readInt()));
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readCharArray(char[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = ((char) (readInt()));
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeIntArray(int[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeInt(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    public final int[] createIntArray() {
        int N = readInt();
        if ((N >= 0) && (N <= (dataAvail() >> 2))) {
            int[] val = new int[N];
            for (int i = 0; i < N; i++) {
                val[i] = readInt();
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readIntArray(int[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readInt();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeLongArray(long[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeLong(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    public final long[] createLongArray() {
        int N = readInt();
        // >>3 because stored longs are 64 bits
        if ((N >= 0) && (N <= (dataAvail() >> 3))) {
            long[] val = new long[N];
            for (int i = 0; i < N; i++) {
                val[i] = readLong();
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readLongArray(long[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readLong();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeFloatArray(float[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeFloat(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    public final float[] createFloatArray() {
        int N = readInt();
        // >>2 because stored floats are 4 bytes
        if ((N >= 0) && (N <= (dataAvail() >> 2))) {
            float[] val = new float[N];
            for (int i = 0; i < N; i++) {
                val[i] = readFloat();
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readFloatArray(float[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readFloat();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeDoubleArray(double[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeDouble(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    public final double[] createDoubleArray() {
        int N = readInt();
        // >>3 because stored doubles are 8 bytes
        if ((N >= 0) && (N <= (dataAvail() >> 3))) {
            double[] val = new double[N];
            for (int i = 0; i < N; i++) {
                val[i] = readDouble();
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readDoubleArray(double[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readDouble();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeStringArray(java.lang.String[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeString(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    public final java.lang.String[] createStringArray() {
        int N = readInt();
        if (N >= 0) {
            java.lang.String[] val = new java.lang.String[N];
            for (int i = 0; i < N; i++) {
                val[i] = readString();
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readStringArray(java.lang.String[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readString();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    public final void writeBinderArray(android.os.IBinder[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeStrongBinder(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public final void writeCharSequenceArray(java.lang.CharSequence[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeCharSequence(val[i]);
            }
        } else {
            writeInt(-1);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public final void writeCharSequenceList(java.util.ArrayList<java.lang.CharSequence> val) {
        if (val != null) {
            int N = val.size();
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeCharSequence(val.get(i));
            }
        } else {
            writeInt(-1);
        }
    }

    public final android.os.IBinder[] createBinderArray() {
        int N = readInt();
        if (N >= 0) {
            android.os.IBinder[] val = new android.os.IBinder[N];
            for (int i = 0; i < N; i++) {
                val[i] = readStrongBinder();
            }
            return val;
        } else {
            return null;
        }
    }

    public final void readBinderArray(android.os.IBinder[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readStrongBinder();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    /**
     * Flatten a List containing a particular object type into the parcel, at
     * the current dataPosition() and growing dataCapacity() if needed.  The
     * type of the objects in the list must be one that implements Parcelable.
     * Unlike the generic writeList() method, however, only the raw data of the
     * objects is written and not their type, so you must use the corresponding
     * readTypedList() to unmarshall them.
     *
     * @param val
     * 		The list of objects to be written.
     * @see #createTypedArrayList
     * @see #readTypedList
     * @see Parcelable
     */
    public final <T extends android.os.Parcelable> void writeTypedList(java.util.List<T> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        int i = 0;
        writeInt(N);
        while (i < N) {
            T item = val.get(i);
            if (item != null) {
                writeInt(1);
                item.writeToParcel(this, 0);
            } else {
                writeInt(0);
            }
            i++;
        } 
    }

    /**
     * Flatten a List containing String objects into the parcel, at
     * the current dataPosition() and growing dataCapacity() if needed.  They
     * can later be retrieved with {@link #createStringArrayList} or
     * {@link #readStringList}.
     *
     * @param val
     * 		The list of strings to be written.
     * @see #createStringArrayList
     * @see #readStringList
     */
    public final void writeStringList(java.util.List<java.lang.String> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        int i = 0;
        writeInt(N);
        while (i < N) {
            writeString(val.get(i));
            i++;
        } 
    }

    /**
     * Flatten a List containing IBinder objects into the parcel, at
     * the current dataPosition() and growing dataCapacity() if needed.  They
     * can later be retrieved with {@link #createBinderArrayList} or
     * {@link #readBinderList}.
     *
     * @param val
     * 		The list of strings to be written.
     * @see #createBinderArrayList
     * @see #readBinderList
     */
    public final void writeBinderList(java.util.List<android.os.IBinder> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        int i = 0;
        writeInt(N);
        while (i < N) {
            writeStrongBinder(val.get(i));
            i++;
        } 
    }

    /**
     * Flatten a heterogeneous array containing a particular object type into
     * the parcel, at
     * the current dataPosition() and growing dataCapacity() if needed.  The
     * type of the objects in the array must be one that implements Parcelable.
     * Unlike the {@link #writeParcelableArray} method, however, only the
     * raw data of the objects is written and not their type, so you must use
     * {@link #readTypedArray} with the correct corresponding
     * {@link Parcelable.Creator} implementation to unmarshall them.
     *
     * @param val
     * 		The array of objects to be written.
     * @param parcelableFlags
     * 		Contextual flags as per
     * 		{@link Parcelable#writeToParcel(Parcel, int) Parcelable.writeToParcel()}.
     * @see #readTypedArray
     * @see #writeParcelableArray
     * @see Parcelable.Creator
     */
    public final <T extends android.os.Parcelable> void writeTypedArray(T[] val, int parcelableFlags) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                T item = val[i];
                if (item != null) {
                    writeInt(1);
                    item.writeToParcel(this, parcelableFlags);
                } else {
                    writeInt(0);
                }
            }
        } else {
            writeInt(-1);
        }
    }

    /**
     * Flatten the Parcelable object into the parcel.
     *
     * @param val
     * 		The Parcelable object to be written.
     * @param parcelableFlags
     * 		Contextual flags as per
     * 		{@link Parcelable#writeToParcel(Parcel, int) Parcelable.writeToParcel()}.
     * @see #readTypedObject
     */
    public final <T extends android.os.Parcelable> void writeTypedObject(T val, int parcelableFlags) {
        if (val != null) {
            writeInt(1);
            val.writeToParcel(this, parcelableFlags);
        } else {
            writeInt(0);
        }
    }

    /**
     * Flatten a generic object in to a parcel.  The given Object value may
     * currently be one of the following types:
     *
     * <ul>
     * <li> null
     * <li> String
     * <li> Byte
     * <li> Short
     * <li> Integer
     * <li> Long
     * <li> Float
     * <li> Double
     * <li> Boolean
     * <li> String[]
     * <li> boolean[]
     * <li> byte[]
     * <li> int[]
     * <li> long[]
     * <li> Object[] (supporting objects of the same type defined here).
     * <li> {@link Bundle}
     * <li> Map (as supported by {@link #writeMap}).
     * <li> Any object that implements the {@link Parcelable} protocol.
     * <li> Parcelable[]
     * <li> CharSequence (as supported by {@link TextUtils#writeToParcel}).
     * <li> List (as supported by {@link #writeList}).
     * <li> {@link SparseArray} (as supported by {@link #writeSparseArray(SparseArray)}).
     * <li> {@link IBinder}
     * <li> Any object that implements Serializable (but see
     *      {@link #writeSerializable} for caveats).  Note that all of the
     *      previous types have relatively efficient implementations for
     *      writing to a Parcel; having to rely on the generic serialization
     *      approach is much less efficient and should be avoided whenever
     *      possible.
     * </ul>
     *
     * <p class="caution">{@link Parcelable} objects are written with
     * {@link Parcelable#writeToParcel} using contextual flags of 0.  When
     * serializing objects containing {@link ParcelFileDescriptor}s,
     * this may result in file descriptor leaks when they are returned from
     * Binder calls (where {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     * should be used).</p>
     */
    public final void writeValue(java.lang.Object v) {
        if (v == null) {
            writeInt(android.os.Parcel.VAL_NULL);
        } else
            if (v instanceof java.lang.String) {
                writeInt(android.os.Parcel.VAL_STRING);
                writeString(((java.lang.String) (v)));
            } else
                if (v instanceof java.lang.Integer) {
                    writeInt(android.os.Parcel.VAL_INTEGER);
                    writeInt(((java.lang.Integer) (v)));
                } else
                    if (v instanceof java.util.Map) {
                        writeInt(android.os.Parcel.VAL_MAP);
                        writeMap(((java.util.Map) (v)));
                    } else
                        if (v instanceof android.os.Bundle) {
                            // Must be before Parcelable
                            writeInt(android.os.Parcel.VAL_BUNDLE);
                            writeBundle(((android.os.Bundle) (v)));
                        } else
                            if (v instanceof android.os.PersistableBundle) {
                                writeInt(android.os.Parcel.VAL_PERSISTABLEBUNDLE);
                                writePersistableBundle(((android.os.PersistableBundle) (v)));
                            } else
                                if (v instanceof android.os.Parcelable) {
                                    // IMPOTANT: cases for classes that implement Parcelable must
                                    // come before the Parcelable case, so that their specific VAL_*
                                    // types will be written.
                                    writeInt(android.os.Parcel.VAL_PARCELABLE);
                                    writeParcelable(((android.os.Parcelable) (v)), 0);
                                } else
                                    if (v instanceof java.lang.Short) {
                                        writeInt(android.os.Parcel.VAL_SHORT);
                                        writeInt(((java.lang.Short) (v)).intValue());
                                    } else
                                        if (v instanceof java.lang.Long) {
                                            writeInt(android.os.Parcel.VAL_LONG);
                                            writeLong(((java.lang.Long) (v)));
                                        } else
                                            if (v instanceof java.lang.Float) {
                                                writeInt(android.os.Parcel.VAL_FLOAT);
                                                writeFloat(((java.lang.Float) (v)));
                                            } else
                                                if (v instanceof java.lang.Double) {
                                                    writeInt(android.os.Parcel.VAL_DOUBLE);
                                                    writeDouble(((java.lang.Double) (v)));
                                                } else
                                                    if (v instanceof java.lang.Boolean) {
                                                        writeInt(android.os.Parcel.VAL_BOOLEAN);
                                                        writeInt(((java.lang.Boolean) (v)) ? 1 : 0);
                                                    } else
                                                        if (v instanceof java.lang.CharSequence) {
                                                            // Must be after String
                                                            writeInt(android.os.Parcel.VAL_CHARSEQUENCE);
                                                            writeCharSequence(((java.lang.CharSequence) (v)));
                                                        } else
                                                            if (v instanceof java.util.List) {
                                                                writeInt(android.os.Parcel.VAL_LIST);
                                                                writeList(((java.util.List) (v)));
                                                            } else
                                                                if (v instanceof android.util.SparseArray) {
                                                                    writeInt(android.os.Parcel.VAL_SPARSEARRAY);
                                                                    writeSparseArray(((android.util.SparseArray) (v)));
                                                                } else
                                                                    if (v instanceof boolean[]) {
                                                                        writeInt(android.os.Parcel.VAL_BOOLEANARRAY);
                                                                        writeBooleanArray(((boolean[]) (v)));
                                                                    } else
                                                                        if (v instanceof byte[]) {
                                                                            writeInt(android.os.Parcel.VAL_BYTEARRAY);
                                                                            writeByteArray(((byte[]) (v)));
                                                                        } else
                                                                            if (v instanceof java.lang.String[]) {
                                                                                writeInt(android.os.Parcel.VAL_STRINGARRAY);
                                                                                writeStringArray(((java.lang.String[]) (v)));
                                                                            } else
                                                                                if (v instanceof java.lang.CharSequence[]) {
                                                                                    // Must be after String[] and before Object[]
                                                                                    writeInt(android.os.Parcel.VAL_CHARSEQUENCEARRAY);
                                                                                    writeCharSequenceArray(((java.lang.CharSequence[]) (v)));
                                                                                } else
                                                                                    if (v instanceof android.os.IBinder) {
                                                                                        writeInt(android.os.Parcel.VAL_IBINDER);
                                                                                        writeStrongBinder(((android.os.IBinder) (v)));
                                                                                    } else
                                                                                        if (v instanceof android.os.Parcelable[]) {
                                                                                            writeInt(android.os.Parcel.VAL_PARCELABLEARRAY);
                                                                                            writeParcelableArray(((android.os.Parcelable[]) (v)), 0);
                                                                                        } else
                                                                                            if (v instanceof int[]) {
                                                                                                writeInt(android.os.Parcel.VAL_INTARRAY);
                                                                                                writeIntArray(((int[]) (v)));
                                                                                            } else
                                                                                                if (v instanceof long[]) {
                                                                                                    writeInt(android.os.Parcel.VAL_LONGARRAY);
                                                                                                    writeLongArray(((long[]) (v)));
                                                                                                } else
                                                                                                    if (v instanceof java.lang.Byte) {
                                                                                                        writeInt(android.os.Parcel.VAL_BYTE);
                                                                                                        writeInt(((java.lang.Byte) (v)));
                                                                                                    } else
                                                                                                        if (v instanceof android.util.Size) {
                                                                                                            writeInt(android.os.Parcel.VAL_SIZE);
                                                                                                            writeSize(((android.util.Size) (v)));
                                                                                                        } else
                                                                                                            if (v instanceof android.util.SizeF) {
                                                                                                                writeInt(android.os.Parcel.VAL_SIZEF);
                                                                                                                writeSizeF(((android.util.SizeF) (v)));
                                                                                                            } else
                                                                                                                if (v instanceof double[]) {
                                                                                                                    writeInt(android.os.Parcel.VAL_DOUBLEARRAY);
                                                                                                                    writeDoubleArray(((double[]) (v)));
                                                                                                                } else {
                                                                                                                    java.lang.Class<?> clazz = v.getClass();
                                                                                                                    if (clazz.isArray() && (clazz.getComponentType() == java.lang.Object.class)) {
                                                                                                                        // Only pure Object[] are written here, Other arrays of non-primitive types are
                                                                                                                        // handled by serialization as this does not record the component type.
                                                                                                                        writeInt(android.os.Parcel.VAL_OBJECTARRAY);
                                                                                                                        writeArray(((java.lang.Object[]) (v)));
                                                                                                                    } else
                                                                                                                        if (v instanceof java.io.Serializable) {
                                                                                                                            // Must be last
                                                                                                                            writeInt(android.os.Parcel.VAL_SERIALIZABLE);
                                                                                                                            writeSerializable(((java.io.Serializable) (v)));
                                                                                                                        } else {
                                                                                                                            throw new java.lang.RuntimeException("Parcel: unable to marshal value " + v);
                                                                                                                        }

                                                                                                                }


























    }

    /**
     * Flatten the name of the class of the Parcelable and its contents
     * into the parcel.
     *
     * @param p
     * 		The Parcelable object to be written.
     * @param parcelableFlags
     * 		Contextual flags as per
     * 		{@link Parcelable#writeToParcel(Parcel, int) Parcelable.writeToParcel()}.
     */
    public final void writeParcelable(android.os.Parcelable p, int parcelableFlags) {
        if (p == null) {
            writeString(null);
            return;
        }
        writeParcelableCreator(p);
        p.writeToParcel(this, parcelableFlags);
    }

    /**
     *
     *
     * @unknown 
     */
    public final void writeParcelableCreator(android.os.Parcelable p) {
        java.lang.String name = p.getClass().getName();
        writeString(name);
    }

    /**
     * Write a generic serializable object in to a Parcel.  It is strongly
     * recommended that this method be avoided, since the serialization
     * overhead is extremely large, and this approach will be much slower than
     * using the other approaches to writing data in to a Parcel.
     */
    public final void writeSerializable(java.io.Serializable s) {
        if (s == null) {
            writeString(null);
            return;
        }
        java.lang.String name = s.getClass().getName();
        writeString(name);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.close();
            writeByteArray(baos.toByteArray());
        } catch (java.io.IOException ioe) {
            throw new java.lang.RuntimeException((("Parcelable encountered " + "IOException writing serializable object (name = ") + name) + ")", ioe);
        }
    }

    /**
     * Special function for writing an exception result at the header of
     * a parcel, to be used when returning an exception from a transaction.
     * Note that this currently only supports a few exception types; any other
     * exception will be re-thrown by this function as a RuntimeException
     * (to be caught by the system's last-resort exception handling when
     * dispatching a transaction).
     *
     * <p>The supported exception types are:
     * <ul>
     * <li>{@link BadParcelableException}
     * <li>{@link IllegalArgumentException}
     * <li>{@link IllegalStateException}
     * <li>{@link NullPointerException}
     * <li>{@link SecurityException}
     * <li>{@link NetworkOnMainThreadException}
     * </ul>
     *
     * @param e
     * 		The Exception to be written.
     * @see #writeNoException
     * @see #readException
     */
    public final void writeException(java.lang.Exception e) {
        int code = 0;
        if (e instanceof java.lang.SecurityException) {
            code = android.os.Parcel.EX_SECURITY;
        } else
            if (e instanceof android.os.BadParcelableException) {
                code = android.os.Parcel.EX_BAD_PARCELABLE;
            } else
                if (e instanceof java.lang.IllegalArgumentException) {
                    code = android.os.Parcel.EX_ILLEGAL_ARGUMENT;
                } else
                    if (e instanceof java.lang.NullPointerException) {
                        code = android.os.Parcel.EX_NULL_POINTER;
                    } else
                        if (e instanceof java.lang.IllegalStateException) {
                            code = android.os.Parcel.EX_ILLEGAL_STATE;
                        } else
                            if (e instanceof android.os.NetworkOnMainThreadException) {
                                code = android.os.Parcel.EX_NETWORK_MAIN_THREAD;
                            } else
                                if (e instanceof java.lang.UnsupportedOperationException) {
                                    code = android.os.Parcel.EX_UNSUPPORTED_OPERATION;
                                } else
                                    if (e instanceof android.os.ServiceSpecificException) {
                                        code = android.os.Parcel.EX_SERVICE_SPECIFIC;
                                    }







        writeInt(code);
        android.os.StrictMode.clearGatheredViolations();
        if (code == 0) {
            if (e instanceof java.lang.RuntimeException) {
                throw ((java.lang.RuntimeException) (e));
            }
            throw new java.lang.RuntimeException(e);
        }
        writeString(e.getMessage());
        if (e instanceof android.os.ServiceSpecificException) {
            writeInt(((android.os.ServiceSpecificException) (e)).errorCode);
        }
    }

    /**
     * Special function for writing information at the front of the Parcel
     * indicating that no exception occurred.
     *
     * @see #writeException
     * @see #readException
     */
    public final void writeNoException() {
        // Despite the name of this function ("write no exception"),
        // it should instead be thought of as "write the RPC response
        // header", but because this function name is written out by
        // the AIDL compiler, we're not going to rename it.
        // 
        // The response header, in the non-exception case (see also
        // writeException above, also called by the AIDL compiler), is
        // either a 0 (the default case), or EX_HAS_REPLY_HEADER if
        // StrictMode has gathered up violations that have occurred
        // during a Binder call, in which case we write out the number
        // of violations and their details, serialized, before the
        // actual RPC respons data.  The receiving end of this is
        // readException(), below.
        if (android.os.StrictMode.hasGatheredViolations()) {
            writeInt(android.os.Parcel.EX_HAS_REPLY_HEADER);
            final int sizePosition = dataPosition();
            writeInt(0);// total size of fat header, to be filled in later

            android.os.StrictMode.writeGatheredViolationsToParcel(this);
            final int payloadPosition = dataPosition();
            setDataPosition(sizePosition);
            writeInt(payloadPosition - sizePosition);// header size

            setDataPosition(payloadPosition);
        } else {
            writeInt(0);
        }
    }

    /**
     * Special function for reading an exception result from the header of
     * a parcel, to be used after receiving the result of a transaction.  This
     * will throw the exception for you if it had been written to the Parcel,
     * otherwise return and let you read the normal result data from the Parcel.
     *
     * @see #writeException
     * @see #writeNoException
     */
    public final void readException() {
        int code = readExceptionCode();
        if (code != 0) {
            java.lang.String msg = readString();
            readException(code, msg);
        }
    }

    /**
     * Parses the header of a Binder call's response Parcel and
     * returns the exception code.  Deals with lite or fat headers.
     * In the common successful case, this header is generally zero.
     * In less common cases, it's a small negative number and will be
     * followed by an error string.
     *
     * This exists purely for android.database.DatabaseUtils and
     * insulating it from having to handle fat headers as returned by
     * e.g. StrictMode-induced RPC responses.
     *
     * @unknown 
     */
    public final int readExceptionCode() {
        int code = readInt();
        if (code == android.os.Parcel.EX_HAS_REPLY_HEADER) {
            int headerSize = readInt();
            if (headerSize == 0) {
                android.util.Log.e(android.os.Parcel.TAG, "Unexpected zero-sized Parcel reply header.");
            } else {
                // Currently the only thing in the header is StrictMode stacks,
                // but discussions around event/RPC tracing suggest we might
                // put that here too.  If so, switch on sub-header tags here.
                // But for now, just parse out the StrictMode stuff.
                android.os.StrictMode.readAndHandleBinderCallViolations(this);
            }
            // And fat response headers are currently only used when
            // there are no exceptions, so return no error:
            return 0;
        }
        return code;
    }

    /**
     * Throw an exception with the given message. Not intended for use
     * outside the Parcel class.
     *
     * @param code
     * 		Used to determine which exception class to throw.
     * @param msg
     * 		The exception message.
     */
    public final void readException(int code, java.lang.String msg) {
        switch (code) {
            case android.os.Parcel.EX_SECURITY :
                throw new java.lang.SecurityException(msg);
            case android.os.Parcel.EX_BAD_PARCELABLE :
                throw new android.os.BadParcelableException(msg);
            case android.os.Parcel.EX_ILLEGAL_ARGUMENT :
                throw new java.lang.IllegalArgumentException(msg);
            case android.os.Parcel.EX_NULL_POINTER :
                throw new java.lang.NullPointerException(msg);
            case android.os.Parcel.EX_ILLEGAL_STATE :
                throw new java.lang.IllegalStateException(msg);
            case android.os.Parcel.EX_NETWORK_MAIN_THREAD :
                throw new android.os.NetworkOnMainThreadException();
            case android.os.Parcel.EX_UNSUPPORTED_OPERATION :
                throw new java.lang.UnsupportedOperationException(msg);
            case android.os.Parcel.EX_SERVICE_SPECIFIC :
                throw new android.os.ServiceSpecificException(readInt(), msg);
        }
        throw new java.lang.RuntimeException((("Unknown exception code: " + code) + " msg ") + msg);
    }

    /**
     * Read an integer value from the parcel at the current dataPosition().
     */
    public final int readInt() {
        return android.os.Parcel.nativeReadInt(mNativePtr);
    }

    /**
     * Read a long integer value from the parcel at the current dataPosition().
     */
    public final long readLong() {
        return android.os.Parcel.nativeReadLong(mNativePtr);
    }

    /**
     * Read a floating point value from the parcel at the current
     * dataPosition().
     */
    public final float readFloat() {
        return android.os.Parcel.nativeReadFloat(mNativePtr);
    }

    /**
     * Read a double precision floating point value from the parcel at the
     * current dataPosition().
     */
    public final double readDouble() {
        return android.os.Parcel.nativeReadDouble(mNativePtr);
    }

    /**
     * Read a string value from the parcel at the current dataPosition().
     */
    public final java.lang.String readString() {
        return android.os.Parcel.nativeReadString(mNativePtr);
    }

    /**
     * Read a CharSequence value from the parcel at the current dataPosition().
     *
     * @unknown 
     */
    public final java.lang.CharSequence readCharSequence() {
        return android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this);
    }

    /**
     * Read an object from the parcel at the current dataPosition().
     */
    public final android.os.IBinder readStrongBinder() {
        return android.os.Parcel.nativeReadStrongBinder(mNativePtr);
    }

    /**
     * Read a FileDescriptor from the parcel at the current dataPosition().
     */
    public final android.os.ParcelFileDescriptor readFileDescriptor() {
        java.io.FileDescriptor fd = android.os.Parcel.nativeReadFileDescriptor(mNativePtr);
        return fd != null ? new android.os.ParcelFileDescriptor(fd) : null;
    }

    /**
     * {@hide }
     */
    public final java.io.FileDescriptor readRawFileDescriptor() {
        return android.os.Parcel.nativeReadFileDescriptor(mNativePtr);
    }

    /**
     * {@hide }
     * Read and return a new array of FileDescriptors from the parcel.
     *
     * @return the FileDescriptor array, or null if the array is null.
     */
    public final java.io.FileDescriptor[] createRawFileDescriptorArray() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.io.FileDescriptor[] f = new java.io.FileDescriptor[N];
        for (int i = 0; i < N; i++) {
            f[i] = readRawFileDescriptor();
        }
        return f;
    }

    /**
     * {@hide }
     * Read an array of FileDescriptors from a parcel.
     * The passed array must be exactly the length of the array in the parcel.
     *
     * @return the FileDescriptor array, or null if the array is null.
     */
    public final void readRawFileDescriptorArray(java.io.FileDescriptor[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readRawFileDescriptor();
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    /* package */
    static native java.io.FileDescriptor openFileDescriptor(java.lang.String file, int mode) throws java.io.FileNotFoundException;

    /* package */
    static native java.io.FileDescriptor dupFileDescriptor(java.io.FileDescriptor orig) throws java.io.IOException;

    /* package */
    static native void closeFileDescriptor(java.io.FileDescriptor desc) throws java.io.IOException;

    /* package */
    static native void clearFileDescriptor(java.io.FileDescriptor desc);

    /**
     * Read a byte value from the parcel at the current dataPosition().
     */
    public final byte readByte() {
        return ((byte) (readInt() & 0xff));
    }

    /**
     * Please use {@link #readBundle(ClassLoader)} instead (whose data must have
     * been written with {@link #writeBundle}.  Read into an existing Map object
     * from the parcel at the current dataPosition().
     */
    public final void readMap(java.util.Map outVal, java.lang.ClassLoader loader) {
        int N = readInt();
        readMapInternal(outVal, N, loader);
    }

    /**
     * Read into an existing List object from the parcel at the current
     * dataPosition(), using the given class loader to load any enclosed
     * Parcelables.  If it is null, the default class loader is used.
     */
    public final void readList(java.util.List outVal, java.lang.ClassLoader loader) {
        int N = readInt();
        readListInternal(outVal, N, loader);
    }

    /**
     * Please use {@link #readBundle(ClassLoader)} instead (whose data must have
     * been written with {@link #writeBundle}.  Read and return a new HashMap
     * object from the parcel at the current dataPosition(), using the given
     * class loader to load any enclosed Parcelables.  Returns null if
     * the previously written map object was null.
     */
    public final java.util.HashMap readHashMap(java.lang.ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.util.HashMap m = new java.util.HashMap(N);
        readMapInternal(m, N, loader);
        return m;
    }

    /**
     * Read and return a new Bundle object from the parcel at the current
     * dataPosition().  Returns null if the previously written Bundle object was
     * null.
     */
    public final android.os.Bundle readBundle() {
        return readBundle(null);
    }

    /**
     * Read and return a new Bundle object from the parcel at the current
     * dataPosition(), using the given class loader to initialize the class
     * loader of the Bundle for later retrieval of Parcelable objects.
     * Returns null if the previously written Bundle object was null.
     */
    public final android.os.Bundle readBundle(java.lang.ClassLoader loader) {
        int length = readInt();
        if (length < 0) {
            if (android.os.Bundle.DEBUG)
                android.util.Log.d(android.os.Parcel.TAG, "null bundle: length=" + length);

            return null;
        }
        final android.os.Bundle bundle = new android.os.Bundle(this, length);
        if (loader != null) {
            bundle.setClassLoader(loader);
        }
        return bundle;
    }

    /**
     * Read and return a new Bundle object from the parcel at the current
     * dataPosition().  Returns null if the previously written Bundle object was
     * null.
     */
    public final android.os.PersistableBundle readPersistableBundle() {
        return readPersistableBundle(null);
    }

    /**
     * Read and return a new Bundle object from the parcel at the current
     * dataPosition(), using the given class loader to initialize the class
     * loader of the Bundle for later retrieval of Parcelable objects.
     * Returns null if the previously written Bundle object was null.
     */
    public final android.os.PersistableBundle readPersistableBundle(java.lang.ClassLoader loader) {
        int length = readInt();
        if (length < 0) {
            if (android.os.Bundle.DEBUG)
                android.util.Log.d(android.os.Parcel.TAG, "null bundle: length=" + length);

            return null;
        }
        final android.os.PersistableBundle bundle = new android.os.PersistableBundle(this, length);
        if (loader != null) {
            bundle.setClassLoader(loader);
        }
        return bundle;
    }

    /**
     * Read a Size from the parcel at the current dataPosition().
     */
    public final android.util.Size readSize() {
        final int width = readInt();
        final int height = readInt();
        return new android.util.Size(width, height);
    }

    /**
     * Read a SizeF from the parcel at the current dataPosition().
     */
    public final android.util.SizeF readSizeF() {
        final float width = readFloat();
        final float height = readFloat();
        return new android.util.SizeF(width, height);
    }

    /**
     * Read and return a byte[] object from the parcel.
     */
    public final byte[] createByteArray() {
        return android.os.Parcel.nativeCreateByteArray(mNativePtr);
    }

    /**
     * Read a byte[] object from the parcel and copy it into the
     * given byte array.
     */
    public final void readByteArray(byte[] val) {
        // TODO: make this a native method to avoid the extra copy.
        byte[] ba = createByteArray();
        if (ba.length == val.length) {
            java.lang.System.arraycopy(ba, 0, val, 0, ba.length);
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    /**
     * Read a blob of data from the parcel and return it as a byte array.
     * {@hide }
     * {@SystemApi }
     */
    public final byte[] readBlob() {
        return android.os.Parcel.nativeReadBlob(mNativePtr);
    }

    /**
     * Read and return a String[] object from the parcel.
     * {@hide }
     */
    public final java.lang.String[] readStringArray() {
        java.lang.String[] array = null;
        int length = readInt();
        if (length >= 0) {
            array = new java.lang.String[length];
            for (int i = 0; i < length; i++) {
                array[i] = readString();
            }
        }
        return array;
    }

    /**
     * Read and return a CharSequence[] object from the parcel.
     * {@hide }
     */
    public final java.lang.CharSequence[] readCharSequenceArray() {
        java.lang.CharSequence[] array = null;
        int length = readInt();
        if (length >= 0) {
            array = new java.lang.CharSequence[length];
            for (int i = 0; i < length; i++) {
                array[i] = readCharSequence();
            }
        }
        return array;
    }

    /**
     * Read and return an ArrayList&lt;CharSequence&gt; object from the parcel.
     * {@hide }
     */
    public final java.util.ArrayList<java.lang.CharSequence> readCharSequenceList() {
        java.util.ArrayList<java.lang.CharSequence> array = null;
        int length = readInt();
        if (length >= 0) {
            array = new java.util.ArrayList<java.lang.CharSequence>(length);
            for (int i = 0; i < length; i++) {
                array.add(readCharSequence());
            }
        }
        return array;
    }

    /**
     * Read and return a new ArrayList object from the parcel at the current
     * dataPosition().  Returns null if the previously written list object was
     * null.  The given class loader will be used to load any enclosed
     * Parcelables.
     */
    public final java.util.ArrayList readArrayList(java.lang.ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.util.ArrayList l = new java.util.ArrayList(N);
        readListInternal(l, N, loader);
        return l;
    }

    /**
     * Read and return a new Object array from the parcel at the current
     * dataPosition().  Returns null if the previously written array was
     * null.  The given class loader will be used to load any enclosed
     * Parcelables.
     */
    public final java.lang.Object[] readArray(java.lang.ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.lang.Object[] l = new java.lang.Object[N];
        readArrayInternal(l, N, loader);
        return l;
    }

    /**
     * Read and return a new SparseArray object from the parcel at the current
     * dataPosition().  Returns null if the previously written list object was
     * null.  The given class loader will be used to load any enclosed
     * Parcelables.
     */
    public final android.util.SparseArray readSparseArray(java.lang.ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        android.util.SparseArray sa = new android.util.SparseArray(N);
        readSparseArrayInternal(sa, N, loader);
        return sa;
    }

    /**
     * Read and return a new SparseBooleanArray object from the parcel at the current
     * dataPosition().  Returns null if the previously written list object was
     * null.
     */
    public final android.util.SparseBooleanArray readSparseBooleanArray() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        android.util.SparseBooleanArray sa = new android.util.SparseBooleanArray(N);
        readSparseBooleanArrayInternal(sa, N);
        return sa;
    }

    /**
     * Read and return a new ArrayList containing a particular object type from
     * the parcel that was written with {@link #writeTypedList} at the
     * current dataPosition().  Returns null if the
     * previously written list object was null.  The list <em>must</em> have
     * previously been written via {@link #writeTypedList} with the same object
     * type.
     *
     * @return A newly created ArrayList containing objects with the same data
    as those that were previously written.
     * @see #writeTypedList
     */
    public final <T> java.util.ArrayList<T> createTypedArrayList(android.os.Parcelable.Creator<T> c) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.util.ArrayList<T> l = new java.util.ArrayList<T>(N);
        while (N > 0) {
            if (readInt() != 0) {
                l.add(c.createFromParcel(this));
            } else {
                l.add(null);
            }
            N--;
        } 
        return l;
    }

    /**
     * Read into the given List items containing a particular object type
     * that were written with {@link #writeTypedList} at the
     * current dataPosition().  The list <em>must</em> have
     * previously been written via {@link #writeTypedList} with the same object
     * type.
     *
     * @return A newly created ArrayList containing objects with the same data
    as those that were previously written.
     * @see #writeTypedList
     */
    public final <T> void readTypedList(java.util.List<T> list, android.os.Parcelable.Creator<T> c) {
        int M = list.size();
        int N = readInt();
        int i = 0;
        for (; (i < M) && (i < N); i++) {
            if (readInt() != 0) {
                list.set(i, c.createFromParcel(this));
            } else {
                list.set(i, null);
            }
        }
        for (; i < N; i++) {
            if (readInt() != 0) {
                list.add(c.createFromParcel(this));
            } else {
                list.add(null);
            }
        }
        for (; i < M; i++) {
            list.remove(N);
        }
    }

    /**
     * Read and return a new ArrayList containing String objects from
     * the parcel that was written with {@link #writeStringList} at the
     * current dataPosition().  Returns null if the
     * previously written list object was null.
     *
     * @return A newly created ArrayList containing strings with the same data
    as those that were previously written.
     * @see #writeStringList
     */
    public final java.util.ArrayList<java.lang.String> createStringArrayList() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.util.ArrayList<java.lang.String> l = new java.util.ArrayList<java.lang.String>(N);
        while (N > 0) {
            l.add(readString());
            N--;
        } 
        return l;
    }

    /**
     * Read and return a new ArrayList containing IBinder objects from
     * the parcel that was written with {@link #writeBinderList} at the
     * current dataPosition().  Returns null if the
     * previously written list object was null.
     *
     * @return A newly created ArrayList containing strings with the same data
    as those that were previously written.
     * @see #writeBinderList
     */
    public final java.util.ArrayList<android.os.IBinder> createBinderArrayList() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        java.util.ArrayList<android.os.IBinder> l = new java.util.ArrayList<android.os.IBinder>(N);
        while (N > 0) {
            l.add(readStrongBinder());
            N--;
        } 
        return l;
    }

    /**
     * Read into the given List items String objects that were written with
     * {@link #writeStringList} at the current dataPosition().
     *
     * @return A newly created ArrayList containing strings with the same data
    as those that were previously written.
     * @see #writeStringList
     */
    public final void readStringList(java.util.List<java.lang.String> list) {
        int M = list.size();
        int N = readInt();
        int i = 0;
        for (; (i < M) && (i < N); i++) {
            list.set(i, readString());
        }
        for (; i < N; i++) {
            list.add(readString());
        }
        for (; i < M; i++) {
            list.remove(N);
        }
    }

    /**
     * Read into the given List items IBinder objects that were written with
     * {@link #writeBinderList} at the current dataPosition().
     *
     * @return A newly created ArrayList containing strings with the same data
    as those that were previously written.
     * @see #writeBinderList
     */
    public final void readBinderList(java.util.List<android.os.IBinder> list) {
        int M = list.size();
        int N = readInt();
        int i = 0;
        for (; (i < M) && (i < N); i++) {
            list.set(i, readStrongBinder());
        }
        for (; i < N; i++) {
            list.add(readStrongBinder());
        }
        for (; i < M; i++) {
            list.remove(N);
        }
    }

    /**
     * Read and return a new array containing a particular object type from
     * the parcel at the current dataPosition().  Returns null if the
     * previously written array was null.  The array <em>must</em> have
     * previously been written via {@link #writeTypedArray} with the same
     * object type.
     *
     * @return A newly created array containing objects with the same data
    as those that were previously written.
     * @see #writeTypedArray
     */
    public final <T> T[] createTypedArray(android.os.Parcelable.Creator<T> c) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        T[] l = c.newArray(N);
        for (int i = 0; i < N; i++) {
            if (readInt() != 0) {
                l[i] = c.createFromParcel(this);
            }
        }
        return l;
    }

    public final <T> void readTypedArray(T[] val, android.os.Parcelable.Creator<T> c) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                if (readInt() != 0) {
                    val[i] = c.createFromParcel(this);
                } else {
                    val[i] = null;
                }
            }
        } else {
            throw new java.lang.RuntimeException("bad array lengths");
        }
    }

    /**
     *
     *
     * @deprecated 
     * @unknown 
     */
    @java.lang.Deprecated
    public final <T> T[] readTypedArray(android.os.Parcelable.Creator<T> c) {
        return createTypedArray(c);
    }

    /**
     * Read and return a typed Parcelable object from a parcel.
     * Returns null if the previous written object was null.
     * The object <em>must</em> have previous been written via
     * {@link #writeTypedObject} with the same object type.
     *
     * @return A newly created object of the type that was previously
    written.
     * @see #writeTypedObject
     */
    public final <T> T readTypedObject(android.os.Parcelable.Creator<T> c) {
        if (readInt() != 0) {
            return c.createFromParcel(this);
        } else {
            return null;
        }
    }

    /**
     * Write a heterogeneous array of Parcelable objects into the Parcel.
     * Each object in the array is written along with its class name, so
     * that the correct class can later be instantiated.  As a result, this
     * has significantly more overhead than {@link #writeTypedArray}, but will
     * correctly handle an array containing more than one type of object.
     *
     * @param value
     * 		The array of objects to be written.
     * @param parcelableFlags
     * 		Contextual flags as per
     * 		{@link Parcelable#writeToParcel(Parcel, int) Parcelable.writeToParcel()}.
     * @see #writeTypedArray
     */
    public final <T extends android.os.Parcelable> void writeParcelableArray(T[] value, int parcelableFlags) {
        if (value != null) {
            int N = value.length;
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeParcelable(value[i], parcelableFlags);
            }
        } else {
            writeInt(-1);
        }
    }

    /**
     * Read a typed object from a parcel.  The given class loader will be
     * used to load any enclosed Parcelables.  If it is null, the default class
     * loader will be used.
     */
    public final java.lang.Object readValue(java.lang.ClassLoader loader) {
        int type = readInt();
        switch (type) {
            case android.os.Parcel.VAL_NULL :
                return null;
            case android.os.Parcel.VAL_STRING :
                return readString();
            case android.os.Parcel.VAL_INTEGER :
                return readInt();
            case android.os.Parcel.VAL_MAP :
                return readHashMap(loader);
            case android.os.Parcel.VAL_PARCELABLE :
                return readParcelable(loader);
            case android.os.Parcel.VAL_SHORT :
                return ((short) (readInt()));
            case android.os.Parcel.VAL_LONG :
                return readLong();
            case android.os.Parcel.VAL_FLOAT :
                return readFloat();
            case android.os.Parcel.VAL_DOUBLE :
                return readDouble();
            case android.os.Parcel.VAL_BOOLEAN :
                return readInt() == 1;
            case android.os.Parcel.VAL_CHARSEQUENCE :
                return readCharSequence();
            case android.os.Parcel.VAL_LIST :
                return readArrayList(loader);
            case android.os.Parcel.VAL_BOOLEANARRAY :
                return createBooleanArray();
            case android.os.Parcel.VAL_BYTEARRAY :
                return createByteArray();
            case android.os.Parcel.VAL_STRINGARRAY :
                return readStringArray();
            case android.os.Parcel.VAL_CHARSEQUENCEARRAY :
                return readCharSequenceArray();
            case android.os.Parcel.VAL_IBINDER :
                return readStrongBinder();
            case android.os.Parcel.VAL_OBJECTARRAY :
                return readArray(loader);
            case android.os.Parcel.VAL_INTARRAY :
                return createIntArray();
            case android.os.Parcel.VAL_LONGARRAY :
                return createLongArray();
            case android.os.Parcel.VAL_BYTE :
                return readByte();
            case android.os.Parcel.VAL_SERIALIZABLE :
                return readSerializable(loader);
            case android.os.Parcel.VAL_PARCELABLEARRAY :
                return readParcelableArray(loader);
            case android.os.Parcel.VAL_SPARSEARRAY :
                return readSparseArray(loader);
            case android.os.Parcel.VAL_SPARSEBOOLEANARRAY :
                return readSparseBooleanArray();
            case android.os.Parcel.VAL_BUNDLE :
                return readBundle(loader);// loading will be deferred

            case android.os.Parcel.VAL_PERSISTABLEBUNDLE :
                return readPersistableBundle(loader);
            case android.os.Parcel.VAL_SIZE :
                return readSize();
            case android.os.Parcel.VAL_SIZEF :
                return readSizeF();
            case android.os.Parcel.VAL_DOUBLEARRAY :
                return createDoubleArray();
            default :
                int off = dataPosition() - 4;
                throw new java.lang.RuntimeException((((("Parcel " + this) + ": Unmarshalling unknown type code ") + type) + " at offset ") + off);
        }
    }

    /**
     * Read and return a new Parcelable from the parcel.  The given class loader
     * will be used to load any enclosed Parcelables.  If it is null, the default
     * class loader will be used.
     *
     * @param loader
     * 		A ClassLoader from which to instantiate the Parcelable
     * 		object, or null for the default class loader.
     * @return Returns the newly created Parcelable, or null if a null
    object has been written.
     * @throws BadParcelableException
     * 		Throws BadParcelableException if there
     * 		was an error trying to instantiate the Parcelable.
     */
    @java.lang.SuppressWarnings("unchecked")
    public final <T extends android.os.Parcelable> T readParcelable(java.lang.ClassLoader loader) {
        android.os.Parcelable.Creator<?> creator = readParcelableCreator(loader);
        if (creator == null) {
            return null;
        }
        if (creator instanceof android.os.Parcelable.ClassLoaderCreator<?>) {
            android.os.Parcelable.ClassLoaderCreator<?> classLoaderCreator = ((android.os.Parcelable.ClassLoaderCreator<?>) (creator));
            return ((T) (classLoaderCreator.createFromParcel(this, loader)));
        }
        return ((T) (creator.createFromParcel(this)));
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.SuppressWarnings("unchecked")
    public final <T extends android.os.Parcelable> T readCreator(android.os.Parcelable.Creator<?> creator, java.lang.ClassLoader loader) {
        if (creator instanceof android.os.Parcelable.ClassLoaderCreator<?>) {
            android.os.Parcelable.ClassLoaderCreator<?> classLoaderCreator = ((android.os.Parcelable.ClassLoaderCreator<?>) (creator));
            return ((T) (classLoaderCreator.createFromParcel(this, loader)));
        }
        return ((T) (creator.createFromParcel(this)));
    }

    /**
     *
     *
     * @unknown 
     */
    public final android.os.Parcelable.Creator<?> readParcelableCreator(java.lang.ClassLoader loader) {
        java.lang.String name = readString();
        if (name == null) {
            return null;
        }
        android.os.Parcelable.Creator<?> creator;
        synchronized(android.os.Parcel.mCreators) {
            java.util.HashMap<java.lang.String, android.os.Parcelable.Creator<?>> map = android.os.Parcel.mCreators.get(loader);
            if (map == null) {
                map = new java.util.HashMap<>();
                android.os.Parcel.mCreators.put(loader, map);
            }
            creator = map.get(name);
            if (creator == null) {
                try {
                    // If loader == null, explicitly emulate Class.forName(String) "caller
                    // classloader" behavior.
                    java.lang.ClassLoader parcelableClassLoader = (loader == null) ? getClass().getClassLoader() : loader;
                    // Avoid initializing the Parcelable class until we know it implements
                    // Parcelable and has the necessary CREATOR field. http://b/1171613.
                    java.lang.Class<?> parcelableClass = /* initialize */
                    java.lang.Class.forName(name, false, parcelableClassLoader);
                    if (!android.os.Parcelable.class.isAssignableFrom(parcelableClass)) {
                        throw new android.os.BadParcelableException("Parcelable protocol requires that the " + "class implements Parcelable");
                    }
                    java.lang.reflect.Field f = parcelableClass.getField("CREATOR");
                    if ((f.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0) {
                        throw new android.os.BadParcelableException(("Parcelable protocol requires " + "the CREATOR object to be static on class ") + name);
                    }
                    java.lang.Class<?> creatorType = f.getType();
                    if (!android.os.Parcelable.Creator.class.isAssignableFrom(creatorType)) {
                        // Fail before calling Field.get(), not after, to avoid initializing
                        // parcelableClass unnecessarily.
                        throw new android.os.BadParcelableException(("Parcelable protocol requires a " + ("Parcelable.Creator object called " + "CREATOR on class ")) + name);
                    }
                    creator = ((android.os.Parcelable.Creator<?>) (f.get(null)));
                } catch (java.lang.IllegalAccessException e) {
                    android.util.Log.e(android.os.Parcel.TAG, "Illegal access when unmarshalling: " + name, e);
                    throw new android.os.BadParcelableException("IllegalAccessException when unmarshalling: " + name);
                } catch (java.lang.ClassNotFoundException e) {
                    android.util.Log.e(android.os.Parcel.TAG, "Class not found when unmarshalling: " + name, e);
                    throw new android.os.BadParcelableException("ClassNotFoundException when unmarshalling: " + name);
                } catch (java.lang.NoSuchFieldException e) {
                    throw new android.os.BadParcelableException(("Parcelable protocol requires a " + ("Parcelable.Creator object called " + "CREATOR on class ")) + name);
                }
                if (creator == null) {
                    throw new android.os.BadParcelableException(("Parcelable protocol requires a " + ("non-null Parcelable.Creator object called " + "CREATOR on class ")) + name);
                }
                map.put(name, creator);
            }
        }
        return creator;
    }

    /**
     * Read and return a new Parcelable array from the parcel.
     * The given class loader will be used to load any enclosed
     * Parcelables.
     *
     * @return the Parcelable array, or null if the array is null
     */
    public final android.os.Parcelable[] readParcelableArray(java.lang.ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        android.os.Parcelable[] p = new android.os.Parcelable[N];
        for (int i = 0; i < N; i++) {
            p[i] = readParcelable(loader);
        }
        return p;
    }

    /**
     *
     *
     * @unknown 
     */
    public final <T extends android.os.Parcelable> T[] readParcelableArray(java.lang.ClassLoader loader, java.lang.Class<T> clazz) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        T[] p = ((T[]) (java.lang.reflect.Array.newInstance(clazz, N)));
        for (int i = 0; i < N; i++) {
            p[i] = readParcelable(loader);
        }
        return p;
    }

    /**
     * Read and return a new Serializable object from the parcel.
     *
     * @return the Serializable object, or null if the Serializable name
    wasn't found in the parcel.
     */
    public final java.io.Serializable readSerializable() {
        return readSerializable(null);
    }

    private final java.io.Serializable readSerializable(final java.lang.ClassLoader loader) {
        java.lang.String name = readString();
        if (name == null) {
            // For some reason we were unable to read the name of the Serializable (either there
            // is nothing left in the Parcel to read, or the next value wasn't a String), so
            // return null, which indicates that the name wasn't found in the parcel.
            return null;
        }
        byte[] serializedData = createByteArray();
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(serializedData);
        try {
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais) {
                @java.lang.Override
                protected java.lang.Class<?> resolveClass(java.io.ObjectStreamClass osClass) throws java.io.IOException, java.lang.ClassNotFoundException {
                    // try the custom classloader if provided
                    if (loader != null) {
                        java.lang.Class<?> c = java.lang.Class.forName(osClass.getName(), false, loader);
                        if (c != null) {
                            return c;
                        }
                    }
                    return super.resolveClass(osClass);
                }
            };
            return ((java.io.Serializable) (ois.readObject()));
        } catch (java.io.IOException ioe) {
            throw new java.lang.RuntimeException((("Parcelable encountered " + "IOException reading a Serializable object (name = ") + name) + ")", ioe);
        } catch (java.lang.ClassNotFoundException cnfe) {
            throw new java.lang.RuntimeException((("Parcelable encountered " + "ClassNotFoundException reading a Serializable object (name = ") + name) + ")", cnfe);
        }
    }

    // Cache of previously looked up CREATOR.createFromParcel() methods for
    // particular classes.  Keys are the names of the classes, values are
    // Method objects.
    private static final java.util.HashMap<java.lang.ClassLoader, java.util.HashMap<java.lang.String, android.os.Parcelable.Creator<?>>> mCreators = new java.util.HashMap<>();

    /**
     *
     *
     * @unknown for internal use only.
     */
    protected static final android.os.Parcel obtain(int obj) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    protected static final android.os.Parcel obtain(long obj) {
        final android.os.Parcel[] pool = android.os.Parcel.sHolderPool;
        synchronized(pool) {
            android.os.Parcel p;
            for (int i = 0; i < android.os.Parcel.POOL_SIZE; i++) {
                p = pool[i];
                if (p != null) {
                    pool[i] = null;
                    if (android.os.Parcel.DEBUG_RECYCLE) {
                        p.mStack = new java.lang.RuntimeException();
                    }
                    p.init(obj);
                    return p;
                }
            }
        }
        return new android.os.Parcel(obj);
    }

    private Parcel(long nativePtr) {
        if (android.os.Parcel.DEBUG_RECYCLE) {
            mStack = new java.lang.RuntimeException();
        }
        // Log.i(TAG, "Initializing obj=0x" + Integer.toHexString(obj), mStack);
        init(nativePtr);
    }

    private void init(long nativePtr) {
        if (nativePtr != 0) {
            mNativePtr = nativePtr;
            mOwnsNativeParcelObject = false;
        } else {
            mNativePtr = android.os.Parcel.nativeCreate();
            mOwnsNativeParcelObject = true;
        }
    }

    private void freeBuffer() {
        if (mOwnsNativeParcelObject) {
            updateNativeSize(android.os.Parcel.nativeFreeBuffer(mNativePtr));
        }
    }

    private void destroy() {
        if (mNativePtr != 0) {
            if (mOwnsNativeParcelObject) {
                android.os.Parcel.nativeDestroy(mNativePtr);
                updateNativeSize(0);
            }
            mNativePtr = 0;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        if (android.os.Parcel.DEBUG_RECYCLE) {
            if (mStack != null) {
                android.util.Log.w(android.os.Parcel.TAG, "Client did not call Parcel.recycle()", mStack);
            }
        }
        destroy();
    }

    /* package */
    void readMapInternal(java.util.Map outVal, int N, java.lang.ClassLoader loader) {
        while (N > 0) {
            java.lang.Object key = readValue(loader);
            java.lang.Object value = readValue(loader);
            outVal.put(key, value);
            N--;
        } 
    }

    /* package */
    void readArrayMapInternal(android.util.ArrayMap outVal, int N, java.lang.ClassLoader loader) {
        if (android.os.Parcel.DEBUG_ARRAY_MAP) {
            java.lang.RuntimeException here = new java.lang.RuntimeException("here");
            here.fillInStackTrace();
            android.util.Log.d(android.os.Parcel.TAG, ("Reading " + N) + " ArrayMap entries", here);
        }
        int startPos;
        while (N > 0) {
            if (android.os.Parcel.DEBUG_ARRAY_MAP)
                startPos = dataPosition();

            java.lang.String key = readString();
            java.lang.Object value = readValue(loader);
            if (android.os.Parcel.DEBUG_ARRAY_MAP)
                android.util.Log.d(android.os.Parcel.TAG, (((((("  Read #" + (N - 1)) + " ") + (dataPosition() - startPos)) + " bytes: key=0x") + java.lang.Integer.toHexString(key != null ? key.hashCode() : 0)) + " ") + key);

            outVal.append(key, value);
            N--;
        } 
        outVal.validate();
    }

    /* package */
    void readArrayMapSafelyInternal(android.util.ArrayMap outVal, int N, java.lang.ClassLoader loader) {
        if (android.os.Parcel.DEBUG_ARRAY_MAP) {
            java.lang.RuntimeException here = new java.lang.RuntimeException("here");
            here.fillInStackTrace();
            android.util.Log.d(android.os.Parcel.TAG, ("Reading safely " + N) + " ArrayMap entries", here);
        }
        while (N > 0) {
            java.lang.String key = readString();
            if (android.os.Parcel.DEBUG_ARRAY_MAP)
                android.util.Log.d(android.os.Parcel.TAG, (((("  Read safe #" + (N - 1)) + ": key=0x") + (key != null ? key.hashCode() : 0)) + " ") + key);

            java.lang.Object value = readValue(loader);
            outVal.put(key, value);
            N--;
        } 
    }

    /**
     *
     *
     * @unknown For testing only.
     */
    public void readArrayMap(android.util.ArrayMap outVal, java.lang.ClassLoader loader) {
        final int N = readInt();
        if (N < 0) {
            return;
        }
        readArrayMapInternal(outVal, N, loader);
    }

    /**
     * Reads an array set.
     *
     * @param loader
     * 		The class loader to use.
     * @unknown 
     */
    @android.annotation.Nullable
    public android.util.ArraySet<? extends java.lang.Object> readArraySet(java.lang.ClassLoader loader) {
        final int size = readInt();
        if (size < 0) {
            return null;
        }
        android.util.ArraySet<java.lang.Object> result = new android.util.ArraySet<>(size);
        for (int i = 0; i < size; i++) {
            java.lang.Object value = readValue(loader);
            result.append(value);
        }
        return result;
    }

    private void readListInternal(java.util.List outVal, int N, java.lang.ClassLoader loader) {
        while (N > 0) {
            java.lang.Object value = readValue(loader);
            // Log.d(TAG, "Unmarshalling value=" + value);
            outVal.add(value);
            N--;
        } 
    }

    private void readArrayInternal(java.lang.Object[] outVal, int N, java.lang.ClassLoader loader) {
        for (int i = 0; i < N; i++) {
            java.lang.Object value = readValue(loader);
            // Log.d(TAG, "Unmarshalling value=" + value);
            outVal[i] = value;
        }
    }

    private void readSparseArrayInternal(android.util.SparseArray outVal, int N, java.lang.ClassLoader loader) {
        while (N > 0) {
            int key = readInt();
            java.lang.Object value = readValue(loader);
            // Log.i(TAG, "Unmarshalling key=" + key + " value=" + value);
            outVal.append(key, value);
            N--;
        } 
    }

    private void readSparseBooleanArrayInternal(android.util.SparseBooleanArray outVal, int N) {
        while (N > 0) {
            int key = readInt();
            boolean value = this.readByte() == 1;
            // Log.i(TAG, "Unmarshalling key=" + key + " value=" + value);
            outVal.append(key, value);
            N--;
        } 
    }

    /**
     *
     *
     * @unknown For testing
     */
    public long getBlobAshmemSize() {
        return android.os.Parcel.nativeGetBlobAshmemSize(mNativePtr);
    }
}

