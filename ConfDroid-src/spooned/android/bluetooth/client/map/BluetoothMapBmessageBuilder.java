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


class BluetoothMapBmessageBuilder {
    private static final java.lang.String CRLF = "\r\n";

    private static final java.lang.String BMSG_BEGIN = "BEGIN:BMSG";

    private static final java.lang.String BMSG_VERSION = "VERSION:1.0";

    private static final java.lang.String BMSG_STATUS = "STATUS:";

    private static final java.lang.String BMSG_TYPE = "TYPE:";

    private static final java.lang.String BMSG_FOLDER = "FOLDER:";

    private static final java.lang.String BMSG_END = "END:BMSG";

    private static final java.lang.String BENV_BEGIN = "BEGIN:BENV";

    private static final java.lang.String BENV_END = "END:BENV";

    private static final java.lang.String BBODY_BEGIN = "BEGIN:BBODY";

    private static final java.lang.String BBODY_ENCODING = "ENCODING:";

    private static final java.lang.String BBODY_CHARSET = "CHARSET:";

    private static final java.lang.String BBODY_LANGUAGE = "LANGUAGE:";

    private static final java.lang.String BBODY_LENGTH = "LENGTH:";

    private static final java.lang.String BBODY_END = "END:BBODY";

    private static final java.lang.String MSG_BEGIN = "BEGIN:MSG";

    private static final java.lang.String MSG_END = "END:MSG";

    private static final java.lang.String VCARD_BEGIN = "BEGIN:VCARD";

    private static final java.lang.String VCARD_VERSION = "VERSION:2.1";

    private static final java.lang.String VCARD_N = "N:";

    private static final java.lang.String VCARD_EMAIL = "EMAIL:";

    private static final java.lang.String VCARD_TEL = "TEL:";

    private static final java.lang.String VCARD_END = "END:VCARD";

    private final java.lang.StringBuilder mBmsg;

    private BluetoothMapBmessageBuilder() {
        mBmsg = new java.lang.StringBuilder();
    }

    public static java.lang.String createBmessage(android.bluetooth.client.map.BluetoothMapBmessage bmsg) {
        android.bluetooth.client.map.BluetoothMapBmessageBuilder b = new android.bluetooth.client.map.BluetoothMapBmessageBuilder();
        b.build(bmsg);
        return b.mBmsg.toString();
    }

    private void build(android.bluetooth.client.map.BluetoothMapBmessage bmsg) {
        int bodyLen = ((android.bluetooth.client.map.BluetoothMapBmessageBuilder.MSG_BEGIN.length() + android.bluetooth.client.map.BluetoothMapBmessageBuilder.MSG_END.length()) + (3 * android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF.length())) + bmsg.mMessage.getBytes().length;
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BMSG_BEGIN).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BMSG_VERSION).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BMSG_STATUS).append(bmsg.mBmsgStatus).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BMSG_TYPE).append(bmsg.mBmsgType).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BMSG_FOLDER).append(bmsg.mBmsgFolder).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        for (com.android.vcard.VCardEntry vcard : bmsg.mOriginators) {
            buildVcard(vcard);
        }
        {
            mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BENV_BEGIN).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
            for (com.android.vcard.VCardEntry vcard : bmsg.mRecipients) {
                buildVcard(vcard);
            }
            {
                mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BBODY_BEGIN).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                if (bmsg.mBbodyEncoding != null) {
                    mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BBODY_ENCODING).append(bmsg.mBbodyEncoding).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                }
                if (bmsg.mBbodyCharset != null) {
                    mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BBODY_CHARSET).append(bmsg.mBbodyCharset).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                }
                if (bmsg.mBbodyLanguage != null) {
                    mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BBODY_LANGUAGE).append(bmsg.mBbodyLanguage).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                }
                mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BBODY_LENGTH).append(bodyLen).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                {
                    mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.MSG_BEGIN).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                    mBmsg.append(bmsg.mMessage).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                    mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.MSG_END).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
                }
                mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BBODY_END).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
            }
            mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BENV_END).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        }
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.BMSG_END).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
    }

    private void buildVcard(com.android.vcard.VCardEntry vcard) {
        java.lang.String n = buildVcardN(vcard);
        java.util.List<com.android.vcard.VCardEntry.PhoneData> tel = vcard.getPhoneList();
        java.util.List<com.android.vcard.VCardEntry.EmailData> email = vcard.getEmailList();
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.VCARD_BEGIN).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.VCARD_VERSION).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.VCARD_N).append(n).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        if ((tel != null) && (tel.size() > 0)) {
            mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.VCARD_TEL).append(tel.get(0).getNumber()).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        }
        if ((email != null) && (email.size() > 0)) {
            mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.VCARD_EMAIL).append(email.get(0).getAddress()).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
        }
        mBmsg.append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.VCARD_END).append(android.bluetooth.client.map.BluetoothMapBmessageBuilder.CRLF);
    }

    private java.lang.String buildVcardN(com.android.vcard.VCardEntry vcard) {
        com.android.vcard.VCardEntry.NameData nd = vcard.getNameData();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(nd.getFamily()).append(";");
        sb.append(nd.getGiven() == null ? "" : nd.getGiven()).append(";");
        sb.append(nd.getMiddle() == null ? "" : nd.getMiddle()).append(";");
        sb.append(nd.getPrefix() == null ? "" : nd.getPrefix()).append(";");
        sb.append(nd.getSuffix() == null ? "" : nd.getSuffix());
        return sb.toString();
    }
}

