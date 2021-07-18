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
 * This class is used for receiving data from a port on a MIDI device
 */
public final class MidiOutputPort extends android.media.midi.MidiSender implements java.io.Closeable {
    private static final java.lang.String TAG = "MidiOutputPort";

    private android.media.midi.IMidiDeviceServer mDeviceServer;

    private final android.os.IBinder mToken;

    private final int mPortNumber;

    private final java.io.FileInputStream mInputStream;

    private final com.android.internal.midi.MidiDispatcher mDispatcher = new com.android.internal.midi.MidiDispatcher();

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    private boolean mIsClosed;

    // This thread reads MIDI events from a socket and distributes them to the list of
    // MidiReceivers attached to this device.
    private final java.lang.Thread mThread = new java.lang.Thread() {
        @java.lang.Override
        public void run() {
            byte[] buffer = new byte[android.media.midi.MidiPortImpl.MAX_PACKET_SIZE];
            try {
                while (true) {
                    // read next event
                    int count = mInputStream.read(buffer);
                    if (count < 0) {
                        break;
                        // FIXME - inform receivers here?
                    }
                    int packetType = android.media.midi.MidiPortImpl.getPacketType(buffer, count);
                    switch (packetType) {
                        case android.media.midi.MidiPortImpl.PACKET_TYPE_DATA :
                            {
                                int offset = android.media.midi.MidiPortImpl.getDataOffset(buffer, count);
                                int size = android.media.midi.MidiPortImpl.getDataSize(buffer, count);
                                long timestamp = android.media.midi.MidiPortImpl.getPacketTimestamp(buffer, count);
                                // dispatch to all our receivers
                                mDispatcher.send(buffer, offset, size, timestamp);
                                break;
                            }
                        case android.media.midi.MidiPortImpl.PACKET_TYPE_FLUSH :
                            mDispatcher.flush();
                            break;
                        default :
                            android.util.Log.e(android.media.midi.MidiOutputPort.TAG, "Unknown packet type " + packetType);
                            break;
                    }
                } 
            } catch (java.io.IOException e) {
                // FIXME report I/O failure?
                android.util.Log.e(android.media.midi.MidiOutputPort.TAG, "read failed");
            } finally {
                libcore.io.IoUtils.closeQuietly(mInputStream);
            }
        }
    };

    /* package */
    MidiOutputPort(android.media.midi.IMidiDeviceServer server, android.os.IBinder token, android.os.ParcelFileDescriptor pfd, int portNumber) {
        mDeviceServer = server;
        mToken = token;
        mPortNumber = portNumber;
        mInputStream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
        mThread.start();
        mGuard.open("close");
    }

    /* package */
    MidiOutputPort(android.os.ParcelFileDescriptor pfd, int portNumber) {
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
    public void onConnect(android.media.midi.MidiReceiver receiver) {
        mDispatcher.getSender().connect(receiver);
    }

    @java.lang.Override
    public void onDisconnect(android.media.midi.MidiReceiver receiver) {
        mDispatcher.getSender().disconnect(receiver);
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        synchronized(mGuard) {
            if (mIsClosed)
                return;

            mGuard.close();
            mInputStream.close();
            if (mDeviceServer != null) {
                try {
                    mDeviceServer.closePort(mToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.midi.MidiOutputPort.TAG, "RemoteException in MidiOutputPort.close()");
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

