/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * Class for handling cross profile operations. Apps can use this class to interact with its
 * instance in any profile that is in {@link #getTargetUserProfiles()}. For example, app can
 * use this class to start its main activity in managed profile.
 */
public class CrossProfileApps {
    private final android.content.Context mContext;

    private final android.content.pm.ICrossProfileApps mService;

    private final android.os.UserManager mUserManager;

    private final android.content.res.Resources mResources;

    /**
     *
     *
     * @unknown 
     */
    public CrossProfileApps(android.content.Context context, android.content.pm.ICrossProfileApps service) {
        mContext = context;
        mService = service;
        mUserManager = context.getSystemService(android.os.UserManager.class);
        mResources = context.getResources();
    }

    /**
     * Starts the specified main activity of the caller package in the specified profile.
     *
     * @param component
     * 		The ComponentName of the activity to launch, it must be exported and has
     * 		action {@link android.content.Intent#ACTION_MAIN}, category
     * 		{@link android.content.Intent#CATEGORY_LAUNCHER}. Otherwise, SecurityException will
     * 		be thrown.
     * @param targetUser
     * 		The UserHandle of the profile, must be one of the users returned by
     * 		{@link #getTargetUserProfiles()}, otherwise a {@link SecurityException} will
     * 		be thrown.
     */
    public void startMainActivity(@android.annotation.NonNull
    android.content.ComponentName component, @android.annotation.NonNull
    android.os.UserHandle targetUser) {
        try {
            mService.startActivityAsUser(mContext.getIApplicationThread(), mContext.getPackageName(), component, targetUser.getIdentifier(), true);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Starts the specified activity of the caller package in the specified profile. Unlike
     * {@link #startMainActivity}, this can start any activity of the caller package, not just
     * the main activity.
     * The caller must have the {@link android.Manifest.permission#INTERACT_ACROSS_PROFILES}
     * permission and both the caller and target user profiles must be in the same profile group.
     *
     * @param component
     * 		The ComponentName of the activity to launch. It must be exported.
     * @param targetUser
     * 		The UserHandle of the profile, must be one of the users returned by
     * 		{@link #getTargetUserProfiles()}, otherwise a {@link SecurityException} will
     * 		be thrown.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.content.pm.android.Manifest.permission.class)
    public void startActivity(@android.annotation.NonNull
    android.content.ComponentName component, @android.annotation.NonNull
    android.os.UserHandle targetUser) {
        try {
            mService.startActivityAsUser(mContext.getIApplicationThread(), mContext.getPackageName(), component, targetUser.getIdentifier(), false);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Return a list of user profiles that that the caller can use when calling other APIs in this
     * class.
     * <p>
     * A user profile would be considered as a valid target user profile, provided that:
     * <ul>
     * <li>It gets caller app installed</li>
     * <li>It is not equal to the calling user</li>
     * <li>It is in the same profile group of calling user profile</li>
     * <li>It is enabled</li>
     * </ul>
     *
     * @see UserManager#getUserProfiles()
     */
    @android.annotation.NonNull
    public java.util.List<android.os.UserHandle> getTargetUserProfiles() {
        try {
            return mService.getTargetUserProfiles(mContext.getPackageName());
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Return a label that calling app can show to user for the semantic of profile switching --
     * launching its own activity in specified user profile. For example, it may return
     * "Switch to work" if the given user handle is the managed profile one.
     *
     * @param userHandle
     * 		The UserHandle of the target profile, must be one of the users returned by
     * 		{@link #getTargetUserProfiles()}, otherwise a {@link SecurityException} will
     * 		be thrown.
     * @return a label that calling app can show user for the semantic of launching its own
    activity in the specified user profile.
     * @see #startMainActivity(ComponentName, UserHandle)
     */
    @android.annotation.NonNull
    public java.lang.CharSequence getProfileSwitchingLabel(@android.annotation.NonNull
    android.os.UserHandle userHandle) {
        verifyCanAccessUser(userHandle);
        final int stringRes = (mUserManager.isManagedProfile(userHandle.getIdentifier())) ? R.string.managed_profile_label : R.string.user_owner_label;
        return mResources.getString(stringRes);
    }

    /**
     * Return a drawable that calling app can show to user for the semantic of profile switching --
     * launching its own activity in specified user profile. For example, it may return a briefcase
     * icon if the given user handle is the managed profile one.
     *
     * @param userHandle
     * 		The UserHandle of the target profile, must be one of the users returned by
     * 		{@link #getTargetUserProfiles()}, otherwise a {@link SecurityException} will
     * 		be thrown.
     * @return an icon that calling app can show user for the semantic of launching its own
    activity in specified user profile.
     * @see #startMainActivity(ComponentName, UserHandle)
     */
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable getProfileSwitchingIconDrawable(@android.annotation.NonNull
    android.os.UserHandle userHandle) {
        verifyCanAccessUser(userHandle);
        final boolean isManagedProfile = mUserManager.isManagedProfile(userHandle.getIdentifier());
        if (isManagedProfile) {
            return mResources.getDrawable(R.drawable.ic_corp_badge, null);
        } else {
            return /* light */
            com.android.internal.util.UserIcons.getDefaultUserIcon(mResources, UserHandle.USER_SYSTEM, true);
        }
    }

    private void verifyCanAccessUser(android.os.UserHandle userHandle) {
        if (!getTargetUserProfiles().contains(userHandle)) {
            throw new java.lang.SecurityException("Not allowed to access " + userHandle);
        }
    }
}

