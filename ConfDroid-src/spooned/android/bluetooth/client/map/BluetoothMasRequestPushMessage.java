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


final class BluetoothMasRequestPushMessage extends android.bluetooth.client.map.BluetoothMasRequest {
    private static final java.lang.String TYPE = "x-bt/message";

    private java.lang.String mMsg;

    private java.lang.String mMsgHandle;

    private BluetoothMasRequestPushMessage(java.lang.String folder) {
        mHeaderSet.setHeader(HeaderSet.TYPE, android.bluetooth.client.map.BluetoothMasRequestPushMessage.TYPE);
        if (folder == null) {
            folder = "";
        }
        mHeaderSet.setHeader(HeaderSet.NAME, folder);
    }

    public BluetoothMasRequestPushMessage(java.lang.String folder, java.lang.String msg, android.bluetooth.client.map.BluetoothMasClient.CharsetType charset, boolean transparent, boolean retry) {
        this(folder);
        mMsg = msg;
        android.bluetooth.client.map.utils.ObexAppParameters oap = new android.bluetooth.client.map.utils.ObexAppParameters();
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_TRANSPARENT, transparent ? android.bluetooth.client.map.BluetoothMasRequest.TRANSPARENT_ON : android.bluetooth.client.map.BluetoothMasRequest.TRANSPARENT_OFF);
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_RETRY, retry ? android.bluetooth.client.map.BluetoothMasRequest.RETRY_ON : android.bluetooth.client.map.BluetoothMasRequest.RETRY_OFF);
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_CHARSET, charset == android.bluetooth.client.map.BluetoothMasClient.CharsetType.NATIVE ? android.bluetooth.client.map.BluetoothMasRequest.CHARSET_NATIVE : android.bluetooth.client.map.BluetoothMasRequest.CHARSET_UTF8);
        oap.addToHeaderSet(mHeaderSet);
    }

    @java.lang.Override
    protected void readResponseHeaders(javax.obex.HeaderSet headerset) {
        try {
            java.lang.String handle = ((java.lang.String) (headerset.getHeader(HeaderSet.NAME)));
            if (handle != null) {
                /* just to validate */
                new java.math.BigInteger(handle, 16);
                mMsgHandle = handle;
            }
        } catch (java.lang.NumberFormatException e) {
            mResponseCode = javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        } catch (java.io.IOException e) {
            mResponseCode = javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
    }

    public java.lang.String getMsgHandle() {
        return mMsgHandle;
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) throws java.io.IOException {
        executePut(session, mMsg.getBytes());
    }
}

