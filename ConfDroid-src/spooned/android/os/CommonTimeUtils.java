/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.os;


class CommonTimeUtils {
    /**
     * Successful operation.
     */
    public static final int SUCCESS = 0;

    /**
     * Unspecified error.
     */
    public static final int ERROR = -1;

    /**
     * Operation failed due to bad parameter value.
     */
    public static final int ERROR_BAD_VALUE = -4;

    /**
     * Operation failed due to dead remote object.
     */
    public static final int ERROR_DEAD_OBJECT = -7;

    public CommonTimeUtils(android.os.IBinder remote, java.lang.String interfaceDesc) {
        mRemote = remote;
        mInterfaceDesc = interfaceDesc;
    }

    public int transactGetInt(int method_code, int error_ret_val) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        int ret_val;
        try {
            int res;
            data.writeInterfaceToken(mInterfaceDesc);
            mRemote.transact(method_code, data, reply, 0);
            res = reply.readInt();
            ret_val = (0 == res) ? reply.readInt() : error_ret_val;
        } finally {
            reply.recycle();
            data.recycle();
        }
        return ret_val;
    }

    public int transactSetInt(int method_code, int val) {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(mInterfaceDesc);
            data.writeInt(val);
            mRemote.transact(method_code, data, reply, 0);
            return reply.readInt();
        } catch (android.os.RemoteException e) {
            return android.os.CommonTimeUtils.ERROR_DEAD_OBJECT;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public long transactGetLong(int method_code, long error_ret_val) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        long ret_val;
        try {
            int res;
            data.writeInterfaceToken(mInterfaceDesc);
            mRemote.transact(method_code, data, reply, 0);
            res = reply.readInt();
            ret_val = (0 == res) ? reply.readLong() : error_ret_val;
        } finally {
            reply.recycle();
            data.recycle();
        }
        return ret_val;
    }

    public int transactSetLong(int method_code, long val) {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(mInterfaceDesc);
            data.writeLong(val);
            mRemote.transact(method_code, data, reply, 0);
            return reply.readInt();
        } catch (android.os.RemoteException e) {
            return android.os.CommonTimeUtils.ERROR_DEAD_OBJECT;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public java.lang.String transactGetString(int method_code, java.lang.String error_ret_val) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        java.lang.String ret_val;
        try {
            int res;
            data.writeInterfaceToken(mInterfaceDesc);
            mRemote.transact(method_code, data, reply, 0);
            res = reply.readInt();
            ret_val = (0 == res) ? reply.readString() : error_ret_val;
        } finally {
            reply.recycle();
            data.recycle();
        }
        return ret_val;
    }

    public int transactSetString(int method_code, java.lang.String val) {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(mInterfaceDesc);
            data.writeString(val);
            mRemote.transact(method_code, data, reply, 0);
            return reply.readInt();
        } catch (android.os.RemoteException e) {
            return android.os.CommonTimeUtils.ERROR_DEAD_OBJECT;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public java.net.InetSocketAddress transactGetSockaddr(int method_code) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        java.net.InetSocketAddress ret_val = null;
        try {
            int res;
            data.writeInterfaceToken(mInterfaceDesc);
            mRemote.transact(method_code, data, reply, 0);
            res = reply.readInt();
            if (0 == res) {
                int type;
                int port = 0;
                java.lang.String addrStr = null;
                type = reply.readInt();
                if (android.system.OsConstants.AF_INET == type) {
                    int addr = reply.readInt();
                    port = reply.readInt();
                    addrStr = java.lang.String.format(java.util.Locale.US, "%d.%d.%d.%d", (addr >> 24) & 0xff, (addr >> 16) & 0xff, (addr >> 8) & 0xff, addr & 0xff);
                } else
                    if (android.system.OsConstants.AF_INET6 == type) {
                        int addr1 = reply.readInt();
                        int addr2 = reply.readInt();
                        int addr3 = reply.readInt();
                        int addr4 = reply.readInt();
                        port = reply.readInt();
                        int flowinfo = reply.readInt();
                        int scope_id = reply.readInt();
                        addrStr = java.lang.String.format(java.util.Locale.US, "[%04X:%04X:%04X:%04X:%04X:%04X:%04X:%04X]", (addr1 >> 16) & 0xffff, addr1 & 0xffff, (addr2 >> 16) & 0xffff, addr2 & 0xffff, (addr3 >> 16) & 0xffff, addr3 & 0xffff, (addr4 >> 16) & 0xffff, addr4 & 0xffff);
                    }

                if (null != addrStr) {
                    ret_val = new java.net.InetSocketAddress(addrStr, port);
                }
            }
        } finally {
            reply.recycle();
            data.recycle();
        }
        return ret_val;
    }

    public int transactSetSockaddr(int method_code, java.net.InetSocketAddress addr) {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        int ret_val = android.os.CommonTimeUtils.ERROR;
        try {
            data.writeInterfaceToken(mInterfaceDesc);
            if (null == addr) {
                data.writeInt(0);
            } else {
                data.writeInt(1);
                final java.net.InetAddress a = addr.getAddress();
                final byte[] b = a.getAddress();
                final int p = addr.getPort();
                if (a instanceof java.net.Inet4Address) {
                    int v4addr = ((((((int) (b[0])) & 0xff) << 24) | ((((int) (b[1])) & 0xff) << 16)) | ((((int) (b[2])) & 0xff) << 8)) | (((int) (b[3])) & 0xff);
                    data.writeInt(android.system.OsConstants.AF_INET);
                    data.writeInt(v4addr);
                    data.writeInt(p);
                } else
                    if (a instanceof java.net.Inet6Address) {
                        int i;
                        java.net.Inet6Address v6 = ((java.net.Inet6Address) (a));
                        data.writeInt(android.system.OsConstants.AF_INET6);
                        for (i = 0; i < 4; ++i) {
                            int aword = ((((((int) (b[(i * 4) + 0])) & 0xff) << 24) | ((((int) (b[(i * 4) + 1])) & 0xff) << 16)) | ((((int) (b[(i * 4) + 2])) & 0xff) << 8)) | (((int) (b[(i * 4) + 3])) & 0xff);
                            data.writeInt(aword);
                        }
                        data.writeInt(p);
                        data.writeInt(0);// flow info

                        data.writeInt(v6.getScopeId());
                    } else {
                        return android.os.CommonTimeUtils.ERROR_BAD_VALUE;
                    }

            }
            mRemote.transact(method_code, data, reply, 0);
            ret_val = reply.readInt();
        } catch (android.os.RemoteException e) {
            ret_val = android.os.CommonTimeUtils.ERROR_DEAD_OBJECT;
        } finally {
            reply.recycle();
            data.recycle();
        }
        return ret_val;
    }

    private android.os.IBinder mRemote;

    private java.lang.String mInterfaceDesc;
}

