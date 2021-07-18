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


class NotificationCompatKitKat {
    public static class Builder implements android.support.v4.app.NotificationBuilderWithActions , android.support.v4.app.NotificationBuilderWithBuilderAccessor {
        private android.app.Notification.Builder b;

        private android.os.Bundle mExtras;

        private java.util.List<android.os.Bundle> mActionExtrasList = new java.util.ArrayList<android.os.Bundle>();

        private android.widget.RemoteViews mContentView;

        private android.widget.RemoteViews mBigContentView;

        public Builder(android.content.Context context, android.app.Notification n, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, android.widget.RemoteViews tickerView, int number, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent, android.graphics.Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean showWhen, boolean useChronometer, int priority, java.lang.CharSequence subText, boolean localOnly, java.util.ArrayList<java.lang.String> people, android.os.Bundle extras, java.lang.String groupKey, boolean groupSummary, java.lang.String sortKey, android.widget.RemoteViews contentView, android.widget.RemoteViews bigContentView) {
            b = new android.app.Notification.Builder(context).setWhen(n.when).setShowWhen(showWhen).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0).setOnlyAlertOnce((n.flags & android.app.Notification.FLAG_ONLY_ALERT_ONCE) != 0).setAutoCancel((n.flags & android.app.Notification.FLAG_AUTO_CANCEL) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & android.app.Notification.FLAG_HIGH_PRIORITY) != 0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate);
            mExtras = new android.os.Bundle();
            if (extras != null) {
                mExtras.putAll(extras);
            }
            if ((people != null) && (!people.isEmpty())) {
                mExtras.putStringArray(android.app.Notification.EXTRA_PEOPLE, people.toArray(new java.lang.String[people.size()]));
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

        @java.lang.Override
        public android.app.Notification build() {
            android.util.SparseArray<android.os.Bundle> actionExtrasMap = android.support.v4.app.NotificationCompatJellybean.buildActionExtrasMap(mActionExtrasList);
            if (actionExtrasMap != null) {
                // Add the action extras sparse array if any action was added with extras.
                mExtras.putSparseParcelableArray(android.support.v4.app.NotificationCompatJellybean.EXTRA_ACTION_EXTRAS, actionExtrasMap);
            }
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

    public static android.os.Bundle getExtras(android.app.Notification notif) {
        return notif.extras;
    }

    public static int getActionCount(android.app.Notification notif) {
        return notif.actions != null ? notif.actions.length : 0;
    }

    public static android.support.v4.app.NotificationCompatBase.Action getAction(android.app.Notification notif, int actionIndex, android.support.v4.app.NotificationCompatBase.Action.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        android.app.Notification.Action action = notif.actions[actionIndex];
        android.os.Bundle actionExtras = null;
        android.util.SparseArray<android.os.Bundle> actionExtrasMap = notif.extras.getSparseParcelableArray(android.support.v4.app.NotificationCompatJellybean.EXTRA_ACTION_EXTRAS);
        if (actionExtrasMap != null) {
            actionExtras = actionExtrasMap.get(actionIndex);
        }
        return android.support.v4.app.NotificationCompatJellybean.readAction(factory, remoteInputFactory, action.icon, action.title, action.actionIntent, actionExtras);
    }

    public static boolean getLocalOnly(android.app.Notification notif) {
        return notif.extras.getBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_LOCAL_ONLY);
    }

    public static java.lang.String getGroup(android.app.Notification notif) {
        return notif.extras.getString(android.support.v4.app.NotificationCompatJellybean.EXTRA_GROUP_KEY);
    }

    public static boolean isGroupSummary(android.app.Notification notif) {
        return notif.extras.getBoolean(android.support.v4.app.NotificationCompatJellybean.EXTRA_GROUP_SUMMARY);
    }

    public static java.lang.String getSortKey(android.app.Notification notif) {
        return notif.extras.getString(android.support.v4.app.NotificationCompatJellybean.EXTRA_SORT_KEY);
    }
}

