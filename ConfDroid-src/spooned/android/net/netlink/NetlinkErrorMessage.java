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
 * @unknown 
 */
public class NetlinkErrorMessage extends android.net.netlink.NetlinkMessage {
    public static android.net.netlink.NetlinkErrorMessage parse(android.net.netlink.StructNlMsgHdr header, java.nio.ByteBuffer byteBuffer) {
        final android.net.netlink.NetlinkErrorMessage errorMsg = new android.net.netlink.NetlinkErrorMessage(header);
        errorMsg.mNlMsgErr = android.net.netlink.StructNlMsgErr.parse(byteBuffer);
        if (errorMsg.mNlMsgErr == null) {
            return null;
        }
        return errorMsg;
    }

    private android.net.netlink.StructNlMsgErr mNlMsgErr;

    NetlinkErrorMessage(android.net.netlink.StructNlMsgHdr header) {
        super(header);
        mNlMsgErr = null;
    }

    public android.net.netlink.StructNlMsgErr getNlMsgError() {
        return mNlMsgErr;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("NetlinkErrorMessage{ " + "nlmsghdr{") + (mHeader == null ? "" : mHeader.toString())) + "}, ") + "nlmsgerr{") + (mNlMsgErr == null ? "" : mNlMsgErr.toString())) + "} ") + "}";
    }
}

