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


class NotificationCompatApi21 {
    public static final java.lang.String CATEGORY_CALL = android.app.Notification.CATEGORY_CALL;

    public static final java.lang.String CATEGORY_MESSAGE = android.app.Notification.CATEGORY_MESSAGE;

    public static final java.lang.String CATEGORY_EMAIL = android.app.Notification.CATEGORY_EMAIL;

    public static final java.lang.String CATEGORY_EVENT = android.app.Notification.CATEGORY_EVENT;

    public static final java.lang.String CATEGORY_PROMO = android.app.Notification.CATEGORY_PROMO;

    public static final java.lang.String CATEGORY_ALARM = android.app.Notification.CATEGORY_ALARM;

    public static final java.lang.String CATEGORY_PROGRESS = android.app.Notification.CATEGORY_PROGRESS;

    public static final java.lang.String CATEGORY_SOCIAL = android.app.Notification.CATEGORY_SOCIAL;

    public static final java.lang.String CATEGORY_ERROR = android.app.Notification.CATEGORY_ERROR;

    public static final java.lang.String CATEGORY_TRANSPORT = android.app.Notification.CATEGORY_TRANSPORT;

    public static final java.lang.String CATEGORY_SYSTEM = android.app.Notification.CATEGORY_SYSTEM;

    public static final java.lang.String CATEGORY_SERVICE = android.app.Notification.CATEGORY_SERVICE;

    public static final java.lang.String CATEGORY_RECOMMENDATION = android.app.Notification.CATEGORY_RECOMMENDATION;

    public static final java.lang.String CATEGORY_STATUS = android.app.Notification.CATEGORY_STATUS;

    private static final java.lang.String KEY_AUTHOR = "author";

    private static final java.lang.String KEY_TEXT = "text";

    private static final java.lang.String KEY_MESSAGES = "messages";

    private static final java.lang.String KEY_REMOTE_INPUT = "remote_input";

    private static final java.lang.String KEY_ON_REPLY = "on_reply";

    private static final java.lang.String KEY_ON_READ = "on_read";

    private static final java.lang.String KEY_PARTICIPANTS = "participants";

    private static final java.lang.String KEY_TIMESTAMP = "timestamp";

    public static class Builder implements android.support.v4.app.NotificationBuilderWithActions , android.support.v4.app.NotificationBuilderWithBuilderAccessor {
        private android.app.Notification.Builder b;

        private android.os.Bundle mExtras;

        private android.widget.RemoteViews mContentView;

        private android.widget.RemoteViews mBigContentView;

        private android.widget.RemoteViews mHeadsUpContentView;

