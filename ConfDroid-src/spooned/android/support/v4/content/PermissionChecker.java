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
 * limitations under the License.
 */
package android.support.v4.content;


/**
 * This class provides permission check APIs that verify both the
 * permission and the associated app op for this permission if
 * such is defined.
 * <p>
 * In the new permission model permissions with protection level
 * dangerous are runtime permissions. For apps targeting {@link android.os.Build.VERSION_CODES#M}
 * and above the user may not grant such permissions or revoke
 * them at any time. For apps targeting API lower than {@link android.os.Build.VERSION_CODES#M}
 * these permissions are always granted as such apps do not expect
 * permission revocations and would crash. Therefore, when the
 * user disables a permission for a legacy app in the UI the
 * platform disables the APIs guarded by this permission making
 * them a no-op which is doing nothing or returning an empty
 * result or default error.
 * </p>
 * <p>
 * It is important that when you perform an operation on behalf of
 * another app you use these APIs to check for permissions as the
 * app may be a legacy app that does not participate in the new
 * permission model for which the user had disabled the "permission"
 * which is achieved by disallowing the corresponding app op.
 * </p>
 */
public final class PermissionChecker {
    /**
     * Permission result: The permission is granted.
     */
    public static final int PERMISSION_GRANTED = android.content.pm.PackageManager.PERMISSION_GRANTED;

    /**
     * Permission result: The permission is denied.
     */
    public static final int PERMISSION_DENIED = android.content.pm.PackageManager.PERMISSION_DENIED;

    /**
     * Permission result: The permission is denied because the app op is not allowed.
     */
    public static final int PERMISSION_DENIED_APP_OP = android.content.pm.PackageManager.PERMISSION_DENIED - 1;

    @android.support.annotation.IntDef({ android.support.v4.content.PermissionChecker.PERMISSION_GRANTED, android.support.v4.content.PermissionChecker.PERMISSION_DENIED, android.support.v4.content.PermissionChecker.PERMISSION_DENIED_APP_OP })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PermissionResult {}

    private PermissionChecker() {
        /* do nothing */
    }

    /**
     * Checks whether a given package in a UID and PID has a given permission
     * and whether the app op that corresponds to this permission is allowed.
     *
     * @param context
     * 		Context for accessing resources.
     * @param permission
     * 		The permission to check.
     * @param pid
     * 		The process id for which to check.
     * @param uid
     * 		The uid for which to check.
     * @param packageName
     * 		The package name for which to check. If null the
     * 		the first package for the calling UID will be used.
     * @return The permission check result which is either {@link #PERMISSION_GRANTED}
    or {@link #PERMISSION_DENIED} or {@link #PERMISSION_DENIED_APP_OP}.
     */
    public static int checkPermission(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    java.lang.String permission, int pid, int uid, java.lang.String packageName) {
        if (context.checkPermission(permission, pid, uid) == android.content.pm.PackageManager.PERMISSION_DENIED) {
            return android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
        }
        java.lang.String op = android.support.v4.app.AppOpsManagerCompat.permissionToOp(permission);
        if (op == null) {
            return android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
        }
        if (packageName == null) {
            java.lang.String[] packageNames = context.getPackageManager().getPackagesForUid(uid);
            if ((packageNames == null) || (packageNames.length <= 0)) {
                return android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
            }
            packageName = packageNames[0];
        }
        if (android.support.v4.app.AppOpsManagerCompat.noteProxyOp(context, op, packageName) != android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED) {
            return android.support.v4.content.PermissionChecker.PERMISSION_DENIED_APP_OP;
        }
        return android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
    }

    /**
     * Checks whether your app has a given permission and whether the app op
     * that corresponds to this permission is allowed.
     *
     * @param context
     * 		Context for accessing resources.
     * @param permission
     * 		The permission to check.
     * @return The permission check result which is either {@link #PERMISSION_GRANTED}
    or {@link #PERMISSION_DENIED} or {@link #PERMISSION_DENIED_APP_OP}.
     */
    public static int checkSelfPermission(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    java.lang.String permission) {
        return android.support.v4.content.PermissionChecker.checkPermission(context, permission, android.os.Process.myPid(), android.os.Process.myUid(), context.getPackageName());
    }

    /**
     * Checks whether the IPC you are handling has a given permission and whether
     * the app op that corresponds to this permission is allowed.
     *
     * @param context
     * 		Context for accessing resources.
     * @param permission
     * 		The permission to check.
     * @param packageName
     * 		The package name making the IPC. If null the
     * 		the first package for the calling UID will be used.
     * @return The permission check result which is either {@link #PERMISSION_GRANTED}
    or {@link #PERMISSION_DENIED} or {@link #PERMISSION_DENIED_APP_OP}.
     */
    public static int checkCallingPermission(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    java.lang.String permission, java.lang.String packageName) {
        if (android.os.Binder.getCallingPid() == android.os.Process.myPid()) {
            return android.content.pm.PackageManager.PERMISSION_DENIED;
        }
        return android.support.v4.content.PermissionChecker.checkPermission(context, permission, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), packageName);
    }

    /**
     * Checks whether the IPC you are handling or your app has a given permission
     * and whether the app op that corresponds to this permission is allowed.
     *
     * @param context
     * 		Context for accessing resources.
     * @param permission
     * 		The permission to check.
     * @return The permission check result which is either {@link #PERMISSION_GRANTED}
    or {@link #PERMISSION_DENIED} or {@link #PERMISSION_DENIED_APP_OP}.
     */
    public static int checkCallingOrSelfPermission(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    java.lang.String permission) {
        java.lang.String packageName = (android.os.Binder.getCallingPid() == android.os.Process.myPid()) ? context.getPackageName() : null;
        return android.support.v4.content.PermissionChecker.checkPermission(context, permission, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), packageName);
    }
}

