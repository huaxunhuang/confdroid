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
package android.os;


/**
 * Representation of a user on the device.
 */
public final class UserHandle implements android.os.Parcelable {
    /**
     *
     *
     * @unknown Range of uids allocated for a user.
     */
    public static final int PER_USER_RANGE = 100000;

    /**
     *
     *
     * @unknown A user id to indicate all users on the device
     */
    @android.annotation.UserIdInt
    public static final int USER_ALL = -1;

    /**
     *
     *
     * @unknown A user handle to indicate all users on the device
     */
    public static final android.os.UserHandle ALL = new android.os.UserHandle(android.os.UserHandle.USER_ALL);

    /**
     *
     *
     * @unknown A user id to indicate the currently active user
     */
    @android.annotation.UserIdInt
    public static final int USER_CURRENT = -2;

    /**
     *
     *
     * @unknown A user handle to indicate the current user of the device
     */
    public static final android.os.UserHandle CURRENT = new android.os.UserHandle(android.os.UserHandle.USER_CURRENT);

    /**
     *
     *
     * @unknown A user id to indicate that we would like to send to the current
    user, but if this is calling from a user process then we will send it
    to the caller's user instead of failing with a security exception
     */
    @android.annotation.UserIdInt
    public static final int USER_CURRENT_OR_SELF = -3;

    /**
     *
     *
     * @unknown A user handle to indicate that we would like to send to the current
    user, but if this is calling from a user process then we will send it
    to the caller's user instead of failing with a security exception
     */
    public static final android.os.UserHandle CURRENT_OR_SELF = new android.os.UserHandle(android.os.UserHandle.USER_CURRENT_OR_SELF);

    /**
     *
     *
     * @unknown An undefined user id
     */
    @android.annotation.UserIdInt
    public static final int USER_NULL = -10000;

    /**
     *
     *
     * @unknown A user id constant to indicate the "owner" user of the device
     * @deprecated Consider using either {@link UserHandle#USER_SYSTEM} constant or
    check the target user's flag {@link android.content.pm.UserInfo#isAdmin}.
     */
    @android.annotation.UserIdInt
    public static final int USER_OWNER = 0;

    /**
     *
     *
     * @unknown A user handle to indicate the primary/owner user of the device
     * @deprecated Consider using either {@link UserHandle#SYSTEM} constant or
    check the target user's flag {@link android.content.pm.UserInfo#isAdmin}.
     */
    public static final android.os.UserHandle OWNER = new android.os.UserHandle(android.os.UserHandle.USER_OWNER);

    /**
     *
     *
     * @unknown A user id constant to indicate the "system" user of the device
     */
    @android.annotation.UserIdInt
    public static final int USER_SYSTEM = 0;

    /**
     *
     *
     * @unknown A user serial constant to indicate the "system" user of the device
     */
    public static final int USER_SERIAL_SYSTEM = 0;

    /**
     *
     *
     * @unknown A user handle to indicate the "system" user of the device
     */
    public static final android.os.UserHandle SYSTEM = new android.os.UserHandle(android.os.UserHandle.USER_SYSTEM);

    /**
     *
     *
     * @unknown Enable multi-user related side effects. Set this to false if
    there are problems with single user use-cases.
     */
    public static final boolean MU_ENABLED = true;

    final int mHandle;

    /**
     * Checks to see if the user id is the same for the two uids, i.e., they belong to the same
     * user.
     *
     * @unknown 
     */
    public static boolean isSameUser(int uid1, int uid2) {
        return android.os.UserHandle.getUserId(uid1) == android.os.UserHandle.getUserId(uid2);
    }

