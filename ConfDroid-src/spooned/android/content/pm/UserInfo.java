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
 * Per-user information.
 *
 * @unknown 
 */
public class UserInfo implements android.os.Parcelable {
    /**
     * 16 bits for user type
     */
    public static final int FLAG_MASK_USER_TYPE = 0xffff;

    /**
     * *************************** NOTE ***************************
     * These flag values CAN NOT CHANGE because they are written
     * directly to storage.
     */
    /**
     * Primary user. Only one user can have this flag set. It identifies the first human user
     * on a device.
     */
    @android.annotation.UnsupportedAppUsage
    public static final int FLAG_PRIMARY = 0x1;

    /**
     * User with administrative privileges. Such a user can create and
     * delete users.
     */
    public static final int FLAG_ADMIN = 0x2;

    /**
     * Indicates a guest user that may be transient.
     */
    public static final int FLAG_GUEST = 0x4;

    /**
     * Indicates the user has restrictions in privileges, in addition to those for normal users.
     * Exact meaning TBD. For instance, maybe they can't install apps or administer WiFi access pts.
     */
    public static final int FLAG_RESTRICTED = 0x8;

    /**
     * Indicates that this user has gone through its first-time initialization.
     */
    public static final int FLAG_INITIALIZED = 0x10;

    /**
     * Indicates that this user is a profile of another user, for example holding a users
     * corporate data.
     */
    public static final int FLAG_MANAGED_PROFILE = 0x20;

    /**
     * Indicates that this user is disabled.
     *
     * <p>Note: If an ephemeral user is disabled, it shouldn't be later re-enabled. Ephemeral users
     * are disabled as their removal is in progress to indicate that they shouldn't be re-entered.
     */
    public static final int FLAG_DISABLED = 0x40;

    public static final int FLAG_QUIET_MODE = 0x80;

    /**
     * Indicates that this user is ephemeral. I.e. the user will be removed after leaving
     * the foreground.
     */
    public static final int FLAG_EPHEMERAL = 0x100;

    /**
     * User is for demo purposes only and can be removed at any time.
     */
    public static final int FLAG_DEMO = 0x200;

    public static final int NO_PROFILE_GROUP_ID = android.os.UserHandle.USER_NULL;

    @android.annotation.UnsupportedAppUsage
    public int id;

    @android.annotation.UnsupportedAppUsage
    public int serialNumber;

    @android.annotation.UnsupportedAppUsage
    public java.lang.String name;

    @android.annotation.UnsupportedAppUsage
    public java.lang.String iconPath;

    @android.annotation.UnsupportedAppUsage
    public int flags;

    @android.annotation.UnsupportedAppUsage
    public long creationTime;

    @android.annotation.UnsupportedAppUsage
    public long lastLoggedInTime;

    public java.lang.String lastLoggedInFingerprint;

    /**
     * If this user is a parent user, it would be its own user id.
     * If this user is a child user, it would be its parent user id.
     * Otherwise, it would be {@link #NO_PROFILE_GROUP_ID}.
     */
    @android.annotation.UnsupportedAppUsage
    public int profileGroupId;

    public int restrictedProfileParentId;

    /**
     * Which profile badge color/label to use.
     */
    public int profileBadge;

    /**
     * User is only partially created.
     */
    @android.annotation.UnsupportedAppUsage
    public boolean partial;

    @android.annotation.UnsupportedAppUsage
    public boolean guestToRemove;

    @android.annotation.UnsupportedAppUsage
    public UserInfo(int id, java.lang.String name, int flags) {
        this(id, name, null, flags);
    }

