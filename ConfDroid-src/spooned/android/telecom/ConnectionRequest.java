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
package android.telecom;


/**
 * Simple data container encapsulating a request to some entity to
 * create a new {@link Connection}.
 */
public final class ConnectionRequest implements android.os.Parcelable {
    // TODO: Token to limit recursive invocations
    private final android.telecom.PhoneAccountHandle mAccountHandle;

    private final android.net.Uri mAddress;

    private final android.os.Bundle mExtras;

    private final int mVideoState;

    private final java.lang.String mTelecomCallId;

    /**
     *
     *
     * @param accountHandle
     * 		The accountHandle which should be used to place the call.
     * @param handle
     * 		The handle (e.g., phone number) to which the {@link Connection} is to connect.
     * @param extras
     * 		Application-specific extra data.
     */
    public ConnectionRequest(android.telecom.PhoneAccountHandle accountHandle, android.net.Uri handle, android.os.Bundle extras) {
        this(accountHandle, handle, extras, android.telecom.VideoProfile.STATE_AUDIO_ONLY, null);
    }

    /**
     *
     *
     * @param accountHandle
     * 		The accountHandle which should be used to place the call.
     * @param handle
     * 		The handle (e.g., phone number) to which the {@link Connection} is to connect.
     * @param extras
     * 		Application-specific extra data.
     * @param videoState
     * 		Determines the video state for the connection.
     */
    public ConnectionRequest(android.telecom.PhoneAccountHandle accountHandle, android.net.Uri handle, android.os.Bundle extras, int videoState) {
        this(accountHandle, handle, extras, videoState, null);
    }

    /**
     *
     *
     * @param accountHandle
     * 		The accountHandle which should be used to place the call.
     * @param handle
     * 		The handle (e.g., phone number) to which the {@link Connection} is to connect.
     * @param extras
     * 		Application-specific extra data.
     * @param videoState
     * 		Determines the video state for the connection.
     * @param telecomCallId
     * 		The telecom call ID.
     * @unknown 
     */
    public ConnectionRequest(android.telecom.PhoneAccountHandle accountHandle, android.net.Uri handle, android.os.Bundle extras, int videoState, java.lang.String telecomCallId) {
        mAccountHandle = accountHandle;
        mAddress = handle;
        mExtras = extras;
        mVideoState = videoState;
        mTelecomCallId = telecomCallId;
    }

    private ConnectionRequest(android.os.Parcel in) {
        mAccountHandle = in.readParcelable(getClass().getClassLoader());
        mAddress = in.readParcelable(getClass().getClassLoader());
        mExtras = in.readParcelable(getClass().getClassLoader());
        mVideoState = in.readInt();
        mTelecomCallId = in.readString();
    }

    /**
     * The account which should be used to place the call.
     */
    public android.telecom.PhoneAccountHandle getAccountHandle() {
        return mAccountHandle;
    }

    /**
     * The handle (e.g., phone number) to which the {@link Connection} is to connect.
     */
    public android.net.Uri getAddress() {
        return mAddress;
    }

    /**
     * Application-specific extra data. Used for passing back information from an incoming
     * call {@code Intent}, and for any proprietary extensions arranged between a client
     * and servant {@code ConnectionService} which agree on a vocabulary for such data.
     */
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Describes the video states supported by the client requesting the connection.
     * Valid values: {@link VideoProfile#STATE_AUDIO_ONLY},
     * {@link VideoProfile#STATE_BIDIRECTIONAL},
     * {@link VideoProfile#STATE_TX_ENABLED},
     * {@link VideoProfile#STATE_RX_ENABLED}.
     *
     * @return The video state for the connection.
     */
    public int getVideoState() {
        return mVideoState;
    }

    /**
     * Returns the internal Telecom ID associated with the connection request.
     *
     * @return The Telecom ID.
     * @unknown 
     */
    public java.lang.String getTelecomCallId() {
        return mTelecomCallId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("ConnectionRequest %s %s", mAddress == null ? android.net.Uri.EMPTY : android.telecom.Connection.toLogSafePhoneNumber(mAddress.toString()), mExtras == null ? "" : mExtras);
    }

    public static final android.os.Parcelable.Creator<android.telecom.ConnectionRequest> CREATOR = new android.os.Parcelable.Creator<android.telecom.ConnectionRequest>() {
        @java.lang.Override
        public android.telecom.ConnectionRequest createFromParcel(android.os.Parcel source) {
            return new android.telecom.ConnectionRequest(source);
        }

        @java.lang.Override
        public android.telecom.ConnectionRequest[] newArray(int size) {
            return new android.telecom.ConnectionRequest[size];
        }
    };

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeParcelable(mAccountHandle, 0);
        destination.writeParcelable(mAddress, 0);
        destination.writeParcelable(mExtras, 0);
        destination.writeInt(mVideoState);
        destination.writeString(mTelecomCallId);
    }
}

