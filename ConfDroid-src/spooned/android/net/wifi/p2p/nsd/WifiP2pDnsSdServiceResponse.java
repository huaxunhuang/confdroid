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
 * A class for a response of bonjour service discovery.
 *
 * @unknown 
 */
public class WifiP2pDnsSdServiceResponse extends android.net.wifi.p2p.nsd.WifiP2pServiceResponse {
    /**
     * DNS query name.
     * e.g)
     * for PTR
     * "_ipp._tcp.local."
     * for TXT
     * "MyPrinter._ipp._tcp.local."
     */
    private java.lang.String mDnsQueryName;

    /**
     * Service instance name.
     * e.g) "MyPrinter"
     * This field is only used when the dns type equals to
     * {@link WifiP2pDnsSdServiceInfo#DNS_TYPE_PTR}.
     */
    private java.lang.String mInstanceName;

    /**
     * DNS Type.
     * Should be {@link WifiP2pDnsSdServiceInfo#DNS_TYPE_PTR} or
     * {@link WifiP2pDnsSdServiceInfo#DNS_TYPE_TXT}.
     */
    private int mDnsType;

    /**
     * DnsSd version number.
     * Should be {@link WifiP2pDnsSdServiceInfo#VERSION_1}.
     */
    private int mVersion;

    /**
     * Txt record.
     * This field is only used when the dns type equals to
     * {@link WifiP2pDnsSdServiceInfo#DNS_TYPE_TXT}.
     */
    private final java.util.HashMap<java.lang.String, java.lang.String> mTxtRecord = new java.util.HashMap<java.lang.String, java.lang.String>();

    /**
     * Virtual memory packet.
     * see E.3 of the Wi-Fi Direct technical specification for the detail.<br>
     * The spec can be obtained from wi-fi.org
     * Key: pointer Value: domain name.<br>
     */
    private static final java.util.Map<java.lang.Integer, java.lang.String> sVmpack;

    static {
        sVmpack = new java.util.HashMap<java.lang.Integer, java.lang.String>();
        android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse.sVmpack.put(0xc, "_tcp.local.");
        android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse.sVmpack.put(0x11, "local.");
        android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse.sVmpack.put(0x1c, "_udp.local.");
    }

    /**
     * Returns query DNS name.
     *
     * @return DNS name.
     */
    public java.lang.String getDnsQueryName() {
        return mDnsQueryName;
    }

    /**
     * Return query DNS type.
     *
     * @return DNS type.
     */
    public int getDnsType() {
        return mDnsType;
    }

    /**
     * Return bonjour version number.
     *
     * @return version number.
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * Return instance name.
     *
     * @return 
     */
    public java.lang.String getInstanceName() {
        return mInstanceName;
    }

    /**
     * Return TXT record data.
     *
     * @return TXT record data.
     */
    public java.util.Map<java.lang.String, java.lang.String> getTxtRecord() {
        return mTxtRecord;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        sbuf.append("serviceType:DnsSd(").append(mServiceType).append(")");
        sbuf.append(" status:").append(android.net.wifi.p2p.nsd.WifiP2pServiceResponse.Status.toString(mStatus));
        sbuf.append(" srcAddr:").append(mDevice.deviceAddress);
        sbuf.append(" version:").append(java.lang.String.format("%02x", mVersion));
        sbuf.append(" dnsName:").append(mDnsQueryName);
        sbuf.append(" TxtRecord:");
        for (java.lang.String key : mTxtRecord.keySet()) {
            sbuf.append(" key:").append(key).append(" value:").append(mTxtRecord.get(key));
        }
        if (mInstanceName != null) {
            sbuf.append(" InsName:").append(mInstanceName);
        }
        return sbuf.toString();
    }

