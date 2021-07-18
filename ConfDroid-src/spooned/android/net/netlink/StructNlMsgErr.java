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
 * struct nlmsgerr
 *
 * see &lt;linux_src&gt;/include/uapi/linux/netlink.h
 *
 * @unknown 
 */
public class StructNlMsgErr {
    public static final int STRUCT_SIZE = libcore.io.SizeOf.INT + android.net.netlink.StructNlMsgHdr.STRUCT_SIZE;

    public static boolean hasAvailableSpace(java.nio.ByteBuffer byteBuffer) {
        return (byteBuffer != null) && (byteBuffer.remaining() >= android.net.netlink.StructNlMsgErr.STRUCT_SIZE);
    }

    public static android.net.netlink.StructNlMsgErr parse(java.nio.ByteBuffer byteBuffer) {
        if (!android.net.netlink.StructNlMsgErr.hasAvailableSpace(byteBuffer)) {
            return null;
        }
        // The ByteOrder must have already been set by the caller.  In most
        // cases ByteOrder.nativeOrder() is correct, with the exception
        // of usage within unittests.
        final android.net.netlink.StructNlMsgErr struct = new android.net.netlink.StructNlMsgErr();
        struct.error = byteBuffer.getInt();
        struct.msg = android.net.netlink.StructNlMsgHdr.parse(byteBuffer);
        return struct;
    }

    public int error;

    public android.net.netlink.StructNlMsgHdr msg;

    public void pack(java.nio.ByteBuffer byteBuffer) {
        // The ByteOrder must have already been set by the caller.  In most
        // cases ByteOrder.nativeOrder() is correct, with the possible
        // exception of usage within unittests.
        byteBuffer.putInt(error);
        if (msg != null) {
            msg.pack(byteBuffer);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("StructNlMsgErr{ " + "error{") + error) + "}, ") + "msg{") + (msg == null ? "" : msg.toString())) + "} ") + "}";
    }
}

