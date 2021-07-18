/**
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.telecom;


/**
 * Information about a connection that is used between Telecom and the ConnectionService.
 * This is used to send initial Connection information to Telecom when the connection is
 * first created.
 *
 * @unknown 
 */
public final class ParcelableConnection implements android.os.Parcelable {
    private final android.telecom.PhoneAccountHandle mPhoneAccount;

    private final int mState;

    private final int mConnectionCapabilities;

    private final int mConnectionProperties;

    private final android.net.Uri mAddress;

    private final int mAddressPresentation;

    private final java.lang.String mCallerDisplayName;

    private final int mCallerDisplayNamePresentation;

    private final com.android.internal.telecom.IVideoProvider mVideoProvider;

    private final int mVideoState;

    private final boolean mRingbackRequested;

    private final boolean mIsVoipAudioMode;

    private final long mConnectTimeMillis;

    private final android.telecom.StatusHints mStatusHints;

    private final android.telecom.DisconnectCause mDisconnectCause;

    private final java.util.List<java.lang.String> mConferenceableConnectionIds;

    private final android.os.Bundle mExtras;

    /**
     *
     *
     * @unknown 
     */
    public ParcelableConnection(android.telecom.PhoneAccountHandle phoneAccount, int state, int capabilities, int properties, android.net.Uri address, int addressPresentation, java.lang.String callerDisplayName, int callerDisplayNamePresentation, com.android.internal.telecom.IVideoProvider videoProvider, int videoState, boolean ringbackRequested, boolean isVoipAudioMode, long connectTimeMillis, android.telecom.StatusHints statusHints, android.telecom.DisconnectCause disconnectCause, java.util.List<java.lang.String> conferenceableConnectionIds, android.os.Bundle extras) {
        mPhoneAccount = phoneAccount;
        mState = state;
        mConnectionCapabilities = capabilities;
        mConnectionProperties = properties;
        mAddress = address;
        mAddressPresentation = addressPresentation;
        mCallerDisplayName = callerDisplayName;
        mCallerDisplayNamePresentation = callerDisplayNamePresentation;
        mVideoProvider = videoProvider;
        mVideoState = videoState;
        mRingbackRequested = ringbackRequested;
        mIsVoipAudioMode = isVoipAudioMode;
        mConnectTimeMillis = connectTimeMillis;
        mStatusHints = statusHints;
        mDisconnectCause = disconnectCause;
        mConferenceableConnectionIds = conferenceableConnectionIds;
        mExtras = extras;
    }

    public android.telecom.PhoneAccountHandle getPhoneAccount() {
        return mPhoneAccount;
    }

    public int getState() {
        return mState;
    }

    /**
     * Returns the current connection capabilities bit-mask.  Connection capabilities are defined as
     * {@code CAPABILITY_*} constants in {@link Connection}.
     *
     * @return Bit-mask containing capabilities of the connection.
     */
    public int getConnectionCapabilities() {
        return mConnectionCapabilities;
    }

    /**
     * Returns the current connection properties bit-mask.  Connection properties are defined as
     * {@code PROPERTY_*} constants in {@link Connection}.
     *
     * @return Bit-mask containing properties of the connection.
     */
    public int getConnectionProperties() {
        return mConnectionProperties;
    }

    public android.net.Uri getHandle() {
        return mAddress;
    }

    public int getHandlePresentation() {
        return mAddressPresentation;
    }

    public java.lang.String getCallerDisplayName() {
        return mCallerDisplayName;
    }

    public int getCallerDisplayNamePresentation() {
        return mCallerDisplayNamePresentation;
    }

    public com.android.internal.telecom.IVideoProvider getVideoProvider() {
        return mVideoProvider;
    }

    public int getVideoState() {
        return mVideoState;
    }

    public boolean isRingbackRequested() {
        return mRingbackRequested;
    }

    public boolean getIsVoipAudioMode() {
        return mIsVoipAudioMode;
    }

    public long getConnectTimeMillis() {
        return mConnectTimeMillis;
    }

    public final android.telecom.StatusHints getStatusHints() {
        return mStatusHints;
    }

    public final android.telecom.DisconnectCause getDisconnectCause() {
        return mDisconnectCause;
    }

    public final java.util.List<java.lang.String> getConferenceableConnectionIds() {
        return mConferenceableConnectionIds;
    }

    public final android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder().append("ParcelableConnection [act:").append(mPhoneAccount).append("], state:").append(mState).append(", capabilities:").append(android.telecom.Connection.capabilitiesToString(mConnectionCapabilities)).append(", properties:").append(android.telecom.Connection.propertiesToString(mConnectionProperties)).append(", extras:").append(mExtras).toString();
    }

    public static final android.os.Parcelable.Creator<android.telecom.ParcelableConnection> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableConnection>() {
        @java.lang.Override
        public android.telecom.ParcelableConnection createFromParcel(android.os.Parcel source) {
            java.lang.ClassLoader classLoader = android.telecom.ParcelableConnection.class.getClassLoader();
            android.telecom.PhoneAccountHandle phoneAccount = source.readParcelable(classLoader);
            int state = source.readInt();
            int capabilities = source.readInt();
            android.net.Uri address = source.readParcelable(classLoader);
            int addressPresentation = source.readInt();
            java.lang.String callerDisplayName = source.readString();
            int callerDisplayNamePresentation = source.readInt();
            com.android.internal.telecom.IVideoProvider videoCallProvider = IVideoProvider.Stub.asInterface(source.readStrongBinder());
            int videoState = source.readInt();
            boolean ringbackRequested = source.readByte() == 1;
            boolean audioModeIsVoip = source.readByte() == 1;
            long connectTimeMillis = source.readLong();
            android.telecom.StatusHints statusHints = source.readParcelable(classLoader);
            android.telecom.DisconnectCause disconnectCause = source.readParcelable(classLoader);
            java.util.List<java.lang.String> conferenceableConnectionIds = new java.util.ArrayList<>();
            source.readStringList(conferenceableConnectionIds);
            android.os.Bundle extras = android.os.Bundle.setDefusable(source.readBundle(classLoader), true);
            int properties = source.readInt();
            return new android.telecom.ParcelableConnection(phoneAccount, state, capabilities, properties, address, addressPresentation, callerDisplayName, callerDisplayNamePresentation, videoCallProvider, videoState, ringbackRequested, audioModeIsVoip, connectTimeMillis, statusHints, disconnectCause, conferenceableConnectionIds, extras);
        }

        @java.lang.Override
        public android.telecom.ParcelableConnection[] newArray(int size) {
            return new android.telecom.ParcelableConnection[size];
        }
    };

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes ParcelableConnection object into a Parcel.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeParcelable(mPhoneAccount, 0);
        destination.writeInt(mState);
        destination.writeInt(mConnectionCapabilities);
        destination.writeParcelable(mAddress, 0);
        destination.writeInt(mAddressPresentation);
        destination.writeString(mCallerDisplayName);
        destination.writeInt(mCallerDisplayNamePresentation);
        destination.writeStrongBinder(mVideoProvider != null ? mVideoProvider.asBinder() : null);
        destination.writeInt(mVideoState);
        destination.writeByte(((byte) (mRingbackRequested ? 1 : 0)));
        destination.writeByte(((byte) (mIsVoipAudioMode ? 1 : 0)));
        destination.writeLong(mConnectTimeMillis);
        destination.writeParcelable(mStatusHints, 0);
        destination.writeParcelable(mDisconnectCause, 0);
        destination.writeStringList(mConferenceableConnectionIds);
        destination.writeBundle(mExtras);
        destination.writeInt(mConnectionProperties);
    }
}

