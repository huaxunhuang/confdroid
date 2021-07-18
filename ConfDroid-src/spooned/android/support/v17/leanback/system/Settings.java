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
 * limitations under the License
 */
package android.support.v17.leanback.system;


/**
 * Provides various preferences affecting Leanback runtime behavior.
 * <p>Note this class is not thread safe and its methods should only
 * be invoked from the UI thread
 * </p>
 */
public class Settings {
    private static final java.lang.String TAG = "Settings";

    private static final boolean DEBUG = false;

    // The intent action that must be provided by a broadcast receiver
    // in a customization package.
    private static final java.lang.String ACTION_PARTNER_CUSTOMIZATION = "android.support.v17.leanback.action.PARTNER_CUSTOMIZATION";

    public static final java.lang.String PREFER_STATIC_SHADOWS = "PREFER_STATIC_SHADOWS";

    private static android.support.v17.leanback.system.Settings sInstance;

    private boolean mPreferStaticShadows;

    /**
     * Returns the singleton Settings instance.
     */
    public static android.support.v17.leanback.system.Settings getInstance(android.content.Context context) {
        if (android.support.v17.leanback.system.Settings.sInstance == null) {
            android.support.v17.leanback.system.Settings.sInstance = new android.support.v17.leanback.system.Settings(context);
        }
        return android.support.v17.leanback.system.Settings.sInstance;
    }

    private Settings(android.content.Context context) {
        if (android.support.v17.leanback.system.Settings.DEBUG)
            android.util.Log.v(android.support.v17.leanback.system.Settings.TAG, "generating preferences");

        android.support.v17.leanback.system.Settings.Customizations customizations = getCustomizations(context);
        generateShadowSetting(customizations);
    }

    /**
     * Returns true if static shadows are recommended.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public boolean preferStaticShadows() {
        return mPreferStaticShadows;
    }

    /**
     * Returns the boolean preference for the given key.
     */
    public boolean getBoolean(java.lang.String key) {
        return getOrSetBoolean(key, false, false);
    }

    /**
     * Sets the boolean preference for the given key.  If an app uses this api to override
     * a default preference, it must do so on every activity create.
     */
    public void setBoolean(java.lang.String key, boolean value) {
        getOrSetBoolean(key, true, value);
    }

    boolean getOrSetBoolean(java.lang.String key, boolean set, boolean value) {
        if (key.compareTo(android.support.v17.leanback.system.Settings.PREFER_STATIC_SHADOWS) == 0) {
            return set ? mPreferStaticShadows = value : mPreferStaticShadows;
        }
        throw new java.lang.IllegalArgumentException("Invalid key");
    }

    private void generateShadowSetting(android.support.v17.leanback.system.Settings.Customizations customizations) {
        if (android.support.v17.leanback.widget.ShadowOverlayContainer.supportsDynamicShadow()) {
            mPreferStaticShadows = false;
            if (customizations != null) {
                mPreferStaticShadows = customizations.getBoolean("leanback_prefer_static_shadows", mPreferStaticShadows);
            }
        } else {
            mPreferStaticShadows = true;
        }
        if (android.support.v17.leanback.system.Settings.DEBUG)
            android.util.Log.v(android.support.v17.leanback.system.Settings.TAG, (("generated preference " + android.support.v17.leanback.system.Settings.PREFER_STATIC_SHADOWS) + ": ") + mPreferStaticShadows);

    }

    static class Customizations {
        android.content.res.Resources mResources;

        java.lang.String mPackageName;

        public Customizations(android.content.res.Resources resources, java.lang.String packageName) {
            mResources = resources;
            mPackageName = packageName;
        }

        public boolean getBoolean(java.lang.String resourceName, boolean defaultValue) {
            int resId = mResources.getIdentifier(resourceName, "bool", mPackageName);
            return resId > 0 ? mResources.getBoolean(resId) : defaultValue;
        }
    }

    private android.support.v17.leanback.system.Settings.Customizations getCustomizations(android.content.Context context) {
        final android.content.pm.PackageManager pm = context.getPackageManager();
        final android.content.Intent intent = new android.content.Intent(android.support.v17.leanback.system.Settings.ACTION_PARTNER_CUSTOMIZATION);
        if (android.support.v17.leanback.system.Settings.DEBUG)
            android.util.Log.v(android.support.v17.leanback.system.Settings.TAG, "getting oem customizations by intent: " + android.support.v17.leanback.system.Settings.ACTION_PARTNER_CUSTOMIZATION);

        android.content.res.Resources resources = null;
        java.lang.String packageName = null;
        for (android.content.pm.ResolveInfo info : pm.queryBroadcastReceivers(intent, 0)) {
            packageName = info.activityInfo.packageName;
            if (android.support.v17.leanback.system.Settings.DEBUG)
                android.util.Log.v(android.support.v17.leanback.system.Settings.TAG, "got package " + packageName);

            if ((packageName != null) && android.support.v17.leanback.system.Settings.isSystemApp(info))
                try {
                    resources = pm.getResourcesForApplication(packageName);
                } catch (android.content.pm.PackageManager.NameNotFoundException ex) {
                    // Do nothing
                }

            if (resources != null) {
                if (android.support.v17.leanback.system.Settings.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.system.Settings.TAG, "found customization package: " + packageName);

                break;
            }
        }
        return resources == null ? null : new android.support.v17.leanback.system.Settings.Customizations(resources, packageName);
    }

    private static boolean isSystemApp(android.content.pm.ResolveInfo info) {
        return (info.activityInfo != null) && ((info.activityInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}

