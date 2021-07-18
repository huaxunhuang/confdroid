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
 * Sensor manager implementation that communicates with the built-in
 * system sensors.
 *
 * @unknown 
 */
public class SystemSensorManager extends android.hardware.SensorManager {
    // TODO: disable extra logging before release
    private static boolean DEBUG_DYNAMIC_SENSOR = true;

    private static native void nativeClassInit();

    private static native long nativeCreate(java.lang.String opPackageName);

    private static native boolean nativeGetSensorAtIndex(long nativeInstance, android.hardware.Sensor sensor, int index);

    private static native void nativeGetDynamicSensors(long nativeInstance, java.util.List<android.hardware.Sensor> list);

    private static native boolean nativeIsDataInjectionEnabled(long nativeInstance);

    private static final java.lang.Object sLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("sLock")
    private static boolean sNativeClassInited = false;

    @com.android.internal.annotations.GuardedBy("sLock")
    private static android.hardware.SystemSensorManager.InjectEventQueue sInjectEventQueue = null;

    private final java.util.ArrayList<android.hardware.Sensor> mFullSensorsList = new java.util.ArrayList<>();

    private java.util.List<android.hardware.Sensor> mFullDynamicSensorsList = new java.util.ArrayList<>();

    private boolean mDynamicSensorListDirty = true;

    private final java.util.HashMap<java.lang.Integer, android.hardware.Sensor> mHandleToSensor = new java.util.HashMap<>();

    // Listener list
    private final java.util.HashMap<android.hardware.SensorEventListener, android.hardware.SystemSensorManager.SensorEventQueue> mSensorListeners = new java.util.HashMap<android.hardware.SensorEventListener, android.hardware.SystemSensorManager.SensorEventQueue>();

    private final java.util.HashMap<android.hardware.TriggerEventListener, android.hardware.SystemSensorManager.TriggerEventQueue> mTriggerListeners = new java.util.HashMap<android.hardware.TriggerEventListener, android.hardware.SystemSensorManager.TriggerEventQueue>();

    // Dynamic Sensor callbacks
    private java.util.HashMap<android.hardware.SensorManager.DynamicSensorCallback, android.os.Handler> mDynamicSensorCallbacks = new java.util.HashMap<>();

    private android.content.BroadcastReceiver mDynamicSensorBroadcastReceiver;

    // Looper associated with the context in which this instance was created.
    private final android.os.Looper mMainLooper;

    private final int mTargetSdkLevel;

    private final android.content.Context mContext;

    private final long mNativeInstance;

