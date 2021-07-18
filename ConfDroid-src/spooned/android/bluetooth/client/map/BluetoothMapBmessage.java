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
 * Object representation of message in bMessage format
 * <p>
 * This object will be received in {@link BluetoothMasClient#EVENT_GET_MESSAGE}
 * callback message.
 */
public class BluetoothMapBmessage {
    java.lang.String mBmsgVersion;

    android.bluetooth.client.map.BluetoothMapBmessage.Status mBmsgStatus;

    android.bluetooth.client.map.BluetoothMapBmessage.Type mBmsgType;

    java.lang.String mBmsgFolder;

    java.lang.String mBbodyEncoding;

    java.lang.String mBbodyCharset;

    java.lang.String mBbodyLanguage;

    int mBbodyLength;

    java.lang.String mMessage;

    java.util.ArrayList<com.android.vcard.VCardEntry> mOriginators;

    java.util.ArrayList<com.android.vcard.VCardEntry> mRecipients;

    public enum Status {

        READ,
        UNREAD;}

    public enum Type {

        EMAIL,
        SMS_GSM,
        SMS_CDMA,
        MMS;}

    /**
     * Constructs empty message object
     */
    public BluetoothMapBmessage() {
        mOriginators = new java.util.ArrayList<com.android.vcard.VCardEntry>();
        mRecipients = new java.util.ArrayList<com.android.vcard.VCardEntry>();
    }

    public com.android.vcard.VCardEntry getOriginator() {
        if (mOriginators.size() > 0) {
            return mOriginators.get(0);
        } else {
            return null;
        }
    }

    public java.util.ArrayList<com.android.vcard.VCardEntry> getOriginators() {
        return mOriginators;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage addOriginator(com.android.vcard.VCardEntry vcard) {
        mOriginators.add(vcard);
        return this;
    }

    public java.util.ArrayList<com.android.vcard.VCardEntry> getRecipients() {
        return mRecipients;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage addRecipient(com.android.vcard.VCardEntry vcard) {
        mRecipients.add(vcard);
        return this;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage.Status getStatus() {
        return mBmsgStatus;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setStatus(android.bluetooth.client.map.BluetoothMapBmessage.Status status) {
        mBmsgStatus = status;
        return this;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage.Type getType() {
        return mBmsgType;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setType(android.bluetooth.client.map.BluetoothMapBmessage.Type type) {
        mBmsgType = type;
        return this;
    }

    public java.lang.String getFolder() {
        return mBmsgFolder;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setFolder(java.lang.String folder) {
        mBmsgFolder = folder;
        return this;
    }

    public java.lang.String getEncoding() {
        return mBbodyEncoding;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setEncoding(java.lang.String encoding) {
        mBbodyEncoding = encoding;
        return this;
    }

    public java.lang.String getCharset() {
        return mBbodyCharset;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setCharset(java.lang.String charset) {
        mBbodyCharset = charset;
        return this;
    }

    public java.lang.String getLanguage() {
        return mBbodyLanguage;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setLanguage(java.lang.String language) {
        mBbodyLanguage = language;
        return this;
    }

    public java.lang.String getBodyContent() {
        return mMessage;
    }

    public android.bluetooth.client.map.BluetoothMapBmessage setBodyContent(java.lang.String body) {
        mMessage = body;
        return this;
    }

    @java.lang.Override
    public java.lang.String toString() {
        org.json.JSONObject json = new org.json.JSONObject();
        try {
            json.put("status", mBmsgStatus);
            json.put("type", mBmsgType);
            json.put("folder", mBmsgFolder);
            json.put("charset", mBbodyCharset);
            json.put("message", mMessage);
        } catch (org.json.JSONException e) {
            // do nothing
        }
        return json.toString();
    }
}

