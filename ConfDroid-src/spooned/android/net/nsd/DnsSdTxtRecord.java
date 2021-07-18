/**
 * -*- Mode: Java; tab-width: 4 -*-
 *
 * Copyright (c) 2004 Apple Computer, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To do:
 * - implement remove()
 * - fix set() to replace existing values
 */
package android.net.nsd;


/**
 * This class handles TXT record data for DNS based service discovery as specified at
 * http://tools.ietf.org/html/draft-cheshire-dnsext-dns-sd-11
 *
 * DNS-SD specifies that a TXT record corresponding to an SRV record consist of
 * a packed array of bytes, each preceded by a length byte. Each string
 * is an attribute-value pair.
 *
 * The DnsSdTxtRecord object stores the entire TXT data as a single byte array, traversing it
 * as need be to implement its various methods.
 *
 * @unknown 
 */
public class DnsSdTxtRecord implements android.os.Parcelable {
    private static final byte mSeperator = '=';

    private byte[] mData;

    /**
     * Constructs a new, empty TXT record.
     */
    public DnsSdTxtRecord() {
        mData = new byte[0];
    }

    /**
     * Constructs a new TXT record from a byte array in the standard format.
     */
    public DnsSdTxtRecord(byte[] data) {
        mData = ((byte[]) (data.clone()));
    }

    /**
     * Copy constructor
     */
    public DnsSdTxtRecord(android.net.nsd.DnsSdTxtRecord src) {
        if ((src != null) && (src.mData != null)) {
            mData = ((byte[]) (src.mData.clone()));
        }
    }

    /**
     * Set a key/value pair. Setting an existing key will replace its value.
     *
     * @param key
     * 		Must be ascii with no '='
     * @param value
     * 		matching value to key
     */
    public void set(java.lang.String key, java.lang.String value) {
        byte[] keyBytes;
        byte[] valBytes;
        int valLen;
        if (value != null) {
            valBytes = value.getBytes();
            valLen = valBytes.length;
        } else {
            valBytes = null;
            valLen = 0;
        }
        try {
            keyBytes = key.getBytes("US-ASCII");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new java.lang.IllegalArgumentException("key should be US-ASCII");
        }
        for (int i = 0; i < keyBytes.length; i++) {
            if (keyBytes[i] == '=') {
                throw new java.lang.IllegalArgumentException("= is not a valid character in key");
            }
        }
        if ((keyBytes.length + valLen) >= 255) {
            throw new java.lang.IllegalArgumentException("Key and Value length cannot exceed 255 bytes");
        }
        int currentLoc = remove(key);
        if (currentLoc == (-1))
            currentLoc = keyCount();

        insert(keyBytes, valBytes, currentLoc);
    }

    /**
     * Get a value for a key
     *
     * @param key
     * 		
     * @return The value associated with the key
     */
    public java.lang.String get(java.lang.String key) {
        byte[] val = this.getValue(key);
        return val != null ? new java.lang.String(val) : null;
    }

    /**
     * Remove a key/value pair. If found, returns the index or -1 if not found
     */
    public int remove(java.lang.String key) {
        int avStart = 0;
        for (int i = 0; avStart < mData.length; i++) {
            int avLen = mData[avStart];
            if ((key.length() <= avLen) && ((key.length() == avLen) || (mData[(avStart + key.length()) + 1] == android.net.nsd.DnsSdTxtRecord.mSeperator))) {
                java.lang.String s = new java.lang.String(mData, avStart + 1, key.length());
                if (0 == key.compareToIgnoreCase(s)) {
                    byte[] oldBytes = mData;
                    mData = new byte[(oldBytes.length - avLen) - 1];
                    java.lang.System.arraycopy(oldBytes, 0, mData, 0, avStart);
                    java.lang.System.arraycopy(oldBytes, (avStart + avLen) + 1, mData, avStart, ((oldBytes.length - avStart) - avLen) - 1);
                    return i;
                }
            }
            avStart += 0xff & (avLen + 1);
        }
        return -1;
    }

    /**
     * Return the count of keys
     */
    public int keyCount() {
        int count = 0;
        int nextKey;
        for (nextKey = 0; nextKey < mData.length; count++) {
            nextKey += 0xff & (mData[nextKey] + 1);
        }
        return count;
    }

    /**
     * Return true if key is present, false if not.
     */
    public boolean contains(java.lang.String key) {
        java.lang.String s = null;
        for (int i = 0; null != (s = this.getKey(i)); i++) {
            if (0 == key.compareToIgnoreCase(s))
                return true;

        }
        return false;
    }

    /* Gets the size in bytes */
    public int size() {
        return mData.length;
    }

    /* Gets the raw data in bytes */
    public byte[] getRawData() {
        return ((byte[]) (mData.clone()));
    }

