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
 * Class for handling the parceling of return values from keymaster crypto operations
 * (begin/update/finish).
 *
 * @unknown 
 */
public class OperationResult implements android.os.Parcelable {
    public final int resultCode;

    public final android.os.IBinder token;

    public final long operationHandle;

    public final int inputConsumed;

    public final byte[] output;

    public final android.security.keymaster.KeymasterArguments outParams;

    public static final android.os.Parcelable.Creator<android.security.keymaster.OperationResult> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.OperationResult>() {
        @java.lang.Override
        public android.security.keymaster.OperationResult createFromParcel(android.os.Parcel in) {
            return new android.security.keymaster.OperationResult(in);
        }

        @java.lang.Override
        public android.security.keymaster.OperationResult[] newArray(int length) {
            return new android.security.keymaster.OperationResult[length];
        }
    };

    public OperationResult(int resultCode, android.os.IBinder token, long operationHandle, int inputConsumed, byte[] output, android.security.keymaster.KeymasterArguments outParams) {
        this.resultCode = resultCode;
        this.token = token;
        this.operationHandle = operationHandle;
        this.inputConsumed = inputConsumed;
        this.output = output;
        this.outParams = outParams;
    }

    protected OperationResult(android.os.Parcel in) {
        resultCode = in.readInt();
        token = in.readStrongBinder();
        operationHandle = in.readLong();
        inputConsumed = in.readInt();
        output = in.createByteArray();
        outParams = android.security.keymaster.KeymasterArguments.CREATOR.createFromParcel(in);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(resultCode);
        out.writeStrongBinder(token);
        out.writeLong(operationHandle);
        out.writeInt(inputConsumed);
        out.writeByteArray(output);
        outParams.writeToParcel(out, flags);
    }
}

