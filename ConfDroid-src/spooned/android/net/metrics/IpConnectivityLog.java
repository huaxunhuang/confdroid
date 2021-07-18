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
package android.net.metrics;


/**
 * Class for logging IpConnectvity events with IpConnectivityMetrics
 * {@hide }
 */
public class IpConnectivityLog {
    private static final java.lang.String TAG = android.net.metrics.IpConnectivityLog.class.getSimpleName();

    private static final boolean DBG = false;

    public static final java.lang.String SERVICE_NAME = "connmetrics";

    private android.net.IIpConnectivityMetrics mService;

    public IpConnectivityLog() {
    }

    @com.android.internal.annotations.VisibleForTesting
    public IpConnectivityLog(android.net.IIpConnectivityMetrics service) {
        mService = service;
    }

    private boolean checkLoggerService() {
        if (mService != null) {
            return true;
        }
        final android.net.IIpConnectivityMetrics service = IIpConnectivityMetrics.Stub.asInterface(android.os.ServiceManager.getService(android.net.metrics.IpConnectivityLog.SERVICE_NAME));
        if (service == null) {
            return false;
        }
        // Two threads racing here will write the same pointer because getService
        // is idempotent once MetricsLoggerService is initialized.
        mService = service;
        return true;
    }

    /**
     * Log an IpConnectivity event.
     *
     * @param timestamp
     * 		is the epoch timestamp of the event in ms.
     * @param data
     * 		is a Parcelable instance representing the event.
     * @return true if the event was successfully logged.
     */
    public boolean log(long timestamp, android.os.Parcelable data) {
        if (!checkLoggerService()) {
            if (android.net.metrics.IpConnectivityLog.DBG) {
                android.util.Log.d(android.net.metrics.IpConnectivityLog.TAG, android.net.metrics.IpConnectivityLog.SERVICE_NAME + " service was not ready");
            }
            return false;
        }
        try {
            int left = mService.logEvent(new android.net.ConnectivityMetricsEvent(timestamp, 0, 0, data));
            return left >= 0;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.net.metrics.IpConnectivityLog.TAG, "Error logging event", e);
            return false;
        }
    }

    public void log(android.os.Parcelable event) {
        log(java.lang.System.currentTimeMillis(), event);
    }
}

