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
package android.hardware.hdmi;


/**
 * Container for record source used for one touch record.
 * Use one of helper method by source type.
 * <ul>
 * <li>Own source: {@link #ofOwnSource()}
 * <li>Digital service(channel id): {@link #ofDigitalChannelId(int, DigitalChannelData)}
 * <li>Digital service(ARIB): {@link #ofArib(int, AribData)}
 * <li>Digital service(ATSC): {@link #ofAtsc(int, AtscData)}
 * <li>Digital service(DVB): {@link #ofDvb(int, DvbData)}
 * <li>Analogue: {@link #ofAnalogue(int, int, int)}
 * <li>External plug: {@link #ofExternalPlug(int)}
 * <li>External physical address: {@link #ofExternalPhysicalAddress(int)}.
 * <ul>
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class HdmiRecordSources {
    private static final java.lang.String TAG = "HdmiRecordSources";

    /**
     * Record source type for "Own Source".
     */
    private static final int RECORD_SOURCE_TYPE_OWN_SOURCE = 1;

    /**
     * Record source type for "Digital Service".
     */
    private static final int RECORD_SOURCE_TYPE_DIGITAL_SERVICE = 2;

    /**
     * Record source type for "Analogue Service".
     */
    private static final int RECORD_SOURCE_TYPE_ANALOGUE_SERVICE = 3;

    /**
     * Record source type for "Exteranl Plug".
     */
    private static final int RECORD_SOURCE_TYPE_EXTERNAL_PLUG = 4;

    /**
     * Record source type for "External Physical Address".
     */
    private static final int RECORD_SOURCE_TYPE_EXTERNAL_PHYSICAL_ADDRESS = 5;

    private HdmiRecordSources() {
    }

    /**
     * Base class for each record source.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static abstract class RecordSource {
        /* package */
        final int mSourceType;

        /* package */
        final int mExtraDataSize;

        /* package */
        RecordSource(int sourceType, int extraDataSize) {
            mSourceType = sourceType;
            mExtraDataSize = extraDataSize;
        }

        /* package */
        abstract int extraParamToByteArray(byte[] data, int index);

        /* package */
        final int getDataSize(boolean includeType) {
            return includeType ? mExtraDataSize + 1 : mExtraDataSize;
        }

        /* package */
        final int toByteArray(boolean includeType, byte[] data, int index) {
            if (includeType) {
                // 1 to 8 bytes (depends on source).
                // {[Record Source Type]} |
                // {[Record Source Type] [Digital Service Identification]} |
                // {[Record Source Type] [Analogue Broadcast Type] [Analogue Frequency]
                // [Broadcast System]} |
                // {[Record Source Type] [External Plug]} |
                // {[Record Source Type] [External Physical Address]}
                // The first byte is used for record source type.
                data[index++] = ((byte) (mSourceType));
            }
            extraParamToByteArray(data, index);
            return getDataSize(includeType);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ---- Own source -----------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Creates {@link OwnSource} of own source.
     */
    public static android.hardware.hdmi.HdmiRecordSources.OwnSource ofOwnSource() {
        return new android.hardware.hdmi.HdmiRecordSources.OwnSource();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class OwnSource extends android.hardware.hdmi.HdmiRecordSources.RecordSource {
        private static final int EXTRA_DATA_SIZE = 0;

        private OwnSource() {
            super(android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_OWN_SOURCE, android.hardware.hdmi.HdmiRecordSources.OwnSource.EXTRA_DATA_SIZE);
        }

        @java.lang.Override
        int extraParamToByteArray(byte[] data, int index) {
            return 0;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ---- Digital service data -------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Digital broadcast general types
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ARIB = 0x0;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ATSC = 0x1;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_DVB = 0x2;

    /**
     * Digital broadcast specific types
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ARIB_BS = 0x8;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ARIB_CS = 0x9;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ARIB_T = 0xa;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ATSC_CABLE = 0x10;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ATSC_SATELLITE = 0x11;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_ATSC_TERRESTRIAL = 0x12;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_DVB_C = 0x18;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_DVB_S = 0x19;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_DVB_S2 = 0x1a;

    /**
     *
     *
     * @unknown 
     */
    public static final int DIGITAL_BROADCAST_TYPE_DVB_T = 0x1b;

    /**
     * Channel number formats.
     */
    private static final int CHANNEL_NUMBER_FORMAT_1_PART = 0x1;

    private static final int CHANNEL_NUMBER_FORMAT_2_PART = 0x2;

    /**
     * Interface for digital source identification.
     */
    private interface DigitalServiceIdentification {
        int toByteArray(byte[] data, int index);
    }

    /**
     * Digital service identification for ARIB.
     * <p>
     * It consists of the following fields
     * <ul>
     * <li>transport stream id: 2bytes
     * <li>service id: 2bytes
     * <li>original network id: 2bytes
     * </ul>
     *
     * @unknown 
     */
    public static final class AribData implements android.hardware.hdmi.HdmiRecordSources.DigitalServiceIdentification {
        /**
         * The transport_stream_ID of the transport stream carrying the required service
         */
        private final int mTransportStreamId;

        /**
         * The service_ID of the required service
         */
        private final int mServiceId;

        /**
         * The original_network_ID of the network carrying the transport stream for the required
         * service
         */
        private final int mOriginalNetworkId;

        public AribData(int transportStreamId, int serviceId, int originalNetworkId) {
            mTransportStreamId = transportStreamId;
            mServiceId = serviceId;
            mOriginalNetworkId = originalNetworkId;
        }

        @java.lang.Override
        public int toByteArray(byte[] data, int index) {
            return android.hardware.hdmi.HdmiRecordSources.threeFieldsToSixBytes(mTransportStreamId, mServiceId, mOriginalNetworkId, data, index);
        }
    }

    /**
     * Digital service identification for ATSC.
     * <p>
     * It consists of the following fields
     * <ul>
     * <li>transport stream id: 2bytes
     * <li>program number: 2bytes
     * <li>reserved: 2bytes
     * </ul>
     *
     * @unknown 
     */
    public static final class AtscData implements android.hardware.hdmi.HdmiRecordSources.DigitalServiceIdentification {
        /**
         * The transport_stream_ID of the transport stream carrying the required service
         */
        private final int mTransportStreamId;

        /**
         * The Program_number of the required service
         */
        private final int mProgramNumber;

        public AtscData(int transportStreamId, int programNumber) {
            mTransportStreamId = transportStreamId;
            mProgramNumber = programNumber;
        }

        @java.lang.Override
        public int toByteArray(byte[] data, int index) {
            return android.hardware.hdmi.HdmiRecordSources.threeFieldsToSixBytes(mTransportStreamId, mProgramNumber, 0, data, index);
        }
    }

    /**
     * Digital service identification for DVB.
     * <p>
     * It consists of the following fields
     * <ul>
     * <li>transport stream id: 2bytes
     * <li>service id: 2bytes
     * <li>original network id: 2bytes
     * </ul>
     *
     * @unknown 
     */
    public static final class DvbData implements android.hardware.hdmi.HdmiRecordSources.DigitalServiceIdentification {
        /**
         * The transport_stream_ID of the transport stream carrying the required service
         */
        private final int mTransportStreamId;

        /**
         * The service_ID of the required service
         */
        private final int mServiceId;

        /**
         * The original_network_ID of the network carrying the transport stream for the required
         * service
         */
        private final int mOriginalNetworkId;

        public DvbData(int transportStreamId, int serviceId, int originalNetworkId) {
            mTransportStreamId = transportStreamId;
            mServiceId = serviceId;
            mOriginalNetworkId = originalNetworkId;
        }

        @java.lang.Override
        public int toByteArray(byte[] data, int index) {
            return android.hardware.hdmi.HdmiRecordSources.threeFieldsToSixBytes(mTransportStreamId, mServiceId, mOriginalNetworkId, data, index);
        }
    }

    /**
     * Identifies a 1-part Logical or Virtual Channel Number or a 2-part Major and Minor channel
     * combination.
     */
    private static final class ChannelIdentifier {
        /**
         * Identifies Channel Format
         */
        private final int mChannelNumberFormat;

        /**
         * Major Channel Number (if Channel Number Format is 2-part). If format is
         * CHANNEL_NUMBER_FORMAT_1_PART, this will be ignored(0).
         */
        private final int mMajorChannelNumber;

        /**
         * 1-part Channel Number, or a Minor Channel Number (if Channel Number Format is 2-part).
         */
        private final int mMinorChannelNumber;

        private ChannelIdentifier(int format, int majorNumber, int minorNumer) {
            mChannelNumberFormat = format;
            mMajorChannelNumber = majorNumber;
            mMinorChannelNumber = minorNumer;
        }

        private int toByteArray(byte[] data, int index) {
            // The first 6 bits for format, the 10 bits for major number.
            data[index] = ((byte) ((mChannelNumberFormat << 2) | ((mMajorChannelNumber >>> 8) & 0x3)));
            data[index + 1] = ((byte) (mMajorChannelNumber & 0xff));
            // Minor number uses the next 16 bits.
            android.hardware.hdmi.HdmiRecordSources.shortToByteArray(((short) (mMinorChannelNumber)), data, index + 2);
            return 4;
        }
    }

    /**
     * Digital channel id.
     * <p>
     * It consists of the following fields
     * <ul>
     * <li>channel number format: 6bits
     * <li>major number: 10bits
     * <li>minor number: 16bits
     * <li>reserved: 2bytes
     * </ul>
     *
     * @unknown 
     */
    public static final class DigitalChannelData implements android.hardware.hdmi.HdmiRecordSources.DigitalServiceIdentification {
        /**
         * Identifies the logical or virtual channel number of a service.
         */
        private final android.hardware.hdmi.HdmiRecordSources.ChannelIdentifier mChannelIdentifier;

        public static android.hardware.hdmi.HdmiRecordSources.DigitalChannelData ofTwoNumbers(int majorNumber, int minorNumber) {
            return new android.hardware.hdmi.HdmiRecordSources.DigitalChannelData(new android.hardware.hdmi.HdmiRecordSources.ChannelIdentifier(android.hardware.hdmi.HdmiRecordSources.CHANNEL_NUMBER_FORMAT_2_PART, majorNumber, minorNumber));
        }

        public static android.hardware.hdmi.HdmiRecordSources.DigitalChannelData ofOneNumber(int number) {
            return new android.hardware.hdmi.HdmiRecordSources.DigitalChannelData(new android.hardware.hdmi.HdmiRecordSources.ChannelIdentifier(android.hardware.hdmi.HdmiRecordSources.CHANNEL_NUMBER_FORMAT_1_PART, 0, number));
        }

        private DigitalChannelData(android.hardware.hdmi.HdmiRecordSources.ChannelIdentifier id) {
            mChannelIdentifier = id;
        }

        @java.lang.Override
        public int toByteArray(byte[] data, int index) {
            mChannelIdentifier.toByteArray(data, index);
            // The last 2 bytes is reserved for future use.
            data[index + 4] = 0;
            data[index + 5] = 0;
            return 6;
        }
    }

    /**
     * Creates {@link DigitalServiceSource} with channel type.
     *
     * @param broadcastSystem
     * 		digital broadcast system. It should be one of
     * 		<ul>
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB_BS}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB_CS}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB_T}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC_CABLE}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC_SATELLITE}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC_TERRESTRIAL}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_C}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_S}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_S2}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_T}
     * 		</ul>
     * @unknown 
     */
    public static android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource ofDigitalChannelId(int broadcastSystem, android.hardware.hdmi.HdmiRecordSources.DigitalChannelData data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("data should not be null.");
        }
        switch (broadcastSystem) {
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB_BS :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB_CS :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB_T :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC_CABLE :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC_SATELLITE :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC_TERRESTRIAL :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_C :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_S :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_S2 :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_T :
                return new android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource(android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.DIGITAL_SERVICE_IDENTIFIED_BY_CHANNEL, broadcastSystem, data);
            default :
                android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid broadcast type:" + broadcastSystem);
                throw new java.lang.IllegalArgumentException("Invalid broadcast system value:" + broadcastSystem);
        }
    }

    /**
     * Creates {@link DigitalServiceSource} of ARIB type.
     *
     * @param aribType
     * 		ARIB type. It should be one of
     * 		<ul>
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB_BS}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB_CS}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ARIB_T}
     * 		</ul>
     * @unknown 
     */
    @android.annotation.Nullable
    public static android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource ofArib(int aribType, android.hardware.hdmi.HdmiRecordSources.AribData data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("data should not be null.");
        }
        switch (aribType) {
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB_BS :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB_CS :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ARIB_T :
                return new android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource(android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.DIGITAL_SERVICE_IDENTIFIED_BY_DIGITAL_ID, aribType, data);
            default :
                android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid ARIB type:" + aribType);
                throw new java.lang.IllegalArgumentException("type should not be null.");
        }
    }

    /**
     * Creates {@link DigitalServiceSource} of ATSC type.
     *
     * @param atscType
     * 		ATSC type. It should be one of
     * 		<ul>
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC_CABLE}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC_SATELLITE}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_ATSC_TERRESTRIAL}
     * 		</ul>
     * @unknown 
     */
    @android.annotation.Nullable
    public static android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource ofAtsc(int atscType, android.hardware.hdmi.HdmiRecordSources.AtscData data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("data should not be null.");
        }
        switch (atscType) {
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC_CABLE :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC_SATELLITE :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_ATSC_TERRESTRIAL :
                return new android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource(android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.DIGITAL_SERVICE_IDENTIFIED_BY_DIGITAL_ID, atscType, data);
            default :
                android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid ATSC type:" + atscType);
                throw new java.lang.IllegalArgumentException("Invalid ATSC type:" + atscType);
        }
    }

    /**
     * Creates {@link DigitalServiceSource} of ATSC type.
     *
     * @param dvbType
     * 		DVB type. It should be one of
     * 		<ul>
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_C}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_S}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_S2}
     * 		<li>{@link #DIGITAL_BROADCAST_TYPE_DVB_T}
     * 		</ul>
     * @unknown 
     */
    @android.annotation.Nullable
    public static android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource ofDvb(int dvbType, android.hardware.hdmi.HdmiRecordSources.DvbData data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("data should not be null.");
        }
        switch (dvbType) {
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_C :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_S :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_S2 :
            case android.hardware.hdmi.HdmiRecordSources.DIGITAL_BROADCAST_TYPE_DVB_T :
                return new android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource(android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.DIGITAL_SERVICE_IDENTIFIED_BY_DIGITAL_ID, dvbType, data);
            default :
                android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid DVB type:" + dvbType);
                throw new java.lang.IllegalArgumentException("Invalid DVB type:" + dvbType);
        }
    }

    /**
     * Record source container for "Digital Service".
     * <ul>
     * <li>[Record Source Type] - 1 byte
     * <li>[Digital Identification] - 7 bytes
     * </ul>
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class DigitalServiceSource extends android.hardware.hdmi.HdmiRecordSources.RecordSource {
        /**
         * Indicates that a service is identified by digital service IDs.
         */
        private static final int DIGITAL_SERVICE_IDENTIFIED_BY_DIGITAL_ID = 0;

        /**
         * Indicates that a service is identified by a logical or virtual channel number.
         */
        private static final int DIGITAL_SERVICE_IDENTIFIED_BY_CHANNEL = 1;

        static final int EXTRA_DATA_SIZE = 7;

        /**
         * Type of identification. It should be one of DIGITAL_SERVICE_IDENTIFIED_BY_DIGITAL_ID and
         * DIGITAL_SERVICE_IDENTIFIED_BY_CHANNEL
         */
        private final int mIdentificationMethod;

        /**
         * Indicates the Digital Broadcast System of required service. This is present irrespective
         * of the state of [Service Identification Method].
         */
        private final int mBroadcastSystem;

        /**
         * Extra parameter for digital service identification.
         */
        private final android.hardware.hdmi.HdmiRecordSources.DigitalServiceIdentification mIdentification;

        private DigitalServiceSource(int identificatinoMethod, int broadcastSystem, android.hardware.hdmi.HdmiRecordSources.DigitalServiceIdentification identification) {
            super(android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_DIGITAL_SERVICE, android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.EXTRA_DATA_SIZE);
            mIdentificationMethod = identificatinoMethod;
            mBroadcastSystem = broadcastSystem;
            mIdentification = identification;
        }

        @java.lang.Override
        int extraParamToByteArray(byte[] data, int index) {
            data[index] = ((byte) ((mIdentificationMethod << 7) | (mBroadcastSystem & 0x7f)));
            mIdentification.toByteArray(data, index + 1);
            return android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.EXTRA_DATA_SIZE;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ---- Analogue service data ------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Analogue broadcast types.
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int ANALOGUE_BROADCAST_TYPE_CABLE = 0x0;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANALOGUE_BROADCAST_TYPE_SATELLITE = 0x1;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANALOGUE_BROADCAST_TYPE_TERRESTRIAL = 0x2;

    /**
     * Broadcast system values.
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_PAL_BG = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_SECAM_LP = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_PAL_M = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_NTSC_M = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_PAL_I = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_SECAM_DK = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_SECAM_BG = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_SECAM_L = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_PAL_DK = 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int BROADCAST_SYSTEM_PAL_OTHER_SYSTEM = 31;

    /**
     * Creates {@link AnalogueServiceSource} of analogue service.
     *
     * @param broadcastType
     * 		
     * @param frequency
     * 		
     * @param broadcastSystem
     * 		
     * @unknown 
     */
    @android.annotation.Nullable
    public static android.hardware.hdmi.HdmiRecordSources.AnalogueServiceSource ofAnalogue(int broadcastType, int frequency, int broadcastSystem) {
        if ((broadcastType < android.hardware.hdmi.HdmiRecordSources.ANALOGUE_BROADCAST_TYPE_CABLE) || (broadcastType > android.hardware.hdmi.HdmiRecordSources.ANALOGUE_BROADCAST_TYPE_TERRESTRIAL)) {
            android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid Broadcast type:" + broadcastType);
            throw new java.lang.IllegalArgumentException("Invalid Broadcast type:" + broadcastType);
        }
        if ((frequency < 0) || (frequency > 0xffff)) {
            android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid frequency value[0x0000-0xFFFF]:" + frequency);
            throw new java.lang.IllegalArgumentException("Invalid frequency value[0x0000-0xFFFF]:" + frequency);
        }
        if ((broadcastSystem < android.hardware.hdmi.HdmiRecordSources.BROADCAST_SYSTEM_PAL_BG) || (broadcastSystem > android.hardware.hdmi.HdmiRecordSources.BROADCAST_SYSTEM_PAL_OTHER_SYSTEM)) {
            android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid Broadcast system:" + broadcastSystem);
            throw new java.lang.IllegalArgumentException("Invalid Broadcast system:" + broadcastSystem);
        }
        return new android.hardware.hdmi.HdmiRecordSources.AnalogueServiceSource(broadcastType, frequency, broadcastSystem);
    }

    /**
     * Record source for analogue service data. It consists of
     * <ul>
     * <li>[Record Source Type] - 1 byte
     * <li>[Analogue Broadcast Type] - 1 byte
     * <li>[Analogue Frequency] - 2 bytes
     * <li>[Broadcast System] - 1 byte
     * </ul>
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class AnalogueServiceSource extends android.hardware.hdmi.HdmiRecordSources.RecordSource {
        /* package */
        static final int EXTRA_DATA_SIZE = 4;

        /**
         * Indicates the Analogue broadcast type.
         */
        private final int mBroadcastType;

        /**
         * Used to specify the frequency used by an analogue tuner. 0x0000<N<0xFFFF.
         */
        private final int mFrequency;

        /**
         * This specifies information about the color system, the sound carrier and the
         * IF-frequency.
         */
        private final int mBroadcastSystem;

        private AnalogueServiceSource(int broadcastType, int frequency, int broadcastSystem) {
            super(android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_ANALOGUE_SERVICE, android.hardware.hdmi.HdmiRecordSources.AnalogueServiceSource.EXTRA_DATA_SIZE);
            mBroadcastType = broadcastType;
            mFrequency = frequency;
            mBroadcastSystem = broadcastSystem;
        }

        /* package */
        @java.lang.Override
        int extraParamToByteArray(byte[] data, int index) {
            // [Analogue Broadcast Type] - 1 byte
            data[index] = ((byte) (mBroadcastType));
            // [Analogue Frequency] - 2 bytes
            android.hardware.hdmi.HdmiRecordSources.shortToByteArray(((short) (mFrequency)), data, index + 1);
            // [Broadcast System] - 1 byte
            data[index + 3] = ((byte) (mBroadcastSystem));
            return android.hardware.hdmi.HdmiRecordSources.AnalogueServiceSource.EXTRA_DATA_SIZE;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ---- External plug data ---------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Creates {@link ExternalPlugData} of external plug type.
     *
     * @param plugNumber
     * 		plug number. It should be in range of [1, 255]
     * @unknown 
     */
    public static android.hardware.hdmi.HdmiRecordSources.ExternalPlugData ofExternalPlug(int plugNumber) {
        if ((plugNumber < 1) || (plugNumber > 255)) {
            android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid plug number[1-255]" + plugNumber);
            throw new java.lang.IllegalArgumentException("Invalid plug number[1-255]" + plugNumber);
        }
        return new android.hardware.hdmi.HdmiRecordSources.ExternalPlugData(plugNumber);
    }

    /**
     * Record source for external plug (external non-HDMI device connect) type.
     * <ul>
     * <li>[Record Source Type] - 1 byte
     * <li>[External Plug] - 1 byte
     * </ul>
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class ExternalPlugData extends android.hardware.hdmi.HdmiRecordSources.RecordSource {
        static final int EXTRA_DATA_SIZE = 1;

        /**
         * External Plug number on the Recording Device.
         */
        private final int mPlugNumber;

        private ExternalPlugData(int plugNumber) {
            super(android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_EXTERNAL_PLUG, android.hardware.hdmi.HdmiRecordSources.ExternalPlugData.EXTRA_DATA_SIZE);
            mPlugNumber = plugNumber;
        }

        @java.lang.Override
        int extraParamToByteArray(byte[] data, int index) {
            data[index] = ((byte) (mPlugNumber));
            return android.hardware.hdmi.HdmiRecordSources.ExternalPlugData.EXTRA_DATA_SIZE;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ---- External physical address --------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * Creates {@link ExternalPhysicalAddress} of external physical address.
     *
     * @param physicalAddress
     * 		
     * @unknown 
     */
    public static android.hardware.hdmi.HdmiRecordSources.ExternalPhysicalAddress ofExternalPhysicalAddress(int physicalAddress) {
        if ((physicalAddress & (~0xffff)) != 0) {
            android.util.Log.w(android.hardware.hdmi.HdmiRecordSources.TAG, "Invalid physical address:" + physicalAddress);
            throw new java.lang.IllegalArgumentException("Invalid physical address:" + physicalAddress);
        }
        return new android.hardware.hdmi.HdmiRecordSources.ExternalPhysicalAddress(physicalAddress);
    }

    /**
     * Record source for external physical address.
     * <ul>
     * <li>[Record Source Type] - 1 byte
     * <li>[Physical address] - 2 byte
     * </ul>
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class ExternalPhysicalAddress extends android.hardware.hdmi.HdmiRecordSources.RecordSource {
        static final int EXTRA_DATA_SIZE = 2;

        private final int mPhysicalAddress;

        private ExternalPhysicalAddress(int physicalAddress) {
            super(android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_EXTERNAL_PHYSICAL_ADDRESS, android.hardware.hdmi.HdmiRecordSources.ExternalPhysicalAddress.EXTRA_DATA_SIZE);
            mPhysicalAddress = physicalAddress;
        }

        @java.lang.Override
        int extraParamToByteArray(byte[] data, int index) {
            android.hardware.hdmi.HdmiRecordSources.shortToByteArray(((short) (mPhysicalAddress)), data, index);
            return android.hardware.hdmi.HdmiRecordSources.ExternalPhysicalAddress.EXTRA_DATA_SIZE;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // ------- Helper methods ----------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    private static int threeFieldsToSixBytes(int first, int second, int third, byte[] data, int index) {
        android.hardware.hdmi.HdmiRecordSources.shortToByteArray(((short) (first)), data, index);
        android.hardware.hdmi.HdmiRecordSources.shortToByteArray(((short) (second)), data, index + 2);
        android.hardware.hdmi.HdmiRecordSources.shortToByteArray(((short) (third)), data, index + 4);
        return 6;
    }

    private static int shortToByteArray(short value, byte[] byteArray, int index) {
        byteArray[index] = ((byte) ((value >>> 8) & 0xff));
        byteArray[index + 1] = ((byte) (value & 0xff));
        return 2;
    }

    /**
     * Checks the byte array of record source.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static boolean checkRecordSource(byte[] recordSource) {
        if ((recordSource == null) || (recordSource.length == 0))
            return false;

        int recordSourceType = recordSource[0];
        int extraDataSize = recordSource.length - 1;
        switch (recordSourceType) {
            case android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_OWN_SOURCE :
                return extraDataSize == android.hardware.hdmi.HdmiRecordSources.OwnSource.EXTRA_DATA_SIZE;
            case android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_DIGITAL_SERVICE :
                return extraDataSize == android.hardware.hdmi.HdmiRecordSources.DigitalServiceSource.EXTRA_DATA_SIZE;
            case android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_ANALOGUE_SERVICE :
                return extraDataSize == android.hardware.hdmi.HdmiRecordSources.AnalogueServiceSource.EXTRA_DATA_SIZE;
            case android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_EXTERNAL_PLUG :
                return extraDataSize == android.hardware.hdmi.HdmiRecordSources.ExternalPlugData.EXTRA_DATA_SIZE;
            case android.hardware.hdmi.HdmiRecordSources.RECORD_SOURCE_TYPE_EXTERNAL_PHYSICAL_ADDRESS :
                return extraDataSize == android.hardware.hdmi.HdmiRecordSources.ExternalPhysicalAddress.EXTRA_DATA_SIZE;
            default :
                return false;
        }
    }
}

