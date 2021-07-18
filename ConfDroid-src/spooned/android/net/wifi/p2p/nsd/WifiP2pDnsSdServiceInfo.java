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
 * A class for storing Bonjour service information that is advertised
 * over a Wi-Fi peer-to-peer setup.
 *
 * {@see android.net.wifi.p2p.WifiP2pManager#addLocalService}
 * {@see android.net.wifi.p2p.WifiP2pManager#removeLocalService}
 * {@see WifiP2pServiceInfo}
 * {@see WifiP2pUpnpServiceInfo}
 */
public class WifiP2pDnsSdServiceInfo extends android.net.wifi.p2p.nsd.WifiP2pServiceInfo {
    /**
     * Bonjour version 1.
     *
     * @unknown 
     */
    public static final int VERSION_1 = 0x1;

    /**
     * Pointer record.
     *
     * @unknown 
     */
    public static final int DNS_TYPE_PTR = 12;

    /**
     * Text record.
     *
     * @unknown 
     */
    public static final int DNS_TYPE_TXT = 16;

    /**
     * virtual memory packet.
     * see E.3 of the Wi-Fi Direct technical specification for the detail.<br>
     * Key: domain name Value: pointer address.<br>
     */
    private static final java.util.Map<java.lang.String, java.lang.String> sVmPacket;

    static {
        sVmPacket = new java.util.HashMap<java.lang.String, java.lang.String>();
        android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.sVmPacket.put("_tcp.local.", "c00c");
        android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.sVmPacket.put("local.", "c011");
        android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.sVmPacket.put("_udp.local.", "c01c");
    }

    /**
     * This constructor is only used in newInstance().
     *
     * @param queryList
     * 		
     */
    private WifiP2pDnsSdServiceInfo(java.util.List<java.lang.String> queryList) {
        super(queryList);
    }

    /**
     * Create a Bonjour service information object.
     *
     * @param instanceName
     * 		instance name.<br>
     * 		e.g) "MyPrinter"
     * @param serviceType
     * 		service type.<br>
     * 		e.g) "_ipp._tcp"
     * @param txtMap
     * 		TXT record with key/value pair in a map confirming to format defined at
     * 		http://files.dns-sd.org/draft-cheshire-dnsext-dns-sd.txt
     * @return Bonjour service information object
     */
    public static android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo newInstance(java.lang.String instanceName, java.lang.String serviceType, java.util.Map<java.lang.String, java.lang.String> txtMap) {
        if (android.text.TextUtils.isEmpty(instanceName) || android.text.TextUtils.isEmpty(serviceType)) {
            throw new java.lang.IllegalArgumentException("instance name or service type cannot be empty");
        }
        android.net.nsd.DnsSdTxtRecord txtRecord = new android.net.nsd.DnsSdTxtRecord();
        if (txtMap != null) {
            for (java.lang.String key : txtMap.keySet()) {
                txtRecord.set(key, txtMap.get(key));
            }
        }
        java.util.ArrayList<java.lang.String> queries = new java.util.ArrayList<java.lang.String>();
        queries.add(android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.createPtrServiceQuery(instanceName, serviceType));
        queries.add(android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.createTxtServiceQuery(instanceName, serviceType, txtRecord));
        return new android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo(queries);
    }

    /**
     * Create wpa_supplicant service query for PTR record.
     *
     * @param instanceName
     * 		instance name.<br>
     * 		e.g) "MyPrinter"
     * @param serviceType
     * 		service type.<br>
     * 		e.g) "_ipp._tcp"
     * @return wpa_supplicant service query.
     */
    private static java.lang.String createPtrServiceQuery(java.lang.String instanceName, java.lang.String serviceType) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("bonjour ");
        sb.append(android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.createRequest(serviceType + ".local.", android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_PTR, android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.VERSION_1));
        sb.append(" ");
        byte[] data = instanceName.getBytes();
        sb.append(java.lang.String.format(java.util.Locale.US, "%02x", data.length));
        sb.append(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(data));
        // This is the start point of this response.
        // Therefore, it indicates the request domain name.
        sb.append("c027");
        return sb.toString();
    }

    /**
     * Create wpa_supplicant service query for TXT record.
     *
     * @param instanceName
     * 		instance name.<br>
     * 		e.g) "MyPrinter"
     * @param serviceType
     * 		service type.<br>
     * 		e.g) "_ipp._tcp"
     * @param txtRecord
     * 		TXT record.<br>
     * @return wpa_supplicant service query.
     */
    private static java.lang.String createTxtServiceQuery(java.lang.String instanceName, java.lang.String serviceType, android.net.nsd.DnsSdTxtRecord txtRecord) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("bonjour ");
        sb.append(android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.createRequest(((instanceName + ".") + serviceType) + ".local.", android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_TXT, android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.VERSION_1));
        sb.append(" ");
        byte[] rawData = txtRecord.getRawData();
        if (rawData.length == 0) {
            sb.append("00");
        } else {
            sb.append(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(rawData));
        }
        return sb.toString();
    }

    /**
     * Create bonjour service discovery request.
     *
     * @param dnsName
     * 		dns name
     * @param dnsType
     * 		dns type
     * @param version
     * 		version number
     * @unknown 
     */
    static java.lang.String createRequest(java.lang.String dnsName, int dnsType, int version) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        /* The request format is as follows.
        ________________________________________________
        |  Encoded and Compressed dns name (variable)  |
        ________________________________________________
        |   Type (2)           | Version (1) |
         */
        if (dnsType == android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_TXT) {
            dnsName = dnsName.toLowerCase(java.util.Locale.ROOT);// TODO: is this right?

        }
        sb.append(android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.compressDnsName(dnsName));
        sb.append(java.lang.String.format(java.util.Locale.US, "%04x", dnsType));
        sb.append(java.lang.String.format(java.util.Locale.US, "%02x", version));
        return sb.toString();
    }

    /**
     * Compress DNS data.
     *
     * see E.3 of the Wi-Fi Direct technical specification for the detail.
     *
     * @param dnsName
     * 		dns name
     * @return compressed dns name
     */
    private static java.lang.String compressDnsName(java.lang.String dnsName) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        // The domain name is replaced with a pointer to a prior
        // occurrence of the same name in virtual memory packet.
        while (true) {
            java.lang.String data = android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.sVmPacket.get(dnsName);
            if (data != null) {
                sb.append(data);
                break;
            }
            int i = dnsName.indexOf('.');
            if (i == (-1)) {
                if (dnsName.length() > 0) {
                    sb.append(java.lang.String.format(java.util.Locale.US, "%02x", dnsName.length()));
                    sb.append(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(dnsName.getBytes()));
                }
                // for a sequence of labels ending in a zero octet
                sb.append("00");
                break;
            }
            java.lang.String name = dnsName.substring(0, i);
            dnsName = dnsName.substring(i + 1);
            sb.append(java.lang.String.format(java.util.Locale.US, "%02x", name.length()));
            sb.append(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(name.getBytes()));
        } 
        return sb.toString();
    }
}

