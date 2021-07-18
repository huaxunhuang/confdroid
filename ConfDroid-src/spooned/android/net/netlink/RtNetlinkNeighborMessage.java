/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.net.netlink;


/**
 * A NetlinkMessage subclass for netlink error messages.
 *
 * see also: &lt;linux_src&gt;/include/uapi/linux/neighbour.h
 *
 * @unknown 
 */
public class RtNetlinkNeighborMessage extends android.net.netlink.NetlinkMessage {
    public static final short NDA_UNSPEC = 0;

    public static final short NDA_DST = 1;

    public static final short NDA_LLADDR = 2;

    public static final short NDA_CACHEINFO = 3;

    public static final short NDA_PROBES = 4;

    public static final short NDA_VLAN = 5;

    public static final short NDA_PORT = 6;

    public static final short NDA_VNI = 7;

    public static final short NDA_IFINDEX = 8;

    public static final short NDA_MASTER = 9;

    private static android.net.netlink.StructNlAttr findNextAttrOfType(short attrType, java.nio.ByteBuffer byteBuffer) {
        while ((byteBuffer != null) && (byteBuffer.remaining() > 0)) {
            final android.net.netlink.StructNlAttr nlAttr = android.net.netlink.StructNlAttr.peek(byteBuffer);
            if (nlAttr == null) {
                break;
            }
            if (nlAttr.nla_type == attrType) {
                return android.net.netlink.StructNlAttr.parse(byteBuffer);
            }
            if (byteBuffer.remaining() < nlAttr.getAlignedLength()) {
                break;
            }
            byteBuffer.position(byteBuffer.position() + nlAttr.getAlignedLength());
        } 
        return null;
    }

    public static android.net.netlink.RtNetlinkNeighborMessage parse(android.net.netlink.StructNlMsgHdr header, java.nio.ByteBuffer byteBuffer) {
        final android.net.netlink.RtNetlinkNeighborMessage neighMsg = new android.net.netlink.RtNetlinkNeighborMessage(header);
        neighMsg.mNdmsg = android.net.netlink.StructNdMsg.parse(byteBuffer);
        if (neighMsg.mNdmsg == null) {
            return null;
        }
        // Some of these are message-type dependent, and not always present.
        final int baseOffset = byteBuffer.position();
        android.net.netlink.StructNlAttr nlAttr = android.net.netlink.RtNetlinkNeighborMessage.findNextAttrOfType(android.net.netlink.RtNetlinkNeighborMessage.NDA_DST, byteBuffer);
        if (nlAttr != null) {
            neighMsg.mDestination = nlAttr.getValueAsInetAddress();
        }
        byteBuffer.position(baseOffset);
        nlAttr = android.net.netlink.RtNetlinkNeighborMessage.findNextAttrOfType(android.net.netlink.RtNetlinkNeighborMessage.NDA_LLADDR, byteBuffer);
        if (nlAttr != null) {
            neighMsg.mLinkLayerAddr = nlAttr.nla_value;
        }
        byteBuffer.position(baseOffset);
        nlAttr = android.net.netlink.RtNetlinkNeighborMessage.findNextAttrOfType(android.net.netlink.RtNetlinkNeighborMessage.NDA_PROBES, byteBuffer);
        if (nlAttr != null) {
            neighMsg.mNumProbes = nlAttr.getValueAsInt(0);
        }
        byteBuffer.position(baseOffset);
        nlAttr = android.net.netlink.RtNetlinkNeighborMessage.findNextAttrOfType(android.net.netlink.RtNetlinkNeighborMessage.NDA_CACHEINFO, byteBuffer);
        if (nlAttr != null) {
            neighMsg.mCacheInfo = android.net.netlink.StructNdaCacheInfo.parse(nlAttr.getValueAsByteBuffer());
        }
        final int kMinConsumed = android.net.netlink.StructNlMsgHdr.STRUCT_SIZE + android.net.netlink.StructNdMsg.STRUCT_SIZE;
        final int kAdditionalSpace = android.net.netlink.NetlinkConstants.alignedLengthOf(neighMsg.mHeader.nlmsg_len - kMinConsumed);
        if (byteBuffer.remaining() < kAdditionalSpace) {
            byteBuffer.position(byteBuffer.limit());
        } else {
            byteBuffer.position(baseOffset + kAdditionalSpace);
        }
        return neighMsg;
    }

    /**
     * A convenience method to create an RTM_GETNEIGH request message.
     */
    public static byte[] newGetNeighborsRequest(int seqNo) {
        final int length = android.net.netlink.StructNlMsgHdr.STRUCT_SIZE + android.net.netlink.StructNdMsg.STRUCT_SIZE;
        final byte[] bytes = new byte[length];
        final java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(bytes);
        byteBuffer.order(java.nio.ByteOrder.nativeOrder());
        final android.net.netlink.StructNlMsgHdr nlmsghdr = new android.net.netlink.StructNlMsgHdr();
        nlmsghdr.nlmsg_len = length;
        nlmsghdr.nlmsg_type = android.net.netlink.NetlinkConstants.RTM_GETNEIGH;
        nlmsghdr.nlmsg_flags = android.net.netlink.StructNlMsgHdr.NLM_F_REQUEST | android.net.netlink.StructNlMsgHdr.NLM_F_DUMP;
        nlmsghdr.nlmsg_seq = seqNo;
        nlmsghdr.pack(byteBuffer);
        final android.net.netlink.StructNdMsg ndmsg = new android.net.netlink.StructNdMsg();
        ndmsg.pack(byteBuffer);
        return bytes;
    }

