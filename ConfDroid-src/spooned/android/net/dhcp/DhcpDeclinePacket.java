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
 * This class implements the DHCP-DECLINE packet.
 */
class DhcpDeclinePacket extends android.net.dhcp.DhcpPacket {
    /**
     * Generates a DECLINE packet with the specified parameters.
     */
    DhcpDeclinePacket(int transId, short secs, java.net.Inet4Address clientIp, java.net.Inet4Address yourIp, java.net.Inet4Address nextIp, java.net.Inet4Address relayIp, byte[] clientMac) {
        super(transId, secs, clientIp, yourIp, nextIp, relayIp, clientMac, false);
    }

    public java.lang.String toString() {
        java.lang.String s = super.toString();
        return s + " DECLINE";
    }

    /**
     * Fills in a packet with the requested DECLINE attributes.
     */
    public java.nio.ByteBuffer buildPacket(int encap, short destUdp, short srcUdp) {
        java.nio.ByteBuffer result = java.nio.ByteBuffer.allocate(android.net.dhcp.DhcpPacket.MAX_LENGTH);
        fillInPacket(encap, mClientIp, mYourIp, destUdp, srcUdp, result, android.net.dhcp.DhcpPacket.DHCP_BOOTREQUEST, false);
        result.flip();
        return result;
    }

    /**
     * Adds optional parameters to the DECLINE packet.
     */
    void finishPacket(java.nio.ByteBuffer buffer) {
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE, android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_DECLINE);
        android.net.dhcp.DhcpPacket.addTlv(buffer, android.net.dhcp.DhcpPacket.DHCP_CLIENT_IDENTIFIER, getClientId());
        // RFC 2131 says we MUST NOT include our common client TLVs or the parameter request list.
        android.net.dhcp.DhcpPacket.addTlvEnd(buffer);
    }
}

