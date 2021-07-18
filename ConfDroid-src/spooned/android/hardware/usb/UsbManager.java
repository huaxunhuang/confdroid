/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.hardware.usb;


/**
 * This class allows you to access the state of USB and communicate with USB devices.
 * Currently only host mode is supported in the public API.
 *
 * <p>You can obtain an instance of this class by calling
 * {@link android.content.Context#getSystemService(java.lang.String) Context.getSystemService()}.
 *
 * {@samplecode UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);}
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about communicating with USB hardware, read the
 * <a href="{@docRoot }guide/topics/connectivity/usb/index.html">USB developer guide</a>.</p>
 * </div>
 */
public class UsbManager {
    private static final java.lang.String TAG = "UsbManager";

    /**
     * Broadcast Action:  A sticky broadcast for USB state change events when in device mode.
     *
     * This is a sticky broadcast for clients that includes USB connected/disconnected state,
     * <ul>
     * <li> {@link #USB_CONNECTED} boolean indicating whether USB is connected or disconnected.
     * <li> {@link #USB_HOST_CONNECTED} boolean indicating whether USB is connected or
     *     disconnected as host.
     * <li> {@link #USB_CONFIGURED} boolean indicating whether USB is configured.
     * currently zero if not configured, one for configured.
     * <li> {@link #USB_FUNCTION_ADB} boolean extra indicating whether the
     * adb function is enabled
     * <li> {@link #USB_FUNCTION_RNDIS} boolean extra indicating whether the
     * RNDIS ethernet function is enabled
     * <li> {@link #USB_FUNCTION_MTP} boolean extra indicating whether the
     * MTP function is enabled
     * <li> {@link #USB_FUNCTION_PTP} boolean extra indicating whether the
     * PTP function is enabled
     * <li> {@link #USB_FUNCTION_PTP} boolean extra indicating whether the
     * accessory function is enabled
     * <li> {@link #USB_FUNCTION_AUDIO_SOURCE} boolean extra indicating whether the
     * audio source function is enabled
     * <li> {@link #USB_FUNCTION_MIDI} boolean extra indicating whether the
     * MIDI function is enabled
     * </ul>
     * If the sticky intent has not been found, that indicates USB is disconnected,
     * USB is not configued, MTP function is enabled, and all the other functions are disabled.
     *
     * {@hide }
     */
    public static final java.lang.String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";

    /**
     * Broadcast Action: A broadcast for USB port changes.
     *
     * This intent is sent when a USB port is added, removed, or changes state.
     * <ul>
     * <li> {@link #EXTRA_PORT} containing the {@link android.hardware.usb.UsbPort}
     * for the port.
     * <li> {@link #EXTRA_PORT_STATUS} containing the {@link android.hardware.usb.UsbPortStatus}
     * for the port, or null if the port has been removed
     * </ul>
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_USB_PORT_CHANGED = "android.hardware.usb.action.USB_PORT_CHANGED";

    /**
     * Broadcast Action:  A broadcast for USB device attached event.
     *
     * This intent is sent when a USB device is attached to the USB bus when in host mode.
     * <ul>
     * <li> {@link #EXTRA_DEVICE} containing the {@link android.hardware.usb.UsbDevice}
     * for the attached device
     * </ul>
     */
    public static final java.lang.String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";

    /**
     * Broadcast Action:  A broadcast for USB device detached event.
     *
     * This intent is sent when a USB device is detached from the USB bus when in host mode.
     * <ul>
     * <li> {@link #EXTRA_DEVICE} containing the {@link android.hardware.usb.UsbDevice}
     * for the detached device
     * </ul>
     */
    public static final java.lang.String ACTION_USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    /**
     * Broadcast Action:  A broadcast for USB accessory attached event.
     *
     * This intent is sent when a USB accessory is attached.
     * <ul>
     * <li> {@link #EXTRA_ACCESSORY} containing the {@link android.hardware.usb.UsbAccessory}
     * for the attached accessory
     * </ul>
     */
    public static final java.lang.String ACTION_USB_ACCESSORY_ATTACHED = "android.hardware.usb.action.USB_ACCESSORY_ATTACHED";

    /**
     * Broadcast Action:  A broadcast for USB accessory detached event.
     *
     * This intent is sent when a USB accessory is detached.
     * <ul>
     * <li> {@link #EXTRA_ACCESSORY} containing the {@link UsbAccessory}
     * for the attached accessory that was detached
     * </ul>
     */
    public static final java.lang.String ACTION_USB_ACCESSORY_DETACHED = "android.hardware.usb.action.USB_ACCESSORY_DETACHED";

    /**
     * Boolean extra indicating whether USB is connected or disconnected.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
     *
     * {@hide }
     */
    public static final java.lang.String USB_CONNECTED = "connected";

