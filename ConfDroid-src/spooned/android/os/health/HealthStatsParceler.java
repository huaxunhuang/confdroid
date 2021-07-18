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
package android.os.health;


/**
 * Class to allow sending the HealthStats through aidl generated glue.
 *
 * The alternative would be to send a HealthStats object, which would
 * require constructing one, and then immediately flattening it. This
 * saves that step at the cost of doing the extra flattening when
 * accessed in the same process as the writer.
 *
 * The HealthStatsWriter passed in the constructor is retained, so don't
 * reuse them.
 *
 * @unknown 
 */
public class HealthStatsParceler implements android.os.Parcelable {
    private android.os.health.HealthStatsWriter mWriter;

    private android.os.health.HealthStats mHealthStats;

    public static final android.os.Parcelable.Creator<android.os.health.HealthStatsParceler> CREATOR = new android.os.Parcelable.Creator<android.os.health.HealthStatsParceler>() {
        public android.os.health.HealthStatsParceler createFromParcel(android.os.Parcel in) {
            return new android.os.health.HealthStatsParceler(in);
        }

        public android.os.health.HealthStatsParceler[] newArray(int size) {
            return new android.os.health.HealthStatsParceler[size];
        }
    };

    public HealthStatsParceler(android.os.health.HealthStatsWriter writer) {
        mWriter = writer;
    }

    public HealthStatsParceler(android.os.Parcel in) {
        mHealthStats = new android.os.health.HealthStats(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        // See comment on mWriter declaration above.
        if (mWriter != null) {
            mWriter.flattenToParcel(out);
        } else {
            throw new java.lang.RuntimeException("Can not re-parcel HealthStatsParceler that was" + " constructed from a Parcel");
        }
    }

    public android.os.health.HealthStats getHealthStats() {
        if (mWriter != null) {
            final android.os.Parcel parcel = android.os.Parcel.obtain();
            mWriter.flattenToParcel(parcel);
            parcel.setDataPosition(0);
            mHealthStats = new android.os.health.HealthStats(parcel);
            parcel.recycle();
        }
        return mHealthStats;
    }
}

