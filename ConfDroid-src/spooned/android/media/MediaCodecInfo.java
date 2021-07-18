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
package android.media;


/**
 * Provides information about a given media codec available on the device. You can
 * iterate through all codecs available by querying {@link MediaCodecList}. For example,
 * here's how to find an encoder that supports a given MIME type:
 * <pre>
 * private static MediaCodecInfo selectCodec(String mimeType) {
 *     int numCodecs = MediaCodecList.getCodecCount();
 *     for (int i = 0; i &lt; numCodecs; i++) {
 *         MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
 *
 *         if (!codecInfo.isEncoder()) {
 *             continue;
 *         }
 *
 *         String[] types = codecInfo.getSupportedTypes();
 *         for (int j = 0; j &lt; types.length; j++) {
 *             if (types[j].equalsIgnoreCase(mimeType)) {
 *                 return codecInfo;
 *             }
 *         }
 *     }
 *     return null;
 * }</pre>
 */
public final class MediaCodecInfo {
    private boolean mIsEncoder;

    private java.lang.String mName;

    private java.util.Map<java.lang.String, android.media.MediaCodecInfo.CodecCapabilities> mCaps;

    /* package private */
    MediaCodecInfo(java.lang.String name, boolean isEncoder, android.media.MediaCodecInfo.CodecCapabilities[] caps) {
        mName = name;
        mIsEncoder = isEncoder;
        mCaps = new java.util.HashMap<java.lang.String, android.media.MediaCodecInfo.CodecCapabilities>();
        for (android.media.MediaCodecInfo.CodecCapabilities c : caps) {
            mCaps.put(c.getMimeType(), c);
        }
    }

    /**
     * Retrieve the codec name.
     */
    public final java.lang.String getName() {
        return mName;
    }

    /**
     * Query if the codec is an encoder.
     */
    public final boolean isEncoder() {
        return mIsEncoder;
    }

    /**
     * Query the media types supported by the codec.
     */
    public final java.lang.String[] getSupportedTypes() {
        java.util.Set<java.lang.String> typeSet = mCaps.keySet();
        java.lang.String[] types = typeSet.toArray(new java.lang.String[typeSet.size()]);
        java.util.Arrays.sort(types);
        return types;
    }

    private static int checkPowerOfTwo(int value, java.lang.String message) {
        if ((value & (value - 1)) != 0) {
            throw new java.lang.IllegalArgumentException(message);
        }
        return value;
    }

    private static class Feature {
        public java.lang.String mName;

        public int mValue;

        public boolean mDefault;

        public Feature(java.lang.String name, int value, boolean def) {
            mName = name;
            mValue = value;
            mDefault = def;
        }
    }

    // COMMON CONSTANTS
    private static final android.util.Range<java.lang.Integer> POSITIVE_INTEGERS = android.util.Range.create(1, java.lang.Integer.MAX_VALUE);

    private static final android.util.Range<java.lang.Long> POSITIVE_LONGS = android.util.Range.create(1L, java.lang.Long.MAX_VALUE);

    private static final android.util.Range<android.util.Rational> POSITIVE_RATIONALS = android.util.Range.create(new android.util.Rational(1, java.lang.Integer.MAX_VALUE), new android.util.Rational(java.lang.Integer.MAX_VALUE, 1));

    private static final android.util.Range<java.lang.Integer> SIZE_RANGE = android.util.Range.create(1, 32768);

    private static final android.util.Range<java.lang.Integer> FRAME_RATE_RANGE = android.util.Range.create(0, 960);

    private static final android.util.Range<java.lang.Integer> BITRATE_RANGE = android.util.Range.create(0, 500000000);

    private static final int DEFAULT_MAX_SUPPORTED_INSTANCES = 32;

    private static final int MAX_SUPPORTED_INSTANCES_LIMIT = 256;

    // found stuff that is not supported by framework (=> this should not happen)
    private static final int ERROR_UNRECOGNIZED = 1 << 0;

    // found profile/level for which we don't have capability estimates
    private static final int ERROR_UNSUPPORTED = 1 << 1;

    // have not found any profile/level for which we don't have capability estimate
    private static final int ERROR_NONE_SUPPORTED = 1 << 2;

    /**
     * Encapsulates the capabilities of a given codec component.
     * For example, what profile/level combinations it supports and what colorspaces
     * it is capable of providing the decoded data in, as well as some
     * codec-type specific capability flags.
     * <p>You can get an instance for a given {@link MediaCodecInfo} object with
     * {@link MediaCodecInfo#getCapabilitiesForType getCapabilitiesForType()}, passing a MIME type.
     */
    public static final class CodecCapabilities {
        public CodecCapabilities() {
        }

        // CLASSIFICATION
        private java.lang.String mMime;

        private int mMaxSupportedInstances;

        // LEGACY FIELDS
        // Enumerates supported profile/level combinations as defined
        // by the type of encoded data. These combinations impose restrictions
        // on video resolution, bitrate... and limit the available encoder tools
        // such as B-frame support, arithmetic coding...
        public android.media.MediaCodecInfo.CodecProfileLevel[] profileLevels;// NOTE this array is modifiable by user