    /**
     * Checks to see if both uids are referring to the same app id, ignoring the user id part of the
     * uids.
     *
     * @param uid1
     * 		uid to compare
     * @param uid2
     * 		other uid to compare
     * @return whether the appId is the same for both uids
     * @unknown 
     */
    public static boolean isSameApp(int uid1, int uid2) {
        return android.os.UserHandle.getAppId(uid1) == android.os.UserHandle.getAppId(uid2);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isIsolated(int uid) {
        if (uid > 0) {
            final int appId = android.os.UserHandle.getAppId(uid);
            return (appId >= android.os.Process.FIRST_ISOLATED_UID) && (appId <= android.os.Process.LAST_ISOLATED_UID);
        } else {
            return false;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isApp(int uid) {
        if (uid > 0) {
            final int appId = android.os.UserHandle.getAppId(uid);
            return (appId >= android.os.Process.FIRST_APPLICATION_UID) && (appId <= android.os.Process.LAST_APPLICATION_UID);
        } else {
            return false;
        }
    }

    /**
     * Returns the user for a given uid.
     *
     * @param uid
     * 		A uid for an application running in a particular user.
     * @return A {@link UserHandle} for that user.
     */
    public static android.os.UserHandle getUserHandleForUid(int uid) {
        return android.os.UserHandle.of(android.os.UserHandle.getUserId(uid));
    }

    /**
     * Returns the user id for a given uid.
     *
     * @unknown 
     */
    @android.annotation.UserIdInt
    public static int getUserId(int uid) {
        if (android.os.UserHandle.MU_ENABLED) {
            return uid / android.os.UserHandle.PER_USER_RANGE;
        } else {
            return android.os.UserHandle.USER_SYSTEM;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UserIdInt
    public static int getCallingUserId() {
        return android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static android.os.UserHandle of(@android.annotation.UserIdInt
    int userId) {
        return userId == android.os.UserHandle.USER_SYSTEM ? android.os.UserHandle.SYSTEM : new android.os.UserHandle(userId);
    }

    /**
     * Returns the uid that is composed from the userId and the appId.
     *
     * @unknown 
     */
    public static int getUid(@android.annotation.UserIdInt
    int userId, @android.annotation.AppIdInt
    int appId) {
        if (android.os.UserHandle.MU_ENABLED) {
            return (userId * android.os.UserHandle.PER_USER_RANGE) + (appId % android.os.UserHandle.PER_USER_RANGE);
        } else {
            return appId;
        }
    }

    /**
     * Returns the app id (or base uid) for a given uid, stripping out the user id from it.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.AppIdInt
    public static int getAppId(int uid) {
        return uid % android.os.UserHandle.PER_USER_RANGE;
    }

    /**
     * Returns the gid shared between all apps with this userId.
     *
     * @unknown 
     */
    public static int getUserGid(@android.annotation.UserIdInt
    int userId) {
        return android.os.UserHandle.getUid(userId, android.os.Process.SHARED_USER_GID);
    }

    /**
     * Returns the shared app gid for a given uid or appId.
     *
     * @unknown 
     */
    public static int getSharedAppGid(int id) {
        return (android.os.Process.FIRST_SHARED_APPLICATION_GID + (id % android.os.UserHandle.PER_USER_RANGE)) - android.os.Process.FIRST_APPLICATION_UID;
    }

    /**
     * Returns the app id for a given shared app gid. Returns -1 if the ID is invalid.
     *
     * @unknown 
     */
    @android.annotation.AppIdInt
    public static int getAppIdFromSharedAppGid(int gid) {
        final int appId = (android.os.UserHandle.getAppId(gid) + android.os.Process.FIRST_APPLICATION_UID) - android.os.Process.FIRST_SHARED_APPLICATION_GID;
        if ((appId < 0) || (appId >= android.os.Process.FIRST_SHARED_APPLICATION_GID)) {
            return -1;
        }
        return appId;
    }

    /**
     * Generate a text representation of the uid, breaking out its individual
     * components -- user, app, isolated, etc.
     *
     * @unknown 
     */
    public static void formatUid(java.lang.StringBuilder sb, int uid) {
        if (uid < android.os.Process.FIRST_APPLICATION_UID) {
            sb.append(uid);
        } else {
            sb.append('u');
            sb.append(android.os.UserHandle.getUserId(uid));
            final int appId = android.os.UserHandle.getAppId(uid);
            if ((appId >= android.os.Process.FIRST_ISOLATED_UID) && (appId <= android.os.Process.LAST_ISOLATED_UID)) {
                sb.append('i');
                sb.append(appId - android.os.Process.FIRST_ISOLATED_UID);
            } else
                if (appId >= android.os.Process.FIRST_APPLICATION_UID) {
                    sb.append('a');
                    sb.append(appId - android.os.Process.FIRST_APPLICATION_UID);
                } else {
                    sb.append('s');
                    sb.append(appId);
                }

        }
    }

    /**
     * Generate a text representation of the uid, breaking out its individual
     * components -- user, app, isolated, etc.
     *
     * @unknown 
     */
    public static java.lang.String formatUid(int uid) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        android.os.UserHandle.formatUid(sb, uid);
        return sb.toString();
    }

    /**
     * Generate a text representation of the uid, breaking out its individual
     * components -- user, app, isolated, etc.
     *
     * @unknown 
     */
    public static void formatUid(java.io.PrintWriter pw, int uid) {
        if (uid < android.os.Process.FIRST_APPLICATION_UID) {
            pw.print(uid);
        } else {
            pw.print('u');
            pw.print(android.os.UserHandle.getUserId(uid));
            final int appId = android.os.UserHandle.getAppId(uid);
            if ((appId >= android.os.Process.FIRST_ISOLATED_UID) && (appId <= android.os.Process.LAST_ISOLATED_UID)) {
                pw.print('i');
                pw.print(appId - android.os.Process.FIRST_ISOLATED_UID);
            } else
                if (appId >= android.os.Process.FIRST_APPLICATION_UID) {
                    pw.print('a');
                    pw.print(appId - android.os.Process.FIRST_APPLICATION_UID);
                } else {
                    pw.print('s');
                    pw.print(appId);
                }

        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UserIdInt
    public static int parseUserArg(java.lang.String arg) {
        int userId;
        if ("all".equals(arg)) {
            userId = android.os.UserHandle.USER_ALL;
        } else
            if ("current".equals(arg) || "cur".equals(arg)) {
                userId = android.os.UserHandle.USER_CURRENT;
            } else {
                try {
                    userId = java.lang.Integer.parseInt(arg);
                } catch (java.lang.NumberFormatException e) {
                    throw new java.lang.IllegalArgumentException("Bad user number: " + arg);
                }
            }

        return userId;
    }

    /**
     * Returns the user id of the current process
     *
     * @return user id of the current process
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.UserIdInt
    public static int myUserId() {
        return android.os.UserHandle.getUserId(android.os.Process.myUid());
    }

    /**
     * Returns true if this UserHandle refers to the owner user; false otherwise.
     *
     * @return true if this UserHandle refers to the owner user; false otherwise.
     * @unknown 
     * @deprecated please use {@link #isSystem()} or check for
    {@link android.content.pm.UserInfo#isPrimary()}
    {@link android.content.pm.UserInfo#isAdmin()} based on your particular use case.
     */
    @android.annotation.SystemApi
    public boolean isOwner() {
        return this.equals(android.os.UserHandle.OWNER);
    }

    /**
     *
     *
     * @return true if this UserHandle refers to the system user; false otherwise.
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isSystem() {
        return this.equals(android.os.UserHandle.SYSTEM);
    }

    /**
     *
     *
     * @unknown 
     */
    public UserHandle(int h) {
        mHandle = h;
    }

    /**
     * Returns the userId stored in this UserHandle.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.UserIdInt
    public int getIdentifier() {
        return mHandle;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("UserHandle{" + mHandle) + "}";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        try {
            if (obj != null) {
                android.os.UserHandle other = ((android.os.UserHandle) (obj));
                return mHandle == other.mHandle;
            }
        } catch (java.lang.ClassCastException e) {
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return mHandle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mHandle);
    }

    /**
     * Write a UserHandle to a Parcel, handling null pointers.  Must be
     * read with {@link #readFromParcel(Parcel)}.
     *
     * @param h
     * 		The UserHandle to be written.
     * @param out
     * 		The Parcel in which the UserHandle will be placed.
     * @see #readFromParcel(Parcel)
     */
    public static void writeToParcel(android.os.UserHandle h, android.os.Parcel out) {
        if (h != null) {
            h.writeToParcel(out, 0);
        } else {
            out.writeInt(android.os.UserHandle.USER_NULL);
        }
    }

    /**
     * Read a UserHandle from a Parcel that was previously written
     * with {@link #writeToParcel(UserHandle, Parcel)}, returning either
     * a null or new object as appropriate.
     *
     * @param in
     * 		The Parcel from which to read the UserHandle
     * @return Returns a new UserHandle matching the previously written
    object, or null if a null had been written.
     * @see #writeToParcel(UserHandle, Parcel)
     */
    public static android.os.UserHandle readFromParcel(android.os.Parcel in) {
        int h = in.readInt();
        return h != android.os.UserHandle.USER_NULL ? new android.os.UserHandle(h) : null;
    }

    public static final android.os.Parcelable.Creator<android.os.UserHandle> CREATOR = new android.os.Parcelable.Creator<android.os.UserHandle>() {
        public android.os.UserHandle createFromParcel(android.os.Parcel in) {
            return new android.os.UserHandle(in);
        }

        public android.os.UserHandle[] newArray(int size) {
            return new android.os.UserHandle[size];
        }
    };

    /**
     * Instantiate a new UserHandle from the data in a Parcel that was
     * previously written with {@link #writeToParcel(Parcel, int)}.  Note that you
     * must not use this with data written by
     * {@link #writeToParcel(UserHandle, Parcel)} since it is not possible
     * to handle a null UserHandle here.
     *
     * @param in
     * 		The Parcel containing the previously written UserHandle,
     * 		positioned at the location in the buffer where it was written.
     */
    public UserHandle(android.os.Parcel in) {
        mHandle = in.readInt();
    }
}

