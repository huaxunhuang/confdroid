/**
 * Copyright (C) 2016 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class TelecomAnalytics implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.telecom.TelecomAnalytics> CREATOR = new android.os.Parcelable.Creator<android.telecom.TelecomAnalytics>() {
        @java.lang.Override
        public android.telecom.TelecomAnalytics createFromParcel(android.os.Parcel in) {
            return new android.telecom.TelecomAnalytics(in);
        }

        @java.lang.Override
        public android.telecom.TelecomAnalytics[] newArray(int size) {
            return new android.telecom.TelecomAnalytics[size];
        }
    };

    public static final class SessionTiming extends android.telecom.TimedEvent<java.lang.Integer> implements android.os.Parcelable {
        public static final android.os.Parcelable.Creator<android.telecom.TelecomAnalytics.SessionTiming> CREATOR = new android.os.Parcelable.Creator<android.telecom.TelecomAnalytics.SessionTiming>() {
            @java.lang.Override
            public android.telecom.TelecomAnalytics.SessionTiming createFromParcel(android.os.Parcel in) {
                return new android.telecom.TelecomAnalytics.SessionTiming(in);
            }

            @java.lang.Override
            public android.telecom.TelecomAnalytics.SessionTiming[] newArray(int size) {
                return new android.telecom.TelecomAnalytics.SessionTiming[size];
            }
        };

        public static final int ICA_ANSWER_CALL = 1;

        public static final int ICA_REJECT_CALL = 2;

        public static final int ICA_DISCONNECT_CALL = 3;

        public static final int ICA_HOLD_CALL = 4;

        public static final int ICA_UNHOLD_CALL = 5;

        public static final int ICA_MUTE = 6;

        public static final int ICA_SET_AUDIO_ROUTE = 7;

        public static final int ICA_CONFERENCE = 8;

        public static final int CSW_HANDLE_CREATE_CONNECTION_COMPLETE = 100;

        public static final int CSW_SET_ACTIVE = 101;

        public static final int CSW_SET_RINGING = 102;

        public static final int CSW_SET_DIALING = 103;

        public static final int CSW_SET_DISCONNECTED = 104;

        public static final int CSW_SET_ON_HOLD = 105;

        public static final int CSW_REMOVE_CALL = 106;

        public static final int CSW_SET_IS_CONFERENCED = 107;

        public static final int CSW_ADD_CONFERENCE_CALL = 108;

        private int mId;

        private long mTime;

        public SessionTiming(int id, long time) {
            this.mId = id;
            this.mTime = time;
        }

        private SessionTiming(android.os.Parcel in) {
            mId = in.readInt();
            mTime = in.readLong();
        }

        @java.lang.Override
        public java.lang.Integer getKey() {
            return mId;
        }

        @java.lang.Override
        public long getTime() {
            return mTime;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(mId);
            out.writeLong(mTime);
        }
    }

    private java.util.List<android.telecom.TelecomAnalytics.SessionTiming> mSessionTimings;

    private java.util.List<android.telecom.ParcelableCallAnalytics> mCallAnalytics;

    public TelecomAnalytics(java.util.List<android.telecom.TelecomAnalytics.SessionTiming> sessionTimings, java.util.List<android.telecom.ParcelableCallAnalytics> callAnalytics) {
        this.mSessionTimings = sessionTimings;
        this.mCallAnalytics = callAnalytics;
    }

    private TelecomAnalytics(android.os.Parcel in) {
        mSessionTimings = new java.util.ArrayList<>();
        in.readTypedList(mSessionTimings, android.telecom.TelecomAnalytics.SessionTiming.CREATOR);
        mCallAnalytics = new java.util.ArrayList<>();
        in.readTypedList(mCallAnalytics, android.telecom.ParcelableCallAnalytics.CREATOR);
    }

    public java.util.List<android.telecom.TelecomAnalytics.SessionTiming> getSessionTimings() {
        return mSessionTimings;
    }

    public java.util.List<android.telecom.ParcelableCallAnalytics> getCallAnalytics() {
        return mCallAnalytics;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeTypedList(mSessionTimings);
        out.writeTypedList(mCallAnalytics);
    }
}

