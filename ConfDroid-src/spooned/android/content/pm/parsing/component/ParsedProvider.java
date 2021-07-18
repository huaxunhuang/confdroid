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
public class ParsedProvider extends android.content.pm.parsing.component.ParsedMainComponent {
    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String authority;

    boolean syncable;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String readPermission;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String writePermission;

    boolean grantUriPermissions;

    boolean forceUriPermissions;

    boolean multiProcess;

    int initOrder;

    @android.annotation.Nullable
    android.os.PatternMatcher[] uriPermissionPatterns;

    @android.annotation.Nullable
    android.content.pm.PathPermission[] pathPermissions;

    public ParsedProvider(android.content.pm.parsing.component.ParsedProvider other) {
        super(other);
        this.authority = other.authority;
        this.syncable = other.syncable;
        this.readPermission = other.readPermission;
        this.writePermission = other.writePermission;
        this.grantUriPermissions = other.grantUriPermissions;
        this.forceUriPermissions = other.forceUriPermissions;
        this.multiProcess = other.multiProcess;
        this.initOrder = other.initOrder;
        this.uriPermissionPatterns = other.uriPermissionPatterns;
        this.pathPermissions = other.pathPermissions;
    }

    public void setAuthority(java.lang.String authority) {
        this.authority = android.text.TextUtils.safeIntern(authority);
    }

    public void setSyncable(boolean syncable) {
        this.syncable = syncable;
    }

    public void setReadPermission(java.lang.String readPermission) {
        // Empty string must be converted to null
        this.readPermission = (android.text.TextUtils.isEmpty(readPermission)) ? null : readPermission.intern();
    }

    public void setWritePermission(java.lang.String writePermission) {
        // Empty string must be converted to null
        this.writePermission = (android.text.TextUtils.isEmpty(writePermission)) ? null : writePermission.intern();
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Provider{");
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
        dest.writeString(this.authority);
        dest.writeBoolean(this.syncable);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.readPermission, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.writePermission, dest, flags);
        dest.writeBoolean(this.grantUriPermissions);
        dest.writeBoolean(this.forceUriPermissions);
        dest.writeBoolean(this.multiProcess);
        dest.writeInt(this.initOrder);
        dest.writeTypedArray(this.uriPermissionPatterns, flags);
        dest.writeTypedArray(this.pathPermissions, flags);
    }

    public ParsedProvider() {
    }

    protected ParsedProvider(android.os.Parcel in) {
        super(in);
        // noinspection ConstantConditions
        this.authority = in.readString();
        this.syncable = in.readBoolean();
        this.readPermission = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.writePermission = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.grantUriPermissions = in.readBoolean();
        this.forceUriPermissions = in.readBoolean();
        this.multiProcess = in.readBoolean();
        this.initOrder = in.readInt();
        this.uriPermissionPatterns = in.createTypedArray(PatternMatcher.CREATOR);
        this.pathPermissions = in.createTypedArray(this.CREATOR);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedProvider> CREATOR = new android.content.pm.parsing.component.Creator<android.content.pm.parsing.component.ParsedProvider>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedProvider createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedProvider(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedProvider[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedProvider[size];
        }
    };

    @android.annotation.NonNull
    public java.lang.String getAuthority() {
        return authority;
    }

    public boolean isSyncable() {
        return syncable;
    }

    @android.annotation.Nullable
    public java.lang.String getReadPermission() {
        return readPermission;
    }

    @android.annotation.Nullable
    public java.lang.String getWritePermission() {
        return writePermission;
    }

    public boolean isGrantUriPermissions() {
        return grantUriPermissions;
    }

    public boolean isForceUriPermissions() {
        return forceUriPermissions;
    }

    public boolean isMultiProcess() {
        return multiProcess;
    }

    public int getInitOrder() {
        return initOrder;
    }

    @android.annotation.Nullable
    public android.os.PatternMatcher[] getUriPermissionPatterns() {
        return uriPermissionPatterns;
    }

    @android.annotation.Nullable
    public android.content.pm.PathPermission[] getPathPermissions() {
        return pathPermissions;
    }
}

