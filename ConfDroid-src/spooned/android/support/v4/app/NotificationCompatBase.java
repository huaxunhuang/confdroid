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


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class NotificationCompatBase {
    public static abstract class Action {
        public abstract int getIcon();

        public abstract java.lang.CharSequence getTitle();

        public abstract android.app.PendingIntent getActionIntent();

        public abstract android.os.Bundle getExtras();

        public abstract android.support.v4.app.RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

        public abstract boolean getAllowGeneratedReplies();

        public interface Factory {
            android.support.v4.app.NotificationCompatBase.Action build(int icon, java.lang.CharSequence title, android.app.PendingIntent actionIntent, android.os.Bundle extras, android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs, boolean allowGeneratedReplies);

            public android.support.v4.app.NotificationCompatBase.Action[] newArray(int length);
        }
    }

    public static abstract class UnreadConversation {
        abstract java.lang.String[] getParticipants();

        abstract java.lang.String getParticipant();

        abstract java.lang.String[] getMessages();

        abstract android.support.v4.app.RemoteInputCompatBase.RemoteInput getRemoteInput();

        abstract android.app.PendingIntent getReplyPendingIntent();

        abstract android.app.PendingIntent getReadPendingIntent();

        abstract long getLatestTimestamp();

        public interface Factory {
            android.support.v4.app.NotificationCompatBase.UnreadConversation build(java.lang.String[] messages, android.support.v4.app.RemoteInputCompatBase.RemoteInput remoteInput, android.app.PendingIntent replyPendingIntent, android.app.PendingIntent readPendingIntent, java.lang.String[] participants, long latestTimestamp);
        }
    }

    public static android.app.Notification add(android.app.Notification notification, android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, android.app.PendingIntent contentIntent, android.app.PendingIntent fullScreenIntent) {
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.fullScreenIntent = fullScreenIntent;
        return notification;
    }
}

