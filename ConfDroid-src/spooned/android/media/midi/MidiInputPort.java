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
package android.media.midi;


/**
 * This class is used for sending data to a port on a MIDI device
 */
public final class MidiInputPort extends android.media.midi.MidiReceiver implements java.io.Closeable {
    private static final java.lang.String TAG = "MidiInputPort";

    private android.media.midi.IMidiDeviceServer mDeviceServer;

    private final android.os.IBinder mToken;

    private final int mPortNumber;

    private android.os.ParcelFileDescriptor mParcelFileDescriptor;

    private java.io.FileOutputStream mOutputStream;

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    private boolean mIsClosed;

    // buffer to use for sending data out our output stream
    private final byte[] mBuffer = new byte[android.media.midi.MidiPortImpl.MAX_PACKET_SIZE];

    /* package */
    MidiInputPort(android.media.midi.IMidiDeviceServer server, android.os.IBinder token, android.os.ParcelFileDescriptor pfd, int portNumber) {
        super(android.media.midi.MidiPortImpl.MAX_PACKET_DATA_SIZE);
        mDeviceServer = server;
        mToken = token;
        mParcelFileDescriptor = pfd;
        mPortNumber = portNumber;
        mOutputStream = new java.io.FileOutputStream(pfd.getFileDescriptor());
        mGuard.open("close");
    }

    /* package */
    MidiInputPort(android.os.ParcelFileDescriptor pfd, int portNumber) {
        this(null, null, pfd, portNumber);
    }

    /**
     * Returns the port number of this port
     *
     * @return the port's port number
     */
    public final int getPortNumber() {
        return mPortNumber;
    }

    @java.lang.Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws java.io.IOException {
        if (((offset < 0) || (count < 0)) || ((offset + count) > msg.length)) {
            throw new java.lang.IllegalArgumentException("offset or count out of range");
        }
        if (count > android.media.midi.MidiPortImpl.MAX_PACKET_DATA_SIZE) {
            throw new java.lang.IllegalArgumentException("count exceeds max message size");
        }
        synchronized(mBuffer) {
            if (mOutputStream == null) {
                throw new java.io.IOException("MidiInputPort is closed");
            }
            int length = android.media.midi.MidiPortImpl.packData(msg, offset, count, timestamp, mBuffer);
            mOutputStream.write(mBuffer, 0, length);
        }
    }

    @java.lang.Override
    public void onFlush() throws java.io.IOException {
        synchronized(mBuffer) {
            if (mOutputStream == null) {
                throw new java.io.IOException("MidiInputPort is closed");
            }
            int length = android.media.midi.MidiPortImpl.packFlush(mBuffer);
            mOutputStream.write(mBuffer, 0, length);
        }
    }

    // used by MidiDevice.connectInputPort() to connect our socket directly to another device
    /* package */
    android.os.ParcelFileDescriptor claimFileDescriptor() {
        synchronized(mGuard) {
            android.os.ParcelFileDescriptor pfd;
            synchronized(mBuffer) {
                pfd = mParcelFileDescriptor;
                if (pfd == null)
                    return null;

                libcore.io.IoUtils.closeQuietly(mOutputStream);
                mParcelFileDescriptor = null;
                mOutputStream = null;
            }
            // Set mIsClosed = true so we will not call mDeviceServer.closePort() in close().
            // MidiDevice.MidiConnection.close() will do the cleanup instead.
            mIsClosed = true;
            return pfd;
        }
    }

    // used by MidiDevice.MidiConnection to close this port after the connection is closed
    /* package */
    android.os.IBinder getToken() {
        return mToken;
    }

    // used by MidiDevice.MidiConnection to close this port after the connection is closed
    /* package */
    android.media.midi.IMidiDeviceServer getDeviceServer() {
        return mDeviceServer;
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        synchronized(mGuard) {
            if (mIsClosed)
                return;

            mGuard.close();
            synchronized(mBuffer) {
                if (mParcelFileDescriptor != null) {
                    mParcelFileDescriptor.close();
                    mParcelFileDescriptor = null;
                }
                if (mOutputStream != null) {
                    mOutputStream.close();
                    mOutputStream = null;
                }
            }
            if (mDeviceServer != null) {
                try {
                    mDeviceServer.closePort(mToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.midi.MidiInputPort.TAG, "RemoteException in MidiInputPort.close()");
                }
            }
            mIsClosed = true;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            mGuard.warnIfOpen();
            // not safe to make binder calls from finalize()
            mDeviceServer = null;
            close();
        } finally {
            super.finalize();
        }
    }
}

