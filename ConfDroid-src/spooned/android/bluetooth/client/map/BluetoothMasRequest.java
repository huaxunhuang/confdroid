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


abstract class BluetoothMasRequest {
    protected static final byte OAP_TAGID_MAX_LIST_COUNT = 0x1;

    protected static final byte OAP_TAGID_START_OFFSET = 0x2;

    protected static final byte OAP_TAGID_FILTER_MESSAGE_TYPE = 0x3;

    protected static final byte OAP_TAGID_FILTER_PERIOD_BEGIN = 0x4;

    protected static final byte OAP_TAGID_FILTER_PERIOD_END = 0x5;

    protected static final byte OAP_TAGID_FILTER_READ_STATUS = 0x6;

    protected static final byte OAP_TAGID_FILTER_RECIPIENT = 0x7;

    protected static final byte OAP_TAGID_FILTER_ORIGINATOR = 0x8;

    protected static final byte OAP_TAGID_FILTER_PRIORITY = 0x9;

    protected static final byte OAP_TAGID_ATTACHMENT = 0xa;

    protected static final byte OAP_TAGID_TRANSPARENT = 0xb;

    protected static final byte OAP_TAGID_RETRY = 0xc;

    protected static final byte OAP_TAGID_NEW_MESSAGE = 0xd;

    protected static final byte OAP_TAGID_NOTIFICATION_STATUS = 0xe;

    protected static final byte OAP_TAGID_MAS_INSTANCE_ID = 0xf;

    protected static final byte OAP_TAGID_PARAMETER_MASK = 0x10;

    protected static final byte OAP_TAGID_FOLDER_LISTING_SIZE = 0x11;

    protected static final byte OAP_TAGID_MESSAGES_LISTING_SIZE = 0x12;

    protected static final byte OAP_TAGID_SUBJECT_LENGTH = 0x13;

    protected static final byte OAP_TAGID_CHARSET = 0x14;

    protected static final byte OAP_TAGID_STATUS_INDICATOR = 0x17;

    protected static final byte OAP_TAGID_STATUS_VALUE = 0x18;

    protected static final byte OAP_TAGID_MSE_TIME = 0x19;

    protected static byte NOTIFICATION_ON = 0x1;

    protected static byte NOTIFICATION_OFF = 0x0;

    protected static byte ATTACHMENT_ON = 0x1;

    protected static byte ATTACHMENT_OFF = 0x0;

    protected static byte CHARSET_NATIVE = 0x0;

    protected static byte CHARSET_UTF8 = 0x1;

    protected static byte STATUS_INDICATOR_READ = 0x0;

    protected static byte STATUS_INDICATOR_DELETED = 0x1;

    protected static byte STATUS_NO = 0x0;

    protected static byte STATUS_YES = 0x1;

    protected static byte TRANSPARENT_OFF = 0x0;

    protected static byte TRANSPARENT_ON = 0x1;

    protected static byte RETRY_OFF = 0x0;

    protected static byte RETRY_ON = 0x1;

    /* used for PUT requests which require filler byte */
    protected static final byte[] FILLER_BYTE = new byte[]{ 0x30 };

    protected javax.obex.HeaderSet mHeaderSet;

    protected int mResponseCode;

    public BluetoothMasRequest() {
        mHeaderSet = new javax.obex.HeaderSet();
    }

    public abstract void execute(javax.obex.ClientSession session) throws java.io.IOException;

    protected void executeGet(javax.obex.ClientSession session) throws java.io.IOException {
        javax.obex.ClientOperation op = null;
        try {
            op = ((javax.obex.ClientOperation) (session.get(mHeaderSet)));
            /* MAP spec does not explicitly require that GET request should be
            sent in single packet but for some reason PTS complains when
            final GET packet with no headers follows non-final GET with all
            headers. So this is workaround, at least temporary. TODO: check
            with PTS
             */
            op.setGetFinalFlag(true);
            /* this will trigger ClientOperation to use non-buffered stream so
            we can abort operation
             */
            op.continueOperation(true, false);
            readResponseHeaders(op.getReceivedHeader());
            java.io.InputStream is = op.openInputStream();
            readResponse(is);
            is.close();
            op.close();
            mResponseCode = op.getResponseCode();
        } catch (java.io.IOException e) {
            mResponseCode = javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
            throw e;
        }
    }

    protected void executePut(javax.obex.ClientSession session, byte[] body) throws java.io.IOException {
        javax.obex.Operation op = null;
        mHeaderSet.setHeader(HeaderSet.LENGTH, java.lang.Long.valueOf(body.length));
        try {
            op = session.put(mHeaderSet);
            java.io.DataOutputStream out = op.openDataOutputStream();
            out.write(body);
            out.close();
            readResponseHeaders(op.getReceivedHeader());
            op.close();
            mResponseCode = op.getResponseCode();
        } catch (java.io.IOException e) {
            mResponseCode = javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
            throw e;
        }
    }

    public final boolean isSuccess() {
        return mResponseCode == javax.obex.ResponseCodes.OBEX_HTTP_OK;
    }

    protected void readResponse(java.io.InputStream stream) throws java.io.IOException {
        /* nothing here by default */
    }

    protected void readResponseHeaders(javax.obex.HeaderSet headerset) {
        /* nothing here by default */
    }
}

