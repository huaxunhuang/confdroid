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
 * A class for creating a Bonjour service discovery request for use with
 * {@link WifiP2pManager#addServiceRequest} and {@link WifiP2pManager#removeServiceRequest}
 *
 * {@see WifiP2pManager}
 * {@see WifiP2pServiceRequest}
 * {@see WifiP2pUpnpServiceRequest}
 */
public class WifiP2pDnsSdServiceRequest extends android.net.wifi.p2p.nsd.WifiP2pServiceRequest {
    /**
     * This constructor is only used in newInstance().
     *
     * @param query
     * 		The part of service specific query.
     * @unknown 
     */
    private WifiP2pDnsSdServiceRequest(java.lang.String query) {
        super(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.SERVICE_TYPE_BONJOUR, query);
    }

    /**
     * This constructor is only used in newInstance().
     *
     * @unknown 
     */
    private WifiP2pDnsSdServiceRequest() {
        super(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.SERVICE_TYPE_BONJOUR, null);
    }

    private WifiP2pDnsSdServiceRequest(java.lang.String dnsQuery, int dnsType, int version) {
        super(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.SERVICE_TYPE_BONJOUR, android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.createRequest(dnsQuery, dnsType, version));
    }

    /**
     * Create a service discovery request to search all Bonjour services.
     *
     * @return service request for Bonjour.
     */
    public static android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest newInstance() {
        return new android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest();
    }

    /**
     * Create a service discovery to search for Bonjour services with the specified
     * service type.
     *
     * @param serviceType
     * 		service type. Cannot be null <br>
     * 		"_afpovertcp._tcp."(Apple File Sharing over TCP)<br>
     * 		"_ipp._tcp" (IP Printing over TCP)<br>
     * 		"_http._tcp" (http service)
     * @return service request for DnsSd.
     */
    public static android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest newInstance(java.lang.String serviceType) {
        if (serviceType == null) {
            throw new java.lang.IllegalArgumentException("service type cannot be null");
        }
        return new android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest(serviceType + ".local.", android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_PTR, android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.VERSION_1);
    }

    /**
     * Create a service discovery request to get the TXT data from the specified
     * Bonjour service.
     *
     * @param instanceName
     * 		instance name. Cannot be null. <br>
     * 		"MyPrinter"
     * @param serviceType
     * 		service type. Cannot be null. <br>
     * 		e.g) <br>
     * 		"_afpovertcp._tcp"(Apple File Sharing over TCP)<br>
     * 		"_ipp._tcp" (IP Printing over TCP)<br>
     * @return service request for Bonjour.
     */
    public static android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest newInstance(java.lang.String instanceName, java.lang.String serviceType) {
        if ((instanceName == null) || (serviceType == null)) {
            throw new java.lang.IllegalArgumentException("instance name or service type cannot be null");
        }
        java.lang.String fullDomainName = ((instanceName + ".") + serviceType) + ".local.";
        return new android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest(fullDomainName, android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_TXT, android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.VERSION_1);
    }
}

