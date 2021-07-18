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
package android.content.pm;


public class PackageHelperTests extends android.test.AndroidTestCase {
    private static final boolean localLOGV = true;

    public static final java.lang.String TAG = "PackageHelperTests";

    protected final java.lang.String PREFIX = "android.content.pm";

    private android.os.storage.IMountService mMs;

    private java.lang.String fullId;

    private java.lang.String fullId2;

    private android.os.storage.IMountService getMs() {
        android.os.IBinder service = android.os.ServiceManager.getService("mount");
        if (service != null) {
            return IMountService.Stub.asInterface(service);
        } else {
            android.util.Log.e(android.content.pm.PackageHelperTests.TAG, "Can't get mount service");
        }
        return null;
    }

    private void cleanupContainers() throws android.os.RemoteException {
        android.util.Log.d(android.content.pm.PackageHelperTests.TAG, "cleanUp");
        android.os.storage.IMountService ms = getMs();
        java.lang.String[] containers = ms.getSecureContainerList();
        for (int i = 0; i < containers.length; i++) {
            if (containers[i].startsWith(PREFIX)) {
                android.util.Log.d(android.content.pm.PackageHelperTests.TAG, "cleaing up " + containers[i]);
                ms.destroySecureContainer(containers[i], true);
            }
        }
    }

    void failStr(java.lang.String errMsg) {
        android.util.Log.w(android.content.pm.PackageHelperTests.TAG, "errMsg=" + errMsg);
        fail(errMsg);
    }

    void failStr(java.lang.Exception e) {
        failStr(e.getMessage());
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        if (android.content.pm.PackageHelperTests.localLOGV)
            android.util.Log.i(android.content.pm.PackageHelperTests.TAG, "Cleaning out old test containers");

        cleanupContainers();
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        tearDown();
        if (android.content.pm.PackageHelperTests.localLOGV)
            android.util.Log.i(android.content.pm.PackageHelperTests.TAG, "Cleaning out old test containers");

        cleanupContainers();
    }

    public void testMountAndPullSdCard() {
        try {
            fullId = PREFIX;
            fullId2 = com.android.internal.content.PackageHelper.createSdDir(1024 * android.net.TrafficStats.MB_IN_BYTES, fullId, "none", android.content.pm.android.os.Process.myUid(), true);
            android.util.Log.d(android.content.pm.PackageHelperTests.TAG, com.android.internal.content.PackageHelper.getSdDir(fullId));
            com.android.internal.content.PackageHelper.unMountSdDir(fullId);
            java.lang.Runnable r1 = getMountRunnable();
            java.lang.Runnable r2 = getDestroyRunnable();
            java.lang.Thread thread = new java.lang.Thread(r1);
            java.lang.Thread thread2 = new java.lang.Thread(r2);
            thread2.start();
            thread.start();
        } catch (java.lang.Exception e) {
            failStr(e);
        }
    }

    public java.lang.Runnable getMountRunnable() {
        java.lang.Runnable r = new java.lang.Runnable() {
            public void run() {
                try {
                    java.lang.Thread.sleep(5);
                    java.lang.String path = com.android.internal.content.PackageHelper.mountSdDir(fullId, "none", android.content.pm.android.os.Process.myUid());
                    android.util.Log.e(android.content.pm.PackageHelperTests.TAG, "mount done " + path);
                } catch (java.lang.IllegalArgumentException iae) {
                    throw iae;
                } catch (java.lang.Throwable t) {
                    android.util.Log.e(android.content.pm.PackageHelperTests.TAG, "mount failed", t);
                }
            }
        };
        return r;
    }

    public java.lang.Runnable getDestroyRunnable() {
        java.lang.Runnable r = new java.lang.Runnable() {
            public void run() {
                try {
                    com.android.internal.content.PackageHelper.destroySdDir(fullId);
                    android.util.Log.e(android.content.pm.PackageHelperTests.TAG, "destroy done: " + fullId);
                } catch (java.lang.Throwable t) {
                    android.util.Log.e(android.content.pm.PackageHelperTests.TAG, "destroy failed", t);
                }
            }
        };
        return r;
    }
}