    /**
     * {@hide }
     */
    public SystemSensorManager(android.content.Context context, android.os.Looper mainLooper) {
        synchronized(android.hardware.SystemSensorManager.sLock) {
            if (!android.hardware.SystemSensorManager.sNativeClassInited) {
                android.hardware.SystemSensorManager.sNativeClassInited = true;
                android.hardware.SystemSensorManager.nativeClassInit();
            }
        }
        mMainLooper = mainLooper;
        mTargetSdkLevel = context.getApplicationInfo().targetSdkVersion;
        mContext = context;
        mNativeInstance = android.hardware.SystemSensorManager.nativeCreate(context.getOpPackageName());
        // initialize the sensor list
        for (int index = 0; ; ++index) {
            android.hardware.Sensor sensor = new android.hardware.Sensor();
            if (!android.hardware.SystemSensorManager.nativeGetSensorAtIndex(mNativeInstance, sensor, index))
                break;

            mFullSensorsList.add(sensor);
            mHandleToSensor.put(sensor.getHandle(), sensor);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected java.util.List<android.hardware.Sensor> getFullSensorList() {
        return mFullSensorsList;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected java.util.List<android.hardware.Sensor> getFullDynamicSensorList() {
        // only set up broadcast receiver if the application tries to find dynamic sensors or
        // explicitly register a DynamicSensorCallback
        setupDynamicSensorBroadcastReceiver();
        updateDynamicSensorList();
        return mFullDynamicSensorsList;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected boolean registerListenerImpl(android.hardware.SensorEventListener listener, android.hardware.Sensor sensor, int delayUs, android.os.Handler handler, int maxBatchReportLatencyUs, int reservedFlags) {
        if ((listener == null) || (sensor == null)) {
            android.util.Log.e(android.hardware.SensorManager.TAG, "sensor or listener is null");
            return false;
        }
        // Trigger Sensors should use the requestTriggerSensor call.
        if (sensor.getReportingMode() == android.hardware.Sensor.REPORTING_MODE_ONE_SHOT) {
            android.util.Log.e(android.hardware.SensorManager.TAG, "Trigger Sensors should use the requestTriggerSensor.");
            return false;
        }
        if ((maxBatchReportLatencyUs < 0) || (delayUs < 0)) {
            android.util.Log.e(android.hardware.SensorManager.TAG, "maxBatchReportLatencyUs and delayUs should be non-negative");
            return false;
        }
        // Invariants to preserve:
        // - one Looper per SensorEventListener
        // - one Looper per SensorEventQueue
        // We map SensorEventListener to a SensorEventQueue, which holds the looper
        synchronized(mSensorListeners) {
            android.hardware.SystemSensorManager.SensorEventQueue queue = mSensorListeners.get(listener);
            if (queue == null) {
                android.os.Looper looper = (handler != null) ? handler.getLooper() : mMainLooper;
                final java.lang.String fullClassName = (listener.getClass().getEnclosingClass() != null) ? listener.getClass().getEnclosingClass().getName() : listener.getClass().getName();
                queue = new android.hardware.SystemSensorManager.SensorEventQueue(listener, looper, this, fullClassName);
                if (!queue.addSensor(sensor, delayUs, maxBatchReportLatencyUs)) {
                    queue.dispose();
                    return false;
                }
                mSensorListeners.put(listener, queue);
                return true;
            } else {
                return queue.addSensor(sensor, delayUs, maxBatchReportLatencyUs);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void unregisterListenerImpl(android.hardware.SensorEventListener listener, android.hardware.Sensor sensor) {
        // Trigger Sensors should use the cancelTriggerSensor call.
        if ((sensor != null) && (sensor.getReportingMode() == android.hardware.Sensor.REPORTING_MODE_ONE_SHOT)) {
            return;
        }
        synchronized(mSensorListeners) {
            android.hardware.SystemSensorManager.SensorEventQueue queue = mSensorListeners.get(listener);
            if (queue != null) {
                boolean result;
                if (sensor == null) {
                    result = queue.removeAllSensors();
                } else {
                    result = queue.removeSensor(sensor, true);
                }
                if (result && (!queue.hasSensors())) {
                    mSensorListeners.remove(listener);
                    queue.dispose();
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected boolean requestTriggerSensorImpl(android.hardware.TriggerEventListener listener, android.hardware.Sensor sensor) {
        if (sensor == null)
            throw new java.lang.IllegalArgumentException("sensor cannot be null");

        if (listener == null)
            throw new java.lang.IllegalArgumentException("listener cannot be null");

        if (sensor.getReportingMode() != android.hardware.Sensor.REPORTING_MODE_ONE_SHOT)
            return false;

        synchronized(mTriggerListeners) {
            android.hardware.SystemSensorManager.TriggerEventQueue queue = mTriggerListeners.get(listener);
            if (queue == null) {
                final java.lang.String fullClassName = (listener.getClass().getEnclosingClass() != null) ? listener.getClass().getEnclosingClass().getName() : listener.getClass().getName();
                queue = new android.hardware.SystemSensorManager.TriggerEventQueue(listener, mMainLooper, this, fullClassName);
                if (!queue.addSensor(sensor, 0, 0)) {
                    queue.dispose();
                    return false;
                }
                mTriggerListeners.put(listener, queue);
                return true;
            } else {
                return queue.addSensor(sensor, 0, 0);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected boolean cancelTriggerSensorImpl(android.hardware.TriggerEventListener listener, android.hardware.Sensor sensor, boolean disable) {
        if ((sensor != null) && (sensor.getReportingMode() != android.hardware.Sensor.REPORTING_MODE_ONE_SHOT)) {
            return false;
        }
        synchronized(mTriggerListeners) {
            android.hardware.SystemSensorManager.TriggerEventQueue queue = mTriggerListeners.get(listener);
            if (queue != null) {
                boolean result;
                if (sensor == null) {
                    result = queue.removeAllSensors();
                } else {
                    result = queue.removeSensor(sensor, disable);
                }
                if (result && (!queue.hasSensors())) {
                    mTriggerListeners.remove(listener);
                    queue.dispose();
                }
                return result;
            }
            return false;
        }
    }

    protected boolean flushImpl(android.hardware.SensorEventListener listener) {
        if (listener == null)
            throw new java.lang.IllegalArgumentException("listener cannot be null");

        synchronized(mSensorListeners) {
            android.hardware.SystemSensorManager.SensorEventQueue queue = mSensorListeners.get(listener);
            if (queue == null) {
                return false;
            } else {
                return queue.flush() == 0;
            }
        }
    }

    protected boolean initDataInjectionImpl(boolean enable) {
        synchronized(android.hardware.SystemSensorManager.sLock) {
            if (enable) {
                boolean isDataInjectionModeEnabled = android.hardware.SystemSensorManager.nativeIsDataInjectionEnabled(mNativeInstance);
                // The HAL does not support injection OR SensorService hasn't been set in DI mode.
                if (!isDataInjectionModeEnabled) {
                    android.util.Log.e(android.hardware.SensorManager.TAG, "Data Injection mode not enabled");
                    return false;
                }
                // Initialize a client for data_injection.
                if (android.hardware.SystemSensorManager.sInjectEventQueue == null) {
                    android.hardware.SystemSensorManager.sInjectEventQueue = new android.hardware.SystemSensorManager.InjectEventQueue(mMainLooper, this, mContext.getPackageName());
                }
            } else {
                // If data injection is being disabled clean up the native resources.
                if (android.hardware.SystemSensorManager.sInjectEventQueue != null) {
                    android.hardware.SystemSensorManager.sInjectEventQueue.dispose();
                    android.hardware.SystemSensorManager.sInjectEventQueue = null;
                }
            }
            return true;
        }
    }

    protected boolean injectSensorDataImpl(android.hardware.Sensor sensor, float[] values, int accuracy, long timestamp) {
        synchronized(android.hardware.SystemSensorManager.sLock) {
            if (android.hardware.SystemSensorManager.sInjectEventQueue == null) {
                android.util.Log.e(android.hardware.SensorManager.TAG, "Data injection mode not activated before calling injectSensorData");
                return false;
            }
            int ret = android.hardware.SystemSensorManager.sInjectEventQueue.injectSensorData(sensor.getHandle(), values, accuracy, timestamp);
            // If there are any errors in data injection clean up the native resources.
            if (ret != 0) {
                android.hardware.SystemSensorManager.sInjectEventQueue.dispose();
                android.hardware.SystemSensorManager.sInjectEventQueue = null;
            }
            return ret == 0;
        }
    }

    private void cleanupSensorConnection(android.hardware.Sensor sensor) {
        mHandleToSensor.remove(sensor.getHandle());
        if (sensor.getReportingMode() == android.hardware.Sensor.REPORTING_MODE_ONE_SHOT) {
            synchronized(mTriggerListeners) {
                for (android.hardware.TriggerEventListener l : mTriggerListeners.keySet()) {
                    if (android.hardware.SystemSensorManager.DEBUG_DYNAMIC_SENSOR) {
                        android.util.Log.i(android.hardware.SensorManager.TAG, ("removed trigger listener" + l.toString()) + " due to sensor disconnection");
                    }
                    cancelTriggerSensorImpl(l, sensor, true);
                }
            }
        } else {
            synchronized(mSensorListeners) {
                for (android.hardware.SensorEventListener l : mSensorListeners.keySet()) {
                    if (android.hardware.SystemSensorManager.DEBUG_DYNAMIC_SENSOR) {
                        android.util.Log.i(android.hardware.SensorManager.TAG, ("removed event listener" + l.toString()) + " due to sensor disconnection");
                    }
                    unregisterListenerImpl(l, sensor);
                }
            }
        }
    }

    private void updateDynamicSensorList() {
        synchronized(mFullDynamicSensorsList) {
            if (mDynamicSensorListDirty) {
                java.util.List<android.hardware.Sensor> list = new java.util.ArrayList<>();
                android.hardware.SystemSensorManager.nativeGetDynamicSensors(mNativeInstance, list);
                final java.util.List<android.hardware.Sensor> updatedList = new java.util.ArrayList<>();
                final java.util.List<android.hardware.Sensor> addedList = new java.util.ArrayList<>();
                final java.util.List<android.hardware.Sensor> removedList = new java.util.ArrayList<>();
                boolean changed = android.hardware.SystemSensorManager.diffSortedSensorList(mFullDynamicSensorsList, list, updatedList, addedList, removedList);
                if (changed) {
                    if (android.hardware.SystemSensorManager.DEBUG_DYNAMIC_SENSOR) {
                        android.util.Log.i(android.hardware.SensorManager.TAG, "DYNS dynamic sensor list cached should be updated");
                    }
                    mFullDynamicSensorsList = updatedList;
                    for (android.hardware.Sensor s : addedList) {
                        mHandleToSensor.put(s.getHandle(), s);
                    }
                    android.os.Handler mainHandler = new android.os.Handler(mContext.getMainLooper());
                    for (java.util.Map.Entry<android.hardware.SensorManager.DynamicSensorCallback, android.os.Handler> entry : mDynamicSensorCallbacks.entrySet()) {
                        final android.hardware.SensorManager.DynamicSensorCallback callback = entry.getKey();
                        android.os.Handler handler = (entry.getValue() == null) ? mainHandler : entry.getValue();
                        handler.post(new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                for (android.hardware.Sensor s : addedList) {
                                    callback.onDynamicSensorConnected(s);
                                }
                                for (android.hardware.Sensor s : removedList) {
                                    callback.onDynamicSensorDisconnected(s);
                                }
                            }
                        });
                    }
                    for (android.hardware.Sensor s : removedList) {
                        cleanupSensorConnection(s);
                    }
                }
                mDynamicSensorListDirty = false;
            }
        }
    }

    private void setupDynamicSensorBroadcastReceiver() {
        if (mDynamicSensorBroadcastReceiver == null) {
            mDynamicSensorBroadcastReceiver = new android.content.BroadcastReceiver() {
                @java.lang.Override
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    if (intent.getAction() == android.content.Intent.ACTION_DYNAMIC_SENSOR_CHANGED) {
                        if (android.hardware.SystemSensorManager.DEBUG_DYNAMIC_SENSOR) {
                            android.util.Log.i(android.hardware.SensorManager.TAG, "DYNS received DYNAMIC_SENSOR_CHANED broadcast");
                        }
                        // Dynamic sensors probably changed
                        mDynamicSensorListDirty = true;
                        updateDynamicSensorList();
                    }
                }
            };
            android.content.IntentFilter filter = new android.content.IntentFilter("dynamic_sensor_change");
            filter.addAction(android.content.Intent.ACTION_DYNAMIC_SENSOR_CHANGED);
            mContext.registerReceiver(mDynamicSensorBroadcastReceiver, filter);
        }
    }

    private void teardownDynamicSensorBroadcastReceiver() {
        mDynamicSensorCallbacks.clear();
        mContext.unregisterReceiver(mDynamicSensorBroadcastReceiver);
        mDynamicSensorBroadcastReceiver = null;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void registerDynamicSensorCallbackImpl(android.hardware.SensorManager.DynamicSensorCallback callback, android.os.Handler handler) {
        if (android.hardware.SystemSensorManager.DEBUG_DYNAMIC_SENSOR) {
            android.util.Log.i(android.hardware.SensorManager.TAG, "DYNS Register dynamic sensor callback");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback cannot be null");
        }
        if (mDynamicSensorCallbacks.containsKey(callback)) {
            // has been already registered, ignore
            return;
        }
        setupDynamicSensorBroadcastReceiver();
        mDynamicSensorCallbacks.put(callback, handler);
    }

    /**
     *
     *
     * @unknown 
     */
    protected void unregisterDynamicSensorCallbackImpl(android.hardware.SensorManager.DynamicSensorCallback callback) {
        if (android.hardware.SystemSensorManager.DEBUG_DYNAMIC_SENSOR) {
            android.util.Log.i(android.hardware.SensorManager.TAG, "Removing dynamic sensor listerner");
        }
        mDynamicSensorCallbacks.remove(callback);
    }

    /* Find the difference of two List<Sensor> assuming List are sorted by handle of sensor,
    assuming the input list is already sorted by handle. Inputs are ol and nl; outputs are
    updated, added and removed. Any of the output lists can be null in case the result is not
    interested.
     */
    private static boolean diffSortedSensorList(java.util.List<android.hardware.Sensor> oldList, java.util.List<android.hardware.Sensor> newList, java.util.List<android.hardware.Sensor> updated, java.util.List<android.hardware.Sensor> added, java.util.List<android.hardware.Sensor> removed) {
        boolean changed = false;
        int i = 0;
        int j = 0;
        while (true) {
            if ((j < oldList.size()) && ((i >= newList.size()) || (newList.get(i).getHandle() > oldList.get(j).getHandle()))) {
                changed = true;
                if (removed != null) {
                    removed.add(oldList.get(j));
                }
                ++j;
            } else
                if ((i < newList.size()) && ((j >= oldList.size()) || (newList.get(i).getHandle() < oldList.get(j).getHandle()))) {
                    changed = true;
                    if (added != null) {
                        added.add(newList.get(i));
                    }
                    if (updated != null) {
                        updated.add(newList.get(i));
                    }
                    ++i;
                } else
                    if (((i < newList.size()) && (j < oldList.size())) && (newList.get(i).getHandle() == oldList.get(j).getHandle())) {
                        if (updated != null) {
                            updated.add(oldList.get(j));
                        }
                        ++i;
                        ++j;
                    } else {
                        break;
                    }


        } 
        return changed;
    }

    /* BaseEventQueue is the communication channel with the sensor service,
    SensorEventQueue, TriggerEventQueue are subclases and there is one-to-one mapping between
    the queues and the listeners. InjectEventQueue is also a sub-class which is a special case
    where data is being injected into the sensor HAL through the sensor service. It is not
    associated with any listener and there is one InjectEventQueue associated with a
    SensorManager instance.
     */
    private static abstract class BaseEventQueue {
        private static native long nativeInitBaseEventQueue(long nativeManager, java.lang.ref.WeakReference<android.hardware.SystemSensorManager.BaseEventQueue> eventQWeak, android.os.MessageQueue msgQ, java.lang.String packageName, int mode, java.lang.String opPackageName);

        private static native int nativeEnableSensor(long eventQ, int handle, int rateUs, int maxBatchReportLatencyUs);

        private static native int nativeDisableSensor(long eventQ, int handle);

        private static native void nativeDestroySensorEventQueue(long eventQ);

        private static native int nativeFlushSensor(long eventQ);

        private static native int nativeInjectSensorData(long eventQ, int handle, float[] values, int accuracy, long timestamp);

        private long nSensorEventQueue;

        private final android.util.SparseBooleanArray mActiveSensors = new android.util.SparseBooleanArray();

        protected final android.util.SparseIntArray mSensorAccuracies = new android.util.SparseIntArray();

        private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

        protected final android.hardware.SystemSensorManager mManager;

        protected static final int OPERATING_MODE_NORMAL = 0;

        protected static final int OPERATING_MODE_DATA_INJECTION = 1;

        BaseEventQueue(android.os.Looper looper, android.hardware.SystemSensorManager manager, int mode, java.lang.String packageName) {
            if (packageName == null)
                packageName = "";

            nSensorEventQueue = android.hardware.SystemSensorManager.BaseEventQueue.nativeInitBaseEventQueue(manager.mNativeInstance, new java.lang.ref.WeakReference<>(this), looper.getQueue(), packageName, mode, manager.mContext.getOpPackageName());
            mCloseGuard.open("dispose");
            mManager = manager;
        }

        public void dispose() {
            dispose(false);
        }

        public boolean addSensor(android.hardware.Sensor sensor, int delayUs, int maxBatchReportLatencyUs) {
            // Check if already present.
            int handle = sensor.getHandle();
            if (mActiveSensors.get(handle))
                return false;

            // Get ready to receive events before calling enable.
            mActiveSensors.put(handle, true);
            addSensorEvent(sensor);
            if (enableSensor(sensor, delayUs, maxBatchReportLatencyUs) != 0) {
                // Try continuous mode if batching fails.
                if ((maxBatchReportLatencyUs == 0) || ((maxBatchReportLatencyUs > 0) && (enableSensor(sensor, delayUs, 0) != 0))) {
                    removeSensor(sensor, false);
                    return false;
                }
            }
            return true;
        }

        public boolean removeAllSensors() {
            for (int i = 0; i < mActiveSensors.size(); i++) {
                if (mActiveSensors.valueAt(i) == true) {
                    int handle = mActiveSensors.keyAt(i);
                    android.hardware.Sensor sensor = mManager.mHandleToSensor.get(handle);
                    if (sensor != null) {
                        disableSensor(sensor);
                        mActiveSensors.put(handle, false);
                        removeSensorEvent(sensor);
                    } else {
                        // sensor just disconnected -- just ignore.
                    }
                }
            }
            return true;
        }

        public boolean removeSensor(android.hardware.Sensor sensor, boolean disable) {
            final int handle = sensor.getHandle();
            if (mActiveSensors.get(handle)) {
                if (disable)
                    disableSensor(sensor);

                mActiveSensors.put(sensor.getHandle(), false);
                removeSensorEvent(sensor);
                return true;
            }
            return false;
        }

        public int flush() {
            if (nSensorEventQueue == 0)
                throw new java.lang.NullPointerException();

            return android.hardware.SystemSensorManager.BaseEventQueue.nativeFlushSensor(nSensorEventQueue);
        }

        public boolean hasSensors() {
            // no more sensors are set
            return mActiveSensors.indexOfValue(true) >= 0;
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            try {
                dispose(true);
            } finally {
                super.finalize();
            }
        }

        private void dispose(boolean finalized) {
            if (mCloseGuard != null) {
                if (finalized) {
                    mCloseGuard.warnIfOpen();
                }
                mCloseGuard.close();
            }
            if (nSensorEventQueue != 0) {
                android.hardware.SystemSensorManager.BaseEventQueue.nativeDestroySensorEventQueue(nSensorEventQueue);
                nSensorEventQueue = 0;
            }
        }

        private int enableSensor(android.hardware.Sensor sensor, int rateUs, int maxBatchReportLatencyUs) {
            if (nSensorEventQueue == 0)
                throw new java.lang.NullPointerException();

            if (sensor == null)
                throw new java.lang.NullPointerException();

            return android.hardware.SystemSensorManager.BaseEventQueue.nativeEnableSensor(nSensorEventQueue, sensor.getHandle(), rateUs, maxBatchReportLatencyUs);
        }

        protected int injectSensorDataBase(int handle, float[] values, int accuracy, long timestamp) {
            return android.hardware.SystemSensorManager.BaseEventQueue.nativeInjectSensorData(nSensorEventQueue, handle, values, accuracy, timestamp);
        }

        private int disableSensor(android.hardware.Sensor sensor) {
            if (nSensorEventQueue == 0)
                throw new java.lang.NullPointerException();

            if (sensor == null)
                throw new java.lang.NullPointerException();

            return android.hardware.SystemSensorManager.BaseEventQueue.nativeDisableSensor(nSensorEventQueue, sensor.getHandle());
        }

        protected abstract void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp);

        protected abstract void dispatchFlushCompleteEvent(int handle);

        protected void dispatchAdditionalInfoEvent(int handle, int type, int serial, float[] floatValues, int[] intValues) {
            // default implementation is do nothing
        }

        protected abstract void addSensorEvent(android.hardware.Sensor sensor);

        protected abstract void removeSensorEvent(android.hardware.Sensor sensor);
    }

    static final class SensorEventQueue extends android.hardware.SystemSensorManager.BaseEventQueue {
        private final android.hardware.SensorEventListener mListener;

        private final android.util.SparseArray<android.hardware.SensorEvent> mSensorsEvents = new android.util.SparseArray<android.hardware.SensorEvent>();

        public SensorEventQueue(android.hardware.SensorEventListener listener, android.os.Looper looper, android.hardware.SystemSensorManager manager, java.lang.String packageName) {
            super(looper, manager, android.hardware.SystemSensorManager.BaseEventQueue.OPERATING_MODE_NORMAL, packageName);
            mListener = listener;
        }

        @java.lang.Override
        public void addSensorEvent(android.hardware.Sensor sensor) {
            android.hardware.SensorEvent t = new android.hardware.SensorEvent(android.hardware.Sensor.getMaxLengthValuesArray(sensor, mManager.mTargetSdkLevel));
            synchronized(mSensorsEvents) {
                mSensorsEvents.put(sensor.getHandle(), t);
            }
        }

        @java.lang.Override
        public void removeSensorEvent(android.hardware.Sensor sensor) {
            synchronized(mSensorsEvents) {
                mSensorsEvents.delete(sensor.getHandle());
            }
        }

        // Called from native code.
        @java.lang.SuppressWarnings("unused")
        @java.lang.Override
        protected void dispatchSensorEvent(int handle, float[] values, int inAccuracy, long timestamp) {
            final android.hardware.Sensor sensor = mManager.mHandleToSensor.get(handle);
            if (sensor == null) {
                // sensor disconnected
                return;
            }
            android.hardware.SensorEvent t = null;
            synchronized(mSensorsEvents) {
                t = mSensorsEvents.get(handle);
            }
            if (t == null) {
                // This may happen if the client has unregistered and there are pending events in
                // the queue waiting to be delivered. Ignore.
                return;
            }
            // Copy from the values array.
            java.lang.System.arraycopy(values, 0, t.values, 0, t.values.length);
            t.timestamp = timestamp;
            t.accuracy = inAccuracy;
            t.sensor = sensor;
            // call onAccuracyChanged() only if the value changes
            final int accuracy = mSensorAccuracies.get(handle);
            if ((t.accuracy >= 0) && (accuracy != t.accuracy)) {
                mSensorAccuracies.put(handle, t.accuracy);
                mListener.onAccuracyChanged(t.sensor, t.accuracy);
            }
            mListener.onSensorChanged(t);
        }

        // Called from native code.
        @java.lang.SuppressWarnings("unused")
        @java.lang.Override
        protected void dispatchFlushCompleteEvent(int handle) {
            if (mListener instanceof android.hardware.SensorEventListener2) {
                final android.hardware.Sensor sensor = mManager.mHandleToSensor.get(handle);
                if (sensor == null) {
                    // sensor disconnected
                    return;
                }
                ((android.hardware.SensorEventListener2) (mListener)).onFlushCompleted(sensor);
            }
            return;
        }

        // Called from native code.
        @java.lang.SuppressWarnings("unused")
        @java.lang.Override
        protected void dispatchAdditionalInfoEvent(int handle, int type, int serial, float[] floatValues, int[] intValues) {
            if (mListener instanceof android.hardware.SensorEventCallback) {
                final android.hardware.Sensor sensor = mManager.mHandleToSensor.get(handle);
                if (sensor == null) {
                    // sensor disconnected
                    return;
                }
                android.hardware.SensorAdditionalInfo info = new android.hardware.SensorAdditionalInfo(sensor, type, serial, intValues, floatValues);
                ((android.hardware.SensorEventCallback) (mListener)).onSensorAdditionalInfo(info);
            }
        }
    }

    static final class TriggerEventQueue extends android.hardware.SystemSensorManager.BaseEventQueue {
        private final android.hardware.TriggerEventListener mListener;

        private final android.util.SparseArray<android.hardware.TriggerEvent> mTriggerEvents = new android.util.SparseArray<android.hardware.TriggerEvent>();

        public TriggerEventQueue(android.hardware.TriggerEventListener listener, android.os.Looper looper, android.hardware.SystemSensorManager manager, java.lang.String packageName) {
            super(looper, manager, android.hardware.SystemSensorManager.BaseEventQueue.OPERATING_MODE_NORMAL, packageName);
            mListener = listener;
        }

        @java.lang.Override
        public void addSensorEvent(android.hardware.Sensor sensor) {
            android.hardware.TriggerEvent t = new android.hardware.TriggerEvent(android.hardware.Sensor.getMaxLengthValuesArray(sensor, mManager.mTargetSdkLevel));
            synchronized(mTriggerEvents) {
                mTriggerEvents.put(sensor.getHandle(), t);
            }
        }

        @java.lang.Override
        public void removeSensorEvent(android.hardware.Sensor sensor) {
            synchronized(mTriggerEvents) {
                mTriggerEvents.delete(sensor.getHandle());
            }
        }

        // Called from native code.
        @java.lang.SuppressWarnings("unused")
        @java.lang.Override
        protected void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
            final android.hardware.Sensor sensor = mManager.mHandleToSensor.get(handle);
            if (sensor == null) {
                // sensor disconnected
                return;
            }
            android.hardware.TriggerEvent t = null;
            synchronized(mTriggerEvents) {
                t = mTriggerEvents.get(handle);
            }
            if (t == null) {
                android.util.Log.e(android.hardware.SensorManager.TAG, "Error: Trigger Event is null for Sensor: " + sensor);
                return;
            }
            // Copy from the values array.
            java.lang.System.arraycopy(values, 0, t.values, 0, t.values.length);
            t.timestamp = timestamp;
            t.sensor = sensor;
            // A trigger sensor is auto disabled. So just clean up and don't call native
            // disable.
            mManager.cancelTriggerSensorImpl(mListener, sensor, false);
            mListener.onTrigger(t);
        }

        @java.lang.SuppressWarnings("unused")
        protected void dispatchFlushCompleteEvent(int handle) {
        }
    }

    final class InjectEventQueue extends android.hardware.SystemSensorManager.BaseEventQueue {
        public InjectEventQueue(android.os.Looper looper, android.hardware.SystemSensorManager manager, java.lang.String packageName) {
            super(looper, manager, android.hardware.SystemSensorManager.BaseEventQueue.OPERATING_MODE_DATA_INJECTION, packageName);
        }

        int injectSensorData(int handle, float[] values, int accuracy, long timestamp) {
            return injectSensorDataBase(handle, values, accuracy, timestamp);
        }

        @java.lang.SuppressWarnings("unused")
        protected void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
        }

        @java.lang.SuppressWarnings("unused")
        protected void dispatchFlushCompleteEvent(int handle) {
        }

        @java.lang.SuppressWarnings("unused")
        protected void addSensorEvent(android.hardware.Sensor sensor) {
        }

        @java.lang.SuppressWarnings("unused")
        protected void removeSensorEvent(android.hardware.Sensor sensor) {
        }
    }
}

