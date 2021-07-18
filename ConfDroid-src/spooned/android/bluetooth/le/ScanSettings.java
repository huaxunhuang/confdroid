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
package android.bluetooth.le;


/**
 * Bluetooth LE scan settings are passed to {@link BluetoothLeScanner#startScan} to define the
 * parameters for the scan.
 */
public final class ScanSettings implements android.os.Parcelable {
    /**
     * A special Bluetooth LE scan mode. Applications using this scan mode will passively listen for
     * other scan results without starting BLE scans themselves.
     */
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;

    /**
     * Perform Bluetooth LE scan in low power mode. This is the default scan mode as it consumes the
     * least power.
     */
    public static final int SCAN_MODE_LOW_POWER = 0;

    /**
     * Perform Bluetooth LE scan in balanced power mode. Scan results are returned at a rate that
     * provides a good trade-off between scan frequency and power consumption.
     */
    public static final int SCAN_MODE_BALANCED = 1;

    /**
     * Scan using highest duty cycle. It's recommended to only use this mode when the application is
     * running in the foreground.
     */
    public static final int SCAN_MODE_LOW_LATENCY = 2;

    /**
     * Trigger a callback for every Bluetooth advertisement found that matches the filter criteria.
     * If no filter is active, all advertisement packets are reported.
     */
    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;

    /**
     * A result callback is only triggered for the first advertisement packet received that matches
     * the filter criteria.
     */
    public static final int CALLBACK_TYPE_FIRST_MATCH = 2;

    /**
     * Receive a callback when advertisements are no longer received from a device that has been
     * previously reported by a first match callback.
     */
    public static final int CALLBACK_TYPE_MATCH_LOST = 4;

    /**
     * Determines how many advertisements to match per filter, as this is scarce hw resource
     */
    /**
     * Match one advertisement per filter
     */
    public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;

    /**
     * Match few advertisement per filter, depends on current capability and availibility of
     * the resources in hw
     */
    public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;

    /**
     * Match as many advertisement per filter as hw could allow, depends on current
     * capability and availibility of the resources in hw
     */
    public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;

    /**
     * In Aggressive mode, hw will determine a match sooner even with feeble signal strength
     * and few number of sightings/match in a duration.
     */
    public static final int MATCH_MODE_AGGRESSIVE = 1;

    /**
     * For sticky mode, higher threshold of signal strength and sightings is required
     * before reporting by hw
     */
    public static final int MATCH_MODE_STICKY = 2;

    /**
     * Request full scan results which contain the device, rssi, advertising data, scan response
     * as well as the scan timestamp.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final int SCAN_RESULT_TYPE_FULL = 0;

    /**
     * Request abbreviated scan results which contain the device, rssi and scan timestamp.
     * <p>
     * <b>Note:</b> It is possible for an application to get more scan results than it asked for, if
     * there are multiple apps using this type.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final int SCAN_RESULT_TYPE_ABBREVIATED = 1;

    // Bluetooth LE scan mode.
    private int mScanMode;

    // Bluetooth LE scan callback type
    private int mCallbackType;

    // Bluetooth LE scan result type
    private int mScanResultType;

    // Time of delay for reporting the scan result
    private long mReportDelayMillis;

    private int mMatchMode;

    private int mNumOfMatchesPerFilter;

    public int getScanMode() {
        return mScanMode;
    }

    public int getCallbackType() {
        return mCallbackType;
    }

    public int getScanResultType() {
        return mScanResultType;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getMatchMode() {
        return mMatchMode;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getNumOfMatches() {
        return mNumOfMatchesPerFilter;
    }

    /**
     * Returns report delay timestamp based on the device clock.
     */
    public long getReportDelayMillis() {
        return mReportDelayMillis;
    }

    private ScanSettings(int scanMode, int callbackType, int scanResultType, long reportDelayMillis, int matchMode, int numOfMatchesPerFilter) {
        mScanMode = scanMode;
        mCallbackType = callbackType;
        mScanResultType = scanResultType;
        mReportDelayMillis = reportDelayMillis;
        mNumOfMatchesPerFilter = numOfMatchesPerFilter;
        mMatchMode = matchMode;
    }

