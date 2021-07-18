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
 * struct ndmsg
 *
 * see: &lt;linux_src&gt;/include/uapi/linux/neighbour.h
 *
 * @unknown 
 */
public class StructNdMsg {
    // Already aligned.
    public static final int STRUCT_SIZE = 12;

    // Neighbor Cache Entry States
    public static final short NUD_NONE = 0x0;

    public static final short NUD_INCOMPLETE = 0x1;

    public static final short NUD_REACHABLE = 0x2;

    public static final short NUD_STALE = 0x4;

    public static final short NUD_DELAY = 0x8;

    public static final short NUD_PROBE = 0x10;

    public static final short NUD_FAILED = 0x20;

    public static final short NUD_NOARP = 0x40;

    public static final short NUD_PERMANENT = 0x80;

    public static java.lang.String stringForNudState(short nudState) {
        switch (nudState) {
            case android.net.netlink.StructNdMsg.NUD_NONE :
                return "NUD_NONE";
            case android.net.netlink.StructNdMsg.NUD_INCOMPLETE :
                return "NUD_INCOMPLETE";
            case android.net.netlink.StructNdMsg.NUD_REACHABLE :
                return "NUD_REACHABLE";
            case android.net.netlink.StructNdMsg.NUD_STALE :
                return "NUD_STALE";
            case android.net.netlink.StructNdMsg.NUD_DELAY :
                return "NUD_DELAY";
            case android.net.netlink.StructNdMsg.NUD_PROBE :
                return "NUD_PROBE";
            case android.net.netlink.StructNdMsg.NUD_FAILED :
                return "NUD_FAILED";
            case android.net.netlink.StructNdMsg.NUD_NOARP :
                return "NUD_NOARP";
            case android.net.netlink.StructNdMsg.NUD_PERMANENT :
                return "NUD_PERMANENT";
            default :
                return "unknown NUD state: " + java.lang.String.valueOf(nudState);
        }
    }

    public static boolean isNudStateConnected(short nudState) {
        return (nudState & ((android.net.netlink.StructNdMsg.NUD_PERMANENT | android.net.netlink.StructNdMsg.NUD_NOARP) | android.net.netlink.StructNdMsg.NUD_REACHABLE)) != 0;
    }

    // Neighbor Cache Entry Flags
    public static byte NTF_USE = ((byte) (0x1));

    public static byte NTF_SELF = ((byte) (0x2));

    public static byte NTF_MASTER = ((byte) (0x4));

    public static byte NTF_PROXY = ((byte) (0x8));

    public static byte NTF_ROUTER = ((byte) (0x80));

    public static java.lang.String stringForNudFlags(byte flags) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((flags & android.net.netlink.StructNdMsg.NTF_USE) != 0) {
            sb.append("NTF_USE");
        }
        if ((flags & android.net.netlink.StructNdMsg.NTF_SELF) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NTF_SELF");
        }
        if ((flags & android.net.netlink.StructNdMsg.NTF_MASTER) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NTF_MASTER");
        }
        if ((flags & android.net.netlink.StructNdMsg.NTF_PROXY) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NTF_PROXY");
        }
        if ((flags & android.net.netlink.StructNdMsg.NTF_ROUTER) != 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("NTF_ROUTER");
        }
        return sb.toString();
    }

    private static boolean hasAvailableSpace(java.nio.ByteBuffer byteBuffer) {
        return (byteBuffer != null) && (byteBuffer.remaining() >= android.net.netlink.StructNdMsg.STRUCT_SIZE);
    }

    public static android.net.netlink.StructNdMsg parse(java.nio.ByteBuffer byteBuffer) {
        if (!android.net.netlink.StructNdMsg.hasAvailableSpace(byteBuffer)) {
            return null;
        }
        // The ByteOrder must have already been set by the caller.  In most
        // cases ByteOrder.nativeOrder() is correct, with the possible
        // exception of usage within unittests.
        final android.net.netlink.StructNdMsg struct = new android.net.netlink.StructNdMsg();
        struct.ndm_family = byteBuffer.get();
        final byte pad1 = byteBuffer.get();
        final short pad2 = byteBuffer.getShort();
        struct.ndm_ifindex = byteBuffer.getInt();
        struct.ndm_state = byteBuffer.getShort();
        struct.ndm_flags = byteBuffer.get();
        struct.ndm_type = byteBuffer.get();
        return struct;
    }

    public byte ndm_family;

    public int ndm_ifindex;

    public short ndm_state;

    public byte ndm_flags;

    public byte ndm_type;

    public StructNdMsg() {
        ndm_family = ((byte) (android.system.OsConstants.AF_UNSPEC));
    }

    public void pack(java.nio.ByteBuffer byteBuffer) {
        // The ByteOrder must have already been set by the caller.  In most
        // cases ByteOrder.nativeOrder() is correct, with the exception
        // of usage within unittests.
        byteBuffer.put(ndm_family);
        byteBuffer.put(((byte) (0)));// pad1

        byteBuffer.putShort(((short) (0)));// pad2

        byteBuffer.putInt(ndm_ifindex);
        byteBuffer.putShort(ndm_state);
        byteBuffer.put(ndm_flags);
        byteBuffer.put(ndm_type);
    }

    public boolean nudConnected() {
        return android.net.netlink.StructNdMsg.isNudStateConnected(ndm_state);
    }

    public boolean nudValid() {
        return nudConnected() || ((ndm_state & ((android.net.netlink.StructNdMsg.NUD_PROBE | android.net.netlink.StructNdMsg.NUD_STALE) | android.net.netlink.StructNdMsg.NUD_DELAY)) != 0);
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.String stateStr = ((("" + ndm_state) + " (") + android.net.netlink.StructNdMsg.stringForNudState(ndm_state)) + ")";
        final java.lang.String flagsStr = ((("" + ndm_flags) + " (") + android.net.netlink.StructNdMsg.stringForNudFlags(ndm_flags)) + ")";
        return ((((((((((((((("StructNdMsg{ " + "family{") + android.net.netlink.NetlinkConstants.stringForAddressFamily(((int) (ndm_family)))) + "}, ") + "ifindex{") + ndm_ifindex) + "}, ") + "state{") + stateStr) + "}, ") + "flags{") + flagsStr) + "}, ") + "type{") + ndm_type) + "} ") + "}";
    }
}

