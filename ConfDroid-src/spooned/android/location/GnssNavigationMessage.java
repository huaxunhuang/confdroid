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
 * limitations under the License
 */
package android.location;


/**
 * A class containing a GNSS satellite Navigation Message.
 */
public final class GnssNavigationMessage implements android.os.Parcelable {
    private static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * The type of the GNSS Navigation Message
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.location.GnssNavigationMessage.TYPE_UNKNOWN, android.location.GnssNavigationMessage.TYPE_GPS_L1CA, android.location.GnssNavigationMessage.TYPE_GPS_L2CNAV, android.location.GnssNavigationMessage.TYPE_GPS_L5CNAV, android.location.GnssNavigationMessage.TYPE_GPS_CNAV2, android.location.GnssNavigationMessage.TYPE_GLO_L1CA, android.location.GnssNavigationMessage.TYPE_BDS_D1, android.location.GnssNavigationMessage.TYPE_BDS_D2, android.location.GnssNavigationMessage.TYPE_GAL_I, android.location.GnssNavigationMessage.TYPE_GAL_F })
    public @interface GnssNavigationMessageType {}

    // The following enumerations must be in sync with the values declared in gps.h
    /**
     * Message type unknown
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * GPS L1 C/A message contained in the structure.
     */
    public static final int TYPE_GPS_L1CA = 0x101;

    /**
     * GPS L2-CNAV message contained in the structure.
     */
    public static final int TYPE_GPS_L2CNAV = 0x102;

    /**
     * GPS L5-CNAV message contained in the structure.
     */
    public static final int TYPE_GPS_L5CNAV = 0x103;

    /**
     * GPS CNAV-2 message contained in the structure.
     */
    public static final int TYPE_GPS_CNAV2 = 0x104;

    /**
     * Glonass L1 CA message contained in the structure.
     */
    public static final int TYPE_GLO_L1CA = 0x301;

    /**
     * Beidou D1 message contained in the structure.
     */
    public static final int TYPE_BDS_D1 = 0x501;

    /**
     * Beidou D2 message contained in the structure.
     */
    public static final int TYPE_BDS_D2 = 0x502;

    /**
     * Galileo I/NAV message contained in the structure.
     */
    public static final int TYPE_GAL_I = 0x601;

    /**
     * Galileo F/NAV message contained in the structure.
     */
    public static final int TYPE_GAL_F = 0x602;

    /**
     * The Navigation Message Status is 'unknown'.
     */
    public static final int STATUS_UNKNOWN = 0;

    /**
     * The Navigation Message was received without any parity error in its navigation words.
     */
    public static final int STATUS_PARITY_PASSED = 1 << 0;

    /**
     * The Navigation Message was received with words that failed parity check, but the receiver was
     * able to correct those words.
     */
    public static final int STATUS_PARITY_REBUILT = 1 << 1;

    /**
     * Used for receiving GNSS satellite Navigation Messages from the GNSS engine.
     *
     * <p>You can implement this interface and call
     * {@link LocationManager#registerGnssNavigationMessageCallback}.
     */
    public static abstract class Callback {
        /**
         * The status of GNSS measurements event.
         *
         * @unknown 
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.location.GnssNavigationMessage.Callback.STATUS_NOT_SUPPORTED, android.location.GnssNavigationMessage.Callback.STATUS_READY, android.location.GnssNavigationMessage.Callback.STATUS_LOCATION_DISABLED })
        public @interface GnssNavigationMessageStatus {}

        /**
         * The system does not support tracking of GNSS Navigation Messages.
         *
         * This status will not change in the future.
         */
        public static final int STATUS_NOT_SUPPORTED = 0;

        /**
         * GNSS Navigation Messages are successfully being tracked, it will receive updates once
         * they are available.
         */
        public static final int STATUS_READY = 1;

        /**
         * GNSS provider or Location is disabled, updated will not be received until they are
         * enabled.
         */
        public static final int STATUS_LOCATION_DISABLED = 2;

        /**
         * Returns the latest collected GNSS Navigation Message.
         */
        public void onGnssNavigationMessageReceived(android.location.GnssNavigationMessage event) {
        }

        /**
         * Returns the latest status of the GNSS Navigation Messages sub-system.
         */
        public void onStatusChanged(@android.location.GnssNavigationMessage.Callback.GnssNavigationMessageStatus
        int status) {
        }
    }

    // End enumerations in sync with gps.h
    private int mType;

    private int mSvid;

    private int mMessageId;

    private int mSubmessageId;

    private byte[] mData;

    private int mStatus;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public GnssNavigationMessage() {
        initialize();
    }

    /**
     * Sets all contents to the values stored in the provided object.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void set(android.location.GnssNavigationMessage navigationMessage) {
        mType = navigationMessage.mType;
        mSvid = navigationMessage.mSvid;
        mMessageId = navigationMessage.mMessageId;
        mSubmessageId = navigationMessage.mSubmessageId;
        mData = navigationMessage.mData;
        mStatus = navigationMessage.mStatus;
    }

    /**
     * Resets all the contents to its original state.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void reset() {
        initialize();
    }

    /**
     * Gets the type of the navigation message contained in the object.
     */
    @android.location.GnssNavigationMessage.GnssNavigationMessageType
    public int getType() {
        return mType;
    }

    /**
     * Sets the type of the navigation message.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setType(@android.location.GnssNavigationMessage.GnssNavigationMessageType
    int value) {
        mType = value;
    }

    /**
     * Gets a string representation of the 'type'.
     * For internal and logging use only.
     */
    private java.lang.String getTypeString() {
        switch (mType) {
            case android.location.GnssNavigationMessage.TYPE_UNKNOWN :
                return "Unknown";
            case android.location.GnssNavigationMessage.TYPE_GPS_L1CA :
                return "GPS L1 C/A";
            case android.location.GnssNavigationMessage.TYPE_GPS_L2CNAV :
                return "GPS L2-CNAV";
            case android.location.GnssNavigationMessage.TYPE_GPS_L5CNAV :
                return "GPS L5-CNAV";
            case android.location.GnssNavigationMessage.TYPE_GPS_CNAV2 :
                return "GPS CNAV2";
            case android.location.GnssNavigationMessage.TYPE_GLO_L1CA :
                return "Glonass L1 C/A";
            case android.location.GnssNavigationMessage.TYPE_BDS_D1 :
                return "Beidou D1";
            case android.location.GnssNavigationMessage.TYPE_BDS_D2 :
                return "Beidou D2";
            case android.location.GnssNavigationMessage.TYPE_GAL_I :
                return "Galileo I";
            case android.location.GnssNavigationMessage.TYPE_GAL_F :
                return "Galileo F";
            default :
                return ("<Invalid:" + mType) + ">";
        }
    }

    /**
     * Gets the satellite ID.
     *
     * <p>Range varies by constellation.  See definition at {@code GnssStatus#getSvid(int)}
     */
    public int getSvid() {
        return mSvid;
    }

    /**
     * Sets the satellite ID.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setSvid(int value) {
        mSvid = value;
    }

    /**
     * Gets the Message identifier.
     *
     * <p>This provides an index to help with complete Navigation Message assembly. Similar
     * identifiers within the data bits themselves often supplement this information, in ways even
     * more specific to each message type; see the relevant satellite constellation ICDs for
     * details.
     *
     * <ul>
     * <li> For GPS L1 C/A subframe 4 and 5, this value corresponds to the 'frame id' of the
     * navigation message, in the range of 1-25 (Subframe 1, 2, 3 does not contain a 'frame id' and
     * this value can be set to -1.)</li>
     * <li> For Glonass L1 C/A, this refers to the frame ID, in the range of 1-5.</li>
     * <li> For BeiDou D1, this refers to the frame number in the range of 1-24</li>
     * <li> For Beidou D2, this refers to the frame number, in the range of 1-120</li>
     * <li> For Galileo F/NAV nominal frame structure, this refers to the subframe number, in the
     * range of 1-12</li>
     * <li> For Galileo I/NAV nominal frame structure, this refers to the subframe number in the
     * range of 1-24</li>
     * </ul>
     */
    public int getMessageId() {
        return mMessageId;
    }

    /**
     * Sets the Message Identifier.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setMessageId(int value) {
        mMessageId = value;
    }

    /**
     * Gets the sub-message identifier, relevant to the {@link #getType()} of the message.
     *
     * <ul>
     * <li> For GPS L1 C/A, BeiDou D1 &amp; BeiDou D2, the submessage id corresponds to the subframe
     * number of the navigation message, in the range of 1-5.</li>
     * <li>For Glonass L1 C/A, this refers to the String number, in the range from 1-15</li>
     * <li>For Galileo F/NAV, this refers to the page type in the range 1-6</li>
     * <li>For Galileo I/NAV, this refers to the word type in the range 1-10+</li>
     * <li>For Galileo in particular, the type information embedded within the data bits may be even
     * more useful in interpretation, than the nominal page and word types provided in this
     * field.</li>
     * </ul>
     */
    public int getSubmessageId() {
        return mSubmessageId;
    }

    /**
     * Sets the Sub-message identifier.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setSubmessageId(int value) {
        mSubmessageId = value;
    }

    /**
     * Gets the data of the reported GPS message.
     *
     * <p>The bytes (or words) specified using big endian format (MSB first).
     *
     * <ul>
     * <li>For GPS L1 C/A, Beidou D1 &amp; Beidou D2, each subframe contains 10 30-bit words. Each
     * word (30 bits) should be fit into the last 30 bits in a 4-byte word (skip B31 and B32), with
     * MSB first, for a total of 40 bytes, covering a time period of 6, 6, and 0.6 seconds,
     * respectively.</li>
     * <li>For Glonass L1 C/A, each string contains 85 data bits, including the checksum.  These
     * bits should be fit into 11 bytes, with MSB first (skip B86-B88), covering a time period of 2
     * seconds.</li>
     * <li>For Galileo F/NAV, each word consists of 238-bit (sync &amp; tail symbols excluded). Each
     * word should be fit into 30-bytes, with MSB first (skip B239, B240), covering a time period of
     * 10 seconds.</li>
     * <li>For Galileo I/NAV, each page contains 2 page parts, even and odd, with a total of 2x114 =
     * 228 bits, (sync &amp; tail excluded) that should be fit into 29 bytes, with MSB first (skip
     * B229-B232).</li>
     * </ul>
     */
    @android.annotation.NonNull
    public byte[] getData() {
        return mData;
    }

    /**
     * Sets the data associated with the Navigation Message.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setData(byte[] value) {
        if (value == null) {
            throw new java.security.InvalidParameterException("Data must be a non-null array");
        }
        mData = value;
    }

    /**
     * Gets the Status of the navigation message contained in the object.
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * Sets the status of the navigation message.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setStatus(int value) {
        mStatus = value;
    }

    /**
     * Gets a string representation of the 'status'.
     * For internal and logging use only.
     */
    private java.lang.String getStatusString() {
        switch (mStatus) {
            case android.location.GnssNavigationMessage.STATUS_UNKNOWN :
                return "Unknown";
            case android.location.GnssNavigationMessage.STATUS_PARITY_PASSED :
                return "ParityPassed";
            case android.location.GnssNavigationMessage.STATUS_PARITY_REBUILT :
                return "ParityRebuilt";
            default :
                return ("<Invalid:" + mStatus) + ">";
        }
    }

    public static final android.os.Parcelable.Creator<android.location.GnssNavigationMessage> CREATOR = new android.os.Parcelable.Creator<android.location.GnssNavigationMessage>() {
        @java.lang.Override
        public android.location.GnssNavigationMessage createFromParcel(android.os.Parcel parcel) {
            android.location.GnssNavigationMessage navigationMessage = new android.location.GnssNavigationMessage();
            navigationMessage.setType(parcel.readInt());
            navigationMessage.setSvid(parcel.readInt());
            navigationMessage.setMessageId(parcel.readInt());
            navigationMessage.setSubmessageId(parcel.readInt());
            int dataLength = parcel.readInt();
            byte[] data = new byte[dataLength];
            parcel.readByteArray(data);
            navigationMessage.setData(data);
            navigationMessage.setStatus(parcel.readInt());
            return navigationMessage;
        }

        @java.lang.Override
        public android.location.GnssNavigationMessage[] newArray(int size) {
            return new android.location.GnssNavigationMessage[size];
        }
    };

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mType);
        parcel.writeInt(mSvid);
        parcel.writeInt(mMessageId);
        parcel.writeInt(mSubmessageId);
        parcel.writeInt(mData.length);
        parcel.writeByteArray(mData);
        parcel.writeInt(mStatus);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.String format = "   %-15s = %s\n";
        java.lang.StringBuilder builder = new java.lang.StringBuilder("GnssNavigationMessage:\n");
        builder.append(java.lang.String.format(format, "Type", getTypeString()));
        builder.append(java.lang.String.format(format, "Svid", mSvid));
        builder.append(java.lang.String.format(format, "Status", getStatusString()));
        builder.append(java.lang.String.format(format, "MessageId", mMessageId));
        builder.append(java.lang.String.format(format, "SubmessageId", mSubmessageId));
        builder.append(java.lang.String.format(format, "Data", "{"));
        java.lang.String prefix = "        ";
        for (byte value : mData) {
            builder.append(prefix);
            builder.append(value);
            prefix = ", ";
        }
        builder.append(" }");
        return builder.toString();
    }

    private void initialize() {
        mType = android.location.GnssNavigationMessage.TYPE_UNKNOWN;
        mSvid = 0;
        mMessageId = -1;
        mSubmessageId = -1;
        mData = android.location.GnssNavigationMessage.EMPTY_ARRAY;
        mStatus = android.location.GnssNavigationMessage.STATUS_UNKNOWN;
    }
}

