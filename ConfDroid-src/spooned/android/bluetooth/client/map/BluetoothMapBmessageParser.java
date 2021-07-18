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


class BluetoothMapBmessageParser {
    private static final java.lang.String TAG = "BluetoothMapBmessageParser";

    private static final boolean DBG = false;

    private static final java.lang.String CRLF = "\r\n";

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property BEGIN_BMSG = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("BEGIN", "BMSG");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property END_BMSG = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("END", "BMSG");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property BEGIN_VCARD = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("BEGIN", "VCARD");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property END_VCARD = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("END", "VCARD");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property BEGIN_BENV = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("BEGIN", "BENV");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property END_BENV = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("END", "BENV");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property BEGIN_BBODY = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("BEGIN", "BBODY");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property END_BBODY = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("END", "BBODY");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property BEGIN_MSG = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("BEGIN", "MSG");

    private static final android.bluetooth.client.map.utils.BmsgTokenizer.Property END_MSG = new android.bluetooth.client.map.utils.BmsgTokenizer.Property("END", "MSG");

    private static final int CRLF_LEN = 2;

    /* length of "container" for 'message' in bmessage-body-content:
    BEGIN:MSG<CRLF> + <CRLF> + END:MSG<CRFL>
     */
    private static final int MSG_CONTAINER_LEN = 22;

    private android.bluetooth.client.map.utils.BmsgTokenizer mParser;

    private final android.bluetooth.client.map.BluetoothMapBmessage mBmsg;

    private BluetoothMapBmessageParser() {
        mBmsg = new android.bluetooth.client.map.BluetoothMapBmessage();
    }

