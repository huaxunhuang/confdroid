/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.media;


/**
 * This is a class for reading and writing Exif tags in a JPEG file or a RAW image file.
 * <p>
 * Supported formats are: JPEG, DNG, CR2, NEF, NRW, ARW, RW2, ORF and RAF.
 * <p>
 * Attribute mutation is supported for JPEG image files.
 */
public class ExifInterface {
    private static final java.lang.String TAG = "ExifInterface";

    private static final boolean DEBUG = false;

    // The Exif tag names
    /**
     * Type is String.
     */
    public static final java.lang.String TAG_ARTIST = "Artist";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_BITS_PER_SAMPLE = "BitsPerSample";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_COMPRESSION = "Compression";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_COPYRIGHT = "Copyright";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_DATETIME = "DateTime";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_IMAGE_DESCRIPTION = "ImageDescription";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_IMAGE_LENGTH = "ImageLength";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_IMAGE_WIDTH = "ImageWidth";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_MAKE = "Make";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_MODEL = "Model";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_ORIENTATION = "Orientation";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_RESOLUTION_UNIT = "ResolutionUnit";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_ROWS_PER_STRIP = "RowsPerStrip";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SOFTWARE = "Software";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_STRIP_OFFSETS = "StripOffsets";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_TRANSFER_FUNCTION = "TransferFunction";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_WHITE_POINT = "WhitePoint";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_X_RESOLUTION = "XResolution";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_Y_RESOLUTION = "YResolution";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_APERTURE_VALUE = "ApertureValue";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_BRIGHTNESS_VALUE = "BrightnessValue";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_CFA_PATTERN = "CFAPattern";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_COLOR_SPACE = "ColorSpace";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_CONTRAST = "Contrast";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_CUSTOM_RENDERED = "CustomRendered";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";

    /**
     * Type is double.
     */
    public static final java.lang.String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_EXIF_VERSION = "ExifVersion";

    /**
     * Type is double.
     */
    public static final java.lang.String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_EXPOSURE_INDEX = "ExposureIndex";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_EXPOSURE_MODE = "ExposureMode";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_EXPOSURE_PROGRAM = "ExposureProgram";

    /**
     * Type is double.
     */
    public static final java.lang.String TAG_EXPOSURE_TIME = "ExposureTime";

    /**
     * Type is double.
     */
    public static final java.lang.String TAG_F_NUMBER = "FNumber";

    /**
     * Type is double.
     *
     * @deprecated use {@link #TAG_F_NUMBER} instead
     */
    @java.lang.Deprecated
    public static final java.lang.String TAG_APERTURE = "FNumber";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_FILE_SOURCE = "FileSource";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_FLASH = "Flash";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_FLASH_ENERGY = "FlashEnergy";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_FLASHPIX_VERSION = "FlashpixVersion";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_FOCAL_LENGTH = "FocalLength";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_GAIN_CONTROL = "GainControl";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";

    /**
     * Type is int.
     *
     * @deprecated use {@link #TAG_ISO_SPEED_RATINGS} instead
     */
    @java.lang.Deprecated
    public static final java.lang.String TAG_ISO = "ISOSpeedRatings";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_LIGHT_SOURCE = "LightSource";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_MAKER_NOTE = "MakerNote";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_METERING_MODE = "MeteringMode";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_OECF = "OECF";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_PIXEL_X_DIMENSION = "PixelXDimension";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SATURATION = "Saturation";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SCENE_TYPE = "SceneType";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SENSING_METHOD = "SensingMethod";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SHARPNESS = "Sharpness";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SUBSEC_TIME = "SubSecTime";

    /**
     * Type is String.
     *
     * @deprecated use {@link #TAG_SUBSEC_TIME_DIGITIZED} instead
     */
    public static final java.lang.String TAG_SUBSEC_TIME_DIG = "SubSecTimeDigitized";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";

    /**
     * Type is String.
     *
     * @deprecated use {@link #TAG_SUBSEC_TIME_ORIGINAL} instead
     */
    public static final java.lang.String TAG_SUBSEC_TIME_ORIG = "SubSecTimeOriginal";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SUBJECT_AREA = "SubjectArea";

    /**
     * Type is double.
     */
    public static final java.lang.String TAG_SUBJECT_DISTANCE = "SubjectDistance";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_SUBJECT_LOCATION = "SubjectLocation";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_USER_COMMENT = "UserComment";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_WHITE_BALANCE = "WhiteBalance";

    /**
     * The altitude (in meters) based on the reference in TAG_GPS_ALTITUDE_REF.
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_ALTITUDE = "GPSAltitude";

    /**
     * 0 if the altitude is above sea level. 1 if the altitude is below sea
     * level. Type is int.
     */
    public static final java.lang.String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_DOP = "GPSDOP";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_DATESTAMP = "GPSDateStamp";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_DEST_BEARING = "GPSDestBearing";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_GPS_DIFFERENTIAL = "GPSDifferential";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";

    /**
     * Type is rational. Format is "num1/denom1,num2/denom2,num3/denom3".
     */
    public static final java.lang.String TAG_GPS_LATITUDE = "GPSLatitude";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";

    /**
     * Type is rational. Format is "num1/denom1,num2/denom2,num3/denom3".
     */
    public static final java.lang.String TAG_GPS_LONGITUDE = "GPSLongitude";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_MAP_DATUM = "GPSMapDatum";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";

    /**
     * Type is String. Name of GPS processing method used for location finding.
     */
    public static final java.lang.String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_SATELLITES = "GPSSatellites";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_SPEED = "GPSSpeed";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_SPEED_REF = "GPSSpeedRef";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_STATUS = "GPSStatus";

    /**
     * Type is String. Format is "hh:mm:ss".
     */
    public static final java.lang.String TAG_GPS_TIMESTAMP = "GPSTimeStamp";

    /**
     * Type is rational.
     */
    public static final java.lang.String TAG_GPS_TRACK = "GPSTrack";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_TRACK_REF = "GPSTrackRef";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_GPS_VERSION_ID = "GPSVersionID";

    /**
     * Type is String.
     */
    public static final java.lang.String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";

    /**
     * Type is int.
     */
    public static final java.lang.String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";

    // Private tags used for pointing the other IFD offset. The types of the following tags are int.
    private static final java.lang.String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";

    private static final java.lang.String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";

    private static final java.lang.String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";

    // Private tags used for thumbnail information.
    private static final java.lang.String TAG_HAS_THUMBNAIL = "HasThumbnail";

    private static final java.lang.String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";

    private static final java.lang.String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";

    private static final java.lang.String TAG_THUMBNAIL_DATA = "ThumbnailData";

    // Constants used for the Orientation Exif tag.
    public static final int ORIENTATION_UNDEFINED = 0;

    public static final int ORIENTATION_NORMAL = 1;

    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;// left right reversed mirror


    public static final int ORIENTATION_ROTATE_180 = 3;

    public static final int ORIENTATION_FLIP_VERTICAL = 4;// upside down mirror


    // flipped about top-left <--> bottom-right axis
    public static final int ORIENTATION_TRANSPOSE = 5;

    public static final int ORIENTATION_ROTATE_90 = 6;// rotate 90 cw to right it


    // flipped about top-right <--> bottom-left axis
    public static final int ORIENTATION_TRANSVERSE = 7;

    public static final int ORIENTATION_ROTATE_270 = 8;// rotate 270 to right it


    // Constants used for white balance
    public static final int WHITEBALANCE_AUTO = 0;

    public static final int WHITEBALANCE_MANUAL = 1;

    private static final byte[] JPEG_SIGNATURE = new byte[]{ ((byte) (0xff)), ((byte) (0xd8)), ((byte) (0xff)) };

    private static final int JPEG_SIGNATURE_SIZE = 3;

    private static java.text.SimpleDateFormat sFormatter;

    // See Exchangeable image file format for digital still cameras: Exif version 2.2.
    // The following values are for parsing EXIF data area. There are tag groups in EXIF data area.
    // They are called "Image File Directory". They have multiple data formats to cover various
    // image metadata from GPS longitude to camera model name.
    // Types of Exif byte alignments (see JEITA CP-3451 page 10)
    private static final short BYTE_ALIGN_II = 0x4949;// II: Intel order


    private static final short BYTE_ALIGN_MM = 0x4d4d;// MM: Motorola order


    // Formats for the value in IFD entry (See TIFF 6.0 spec Types page 15).
    private static final int IFD_FORMAT_BYTE = 1;

    private static final int IFD_FORMAT_STRING = 2;

    private static final int IFD_FORMAT_USHORT = 3;

    private static final int IFD_FORMAT_ULONG = 4;

    private static final int IFD_FORMAT_URATIONAL = 5;

    private static final int IFD_FORMAT_SBYTE = 6;

    private static final int IFD_FORMAT_UNDEFINED = 7;

    private static final int IFD_FORMAT_SSHORT = 8;

    private static final int IFD_FORMAT_SLONG = 9;

    private static final int IFD_FORMAT_SRATIONAL = 10;

    private static final int IFD_FORMAT_SINGLE = 11;

    private static final int IFD_FORMAT_DOUBLE = 12;

    // Names for the data formats for debugging purpose.
    private static final java.lang.String[] IFD_FORMAT_NAMES = new java.lang.String[]{ "", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE" };

    // Sizes of the components of each IFD value format
    private static final int[] IFD_FORMAT_BYTES_PER_FORMAT = new int[]{ 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8 };

    private static final byte[] EXIF_ASCII_PREFIX = new byte[]{ 0x41, 0x53, 0x43, 0x49, 0x49, 0x0, 0x0, 0x0 };

    // A class for indicating EXIF rational type.
    private static class Rational {
        public final long numerator;

        public final long denominator;

        private Rational(long numerator, long denominator) {
            // Handle erroneous case
            if (denominator == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = numerator;
            this.denominator = denominator;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (numerator + "/") + denominator;
        }

        public double calculate() {
            return ((double) (numerator)) / denominator;
        }
    }

    // A class for indicating EXIF attribute.
    private static class ExifAttribute {
        public final int format;

        public final int numberOfComponents;

        public final byte[] bytes;

        private ExifAttribute(int format, int numberOfComponents, byte[] bytes) {
            this.format = format;
            this.numberOfComponents = numberOfComponents;
            this.bytes = bytes;
        }

        public static android.media.ExifInterface.ExifAttribute createUShort(int[] values, java.nio.ByteOrder byteOrder) {
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(new byte[android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[android.media.ExifInterface.IFD_FORMAT_USHORT] * values.length]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putShort(((short) (value)));
            }
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_USHORT, values.length, buffer.array());
        }

        public static android.media.ExifInterface.ExifAttribute createUShort(int value, java.nio.ByteOrder byteOrder) {
            return android.media.ExifInterface.ExifAttribute.createUShort(new int[]{ value }, byteOrder);
        }

        public static android.media.ExifInterface.ExifAttribute createULong(long[] values, java.nio.ByteOrder byteOrder) {
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(new byte[android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[android.media.ExifInterface.IFD_FORMAT_ULONG] * values.length]);
            buffer.order(byteOrder);
            for (long value : values) {
                buffer.putInt(((int) (value)));
            }
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_ULONG, values.length, buffer.array());
        }

        public static android.media.ExifInterface.ExifAttribute createULong(long value, java.nio.ByteOrder byteOrder) {
            return android.media.ExifInterface.ExifAttribute.createULong(new long[]{ value }, byteOrder);
        }

        public static android.media.ExifInterface.ExifAttribute createSLong(int[] values, java.nio.ByteOrder byteOrder) {
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(new byte[android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[android.media.ExifInterface.IFD_FORMAT_SLONG] * values.length]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putInt(value);
            }
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_SLONG, values.length, buffer.array());
        }

        public static android.media.ExifInterface.ExifAttribute createSLong(int value, java.nio.ByteOrder byteOrder) {
            return android.media.ExifInterface.ExifAttribute.createSLong(new int[]{ value }, byteOrder);
        }

