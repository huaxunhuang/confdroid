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
 * Various constants and static helper methods for netlink communications.
 *
 * Values taken from:
 *
 *     &lt;linux_src&gt;/include/uapi/linux/netlink.h
 *     &lt;linux_src&gt;/include/uapi/linux/rtnetlink.h
 *
 * @unknown 
 */
public class NetlinkConstants {
    private NetlinkConstants() {
    }

    public static final int NLA_ALIGNTO = 4;

    public static final int alignedLengthOf(short length) {
        final int intLength = ((int) (length)) & 0xffff;
        return android.net.netlink.NetlinkConstants.alignedLengthOf(intLength);
    }

    public static final int alignedLengthOf(int length) {
        if (length <= 0) {
            return 0;
        }
        return (((length + android.net.netlink.NetlinkConstants.NLA_ALIGNTO) - 1) / android.net.netlink.NetlinkConstants.NLA_ALIGNTO) * android.net.netlink.NetlinkConstants.NLA_ALIGNTO;
    }

    public static java.lang.String stringForAddressFamily(int family) {
        if (family == android.system.OsConstants.AF_INET) {
            return "AF_INET";
        }
        if (family == android.system.OsConstants.AF_INET6) {
            return "AF_INET6";
        }
        if (family == android.system.OsConstants.AF_NETLINK) {
            return "AF_NETLINK";
        }
        return java.lang.String.valueOf(family);
    }

    public static java.lang.String hexify(byte[] bytes) {
        if (bytes == null) {
            return "(null)";
        }
        return com.android.internal.util.HexDump.toHexString(bytes);
    }

    public static java.lang.String hexify(java.nio.ByteBuffer buffer) {
        if (buffer == null) {
            return "(null)";
        }
        return com.android.internal.util.HexDump.toHexString(buffer.array(), buffer.position(), buffer.remaining());
    }

    // Known values for struct nlmsghdr nlm_type.
    public static final short NLMSG_NOOP = 1;// Nothing


    public static final short NLMSG_ERROR = 2;// Error


    public static final short NLMSG_DONE = 3;// End of a dump


    public static final short NLMSG_OVERRUN = 4;// Data lost


    public static final short NLMSG_MAX_RESERVED = 15;// Max reserved value


    public static final short RTM_NEWLINK = 16;

    public static final short RTM_DELLINK = 17;

    public static final short RTM_GETLINK = 18;

    public static final short RTM_SETLINK = 19;

    public static final short RTM_NEWADDR = 20;

    public static final short RTM_DELADDR = 21;

    public static final short RTM_GETADDR = 22;

    public static final short RTM_NEWROUTE = 24;

    public static final short RTM_DELROUTE = 25;

    public static final short RTM_GETROUTE = 26;

    public static final short RTM_NEWNEIGH = 28;

    public static final short RTM_DELNEIGH = 29;

    public static final short RTM_GETNEIGH = 30;

    public static final short RTM_NEWRULE = 32;

    public static final short RTM_DELRULE = 33;

    public static final short RTM_GETRULE = 34;

    public static final short RTM_NEWNDUSEROPT = 68;

    public static java.lang.String stringForNlMsgType(short nlm_type) {
        switch (nlm_type) {
            case android.net.netlink.NetlinkConstants.NLMSG_NOOP :
                return "NLMSG_NOOP";
            case android.net.netlink.NetlinkConstants.NLMSG_ERROR :
                return "NLMSG_ERROR";
            case android.net.netlink.NetlinkConstants.NLMSG_DONE :
                return "NLMSG_DONE";
            case android.net.netlink.NetlinkConstants.NLMSG_OVERRUN :
                return "NLMSG_OVERRUN";
            case android.net.netlink.NetlinkConstants.RTM_NEWLINK :
                return "RTM_NEWLINK";
            case android.net.netlink.NetlinkConstants.RTM_DELLINK :
                return "RTM_DELLINK";
            case android.net.netlink.NetlinkConstants.RTM_GETLINK :
                return "RTM_GETLINK";
            case android.net.netlink.NetlinkConstants.RTM_SETLINK :
                return "RTM_SETLINK";
            case android.net.netlink.NetlinkConstants.RTM_NEWADDR :
                return "RTM_NEWADDR";
            case android.net.netlink.NetlinkConstants.RTM_DELADDR :
                return "RTM_DELADDR";
            case android.net.netlink.NetlinkConstants.RTM_GETADDR :
                return "RTM_GETADDR";
            case android.net.netlink.NetlinkConstants.RTM_NEWROUTE :
                return "RTM_NEWROUTE";
            case android.net.netlink.NetlinkConstants.RTM_DELROUTE :
                return "RTM_DELROUTE";
            case android.net.netlink.NetlinkConstants.RTM_GETROUTE :
                return "RTM_GETROUTE";
            case android.net.netlink.NetlinkConstants.RTM_NEWNEIGH :
                return "RTM_NEWNEIGH";
            case android.net.netlink.NetlinkConstants.RTM_DELNEIGH :
                return "RTM_DELNEIGH";
            case android.net.netlink.NetlinkConstants.RTM_GETNEIGH :
                return "RTM_GETNEIGH";
            case android.net.netlink.NetlinkConstants.RTM_NEWRULE :
                return "RTM_NEWRULE";
            case android.net.netlink.NetlinkConstants.RTM_DELRULE :
                return "RTM_DELRULE";
            case android.net.netlink.NetlinkConstants.RTM_GETRULE :
                return "RTM_GETRULE";
            case android.net.netlink.NetlinkConstants.RTM_NEWNDUSEROPT :
                return "RTM_NEWNDUSEROPT";
            default :
                return "unknown RTM type: " + java.lang.String.valueOf(nlm_type);
        }
    }
}

