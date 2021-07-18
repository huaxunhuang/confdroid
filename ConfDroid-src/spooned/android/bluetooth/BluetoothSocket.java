/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.bluetooth;


/**
 * A connected or connecting Bluetooth socket.
 *
 * <p>The interface for Bluetooth Sockets is similar to that of TCP sockets:
 * {@link java.net.Socket} and {@link java.net.ServerSocket}. On the server
 * side, use a {@link BluetoothServerSocket} to create a listening server
 * socket. When a connection is accepted by the {@link BluetoothServerSocket},
 * it will return a new {@link BluetoothSocket} to manage the connection.
 * On the client side, use a single {@link BluetoothSocket} to both initiate
 * an outgoing connection and to manage the connection.
 *
 * <p>The most common type of Bluetooth socket is RFCOMM, which is the type
 * supported by the Android APIs. RFCOMM is a connection-oriented, streaming
 * transport over Bluetooth. It is also known as the Serial Port Profile (SPP).
 *
 * <p>To create a {@link BluetoothSocket} for connecting to a known device, use
 * {@link BluetoothDevice#createRfcommSocketToServiceRecord
 * BluetoothDevice.createRfcommSocketToServiceRecord()}.
 * Then call {@link #connect()} to attempt a connection to the remote device.
 * This call will block until a connection is established or the connection
 * fails.
 *
 * <p>To create a {@link BluetoothSocket} as a server (or "host"), see the
 * {@link BluetoothServerSocket} documentation.
 *
 * <p>Once the socket is connected, whether initiated as a client or accepted
 * as a server, open the IO streams by calling {@link #getInputStream} and
 * {@link #getOutputStream} in order to retrieve {@link java.io.InputStream}
 * and {@link java.io.OutputStream} objects, respectively, which are
 * automatically connected to the socket.
 *
 * <p>{@link BluetoothSocket} is thread
 * safe. In particular, {@link #close} will always immediately abort ongoing
 * operations and close the socket.
 *
 * <p class="note"><strong>Note:</strong>
 * Requires the {@link android.Manifest.permission#BLUETOOTH} permission.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using Bluetooth, read the
 * <a href="{@docRoot }guide/topics/wireless/bluetooth.html">Bluetooth</a> developer guide.</p>
 * </div>
 *
 * {@see BluetoothServerSocket}
 * {@see java.io.InputStream}
 * {@see java.io.OutputStream}
 */
public final class BluetoothSocket implements java.io.Closeable {
    private static final java.lang.String TAG = "BluetoothSocket";

    private static final boolean DBG = android.util.Log.isLoggable(android.bluetooth.BluetoothSocket.TAG, android.util.Log.DEBUG);

    private static final boolean VDBG = android.util.Log.isLoggable(android.bluetooth.BluetoothSocket.TAG, android.util.Log.VERBOSE);

    /**
     *
     *
     * @unknown 
     */
    public static final int MAX_RFCOMM_CHANNEL = 30;

    /* package */
    static final int MAX_L2CAP_PACKAGE_SIZE = 0xffff;

    /**
     * RFCOMM socket
     */
    public static final int TYPE_RFCOMM = 1;

    /**
     * SCO socket
     */
    public static final int TYPE_SCO = 2;

    /**
     * L2CAP socket
     */
    public static final int TYPE_L2CAP = 3;

    /* package */
    static final int EBADFD = 77;

    /* package */
    static final int EADDRINUSE = 98;

    /* package */
    static final int SEC_FLAG_ENCRYPT = 1;

    /* package */
    static final int SEC_FLAG_AUTH = 1 << 1;

    /* package */
    static final int BTSOCK_FLAG_NO_SDP = 1 << 2;

    /* package */
    static final int SEC_FLAG_AUTH_MITM = 1 << 3;

    /* package */
    static final int SEC_FLAG_AUTH_16_DIGIT = 1 << 4;

    private final int mType;/* one of TYPE_RFCOMM etc */


    private android.bluetooth.BluetoothDevice mDevice;/* remote device */


    private java.lang.String mAddress;/* remote address */


    private final boolean mAuth;

    private final boolean mEncrypt;

    private final android.bluetooth.BluetoothInputStream mInputStream;

    private final android.bluetooth.BluetoothOutputStream mOutputStream;

