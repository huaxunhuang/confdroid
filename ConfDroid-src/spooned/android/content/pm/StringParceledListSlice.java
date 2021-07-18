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
package android.content.pm;


/**
 * Transfer a large list of Parcelable objects across an IPC.  Splits into
 * multiple transactions if needed.
 *
 * @see BaseParceledListSlice
 * @unknown 
 */
public class StringParceledListSlice extends android.content.pm.BaseParceledListSlice<java.lang.String> {
    public StringParceledListSlice(java.util.List<java.lang.String> list) {
        super(list);
    }

    private StringParceledListSlice(android.os.Parcel in, java.lang.ClassLoader loader) {
        super(in, loader);
    }

    public static android.content.pm.StringParceledListSlice emptyList() {
        return new android.content.pm.StringParceledListSlice(java.util.Collections.<java.lang.String>emptyList());
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    protected void writeElement(java.lang.String parcelable, android.os.Parcel reply, int callFlags) {
        reply.writeString(parcelable);
    }

    @java.lang.Override
    protected void writeParcelableCreator(java.lang.String parcelable, android.os.Parcel dest) {
        return;
    }

    @java.lang.Override
    protected android.os.Parcelable.Creator<?> readParcelableCreator(android.os.Parcel from, java.lang.ClassLoader loader) {
        return android.os.Parcel.STRING_CREATOR;
    }

    @java.lang.SuppressWarnings("unchecked")
    public static final android.os.Parcelable.ClassLoaderCreator<android.content.pm.StringParceledListSlice> CREATOR = new android.os.Parcelable.ClassLoaderCreator<android.content.pm.StringParceledListSlice>() {
        public android.content.pm.StringParceledListSlice createFromParcel(android.os.Parcel in) {
            return new android.content.pm.StringParceledListSlice(in, null);
        }

        @java.lang.Override
        public android.content.pm.StringParceledListSlice createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
            return new android.content.pm.StringParceledListSlice(in, loader);
        }

        @java.lang.Override
        public android.content.pm.StringParceledListSlice[] newArray(int size) {
            return new android.content.pm.StringParceledListSlice[size];
        }
    };
}

