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
package android.hardware;


/**
 * Helper class for implementing the legacy sensor manager API.
 *
 * @unknown 
 */
@java.lang.SuppressWarnings("deprecation")
final class LegacySensorManager {
    private static boolean sInitialized;

    private static android.view.IWindowManager sWindowManager;

    private static int sRotation = android.view.Surface.ROTATION_0;

    private final android.hardware.SensorManager mSensorManager;

    // List of legacy listeners.  Guarded by mLegacyListenersMap.
    private final java.util.HashMap<android.hardware.SensorListener, android.hardware.LegacySensorManager.LegacyListener> mLegacyListenersMap = new java.util.HashMap<android.hardware.SensorListener, android.hardware.LegacySensorManager.LegacyListener>();

    public LegacySensorManager(android.hardware.SensorManager sensorManager) {
        mSensorManager = sensorManager;
        synchronized(android.hardware.SensorManager.class) {
            if (!android.hardware.LegacySensorManager.sInitialized) {
                android.hardware.LegacySensorManager.sWindowManager = IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window"));
                if (android.hardware.LegacySensorManager.sWindowManager != null) {
                    // if it's null we're running in the system process
                    // which won't get the rotated values
                    try {
                        android.hardware.LegacySensorManager.sRotation = android.hardware.LegacySensorManager.sWindowManager.watchRotation(new android.view.IRotationWatcher.Stub() {
                            public void onRotationChanged(int rotation) {
                                android.hardware.LegacySensorManager.onRotationChanged(rotation);
                            }
                        });
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }
    }

    public int getSensors() {
        int result = 0;
        final java.util.List<android.hardware.Sensor> fullList = mSensorManager.getFullSensorList();
        for (android.hardware.Sensor i : fullList) {
            switch (i.getType()) {
                case android.hardware.Sensor.TYPE_ACCELEROMETER :
                    result |= android.hardware.SensorManager.SENSOR_ACCELEROMETER;
                    break;
                case android.hardware.Sensor.TYPE_MAGNETIC_FIELD :
                    result |= android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD;
                    break;
                case android.hardware.Sensor.TYPE_ORIENTATION :
                    result |= android.hardware.SensorManager.SENSOR_ORIENTATION | android.hardware.SensorManager.SENSOR_ORIENTATION_RAW;
                    break;
            }
        }
        return result;
    }

    public boolean registerListener(android.hardware.SensorListener listener, int sensors, int rate) {
        if (listener == null) {
            return false;
        }
        boolean result = false;
        result = registerLegacyListener(android.hardware.SensorManager.SENSOR_ACCELEROMETER, android.hardware.Sensor.TYPE_ACCELEROMETER, listener, sensors, rate) || result;
        result = registerLegacyListener(android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD, android.hardware.Sensor.TYPE_MAGNETIC_FIELD, listener, sensors, rate) || result;
        result = registerLegacyListener(android.hardware.SensorManager.SENSOR_ORIENTATION_RAW, android.hardware.Sensor.TYPE_ORIENTATION, listener, sensors, rate) || result;
        result = registerLegacyListener(android.hardware.SensorManager.SENSOR_ORIENTATION, android.hardware.Sensor.TYPE_ORIENTATION, listener, sensors, rate) || result;
        result = registerLegacyListener(android.hardware.SensorManager.SENSOR_TEMPERATURE, android.hardware.Sensor.TYPE_TEMPERATURE, listener, sensors, rate) || result;
        return result;
    }

    private boolean registerLegacyListener(int legacyType, int type, android.hardware.SensorListener listener, int sensors, int rate) {
        boolean result = false;
        // Are we activating this legacy sensor?
        if ((sensors & legacyType) != 0) {
            // if so, find a suitable Sensor
            android.hardware.Sensor sensor = mSensorManager.getDefaultSensor(type);
            if (sensor != null) {
                // We do all of this work holding the legacy listener lock to ensure
                // that the invariants around listeners are maintained.  This is safe
                // because neither registerLegacyListener nor unregisterLegacyListener
                // are called reentrantly while sensors are being registered or unregistered.
                synchronized(mLegacyListenersMap) {
                    // If we don't already have one, create a LegacyListener
                    // to wrap this listener and process the events as
                    // they are expected by legacy apps.
                    android.hardware.LegacySensorManager.LegacyListener legacyListener = mLegacyListenersMap.get(listener);
                    if (legacyListener == null) {
                        // we didn't find a LegacyListener for this client,
                        // create one, and put it in our list.
                        legacyListener = new android.hardware.LegacySensorManager.LegacyListener(listener);
                        mLegacyListenersMap.put(listener, legacyListener);
                    }
                    // register this legacy sensor with this legacy listener
                    if (legacyListener.registerSensor(legacyType)) {
                        // and finally, register the legacy listener with the new apis
                        result = mSensorManager.registerListener(legacyListener, sensor, rate);
                    } else {
                        result = true;// sensor already enabled

                    }
                }
            }
        }
        return result;
    }

    public void unregisterListener(android.hardware.SensorListener listener, int sensors) {
        if (listener == null) {
            return;
        }
        unregisterLegacyListener(android.hardware.SensorManager.SENSOR_ACCELEROMETER, android.hardware.Sensor.TYPE_ACCELEROMETER, listener, sensors);
        unregisterLegacyListener(android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD, android.hardware.Sensor.TYPE_MAGNETIC_FIELD, listener, sensors);
        unregisterLegacyListener(android.hardware.SensorManager.SENSOR_ORIENTATION_RAW, android.hardware.Sensor.TYPE_ORIENTATION, listener, sensors);
        unregisterLegacyListener(android.hardware.SensorManager.SENSOR_ORIENTATION, android.hardware.Sensor.TYPE_ORIENTATION, listener, sensors);
        unregisterLegacyListener(android.hardware.SensorManager.SENSOR_TEMPERATURE, android.hardware.Sensor.TYPE_TEMPERATURE, listener, sensors);
    }

    private void unregisterLegacyListener(int legacyType, int type, android.hardware.SensorListener listener, int sensors) {
        // Are we deactivating this legacy sensor?
        if ((sensors & legacyType) != 0) {
            // if so, find the corresponding Sensor
            android.hardware.Sensor sensor = mSensorManager.getDefaultSensor(type);
            if (sensor != null) {
                // We do all of this work holding the legacy listener lock to ensure
                // that the invariants around listeners are maintained.  This is safe
                // because neither registerLegacyListener nor unregisterLegacyListener
                // are called re-entrantly while sensors are being registered or unregistered.
                synchronized(mLegacyListenersMap) {
                    // do we know about this listener?
                    android.hardware.LegacySensorManager.LegacyListener legacyListener = mLegacyListenersMap.get(listener);
                    if (legacyListener != null) {
                        // unregister this legacy sensor and if we don't
                        // need the corresponding Sensor, unregister it too
                        if (legacyListener.unregisterSensor(legacyType)) {
                            // corresponding sensor not needed, unregister
                            mSensorManager.unregisterListener(legacyListener, sensor);
                            // finally check if we still need the legacyListener
                            // in our mapping, if not, get rid of it too.
                            if (!legacyListener.hasSensors()) {
                                mLegacyListenersMap.remove(listener);
                            }
                        }
                    }
                }
            }
        }
    }

    static void onRotationChanged(int rotation) {
        synchronized(android.hardware.SensorManager.class) {
            android.hardware.LegacySensorManager.sRotation = rotation;
        }
    }

    static int getRotation() {
        synchronized(android.hardware.SensorManager.class) {
            return android.hardware.LegacySensorManager.sRotation;
        }
    }

    private static final class LegacyListener implements android.hardware.SensorEventListener {
        private float[] mValues = new float[6];

        private android.hardware.SensorListener mTarget;

        private int mSensors;

        private final android.hardware.LegacySensorManager.LmsFilter mYawfilter = new android.hardware.LegacySensorManager.LmsFilter();

        LegacyListener(android.hardware.SensorListener target) {
            mTarget = target;
            mSensors = 0;
        }

        boolean registerSensor(int legacyType) {
            if ((mSensors & legacyType) != 0) {
                return false;
            }
            boolean alreadyHasOrientationSensor = android.hardware.LegacySensorManager.LegacyListener.hasOrientationSensor(mSensors);
            mSensors |= legacyType;
            if (alreadyHasOrientationSensor && android.hardware.LegacySensorManager.LegacyListener.hasOrientationSensor(legacyType)) {
                return false;// don't need to re-register the orientation sensor

            }
            return true;
        }

        boolean unregisterSensor(int legacyType) {
            if ((mSensors & legacyType) == 0) {
                return false;
            }
            mSensors &= ~legacyType;
            if (android.hardware.LegacySensorManager.LegacyListener.hasOrientationSensor(legacyType) && android.hardware.LegacySensorManager.LegacyListener.hasOrientationSensor(mSensors)) {
                return false;// can't unregister the orientation sensor just yet

            }
            return true;
        }

        boolean hasSensors() {
            return mSensors != 0;
        }

        private static boolean hasOrientationSensor(int sensors) {
            return (sensors & (android.hardware.SensorManager.SENSOR_ORIENTATION | android.hardware.SensorManager.SENSOR_ORIENTATION_RAW)) != 0;
        }

        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
            try {
                mTarget.onAccuracyChanged(android.hardware.LegacySensorManager.LegacyListener.getLegacySensorType(sensor.getType()), accuracy);
            } catch (java.lang.AbstractMethodError e) {
                // old app that doesn't implement this method
                // just ignore it.
            }
        }

        public void onSensorChanged(android.hardware.SensorEvent event) {
            final float[] v = mValues;
            v[0] = event.values[0];
            v[1] = event.values[1];
            v[2] = event.values[2];
            int type = event.sensor.getType();
            int legacyType = android.hardware.LegacySensorManager.LegacyListener.getLegacySensorType(type);
            mapSensorDataToWindow(legacyType, v, android.hardware.LegacySensorManager.getRotation());
            if (type == android.hardware.Sensor.TYPE_ORIENTATION) {
                if ((mSensors & android.hardware.SensorManager.SENSOR_ORIENTATION_RAW) != 0) {
                    mTarget.onSensorChanged(android.hardware.SensorManager.SENSOR_ORIENTATION_RAW, v);
                }
                if ((mSensors & android.hardware.SensorManager.SENSOR_ORIENTATION) != 0) {
                    v[0] = mYawfilter.filter(event.timestamp, v[0]);
                    mTarget.onSensorChanged(android.hardware.SensorManager.SENSOR_ORIENTATION, v);
                }
            } else {
                mTarget.onSensorChanged(legacyType, v);
            }
        }

        /* Helper function to convert the specified sensor's data to the windows's
        coordinate space from the device's coordinate space.

        output: 3,4,5: values in the old API format
                0,1,2: transformed values in the old API format
         */
        private void mapSensorDataToWindow(int sensor, float[] values, int orientation) {
            float x = values[0];
            float y = values[1];
            float z = values[2];
            switch (sensor) {
                case android.hardware.SensorManager.SENSOR_ORIENTATION :
                case android.hardware.SensorManager.SENSOR_ORIENTATION_RAW :
                    z = -z;
                    break;
                case android.hardware.SensorManager.SENSOR_ACCELEROMETER :
                    x = -x;
                    y = -y;
                    z = -z;
                    break;
                case android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD :
                    x = -x;
                    y = -y;
                    break;
            }
            values[0] = x;
            values[1] = y;
            values[2] = z;
            values[3] = x;
            values[4] = y;
            values[5] = z;
            if ((orientation & android.view.Surface.ROTATION_90) != 0) {
                // handles 90 and 270 rotation
                switch (sensor) {
                    case android.hardware.SensorManager.SENSOR_ACCELEROMETER :
                    case android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD :
                        values[0] = -y;
                        values[1] = x;
                        values[2] = z;
                        break;
                    case android.hardware.SensorManager.SENSOR_ORIENTATION :
                    case android.hardware.SensorManager.SENSOR_ORIENTATION_RAW :
                        values[0] = x + (x < 270 ? 90 : -270);
                        values[1] = z;
                        values[2] = y;
                        break;
                }
            }
            if ((orientation & android.view.Surface.ROTATION_180) != 0) {
                x = values[0];
                y = values[1];
                z = values[2];
                // handles 180 (flip) and 270 (flip + 90) rotation
                switch (sensor) {
                    case android.hardware.SensorManager.SENSOR_ACCELEROMETER :
                    case android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD :
                        values[0] = -x;
                        values[1] = -y;
                        values[2] = z;
                        break;
                    case android.hardware.SensorManager.SENSOR_ORIENTATION :
                    case android.hardware.SensorManager.SENSOR_ORIENTATION_RAW :
                        values[0] = (x >= 180) ? x - 180 : x + 180;
                        values[1] = -y;
                        values[2] = -z;
                        break;
                }
            }
        }

        private static int getLegacySensorType(int type) {
            switch (type) {
                case android.hardware.Sensor.TYPE_ACCELEROMETER :
                    return android.hardware.SensorManager.SENSOR_ACCELEROMETER;
                case android.hardware.Sensor.TYPE_MAGNETIC_FIELD :
                    return android.hardware.SensorManager.SENSOR_MAGNETIC_FIELD;
                case android.hardware.Sensor.TYPE_ORIENTATION :
                    return android.hardware.SensorManager.SENSOR_ORIENTATION_RAW;
                case android.hardware.Sensor.TYPE_TEMPERATURE :
                    return android.hardware.SensorManager.SENSOR_TEMPERATURE;
            }
            return 0;
        }
    }

    private static final class LmsFilter {
        private static final int SENSORS_RATE_MS = 20;

        private static final int COUNT = 12;

        private static final float PREDICTION_RATIO = 1.0F / 3.0F;

        private static final float PREDICTION_TIME = ((android.hardware.LegacySensorManager.LmsFilter.SENSORS_RATE_MS * android.hardware.LegacySensorManager.LmsFilter.COUNT) / 1000.0F) * android.hardware.LegacySensorManager.LmsFilter.PREDICTION_RATIO;

        private float[] mV = new float[android.hardware.LegacySensorManager.LmsFilter.COUNT * 2];

        private long[] mT = new long[android.hardware.LegacySensorManager.LmsFilter.COUNT * 2];

        private int mIndex;

        public LmsFilter() {
            mIndex = android.hardware.LegacySensorManager.LmsFilter.COUNT;
        }

        public float filter(long time, float in) {
            float v = in;
            final float ns = 1.0F / 1.0E9F;
            float v1 = mV[mIndex];
            if ((v - v1) > 180) {
                v -= 360;
            } else
                if ((v1 - v) > 180) {
                    v += 360;
                }

            /* Manage the circular buffer, we write the data twice spaced
            by COUNT values, so that we don't have to copy the array
            when it's full
             */
            mIndex++;
            if (mIndex >= (android.hardware.LegacySensorManager.LmsFilter.COUNT * 2))
                mIndex = android.hardware.LegacySensorManager.LmsFilter.COUNT;

            mV[mIndex] = v;
            mT[mIndex] = time;
            mV[mIndex - android.hardware.LegacySensorManager.LmsFilter.COUNT] = v;
            mT[mIndex - android.hardware.LegacySensorManager.LmsFilter.COUNT] = time;
            float A;
            float B;
            float C;
            float D;
            float E;
            float a;
            float b;
            int i;
            A = B = C = D = E = 0;
            for (i = 0; i < (android.hardware.LegacySensorManager.LmsFilter.COUNT - 1); i++) {
                final int j = (mIndex - 1) - i;
                final float Z = mV[j];
                final float T = (((mT[j] / 2) + (mT[j + 1] / 2)) - time) * ns;
                float dT = (mT[j] - mT[j + 1]) * ns;
                dT *= dT;
                A += Z * dT;
                B += T * (T * dT);
                C += T * dT;
                D += Z * (T * dT);
                E += dT;
            }
            b = ((A * B) + (C * D)) / ((E * B) + (C * C));
            a = ((E * b) - A) / C;
            float f = b + (android.hardware.LegacySensorManager.LmsFilter.PREDICTION_TIME * a);
            // Normalize
            f *= 1.0F / 360.0F;
            if ((f >= 0 ? f : -f) >= 0.5F)
                f = (f - ((float) (java.lang.Math.ceil(f + 0.5F)))) + 1.0F;

            if (f < 0)
                f += 1.0F;

            f *= 360.0F;
            return f;
        }
    }
}

