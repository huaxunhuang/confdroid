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
package android.bluetooth.client.map;


class BluetoothMnsObexServer extends javax.obex.ServerRequestHandler {
    private static final java.lang.String TAG = "BluetoothMnsObexServer";

    private static final byte[] MNS_TARGET = new byte[]{ ((byte) (0xbb)), 0x58, 0x2b, 0x41, 0x42, 0xc, 0x11, ((byte) (0xdb)), ((byte) (0xb0)), ((byte) (0xde)), 0x8, 0x0, 0x20, 0xc, ((byte) (0x9a)), 0x66 };

    private static final java.lang.String TYPE = "x-bt/MAP-event-report";

    private final android.os.Handler mCallback;

    public BluetoothMnsObexServer(android.os.Handler callback) {
        super();
        mCallback = callback;
    }

    @java.lang.Override
    public int onConnect(final javax.obex.HeaderSet request, javax.obex.HeaderSet reply) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onConnect");
        try {
            byte[] uuid = ((byte[]) (request.getHeader(HeaderSet.TARGET)));
            if (!java.util.Arrays.equals(uuid, android.bluetooth.client.map.BluetoothMnsObexServer.MNS_TARGET)) {
                return javax.obex.ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE;
            }
        } catch (java.io.IOException e) {
            // this should never happen since getHeader won't throw exception it
            // declares to throw
            return javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        reply.setHeader(HeaderSet.WHO, android.bluetooth.client.map.BluetoothMnsObexServer.MNS_TARGET);
        return javax.obex.ResponseCodes.OBEX_HTTP_OK;
    }

    @java.lang.Override
    public void onDisconnect(final javax.obex.HeaderSet request, javax.obex.HeaderSet reply) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onDisconnect");
    }

    @java.lang.Override
    public int onGet(final javax.obex.Operation op) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onGet");
        return javax.obex.ResponseCodes.OBEX_HTTP_BAD_REQUEST;
    }

    @java.lang.Override
    public int onPut(final javax.obex.Operation op) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onPut");
        try {
            javax.obex.HeaderSet headerset;
            headerset = op.getReceivedHeader();
            java.lang.String type = ((java.lang.String) (headerset.getHeader(HeaderSet.TYPE)));
            android.bluetooth.client.map.utils.ObexAppParameters oap = android.bluetooth.client.map.utils.ObexAppParameters.fromHeaderSet(headerset);
            if ((!android.bluetooth.client.map.BluetoothMnsObexServer.TYPE.equals(type)) || (!oap.exists(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MAS_INSTANCE_ID))) {
                return javax.obex.ResponseCodes.OBEX_HTTP_BAD_REQUEST;
            }
            java.lang.Byte inst = oap.getByte(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MAS_INSTANCE_ID);
            android.bluetooth.client.map.BluetoothMapEventReport ev = android.bluetooth.client.map.BluetoothMapEventReport.fromStream(op.openDataInputStream());
            op.close();
            mCallback.obtainMessage(android.bluetooth.client.map.BluetoothMnsService.MSG_EVENT, inst, 0, ev).sendToTarget();
        } catch (java.io.IOException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "I/O exception when handling PUT request", e);
            return javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        return javax.obex.ResponseCodes.OBEX_HTTP_OK;
    }

    @java.lang.Override
    public int onAbort(final javax.obex.HeaderSet request, javax.obex.HeaderSet reply) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onAbort");
        return javax.obex.ResponseCodes.OBEX_HTTP_NOT_IMPLEMENTED;
    }

    @java.lang.Override
    public int onSetPath(final javax.obex.HeaderSet request, javax.obex.HeaderSet reply, final boolean backup, final boolean create) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onSetPath");
        return javax.obex.ResponseCodes.OBEX_HTTP_BAD_REQUEST;
    }

    @java.lang.Override
    public void onClose() {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsObexServer.TAG, "onClose");
        // TODO: call session handler so it can disconnect
    }
}

