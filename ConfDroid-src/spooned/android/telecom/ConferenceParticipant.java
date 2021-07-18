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
 * limitations under the License
 */
package android.telecom;


/**
 * Parcelable representation of a participant's state in a conference call.
 *
 * @unknown 
 */
public class ConferenceParticipant implements android.os.Parcelable {
    /**
     * The conference participant's handle (e.g., phone number).
     */
    private final android.net.Uri mHandle;

    /**
     * The display name for the participant.
     */
    private final java.lang.String mDisplayName;

    /**
     * The endpoint Uri which uniquely identifies this conference participant.  E.g. for an IMS
     * conference call, this is the endpoint URI for the participant on the IMS conference server.
     */
    private final android.net.Uri mEndpoint;

    /**
     * The state of the participant in the conference.
     *
     * @see android.telecom.Connection
     */
    private final int mState;

    /**
     * Creates an instance of {@code ConferenceParticipant}.
     *
     * @param handle
     * 		The conference participant's handle (e.g., phone number).
     * @param displayName
     * 		The display name for the participant.
     * @param endpoint
     * 		The enpoint Uri which uniquely identifies this conference participant.
     * @param state
     * 		The state of the participant in the conference.
     */
    public ConferenceParticipant(android.net.Uri handle, java.lang.String displayName, android.net.Uri endpoint, int state) {
        mHandle = handle;
        mDisplayName = displayName;
        mEndpoint = endpoint;
        mState = state;
    }

    /**
     * Responsible for creating {@code ConferenceParticipant} objects for deserialized Parcels.
     */
    public static final android.os.Parcelable.Creator<android.telecom.ConferenceParticipant> CREATOR = new android.os.Parcelable.Creator<android.telecom.ConferenceParticipant>() {
        @java.lang.Override
        public android.telecom.ConferenceParticipant createFromParcel(android.os.Parcel source) {
            java.lang.ClassLoader classLoader = android.telecom.ParcelableCall.class.getClassLoader();
            android.net.Uri handle = source.readParcelable(classLoader);
            java.lang.String displayName = source.readString();
            android.net.Uri endpoint = source.readParcelable(classLoader);
            int state = source.readInt();
            return new android.telecom.ConferenceParticipant(handle, displayName, endpoint, state);
        }

        @java.lang.Override
        public android.telecom.ConferenceParticipant[] newArray(int size) {
            return new android.telecom.ConferenceParticipant[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the {@code ConferenceParticipant} to a parcel.
     *
     * @param dest
     * 		The Parcel in which the object should be written.
     * @param flags
     * 		Additional flags about how the object should be written.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(mHandle, 0);
        dest.writeString(mDisplayName);
        dest.writeParcelable(mEndpoint, 0);
        dest.writeInt(mState);
    }

    /**
     * Builds a string representation of this instance.
     *
     * @return String representing the conference participant.
     */
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[ConferenceParticipant Handle: ");
        sb.append(android.telecom.Log.pii(mHandle));
        sb.append(" DisplayName: ");
        sb.append(android.telecom.Log.pii(mDisplayName));
        sb.append(" Endpoint: ");
        sb.append(android.telecom.Log.pii(mEndpoint));
        sb.append(" State: ");
        sb.append(android.telecom.Connection.stateToString(mState));
        sb.append("]");
        return sb.toString();
    }

    /**
     * The conference participant's handle (e.g., phone number).
     */
    public android.net.Uri getHandle() {
        return mHandle;
    }

    /**
     * The display name for the participant.
     */
    public java.lang.String getDisplayName() {
        return mDisplayName;
    }

    /**
     * The enpoint Uri which uniquely identifies this conference participant.  E.g. for an IMS
     * conference call, this is the endpoint URI for the participant on the IMS conference server.
     */
    public android.net.Uri getEndpoint() {
        return mEndpoint;
    }

    /**
     * The state of the participant in the conference.
     *
     * @see android.telecom.Connection
     */
    public int getState() {
        return mState;
    }
}

