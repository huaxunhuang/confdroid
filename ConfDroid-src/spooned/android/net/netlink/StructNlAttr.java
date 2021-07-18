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
 * struct nlattr
 *
 * see: &lt;linux_src&gt;/include/uapi/linux/netlink.h
 *
 * @unknown 
 */
public class StructNlAttr {
    // Already aligned.
    public static final int NLA_HEADERLEN = 4;

    // Return a (length, type) object only, without consuming any bytes in
    // |byteBuffer| and without copying or interpreting any value bytes.
    // This is used for scanning over a packed set of struct nlattr's,
    // looking for instances of a particular type.
    public static android.net.netlink.StructNlAttr peek(java.nio.ByteBuffer byteBuffer) {
        if ((byteBuffer == null) || (byteBuffer.remaining() < android.net.netlink.StructNlAttr.NLA_HEADERLEN)) {
            return null;
        }
        final int baseOffset = byteBuffer.position();
        final android.net.netlink.StructNlAttr struct = new android.net.netlink.StructNlAttr();
        struct.nla_len = byteBuffer.getShort();
        struct.nla_type = byteBuffer.getShort();
        struct.mByteOrder = byteBuffer.order();
        byteBuffer.position(baseOffset);
        if (struct.nla_len < android.net.netlink.StructNlAttr.NLA_HEADERLEN) {
            // Malformed.
            return null;
        }
        return struct;
    }

    public static android.net.netlink.StructNlAttr parse(java.nio.ByteBuffer byteBuffer) {
        final android.net.netlink.StructNlAttr struct = android.net.netlink.StructNlAttr.peek(byteBuffer);
        if ((struct == null) || (byteBuffer.remaining() < struct.getAlignedLength())) {
            return null;
        }
        final int baseOffset = byteBuffer.position();
        byteBuffer.position(baseOffset + android.net.netlink.StructNlAttr.NLA_HEADERLEN);
        int valueLen = ((int) (struct.nla_len)) & 0xffff;
        valueLen -= android.net.netlink.StructNlAttr.NLA_HEADERLEN;
        if (valueLen > 0) {
            struct.nla_value = new byte[valueLen];
            byteBuffer.get(struct.nla_value, 0, valueLen);
            byteBuffer.position(baseOffset + struct.getAlignedLength());
        }
        return struct;
    }

    public short nla_len;

    public short nla_type;

    public byte[] nla_value;

    public java.nio.ByteOrder mByteOrder;

    public StructNlAttr() {
        mByteOrder = java.nio.ByteOrder.nativeOrder();
    }

    public int getAlignedLength() {
        return android.net.netlink.NetlinkConstants.alignedLengthOf(nla_len);
    }

    public java.nio.ByteBuffer getValueAsByteBuffer() {
        if (nla_value == null) {
            return null;
        }
        final java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(nla_value);
        byteBuffer.order(mByteOrder);
        return byteBuffer;
    }

    public int getValueAsInt(int defaultValue) {
        final java.nio.ByteBuffer byteBuffer = getValueAsByteBuffer();
        if ((byteBuffer == null) || (byteBuffer.remaining() != libcore.io.SizeOf.INT)) {
            return defaultValue;
        }
        return getValueAsByteBuffer().getInt();
    }

    public java.net.InetAddress getValueAsInetAddress() {
        if (nla_value == null) {
            return null;
        }
        try {
            return java.net.InetAddress.getByAddress(nla_value);
        } catch (java.net.UnknownHostException ignored) {
            return null;
        }
    }

    public void pack(java.nio.ByteBuffer byteBuffer) {
        final int originalPosition = byteBuffer.position();
        byteBuffer.putShort(nla_len);
        byteBuffer.putShort(nla_type);
        byteBuffer.put(nla_value);
        byteBuffer.position(originalPosition + getAlignedLength());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("StructNlAttr{ " + "nla_len{") + nla_len) + "}, ") + "nla_type{") + nla_type) + "}, ") + "nla_value{") + android.net.netlink.NetlinkConstants.hexify(nla_value)) + "}, ") + "}";
    }
}