    @android.annotation.UnsupportedAppUsage
    public UserInfo(int id, java.lang.String name, java.lang.String iconPath, int flags) {
        this.id = id;
        this.name = name;
        this.flags = flags;
        this.iconPath = iconPath;
        this.profileGroupId = android.content.pm.UserInfo.NO_PROFILE_GROUP_ID;
        this.restrictedProfileParentId = android.content.pm.UserInfo.NO_PROFILE_GROUP_ID;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isPrimary() {
        return (flags & android.content.pm.UserInfo.FLAG_PRIMARY) == android.content.pm.UserInfo.FLAG_PRIMARY;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isAdmin() {
        return (flags & android.content.pm.UserInfo.FLAG_ADMIN) == android.content.pm.UserInfo.FLAG_ADMIN;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isGuest() {
        return (flags & android.content.pm.UserInfo.FLAG_GUEST) == android.content.pm.UserInfo.FLAG_GUEST;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isRestricted() {
        return (flags & android.content.pm.UserInfo.FLAG_RESTRICTED) == android.content.pm.UserInfo.FLAG_RESTRICTED;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isManagedProfile() {
        return (flags & android.content.pm.UserInfo.FLAG_MANAGED_PROFILE) == android.content.pm.UserInfo.FLAG_MANAGED_PROFILE;
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isEnabled() {
        return (flags & android.content.pm.UserInfo.FLAG_DISABLED) != android.content.pm.UserInfo.FLAG_DISABLED;
    }

    public boolean isQuietModeEnabled() {
        return (flags & android.content.pm.UserInfo.FLAG_QUIET_MODE) == android.content.pm.UserInfo.FLAG_QUIET_MODE;
    }

    public boolean isEphemeral() {
        return (flags & android.content.pm.UserInfo.FLAG_EPHEMERAL) == android.content.pm.UserInfo.FLAG_EPHEMERAL;
    }

    public boolean isInitialized() {
        return (flags & android.content.pm.UserInfo.FLAG_INITIALIZED) == android.content.pm.UserInfo.FLAG_INITIALIZED;
    }

    public boolean isDemo() {
        return (flags & android.content.pm.UserInfo.FLAG_DEMO) == android.content.pm.UserInfo.FLAG_DEMO;
    }

    /**
     * Returns true if the user is a split system user.
     * <p>If {@link UserManager#isSplitSystemUser split system user mode} is not enabled,
     * the method always returns false.
     */
    public boolean isSystemOnly() {
        return android.content.pm.UserInfo.isSystemOnly(id);
    }

    /**
     * Returns true if the given user is a split system user.
     * <p>If {@link UserManager#isSplitSystemUser split system user mode} is not enabled,
     * the method always returns false.
     */
    public static boolean isSystemOnly(int userId) {
        return (userId == android.os.UserHandle.USER_SYSTEM) && android.os.UserManager.isSplitSystemUser();
    }

    /**
     *
     *
     * @return true if this user can be switched to.
     */
    public boolean supportsSwitchTo() {
        if (isEphemeral() && (!isEnabled())) {
            // Don't support switching to an ephemeral user with removal in progress.
            return false;
        }
        return !isManagedProfile();
    }

    /**
     *
     *
     * @return true if this user can be switched to by end user through UI.
     */
    public boolean supportsSwitchToByUser() {
        // Hide the system user when it does not represent a human user.
        boolean hideSystemUser = android.os.UserManager.isSplitSystemUser();
        return ((!hideSystemUser) || (id != android.os.UserHandle.USER_SYSTEM)) && supportsSwitchTo();
    }

    /* @hide */
    public boolean canHaveProfile() {
        if ((isManagedProfile() || isGuest()) || isRestricted()) {
            return false;
        }
        if (android.os.UserManager.isSplitSystemUser()) {
            return id != android.os.UserHandle.USER_SYSTEM;
        } else {
            return id == android.os.UserHandle.USER_SYSTEM;
        }
    }

    public UserInfo() {
    }

    public UserInfo(android.content.pm.UserInfo orig) {
        name = orig.name;
        iconPath = orig.iconPath;
        id = orig.id;
        flags = orig.flags;
        serialNumber = orig.serialNumber;
        creationTime = orig.creationTime;
        lastLoggedInTime = orig.lastLoggedInTime;
        lastLoggedInFingerprint = orig.lastLoggedInFingerprint;
        partial = orig.partial;
        profileGroupId = orig.profileGroupId;
        restrictedProfileParentId = orig.restrictedProfileParentId;
        guestToRemove = orig.guestToRemove;
        profileBadge = orig.profileBadge;
    }

    @android.annotation.UnsupportedAppUsage
    public android.os.UserHandle getUserHandle() {
        return new android.os.UserHandle(id);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("UserInfo{" + id) + ":") + name) + ":") + java.lang.Integer.toHexString(flags)) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(iconPath);
        dest.writeInt(flags);
        dest.writeInt(serialNumber);
        dest.writeLong(creationTime);
        dest.writeLong(lastLoggedInTime);
        dest.writeString(lastLoggedInFingerprint);
        dest.writeInt(partial ? 1 : 0);
        dest.writeInt(profileGroupId);
        dest.writeInt(guestToRemove ? 1 : 0);
        dest.writeInt(restrictedProfileParentId);
        dest.writeInt(profileBadge);
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.UserInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.UserInfo>() {
        public android.content.pm.UserInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.UserInfo(source);
        }

        public android.content.pm.UserInfo[] newArray(int size) {
            return new android.content.pm.UserInfo[size];
        }
    };

    private UserInfo(android.os.Parcel source) {
        id = source.readInt();
        name = source.readString();
        iconPath = source.readString();
        flags = source.readInt();
        serialNumber = source.readInt();
        creationTime = source.readLong();
        lastLoggedInTime = source.readLong();
        lastLoggedInFingerprint = source.readString();
        partial = source.readInt() != 0;
        profileGroupId = source.readInt();
        guestToRemove = source.readInt() != 0;
        restrictedProfileParentId = source.readInt();
        profileBadge = source.readInt();
    }
}

