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


final class BluetoothMasRequestGetMessage extends android.bluetooth.client.map.BluetoothMasRequest {
    private static final java.lang.String TAG = "BluetoothMasRequestGetMessage";

    private static final java.lang.String TYPE = "x-bt/message";

    private android.bluetooth.client.map.BluetoothMapBmessage mBmessage;

    public BluetoothMasRequestGetMessage(java.lang.String handle, android.bluetooth.client.map.BluetoothMasClient.CharsetType charset, boolean attachment) {
        mHeaderSet.setHeader(HeaderSet.NAME, handle);
        mHeaderSet.setHeader(HeaderSet.TYPE, android.bluetooth.client.map.BluetoothMasRequestGetMessage.TYPE);
        android.bluetooth.client.map.utils.ObexAppParameters oap = new android.bluetooth.client.map.utils.ObexAppParameters();
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_CHARSET, android.bluetooth.client.map.BluetoothMasClient.CharsetType.UTF_8.equals(charset) ? android.bluetooth.client.map.BluetoothMasRequest.CHARSET_UTF8 : android.bluetooth.client.map.BluetoothMasRequest.CHARSET_NATIVE);
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_ATTACHMENT, attachment ? android.bluetooth.client.map.BluetoothMasRequest.ATTACHMENT_ON : android.bluetooth.client.map.BluetoothMasRequest.ATTACHMENT_OFF);
        oap.addToHeaderSet(mHeaderSet);
    }

    @java.lang.Override
    protected void readResponse(java.io.InputStream stream) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            int len;
            while ((len = stream.read(buf)) != (-1)) {
                baos.write(buf, 0, len);
            } 
        } catch (java.io.IOException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMasRequestGetMessage.TAG, "I/O exception while reading response", e);
        }
        // Convert the input stream using UTF-8 since the attributes in the payload are all encoded
        // according to it. The actual message body may need to be transcoded depending on
        // charset/encoding defined for body-content.
        java.lang.String bmsg;
        try {
            bmsg = baos.toString(java.nio.charset.StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException ex) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMasRequestGetMessage.TAG, "Coudn't decode the bmessage with UTF-8. Something must be really messed up.");
            return;
        }
        mBmessage = android.bluetooth.client.map.BluetoothMapBmessageParser.createBmessage(bmsg);
        if (mBmessage == null) {
            mResponseCode = javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
    }

    public android.bluetooth.client.map.BluetoothMapBmessage getMessage() {
        return mBmessage;
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) throws java.io.IOException {
        executeGet(session);
    }
}

