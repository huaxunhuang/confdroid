/**
 * Copyright (C) 2011, The Android Open Source Project
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
package android.nfc;


/**
 * Class used to pipe transceive result from the NFC service.
 *
 * @unknown 
 */
public final class TransceiveResult implements android.os.Parcelable {
    public static final int RESULT_SUCCESS = 0;

    public static final int RESULT_FAILURE = 1;

    public static final int RESULT_TAGLOST = 2;

    public static final int RESULT_EXCEEDED_LENGTH = 3;

    final int mResult;

    final byte[] mResponseData;

    public TransceiveResult(final int result, final byte[] data) {
        mResult = result;
        mResponseData = data;
    }

    public byte[] getResponseOrThrow() throws java.io.IOException {
        switch (mResult) {
            case android.nfc.TransceiveResult.RESULT_SUCCESS :
                return mResponseData;
            case android.nfc.TransceiveResult.RESULT_TAGLOST :
                throw new android.nfc.TagLostException("Tag was lost.");
            case android.nfc.TransceiveResult.RESULT_EXCEEDED_LENGTH :
                throw new java.io.IOException("Transceive length exceeds supported maximum");
            default :
                throw new java.io.IOException("Transceive failed");
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mResult);
        if (mResult == android.nfc.TransceiveResult.RESULT_SUCCESS) {
            dest.writeInt(mResponseData.length);
            dest.writeByteArray(mResponseData);
        }
    }

    public static final android.os.Parcelable.Creator<android.nfc.TransceiveResult> CREATOR = new android.os.Parcelable.Creator<android.nfc.TransceiveResult>() {
        @java.lang.Override
        public android.nfc.TransceiveResult createFromParcel(android.os.Parcel in) {
            int result = in.readInt();
            byte[] responseData;
            if (result == android.nfc.TransceiveResult.RESULT_SUCCESS) {
                int responseLength = in.readInt();
                responseData = new byte[responseLength];
                in.readByteArray(responseData);
            } else {
                responseData = null;
            }
            return new android.nfc.TransceiveResult(result, responseData);
        }

        @java.lang.Override
        public android.nfc.TransceiveResult[] newArray(int size) {
            return new android.nfc.TransceiveResult[size];
        }
    };
}

