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
package android.permissionpresenterservice;


/**
 * This service presents information regarding runtime permissions that is
 * used for presenting them in the UI. Runtime permissions are presented as
 * a single permission in the UI but may be composed of several individual
 * permissions.
 *
 * @see RuntimePermissionPresenter
 * @see RuntimePermissionPresentationInfo
 * @unknown 
 */
@android.annotation.SystemApi
public abstract class RuntimePermissionPresenterService extends android.app.Service {
    /**
     * The {@link Intent} action that must be declared as handled by a service
     * in its manifest for the system to recognize it as a runtime permission
     * presenter service.
     */
    public static final java.lang.String SERVICE_INTERFACE = "android.permissionpresenterservice.RuntimePermissionPresenterService";

    // No need for locking - always set first and never modified
    private android.os.Handler mHandler;

    @java.lang.Override
    public final void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        mHandler = new android.permissionpresenterservice.RuntimePermissionPresenterService.MyHandler(base.getMainLooper());
    }

    /**
     * Gets the runtime permissions for an app.
     *
     * @param packageName
     * 		The package for which to query.
     */
    public abstract java.util.List<android.content.pm.permission.RuntimePermissionPresentationInfo> onGetAppPermissions(java.lang.String packageName);

    /**
     * Gets the apps that use runtime permissions.
     *
     * @param system
     * 		Whether to return only the system apps or only the non-system ones.
     * @return The app list.
     */
    public abstract java.util.List<android.content.pm.ApplicationInfo> onGetAppsUsingPermissions(boolean system);

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return new android.content.pm.permission.IRuntimePermissionPresenter.Stub() {
            @java.lang.Override
            public void getAppPermissions(java.lang.String packageName, android.os.RemoteCallback callback) {
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = packageName;
                args.arg2 = callback;
                mHandler.obtainMessage(android.permissionpresenterservice.RuntimePermissionPresenterService.MyHandler.MSG_GET_APP_PERMISSIONS, args).sendToTarget();
            }

            @java.lang.Override
            public void getAppsUsingPermissions(boolean system, android.os.RemoteCallback callback) {
                mHandler.obtainMessage(android.permissionpresenterservice.RuntimePermissionPresenterService.MyHandler.MSG_GET_APPS_USING_PERMISSIONS, system ? 1 : 0, 0, callback).sendToTarget();
            }
        };
    }

    private final class MyHandler extends android.os.Handler {
        public static final int MSG_GET_APP_PERMISSIONS = 1;

        public static final int MSG_GET_APPS_USING_PERMISSIONS = 2;

        public MyHandler(android.os.Looper looper) {
            super(looper, null, false);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.permissionpresenterservice.RuntimePermissionPresenterService.MyHandler.MSG_GET_APP_PERMISSIONS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        java.lang.String packageName = ((java.lang.String) (args.arg1));
                        android.os.RemoteCallback callback = ((android.os.RemoteCallback) (args.arg2));
                        args.recycle();
                        java.util.List<android.content.pm.permission.RuntimePermissionPresentationInfo> permissions = onGetAppPermissions(packageName);
                        if ((permissions != null) && (!permissions.isEmpty())) {
                            android.os.Bundle result = new android.os.Bundle();
                            result.putParcelableList(android.content.pm.permission.RuntimePermissionPresenter.KEY_RESULT, permissions);
                            callback.sendResult(result);
                        } else {
                            callback.sendResult(null);
                        }
                    }
                    break;
                case android.permissionpresenterservice.RuntimePermissionPresenterService.MyHandler.MSG_GET_APPS_USING_PERMISSIONS :
                    {
                        android.os.RemoteCallback callback = ((android.os.RemoteCallback) (msg.obj));
                        final boolean system = msg.arg1 == 1;
                        java.util.List<android.content.pm.ApplicationInfo> apps = onGetAppsUsingPermissions(system);
                        if ((apps != null) && (!apps.isEmpty())) {
                            android.os.Bundle result = new android.os.Bundle();
                            result.putParcelableList(android.content.pm.permission.RuntimePermissionPresenter.KEY_RESULT, apps);
                            callback.sendResult(result);
                        } else {
                            callback.sendResult(null);
                        }
                    }
                    break;
            }
        }
    }
}

