/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * Description of permissions needed to access a particular path
 * in a {@link ProviderInfo}.
 */
public class PathPermission extends android.os.PatternMatcher {
    private final java.lang.String mReadPermission;

    private final java.lang.String mWritePermission;

    public PathPermission(java.lang.String pattern, int type, java.lang.String readPermission, java.lang.String writePermission) {
        super(pattern, type);
        mReadPermission = readPermission;
        mWritePermission = writePermission;
    }

    public java.lang.String getReadPermission() {
        return mReadPermission;
    }

    public java.lang.String getWritePermission() {
        return mWritePermission;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mReadPermission);
        dest.writeString(mWritePermission);
    }

    public PathPermission(android.os.Parcel src) {
        super(src);
        mReadPermission = src.readString();
        mWritePermission = src.readString();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.PathPermission> CREATOR = new android.os.Parcelable.Creator<android.content.pm.PathPermission>() {
        public android.content.pm.PathPermission createFromParcel(android.os.Parcel source) {
            return new android.content.pm.PathPermission(source);
        }

        public android.content.pm.PathPermission[] newArray(int size) {
            return new android.content.pm.PathPermission[size];
        }
    };
}

