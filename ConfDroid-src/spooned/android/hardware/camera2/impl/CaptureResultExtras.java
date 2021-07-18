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
package android.hardware.camera2.impl;


/**
 *
 *
 * @unknown 
 */
public class CaptureResultExtras implements android.os.Parcelable {
    private int requestId;

    private int subsequenceId;

    private int afTriggerId;

    private int precaptureTriggerId;

    private long frameNumber;

    private int partialResultCount;

    private int errorStreamId;

    public static final android.os.Parcelable.Creator<android.hardware.camera2.impl.CaptureResultExtras> CREATOR = new android.os.Parcelable.Creator<android.hardware.camera2.impl.CaptureResultExtras>() {
        @java.lang.Override
        public android.hardware.camera2.impl.CaptureResultExtras createFromParcel(android.os.Parcel in) {
            return new android.hardware.camera2.impl.CaptureResultExtras(in);
        }

        @java.lang.Override
        public android.hardware.camera2.impl.CaptureResultExtras[] newArray(int size) {
            return new android.hardware.camera2.impl.CaptureResultExtras[size];
        }
    };

    private CaptureResultExtras(android.os.Parcel in) {
        readFromParcel(in);
    }

    public CaptureResultExtras(int requestId, int subsequenceId, int afTriggerId, int precaptureTriggerId, long frameNumber, int partialResultCount, int errorStreamId) {
        this.requestId = requestId;
        this.subsequenceId = subsequenceId;
        this.afTriggerId = afTriggerId;
        this.precaptureTriggerId = precaptureTriggerId;
        this.frameNumber = frameNumber;
        this.partialResultCount = partialResultCount;
        this.errorStreamId = errorStreamId;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(requestId);
        dest.writeInt(subsequenceId);
        dest.writeInt(afTriggerId);
        dest.writeInt(precaptureTriggerId);
        dest.writeLong(frameNumber);
        dest.writeInt(partialResultCount);
        dest.writeInt(errorStreamId);
    }

    public void readFromParcel(android.os.Parcel in) {
        requestId = in.readInt();
        subsequenceId = in.readInt();
        afTriggerId = in.readInt();
        precaptureTriggerId = in.readInt();
        frameNumber = in.readLong();
        partialResultCount = in.readInt();
        errorStreamId = in.readInt();
    }

    public int getRequestId() {
        return requestId;
    }

    public int getSubsequenceId() {
        return subsequenceId;
    }

    public int getAfTriggerId() {
        return afTriggerId;
    }

    public int getPrecaptureTriggerId() {
        return precaptureTriggerId;
    }

    public long getFrameNumber() {
        return frameNumber;
    }

    public int getPartialResultCount() {
        return partialResultCount;
    }

    public int getErrorStreamId() {
        return errorStreamId;
    }
}

