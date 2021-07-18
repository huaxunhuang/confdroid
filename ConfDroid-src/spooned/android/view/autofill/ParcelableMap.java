/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.autofill;


/**
 * A parcelable HashMap for {@link AutofillId} and {@link AutofillValue}
 *
 * {@hide }
 */
class ParcelableMap extends java.util.HashMap<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> implements android.os.Parcelable {
    ParcelableMap(int size) {
        super(size);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(size());
        for (java.util.Map.Entry<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> entry : entrySet()) {
            dest.writeParcelable(entry.getKey(), 0);
            dest.writeParcelable(entry.getValue(), 0);
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.autofill.ParcelableMap> CREATOR = new android.os.Parcelable.Creator<android.view.autofill.ParcelableMap>() {
        @java.lang.Override
        public android.view.autofill.ParcelableMap createFromParcel(android.os.Parcel source) {
            int size = source.readInt();
            android.view.autofill.ParcelableMap map = new android.view.autofill.ParcelableMap(size);
            for (int i = 0; i < size; i++) {
                android.view.autofill.AutofillId key = source.readParcelable(null);
                android.view.autofill.AutofillValue value = source.readParcelable(null);
                map.put(key, value);
            }
            return map;
        }

        @java.lang.Override
        public android.view.autofill.ParcelableMap[] newArray(int size) {
            return new android.view.autofill.ParcelableMap[size];
        }
    };
}

