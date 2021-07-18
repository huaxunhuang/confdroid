/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.service.notification;


/**
 * Class encapsulating a Notification. Sent by the NotificationManagerService to clients including
 * the status bar and any {@link android.service.notification.NotificationListenerService}s.
 */
public class StatusBarNotification implements android.os.Parcelable {
    private final java.lang.String pkg;

    private final int id;

    private final java.lang.String tag;

    private final java.lang.String key;

    private java.lang.String groupKey;

    private java.lang.String overrideGroupKey;

    private final int uid;

    private final java.lang.String opPkg;

    private final int initialPid;

    private final android.app.Notification notification;

    private final android.os.UserHandle user;

    private final long postTime;

    private android.content.Context mContext;// used for inflation & icon expansion


    /**
     *
     *
     * @unknown 
     */
    public StatusBarNotification(java.lang.String pkg, java.lang.String opPkg, int id, java.lang.String tag, int uid, int initialPid, int score, android.app.Notification notification, android.os.UserHandle user) {
        this(pkg, opPkg, id, tag, uid, initialPid, score, notification, user, java.lang.System.currentTimeMillis());
    }

    /**
     *
     *
     * @unknown 
     */
    public StatusBarNotification(java.lang.String pkg, java.lang.String opPkg, int id, java.lang.String tag, int uid, int initialPid, android.app.Notification notification, android.os.UserHandle user, java.lang.String overrideGroupKey, long postTime) {
        if (pkg == null)
            throw new java.lang.NullPointerException();

        if (notification == null)
            throw new java.lang.NullPointerException();

        this.pkg = pkg;
        this.opPkg = opPkg;
        this.id = id;
        this.tag = tag;
        this.uid = uid;
        this.initialPid = initialPid;
        this.notification = notification;
        this.user = user;
        this.postTime = postTime;
        this.overrideGroupKey = overrideGroupKey;
        this.key = key();
        this.groupKey = groupKey();
    }

    public StatusBarNotification(java.lang.String pkg, java.lang.String opPkg, int id, java.lang.String tag, int uid, int initialPid, int score, android.app.Notification notification, android.os.UserHandle user, long postTime) {
        if (pkg == null)
            throw new java.lang.NullPointerException();

        if (notification == null)
            throw new java.lang.NullPointerException();

        this.pkg = pkg;
        this.opPkg = opPkg;
        this.id = id;
        this.tag = tag;
        this.uid = uid;
        this.initialPid = initialPid;
        this.notification = notification;
        this.user = user;
        this.postTime = postTime;
        this.key = key();
        this.groupKey = groupKey();
    }

    public StatusBarNotification(android.os.Parcel in) {
        this.pkg = in.readString();
        this.opPkg = in.readString();
        this.id = in.readInt();
        if (in.readInt() != 0) {
            this.tag = in.readString();
        } else {
            this.tag = null;
        }
        this.uid = in.readInt();
        this.initialPid = in.readInt();
        this.notification = new android.app.Notification(in);
        this.user = android.os.UserHandle.readFromParcel(in);
        this.postTime = in.readLong();
        if (in.readInt() != 0) {
            this.overrideGroupKey = in.readString();
        } else {
            this.overrideGroupKey = null;
        }
        this.key = key();
        this.groupKey = groupKey();
    }

    private java.lang.String key() {
        java.lang.String sbnKey = (((((((user.getIdentifier() + "|") + pkg) + "|") + id) + "|") + tag) + "|") + uid;
        if ((overrideGroupKey != null) && getNotification().isGroupSummary()) {
            sbnKey = (sbnKey + "|") + overrideGroupKey;
        }
        return sbnKey;
    }

    private java.lang.String groupKey() {
        if (overrideGroupKey != null) {
            return ((((user.getIdentifier() + "|") + pkg) + "|") + "g:") + overrideGroupKey;
        }
        final java.lang.String group = getNotification().getGroup();
        final java.lang.String sortKey = getNotification().getSortKey();
        if ((group == null) && (sortKey == null)) {
            // a group of one
            return key;
        }
        return (((user.getIdentifier() + "|") + pkg) + "|") + (group == null ? "p:" + notification.priority : "g:" + group);
    }

