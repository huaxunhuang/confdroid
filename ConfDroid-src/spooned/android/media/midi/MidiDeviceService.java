/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * A service that implements a virtual MIDI device.
 * Subclasses must implement the {@link #onGetInputPortReceivers} method to provide a
 * list of {@link MidiReceiver}s to receive data sent to the device's input ports.
 * Similarly, subclasses can call {@link #getOutputPortReceivers} to fetch a list
 * of {@link MidiReceiver}s for sending data out the output ports.
 *
 * <p>To extend this class, you must declare the service in your manifest file with
 * an intent filter with the {@link #SERVICE_INTERFACE} action
 * and meta-data to describe the virtual device.
 * For example:</p>
 * <pre>
 * &lt;service android:name=".VirtualDeviceService"
 *          android:label="&#64;string/service_name">
 *     &lt;intent-filter>
 *         &lt;action android:name="android.media.midi.MidiDeviceService" />
 *     &lt;/intent-filter>
 *           &lt;meta-data android:name="android.media.midi.MidiDeviceService"
 * android:resource="@xml/device_info" />
 * &lt;/service></pre>
 */
public abstract class MidiDeviceService extends android.app.Service {
    private static final java.lang.String TAG = "MidiDeviceService";

    public static final java.lang.String SERVICE_INTERFACE = "android.media.midi.MidiDeviceService";

    private android.media.midi.IMidiManager mMidiManager;

    private android.media.midi.MidiDeviceServer mServer;

    private android.media.midi.MidiDeviceInfo mDeviceInfo;

    private final android.media.midi.MidiDeviceServer.Callback mCallback = new android.media.midi.MidiDeviceServer.Callback() {
        @java.lang.Override
        public void onDeviceStatusChanged(android.media.midi.MidiDeviceServer server, android.media.midi.MidiDeviceStatus status) {
            android.media.midi.MidiDeviceService.this.onDeviceStatusChanged(status);
        }

        @java.lang.Override
        public void onClose() {
            android.media.midi.MidiDeviceService.this.onClose();
        }
    };

    @java.lang.Override
    public void onCreate() {
        mMidiManager = IMidiManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.MIDI_SERVICE));
        android.media.midi.MidiDeviceServer server;
        try {
            android.media.midi.MidiDeviceInfo deviceInfo = mMidiManager.getServiceDeviceInfo(getPackageName(), this.getClass().getName());
            if (deviceInfo == null) {
                android.util.Log.e(android.media.midi.MidiDeviceService.TAG, "Could not find MidiDeviceInfo for MidiDeviceService " + this);
                return;
            }
            mDeviceInfo = deviceInfo;
            android.media.midi.MidiReceiver[] inputPortReceivers = onGetInputPortReceivers();
            if (inputPortReceivers == null) {
                inputPortReceivers = new android.media.midi.MidiReceiver[0];
            }
            server = new android.media.midi.MidiDeviceServer(mMidiManager, inputPortReceivers, deviceInfo, mCallback);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.midi.MidiDeviceService.TAG, "RemoteException in IMidiManager.getServiceDeviceInfo");
            server = null;
        }
        mServer = server;
    }

    /**
     * Returns an array of {@link MidiReceiver} for the device's input ports.
     * Subclasses must override this to provide the receivers which will receive
     * data sent to the device's input ports. An empty array should be returned if
     * the device has no input ports.
     *
     * @return array of MidiReceivers
     */
    public abstract android.media.midi.MidiReceiver[] onGetInputPortReceivers();

    /**
     * Returns an array of {@link MidiReceiver} for the device's output ports.
     * These can be used to send data out the device's output ports.
     *
     * @return array of MidiReceivers
     */
    public final android.media.midi.MidiReceiver[] getOutputPortReceivers() {
        if (mServer == null) {
            return null;
        } else {
            return mServer.getOutputPortReceivers();
        }
    }

    /**
     * returns the {@link MidiDeviceInfo} instance for this service
     *
     * @return our MidiDeviceInfo
     */
    public final android.media.midi.MidiDeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    /**
     * Called to notify when an our {@link MidiDeviceStatus} has changed
     *
     * @param status
     * 		the number of the port that was opened
     */
    public void onDeviceStatusChanged(android.media.midi.MidiDeviceStatus status) {
    }

    /**
     * Called to notify when our device has been closed by all its clients
     */
    public void onClose() {
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (android.media.midi.MidiDeviceService.SERVICE_INTERFACE.equals(intent.getAction()) && (mServer != null)) {
            return mServer.getBinderInterface().asBinder();
        } else {
            return null;
        }
    }
}

