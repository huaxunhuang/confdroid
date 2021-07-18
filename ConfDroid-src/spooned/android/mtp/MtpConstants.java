/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.mtp;


/**
 * A class containing constants in the MTP and PTP specifications.
 */
public final class MtpConstants {
    // MTP Data Types
    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_UNDEFINED = 0x0;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT8 = 0x1;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_UINT8 = 0x2;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT16 = 0x3;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_UINT16 = 0x4;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT32 = 0x5;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_UINT32 = 0x6;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT64 = 0x7;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_UINT64 = 0x8;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT128 = 0x9;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_UINT128 = 0xa;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AINT8 = 0x4001;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AUINT8 = 0x4002;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AINT16 = 0x4003;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AUINT16 = 0x4004;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AINT32 = 0x4005;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AUINT32 = 0x4006;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AINT64 = 0x4007;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AUINT64 = 0x4008;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AINT128 = 0x4009;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_AUINT128 = 0x400a;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_STR = 0xffff;

    // MTP Response Codes
    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_UNDEFINED = 0x2000;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_OK = 0x2001;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_GENERAL_ERROR = 0x2002;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SESSION_NOT_OPEN = 0x2003;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_TRANSACTION_ID = 0x2004;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_OPERATION_NOT_SUPPORTED = 0x2005;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_PARAMETER_NOT_SUPPORTED = 0x2006;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INCOMPLETE_TRANSFER = 0x2007;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_STORAGE_ID = 0x2008;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_OBJECT_HANDLE = 0x2009;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_DEVICE_PROP_NOT_SUPPORTED = 0x200a;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_OBJECT_FORMAT_CODE = 0x200b;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_STORAGE_FULL = 0x200c;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_OBJECT_WRITE_PROTECTED = 0x200d;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_STORE_READ_ONLY = 0x200e;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_ACCESS_DENIED = 0x200f;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_NO_THUMBNAIL_PRESENT = 0x2010;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SELF_TEST_FAILED = 0x2011;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_PARTIAL_DELETION = 0x2012;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_STORE_NOT_AVAILABLE = 0x2013;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SPECIFICATION_BY_FORMAT_UNSUPPORTED = 0x2014;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_NO_VALID_OBJECT_INFO = 0x2015;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_CODE_FORMAT = 0x2016;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_UNKNOWN_VENDOR_CODE = 0x2017;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_CAPTURE_ALREADY_TERMINATED = 0x2018;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_DEVICE_BUSY = 0x2019;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_PARENT_OBJECT = 0x201a;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_DEVICE_PROP_FORMAT = 0x201b;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_DEVICE_PROP_VALUE = 0x201c;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_PARAMETER = 0x201d;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SESSION_ALREADY_OPEN = 0x201e;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_TRANSACTION_CANCELLED = 0x201f;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SPECIFICATION_OF_DESTINATION_UNSUPPORTED = 0x2020;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_OBJECT_PROP_CODE = 0xa801;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_OBJECT_PROP_FORMAT = 0xa802;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_OBJECT_PROP_VALUE = 0xa803;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_OBJECT_REFERENCE = 0xa804;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_GROUP_NOT_SUPPORTED = 0xa805;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_INVALID_DATASET = 0xa806;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED = 0xa807;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED = 0xa808;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_OBJECT_TOO_LARGE = 0xa809;

    /**
     *
     *
     * @unknown 
     */
    public static final int RESPONSE_OBJECT_PROP_NOT_SUPPORTED = 0xa80a;

    // MTP format codes
    /**
     * Undefined format code
     */
    public static final int FORMAT_UNDEFINED = 0x3000;

    /**
     * Format code for associations (folders and directories)
     */
    public static final int FORMAT_ASSOCIATION = 0x3001;

    /**
     * Format code for script files
     */
    public static final int FORMAT_SCRIPT = 0x3002;

    /**
     * Format code for executable files
     */
    public static final int FORMAT_EXECUTABLE = 0x3003;

    /**
     * Format code for text files
     */
    public static final int FORMAT_TEXT = 0x3004;

    /**
     * Format code for HTML files
     */
    public static final int FORMAT_HTML = 0x3005;

