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
package android.service.gatekeeper;


/**
 * Response object for a GateKeeper verification request.
 *
 * @unknown 
 */
public final class GateKeeperResponse implements android.os.Parcelable {
    public static final int RESPONSE_ERROR = -1;

    public static final int RESPONSE_OK = 0;

    public static final int RESPONSE_RETRY = 1;

    private final int mResponseCode;

    private int mTimeout;

    private byte[] mPayload;

    private boolean mShouldReEnroll;

    private GateKeeperResponse(int responseCode) {
        mResponseCode = responseCode;
    }

    private GateKeeperResponse(int responseCode, int timeout) {
        mResponseCode = responseCode;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.service.gatekeeper.GateKeeperResponse> CREATOR = new android.os.Parcelable.Creator<android.service.gatekeeper.GateKeeperResponse>() {
        @java.lang.Override
        public android.service.gatekeeper.GateKeeperResponse createFromParcel(android.os.Parcel source) {
            int responseCode = source.readInt();
            android.service.gatekeeper.GateKeeperResponse response = new android.service.gatekeeper.GateKeeperResponse(responseCode);
            if (responseCode == android.service.gatekeeper.GateKeeperResponse.RESPONSE_RETRY) {
                response.setTimeout(source.readInt());
            } else
                if (responseCode == android.service.gatekeeper.GateKeeperResponse.RESPONSE_OK) {
                    response.setShouldReEnroll(source.readInt() == 1);
                    int size = source.readInt();
                    if (size > 0) {
                        byte[] payload = new byte[size];
                        source.readByteArray(payload);
                        response.setPayload(payload);
                    }
                }

            return response;
        }

        @java.lang.Override
        public android.service.gatekeeper.GateKeeperResponse[] newArray(int size) {
            return new android.service.gatekeeper.GateKeeperResponse[size];
        }
    };

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mResponseCode);
        if (mResponseCode == android.service.gatekeeper.GateKeeperResponse.RESPONSE_RETRY) {
            dest.writeInt(mTimeout);
        } else
            if (mResponseCode == android.service.gatekeeper.GateKeeperResponse.RESPONSE_OK) {
                dest.writeInt(mShouldReEnroll ? 1 : 0);
                if (mPayload != null) {
                    dest.writeInt(mPayload.length);
                    dest.writeByteArray(mPayload);
                }
            }

    }

    public byte[] getPayload() {
        return mPayload;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public boolean getShouldReEnroll() {
        return mShouldReEnroll;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    private void setTimeout(int timeout) {
        mTimeout = timeout;
    }

    private void setShouldReEnroll(boolean shouldReEnroll) {
        mShouldReEnroll = shouldReEnroll;
    }

    private void setPayload(byte[] payload) {
        mPayload = payload;
    }
}

