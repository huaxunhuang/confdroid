/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * A service that receives calls from the system when new notifications are
 * posted or removed, or their ranking changed.
 * <p>To extend this class, you must declare the service in your manifest file with
 * the {@link android.Manifest.permission#BIND_NOTIFICATION_LISTENER_SERVICE} permission
 * and include an intent filter with the {@link #SERVICE_INTERFACE} action. For example:</p>
 * <pre>
 * &lt;service android:name=".NotificationListener"
 *          android:label="&#64;string/service_name"
 *          android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
 *     &lt;intent-filter>
 *         &lt;action android:name="android.service.notification.NotificationListenerService" />
 *     &lt;/intent-filter>
 * &lt;/service></pre>
 *
 * <p>The service should wait for the {@link #onListenerConnected()} event
 * before performing any operations. The {@link #requestRebind(ComponentName)}
 * method is the <i>only</i> one that is safe to call before {@link #onListenerConnected()}
 * or after {@link #onListenerDisconnected()}.
 * </p>
 */
public abstract class NotificationListenerService extends android.app.Service {
    // TAG = "NotificationListenerService[MySubclass]"
    private final java.lang.String TAG = ((android.service.notification.NotificationListenerService.class.getSimpleName() + "[") + getClass().getSimpleName()) + "]";

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     Normal interruption filter.
     */
    public static final int INTERRUPTION_FILTER_ALL = android.app.NotificationManager.INTERRUPTION_FILTER_ALL;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     Priority interruption filter.
     */
    public static final int INTERRUPTION_FILTER_PRIORITY = android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     No interruptions filter.
     */
    public static final int INTERRUPTION_FILTER_NONE = android.app.NotificationManager.INTERRUPTION_FILTER_NONE;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     Alarms only interruption filter.
     */
    public static final int INTERRUPTION_FILTER_ALARMS = android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant - returned when
     * the value is unavailable for any reason.  For example, before the notification listener
     * is connected.
     *
     * {@see #onListenerConnected()}
     */
    public static final int INTERRUPTION_FILTER_UNKNOWN = android.app.NotificationManager.INTERRUPTION_FILTER_UNKNOWN;

    /**
     * {@link #getCurrentListenerHints() Listener hints} constant - the primary device UI
     * should disable notification sound, vibrating and other visual or aural effects.
     * This does not change the interruption filter, only the effects. *
     */
    public static final int HINT_HOST_DISABLE_EFFECTS = 1;

    /**
     * {@link #getCurrentListenerHints() Listener hints} constant - the primary device UI
     * should disable notification sound, but not phone calls.
     * This does not change the interruption filter, only the effects. *
     */
    public static final int HINT_HOST_DISABLE_NOTIFICATION_EFFECTS = 1 << 1;

    /**
     * {@link #getCurrentListenerHints() Listener hints} constant - the primary device UI
     * should disable phone call sounds, buyt not notification sound.
     * This does not change the interruption filter, only the effects. *
     */
    public static final int HINT_HOST_DISABLE_CALL_EFFECTS = 1 << 2;

    /**
     * Whether notification suppressed by DND should not interruption visually when the screen is
     * off.
     */
    public static final int SUPPRESSED_EFFECT_SCREEN_OFF = android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_OFF;

    /**
     * Whether notification suppressed by DND should not interruption visually when the screen is
     * on.
     */
    public static final int SUPPRESSED_EFFECT_SCREEN_ON = android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_ON;

    /**
     * The full trim of the StatusBarNotification including all its features.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final int TRIM_FULL = 0;

    /**
     * A light trim of the StatusBarNotification excluding the following features:
     *
     * <ol>
     *     <li>{@link Notification#tickerView tickerView}</li>
     *     <li>{@link Notification#contentView contentView}</li>
     *     <li>{@link Notification#largeIcon largeIcon}</li>
     *     <li>{@link Notification#bigContentView bigContentView}</li>
     *     <li>{@link Notification#headsUpContentView headsUpContentView}</li>
     *     <li>{@link Notification#EXTRA_LARGE_ICON extras[EXTRA_LARGE_ICON]}</li>
     *     <li>{@link Notification#EXTRA_LARGE_ICON_BIG extras[EXTRA_LARGE_ICON_BIG]}</li>
     *     <li>{@link Notification#EXTRA_PICTURE extras[EXTRA_PICTURE]}</li>
     *     <li>{@link Notification#EXTRA_BIG_TEXT extras[EXTRA_BIG_TEXT]}</li>
     * </ol>
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final int TRIM_LIGHT = 1;

    private final java.lang.Object mLock = new java.lang.Object();

    private android.os.Handler mHandler;

    /**
     *
     *
     * @unknown 
     */
    protected android.service.notification.NotificationListenerService.NotificationListenerWrapper mWrapper = null;

    private boolean isConnected = false;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.service.notification.NotificationListenerService.RankingMap mRankingMap;

    private android.app.INotificationManager mNoMan;

    /**
     * Only valid after a successful call to (@link registerAsService}.
     *
     * @unknown 
     */
    protected int mCurrentUser;

    /**
     * This context is required for system services since NotificationListenerService isn't
     * started as a real Service and hence no context is available..
     *
     * @unknown 
     */
    protected android.content.Context mSystemContext;

    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.service.notification.NotificationListenerService";

    @java.lang.Override
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        mHandler = new android.service.notification.NotificationListenerService.MyHandler(getMainLooper());
    }

    /**
     * Implement this method to learn about new notifications as they are posted by apps.
     *
     * @param sbn
     * 		A data structure encapsulating the original {@link android.app.Notification}
     * 		object as well as its identifying information (tag and id) and source
     * 		(package name).
     */
    public void onNotificationPosted(android.service.notification.StatusBarNotification sbn) {
        // optional
    }

    /**
     * Implement this method to learn about new notifications as they are posted by apps.
     *
     * @param sbn
     * 		A data structure encapsulating the original {@link android.app.Notification}
     * 		object as well as its identifying information (tag and id) and source
     * 		(package name).
     * @param rankingMap
     * 		The current ranking map that can be used to retrieve ranking information
     * 		for active notifications, including the newly posted one.
     */
    public void onNotificationPosted(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        onNotificationPosted(sbn);
    }

    /**
     * Implement this method to learn when notifications are removed.
     * <p>
     * This might occur because the user has dismissed the notification using system UI (or another
     * notification listener) or because the app has withdrawn the notification.
     * <p>
     * NOTE: The {@link StatusBarNotification} object you receive will be "light"; that is, the
     * result from {@link StatusBarNotification#getNotification} may be missing some heavyweight
     * fields such as {@link android.app.Notification#contentView} and
     * {@link android.app.Notification#largeIcon}. However, all other fields on
     * {@link StatusBarNotification}, sufficient to match this call with a prior call to
     * {@link #onNotificationPosted(StatusBarNotification)}, will be intact.
     *
     * @param sbn
     * 		A data structure encapsulating at least the original information (tag and id)
     * 		and source (package name) used to post the {@link android.app.Notification} that
     * 		was just removed.
     */
    public void onNotificationRemoved(android.service.notification.StatusBarNotification sbn) {
        // optional
    }

    /**
     * Implement this method to learn when notifications are removed.
     * <p>
     * This might occur because the user has dismissed the notification using system UI (or another
     * notification listener) or because the app has withdrawn the notification.
     * <p>
     * NOTE: The {@link StatusBarNotification} object you receive will be "light"; that is, the
     * result from {@link StatusBarNotification#getNotification} may be missing some heavyweight
     * fields such as {@link android.app.Notification#contentView} and
     * {@link android.app.Notification#largeIcon}. However, all other fields on
     * {@link StatusBarNotification}, sufficient to match this call with a prior call to
     * {@link #onNotificationPosted(StatusBarNotification)}, will be intact.
     *
     * @param sbn
     * 		A data structure encapsulating at least the original information (tag and id)
     * 		and source (package name) used to post the {@link android.app.Notification} that
     * 		was just removed.
     * @param rankingMap
     * 		The current ranking map that can be used to retrieve ranking information
     * 		for active notifications.
     */
    public void onNotificationRemoved(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        onNotificationRemoved(sbn);
    }

    /**
     * Implement this method to learn about when the listener is enabled and connected to
     * the notification manager.  You are safe to call {@link #getActiveNotifications()}
     * at this time.
     */
    public void onListenerConnected() {
        // optional
    }

    /**
     * Implement this method to learn about when the listener is disconnected from the
     * notification manager.You will not receive any events after this call, and may only
     * call {@link #requestRebind(ComponentName)} at this time.
     */
    public void onListenerDisconnected() {
        // optional
    }

    /**
     * Implement this method to be notified when the notification ranking changes.
     *
     * @param rankingMap
     * 		The current ranking map that can be used to retrieve ranking information
     * 		for active notifications.
     */
    public void onNotificationRankingUpdate(android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        // optional
    }

    /**
     * Implement this method to be notified when the
     * {@link #getCurrentListenerHints() Listener hints} change.
     *
     * @param hints
     * 		The current {@link #getCurrentListenerHints() listener hints}.
     */
    public void onListenerHintsChanged(int hints) {
        // optional
    }

    /**
     * Implement this method to be notified when the
     * {@link #getCurrentInterruptionFilter() interruption filter} changed.
     *
     * @param interruptionFilter
     * 		The current
     * 		{@link #getCurrentInterruptionFilter() interruption filter}.
     */
    public void onInterruptionFilterChanged(int interruptionFilter) {
        // optional
    }

    /**
     *
     *
     * @unknown 
     */
    protected final android.app.INotificationManager getNotificationInterface() {
        if (mNoMan == null) {
            mNoMan = INotificationManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.NOTIFICATION_SERVICE));
        }
        return mNoMan;
    }

    /**
     * Inform the notification manager about dismissal of a single notification.
     * <p>
     * Use this if your listener has a user interface that allows the user to dismiss individual
     * notifications, similar to the behavior of Android's status bar and notification panel.
     * It should be called after the user dismisses a single notification using your UI;
     * upon being informed, the notification manager will actually remove the notification
     * and you will get an {@link #onNotificationRemoved(StatusBarNotification)} callback.
     * <p>
     * <b>Note:</b> If your listener allows the user to fire a notification's
     * {@link android.app.Notification#contentIntent} by tapping/clicking/etc., you should call
     * this method at that time <i>if</i> the Notification in question has the
     * {@link android.app.Notification#FLAG_AUTO_CANCEL} flag set.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param pkg
     * 		Package of the notifying app.
     * @param tag
     * 		Tag of the notification as specified by the notifying app in
     * 		{@link android.app.NotificationManager#notify(String, int, android.app.Notification)}.
     * @param id
     * 		ID of the notification as specified by the notifying app in
     * 		{@link android.app.NotificationManager#notify(String, int, android.app.Notification)}.
     * 		<p>
     * @deprecated Use {@link #cancelNotification(String key)}
    instead. Beginning with {@link android.os.Build.VERSION_CODES#LOLLIPOP} this method will no longer
    cancel the notification. It will continue to cancel the notification for applications
    whose {@code targetSdkVersion} is earlier than {@link android.os.Build.VERSION_CODES#LOLLIPOP}.
     */
    public final void cancelNotification(java.lang.String pkg, java.lang.String tag, int id) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().cancelNotificationFromListener(mWrapper, pkg, tag, id);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Inform the notification manager about dismissal of a single notification.
     * <p>
     * Use this if your listener has a user interface that allows the user to dismiss individual
     * notifications, similar to the behavior of Android's status bar and notification panel.
     * It should be called after the user dismisses a single notification using your UI;
     * upon being informed, the notification manager will actually remove the notification
     * and you will get an {@link #onNotificationRemoved(StatusBarNotification)} callback.
     * <p>
     * <b>Note:</b> If your listener allows the user to fire a notification's
     * {@link android.app.Notification#contentIntent} by tapping/clicking/etc., you should call
     * this method at that time <i>if</i> the Notification in question has the
     * {@link android.app.Notification#FLAG_AUTO_CANCEL} flag set.
     * <p>
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param key
     * 		Notification to dismiss from {@link StatusBarNotification#getKey()}.
     */
    public final void cancelNotification(java.lang.String key) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().cancelNotificationsFromListener(mWrapper, new java.lang.String[]{ key });
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Inform the notification manager about dismissal of all notifications.
     * <p>
     * Use this if your listener has a user interface that allows the user to dismiss all
     * notifications, similar to the behavior of Android's status bar and notification panel.
     * It should be called after the user invokes the "dismiss all" function of your UI;
     * upon being informed, the notification manager will actually remove all active notifications
     * and you will get multiple {@link #onNotificationRemoved(StatusBarNotification)} callbacks.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * {@see #cancelNotification(String, String, int)}
     */
    public final void cancelAllNotifications() {
        /* all */
        cancelNotifications(null);
    }

    /**
     * Inform the notification manager about dismissal of specific notifications.
     * <p>
     * Use this if your listener has a user interface that allows the user to dismiss
     * multiple notifications at once.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param keys
     * 		Notifications to dismiss, or {@code null} to dismiss all.
     * 		
     * 		{@see #cancelNotification(String, String, int)}
     */
    public final void cancelNotifications(java.lang.String[] keys) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().cancelNotificationsFromListener(mWrapper, keys);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Inform the notification manager that these notifications have been viewed by the
     * user. This should only be called when there is sufficient confidence that the user is
     * looking at the notifications, such as when the notifications appear on the screen due to
     * an explicit user interaction.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param keys
     * 		Notifications to mark as seen.
     */
    public final void setNotificationsShown(java.lang.String[] keys) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().setNotificationsShownFromListener(mWrapper, keys);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Sets the notification trim that will be received via {@link #onNotificationPosted}.
     *
     * <p>
     * Setting a trim other than {@link #TRIM_FULL} enables listeners that don't need access to the
     * full notification features right away to reduce their memory footprint. Full notifications
     * can be requested on-demand via {@link #getActiveNotifications(int)}.
     *
     * <p>
     * Set to {@link #TRIM_FULL} initially.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @unknown 
     * @param trim
     * 		trim of the notifications to be passed via {@link #onNotificationPosted}.
     * 		See <code>TRIM_*</code> constants.
     */
    @android.annotation.SystemApi
    public final void setOnNotificationPostedTrim(int trim) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().setOnNotificationPostedTrimFromListener(mWrapper, trim);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Request the list of outstanding notifications (that is, those that are visible to the
     * current user). Useful when you don't know what's already been posted.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @return An array of active notifications, sorted in natural order.
     */
    public android.service.notification.StatusBarNotification[] getActiveNotifications() {
        return getActiveNotifications(null, android.service.notification.NotificationListenerService.TRIM_FULL);
    }

    /**
     * Request the list of outstanding notifications (that is, those that are visible to the
     * current user). Useful when you don't know what's already been posted.
     *
     * @unknown 
     * @param trim
     * 		trim of the notifications to be returned. See <code>TRIM_*</code> constants.
     * @return An array of active notifications, sorted in natural order.
     */
    @android.annotation.SystemApi
    public android.service.notification.StatusBarNotification[] getActiveNotifications(int trim) {
        return getActiveNotifications(null, trim);
    }

    /**
     * Request one or more notifications by key. Useful if you have been keeping track of
     * notifications but didn't want to retain the bits, and now need to go back and extract
     * more data out of those notifications.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param keys
     * 		the keys of the notifications to request
     * @return An array of notifications corresponding to the requested keys, in the
    same order as the key list.
     */
    public android.service.notification.StatusBarNotification[] getActiveNotifications(java.lang.String[] keys) {
        return getActiveNotifications(keys, android.service.notification.NotificationListenerService.TRIM_FULL);
    }

    /**
     * Request one or more notifications by key. Useful if you have been keeping track of
     * notifications but didn't want to retain the bits, and now need to go back and extract
     * more data out of those notifications.
     *
     * @unknown 
     * @param keys
     * 		the keys of the notifications to request
     * @param trim
     * 		trim of the notifications to be returned. See <code>TRIM_*</code> constants.
     * @return An array of notifications corresponding to the requested keys, in the
    same order as the key list.
     */
    @android.annotation.SystemApi
    public android.service.notification.StatusBarNotification[] getActiveNotifications(java.lang.String[] keys, int trim) {
        if (!isBound())
            return null;

        try {
            android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> parceledList = getNotificationInterface().getActiveNotificationsFromListener(mWrapper, keys, trim);
            java.util.List<android.service.notification.StatusBarNotification> list = parceledList.getList();
            java.util.ArrayList<android.service.notification.StatusBarNotification> corruptNotifications = null;
            int N = list.size();
            for (int i = 0; i < N; i++) {
                android.service.notification.StatusBarNotification sbn = list.get(i);
                android.app.Notification notification = sbn.getNotification();
                try {
                    // convert icon metadata to legacy format for older clients
                    createLegacyIconExtras(notification);
                    // populate remote views for older clients.
                    maybePopulateRemoteViews(notification);
                } catch (java.lang.IllegalArgumentException e) {
                    if (corruptNotifications == null) {
                        corruptNotifications = new java.util.ArrayList<>(N);
                    }
                    corruptNotifications.add(sbn);
                    android.util.Log.w(TAG, "onNotificationPosted: can't rebuild notification from " + sbn.getPackageName());
                }
            }
            if (corruptNotifications != null) {
                list.removeAll(corruptNotifications);
            }
            return list.toArray(new android.service.notification.StatusBarNotification[list.size()]);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
        return null;
    }

    /**
     * Gets the set of hints representing current state.
     *
     * <p>
     * The current state may differ from the requested state if the hint represents state
     * shared across all listeners or a feature the notification host does not support or refuses
     * to grant.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @return Zero or more of the HINT_ constants.
     */
    public final int getCurrentListenerHints() {
        if (!isBound())
            return 0;

        try {
            return getNotificationInterface().getHintsFromListener(mWrapper);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
            return 0;
        }
    }

    /**
     * Gets the current notification interruption filter active on the host.
     *
     * <p>
     * The interruption filter defines which notifications are allowed to interrupt the user
     * (e.g. via sound &amp; vibration) and is applied globally. Listeners can find out whether
     * a specific notification matched the interruption filter via
     * {@link Ranking#matchesInterruptionFilter()}.
     * <p>
     * The current filter may differ from the previously requested filter if the notification host
     * does not support or refuses to apply the requested filter, or if another component changed
     * the filter in the meantime.
     * <p>
     * Listen for updates using {@link #onInterruptionFilterChanged(int)}.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @return One of the INTERRUPTION_FILTER_ constants, or INTERRUPTION_FILTER_UNKNOWN when
    unavailable.
     */
    public final int getCurrentInterruptionFilter() {
        if (!isBound())
            return android.service.notification.NotificationListenerService.INTERRUPTION_FILTER_UNKNOWN;

        try {
            return getNotificationInterface().getInterruptionFilterFromListener(mWrapper);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
            return android.service.notification.NotificationListenerService.INTERRUPTION_FILTER_UNKNOWN;
        }
    }

    /**
     * Sets the desired {@link #getCurrentListenerHints() listener hints}.
     *
     * <p>
     * This is merely a request, the host may or may not choose to take action depending
     * on other listener requests or other global state.
     * <p>
     * Listen for updates using {@link #onListenerHintsChanged(int)}.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param hints
     * 		One or more of the HINT_ constants.
     */
    public final void requestListenerHints(int hints) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().requestHintsFromListener(mWrapper, hints);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Sets the desired {@link #getCurrentInterruptionFilter() interruption filter}.
     *
     * <p>
     * This is merely a request, the host may or may not choose to apply the requested
     * interruption filter depending on other listener requests or other global state.
     * <p>
     * Listen for updates using {@link #onInterruptionFilterChanged(int)}.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @param interruptionFilter
     * 		One of the INTERRUPTION_FILTER_ constants.
     */
    public final void requestInterruptionFilter(int interruptionFilter) {
        if (!isBound())
            return;

        try {
            getNotificationInterface().requestInterruptionFilterFromListener(mWrapper, interruptionFilter);
        } catch (android.os.RemoteException ex) {
            android.util.Log.v(TAG, "Unable to contact notification manager", ex);
        }
    }

    /**
     * Returns current ranking information.
     *
     * <p>
     * The returned object represents the current ranking snapshot and only
     * applies for currently active notifications.
     * <p>
     * Generally you should use the RankingMap that is passed with events such
     * as {@link #onNotificationPosted(StatusBarNotification, RankingMap)},
     * {@link #onNotificationRemoved(StatusBarNotification, RankingMap)}, and
     * so on. This method should only be used when needing access outside of
     * such events, for example to retrieve the RankingMap right after
     * initialization.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation.
     *
     * @return A {@link RankingMap} object providing access to ranking information
     */
    public android.service.notification.NotificationListenerService.RankingMap getCurrentRanking() {
        synchronized(mLock) {
            return mRankingMap;
        }
    }

    /**
     * This is not the lifecycle event you are looking for.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing any operations.
     */
    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (mWrapper == null) {
            mWrapper = new android.service.notification.NotificationListenerService.NotificationListenerWrapper();
        }
        return mWrapper;
    }

    /**
     *
     *
     * @unknown 
     */
    protected boolean isBound() {
        if (mWrapper == null) {
            android.util.Log.w(TAG, "Notification listener service not yet bound.");
            return false;
        }
        return true;
    }

    @java.lang.Override
    public void onDestroy() {
        onListenerDisconnected();
        super.onDestroy();
    }

    /**
     * Directly register this service with the Notification Manager.
     *
     * <p>Only system services may use this call. It will fail for non-system callers.
     * Apps should ask the user to add their listener in Settings.
     *
     * @param context
     * 		Context required for accessing resources. Since this service isn't
     * 		launched as a real Service when using this method, a context has to be passed in.
     * @param componentName
     * 		the component that will consume the notification information
     * @param currentUser
     * 		the user to use as the stream filter
     * @unknown 
     */
    @android.annotation.SystemApi
    public void registerAsSystemService(android.content.Context context, android.content.ComponentName componentName, int currentUser) throws android.os.RemoteException {
        if (mWrapper == null) {
            mWrapper = new android.service.notification.NotificationListenerService.NotificationListenerWrapper();
        }
        mSystemContext = context;
        android.app.INotificationManager noMan = getNotificationInterface();
        mHandler = new android.service.notification.NotificationListenerService.MyHandler(context.getMainLooper());
        mCurrentUser = currentUser;
        noMan.registerListener(mWrapper, componentName, currentUser);
    }

    /**
     * Directly unregister this service from the Notification Manager.
     *
     * <p>This method will fail for listeners that were not registered
     * with (@link registerAsService).
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public void unregisterAsSystemService() throws android.os.RemoteException {
        if (mWrapper != null) {
            android.app.INotificationManager noMan = getNotificationInterface();
            noMan.unregisterListener(mWrapper, mCurrentUser);
        }
    }

    /**
     * Request that the listener be rebound, after a previous call to (@link requestUnbind).
     *
     * <p>This method will fail for listeners that have
     * not been granted the permission by the user.
     */
    public static void requestRebind(android.content.ComponentName componentName) {
        android.app.INotificationManager noMan = INotificationManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.NOTIFICATION_SERVICE));
        try {
            noMan.requestBindListener(componentName);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Request that the service be unbound.
     *
     * <p>This will no longer receive updates until
     * {@link #requestRebind(ComponentName)} is called.
     * The service will likely be kiled by the system after this call.
     *
     * <p>The service should wait for the {@link #onListenerConnected()} event
     * before performing this operation. I know it's tempting, but you must wait.
     */
    public final void requestUnbind() {
        if (mWrapper != null) {
            android.app.INotificationManager noMan = getNotificationInterface();
            try {
                noMan.requestUnbindListener(mWrapper);
                // Disable future messages.
                isConnected = false;
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    /**
     * Convert new-style Icons to legacy representations for pre-M clients.
     */
    private void createLegacyIconExtras(android.app.Notification n) {
        android.graphics.drawable.Icon smallIcon = n.getSmallIcon();
        android.graphics.drawable.Icon largeIcon = n.getLargeIcon();
        if ((smallIcon != null) && (smallIcon.getType() == android.graphics.drawable.Icon.TYPE_RESOURCE)) {
            n.extras.putInt(android.app.Notification.EXTRA_SMALL_ICON, smallIcon.getResId());
            n.icon = smallIcon.getResId();
        }
        if (largeIcon != null) {
            android.graphics.drawable.Drawable d = largeIcon.loadDrawable(getContext());
            if ((d != null) && (d instanceof android.graphics.drawable.BitmapDrawable)) {
                final android.graphics.Bitmap largeIconBits = ((android.graphics.drawable.BitmapDrawable) (d)).getBitmap();
                n.extras.putParcelable(android.app.Notification.EXTRA_LARGE_ICON, largeIconBits);
                n.largeIcon = largeIconBits;
            }
        }
    }

    /**
     * Populates remote views for pre-N targeting apps.
     */
    private void maybePopulateRemoteViews(android.app.Notification notification) {
        if (getContext().getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.N) {
            android.app.Notification.Builder builder = android.app.Notification.Builder.recoverBuilder(getContext(), notification);
            // Some styles wrap Notification's contentView, bigContentView and headsUpContentView.
            // First inflate them all, only then set them to avoid recursive wrapping.
            android.widget.RemoteViews content = builder.createContentView();
            android.widget.RemoteViews big = builder.createBigContentView();
            android.widget.RemoteViews headsUp = builder.createHeadsUpContentView();
            notification.contentView = content;
            notification.bigContentView = big;
            notification.headsUpContentView = headsUp;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected class NotificationListenerWrapper extends android.service.notification.INotificationListener.Stub {
        @java.lang.Override
        public void onNotificationPosted(android.service.notification.IStatusBarNotificationHolder sbnHolder, android.service.notification.NotificationRankingUpdate update) {
            android.service.notification.StatusBarNotification sbn;
            try {
                sbn = sbnHolder.get();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "onNotificationPosted: Error receiving StatusBarNotification", e);
                return;
            }
            try {
                // convert icon metadata to legacy format for older clients
                createLegacyIconExtras(sbn.getNotification());
                maybePopulateRemoteViews(sbn.getNotification());
            } catch (java.lang.IllegalArgumentException e) {
                // warn and drop corrupt notification
                android.util.Log.w(TAG, "onNotificationPosted: can't rebuild notification from " + sbn.getPackageName());
                sbn = null;
            }
            // protect subclass from concurrent modifications of (@link mNotificationKeys}.
            synchronized(mLock) {
                applyUpdateLocked(update);
                if (sbn != null) {
                    com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                    args.arg1 = sbn;
                    args.arg2 = mRankingMap;
                    mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_POSTED, args).sendToTarget();
                } else {
                    // still pass along the ranking map, it may contain other information
                    mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_RANKING_UPDATE, mRankingMap).sendToTarget();
                }
            }
        }

        @java.lang.Override
        public void onNotificationRemoved(android.service.notification.IStatusBarNotificationHolder sbnHolder, android.service.notification.NotificationRankingUpdate update) {
            android.service.notification.StatusBarNotification sbn;
            try {
                sbn = sbnHolder.get();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, "onNotificationRemoved: Error receiving StatusBarNotification", e);
                return;
            }
            // protect subclass from concurrent modifications of (@link mNotificationKeys}.
            synchronized(mLock) {
                applyUpdateLocked(update);
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = sbn;
                args.arg2 = mRankingMap;
                mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_REMOVED, args).sendToTarget();
            }
        }

        @java.lang.Override
        public void onListenerConnected(android.service.notification.NotificationRankingUpdate update) {
            // protect subclass from concurrent modifications of (@link mNotificationKeys}.
            synchronized(mLock) {
                applyUpdateLocked(update);
            }
            isConnected = true;
            mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_LISTENER_CONNECTED).sendToTarget();
        }

        @java.lang.Override
        public void onNotificationRankingUpdate(android.service.notification.NotificationRankingUpdate update) throws android.os.RemoteException {
            // protect subclass from concurrent modifications of (@link mNotificationKeys}.
            synchronized(mLock) {
                applyUpdateLocked(update);
                mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_RANKING_UPDATE, mRankingMap).sendToTarget();
            }
        }

        @java.lang.Override
        public void onListenerHintsChanged(int hints) throws android.os.RemoteException {
            mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_LISTENER_HINTS_CHANGED, hints, 0).sendToTarget();
        }

        @java.lang.Override
        public void onInterruptionFilterChanged(int interruptionFilter) throws android.os.RemoteException {
            mHandler.obtainMessage(android.service.notification.NotificationListenerService.MyHandler.MSG_ON_INTERRUPTION_FILTER_CHANGED, interruptionFilter, 0).sendToTarget();
        }

        @java.lang.Override
        public void onNotificationEnqueued(android.service.notification.IStatusBarNotificationHolder notificationHolder, int importance, boolean user) throws android.os.RemoteException {
            // no-op in the listener
        }

        @java.lang.Override
        public void onNotificationVisibilityChanged(java.lang.String key, long time, boolean visible) throws android.os.RemoteException {
            // no-op in the listener
        }

        @java.lang.Override
        public void onNotificationClick(java.lang.String key, long time) throws android.os.RemoteException {
            // no-op in the listener
        }

        @java.lang.Override
        public void onNotificationActionClick(java.lang.String key, long time, int actionIndex) throws android.os.RemoteException {
            // no-op in the listener
        }

        @java.lang.Override
        public void onNotificationRemovedReason(java.lang.String key, long time, int reason) throws android.os.RemoteException {
            // no-op in the listener
        }
    }

    private void applyUpdateLocked(android.service.notification.NotificationRankingUpdate update) {
        mRankingMap = new android.service.notification.NotificationListenerService.RankingMap(update);
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.content.Context getContext() {
        if (mSystemContext != null) {
            return mSystemContext;
        }
        return this;
    }

    /**
     * Stores ranking related information on a currently active notification.
     *
     * <p>
     * Ranking objects aren't automatically updated as notification events
     * occur. Instead, ranking information has to be retrieved again via the
     * current {@link RankingMap}.
     */
    public static class Ranking {
        /**
         * Value signifying that the user has not expressed a per-app visibility override value.
         *
         * @unknown 
         */
        public static final int VISIBILITY_NO_OVERRIDE = android.app.NotificationManager.VISIBILITY_NO_OVERRIDE;

        /**
         * Value signifying that the user has not expressed an importance.
         *
         * This value is for persisting preferences, and should never be associated with
         * an actual notification.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_UNSPECIFIED = android.app.NotificationManager.IMPORTANCE_UNSPECIFIED;

        /**
         * A notification with no importance: shows nowhere, is blocked.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_NONE = android.app.NotificationManager.IMPORTANCE_NONE;

        /**
         * Min notification importance: only shows in the shade, below the fold.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_MIN = android.app.NotificationManager.IMPORTANCE_MIN;

        /**
         * Low notification importance: shows everywhere, but is not intrusive.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_LOW = android.app.NotificationManager.IMPORTANCE_LOW;

        /**
         * Default notification importance: shows everywhere, allowed to makes noise,
         * but does not visually intrude.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_DEFAULT = android.app.NotificationManager.IMPORTANCE_DEFAULT;

        /**
         * Higher notification importance: shows everywhere, allowed to makes noise and peek.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_HIGH = android.app.NotificationManager.IMPORTANCE_HIGH;

        /**
         * Highest notification importance: shows everywhere, allowed to makes noise, peek, and
         * use full screen intents.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_MAX = android.app.NotificationManager.IMPORTANCE_MAX;

        private java.lang.String mKey;

        private int mRank = -1;

        private boolean mIsAmbient;

        private boolean mMatchesInterruptionFilter;

        private int mVisibilityOverride;

        private int mSuppressedVisualEffects;

        @android.app.NotificationManager.Importance
        private int mImportance;

        private java.lang.CharSequence mImportanceExplanation;

        // System specified group key.
        private java.lang.String mOverrideGroupKey;

        public Ranking() {
        }

        /**
         * Returns the key of the notification this Ranking applies to.
         */
        public java.lang.String getKey() {
            return mKey;
        }

        /**
         * Returns the rank of the notification.
         *
         * @return the rank of the notification, that is the 0-based index in
        the list of active notifications.
         */
        public int getRank() {
            return mRank;
        }

        /**
         * Returns whether the notification is an ambient notification, that is
         * a notification that doesn't require the user's immediate attention.
         */
        public boolean isAmbient() {
            return mIsAmbient;
        }

        /**
         * Returns the user specificed visibility for the package that posted
         * this notification, or
         * {@link NotificationListenerService.Ranking#VISIBILITY_NO_OVERRIDE} if
         * no such preference has been expressed.
         *
         * @unknown 
         */
        public int getVisibilityOverride() {
            return mVisibilityOverride;
        }

        /**
         * Returns the type(s) of visual effects that should be suppressed for this notification.
         * See {@link #SUPPRESSED_EFFECT_SCREEN_OFF}, {@link #SUPPRESSED_EFFECT_SCREEN_ON}.
         */
        public int getSuppressedVisualEffects() {
            return mSuppressedVisualEffects;
        }

        /**
         * Returns whether the notification matches the user's interruption
         * filter.
         *
         * @return {@code true} if the notification is allowed by the filter, or
        {@code false} if it is blocked.
         */
        public boolean matchesInterruptionFilter() {
            return mMatchesInterruptionFilter;
        }

        /**
         * Returns the importance of the notification, which dictates its
         * modes of presentation, see: {@link NotificationManager#IMPORTANCE_DEFAULT}, etc.
         *
         * @return the rank of the notification
         */
        @android.app.NotificationManager.Importance
        public int getImportance() {
            return mImportance;
        }

        /**
         * If the importance has been overriden by user preference, then this will be non-null,
         * and should be displayed to the user.
         *
         * @return the explanation for the importance, or null if it is the natural importance
         */
        public java.lang.CharSequence getImportanceExplanation() {
            return mImportanceExplanation;
        }

        /**
         * If the system has overriden the group key, then this will be non-null, and this
         * key should be used to bundle notifications.
         */
        public java.lang.String getOverrideGroupKey() {
            return mOverrideGroupKey;
        }

        private void populate(java.lang.String key, int rank, boolean matchesInterruptionFilter, int visibilityOverride, int suppressedVisualEffects, int importance, java.lang.CharSequence explanation, java.lang.String overrideGroupKey) {
            mKey = key;
            mRank = rank;
            mIsAmbient = importance < android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_LOW;
            mMatchesInterruptionFilter = matchesInterruptionFilter;
            mVisibilityOverride = visibilityOverride;
            mSuppressedVisualEffects = suppressedVisualEffects;
            mImportance = importance;
            mImportanceExplanation = explanation;
            mOverrideGroupKey = overrideGroupKey;
        }

        /**
         * {@hide }
         */
        public static java.lang.String importanceToString(int importance) {
            switch (importance) {
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_UNSPECIFIED :
                    return "UNSPECIFIED";
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_NONE :
                    return "NONE";
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_MIN :
                    return "MIN";
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_LOW :
                    return "LOW";
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_DEFAULT :
                    return "DEFAULT";
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_HIGH :
                    return "HIGH";
                case android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_MAX :
                    return "MAX";
                default :
                    return ("UNKNOWN(" + java.lang.String.valueOf(importance)) + ")";
            }
        }
    }

    /**
     * Provides access to ranking information on currently active
     * notifications.
     *
     * <p>
     * Note that this object represents a ranking snapshot that only applies to
     * notifications active at the time of retrieval.
     */
    public static class RankingMap implements android.os.Parcelable {
        private final android.service.notification.NotificationRankingUpdate mRankingUpdate;

        private android.util.ArrayMap<java.lang.String, java.lang.Integer> mRanks;

        private android.util.ArraySet<java.lang.Object> mIntercepted;

        private android.util.ArrayMap<java.lang.String, java.lang.Integer> mVisibilityOverrides;

        private android.util.ArrayMap<java.lang.String, java.lang.Integer> mSuppressedVisualEffects;

        private android.util.ArrayMap<java.lang.String, java.lang.Integer> mImportance;

        private android.util.ArrayMap<java.lang.String, java.lang.String> mImportanceExplanation;

        private android.util.ArrayMap<java.lang.String, java.lang.String> mOverrideGroupKeys;

        private RankingMap(android.service.notification.NotificationRankingUpdate rankingUpdate) {
            mRankingUpdate = rankingUpdate;
        }

        /**
         * Request the list of notification keys in their current ranking
         * order.
         *
         * @return An array of active notification keys, in their ranking order.
         */
        public java.lang.String[] getOrderedKeys() {
            return mRankingUpdate.getOrderedKeys();
        }

        /**
         * Populates outRanking with ranking information for the notification
         * with the given key.
         *
         * @return true if a valid key has been passed and outRanking has
        been populated; false otherwise
         */
        public boolean getRanking(java.lang.String key, android.service.notification.NotificationListenerService.Ranking outRanking) {
            int rank = getRank(key);
            outRanking.populate(key, rank, !isIntercepted(key), getVisibilityOverride(key), getSuppressedVisualEffects(key), getImportance(key), getImportanceExplanation(key), getOverrideGroupKey(key));
            return rank >= 0;
        }

        private int getRank(java.lang.String key) {
            synchronized(this) {
                if (mRanks == null) {
                    buildRanksLocked();
                }
            }
            java.lang.Integer rank = mRanks.get(key);
            return rank != null ? rank : -1;
        }

        private boolean isIntercepted(java.lang.String key) {
            synchronized(this) {
                if (mIntercepted == null) {
                    buildInterceptedSetLocked();
                }
            }
            return mIntercepted.contains(key);
        }

        private int getVisibilityOverride(java.lang.String key) {
            synchronized(this) {
                if (mVisibilityOverrides == null) {
                    buildVisibilityOverridesLocked();
                }
            }
            java.lang.Integer override = mVisibilityOverrides.get(key);
            if (override == null) {
                return android.service.notification.NotificationListenerService.Ranking.VISIBILITY_NO_OVERRIDE;
            }
            return override.intValue();
        }

        private int getSuppressedVisualEffects(java.lang.String key) {
            synchronized(this) {
                if (mSuppressedVisualEffects == null) {
                    buildSuppressedVisualEffectsLocked();
                }
            }
            java.lang.Integer suppressed = mSuppressedVisualEffects.get(key);
            if (suppressed == null) {
                return 0;
            }
            return suppressed.intValue();
        }

        private int getImportance(java.lang.String key) {
            synchronized(this) {
                if (mImportance == null) {
                    buildImportanceLocked();
                }
            }
            java.lang.Integer importance = mImportance.get(key);
            if (importance == null) {
                return android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_DEFAULT;
            }
            return importance.intValue();
        }

        private java.lang.String getImportanceExplanation(java.lang.String key) {
            synchronized(this) {
                if (mImportanceExplanation == null) {
                    buildImportanceExplanationLocked();
                }
            }
            return mImportanceExplanation.get(key);
        }

        private java.lang.String getOverrideGroupKey(java.lang.String key) {
            synchronized(this) {
                if (mOverrideGroupKeys == null) {
                    buildOverrideGroupKeys();
                }
            }
            return mOverrideGroupKeys.get(key);
        }

        // Locked by 'this'
        private void buildRanksLocked() {
            java.lang.String[] orderedKeys = mRankingUpdate.getOrderedKeys();
            mRanks = new android.util.ArrayMap<>(orderedKeys.length);
            for (int i = 0; i < orderedKeys.length; i++) {
                java.lang.String key = orderedKeys[i];
                mRanks.put(key, i);
            }
        }

        // Locked by 'this'
        private void buildInterceptedSetLocked() {
            java.lang.String[] dndInterceptedKeys = mRankingUpdate.getInterceptedKeys();
            mIntercepted = new android.util.ArraySet<>(dndInterceptedKeys.length);
            java.util.Collections.addAll(mIntercepted, dndInterceptedKeys);
        }

        // Locked by 'this'
        private void buildVisibilityOverridesLocked() {
            android.os.Bundle visibilityBundle = mRankingUpdate.getVisibilityOverrides();
            mVisibilityOverrides = new android.util.ArrayMap<>(visibilityBundle.size());
            for (java.lang.String key : visibilityBundle.keySet()) {
                mVisibilityOverrides.put(key, visibilityBundle.getInt(key));
            }
        }

        // Locked by 'this'
        private void buildSuppressedVisualEffectsLocked() {
            android.os.Bundle suppressedBundle = mRankingUpdate.getSuppressedVisualEffects();
            mSuppressedVisualEffects = new android.util.ArrayMap<>(suppressedBundle.size());
            for (java.lang.String key : suppressedBundle.keySet()) {
                mSuppressedVisualEffects.put(key, suppressedBundle.getInt(key));
            }
        }

        // Locked by 'this'
        private void buildImportanceLocked() {
            java.lang.String[] orderedKeys = mRankingUpdate.getOrderedKeys();
            int[] importance = mRankingUpdate.getImportance();
            mImportance = new android.util.ArrayMap<>(orderedKeys.length);
            for (int i = 0; i < orderedKeys.length; i++) {
                java.lang.String key = orderedKeys[i];
                mImportance.put(key, importance[i]);
            }
        }

        // Locked by 'this'
        private void buildImportanceExplanationLocked() {
            android.os.Bundle explanationBundle = mRankingUpdate.getImportanceExplanation();
            mImportanceExplanation = new android.util.ArrayMap<>(explanationBundle.size());
            for (java.lang.String key : explanationBundle.keySet()) {
                mImportanceExplanation.put(key, explanationBundle.getString(key));
            }
        }

        // Locked by 'this'
        private void buildOverrideGroupKeys() {
            android.os.Bundle overrideGroupKeys = mRankingUpdate.getOverrideGroupKeys();
            mOverrideGroupKeys = new android.util.ArrayMap<>(overrideGroupKeys.size());
            for (java.lang.String key : overrideGroupKeys.keySet()) {
                mOverrideGroupKeys.put(key, overrideGroupKeys.getString(key));
            }
        }

        // ----------- Parcelable
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeParcelable(mRankingUpdate, flags);
        }

        public static final android.os.Parcelable.Creator<android.service.notification.NotificationListenerService.RankingMap> CREATOR = new android.os.Parcelable.Creator<android.service.notification.NotificationListenerService.RankingMap>() {
            @java.lang.Override
            public android.service.notification.NotificationListenerService.RankingMap createFromParcel(android.os.Parcel source) {
                android.service.notification.NotificationRankingUpdate rankingUpdate = source.readParcelable(null);
                return new android.service.notification.NotificationListenerService.RankingMap(rankingUpdate);
            }

            @java.lang.Override
            public android.service.notification.NotificationListenerService.RankingMap[] newArray(int size) {
                return new android.service.notification.NotificationListenerService.RankingMap[size];
            }
        };
    }

    private final class MyHandler extends android.os.Handler {
        public static final int MSG_ON_NOTIFICATION_POSTED = 1;

        public static final int MSG_ON_NOTIFICATION_REMOVED = 2;

        public static final int MSG_ON_LISTENER_CONNECTED = 3;

        public static final int MSG_ON_NOTIFICATION_RANKING_UPDATE = 4;

        public static final int MSG_ON_LISTENER_HINTS_CHANGED = 5;

        public static final int MSG_ON_INTERRUPTION_FILTER_CHANGED = 6;

        public MyHandler(android.os.Looper looper) {
            super(looper, null, false);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (!isConnected) {
                return;
            }
            switch (msg.what) {
                case android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_POSTED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        android.service.notification.StatusBarNotification sbn = ((android.service.notification.StatusBarNotification) (args.arg1));
                        android.service.notification.NotificationListenerService.RankingMap rankingMap = ((android.service.notification.NotificationListenerService.RankingMap) (args.arg2));
                        args.recycle();
                        onNotificationPosted(sbn, rankingMap);
                    }
                    break;
                case android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_REMOVED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        android.service.notification.StatusBarNotification sbn = ((android.service.notification.StatusBarNotification) (args.arg1));
                        android.service.notification.NotificationListenerService.RankingMap rankingMap = ((android.service.notification.NotificationListenerService.RankingMap) (args.arg2));
                        args.recycle();
                        onNotificationRemoved(sbn, rankingMap);
                    }
                    break;
                case android.service.notification.NotificationListenerService.MyHandler.MSG_ON_LISTENER_CONNECTED :
                    {
                        onListenerConnected();
                    }
                    break;
                case android.service.notification.NotificationListenerService.MyHandler.MSG_ON_NOTIFICATION_RANKING_UPDATE :
                    {
                        android.service.notification.NotificationListenerService.RankingMap rankingMap = ((android.service.notification.NotificationListenerService.RankingMap) (msg.obj));
                        onNotificationRankingUpdate(rankingMap);
                    }
                    break;
                case android.service.notification.NotificationListenerService.MyHandler.MSG_ON_LISTENER_HINTS_CHANGED :
                    {
                        final int hints = msg.arg1;
                        onListenerHintsChanged(hints);
                    }
                    break;
                case android.service.notification.NotificationListenerService.MyHandler.MSG_ON_INTERRUPTION_FILTER_CHANGED :
                    {
                        final int interruptionFilter = msg.arg1;
                        onInterruptionFilterChanged(interruptionFilter);
                    }
                    break;
            }
        }
    }
}

