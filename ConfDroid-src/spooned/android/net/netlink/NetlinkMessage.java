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
 * NetlinkMessage base class for other, more specific netlink message types.
 *
 * Classes that extend NetlinkMessage should:
 *     - implement a public static parse(StructNlMsgHdr, ByteBuffer) method
 *     - returning either null (parse errors) or a new object of the subclass
 *       type (cast-able to NetlinkMessage)
 *
 * NetlinkMessage.parse() should be updated to know which nlmsg_type values
 * correspond with which message subclasses.
 *
 * @unknown 
 */
public class NetlinkMessage {
    private static final java.lang.String TAG = "NetlinkMessage";

    public static android.net.netlink.NetlinkMessage parse(java.nio.ByteBuffer byteBuffer) {
        final int startPosition = (byteBuffer != null) ? byteBuffer.position() : -1;
        final android.net.netlink.StructNlMsgHdr nlmsghdr = android.net.netlink.StructNlMsgHdr.parse(byteBuffer);
        if (nlmsghdr == null) {
            return null;
        }
        int payloadLength = android.net.netlink.NetlinkConstants.alignedLengthOf(nlmsghdr.nlmsg_len);
        payloadLength -= android.net.netlink.StructNlMsgHdr.STRUCT_SIZE;
        if ((payloadLength < 0) || (payloadLength > byteBuffer.remaining())) {
            // Malformed message or runt buffer.  Pretend the buffer was consumed.
            byteBuffer.position(byteBuffer.limit());
            return null;
        }
        switch (nlmsghdr.nlmsg_type) {
            // case NetlinkConstants.NLMSG_NOOP:
            case android.net.netlink.NetlinkConstants.NLMSG_ERROR :
                return ((android.net.netlink.NetlinkMessage) (android.net.netlink.NetlinkErrorMessage.parse(nlmsghdr, byteBuffer)));
            case android.net.netlink.NetlinkConstants.NLMSG_DONE :
                byteBuffer.position(byteBuffer.position() + payloadLength);
                return new android.net.netlink.NetlinkMessage(nlmsghdr);
                // case NetlinkConstants.NLMSG_OVERRUN:
            case android.net.netlink.NetlinkConstants.RTM_NEWNEIGH :
            case android.net.netlink.NetlinkConstants.RTM_DELNEIGH :
            case android.net.netlink.NetlinkConstants.RTM_GETNEIGH :
                return ((android.net.netlink.NetlinkMessage) (android.net.netlink.RtNetlinkNeighborMessage.parse(nlmsghdr, byteBuffer)));
            default :
                if (nlmsghdr.nlmsg_type <= android.net.netlink.NetlinkConstants.NLMSG_MAX_RESERVED) {
                    // Netlink control message.  Just parse the header for now,
                    // pretending the whole message was consumed.
                    byteBuffer.position(byteBuffer.position() + payloadLength);
                    return new android.net.netlink.NetlinkMessage(nlmsghdr);
                }
                return null;
        }
    }

    protected android.net.netlink.StructNlMsgHdr mHeader;

    public NetlinkMessage(android.net.netlink.StructNlMsgHdr nlmsghdr) {
        mHeader = nlmsghdr;
    }

    public android.net.netlink.StructNlMsgHdr getHeader() {
        return mHeader;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("NetlinkMessage{" + (mHeader == null ? "" : mHeader.toString())) + "}";
    }
}

