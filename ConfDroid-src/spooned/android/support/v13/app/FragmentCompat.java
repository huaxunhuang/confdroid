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
package android.support.v13.app;


/**
 * Helper for accessing features in {@link Fragment} introduced after
 * API level 13 in a backwards compatible fashion.
 */
public class FragmentCompat {
    interface FragmentCompatImpl {
        void setMenuVisibility(android.app.Fragment f, boolean visible);

        void setUserVisibleHint(android.app.Fragment f, boolean deferStart);

        void requestPermissions(android.app.Fragment fragment, java.lang.String[] permissions, int requestCode);

        boolean shouldShowRequestPermissionRationale(android.app.Fragment fragment, java.lang.String permission);
    }

    static class BaseFragmentCompatImpl implements android.support.v13.app.FragmentCompat.FragmentCompatImpl {
        @java.lang.Override
        public void setMenuVisibility(android.app.Fragment f, boolean visible) {
        }

        @java.lang.Override
        public void setUserVisibleHint(android.app.Fragment f, boolean deferStart) {
        }

        @java.lang.Override
        public void requestPermissions(final android.app.Fragment fragment, final java.lang.String[] permissions, final int requestCode) {
            android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
            handler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final int[] grantResults = new int[permissions.length];
                    android.content.Context context = fragment.getActivity();
                    if (context != null) {
                        android.content.pm.PackageManager packageManager = context.getPackageManager();
                        java.lang.String packageName = context.getPackageName();
                        final int permissionCount = permissions.length;
                        for (int i = 0; i < permissionCount; i++) {
                            grantResults[i] = packageManager.checkPermission(permissions[i], packageName);
                        }
                    } else {
                        java.util.Arrays.fill(grantResults, android.content.pm.PackageManager.PERMISSION_DENIED);
                    }
                    ((android.support.v13.app.FragmentCompat.OnRequestPermissionsResultCallback) (fragment)).onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            });
        }

        @java.lang.Override
        public boolean shouldShowRequestPermissionRationale(android.app.Fragment fragment, java.lang.String permission) {
            return false;
        }
    }

    static class ICSFragmentCompatImpl extends android.support.v13.app.FragmentCompat.BaseFragmentCompatImpl {
        @java.lang.Override
        public void setMenuVisibility(android.app.Fragment f, boolean visible) {
            android.support.v13.app.FragmentCompatICS.setMenuVisibility(f, visible);
        }
    }

    static class ICSMR1FragmentCompatImpl extends android.support.v13.app.FragmentCompat.ICSFragmentCompatImpl {
        @java.lang.Override
        public void setUserVisibleHint(android.app.Fragment f, boolean deferStart) {
            android.support.v13.app.FragmentCompatICSMR1.setUserVisibleHint(f, deferStart);
        }
    }

    static class MncFragmentCompatImpl extends android.support.v13.app.FragmentCompat.ICSMR1FragmentCompatImpl {
        @java.lang.Override
        public void requestPermissions(android.app.Fragment fragment, java.lang.String[] permissions, int requestCode) {
            android.support.v13.app.FragmentCompat23.requestPermissions(fragment, permissions, requestCode);
        }

        @java.lang.Override
        public boolean shouldShowRequestPermissionRationale(android.app.Fragment fragment, java.lang.String permission) {
            return android.support.v13.app.FragmentCompat23.shouldShowRequestPermissionRationale(fragment, permission);
        }
    }

    static class NFragmentCompatImpl extends android.support.v13.app.FragmentCompat.MncFragmentCompatImpl {
        @java.lang.Override
        public void setUserVisibleHint(android.app.Fragment f, boolean deferStart) {
            android.support.v13.app.FragmentCompatApi24.setUserVisibleHint(f, deferStart);
        }
    }

    static final android.support.v13.app.FragmentCompat.FragmentCompatImpl IMPL;

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastN()) {
            IMPL = new android.support.v13.app.FragmentCompat.NFragmentCompatImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                IMPL = new android.support.v13.app.FragmentCompat.MncFragmentCompatImpl();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 15) {
                    IMPL = new android.support.v13.app.FragmentCompat.ICSMR1FragmentCompatImpl();
                } else
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        IMPL = new android.support.v13.app.FragmentCompat.ICSFragmentCompatImpl();
                    } else {
                        IMPL = new android.support.v13.app.FragmentCompat.BaseFragmentCompatImpl();
                    }



    }

    /**
     * This interface is the contract for receiving the results for permission requests.
     */
    public interface OnRequestPermissionsResultCallback {
        /**
         * Callback for the result from requesting permissions. This method
         * is invoked for every call on {@link #requestPermissions(android.app.Fragment,
         * String[], int)}
         *
         * @param requestCode
         * 		The request code passed in {@link #requestPermissions(
         * 		android.app.Fragment, String[], int)}
         * @param permissions
         * 		The requested permissions. Never null.
         * @param grantResults
         * 		The grant results for the corresponding permissions
         * 		which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
         * 		or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
         * @see #requestPermissions(android.app.Fragment, String[], int)
         */
        public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull
        java.lang.String[] permissions, @android.support.annotation.NonNull
        int[] grantResults);
    }

    /**
     * Call {@link Fragment#setMenuVisibility(boolean) Fragment.setMenuVisibility(boolean)}
     * if running on an appropriate version of the platform.
     */
    public static void setMenuVisibility(android.app.Fragment f, boolean visible) {
        android.support.v13.app.FragmentCompat.IMPL.setMenuVisibility(f, visible);
    }

    /**
     * Call {@link Fragment#setUserVisibleHint(boolean) setUserVisibleHint(boolean)}
     * if running on an appropriate version of the platform.
     */
    public static void setUserVisibleHint(android.app.Fragment f, boolean deferStart) {
        android.support.v13.app.FragmentCompat.IMPL.setUserVisibleHint(f, deferStart);
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
     * permissions were granted or not. Your fragment has to implement {@link OnRequestPermissionsResultCallback}
     * and the results of permission requests will be delivered to its
     * {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
     * int, String[], int[])}.
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
     * activity stack before delivering the result to your onRequestPermissionsResult(
     * int, String[], int[]).
     * </p>
     * <p>
     * When checking whether you have a permission you should use {@link android.support.v4.content.ContextCompat#checkSelfPermission(
     * android.content.Context, String)}.
     * </p>
     *
     * @param fragment
     * 		The target fragment.
     * @param permissions
     * 		The requested permissions.
     * @param requestCode
     * 		Application specific request code to match with a result
     * 		reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
     * 		int, String[], int[])}.
     * @see android.support.v4.content.ContextCompat#checkSelfPermission(
    android.content.Context, String)
     * @see #shouldShowRequestPermissionRationale(android.app.Fragment, String)
     */
    public static void requestPermissions(@android.support.annotation.NonNull
    android.app.Fragment fragment, @android.support.annotation.NonNull
    java.lang.String[] permissions, int requestCode) {
        android.support.v13.app.FragmentCompat.IMPL.requestPermissions(fragment, permissions, requestCode);
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
     * @param fragment
     * 		The target fragment.
     * @param permission
     * 		A permission your app wants to request.
     * @return Whether you can show permission rationale UI.
     * @see android.support.v4.content.ContextCompat#checkSelfPermission(
    android.content.Context, String)
     * @see #requestPermissions(android.app.Fragment, String[], int)
     */
    public static boolean shouldShowRequestPermissionRationale(@android.support.annotation.NonNull
    android.app.Fragment fragment, @android.support.annotation.NonNull
    java.lang.String permission) {
        return android.support.v13.app.FragmentCompat.IMPL.shouldShowRequestPermissionRationale(fragment, permission);
    }
}

