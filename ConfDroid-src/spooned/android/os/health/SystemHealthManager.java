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
package android.os.health;


/**
 * Provides access to data about how various system resources are used by applications.
 *
 * @unknown <p>
If you are going to be using this class to log your application's resource usage,
please consider the amount of resources (battery, network, etc) that will be used
by the logging itself.  It can be substantial.
<p>
<b>Battery Usage</b><br>
The statistics related to power (battery) usage are recorded since the device
was last unplugged. It is expected that applications schedule more work to do
while the device is plugged in (e.g. using {@link android.app.job.JobScheduler
JobScheduler}), and while that can affect charging rates, it is still preferable
to actually draining the battery.
 */
public class SystemHealthManager {
    private final com.android.internal.app.IBatteryStats mBatteryStats;

    /**
     * Construct a new SystemHealthManager object.
     *
     * @unknown 
     */
    public SystemHealthManager() {
        mBatteryStats = IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService(android.os.BatteryStats.SERVICE_NAME));
    }

    /**
     * Obtain a SystemHealthManager object for the supplied context.
     *
     * @unknown 
     */
    public static android.os.health.SystemHealthManager from(android.content.Context context) {
        return ((android.os.health.SystemHealthManager) (context.getSystemService(android.content.Context.SYSTEM_HEALTH_SERVICE)));
    }

    /**
     * Return a {@link HealthStats} object containing a snapshot of system health
     * metrics for the given uid (user-id, which in usually corresponds to application).
     *
     * @unknown An application must hold the {@link android.Manifest.permission#BATTERY_STATS
    android.permission.BATTERY_STATS} permission in order to retrieve any HealthStats
    other than its own.
     * @param uid
     * 		User ID for a given application.
     * @return A {@link HealthStats} object containing the metrics for the requested
    application. The keys for this HealthStats object will be from the {@link UidHealthStats}
    class.
     * @see Process#myUid() Process.myUid()
     */
    public android.os.health.HealthStats takeUidSnapshot(int uid) {
        try {
            final android.os.health.HealthStatsParceler parceler = mBatteryStats.takeUidSnapshot(uid);
            return parceler.getHealthStats();
        } catch (android.os.RemoteException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }

    /**
     * Return a {@link HealthStats} object containing a snapshot of system health
     * metrics for the application calling this API. This method is the same as calling
     * {@code takeUidSnapshot(Process.myUid())}.
     *
     * @return A {@link HealthStats} object containing the metrics for this application. The keys
    for this HealthStats object will be from the {@link UidHealthStats} class.
     */
    public android.os.health.HealthStats takeMyUidSnapshot() {
        return takeUidSnapshot(android.os.Process.myUid());
    }

    /**
     * Return a {@link HealthStats} object containing a snapshot of system health
     * metrics for the given uids (user-id, which in usually corresponds to application).
     *
     * @unknown An application must hold the {@link android.Manifest.permission#BATTERY_STATS
    android.permission.BATTERY_STATS} permission in order to retrieve any HealthStats
    other than its own.
     * @param uids
     * 		An array of User IDs to retrieve.
     * @return An array of {@link HealthStats} objects containing the metrics for each of
    the requested uids. The keys for this HealthStats object will be from the
    {@link UidHealthStats} class.
     */
    public android.os.health.HealthStats[] takeUidSnapshots(int[] uids) {
        try {
            final android.os.health.HealthStatsParceler[] parcelers = mBatteryStats.takeUidSnapshots(uids);
            final android.os.health.HealthStats[] results = new android.os.health.HealthStats[uids.length];
            final int N = uids.length;
            for (int i = 0; i < N; i++) {
                results[i] = parcelers[i].getHealthStats();
            }
            return results;
        } catch (android.os.RemoteException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }
}