    public static android.bluetooth.client.map.BluetoothMapBmessage createBmessage(java.lang.String str) {
        android.bluetooth.client.map.BluetoothMapBmessageParser p = new android.bluetooth.client.map.BluetoothMapBmessageParser();
        if (android.bluetooth.client.map.BluetoothMapBmessageParser.DBG) {
            android.util.Log.d(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "actual wired contents: " + str);
        }
        try {
            p.parse(str);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "I/O exception when parsing bMessage", e);
            return null;
        } catch (java.text.ParseException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "Cannot parse bMessage", e);
            return null;
        }
        return p.mBmsg;
    }

    private java.text.ParseException expected(android.bluetooth.client.map.utils.BmsgTokenizer.Property... props) {
        boolean first = true;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.bluetooth.client.map.utils.BmsgTokenizer.Property prop : props) {
            if (!first) {
                sb.append(" or ");
            }
            sb.append(prop);
            first = false;
        }
        return new java.text.ParseException("Expected: " + sb.toString(), mParser.pos());
    }

    private void parse(java.lang.String str) throws java.io.IOException, java.text.ParseException {
        android.bluetooth.client.map.utils.BmsgTokenizer.Property prop;
        /* <bmessage-object>::= { "BEGIN:BMSG" <CRLF> <bmessage-property>
        [<bmessage-originator>]* <bmessage-envelope> "END:BMSG" <CRLF> }
         */
        mParser = new android.bluetooth.client.map.utils.BmsgTokenizer(str + android.bluetooth.client.map.BluetoothMapBmessageParser.CRLF);
        prop = mParser.next();
        if (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BMSG)) {
            throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BMSG);
        }
        prop = parseProperties();
        while (prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_VCARD)) {
            /* <bmessage-originator>::= <vcard> <CRLF> */
            java.lang.StringBuilder vcard = new java.lang.StringBuilder();
            prop = extractVcard(vcard);
            com.android.vcard.VCardEntry entry = parseVcard(vcard.toString());
            mBmsg.mOriginators.add(entry);
        } 
        if (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BENV)) {
            throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BENV);
        }
        prop = parseEnvelope(1);
        if (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.END_BMSG)) {
            throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.END_BENV);
        }
        /* there should be no meaningful data left in stream here so we just
        ignore whatever is left
         */
        mParser = null;
    }

    private android.bluetooth.client.map.utils.BmsgTokenizer.Property parseProperties() throws java.text.ParseException {
        android.bluetooth.client.map.utils.BmsgTokenizer.Property prop;
        /* <bmessage-property>::=<bmessage-version-property>
        <bmessage-readstatus-property> <bmessage-type-property>
        <bmessage-folder-property> <bmessage-version-property>::="VERSION:"
        <common-digit>*"."<common-digit>* <CRLF>
        <bmessage-readstatus-property>::="STATUS:" 'readstatus' <CRLF>
        <bmessage-type-property>::="TYPE:" 'type' <CRLF>
        <bmessage-folder-property>::="FOLDER:" 'foldername' <CRLF>
         */
        do {
            prop = mParser.next();
            if (prop.name.equals("VERSION")) {
                mBmsg.mBmsgVersion = prop.value;
            } else
                if (prop.name.equals("STATUS")) {
                    for (android.bluetooth.client.map.BluetoothMapBmessage.Status s : android.bluetooth.client.map.BluetoothMapBmessage.Status.values()) {
                        if (prop.value.equals(s.toString())) {
                            mBmsg.mBmsgStatus = s;
                            break;
                        }
                    }
                } else
                    if (prop.name.equals("TYPE")) {
                        for (android.bluetooth.client.map.BluetoothMapBmessage.Type t : android.bluetooth.client.map.BluetoothMapBmessage.Type.values()) {
                            if (prop.value.equals(t.toString())) {
                                mBmsg.mBmsgType = t;
                                break;
                            }
                        }
                    } else
                        if (prop.name.equals("FOLDER")) {
                            mBmsg.mBmsgFolder = prop.value;
                        }



        } while ((!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_VCARD)) && (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BENV)) );
        return prop;
    }

    private android.bluetooth.client.map.utils.BmsgTokenizer.Property parseEnvelope(int level) throws java.io.IOException, java.text.ParseException {
        android.bluetooth.client.map.utils.BmsgTokenizer.Property prop;
        /* we can support as many nesting level as we want, but MAP spec clearly
        defines that there should be no more than 3 levels. so we verify it
        here.
         */
        if (level > 3) {
            throw new java.text.ParseException("bEnvelope is nested more than 3 times", mParser.pos());
        }
        /* <bmessage-envelope> ::= { "BEGIN:BENV" <CRLF> [<bmessage-recipient>]*
        <bmessage-envelope> | <bmessage-content> "END:BENV" <CRLF> }
         */
        prop = mParser.next();
        while (prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_VCARD)) {
            /* <bmessage-originator>::= <vcard> <CRLF> */
            java.lang.StringBuilder vcard = new java.lang.StringBuilder();
            prop = extractVcard(vcard);
            if (level == 1) {
                com.android.vcard.VCardEntry entry = parseVcard(vcard.toString());
                mBmsg.mRecipients.add(entry);
            }
        } 
        if (prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BENV)) {
            prop = parseEnvelope(level + 1);
        } else
            if (prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BBODY)) {
                prop = parseBody();
            } else {
                throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BENV, android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_BBODY);
            }

        if (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.END_BENV)) {
            throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.END_BENV);
        }
        return mParser.next();
    }

    private android.bluetooth.client.map.utils.BmsgTokenizer.Property parseBody() throws java.io.IOException, java.text.ParseException {
        android.bluetooth.client.map.utils.BmsgTokenizer.Property prop;
        /* <bmessage-content>::= { "BEGIN:BBODY"<CRLF> [<bmessage-body-part-ID>
        <CRLF>] <bmessage-body-property> <bmessage-body-content>* <CRLF>
        "END:BBODY"<CRLF> } <bmessage-body-part-ID>::="PARTID:" 'Part-ID'
        <bmessage-body-property>::=[<bmessage-body-encoding-property>]
        [<bmessage-body-charset-property>]
        [<bmessage-body-language-property>]
        <bmessage-body-content-length-property>
        <bmessage-body-encoding-property>::="ENCODING:"'encoding' <CRLF>
        <bmessage-body-charset-property>::="CHARSET:"'charset' <CRLF>
        <bmessage-body-language-property>::="LANGUAGE:"'language' <CRLF>
        <bmessage-body-content-length-property>::= "LENGTH:" <common-digit>*
        <CRLF>
         */
        do {
            prop = mParser.next();
            if (prop.name.equals("PARTID")) {
            } else
                if (prop.name.equals("ENCODING")) {
                    mBmsg.mBbodyEncoding = prop.value;
                } else
                    if (prop.name.equals("CHARSET")) {
                        mBmsg.mBbodyCharset = prop.value;
                    } else
                        if (prop.name.equals("LANGUAGE")) {
                            mBmsg.mBbodyLanguage = prop.value;
                        } else
                            if (prop.name.equals("LENGTH")) {
                                try {
                                    mBmsg.mBbodyLength = java.lang.Integer.parseInt(prop.value);
                                } catch (java.lang.NumberFormatException e) {
                                    throw new java.text.ParseException("Invalid LENGTH value", mParser.pos());
                                }
                            }




        } while (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_MSG) );
        /* check that the charset is always set to UTF-8. We expect only text transfer (in lieu with
        the MAPv12 specifying only RFC2822 (text only) for MMS/EMAIL and SMS do not support
        non-text content. If the charset is not set to UTF-8, it is safe to set the message as
        empty. We force the getMessage (see BluetoothMasClient) to only call getMessage with
        UTF-8 as the MCE is not obliged to support native charset.
         */
        if (!mBmsg.mBbodyCharset.equals("UTF-8")) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "The charset was not set to charset UTF-8: " + mBmsg.mBbodyCharset);
        }
        /* <bmessage-body-content>::={ "BEGIN:MSG"<CRLF> 'message'<CRLF>
        "END:MSG"<CRLF> }
         */
        int messageLen = mBmsg.mBbodyLength - android.bluetooth.client.map.BluetoothMapBmessageParser.MSG_CONTAINER_LEN;
        int offset = messageLen + android.bluetooth.client.map.BluetoothMapBmessageParser.CRLF_LEN;
        int restartPos = mParser.pos() + offset;
        /* length is specified in bytes so we need to convert from unicode
        string back to bytes array
         */
        java.lang.String remng = mParser.remaining();
        byte[] data = remng.getBytes();
        /* restart parsing from after 'message'<CRLF> */
        mParser = new android.bluetooth.client.map.utils.BmsgTokenizer(new java.lang.String(data, offset, data.length - offset), restartPos);
        prop = mParser.next(true);
        if (prop != null) {
            if (prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.END_MSG)) {
                if (mBmsg.mBbodyCharset.equals("UTF-8")) {
                    mBmsg.mMessage = new java.lang.String(data, 0, messageLen, java.nio.charset.StandardCharsets.UTF_8);
                } else {
                    mBmsg.mMessage = null;
                }
            } else {
                /* Handle possible exception for incorrect LENGTH value
                from MSE while parsing  GET Message response
                 */
                android.util.Log.e(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "Prop Invalid: " + prop.toString());
                android.util.Log.e(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "Possible Invalid LENGTH value");
                throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.END_MSG);
            }
        } else {
            data = null;
            /* now we check if bMessage can be parsed if LENGTH is handled as
            number of characters instead of number of bytes
             */
            if ((offset < 0) || (offset > remng.length())) {
                /* Handle possible exception for incorrect LENGTH value
                from MSE while parsing  GET Message response
                 */
                throw new java.text.ParseException("Invalid LENGTH value", mParser.pos());
            }
            android.util.Log.w(android.bluetooth.client.map.BluetoothMapBmessageParser.TAG, "byte LENGTH seems to be invalid, trying with char length");
            mParser = new android.bluetooth.client.map.utils.BmsgTokenizer(remng.substring(offset));
            prop = mParser.next();
            if (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.END_MSG)) {
                throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.END_MSG);
            }
            if (mBmsg.mBbodyCharset.equals("UTF-8")) {
                mBmsg.mMessage = remng.substring(0, messageLen);
            } else {
                mBmsg.mMessage = null;
            }
        }
        prop = mParser.next();
        if (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.END_BBODY)) {
            throw expected(android.bluetooth.client.map.BluetoothMapBmessageParser.END_BBODY);
        }
        return mParser.next();
    }

    private android.bluetooth.client.map.utils.BmsgTokenizer.Property extractVcard(java.lang.StringBuilder out) throws java.io.IOException, java.text.ParseException {
        android.bluetooth.client.map.utils.BmsgTokenizer.Property prop;
        out.append(android.bluetooth.client.map.BluetoothMapBmessageParser.BEGIN_VCARD).append(android.bluetooth.client.map.BluetoothMapBmessageParser.CRLF);
        do {
            prop = mParser.next();
            out.append(prop).append(android.bluetooth.client.map.BluetoothMapBmessageParser.CRLF);
        } while (!prop.equals(android.bluetooth.client.map.BluetoothMapBmessageParser.END_VCARD) );
        return mParser.next();
    }

    private class VcardHandler implements com.android.vcard.VCardEntryHandler {
        com.android.vcard.VCardEntry vcard;

        @java.lang.Override
        public void onStart() {
        }

        @java.lang.Override
        public void onEntryCreated(com.android.vcard.VCardEntry entry) {
            vcard = entry;
        }

        @java.lang.Override
        public void onEnd() {
        }
    }

    private com.android.vcard.VCardEntry parseVcard(java.lang.String str) throws java.io.IOException, java.text.ParseException {
        com.android.vcard.VCardEntry vcard = null;
        try {
            com.android.vcard.VCardParser p = new com.android.vcard.VCardParser_V21();
            com.android.vcard.VCardEntryConstructor c = new com.android.vcard.VCardEntryConstructor();
            android.bluetooth.client.map.BluetoothMapBmessageParser.VcardHandler handler = new android.bluetooth.client.map.BluetoothMapBmessageParser.VcardHandler();
            c.addEntryHandler(handler);
            p.addInterpreter(c);
            p.parse(new java.io.ByteArrayInputStream(str.getBytes()));
            vcard = handler.vcard;
        } catch (com.android.vcard.exception.VCardVersionException e1) {
            try {
                com.android.vcard.VCardParser p = new com.android.vcard.VCardParser_V30();
                com.android.vcard.VCardEntryConstructor c = new com.android.vcard.VCardEntryConstructor();
                android.bluetooth.client.map.BluetoothMapBmessageParser.VcardHandler handler = new android.bluetooth.client.map.BluetoothMapBmessageParser.VcardHandler();
                c.addEntryHandler(handler);
                p.addInterpreter(c);
                p.parse(new java.io.ByteArrayInputStream(str.getBytes()));
                vcard = handler.vcard;
            } catch (com.android.vcard.exception.VCardVersionException e2) {
                // will throw below
            } catch (com.android.vcard.exception.VCardException e2) {
                // will throw below
            }
        } catch (com.android.vcard.exception.VCardException e1) {
            // will throw below
        }
        if (vcard == null) {
            throw new java.text.ParseException("Cannot parse vCard object (neither 2.1 nor 3.0?)", mParser.pos());
        }
        return vcard;
    }
}

