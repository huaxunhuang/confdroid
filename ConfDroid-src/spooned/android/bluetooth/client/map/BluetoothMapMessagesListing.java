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


class BluetoothMapMessagesListing {
    private static final java.lang.String TAG = "BluetoothMapMessagesListing";

    private final java.util.ArrayList<android.bluetooth.client.map.BluetoothMapMessage> mMessages;

    public BluetoothMapMessagesListing(java.io.InputStream in) {
        mMessages = new java.util.ArrayList<android.bluetooth.client.map.BluetoothMapMessage>();
        parse(in);
    }

    public void parse(java.io.InputStream in) {
        try {
            org.xmlpull.v1.XmlPullParser xpp = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
            xpp.setInput(in, "utf-8");
            int event = xpp.getEventType();
            while (event != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case org.xmlpull.v1.XmlPullParser.START_TAG :
                        if (xpp.getName().equals("msg")) {
                            java.util.HashMap<java.lang.String, java.lang.String> attrs = new java.util.HashMap<java.lang.String, java.lang.String>();
                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                attrs.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
                            }
                            try {
                                android.bluetooth.client.map.BluetoothMapMessage msg = new android.bluetooth.client.map.BluetoothMapMessage(attrs);
                                mMessages.add(msg);
                            } catch (java.lang.IllegalArgumentException e) {
                                /* TODO: provide something more useful here */
                                android.util.Log.w(android.bluetooth.client.map.BluetoothMapMessagesListing.TAG, "Invalid <msg/>");
                            }
                        }
                        break;
                }
                event = xpp.next();
            } 
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapMessagesListing.TAG, "XML parser error when parsing XML", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapMessagesListing.TAG, "I/O error when parsing XML", e);
        }
    }

    public java.util.ArrayList<android.bluetooth.client.map.BluetoothMapMessage> getList() {
        return mMessages;
    }
}

