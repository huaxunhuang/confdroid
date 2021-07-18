/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.bordeaux.services;


// TODO: add functionality to detect speed (use GPS) when needed
// withouth draining the battery quickly
public class LocationStatsAggregator extends android.bordeaux.services.Aggregator {
    final java.lang.String TAG = "LocationStatsAggregator";

    public static final java.lang.String CURRENT_LOCATION = "Current Location";

    public static final java.lang.String CURRENT_SPEED = "Current Speed";

    public static final java.lang.String UNKNOWN_LOCATION = "Unknown Location";

    private static final long REPEAT_INTERVAL = 120000;

    private static final long FRESH_THRESHOLD = 90000;

    private static final int LOCATION_CHANGE = 1;

    // record time when the location provider is set
    private long mProviderSetTime;

    private android.os.Handler mHandler;

    private android.os.HandlerThread mHandlerThread;

    private android.app.AlarmManager mAlarmManager;

    private android.location.LocationManager mLocationManager;

    private android.bordeaux.services.ClusterManager mClusterManager;

    private android.location.Criteria mCriteria = new android.location.Criteria();

    private android.bordeaux.services.LocationStatsAggregator.LocationUpdater mLocationUpdater;

    private android.content.Context mContext;

    private android.app.PendingIntent mPendingIntent;

    // Fake location, used for testing.
    private java.lang.String mFakeLocation = null;

    public LocationStatsAggregator(final android.content.Context context) {
        mLocationManager = ((android.location.LocationManager) (context.getSystemService(android.content.Context.LOCATION_SERVICE)));
        mAlarmManager = ((android.app.AlarmManager) (context.getSystemService(android.content.Context.ALARM_SERVICE)));
        setClusteringThread(context);
        mCriteria.setAccuracy(android.location.Criteria.ACCURACY_COARSE);
        mCriteria.setPowerRequirement(android.location.Criteria.POWER_LOW);
        /* mCriteria.setAltitudeRequired(false);
        mCriteria.setBearingRequired(false);
        mCriteria.setSpeedRequired(true);
         */
        mCriteria.setCostAllowed(true);
        android.content.IntentFilter filter = new android.content.IntentFilter(android.bordeaux.services.LocationStatsAggregator.LocationUpdater.LOCATION_UPDATE);
        mLocationUpdater = new android.bordeaux.services.LocationStatsAggregator.LocationUpdater();
        context.registerReceiver(mLocationUpdater, filter);
        android.content.Intent intent = new android.content.Intent(android.bordeaux.services.LocationStatsAggregator.LocationUpdater.LOCATION_UPDATE);
        mContext = context;
        mPendingIntent = android.app.PendingIntent.getBroadcast(mContext, 0, intent, 0);
        // 
        mAlarmManager.setInexactRepeating(android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP, android.os.SystemClock.elapsedRealtime() + 30000, android.bordeaux.services.LocationStatsAggregator.REPEAT_INTERVAL, mPendingIntent);
    }

    public void release() {
        mContext.unregisterReceiver(mLocationUpdater);
        mAlarmManager.cancel(mPendingIntent);
    }

    public java.lang.String[] getListOfFeatures() {
        java.lang.String[] list = new java.lang.String[]{ android.bordeaux.services.LocationStatsAggregator.CURRENT_LOCATION };
        return list;
    }

    public java.util.Map<java.lang.String, java.lang.String> getFeatureValue(java.lang.String featureName) {
        java.util.HashMap<java.lang.String, java.lang.String> feature = new java.util.HashMap<java.lang.String, java.lang.String>();
        if (featureName.equals(android.bordeaux.services.LocationStatsAggregator.CURRENT_LOCATION)) {
            // TODO: check last known location first before sending out location request.
            /* Location location =
            mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             */
            java.lang.String location = mClusterManager.getSemanticLocation();
            if (!location.equals(android.bordeaux.services.LocationStatsAggregator.UNKNOWN_LOCATION)) {
                if (mFakeLocation != null) {
                    feature.put(android.bordeaux.services.LocationStatsAggregator.CURRENT_LOCATION, mFakeLocation);
                } else {
                    feature.put(android.bordeaux.services.LocationStatsAggregator.CURRENT_LOCATION, location);
                }
            }
        }
        return ((java.util.Map) (feature));
    }

    public java.util.List<java.lang.String> getClusterNames() {
        return mClusterManager.getClusterNames();
    }

    // set a fake location using cluster name.
    // Set an empty string "" to disable the fake location
    public void setFakeLocation(java.lang.String name) {
        if ((name != null) && (name.length() != 0))
            mFakeLocation = name;
        else
            mFakeLocation = null;

    }

    private android.location.Location getLastKnownLocation() {
        java.util.List<java.lang.String> providers = mLocationManager.getAllProviders();
        android.location.Location bestResult = null;
        float bestAccuracy = java.lang.Float.MAX_VALUE;
        long bestTime;
        // get the latest location data
        long currTime = java.lang.System.currentTimeMillis();
        for (java.lang.String provider : providers) {
            android.location.Location location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();
                if (((currTime - time) < android.bordeaux.services.LocationStatsAggregator.FRESH_THRESHOLD) && (accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
            }
        }
        if (bestResult != null) {
            android.util.Log.i(TAG, "found location for free: " + bestResult);
        }
        return bestResult;
    }

    private class LocationUpdater extends android.content.BroadcastReceiver {
        java.lang.String TAG = "LocationUpdater";

        public static final java.lang.String LOCATION_UPDATE = "android.bordeaux.services.LOCATION_UPDATE";

        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.location.Location location = getLastKnownLocation();
            if (location == null) {
                java.lang.String provider = mLocationManager.getBestProvider(mCriteria, true);
                android.util.Log.i(TAG, "Best Available Location Provider: " + provider);
                mLocationManager.requestSingleUpdate(provider, mLocationListener, mHandlerThread.getLooper());
            } else {
                mHandler.sendMessage(mHandler.obtainMessage(android.bordeaux.services.LocationStatsAggregator.LOCATION_CHANGE, location));
            }
        }
    }

    private void setClusteringThread(android.content.Context context) {
        mClusterManager = new android.bordeaux.services.ClusterManager(context);
        mHandlerThread = new android.os.HandlerThread("Location Handler", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new android.os.Handler(mHandlerThread.getLooper()) {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (!(msg.obj instanceof android.location.Location)) {
                    return;
                }
                android.location.Location location = ((android.location.Location) (msg.obj));
                switch (msg.what) {
                    case android.bordeaux.services.LocationStatsAggregator.LOCATION_CHANGE :
                        mClusterManager.addSample(location);
                        break;
                    default :
                        super.handleMessage(msg);
                }
            }
        };
    }

    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        private static final java.lang.String TAG = "LocationListener";

        public void onLocationChanged(android.location.Location location) {
            mHandler.sendMessage(mHandler.obtainMessage(android.bordeaux.services.LocationStatsAggregator.LOCATION_CHANGE, location));
            mLocationManager.removeUpdates(this);
        }

        public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) {
        }

        public void onProviderEnabled(java.lang.String provider) {
        }

        public void onProviderDisabled(java.lang.String provider) {
        }
    };
}

