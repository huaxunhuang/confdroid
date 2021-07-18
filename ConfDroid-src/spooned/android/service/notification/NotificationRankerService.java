/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * A service that helps the user manage notifications. This class is only used to
 * extend the framework service and may not be implemented by non-framework components.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public abstract class NotificationRankerService extends android.service.notification.NotificationListenerService {
    private static final java.lang.String TAG = "NotificationRankers";

    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.service.notification.NotificationRankerService";

    /**
     * Notification was canceled by the status bar reporting a click.
     */
    public static final int REASON_DELEGATE_CLICK = 1;

    /**
     * Notification was canceled by the status bar reporting a user dismissal.
     */
    public static final int REASON_DELEGATE_CANCEL = 2;

    /**
     * Notification was canceled by the status bar reporting a user dismiss all.
     */
    public static final int REASON_DELEGATE_CANCEL_ALL = 3;

    /**
     * Notification was canceled by the status bar reporting an inflation error.
     */
    public static final int REASON_DELEGATE_ERROR = 4;

    /**
     * Notification was canceled by the package manager modifying the package.
     */
    public static final int REASON_PACKAGE_CHANGED = 5;

    /**
     * Notification was canceled by the owning user context being stopped.
     */
    public static final int REASON_USER_STOPPED = 6;

    /**
     * Notification was canceled by the user banning the package.
     */
    public static final int REASON_PACKAGE_BANNED = 7;

    /**
     * Notification was canceled by the app canceling this specific notification.
     */
    public static final int REASON_APP_CANCEL = 8;

    /**
     * Notification was canceled by the app cancelling all its notifications.
     */
    public static final int REASON_APP_CANCEL_ALL = 9;

    /**
     * Notification was canceled by a listener reporting a user dismissal.
     */
    public static final int REASON_LISTENER_CANCEL = 10;

    /**
     * Notification was canceled by a listener reporting a user dismiss all.
     */
    public static final int REASON_LISTENER_CANCEL_ALL = 11;

    /**
     * Notification was canceled because it was a member of a canceled group.
     */
    public static final int REASON_GROUP_SUMMARY_CANCELED = 12;

    /**
     * Notification was canceled because it was an invisible member of a group.
     */
    public static final int REASON_GROUP_OPTIMIZATION = 13;

    /**
     * Notification was canceled by the device administrator suspending the package.
     */
    public static final int REASON_PACKAGE_SUSPENDED = 14;

    /**
     * Notification was canceled by the owning managed profile being turned off.
     */
    public static final int REASON_PROFILE_TURNED_OFF = 15;

    /**
     * Autobundled summary notification was canceled because its group was unbundled
     */
    public static final int REASON_UNAUTOBUNDLED = 16;

    private android.os.Handler mHandler;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void registerAsSystemService(android.content.Context context, android.content.ComponentName componentName, int currentUser) {
        throw new java.lang.UnsupportedOperationException("the ranker lifecycle is managed by the system.");
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void unregisterAsSystemService() {
        throw new java.lang.UnsupportedOperationException("the ranker lifecycle is managed by the system.");
    }

    @java.lang.Override
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        mHandler = new android.service.notification.NotificationRankerService.MyHandler(getContext().getMainLooper());
    }

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        if (mWrapper == null) {
            mWrapper = new android.service.notification.NotificationRankerService.NotificationRankingServiceWrapper();
        }
        return mWrapper;
    }

    /**
     * A notification was posted by an app. Called before alert.
     *
     * @param sbn
     * 		the new notification
     * @param importance
     * 		the initial importance of the notification.
     * @param user
     * 		true if the initial importance reflects an explicit user preference.
     * @return an adjustment or null to take no action, within 100ms.
     */
    public abstract android.service.notification.Adjustment onNotificationEnqueued(android.service.notification.StatusBarNotification sbn, int importance, boolean user);

    /**
     * The visibility of a notification has changed.
     *
     * @param key
     * 		the notification key
     * @param time
     * 		milliseconds since midnight, January 1, 1970 UTC.
     * @param visible
     * 		true if the notification became visible, false if hidden.
     */
    public void onNotificationVisibilityChanged(java.lang.String key, long time, boolean visible) {
        // Do nothing, Override this to collect visibility statistics.
    }

    /**
     * The user clicked on a notification.
     *
     * @param key
     * 		the notification key
     * @param time
     * 		milliseconds since midnight, January 1, 1970 UTC.
     */
    public void onNotificationClick(java.lang.String key, long time) {
        // Do nothing, Override this to collect click statistics
    }

    /**
     * The user clicked on a notification action.
     *
     * @param key
     * 		the notification key
     * @param time
     * 		milliseconds since midnight, January 1, 1970 UTC.
     * @param actionIndex
     * 		the index of the action button that was pressed.
     */
    public void onNotificationActionClick(java.lang.String key, long time, int actionIndex) {
        // Do nothing, Override this to collect action button click statistics
    }

    /**
     * A notification was removed.
     *
     * @param key
     * 		the notification key
     * @param time
     * 		milliseconds since midnight, January 1, 1970 UTC.
     * @param reason
     * 		see {@link #REASON_LISTENER_CANCEL}, etc.
     */
    public void onNotificationRemoved(java.lang.String key, long time, int reason) {
        // Do nothing, Override this to collect dismissal statistics
    }

    /**
     * Updates a notification.  N.B. this won’t cause
     * an existing notification to alert, but might allow a future update to
     * this notification to alert.
     *
     * @param adjustment
     * 		the adjustment with an explanation
     */
    public final void adjustNotification(android.service.notification.Adjustment adjustment) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().applyAdjustmentFromRankerService(mWrapper, adjustment);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(android.service.notification.NotificationRankerService.TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Updates existing notifications. Re-ranking won't occur until all adjustments are applied.
     * N.B. this won’t cause an existing notification to alert, but might allow a future update to
     * these notifications to alert.
     *
     * @param adjustments
     * 		a list of adjustments with explanations
     */
    public final void adjustNotifications(java.util.List<android.service.notification.Adjustment> adjustments) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().applyAdjustmentsFromRankerService(mWrapper, adjustments);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(android.service.notification.NotificationRankerService.TAG, "Unable to contact notification manager", ex);
        }
    }

    private class NotificationRankingServiceWrapper extends android.service.notification.NotificationListenerService.NotificationListenerWrapper {
        @java.lang.Override
        public void onNotificationEnqueued(android.service.notification.IStatusBarNotificationHolder sbnHolder, int importance, boolean user) {
            android.service.notification.StatusBarNotification sbn;
            try {
                sbn = sbnHolder.get();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.service.notification.NotificationRankerService.TAG, "onNotificationEnqueued: Error receiving StatusBarNotification", e);
                return;
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = sbn;
            args.argi1 = importance;
            args.argi2 = (user) ? 1 : 0;
            mHandler.obtainMessage(android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_ENQUEUED, args).sendToTarget();
        }

        @java.lang.Override
        public void onNotificationVisibilityChanged(java.lang.String key, long time, boolean visible) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = key;
            args.arg2 = time;
            args.argi1 = (visible) ? 1 : 0;
            mHandler.obtainMessage(android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_VISIBILITY_CHANGED, args).sendToTarget();
        }

        @java.lang.Override
        public void onNotificationClick(java.lang.String key, long time) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = key;
            args.arg2 = time;
            mHandler.obtainMessage(android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_CLICK, args).sendToTarget();
        }

        @java.lang.Override
        public void onNotificationActionClick(java.lang.String key, long time, int actionIndex) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = key;
            args.arg2 = time;
            args.argi1 = actionIndex;
            mHandler.obtainMessage(android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_ACTION_CLICK, args).sendToTarget();
        }

        @java.lang.Override
        public void onNotificationRemovedReason(java.lang.String key, long time, int reason) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = key;
            args.arg2 = time;
            args.argi1 = reason;
            mHandler.obtainMessage(android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_REMOVED_REASON, args).sendToTarget();
        }
    }

    private final class MyHandler extends android.os.Handler {
        public static final int MSG_ON_NOTIFICATION_ENQUEUED = 1;

        public static final int MSG_ON_NOTIFICATION_VISIBILITY_CHANGED = 2;

        public static final int MSG_ON_NOTIFICATION_CLICK = 3;

        public static final int MSG_ON_NOTIFICATION_ACTION_CLICK = 4;

        public static final int MSG_ON_NOTIFICATION_REMOVED_REASON = 5;

        public MyHandler(android.os.Looper looper) {
            super(looper, null, false);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_ENQUEUED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        android.service.notification.StatusBarNotification sbn = ((android.service.notification.StatusBarNotification) (args.arg1));
                        final int importance = args.argi1;
                        final boolean user = args.argi2 == 1;
                        args.recycle();
                        android.service.notification.Adjustment adjustment = onNotificationEnqueued(sbn, importance, user);
                        if (adjustment != null) {
                            adjustNotification(adjustment);
                        }
                    }
                    break;
                case android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_VISIBILITY_CHANGED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final java.lang.String key = ((java.lang.String) (args.arg1));
                        final long time = ((long) (args.arg2));
                        final boolean visible = args.argi1 == 1;
                        args.recycle();
                        onNotificationVisibilityChanged(key, time, visible);
                    }
                    break;
                case android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_CLICK :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final java.lang.String key = ((java.lang.String) (args.arg1));
                        final long time = ((long) (args.arg2));
                        args.recycle();
                        onNotificationClick(key, time);
                    }
                    break;
                case android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_ACTION_CLICK :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final java.lang.String key = ((java.lang.String) (args.arg1));
                        final long time = ((long) (args.arg2));
                        final int actionIndex = args.argi1;
                        args.recycle();
                        onNotificationActionClick(key, time, actionIndex);
                    }
                    break;
                case android.service.notification.NotificationRankerService.MyHandler.MSG_ON_NOTIFICATION_REMOVED_REASON :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final java.lang.String key = ((java.lang.String) (args.arg1));
                        final long time = ((long) (args.arg2));
                        final int reason = args.argi1;
                        args.recycle();
                        onNotificationRemoved(key, time, reason);
                    }
                    break;
            }
        }
    }
}