        public static android.media.ExifInterface.ExifAttribute createByte(java.lang.String value) {
            // Exception for GPSAltitudeRef tag
            if (((value.length() == 1) && (value.charAt(0) >= '0')) && (value.charAt(0) <= '1')) {
                final byte[] bytes = new byte[]{ ((byte) (value.charAt(0) - '0')) };
                return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_BYTE, bytes.length, bytes);
            }
            final byte[] ascii = value.getBytes(android.media.ExifInterface.ASCII);
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_BYTE, ascii.length, ascii);
        }

        public static android.media.ExifInterface.ExifAttribute createString(java.lang.String value) {
            final byte[] ascii = (value + '\u0000').getBytes(android.media.ExifInterface.ASCII);
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_STRING, ascii.length, ascii);
        }

        public static android.media.ExifInterface.ExifAttribute createURational(android.media.ExifInterface.Rational[] values, java.nio.ByteOrder byteOrder) {
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(new byte[android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[android.media.ExifInterface.IFD_FORMAT_URATIONAL] * values.length]);
            buffer.order(byteOrder);
            for (android.media.ExifInterface.Rational value : values) {
                buffer.putInt(((int) (value.numerator)));
                buffer.putInt(((int) (value.denominator)));
            }
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_URATIONAL, values.length, buffer.array());
        }

        public static android.media.ExifInterface.ExifAttribute createURational(android.media.ExifInterface.Rational value, java.nio.ByteOrder byteOrder) {
            return android.media.ExifInterface.ExifAttribute.createURational(new android.media.ExifInterface.Rational[]{ value }, byteOrder);
        }

        public static android.media.ExifInterface.ExifAttribute createSRational(android.media.ExifInterface.Rational[] values, java.nio.ByteOrder byteOrder) {
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(new byte[android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[android.media.ExifInterface.IFD_FORMAT_SRATIONAL] * values.length]);
            buffer.order(byteOrder);
            for (android.media.ExifInterface.Rational value : values) {
                buffer.putInt(((int) (value.numerator)));
                buffer.putInt(((int) (value.denominator)));
            }
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_SRATIONAL, values.length, buffer.array());
        }

        public static android.media.ExifInterface.ExifAttribute createSRational(android.media.ExifInterface.Rational value, java.nio.ByteOrder byteOrder) {
            return android.media.ExifInterface.ExifAttribute.createSRational(new android.media.ExifInterface.Rational[]{ value }, byteOrder);
        }

        public static android.media.ExifInterface.ExifAttribute createDouble(double[] values, java.nio.ByteOrder byteOrder) {
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(new byte[android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[android.media.ExifInterface.IFD_FORMAT_DOUBLE] * values.length]);
            buffer.order(byteOrder);
            for (double value : values) {
                buffer.putDouble(value);
            }
            return new android.media.ExifInterface.ExifAttribute(android.media.ExifInterface.IFD_FORMAT_DOUBLE, values.length, buffer.array());
        }

        public static android.media.ExifInterface.ExifAttribute createDouble(double value, java.nio.ByteOrder byteOrder) {
            return android.media.ExifInterface.ExifAttribute.createDouble(new double[]{ value }, byteOrder);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("(" + android.media.ExifInterface.IFD_FORMAT_NAMES[format]) + ", data length:") + bytes.length) + ")";
        }

        private java.lang.Object getValue(java.nio.ByteOrder byteOrder) {
            try {
                android.media.ExifInterface.ByteOrderAwarenessDataInputStream inputStream = new android.media.ExifInterface.ByteOrderAwarenessDataInputStream(bytes);
                inputStream.setByteOrder(byteOrder);
                switch (format) {
                    case android.media.ExifInterface.IFD_FORMAT_BYTE :
                    case android.media.ExifInterface.IFD_FORMAT_SBYTE :
                        {
                            // Exception for GPSAltitudeRef tag
                            if (((bytes.length == 1) && (bytes[0] >= 0)) && (bytes[0] <= 1)) {
                                return new java.lang.String(new char[]{ ((char) (bytes[0] + '0')) });
                            }
                            return new java.lang.String(bytes, android.media.ExifInterface.ASCII);
                        }
                    case android.media.ExifInterface.IFD_FORMAT_UNDEFINED :
                    case android.media.ExifInterface.IFD_FORMAT_STRING :
                        {
                            int index = 0;
                            if (numberOfComponents >= android.media.ExifInterface.EXIF_ASCII_PREFIX.length) {
                                boolean same = true;
                                for (int i = 0; i < android.media.ExifInterface.EXIF_ASCII_PREFIX.length; ++i) {
                                    if (bytes[i] != android.media.ExifInterface.EXIF_ASCII_PREFIX[i]) {
                                        same = false;
                                        break;
                                    }
                                }
                                if (same) {
                                    index = android.media.ExifInterface.EXIF_ASCII_PREFIX.length;
                                }
                            }
                            java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
                            while (index < numberOfComponents) {
                                int ch = bytes[index];
                                if (ch == 0) {
                                    break;
                                }
                                if (ch >= 32) {
                                    stringBuilder.append(((char) (ch)));
                                } else {
                                    stringBuilder.append('?');
                                }
                                ++index;
                            } 
                            return stringBuilder.toString();
                        }
                    case android.media.ExifInterface.IFD_FORMAT_USHORT :
                        {
                            final int[] values = new int[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                values[i] = inputStream.readUnsignedShort();
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_ULONG :
                        {
                            final long[] values = new long[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                values[i] = inputStream.readUnsignedInt();
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_URATIONAL :
                        {
                            final android.media.ExifInterface.Rational[] values = new android.media.ExifInterface.Rational[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                final long numerator = inputStream.readUnsignedInt();
                                final long denominator = inputStream.readUnsignedInt();
                                values[i] = new android.media.ExifInterface.Rational(numerator, denominator);
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SSHORT :
                        {
                            final int[] values = new int[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                values[i] = inputStream.readShort();
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SLONG :
                        {
                            final int[] values = new int[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                values[i] = inputStream.readInt();
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SRATIONAL :
                        {
                            final android.media.ExifInterface.Rational[] values = new android.media.ExifInterface.Rational[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                final long numerator = inputStream.readInt();
                                final long denominator = inputStream.readInt();
                                values[i] = new android.media.ExifInterface.Rational(numerator, denominator);
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SINGLE :
                        {
                            final double[] values = new double[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                values[i] = inputStream.readFloat();
                            }
                            return values;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_DOUBLE :
                        {
                            final double[] values = new double[numberOfComponents];
                            for (int i = 0; i < numberOfComponents; ++i) {
                                values[i] = inputStream.readDouble();
                            }
                            return values;
                        }
                    default :
                        return null;
                }
            } catch (java.io.IOException e) {
                android.util.Log.w(android.media.ExifInterface.TAG, "IOException occurred during reading a value", e);
                return null;
            }
        }

        public double getDoubleValue(java.nio.ByteOrder byteOrder) {
            java.lang.Object value = getValue(byteOrder);
            if (value == null) {
                throw new java.lang.NumberFormatException("NULL can't be converted to a double value");
            }
            if (value instanceof java.lang.String) {
                return java.lang.Double.parseDouble(((java.lang.String) (value)));
            }
            if (value instanceof long[]) {
                long[] array = ((long[]) (value));
                if (array.length == 1) {
                    return array[0];
                }
                throw new java.lang.NumberFormatException("There are more than one component");
            }
            if (value instanceof int[]) {
                int[] array = ((int[]) (value));
                if (array.length == 1) {
                    return array[0];
                }
                throw new java.lang.NumberFormatException("There are more than one component");
            }
            if (value instanceof double[]) {
                double[] array = ((double[]) (value));
                if (array.length == 1) {
                    return array[0];
                }
                throw new java.lang.NumberFormatException("There are more than one component");
            }
            if (value instanceof android.media.ExifInterface.Rational[]) {
                android.media.ExifInterface.Rational[] array = ((android.media.ExifInterface.Rational[]) (value));
                if (array.length == 1) {
                    return array[0].calculate();
                }
                throw new java.lang.NumberFormatException("There are more than one component");
            }
            throw new java.lang.NumberFormatException("Couldn't find a double value");
        }

        public int getIntValue(java.nio.ByteOrder byteOrder) {
            java.lang.Object value = getValue(byteOrder);
            if (value == null) {
                throw new java.lang.NumberFormatException("NULL can't be converted to a integer value");
            }
            if (value instanceof java.lang.String) {
                return java.lang.Integer.parseInt(((java.lang.String) (value)));
            }
            if (value instanceof long[]) {
                long[] array = ((long[]) (value));
                if (array.length == 1) {
                    return ((int) (array[0]));
                }
                throw new java.lang.NumberFormatException("There are more than one component");
            }
            if (value instanceof int[]) {
                int[] array = ((int[]) (value));
                if (array.length == 1) {
                    return array[0];
                }
                throw new java.lang.NumberFormatException("There are more than one component");
            }
            throw new java.lang.NumberFormatException("Couldn't find a integer value");
        }

        public java.lang.String getStringValue(java.nio.ByteOrder byteOrder) {
            java.lang.Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof java.lang.String) {
                return ((java.lang.String) (value));
            }
            final java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
            if (value instanceof long[]) {
                long[] array = ((long[]) (value));
                for (int i = 0; i < array.length; ++i) {
                    stringBuilder.append(array[i]);
                    if ((i + 1) != array.length) {
                        stringBuilder.append(",");
                    }
                }
                return stringBuilder.toString();
            }
            if (value instanceof int[]) {
                int[] array = ((int[]) (value));
                for (int i = 0; i < array.length; ++i) {
                    stringBuilder.append(array[i]);
                    if ((i + 1) != array.length) {
                        stringBuilder.append(",");
                    }
                }
                return stringBuilder.toString();
            }
            if (value instanceof double[]) {
                double[] array = ((double[]) (value));
                for (int i = 0; i < array.length; ++i) {
                    stringBuilder.append(array[i]);
                    if ((i + 1) != array.length) {
                        stringBuilder.append(",");
                    }
                }
                return stringBuilder.toString();
            }
            if (value instanceof android.media.ExifInterface.Rational[]) {
                android.media.ExifInterface.Rational[] array = ((android.media.ExifInterface.Rational[]) (value));
                for (int i = 0; i < array.length; ++i) {
                    stringBuilder.append(array[i].numerator);
                    stringBuilder.append('/');
                    stringBuilder.append(array[i].denominator);
                    if ((i + 1) != array.length) {
                        stringBuilder.append(",");
                    }
                }
                return stringBuilder.toString();
            }
            return null;
        }

        public int size() {
            return android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[format] * numberOfComponents;
        }
    }

    // A class for indicating EXIF tag.
    private static class ExifTag {
        public final int number;

        public final java.lang.String name;

        public final int primaryFormat;

        public final int secondaryFormat;

        private ExifTag(java.lang.String name, int number, int format) {
            this.name = name;
            this.number = number;
            this.primaryFormat = format;
            this.secondaryFormat = -1;
        }

        private ExifTag(java.lang.String name, int number, int primaryFormat, int secondaryFormat) {
            this.name = name;
            this.number = number;
            this.primaryFormat = primaryFormat;
            this.secondaryFormat = secondaryFormat;
        }
    }

    // Primary image IFD TIFF tags (See JEITA CP-3451 Table 14. page 54).
    private static final android.media.ExifInterface.ExifTag[] IFD_TIFF_TAGS = new android.media.ExifInterface.ExifTag[]{ new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_IMAGE_WIDTH, 256, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_IMAGE_LENGTH, 257, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_BITS_PER_SAMPLE, 258, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COMPRESSION, 259, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION, 262, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_IMAGE_DESCRIPTION, 270, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_MAKE, 271, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_MODEL, 272, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_STRIP_OFFSETS, 273, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ORIENTATION, 274, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SAMPLES_PER_PIXEL, 277, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ROWS_PER_STRIP, 278, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_STRIP_BYTE_COUNTS, 279, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_X_RESOLUTION, 282, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_RESOLUTION, 283, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PLANAR_CONFIGURATION, 284, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_RESOLUTION_UNIT, 296, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_TRANSFER_FUNCTION, 301, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SOFTWARE, 305, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_DATETIME, 306, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ARTIST, 315, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_WHITE_POINT, 318, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PRIMARY_CHROMATICITIES, 319, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, 513, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_CB_CR_COEFFICIENTS, 529, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING, 530, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_CB_CR_POSITIONING, 531, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_REFERENCE_BLACK_WHITE, 532, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COPYRIGHT, 33432, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXIF_IFD_POINTER, 34665, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_INFO_IFD_POINTER, 34853, android.media.ExifInterface.IFD_FORMAT_ULONG) };

    // Primary image IFD Exif Private tags (See JEITA CP-3451 Table 15. page 55).
    private static final android.media.ExifInterface.ExifTag[] IFD_EXIF_TAGS = new android.media.ExifInterface.ExifTag[]{ new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXPOSURE_TIME, 33434, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_F_NUMBER, 33437, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXPOSURE_PROGRAM, 34850, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SPECTRAL_SENSITIVITY, 34852, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ISO_SPEED_RATINGS, 34855, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_OECF, 34856, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXIF_VERSION, 36864, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_DATETIME_ORIGINAL, 36867, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_DATETIME_DIGITIZED, 36868, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COMPONENTS_CONFIGURATION, 37121, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL, 37122, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SHUTTER_SPEED_VALUE, 37377, android.media.ExifInterface.IFD_FORMAT_SRATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_APERTURE_VALUE, 37378, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_BRIGHTNESS_VALUE, 37379, android.media.ExifInterface.IFD_FORMAT_SRATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXPOSURE_BIAS_VALUE, 37380, android.media.ExifInterface.IFD_FORMAT_SRATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_MAX_APERTURE_VALUE, 37381, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBJECT_DISTANCE, 37382, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_METERING_MODE, 37383, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_LIGHT_SOURCE, 37384, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FLASH, 37385, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FOCAL_LENGTH, 37386, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBJECT_AREA, 37396, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_MAKER_NOTE, 37500, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_USER_COMMENT, 37510, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBSEC_TIME, 37520, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBSEC_TIME_ORIG, 37521, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBSEC_TIME_DIG, 37522, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FLASHPIX_VERSION, 40960, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COLOR_SPACE, 40961, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PIXEL_X_DIMENSION, 40962, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PIXEL_Y_DIMENSION, 40963, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_RELATED_SOUND_FILE, 40964, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_INTEROPERABILITY_IFD_POINTER, 40965, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FLASH_ENERGY, 41483, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION, 41486, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBJECT_LOCATION, 41492, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXPOSURE_INDEX, 41493, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SENSING_METHOD, 41495, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FILE_SOURCE, 41728, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SCENE_TYPE, 41729, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_CFA_PATTERN, 41730, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_CUSTOM_RENDERED, 41985, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXPOSURE_MODE, 41986, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_WHITE_BALANCE, 41987, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_DIGITAL_ZOOM_RATIO, 41988, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SCENE_CAPTURE_TYPE, 41990, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GAIN_CONTROL, 41991, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_CONTRAST, 41992, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SATURATION, 41993, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SHARPNESS, 41994, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION, 41995, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SUBJECT_DISTANCE_RANGE, 41996, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_IMAGE_UNIQUE_ID, 42016, android.media.ExifInterface.IFD_FORMAT_STRING) };

    // Primary image IFD GPS Info tags (See JEITA CP-3451 Table 16. page 56).
    private static final android.media.ExifInterface.ExifTag[] IFD_GPS_TAGS = new android.media.ExifInterface.ExifTag[]{ new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_VERSION_ID, 0, android.media.ExifInterface.IFD_FORMAT_BYTE), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_LATITUDE_REF, 1, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_LATITUDE, 2, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_LONGITUDE_REF, 3, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_LONGITUDE, 4, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_ALTITUDE_REF, 5, android.media.ExifInterface.IFD_FORMAT_BYTE), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_ALTITUDE, 6, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_TIMESTAMP, 7, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_SATELLITES, 8, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_STATUS, 9, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_MEASURE_MODE, 10, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DOP, 11, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_SPEED_REF, 12, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_SPEED, 13, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_TRACK_REF, 14, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_TRACK, 15, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_IMG_DIRECTION_REF, 16, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_IMG_DIRECTION, 17, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_MAP_DATUM, 18, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_LATITUDE_REF, 19, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_LATITUDE, 20, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_LONGITUDE_REF, 21, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_LONGITUDE, 22, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_BEARING_REF, 23, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_BEARING, 24, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_DISTANCE_REF, 25, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DEST_DISTANCE, 26, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_PROCESSING_METHOD, 27, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_AREA_INFORMATION, 28, android.media.ExifInterface.IFD_FORMAT_UNDEFINED), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DATESTAMP, 29, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_DIFFERENTIAL, 30, android.media.ExifInterface.IFD_FORMAT_USHORT) };

    // Primary image IFD Interoperability tag (See JEITA CP-3451 Table 17. page 56).
    private static final android.media.ExifInterface.ExifTag[] IFD_INTEROPERABILITY_TAGS = new android.media.ExifInterface.ExifTag[]{ new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_INTEROPERABILITY_INDEX, 1, android.media.ExifInterface.IFD_FORMAT_STRING) };

    // IFD Thumbnail tags (See JEITA CP-3451 Table 18. page 57).
    private static final android.media.ExifInterface.ExifTag[] IFD_THUMBNAIL_TAGS = new android.media.ExifInterface.ExifTag[]{ new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH, 256, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH, 257, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_BITS_PER_SAMPLE, 258, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COMPRESSION, 259, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION, 262, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_IMAGE_DESCRIPTION, 270, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_MAKE, 271, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_MODEL, 272, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_STRIP_OFFSETS, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ORIENTATION, 274, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SAMPLES_PER_PIXEL, 277, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ROWS_PER_STRIP, 278, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_STRIP_BYTE_COUNTS, 279, android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_X_RESOLUTION, 282, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_RESOLUTION, 283, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PLANAR_CONFIGURATION, 284, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_RESOLUTION_UNIT, 296, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_TRANSFER_FUNCTION, 301, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_SOFTWARE, 305, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_DATETIME, 306, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_ARTIST, 315, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_WHITE_POINT, 318, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_PRIMARY_CHROMATICITIES, 319, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, 513, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_CB_CR_COEFFICIENTS, 529, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING, 530, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_Y_CB_CR_POSITIONING, 531, android.media.ExifInterface.IFD_FORMAT_USHORT), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_REFERENCE_BLACK_WHITE, 532, android.media.ExifInterface.IFD_FORMAT_URATIONAL), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_COPYRIGHT, 33432, android.media.ExifInterface.IFD_FORMAT_STRING), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXIF_IFD_POINTER, 34665, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_INFO_IFD_POINTER, 34853, android.media.ExifInterface.IFD_FORMAT_ULONG) };

    // See JEITA CP-3451 Figure 5. page 9.
    // The following values are used for indicating pointers to the other Image File Directorys.
    // Indices of Exif Ifd tag groups
    private static final int IFD_TIFF_HINT = 0;

    private static final int IFD_EXIF_HINT = 1;

    private static final int IFD_GPS_HINT = 2;

    private static final int IFD_INTEROPERABILITY_HINT = 3;

    private static final int IFD_THUMBNAIL_HINT = 4;

    // List of Exif tag groups
    private static final android.media.ExifInterface.ExifTag[][] EXIF_TAGS = new android.media.ExifInterface.ExifTag[][]{ android.media.ExifInterface.IFD_TIFF_TAGS, android.media.ExifInterface.IFD_EXIF_TAGS, android.media.ExifInterface.IFD_GPS_TAGS, android.media.ExifInterface.IFD_INTEROPERABILITY_TAGS, android.media.ExifInterface.IFD_THUMBNAIL_TAGS };

    // List of tags for pointing to the other image file directory offset.
    private static final android.media.ExifInterface.ExifTag[] IFD_POINTER_TAGS = new android.media.ExifInterface.ExifTag[]{ new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_EXIF_IFD_POINTER, 34665, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_GPS_INFO_IFD_POINTER, 34853, android.media.ExifInterface.IFD_FORMAT_ULONG), new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_INTEROPERABILITY_IFD_POINTER, 40965, android.media.ExifInterface.IFD_FORMAT_ULONG) };

    // List of indices of the indicated tag groups according to the IFD_POINTER_TAGS
    private static final int[] IFD_POINTER_TAG_HINTS = new int[]{ android.media.ExifInterface.IFD_EXIF_HINT, android.media.ExifInterface.IFD_GPS_HINT, android.media.ExifInterface.IFD_INTEROPERABILITY_HINT };

    // Tags for indicating the thumbnail offset and length
    private static final android.media.ExifInterface.ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, 513, android.media.ExifInterface.IFD_FORMAT_ULONG);

    private static final android.media.ExifInterface.ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new android.media.ExifInterface.ExifTag(android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, android.media.ExifInterface.IFD_FORMAT_ULONG);

    // Mappings from tag number to tag name and each item represents one IFD tag group.
    private static final java.util.HashMap[] sExifTagMapsForReading = new java.util.HashMap[android.media.ExifInterface.EXIF_TAGS.length];

    // Mappings from tag name to tag number and each item represents one IFD tag group.
    private static final java.util.HashMap[] sExifTagMapsForWriting = new java.util.HashMap[android.media.ExifInterface.EXIF_TAGS.length];

    private static final java.util.HashSet<java.lang.String> sTagSetForCompatibility = new java.util.HashSet<>(java.util.Arrays.asList(android.media.ExifInterface.TAG_F_NUMBER, android.media.ExifInterface.TAG_DIGITAL_ZOOM_RATIO, android.media.ExifInterface.TAG_EXPOSURE_TIME, android.media.ExifInterface.TAG_SUBJECT_DISTANCE, android.media.ExifInterface.TAG_GPS_TIMESTAMP));

    // See JPEG File Interchange Format Version 1.02.
    // The following values are defined for handling JPEG streams. In this implementation, we are
    // not only getting information from EXIF but also from some JPEG special segments such as
    // MARKER_COM for user comment and MARKER_SOFx for image width and height.
    private static final java.nio.charset.Charset ASCII = java.nio.charset.Charset.forName("US-ASCII");

    // Identifier for EXIF APP1 segment in JPEG
    private static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(android.media.ExifInterface.ASCII);

    // JPEG segment markers, that each marker consumes two bytes beginning with 0xff and ending with
    // the indicator. There is no SOF4, SOF8, SOF16 markers in JPEG and SOFx markers indicates start
    // of frame(baseline DCT) and the image size info exists in its beginning part.
    private static final byte MARKER = ((byte) (0xff));

    private static final byte MARKER_SOI = ((byte) (0xd8));

    private static final byte MARKER_SOF0 = ((byte) (0xc0));

    private static final byte MARKER_SOF1 = ((byte) (0xc1));

    private static final byte MARKER_SOF2 = ((byte) (0xc2));

    private static final byte MARKER_SOF3 = ((byte) (0xc3));

    private static final byte MARKER_SOF5 = ((byte) (0xc5));

    private static final byte MARKER_SOF6 = ((byte) (0xc6));

    private static final byte MARKER_SOF7 = ((byte) (0xc7));

    private static final byte MARKER_SOF9 = ((byte) (0xc9));

    private static final byte MARKER_SOF10 = ((byte) (0xca));

    private static final byte MARKER_SOF11 = ((byte) (0xcb));

    private static final byte MARKER_SOF13 = ((byte) (0xcd));

    private static final byte MARKER_SOF14 = ((byte) (0xce));

    private static final byte MARKER_SOF15 = ((byte) (0xcf));

    private static final byte MARKER_SOS = ((byte) (0xda));

    private static final byte MARKER_APP1 = ((byte) (0xe1));

    private static final byte MARKER_COM = ((byte) (0xfe));

    private static final byte MARKER_EOI = ((byte) (0xd9));

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.ExifInterface.nativeInitRaw();
        android.media.ExifInterface.sFormatter = new java.text.SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        android.media.ExifInterface.sFormatter.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        // Build up the hash tables to look up Exif tags for reading Exif tags.
        for (int hint = 0; hint < android.media.ExifInterface.EXIF_TAGS.length; ++hint) {
            android.media.ExifInterface.sExifTagMapsForReading[hint] = new java.util.HashMap();
            android.media.ExifInterface.sExifTagMapsForWriting[hint] = new java.util.HashMap();
            for (android.media.ExifInterface.ExifTag tag : android.media.ExifInterface.EXIF_TAGS[hint]) {
                android.media.ExifInterface.sExifTagMapsForReading[hint].put(tag.number, tag);
                android.media.ExifInterface.sExifTagMapsForWriting[hint].put(tag.name, tag);
            }
        }
    }

    private final java.lang.String mFilename;

    private final java.io.FileDescriptor mSeekableFileDescriptor;

    private final android.content.res.AssetManager.AssetInputStream mAssetInputStream;

    private final boolean mIsInputStream;

    private boolean mIsRaw;

    private final java.util.HashMap[] mAttributes = new java.util.HashMap[android.media.ExifInterface.EXIF_TAGS.length];

    private java.nio.ByteOrder mExifByteOrder = java.nio.ByteOrder.BIG_ENDIAN;

    private boolean mHasThumbnail;

    // The following values used for indicating a thumbnail position.
    private int mThumbnailOffset;

    private int mThumbnailLength;

    private byte[] mThumbnailBytes;

    private boolean mIsSupportedFile;

    // Pattern to check non zero timestamp
    private static final java.util.regex.Pattern sNonZeroTimePattern = java.util.regex.Pattern.compile(".*[1-9].*");

    // Pattern to check gps timestamp
    private static final java.util.regex.Pattern sGpsTimestampPattern = java.util.regex.Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");

    /**
     * Reads Exif tags from the specified image file.
     */
    public ExifInterface(java.lang.String filename) throws java.io.IOException {
        if (filename == null) {
            throw new java.lang.IllegalArgumentException("filename cannot be null");
        }
        java.io.FileInputStream in = null;
        mAssetInputStream = null;
        mFilename = filename;
        mIsInputStream = false;
        try {
            in = new java.io.FileInputStream(filename);
            if (android.media.ExifInterface.isSeekableFD(in.getFD())) {
                mSeekableFileDescriptor = in.getFD();
            } else {
                mSeekableFileDescriptor = null;
            }
            loadAttributes(in);
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
        }
    }

    /**
     * Reads Exif tags from the specified image file descriptor. Attribute mutation is supported
     * for writable and seekable file descriptors only. This constructor will not rewind the offset
     * of the given file descriptor. Developers should close the file descriptor after use.
     */
    public ExifInterface(java.io.FileDescriptor fileDescriptor) throws java.io.IOException {
        if (fileDescriptor == null) {
            throw new java.lang.IllegalArgumentException("fileDescriptor cannot be null");
        }
        mAssetInputStream = null;
        mFilename = null;
        if (android.media.ExifInterface.isSeekableFD(fileDescriptor)) {
            mSeekableFileDescriptor = fileDescriptor;
            // Keep the original file descriptor in order to save attributes when it's seekable.
            // Otherwise, just close the given file descriptor after reading it because the save
            // feature won't be working.
            try {
                fileDescriptor = android.system.Os.dup(fileDescriptor);
            } catch (android.system.ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        } else {
            mSeekableFileDescriptor = null;
        }
        mIsInputStream = false;
        java.io.FileInputStream in = null;
        try {
            in = new java.io.FileInputStream(fileDescriptor);
            loadAttributes(in);
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
        }
    }

    /**
     * Reads Exif tags from the specified image input stream. Attribute mutation is not supported
     * for input streams. The given input stream will proceed its current position. Developers
     * should close the input stream after use.
     */
    public ExifInterface(java.io.InputStream inputStream) throws java.io.IOException {
        if (inputStream == null) {
            throw new java.lang.IllegalArgumentException("inputStream cannot be null");
        }
        mFilename = null;
        if (inputStream instanceof android.content.res.AssetManager.AssetInputStream) {
            mAssetInputStream = ((android.content.res.AssetManager.AssetInputStream) (inputStream));
            mSeekableFileDescriptor = null;
        } else
            if ((inputStream instanceof java.io.FileInputStream) && android.media.ExifInterface.isSeekableFD(((java.io.FileInputStream) (inputStream)).getFD())) {
                mAssetInputStream = null;
                mSeekableFileDescriptor = ((java.io.FileInputStream) (inputStream)).getFD();
            } else {
                mAssetInputStream = null;
                mSeekableFileDescriptor = null;
            }

        mIsInputStream = true;
        loadAttributes(inputStream);
    }

    /**
     * Returns the EXIF attribute of the specified tag or {@code null} if there is no such tag in
     * the image file.
     *
     * @param tag
     * 		the name of the tag.
     */
    private android.media.ExifInterface.ExifAttribute getExifAttribute(java.lang.String tag) {
        // Retrieves all tag groups. The value from primary image tag group has a higher priority
        // than the value from the thumbnail tag group if there are more than one candidates.
        for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
            java.lang.Object value = mAttributes[i].get(tag);
            if (value != null) {
                return ((android.media.ExifInterface.ExifAttribute) (value));
            }
        }
        return null;
    }

    /**
     * Returns the value of the specified tag or {@code null} if there
     * is no such tag in the image file.
     *
     * @param tag
     * 		the name of the tag.
     */
    public java.lang.String getAttribute(java.lang.String tag) {
        android.media.ExifInterface.ExifAttribute attribute = getExifAttribute(tag);
        if (attribute != null) {
            if (!android.media.ExifInterface.sTagSetForCompatibility.contains(tag)) {
                return attribute.getStringValue(mExifByteOrder);
            }
            if (tag.equals(android.media.ExifInterface.TAG_GPS_TIMESTAMP)) {
                // Convert the rational values to the custom formats for backwards compatibility.
                if ((attribute.format != android.media.ExifInterface.IFD_FORMAT_URATIONAL) && (attribute.format != android.media.ExifInterface.IFD_FORMAT_SRATIONAL)) {
                    return null;
                }
                android.media.ExifInterface.Rational[] array = ((android.media.ExifInterface.Rational[]) (attribute.getValue(mExifByteOrder)));
                if (array.length != 3) {
                    return null;
                }
                return java.lang.String.format("%02d:%02d:%02d", ((int) (((float) (array[0].numerator)) / array[0].denominator)), ((int) (((float) (array[1].numerator)) / array[1].denominator)), ((int) (((float) (array[2].numerator)) / array[2].denominator)));
            }
            try {
                return java.lang.Double.toString(attribute.getDoubleValue(mExifByteOrder));
            } catch (java.lang.NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Returns the integer value of the specified tag. If there is no such tag
     * in the image file or the value cannot be parsed as integer, return
     * <var>defaultValue</var>.
     *
     * @param tag
     * 		the name of the tag.
     * @param defaultValue
     * 		the value to return if the tag is not available.
     */
    public int getAttributeInt(java.lang.String tag, int defaultValue) {
        android.media.ExifInterface.ExifAttribute exifAttribute = getExifAttribute(tag);
        if (exifAttribute == null) {
            return defaultValue;
        }
        try {
            return exifAttribute.getIntValue(mExifByteOrder);
        } catch (java.lang.NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the double value of the tag that is specified as rational or contains a
     * double-formatted value. If there is no such tag in the image file or the value cannot be
     * parsed as double, return <var>defaultValue</var>.
     *
     * @param tag
     * 		the name of the tag.
     * @param defaultValue
     * 		the value to return if the tag is not available.
     */
    public double getAttributeDouble(java.lang.String tag, double defaultValue) {
        android.media.ExifInterface.ExifAttribute exifAttribute = getExifAttribute(tag);
        if (exifAttribute == null) {
            return defaultValue;
        }
        try {
            return exifAttribute.getDoubleValue(mExifByteOrder);
        } catch (java.lang.NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Set the value of the specified tag.
     *
     * @param tag
     * 		the name of the tag.
     * @param value
     * 		the value of the tag.
     */
    public void setAttribute(java.lang.String tag, java.lang.String value) {
        // Convert the given value to rational values for backwards compatibility.
        if ((value != null) && android.media.ExifInterface.sTagSetForCompatibility.contains(tag)) {
            if (tag.equals(android.media.ExifInterface.TAG_GPS_TIMESTAMP)) {
                java.util.regex.Matcher m = android.media.ExifInterface.sGpsTimestampPattern.matcher(value);
                if (!m.find()) {
                    android.util.Log.w(android.media.ExifInterface.TAG, (("Invalid value for " + tag) + " : ") + value);
                    return;
                }
                value = ((((java.lang.Integer.parseInt(m.group(1)) + "/1,") + java.lang.Integer.parseInt(m.group(2))) + "/1,") + java.lang.Integer.parseInt(m.group(3))) + "/1";
            } else {
                try {
                    double doubleValue = java.lang.Double.parseDouble(value);
                    value = ((long) (doubleValue * 10000L)) + "/10000";
                } catch (java.lang.NumberFormatException e) {
                    android.util.Log.w(android.media.ExifInterface.TAG, (("Invalid value for " + tag) + " : ") + value);
                    return;
                }
            }
        }
        for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
            if ((i == android.media.ExifInterface.IFD_THUMBNAIL_HINT) && (!mHasThumbnail)) {
                continue;
            }
            final java.lang.Object obj = android.media.ExifInterface.sExifTagMapsForWriting[i].get(tag);
            if (obj != null) {
                if (value == null) {
                    mAttributes[i].remove(tag);
                    continue;
                }
                final android.media.ExifInterface.ExifTag exifTag = ((android.media.ExifInterface.ExifTag) (obj));
                android.util.Pair<java.lang.Integer, java.lang.Integer> guess = android.media.ExifInterface.guessDataFormat(value);
                int dataFormat;
                if ((exifTag.primaryFormat == guess.first) || (exifTag.primaryFormat == guess.second)) {
                    dataFormat = exifTag.primaryFormat;
                } else
                    if ((exifTag.secondaryFormat != (-1)) && ((exifTag.secondaryFormat == guess.first) || (exifTag.secondaryFormat == guess.second))) {
                        dataFormat = exifTag.secondaryFormat;
                    } else
                        if (((exifTag.primaryFormat == android.media.ExifInterface.IFD_FORMAT_BYTE) || (exifTag.primaryFormat == android.media.ExifInterface.IFD_FORMAT_UNDEFINED)) || (exifTag.primaryFormat == android.media.ExifInterface.IFD_FORMAT_STRING)) {
                            dataFormat = exifTag.primaryFormat;
                        } else {
                            android.util.Log.w(android.media.ExifInterface.TAG, (((((((("Given tag (" + tag) + ") value didn't match with one of expected ") + "formats: ") + android.media.ExifInterface.IFD_FORMAT_NAMES[exifTag.primaryFormat]) + (exifTag.secondaryFormat == (-1) ? "" : ", " + android.media.ExifInterface.IFD_FORMAT_NAMES[exifTag.secondaryFormat])) + " (guess: ") + android.media.ExifInterface.IFD_FORMAT_NAMES[guess.first]) + (guess.second == (-1) ? "" : ", " + android.media.ExifInterface.IFD_FORMAT_NAMES[guess.second])) + ")");
                            continue;
                        }


                switch (dataFormat) {
                    case android.media.ExifInterface.IFD_FORMAT_BYTE :
                        {
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createByte(value));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_UNDEFINED :
                    case android.media.ExifInterface.IFD_FORMAT_STRING :
                        {
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createString(value));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_USHORT :
                        {
                            final java.lang.String[] values = value.split(",");
                            final int[] intArray = new int[values.length];
                            for (int j = 0; j < values.length; ++j) {
                                intArray[j] = java.lang.Integer.parseInt(values[j]);
                            }
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createUShort(intArray, mExifByteOrder));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SLONG :
                        {
                            final java.lang.String[] values = value.split(",");
                            final int[] intArray = new int[values.length];
                            for (int j = 0; j < values.length; ++j) {
                                intArray[j] = java.lang.Integer.parseInt(values[j]);
                            }
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createSLong(intArray, mExifByteOrder));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_ULONG :
                        {
                            final java.lang.String[] values = value.split(",");
                            final long[] longArray = new long[values.length];
                            for (int j = 0; j < values.length; ++j) {
                                longArray[j] = java.lang.Long.parseLong(values[j]);
                            }
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createULong(longArray, mExifByteOrder));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_URATIONAL :
                        {
                            final java.lang.String[] values = value.split(",");
                            final android.media.ExifInterface.Rational[] rationalArray = new android.media.ExifInterface.Rational[values.length];
                            for (int j = 0; j < values.length; ++j) {
                                final java.lang.String[] numbers = values[j].split("/");
                                rationalArray[j] = new android.media.ExifInterface.Rational(java.lang.Long.parseLong(numbers[0]), java.lang.Long.parseLong(numbers[1]));
                            }
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createURational(rationalArray, mExifByteOrder));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SRATIONAL :
                        {
                            final java.lang.String[] values = value.split(",");
                            final android.media.ExifInterface.Rational[] rationalArray = new android.media.ExifInterface.Rational[values.length];
                            for (int j = 0; j < values.length; ++j) {
                                final java.lang.String[] numbers = values[j].split("/");
                                rationalArray[j] = new android.media.ExifInterface.Rational(java.lang.Long.parseLong(numbers[0]), java.lang.Long.parseLong(numbers[1]));
                            }
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createSRational(rationalArray, mExifByteOrder));
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_DOUBLE :
                        {
                            final java.lang.String[] values = value.split(",");
                            final double[] doubleArray = new double[values.length];
                            for (int j = 0; j < values.length; ++j) {
                                doubleArray[j] = java.lang.Double.parseDouble(values[j]);
                            }
                            mAttributes[i].put(tag, android.media.ExifInterface.ExifAttribute.createDouble(doubleArray, mExifByteOrder));
                            break;
                        }
                    default :
                        android.util.Log.w(android.media.ExifInterface.TAG, "Data format isn't one of expected formats: " + dataFormat);
                        continue;
                }
            }
        }
    }

    /**
     * Update the values of the tags in the tag groups if any value for the tag already was stored.
     *
     * @param tag
     * 		the name of the tag.
     * @param value
     * 		the value of the tag in a form of {@link ExifAttribute}.
     * @return Returns {@code true} if updating is placed.
     */
    private boolean updateAttribute(java.lang.String tag, android.media.ExifInterface.ExifAttribute value) {
        boolean updated = false;
        for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
            if (mAttributes[i].containsKey(tag)) {
                mAttributes[i].put(tag, value);
                updated = true;
            }
        }
        return updated;
    }

    /**
     * Remove any values of the specified tag.
     *
     * @param tag
     * 		the name of the tag.
     */
    private void removeAttribute(java.lang.String tag) {
        for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
            mAttributes[i].remove(tag);
        }
    }

    /**
     * This function decides which parser to read the image data according to the given input stream
     * type and the content of the input stream. In each case, it reads the first three bytes to
     * determine whether the image data format is JPEG or not.
     */
    private void loadAttributes(@android.annotation.NonNull
    java.io.InputStream in) throws java.io.IOException {
        try {
            // Initialize mAttributes.
            for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
                mAttributes[i] = new java.util.HashMap();
            }
            // Process RAW input stream
            if (mAssetInputStream != null) {
                long asset = mAssetInputStream.getNativeAsset();
                if (handleRawResult(android.media.ExifInterface.nativeGetRawAttributesFromAsset(asset))) {
                    return;
                }
            } else
                if (mSeekableFileDescriptor != null) {
                    if (handleRawResult(android.media.ExifInterface.nativeGetRawAttributesFromFileDescriptor(mSeekableFileDescriptor))) {
                        return;
                    }
                } else {
                    in = new java.io.BufferedInputStream(in, android.media.ExifInterface.JPEG_SIGNATURE_SIZE);
                    if ((!android.media.ExifInterface.isJpegInputStream(((java.io.BufferedInputStream) (in)))) && handleRawResult(android.media.ExifInterface.nativeGetRawAttributesFromInputStream(in))) {
                        return;
                    }
                }

            // Process JPEG input stream
            getJpegAttributes(in);
            mIsSupportedFile = true;
        } catch (java.io.IOException e) {
            // Ignore exceptions in order to keep the compatibility with the old versions of
            // ExifInterface.
            mIsSupportedFile = false;
            android.util.Log.w(android.media.ExifInterface.TAG, "Invalid image: ExifInterface got an unsupported image format file" + ("(ExifInterface supports JPEG and some RAW image formats only) " + "or a corrupted JPEG file to ExifInterface."), e);
        } finally {
            addDefaultValuesForCompatibility();
            if (android.media.ExifInterface.DEBUG) {
                printAttributes();
            }
        }
    }

    private static boolean isJpegInputStream(java.io.BufferedInputStream in) throws java.io.IOException {
        in.mark(android.media.ExifInterface.JPEG_SIGNATURE_SIZE);
        byte[] signatureBytes = new byte[android.media.ExifInterface.JPEG_SIGNATURE_SIZE];
        if (in.read(signatureBytes) != android.media.ExifInterface.JPEG_SIGNATURE_SIZE) {
            throw new java.io.EOFException();
        }
        boolean isJpeg = java.util.Arrays.equals(android.media.ExifInterface.JPEG_SIGNATURE, signatureBytes);
        in.reset();
        return isJpeg;
    }

    private boolean handleRawResult(java.util.HashMap map) {
        if (map == null) {
            return false;
        }
        // Mark for disabling the save feature.
        mIsRaw = true;
        java.lang.String value = ((java.lang.String) (map.remove(android.media.ExifInterface.TAG_HAS_THUMBNAIL)));
        mHasThumbnail = (value != null) && value.equalsIgnoreCase("true");
        value = ((java.lang.String) (map.remove(android.media.ExifInterface.TAG_THUMBNAIL_OFFSET)));
        if (value != null) {
            mThumbnailOffset = java.lang.Integer.parseInt(value);
        }
        value = ((java.lang.String) (map.remove(android.media.ExifInterface.TAG_THUMBNAIL_LENGTH)));
        if (value != null) {
            mThumbnailLength = java.lang.Integer.parseInt(value);
        }
        mThumbnailBytes = ((byte[]) (map.remove(android.media.ExifInterface.TAG_THUMBNAIL_DATA)));
        for (java.util.Map.Entry entry : ((java.util.Set<java.util.Map.Entry>) (map.entrySet()))) {
            setAttribute(((java.lang.String) (entry.getKey())), ((java.lang.String) (entry.getValue())));
        }
        return true;
    }

    private static boolean isSeekableFD(java.io.FileDescriptor fd) throws java.io.IOException {
        try {
            android.system.Os.lseek(fd, 0, android.system.OsConstants.SEEK_CUR);
            return true;
        } catch (android.system.ErrnoException e) {
            return false;
        }
    }

    // Prints out attributes for debugging.
    private void printAttributes() {
        for (int i = 0; i < mAttributes.length; ++i) {
            android.util.Log.d(android.media.ExifInterface.TAG, (("The size of tag group[" + i) + "]: ") + mAttributes[i].size());
            for (java.util.Map.Entry entry : ((java.util.Set<java.util.Map.Entry>) (mAttributes[i].entrySet()))) {
                final android.media.ExifInterface.ExifAttribute tagValue = ((android.media.ExifInterface.ExifAttribute) (entry.getValue()));
                android.util.Log.d(android.media.ExifInterface.TAG, ((((("tagName: " + entry.getKey()) + ", tagType: ") + tagValue.toString()) + ", tagValue: '") + tagValue.getStringValue(mExifByteOrder)) + "'");
            }
        }
    }

    /**
     * Save the tag data into the original image file. This is expensive because it involves
     * copying all the data from one file to another and deleting the old file and renaming the
     * other. It's best to use {@link #setAttribute(String,String)} to set all attributes to write
     * and make a single call rather than multiple calls for each attribute.
     * <p>
     * This method is only supported for JPEG files.
     * </p>
     */
    public void saveAttributes() throws java.io.IOException {
        if ((!mIsSupportedFile) || mIsRaw) {
            throw new java.io.IOException("ExifInterface only supports saving attributes on JPEG formats.");
        }
        if (mIsInputStream || ((mSeekableFileDescriptor == null) && (mFilename == null))) {
            throw new java.io.IOException("ExifInterface does not support saving attributes for the current input.");
        }
        // Keep the thumbnail in memory
        mThumbnailBytes = getThumbnail();
        java.io.FileInputStream in = null;
        java.io.FileOutputStream out = null;
        java.io.File tempFile = null;
        try {
            // Move the original file to temporary file.
            if (mFilename != null) {
                tempFile = new java.io.File(mFilename + ".tmp");
                java.io.File originalFile = new java.io.File(mFilename);
                if (!originalFile.renameTo(tempFile)) {
                    throw new java.io.IOException("Could'nt rename to " + tempFile.getAbsolutePath());
                }
            } else
                if (mSeekableFileDescriptor != null) {
                    tempFile = java.io.File.createTempFile("temp", "jpg");
                    android.system.Os.lseek(mSeekableFileDescriptor, 0, android.system.OsConstants.SEEK_SET);
                    in = new java.io.FileInputStream(mSeekableFileDescriptor);
                    out = new java.io.FileOutputStream(tempFile);
                    libcore.io.Streams.copy(in, out);
                }

        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
            libcore.io.IoUtils.closeQuietly(out);
        }
        in = null;
        out = null;
        try {
            // Save the new file.
            in = new java.io.FileInputStream(tempFile);
            if (mFilename != null) {
                out = new java.io.FileOutputStream(mFilename);
            } else
                if (mSeekableFileDescriptor != null) {
                    android.system.Os.lseek(mSeekableFileDescriptor, 0, android.system.OsConstants.SEEK_SET);
                    out = new java.io.FileOutputStream(mSeekableFileDescriptor);
                }

            saveJpegAttributes(in, out);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
            libcore.io.IoUtils.closeQuietly(out);
            tempFile.delete();
        }
        // Discard the thumbnail in memory
        mThumbnailBytes = null;
    }

    /**
     * Returns true if the image file has a thumbnail.
     */
    public boolean hasThumbnail() {
        return mHasThumbnail;
    }

    /**
     * Returns the thumbnail inside the image file, or {@code null} if there is no thumbnail.
     * The returned data is in JPEG format and can be decoded using
     * {@link android.graphics.BitmapFactory#decodeByteArray(byte[],int,int)}
     */
    public byte[] getThumbnail() {
        if (!mHasThumbnail) {
            return null;
        }
        if (mThumbnailBytes != null) {
            return mThumbnailBytes;
        }
        // Read the thumbnail.
        java.io.FileInputStream in = null;
        try {
            if (mAssetInputStream != null) {
                return android.media.ExifInterface.nativeGetThumbnailFromAsset(mAssetInputStream.getNativeAsset(), mThumbnailOffset, mThumbnailLength);
            } else
                if (mFilename != null) {
                    in = new java.io.FileInputStream(mFilename);
                } else
                    if (mSeekableFileDescriptor != null) {
                        java.io.FileDescriptor fileDescriptor = android.system.Os.dup(mSeekableFileDescriptor);
                        android.system.Os.lseek(fileDescriptor, 0, android.system.OsConstants.SEEK_SET);
                        in = new java.io.FileInputStream(fileDescriptor);
                    }


            if (in == null) {
                // Should not be reached this.
                throw new java.io.FileNotFoundException();
            }
            if (in.skip(mThumbnailOffset) != mThumbnailOffset) {
                throw new java.io.IOException("Corrupted image");
            }
            byte[] buffer = new byte[mThumbnailLength];
            if (in.read(buffer) != mThumbnailLength) {
                throw new java.io.IOException("Corrupted image");
            }
            return buffer;
        } catch (java.io.IOException | android.system.ErrnoException e) {
            // Couldn't get a thumbnail image.
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
        }
        return null;
    }

    /**
     * Returns the offset and length of thumbnail inside the image file, or
     * {@code null} if there is no thumbnail.
     *
     * @return two-element array, the offset in the first value, and length in
    the second, or {@code null} if no thumbnail was found.
     */
    public long[] getThumbnailRange() {
        if (!mHasThumbnail) {
            return null;
        }
        long[] range = new long[2];
        range[0] = mThumbnailOffset;
        range[1] = mThumbnailLength;
        return range;
    }

    /**
     * Stores the latitude and longitude value in a float array. The first element is
     * the latitude, and the second element is the longitude. Returns false if the
     * Exif tags are not available.
     */
    public boolean getLatLong(float[] output) {
        java.lang.String latValue = getAttribute(android.media.ExifInterface.TAG_GPS_LATITUDE);
        java.lang.String latRef = getAttribute(android.media.ExifInterface.TAG_GPS_LATITUDE_REF);
        java.lang.String lngValue = getAttribute(android.media.ExifInterface.TAG_GPS_LONGITUDE);
        java.lang.String lngRef = getAttribute(android.media.ExifInterface.TAG_GPS_LONGITUDE_REF);
        if ((((latValue != null) && (latRef != null)) && (lngValue != null)) && (lngRef != null)) {
            try {
                output[0] = android.media.ExifInterface.convertRationalLatLonToFloat(latValue, latRef);
                output[1] = android.media.ExifInterface.convertRationalLatLonToFloat(lngValue, lngRef);
                return true;
            } catch (java.lang.IllegalArgumentException e) {
                // if values are not parseable
            }
        }
        return false;
    }

    /**
     * Return the altitude in meters. If the exif tag does not exist, return
     * <var>defaultValue</var>.
     *
     * @param defaultValue
     * 		the value to return if the tag is not available.
     */
    public double getAltitude(double defaultValue) {
        double altitude = getAttributeDouble(android.media.ExifInterface.TAG_GPS_ALTITUDE, -1);
        int ref = getAttributeInt(android.media.ExifInterface.TAG_GPS_ALTITUDE_REF, -1);
        if ((altitude >= 0) && (ref >= 0)) {
            return altitude * (ref == 1 ? -1 : 1);
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns number of milliseconds since Jan. 1, 1970, midnight local time.
     * Returns -1 if the date time information if not available.
     *
     * @unknown 
     */
    public long getDateTime() {
        java.lang.String dateTimeString = getAttribute(android.media.ExifInterface.TAG_DATETIME);
        if ((dateTimeString == null) || (!android.media.ExifInterface.sNonZeroTimePattern.matcher(dateTimeString).matches()))
            return -1;

        java.text.ParsePosition pos = new java.text.ParsePosition(0);
        try {
            // The exif field is in local time. Parsing it as if it is UTC will yield time
            // since 1/1/1970 local time
            java.util.Date datetime = android.media.ExifInterface.sFormatter.parse(dateTimeString, pos);
            if (datetime == null)
                return -1;

            long msecs = datetime.getTime();
            java.lang.String subSecs = getAttribute(android.media.ExifInterface.TAG_SUBSEC_TIME);
            if (subSecs != null) {
                try {
                    long sub = java.lang.Long.valueOf(subSecs);
                    while (sub > 1000) {
                        sub /= 10;
                    } 
                    msecs += sub;
                } catch (java.lang.NumberFormatException e) {
                    // Ignored
                }
            }
            return msecs;
        } catch (java.lang.IllegalArgumentException e) {
            return -1;
        }
    }

    /**
     * Returns number of milliseconds since Jan. 1, 1970, midnight UTC.
     * Returns -1 if the date time information if not available.
     *
     * @unknown 
     */
    public long getGpsDateTime() {
        java.lang.String date = getAttribute(android.media.ExifInterface.TAG_GPS_DATESTAMP);
        java.lang.String time = getAttribute(android.media.ExifInterface.TAG_GPS_TIMESTAMP);
        if (((date == null) || (time == null)) || ((!android.media.ExifInterface.sNonZeroTimePattern.matcher(date).matches()) && (!android.media.ExifInterface.sNonZeroTimePattern.matcher(time).matches()))) {
            return -1;
        }
        java.lang.String dateTimeString = (date + ' ') + time;
        java.text.ParsePosition pos = new java.text.ParsePosition(0);
        try {
            java.util.Date datetime = android.media.ExifInterface.sFormatter.parse(dateTimeString, pos);
            if (datetime == null)
                return -1;

            return datetime.getTime();
        } catch (java.lang.IllegalArgumentException e) {
            return -1;
        }
    }

    private static float convertRationalLatLonToFloat(java.lang.String rationalString, java.lang.String ref) {
        try {
            java.lang.String[] parts = rationalString.split(",");
            java.lang.String[] pair;
            pair = parts[0].split("/");
            double degrees = java.lang.Double.parseDouble(pair[0].trim()) / java.lang.Double.parseDouble(pair[1].trim());
            pair = parts[1].split("/");
            double minutes = java.lang.Double.parseDouble(pair[0].trim()) / java.lang.Double.parseDouble(pair[1].trim());
            pair = parts[2].split("/");
            double seconds = java.lang.Double.parseDouble(pair[0].trim()) / java.lang.Double.parseDouble(pair[1].trim());
            double result = (degrees + (minutes / 60.0)) + (seconds / 3600.0);
            if (ref.equals("S") || ref.equals("W")) {
                return ((float) (-result));
            }
            return ((float) (result));
        } catch (java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException e) {
            // Not valid
            throw new java.lang.IllegalArgumentException();
        }
    }

    // Loads EXIF attributes from a JPEG input stream.
    private void getJpegAttributes(java.io.InputStream inputStream) throws java.io.IOException {
        // See JPEG File Interchange Format Specification page 5.
        if (android.media.ExifInterface.DEBUG) {
            android.util.Log.d(android.media.ExifInterface.TAG, "getJpegAttributes starting with: " + inputStream);
        }
        java.io.DataInputStream dataInputStream = new java.io.DataInputStream(inputStream);
        byte marker;
        int bytesRead = 0;
        if ((marker = dataInputStream.readByte()) != android.media.ExifInterface.MARKER) {
            throw new java.io.IOException("Invalid marker: " + java.lang.Integer.toHexString(marker & 0xff));
        }
        ++bytesRead;
        if (dataInputStream.readByte() != android.media.ExifInterface.MARKER_SOI) {
            throw new java.io.IOException("Invalid marker: " + java.lang.Integer.toHexString(marker & 0xff));
        }
        ++bytesRead;
        while (true) {
            marker = dataInputStream.readByte();
            if (marker != android.media.ExifInterface.MARKER) {
                throw new java.io.IOException("Invalid marker:" + java.lang.Integer.toHexString(marker & 0xff));
            }
            ++bytesRead;
            marker = dataInputStream.readByte();
            if (android.media.ExifInterface.DEBUG) {
                android.util.Log.d(android.media.ExifInterface.TAG, "Found JPEG segment indicator: " + java.lang.Integer.toHexString(marker & 0xff));
            }
            ++bytesRead;
            // EOI indicates the end of an image and in case of SOS, JPEG image stream starts and
            // the image data will terminate right after.
            if ((marker == android.media.ExifInterface.MARKER_EOI) || (marker == android.media.ExifInterface.MARKER_SOS)) {
                break;
            }
            int length = dataInputStream.readUnsignedShort() - 2;
            bytesRead += 2;
            if (android.media.ExifInterface.DEBUG) {
                android.util.Log.d(android.media.ExifInterface.TAG, ((("JPEG segment: " + java.lang.Integer.toHexString(marker & 0xff)) + " (length: ") + (length + 2)) + ")");
            }
            if (length < 0) {
                throw new java.io.IOException("Invalid length");
            }
            switch (marker) {
                case android.media.ExifInterface.MARKER_APP1 :
                    {
                        if (android.media.ExifInterface.DEBUG) {
                            android.util.Log.d(android.media.ExifInterface.TAG, "MARKER_APP1");
                        }
                        if (length < 6) {
                            // Skip if it's not an EXIF APP1 segment.
                            break;
                        }
                        byte[] identifier = new byte[6];
                        if (inputStream.read(identifier) != 6) {
                            throw new java.io.IOException("Invalid exif");
                        }
                        bytesRead += 6;
                        length -= 6;
                        if (!java.util.Arrays.equals(identifier, android.media.ExifInterface.IDENTIFIER_EXIF_APP1)) {
                            // Skip if it's not an EXIF APP1 segment.
                            break;
                        }
                        if (length <= 0) {
                            throw new java.io.IOException("Invalid exif");
                        }
                        if (android.media.ExifInterface.DEBUG) {
                            android.util.Log.d(android.media.ExifInterface.TAG, ("readExifSegment with a byte array (length: " + length) + ")");
                        }
                        byte[] bytes = new byte[length];
                        if (dataInputStream.read(bytes) != length) {
                            throw new java.io.IOException("Invalid exif");
                        }
                        readExifSegment(bytes, bytesRead);
                        bytesRead += length;
                        length = 0;
                        break;
                    }
                case android.media.ExifInterface.MARKER_COM :
                    {
                        byte[] bytes = new byte[length];
                        if (dataInputStream.read(bytes) != length) {
                            throw new java.io.IOException("Invalid exif");
                        }
                        length = 0;
                        if (getAttribute(android.media.ExifInterface.TAG_USER_COMMENT) == null) {
                            mAttributes[android.media.ExifInterface.IFD_EXIF_HINT].put(android.media.ExifInterface.TAG_USER_COMMENT, android.media.ExifInterface.ExifAttribute.createString(new java.lang.String(bytes, android.media.ExifInterface.ASCII)));
                        }
                        break;
                    }
                case android.media.ExifInterface.MARKER_SOF0 :
                case android.media.ExifInterface.MARKER_SOF1 :
                case android.media.ExifInterface.MARKER_SOF2 :
                case android.media.ExifInterface.MARKER_SOF3 :
                case android.media.ExifInterface.MARKER_SOF5 :
                case android.media.ExifInterface.MARKER_SOF6 :
                case android.media.ExifInterface.MARKER_SOF7 :
                case android.media.ExifInterface.MARKER_SOF9 :
                case android.media.ExifInterface.MARKER_SOF10 :
                case android.media.ExifInterface.MARKER_SOF11 :
                case android.media.ExifInterface.MARKER_SOF13 :
                case android.media.ExifInterface.MARKER_SOF14 :
                case android.media.ExifInterface.MARKER_SOF15 :
                    {
                        if (dataInputStream.skipBytes(1) != 1) {
                            throw new java.io.IOException("Invalid SOFx");
                        }
                        mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.TAG_IMAGE_LENGTH, android.media.ExifInterface.ExifAttribute.createULong(dataInputStream.readUnsignedShort(), mExifByteOrder));
                        mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.TAG_IMAGE_WIDTH, android.media.ExifInterface.ExifAttribute.createULong(dataInputStream.readUnsignedShort(), mExifByteOrder));
                        length -= 5;
                        break;
                    }
                default :
                    {
                        break;
                    }
            }
            if (length < 0) {
                throw new java.io.IOException("Invalid length");
            }
            if (dataInputStream.skipBytes(length) != length) {
                throw new java.io.IOException("Invalid JPEG segment");
            }
            bytesRead += length;
        } 
    }

    // Stores a new JPEG image with EXIF attributes into a given output stream.
    private void saveJpegAttributes(java.io.InputStream inputStream, java.io.OutputStream outputStream) throws java.io.IOException {
        // See JPEG File Interchange Format Specification page 5.
        if (android.media.ExifInterface.DEBUG) {
            android.util.Log.d(android.media.ExifInterface.TAG, ((("saveJpegAttributes starting with (inputStream: " + inputStream) + ", outputStream: ") + outputStream) + ")");
        }
        java.io.DataInputStream dataInputStream = new java.io.DataInputStream(inputStream);
        android.media.ExifInterface.ByteOrderAwarenessDataOutputStream dataOutputStream = new android.media.ExifInterface.ByteOrderAwarenessDataOutputStream(outputStream, java.nio.ByteOrder.BIG_ENDIAN);
        if (dataInputStream.readByte() != android.media.ExifInterface.MARKER) {
            throw new java.io.IOException("Invalid marker");
        }
        dataOutputStream.writeByte(android.media.ExifInterface.MARKER);
        if (dataInputStream.readByte() != android.media.ExifInterface.MARKER_SOI) {
            throw new java.io.IOException("Invalid marker");
        }
        dataOutputStream.writeByte(android.media.ExifInterface.MARKER_SOI);
        // Write EXIF APP1 segment
        dataOutputStream.writeByte(android.media.ExifInterface.MARKER);
        dataOutputStream.writeByte(android.media.ExifInterface.MARKER_APP1);
        writeExifSegment(dataOutputStream, 6);
        byte[] bytes = new byte[4096];
        while (true) {
            byte marker = dataInputStream.readByte();
            if (marker != android.media.ExifInterface.MARKER) {
                throw new java.io.IOException("Invalid marker");
            }
            marker = dataInputStream.readByte();
            switch (marker) {
                case android.media.ExifInterface.MARKER_APP1 :
                    {
                        int length = dataInputStream.readUnsignedShort() - 2;
                        if (length < 0) {
                            throw new java.io.IOException("Invalid length");
                        }
                        byte[] identifier = new byte[6];
                        if (length >= 6) {
                            if (dataInputStream.read(identifier) != 6) {
                                throw new java.io.IOException("Invalid exif");
                            }
                            if (java.util.Arrays.equals(identifier, android.media.ExifInterface.IDENTIFIER_EXIF_APP1)) {
                                // Skip the original EXIF APP1 segment.
                                if (dataInputStream.skip(length - 6) != (length - 6)) {
                                    throw new java.io.IOException("Invalid length");
                                }
                                break;
                            }
                        }
                        // Copy non-EXIF APP1 segment.
                        dataOutputStream.writeByte(android.media.ExifInterface.MARKER);
                        dataOutputStream.writeByte(marker);
                        dataOutputStream.writeUnsignedShort(length + 2);
                        if (length >= 6) {
                            length -= 6;
                            dataOutputStream.write(identifier);
                        }
                        int read;
                        while ((length > 0) && ((read = dataInputStream.read(bytes, 0, java.lang.Math.min(length, bytes.length))) >= 0)) {
                            dataOutputStream.write(bytes, 0, read);
                            length -= read;
                        } 
                        break;
                    }
                case android.media.ExifInterface.MARKER_EOI :
                case android.media.ExifInterface.MARKER_SOS :
                    {
                        dataOutputStream.writeByte(android.media.ExifInterface.MARKER);
                        dataOutputStream.writeByte(marker);
                        // Copy all the remaining data
                        libcore.io.Streams.copy(dataInputStream, dataOutputStream);
                        return;
                    }
                default :
                    {
                        // Copy JPEG segment
                        dataOutputStream.writeByte(android.media.ExifInterface.MARKER);
                        dataOutputStream.writeByte(marker);
                        int length = dataInputStream.readUnsignedShort();
                        dataOutputStream.writeUnsignedShort(length);
                        length -= 2;
                        if (length < 0) {
                            throw new java.io.IOException("Invalid length");
                        }
                        int read;
                        while ((length > 0) && ((read = dataInputStream.read(bytes, 0, java.lang.Math.min(length, bytes.length))) >= 0)) {
                            dataOutputStream.write(bytes, 0, read);
                            length -= read;
                        } 
                        break;
                    }
            }
        } 
    }

    // Reads the given EXIF byte area and save its tag data into attributes.
    private void readExifSegment(byte[] exifBytes, int exifOffsetFromBeginning) throws java.io.IOException {
        // Parse TIFF Headers. See JEITA CP-3451C Table 1. page 10.
        android.media.ExifInterface.ByteOrderAwarenessDataInputStream dataInputStream = new android.media.ExifInterface.ByteOrderAwarenessDataInputStream(exifBytes);
        // Read byte align
        short byteOrder = dataInputStream.readShort();
        switch (byteOrder) {
            case android.media.ExifInterface.BYTE_ALIGN_II :
                if (android.media.ExifInterface.DEBUG) {
                    android.util.Log.d(android.media.ExifInterface.TAG, "readExifSegment: Byte Align II");
                }
                mExifByteOrder = java.nio.ByteOrder.LITTLE_ENDIAN;
                break;
            case android.media.ExifInterface.BYTE_ALIGN_MM :
                if (android.media.ExifInterface.DEBUG) {
                    android.util.Log.d(android.media.ExifInterface.TAG, "readExifSegment: Byte Align MM");
                }
                mExifByteOrder = java.nio.ByteOrder.BIG_ENDIAN;
                break;
            default :
                throw new java.io.IOException("Invalid byte order: " + java.lang.Integer.toHexString(byteOrder));
        }
        // Set byte order.
        dataInputStream.setByteOrder(mExifByteOrder);
        int startCode = dataInputStream.readUnsignedShort();
        if (startCode != 0x2a) {
            throw new java.io.IOException("Invalid exif start: " + java.lang.Integer.toHexString(startCode));
        }
        // Read first ifd offset
        long firstIfdOffset = dataInputStream.readUnsignedInt();
        if ((firstIfdOffset < 8) || (firstIfdOffset >= exifBytes.length)) {
            throw new java.io.IOException("Invalid first Ifd offset: " + firstIfdOffset);
        }
        firstIfdOffset -= 8;
        if (firstIfdOffset > 0) {
            if (dataInputStream.skip(firstIfdOffset) != firstIfdOffset) {
                throw new java.io.IOException("Couldn't jump to first Ifd: " + firstIfdOffset);
            }
        }
        // Read primary image TIFF image file directory.
        readImageFileDirectory(dataInputStream, android.media.ExifInterface.IFD_TIFF_HINT);
        // Process thumbnail.
        java.lang.String jpegInterchangeFormatString = getAttribute(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name);
        java.lang.String jpegInterchangeFormatLengthString = getAttribute(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        if ((jpegInterchangeFormatString != null) && (jpegInterchangeFormatLengthString != null)) {
            try {
                int jpegInterchangeFormat = java.lang.Integer.parseInt(jpegInterchangeFormatString);
                int jpegInterchangeFormatLength = java.lang.Integer.parseInt(jpegInterchangeFormatLengthString);
                // The following code limits the size of thumbnail size not to overflow EXIF data area.
                jpegInterchangeFormatLength = java.lang.Math.min(jpegInterchangeFormat + jpegInterchangeFormatLength, exifBytes.length) - jpegInterchangeFormat;
                if ((jpegInterchangeFormat > 0) && (jpegInterchangeFormatLength > 0)) {
                    mHasThumbnail = true;
                    mThumbnailOffset = exifOffsetFromBeginning + jpegInterchangeFormat;
                    mThumbnailLength = jpegInterchangeFormatLength;
                    if (((mFilename == null) && (mAssetInputStream == null)) && (mSeekableFileDescriptor == null)) {
                        // Save the thumbnail in memory if the input doesn't support reading again.
                        byte[] thumbnailBytes = new byte[jpegInterchangeFormatLength];
                        dataInputStream.seek(jpegInterchangeFormat);
                        dataInputStream.readFully(thumbnailBytes);
                        mThumbnailBytes = thumbnailBytes;
                        if (android.media.ExifInterface.DEBUG) {
                            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
                            android.util.Log.d(android.media.ExifInterface.TAG, (((((("Thumbnail offset: " + mThumbnailOffset) + ", length: ") + mThumbnailLength) + ", width: ") + bitmap.getWidth()) + ", height: ") + bitmap.getHeight());
                        }
                    }
                }
            } catch (java.lang.NumberFormatException e) {
                // Ignored the corrupted image.
            }
        }
    }

    private void addDefaultValuesForCompatibility() {
        // The value of DATETIME tag has the same value of DATETIME_ORIGINAL tag.
        java.lang.String valueOfDateTimeOriginal = getAttribute(android.media.ExifInterface.TAG_DATETIME_ORIGINAL);
        if (valueOfDateTimeOriginal != null) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.TAG_DATETIME, android.media.ExifInterface.ExifAttribute.createString(valueOfDateTimeOriginal));
        }
        // Add the default value.
        if (getAttribute(android.media.ExifInterface.TAG_IMAGE_WIDTH) == null) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.TAG_IMAGE_WIDTH, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
        if (getAttribute(android.media.ExifInterface.TAG_IMAGE_LENGTH) == null) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.TAG_IMAGE_LENGTH, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
        if (getAttribute(android.media.ExifInterface.TAG_ORIENTATION) == null) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
        if (getAttribute(android.media.ExifInterface.TAG_LIGHT_SOURCE) == null) {
            mAttributes[android.media.ExifInterface.IFD_EXIF_HINT].put(android.media.ExifInterface.TAG_LIGHT_SOURCE, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
    }

    // Reads image file directory, which is a tag group in EXIF.
    private void readImageFileDirectory(android.media.ExifInterface.ByteOrderAwarenessDataInputStream dataInputStream, int hint) throws java.io.IOException {
        if ((dataInputStream.peek() + 2) > dataInputStream.mLength) {
            // Return if there is no data from the offset.
            return;
        }
        // See JEITA CP-3451 Figure 5. page 9.
        short numberOfDirectoryEntry = dataInputStream.readShort();
        if ((dataInputStream.peek() + (12 * numberOfDirectoryEntry)) > dataInputStream.mLength) {
            // Return if the size of entries is too big.
            return;
        }
        if (android.media.ExifInterface.DEBUG) {
            android.util.Log.d(android.media.ExifInterface.TAG, "numberOfDirectoryEntry: " + numberOfDirectoryEntry);
        }
        for (short i = 0; i < numberOfDirectoryEntry; ++i) {
            int tagNumber = dataInputStream.readUnsignedShort();
            int dataFormat = dataInputStream.readUnsignedShort();
            int numberOfComponents = dataInputStream.readInt();
            long nextEntryOffset = dataInputStream.peek() + 4;// next four bytes is for data

            // offset or value.
            // Look up a corresponding tag from tag number
            final android.media.ExifInterface.ExifTag tag = ((android.media.ExifInterface.ExifTag) (android.media.ExifInterface.sExifTagMapsForReading[hint].get(tagNumber)));
            if (android.media.ExifInterface.DEBUG) {
                android.util.Log.d(android.media.ExifInterface.TAG, java.lang.String.format("hint: %d, tagNumber: %d, tagName: %s, dataFormat: %d, " + "numberOfComponents: %d", hint, tagNumber, tag != null ? tag.name : null, dataFormat, numberOfComponents));
            }
            if (((tag == null) || (dataFormat <= 0)) || (dataFormat >= android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT.length)) {
                // Skip if the parsed tag number is not defined or invalid data format.
                if (tag == null) {
                    android.util.Log.w(android.media.ExifInterface.TAG, "Skip the tag entry since tag number is not defined: " + tagNumber);
                } else {
                    android.util.Log.w(android.media.ExifInterface.TAG, "Skip the tag entry since data format is invalid: " + dataFormat);
                }
                dataInputStream.seek(nextEntryOffset);
                continue;
            }
            // Read a value from data field or seek to the value offset which is stored in data
            // field if the size of the entry value is bigger than 4.
            int byteCount = numberOfComponents * android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[dataFormat];
            if (byteCount > 4) {
                long offset = dataInputStream.readUnsignedInt();
                if (android.media.ExifInterface.DEBUG) {
                    android.util.Log.d(android.media.ExifInterface.TAG, "seek to data offset: " + offset);
                }
                if ((offset + byteCount) <= dataInputStream.mLength) {
                    dataInputStream.seek(offset);
                } else {
                    // Skip if invalid data offset.
                    android.util.Log.w(android.media.ExifInterface.TAG, "Skip the tag entry since data offset is invalid: " + offset);
                    dataInputStream.seek(nextEntryOffset);
                    continue;
                }
            }
            // Recursively parse IFD when a IFD pointer tag appears.
            int innerIfdHint = android.media.ExifInterface.getIfdHintFromTagNumber(tagNumber);
            if (android.media.ExifInterface.DEBUG) {
                android.util.Log.d(android.media.ExifInterface.TAG, (("innerIfdHint: " + innerIfdHint) + " byteCount: ") + byteCount);
            }
            if (innerIfdHint >= 0) {
                long offset = -1L;
                // Get offset from data field
                switch (dataFormat) {
                    case android.media.ExifInterface.IFD_FORMAT_USHORT :
                        {
                            offset = dataInputStream.readUnsignedShort();
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SSHORT :
                        {
                            offset = dataInputStream.readShort();
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_ULONG :
                        {
                            offset = dataInputStream.readUnsignedInt();
                            break;
                        }
                    case android.media.ExifInterface.IFD_FORMAT_SLONG :
                        {
                            offset = dataInputStream.readInt();
                            break;
                        }
                    default :
                        {
                            // Nothing to do
                            break;
                        }
                }
                if (android.media.ExifInterface.DEBUG) {
                    android.util.Log.d(android.media.ExifInterface.TAG, java.lang.String.format("Offset: %d, tagName: %s", offset, tag.name));
                }
                if ((offset > 0L) && (offset < dataInputStream.mLength)) {
                    dataInputStream.seek(offset);
                    readImageFileDirectory(dataInputStream, innerIfdHint);
                } else {
                    android.util.Log.w(android.media.ExifInterface.TAG, "Skip jump into the IFD since its offset is invalid: " + offset);
                }
                dataInputStream.seek(nextEntryOffset);
                continue;
            }
            byte[] bytes = new byte[numberOfComponents * android.media.ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[dataFormat]];
            dataInputStream.readFully(bytes);
            mAttributes[hint].put(tag.name, new android.media.ExifInterface.ExifAttribute(dataFormat, numberOfComponents, bytes));
            if (dataInputStream.peek() != nextEntryOffset) {
                dataInputStream.seek(nextEntryOffset);
            }
        }
        if ((dataInputStream.peek() + 4) <= dataInputStream.mLength) {
            long nextIfdOffset = dataInputStream.readUnsignedInt();
            if (android.media.ExifInterface.DEBUG) {
                android.util.Log.d(android.media.ExifInterface.TAG, java.lang.String.format("nextIfdOffset: %d", nextIfdOffset));
            }
            // The next IFD offset needs to be bigger than 8
            // since the first IFD offset is at least 8.
            if ((nextIfdOffset > 8) && (nextIfdOffset < dataInputStream.mLength)) {
                dataInputStream.seek(nextIfdOffset);
                readImageFileDirectory(dataInputStream, android.media.ExifInterface.IFD_THUMBNAIL_HINT);
            }
        }
    }

    // Gets the corresponding IFD group index of the given tag number for writing Exif Tags.
    private static int getIfdHintFromTagNumber(int tagNumber) {
        for (int i = 0; i < android.media.ExifInterface.IFD_POINTER_TAG_HINTS.length; ++i) {
            if (android.media.ExifInterface.IFD_POINTER_TAGS[i].number == tagNumber) {
                return android.media.ExifInterface.IFD_POINTER_TAG_HINTS[i];
            }
        }
        return -1;
    }

    // Writes an Exif segment into the given output stream.
    private int writeExifSegment(android.media.ExifInterface.ByteOrderAwarenessDataOutputStream dataOutputStream, int exifOffsetFromBeginning) throws java.io.IOException {
        // The following variables are for calculating each IFD tag group size in bytes.
        int[] ifdOffsets = new int[android.media.ExifInterface.EXIF_TAGS.length];
        int[] ifdDataSizes = new int[android.media.ExifInterface.EXIF_TAGS.length];
        // Remove IFD pointer tags (we'll re-add it later.)
        for (android.media.ExifInterface.ExifTag tag : android.media.ExifInterface.IFD_POINTER_TAGS) {
            removeAttribute(tag.name);
        }
        // Remove old thumbnail data
        removeAttribute(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name);
        removeAttribute(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        // Remove null value tags.
        for (int hint = 0; hint < android.media.ExifInterface.EXIF_TAGS.length; ++hint) {
            for (java.lang.Object obj : mAttributes[hint].entrySet().toArray()) {
                final java.util.Map.Entry entry = ((java.util.Map.Entry) (obj));
                if (entry.getValue() == null) {
                    mAttributes[hint].remove(entry.getKey());
                }
            }
        }
        // Add IFD pointer tags. The next offset of primary image TIFF IFD will have thumbnail IFD
        // offset when there is one or more tags in the thumbnail IFD.
        if (!mAttributes[android.media.ExifInterface.IFD_INTEROPERABILITY_HINT].isEmpty()) {
            mAttributes[android.media.ExifInterface.IFD_EXIF_HINT].put(android.media.ExifInterface.IFD_POINTER_TAGS[2].name, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
        if (!mAttributes[android.media.ExifInterface.IFD_EXIF_HINT].isEmpty()) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.IFD_POINTER_TAGS[0].name, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
        if (!mAttributes[android.media.ExifInterface.IFD_GPS_HINT].isEmpty()) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.IFD_POINTER_TAGS[1].name, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
        }
        if (mHasThumbnail) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name, android.media.ExifInterface.ExifAttribute.createULong(0, mExifByteOrder));
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, android.media.ExifInterface.ExifAttribute.createULong(mThumbnailLength, mExifByteOrder));
        }
        // Calculate IFD group data area sizes. IFD group data area is assigned to save the entry
        // value which has a bigger size than 4 bytes.
        for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
            int sum = 0;
            for (java.util.Map.Entry entry : ((java.util.Set<java.util.Map.Entry>) (mAttributes[i].entrySet()))) {
                final android.media.ExifInterface.ExifAttribute exifAttribute = ((android.media.ExifInterface.ExifAttribute) (entry.getValue()));
                final int size = exifAttribute.size();
                if (size > 4) {
                    sum += size;
                }
            }
            ifdDataSizes[i] += sum;
        }
        // Calculate IFD offsets.
        int position = 8;
        for (int hint = 0; hint < android.media.ExifInterface.EXIF_TAGS.length; ++hint) {
            if (!mAttributes[hint].isEmpty()) {
                ifdOffsets[hint] = position;
                position += ((2 + (mAttributes[hint].size() * 12)) + 4) + ifdDataSizes[hint];
            }
        }
        if (mHasThumbnail) {
            int thumbnailOffset = position;
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name, android.media.ExifInterface.ExifAttribute.createULong(thumbnailOffset, mExifByteOrder));
            mThumbnailOffset = exifOffsetFromBeginning + thumbnailOffset;
            position += mThumbnailLength;
        }
        // Calculate the total size
        int totalSize = position + 8;// eight bytes is for header part.

        if (android.media.ExifInterface.DEBUG) {
            android.util.Log.d(android.media.ExifInterface.TAG, "totalSize length: " + totalSize);
            for (int i = 0; i < android.media.ExifInterface.EXIF_TAGS.length; ++i) {
                android.util.Log.d(android.media.ExifInterface.TAG, java.lang.String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d", i, ifdOffsets[i], mAttributes[i].size(), ifdDataSizes[i]));
            }
        }
        // Update IFD pointer tags with the calculated offsets.
        if (!mAttributes[android.media.ExifInterface.IFD_EXIF_HINT].isEmpty()) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.IFD_POINTER_TAGS[0].name, android.media.ExifInterface.ExifAttribute.createULong(ifdOffsets[android.media.ExifInterface.IFD_EXIF_HINT], mExifByteOrder));
        }
        if (!mAttributes[android.media.ExifInterface.IFD_GPS_HINT].isEmpty()) {
            mAttributes[android.media.ExifInterface.IFD_TIFF_HINT].put(android.media.ExifInterface.IFD_POINTER_TAGS[1].name, android.media.ExifInterface.ExifAttribute.createULong(ifdOffsets[android.media.ExifInterface.IFD_GPS_HINT], mExifByteOrder));
        }
        if (!mAttributes[android.media.ExifInterface.IFD_INTEROPERABILITY_HINT].isEmpty()) {
            mAttributes[android.media.ExifInterface.IFD_EXIF_HINT].put(android.media.ExifInterface.IFD_POINTER_TAGS[2].name, android.media.ExifInterface.ExifAttribute.createULong(ifdOffsets[android.media.ExifInterface.IFD_INTEROPERABILITY_HINT], mExifByteOrder));
        }
        // Write TIFF Headers. See JEITA CP-3451C Table 1. page 10.
        dataOutputStream.writeUnsignedShort(totalSize);
        dataOutputStream.write(android.media.ExifInterface.IDENTIFIER_EXIF_APP1);
        dataOutputStream.writeShort(mExifByteOrder == java.nio.ByteOrder.BIG_ENDIAN ? android.media.ExifInterface.BYTE_ALIGN_MM : android.media.ExifInterface.BYTE_ALIGN_II);
        dataOutputStream.setByteOrder(mExifByteOrder);
        dataOutputStream.writeUnsignedShort(0x2a);
        dataOutputStream.writeUnsignedInt(8);
        // Write IFD groups. See JEITA CP-3451C Figure 7. page 12.
        for (int hint = 0; hint < android.media.ExifInterface.EXIF_TAGS.length; ++hint) {
            if (!mAttributes[hint].isEmpty()) {
                // See JEITA CP-3451C 4.6.2 IFD structure. page 13.
                // Write entry count
                dataOutputStream.writeUnsignedShort(mAttributes[hint].size());
                // Write entry info
                int dataOffset = ((ifdOffsets[hint] + 2) + (mAttributes[hint].size() * 12)) + 4;
                for (java.util.Map.Entry entry : ((java.util.Set<java.util.Map.Entry>) (mAttributes[hint].entrySet()))) {
                    // Convert tag name to tag number.
                    final android.media.ExifInterface.ExifTag tag = ((android.media.ExifInterface.ExifTag) (android.media.ExifInterface.sExifTagMapsForWriting[hint].get(entry.getKey())));
                    final int tagNumber = tag.number;
                    final android.media.ExifInterface.ExifAttribute attribute = ((android.media.ExifInterface.ExifAttribute) (entry.getValue()));
                    final int size = attribute.size();
                    dataOutputStream.writeUnsignedShort(tagNumber);
                    dataOutputStream.writeUnsignedShort(attribute.format);
                    dataOutputStream.writeInt(attribute.numberOfComponents);
                    if (size > 4) {
                        dataOutputStream.writeUnsignedInt(dataOffset);
                        dataOffset += size;
                    } else {
                        dataOutputStream.write(attribute.bytes);
                        // Fill zero up to 4 bytes
                        if (size < 4) {
                            for (int i = size; i < 4; ++i) {
                                dataOutputStream.writeByte(0);
                            }
                        }
                    }
                }
                // Write the next offset. It writes the offset of thumbnail IFD if there is one or
                // more tags in the thumbnail IFD when the current IFD is the primary image TIFF
                // IFD; Otherwise 0.
                if ((hint == 0) && (!mAttributes[android.media.ExifInterface.IFD_THUMBNAIL_HINT].isEmpty())) {
                    dataOutputStream.writeUnsignedInt(ifdOffsets[android.media.ExifInterface.IFD_THUMBNAIL_HINT]);
                } else {
                    dataOutputStream.writeUnsignedInt(0);
                }
                // Write values of data field exceeding 4 bytes after the next offset.
                for (java.util.Map.Entry entry : ((java.util.Set<java.util.Map.Entry>) (mAttributes[hint].entrySet()))) {
                    android.media.ExifInterface.ExifAttribute attribute = ((android.media.ExifInterface.ExifAttribute) (entry.getValue()));
                    if (attribute.bytes.length > 4) {
                        dataOutputStream.write(attribute.bytes, 0, attribute.bytes.length);
                    }
                }
            }
        }
        // Write thumbnail
        if (mHasThumbnail) {
            dataOutputStream.write(getThumbnail());
        }
        // Reset the byte order to big endian in order to write remaining parts of the JPEG file.
        dataOutputStream.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);
        return totalSize;
    }

    /**
     * Determines the data format of EXIF entry value.
     *
     * @param entryValue
     * 		The value to be determined.
     * @return Returns two data formats gussed as a pair in integer. If there is no two candidate
    data formats for the given entry value, returns {@code -1} in the second of the pair.
     */
    private static android.util.Pair<java.lang.Integer, java.lang.Integer> guessDataFormat(java.lang.String entryValue) {
        // See TIFF 6.0 spec Types. page 15.
        // Take the first component if there are more than one component.
        if (entryValue.contains(",")) {
            java.lang.String[] entryValues = entryValue.split(",");
            android.util.Pair<java.lang.Integer, java.lang.Integer> dataFormat = android.media.ExifInterface.guessDataFormat(entryValues[0]);
            if (dataFormat.first == android.media.ExifInterface.IFD_FORMAT_STRING) {
                return dataFormat;
            }
            for (int i = 1; i < entryValues.length; ++i) {
                final android.util.Pair<java.lang.Integer, java.lang.Integer> guessDataFormat = android.media.ExifInterface.guessDataFormat(entryValues[i]);
                int first = -1;
                int second = -1;
                if ((guessDataFormat.first == dataFormat.first) || (guessDataFormat.second == dataFormat.first)) {
                    first = dataFormat.first;
                }
                if ((dataFormat.second != (-1)) && ((guessDataFormat.first == dataFormat.second) || (guessDataFormat.second == dataFormat.second))) {
                    second = dataFormat.second;
                }
                if ((first == (-1)) && (second == (-1))) {
                    return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_STRING, -1);
                }
                if (first == (-1)) {
                    dataFormat = new android.util.Pair<>(second, -1);
                    continue;
                }
                if (second == (-1)) {
                    dataFormat = new android.util.Pair<>(first, -1);
                    continue;
                }
            }
            return dataFormat;
        }
        if (entryValue.contains("/")) {
            java.lang.String[] rationalNumber = entryValue.split("/");
            if (rationalNumber.length == 2) {
                try {
                    long numerator = java.lang.Long.parseLong(rationalNumber[0]);
                    long denominator = java.lang.Long.parseLong(rationalNumber[1]);
                    if ((numerator < 0L) || (denominator < 0L)) {
                        return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_SRATIONAL, -1);
                    }
                    if ((numerator > java.lang.Integer.MAX_VALUE) || (denominator > java.lang.Integer.MAX_VALUE)) {
                        return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_URATIONAL, -1);
                    }
                    return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_SRATIONAL, android.media.ExifInterface.IFD_FORMAT_URATIONAL);
                } catch (java.lang.NumberFormatException e) {
                    // Ignored
                }
            }
            return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_STRING, -1);
        }
        try {
            java.lang.Long longValue = java.lang.Long.parseLong(entryValue);
            if ((longValue >= 0) && (longValue <= 65535)) {
                return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_USHORT, android.media.ExifInterface.IFD_FORMAT_ULONG);
            }
            if (longValue < 0) {
                return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_SLONG, -1);
            }
            return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_ULONG, -1);
        } catch (java.lang.NumberFormatException e) {
            // Ignored
        }
        try {
            java.lang.Double.parseDouble(entryValue);
            return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_DOUBLE, -1);
        } catch (java.lang.NumberFormatException e) {
            // Ignored
        }
        return new android.util.Pair<>(android.media.ExifInterface.IFD_FORMAT_STRING, -1);
    }

    // An input stream to parse EXIF data area, which can be written in either little or big endian
    // order.
    private static class ByteOrderAwarenessDataInputStream extends java.io.ByteArrayInputStream {
        private static final java.nio.ByteOrder LITTLE_ENDIAN = java.nio.ByteOrder.LITTLE_ENDIAN;

        private static final java.nio.ByteOrder BIG_ENDIAN = java.nio.ByteOrder.BIG_ENDIAN;

        private java.nio.ByteOrder mByteOrder = java.nio.ByteOrder.BIG_ENDIAN;

        private final long mLength;

        private long mPosition;

        public ByteOrderAwarenessDataInputStream(byte[] bytes) {
            super(bytes);
            mLength = bytes.length;
            mPosition = 0L;
        }

        public void setByteOrder(java.nio.ByteOrder byteOrder) {
            mByteOrder = byteOrder;
        }

        public void seek(long byteCount) throws java.io.IOException {
            mPosition = 0L;
            reset();
            if (skip(byteCount) != byteCount) {
                throw new java.io.IOException("Couldn't seek up to the byteCount");
            }
        }

        public long peek() {
            return mPosition;
        }

        public void readFully(byte[] buffer) throws java.io.IOException {
            mPosition += buffer.length;
            if (mPosition > mLength) {
                throw new java.io.EOFException();
            }
            if (super.read(buffer, 0, buffer.length) != buffer.length) {
                throw new java.io.IOException("Couldn't read up to the length of buffer");
            }
        }

        public byte readByte() throws java.io.IOException {
            ++mPosition;
            if (mPosition > mLength) {
                throw new java.io.EOFException();
            }
            int ch = super.read();
            if (ch < 0) {
                throw new java.io.EOFException();
            }
            return ((byte) (ch));
        }

        public short readShort() throws java.io.IOException {
            mPosition += 2;
            if (mPosition > mLength) {
                throw new java.io.EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            if ((ch1 | ch2) < 0) {
                throw new java.io.EOFException();
            }
            if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.LITTLE_ENDIAN) {
                return ((short) ((ch2 << 8) + ch1));
            } else
                if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.BIG_ENDIAN) {
                    return ((short) ((ch1 << 8) + ch2));
                }

            throw new java.io.IOException("Invalid byte order: " + mByteOrder);
        }

        public int readInt() throws java.io.IOException {
            mPosition += 4;
            if (mPosition > mLength) {
                throw new java.io.EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            int ch3 = super.read();
            int ch4 = super.read();
            if ((((ch1 | ch2) | ch3) | ch4) < 0) {
                throw new java.io.EOFException();
            }
            if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.LITTLE_ENDIAN) {
                return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + ch1;
            } else
                if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.BIG_ENDIAN) {
                    return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + ch4;
                }

            throw new java.io.IOException("Invalid byte order: " + mByteOrder);
        }

        @java.lang.Override
        public long skip(long byteCount) {
            long skipped = super.skip(java.lang.Math.min(byteCount, mLength - mPosition));
            mPosition += skipped;
            return skipped;
        }

        public int readUnsignedShort() throws java.io.IOException {
            mPosition += 2;
            if (mPosition > mLength) {
                throw new java.io.EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            if ((ch1 | ch2) < 0) {
                throw new java.io.EOFException();
            }
            if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.LITTLE_ENDIAN) {
                return (ch2 << 8) + ch1;
            } else
                if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.BIG_ENDIAN) {
                    return (ch1 << 8) + ch2;
                }

            throw new java.io.IOException("Invalid byte order: " + mByteOrder);
        }

        public long readUnsignedInt() throws java.io.IOException {
            return readInt() & 0xffffffffL;
        }

        public long readLong() throws java.io.IOException {
            mPosition += 8;
            if (mPosition > mLength) {
                throw new java.io.EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            int ch3 = super.read();
            int ch4 = super.read();
            int ch5 = super.read();
            int ch6 = super.read();
            int ch7 = super.read();
            int ch8 = super.read();
            if ((((((((ch1 | ch2) | ch3) | ch4) | ch5) | ch6) | ch7) | ch8) < 0) {
                throw new java.io.EOFException();
            }
            if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.LITTLE_ENDIAN) {
                return (((((((((long) (ch8)) << 56) + (((long) (ch7)) << 48)) + (((long) (ch6)) << 40)) + (((long) (ch5)) << 32)) + (((long) (ch4)) << 24)) + (((long) (ch3)) << 16)) + (((long) (ch2)) << 8)) + ((long) (ch1));
            } else
                if (mByteOrder == android.media.ExifInterface.ByteOrderAwarenessDataInputStream.BIG_ENDIAN) {
                    return (((((((((long) (ch1)) << 56) + (((long) (ch2)) << 48)) + (((long) (ch3)) << 40)) + (((long) (ch4)) << 32)) + (((long) (ch5)) << 24)) + (((long) (ch6)) << 16)) + (((long) (ch7)) << 8)) + ((long) (ch8));
                }

            throw new java.io.IOException("Invalid byte order: " + mByteOrder);
        }

        public float readFloat() throws java.io.IOException {
            return java.lang.Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws java.io.IOException {
            return java.lang.Double.longBitsToDouble(readLong());
        }
    }

    // An output stream to write EXIF data area, which can be written in either little or big endian
    // order.
    private static class ByteOrderAwarenessDataOutputStream extends java.io.FilterOutputStream {
        private final java.io.OutputStream mOutputStream;

        private java.nio.ByteOrder mByteOrder;

        public ByteOrderAwarenessDataOutputStream(java.io.OutputStream out, java.nio.ByteOrder byteOrder) {
            super(out);
            mOutputStream = out;
            mByteOrder = byteOrder;
        }

        public void setByteOrder(java.nio.ByteOrder byteOrder) {
            mByteOrder = byteOrder;
        }

        public void write(byte[] bytes) throws java.io.IOException {
            mOutputStream.write(bytes);
        }

        public void write(byte[] bytes, int offset, int length) throws java.io.IOException {
            mOutputStream.write(bytes, offset, length);
        }

        public void writeByte(int val) throws java.io.IOException {
            mOutputStream.write(val);
        }

        public void writeShort(short val) throws java.io.IOException {
            if (mByteOrder == java.nio.ByteOrder.LITTLE_ENDIAN) {
                mOutputStream.write((val >>> 0) & 0xff);
                mOutputStream.write((val >>> 8) & 0xff);
            } else
                if (mByteOrder == java.nio.ByteOrder.BIG_ENDIAN) {
                    mOutputStream.write((val >>> 8) & 0xff);
                    mOutputStream.write((val >>> 0) & 0xff);
                }

        }

        public void writeInt(int val) throws java.io.IOException {
            if (mByteOrder == java.nio.ByteOrder.LITTLE_ENDIAN) {
                mOutputStream.write((val >>> 0) & 0xff);
                mOutputStream.write((val >>> 8) & 0xff);
                mOutputStream.write((val >>> 16) & 0xff);
                mOutputStream.write((val >>> 24) & 0xff);
            } else
                if (mByteOrder == java.nio.ByteOrder.BIG_ENDIAN) {
                    mOutputStream.write((val >>> 24) & 0xff);
                    mOutputStream.write((val >>> 16) & 0xff);
                    mOutputStream.write((val >>> 8) & 0xff);
                    mOutputStream.write((val >>> 0) & 0xff);
                }

        }

        public void writeUnsignedShort(int val) throws java.io.IOException {
            writeShort(((short) (val)));
        }

        public void writeUnsignedInt(long val) throws java.io.IOException {
            writeInt(((int) (val)));
        }
    }

    // JNI methods for RAW formats.
    private static native void nativeInitRaw();

    private static native byte[] nativeGetThumbnailFromAsset(long asset, int thumbnailOffset, int thumbnailLength);

    private static native java.util.HashMap nativeGetRawAttributesFromAsset(long asset);

    private static native java.util.HashMap nativeGetRawAttributesFromFileDescriptor(java.io.FileDescriptor fd);

    private static native java.util.HashMap nativeGetRawAttributesFromInputStream(java.io.InputStream in);
}

