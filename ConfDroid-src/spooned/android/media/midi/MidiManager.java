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
 * This class is the public application interface to the MIDI service.
 *
 * <p>You can obtain an instance of this class by calling
 * {@link android.content.Context#getSystemService(java.lang.String) Context.getSystemService()}.
 *
 * {@samplecode MidiManager manager = (MidiManager) getSystemService(Context.MIDI_SERVICE);}
 */
public final class MidiManager {
    private static final java.lang.String TAG = "MidiManager";

    /**
     * Intent for starting BluetoothMidiService
     *
     * @unknown 
     */
    public static final java.lang.String BLUETOOTH_MIDI_SERVICE_INTENT = "android.media.midi.BluetoothMidiService";

    /**
     * BluetoothMidiService package name
     *
     * @unknown 
     */
    public static final java.lang.String BLUETOOTH_MIDI_SERVICE_PACKAGE = "com.android.bluetoothmidiservice";

    /**
     * BluetoothMidiService class name
     *
     * @unknown 
     */
    public static final java.lang.String BLUETOOTH_MIDI_SERVICE_CLASS = "com.android.bluetoothmidiservice.BluetoothMidiService";

    private final android.media.midi.IMidiManager mService;

    private final android.os.IBinder mToken = new android.os.Binder();

    private java.util.concurrent.ConcurrentHashMap<android.media.midi.MidiManager.DeviceCallback, android.media.midi.MidiManager.DeviceListener> mDeviceListeners = new java.util.concurrent.ConcurrentHashMap<android.media.midi.MidiManager.DeviceCallback, android.media.midi.MidiManager.DeviceListener>();

    // Binder stub for receiving device notifications from MidiService
    private class DeviceListener extends android.media.midi.IMidiDeviceListener.Stub {
        private final android.media.midi.MidiManager.DeviceCallback mCallback;

        private final android.os.Handler mHandler;

        public DeviceListener(android.media.midi.MidiManager.DeviceCallback callback, android.os.Handler handler) {
            mCallback = callback;
            mHandler = handler;
        }

        @java.lang.Override
        public void onDeviceAdded(android.media.midi.MidiDeviceInfo device) {
            if (mHandler != null) {
                final android.media.midi.MidiDeviceInfo deviceF = device;
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        mCallback.onDeviceAdded(deviceF);
                    }
                });
            } else {
                mCallback.onDeviceAdded(device);
            }
        }

        @java.lang.Override
        public void onDeviceRemoved(android.media.midi.MidiDeviceInfo device) {
            if (mHandler != null) {
                final android.media.midi.MidiDeviceInfo deviceF = device;
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        mCallback.onDeviceRemoved(deviceF);
                    }
                });
            } else {
                mCallback.onDeviceRemoved(device);
            }
        }

        @java.lang.Override
        public void onDeviceStatusChanged(android.media.midi.MidiDeviceStatus status) {
            if (mHandler != null) {
                final android.media.midi.MidiDeviceStatus statusF = status;
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        mCallback.onDeviceStatusChanged(statusF);
                    }
                });
            } else {
                mCallback.onDeviceStatusChanged(status);
            }
        }
    }

    /**
     * Callback class used for clients to receive MIDI device added and removed notifications
     */
    public static class DeviceCallback {
        /**
         * Called to notify when a new MIDI device has been added
         *
         * @param device
         * 		a {@link MidiDeviceInfo} for the newly added device
         */
        public void onDeviceAdded(android.media.midi.MidiDeviceInfo device) {
        }

        /**
         * Called to notify when a MIDI device has been removed
         *
         * @param device
         * 		a {@link MidiDeviceInfo} for the removed device
         */
        public void onDeviceRemoved(android.media.midi.MidiDeviceInfo device) {
        }

        /**
         * Called to notify when the status of a MIDI device has changed
         *
         * @param status
         * 		a {@link MidiDeviceStatus} for the changed device
         */
        public void onDeviceStatusChanged(android.media.midi.MidiDeviceStatus status) {
        }
    }

    /**
     * Listener class used for receiving the results of {@link #openDevice} and
     * {@link #openBluetoothDevice}
     */
    public interface OnDeviceOpenedListener {
        /**
         * Called to respond to a {@link #openDevice} request
         *
         * @param device
         * 		a {@link MidiDevice} for opened device, or null if opening failed
         */
        public abstract void onDeviceOpened(android.media.midi.MidiDevice device);
    }

    /**
     *
     *
     * @unknown 
     */
    public MidiManager(android.media.midi.IMidiManager service) {
        mService = service;
    }

    /**
     * Registers a callback to receive notifications when MIDI devices are added and removed.
     *
     * The {@link DeviceCallback#onDeviceStatusChanged} method will be called immediately
     * for any devices that have open ports. This allows applications to know which input
     * ports are already in use and, therefore, unavailable.
     *
     * Applications should call {@link #getDevices} before registering the callback
     * to get a list of devices already added.
     *
     * @param callback
     * 		a {@link DeviceCallback} for MIDI device notifications
     * @param handler
     * 		The {@link android.os.Handler Handler} that will be used for delivering the
     * 		device notifications. If handler is null, then the thread used for the
     * 		callback is unspecified.
     */
    public void registerDeviceCallback(android.media.midi.MidiManager.DeviceCallback callback, android.os.Handler handler) {
        android.media.midi.MidiManager.DeviceListener deviceListener = new android.media.midi.MidiManager.DeviceListener(callback, handler);
        try {
            mService.registerListener(mToken, deviceListener);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        mDeviceListeners.put(callback, deviceListener);
    }

    /**
     * Unregisters a {@link DeviceCallback}.
     *
     * @param callback
     * 		a {@link DeviceCallback} to unregister
     */
    public void unregisterDeviceCallback(android.media.midi.MidiManager.DeviceCallback callback) {
        android.media.midi.MidiManager.DeviceListener deviceListener = mDeviceListeners.remove(callback);
        if (deviceListener != null) {
            try {
                mService.unregisterListener(mToken, deviceListener);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /**
     * Gets the list of all connected MIDI devices.
     *
     * @return an array of all MIDI devices
     */
    public android.media.midi.MidiDeviceInfo[] getDevices() {
        try {
            return mService.getDevices();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void sendOpenDeviceResponse(final android.media.midi.MidiDevice device, final android.media.midi.MidiManager.OnDeviceOpenedListener listener, android.os.Handler handler) {
        if (handler != null) {
            handler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    listener.onDeviceOpened(device);
                }
            });
        } else {
            listener.onDeviceOpened(device);
        }
    }

    /**
     * Opens a MIDI device for reading and writing.
     *
     * @param deviceInfo
     * 		a {@link android.media.midi.MidiDeviceInfo} to open
     * @param listener
     * 		a {@link MidiManager.OnDeviceOpenedListener} to be called
     * 		to receive the result
     * @param handler
     * 		the {@link android.os.Handler Handler} that will be used for delivering
     * 		the result. If handler is null, then the thread used for the
     * 		listener is unspecified.
     */
    public void openDevice(android.media.midi.MidiDeviceInfo deviceInfo, android.media.midi.MidiManager.OnDeviceOpenedListener listener, android.os.Handler handler) {
        final android.media.midi.MidiDeviceInfo deviceInfoF = deviceInfo;
        final android.media.midi.MidiManager.OnDeviceOpenedListener listenerF = listener;
        final android.os.Handler handlerF = handler;
        android.media.midi.IMidiDeviceOpenCallback callback = new android.media.midi.IMidiDeviceOpenCallback.Stub() {
            @java.lang.Override
            public void onDeviceOpened(android.media.midi.IMidiDeviceServer server, android.os.IBinder deviceToken) {
                android.media.midi.MidiDevice device;
                if (server != null) {
                    device = new android.media.midi.MidiDevice(deviceInfoF, server, mService, mToken, deviceToken);
                } else {
                    device = null;
                }
                sendOpenDeviceResponse(device, listenerF, handlerF);
            }
        };
        try {
            mService.openDevice(mToken, deviceInfo, callback);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Opens a Bluetooth MIDI device for reading and writing.
     *
     * @param bluetoothDevice
     * 		a {@link android.bluetooth.BluetoothDevice} to open as a MIDI device
     * @param listener
     * 		a {@link MidiManager.OnDeviceOpenedListener} to be called to receive the
     * 		result
     * @param handler
     * 		the {@link android.os.Handler Handler} that will be used for delivering
     * 		the result. If handler is null, then the thread used for the
     * 		listener is unspecified.
     */
    public void openBluetoothDevice(android.bluetooth.BluetoothDevice bluetoothDevice, android.media.midi.MidiManager.OnDeviceOpenedListener listener, android.os.Handler handler) {
        final android.media.midi.MidiManager.OnDeviceOpenedListener listenerF = listener;
        final android.os.Handler handlerF = handler;
        android.media.midi.IMidiDeviceOpenCallback callback = new android.media.midi.IMidiDeviceOpenCallback.Stub() {
            @java.lang.Override
            public void onDeviceOpened(android.media.midi.IMidiDeviceServer server, android.os.IBinder deviceToken) {
                android.media.midi.MidiDevice device = null;
                if (server != null) {
                    try {
                        // fetch MidiDeviceInfo from the server
                        android.media.midi.MidiDeviceInfo deviceInfo = server.getDeviceInfo();
                        device = new android.media.midi.MidiDevice(deviceInfo, server, mService, mToken, deviceToken);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(android.media.midi.MidiManager.TAG, "remote exception in getDeviceInfo()");
                    }
                }
                sendOpenDeviceResponse(device, listenerF, handlerF);
            }
        };
        try {
            mService.openBluetoothDevice(mToken, bluetoothDevice, callback);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.media.midi.MidiDeviceServer createDeviceServer(android.media.midi.MidiReceiver[] inputPortReceivers, int numOutputPorts, java.lang.String[] inputPortNames, java.lang.String[] outputPortNames, android.os.Bundle properties, int type, android.media.midi.MidiDeviceServer.Callback callback) {
        try {
            android.media.midi.MidiDeviceServer server = new android.media.midi.MidiDeviceServer(mService, inputPortReceivers, numOutputPorts, callback);
            android.media.midi.MidiDeviceInfo deviceInfo = mService.registerDeviceServer(server.getBinderInterface(), inputPortReceivers.length, numOutputPorts, inputPortNames, outputPortNames, properties, type);
            if (deviceInfo == null) {
                android.util.Log.e(android.media.midi.MidiManager.TAG, "registerVirtualDevice failed");
                return null;
            }
            return server;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}

