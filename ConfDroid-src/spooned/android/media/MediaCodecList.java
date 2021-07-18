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
 * Allows you to enumerate available codecs, each specified as a {@link MediaCodecInfo} object,
 * find a codec supporting a given format and query the capabilities
 * of a given codec.
 * <p>See {@link MediaCodecInfo} for sample usage.
 */
public final class MediaCodecList {
    private static final java.lang.String TAG = "MediaCodecList";

    /**
     * Count the number of available (regular) codecs.
     *
     * @deprecated Use {@link #getCodecInfos} instead.
     * @see #REGULAR_CODECS
     */
    public static final int getCodecCount() {
        android.media.MediaCodecList.initCodecList();
        return android.media.MediaCodecList.sRegularCodecInfos.length;
    }

    private static final native int native_getCodecCount();

    /**
     * Return the {@link MediaCodecInfo} object for the codec at
     * the given {@code index} in the regular list.
     *
     * @deprecated Use {@link #getCodecInfos} instead.
     * @see #REGULAR_CODECS
     */
    public static final android.media.MediaCodecInfo getCodecInfoAt(int index) {
        android.media.MediaCodecList.initCodecList();
        if ((index < 0) || (index > android.media.MediaCodecList.sRegularCodecInfos.length)) {
            throw new java.lang.IllegalArgumentException();
        }
        return android.media.MediaCodecList.sRegularCodecInfos[index];
    }

    /* package private */
    static final java.util.Map<java.lang.String, java.lang.Object> getGlobalSettings() {
        synchronized(android.media.MediaCodecList.sInitLock) {
            if (android.media.MediaCodecList.sGlobalSettings == null) {
                android.media.MediaCodecList.sGlobalSettings = android.media.MediaCodecList.native_getGlobalSettings();
            }
        }
        return android.media.MediaCodecList.sGlobalSettings;
    }

    private static java.lang.Object sInitLock = new java.lang.Object();

    private static android.media.MediaCodecInfo[] sAllCodecInfos;

    private static android.media.MediaCodecInfo[] sRegularCodecInfos;

    private static java.util.Map<java.lang.String, java.lang.Object> sGlobalSettings;

    private static final void initCodecList() {
        synchronized(android.media.MediaCodecList.sInitLock) {
            if (android.media.MediaCodecList.sRegularCodecInfos == null) {
                int count = android.media.MediaCodecList.native_getCodecCount();
                java.util.ArrayList<android.media.MediaCodecInfo> regulars = new java.util.ArrayList<android.media.MediaCodecInfo>();
                java.util.ArrayList<android.media.MediaCodecInfo> all = new java.util.ArrayList<android.media.MediaCodecInfo>();
                for (int index = 0; index < count; index++) {
                    try {
                        android.media.MediaCodecInfo info = android.media.MediaCodecList.getNewCodecInfoAt(index);
                        all.add(info);
                        info = info.makeRegular();
                        if (info != null) {
                            regulars.add(info);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Log.e(android.media.MediaCodecList.TAG, "Could not get codec capabilities", e);
                    }
                }
                android.media.MediaCodecList.sRegularCodecInfos = regulars.toArray(new android.media.MediaCodecInfo[regulars.size()]);
                android.media.MediaCodecList.sAllCodecInfos = all.toArray(new android.media.MediaCodecInfo[all.size()]);
            }
        }
    }

    private static android.media.MediaCodecInfo getNewCodecInfoAt(int index) {
        java.lang.String[] supportedTypes = android.media.MediaCodecList.getSupportedTypes(index);
        android.media.MediaCodecInfo.CodecCapabilities[] caps = new android.media.MediaCodecInfo.CodecCapabilities[supportedTypes.length];
        int typeIx = 0;
        for (java.lang.String type : supportedTypes) {
            caps[typeIx++] = android.media.MediaCodecList.getCodecCapabilities(index, type);
        }
        return new android.media.MediaCodecInfo(android.media.MediaCodecList.getCodecName(index), android.media.MediaCodecList.isEncoder(index), caps);
    }

    /* package private */
    static final native java.lang.String getCodecName(int index);

    /* package private */
    static final native boolean isEncoder(int index);

    /* package private */
    static final native java.lang.String[] getSupportedTypes(int index);

    /* package private */
    static final native android.media.MediaCodecInfo.CodecCapabilities getCodecCapabilities(int index, java.lang.String type);

    /* package private */
    static final native java.util.Map<java.lang.String, java.lang.Object> native_getGlobalSettings();

    /* package private */
    static final native int findCodecByName(java.lang.String codec);

    /**
     *
     *
     * @unknown 
     */
    public static android.media.MediaCodecInfo getInfoFor(java.lang.String codec) {
        android.media.MediaCodecList.initCodecList();
        return android.media.MediaCodecList.sAllCodecInfos[android.media.MediaCodecList.findCodecByName(codec)];
    }

    private static final native void native_init();

    /**
     * Use in {@link #MediaCodecList} to enumerate only codecs that are suitable
     * for regular (buffer-to-buffer) decoding or encoding.
     *
     * <em>NOTE:</em> These are the codecs that are returned prior to API 21,
     * using the now deprecated static methods.
     */
    public static final int REGULAR_CODECS = 0;

    /**
     * Use in {@link #MediaCodecList} to enumerate all codecs, even ones that are
     * not suitable for regular (buffer-to-buffer) decoding or encoding.  These
     * include codecs, for example, that only work with special input or output
     * surfaces, such as secure-only or tunneled-only codecs.
     *
     * @see MediaCodecInfo.CodecCapabilities#isFormatSupported
     * @see MediaCodecInfo.CodecCapabilities#FEATURE_SecurePlayback
     * @see MediaCodecInfo.CodecCapabilities#FEATURE_TunneledPlayback
     */
    public static final int ALL_CODECS = 1;

    private MediaCodecList() {
        this(android.media.MediaCodecList.REGULAR_CODECS);
    }

    private android.media.MediaCodecInfo[] mCodecInfos;

    /**
     * Create a list of media-codecs of a specific kind.
     *
     * @param kind
     * 		Either {@code REGULAR_CODECS} or {@code ALL_CODECS}.
     */
    public MediaCodecList(int kind) {
        android.media.MediaCodecList.initCodecList();
        if (kind == android.media.MediaCodecList.REGULAR_CODECS) {
            mCodecInfos = android.media.MediaCodecList.sRegularCodecInfos;
        } else {
            mCodecInfos = android.media.MediaCodecList.sAllCodecInfos;
        }
    }

    /**
     * Returns the list of {@link MediaCodecInfo} objects for the list
     * of media-codecs.
     */
    public final android.media.MediaCodecInfo[] getCodecInfos() {
        return java.util.Arrays.copyOf(mCodecInfos, mCodecInfos.length);
    }

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaCodecList.native_init();
        // mediaserver is not yet alive here
    }

