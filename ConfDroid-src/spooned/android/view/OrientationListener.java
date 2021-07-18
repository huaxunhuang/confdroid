/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.view;


/**
 * Helper class for receiving notifications from the SensorManager when
 * the orientation of the device has changed.
 *
 * @deprecated use {@link android.view.OrientationEventListener} instead.
This class internally uses the OrientationEventListener.
 */
@java.lang.Deprecated
public abstract class OrientationListener implements android.hardware.SensorListener {
    private android.view.OrientationEventListener mOrientationEventLis;

    /**
     * Returned from onOrientationChanged when the device orientation cannot be determined
     * (typically when the device is in a close to flat position).
     *
     * @see #onOrientationChanged
     */
    public static final int ORIENTATION_UNKNOWN = android.view.OrientationEventListener.ORIENTATION_UNKNOWN;

    /**
     * Creates a new OrientationListener.
     *
     * @param context
     * 		for the OrientationListener.
     */
    public OrientationListener(android.content.Context context) {
        mOrientationEventLis = new android.view.OrientationListener.OrientationEventListenerInternal(context);
    }

    /**
     * Creates a new OrientationListener.
     *
     * @param context
     * 		for the OrientationListener.
     * @param rate
     * 		at which sensor events are processed (see also
     * 		{@link android.hardware.SensorManager SensorManager}). Use the default
     * 		value of {@link android.hardware.SensorManager#SENSOR_DELAY_NORMAL
     * 		SENSOR_DELAY_NORMAL} for simple screen orientation change detection.
     */
    public OrientationListener(android.content.Context context, int rate) {
        mOrientationEventLis = new android.view.OrientationListener.OrientationEventListenerInternal(context, rate);
    }

    class OrientationEventListenerInternal extends android.view.OrientationEventListener {
        OrientationEventListenerInternal(android.content.Context context) {
            super(context);
        }

        OrientationEventListenerInternal(android.content.Context context, int rate) {
            super(context, rate);
            // register so that onSensorChanged gets invoked
            registerListener(android.view.OrientationListener.this);
        }

        public void onOrientationChanged(int orientation) {
            android.view.OrientationListener.this.onOrientationChanged(orientation);
        }
    }

    /**
     * Enables the OrientationListener so it will monitor the sensor and call
     * {@link #onOrientationChanged} when the device orientation changes.
     */
    public void enable() {
        mOrientationEventLis.enable();
    }

    /**
     * Disables the OrientationListener.
     */
    public void disable() {
        mOrientationEventLis.disable();
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    public void onSensorChanged(int sensor, float[] values) {
        // just ignore the call here onOrientationChanged is invoked anyway
    }

    /**
     * Look at {@link android.view.OrientationEventListener#onOrientationChanged}
     * for method description and usage
     *
     * @param orientation
     * 		The new orientation of the device.
     * @see #ORIENTATION_UNKNOWN
     */
    public abstract void onOrientationChanged(int orientation);
}

