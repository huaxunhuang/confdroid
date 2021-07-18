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
 * limitations under the License
 */
package android.net;


/**
 * Internal class for managing the primary network scorer application.
 *
 * TODO: Rename this to something more generic.
 *
 * @unknown 
 */
public final class NetworkScorerAppManager {
    private static final java.lang.String TAG = "NetworkScorerAppManager";

    private static final android.content.Intent SCORE_INTENT = new android.content.Intent(android.net.NetworkScoreManager.ACTION_SCORE_NETWORKS);

    /**
     * This class cannot be instantiated.
     */
    private NetworkScorerAppManager() {
    }

    public static class NetworkScorerAppData {
        /**
         * Package name of this scorer app.
         */
        public final java.lang.String mPackageName;

        /**
         * UID of the scorer app.
         */
        public final int mPackageUid;

        /**
         * Name of this scorer app for display.
         */
        public final java.lang.CharSequence mScorerName;

        /**
         * Optional class name of a configuration activity. Null if none is set.
         *
         * @see NetworkScoreManager#ACTION_CUSTOM_ENABLE
         */
        public final java.lang.String mConfigurationActivityClassName;

        /**
         * Optional class name of the scoring service we can bind to. Null if none is set.
         */
        public final java.lang.String mScoringServiceClassName;

        public NetworkScorerAppData(java.lang.String packageName, int packageUid, java.lang.CharSequence scorerName, @android.annotation.Nullable
        java.lang.String configurationActivityClassName, @android.annotation.Nullable
        java.lang.String scoringServiceClassName) {
            mScorerName = scorerName;
            mPackageName = packageName;
            mPackageUid = packageUid;
            mConfigurationActivityClassName = configurationActivityClassName;
            mScoringServiceClassName = scoringServiceClassName;
        }

