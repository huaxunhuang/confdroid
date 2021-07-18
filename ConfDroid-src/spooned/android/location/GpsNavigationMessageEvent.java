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
package android.location;


/**
 * A class implementing a container for data associated with a navigation message event.
 * Events are delivered to registered instances of {@link Listener}.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class GpsNavigationMessageEvent implements android.os.Parcelable {
    /**
     * The system does not support tracking of GPS Navigation Messages. This status will not change
     * in the future.
     */
    public static int STATUS_NOT_SUPPORTED = 0;

    /**
     * GPS Navigation Messages are successfully being tracked, it will receive updates once they are
     * available.
     */
    public static int STATUS_READY = 1;

    /**
     * GPS provider or Location is disabled, updated will not be received until they are enabled.
     */
    public static int STATUS_GPS_LOCATION_DISABLED = 2;

    private final android.location.GpsNavigationMessage mNavigationMessage;

    /**
     * Used for receiving GPS satellite Navigation Messages from the GPS engine.
     * You can implement this interface and call
     * {@link LocationManager#addGpsNavigationMessageListener}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public interface Listener {
        /**
         * Returns the latest collected GPS Navigation Message.
         */
        void onGpsNavigationMessageReceived(android.location.GpsNavigationMessageEvent event);

        /**
         * Returns the latest status of the GPS Navigation Messages sub-system.
         */
        void onStatusChanged(int status);
    }

    public GpsNavigationMessageEvent(android.location.GpsNavigationMessage message) {
        if (message == null) {
            throw new java.security.InvalidParameterException("Parameter 'message' must not be null.");
        }
        mNavigationMessage = message;
    }

    @android.annotation.NonNull
    public android.location.GpsNavigationMessage getNavigationMessage() {
        return mNavigationMessage;
    }

    public static final android.os.Parcelable.Creator<android.location.GpsNavigationMessageEvent> CREATOR = new android.os.Parcelable.Creator<android.location.GpsNavigationMessageEvent>() {
        @java.lang.Override
        public android.location.GpsNavigationMessageEvent createFromParcel(android.os.Parcel in) {
            java.lang.ClassLoader classLoader = getClass().getClassLoader();
            android.location.GpsNavigationMessage navigationMessage = in.readParcelable(classLoader);
            return new android.location.GpsNavigationMessageEvent(navigationMessage);
        }

        @java.lang.Override
        public android.location.GpsNavigationMessageEvent[] newArray(int size) {
            return new android.location.GpsNavigationMessageEvent[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mNavigationMessage, flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("[ GpsNavigationMessageEvent:\n\n");
        builder.append(mNavigationMessage.toString());
        builder.append("\n]");
        return builder.toString();
    }
}

