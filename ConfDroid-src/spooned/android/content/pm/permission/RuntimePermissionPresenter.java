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
package android.content.pm.permission;


/**
 * This class provides information about runtime permissions for a specific
 * app or all apps. This information is dedicated for presentation purposes
 * and does not necessarily reflect the individual permissions requested/
 * granted to an app as the platform may be grouping permissions to improve
 * presentation and help the user make an informed choice. For example, all
 * runtime permissions in the same permission group may be presented as a
 * single permission in the UI.
 *
 * @unknown 
 */
public final class RuntimePermissionPresenter {
    private static final java.lang.String TAG = "RuntimePermPresenter";

    /**
     * The key for retrieving the result from the returned bundle.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_RESULT = "android.content.pm.permission.RuntimePermissionPresenter.key.result";

    /**
     * Listener for delivering a result.
     */
    public static abstract class OnResultCallback {
        /**
         * The result for {@link #getAppPermissions(String, OnResultCallback, Handler)}.
         *
         * @param permissions
         * 		The permissions list.
         */
        public void onGetAppPermissions(@android.annotation.NonNull
        java.util.List<android.content.pm.permission.RuntimePermissionPresentationInfo> permissions) {
            /* do nothing - stub */
        }
    }

    private static final java.lang.Object sLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("sLock")
    private static android.content.pm.permission.RuntimePermissionPresenter sInstance;

    private final android.content.pm.permission.RuntimePermissionPresenter.RemoteService mRemoteService;

    /**
     * Gets the singleton runtime permission presenter.
     *
     * @param context
     * 		Context for accessing resources.
     * @return The singleton instance.
     */
    public static android.content.pm.permission.RuntimePermissionPresenter getInstance(@android.annotation.NonNull
    android.content.Context context) {
        synchronized(android.content.pm.permission.RuntimePermissionPresenter.sLock) {
            if (android.content.pm.permission.RuntimePermissionPresenter.sInstance == null) {
                android.content.pm.permission.RuntimePermissionPresenter.sInstance = new android.content.pm.permission.RuntimePermissionPresenter(context.getApplicationContext());
            }
            return android.content.pm.permission.RuntimePermissionPresenter.sInstance;
        }
    }

    private RuntimePermissionPresenter(android.content.Context context) {
        mRemoteService = new android.content.pm.permission.RuntimePermissionPresenter.RemoteService(context);
    }

    /**
     * Gets the runtime permissions for an app.
     *
     * @param packageName
     * 		The package for which to query.
     * @param callback
     * 		Callback to receive the result.
     * @param handler
     * 		Handler on which to invoke the callback.
     */
    public void getAppPermissions(@android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    android.content.pm.permission.RuntimePermissionPresenter.OnResultCallback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = packageName;
        args.arg2 = callback;
        args.arg3 = handler;
        android.os.Message message = mRemoteService.obtainMessage(android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_GET_APP_PERMISSIONS, args);
        mRemoteService.processMessage(message);
    }