    private ScanSettings(android.os.Parcel in) {
        mScanMode = in.readInt();
        mCallbackType = in.readInt();
        mScanResultType = in.readInt();
        mReportDelayMillis = in.readLong();
        mMatchMode = in.readInt();
        mNumOfMatchesPerFilter = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mScanMode);
        dest.writeInt(mCallbackType);
        dest.writeInt(mScanResultType);
        dest.writeLong(mReportDelayMillis);
        dest.writeInt(mMatchMode);
        dest.writeInt(mNumOfMatchesPerFilter);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.le.ScanSettings> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.le.ScanSettings>() {
        @java.lang.Override
        public android.bluetooth.le.ScanSettings[] newArray(int size) {
            return new android.bluetooth.le.ScanSettings[size];
        }

        @java.lang.Override
        public android.bluetooth.le.ScanSettings createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.le.ScanSettings(in);
        }
    };

    /**
     * Builder for {@link ScanSettings}.
     */
    public static final class Builder {
        private int mScanMode = android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;

        private int mCallbackType = android.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES;

        private int mScanResultType = android.bluetooth.le.ScanSettings.SCAN_RESULT_TYPE_FULL;

        private long mReportDelayMillis = 0;

        private int mMatchMode = android.bluetooth.le.ScanSettings.MATCH_MODE_AGGRESSIVE;

        private int mNumOfMatchesPerFilter = android.bluetooth.le.ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT;

        /**
         * Set scan mode for Bluetooth LE scan.
         *
         * @param scanMode
         * 		The scan mode can be one of {@link ScanSettings#SCAN_MODE_LOW_POWER},
         * 		{@link ScanSettings#SCAN_MODE_BALANCED} or
         * 		{@link ScanSettings#SCAN_MODE_LOW_LATENCY}.
         * @throws IllegalArgumentException
         * 		If the {@code scanMode} is invalid.
         */
        public android.bluetooth.le.ScanSettings.Builder setScanMode(int scanMode) {
            if ((scanMode < android.bluetooth.le.ScanSettings.SCAN_MODE_OPPORTUNISTIC) || (scanMode > android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY)) {
                throw new java.lang.IllegalArgumentException("invalid scan mode " + scanMode);
            }
            mScanMode = scanMode;
            return this;
        }

        /**
         * Set callback type for Bluetooth LE scan.
         *
         * @param callbackType
         * 		The callback type flags for the scan.
         * @throws IllegalArgumentException
         * 		If the {@code callbackType} is invalid.
         */
        public android.bluetooth.le.ScanSettings.Builder setCallbackType(int callbackType) {
            if (!isValidCallbackType(callbackType)) {
                throw new java.lang.IllegalArgumentException("invalid callback type - " + callbackType);
            }
            mCallbackType = callbackType;
            return this;
        }

        // Returns true if the callbackType is valid.
        private boolean isValidCallbackType(int callbackType) {
            if (((callbackType == android.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES) || (callbackType == android.bluetooth.le.ScanSettings.CALLBACK_TYPE_FIRST_MATCH)) || (callbackType == android.bluetooth.le.ScanSettings.CALLBACK_TYPE_MATCH_LOST)) {
                return true;
            }
            return callbackType == (android.bluetooth.le.ScanSettings.CALLBACK_TYPE_FIRST_MATCH | android.bluetooth.le.ScanSettings.CALLBACK_TYPE_MATCH_LOST);
        }

        /**
         * Set scan result type for Bluetooth LE scan.
         *
         * @param scanResultType
         * 		Type for scan result, could be either
         * 		{@link ScanSettings#SCAN_RESULT_TYPE_FULL} or
         * 		{@link ScanSettings#SCAN_RESULT_TYPE_ABBREVIATED}.
         * @throws IllegalArgumentException
         * 		If the {@code scanResultType} is invalid.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.bluetooth.le.ScanSettings.Builder setScanResultType(int scanResultType) {
            if ((scanResultType < android.bluetooth.le.ScanSettings.SCAN_RESULT_TYPE_FULL) || (scanResultType > android.bluetooth.le.ScanSettings.SCAN_RESULT_TYPE_ABBREVIATED)) {
                throw new java.lang.IllegalArgumentException("invalid scanResultType - " + scanResultType);
            }
            mScanResultType = scanResultType;
            return this;
        }

        /**
         * Set report delay timestamp for Bluetooth LE scan.
         *
         * @param reportDelayMillis
         * 		Delay of report in milliseconds. Set to 0 to be notified of
         * 		results immediately. Values &gt; 0 causes the scan results to be queued up and
         * 		delivered after the requested delay or when the internal buffers fill up.
         * @throws IllegalArgumentException
         * 		If {@code reportDelayMillis} &lt; 0.
         */
        public android.bluetooth.le.ScanSettings.Builder setReportDelay(long reportDelayMillis) {
            if (reportDelayMillis < 0) {
                throw new java.lang.IllegalArgumentException("reportDelay must be > 0");
            }
            mReportDelayMillis = reportDelayMillis;
            return this;
        }

        /**
         * Set the number of matches for Bluetooth LE scan filters hardware match
         *
         * @param numOfMatches
         * 		The num of matches can be one of
         * 		{@link ScanSettings#MATCH_NUM_ONE_ADVERTISEMENT} or
         * 		{@link ScanSettings#MATCH_NUM_FEW_ADVERTISEMENT} or
         * 		{@link ScanSettings#MATCH_NUM_MAX_ADVERTISEMENT}
         * @throws IllegalArgumentException
         * 		If the {@code matchMode} is invalid.
         */
        public android.bluetooth.le.ScanSettings.Builder setNumOfMatches(int numOfMatches) {
            if ((numOfMatches < android.bluetooth.le.ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT) || (numOfMatches > android.bluetooth.le.ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)) {
                throw new java.lang.IllegalArgumentException("invalid numOfMatches " + numOfMatches);
            }
            mNumOfMatchesPerFilter = numOfMatches;
            return this;
        }

        /**
         * Set match mode for Bluetooth LE scan filters hardware match
         *
         * @param matchMode
         * 		The match mode can be one of
         * 		{@link ScanSettings#MATCH_MODE_AGGRESSIVE} or
         * 		{@link ScanSettings#MATCH_MODE_STICKY}
         * @throws IllegalArgumentException
         * 		If the {@code matchMode} is invalid.
         */
        public android.bluetooth.le.ScanSettings.Builder setMatchMode(int matchMode) {
            if ((matchMode < android.bluetooth.le.ScanSettings.MATCH_MODE_AGGRESSIVE) || (matchMode > android.bluetooth.le.ScanSettings.MATCH_MODE_STICKY)) {
                throw new java.lang.IllegalArgumentException("invalid matchMode " + matchMode);
            }
            mMatchMode = matchMode;
            return this;
        }

        /**
         * Build {@link ScanSettings}.
         */
        public android.bluetooth.le.ScanSettings build() {
            return new android.bluetooth.le.ScanSettings(mScanMode, mCallbackType, mScanResultType, mReportDelayMillis, mMatchMode, mNumOfMatchesPerFilter);
        }
    }
}

