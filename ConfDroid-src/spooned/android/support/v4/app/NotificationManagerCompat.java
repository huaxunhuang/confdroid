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
 * Compatibility library for NotificationManager with fallbacks for older platforms.
 *
 * <p>To use this class, call the static function {@link #from} to get a
 * {@link NotificationManagerCompat} object, and then call one of its
 * methods to post or cancel notifications.
 */
public final class NotificationManagerCompat {
    private static final java.lang.String TAG = "NotifManCompat";

    /**
     * Notification extras key: if set to true, the posted notification should use
     * the side channel for delivery instead of using notification manager.
     */
    public static final java.lang.String EXTRA_USE_SIDE_CHANNEL = android.support.v4.app.NotificationCompatJellybean.EXTRA_USE_SIDE_CHANNEL;

    /**
     * Intent action to register for on a service to receive side channel
     * notifications. The listening service must be in the same package as an enabled
     * {@link android.service.notification.NotificationListenerService}.
     */
    public static final java.lang.String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";

    /**
     * Maximum sdk build version which needs support for side channeled notifications.
     * Currently the only needed use is for side channeling group children before KITKAT_WATCH.
     */
    static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;

    /**
     * Base time delay for a side channel listener queue retry.
     */
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;

    /**
     * Maximum retries for a side channel listener before dropping tasks.
     */
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;

    /**
     * Hidden field Settings.Secure.ENABLED_NOTIFICATION_LISTENERS
     */
    private static final java.lang.String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    static final int SIDE_CHANNEL_BIND_FLAGS;

    /**
     * Cache of enabled notification listener components
     */
    private static final java.lang.Object sEnabledNotificationListenersLock = new java.lang.Object();

    /**
     * Guarded by {@link #sEnabledNotificationListenersLock}
     */
    private static java.lang.String sEnabledNotificationListeners;

    /**
     * Guarded by {@link #sEnabledNotificationListenersLock}
     */
    private static java.util.Set<java.lang.String> sEnabledNotificationListenerPackages = new java.util.HashSet<java.lang.String>();

    private final android.content.Context mContext;

    private final android.app.NotificationManager mNotificationManager;

    /**
     * Lock for mutable static fields
     */
    private static final java.lang.Object sLock = new java.lang.Object();

    /**
     * Guarded by {@link #sLock}
     */
    private static android.support.v4.app.NotificationManagerCompat.SideChannelManager sSideChannelManager;

    /**
     * Value signifying that the user has not expressed an importance.
     *
     * This value is for persisting preferences, and should never be associated with
     * an actual notification.
     */
    public static final int IMPORTANCE_UNSPECIFIED = -1000;

    /**
     * A notification with no importance: shows nowhere, is blocked.
     */
    public static final int IMPORTANCE_NONE = 0;

    /**
     * Min notification importance: only shows in the shade, below the fold.
     */
    public static final int IMPORTANCE_MIN = 1;

    /**
     * Low notification importance: shows everywhere, but is not intrusive.
     */
    public static final int IMPORTANCE_LOW = 2;

    /**
     * Default notification importance: shows everywhere, allowed to makes noise,
     * but does not visually intrude.
     */
    public static final int IMPORTANCE_DEFAULT = 3;

    /**
     * Higher notification importance: shows everywhere, allowed to makes noise and peek.
     */
    public static final int IMPORTANCE_HIGH = 4;

    /**
     * Highest notification importance: shows everywhere, allowed to makes noise, peek, and
     * use full screen intents.
     */
    public static final int IMPORTANCE_MAX = 5;

    /**
     * Get a {@link NotificationManagerCompat} instance for a provided context.
     */
    public static android.support.v4.app.NotificationManagerCompat from(android.content.Context context) {
        return new android.support.v4.app.NotificationManagerCompat(context);
    }

    private NotificationManagerCompat(android.content.Context context) {
        mContext = context;
        mNotificationManager = ((android.app.NotificationManager) (mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE)));
    }

    private static final android.support.v4.app.NotificationManagerCompat.Impl IMPL;

    interface Impl {
        void cancelNotification(android.app.NotificationManager notificationManager, java.lang.String tag, int id);

        void postNotification(android.app.NotificationManager notificationManager, java.lang.String tag, int id, android.app.Notification notification);

        int getSideChannelBindFlags();

        boolean areNotificationsEnabled(android.content.Context context, android.app.NotificationManager notificationManager);

        int getImportance(android.app.NotificationManager notificationManager);
    }

    static class ImplBase implements android.support.v4.app.NotificationManagerCompat.Impl {
        @java.lang.Override
        public void cancelNotification(android.app.NotificationManager notificationManager, java.lang.String tag, int id) {
            notificationManager.cancel(tag, id);
        }

        @java.lang.Override
        public void postNotification(android.app.NotificationManager notificationManager, java.lang.String tag, int id, android.app.Notification notification) {
            notificationManager.notify(tag, id, notification);
        }

        @java.lang.Override
        public int getSideChannelBindFlags() {
            return android.app.Service.BIND_AUTO_CREATE;
        }

        @java.lang.Override
        public boolean areNotificationsEnabled(android.content.Context context, android.app.NotificationManager notificationManager) {
            return true;
        }

        @java.lang.Override
        public int getImportance(android.app.NotificationManager notificationManager) {
            return android.support.v4.app.NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
        }
    }

    static class ImplIceCreamSandwich extends android.support.v4.app.NotificationManagerCompat.ImplBase {
        @java.lang.Override
        public int getSideChannelBindFlags() {
            return android.support.v4.app.NotificationManagerCompatIceCreamSandwich.SIDE_CHANNEL_BIND_FLAGS;
        }
    }

    static class ImplKitKat extends android.support.v4.app.NotificationManagerCompat.ImplIceCreamSandwich {
        @java.lang.Override
        public boolean areNotificationsEnabled(android.content.Context context, android.app.NotificationManager notificationManager) {
            return android.support.v4.app.NotificationManagerCompatKitKat.areNotificationsEnabled(context);
        }
    }

    static class ImplApi24 extends android.support.v4.app.NotificationManagerCompat.ImplKitKat {
        @java.lang.Override
        public boolean areNotificationsEnabled(android.content.Context context, android.app.NotificationManager notificationManager) {
            return android.support.v4.app.NotificationManagerCompatApi24.areNotificationsEnabled(notificationManager);
        }

        @java.lang.Override
        public int getImportance(android.app.NotificationManager notificationManager) {
            return android.support.v4.app.NotificationManagerCompatApi24.getImportance(notificationManager);
        }
    }

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastN()) {
            IMPL = new android.support.v4.app.NotificationManagerCompat.ImplApi24();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                IMPL = new android.support.v4.app.NotificationManagerCompat.ImplKitKat();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    IMPL = new android.support.v4.app.NotificationManagerCompat.ImplIceCreamSandwich();
                } else {
                    IMPL = new android.support.v4.app.NotificationManagerCompat.ImplBase();
                }


        SIDE_CHANNEL_BIND_FLAGS = android.support.v4.app.NotificationManagerCompat.IMPL.getSideChannelBindFlags();
    }

    /**
     * Cancel a previously shown notification.
     *
     * @param id
     * 		the ID of the notification
     */
    public void cancel(int id) {
        cancel(null, id);
    }

    /**
     * Cancel a previously shown notification.
     *
     * @param tag
     * 		the string identifier of the notification.
     * @param id
     * 		the ID of the notification
     */
    public void cancel(java.lang.String tag, int id) {
        android.support.v4.app.NotificationManagerCompat.IMPL.cancelNotification(mNotificationManager, tag, id);
        if (android.os.Build.VERSION.SDK_INT <= android.support.v4.app.NotificationManagerCompat.MAX_SIDE_CHANNEL_SDK_VERSION) {
            pushSideChannelQueue(new android.support.v4.app.NotificationManagerCompat.CancelTask(mContext.getPackageName(), id, tag));
        }
    }

    /**
     * Cancel all previously shown notifications.
     */
    public void cancelAll() {
        mNotificationManager.cancelAll();
        if (android.os.Build.VERSION.SDK_INT <= android.support.v4.app.NotificationManagerCompat.MAX_SIDE_CHANNEL_SDK_VERSION) {
            pushSideChannelQueue(new android.support.v4.app.NotificationManagerCompat.CancelTask(mContext.getPackageName()));
        }
    }

    /**
     * Post a notification to be shown in the status bar, stream, etc.
     *
     * @param id
     * 		the ID of the notification
     * @param notification
     * 		the notification to post to the system
     */
    public void notify(int id, android.app.Notification notification) {
        notify(null, id, notification);
    }

    /**
     * Post a notification to be shown in the status bar, stream, etc.
     *
     * @param tag
     * 		the string identifier for a notification. Can be {@code null}.
     * @param id
     * 		the ID of the notification. The pair (tag, id) must be unique within your app.
     * @param notification
     * 		the notification to post to the system
     */
    public void notify(java.lang.String tag, int id, android.app.Notification notification) {
        if (android.support.v4.app.NotificationManagerCompat.useSideChannelForNotification(notification)) {
            pushSideChannelQueue(new android.support.v4.app.NotificationManagerCompat.NotifyTask(mContext.getPackageName(), id, tag, notification));
            // Cancel this notification in notification manager if it just transitioned to being
            // side channelled.
            android.support.v4.app.NotificationManagerCompat.IMPL.cancelNotification(mNotificationManager, tag, id);
        } else {
            android.support.v4.app.NotificationManagerCompat.IMPL.postNotification(mNotificationManager, tag, id, notification);
        }
    }

    /**
     * Returns whether notifications from the calling package are not blocked.
     */
    public boolean areNotificationsEnabled() {
        return android.support.v4.app.NotificationManagerCompat.IMPL.areNotificationsEnabled(mContext, mNotificationManager);
    }

    /**
     * Returns the user specified importance for notifications from the calling package.
     *
     * @return An importance level, such as {@link #IMPORTANCE_DEFAULT}.
     */
    public int getImportance() {
        return android.support.v4.app.NotificationManagerCompat.IMPL.getImportance(mNotificationManager);
    }

    /**
     * Get the set of packages that have an enabled notification listener component within them.
     */
    public static java.util.Set<java.lang.String> getEnabledListenerPackages(android.content.Context context) {
        final java.lang.String enabledNotificationListeners = android.provider.Settings.Secure.getString(context.getContentResolver(), android.support.v4.app.NotificationManagerCompat.SETTING_ENABLED_NOTIFICATION_LISTENERS);
        synchronized(android.support.v4.app.NotificationManagerCompat.sEnabledNotificationListenersLock) {
            // Parse the string again if it is different from the last time this method was called.
            if ((enabledNotificationListeners != null) && (!enabledNotificationListeners.equals(android.support.v4.app.NotificationManagerCompat.sEnabledNotificationListeners))) {
                final java.lang.String[] components = enabledNotificationListeners.split(":");
                java.util.Set<java.lang.String> packageNames = new java.util.HashSet<java.lang.String>(components.length);
                for (java.lang.String component : components) {
                    android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(component);
                    if (componentName != null) {
                        packageNames.add(componentName.getPackageName());
                    }
                }
                android.support.v4.app.NotificationManagerCompat.sEnabledNotificationListenerPackages = packageNames;
                android.support.v4.app.NotificationManagerCompat.sEnabledNotificationListeners = enabledNotificationListeners;
            }
            return android.support.v4.app.NotificationManagerCompat.sEnabledNotificationListenerPackages;
        }
    }

    /**
     * Returns true if this notification should use the side channel for delivery.
     */
    private static boolean useSideChannelForNotification(android.app.Notification notification) {
        android.os.Bundle extras = android.support.v4.app.NotificationCompat.getExtras(notification);
        return (extras != null) && extras.getBoolean(android.support.v4.app.NotificationManagerCompat.EXTRA_USE_SIDE_CHANNEL);
    }

    /**
     * Push a notification task for distribution to notification side channels.
     */
    private void pushSideChannelQueue(android.support.v4.app.NotificationManagerCompat.Task task) {
        synchronized(android.support.v4.app.NotificationManagerCompat.sLock) {
            if (android.support.v4.app.NotificationManagerCompat.sSideChannelManager == null) {
                android.support.v4.app.NotificationManagerCompat.sSideChannelManager = new android.support.v4.app.NotificationManagerCompat.SideChannelManager(mContext.getApplicationContext());
            }
            android.support.v4.app.NotificationManagerCompat.sSideChannelManager.queueTask(task);
        }
    }

    /**
     * Helper class to manage a queue of pending tasks to send to notification side channel
     * listeners.
     */
    private static class SideChannelManager implements android.content.ServiceConnection , android.os.Handler.Callback {
        private static final int MSG_QUEUE_TASK = 0;

        private static final int MSG_SERVICE_CONNECTED = 1;

        private static final int MSG_SERVICE_DISCONNECTED = 2;

        private static final int MSG_RETRY_LISTENER_QUEUE = 3;

        private static final java.lang.String KEY_BINDER = "binder";

        private final android.content.Context mContext;

        private final android.os.HandlerThread mHandlerThread;

        private final android.os.Handler mHandler;

        private final java.util.Map<android.content.ComponentName, android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord> mRecordMap = new java.util.HashMap<android.content.ComponentName, android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord>();

        private java.util.Set<java.lang.String> mCachedEnabledPackages = new java.util.HashSet<java.lang.String>();

        public SideChannelManager(android.content.Context context) {
            mContext = context;
            mHandlerThread = new android.os.HandlerThread("NotificationManagerCompat");
            mHandlerThread.start();
            mHandler = new android.os.Handler(mHandlerThread.getLooper(), this);
        }

        /**
         * Queue a new task to be sent to all listeners. This function can be called
         * from any thread.
         */
        public void queueTask(android.support.v4.app.NotificationManagerCompat.Task task) {
            mHandler.obtainMessage(android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_QUEUE_TASK, task).sendToTarget();
        }

        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_QUEUE_TASK :
                    handleQueueTask(((android.support.v4.app.NotificationManagerCompat.Task) (msg.obj)));
                    return true;
                case android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_SERVICE_CONNECTED :
                    android.support.v4.app.NotificationManagerCompat.ServiceConnectedEvent event = ((android.support.v4.app.NotificationManagerCompat.ServiceConnectedEvent) (msg.obj));
                    handleServiceConnected(event.componentName, event.iBinder);
                    return true;
                case android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_SERVICE_DISCONNECTED :
                    handleServiceDisconnected(((android.content.ComponentName) (msg.obj)));
                    return true;
                case android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_RETRY_LISTENER_QUEUE :
                    handleRetryListenerQueue(((android.content.ComponentName) (msg.obj)));
                    return true;
            }
            return false;
        }

        private void handleQueueTask(android.support.v4.app.NotificationManagerCompat.Task task) {
            updateListenerMap();
            for (android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record : mRecordMap.values()) {
                record.taskQueue.add(task);
                processListenerQueue(record);
            }
        }

        private void handleServiceConnected(android.content.ComponentName componentName, android.os.IBinder iBinder) {
            android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record = mRecordMap.get(componentName);
            if (record != null) {
                record.service = INotificationSideChannel.Stub.asInterface(iBinder);
                record.retryCount = 0;
                processListenerQueue(record);
            }
        }

        private void handleServiceDisconnected(android.content.ComponentName componentName) {
            android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record = mRecordMap.get(componentName);
            if (record != null) {
                ensureServiceUnbound(record);
            }
        }

        private void handleRetryListenerQueue(android.content.ComponentName componentName) {
            android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record = mRecordMap.get(componentName);
            if (record != null) {
                processListenerQueue(record);
            }
        }

        @java.lang.Override
        public void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder iBinder) {
            if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, "Connected to service " + componentName);
            }
            mHandler.obtainMessage(android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_SERVICE_CONNECTED, new android.support.v4.app.NotificationManagerCompat.ServiceConnectedEvent(componentName, iBinder)).sendToTarget();
        }

        @java.lang.Override
        public void onServiceDisconnected(android.content.ComponentName componentName) {
            if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, "Disconnected from service " + componentName);
            }
            mHandler.obtainMessage(android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_SERVICE_DISCONNECTED, componentName).sendToTarget();
        }

        /**
         * Check the current list of enabled listener packages and update the records map
         * accordingly.
         */
        private void updateListenerMap() {
            java.util.Set<java.lang.String> enabledPackages = android.support.v4.app.NotificationManagerCompat.getEnabledListenerPackages(mContext);
            if (enabledPackages.equals(mCachedEnabledPackages)) {
                // Short-circuit when the list of enabled packages has not changed.
                return;
            }
            mCachedEnabledPackages = enabledPackages;
            java.util.List<android.content.pm.ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentServices(new android.content.Intent().setAction(android.support.v4.app.NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL), android.content.pm.PackageManager.GET_SERVICES);
            java.util.Set<android.content.ComponentName> enabledComponents = new java.util.HashSet<android.content.ComponentName>();
            for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
                if (!enabledPackages.contains(resolveInfo.serviceInfo.packageName)) {
                    continue;
                }
                android.content.ComponentName componentName = new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                if (resolveInfo.serviceInfo.permission != null) {
                    android.util.Log.w(android.support.v4.app.NotificationManagerCompat.TAG, ("Permission present on component " + componentName) + ", not adding listener record.");
                    continue;
                }
                enabledComponents.add(componentName);
            }
            // Ensure all enabled components have a record in the listener map.
            for (android.content.ComponentName componentName : enabledComponents) {
                if (!mRecordMap.containsKey(componentName)) {
                    if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                        android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, "Adding listener record for " + componentName);
                    }
                    mRecordMap.put(componentName, new android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord(componentName));
                }
            }
            // Remove listener records that are no longer for enabled components.
            java.util.Iterator<java.util.Map.Entry<android.content.ComponentName, android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord>> it = mRecordMap.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<android.content.ComponentName, android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord> entry = it.next();
                if (!enabledComponents.contains(entry.getKey())) {
                    if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                        android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, "Removing listener record for " + entry.getKey());
                    }
                    ensureServiceUnbound(entry.getValue());
                    it.remove();
                }
            } 
        }

        /**
         * Ensure we are already attempting to bind to a service, or start a new binding if not.
         *
         * @return Whether the service bind attempt was successful.
         */
        private boolean ensureServiceBound(android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record) {
            if (record.bound) {
                return true;
            }
            android.content.Intent intent = new android.content.Intent(android.support.v4.app.NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL).setComponent(record.componentName);
            record.bound = mContext.bindService(intent, this, android.support.v4.app.NotificationManagerCompat.SIDE_CHANNEL_BIND_FLAGS);
            if (record.bound) {
                record.retryCount = 0;
            } else {
                android.util.Log.w(android.support.v4.app.NotificationManagerCompat.TAG, "Unable to bind to listener " + record.componentName);
                mContext.unbindService(this);
            }
            return record.bound;
        }

        /**
         * Ensure we have unbound from a service.
         */
        private void ensureServiceUnbound(android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record) {
            if (record.bound) {
                mContext.unbindService(this);
                record.bound = false;
            }
            record.service = null;
        }

        /**
         * Schedule a delayed retry to communicate with a listener service.
         * After a maximum number of attempts (with exponential back-off), start
         * dropping pending tasks for this listener.
         */
        private void scheduleListenerRetry(android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record) {
            if (mHandler.hasMessages(android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_RETRY_LISTENER_QUEUE, record.componentName)) {
                return;
            }
            record.retryCount++;
            if (record.retryCount > android.support.v4.app.NotificationManagerCompat.SIDE_CHANNEL_RETRY_MAX_COUNT) {
                android.util.Log.w(android.support.v4.app.NotificationManagerCompat.TAG, ((((("Giving up on delivering " + record.taskQueue.size()) + " tasks to ") + record.componentName) + " after ") + record.retryCount) + " retries");
                record.taskQueue.clear();
                return;
            }
            int delayMs = android.support.v4.app.NotificationManagerCompat.SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS * (1 << (record.retryCount - 1));
            if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, ("Scheduling retry for " + delayMs) + " ms");
            }
            android.os.Message msg = mHandler.obtainMessage(android.support.v4.app.NotificationManagerCompat.SideChannelManager.MSG_RETRY_LISTENER_QUEUE, record.componentName);
            mHandler.sendMessageDelayed(msg, delayMs);
        }

        /**
         * Perform a processing step for a listener. First check the bind state, then attempt
         * to flush the task queue, and if an error is encountered, schedule a retry.
         */
        private void processListenerQueue(android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord record) {
            if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, ((("Processing component " + record.componentName) + ", ") + record.taskQueue.size()) + " queued tasks");
            }
            if (record.taskQueue.isEmpty()) {
                return;
            }
            if ((!ensureServiceBound(record)) || (record.service == null)) {
                // Ensure bind has started and that a service interface is ready to use.
                scheduleListenerRetry(record);
                return;
            }
            // Attempt to flush all items in the task queue.
            while (true) {
                android.support.v4.app.NotificationManagerCompat.Task task = record.taskQueue.peek();
                if (task == null) {
                    break;
                }
                try {
                    if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                        android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, "Sending task " + task);
                    }
                    task.send(record.service);
                    record.taskQueue.remove();
                } catch (android.os.DeadObjectException e) {
                    if (android.util.Log.isLoggable(android.support.v4.app.NotificationManagerCompat.TAG, android.util.Log.DEBUG)) {
                        android.util.Log.d(android.support.v4.app.NotificationManagerCompat.TAG, "Remote service has died: " + record.componentName);
                    }
                    break;
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(android.support.v4.app.NotificationManagerCompat.TAG, "RemoteException communicating with " + record.componentName, e);
                    break;
                }
            } 
            if (!record.taskQueue.isEmpty()) {
                // Some tasks were not sent, meaning an error was encountered, schedule a retry.
                scheduleListenerRetry(record);
            }
        }

        /**
         * A per-side-channel-service listener state record
         */
        private static class ListenerRecord {
            public final android.content.ComponentName componentName;

            /**
             * Whether the service is currently bound to.
             */
            public boolean bound = false;

            /**
             * The service stub provided by onServiceConnected
             */
            public android.support.v4.app.INotificationSideChannel service;

            /**
             * Queue of pending tasks to send to this listener service
             */
            public java.util.LinkedList<android.support.v4.app.NotificationManagerCompat.Task> taskQueue = new java.util.LinkedList<android.support.v4.app.NotificationManagerCompat.Task>();

            /**
             * Number of retries attempted while connecting to this listener service
             */
            public int retryCount = 0;

            public ListenerRecord(android.content.ComponentName componentName) {
                this.componentName = componentName;
            }
        }
    }

    private static class ServiceConnectedEvent {
        final android.content.ComponentName componentName;

        final android.os.IBinder iBinder;

        public ServiceConnectedEvent(android.content.ComponentName componentName, final android.os.IBinder iBinder) {
            this.componentName = componentName;
            this.iBinder = iBinder;
        }
    }

    private interface Task {
        public void send(android.support.v4.app.INotificationSideChannel service) throws android.os.RemoteException;
    }

    private static class NotifyTask implements android.support.v4.app.NotificationManagerCompat.Task {
        final java.lang.String packageName;

        final int id;

        final java.lang.String tag;

        final android.app.Notification notif;

        public NotifyTask(java.lang.String packageName, int id, java.lang.String tag, android.app.Notification notif) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.notif = notif;
        }

        @java.lang.Override
        public void send(android.support.v4.app.INotificationSideChannel service) throws android.os.RemoteException {
            service.notify(packageName, id, tag, notif);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("NotifyTask[");
            sb.append("packageName:").append(packageName);
            sb.append(", id:").append(id);
            sb.append(", tag:").append(tag);
            sb.append("]");
            return sb.toString();
        }
    }

    private static class CancelTask implements android.support.v4.app.NotificationManagerCompat.Task {
        final java.lang.String packageName;

        final int id;

        final java.lang.String tag;

        final boolean all;

        public CancelTask(java.lang.String packageName) {
            this.packageName = packageName;
            this.id = 0;
            this.tag = null;
            this.all = true;
        }

        public CancelTask(java.lang.String packageName, int id, java.lang.String tag) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.all = false;
        }

        @java.lang.Override
        public void send(android.support.v4.app.INotificationSideChannel service) throws android.os.RemoteException {
            if (all) {
                service.cancelAll(packageName);
            } else {
                service.cancel(packageName, id, tag);
            }
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("CancelTask[");
            sb.append("packageName:").append(packageName);
            sb.append(", id:").append(id);
            sb.append(", tag:").append(tag);
            sb.append(", all:").append(all);
            sb.append("]");
            return sb.toString();
        }
    }
}

