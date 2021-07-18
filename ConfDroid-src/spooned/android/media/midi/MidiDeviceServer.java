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
 * Internal class used for providing an implementation for a MIDI device.
 *
 * @unknown 
 */
public final class MidiDeviceServer implements java.io.Closeable {
    private static final java.lang.String TAG = "MidiDeviceServer";

    private final android.media.midi.IMidiManager mMidiManager;

    // MidiDeviceInfo for the device implemented by this server
    private android.media.midi.MidiDeviceInfo mDeviceInfo;

    private final int mInputPortCount;

    private final int mOutputPortCount;

    // MidiReceivers for receiving data on our input ports
    private final android.media.midi.MidiReceiver[] mInputPortReceivers;

    // MidiDispatchers for sending data on our output ports
    private com.android.internal.midi.MidiDispatcher[] mOutputPortDispatchers;

    // MidiOutputPorts for clients connected to our input ports
    private final android.media.midi.MidiOutputPort[] mInputPortOutputPorts;

    // List of all MidiInputPorts we created
    private final java.util.concurrent.CopyOnWriteArrayList<android.media.midi.MidiInputPort> mInputPorts = new java.util.concurrent.CopyOnWriteArrayList<android.media.midi.MidiInputPort>();

    // for reporting device status
    private final boolean[] mInputPortOpen;

    private final int[] mOutputPortOpenCount;

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    private boolean mIsClosed;

    private final android.media.midi.MidiDeviceServer.Callback mCallback;

    public interface Callback {
        /**
         * Called to notify when an our device status has changed
         *
         * @param server
         * 		the {@link MidiDeviceServer} that changed
         * @param status
         * 		the {@link MidiDeviceStatus} for the device
         */
        public void onDeviceStatusChanged(android.media.midi.MidiDeviceServer server, android.media.midi.MidiDeviceStatus status);

        /**
         * Called to notify when the device is closed
         */
        public void onClose();
    }

    private abstract class PortClient implements android.os.IBinder.DeathRecipient {
        final android.os.IBinder mToken;

        PortClient(android.os.IBinder token) {
            mToken = token;
            try {
                token.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                close();
            }
        }

        abstract void close();

        @java.lang.Override
        public void binderDied() {
            close();
        }
    }

    private class InputPortClient extends android.media.midi.MidiDeviceServer.PortClient {
        private final android.media.midi.MidiOutputPort mOutputPort;

        InputPortClient(android.os.IBinder token, android.media.midi.MidiOutputPort outputPort) {
            super(token);
            mOutputPort = outputPort;
        }

        @java.lang.Override
        void close() {
            mToken.unlinkToDeath(this, 0);
            synchronized(mInputPortOutputPorts) {
                int portNumber = mOutputPort.getPortNumber();
                mInputPortOutputPorts[portNumber] = null;
                mInputPortOpen[portNumber] = false;
                updateDeviceStatus();
            }
            libcore.io.IoUtils.closeQuietly(mOutputPort);
        }
    }

    private class OutputPortClient extends android.media.midi.MidiDeviceServer.PortClient {
        private final android.media.midi.MidiInputPort mInputPort;

        OutputPortClient(android.os.IBinder token, android.media.midi.MidiInputPort inputPort) {
            super(token);
            mInputPort = inputPort;
        }

        @java.lang.Override
        void close() {
            mToken.unlinkToDeath(this, 0);
            int portNumber = mInputPort.getPortNumber();
            com.android.internal.midi.MidiDispatcher dispatcher = mOutputPortDispatchers[portNumber];
            synchronized(dispatcher) {
                dispatcher.getSender().disconnect(mInputPort);
                int openCount = dispatcher.getReceiverCount();
                mOutputPortOpenCount[portNumber] = openCount;
                updateDeviceStatus();
            }
            mInputPorts.remove(mInputPort);
            libcore.io.IoUtils.closeQuietly(mInputPort);
        }
    }

    private final java.util.HashMap<android.os.IBinder, android.media.midi.MidiDeviceServer.PortClient> mPortClients = new java.util.HashMap<android.os.IBinder, android.media.midi.MidiDeviceServer.PortClient>();