        // from OMX_COLOR_FORMATTYPE
        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format24bitBGR888}.
         */
        public static final int COLOR_FormatMonochrome = 1;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format24bitBGR888}.
         */
        public static final int COLOR_Format8bitRGB332 = 2;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format24bitBGR888}.
         */
        public static final int COLOR_Format12bitRGB444 = 3;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format16bitARGB4444 = 4;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format16bitARGB1555 = 5;

        /**
         * 16 bits per pixel RGB color format, with 5-bit red & blue and 6-bit green component.
         * <p>
         * Using 16-bit little-endian representation, colors stored as Red 15:11, Green 10:5, Blue 4:0.
         * <pre>
         *            byte                   byte
         *  <--------- i --------> | <------ i + 1 ------>
         * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
         * |     BLUE     |      GREEN      |     RED      |
         * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
         *  0           4  5     7   0     2  3           7
         * bit
         * </pre>
         *
         * This format corresponds to {@link android.graphics.PixelFormat#RGB_565} and
         * {@link android.graphics.ImageFormat#RGB_565}.
         */
        public static final int COLOR_Format16bitRGB565 = 6;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format16bitRGB565}.
         */
        public static final int COLOR_Format16bitBGR565 = 7;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format24bitBGR888}.
         */
        public static final int COLOR_Format18bitRGB666 = 8;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format18bitARGB1665 = 9;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format19bitARGB1666 = 10;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format24bitBGR888} or {@link #COLOR_FormatRGBFlexible}.
         */
        public static final int COLOR_Format24bitRGB888 = 11;

        /**
         * 24 bits per pixel RGB color format, with 8-bit red, green & blue components.
         * <p>
         * Using 24-bit little-endian representation, colors stored as Red 7:0, Green 15:8, Blue 23:16.
         * <pre>
         *         byte              byte             byte
         *  <------ i -----> | <---- i+1 ----> | <---- i+2 ----->
         * +-----------------+-----------------+-----------------+
         * |       RED       |      GREEN      |       BLUE      |
         * +-----------------+-----------------+-----------------+
         * </pre>
         *
         * This format corresponds to {@link android.graphics.PixelFormat#RGB_888}, and can also be
         * represented as a flexible format by {@link #COLOR_FormatRGBFlexible}.
         */
        public static final int COLOR_Format24bitBGR888 = 12;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format24bitARGB1887 = 13;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format25bitARGB1888 = 14;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888} Or {@link #COLOR_FormatRGBAFlexible}.
         */
        public static final int COLOR_Format32bitBGRA8888 = 15;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888} Or {@link #COLOR_FormatRGBAFlexible}.
         */
        public static final int COLOR_Format32bitARGB8888 = 16;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_FormatYUV411Planar = 17;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_FormatYUV411PackedPlanar = 18;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_FormatYUV420Planar = 19;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_FormatYUV420PackedPlanar = 20;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_FormatYUV420SemiPlanar = 21;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatYUV422Planar = 22;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatYUV422PackedPlanar = 23;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatYUV422SemiPlanar = 24;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatYCbYCr = 25;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatYCrYCb = 26;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatCbYCrY = 27;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatCrYCbY = 28;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV444Flexible}.
         */
        public static final int COLOR_FormatYUV444Interleaved = 29;

        /**
         * SMIA 8-bit Bayer format.
         * Each byte represents the top 8-bits of a 10-bit signal.
         */
        public static final int COLOR_FormatRawBayer8bit = 30;

        /**
         * SMIA 10-bit Bayer format.
         */
        public static final int COLOR_FormatRawBayer10bit = 31;

        /**
         * SMIA 8-bit compressed Bayer format.
         * Each byte represents a sample from the 10-bit signal that is compressed into 8-bits
         * using DPCM/PCM compression, as defined by the SMIA Functional Specification.
         */
        public static final int COLOR_FormatRawBayer8bitcompressed = 32;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatL8}.
         */
        public static final int COLOR_FormatL2 = 33;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatL8}.
         */
        public static final int COLOR_FormatL4 = 34;

        /**
         * 8 bits per pixel Y color format.
         * <p>
         * Each byte contains a single pixel.
         * This format corresponds to {@link android.graphics.PixelFormat#L_8}.
         */
        public static final int COLOR_FormatL8 = 35;

        /**
         * 16 bits per pixel, little-endian Y color format.
         * <p>
         * <pre>
         *            byte                   byte
         *  <--------- i --------> | <------ i + 1 ------>
         * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
         * |                       Y                       |
         * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
         *  0                    7   0                    7
         * bit
         * </pre>
         */
        public static final int COLOR_FormatL16 = 36;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatL16}.
         */
        public static final int COLOR_FormatL24 = 37;

        /**
         * 32 bits per pixel, little-endian Y color format.
         * <p>
         * <pre>
         *         byte              byte             byte              byte
         *  <------ i -----> | <---- i+1 ----> | <---- i+2 ----> | <---- i+3 ----->
         * +-----------------+-----------------+-----------------+-----------------+
         * |                                   Y                                   |
         * +-----------------+-----------------+-----------------+-----------------+
         *  0               7 0               7 0               7 0               7
         * bit
         * </pre>
         *
         * @deprecated Use {@link #COLOR_FormatL16}.
         */
        public static final int COLOR_FormatL32 = 38;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_FormatYUV420PackedSemiPlanar = 39;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV422Flexible}.
         */
        public static final int COLOR_FormatYUV422PackedSemiPlanar = 40;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format24bitBGR888}.
         */
        public static final int COLOR_Format18BitBGR666 = 41;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format24BitARGB6666 = 42;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_Format32bitABGR8888}.
         */
        public static final int COLOR_Format24BitABGR6666 = 43;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_TI_FormatYUV420PackedSemiPlanar = 0x7f000100;

        // COLOR_FormatSurface indicates that the data will be a GraphicBuffer metadata reference.
        // In OMX this is called OMX_COLOR_FormatAndroidOpaque.
        public static final int COLOR_FormatSurface = 0x7f000789;

        /**
         * 32 bits per pixel RGBA color format, with 8-bit red, green, blue, and alpha components.
         * <p>
         * Using 32-bit little-endian representation, colors stored as Red 7:0, Green 15:8,
         * Blue 23:16, and Alpha 31:24.
         * <pre>
         *         byte              byte             byte              byte
         *  <------ i -----> | <---- i+1 ----> | <---- i+2 ----> | <---- i+3 ----->
         * +-----------------+-----------------+-----------------+-----------------+
         * |       RED       |      GREEN      |       BLUE      |      ALPHA      |
         * +-----------------+-----------------+-----------------+-----------------+
         * </pre>
         *
         * This corresponds to {@link android.graphics.PixelFormat#RGBA_8888}.
         */
        public static final int COLOR_Format32bitABGR8888 = 0x7f00a000;

        /**
         * Flexible 12 bits per pixel, subsampled YUV color format with 8-bit chroma and luma
         * components.
         * <p>
         * Chroma planes are subsampled by 2 both horizontally and vertically.
         * Use this format with {@link Image}.
         * This format corresponds to {@link android.graphics.ImageFormat#YUV_420_888},
         * and can represent the {@link #COLOR_FormatYUV411Planar},
         * {@link #COLOR_FormatYUV411PackedPlanar}, {@link #COLOR_FormatYUV420Planar},
         * {@link #COLOR_FormatYUV420PackedPlanar}, {@link #COLOR_FormatYUV420SemiPlanar}
         * and {@link #COLOR_FormatYUV420PackedSemiPlanar} formats.
         *
         * @see Image#getFormat
         */
        public static final int COLOR_FormatYUV420Flexible = 0x7f420888;

        /**
         * Flexible 16 bits per pixel, subsampled YUV color format with 8-bit chroma and luma
         * components.
         * <p>
         * Chroma planes are horizontally subsampled by 2. Use this format with {@link Image}.
         * This format corresponds to {@link android.graphics.ImageFormat#YUV_422_888},
         * and can represent the {@link #COLOR_FormatYCbYCr}, {@link #COLOR_FormatYCrYCb},
         * {@link #COLOR_FormatCbYCrY}, {@link #COLOR_FormatCrYCbY},
         * {@link #COLOR_FormatYUV422Planar}, {@link #COLOR_FormatYUV422PackedPlanar},
         * {@link #COLOR_FormatYUV422SemiPlanar} and {@link #COLOR_FormatYUV422PackedSemiPlanar}
         * formats.
         *
         * @see Image#getFormat
         */
        public static final int COLOR_FormatYUV422Flexible = 0x7f422888;

        /**
         * Flexible 24 bits per pixel YUV color format with 8-bit chroma and luma
         * components.
         * <p>
         * Chroma planes are not subsampled. Use this format with {@link Image}.
         * This format corresponds to {@link android.graphics.ImageFormat#YUV_444_888},
         * and can represent the {@link #COLOR_FormatYUV444Interleaved} format.
         *
         * @see Image#getFormat
         */
        public static final int COLOR_FormatYUV444Flexible = 0x7f444888;

        /**
         * Flexible 24 bits per pixel RGB color format with 8-bit red, green and blue
         * components.
         * <p>
         * Use this format with {@link Image}. This format corresponds to
         * {@link android.graphics.ImageFormat#FLEX_RGB_888}, and can represent
         * {@link #COLOR_Format24bitBGR888} and {@link #COLOR_Format24bitRGB888} formats.
         *
         * @see Image#getFormat.
         */
        public static final int COLOR_FormatRGBFlexible = 0x7f36b888;

        /**
         * Flexible 32 bits per pixel RGBA color format with 8-bit red, green, blue, and alpha
         * components.
         * <p>
         * Use this format with {@link Image}. This format corresponds to
         * {@link android.graphics.ImageFormat#FLEX_RGBA_8888}, and can represent
         * {@link #COLOR_Format32bitBGRA8888}, {@link #COLOR_Format32bitABGR8888} and
         * {@link #COLOR_Format32bitARGB8888} formats.
         *
         * @see Image#getFormat
         */
        public static final int COLOR_FormatRGBAFlexible = 0x7f36a888;

        /**
         *
         *
         * @deprecated Use {@link #COLOR_FormatYUV420Flexible}.
         */
        public static final int COLOR_QCOM_FormatYUV420SemiPlanar = 0x7fa30c00;

        /**
         * Defined in the OpenMAX IL specs, color format values are drawn from
         * OMX_COLOR_FORMATTYPE.
         */
        public int[] colorFormats;// NOTE this array is modifiable by user


        // FEATURES
        private int mFlagsSupported;

        private int mFlagsRequired;

        private int mFlagsVerified;

        /**
         * <b>video decoder only</b>: codec supports seamless resolution changes.
         */
        public static final java.lang.String FEATURE_AdaptivePlayback = "adaptive-playback";

        /**
         * <b>video decoder only</b>: codec supports secure decryption.
         */
        public static final java.lang.String FEATURE_SecurePlayback = "secure-playback";

        /**
         * <b>video or audio decoder only</b>: codec supports tunneled playback.
         */
        public static final java.lang.String FEATURE_TunneledPlayback = "tunneled-playback";

        /**
         * <b>video encoder only</b>: codec supports intra refresh.
         */
        public static final java.lang.String FEATURE_IntraRefresh = "intra-refresh";

        /**
         * Query codec feature capabilities.
         * <p>
         * These features are supported to be used by the codec.  These
         * include optional features that can be turned on, as well as
         * features that are always on.
         */
        public final boolean isFeatureSupported(java.lang.String name) {
            return checkFeature(name, mFlagsSupported);
        }

        /**
         * Query codec feature requirements.
         * <p>
         * These features are required to be used by the codec, and as such,
         * they are always turned on.
         */
        public final boolean isFeatureRequired(java.lang.String name) {
            return checkFeature(name, mFlagsRequired);
        }

        private static final android.media.MediaCodecInfo.Feature[] decoderFeatures = new android.media.MediaCodecInfo.Feature[]{ new android.media.MediaCodecInfo.Feature(android.media.MediaCodecInfo.CodecCapabilities.FEATURE_AdaptivePlayback, 1 << 0, true), new android.media.MediaCodecInfo.Feature(android.media.MediaCodecInfo.CodecCapabilities.FEATURE_SecurePlayback, 1 << 1, false), new android.media.MediaCodecInfo.Feature(android.media.MediaCodecInfo.CodecCapabilities.FEATURE_TunneledPlayback, 1 << 2, false) };

        private static final android.media.MediaCodecInfo.Feature[] encoderFeatures = new android.media.MediaCodecInfo.Feature[]{ new android.media.MediaCodecInfo.Feature(android.media.MediaCodecInfo.CodecCapabilities.FEATURE_IntraRefresh, 1 << 0, false) };

        /**
         *
         *
         * @unknown 
         */
        public java.lang.String[] validFeatures() {
            android.media.MediaCodecInfo.Feature[] features = getValidFeatures();
            java.lang.String[] res = new java.lang.String[features.length];
            for (int i = 0; i < res.length; i++) {
                res[i] = features[i].mName;
            }
            return res;
        }

        private android.media.MediaCodecInfo.Feature[] getValidFeatures() {
            if (!isEncoder()) {
                return android.media.MediaCodecInfo.CodecCapabilities.decoderFeatures;
            }
            return android.media.MediaCodecInfo.CodecCapabilities.encoderFeatures;
        }

        private boolean checkFeature(java.lang.String name, int flags) {
            for (android.media.MediaCodecInfo.Feature feat : getValidFeatures()) {
                if (feat.mName.equals(name)) {
                    return (flags & feat.mValue) != 0;
                }
            }
            return false;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isRegular() {
            // regular codecs only require default features
            for (android.media.MediaCodecInfo.Feature feat : getValidFeatures()) {
                if ((!feat.mDefault) && isFeatureRequired(feat.mName)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Query whether codec supports a given {@link MediaFormat}.
         *
         * <p class=note>
         * <strong>Note:</strong> On {@link android.os.Build.VERSION_CODES#LOLLIPOP},
         * {@code format} must not contain a {@linkplain MediaFormat#KEY_FRAME_RATE
         * frame rate}. Use
         * <code class=prettyprint>format.setString(MediaFormat.KEY_FRAME_RATE, null)</code>
         * to clear any existing frame rate setting in the format.
         * <p>
         *
         * The following table summarizes the format keys considered by this method.
         *
         * <table style="width: 0%">
         *  <thead>
         *   <tr>
         *    <th rowspan=3>OS Version(s)</th>
         *    <td colspan=3>{@code MediaFormat} keys considered for</th>
         *   </tr><tr>
         *    <th>Audio Codecs</th>
         *    <th>Video Codecs</th>
         *    <th>Encoders</th>
         *   </tr>
         *  </thead>
         *  <tbody>
         *   <tr>
         *    <td>{@link android.os.Build.VERSION_CODES#LOLLIPOP}</th>
         *    <td rowspan=3>{@link MediaFormat#KEY_MIME}<sup>*</sup>,<br>
         *        {@link MediaFormat#KEY_SAMPLE_RATE},<br>
         *        {@link MediaFormat#KEY_CHANNEL_COUNT},</td>
         *    <td>{@link MediaFormat#KEY_MIME}<sup>*</sup>,<br>
         *        {@link CodecCapabilities#FEATURE_AdaptivePlayback}<sup>D</sup>,<br>
         *        {@link CodecCapabilities#FEATURE_SecurePlayback}<sup>D</sup>,<br>
         *        {@link CodecCapabilities#FEATURE_TunneledPlayback}<sup>D</sup>,<br>
         *        {@link MediaFormat#KEY_WIDTH},<br>
         *        {@link MediaFormat#KEY_HEIGHT},<br>
         *        <strong>no</strong> {@code KEY_FRAME_RATE}</td>
         *    <td rowspan=4>{@link MediaFormat#KEY_BITRATE_MODE},<br>
         *        {@link MediaFormat#KEY_PROFILE}
         *        (and/or {@link MediaFormat#KEY_AAC_PROFILE}<sup>~</sup>),<br>
         *        <!-- {link MediaFormat#KEY_QUALITY},<br> -->
         *        {@link MediaFormat#KEY_COMPLEXITY}
         *        (and/or {@link MediaFormat#KEY_FLAC_COMPRESSION_LEVEL}<sup>~</sup>)</td>
         *   </tr><tr>
         *    <td>{@link android.os.Build.VERSION_CODES#LOLLIPOP_MR1}</th>
         *    <td rowspan=2>as above, plus<br>
         *        {@link MediaFormat#KEY_FRAME_RATE}</td>
         *   </tr><tr>
         *    <td>{@link android.os.Build.VERSION_CODES#M}</th>
         *   </tr><tr>
         *    <td>{@link android.os.Build.VERSION_CODES#N}</th>
         *    <td>as above, plus<br>
         *        {@link MediaFormat#KEY_PROFILE},<br>
         *        <!-- {link MediaFormat#KEY_MAX_BIT_RATE},<br> -->
         *        {@link MediaFormat#KEY_BIT_RATE}</td>
         *    <td>as above, plus<br>
         *        {@link MediaFormat#KEY_PROFILE},<br>
         *        {@link MediaFormat#KEY_LEVEL}<sup>+</sup>,<br>
         *        <!-- {link MediaFormat#KEY_MAX_BIT_RATE},<br> -->
         *        {@link MediaFormat#KEY_BIT_RATE},<br>
         *        {@link CodecCapabilities#FEATURE_IntraRefresh}<sup>E</sup></td>
         *   </tr>
         *   <tr>
         *    <td colspan=4>
         *     <p class=note><strong>Notes:</strong><br>
         *      *: must be specified; otherwise, method returns {@code false}.<br>
         *      +: method does not verify that the format parameters are supported
         *      by the specified level.<br>
         *      D: decoders only<br>
         *      E: encoders only<br>
         *      ~: if both keys are provided values must match
         *    </td>
         *   </tr>
         *  </tbody>
         * </table>
         *
         * @param format
         * 		media format with optional feature directives.
         * @throws IllegalArgumentException
         * 		if format is not a valid media format.
         * @return whether the codec capabilities support the given format
        and feature requests.
         */
        public final boolean isFormatSupported(android.media.MediaFormat format) {
            final java.util.Map<java.lang.String, java.lang.Object> map = format.getMap();
            final java.lang.String mime = ((java.lang.String) (map.get(android.media.MediaFormat.KEY_MIME)));
            // mime must match if present
            if ((mime != null) && (!mMime.equalsIgnoreCase(mime))) {
                return false;
            }
            // check feature support
            for (android.media.MediaCodecInfo.Feature feat : getValidFeatures()) {
                java.lang.Integer yesNo = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_FEATURE_ + feat.mName)));
                if (yesNo == null) {
                    continue;
                }
                if (((yesNo == 1) && (!isFeatureSupported(feat.mName))) || ((yesNo == 0) && isFeatureRequired(feat.mName))) {
                    return false;
                }
            }
            java.lang.Integer profile = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_PROFILE)));
            java.lang.Integer level = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_LEVEL)));
            if (profile != null) {
                if (!supportsProfileLevel(profile, level)) {
                    return false;
                }
                // If we recognize this profile, check that this format is supported by the
                // highest level supported by the codec for that profile. (Ignore specified
                // level beyond the above profile/level check as level is only used as a
                // guidance. E.g. AVC Level 1 CIF format is supported if codec supports level 1.1
                // even though max size for Level 1 is QCIF. However, MPEG2 Simple Profile
                // 1080p format is not supported even if codec supports Main Profile Level High,
                // as Simple Profile does not support 1080p.
                android.media.MediaCodecInfo.CodecCapabilities levelCaps = null;
                int maxLevel = 0;
                for (android.media.MediaCodecInfo.CodecProfileLevel pl : profileLevels) {
                    if ((pl.profile == profile) && (pl.level > maxLevel)) {
                        maxLevel = pl.level;
                    }
                }
                levelCaps = android.media.MediaCodecInfo.CodecCapabilities.createFromProfileLevel(mMime, profile, maxLevel);
                // remove profile from this format otherwise levelCaps.isFormatSupported will
                // get into this same conditon and loop forever.
                java.util.Map<java.lang.String, java.lang.Object> mapWithoutProfile = new java.util.HashMap<>(map);
                mapWithoutProfile.remove(android.media.MediaFormat.KEY_PROFILE);
                android.media.MediaFormat formatWithoutProfile = new android.media.MediaFormat(mapWithoutProfile);
                if ((levelCaps != null) && (!levelCaps.isFormatSupported(formatWithoutProfile))) {
                    return false;
                }
            }
            if ((mAudioCaps != null) && (!mAudioCaps.supportsFormat(format))) {
                return false;
            }
            if ((mVideoCaps != null) && (!mVideoCaps.supportsFormat(format))) {
                return false;
            }
            if ((mEncoderCaps != null) && (!mEncoderCaps.supportsFormat(format))) {
                return false;
            }
            return true;
        }

        private static boolean supportsBitrate(android.util.Range<java.lang.Integer> bitrateRange, android.media.MediaFormat format) {
            java.util.Map<java.lang.String, java.lang.Object> map = format.getMap();
            // consider max bitrate over average bitrate for support
            java.lang.Integer maxBitrate = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_MAX_BIT_RATE)));
            java.lang.Integer bitrate = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_BIT_RATE)));
            if (bitrate == null) {
                bitrate = maxBitrate;
            } else
                if (maxBitrate != null) {
                    bitrate = java.lang.Math.max(bitrate, maxBitrate);
                }

            if ((bitrate != null) && (bitrate > 0)) {
                return bitrateRange.contains(bitrate);
            }
            return true;
        }

        private boolean supportsProfileLevel(int profile, java.lang.Integer level) {
            for (android.media.MediaCodecInfo.CodecProfileLevel pl : profileLevels) {
                if (pl.profile != profile) {
                    continue;
                }
                // AAC does not use levels
                if ((level == null) || mMime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_AAC)) {
                    return true;
                }
                // H.263 levels are not completely ordered:
                // Level45 support only implies Level10 support
                if (mMime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_H263)) {
                    if (((pl.level != level) && (pl.level == android.media.MediaCodecInfo.CodecProfileLevel.H263Level45)) && (level > android.media.MediaCodecInfo.CodecProfileLevel.H263Level10)) {
                        continue;
                    }
                }
                // MPEG4 levels are not completely ordered:
                // Level1 support only implies Level0 (and not Level0b) support
                if (mMime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_MPEG4)) {
                    if (((pl.level != level) && (pl.level == android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level1)) && (level > android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level0)) {
                        continue;
                    }
                }
                // HEVC levels incorporate both tiers and levels. Verify tier support.
                if (mMime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_HEVC)) {
                    boolean supportsHighTier = (pl.level & android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevels) != 0;
                    boolean checkingHighTier = (level & android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevels) != 0;
                    // high tier levels are only supported by other high tier levels
                    if (checkingHighTier && (!supportsHighTier)) {
                        continue;
                    }
                }
                if (pl.level >= level) {
                    // if we recognize the listed profile/level, we must also recognize the
                    // profile/level arguments.
                    if (android.media.MediaCodecInfo.CodecCapabilities.createFromProfileLevel(mMime, profile, pl.level) != null) {
                        return android.media.MediaCodecInfo.CodecCapabilities.createFromProfileLevel(mMime, profile, level) != null;
                    }
                    return true;
                }
            }
            return false;
        }

        // errors while reading profile levels - accessed from sister capabilities
        int mError;

        private static final java.lang.String TAG = "CodecCapabilities";

        // NEW-STYLE CAPABILITIES
        private android.media.MediaCodecInfo.AudioCapabilities mAudioCaps;

        private android.media.MediaCodecInfo.VideoCapabilities mVideoCaps;

        private android.media.MediaCodecInfo.EncoderCapabilities mEncoderCaps;

        private android.media.MediaFormat mDefaultFormat;

        /**
         * Returns a MediaFormat object with default values for configurations that have
         * defaults.
         */
        public android.media.MediaFormat getDefaultFormat() {
            return mDefaultFormat;
        }

        /**
         * Returns the mime type for which this codec-capability object was created.
         */
        public java.lang.String getMimeType() {
            return mMime;
        }

        /**
         * Returns the max number of the supported concurrent codec instances.
         * <p>
         * This is a hint for an upper bound. Applications should not expect to successfully
         * operate more instances than the returned value, but the actual number of
         * concurrently operable instances may be less as it depends on the available
         * resources at time of use.
         */
        public int getMaxSupportedInstances() {
            return mMaxSupportedInstances;
        }

        private boolean isAudio() {
            return mAudioCaps != null;
        }

        /**
         * Returns the audio capabilities or {@code null} if this is not an audio codec.
         */
        public android.media.MediaCodecInfo.AudioCapabilities getAudioCapabilities() {
            return mAudioCaps;
        }

        private boolean isEncoder() {
            return mEncoderCaps != null;
        }

        /**
         * Returns the encoding capabilities or {@code null} if this is not an encoder.
         */
        public android.media.MediaCodecInfo.EncoderCapabilities getEncoderCapabilities() {
            return mEncoderCaps;
        }

        private boolean isVideo() {
            return mVideoCaps != null;
        }

        /**
         * Returns the video capabilities or {@code null} if this is not a video codec.
         */
        public android.media.MediaCodecInfo.VideoCapabilities getVideoCapabilities() {
            return mVideoCaps;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.media.MediaCodecInfo.CodecCapabilities dup() {
            return // clone writable arrays
            new android.media.MediaCodecInfo.CodecCapabilities(java.util.Arrays.copyOf(profileLevels, profileLevels.length), java.util.Arrays.copyOf(colorFormats, colorFormats.length), isEncoder(), mFlagsVerified, mDefaultFormat, mCapabilitiesInfo);
        }

        /**
         * Retrieve the codec capabilities for a certain {@code mime type}, {@code profile} and {@code level}.  If the type, or profile-level combination
         * is not understood by the framework, it returns null.
         * <p class=note> In {@link android.os.Build.VERSION_CODES#M}, calling this
         * method without calling any method of the {@link MediaCodecList} class beforehand
         * results in a {@link NullPointerException}.</p>
         */
        public static android.media.MediaCodecInfo.CodecCapabilities createFromProfileLevel(java.lang.String mime, int profile, int level) {
            android.media.MediaCodecInfo.CodecProfileLevel pl = new android.media.MediaCodecInfo.CodecProfileLevel();
            pl.profile = profile;
            pl.level = level;
            android.media.MediaFormat defaultFormat = new android.media.MediaFormat();
            defaultFormat.setString(android.media.MediaFormat.KEY_MIME, mime);
            android.media.MediaCodecInfo.CodecCapabilities ret = /* encoder */
            /* flags */
            /* info */
            new android.media.MediaCodecInfo.CodecCapabilities(new android.media.MediaCodecInfo.CodecProfileLevel[]{ pl }, new int[0], true, 0, defaultFormat, new android.media.MediaFormat());
            if (ret.mError != 0) {
                return null;
            }
            return ret;
        }

        /* package private */
        CodecCapabilities(android.media.MediaCodecInfo.CodecProfileLevel[] profLevs, int[] colFmts, boolean encoder, int flags, java.util.Map<java.lang.String, java.lang.Object> defaultFormatMap, java.util.Map<java.lang.String, java.lang.Object> capabilitiesMap) {
            this(profLevs, colFmts, encoder, flags, new android.media.MediaFormat(defaultFormatMap), new android.media.MediaFormat(capabilitiesMap));
        }

        private android.media.MediaFormat mCapabilitiesInfo;

        /* package private */
        CodecCapabilities(android.media.MediaCodecInfo.CodecProfileLevel[] profLevs, int[] colFmts, boolean encoder, int flags, android.media.MediaFormat defaultFormat, android.media.MediaFormat info) {
            final java.util.Map<java.lang.String, java.lang.Object> map = info.getMap();
            colorFormats = colFmts;
            mFlagsVerified = flags;
            mDefaultFormat = defaultFormat;
            mCapabilitiesInfo = info;
            mMime = mDefaultFormat.getString(android.media.MediaFormat.KEY_MIME);
            /* VP9 introduced profiles around 2016, so some VP9 codecs may not advertise any
            supported profiles. Determine the level for them using the info they provide.
             */
            if ((profLevs.length == 0) && mMime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_VP9)) {
                android.media.MediaCodecInfo.CodecProfileLevel profLev = new android.media.MediaCodecInfo.CodecProfileLevel();
                profLev.profile = android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile0;
                profLev.level = android.media.MediaCodecInfo.VideoCapabilities.equivalentVP9Level(info);
                profLevs = new android.media.MediaCodecInfo.CodecProfileLevel[]{ profLev };
            }
            profileLevels = profLevs;
            if (mMime.toLowerCase().startsWith("audio/")) {
                mAudioCaps = android.media.MediaCodecInfo.AudioCapabilities.create(info, this);
                mAudioCaps.setDefaultFormat(mDefaultFormat);
            } else
                if (mMime.toLowerCase().startsWith("video/")) {
                    mVideoCaps = android.media.MediaCodecInfo.VideoCapabilities.create(info, this);
                }

            if (encoder) {
                mEncoderCaps = android.media.MediaCodecInfo.EncoderCapabilities.create(info, this);
                mEncoderCaps.setDefaultFormat(mDefaultFormat);
            }
            final java.util.Map<java.lang.String, java.lang.Object> global = android.media.MediaCodecList.getGlobalSettings();
            mMaxSupportedInstances = android.media.Utils.parseIntSafely(global.get("max-concurrent-instances"), android.media.MediaCodecInfo.DEFAULT_MAX_SUPPORTED_INSTANCES);
            int maxInstances = android.media.Utils.parseIntSafely(map.get("max-concurrent-instances"), mMaxSupportedInstances);
            mMaxSupportedInstances = android.util.Range.create(1, android.media.MediaCodecInfo.MAX_SUPPORTED_INSTANCES_LIMIT).clamp(maxInstances);
            for (android.media.MediaCodecInfo.Feature feat : getValidFeatures()) {
                java.lang.String key = android.media.MediaFormat.KEY_FEATURE_ + feat.mName;
                java.lang.Integer yesNo = ((java.lang.Integer) (map.get(key)));
                if (yesNo == null) {
                    continue;
                }
                if (yesNo > 0) {
                    mFlagsRequired |= feat.mValue;
                }
                mFlagsSupported |= feat.mValue;
                mDefaultFormat.setInteger(key, 1);
                // TODO restrict features by mFlagsVerified once all codecs reliably verify them
            }
        }
    }

    /**
     * A class that supports querying the audio capabilities of a codec.
     */
    public static final class AudioCapabilities {
        private static final java.lang.String TAG = "AudioCapabilities";

        private android.media.MediaCodecInfo.CodecCapabilities mParent;

        private android.util.Range<java.lang.Integer> mBitrateRange;

        private int[] mSampleRates;

        private android.util.Range<java.lang.Integer>[] mSampleRateRanges;

        private int mMaxInputChannelCount;

        private static final int MAX_INPUT_CHANNEL_COUNT = 30;

        /**
         * Returns the range of supported bitrates in bits/second.
         */
        public android.util.Range<java.lang.Integer> getBitrateRange() {
            return mBitrateRange;
        }

        /**
         * Returns the array of supported sample rates if the codec
         * supports only discrete values.  Otherwise, it returns
         * {@code null}.  The array is sorted in ascending order.
         */
        public int[] getSupportedSampleRates() {
            return java.util.Arrays.copyOf(mSampleRates, mSampleRates.length);
        }

        /**
         * Returns the array of supported sample rate ranges.  The
         * array is sorted in ascending order, and the ranges are
         * distinct.
         */
        public android.util.Range<java.lang.Integer>[] getSupportedSampleRateRanges() {
            return java.util.Arrays.copyOf(mSampleRateRanges, mSampleRateRanges.length);
        }

        /**
         * Returns the maximum number of input channels supported.  The codec
         * supports any number of channels between 1 and this maximum value.
         */
        public int getMaxInputChannelCount() {
            return mMaxInputChannelCount;
        }

        /* no public constructor */
        private AudioCapabilities() {
        }

        /**
         *
         *
         * @unknown 
         */
        public static android.media.MediaCodecInfo.AudioCapabilities create(android.media.MediaFormat info, android.media.MediaCodecInfo.CodecCapabilities parent) {
            android.media.MediaCodecInfo.AudioCapabilities caps = new android.media.MediaCodecInfo.AudioCapabilities();
            caps.init(info, parent);
            return caps;
        }

        /**
         *
         *
         * @unknown 
         */
        public void init(android.media.MediaFormat info, android.media.MediaCodecInfo.CodecCapabilities parent) {
            mParent = parent;
            initWithPlatformLimits();
            applyLevelLimits();
            parseFromInfo(info);
        }

        private void initWithPlatformLimits() {
            mBitrateRange = android.util.Range.create(0, java.lang.Integer.MAX_VALUE);
            mMaxInputChannelCount = android.media.MediaCodecInfo.AudioCapabilities.MAX_INPUT_CHANNEL_COUNT;
            // mBitrateRange = Range.create(1, 320000);
            mSampleRateRanges = new android.util.Range[]{ android.util.Range.create(8000, 96000) };
            mSampleRates = null;
        }

        private boolean supports(java.lang.Integer sampleRate, java.lang.Integer inputChannels) {
            // channels and sample rates are checked orthogonally
            if ((inputChannels != null) && ((inputChannels < 1) || (inputChannels > mMaxInputChannelCount))) {
                return false;
            }
            if (sampleRate != null) {
                int ix = android.media.Utils.binarySearchDistinctRanges(mSampleRateRanges, sampleRate);
                if (ix < 0) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Query whether the sample rate is supported by the codec.
         */
        public boolean isSampleRateSupported(int sampleRate) {
            return supports(sampleRate, null);
        }

        /**
         * modifies rates
         */
        private void limitSampleRates(int[] rates) {
            java.util.Arrays.sort(rates);
            java.util.ArrayList<android.util.Range<java.lang.Integer>> ranges = new java.util.ArrayList<android.util.Range<java.lang.Integer>>();
            for (int rate : rates) {
                if (/* channels */
                supports(rate, null)) {
                    ranges.add(android.util.Range.create(rate, rate));
                }
            }
            mSampleRateRanges = ranges.toArray(new android.util.Range[ranges.size()]);
            createDiscreteSampleRates();
        }

        private void createDiscreteSampleRates() {
            mSampleRates = new int[mSampleRateRanges.length];
            for (int i = 0; i < mSampleRateRanges.length; i++) {
                mSampleRates[i] = mSampleRateRanges[i].getLower();
            }
        }

        /**
         * modifies rateRanges
         */
        private void limitSampleRates(android.util.Range<java.lang.Integer>[] rateRanges) {
            android.media.Utils.sortDistinctRanges(rateRanges);
            mSampleRateRanges = android.media.Utils.intersectSortedDistinctRanges(mSampleRateRanges, rateRanges);
            // check if all values are discrete
            for (android.util.Range<java.lang.Integer> range : mSampleRateRanges) {
                if (!range.getLower().equals(range.getUpper())) {
                    mSampleRates = null;
                    return;
                }
            }
            createDiscreteSampleRates();
        }

        private void applyLevelLimits() {
            int[] sampleRates = null;
            android.util.Range<java.lang.Integer> sampleRateRange = null;
            android.util.Range<java.lang.Integer> bitRates = null;
            int maxChannels = 0;
            java.lang.String mime = mParent.getMimeType();
            if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_MPEG)) {
                sampleRates = new int[]{ 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000 };
                bitRates = android.util.Range.create(8000, 320000);
                maxChannels = 2;
            } else
                if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_AMR_NB)) {
                    sampleRates = new int[]{ 8000 };
                    bitRates = android.util.Range.create(4750, 12200);
                    maxChannels = 1;
                } else
                    if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_AMR_WB)) {
                        sampleRates = new int[]{ 16000 };
                        bitRates = android.util.Range.create(6600, 23850);
                        maxChannels = 1;
                    } else
                        if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_AAC)) {
                            sampleRates = new int[]{ 7350, 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000, 64000, 88200, 96000 };
                            bitRates = android.util.Range.create(8000, 510000);
                            maxChannels = 48;
                        } else
                            if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_VORBIS)) {
                                bitRates = android.util.Range.create(32000, 500000);
                                sampleRateRange = android.util.Range.create(8000, 192000);
                                maxChannels = 255;
                            } else
                                if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_OPUS)) {
                                    bitRates = android.util.Range.create(6000, 510000);
                                    sampleRates = new int[]{ 8000, 12000, 16000, 24000, 48000 };
                                    maxChannels = 255;
                                } else
                                    if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_RAW)) {
                                        sampleRateRange = android.util.Range.create(1, 96000);
                                        bitRates = android.util.Range.create(1, 10000000);
                                        maxChannels = android.media.AudioTrack.CHANNEL_COUNT_MAX;
                                    } else
                                        if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_FLAC)) {
                                            sampleRateRange = android.util.Range.create(1, 655350);
                                            // lossless codec, so bitrate is ignored
                                            maxChannels = 255;
                                        } else
                                            if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_G711_ALAW) || mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_G711_MLAW)) {
                                                sampleRates = new int[]{ 8000 };
                                                bitRates = android.util.Range.create(64000, 64000);
                                                // platform allows multiple channels for this format
                                            } else
                                                if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_MSGSM)) {
                                                    sampleRates = new int[]{ 8000 };
                                                    bitRates = android.util.Range.create(13000, 13000);
                                                    maxChannels = 1;
                                                } else {
                                                    android.util.Log.w(android.media.MediaCodecInfo.AudioCapabilities.TAG, "Unsupported mime " + mime);
                                                    mParent.mError |= android.media.MediaCodecInfo.ERROR_UNSUPPORTED;
                                                }









            // restrict ranges
            if (sampleRates != null) {
                limitSampleRates(sampleRates);
            } else
                if (sampleRateRange != null) {
                    limitSampleRates(new android.util.Range[]{ sampleRateRange });
                }

            applyLimits(maxChannels, bitRates);
        }

        private void applyLimits(int maxInputChannels, android.util.Range<java.lang.Integer> bitRates) {
            mMaxInputChannelCount = android.util.Range.create(1, mMaxInputChannelCount).clamp(maxInputChannels);
            if (bitRates != null) {
                mBitrateRange = mBitrateRange.intersect(bitRates);
            }
        }

        private void parseFromInfo(android.media.MediaFormat info) {
            int maxInputChannels = android.media.MediaCodecInfo.AudioCapabilities.MAX_INPUT_CHANNEL_COUNT;
            android.util.Range<java.lang.Integer> bitRates = android.media.MediaCodecInfo.POSITIVE_INTEGERS;
            if (info.containsKey("sample-rate-ranges")) {
                java.lang.String[] rateStrings = info.getString("sample-rate-ranges").split(",");
                android.util.Range<java.lang.Integer>[] rateRanges = new android.util.Range[rateStrings.length];
                for (int i = 0; i < rateStrings.length; i++) {
                    rateRanges[i] = android.media.Utils.parseIntRange(rateStrings[i], null);
                }
                limitSampleRates(rateRanges);
            }
            if (info.containsKey("max-channel-count")) {
                maxInputChannels = android.media.Utils.parseIntSafely(info.getString("max-channel-count"), maxInputChannels);
            }
            if (info.containsKey("bitrate-range")) {
                bitRates = bitRates.intersect(android.media.Utils.parseIntRange(info.getString("bitrate-range"), bitRates));
            }
            applyLimits(maxInputChannels, bitRates);
        }

        /**
         *
         *
         * @unknown 
         */
        public void setDefaultFormat(android.media.MediaFormat format) {
            // report settings that have only a single choice
            if (mBitrateRange.getLower().equals(mBitrateRange.getUpper())) {
                format.setInteger(android.media.MediaFormat.KEY_BIT_RATE, mBitrateRange.getLower());
            }
            if (mMaxInputChannelCount == 1) {
                // mono-only format
                format.setInteger(android.media.MediaFormat.KEY_CHANNEL_COUNT, 1);
            }
            if ((mSampleRates != null) && (mSampleRates.length == 1)) {
                format.setInteger(android.media.MediaFormat.KEY_SAMPLE_RATE, mSampleRates[0]);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean supportsFormat(android.media.MediaFormat format) {
            java.util.Map<java.lang.String, java.lang.Object> map = format.getMap();
            java.lang.Integer sampleRate = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_SAMPLE_RATE)));
            java.lang.Integer channels = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_CHANNEL_COUNT)));
            if (!supports(sampleRate, channels)) {
                return false;
            }
            if (!android.media.MediaCodecInfo.CodecCapabilities.supportsBitrate(mBitrateRange, format)) {
                return false;
            }
            // nothing to do for:
            // KEY_CHANNEL_MASK: codecs don't get this
            // KEY_IS_ADTS:      required feature for all AAC decoders
            return true;
        }
    }

    /**
     * A class that supports querying the video capabilities of a codec.
     */
    public static final class VideoCapabilities {
        private static final java.lang.String TAG = "VideoCapabilities";

        private android.media.MediaCodecInfo.CodecCapabilities mParent;

        private android.util.Range<java.lang.Integer> mBitrateRange;

        private android.util.Range<java.lang.Integer> mHeightRange;

        private android.util.Range<java.lang.Integer> mWidthRange;

        private android.util.Range<java.lang.Integer> mBlockCountRange;

        private android.util.Range<java.lang.Integer> mHorizontalBlockRange;

        private android.util.Range<java.lang.Integer> mVerticalBlockRange;

        private android.util.Range<android.util.Rational> mAspectRatioRange;

        private android.util.Range<android.util.Rational> mBlockAspectRatioRange;

        private android.util.Range<java.lang.Long> mBlocksPerSecondRange;

        private java.util.Map<android.util.Size, android.util.Range<java.lang.Long>> mMeasuredFrameRates;

        private android.util.Range<java.lang.Integer> mFrameRateRange;

        private int mBlockWidth;

        private int mBlockHeight;

        private int mWidthAlignment;

        private int mHeightAlignment;

        private int mSmallerDimensionUpperLimit;

        private boolean mAllowMbOverride;// allow XML to override calculated limits


        /**
         * Returns the range of supported bitrates in bits/second.
         */
        public android.util.Range<java.lang.Integer> getBitrateRange() {
            return mBitrateRange;
        }

        /**
         * Returns the range of supported video widths.
         */
        public android.util.Range<java.lang.Integer> getSupportedWidths() {
            return mWidthRange;
        }

        /**
         * Returns the range of supported video heights.
         */
        public android.util.Range<java.lang.Integer> getSupportedHeights() {
            return mHeightRange;
        }

        /**
         * Returns the alignment requirement for video width (in pixels).
         *
         * This is a power-of-2 value that video width must be a
         * multiple of.
         */
        public int getWidthAlignment() {
            return mWidthAlignment;
        }

        /**
         * Returns the alignment requirement for video height (in pixels).
         *
         * This is a power-of-2 value that video height must be a
         * multiple of.
         */
        public int getHeightAlignment() {
            return mHeightAlignment;
        }

        /**
         * Return the upper limit on the smaller dimension of width or height.
         * <p></p>
         * Some codecs have a limit on the smaller dimension, whether it be
         * the width or the height.  E.g. a codec may only be able to handle
         * up to 1920x1080 both in landscape and portrait mode (1080x1920).
         * In this case the maximum width and height are both 1920, but the
         * smaller dimension limit will be 1080. For other codecs, this is
         * {@code Math.min(getSupportedWidths().getUpper(),
         * getSupportedHeights().getUpper())}.
         *
         * @unknown 
         */
        public int getSmallerDimensionUpperLimit() {
            return mSmallerDimensionUpperLimit;
        }

        /**
         * Returns the range of supported frame rates.
         * <p>
         * This is not a performance indicator.  Rather, it expresses the
         * limits specified in the coding standard, based on the complexities
         * of encoding material for later playback at a certain frame rate,
         * or the decoding of such material in non-realtime.
         */
        public android.util.Range<java.lang.Integer> getSupportedFrameRates() {
            return mFrameRateRange;
        }

        /**
         * Returns the range of supported video widths for a video height.
         *
         * @param height
         * 		the height of the video
         */
        public android.util.Range<java.lang.Integer> getSupportedWidthsFor(int height) {
            try {
                android.util.Range<java.lang.Integer> range = mWidthRange;
                if ((!mHeightRange.contains(height)) || ((height % mHeightAlignment) != 0)) {
                    throw new java.lang.IllegalArgumentException("unsupported height");
                }
                final int heightInBlocks = android.media.Utils.divUp(height, mBlockHeight);
                // constrain by block count and by block aspect ratio
                final int minWidthInBlocks = java.lang.Math.max(android.media.Utils.divUp(mBlockCountRange.getLower(), heightInBlocks), ((int) (java.lang.Math.ceil(mBlockAspectRatioRange.getLower().doubleValue() * heightInBlocks))));
                final int maxWidthInBlocks = java.lang.Math.min(mBlockCountRange.getUpper() / heightInBlocks, ((int) (mBlockAspectRatioRange.getUpper().doubleValue() * heightInBlocks)));
                range = range.intersect(((minWidthInBlocks - 1) * mBlockWidth) + mWidthAlignment, maxWidthInBlocks * mBlockWidth);
                // constrain by smaller dimension limit
                if (height > mSmallerDimensionUpperLimit) {
                    range = range.intersect(1, mSmallerDimensionUpperLimit);
                }
                // constrain by aspect ratio
                range = range.intersect(((int) (java.lang.Math.ceil(mAspectRatioRange.getLower().doubleValue() * height))), ((int) (mAspectRatioRange.getUpper().doubleValue() * height)));
                return range;
            } catch (java.lang.IllegalArgumentException e) {
                // height is not supported because there are no suitable widths
                android.util.Log.v(android.media.MediaCodecInfo.VideoCapabilities.TAG, "could not get supported widths for " + height);
                throw new java.lang.IllegalArgumentException("unsupported height");
            }
        }

        /**
         * Returns the range of supported video heights for a video width
         *
         * @param width
         * 		the width of the video
         */
        public android.util.Range<java.lang.Integer> getSupportedHeightsFor(int width) {
            try {
                android.util.Range<java.lang.Integer> range = mHeightRange;
                if ((!mWidthRange.contains(width)) || ((width % mWidthAlignment) != 0)) {
                    throw new java.lang.IllegalArgumentException("unsupported width");
                }
                final int widthInBlocks = android.media.Utils.divUp(width, mBlockWidth);
                // constrain by block count and by block aspect ratio
                final int minHeightInBlocks = java.lang.Math.max(android.media.Utils.divUp(mBlockCountRange.getLower(), widthInBlocks), ((int) (java.lang.Math.ceil(widthInBlocks / mBlockAspectRatioRange.getUpper().doubleValue()))));
                final int maxHeightInBlocks = java.lang.Math.min(mBlockCountRange.getUpper() / widthInBlocks, ((int) (widthInBlocks / mBlockAspectRatioRange.getLower().doubleValue())));
                range = range.intersect(((minHeightInBlocks - 1) * mBlockHeight) + mHeightAlignment, maxHeightInBlocks * mBlockHeight);
                // constrain by smaller dimension limit
                if (width > mSmallerDimensionUpperLimit) {
                    range = range.intersect(1, mSmallerDimensionUpperLimit);
                }
                // constrain by aspect ratio
                range = range.intersect(((int) (java.lang.Math.ceil(width / mAspectRatioRange.getUpper().doubleValue()))), ((int) (width / mAspectRatioRange.getLower().doubleValue())));
                return range;
            } catch (java.lang.IllegalArgumentException e) {
                // width is not supported because there are no suitable heights
                android.util.Log.v(android.media.MediaCodecInfo.VideoCapabilities.TAG, "could not get supported heights for " + width);
                throw new java.lang.IllegalArgumentException("unsupported width");
            }
        }

        /**
         * Returns the range of supported video frame rates for a video size.
         * <p>
         * This is not a performance indicator.  Rather, it expresses the limits specified in
         * the coding standard, based on the complexities of encoding material of a given
         * size for later playback at a certain frame rate, or the decoding of such material
         * in non-realtime.
         *
         * @param width
         * 		the width of the video
         * @param height
         * 		the height of the video
         */
        public android.util.Range<java.lang.Double> getSupportedFrameRatesFor(int width, int height) {
            android.util.Range<java.lang.Integer> range = mHeightRange;
            if (!supports(width, height, null)) {
                throw new java.lang.IllegalArgumentException("unsupported size");
            }
            final int blockCount = android.media.Utils.divUp(width, mBlockWidth) * android.media.Utils.divUp(height, mBlockHeight);
            return android.util.Range.create(java.lang.Math.max(mBlocksPerSecondRange.getLower() / ((double) (blockCount)), ((double) (mFrameRateRange.getLower()))), java.lang.Math.min(mBlocksPerSecondRange.getUpper() / ((double) (blockCount)), ((double) (mFrameRateRange.getUpper()))));
        }

        private int getBlockCount(int width, int height) {
            return android.media.Utils.divUp(width, mBlockWidth) * android.media.Utils.divUp(height, mBlockHeight);
        }

        @android.annotation.NonNull
        private android.util.Size findClosestSize(int width, int height) {
            int targetBlockCount = getBlockCount(width, height);
            android.util.Size closestSize = null;
            int minDiff = java.lang.Integer.MAX_VALUE;
            for (android.util.Size size : mMeasuredFrameRates.keySet()) {
                int diff = java.lang.Math.abs(targetBlockCount - getBlockCount(size.getWidth(), size.getHeight()));
                if (diff < minDiff) {
                    minDiff = diff;
                    closestSize = size;
                }
            }
            return closestSize;
        }

        private android.util.Range<java.lang.Double> estimateFrameRatesFor(int width, int height) {
            android.util.Size size = findClosestSize(width, height);
            android.util.Range<java.lang.Long> range = mMeasuredFrameRates.get(size);
            java.lang.Double ratio = getBlockCount(size.getWidth(), size.getHeight()) / ((double) (java.lang.Math.max(getBlockCount(width, height), 1)));
            return android.util.Range.create(range.getLower() * ratio, range.getUpper() * ratio);
        }

        /**
         * Returns the range of achievable video frame rates for a video size.
         * May return {@code null}, if the codec did not publish any measurement
         * data.
         * <p>
         * This is a performance estimate provided by the device manufacturer based on statistical
         * sampling of full-speed decoding and encoding measurements in various configurations
         * of common video sizes supported by the codec. As such it should only be used to
         * compare individual codecs on the device. The value is not suitable for comparing
         * different devices or even different android releases for the same device.
         * <p>
         * <em>On {@link android.os.Build.VERSION_CODES#M} release</em> the returned range
         * corresponds to the fastest frame rates achieved in the tested configurations. As
         * such, it should not be used to gauge guaranteed or even average codec performance
         * on the device.
         * <p>
         * <em>On {@link android.os.Build.VERSION_CODES#N} release</em> the returned range
         * corresponds closer to sustained performance <em>in tested configurations</em>.
         * One can expect to achieve sustained performance higher than the lower limit more than
         * 50% of the time, and higher than half of the lower limit at least 90% of the time
         * <em>in tested configurations</em>.
         * Conversely, one can expect performance lower than twice the upper limit at least
         * 90% of the time.
         * <p class=note>
         * Tested configurations use a single active codec. For use cases where multiple
         * codecs are active, applications can expect lower and in most cases significantly lower
         * performance.
         * <p class=note>
         * The returned range value is interpolated from the nearest frame size(s) tested.
         * Codec performance is severely impacted by other activity on the device as well
         * as environmental factors (such as battery level, temperature or power source), and can
         * vary significantly even in a steady environment.
         * <p class=note>
         * Use this method in cases where only codec performance matters, e.g. to evaluate if
         * a codec has any chance of meeting a performance target. Codecs are listed
         * in {@link MediaCodecList} in the preferred order as defined by the device
         * manufacturer. As such, applications should use the first suitable codec in the
         * list to achieve the best balance between power use and performance.
         *
         * @param width
         * 		the width of the video
         * @param height
         * 		the height of the video
         * @throws IllegalArgumentException
         * 		if the video size is not supported.
         */
        @android.annotation.Nullable
        public android.util.Range<java.lang.Double> getAchievableFrameRatesFor(int width, int height) {
            if (!supports(width, height, null)) {
                throw new java.lang.IllegalArgumentException("unsupported size");
            }
            if ((mMeasuredFrameRates == null) || (mMeasuredFrameRates.size() <= 0)) {
                android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, "Codec did not publish any measurement data.");
                return null;
            }
            return estimateFrameRatesFor(width, height);
        }

        /**
         * Returns whether a given video size ({@code width} and
         * {@code height}) and {@code frameRate} combination is supported.
         */
        public boolean areSizeAndRateSupported(int width, int height, double frameRate) {
            return supports(width, height, frameRate);
        }

        /**
         * Returns whether a given video size ({@code width} and
         * {@code height}) is supported.
         */
        public boolean isSizeSupported(int width, int height) {
            return supports(width, height, null);
        }

        private boolean supports(java.lang.Integer width, java.lang.Integer height, java.lang.Number rate) {
            boolean ok = true;
            if (ok && (width != null)) {
                ok = mWidthRange.contains(width) && ((width % mWidthAlignment) == 0);
            }
            if (ok && (height != null)) {
                ok = mHeightRange.contains(height) && ((height % mHeightAlignment) == 0);
            }
            if (ok && (rate != null)) {
                ok = mFrameRateRange.contains(android.media.Utils.intRangeFor(rate.doubleValue()));
            }
            if ((ok && (height != null)) && (width != null)) {
                ok = java.lang.Math.min(height, width) <= mSmallerDimensionUpperLimit;
                final int widthInBlocks = android.media.Utils.divUp(width, mBlockWidth);
                final int heightInBlocks = android.media.Utils.divUp(height, mBlockHeight);
                final int blockCount = widthInBlocks * heightInBlocks;
                ok = ((ok && mBlockCountRange.contains(blockCount)) && mBlockAspectRatioRange.contains(new android.util.Rational(widthInBlocks, heightInBlocks))) && mAspectRatioRange.contains(new android.util.Rational(width, height));
                if (ok && (rate != null)) {
                    double blocksPerSec = blockCount * rate.doubleValue();
                    ok = mBlocksPerSecondRange.contains(android.media.Utils.longRangeFor(blocksPerSec));
                }
            }
            return ok;
        }

        /**
         *
         *
         * @unknown 
         * @throws java.lang.ClassCastException
         * 		
         */
        public boolean supportsFormat(android.media.MediaFormat format) {
            final java.util.Map<java.lang.String, java.lang.Object> map = format.getMap();
            java.lang.Integer width = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_WIDTH)));
            java.lang.Integer height = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_HEIGHT)));
            java.lang.Number rate = ((java.lang.Number) (map.get(android.media.MediaFormat.KEY_FRAME_RATE)));
            if (!supports(width, height, rate)) {
                return false;
            }
            if (!android.media.MediaCodecInfo.CodecCapabilities.supportsBitrate(mBitrateRange, format)) {
                return false;
            }
            // we ignore color-format for now as it is not reliably reported by codec
            return true;
        }

        /* no public constructor */
        private VideoCapabilities() {
        }

        /**
         *
         *
         * @unknown 
         */
        public static android.media.MediaCodecInfo.VideoCapabilities create(android.media.MediaFormat info, android.media.MediaCodecInfo.CodecCapabilities parent) {
            android.media.MediaCodecInfo.VideoCapabilities caps = new android.media.MediaCodecInfo.VideoCapabilities();
            caps.init(info, parent);
            return caps;
        }

        /**
         *
         *
         * @unknown 
         */
        public void init(android.media.MediaFormat info, android.media.MediaCodecInfo.CodecCapabilities parent) {
            mParent = parent;
            initWithPlatformLimits();
            applyLevelLimits();
            parseFromInfo(info);
            updateLimits();
        }

        /**
         *
         *
         * @unknown 
         */
        public android.util.Size getBlockSize() {
            return new android.util.Size(mBlockWidth, mBlockHeight);
        }

        /**
         *
         *
         * @unknown 
         */
        public android.util.Range<java.lang.Integer> getBlockCountRange() {
            return mBlockCountRange;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.util.Range<java.lang.Long> getBlocksPerSecondRange() {
            return mBlocksPerSecondRange;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.util.Range<android.util.Rational> getAspectRatioRange(boolean blocks) {
            return blocks ? mBlockAspectRatioRange : mAspectRatioRange;
        }

        private void initWithPlatformLimits() {
            mBitrateRange = android.media.MediaCodecInfo.BITRATE_RANGE;
            mWidthRange = android.media.MediaCodecInfo.SIZE_RANGE;
            mHeightRange = android.media.MediaCodecInfo.SIZE_RANGE;
            mFrameRateRange = android.media.MediaCodecInfo.FRAME_RATE_RANGE;
            mHorizontalBlockRange = android.media.MediaCodecInfo.SIZE_RANGE;
            mVerticalBlockRange = android.media.MediaCodecInfo.SIZE_RANGE;
            // full positive ranges are supported as these get calculated
            mBlockCountRange = android.media.MediaCodecInfo.POSITIVE_INTEGERS;
            mBlocksPerSecondRange = android.media.MediaCodecInfo.POSITIVE_LONGS;
            mBlockAspectRatioRange = android.media.MediaCodecInfo.POSITIVE_RATIONALS;
            mAspectRatioRange = android.media.MediaCodecInfo.POSITIVE_RATIONALS;
            // YUV 4:2:0 requires 2:2 alignment
            mWidthAlignment = 2;
            mHeightAlignment = 2;
            mBlockWidth = 2;
            mBlockHeight = 2;
            mSmallerDimensionUpperLimit = android.media.MediaCodecInfo.SIZE_RANGE.getUpper();
        }

        private java.util.Map<android.util.Size, android.util.Range<java.lang.Long>> getMeasuredFrameRates(java.util.Map<java.lang.String, java.lang.Object> map) {
            java.util.Map<android.util.Size, android.util.Range<java.lang.Long>> ret = new java.util.HashMap<android.util.Size, android.util.Range<java.lang.Long>>();
            final java.lang.String prefix = "measured-frame-rate-";
            java.util.Set<java.lang.String> keys = map.keySet();
            for (java.lang.String key : keys) {
                // looking for: measured-frame-rate-WIDTHxHEIGHT-range
                if (!key.startsWith(prefix)) {
                    continue;
                }
                java.lang.String subKey = key.substring(prefix.length());
                java.lang.String[] temp = key.split("-");
                if (temp.length != 5) {
                    continue;
                }
                java.lang.String sizeStr = temp[3];
                android.util.Size size = android.media.Utils.parseSize(sizeStr, null);
                if ((size == null) || ((size.getWidth() * size.getHeight()) <= 0)) {
                    continue;
                }
                android.util.Range<java.lang.Long> range = android.media.Utils.parseLongRange(map.get(key), null);
                if (((range == null) || (range.getLower() < 0)) || (range.getUpper() < 0)) {
                    continue;
                }
                ret.put(size, range);
            }
            return ret;
        }

        private static android.util.Pair<android.util.Range<java.lang.Integer>, android.util.Range<java.lang.Integer>> parseWidthHeightRanges(java.lang.Object o) {
            android.util.Pair<android.util.Size, android.util.Size> range = android.media.Utils.parseSizeRange(o);
            if (range != null) {
                try {
                    return android.util.Pair.create(android.util.Range.create(range.first.getWidth(), range.second.getWidth()), android.util.Range.create(range.first.getHeight(), range.second.getHeight()));
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, ("could not parse size range '" + o) + "'");
                }
            }
            return null;
        }

        /**
         *
         *
         * @unknown 
         */
        public static int equivalentVP9Level(android.media.MediaFormat info) {
            final java.util.Map<java.lang.String, java.lang.Object> map = info.getMap();
            android.util.Size blockSize = android.media.Utils.parseSize(map.get("block-size"), new android.util.Size(8, 8));
            int BS = blockSize.getWidth() * blockSize.getHeight();
            android.util.Range<java.lang.Integer> counts = android.media.Utils.parseIntRange(map.get("block-count-range"), null);
            int FS = (counts == null) ? 0 : BS * counts.getUpper();
            android.util.Range<java.lang.Long> blockRates = android.media.Utils.parseLongRange(map.get("blocks-per-second-range"), null);
            long SR = (blockRates == null) ? 0 : BS * blockRates.getUpper();
            android.util.Pair<android.util.Range<java.lang.Integer>, android.util.Range<java.lang.Integer>> dimensionRanges = android.media.MediaCodecInfo.VideoCapabilities.parseWidthHeightRanges(map.get("size-range"));
            int D = (dimensionRanges == null) ? 0 : java.lang.Math.max(dimensionRanges.first.getUpper(), dimensionRanges.second.getUpper());
            android.util.Range<java.lang.Integer> bitRates = android.media.Utils.parseIntRange(map.get("bitrate-range"), null);
            int BR = (bitRates == null) ? 0 : android.media.Utils.divUp(bitRates.getUpper(), 1000);
            if ((((SR <= 829440) && (FS <= 36864)) && (BR <= 200)) && (D <= 512))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level1;

            if ((((SR <= 2764800) && (FS <= 73728)) && (BR <= 800)) && (D <= 768))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level11;

            if ((((SR <= 4608000) && (FS <= 122880)) && (BR <= 1800)) && (D <= 960))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level2;

            if ((((SR <= 9216000) && (FS <= 245760)) && (BR <= 3600)) && (D <= 1344))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level21;

            if ((((SR <= 20736000) && (FS <= 552960)) && (BR <= 7200)) && (D <= 2048))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level3;

            if ((((SR <= 36864000) && (FS <= 983040)) && (BR <= 12000)) && (D <= 2752))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level31;

            if ((((SR <= 83558400) && (FS <= 2228224)) && (BR <= 18000)) && (D <= 4160))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level4;

            if ((((SR <= 160432128) && (FS <= 2228224)) && (BR <= 30000)) && (D <= 4160))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level41;

            if ((((SR <= 311951360) && (FS <= 8912896)) && (BR <= 60000)) && (D <= 8384))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level5;

            if ((((SR <= 588251136) && (FS <= 8912896)) && (BR <= 120000)) && (D <= 8384))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level51;

            if ((((SR <= 1176502272) && (FS <= 8912896)) && (BR <= 180000)) && (D <= 8384))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level52;

            if ((((SR <= 1176502272) && (FS <= 35651584)) && (BR <= 180000)) && (D <= 16832))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level6;

            if ((((SR <= 2353004544L) && (FS <= 35651584)) && (BR <= 240000)) && (D <= 16832))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level61;

            if ((((SR <= 4706009088L) && (FS <= 35651584)) && (BR <= 480000)) && (D <= 16832))
                return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level62;

            // returning largest level
            return android.media.MediaCodecInfo.CodecProfileLevel.VP9Level62;
        }

        private void parseFromInfo(android.media.MediaFormat info) {
            final java.util.Map<java.lang.String, java.lang.Object> map = info.getMap();
            android.util.Size blockSize = new android.util.Size(mBlockWidth, mBlockHeight);
            android.util.Size alignment = new android.util.Size(mWidthAlignment, mHeightAlignment);
            android.util.Range<java.lang.Integer> counts = null;
            android.util.Range<java.lang.Integer> widths = null;
            android.util.Range<java.lang.Integer> heights = null;
            android.util.Range<java.lang.Integer> frameRates = null;
            android.util.Range<java.lang.Integer> bitRates = null;
            android.util.Range<java.lang.Long> blockRates = null;
            android.util.Range<android.util.Rational> ratios = null;
            android.util.Range<android.util.Rational> blockRatios = null;
            blockSize = android.media.Utils.parseSize(map.get("block-size"), blockSize);
            alignment = android.media.Utils.parseSize(map.get("alignment"), alignment);
            counts = android.media.Utils.parseIntRange(map.get("block-count-range"), null);
            blockRates = android.media.Utils.parseLongRange(map.get("blocks-per-second-range"), null);
            mMeasuredFrameRates = getMeasuredFrameRates(map);
            android.util.Pair<android.util.Range<java.lang.Integer>, android.util.Range<java.lang.Integer>> sizeRanges = android.media.MediaCodecInfo.VideoCapabilities.parseWidthHeightRanges(map.get("size-range"));
            if (sizeRanges != null) {
                widths = sizeRanges.first;
                heights = sizeRanges.second;
            }
            // for now this just means using the smaller max size as 2nd
            // upper limit.
            // for now we are keeping the profile specific "width/height
            // in macroblocks" limits.
            if (map.containsKey("feature-can-swap-width-height")) {
                if (widths != null) {
                    mSmallerDimensionUpperLimit = java.lang.Math.min(widths.getUpper(), heights.getUpper());
                    widths = heights = widths.extend(heights);
                } else {
                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, "feature can-swap-width-height is best used with size-range");
                    mSmallerDimensionUpperLimit = java.lang.Math.min(mWidthRange.getUpper(), mHeightRange.getUpper());
                    mWidthRange = mHeightRange = mWidthRange.extend(mHeightRange);
                }
            }
            ratios = android.media.Utils.parseRationalRange(map.get("block-aspect-ratio-range"), null);
            blockRatios = android.media.Utils.parseRationalRange(map.get("pixel-aspect-ratio-range"), null);
            frameRates = android.media.Utils.parseIntRange(map.get("frame-rate-range"), null);
            if (frameRates != null) {
                try {
                    frameRates = frameRates.intersect(android.media.MediaCodecInfo.FRAME_RATE_RANGE);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("frame rate range (" + frameRates) + ") is out of limits: ") + android.media.MediaCodecInfo.FRAME_RATE_RANGE);
                    frameRates = null;
                }
            }
            bitRates = android.media.Utils.parseIntRange(map.get("bitrate-range"), null);
            if (bitRates != null) {
                try {
                    bitRates = bitRates.intersect(android.media.MediaCodecInfo.BITRATE_RANGE);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("bitrate range (" + bitRates) + ") is out of limits: ") + android.media.MediaCodecInfo.BITRATE_RANGE);
                    bitRates = null;
                }
            }
            android.media.MediaCodecInfo.checkPowerOfTwo(blockSize.getWidth(), "block-size width must be power of two");
            android.media.MediaCodecInfo.checkPowerOfTwo(blockSize.getHeight(), "block-size height must be power of two");
            android.media.MediaCodecInfo.checkPowerOfTwo(alignment.getWidth(), "alignment width must be power of two");
            android.media.MediaCodecInfo.checkPowerOfTwo(alignment.getHeight(), "alignment height must be power of two");
            // update block-size and alignment
            applyMacroBlockLimits(java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE, java.lang.Long.MAX_VALUE, blockSize.getWidth(), blockSize.getHeight(), alignment.getWidth(), alignment.getHeight());
            if (((mParent.mError & android.media.MediaCodecInfo.ERROR_UNSUPPORTED) != 0) || mAllowMbOverride) {
                // codec supports profiles that we don't know.
                // Use supplied values clipped to platform limits
                if (widths != null) {
                    mWidthRange = android.media.MediaCodecInfo.SIZE_RANGE.intersect(widths);
                }
                if (heights != null) {
                    mHeightRange = android.media.MediaCodecInfo.SIZE_RANGE.intersect(heights);
                }
                if (counts != null) {
                    mBlockCountRange = android.media.MediaCodecInfo.POSITIVE_INTEGERS.intersect(android.media.Utils.factorRange(counts, ((mBlockWidth * mBlockHeight) / blockSize.getWidth()) / blockSize.getHeight()));
                }
                if (blockRates != null) {
                    mBlocksPerSecondRange = android.media.MediaCodecInfo.POSITIVE_LONGS.intersect(android.media.Utils.factorRange(blockRates, ((mBlockWidth * mBlockHeight) / blockSize.getWidth()) / blockSize.getHeight()));
                }
                if (blockRatios != null) {
                    mBlockAspectRatioRange = android.media.MediaCodecInfo.POSITIVE_RATIONALS.intersect(android.media.Utils.scaleRange(blockRatios, mBlockHeight / blockSize.getHeight(), mBlockWidth / blockSize.getWidth()));
                }
                if (ratios != null) {
                    mAspectRatioRange = android.media.MediaCodecInfo.POSITIVE_RATIONALS.intersect(ratios);
                }
                if (frameRates != null) {
                    mFrameRateRange = android.media.MediaCodecInfo.FRAME_RATE_RANGE.intersect(frameRates);
                }
                if (bitRates != null) {
                    // only allow bitrate override if unsupported profiles were encountered
                    if ((mParent.mError & android.media.MediaCodecInfo.ERROR_UNSUPPORTED) != 0) {
                        mBitrateRange = android.media.MediaCodecInfo.BITRATE_RANGE.intersect(bitRates);
                    } else {
                        mBitrateRange = mBitrateRange.intersect(bitRates);
                    }
                }
            } else {
                // no unsupported profile/levels, so restrict values to known limits
                if (widths != null) {
                    mWidthRange = mWidthRange.intersect(widths);
                }
                if (heights != null) {
                    mHeightRange = mHeightRange.intersect(heights);
                }
                if (counts != null) {
                    mBlockCountRange = mBlockCountRange.intersect(android.media.Utils.factorRange(counts, ((mBlockWidth * mBlockHeight) / blockSize.getWidth()) / blockSize.getHeight()));
                }
                if (blockRates != null) {
                    mBlocksPerSecondRange = mBlocksPerSecondRange.intersect(android.media.Utils.factorRange(blockRates, ((mBlockWidth * mBlockHeight) / blockSize.getWidth()) / blockSize.getHeight()));
                }
                if (blockRatios != null) {
                    mBlockAspectRatioRange = mBlockAspectRatioRange.intersect(android.media.Utils.scaleRange(blockRatios, mBlockHeight / blockSize.getHeight(), mBlockWidth / blockSize.getWidth()));
                }
                if (ratios != null) {
                    mAspectRatioRange = mAspectRatioRange.intersect(ratios);
                }
                if (frameRates != null) {
                    mFrameRateRange = mFrameRateRange.intersect(frameRates);
                }
                if (bitRates != null) {
                    mBitrateRange = mBitrateRange.intersect(bitRates);
                }
            }
            updateLimits();
        }

        private void applyBlockLimits(int blockWidth, int blockHeight, android.util.Range<java.lang.Integer> counts, android.util.Range<java.lang.Long> rates, android.util.Range<android.util.Rational> ratios) {
            android.media.MediaCodecInfo.checkPowerOfTwo(blockWidth, "blockWidth must be a power of two");
            android.media.MediaCodecInfo.checkPowerOfTwo(blockHeight, "blockHeight must be a power of two");
            final int newBlockWidth = java.lang.Math.max(blockWidth, mBlockWidth);
            final int newBlockHeight = java.lang.Math.max(blockHeight, mBlockHeight);
            // factor will always be a power-of-2
            int factor = ((newBlockWidth * newBlockHeight) / mBlockWidth) / mBlockHeight;
            if (factor != 1) {
                mBlockCountRange = android.media.Utils.factorRange(mBlockCountRange, factor);
                mBlocksPerSecondRange = android.media.Utils.factorRange(mBlocksPerSecondRange, factor);
                mBlockAspectRatioRange = android.media.Utils.scaleRange(mBlockAspectRatioRange, newBlockHeight / mBlockHeight, newBlockWidth / mBlockWidth);
                mHorizontalBlockRange = android.media.Utils.factorRange(mHorizontalBlockRange, newBlockWidth / mBlockWidth);
                mVerticalBlockRange = android.media.Utils.factorRange(mVerticalBlockRange, newBlockHeight / mBlockHeight);
            }
            factor = ((newBlockWidth * newBlockHeight) / blockWidth) / blockHeight;
            if (factor != 1) {
                counts = android.media.Utils.factorRange(counts, factor);
                rates = android.media.Utils.factorRange(rates, factor);
                ratios = android.media.Utils.scaleRange(ratios, newBlockHeight / blockHeight, newBlockWidth / blockWidth);
            }
            mBlockCountRange = mBlockCountRange.intersect(counts);
            mBlocksPerSecondRange = mBlocksPerSecondRange.intersect(rates);
            mBlockAspectRatioRange = mBlockAspectRatioRange.intersect(ratios);
            mBlockWidth = newBlockWidth;
            mBlockHeight = newBlockHeight;
        }

        private void applyAlignment(int widthAlignment, int heightAlignment) {
            android.media.MediaCodecInfo.checkPowerOfTwo(widthAlignment, "widthAlignment must be a power of two");
            android.media.MediaCodecInfo.checkPowerOfTwo(heightAlignment, "heightAlignment must be a power of two");
            if ((widthAlignment > mBlockWidth) || (heightAlignment > mBlockHeight)) {
                // maintain assumption that 0 < alignment <= block-size
                applyBlockLimits(java.lang.Math.max(widthAlignment, mBlockWidth), java.lang.Math.max(heightAlignment, mBlockHeight), android.media.MediaCodecInfo.POSITIVE_INTEGERS, android.media.MediaCodecInfo.POSITIVE_LONGS, android.media.MediaCodecInfo.POSITIVE_RATIONALS);
            }
            mWidthAlignment = java.lang.Math.max(widthAlignment, mWidthAlignment);
            mHeightAlignment = java.lang.Math.max(heightAlignment, mHeightAlignment);
            mWidthRange = android.media.Utils.alignRange(mWidthRange, mWidthAlignment);
            mHeightRange = android.media.Utils.alignRange(mHeightRange, mHeightAlignment);
        }

        private void updateLimits() {
            // pixels -> blocks <- counts
            mHorizontalBlockRange = mHorizontalBlockRange.intersect(android.media.Utils.factorRange(mWidthRange, mBlockWidth));
            mHorizontalBlockRange = mHorizontalBlockRange.intersect(android.util.Range.create(mBlockCountRange.getLower() / mVerticalBlockRange.getUpper(), mBlockCountRange.getUpper() / mVerticalBlockRange.getLower()));
            mVerticalBlockRange = mVerticalBlockRange.intersect(android.media.Utils.factorRange(mHeightRange, mBlockHeight));
            mVerticalBlockRange = mVerticalBlockRange.intersect(android.util.Range.create(mBlockCountRange.getLower() / mHorizontalBlockRange.getUpper(), mBlockCountRange.getUpper() / mHorizontalBlockRange.getLower()));
            mBlockCountRange = mBlockCountRange.intersect(android.util.Range.create(mHorizontalBlockRange.getLower() * mVerticalBlockRange.getLower(), mHorizontalBlockRange.getUpper() * mVerticalBlockRange.getUpper()));
            mBlockAspectRatioRange = mBlockAspectRatioRange.intersect(new android.util.Rational(mHorizontalBlockRange.getLower(), mVerticalBlockRange.getUpper()), new android.util.Rational(mHorizontalBlockRange.getUpper(), mVerticalBlockRange.getLower()));
            // blocks -> pixels
            mWidthRange = mWidthRange.intersect(((mHorizontalBlockRange.getLower() - 1) * mBlockWidth) + mWidthAlignment, mHorizontalBlockRange.getUpper() * mBlockWidth);
            mHeightRange = mHeightRange.intersect(((mVerticalBlockRange.getLower() - 1) * mBlockHeight) + mHeightAlignment, mVerticalBlockRange.getUpper() * mBlockHeight);
            mAspectRatioRange = mAspectRatioRange.intersect(new android.util.Rational(mWidthRange.getLower(), mHeightRange.getUpper()), new android.util.Rational(mWidthRange.getUpper(), mHeightRange.getLower()));
            mSmallerDimensionUpperLimit = java.lang.Math.min(mSmallerDimensionUpperLimit, java.lang.Math.min(mWidthRange.getUpper(), mHeightRange.getUpper()));
            // blocks -> rate
            mBlocksPerSecondRange = mBlocksPerSecondRange.intersect(mBlockCountRange.getLower() * ((long) (mFrameRateRange.getLower())), mBlockCountRange.getUpper() * ((long) (mFrameRateRange.getUpper())));
            mFrameRateRange = mFrameRateRange.intersect(((int) (mBlocksPerSecondRange.getLower() / mBlockCountRange.getUpper())), ((int) (mBlocksPerSecondRange.getUpper() / ((double) (mBlockCountRange.getLower())))));
        }

        private void applyMacroBlockLimits(int maxHorizontalBlocks, int maxVerticalBlocks, int maxBlocks, long maxBlocksPerSecond, int blockWidth, int blockHeight, int widthAlignment, int heightAlignment) {
            /* minHorizontalBlocks */
            /* minVerticalBlocks */
            applyMacroBlockLimits(1, 1, maxHorizontalBlocks, maxVerticalBlocks, maxBlocks, maxBlocksPerSecond, blockWidth, blockHeight, widthAlignment, heightAlignment);
        }

        private void applyMacroBlockLimits(int minHorizontalBlocks, int minVerticalBlocks, int maxHorizontalBlocks, int maxVerticalBlocks, int maxBlocks, long maxBlocksPerSecond, int blockWidth, int blockHeight, int widthAlignment, int heightAlignment) {
            applyAlignment(widthAlignment, heightAlignment);
            applyBlockLimits(blockWidth, blockHeight, android.util.Range.create(1, maxBlocks), android.util.Range.create(1L, maxBlocksPerSecond), android.util.Range.create(new android.util.Rational(1, maxVerticalBlocks), new android.util.Rational(maxHorizontalBlocks, 1)));
            mHorizontalBlockRange = mHorizontalBlockRange.intersect(android.media.Utils.divUp(minHorizontalBlocks, mBlockWidth / blockWidth), maxHorizontalBlocks / (mBlockWidth / blockWidth));
            mVerticalBlockRange = mVerticalBlockRange.intersect(android.media.Utils.divUp(minVerticalBlocks, mBlockHeight / blockHeight), maxVerticalBlocks / (mBlockHeight / blockHeight));
        }

        private void applyLevelLimits() {
            long maxBlocksPerSecond = 0;
            int maxBlocks = 0;
            int maxBps = 0;
            int maxDPBBlocks = 0;
            int errors = android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
            android.media.MediaCodecInfo.CodecProfileLevel[] profileLevels = mParent.profileLevels;
            java.lang.String mime = mParent.getMimeType();
            if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_AVC)) {
                maxBlocks = 99;
                maxBlocksPerSecond = 1485;
                maxBps = 64000;
                maxDPBBlocks = 396;
                for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                    int MBPS = 0;
                    int FS = 0;
                    int BR = 0;
                    int DPB = 0;
                    boolean supported = true;
                    switch (profileLevel.level) {
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel1 :
                            MBPS = 1485;
                            FS = 99;
                            BR = 64;
                            DPB = 396;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel1b :
                            MBPS = 1485;
                            FS = 99;
                            BR = 128;
                            DPB = 396;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel11 :
                            MBPS = 3000;
                            FS = 396;
                            BR = 192;
                            DPB = 900;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel12 :
                            MBPS = 6000;
                            FS = 396;
                            BR = 384;
                            DPB = 2376;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel13 :
                            MBPS = 11880;
                            FS = 396;
                            BR = 768;
                            DPB = 2376;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel2 :
                            MBPS = 11880;
                            FS = 396;
                            BR = 2000;
                            DPB = 2376;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel21 :
                            MBPS = 19800;
                            FS = 792;
                            BR = 4000;
                            DPB = 4752;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel22 :
                            MBPS = 20250;
                            FS = 1620;
                            BR = 4000;
                            DPB = 8100;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel3 :
                            MBPS = 40500;
                            FS = 1620;
                            BR = 10000;
                            DPB = 8100;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel31 :
                            MBPS = 108000;
                            FS = 3600;
                            BR = 14000;
                            DPB = 18000;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel32 :
                            MBPS = 216000;
                            FS = 5120;
                            BR = 20000;
                            DPB = 20480;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel4 :
                            MBPS = 245760;
                            FS = 8192;
                            BR = 20000;
                            DPB = 32768;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel41 :
                            MBPS = 245760;
                            FS = 8192;
                            BR = 50000;
                            DPB = 32768;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel42 :
                            MBPS = 522240;
                            FS = 8704;
                            BR = 50000;
                            DPB = 34816;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel5 :
                            MBPS = 589824;
                            FS = 22080;
                            BR = 135000;
                            DPB = 110400;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel51 :
                            MBPS = 983040;
                            FS = 36864;
                            BR = 240000;
                            DPB = 184320;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel52 :
                            MBPS = 2073600;
                            FS = 36864;
                            BR = 240000;
                            DPB = 184320;
                            break;
                        default :
                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized level " + profileLevel.level) + " for ") + mime);
                            errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                    }
                    switch (profileLevel.profile) {
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileHigh :
                            BR *= 1250;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileHigh10 :
                            BR *= 3000;
                            break;
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileExtended :
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileHigh422 :
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileHigh444 :
                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unsupported profile " + profileLevel.profile) + " for ") + mime);
                            errors |= android.media.MediaCodecInfo.ERROR_UNSUPPORTED;
                            supported = false;
                            // fall through - treat as base profile
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline :
                        case android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileMain :
                            BR *= 1000;
                            break;
                        default :
                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                            errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                            BR *= 1000;
                    }
                    if (supported) {
                        errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                    }
                    maxBlocksPerSecond = java.lang.Math.max(MBPS, maxBlocksPerSecond);
                    maxBlocks = java.lang.Math.max(FS, maxBlocks);
                    maxBps = java.lang.Math.max(BR, maxBps);
                    maxDPBBlocks = java.lang.Math.max(maxDPBBlocks, DPB);
                }
                int maxLengthInBlocks = ((int) (java.lang.Math.sqrt(maxBlocks * 8)));
                /* blockWidth */
                /* blockHeight */
                /* widthAlignment */
                /* heightAlignment */
                applyMacroBlockLimits(maxLengthInBlocks, maxLengthInBlocks, maxBlocks, maxBlocksPerSecond, 16, 16, 1, 1);
            } else
                if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_MPEG2)) {
                    int maxWidth = 11;
                    int maxHeight = 9;
                    int maxRate = 15;
                    maxBlocks = 99;
                    maxBlocksPerSecond = 1485;
                    maxBps = 64000;
                    for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                        int MBPS = 0;
                        int FS = 0;
                        int BR = 0;
                        int FR = 0;
                        int W = 0;
                        int H = 0;
                        boolean supported = true;
                        switch (profileLevel.profile) {
                            case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2ProfileSimple :
                                switch (profileLevel.level) {
                                    case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2LevelML :
                                        FR = 30;
                                        W = 45;
                                        H = 36;
                                        MBPS = 40500;
                                        FS = 1620;
                                        BR = 15000;
                                        break;
                                    default :
                                        android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (((("Unrecognized profile/level " + profileLevel.profile) + "/") + profileLevel.level) + " for ") + mime);
                                        errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                }
                                break;
                            case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2ProfileMain :
                                switch (profileLevel.level) {
                                    case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2LevelLL :
                                        FR = 30;
                                        W = 22;
                                        H = 18;
                                        MBPS = 11880;
                                        FS = 396;
                                        BR = 4000;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2LevelML :
                                        FR = 30;
                                        W = 45;
                                        H = 36;
                                        MBPS = 40500;
                                        FS = 1620;
                                        BR = 15000;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2LevelH14 :
                                        FR = 60;
                                        W = 90;
                                        H = 68;
                                        MBPS = 183600;
                                        FS = 6120;
                                        BR = 60000;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2LevelHL :
                                        FR = 60;
                                        W = 120;
                                        H = 68;
                                        MBPS = 244800;
                                        FS = 8160;
                                        BR = 80000;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2LevelHP :
                                        FR = 60;
                                        W = 120;
                                        H = 68;
                                        MBPS = 489600;
                                        FS = 8160;
                                        BR = 80000;
                                        break;
                                    default :
                                        android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (((("Unrecognized profile/level " + profileLevel.profile) + "/") + profileLevel.level) + " for ") + mime);
                                        errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                }
                                break;
                            case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2Profile422 :
                            case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2ProfileSNR :
                            case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2ProfileSpatial :
                            case android.media.MediaCodecInfo.CodecProfileLevel.MPEG2ProfileHigh :
                                android.util.Log.i(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unsupported profile " + profileLevel.profile) + " for ") + mime);
                                errors |= android.media.MediaCodecInfo.ERROR_UNSUPPORTED;
                                supported = false;
                                break;
                            default :
                                android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                                errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                        }
                        if (supported) {
                            errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                        }
                        maxBlocksPerSecond = java.lang.Math.max(MBPS, maxBlocksPerSecond);
                        maxBlocks = java.lang.Math.max(FS, maxBlocks);
                        maxBps = java.lang.Math.max(BR * 1000, maxBps);
                        maxWidth = java.lang.Math.max(W, maxWidth);
                        maxHeight = java.lang.Math.max(H, maxHeight);
                        maxRate = java.lang.Math.max(FR, maxRate);
                    }
                    /* blockWidth */
                    /* blockHeight */
                    /* widthAlignment */
                    /* heightAlignment */
                    applyMacroBlockLimits(maxWidth, maxHeight, maxBlocks, maxBlocksPerSecond, 16, 16, 1, 1);
                    mFrameRateRange = mFrameRateRange.intersect(12, maxRate);
                } else
                    if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_MPEG4)) {
                        int maxWidth = 11;
                        int maxHeight = 9;
                        int maxRate = 15;
                        maxBlocks = 99;
                        maxBlocksPerSecond = 1485;
                        maxBps = 64000;
                        for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                            int MBPS = 0;
                            int FS = 0;
                            int BR = 0;
                            int FR = 0;
                            int W = 0;
                            int H = 0;
                            boolean strict = false;// true: W, H and FR are individual max limits

                            boolean supported = true;
                            switch (profileLevel.profile) {
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileSimple :
                                    switch (profileLevel.level) {
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level0 :
                                            strict = true;
                                            FR = 15;
                                            W = 11;
                                            H = 9;
                                            MBPS = 1485;
                                            FS = 99;
                                            BR = 64;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level1 :
                                            FR = 30;
                                            W = 11;
                                            H = 9;
                                            MBPS = 1485;
                                            FS = 99;
                                            BR = 64;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level0b :
                                            strict = true;
                                            FR = 15;
                                            W = 11;
                                            H = 9;
                                            MBPS = 1485;
                                            FS = 99;
                                            BR = 128;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level2 :
                                            FR = 30;
                                            W = 22;
                                            H = 18;
                                            MBPS = 5940;
                                            FS = 396;
                                            BR = 128;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level3 :
                                            FR = 30;
                                            W = 22;
                                            H = 18;
                                            MBPS = 11880;
                                            FS = 396;
                                            BR = 384;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level4a :
                                            FR = 30;
                                            W = 40;
                                            H = 30;
                                            MBPS = 36000;
                                            FS = 1200;
                                            BR = 4000;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level5 :
                                            FR = 30;
                                            W = 45;
                                            H = 36;
                                            MBPS = 40500;
                                            FS = 1620;
                                            BR = 8000;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level6 :
                                            FR = 30;
                                            W = 80;
                                            H = 45;
                                            MBPS = 108000;
                                            FS = 3600;
                                            BR = 12000;
                                            break;
                                        default :
                                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (((("Unrecognized profile/level " + profileLevel.profile) + "/") + profileLevel.level) + " for ") + mime);
                                            errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                    }
                                    break;
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileAdvancedSimple :
                                    switch (profileLevel.level) {
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level0 :
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level1 :
                                            FR = 30;
                                            W = 11;
                                            H = 9;
                                            MBPS = 2970;
                                            FS = 99;
                                            BR = 128;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level2 :
                                            FR = 30;
                                            W = 22;
                                            H = 18;
                                            MBPS = 5940;
                                            FS = 396;
                                            BR = 384;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level3 :
                                            FR = 30;
                                            W = 22;
                                            H = 18;
                                            MBPS = 11880;
                                            FS = 396;
                                            BR = 768;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level3b :
                                            FR = 30;
                                            W = 22;
                                            H = 18;
                                            MBPS = 11880;
                                            FS = 396;
                                            BR = 1500;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level4 :
                                            FR = 30;
                                            W = 44;
                                            H = 36;
                                            MBPS = 23760;
                                            FS = 792;
                                            BR = 3000;
                                            break;
                                        case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4Level5 :
                                            FR = 30;
                                            W = 45;
                                            H = 36;
                                            MBPS = 48600;
                                            FS = 1620;
                                            BR = 8000;
                                            break;
                                        default :
                                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (((("Unrecognized profile/level " + profileLevel.profile) + "/") + profileLevel.level) + " for ") + mime);
                                            errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                    }
                                    break;
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileMain :
                                    // 2-4
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileNbit :
                                    // 2
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileAdvancedRealTime :
                                    // 1-4
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileCoreScalable :
                                    // 1-3
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileAdvancedCoding :
                                    // 1-4
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileCore :
                                    // 1-2
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileAdvancedCore :
                                    // 1-4
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileSimpleScalable :
                                    // 0-2
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileHybrid :
                                    // 1-2
                                    // Studio profiles are not supported by our codecs.
                                    // Only profiles that can decode simple object types are considered.
                                    // The following profiles are not able to.
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileBasicAnimated :
                                    // 1-2
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileScalableTexture :
                                    // 1
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileSimpleFace :
                                    // 1-2
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileAdvancedScalable :
                                    // 1-3
                                case android.media.MediaCodecInfo.CodecProfileLevel.MPEG4ProfileSimpleFBA :
                                    // 1-2
                                    android.util.Log.i(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unsupported profile " + profileLevel.profile) + " for ") + mime);
                                    errors |= android.media.MediaCodecInfo.ERROR_UNSUPPORTED;
                                    supported = false;
                                    break;
                                default :
                                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                                    errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                            }
                            if (supported) {
                                errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                            }
                            maxBlocksPerSecond = java.lang.Math.max(MBPS, maxBlocksPerSecond);
                            maxBlocks = java.lang.Math.max(FS, maxBlocks);
                            maxBps = java.lang.Math.max(BR * 1000, maxBps);
                            if (strict) {
                                maxWidth = java.lang.Math.max(W, maxWidth);
                                maxHeight = java.lang.Math.max(H, maxHeight);
                                maxRate = java.lang.Math.max(FR, maxRate);
                            } else {
                                // assuming max 60 fps frame rate and 1:2 aspect ratio
                                int maxDim = ((int) (java.lang.Math.sqrt(FS * 2)));
                                maxWidth = java.lang.Math.max(maxDim, maxWidth);
                                maxHeight = java.lang.Math.max(maxDim, maxHeight);
                                maxRate = java.lang.Math.max(java.lang.Math.max(FR, 60), maxRate);
                            }
                        }
                        /* blockWidth */
                        /* blockHeight */
                        /* widthAlignment */
                        /* heightAlignment */
                        applyMacroBlockLimits(maxWidth, maxHeight, maxBlocks, maxBlocksPerSecond, 16, 16, 1, 1);
                        mFrameRateRange = mFrameRateRange.intersect(12, maxRate);
                    } else
                        if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_H263)) {
                            int maxWidth = 11;
                            int maxHeight = 9;
                            int maxRate = 15;
                            int minWidth = maxWidth;
                            int minHeight = maxHeight;
                            int minAlignment = 16;
                            maxBlocks = 99;
                            maxBlocksPerSecond = 1485;
                            maxBps = 64000;
                            for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                                int MBPS = 0;
                                int BR = 0;
                                int FR = 0;
                                int W = 0;
                                int H = 0;
                                int minW = minWidth;
                                int minH = minHeight;
                                boolean strict = false;// true: support only sQCIF, QCIF (maybe CIF)

                                switch (profileLevel.level) {
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level10 :
                                        strict = true;// only supports sQCIF & QCIF

                                        FR = 15;
                                        W = 11;
                                        H = 9;
                                        BR = 1;
                                        MBPS = (W * H) * FR;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level20 :
                                        strict = true;// only supports sQCIF, QCIF & CIF

                                        FR = 30;
                                        W = 22;
                                        H = 18;
                                        BR = 2;
                                        MBPS = (W * H) * 15;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level30 :
                                        strict = true;// only supports sQCIF, QCIF & CIF

                                        FR = 30;
                                        W = 22;
                                        H = 18;
                                        BR = 6;
                                        MBPS = (W * H) * FR;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level40 :
                                        strict = true;// only supports sQCIF, QCIF & CIF

                                        FR = 30;
                                        W = 22;
                                        H = 18;
                                        BR = 32;
                                        MBPS = (W * H) * FR;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level45 :
                                        // only implies level 10 support
                                        strict = (profileLevel.profile == android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileBaseline) || (profileLevel.profile == android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileBackwardCompatible);
                                        if (!strict) {
                                            minW = 1;
                                            minH = 1;
                                            minAlignment = 4;
                                        }
                                        FR = 15;
                                        W = 11;
                                        H = 9;
                                        BR = 2;
                                        MBPS = (W * H) * FR;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level50 :
                                        // only supports 50fps for H > 15
                                        minW = 1;
                                        minH = 1;
                                        minAlignment = 4;
                                        FR = 60;
                                        W = 22;
                                        H = 18;
                                        BR = 64;
                                        MBPS = (W * H) * 50;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level60 :
                                        // only supports 50fps for H > 15
                                        minW = 1;
                                        minH = 1;
                                        minAlignment = 4;
                                        FR = 60;
                                        W = 45;
                                        H = 18;
                                        BR = 128;
                                        MBPS = (W * H) * 50;
                                        break;
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263Level70 :
                                        // only supports 50fps for H > 30
                                        minW = 1;
                                        minH = 1;
                                        minAlignment = 4;
                                        FR = 60;
                                        W = 45;
                                        H = 36;
                                        BR = 256;
                                        MBPS = (W * H) * 50;
                                        break;
                                    default :
                                        android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (((("Unrecognized profile/level " + profileLevel.profile) + "/") + profileLevel.level) + " for ") + mime);
                                        errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                }
                                switch (profileLevel.profile) {
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileBackwardCompatible :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileBaseline :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileH320Coding :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileHighCompression :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileHighLatency :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileInterlace :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileInternet :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileISWV2 :
                                    case android.media.MediaCodecInfo.CodecProfileLevel.H263ProfileISWV3 :
                                        break;
                                    default :
                                        android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                                        errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                }
                                if (strict) {
                                    // Strict levels define sub-QCIF min size and enumerated sizes. We cannot
                                    // express support for "only sQCIF & QCIF (& CIF)" using VideoCapabilities
                                    // but we can express "only QCIF (& CIF)", so set minimume size at QCIF.
                                    // minW = 8; minH = 6;
                                    minW = 11;
                                    minH = 9;
                                } else {
                                    // any support for non-strict levels (including unrecognized profiles or
                                    // levels) allow custom frame size support beyond supported limits
                                    // (other than bitrate)
                                    mAllowMbOverride = true;
                                }
                                errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                                maxBlocksPerSecond = java.lang.Math.max(MBPS, maxBlocksPerSecond);
                                maxBlocks = java.lang.Math.max(W * H, maxBlocks);
                                maxBps = java.lang.Math.max(BR * 64000, maxBps);
                                maxWidth = java.lang.Math.max(W, maxWidth);
                                maxHeight = java.lang.Math.max(H, maxHeight);
                                maxRate = java.lang.Math.max(FR, maxRate);
                                minWidth = java.lang.Math.min(minW, minWidth);
                                minHeight = java.lang.Math.min(minH, minHeight);
                            }
                            // unless we encountered custom frame size support, limit size to QCIF and CIF
                            // using aspect ratio.
                            if (!mAllowMbOverride) {
                                mBlockAspectRatioRange = android.util.Range.create(new android.util.Rational(11, 9), new android.util.Rational(11, 9));
                            }
                            /* blockWidth */
                            /* blockHeight */
                            /* widthAlignment */
                            /* heightAlignment */
                            applyMacroBlockLimits(minWidth, minHeight, maxWidth, maxHeight, maxBlocks, maxBlocksPerSecond, 16, 16, minAlignment, minAlignment);
                            mFrameRateRange = android.util.Range.create(1, maxRate);
                        } else
                            if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_VP8)) {
                                maxBlocks = java.lang.Integer.MAX_VALUE;
                                maxBlocksPerSecond = java.lang.Integer.MAX_VALUE;
                                // TODO: set to 100Mbps for now, need a number for VP8
                                maxBps = 100000000;
                                // profile levels are not indicative for VPx, but verify
                                // them nonetheless
                                for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                                    switch (profileLevel.level) {
                                        case android.media.MediaCodecInfo.CodecProfileLevel.VP8Level_Version0 :
                                        case android.media.MediaCodecInfo.CodecProfileLevel.VP8Level_Version1 :
                                        case android.media.MediaCodecInfo.CodecProfileLevel.VP8Level_Version2 :
                                        case android.media.MediaCodecInfo.CodecProfileLevel.VP8Level_Version3 :
                                            break;
                                        default :
                                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized level " + profileLevel.level) + " for ") + mime);
                                            errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                    }
                                    switch (profileLevel.profile) {
                                        case android.media.MediaCodecInfo.CodecProfileLevel.VP8ProfileMain :
                                            break;
                                        default :
                                            android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                                            errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                    }
                                    errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                                }
                                final int blockSize = 16;
                                /* widthAlignment */
                                /* heightAlignment */
                                applyMacroBlockLimits(java.lang.Short.MAX_VALUE, java.lang.Short.MAX_VALUE, maxBlocks, maxBlocksPerSecond, blockSize, blockSize, 1, 1);
                            } else
                                if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_VP9)) {
                                    maxBlocksPerSecond = 829440;
                                    maxBlocks = 36864;
                                    maxBps = 200000;
                                    int maxDim = 512;
                                    for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                                        long SR = 0;// luma sample rate

                                        int FS = 0;// luma picture size

                                        int BR = 0;// bit rate kbps

                                        int D = 0;// luma dimension

                                        switch (profileLevel.level) {
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level1 :
                                                SR = 829440;
                                                FS = 36864;
                                                BR = 200;
                                                D = 512;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level11 :
                                                SR = 2764800;
                                                FS = 73728;
                                                BR = 800;
                                                D = 768;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level2 :
                                                SR = 4608000;
                                                FS = 122880;
                                                BR = 1800;
                                                D = 960;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level21 :
                                                SR = 9216000;
                                                FS = 245760;
                                                BR = 3600;
                                                D = 1344;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level3 :
                                                SR = 20736000;
                                                FS = 552960;
                                                BR = 7200;
                                                D = 2048;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level31 :
                                                SR = 36864000;
                                                FS = 983040;
                                                BR = 12000;
                                                D = 2752;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level4 :
                                                SR = 83558400;
                                                FS = 2228224;
                                                BR = 18000;
                                                D = 4160;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level41 :
                                                SR = 160432128;
                                                FS = 2228224;
                                                BR = 30000;
                                                D = 4160;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level5 :
                                                SR = 311951360;
                                                FS = 8912896;
                                                BR = 60000;
                                                D = 8384;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level51 :
                                                SR = 588251136;
                                                FS = 8912896;
                                                BR = 120000;
                                                D = 8384;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level52 :
                                                SR = 1176502272;
                                                FS = 8912896;
                                                BR = 180000;
                                                D = 8384;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level6 :
                                                SR = 1176502272;
                                                FS = 35651584;
                                                BR = 180000;
                                                D = 16832;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level61 :
                                                SR = 2353004544L;
                                                FS = 35651584;
                                                BR = 240000;
                                                D = 16832;
                                                break;
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Level62 :
                                                SR = 4706009088L;
                                                FS = 35651584;
                                                BR = 480000;
                                                D = 16832;
                                                break;
                                            default :
                                                android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized level " + profileLevel.level) + " for ") + mime);
                                                errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                        }
                                        switch (profileLevel.profile) {
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile0 :
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile1 :
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile2 :
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile3 :
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile2HDR :
                                            case android.media.MediaCodecInfo.CodecProfileLevel.VP9Profile3HDR :
                                                break;
                                            default :
                                                android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                                                errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                        }
                                        errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                                        maxBlocksPerSecond = java.lang.Math.max(SR, maxBlocksPerSecond);
                                        maxBlocks = java.lang.Math.max(FS, maxBlocks);
                                        maxBps = java.lang.Math.max(BR * 1000, maxBps);
                                        maxDim = java.lang.Math.max(D, maxDim);
                                    }
                                    final int blockSize = 8;
                                    int maxLengthInBlocks = android.media.Utils.divUp(maxDim, blockSize);
                                    maxBlocks = android.media.Utils.divUp(maxBlocks, blockSize * blockSize);
                                    maxBlocksPerSecond = android.media.Utils.divUp(maxBlocksPerSecond, blockSize * blockSize);
                                    /* widthAlignment */
                                    /* heightAlignment */
                                    applyMacroBlockLimits(maxLengthInBlocks, maxLengthInBlocks, maxBlocks, maxBlocksPerSecond, blockSize, blockSize, 1, 1);
                                } else
                                    if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_VIDEO_HEVC)) {
                                        // CTBs are at least 8x8 so use 8x8 block size
                                        maxBlocks = 36864 >> 6;// 192x192 pixels == 576 8x8 blocks

                                        maxBlocksPerSecond = maxBlocks * 15;
                                        maxBps = 128000;
                                        for (android.media.MediaCodecInfo.CodecProfileLevel profileLevel : profileLevels) {
                                            double FR = 0;
                                            int FS = 0;
                                            int BR = 0;
                                            switch (profileLevel.level) {
                                                /* The HEVC spec talks only in a very convoluted manner about the
                                                existence of levels 1-3.1 for High tier, which could also be
                                                understood as 'decoders and encoders should treat these levels
                                                as if they were Main tier', so we do that.
                                                 */
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel1 :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel1 :
                                                    FR = 15;
                                                    FS = 36864;
                                                    BR = 128;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel2 :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel2 :
                                                    FR = 30;
                                                    FS = 122880;
                                                    BR = 1500;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel21 :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel21 :
                                                    FR = 30;
                                                    FS = 245760;
                                                    BR = 3000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel3 :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel3 :
                                                    FR = 30;
                                                    FS = 552960;
                                                    BR = 6000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel31 :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel31 :
                                                    FR = 33.75;
                                                    FS = 983040;
                                                    BR = 10000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel4 :
                                                    FR = 30;
                                                    FS = 2228224;
                                                    BR = 12000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel4 :
                                                    FR = 30;
                                                    FS = 2228224;
                                                    BR = 30000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel41 :
                                                    FR = 60;
                                                    FS = 2228224;
                                                    BR = 20000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel41 :
                                                    FR = 60;
                                                    FS = 2228224;
                                                    BR = 50000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel5 :
                                                    FR = 30;
                                                    FS = 8912896;
                                                    BR = 25000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel5 :
                                                    FR = 30;
                                                    FS = 8912896;
                                                    BR = 100000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel51 :
                                                    FR = 60;
                                                    FS = 8912896;
                                                    BR = 40000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel51 :
                                                    FR = 60;
                                                    FS = 8912896;
                                                    BR = 160000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel52 :
                                                    FR = 120;
                                                    FS = 8912896;
                                                    BR = 60000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel52 :
                                                    FR = 120;
                                                    FS = 8912896;
                                                    BR = 240000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel6 :
                                                    FR = 30;
                                                    FS = 35651584;
                                                    BR = 60000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel6 :
                                                    FR = 30;
                                                    FS = 35651584;
                                                    BR = 240000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel61 :
                                                    FR = 60;
                                                    FS = 35651584;
                                                    BR = 120000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel61 :
                                                    FR = 60;
                                                    FS = 35651584;
                                                    BR = 480000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCMainTierLevel62 :
                                                    FR = 120;
                                                    FS = 35651584;
                                                    BR = 240000;
                                                    break;
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel62 :
                                                    FR = 120;
                                                    FS = 35651584;
                                                    BR = 800000;
                                                    break;
                                                default :
                                                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized level " + profileLevel.level) + " for ") + mime);
                                                    errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                            }
                                            switch (profileLevel.profile) {
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCProfileMain :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCProfileMain10 :
                                                case android.media.MediaCodecInfo.CodecProfileLevel.HEVCProfileMain10HDR10 :
                                                    break;
                                                default :
                                                    android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, (("Unrecognized profile " + profileLevel.profile) + " for ") + mime);
                                                    errors |= android.media.MediaCodecInfo.ERROR_UNRECOGNIZED;
                                            }
                                            /* DPB logic:
                                            if      (width * height <= FS / 4)    DPB = 16;
                                            else if (width * height <= FS / 2)    DPB = 12;
                                            else if (width * height <= FS * 0.75) DPB = 8;
                                            else                                  DPB = 6;
                                             */
                                            FS >>= 6;// convert pixels to blocks

                                            errors &= ~android.media.MediaCodecInfo.ERROR_NONE_SUPPORTED;
                                            maxBlocksPerSecond = java.lang.Math.max(((int) (FR * FS)), maxBlocksPerSecond);
                                            maxBlocks = java.lang.Math.max(FS, maxBlocks);
                                            maxBps = java.lang.Math.max(BR * 1000, maxBps);
                                        }
                                        int maxLengthInBlocks = ((int) (java.lang.Math.sqrt(maxBlocks * 8)));
                                        /* blockWidth */
                                        /* blockHeight */
                                        /* widthAlignment */
                                        /* heightAlignment */
                                        applyMacroBlockLimits(maxLengthInBlocks, maxLengthInBlocks, maxBlocks, maxBlocksPerSecond, 8, 8, 1, 1);
                                    } else {
                                        android.util.Log.w(android.media.MediaCodecInfo.VideoCapabilities.TAG, "Unsupported mime " + mime);
                                        // using minimal bitrate here.  should be overriden by
                                        // info from media_codecs.xml
                                        maxBps = 64000;
                                        errors |= android.media.MediaCodecInfo.ERROR_UNSUPPORTED;
                                    }






            mBitrateRange = android.util.Range.create(1, maxBps);
            mParent.mError |= errors;
        }
    }

    /**
     * A class that supports querying the encoding capabilities of a codec.
     */
    public static final class EncoderCapabilities {
        /**
         * Returns the supported range of quality values.
         *
         * @unknown 
         */
        public android.util.Range<java.lang.Integer> getQualityRange() {
            return mQualityRange;
        }

        /**
         * Returns the supported range of encoder complexity values.
         * <p>
         * Some codecs may support multiple complexity levels, where higher
         * complexity values use more encoder tools (e.g. perform more
         * intensive calculations) to improve the quality or the compression
         * ratio.  Use a lower value to save power and/or time.
         */
        public android.util.Range<java.lang.Integer> getComplexityRange() {
            return mComplexityRange;
        }

        /**
         * Constant quality mode
         */
        public static final int BITRATE_MODE_CQ = 0;

        /**
         * Variable bitrate mode
         */
        public static final int BITRATE_MODE_VBR = 1;

        /**
         * Constant bitrate mode
         */
        public static final int BITRATE_MODE_CBR = 2;

        private static final android.media.MediaCodecInfo.Feature[] bitrates = new android.media.MediaCodecInfo.Feature[]{ new android.media.MediaCodecInfo.Feature("VBR", android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR, true), new android.media.MediaCodecInfo.Feature("CBR", android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR, false), new android.media.MediaCodecInfo.Feature("CQ", android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ, false) };

        private static int parseBitrateMode(java.lang.String mode) {
            for (android.media.MediaCodecInfo.Feature feat : android.media.MediaCodecInfo.EncoderCapabilities.bitrates) {
                if (feat.mName.equalsIgnoreCase(mode)) {
                    return feat.mValue;
                }
            }
            return 0;
        }

        /**
         * Query whether a bitrate mode is supported.
         */
        public boolean isBitrateModeSupported(int mode) {
            for (android.media.MediaCodecInfo.Feature feat : android.media.MediaCodecInfo.EncoderCapabilities.bitrates) {
                if (mode == feat.mValue) {
                    return (mBitControl & (1 << mode)) != 0;
                }
            }
            return false;
        }

        private android.util.Range<java.lang.Integer> mQualityRange;

        private android.util.Range<java.lang.Integer> mComplexityRange;

        private android.media.MediaCodecInfo.CodecCapabilities mParent;

        /* no public constructor */
        private EncoderCapabilities() {
        }

        /**
         *
         *
         * @unknown 
         */
        public static android.media.MediaCodecInfo.EncoderCapabilities create(android.media.MediaFormat info, android.media.MediaCodecInfo.CodecCapabilities parent) {
            android.media.MediaCodecInfo.EncoderCapabilities caps = new android.media.MediaCodecInfo.EncoderCapabilities();
            caps.init(info, parent);
            return caps;
        }

        /**
         *
         *
         * @unknown 
         */
        public void init(android.media.MediaFormat info, android.media.MediaCodecInfo.CodecCapabilities parent) {
            // no support for complexity or quality yet
            mParent = parent;
            mComplexityRange = android.util.Range.create(0, 0);
            mQualityRange = android.util.Range.create(0, 0);
            mBitControl = 1 << android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR;
            applyLevelLimits();
            parseFromInfo(info);
        }

        private void applyLevelLimits() {
            java.lang.String mime = mParent.getMimeType();
            if (mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_FLAC)) {
                mComplexityRange = android.util.Range.create(0, 8);
                mBitControl = 1 << android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ;
            } else
                if ((((mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_AMR_NB) || mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_AMR_WB)) || mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_G711_ALAW)) || mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_G711_MLAW)) || mime.equalsIgnoreCase(android.media.MediaFormat.MIMETYPE_AUDIO_MSGSM)) {
                    mBitControl = 1 << android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR;
                }

        }

        private int mBitControl;

        private java.lang.Integer mDefaultComplexity;

        private java.lang.Integer mDefaultQuality;

        private java.lang.String mQualityScale;

        private void parseFromInfo(android.media.MediaFormat info) {
            java.util.Map<java.lang.String, java.lang.Object> map = info.getMap();
            if (info.containsKey("complexity-range")) {
                mComplexityRange = android.media.Utils.parseIntRange(info.getString("complexity-range"), mComplexityRange);
                // TODO should we limit this to level limits?
            }
            if (info.containsKey("quality-range")) {
                mQualityRange = android.media.Utils.parseIntRange(info.getString("quality-range"), mQualityRange);
            }
            if (info.containsKey("feature-bitrate-control")) {
                for (java.lang.String mode : info.getString("feature-bitrate-control").split(",")) {
                    mBitControl |= android.media.MediaCodecInfo.EncoderCapabilities.parseBitrateMode(mode);
                }
            }
            try {
                mDefaultComplexity = java.lang.Integer.parseInt(((java.lang.String) (map.get("complexity-default"))));
            } catch (java.lang.NumberFormatException e) {
            }
            try {
                mDefaultQuality = java.lang.Integer.parseInt(((java.lang.String) (map.get("quality-default"))));
            } catch (java.lang.NumberFormatException e) {
            }
            mQualityScale = ((java.lang.String) (map.get("quality-scale")));
        }

        private boolean supports(java.lang.Integer complexity, java.lang.Integer quality, java.lang.Integer profile) {
            boolean ok = true;
            if (ok && (complexity != null)) {
                ok = mComplexityRange.contains(complexity);
            }
            if (ok && (quality != null)) {
                ok = mQualityRange.contains(quality);
            }
            if (ok && (profile != null)) {
                for (android.media.MediaCodecInfo.CodecProfileLevel pl : mParent.profileLevels) {
                    if (pl.profile == profile) {
                        profile = null;
                        break;
                    }
                }
                ok = profile == null;
            }
            return ok;
        }

        /**
         *
         *
         * @unknown 
         */
        public void setDefaultFormat(android.media.MediaFormat format) {
            // don't list trivial quality/complexity as default for now
            if ((!mQualityRange.getUpper().equals(mQualityRange.getLower())) && (mDefaultQuality != null)) {
                format.setInteger(android.media.MediaFormat.KEY_QUALITY, mDefaultQuality);
            }
            if ((!mComplexityRange.getUpper().equals(mComplexityRange.getLower())) && (mDefaultComplexity != null)) {
                format.setInteger(android.media.MediaFormat.KEY_COMPLEXITY, mDefaultComplexity);
            }
            // bitrates are listed in order of preference
            for (android.media.MediaCodecInfo.Feature feat : android.media.MediaCodecInfo.EncoderCapabilities.bitrates) {
                if ((mBitControl & (1 << feat.mValue)) != 0) {
                    format.setInteger(android.media.MediaFormat.KEY_BITRATE_MODE, feat.mValue);
                    break;
                }
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean supportsFormat(android.media.MediaFormat format) {
            final java.util.Map<java.lang.String, java.lang.Object> map = format.getMap();
            final java.lang.String mime = mParent.getMimeType();
            java.lang.Integer mode = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_BITRATE_MODE)));
            if ((mode != null) && (!isBitrateModeSupported(mode))) {
                return false;
            }
            java.lang.Integer complexity = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_COMPLEXITY)));
            if (android.media.MediaFormat.MIMETYPE_AUDIO_FLAC.equalsIgnoreCase(mime)) {
                java.lang.Integer flacComplexity = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_FLAC_COMPRESSION_LEVEL)));
                if (complexity == null) {
                    complexity = flacComplexity;
                } else
                    if ((flacComplexity != null) && (!complexity.equals(flacComplexity))) {
                        throw new java.lang.IllegalArgumentException("conflicting values for complexity and " + "flac-compression-level");
                    }

            }
            // other audio parameters
            java.lang.Integer profile = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_PROFILE)));
            if (android.media.MediaFormat.MIMETYPE_AUDIO_AAC.equalsIgnoreCase(mime)) {
                java.lang.Integer aacProfile = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_AAC_PROFILE)));
                if (profile == null) {
                    profile = aacProfile;
                } else
                    if ((aacProfile != null) && (!aacProfile.equals(profile))) {
                        throw new java.lang.IllegalArgumentException("conflicting values for profile and aac-profile");
                    }

            }
            java.lang.Integer quality = ((java.lang.Integer) (map.get(android.media.MediaFormat.KEY_QUALITY)));
            return supports(complexity, quality, profile);
        }
    }

    /**
     * Encapsulates the profiles available for a codec component.
     * <p>You can get a set of {@link MediaCodecInfo.CodecProfileLevel} objects for a given
     * {@link MediaCodecInfo} object from the
     * {@link MediaCodecInfo.CodecCapabilities#profileLevels} field.
     */
    public static final class CodecProfileLevel {
        // from OMX_VIDEO_AVCPROFILETYPE
        public static final int AVCProfileBaseline = 0x1;

        public static final int AVCProfileMain = 0x2;

        public static final int AVCProfileExtended = 0x4;

        public static final int AVCProfileHigh = 0x8;

        public static final int AVCProfileHigh10 = 0x10;

        public static final int AVCProfileHigh422 = 0x20;

        public static final int AVCProfileHigh444 = 0x40;

        // from OMX_VIDEO_AVCLEVELTYPE
        public static final int AVCLevel1 = 0x1;

        public static final int AVCLevel1b = 0x2;

        public static final int AVCLevel11 = 0x4;

        public static final int AVCLevel12 = 0x8;

        public static final int AVCLevel13 = 0x10;

        public static final int AVCLevel2 = 0x20;

        public static final int AVCLevel21 = 0x40;

        public static final int AVCLevel22 = 0x80;

        public static final int AVCLevel3 = 0x100;

        public static final int AVCLevel31 = 0x200;

        public static final int AVCLevel32 = 0x400;

        public static final int AVCLevel4 = 0x800;

        public static final int AVCLevel41 = 0x1000;

        public static final int AVCLevel42 = 0x2000;

        public static final int AVCLevel5 = 0x4000;

        public static final int AVCLevel51 = 0x8000;

        public static final int AVCLevel52 = 0x10000;

        // from OMX_VIDEO_H263PROFILETYPE
        public static final int H263ProfileBaseline = 0x1;

        public static final int H263ProfileH320Coding = 0x2;

        public static final int H263ProfileBackwardCompatible = 0x4;

        public static final int H263ProfileISWV2 = 0x8;

        public static final int H263ProfileISWV3 = 0x10;

        public static final int H263ProfileHighCompression = 0x20;

        public static final int H263ProfileInternet = 0x40;

        public static final int H263ProfileInterlace = 0x80;

        public static final int H263ProfileHighLatency = 0x100;

        // from OMX_VIDEO_H263LEVELTYPE
        public static final int H263Level10 = 0x1;

        public static final int H263Level20 = 0x2;

        public static final int H263Level30 = 0x4;

        public static final int H263Level40 = 0x8;

        public static final int H263Level45 = 0x10;

        public static final int H263Level50 = 0x20;

        public static final int H263Level60 = 0x40;

        public static final int H263Level70 = 0x80;

        // from OMX_VIDEO_MPEG4PROFILETYPE
        public static final int MPEG4ProfileSimple = 0x1;

        public static final int MPEG4ProfileSimpleScalable = 0x2;

        public static final int MPEG4ProfileCore = 0x4;

        public static final int MPEG4ProfileMain = 0x8;

        public static final int MPEG4ProfileNbit = 0x10;

        public static final int MPEG4ProfileScalableTexture = 0x20;

        public static final int MPEG4ProfileSimpleFace = 0x40;

        public static final int MPEG4ProfileSimpleFBA = 0x80;

        public static final int MPEG4ProfileBasicAnimated = 0x100;

        public static final int MPEG4ProfileHybrid = 0x200;

        public static final int MPEG4ProfileAdvancedRealTime = 0x400;

        public static final int MPEG4ProfileCoreScalable = 0x800;

        public static final int MPEG4ProfileAdvancedCoding = 0x1000;

        public static final int MPEG4ProfileAdvancedCore = 0x2000;

        public static final int MPEG4ProfileAdvancedScalable = 0x4000;

        public static final int MPEG4ProfileAdvancedSimple = 0x8000;

        // from OMX_VIDEO_MPEG4LEVELTYPE
        public static final int MPEG4Level0 = 0x1;

        public static final int MPEG4Level0b = 0x2;

        public static final int MPEG4Level1 = 0x4;

        public static final int MPEG4Level2 = 0x8;

        public static final int MPEG4Level3 = 0x10;

        public static final int MPEG4Level3b = 0x18;

        public static final int MPEG4Level4 = 0x20;

        public static final int MPEG4Level4a = 0x40;

        public static final int MPEG4Level5 = 0x80;

        public static final int MPEG4Level6 = 0x100;

        // from OMX_VIDEO_MPEG2PROFILETYPE
        public static final int MPEG2ProfileSimple = 0x0;

        public static final int MPEG2ProfileMain = 0x1;

        public static final int MPEG2Profile422 = 0x2;

        public static final int MPEG2ProfileSNR = 0x3;

        public static final int MPEG2ProfileSpatial = 0x4;

        public static final int MPEG2ProfileHigh = 0x5;

        // from OMX_VIDEO_MPEG2LEVELTYPE
        public static final int MPEG2LevelLL = 0x0;

        public static final int MPEG2LevelML = 0x1;

        public static final int MPEG2LevelH14 = 0x2;

        public static final int MPEG2LevelHL = 0x3;

        public static final int MPEG2LevelHP = 0x4;

        // from OMX_AUDIO_AACPROFILETYPE
        public static final int AACObjectMain = 1;

        public static final int AACObjectLC = 2;

        public static final int AACObjectSSR = 3;

        public static final int AACObjectLTP = 4;

        public static final int AACObjectHE = 5;

        public static final int AACObjectScalable = 6;

        public static final int AACObjectERLC = 17;

        public static final int AACObjectLD = 23;

        public static final int AACObjectHE_PS = 29;

        public static final int AACObjectELD = 39;

        // from OMX_VIDEO_VP8LEVELTYPE
        public static final int VP8Level_Version0 = 0x1;

        public static final int VP8Level_Version1 = 0x2;

        public static final int VP8Level_Version2 = 0x4;

        public static final int VP8Level_Version3 = 0x8;

        // from OMX_VIDEO_VP8PROFILETYPE
        public static final int VP8ProfileMain = 0x1;

        // from OMX_VIDEO_VP9PROFILETYPE
        public static final int VP9Profile0 = 0x1;

        public static final int VP9Profile1 = 0x2;

        public static final int VP9Profile2 = 0x4;

        public static final int VP9Profile3 = 0x8;

        // HDR profiles also support passing HDR metadata
        public static final int VP9Profile2HDR = 0x1000;

        public static final int VP9Profile3HDR = 0x2000;

        // from OMX_VIDEO_VP9LEVELTYPE
        public static final int VP9Level1 = 0x1;

        public static final int VP9Level11 = 0x2;

        public static final int VP9Level2 = 0x4;

        public static final int VP9Level21 = 0x8;

        public static final int VP9Level3 = 0x10;

        public static final int VP9Level31 = 0x20;

        public static final int VP9Level4 = 0x40;

        public static final int VP9Level41 = 0x80;

        public static final int VP9Level5 = 0x100;

        public static final int VP9Level51 = 0x200;

        public static final int VP9Level52 = 0x400;

        public static final int VP9Level6 = 0x800;

        public static final int VP9Level61 = 0x1000;

        public static final int VP9Level62 = 0x2000;

        // from OMX_VIDEO_HEVCPROFILETYPE
        public static final int HEVCProfileMain = 0x1;

        public static final int HEVCProfileMain10 = 0x2;

        public static final int HEVCProfileMain10HDR10 = 0x1000;

        // from OMX_VIDEO_HEVCLEVELTYPE
        public static final int HEVCMainTierLevel1 = 0x1;

        public static final int HEVCHighTierLevel1 = 0x2;

        public static final int HEVCMainTierLevel2 = 0x4;

        public static final int HEVCHighTierLevel2 = 0x8;

        public static final int HEVCMainTierLevel21 = 0x10;

        public static final int HEVCHighTierLevel21 = 0x20;

        public static final int HEVCMainTierLevel3 = 0x40;

        public static final int HEVCHighTierLevel3 = 0x80;

        public static final int HEVCMainTierLevel31 = 0x100;

        public static final int HEVCHighTierLevel31 = 0x200;

        public static final int HEVCMainTierLevel4 = 0x400;

        public static final int HEVCHighTierLevel4 = 0x800;

        public static final int HEVCMainTierLevel41 = 0x1000;

        public static final int HEVCHighTierLevel41 = 0x2000;

        public static final int HEVCMainTierLevel5 = 0x4000;

        public static final int HEVCHighTierLevel5 = 0x8000;

        public static final int HEVCMainTierLevel51 = 0x10000;

        public static final int HEVCHighTierLevel51 = 0x20000;

        public static final int HEVCMainTierLevel52 = 0x40000;

        public static final int HEVCHighTierLevel52 = 0x80000;

        public static final int HEVCMainTierLevel6 = 0x100000;

        public static final int HEVCHighTierLevel6 = 0x200000;

        public static final int HEVCMainTierLevel61 = 0x400000;

        public static final int HEVCHighTierLevel61 = 0x800000;

        public static final int HEVCMainTierLevel62 = 0x1000000;

        public static final int HEVCHighTierLevel62 = 0x2000000;

        private static final int HEVCHighTierLevels = (((((((((((android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel1 | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel2) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel21) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel3) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel31) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel4) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel41) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel5) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel51) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel52) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel6) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel61) | android.media.MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel62;

        // from OMX_VIDEO_DOLBYVISIONPROFILETYPE
        public static final int DolbyVisionProfileDvavPer = 0x1;

        public static final int DolbyVisionProfileDvavPen = 0x2;

        public static final int DolbyVisionProfileDvheDer = 0x4;

        public static final int DolbyVisionProfileDvheDen = 0x8;

        public static final int DolbyVisionProfileDvheDtr = 0x10;

        public static final int DolbyVisionProfileDvheStn = 0x20;

        public static final int DolbyVisionProfileDvheDth = 0x40;

        public static final int DolbyVisionProfileDvheDtb = 0x80;

        // from OMX_VIDEO_DOLBYVISIONLEVELTYPE
        public static final int DolbyVisionLevelHd24 = 0x1;

        public static final int DolbyVisionLevelHd30 = 0x2;

        public static final int DolbyVisionLevelFhd24 = 0x4;

        public static final int DolbyVisionLevelFhd30 = 0x8;

        public static final int DolbyVisionLevelFhd60 = 0x10;

        public static final int DolbyVisionLevelUhd24 = 0x20;

        public static final int DolbyVisionLevelUhd30 = 0x40;

        public static final int DolbyVisionLevelUhd48 = 0x80;

        public static final int DolbyVisionLevelUhd60 = 0x100;

        /**
         * Defined in the OpenMAX IL specs, depending on the type of media
         * this can be OMX_VIDEO_AVCPROFILETYPE, OMX_VIDEO_H263PROFILETYPE,
         * OMX_VIDEO_MPEG4PROFILETYPE, OMX_VIDEO_VP8PROFILETYPE or OMX_VIDEO_VP9PROFILETYPE.
         */
        public int profile;

        /**
         * Defined in the OpenMAX IL specs, depending on the type of media
         * this can be OMX_VIDEO_AVCLEVELTYPE, OMX_VIDEO_H263LEVELTYPE
         * OMX_VIDEO_MPEG4LEVELTYPE, OMX_VIDEO_VP8LEVELTYPE or OMX_VIDEO_VP9LEVELTYPE.
         *
         * Note that VP9 decoder on platforms before {@link android.os.Build.VERSION_CODES#N} may
         * not advertise a profile level support. For those VP9 decoders, please use
         * {@link VideoCapabilities} to determine the codec capabilities.
         */
        public int level;
    }

    /**
     * Enumerates the capabilities of the codec component. Since a single
     * component can support data of a variety of types, the type has to be
     * specified to yield a meaningful result.
     *
     * @param type
     * 		The MIME type to query
     */
    public final android.media.MediaCodecInfo.CodecCapabilities getCapabilitiesForType(java.lang.String type) {
        android.media.MediaCodecInfo.CodecCapabilities caps = mCaps.get(type);
        if (caps == null) {
            throw new java.lang.IllegalArgumentException("codec does not support type");
        }
        // clone writable object
        return caps.dup();
    }

    /**
     *
     *
     * @unknown 
     */
    public android.media.MediaCodecInfo makeRegular() {
        java.util.ArrayList<android.media.MediaCodecInfo.CodecCapabilities> caps = new java.util.ArrayList<android.media.MediaCodecInfo.CodecCapabilities>();
        for (android.media.MediaCodecInfo.CodecCapabilities c : mCaps.values()) {
            if (c.isRegular()) {
                caps.add(c);
            }
        }
        if (caps.size() == 0) {
            return null;
        } else
            if (caps.size() == mCaps.size()) {
                return this;
            }

        return new android.media.MediaCodecInfo(mName, mIsEncoder, caps.toArray(new android.media.MediaCodecInfo.CodecCapabilities[caps.size()]));
    }
}

