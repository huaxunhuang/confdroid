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
 * Information about a call that is used between InCallService and Telecom.
 *
 * @unknown 
 */
public final class ParcelableCall implements android.os.Parcelable {
    private final java.lang.String mId;

    private final int mState;

    private final android.telecom.DisconnectCause mDisconnectCause;

    private final java.util.List<java.lang.String> mCannedSmsResponses;

    private final int mCapabilities;

    private final int mProperties;

    private final long mConnectTimeMillis;

    private final android.net.Uri mHandle;

    private final int mHandlePresentation;

    private final java.lang.String mCallerDisplayName;

    private final int mCallerDisplayNamePresentation;

    private final android.telecom.GatewayInfo mGatewayInfo;

    private final android.telecom.PhoneAccountHandle mAccountHandle;

    private final boolean mIsVideoCallProviderChanged;

    private final com.android.internal.telecom.IVideoProvider mVideoCallProvider;

    private android.telecom.VideoCallImpl mVideoCall;

    private final java.lang.String mParentCallId;

    private final java.util.List<java.lang.String> mChildCallIds;

    private final android.telecom.StatusHints mStatusHints;

    private final int mVideoState;

    private final java.util.List<java.lang.String> mConferenceableCallIds;

    private final android.os.Bundle mIntentExtras;

    private final android.os.Bundle mExtras;

    public ParcelableCall(java.lang.String id, int state, android.telecom.DisconnectCause disconnectCause, java.util.List<java.lang.String> cannedSmsResponses, int capabilities, int properties, long connectTimeMillis, android.net.Uri handle, int handlePresentation, java.lang.String callerDisplayName, int callerDisplayNamePresentation, android.telecom.GatewayInfo gatewayInfo, android.telecom.PhoneAccountHandle accountHandle, boolean isVideoCallProviderChanged, com.android.internal.telecom.IVideoProvider videoCallProvider, java.lang.String parentCallId, java.util.List<java.lang.String> childCallIds, android.telecom.StatusHints statusHints, int videoState, java.util.List<java.lang.String> conferenceableCallIds, android.os.Bundle intentExtras, android.os.Bundle extras) {
        mId = id;
        mState = state;
        mDisconnectCause = disconnectCause;
        mCannedSmsResponses = cannedSmsResponses;
        mCapabilities = capabilities;
        mProperties = properties;
        mConnectTimeMillis = connectTimeMillis;
        mHandle = handle;
        mHandlePresentation = handlePresentation;
        mCallerDisplayName = callerDisplayName;
        mCallerDisplayNamePresentation = callerDisplayNamePresentation;
        mGatewayInfo = gatewayInfo;
        mAccountHandle = accountHandle;
        mIsVideoCallProviderChanged = isVideoCallProviderChanged;
        mVideoCallProvider = videoCallProvider;
        mParentCallId = parentCallId;
        mChildCallIds = childCallIds;
        mStatusHints = statusHints;
        mVideoState = videoState;
        mConferenceableCallIds = java.util.Collections.unmodifiableList(conferenceableCallIds);
        mIntentExtras = intentExtras;
        mExtras = extras;
    }

    /**
     * The unique ID of the call.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * The current state of the call.
     */
    public int getState() {
        return mState;
    }

    /**
     * Reason for disconnection, as described by {@link android.telecomm.DisconnectCause}. Valid
     * when call state is {@link CallState#DISCONNECTED}.
     */
    public android.telecom.DisconnectCause getDisconnectCause() {
        return mDisconnectCause;
    }

    /**
     * The set of possible text message responses when this call is incoming.
     */
    public java.util.List<java.lang.String> getCannedSmsResponses() {
        return mCannedSmsResponses;
    }

    // Bit mask of actions a call supports, values are defined in {@link CallCapabilities}.
    public int getCapabilities() {
        return mCapabilities;
    }

    /**
     * Bitmask of properties of the call.
     */
    public int getProperties() {
        return mProperties;
    }

    /**
     * The time that the call switched to the active state.
     */
    public long getConnectTimeMillis() {
        return mConnectTimeMillis;
    }

    /**
     * The endpoint to which the call is connected.
     */
    public android.net.Uri getHandle() {
        return mHandle;
    }

    /**
     * The presentation requirements for the handle. See {@link TelecomManager} for valid values.
     */
    public int getHandlePresentation() {
        return mHandlePresentation;
    }

    /**
     * The endpoint to which the call is connected.
     */
    public java.lang.String getCallerDisplayName() {
        return mCallerDisplayName;
    }

    /**
     * The presentation requirements for the caller display name.
     * See {@link TelecomManager} for valid values.
     */
    public int getCallerDisplayNamePresentation() {
        return mCallerDisplayNamePresentation;
    }

    /**
     * Gateway information for the call.
     */
    public android.telecom.GatewayInfo getGatewayInfo() {
        return mGatewayInfo;
    }

    /**
     * PhoneAccountHandle information for the call.
     */
    public android.telecom.PhoneAccountHandle getAccountHandle() {
        return mAccountHandle;
    }

    /**
     * Returns an object for remotely communicating through the video call provider's binder.
     *
     * @return The video call.
     */
    public android.telecom.VideoCallImpl getVideoCallImpl() {
        if ((mVideoCall == null) && (mVideoCallProvider != null)) {
            try {
                mVideoCall = new android.telecom.VideoCallImpl(mVideoCallProvider);
            } catch (android.os.RemoteException ignored) {
                // Ignore RemoteException.
            }
        }
        return mVideoCall;
    }