    /**
     * Returns true if this notification is part of a group.
     */
    public boolean isGroup() {
        if ((overrideGroupKey != null) || isAppGroup()) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if application asked that this notification be part of a group.
     *
     * @unknown 
     */
    public boolean isAppGroup() {
        if ((getNotification().getGroup() != null) || (getNotification().getSortKey() != null)) {
            return true;
        }
        return false;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(this.pkg);
        out.writeString(this.opPkg);
        out.writeInt(this.id);
        if (this.tag != null) {
            out.writeInt(1);
            out.writeString(this.tag);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.uid);
        out.writeInt(this.initialPid);
        this.notification.writeToParcel(out, flags);
        user.writeToParcel(out, flags);
        out.writeLong(this.postTime);
        if (this.overrideGroupKey != null) {
            out.writeInt(1);
            out.writeString(this.overrideGroupKey);
        } else {
            out.writeInt(0);
        }
    }

    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.service.notification.StatusBarNotification> CREATOR = new android.os.Parcelable.Creator<android.service.notification.StatusBarNotification>() {
        public android.service.notification.StatusBarNotification createFromParcel(android.os.Parcel parcel) {
            return new android.service.notification.StatusBarNotification(parcel);
        }

        public android.service.notification.StatusBarNotification[] newArray(int size) {
            return new android.service.notification.StatusBarNotification[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    public android.service.notification.StatusBarNotification cloneLight() {
        final android.app.Notification no = new android.app.Notification();
        this.notification.cloneInto(no, false);// light copy

        return new android.service.notification.StatusBarNotification(this.pkg, this.opPkg, this.id, this.tag, this.uid, this.initialPid, no, this.user, this.overrideGroupKey, this.postTime);
    }

    @java.lang.Override
    public android.service.notification.StatusBarNotification clone() {
        return new android.service.notification.StatusBarNotification(this.pkg, this.opPkg, this.id, this.tag, this.uid, this.initialPid, this.notification.clone(), this.user, this.overrideGroupKey, this.postTime);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("StatusBarNotification(pkg=%s user=%s id=%d tag=%s key=%s: %s)", this.pkg, this.user, this.id, this.tag, this.key, this.notification);
    }

    /**
     * Convenience method to check the notification's flags for
     * {@link Notification#FLAG_ONGOING_EVENT}.
     */
    public boolean isOngoing() {
        return (notification.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0;
    }

    /**
     * Convenience method to check the notification's flags for
     * either {@link Notification#FLAG_ONGOING_EVENT} or
     * {@link Notification#FLAG_NO_CLEAR}.
     */
    public boolean isClearable() {
        return ((notification.flags & android.app.Notification.FLAG_ONGOING_EVENT) == 0) && ((notification.flags & android.app.Notification.FLAG_NO_CLEAR) == 0);
    }

    /**
     * Returns a userHandle for the instance of the app that posted this notification.
     *
     * @deprecated Use {@link #getUser()} instead.
     */
    public int getUserId() {
        return this.user.getIdentifier();
    }

    /**
     * The package of the app that posted the notification.
     */
    public java.lang.String getPackageName() {
        return pkg;
    }

    /**
     * The id supplied to {@link android.app.NotificationManager#notify(int,Notification)}.
     */
    public int getId() {
        return id;
    }

    /**
     * The tag supplied to {@link android.app.NotificationManager#notify(int,Notification)},
     * or null if no tag was specified.
     */
    public java.lang.String getTag() {
        return tag;
    }

    /**
     * The notifying app's calling uid. @hide
     */
    public int getUid() {
        return uid;
    }

    /**
     * The package used for AppOps tracking. @hide
     */
    public java.lang.String getOpPkg() {
        return opPkg;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getInitialPid() {
        return initialPid;
    }

    /**
     * The {@link android.app.Notification} supplied to
     * {@link android.app.NotificationManager#notify(int,Notification)}.
     */
    public android.app.Notification getNotification() {
        return notification;
    }

    /**
     * The {@link android.os.UserHandle} for whom this notification is intended.
     */
    public android.os.UserHandle getUser() {
        return user;
    }

    /**
     * The time (in {@link System#currentTimeMillis} time) the notification was posted,
     * which may be different than {@link android.app.Notification#when}.
     */
    public long getPostTime() {
        return postTime;
    }

    /**
     * A unique instance key for this notification record.
     */
    public java.lang.String getKey() {
        return key;
    }

    /**
     * A key that indicates the group with which this message ranks.
     */
    public java.lang.String getGroupKey() {
        return groupKey;
    }

    /**
     * Sets the override group key.
     */
    public void setOverrideGroupKey(java.lang.String overrideGroupKey) {
        this.overrideGroupKey = overrideGroupKey;
        groupKey = groupKey();
    }

    /**
     * Returns the override group key.
     */
    public java.lang.String getOverrideGroupKey() {
        return overrideGroupKey;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.Context getPackageContext(android.content.Context context) {
        if (mContext == null) {
            try {
                android.content.pm.ApplicationInfo ai = context.getPackageManager().getApplicationInfo(pkg, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
                mContext = context.createApplicationContext(ai, android.content.Context.CONTEXT_RESTRICTED);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                mContext = null;
            }
        }
        if (mContext == null) {
            mContext = context;
        }
        return mContext;
    }
}

