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
package android.net.wifi.p2p.nsd;


/**
 * A class for storing Upnp service information that is advertised
 * over a Wi-Fi peer-to-peer setup.
 *
 * {@see android.net.wifi.p2p.WifiP2pManager#addLocalService}
 * {@see android.net.wifi.p2p.WifiP2pManager#removeLocalService}
 * {@see WifiP2pServiceInfo}
 * {@see WifiP2pDnsSdServiceInfo}
 */
public class WifiP2pUpnpServiceInfo extends android.net.wifi.p2p.nsd.WifiP2pServiceInfo {
    /**
     * UPnP version 1.0.
     *
     * <pre>Query Version should always be set to 0x10 if the query values are
     * compatible with UPnP Device Architecture 1.0.
     *
     * @unknown 
     */
    public static final int VERSION_1_0 = 0x10;

    /**
     * This constructor is only used in newInstance().
     *
     * @param queryList
     * 		
     */
    private WifiP2pUpnpServiceInfo(java.util.List<java.lang.String> queryList) {
        super(queryList);
    }

    /**
     * Create UPnP service information object.
     *
     * @param uuid
     * 		a string representation of this UUID in the following format,
     * 		as per <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>.<br>
     * 		e.g) 6859dede-8574-59ab-9332-123456789012
     * @param device
     * 		a string representation of this device in the following format,
     * 		as per
     * 		<a href="http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf">
     * 		UPnP Device Architecture1.1</a><br>
     * 		e.g) urn:schemas-upnp-org:device:MediaServer:1
     * @param services
     * 		a string representation of this service in the following format,
     * 		as per
     * 		<a href="http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf">
     * 		UPnP Device Architecture1.1</a><br>
     * 		e.g) urn:schemas-upnp-org:service:ContentDirectory:1
     * @return UPnP service information object.
     */
    public static android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo newInstance(java.lang.String uuid, java.lang.String device, java.util.List<java.lang.String> services) {
        if ((uuid == null) || (device == null)) {
            throw new java.lang.IllegalArgumentException("uuid or device cannnot be null");
        }
        java.util.UUID.fromString(uuid);
        java.util.ArrayList<java.lang.String> info = new java.util.ArrayList<java.lang.String>();
        info.add(android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.createSupplicantQuery(uuid, null));
        info.add(android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.createSupplicantQuery(uuid, "upnp:rootdevice"));
        info.add(android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.createSupplicantQuery(uuid, device));
        if (services != null) {
            for (java.lang.String service : services) {
                info.add(android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.createSupplicantQuery(uuid, service));
            }
        }
        return new android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo(info);
    }

    /**
     * Create wpa_supplicant service query for upnp.
     *
     * @param uuid
     * 		
     * @param data
     * 		
     * @return wpa_supplicant service query for upnp
     */
    private static java.lang.String createSupplicantQuery(java.lang.String uuid, java.lang.String data) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("upnp ");
        sb.append(java.lang.String.format(java.util.Locale.US, "%02x ", android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.VERSION_1_0));
        sb.append("uuid:");
        sb.append(uuid);
        if (data != null) {
            sb.append("::");
            sb.append(data);
        }
        return sb.toString();
    }
}