    /**
     * Boolean extra indicating whether USB is connected or disconnected as host.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
     *
     * {@hide }
     */
    public static final java.lang.String USB_HOST_CONNECTED = "host_connected";

    /**
     * Boolean extra indicating whether USB is configured.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
     *
     * {@hide }
     */
    public static final java.lang.String USB_CONFIGURED = "configured";

    /**
     * Boolean extra indicating whether confidential user data, such as photos, should be
     * made available on the USB connection. This variable will only be set when the user
     * has explicitly asked for this data to be unlocked.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
     *
     * {@hide }
     */
    public static final java.lang.String USB_DATA_UNLOCKED = "unlocked";

    /**
     * A placeholder indicating that no USB function is being specified.
     * Used to distinguish between selecting no function vs. the default function in
     * {@link #setCurrentFunction(String)}.
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_NONE = "none";

    /**
     * Name of the adb USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_ADB = "adb";

    /**
     * Name of the RNDIS ethernet USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_RNDIS = "rndis";

    /**
     * Name of the MTP USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_MTP = "mtp";

    /**
     * Name of the PTP USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_PTP = "ptp";

    /**
     * Name of the audio source USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_AUDIO_SOURCE = "audio_source";

    /**
     * Name of the MIDI USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_MIDI = "midi";

    /**
     * Name of the Accessory USB function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     *
     * {@hide }
     */
    public static final java.lang.String USB_FUNCTION_ACCESSORY = "accessory";

    /**
     * Name of extra for {@link #ACTION_USB_PORT_CHANGED}
     * containing the {@link UsbPort} object for the port.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_PORT = "port";

    /**
     * Name of extra for {@link #ACTION_USB_PORT_CHANGED}
     * containing the {@link UsbPortStatus} object for the port, or null if the port
     * was removed.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_PORT_STATUS = "portStatus";

    /**
     * Name of extra for {@link #ACTION_USB_DEVICE_ATTACHED} and
     * {@link #ACTION_USB_DEVICE_DETACHED} broadcasts
     * containing the {@link UsbDevice} object for the device.
     */
    public static final java.lang.String EXTRA_DEVICE = "device";

    /**
     * Name of extra for {@link #ACTION_USB_ACCESSORY_ATTACHED} and
     * {@link #ACTION_USB_ACCESSORY_DETACHED} broadcasts
     * containing the {@link UsbAccessory} object for the accessory.
     */
    public static final java.lang.String EXTRA_ACCESSORY = "accessory";

    /**
     * Name of extra added to the {@link android.app.PendingIntent}
     * passed into {@link #requestPermission(UsbDevice, PendingIntent)}
     * or {@link #requestPermission(UsbAccessory, PendingIntent)}
     * containing a boolean value indicating whether the user granted permission or not.
     */
    public static final java.lang.String EXTRA_PERMISSION_GRANTED = "permission";

    private final android.content.Context mContext;

    private final android.hardware.usb.IUsbManager mService;

    /**
     * {@hide }
     */
    public UsbManager(android.content.Context context, android.hardware.usb.IUsbManager service) {
        mContext = context;
        mService = service;
    }

