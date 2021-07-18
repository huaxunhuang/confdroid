/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package android.net;


/**
 * Defines a request to register a callbacks. Used to be notified on data usage via
 * {@link android.app.usage.NetworkStatsManager#registerDataUsageCallback}.
 * If no {@code uid}s are set, callbacks are restricted to device-owners,
 * carrier-privileged apps, or system apps.
 *
 * @unknown 
 */
public final class DataUsageRequest implements android.os.Parcelable {
    public static final java.lang.String PARCELABLE_KEY = "DataUsageRequest";

    public static final int REQUEST_ID_UNSET = 0;

    /**
     * Identifies the request.  {@link DataUsageRequest}s should only be constructed by
     * the Framework and it is used internally to identify the request.
     */
    public final int requestId;

    /**
     * {@link NetworkTemplate} describing the network to monitor.
     */
    public final android.net.NetworkTemplate template;

    /**
     * Threshold in bytes to be notified on.
     */
    public final long thresholdInBytes;

    public DataUsageRequest(int requestId, android.net.NetworkTemplate template, long thresholdInBytes) {
        this.requestId = requestId;
        this.template = template;
        this.thresholdInBytes = thresholdInBytes;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(requestId);
        dest.writeParcelable(template, flags);
        dest.writeLong(thresholdInBytes);
    }

    public static final android.os.Parcelable.Creator<android.net.DataUsageRequest> CREATOR = new android.os.Parcelable.Creator<android.net.DataUsageRequest>() {
        @java.lang.Override
        public android.net.DataUsageRequest createFromParcel(android.os.Parcel in) {
            int requestId = in.readInt();
            android.net.NetworkTemplate template = in.readParcelable(null);
            long thresholdInBytes = in.readLong();
            android.net.DataUsageRequest result = new android.net.DataUsageRequest(requestId, template, thresholdInBytes);
            return result;
        }

        @java.lang.Override
        public android.net.DataUsageRequest[] newArray(int size) {
            return new android.net.DataUsageRequest[size];
        }
    };

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("DataUsageRequest [ requestId=" + requestId) + ", networkTemplate=") + template) + ", thresholdInBytes=") + thresholdInBytes) + " ]";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if ((obj instanceof android.net.DataUsageRequest) == false)
            return false;

        android.net.DataUsageRequest that = ((android.net.DataUsageRequest) (obj));
        return ((that.requestId == this.requestId) && java.util.Objects.equals(that.template, this.template)) && (that.thresholdInBytes == this.thresholdInBytes);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(requestId, template, thresholdInBytes);
    }
}

