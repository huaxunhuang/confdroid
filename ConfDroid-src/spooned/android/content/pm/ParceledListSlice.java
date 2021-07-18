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
public class ParceledListSlice<T extends android.os.Parcelable> extends android.content.pm.BaseParceledListSlice<T> {
    @android.annotation.UnsupportedAppUsage
    public ParceledListSlice(java.util.List<T> list) {
        super(list);
    }

    private ParceledListSlice(android.os.Parcel in, java.lang.ClassLoader loader) {
        super(in, loader);
    }

    public static <T extends android.os.Parcelable> android.content.pm.ParceledListSlice<T> emptyList() {
        return new android.content.pm.ParceledListSlice<T>(java.util.Collections.<T>emptyList());
    }

    @java.lang.Override
    public int describeContents() {
        int contents = 0;
        final java.util.List<T> list = getList();
        for (int i = 0; i < list.size(); i++) {
            contents |= describeContents();
        }
        return contents;
    }

    @java.lang.Override
    protected void writeElement(T parcelable, android.os.Parcel dest, int callFlags) {
        parcelable.writeToParcel(dest, callFlags);
    }

    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    protected void writeParcelableCreator(T parcelable, android.os.Parcel dest) {
        dest.writeParcelableCreator(((android.os.Parcelable) (parcelable)));
    }

    @java.lang.Override
    protected android.os.Parcelable.Creator<?> readParcelableCreator(android.os.Parcel from, java.lang.ClassLoader loader) {
        return from.readParcelableCreator(loader);
    }

    @java.lang.SuppressWarnings("unchecked")
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public static final android.os.Parcelable.ClassLoaderCreator<android.content.pm.ParceledListSlice> CREATOR = new android.os.Parcelable.ClassLoaderCreator<android.content.pm.ParceledListSlice>() {
        public android.content.pm.ParceledListSlice createFromParcel(android.os.Parcel in) {
            return new android.content.pm.ParceledListSlice(in, null);
        }

        @java.lang.Override
        public android.content.pm.ParceledListSlice createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
            return new android.content.pm.ParceledListSlice(in, loader);
        }

        @java.lang.Override
        public android.content.pm.ParceledListSlice[] newArray(int size) {
            return new android.content.pm.ParceledListSlice[size];
        }
    };
}

