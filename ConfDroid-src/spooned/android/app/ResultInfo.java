/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.app;


/**
 * {@hide }
 */
public class ResultInfo implements android.os.Parcelable {
    public final java.lang.String mResultWho;

    public final int mRequestCode;

    public final int mResultCode;

    public final android.content.Intent mData;

    public ResultInfo(java.lang.String resultWho, int requestCode, int resultCode, android.content.Intent data) {
        mResultWho = resultWho;
        mRequestCode = requestCode;
        mResultCode = resultCode;
        mData = data;
    }

    public java.lang.String toString() {
        return ((((((("ResultInfo{who=" + mResultWho) + ", request=") + mRequestCode) + ", result=") + mResultCode) + ", data=") + mData) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(mResultWho);
        out.writeInt(mRequestCode);
        out.writeInt(mResultCode);
        if (mData != null) {
            out.writeInt(1);
            mData.writeToParcel(out, 0);
        } else {
            out.writeInt(0);
        }
    }

    public static final android.os.Parcelable.Creator<android.app.ResultInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ResultInfo>() {
        public android.app.ResultInfo createFromParcel(android.os.Parcel in) {
            return new android.app.ResultInfo(in);
        }

        public android.app.ResultInfo[] newArray(int size) {
            return new android.app.ResultInfo[size];
        }
    };

    public ResultInfo(android.os.Parcel in) {
        mResultWho = in.readString();
        mRequestCode = in.readInt();
        mResultCode = in.readInt();
        if (in.readInt() != 0) {
            mData = android.content.Intent.CREATOR.createFromParcel(in);
        } else {
            mData = null;
        }
    }
}

