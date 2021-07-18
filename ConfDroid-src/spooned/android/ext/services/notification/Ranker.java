/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.ext.services.notification;


/**
 * Class that provides an updatable ranker module for the notification manager..
 */
public final class Ranker extends android.service.notification.NotificationRankerService {
    private static final java.lang.String TAG = "RocketRanker";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.ext.services.notification.Ranker.TAG, android.util.Log.DEBUG);

    private static final int AUTOBUNDLE_AT_COUNT = 4;

    private static final java.lang.String AUTOBUNDLE_KEY = "ranker_bundle";

    // Map of user : <Map of package : notification keys>. Only contains notifications that are not
    // bundled by the app (aka no group or sort key).
    java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.util.LinkedHashSet<java.lang.String>>> mUnbundledNotifications;

    @java.lang.Override
    public android.service.notification.Adjustment onNotificationEnqueued(android.service.notification.StatusBarNotification sbn, int importance, boolean user) {
        if (android.ext.services.notification.Ranker.DEBUG)
            android.util.Log.i(android.ext.services.notification.Ranker.TAG, "ENQUEUED " + sbn.getKey());

        return null;
    }

    @java.lang.Override
    public void onNotificationPosted(android.service.notification.StatusBarNotification sbn) {
        if (android.ext.services.notification.Ranker.DEBUG)
            android.util.Log.i(android.ext.services.notification.Ranker.TAG, "POSTED " + sbn.getKey());

        try {
            java.util.List<java.lang.String> notificationsToBundle = new java.util.ArrayList<>();
            if (!sbn.isAppGroup()) {
                // Not grouped by the app, add to the list of notifications for the app;
                // send bundling update if app exceeds the autobundling limit.
                synchronized(mUnbundledNotifications) {
                    java.util.Map<java.lang.String, java.util.LinkedHashSet<java.lang.String>> unbundledNotificationsByUser = mUnbundledNotifications.get(sbn.getUserId());
                    if (unbundledNotificationsByUser == null) {
                        unbundledNotificationsByUser = new java.util.HashMap<>();
                    }
                    mUnbundledNotifications.put(sbn.getUserId(), unbundledNotificationsByUser);
                    java.util.LinkedHashSet<java.lang.String> notificationsForPackage = unbundledNotificationsByUser.get(sbn.getPackageName());
                    if (notificationsForPackage == null) {
                        notificationsForPackage = new java.util.LinkedHashSet<>();
                    }
                    notificationsForPackage.add(sbn.getKey());
                    unbundledNotificationsByUser.put(sbn.getPackageName(), notificationsForPackage);
                    if (notificationsForPackage.size() >= android.ext.services.notification.Ranker.AUTOBUNDLE_AT_COUNT) {
                        for (java.lang.String key : notificationsForPackage) {
                            notificationsToBundle.add(key);
                        }
                    }
                }
                if (notificationsToBundle.size() > 0) {
                    adjustAutobundlingSummary(sbn.getPackageName(), notificationsToBundle.get(0), true, sbn.getUserId());
                    adjustNotificationBundling(sbn.getPackageName(), notificationsToBundle, true, sbn.getUserId());
                }
            } else {
                // Grouped, but not by us. Send updates to unautobundle, if we bundled it.
                maybeUnbundle(sbn, false, sbn.getUserId());
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.ext.services.notification.Ranker.TAG, "Failure processing new notification", e);
        }
    }

    @java.lang.Override
    public void onNotificationRemoved(android.service.notification.StatusBarNotification sbn) {
        try {
            maybeUnbundle(sbn, true, sbn.getUserId());
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.ext.services.notification.Ranker.TAG, "Error processing canceled notification", e);
        }
    }

    /**
     * Un-autobundles notifications that are now grouped by the app. Additionally cancels
     * autobundling if the status change of this notification resulted in the loose notification
     * count being under the limit.
     */
    private void maybeUnbundle(android.service.notification.StatusBarNotification sbn, boolean notificationGone, int user) {
        java.util.List<java.lang.String> notificationsToUnAutobundle = new java.util.ArrayList<>();
        boolean removeSummary = false;
        synchronized(mUnbundledNotifications) {
            java.util.Map<java.lang.String, java.util.LinkedHashSet<java.lang.String>> unbundledNotificationsByUser = mUnbundledNotifications.get(sbn.getUserId());
            if ((unbundledNotificationsByUser == null) || (unbundledNotificationsByUser.size() == 0)) {
                return;
            }
            java.util.LinkedHashSet<java.lang.String> notificationsForPackage = unbundledNotificationsByUser.get(sbn.getPackageName());
            if ((notificationsForPackage == null) || (notificationsForPackage.size() == 0)) {
                return;
            }
            if (notificationsForPackage.remove(sbn.getKey())) {
                if (!notificationGone) {
                    // Add the current notification to the unbundling list if it still exists.
                    notificationsToUnAutobundle.add(sbn.getKey());
                }
                // If the status change of this notification has brought the number of loose
                // notifications back below the limit, remove the summary and un-autobundle.
                if (notificationsForPackage.size() == (android.ext.services.notification.Ranker.AUTOBUNDLE_AT_COUNT - 1)) {
                    removeSummary = true;
                    for (java.lang.String key : notificationsForPackage) {
                        notificationsToUnAutobundle.add(key);
                    }
                }
            }
        }
        if (notificationsToUnAutobundle.size() > 0) {
            if (removeSummary) {
                adjustAutobundlingSummary(sbn.getPackageName(), null, false, user);
            }
            adjustNotificationBundling(sbn.getPackageName(), notificationsToUnAutobundle, false, user);
        }
    }

    @java.lang.Override
    public void onListenerConnected() {
        if (android.ext.services.notification.Ranker.DEBUG)
            android.util.Log.i(android.ext.services.notification.Ranker.TAG, "CONNECTED");

        mUnbundledNotifications = new java.util.HashMap<>();
        for (android.service.notification.StatusBarNotification sbn : getActiveNotifications()) {
            onNotificationPosted(sbn);
        }
    }

    private void adjustAutobundlingSummary(java.lang.String packageName, java.lang.String key, boolean summaryNeeded, int user) {
        android.os.Bundle signals = new android.os.Bundle();
        if (summaryNeeded) {
            signals.putBoolean(android.service.notification.Adjustment.NEEDS_AUTOGROUPING_KEY, true);
            signals.putString(android.service.notification.Adjustment.GROUP_KEY_OVERRIDE_KEY, android.ext.services.notification.Ranker.AUTOBUNDLE_KEY);
        } else {
            signals.putBoolean(android.service.notification.Adjustment.NEEDS_AUTOGROUPING_KEY, false);
        }
        android.service.notification.Adjustment adjustment = new android.service.notification.Adjustment(packageName, key, android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_UNSPECIFIED, signals, getContext().getString(R.string.notification_ranker_autobundle_explanation), null, user);
        if (android.ext.services.notification.Ranker.DEBUG) {
            android.util.Log.i(android.ext.services.notification.Ranker.TAG, (("Summary update for: " + packageName) + " ") + (summaryNeeded ? "adding" : "removing"));
        }
        try {
            adjustNotification(adjustment);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.ext.services.notification.Ranker.TAG, "Adjustment failed", e);
        }
    }

    private void adjustNotificationBundling(java.lang.String packageName, java.util.List<java.lang.String> keys, boolean bundle, int user) {
        java.util.List<android.service.notification.Adjustment> adjustments = new java.util.ArrayList<>();
        for (java.lang.String key : keys) {
            adjustments.add(createBundlingAdjustment(packageName, key, bundle, user));
            if (android.ext.services.notification.Ranker.DEBUG)
                android.util.Log.i(android.ext.services.notification.Ranker.TAG, "Sending bundling adjustment for: " + key);

        }
        try {
            adjustNotifications(adjustments);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.ext.services.notification.Ranker.TAG, "Adjustments failed", e);
        }
    }

    private android.service.notification.Adjustment createBundlingAdjustment(java.lang.String packageName, java.lang.String key, boolean bundle, int user) {
        android.os.Bundle signals = new android.os.Bundle();
        if (bundle) {
            signals.putString(android.service.notification.Adjustment.GROUP_KEY_OVERRIDE_KEY, android.ext.services.notification.Ranker.AUTOBUNDLE_KEY);
        } else {
            signals.putString(android.service.notification.Adjustment.GROUP_KEY_OVERRIDE_KEY, null);
        }
        return new android.service.notification.Adjustment(packageName, key, android.service.notification.NotificationListenerService.Ranking.IMPORTANCE_UNSPECIFIED, signals, getContext().getString(R.string.notification_ranker_autobundle_explanation), null, user);
    }
}

