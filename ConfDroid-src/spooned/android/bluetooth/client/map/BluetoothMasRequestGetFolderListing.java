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


final class BluetoothMasRequestGetFolderListing extends android.bluetooth.client.map.BluetoothMasRequest {
    private static final java.lang.String TYPE = "x-obex/folder-listing";

    private android.bluetooth.client.map.BluetoothMapFolderListing mResponse = null;

    public BluetoothMasRequestGetFolderListing(int maxListCount, int listStartOffset) {
        if ((maxListCount < 0) || (maxListCount > 65535)) {
            throw new java.lang.IllegalArgumentException("maxListCount should be [0..65535]");
        }
        if ((listStartOffset < 0) || (listStartOffset > 65535)) {
            throw new java.lang.IllegalArgumentException("listStartOffset should be [0..65535]");
        }
        mHeaderSet.setHeader(HeaderSet.TYPE, android.bluetooth.client.map.BluetoothMasRequestGetFolderListing.TYPE);
        android.bluetooth.client.map.utils.ObexAppParameters oap = new android.bluetooth.client.map.utils.ObexAppParameters();
        // Allow GetFolderListing for maxListCount value 0 also.
        if (maxListCount >= 0) {
            oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MAX_LIST_COUNT, ((short) (maxListCount)));
        }
        if (listStartOffset > 0) {
            oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_START_OFFSET, ((short) (listStartOffset)));
        }
        oap.addToHeaderSet(mHeaderSet);
    }

    @java.lang.Override
    protected void readResponse(java.io.InputStream stream) {
        mResponse = new android.bluetooth.client.map.BluetoothMapFolderListing(stream);
    }

    public java.util.ArrayList<java.lang.String> getList() {
        if (mResponse == null) {
            return null;
        }
        return mResponse.getList();
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) throws java.io.IOException {
        executeGet(session);
    }
}

