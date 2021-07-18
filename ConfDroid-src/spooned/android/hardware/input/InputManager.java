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
package android.hardware.input;


/**
 * Provides information about input devices and available key layouts.
 * <p>
 * Get an instance of this class by calling
 * {@link android.content.Context#getSystemService(java.lang.String)
 * Context.getSystemService()} with the argument
 * {@link android.content.Context#INPUT_SERVICE}.
 * </p>
 */
public final class InputManager {
    private static final java.lang.String TAG = "InputManager";

    private static final boolean DEBUG = false;

    private static final int MSG_DEVICE_ADDED = 1;

    private static final int MSG_DEVICE_REMOVED = 2;

    private static final int MSG_DEVICE_CHANGED = 3;

    private static android.hardware.input.InputManager sInstance;

    private final android.hardware.input.IInputManager mIm;

    // Guarded by mInputDevicesLock
    private final java.lang.Object mInputDevicesLock = new java.lang.Object();

    private android.util.SparseArray<android.view.InputDevice> mInputDevices;

    private android.hardware.input.InputManager.InputDevicesChangedListener mInputDevicesChangedListener;

    private final java.util.ArrayList<android.hardware.input.InputManager.InputDeviceListenerDelegate> mInputDeviceListeners = new java.util.ArrayList<android.hardware.input.InputManager.InputDeviceListenerDelegate>();

    // Guarded by mTabletModeLock
    private final java.lang.Object mTabletModeLock = new java.lang.Object();

    private android.hardware.input.InputManager.TabletModeChangedListener mTabletModeChangedListener;

    private java.util.List<android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate> mOnTabletModeChangedListeners;

    /**
     * Broadcast Action: Query available keyboard layouts.
     * <p>
     * The input manager service locates available keyboard layouts
     * by querying broadcast receivers that are registered for this action.
     * An application can offer additional keyboard layouts to the user
     * by declaring a suitable broadcast receiver in its manifest.
     * </p><p>
     * Here is an example broadcast receiver declaration that an application
     * might include in its AndroidManifest.xml to advertise keyboard layouts.
     * The meta-data specifies a resource that contains a description of each keyboard
     * layout that is provided by the application.
     * <pre><code>
     * &lt;receiver android:name=".InputDeviceReceiver"
     *         android:label="@string/keyboard_layouts_label">
     *     &lt;intent-filter>
     *         &lt;action android:name="android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS" />
     *     &lt;/intent-filter>
     *     &lt;meta-data android:name="android.hardware.input.metadata.KEYBOARD_LAYOUTS"
     *             android:resource="@xml/keyboard_layouts" />
     * &lt;/receiver>
     * </code></pre>
     * </p><p>
     * In the above example, the <code>@xml/keyboard_layouts</code> resource refers to
     * an XML resource whose root element is <code>&lt;keyboard-layouts></code> that
     * contains zero or more <code>&lt;keyboard-layout></code> elements.
     * Each <code>&lt;keyboard-layout></code> element specifies the name, label, and location
     * of a key character map for a particular keyboard layout.  The label on the receiver
     * is used to name the collection of keyboard layouts provided by this receiver in the
     * keyboard layout settings.
     * <pre><code>
     * &lt;?xml version="1.0" encoding="utf-8"?>
     * &lt;keyboard-layouts xmlns:android="http://schemas.android.com/apk/res/android">
     *     &lt;keyboard-layout android:name="keyboard_layout_english_us"
     *             android:label="@string/keyboard_layout_english_us_label"
     *             android:keyboardLayout="@raw/keyboard_layout_english_us" />
     * &lt;/keyboard-layouts>
     * </pre></code>
     * </p><p>
     * The <code>android:name</code> attribute specifies an identifier by which
     * the keyboard layout will be known in the package.
     * The <code>android:label</code> attribute specifies a human-readable descriptive
     * label to describe the keyboard layout in the user interface, such as "English (US)".
     * The <code>android:keyboardLayout</code> attribute refers to a
     * <a href="http://source.android.com/tech/input/key-character-map-files.html">
     * key character map</a> resource that defines the keyboard layout.
     * </p>
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_QUERY_KEYBOARD_LAYOUTS = "android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS";

    /**
     * Metadata Key: Keyboard layout metadata associated with
     * {@link #ACTION_QUERY_KEYBOARD_LAYOUTS}.
     * <p>
     * Specifies the resource id of a XML resource that describes the keyboard
     * layouts that are provided by the application.
     * </p>
     */
    public static final java.lang.String META_DATA_KEYBOARD_LAYOUTS = "android.hardware.input.metadata.KEYBOARD_LAYOUTS";

