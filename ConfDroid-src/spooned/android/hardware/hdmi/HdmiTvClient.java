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
package android.hardware.hdmi;


/**
 * HdmiTvClient represents HDMI-CEC logical device of type TV in the Android system
 * which acts as TV/Display. It provides with methods that manage, interact with other
 * devices on the CEC bus.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class HdmiTvClient extends android.hardware.hdmi.HdmiClient {
    private static final java.lang.String TAG = "HdmiTvClient";

    /**
     * Size of MHL register for vendor command
     */
    public static final int VENDOR_DATA_SIZE = 16;

    /* package */
    HdmiTvClient(android.hardware.hdmi.IHdmiControlService service) {
        super(service);
    }

    // Factory method for HdmiTvClient.
    // Declared package-private. Accessed by HdmiControlManager only.
    /* package */
    static android.hardware.hdmi.HdmiTvClient create(android.hardware.hdmi.IHdmiControlService service) {
        return new android.hardware.hdmi.HdmiTvClient(service);
    }

    @java.lang.Override
    public int getDeviceType() {
        return android.hardware.hdmi.HdmiDeviceInfo.DEVICE_TV;
    }

    /**
     * Callback interface used to get the result of {@link #deviceSelect}.
     */
    public interface SelectCallback {
        /**
         * Called when the operation is finished.
         *
         * @param result
         * 		the result value of {@link #deviceSelect}
         */
        void onComplete(int result);
    }

    /**
     * Selects a CEC logical device to be a new active source.
     *
     * @param logicalAddress
     * 		logical address of the device to select
     * @param callback
     * 		callback to get the result with
     * @throws {@link
     * 		IllegalArgumentException} if the {@code callback} is null
     */
    public void deviceSelect(int logicalAddress, @android.annotation.NonNull
    android.hardware.hdmi.HdmiTvClient.SelectCallback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null.");
        }
        try {
            mService.deviceSelect(logicalAddress, android.hardware.hdmi.HdmiTvClient.getCallbackWrapper(callback));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to select device: ", e);
        }
    }

    private static android.hardware.hdmi.IHdmiControlCallback getCallbackWrapper(final android.hardware.hdmi.HdmiTvClient.SelectCallback callback) {
        return new android.hardware.hdmi.IHdmiControlCallback.Stub() {
            @java.lang.Override
            public void onComplete(int result) {
                callback.onComplete(result);
            }
        };
    }

    /**
     * Selects a HDMI port to be a new route path.
     *
     * @param portId
     * 		HDMI port to select
     * @param callback
     * 		callback to get the result with
     * @throws {@link
     * 		IllegalArgumentException} if the {@code callback} is null
     */
    public void portSelect(int portId, @android.annotation.NonNull
    android.hardware.hdmi.HdmiTvClient.SelectCallback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("Callback must not be null");
        }
        try {
            mService.portSelect(portId, android.hardware.hdmi.HdmiTvClient.getCallbackWrapper(callback));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to select port: ", e);
        }
    }

    /**
     * Callback interface used to get the input change event.
     */
    public interface InputChangeListener {
        /**
         * Called when the input was changed.
         *
         * @param info
         * 		newly selected HDMI input
         */
        void onChanged(android.hardware.hdmi.HdmiDeviceInfo info);
    }

    /**
     * Sets the listener used to get informed of the input change event.
     *
     * @param listener
     * 		listener object
     */
    public void setInputChangeListener(android.hardware.hdmi.HdmiTvClient.InputChangeListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null.");
        }
        try {
            mService.setInputChangeListener(android.hardware.hdmi.HdmiTvClient.getListenerWrapper(listener));
        } catch (android.os.RemoteException e) {
            android.util.Log.e("TAG", "Failed to set InputChangeListener:", e);
        }
    }

    private static android.hardware.hdmi.IHdmiInputChangeListener getListenerWrapper(final android.hardware.hdmi.HdmiTvClient.InputChangeListener listener) {
        return new android.hardware.hdmi.IHdmiInputChangeListener.Stub() {
            @java.lang.Override
            public void onChanged(android.hardware.hdmi.HdmiDeviceInfo info) {
                listener.onChanged(info);
            }
        };
    }

    /**
     * Returns all the CEC devices connected to TV.
     *
     * @return list of {@link HdmiDeviceInfo} for connected CEC devices.
    Empty list is returned if there is none.
     */
    public java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getDeviceList() {
        try {
            return mService.getDeviceList();
        } catch (android.os.RemoteException e) {
            android.util.Log.e("TAG", "Failed to call getDeviceList():", e);
            return java.util.Collections.<android.hardware.hdmi.HdmiDeviceInfo>emptyList();
        }
    }

    /**
     * Sets system audio mode.
     *
     * @param enabled
     * 		set to {@code true} to enable the mode; otherwise {@code false}
     * @param callback
     * 		callback to get the result with
     * @throws {@link
     * 		IllegalArgumentException} if the {@code callback} is null
     */
    public void setSystemAudioMode(boolean enabled, android.hardware.hdmi.HdmiTvClient.SelectCallback callback) {
        try {
            mService.setSystemAudioMode(enabled, android.hardware.hdmi.HdmiTvClient.getCallbackWrapper(callback));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to set system audio mode:", e);
        }
    }

    /**
     * Sets system audio volume.
     *
     * @param oldIndex
     * 		current volume index
     * @param newIndex
     * 		volume index to be set
     * @param maxIndex
     * 		maximum volume index
     */
    public void setSystemAudioVolume(int oldIndex, int newIndex, int maxIndex) {
        try {
            mService.setSystemAudioVolume(oldIndex, newIndex, maxIndex);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to set volume: ", e);
        }
    }

    /**
     * Sets system audio mute status.
     *
     * @param mute
     * 		{@code true} if muted; otherwise, {@code false}
     */
    public void setSystemAudioMute(boolean mute) {
        try {
            mService.setSystemAudioMute(mute);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to set mute: ", e);
        }
    }

    /**
     * Sets record listener.
     *
     * @param listener
     * 		
     */
    public void setRecordListener(@android.annotation.NonNull
    android.hardware.hdmi.HdmiRecordListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null.");
        }
        try {
            mService.setHdmiRecordListener(android.hardware.hdmi.HdmiTvClient.getListenerWrapper(listener));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to set record listener.", e);
        }
    }

    /**
     * Sends a &lt;Standby&gt; command to other device.
     *
     * @param deviceId
     * 		device id to send the command to
     */
    public void sendStandby(int deviceId) {
        try {
            mService.sendStandby(getDeviceType(), deviceId);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "sendStandby threw exception ", e);
        }
    }

    private static android.hardware.hdmi.IHdmiRecordListener getListenerWrapper(final android.hardware.hdmi.HdmiRecordListener callback) {
        return new android.hardware.hdmi.IHdmiRecordListener.Stub() {
            @java.lang.Override
            public byte[] getOneTouchRecordSource(int recorderAddress) {
                android.hardware.hdmi.HdmiRecordSources.RecordSource source = callback.onOneTouchRecordSourceRequested(recorderAddress);
                if (source == null) {
                    return libcore.util.EmptyArray.BYTE;
                }
                byte[] data = new byte[source.getDataSize(true)];
                source.toByteArray(true, data, 0);
                return data;
            }

            @java.lang.Override
            public void onOneTouchRecordResult(int recorderAddress, int result) {
                callback.onOneTouchRecordResult(recorderAddress, result);
            }

            @java.lang.Override
            public void onTimerRecordingResult(int recorderAddress, int result) {
                callback.onTimerRecordingResult(recorderAddress, android.hardware.hdmi.HdmiRecordListener.TimerStatusData.parseFrom(result));
            }

            @java.lang.Override
            public void onClearTimerRecordingResult(int recorderAddress, int result) {
                callback.onClearTimerRecordingResult(recorderAddress, result);
            }
        };
    }

    /**
     * Starts one touch recording with the given recorder address and recorder source.
     * <p>
     * Usage
     * <pre>
     * HdmiTvClient tvClient = ....;
     * // for own source.
     * OwnSource ownSource = HdmiRecordSources.ofOwnSource();
     * tvClient.startOneTouchRecord(recorderAddress, ownSource);
     * </pre>
     */
    public void startOneTouchRecord(int recorderAddress, @android.annotation.NonNull
    android.hardware.hdmi.HdmiRecordSources.RecordSource source) {
        if (source == null) {
            throw new java.lang.IllegalArgumentException("source must not be null.");
        }
        try {
            byte[] data = new byte[source.getDataSize(true)];
            source.toByteArray(true, data, 0);
            mService.startOneTouchRecord(recorderAddress, data);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to start record: ", e);
        }
    }

    /**
     * Stops one touch record.
     *
     * @param recorderAddress
     * 		recorder address where recoding will be stopped
     */
    public void stopOneTouchRecord(int recorderAddress) {
        try {
            mService.stopOneTouchRecord(recorderAddress);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to stop record: ", e);
        }
    }

    /**
     * Starts timer recording with the given recoder address and recorder source.
     * <p>
     * Usage
     * <pre>
     * HdmiTvClient tvClient = ....;
     * // create timer info
     * TimerInfo timerInfo = HdmiTimerRecourdSources.timerInfoOf(...);
     * // for digital source.
     * DigitalServiceSource recordSource = HdmiRecordSources.ofDigitalService(...);
     * // create timer recording source.
     * TimerRecordSource source = HdmiTimerRecourdSources.ofDigitalSource(timerInfo, recordSource);
     * tvClient.startTimerRecording(recorderAddress, source);
     * </pre>
     *
     * @param recorderAddress
     * 		target recorder address
     * @param sourceType
     * 		type of record source. It should be one of
     * 		{@link HdmiControlManager#TIMER_RECORDING_TYPE_DIGITAL},
     * 		{@link HdmiControlManager#TIMER_RECORDING_TYPE_ANALOGUE},
     * 		{@link HdmiControlManager#TIMER_RECORDING_TYPE_EXTERNAL}.
     * @param source
     * 		record source to be used
     */
    public void startTimerRecording(int recorderAddress, int sourceType, android.hardware.hdmi.HdmiTimerRecordSources.TimerRecordSource source) {
        if (source == null) {
            throw new java.lang.IllegalArgumentException("source must not be null.");
        }
        checkTimerRecordingSourceType(sourceType);
        try {
            byte[] data = new byte[source.getDataSize()];
            source.toByteArray(data, 0);
            mService.startTimerRecording(recorderAddress, sourceType, data);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to start record: ", e);
        }
    }

    private void checkTimerRecordingSourceType(int sourceType) {
        switch (sourceType) {
            case android.hardware.hdmi.HdmiControlManager.TIMER_RECORDING_TYPE_DIGITAL :
            case android.hardware.hdmi.HdmiControlManager.TIMER_RECORDING_TYPE_ANALOGUE :
            case android.hardware.hdmi.HdmiControlManager.TIMER_RECORDING_TYPE_EXTERNAL :
                break;
            default :
                throw new java.lang.IllegalArgumentException("Invalid source type:" + sourceType);
        }
    }

    /**
     * Clears timer recording with the given recorder address and recording source.
     * For more details, please refer {@link #startTimerRecording(int, int, TimerRecordSource)}.
     */
    public void clearTimerRecording(int recorderAddress, int sourceType, android.hardware.hdmi.HdmiTimerRecordSources.TimerRecordSource source) {
        if (source == null) {
            throw new java.lang.IllegalArgumentException("source must not be null.");
        }
        checkTimerRecordingSourceType(sourceType);
        try {
            byte[] data = new byte[source.getDataSize()];
            source.toByteArray(data, 0);
            mService.clearTimerRecording(recorderAddress, sourceType, data);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to start record: ", e);
        }
    }

    /**
     * Interface used to get incoming MHL vendor command.
     */
    public interface HdmiMhlVendorCommandListener {
        void onReceived(int portId, int offset, int length, byte[] data);
    }

    /**
     * Sets {@link HdmiMhlVendorCommandListener} to get incoming MHL vendor command.
     *
     * @param listener
     * 		to receive incoming MHL vendor command
     */
    public void setHdmiMhlVendorCommandListener(android.hardware.hdmi.HdmiTvClient.HdmiMhlVendorCommandListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null.");
        }
        try {
            mService.addHdmiMhlVendorCommandListener(getListenerWrapper(listener));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to set hdmi mhl vendor command listener: ", e);
        }
    }

    private android.hardware.hdmi.IHdmiMhlVendorCommandListener getListenerWrapper(final android.hardware.hdmi.HdmiTvClient.HdmiMhlVendorCommandListener listener) {
        return new android.hardware.hdmi.IHdmiMhlVendorCommandListener.Stub() {
            @java.lang.Override
            public void onReceived(int portId, int offset, int length, byte[] data) {
                listener.onReceived(portId, offset, length, data);
            }
        };
    }

    /**
     * Sends MHL vendor command to the device connected to a port of the given portId.
     *
     * @param portId
     * 		id of port to send MHL vendor command
     * @param offset
     * 		offset in the in given data
     * @param length
     * 		length of data. offset + length should be bound to length of data.
     * @param data
     * 		container for vendor command data. It should be 16 bytes.
     * @throws IllegalArgumentException
     * 		if the given parameters are invalid
     */
    public void sendMhlVendorCommand(int portId, int offset, int length, byte[] data) {
        if ((data == null) || (data.length != android.hardware.hdmi.HdmiTvClient.VENDOR_DATA_SIZE)) {
            throw new java.lang.IllegalArgumentException("Invalid vendor command data.");
        }
        if ((offset < 0) || (offset >= android.hardware.hdmi.HdmiTvClient.VENDOR_DATA_SIZE)) {
            throw new java.lang.IllegalArgumentException("Invalid offset:" + offset);
        }
        if ((length < 0) || ((offset + length) > android.hardware.hdmi.HdmiTvClient.VENDOR_DATA_SIZE)) {
            throw new java.lang.IllegalArgumentException("Invalid length:" + length);
        }
        try {
            mService.sendMhlVendorCommand(portId, offset, length, data);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiTvClient.TAG, "failed to send vendor command: ", e);
        }
    }
}

