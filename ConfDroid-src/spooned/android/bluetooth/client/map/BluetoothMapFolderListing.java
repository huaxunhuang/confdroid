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


class BluetoothMapFolderListing {
    private static final java.lang.String TAG = "BluetoothMasFolderListing";

    private final java.util.ArrayList<java.lang.String> mFolders;

    public BluetoothMapFolderListing(java.io.InputStream in) {
        mFolders = new java.util.ArrayList<java.lang.String>();
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
                        if (xpp.getName().equals("folder")) {
                            mFolders.add(xpp.getAttributeValue(null, "name"));
                        }
                        break;
                }
                event = xpp.next();
            } 
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapFolderListing.TAG, "XML parser error when parsing XML", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMapFolderListing.TAG, "I/O error when parsing XML", e);
        }
    }

    public java.util.ArrayList<java.lang.String> getList() {
        return mFolders;
    }
}

