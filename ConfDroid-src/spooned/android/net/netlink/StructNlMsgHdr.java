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
 * struct nlmsghdr
 *
 * see &lt;linux_src&gt;/include/uapi/linux/netlink.h
 *
 * @unknown 
 */
public class StructNlMsgHdr {
    // Already aligned.
    public static final int STRUCT_SIZE = 16;

    public static final short NLM_F_REQUEST = 0x1;

    public static final short NLM_F_MULTI = 0x2;

    public static final short NLM_F_ACK = 0x4;

    public static final short NLM_F_ECHO = 0x8;

    // Flags for a GET request.
    public static final short NLM_F_ROOT = 0x100;

    public static final short NLM_F_MATCH = 0x200;

    public static final short NLM_F_DUMP = android.net.netlink.StructNlMsgHdr.NLM_F_ROOT | android.net.netlink.StructNlMsgHdr.NLM_F_MATCH;

    // Flags for a NEW request.
    public static final short NLM_F_REPLACE = 0x100;

    public static final short NLM_F_EXCL = 0x200;

    public static final short NLM_F_CREATE = 0x400;

    public static final short NLM_F_APPEND = 0x800;

    public static java.lang.String stringForNlMsgFlags(short flags) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((flags & android.net.netlink.StructNlMsgHdr.NLM_F_REQUEST) != 0) {
            sb.append("NLM_F_REQUEST");
        }
        if ((flags & android.net.netlink.StructNlMsgHdr.NLM_F_MULTI) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NLM_F_MULTI");
        }
        if ((flags & android.net.netlink.StructNlMsgHdr.NLM_F_ACK) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NLM_F_ACK");
        }
        if ((flags & android.net.netlink.StructNlMsgHdr.NLM_F_ECHO) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NLM_F_ECHO");
        }
        if ((flags & android.net.netlink.StructNlMsgHdr.NLM_F_ROOT) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NLM_F_ROOT");
        }
        if ((flags & android.net.netlink.StructNlMsgHdr.NLM_F_MATCH) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NLM_F_MATCH");
        }
        return sb.toString();
    }

    public static boolean hasAvailableSpace(java.nio.ByteBuffer byteBuffer) {
        return (byteBuffer != null) && (byteBuffer.remaining() >= android.net.netlink.StructNlMsgHdr.STRUCT_SIZE);
    }

    public static android.net.netlink.StructNlMsgHdr parse(java.nio.ByteBuffer byteBuffer) {
        if (!android.net.netlink.StructNlMsgHdr.hasAvailableSpace(byteBuffer)) {
            return null;
        }
        // The ByteOrder must have already been set by the caller.  In most
        // cases ByteOrder.nativeOrder() is correct, with the exception
        // of usage within unittests.
        final android.net.netlink.StructNlMsgHdr struct = new android.net.netlink.StructNlMsgHdr();
        struct.nlmsg_len = byteBuffer.getInt();
        struct.nlmsg_type = byteBuffer.getShort();
        struct.nlmsg_flags = byteBuffer.getShort();
        struct.nlmsg_seq = byteBuffer.getInt();
        struct.nlmsg_pid = byteBuffer.getInt();
        if (struct.nlmsg_len < android.net.netlink.StructNlMsgHdr.STRUCT_SIZE) {
            // Malformed.
            return null;
        }
        return struct;
    }

    public int nlmsg_len;

    public short nlmsg_type;

    public short nlmsg_flags;

    public int nlmsg_seq;

    public int nlmsg_pid;

    public StructNlMsgHdr() {
        nlmsg_len = 0;
        nlmsg_type = 0;
        nlmsg_flags = 0;
        nlmsg_seq = 0;
        nlmsg_pid = 0;
    }

    public void pack(java.nio.ByteBuffer byteBuffer) {
        // The ByteOrder must have already been set by the caller.  In most
        // cases ByteOrder.nativeOrder() is correct, with the possible
        // exception of usage within unittests.
        byteBuffer.putInt(nlmsg_len);
        byteBuffer.putShort(nlmsg_type);
        byteBuffer.putShort(nlmsg_flags);
        byteBuffer.putInt(nlmsg_seq);
        byteBuffer.putInt(nlmsg_pid);
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.String typeStr = ((("" + nlmsg_type) + "(") + android.net.netlink.NetlinkConstants.stringForNlMsgType(nlmsg_type)) + ")";
        final java.lang.String flagsStr = ((("" + nlmsg_flags) + "(") + android.net.netlink.StructNlMsgHdr.stringForNlMsgFlags(nlmsg_flags)) + ")";
        return ((((((((((((((("StructNlMsgHdr{ " + "nlmsg_len{") + nlmsg_len) + "}, ") + "nlmsg_type{") + typeStr) + "}, ") + "nlmsg_flags{") + flagsStr) + ")}, ") + "nlmsg_seq{") + nlmsg_seq) + "}, ") + "nlmsg_pid{") + nlmsg_pid) + "} ") + "}";
    }
}

