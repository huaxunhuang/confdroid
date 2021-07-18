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


final class BluetoothMasRequestSetMessageStatus extends android.bluetooth.client.map.BluetoothMasRequest {
    public enum StatusIndicator {

        READ,
        DELETED;}

    private static final java.lang.String TYPE = "x-bt/messageStatus";

    public BluetoothMasRequestSetMessageStatus(java.lang.String handle, android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus.StatusIndicator statusInd, boolean statusValue) {
        mHeaderSet.setHeader(HeaderSet.TYPE, android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus.TYPE);
        mHeaderSet.setHeader(HeaderSet.NAME, handle);
        android.bluetooth.client.map.utils.ObexAppParameters oap = new android.bluetooth.client.map.utils.ObexAppParameters();
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_STATUS_INDICATOR, statusInd == android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus.StatusIndicator.READ ? android.bluetooth.client.map.BluetoothMasRequest.STATUS_INDICATOR_READ : android.bluetooth.client.map.BluetoothMasRequest.STATUS_INDICATOR_DELETED);
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_STATUS_VALUE, statusValue ? android.bluetooth.client.map.BluetoothMasRequest.STATUS_YES : android.bluetooth.client.map.BluetoothMasRequest.STATUS_NO);
        oap.addToHeaderSet(mHeaderSet);
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) throws java.io.IOException {
        executePut(session, android.bluetooth.client.map.BluetoothMasRequest.FILLER_BYTE);
    }
}

