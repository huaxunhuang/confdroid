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
package android.hardware.location;


/**
 * This class manages the geofences which are handled by hardware.
 *
 * @unknown 
 */
public final class GeofenceHardwareImpl {
    private static final java.lang.String TAG = "GeofenceHardwareImpl";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.hardware.location.GeofenceHardwareImpl.TAG, android.util.Log.DEBUG);

    private static final int FIRST_VERSION_WITH_CAPABILITIES = 2;

    private final android.content.Context mContext;

    private static android.hardware.location.GeofenceHardwareImpl sInstance;

    private android.os.PowerManager.WakeLock mWakeLock;

    private final android.util.SparseArray<android.hardware.location.IGeofenceHardwareCallback> mGeofences = new android.util.SparseArray<android.hardware.location.IGeofenceHardwareCallback>();

    private final java.util.ArrayList<android.hardware.location.IGeofenceHardwareMonitorCallback>[] mCallbacks = new java.util.ArrayList[android.hardware.location.GeofenceHardware.NUM_MONITORS];

    private final java.util.ArrayList<android.hardware.location.GeofenceHardwareImpl.Reaper> mReapers = new java.util.ArrayList<android.hardware.location.GeofenceHardwareImpl.Reaper>();

    private android.location.IFusedGeofenceHardware mFusedService;

    private android.location.IGpsGeofenceHardware mGpsService;

    private int mCapabilities;

    private int mVersion = 1;

    private int[] mSupportedMonitorTypes = new int[android.hardware.location.GeofenceHardware.NUM_MONITORS];

    // mGeofenceHandler message types
    private static final int GEOFENCE_TRANSITION_CALLBACK = 1;

    private static final int ADD_GEOFENCE_CALLBACK = 2;

    private static final int REMOVE_GEOFENCE_CALLBACK = 3;

    private static final int PAUSE_GEOFENCE_CALLBACK = 4;

    private static final int RESUME_GEOFENCE_CALLBACK = 5;

    private static final int GEOFENCE_CALLBACK_BINDER_DIED = 6;

    // mCallbacksHandler message types
    private static final int GEOFENCE_STATUS = 1;

    private static final int CALLBACK_ADD = 2;

    private static final int CALLBACK_REMOVE = 3;

    private static final int MONITOR_CALLBACK_BINDER_DIED = 4;

    // mReaperHandler message types
    private static final int REAPER_GEOFENCE_ADDED = 1;

    private static final int REAPER_MONITOR_CALLBACK_ADDED = 2;

    private static final int REAPER_REMOVED = 3;

    // The following constants need to match GpsLocationFlags enum in gps.h
    private static final int LOCATION_INVALID = 0;

    private static final int LOCATION_HAS_LAT_LONG = 1;

    private static final int LOCATION_HAS_ALTITUDE = 2;

    private static final int LOCATION_HAS_SPEED = 4;

    private static final int LOCATION_HAS_BEARING = 8;

    private static final int LOCATION_HAS_ACCURACY = 16;

    // Resolution level constants used for permission checks.
    // These constants must be in increasing order of finer resolution.
    private static final int RESOLUTION_LEVEL_NONE = 1;

    private static final int RESOLUTION_LEVEL_COARSE = 2;

    private static final int RESOLUTION_LEVEL_FINE = 3;

    // Capability constant corresponding to fused_location.h entry when geofencing supports GNNS.
    private static final int CAPABILITY_GNSS = 1;

    public static synchronized android.hardware.location.GeofenceHardwareImpl getInstance(android.content.Context context) {
        if (android.hardware.location.GeofenceHardwareImpl.sInstance == null) {
            android.hardware.location.GeofenceHardwareImpl.sInstance = new android.hardware.location.GeofenceHardwareImpl(context);
        }
        return android.hardware.location.GeofenceHardwareImpl.sInstance;
    }

    private GeofenceHardwareImpl(android.content.Context context) {
        mContext = context;
        // Init everything to unsupported.
        setMonitorAvailability(android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE, android.hardware.location.GeofenceHardware.MONITOR_UNSUPPORTED);
        setMonitorAvailability(android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE, android.hardware.location.GeofenceHardware.MONITOR_UNSUPPORTED);
    }

    private void acquireWakeLock() {
        if (mWakeLock == null) {
            android.os.PowerManager powerManager = ((android.os.PowerManager) (mContext.getSystemService(android.content.Context.POWER_SERVICE)));
            mWakeLock = powerManager.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, android.hardware.location.GeofenceHardwareImpl.TAG);
        }
        mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (mWakeLock.isHeld())
            mWakeLock.release();

    }

    private void updateGpsHardwareAvailability() {
        // Check which monitors are available.
        boolean gpsSupported;
        try {
            gpsSupported = mGpsService.isHardwareGeofenceSupported();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "Remote Exception calling LocationManagerService");
            gpsSupported = false;
        }
        if (gpsSupported) {
            // Its assumed currently available at startup.
            // native layer will update later.
            setMonitorAvailability(android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE, android.hardware.location.GeofenceHardware.MONITOR_CURRENTLY_AVAILABLE);
        }
    }

    private void updateFusedHardwareAvailability() {
        boolean fusedSupported;
        try {
            final boolean hasGnnsCapabilities = (mVersion < android.hardware.location.GeofenceHardwareImpl.FIRST_VERSION_WITH_CAPABILITIES) || ((mCapabilities & android.hardware.location.GeofenceHardwareImpl.CAPABILITY_GNSS) != 0);
            fusedSupported = (mFusedService != null) ? mFusedService.isSupported() && hasGnnsCapabilities : false;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "RemoteException calling LocationManagerService");
            fusedSupported = false;
        }
        if (fusedSupported) {
            setMonitorAvailability(android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE, android.hardware.location.GeofenceHardware.MONITOR_CURRENTLY_AVAILABLE);
        }
    }

    public void setGpsHardwareGeofence(android.location.IGpsGeofenceHardware service) {
        if (mGpsService == null) {
            mGpsService = service;
            updateGpsHardwareAvailability();
        } else
            if (service == null) {
                mGpsService = null;
                android.util.Log.w(android.hardware.location.GeofenceHardwareImpl.TAG, "GPS Geofence Hardware service seems to have crashed");
            } else {
                android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "Error: GpsService being set again.");
            }

    }

    public void onCapabilities(int capabilities) {
        mCapabilities = capabilities;
        updateFusedHardwareAvailability();
    }

    public void setVersion(int version) {
        mVersion = version;
        updateFusedHardwareAvailability();
    }

    public void setFusedGeofenceHardware(android.location.IFusedGeofenceHardware service) {
        if (mFusedService == null) {
            mFusedService = service;
            updateFusedHardwareAvailability();
        } else
            if (service == null) {
                mFusedService = null;
                android.util.Log.w(android.hardware.location.GeofenceHardwareImpl.TAG, "Fused Geofence Hardware service seems to have crashed");
            } else {
                android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "Error: FusedService being set again");
            }

    }

    public int[] getMonitoringTypes() {
        boolean gpsSupported;
        boolean fusedSupported;
        synchronized(mSupportedMonitorTypes) {
            gpsSupported = mSupportedMonitorTypes[android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE] != android.hardware.location.GeofenceHardware.MONITOR_UNSUPPORTED;
            fusedSupported = mSupportedMonitorTypes[android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE] != android.hardware.location.GeofenceHardware.MONITOR_UNSUPPORTED;
        }
        if (gpsSupported) {
            if (fusedSupported) {
                return new int[]{ android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE, android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE };
            } else {
                return new int[]{ android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE };
            }
        } else
            if (fusedSupported) {
                return new int[]{ android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE };
            } else {
                return new int[0];
            }

    }

    public int getStatusOfMonitoringType(int monitoringType) {
        synchronized(mSupportedMonitorTypes) {
            if ((monitoringType >= mSupportedMonitorTypes.length) || (monitoringType < 0)) {
                throw new java.lang.IllegalArgumentException("Unknown monitoring type");
            }
            return mSupportedMonitorTypes[monitoringType];
        }
    }

    public int getCapabilitiesForMonitoringType(int monitoringType) {
        switch (mSupportedMonitorTypes[monitoringType]) {
            case android.hardware.location.GeofenceHardware.MONITOR_CURRENTLY_AVAILABLE :
                switch (monitoringType) {
                    case android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE :
                        return android.hardware.location.GeofenceHardwareImpl.CAPABILITY_GNSS;
                    case android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE :
                        if (mVersion >= android.hardware.location.GeofenceHardwareImpl.FIRST_VERSION_WITH_CAPABILITIES) {
                            return mCapabilities;
                        }
                        // This was the implied capability on old FLP HAL versions that didn't
                        // have the capability callback.
                        return android.hardware.location.GeofenceHardwareImpl.CAPABILITY_GNSS;
                }
                break;
        }
        return 0;
    }

    public boolean addCircularFence(int monitoringType, android.hardware.location.GeofenceHardwareRequestParcelable request, android.hardware.location.IGeofenceHardwareCallback callback) {
        int geofenceId = request.getId();
        // This API is not thread safe. Operations on the same geofence need to be serialized
        // by upper layers
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG) {
            java.lang.String message = java.lang.String.format("addCircularFence: monitoringType=%d, %s", monitoringType, request);
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, message);
        }
        boolean result;
        // The callback must be added before addCircularHardwareGeofence is called otherwise the
        // callback might not be called after the geofence is added in the geofence hardware.
        // This also means that the callback must be removed if the addCircularHardwareGeofence
        // operations is not called or fails.
        synchronized(mGeofences) {
            mGeofences.put(geofenceId, callback);
        }
        switch (monitoringType) {
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE :
                if (mGpsService == null)
                    return false;

                try {
                    result = mGpsService.addCircularHardwareGeofence(request.getId(), request.getLatitude(), request.getLongitude(), request.getRadius(), request.getLastTransition(), request.getMonitorTransitions(), request.getNotificationResponsiveness(), request.getUnknownTimer());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "AddGeofence: Remote Exception calling LocationManagerService");
                    result = false;
                }
                break;
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE :
                if (mFusedService == null) {
                    return false;
                }
                try {
                    mFusedService.addGeofences(new android.hardware.location.GeofenceHardwareRequestParcelable[]{ request });
                    result = true;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "AddGeofence: RemoteException calling LocationManagerService");
                    result = false;
                }
                break;
            default :
                result = false;
        }
        if (result) {
            android.os.Message m = mReaperHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.REAPER_GEOFENCE_ADDED, callback);
            m.arg1 = monitoringType;
            mReaperHandler.sendMessage(m);
        } else {
            synchronized(mGeofences) {
                mGeofences.remove(geofenceId);
            }
        }
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "addCircularFence: Result is: " + result);

        return result;
    }

    public boolean removeGeofence(int geofenceId, int monitoringType) {
        // This API is not thread safe. Operations on the same geofence need to be serialized
        // by upper layers
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "Remove Geofence: GeofenceId: " + geofenceId);

        boolean result = false;
        synchronized(mGeofences) {
            if (mGeofences.get(geofenceId) == null) {
                throw new java.lang.IllegalArgumentException(("Geofence " + geofenceId) + " not registered.");
            }
        }
        switch (monitoringType) {
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE :
                if (mGpsService == null)
                    return false;

                try {
                    result = mGpsService.removeHardwareGeofence(geofenceId);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "RemoveGeofence: Remote Exception calling LocationManagerService");
                    result = false;
                }
                break;
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE :
                if (mFusedService == null) {
                    return false;
                }
                try {
                    mFusedService.removeGeofences(new int[]{ geofenceId });
                    result = true;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "RemoveGeofence: RemoteException calling LocationManagerService");
                    result = false;
                }
                break;
            default :
                result = false;
        }
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "removeGeofence: Result is: " + result);

        return result;
    }

    public boolean pauseGeofence(int geofenceId, int monitoringType) {
        // This API is not thread safe. Operations on the same geofence need to be serialized
        // by upper layers
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "Pause Geofence: GeofenceId: " + geofenceId);

        boolean result;
        synchronized(mGeofences) {
            if (mGeofences.get(geofenceId) == null) {
                throw new java.lang.IllegalArgumentException(("Geofence " + geofenceId) + " not registered.");
            }
        }
        switch (monitoringType) {
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE :
                if (mGpsService == null)
                    return false;

                try {
                    result = mGpsService.pauseHardwareGeofence(geofenceId);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "PauseGeofence: Remote Exception calling LocationManagerService");
                    result = false;
                }
                break;
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE :
                if (mFusedService == null) {
                    return false;
                }
                try {
                    mFusedService.pauseMonitoringGeofence(geofenceId);
                    result = true;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "PauseGeofence: RemoteException calling LocationManagerService");
                    result = false;
                }
                break;
            default :
                result = false;
        }
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "pauseGeofence: Result is: " + result);

        return result;
    }

    public boolean resumeGeofence(int geofenceId, int monitoringType, int monitorTransition) {
        // This API is not thread safe. Operations on the same geofence need to be serialized
        // by upper layers
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "Resume Geofence: GeofenceId: " + geofenceId);

        boolean result;
        synchronized(mGeofences) {
            if (mGeofences.get(geofenceId) == null) {
                throw new java.lang.IllegalArgumentException(("Geofence " + geofenceId) + " not registered.");
            }
        }
        switch (monitoringType) {
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE :
                if (mGpsService == null)
                    return false;

                try {
                    result = mGpsService.resumeHardwareGeofence(geofenceId, monitorTransition);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "ResumeGeofence: Remote Exception calling LocationManagerService");
                    result = false;
                }
                break;
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE :
                if (mFusedService == null) {
                    return false;
                }
                try {
                    mFusedService.resumeMonitoringGeofence(geofenceId, monitorTransition);
                    result = true;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, "ResumeGeofence: RemoteException calling LocationManagerService");
                    result = false;
                }
                break;
            default :
                result = false;
        }
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "resumeGeofence: Result is: " + result);

        return result;
    }

    public boolean registerForMonitorStateChangeCallback(int monitoringType, android.hardware.location.IGeofenceHardwareMonitorCallback callback) {
        android.os.Message reaperMessage = mReaperHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.REAPER_MONITOR_CALLBACK_ADDED, callback);
        reaperMessage.arg1 = monitoringType;
        mReaperHandler.sendMessage(reaperMessage);
        android.os.Message m = mCallbacksHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.CALLBACK_ADD, callback);
        m.arg1 = monitoringType;
        mCallbacksHandler.sendMessage(m);
        return true;
    }

    public boolean unregisterForMonitorStateChangeCallback(int monitoringType, android.hardware.location.IGeofenceHardwareMonitorCallback callback) {
        android.os.Message m = mCallbacksHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.CALLBACK_REMOVE, callback);
        m.arg1 = monitoringType;
        mCallbacksHandler.sendMessage(m);
        return true;
    }

    /**
     * Used to report geofence transitions
     */
    public void reportGeofenceTransition(int geofenceId, android.location.Location location, int transition, long transitionTimestamp, int monitoringType, int sourcesUsed) {
        if (location == null) {
            android.util.Log.e(android.hardware.location.GeofenceHardwareImpl.TAG, java.lang.String.format("Invalid Geofence Transition: location=null"));
            return;
        }
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG) {
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, (((((((("GeofenceTransition| " + location) + ", transition:") + transition) + ", transitionTimestamp:") + transitionTimestamp) + ", monitoringType:") + monitoringType) + ", sourcesUsed:") + sourcesUsed);
        }
        android.hardware.location.GeofenceHardwareImpl.GeofenceTransition geofenceTransition = new android.hardware.location.GeofenceHardwareImpl.GeofenceTransition(geofenceId, transition, transitionTimestamp, location, monitoringType, sourcesUsed);
        acquireWakeLock();
        android.os.Message message = mGeofenceHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.GEOFENCE_TRANSITION_CALLBACK, geofenceTransition);
        message.sendToTarget();
    }

    /**
     * Used to report Monitor status changes.
     */
    public void reportGeofenceMonitorStatus(int monitoringType, int monitoringStatus, android.location.Location location, int source) {
        setMonitorAvailability(monitoringType, monitoringStatus);
        acquireWakeLock();
        android.hardware.location.GeofenceHardwareMonitorEvent event = new android.hardware.location.GeofenceHardwareMonitorEvent(monitoringType, monitoringStatus, source, location);
        android.os.Message message = mCallbacksHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.GEOFENCE_STATUS, event);
        message.sendToTarget();
    }

    /**
     * Internal generic status report function for Geofence operations.
     *
     * @param operation
     * 		The operation to be reported as defined internally.
     * @param geofenceId
     * 		The id of the geofence the operation is related to.
     * @param operationStatus
     * 		The status of the operation as defined in GeofenceHardware class. This
     * 		status is independent of the statuses reported by different HALs.
     */
    private void reportGeofenceOperationStatus(int operation, int geofenceId, int operationStatus) {
        acquireWakeLock();
        android.os.Message message = mGeofenceHandler.obtainMessage(operation);
        message.arg1 = geofenceId;
        message.arg2 = operationStatus;
        message.sendToTarget();
    }

    /**
     * Used to report the status of a Geofence Add operation.
     */
    public void reportGeofenceAddStatus(int geofenceId, int status) {
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, (("AddCallback| id:" + geofenceId) + ", status:") + status);

        reportGeofenceOperationStatus(android.hardware.location.GeofenceHardwareImpl.ADD_GEOFENCE_CALLBACK, geofenceId, status);
    }

    /**
     * Used to report the status of a Geofence Remove operation.
     */
    public void reportGeofenceRemoveStatus(int geofenceId, int status) {
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, (("RemoveCallback| id:" + geofenceId) + ", status:") + status);

        reportGeofenceOperationStatus(android.hardware.location.GeofenceHardwareImpl.REMOVE_GEOFENCE_CALLBACK, geofenceId, status);
    }

    /**
     * Used to report the status of a Geofence Pause operation.
     */
    public void reportGeofencePauseStatus(int geofenceId, int status) {
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, (("PauseCallbac| id:" + geofenceId) + ", status") + status);

        reportGeofenceOperationStatus(android.hardware.location.GeofenceHardwareImpl.PAUSE_GEOFENCE_CALLBACK, geofenceId, status);
    }

    /**
     * Used to report the status of a Geofence Resume operation.
     */
    public void reportGeofenceResumeStatus(int geofenceId, int status) {
        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, (("ResumeCallback| id:" + geofenceId) + ", status:") + status);

        reportGeofenceOperationStatus(android.hardware.location.GeofenceHardwareImpl.RESUME_GEOFENCE_CALLBACK, geofenceId, status);
    }

    // All operations on mGeofences
    private android.os.Handler mGeofenceHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            int geofenceId;
            int status;
            android.hardware.location.IGeofenceHardwareCallback callback;
            switch (msg.what) {
                case android.hardware.location.GeofenceHardwareImpl.ADD_GEOFENCE_CALLBACK :
                    geofenceId = msg.arg1;
                    synchronized(mGeofences) {
                        callback = mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceAdd(geofenceId, msg.arg2);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.i(android.hardware.location.GeofenceHardwareImpl.TAG, "Remote Exception:" + e);
                        }
                    }
                    releaseWakeLock();
                    break;
                case android.hardware.location.GeofenceHardwareImpl.REMOVE_GEOFENCE_CALLBACK :
                    geofenceId = msg.arg1;
                    synchronized(mGeofences) {
                        callback = mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceRemove(geofenceId, msg.arg2);
                        } catch (android.os.RemoteException e) {
                        }
                        android.os.IBinder callbackBinder = callback.asBinder();
                        boolean callbackInUse = false;
                        synchronized(mGeofences) {
                            mGeofences.remove(geofenceId);
                            // Check if the underlying binder is still useful for other geofences,
                            // if no, unlink the DeathRecipient to avoid memory leak.
                            for (int i = 0; i < mGeofences.size(); i++) {
                                if (mGeofences.valueAt(i).asBinder() == callbackBinder) {
                                    callbackInUse = true;
                                    break;
                                }
                            }
                        }
                        // Remove the reaper associated with this binder.
                        if (!callbackInUse) {
                            for (java.util.Iterator<android.hardware.location.GeofenceHardwareImpl.Reaper> iterator = mReapers.iterator(); iterator.hasNext();) {
                                android.hardware.location.GeofenceHardwareImpl.Reaper reaper = iterator.next();
                                if ((reaper.mCallback != null) && (reaper.mCallback.asBinder() == callbackBinder)) {
                                    iterator.remove();
                                    reaper.unlinkToDeath();
                                    if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
                                        android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, java.lang.String.format("Removed reaper %s " + "because binder %s is no longer needed.", reaper, callbackBinder));

                                }
                            }
                        }
                    }
                    releaseWakeLock();
                    break;
                case android.hardware.location.GeofenceHardwareImpl.PAUSE_GEOFENCE_CALLBACK :
                    geofenceId = msg.arg1;
                    synchronized(mGeofences) {
                        callback = mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofencePause(geofenceId, msg.arg2);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    releaseWakeLock();
                    break;
                case android.hardware.location.GeofenceHardwareImpl.RESUME_GEOFENCE_CALLBACK :
                    geofenceId = msg.arg1;
                    synchronized(mGeofences) {
                        callback = mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceResume(geofenceId, msg.arg2);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    releaseWakeLock();
                    break;
                case android.hardware.location.GeofenceHardwareImpl.GEOFENCE_TRANSITION_CALLBACK :
                    android.hardware.location.GeofenceHardwareImpl.GeofenceTransition geofenceTransition = ((android.hardware.location.GeofenceHardwareImpl.GeofenceTransition) (msg.obj));
                    synchronized(mGeofences) {
                        callback = mGeofences.get(geofenceTransition.mGeofenceId);
                        // need to keep access to mGeofences synchronized at all times
                        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
                            android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, (((((("GeofenceTransistionCallback: GPS : GeofenceId: " + geofenceTransition.mGeofenceId) + " Transition: ") + geofenceTransition.mTransition) + " Location: ") + geofenceTransition.mLocation) + ":") + mGeofences);

                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceTransition(geofenceTransition.mGeofenceId, geofenceTransition.mTransition, geofenceTransition.mLocation, geofenceTransition.mTimestamp, geofenceTransition.mMonitoringType);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    releaseWakeLock();
                    break;
                case android.hardware.location.GeofenceHardwareImpl.GEOFENCE_CALLBACK_BINDER_DIED :
                    // Find all geofences associated with this callback and remove them.
                    callback = ((android.hardware.location.IGeofenceHardwareCallback) (msg.obj));
                    if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
                        android.util.Log.d(android.hardware.location.GeofenceHardwareImpl.TAG, "Geofence callback reaped:" + callback);

                    int monitoringType = msg.arg1;
                    synchronized(mGeofences) {
                        for (int i = 0; i < mGeofences.size(); i++) {
                            if (mGeofences.valueAt(i).equals(callback)) {
                                geofenceId = mGeofences.keyAt(i);
                                removeGeofence(mGeofences.keyAt(i), monitoringType);
                                mGeofences.remove(geofenceId);
                            }
                        }
                    }
            }
        }
    };

    // All operations on mCallbacks
    private android.os.Handler mCallbacksHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            int monitoringType;
            java.util.ArrayList<android.hardware.location.IGeofenceHardwareMonitorCallback> callbackList;
            android.hardware.location.IGeofenceHardwareMonitorCallback callback;
            switch (msg.what) {
                case android.hardware.location.GeofenceHardwareImpl.GEOFENCE_STATUS :
                    android.hardware.location.GeofenceHardwareMonitorEvent event = ((android.hardware.location.GeofenceHardwareMonitorEvent) (msg.obj));
                    callbackList = mCallbacks[event.getMonitoringType()];
                    if (callbackList != null) {
                        if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
                            android.util.Log.d(android.os.Handler.TAG, "MonitoringSystemChangeCallback: " + event);

                        for (android.hardware.location.IGeofenceHardwareMonitorCallback c : callbackList) {
                            try {
                                c.onMonitoringSystemChange(event);
                            } catch ( e) {
                                android.util.Log.d(android.os.Handler.TAG, "Error reporting onMonitoringSystemChange.", android.hardware.location.e);
                            }
                        }
                    }
                    releaseWakeLock();
                    break;
                case android.hardware.location.GeofenceHardwareImpl.CALLBACK_ADD :
                    monitoringType = msg.arg1;
                    callback = ((android.hardware.location.IGeofenceHardwareMonitorCallback) (msg.obj));
                    callbackList = mCallbacks[monitoringType];
                    if (callbackList == null) {
                        callbackList = new ArrayList<android.hardware.location.IGeofenceHardwareMonitorCallback>();
                        mCallbacks[monitoringType] = callbackList;
                    }
                    if (!callbackList.contains(callback))
                        callbackList.add(callback);

                    break;
                case android.hardware.location.GeofenceHardwareImpl.CALLBACK_REMOVE :
                    monitoringType = msg.arg1;
                    callback = ((android.hardware.location.IGeofenceHardwareMonitorCallback) (msg.obj));
                    callbackList = mCallbacks[monitoringType];
                    if (callbackList != null) {
                        callbackList.remove(callback);
                    }
                    break;
                case android.hardware.location.GeofenceHardwareImpl.MONITOR_CALLBACK_BINDER_DIED :
                    callback = ((android.hardware.location.IGeofenceHardwareMonitorCallback) (msg.obj));
                    if (android.hardware.location.GeofenceHardwareImpl.DEBUG)
                        android.util.Log.d(android.os.Handler.TAG, "Monitor callback reaped:" + callback);

                    callbackList = mCallbacks[msg.arg1];
                    if ((callbackList != null) && callbackList.contains(callback)) {
                        callbackList.remove(callback);
                    }
            }
        }
    };

    // All operations on mReaper
    private android.os.Handler mReaperHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.hardware.location.GeofenceHardwareImpl.Reaper r;
            android.hardware.location.IGeofenceHardwareCallback callback;
            android.hardware.location.IGeofenceHardwareMonitorCallback monitorCallback;
            int monitoringType;
            switch (msg.what) {
                case android.hardware.location.GeofenceHardwareImpl.REAPER_GEOFENCE_ADDED :
                    callback = ((android.hardware.location.IGeofenceHardwareCallback) (msg.obj));
                    monitoringType = msg.arg1;
                    r = new android.hardware.location.GeofenceHardwareImpl.Reaper(callback, monitoringType);
                    if (!mReapers.contains(r)) {
                        mReapers.add(r);
                        android.os.IBinder b = callback.asBinder();
                        try {
                            b.linkToDeath(r, 0);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    break;
                case android.hardware.location.GeofenceHardwareImpl.REAPER_MONITOR_CALLBACK_ADDED :
                    monitorCallback = ((android.hardware.location.IGeofenceHardwareMonitorCallback) (msg.obj));
                    monitoringType = msg.arg1;
                    r = new android.hardware.location.GeofenceHardwareImpl.Reaper(monitorCallback, monitoringType);
                    if (!mReapers.contains(r)) {
                        mReapers.add(r);
                        android.os.IBinder b = monitorCallback.asBinder();
                        try {
                            b.linkToDeath(r, 0);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    break;
                case android.hardware.location.GeofenceHardwareImpl.REAPER_REMOVED :
                    r = ((android.hardware.location.GeofenceHardwareImpl.Reaper) (msg.obj));
                    mReapers.remove(r);
            }
        }
    };

    private class GeofenceTransition {
        private int mGeofenceId;

        private int mTransition;

        private long mTimestamp;

        private android.location.Location mLocation;

        private int mMonitoringType;

        private int mSourcesUsed;

        GeofenceTransition(int geofenceId, int transition, long timestamp, android.location.Location location, int monitoringType, int sourcesUsed) {
            mGeofenceId = geofenceId;
            mTransition = transition;
            mTimestamp = timestamp;
            mLocation = location;
            mMonitoringType = monitoringType;
            mSourcesUsed = sourcesUsed;
        }
    }

    private void setMonitorAvailability(int monitor, int val) {
        synchronized(mSupportedMonitorTypes) {
            mSupportedMonitorTypes[monitor] = val;
        }
    }

    int getMonitoringResolutionLevel(int monitoringType) {
        switch (monitoringType) {
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_GPS_HARDWARE :
                return android.hardware.location.GeofenceHardwareImpl.RESOLUTION_LEVEL_FINE;
            case android.hardware.location.GeofenceHardware.MONITORING_TYPE_FUSED_HARDWARE :
                return android.hardware.location.GeofenceHardwareImpl.RESOLUTION_LEVEL_FINE;
        }
        return android.hardware.location.GeofenceHardwareImpl.RESOLUTION_LEVEL_NONE;
    }

    class Reaper implements android.os.IBinder.DeathRecipient {
        private android.hardware.location.IGeofenceHardwareMonitorCallback mMonitorCallback;

        private android.hardware.location.IGeofenceHardwareCallback mCallback;

        private int mMonitoringType;

        Reaper(android.hardware.location.IGeofenceHardwareCallback c, int monitoringType) {
            mCallback = c;
            mMonitoringType = monitoringType;
        }

        Reaper(android.hardware.location.IGeofenceHardwareMonitorCallback c, int monitoringType) {
            mMonitorCallback = c;
            mMonitoringType = monitoringType;
        }

        @java.lang.Override
        public void binderDied() {
            android.os.Message m;
            if (mCallback != null) {
                m = mGeofenceHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.GEOFENCE_CALLBACK_BINDER_DIED, mCallback);
                m.arg1 = mMonitoringType;
                mGeofenceHandler.sendMessage(m);
            } else
                if (mMonitorCallback != null) {
                    m = mCallbacksHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.MONITOR_CALLBACK_BINDER_DIED, mMonitorCallback);
                    m.arg1 = mMonitoringType;
                    mCallbacksHandler.sendMessage(m);
                }

            android.os.Message reaperMessage = mReaperHandler.obtainMessage(android.hardware.location.GeofenceHardwareImpl.REAPER_REMOVED, this);
            mReaperHandler.sendMessage(reaperMessage);
        }

        @java.lang.Override
        public int hashCode() {
            int result = 17;
            result = (31 * result) + (mCallback != null ? hashCode() : 0);
            result = (31 * result) + (mMonitorCallback != null ? hashCode() : 0);
            result = (31 * result) + mMonitoringType;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (obj == null)
                return false;

            if (obj == this)
                return true;

            android.hardware.location.GeofenceHardwareImpl.Reaper rhs = ((android.hardware.location.GeofenceHardwareImpl.Reaper) (obj));
            return (binderEquals(rhs.mCallback, mCallback) && binderEquals(rhs.mMonitorCallback, mMonitorCallback)) && (rhs.mMonitoringType == mMonitoringType);
        }

        /**
         * Compares the underlying Binder of the given two IInterface objects and returns true if
         * they equals. null values are accepted.
         */
        private boolean binderEquals(android.os.IInterface left, android.os.IInterface right) {
            if (left == null) {
                return right == null;
            } else {
                return right == null ? false : left.asBinder() == right.asBinder();
            }
        }

        /**
         * Unlinks this DeathRecipient.
         */
        private boolean unlinkToDeath() {
            if (mMonitorCallback != null) {
                return unlinkToDeath(this, 0);
            } else
                if (mCallback != null) {
                    return unlinkToDeath(this, 0);
                }

            return true;
        }

        private boolean callbackEquals(android.hardware.location.IGeofenceHardwareCallback cb) {
            return (mCallback != null) && (mCallback.asBinder() == cb.asBinder());
        }
    }

    int getAllowedResolutionLevel(int pid, int uid) {
        if (mContext.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, pid, uid) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return android.hardware.location.GeofenceHardwareImpl.RESOLUTION_LEVEL_FINE;
        } else
            if (mContext.checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, pid, uid) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return android.hardware.location.GeofenceHardwareImpl.RESOLUTION_LEVEL_COARSE;
            } else {
                return android.hardware.location.GeofenceHardwareImpl.RESOLUTION_LEVEL_NONE;
            }

    }
}

