/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.net.wifi;


/**
 * A class representing the result of a WPS request
 *
 * @unknown 
 */
public class WpsResult implements android.os.Parcelable {
    public enum Status {

        SUCCESS,
        FAILURE,
        IN_PROGRESS;}

    public android.net.wifi.WpsResult.Status status;

    public java.lang.String pin;

    public WpsResult() {
        status = android.net.wifi.WpsResult.Status.FAILURE;
        pin = null;
    }

    public WpsResult(android.net.wifi.WpsResult.Status s) {
        status = s;
        pin = null;
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        sbuf.append(" status: ").append(status.toString());
        sbuf.append('\n');
        sbuf.append(" pin: ").append(pin);
        sbuf.append("\n");
        return sbuf.toString();
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public int describeContents() {
        return 0;
    }

    /**
     * copy constructor {@hide }
     */
    public WpsResult(android.net.wifi.WpsResult source) {
        if (source != null) {
            status = source.status;
            pin = source.pin;
        }
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(status.name());
        dest.writeString(pin);
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.WpsResult> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.WpsResult>() {
        public android.net.wifi.WpsResult createFromParcel(android.os.Parcel in) {
            android.net.wifi.WpsResult result = new android.net.wifi.WpsResult();
            result.status = android.net.wifi.WpsResult.Status.valueOf(in.readString());
            result.pin = in.readString();
            return result;
        }

        public android.net.wifi.WpsResult[] newArray(int size) {
            return new android.net.wifi.WpsResult[size];
        }
    };
}

