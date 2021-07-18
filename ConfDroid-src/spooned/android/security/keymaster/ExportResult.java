/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.security.keymaster;


/**
 * Class for handling parceling the return values from keymaster's export operation.
 *
 * @unknown 
 */
public class ExportResult implements android.os.Parcelable {
    public final int resultCode;

    public final byte[] exportData;

    public static final android.os.Parcelable.Creator<android.security.keymaster.ExportResult> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.ExportResult>() {
        public android.security.keymaster.ExportResult createFromParcel(android.os.Parcel in) {
            return new android.security.keymaster.ExportResult(in);
        }

        public android.security.keymaster.ExportResult[] newArray(int length) {
            return new android.security.keymaster.ExportResult[length];
        }
    };

    protected ExportResult(android.os.Parcel in) {
        resultCode = in.readInt();
        exportData = in.createByteArray();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(resultCode);
        out.writeByteArray(exportData);
    }
}

