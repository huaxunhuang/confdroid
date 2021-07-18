/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.app;


/**
 * Class to notify the user of events that happen.  This is how you tell
 * the user that something has happened in the background. {@more }
 *
 * Notifications can take different forms:
 * <ul>
 *      <li>A persistent icon that goes in the status bar and is accessible
 *          through the launcher, (when the user selects it, a designated Intent
 *          can be launched),</li>
 *      <li>Turning on or flashing LEDs on the device, or</li>
 *      <li>Alerting the user by flashing the backlight, playing a sound,
 *          or vibrating.</li>
 * </ul>
 *
 * <p>
 * Each of the notify methods takes an int id parameter and optionally a
 * {@link String} tag parameter, which may be {@code null}.  These parameters
 * are used to form a pair (tag, id), or ({@code null}, id) if tag is
 * unspecified.  This pair identifies this notification from your app to the
 * system, so that pair should be unique within your app.  If you call one
 * of the notify methods with a (tag, id) pair that is currently active and
 * a new set of notification parameters, it will be updated.  For example,
 * if you pass a new status bar icon, the old icon in the status bar will
 * be replaced with the new one.  This is also the same tag and id you pass
 * to the {@link #cancel(int)} or {@link #cancel(String, int)} method to clear
 * this notification.
 *
 * <p>
 * You do not instantiate this class directly; instead, retrieve it through
 * {@link android.content.Context#getSystemService}.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For a guide to creating notifications, read the
 * <a href="{@docRoot }guide/topics/ui/notifiers/notifications.html">Status Bar Notifications</a>
 * developer guide.</p>
 * </div>
 *
 * @see android.app.Notification
 * @see android.content.Context#getSystemService
 */
public class NotificationManager {
    private static java.lang.String TAG = "NotificationManager";

    private static boolean localLOGV = false;

