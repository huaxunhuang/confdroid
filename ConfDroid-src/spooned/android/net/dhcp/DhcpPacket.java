package android.net.dhcp;


/**
 * Defines basic data and operations needed to build and use packets for the
 * DHCP protocol.  Subclasses create the specific packets used at each
 * stage of the negotiation.
 */
abstract class DhcpPacket {
    protected static final java.lang.String TAG = "DhcpPacket";

    // dhcpcd has a minimum lease of 20 seconds, but DhcpStateMachine would refuse to wake up the
    // CPU for anything shorter than 5 minutes. For sanity's sake, this must be higher than the
    // DHCP client timeout.
    public static final int MINIMUM_LEASE = 60;

    public static final int INFINITE_LEASE = ((int) (0xffffffff));

    public static final java.net.Inet4Address INADDR_ANY = ((java.net.Inet4Address) (ANY));

    public static final java.net.Inet4Address INADDR_BROADCAST = ((java.net.Inet4Address) (ALL));

    public static final byte[] ETHER_BROADCAST = new byte[]{ ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)) };

    /**
     * Packet encapsulations.
     */
    public static final int ENCAP_L2 = 0;// EthernetII header included


    public static final int ENCAP_L3 = 1;// IP/UDP header included


    public static final int ENCAP_BOOTP = 2;// BOOTP contents only


    /**
     * Minimum length of a DHCP packet, excluding options, in the above encapsulations.
     */
    public static final int MIN_PACKET_LENGTH_BOOTP = 236;// See diagram in RFC 2131, section 2.


    public static final int MIN_PACKET_LENGTH_L3 = (android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_BOOTP + 20) + 8;

    public static final int MIN_PACKET_LENGTH_L2 = android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_L3 + 14;

    public static final int HWADDR_LEN = 16;

    public static final int MAX_OPTION_LEN = 255;

    /**
     * The minimum and maximum MTU that we are prepared to use. We set the minimum to the minimum
     * IPv6 MTU because the IPv6 stack enters unusual codepaths when the link MTU drops below 1280,
     * and does not recover if the MTU is brought above 1280 again. We set the maximum to 1500
     * because in general it is risky to assume that the hardware is able to send/receive packets
     * larger than 1500 bytes even if the network supports it.
     */
    private static final int MIN_MTU = 1280;

    private static final int MAX_MTU = 1500;

    /**
     * IP layer definitions.
     */
    private static final byte IP_TYPE_UDP = ((byte) (0x11));

    /**
     * IP: Version 4, Header Length 20 bytes
     */
    private static final byte IP_VERSION_HEADER_LEN = ((byte) (0x45));

    /**
     * IP: Flags 0, Fragment Offset 0, Don't Fragment
     */
    private static final short IP_FLAGS_OFFSET = ((short) (0x4000));

    /**
     * IP: TOS
     */
    private static final byte IP_TOS_LOWDELAY = ((byte) (0x10));

    /**
     * IP: TTL -- use default 64 from RFC1340
     */
    private static final byte IP_TTL = ((byte) (0x40));

    /**
     * The client DHCP port.
     */
    static final short DHCP_CLIENT = ((short) (68));

    /**
     * The server DHCP port.
     */
    static final short DHCP_SERVER = ((short) (67));

    /**
     * The message op code indicating a request from a client.
     */
    protected static final byte DHCP_BOOTREQUEST = ((byte) (1));

    /**
     * The message op code indicating a response from the server.
     */
    protected static final byte DHCP_BOOTREPLY = ((byte) (2));

    /**
     * The code type used to identify an Ethernet MAC address in the
     * Client-ID field.
     */
    protected static final byte CLIENT_ID_ETHER = ((byte) (1));

    /**
     * The maximum length of a packet that can be constructed.
     */
    protected static final int MAX_LENGTH = 1500;

    /**
     * The magic cookie that identifies this as a DHCP packet instead of BOOTP.
     */
    private static final int DHCP_MAGIC_COOKIE = 0x63825363;

    /**
     * DHCP Optional Type: DHCP Subnet Mask
     */
    protected static final byte DHCP_SUBNET_MASK = 1;

    protected java.net.Inet4Address mSubnetMask;

    /**
     * DHCP Optional Type: DHCP Router
     */
    protected static final byte DHCP_ROUTER = 3;

    protected java.util.List<java.net.Inet4Address> mGateways;

    /**
     * DHCP Optional Type: DHCP DNS Server
     */
    protected static final byte DHCP_DNS_SERVER = 6;

    protected java.util.List<java.net.Inet4Address> mDnsServers;

    /**
     * DHCP Optional Type: DHCP Host Name
     */
    protected static final byte DHCP_HOST_NAME = 12;

    protected java.lang.String mHostName;

    /**
     * DHCP Optional Type: DHCP DOMAIN NAME
     */
    protected static final byte DHCP_DOMAIN_NAME = 15;

    protected java.lang.String mDomainName;

    /**
     * DHCP Optional Type: DHCP Interface MTU
     */
    protected static final byte DHCP_MTU = 26;

    protected java.lang.Short mMtu;

    /**
     * DHCP Optional Type: DHCP BROADCAST ADDRESS
     */
    protected static final byte DHCP_BROADCAST_ADDRESS = 28;

    protected java.net.Inet4Address mBroadcastAddress;

    /**
     * DHCP Optional Type: Vendor specific information
     */
    protected static final byte DHCP_VENDOR_INFO = 43;

    protected java.lang.String mVendorInfo;

    /**
     * DHCP Optional Type: DHCP Requested IP Address
     */
    protected static final byte DHCP_REQUESTED_IP = 50;

    protected java.net.Inet4Address mRequestedIp;

    /**
     * DHCP Optional Type: DHCP Lease Time
     */
    protected static final byte DHCP_LEASE_TIME = 51;

    protected java.lang.Integer mLeaseTime;

    /**
     * DHCP Optional Type: DHCP Message Type
     */
    protected static final byte DHCP_MESSAGE_TYPE = 53;

    // the actual type values
    protected static final byte DHCP_MESSAGE_TYPE_DISCOVER = 1;

    protected static final byte DHCP_MESSAGE_TYPE_OFFER = 2;

    protected static final byte DHCP_MESSAGE_TYPE_REQUEST = 3;

    protected static final byte DHCP_MESSAGE_TYPE_DECLINE = 4;

    protected static final byte DHCP_MESSAGE_TYPE_ACK = 5;

    protected static final byte DHCP_MESSAGE_TYPE_NAK = 6;

    protected static final byte DHCP_MESSAGE_TYPE_INFORM = 8;

    /**
     * DHCP Optional Type: DHCP Server Identifier
     */
    protected static final byte DHCP_SERVER_IDENTIFIER = 54;

    protected java.net.Inet4Address mServerIdentifier;

    /**
     * DHCP Optional Type: DHCP Parameter List
     */
    protected static final byte DHCP_PARAMETER_LIST = 55;

    protected byte[] mRequestedParams;

    /**
     * DHCP Optional Type: DHCP MESSAGE
     */
    protected static final byte DHCP_MESSAGE = 56;

    protected java.lang.String mMessage;

    /**
     * DHCP Optional Type: Maximum DHCP Message Size
     */
    protected static final byte DHCP_MAX_MESSAGE_SIZE = 57;

    protected java.lang.Short mMaxMessageSize;

    /**
     * DHCP Optional Type: DHCP Renewal Time Value
     */
    protected static final byte DHCP_RENEWAL_TIME = 58;

    protected java.lang.Integer mT1;

    /**
     * DHCP Optional Type: Rebinding Time Value
     */
    protected static final byte DHCP_REBINDING_TIME = 59;

    protected java.lang.Integer mT2;

    /**
     * DHCP Optional Type: Vendor Class Identifier
     */
    protected static final byte DHCP_VENDOR_CLASS_ID = 60;

    protected java.lang.String mVendorId;

    /**
     * DHCP Optional Type: DHCP Client Identifier
     */
    protected static final byte DHCP_CLIENT_IDENTIFIER = 61;

    /**
     * DHCP zero-length option code: pad
     */
    protected static final byte DHCP_OPTION_PAD = 0x0;

    /**
     * DHCP zero-length option code: end of options
     */
    protected static final byte DHCP_OPTION_END = ((byte) (0xff));

    /**
     * The transaction identifier used in this particular DHCP negotiation
     */
    protected final int mTransId;

    /**
     * The seconds field in the BOOTP header. Per RFC, should be nonzero in client requests only.
     */
    protected final short mSecs;

    /**
     * The IP address of the client host.  This address is typically
     * proposed by the client (from an earlier DHCP negotiation) or
     * supplied by the server.
     */
    protected final java.net.Inet4Address mClientIp;

    protected final java.net.Inet4Address mYourIp;

    private final java.net.Inet4Address mNextIp;

    private final java.net.Inet4Address mRelayIp;

    /**
     * Does the client request a broadcast response?
     */
    protected boolean mBroadcast;

    /**
     * The six-octet MAC of the client.
     */
    protected final byte[] mClientMac;

    /**
     * Asks the packet object to create a ByteBuffer serialization of
     * the packet for transmission.
     */
    public abstract java.nio.ByteBuffer buildPacket(int encap, short destUdp, short srcUdp);

    /**
     * Allows the concrete class to fill in packet-type-specific details,
     * typically optional parameters at the end of the packet.
     */
    abstract void finishPacket(java.nio.ByteBuffer buffer);

    // Set in unit tests, to ensure that the test does not break when run on different devices and
    // on different releases.
    static java.lang.String testOverrideVendorId = null;

    static java.lang.String testOverrideHostname = null;

    protected DhcpPacket(int transId, short secs, java.net.Inet4Address clientIp, java.net.Inet4Address yourIp, java.net.Inet4Address nextIp, java.net.Inet4Address relayIp, byte[] clientMac, boolean broadcast) {
        mTransId = transId;
        mSecs = secs;
        mClientIp = clientIp;
        mYourIp = yourIp;
        mNextIp = nextIp;
        mRelayIp = relayIp;
        mClientMac = clientMac;
        mBroadcast = broadcast;
    }

    /**
     * Returns the transaction ID.
     */
    public int getTransactionId() {
        return mTransId;
    }

    /**
     * Returns the client MAC.
     */
    public byte[] getClientMac() {
        return mClientMac;
    }

    /**
     * Returns the client ID. This follows RFC 2132 and is based on the hardware address.
     */
    public byte[] getClientId() {
        byte[] clientId = new byte[mClientMac.length + 1];
        clientId[0] = android.net.dhcp.DhcpPacket.CLIENT_ID_ETHER;
        java.lang.System.arraycopy(mClientMac, 0, clientId, 1, mClientMac.length);
        return clientId;
    }

    /**
     * Creates a new L3 packet (including IP header) containing the
     * DHCP udp packet.  This method relies upon the delegated method
     * finishPacket() to insert the per-packet contents.
     */
    protected void fillInPacket(int encap, java.net.Inet4Address destIp, java.net.Inet4Address srcIp, short destUdp, short srcUdp, java.nio.ByteBuffer buf, byte requestCode, boolean broadcast) {
        byte[] destIpArray = destIp.getAddress();
        byte[] srcIpArray = srcIp.getAddress();
        int ipHeaderOffset = 0;
        int ipLengthOffset = 0;
        int ipChecksumOffset = 0;
        int endIpHeader = 0;
        int udpHeaderOffset = 0;
        int udpLengthOffset = 0;
        int udpChecksumOffset = 0;
        buf.clear();
        buf.order(java.nio.ByteOrder.BIG_ENDIAN);
        if (encap == android.net.dhcp.DhcpPacket.ENCAP_L2) {
            buf.put(android.net.dhcp.DhcpPacket.ETHER_BROADCAST);
            buf.put(mClientMac);
            buf.putShort(((short) (android.system.OsConstants.ETH_P_IP)));
        }
        // if a full IP packet needs to be generated, put the IP & UDP
        // headers in place, and pre-populate with artificial values
        // needed to seed the IP checksum.
        if (encap <= android.net.dhcp.DhcpPacket.ENCAP_L3) {
            ipHeaderOffset = buf.position();
            buf.put(android.net.dhcp.DhcpPacket.IP_VERSION_HEADER_LEN);
            buf.put(android.net.dhcp.DhcpPacket.IP_TOS_LOWDELAY);// tos: IPTOS_LOWDELAY

            ipLengthOffset = buf.position();
            buf.putShort(((short) (0)));// length

            buf.putShort(((short) (0)));// id

            buf.putShort(android.net.dhcp.DhcpPacket.IP_FLAGS_OFFSET);// ip offset: don't fragment

            buf.put(android.net.dhcp.DhcpPacket.IP_TTL);// TTL: use default 64 from RFC1340

            buf.put(android.net.dhcp.DhcpPacket.IP_TYPE_UDP);
            ipChecksumOffset = buf.position();
            buf.putShort(((short) (0)));// checksum

            buf.put(srcIpArray);
            buf.put(destIpArray);
            endIpHeader = buf.position();
            // UDP header
            udpHeaderOffset = buf.position();
            buf.putShort(srcUdp);
            buf.putShort(destUdp);
            udpLengthOffset = buf.position();
            buf.putShort(((short) (0)));// length

            udpChecksumOffset = buf.position();
            buf.putShort(((short) (0)));// UDP checksum -- initially zero

        }
        // DHCP payload
        buf.put(requestCode);
        buf.put(((byte) (1)));// Hardware Type: Ethernet

        buf.put(((byte) (mClientMac.length)));// Hardware Address Length

        buf.put(((byte) (0)));// Hop Count

        buf.putInt(mTransId);// Transaction ID

        buf.putShort(mSecs);// Elapsed Seconds

        if (broadcast) {
            buf.putShort(((short) (0x8000)));// Flags

        } else {
            buf.putShort(((short) (0x0)));// Flags

        }
        buf.put(mClientIp.getAddress());
        buf.put(mYourIp.getAddress());
        buf.put(mNextIp.getAddress());
        buf.put(mRelayIp.getAddress());
        buf.put(mClientMac);
        buf.position(((buf.position() + (android.net.dhcp.DhcpPacket.HWADDR_LEN - mClientMac.length))// pad addr to 16 bytes
         + 64)// empty server host name (64 bytes)
         + 128);// empty boot file name (128 bytes)

        buf.putInt(android.net.dhcp.DhcpPacket.DHCP_MAGIC_COOKIE);// magic number

        finishPacket(buf);
        // round up to an even number of octets
        if ((buf.position() & 1) == 1) {
            buf.put(((byte) (0)));
        }
        // If an IP packet is being built, the IP & UDP checksums must be
        // computed.
        if (encap <= android.net.dhcp.DhcpPacket.ENCAP_L3) {
            // fix UDP header: insert length
            short udpLen = ((short) (buf.position() - udpHeaderOffset));
            buf.putShort(udpLengthOffset, udpLen);
            // fix UDP header: checksum
            // checksum for UDP at udpChecksumOffset
            int udpSeed = 0;
            // apply IPv4 pseudo-header.  Read IP address src and destination
            // values from the IP header and accumulate checksum.
            udpSeed += android.net.dhcp.DhcpPacket.intAbs(buf.getShort(ipChecksumOffset + 2));
            udpSeed += android.net.dhcp.DhcpPacket.intAbs(buf.getShort(ipChecksumOffset + 4));
            udpSeed += android.net.dhcp.DhcpPacket.intAbs(buf.getShort(ipChecksumOffset + 6));
            udpSeed += android.net.dhcp.DhcpPacket.intAbs(buf.getShort(ipChecksumOffset + 8));
            // accumulate extra data for the pseudo-header
            udpSeed += android.net.dhcp.DhcpPacket.IP_TYPE_UDP;
            udpSeed += udpLen;
            // and compute UDP checksum
            buf.putShort(udpChecksumOffset, ((short) (checksum(buf, udpSeed, udpHeaderOffset, buf.position()))));
            // fix IP header: insert length
            buf.putShort(ipLengthOffset, ((short) (buf.position() - ipHeaderOffset)));
            // fixup IP-header checksum
            buf.putShort(ipChecksumOffset, ((short) (checksum(buf, 0, ipHeaderOffset, endIpHeader))));
        }
    }

    /**
     * Converts a signed short value to an unsigned int value.  Needed
     * because Java does not have unsigned types.
     */
    private static int intAbs(short v) {
        return v & 0xffff;
    }

    /**
     * Performs an IP checksum (used in IP header and across UDP
     * payload) on the specified portion of a ByteBuffer.  The seed
     * allows the checksum to commence with a specified value.
     */
    private int checksum(java.nio.ByteBuffer buf, int seed, int start, int end) {
        int sum = seed;
        int bufPosition = buf.position();
        // set position of original ByteBuffer, so that the ShortBuffer
        // will be correctly initialized
        buf.position(start);
        java.nio.ShortBuffer shortBuf = buf.asShortBuffer();
        // re-set ByteBuffer position
        buf.position(bufPosition);
        short[] shortArray = new short[(end - start) / 2];
        shortBuf.get(shortArray);
        for (short s : shortArray) {
            sum += android.net.dhcp.DhcpPacket.intAbs(s);
        }
        start += shortArray.length * 2;
        // see if a singleton byte remains
        if (end != start) {
            short b = buf.get(start);
            // make it unsigned
            if (b < 0) {
                b += 256;
            }
            sum += b * 256;
        }
        sum = ((sum >> 16) & 0xffff) + (sum & 0xffff);
        sum = (sum + ((sum >> 16) & 0xffff)) & 0xffff;
        int negated = ~sum;
        return android.net.dhcp.DhcpPacket.intAbs(((short) (negated)));
    }

    /**
     * Adds an optional parameter containing a single byte value.
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, byte value) {
        buf.put(type);
        buf.put(((byte) (1)));
        buf.put(value);
    }

    /**
     * Adds an optional parameter containing an array of bytes.
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, byte[] payload) {
        if (payload != null) {
            if (payload.length > android.net.dhcp.DhcpPacket.MAX_OPTION_LEN) {
                throw new java.lang.IllegalArgumentException((("DHCP option too long: " + payload.length) + " vs. ") + android.net.dhcp.DhcpPacket.MAX_OPTION_LEN);
            }
            buf.put(type);
            buf.put(((byte) (payload.length)));
            buf.put(payload);
        }
    }

    /**
     * Adds an optional parameter containing an IP address.
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, java.net.Inet4Address addr) {
        if (addr != null) {
            android.net.dhcp.DhcpPacket.addTlv(buf, type, addr.getAddress());
        }
    }

    /**
     * Adds an optional parameter containing a list of IP addresses.
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, java.util.List<java.net.Inet4Address> addrs) {
        if ((addrs == null) || (addrs.size() == 0))
            return;

        int optionLen = 4 * addrs.size();
        if (optionLen > android.net.dhcp.DhcpPacket.MAX_OPTION_LEN) {
            throw new java.lang.IllegalArgumentException((("DHCP option too long: " + optionLen) + " vs. ") + android.net.dhcp.DhcpPacket.MAX_OPTION_LEN);
        }
        buf.put(type);
        buf.put(((byte) (optionLen)));
        for (java.net.Inet4Address addr : addrs) {
            buf.put(addr.getAddress());
        }
    }

    /**
     * Adds an optional parameter containing a short integer
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, java.lang.Short value) {
        if (value != null) {
            buf.put(type);
            buf.put(((byte) (2)));
            buf.putShort(value.shortValue());
        }
    }

    /**
     * Adds an optional parameter containing a simple integer
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, java.lang.Integer value) {
        if (value != null) {
            buf.put(type);
            buf.put(((byte) (4)));
            buf.putInt(value.intValue());
        }
    }

    /**
     * Adds an optional parameter containing an ASCII string.
     */
    protected static void addTlv(java.nio.ByteBuffer buf, byte type, java.lang.String str) {
        try {
            android.net.dhcp.DhcpPacket.addTlv(buf, type, str.getBytes("US-ASCII"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new java.lang.IllegalArgumentException("String is not US-ASCII: " + str);
        }
    }

    /**
     * Adds the special end-of-optional-parameters indicator.
     */
    protected static void addTlvEnd(java.nio.ByteBuffer buf) {
        buf.put(((byte) (0xff)));
    }

    private java.lang.String getVendorId() {
        if (android.net.dhcp.DhcpPacket.testOverrideVendorId != null)
            return android.net.dhcp.DhcpPacket.testOverrideVendorId;

        return "android-dhcp-" + android.os.Build.VERSION.RELEASE;
    }

    private java.lang.String getHostname() {
        if (android.net.dhcp.DhcpPacket.testOverrideHostname != null)
            return android.net.dhcp.DhcpPacket.testOverrideHostname;

        return android.os.SystemProperties.get("net.hostname");
    }

    /**
     * Adds common client TLVs.
     *
     * TODO: Does this belong here? The alternative would be to modify all the buildXyzPacket
     * methods to take them.
     */
    protected void addCommonClientTlvs(java.nio.ByteBuffer buf) {
        android.net.dhcp.DhcpPacket.addTlv(buf, android.net.dhcp.DhcpPacket.DHCP_MAX_MESSAGE_SIZE, ((short) (android.net.dhcp.DhcpPacket.MAX_LENGTH)));
        android.net.dhcp.DhcpPacket.addTlv(buf, android.net.dhcp.DhcpPacket.DHCP_VENDOR_CLASS_ID, getVendorId());
        android.net.dhcp.DhcpPacket.addTlv(buf, android.net.dhcp.DhcpPacket.DHCP_HOST_NAME, getHostname());
    }

    /**
     * Converts a MAC from an array of octets to an ASCII string.
     */
    public static java.lang.String macToString(byte[] mac) {
        java.lang.String macAddr = "";
        for (int i = 0; i < mac.length; i++) {
            java.lang.String hexString = "0" + java.lang.Integer.toHexString(mac[i]);
            // substring operation grabs the last 2 digits: this
            // allows signed bytes to be converted correctly.
            macAddr += hexString.substring(hexString.length() - 2);
            if (i != (mac.length - 1)) {
                macAddr += ":";
            }
        }
        return macAddr;
    }

    public java.lang.String toString() {
        java.lang.String macAddr = android.net.dhcp.DhcpPacket.macToString(mClientMac);
        return macAddr;
    }

    /**
     * Reads a four-octet value from a ByteBuffer and construct
     * an IPv4 address from that value.
     */
    private static java.net.Inet4Address readIpAddress(java.nio.ByteBuffer packet) {
        java.net.Inet4Address result = null;
        byte[] ipAddr = new byte[4];
        packet.get(ipAddr);
        try {
            result = ((java.net.Inet4Address) (java.net.Inet4Address.getByAddress(ipAddr)));
        } catch (java.net.UnknownHostException ex) {
            // ipAddr is numeric, so this should not be
            // triggered.  However, if it is, just nullify
            result = null;
        }
        return result;
    }

    /**
     * Reads a string of specified length from the buffer.
     */
    private static java.lang.String readAsciiString(java.nio.ByteBuffer buf, int byteCount, boolean nullOk) {
        byte[] bytes = new byte[byteCount];
        buf.get(bytes);
        int length = bytes.length;
        if (!nullOk) {
            // Stop at the first null byte. This is because some DHCP options (e.g., the domain
            // name) are passed to netd via FrameworkListener, which refuses arguments containing
            // null bytes. We don't do this by default because vendorInfo is an opaque string which
            // could in theory contain null bytes.
            for (length = 0; length < bytes.length; length++) {
                if (bytes[length] == 0) {
                    break;
                }
            }
        }
        return new java.lang.String(bytes, 0, length, java.nio.charset.StandardCharsets.US_ASCII);
    }

    private static boolean isPacketToOrFromClient(short udpSrcPort, short udpDstPort) {
        return (udpSrcPort == android.net.dhcp.DhcpPacket.DHCP_CLIENT) || (udpDstPort == android.net.dhcp.DhcpPacket.DHCP_CLIENT);
    }

    private static boolean isPacketServerToServer(short udpSrcPort, short udpDstPort) {
        return (udpSrcPort == android.net.dhcp.DhcpPacket.DHCP_SERVER) && (udpDstPort == android.net.dhcp.DhcpPacket.DHCP_SERVER);
    }

    public static class ParseException extends java.lang.Exception {
        public final int errorCode;

        public ParseException(int errorCode, java.lang.String msg, java.lang.Object... args) {
            super(java.lang.String.format(msg, args));
            this.errorCode = errorCode;
        }
    }

    /**
     * Creates a concrete DhcpPacket from the supplied ByteBuffer.  The
     * buffer may have an L2 encapsulation (which is the full EthernetII
     * format starting with the source-address MAC) or an L3 encapsulation
     * (which starts with the IP header).
     * <br>
     * A subset of the optional parameters are parsed and are stored
     * in object fields.
     */
    @com.android.internal.annotations.VisibleForTesting
    static android.net.dhcp.DhcpPacket decodeFullPacket(java.nio.ByteBuffer packet, int pktType) throws android.net.dhcp.DhcpPacket.ParseException {
        // bootp parameters
        int transactionId;
        short secs;
        java.net.Inet4Address clientIp;
        java.net.Inet4Address yourIp;
        java.net.Inet4Address nextIp;
        java.net.Inet4Address relayIp;
        byte[] clientMac;
        java.util.List<java.net.Inet4Address> dnsServers = new java.util.ArrayList<>();
        java.util.List<java.net.Inet4Address> gateways = new java.util.ArrayList<>();// aka router

        java.net.Inet4Address serverIdentifier = null;
        java.net.Inet4Address netMask = null;
        java.lang.String message = null;
        java.lang.String vendorId = null;
        java.lang.String vendorInfo = null;
        byte[] expectedParams = null;
        java.lang.String hostName = null;
        java.lang.String domainName = null;
        java.net.Inet4Address ipSrc = null;
        java.net.Inet4Address ipDst = null;
        java.net.Inet4Address bcAddr = null;
        java.net.Inet4Address requestedIp = null;
        // The following are all unsigned integers. Internally we store them as signed integers of
        // the same length because that way we're guaranteed that they can't be out of the range of
        // the unsigned field in the packet. Callers wanting to pass in an unsigned value will need
        // to cast it.
        java.lang.Short mtu = null;
        java.lang.Short maxMessageSize = null;
        java.lang.Integer leaseTime = null;
        java.lang.Integer T1 = null;
        java.lang.Integer T2 = null;
        // dhcp options
        byte dhcpType = ((byte) (0xff));
        packet.order(java.nio.ByteOrder.BIG_ENDIAN);
        // check to see if we need to parse L2, IP, and UDP encaps
        if (pktType == android.net.dhcp.DhcpPacket.ENCAP_L2) {
            if (packet.remaining() < android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_L2) {
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L2_TOO_SHORT, "L2 packet too short, %d < %d", packet.remaining(), android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_L2);
            }
            byte[] l2dst = new byte[6];
            byte[] l2src = new byte[6];
            packet.get(l2dst);
            packet.get(l2src);
            short l2type = packet.getShort();
            if (l2type != android.system.OsConstants.ETH_P_IP) {
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L2_WRONG_ETH_TYPE, "Unexpected L2 type 0x%04x, expected 0x%04x", l2type, android.system.OsConstants.ETH_P_IP);
            }
        }
        if (pktType <= android.net.dhcp.DhcpPacket.ENCAP_L3) {
            if (packet.remaining() < android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_L3) {
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L3_TOO_SHORT, "L3 packet too short, %d < %d", packet.remaining(), android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_L3);
            }
            byte ipTypeAndLength = packet.get();
            int ipVersion = (ipTypeAndLength & 0xf0) >> 4;
            if (ipVersion != 4) {
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L3_NOT_IPV4, "Invalid IP version %d", ipVersion);
            }
            // System.out.println("ipType is " + ipType);
            byte ipDiffServicesField = packet.get();
            short ipTotalLength = packet.getShort();
            short ipIdentification = packet.getShort();
            byte ipFlags = packet.get();
            byte ipFragOffset = packet.get();
            byte ipTTL = packet.get();
            byte ipProto = packet.get();
            short ipChksm = packet.getShort();
            ipSrc = android.net.dhcp.DhcpPacket.readIpAddress(packet);
            ipDst = android.net.dhcp.DhcpPacket.readIpAddress(packet);
            if (ipProto != android.net.dhcp.DhcpPacket.IP_TYPE_UDP) {
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L4_NOT_UDP, "Protocol not UDP: %d", ipProto);
            }
            // Skip options. This cannot cause us to read beyond the end of the buffer because the
            // IPv4 header cannot be more than (0x0f * 4) = 60 bytes long, and that is less than
            // MIN_PACKET_LENGTH_L3.
            int optionWords = (ipTypeAndLength & 0xf) - 5;
            for (int i = 0; i < optionWords; i++) {
                packet.getInt();
            }
            // assume UDP
            short udpSrcPort = packet.getShort();
            short udpDstPort = packet.getShort();
            short udpLen = packet.getShort();
            short udpChkSum = packet.getShort();
            // Only accept packets to or from the well-known client port (expressly permitting
            // packets from ports other than the well-known server port; http://b/24687559), and
            // server-to-server packets, e.g. for relays.
            if ((!android.net.dhcp.DhcpPacket.isPacketToOrFromClient(udpSrcPort, udpDstPort)) && (!android.net.dhcp.DhcpPacket.isPacketServerToServer(udpSrcPort, udpDstPort))) {
                // This should almost never happen because we use SO_ATTACH_FILTER on the packet
                // socket to drop packets that don't have the right source ports. However, it's
                // possible that a packet arrives between when the socket is bound and when the
                // filter is set. http://b/26696823 .
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L4_WRONG_PORT, "Unexpected UDP ports %d->%d", udpSrcPort, udpDstPort);
            }
        }
        // We need to check the length even for ENCAP_L3 because the IPv4 header is variable-length.
        if ((pktType > android.net.dhcp.DhcpPacket.ENCAP_BOOTP) || (packet.remaining() < android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_BOOTP)) {
            throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.BOOTP_TOO_SHORT, "Invalid type or BOOTP packet too short, %d < %d", packet.remaining(), android.net.dhcp.DhcpPacket.MIN_PACKET_LENGTH_BOOTP);
        }
        byte type = packet.get();
        byte hwType = packet.get();
        int addrLen = packet.get() & 0xff;
        byte hops = packet.get();
        transactionId = packet.getInt();
        secs = packet.getShort();
        short bootpFlags = packet.getShort();
        boolean broadcast = (bootpFlags & 0x8000) != 0;
        byte[] ipv4addr = new byte[4];
        try {
            packet.get(ipv4addr);
            clientIp = ((java.net.Inet4Address) (java.net.Inet4Address.getByAddress(ipv4addr)));
            packet.get(ipv4addr);
            yourIp = ((java.net.Inet4Address) (java.net.Inet4Address.getByAddress(ipv4addr)));
            packet.get(ipv4addr);
            nextIp = ((java.net.Inet4Address) (java.net.Inet4Address.getByAddress(ipv4addr)));
            packet.get(ipv4addr);
            relayIp = ((java.net.Inet4Address) (java.net.Inet4Address.getByAddress(ipv4addr)));
        } catch (java.net.UnknownHostException ex) {
            throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.L3_INVALID_IP, "Invalid IPv4 address: %s", java.util.Arrays.toString(ipv4addr));
        }
        // Some DHCP servers have been known to announce invalid client hardware address values such
        // as 0xff. The legacy DHCP client accepted these becuause it does not check the length at
        // all but only checks that the interface MAC address matches the first bytes of the address
        // in the packets. We're a bit stricter: if the length is obviously invalid (i.e., bigger
        // than the size of the field), we fudge it to 6 (Ethernet). http://b/23725795
        // TODO: evaluate whether to make this test more liberal.
        if (addrLen > android.net.dhcp.DhcpPacket.HWADDR_LEN) {
            addrLen = android.net.dhcp.DhcpPacket.ETHER_BROADCAST.length;
        }
        clientMac = new byte[addrLen];
        packet.get(clientMac);
        // skip over address padding (16 octets allocated)
        packet.position(((packet.position() + (16 - addrLen)) + 64)// skip server host name (64 chars)
         + 128);// skip boot file name (128 chars)

        // Ensure this is a DHCP packet with a magic cookie, and not BOOTP. http://b/31850211
        if (packet.remaining() < 4) {
            throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.DHCP_NO_COOKIE, "not a DHCP message");
        }
        int dhcpMagicCookie = packet.getInt();
        if (dhcpMagicCookie != android.net.dhcp.DhcpPacket.DHCP_MAGIC_COOKIE) {
            throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.DHCP_BAD_MAGIC_COOKIE, "Bad magic cookie 0x%08x, should be 0x%08x", dhcpMagicCookie, android.net.dhcp.DhcpPacket.DHCP_MAGIC_COOKIE);
        }
        // parse options
        boolean notFinishedOptions = true;
        while ((packet.position() < packet.limit()) && notFinishedOptions) {
            final byte optionType = packet.get();// cannot underflow because position < limit

            try {
                if (optionType == android.net.dhcp.DhcpPacket.DHCP_OPTION_END) {
                    notFinishedOptions = false;
                } else
                    if (optionType == android.net.dhcp.DhcpPacket.DHCP_OPTION_PAD) {
                        // The pad option doesn't have a length field. Nothing to do.
                    } else {
                        int optionLen = packet.get() & 0xff;
                        int expectedLen = 0;
                        switch (optionType) {
                            case android.net.dhcp.DhcpPacket.DHCP_SUBNET_MASK :
                                netMask = android.net.dhcp.DhcpPacket.readIpAddress(packet);
                                expectedLen = 4;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_ROUTER :
                                for (expectedLen = 0; expectedLen < optionLen; expectedLen += 4) {
                                    gateways.add(android.net.dhcp.DhcpPacket.readIpAddress(packet));
                                }
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_DNS_SERVER :
                                for (expectedLen = 0; expectedLen < optionLen; expectedLen += 4) {
                                    dnsServers.add(android.net.dhcp.DhcpPacket.readIpAddress(packet));
                                }
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_HOST_NAME :
                                expectedLen = optionLen;
                                hostName = android.net.dhcp.DhcpPacket.readAsciiString(packet, optionLen, false);
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_MTU :
                                expectedLen = 2;
                                mtu = packet.getShort();
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_DOMAIN_NAME :
                                expectedLen = optionLen;
                                domainName = android.net.dhcp.DhcpPacket.readAsciiString(packet, optionLen, false);
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_BROADCAST_ADDRESS :
                                bcAddr = android.net.dhcp.DhcpPacket.readIpAddress(packet);
                                expectedLen = 4;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_REQUESTED_IP :
                                requestedIp = android.net.dhcp.DhcpPacket.readIpAddress(packet);
                                expectedLen = 4;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_LEASE_TIME :
                                leaseTime = java.lang.Integer.valueOf(packet.getInt());
                                expectedLen = 4;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE :
                                dhcpType = packet.get();
                                expectedLen = 1;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_SERVER_IDENTIFIER :
                                serverIdentifier = android.net.dhcp.DhcpPacket.readIpAddress(packet);
                                expectedLen = 4;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_PARAMETER_LIST :
                                expectedParams = new byte[optionLen];
                                packet.get(expectedParams);
                                expectedLen = optionLen;
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE :
                                expectedLen = optionLen;
                                message = android.net.dhcp.DhcpPacket.readAsciiString(packet, optionLen, false);
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_MAX_MESSAGE_SIZE :
                                expectedLen = 2;
                                maxMessageSize = java.lang.Short.valueOf(packet.getShort());
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_RENEWAL_TIME :
                                expectedLen = 4;
                                T1 = java.lang.Integer.valueOf(packet.getInt());
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_REBINDING_TIME :
                                expectedLen = 4;
                                T2 = java.lang.Integer.valueOf(packet.getInt());
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_VENDOR_CLASS_ID :
                                expectedLen = optionLen;
                                // Embedded nulls are safe as this does not get passed to netd.
                                vendorId = android.net.dhcp.DhcpPacket.readAsciiString(packet, optionLen, true);
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_CLIENT_IDENTIFIER :
                                {
                                    // Client identifier
                                    byte[] id = new byte[optionLen];
                                    packet.get(id);
                                    expectedLen = optionLen;
                                }
                                break;
                            case android.net.dhcp.DhcpPacket.DHCP_VENDOR_INFO :
                                expectedLen = optionLen;
                                // Embedded nulls are safe as this does not get passed to netd.
                                vendorInfo = android.net.dhcp.DhcpPacket.readAsciiString(packet, optionLen, true);
                                break;
                            default :
                                // ignore any other parameters
                                for (int i = 0; i < optionLen; i++) {
                                    expectedLen++;
                                    byte throwaway = packet.get();
                                }
                        }
                        if (expectedLen != optionLen) {
                            final int errorCode = android.net.metrics.DhcpErrorEvent.errorCodeWithOption(android.net.metrics.DhcpErrorEvent.DHCP_INVALID_OPTION_LENGTH, optionType);
                            throw new android.net.dhcp.DhcpPacket.ParseException(errorCode, "Invalid length %d for option %d, expected %d", optionLen, optionType, expectedLen);
                        }
                    }

            } catch (java.nio.BufferUnderflowException e) {
                final int errorCode = android.net.metrics.DhcpErrorEvent.errorCodeWithOption(android.net.metrics.DhcpErrorEvent.BUFFER_UNDERFLOW, optionType);
                throw new android.net.dhcp.DhcpPacket.ParseException(errorCode, "BufferUnderflowException");
            }
        } 
        android.net.dhcp.DhcpPacket newPacket;
        switch (dhcpType) {
            case ((byte) (0xff)) :
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.DHCP_NO_MSG_TYPE, "No DHCP message type option");
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_DISCOVER :
                newPacket = new android.net.dhcp.DhcpDiscoverPacket(transactionId, secs, clientMac, broadcast);
                break;
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_OFFER :
                newPacket = new android.net.dhcp.DhcpOfferPacket(transactionId, secs, broadcast, ipSrc, clientIp, yourIp, clientMac);
                break;
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_REQUEST :
                newPacket = new android.net.dhcp.DhcpRequestPacket(transactionId, secs, clientIp, clientMac, broadcast);
                break;
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_DECLINE :
                newPacket = new android.net.dhcp.DhcpDeclinePacket(transactionId, secs, clientIp, yourIp, nextIp, relayIp, clientMac);
                break;
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_ACK :
                newPacket = new android.net.dhcp.DhcpAckPacket(transactionId, secs, broadcast, ipSrc, clientIp, yourIp, clientMac);
                break;
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_NAK :
                newPacket = new android.net.dhcp.DhcpNakPacket(transactionId, secs, clientIp, yourIp, nextIp, relayIp, clientMac);
                break;
            case android.net.dhcp.DhcpPacket.DHCP_MESSAGE_TYPE_INFORM :
                newPacket = new android.net.dhcp.DhcpInformPacket(transactionId, secs, clientIp, yourIp, nextIp, relayIp, clientMac);
                break;
            default :
                throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.DHCP_UNKNOWN_MSG_TYPE, "Unimplemented DHCP type %d", dhcpType);
        }
        newPacket.mBroadcastAddress = bcAddr;
        newPacket.mDnsServers = dnsServers;
        newPacket.mDomainName = domainName;
        newPacket.mGateways = gateways;
        newPacket.mHostName = hostName;
        newPacket.mLeaseTime = leaseTime;
        newPacket.mMessage = message;
        newPacket.mMtu = mtu;
        newPacket.mRequestedIp = requestedIp;
        newPacket.mRequestedParams = expectedParams;
        newPacket.mServerIdentifier = serverIdentifier;
        newPacket.mSubnetMask = netMask;
        newPacket.mMaxMessageSize = maxMessageSize;
        newPacket.mT1 = T1;
        newPacket.mT2 = T2;
        newPacket.mVendorId = vendorId;
        newPacket.mVendorInfo = vendorInfo;
        return newPacket;
    }

    /**
     * Parse a packet from an array of bytes, stopping at the given length.
     */
    public static android.net.dhcp.DhcpPacket decodeFullPacket(byte[] packet, int length, int pktType) throws android.net.dhcp.DhcpPacket.ParseException {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(packet, 0, length).order(java.nio.ByteOrder.BIG_ENDIAN);
        try {
            return android.net.dhcp.DhcpPacket.decodeFullPacket(buffer, pktType);
        } catch (android.net.dhcp.DhcpPacket.ParseException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new android.net.dhcp.DhcpPacket.ParseException(android.net.metrics.DhcpErrorEvent.PARSING_ERROR, e.getMessage());
        }
    }

    /**
     * Construct a DhcpResults object from a DHCP reply packet.
     */
    public android.net.DhcpResults toDhcpResults() {
        java.net.Inet4Address ipAddress = mYourIp;
        if (ipAddress.equals(ANY)) {
            ipAddress = mClientIp;
            if (ipAddress.equals(ANY)) {
                return null;
            }
        }
        int prefixLength;
        if (mSubnetMask != null) {
            try {
                prefixLength = android.net.NetworkUtils.netmaskToPrefixLength(mSubnetMask);
            } catch (java.lang.IllegalArgumentException e) {
                // Non-contiguous netmask.
                return null;
            }
        } else {
            prefixLength = android.net.NetworkUtils.getImplicitNetmask(ipAddress);
        }
        android.net.DhcpResults results = new android.net.DhcpResults();
        try {
            results.ipAddress = new android.net.LinkAddress(ipAddress, prefixLength);
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
        if (mGateways.size() > 0) {
            results.gateway = mGateways.get(0);
        }
        results.dnsServers.addAll(mDnsServers);
        results.domains = mDomainName;
        results.serverAddress = mServerIdentifier;
        results.vendorInfo = mVendorInfo;
        results.leaseDuration = (mLeaseTime != null) ? mLeaseTime : android.net.dhcp.DhcpPacket.INFINITE_LEASE;
        results.mtu = (((mMtu != null) && (android.net.dhcp.DhcpPacket.MIN_MTU <= mMtu)) && (mMtu <= android.net.dhcp.DhcpPacket.MAX_MTU)) ? mMtu : 0;
        return results;
    }

    /**
     * Returns the parsed lease time, in milliseconds, or 0 for infinite.
     */
    public long getLeaseTimeMillis() {
        // dhcpcd treats the lack of a lease time option as an infinite lease.
        if ((mLeaseTime == null) || (mLeaseTime == android.net.dhcp.DhcpPacket.INFINITE_LEASE)) {
            return 0;
        } else
            if ((0 <= mLeaseTime) && (mLeaseTime < android.net.dhcp.DhcpPacket.MINIMUM_LEASE)) {
                return android.net.dhcp.DhcpPacket.MINIMUM_LEASE * 1000;
            } else {
                return (mLeaseTime & 0xffffffffL) * 1000;
            }

    }

    /**
     * Builds a DHCP-DISCOVER packet from the required specified
     * parameters.
     */
    public static java.nio.ByteBuffer buildDiscoverPacket(int encap, int transactionId, short secs, byte[] clientMac, boolean broadcast, byte[] expectedParams) {
        android.net.dhcp.DhcpPacket pkt = new android.net.dhcp.DhcpDiscoverPacket(transactionId, secs, clientMac, broadcast);
        pkt.mRequestedParams = expectedParams;
        return pkt.buildPacket(encap, android.net.dhcp.DhcpPacket.DHCP_SERVER, android.net.dhcp.DhcpPacket.DHCP_CLIENT);
    }

    /**
     * Builds a DHCP-OFFER packet from the required specified
     * parameters.
     */
    public static java.nio.ByteBuffer buildOfferPacket(int encap, int transactionId, boolean broadcast, java.net.Inet4Address serverIpAddr, java.net.Inet4Address clientIpAddr, byte[] mac, java.lang.Integer timeout, java.net.Inet4Address netMask, java.net.Inet4Address bcAddr, java.util.List<java.net.Inet4Address> gateways, java.util.List<java.net.Inet4Address> dnsServers, java.net.Inet4Address dhcpServerIdentifier, java.lang.String domainName) {
        android.net.dhcp.DhcpPacket pkt = new android.net.dhcp.DhcpOfferPacket(transactionId, ((short) (0)), broadcast, serverIpAddr, android.net.dhcp.DhcpPacket.INADDR_ANY, clientIpAddr, mac);
        pkt.mGateways = gateways;
        pkt.mDnsServers = dnsServers;
        pkt.mLeaseTime = timeout;
        pkt.mDomainName = domainName;
        pkt.mServerIdentifier = dhcpServerIdentifier;
        pkt.mSubnetMask = netMask;
        pkt.mBroadcastAddress = bcAddr;
        return pkt.buildPacket(encap, android.net.dhcp.DhcpPacket.DHCP_CLIENT, android.net.dhcp.DhcpPacket.DHCP_SERVER);
    }

    /**
     * Builds a DHCP-ACK packet from the required specified parameters.
     */
    public static java.nio.ByteBuffer buildAckPacket(int encap, int transactionId, boolean broadcast, java.net.Inet4Address serverIpAddr, java.net.Inet4Address clientIpAddr, byte[] mac, java.lang.Integer timeout, java.net.Inet4Address netMask, java.net.Inet4Address bcAddr, java.util.List<java.net.Inet4Address> gateways, java.util.List<java.net.Inet4Address> dnsServers, java.net.Inet4Address dhcpServerIdentifier, java.lang.String domainName) {
        android.net.dhcp.DhcpPacket pkt = new android.net.dhcp.DhcpAckPacket(transactionId, ((short) (0)), broadcast, serverIpAddr, android.net.dhcp.DhcpPacket.INADDR_ANY, clientIpAddr, mac);
        pkt.mGateways = gateways;
        pkt.mDnsServers = dnsServers;
        pkt.mLeaseTime = timeout;
        pkt.mDomainName = domainName;
        pkt.mSubnetMask = netMask;
        pkt.mServerIdentifier = dhcpServerIdentifier;
        pkt.mBroadcastAddress = bcAddr;
        return pkt.buildPacket(encap, android.net.dhcp.DhcpPacket.DHCP_CLIENT, android.net.dhcp.DhcpPacket.DHCP_SERVER);
    }

    /**
     * Builds a DHCP-NAK packet from the required specified parameters.
     */
    public static java.nio.ByteBuffer buildNakPacket(int encap, int transactionId, java.net.Inet4Address serverIpAddr, java.net.Inet4Address clientIpAddr, byte[] mac) {
        android.net.dhcp.DhcpPacket pkt = new android.net.dhcp.DhcpNakPacket(transactionId, ((short) (0)), clientIpAddr, serverIpAddr, serverIpAddr, serverIpAddr, mac);
        pkt.mMessage = "requested address not available";
        pkt.mRequestedIp = clientIpAddr;
        return pkt.buildPacket(encap, android.net.dhcp.DhcpPacket.DHCP_CLIENT, android.net.dhcp.DhcpPacket.DHCP_SERVER);
    }

    /**
     * Builds a DHCP-REQUEST packet from the required specified parameters.
     */
    public static java.nio.ByteBuffer buildRequestPacket(int encap, int transactionId, short secs, java.net.Inet4Address clientIp, boolean broadcast, byte[] clientMac, java.net.Inet4Address requestedIpAddress, java.net.Inet4Address serverIdentifier, byte[] requestedParams, java.lang.String hostName) {
        android.net.dhcp.DhcpPacket pkt = new android.net.dhcp.DhcpRequestPacket(transactionId, secs, clientIp, clientMac, broadcast);
        pkt.mRequestedIp = requestedIpAddress;
        pkt.mServerIdentifier = serverIdentifier;
        pkt.mHostName = hostName;
        pkt.mRequestedParams = requestedParams;
        java.nio.ByteBuffer result = pkt.buildPacket(encap, android.net.dhcp.DhcpPacket.DHCP_SERVER, android.net.dhcp.DhcpPacket.DHCP_CLIENT);
        return result;
    }
}

