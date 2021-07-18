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
/**
 * import android.bordeaux.R;
 */
package android.bordeaux.services;


/**
 * Machine Learning service that runs in a remote process.
 * The application doesn't use this class directly.
 */
public class BordeauxService extends android.app.Service {
    private final java.lang.String TAG = "BordeauxService";

    /**
     * This is a list of callbacks that have been registered with the
     * service.
     * It's a place holder for future communications with all registered
     * clients.
     */
    final android.os.RemoteCallbackList<android.bordeaux.services.IBordeauxServiceCallback> mCallbacks = new android.os.RemoteCallbackList<android.bordeaux.services.IBordeauxServiceCallback>();

    int mValue = 0;

    android.app.NotificationManager mNotificationManager;

    android.bordeaux.services.BordeauxSessionManager mSessionManager;

    android.bordeaux.services.AggregatorManager mAggregatorManager;

    android.bordeaux.services.TimeStatsAggregator mTimeStatsAggregator;

    android.bordeaux.services.LocationStatsAggregator mLocationStatsAggregator;

    android.bordeaux.services.MotionStatsAggregator mMotionStatsAggregator;

    @java.lang.Override
    public void onCreate() {
        android.util.Log.i(TAG, "Bordeaux service created.");
        // mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mSessionManager = new android.bordeaux.services.BordeauxSessionManager(this);
        mMotionStatsAggregator = new android.bordeaux.services.MotionStatsAggregator();
        mLocationStatsAggregator = new android.bordeaux.services.LocationStatsAggregator(this);
        mTimeStatsAggregator = new android.bordeaux.services.TimeStatsAggregator();
        mAggregatorManager = android.bordeaux.services.AggregatorManager.getInstance();
        mAggregatorManager.registerAggregator(mMotionStatsAggregator, mAggregatorManager);
        mAggregatorManager.registerAggregator(mLocationStatsAggregator, mAggregatorManager);
        mAggregatorManager.registerAggregator(mTimeStatsAggregator, mAggregatorManager);
        // Display a notification about us starting.
        // TODO: don't display the notification after the service is
        // automatically started by the system, currently it's useful for
        // debugging.
        showNotification();
    }

    @java.lang.Override
    public void onDestroy() {
        // Save the sessions
        mSessionManager.saveSessions();
        // Cancel the persistent notification.
        // mNotificationManager.cancel(R.string.remote_service_started);
        // Tell the user we stopped.
        // Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
        // Unregister all callbacks.
        mCallbacks.kill();
        mLocationStatsAggregator.release();
        android.util.Log.i(TAG, "Bordeaux service stopped.");
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        // Return the requested interface.
        if (android.bordeaux.services.IBordeauxService.class.getName().equals(intent.getAction())) {
            return mBinder;
        }
        return null;
    }

    // The main interface implemented by the service.
    private final IBordeauxService.Stub mBinder = new android.bordeaux.services.IBordeauxService.Stub() {
        private android.os.IBinder getLearningSession(java.lang.Class learnerClass, java.lang.String name) {
            android.content.pm.PackageManager pm = getPackageManager();
            java.lang.String uidname = pm.getNameForUid(getCallingUid());
            android.util.Log.i(TAG, "Name for uid: " + uidname);
            android.bordeaux.services.BordeauxSessionManager.SessionKey key = mSessionManager.getSessionKey(uidname, learnerClass, name);
            android.util.Log.i(TAG, "request learning session: " + key.value);
            try {
                android.os.IBinder iLearner = mSessionManager.getSessionBinder(learnerClass, key);
                return iLearner;
            } catch (java.lang.RuntimeException e) {
                android.util.Log.e(TAG, "Error getting learning interface" + e);
                return null;
            }
        }

        public android.os.IBinder getClassifier(java.lang.String name) {
            return getLearningSession(android.bordeaux.services.Learning_MulticlassPA.class, name);
        }

        public android.os.IBinder getRanker(java.lang.String name) {
            return getLearningSession(android.bordeaux.services.Learning_StochasticLinearRanker.class, name);
        }

        public android.os.IBinder getPredictor(java.lang.String name) {
            return getLearningSession(android.bordeaux.services.Predictor.class, name);
        }

        public android.os.IBinder getAggregatorManager() {
            return ((android.os.IBinder) (mAggregatorManager));
        }

        public void registerCallback(android.bordeaux.services.IBordeauxServiceCallback cb) {
            if (cb != null)
                mCallbacks.register(cb);

        }

        public void unregisterCallback(android.bordeaux.services.IBordeauxServiceCallback cb) {
            if (cb != null)
                mCallbacks.unregister(cb);

        }
    };

    @java.lang.Override
    public void onTaskRemoved(android.content.Intent rootIntent) {
        android.widget.Toast.makeText(this, "Task removed: " + rootIntent, android.widget.Toast.LENGTH_LONG).show();
    }

    /**
     * Show a notification while this service is running.
     * TODO: remove the code after production (when service is loaded
     * automatically by the system).
     */
    private void showNotification() {
        /* // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.remote_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent =
        PendingIntent.getActivity(this, 0,
        new Intent("android.bordeaux.DEBUG_CONTROLLER"), 0);

        // // Set the info for the views that show in the notification panel.

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_bordeaux);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(text);
        builder.setContentTitle(text);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.getNotification();
        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNotificationManager.notify(R.string.remote_service_started, notification);
         */
    }
}