    private void insert(byte[] keyBytes, byte[] value, int index) {
        byte[] oldBytes = mData;
        int valLen = (value != null) ? value.length : 0;
        int insertion = 0;
        int newLen;
        int avLen;
        for (int i = 0; (i < index) && (insertion < mData.length); i++) {
            insertion += 0xff & (mData[insertion] + 1);
        }
        avLen = (keyBytes.length + valLen) + (value != null ? 1 : 0);
        newLen = (avLen + oldBytes.length) + 1;
        mData = new byte[newLen];
        java.lang.System.arraycopy(oldBytes, 0, mData, 0, insertion);
        int secondHalfLen = oldBytes.length - insertion;
        java.lang.System.arraycopy(oldBytes, insertion, mData, newLen - secondHalfLen, secondHalfLen);
        mData[insertion] = ((byte) (avLen));
        java.lang.System.arraycopy(keyBytes, 0, mData, insertion + 1, keyBytes.length);
        if (value != null) {
            mData[(insertion + 1) + keyBytes.length] = android.net.nsd.DnsSdTxtRecord.mSeperator;
            java.lang.System.arraycopy(value, 0, mData, (insertion + keyBytes.length) + 2, valLen);
        }
    }

    /**
     * Return a key in the TXT record by zero-based index. Returns null if index exceeds the total number of keys.
     */
    private java.lang.String getKey(int index) {
        int avStart = 0;
        for (int i = 0; (i < index) && (avStart < mData.length); i++) {
            avStart += mData[avStart] + 1;
        }
        if (avStart < mData.length) {
            int avLen = mData[avStart];
            int aLen = 0;
            for (aLen = 0; aLen < avLen; aLen++) {
                if (mData[(avStart + aLen) + 1] == android.net.nsd.DnsSdTxtRecord.mSeperator)
                    break;

            }
            return new java.lang.String(mData, avStart + 1, aLen);
        }
        return null;
    }

    /**
     * Look up a key in the TXT record by zero-based index and return its value.
     * Returns null if index exceeds the total number of keys.
     * Returns null if the key is present with no value.
     */
    private byte[] getValue(int index) {
        int avStart = 0;
        byte[] value = null;
        for (int i = 0; (i < index) && (avStart < mData.length); i++) {
            avStart += mData[avStart] + 1;
        }
        if (avStart < mData.length) {
            int avLen = mData[avStart];
            int aLen = 0;
            for (aLen = 0; aLen < avLen; aLen++) {
                if (mData[(avStart + aLen) + 1] == android.net.nsd.DnsSdTxtRecord.mSeperator) {
                    value = new byte[(avLen - aLen) - 1];
                    java.lang.System.arraycopy(mData, (avStart + aLen) + 2, value, 0, (avLen - aLen) - 1);
                    break;
                }
            }
        }
        return value;
    }

    private java.lang.String getValueAsString(int index) {
        byte[] value = this.getValue(index);
        return value != null ? new java.lang.String(value) : null;
    }

    private byte[] getValue(java.lang.String forKey) {
        java.lang.String s = null;
        int i;
        for (i = 0; null != (s = this.getKey(i)); i++) {
            if (0 == forKey.compareToIgnoreCase(s)) {
                return this.getValue(i);
            }
        }
        return null;
    }

    /**
     * Return a string representation.
     * Example : {key1=value1},{key2=value2}..
     *
     * For a key say like "key3" with null value
     * {key1=value1},{key2=value2}{key3}
     */
    public java.lang.String toString() {
        java.lang.String a;
        java.lang.String result = null;
        for (int i = 0; null != (a = this.getKey(i)); i++) {
            java.lang.String av = "{" + a;
            java.lang.String val = this.getValueAsString(i);
            if (val != null)
                av += ("=" + val) + "}";
            else
                av += "}";

            if (result == null)
                result = av;
            else
                result = (result + ", ") + av;

        }
        return result != null ? result : "";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof android.net.nsd.DnsSdTxtRecord)) {
            return false;
        }
        android.net.nsd.DnsSdTxtRecord record = ((android.net.nsd.DnsSdTxtRecord) (o));
        return java.util.Arrays.equals(record.mData, mData);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Arrays.hashCode(mData);
    }

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByteArray(mData);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.nsd.DnsSdTxtRecord> CREATOR = new android.os.Parcelable.Creator<android.net.nsd.DnsSdTxtRecord>() {
        public android.net.nsd.DnsSdTxtRecord createFromParcel(android.os.Parcel in) {
            android.net.nsd.DnsSdTxtRecord info = new android.net.nsd.DnsSdTxtRecord();
            in.readByteArray(info.mData);
            return info;
        }

        public android.net.nsd.DnsSdTxtRecord[] newArray(int size) {
            return new android.net.nsd.DnsSdTxtRecord[size];
        }
    };
}

