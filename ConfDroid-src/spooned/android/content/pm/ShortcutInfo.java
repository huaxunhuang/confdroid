/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * Represents a shortcut that can be published via {@link ShortcutManager}.
 *
 * @see ShortcutManager
 */
public final class ShortcutInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "Shortcut";

    private static final java.lang.String RES_TYPE_STRING = "string";

    private static final java.lang.String ANDROID_PACKAGE_NAME = "android";

    private static final int IMPLICIT_RANK_MASK = 0x7fffffff;

    private static final int RANK_CHANGED_BIT = ~android.content.pm.ShortcutInfo.IMPLICIT_RANK_MASK;

    /**
     *
     *
     * @unknown 
     */
    public static final int RANK_NOT_SET = java.lang.Integer.MAX_VALUE;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_DYNAMIC = 1 << 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_PINNED = 1 << 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_HAS_ICON_RES = 1 << 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_HAS_ICON_FILE = 1 << 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_KEY_FIELDS_ONLY = 1 << 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_MANIFEST = 1 << 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_DISABLED = 1 << 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_STRINGS_RESOLVED = 1 << 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_IMMUTABLE = 1 << 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_ADAPTIVE_BITMAP = 1 << 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_RETURNED_BY_SERVICE = 1 << 10;

    /**
     *
     *
     * @unknown When this is set, the bitmap icon is waiting to be saved.
     */
    public static final int FLAG_ICON_FILE_PENDING_SAVE = 1 << 11;

    /**
     * "Shadow" shortcuts are the ones that are restored, but the owner package hasn't been
     * installed yet.
     *
     * @unknown 
     */
    public static final int FLAG_SHADOW = 1 << 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_LONG_LIVED = 1 << 13;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "FLAG_" }, value = { android.content.pm.ShortcutInfo.FLAG_DYNAMIC, android.content.pm.ShortcutInfo.FLAG_PINNED, android.content.pm.ShortcutInfo.FLAG_HAS_ICON_RES, android.content.pm.ShortcutInfo.FLAG_HAS_ICON_FILE, android.content.pm.ShortcutInfo.FLAG_KEY_FIELDS_ONLY, android.content.pm.ShortcutInfo.FLAG_MANIFEST, android.content.pm.ShortcutInfo.FLAG_DISABLED, android.content.pm.ShortcutInfo.FLAG_STRINGS_RESOLVED, android.content.pm.ShortcutInfo.FLAG_IMMUTABLE, android.content.pm.ShortcutInfo.FLAG_ADAPTIVE_BITMAP, android.content.pm.ShortcutInfo.FLAG_RETURNED_BY_SERVICE, android.content.pm.ShortcutInfo.FLAG_ICON_FILE_PENDING_SAVE, android.content.pm.ShortcutInfo.FLAG_SHADOW, android.content.pm.ShortcutInfo.FLAG_LONG_LIVED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ShortcutFlags {}

    // Cloning options.
    /**
     *
     *
     * @unknown 
     */
    private static final int CLONE_REMOVE_ICON = 1 << 0;

    /**
     *
     *
     * @unknown 
     */
    private static final int CLONE_REMOVE_INTENT = 1 << 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_NON_KEY_INFO = 1 << 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_RES_NAMES = 1 << 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_PERSON = 1 << 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_FOR_CREATOR = android.content.pm.ShortcutInfo.CLONE_REMOVE_ICON | android.content.pm.ShortcutInfo.CLONE_REMOVE_RES_NAMES;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_FOR_LAUNCHER = ((android.content.pm.ShortcutInfo.CLONE_REMOVE_ICON | android.content.pm.ShortcutInfo.CLONE_REMOVE_INTENT) | android.content.pm.ShortcutInfo.CLONE_REMOVE_RES_NAMES) | android.content.pm.ShortcutInfo.CLONE_REMOVE_PERSON;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_FOR_LAUNCHER_APPROVAL = (android.content.pm.ShortcutInfo.CLONE_REMOVE_INTENT | android.content.pm.ShortcutInfo.CLONE_REMOVE_RES_NAMES) | android.content.pm.ShortcutInfo.CLONE_REMOVE_PERSON;

    /**
     *
     *
     * @unknown 
     */
    public static final int CLONE_REMOVE_FOR_APP_PREDICTION = android.content.pm.ShortcutInfo.CLONE_REMOVE_ICON | android.content.pm.ShortcutInfo.CLONE_REMOVE_RES_NAMES;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "CLONE_" }, value = { android.content.pm.ShortcutInfo.CLONE_REMOVE_ICON, android.content.pm.ShortcutInfo.CLONE_REMOVE_INTENT, android.content.pm.ShortcutInfo.CLONE_REMOVE_NON_KEY_INFO, android.content.pm.ShortcutInfo.CLONE_REMOVE_RES_NAMES, android.content.pm.ShortcutInfo.CLONE_REMOVE_PERSON, android.content.pm.ShortcutInfo.CLONE_REMOVE_FOR_CREATOR, android.content.pm.ShortcutInfo.CLONE_REMOVE_FOR_LAUNCHER, android.content.pm.ShortcutInfo.CLONE_REMOVE_FOR_LAUNCHER_APPROVAL, android.content.pm.ShortcutInfo.CLONE_REMOVE_FOR_APP_PREDICTION })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface CloneFlags {}

    /**
     * Shortcut is not disabled.
     */
    public static final int DISABLED_REASON_NOT_DISABLED = 0;

    /**
     * Shortcut has been disabled by the publisher app with the
     * {@link ShortcutManager#disableShortcuts(List)} API.
     */
    public static final int DISABLED_REASON_BY_APP = 1;

    /**
     * Shortcut has been disabled due to changes to the publisher app. (e.g. a manifest shortcut
     * no longer exists.)
     */
    public static final int DISABLED_REASON_APP_CHANGED = 2;

    /**
     * Shortcut is disabled for an unknown reason.
     */
    public static final int DISABLED_REASON_UNKNOWN = 3;

    /**
     * A disabled reason that's equal to or bigger than this is due to backup and restore issue.
     * A shortcut with such a reason wil be visible to the launcher, but not to the publisher.
     * ({@link #isVisibleToPublisher()} will be false.)
     */
    private static final int DISABLED_REASON_RESTORE_ISSUE_START = 100;

    /**
     * Shortcut has been restored from the previous device, but the publisher app on the current
     * device is of a lower version. The shortcut will not be usable until the app is upgraded to
     * the same version or higher.
     */
    public static final int DISABLED_REASON_VERSION_LOWER = 100;

    /**
     * Shortcut has not been restored because the publisher app does not support backup and restore.
     */
    public static final int DISABLED_REASON_BACKUP_NOT_SUPPORTED = 101;

    /**
     * Shortcut has not been restored because the publisher app's signature has changed.
     */
    public static final int DISABLED_REASON_SIGNATURE_MISMATCH = 102;

    /**
     * Shortcut has not been restored for unknown reason.
     */
    public static final int DISABLED_REASON_OTHER_RESTORE_ISSUE = 103;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "DISABLED_REASON_" }, value = { android.content.pm.ShortcutInfo.DISABLED_REASON_NOT_DISABLED, android.content.pm.ShortcutInfo.DISABLED_REASON_BY_APP, android.content.pm.ShortcutInfo.DISABLED_REASON_APP_CHANGED, android.content.pm.ShortcutInfo.DISABLED_REASON_UNKNOWN, android.content.pm.ShortcutInfo.DISABLED_REASON_VERSION_LOWER, android.content.pm.ShortcutInfo.DISABLED_REASON_BACKUP_NOT_SUPPORTED, android.content.pm.ShortcutInfo.DISABLED_REASON_SIGNATURE_MISMATCH, android.content.pm.ShortcutInfo.DISABLED_REASON_OTHER_RESTORE_ISSUE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DisabledReason {}

    /**
     * Return a label for disabled reasons, which are *not* supposed to be shown to the user.
     *
     * @unknown 
     */
    public static java.lang.String getDisabledReasonDebugString(@android.content.pm.ShortcutInfo.DisabledReason
    int disabledReason) {
        switch (disabledReason) {
            case android.content.pm.ShortcutInfo.DISABLED_REASON_NOT_DISABLED :
                return "[Not disabled]";
            case android.content.pm.ShortcutInfo.DISABLED_REASON_BY_APP :
                return "[Disabled: by app]";
            case android.content.pm.ShortcutInfo.DISABLED_REASON_APP_CHANGED :
                return "[Disabled: app changed]";
            case android.content.pm.ShortcutInfo.DISABLED_REASON_VERSION_LOWER :
                return "[Disabled: lower version]";
            case android.content.pm.ShortcutInfo.DISABLED_REASON_BACKUP_NOT_SUPPORTED :
                return "[Disabled: backup not supported]";
            case android.content.pm.ShortcutInfo.DISABLED_REASON_SIGNATURE_MISMATCH :
                return "[Disabled: signature mismatch]";
            case android.content.pm.ShortcutInfo.DISABLED_REASON_OTHER_RESTORE_ISSUE :
                return "[Disabled: unknown restore issue]";
        }
        return ("[Disabled: unknown reason:" + disabledReason) + "]";
    }

    /**
     * Return a label for a disabled reason for shortcuts that are disabled due to a backup and
     * restore issue. If the reason is not due to backup & restore, then it'll return null.
     *
     * This method returns localized, user-facing strings, which will be returned by
     * {@link #getDisabledMessage()}.
     *
     * @unknown 
     */
    public static java.lang.String getDisabledReasonForRestoreIssue(android.content.Context context, @android.content.pm.ShortcutInfo.DisabledReason
    int disabledReason) {
        final android.content.res.Resources res = context.getResources();
        switch (disabledReason) {
            case android.content.pm.ShortcutInfo.DISABLED_REASON_VERSION_LOWER :
                return res.getString(com.android.internal.R.string.shortcut_restored_on_lower_version);
            case android.content.pm.ShortcutInfo.DISABLED_REASON_BACKUP_NOT_SUPPORTED :
                return res.getString(com.android.internal.R.string.shortcut_restore_not_supported);
            case android.content.pm.ShortcutInfo.DISABLED_REASON_SIGNATURE_MISMATCH :
                return res.getString(com.android.internal.R.string.shortcut_restore_signature_mismatch);
            case android.content.pm.ShortcutInfo.DISABLED_REASON_OTHER_RESTORE_ISSUE :
                return res.getString(com.android.internal.R.string.shortcut_restore_unknown_issue);
            case android.content.pm.ShortcutInfo.DISABLED_REASON_UNKNOWN :
                return res.getString(com.android.internal.R.string.shortcut_disabled_reason_unknown);
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isDisabledForRestoreIssue(@android.content.pm.ShortcutInfo.DisabledReason
    int disabledReason) {
        return disabledReason >= android.content.pm.ShortcutInfo.DISABLED_REASON_RESTORE_ISSUE_START;
    }

    /**
     * Shortcut category for messaging related actions, such as chat.
     */
    public static final java.lang.String SHORTCUT_CATEGORY_CONVERSATION = "android.shortcut.conversation";

    private final java.lang.String mId;

    @android.annotation.NonNull
    private final java.lang.String mPackageName;

    @android.annotation.Nullable
    private android.content.ComponentName mActivity;

    @android.annotation.Nullable
    private android.graphics.drawable.Icon mIcon;

    private int mTitleResId;

    private java.lang.String mTitleResName;

    @android.annotation.Nullable
    private java.lang.CharSequence mTitle;

    private int mTextResId;

    private java.lang.String mTextResName;

    @android.annotation.Nullable
    private java.lang.CharSequence mText;

    private int mDisabledMessageResId;

    private java.lang.String mDisabledMessageResName;

    @android.annotation.Nullable
    private java.lang.CharSequence mDisabledMessage;

    @android.annotation.Nullable
    private android.util.ArraySet<java.lang.String> mCategories;

    /**
     * Intents *with extras removed*.
     */
    @android.annotation.Nullable
    private android.content.Intent[] mIntents;

    /**
     * Extras for the intents.
     */
    @android.annotation.Nullable
    private android.os.PersistableBundle[] mIntentPersistableExtrases;

    @android.annotation.Nullable
    private android.app.Person[] mPersons;

    @android.annotation.Nullable
    private android.content.LocusId mLocusId;

    private int mRank;

    /**
     * Internally used for auto-rank-adjustment.
     *
     * RANK_CHANGED_BIT is used to denote that the rank of a shortcut is changing.
     * The rest of the bits are used to denote the order in which shortcuts are passed to
     * APIs, which is used to preserve the argument order when ranks are tie.
     */
    private int mImplicitRank;

    @android.annotation.Nullable
    private android.os.PersistableBundle mExtras;

    private long mLastChangedTimestamp;

    // Internal use only.
    @android.content.pm.ShortcutInfo.ShortcutFlags
    private int mFlags;

    // Internal use only.
    private int mIconResId;

    private java.lang.String mIconResName;

    // Internal use only.
    @android.annotation.Nullable
    private java.lang.String mBitmapPath;

    private final int mUserId;

    /**
     *
     *
     * @unknown 
     */
    public static final int VERSION_CODE_UNKNOWN = -1;

    private int mDisabledReason;

    private ShortcutInfo(android.content.pm.ShortcutInfo.Builder b) {
        mUserId = b.mContext.getUserId();
        mId = com.android.internal.util.Preconditions.checkStringNotEmpty(b.mId, "Shortcut ID must be provided");
        // Note we can't do other null checks here because SM.updateShortcuts() takes partial
        // information.
        mPackageName = b.mContext.getPackageName();
        mActivity = b.mActivity;
        mIcon = b.mIcon;
        mTitle = b.mTitle;
        mTitleResId = b.mTitleResId;
        mText = b.mText;
        mTextResId = b.mTextResId;
        mDisabledMessage = b.mDisabledMessage;
        mDisabledMessageResId = b.mDisabledMessageResId;
        mCategories = android.content.pm.ShortcutInfo.cloneCategories(b.mCategories);
        mIntents = android.content.pm.ShortcutInfo.cloneIntents(b.mIntents);
        fixUpIntentExtras();
        mPersons = android.content.pm.ShortcutInfo.clonePersons(b.mPersons);
        if (b.mIsLongLived) {
            setLongLived();
        }
        mRank = b.mRank;
        mExtras = b.mExtras;
        mLocusId = b.mLocusId;
        updateTimestamp();
    }

    /**
     * Extract extras from {@link #mIntents} and set them to {@link #mIntentPersistableExtrases}
     * as {@link PersistableBundle}, and remove extras from the original intents.
     */
    private void fixUpIntentExtras() {
        if (mIntents == null) {
            mIntentPersistableExtrases = null;
            return;
        }
        mIntentPersistableExtrases = new android.os.PersistableBundle[mIntents.length];
        for (int i = 0; i < mIntents.length; i++) {
            final android.content.Intent intent = mIntents[i];
            final android.os.Bundle extras = intent.getExtras();
            if (extras == null) {
                mIntentPersistableExtrases[i] = null;
            } else {
                mIntentPersistableExtrases[i] = new android.os.PersistableBundle(extras);
                intent.replaceExtras(((android.os.Bundle) (null)));
            }
        }
    }

    private static android.util.ArraySet<java.lang.String> cloneCategories(java.util.Set<java.lang.String> source) {
        if (source == null) {
            return null;
        }
        final android.util.ArraySet<java.lang.String> ret = new android.util.ArraySet(source.size());
        for (java.lang.CharSequence s : source) {
            if (!android.text.TextUtils.isEmpty(s)) {
                ret.add(s.toString().intern());
            }
        }
        return ret;
    }

    private static android.content.Intent[] cloneIntents(android.content.Intent[] intents) {
        if (intents == null) {
            return null;
        }
        final android.content.Intent[] ret = new android.content.Intent[intents.length];
        for (int i = 0; i < ret.length; i++) {
            if (intents[i] != null) {
                ret[i] = new android.content.Intent(intents[i]);
            }
        }
        return ret;
    }

    private static android.os.PersistableBundle[] clonePersistableBundle(android.os.PersistableBundle[] bundle) {
        if (bundle == null) {
            return null;
        }
        final android.os.PersistableBundle[] ret = new android.os.PersistableBundle[bundle.length];
        for (int i = 0; i < ret.length; i++) {
            if (bundle[i] != null) {
                ret[i] = new android.os.PersistableBundle(bundle[i]);
            }
        }
        return ret;
    }

    private static android.app.Person[] clonePersons(android.app.Person[] persons) {
        if (persons == null) {
            return null;
        }
        final android.app.Person[] ret = new android.app.Person[persons.length];
        for (int i = 0; i < ret.length; i++) {
            if (persons[i] != null) {
                // Don't need to keep the icon, remove it to save space
                ret[i] = persons[i].toBuilder().setIcon(null).build();
            }
        }
        return ret;
    }

    /**
     * Throws if any of the mandatory fields is not set.
     *
     * @unknown 
     */
    public void enforceMandatoryFields(boolean forPinned) {
        com.android.internal.util.Preconditions.checkStringNotEmpty(mId, "Shortcut ID must be provided");
        if (!forPinned) {
            com.android.internal.util.Preconditions.checkNotNull(mActivity, "Activity must be provided");
        }
        if ((mTitle == null) && (mTitleResId == 0)) {
            throw new java.lang.IllegalArgumentException("Short label must be provided");
        }
        com.android.internal.util.Preconditions.checkNotNull(mIntents, "Shortcut Intent must be provided");
        com.android.internal.util.Preconditions.checkArgument(mIntents.length > 0, "Shortcut Intent must be provided");
    }

    /**
     * Copy constructor.
     */
    private ShortcutInfo(android.content.pm.ShortcutInfo source, @android.content.pm.ShortcutInfo.CloneFlags
    int cloneFlags) {
        mUserId = source.mUserId;
        mId = source.mId;
        mPackageName = source.mPackageName;
        mActivity = source.mActivity;
        mFlags = source.mFlags;
        mLastChangedTimestamp = source.mLastChangedTimestamp;
        mDisabledReason = source.mDisabledReason;
        mLocusId = source.mLocusId;
        // Just always keep it since it's cheep.
        mIconResId = source.mIconResId;
        if ((cloneFlags & android.content.pm.ShortcutInfo.CLONE_REMOVE_NON_KEY_INFO) == 0) {
            if ((cloneFlags & android.content.pm.ShortcutInfo.CLONE_REMOVE_ICON) == 0) {
                mIcon = source.mIcon;
                mBitmapPath = source.mBitmapPath;
            }
            mTitle = source.mTitle;
            mTitleResId = source.mTitleResId;
            mText = source.mText;
            mTextResId = source.mTextResId;
            mDisabledMessage = source.mDisabledMessage;
            mDisabledMessageResId = source.mDisabledMessageResId;
            mCategories = android.content.pm.ShortcutInfo.cloneCategories(source.mCategories);
            if ((cloneFlags & android.content.pm.ShortcutInfo.CLONE_REMOVE_PERSON) == 0) {
                mPersons = android.content.pm.ShortcutInfo.clonePersons(source.mPersons);
            }
            if ((cloneFlags & android.content.pm.ShortcutInfo.CLONE_REMOVE_INTENT) == 0) {
                mIntents = android.content.pm.ShortcutInfo.cloneIntents(source.mIntents);
                mIntentPersistableExtrases = android.content.pm.ShortcutInfo.clonePersistableBundle(source.mIntentPersistableExtrases);
            }
            mRank = source.mRank;
            mExtras = source.mExtras;
            if ((cloneFlags & android.content.pm.ShortcutInfo.CLONE_REMOVE_RES_NAMES) == 0) {
                mTitleResName = source.mTitleResName;
                mTextResName = source.mTextResName;
                mDisabledMessageResName = source.mDisabledMessageResName;
                mIconResName = source.mIconResName;
            }
        } else {
            // Set this bit.
            mFlags |= android.content.pm.ShortcutInfo.FLAG_KEY_FIELDS_ONLY;
        }
    }

    /**
     * Load a string resource from the publisher app.
     *
     * @param resId
     * 		resource ID
     * @param defValue
     * 		default value to be returned when the specified resource isn't found.
     */
    private java.lang.CharSequence getResourceString(android.content.res.Resources res, int resId, java.lang.CharSequence defValue) {
        try {
            return res.getString(resId);
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.e(android.content.pm.ShortcutInfo.TAG, (("Resource for ID=" + resId) + " not found in package ") + mPackageName);
            return defValue;
        }
    }

    /**
     * Load the string resources for the text fields and set them to the actual value fields.
     * This will set {@link #FLAG_STRINGS_RESOLVED}.
     *
     * @param res
     * 		{@link Resources} for the publisher.  Must have been loaded with
     * 		{@link PackageManager#getResourcesForApplicationAsUser}.
     * @unknown 
     */
    public void resolveResourceStrings(@android.annotation.NonNull
    android.content.res.Resources res) {
        mFlags |= android.content.pm.ShortcutInfo.FLAG_STRINGS_RESOLVED;
        if (((mTitleResId == 0) && (mTextResId == 0)) && (mDisabledMessageResId == 0)) {
            return;// Bail early.

        }
        if (mTitleResId != 0) {
            mTitle = getResourceString(res, mTitleResId, mTitle);
        }
        if (mTextResId != 0) {
            mText = getResourceString(res, mTextResId, mText);
        }
        if (mDisabledMessageResId != 0) {
            mDisabledMessage = getResourceString(res, mDisabledMessageResId, mDisabledMessage);
        }
    }

    /**
     * Look up resource name for a given resource ID.
     *
     * @return a simple resource name (e.g. "text_1") when {@code withType} is false, or with the
    type (e.g. "string/text_1").
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static java.lang.String lookUpResourceName(@android.annotation.NonNull
    android.content.res.Resources res, int resId, boolean withType, @android.annotation.NonNull
    java.lang.String packageName) {
        if (resId == 0) {
            return null;
        }
        try {
            final java.lang.String fullName = res.getResourceName(resId);
            if (android.content.pm.ShortcutInfo.ANDROID_PACKAGE_NAME.equals(android.content.pm.ShortcutInfo.getResourcePackageName(fullName))) {
                // If it's a framework resource, the value won't change, so just return the ID
                // value as a string.
                return java.lang.String.valueOf(resId);
            }
            return withType ? android.content.pm.ShortcutInfo.getResourceTypeAndEntryName(fullName) : android.content.pm.ShortcutInfo.getResourceEntryName(fullName);
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.e(android.content.pm.ShortcutInfo.TAG, (((("Resource name for ID=" + resId) + " not found in package ") + packageName) + ". Resource IDs may change when the application is upgraded, and the system") + " may not be able to find the correct resource.");
            return null;
        }
    }

    /**
     * Extract the package name from a fully-donated resource name.
     * e.g. "com.android.app1:drawable/icon1" -> "com.android.app1"
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static java.lang.String getResourcePackageName(@android.annotation.NonNull
    java.lang.String fullResourceName) {
        final int p1 = fullResourceName.indexOf(':');
        if (p1 < 0) {
            return null;
        }
        return fullResourceName.substring(0, p1);
    }

    /**
     * Extract the type name from a fully-donated resource name.
     * e.g. "com.android.app1:drawable/icon1" -> "drawable"
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static java.lang.String getResourceTypeName(@android.annotation.NonNull
    java.lang.String fullResourceName) {
        final int p1 = fullResourceName.indexOf(':');
        if (p1 < 0) {
            return null;
        }
        final int p2 = fullResourceName.indexOf('/', p1 + 1);
        if (p2 < 0) {
            return null;
        }
        return fullResourceName.substring(p1 + 1, p2);
    }

    /**
     * Extract the type name + the entry name from a fully-donated resource name.
     * e.g. "com.android.app1:drawable/icon1" -> "drawable/icon1"
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static java.lang.String getResourceTypeAndEntryName(@android.annotation.NonNull
    java.lang.String fullResourceName) {
        final int p1 = fullResourceName.indexOf(':');
        if (p1 < 0) {
            return null;
        }
        return fullResourceName.substring(p1 + 1);
    }

    /**
     * Extract the entry name from a fully-donated resource name.
     * e.g. "com.android.app1:drawable/icon1" -> "icon1"
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static java.lang.String getResourceEntryName(@android.annotation.NonNull
    java.lang.String fullResourceName) {
        final int p1 = fullResourceName.indexOf('/');
        if (p1 < 0) {
            return null;
        }
        return fullResourceName.substring(p1 + 1);
    }

    /**
     * Return the resource ID for a given resource ID.
     *
     * Basically its' a wrapper over {@link Resources#getIdentifier(String, String, String)}, except
     * if {@code resourceName} is an integer then it'll just return its value.  (Which also the
     * aforementioned method would do internally, but not documented, so doing here explicitly.)
     *
     * @param res
     * 		{@link Resources} for the publisher.  Must have been loaded with
     * 		{@link PackageManager#getResourcesForApplicationAsUser}.
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static int lookUpResourceId(@android.annotation.NonNull
    android.content.res.Resources res, @android.annotation.Nullable
    java.lang.String resourceName, @android.annotation.Nullable
    java.lang.String resourceType, java.lang.String packageName) {
        if (resourceName == null) {
            return 0;
        }
        try {
            try {
                // It the name can be parsed as an integer, just use it.
                return java.lang.Integer.parseInt(resourceName);
            } catch (java.lang.NumberFormatException ignore) {
            }
            return res.getIdentifier(resourceName, resourceType, packageName);
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.e(android.content.pm.ShortcutInfo.TAG, (("Resource ID for name=" + resourceName) + " not found in package ") + packageName);
            return 0;
        }
    }

    /**
     * Look up resource names from the resource IDs for the icon res and the text fields, and fill
     * in the resource name fields.
     *
     * @param res
     * 		{@link Resources} for the publisher.  Must have been loaded with
     * 		{@link PackageManager#getResourcesForApplicationAsUser}.
     * @unknown 
     */
    public void lookupAndFillInResourceNames(@android.annotation.NonNull
    android.content.res.Resources res) {
        if ((((mTitleResId == 0) && (mTextResId == 0)) && (mDisabledMessageResId == 0)) && (mIconResId == 0)) {
            return;// Bail early.

        }
        // We don't need types for strings because their types are always "string".
        mTitleResName = /* withType= */
        android.content.pm.ShortcutInfo.lookUpResourceName(res, mTitleResId, false, mPackageName);
        mTextResName = /* withType= */
        android.content.pm.ShortcutInfo.lookUpResourceName(res, mTextResId, false, mPackageName);
        mDisabledMessageResName = /* withType= */
        android.content.pm.ShortcutInfo.lookUpResourceName(res, mDisabledMessageResId, false, mPackageName);
        // But icons have multiple possible types, so include the type.
        mIconResName = /* withType= */
        android.content.pm.ShortcutInfo.lookUpResourceName(res, mIconResId, true, mPackageName);
    }

    /**
     * Look up resource IDs from the resource names for the icon res and the text fields, and fill
     * in the resource ID fields.
     *
     * This is called when an app is updated.
     *
     * @unknown 
     */
    public void lookupAndFillInResourceIds(@android.annotation.NonNull
    android.content.res.Resources res) {
        if ((((mTitleResName == null) && (mTextResName == null)) && (mDisabledMessageResName == null)) && (mIconResName == null)) {
            return;// Bail early.

        }
        mTitleResId = android.content.pm.ShortcutInfo.lookUpResourceId(res, mTitleResName, android.content.pm.ShortcutInfo.RES_TYPE_STRING, mPackageName);
        mTextResId = android.content.pm.ShortcutInfo.lookUpResourceId(res, mTextResName, android.content.pm.ShortcutInfo.RES_TYPE_STRING, mPackageName);
        mDisabledMessageResId = android.content.pm.ShortcutInfo.lookUpResourceId(res, mDisabledMessageResName, android.content.pm.ShortcutInfo.RES_TYPE_STRING, mPackageName);
        // mIconResName already contains the type, so the third argument is not needed.
        mIconResId = android.content.pm.ShortcutInfo.lookUpResourceId(res, mIconResName, null, mPackageName);
    }

    /**
     * Copy a {@link ShortcutInfo}, optionally removing fields.
     *
     * @unknown 
     */
    public android.content.pm.ShortcutInfo clone(@android.content.pm.ShortcutInfo.CloneFlags
    int cloneFlags) {
        return new android.content.pm.ShortcutInfo(this, cloneFlags);
    }

    /**
     *
     *
     * @unknown 
     * @unknown set true if it's "update", as opposed to "replace".
     */
    public void ensureUpdatableWith(android.content.pm.ShortcutInfo source, boolean isUpdating) {
        if (isUpdating) {
            com.android.internal.util.Preconditions.checkState(isVisibleToPublisher(), "[Framework BUG] Invisible shortcuts can't be updated");
        }
        com.android.internal.util.Preconditions.checkState(mUserId == source.mUserId, "Owner User ID must match");
        com.android.internal.util.Preconditions.checkState(mId.equals(source.mId), "ID must match");
        com.android.internal.util.Preconditions.checkState(mPackageName.equals(source.mPackageName), "Package name must match");
        if (isVisibleToPublisher()) {
            // Don't do this check for restore-blocked shortcuts.
            com.android.internal.util.Preconditions.checkState(!isImmutable(), "Target ShortcutInfo is immutable");
        }
    }

    /**
     * Copy non-null/zero fields from another {@link ShortcutInfo}.  Only "public" information
     * will be overwritten.  The timestamp will *not* be updated to be consistent with other
     * setters (and also the clock is not injectable in this file).
     *
     * - Flags will not change
     * - mBitmapPath will not change
     * - Current time will be set to timestamp
     *
     * @throws IllegalStateException
     * 		if source is not compatible.
     * @unknown 
     */
    public void copyNonNullFieldsFrom(android.content.pm.ShortcutInfo source) {
        /* isUpdating= */
        ensureUpdatableWith(source, true);
        if (source.mActivity != null) {
            mActivity = source.mActivity;
        }
        if (source.mIcon != null) {
            mIcon = source.mIcon;
            mIconResId = 0;
            mIconResName = null;
            mBitmapPath = null;
        }
        if (source.mTitle != null) {
            mTitle = source.mTitle;
            mTitleResId = 0;
            mTitleResName = null;
        } else
            if (source.mTitleResId != 0) {
                mTitle = null;
                mTitleResId = source.mTitleResId;
                mTitleResName = null;
            }

        if (source.mText != null) {
            mText = source.mText;
            mTextResId = 0;
            mTextResName = null;
        } else
            if (source.mTextResId != 0) {
                mText = null;
                mTextResId = source.mTextResId;
                mTextResName = null;
            }

        if (source.mDisabledMessage != null) {
            mDisabledMessage = source.mDisabledMessage;
            mDisabledMessageResId = 0;
            mDisabledMessageResName = null;
        } else
            if (source.mDisabledMessageResId != 0) {
                mDisabledMessage = null;
                mDisabledMessageResId = source.mDisabledMessageResId;
                mDisabledMessageResName = null;
            }

        if (source.mCategories != null) {
            mCategories = android.content.pm.ShortcutInfo.cloneCategories(source.mCategories);
        }
        if (source.mPersons != null) {
            mPersons = android.content.pm.ShortcutInfo.clonePersons(source.mPersons);
        }
        if (source.mIntents != null) {
            mIntents = android.content.pm.ShortcutInfo.cloneIntents(source.mIntents);
            mIntentPersistableExtrases = android.content.pm.ShortcutInfo.clonePersistableBundle(source.mIntentPersistableExtrases);
        }
        if (source.mRank != android.content.pm.ShortcutInfo.RANK_NOT_SET) {
            mRank = source.mRank;
        }
        if (source.mExtras != null) {
            mExtras = source.mExtras;
        }
        if (source.mLocusId != null) {
            mLocusId = source.mLocusId;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.graphics.drawable.Icon validateIcon(android.graphics.drawable.Icon icon) {
        switch (icon.getType()) {
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
            case android.graphics.drawable.Icon.TYPE_BITMAP :
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                break;// OK

            default :
                throw android.content.pm.ShortcutInfo.getInvalidIconException();
        }
        if (icon.hasTint()) {
            throw new java.lang.IllegalArgumentException("Icons with tints are not supported");
        }
        return icon;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.IllegalArgumentException getInvalidIconException() {
        return new java.lang.IllegalArgumentException("Unsupported icon type:" + " only the bitmap and resource types are supported");
    }

    /**
     * Builder class for {@link ShortcutInfo} objects.
     *
     * @see ShortcutManager
     */
    public static class Builder {
        private final android.content.Context mContext;

        private java.lang.String mId;

        private android.content.ComponentName mActivity;

        private android.graphics.drawable.Icon mIcon;

        private int mTitleResId;

        private java.lang.CharSequence mTitle;

        private int mTextResId;

        private java.lang.CharSequence mText;

        private int mDisabledMessageResId;

        private java.lang.CharSequence mDisabledMessage;

        private java.util.Set<java.lang.String> mCategories;

        private android.content.Intent[] mIntents;

        private android.app.Person[] mPersons;

        private boolean mIsLongLived;

        private int mRank = android.content.pm.ShortcutInfo.RANK_NOT_SET;

        private android.os.PersistableBundle mExtras;

        private android.content.LocusId mLocusId;

        /**
         * Old style constructor.
         *
         * @unknown 
         */
        @java.lang.Deprecated
        public Builder(android.content.Context context) {
            mContext = context;
        }

        /**
         * Used with the old style constructor, kept for unit tests.
         *
         * @unknown 
         */
        @android.annotation.NonNull
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setId(@android.annotation.NonNull
        java.lang.String id) {
            mId = com.android.internal.util.Preconditions.checkStringNotEmpty(id, "id cannot be empty");
            return this;
        }

        /**
         * Constructor.
         *
         * @param context
         * 		Client context.
         * @param id
         * 		ID of the shortcut.
         */
        public Builder(android.content.Context context, java.lang.String id) {
            mContext = context;
            mId = com.android.internal.util.Preconditions.checkStringNotEmpty(id, "id cannot be empty");
        }

        /**
         * Sets the {@link LocusId} associated with this shortcut.
         *
         * <p>This method should be called when the {@link LocusId} is used in other places (such
         * as {@link Notification} and {@link ContentCaptureContext}) so the Android system can
         * correlate them.
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setLocusId(@android.annotation.NonNull
        android.content.LocusId locusId) {
            mLocusId = com.android.internal.util.Preconditions.checkNotNull(locusId, "locusId cannot be null");
            return this;
        }

        /**
         * Sets the target activity.  A shortcut will be shown along with this activity's icon
         * on the launcher.
         *
         * When selecting a target activity, keep the following in mind:
         * <ul>
         * <li>All dynamic shortcuts must have a target activity.  When a shortcut with no target
         * activity is published using
         * {@link ShortcutManager#addDynamicShortcuts(List)} or
         * {@link ShortcutManager#setDynamicShortcuts(List)},
         * the first main activity defined in the app's <code>AndroidManifest.xml</code>
         * file is used.
         *
         * <li>Only "main" activities&mdash;ones that define the {@link Intent#ACTION_MAIN}
         * and {@link Intent#CATEGORY_LAUNCHER} intent filters&mdash;can be target
         * activities.
         *
         * <li>By default, the first main activity defined in the app's manifest is
         * the target activity.
         *
         * <li>A target activity must belong to the publisher app.
         * </ul>
         *
         * @see ShortcutInfo#getActivity()
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setActivity(@android.annotation.NonNull
        android.content.ComponentName activity) {
            mActivity = com.android.internal.util.Preconditions.checkNotNull(activity, "activity cannot be null");
            return this;
        }

        /**
         * Sets an icon of a shortcut.
         *
         * <p>Icons are not available on {@link ShortcutInfo} instances
         * returned by {@link ShortcutManager} or {@link LauncherApps}.  The default launcher
         * app can use {@link LauncherApps#getShortcutIconDrawable(ShortcutInfo, int)}
         * or {@link LauncherApps#getShortcutBadgedIconDrawable(ShortcutInfo, int)} to fetch
         * shortcut icons.
         *
         * <p>Tints set with {@link Icon#setTint} or {@link Icon#setTintList} are not supported
         * and will be ignored.
         *
         * <p>Only icons created with {@link Icon#createWithBitmap(Bitmap)},
         * {@link Icon#createWithAdaptiveBitmap(Bitmap)}
         * and {@link Icon#createWithResource} are supported.
         * Other types, such as URI-based icons, are not supported.
         *
         * @see LauncherApps#getShortcutIconDrawable(ShortcutInfo, int)
         * @see LauncherApps#getShortcutBadgedIconDrawable(ShortcutInfo, int)
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setIcon(android.graphics.drawable.Icon icon) {
            mIcon = android.content.pm.ShortcutInfo.validateIcon(icon);
            return this;
        }

        /**
         *
         *
         * @unknown We don't support resource strings for dynamic shortcuts for now.  (But unit tests
        use it.)
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setShortLabelResId(int shortLabelResId) {
            com.android.internal.util.Preconditions.checkState(mTitle == null, "shortLabel already set");
            mTitleResId = shortLabelResId;
            return this;
        }

        /**
         * Sets the short title of a shortcut.
         *
         * <p>This is a mandatory field when publishing a new shortcut with
         * {@link ShortcutManager#addDynamicShortcuts(List)} or
         * {@link ShortcutManager#setDynamicShortcuts(List)}.
         *
         * <p>This field is intended to be a concise description of a shortcut.
         *
         * <p>The recommended maximum length is 10 characters.
         *
         * @see ShortcutInfo#getShortLabel()
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setShortLabel(@android.annotation.NonNull
        java.lang.CharSequence shortLabel) {
            com.android.internal.util.Preconditions.checkState(mTitleResId == 0, "shortLabelResId already set");
            mTitle = com.android.internal.util.Preconditions.checkStringNotEmpty(shortLabel, "shortLabel cannot be empty");
            return this;
        }

        /**
         *
         *
         * @unknown We don't support resource strings for dynamic shortcuts for now.  (But unit tests
        use it.)
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setLongLabelResId(int longLabelResId) {
            com.android.internal.util.Preconditions.checkState(mText == null, "longLabel already set");
            mTextResId = longLabelResId;
            return this;
        }

        /**
         * Sets the text of a shortcut.
         *
         * <p>This field is intended to be more descriptive than the shortcut title.  The launcher
         * shows this instead of the short title when it has enough space.
         *
         * <p>The recommend maximum length is 25 characters.
         *
         * @see ShortcutInfo#getLongLabel()
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setLongLabel(@android.annotation.NonNull
        java.lang.CharSequence longLabel) {
            com.android.internal.util.Preconditions.checkState(mTextResId == 0, "longLabelResId already set");
            mText = com.android.internal.util.Preconditions.checkStringNotEmpty(longLabel, "longLabel cannot be empty");
            return this;
        }

        /**
         *
         *
         * @unknown -- old signature, the internal code still uses it.
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setTitle(@android.annotation.NonNull
        java.lang.CharSequence value) {
            return setShortLabel(value);
        }

        /**
         *
         *
         * @unknown -- old signature, the internal code still uses it.
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setTitleResId(int value) {
            return setShortLabelResId(value);
        }

        /**
         *
         *
         * @unknown -- old signature, the internal code still uses it.
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setText(@android.annotation.NonNull
        java.lang.CharSequence value) {
            return setLongLabel(value);
        }

        /**
         *
         *
         * @unknown -- old signature, the internal code still uses it.
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setTextResId(int value) {
            return setLongLabelResId(value);
        }

        /**
         *
         *
         * @unknown We don't support resource strings for dynamic shortcuts for now.  (But unit tests
        use it.)
         */
        @java.lang.Deprecated
        public android.content.pm.ShortcutInfo.Builder setDisabledMessageResId(int disabledMessageResId) {
            com.android.internal.util.Preconditions.checkState(mDisabledMessage == null, "disabledMessage already set");
            mDisabledMessageResId = disabledMessageResId;
            return this;
        }

        /**
         * Sets the message that should be shown when the user attempts to start a shortcut that
         * is disabled.
         *
         * @see ShortcutInfo#getDisabledMessage()
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setDisabledMessage(@android.annotation.NonNull
        java.lang.CharSequence disabledMessage) {
            com.android.internal.util.Preconditions.checkState(mDisabledMessageResId == 0, "disabledMessageResId already set");
            mDisabledMessage = com.android.internal.util.Preconditions.checkStringNotEmpty(disabledMessage, "disabledMessage cannot be empty");
            return this;
        }

        /**
         * Sets categories for a shortcut.  Launcher apps may use this information to
         * categorize shortcuts.
         *
         * @see #SHORTCUT_CATEGORY_CONVERSATION
         * @see ShortcutInfo#getCategories()
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setCategories(java.util.Set<java.lang.String> categories) {
            mCategories = categories;
            return this;
        }

        /**
         * Sets the intent of a shortcut.  Alternatively, {@link #setIntents(Intent[])} can be used
         * to launch an activity with other activities in the back stack.
         *
         * <p>This is a mandatory field when publishing a new shortcut with
         * {@link ShortcutManager#addDynamicShortcuts(List)} or
         * {@link ShortcutManager#setDynamicShortcuts(List)}.
         *
         * <p>A shortcut can launch any intent that the publisher app has permission to
         * launch.  For example, a shortcut can launch an unexported activity within the publisher
         * app.  A shortcut intent doesn't have to point at the target activity.
         *
         * <p>The given {@code intent} can contain extras, but these extras must contain values
         * of primitive types in order for the system to persist these values.
         *
         * @see ShortcutInfo#getIntent()
         * @see #setIntents(Intent[])
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setIntent(@android.annotation.NonNull
        android.content.Intent intent) {
            return setIntents(new android.content.Intent[]{ intent });
        }

        /**
         * Sets multiple intents instead of a single intent, in order to launch an activity with
         * other activities in back stack.  Use {@link TaskStackBuilder} to build intents. The
         * last element in the list represents the only intent that doesn't place an activity on
         * the back stack.
         * See the {@link ShortcutManager} javadoc for details.
         *
         * @see Builder#setIntent(Intent)
         * @see ShortcutInfo#getIntents()
         * @see Context#startActivities(Intent[])
         * @see TaskStackBuilder
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setIntents(@android.annotation.NonNull
        android.content.Intent[] intents) {
            com.android.internal.util.Preconditions.checkNotNull(intents, "intents cannot be null");
            com.android.internal.util.Preconditions.checkNotNull(intents.length, "intents cannot be empty");
            for (android.content.Intent intent : intents) {
                com.android.internal.util.Preconditions.checkNotNull(intent, "intents cannot contain null");
                com.android.internal.util.Preconditions.checkNotNull(intent.getAction(), "intent's action must be set");
            }
            // Make sure always clone incoming intents.
            mIntents = android.content.pm.ShortcutInfo.cloneIntents(intents);
            return this;
        }

        /**
         * Add a person that is relevant to this shortcut. Alternatively,
         * {@link #setPersons(Person[])} can be used to add multiple persons to a shortcut.
         *
         * <p> This is an optional field, but the addition of person may cause this shortcut to
         * appear more prominently in the user interface (e.g. ShareSheet).
         *
         * <p> A person should usually contain a uri in order to benefit from the ranking boost.
         * However, even if no uri is provided, it's beneficial to provide people in the shortcut,
         * such that listeners and voice only devices can announce and handle them properly.
         *
         * @see Person
         * @see #setPersons(Person[])
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setPerson(@android.annotation.NonNull
        android.app.Person person) {
            return setPersons(new android.app.Person[]{ person });
        }

        /**
         * Sets multiple persons instead of a single person.
         *
         * @see Person
         * @see #setPerson(Person)
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setPersons(@android.annotation.NonNull
        android.app.Person[] persons) {
            com.android.internal.util.Preconditions.checkNotNull(persons, "persons cannot be null");
            com.android.internal.util.Preconditions.checkNotNull(persons.length, "persons cannot be empty");
            for (android.app.Person person : persons) {
                com.android.internal.util.Preconditions.checkNotNull(person, "persons cannot contain null");
            }
            mPersons = android.content.pm.ShortcutInfo.clonePersons(persons);
            return this;
        }

        /**
         * Sets if a shortcut would be valid even if it has been unpublished/invisible by the app
         * (as a dynamic or pinned shortcut). If it is long lived, it can be cached by various
         * system services even after it has been unpublished as a dynamic shortcut.
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setLongLived(boolean londLived) {
            mIsLongLived = londLived;
            return this;
        }

        /**
         * "Rank" of a shortcut, which is a non-negative value that's used by the launcher app
         * to sort shortcuts.
         *
         * See {@link ShortcutInfo#getRank()} for details.
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setRank(int rank) {
            com.android.internal.util.Preconditions.checkArgument(0 <= rank, "Rank cannot be negative or bigger than MAX_RANK");
            mRank = rank;
            return this;
        }

        /**
         * Extras that the app can set for any purpose.
         *
         * <p>Apps can store arbitrary shortcut metadata in extras and retrieve the
         * metadata later using {@link ShortcutInfo#getExtras()}.
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo.Builder setExtras(@android.annotation.NonNull
        android.os.PersistableBundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Creates a {@link ShortcutInfo} instance.
         */
        @android.annotation.NonNull
        public android.content.pm.ShortcutInfo build() {
            return new android.content.pm.ShortcutInfo(this);
        }
    }

    /**
     * Returns the ID of a shortcut.
     *
     * <p>Shortcut IDs are unique within each publisher app and must be stable across
     * devices so that shortcuts will still be valid when restored on a different device.
     * See {@link ShortcutManager} for details.
     */
    @android.annotation.NonNull
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Gets the {@link LocusId} associated with this shortcut.
     *
     * <p>Used by the Android system to correlate objects (such as
     * {@link Notification} and {@link ContentCaptureContext}).
     */
    @android.annotation.Nullable
    public android.content.LocusId getLocusId() {
        return mLocusId;
    }

    /**
     * Return the package name of the publisher app.
     */
    @android.annotation.NonNull
    public java.lang.String getPackage() {
        return mPackageName;
    }

    /**
     * Return the target activity.
     *
     * <p>This has nothing to do with the activity that this shortcut will launch.
     * Launcher apps should show the launcher icon for the returned activity alongside
     * this shortcut.
     *
     * @see Builder#setActivity
     */
    @android.annotation.Nullable
    public android.content.ComponentName getActivity() {
        return mActivity;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setActivity(android.content.ComponentName activity) {
        mActivity = activity;
    }

    /**
     * Returns the shortcut icon.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public android.graphics.drawable.Icon getIcon() {
        return mIcon;
    }

    /**
     *
     *
     * @unknown -- old signature, the internal code still uses it.
     */
    @android.annotation.Nullable
    @java.lang.Deprecated
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     *
     *
     * @unknown -- old signature, the internal code still uses it.
     */
    @java.lang.Deprecated
    public int getTitleResId() {
        return mTitleResId;
    }

    /**
     *
     *
     * @unknown -- old signature, the internal code still uses it.
     */
    @android.annotation.Nullable
    @java.lang.Deprecated
    public java.lang.CharSequence getText() {
        return mText;
    }

    /**
     *
     *
     * @unknown -- old signature, the internal code still uses it.
     */
    @java.lang.Deprecated
    public int getTextResId() {
        return mTextResId;
    }

    /**
     * Return the short description of a shortcut.
     *
     * @see Builder#setShortLabel(CharSequence)
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getShortLabel() {
        return mTitle;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getShortLabelResourceId() {
        return mTitleResId;
    }

    /**
     * Return the long description of a shortcut.
     *
     * @see Builder#setLongLabel(CharSequence)
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getLongLabel() {
        return mText;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLongLabelResourceId() {
        return mTextResId;
    }

    /**
     * Return the message that should be shown when the user attempts to start a shortcut
     * that is disabled.
     *
     * @see Builder#setDisabledMessage(CharSequence)
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getDisabledMessage() {
        return mDisabledMessage;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getDisabledMessageResourceId() {
        return mDisabledMessageResId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDisabledReason(@android.content.pm.ShortcutInfo.DisabledReason
    int reason) {
        mDisabledReason = reason;
    }

    /**
     * Returns why a shortcut has been disabled.
     */
    @android.content.pm.ShortcutInfo.DisabledReason
    public int getDisabledReason() {
        return mDisabledReason;
    }

    /**
     * Return the shortcut's categories.
     *
     * @see Builder#setCategories(Set)
     */
    @android.annotation.Nullable
    public java.util.Set<java.lang.String> getCategories() {
        return mCategories;
    }

    /**
     * Returns the intent that is executed when the user selects this shortcut.
     * If setIntents() was used, then return the last intent in the array.
     *
     * <p>Launcher apps <b>cannot</b> see the intent.  If a {@link ShortcutInfo} is
     * obtained via {@link LauncherApps}, then this method will always return null.
     * Launchers can only start a shortcut intent with {@link LauncherApps#startShortcut}.
     *
     * @see Builder#setIntent(Intent)
     */
    @android.annotation.Nullable
    public android.content.Intent getIntent() {
        if ((mIntents == null) || (mIntents.length == 0)) {
            return null;
        }
        final int last = mIntents.length - 1;
        final android.content.Intent intent = new android.content.Intent(mIntents[last]);
        return android.content.pm.ShortcutInfo.setIntentExtras(intent, mIntentPersistableExtrases[last]);
    }

    /**
     * Return the intent set with {@link Builder#setIntents(Intent[])}.
     *
     * <p>Launcher apps <b>cannot</b> see the intents.  If a {@link ShortcutInfo} is
     * obtained via {@link LauncherApps}, then this method will always return null.
     * Launchers can only start a shortcut intent with {@link LauncherApps#startShortcut}.
     *
     * @see Builder#setIntents(Intent[])
     */
    @android.annotation.Nullable
    public android.content.Intent[] getIntents() {
        final android.content.Intent[] ret = new android.content.Intent[mIntents.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new android.content.Intent(mIntents[i]);
            android.content.pm.ShortcutInfo.setIntentExtras(ret[i], mIntentPersistableExtrases[i]);
        }
        return ret;
    }

    /**
     * Return "raw" intents, which is the original intents without the extras.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public android.content.Intent[] getIntentsNoExtras() {
        return mIntents;
    }

    /**
     * Return the Persons set with {@link Builder#setPersons(Person[])}.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    @android.annotation.SystemApi
    public android.app.Person[] getPersons() {
        return android.content.pm.ShortcutInfo.clonePersons(mPersons);
    }

    /**
     * The extras in the intents.  We convert extras into {@link PersistableBundle} so we can
     * persist them.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public android.os.PersistableBundle[] getIntentPersistableExtrases() {
        return mIntentPersistableExtrases;
    }

    /**
     * "Rank" of a shortcut, which is a non-negative, sequential value that's unique for each
     * {@link #getActivity} for each of the two types of shortcuts (static and dynamic).
     *
     * <p>Because static shortcuts and dynamic shortcuts have overlapping ranks,
     * when a launcher app shows shortcuts for an activity, it should first show
     * the static shortcuts, followed by the dynamic shortcuts.  Within each of those categories,
     * shortcuts should be sorted by rank in ascending order.
     *
     * <p><em>Floating shortcuts</em>, or shortcuts that are neither static nor dynamic, will all
     * have rank 0, because they aren't sorted.
     *
     * See the {@link ShortcutManager}'s class javadoc for details.
     *
     * @see Builder#setRank(int)
     */
    public int getRank() {
        return mRank;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasRank() {
        return mRank != android.content.pm.ShortcutInfo.RANK_NOT_SET;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRank(int rank) {
        mRank = rank;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearImplicitRankAndRankChangedFlag() {
        mImplicitRank = 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setImplicitRank(int rank) {
        // Make sure to keep RANK_CHANGED_BIT.
        mImplicitRank = (mImplicitRank & android.content.pm.ShortcutInfo.RANK_CHANGED_BIT) | (rank & android.content.pm.ShortcutInfo.IMPLICIT_RANK_MASK);
    }

    /**
     *
     *
     * @unknown 
     */
    public int getImplicitRank() {
        return mImplicitRank & android.content.pm.ShortcutInfo.IMPLICIT_RANK_MASK;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRankChanged() {
        mImplicitRank |= android.content.pm.ShortcutInfo.RANK_CHANGED_BIT;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isRankChanged() {
        return (mImplicitRank & android.content.pm.ShortcutInfo.RANK_CHANGED_BIT) != 0;
    }

    /**
     * Extras that the app can set for any purpose.
     *
     * @see Builder#setExtras(PersistableBundle)
     */
    @android.annotation.Nullable
    public android.os.PersistableBundle getExtras() {
        return mExtras;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getUserId() {
        return mUserId;
    }

    /**
     * {@link UserHandle} on which the publisher created this shortcut.
     */
    public android.os.UserHandle getUserHandle() {
        return android.os.UserHandle.of(mUserId);
    }

    /**
     * Last time when any of the fields was updated.
     */
    public long getLastChangedTimestamp() {
        return mLastChangedTimestamp;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.content.pm.ShortcutInfo.ShortcutFlags
    public int getFlags() {
        return mFlags;
    }

    /**
     *
     *
     * @unknown 
     */
    public void replaceFlags(@android.content.pm.ShortcutInfo.ShortcutFlags
    int flags) {
        mFlags = flags;
    }

    /**
     *
     *
     * @unknown 
     */
    public void addFlags(@android.content.pm.ShortcutInfo.ShortcutFlags
    int flags) {
        mFlags |= flags;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearFlags(@android.content.pm.ShortcutInfo.ShortcutFlags
    int flags) {
        mFlags &= ~flags;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasFlags(@android.content.pm.ShortcutInfo.ShortcutFlags
    int flags) {
        return (mFlags & flags) == flags;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isReturnedByServer() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_RETURNED_BY_SERVICE);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setReturnedByServer() {
        addFlags(android.content.pm.ShortcutInfo.FLAG_RETURNED_BY_SERVICE);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isLongLived() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_LONG_LIVED);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLongLived() {
        addFlags(android.content.pm.ShortcutInfo.FLAG_LONG_LIVED);
    }

    /**
     * Return whether a shortcut is dynamic.
     */
    public boolean isDynamic() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_DYNAMIC);
    }

    /**
     * Return whether a shortcut is pinned.
     */
    public boolean isPinned() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_PINNED);
    }

    /**
     * Return whether a shortcut is static; that is, whether a shortcut is
     * published from AndroidManifest.xml.  If {@code true}, the shortcut is
     * also {@link #isImmutable()}.
     *
     * <p>When an app is upgraded and a shortcut is no longer published from AndroidManifest.xml,
     * this will be set to {@code false}.  If the shortcut is not pinned, then it'll disappear.
     * However, if it's pinned, it will still be visible, {@link #isEnabled()} will be
     * {@code false} and {@link #isImmutable()} will be {@code true}.
     */
    public boolean isDeclaredInManifest() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_MANIFEST);
    }

    /**
     *
     *
     * @unknown kept for unit tests
     */
    @java.lang.Deprecated
    public boolean isManifestShortcut() {
        return isDeclaredInManifest();
    }

    /**
     *
     *
     * @return true if pinned but neither static nor dynamic.
     * @unknown 
     */
    public boolean isFloating() {
        return isPinned() && (!(isDynamic() || isManifestShortcut()));
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isOriginallyFromManifest() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_IMMUTABLE);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isDynamicVisible() {
        return isDynamic() && isVisibleToPublisher();
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isPinnedVisible() {
        return isPinned() && isVisibleToPublisher();
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isManifestVisible() {
        return isDeclaredInManifest() && isVisibleToPublisher();
    }

    /**
     * Return if a shortcut is immutable, in which case it cannot be modified with any of
     * {@link ShortcutManager} APIs.
     *
     * <p>All static shortcuts are immutable.  When a static shortcut is pinned and is then
     * disabled because it doesn't appear in AndroidManifest.xml for a newer version of the
     * app, {@link #isDeclaredInManifest()} returns {@code false}, but the shortcut
     * is still immutable.
     *
     * <p>All shortcuts originally published via the {@link ShortcutManager} APIs
     * are all mutable.
     */
    public boolean isImmutable() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_IMMUTABLE);
    }

    /**
     * Returns {@code false} if a shortcut is disabled with
     * {@link ShortcutManager#disableShortcuts}.
     */
    public boolean isEnabled() {
        return !hasFlags(android.content.pm.ShortcutInfo.FLAG_DISABLED);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isAlive() {
        return (hasFlags(android.content.pm.ShortcutInfo.FLAG_PINNED) || hasFlags(android.content.pm.ShortcutInfo.FLAG_DYNAMIC)) || hasFlags(android.content.pm.ShortcutInfo.FLAG_MANIFEST);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean usesQuota() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_DYNAMIC) || hasFlags(android.content.pm.ShortcutInfo.FLAG_MANIFEST);
    }

    /**
     * Return whether a shortcut's icon is a resource in the owning package.
     *
     * @unknown internal/unit tests only
     */
    public boolean hasIconResource() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_HAS_ICON_RES);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasStringResources() {
        return ((mTitleResId != 0) || (mTextResId != 0)) || (mDisabledMessageResId != 0);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasAnyResources() {
        return hasIconResource() || hasStringResources();
    }

    /**
     * Return whether a shortcut's icon is stored as a file.
     *
     * @unknown internal/unit tests only
     */
    public boolean hasIconFile() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_HAS_ICON_FILE);
    }

    /**
     * Return whether a shortcut's icon is adaptive bitmap following design guideline
     * defined in {@link android.graphics.drawable.AdaptiveIconDrawable}.
     *
     * @unknown internal/unit tests only
     */
    public boolean hasAdaptiveBitmap() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_ADAPTIVE_BITMAP);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isIconPendingSave() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_ICON_FILE_PENDING_SAVE);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setIconPendingSave() {
        addFlags(android.content.pm.ShortcutInfo.FLAG_ICON_FILE_PENDING_SAVE);
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearIconPendingSave() {
        clearFlags(android.content.pm.ShortcutInfo.FLAG_ICON_FILE_PENDING_SAVE);
    }

    /**
     * When the system wasn't able to restore a shortcut, it'll still be registered to the system
     * but disabled, and such shortcuts will not be visible to the publisher. They're still visible
     * to launchers though.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public boolean isVisibleToPublisher() {
        return !android.content.pm.ShortcutInfo.isDisabledForRestoreIssue(mDisabledReason);
    }

    /**
     * Return whether a shortcut only contains "key" information only or not.  If true, only the
     * following fields are available.
     * <ul>
     *     <li>{@link #getId()}
     *     <li>{@link #getPackage()}
     *     <li>{@link #getActivity()}
     *     <li>{@link #getLastChangedTimestamp()}
     *     <li>{@link #isDynamic()}
     *     <li>{@link #isPinned()}
     *     <li>{@link #isDeclaredInManifest()}
     *     <li>{@link #isImmutable()}
     *     <li>{@link #isEnabled()}
     *     <li>{@link #getUserHandle()}
     * </ul>
     *
     * <p>For performance reasons, shortcuts passed to
     * {@link LauncherApps.Callback#onShortcutsChanged(String, List, UserHandle)} as well as those
     * returned from {@link LauncherApps#getShortcuts(ShortcutQuery, UserHandle)}
     * while using the {@link ShortcutQuery#FLAG_GET_KEY_FIELDS_ONLY} option contain only key
     * information.
     */
    public boolean hasKeyFieldsOnly() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_KEY_FIELDS_ONLY);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasStringResourcesResolved() {
        return hasFlags(android.content.pm.ShortcutInfo.FLAG_STRINGS_RESOLVED);
    }

    /**
     *
     *
     * @unknown 
     */
    public void updateTimestamp() {
        mLastChangedTimestamp = java.lang.System.currentTimeMillis();
    }

    /**
     *
     *
     * @unknown 
     */
    // VisibleForTesting
    public void setTimestamp(long value) {
        mLastChangedTimestamp = value;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearIcon() {
        mIcon = null;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setIconResourceId(int iconResourceId) {
        if (mIconResId != iconResourceId) {
            mIconResName = null;
        }
        mIconResId = iconResourceId;
    }

    /**
     * Get the resource ID for the icon, valid only when {@link #hasIconResource()} } is true.
     *
     * @unknown internal / tests only.
     */
    public int getIconResourceId() {
        return mIconResId;
    }

    /**
     * Bitmap path.  Note this will be null even if {@link #hasIconFile()} is set when the save
     * is pending.  Use {@link #isIconPendingSave()} to check it.
     *
     * @unknown 
     */
    public java.lang.String getBitmapPath() {
        return mBitmapPath;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setBitmapPath(java.lang.String bitmapPath) {
        mBitmapPath = bitmapPath;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDisabledMessageResId(int disabledMessageResId) {
        if (mDisabledMessageResId != disabledMessageResId) {
            mDisabledMessageResName = null;
        }
        mDisabledMessageResId = disabledMessageResId;
        mDisabledMessage = null;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDisabledMessage(java.lang.String disabledMessage) {
        mDisabledMessage = disabledMessage;
        mDisabledMessageResId = 0;
        mDisabledMessageResName = null;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getTitleResName() {
        return mTitleResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTitleResName(java.lang.String titleResName) {
        mTitleResName = titleResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getTextResName() {
        return mTextResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTextResName(java.lang.String textResName) {
        mTextResName = textResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getDisabledMessageResName() {
        return mDisabledMessageResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDisabledMessageResName(java.lang.String disabledMessageResName) {
        mDisabledMessageResName = disabledMessageResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getIconResName() {
        return mIconResName;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setIconResName(java.lang.String iconResName) {
        mIconResName = iconResName;
    }

    /**
     * Replaces the intent.
     *
     * @throws IllegalArgumentException
     * 		when extra is not compatible with {@link PersistableBundle}.
     * @unknown 
     */
    public void setIntents(android.content.Intent[] intents) throws java.lang.IllegalArgumentException {
        com.android.internal.util.Preconditions.checkNotNull(intents);
        com.android.internal.util.Preconditions.checkArgument(intents.length > 0);
        mIntents = android.content.pm.ShortcutInfo.cloneIntents(intents);
        fixUpIntentExtras();
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.content.Intent setIntentExtras(android.content.Intent intent, android.os.PersistableBundle extras) {
        if (extras == null) {
            intent.replaceExtras(((android.os.Bundle) (null)));
        } else {
            intent.replaceExtras(new android.os.Bundle(extras));
        }
        return intent;
    }

    /**
     * Replaces the categories.
     *
     * @unknown 
     */
    public void setCategories(java.util.Set<java.lang.String> categories) {
        mCategories = android.content.pm.ShortcutInfo.cloneCategories(categories);
    }

    private ShortcutInfo(android.os.Parcel source) {
        final java.lang.ClassLoader cl = getClass().getClassLoader();
        mUserId = source.readInt();
        mId = source.readString();
        mPackageName = source.readString();
        mActivity = source.readParcelable(cl);
        mFlags = source.readInt();
        mIconResId = source.readInt();
        mLastChangedTimestamp = source.readLong();
        mDisabledReason = source.readInt();
        if (source.readInt() == 0) {
            return;// key information only.

        }
        mIcon = source.readParcelable(cl);
        mTitle = source.readCharSequence();
        mTitleResId = source.readInt();
        mText = source.readCharSequence();
        mTextResId = source.readInt();
        mDisabledMessage = source.readCharSequence();
        mDisabledMessageResId = source.readInt();
        mIntents = source.readParcelableArray(cl, android.content.Intent.class);
        mIntentPersistableExtrases = source.readParcelableArray(cl, android.os.PersistableBundle.class);
        mRank = source.readInt();
        mExtras = source.readParcelable(cl);
        mBitmapPath = source.readString();
        mIconResName = source.readString();
        mTitleResName = source.readString();
        mTextResName = source.readString();
        mDisabledMessageResName = source.readString();
        int N = source.readInt();
        if (N == 0) {
            mCategories = null;
        } else {
            mCategories = new android.util.ArraySet(N);
            for (int i = 0; i < N; i++) {
                mCategories.add(source.readString().intern());
            }
        }
        mPersons = source.readParcelableArray(cl, android.app.Person.class);
        mLocusId = source.readParcelable(cl);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mUserId);
        dest.writeString(mId);
        dest.writeString(mPackageName);
        dest.writeParcelable(mActivity, flags);
        dest.writeInt(mFlags);
        dest.writeInt(mIconResId);
        dest.writeLong(mLastChangedTimestamp);
        dest.writeInt(mDisabledReason);
        if (hasKeyFieldsOnly()) {
            dest.writeInt(0);
            return;
        }
        dest.writeInt(1);
        dest.writeParcelable(mIcon, flags);
        dest.writeCharSequence(mTitle);
        dest.writeInt(mTitleResId);
        dest.writeCharSequence(mText);
        dest.writeInt(mTextResId);
        dest.writeCharSequence(mDisabledMessage);
        dest.writeInt(mDisabledMessageResId);
        dest.writeParcelableArray(mIntents, flags);
        dest.writeParcelableArray(mIntentPersistableExtrases, flags);
        dest.writeInt(mRank);
        dest.writeParcelable(mExtras, flags);
        dest.writeString(mBitmapPath);
        dest.writeString(mIconResName);
        dest.writeString(mTitleResName);
        dest.writeString(mTextResName);
        dest.writeString(mDisabledMessageResName);
        if (mCategories != null) {
            final int N = mCategories.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                dest.writeString(mCategories.valueAt(i));
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeParcelableArray(mPersons, flags);
        dest.writeParcelable(mLocusId, flags);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.ShortcutInfo> CREATOR = new android.content.pm.Creator<android.content.pm.ShortcutInfo>() {
        public android.content.pm.ShortcutInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.ShortcutInfo(source);
        }

        public android.content.pm.ShortcutInfo[] newArray(int size) {
            return new android.content.pm.ShortcutInfo[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Return a string representation, intended for logging.  Some fields will be retracted.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return /* secure = */
        /* includeInternalData = */
        /* indent= */
        toStringInner(true, false, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String toInsecureString() {
        return /* secure = */
        /* includeInternalData = */
        /* indent= */
        toStringInner(false, true, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String toDumpString(java.lang.String indent) {
        return /* secure = */
        /* includeInternalData = */
        toStringInner(false, true, indent);
    }

    private void addIndentOrComma(java.lang.StringBuilder sb, java.lang.String indent) {
        if (indent != null) {
            sb.append("\n  ");
            sb.append(indent);
        } else {
            sb.append(", ");
        }
    }

    private java.lang.String toStringInner(boolean secure, boolean includeInternalData, java.lang.String indent) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (indent != null) {
            sb.append(indent);
        }
        sb.append("ShortcutInfo {");
        sb.append("id=");
        sb.append(secure ? "***" : mId);
        sb.append(", flags=0x");
        sb.append(java.lang.Integer.toHexString(mFlags));
        sb.append(" [");
        if ((mFlags & android.content.pm.ShortcutInfo.FLAG_SHADOW) != 0) {
            // Note the shadow flag isn't actually used anywhere and it's just for dumpsys, so
            // we don't have an isXxx for this.
            sb.append("Sdw");
        }
        if (!isEnabled()) {
            sb.append("Dis");
        }
        if (isImmutable()) {
            sb.append("Im");
        }
        if (isManifestShortcut()) {
            sb.append("Man");
        }
        if (isDynamic()) {
            sb.append("Dyn");
        }
        if (isPinned()) {
            sb.append("Pin");
        }
        if (hasIconFile()) {
            sb.append("Ic-f");
        }
        if (isIconPendingSave()) {
            sb.append("Pens");
        }
        if (hasIconResource()) {
            sb.append("Ic-r");
        }
        if (hasKeyFieldsOnly()) {
            sb.append("Key");
        }
        if (hasStringResourcesResolved()) {
            sb.append("Str");
        }
        if (isReturnedByServer()) {
            sb.append("Rets");
        }
        if (isLongLived()) {
            sb.append("Liv");
        }
        sb.append("]");
        addIndentOrComma(sb, indent);
        sb.append("packageName=");
        sb.append(mPackageName);
        addIndentOrComma(sb, indent);
        sb.append("activity=");
        sb.append(mActivity);
        addIndentOrComma(sb, indent);
        sb.append("shortLabel=");
        sb.append(secure ? "***" : mTitle);
        sb.append(", resId=");
        sb.append(mTitleResId);
        sb.append("[");
        sb.append(mTitleResName);
        sb.append("]");
        addIndentOrComma(sb, indent);
        sb.append("longLabel=");
        sb.append(secure ? "***" : mText);
        sb.append(", resId=");
        sb.append(mTextResId);
        sb.append("[");
        sb.append(mTextResName);
        sb.append("]");
        addIndentOrComma(sb, indent);
        sb.append("disabledMessage=");
        sb.append(secure ? "***" : mDisabledMessage);
        sb.append(", resId=");
        sb.append(mDisabledMessageResId);
        sb.append("[");
        sb.append(mDisabledMessageResName);
        sb.append("]");
        addIndentOrComma(sb, indent);
        sb.append("disabledReason=");
        sb.append(android.content.pm.ShortcutInfo.getDisabledReasonDebugString(mDisabledReason));
        addIndentOrComma(sb, indent);
        sb.append("categories=");
        sb.append(mCategories);
        addIndentOrComma(sb, indent);
        sb.append("persons=");
        sb.append(mPersons);
        addIndentOrComma(sb, indent);
        sb.append("icon=");
        sb.append(mIcon);
        addIndentOrComma(sb, indent);
        sb.append("rank=");
        sb.append(mRank);
        sb.append(", timestamp=");
        sb.append(mLastChangedTimestamp);
        addIndentOrComma(sb, indent);
        sb.append("intents=");
        if (mIntents == null) {
            sb.append("null");
        } else {
            if (secure) {
                sb.append("size:");
                sb.append(mIntents.length);
            } else {
                final int size = mIntents.length;
                sb.append("[");
                java.lang.String sep = "";
                for (int i = 0; i < size; i++) {
                    sb.append(sep);
                    sep = ", ";
                    sb.append(mIntents[i]);
                    sb.append("/");
                    sb.append(mIntentPersistableExtrases[i]);
                }
                sb.append("]");
            }
        }
        addIndentOrComma(sb, indent);
        sb.append("extras=");
        sb.append(mExtras);
        if (includeInternalData) {
            addIndentOrComma(sb, indent);
            sb.append("iconRes=");
            sb.append(mIconResId);
            sb.append("[");
            sb.append(mIconResName);
            sb.append("]");
            sb.append(", bitmapPath=");
            sb.append(mBitmapPath);
        }
        if (mLocusId != null) {
            sb.append("locusId=");
            sb.append(mLocusId);// LocusId.toString() is PII-safe.

        }
        sb.append("}");
        return sb.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public ShortcutInfo(@android.annotation.UserIdInt
    int userId, java.lang.String id, java.lang.String packageName, android.content.ComponentName activity, android.graphics.drawable.Icon icon, java.lang.CharSequence title, int titleResId, java.lang.String titleResName, java.lang.CharSequence text, int textResId, java.lang.String textResName, java.lang.CharSequence disabledMessage, int disabledMessageResId, java.lang.String disabledMessageResName, java.util.Set<java.lang.String> categories, android.content.Intent[] intentsWithExtras, int rank, android.os.PersistableBundle extras, long lastChangedTimestamp, int flags, int iconResId, java.lang.String iconResName, java.lang.String bitmapPath, int disabledReason, android.app.Person[] persons, android.content.LocusId locusId) {
        mUserId = userId;
        mId = id;
        mPackageName = packageName;
        mActivity = activity;
        mIcon = icon;
        mTitle = title;
        mTitleResId = titleResId;
        mTitleResName = titleResName;
        mText = text;
        mTextResId = textResId;
        mTextResName = textResName;
        mDisabledMessage = disabledMessage;
        mDisabledMessageResId = disabledMessageResId;
        mDisabledMessageResName = disabledMessageResName;
        mCategories = android.content.pm.ShortcutInfo.cloneCategories(categories);
        mIntents = android.content.pm.ShortcutInfo.cloneIntents(intentsWithExtras);
        fixUpIntentExtras();
        mRank = rank;
        mExtras = extras;
        mLastChangedTimestamp = lastChangedTimestamp;
        mFlags = flags;
        mIconResId = iconResId;
        mIconResName = iconResName;
        mBitmapPath = bitmapPath;
        mDisabledReason = disabledReason;
        mPersons = persons;
        mLocusId = locusId;
    }
}