    private final android.os.ParcelUuid mUuid;

    private boolean mExcludeSdp = false;/* when true no SPP SDP record will be created */


    private boolean mAuthMitm = false;/* when true Man-in-the-middle protection will be enabled */


    private boolean mMin16DigitPin = false;/* Minimum 16 digit pin for sec mode 2 connections */


    private android.os.ParcelFileDescriptor mPfd;

    private android.net.LocalSocket mSocket;

    private java.io.InputStream mSocketIS;

    private java.io.OutputStream mSocketOS;

    private int mPort;/* RFCOMM channel or L2CAP psm */


    private int mFd;

    private java.lang.String mServiceName;

    private static int PROXY_CONNECTION_TIMEOUT = 5000;

    private static int SOCK_SIGNAL_SIZE = 20;

    private java.nio.ByteBuffer mL2capBuffer = null;

    private int mMaxTxPacketSize = 0;// The l2cap maximum packet size supported by the peer.


    private int mMaxRxPacketSize = 0;// The l2cap maximum packet size that can be received.


    private enum SocketState {

        INIT,
        CONNECTED,
        LISTENING,
        CLOSED;}

    /**
     * prevents all native calls after destroyNative()
     */
    private volatile android.bluetooth.BluetoothSocket.SocketState mSocketState;