    /**
     * Format code for DPOF files
     */
    public static final int FORMAT_DPOF = 0x3006;

    /**
     * Format code for AIFF audio files
     */
    public static final int FORMAT_AIFF = 0x3007;

    /**
     * Format code for WAV audio files
     */
    public static final int FORMAT_WAV = 0x3008;

    /**
     * Format code for MP3 audio files
     */
    public static final int FORMAT_MP3 = 0x3009;

    /**
     * Format code for AVI video files
     */
    public static final int FORMAT_AVI = 0x300a;

    /**
     * Format code for MPEG video files
     */
    public static final int FORMAT_MPEG = 0x300b;

    /**
     * Format code for ASF files
     */
    public static final int FORMAT_ASF = 0x300c;

    /**
     * Format code for unknown image files.
     * <p>
     * Will be used for the formats which are not specified in PTP specification.
     * For instance, WEBP and WBMP.
     */
    public static final int FORMAT_DEFINED = 0x3800;

    /**
     * Format code for JPEG image files
     */
    public static final int FORMAT_EXIF_JPEG = 0x3801;

    /**
     * Format code for TIFF EP image files
     */
    public static final int FORMAT_TIFF_EP = 0x3802;

    /**
     * Format code for BMP image files
     */
    public static final int FORMAT_BMP = 0x3804;

    /**
     * Format code for GIF image files
     */
    public static final int FORMAT_GIF = 0x3807;

    /**
     * Format code for JFIF image files
     */
    public static final int FORMAT_JFIF = 0x3808;

    /**
     * Format code for PICT image files
     */
    public static final int FORMAT_PICT = 0x380a;

    /**
     * Format code for PNG image files
     */
    public static final int FORMAT_PNG = 0x380b;

    /**
     * Format code for TIFF image files
     */
    public static final int FORMAT_TIFF = 0x380d;

    /**
     * Format code for JP2 files
     */
    public static final int FORMAT_JP2 = 0x380f;

    /**
     * Format code for JPX files
     */
    public static final int FORMAT_JPX = 0x3810;

    /**
     * Format code for DNG files
     */
    public static final int FORMAT_DNG = 0x3811;

    /**
     * Format code for firmware files
     */
    public static final int FORMAT_UNDEFINED_FIRMWARE = 0xb802;

    /**
     * Format code for Windows image files
     */
    public static final int FORMAT_WINDOWS_IMAGE_FORMAT = 0xb881;

    /**
     * Format code for undefined audio files files
     */
    public static final int FORMAT_UNDEFINED_AUDIO = 0xb900;

    /**
     * Format code for WMA audio files
     */
    public static final int FORMAT_WMA = 0xb901;

    /**
     * Format code for OGG audio files
     */
    public static final int FORMAT_OGG = 0xb902;

    /**
     * Format code for AAC audio files
     */
    public static final int FORMAT_AAC = 0xb903;

    /**
     * Format code for Audible audio files
     */
    public static final int FORMAT_AUDIBLE = 0xb904;

    /**
     * Format code for FLAC audio files
     */
    public static final int FORMAT_FLAC = 0xb906;

    /**
     * Format code for undefined video files
     */
    public static final int FORMAT_UNDEFINED_VIDEO = 0xb980;

    /**
     * Format code for WMV video files
     */
    public static final int FORMAT_WMV = 0xb981;

    /**
     * Format code for MP4 files
     */
    public static final int FORMAT_MP4_CONTAINER = 0xb982;

    /**
     * Format code for MP2 files
     */
    public static final int FORMAT_MP2 = 0xb983;

    /**
     * Format code for 3GP files
     */
    public static final int FORMAT_3GP_CONTAINER = 0xb984;

    /**
     * Format code for undefined collections
     */
    public static final int FORMAT_UNDEFINED_COLLECTION = 0xba00;

    /**
     * Format code for multimedia albums
     */
    public static final int FORMAT_ABSTRACT_MULTIMEDIA_ALBUM = 0xba01;

    /**
     * Format code for image albums
     */
    public static final int FORMAT_ABSTRACT_IMAGE_ALBUM = 0xba02;

