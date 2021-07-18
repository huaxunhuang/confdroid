/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.net.wifi.p2p;


/**
 * A class representing a Wi-Fi P2p device list.
 *
 * Note that the operations are not thread safe.
 * {@see WifiP2pManager}
 */
public class WifiP2pDeviceList implements android.os.Parcelable {
    private final java.util.HashMap<java.lang.String, android.net.wifi.p2p.WifiP2pDevice> mDevices = new java.util.HashMap<java.lang.String, android.net.wifi.p2p.WifiP2pDevice>();

    public WifiP2pDeviceList() {
    }

    /**
     * copy constructor
     */
    public WifiP2pDeviceList(android.net.wifi.p2p.WifiP2pDeviceList source) {
        if (source != null) {
            for (android.net.wifi.p2p.WifiP2pDevice d : source.getDeviceList()) {
                mDevices.put(d.deviceAddress, new android.net.wifi.p2p.WifiP2pDevice(d));
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public WifiP2pDeviceList(java.util.ArrayList<android.net.wifi.p2p.WifiP2pDevice> devices) {
        for (android.net.wifi.p2p.WifiP2pDevice device : devices) {
            if (device.deviceAddress != null) {
                mDevices.put(device.deviceAddress, new android.net.wifi.p2p.WifiP2pDevice(device));
            }
        }
    }

    private void validateDevice(android.net.wifi.p2p.WifiP2pDevice device) {
        if (device == null)
            throw new java.lang.IllegalArgumentException("Null device");

        if (android.text.TextUtils.isEmpty(device.deviceAddress)) {
            throw new java.lang.IllegalArgumentException("Empty deviceAddress");
        }
    }

    private void validateDeviceAddress(java.lang.String deviceAddress) {
        if (android.text.TextUtils.isEmpty(deviceAddress)) {
            throw new java.lang.IllegalArgumentException("Empty deviceAddress");
        }
    }

    /**
     * Clear the list @hide
     */
    public boolean clear() {
        if (mDevices.isEmpty())
            return false;

        mDevices.clear();
        return true;
    }

    /**
     * Add/update a device to the list. If the device is not found, a new device entry
     * is created. If the device is already found, the device details are updated
     *
     * @param device
     * 		to be updated
     * @unknown 
     */
    public void update(android.net.wifi.p2p.WifiP2pDevice device) {
        updateSupplicantDetails(device);
        mDevices.get(device.deviceAddress).status = device.status;
    }

    /**
     * Only updates details fetched from the supplicant @hide
     */
    public void updateSupplicantDetails(android.net.wifi.p2p.WifiP2pDevice device) {
        validateDevice(device);
        android.net.wifi.p2p.WifiP2pDevice d = mDevices.get(device.deviceAddress);
        if (d != null) {
            d.deviceName = device.deviceName;
            d.primaryDeviceType = device.primaryDeviceType;
            d.secondaryDeviceType = device.secondaryDeviceType;
            d.wpsConfigMethodsSupported = device.wpsConfigMethodsSupported;
            d.deviceCapability = device.deviceCapability;
            d.groupCapability = device.groupCapability;
            d.wfdInfo = device.wfdInfo;
            return;
        }
        // Not found, add a new one
        mDevices.put(device.deviceAddress, device);
    }

    /**
     *
     *
     * @unknown 
     */
    public void updateGroupCapability(java.lang.String deviceAddress, int groupCapab) {
        validateDeviceAddress(deviceAddress);
        android.net.wifi.p2p.WifiP2pDevice d = mDevices.get(deviceAddress);
        if (d != null) {
            d.groupCapability = groupCapab;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void updateStatus(java.lang.String deviceAddress, int status) {
        validateDeviceAddress(deviceAddress);
        android.net.wifi.p2p.WifiP2pDevice d = mDevices.get(deviceAddress);
        if (d != null) {
            d.status = status;
        }
    }

    /**
     * Fetch a device from the list
     *
     * @param deviceAddress
     * 		is the address of the device
     * @return WifiP2pDevice device found, or null if none found
     */
    public android.net.wifi.p2p.WifiP2pDevice get(java.lang.String deviceAddress) {
        validateDeviceAddress(deviceAddress);
        return mDevices.get(deviceAddress);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean remove(android.net.wifi.p2p.WifiP2pDevice device) {
        validateDevice(device);
        return mDevices.remove(device.deviceAddress) != null;
    }

    /**
     * Remove a device from the list
     *
     * @param deviceAddress
     * 		is the address of the device
     * @return WifiP2pDevice device removed, or null if none removed
     * @unknown 
     */
    public android.net.wifi.p2p.WifiP2pDevice remove(java.lang.String deviceAddress) {
        validateDeviceAddress(deviceAddress);
        return mDevices.remove(deviceAddress);
    }

    /**
     * Returns true if any device the list was removed @hide
     */
    public boolean remove(android.net.wifi.p2p.WifiP2pDeviceList list) {
        boolean ret = false;
        for (android.net.wifi.p2p.WifiP2pDevice d : list.mDevices.values()) {
            if (remove(d))
                ret = true;

        }
        return ret;
    }

    /**
     * Get the list of devices
     */
    public java.util.Collection<android.net.wifi.p2p.WifiP2pDevice> getDeviceList() {
        return java.util.Collections.unmodifiableCollection(mDevices.values());
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isGroupOwner(java.lang.String deviceAddress) {
        validateDeviceAddress(deviceAddress);
        android.net.wifi.p2p.WifiP2pDevice device = mDevices.get(deviceAddress);
        if (device == null) {
            throw new java.lang.IllegalArgumentException("Device not found " + deviceAddress);
        }
        return device.isGroupOwner();
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        for (android.net.wifi.p2p.WifiP2pDevice device : mDevices.values()) {
            sbuf.append("\n").append(device);
        }
        return sbuf.toString();
    }

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mDevices.size());
        for (android.net.wifi.p2p.WifiP2pDevice device : mDevices.values()) {
            dest.writeParcelable(device, flags);
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pDeviceList> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pDeviceList>() {
        public android.net.wifi.p2p.WifiP2pDeviceList createFromParcel(android.os.Parcel in) {
            android.net.wifi.p2p.WifiP2pDeviceList deviceList = new android.net.wifi.p2p.WifiP2pDeviceList();
            int deviceCount = in.readInt();
            for (int i = 0; i < deviceCount; i++) {
                deviceList.update(((android.net.wifi.p2p.WifiP2pDevice) (in.readParcelable(null))));
            }
            return deviceList;
        }

        public android.net.wifi.p2p.WifiP2pDeviceList[] newArray(int size) {
            return new android.net.wifi.p2p.WifiP2pDeviceList[size];
        }
    };
}

