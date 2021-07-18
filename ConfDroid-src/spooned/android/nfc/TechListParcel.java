/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * limitations under the License
 */
package android.nfc;


/**
 *
 *
 * @unknown 
 */
public class TechListParcel implements android.os.Parcelable {
    private java.lang.String[][] mTechLists;

    public TechListParcel(java.lang.String[]... strings) {
        mTechLists = strings;
    }

    public java.lang.String[][] getTechLists() {
        return mTechLists;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        int count = mTechLists.length;
        dest.writeInt(count);
        for (int i = 0; i < count; i++) {
            java.lang.String[] techList = mTechLists[i];
            dest.writeStringArray(techList);
        }
    }

    public static final android.os.Parcelable.Creator<android.nfc.TechListParcel> CREATOR = new android.os.Parcelable.Creator<android.nfc.TechListParcel>() {
        @java.lang.Override
        public android.nfc.TechListParcel createFromParcel(android.os.Parcel source) {
            int count = source.readInt();
            java.lang.String[][] techLists = new java.lang.String[count][];
            for (int i = 0; i < count; i++) {
                techLists[i] = source.readStringArray();
            }
            return new android.nfc.TechListParcel(techLists);
        }

        @java.lang.Override
        public android.nfc.TechListParcel[] newArray(int size) {
            return new android.nfc.TechListParcel[size];
        }
    };
}