    /**
     * Format code for audio albums
     */
    public static final int FORMAT_ABSTRACT_AUDIO_ALBUM = 0xba03;

    /**
     * Format code for video albums
     */
    public static final int FORMAT_ABSTRACT_VIDEO_ALBUM = 0xba04;

    /**
     * Format code for abstract AV playlists
     */
    public static final int FORMAT_ABSTRACT_AV_PLAYLIST = 0xba05;

    /**
     * Format code for abstract audio playlists
     */
    public static final int FORMAT_ABSTRACT_AUDIO_PLAYLIST = 0xba09;

    /**
     * Format code for abstract video playlists
     */
    public static final int FORMAT_ABSTRACT_VIDEO_PLAYLIST = 0xba0a;

    /**
     * Format code for abstract mediacasts
     */
    public static final int FORMAT_ABSTRACT_MEDIACAST = 0xba0b;

    /**
     * Format code for WPL playlist files
     */
    public static final int FORMAT_WPL_PLAYLIST = 0xba10;

    /**
     * Format code for M3u playlist files
     */
    public static final int FORMAT_M3U_PLAYLIST = 0xba11;

    /**
     * Format code for MPL playlist files
     */
    public static final int FORMAT_MPL_PLAYLIST = 0xba12;

    /**
     * Format code for ASX playlist files
     */
    public static final int FORMAT_ASX_PLAYLIST = 0xba13;

    /**
     * Format code for PLS playlist files
     */
    public static final int FORMAT_PLS_PLAYLIST = 0xba14;

    /**
     * Format code for undefined document files
     */
    public static final int FORMAT_UNDEFINED_DOCUMENT = 0xba80;

    /**
     * Format code for abstract documents
     */
    public static final int FORMAT_ABSTRACT_DOCUMENT = 0xba81;

    /**
     * Format code for XML documents
     */
    public static final int FORMAT_XML_DOCUMENT = 0xba82;

    /**
     * Format code for MS Word documents
     */
    public static final int FORMAT_MS_WORD_DOCUMENT = 0xba83;

    /**
     * Format code for MS Excel spreadsheets
     */
    public static final int FORMAT_MS_EXCEL_SPREADSHEET = 0xba85;

    /**
     * Format code for MS PowerPoint presentatiosn
     */
    public static final int FORMAT_MS_POWERPOINT_PRESENTATION = 0xba86;

    /**
     * Returns true if the object is abstract (that is, it has no representation
     * in the underlying file system).
     *
     * @param format
     * 		the format of the object
     * @return true if the object is abstract
     */
    public static boolean isAbstractObject(int format) {
        switch (format) {
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_MULTIMEDIA_ALBUM :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_IMAGE_ALBUM :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_AUDIO_ALBUM :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_VIDEO_ALBUM :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_AUDIO_PLAYLIST :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_VIDEO_PLAYLIST :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_MEDIACAST :
            case android.mtp.MtpConstants.FORMAT_ABSTRACT_DOCUMENT :
                return true;
            default :
                return false;
        }
    }

