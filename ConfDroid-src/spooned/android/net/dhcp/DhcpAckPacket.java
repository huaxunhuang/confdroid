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
package android.net.dhcp;


/**
 * This class implements the DHCP-ACK packet.
 */
class DhcpAckPacket extends android.net.dhcp.DhcpPacket {
    /**
     * The address of the server which sent this packet.
     */
    private final java.net.Inet4Address mSrcIp;

    DhcpAckPacket(int transId, short secs, boolean broadcast, java.net.Inet4Address serverAddress, java.net.Inet4Address clientIp, java.net.Inet4Address yourIp, byte[] clientMac) {
        super(transId, secs, clientIp, yourIp, serverAddress, android.net.dhcp.DhcpPacket.INADDR_ANY, clientMac, broadcast);
        mBroadcast = broadcast;
        mSrcIp = serverAddress;
    }

    public java.lang.String toString() {
        java.lang.String s = super.toString();
        java.lang.String dnsServers = " DNS servers: ";
        for (java.net.Inet4Address dnsServer : mDnsServers) {
            dnsServers += dnsServer.toString() + " ";
        }
        return ((((((((s + " ACK: your new IP ") + mYourIp) + ", netmask ") + mSubnetMask) + ", gateways ") + mGateways) + dnsServers) + ", lease time ") + mLeaseTime;
    }

    /**
     * Fills in a packet with the requested ACK parameters.
     */
    public java.nio.ByteBuffer buildPacket(int encap, short destUdp, short srcUdp) {
        java.nio.ByteBuffer result = java.nio.ByteBuffer.allocate(android.net.dhcp.DhcpPacket.MAX_LENGTH);
        java.net.Inet4Address destIp = (mBroadcast) ? android.net.dhcp.DhcpPacket.INADDR_BROADCAST : mYourIp;
        java.net.Inet4Address srcIp = (mBroadcast) ? android.net.dhcp.DhcpPacket.INADDR_ANY : mSrcIp;
        fillInPacket(encap, destIp, srcIp, destUdp, srcUdp, result, android.net.dhcp.DhcpPacket.DHCP_BOOTREPLY, mBroadcast);
        result.flip();
        return result;
    }

    /**
     * Adds the optional parameters to the client-generated ACK packet.
     */
    void finishPacket(java.nio.ByteBuffer buffer) {
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_ACK);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_SERVER_IDENTIFIER, mServerIdentifier);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_LEASE_TIME, mLeaseTime);
        // the client should renew at 1/2 the lease-expiry interval
        if (mLeaseTime != null) {
            android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_RENEWAL_TIME, java.lang.Integer.valueOf(mLeaseTime.intValue() / 2));
        }
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_SUBNET_MASK, mSubnetMask);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_ROUTER, mGateways);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_DOMAIN_NAME, mDomainName);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_BROADCAST_ADDRESS, mBroadcastAddress);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_DNS_SERVER, mDnsServers);
        android.net.dhcp.DhcpPacket.addTlvEnd(buffer);
    }

    /**
     * Un-boxes an Integer, returning 0 if a null reference is supplied.
     */
    private static final int getInt(java.lang.Integer v) {
        if (v == null) {
            return 0;
        } else {
            return v.intValue();
        }
    }
}