    /**
     * A convenience method to create an RTM_NEWNEIGH message, to modify
     * the kernel's state information for a specific neighbor.
     */
    public static byte[] newNewNeighborMessage(int seqNo, java.net.InetAddress ip, short nudState, int ifIndex, byte[] llAddr) {
        final android.net.netlink.StructNlMsgHdr nlmsghdr = new android.net.netlink.StructNlMsgHdr();
        nlmsghdr.nlmsg_type = android.net.netlink.NetlinkConstants.RTM_NEWNEIGH;
        nlmsghdr.nlmsg_flags = (android.net.netlink.StructNlMsgHdr.NLM_F_REQUEST | android.net.netlink.StructNlMsgHdr.NLM_F_ACK) | android.net.netlink.StructNlMsgHdr.NLM_F_REPLACE;
        nlmsghdr.nlmsg_seq = seqNo;
        final android.net.netlink.RtNetlinkNeighborMessage msg = new android.net.netlink.RtNetlinkNeighborMessage(nlmsghdr);
        msg.mNdmsg = new android.net.netlink.StructNdMsg();
        msg.mNdmsg.ndm_family = ((byte) ((ip instanceof java.net.Inet6Address) ? android.system.OsConstants.AF_INET6 : android.system.OsConstants.AF_INET));
        msg.mNdmsg.ndm_ifindex = ifIndex;
        msg.mNdmsg.ndm_state = nudState;
        msg.mDestination = ip;
        msg.mLinkLayerAddr = llAddr;// might be null

        final byte[] bytes = new byte[msg.getRequiredSpace()];
        nlmsghdr.nlmsg_len = bytes.length;
        final java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(bytes);
        byteBuffer.order(java.nio.ByteOrder.nativeOrder());
        msg.pack(byteBuffer);
        return bytes;
    }

    private android.net.netlink.StructNdMsg mNdmsg;

    private java.net.InetAddress mDestination;

    private byte[] mLinkLayerAddr;

    private int mNumProbes;

    private android.net.netlink.StructNdaCacheInfo mCacheInfo;

    private RtNetlinkNeighborMessage(android.net.netlink.StructNlMsgHdr header) {
        super(header);
        mNdmsg = null;
        mDestination = null;
        mLinkLayerAddr = null;
        mNumProbes = 0;
        mCacheInfo = null;
    }

    public android.net.netlink.StructNdMsg getNdHeader() {
        return mNdmsg;
    }

    public java.net.InetAddress getDestination() {
        return mDestination;
    }

    public byte[] getLinkLayerAddress() {
        return mLinkLayerAddr;
    }

    public int getProbes() {
        return mNumProbes;
    }

    public android.net.netlink.StructNdaCacheInfo getCacheInfo() {
        return mCacheInfo;
    }

    public int getRequiredSpace() {
        int spaceRequired = android.net.netlink.StructNlMsgHdr.STRUCT_SIZE + android.net.netlink.StructNdMsg.STRUCT_SIZE;
        if (mDestination != null) {
            spaceRequired += android.net.netlink.NetlinkConstants.alignedLengthOf(android.net.netlink.StructNlAttr.NLA_HEADERLEN + mDestination.getAddress().length);
        }
        if (mLinkLayerAddr != null) {
            spaceRequired += android.net.netlink.NetlinkConstants.alignedLengthOf(android.net.netlink.StructNlAttr.NLA_HEADERLEN + mLinkLayerAddr.length);
        }
        // Currently we don't write messages with NDA_PROBES nor NDA_CACHEINFO
        // attributes appended.  Fix later, if necessary.
        return spaceRequired;
    }

    private static void packNlAttr(short nlType, byte[] nlValue, java.nio.ByteBuffer byteBuffer) {
        final android.net.netlink.StructNlAttr nlAttr = new android.net.netlink.StructNlAttr();
        nlAttr.nla_type = nlType;
        nlAttr.nla_value = nlValue;
        nlAttr.nla_len = ((short) (android.net.netlink.StructNlAttr.NLA_HEADERLEN + nlAttr.nla_value.length));
        nlAttr.pack(byteBuffer);
    }

    public void pack(java.nio.ByteBuffer byteBuffer) {
        getHeader().pack(byteBuffer);
        mNdmsg.pack(byteBuffer);
        if (mDestination != null) {
            android.net.netlink.RtNetlinkNeighborMessage.packNlAttr(android.net.netlink.RtNetlinkNeighborMessage.NDA_DST, mDestination.getAddress(), byteBuffer);
        }
        if (mLinkLayerAddr != null) {
            android.net.netlink.RtNetlinkNeighborMessage.packNlAttr(android.net.netlink.RtNetlinkNeighborMessage.NDA_LLADDR, mLinkLayerAddr, byteBuffer);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.String ipLiteral = (mDestination == null) ? "" : mDestination.getHostAddress();
        return (((((((((((((((((("RtNetlinkNeighborMessage{ " + "nlmsghdr{") + (mHeader == null ? "" : mHeader.toString())) + "}, ") + "ndmsg{") + (mNdmsg == null ? "" : mNdmsg.toString())) + "}, ") + "destination{") + ipLiteral) + "} ") + "linklayeraddr{") + android.net.netlink.NetlinkConstants.hexify(mLinkLayerAddr)) + "} ") + "probes{") + mNumProbes) + "} ") + "cacheinfo{") + (mCacheInfo == null ? "" : mCacheInfo.toString())) + "} ") + "}";
    }
}

