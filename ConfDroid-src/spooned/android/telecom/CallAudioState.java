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
 * Encapsulates the telecom audio state, including the current audio routing, supported audio
 *  routing and mute.
 */
public final class CallAudioState implements android.os.Parcelable {
    /**
     * Direct the audio stream through the device's earpiece.
     */
    public static final int ROUTE_EARPIECE = 0x1;

    /**
     * Direct the audio stream through Bluetooth.
     */
    public static final int ROUTE_BLUETOOTH = 0x2;

    /**
     * Direct the audio stream through a wired headset.
     */
    public static final int ROUTE_WIRED_HEADSET = 0x4;

    /**
     * Direct the audio stream through the device's speakerphone.
     */
    public static final int ROUTE_SPEAKER = 0x8;

    /**
     * Direct the audio stream through the device's earpiece or wired headset if one is
     * connected.
     */
    public static final int ROUTE_WIRED_OR_EARPIECE = android.telecom.CallAudioState.ROUTE_EARPIECE | android.telecom.CallAudioState.ROUTE_WIRED_HEADSET;

    /**
     * Bit mask of all possible audio routes.
     */
    private static final int ROUTE_ALL = ((android.telecom.CallAudioState.ROUTE_EARPIECE | android.telecom.CallAudioState.ROUTE_BLUETOOTH) | android.telecom.CallAudioState.ROUTE_WIRED_HEADSET) | android.telecom.CallAudioState.ROUTE_SPEAKER;

    private final boolean isMuted;

    private final int route;

    private final int supportedRouteMask;

    /**
     * Constructor for a {@link CallAudioState} object.
     *
     * @param muted
     * 		{@code true} if the call is muted, {@code false} otherwise.
     * @param route
     * 		The current audio route being used.
     * 		Allowed values:
     * 		{@link #ROUTE_EARPIECE}
     * 		{@link #ROUTE_BLUETOOTH}
     * 		{@link #ROUTE_WIRED_HEADSET}
     * 		{@link #ROUTE_SPEAKER}
     * @param supportedRouteMask
     * 		Bit mask of all routes supported by this call. This should be a
     * 		bitwise combination of the following values:
     * 		{@link #ROUTE_EARPIECE}
     * 		{@link #ROUTE_BLUETOOTH}
     * 		{@link #ROUTE_WIRED_HEADSET}
     * 		{@link #ROUTE_SPEAKER}
     */
    public CallAudioState(boolean muted, int route, int supportedRouteMask) {
        this.isMuted = muted;
        this.route = route;
        this.supportedRouteMask = supportedRouteMask;
    }

    /**
     *
     *
     * @unknown 
     */
    public CallAudioState(android.telecom.CallAudioState state) {
        isMuted = state.isMuted();
        route = state.getRoute();
        supportedRouteMask = state.getSupportedRouteMask();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.SuppressWarnings("deprecation")
    public CallAudioState(android.telecom.AudioState state) {
        isMuted = state.isMuted();
        route = state.getRoute();
        supportedRouteMask = state.getSupportedRouteMask();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof android.telecom.CallAudioState)) {
            return false;
        }
        android.telecom.CallAudioState state = ((android.telecom.CallAudioState) (obj));
        return ((isMuted() == state.isMuted()) && (getRoute() == state.getRoute())) && (getSupportedRouteMask() == state.getSupportedRouteMask());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format(java.util.Locale.US, "[AudioState isMuted: %b, route: %s, supportedRouteMask: %s]", isMuted, android.telecom.CallAudioState.audioRouteToString(route), android.telecom.CallAudioState.audioRouteToString(supportedRouteMask));
    }

    /**
     *
     *
     * @return {@code true} if the call is muted, {@code false} otherwise.
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     *
     *
     * @return The current audio route being used.
     */
    public int getRoute() {
        return route;
    }

    /**
     *
     *
     * @return Bit mask of all routes supported by this call.
     */
    public int getSupportedRouteMask() {
        return supportedRouteMask;
    }

    /**
     * Converts the provided audio route into a human readable string representation.
     *
     * @param route
     * 		to convert into a string.
     * @return String representation of the provided audio route.
     */
    public static java.lang.String audioRouteToString(int route) {
        if ((route == 0) || ((route & (~android.telecom.CallAudioState.ROUTE_ALL)) != 0x0)) {
            return "UNKNOWN";
        }
        java.lang.StringBuffer buffer = new java.lang.StringBuffer();
        if ((route & android.telecom.CallAudioState.ROUTE_EARPIECE) == android.telecom.CallAudioState.ROUTE_EARPIECE) {
            android.telecom.CallAudioState.listAppend(buffer, "EARPIECE");
        }
        if ((route & android.telecom.CallAudioState.ROUTE_BLUETOOTH) == android.telecom.CallAudioState.ROUTE_BLUETOOTH) {
            android.telecom.CallAudioState.listAppend(buffer, "BLUETOOTH");
        }
        if ((route & android.telecom.CallAudioState.ROUTE_WIRED_HEADSET) == android.telecom.CallAudioState.ROUTE_WIRED_HEADSET) {
            android.telecom.CallAudioState.listAppend(buffer, "WIRED_HEADSET");
        }
        if ((route & android.telecom.CallAudioState.ROUTE_SPEAKER) == android.telecom.CallAudioState.ROUTE_SPEAKER) {
            android.telecom.CallAudioState.listAppend(buffer, "SPEAKER");
        }
        return buffer.toString();
    }

    /**
     * Responsible for creating AudioState objects for deserialized Parcels.
     */
    public static final android.os.Parcelable.Creator<android.telecom.CallAudioState> CREATOR = new android.os.Parcelable.Creator<android.telecom.CallAudioState>() {
        @java.lang.Override
        public android.telecom.CallAudioState createFromParcel(android.os.Parcel source) {
            boolean isMuted = (source.readByte() == 0) ? false : true;
            int route = source.readInt();
            int supportedRouteMask = source.readInt();
            return new android.telecom.CallAudioState(isMuted, route, supportedRouteMask);
        }

        @java.lang.Override
        public android.telecom.CallAudioState[] newArray(int size) {
            return new android.telecom.CallAudioState[size];
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
     * Writes AudioState object into a serializeable Parcel.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeByte(((byte) (isMuted ? 1 : 0)));
        destination.writeInt(route);
        destination.writeInt(supportedRouteMask);
    }

    private static void listAppend(java.lang.StringBuffer buffer, java.lang.String str) {
        if (buffer.length() > 0) {
            buffer.append(", ");
        }
        buffer.append(str);
    }
}

