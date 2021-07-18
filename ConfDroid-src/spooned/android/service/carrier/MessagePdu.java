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
package android.service.carrier;


/**
 * A parcelable list of PDUs representing contents of a possibly multi-part SMS.
 */
public final class MessagePdu implements android.os.Parcelable {
    private static final int NULL_LENGTH = -1;

    private final java.util.List<byte[]> mPduList;

    /**
     * Constructs a MessagePdu with the list of message PDUs.
     *
     * @param pduList
     * 		the list of message PDUs
     */
    public MessagePdu(@android.annotation.NonNull
    java.util.List<byte[]> pduList) {
        if ((pduList == null) || pduList.contains(null)) {
            throw new java.lang.IllegalArgumentException("pduList must not be null or contain nulls");
        }
        mPduList = pduList;
    }

    /**
     * Returns the contents of a possibly multi-part SMS.
     *
     * @return the list of PDUs
     */
    @android.annotation.NonNull
    public java.util.List<byte[]> getPdus() {
        return mPduList;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (mPduList == null) {
            dest.writeInt(android.service.carrier.MessagePdu.NULL_LENGTH);
        } else {
            dest.writeInt(mPduList.size());
            for (byte[] messagePdu : mPduList) {
                dest.writeByteArray(messagePdu);
            }
        }
    }

    /**
     * Constructs a {@link MessagePdu} from a {@link Parcel}.
     */
    public static final android.os.Parcelable.Creator<android.service.carrier.MessagePdu> CREATOR = new android.os.Parcelable.Creator<android.service.carrier.MessagePdu>() {
        @java.lang.Override
        public android.service.carrier.MessagePdu createFromParcel(android.os.Parcel source) {
            int size = source.readInt();
            java.util.List<byte[]> pduList;
            if (size == android.service.carrier.MessagePdu.NULL_LENGTH) {
                pduList = null;
            } else {
                pduList = new java.util.ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    pduList.add(source.createByteArray());
                }
            }
            return new android.service.carrier.MessagePdu(pduList);
        }

        @java.lang.Override
        public android.service.carrier.MessagePdu[] newArray(int size) {
            return new android.service.carrier.MessagePdu[size];
        }
    };
}

