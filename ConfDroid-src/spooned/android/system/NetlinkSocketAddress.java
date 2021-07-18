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
package android.system;


/**
 * Netlink socket address.
 *
 * Corresponds to Linux's {@code struct sockaddr_nl} from
 * <a href="https://github.com/torvalds/linux/blob/master/include/uapi/linux/netlink.h">&lt;linux/netlink.h&gt;</a>.
 *
 * @unknown 
 */
public final class NetlinkSocketAddress extends java.net.SocketAddress {
    /**
     * port ID
     */
    private final int nlPortId;

    /**
     * multicast groups mask
     */
    private final int nlGroupsMask;

    public NetlinkSocketAddress() {
        this(0, 0);
    }

    public NetlinkSocketAddress(int nlPortId) {
        this(nlPortId, 0);
    }

    public NetlinkSocketAddress(int nlPortId, int nlGroupsMask) {
        this.nlPortId = nlPortId;
        this.nlGroupsMask = nlGroupsMask;
    }

    public int getPortId() {
        return nlPortId;
    }

    public int getGroupsMask() {
        return nlGroupsMask;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return libcore.util.Objects.toString(this);
    }
}

