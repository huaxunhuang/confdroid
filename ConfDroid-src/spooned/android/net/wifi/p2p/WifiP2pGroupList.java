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
package android.net.wifi.p2p;


/**
 * A class representing a Wi-Fi P2p group list
 *
 * {@see WifiP2pManager}
 *
 * @unknown 
 */
public class WifiP2pGroupList implements android.os.Parcelable {
    private static final int CREDENTIAL_MAX_NUM = 32;

    private final android.util.LruCache<java.lang.Integer, android.net.wifi.p2p.WifiP2pGroup> mGroups;

    private final android.net.wifi.p2p.WifiP2pGroupList.GroupDeleteListener mListener;

    private boolean isClearCalled = false;

    public interface GroupDeleteListener {
        public void onDeleteGroup(int netId);
    }

    /**
     *
     *
     * @unknown 
     */
    public WifiP2pGroupList() {
        this(null, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public WifiP2pGroupList(android.net.wifi.p2p.WifiP2pGroupList source, android.net.wifi.p2p.WifiP2pGroupList.GroupDeleteListener listener) {
        mListener = listener;
        mGroups = new android.util.LruCache<java.lang.Integer, android.net.wifi.p2p.WifiP2pGroup>(android.net.wifi.p2p.WifiP2pGroupList.CREDENTIAL_MAX_NUM) {
            @java.lang.Override
            protected void entryRemoved(boolean evicted, java.lang.Integer netId, android.net.wifi.p2p.WifiP2pGroup oldValue, android.net.wifi.p2p.WifiP2pGroup newValue) {
                if ((mListener != null) && (!isClearCalled)) {
                    mListener.onDeleteGroup(oldValue.getNetworkId());
                }
            }
        };
        if (source != null) {
            for (java.util.Map.Entry<java.lang.Integer, android.net.wifi.p2p.WifiP2pGroup> item : source.mGroups.snapshot().entrySet()) {
                mGroups.put(item.getKey(), item.getValue());
            }
        }
    }

    /**
     * Return the list of p2p group.
     *
     * @return the list of p2p group.
     */
    public java.util.Collection<android.net.wifi.p2p.WifiP2pGroup> getGroupList() {
        return mGroups.snapshot().values();
    }

    /**
     * Add the specified group to this group list.
     *
     * @param group
     * 		
     * @unknown 
     */
    public void add(android.net.wifi.p2p.WifiP2pGroup group) {
        mGroups.put(group.getNetworkId(), group);
    }

    /**
     * Remove the group with the specified network id from this group list.
     *
     * @param netId
     * 		
     * @unknown 
     */
    public void remove(int netId) {
        mGroups.remove(netId);
    }

    /**
     * Remove the group with the specified device address from this group list.
     *
     * @param deviceAddress
     * 		
     */
    void remove(java.lang.String deviceAddress) {
        remove(getNetworkId(deviceAddress));
    }

    /**
     * Clear the group.
     *
     * @unknown 
     */
    public boolean clear() {
        if (mGroups.size() == 0)
            return false;

        isClearCalled = true;
        mGroups.evictAll();
        isClearCalled = false;
        return true;
    }

    /**
     * Return the network id of the group owner profile with the specified p2p device
     * address.
     * If more than one persistent group of the same address is present in the list,
     * return the first one.
     *
     * @param deviceAddress
     * 		p2p device address.
     * @return the network id. if not found, return -1.
     * @unknown 
     */
    public int getNetworkId(java.lang.String deviceAddress) {
        if (deviceAddress == null)
            return -1;

        final java.util.Collection<android.net.wifi.p2p.WifiP2pGroup> groups = mGroups.snapshot().values();
        for (android.net.wifi.p2p.WifiP2pGroup grp : groups) {
            if (deviceAddress.equalsIgnoreCase(grp.getOwner().deviceAddress)) {
                // update cache ordered.
                mGroups.get(grp.getNetworkId());
                return grp.getNetworkId();
            }
        }
        return -1;
    }

    /**
     * Return the network id of the group with the specified p2p device address
     * and the ssid.
     *
     * @param deviceAddress
     * 		p2p device address.
     * @param ssid
     * 		ssid.
     * @return the network id. if not found, return -1.
     * @unknown 
     */
    public int getNetworkId(java.lang.String deviceAddress, java.lang.String ssid) {
        if ((deviceAddress == null) || (ssid == null)) {
            return -1;
        }
        final java.util.Collection<android.net.wifi.p2p.WifiP2pGroup> groups = mGroups.snapshot().values();
        for (android.net.wifi.p2p.WifiP2pGroup grp : groups) {
            if (deviceAddress.equalsIgnoreCase(grp.getOwner().deviceAddress) && ssid.equals(grp.getNetworkName())) {
                // update cache ordered.
                mGroups.get(grp.getNetworkId());
                return grp.getNetworkId();
            }
        }
        return -1;
    }

    /**
     * Return the group owner address of the group with the specified network id
     *
     * @param netId
     * 		network id.
     * @return the address. if not found, return null.
     * @unknown 
     */
    public java.lang.String getOwnerAddr(int netId) {
        android.net.wifi.p2p.WifiP2pGroup grp = mGroups.get(netId);
        if (grp != null) {
            return grp.getOwner().deviceAddress;
        }
        return null;
    }

    /**
     * Return true if this group list contains the specified network id.
     * This function does NOT update LRU information.
     * It means the internal queue is NOT reordered.
     *
     * @param netId
     * 		network id.
     * @return true if the specified network id is present in this group list.
     * @unknown 
     */
    public boolean contains(int netId) {
        final java.util.Collection<android.net.wifi.p2p.WifiP2pGroup> groups = mGroups.snapshot().values();
        for (android.net.wifi.p2p.WifiP2pGroup grp : groups) {
            if (netId == grp.getNetworkId()) {
                return true;
            }
        }
        return false;
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        final java.util.Collection<android.net.wifi.p2p.WifiP2pGroup> groups = mGroups.snapshot().values();
        for (android.net.wifi.p2p.WifiP2pGroup grp : groups) {
            sbuf.append(grp).append("\n");
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
        final java.util.Collection<android.net.wifi.p2p.WifiP2pGroup> groups = mGroups.snapshot().values();
        dest.writeInt(groups.size());
        for (android.net.wifi.p2p.WifiP2pGroup group : groups) {
            dest.writeParcelable(group, flags);
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pGroupList> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pGroupList>() {
        public android.net.wifi.p2p.WifiP2pGroupList createFromParcel(android.os.Parcel in) {
            android.net.wifi.p2p.WifiP2pGroupList grpList = new android.net.wifi.p2p.WifiP2pGroupList();
            int deviceCount = in.readInt();
            for (int i = 0; i < deviceCount; i++) {
                grpList.add(((android.net.wifi.p2p.WifiP2pGroup) (in.readParcelable(null))));
            }
            return grpList;
        }

        public android.net.wifi.p2p.WifiP2pGroupList[] newArray(int size) {
            return new android.net.wifi.p2p.WifiP2pGroupList[size];
        }
    };
}