    /**
     * Pointer Speed: The minimum (slowest) pointer speed (-7).
     *
     * @unknown 
     */
    public static final int MIN_POINTER_SPEED = -7;

    /**
     * Pointer Speed: The maximum (fastest) pointer speed (7).
     *
     * @unknown 
     */
    public static final int MAX_POINTER_SPEED = 7;

    /**
     * Pointer Speed: The default pointer speed (0).
     *
     * @unknown 
     */
    public static final int DEFAULT_POINTER_SPEED = 0;

    /**
     * Input Event Injection Synchronization Mode: None.
     * Never blocks.  Injection is asynchronous and is assumed always to be successful.
     *
     * @unknown 
     */
    public static final int INJECT_INPUT_EVENT_MODE_ASYNC = 0;// see InputDispatcher.h


    /**
     * Input Event Injection Synchronization Mode: Wait for result.
     * Waits for previous events to be dispatched so that the input dispatcher can
     * determine whether input event injection will be permitted based on the current
     * input focus.  Does not wait for the input event to finish being handled
     * by the application.
     *
     * @unknown 
     */
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1;// see InputDispatcher.h


    /**
     * Input Event Injection Synchronization Mode: Wait for finish.
     * Waits for the event to be delivered to the application and handled.
     *
     * @unknown 
     */
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2;// see InputDispatcher.h


    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.hardware.input.InputManager.SWITCH_STATE_UNKNOWN, android.hardware.input.InputManager.SWITCH_STATE_OFF, android.hardware.input.InputManager.SWITCH_STATE_ON })
    public @interface SwitchState {}

    /**
     * Switch State: Unknown.
     *
     * The system has yet to report a valid value for the switch.
     *
     * @unknown 
     */
    public static final int SWITCH_STATE_UNKNOWN = -1;

    /**
     * Switch State: Off.
     *
     * @unknown 
     */
    public static final int SWITCH_STATE_OFF = 0;

    /**
     * Switch State: On.
     *
     * @unknown 
     */
    public static final int SWITCH_STATE_ON = 1;

    private InputManager(android.hardware.input.IInputManager im) {
        mIm = im;
    }

    /**
     * Gets an instance of the input manager.
     *
     * @return The input manager instance.
     * @unknown 
     */
    public static android.hardware.input.InputManager getInstance() {
        synchronized(android.hardware.input.InputManager.class) {
            if (android.hardware.input.InputManager.sInstance == null) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.INPUT_SERVICE);
                android.hardware.input.InputManager.sInstance = new android.hardware.input.InputManager(IInputManager.Stub.asInterface(b));
            }
            return android.hardware.input.InputManager.sInstance;
        }
    }

    /**
     * Gets information about the input device with the specified id.
     *
     * @param id
     * 		The device id.
     * @return The input device or null if not found.
     */
    public android.view.InputDevice getInputDevice(int id) {
        synchronized(mInputDevicesLock) {
            populateInputDevicesLocked();
            int index = mInputDevices.indexOfKey(id);
            if (index < 0) {
                return null;
            }
            android.view.InputDevice inputDevice = mInputDevices.valueAt(index);
            if (inputDevice == null) {
                try {
                    inputDevice = mIm.getInputDevice(id);
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
                if (inputDevice != null) {
                    mInputDevices.setValueAt(index, inputDevice);
                }
            }
            return inputDevice;
        }
    }

    /**
     * Gets information about the input device with the specified descriptor.
     *
     * @param descriptor
     * 		The input device descriptor.
     * @return The input device or null if not found.
     * @unknown 
     */
    public android.view.InputDevice getInputDeviceByDescriptor(java.lang.String descriptor) {
        if (descriptor == null) {
            throw new java.lang.IllegalArgumentException("descriptor must not be null.");
        }
        synchronized(mInputDevicesLock) {
            populateInputDevicesLocked();
            int numDevices = mInputDevices.size();
            for (int i = 0; i < numDevices; i++) {
                android.view.InputDevice inputDevice = mInputDevices.valueAt(i);
                if (inputDevice == null) {
                    int id = mInputDevices.keyAt(i);
                    try {
                        inputDevice = mIm.getInputDevice(id);
                    } catch (android.os.RemoteException ex) {
                        throw ex.rethrowFromSystemServer();
                    }
                    if (inputDevice == null) {
                        continue;
                    }
                    mInputDevices.setValueAt(i, inputDevice);
                }
                if (descriptor.equals(inputDevice.getDescriptor())) {
                    return inputDevice;
                }
            }
            return null;
        }
    }

    /**
     * Gets the ids of all input devices in the system.
     *
     * @return The input device ids.
     */
    public int[] getInputDeviceIds() {
        synchronized(mInputDevicesLock) {
            populateInputDevicesLocked();
            final int count = mInputDevices.size();
            final int[] ids = new int[count];
            for (int i = 0; i < count; i++) {
                ids[i] = mInputDevices.keyAt(i);
            }
            return ids;
        }
    }

    /**
     * Registers an input device listener to receive notifications about when
     * input devices are added, removed or changed.
     *
     * @param listener
     * 		The listener to register.
     * @param handler
     * 		The handler on which the listener should be invoked, or null
     * 		if the listener should be invoked on the calling thread's looper.
     * @see #unregisterInputDeviceListener
     */
    public void registerInputDeviceListener(android.hardware.input.InputManager.InputDeviceListener listener, android.os.Handler handler) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        synchronized(mInputDevicesLock) {
            populateInputDevicesLocked();
            int index = findInputDeviceListenerLocked(listener);
            if (index < 0) {
                mInputDeviceListeners.add(new android.hardware.input.InputManager.InputDeviceListenerDelegate(listener, handler));
            }
        }
    }

    /**
     * Unregisters an input device listener.
     *
     * @param listener
     * 		The listener to unregister.
     * @see #registerInputDeviceListener
     */
    public void unregisterInputDeviceListener(android.hardware.input.InputManager.InputDeviceListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        synchronized(mInputDevicesLock) {
            int index = findInputDeviceListenerLocked(listener);
            if (index >= 0) {
                android.hardware.input.InputManager.InputDeviceListenerDelegate d = mInputDeviceListeners.get(index);
                d.removeCallbacksAndMessages(null);
                mInputDeviceListeners.remove(index);
            }
        }
    }

    private int findInputDeviceListenerLocked(android.hardware.input.InputManager.InputDeviceListener listener) {
        final int numListeners = mInputDeviceListeners.size();
        for (int i = 0; i < numListeners; i++) {
            if (mInputDeviceListeners.get(i).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Queries whether the device is in tablet mode.
     *
     * @return The tablet switch state which is one of {@link #SWITCH_STATE_UNKNOWN},
    {@link #SWITCH_STATE_OFF} or {@link #SWITCH_STATE_ON}.
     * @unknown 
     */
    @android.hardware.input.InputManager.SwitchState
    public int isInTabletMode() {
        try {
            return mIm.isInTabletMode();
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Register a tablet mode changed listener.
     *
     * @param listener
     * 		The listener to register.
     * @param handler
     * 		The handler on which the listener should be invoked, or null
     * 		if the listener should be invoked on the calling thread's looper.
     * @unknown 
     */
    public void registerOnTabletModeChangedListener(android.hardware.input.InputManager.OnTabletModeChangedListener listener, android.os.Handler handler) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        synchronized(mTabletModeLock) {
            if (mOnTabletModeChangedListeners == null) {
                initializeTabletModeListenerLocked();
            }
            int idx = findOnTabletModeChangedListenerLocked(listener);
            if (idx < 0) {
                android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate d = new android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate(listener, handler);
                mOnTabletModeChangedListeners.add(d);
            }
        }
    }

    /**
     * Unregister a tablet mode changed listener.
     *
     * @param listener
     * 		The listener to unregister.
     * @unknown 
     */
    public void unregisterOnTabletModeChangedListener(android.hardware.input.InputManager.OnTabletModeChangedListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        synchronized(mTabletModeLock) {
            int idx = findOnTabletModeChangedListenerLocked(listener);
            if (idx >= 0) {
                android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate d = mOnTabletModeChangedListeners.remove(idx);
                d.removeCallbacksAndMessages(null);
            }
        }
    }

    private void initializeTabletModeListenerLocked() {
        final android.hardware.input.InputManager.TabletModeChangedListener listener = new android.hardware.input.InputManager.TabletModeChangedListener();
        try {
            mIm.registerTabletModeChangedListener(listener);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
        mTabletModeChangedListener = listener;
        mOnTabletModeChangedListeners = new java.util.ArrayList<>();
    }

    private int findOnTabletModeChangedListenerLocked(android.hardware.input.InputManager.OnTabletModeChangedListener listener) {
        final int N = mOnTabletModeChangedListeners.size();
        for (int i = 0; i < N; i++) {
            if (mOnTabletModeChangedListeners.get(i).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets information about all supported keyboard layouts.
     * <p>
     * The input manager consults the built-in keyboard layouts as well
     * as all keyboard layouts advertised by applications using a
     * {@link #ACTION_QUERY_KEYBOARD_LAYOUTS} broadcast receiver.
     * </p>
     *
     * @return A list of all supported keyboard layouts.
     * @unknown 
     */
    public android.hardware.input.KeyboardLayout[] getKeyboardLayouts() {
        try {
            return mIm.getKeyboardLayouts();
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets information about all supported keyboard layouts appropriate
     * for a specific input device.
     * <p>
     * The input manager consults the built-in keyboard layouts as well
     * as all keyboard layouts advertised by applications using a
     * {@link #ACTION_QUERY_KEYBOARD_LAYOUTS} broadcast receiver.
     * </p>
     *
     * @return A list of all supported keyboard layouts for a specific
    input device.
     * @unknown 
     */
    public android.hardware.input.KeyboardLayout[] getKeyboardLayoutsForInputDevice(android.hardware.input.InputDeviceIdentifier identifier) {
        try {
            return mIm.getKeyboardLayoutsForInputDevice(identifier);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the keyboard layout with the specified descriptor.
     *
     * @param keyboardLayoutDescriptor
     * 		The keyboard layout descriptor, as returned by
     * 		{@link KeyboardLayout#getDescriptor()}.
     * @return The keyboard layout, or null if it could not be loaded.
     * @unknown 
     */
    public android.hardware.input.KeyboardLayout getKeyboardLayout(java.lang.String keyboardLayoutDescriptor) {
        if (keyboardLayoutDescriptor == null) {
            throw new java.lang.IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        }
        try {
            return mIm.getKeyboardLayout(keyboardLayoutDescriptor);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the current keyboard layout descriptor for the specified input
     * device.
     *
     * @param identifier
     * 		Identifier for the input device
     * @return The keyboard layout descriptor, or null if no keyboard layout has
    been set.
     * @unknown 
     */
    public java.lang.String getCurrentKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier) {
        try {
            return mIm.getCurrentKeyboardLayoutForInputDevice(identifier);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the current keyboard layout descriptor for the specified input
     * device.
     * <p>
     * This method may have the side-effect of causing the input device in
     * question to be reconfigured.
     * </p>
     *
     * @param identifier
     * 		The identifier for the input device.
     * @param keyboardLayoutDescriptor
     * 		The keyboard layout descriptor to use,
     * 		must not be null.
     * @unknown 
     */
    public void setCurrentKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, java.lang.String keyboardLayoutDescriptor) {
        if (identifier == null) {
            throw new java.lang.IllegalArgumentException("identifier must not be null");
        }
        if (keyboardLayoutDescriptor == null) {
            throw new java.lang.IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        }
        try {
            mIm.setCurrentKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets all keyboard layout descriptors that are enabled for the specified
     * input device.
     *
     * @param identifier
     * 		The identifier for the input device.
     * @return The keyboard layout descriptors.
     * @unknown 
     */
    public java.lang.String[] getEnabledKeyboardLayoutsForInputDevice(android.hardware.input.InputDeviceIdentifier identifier) {
        if (identifier == null) {
            throw new java.lang.IllegalArgumentException("inputDeviceDescriptor must not be null");
        }
        try {
            return mIm.getEnabledKeyboardLayoutsForInputDevice(identifier);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Adds the keyboard layout descriptor for the specified input device.
     * <p>
     * This method may have the side-effect of causing the input device in
     * question to be reconfigured.
     * </p>
     *
     * @param identifier
     * 		The identifier for the input device.
     * @param keyboardLayoutDescriptor
     * 		The descriptor of the keyboard layout to
     * 		add.
     * @unknown 
     */
    public void addKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, java.lang.String keyboardLayoutDescriptor) {
        if (identifier == null) {
            throw new java.lang.IllegalArgumentException("inputDeviceDescriptor must not be null");
        }
        if (keyboardLayoutDescriptor == null) {
            throw new java.lang.IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        }
        try {
            mIm.addKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Removes the keyboard layout descriptor for the specified input device.
     * <p>
     * This method may have the side-effect of causing the input device in
     * question to be reconfigured.
     * </p>
     *
     * @param identifier
     * 		The identifier for the input device.
     * @param keyboardLayoutDescriptor
     * 		The descriptor of the keyboard layout to
     * 		remove.
     * @unknown 
     */
    public void removeKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, java.lang.String keyboardLayoutDescriptor) {
        if (identifier == null) {
            throw new java.lang.IllegalArgumentException("inputDeviceDescriptor must not be null");
        }
        if (keyboardLayoutDescriptor == null) {
            throw new java.lang.IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        }
        try {
            mIm.removeKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the keyboard layout for the specified input device and IME subtype.
     *
     * @param identifier
     * 		The identifier for the input device.
     * @param inputMethodInfo
     * 		The input method.
     * @param inputMethodSubtype
     * 		The input method subtype. {@code null} if this input method does
     * 		not support any subtype.
     * @return The associated {@link KeyboardLayout}, or null if one has not been set.
     * @unknown 
     */
    @android.annotation.Nullable
    public android.hardware.input.KeyboardLayout getKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, android.view.inputmethod.InputMethodInfo inputMethodInfo, @android.annotation.Nullable
    android.view.inputmethod.InputMethodSubtype inputMethodSubtype) {
        try {
            return mIm.getKeyboardLayoutForInputDevice(identifier, inputMethodInfo, inputMethodSubtype);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the keyboard layout for the specified input device and IME subtype pair.
     *
     * @param identifier
     * 		The identifier for the input device.
     * @param inputMethodInfo
     * 		The input method with which to associate the keyboard layout.
     * @param inputMethodSubtype
     * 		The input method subtype which which to associate the keyboard
     * 		layout. {@code null} if this input method does not support any subtype.
     * @param keyboardLayoutDescriptor
     * 		The descriptor of the keyboard layout to set
     * @unknown 
     */
    public void setKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, android.view.inputmethod.InputMethodInfo inputMethodInfo, @android.annotation.Nullable
    android.view.inputmethod.InputMethodSubtype inputMethodSubtype, java.lang.String keyboardLayoutDescriptor) {
        try {
            mIm.setKeyboardLayoutForInputDevice(identifier, inputMethodInfo, inputMethodSubtype, keyboardLayoutDescriptor);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the TouchCalibration applied to the specified input device's coordinates.
     *
     * @param inputDeviceDescriptor
     * 		The input device descriptor.
     * @return The TouchCalibration currently assigned for use with the given
    input device. If none is set, an identity TouchCalibration is returned.
     * @unknown 
     */
    public android.hardware.input.TouchCalibration getTouchCalibration(java.lang.String inputDeviceDescriptor, int surfaceRotation) {
        try {
            return mIm.getTouchCalibrationForInputDevice(inputDeviceDescriptor, surfaceRotation);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the TouchCalibration to apply to the specified input device's coordinates.
     * <p>
     * This method may have the side-effect of causing the input device in question
     * to be reconfigured. Requires {@link android.Manifest.permissions.SET_INPUT_CALIBRATION}.
     * </p>
     *
     * @param inputDeviceDescriptor
     * 		The input device descriptor.
     * @param calibration
     * 		The calibration to be applied
     * @unknown 
     */
    public void setTouchCalibration(java.lang.String inputDeviceDescriptor, int surfaceRotation, android.hardware.input.TouchCalibration calibration) {
        try {
            mIm.setTouchCalibrationForInputDevice(inputDeviceDescriptor, surfaceRotation, calibration);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the mouse pointer speed.
     * <p>
     * Only returns the permanent mouse pointer speed.  Ignores any temporary pointer
     * speed set by {@link #tryPointerSpeed}.
     * </p>
     *
     * @param context
     * 		The application context.
     * @return The pointer speed as a value between {@link #MIN_POINTER_SPEED} and
    {@link #MAX_POINTER_SPEED}, or the default value {@link #DEFAULT_POINTER_SPEED}.
     * @unknown 
     */
    public int getPointerSpeed(android.content.Context context) {
        int speed = android.hardware.input.InputManager.DEFAULT_POINTER_SPEED;
        try {
            speed = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.POINTER_SPEED);
        } catch (android.provider.Settings.SettingNotFoundException snfe) {
        }
        return speed;
    }

    /**
     * Sets the mouse pointer speed.
     * <p>
     * Requires {@link android.Manifest.permissions.WRITE_SETTINGS}.
     * </p>
     *
     * @param context
     * 		The application context.
     * @param speed
     * 		The pointer speed as a value between {@link #MIN_POINTER_SPEED} and
     * 		{@link #MAX_POINTER_SPEED}, or the default value {@link #DEFAULT_POINTER_SPEED}.
     * @unknown 
     */
    public void setPointerSpeed(android.content.Context context, int speed) {
        if ((speed < android.hardware.input.InputManager.MIN_POINTER_SPEED) || (speed > android.hardware.input.InputManager.MAX_POINTER_SPEED)) {
            throw new java.lang.IllegalArgumentException("speed out of range");
        }
        android.provider.Settings.System.putInt(context.getContentResolver(), android.provider.Settings.System.POINTER_SPEED, speed);
    }

    /**
     * Changes the mouse pointer speed temporarily, but does not save the setting.
     * <p>
     * Requires {@link android.Manifest.permission.SET_POINTER_SPEED}.
     * </p>
     *
     * @param speed
     * 		The pointer speed as a value between {@link #MIN_POINTER_SPEED} and
     * 		{@link #MAX_POINTER_SPEED}, or the default value {@link #DEFAULT_POINTER_SPEED}.
     * @unknown 
     */
    public void tryPointerSpeed(int speed) {
        if ((speed < android.hardware.input.InputManager.MIN_POINTER_SPEED) || (speed > android.hardware.input.InputManager.MAX_POINTER_SPEED)) {
            throw new java.lang.IllegalArgumentException("speed out of range");
        }
        try {
            mIm.tryPointerSpeed(speed);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Queries the framework about whether any physical keys exist on the
     * any keyboard attached to the device that are capable of producing the given
     * array of key codes.
     *
     * @param keyCodes
     * 		The array of key codes to query.
     * @return A new array of the same size as the key codes array whose elements
    are set to true if at least one attached keyboard supports the corresponding key code
    at the same index in the key codes array.
     * @unknown 
     */
    public boolean[] deviceHasKeys(int[] keyCodes) {
        return deviceHasKeys(-1, keyCodes);
    }

    /**
     * Queries the framework about whether any physical keys exist on the
     * any keyboard attached to the device that are capable of producing the given
     * array of key codes.
     *
     * @param id
     * 		The id of the device to query.
     * @param keyCodes
     * 		The array of key codes to query.
     * @return A new array of the same size as the key codes array whose elements are set to true
    if the given device could produce the corresponding key code at the same index in the key
    codes array.
     * @unknown 
     */
    public boolean[] deviceHasKeys(int id, int[] keyCodes) {
        boolean[] ret = new boolean[keyCodes.length];
        try {
            mIm.hasKeys(id, android.view.InputDevice.SOURCE_ANY, keyCodes, ret);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        return ret;
    }

    /**
     * Injects an input event into the event system on behalf of an application.
     * The synchronization mode determines whether the method blocks while waiting for
     * input injection to proceed.
     * <p>
     * Requires {@link android.Manifest.permission.INJECT_EVENTS} to inject into
     * windows that are owned by other applications.
     * </p><p>
     * Make sure you correctly set the event time and input source of the event
     * before calling this method.
     * </p>
     *
     * @param event
     * 		The event to inject.
     * @param mode
     * 		The synchronization mode.  One of:
     * 		{@link #INJECT_INPUT_EVENT_MODE_ASYNC},
     * 		{@link #INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT}, or
     * 		{@link #INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH}.
     * @return True if input event injection succeeded.
     * @unknown 
     */
    public boolean injectInputEvent(android.view.InputEvent event, int mode) {
        if (event == null) {
            throw new java.lang.IllegalArgumentException("event must not be null");
        }
        if (((mode != android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_ASYNC) && (mode != android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH)) && (mode != android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT)) {
            throw new java.lang.IllegalArgumentException("mode is invalid");
        }
        try {
            return mIm.injectInputEvent(event, mode);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Changes the mouse pointer's icon shape into the specified id.
     *
     * @param iconId
     * 		The id of the pointer graphic, as a value between
     * 		{@link PointerIcon.TYPE_ARROW} and {@link PointerIcon.TYPE_GRABBING}.
     * @unknown 
     */
    public void setPointerIconType(int iconId) {
        try {
            mIm.setPointerIconType(iconId);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCustomPointerIcon(android.view.PointerIcon icon) {
        try {
            mIm.setCustomPointerIcon(icon);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private void populateInputDevicesLocked() {
        if (mInputDevicesChangedListener == null) {
            final android.hardware.input.InputManager.InputDevicesChangedListener listener = new android.hardware.input.InputManager.InputDevicesChangedListener();
            try {
                mIm.registerInputDevicesChangedListener(listener);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
            mInputDevicesChangedListener = listener;
        }
        if (mInputDevices == null) {
            final int[] ids;
            try {
                ids = mIm.getInputDeviceIds();
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
            mInputDevices = new android.util.SparseArray<android.view.InputDevice>();
            for (int i = 0; i < ids.length; i++) {
                mInputDevices.put(ids[i], null);
            }
        }
    }

    private void onInputDevicesChanged(int[] deviceIdAndGeneration) {
        if (android.hardware.input.InputManager.DEBUG) {
            android.util.Log.d(android.hardware.input.InputManager.TAG, "Received input devices changed.");
        }
        synchronized(mInputDevicesLock) {
            for (int i = mInputDevices.size(); (--i) > 0;) {
                final int deviceId = mInputDevices.keyAt(i);
                if (!android.hardware.input.InputManager.containsDeviceId(deviceIdAndGeneration, deviceId)) {
                    if (android.hardware.input.InputManager.DEBUG) {
                        android.util.Log.d(android.hardware.input.InputManager.TAG, "Device removed: " + deviceId);
                    }
                    mInputDevices.removeAt(i);
                    sendMessageToInputDeviceListenersLocked(android.hardware.input.InputManager.MSG_DEVICE_REMOVED, deviceId);
                }
            }
            for (int i = 0; i < deviceIdAndGeneration.length; i += 2) {
                final int deviceId = deviceIdAndGeneration[i];
                int index = mInputDevices.indexOfKey(deviceId);
                if (index >= 0) {
                    final android.view.InputDevice device = mInputDevices.valueAt(index);
                    if (device != null) {
                        final int generation = deviceIdAndGeneration[i + 1];
                        if (device.getGeneration() != generation) {
                            if (android.hardware.input.InputManager.DEBUG) {
                                android.util.Log.d(android.hardware.input.InputManager.TAG, "Device changed: " + deviceId);
                            }
                            mInputDevices.setValueAt(index, null);
                            sendMessageToInputDeviceListenersLocked(android.hardware.input.InputManager.MSG_DEVICE_CHANGED, deviceId);
                        }
                    }
                } else {
                    if (android.hardware.input.InputManager.DEBUG) {
                        android.util.Log.d(android.hardware.input.InputManager.TAG, "Device added: " + deviceId);
                    }
                    mInputDevices.put(deviceId, null);
                    sendMessageToInputDeviceListenersLocked(android.hardware.input.InputManager.MSG_DEVICE_ADDED, deviceId);
                }
            }
        }
    }

    private void sendMessageToInputDeviceListenersLocked(int what, int deviceId) {
        final int numListeners = mInputDeviceListeners.size();
        for (int i = 0; i < numListeners; i++) {
            android.hardware.input.InputManager.InputDeviceListenerDelegate listener = mInputDeviceListeners.get(i);
            listener.sendMessage(listener.obtainMessage(what, deviceId, 0));
        }
    }

    private static boolean containsDeviceId(int[] deviceIdAndGeneration, int deviceId) {
        for (int i = 0; i < deviceIdAndGeneration.length; i += 2) {
            if (deviceIdAndGeneration[i] == deviceId) {
                return true;
            }
        }
        return false;
    }

    private void onTabletModeChanged(long whenNanos, boolean inTabletMode) {
        if (android.hardware.input.InputManager.DEBUG) {
            android.util.Log.d(android.hardware.input.InputManager.TAG, ((("Received tablet mode changed: " + "whenNanos=") + whenNanos) + ", inTabletMode=") + inTabletMode);
        }
        synchronized(mTabletModeLock) {
            final int N = mOnTabletModeChangedListeners.size();
            for (int i = 0; i < N; i++) {
                android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate listener = mOnTabletModeChangedListeners.get(i);
                listener.sendTabletModeChanged(whenNanos, inTabletMode);
            }
        }
    }

    /**
     * Gets a vibrator service associated with an input device, assuming it has one.
     *
     * @return The vibrator, never null.
     * @unknown 
     */
    public android.os.Vibrator getInputDeviceVibrator(int deviceId) {
        return new android.hardware.input.InputManager.InputDeviceVibrator(deviceId);
    }

    /**
     * Listens for changes in input devices.
     */
    public interface InputDeviceListener {
        /**
         * Called whenever an input device has been added to the system.
         * Use {@link InputManager#getInputDevice} to get more information about the device.
         *
         * @param deviceId
         * 		The id of the input device that was added.
         */
        void onInputDeviceAdded(int deviceId);

        /**
         * Called whenever an input device has been removed from the system.
         *
         * @param deviceId
         * 		The id of the input device that was removed.
         */
        void onInputDeviceRemoved(int deviceId);

        /**
         * Called whenever the properties of an input device have changed since they
         * were last queried.  Use {@link InputManager#getInputDevice} to get
         * a fresh {@link InputDevice} object with the new properties.
         *
         * @param deviceId
         * 		The id of the input device that changed.
         */
        void onInputDeviceChanged(int deviceId);
    }

    private final class InputDevicesChangedListener extends android.hardware.input.IInputDevicesChangedListener.Stub {
        @java.lang.Override
        public void onInputDevicesChanged(int[] deviceIdAndGeneration) throws android.os.RemoteException {
            android.hardware.input.InputManager.this.onInputDevicesChanged(deviceIdAndGeneration);
        }
    }

    private static final class InputDeviceListenerDelegate extends android.os.Handler {
        public final android.hardware.input.InputManager.InputDeviceListener mListener;

        public InputDeviceListenerDelegate(android.hardware.input.InputManager.InputDeviceListener listener, android.os.Handler handler) {
            super(handler != null ? handler.getLooper() : android.os.Looper.myLooper());
            mListener = listener;
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.hardware.input.InputManager.MSG_DEVICE_ADDED :
                    mListener.onInputDeviceAdded(msg.arg1);
                    break;
                case android.hardware.input.InputManager.MSG_DEVICE_REMOVED :
                    mListener.onInputDeviceRemoved(msg.arg1);
                    break;
                case android.hardware.input.InputManager.MSG_DEVICE_CHANGED :
                    mListener.onInputDeviceChanged(msg.arg1);
                    break;
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public interface OnTabletModeChangedListener {
        /**
         * Called whenever the device goes into or comes out of tablet mode.
         *
         * @param whenNanos
         * 		The time at which the device transitioned into or
         * 		out of tablet mode. This is given in nanoseconds in the
         * 		{@link SystemClock#uptimeMillis} time base.
         */
        void onTabletModeChanged(long whenNanos, boolean inTabletMode);
    }

    private final class TabletModeChangedListener extends android.hardware.input.ITabletModeChangedListener.Stub {
        @java.lang.Override
        public void onTabletModeChanged(long whenNanos, boolean inTabletMode) {
            android.hardware.input.InputManager.this.onTabletModeChanged(whenNanos, inTabletMode);
        }
    }

    private static final class OnTabletModeChangedListenerDelegate extends android.os.Handler {
        private static final int MSG_TABLET_MODE_CHANGED = 0;

        public final android.hardware.input.InputManager.OnTabletModeChangedListener mListener;

        public OnTabletModeChangedListenerDelegate(android.hardware.input.InputManager.OnTabletModeChangedListener listener, android.os.Handler handler) {
            super(handler != null ? handler.getLooper() : android.os.Looper.myLooper());
            mListener = listener;
        }

        public void sendTabletModeChanged(long whenNanos, boolean inTabletMode) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = ((int) (whenNanos & 0xffffffff));
            args.argi2 = ((int) (whenNanos >> 32));
            args.arg1 = ((java.lang.Boolean) (inTabletMode));
            obtainMessage(android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate.MSG_TABLET_MODE_CHANGED, args).sendToTarget();
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.hardware.input.InputManager.OnTabletModeChangedListenerDelegate.MSG_TABLET_MODE_CHANGED :
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    long whenNanos = (args.argi1 & 0xffffffffL) | (((long) (args.argi2)) << 32);
                    boolean inTabletMode = ((boolean) (args.arg1));
                    mListener.onTabletModeChanged(whenNanos, inTabletMode);
                    break;
            }
        }
    }

    private final class InputDeviceVibrator extends android.os.Vibrator {
        private final int mDeviceId;

        private final android.os.Binder mToken;

        public InputDeviceVibrator(int deviceId) {
            mDeviceId = deviceId;
            mToken = new android.os.Binder();
        }

        @java.lang.Override
        public boolean hasVibrator() {
            return true;
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void vibrate(int uid, java.lang.String opPkg, long milliseconds, android.media.AudioAttributes attributes) {
            vibrate(new long[]{ 0, milliseconds }, -1);
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void vibrate(int uid, java.lang.String opPkg, long[] pattern, int repeat, android.media.AudioAttributes attributes) {
            if (repeat >= pattern.length) {
                throw new java.lang.ArrayIndexOutOfBoundsException();
            }
            try {
                mIm.vibrate(mDeviceId, pattern, repeat, mToken);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }

        @java.lang.Override
        public void cancel() {
            try {
                mIm.cancelVibrate(mDeviceId, mToken);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }
}