    // MTP object properties
    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_STORAGE_ID = 0xdc01;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_OBJECT_FORMAT = 0xdc02;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PROTECTION_STATUS = 0xdc03;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_OBJECT_SIZE = 0xdc04;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ASSOCIATION_TYPE = 0xdc05;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ASSOCIATION_DESC = 0xdc06;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_OBJECT_FILE_NAME = 0xdc07;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DATE_CREATED = 0xdc08;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DATE_MODIFIED = 0xdc09;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_KEYWORDS = 0xdc0a;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PARENT_OBJECT = 0xdc0b;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ALLOWED_FOLDER_CONTENTS = 0xdc0c;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_HIDDEN = 0xdc0d;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SYSTEM_OBJECT = 0xdc0e;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PERSISTENT_UID = 0xdc41;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SYNC_ID = 0xdc42;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PROPERTY_BAG = 0xdc43;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_NAME = 0xdc44;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_CREATED_BY = 0xdc45;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ARTIST = 0xdc46;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DATE_AUTHORED = 0xdc47;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DESCRIPTION = 0xdc48;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_URL_REFERENCE = 0xdc49;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_LANGUAGE_LOCALE = 0xdc4a;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_COPYRIGHT_INFORMATION = 0xdc4b;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SOURCE = 0xdc4c;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ORIGIN_LOCATION = 0xdc4d;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DATE_ADDED = 0xdc4e;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_NON_CONSUMABLE = 0xdc4f;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_CORRUPT_UNPLAYABLE = 0xdc50;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PRODUCER_SERIAL_NUMBER = 0xdc51;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_REPRESENTATIVE_SAMPLE_FORMAT = 0xdc81;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_REPRESENTATIVE_SAMPLE_SIZE = 0xdc82;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_REPRESENTATIVE_SAMPLE_HEIGHT = 0xdc83;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_REPRESENTATIVE_SAMPLE_WIDTH = 0xdc84;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_REPRESENTATIVE_SAMPLE_DURATION = 0xdc85;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_REPRESENTATIVE_SAMPLE_DATA = 0xdc86;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_WIDTH = 0xdc87;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_HEIGHT = 0xdc88;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DURATION = 0xdc89;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_RATING = 0xdc8a;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_TRACK = 0xdc8b;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_GENRE = 0xdc8c;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_CREDITS = 0xdc8d;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_LYRICS = 0xdc8e;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SUBSCRIPTION_CONTENT_ID = 0xdc8f;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PRODUCED_BY = 0xdc90;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_USE_COUNT = 0xdc91;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SKIP_COUNT = 0xdc92;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_LAST_ACCESSED = 0xdc93;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_PARENTAL_RATING = 0xdc94;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_META_GENRE = 0xdc95;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_COMPOSER = 0xdc96;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_EFFECTIVE_RATING = 0xdc97;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SUBTITLE = 0xdc98;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ORIGINAL_RELEASE_DATE = 0xdc99;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ALBUM_NAME = 0xdc9a;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ALBUM_ARTIST = 0xdc9b;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_MOOD = 0xdc9c;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DRM_STATUS = 0xdc9d;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SUB_DESCRIPTION = 0xdc9e;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_IS_CROPPED = 0xdcd1;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_IS_COLOUR_CORRECTED = 0xdcd2;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_IMAGE_BIT_DEPTH = 0xdcd3;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_F_NUMBER = 0xdcd4;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_EXPOSURE_TIME = 0xdcd5;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_EXPOSURE_INDEX = 0xdcd6;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_TOTAL_BITRATE = 0xde91;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_BITRATE_TYPE = 0xde92;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SAMPLE_RATE = 0xde93;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_NUMBER_OF_CHANNELS = 0xde94;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_AUDIO_BIT_DEPTH = 0xde95;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_SCAN_TYPE = 0xde97;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_AUDIO_WAVE_CODEC = 0xde99;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_AUDIO_BITRATE = 0xde9a;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_VIDEO_FOURCC_CODEC = 0xde9b;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_VIDEO_BITRATE = 0xde9c;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_FRAMES_PER_THOUSAND_SECONDS = 0xde9d;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_KEYFRAME_DISTANCE = 0xde9e;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_BUFFER_SIZE = 0xde9f;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ENCODING_QUALITY = 0xdea0;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_ENCODING_PROFILE = 0xdea1;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROPERTY_DISPLAY_NAME = 0xdce0;