    /**
     * protects mSocketState
     */
    // private final ReentrantReadWriteLock mLock;
    /**
     * Construct a BluetoothSocket.
     *
     * @param type
     * 		type of socket
     * @param fd
     * 		fd to use for connected socket, or -1 for a new socket
     * @param auth
     * 		require the remote device to be authenticated
     * @param encrypt
     * 		require the connection to be encrypted
     * @param device
     * 		remote device that this socket can connect to
     * @param port
     * 		remote port
     * @param uuid
     * 		SDP uuid
     * @throws IOException
     * 		On error, for example Bluetooth not available, or
     * 		insufficient privileges
     */
    /* package */
    BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, android.bluetooth.BluetoothDevice device, int port, android.os.ParcelUuid uuid) throws java.io.IOException {
        this(type, fd, auth, encrypt, device, port, uuid, false, false);
    }

    /**
     * Construct a BluetoothSocket.
     *
     * @param type
     * 		type of socket
     * @param fd
     * 		fd to use for connected socket, or -1 for a new socket
     * @param auth
     * 		require the remote device to be authenticated
     * @param encrypt
     * 		require the connection to be encrypted
     * @param device
     * 		remote device that this socket can connect to
     * @param port
     * 		remote port
     * @param uuid
     * 		SDP uuid
     * @param mitm
     * 		enforce man-in-the-middle protection.
     * @param min16DigitPin
     * 		enforce a minimum length of 16 digits for a sec mode 2 connection
     * @throws IOException
     * 		On error, for example Bluetooth not available, or
     * 		insufficient privileges
     */
    /* package */
    BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, android.bluetooth.BluetoothDevice device, int port, android.os.ParcelUuid uuid, boolean mitm, boolean min16DigitPin) throws java.io.IOException {
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "Creating new BluetoothSocket of type: " + type);

        if ((((type == android.bluetooth.BluetoothSocket.TYPE_RFCOMM) && (uuid == null)) && (fd == (-1))) && (port != android.bluetooth.BluetoothAdapter.SOCKET_CHANNEL_AUTO_STATIC_NO_SDP)) {
            if ((port < 1) || (port > android.bluetooth.BluetoothSocket.MAX_RFCOMM_CHANNEL)) {
                throw new java.io.IOException("Invalid RFCOMM channel: " + port);
            }
        }
        if (uuid != null)
            mUuid = uuid;
        else
            mUuid = new android.os.ParcelUuid(new java.util.UUID(0, 0));

        mType = type;
        mAuth = auth;
        mAuthMitm = mitm;
        mMin16DigitPin = min16DigitPin;
        mEncrypt = encrypt;
        mDevice = device;
        mPort = port;
        mFd = fd;
        mSocketState = android.bluetooth.BluetoothSocket.SocketState.INIT;
        if (device == null) {
            // Server socket
            mAddress = android.bluetooth.BluetoothAdapter.getDefaultAdapter().getAddress();
        } else {
            // Remote socket
            mAddress = device.getAddress();
        }
        mInputStream = new android.bluetooth.BluetoothInputStream(this);
        mOutputStream = new android.bluetooth.BluetoothOutputStream(this);
    }

    private BluetoothSocket(android.bluetooth.BluetoothSocket s) {
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "Creating new Private BluetoothSocket of type: " + s.mType);

        mUuid = s.mUuid;
        mType = s.mType;
        mAuth = s.mAuth;
        mEncrypt = s.mEncrypt;
        mPort = s.mPort;
        mInputStream = new android.bluetooth.BluetoothInputStream(this);
        mOutputStream = new android.bluetooth.BluetoothOutputStream(this);
        mMaxRxPacketSize = s.mMaxRxPacketSize;
        mMaxTxPacketSize = s.mMaxTxPacketSize;
        mServiceName = s.mServiceName;
        mExcludeSdp = s.mExcludeSdp;
        mAuthMitm = s.mAuthMitm;
        mMin16DigitPin = s.mMin16DigitPin;
    }

    private android.bluetooth.BluetoothSocket acceptSocket(java.lang.String RemoteAddr) throws java.io.IOException {
        android.bluetooth.BluetoothSocket as = new android.bluetooth.BluetoothSocket(this);
        as.mSocketState = android.bluetooth.BluetoothSocket.SocketState.CONNECTED;
        java.io.FileDescriptor[] fds = mSocket.getAncillaryFileDescriptors();
        if (android.bluetooth.BluetoothSocket.DBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "socket fd passed by stack fds: " + java.util.Arrays.toString(fds));

        if ((fds == null) || (fds.length != 1)) {
            android.util.Log.e(android.bluetooth.BluetoothSocket.TAG, "socket fd passed from stack failed, fds: " + java.util.Arrays.toString(fds));
            as.close();
            throw new java.io.IOException("bt socket acept failed");
        }
        as.mPfd = new android.os.ParcelFileDescriptor(fds[0]);
        as.mSocket = new android.net.LocalSocket(fds[0]);
        as.mSocketIS = as.mSocket.getInputStream();
        as.mSocketOS = as.mSocket.getOutputStream();
        as.mAddress = RemoteAddr;
        as.mDevice = android.bluetooth.BluetoothAdapter.getDefaultAdapter().getRemoteDevice(RemoteAddr);
        return as;
    }

    /**
     * Construct a BluetoothSocket from address. Used by native code.
     *
     * @param type
     * 		type of socket
     * @param fd
     * 		fd to use for connected socket, or -1 for a new socket
     * @param auth
     * 		require the remote device to be authenticated
     * @param encrypt
     * 		require the connection to be encrypted
     * @param address
     * 		remote device that this socket can connect to
     * @param port
     * 		remote port
     * @throws IOException
     * 		On error, for example Bluetooth not available, or
     * 		insufficient privileges
     */
    private BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, java.lang.String address, int port) throws java.io.IOException {
        this(type, fd, auth, encrypt, new android.bluetooth.BluetoothDevice(address), port, null, false, false);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private int getSecurityFlags() {
        int flags = 0;
        if (mAuth)
            flags |= android.bluetooth.BluetoothSocket.SEC_FLAG_AUTH;

        if (mEncrypt)
            flags |= android.bluetooth.BluetoothSocket.SEC_FLAG_ENCRYPT;

        if (mExcludeSdp)
            flags |= android.bluetooth.BluetoothSocket.BTSOCK_FLAG_NO_SDP;

        if (mAuthMitm)
            flags |= android.bluetooth.BluetoothSocket.SEC_FLAG_AUTH_MITM;

        if (mMin16DigitPin)
            flags |= android.bluetooth.BluetoothSocket.SEC_FLAG_AUTH_16_DIGIT;

        return flags;
    }

    /**
     * Get the remote device this socket is connecting, or connected, to.
     *
     * @return remote device
     */
    public android.bluetooth.BluetoothDevice getRemoteDevice() {
        return mDevice;
    }

    /**
     * Get the input stream associated with this socket.
     * <p>The input stream will be returned even if the socket is not yet
     * connected, but operations on that stream will throw IOException until
     * the associated socket is connected.
     *
     * @return InputStream
     */
    public java.io.InputStream getInputStream() throws java.io.IOException {
        return mInputStream;
    }

    /**
     * Get the output stream associated with this socket.
     * <p>The output stream will be returned even if the socket is not yet
     * connected, but operations on that stream will throw IOException until
     * the associated socket is connected.
     *
     * @return OutputStream
     */
    public java.io.OutputStream getOutputStream() throws java.io.IOException {
        return mOutputStream;
    }

    /**
     * Get the connection status of this socket, ie, whether there is an active connection with
     * remote device.
     *
     * @return true if connected
    false if not connected
     */
    public boolean isConnected() {
        return mSocketState == android.bluetooth.BluetoothSocket.SocketState.CONNECTED;
    }

    /* package */
    void setServiceName(java.lang.String name) {
        mServiceName = name;
    }

    /**
     * Attempt to connect to a remote device.
     * <p>This method will block until a connection is made or the connection
     * fails. If this method returns without an exception then this socket
     * is now connected.
     * <p>Creating new connections to
     * remote Bluetooth devices should not be attempted while device discovery
     * is in progress. Device discovery is a heavyweight procedure on the
     * Bluetooth adapter and will significantly slow a device connection.
     * Use {@link BluetoothAdapter#cancelDiscovery()} to cancel an ongoing
     * discovery. Discovery is not managed by the Activity,
     * but is run as a system service, so an application should always call
     * {@link BluetoothAdapter#cancelDiscovery()} even if it
     * did not directly request a discovery, just to be sure.
     * <p>{@link #close} can be used to abort this call from another thread.
     *
     * @throws IOException
     * 		on error, for example connection failure
     */
    public void connect() throws java.io.IOException {
        if (mDevice == null)
            throw new java.io.IOException("Connect is called on null device");

        try {
            if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.CLOSED)
                throw new java.io.IOException("socket closed");

            android.bluetooth.IBluetooth bluetoothProxy = android.bluetooth.BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
            if (bluetoothProxy == null)
                throw new java.io.IOException("Bluetooth is off");

            mPfd = bluetoothProxy.connectSocket(mDevice, mType, mUuid, mPort, getSecurityFlags());
            synchronized(this) {
                if (android.bluetooth.BluetoothSocket.DBG)
                    android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("connect(), SocketState: " + mSocketState) + ", mPfd: ") + mPfd);

                if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.CLOSED)
                    throw new java.io.IOException("socket closed");

                if (mPfd == null)
                    throw new java.io.IOException("bt socket connect failed");

                java.io.FileDescriptor fd = mPfd.getFileDescriptor();
                mSocket = new android.net.LocalSocket(fd);
                mSocketIS = mSocket.getInputStream();
                mSocketOS = mSocket.getOutputStream();
            }
            int channel = readInt(mSocketIS);
            if (channel <= 0)
                throw new java.io.IOException("bt socket connect failed");

            mPort = channel;
            waitSocketSignal(mSocketIS);
            synchronized(this) {
                if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.CLOSED)
                    throw new java.io.IOException("bt socket closed");

                mSocketState = android.bluetooth.BluetoothSocket.SocketState.CONNECTED;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothSocket.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            throw new java.io.IOException("unable to send RPC: " + e.getMessage());
        }
    }

    /**
     * Currently returns unix errno instead of throwing IOException,
     * so that BluetoothAdapter can check the error code for EADDRINUSE
     */
    /* package */
    int bindListen() {
        int ret;
        if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.CLOSED)
            return android.bluetooth.BluetoothSocket.EBADFD;

        android.bluetooth.IBluetooth bluetoothProxy = android.bluetooth.BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
        if (bluetoothProxy == null) {
            android.util.Log.e(android.bluetooth.BluetoothSocket.TAG, "bindListen fail, reason: bluetooth is off");
            return -1;
        }
        try {
            mPfd = bluetoothProxy.createSocketChannel(mType, mServiceName, mUuid, mPort, getSecurityFlags());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothSocket.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return -1;
        }
        // read out port number
        try {
            synchronized(this) {
                if (android.bluetooth.BluetoothSocket.DBG)
                    android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("bindListen(), SocketState: " + mSocketState) + ", mPfd: ") + mPfd);

                if (mSocketState != android.bluetooth.BluetoothSocket.SocketState.INIT)
                    return android.bluetooth.BluetoothSocket.EBADFD;

                if (mPfd == null)
                    return -1;

                java.io.FileDescriptor fd = mPfd.getFileDescriptor();
                if (android.bluetooth.BluetoothSocket.DBG)
                    android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "bindListen(), new LocalSocket ");

                mSocket = new android.net.LocalSocket(fd);
                if (android.bluetooth.BluetoothSocket.DBG)
                    android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "bindListen(), new LocalSocket.getInputStream() ");

                mSocketIS = mSocket.getInputStream();
                mSocketOS = mSocket.getOutputStream();
            }
            if (android.bluetooth.BluetoothSocket.DBG)
                android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "bindListen(), readInt mSocketIS: " + mSocketIS);

            int channel = readInt(mSocketIS);
            synchronized(this) {
                if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.INIT)
                    mSocketState = android.bluetooth.BluetoothSocket.SocketState.LISTENING;

            }
            if (android.bluetooth.BluetoothSocket.DBG)
                android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "channel: " + channel);

            if (mPort <= (-1)) {
                mPort = channel;
            }// else ASSERT(mPort == channel)

            ret = 0;
        } catch (java.io.IOException e) {
            if (mPfd != null) {
                try {
                    mPfd.close();
                } catch (java.io.IOException e1) {
                    android.util.Log.e(android.bluetooth.BluetoothSocket.TAG, "bindListen, close mPfd: " + e1);
                }
                mPfd = null;
            }
            android.util.Log.e(android.bluetooth.BluetoothSocket.TAG, "bindListen, fail to get port number, exception: " + e);
            return -1;
        }
        return ret;
    }

    /* package */
    android.bluetooth.BluetoothSocket accept(int timeout) throws java.io.IOException {
        android.bluetooth.BluetoothSocket acceptedSocket;
        if (mSocketState != android.bluetooth.BluetoothSocket.SocketState.LISTENING)
            throw new java.io.IOException("bt socket is not in listen state");

        if (timeout > 0) {
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "accept() set timeout (ms):" + timeout);
            mSocket.setSoTimeout(timeout);
        }
        java.lang.String RemoteAddr = waitSocketSignal(mSocketIS);
        if (timeout > 0)
            mSocket.setSoTimeout(0);

        synchronized(this) {
            if (mSocketState != android.bluetooth.BluetoothSocket.SocketState.LISTENING)
                throw new java.io.IOException("bt socket is not in listen state");

            acceptedSocket = acceptSocket(RemoteAddr);
            // quick drop the reference of the file handle
        }
        return acceptedSocket;
    }

    /* package */
    int available() throws java.io.IOException {
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "available: " + mSocketIS);

        return mSocketIS.available();
    }

    /**
     * Wait until the data in sending queue is emptied. A polling version
     * for flush implementation. Used to ensure the writing data afterwards will
     * be packed in new RFCOMM frame.
     *
     * @throws IOException
     * 		if an i/o error occurs.
     */
    /* package */
    void flush() throws java.io.IOException {
        if (mSocketOS == null)
            throw new java.io.IOException("flush is called on null OutputStream");

        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "flush: " + mSocketOS);

        mSocketOS.flush();
    }

    /* package */
    int read(byte[] b, int offset, int length) throws java.io.IOException {
        int ret = 0;
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("read in:  " + mSocketIS) + " len: ") + length);

        if (mType == android.bluetooth.BluetoothSocket.TYPE_L2CAP) {
            int bytesToRead = length;
            if (android.bluetooth.BluetoothSocket.VDBG)
                android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, (((("l2cap: read(): offset: " + offset) + " length:") + length) + "mL2capBuffer= ") + mL2capBuffer);

            if (mL2capBuffer == null) {
                createL2capRxBuffer();
            }
            if (mL2capBuffer.remaining() == 0) {
                if (android.bluetooth.BluetoothSocket.VDBG)
                    android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, "l2cap buffer empty, refilling...");

                if (fillL2capRxBuffer() == (-1)) {
                    return -1;
                }
            }
            if (bytesToRead > mL2capBuffer.remaining()) {
                bytesToRead = mL2capBuffer.remaining();
            }
            if (android.bluetooth.BluetoothSocket.VDBG)
                android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, (("get(): offset: " + offset) + " bytesToRead: ") + bytesToRead);

            mL2capBuffer.get(b, offset, bytesToRead);
            ret = bytesToRead;
        } else {
            if (android.bluetooth.BluetoothSocket.VDBG)
                android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, (("default: read(): offset: " + offset) + " length:") + length);

            ret = mSocketIS.read(b, offset, length);
        }
        if (ret < 0)
            throw new java.io.IOException("bt socket closed, read return: " + ret);

        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("read out:  " + mSocketIS) + " ret: ") + ret);

        return ret;
    }

    /* package */
    int write(byte[] b, int offset, int length) throws java.io.IOException {
        // TODO: Since bindings can exist between the SDU size and the
        // protocol, we might need to throw an exception instead of just
        // splitting the write into multiple smaller writes.
        // Rfcomm uses dynamic allocation, and should not have any bindings
        // to the actual message length.
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("write: " + mSocketOS) + " length: ") + length);

        if (mType == android.bluetooth.BluetoothSocket.TYPE_L2CAP) {
            if (length <= mMaxTxPacketSize) {
                mSocketOS.write(b, offset, length);
            } else {
                if (android.bluetooth.BluetoothSocket.DBG)
                    android.util.Log.w(android.bluetooth.BluetoothSocket.TAG, ("WARNING: Write buffer larger than L2CAP packet size!\n" + "Packet will be divided into SDU packets of size ") + mMaxTxPacketSize);

                int tmpOffset = offset;
                int bytesToWrite = length;
                while (bytesToWrite > 0) {
                    int tmpLength = (bytesToWrite > mMaxTxPacketSize) ? mMaxTxPacketSize : bytesToWrite;
                    mSocketOS.write(b, tmpOffset, tmpLength);
                    tmpOffset += tmpLength;
                    bytesToWrite -= tmpLength;
                } 
            }
        } else {
            mSocketOS.write(b, offset, length);
        }
        // There is no good way to confirm since the entire process is asynchronous anyway
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("write out: " + mSocketOS) + " length: ") + length);

        return length;
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        if (android.bluetooth.BluetoothSocket.DBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (((("close() in, this: " + this) + ", channel: ") + mPort) + ", state: ") + mSocketState);

        if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.CLOSED)
            return;
        else {
            synchronized(this) {
                if (mSocketState == android.bluetooth.BluetoothSocket.SocketState.CLOSED)
                    return;

                mSocketState = android.bluetooth.BluetoothSocket.SocketState.CLOSED;
                if (android.bluetooth.BluetoothSocket.DBG)
                    android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (((((((("close() this: " + this) + ", channel: ") + mPort) + ", mSocketIS: ") + mSocketIS) + ", mSocketOS: ") + mSocketOS) + "mSocket: ") + mSocket);

                if (mSocket != null) {
                    if (android.bluetooth.BluetoothSocket.DBG)
                        android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "Closing mSocket: " + mSocket);

                    mSocket.shutdownInput();
                    mSocket.shutdownOutput();
                    mSocket.close();
                    mSocket = null;
                }
                if (mPfd != null) {
                    mPfd.close();
                    mPfd = null;
                }
            }
        }
    }

    /* package */
    void removeChannel() {
    }

    /* package */
    int getPort() {
        return mPort;
    }

    /**
     * Get the maximum supported Transmit packet size for the underlying transport.
     * Use this to optimize the writes done to the output socket, to avoid sending
     * half full packets.
     *
     * @return the maximum supported Transmit packet size for the underlying transport.
     */
    public int getMaxTransmitPacketSize() {
        return mMaxTxPacketSize;
    }

    /**
     * Get the maximum supported Receive packet size for the underlying transport.
     * Use this to optimize the reads done on the input stream, as any call to read
     * will return a maximum of this amount of bytes - or for some transports a
     * multiple of this value.
     *
     * @return the maximum supported Receive packet size for the underlying transport.
     */
    public int getMaxReceivePacketSize() {
        return mMaxRxPacketSize;
    }

    /**
     * Get the type of the underlying connection.
     *
     * @return one of {@link #TYPE_RFCOMM}, {@link #TYPE_SCO} or {@link #TYPE_L2CAP}
     */
    public int getConnectionType() {
        return mType;
    }

    /**
     * Change if a SDP entry should be automatically created.
     * Must be called before calling .bind, for the call to have any effect.
     *
     * @param mExcludeSdp
     * 		<li>TRUE  - do not auto generate SDP record.
     * 		<li>FALSE - default - auto generate SPP SDP record.
     * @unknown 
     */
    public void setExcludeSdp(boolean excludeSdp) {
        this.mExcludeSdp = excludeSdp;
    }

    private java.lang.String convertAddr(final byte[] addr) {
        return java.lang.String.format(java.util.Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", addr[0], addr[1], addr[2], addr[3], addr[4], addr[5]);
    }

    private java.lang.String waitSocketSignal(java.io.InputStream is) throws java.io.IOException {
        byte[] sig = new byte[android.bluetooth.BluetoothSocket.SOCK_SIGNAL_SIZE];
        int ret = readAll(is, sig);
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (("waitSocketSignal read " + android.bluetooth.BluetoothSocket.SOCK_SIGNAL_SIZE) + " bytes signal ret: ") + ret);

        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(sig);
        /* the struct in native is decorated with __attribute__((packed)), hence this is possible */
        bb.order(java.nio.ByteOrder.nativeOrder());
        int size = bb.getShort();
        if (size != android.bluetooth.BluetoothSocket.SOCK_SIGNAL_SIZE)
            throw new java.io.IOException("Connection failure, wrong signal size: " + size);

        byte[] addr = new byte[6];
        bb.get(addr);
        int channel = bb.getInt();
        int status = bb.getInt();
        mMaxTxPacketSize = bb.getShort() & 0xffff;// Convert to unsigned value

        mMaxRxPacketSize = bb.getShort() & 0xffff;// Convert to unsigned value

        java.lang.String RemoteAddr = convertAddr(addr);
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, (((((((((("waitSocketSignal: sig size: " + size) + ", remote addr: ") + RemoteAddr) + ", channel: ") + channel) + ", status: ") + status) + " MaxRxPktSize: ") + mMaxRxPacketSize) + " MaxTxPktSize: ") + mMaxTxPacketSize);

        if (status != 0)
            throw new java.io.IOException("Connection failure, status: " + status);

        return RemoteAddr;
    }

    private void createL2capRxBuffer() {
        if (mType == android.bluetooth.BluetoothSocket.TYPE_L2CAP) {
            // Allocate the buffer to use for reads.
            if (android.bluetooth.BluetoothSocket.VDBG)
                android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, "  Creating mL2capBuffer: mMaxPacketSize: " + mMaxRxPacketSize);

            mL2capBuffer = java.nio.ByteBuffer.wrap(new byte[mMaxRxPacketSize]);
            if (android.bluetooth.BluetoothSocket.VDBG)
                android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, "mL2capBuffer.remaining()" + mL2capBuffer.remaining());

            mL2capBuffer.limit(0);// Ensure we do a real read at the first read-request

            if (android.bluetooth.BluetoothSocket.VDBG)
                android.util.Log.v(android.bluetooth.BluetoothSocket.TAG, "mL2capBuffer.remaining() after limit(0):" + mL2capBuffer.remaining());

        }
    }

    private int readAll(java.io.InputStream is, byte[] b) throws java.io.IOException {
        int left = b.length;
        while (left > 0) {
            int ret = is.read(b, b.length - left, left);
            if (ret <= 0)
                throw new java.io.IOException("read failed, socket might closed or timeout, read ret: " + ret);

            left -= ret;
            if (left != 0)
                android.util.Log.w(android.bluetooth.BluetoothSocket.TAG, (("readAll() looping, read partial size: " + (b.length - left)) + ", expect size: ") + b.length);

        } 
        return b.length;
    }

    private int readInt(java.io.InputStream is) throws java.io.IOException {
        byte[] ibytes = new byte[4];
        int ret = readAll(is, ibytes);
        if (android.bluetooth.BluetoothSocket.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothSocket.TAG, "inputStream.read ret: " + ret);

        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(ibytes);
        bb.order(java.nio.ByteOrder.nativeOrder());
        return bb.getInt();
    }

    private int fillL2capRxBuffer() throws java.io.IOException {
        mL2capBuffer.rewind();
        int ret = mSocketIS.read(mL2capBuffer.array());
        if (ret == (-1)) {
            // reached end of stream - return -1
            mL2capBuffer.limit(0);
            return -1;
        }
        mL2capBuffer.limit(ret);
        return ret;
    }
}

