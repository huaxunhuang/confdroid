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


final class BluetoothMasRequestGetMessagesListing extends android.bluetooth.client.map.BluetoothMasRequest {
    private static final java.lang.String TYPE = "x-bt/MAP-msg-listing";

    private android.bluetooth.client.map.BluetoothMapMessagesListing mResponse = null;

    private boolean mNewMessage = false;

    private java.util.Date mServerTime = null;

    public BluetoothMasRequestGetMessagesListing(java.lang.String folderName, int parameters, android.bluetooth.client.map.BluetoothMasClient.MessagesFilter filter, int subjectLength, int maxListCount, int listStartOffset) {
        if ((subjectLength < 0) || (subjectLength > 255)) {
            throw new java.lang.IllegalArgumentException("subjectLength should be [0..255]");
        }
        if ((maxListCount < 0) || (maxListCount > 65535)) {
            throw new java.lang.IllegalArgumentException("maxListCount should be [0..65535]");
        }
        if ((listStartOffset < 0) || (listStartOffset > 65535)) {
            throw new java.lang.IllegalArgumentException("listStartOffset should be [0..65535]");
        }
        mHeaderSet.setHeader(HeaderSet.TYPE, android.bluetooth.client.map.BluetoothMasRequestGetMessagesListing.TYPE);
        if (folderName == null) {
            mHeaderSet.setHeader(HeaderSet.NAME, "");
        } else {
            mHeaderSet.setHeader(HeaderSet.NAME, folderName);
        }
        android.bluetooth.client.map.utils.ObexAppParameters oap = new android.bluetooth.client.map.utils.ObexAppParameters();
        if (filter != null) {
            if (filter.messageType != android.bluetooth.client.map.BluetoothMasClient.MessagesFilter.MESSAGE_TYPE_ALL) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_MESSAGE_TYPE, filter.messageType);
            }
            if (filter.periodBegin != null) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_PERIOD_BEGIN, filter.periodBegin);
            }
            if (filter.periodEnd != null) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_PERIOD_END, filter.periodEnd);
            }
            if (filter.readStatus != android.bluetooth.client.map.BluetoothMasClient.MessagesFilter.READ_STATUS_ANY) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_READ_STATUS, filter.readStatus);
            }
            if (filter.recipient != null) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_RECIPIENT, filter.recipient);
            }
            if (filter.originator != null) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_ORIGINATOR, filter.originator);
            }
            if (filter.priority != android.bluetooth.client.map.BluetoothMasClient.MessagesFilter.PRIORITY_ANY) {
                oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_FILTER_PRIORITY, filter.priority);
            }
        }
        if (subjectLength != 0) {
            oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_SUBJECT_LENGTH, ((byte) (subjectLength)));
        }
        /* Include parameterMask only when specific values are selected,
        to avoid IOT specific issue with no paramterMask header support.
         */
        if (parameters > 0) {
            oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_PARAMETER_MASK, parameters);
        }
        // Allow GetMessageListing for maxlistcount value 0 also.
        if (maxListCount >= 0) {
            oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MAX_LIST_COUNT, ((short) (maxListCount)));
        }
        if (listStartOffset != 0) {
            oap.add(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_START_OFFSET, ((short) (listStartOffset)));
        }
        oap.addToHeaderSet(mHeaderSet);
    }

    @java.lang.Override
    protected void readResponse(java.io.InputStream stream) {
        mResponse = new android.bluetooth.client.map.BluetoothMapMessagesListing(stream);
    }

    @java.lang.Override
    protected void readResponseHeaders(javax.obex.HeaderSet headerset) {
        android.bluetooth.client.map.utils.ObexAppParameters oap = android.bluetooth.client.map.utils.ObexAppParameters.fromHeaderSet(headerset);
        mNewMessage = (oap.getByte(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_NEW_MESSAGE) & 0x1) == 1;
        if (oap.exists(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MSE_TIME)) {
            java.lang.String mseTime = oap.getString(android.bluetooth.client.map.BluetoothMasRequest.OAP_TAGID_MSE_TIME);
            if (mseTime != null)
                mServerTime = new android.bluetooth.client.map.utils.ObexTime(mseTime).getTime();

        }
    }

    public java.util.ArrayList<android.bluetooth.client.map.BluetoothMapMessage> getList() {
        if (mResponse == null) {
            return null;
        }
        return mResponse.getList();
    }

    public boolean getNewMessageStatus() {
        return mNewMessage;
    }

    public java.util.Date getMseTime() {
        return mServerTime;
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) throws java.io.IOException {
        executeGet(session);
    }
}