    /**
     * This is only used in framework.
     *
     * @param status
     * 		status code.
     * @param dev
     * 		source device.
     * @param data
     * 		RDATA.
     * @unknown 
     */
    protected WifiP2pDnsSdServiceResponse(int status, int tranId, android.net.wifi.p2p.WifiP2pDevice dev, byte[] data) {
        super(android.net.wifi.p2p.nsd.WifiP2pServiceInfo.SERVICE_TYPE_BONJOUR, status, tranId, dev, data);
        if (!parse()) {
            throw new java.lang.IllegalArgumentException("Malformed bonjour service response");
        }
    }

    /**
     * Parse DnsSd service discovery response.
     *
     * @return {@code true} if the operation succeeded
     */
    private boolean parse() {
        /* The data format from Wi-Fi Direct spec is as follows.
        ________________________________________________
        |  encoded and compressed dns name (variable)  |
        ________________________________________________
        |       dnstype(2byte)      |  version(1byte)  |
        ________________________________________________
        |              RDATA (variable)                |
         */
        if (mData == null) {
            // the empty is OK.
            return true;
        }
        java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(mData));
        mDnsQueryName = readDnsName(dis);
        if (mDnsQueryName == null) {
            return false;
        }
        try {
            mDnsType = dis.readUnsignedShort();
            mVersion = dis.readUnsignedByte();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
        if (mDnsType == android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_PTR) {
            java.lang.String rData = readDnsName(dis);
            if (rData == null) {
                return false;
            }
            if (rData.length() <= mDnsQueryName.length()) {
                return false;
            }
            mInstanceName = rData.substring(0, (rData.length() - mDnsQueryName.length()) - 1);
        } else
            if (mDnsType == android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.DNS_TYPE_TXT) {
                return readTxtData(dis);
            } else {
                return false;
            }

        return true;
    }

    /**
     * Read dns name.
     *
     * @param dis
     * 		data input stream.
     * @return dns name
     */
    private java.lang.String readDnsName(java.io.DataInputStream dis) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        // copy virtual memory packet.
        java.util.HashMap<java.lang.Integer, java.lang.String> vmpack = new java.util.HashMap<java.lang.Integer, java.lang.String>(android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse.sVmpack);
        if (mDnsQueryName != null) {
            vmpack.put(0x27, mDnsQueryName);
        }
        try {
            while (true) {
                int i = dis.readUnsignedByte();
                if (i == 0x0) {
                    return sb.toString();
                } else
                    if (i == 0xc0) {
                        // refer to pointer.
                        java.lang.String ref = vmpack.get(dis.readUnsignedByte());
                        if (ref == null) {
                            // invalid.
                            return null;
                        }
                        sb.append(ref);
                        return sb.toString();
                    } else {
                        byte[] data = new byte[i];
                        dis.readFully(data);
                        sb.append(new java.lang.String(data));
                        sb.append(".");
                    }

            } 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Read TXT record data.
     *
     * @param dis
     * 		
     * @return true if TXT data is valid
     */
    private boolean readTxtData(java.io.DataInputStream dis) {
        try {
            while (dis.available() > 0) {
                int len = dis.readUnsignedByte();
                if (len == 0) {
                    break;
                }
                byte[] data = new byte[len];
                dis.readFully(data);
                java.lang.String[] keyVal = new java.lang.String(data).split("=");
                if (keyVal.length != 2) {
                    return false;
                }
                mTxtRecord.put(keyVal[0], keyVal[1]);
            } 
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates DnsSd service response.
     *  This is only called from WifiP2pServiceResponse
     *
     * @param status
     * 		status code.
     * @param dev
     * 		source device.
     * @param data
     * 		DnsSd response data.
     * @return DnsSd service response data.
     * @unknown 
     */
    static android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse newInstance(int status, int transId, android.net.wifi.p2p.WifiP2pDevice dev, byte[] data) {
        if (status != android.net.wifi.p2p.nsd.WifiP2pServiceResponse.Status.SUCCESS) {
            return new android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse(status, transId, dev, null);
        }
        try {
            return new android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse(status, transId, dev, data);
        } catch (java.lang.IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}

