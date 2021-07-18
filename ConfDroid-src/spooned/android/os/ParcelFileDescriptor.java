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
 * The FileDescriptor returned by {@link Parcel#readFileDescriptor}, allowing
 * you to close it when done with it.
 */
public class ParcelFileDescriptor implements android.os.Parcelable , java.io.Closeable {
    private static final java.lang.String TAG = "ParcelFileDescriptor";

    private final java.io.FileDescriptor mFd;

    /**
     * Optional socket used to communicate close events, status at close, and
     * detect remote process crashes.
     */
    private java.io.FileDescriptor mCommFd;

    /**
     * Wrapped {@link ParcelFileDescriptor}, if any. Used to avoid
     * double-closing {@link #mFd}.
     */
    private final android.os.ParcelFileDescriptor mWrapped;

    /**
     * Maximum {@link #mStatusBuf} size; longer status messages will be
     * truncated.
     */
    private static final int MAX_STATUS = 1024;

    /**
     * Temporary buffer used by {@link #readCommStatus(FileDescriptor, byte[])},
     * allocated on-demand.
     */
    private byte[] mStatusBuf;

    /**
     * Status read by {@link #checkError()}, or null if not read yet.
     */
    private android.os.ParcelFileDescriptor.Status mStatus;

    private volatile boolean mClosed;

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    /**
     * For use with {@link #open}: if {@link #MODE_CREATE} has been supplied and
     * this file doesn't already exist, then create the file with permissions
     * such that any application can read it.
     *
     * @deprecated Creating world-readable files is very dangerous, and likely
    to cause security holes in applications. It is strongly
    discouraged; instead, applications should use more formal
    mechanism for interactions such as {@link ContentProvider},
    {@link BroadcastReceiver}, and {@link android.app.Service}.
    There are no guarantees that this access mode will remain on
    a file, such as when it goes through a backup and restore.
     */
    @java.lang.Deprecated
    public static final int MODE_WORLD_READABLE = 0x1;

    /**
     * For use with {@link #open}: if {@link #MODE_CREATE} has been supplied and
     * this file doesn't already exist, then create the file with permissions
     * such that any application can write it.
     *
     * @deprecated Creating world-writable files is very dangerous, and likely
    to cause security holes in applications. It is strongly
    discouraged; instead, applications should use more formal
    mechanism for interactions such as {@link ContentProvider},
    {@link BroadcastReceiver}, and {@link android.app.Service}.
    There are no guarantees that this access mode will remain on
    a file, such as when it goes through a backup and restore.
     */
    @java.lang.Deprecated
    public static final int MODE_WORLD_WRITEABLE = 0x2;

    /**
     * For use with {@link #open}: open the file with read-only access.
     */
    public static final int MODE_READ_ONLY = 0x10000000;

    /**
     * For use with {@link #open}: open the file with write-only access.
     */
    public static final int MODE_WRITE_ONLY = 0x20000000;

    /**
     * For use with {@link #open}: open the file with read and write access.
     */
    public static final int MODE_READ_WRITE = 0x30000000;

    /**
     * For use with {@link #open}: create the file if it doesn't already exist.
     */
    public static final int MODE_CREATE = 0x8000000;

    /**
     * For use with {@link #open}: erase contents of file when opening.
     */
    public static final int MODE_TRUNCATE = 0x4000000;

    /**
     * For use with {@link #open}: append to end of file while writing.
     */
    public static final int MODE_APPEND = 0x2000000;

    /**
     * Create a new ParcelFileDescriptor wrapped around another descriptor. By
     * default all method calls are delegated to the wrapped descriptor.
     */
    public ParcelFileDescriptor(android.os.ParcelFileDescriptor wrapped) {
        // We keep a strong reference to the wrapped PFD, and rely on its
        // finalizer to trigger CloseGuard. All calls are delegated to wrapper.
        mWrapped = wrapped;
        mFd = null;
        mCommFd = null;
        mClosed = true;
    }

    /**
     * {@hide }
     */
    public ParcelFileDescriptor(java.io.FileDescriptor fd) {
        this(fd, null);
    }

    /**
     * {@hide }
     */
    public ParcelFileDescriptor(java.io.FileDescriptor fd, java.io.FileDescriptor commChannel) {
        if (fd == null) {
            throw new java.lang.NullPointerException("FileDescriptor must not be null");
        }
        mWrapped = null;
        mFd = fd;
        mCommFd = commChannel;
        mGuard.open("close");
    }

    /**
     * Create a new ParcelFileDescriptor accessing a given file.
     *
     * @param file
     * 		The file to be opened.
     * @param mode
     * 		The desired access mode, must be one of
     * 		{@link #MODE_READ_ONLY}, {@link #MODE_WRITE_ONLY}, or
     * 		{@link #MODE_READ_WRITE}; may also be any combination of
     * 		{@link #MODE_CREATE}, {@link #MODE_TRUNCATE},
     * 		{@link #MODE_WORLD_READABLE}, and
     * 		{@link #MODE_WORLD_WRITEABLE}.
     * @return a new ParcelFileDescriptor pointing to the given file.
     * @throws FileNotFoundException
     * 		if the given file does not exist or can not
     * 		be opened with the requested mode.
     * @see #parseMode(String)
     */
    public static android.os.ParcelFileDescriptor open(java.io.File file, int mode) throws java.io.FileNotFoundException {
        final java.io.FileDescriptor fd = android.os.ParcelFileDescriptor.openInternal(file, mode);
        if (fd == null)
            return null;

        return new android.os.ParcelFileDescriptor(fd);
    }

    /**
     * Create a new ParcelFileDescriptor accessing a given file.
     *
     * @param file
     * 		The file to be opened.
     * @param mode
     * 		The desired access mode, must be one of
     * 		{@link #MODE_READ_ONLY}, {@link #MODE_WRITE_ONLY}, or
     * 		{@link #MODE_READ_WRITE}; may also be any combination of
     * 		{@link #MODE_CREATE}, {@link #MODE_TRUNCATE},
     * 		{@link #MODE_WORLD_READABLE}, and
     * 		{@link #MODE_WORLD_WRITEABLE}.
     * @param handler
     * 		to call listener from; must not be null.
     * @param listener
     * 		to be invoked when the returned descriptor has been
     * 		closed; must not be null.
     * @return a new ParcelFileDescriptor pointing to the given file.
     * @throws FileNotFoundException
     * 		if the given file does not exist or can not
     * 		be opened with the requested mode.
     * @see #parseMode(String)
     */
    public static android.os.ParcelFileDescriptor open(java.io.File file, int mode, android.os.Handler handler, final android.os.ParcelFileDescriptor.OnCloseListener listener) throws java.io.IOException {
        if (handler == null) {
            throw new java.lang.IllegalArgumentException("Handler must not be null");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("Listener must not be null");
        }
        final java.io.FileDescriptor fd = android.os.ParcelFileDescriptor.openInternal(file, mode);
        if (fd == null)
            return null;

        return android.os.ParcelFileDescriptor.fromFd(fd, handler, listener);
    }

    /**
     * {@hide }
     */
    public static android.os.ParcelFileDescriptor fromFd(java.io.FileDescriptor fd, android.os.Handler handler, final android.os.ParcelFileDescriptor.OnCloseListener listener) throws java.io.IOException {
        if (handler == null) {
            throw new java.lang.IllegalArgumentException("Handler must not be null");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("Listener must not be null");
        }
        final java.io.FileDescriptor[] comm = android.os.ParcelFileDescriptor.createCommSocketPair();
        final android.os.ParcelFileDescriptor pfd = new android.os.ParcelFileDescriptor(fd, comm[0]);
        final android.os.MessageQueue queue = handler.getLooper().getQueue();
        queue.addOnFileDescriptorEventListener(comm[1], android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT, new android.os.MessageQueue.OnFileDescriptorEventListener() {
            @java.lang.Override
            public int onFileDescriptorEvents(java.io.FileDescriptor fd, int events) {
                android.os.ParcelFileDescriptor.Status status = null;
                if ((events & android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT) != 0) {
                    final byte[] buf = new byte[android.os.ParcelFileDescriptor.MAX_STATUS];
                    status = android.os.ParcelFileDescriptor.readCommStatus(fd, buf);
                } else
                    if ((events & android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_ERROR) != 0) {
                        status = new android.os.ParcelFileDescriptor.Status(android.os.ParcelFileDescriptor.Status.DEAD);
                    }

                if (status != null) {
                    queue.removeOnFileDescriptorEventListener(fd);
                    libcore.io.IoUtils.closeQuietly(fd);
                    listener.onClose(status.asIOException());
                    return 0;
                }
                return android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT;
            }
        });
        return pfd;
    }

    private static java.io.FileDescriptor openInternal(java.io.File file, int mode) throws java.io.FileNotFoundException {
        if ((mode & android.os.ParcelFileDescriptor.MODE_READ_WRITE) == 0) {
            throw new java.lang.IllegalArgumentException("Must specify MODE_READ_ONLY, MODE_WRITE_ONLY, or MODE_READ_WRITE");
        }
        final java.lang.String path = file.getPath();
        return android.os.Parcel.openFileDescriptor(path, mode);
    }

    /**
     * Create a new ParcelFileDescriptor that is a dup of an existing
     * FileDescriptor.  This obeys standard POSIX semantics, where the
     * new file descriptor shared state such as file position with the
     * original file descriptor.
     */
    public static android.os.ParcelFileDescriptor dup(java.io.FileDescriptor orig) throws java.io.IOException {
        try {
            final java.io.FileDescriptor fd = android.system.Os.dup(orig);
            return new android.os.ParcelFileDescriptor(fd);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Create a new ParcelFileDescriptor that is a dup of the existing
     * FileDescriptor.  This obeys standard POSIX semantics, where the
     * new file descriptor shared state such as file position with the
     * original file descriptor.
     */
    public android.os.ParcelFileDescriptor dup() throws java.io.IOException {
        if (mWrapped != null) {
            return mWrapped.dup();
        } else {
            return android.os.ParcelFileDescriptor.dup(getFileDescriptor());
        }
    }

    /**
     * Create a new ParcelFileDescriptor from a raw native fd.  The new
     * ParcelFileDescriptor holds a dup of the original fd passed in here,
     * so you must still close that fd as well as the new ParcelFileDescriptor.
     *
     * @param fd
     * 		The native fd that the ParcelFileDescriptor should dup.
     * @return Returns a new ParcelFileDescriptor holding a FileDescriptor
    for a dup of the given fd.
     */
    public static android.os.ParcelFileDescriptor fromFd(int fd) throws java.io.IOException {
        final java.io.FileDescriptor original = new java.io.FileDescriptor();
        original.setInt$(fd);
        try {
            final java.io.FileDescriptor dup = android.system.Os.dup(original);
            return new android.os.ParcelFileDescriptor(dup);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Take ownership of a raw native fd in to a new ParcelFileDescriptor.
     * The returned ParcelFileDescriptor now owns the given fd, and will be
     * responsible for closing it.  You must not close the fd yourself.
     *
     * @param fd
     * 		The native fd that the ParcelFileDescriptor should adopt.
     * @return Returns a new ParcelFileDescriptor holding a FileDescriptor
    for the given fd.
     */
    public static android.os.ParcelFileDescriptor adoptFd(int fd) {
        final java.io.FileDescriptor fdesc = new java.io.FileDescriptor();
        fdesc.setInt$(fd);
        return new android.os.ParcelFileDescriptor(fdesc);
    }

    /**
     * Create a new ParcelFileDescriptor from the specified Socket.  The new
     * ParcelFileDescriptor holds a dup of the original FileDescriptor in
     * the Socket, so you must still close the Socket as well as the new
     * ParcelFileDescriptor.
     *
     * @param socket
     * 		The Socket whose FileDescriptor is used to create
     * 		a new ParcelFileDescriptor.
     * @return A new ParcelFileDescriptor with the FileDescriptor of the
    specified Socket.
     */
    public static android.os.ParcelFileDescriptor fromSocket(java.net.Socket socket) {
        java.io.FileDescriptor fd = socket.getFileDescriptor$();
        return fd != null ? new android.os.ParcelFileDescriptor(fd) : null;
    }

    /**
     * Create a new ParcelFileDescriptor from the specified DatagramSocket.
     *
     * @param datagramSocket
     * 		The DatagramSocket whose FileDescriptor is used
     * 		to create a new ParcelFileDescriptor.
     * @return A new ParcelFileDescriptor with the FileDescriptor of the
    specified DatagramSocket.
     */
    public static android.os.ParcelFileDescriptor fromDatagramSocket(java.net.DatagramSocket datagramSocket) {
        java.io.FileDescriptor fd = datagramSocket.getFileDescriptor$();
        return fd != null ? new android.os.ParcelFileDescriptor(fd) : null;
    }

    /**
     * Create two ParcelFileDescriptors structured as a data pipe.  The first
     * ParcelFileDescriptor in the returned array is the read side; the second
     * is the write side.
     */
    public static android.os.ParcelFileDescriptor[] createPipe() throws java.io.IOException {
        try {
            final java.io.FileDescriptor[] fds = android.system.Os.pipe();
            return new android.os.ParcelFileDescriptor[]{ new android.os.ParcelFileDescriptor(fds[0]), new android.os.ParcelFileDescriptor(fds[1]) };
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Create two ParcelFileDescriptors structured as a data pipe. The first
     * ParcelFileDescriptor in the returned array is the read side; the second
     * is the write side.
     * <p>
     * The write end has the ability to deliver an error message through
     * {@link #closeWithError(String)} which can be handled by the read end
     * calling {@link #checkError()}, usually after detecting an EOF.
     * This can also be used to detect remote crashes.
     */
    public static android.os.ParcelFileDescriptor[] createReliablePipe() throws java.io.IOException {
        try {
            final java.io.FileDescriptor[] comm = android.os.ParcelFileDescriptor.createCommSocketPair();
            final java.io.FileDescriptor[] fds = android.system.Os.pipe();
            return new android.os.ParcelFileDescriptor[]{ new android.os.ParcelFileDescriptor(fds[0], comm[0]), new android.os.ParcelFileDescriptor(fds[1], comm[1]) };
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Create two ParcelFileDescriptors structured as a pair of sockets
     * connected to each other. The two sockets are indistinguishable.
     */
    public static android.os.ParcelFileDescriptor[] createSocketPair() throws java.io.IOException {
        return android.os.ParcelFileDescriptor.createSocketPair(android.system.OsConstants.SOCK_STREAM);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.os.ParcelFileDescriptor[] createSocketPair(int type) throws java.io.IOException {
        try {
            final java.io.FileDescriptor fd0 = new java.io.FileDescriptor();
            final java.io.FileDescriptor fd1 = new java.io.FileDescriptor();
            android.system.Os.socketpair(android.system.OsConstants.AF_UNIX, type, 0, fd0, fd1);
            return new android.os.ParcelFileDescriptor[]{ new android.os.ParcelFileDescriptor(fd0), new android.os.ParcelFileDescriptor(fd1) };
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Create two ParcelFileDescriptors structured as a pair of sockets
     * connected to each other. The two sockets are indistinguishable.
     * <p>
     * Both ends have the ability to deliver an error message through
     * {@link #closeWithError(String)} which can be detected by the other end
     * calling {@link #checkError()}, usually after detecting an EOF.
     * This can also be used to detect remote crashes.
     */
    public static android.os.ParcelFileDescriptor[] createReliableSocketPair() throws java.io.IOException {
        return android.os.ParcelFileDescriptor.createReliableSocketPair(android.system.OsConstants.SOCK_STREAM);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.os.ParcelFileDescriptor[] createReliableSocketPair(int type) throws java.io.IOException {
        try {
            final java.io.FileDescriptor[] comm = android.os.ParcelFileDescriptor.createCommSocketPair();
            final java.io.FileDescriptor fd0 = new java.io.FileDescriptor();
            final java.io.FileDescriptor fd1 = new java.io.FileDescriptor();
            android.system.Os.socketpair(android.system.OsConstants.AF_UNIX, type, 0, fd0, fd1);
            return new android.os.ParcelFileDescriptor[]{ new android.os.ParcelFileDescriptor(fd0, comm[0]), new android.os.ParcelFileDescriptor(fd1, comm[1]) };
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private static java.io.FileDescriptor[] createCommSocketPair() throws java.io.IOException {
        try {
            // Use SOCK_SEQPACKET so that we have a guarantee that the status
            // is written and read atomically as one unit and is not split
            // across multiple IO operations.
            final java.io.FileDescriptor comm1 = new java.io.FileDescriptor();
            final java.io.FileDescriptor comm2 = new java.io.FileDescriptor();
            android.system.Os.socketpair(android.system.OsConstants.AF_UNIX, android.system.OsConstants.SOCK_SEQPACKET, 0, comm1, comm2);
            libcore.io.IoUtils.setBlocking(comm1, false);
            libcore.io.IoUtils.setBlocking(comm2, false);
            return new java.io.FileDescriptor[]{ comm1, comm2 };
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     *
     *
     * @unknown Please use createPipe() or ContentProvider.openPipeHelper().
    Gets a file descriptor for a read-only copy of the given data.
     * @param data
     * 		Data to copy.
     * @param name
     * 		Name for the shared memory area that may back the file descriptor.
     * 		This is purely informative and may be {@code null}.
     * @return A ParcelFileDescriptor.
     * @throws IOException
     * 		if there is an error while creating the shared memory area.
     */
    @java.lang.Deprecated
    public static android.os.ParcelFileDescriptor fromData(byte[] data, java.lang.String name) throws java.io.IOException {
        if (data == null)
            return null;

        android.os.MemoryFile file = new android.os.MemoryFile(name, data.length);
        if (data.length > 0) {
            file.writeBytes(data, 0, 0, data.length);
        }
        file.deactivate();
        java.io.FileDescriptor fd = file.getFileDescriptor();
        return fd != null ? new android.os.ParcelFileDescriptor(fd) : null;
    }

    /**
     * Converts a string representing a file mode, such as "rw", into a bitmask suitable for use
     * with {@link #open}.
     * <p>
     *
     * @param mode
     * 		The string representation of the file mode.
     * @return A bitmask representing the given file mode.
     * @throws IllegalArgumentException
     * 		if the given string does not match a known file mode.
     */
    public static int parseMode(java.lang.String mode) {
        final int modeBits;
        if ("r".equals(mode)) {
            modeBits = android.os.ParcelFileDescriptor.MODE_READ_ONLY;
        } else
            if ("w".equals(mode) || "wt".equals(mode)) {
                modeBits = (android.os.ParcelFileDescriptor.MODE_WRITE_ONLY | android.os.ParcelFileDescriptor.MODE_CREATE) | android.os.ParcelFileDescriptor.MODE_TRUNCATE;
            } else
                if ("wa".equals(mode)) {
                    modeBits = (android.os.ParcelFileDescriptor.MODE_WRITE_ONLY | android.os.ParcelFileDescriptor.MODE_CREATE) | android.os.ParcelFileDescriptor.MODE_APPEND;
                } else
                    if ("rw".equals(mode)) {
                        modeBits = android.os.ParcelFileDescriptor.MODE_READ_WRITE | android.os.ParcelFileDescriptor.MODE_CREATE;
                    } else
                        if ("rwt".equals(mode)) {
                            modeBits = (android.os.ParcelFileDescriptor.MODE_READ_WRITE | android.os.ParcelFileDescriptor.MODE_CREATE) | android.os.ParcelFileDescriptor.MODE_TRUNCATE;
                        } else {
                            throw new java.lang.IllegalArgumentException(("Bad mode '" + mode) + "'");
                        }




        return modeBits;
    }

    /**
     * Retrieve the actual FileDescriptor associated with this object.
     *
     * @return Returns the FileDescriptor associated with this object.
     */
    public java.io.FileDescriptor getFileDescriptor() {
        if (mWrapped != null) {
            return mWrapped.getFileDescriptor();
        } else {
            return mFd;
        }
    }

    /**
     * Return the total size of the file representing this fd, as determined by
     * {@code stat()}. Returns -1 if the fd is not a file.
     */
    public long getStatSize() {
        if (mWrapped != null) {
            return mWrapped.getStatSize();
        } else {
            try {
                final android.system.StructStat st = android.system.Os.fstat(mFd);
                if (android.system.OsConstants.S_ISREG(st.st_mode) || android.system.OsConstants.S_ISLNK(st.st_mode)) {
                    return st.st_size;
                } else {
                    return -1;
                }
            } catch (android.system.ErrnoException e) {
                android.util.Log.w(android.os.ParcelFileDescriptor.TAG, "fstat() failed: " + e);
                return -1;
            }
        }
    }

    /**
     * This is needed for implementing AssetFileDescriptor.AutoCloseOutputStream,
     * and I really don't think we want it to be public.
     *
     * @unknown 
     */
    public long seekTo(long pos) throws java.io.IOException {
        if (mWrapped != null) {
            return mWrapped.seekTo(pos);
        } else {
            try {
                return android.system.Os.lseek(mFd, pos, android.system.OsConstants.SEEK_SET);
            } catch (android.system.ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        }
    }

    /**
     * Return the native fd int for this ParcelFileDescriptor.  The
     * ParcelFileDescriptor still owns the fd, and it still must be closed
     * through this API.
     */
    public int getFd() {
        if (mWrapped != null) {
            return mWrapped.getFd();
        } else {
            if (mClosed) {
                throw new java.lang.IllegalStateException("Already closed");
            }
            return mFd.getInt$();
        }
    }

    /**
     * Return the native fd int for this ParcelFileDescriptor and detach it from
     * the object here. You are now responsible for closing the fd in native
     * code.
     * <p>
     * You should not detach when the original creator of the descriptor is
     * expecting a reliable signal through {@link #close()} or
     * {@link #closeWithError(String)}.
     *
     * @see #canDetectErrors()
     */
    public int detachFd() {
        if (mWrapped != null) {
            return mWrapped.detachFd();
        } else {
            if (mClosed) {
                throw new java.lang.IllegalStateException("Already closed");
            }
            final int fd = getFd();
            android.os.Parcel.clearFileDescriptor(mFd);
            writeCommStatusAndClose(android.os.ParcelFileDescriptor.Status.DETACHED, null);
            mClosed = true;
            mGuard.close();
            releaseResources();
            return fd;
        }
    }

    /**
     * Close the ParcelFileDescriptor. This implementation closes the underlying
     * OS resources allocated to represent this stream.
     *
     * @throws IOException
     * 		If an error occurs attempting to close this ParcelFileDescriptor.
     */
    @java.lang.Override
    public void close() throws java.io.IOException {
        if (mWrapped != null) {
            try {
                mWrapped.close();
            } finally {
                releaseResources();
            }
        } else {
            closeWithStatus(android.os.ParcelFileDescriptor.Status.OK, null);
        }
    }

    /**
     * Close the ParcelFileDescriptor, informing any peer that an error occurred
     * while processing. If the creator of this descriptor is not observing
     * errors, it will close normally.
     *
     * @param msg
     * 		describing the error; must not be null.
     */
    public void closeWithError(java.lang.String msg) throws java.io.IOException {
        if (mWrapped != null) {
            try {
                mWrapped.closeWithError(msg);
            } finally {
                releaseResources();
            }
        } else {
            if (msg == null) {
                throw new java.lang.IllegalArgumentException("Message must not be null");
            }
            closeWithStatus(android.os.ParcelFileDescriptor.Status.ERROR, msg);
        }
    }

    private void closeWithStatus(int status, java.lang.String msg) {
        if (mClosed)
            return;

        mClosed = true;
        mGuard.close();
        // Status MUST be sent before closing actual descriptor
        writeCommStatusAndClose(status, msg);
        libcore.io.IoUtils.closeQuietly(mFd);
        releaseResources();
    }

    /**
     * Called when the fd is being closed, for subclasses to release any other resources
     * associated with it, such as acquired providers.
     *
     * @unknown 
     */
    public void releaseResources() {
    }

    private byte[] getOrCreateStatusBuffer() {
        if (mStatusBuf == null) {
            mStatusBuf = new byte[android.os.ParcelFileDescriptor.MAX_STATUS];
        }
        return mStatusBuf;
    }

    private void writeCommStatusAndClose(int status, java.lang.String msg) {
        if (mCommFd == null) {
            // Not reliable, or someone already sent status
            if (msg != null) {
                android.util.Log.w(android.os.ParcelFileDescriptor.TAG, "Unable to inform peer: " + msg);
            }
            return;
        }
        if (status == android.os.ParcelFileDescriptor.Status.DETACHED) {
            android.util.Log.w(android.os.ParcelFileDescriptor.TAG, "Peer expected signal when closed; unable to deliver after detach");
        }
        try {
            if (status == android.os.ParcelFileDescriptor.Status.SILENCE)
                return;

            // Since we're about to close, read off any remote status. It's
            // okay to remember missing here.
            mStatus = android.os.ParcelFileDescriptor.readCommStatus(mCommFd, getOrCreateStatusBuffer());
            // Skip writing status when other end has already gone away.
            if (mStatus != null)
                return;

            try {
                final byte[] buf = getOrCreateStatusBuffer();
                int writePtr = 0;
                libcore.io.Memory.pokeInt(buf, writePtr, status, java.nio.ByteOrder.BIG_ENDIAN);
                writePtr += 4;
                if (msg != null) {
                    final byte[] rawMsg = msg.getBytes();
                    final int len = java.lang.Math.min(rawMsg.length, buf.length - writePtr);
                    java.lang.System.arraycopy(rawMsg, 0, buf, writePtr, len);
                    writePtr += len;
                }
                // Must write the entire status as a single operation.
                android.system.Os.write(mCommFd, buf, 0, writePtr);
            } catch (android.system.ErrnoException e) {
                // Reporting status is best-effort
                android.util.Log.w(android.os.ParcelFileDescriptor.TAG, "Failed to report status: " + e);
            } catch (java.io.InterruptedIOException e) {
                // Reporting status is best-effort
                android.util.Log.w(android.os.ParcelFileDescriptor.TAG, "Failed to report status: " + e);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(mCommFd);
            mCommFd = null;
        }
    }

    private static android.os.ParcelFileDescriptor.Status readCommStatus(java.io.FileDescriptor comm, byte[] buf) {
        try {
            // Must read the entire status as a single operation.
            final int n = android.system.Os.read(comm, buf, 0, buf.length);
            if (n == 0) {
                // EOF means they're dead
                return new android.os.ParcelFileDescriptor.Status(android.os.ParcelFileDescriptor.Status.DEAD);
            } else {
                final int status = libcore.io.Memory.peekInt(buf, 0, java.nio.ByteOrder.BIG_ENDIAN);
                if (status == android.os.ParcelFileDescriptor.Status.ERROR) {
                    final java.lang.String msg = new java.lang.String(buf, 4, n - 4);
                    return new android.os.ParcelFileDescriptor.Status(status, msg);
                }
                return new android.os.ParcelFileDescriptor.Status(status);
            }
        } catch (android.system.ErrnoException e) {
            if (e.errno == android.system.OsConstants.EAGAIN) {
                // Remote is still alive, but no status written yet
                return null;
            } else {
                android.util.Log.d(android.os.ParcelFileDescriptor.TAG, "Failed to read status; assuming dead: " + e);
                return new android.os.ParcelFileDescriptor.Status(android.os.ParcelFileDescriptor.Status.DEAD);
            }
        } catch (java.io.InterruptedIOException e) {
            android.util.Log.d(android.os.ParcelFileDescriptor.TAG, "Failed to read status; assuming dead: " + e);
            return new android.os.ParcelFileDescriptor.Status(android.os.ParcelFileDescriptor.Status.DEAD);
        }
    }

    /**
     * Indicates if this ParcelFileDescriptor can communicate and detect remote
     * errors/crashes.
     *
     * @see #checkError()
     */
    public boolean canDetectErrors() {
        if (mWrapped != null) {
            return mWrapped.canDetectErrors();
        } else {
            return mCommFd != null;
        }
    }

    /**
     * Detect and throw if the other end of a pipe or socket pair encountered an
     * error or crashed. This allows a reader to distinguish between a valid EOF
     * and an error/crash.
     * <p>
     * If this ParcelFileDescriptor is unable to detect remote errors, it will
     * return silently.
     *
     * @throws IOException
     * 		for normal errors.
     * @throws FileDescriptorDetachedException
     * 		if the remote side called {@link #detachFd()}. Once detached, the remote
     * 		side is unable to communicate any errors through
     * 		{@link #closeWithError(String)}.
     * @see #canDetectErrors()
     */
    public void checkError() throws java.io.IOException {
        if (mWrapped != null) {
            mWrapped.checkError();
        } else {
            if (mStatus == null) {
                if (mCommFd == null) {
                    android.util.Log.w(android.os.ParcelFileDescriptor.TAG, "Peer didn't provide a comm channel; unable to check for errors");
                    return;
                }
                // Try reading status; it might be null if nothing written yet.
                // Either way, we keep comm open to write our status later.
                mStatus = android.os.ParcelFileDescriptor.readCommStatus(mCommFd, getOrCreateStatusBuffer());
            }
            if ((mStatus == null) || (mStatus.status == android.os.ParcelFileDescriptor.Status.OK)) {
                // No status yet, or everything is peachy!
                return;
            } else {
                throw mStatus.asIOException();
            }
        }
    }

    /**
     * An InputStream you can create on a ParcelFileDescriptor, which will
     * take care of calling {@link ParcelFileDescriptor#close
     * ParcelFileDescriptor.close()} for you when the stream is closed.
     */
    public static class AutoCloseInputStream extends java.io.FileInputStream {
        private final android.os.ParcelFileDescriptor mPfd;

        public AutoCloseInputStream(android.os.ParcelFileDescriptor pfd) {
            super(pfd.getFileDescriptor());
            mPfd = pfd;
        }

        @java.lang.Override
        public void close() throws java.io.IOException {
            try {
                mPfd.close();
            } finally {
                super.close();
            }
        }

        @java.lang.Override
        public int read() throws java.io.IOException {
            final int result = super.read();
            if ((result == (-1)) && mPfd.canDetectErrors()) {
                // Check for errors only on EOF, to minimize overhead.
                mPfd.checkError();
            }
            return result;
        }

        @java.lang.Override
        public int read(byte[] b) throws java.io.IOException {
            final int result = super.read(b);
            if ((result == (-1)) && mPfd.canDetectErrors()) {
                mPfd.checkError();
            }
            return result;
        }

        @java.lang.Override
        public int read(byte[] b, int off, int len) throws java.io.IOException {
            final int result = super.read(b, off, len);
            if ((result == (-1)) && mPfd.canDetectErrors()) {
                mPfd.checkError();
            }
            return result;
        }
    }

    /**
     * An OutputStream you can create on a ParcelFileDescriptor, which will
     * take care of calling {@link ParcelFileDescriptor#close
     * ParcelFileDescriptor.close()} for you when the stream is closed.
     */
    public static class AutoCloseOutputStream extends java.io.FileOutputStream {
        private final android.os.ParcelFileDescriptor mPfd;

        public AutoCloseOutputStream(android.os.ParcelFileDescriptor pfd) {
            super(pfd.getFileDescriptor());
            mPfd = pfd;
        }

        @java.lang.Override
        public void close() throws java.io.IOException {
            try {
                mPfd.close();
            } finally {
                super.close();
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (mWrapped != null) {
            return mWrapped.toString();
        } else {
            return ("{ParcelFileDescriptor: " + mFd) + "}";
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        if (mWrapped != null) {
            releaseResources();
        }
        if (mGuard != null) {
            mGuard.warnIfOpen();
        }
        try {
            if (!mClosed) {
                closeWithStatus(android.os.ParcelFileDescriptor.Status.LEAKED, null);
            }
        } finally {
            super.finalize();
        }
    }

    @java.lang.Override
    public int describeContents() {
        if (mWrapped != null) {
            return mWrapped.describeContents();
        } else {
            return android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;
        }
    }

    /**
     * {@inheritDoc }
     * If {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE} is set in flags,
     * the file descriptor will be closed after a copy is written to the Parcel.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        if (mWrapped != null) {
            try {
                mWrapped.writeToParcel(out, flags);
            } finally {
                releaseResources();
            }
        } else {
            if (mCommFd != null) {
                out.writeInt(1);
                out.writeFileDescriptor(mFd);
                out.writeFileDescriptor(mCommFd);
            } else {
                out.writeInt(0);
                out.writeFileDescriptor(mFd);
            }
            if (((flags & android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE) != 0) && (!mClosed)) {
                // Not a real close, so emit no status
                closeWithStatus(android.os.ParcelFileDescriptor.Status.SILENCE, null);
            }
        }
    }

    public static final android.os.Parcelable.Creator<android.os.ParcelFileDescriptor> CREATOR = new android.os.Parcelable.Creator<android.os.ParcelFileDescriptor>() {
        @java.lang.Override
        public android.os.ParcelFileDescriptor createFromParcel(android.os.Parcel in) {
            int hasCommChannel = in.readInt();
            final java.io.FileDescriptor fd = in.readRawFileDescriptor();
            java.io.FileDescriptor commChannel = null;
            if (hasCommChannel != 0) {
                commChannel = in.readRawFileDescriptor();
            }
            return new android.os.ParcelFileDescriptor(fd, commChannel);
        }

        @java.lang.Override
        public android.os.ParcelFileDescriptor[] newArray(int size) {
            return new android.os.ParcelFileDescriptor[size];
        }
    };

    /**
     * Callback indicating that a ParcelFileDescriptor has been closed.
     */
    public interface OnCloseListener {
        /**
         * Event indicating the ParcelFileDescriptor to which this listener was
         * attached has been closed.
         *
         * @param e
         * 		error state, or {@code null} if closed cleanly.
         * 		If the close event was the result of
         * 		{@link ParcelFileDescriptor#detachFd()}, this will be a
         * 		{@link FileDescriptorDetachedException}. After detach the
         * 		remote side may continue reading/writing to the underlying
         * 		{@link FileDescriptor}, but they can no longer deliver
         * 		reliable close/error events.
         */
        public void onClose(java.io.IOException e);
    }

    /**
     * Exception that indicates that the file descriptor was detached.
     */
    public static class FileDescriptorDetachedException extends java.io.IOException {
        private static final long serialVersionUID = 0xde7ac4edfdL;

        public FileDescriptorDetachedException() {
            super("Remote side is detached");
        }
    }

    /**
     * Internal class representing a remote status read by
     * {@link ParcelFileDescriptor#readCommStatus(FileDescriptor, byte[])}.
     */
    private static class Status {
        /**
         * Special value indicating remote side died.
         */
        public static final int DEAD = -2;

        /**
         * Special value indicating no status should be written.
         */
        public static final int SILENCE = -1;

        /**
         * Remote reported that everything went better than expected.
         */
        public static final int OK = 0;

        /**
         * Remote reported error; length and message follow.
         */
        public static final int ERROR = 1;

        /**
         * Remote reported {@link #detachFd()} and went rogue.
         */
        public static final int DETACHED = 2;

        /**
         * Remote reported their object was finalized.
         */
        public static final int LEAKED = 3;

        public final int status;

        public final java.lang.String msg;

        public Status(int status) {
            this(status, null);
        }

        public Status(int status, java.lang.String msg) {
            this.status = status;
            this.msg = msg;
        }

        public java.io.IOException asIOException() {
            switch (status) {
                case android.os.ParcelFileDescriptor.Status.DEAD :
                    return new java.io.IOException("Remote side is dead");
                case android.os.ParcelFileDescriptor.Status.OK :
                    return null;
                case android.os.ParcelFileDescriptor.Status.ERROR :
                    return new java.io.IOException("Remote error: " + msg);
                case android.os.ParcelFileDescriptor.Status.DETACHED :
                    return new android.os.ParcelFileDescriptor.FileDescriptorDetachedException();
                case android.os.ParcelFileDescriptor.Status.LEAKED :
                    return new java.io.IOException("Remote side was leaked");
                default :
                    return new java.io.IOException("Unknown status: " + status);
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("{" + status) + ": ") + msg) + "}";
        }
    }
}

