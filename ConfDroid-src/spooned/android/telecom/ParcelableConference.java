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
 * A parcelable representation of a conference connection.
 *
 * @unknown 
 */
public final class ParcelableConference implements android.os.Parcelable {
    private android.telecom.PhoneAccountHandle mPhoneAccount;

    private int mState;

    private int mConnectionCapabilities;

    private int mConnectionProperties;

    private java.util.List<java.lang.String> mConnectionIds;

    private long mConnectTimeMillis = android.telecom.Conference.CONNECT_TIME_NOT_SPECIFIED;

    private final com.android.internal.telecom.IVideoProvider mVideoProvider;

    private final int mVideoState;

    private android.telecom.StatusHints mStatusHints;

    private android.os.Bundle mExtras;

    public ParcelableConference(android.telecom.PhoneAccountHandle phoneAccount, int state, int connectionCapabilities, int connectionProperties, java.util.List<java.lang.String> connectionIds, com.android.internal.telecom.IVideoProvider videoProvider, int videoState, long connectTimeMillis, android.telecom.StatusHints statusHints, android.os.Bundle extras) {
        mPhoneAccount = phoneAccount;
        mState = state;
        mConnectionCapabilities = connectionCapabilities;
        mConnectionProperties = connectionProperties;
        mConnectionIds = connectionIds;
        mConnectTimeMillis = android.telecom.Conference.CONNECT_TIME_NOT_SPECIFIED;
        mVideoProvider = videoProvider;
        mVideoState = videoState;
        mConnectTimeMillis = connectTimeMillis;
        mStatusHints = statusHints;
        mExtras = extras;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return toString();
    }

    public android.telecom.PhoneAccountHandle getPhoneAccount() {
        return mPhoneAccount;
    }

    public int getState() {
        return mState;
    }

    public int getConnectionCapabilities() {
        return mConnectionCapabilities;
    }

    public int getConnectionProperties() {
        return mConnectionProperties;
    }

    public java.util.List<java.lang.String> getConnectionIds() {
        return mConnectionIds;
    }

    public long getConnectTimeMillis() {
        return mConnectTimeMillis;
    }

    public com.android.internal.telecom.IVideoProvider getVideoProvider() {
        return mVideoProvider;
    }

    public int getVideoState() {
        return mVideoState;
    }

    public android.telecom.StatusHints getStatusHints() {
        return mStatusHints;
    }

    public android.os.Bundle getExtras() {
        return mExtras;
    }

    public static final android.os.Parcelable.Creator<android.telecom.ParcelableConference> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableConference>() {
        @java.lang.Override
        public android.telecom.ParcelableConference createFromParcel(android.os.Parcel source) {
            java.lang.ClassLoader classLoader = android.telecom.ParcelableConference.class.getClassLoader();
            android.telecom.PhoneAccountHandle phoneAccount = source.readParcelable(classLoader);
            int state = source.readInt();
            int capabilities = source.readInt();
            java.util.List<java.lang.String> connectionIds = new java.util.ArrayList<>(2);
            source.readList(connectionIds, classLoader);
            long connectTimeMillis = source.readLong();
            com.android.internal.telecom.IVideoProvider videoCallProvider = IVideoProvider.Stub.asInterface(source.readStrongBinder());
            int videoState = source.readInt();
            android.telecom.StatusHints statusHints = source.readParcelable(classLoader);
            android.os.Bundle extras = source.readBundle(classLoader);
            int properties = source.readInt();
            return new android.telecom.ParcelableConference(phoneAccount, state, capabilities, properties, connectionIds, videoCallProvider, videoState, connectTimeMillis, statusHints, extras);
        }

        @java.lang.Override
        public android.telecom.ParcelableConference[] newArray(int size) {
            return new android.telecom.ParcelableConference[size];
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
     * Writes ParcelableConference object into a Parcel.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeParcelable(mPhoneAccount, 0);
        destination.writeInt(mState);
        destination.writeInt(mConnectionCapabilities);
        destination.writeList(mConnectionIds);
        destination.writeLong(mConnectTimeMillis);
        destination.writeStrongBinder(mVideoProvider != null ? mVideoProvider.asBinder() : null);
        destination.writeInt(mVideoState);
        destination.writeParcelable(mStatusHints, 0);
        destination.writeBundle(mExtras);
        destination.writeInt(mConnectionProperties);
    }
}

