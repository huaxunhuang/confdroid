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
 * Service that handles hardware geofencing.
 *
 * @unknown 
 */
public class GeofenceHardwareService extends android.app.Service {
    private android.hardware.location.GeofenceHardwareImpl mGeofenceHardwareImpl;

    private android.content.Context mContext;

    @java.lang.Override
    public void onCreate() {
        mContext = this;
        mGeofenceHardwareImpl = android.hardware.location.GeofenceHardwareImpl.getInstance(mContext);
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return mBinder;
    }

    @java.lang.Override
    public boolean onUnbind(android.content.Intent intent) {
        return false;
    }

    @java.lang.Override
    public void onDestroy() {
        mGeofenceHardwareImpl = null;
    }

    private void checkPermission(int pid, int uid, int monitoringType) {
        if (mGeofenceHardwareImpl.getAllowedResolutionLevel(pid, uid) < mGeofenceHardwareImpl.getMonitoringResolutionLevel(monitoringType)) {
            throw new java.lang.SecurityException(("Insufficient permissions to access hardware geofence for" + " type: ") + monitoringType);
        }
    }

    private android.os.IBinder mBinder = new android.hardware.location.IGeofenceHardware.Stub() {
        @java.lang.Override
        public void setGpsGeofenceHardware(android.location.IGpsGeofenceHardware service) {
            mGeofenceHardwareImpl.setGpsHardwareGeofence(service);
        }

        @java.lang.Override
        public void setFusedGeofenceHardware(android.location.IFusedGeofenceHardware service) {
            mGeofenceHardwareImpl.setFusedGeofenceHardware(service);
        }

        @java.lang.Override
        public int[] getMonitoringTypes() {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            return mGeofenceHardwareImpl.getMonitoringTypes();
        }

        @java.lang.Override
        public int getStatusOfMonitoringType(int monitoringType) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            return mGeofenceHardwareImpl.getStatusOfMonitoringType(monitoringType);
        }

        @java.lang.Override
        public boolean addCircularFence(int monitoringType, android.hardware.location.GeofenceHardwareRequestParcelable request, android.hardware.location.IGeofenceHardwareCallback callback) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            checkPermission(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), monitoringType);
            return mGeofenceHardwareImpl.addCircularFence(monitoringType, request, callback);
        }

        @java.lang.Override
        public boolean removeGeofence(int id, int monitoringType) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            checkPermission(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), monitoringType);
            return mGeofenceHardwareImpl.removeGeofence(id, monitoringType);
        }

        @java.lang.Override
        public boolean pauseGeofence(int id, int monitoringType) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            checkPermission(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), monitoringType);
            return mGeofenceHardwareImpl.pauseGeofence(id, monitoringType);
        }

        @java.lang.Override
        public boolean resumeGeofence(int id, int monitoringType, int monitorTransitions) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            checkPermission(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), monitoringType);
            return mGeofenceHardwareImpl.resumeGeofence(id, monitoringType, monitorTransitions);
        }

        @java.lang.Override
        public boolean registerForMonitorStateChangeCallback(int monitoringType, android.hardware.location.IGeofenceHardwareMonitorCallback callback) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            checkPermission(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), monitoringType);
            return mGeofenceHardwareImpl.registerForMonitorStateChangeCallback(monitoringType, callback);
        }

        @java.lang.Override
        public boolean unregisterForMonitorStateChangeCallback(int monitoringType, android.hardware.location.IGeofenceHardwareMonitorCallback callback) {
            mContext.enforceCallingPermission(Manifest.permission.LOCATION_HARDWARE, "Location Hardware permission not granted to access hardware geofence");
            checkPermission(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), monitoringType);
            return mGeofenceHardwareImpl.unregisterForMonitorStateChangeCallback(monitoringType, callback);
        }
    };
}