    /**
     * Intent that is broadcast when the state of {@link #getEffectsSuppressor()} changes.
     * This broadcast is only sent to registered receivers.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_EFFECTS_SUPPRESSOR_CHANGED = "android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED";

    /**
     * Intent that is broadcast when the state of {@link #isNotificationPolicyAccessGranted()}
     * changes.
     *
     * This broadcast is only sent to registered receivers, and only to the apps that have changed.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED = "android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED";

    /**
     * Intent that is broadcast when the state of getNotificationPolicy() changes.
     * This broadcast is only sent to registered receivers.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_NOTIFICATION_POLICY_CHANGED = "android.app.action.NOTIFICATION_POLICY_CHANGED";

    /**
     * Intent that is broadcast when the state of getCurrentInterruptionFilter() changes.
     * This broadcast is only sent to registered receivers.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_INTERRUPTION_FILTER_CHANGED = "android.app.action.INTERRUPTION_FILTER_CHANGED";

    /**
     * Intent that is broadcast when the state of getCurrentInterruptionFilter() changes.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_INTERRUPTION_FILTER_CHANGED_INTERNAL = "android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.app.NotificationManager.INTERRUPTION_FILTER_NONE, android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY, android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS, android.app.NotificationManager.INTERRUPTION_FILTER_ALL, android.app.NotificationManager.INTERRUPTION_FILTER_UNKNOWN })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface InterruptionFilter {}

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     Normal interruption filter - no notifications are suppressed.
     */
    public static final int INTERRUPTION_FILTER_ALL = 1;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     Priority interruption filter - all notifications are suppressed except those that match
     *     the priority criteria. Some audio streams are muted. See
     *     {@link Policy#priorityCallSenders}, {@link Policy#priorityCategories},
     *     {@link Policy#priorityMessageSenders} to define or query this criteria. Users can
     *     additionally specify packages that can bypass this interruption filter.
     */
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     No interruptions filter - all notifications are suppressed and all audio streams (except
     *     those used for phone calls) and vibrations are muted.
     */
    public static final int INTERRUPTION_FILTER_NONE = 3;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant -
     *     Alarms only interruption filter - all notifications except those of category
     *     {@link Notification#CATEGORY_ALARM} are suppressed. Some audio streams are muted.
     */
    public static final int INTERRUPTION_FILTER_ALARMS = 4;

    /**
     * {@link #getCurrentInterruptionFilter() Interruption filter} constant - returned when
     * the value is unavailable for any reason.
     */
    public static final int INTERRUPTION_FILTER_UNKNOWN = 0;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.app.NotificationManager.VISIBILITY_NO_OVERRIDE, android.app.NotificationManager.IMPORTANCE_UNSPECIFIED, android.app.NotificationManager.IMPORTANCE_NONE, android.app.NotificationManager.IMPORTANCE_MIN, android.app.NotificationManager.IMPORTANCE_LOW, android.app.NotificationManager.IMPORTANCE_DEFAULT, android.app.NotificationManager.IMPORTANCE_HIGH, android.app.NotificationManager.IMPORTANCE_MAX })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Importance {}

    /**
     * Value signifying that the user has not expressed a per-app visibility override value.
     *
     * @unknown 
     */
    public static final int VISIBILITY_NO_OVERRIDE = -1000;

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

    private static android.app.INotificationManager sService;

    /**
     *
     *
     * @unknown 
     */
    public static android.app.INotificationManager getService() {
        if (android.app.NotificationManager.sService != null) {
            return android.app.NotificationManager.sService;
        }
        android.os.IBinder b = android.os.ServiceManager.getService("notification");
        android.app.NotificationManager.sService = INotificationManager.Stub.asInterface(b);
        return android.app.NotificationManager.sService;
    }

    /* package */
    NotificationManager(android.content.Context context, android.os.Handler handler) {
        mContext = context;
    }

    /**
     * {@hide }
     */
    public static android.app.NotificationManager from(android.content.Context context) {
        return ((android.app.NotificationManager) (context.getSystemService(android.content.Context.NOTIFICATION_SERVICE)));
    }

    /**
     * Post a notification to be shown in the status bar. If a notification with
     * the same id has already been posted by your application and has not yet been canceled, it
     * will be replaced by the updated information.
     *
     * @param id
     * 		An identifier for this notification unique within your
     * 		application.
     * @param notification
     * 		A {@link Notification} object describing what to show the user. Must not
     * 		be null.
     */
    public void notify(int id, android.app.Notification notification) {
        notify(null, id, notification);
    }

    /**
     * Post a notification to be shown in the status bar. If a notification with
     * the same tag and id has already been posted by your application and has not yet been
     * canceled, it will be replaced by the updated information.
     *
     * @param tag
     * 		A string identifier for this notification.  May be {@code null}.
     * @param id
     * 		An identifier for this notification.  The pair (tag, id) must be unique
     * 		within your application.
     * @param notification
     * 		A {@link Notification} object describing what to
     * 		show the user. Must not be null.
     */
    public void notify(java.lang.String tag, int id, android.app.Notification notification) {
        notifyAsUser(tag, id, notification, new android.os.UserHandle(android.os.UserHandle.myUserId()));
    }

    /**
     *
     *
     * @unknown 
     */
    public void notifyAsUser(java.lang.String tag, int id, android.app.Notification notification, android.os.UserHandle user) {
        int[] idOut = new int[1];
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        java.lang.String pkg = mContext.getPackageName();
        // Fix the notification as best we can.
        android.app.Notification.addFieldsFromContext(mContext, notification);
        if (notification.sound != null) {
            notification.sound = notification.sound.getCanonicalUri();
            if (android.os.StrictMode.vmFileUriExposureEnabled()) {
                notification.sound.checkFileUriExposed("Notification.sound");
            }
        }
        fixLegacySmallIcon(notification, pkg);
        if (mContext.getApplicationInfo().targetSdkVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (notification.getSmallIcon() == null) {
                throw new java.lang.IllegalArgumentException("Invalid notification (no valid small icon): " + notification);
            }
        }
        if (android.app.NotificationManager.localLOGV)
            android.util.Log.v(android.app.NotificationManager.TAG, ((((pkg + ": notify(") + id) + ", ") + notification) + ")");

        final android.app.Notification copy = android.app.Notification.Builder.maybeCloneStrippedForDelivery(notification);
        try {
            service.enqueueNotificationWithTag(pkg, mContext.getOpPackageName(), tag, id, copy, idOut, user.getIdentifier());
            if (android.app.NotificationManager.localLOGV && (id != idOut[0])) {
                android.util.Log.v(android.app.NotificationManager.TAG, (("notify: id corrupted: sent " + id) + ", got back ") + idOut[0]);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void fixLegacySmallIcon(android.app.Notification n, java.lang.String pkg) {
        if ((n.getSmallIcon() == null) && (n.icon != 0)) {
            n.setSmallIcon(android.graphics.drawable.Icon.createWithResource(pkg, n.icon));
        }
    }

    /**
     * Cancel a previously shown notification.  If it's transient, the view
     * will be hidden.  If it's persistent, it will be removed from the status
     * bar.
     */
    public void cancel(int id) {
        cancel(null, id);
    }

    /**
     * Cancel a previously shown notification.  If it's transient, the view
     * will be hidden.  If it's persistent, it will be removed from the status
     * bar.
     */
    public void cancel(java.lang.String tag, int id) {
        cancelAsUser(tag, id, new android.os.UserHandle(android.os.UserHandle.myUserId()));
    }

    /**
     *
     *
     * @unknown 
     */
    public void cancelAsUser(java.lang.String tag, int id, android.os.UserHandle user) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        java.lang.String pkg = mContext.getPackageName();
        if (android.app.NotificationManager.localLOGV)
            android.util.Log.v(android.app.NotificationManager.TAG, ((pkg + ": cancel(") + id) + ")");

        try {
            service.cancelNotificationWithTag(pkg, tag, id, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Cancel all previously shown notifications. See {@link #cancel} for the
     * detailed behavior.
     */
    public void cancelAll() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        java.lang.String pkg = mContext.getPackageName();
        if (android.app.NotificationManager.localLOGV)
            android.util.Log.v(android.app.NotificationManager.TAG, pkg + ": cancelAll()");

        try {
            service.cancelAllNotifications(pkg, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.ComponentName getEffectsSuppressor() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getEffectsSuppressor();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean matchesCallFilter(android.os.Bundle extras) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.matchesCallFilter(extras);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isSystemConditionProviderEnabled(java.lang.String path) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.isSystemConditionProviderEnabled(path);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setZenMode(int mode, android.net.Uri conditionId, java.lang.String reason) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            service.setZenMode(mode, conditionId, reason);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int getZenMode() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getZenMode();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.service.notification.ZenModeConfig getZenModeConfig() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getZenModeConfig();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int getRuleInstanceCount(android.content.ComponentName owner) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getRuleInstanceCount(owner);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns AutomaticZenRules owned by the caller.
     *
     * <p>
     * Throws a SecurityException if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     */
    public java.util.Map<java.lang.String, android.app.AutomaticZenRule> getAutomaticZenRules() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            java.util.List<android.service.notification.ZenModeConfig.ZenRule> rules = service.getZenRules();
            java.util.Map<java.lang.String, android.app.AutomaticZenRule> ruleMap = new java.util.HashMap<>();
            for (android.service.notification.ZenModeConfig.ZenRule rule : rules) {
                ruleMap.put(rule.id, new android.app.AutomaticZenRule(rule.name, rule.component, rule.conditionId, android.app.NotificationManager.zenModeToInterruptionFilter(rule.zenMode), rule.enabled, rule.creationTime));
            }
            return ruleMap;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the AutomaticZenRule with the given id, if it exists and the caller has access.
     *
     * <p>
     * Throws a SecurityException if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     *
     * <p>
     * Returns null if there are no zen rules that match the given id, or if the calling package
     * doesn't own the matching rule. See {@link AutomaticZenRule#getOwner}.
     */
    public android.app.AutomaticZenRule getAutomaticZenRule(java.lang.String id) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getAutomaticZenRule(id);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Creates the given zen rule.
     *
     * <p>
     * Throws a SecurityException if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     *
     * @param automaticZenRule
     * 		the rule to create.
     * @return The id of the newly created rule; null if the rule could not be created.
     */
    public java.lang.String addAutomaticZenRule(android.app.AutomaticZenRule automaticZenRule) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.addAutomaticZenRule(automaticZenRule);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Updates the given zen rule.
     *
     * <p>
     * Throws a SecurityException if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     *
     * <p>
     * Callers can only update rules that they own. See {@link AutomaticZenRule#getOwner}.
     *
     * @param id
     * 		The id of the rule to update
     * @param automaticZenRule
     * 		the rule to update.
     * @return Whether the rule was successfully updated.
     */
    public boolean updateAutomaticZenRule(java.lang.String id, android.app.AutomaticZenRule automaticZenRule) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.updateAutomaticZenRule(id, automaticZenRule);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Deletes the automatic zen rule with the given id.
     *
     * <p>
     * Throws a SecurityException if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     *
     * <p>
     * Callers can only delete rules that they own. See {@link AutomaticZenRule#getOwner}.
     *
     * @param id
     * 		the id of the rule to delete.
     * @return Whether the rule was successfully deleted.
     */
    public boolean removeAutomaticZenRule(java.lang.String id) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.removeAutomaticZenRule(id);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Deletes all automatic zen rules owned by the given package.
     *
     * @unknown 
     */
    public boolean removeAutomaticZenRules(java.lang.String packageName) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.removeAutomaticZenRules(packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the user specified importance for notifications from the calling package.
     *
     * @return An importance level, such as {@link #IMPORTANCE_DEFAULT}.
     */
    @android.app.NotificationManager.Importance
    public int getImportance() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getPackageImportance(mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns whether notifications from the calling package are blocked.
     */
    public boolean areNotificationsEnabled() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.areNotificationsEnabled(mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Checks the ability to read/modify notification policy for the calling package.
     *
     * <p>
     * Returns true if the calling package can read/modify notification policy.
     *
     * <p>
     * Request policy access by sending the user to the activity that matches the system intent
     * action {@link android.provider.Settings#ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS}.
     *
     * <p>
     * Use {@link #ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED} to listen for
     * user grant or denial of this access.
     */
    public boolean isNotificationPolicyAccessGranted() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.isNotificationPolicyAccessGranted(mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isNotificationPolicyAccessGrantedForPackage(java.lang.String pkg) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.isNotificationPolicyAccessGrantedForPackage(pkg);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the current notification policy.
     *
     * <p>
     * Only available if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     */
    public android.app.NotificationManager.Policy getNotificationPolicy() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return service.getNotificationPolicy(mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the current notification policy.
     *
     * <p>
     * Only available if policy access is granted to this package.
     * See {@link #isNotificationPolicyAccessGranted}.
     *
     * @param policy
     * 		The new desired policy.
     */
    public void setNotificationPolicy(@android.annotation.NonNull
    android.app.NotificationManager.Policy policy) {
        android.app.NotificationManager.checkRequired("policy", policy);
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            service.setNotificationPolicy(mContext.getOpPackageName(), policy);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setNotificationPolicyAccessGranted(java.lang.String pkg, boolean granted) {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            service.setNotificationPolicyAccessGranted(pkg, granted);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.util.ArraySet<java.lang.String> getPackagesRequestingNotificationPolicyAccess() {
        android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            final java.lang.String[] pkgs = service.getPackagesRequestingNotificationPolicyAccess();
            if ((pkgs != null) && (pkgs.length > 0)) {
                final android.util.ArraySet<java.lang.String> rt = new android.util.ArraySet<>(pkgs.length);
                for (int i = 0; i < pkgs.length; i++) {
                    rt.add(pkgs[i]);
                }
                return rt;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        return new android.util.ArraySet<>();
    }

    private android.content.Context mContext;

    private static void checkRequired(java.lang.String name, java.lang.Object value) {
        if (value == null) {
            throw new java.lang.IllegalArgumentException(name + " is required");
        }
    }

    /**
     * Notification policy configuration.  Represents user-preferences for notification
     * filtering.
     */
    public static class Policy implements android.os.Parcelable {
        /**
         * Reminder notifications are prioritized.
         */
        public static final int PRIORITY_CATEGORY_REMINDERS = 1 << 0;

        /**
         * Event notifications are prioritized.
         */
        public static final int PRIORITY_CATEGORY_EVENTS = 1 << 1;

        /**
         * Message notifications are prioritized.
         */
        public static final int PRIORITY_CATEGORY_MESSAGES = 1 << 2;

        /**
         * Calls are prioritized.
         */
        public static final int PRIORITY_CATEGORY_CALLS = 1 << 3;

        /**
         * Calls from repeat callers are prioritized.
         */
        public static final int PRIORITY_CATEGORY_REPEAT_CALLERS = 1 << 4;

        private static final int[] ALL_PRIORITY_CATEGORIES = new int[]{ android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS, android.app.NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS, android.app.NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES, android.app.NotificationManager.Policy.PRIORITY_CATEGORY_CALLS, android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REPEAT_CALLERS };

        /**
         * Any sender is prioritized.
         */
        public static final int PRIORITY_SENDERS_ANY = 0;

        /**
         * Saved contacts are prioritized.
         */
        public static final int PRIORITY_SENDERS_CONTACTS = 1;

        /**
         * Only starred contacts are prioritized.
         */
        public static final int PRIORITY_SENDERS_STARRED = 2;

        /**
         * Notification categories to prioritize. Bitmask of PRIORITY_CATEGORY_* constants.
         */
        public final int priorityCategories;

        /**
         * Notification senders to prioritize for calls. One of:
         * PRIORITY_SENDERS_ANY, PRIORITY_SENDERS_CONTACTS, PRIORITY_SENDERS_STARRED
         */
        public final int priorityCallSenders;

        /**
         * Notification senders to prioritize for messages. One of:
         * PRIORITY_SENDERS_ANY, PRIORITY_SENDERS_CONTACTS, PRIORITY_SENDERS_STARRED
         */
        public final int priorityMessageSenders;

        /**
         *
         *
         * @unknown 
         */
        public static final int SUPPRESSED_EFFECTS_UNSET = -1;

        /**
         * Whether notifications suppressed by DND should not interrupt visually (e.g. with
         * notification lights or by turning the screen on) when the screen is off.
         */
        public static final int SUPPRESSED_EFFECT_SCREEN_OFF = 1 << 0;

        /**
         * Whether notifications suppressed by DND should not interrupt visually when the screen
         * is on (e.g. by peeking onto the screen).
         */
        public static final int SUPPRESSED_EFFECT_SCREEN_ON = 1 << 1;

        private static final int[] ALL_SUPPRESSED_EFFECTS = new int[]{ android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_OFF, android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_ON };

        /**
         * Visual effects to suppress for a notification that is filtered by Do Not Disturb mode.
         * Bitmask of SUPPRESSED_EFFECT_* constants.
         */
        public final int suppressedVisualEffects;

        /**
         * Constructs a policy for Do Not Disturb priority mode behavior.
         *
         * @param priorityCategories
         * 		bitmask of categories of notifications that can bypass DND.
         * @param priorityCallSenders
         * 		which callers can bypass DND.
         * @param priorityMessageSenders
         * 		which message senders can bypass DND.
         */
        public Policy(int priorityCategories, int priorityCallSenders, int priorityMessageSenders) {
            this(priorityCategories, priorityCallSenders, priorityMessageSenders, android.app.NotificationManager.Policy.SUPPRESSED_EFFECTS_UNSET);
        }

        /**
         * Constructs a policy for Do Not Disturb priority mode behavior.
         *
         * @param priorityCategories
         * 		bitmask of categories of notifications that can bypass DND.
         * @param priorityCallSenders
         * 		which callers can bypass DND.
         * @param priorityMessageSenders
         * 		which message senders can bypass DND.
         * @param suppressedVisualEffects
         * 		which visual interruptions should be suppressed from
         * 		notifications that are filtered by DND.
         */
        public Policy(int priorityCategories, int priorityCallSenders, int priorityMessageSenders, int suppressedVisualEffects) {
            this.priorityCategories = priorityCategories;
            this.priorityCallSenders = priorityCallSenders;
            this.priorityMessageSenders = priorityMessageSenders;
            this.suppressedVisualEffects = suppressedVisualEffects;
        }

        /**
         *
         *
         * @unknown 
         */
        public Policy(android.os.Parcel source) {
            this(source.readInt(), source.readInt(), source.readInt(), source.readInt());
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(priorityCategories);
            dest.writeInt(priorityCallSenders);
            dest.writeInt(priorityMessageSenders);
            dest.writeInt(suppressedVisualEffects);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(priorityCategories, priorityCallSenders, priorityMessageSenders, suppressedVisualEffects);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.app.NotificationManager.Policy))
                return false;

            if (o == this)
                return true;

            final android.app.NotificationManager.Policy other = ((android.app.NotificationManager.Policy) (o));
            return (((other.priorityCategories == priorityCategories) && (other.priorityCallSenders == priorityCallSenders)) && (other.priorityMessageSenders == priorityMessageSenders)) && (other.suppressedVisualEffects == suppressedVisualEffects);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((("NotificationManager.Policy[" + "priorityCategories=") + android.app.NotificationManager.Policy.priorityCategoriesToString(priorityCategories)) + ",priorityCallSenders=") + android.app.NotificationManager.Policy.prioritySendersToString(priorityCallSenders)) + ",priorityMessageSenders=") + android.app.NotificationManager.Policy.prioritySendersToString(priorityMessageSenders)) + ",suppressedVisualEffects=") + android.app.NotificationManager.Policy.suppressedEffectsToString(suppressedVisualEffects)) + "]";
        }

        public static java.lang.String suppressedEffectsToString(int effects) {
            if (effects <= 0)
                return "";

            final java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (int i = 0; i < android.app.NotificationManager.Policy.ALL_SUPPRESSED_EFFECTS.length; i++) {
                final int effect = android.app.NotificationManager.Policy.ALL_SUPPRESSED_EFFECTS[i];
                if ((effects & effect) != 0) {
                    if (sb.length() > 0)
                        sb.append(',');

                    sb.append(android.app.NotificationManager.Policy.effectToString(effect));
                }
                effects &= ~effect;
            }
            if (effects != 0) {
                if (sb.length() > 0)
                    sb.append(',');

                sb.append("UNKNOWN_").append(effects);
            }
            return sb.toString();
        }

        public static java.lang.String priorityCategoriesToString(int priorityCategories) {
            if (priorityCategories == 0)
                return "";

            final java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (int i = 0; i < android.app.NotificationManager.Policy.ALL_PRIORITY_CATEGORIES.length; i++) {
                final int priorityCategory = android.app.NotificationManager.Policy.ALL_PRIORITY_CATEGORIES[i];
                if ((priorityCategories & priorityCategory) != 0) {
                    if (sb.length() > 0)
                        sb.append(',');

                    sb.append(android.app.NotificationManager.Policy.priorityCategoryToString(priorityCategory));
                }
                priorityCategories &= ~priorityCategory;
            }
            if (priorityCategories != 0) {
                if (sb.length() > 0)
                    sb.append(',');

                sb.append("PRIORITY_CATEGORY_UNKNOWN_").append(priorityCategories);
            }
            return sb.toString();
        }

        private static java.lang.String effectToString(int effect) {
            switch (effect) {
                case android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_OFF :
                    return "SUPPRESSED_EFFECT_SCREEN_OFF";
                case android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_ON :
                    return "SUPPRESSED_EFFECT_SCREEN_ON";
                case android.app.NotificationManager.Policy.SUPPRESSED_EFFECTS_UNSET :
                    return "SUPPRESSED_EFFECTS_UNSET";
                default :
                    return "UNKNOWN_" + effect;
            }
        }

        private static java.lang.String priorityCategoryToString(int priorityCategory) {
            switch (priorityCategory) {
                case android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS :
                    return "PRIORITY_CATEGORY_REMINDERS";
                case android.app.NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS :
                    return "PRIORITY_CATEGORY_EVENTS";
                case android.app.NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES :
                    return "PRIORITY_CATEGORY_MESSAGES";
                case android.app.NotificationManager.Policy.PRIORITY_CATEGORY_CALLS :
                    return "PRIORITY_CATEGORY_CALLS";
                case android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REPEAT_CALLERS :
                    return "PRIORITY_CATEGORY_REPEAT_CALLERS";
                default :
                    return "PRIORITY_CATEGORY_UNKNOWN_" + priorityCategory;
            }
        }

        public static java.lang.String prioritySendersToString(int prioritySenders) {
            switch (prioritySenders) {
                case android.app.NotificationManager.Policy.PRIORITY_SENDERS_ANY :
                    return "PRIORITY_SENDERS_ANY";
                case android.app.NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS :
                    return "PRIORITY_SENDERS_CONTACTS";
                case android.app.NotificationManager.Policy.PRIORITY_SENDERS_STARRED :
                    return "PRIORITY_SENDERS_STARRED";
                default :
                    return "PRIORITY_SENDERS_UNKNOWN_" + prioritySenders;
            }
        }

        public static final android.os.Parcelable.Creator<android.app.NotificationManager.Policy> CREATOR = new android.os.Parcelable.Creator<android.app.NotificationManager.Policy>() {
            @java.lang.Override
            public android.app.NotificationManager.Policy createFromParcel(android.os.Parcel in) {
                return new android.app.NotificationManager.Policy(in);
            }

            @java.lang.Override
            public android.app.NotificationManager.Policy[] newArray(int size) {
                return new android.app.NotificationManager.Policy[size];
            }
        };
    }

    /**
     * Recover a list of active notifications: ones that have been posted by the calling app that
     * have not yet been dismissed by the user or {@link #cancel(String, int)}ed by the app.
     *
     * Each notification is embedded in a {@link StatusBarNotification} object, including the
     * original <code>tag</code> and <code>id</code> supplied to
     * {@link #notify(String, int, Notification) notify()}
     * (via {@link StatusBarNotification#getTag() getTag()} and
     * {@link StatusBarNotification#getId() getId()}) as well as a copy of the original
     * {@link Notification} object (via {@link StatusBarNotification#getNotification()}).
     *
     * @return An array of {@link StatusBarNotification}.
     */
    public android.service.notification.StatusBarNotification[] getActiveNotifications() {
        final android.app.INotificationManager service = android.app.NotificationManager.getService();
        final java.lang.String pkg = mContext.getPackageName();
        try {
            final android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> parceledList = service.getAppActiveNotifications(pkg, android.os.UserHandle.myUserId());
            final java.util.List<android.service.notification.StatusBarNotification> list = parceledList.getList();
            return list.toArray(new android.service.notification.StatusBarNotification[list.size()]);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the current notification interruption filter.
     *
     * <p>
     * The interruption filter defines which notifications are allowed to interrupt the user
     * (e.g. via sound &amp; vibration) and is applied globally.
     *
     * @return One of the INTERRUPTION_FILTER_ constants, or INTERRUPTION_FILTER_UNKNOWN when
    unavailable.
     */
    @android.app.NotificationManager.InterruptionFilter
    public final int getCurrentInterruptionFilter() {
        final android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            return android.app.NotificationManager.zenModeToInterruptionFilter(service.getZenMode());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the current notification interruption filter.
     *
     * <p>
     * The interruption filter defines which notifications are allowed to interrupt the user
     * (e.g. via sound &amp; vibration) and is applied globally.
     *
     * @return One of the INTERRUPTION_FILTER_ constants, or INTERRUPTION_FILTER_UNKNOWN when
    unavailable.

    <p>
    Only available if policy access is granted to this package.
    See {@link #isNotificationPolicyAccessGranted}.
     */
    public final void setInterruptionFilter(int interruptionFilter) {
        final android.app.INotificationManager service = android.app.NotificationManager.getService();
        try {
            service.setInterruptionFilter(mContext.getOpPackageName(), interruptionFilter);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static int zenModeToInterruptionFilter(int zen) {
        switch (zen) {
            case android.provider.Settings.Global.ZEN_MODE_OFF :
                return android.app.NotificationManager.INTERRUPTION_FILTER_ALL;
            case android.provider.Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS :
                return android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY;
            case android.provider.Settings.Global.ZEN_MODE_ALARMS :
                return android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS;
            case android.provider.Settings.Global.ZEN_MODE_NO_INTERRUPTIONS :
                return android.app.NotificationManager.INTERRUPTION_FILTER_NONE;
            default :
                return android.app.NotificationManager.INTERRUPTION_FILTER_UNKNOWN;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static int zenModeFromInterruptionFilter(int interruptionFilter, int defValue) {
        switch (interruptionFilter) {
            case android.app.NotificationManager.INTERRUPTION_FILTER_ALL :
                return android.provider.Settings.Global.ZEN_MODE_OFF;
            case android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY :
                return android.provider.Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS;
            case android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS :
                return android.provider.Settings.Global.ZEN_MODE_ALARMS;
            case android.app.NotificationManager.INTERRUPTION_FILTER_NONE :
                return android.provider.Settings.Global.ZEN_MODE_NO_INTERRUPTIONS;
            default :
                return defValue;
        }
    }
}