        @java.lang.Override
        public java.lang.String toString() {
            final java.lang.StringBuilder sb = new java.lang.StringBuilder("NetworkScorerAppData{");
            sb.append("mPackageName='").append(mPackageName).append('\'');
            sb.append(", mPackageUid=").append(mPackageUid);
            sb.append(", mScorerName=").append(mScorerName);
            sb.append(", mConfigurationActivityClassName='").append(mConfigurationActivityClassName).append('\'');
            sb.append(", mScoringServiceClassName='").append(mScoringServiceClassName).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Returns the list of available scorer apps.
     *
     * <p>A network scorer is any application which:
     * <ul>
     * <li>Declares the {@link android.Manifest.permission#SCORE_NETWORKS} permission.
     * <li>Includes a receiver for {@link NetworkScoreManager#ACTION_SCORE_NETWORKS} guarded by the
     *     {@link android.Manifest.permission#BROADCAST_NETWORK_PRIVILEGED} permission.
     * </ul>
     *
     * @return the list of scorers, or the empty list if there are no valid scorers.
     */
    public static java.util.Collection<android.net.NetworkScorerAppManager.NetworkScorerAppData> getAllValidScorers(android.content.Context context) {
        // Network scorer apps can only run as the primary user so exit early if we're not the
        // primary user.
        if (android.os.UserHandle.getCallingUserId() != android.os.UserHandle.USER_SYSTEM) {
            return java.util.Collections.emptyList();
        }
        java.util.List<android.net.NetworkScorerAppManager.NetworkScorerAppData> scorers = new java.util.ArrayList<>();
        android.content.pm.PackageManager pm = context.getPackageManager();
        // Only apps installed under the primary user of the device can be scorers.
        // TODO: http://b/23422763
        java.util.List<android.content.pm.ResolveInfo> receivers = /* flags */
        pm.queryBroadcastReceiversAsUser(android.net.NetworkScorerAppManager.SCORE_INTENT, 0, android.os.UserHandle.USER_SYSTEM);
        for (android.content.pm.ResolveInfo receiver : receivers) {
            // This field is a misnomer, see android.content.pm.ResolveInfo#activityInfo
            final android.content.pm.ActivityInfo receiverInfo = receiver.activityInfo;
            if (receiverInfo == null) {
                // Should never happen with queryBroadcastReceivers, but invalid nonetheless.
                continue;
            }
            if (!permission.BROADCAST_NETWORK_PRIVILEGED.equals(receiverInfo.permission)) {
                // Receiver doesn't require the BROADCAST_NETWORK_PRIVILEGED permission, which
                // means anyone could trigger network scoring and flood the framework with score
                // requests.
                continue;
            }
            if (pm.checkPermission(permission.SCORE_NETWORKS, receiverInfo.packageName) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Application doesn't hold the SCORE_NETWORKS permission, so the user never
                // approved it as a network scorer.
                continue;
            }
            // Optionally, this package may specify a configuration activity.
            java.lang.String configurationActivityClassName = null;
            android.content.Intent intent = new android.content.Intent(android.net.NetworkScoreManager.ACTION_CUSTOM_ENABLE);
            intent.setPackage(receiverInfo.packageName);
            java.util.List<android.content.pm.ResolveInfo> configActivities = /* flags */
            pm.queryIntentActivities(intent, 0);
            if ((configActivities != null) && (!configActivities.isEmpty())) {
                android.content.pm.ActivityInfo activityInfo = configActivities.get(0).activityInfo;
                if (activityInfo != null) {
                    configurationActivityClassName = activityInfo.name;
                }
            }
            // Find the scoring service class we can bind to, if any.
            java.lang.String scoringServiceClassName = null;
            android.content.Intent serviceIntent = new android.content.Intent(android.net.NetworkScoreManager.ACTION_SCORE_NETWORKS);
            serviceIntent.setPackage(receiverInfo.packageName);
            android.content.pm.ResolveInfo resolveServiceInfo = /* flags */
            pm.resolveService(serviceIntent, 0);
            if ((resolveServiceInfo != null) && (resolveServiceInfo.serviceInfo != null)) {
                scoringServiceClassName = resolveServiceInfo.serviceInfo.name;
            }
            // NOTE: loadLabel will attempt to load the receiver's label and fall back to the
            // app label if none is present.
            scorers.add(new android.net.NetworkScorerAppManager.NetworkScorerAppData(receiverInfo.packageName, receiverInfo.applicationInfo.uid, receiverInfo.loadLabel(pm), configurationActivityClassName, scoringServiceClassName));
        }
        return scorers;
    }

    /**
     * Get the application to use for scoring networks.
     *
     * @return the scorer app info or null if scoring is disabled (including if no scorer was ever
    selected) or if the previously-set scorer is no longer a valid scorer app (e.g. because
    it was disabled or uninstalled).
     */
    public static android.net.NetworkScorerAppManager.NetworkScorerAppData getActiveScorer(android.content.Context context) {
        java.lang.String scorerPackage = android.provider.Settings.Global.getString(context.getContentResolver(), android.provider.Settings.Global.NETWORK_SCORER_APP);
        return android.net.NetworkScorerAppManager.getScorer(context, scorerPackage);
    }

    /**
     * Set the specified package as the default scorer application.
     *
     * <p>The caller must have permission to write to {@link android.provider.Settings.Global}.
     *
     * @param context
     * 		the context of the calling application
     * @param packageName
     * 		the packageName of the new scorer to use. If null, scoring will be
     * 		disabled. Otherwise, the scorer will only be set if it is a valid scorer application.
     * @return true if the scorer was changed, or false if the package is not a valid scorer.
     */
    public static boolean setActiveScorer(android.content.Context context, java.lang.String packageName) {
        java.lang.String oldPackageName = android.provider.Settings.Global.getString(context.getContentResolver(), android.provider.Settings.Global.NETWORK_SCORER_APP);
        if (android.text.TextUtils.equals(oldPackageName, packageName)) {
            // No change.
            return true;
        }
        android.util.Log.i(android.net.NetworkScorerAppManager.TAG, (("Changing network scorer from " + oldPackageName) + " to ") + packageName);
        if (packageName == null) {
            android.provider.Settings.Global.putString(context.getContentResolver(), android.provider.Settings.Global.NETWORK_SCORER_APP, null);
            return true;
        } else {
            // We only make the change if the new package is valid.
            if (android.net.NetworkScorerAppManager.getScorer(context, packageName) != null) {
                android.provider.Settings.Global.putString(context.getContentResolver(), android.provider.Settings.Global.NETWORK_SCORER_APP, packageName);
                return true;
            } else {
                android.util.Log.w(android.net.NetworkScorerAppManager.TAG, "Requested network scorer is not valid: " + packageName);
                return false;
            }
        }
    }

    /**
     * Determine whether the application with the given UID is the enabled scorer.
     */
    public static boolean isCallerActiveScorer(android.content.Context context, int callingUid) {
        android.net.NetworkScorerAppManager.NetworkScorerAppData defaultApp = android.net.NetworkScorerAppManager.getActiveScorer(context);
        if (defaultApp == null) {
            return false;
        }
        if (callingUid != defaultApp.mPackageUid) {
            return false;
        }
        // To be extra safe, ensure the caller holds the SCORE_NETWORKS permission. It always
        // should, since it couldn't become the active scorer otherwise, but this can't hurt.
        return context.checkCallingPermission(Manifest.permission.SCORE_NETWORKS) == android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns the {@link NetworkScorerAppData} for the given app, or null if it's not a scorer.
     */
    public static android.net.NetworkScorerAppManager.NetworkScorerAppData getScorer(android.content.Context context, java.lang.String packageName) {
        if (android.text.TextUtils.isEmpty(packageName)) {
            return null;
        }
        java.util.Collection<android.net.NetworkScorerAppManager.NetworkScorerAppData> applications = android.net.NetworkScorerAppManager.getAllValidScorers(context);
        for (android.net.NetworkScorerAppManager.NetworkScorerAppData app : applications) {
            if (packageName.equals(app.mPackageName)) {
                return app;
            }
        }
        return null;
    }
}

