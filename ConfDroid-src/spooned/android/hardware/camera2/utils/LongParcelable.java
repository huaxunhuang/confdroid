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
package android.hardware.camera2.utils;


/**
 *
 *
 * @unknown 
 */
public class LongParcelable implements android.os.Parcelable {
    private long number;

    public LongParcelable() {
        this.number = 0;
    }

    public LongParcelable(long number) {
        this.number = number;
    }

    public static final android.os.Parcelable.Creator<android.hardware.camera2.utils.LongParcelable> CREATOR = new android.os.Parcelable.Creator<android.hardware.camera2.utils.LongParcelable>() {
        @java.lang.Override
        public android.hardware.camera2.utils.LongParcelable createFromParcel(android.os.Parcel in) {
            return new android.hardware.camera2.utils.LongParcelable(in);
        }

        @java.lang.Override
        public android.hardware.camera2.utils.LongParcelable[] newArray(int size) {
            return new android.hardware.camera2.utils.LongParcelable[size];
        }
    };

    private LongParcelable(android.os.Parcel in) {
        readFromParcel(in);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(number);
    }

    public void readFromParcel(android.os.Parcel in) {
        number = in.readLong();
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}

