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
public class ParcelableCallAnalytics implements android.os.Parcelable {
    /**
     * {@hide }
     */
    public static final class VideoEvent implements android.os.Parcelable {
        public static final int SEND_LOCAL_SESSION_MODIFY_REQUEST = 0;

        public static final int SEND_LOCAL_SESSION_MODIFY_RESPONSE = 1;

        public static final int RECEIVE_REMOTE_SESSION_MODIFY_REQUEST = 2;

        public static final int RECEIVE_REMOTE_SESSION_MODIFY_RESPONSE = 3;

        public static final android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics.VideoEvent> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics.VideoEvent>() {
            @java.lang.Override
            public android.telecom.ParcelableCallAnalytics.VideoEvent createFromParcel(android.os.Parcel in) {
                return new android.telecom.ParcelableCallAnalytics.VideoEvent(in);
            }

            @java.lang.Override
            public android.telecom.ParcelableCallAnalytics.VideoEvent[] newArray(int size) {
                return new android.telecom.ParcelableCallAnalytics.VideoEvent[size];
            }
        };

        private int mEventName;

        private long mTimeSinceLastEvent;

        private int mVideoState;

        public VideoEvent(int eventName, long timeSinceLastEvent, int videoState) {
            mEventName = eventName;
            mTimeSinceLastEvent = timeSinceLastEvent;
            mVideoState = videoState;
        }

        VideoEvent(android.os.Parcel in) {
            mEventName = in.readInt();
            mTimeSinceLastEvent = in.readLong();
            mVideoState = in.readInt();
        }

        public int getEventName() {
            return mEventName;
        }

        public long getTimeSinceLastEvent() {
            return mTimeSinceLastEvent;
        }

        public int getVideoState() {
            return mVideoState;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(mEventName);
            out.writeLong(mTimeSinceLastEvent);
            out.writeInt(mVideoState);
        }
    }

    public static final class AnalyticsEvent implements android.os.Parcelable {
        public static final int SET_SELECT_PHONE_ACCOUNT = 0;

        public static final int SET_ACTIVE = 1;

        public static final int SET_DISCONNECTED = 2;

        public static final int START_CONNECTION = 3;

        public static final int SET_DIALING = 4;

        public static final int BIND_CS = 5;

        public static final int CS_BOUND = 6;

        public static final int REQUEST_ACCEPT = 7;

        public static final int REQUEST_REJECT = 8;

        public static final int SCREENING_SENT = 100;

        public static final int SCREENING_COMPLETED = 101;

        public static final int DIRECT_TO_VM_INITIATED = 102;

        public static final int DIRECT_TO_VM_FINISHED = 103;

        public static final int BLOCK_CHECK_INITIATED = 104;

        public static final int BLOCK_CHECK_FINISHED = 105;

        public static final int FILTERING_INITIATED = 106;

        public static final int FILTERING_COMPLETED = 107;

        public static final int FILTERING_TIMED_OUT = 108;

        public static final int SKIP_RINGING = 200;

        public static final int SILENCE = 201;

        public static final int MUTE = 202;

        public static final int UNMUTE = 203;

        public static final int AUDIO_ROUTE_BT = 204;

        public static final int AUDIO_ROUTE_EARPIECE = 205;

        public static final int AUDIO_ROUTE_HEADSET = 206;

        public static final int AUDIO_ROUTE_SPEAKER = 207;

        public static final int CONFERENCE_WITH = 300;

        public static final int SPLIT_CONFERENCE = 301;

        public static final int SET_PARENT = 302;

        public static final int REQUEST_HOLD = 400;

        public static final int REQUEST_UNHOLD = 401;

        public static final int REMOTELY_HELD = 402;

        public static final int REMOTELY_UNHELD = 403;

        public static final int SET_HOLD = 404;

        public static final int SWAP = 405;

        public static final int REQUEST_PULL = 500;

        public static final android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics.AnalyticsEvent> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics.AnalyticsEvent>() {
            @java.lang.Override
            public android.telecom.ParcelableCallAnalytics.AnalyticsEvent createFromParcel(android.os.Parcel in) {
                return new android.telecom.ParcelableCallAnalytics.AnalyticsEvent(in);
            }

            @java.lang.Override
            public android.telecom.ParcelableCallAnalytics.AnalyticsEvent[] newArray(int size) {
                return new android.telecom.ParcelableCallAnalytics.AnalyticsEvent[size];
            }
        };

        private int mEventName;

        private long mTimeSinceLastEvent;

        public AnalyticsEvent(int eventName, long timestamp) {
            mEventName = eventName;
            mTimeSinceLastEvent = timestamp;
        }

        AnalyticsEvent(android.os.Parcel in) {
            mEventName = in.readInt();
            mTimeSinceLastEvent = in.readLong();
        }

        public int getEventName() {
            return mEventName;
        }