    // Binder interface stub for receiving connection requests from clients
    private final android.media.midi.IMidiDeviceServer mServer = new android.media.midi.IMidiDeviceServer.Stub() {
        @java.lang.Override
        public android.os.ParcelFileDescriptor openInputPort(android.os.IBinder token, int portNumber) {
            if (mDeviceInfo.isPrivate()) {
                if (android.os.Binder.getCallingUid() != android.os.Process.myUid()) {
                    throw new java.lang.SecurityException("Can't access private device from different UID");
                }
            }
            if ((portNumber < 0) || (portNumber >= mInputPortCount)) {
                android.util.Log.e(android.media.midi.MidiDeviceServer.TAG, "portNumber out of range in openInputPort: " + portNumber);
                return null;
            }
            synchronized(mInputPortOutputPorts) {
                if (mInputPortOutputPorts[portNumber] != null) {
                    android.util.Log.d(android.media.midi.MidiDeviceServer.TAG, ("port " + portNumber) + " already open");
                    return null;
                }
                try {
                    android.os.ParcelFileDescriptor[] pair = android.os.ParcelFileDescriptor.createSocketPair(android.system.OsConstants.SOCK_SEQPACKET);
                    android.media.midi.MidiOutputPort outputPort = new android.media.midi.MidiOutputPort(pair[0], portNumber);
                    mInputPortOutputPorts[portNumber] = outputPort;
                    outputPort.connect(mInputPortReceivers[portNumber]);
                    android.media.midi.MidiDeviceServer.InputPortClient client = new android.media.midi.MidiDeviceServer.InputPortClient(token, outputPort);
                    synchronized(mPortClients) {
                        mPortClients.put(token, client);
                    }
                    mInputPortOpen[portNumber] = true;
                    updateDeviceStatus();
                    return pair[1];
                } catch (java.io.IOException e) {
                    android.util.Log.e(android.media.midi.MidiDeviceServer.TAG, "unable to create ParcelFileDescriptors in openInputPort");
                    return null;
                }
            }
        }

        @java.lang.Override
        public android.os.ParcelFileDescriptor openOutputPort(android.os.IBinder token, int portNumber) {
            if (mDeviceInfo.isPrivate()) {
                if (android.os.Binder.getCallingUid() != android.os.Process.myUid()) {
                    throw new java.lang.SecurityException("Can't access private device from different UID");
                }
            }
            if ((portNumber < 0) || (portNumber >= mOutputPortCount)) {
                android.util.Log.e(android.media.midi.MidiDeviceServer.TAG, "portNumber out of range in openOutputPort: " + portNumber);
                return null;
            }
            try {
                android.os.ParcelFileDescriptor[] pair = android.os.ParcelFileDescriptor.createSocketPair(android.system.OsConstants.SOCK_SEQPACKET);
                android.media.midi.MidiInputPort inputPort = new android.media.midi.MidiInputPort(pair[0], portNumber);
                com.android.internal.midi.MidiDispatcher dispatcher = mOutputPortDispatchers[portNumber];
                synchronized(dispatcher) {
                    dispatcher.getSender().connect(inputPort);
                    int openCount = dispatcher.getReceiverCount();
                    mOutputPortOpenCount[portNumber] = openCount;
                    updateDeviceStatus();
                }
                mInputPorts.add(inputPort);
                android.media.midi.MidiDeviceServer.OutputPortClient client = new android.media.midi.MidiDeviceServer.OutputPortClient(token, inputPort);
                synchronized(mPortClients) {
                    mPortClients.put(token, client);
                }
                return pair[1];
            } catch (java.io.IOException e) {
                android.util.Log.e(android.media.midi.MidiDeviceServer.TAG, "unable to create ParcelFileDescriptors in openOutputPort");
                return null;
            }
        }

        @java.lang.Override
        public void closePort(android.os.IBinder token) {
            synchronized(mPortClients) {
                android.media.midi.MidiDeviceServer.PortClient client = mPortClients.remove(token);
                if (client != null) {
                    client.close();
                }
            }
        }

        @java.lang.Override
        public void closeDevice() {
            if (mCallback != null) {
                mCallback.onClose();
            }
            libcore.io.IoUtils.closeQuietly(android.media.midi.MidiDeviceServer.this);
        }

        @java.lang.Override
        public int connectPorts(android.os.IBinder token, android.os.ParcelFileDescriptor pfd, int outputPortNumber) {
            android.media.midi.MidiInputPort inputPort = new android.media.midi.MidiInputPort(pfd, outputPortNumber);
            com.android.internal.midi.MidiDispatcher dispatcher = mOutputPortDispatchers[outputPortNumber];
            synchronized(dispatcher) {
                dispatcher.getSender().connect(inputPort);
                int openCount = dispatcher.getReceiverCount();
                mOutputPortOpenCount[outputPortNumber] = openCount;
                updateDeviceStatus();
            }
            mInputPorts.add(inputPort);
            android.media.midi.MidiDeviceServer.OutputPortClient client = new android.media.midi.MidiDeviceServer.OutputPortClient(token, inputPort);
            synchronized(mPortClients) {
                mPortClients.put(token, client);
            }
            return android.os.Process.myPid();// for caller to detect same process ID

        }

        @java.lang.Override
        public android.media.midi.MidiDeviceInfo getDeviceInfo() {
            return mDeviceInfo;
        }

        @java.lang.Override
        public void setDeviceInfo(android.media.midi.MidiDeviceInfo deviceInfo) {
            if (android.os.Binder.getCallingUid() != android.os.Process.SYSTEM_UID) {
                throw new java.lang.SecurityException("setDeviceInfo should only be called by MidiService");
            }
            if (mDeviceInfo != null) {
                throw new java.lang.IllegalStateException("setDeviceInfo should only be called once");
            }
            mDeviceInfo = deviceInfo;
        }
    };

