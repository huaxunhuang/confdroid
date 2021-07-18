/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Helper for accessing features in {@link android.app.Activity}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public class ActivityCompat extends android.support.v4.content.ContextCompat {
    /**
     * This interface is the contract for receiving the results for permission requests.
     */
    public interface OnRequestPermissionsResultCallback {
        /**
         * Callback for the result from requesting permissions. This method
         * is invoked for every call on {@link #requestPermissions(android.app.Activity,
         * String[], int)}.
         * <p>
         * <strong>Note:</strong> It is possible that the permissions request interaction
         * with the user is interrupted. In this case you will receive empty permissions
         * and results arrays which should be treated as a cancellation.
         * </p>
         *
         * @param requestCode
         * 		The request code passed in {@link #requestPermissions(
         * 		android.app.Activity, String[], int)}
         * @param permissions
         * 		The requested permissions. Never null.
         * @param grantResults
         * 		The grant results for the corresponding permissions
         * 		which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
         * 		or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
         * @see #requestPermissions(android.app.Activity, String[], int)
         */
        void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull
        java.lang.String[] permissions, @android.support.annotation.NonNull
        int[] grantResults);
    }

    /**
     * This class should not be instantiated, but the constructor must be
     * visible for the class to be extended (ex. in support-v13).
     */
    protected ActivityCompat() {
        // Not publicly instantiable, but may be extended.
    }

    /**
     * Invalidate the activity's options menu, if able.
     *
     * <p>Before API level 11 (Android 3.0/Honeycomb) the lifecycle of the
     * options menu was controlled primarily by the user's operation of
     * the hardware menu key. When the user presses down on the menu key
     * for the first time the menu was created and prepared by calls
     * to {@link Activity#onCreateOptionsMenu(android.view.Menu)} and
     * {@link Activity#onPrepareOptionsMenu(android.view.Menu)} respectively.
     * Subsequent presses of the menu key kept the existing instance of the
     * Menu itself and called {@link Activity#onPrepareOptionsMenu(android.view.Menu)}
     * to give the activity an opportunity to contextually alter the menu
     * before the menu panel was shown.</p>
     *
     * <p>In Android 3.0+ the Action Bar forces the options menu to be built early
     * so that items chosen to show as actions may be displayed when the activity
     * first becomes visible. The Activity method invalidateOptionsMenu forces
     * the entire menu to be destroyed and recreated from
     * {@link Activity#onCreateOptionsMenu(android.view.Menu)}, offering a similar
     * though heavier-weight opportunity to change the menu's contents. Normally
     * this functionality is used to support a changing configuration of Fragments.</p>
     *
     * <p>Applications may use this support helper to signal a significant change in
     * activity state that should cause the options menu to be rebuilt. If the app
     * is running on an older platform version that does not support menu invalidation
     * the app will still receive {@link Activity#onPrepareOptionsMenu(android.view.Menu)}
     * the next time the user presses the menu key and this method will return false.
     * If this method returns true the options menu was successfully invalidated.</p>
     *
     * @param activity
     * 		Invalidate the options menu of this activity
     * @return true if this operation was supported and it completed; false if it was not available.
     */
    public static boolean invalidateOptionsMenu(android.app.Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            android.support.v4.app.ActivityCompatHoneycomb.invalidateOptionsMenu(activity);
            return true;
        }
        return false;
    }

    /**
     * Start new activity with options, if able, for which you would like a
     * result when it finished.
     *
     * <p>In Android 4.1+ additional options were introduced to allow for more
     * control on activity launch animations. Applications can use this method
     * along with {@link ActivityOptionsCompat} to use these animations when
     * available. When run on versions of the platform where this feature does
     * not exist the activity will be launched normally.</p>
     *
     * @param activity
     * 		Origin activity to launch from.
     * @param intent
     * 		The description of the activity to start.
     * @param requestCode
     * 		If >= 0, this code will be returned in
     * 		onActivityResult() when the activity exits.
     * @param options
     * 		Additional options for how the Activity should be started.
     * 		May be null if there are no options. See
     * 		{@link ActivityOptionsCompat} for how to build the Bundle
     * 		supplied here; there are no supported definitions for
     * 		building it manually.
     */
    public static void startActivityForResult(android.app.Activity activity, android.content.Intent intent, int requestCode, @android.support.annotation.Nullable
    android.os.Bundle options) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            android.support.v4.app.ActivityCompatJB.startActivityForResult(activity, intent, requestCode, options);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Start new IntentSender with options, if able, for which you would like a
     * result when it finished.
     *
     * <p>In Android 4.1+ additional options were introduced to allow for more
     * control on activity launch animations. Applications can use this method
     * along with {@link ActivityOptionsCompat} to use these animations when
     * available. When run on versions of the platform where this feature does
     * not exist the activity will be launched normally.</p>
     *
     * @param activity
     * 		Origin activity to launch from.
     * @param intent
     * 		The IntentSender to launch.
     * @param requestCode
     * 		If >= 0, this code will be returned in
     * 		onActivityResult() when the activity exits.
     * @param fillInIntent
     * 		If non-null, this will be provided as the
     * 		intent parameter to {@link IntentSender#sendIntent}.
     * @param flagsMask
     * 		Intent flags in the original IntentSender that you
     * 		would like to change.
     * @param flagsValues
     * 		Desired values for any bits set in <var>flagsMask</var>
     * @param extraFlags
     * 		Always set to 0.
     * @param options
     * 		Additional options for how the Activity should be started.
     * 		May be null if there are no options. See
     * 		{@link ActivityOptionsCompat} for how to build the Bundle
     * 		supplied here; there are no supported definitions for
     * 		building it manually.
     */
    public static void startIntentSenderForResult(android.app.Activity activity, android.content.IntentSender intent, int requestCode, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @android.support.annotation.Nullable
    android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            android.support.v4.app.ActivityCompatJB.startIntentSenderForResult(activity, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    /**
     * Finish this activity, and tries to finish all activities immediately below it
     * in the current task that have the same affinity.
     *
     * <p>On Android 4.1+ calling this method will call through to the native version of this
     * method. For other platforms {@link Activity#finish()} will be called instead.</p>
     */
    public static void finishAffinity(android.app.Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            android.support.v4.app.ActivityCompatJB.finishAffinity(activity);
        } else {
            activity.finish();
        }
    }

    /**
     * Reverses the Activity Scene entry Transition and triggers the calling Activity
     * to reverse its exit Transition. When the exit Transition completes,
     * {@link Activity#finish()} is called. If no entry Transition was used, finish() is called
     * immediately and the Activity exit Transition is run.
     *
     * <p>On Android 4.4 or lower, this method only finishes the Activity with no
     * special exit transition.</p>
     */
    public static void finishAfterTransition(android.app.Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v4.app.ActivityCompatApi21.finishAfterTransition(activity);
        } else {
            activity.finish();
        }
    }

    /**
     * Return information about who launched this activity.  If the launching Intent
     * contains an {@link Intent#EXTRA_REFERRER Intent.EXTRA_REFERRER},
     * that will be returned as-is; otherwise, if known, an
     * {@link Intent#URI_ANDROID_APP_SCHEME android-app:} referrer URI containing the
     * package name that started the Intent will be returned.  This may return null if no
     * referrer can be identified -- it is neither explicitly specified, nor is it known which
     * application package was involved.
     *
     * <p>If called while inside the handling of {@link Activity#onNewIntent}, this function will
     * return the referrer that submitted that new intent to the activity.  Otherwise, it
     * always returns the referrer of the original Intent.</p>
     *
     * <p>Note that this is <em>not</em> a security feature -- you can not trust the
     * referrer information, applications can spoof it.</p>
     */
    @android.support.annotation.Nullable
    public static android.net.Uri getReferrer(android.app.Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 22) {
            return android.support.v4.app.ActivityCompatApi22.getReferrer(activity);
        }
        android.content.Intent intent = activity.getIntent();
        android.net.Uri referrer = intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (referrer != null) {
            return referrer;
        }
        java.lang.String referrerName = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (referrerName != null) {
            return android.net.Uri.parse(referrerName);
        }
        return null;
    }

    /**
     * When {@link android.app.ActivityOptions#makeSceneTransitionAnimation(Activity,
     * android.view.View, String)} was used to start an Activity, <var>callback</var>
     * will be called to handle shared elements on the <i>launched</i> Activity. This requires
     * {@link android.view.Window#FEATURE_CONTENT_TRANSITIONS}.
     *
     * @param callback
     * 		Used to manipulate shared element transitions on the launched Activity.
     */
    public static void setEnterSharedElementCallback(android.app.Activity activity, android.support.v4.app.SharedElementCallback callback) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            android.support.v4.app.ActivityCompatApi23.setEnterSharedElementCallback(activity, android.support.v4.app.ActivityCompat.createCallback23(callback));
        } else
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                android.support.v4.app.ActivityCompatApi21.setEnterSharedElementCallback(activity, android.support.v4.app.ActivityCompat.createCallback(callback));
            }

    }

    /**
     * When {@link android.app.ActivityOptions#makeSceneTransitionAnimation(Activity,
     * android.view.View, String)} was used to start an Activity, <var>callback</var>
     * will be called to handle shared elements on the <i>launching</i> Activity. Most
     * calls will only come when returning from the started Activity.
     * This requires {@link android.view.Window#FEATURE_CONTENT_TRANSITIONS}.
     *
     * @param callback
     * 		Used to manipulate shared element transitions on the launching Activity.
     */
    public static void setExitSharedElementCallback(android.app.Activity activity, android.support.v4.app.SharedElementCallback callback) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            android.support.v4.app.ActivityCompatApi23.setExitSharedElementCallback(activity, android.support.v4.app.ActivityCompat.createCallback23(callback));
        } else
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                android.support.v4.app.ActivityCompatApi21.setExitSharedElementCallback(activity, android.support.v4.app.ActivityCompat.createCallback(callback));
            }

    }

    public static void postponeEnterTransition(android.app.Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v4.app.ActivityCompatApi21.postponeEnterTransition(activity);
        }
    }

    public static void startPostponedEnterTransition(android.app.Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v4.app.ActivityCompatApi21.startPostponedEnterTransition(activity);
        }
    }

    /**
     * Requests permissions to be granted to this application. These permissions
     * must be requested in your manifest, they should not be granted to your app,
     * and they should have protection level {@link android.content.pm.PermissionInfo
     * #PROTECTION_DANGEROUS dangerous}, regardless whether they are declared by
     * the platform or a third-party app.
     * <p>
     * Normal permissions {@link android.content.pm.PermissionInfo#PROTECTION_NORMAL}
     * are granted at install time if requested in the manifest. Signature permissions
     * {@link android.content.pm.PermissionInfo#PROTECTION_SIGNATURE} are granted at
     * install time if requested in the manifest and the signature of your app matches
     * the signature of the app declaring the permissions.
     * </p>
     * <p>
     * If your app does not have the requested permissions the user will be presented
     * with UI for accepting them. After the user has accepted or rejected the
     * requested permissions you will receive a callback reporting whether the
     * permissions were granted or not. Your activity has to implement {@link android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback}
     * and the results of permission requests will be delivered to its {@link android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(
     * int, String[], int[])} method.
     * </p>
     * <p>
     * Note that requesting a permission does not guarantee it will be granted and
     * your app should be able to run without having this permission.
     * </p>
     * <p>
     * This method may start an activity allowing the user to choose which permissions
     * to grant and which to reject. Hence, you should be prepared that your activity
     * may be paused and resumed. Further, granting some permissions may require
     * a restart of you application. In such a case, the system will recreate the
     * activity stack before delivering the result to your
     * {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
     * </p>
     * <p>
     * When checking whether you have a permission you should use {@link #checkSelfPermission(android.content.Context, String)}.
     * </p>
     * <p>
     * Calling this API for permissions already granted to your app would show UI
     * to the user to decided whether the app can still hold these permissions. This
     * can be useful if the way your app uses the data guarded by the permissions
     * changes significantly.
     * </p>
     * <p>
     * You cannot request a permission if your activity sets {@link android.R.attr#noHistory noHistory} to <code>true</code> in the manifest
     * because in this case the activity would not receive result callbacks including
     * {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
     * </p>
     * <p>
     * The <a href="http://developer.android.com/samples/RuntimePermissions/index.html">
     * RuntimePermissions</a> sample app demonstrates how to use this method to
     * request permissions at run time.
     * </p>
     *
     * @param activity
     * 		The target activity.
     * @param permissions
     * 		The requested permissions. Must me non-null and not empty.
     * @param requestCode
     * 		Application specific request code to match with a result
     * 		reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
     * 		Should be >= 0.
     * @see OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])
     * @see #checkSelfPermission(android.content.Context, String)
     * @see #shouldShowRequestPermissionRationale(android.app.Activity, String)
     */
    public static void requestPermissions(@android.support.annotation.NonNull
    final android.app.Activity activity, @android.support.annotation.NonNull
    final java.lang.String[] permissions, @android.support.annotation.IntRange(from = 0)
    final int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            android.support.v4.app.ActivityCompatApi23.requestPermissions(activity, permissions, requestCode);
        } else
            if (activity instanceof android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback) {
                android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                handler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        final int[] grantResults = new int[permissions.length];
                        android.content.pm.PackageManager packageManager = activity.getPackageManager();
                        java.lang.String packageName = activity.getPackageName();
                        final int permissionCount = permissions.length;
                        for (int i = 0; i < permissionCount; i++) {
                            grantResults[i] = packageManager.checkPermission(permissions[i], packageName);
                        }
                        ((android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback) (activity)).onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                });
            }

    }

    /**
     * Gets whether you should show UI with rationale for requesting a permission.
     * You should do this only if you do not have the permission and the context in
     * which the permission is requested does not clearly communicate to the user
     * what would be the benefit from granting this permission.
     * <p>
     * For example, if you write a camera app, requesting the camera permission
     * would be expected by the user and no rationale for why it is requested is
     * needed. If however, the app needs location for tagging photos then a non-tech
     * savvy user may wonder how location is related to taking photos. In this case
     * you may choose to show UI with rationale of requesting this permission.
     * </p>
     *
     * @param activity
     * 		The target activity.
     * @param permission
     * 		A permission your app wants to request.
     * @return Whether you can show permission rationale UI.
     * @see #checkSelfPermission(android.content.Context, String)
     * @see #requestPermissions(android.app.Activity, String[], int)
     */
    public static boolean shouldShowRequestPermissionRationale(@android.support.annotation.NonNull
    android.app.Activity activity, @android.support.annotation.NonNull
    java.lang.String permission) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            return android.support.v4.app.ActivityCompatApi23.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }

    private static android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 createCallback(android.support.v4.app.SharedElementCallback callback) {
        android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 newCallback = null;
        if (callback != null) {
            newCallback = new android.support.v4.app.ActivityCompat.SharedElementCallback21Impl(callback);
        }
        return newCallback;
    }

    private static android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 createCallback23(android.support.v4.app.SharedElementCallback callback) {
        android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 newCallback = null;
        if (callback != null) {
            newCallback = new android.support.v4.app.ActivityCompat.SharedElementCallback23Impl(callback);
        }
        return newCallback;
    }

    private static class SharedElementCallback21Impl extends android.support.v4.app.ActivityCompatApi21.SharedElementCallback21 {
        private android.support.v4.app.SharedElementCallback mCallback;

        public SharedElementCallback21Impl(android.support.v4.app.SharedElementCallback callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onSharedElementStart(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
            mCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @java.lang.Override
        public void onSharedElementEnd(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
            mCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @java.lang.Override
        public void onRejectSharedElements(java.util.List<android.view.View> rejectedSharedElements) {
            mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @java.lang.Override
        public void onMapSharedElements(java.util.List<java.lang.String> names, java.util.Map<java.lang.String, android.view.View> sharedElements) {
            mCallback.onMapSharedElements(names, sharedElements);
        }

        @java.lang.Override
        public android.os.Parcelable onCaptureSharedElementSnapshot(android.view.View sharedElement, android.graphics.Matrix viewToGlobalMatrix, android.graphics.RectF screenBounds) {
            return mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        @java.lang.Override
        public android.view.View onCreateSnapshotView(android.content.Context context, android.os.Parcelable snapshot) {
            return mCallback.onCreateSnapshotView(context, snapshot);
        }
    }

    private static class SharedElementCallback23Impl extends android.support.v4.app.ActivityCompatApi23.SharedElementCallback23 {
        private android.support.v4.app.SharedElementCallback mCallback;

        public SharedElementCallback23Impl(android.support.v4.app.SharedElementCallback callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onSharedElementStart(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
            mCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @java.lang.Override
        public void onSharedElementEnd(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
            mCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @java.lang.Override
        public void onRejectSharedElements(java.util.List<android.view.View> rejectedSharedElements) {
            mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @java.lang.Override
        public void onMapSharedElements(java.util.List<java.lang.String> names, java.util.Map<java.lang.String, android.view.View> sharedElements) {
            mCallback.onMapSharedElements(names, sharedElements);
        }

        @java.lang.Override
        public android.os.Parcelable onCaptureSharedElementSnapshot(android.view.View sharedElement, android.graphics.Matrix viewToGlobalMatrix, android.graphics.RectF screenBounds) {
            return mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        @java.lang.Override
        public android.view.View onCreateSnapshotView(android.content.Context context, android.os.Parcelable snapshot) {
            return mCallback.onCreateSnapshotView(context, snapshot);
        }

        @java.lang.Override
        public void onSharedElementsArrived(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, final android.support.v4.app.ActivityCompatApi23.OnSharedElementsReadyListenerBridge listener) {
            mCallback.onSharedElementsArrived(sharedElementNames, sharedElements, new android.support.v4.app.SharedElementCallback.OnSharedElementsReadyListener() {
                @java.lang.Override
                public void onSharedElementsReady() {
                    listener.onSharedElementsReady();
                }
            });
        }
    }
}