        public long getTimeSinceLastEvent() {
            return mTimeSinceLastEvent;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(mEventName);
            out.writeLong(mTimeSinceLastEvent);
        }
    }

    public static final class EventTiming implements android.os.Parcelable {
        public static final int ACCEPT_TIMING = 0;

        public static final int REJECT_TIMING = 1;

        public static final int DISCONNECT_TIMING = 2;

        public static final int HOLD_TIMING = 3;

        public static final int UNHOLD_TIMING = 4;

        public static final int OUTGOING_TIME_TO_DIALING_TIMING = 5;

        public static final int BIND_CS_TIMING = 6;

        public static final int SCREENING_COMPLETED_TIMING = 7;

        public static final int DIRECT_TO_VM_FINISHED_TIMING = 8;

        public static final int BLOCK_CHECK_FINISHED_TIMING = 9;

        public static final int FILTERING_COMPLETED_TIMING = 10;

        public static final int FILTERING_TIMED_OUT_TIMING = 11;

        public static final int INVALID = 999999;

        public static final android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics.EventTiming> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics.EventTiming>() {
            @java.lang.Override
            public android.telecom.ParcelableCallAnalytics.EventTiming createFromParcel(android.os.Parcel in) {
                return new android.telecom.ParcelableCallAnalytics.EventTiming(in);
            }

            @java.lang.Override
            public android.telecom.ParcelableCallAnalytics.EventTiming[] newArray(int size) {
                return new android.telecom.ParcelableCallAnalytics.EventTiming[size];
            }
        };

        private int mName;

        private long mTime;

        public EventTiming(int name, long time) {
            this.mName = name;
            this.mTime = time;
        }

        private EventTiming(android.os.Parcel in) {
            mName = in.readInt();
            mTime = in.readLong();
        }

        public int getName() {
            return mName;
        }

        public long getTime() {
            return mTime;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(mName);
            out.writeLong(mTime);
        }
    }

    public static final int CALLTYPE_UNKNOWN = 0;

    public static final int CALLTYPE_INCOMING = 1;

    public static final int CALLTYPE_OUTGOING = 2;

    // Constants for call technology
    public static final int CDMA_PHONE = 0x1;

    public static final int GSM_PHONE = 0x2;

    public static final int IMS_PHONE = 0x4;

    public static final int SIP_PHONE = 0x8;

    public static final int THIRD_PARTY_PHONE = 0x10;

    public static final long MILLIS_IN_5_MINUTES = (1000 * 60) * 5;

    public static final long MILLIS_IN_1_SECOND = 1000;

    public static final int STILL_CONNECTED = -1;

    public static final android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics> CREATOR = new android.os.Parcelable.Creator<android.telecom.ParcelableCallAnalytics>() {
        @java.lang.Override
        public android.telecom.ParcelableCallAnalytics createFromParcel(android.os.Parcel in) {
            return new android.telecom.ParcelableCallAnalytics(in);
        }

        @java.lang.Override
        public android.telecom.ParcelableCallAnalytics[] newArray(int size) {
            return new android.telecom.ParcelableCallAnalytics[size];
        }
    };

    // The start time of the call in milliseconds since Jan. 1, 1970, rounded to the nearest
    // 5 minute increment.
    private final long startTimeMillis;

    // The duration of the call, in milliseconds.
    private final long callDurationMillis;

    // ONE OF calltype_unknown, calltype_incoming, or calltype_outgoing
    private final int callType;

    // true if the call came in while another call was in progress or if the user dialed this call
    // while in the middle of another call.
    private final boolean isAdditionalCall;

    // true if the call was interrupted by an incoming or outgoing call.
    private final boolean isInterrupted;

    // bitmask denoting which technologies a call used.
    private final int callTechnologies;

    // Any of the DisconnectCause codes, or STILL_CONNECTED.
    private final int callTerminationCode;

    // Whether the call is an emergency call
    private final boolean isEmergencyCall;

    // The package name of the connection service that this call used.
    private final java.lang.String connectionService;

    // Whether the call object was created from an existing connection.
    private final boolean isCreatedFromExistingConnection;

    // A list of events that are associated with this call
    private final java.util.List<android.telecom.ParcelableCallAnalytics.AnalyticsEvent> analyticsEvents;

    // A map from event-pair names to their durations.
    private final java.util.List<android.telecom.ParcelableCallAnalytics.EventTiming> eventTimings;

    // Whether the call has ever been a video call.
    private boolean isVideoCall = false;

    // A list of video events that have occurred.
    private java.util.List<android.telecom.ParcelableCallAnalytics.VideoEvent> videoEvents;

