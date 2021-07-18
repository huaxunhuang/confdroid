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
package android.support.v4.app;


/**
 * Abstract service to receive side channel notifications sent from
 * {@link android.support.v4.app.NotificationManagerCompat}.
 *
 * <p>To receive side channel notifications, extend this service and register it in your
 * android manifest with an intent filter for the BIND_NOTIFICATION_SIDE_CHANNEL action.
 * Note: you must also have an enabled
 * {@link android.service.notification.NotificationListenerService} within your package.
 *
 * <p>Example AndroidManifest.xml addition:
 * <pre>
 * &lt;service android:name="com.example.NotificationSideChannelService"&gt;
 *     &lt;intent-filter&gt;
 *         &lt;action android:name="android.support.BIND_NOTIFICATION_SIDE_CHANNEL" /&gt;
 *     &lt;/intent-filter&gt;
 * &lt;/service&gt;</pre>
 */
public abstract class NotificationCompatSideChannelService extends android.app.Service {
    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (intent.getAction().equals(android.support.v4.app.NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL)) {
            // Block side channel service connections if the current sdk has no need for
            // side channeling.
            if (android.os.Build.VERSION.SDK_INT > android.support.v4.app.NotificationManagerCompat.MAX_SIDE_CHANNEL_SDK_VERSION) {
                return null;
            }
            return new android.support.v4.app.NotificationCompatSideChannelService.NotificationSideChannelStub();
        }
        return null;
    }

    /**
     * Handle a side-channeled notification being posted.
     */
    public abstract void notify(java.lang.String packageName, int id, java.lang.String tag, android.app.Notification notification);

    /**
     * Handle a side-channelled notification being cancelled.
     */
    public abstract void cancel(java.lang.String packageName, int id, java.lang.String tag);

    /**
     * Handle the side-channelled cancelling of all notifications for a package.
     */
    public abstract void cancelAll(java.lang.String packageName);

    private class NotificationSideChannelStub extends android.support.v4.app.INotificationSideChannel.Stub {
        NotificationSideChannelStub() {
        }

        @java.lang.Override
        public void notify(java.lang.String packageName, int id, java.lang.String tag, android.app.Notification notification) throws android.os.RemoteException {
            checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            try {
                android.support.v4.app.NotificationCompatSideChannelService.this.notify(packageName, id, tag, notification);
            } finally {
                restoreCallingIdentity(idToken);
            }
        }

        @java.lang.Override
        public void cancel(java.lang.String packageName, int id, java.lang.String tag) throws android.os.RemoteException {
            checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            try {
                android.support.v4.app.NotificationCompatSideChannelService.this.cancel(packageName, id, tag);
            } finally {
                restoreCallingIdentity(idToken);
            }
        }

        @java.lang.Override
        public void cancelAll(java.lang.String packageName) {
            checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            try {
                android.support.v4.app.NotificationCompatSideChannelService.this.cancelAll(packageName);
            } finally {
                restoreCallingIdentity(idToken);
            }
        }
    }

    void checkPermission(int callingUid, java.lang.String packageName) {
        for (java.lang.String validPackage : getPackageManager().getPackagesForUid(callingUid)) {
            if (validPackage.equals(packageName)) {
                return;
            }
        }
        throw new java.lang.SecurityException((("NotificationSideChannelService: Uid " + callingUid) + " is not authorized for package ") + packageName);
    }
}

