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


class NotificationCompatIceCreamSandwich {
    public static class Builder implements android.support.v4.app.NotificationBuilderWithBuilderAccessor {
        private android.app.Notification.Builder b;

        public Builder(android.content.Context context, android.app.Notification n, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, android.widget.RemoteViews tickerView, int number, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent, android.graphics.Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate) {
            b = new android.app.Notification.Builder(context).setWhen(n.when).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & android.app.Notification.FLAG_ONGOING_EVENT) != 0).setOnlyAlertOnce((n.flags & android.app.Notification.FLAG_ONLY_ALERT_ONCE) != 0).setAutoCancel((n.flags & android.app.Notification.FLAG_AUTO_CANCEL) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & android.app.Notification.FLAG_HIGH_PRIORITY) != 0).setLargeIcon(largeIcon).setNumber(number).setProgress(progressMax, progress, progressIndeterminate);
        }

        @java.lang.Override
        public android.app.Notification.Builder getBuilder() {
            return b;
        }

        @java.lang.Override
        public android.app.Notification build() {
            return b.getNotification();
        }
    }
}

