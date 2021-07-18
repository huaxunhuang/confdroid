/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.media;


/**
 * Watches for media route provider services to be installed.
 * Adds a provider to the media router for each registered service.
 *
 * @see RegisteredMediaRouteProvider
 */
final class RegisteredMediaRouteProviderWatcher {
    private final android.content.Context mContext;

    private final android.support.v7.media.RegisteredMediaRouteProviderWatcher.Callback mCallback;

    private final android.os.Handler mHandler;

    private final android.content.pm.PackageManager mPackageManager;

    private final java.util.ArrayList<android.support.v7.media.RegisteredMediaRouteProvider> mProviders = new java.util.ArrayList<android.support.v7.media.RegisteredMediaRouteProvider>();

    private boolean mRunning;

    public RegisteredMediaRouteProviderWatcher(android.content.Context context, android.support.v7.media.RegisteredMediaRouteProviderWatcher.Callback callback) {
        mContext = context;
        mCallback = callback;
        mHandler = new android.os.Handler();
        mPackageManager = context.getPackageManager();
    }

    public void start() {
        if (!mRunning) {
            mRunning = true;
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction(android.content.Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(android.content.Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(android.content.Intent.ACTION_PACKAGE_CHANGED);
            filter.addAction(android.content.Intent.ACTION_PACKAGE_REPLACED);
            filter.addAction(android.content.Intent.ACTION_PACKAGE_RESTARTED);
            filter.addDataScheme("package");
            mContext.registerReceiver(mScanPackagesReceiver, filter, null, mHandler);
            // Scan packages.
            // Also has the side-effect of restarting providers if needed.
            mHandler.post(mScanPackagesRunnable);
        }
    }

    public void stop() {
        if (mRunning) {
            mRunning = false;
            mContext.unregisterReceiver(mScanPackagesReceiver);
            mHandler.removeCallbacks(mScanPackagesRunnable);
            // Stop all providers.
            for (int i = mProviders.size() - 1; i >= 0; i--) {
                mProviders.get(i).stop();
            }
        }
    }

    void scanPackages() {
        if (!mRunning) {
            return;
        }
        // Add providers for all new services.
        // Reorder the list so that providers left at the end will be the ones to remove.
        int targetIndex = 0;
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaRouteProviderService.SERVICE_INTERFACE);
        for (android.content.pm.ResolveInfo resolveInfo : mPackageManager.queryIntentServices(intent, 0)) {
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null) {
                int sourceIndex = findProvider(serviceInfo.packageName, serviceInfo.name);
                if (sourceIndex < 0) {
                    android.support.v7.media.RegisteredMediaRouteProvider provider = new android.support.v7.media.RegisteredMediaRouteProvider(mContext, new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name));
                    provider.start();
                    mProviders.add(targetIndex++, provider);
                    mCallback.addProvider(provider);
                } else
                    if (sourceIndex >= targetIndex) {
                        android.support.v7.media.RegisteredMediaRouteProvider provider = mProviders.get(sourceIndex);
                        provider.start();// restart the provider if needed

                        provider.rebindIfDisconnected();
                        java.util.Collections.swap(mProviders, sourceIndex, targetIndex++);
                    }

            }
        }
        // Remove providers for missing services.
        if (targetIndex < mProviders.size()) {
            for (int i = mProviders.size() - 1; i >= targetIndex; i--) {
                android.support.v7.media.RegisteredMediaRouteProvider provider = mProviders.get(i);
                mCallback.removeProvider(provider);
                mProviders.remove(provider);
                provider.stop();
            }
        }
    }

    private int findProvider(java.lang.String packageName, java.lang.String className) {
        int count = mProviders.size();
        for (int i = 0; i < count; i++) {
            android.support.v7.media.RegisteredMediaRouteProvider provider = mProviders.get(i);
            if (provider.hasComponentName(packageName, className)) {
                return i;
            }
        }
        return -1;
    }

    private final android.content.BroadcastReceiver mScanPackagesReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            scanPackages();
        }
    };

    private final java.lang.Runnable mScanPackagesRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            scanPackages();
        }
    };

    public interface Callback {
        void addProvider(android.support.v7.media.MediaRouteProvider provider);

        void removeProvider(android.support.v7.media.MediaRouteProvider provider);
    }
}

