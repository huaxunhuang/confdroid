/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v4.app;


class NotificationCompatJellybean {
    public static final java.lang.String TAG = "NotificationCompat";

    // Extras keys used for Jellybean SDK and above.
    static final java.lang.String EXTRA_LOCAL_ONLY = "android.support.localOnly";

    static final java.lang.String EXTRA_ACTION_EXTRAS = "android.support.actionExtras";

    static final java.lang.String EXTRA_REMOTE_INPUTS = "android.support.remoteInputs";

    static final java.lang.String EXTRA_GROUP_KEY = "android.support.groupKey";

    static final java.lang.String EXTRA_GROUP_SUMMARY = "android.support.isGroupSummary";

    static final java.lang.String EXTRA_SORT_KEY = "android.support.sortKey";

    static final java.lang.String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";

    static final java.lang.String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";

    // Bundle keys for storing action fields in a bundle
    private static final java.lang.String KEY_ICON = "icon";

    private static final java.lang.String KEY_TITLE = "title";

    private static final java.lang.String KEY_ACTION_INTENT = "actionIntent";

    private static final java.lang.String KEY_EXTRAS = "extras";

    private static final java.lang.String KEY_REMOTE_INPUTS = "remoteInputs";

    private static final java.lang.String KEY_ALLOW_GENERATED_REPLIES = "allowGeneratedReplies";

    private static final java.lang.Object sExtrasLock = new java.lang.Object();

    private static java.lang.reflect.Field sExtrasField;

    private static boolean sExtrasFieldAccessFailed;

    private static final java.lang.Object sActionsLock = new java.lang.Object();

    private static java.lang.Class<?> sActionClass;

    private static java.lang.reflect.Field sActionsField;

    private static java.lang.reflect.Field sActionIconField;

    private static java.lang.reflect.Field sActionTitleField;

    private static java.lang.reflect.Field sActionIntentField;

    private static boolean sActionsAccessFailed;

    public static class Builder implements android.support.v4.app.NotificationBuilderWithActions , android.support.v4.app.NotificationBuilderWithBuilderAccessor {
        private android.app.Notification.Builder b;

        private final android.os.Bundle mExtras;

        private java.util.List<android.os.Bundle> mActionExtrasList = new java.util.ArrayList<android.os.Bundle>();

        private android.widget.RemoteViews mContentView;

        private android.widget.RemoteViews mBigContentView;

