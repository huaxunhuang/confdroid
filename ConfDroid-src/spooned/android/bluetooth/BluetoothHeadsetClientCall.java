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
package android.bluetooth;


/**
 * This class represents a single call, its state and properties.
 * It implements {@link Parcelable} for inter-process message passing.
 *
 * @unknown 
 */
public final class BluetoothHeadsetClientCall implements android.os.Parcelable {
    /* Call state */
    /**
     * Call is active.
     */
    public static final int CALL_STATE_ACTIVE = 0;

    /**
     * Call is in held state.
     */
    public static final int CALL_STATE_HELD = 1;

    /**
     * Outgoing call that is being dialed right now.
     */
    public static final int CALL_STATE_DIALING = 2;

    /**
     * Outgoing call that remote party has already been alerted about.
     */
    public static final int CALL_STATE_ALERTING = 3;

    /**
     * Incoming call that can be accepted or rejected.
     */
    public static final int CALL_STATE_INCOMING = 4;

    /**
     * Waiting call state when there is already an active call.
     */
    public static final int CALL_STATE_WAITING = 5;

    /**
     * Call that has been held by response and hold
     * (see Bluetooth specification for further references).
     */
    public static final int CALL_STATE_HELD_BY_RESPONSE_AND_HOLD = 6;

    /**
     * Call that has been already terminated and should not be referenced as a valid call.
     */
    public static final int CALL_STATE_TERMINATED = 7;

    private final android.bluetooth.BluetoothDevice mDevice;

    private final int mId;

    private int mState;

    private java.lang.String mNumber;

    private boolean mMultiParty;

    private final boolean mOutgoing;

    private final java.util.UUID mUUID;

    /**
     * Creates BluetoothHeadsetClientCall instance.
     */
    public BluetoothHeadsetClientCall(android.bluetooth.BluetoothDevice device, int id, int state, java.lang.String number, boolean multiParty, boolean outgoing) {
        this(device, id, java.util.UUID.randomUUID(), state, number, multiParty, outgoing);
    }

    public BluetoothHeadsetClientCall(android.bluetooth.BluetoothDevice device, int id, java.util.UUID uuid, int state, java.lang.String number, boolean multiParty, boolean outgoing) {
        mDevice = device;
        mId = id;
        mUUID = uuid;
        mState = state;
        mNumber = (number != null) ? number : "";
        mMultiParty = multiParty;
        mOutgoing = outgoing;
    }

    /**
     * Sets call's state.
     *
     * <p>Note: This is an internal function and shouldn't be exposed</p>
     *
     * @param state
     * 		new call state.
     */
    public void setState(int state) {
        mState = state;
    }

    /**
     * Sets call's number.
     *
     * <p>Note: This is an internal function and shouldn't be exposed</p>
     *
     * @param number
     * 		String representing phone number.
     */
    public void setNumber(java.lang.String number) {
        mNumber = number;
    }

    /**
     * Sets this call as multi party call.
     *
     * <p>Note: This is an internal function and shouldn't be exposed</p>
     *
     * @param multiParty
     * 		if <code>true</code> sets this call as a part
     * 		of multi party conference.
     */
    public void setMultiParty(boolean multiParty) {
        mMultiParty = multiParty;
    }

    /**
     * Gets call's device.
     *
     * @return call device.
     */
    public android.bluetooth.BluetoothDevice getDevice() {
        return mDevice;
    }

    /**
     * Gets call's Id.
     *
     * @return call id.
     */
    public int getId() {
        return mId;
    }

    /**
     * Gets call's UUID.
     *
     * @return call uuid
     * @unknown 
     */
    public java.util.UUID getUUID() {
        return mUUID;
    }

    /**
     * Gets call's current state.
     *
     * @return state of this particular phone call.
     */
    public int getState() {
        return mState;
    }

    /**
     * Gets call's number.
     *
     * @return string representing phone number.
     */
    public java.lang.String getNumber() {
        return mNumber;
    }

    /**
     * Checks if call is an active call in a conference mode (aka multi party).
     *
     * @return <code>true</code> if call is a multi party call,
    <code>false</code> otherwise.
     */
    public boolean isMultiParty() {
        return mMultiParty;
    }

    /**
     * Checks if this call is an outgoing call.
     *
     * @return <code>true</code> if its outgoing call,
    <code>false</code> otherwise.
     */
    public boolean isOutgoing() {
        return mOutgoing;
    }

    public java.lang.String toString() {
        return toString(false);
    }

    public java.lang.String toString(boolean loggable) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("BluetoothHeadsetClientCall{mDevice: ");
        builder.append(loggable ? mDevice : mDevice.hashCode());
        builder.append(", mId: ");
        builder.append(mId);
        builder.append(", mUUID: ");
        builder.append(mUUID);
        builder.append(", mState: ");
        switch (mState) {
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_ACTIVE :
                builder.append("ACTIVE");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_HELD :
                builder.append("HELD");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_DIALING :
                builder.append("DIALING");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_ALERTING :
                builder.append("ALERTING");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_INCOMING :
                builder.append("INCOMING");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_WAITING :
                builder.append("WAITING");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD :
                builder.append("HELD_BY_RESPONSE_AND_HOLD");
                break;
            case android.bluetooth.BluetoothHeadsetClientCall.CALL_STATE_TERMINATED :
                builder.append("TERMINATED");
                break;
            default :
                builder.append(mState);
                break;
        }
        builder.append(", mNumber: ");
        builder.append(loggable ? mNumber : mNumber.hashCode());
        builder.append(", mMultiParty: ");
        builder.append(mMultiParty);
        builder.append(", mOutgoing: ");
        builder.append(mOutgoing);
        builder.append("}");
        return builder.toString();
    }

    /**
     * {@link Parcelable.Creator} interface implementation.
     */
    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothHeadsetClientCall> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothHeadsetClientCall>() {
        @java.lang.Override
        public android.bluetooth.BluetoothHeadsetClientCall createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.BluetoothHeadsetClientCall(((android.bluetooth.BluetoothDevice) (in.readParcelable(null))), in.readInt(), java.util.UUID.fromString(in.readString()), in.readInt(), in.readString(), in.readInt() == 1, in.readInt() == 1);
        }

        @java.lang.Override
        public android.bluetooth.BluetoothHeadsetClientCall[] newArray(int size) {
            return new android.bluetooth.BluetoothHeadsetClientCall[size];
        }
    };

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeParcelable(mDevice, 0);
        out.writeInt(mId);
        out.writeString(mUUID.toString());
        out.writeInt(mState);
        out.writeString(mNumber);
        out.writeInt(mMultiParty ? 1 : 0);
        out.writeInt(mOutgoing ? 1 : 0);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

