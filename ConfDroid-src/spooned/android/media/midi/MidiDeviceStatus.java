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
 * This is an immutable class that describes the current status of a MIDI device's ports.
 */
public final class MidiDeviceStatus implements android.os.Parcelable {
    private static final java.lang.String TAG = "MidiDeviceStatus";

    private final android.media.midi.MidiDeviceInfo mDeviceInfo;

    // true if input ports are open
    private final boolean[] mInputPortOpen;

    // open counts for output ports
    private final int[] mOutputPortOpenCount;

    /**
     *
     *
     * @unknown 
     */
    public MidiDeviceStatus(android.media.midi.MidiDeviceInfo deviceInfo, boolean[] inputPortOpen, int[] outputPortOpenCount) {
        // MidiDeviceInfo is immutable so we can share references
        mDeviceInfo = deviceInfo;
        // make copies of the arrays
        mInputPortOpen = new boolean[inputPortOpen.length];
        java.lang.System.arraycopy(inputPortOpen, 0, mInputPortOpen, 0, inputPortOpen.length);
        mOutputPortOpenCount = new int[outputPortOpenCount.length];
        java.lang.System.arraycopy(outputPortOpenCount, 0, mOutputPortOpenCount, 0, outputPortOpenCount.length);
    }

    /**
     * Creates a MidiDeviceStatus with zero for all port open counts
     *
     * @unknown 
     */
    public MidiDeviceStatus(android.media.midi.MidiDeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
        mInputPortOpen = new boolean[deviceInfo.getInputPortCount()];
        mOutputPortOpenCount = new int[deviceInfo.getOutputPortCount()];
    }

    /**
     * Returns the {@link MidiDeviceInfo} of the device.
     *
     * @return the device info
     */
    public android.media.midi.MidiDeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    /**
     * Returns true if an input port is open.
     * An input port can only be opened by one client at a time.
     *
     * @param portNumber
     * 		the input port's port number
     * @return input port open status
     */
    public boolean isInputPortOpen(int portNumber) {
        return mInputPortOpen[portNumber];
    }

    /**
     * Returns the number of clients currently connected to the specified output port.
     * Unlike input ports, an output port can be opened by multiple clients at the same time.
     *
     * @param portNumber
     * 		the output port's port number
     * @return output port open count
     */
    public int getOutputPortOpenCount(int portNumber) {
        return mOutputPortOpenCount[portNumber];
    }

    @java.lang.Override
    public java.lang.String toString() {
        int inputPortCount = mDeviceInfo.getInputPortCount();
        int outputPortCount = mDeviceInfo.getOutputPortCount();
        java.lang.StringBuilder builder = new java.lang.StringBuilder("mInputPortOpen=[");
        for (int i = 0; i < inputPortCount; i++) {
            builder.append(mInputPortOpen[i]);
            if (i < (inputPortCount - 1)) {
                builder.append(",");
            }
        }
        builder.append("] mOutputPortOpenCount=[");
        for (int i = 0; i < outputPortCount; i++) {
            builder.append(mOutputPortOpenCount[i]);
            if (i < (outputPortCount - 1)) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public static final android.os.Parcelable.Creator<android.media.midi.MidiDeviceStatus> CREATOR = new android.os.Parcelable.Creator<android.media.midi.MidiDeviceStatus>() {
        public android.media.midi.MidiDeviceStatus createFromParcel(android.os.Parcel in) {
            java.lang.ClassLoader classLoader = android.media.midi.MidiDeviceInfo.class.getClassLoader();
            android.media.midi.MidiDeviceInfo deviceInfo = in.readParcelable(classLoader);
            boolean[] inputPortOpen = in.createBooleanArray();
            int[] outputPortOpenCount = in.createIntArray();
            return new android.media.midi.MidiDeviceStatus(deviceInfo, inputPortOpen, outputPortOpenCount);
        }

        public android.media.midi.MidiDeviceStatus[] newArray(int size) {
            return new android.media.midi.MidiDeviceStatus[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mDeviceInfo, flags);
        parcel.writeBooleanArray(mInputPortOpen);
        parcel.writeIntArray(mOutputPortOpenCount);
    }
}