    /**
     * Find a decoder supporting a given {@link MediaFormat} in the list
     * of media-codecs.
     *
     * <p class=note>
     * <strong>Note:</strong> On {@link android.os.Build.VERSION_CODES#LOLLIPOP},
     * {@code format} must not contain a {@linkplain MediaFormat#KEY_FRAME_RATE
     * frame rate}. Use
     * <code class=prettyprint>format.setString(MediaFormat.KEY_FRAME_RATE, null)</code>
     * to clear any existing frame rate setting in the format.
     *
     * @see MediaCodecList.CodecCapabilities.isFormatSupported for format keys
    considered per android versions when evaluating suitable codecs.
     * @param format
     * 		A decoder media format with optional feature directives.
     * @throws IllegalArgumentException
     * 		if format is not a valid media format.
     * @throws NullPointerException
     * 		if format is null.
     * @return the name of a decoder that supports the given format and feature
    requests, or {@code null} if no such codec has been found.
     */
    public final java.lang.String findDecoderForFormat(android.media.MediaFormat format) {
        return /* encoder */
        findCodecForFormat(false, format);
    }

    /**
     * Find an encoder supporting a given {@link MediaFormat} in the list
     * of media-codecs.
     *
     * <p class=note>
     * <strong>Note:</strong> On {@link android.os.Build.VERSION_CODES#LOLLIPOP},
     * {@code format} must not contain a {@linkplain MediaFormat#KEY_FRAME_RATE
     * frame rate}. Use
     * <code class=prettyprint>format.setString(MediaFormat.KEY_FRAME_RATE, null)</code>
     * to clear any existing frame rate setting in the format.
     *
     * @see MediaCodecList.CodecCapabilities.isFormatSupported for format keys
    considered per android versions when evaluating suitable codecs.
     * @param format
     * 		An encoder media format with optional feature directives.
     * @throws IllegalArgumentException
     * 		if format is not a valid media format.
     * @throws NullPointerException
     * 		if format is null.
     * @return the name of an encoder that supports the given format and feature
    requests, or {@code null} if no such codec has been found.
     */
    public final java.lang.String findEncoderForFormat(android.media.MediaFormat format) {
        return /* encoder */
        findCodecForFormat(true, format);
    }

    private java.lang.String findCodecForFormat(boolean encoder, android.media.MediaFormat format) {
        java.lang.String mime = format.getString(android.media.MediaFormat.KEY_MIME);
        for (android.media.MediaCodecInfo info : mCodecInfos) {
            if (info.isEncoder() != encoder) {
                continue;
            }
            try {
                android.media.MediaCodecInfo.CodecCapabilities caps = info.getCapabilitiesForType(mime);
                if ((caps != null) && caps.isFormatSupported(format)) {
                    return info.getName();
                }
            } catch (java.lang.IllegalArgumentException e) {
                // type is not supported
            }
        }
        return null;
    }
}

