/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.bluetooth.client.map.utils;


public final class ObexAppParameters {
    private final java.util.HashMap<java.lang.Byte, byte[]> mParams;

    public ObexAppParameters() {
        mParams = new java.util.HashMap<java.lang.Byte, byte[]>();
    }

    public ObexAppParameters(byte[] raw) {
        mParams = new java.util.HashMap<java.lang.Byte, byte[]>();
        if (raw != null) {
            for (int i = 0; i < raw.length;) {
                if ((raw.length - i) < 2) {
                    break;
                }
                byte tag = raw[i++];
                byte len = raw[i++];
                if (((raw.length - i) - len) < 0) {
                    break;
                }
                byte[] val = new byte[len];
                java.lang.System.arraycopy(raw, i, val, 0, len);
                this.add(tag, val);
                i += len;
            }
        }
    }

    public static android.bluetooth.client.map.utils.ObexAppParameters fromHeaderSet(javax.obex.HeaderSet headerset) {
        try {
            byte[] raw = ((byte[]) (headerset.getHeader(HeaderSet.APPLICATION_PARAMETER)));
            return new android.bluetooth.client.map.utils.ObexAppParameters(raw);
        } catch (java.io.IOException e) {
            // won't happen
        }
        return null;
    }

    public byte[] getHeader() {
        int length = 0;
        for (java.util.Map.Entry<java.lang.Byte, byte[]> entry : mParams.entrySet()) {
            length += entry.getValue().length + 2;
        }
        byte[] ret = new byte[length];
        int idx = 0;
        for (java.util.Map.Entry<java.lang.Byte, byte[]> entry : mParams.entrySet()) {
            length = entry.getValue().length;
            ret[idx++] = entry.getKey();
            ret[idx++] = ((byte) (length));
            java.lang.System.arraycopy(entry.getValue(), 0, ret, idx, length);
            idx += length;
        }
        return ret;
    }

    public void addToHeaderSet(javax.obex.HeaderSet headerset) {
        if (mParams.size() > 0) {
            headerset.setHeader(HeaderSet.APPLICATION_PARAMETER, getHeader());
        }
    }

    public boolean exists(byte tag) {
        return mParams.containsKey(tag);
    }

    public void add(byte tag, byte val) {
        byte[] bval = java.nio.ByteBuffer.allocate(1).put(val).array();
        mParams.put(tag, bval);
    }

    public void add(byte tag, short val) {
        byte[] bval = java.nio.ByteBuffer.allocate(2).putShort(val).array();
        mParams.put(tag, bval);
    }

    public void add(byte tag, int val) {
        byte[] bval = java.nio.ByteBuffer.allocate(4).putInt(val).array();
        mParams.put(tag, bval);
    }

    public void add(byte tag, long val) {
        byte[] bval = java.nio.ByteBuffer.allocate(8).putLong(val).array();
        mParams.put(tag, bval);
    }

    public void add(byte tag, java.lang.String val) {
        byte[] bval = val.getBytes();
        mParams.put(tag, bval);
    }

    public void add(byte tag, byte[] bval) {
        mParams.put(tag, bval);
    }

    public byte getByte(byte tag) {
        byte[] bval = mParams.get(tag);
        if ((bval == null) || (bval.length < 1)) {
            return 0;
        }
        return java.nio.ByteBuffer.wrap(bval).get();
    }

    public short getShort(byte tag) {
        byte[] bval = mParams.get(tag);
        if ((bval == null) || (bval.length < 2)) {
            return 0;
        }
        return java.nio.ByteBuffer.wrap(bval).getShort();
    }

    public int getInt(byte tag) {
        byte[] bval = mParams.get(tag);
        if ((bval == null) || (bval.length < 4)) {
            return 0;
        }
        return java.nio.ByteBuffer.wrap(bval).getInt();
    }

    public java.lang.String getString(byte tag) {
        byte[] bval = mParams.get(tag);
        if (bval == null) {
            return null;
        }
        return new java.lang.String(bval);
    }

    public byte[] getByteArray(byte tag) {
        byte[] bval = mParams.get(tag);
        return bval;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mParams.toString();
    }
}

