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


final class BluetoothMasRequestGetMessagesListingSize extends android.bluetooth.client.map.BluetoothMasRequest {
    private static final java.lang.String TYPE = "x-bt/MAP-msg-listing";

    private int mSize;

    public BluetoothMasRequestGetMessagesListingSize() {
        mHeaderSet.setHeader(HeaderSet.NAME, "");
        mHeaderSet.setHeader(HeaderSet.TYPE, android.bluetooth.client.map.BluetoothMasRequestGetMessagesListingSize.TYPE);
        android.bluetooth.client.map.utils.ObexAppParameters oap = new android.bluetooth.client.map.utils.ObexAppParameters();
        oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MAX_LIST_COUNT, ((short) (0)));
        oap.addToHeaderSet(mHeaderSet);
    }

    @java.lang.Override
    protected void readResponseHeaders(javax.obex.HeaderSet headerset) {
        android.bluetooth.client.map.utils.ObexAppParameters oap = android.bluetooth.client.map.utils.ObexAppParameters.fromHeaderSet(headerset);
        mSize = oap.getShort(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MESSAGES_LISTING_SIZE);
    }

    public int getSize() {
        return mSize;
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) throws java.io.IOException {
        executeGet(session);
    }
}

