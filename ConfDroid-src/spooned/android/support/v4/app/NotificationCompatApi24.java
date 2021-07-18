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


class NotificationCompatApi24 {
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

    public static class Builder implements android.support.v4.app.NotificationBuilderWithActions , android.support.v4.app.NotificationBuilderWithBuilderAccessor {
        private android.app.Notification.Builder b;

        public Builder(android.content.Context context, android.app.Notification n, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, android.widget.RemoteViews tickerView, int number, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent, android.graphics.Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean showWhen, boolean useChronometer, int priority, java.lang.CharSequence subText, boolean localOnly, java.lang.String category, java.util.ArrayList<java.lang.String> people, android.os.Bundle extras, int color, int visibility, android.app.Notification publicVersion, java.lang.String groupKey, boolean groupSummary, java.lang.String sortKey, java.lang.CharSequence[] remoteInputHistory, android.widget.RemoteViews contentView, android.widget.RemoteViews bigContentView, android.widget.RemoteViews headsUpContentView) {
            b = new android.app.Notification.Builder(context).setWhen(n.when).setShowWhen(showWhen).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0).setOnlyAlertOnce((n.flags & android.app.Notification.FLAG_ONLY_ALERT_ONCE) != 0).setAutoCancel((n.flags & android.app.Notification.FLAG_AUTO_CANCEL) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & android.app.Notification.FLAG_HIGH_PRIORITY) != 0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate).setLocalOnly(localOnly).setExtras(extras).setGroup(groupKey).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion).setRemoteInputHistory(remoteInputHistory);
            if (contentView != null) {
                b.setCustomContentView(contentView);
            }
            if (bigContentView != null) {
                b.setCustomBigContentView(bigContentView);
            }
            if (headsUpContentView != null) {
                b.setCustomHeadsUpContentView(headsUpContentView);
            }
            for (java.lang.String person : people) {
                b.addPerson(person);
            }
        }

        @java.lang.Override
        public void addAction(android.support.v4.app.NotificationCompatBase.Action action) {
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
            actionBuilder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            b.addAction(actionBuilder.build());
        }

        @java.lang.Override
        public android.app.Notification.Builder getBuilder() {
            return b;
        }

        @java.lang.Override
        public android.app.Notification build() {
            return b.build();
        }
    }

    public static void addMessagingStyle(android.support.v4.app.NotificationBuilderWithBuilderAccessor b, java.lang.CharSequence userDisplayName, java.lang.CharSequence conversationTitle, java.util.List<java.lang.CharSequence> texts, java.util.List<java.lang.Long> timestamps, java.util.List<java.lang.CharSequence> senders, java.util.List<java.lang.String> dataMimeTypes, java.util.List<android.net.Uri> dataUris) {
        android.app.Notification.MessagingStyle style = new android.app.Notification.MessagingStyle(userDisplayName).setConversationTitle(conversationTitle);
        for (int i = 0; i < texts.size(); i++) {
            android.app.Notification.MessagingStyle.Message message = new android.app.Notification.MessagingStyle.Message(texts.get(i), timestamps.get(i), senders.get(i));
            if (dataMimeTypes.get(i) != null) {
                message.setData(dataMimeTypes.get(i), dataUris.get(i));
            }
            style.addMessage(message);
        }
        style.setBuilder(b.getBuilder());
    }
}

