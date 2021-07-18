/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.os;


/**
 * The HardwarePropertiesManager class provides a mechanism of accessing hardware state of a
 * device: CPU, GPU and battery temperatures, CPU usage per core, fan speed, etc.
 */
public class HardwarePropertiesManager {
    private static final java.lang.String TAG = android.os.HardwarePropertiesManager.class.getSimpleName();

    private final android.os.IHardwarePropertiesManager mService;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU, android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_GPU, android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_BATTERY, android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_SKIN })
    public @interface DeviceTemperatureType {}

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.os.HardwarePropertiesManager.TEMPERATURE_CURRENT, android.os.HardwarePropertiesManager.TEMPERATURE_THROTTLING, android.os.HardwarePropertiesManager.TEMPERATURE_SHUTDOWN, android.os.HardwarePropertiesManager.TEMPERATURE_THROTTLING_BELOW_VR_MIN })
    public @interface TemperatureSource {}

    /**
     * Device temperature types. These must match the values in
     * frameworks/native/include/hardwareproperties/HardwarePropertiesManager.h
     */
    /**
     * Temperature of CPUs in Celsius.
     */
    public static final int DEVICE_TEMPERATURE_CPU = 0;

    /**
     * Temperature of GPUs in Celsius.
     */
    public static final int DEVICE_TEMPERATURE_GPU = 1;

    /**
     * Temperature of battery in Celsius.
     */
    public static final int DEVICE_TEMPERATURE_BATTERY = 2;

    /**
     * Temperature of device skin in Celsius.
     */
    public static final int DEVICE_TEMPERATURE_SKIN = 3;

    /**
     * Get current temperature.
     */
    public static final int TEMPERATURE_CURRENT = 0;

    /**
     * Get throttling temperature threshold.
     */
    public static final int TEMPERATURE_THROTTLING = 1;

    /**
     * Get shutdown temperature threshold.
     */
    public static final int TEMPERATURE_SHUTDOWN = 2;

    /**
     * Get throttling temperature threshold above which minimum clockrates for VR mode will not be
     * met.
     */
    public static final int TEMPERATURE_THROTTLING_BELOW_VR_MIN = 3;

    /**
     * Undefined temperature constant.
     */
    public static final float UNDEFINED_TEMPERATURE = -java.lang.Float.MAX_VALUE;

    /**
     * Calling app context.
     */
    private final android.content.Context mContext;

    /**
     *
     *
     * @unknown 
     */
    public HardwarePropertiesManager(android.content.Context context, android.os.IHardwarePropertiesManager service) {
        mContext = context;
        mService = service;
    }

    /**
     * Return an array of device temperatures in Celsius.
     *
     * @param type
     * 		type of requested device temperature, one of {@link #DEVICE_TEMPERATURE_CPU},
     * 		{@link #DEVICE_TEMPERATURE_GPU}, {@link #DEVICE_TEMPERATURE_BATTERY} or {@link #DEVICE_TEMPERATURE_SKIN}.
     * @param source
     * 		source of requested device temperature, one of {@link #TEMPERATURE_CURRENT},
     * 		{@link #TEMPERATURE_THROTTLING}, {@link #TEMPERATURE_THROTTLING_BELOW_VR_MIN} or
     * 		{@link #TEMPERATURE_SHUTDOWN}.
     * @return an array of requested float device temperatures. Temperature equals to
    {@link #UNDEFINED_TEMPERATURE} if undefined.
    Empty if platform doesn't provide the queried temperature.
     * @throws SecurityException
     * 		if something other than the profile or device owner, or the
     * 		current VR service tries to retrieve information provided by this service.
     */
    @android.annotation.NonNull
    public float[] getDeviceTemperatures(@android.os.HardwarePropertiesManager.DeviceTemperatureType
    int type, @android.os.HardwarePropertiesManager.TemperatureSource
    int source) {
        switch (type) {
            case android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU :
            case android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_GPU :
            case android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_BATTERY :
            case android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_SKIN :
                switch (source) {
                    case android.os.HardwarePropertiesManager.TEMPERATURE_CURRENT :
                    case android.os.HardwarePropertiesManager.TEMPERATURE_THROTTLING :
                    case android.os.HardwarePropertiesManager.TEMPERATURE_SHUTDOWN :
                    case android.os.HardwarePropertiesManager.TEMPERATURE_THROTTLING_BELOW_VR_MIN :
                        try {
                            return mService.getDeviceTemperatures(mContext.getOpPackageName(), type, source);
                        } catch (android.os.RemoteException e) {
                            throw e.rethrowFromSystemServer();
                        }
                    default :
                        android.util.Log.w(android.os.HardwarePropertiesManager.TAG, "Unknown device temperature source.");
                        return new float[0];
                }
            default :
                android.util.Log.w(android.os.HardwarePropertiesManager.TAG, "Unknown device temperature type.");
                return new float[0];
        }
    }

    /**
     * Return an array of CPU usage info for each core.
     *
     * @return an array of {@link android.os.CpuUsageInfo} for each core. Return {@code null} for
    each unplugged core.
    Empty if CPU usage is not supported on this system.
     * @throws SecurityException
     * 		if something other than the profile or device owner, or the
     * 		current VR service tries to retrieve information provided by this service.
     */
    @android.annotation.NonNull
    public android.os.CpuUsageInfo[] getCpuUsages() {
        try {
            return mService.getCpuUsages(mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return an array of fan speeds in RPM.
     *
     * @return an array of float fan speeds in RPM. Empty if there are no fans or fan speed is not
    supported on this system.
     * @throws SecurityException
     * 		if something other than the profile or device owner, or the
     * 		current VR service tries to retrieve information provided by this service.
     */
    @android.annotation.NonNull
    public float[] getFanSpeeds() {
        try {
            return mService.getFanSpeeds(mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}