    /**
     * Revoke the permission {@code permissionName} for app {@code packageName}
     *
     * @param packageName
     * 		The package for which to revoke
     * @param permissionName
     * 		The permission to revoke
     */
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permissionName) {
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = packageName;
        args.arg2 = permissionName;
        android.os.Message message = mRemoteService.obtainMessage(android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_REVOKE_APP_PERMISSIONS, args);
        mRemoteService.processMessage(message);
    }

    private static final class RemoteService extends android.os.Handler implements android.content.ServiceConnection {
        private static final long UNBIND_TIMEOUT_MILLIS = 10000;

        public static final int MSG_GET_APP_PERMISSIONS = 1;

        public static final int MSG_GET_APPS_USING_PERMISSIONS = 2;

        public static final int MSG_UNBIND = 3;

        public static final int MSG_REVOKE_APP_PERMISSIONS = 4;

        private final java.lang.Object mLock = new java.lang.Object();

        private final android.content.Context mContext;

        @com.android.internal.annotations.GuardedBy("mLock")
        private final java.util.List<android.os.Message> mPendingWork = new java.util.ArrayList<>();

        @com.android.internal.annotations.GuardedBy("mLock")
        private android.content.pm.permission.IRuntimePermissionPresenter mRemoteInstance;

        @com.android.internal.annotations.GuardedBy("mLock")
        private boolean mBound;

        public RemoteService(android.content.Context context) {
            super(context.getMainLooper(), null, false);
            mContext = context;
        }

        public void processMessage(android.os.Message message) {
            synchronized(mLock) {
                if (!mBound) {
                    android.content.Intent intent = new android.content.Intent(android.permissionpresenterservice.RuntimePermissionPresenterService.SERVICE_INTERFACE);
                    intent.setPackage(mContext.getPackageManager().getPermissionControllerPackageName());
                    mBound = mContext.bindService(intent, this, android.content.Context.BIND_AUTO_CREATE);
                }
                mPendingWork.add(message);
                scheduleNextMessageIfNeededLocked();
            }
        }

        @java.lang.Override
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized(mLock) {
                mRemoteInstance = IRuntimePermissionPresenter.Stub.asInterface(service);
                scheduleNextMessageIfNeededLocked();
            }
        }

        @java.lang.Override
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized(mLock) {
                mRemoteInstance = null;
            }
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_GET_APP_PERMISSIONS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final java.lang.String packageName = ((java.lang.String) (args.arg1));
                        final android.content.pm.permission.RuntimePermissionPresenter.OnResultCallback callback = ((android.content.pm.permission.RuntimePermissionPresenter.OnResultCallback) (args.arg2));
                        final android.os.Handler handler = ((android.os.Handler) (args.arg3));
                        args.recycle();
                        final android.content.pm.permission.IRuntimePermissionPresenter remoteInstance;
                        synchronized(mLock) {
                            remoteInstance = mRemoteInstance;
                        }
                        if (remoteInstance == null) {
                            return;
                        }
                        try {
                            remoteInstance.getAppPermissions(packageName, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() {
                                @java.lang.Override
                                public void onResult(android.os.Bundle result) {
                                    final java.util.List<android.content.pm.permission.RuntimePermissionPresentationInfo> reportedPermissions;
                                    java.util.List<android.content.pm.permission.RuntimePermissionPresentationInfo> permissions = null;
                                    if (result != null) {
                                        permissions = result.getParcelableArrayList(android.content.pm.permission.RuntimePermissionPresenter.KEY_RESULT);
                                    }
                                    if (permissions == null) {
                                        permissions = java.util.Collections.emptyList();
                                    }
                                    reportedPermissions = permissions;
                                    if (handler != null) {
                                        handler.post(new java.lang.Runnable() {
                                            @java.lang.Override
                                            public void run() {
                                                callback.onGetAppPermissions(reportedPermissions);
                                            }
                                        });
                                    } else {
                                        callback.onGetAppPermissions(reportedPermissions);
                                    }
                                }
                            }, this));
                        } catch (android.os.RemoteException re) {
                            android.util.Log.e(android.content.pm.permission.RuntimePermissionPresenter.TAG, "Error getting app permissions", re);
                        }
                        scheduleUnbind();
                    }
                    break;
                case android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_UNBIND :
                    {
                        synchronized(mLock) {
                            if (mBound) {
                                mContext.unbindService(this);
                                mBound = false;
                            }
                            mRemoteInstance = null;
                        }
                    }
                    break;
                case android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_REVOKE_APP_PERMISSIONS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final java.lang.String packageName = ((java.lang.String) (args.arg1));
                        final java.lang.String permissionName = ((java.lang.String) (args.arg2));
                        args.recycle();
                        final android.content.pm.permission.IRuntimePermissionPresenter remoteInstance;
                        synchronized(mLock) {
                            remoteInstance = mRemoteInstance;
                        }
                        if (remoteInstance == null) {
                            return;
                        }
                        try {
                            remoteInstance.revokeRuntimePermission(packageName, permissionName);
                        } catch (android.os.RemoteException re) {
                            android.util.Log.e(android.content.pm.permission.RuntimePermissionPresenter.TAG, "Error getting app permissions", re);
                        }
                    }
                    break;
            }
            synchronized(mLock) {
                scheduleNextMessageIfNeededLocked();
            }
        }

        @com.android.internal.annotations.GuardedBy("mLock")
        private void scheduleNextMessageIfNeededLocked() {
            if ((mBound && (mRemoteInstance != null)) && (!mPendingWork.isEmpty())) {
                android.os.Message nextMessage = mPendingWork.remove(0);
                sendMessage(nextMessage);
            }
        }

        private void scheduleUnbind() {
            removeMessages(android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_UNBIND);
            sendEmptyMessageDelayed(android.content.pm.permission.RuntimePermissionPresenter.RemoteService.MSG_UNBIND, android.content.pm.permission.RuntimePermissionPresenter.RemoteService.UNBIND_TIMEOUT_MILLIS);
        }
    }
}

