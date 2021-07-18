/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.net.nsd;


/**
 * A class representing service information for network service discovery
 * {@see NsdManager}
 */
public final class NsdServiceInfo implements android.os.Parcelable {
    private static final java.lang.String TAG = "NsdServiceInfo";

    private java.lang.String mServiceName;

    private java.lang.String mServiceType;

    private final android.util.ArrayMap<java.lang.String, byte[]> mTxtRecord = new android.util.ArrayMap<java.lang.String, byte[]>();

    private java.net.InetAddress mHost;

    private int mPort;

    public NsdServiceInfo() {
    }

    /**
     *
     *
     * @unknown 
     */
    public NsdServiceInfo(java.lang.String sn, java.lang.String rt) {
        mServiceName = sn;
        mServiceType = rt;
    }

    /**
     * Get the service name
     */
    public java.lang.String getServiceName() {
        return mServiceName;
    }

    /**
     * Set the service name
     */
    public void setServiceName(java.lang.String s) {
        mServiceName = s;
    }

    /**
     * Get the service type
     */
    public java.lang.String getServiceType() {
        return mServiceType;
    }

    /**
     * Set the service type
     */
    public void setServiceType(java.lang.String s) {
        mServiceType = s;
    }

    /**
     * Get the host address. The host address is valid for a resolved service.
     */
    public java.net.InetAddress getHost() {
        return mHost;
    }

    /**
     * Set the host address
     */
    public void setHost(java.net.InetAddress s) {
        mHost = s;
    }

    /**
     * Get port number. The port number is valid for a resolved service.
     */
    public int getPort() {
        return mPort;
    }

    /**
     * Set port number
     */
    public void setPort(int p) {
        mPort = p;
    }

    /**
     * Unpack txt information from a base-64 encoded byte array.
     *
     * @param rawRecords
     * 		The raw base64 encoded records string read from netd.
     * @unknown 
     */
    public void setTxtRecords(@android.annotation.NonNull
    java.lang.String rawRecords) {
        byte[] txtRecordsRawBytes = android.util.Base64.decode(rawRecords, android.util.Base64.DEFAULT);
        // There can be multiple TXT records after each other. Each record has to following format:
        // 
        // byte                  type                  required   meaning
        // -------------------   -------------------   --------   ----------------------------------
        // 0                     unsigned 8 bit        yes        size of record excluding this byte
        // 1 - n                 ASCII but not '='     yes        key
        // n + 1                 '='                   optional   separator of key and value
        // n + 2 - record size   uninterpreted bytes   optional   value
        // 
        // Example legal records:
        // [11, 'm', 'y', 'k', 'e', 'y', '=', 0x0, 0x4, 0x65, 0x7, 0xff]
        // [17, 'm', 'y', 'K', 'e', 'y', 'W', 'i', 't', 'h', 'N', 'o', 'V', 'a', 'l', 'u', 'e', '=']
        // [12, 'm', 'y', 'B', 'o', 'o', 'l', 'e', 'a', 'n', 'K', 'e', 'y']
        // 
        // Example corrupted records
        // [3, =, 1, 2]    <- key is empty
        // [3, 0, =, 2]    <- key contains non-ASCII character. We handle this by replacing the
        // invalid characters instead of skipping the record.
        // [30, 'a', =, 2] <- length exceeds total left over bytes in the TXT records array, we
        // handle this by reducing the length of the record as needed.
        int pos = 0;
        while (pos < txtRecordsRawBytes.length) {
            // recordLen is an unsigned 8 bit value
            int recordLen = txtRecordsRawBytes[pos] & 0xff;
            pos += 1;
            try {
                if (recordLen == 0) {
                    throw new java.lang.IllegalArgumentException("Zero sized txt record");
                } else
                    if ((pos + recordLen) > txtRecordsRawBytes.length) {
                        android.util.Log.w(android.net.nsd.NsdServiceInfo.TAG, (("Corrupt record length (pos = " + pos) + "): ") + recordLen);
                        recordLen = txtRecordsRawBytes.length - pos;
                    }

                // Decode key-value records
                java.lang.String key = null;
                byte[] value = null;
                int valueLen = 0;
                for (int i = pos; i < (pos + recordLen); i++) {
                    if (key == null) {
                        if (txtRecordsRawBytes[i] == '=') {
                            key = new java.lang.String(txtRecordsRawBytes, pos, i - pos, java.nio.charset.StandardCharsets.US_ASCII);
                        }
                    } else {
                        if (value == null) {
                            value = new byte[(recordLen - key.length()) - 1];
                        }
                        value[valueLen] = txtRecordsRawBytes[i];
                        valueLen++;
                    }
                }
                // If '=' was not found we have a boolean record
                if (key == null) {
                    key = new java.lang.String(txtRecordsRawBytes, pos, recordLen, java.nio.charset.StandardCharsets.US_ASCII);
                }
                if (android.text.TextUtils.isEmpty(key)) {
                    // Empty keys are not allowed (RFC6763 6.4)
                    throw new java.lang.IllegalArgumentException("Invalid txt record (key is empty)");
                }
                if (getAttributes().containsKey(key)) {
                    // When we have a duplicate record, the later ones are ignored (RFC6763 6.4)
                    throw new java.lang.IllegalArgumentException(("Invalid txt record (duplicate key \"" + key) + "\")");
                }
                setAttribute(key, value);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(android.net.nsd.NsdServiceInfo.TAG, (("While parsing txt records (pos = " + pos) + "): ") + e.getMessage());
            }
            pos += recordLen;
        } 
    }

