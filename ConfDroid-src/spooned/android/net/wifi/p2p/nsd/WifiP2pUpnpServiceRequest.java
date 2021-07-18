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
 * A class for creating a Upnp service discovery request for use with
 * {@link WifiP2pManager#addServiceRequest} and {@link WifiP2pManager#removeServiceRequest}
 *
 * {@see WifiP2pManager}
 * {@see WifiP2pServiceRequest}
 * {@see WifiP2pDnsSdServiceRequest}
 */
public class WifiP2pUpnpServiceRequest extends android.net.wifi.p2p.nsd.WifiP2pServiceRequest {
    /**
     * This constructor is only used in newInstance().
     *
     * @param query
     * 		The part of service specific query.
     * @unknown 
     */
    protected WifiP2pUpnpServiceRequest(java.lang.String query) {
        super(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.SERVICE_TYPE_UPNP, query);
    }

    /**
     * This constructor is only used in newInstance().
     *
     * @unknown 
     */
    protected WifiP2pUpnpServiceRequest() {
        super(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.SERVICE_TYPE_UPNP, null);
    }

    /**
     * Create a service discovery request to search all UPnP services.
     *
     * @return service request for UPnP.
     */
    public static android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest newInstance() {
        return new android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest();
    }

    /**
     * Create a service discovery request to search specified UPnP services.
     *
     * @param st
     * 		ssdp search target.  Cannot be null.<br>
     * 		e.g ) <br>
     * 		<ul>
     * 		<li>"ssdp:all"
     * 		<li>"upnp:rootdevice"
     * 		<li>"urn:schemas-upnp-org:device:MediaServer:2"
     * 		<li>"urn:schemas-upnp-org:service:ContentDirectory:2"
     * 		<li>"uuid:6859dede-8574-59ab-9332-123456789012"
     * 		</ul>
     * @return service request for UPnP.
     */
    public static android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest newInstance(java.lang.String st) {
        if (st == null) {
            throw new java.lang.IllegalArgumentException("search target cannot be null");
        }
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append(java.lang.String.format(java.util.Locale.US, "%02x", android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.VERSION_1_0));
        sb.append(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(st.getBytes()));
        return new android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest(sb.toString());
    }
}