    // MTP device properties
    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_UNDEFINED = 0x5000;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_BATTERY_LEVEL = 0x5001;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_FUNCTIONAL_MODE = 0x5002;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_IMAGE_SIZE = 0x5003;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_COMPRESSION_SETTING = 0x5004;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_WHITE_BALANCE = 0x5005;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_RGB_GAIN = 0x5006;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_F_NUMBER = 0x5007;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_FOCAL_LENGTH = 0x5008;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_FOCUS_DISTANCE = 0x5009;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_FOCUS_MODE = 0x500a;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_EXPOSURE_METERING_MODE = 0x500b;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_FLASH_MODE = 0x500c;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_EXPOSURE_TIME = 0x500d;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_EXPOSURE_PROGRAM_MODE = 0x500e;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_EXPOSURE_INDEX = 0x500f;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_EXPOSURE_BIAS_COMPENSATION = 0x5010;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_DATETIME = 0x5011;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_CAPTURE_DELAY = 0x5012;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_STILL_CAPTURE_MODE = 0x5013;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_CONTRAST = 0x5014;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_SHARPNESS = 0x5015;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_DIGITAL_ZOOM = 0x5016;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_EFFECT_MODE = 0x5017;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_BURST_NUMBER = 0x5018;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_BURST_INTERVAL = 0x5019;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_TIMELAPSE_NUMBER = 0x501a;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_TIMELAPSE_INTERVAL = 0x501b;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_FOCUS_METERING_MODE = 0x501c;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_UPLOAD_URL = 0x501d;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_ARTIST = 0x501e;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_COPYRIGHT_INFO = 0x501f;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER = 0xd401;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME = 0xd402;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_VOLUME = 0xd403;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_SUPPORTED_FORMATS_ORDERED = 0xd404;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_DEVICE_ICON = 0xd405;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_PLAYBACK_RATE = 0xd410;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_PLAYBACK_OBJECT = 0xd411;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_PLAYBACK_CONTAINER_INDEX = 0xd412;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_SESSION_INITIATOR_VERSION_INFO = 0xd406;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEVICE_PROPERTY_PERCEIVED_DEVICE_TYPE = 0xd407;

    /**
     * Object is not protected. It may be modified and deleted, and its properties
     * may be modified.
     */
    public static final int PROTECTION_STATUS_NONE = 0;

    /**
     * Object can not be modified or deleted and its properties can not be modified.
     */
    public static final int PROTECTION_STATUS_READ_ONLY = 0x8001;

    /**
     * Object can not be modified or deleted but its properties are modifiable.
     */
    public static final int PROTECTION_STATUS_READ_ONLY_DATA = 0x8002;

    /**
     * Object's contents can not be transfered from the device, but the object
     * may be moved or deleted and its properties may be modified.
     */
    public static final int PROTECTION_STATUS_NON_TRANSFERABLE_DATA = 0x8003;

    /**
     * Association type for objects representing file system directories.
     */
    public static final int ASSOCIATION_TYPE_GENERIC_FOLDER = 0x1;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_UNDEFINED = 0x4000;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_CANCEL_TRANSACTION = 0x4001;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_OBJECT_ADDED = 0x4002;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_OBJECT_REMOVED = 0x4003;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_STORE_ADDED = 0x4004;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_STORE_REMOVED = 0x4005;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_DEVICE_PROP_CHANGED = 0x4006;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_OBJECT_INFO_CHANGED = 0x4007;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_DEVICE_INFO_CHANGED = 0x4008;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_REQUEST_OBJECT_TRANSFER = 0x4009;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_STORE_FULL = 0x400a;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_DEVICE_RESET = 0x400b;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_STORAGE_INFO_CHANGED = 0x400c;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_CAPTURE_COMPLETE = 0x400d;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_UNREPORTED_STATUS = 0x400e;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_OBJECT_PROP_CHANGED = 0xc801;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_OBJECT_PROP_DESC_CHANGED = 0xc802;

    /**
     *
     *
     * @unknown 
     */
    public static final int EVENT_OBJECT_REFERENCES_CHANGED = 0xc803;

    /**
     * Operation code for GetDeviceInfo
     */
    public static final int OPERATION_GET_DEVICE_INFO = 0x1001;

    /**
     * Operation code for OpenSession
     */
    public static final int OPERATION_OPEN_SESSION = 0x1002;

    /**
     * Operation code for CloseSession
     */
    public static final int OPERATION_CLOSE_SESSION = 0x1003;

    /**
     * Operation code for GetStorageIDs
     */
    public static final int OPERATION_GET_STORAGE_I_DS = 0x1004;

    /**
     * Operation code for GetStorageInfo
     */
    public static final int OPERATION_GET_STORAGE_INFO = 0x1005;

    /**
     * Operation code for GetNumObjects
     */
    public static final int OPERATION_GET_NUM_OBJECTS = 0x1006;