    /**
     *
     *
     * @unknown 
     */
    public void setAttribute(java.lang.String key, byte[] value) {
        if (android.text.TextUtils.isEmpty(key)) {
            throw new java.lang.IllegalArgumentException("Key cannot be empty");
        }
        // Key must be printable US-ASCII, excluding =.
        for (int i = 0; i < key.length(); ++i) {
            char character = key.charAt(i);
            if ((character < 0x20) || (character > 0x7e)) {
                throw new java.lang.IllegalArgumentException("Key strings must be printable US-ASCII");
            } else
                if (character == 0x3d) {
                    throw new java.lang.IllegalArgumentException("Key strings must not include '='");
                }

        }
        // Key length + value length must be < 255.
        if ((key.length() + (value == null ? 0 : value.length)) >= 255) {
            throw new java.lang.IllegalArgumentException("Key length + value length must be < 255 bytes");
        }
        // Warn if key is > 9 characters, as recommended by RFC 6763 section 6.4.
        if (key.length() > 9) {
            android.util.Log.w(android.net.nsd.NsdServiceInfo.TAG, "Key lengths > 9 are discouraged: " + key);
        }
        // Check against total TXT record size limits.
        // Arbitrary 400 / 1300 byte limits taken from RFC 6763 section 6.2.
        int txtRecordSize = getTxtRecordSize();
        int futureSize = ((txtRecordSize + key.length()) + (value == null ? 0 : value.length)) + 2;
        if (futureSize > 1300) {
            throw new java.lang.IllegalArgumentException("Total length of attributes must be < 1300 bytes");
        } else
            if (futureSize > 400) {
                android.util.Log.w(android.net.nsd.NsdServiceInfo.TAG, "Total length of all attributes exceeds 400 bytes; truncation may occur");
            }

        mTxtRecord.put(key, value);
    }

