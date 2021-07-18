/**
 * Copyright (C) 2014 The Android Open Source Project
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


class NotificationCompatApi20 {
    public static class Builder implements android.support.v4.app.NotificationBuilderWithActions , android.support.v4.app.NotificationBuilderWithBuilderAccessor {
        private android.app.Notification.Builder b;

        private android.os.Bundle mExtras;

        private android.widget.RemoteViews mContentView;

        private android.widget.RemoteViews mBigContentView;

        public Builder(android.content.Context context, android.app.Notification n, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, android.widget.RemoteViews tickerView, int number, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent, android.graphics.Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean showWhen, boolean useChronometer, int priority, java.lang.CharSequence subText, boolean localOnly, java.util.ArrayList<java.lang.String> people, android.os.Bundle extras, java.lang.String groupKey, boolean groupSummary, java.lang.String sortKey, android.widget.RemoteViews contentView, android.widget.RemoteViews bigContentView) {
            b = new android.app.Notification.Builder(context).setWhen(n.when).setShowWhen(showWhen).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0).setOnlyAlertOnce((n.flags & android.app.Notification.FLAG_ONLY_ALERT_ONCE) != 0).setAutoCancel((n.flags & android.app.Notification.FLAG_AUTO_CANCEL) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & android.app.Notification.FLAG_HIGH_PRIORITY) != 0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate).setLocalOnly(localOnly).setGroup(groupKey).setGroupSummary(groupSummary).setSortKey(sortKey);
            mExtras = new android.os.Bundle();
            if (extras != null) {
                mExtras.putAll(extras);
            }
            if ((people != null) && (!people.isEmpty())) {
                mExtras.putStringArray(android.app.Notification.EXTRA_PEOPLE, people.toArray(new java.lang.String[people.size()]));
            }
            mContentView = contentView;
            mBigContentView = bigContentView;
        }

        @java.lang.Override
        public void addAction(android.support.v4.app.NotificationCompatBase.Action action) {
            android.support.v4.app.NotificationCompatApi20.addAction(b, action);
        }

        @java.lang.Override
        public android.app.Notification.Builder getBuilder() {
            return b;
        }

        @java.lang.Override
        public android.app.Notification build() {
            b.setExtras(mExtras);
            android.app.Notification notification = b.build();
            if (mContentView != null) {
                notification.contentView = mContentView;
            }
            if (mBigContentView != null) {
                notification.bigContentView = mBigContentView;
            }
            return notification;
        }
    }

    public static void addAction(android.app.Notification.Builder b, android.support.v4.app.NotificationCompatBase.Action action) {
        android.app.Notification.Action.Builder actionBuilder = new android.app.Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            for (android.app.RemoteInput remoteInput : android.support.v4.app.RemoteInputCompatApi20.fromCompat(action.getRemoteInputs())) {
                actionBuilder.addRemoteInput(remoteInput);
            }
        }
        android.os.Bundle actionExtras;
        if (action.getExtras() != null) {
            actionExtras = new android.os.Bundle(action.getExtras());
        } else {
            actionExtras = new android.os.Bundle();
        }
        actionExtras.putBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES, action.getAllowGeneratedReplies());
        actionBuilder.addExtras(actionExtras);
        b.addAction(actionBuilder.build());
    }

    public static android.support.v4.app.NotificationCompatBase.Action getAction(android.app.Notification notif, int actionIndex, android.support.v4.app.NotificationCompatBase.Action.Factory actionFactory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        return android.support.v4.app.NotificationCompatApi20.getActionCompatFromAction(notif.actions[actionIndex], actionFactory, remoteInputFactory);
    }

    private static android.support.v4.app.NotificationCompatBase.Action getActionCompatFromAction(android.app.Notification.Action action, android.support.v4.app.NotificationCompatBase.Action.Factory actionFactory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs = android.support.v4.app.RemoteInputCompatApi20.toCompat(action.getRemoteInputs(), remoteInputFactory);
        boolean allowGeneratedReplies = action.getExtras().getBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES);
        return actionFactory.build(action.icon, action.title, action.actionIntent, action.getExtras(), remoteInputs, allowGeneratedReplies);
    }

    private static android.app.Notification.Action getActionFromActionCompat(android.support.v4.app.NotificationCompatBase.Action actionCompat) {
        android.app.Notification.Action.Builder actionBuilder = new android.app.Notification.Action.Builder(actionCompat.getIcon(), actionCompat.getTitle(), actionCompat.getActionIntent()).addExtras(actionCompat.getExtras());
        android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputCompats = actionCompat.getRemoteInputs();
        if (remoteInputCompats != null) {
            android.app.RemoteInput[] remoteInputs = android.support.v4.app.RemoteInputCompatApi20.fromCompat(remoteInputCompats);
            for (android.app.RemoteInput remoteInput : remoteInputs) {
                actionBuilder.addRemoteInput(remoteInput);
            }
        }
        return actionBuilder.build();
    }

    /**
     * Get a list of notification compat actions by parsing actions stored within a list of
     * parcelables using the {@link Bundle#getParcelableArrayList} function in the same
     * manner that framework code would do so. In API20, Using Action parcelable directly
     * is correct.
     */
    public static android.support.v4.app.NotificationCompatBase.Action[] getActionsFromParcelableArrayList(java.util.ArrayList<android.os.Parcelable> parcelables, android.support.v4.app.NotificationCompatBase.Action.Factory actionFactory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        if (parcelables == null) {
            return null;
        }
        android.support.v4.app.NotificationCompatBase.Action[] actions = actionFactory.newArray(parcelables.size());
        for (int i = 0; i < actions.length; i++) {
            android.app.Notification.Action action = ((android.app.Notification.Action) (parcelables.get(i)));
            actions[i] = android.support.v4.app.NotificationCompatApi20.getActionCompatFromAction(action, actionFactory, remoteInputFactory);
        }
        return actions;
    }

    /**
     * Get an array list of parcelables, suitable for {@link Bundle#putParcelableArrayList},
     * that matches what framework code would do to store an actions list in this way. In API20,
     * action parcelables were directly placed as entries in the array list.
     */
    public static java.util.ArrayList<android.os.Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompatBase.Action[] actions) {
        if (actions == null) {
            return null;
        }
        java.util.ArrayList<android.os.Parcelable> parcelables = new java.util.ArrayList<android.os.Parcelable>(actions.length);
        for (android.support.v4.app.NotificationCompatBase.Action action : actions) {
            parcelables.add(android.support.v4.app.NotificationCompatApi20.getActionFromActionCompat(action));
        }
        return parcelables;
    }

    public static boolean getLocalOnly(android.app.Notification notif) {
        return (notif.flags & android.app.Notification.FLAG_LOCAL_ONLY) != 0;
    }

    public static java.lang.String getGroup(android.app.Notification notif) {
        return notif.getGroup();
    }

    public static boolean isGroupSummary(android.app.Notification notif) {
        return (notif.flags & android.app.Notification.FLAG_GROUP_SUMMARY) != 0;
    }

    public static java.lang.String getSortKey(android.app.Notification notif) {
        return notif.getSortKey();
    }
}