    /**
     * Returns a HashMap containing all USB devices currently attached.
     * USB device name is the key for the returned HashMap.
     * The result will be empty if no devices are attached, or if
     * USB host mode is inactive or unsupported.
     *
     * @return HashMap containing all connected USB devices.
     */
    public java.util.HashMap<java.lang.String, android.hardware.usb.UsbDevice> getDeviceList() {
        android.os.Bundle bundle = new android.os.Bundle();
        try {
            mService.getDeviceList(bundle);
            java.util.HashMap<java.lang.String, android.hardware.usb.UsbDevice> result = new java.util.HashMap<java.lang.String, android.hardware.usb.UsbDevice>();
            for (java.lang.String name : bundle.keySet()) {
                result.put(name, ((android.hardware.usb.UsbDevice) (bundle.get(name))));
            }
            return result;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Opens the device so it can be used to send and receive
     * data using {@link android.hardware.usb.UsbRequest}.
     *
     * @param device
     * 		the device to open
     * @return a {@link UsbDeviceConnection}, or {@code null} if open failed
     */
    public android.hardware.usb.UsbDeviceConnection openDevice(android.hardware.usb.UsbDevice device) {
        try {
            java.lang.String deviceName = device.getDeviceName();
            android.os.ParcelFileDescriptor pfd = mService.openDevice(deviceName);
            if (pfd != null) {
                android.hardware.usb.UsbDeviceConnection connection = new android.hardware.usb.UsbDeviceConnection(device);
                boolean result = connection.open(deviceName, pfd, mContext);
                pfd.close();
                if (result) {
                    return connection;
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.hardware.usb.UsbManager.TAG, "exception in UsbManager.openDevice", e);
        }
        return null;
    }

    /**
     * Returns a list of currently attached USB accessories.
     * (in the current implementation there can be at most one)
     *
     * @return list of USB accessories, or null if none are attached.
     */
    public android.hardware.usb.UsbAccessory[] getAccessoryList() {
        try {
            android.hardware.usb.UsbAccessory accessory = mService.getCurrentAccessory();
            if (accessory == null) {
                return null;
            } else {
                return new android.hardware.usb.UsbAccessory[]{ accessory };
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Opens a file descriptor for reading and writing data to the USB accessory.
     *
     * @param accessory
     * 		the USB accessory to open
     * @return file descriptor, or null if the accessor could not be opened.
     */
    public android.os.ParcelFileDescriptor openAccessory(android.hardware.usb.UsbAccessory accessory) {
        try {
            return mService.openAccessory(accessory);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns true if the caller has permission to access the device.
     * Permission might have been granted temporarily via
     * {@link #requestPermission(UsbDevice, PendingIntent)} or
     * by the user choosing the caller as the default application for the device.
     *
     * @param device
     * 		to check permissions for
     * @return true if caller has permission
     */
    public boolean hasPermission(android.hardware.usb.UsbDevice device) {
        try {
            return mService.hasDevicePermission(device);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns true if the caller has permission to access the accessory.
     * Permission might have been granted temporarily via
     * {@link #requestPermission(UsbAccessory, PendingIntent)} or
     * by the user choosing the caller as the default application for the accessory.
     *
     * @param accessory
     * 		to check permissions for
     * @return true if caller has permission
     */
    public boolean hasPermission(android.hardware.usb.UsbAccessory accessory) {
        try {
            return mService.hasAccessoryPermission(accessory);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Requests temporary permission for the given package to access the device.
     * This may result in a system dialog being displayed to the user
     * if permission had not already been granted.
     * Success or failure is returned via the {@link android.app.PendingIntent} pi.
     * If successful, this grants the caller permission to access the device only
     * until the device is disconnected.
     *
     * The following extras will be added to pi:
     * <ul>
     * <li> {@link #EXTRA_DEVICE} containing the device passed into this call
     * <li> {@link #EXTRA_PERMISSION_GRANTED} containing boolean indicating whether
     * permission was granted by the user
     * </ul>
     *
     * @param device
     * 		to request permissions for
     * @param pi
     * 		PendingIntent for returning result
     */
    public void requestPermission(android.hardware.usb.UsbDevice device, android.app.PendingIntent pi) {
        try {
            mService.requestDevicePermission(device, mContext.getPackageName(), pi);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Requests temporary permission for the given package to access the accessory.
     * This may result in a system dialog being displayed to the user
     * if permission had not already been granted.
     * Success or failure is returned via the {@link android.app.PendingIntent} pi.
     * If successful, this grants the caller permission to access the accessory only
     * until the device is disconnected.
     *
     * The following extras will be added to pi:
     * <ul>
     * <li> {@link #EXTRA_ACCESSORY} containing the accessory passed into this call
     * <li> {@link #EXTRA_PERMISSION_GRANTED} containing boolean indicating whether
     * permission was granted by the user
     * </ul>
     *
     * @param accessory
     * 		to request permissions for
     * @param pi
     * 		PendingIntent for returning result
     */
    public void requestPermission(android.hardware.usb.UsbAccessory accessory, android.app.PendingIntent pi) {
        try {
            mService.requestAccessoryPermission(accessory, mContext.getPackageName(), pi);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Grants permission for USB device without showing system dialog.
     * Only system components can call this function.
     *
     * @param device
     * 		to request permissions for
     * 		
     * 		{@hide }
     */
    public void grantPermission(android.hardware.usb.UsbDevice device) {
        try {
            mService.grantDevicePermission(device, android.os.Process.myUid());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Grants permission to specified package for USB device without showing system dialog.
     * Only system components can call this function, as it requires the MANAGE_USB permission.
     *
     * @param device
     * 		to request permissions for
     * @param packageName
     * 		of package to grant permissions
     * 		
     * 		{@hide }
     */
    public void grantPermission(android.hardware.usb.UsbDevice device, java.lang.String packageName) {
        try {
            int uid = mContext.getPackageManager().getPackageUidAsUser(packageName, mContext.getUserId());
            mService.grantDevicePermission(device, uid);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(android.hardware.usb.UsbManager.TAG, ("Package " + packageName) + " not found.", e);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns true if the specified USB function is currently enabled when in device mode.
     * <p>
     * USB functions represent interfaces which are published to the host to access
     * services offered by the device.
     * </p>
     *
     * @param function
     * 		name of the USB function
     * @return true if the USB function is enabled

    {@hide }
     */
    public boolean isFunctionEnabled(java.lang.String function) {
        try {
            return mService.isFunctionEnabled(function);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the current USB function when in device mode.
     * <p>
     * USB functions represent interfaces which are published to the host to access
     * services offered by the device.
     * </p><p>
     * This method is intended to select among primary USB functions.  The system may
     * automatically activate additional functions such as {@link #USB_FUNCTION_ADB}
     * or {@link #USB_FUNCTION_ACCESSORY} based on other settings and states.
     * </p><p>
     * The allowed values are: {@link #USB_FUNCTION_NONE}, {@link #USB_FUNCTION_AUDIO_SOURCE},
     * {@link #USB_FUNCTION_MIDI}, {@link #USB_FUNCTION_MTP}, {@link #USB_FUNCTION_PTP},
     * or {@link #USB_FUNCTION_RNDIS}.
     * </p><p>
     * Note: This function is asynchronous and may fail silently without applying
     * the requested changes.
     * </p>
     *
     * @param function
     * 		name of the USB function, or null to restore the default function
     * 		
     * 		{@hide }
     */
    public void setCurrentFunction(java.lang.String function) {
        try {
            mService.setCurrentFunction(function);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets whether USB data (for example, MTP exposed pictures) should be made available
     * on the USB connection when in device mode. Unlocking usb data should only be done with
     * user involvement, since exposing pictures or other data could leak sensitive
     * user information.
     *
     * {@hide }
     */
    public void setUsbDataUnlocked(boolean unlocked) {
        try {
            mService.setUsbDataUnlocked(unlocked);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a list of physical USB ports on the device.
     * <p>
     * This list is guaranteed to contain all dual-role USB Type C ports but it might
     * be missing other ports depending on whether the kernel USB drivers have been
     * updated to publish all of the device's ports through the new "dual_role_usb"
     * device class (which supports all types of ports despite its name).
     * </p>
     *
     * @return The list of USB ports, or null if none.
     * @unknown 
     */
    public android.hardware.usb.UsbPort[] getPorts() {
        try {
            return mService.getPorts();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the status of the specified USB port.
     *
     * @param port
     * 		The port to query.
     * @return The status of the specified USB port, or null if unknown.
     * @unknown 
     */
    public android.hardware.usb.UsbPortStatus getPortStatus(android.hardware.usb.UsbPort port) {
        com.android.internal.util.Preconditions.checkNotNull(port, "port must not be null");
        try {
            return mService.getPortStatus(port.getId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the desired role combination of the port.
     * <p>
     * The supported role combinations depend on what is connected to the port and may be
     * determined by consulting
     * {@link UsbPortStatus#isRoleCombinationSupported UsbPortStatus.isRoleCombinationSupported}.
     * </p><p>
     * Note: This function is asynchronous and may fail silently without applying
     * the requested changes.  If this function does cause a status change to occur then
     * a {@link #ACTION_USB_PORT_CHANGED} broadcast will be sent.
     * </p>
     *
     * @param powerRole
     * 		The desired power role: {@link UsbPort#POWER_ROLE_SOURCE}
     * 		or {@link UsbPort#POWER_ROLE_SINK}, or 0 if no power role.
     * @param dataRole
     * 		The desired data role: {@link UsbPort#DATA_ROLE_HOST}
     * 		or {@link UsbPort#DATA_ROLE_DEVICE}, or 0 if no data role.
     * @unknown 
     */
    public void setPortRoles(android.hardware.usb.UsbPort port, int powerRole, int dataRole) {
        com.android.internal.util.Preconditions.checkNotNull(port, "port must not be null");
        android.hardware.usb.UsbPort.checkRoles(powerRole, dataRole);
        try {
            mService.setPortRoles(port.getId(), powerRole, dataRole);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String addFunction(java.lang.String functions, java.lang.String function) {
        if (android.hardware.usb.UsbManager.USB_FUNCTION_NONE.equals(functions)) {
            return function;
        }
        if (!android.hardware.usb.UsbManager.containsFunction(functions, function)) {
            if (functions.length() > 0) {
                functions += ",";
            }
            functions += function;
        }
        return functions;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String removeFunction(java.lang.String functions, java.lang.String function) {
        java.lang.String[] split = functions.split(",");
        for (int i = 0; i < split.length; i++) {
            if (function.equals(split[i])) {
                split[i] = null;
            }
        }
        if ((split.length == 1) && (split[0] == null)) {
            return android.hardware.usb.UsbManager.USB_FUNCTION_NONE;
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (int i = 0; i < split.length; i++) {
            java.lang.String s = split[i];
            if (s != null) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(s);
            }
        }
        return builder.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean containsFunction(java.lang.String functions, java.lang.String function) {
        int index = functions.indexOf(function);
        if (index < 0)
            return false;

        if ((index > 0) && (functions.charAt(index - 1) != ','))
            return false;

        int charAfter = index + function.length();
        if ((charAfter < functions.length()) && (functions.charAt(charAfter) != ','))
            return false;

        return true;
    }
}

