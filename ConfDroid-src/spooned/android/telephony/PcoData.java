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
package android.telephony;


/**
 * Contains Carrier-specific (and opaque) Protocol configuration Option
 * Data.  In general this is only passed on to carrier-specific applications
 * for interpretation.
 *
 * @unknown 
 */
public class PcoData implements android.os.Parcelable {
    public final int cid;

    public final java.lang.String bearerProto;

    public final int pcoId;

    public final byte[] contents;

    public PcoData(int cid, java.lang.String bearerProto, int pcoId, byte[] contents) {
        this.cid = cid;
        this.bearerProto = bearerProto;
        this.pcoId = pcoId;
        this.contents = contents;
    }

    public PcoData(android.os.Parcel in) {
        cid = in.readInt();
        bearerProto = in.readString();
        pcoId = in.readInt();
        contents = in.createByteArray();
    }

    /**
     * {@link Parcelable#writeToParcel}
     */
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(cid);
        out.writeString(bearerProto);
        out.writeInt(pcoId);
        out.writeByteArray(contents);
    }

    /**
     * {@link Parcelable#describeContents}
     */
    public int describeContents() {
        return 0;
    }

    /**
     * {@link Parcelable.Creator}
     *
     * @unknown 
     */
    public static final android.os.Parcelable.Creator<android.telephony.PcoData> CREATOR = new android.os.Parcelable.Creator() {
        public android.telephony.PcoData createFromParcel(android.os.Parcel in) {
            return new android.telephony.PcoData(in);
        }

        public android.telephony.PcoData[] newArray(int size) {
            return new android.telephony.PcoData[size];
        }
    };

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((("PcoData(" + cid) + ", ") + bearerProto) + ", ") + pcoId) + ", contents[") + contents.length) + "])";
    }
}