        public Builder(android.content.Context context, android.app.Notification n, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, android.widget.RemoteViews tickerView, int number, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent, android.graphics.Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean useChronometer, int priority, java.lang.CharSequence subText, boolean localOnly, android.os.Bundle extras, java.lang.String groupKey, boolean groupSummary, java.lang.String sortKey, android.widget.RemoteViews contentView, android.widget.RemoteViews bigContentView) {
            b = new android.app.Notification.Builder(context).setWhen(n.when).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0).setOnlyAlertOnce((n.flags & android.app.Notification.FLAG_ONLY_ALERT_ONCE) != 0).setAutoCancel((n.flags & android.app.Notification.FLAG_AUTO_CANCEL) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & android.app.Notification.FLAG_HIGH_PRIORITY) != 0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate);
            mExtras = new android.os.Bundle();
            if (extras != null) {
                mExtras.putAll(extras);
            }
            if (localOnly) {
                mExtras.putBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_LOCAL_ONLY, true);
            }
            if (groupKey != null) {
                mExtras.putString(android.support.v4.app.NotificationCompatJellybean.EXTRA_GROUP_KEY, groupKey);
                if (groupSummary) {
                    mExtras.putBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_GROUP_SUMMARY, true);
                } else {
                    mExtras.putBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_USE_SIDE_CHANNEL, true);
                }
            }
            if (sortKey != null) {
                mExtras.putString(android.support.v4.app.NotificationCompatJellybean.EXTRA_SORT_KEY, sortKey);
            }
            mContentView = contentView;
            mBigContentView = bigContentView;
        }

        @java.lang.Override
        public void addAction(android.support.v4.app.NotificationCompatBase.Action action) {
            mActionExtrasList.add(android.support.v4.app.NotificationCompatJellybean.writeActionAndGetExtras(b, action));
        }

        @java.lang.Override
        public android.app.Notification.Builder getBuilder() {
            return b;
        }

        public android.app.Notification build() {
            android.app.Notification notif = b.build();
            // Merge in developer provided extras, but let the values already set
            // for keys take precedence.
            android.os.Bundle extras = android.support.v4.app.NotificationCompatJellybean.getExtras(notif);
            android.os.Bundle mergeBundle = new android.os.Bundle(mExtras);
            for (java.lang.String key : mExtras.keySet()) {
                if (extras.containsKey(key)) {
                    mergeBundle.remove(key);
                }
            }
            extras.putAll(mergeBundle);
            android.util.SparseArray<android.os.Bundle> actionExtrasMap = android.support.v4.app.NotificationCompatJellybean.buildActionExtrasMap(mActionExtrasList);
            if (actionExtrasMap != null) {
                // Add the action extras sparse array if any action was added with extras.
                android.support.v4.app.NotificationCompatJellybean.getExtras(notif).putSparseParcelableArray(android.support.v4.app.NotificationCompatJellybean.EXTRA_ACTION_EXTRAS, actionExtrasMap);
            }
            if (mContentView != null) {
                notif.contentView = mContentView;
            }
            if (mBigContentView != null) {
                notif.bigContentView = mBigContentView;
            }
            return notif;
        }
    }

    public static void addBigTextStyle(android.support.v4.app.NotificationBuilderWithBuilderAccessor b, java.lang.CharSequence bigContentTitle, boolean useSummary, java.lang.CharSequence summaryText, java.lang.CharSequence bigText) {
        android.app.Notification.BigTextStyle style = new android.app.Notification.BigTextStyle(b.getBuilder()).setBigContentTitle(bigContentTitle).bigText(bigText);
        if (useSummary) {
            style.setSummaryText(summaryText);
        }
    }

    public static void addBigPictureStyle(android.support.v4.app.NotificationBuilderWithBuilderAccessor b, java.lang.CharSequence bigContentTitle, boolean useSummary, java.lang.CharSequence summaryText, android.graphics.Bitmap bigPicture, android.graphics.Bitmap bigLargeIcon, boolean bigLargeIconSet) {
        android.app.Notification.BigPictureStyle style = new android.app.Notification.BigPictureStyle(b.getBuilder()).setBigContentTitle(bigContentTitle).bigPicture(bigPicture);
        if (bigLargeIconSet) {
            style.bigLargeIcon(bigLargeIcon);
        }
        if (useSummary) {
            style.setSummaryText(summaryText);
        }
    }

    public static void addInboxStyle(android.support.v4.app.NotificationBuilderWithBuilderAccessor b, java.lang.CharSequence bigContentTitle, boolean useSummary, java.lang.CharSequence summaryText, java.util.ArrayList<java.lang.CharSequence> texts) {
        android.app.Notification.InboxStyle style = new android.app.Notification.InboxStyle(b.getBuilder()).setBigContentTitle(bigContentTitle);
        if (useSummary) {
            style.setSummaryText(summaryText);
        }
        for (java.lang.CharSequence text : texts) {
            style.addLine(text);
        }
    }

    /**
     * Return an SparseArray for action extras or null if none was needed.
     */
    public static android.util.SparseArray<android.os.Bundle> buildActionExtrasMap(java.util.List<android.os.Bundle> actionExtrasList) {
        android.util.SparseArray<android.os.Bundle> actionExtrasMap = null;
        for (int i = 0, count = actionExtrasList.size(); i < count; i++) {
            android.os.Bundle actionExtras = actionExtrasList.get(i);
            if (actionExtras != null) {
                if (actionExtrasMap == null) {
                    actionExtrasMap = new android.util.SparseArray<android.os.Bundle>();
                }
                actionExtrasMap.put(i, actionExtras);
            }
        }
        return actionExtrasMap;
    }

    /**
     * Get the extras Bundle from a notification using reflection. Extras were present in
     * Jellybean notifications, but the field was private until KitKat.
     */
    public static android.os.Bundle getExtras(android.app.Notification notif) {
        synchronized(android.support.v4.app.NotificationCompatJellybean.sExtrasLock) {
            if (android.support.v4.app.NotificationCompatJellybean.sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                if (android.support.v4.app.NotificationCompatJellybean.sExtrasField == null) {
                    java.lang.reflect.Field extrasField = android.app.Notification.class.getDeclaredField("extras");
                    if (!android.os.Bundle.class.isAssignableFrom(extrasField.getType())) {
                        android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Notification.extras field is not of type Bundle");
                        android.support.v4.app.NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    extrasField.setAccessible(true);
                    android.support.v4.app.NotificationCompatJellybean.sExtrasField = extrasField;
                }
                android.os.Bundle extras = ((android.os.Bundle) (android.support.v4.app.NotificationCompatJellybean.sExtrasField.get(notif)));
                if (extras == null) {
                    extras = new android.os.Bundle();
                    android.support.v4.app.NotificationCompatJellybean.sExtrasField.set(notif, extras);
                }
                return extras;
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Unable to access notification extras", e);
            } catch (java.lang.NoSuchFieldException e) {
                android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Unable to access notification extras", e);
            }
            android.support.v4.app.NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
            return null;
        }
    }

    public static android.support.v4.app.NotificationCompatBase.Action readAction(android.support.v4.app.NotificationCompatBase.Action.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory, int icon, java.lang.CharSequence title, android.app.PendingIntent actionIntent, android.os.Bundle extras) {
        android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs = null;
        boolean allowGeneratedReplies = false;
        if (extras != null) {
            remoteInputs = android.support.v4.app.RemoteInputCompatJellybean.fromBundleArray(android.support.v4.app.BundleUtil.getBundleArrayFromBundle(extras, android.support.v4.app.NotificationCompatJellybean.EXTRA_REMOTE_INPUTS), remoteInputFactory);
            allowGeneratedReplies = extras.getBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES);
        }
        return factory.build(icon, title, actionIntent, extras, remoteInputs, allowGeneratedReplies);
    }

    public static android.os.Bundle writeActionAndGetExtras(android.app.Notification.Builder builder, android.support.v4.app.NotificationCompatBase.Action action) {
        builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        android.os.Bundle actionExtras = new android.os.Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            actionExtras.putParcelableArray(android.support.v4.app.NotificationCompatJellybean.EXTRA_REMOTE_INPUTS, android.support.v4.app.RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        }
        actionExtras.putBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES, action.getAllowGeneratedReplies());
        return actionExtras;
    }

    public static int getActionCount(android.app.Notification notif) {
        synchronized(android.support.v4.app.NotificationCompatJellybean.sActionsLock) {
            java.lang.Object[] actionObjects = android.support.v4.app.NotificationCompatJellybean.getActionObjectsLocked(notif);
            return actionObjects != null ? actionObjects.length : 0;
        }
    }

    public static android.support.v4.app.NotificationCompatBase.Action getAction(android.app.Notification notif, int actionIndex, android.support.v4.app.NotificationCompatBase.Action.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        synchronized(android.support.v4.app.NotificationCompatJellybean.sActionsLock) {
            try {
                java.lang.Object actionObject = android.support.v4.app.NotificationCompatJellybean.getActionObjectsLocked(notif)[actionIndex];
                android.os.Bundle actionExtras = null;
                android.os.Bundle extras = android.support.v4.app.NotificationCompatJellybean.getExtras(notif);
                if (extras != null) {
                    android.util.SparseArray<android.os.Bundle> actionExtrasMap = extras.getSparseParcelableArray(android.support.v4.app.NotificationCompatJellybean.EXTRA_ACTION_EXTRAS);
                    if (actionExtrasMap != null) {
                        actionExtras = actionExtrasMap.get(actionIndex);
                    }
                }
                return android.support.v4.app.NotificationCompatJellybean.readAction(factory, remoteInputFactory, android.support.v4.app.NotificationCompatJellybean.sActionIconField.getInt(actionObject), ((java.lang.CharSequence) (android.support.v4.app.NotificationCompatJellybean.sActionTitleField.get(actionObject))), ((android.app.PendingIntent) (android.support.v4.app.NotificationCompatJellybean.sActionIntentField.get(actionObject))), actionExtras);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Unable to access notification actions", e);
                android.support.v4.app.NotificationCompatJellybean.sActionsAccessFailed = true;
            }
        }
        return null;
    }

    private static java.lang.Object[] getActionObjectsLocked(android.app.Notification notif) {
        synchronized(android.support.v4.app.NotificationCompatJellybean.sActionsLock) {
            if (!android.support.v4.app.NotificationCompatJellybean.ensureActionReflectionReadyLocked()) {
                return null;
            }
            try {
                return ((java.lang.Object[]) (android.support.v4.app.NotificationCompatJellybean.sActionsField.get(notif)));
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Unable to access notification actions", e);
                android.support.v4.app.NotificationCompatJellybean.sActionsAccessFailed = true;
                return null;
            }
        }
    }

    private static boolean ensureActionReflectionReadyLocked() {
        if (android.support.v4.app.NotificationCompatJellybean.sActionsAccessFailed) {
            return false;
        }
        try {
            if (android.support.v4.app.NotificationCompatJellybean.sActionsField == null) {
                android.support.v4.app.NotificationCompatJellybean.sActionClass = java.lang.Class.forName("android.app.Notification$Action");
                android.support.v4.app.NotificationCompatJellybean.sActionIconField = android.support.v4.app.NotificationCompatJellybean.sActionClass.getDeclaredField("icon");
                android.support.v4.app.NotificationCompatJellybean.sActionTitleField = android.support.v4.app.NotificationCompatJellybean.sActionClass.getDeclaredField("title");
                android.support.v4.app.NotificationCompatJellybean.sActionIntentField = android.support.v4.app.NotificationCompatJellybean.sActionClass.getDeclaredField("actionIntent");
                android.support.v4.app.NotificationCompatJellybean.sActionsField = android.app.Notification.class.getDeclaredField("actions");
                android.support.v4.app.NotificationCompatJellybean.sActionsField.setAccessible(true);
            }
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Unable to access notification actions", e);
            android.support.v4.app.NotificationCompatJellybean.sActionsAccessFailed = true;
        } catch (java.lang.NoSuchFieldException e) {
            android.util.Log.e(android.support.v4.app.NotificationCompatJellybean.TAG, "Unable to access notification actions", e);
            android.support.v4.app.NotificationCompatJellybean.sActionsAccessFailed = true;
        }
        return !android.support.v4.app.NotificationCompatJellybean.sActionsAccessFailed;
    }

    public static android.support.v4.app.NotificationCompatBase.Action[] getActionsFromParcelableArrayList(java.util.ArrayList<android.os.Parcelable> parcelables, android.support.v4.app.NotificationCompatBase.Action.Factory actionFactory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        if (parcelables == null) {
            return null;
        }
        android.support.v4.app.NotificationCompatBase.Action[] actions = actionFactory.newArray(parcelables.size());
        for (int i = 0; i < actions.length; i++) {
            actions[i] = android.support.v4.app.NotificationCompatJellybean.getActionFromBundle(((android.os.Bundle) (parcelables.get(i))), actionFactory, remoteInputFactory);
        }
        return actions;
    }

    private static android.support.v4.app.NotificationCompatBase.Action getActionFromBundle(android.os.Bundle bundle, android.support.v4.app.NotificationCompatBase.Action.Factory actionFactory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        return actionFactory.build(bundle.getInt(android.support.v4.app.NotificationCompatJellybean.KEY_ICON), bundle.getCharSequence(android.support.v4.app.NotificationCompatJellybean.KEY_TITLE), bundle.<android.app.PendingIntent>getParcelable(android.support.v4.app.NotificationCompatJellybean.KEY_ACTION_INTENT), bundle.getBundle(android.support.v4.app.NotificationCompatJellybean.KEY_EXTRAS), android.support.v4.app.RemoteInputCompatJellybean.fromBundleArray(android.support.v4.app.BundleUtil.getBundleArrayFromBundle(bundle, android.support.v4.app.NotificationCompatJellybean.KEY_REMOTE_INPUTS), remoteInputFactory), bundle.getBoolean(android.support.v4.app.NotificationCompatJellybean.KEY_ALLOW_GENERATED_REPLIES));
    }

    public static java.util.ArrayList<android.os.Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompatBase.Action[] actions) {
        if (actions == null) {
            return null;
        }
        java.util.ArrayList<android.os.Parcelable> parcelables = new java.util.ArrayList<android.os.Parcelable>(actions.length);
        for (android.support.v4.app.NotificationCompatBase.Action action : actions) {
            parcelables.add(android.support.v4.app.NotificationCompatJellybean.getBundleForAction(action));
        }
        return parcelables;
    }

    private static android.os.Bundle getBundleForAction(android.support.v4.app.NotificationCompatBase.Action action) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putInt(android.support.v4.app.NotificationCompatJellybean.KEY_ICON, action.getIcon());
        bundle.putCharSequence(android.support.v4.app.NotificationCompatJellybean.KEY_TITLE, action.getTitle());
        bundle.putParcelable(android.support.v4.app.NotificationCompatJellybean.KEY_ACTION_INTENT, action.getActionIntent());
        bundle.putBundle(android.support.v4.app.NotificationCompatJellybean.KEY_EXTRAS, action.getExtras());
        bundle.putParcelableArray(android.support.v4.app.NotificationCompatJellybean.KEY_REMOTE_INPUTS, android.support.v4.app.RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        return bundle;
    }

    public static boolean getLocalOnly(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompatJellybean.getExtras(notif).getBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_LOCAL_ONLY);
    }

    public static java.lang.String getGroup(android.app.Notification n) {
        return android.support.v4.app.NotificationCompatJellybean.getExtras(n).getString(android.support.v4.app.NotificationCompatJellybean.EXTRA_GROUP_KEY);
    }

    public static boolean isGroupSummary(android.app.Notification n) {
        return android.support.v4.app.NotificationCompatJellybean.getExtras(n).getBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_GROUP_SUMMARY);
    }

    public static java.lang.String getSortKey(android.app.Notification n) {
        return android.support.v4.app.NotificationCompatJellybean.getExtras(n).getString(android.support.v4.app.NotificationCompatJellybean.EXTRA_SORT_KEY);
    }
}

