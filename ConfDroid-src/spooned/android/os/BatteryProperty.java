/**
 * Copyright 2013, The Android Open Source Project
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
package android.os;


/**
 * Battery properties that may be queried using
 * BatteryManager.getProperty()}
 */
/**
 *
 *
 * @unknown 
 */
public class BatteryProperty implements android.os.Parcelable {
    private long mValueLong;

    /**
     *
     *
     * @unknown 
     */
    public BatteryProperty() {
        mValueLong = java.lang.Long.MIN_VALUE;
    }

    /**
     *
     *
     * @unknown 
     */
    public long getLong() {
        return mValueLong;
    }

    /* Parcel read/write code must be kept in sync with
    frameworks/native/services/batteryservice/BatteryProperty.cpp
     */
    private BatteryProperty(android.os.Parcel p) {
        readFromParcel(p);
    }

    public void readFromParcel(android.os.Parcel p) {
        mValueLong = p.readLong();
    }

    public void writeToParcel(android.os.Parcel p, int flags) {
        p.writeLong(mValueLong);
    }

    public static final android.os.Parcelable.Creator<android.os.BatteryProperty> CREATOR = new android.os.Parcelable.Creator<android.os.BatteryProperty>() {
        public android.os.BatteryProperty createFromParcel(android.os.Parcel p) {
            return new android.os.BatteryProperty(p);
        }

        public android.os.BatteryProperty[] newArray(int size) {
            return new android.os.BatteryProperty[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}