    // Constructor for MidiManager.createDeviceServer()
    /* package */
    MidiDeviceServer(android.media.midi.IMidiManager midiManager, android.media.midi.MidiReceiver[] inputPortReceivers, int numOutputPorts, android.media.midi.MidiDeviceServer.Callback callback) {
        mMidiManager = midiManager;
        mInputPortReceivers = inputPortReceivers;
        mInputPortCount = inputPortReceivers.length;
        mOutputPortCount = numOutputPorts;
        mCallback = callback;
        mInputPortOutputPorts = new android.media.midi.MidiOutputPort[mInputPortCount];
        mOutputPortDispatchers = new com.android.internal.midi.MidiDispatcher[numOutputPorts];
        for (int i = 0; i < numOutputPorts; i++) {
            mOutputPortDispatchers[i] = new com.android.internal.midi.MidiDispatcher();
        }
        mInputPortOpen = new boolean[mInputPortCount];
        mOutputPortOpenCount = new int[numOutputPorts];
        mGuard.open("close");
    }

    // Constructor for MidiDeviceService.onCreate()
    /* package */
    MidiDeviceServer(android.media.midi.IMidiManager midiManager, android.media.midi.MidiReceiver[] inputPortReceivers, android.media.midi.MidiDeviceInfo deviceInfo, android.media.midi.MidiDeviceServer.Callback callback) {
        this(midiManager, inputPortReceivers, deviceInfo.getOutputPortCount(), callback);
        mDeviceInfo = deviceInfo;
    }

    /* package */
    android.media.midi.IMidiDeviceServer getBinderInterface() {
        return mServer;
    }

    public android.os.IBinder asBinder() {
        return mServer.asBinder();
    }

    private void updateDeviceStatus() {
        // clear calling identity, since we may be in a Binder call from one of our clients
        long identityToken = android.os.Binder.clearCallingIdentity();
        android.media.midi.MidiDeviceStatus status = new android.media.midi.MidiDeviceStatus(mDeviceInfo, mInputPortOpen, mOutputPortOpenCount);
        if (mCallback != null) {
            mCallback.onDeviceStatusChanged(this, status);
        }
        try {
            mMidiManager.setDeviceStatus(mServer, status);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.midi.MidiDeviceServer.TAG, "RemoteException in updateDeviceStatus");
        } finally {
            android.os.Binder.restoreCallingIdentity(identityToken);
        }
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        synchronized(mGuard) {
            if (mIsClosed)
                return;

            mGuard.close();
            for (int i = 0; i < mInputPortCount; i++) {
                android.media.midi.MidiOutputPort outputPort = mInputPortOutputPorts[i];
                if (outputPort != null) {
                    libcore.io.IoUtils.closeQuietly(outputPort);
                    mInputPortOutputPorts[i] = null;
                }
            }
            for (android.media.midi.MidiInputPort inputPort : mInputPorts) {
                libcore.io.IoUtils.closeQuietly(inputPort);
            }
            mInputPorts.clear();
            try {
                mMidiManager.unregisterDeviceServer(mServer);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.midi.MidiDeviceServer.TAG, "RemoteException in unregisterDeviceServer");
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

    /**
     * Returns an array of {@link MidiReceiver} for the device's output ports.
     * Clients can use these receivers to send data out the device's output ports.
     *
     * @return array of MidiReceivers
     */
    public android.media.midi.MidiReceiver[] getOutputPortReceivers() {
        android.media.midi.MidiReceiver[] receivers = new android.media.midi.MidiReceiver[mOutputPortCount];
        java.lang.System.arraycopy(mOutputPortDispatchers, 0, receivers, 0, mOutputPortCount);
        return receivers;
    }
}

