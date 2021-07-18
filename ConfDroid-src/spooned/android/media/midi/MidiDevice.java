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
 * This class is used for sending and receiving data to and from a MIDI device
 * Instances of this class are created by {@link MidiManager#openDevice}.
 */
public final class MidiDevice implements java.io.Closeable {
    private static final java.lang.String TAG = "MidiDevice";

    private final android.media.midi.MidiDeviceInfo mDeviceInfo;

    private final android.media.midi.IMidiDeviceServer mDeviceServer;

    private final android.media.midi.IMidiManager mMidiManager;

    private final android.os.IBinder mClientToken;

    private final android.os.IBinder mDeviceToken;

    private boolean mIsDeviceClosed;

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    /**
     * This class represents a connection between the output port of one device
     * and the input port of another. Created by {@link #connectPorts}.
     * Close this object to terminate the connection.
     */
    public class MidiConnection implements java.io.Closeable {
        private final android.media.midi.IMidiDeviceServer mInputPortDeviceServer;

        private final android.os.IBinder mInputPortToken;

        private final android.os.IBinder mOutputPortToken;

        private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

        private boolean mIsClosed;

        MidiConnection(android.os.IBinder outputPortToken, android.media.midi.MidiInputPort inputPort) {
            mInputPortDeviceServer = inputPort.getDeviceServer();
            mInputPortToken = inputPort.getToken();
            mOutputPortToken = outputPortToken;
            mGuard.open("close");
        }

        @java.lang.Override
        public void close() throws java.io.IOException {
            synchronized(mGuard) {
                if (mIsClosed)
                    return;

                mGuard.close();
                try {
                    // close input port
                    mInputPortDeviceServer.closePort(mInputPortToken);
                    // close output port
                    mDeviceServer.closePort(mOutputPortToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.midi.MidiDevice.TAG, "RemoteException in MidiConnection.close");
                }
                mIsClosed = true;
            }
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            try {
                mGuard.warnIfOpen();
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /* package */
    MidiDevice(android.media.midi.MidiDeviceInfo deviceInfo, android.media.midi.IMidiDeviceServer server, android.media.midi.IMidiManager midiManager, android.os.IBinder clientToken, android.os.IBinder deviceToken) {
        mDeviceInfo = deviceInfo;
        mDeviceServer = server;
        mMidiManager = midiManager;
        mClientToken = clientToken;
        mDeviceToken = deviceToken;
        mGuard.open("close");
    }

    /**
     * Returns a {@link MidiDeviceInfo} object, which describes this device.
     *
     * @return the {@link MidiDeviceInfo} object
     */
    public android.media.midi.MidiDeviceInfo getInfo() {
        return mDeviceInfo;
    }

    /**
     * Called to open a {@link MidiInputPort} for the specified port number.
     *
     * An input port can only be used by one sender at a time.
     * Opening an input port will fail if another application has already opened it for use.
     * A {@link MidiDeviceStatus} can be used to determine if an input port is already open.
     *
     * @param portNumber
     * 		the number of the input port to open
     * @return the {@link MidiInputPort} if the open is successful,
    or null in case of failure.
     */
    public android.media.midi.MidiInputPort openInputPort(int portNumber) {
        if (mIsDeviceClosed) {
            return null;
        }
        try {
            android.os.IBinder token = new android.os.Binder();
            android.os.ParcelFileDescriptor pfd = mDeviceServer.openInputPort(token, portNumber);
            if (pfd == null) {
                return null;
            }
            return new android.media.midi.MidiInputPort(mDeviceServer, token, pfd, portNumber);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.midi.MidiDevice.TAG, "RemoteException in openInputPort");
            return null;
        }
    }

    /**
     * Called to open a {@link MidiOutputPort} for the specified port number.
     *
     * An output port may be opened by multiple applications.
     *
     * @param portNumber
     * 		the number of the output port to open
     * @return the {@link MidiOutputPort} if the open is successful,
    or null in case of failure.
     */
    public android.media.midi.MidiOutputPort openOutputPort(int portNumber) {
        if (mIsDeviceClosed) {
            return null;
        }
        try {
            android.os.IBinder token = new android.os.Binder();
            android.os.ParcelFileDescriptor pfd = mDeviceServer.openOutputPort(token, portNumber);
            if (pfd == null) {
                return null;
            }
            return new android.media.midi.MidiOutputPort(mDeviceServer, token, pfd, portNumber);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.midi.MidiDevice.TAG, "RemoteException in openOutputPort");
            return null;
        }
    }

    /**
     * Connects the supplied {@link MidiInputPort} to the output port of this device
     * with the specified port number. Once the connection is made, the MidiInput port instance
     * can no longer receive data via its {@link MidiReceiver#onSend} method.
     * This method returns a {@link MidiDevice.MidiConnection} object, which can be used
     * to close the connection.
     *
     * @param inputPort
     * 		the inputPort to connect
     * @param outputPortNumber
     * 		the port number of the output port to connect inputPort to.
     * @return {@link MidiDevice.MidiConnection} object if the connection is successful,
    or null in case of failure.
     */
    public android.media.midi.MidiDevice.MidiConnection connectPorts(android.media.midi.MidiInputPort inputPort, int outputPortNumber) {
        if ((outputPortNumber < 0) || (outputPortNumber >= mDeviceInfo.getOutputPortCount())) {
            throw new java.lang.IllegalArgumentException("outputPortNumber out of range");
        }
        if (mIsDeviceClosed) {
            return null;
        }
        android.os.ParcelFileDescriptor pfd = inputPort.claimFileDescriptor();
        if (pfd == null) {
            return null;
        }
        try {
            android.os.IBinder token = new android.os.Binder();
            int calleePid = mDeviceServer.connectPorts(token, pfd, outputPortNumber);
            // If the service is a different Process then it will duplicate the pfd
            // and we can safely close this one.
            // But if the service is in the same Process then closing the pfd will
            // kill the connection. So don't do that.
            if (calleePid != android.os.Process.myPid()) {
                // close our copy of the file descriptor
                libcore.io.IoUtils.closeQuietly(pfd);
            }
            return new android.media.midi.MidiDevice.MidiConnection(token, inputPort);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.midi.MidiDevice.TAG, "RemoteException in connectPorts");
            return null;
        }
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        synchronized(mGuard) {
            if (!mIsDeviceClosed) {
                mGuard.close();
                mIsDeviceClosed = true;
                try {
                    mMidiManager.closeDevice(mClientToken, mDeviceToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.midi.MidiDevice.TAG, "RemoteException in closeDevice");
                }
            }
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            mGuard.warnIfOpen();
            close();
        } finally {
            super.finalize();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "MidiDevice: " + mDeviceInfo.toString();
    }
}