    /**
     * Operation code for GetObjectHandles
     */
    public static final int OPERATION_GET_OBJECT_HANDLES = 0x1007;

    /**
     * Operation code for GetObjectInfo
     */
    public static final int OPERATION_GET_OBJECT_INFO = 0x1008;

    /**
     * Operation code for GetObject
     */
    public static final int OPERATION_GET_OBJECT = 0x1009;

    /**
     * Operation code for GetThumb
     */
    public static final int OPERATION_GET_THUMB = 0x100a;

    /**
     * Operation code for DeleteObject
     */
    public static final int OPERATION_DELETE_OBJECT = 0x100b;

    /**
     * Operation code for SendObjectInfo
     */
    public static final int OPERATION_SEND_OBJECT_INFO = 0x100c;

    /**
     * Operation code for SendObject
     */
    public static final int OPERATION_SEND_OBJECT = 0x100d;

    /**
     * Operation code for InitiateCapture
     */
    public static final int OPERATION_INITIATE_CAPTURE = 0x100e;

    /**
     * Operation code for FormatStore
     */
    public static final int OPERATION_FORMAT_STORE = 0x100f;

    /**
     * Operation code for ResetDevice
     */
    public static final int OPERATION_RESET_DEVICE = 0x1010;

    /**
     * Operation code for SelfTest
     */
    public static final int OPERATION_SELF_TEST = 0x1011;

    /**
     * Operation code for SetObjectProtection
     */
    public static final int OPERATION_SET_OBJECT_PROTECTION = 0x1012;

    /**
     * Operation code for PowerDown
     */
    public static final int OPERATION_POWER_DOWN = 0x1013;

    /**
     * Operation code for GetDevicePropDesc
     */
    public static final int OPERATION_GET_DEVICE_PROP_DESC = 0x1014;

    /**
     * Operation code for GetDevicePropValue
     */
    public static final int OPERATION_GET_DEVICE_PROP_VALUE = 0x1015;

    /**
     * Operation code for SetDevicePropValue
     */
    public static final int OPERATION_SET_DEVICE_PROP_VALUE = 0x1016;

    /**
     * Operation code for ResetDevicePropValue
     */
    public static final int OPERATION_RESET_DEVICE_PROP_VALUE = 0x1017;

    /**
     * Operation code for TerminateOpenCapture
     */
    public static final int OPERATION_TERMINATE_OPEN_CAPTURE = 0x1018;

    /**
     * Operation code for MoveObject
     */
    public static final int OPERATION_MOVE_OBJECT = 0x1019;

    /**
     * Operation code for CopyObject
     */
    public static final int OPERATION_COPY_OBJECT = 0x101a;

    /**
     * Operation code for GetPartialObject
     */
    public static final int OPERATION_GET_PARTIAL_OBJECT = 0x101b;

    /**
     * Operation code for InitiateOpenCapture
     */
    public static final int OPERATION_INITIATE_OPEN_CAPTURE = 0x101c;

    /**
     * Operation code for GetObjectPropsSupported
     */
    public static final int OPERATION_GET_OBJECT_PROPS_SUPPORTED = 0x9801;

    /**
     * Operation code for GetObjectPropDesc
     */
    public static final int OPERATION_GET_OBJECT_PROP_DESC = 0x9802;

    /**
     * Operation code for GetObjectPropValue
     */
    public static final int OPERATION_GET_OBJECT_PROP_VALUE = 0x9803;

    /**
     * Operation code for SetObjectPropValue
     */
    public static final int OPERATION_SET_OBJECT_PROP_VALUE = 0x9804;

    /**
     * Operation code for GetObjectReferences
     */
    public static final int OPERATION_GET_OBJECT_REFERENCES = 0x9810;

    /**
     * Operation code for SetObjectReferences
     */
    public static final int OPERATION_SET_OBJECT_REFERENCES = 0x9811;

    /**
     * Operation code for Skip
     */
    public static final int OPERATION_SKIP = 0x9820;

    /**
     * Operation code for GetPartialObject64
     */
    public static final int OPERATION_GET_PARTIAL_OBJECT_64 = 0x95c1;
}

