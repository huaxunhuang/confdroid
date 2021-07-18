/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Describes the Results of a batched set of wifi scans where the firmware performs many
 * scans and stores the timestamped results without waking the main processor each time.
 *
 * @unknown 
 */
@java.lang.Deprecated
public class BatchedScanResult implements android.os.Parcelable {
    private static final java.lang.String TAG = "BatchedScanResult";

    /**
     * Inidcates this scan was interrupted and may only have partial results.
     */
    public boolean truncated;

    /**
     * The result of this particular scan.
     */
    public final java.util.List<android.net.wifi.ScanResult> scanResults = new java.util.ArrayList<android.net.wifi.ScanResult>();

    public BatchedScanResult() {
    }

    public BatchedScanResult(android.net.wifi.BatchedScanResult source) {
        truncated = source.truncated;
        for (android.net.wifi.ScanResult s : source.scanResults)
            scanResults.add(new android.net.wifi.ScanResult(s));

    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("BatchedScanResult: ").append("truncated: ").append(java.lang.String.valueOf(truncated)).append("scanResults: [");
        for (android.net.wifi.ScanResult s : scanResults) {
            sb.append(" <").append(s.toString()).append("> ");
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(truncated ? 1 : 0);
        dest.writeInt(scanResults.size());
        for (android.net.wifi.ScanResult s : scanResults) {
            s.writeToParcel(dest, flags);
        }
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.BatchedScanResult> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.BatchedScanResult>() {
        public android.net.wifi.BatchedScanResult createFromParcel(android.os.Parcel in) {
            android.net.wifi.BatchedScanResult result = new android.net.wifi.BatchedScanResult();
            result.truncated = in.readInt() == 1;
            int count = in.readInt();
            while ((count--) > 0) {
                result.scanResults.add(android.net.wifi.ScanResult.CREATOR.createFromParcel(in));
            } 
            return result;
        }

        public android.net.wifi.BatchedScanResult[] newArray(int size) {
            return new android.net.wifi.BatchedScanResult[size];
        }
    };
}

