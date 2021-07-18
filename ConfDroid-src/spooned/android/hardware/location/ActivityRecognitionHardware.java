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
 * limitations under the License
 */
package android.hardware.location;


/**
 * A class that implements an {@link IActivityRecognitionHardware} backed up by the Activity
 * Recognition HAL.
 *
 * @unknown 
 */
public class ActivityRecognitionHardware extends android.hardware.location.IActivityRecognitionHardware.Stub {
    private static final java.lang.String TAG = "ActivityRecognitionHW";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.hardware.location.ActivityRecognitionHardware.TAG, android.util.Log.DEBUG);

    private static final java.lang.String HARDWARE_PERMISSION = Manifest.permission.LOCATION_HARDWARE;

    private static final java.lang.String ENFORCE_HW_PERMISSION_MESSAGE = ("Permission '" + android.hardware.location.ActivityRecognitionHardware.HARDWARE_PERMISSION) + "' not granted to access ActivityRecognitionHardware";

    private static final int INVALID_ACTIVITY_TYPE = -1;

    private static final int NATIVE_SUCCESS_RESULT = 0;

    private static final int EVENT_TYPE_DISABLED = 0;

    private static final int EVENT_TYPE_ENABLED = 1;

    /**
     * Contains the number of supported Event Types.
     *
     * NOTE: increment this counter every time a new EVENT_TYPE_ is added to
     *       com.android.location.provider.ActivityRecognitionProvider
     */
    private static final int EVENT_TYPE_COUNT = 3;

    private static android.hardware.location.ActivityRecognitionHardware sSingletonInstance;

    private static final java.lang.Object sSingletonInstanceLock = new java.lang.Object();

    private final android.content.Context mContext;

    private final int mSupportedActivitiesCount;

    private final java.lang.String[] mSupportedActivities;

    private final int[][] mSupportedActivitiesEnabledEvents;

    private final android.hardware.location.ActivityRecognitionHardware.SinkList mSinks = new android.hardware.location.ActivityRecognitionHardware.SinkList();

    private static class Event {
        public int activity;

        public int type;

        public long timestamp;
    }

    private ActivityRecognitionHardware(android.content.Context context) {
        nativeInitialize();
        mContext = context;
        mSupportedActivities = fetchSupportedActivities();
        mSupportedActivitiesCount = mSupportedActivities.length;
        mSupportedActivitiesEnabledEvents = new int[mSupportedActivitiesCount][android.hardware.location.ActivityRecognitionHardware.EVENT_TYPE_COUNT];
    }

    public static android.hardware.location.ActivityRecognitionHardware getInstance(android.content.Context context) {
        synchronized(android.hardware.location.ActivityRecognitionHardware.sSingletonInstanceLock) {
            if (android.hardware.location.ActivityRecognitionHardware.sSingletonInstance == null) {
                android.hardware.location.ActivityRecognitionHardware.sSingletonInstance = new android.hardware.location.ActivityRecognitionHardware(context);
            }
            return android.hardware.location.ActivityRecognitionHardware.sSingletonInstance;
        }
    }

    public static boolean isSupported() {
        return android.hardware.location.ActivityRecognitionHardware.nativeIsSupported();
    }

    @java.lang.Override
    public java.lang.String[] getSupportedActivities() {
        checkPermissions();
        return mSupportedActivities;
    }

    @java.lang.Override
    public boolean isActivitySupported(java.lang.String activity) {
        checkPermissions();
        int activityType = getActivityType(activity);
        return activityType != android.hardware.location.ActivityRecognitionHardware.INVALID_ACTIVITY_TYPE;
    }

    @java.lang.Override
    public boolean registerSink(android.hardware.location.IActivityRecognitionHardwareSink sink) {
        checkPermissions();
        return mSinks.register(sink);
    }

    @java.lang.Override
    public boolean unregisterSink(android.hardware.location.IActivityRecognitionHardwareSink sink) {
        checkPermissions();
        return mSinks.unregister(sink);
    }

    @java.lang.Override
    public boolean enableActivityEvent(java.lang.String activity, int eventType, long reportLatencyNs) {
        checkPermissions();
        int activityType = getActivityType(activity);
        if (activityType == android.hardware.location.ActivityRecognitionHardware.INVALID_ACTIVITY_TYPE) {
            return false;
        }
        int result = nativeEnableActivityEvent(activityType, eventType, reportLatencyNs);
        if (result == android.hardware.location.ActivityRecognitionHardware.NATIVE_SUCCESS_RESULT) {
            mSupportedActivitiesEnabledEvents[activityType][eventType] = android.hardware.location.ActivityRecognitionHardware.EVENT_TYPE_ENABLED;
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean disableActivityEvent(java.lang.String activity, int eventType) {
        checkPermissions();
        int activityType = getActivityType(activity);
        if (activityType == android.hardware.location.ActivityRecognitionHardware.INVALID_ACTIVITY_TYPE) {
            return false;
        }
        int result = nativeDisableActivityEvent(activityType, eventType);
        if (result == android.hardware.location.ActivityRecognitionHardware.NATIVE_SUCCESS_RESULT) {
            mSupportedActivitiesEnabledEvents[activityType][eventType] = android.hardware.location.ActivityRecognitionHardware.EVENT_TYPE_DISABLED;
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean flush() {
        checkPermissions();
        int result = nativeFlush();
        return result == android.hardware.location.ActivityRecognitionHardware.NATIVE_SUCCESS_RESULT;
    }

    /**
     * Called by the Activity-Recognition HAL.
     */
    private void onActivityChanged(android.hardware.location.ActivityRecognitionHardware.Event[] events) {
        if ((events == null) || (events.length == 0)) {
            if (android.hardware.location.ActivityRecognitionHardware.DEBUG)
                android.util.Log.d(android.hardware.location.ActivityRecognitionHardware.TAG, "No events to broadcast for onActivityChanged.");

            return;
        }
        int eventsLength = events.length;
        android.hardware.location.ActivityRecognitionEvent[] activityRecognitionEventArray = new android.hardware.location.ActivityRecognitionEvent[eventsLength];
        for (int i = 0; i < eventsLength; ++i) {
            android.hardware.location.ActivityRecognitionHardware.Event event = events[i];
            java.lang.String activityName = getActivityName(event.activity);
            activityRecognitionEventArray[i] = new android.hardware.location.ActivityRecognitionEvent(activityName, event.type, event.timestamp);
        }
        android.hardware.location.ActivityChangedEvent activityChangedEvent = new android.hardware.location.ActivityChangedEvent(activityRecognitionEventArray);
        int size = mSinks.beginBroadcast();
        for (int i = 0; i < size; ++i) {
            android.hardware.location.IActivityRecognitionHardwareSink sink = mSinks.getBroadcastItem(i);
            try {
                sink.onActivityChanged(activityChangedEvent);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.hardware.location.ActivityRecognitionHardware.TAG, "Error delivering activity changed event.", e);
            }
        }
        mSinks.finishBroadcast();
    }

    private java.lang.String getActivityName(int activityType) {
        if ((activityType < 0) || (activityType >= mSupportedActivities.length)) {
            java.lang.String message = java.lang.String.format("Invalid ActivityType: %d, SupportedActivities: %d", activityType, mSupportedActivities.length);
            android.util.Log.e(android.hardware.location.ActivityRecognitionHardware.TAG, message);
            return null;
        }
        return mSupportedActivities[activityType];
    }

    private int getActivityType(java.lang.String activity) {
        if (android.text.TextUtils.isEmpty(activity)) {
            return android.hardware.location.ActivityRecognitionHardware.INVALID_ACTIVITY_TYPE;
        }
        int supportedActivitiesLength = mSupportedActivities.length;
        for (int i = 0; i < supportedActivitiesLength; ++i) {
            if (activity.equals(mSupportedActivities[i])) {
                return i;
            }
        }
        return android.hardware.location.ActivityRecognitionHardware.INVALID_ACTIVITY_TYPE;
    }

    private void checkPermissions() {
        mContext.enforceCallingPermission(android.hardware.location.ActivityRecognitionHardware.HARDWARE_PERMISSION, android.hardware.location.ActivityRecognitionHardware.ENFORCE_HW_PERMISSION_MESSAGE);
    }

    private java.lang.String[] fetchSupportedActivities() {
        java.lang.String[] supportedActivities = nativeGetSupportedActivities();
        if (supportedActivities != null) {
            return supportedActivities;
        }
        return new java.lang.String[0];
    }

    private class SinkList extends android.os.RemoteCallbackList<android.hardware.location.IActivityRecognitionHardwareSink> {
        @java.lang.Override
        public void onCallbackDied(android.hardware.location.IActivityRecognitionHardwareSink callback) {
            int callbackCount = mSinks.getRegisteredCallbackCount();
            if (android.hardware.location.ActivityRecognitionHardware.DEBUG)
                android.util.Log.d(android.hardware.location.ActivityRecognitionHardware.TAG, "RegisteredCallbackCount: " + callbackCount);

            if (callbackCount != 0) {
                return;
            }
            // currently there is only one client for this, so if all its sinks have died, we clean
            // up after them, this ensures that the AR HAL is not out of sink
            for (int activity = 0; activity < mSupportedActivitiesCount; ++activity) {
                for (int event = 0; event < android.hardware.location.ActivityRecognitionHardware.EVENT_TYPE_COUNT; ++event) {
                    disableActivityEventIfEnabled(activity, event);
                }
            }
        }

        private void disableActivityEventIfEnabled(int activityType, int eventType) {
            if (mSupportedActivitiesEnabledEvents[activityType][eventType] != android.hardware.location.ActivityRecognitionHardware.EVENT_TYPE_ENABLED) {
                return;
            }
            int result = nativeDisableActivityEvent(activityType, eventType);
            mSupportedActivitiesEnabledEvents[activityType][eventType] = android.hardware.location.ActivityRecognitionHardware.EVENT_TYPE_DISABLED;
            java.lang.String message = java.lang.String.format("DisableActivityEvent: activityType=%d, eventType=%d, result=%d", activityType, eventType, result);
            android.util.Log.e(android.hardware.location.ActivityRecognitionHardware.TAG, message);
        }
    }

    // native bindings
    static {
        android.hardware.location.ActivityRecognitionHardware.nativeClassInit();
    }

    private static native void nativeClassInit();

    private static native boolean nativeIsSupported();

    private native void nativeInitialize();

    private native void nativeRelease();

    private native java.lang.String[] nativeGetSupportedActivities();

    private native int nativeEnableActivityEvent(int activityType, int eventType, long reportLatenceNs);

    private native int nativeDisableActivityEvent(int activityType, int eventType);

    private native int nativeFlush();
}