    public ParcelableCallAnalytics(long startTimeMillis, long callDurationMillis, int callType, boolean isAdditionalCall, boolean isInterrupted, int callTechnologies, int callTerminationCode, boolean isEmergencyCall, java.lang.String connectionService, boolean isCreatedFromExistingConnection, java.util.List<android.telecom.ParcelableCallAnalytics.AnalyticsEvent> analyticsEvents, java.util.List<android.telecom.ParcelableCallAnalytics.EventTiming> eventTimings) {
        this.startTimeMillis = startTimeMillis;
        this.callDurationMillis = callDurationMillis;
        this.callType = callType;
        this.isAdditionalCall = isAdditionalCall;
        this.isInterrupted = isInterrupted;
        this.callTechnologies = callTechnologies;
        this.callTerminationCode = callTerminationCode;
        this.isEmergencyCall = isEmergencyCall;
        this.connectionService = connectionService;
        this.isCreatedFromExistingConnection = isCreatedFromExistingConnection;
        this.analyticsEvents = analyticsEvents;
        this.eventTimings = eventTimings;
    }

    public ParcelableCallAnalytics(android.os.Parcel in) {
        startTimeMillis = in.readLong();
        callDurationMillis = in.readLong();
        callType = in.readInt();
        isAdditionalCall = android.telecom.ParcelableCallAnalytics.readByteAsBoolean(in);
        isInterrupted = android.telecom.ParcelableCallAnalytics.readByteAsBoolean(in);
        callTechnologies = in.readInt();
        callTerminationCode = in.readInt();
        isEmergencyCall = android.telecom.ParcelableCallAnalytics.readByteAsBoolean(in);
        connectionService = in.readString();
        isCreatedFromExistingConnection = android.telecom.ParcelableCallAnalytics.readByteAsBoolean(in);
        analyticsEvents = new java.util.ArrayList<>();
        in.readTypedList(analyticsEvents, android.telecom.ParcelableCallAnalytics.AnalyticsEvent.CREATOR);
        eventTimings = new java.util.ArrayList<>();
        in.readTypedList(eventTimings, android.telecom.ParcelableCallAnalytics.EventTiming.CREATOR);
        isVideoCall = android.telecom.ParcelableCallAnalytics.readByteAsBoolean(in);
        videoEvents = new java.util.LinkedList<>();
        in.readTypedList(videoEvents, android.telecom.ParcelableCallAnalytics.VideoEvent.CREATOR);
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(startTimeMillis);
        out.writeLong(callDurationMillis);
        out.writeInt(callType);
        android.telecom.ParcelableCallAnalytics.writeBooleanAsByte(out, isAdditionalCall);
        android.telecom.ParcelableCallAnalytics.writeBooleanAsByte(out, isInterrupted);
        out.writeInt(callTechnologies);
        out.writeInt(callTerminationCode);
        android.telecom.ParcelableCallAnalytics.writeBooleanAsByte(out, isEmergencyCall);
        out.writeString(connectionService);
        android.telecom.ParcelableCallAnalytics.writeBooleanAsByte(out, isCreatedFromExistingConnection);
        out.writeTypedList(analyticsEvents);
        out.writeTypedList(eventTimings);
        android.telecom.ParcelableCallAnalytics.writeBooleanAsByte(out, isVideoCall);
        out.writeTypedList(videoEvents);
    }

    /**
     * {@hide }
     */
    public void setIsVideoCall(boolean isVideoCall) {
        this.isVideoCall = isVideoCall;
    }

    /**
     * {@hide }
     */
    public void setVideoEvents(java.util.List<android.telecom.ParcelableCallAnalytics.VideoEvent> videoEvents) {
        this.videoEvents = videoEvents;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getCallDurationMillis() {
        return callDurationMillis;
    }

    public int getCallType() {
        return callType;
    }

    public boolean isAdditionalCall() {
        return isAdditionalCall;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public int getCallTechnologies() {
        return callTechnologies;
    }

    public int getCallTerminationCode() {
        return callTerminationCode;
    }

    public boolean isEmergencyCall() {
        return isEmergencyCall;
    }

    public java.lang.String getConnectionService() {
        return connectionService;
    }

    public boolean isCreatedFromExistingConnection() {
        return isCreatedFromExistingConnection;
    }

    public java.util.List<android.telecom.ParcelableCallAnalytics.AnalyticsEvent> analyticsEvents() {
        return analyticsEvents;
    }

    public java.util.List<android.telecom.ParcelableCallAnalytics.EventTiming> getEventTimings() {
        return eventTimings;
    }

    /**
     * {@hide }
     */
    public boolean isVideoCall() {
        return isVideoCall;
    }

    /**
     * {@hide }
     */
    public java.util.List<android.telecom.ParcelableCallAnalytics.VideoEvent> getVideoEvents() {
        return videoEvents;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    private static void writeBooleanAsByte(android.os.Parcel out, boolean b) {
        out.writeByte(((byte) (b ? 1 : 0)));
    }

    private static boolean readByteAsBoolean(android.os.Parcel in) {
        return in.readByte() == 1;
    }
}

