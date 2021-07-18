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
 * @unknown 
 */
public class ParsedPermission extends android.content.pm.parsing.component.ParsedComponent {
    @android.annotation.Nullable
    java.lang.String backgroundPermission;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String group;

    int requestRes;

    int protectionLevel;

    boolean tree;

    @android.annotation.Nullable
    private android.content.pm.parsing.component.ParsedPermissionGroup parsedPermissionGroup;

    @com.android.internal.annotations.VisibleForTesting
    public ParsedPermission() {
    }

    public ParsedPermission(android.content.pm.parsing.component.ParsedPermission other) {
        super(other);
        this.backgroundPermission = other.backgroundPermission;
        this.group = other.group;
        this.requestRes = other.requestRes;
        this.protectionLevel = other.protectionLevel;
        this.tree = other.tree;
        this.parsedPermissionGroup = other.parsedPermissionGroup;
    }

    public ParsedPermission(android.content.pm.parsing.component.ParsedPermission other, android.content.pm.PermissionInfo pendingPermissionInfo, java.lang.String packageName, java.lang.String name) {
        this(other);
        this.flags = pendingPermissionInfo.flags;
        this.descriptionRes = pendingPermissionInfo.descriptionRes;
        this.backgroundPermission = pendingPermissionInfo.backgroundPermission;
        this.group = pendingPermissionInfo.group;
        this.requestRes = pendingPermissionInfo.requestRes;
        this.protectionLevel = pendingPermissionInfo.protectionLevel;
        setName(name);
        setPackageName(packageName);
    }

    public android.content.pm.parsing.component.ParsedPermission setGroup(java.lang.String group) {
        this.group = android.text.TextUtils.safeIntern(group);
        return this;
    }

    public android.content.pm.parsing.component.ParsedPermission setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public boolean isRuntime() {
        return getProtection() == android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;
    }

    public boolean isAppOp() {
        return (protectionLevel & android.content.pm.PermissionInfo.PROTECTION_FLAG_APPOP) != 0;
    }

    @android.content.pm.PermissionInfo.Protection
    public int getProtection() {
        return protectionLevel & android.content.pm.PermissionInfo.PROTECTION_MASK_BASE;
    }

    public int getProtectionFlags() {
        return protectionLevel & (~android.content.pm.PermissionInfo.PROTECTION_MASK_BASE);
    }

    public int calculateFootprint() {
        int size = getName().length();
        if (getNonLocalizedLabel() != null) {
            size += getNonLocalizedLabel().length();
        }
        return size;
    }

    public java.lang.String toString() {
        return ((("Permission{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + getName()) + "}";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.backgroundPermission);
        dest.writeString(this.group);
        dest.writeInt(this.requestRes);
        dest.writeInt(this.protectionLevel);
        dest.writeBoolean(this.tree);
        dest.writeParcelable(this.parsedPermissionGroup, flags);
    }

    protected ParsedPermission(android.os.Parcel in) {
        super(in);
        // We use the boot classloader for all classes that we load.
        final java.lang.ClassLoader boot = java.lang.Object.class.getClassLoader();
        this.backgroundPermission = in.readString();
        this.group = in.readString();
        this.requestRes = in.readInt();
        this.protectionLevel = in.readInt();
        this.tree = in.readBoolean();
        this.parsedPermissionGroup = in.readParcelable(boot);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedPermission> CREATOR = new android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedPermission>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedPermission createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedPermission(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedPermission[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedPermission[size];
        }
    };

    @android.annotation.Nullable
    public java.lang.String getBackgroundPermission() {
        return backgroundPermission;
    }

    @android.annotation.Nullable
    public java.lang.String getGroup() {
        return group;
    }

    public int getRequestRes() {
        return requestRes;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }

    public boolean isTree() {
        return tree;
    }

    @android.annotation.Nullable
    public android.content.pm.parsing.component.ParsedPermissionGroup getParsedPermissionGroup() {
        return parsedPermissionGroup;
    }

    public android.content.pm.parsing.component.ParsedPermission setProtectionLevel(int value) {
        protectionLevel = value;
        return this;
    }

    public android.content.pm.parsing.component.ParsedPermission setParsedPermissionGroup(@android.annotation.Nullable
    android.content.pm.parsing.component.ParsedPermissionGroup value) {
        parsedPermissionGroup = value;
        return this;
    }
}

