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


/**
 * Object representation of event report received by MNS
 * <p>
 * This object will be received in {@link BluetoothMasClient#EVENT_EVENT_REPORT}
 * callback message.
 */
public class BluetoothMapEventReport {
    private static final java.lang.String TAG = "BluetoothMapEventReport";

    public enum Type {

        NEW_MESSAGE("NewMessage"),
        DELIVERY_SUCCESS("DeliverySuccess"),
        SENDING_SUCCESS("SendingSuccess"),
        DELIVERY_FAILURE("DeliveryFailure"),
        SENDING_FAILURE("SendingFailure"),
        MEMORY_FULL("MemoryFull"),
        MEMORY_AVAILABLE("MemoryAvailable"),
        MESSAGE_DELETED("MessageDeleted"),
        MESSAGE_SHIFT("MessageShift");
        private final java.lang.String mSpecName;

        private Type(java.lang.String specName) {
            mSpecName = specName;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return mSpecName;
        }
    }

    private final android.bluetooth.client.map.BluetoothMapEventReport.Type mType;

    private final java.lang.String mHandle;

    private final java.lang.String mFolder;

    private final java.lang.String mOldFolder;

    private final android.bluetooth.client.map.BluetoothMapBmessage.Type mMsgType;

    private BluetoothMapEventReport(java.util.HashMap<java.lang.String, java.lang.String> attrs) throws java.lang.IllegalArgumentException {
        mType = parseType(attrs.get("type"));
        if ((mType != android.bluetooth.client.map.BluetoothMapEventReport.Type.MEMORY_FULL) && (mType != android.bluetooth.client.map.BluetoothMapEventReport.Type.MEMORY_AVAILABLE)) {
            java.lang.String handle = attrs.get("handle");
            try {
                /* just to validate */
                new java.math.BigInteger(attrs.get("handle"), 16);
                mHandle = attrs.get("handle");
            } catch (java.lang.NumberFormatException e) {
                throw new java.lang.IllegalArgumentException("Invalid value for handle:" + handle);
            }
        } else {
            mHandle = null;
        }
        mFolder = attrs.get("folder");
        mOldFolder = attrs.get("old_folder");
        if ((mType != android.bluetooth.client.map.BluetoothMapEventReport.Type.MEMORY_FULL) && (mType != android.bluetooth.client.map.BluetoothMapEventReport.Type.MEMORY_AVAILABLE)) {
            java.lang.String s = attrs.get("msg_type");
            if ("".equals(s)) {
                // Some phones (e.g. SGS3 for MessageDeleted) send empty
                // msg_type, in such case leave it as null rather than throw
                // parse exception
                mMsgType = null;
            } else {
                mMsgType = parseMsgType(s);
            }
        } else {
            mMsgType = null;
        }
    }

    private android.bluetooth.client.map.BluetoothMapEventReport.Type parseType(java.lang.String type) throws java.lang.IllegalArgumentException {
        for (android.bluetooth.client.map.BluetoothMapEventReport.Type t : android.bluetooth.client.map.BluetoothMapEventReport.Type.values()) {
            if (t.toString().equals(type)) {
                return t;
            }
        }
        throw new java.lang.IllegalArgumentException("Invalid value for type: " + type);
    }

    private android.bluetooth.client.map.BluetoothMapBmessage.Type parseMsgType(java.lang.String msgType) throws java.lang.IllegalArgumentException {
        for (android.bluetooth.client.map.BluetoothMapBmessage.Type t : android.bluetooth.client.map.BluetoothMapBmessage.Type.values()) {
            if (t.name().equals(msgType)) {
                return t;
            }
        }
        throw new java.lang.IllegalArgumentException("Invalid value for msg_type: " + msgType);
    }

    /**
     *
     *
     * @return {@link BluetoothMapEventReport.Type} object corresponding to
    <code>type</code> application parameter in MAP specification
     */
    public android.bluetooth.client.map.BluetoothMapEventReport.Type getType() {
        return mType;
    }

    /**
     *
     *
     * @return value corresponding to <code>handle</code> parameter in MAP
    specification
     */
    public java.lang.String getHandle() {
        return mHandle;
    }

    /**
     *
     *
     * @return value corresponding to <code>folder</code> parameter in MAP
    specification
     */
    public java.lang.String getFolder() {
        return mFolder;
    }

    /**
     *
     *
     * @return value corresponding to <code>old_folder</code> parameter in MAP
    specification
     */
    public java.lang.String getOldFolder() {
        return mOldFolder;
    }

    /**
     *
     *
     * @return {@link BluetoothMapBmessage.Type} object corresponding to
    <code>msg_type</code> application parameter in MAP specification
     */
    public android.bluetooth.client.map.BluetoothMapBmessage.Type getMsgType() {
        return mMsgType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        org.json.JSONObject json = new org.json.JSONObject();
        try {
            json.put("type", mType);
            json.put("handle", mHandle);
            json.put("folder", mFolder);
            json.put("old_folder", mOldFolder);
            json.put("msg_type", mMsgType);
        } catch (org.json.JSONException e) {
            // do nothing
        }
        return json.toString();
    }

    static android.bluetooth.client.map.BluetoothMapEventReport fromStream(java.io.DataInputStream in) {
        android.bluetooth.client.map.BluetoothMapEventReport ev = null;
        try {
            org.xmlpull.v1.XmlPullParser xpp = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
            xpp.setInput(in, "utf-8");
            int event = xpp.getEventType();
            while (event != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case org.xmlpull.v1.XmlPullParser.START_TAG :
                        if (xpp.getName().equals("event")) {
                            java.util.HashMap<java.lang.String, java.lang.String> attrs = new java.util.HashMap<java.lang.String, java.lang.String>();
                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                attrs.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
                            }
                            ev = new android.bluetooth.client.map.BluetoothMapEventReport(attrs);
                            // return immediately, only one event should be here
                            return ev;
                        }
                        break;
                }
                event = xpp.next();
            } 
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapEventReport.TAG, "XML parser error when parsing XML", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapEventReport.TAG, "I/O error when parsing XML", e);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapEventReport.TAG, "Invalid event received", e);
        }
        return ev;
    }
}

