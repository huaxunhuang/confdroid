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
package android.content.pm;


/**
 * A representation of an activity that can belong to this user or a managed
 * profile associated with this user. It can be used to query the label, icon
 * and badged icon for the activity.
 */
public class LauncherActivityInfo {
    private static final java.lang.String TAG = "LauncherActivityInfo";

    private final android.content.pm.PackageManager mPm;

    @android.annotation.UnsupportedAppUsage
    private android.content.pm.ActivityInfo mActivityInfo;

    private android.content.ComponentName mComponentName;

    private android.os.UserHandle mUser;

    /**
     * Create a launchable activity object for a given ResolveInfo and user.
     *
     * @param context
     * 		The context for fetching resources.
     * @param info
     * 		ResolveInfo from which to create the LauncherActivityInfo.
     * @param user
     * 		The UserHandle of the profile to which this activity belongs.
     */
    LauncherActivityInfo(android.content.Context context, android.content.pm.ActivityInfo info, android.os.UserHandle user) {
        this(context);
        mActivityInfo = info;
        mComponentName = new android.content.ComponentName(info.packageName, info.name);
        mUser = user;
    }

    LauncherActivityInfo(android.content.Context context) {
        mPm = context.getPackageManager();
    }

    /**
     * Returns the component name of this activity.
     *
     * @return ComponentName of the activity
     */
    public android.content.ComponentName getComponentName() {
        return mComponentName;
    }

    /**
     * Returns the user handle of the user profile that this activity belongs to. In order to
     * persist the identity of the profile, do not store the UserHandle. Instead retrieve its
     * serial number from UserManager. You can convert the serial number back to a UserHandle
     * for later use.
     *
     * @see UserManager#getSerialNumberForUser(UserHandle)
     * @see UserManager#getUserForSerialNumber(long)
     * @return The UserHandle of the profile.
     */
    public android.os.UserHandle getUser() {
        return mUser;
    }

    /**
     * Retrieves the label for the activity.
     *
     * @return The label for the activity.
     */
    public java.lang.CharSequence getLabel() {
        // TODO: Go through LauncherAppsService
        return mActivityInfo.loadLabel(mPm);
    }

    /**
     * Returns the icon for this activity, without any badging for the profile.
     *
     * @param density
     * 		The preferred density of the icon, zero for default density. Use
     * 		density DPI values from {@link DisplayMetrics}.
     * @see #getBadgedIcon(int)
     * @see DisplayMetrics
     * @return The drawable associated with the activity.
     */
    public android.graphics.drawable.Drawable getIcon(int density) {
        // TODO: Go through LauncherAppsService
        final int iconRes = mActivityInfo.getIconResource();
        android.graphics.drawable.Drawable icon = null;
        // Get the preferred density icon from the app's resources
        if ((density != 0) && (iconRes != 0)) {
            try {
                final android.content.res.Resources resources = mPm.getResourcesForApplication(mActivityInfo.applicationInfo);
                icon = resources.getDrawableForDensity(iconRes, density);
            } catch (android.content.pm.PackageManager.NameNotFoundException | android.content.res.Resources.NotFoundException exc) {
            }
        }
        // Get the default density icon
        if (icon == null) {
            icon = mActivityInfo.loadIcon(mPm);
        }
        return icon;
    }

    /**
     * Returns the application flags from the ApplicationInfo of the activity.
     *
     * @return Application flags
     * @unknown remove before shipping
     */
    public int getApplicationFlags() {
        return mActivityInfo.applicationInfo.flags;
    }

    /**
     * Returns the application info for the appliction this activity belongs to.
     *
     * @return 
     */
    public android.content.pm.ApplicationInfo getApplicationInfo() {
        return mActivityInfo.applicationInfo;
    }

    /**
     * Returns the time at which the package was first installed.
     *
     * @return The time of installation of the package, in milliseconds.
     */
    public long getFirstInstallTime() {
        try {
            // TODO: Go through LauncherAppsService
            return mPm.getPackageInfo(mActivityInfo.packageName, android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES).firstInstallTime;
        } catch (android.content.pm.PackageManager.NameNotFoundException nnfe) {
            // Sorry, can't find package
            return 0;
        }
    }

    /**
     * Returns the name for the acitivty from  android:name in the manifest.
     *
     * @return the name from android:name for the acitivity.
     */
    public java.lang.String getName() {
        return mActivityInfo.name;
    }

    /**
     * Returns the activity icon with badging appropriate for the profile.
     *
     * @param density
     * 		Optional density for the icon, or 0 to use the default density. Use
     * 		{@link DisplayMetrics} for DPI values.
     * @see DisplayMetrics
     * @return A badged icon for the activity.
     */
    public android.graphics.drawable.Drawable getBadgedIcon(int density) {
        android.graphics.drawable.Drawable originalIcon = getIcon(density);
        return mPm.getUserBadgedIcon(originalIcon, mUser);
    }
}

