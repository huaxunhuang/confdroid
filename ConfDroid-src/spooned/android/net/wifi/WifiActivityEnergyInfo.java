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
package android.net.wifi;


/**
 * Record of energy and activity information from controller and
 * underlying wifi stack state. Timestamp the record with elapsed
 * real-time.
 *
 * @unknown 
 */
public final class WifiActivityEnergyInfo implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public long mTimestamp;

    /**
     *
     *
     * @unknown 
     */
    public int mStackState;

    /**
     *
     *
     * @unknown 
     */
    public long mControllerTxTimeMs;

    /**
     *
     *
     * @unknown 
     */
    public long[] mControllerTxTimePerLevelMs;

    /**
     *
     *
     * @unknown 
     */
    public long mControllerRxTimeMs;

    /**
     *
     *
     * @unknown 
     */
    public long mControllerIdleTimeMs;

    /**
     *
     *
     * @unknown 
     */
    public long mControllerEnergyUsed;

    public static final int STACK_STATE_INVALID = 0;

    public static final int STACK_STATE_STATE_ACTIVE = 1;

    public static final int STACK_STATE_STATE_SCANNING = 2;

    public static final int STACK_STATE_STATE_IDLE = 3;

    public WifiActivityEnergyInfo(long timestamp, int stackState, long txTime, long[] txTimePerLevel, long rxTime, long idleTime, long energyUsed) {
        mTimestamp = timestamp;
        mStackState = stackState;
        mControllerTxTimeMs = txTime;
        mControllerTxTimePerLevelMs = txTimePerLevel;
        mControllerRxTimeMs = rxTime;
        mControllerIdleTimeMs = idleTime;
        mControllerEnergyUsed = energyUsed;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((("WifiActivityEnergyInfo{" + " timestamp=") + mTimestamp) + " mStackState=") + mStackState) + " mControllerTxTimeMs=") + mControllerTxTimeMs) + " mControllerTxTimePerLevelMs=") + java.util.Arrays.toString(mControllerTxTimePerLevelMs)) + " mControllerRxTimeMs=") + mControllerRxTimeMs) + " mControllerIdleTimeMs=") + mControllerIdleTimeMs) + " mControllerEnergyUsed=") + mControllerEnergyUsed) + " }";
    }

    public static final android.os.Parcelable.Creator<android.net.wifi.WifiActivityEnergyInfo> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.WifiActivityEnergyInfo>() {
        public android.net.wifi.WifiActivityEnergyInfo createFromParcel(android.os.Parcel in) {
            long timestamp = in.readLong();
            int stackState = in.readInt();
            long txTime = in.readLong();
            long[] txTimePerLevel = in.createLongArray();
            long rxTime = in.readLong();
            long idleTime = in.readLong();
            long energyUsed = in.readLong();
            return new android.net.wifi.WifiActivityEnergyInfo(timestamp, stackState, txTime, txTimePerLevel, rxTime, idleTime, energyUsed);
        }

        public android.net.wifi.WifiActivityEnergyInfo[] newArray(int size) {
            return new android.net.wifi.WifiActivityEnergyInfo[size];
        }
    };

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(mTimestamp);
        out.writeInt(mStackState);
        out.writeLong(mControllerTxTimeMs);
        out.writeLongArray(mControllerTxTimePerLevelMs);
        out.writeLong(mControllerRxTimeMs);
        out.writeLong(mControllerIdleTimeMs);
        out.writeLong(mControllerEnergyUsed);
    }

    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @return bt stack reported state
     */
    public int getStackState() {
        return mStackState;
    }

    /**
     *
     *
     * @return tx time in ms
     */
    public long getControllerTxTimeMillis() {
        return mControllerTxTimeMs;
    }

    /**
     *
     *
     * @return tx time at power level provided in ms
     */
    public long getControllerTxTimeMillisAtLevel(int level) {
        if (level < mControllerTxTimePerLevelMs.length) {
            return mControllerTxTimePerLevelMs[level];
        }
        return 0;
    }

    /**
     *
     *
     * @return rx time in ms
     */
    public long getControllerRxTimeMillis() {
        return mControllerRxTimeMs;
    }

    /**
     *
     *
     * @return idle time in ms
     */
    public long getControllerIdleTimeMillis() {
        return mControllerIdleTimeMs;
    }

    /**
     * product of current(mA), voltage(V) and time(ms)
     *
     * @return energy used
     */
    public long getControllerEnergyUsed() {
        return mControllerEnergyUsed;
    }

    /**
     *
     *
     * @return timestamp(wall clock) of record creation
     */
    public long getTimeStamp() {
        return mTimestamp;
    }

    /**
     *
     *
     * @return if the record is valid
     */
    public boolean isValid() {
        return ((mControllerTxTimeMs >= 0) && (mControllerRxTimeMs >= 0)) && (mControllerIdleTimeMs >= 0);
    }
}

