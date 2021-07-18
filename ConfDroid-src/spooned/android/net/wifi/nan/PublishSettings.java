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
 * limitations under the License.
 */
package android.net.wifi.nan;


/**
 * Defines the settings (configuration) for a NAN publish session. Built using
 * {@link PublishSettings.Builder}. Publish is done using
 * {@link WifiNanManager#publish(PublishData, PublishSettings, WifiNanSessionListener, int)}
 * or {@link WifiNanPublishSession#publish(PublishData, PublishSettings)}.
 *
 * @unknown PROPOSED_NAN_API
 */
public class PublishSettings implements android.os.Parcelable {
    /**
     * Defines an unsolicited publish session - i.e. a publish session where
     * publish packets are transmitted over-the-air. Configuration is done using
     * {@link PublishSettings.Builder#setPublishType(int)}.
     */
    public static final int PUBLISH_TYPE_UNSOLICITED = 0;

    /**
     * Defines a solicited publish session - i.e. a publish session where
     * publish packets are not transmitted over-the-air and the device listens
     * and matches to transmitted subscribe packets. Configuration is done using
     * {@link PublishSettings.Builder#setPublishType(int)}.
     */
    public static final int PUBLISH_TYPE_SOLICITED = 1;

    /**
     *
     *
     * @unknown 
     */
    public final int mPublishType;

    /**
     *
     *
     * @unknown 
     */
    public final int mPublishCount;

    /**
     *
     *
     * @unknown 
     */
    public final int mTtlSec;

    private PublishSettings(int publishType, int publichCount, int ttlSec) {
        mPublishType = publishType;
        mPublishCount = publichCount;
        mTtlSec = ttlSec;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("PublishSettings [mPublishType=" + mPublishType) + ", mPublishCount=") + mPublishCount) + ", mTtlSec=") + mTtlSec) + "]";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mPublishType);
        dest.writeInt(mPublishCount);
        dest.writeInt(mTtlSec);
    }

    public static final android.os.Parcelable.Creator<android.net.wifi.nan.PublishSettings> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.nan.PublishSettings>() {
        @java.lang.Override
        public android.net.wifi.nan.PublishSettings[] newArray(int size) {
            return new android.net.wifi.nan.PublishSettings[size];
        }

        @java.lang.Override
        public android.net.wifi.nan.PublishSettings createFromParcel(android.os.Parcel in) {
            int publishType = in.readInt();
            int publishCount = in.readInt();
            int ttlSec = in.readInt();
            return new android.net.wifi.nan.PublishSettings(publishType, publishCount, ttlSec);
        }
    };

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof android.net.wifi.nan.PublishSettings)) {
            return false;
        }
        android.net.wifi.nan.PublishSettings lhs = ((android.net.wifi.nan.PublishSettings) (o));
        return ((mPublishType == lhs.mPublishType) && (mPublishCount == lhs.mPublishCount)) && (mTtlSec == lhs.mTtlSec);
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + mPublishType;
        result = (31 * result) + mPublishCount;
        result = (31 * result) + mTtlSec;
        return result;
    }

    /**
     * Builder used to build {@link PublishSettings} objects.
     */
    public static final class Builder {
        int mPublishType;

        int mPublishCount;

        int mTtlSec;

        /**
         * Sets the type of the publish session: solicited (aka active - publish
         * packets are transmitted over-the-air), or unsolicited (aka passive -
         * no publish packets are transmitted, a match is made against an active
         * subscribe session whose packets are transmitted over-the-air).
         *
         * @param publishType
         * 		Publish session type: solicited (
         * 		{@link PublishSettings#PUBLISH_TYPE_SOLICITED}) or
         * 		unsolicited (
         * 		{@link PublishSettings#PUBLISH_TYPE_UNSOLICITED}).
         * @return The builder to facilitate chaining
        {@code builder.setXXX(..).setXXX(..)}.
         */
        public android.net.wifi.nan.PublishSettings.Builder setPublishType(int publishType) {
            if ((publishType < android.net.wifi.nan.PublishSettings.PUBLISH_TYPE_UNSOLICITED) || (publishType > android.net.wifi.nan.PublishSettings.PUBLISH_TYPE_SOLICITED)) {
                throw new java.lang.IllegalArgumentException("Invalid publishType - " + publishType);
            }
            mPublishType = publishType;
            return this;
        }

        /**
         * Sets the number of times a solicited (
         * {@link PublishSettings.Builder#setPublishType(int)}) publish session
         * will transmit a packet. When the count is reached an event will be
         * generated for {@link WifiNanSessionListener#onPublishTerminated(int)}
         * with reason={@link WifiNanSessionListener#TERMINATE_REASON_DONE}.
         *
         * @param publishCount
         * 		Number of publish packets to transmit.
         * @return The builder to facilitate chaining
        {@code builder.setXXX(..).setXXX(..)}.
         */
        public android.net.wifi.nan.PublishSettings.Builder setPublishCount(int publishCount) {
            if (publishCount < 0) {
                throw new java.lang.IllegalArgumentException("Invalid publishCount - must be non-negative");
            }
            mPublishCount = publishCount;
            return this;
        }

        /**
         * Sets the time interval (in seconds) a solicited (
         * {@link PublishSettings.Builder#setPublishCount(int)}) publish session
         * will be alive - i.e. transmitting a packet. When the TTL is reached
         * an event will be generated for
         * {@link WifiNanSessionListener#onPublishTerminated(int)} with reason=
         * {@link WifiNanSessionListener#TERMINATE_REASON_DONE}.
         *
         * @param ttlSec
         * 		Lifetime of a publish session in seconds.
         * @return The builder to facilitate chaining
        {@code builder.setXXX(..).setXXX(..)}.
         */
        public android.net.wifi.nan.PublishSettings.Builder setTtlSec(int ttlSec) {
            if (ttlSec < 0) {
                throw new java.lang.IllegalArgumentException("Invalid ttlSec - must be non-negative");
            }
            mTtlSec = ttlSec;
            return this;
        }

        /**
         * Build {@link PublishSettings} given the current requests made on the
         * builder.
         */
        public android.net.wifi.nan.PublishSettings build() {
            return new android.net.wifi.nan.PublishSettings(mPublishType, mPublishCount, mTtlSec);
        }
    }
}

