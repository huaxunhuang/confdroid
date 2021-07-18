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


/**
 * Helper for accessing features in {@link android.app.Notification}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public class NotificationCompat {
    /**
     * Use all default values (where applicable).
     */
    public static final int DEFAULT_ALL = ~0;

    /**
     * Use the default notification sound. This will ignore any sound set using
     * {@link Builder#setSound}
     *
     * <p>
     * A notification that is noisy is more likely to be presented as a heads-up notification,
     * on some platforms.
     * </p>
     *
     * @see Builder#setDefaults
     */
    public static final int DEFAULT_SOUND = 1;

    /**
     * Use the default notification vibrate. This will ignore any vibrate set using
     * {@link Builder#setVibrate}. Using phone vibration requires the
     * {@link android.Manifest.permission#VIBRATE VIBRATE} permission.
     *
     * <p>
     * A notification that vibrates is more likely to be presented as a heads-up notification,
     * on some platforms.
     * </p>
     *
     * @see Builder#setDefaults
     */
    public static final int DEFAULT_VIBRATE = 2;

    /**
     * Use the default notification lights. This will ignore the
     * {@link #FLAG_SHOW_LIGHTS} bit, and values set with {@link Builder#setLights}.
     *
     * @see Builder#setDefaults
     */
    public static final int DEFAULT_LIGHTS = 4;

    /**
     * Use this constant as the value for audioStreamType to request that
     * the default stream type for notifications be used.  Currently the
     * default stream type is {@link AudioManager#STREAM_NOTIFICATION}.
     */
    public static final int STREAM_DEFAULT = -1;

    /**
     * Bit set in the Notification flags field when LEDs should be turned on
     * for this notification.
     */
    public static final int FLAG_SHOW_LIGHTS = 0x1;

    /**
     * Bit set in the Notification flags field if this notification is in
     * reference to something that is ongoing, like a phone call.  It should
     * not be set if this notification is in reference to something that
     * happened at a particular point in time, like a missed phone call.
     */
    public static final int FLAG_ONGOING_EVENT = 0x2;

    /**
     * Bit set in the Notification flags field if
     * the audio will be repeated until the notification is
     * cancelled or the notification window is opened.
     */
    public static final int FLAG_INSISTENT = 0x4;

    /**
     * Bit set in the Notification flags field if the notification's sound,
     * vibrate and ticker should only be played if the notification is not already showing.
     */
    public static final int FLAG_ONLY_ALERT_ONCE = 0x8;

    /**
     * Bit set in the Notification flags field if the notification should be canceled when
     * it is clicked by the user.
     */
    public static final int FLAG_AUTO_CANCEL = 0x10;

    /**
     * Bit set in the Notification flags field if the notification should not be canceled
     * when the user clicks the Clear all button.
     */
    public static final int FLAG_NO_CLEAR = 0x20;

    /**
     * Bit set in the Notification flags field if this notification represents a currently
     * running service.  This will normally be set for you by
     * {@link android.app.Service#startForeground}.
     */
    public static final int FLAG_FOREGROUND_SERVICE = 0x40;

    /**
     * Obsolete flag indicating high-priority notifications; use the priority field instead.
     *
     * @deprecated Use {@link NotificationCompat.Builder#setPriority(int)} with a positive value.
     */
    @java.lang.Deprecated
    public static final int FLAG_HIGH_PRIORITY = 0x80;

    /**
     * Bit set in the Notification flags field if this notification is relevant to the current
     * device only and it is not recommended that it bridge to other devices.
     */
    public static final int FLAG_LOCAL_ONLY = 0x100;

    /**
     * Bit set in the Notification flags field if this notification is the group summary for a
     * group of notifications. Grouped notifications may display in a cluster or stack on devices
     * which support such rendering. Requires a group key also be set using
     * {@link Builder#setGroup}.
     */
    public static final int FLAG_GROUP_SUMMARY = 0x200;

    /**
     * Default notification priority for {@link NotificationCompat.Builder#setPriority(int)}.
     * If your application does not prioritize its own notifications,
     * use this value for all notifications.
     */
    public static final int PRIORITY_DEFAULT = 0;

    /**
     * Lower notification priority for {@link NotificationCompat.Builder#setPriority(int)},
     * for items that are less important. The UI may choose to show
     * these items smaller, or at a different position in the list,
     * compared with your app's {@link #PRIORITY_DEFAULT} items.
     */
    public static final int PRIORITY_LOW = -1;

    /**
     * Lowest notification priority for {@link NotificationCompat.Builder#setPriority(int)};
     * these items might not be shown to the user except under
     * special circumstances, such as detailed notification logs.
     */
    public static final int PRIORITY_MIN = -2;

    /**
     * Higher notification priority for {@link NotificationCompat.Builder#setPriority(int)},
     * for more important notifications or alerts. The UI may choose
     * to show these items larger, or at a different position in
     * notification lists, compared with your app's {@link #PRIORITY_DEFAULT} items.
     */
    public static final int PRIORITY_HIGH = 1;

    /**
     * Highest notification priority for {@link NotificationCompat.Builder#setPriority(int)},
     * for your application's most important items that require the user's
     * prompt attention or input.
     */
    public static final int PRIORITY_MAX = 2;

    /**
     * Notification extras key: this is the title of the notification,
     * as supplied to {@link Builder#setContentTitle(CharSequence)}.
     */
    public static final java.lang.String EXTRA_TITLE = "android.title";

    /**
     * Notification extras key: this is the title of the notification when shown in expanded form,
     * e.g. as supplied to {@link BigTextStyle#setBigContentTitle(CharSequence)}.
     */
    public static final java.lang.String EXTRA_TITLE_BIG = android.support.v4.app.NotificationCompat.EXTRA_TITLE + ".big";

    /**
     * Notification extras key: this is the main text payload, as supplied to
     * {@link Builder#setContentText(CharSequence)}.
     */
    public static final java.lang.String EXTRA_TEXT = "android.text";

    /**
     * Notification extras key: this is a third line of text, as supplied to
     * {@link Builder#setSubText(CharSequence)}.
     */
    public static final java.lang.String EXTRA_SUB_TEXT = "android.subText";

    /**
     * Notification extras key: this is the remote input history, as supplied to
     * {@link Builder#setRemoteInputHistory(CharSequence[])}.
     *
     * Apps can fill this through {@link Builder#setRemoteInputHistory(CharSequence[])}
     * with the most recent inputs that have been sent through a {@link RemoteInput} of this
     * Notification and are expected to clear it once the it is no longer relevant (e.g. for chat
     * notifications once the other party has responded).
     *
     * The extra with this key is of type CharSequence[] and contains the most recent entry at
     * the 0 index, the second most recent at the 1 index, etc.
     *
     * @see Builder#setRemoteInputHistory(CharSequence[])
     */
    public static final java.lang.String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";

    /**
     * Notification extras key: this is a small piece of additional text as supplied to
     * {@link Builder#setContentInfo(CharSequence)}.
     */
    public static final java.lang.String EXTRA_INFO_TEXT = "android.infoText";

    /**
     * Notification extras key: this is a line of summary information intended to be shown
     * alongside expanded notifications, as supplied to (e.g.)
     * {@link BigTextStyle#setSummaryText(CharSequence)}.
     */
    public static final java.lang.String EXTRA_SUMMARY_TEXT = "android.summaryText";

    /**
     * Notification extras key: this is the longer text shown in the big form of a
     * {@link BigTextStyle} notification, as supplied to
     * {@link BigTextStyle#bigText(CharSequence)}.
     */
    public static final java.lang.String EXTRA_BIG_TEXT = "android.bigText";

    /**
     * Notification extras key: this is the resource ID of the notification's main small icon, as
     * supplied to {@link Builder#setSmallIcon(int)}.
     */
    public static final java.lang.String EXTRA_SMALL_ICON = "android.icon";

    /**
     * Notification extras key: this is a bitmap to be used instead of the small icon when showing the
     * notification payload, as
     * supplied to {@link Builder#setLargeIcon(android.graphics.Bitmap)}.
     */
    public static final java.lang.String EXTRA_LARGE_ICON = "android.largeIcon";

    /**
     * Notification extras key: this is a bitmap to be used instead of the one from
     * {@link Builder#setLargeIcon(android.graphics.Bitmap)} when the notification is
     * shown in its expanded form, as supplied to
     * {@link BigPictureStyle#bigLargeIcon(android.graphics.Bitmap)}.
     */
    public static final java.lang.String EXTRA_LARGE_ICON_BIG = android.support.v4.app.NotificationCompat.EXTRA_LARGE_ICON + ".big";

    /**
     * Notification extras key: this is the progress value supplied to
     * {@link Builder#setProgress(int, int, boolean)}.
     */
    public static final java.lang.String EXTRA_PROGRESS = "android.progress";

    /**
     * Notification extras key: this is the maximum value supplied to
     * {@link Builder#setProgress(int, int, boolean)}.
     */
    public static final java.lang.String EXTRA_PROGRESS_MAX = "android.progressMax";

    /**
     * Notification extras key: whether the progress bar is indeterminate, supplied to
     * {@link Builder#setProgress(int, int, boolean)}.
     */
    public static final java.lang.String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";

    /**
     * Notification extras key: whether the when field set using {@link Builder#setWhen} should
     * be shown as a count-up timer (specifically a {@link android.widget.Chronometer}) instead
     * of a timestamp, as supplied to {@link Builder#setUsesChronometer(boolean)}.
     */
    public static final java.lang.String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";

    /**
     * Notification extras key: whether the when field set using {@link Builder#setWhen} should
     * be shown, as supplied to {@link Builder#setShowWhen(boolean)}.
     */
    public static final java.lang.String EXTRA_SHOW_WHEN = "android.showWhen";

    /**
     * Notification extras key: this is a bitmap to be shown in {@link BigPictureStyle} expanded
     * notifications, supplied to {@link BigPictureStyle#bigPicture(android.graphics.Bitmap)}.
     */
    public static final java.lang.String EXTRA_PICTURE = "android.picture";

    /**
     * Notification extras key: An array of CharSequences to show in {@link InboxStyle} expanded
     * notifications, each of which was supplied to {@link InboxStyle#addLine(CharSequence)}.
     */
    public static final java.lang.String EXTRA_TEXT_LINES = "android.textLines";

    /**
     * Notification extras key: A string representing the name of the specific
     * {@link android.app.Notification.Style} used to create this notification.
     */
    public static final java.lang.String EXTRA_TEMPLATE = "android.template";

    /**
     * Notification extras key: A String array containing the people that this
     * notification relates to, each of which was supplied to
     * {@link Builder#addPerson(String)}.
     */
    public static final java.lang.String EXTRA_PEOPLE = "android.people";

    /**
     * Notification extras key: A
     * {@link android.content.ContentUris content URI} pointing to an image that can be displayed
     * in the background when the notification is selected. The URI must point to an image stream
     * suitable for passing into
     * {@link android.graphics.BitmapFactory#decodeStream(java.io.InputStream)
     * BitmapFactory.decodeStream}; all other content types will be ignored. The content provider
     * URI used for this purpose must require no permissions to read the image data.
     */
    public static final java.lang.String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";

    /**
     * Notification key: A
     * {@link android.media.session.MediaSession.Token} associated with a
     * {@link android.app.Notification.MediaStyle} notification.
     */
    public static final java.lang.String EXTRA_MEDIA_SESSION = "android.mediaSession";

    /**
     * Notification extras key: the indices of actions to be shown in the compact view,
     * as supplied to (e.g.) {@link Notification.MediaStyle#setShowActionsInCompactView(int...)}.
     */
    public static final java.lang.String EXTRA_COMPACT_ACTIONS = "android.compactActions";

    /**
     * Notification key: the username to be displayed for all messages sent by the user
     * including
     * direct replies
     * {@link MessagingStyle} notification.
     */
    public static final java.lang.String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";

    /**
     * Notification key: a {@link String} to be displayed as the title to a conversation
     * represented by a {@link MessagingStyle}
     */
    public static final java.lang.String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";

    /**
     * Notification key: an array of {@link Bundle} objects representing
     * {@link MessagingStyle.Message} objects for a {@link MessagingStyle} notification.
     */
    public static final java.lang.String EXTRA_MESSAGES = "android.messages";

    /**
     * Value of {@link Notification#color} equal to 0 (also known as
     * {@link android.graphics.Color#TRANSPARENT Color.TRANSPARENT}),
     * telling the system not to decorate this notification with any special color but instead use
     * default colors when presenting this notification.
     */
    @android.support.annotation.ColorInt
    public static final int COLOR_DEFAULT = android.graphics.Color.TRANSPARENT;

    /**
     * Notification visibility: Show this notification in its entirety on all lockscreens.
     *
     * {@see android.app.Notification#visibility}
     */
    public static final int VISIBILITY_PUBLIC = 1;

    /**
     * Notification visibility: Show this notification on all lockscreens, but conceal sensitive or
     * private information on secure lockscreens.
     *
     * {@see android.app.Notification#visibility}
     */
    public static final int VISIBILITY_PRIVATE = 0;

    /**
     * Notification visibility: Do not reveal any part of this notification on a secure lockscreen.
     *
     * {@see android.app.Notification#visibility}
     */
    public static final int VISIBILITY_SECRET = -1;

    /**
     * Notification category: incoming call (voice or video) or similar synchronous communication request.
     */
    public static final java.lang.String CATEGORY_CALL = android.support.v4.app.NotificationCompatApi21.CATEGORY_CALL;

    /**
     * Notification category: incoming direct message (SMS, instant message, etc.).
     */
    public static final java.lang.String CATEGORY_MESSAGE = android.support.v4.app.NotificationCompatApi21.CATEGORY_MESSAGE;

    /**
     * Notification category: asynchronous bulk message (email).
     */
    public static final java.lang.String CATEGORY_EMAIL = android.support.v4.app.NotificationCompatApi21.CATEGORY_EMAIL;

    /**
     * Notification category: calendar event.
     */
    public static final java.lang.String CATEGORY_EVENT = android.support.v4.app.NotificationCompatApi21.CATEGORY_EVENT;

    /**
     * Notification category: promotion or advertisement.
     */
    public static final java.lang.String CATEGORY_PROMO = android.support.v4.app.NotificationCompatApi21.CATEGORY_PROMO;

    /**
     * Notification category: alarm or timer.
     */
    public static final java.lang.String CATEGORY_ALARM = android.support.v4.app.NotificationCompatApi21.CATEGORY_ALARM;

    /**
     * Notification category: progress of a long-running background operation.
     */
    public static final java.lang.String CATEGORY_PROGRESS = android.support.v4.app.NotificationCompatApi21.CATEGORY_PROGRESS;

    /**
     * Notification category: social network or sharing update.
     */
    public static final java.lang.String CATEGORY_SOCIAL = android.support.v4.app.NotificationCompatApi21.CATEGORY_SOCIAL;

    /**
     * Notification category: error in background operation or authentication status.
     */
    public static final java.lang.String CATEGORY_ERROR = android.support.v4.app.NotificationCompatApi21.CATEGORY_ERROR;

    /**
     * Notification category: media transport control for playback.
     */
    public static final java.lang.String CATEGORY_TRANSPORT = android.support.v4.app.NotificationCompatApi21.CATEGORY_TRANSPORT;

    /**
     * Notification category: system or device status update.  Reserved for system use.
     */
    public static final java.lang.String CATEGORY_SYSTEM = android.support.v4.app.NotificationCompatApi21.CATEGORY_SYSTEM;

    /**
     * Notification category: indication of running background service.
     */
    public static final java.lang.String CATEGORY_SERVICE = android.support.v4.app.NotificationCompatApi21.CATEGORY_SERVICE;

    /**
     * Notification category: user-scheduled reminder.
     */
    public static final java.lang.String CATEGORY_REMINDER = android.support.v4.app.NotificationCompatApi23.CATEGORY_REMINDER;

    /**
     * Notification category: a specific, timely recommendation for a single thing.
     * For example, a news app might want to recommend a news story it believes the user will
     * want to read next.
     */
    public static final java.lang.String CATEGORY_RECOMMENDATION = android.support.v4.app.NotificationCompatApi21.CATEGORY_RECOMMENDATION;

    /**
     * Notification category: ongoing information about device or contextual status.
     */
    public static final java.lang.String CATEGORY_STATUS = android.support.v4.app.NotificationCompatApi21.CATEGORY_STATUS;

    static final android.support.v4.app.NotificationCompat.NotificationCompatImpl IMPL;

    interface NotificationCompatImpl {
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender);

        public android.os.Bundle getExtras(android.app.Notification n);

        public int getActionCount(android.app.Notification n);

        public android.support.v4.app.NotificationCompat.Action getAction(android.app.Notification n, int actionIndex);

        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(java.util.ArrayList<android.os.Parcelable> parcelables);

        public java.util.ArrayList<android.os.Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions);

        public java.lang.String getCategory(android.app.Notification n);

        public boolean getLocalOnly(android.app.Notification n);

        public java.lang.String getGroup(android.app.Notification n);

        public boolean isGroupSummary(android.app.Notification n);

        public java.lang.String getSortKey(android.app.Notification n);

        android.os.Bundle getBundleForUnreadConversation(android.support.v4.app.NotificationCompatBase.UnreadConversation uc);

        android.support.v4.app.NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(android.os.Bundle b, android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory);
    }

    /**
     * Interface for appcompat to extend v4 builder with media style.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected static class BuilderExtender {
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationBuilderWithBuilderAccessor builder) {
            android.app.Notification n = builder.build();
            if (b.mContentView != null) {
                n.contentView = b.mContentView;
            }
            return n;
        }
    }

    static class NotificationCompatImplBase implements android.support.v4.app.NotificationCompat.NotificationCompatImpl {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.app.Notification result = b.mNotification;
            result = android.support.v4.app.NotificationCompatBase.add(result, b.mContext, b.resolveTitle(), b.resolveText(), b.mContentIntent, b.mFullScreenIntent);
            // translate high priority requests into legacy flag
            if (b.mPriority > android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT) {
                result.flags |= android.support.v4.app.NotificationCompat.FLAG_HIGH_PRIORITY;
            }
            if (b.mContentView != null) {
                result.contentView = b.mContentView;
            }
            return result;
        }

        @java.lang.Override
        public android.os.Bundle getExtras(android.app.Notification n) {
            return null;
        }

        @java.lang.Override
        public int getActionCount(android.app.Notification n) {
            return 0;
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action getAction(android.app.Notification n, int actionIndex) {
            return null;
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(java.util.ArrayList<android.os.Parcelable> parcelables) {
            return null;
        }

        @java.lang.Override
        public java.util.ArrayList<android.os.Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions) {
            return null;
        }

        @java.lang.Override
        public java.lang.String getCategory(android.app.Notification n) {
            return null;
        }

        @java.lang.Override
        public boolean getLocalOnly(android.app.Notification n) {
            return false;
        }

        @java.lang.Override
        public java.lang.String getGroup(android.app.Notification n) {
            return null;
        }

        @java.lang.Override
        public boolean isGroupSummary(android.app.Notification n) {
            return false;
        }

        @java.lang.Override
        public java.lang.String getSortKey(android.app.Notification n) {
            return null;
        }

        @java.lang.Override
        public android.os.Bundle getBundleForUnreadConversation(android.support.v4.app.NotificationCompatBase.UnreadConversation uc) {
            return null;
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(android.os.Bundle b, android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
            return null;
        }
    }

    static class NotificationCompatImplHoneycomb extends android.support.v4.app.NotificationCompat.NotificationCompatImplBase {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.app.Notification notification = android.support.v4.app.NotificationCompatHoneycomb.add(b.mContext, b.mNotification, b.resolveTitle(), b.resolveText(), b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon);
            if (b.mContentView != null) {
                notification.contentView = b.mContentView;
            }
            return notification;
        }
    }

    static class NotificationCompatImplIceCreamSandwich extends android.support.v4.app.NotificationCompat.NotificationCompatImplBase {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.support.v4.app.NotificationCompatIceCreamSandwich.Builder builder = new android.support.v4.app.NotificationCompatIceCreamSandwich.Builder(b.mContext, b.mNotification, b.resolveTitle(), b.resolveText(), b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate);
            return extender.build(b, builder);
        }
    }

    static class NotificationCompatImplJellybean extends android.support.v4.app.NotificationCompat.NotificationCompatImplBase {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.support.v4.app.NotificationCompatJellybean.Builder builder = new android.support.v4.app.NotificationCompatJellybean.Builder(b.mContext, b.mNotification, b.resolveTitle(), b.resolveText(), b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey, b.mContentView, b.mBigContentView);
            android.support.v4.app.NotificationCompat.addActionsToBuilder(builder, b.mActions);
            android.support.v4.app.NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            android.app.Notification notification = extender.build(b, builder);
            if (b.mStyle != null) {
                android.os.Bundle extras = getExtras(notification);
                if (extras != null) {
                    b.mStyle.addCompatExtras(extras);
                }
            }
            return notification;
        }

        @java.lang.Override
        public android.os.Bundle getExtras(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatJellybean.getExtras(n);
        }

        @java.lang.Override
        public int getActionCount(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatJellybean.getActionCount(n);
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action getAction(android.app.Notification n, int actionIndex) {
            return ((android.support.v4.app.NotificationCompat.Action) (android.support.v4.app.NotificationCompatJellybean.getAction(n, actionIndex, android.support.v4.app.NotificationCompat.Action.FACTORY, android.support.v4.app.RemoteInput.FACTORY)));
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(java.util.ArrayList<android.os.Parcelable> parcelables) {
            return ((android.support.v4.app.NotificationCompat.Action[]) (android.support.v4.app.NotificationCompatJellybean.getActionsFromParcelableArrayList(parcelables, android.support.v4.app.NotificationCompat.Action.FACTORY, android.support.v4.app.RemoteInput.FACTORY)));
        }

        @java.lang.Override
        public java.util.ArrayList<android.os.Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions) {
            return android.support.v4.app.NotificationCompatJellybean.getParcelableArrayListForActions(actions);
        }

        @java.lang.Override
        public boolean getLocalOnly(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatJellybean.getLocalOnly(n);
        }

        @java.lang.Override
        public java.lang.String getGroup(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatJellybean.getGroup(n);
        }

        @java.lang.Override
        public boolean isGroupSummary(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatJellybean.isGroupSummary(n);
        }

        @java.lang.Override
        public java.lang.String getSortKey(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatJellybean.getSortKey(n);
        }
    }

    static class NotificationCompatImplKitKat extends android.support.v4.app.NotificationCompat.NotificationCompatImplJellybean {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.support.v4.app.NotificationCompatKitKat.Builder builder = new android.support.v4.app.NotificationCompatKitKat.Builder(b.mContext, b.mNotification, b.resolveTitle(), b.resolveText(), b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mShowWhen, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mPeople, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey, b.mContentView, b.mBigContentView);
            android.support.v4.app.NotificationCompat.addActionsToBuilder(builder, b.mActions);
            android.support.v4.app.NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            return extender.build(b, builder);
        }

        @java.lang.Override
        public android.os.Bundle getExtras(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatKitKat.getExtras(n);
        }

        @java.lang.Override
        public int getActionCount(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatKitKat.getActionCount(n);
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action getAction(android.app.Notification n, int actionIndex) {
            return ((android.support.v4.app.NotificationCompat.Action) (android.support.v4.app.NotificationCompatKitKat.getAction(n, actionIndex, android.support.v4.app.NotificationCompat.Action.FACTORY, android.support.v4.app.RemoteInput.FACTORY)));
        }

        @java.lang.Override
        public boolean getLocalOnly(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatKitKat.getLocalOnly(n);
        }

        @java.lang.Override
        public java.lang.String getGroup(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatKitKat.getGroup(n);
        }

        @java.lang.Override
        public boolean isGroupSummary(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatKitKat.isGroupSummary(n);
        }

        @java.lang.Override
        public java.lang.String getSortKey(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatKitKat.getSortKey(n);
        }
    }

    static class NotificationCompatImplApi20 extends android.support.v4.app.NotificationCompat.NotificationCompatImplKitKat {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.support.v4.app.NotificationCompatApi20.Builder builder = new android.support.v4.app.NotificationCompatApi20.Builder(b.mContext, b.mNotification, b.resolveTitle(), b.resolveText(), b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mShowWhen, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mPeople, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey, b.mContentView, b.mBigContentView);
            android.support.v4.app.NotificationCompat.addActionsToBuilder(builder, b.mActions);
            android.support.v4.app.NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            android.app.Notification notification = extender.build(b, builder);
            if (b.mStyle != null) {
                b.mStyle.addCompatExtras(getExtras(notification));
            }
            return notification;
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action getAction(android.app.Notification n, int actionIndex) {
            return ((android.support.v4.app.NotificationCompat.Action) (android.support.v4.app.NotificationCompatApi20.getAction(n, actionIndex, android.support.v4.app.NotificationCompat.Action.FACTORY, android.support.v4.app.RemoteInput.FACTORY)));
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(java.util.ArrayList<android.os.Parcelable> parcelables) {
            return ((android.support.v4.app.NotificationCompat.Action[]) (android.support.v4.app.NotificationCompatApi20.getActionsFromParcelableArrayList(parcelables, android.support.v4.app.NotificationCompat.Action.FACTORY, android.support.v4.app.RemoteInput.FACTORY)));
        }

        @java.lang.Override
        public java.util.ArrayList<android.os.Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions) {
            return android.support.v4.app.NotificationCompatApi20.getParcelableArrayListForActions(actions);
        }

        @java.lang.Override
        public boolean getLocalOnly(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatApi20.getLocalOnly(n);
        }

        @java.lang.Override
        public java.lang.String getGroup(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatApi20.getGroup(n);
        }

        @java.lang.Override
        public boolean isGroupSummary(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatApi20.isGroupSummary(n);
        }

        @java.lang.Override
        public java.lang.String getSortKey(android.app.Notification n) {
            return android.support.v4.app.NotificationCompatApi20.getSortKey(n);
        }
    }

    static class NotificationCompatImplApi21 extends android.support.v4.app.NotificationCompat.NotificationCompatImplApi20 {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.support.v4.app.NotificationCompatApi21.Builder builder = new android.support.v4.app.NotificationCompatApi21.Builder(b.mContext, b.mNotification, b.resolveTitle(), b.resolveText(), b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mShowWhen, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mCategory, b.mPeople, b.mExtras, b.mColor, b.mVisibility, b.mPublicVersion, b.mGroupKey, b.mGroupSummary, b.mSortKey, b.mContentView, b.mBigContentView, b.mHeadsUpContentView);
            android.support.v4.app.NotificationCompat.addActionsToBuilder(builder, b.mActions);
            android.support.v4.app.NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            android.app.Notification notification = extender.build(b, builder);
            if (b.mStyle != null) {
                b.mStyle.addCompatExtras(getExtras(notification));
            }
            return notification;
        }

        @java.lang.Override
        public java.lang.String getCategory(android.app.Notification notif) {
            return android.support.v4.app.NotificationCompatApi21.getCategory(notif);
        }

        @java.lang.Override
        public android.os.Bundle getBundleForUnreadConversation(android.support.v4.app.NotificationCompatBase.UnreadConversation uc) {
            return android.support.v4.app.NotificationCompatApi21.getBundleForUnreadConversation(uc);
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(android.os.Bundle b, android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory factory, android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
            return android.support.v4.app.NotificationCompatApi21.getUnreadConversationFromBundle(b, factory, remoteInputFactory);
        }
    }

    static class NotificationCompatImplApi24 extends android.support.v4.app.NotificationCompat.NotificationCompatImplApi21 {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.BuilderExtender extender) {
            android.support.v4.app.NotificationCompatApi24.Builder builder = new android.support.v4.app.NotificationCompatApi24.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mShowWhen, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mCategory, b.mPeople, b.mExtras, b.mColor, b.mVisibility, b.mPublicVersion, b.mGroupKey, b.mGroupSummary, b.mSortKey, b.mRemoteInputHistory, b.mContentView, b.mBigContentView, b.mHeadsUpContentView);
            android.support.v4.app.NotificationCompat.addActionsToBuilder(builder, b.mActions);
            android.support.v4.app.NotificationCompat.addStyleToBuilderApi24(builder, b.mStyle);
            android.app.Notification notification = extender.build(b, builder);
            if (b.mStyle != null) {
                b.mStyle.addCompatExtras(getExtras(notification));
            }
            return notification;
        }
    }

    static void addActionsToBuilder(android.support.v4.app.NotificationBuilderWithActions builder, java.util.ArrayList<android.support.v4.app.NotificationCompat.Action> actions) {
        for (android.support.v4.app.NotificationCompat.Action action : actions) {
            builder.addAction(action);
        }
    }

    static void addStyleToBuilderJellybean(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Style style) {
        if (style != null) {
            if (style instanceof android.support.v4.app.NotificationCompat.BigTextStyle) {
                android.support.v4.app.NotificationCompat.BigTextStyle bigTextStyle = ((android.support.v4.app.NotificationCompat.BigTextStyle) (style));
                android.support.v4.app.NotificationCompatJellybean.addBigTextStyle(builder, bigTextStyle.mBigContentTitle, bigTextStyle.mSummaryTextSet, bigTextStyle.mSummaryText, bigTextStyle.mBigText);
            } else
                if (style instanceof android.support.v4.app.NotificationCompat.InboxStyle) {
                    android.support.v4.app.NotificationCompat.InboxStyle inboxStyle = ((android.support.v4.app.NotificationCompat.InboxStyle) (style));
                    android.support.v4.app.NotificationCompatJellybean.addInboxStyle(builder, inboxStyle.mBigContentTitle, inboxStyle.mSummaryTextSet, inboxStyle.mSummaryText, inboxStyle.mTexts);
                } else
                    if (style instanceof android.support.v4.app.NotificationCompat.BigPictureStyle) {
                        android.support.v4.app.NotificationCompat.BigPictureStyle bigPictureStyle = ((android.support.v4.app.NotificationCompat.BigPictureStyle) (style));
                        android.support.v4.app.NotificationCompatJellybean.addBigPictureStyle(builder, bigPictureStyle.mBigContentTitle, bigPictureStyle.mSummaryTextSet, bigPictureStyle.mSummaryText, bigPictureStyle.mPicture, bigPictureStyle.mBigLargeIcon, bigPictureStyle.mBigLargeIconSet);
                    }


        }
    }

    static void addStyleToBuilderApi24(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Style style) {
        if (style != null) {
            if (style instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
                android.support.v4.app.NotificationCompat.MessagingStyle messagingStyle = ((android.support.v4.app.NotificationCompat.MessagingStyle) (style));
                java.util.List<java.lang.CharSequence> texts = new java.util.ArrayList<>();
                java.util.List<java.lang.Long> timestamps = new java.util.ArrayList<>();
                java.util.List<java.lang.CharSequence> senders = new java.util.ArrayList<>();
                java.util.List<java.lang.String> dataMimeTypes = new java.util.ArrayList<>();
                java.util.List<android.net.Uri> dataUris = new java.util.ArrayList<>();
                for (android.support.v4.app.NotificationCompat.MessagingStyle.Message message : messagingStyle.mMessages) {
                    texts.add(message.getText());
                    timestamps.add(message.getTimestamp());
                    senders.add(message.getSender());
                    dataMimeTypes.add(message.getDataMimeType());
                    dataUris.add(message.getDataUri());
                }
                android.support.v4.app.NotificationCompatApi24.addMessagingStyle(builder, messagingStyle.mUserDisplayName, messagingStyle.mConversationTitle, texts, timestamps, senders, dataMimeTypes, dataUris);
            } else {
                android.support.v4.app.NotificationCompat.addStyleToBuilderJellybean(builder, style);
            }
        }
    }

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastN()) {
            IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplApi24();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplApi21();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 20) {
                    IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplApi20();
                } else
                    if (android.os.Build.VERSION.SDK_INT >= 19) {
                        IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplKitKat();
                    } else
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplJellybean();
                        } else
                            if (android.os.Build.VERSION.SDK_INT >= 14) {
                                IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplIceCreamSandwich();
                            } else
                                if (android.os.Build.VERSION.SDK_INT >= 11) {
                                    IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplHoneycomb();
                                } else {
                                    IMPL = new android.support.v4.app.NotificationCompat.NotificationCompatImplBase();
                                }






    }

    /**
     * Builder class for {@link NotificationCompat} objects.  Allows easier control over
     * all the flags, as well as help constructing the typical notification layouts.
     * <p>
     * On platform versions that don't offer expanded notifications, methods that depend on
     * expanded notifications have no effect.
     * </p>
     * <p>
     * For example, action buttons won't appear on platforms prior to Android 4.1. Action
     * buttons depend on expanded notifications, which are only available in Android 4.1
     * and later.
     * <p>
     * For this reason, you should always ensure that UI controls in a notification are also
     * available in an {@link android.app.Activity} in your app, and you should always start that
     * {@link android.app.Activity} when users click the notification. To do this, use the
     * {@link NotificationCompat.Builder#setContentIntent setContentIntent()}
     * method.
     * </p>
     */
    public static class Builder {
        /**
         * Maximum length of CharSequences accepted by Builder and friends.
         *
         * <p>
         * Avoids spamming the system with overly large strings such as full e-mails.
         */
        private static final int MAX_CHARSEQUENCE_LENGTH = 5 * 1024;

        // All these variables are declared public/hidden so they can be accessed by a builder
        // extender.
        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.content.Context mContext;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public java.lang.CharSequence mContentTitle;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public java.lang.CharSequence mContentText;

        android.app.PendingIntent mContentIntent;

        android.app.PendingIntent mFullScreenIntent;

        android.widget.RemoteViews mTickerView;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.graphics.Bitmap mLargeIcon;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public java.lang.CharSequence mContentInfo;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public int mNumber;

        int mPriority;

        boolean mShowWhen = true;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public boolean mUseChronometer;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v4.app.NotificationCompat.Style mStyle;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public java.lang.CharSequence mSubText;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public java.lang.CharSequence[] mRemoteInputHistory;

        int mProgressMax;

        int mProgress;

        boolean mProgressIndeterminate;

        java.lang.String mGroupKey;

        boolean mGroupSummary;

        java.lang.String mSortKey;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public java.util.ArrayList<android.support.v4.app.NotificationCompat.Action> mActions = new java.util.ArrayList<android.support.v4.app.NotificationCompat.Action>();

        boolean mLocalOnly = false;

        java.lang.String mCategory;

        android.os.Bundle mExtras;

        int mColor = android.support.v4.app.NotificationCompat.COLOR_DEFAULT;

        int mVisibility = android.support.v4.app.NotificationCompat.VISIBILITY_PRIVATE;

        android.app.Notification mPublicVersion;

        android.widget.RemoteViews mContentView;

        android.widget.RemoteViews mBigContentView;

        android.widget.RemoteViews mHeadsUpContentView;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.app.Notification mNotification = new android.app.Notification();

        public java.util.ArrayList<java.lang.String> mPeople;

        /**
         * Constructor.
         *
         * Automatically sets the when field to {@link System#currentTimeMillis()
         * System.currentTimeMillis()} and the audio stream to the
         * {@link Notification#STREAM_DEFAULT}.
         *
         * @param context
         * 		A {@link Context} that will be used to construct the
         * 		RemoteViews. The Context will not be held past the lifetime of this
         * 		Builder object.
         */
        public Builder(android.content.Context context) {
            mContext = context;
            // Set defaults to match the defaults of a Notification
            mNotification.when = java.lang.System.currentTimeMillis();
            mNotification.audioStreamType = android.app.Notification.STREAM_DEFAULT;
            mPriority = android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
            mPeople = new java.util.ArrayList<java.lang.String>();
        }

        /**
         * Set the time that the event occurred.  Notifications in the panel are
         * sorted by this time.
         */
        public android.support.v4.app.NotificationCompat.Builder setWhen(long when) {
            mNotification.when = when;
            return this;
        }

        /**
         * Control whether the timestamp set with {@link #setWhen(long) setWhen} is shown
         * in the content view.
         */
        public android.support.v4.app.NotificationCompat.Builder setShowWhen(boolean show) {
            mShowWhen = show;
            return this;
        }

        /**
         * Show the {@link Notification#when} field as a stopwatch.
         *
         * Instead of presenting <code>when</code> as a timestamp, the notification will show an
         * automatically updating display of the minutes and seconds since <code>when</code>.
         *
         * Useful when showing an elapsed time (like an ongoing phone call).
         *
         * @see android.widget.Chronometer
         * @see Notification#when
         */
        public android.support.v4.app.NotificationCompat.Builder setUsesChronometer(boolean b) {
            mUseChronometer = b;
            return this;
        }

        /**
         * Set the small icon to use in the notification layouts.  Different classes of devices
         * may return different sizes.  See the UX guidelines for more information on how to
         * design these icons.
         *
         * @param icon
         * 		A resource ID in the application's package of the drawble to use.
         */
        public android.support.v4.app.NotificationCompat.Builder setSmallIcon(int icon) {
            mNotification.icon = icon;
            return this;
        }

        /**
         * A variant of {@link #setSmallIcon(int) setSmallIcon(int)} that takes an additional
         * level parameter for when the icon is a {@link android.graphics.drawable.LevelListDrawable
         * LevelListDrawable}.
         *
         * @param icon
         * 		A resource ID in the application's package of the drawble to use.
         * @param level
         * 		The level to use for the icon.
         * @see android.graphics.drawable.LevelListDrawable
         */
        public android.support.v4.app.NotificationCompat.Builder setSmallIcon(int icon, int level) {
            mNotification.icon = icon;
            mNotification.iconLevel = level;
            return this;
        }

        /**
         * Set the title (first row) of the notification, in a standard notification.
         */
        public android.support.v4.app.NotificationCompat.Builder setContentTitle(java.lang.CharSequence title) {
            mContentTitle = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the text (second row) of the notification, in a standard notification.
         */
        public android.support.v4.app.NotificationCompat.Builder setContentText(java.lang.CharSequence text) {
            mContentText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(text);
            return this;
        }

        /**
         * Set the third line of text in the platform notification template.
         * Don't use if you're also using {@link #setProgress(int, int, boolean)};
         * they occupy the same location in the standard template.
         * <br>
         * If the platform does not provide large-format notifications, this method has no effect.
         * The third line of text only appears in expanded view.
         * <br>
         */
        public android.support.v4.app.NotificationCompat.Builder setSubText(java.lang.CharSequence text) {
            mSubText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(text);
            return this;
        }

        /**
         * Set the remote input history.
         *
         * This should be set to the most recent inputs that have been sent
         * through a {@link RemoteInput} of this Notification and cleared once the it is no
         * longer relevant (e.g. for chat notifications once the other party has responded).
         *
         * The most recent input must be stored at the 0 index, the second most recent at the
         * 1 index, etc. Note that the system will limit both how far back the inputs will be shown
         * and how much of each individual input is shown.
         *
         * <p>Note: The reply text will only be shown on notifications that have least one action
         * with a {@code RemoteInput}.</p>
         */
        public android.support.v4.app.NotificationCompat.Builder setRemoteInputHistory(java.lang.CharSequence[] text) {
            mRemoteInputHistory = text;
            return this;
        }

        /**
         * Set the large number at the right-hand side of the notification.  This is
         * equivalent to setContentInfo, although it might show the number in a different
         * font size for readability.
         */
        public android.support.v4.app.NotificationCompat.Builder setNumber(int number) {
            mNumber = number;
            return this;
        }

        /**
         * Set the large text at the right-hand side of the notification.
         */
        public android.support.v4.app.NotificationCompat.Builder setContentInfo(java.lang.CharSequence info) {
            mContentInfo = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(info);
            return this;
        }

        /**
         * Set the progress this notification represents, which may be
         * represented as a {@link android.widget.ProgressBar}.
         */
        public android.support.v4.app.NotificationCompat.Builder setProgress(int max, int progress, boolean indeterminate) {
            mProgressMax = max;
            mProgress = progress;
            mProgressIndeterminate = indeterminate;
            return this;
        }

        /**
         * Supply a custom RemoteViews to use instead of the standard one.
         */
        public android.support.v4.app.NotificationCompat.Builder setContent(android.widget.RemoteViews views) {
            mNotification.contentView = views;
            return this;
        }

        /**
         * Supply a {@link PendingIntent} to send when the notification is clicked.
         * If you do not supply an intent, you can now add PendingIntents to individual
         * views to be launched when clicked by calling {@link RemoteViews#setOnClickPendingIntent
         * RemoteViews.setOnClickPendingIntent(int,PendingIntent)}.  Be sure to
         * read {@link Notification#contentIntent Notification.contentIntent} for
         * how to correctly use this.
         */
        public android.support.v4.app.NotificationCompat.Builder setContentIntent(android.app.PendingIntent intent) {
            mContentIntent = intent;
            return this;
        }

        /**
         * Supply a {@link PendingIntent} to send when the notification is cleared by the user
         * directly from the notification panel.  For example, this intent is sent when the user
         * clicks the "Clear all" button, or the individual "X" buttons on notifications.  This
         * intent is not sent when the application calls
         * {@link android.app.NotificationManager#cancel NotificationManager.cancel(int)}.
         */
        public android.support.v4.app.NotificationCompat.Builder setDeleteIntent(android.app.PendingIntent intent) {
            mNotification.deleteIntent = intent;
            return this;
        }

        /**
         * An intent to launch instead of posting the notification to the status bar.
         * Only for use with extremely high-priority notifications demanding the user's
         * <strong>immediate</strong> attention, such as an incoming phone call or
         * alarm clock that the user has explicitly set to a particular time.
         * If this facility is used for something else, please give the user an option
         * to turn it off and use a normal notification, as this can be extremely
         * disruptive.
         *
         * <p>
         * On some platforms, the system UI may choose to display a heads-up notification,
         * instead of launching this intent, while the user is using the device.
         * </p>
         *
         * @param intent
         * 		The pending intent to launch.
         * @param highPriority
         * 		Passing true will cause this notification to be sent
         * 		even if other notifications are suppressed.
         */
        public android.support.v4.app.NotificationCompat.Builder setFullScreenIntent(android.app.PendingIntent intent, boolean highPriority) {
            mFullScreenIntent = intent;
            setFlag(android.support.v4.app.NotificationCompat.FLAG_HIGH_PRIORITY, highPriority);
            return this;
        }

        /**
         * Set the text that is displayed in the status bar when the notification first
         * arrives.
         */
        public android.support.v4.app.NotificationCompat.Builder setTicker(java.lang.CharSequence tickerText) {
            mNotification.tickerText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(tickerText);
            return this;
        }

        /**
         * Set the text that is displayed in the status bar when the notification first
         * arrives, and also a RemoteViews object that may be displayed instead on some
         * devices.
         */
        public android.support.v4.app.NotificationCompat.Builder setTicker(java.lang.CharSequence tickerText, android.widget.RemoteViews views) {
            mNotification.tickerText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(tickerText);
            mTickerView = views;
            return this;
        }

        /**
         * Set the large icon that is shown in the ticker and notification.
         */
        public android.support.v4.app.NotificationCompat.Builder setLargeIcon(android.graphics.Bitmap icon) {
            mLargeIcon = icon;
            return this;
        }

        /**
         * Set the sound to play.  It will play on the default stream.
         *
         * <p>
         * On some platforms, a notification that is noisy is more likely to be presented
         * as a heads-up notification.
         * </p>
         */
        public android.support.v4.app.NotificationCompat.Builder setSound(android.net.Uri sound) {
            mNotification.sound = sound;
            mNotification.audioStreamType = android.app.Notification.STREAM_DEFAULT;
            return this;
        }

        /**
         * Set the sound to play.  It will play on the stream you supply.
         *
         * <p>
         * On some platforms, a notification that is noisy is more likely to be presented
         * as a heads-up notification.
         * </p>
         *
         * @see Notification#STREAM_DEFAULT
         * @see AudioManager for the <code>STREAM_</code> constants.
         */
        public android.support.v4.app.NotificationCompat.Builder setSound(android.net.Uri sound, int streamType) {
            mNotification.sound = sound;
            mNotification.audioStreamType = streamType;
            return this;
        }

        /**
         * Set the vibration pattern to use.
         *
         * <p>
         * On some platforms, a notification that vibrates is more likely to be presented
         * as a heads-up notification.
         * </p>
         *
         * @see android.os.Vibrator for a discussion of the <code>pattern</code>
        parameter.
         */
        public android.support.v4.app.NotificationCompat.Builder setVibrate(long[] pattern) {
            mNotification.vibrate = pattern;
            return this;
        }

        /**
         * Set the argb value that you would like the LED on the device to blink, as well as the
         * rate.  The rate is specified in terms of the number of milliseconds to be on
         * and then the number of milliseconds to be off.
         */
        public android.support.v4.app.NotificationCompat.Builder setLights(@android.support.annotation.ColorInt
        int argb, int onMs, int offMs) {
            mNotification.ledARGB = argb;
            mNotification.ledOnMS = onMs;
            mNotification.ledOffMS = offMs;
            boolean showLights = (mNotification.ledOnMS != 0) && (mNotification.ledOffMS != 0);
            mNotification.flags = (mNotification.flags & (~android.app.Notification.FLAG_SHOW_LIGHTS)) | (showLights ? android.app.Notification.FLAG_SHOW_LIGHTS : 0);
            return this;
        }

        /**
         * Set whether this is an ongoing notification.
         *
         * <p>Ongoing notifications differ from regular notifications in the following ways:
         * <ul>
         *   <li>Ongoing notifications are sorted above the regular notifications in the
         *   notification panel.</li>
         *   <li>Ongoing notifications do not have an 'X' close button, and are not affected
         *   by the "Clear all" button.
         * </ul>
         */
        public android.support.v4.app.NotificationCompat.Builder setOngoing(boolean ongoing) {
            setFlag(android.app.Notification.FLAG_ONGOING_EVENT, ongoing);
            return this;
        }

        /**
         * Set this flag if you would only like the sound, vibrate
         * and ticker to be played if the notification is not already showing.
         */
        public android.support.v4.app.NotificationCompat.Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(android.app.Notification.FLAG_ONLY_ALERT_ONCE, onlyAlertOnce);
            return this;
        }

        /**
         * Setting this flag will make it so the notification is automatically
         * canceled when the user clicks it in the panel.  The PendingIntent
         * set with {@link #setDeleteIntent} will be broadcast when the notification
         * is canceled.
         */
        public android.support.v4.app.NotificationCompat.Builder setAutoCancel(boolean autoCancel) {
            setFlag(android.app.Notification.FLAG_AUTO_CANCEL, autoCancel);
            return this;
        }

        /**
         * Set whether or not this notification is only relevant to the current device.
         *
         * <p>Some notifications can be bridged to other devices for remote display.
         * This hint can be set to recommend this notification not be bridged.
         */
        public android.support.v4.app.NotificationCompat.Builder setLocalOnly(boolean b) {
            mLocalOnly = b;
            return this;
        }

        /**
         * Set the notification category.
         *
         * <p>Must be one of the predefined notification categories (see the <code>CATEGORY_*</code>
         * constants in {@link Notification}) that best describes this notification.
         * May be used by the system for ranking and filtering.
         */
        public android.support.v4.app.NotificationCompat.Builder setCategory(java.lang.String category) {
            mCategory = category;
            return this;
        }

        /**
         * Set the default notification options that will be used.
         * <p>
         * The value should be one or more of the following fields combined with
         * bitwise-or:
         * {@link Notification#DEFAULT_SOUND}, {@link Notification#DEFAULT_VIBRATE},
         * {@link Notification#DEFAULT_LIGHTS}.
         * <p>
         * For all default values, use {@link Notification#DEFAULT_ALL}.
         */
        public android.support.v4.app.NotificationCompat.Builder setDefaults(int defaults) {
            mNotification.defaults = defaults;
            if ((defaults & android.app.Notification.DEFAULT_LIGHTS) != 0) {
                mNotification.flags |= android.app.Notification.FLAG_SHOW_LIGHTS;
            }
            return this;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                mNotification.flags |= mask;
            } else {
                mNotification.flags &= ~mask;
            }
        }

        /**
         * Set the relative priority for this notification.
         *
         * Priority is an indication of how much of the user's
         * valuable attention should be consumed by this
         * notification. Low-priority notifications may be hidden from
         * the user in certain situations, while the user might be
         * interrupted for a higher-priority notification.
         * The system sets a notification's priority based on various factors including the
         * setPriority value. The effect may differ slightly on different platforms.
         *
         * @param pri
         * 		Relative priority for this notification. Must be one of
         * 		the priority constants defined by {@link NotificationCompat}.
         * 		Acceptable values range from {@link NotificationCompat#PRIORITY_MIN} (-2) to {@link NotificationCompat#PRIORITY_MAX} (2).
         */
        public android.support.v4.app.NotificationCompat.Builder setPriority(int pri) {
            mPriority = pri;
            return this;
        }

        /**
         * Add a person that is relevant to this notification.
         *
         * <P>
         * Depending on user preferences, this annotation may allow the notification to pass
         * through interruption filters, and to appear more prominently in the user interface.
         * </P>
         *
         * <P>
         * The person should be specified by the {@code String} representation of a
         * {@link android.provider.ContactsContract.Contacts#CONTENT_LOOKUP_URI}.
         * </P>
         *
         * <P>The system will also attempt to resolve {@code mailto:} and {@code tel:} schema
         * URIs.  The path part of these URIs must exist in the contacts database, in the
         * appropriate column, or the reference will be discarded as invalid. Telephone schema
         * URIs will be resolved by {@link android.provider.ContactsContract.PhoneLookup}.
         * </P>
         *
         * @param uri
         * 		A URI for the person.
         * @see Notification#EXTRA_PEOPLE
         */
        public android.support.v4.app.NotificationCompat.Builder addPerson(java.lang.String uri) {
            mPeople.add(uri);
            return this;
        }

        /**
         * Set this notification to be part of a group of notifications sharing the same key.
         * Grouped notifications may display in a cluster or stack on devices which
         * support such rendering.
         *
         * <p>To make this notification the summary for its group, also call
         * {@link #setGroupSummary}. A sort order can be specified for group members by using
         * {@link #setSortKey}.
         *
         * @param groupKey
         * 		The group key of the group.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.Builder setGroup(java.lang.String groupKey) {
            mGroupKey = groupKey;
            return this;
        }

        /**
         * Set this notification to be the group summary for a group of notifications.
         * Grouped notifications may display in a cluster or stack on devices which
         * support such rendering. Requires a group key also be set using {@link #setGroup}.
         *
         * @param isGroupSummary
         * 		Whether this notification should be a group summary.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.Builder setGroupSummary(boolean isGroupSummary) {
            mGroupSummary = isGroupSummary;
            return this;
        }

        /**
         * Set a sort key that orders this notification among other notifications from the
         * same package. This can be useful if an external sort was already applied and an app
         * would like to preserve this. Notifications will be sorted lexicographically using this
         * value, although providing different priorities in addition to providing sort key may
         * cause this value to be ignored.
         *
         * <p>This sort key can also be used to order members of a notification group. See
         * {@link Builder#setGroup}.
         *
         * @see String#compareTo(String)
         */
        public android.support.v4.app.NotificationCompat.Builder setSortKey(java.lang.String sortKey) {
            mSortKey = sortKey;
            return this;
        }

        /**
         * Merge additional metadata into this notification.
         *
         * <p>Values within the Bundle will replace existing extras values in this Builder.
         *
         * @see Notification#extras
         */
        public android.support.v4.app.NotificationCompat.Builder addExtras(android.os.Bundle extras) {
            if (extras != null) {
                if (mExtras == null) {
                    mExtras = new android.os.Bundle(extras);
                } else {
                    mExtras.putAll(extras);
                }
            }
            return this;
        }

        /**
         * Set metadata for this notification.
         *
         * <p>A reference to the Bundle is held for the lifetime of this Builder, and the Bundle's
         * current contents are copied into the Notification each time {@link #build()} is
         * called.
         *
         * <p>Replaces any existing extras values with those from the provided Bundle.
         * Use {@link #addExtras} to merge in metadata instead.
         *
         * @see Notification#extras
         */
        public android.support.v4.app.NotificationCompat.Builder setExtras(android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Get the current metadata Bundle used by this notification Builder.
         *
         * <p>The returned Bundle is shared with this Builder.
         *
         * <p>The current contents of this Bundle are copied into the Notification each time
         * {@link #build()} is called.
         *
         * @see Notification#extras
         */
        public android.os.Bundle getExtras() {
            if (mExtras == null) {
                mExtras = new android.os.Bundle();
            }
            return mExtras;
        }

        /**
         * Add an action to this notification. Actions are typically displayed by
         * the system as a button adjacent to the notification content.
         * <br>
         * Action buttons won't appear on platforms prior to Android 4.1. Action
         * buttons depend on expanded notifications, which are only available in Android 4.1
         * and later. To ensure that an action button's functionality is always available, first
         * implement the functionality in the {@link android.app.Activity} that starts when a user
         * clicks the  notification (see {@link #setContentIntent setContentIntent()}), and then
         * enhance the notification by implementing the same functionality with
         * {@link #addAction addAction()}.
         *
         * @param icon
         * 		Resource ID of a drawable that represents the action.
         * @param title
         * 		Text describing the action.
         * @param intent
         * 		{@link android.app.PendingIntent} to be fired when the action is invoked.
         */
        public android.support.v4.app.NotificationCompat.Builder addAction(int icon, java.lang.CharSequence title, android.app.PendingIntent intent) {
            mActions.add(new android.support.v4.app.NotificationCompat.Action(icon, title, intent));
            return this;
        }

        /**
         * Add an action to this notification. Actions are typically displayed by
         * the system as a button adjacent to the notification content.
         * <br>
         * Action buttons won't appear on platforms prior to Android 4.1. Action
         * buttons depend on expanded notifications, which are only available in Android 4.1
         * and later. To ensure that an action button's functionality is always available, first
         * implement the functionality in the {@link android.app.Activity} that starts when a user
         * clicks the  notification (see {@link #setContentIntent setContentIntent()}), and then
         * enhance the notification by implementing the same functionality with
         * {@link #addAction addAction()}.
         *
         * @param action
         * 		The action to add.
         */
        public android.support.v4.app.NotificationCompat.Builder addAction(android.support.v4.app.NotificationCompat.Action action) {
            mActions.add(action);
            return this;
        }

        /**
         * Add a rich notification style to be applied at build time.
         * <br>
         * If the platform does not provide rich notification styles, this method has no effect. The
         * user will always see the normal notification style.
         *
         * @param style
         * 		Object responsible for modifying the notification style.
         */
        public android.support.v4.app.NotificationCompat.Builder setStyle(android.support.v4.app.NotificationCompat.Style style) {
            if (mStyle != style) {
                mStyle = style;
                if (mStyle != null) {
                    mStyle.setBuilder(this);
                }
            }
            return this;
        }

        /**
         * Sets {@link Notification#color}.
         *
         * @param argb
         * 		The accent color to use
         * @return The same Builder.
         */
        public android.support.v4.app.NotificationCompat.Builder setColor(@android.support.annotation.ColorInt
        int argb) {
            mColor = argb;
            return this;
        }

        /**
         * Sets {@link Notification#visibility}.
         *
         * @param visibility
         * 		One of {@link Notification#VISIBILITY_PRIVATE} (the default),
         * 		{@link Notification#VISIBILITY_PUBLIC}, or
         * 		{@link Notification#VISIBILITY_SECRET}.
         */
        public android.support.v4.app.NotificationCompat.Builder setVisibility(int visibility) {
            mVisibility = visibility;
            return this;
        }

        /**
         * Supply a replacement Notification whose contents should be shown in insecure contexts
         * (i.e. atop the secure lockscreen). See {@link Notification#visibility} and
         * {@link #VISIBILITY_PUBLIC}.
         *
         * @param n
         * 		A replacement notification, presumably with some or all info redacted.
         * @return The same Builder.
         */
        public android.support.v4.app.NotificationCompat.Builder setPublicVersion(android.app.Notification n) {
            mPublicVersion = n;
            return this;
        }

        /**
         * Supply custom RemoteViews to use instead of the platform template.
         *
         * This will override the layout that would otherwise be constructed by this Builder
         * object.
         */
        public android.support.v4.app.NotificationCompat.Builder setCustomContentView(android.widget.RemoteViews contentView) {
            mContentView = contentView;
            return this;
        }

        /**
         * Supply custom RemoteViews to use instead of the platform template in the expanded form.
         *
         * This will override the expanded layout that would otherwise be constructed by this
         * Builder object.
         *
         * No-op on versions prior to {@link android.os.Build.VERSION_CODES#JELLY_BEAN}.
         */
        public android.support.v4.app.NotificationCompat.Builder setCustomBigContentView(android.widget.RemoteViews contentView) {
            mBigContentView = contentView;
            return this;
        }

        /**
         * Supply custom RemoteViews to use instead of the platform template in the heads up dialog.
         *
         * This will override the heads-up layout that would otherwise be constructed by this
         * Builder object.
         *
         * No-op on versions prior to {@link android.os.Build.VERSION_CODES#LOLLIPOP}.
         */
        public android.support.v4.app.NotificationCompat.Builder setCustomHeadsUpContentView(android.widget.RemoteViews contentView) {
            mHeadsUpContentView = contentView;
            return this;
        }

        /**
         * Apply an extender to this notification builder. Extenders may be used to add
         * metadata or change options on this builder.
         */
        public android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Extender extender) {
            extender.extend(this);
            return this;
        }

        /**
         *
         *
         * @deprecated Use {@link #build()} instead.
         */
        @java.lang.Deprecated
        public android.app.Notification getNotification() {
            return build();
        }

        /**
         * Combine all of the options that have been set and return a new {@link Notification}
         * object.
         */
        public android.app.Notification build() {
            return android.support.v4.app.NotificationCompat.IMPL.build(this, getExtender());
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        protected android.support.v4.app.NotificationCompat.BuilderExtender getExtender() {
            return new android.support.v4.app.NotificationCompat.BuilderExtender();
        }

        protected static java.lang.CharSequence limitCharSequenceLength(java.lang.CharSequence cs) {
            if (cs == null)
                return cs;

            if (cs.length() > android.support.v4.app.NotificationCompat.Builder.MAX_CHARSEQUENCE_LENGTH) {
                cs = cs.subSequence(0, android.support.v4.app.NotificationCompat.Builder.MAX_CHARSEQUENCE_LENGTH);
            }
            return cs;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.widget.RemoteViews getContentView() {
            return mContentView;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.widget.RemoteViews getBigContentView() {
            return mBigContentView;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.widget.RemoteViews getHeadsUpContentView() {
            return mHeadsUpContentView;
        }

        /**
         * return when if it is showing or 0 otherwise
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public long getWhenIfShowing() {
            return mShowWhen ? mNotification.when : 0;
        }

        /**
         *
         *
         * @return the priority set on the notification
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public int getPriority() {
            return mPriority;
        }

        /**
         *
         *
         * @return the color of the notification
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public int getColor() {
            return mColor;
        }

        /**
         *
         *
         * @return the text of the notification
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        protected java.lang.CharSequence resolveText() {
            return mContentText;
        }

        /**
         *
         *
         * @return the title of the notification
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        protected java.lang.CharSequence resolveTitle() {
            return mContentTitle;
        }
    }

    /**
     * An object that can apply a rich notification style to a {@link Notification.Builder}
     * object.
     * <br>
     * If the platform does not provide rich notification styles, methods in this class have no
     * effect.
     */
    public static abstract class Style {
        android.support.v4.app.NotificationCompat.Builder mBuilder;

        java.lang.CharSequence mBigContentTitle;

        java.lang.CharSequence mSummaryText;

        boolean mSummaryTextSet = false;

        public void setBuilder(android.support.v4.app.NotificationCompat.Builder builder) {
            if (mBuilder != builder) {
                mBuilder = builder;
                if (mBuilder != null) {
                    mBuilder.setStyle(this);
                }
            }
        }

        public android.app.Notification build() {
            android.app.Notification notification = null;
            if (mBuilder != null) {
                notification = mBuilder.build();
            }
            return notification;
        }

        /**
         *
         *
         * @unknown 
         */
        // TODO: implement for all styles
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public void addCompatExtras(android.os.Bundle extras) {
        }

        /**
         *
         *
         * @unknown 
         */
        // TODO: implement for all styles
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        protected void restoreFromCompatExtras(android.os.Bundle extras) {
        }
    }

    /**
     * Helper class for generating large-format notifications that include a large image attachment.
     * <br>
     * If the platform does not provide large-format notifications, this method has no effect. The
     * user will always see the normal notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification notif = new Notification.Builder(mContext)
     *     .setContentTitle(&quot;New photo from &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_post)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.BigPictureStyle()
     *         .bigPicture(aBigBitmap))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class BigPictureStyle extends android.support.v4.app.NotificationCompat.Style {
        android.graphics.Bitmap mPicture;

        android.graphics.Bitmap mBigLargeIcon;

        boolean mBigLargeIconSet;

        public BigPictureStyle() {
        }

        public BigPictureStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public android.support.v4.app.NotificationCompat.BigPictureStyle setBigContentTitle(java.lang.CharSequence title) {
            mBigContentTitle = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public android.support.v4.app.NotificationCompat.BigPictureStyle setSummaryText(java.lang.CharSequence cs) {
            mSummaryText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(cs);
            mSummaryTextSet = true;
            return this;
        }

        /**
         * Provide the bitmap to be used as the payload for the BigPicture notification.
         */
        public android.support.v4.app.NotificationCompat.BigPictureStyle bigPicture(android.graphics.Bitmap b) {
            mPicture = b;
            return this;
        }

        /**
         * Override the large icon when the big notification is shown.
         */
        public android.support.v4.app.NotificationCompat.BigPictureStyle bigLargeIcon(android.graphics.Bitmap b) {
            mBigLargeIcon = b;
            mBigLargeIconSet = true;
            return this;
        }
    }

    /**
     * Helper class for generating large-format notifications that include a lot of text.
     *
     * <br>
     * If the platform does not provide large-format notifications, this method has no effect. The
     * user will always see the normal notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification notif = new Notification.Builder(mContext)
     *     .setContentTitle(&quot;New mail from &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_mail)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.BigTextStyle()
     *         .bigText(aVeryLongString))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class BigTextStyle extends android.support.v4.app.NotificationCompat.Style {
        java.lang.CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public android.support.v4.app.NotificationCompat.BigTextStyle setBigContentTitle(java.lang.CharSequence title) {
            mBigContentTitle = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public android.support.v4.app.NotificationCompat.BigTextStyle setSummaryText(java.lang.CharSequence cs) {
            mSummaryText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(cs);
            mSummaryTextSet = true;
            return this;
        }

        /**
         * Provide the longer text to be displayed in the big form of the
         * template in place of the content text.
         */
        public android.support.v4.app.NotificationCompat.BigTextStyle bigText(java.lang.CharSequence cs) {
            mBigText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(cs);
            return this;
        }
    }

    /**
     * Helper class for generating large-format notifications that include multiple back-and-forth
     * messages of varying types between any number of people.
     *
     * <br>
     * In order to get a backwards compatible behavior, the app needs to use the v7 version of the
     * notification builder together with this style, otherwise the user will see the normal
     * notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like
     * so:
     * <pre class="prettyprint">
     *
     * Notification noti = new Notification.Builder()
     *     .setContentTitle(&quot;2 new messages wtih &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_message)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.MessagingStyle(resources.getString(R.string.reply_name))
     *         .addMessage(messages[0].getText(), messages[0].getTime(), messages[0].getSender())
     *         .addMessage(messages[1].getText(), messages[1].getTime(), messages[1].getSender()))
     *     .build();
     * </pre>
     */
    public static class MessagingStyle extends android.support.v4.app.NotificationCompat.Style {
        /**
         * The maximum number of messages that will be retained in the Notification itself (the
         * number displayed is up to the platform).
         */
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;

        java.lang.CharSequence mUserDisplayName;

        java.lang.CharSequence mConversationTitle;

        java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> mMessages = new java.util.ArrayList<>();

        MessagingStyle() {
        }

        /**
         *
         *
         * @param userDisplayName
         * 		Required - the name to be displayed for any replies sent by the
         * 		user before the posting app reposts the notification with those messages after they've
         * 		been actually sent and in previous messages sent by the user added in
         * 		{@link #addMessage(Message)}
         */
        public MessagingStyle(@android.support.annotation.NonNull
        java.lang.CharSequence userDisplayName) {
            mUserDisplayName = userDisplayName;
        }

        /**
         * Returns the name to be displayed for any replies sent by the user
         */
        public java.lang.CharSequence getUserDisplayName() {
            return mUserDisplayName;
        }

        /**
         * Sets the title to be displayed on this conversation. This should only be used for
         * group messaging and left unset for one-on-one conversations.
         *
         * @param conversationTitle
         * 		
         * @return this object for method chaining.
         */
        public android.support.v4.app.NotificationCompat.MessagingStyle setConversationTitle(java.lang.CharSequence conversationTitle) {
            mConversationTitle = conversationTitle;
            return this;
        }

        /**
         * Return the title to be displayed on this conversation. Can be <code>null</code> and
         * should be for one-on-one conversations
         */
        public java.lang.CharSequence getConversationTitle() {
            return mConversationTitle;
        }

        /**
         * Adds a message for display by this notification. Convenience call for a simple
         * {@link Message} in {@link #addMessage(Message)}
         *
         * @param text
         * 		A {@link CharSequence} to be displayed as the message content
         * @param timestamp
         * 		Time at which the message arrived
         * @param sender
         * 		A {@link CharSequence} to be used for displaying the name of the
         * 		sender. Should be <code>null</code> for messages by the current user, in which case
         * 		the platform will insert {@link #getUserDisplayName()}.
         * 		Should be unique amongst all individuals in the conversation, and should be
         * 		consistent during re-posts of the notification.
         * @see Message#Message(CharSequence, long, CharSequence)
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.MessagingStyle addMessage(java.lang.CharSequence text, long timestamp, java.lang.CharSequence sender) {
            mMessages.add(new android.support.v4.app.NotificationCompat.MessagingStyle.Message(text, timestamp, sender));
            if (mMessages.size() > android.support.v4.app.NotificationCompat.MessagingStyle.MAXIMUM_RETAINED_MESSAGES) {
                mMessages.remove(0);
            }
            return this;
        }

        /**
         * Adds a {@link Message} for display in this notification.
         *
         * @param message
         * 		The {@link Message} to be displayed
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.MessagingStyle addMessage(android.support.v4.app.NotificationCompat.MessagingStyle.Message message) {
            mMessages.add(message);
            if (mMessages.size() > android.support.v4.app.NotificationCompat.MessagingStyle.MAXIMUM_RETAINED_MESSAGES) {
                mMessages.remove(0);
            }
            return this;
        }

        /**
         * Gets the list of {@code Message} objects that represent the notification
         */
        public java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> getMessages() {
            return mMessages;
        }

        /**
         * Retrieves a {@link MessagingStyle} from a {@link Notification}, enabling an application
         * that has set a {@link MessagingStyle} using {@link NotificationCompat} or
         * {@link android.app.Notification.Builder} to send messaging information to another
         * application using {@link NotificationCompat}, regardless of the API level of the system.
         * Returns {@code null} if there is no {@link MessagingStyle} set.
         */
        public static android.support.v4.app.NotificationCompat.MessagingStyle extractMessagingStyleFromNotification(android.app.Notification notif) {
            android.support.v4.app.NotificationCompat.MessagingStyle style;
            android.os.Bundle extras = android.support.v4.app.NotificationCompat.IMPL.getExtras(notif);
            if (!extras.containsKey(android.support.v4.app.NotificationCompat.EXTRA_SELF_DISPLAY_NAME)) {
                style = null;
            } else {
                try {
                    style = new android.support.v4.app.NotificationCompat.MessagingStyle();
                    style.restoreFromCompatExtras(extras);
                } catch (java.lang.ClassCastException e) {
                    style = null;
                }
            }
            return style;
        }

        @java.lang.Override
        public void addCompatExtras(android.os.Bundle extras) {
            super.addCompatExtras(extras);
            if (mUserDisplayName != null) {
                extras.putCharSequence(android.support.v4.app.NotificationCompat.EXTRA_SELF_DISPLAY_NAME, mUserDisplayName);
            }
            if (mConversationTitle != null) {
                extras.putCharSequence(android.support.v4.app.NotificationCompat.EXTRA_CONVERSATION_TITLE, mConversationTitle);
            }
            if (!mMessages.isEmpty()) {
                extras.putParcelableArray(android.support.v4.app.NotificationCompat.EXTRA_MESSAGES, android.support.v4.app.NotificationCompat.MessagingStyle.Message.getBundleArrayForMessages(mMessages));
            }
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @java.lang.Override
        protected void restoreFromCompatExtras(android.os.Bundle extras) {
            mMessages.clear();
            mUserDisplayName = extras.getString(android.support.v4.app.NotificationCompat.EXTRA_SELF_DISPLAY_NAME);
            mConversationTitle = extras.getString(android.support.v4.app.NotificationCompat.EXTRA_CONVERSATION_TITLE);
            android.os.Parcelable[] parcelables = extras.getParcelableArray(android.support.v4.app.NotificationCompat.EXTRA_MESSAGES);
            if (parcelables != null) {
                mMessages = android.support.v4.app.NotificationCompat.MessagingStyle.Message.getMessagesFromBundleArray(parcelables);
            }
        }

        public static final class Message {
            static final java.lang.String KEY_TEXT = "text";

            static final java.lang.String KEY_TIMESTAMP = "time";

            static final java.lang.String KEY_SENDER = "sender";

            static final java.lang.String KEY_DATA_MIME_TYPE = "type";

            static final java.lang.String KEY_DATA_URI = "uri";

            private final java.lang.CharSequence mText;

            private final long mTimestamp;

            private final java.lang.CharSequence mSender;

            private java.lang.String mDataMimeType;

            private android.net.Uri mDataUri;

            /**
             * Constructor
             *
             * @param text
             * 		A {@link CharSequence} to be displayed as the message content
             * @param timestamp
             * 		Time at which the message arrived
             * @param sender
             * 		A {@link CharSequence} to be used for displaying the name of the
             * 		sender. Should be <code>null</code> for messages by the current user, in which case
             * 		the platform will insert {@link MessagingStyle#getUserDisplayName()}.
             * 		Should be unique amongst all individuals in the conversation, and should be
             * 		consistent during re-posts of the notification.
             */
            public Message(java.lang.CharSequence text, long timestamp, java.lang.CharSequence sender) {
                mText = text;
                mTimestamp = timestamp;
                mSender = sender;
            }

            /**
             * Sets a binary blob of data and an associated MIME type for a message. In the case
             * where the platform doesn't support the MIME type, the original text provided in the
             * constructor will be used.
             *
             * @param dataMimeType
             * 		The MIME type of the content. See
             * 		<a href="{@docRoot }notifications/messaging.html"> for the list of supported MIME
             * 		types on Android and Android Wear.
             * @param dataUri
             * 		The uri containing the content whose type is given by the MIME type.
             * 		<p class="note">
             * 		<ol>
             * 		<li>Notification Listeners including the System UI need permission to access the
             * 		data the Uri points to. The recommended ways to do this are:</li>
             * 		<li>Store the data in your own ContentProvider, making sure that other apps have
             * 		the correct permission to access your provider. The preferred mechanism for
             * 		providing access is to use per-URI permissions which are temporary and only
             * 		grant access to the receiving application. An easy way to create a
             * 		ContentProvider like this is to use the FileProvider helper class.</li>
             * 		<li>Use the system MediaStore. The MediaStore is primarily aimed at video, audio
             * 		and image MIME types, however beginning with Android 3.0 (API level 11) it can
             * 		also store non-media types (see MediaStore.Files for more info). Files can be
             * 		inserted into the MediaStore using scanFile() after which a content:// style
             * 		Uri suitable for sharing is passed to the provided onScanCompleted() callback.
             * 		Note that once added to the system MediaStore the content is accessible to any
             * 		app on the device.</li>
             * 		</ol>
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.MessagingStyle.Message setData(java.lang.String dataMimeType, android.net.Uri dataUri) {
                mDataMimeType = dataMimeType;
                mDataUri = dataUri;
                return this;
            }

            /**
             * Get the text to be used for this message, or the fallback text if a type and content
             * Uri have been set
             */
            public java.lang.CharSequence getText() {
                return mText;
            }

            /**
             * Get the time at which this message arrived
             */
            public long getTimestamp() {
                return mTimestamp;
            }

            /**
             * Get the text used to display the contact's name in the messaging experience
             */
            public java.lang.CharSequence getSender() {
                return mSender;
            }

            /**
             * Get the MIME type of the data pointed to by the Uri
             */
            public java.lang.String getDataMimeType() {
                return mDataMimeType;
            }

            /**
             * Get the the Uri pointing to the content of the message. Can be null, in which case
             * {@see #getText()} is used.
             */
            public android.net.Uri getDataUri() {
                return mDataUri;
            }

            private android.os.Bundle toBundle() {
                android.os.Bundle bundle = new android.os.Bundle();
                if (mText != null) {
                    bundle.putCharSequence(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_TEXT, mText);
                }
                bundle.putLong(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_TIMESTAMP, mTimestamp);
                if (mSender != null) {
                    bundle.putCharSequence(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_SENDER, mSender);
                }
                if (mDataMimeType != null) {
                    bundle.putString(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_DATA_MIME_TYPE, mDataMimeType);
                }
                if (mDataUri != null) {
                    bundle.putParcelable(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_DATA_URI, mDataUri);
                }
                return bundle;
            }

            static android.os.Bundle[] getBundleArrayForMessages(java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> messages) {
                android.os.Bundle[] bundles = new android.os.Bundle[messages.size()];
                final int N = messages.size();
                for (int i = 0; i < N; i++) {
                    bundles[i] = messages.get(i).toBundle();
                }
                return bundles;
            }

            static java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> getMessagesFromBundleArray(android.os.Parcelable[] bundles) {
                java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> messages = new java.util.ArrayList<>(bundles.length);
                for (int i = 0; i < bundles.length; i++) {
                    if (bundles[i] instanceof android.os.Bundle) {
                        android.support.v4.app.NotificationCompat.MessagingStyle.Message message = android.support.v4.app.NotificationCompat.MessagingStyle.Message.getMessageFromBundle(((android.os.Bundle) (bundles[i])));
                        if (message != null) {
                            messages.add(message);
                        }
                    }
                }
                return messages;
            }

            static android.support.v4.app.NotificationCompat.MessagingStyle.Message getMessageFromBundle(android.os.Bundle bundle) {
                try {
                    if ((!bundle.containsKey(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_TEXT)) || (!bundle.containsKey(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_TIMESTAMP))) {
                        return null;
                    } else {
                        android.support.v4.app.NotificationCompat.MessagingStyle.Message message = new android.support.v4.app.NotificationCompat.MessagingStyle.Message(bundle.getCharSequence(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_TEXT), bundle.getLong(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_TIMESTAMP), bundle.getCharSequence(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_SENDER));
                        if (bundle.containsKey(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_DATA_MIME_TYPE) && bundle.containsKey(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_DATA_URI)) {
                            message.setData(bundle.getString(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_DATA_MIME_TYPE), ((android.net.Uri) (bundle.getParcelable(android.support.v4.app.NotificationCompat.MessagingStyle.Message.KEY_DATA_URI))));
                        }
                        return message;
                    }
                } catch (java.lang.ClassCastException e) {
                    return null;
                }
            }
        }
    }

    /**
     * Helper class for generating large-format notifications that include a list of (up to 5) strings.
     *
     * <br>
     * If the platform does not provide large-format notifications, this method has no effect. The
     * user will always see the normal notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification noti = new Notification.Builder()
     *     .setContentTitle(&quot;5 New mails from &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_mail)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.InboxStyle()
     *         .addLine(str1)
     *         .addLine(str2)
     *         .setContentTitle(&quot;&quot;)
     *         .setSummaryText(&quot;+3 more&quot;))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class InboxStyle extends android.support.v4.app.NotificationCompat.Style {
        java.util.ArrayList<java.lang.CharSequence> mTexts = new java.util.ArrayList<java.lang.CharSequence>();

        public InboxStyle() {
        }

        public InboxStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public android.support.v4.app.NotificationCompat.InboxStyle setBigContentTitle(java.lang.CharSequence title) {
            mBigContentTitle = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public android.support.v4.app.NotificationCompat.InboxStyle setSummaryText(java.lang.CharSequence cs) {
            mSummaryText = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(cs);
            mSummaryTextSet = true;
            return this;
        }

        /**
         * Append a line to the digest section of the Inbox notification.
         */
        public android.support.v4.app.NotificationCompat.InboxStyle addLine(java.lang.CharSequence cs) {
            mTexts.add(android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(cs));
            return this;
        }
    }

    /**
     * Structure to encapsulate a named action that can be shown as part of this notification.
     * It must include an icon, a label, and a {@link PendingIntent} to be fired when the action is
     * selected by the user. Action buttons won't appear on platforms prior to Android 4.1.
     * <p>
     * Apps should use {@link NotificationCompat.Builder#addAction(int, CharSequence, PendingIntent)}
     * or {@link NotificationCompat.Builder#addAction(NotificationCompat.Action)}
     * to attach actions.
     */
    public static class Action extends android.support.v4.app.NotificationCompatBase.Action {
        final android.os.Bundle mExtras;

        private final android.support.v4.app.RemoteInput[] mRemoteInputs;

        private boolean mAllowGeneratedReplies = false;

        /**
         * Small icon representing the action.
         */
        public int icon;

        /**
         * Title of the action.
         */
        public java.lang.CharSequence title;

        /**
         * Intent to send when the user invokes this action. May be null, in which case the action
         * may be rendered in a disabled presentation.
         */
        public android.app.PendingIntent actionIntent;

        public Action(int icon, java.lang.CharSequence title, android.app.PendingIntent intent) {
            this(icon, title, intent, new android.os.Bundle(), null, false);
        }

        Action(int icon, java.lang.CharSequence title, android.app.PendingIntent intent, android.os.Bundle extras, android.support.v4.app.RemoteInput[] remoteInputs, boolean allowGeneratedReplies) {
            this.icon = icon;
            this.title = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(title);
            this.actionIntent = intent;
            this.mExtras = (extras != null) ? extras : new android.os.Bundle();
            this.mRemoteInputs = remoteInputs;
            this.mAllowGeneratedReplies = allowGeneratedReplies;
        }

        @java.lang.Override
        public int getIcon() {
            return icon;
        }

        @java.lang.Override
        public java.lang.CharSequence getTitle() {
            return title;
        }

        @java.lang.Override
        public android.app.PendingIntent getActionIntent() {
            return actionIntent;
        }

        /**
         * Get additional metadata carried around with this Action.
         */
        @java.lang.Override
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Return whether the platform should automatically generate possible replies for this
         * {@link Action}
         */
        @java.lang.Override
        public boolean getAllowGeneratedReplies() {
            return mAllowGeneratedReplies;
        }

        /**
         * Get the list of inputs to be collected from the user when this action is sent.
         * May return null if no remote inputs were added.
         */
        @java.lang.Override
        public android.support.v4.app.RemoteInput[] getRemoteInputs() {
            return mRemoteInputs;
        }

        /**
         * Builder class for {@link Action} objects.
         */
        public static final class Builder {
            private final int mIcon;

            private final java.lang.CharSequence mTitle;

            private final android.app.PendingIntent mIntent;

            private boolean mAllowGeneratedReplies;

            private final android.os.Bundle mExtras;

            private java.util.ArrayList<android.support.v4.app.RemoteInput> mRemoteInputs;

            /**
             * Construct a new builder for {@link Action} object.
             *
             * @param icon
             * 		icon to show for this action
             * @param title
             * 		the title of the action
             * @param intent
             * 		the {@link PendingIntent} to fire when users trigger this action
             */
            public Builder(int icon, java.lang.CharSequence title, android.app.PendingIntent intent) {
                this(icon, title, intent, new android.os.Bundle());
            }

            /**
             * Construct a new builder for {@link Action} object using the fields from an
             * {@link Action}.
             *
             * @param action
             * 		the action to read fields from.
             */
            public Builder(android.support.v4.app.NotificationCompat.Action action) {
                this(action.icon, action.title, action.actionIntent, new android.os.Bundle(action.mExtras));
            }

            private Builder(int icon, java.lang.CharSequence title, android.app.PendingIntent intent, android.os.Bundle extras) {
                mIcon = icon;
                mTitle = android.support.v4.app.NotificationCompat.Builder.limitCharSequenceLength(title);
                mIntent = intent;
                mExtras = extras;
            }

            /**
             * Merge additional metadata into this builder.
             *
             * <p>Values within the Bundle will replace existing extras values in this Builder.
             *
             * @see NotificationCompat.Action#getExtras
             */
            public android.support.v4.app.NotificationCompat.Action.Builder addExtras(android.os.Bundle extras) {
                if (extras != null) {
                    mExtras.putAll(extras);
                }
                return this;
            }

            /**
             * Get the metadata Bundle used by this Builder.
             *
             * <p>The returned Bundle is shared with this Builder.
             */
            public android.os.Bundle getExtras() {
                return mExtras;
            }

            /**
             * Add an input to be collected from the user when this action is sent.
             * Response values can be retrieved from the fired intent by using the
             * {@link RemoteInput#getResultsFromIntent} function.
             *
             * @param remoteInput
             * 		a {@link RemoteInput} to add to the action
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.Action.Builder addRemoteInput(android.support.v4.app.RemoteInput remoteInput) {
                if (mRemoteInputs == null) {
                    mRemoteInputs = new java.util.ArrayList<android.support.v4.app.RemoteInput>();
                }
                mRemoteInputs.add(remoteInput);
                return this;
            }

            /**
             * Set whether the platform should automatically generate possible replies to add to
             * {@link RemoteInput#getChoices()}. If the {@link Action} doesn't have a
             * {@link RemoteInput}, this has no effect.
             *
             * @param allowGeneratedReplies
             * 		{@code true} to allow generated replies, {@code false}
             * 		otherwise
             * @return this object for method chaining
            The default value is {@code false}
             */
            public android.support.v4.app.NotificationCompat.Action.Builder setAllowGeneratedReplies(boolean allowGeneratedReplies) {
                mAllowGeneratedReplies = allowGeneratedReplies;
                return this;
            }

            /**
             * Apply an extender to this action builder. Extenders may be used to add
             * metadata or change options on this builder.
             */
            public android.support.v4.app.NotificationCompat.Action.Builder extend(android.support.v4.app.NotificationCompat.Action.Extender extender) {
                extender.extend(this);
                return this;
            }

            /**
             * Combine all of the options that have been set and return a new {@link Action}
             * object.
             *
             * @return the built action
             */
            public android.support.v4.app.NotificationCompat.Action build() {
                android.support.v4.app.RemoteInput[] remoteInputs = (mRemoteInputs != null) ? mRemoteInputs.toArray(new android.support.v4.app.RemoteInput[mRemoteInputs.size()]) : null;
                return new android.support.v4.app.NotificationCompat.Action(mIcon, mTitle, mIntent, mExtras, remoteInputs, mAllowGeneratedReplies);
            }
        }

        /**
         * Extender interface for use with {@link Builder#extend}. Extenders may be used to add
         * metadata or change options on an action builder.
         */
        public interface Extender {
            /**
             * Apply this extender to a notification action builder.
             *
             * @param builder
             * 		the builder to be modified.
             * @return the build object for chaining.
             */
            public android.support.v4.app.NotificationCompat.Action.Builder extend(android.support.v4.app.NotificationCompat.Action.Builder builder);
        }

        /**
         * Wearable extender for notification actions. To add extensions to an action,
         * create a new {@link NotificationCompat.Action.WearableExtender} object using
         * the {@code WearableExtender()} constructor and apply it to a
         * {@link NotificationCompat.Action.Builder} using
         * {@link NotificationCompat.Action.Builder#extend}.
         *
         * <pre class="prettyprint">
         * NotificationCompat.Action action = new NotificationCompat.Action.Builder(
         *         R.drawable.archive_all, "Archive all", actionIntent)
         *         .extend(new NotificationCompat.Action.WearableExtender()
         *                 .setAvailableOffline(false))
         *         .build();</pre>
         */
        public static final class WearableExtender implements android.support.v4.app.NotificationCompat.Action.Extender {
            /**
             * Notification action extra which contains wearable extensions
             */
            private static final java.lang.String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";

            // Keys within EXTRA_WEARABLE_EXTENSIONS for wearable options.
            private static final java.lang.String KEY_FLAGS = "flags";

            private static final java.lang.String KEY_IN_PROGRESS_LABEL = "inProgressLabel";

            private static final java.lang.String KEY_CONFIRM_LABEL = "confirmLabel";

            private static final java.lang.String KEY_CANCEL_LABEL = "cancelLabel";

            // Flags bitwise-ored to mFlags
            private static final int FLAG_AVAILABLE_OFFLINE = 0x1;

            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 1 << 1;

            private static final int FLAG_HINT_DISPLAY_INLINE = 1 << 2;

            // Default value for flags integer
            private static final int DEFAULT_FLAGS = android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_AVAILABLE_OFFLINE;

            private int mFlags = android.support.v4.app.NotificationCompat.Action.WearableExtender.DEFAULT_FLAGS;

            private java.lang.CharSequence mInProgressLabel;

            private java.lang.CharSequence mConfirmLabel;

            private java.lang.CharSequence mCancelLabel;

            /**
             * Create a {@link NotificationCompat.Action.WearableExtender} with default
             * options.
             */
            public WearableExtender() {
            }

            /**
             * Create a {@link NotificationCompat.Action.WearableExtender} by reading
             * wearable options present in an existing notification action.
             *
             * @param action
             * 		the notification action to inspect.
             */
            public WearableExtender(android.support.v4.app.NotificationCompat.Action action) {
                android.os.Bundle wearableBundle = action.getExtras().getBundle(android.support.v4.app.NotificationCompat.Action.WearableExtender.EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    mFlags = wearableBundle.getInt(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_FLAGS, android.support.v4.app.NotificationCompat.Action.WearableExtender.DEFAULT_FLAGS);
                    mInProgressLabel = wearableBundle.getCharSequence(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_IN_PROGRESS_LABEL);
                    mConfirmLabel = wearableBundle.getCharSequence(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_CONFIRM_LABEL);
                    mCancelLabel = wearableBundle.getCharSequence(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_CANCEL_LABEL);
                }
            }

            /**
             * Apply wearable extensions to a notification action that is being built. This is
             * typically called by the {@link NotificationCompat.Action.Builder#extend}
             * method of {@link NotificationCompat.Action.Builder}.
             */
            @java.lang.Override
            public android.support.v4.app.NotificationCompat.Action.Builder extend(android.support.v4.app.NotificationCompat.Action.Builder builder) {
                android.os.Bundle wearableBundle = new android.os.Bundle();
                if (mFlags != android.support.v4.app.NotificationCompat.Action.WearableExtender.DEFAULT_FLAGS) {
                    wearableBundle.putInt(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_FLAGS, mFlags);
                }
                if (mInProgressLabel != null) {
                    wearableBundle.putCharSequence(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_IN_PROGRESS_LABEL, mInProgressLabel);
                }
                if (mConfirmLabel != null) {
                    wearableBundle.putCharSequence(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_CONFIRM_LABEL, mConfirmLabel);
                }
                if (mCancelLabel != null) {
                    wearableBundle.putCharSequence(android.support.v4.app.NotificationCompat.Action.WearableExtender.KEY_CANCEL_LABEL, mCancelLabel);
                }
                builder.getExtras().putBundle(android.support.v4.app.NotificationCompat.Action.WearableExtender.EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            @java.lang.Override
            public android.support.v4.app.NotificationCompat.Action.WearableExtender clone() {
                android.support.v4.app.NotificationCompat.Action.WearableExtender that = new android.support.v4.app.NotificationCompat.Action.WearableExtender();
                that.mFlags = this.mFlags;
                that.mInProgressLabel = this.mInProgressLabel;
                that.mConfirmLabel = this.mConfirmLabel;
                that.mCancelLabel = this.mCancelLabel;
                return that;
            }

            /**
             * Set whether this action is available when the wearable device is not connected to
             * a companion device. The user can still trigger this action when the wearable device
             * is offline, but a visual hint will indicate that the action may not be available.
             * Defaults to true.
             */
            public android.support.v4.app.NotificationCompat.Action.WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_AVAILABLE_OFFLINE, availableOffline);
                return this;
            }

            /**
             * Get whether this action is available when the wearable device is not connected to
             * a companion device. The user can still trigger this action when the wearable device
             * is offline, but a visual hint will indicate that the action may not be available.
             * Defaults to true.
             */
            public boolean isAvailableOffline() {
                return (mFlags & android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_AVAILABLE_OFFLINE) != 0;
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    mFlags |= mask;
                } else {
                    mFlags &= ~mask;
                }
            }

            /**
             * Set a label to display while the wearable is preparing to automatically execute the
             * action. This is usually a 'ing' verb ending in ellipsis like "Sending..."
             *
             * @param label
             * 		the label to display while the action is being prepared to execute
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.Action.WearableExtender setInProgressLabel(java.lang.CharSequence label) {
                mInProgressLabel = label;
                return this;
            }

            /**
             * Get the label to display while the wearable is preparing to automatically execute
             * the action. This is usually a 'ing' verb ending in ellipsis like "Sending..."
             *
             * @return the label to display while the action is being prepared to execute
             */
            public java.lang.CharSequence getInProgressLabel() {
                return mInProgressLabel;
            }

            /**
             * Set a label to display to confirm that the action should be executed.
             * This is usually an imperative verb like "Send".
             *
             * @param label
             * 		the label to confirm the action should be executed
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.Action.WearableExtender setConfirmLabel(java.lang.CharSequence label) {
                mConfirmLabel = label;
                return this;
            }

            /**
             * Get the label to display to confirm that the action should be executed.
             * This is usually an imperative verb like "Send".
             *
             * @return the label to confirm the action should be executed
             */
            public java.lang.CharSequence getConfirmLabel() {
                return mConfirmLabel;
            }

            /**
             * Set a label to display to cancel the action.
             * This is usually an imperative verb, like "Cancel".
             *
             * @param label
             * 		the label to display to cancel the action
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.Action.WearableExtender setCancelLabel(java.lang.CharSequence label) {
                mCancelLabel = label;
                return this;
            }

            /**
             * Get the label to display to cancel the action.
             * This is usually an imperative verb like "Cancel".
             *
             * @return the label to display to cancel the action
             */
            public java.lang.CharSequence getCancelLabel() {
                return mCancelLabel;
            }

            /**
             * Set a hint that this Action will launch an {@link Activity} directly, telling the
             * platform that it can generate the appropriate transitions.
             *
             * @param hintLaunchesActivity
             * 		{@code true} if the content intent will launch
             * 		an activity and transitions should be generated, false otherwise.
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.Action.WearableExtender setHintLaunchesActivity(boolean hintLaunchesActivity) {
                setFlag(android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_HINT_LAUNCHES_ACTIVITY, hintLaunchesActivity);
                return this;
            }

            /**
             * Get a hint that this Action will launch an {@link Activity} directly, telling the
             * platform that it can generate the appropriate transitions
             *
             * @return {@code true} if the content intent will launch an activity and transitions
            should be generated, false otherwise. The default value is {@code false} if this was
            never set.
             */
            public boolean getHintLaunchesActivity() {
                return (mFlags & android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_HINT_LAUNCHES_ACTIVITY) != 0;
            }

            /**
             * Set a hint that this Action should be displayed inline - i.e. it will have a visual
             * representation directly on the notification surface in addition to the expanded
             * Notification
             *
             * @param hintDisplayInline
             * 		{@code true} if action should be displayed inline, false
             * 		otherwise
             * @return this object for method chaining
             */
            public android.support.v4.app.NotificationCompat.Action.WearableExtender setHintDisplayActionInline(boolean hintDisplayInline) {
                setFlag(android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_HINT_DISPLAY_INLINE, hintDisplayInline);
                return this;
            }

            /**
             * Get a hint that this Action should be displayed inline - i.e. it should have a
             * visual representation directly on the notification surface in addition to the
             * expanded Notification
             *
             * @return {@code true} if the Action should be displayed inline, {@code false}
            otherwise. The default value is {@code false} if this was never set.
             */
            public boolean getHintDisplayActionInline() {
                return (mFlags & android.support.v4.app.NotificationCompat.Action.WearableExtender.FLAG_HINT_DISPLAY_INLINE) != 0;
            }
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public static final android.support.v4.app.NotificationCompatBase.Action.Factory FACTORY = new android.support.v4.app.NotificationCompatBase.Action.Factory() {
            @java.lang.Override
            public android.support.v4.app.NotificationCompatBase.Action build(int icon, java.lang.CharSequence title, android.app.PendingIntent actionIntent, android.os.Bundle extras, android.support.v4.app.RemoteInputCompatBase.RemoteInput[] remoteInputs, boolean allowGeneratedReplies) {
                return new android.support.v4.app.NotificationCompat.Action(icon, title, actionIntent, extras, ((android.support.v4.app.RemoteInput[]) (remoteInputs)), allowGeneratedReplies);
            }

            @java.lang.Override
            public android.support.v4.app.NotificationCompat.Action[] newArray(int length) {
                return new android.support.v4.app.NotificationCompat.Action[length];
            }
        };
    }

    /**
     * Extender interface for use with {@link Builder#extend}. Extenders may be used to add
     * metadata or change options on a notification builder.
     */
    public interface Extender {
        /**
         * Apply this extender to a notification builder.
         *
         * @param builder
         * 		the builder to be modified.
         * @return the build object for chaining.
         */
        public android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Builder builder);
    }

    /**
     * Helper class to add wearable extensions to notifications.
     * <p class="note"> See
     * <a href="{@docRoot }wear/notifications/creating.html">Creating Notifications
     * for Android Wear</a> for more information on how to use this class.
     * <p>
     * To create a notification with wearable extensions:
     * <ol>
     *   <li>Create a {@link NotificationCompat.Builder}, setting any desired
     *   properties.
     *   <li>Create a {@link NotificationCompat.WearableExtender}.
     *   <li>Set wearable-specific properties using the
     *   {@code add} and {@code set} methods of {@link NotificationCompat.WearableExtender}.
     *   <li>Call {@link NotificationCompat.Builder#extend} to apply the extensions to a
     *   notification.
     *   <li>Post the notification to the notification
     *   system with the {@code NotificationManagerCompat.notify(...)} methods
     *   and not the {@code NotificationManager.notify(...)} methods.
     * </ol>
     *
     * <pre class="prettyprint">
     * Notification notif = new NotificationCompat.Builder(mContext)
     *         .setContentTitle(&quot;New mail from &quot; + sender.toString())
     *         .setContentText(subject)
     *         .setSmallIcon(R.drawable.new_mail)
     *         .extend(new NotificationCompat.WearableExtender()
     *                 .setContentIcon(R.drawable.new_mail))
     *         .build();
     * NotificationManagerCompat.from(mContext).notify(0, notif);</pre>
     *
     * <p>Wearable extensions can be accessed on an existing notification by using the
     * {@code WearableExtender(Notification)} constructor,
     * and then using the {@code get} methods to access values.
     *
     * <pre class="prettyprint">
     * NotificationCompat.WearableExtender wearableExtender =
     *         new NotificationCompat.WearableExtender(notification);
     * List&lt;Notification&gt; pages = wearableExtender.getPages();</pre>
     */
    public static final class WearableExtender implements android.support.v4.app.NotificationCompat.Extender {
        /**
         * Sentinel value for an action index that is unset.
         */
        public static final int UNSET_ACTION_INDEX = -1;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification with
         * default sizing.
         * <p>For custom display notifications created using {@link #setDisplayIntent},
         * the default is {@link #SIZE_MEDIUM}. All other notifications size automatically based
         * on their content.
         */
        public static final int SIZE_DEFAULT = 0;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with an extra small size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_XSMALL = 1;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with a small size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_SMALL = 2;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with a medium size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_MEDIUM = 3;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with a large size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_LARGE = 4;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * full screen.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_FULL_SCREEN = 5;

        /**
         * Sentinel value for use with {@link #setHintScreenTimeout} to keep the screen on for a
         * short amount of time when this notification is displayed on the screen. This
         * is the default value.
         */
        public static final int SCREEN_TIMEOUT_SHORT = 0;

        /**
         * Sentinel value for use with {@link #setHintScreenTimeout} to keep the screen on
         * for a longer amount of time when this notification is displayed on the screen.
         */
        public static final int SCREEN_TIMEOUT_LONG = -1;

        /**
         * Notification extra which contains wearable extensions
         */
        private static final java.lang.String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";

        // Keys within EXTRA_WEARABLE_EXTENSIONS for wearable options.
        private static final java.lang.String KEY_ACTIONS = "actions";

        private static final java.lang.String KEY_FLAGS = "flags";

        private static final java.lang.String KEY_DISPLAY_INTENT = "displayIntent";

        private static final java.lang.String KEY_PAGES = "pages";

        private static final java.lang.String KEY_BACKGROUND = "background";

        private static final java.lang.String KEY_CONTENT_ICON = "contentIcon";

        private static final java.lang.String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";

        private static final java.lang.String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";

        private static final java.lang.String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";

        private static final java.lang.String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";

        private static final java.lang.String KEY_GRAVITY = "gravity";

        private static final java.lang.String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";

        private static final java.lang.String KEY_DISMISSAL_ID = "dismissalId";

        private static final java.lang.String KEY_BRIDGE_TAG = "bridgeTag";

        // Flags bitwise-ored to mFlags
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 0x1;

        private static final int FLAG_HINT_HIDE_ICON = 1 << 1;

        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 1 << 2;

        private static final int FLAG_START_SCROLL_BOTTOM = 1 << 3;

        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 1 << 4;

        private static final int FLAG_BIG_PICTURE_AMBIENT = 1 << 5;

        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 1 << 6;

        // Default value for flags integer
        private static final int DEFAULT_FLAGS = android.support.v4.app.NotificationCompat.WearableExtender.FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE;

        private static final int DEFAULT_CONTENT_ICON_GRAVITY = android.support.v4.view.GravityCompat.END;

        private static final int DEFAULT_GRAVITY = android.view.Gravity.BOTTOM;

        private java.util.ArrayList<android.support.v4.app.NotificationCompat.Action> mActions = new java.util.ArrayList<android.support.v4.app.NotificationCompat.Action>();

        private int mFlags = android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_FLAGS;

        private android.app.PendingIntent mDisplayIntent;

        private java.util.ArrayList<android.app.Notification> mPages = new java.util.ArrayList<android.app.Notification>();

        private android.graphics.Bitmap mBackground;

        private int mContentIcon;

        private int mContentIconGravity = android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_CONTENT_ICON_GRAVITY;

        private int mContentActionIndex = android.support.v4.app.NotificationCompat.WearableExtender.UNSET_ACTION_INDEX;

        private int mCustomSizePreset = android.support.v4.app.NotificationCompat.WearableExtender.SIZE_DEFAULT;

        private int mCustomContentHeight;

        private int mGravity = android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_GRAVITY;

        private int mHintScreenTimeout;

        private java.lang.String mDismissalId;

        private java.lang.String mBridgeTag;

        /**
         * Create a {@link NotificationCompat.WearableExtender} with default
         * options.
         */
        public WearableExtender() {
        }

        public WearableExtender(android.app.Notification notif) {
            android.os.Bundle extras = android.support.v4.app.NotificationCompat.getExtras(notif);
            android.os.Bundle wearableBundle = (extras != null) ? extras.getBundle(android.support.v4.app.NotificationCompat.WearableExtender.EXTRA_WEARABLE_EXTENSIONS) : null;
            if (wearableBundle != null) {
                android.support.v4.app.NotificationCompat.Action[] actions = android.support.v4.app.NotificationCompat.IMPL.getActionsFromParcelableArrayList(wearableBundle.getParcelableArrayList(android.support.v4.app.NotificationCompat.WearableExtender.KEY_ACTIONS));
                if (actions != null) {
                    java.util.Collections.addAll(mActions, actions);
                }
                mFlags = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_FLAGS, android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_FLAGS);
                mDisplayIntent = wearableBundle.getParcelable(android.support.v4.app.NotificationCompat.WearableExtender.KEY_DISPLAY_INTENT);
                android.app.Notification[] pages = android.support.v4.app.NotificationCompat.getNotificationArrayFromBundle(wearableBundle, android.support.v4.app.NotificationCompat.WearableExtender.KEY_PAGES);
                if (pages != null) {
                    java.util.Collections.addAll(mPages, pages);
                }
                mBackground = wearableBundle.getParcelable(android.support.v4.app.NotificationCompat.WearableExtender.KEY_BACKGROUND);
                mContentIcon = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CONTENT_ICON);
                mContentIconGravity = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CONTENT_ICON_GRAVITY, android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_CONTENT_ICON_GRAVITY);
                mContentActionIndex = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CONTENT_ACTION_INDEX, android.support.v4.app.NotificationCompat.WearableExtender.UNSET_ACTION_INDEX);
                mCustomSizePreset = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CUSTOM_SIZE_PRESET, android.support.v4.app.NotificationCompat.WearableExtender.SIZE_DEFAULT);
                mCustomContentHeight = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CUSTOM_CONTENT_HEIGHT);
                mGravity = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_GRAVITY, android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_GRAVITY);
                mHintScreenTimeout = wearableBundle.getInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_HINT_SCREEN_TIMEOUT);
                mDismissalId = wearableBundle.getString(android.support.v4.app.NotificationCompat.WearableExtender.KEY_DISMISSAL_ID);
                mBridgeTag = wearableBundle.getString(android.support.v4.app.NotificationCompat.WearableExtender.KEY_BRIDGE_TAG);
            }
        }

        /**
         * Apply wearable extensions to a notification that is being built. This is typically
         * called by the {@link NotificationCompat.Builder#extend} method of
         * {@link NotificationCompat.Builder}.
         */
        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Builder builder) {
            android.os.Bundle wearableBundle = new android.os.Bundle();
            if (!mActions.isEmpty()) {
                wearableBundle.putParcelableArrayList(android.support.v4.app.NotificationCompat.WearableExtender.KEY_ACTIONS, android.support.v4.app.NotificationCompat.IMPL.getParcelableArrayListForActions(mActions.toArray(new android.support.v4.app.NotificationCompat.Action[mActions.size()])));
            }
            if (mFlags != android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_FLAGS) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_FLAGS, mFlags);
            }
            if (mDisplayIntent != null) {
                wearableBundle.putParcelable(android.support.v4.app.NotificationCompat.WearableExtender.KEY_DISPLAY_INTENT, mDisplayIntent);
            }
            if (!mPages.isEmpty()) {
                wearableBundle.putParcelableArray(android.support.v4.app.NotificationCompat.WearableExtender.KEY_PAGES, mPages.toArray(new android.app.Notification[mPages.size()]));
            }
            if (mBackground != null) {
                wearableBundle.putParcelable(android.support.v4.app.NotificationCompat.WearableExtender.KEY_BACKGROUND, mBackground);
            }
            if (mContentIcon != 0) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CONTENT_ICON, mContentIcon);
            }
            if (mContentIconGravity != android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_CONTENT_ICON_GRAVITY) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CONTENT_ICON_GRAVITY, mContentIconGravity);
            }
            if (mContentActionIndex != android.support.v4.app.NotificationCompat.WearableExtender.UNSET_ACTION_INDEX) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CONTENT_ACTION_INDEX, mContentActionIndex);
            }
            if (mCustomSizePreset != android.support.v4.app.NotificationCompat.WearableExtender.SIZE_DEFAULT) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CUSTOM_SIZE_PRESET, mCustomSizePreset);
            }
            if (mCustomContentHeight != 0) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_CUSTOM_CONTENT_HEIGHT, mCustomContentHeight);
            }
            if (mGravity != android.support.v4.app.NotificationCompat.WearableExtender.DEFAULT_GRAVITY) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_GRAVITY, mGravity);
            }
            if (mHintScreenTimeout != 0) {
                wearableBundle.putInt(android.support.v4.app.NotificationCompat.WearableExtender.KEY_HINT_SCREEN_TIMEOUT, mHintScreenTimeout);
            }
            if (mDismissalId != null) {
                wearableBundle.putString(android.support.v4.app.NotificationCompat.WearableExtender.KEY_DISMISSAL_ID, mDismissalId);
            }
            if (mBridgeTag != null) {
                wearableBundle.putString(android.support.v4.app.NotificationCompat.WearableExtender.KEY_BRIDGE_TAG, mBridgeTag);
            }
            builder.getExtras().putBundle(android.support.v4.app.NotificationCompat.WearableExtender.EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        @java.lang.Override
        public android.support.v4.app.NotificationCompat.WearableExtender clone() {
            android.support.v4.app.NotificationCompat.WearableExtender that = new android.support.v4.app.NotificationCompat.WearableExtender();
            that.mActions = new java.util.ArrayList<android.support.v4.app.NotificationCompat.Action>(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new java.util.ArrayList<android.app.Notification>(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            that.mHintScreenTimeout = this.mHintScreenTimeout;
            that.mDismissalId = this.mDismissalId;
            that.mBridgeTag = this.mBridgeTag;
            return that;
        }

        /**
         * Add a wearable action to this notification.
         *
         * <p>When wearable actions are added using this method, the set of actions that
         * show on a wearable device splits from devices that only show actions added
         * using {@link NotificationCompat.Builder#addAction}. This allows for customization
         * of which actions display on different devices.
         *
         * @param action
         * 		the action to add to this notification
         * @return this object for method chaining
         * @see NotificationCompat.Action
         */
        public android.support.v4.app.NotificationCompat.WearableExtender addAction(android.support.v4.app.NotificationCompat.Action action) {
            mActions.add(action);
            return this;
        }

        /**
         * Adds wearable actions to this notification.
         *
         * <p>When wearable actions are added using this method, the set of actions that
         * show on a wearable device splits from devices that only show actions added
         * using {@link NotificationCompat.Builder#addAction}. This allows for customization
         * of which actions display on different devices.
         *
         * @param actions
         * 		the actions to add to this notification
         * @return this object for method chaining
         * @see NotificationCompat.Action
         */
        public android.support.v4.app.NotificationCompat.WearableExtender addActions(java.util.List<android.support.v4.app.NotificationCompat.Action> actions) {
            mActions.addAll(actions);
            return this;
        }

        /**
         * Clear all wearable actions present on this builder.
         *
         * @return this object for method chaining.
         * @see #addAction
         */
        public android.support.v4.app.NotificationCompat.WearableExtender clearActions() {
            mActions.clear();
            return this;
        }

        /**
         * Get the wearable actions present on this notification.
         */
        public java.util.List<android.support.v4.app.NotificationCompat.Action> getActions() {
            return mActions;
        }

        /**
         * Set an intent to launch inside of an activity view when displaying
         * this notification. The {@link PendingIntent} provided should be for an activity.
         *
         * <pre class="prettyprint">
         * Intent displayIntent = new Intent(context, MyDisplayActivity.class);
         * PendingIntent displayPendingIntent = PendingIntent.getActivity(context,
         *         0, displayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         * Notification notif = new NotificationCompat.Builder(context)
         *         .extend(new NotificationCompat.WearableExtender()
         *                 .setDisplayIntent(displayPendingIntent)
         *                 .setCustomSizePreset(NotificationCompat.WearableExtender.SIZE_MEDIUM))
         *         .build();</pre>
         *
         * <p>The activity to launch needs to allow embedding, must be exported, and
         * should have an empty task affinity. It is also recommended to use the device
         * default light theme.
         *
         * <p>Example AndroidManifest.xml entry:
         * <pre class="prettyprint">
         * &lt;activity android:name=&quot;com.example.MyDisplayActivity&quot;
         *     android:exported=&quot;true&quot;
         *     android:allowEmbedded=&quot;true&quot;
         *     android:taskAffinity=&quot;&quot;
         *     android:theme=&quot;@android:style/Theme.DeviceDefault.Light&quot; /&gt;</pre>
         *
         * @param intent
         * 		the {@link PendingIntent} for an activity
         * @return this object for method chaining
         * @see NotificationCompat.WearableExtender#getDisplayIntent
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setDisplayIntent(android.app.PendingIntent intent) {
            mDisplayIntent = intent;
            return this;
        }

        /**
         * Get the intent to launch inside of an activity view when displaying this
         * notification. This {@code PendingIntent} should be for an activity.
         */
        public android.app.PendingIntent getDisplayIntent() {
            return mDisplayIntent;
        }

        /**
         * Add an additional page of content to display with this notification. The current
         * notification forms the first page, and pages added using this function form
         * subsequent pages. This field can be used to separate a notification into multiple
         * sections.
         *
         * @param page
         * 		the notification to add as another page
         * @return this object for method chaining
         * @see NotificationCompat.WearableExtender#getPages
         */
        public android.support.v4.app.NotificationCompat.WearableExtender addPage(android.app.Notification page) {
            mPages.add(page);
            return this;
        }

        /**
         * Add additional pages of content to display with this notification. The current
         * notification forms the first page, and pages added using this function form
         * subsequent pages. This field can be used to separate a notification into multiple
         * sections.
         *
         * @param pages
         * 		a list of notifications
         * @return this object for method chaining
         * @see NotificationCompat.WearableExtender#getPages
         */
        public android.support.v4.app.NotificationCompat.WearableExtender addPages(java.util.List<android.app.Notification> pages) {
            mPages.addAll(pages);
            return this;
        }

        /**
         * Clear all additional pages present on this builder.
         *
         * @return this object for method chaining.
         * @see #addPage
         */
        public android.support.v4.app.NotificationCompat.WearableExtender clearPages() {
            mPages.clear();
            return this;
        }

        /**
         * Get the array of additional pages of content for displaying this notification. The
         * current notification forms the first page, and elements within this array form
         * subsequent pages. This field can be used to separate a notification into multiple
         * sections.
         *
         * @return the pages for this notification
         */
        public java.util.List<android.app.Notification> getPages() {
            return mPages;
        }

        /**
         * Set a background image to be displayed behind the notification content.
         * Contrary to the {@link NotificationCompat.BigPictureStyle}, this background
         * will work with any notification style.
         *
         * @param background
         * 		the background bitmap
         * @return this object for method chaining
         * @see NotificationCompat.WearableExtender#getBackground
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setBackground(android.graphics.Bitmap background) {
            mBackground = background;
            return this;
        }

        /**
         * Get a background image to be displayed behind the notification content.
         * Contrary to the {@link NotificationCompat.BigPictureStyle}, this background
         * will work with any notification style.
         *
         * @return the background image
         * @see NotificationCompat.WearableExtender#setBackground
         */
        public android.graphics.Bitmap getBackground() {
            return mBackground;
        }

        /**
         * Set an icon that goes with the content of this notification.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setContentIcon(int icon) {
            mContentIcon = icon;
            return this;
        }

        /**
         * Get an icon that goes with the content of this notification.
         */
        public int getContentIcon() {
            return mContentIcon;
        }

        /**
         * Set the gravity that the content icon should have within the notification display.
         * Supported values include {@link android.view.Gravity#START} and
         * {@link android.view.Gravity#END}. The default value is {@link android.view.Gravity#END}.
         *
         * @see #setContentIcon
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setContentIconGravity(int contentIconGravity) {
            mContentIconGravity = contentIconGravity;
            return this;
        }

        /**
         * Get the gravity that the content icon should have within the notification display.
         * Supported values include {@link android.view.Gravity#START} and
         * {@link android.view.Gravity#END}. The default value is {@link android.view.Gravity#END}.
         *
         * @see #getContentIcon
         */
        public int getContentIconGravity() {
            return mContentIconGravity;
        }

        /**
         * Set an action from this notification's actions to be clickable with the content of
         * this notification. This action will no longer display separately from the
         * notification's content.
         *
         * <p>For notifications with multiple pages, child pages can also have content actions
         * set, although the list of available actions comes from the main notification and not
         * from the child page's notification.
         *
         * @param actionIndex
         * 		The index of the action to hoist onto the current notification page.
         * 		If wearable actions were added to the main notification, this index
         * 		will apply to that list, otherwise it will apply to the regular
         * 		actions list.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setContentAction(int actionIndex) {
            mContentActionIndex = actionIndex;
            return this;
        }

        /**
         * Get the index of the notification action, if any, that was specified as being clickable
         * with the content of this notification. This action will no longer display separately
         * from the notification's content.
         *
         * <p>For notifications with multiple pages, child pages can also have content actions
         * set, although the list of available actions comes from the main notification and not
         * from the child page's notification.
         *
         * <p>If wearable specific actions were added to the main notification, this index will
         * apply to that list, otherwise it will apply to the regular actions list.
         *
         * @return the action index or {@link #UNSET_ACTION_INDEX} if no action was selected.
         */
        public int getContentAction() {
            return mContentActionIndex;
        }

        /**
         * Set the gravity that this notification should have within the available viewport space.
         * Supported values include {@link android.view.Gravity#TOP},
         * {@link android.view.Gravity#CENTER_VERTICAL} and {@link android.view.Gravity#BOTTOM}.
         * The default value is {@link android.view.Gravity#BOTTOM}.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setGravity(int gravity) {
            mGravity = gravity;
            return this;
        }

        /**
         * Get the gravity that this notification should have within the available viewport space.
         * Supported values include {@link android.view.Gravity#TOP},
         * {@link android.view.Gravity#CENTER_VERTICAL} and {@link android.view.Gravity#BOTTOM}.
         * The default value is {@link android.view.Gravity#BOTTOM}.
         */
        public int getGravity() {
            return mGravity;
        }

        /**
         * Set the custom size preset for the display of this notification out of the available
         * presets found in {@link NotificationCompat.WearableExtender}, e.g.
         * {@link #SIZE_LARGE}.
         * <p>Some custom size presets are only applicable for custom display notifications created
         * using {@link NotificationCompat.WearableExtender#setDisplayIntent}. Check the
         * documentation for the preset in question. See also
         * {@link #setCustomContentHeight} and {@link #getCustomSizePreset}.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setCustomSizePreset(int sizePreset) {
            mCustomSizePreset = sizePreset;
            return this;
        }

        /**
         * Get the custom size preset for the display of this notification out of the available
         * presets found in {@link NotificationCompat.WearableExtender}, e.g.
         * {@link #SIZE_LARGE}.
         * <p>Some custom size presets are only applicable for custom display notifications created
         * using {@link #setDisplayIntent}. Check the documentation for the preset in question.
         * See also {@link #setCustomContentHeight} and {@link #setCustomSizePreset}.
         */
        public int getCustomSizePreset() {
            return mCustomSizePreset;
        }

        /**
         * Set the custom height in pixels for the display of this notification's content.
         * <p>This option is only available for custom display notifications created
         * using {@link NotificationCompat.WearableExtender#setDisplayIntent}. See also
         * {@link NotificationCompat.WearableExtender#setCustomSizePreset} and
         * {@link #getCustomContentHeight}.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setCustomContentHeight(int height) {
            mCustomContentHeight = height;
            return this;
        }

        /**
         * Get the custom height in pixels for the display of this notification's content.
         * <p>This option is only available for custom display notifications created
         * using {@link #setDisplayIntent}. See also {@link #setCustomSizePreset} and
         * {@link #setCustomContentHeight}.
         */
        public int getCustomContentHeight() {
            return mCustomContentHeight;
        }

        /**
         * Set whether the scrolling position for the contents of this notification should start
         * at the bottom of the contents instead of the top when the contents are too long to
         * display within the screen.  Default is false (start scroll at the top).
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_START_SCROLL_BOTTOM, startScrollBottom);
            return this;
        }

        /**
         * Get whether the scrolling position for the contents of this notification should start
         * at the bottom of the contents instead of the top when the contents are too long to
         * display within the screen. Default is false (start scroll at the top).
         */
        public boolean getStartScrollBottom() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_START_SCROLL_BOTTOM) != 0;
        }

        /**
         * Set whether the content intent is available when the wearable device is not connected
         * to a companion device.  The user can still trigger this intent when the wearable device
         * is offline, but a visual hint will indicate that the content intent may not be available.
         * Defaults to true.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE, contentIntentAvailableOffline);
            return this;
        }

        /**
         * Get whether the content intent is available when the wearable device is not connected
         * to a companion device.  The user can still trigger this intent when the wearable device
         * is offline, but a visual hint will indicate that the content intent may not be available.
         * Defaults to true.
         */
        public boolean getContentIntentAvailableOffline() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE) != 0;
        }

        /**
         * Set a hint that this notification's icon should not be displayed.
         *
         * @param hintHideIcon
         * 		{@code true} to hide the icon, {@code false} otherwise.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_HIDE_ICON, hintHideIcon);
            return this;
        }

        /**
         * Get a hint that this notification's icon should not be displayed.
         *
         * @return {@code true} if this icon should not be displayed, false otherwise.
        The default value is {@code false} if this was never set.
         */
        public boolean getHintHideIcon() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_HIDE_ICON) != 0;
        }

        /**
         * Set a visual hint that only the background image of this notification should be
         * displayed, and other semantic content should be hidden. This hint is only applicable
         * to sub-pages added using {@link #addPage}.
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_SHOW_BACKGROUND_ONLY, hintShowBackgroundOnly);
            return this;
        }

        /**
         * Get a visual hint that only the background image of this notification should be
         * displayed, and other semantic content should be hidden. This hint is only applicable
         * to sub-pages added using {@link NotificationCompat.WearableExtender#addPage}.
         */
        public boolean getHintShowBackgroundOnly() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_SHOW_BACKGROUND_ONLY) != 0;
        }

        /**
         * Set a hint that this notification's background should not be clipped if possible,
         * and should instead be resized to fully display on the screen, retaining the aspect
         * ratio of the image. This can be useful for images like barcodes or qr codes.
         *
         * @param hintAvoidBackgroundClipping
         * 		{@code true} to avoid clipping if possible.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setHintAvoidBackgroundClipping(boolean hintAvoidBackgroundClipping) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_AVOID_BACKGROUND_CLIPPING, hintAvoidBackgroundClipping);
            return this;
        }

        /**
         * Get a hint that this notification's background should not be clipped if possible,
         * and should instead be resized to fully display on the screen, retaining the aspect
         * ratio of the image. This can be useful for images like barcodes or qr codes.
         *
         * @return {@code true} if it's ok if the background is clipped on the screen, false
        otherwise. The default value is {@code false} if this was never set.
         */
        public boolean getHintAvoidBackgroundClipping() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_AVOID_BACKGROUND_CLIPPING) != 0;
        }

        /**
         * Set a hint that the screen should remain on for at least this duration when
         * this notification is displayed on the screen.
         *
         * @param timeout
         * 		The requested screen timeout in milliseconds. Can also be either
         * 		{@link #SCREEN_TIMEOUT_SHORT} or {@link #SCREEN_TIMEOUT_LONG}.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setHintScreenTimeout(int timeout) {
            mHintScreenTimeout = timeout;
            return this;
        }

        /**
         * Get the duration, in milliseconds, that the screen should remain on for
         * when this notification is displayed.
         *
         * @return the duration in milliseconds if > 0, or either one of the sentinel values
        {@link #SCREEN_TIMEOUT_SHORT} or {@link #SCREEN_TIMEOUT_LONG}.
         */
        public int getHintScreenTimeout() {
            return mHintScreenTimeout;
        }

        /**
         * Set a hint that this notification's {@link BigPictureStyle} (if present) should be
         * converted to low-bit and displayed in ambient mode, especially useful for barcodes and
         * qr codes, as well as other simple black-and-white tickets.
         *
         * @param hintAmbientBigPicture
         * 		{@code true} to enable converstion and ambient.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setHintAmbientBigPicture(boolean hintAmbientBigPicture) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_BIG_PICTURE_AMBIENT, hintAmbientBigPicture);
            return this;
        }

        /**
         * Get a hint that this notification's {@link BigPictureStyle} (if present) should be
         * converted to low-bit and displayed in ambient mode, especially useful for barcodes and
         * qr codes, as well as other simple black-and-white tickets.
         *
         * @return {@code true} if it should be displayed in ambient, false otherwise
        otherwise. The default value is {@code false} if this was never set.
         */
        public boolean getHintAmbientBigPicture() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_BIG_PICTURE_AMBIENT) != 0;
        }

        /**
         * Set a hint that this notification's content intent will launch an {@link Activity}
         * directly, telling the platform that it can generate the appropriate transitions.
         *
         * @param hintContentIntentLaunchesActivity
         * 		{@code true} if the content intent will launch
         * 		an activity and transitions should be generated, false otherwise.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setHintContentIntentLaunchesActivity(boolean hintContentIntentLaunchesActivity) {
            setFlag(android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY, hintContentIntentLaunchesActivity);
            return this;
        }

        /**
         * Get a hint that this notification's content intent will launch an {@link Activity}
         * directly, telling the platform that it can generate the appropriate transitions
         *
         * @return {@code true} if the content intent will launch an activity and transitions should
        be generated, false otherwise. The default value is {@code false} if this was never set.
         */
        public boolean getHintContentIntentLaunchesActivity() {
            return (mFlags & android.support.v4.app.NotificationCompat.WearableExtender.FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY) != 0;
        }

        /**
         * Sets the dismissal id for this notification. If a notification is posted with a
         * dismissal id, then when that notification is canceled, notifications on other wearables
         * and the paired Android phone having that same dismissal id will also be canceled. See
         * <a href="{@docRoot }wear/notifications/index.html">Adding Wearable Features to
         * Notifications</a> for more information.
         *
         * @param dismissalId
         * 		the dismissal id of the notification.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setDismissalId(java.lang.String dismissalId) {
            mDismissalId = dismissalId;
            return this;
        }

        /**
         * Returns the dismissal id of the notification.
         *
         * @return the dismissal id of the notification or null if it has not been set.
         */
        public java.lang.String getDismissalId() {
            return mDismissalId;
        }

        /**
         * Sets a bridge tag for this notification. A bridge tag can be set for notifications
         * posted from a phone to provide finer-grained control on what notifications are bridged
         * to wearables. See <a href="{@docRoot }wear/notifications/index.html">Adding Wearable
         * Features to Notifications</a> for more information.
         *
         * @param bridgeTag
         * 		the bridge tag of the notification.
         * @return this object for method chaining
         */
        public android.support.v4.app.NotificationCompat.WearableExtender setBridgeTag(java.lang.String bridgeTag) {
            mBridgeTag = bridgeTag;
            return this;
        }

        /**
         * Returns the bridge tag of the notification.
         *
         * @return the bridge tag or null if not present.
         */
        public java.lang.String getBridgeTag() {
            return mBridgeTag;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                mFlags |= mask;
            } else {
                mFlags &= ~mask;
            }
        }
    }

    /**
     * <p>Helper class to add Android Auto extensions to notifications. To create a notification
     * with car extensions:
     *
     * <ol>
     *  <li>Create an {@link NotificationCompat.Builder}, setting any desired
     *  properties.
     *  <li>Create a {@link CarExtender}.
     *  <li>Set car-specific properties using the {@code add} and {@code set} methods of
     *  {@link CarExtender}.
     *  <li>Call {@link android.support.v4.app.NotificationCompat.Builder#extend(NotificationCompat.Extender)}
     *  to apply the extensions to a notification.
     *  <li>Post the notification to the notification system with the
     *  {@code NotificationManagerCompat.notify(...)} methods and not the
     *  {@code NotificationManager.notify(...)} methods.
     * </ol>
     *
     * <pre class="prettyprint">
     * Notification notification = new NotificationCompat.Builder(context)
     *         ...
     *         .extend(new CarExtender()
     *                 .set*(...))
     *         .build();
     * </pre>
     *
     * <p>Car extensions can be accessed on an existing notification by using the
     * {@code CarExtender(Notification)} constructor, and then using the {@code get} methods
     * to access values.
     */
    public static final class CarExtender implements android.support.v4.app.NotificationCompat.Extender {
        private static final java.lang.String TAG = "CarExtender";

        private static final java.lang.String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";

        private static final java.lang.String EXTRA_LARGE_ICON = "large_icon";

        private static final java.lang.String EXTRA_CONVERSATION = "car_conversation";

        private static final java.lang.String EXTRA_COLOR = "app_color";

        private android.graphics.Bitmap mLargeIcon;

        private android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation mUnreadConversation;

        private int mColor = android.support.v4.app.NotificationCompat.COLOR_DEFAULT;

        /**
         * Create a {@link CarExtender} with default options.
         */
        public CarExtender() {
        }

        /**
         * Create a {@link CarExtender} from the CarExtender options of an existing Notification.
         *
         * @param notif
         * 		The notification from which to copy options.
         */
        public CarExtender(android.app.Notification notif) {
            if (android.os.Build.VERSION.SDK_INT < 21) {
                return;
            }
            android.os.Bundle carBundle = (android.support.v4.app.NotificationCompat.getExtras(notif) == null) ? null : android.support.v4.app.NotificationCompat.getExtras(notif).getBundle(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_CAR_EXTENDER);
            if (carBundle != null) {
                mLargeIcon = carBundle.getParcelable(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_LARGE_ICON);
                mColor = carBundle.getInt(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_COLOR, android.support.v4.app.NotificationCompat.COLOR_DEFAULT);
                android.os.Bundle b = carBundle.getBundle(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_CONVERSATION);
                mUnreadConversation = ((android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation) (android.support.v4.app.NotificationCompat.IMPL.getUnreadConversationFromBundle(b, android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation.FACTORY, android.support.v4.app.RemoteInput.FACTORY)));
            }
        }

        /**
         * Apply car extensions to a notification that is being built. This is typically called by
         * the {@link android.support.v4.app.NotificationCompat.Builder#extend(NotificationCompat.Extender)}
         * method of {@link NotificationCompat.Builder}.
         */
        @java.lang.Override
        public android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Builder builder) {
            if (android.os.Build.VERSION.SDK_INT < 21) {
                return builder;
            }
            android.os.Bundle carExtensions = new android.os.Bundle();
            if (mLargeIcon != null) {
                carExtensions.putParcelable(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_LARGE_ICON, mLargeIcon);
            }
            if (mColor != android.support.v4.app.NotificationCompat.COLOR_DEFAULT) {
                carExtensions.putInt(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_COLOR, mColor);
            }
            if (mUnreadConversation != null) {
                android.os.Bundle b = android.support.v4.app.NotificationCompat.IMPL.getBundleForUnreadConversation(mUnreadConversation);
                carExtensions.putBundle(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_CONVERSATION, b);
            }
            builder.getExtras().putBundle(android.support.v4.app.NotificationCompat.CarExtender.EXTRA_CAR_EXTENDER, carExtensions);
            return builder;
        }

        /**
         * Sets the accent color to use when Android Auto presents the notification.
         *
         * Android Auto uses the color set with {@link android.support.v4.app.NotificationCompat.Builder#setColor(int)}
         * to accent the displayed notification. However, not all colors are acceptable in an
         * automotive setting. This method can be used to override the color provided in the
         * notification in such a situation.
         */
        public android.support.v4.app.NotificationCompat.CarExtender setColor(@android.support.annotation.ColorInt
        int color) {
            mColor = color;
            return this;
        }

        /**
         * Gets the accent color.
         *
         * @see #setColor
         */
        @android.support.annotation.ColorInt
        public int getColor() {
            return mColor;
        }

        /**
         * Sets the large icon of the car notification.
         *
         * If no large icon is set in the extender, Android Auto will display the icon
         * specified by {@link android.support.v4.app.NotificationCompat.Builder#setLargeIcon(android.graphics.Bitmap)}
         *
         * @param largeIcon
         * 		The large icon to use in the car notification.
         * @return This object for method chaining.
         */
        public android.support.v4.app.NotificationCompat.CarExtender setLargeIcon(android.graphics.Bitmap largeIcon) {
            mLargeIcon = largeIcon;
            return this;
        }

        /**
         * Gets the large icon used in this car notification, or null if no icon has been set.
         *
         * @return The large icon for the car notification.
         * @see CarExtender#setLargeIcon
         */
        public android.graphics.Bitmap getLargeIcon() {
            return mLargeIcon;
        }

        /**
         * Sets the unread conversation in a message notification.
         *
         * @param unreadConversation
         * 		The unread part of the conversation this notification conveys.
         * @return This object for method chaining.
         */
        public android.support.v4.app.NotificationCompat.CarExtender setUnreadConversation(android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation unreadConversation) {
            mUnreadConversation = unreadConversation;
            return this;
        }

        /**
         * Returns the unread conversation conveyed by this notification.
         *
         * @see #setUnreadConversation(UnreadConversation)
         */
        public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation getUnreadConversation() {
            return mUnreadConversation;
        }

        /**
         * A class which holds the unread messages from a conversation.
         */
        public static class UnreadConversation extends android.support.v4.app.NotificationCompatBase.UnreadConversation {
            private final java.lang.String[] mMessages;

            private final android.support.v4.app.RemoteInput mRemoteInput;

            private final android.app.PendingIntent mReplyPendingIntent;

            private final android.app.PendingIntent mReadPendingIntent;

            private final java.lang.String[] mParticipants;

            private final long mLatestTimestamp;

            UnreadConversation(java.lang.String[] messages, android.support.v4.app.RemoteInput remoteInput, android.app.PendingIntent replyPendingIntent, android.app.PendingIntent readPendingIntent, java.lang.String[] participants, long latestTimestamp) {
                mMessages = messages;
                mRemoteInput = remoteInput;
                mReadPendingIntent = readPendingIntent;
                mReplyPendingIntent = replyPendingIntent;
                mParticipants = participants;
                mLatestTimestamp = latestTimestamp;
            }

            /**
             * Gets the list of messages conveyed by this notification.
             */
            @java.lang.Override
            public java.lang.String[] getMessages() {
                return mMessages;
            }

            /**
             * Gets the remote input that will be used to convey the response to a message list, or
             * null if no such remote input exists.
             */
            @java.lang.Override
            public android.support.v4.app.RemoteInput getRemoteInput() {
                return mRemoteInput;
            }

            /**
             * Gets the pending intent that will be triggered when the user replies to this
             * notification.
             */
            @java.lang.Override
            public android.app.PendingIntent getReplyPendingIntent() {
                return mReplyPendingIntent;
            }

            /**
             * Gets the pending intent that Android Auto will send after it reads aloud all messages
             * in this object's message list.
             */
            @java.lang.Override
            public android.app.PendingIntent getReadPendingIntent() {
                return mReadPendingIntent;
            }

            /**
             * Gets the participants in the conversation.
             */
            @java.lang.Override
            public java.lang.String[] getParticipants() {
                return mParticipants;
            }

            /**
             * Gets the firs participant in the conversation.
             */
            @java.lang.Override
            public java.lang.String getParticipant() {
                return mParticipants.length > 0 ? mParticipants[0] : null;
            }

            /**
             * Gets the timestamp of the conversation.
             */
            @java.lang.Override
            public long getLatestTimestamp() {
                return mLatestTimestamp;
            }

            static final android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory FACTORY = new android.support.v4.app.NotificationCompatBase.UnreadConversation.Factory() {
                @java.lang.Override
                public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation build(java.lang.String[] messages, android.support.v4.app.RemoteInputCompatBase.RemoteInput remoteInput, android.app.PendingIntent replyPendingIntent, android.app.PendingIntent readPendingIntent, java.lang.String[] participants, long latestTimestamp) {
                    return new android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation(messages, ((android.support.v4.app.RemoteInput) (remoteInput)), replyPendingIntent, readPendingIntent, participants, latestTimestamp);
                }
            };

            /**
             * Builder class for {@link CarExtender.UnreadConversation} objects.
             */
            public static class Builder {
                private final java.util.List<java.lang.String> mMessages = new java.util.ArrayList<java.lang.String>();

                private final java.lang.String mParticipant;

                private android.support.v4.app.RemoteInput mRemoteInput;

                private android.app.PendingIntent mReadPendingIntent;

                private android.app.PendingIntent mReplyPendingIntent;

                private long mLatestTimestamp;

                /**
                 * Constructs a new builder for {@link CarExtender.UnreadConversation}.
                 *
                 * @param name
                 * 		The name of the other participant in the conversation.
                 */
                public Builder(java.lang.String name) {
                    mParticipant = name;
                }

                /**
                 * Appends a new unread message to the list of messages for this conversation.
                 *
                 * The messages should be added from oldest to newest.
                 *
                 * @param message
                 * 		The text of the new unread message.
                 * @return This object for method chaining.
                 */
                public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation.Builder addMessage(java.lang.String message) {
                    mMessages.add(message);
                    return this;
                }

                /**
                 * Sets the pending intent and remote input which will convey the reply to this
                 * notification.
                 *
                 * @param pendingIntent
                 * 		The pending intent which will be triggered on a reply.
                 * @param remoteInput
                 * 		The remote input parcelable which will carry the reply.
                 * @return This object for method chaining.
                 * @see CarExtender.UnreadConversation#getRemoteInput
                 * @see CarExtender.UnreadConversation#getReplyPendingIntent
                 */
                public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation.Builder setReplyAction(android.app.PendingIntent pendingIntent, android.support.v4.app.RemoteInput remoteInput) {
                    mRemoteInput = remoteInput;
                    mReplyPendingIntent = pendingIntent;
                    return this;
                }

                /**
                 * Sets the pending intent that will be sent once the messages in this notification
                 * are read.
                 *
                 * @param pendingIntent
                 * 		The pending intent to use.
                 * @return This object for method chaining.
                 */
                public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation.Builder setReadPendingIntent(android.app.PendingIntent pendingIntent) {
                    mReadPendingIntent = pendingIntent;
                    return this;
                }

                /**
                 * Sets the timestamp of the most recent message in an unread conversation.
                 *
                 * If a messaging notification has been posted by your application and has not
                 * yet been cancelled, posting a later notification with the same id and tag
                 * but without a newer timestamp may result in Android Auto not displaying a
                 * heads up notification for the later notification.
                 *
                 * @param timestamp
                 * 		The timestamp of the most recent message in the conversation.
                 * @return This object for method chaining.
                 */
                public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation.Builder setLatestTimestamp(long timestamp) {
                    mLatestTimestamp = timestamp;
                    return this;
                }

                /**
                 * Builds a new unread conversation object.
                 *
                 * @return The new unread conversation object.
                 */
                public android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation build() {
                    java.lang.String[] messages = mMessages.toArray(new java.lang.String[mMessages.size()]);
                    java.lang.String[] participants = new java.lang.String[]{ mParticipant };
                    return new android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation(messages, mRemoteInput, mReplyPendingIntent, mReadPendingIntent, participants, mLatestTimestamp);
                }
            }
        }
    }

    /**
     * Get an array of Notification objects from a parcelable array bundle field.
     * Update the bundle to have a typed array so fetches in the future don't need
     * to do an array copy.
     */
    static android.app.Notification[] getNotificationArrayFromBundle(android.os.Bundle bundle, java.lang.String key) {
        android.os.Parcelable[] array = bundle.getParcelableArray(key);
        if ((array instanceof android.app.Notification[]) || (array == null)) {
            return ((android.app.Notification[]) (array));
        }
        android.app.Notification[] typedArray = new android.app.Notification[array.length];
        for (int i = 0; i < array.length; i++) {
            typedArray[i] = ((android.app.Notification) (array[i]));
        }
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }

    /**
     * Gets the {@link Notification#extras} field from a notification in a backwards
     * compatible manner. Extras field was supported from JellyBean (Api level 16)
     * forwards. This function will return null on older api levels.
     */
    public static android.os.Bundle getExtras(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.getExtras(notif);
    }

    /**
     * Get the number of actions in this notification in a backwards compatible
     * manner. Actions were supported from JellyBean (Api level 16) forwards.
     */
    public static int getActionCount(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.getActionCount(notif);
    }

    /**
     * Get an action on this notification in a backwards compatible
     * manner. Actions were supported from JellyBean (Api level 16) forwards.
     *
     * @param notif
     * 		The notification to inspect.
     * @param actionIndex
     * 		The index of the action to retrieve.
     */
    public static android.support.v4.app.NotificationCompat.Action getAction(android.app.Notification notif, int actionIndex) {
        return android.support.v4.app.NotificationCompat.IMPL.getAction(notif, actionIndex);
    }

    /**
     * Get the category of this notification in a backwards compatible
     * manner.
     *
     * @param notif
     * 		The notification to inspect.
     */
    public static java.lang.String getCategory(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.getCategory(notif);
    }

    /**
     * Get whether or not this notification is only relevant to the current device.
     *
     * <p>Some notifications can be bridged to other devices for remote display.
     * If this hint is set, it is recommend that this notification not be bridged.
     */
    public static boolean getLocalOnly(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.getLocalOnly(notif);
    }

    /**
     * Get the key used to group this notification into a cluster or stack
     * with other notifications on devices which support such rendering.
     */
    public static java.lang.String getGroup(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.getGroup(notif);
    }

    /**
     * Get whether this notification to be the group summary for a group of notifications.
     * Grouped notifications may display in a cluster or stack on devices which
     * support such rendering. Requires a group key also be set using {@link Builder#setGroup}.
     *
     * @return Whether this notification is a group summary.
     */
    public static boolean isGroupSummary(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.isGroupSummary(notif);
    }

    /**
     * Get a sort key that orders this notification among other notifications from the
     * same package. This can be useful if an external sort was already applied and an app
     * would like to preserve this. Notifications will be sorted lexicographically using this
     * value, although providing different priorities in addition to providing sort key may
     * cause this value to be ignored.
     *
     * <p>This sort key can also be used to order members of a notification group. See
     * {@link Builder#setGroup}.
     *
     * @see String#compareTo(String)
     */
    public static java.lang.String getSortKey(android.app.Notification notif) {
        return android.support.v4.app.NotificationCompat.IMPL.getSortKey(notif);
    }
}

