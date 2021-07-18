/**
 * Copyright (c) 2015, The Android Open Source Project
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
package android.service.carrier;


/**
 * Used to pass info to CarrierConfigService implementations so they can decide what values to
 * return.
 */
public class CarrierIdentifier implements android.os.Parcelable {
    /**
     * Used to create a {@link CarrierIdentifier} from a {@link Parcel}.
     */
    public static final android.os.Parcelable.Creator<android.service.carrier.CarrierIdentifier> CREATOR = new android.os.Parcelable.Creator<android.service.carrier.CarrierIdentifier>() {
        @java.lang.Override
        public android.service.carrier.CarrierIdentifier createFromParcel(android.os.Parcel parcel) {
            return new android.service.carrier.CarrierIdentifier(parcel);
        }

        @java.lang.Override
        public android.service.carrier.CarrierIdentifier[] newArray(int i) {
            return new android.service.carrier.CarrierIdentifier[i];
        }
    };

    private java.lang.String mMcc;

    private java.lang.String mMnc;

    private java.lang.String mSpn;

    private java.lang.String mImsi;

    private java.lang.String mGid1;

    private java.lang.String mGid2;

    public CarrierIdentifier(java.lang.String mcc, java.lang.String mnc, java.lang.String spn, java.lang.String imsi, java.lang.String gid1, java.lang.String gid2) {
        mMcc = mcc;
        mMnc = mnc;
        mSpn = spn;
        mImsi = imsi;
        mGid1 = gid1;
        mGid2 = gid2;
    }

    /**
     *
     *
     * @unknown 
     */
    public CarrierIdentifier(android.os.Parcel parcel) {
        readFromParcel(parcel);
    }

    /**
     * Get the mobile country code.
     */
    public java.lang.String getMcc() {
        return mMcc;
    }

    /**
     * Get the mobile network code.
     */
    public java.lang.String getMnc() {
        return mMnc;
    }

    /**
     * Get the service provider name.
     */
    public java.lang.String getSpn() {
        return mSpn;
    }

    /**
     * Get the international mobile subscriber identity.
     */
    public java.lang.String getImsi() {
        return mImsi;
    }

    /**
     * Get the group identifier level 1.
     */
    public java.lang.String getGid1() {
        return mGid1;
    }

    /**
     * Get the group identifier level 2.
     */
    public java.lang.String getGid2() {
        return mGid2;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(mMcc);
        out.writeString(mMnc);
        out.writeString(mSpn);
        out.writeString(mImsi);
        out.writeString(mGid1);
        out.writeString(mGid2);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((("CarrierIdentifier{" + "mcc=") + mMcc) + ",mnc=") + mMnc) + ",spn=") + mSpn) + ",imsi=") + mImsi) + ",gid1=") + mGid1) + ",gid2=") + mGid2) + "}";
    }

    /**
     *
     *
     * @unknown 
     */
    public void readFromParcel(android.os.Parcel in) {
        mMcc = in.readString();
        mMnc = in.readString();
        mSpn = in.readString();
        mImsi = in.readString();
        mGid1 = in.readString();
        mGid2 = in.readString();
    }

    /**
     *
     *
     * @unknown 
     */
    public interface MatchType {
        int ALL = 0;

        int SPN = 1;

        int IMSI_PREFIX = 2;

        int GID1 = 3;

        int GID2 = 4;
    }
}