        public Builder(android.content.Context context, android.app.Notification n, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, android.widget.RemoteViews tickerView, int number, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent, android.graphics.Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean showWhen, boolean useChronometer, int priority, java.lang.CharSequence subText, boolean localOnly, java.lang.String category, java.util.ArrayList<java.lang.String> people, android.os.Bundle extras, int color, int visibility, android.app.Notification publicVersion, java.lang.String groupKey, boolean groupSummary, java.lang.String sortKey, android.widget.RemoteViews contentView, android.widget.RemoteViews bigContentView, android.widget.RemoteViews headsUpContentView) {
            b = new android.app.Notification.Builder(context).setWhen(n.when).setShowWhen(showWhen).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0).setOnlyAlertOnce((n.flags & android.app.Notification.FLAG_ONLY_ALERT_ONCE) != 0).setAutoCancel((n.flags & android.app.Notification.FLAG_AUTO_CANCEL) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & android.app.Notification.FLAG_HIGH_PRIORITY) != 0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate).setLocalOnly(localOnly).setGroup(groupKey).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion);
            mExtras = new android.os.Bundle();
            if (extras != null) {
                mExtras.putAll(extras);
            }
            for (java.lang.String person : people) {
                b.addPerson(person);
            }
            mContentView = contentView;
            mBigContentView = bigContentView;
            mHeadsUpContentView = headsUpContentView;
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
            if (mHeadsUpContentView != null) {
                notification.headsUpContentView = mHeadsUpContentView;
            }
            return notification;
        }
    }

    public static java.lang.String getCategory(android.app.Notification notif) {
        return notif.category;
    }

    static android.os.Bundle getBundleForUnreadConversation(android.support.v4.app.NotificationCompatBase.UnreadConversation uc) {
        if (uc == null) {
            return null;
        }
        android.os.Bundle b = new android.os.Bundle();
        java.lang.String author = null;
        if ((uc.getParticipants() != null) && (uc.getParticipants().length > 1)) {
            author = uc.getParticipants()[0];
        }
        android.os.Parcelable[] messages = new android.os.Parcelable[uc.getMessages().length];
        for (int i = 0; i < messages.length; i++) {
            android.os.Bundle m = new android.os.Bundle();
            m.putString(android.support.v4.app.NotificationCompatApi21.KEY_TEXT, uc.getMessages()[i]);
            m.putString(android.support.v4.app.NotificationCompatApi21.KEY_AUTHOR, author);
            messages[i] = m;
        }
        b.putParcelableArray(android.support.v4.app.NotificationCompatApi21.KEY_MESSAGES, messages);
        android.support.v4.app.RemoteInputCompatBase.RemoteInput remoteInput = uc.getRemoteInput();
        if (remoteInput != null) {
            b.putParcelable(android.support.v4.app.NotificationCompatApi21.KEY_REMOTE_INPUT, android.support.v4.app.NotificationCompatApi21.fromCompatRemoteInput(remoteInput));
        }
        b.putParcelable(android.support.v4.app.NotificationCompatApi21.KEY_ON_REPLY, uc.getReplyPendingIntent());
        b.putParcelable(android.support.v4.app.NotificationCompatApi21.KEY_ON_READ, uc.getReadPendingIntent());
        b.putStringArray(android.support.v4.app.NotificationCompatApi21.KEY_PARTICIPANTS, uc.getParticipants());
        b.putLong(android.support.v4.app.NotificationCompatApi21.KEY_TIMESTAMP, uc.getLatestTimestamp());
        return b;
    }

    static android.support.v4.app.NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(android.os.Bundle b, android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        if (b == null) {
            return null;
        }
        android.os.Parcelable[] parcelableMessages = b.getParcelableArray(android.support.v4.app.NotificationCompatApi21.KEY_MESSAGES);
        java.lang.String[] messages = null;
        if (parcelableMessages != null) {
            java.lang.String[] tmp = new java.lang.String[parcelableMessages.length];
            boolean success = true;
            for (int i = 0; i < tmp.length; i++) {
                if (!(parcelableMessages[i] instanceof android.os.Bundle)) {
                    success = false;
                    break;
                }
                tmp[i] = ((android.os.Bundle) (parcelableMessages[i])).getString(android.support.v4.app.NotificationCompatApi21.KEY_TEXT);
                if (tmp[i] == null) {
                    success = false;
                    break;
                }
            }
            if (success) {
                messages = tmp;
            } else {
                return null;
            }
        }
        android.app.PendingIntent onRead = b.getParcelable(android.support.v4.app.NotificationCompatApi21.KEY_ON_READ);
        android.app.PendingIntent onReply = b.getParcelable(android.support.v4.app.NotificationCompatApi21.KEY_ON_REPLY);
        android.app.RemoteInput remoteInput = b.getParcelable(android.support.v4.app.NotificationCompatApi21.KEY_REMOTE_INPUT);
        java.lang.String[] participants = b.getStringArray(android.support.v4.app.NotificationCompatApi21.KEY_PARTICIPANTS);
        if ((participants == null) || (participants.length != 1)) {
            return null;
        }
        return factory.build(messages, remoteInput != null ? android.support.v4.app.NotificationCompatApi21.toCompatRemoteInput(remoteInput, remoteInputFactory) : null, onReply, onRead, participants, b.getLong(android.support.v4.app.NotificationCompatApi21.KEY_TIMESTAMP));
    }

    private static android.app.RemoteInput fromCompatRemoteInput(android.support.v4.app.RemoteInputCompatBase.RemoteInput src) {
        return new android.app.RemoteInput.Builder(src.getResultKey()).setLabel(src.getLabel()).setChoices(src.getChoices()).setAllowFreeFormInput(src.getAllowFreeFormInput()).addExtras(src.getExtras()).build();
    }

    private static android.support.v4.app.RemoteInputCompatBase.RemoteInput toCompatRemoteInput(android.app.RemoteInput remoteInput, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory factory) {
        return factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras());
    }
}

