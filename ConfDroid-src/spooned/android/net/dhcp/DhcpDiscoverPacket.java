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
 * This class implements the DHCP-DISCOVER packet.
 */
class DhcpDiscoverPacket extends android.net.dhcp.DhcpPacket {
    /**
     * Generates a DISCOVER packet with the specified parameters.
     */
    DhcpDiscoverPacket(int transId, short secs, byte[] clientMac, boolean broadcast) {
        super(transId, secs, android.net.dhcp.DhcpPacket.INADDR_ANY, android.net.dhcp.DhcpPacket.INADDR_ANY, android.net.dhcp.DhcpPacket.INADDR_ANY, android.net.dhcp.DhcpPacket.INADDR_ANY, clientMac, broadcast);
    }

    public java.lang.String toString() {
        java.lang.String s = super.toString();
        return (s + " DISCOVER ") + (mBroadcast ? "broadcast " : "unicast ");
    }

    /**
     * Fills in a packet with the requested DISCOVER parameters.
     */
    public java.nio.ByteBuffer buildPacket(int encap, short destUdp, short srcUdp) {
        java.nio.ByteBuffer result = java.nio.ByteBuffer.allocate(android.net.dhcp.DhcpPacket.MAX_LENGTH);
        fillInPacket(encap, android.net.dhcp.DhcpPacket.INADDR_BROADCAST, android.net.dhcp.DhcpPacket.INADDR_ANY, destUdp, srcUdp, result, android.net.dhcp.DhcpPacket.DHCP_BOOTREQUEST, mBroadcast);
        result.flip();
        return result;
    }

    /**
     * Adds optional parameters to a DISCOVER packet.
     */
    void finishPacket(java.nio.ByteBuffer buffer) {
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_DISCOVER);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_CLIENT_IDENTIFIER, getClientId());
        addCommonClientTlvs(buffer);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_PARAMETER_LIST, mRequestedParams);
        android.net.dhcp.DhcpPacket.addTlvEnd(buffer);
    }
}