    /**
     * The conference call to which this call is conferenced. Null if not conferenced.
     */
    public java.lang.String getParentCallId() {
        return mParentCallId;
    }

    /**
     * The child call-IDs if this call is a conference call. Returns an empty list if this is not
     * a conference call or if the conference call contains no children.
     */
    public java.util.List<java.lang.String> getChildCallIds() {
        return mChildCallIds;
    }

    public java.util.List<java.lang.String> getConferenceableCallIds() {
        return mConferenceableCallIds;
    }

    /**
     * The status label and icon.
     *
     * @return Status hints.
     */
    public android.telecom.StatusHints getStatusHints() {
        return mStatusHints;
    }

    /**
     * The video state.
     *
     * @return The video state of the call.
     */
    public int getVideoState() {
        return mVideoState;
    }

    /**
     * Any extras associated with this call.
     *
     * @return a bundle of extras
     */
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Extras passed in as part of the original call intent.
     *
     * @return The intent extras.
     */
    public android.os.Bundle getIntentExtras() {
        return mIntentExtras;
    }

    /**
     * Indicates to the receiver of the {@link ParcelableCall} whether a change has occurred in the
     * {@link android.telecom.InCallService.VideoCall} associated with this call.  Since
     * {@link #getVideoCall()} creates a new {@link VideoCallImpl}, it is useful to know whether
     * the provider has changed (which can influence whether it is accessed).
     *
     * @return {@code true} if the video call changed, {@code false} otherwise.
     */
    public boolean isVideoCallProviderChanged() {
        return mIsVideoCallProviderChanged;
    }

    /**
     * Responsible for creating ParcelableCall objects for deserialized Parcels.
     */
    public static final android.os.Parcelable.Creator<android.telecom.ParcelableCall> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableCall>() {
        @java.lang.Override
        public android.telecom.ParcelableCall createFromParcel(android.os.Parcel source) {
            java.lang.ClassLoader classLoader = android.telecom.ParcelableCall.class.getClassLoader();
            java.lang.String id = source.readString();
            int state = source.readInt();
            android.telecom.DisconnectCause disconnectCause = source.readParcelable(classLoader);
            java.util.List<java.lang.String> cannedSmsResponses = new java.util.ArrayList<>();
            source.readList(cannedSmsResponses, classLoader);
            int capabilities = source.readInt();
            int properties = source.readInt();
            long connectTimeMillis = source.readLong();
            android.net.Uri handle = source.readParcelable(classLoader);
            int handlePresentation = source.readInt();
            java.lang.String callerDisplayName = source.readString();
            int callerDisplayNamePresentation = source.readInt();
            android.telecom.GatewayInfo gatewayInfo = source.readParcelable(classLoader);
            android.telecom.PhoneAccountHandle accountHandle = source.readParcelable(classLoader);
            boolean isVideoCallProviderChanged = source.readByte() == 1;
            com.android.internal.telecom.IVideoProvider videoCallProvider = IVideoProvider.Stub.asInterface(source.readStrongBinder());
            java.lang.String parentCallId = source.readString();
            java.util.List<java.lang.String> childCallIds = new java.util.ArrayList<>();
            source.readList(childCallIds, classLoader);
            android.telecom.StatusHints statusHints = source.readParcelable(classLoader);
            int videoState = source.readInt();
            java.util.List<java.lang.String> conferenceableCallIds = new java.util.ArrayList<>();
            source.readList(conferenceableCallIds, classLoader);
            android.os.Bundle intentExtras = source.readBundle(classLoader);
            android.os.Bundle extras = source.readBundle(classLoader);
            return new android.telecom.ParcelableCall(id, state, disconnectCause, cannedSmsResponses, capabilities, properties, connectTimeMillis, handle, handlePresentation, callerDisplayName, callerDisplayNamePresentation, gatewayInfo, accountHandle, isVideoCallProviderChanged, videoCallProvider, parentCallId, childCallIds, statusHints, videoState, conferenceableCallIds, intentExtras, extras);
        }

        @java.lang.Override
        public android.telecom.ParcelableCall[] newArray(int size) {
            return new android.telecom.ParcelableCall[size];
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
     * Writes ParcelableCall object into a Parcel.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeString(mId);
        destination.writeInt(mState);
        destination.writeParcelable(mDisconnectCause, 0);
        destination.writeList(mCannedSmsResponses);
        destination.writeInt(mCapabilities);
        destination.writeInt(mProperties);
        destination.writeLong(mConnectTimeMillis);
        destination.writeParcelable(mHandle, 0);
        destination.writeInt(mHandlePresentation);
        destination.writeString(mCallerDisplayName);
        destination.writeInt(mCallerDisplayNamePresentation);
        destination.writeParcelable(mGatewayInfo, 0);
        destination.writeParcelable(mAccountHandle, 0);
        destination.writeByte(((byte) (mIsVideoCallProviderChanged ? 1 : 0)));
        destination.writeStrongBinder(mVideoCallProvider != null ? mVideoCallProvider.asBinder() : null);
        destination.writeString(mParentCallId);
        destination.writeList(mChildCallIds);
        destination.writeParcelable(mStatusHints, 0);
        destination.writeInt(mVideoState);
        destination.writeList(mConferenceableCallIds);
        destination.writeBundle(mIntentExtras);
        destination.writeBundle(mExtras);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("[%s, parent:%s, children:%s]", mId, mParentCallId, mChildCallIds);
    }
}

