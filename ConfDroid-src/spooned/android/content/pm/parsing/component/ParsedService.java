/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm.parsing.component;


/**
 *
 *
 * @unknown *
 */
public class ParsedService extends android.content.pm.parsing.component.ParsedMainComponent {
    int foregroundServiceType;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String permission;

    public ParsedService(android.content.pm.parsing.component.ParsedService other) {
        super(other);
        this.foregroundServiceType = other.foregroundServiceType;
        this.permission = other.permission;
    }

    public android.content.pm.parsing.component.ParsedMainComponent setPermission(java.lang.String permission) {
        // Empty string must be converted to null
        this.permission = (android.text.TextUtils.isEmpty(permission)) ? null : permission.intern();
        return this;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Service{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        android.content.ComponentName.appendShortString(sb, getPackageName(), getName());
        sb.append('}');
        return sb.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.foregroundServiceType);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.permission, dest, flags);
    }

    public ParsedService() {
    }

    protected ParsedService(android.os.Parcel in) {
        super(in);
        this.foregroundServiceType = in.readInt();
        this.permission = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedService> CREATOR = new android.content.pm.parsing.component.Creator<android.content.pm.parsing.component.ParsedService>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedService createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedService(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedService[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedService[size];
        }
    };

    public int getForegroundServiceType() {
        return foregroundServiceType;
    }

    @android.annotation.Nullable
    public java.lang.String getPermission() {
        return permission;
    }
}