    /**
     * Add a service attribute as a key/value pair.
     *
     * <p> Service attributes are included as DNS-SD TXT record pairs.
     *
     * <p> The key must be US-ASCII printable characters, excluding the '=' character.  Values may
     * be UTF-8 strings or null.  The total length of key + value must be less than 255 bytes.
     *
     * <p> Keys should be short, ideally no more than 9 characters, and unique per instance of
     * {@link NsdServiceInfo}.  Calling {@link #setAttribute} twice with the same key will overwrite
     * first value.
     */
    public void setAttribute(java.lang.String key, java.lang.String value) {
        try {
            setAttribute(key, value == null ? ((byte[]) (null)) : value.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new java.lang.IllegalArgumentException("Value must be UTF-8");
        }
    }

    /**
     * Remove an attribute by key
     */
    public void removeAttribute(java.lang.String key) {
        mTxtRecord.remove(key);
    }

    /**
     * Retrive attributes as a map of String keys to byte[] values.
     *
     * <p> The returned map is unmodifiable; changes must be made through {@link #setAttribute} and
     * {@link #removeAttribute}.
     */
    public java.util.Map<java.lang.String, byte[]> getAttributes() {
        return java.util.Collections.unmodifiableMap(mTxtRecord);
    }

    private int getTxtRecordSize() {
        int txtRecordSize = 0;
        for (java.util.Map.Entry<java.lang.String, byte[]> entry : mTxtRecord.entrySet()) {
            txtRecordSize += 2;// One for the length byte, one for the = between key and value.

            txtRecordSize += entry.getKey().length();
            byte[] value = entry.getValue();
            txtRecordSize += (value == null) ? 0 : value.length;
        }
        return txtRecordSize;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public byte[] getTxtRecord() {
        int txtRecordSize = getTxtRecordSize();
        if (txtRecordSize == 0) {
            return new byte[]{  };
        }
        byte[] txtRecord = new byte[txtRecordSize];
        int ptr = 0;
        for (java.util.Map.Entry<java.lang.String, byte[]> entry : mTxtRecord.entrySet()) {
            java.lang.String key = entry.getKey();
            byte[] value = entry.getValue();
            // One byte to record the length of this key/value pair.
            txtRecord[ptr++] = ((byte) ((key.length() + (value == null ? 0 : value.length)) + 1));
            // The key, in US-ASCII.
            // Note: use the StandardCharsets const here because it doesn't raise exceptions and we
            // already know the key is ASCII at this point.
            java.lang.System.arraycopy(key.getBytes(java.nio.charset.StandardCharsets.US_ASCII), 0, txtRecord, ptr, key.length());
            ptr += key.length();
            // US-ASCII '=' character.
            txtRecord[ptr++] = ((byte) ('='));
            // The value, as any raw bytes.
            if (value != null) {
                java.lang.System.arraycopy(value, 0, txtRecord, ptr, value.length);
                ptr += value.length;
            }
        }
        return txtRecord;
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("name: ").append(mServiceName).append(", type: ").append(mServiceType).append(", host: ").append(mHost).append(", port: ").append(mPort);
        byte[] txtRecord = getTxtRecord();
        if (txtRecord != null) {
            sb.append(", txtRecord: ").append(new java.lang.String(txtRecord, java.nio.charset.StandardCharsets.UTF_8));
        }
        return sb.toString();
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
        dest.writeString(mServiceName);
        dest.writeString(mServiceType);
        if (mHost != null) {
            dest.writeInt(1);
            dest.writeByteArray(mHost.getAddress());
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mPort);
        // TXT record key/value pairs.
        dest.writeInt(mTxtRecord.size());
        for (java.lang.String key : mTxtRecord.keySet()) {
            byte[] value = mTxtRecord.get(key);
            if (value != null) {
                dest.writeInt(1);
                dest.writeInt(value.length);
                dest.writeByteArray(value);
            } else {
                dest.writeInt(0);
            }
            dest.writeString(key);
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.nsd.NsdServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.net.nsd.NsdServiceInfo>() {
        public android.net.nsd.NsdServiceInfo createFromParcel(android.os.Parcel in) {
            android.net.nsd.NsdServiceInfo info = new android.net.nsd.NsdServiceInfo();
            info.mServiceName = in.readString();
            info.mServiceType = in.readString();
            if (in.readInt() == 1) {
                try {
                    info.mHost = java.net.InetAddress.getByAddress(in.createByteArray());
                } catch (java.net.UnknownHostException e) {
                }
            }
            info.mPort = in.readInt();
            // TXT record key/value pairs.
            int recordCount = in.readInt();
            for (int i = 0; i < recordCount; ++i) {
                byte[] valueArray = null;
                if (in.readInt() == 1) {
                    int valueLength = in.readInt();
                    valueArray = new byte[valueLength];
                    in.readByteArray(valueArray);
                }
                info.mTxtRecord.put(in.readString(), valueArray);
            }
            return info;
        }

        public android.net.nsd.NsdServiceInfo[] newArray(int size) {
            return new android.net.nsd.NsdServiceInfo[size];
        }
    };
}

