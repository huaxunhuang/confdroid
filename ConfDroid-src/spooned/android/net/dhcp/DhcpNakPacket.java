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
 * This class implements the DHCP-NAK packet.
 */
class DhcpNakPacket extends android.net.dhcp.DhcpPacket {
    /**
     * Generates a NAK packet with the specified parameters.
     */
    DhcpNakPacket(int transId, short secs, java.net.Inet4Address clientIp, java.net.Inet4Address yourIp, java.net.Inet4Address nextIp, java.net.Inet4Address relayIp, byte[] clientMac) {
        super(transId, secs, android.net.dhcp.DhcpPacket.INADDR_ANY, android.net.dhcp.DhcpPacket.INADDR_ANY, nextIp, relayIp, clientMac, false);
    }

    public java.lang.String toString() {
        java.lang.String s = super.toString();
        return (s + " NAK, reason ") + (mMessage == null ? "(none)" : mMessage);
    }

    /**
     * Fills in a packet with the requested NAK attributes.
     */
    public java.nio.ByteBuffer buildPacket(int encap, short destUdp, short srcUdp) {
        java.nio.ByteBuffer result = java.nio.ByteBuffer.allocate(android.net.dhcp.DhcpPacket.MAX_LENGTH);
        java.net.Inet4Address destIp = mClientIp;
        java.net.Inet4Address srcIp = mYourIp;
        fillInPacket(encap, destIp, srcIp, destUdp, srcUdp, result, android.net.dhcp.DhcpPacket.DHCP_BOOTREPLY, mBroadcast);
        result.flip();
        return result;
    }

    /**
     * Adds the optional parameters to the client-generated NAK packet.
     */
    void finishPacket(java.nio.ByteBuffer buffer) {
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_NAK);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_SERVER_IDENTIFIER, mServerIdentifier);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_MESSAGE, mMessage);
        android.net.dhcp.DhcpPacket.addTlvEnd(buffer);
    }
}

