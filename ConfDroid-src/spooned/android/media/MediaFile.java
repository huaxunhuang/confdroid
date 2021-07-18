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
 * MediaScanner helper class.
 *
 * {@hide }
 */
public class MediaFile {
    // Audio file types
    public static final int FILE_TYPE_MP3 = 1;

    public static final int FILE_TYPE_M4A = 2;

    public static final int FILE_TYPE_WAV = 3;

    public static final int FILE_TYPE_AMR = 4;

    public static final int FILE_TYPE_AWB = 5;

    public static final int FILE_TYPE_WMA = 6;

    public static final int FILE_TYPE_OGG = 7;

    public static final int FILE_TYPE_AAC = 8;

    public static final int FILE_TYPE_MKA = 9;

    public static final int FILE_TYPE_FLAC = 10;

    private static final int FIRST_AUDIO_FILE_TYPE = android.media.MediaFile.FILE_TYPE_MP3;

    private static final int LAST_AUDIO_FILE_TYPE = android.media.MediaFile.FILE_TYPE_FLAC;

    // MIDI file types
    public static final int FILE_TYPE_MID = 11;

    public static final int FILE_TYPE_SMF = 12;

    public static final int FILE_TYPE_IMY = 13;

    private static final int FIRST_MIDI_FILE_TYPE = android.media.MediaFile.FILE_TYPE_MID;

    private static final int LAST_MIDI_FILE_TYPE = android.media.MediaFile.FILE_TYPE_IMY;

    // Video file types
    public static final int FILE_TYPE_MP4 = 21;

    public static final int FILE_TYPE_M4V = 22;

    public static final int FILE_TYPE_3GPP = 23;

    public static final int FILE_TYPE_3GPP2 = 24;

    public static final int FILE_TYPE_WMV = 25;

    public static final int FILE_TYPE_ASF = 26;

    public static final int FILE_TYPE_MKV = 27;

    public static final int FILE_TYPE_MP2TS = 28;

    public static final int FILE_TYPE_AVI = 29;

    public static final int FILE_TYPE_WEBM = 30;

    private static final int FIRST_VIDEO_FILE_TYPE = android.media.MediaFile.FILE_TYPE_MP4;

    private static final int LAST_VIDEO_FILE_TYPE = android.media.MediaFile.FILE_TYPE_WEBM;

    // More video file types
    public static final int FILE_TYPE_MP2PS = 200;

    public static final int FILE_TYPE_QT = 201;

    private static final int FIRST_VIDEO_FILE_TYPE2 = android.media.MediaFile.FILE_TYPE_MP2PS;

    private static final int LAST_VIDEO_FILE_TYPE2 = android.media.MediaFile.FILE_TYPE_QT;

    // Image file types
    public static final int FILE_TYPE_JPEG = 31;

    public static final int FILE_TYPE_GIF = 32;

    public static final int FILE_TYPE_PNG = 33;

    public static final int FILE_TYPE_BMP = 34;

    public static final int FILE_TYPE_WBMP = 35;

    public static final int FILE_TYPE_WEBP = 36;

    private static final int FIRST_IMAGE_FILE_TYPE = android.media.MediaFile.FILE_TYPE_JPEG;

    private static final int LAST_IMAGE_FILE_TYPE = android.media.MediaFile.FILE_TYPE_WEBP;

    // Raw image file types
    public static final int FILE_TYPE_DNG = 300;

    public static final int FILE_TYPE_CR2 = 301;

    public static final int FILE_TYPE_NEF = 302;

    public static final int FILE_TYPE_NRW = 303;

    public static final int FILE_TYPE_ARW = 304;

    public static final int FILE_TYPE_RW2 = 305;

    public static final int FILE_TYPE_ORF = 306;

    public static final int FILE_TYPE_RAF = 307;

    public static final int FILE_TYPE_PEF = 308;

    public static final int FILE_TYPE_SRW = 309;

    private static final int FIRST_RAW_IMAGE_FILE_TYPE = android.media.MediaFile.FILE_TYPE_DNG;

    private static final int LAST_RAW_IMAGE_FILE_TYPE = android.media.MediaFile.FILE_TYPE_SRW;

    // Playlist file types
    public static final int FILE_TYPE_M3U = 41;

    public static final int FILE_TYPE_PLS = 42;

    public static final int FILE_TYPE_WPL = 43;

    public static final int FILE_TYPE_HTTPLIVE = 44;

    private static final int FIRST_PLAYLIST_FILE_TYPE = android.media.MediaFile.FILE_TYPE_M3U;

    private static final int LAST_PLAYLIST_FILE_TYPE = android.media.MediaFile.FILE_TYPE_HTTPLIVE;

    // Drm file types
    public static final int FILE_TYPE_FL = 51;

    private static final int FIRST_DRM_FILE_TYPE = android.media.MediaFile.FILE_TYPE_FL;

    private static final int LAST_DRM_FILE_TYPE = android.media.MediaFile.FILE_TYPE_FL;

    // Other popular file types
    public static final int FILE_TYPE_TEXT = 100;

    public static final int FILE_TYPE_HTML = 101;

    public static final int FILE_TYPE_PDF = 102;

    public static final int FILE_TYPE_XML = 103;

    public static final int FILE_TYPE_MS_WORD = 104;

    public static final int FILE_TYPE_MS_EXCEL = 105;

    public static final int FILE_TYPE_MS_POWERPOINT = 106;

    public static final int FILE_TYPE_ZIP = 107;

    public static class MediaFileType {
        public final int fileType;

        public final java.lang.String mimeType;

        MediaFileType(int fileType, java.lang.String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    private static final java.util.HashMap<java.lang.String, android.media.MediaFile.MediaFileType> sFileTypeMap = new java.util.HashMap<java.lang.String, android.media.MediaFile.MediaFileType>();

    private static final java.util.HashMap<java.lang.String, java.lang.Integer> sMimeTypeMap = new java.util.HashMap<java.lang.String, java.lang.Integer>();

    // maps file extension to MTP format code
    private static final java.util.HashMap<java.lang.String, java.lang.Integer> sFileTypeToFormatMap = new java.util.HashMap<java.lang.String, java.lang.Integer>();

    // maps mime type to MTP format code
    private static final java.util.HashMap<java.lang.String, java.lang.Integer> sMimeTypeToFormatMap = new java.util.HashMap<java.lang.String, java.lang.Integer>();

    // maps MTP format code to mime type
    private static final java.util.HashMap<java.lang.Integer, java.lang.String> sFormatToMimeTypeMap = new java.util.HashMap<java.lang.Integer, java.lang.String>();

    static void addFileType(java.lang.String extension, int fileType, java.lang.String mimeType) {
        android.media.MediaFile.sFileTypeMap.put(extension, new android.media.MediaFile.MediaFileType(fileType, mimeType));
        android.media.MediaFile.sMimeTypeMap.put(mimeType, java.lang.Integer.valueOf(fileType));
    }

    static void addFileType(java.lang.String extension, int fileType, java.lang.String mimeType, int mtpFormatCode) {
        android.media.MediaFile.addFileType(extension, fileType, mimeType);
        android.media.MediaFile.sFileTypeToFormatMap.put(extension, java.lang.Integer.valueOf(mtpFormatCode));
        android.media.MediaFile.sMimeTypeToFormatMap.put(mimeType, java.lang.Integer.valueOf(mtpFormatCode));
        android.media.MediaFile.sFormatToMimeTypeMap.put(mtpFormatCode, mimeType);
    }

    private static boolean isWMAEnabled() {
        java.util.List<android.media.DecoderCapabilities.AudioDecoder> decoders = android.media.DecoderCapabilities.getAudioDecoders();
        int count = decoders.size();
        for (int i = 0; i < count; i++) {
            android.media.DecoderCapabilities.AudioDecoder decoder = decoders.get(i);
            if (decoder == android.media.DecoderCapabilities.AudioDecoder.AUDIO_DECODER_WMA) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWMVEnabled() {
        java.util.List<android.media.DecoderCapabilities.VideoDecoder> decoders = android.media.DecoderCapabilities.getVideoDecoders();
        int count = decoders.size();
        for (int i = 0; i < count; i++) {
            android.media.DecoderCapabilities.VideoDecoder decoder = decoders.get(i);
            if (decoder == android.media.DecoderCapabilities.VideoDecoder.VIDEO_DECODER_WMV) {
                return true;
            }
        }
        return false;
    }

    static {
        android.media.MediaFile.addFileType("MP3", android.media.MediaFile.FILE_TYPE_MP3, "audio/mpeg", android.mtp.MtpConstants.FORMAT_MP3);
        android.media.MediaFile.addFileType("MPGA", android.media.MediaFile.FILE_TYPE_MP3, "audio/mpeg", android.mtp.MtpConstants.FORMAT_MP3);
        android.media.MediaFile.addFileType("M4A", android.media.MediaFile.FILE_TYPE_M4A, "audio/mp4", android.mtp.MtpConstants.FORMAT_MPEG);
        android.media.MediaFile.addFileType("WAV", android.media.MediaFile.FILE_TYPE_WAV, "audio/x-wav", android.mtp.MtpConstants.FORMAT_WAV);
        android.media.MediaFile.addFileType("AMR", android.media.MediaFile.FILE_TYPE_AMR, "audio/amr");
        android.media.MediaFile.addFileType("AWB", android.media.MediaFile.FILE_TYPE_AWB, "audio/amr-wb");
        if (android.media.MediaFile.isWMAEnabled()) {
            android.media.MediaFile.addFileType("WMA", android.media.MediaFile.FILE_TYPE_WMA, "audio/x-ms-wma", android.mtp.MtpConstants.FORMAT_WMA);
        }
        android.media.MediaFile.addFileType("OGG", android.media.MediaFile.FILE_TYPE_OGG, "audio/ogg", android.mtp.MtpConstants.FORMAT_OGG);
        android.media.MediaFile.addFileType("OGG", android.media.MediaFile.FILE_TYPE_OGG, "application/ogg", android.mtp.MtpConstants.FORMAT_OGG);
        android.media.MediaFile.addFileType("OGA", android.media.MediaFile.FILE_TYPE_OGG, "application/ogg", android.mtp.MtpConstants.FORMAT_OGG);
        android.media.MediaFile.addFileType("AAC", android.media.MediaFile.FILE_TYPE_AAC, "audio/aac", android.mtp.MtpConstants.FORMAT_AAC);
        android.media.MediaFile.addFileType("AAC", android.media.MediaFile.FILE_TYPE_AAC, "audio/aac-adts", android.mtp.MtpConstants.FORMAT_AAC);
        android.media.MediaFile.addFileType("MKA", android.media.MediaFile.FILE_TYPE_MKA, "audio/x-matroska");
        android.media.MediaFile.addFileType("MID", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("MIDI", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("XMF", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("RTTTL", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("SMF", android.media.MediaFile.FILE_TYPE_SMF, "audio/sp-midi");
        android.media.MediaFile.addFileType("IMY", android.media.MediaFile.FILE_TYPE_IMY, "audio/imelody");
        android.media.MediaFile.addFileType("RTX", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("OTA", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("MXMF", android.media.MediaFile.FILE_TYPE_MID, "audio/midi");
        android.media.MediaFile.addFileType("MPEG", android.media.MediaFile.FILE_TYPE_MP4, "video/mpeg", android.mtp.MtpConstants.FORMAT_MPEG);
        android.media.MediaFile.addFileType("MPG", android.media.MediaFile.FILE_TYPE_MP4, "video/mpeg", android.mtp.MtpConstants.FORMAT_MPEG);
        android.media.MediaFile.addFileType("MP4", android.media.MediaFile.FILE_TYPE_MP4, "video/mp4", android.mtp.MtpConstants.FORMAT_MPEG);
        android.media.MediaFile.addFileType("M4V", android.media.MediaFile.FILE_TYPE_M4V, "video/mp4", android.mtp.MtpConstants.FORMAT_MPEG);
        android.media.MediaFile.addFileType("MOV", android.media.MediaFile.FILE_TYPE_QT, "video/quicktime", android.mtp.MtpConstants.FORMAT_MPEG);
        android.media.MediaFile.addFileType("3GP", android.media.MediaFile.FILE_TYPE_3GPP, "video/3gpp", android.mtp.MtpConstants.FORMAT_3GP_CONTAINER);
        android.media.MediaFile.addFileType("3GPP", android.media.MediaFile.FILE_TYPE_3GPP, "video/3gpp", android.mtp.MtpConstants.FORMAT_3GP_CONTAINER);
        android.media.MediaFile.addFileType("3G2", android.media.MediaFile.FILE_TYPE_3GPP2, "video/3gpp2", android.mtp.MtpConstants.FORMAT_3GP_CONTAINER);
        android.media.MediaFile.addFileType("3GPP2", android.media.MediaFile.FILE_TYPE_3GPP2, "video/3gpp2", android.mtp.MtpConstants.FORMAT_3GP_CONTAINER);
        android.media.MediaFile.addFileType("MKV", android.media.MediaFile.FILE_TYPE_MKV, "video/x-matroska");
        android.media.MediaFile.addFileType("WEBM", android.media.MediaFile.FILE_TYPE_WEBM, "video/webm");
        android.media.MediaFile.addFileType("TS", android.media.MediaFile.FILE_TYPE_MP2TS, "video/mp2ts");
        android.media.MediaFile.addFileType("AVI", android.media.MediaFile.FILE_TYPE_AVI, "video/avi");
        if (android.media.MediaFile.isWMVEnabled()) {
            android.media.MediaFile.addFileType("WMV", android.media.MediaFile.FILE_TYPE_WMV, "video/x-ms-wmv", android.mtp.MtpConstants.FORMAT_WMV);
            android.media.MediaFile.addFileType("ASF", android.media.MediaFile.FILE_TYPE_ASF, "video/x-ms-asf");
        }
        android.media.MediaFile.addFileType("JPG", android.media.MediaFile.FILE_TYPE_JPEG, "image/jpeg", android.mtp.MtpConstants.FORMAT_EXIF_JPEG);
        android.media.MediaFile.addFileType("JPEG", android.media.MediaFile.FILE_TYPE_JPEG, "image/jpeg", android.mtp.MtpConstants.FORMAT_EXIF_JPEG);
        android.media.MediaFile.addFileType("GIF", android.media.MediaFile.FILE_TYPE_GIF, "image/gif", android.mtp.MtpConstants.FORMAT_GIF);
        android.media.MediaFile.addFileType("PNG", android.media.MediaFile.FILE_TYPE_PNG, "image/png", android.mtp.MtpConstants.FORMAT_PNG);
        android.media.MediaFile.addFileType("BMP", android.media.MediaFile.FILE_TYPE_BMP, "image/x-ms-bmp", android.mtp.MtpConstants.FORMAT_BMP);
        android.media.MediaFile.addFileType("WBMP", android.media.MediaFile.FILE_TYPE_WBMP, "image/vnd.wap.wbmp", android.mtp.MtpConstants.FORMAT_DEFINED);
        android.media.MediaFile.addFileType("WEBP", android.media.MediaFile.FILE_TYPE_WEBP, "image/webp", android.mtp.MtpConstants.FORMAT_DEFINED);
        android.media.MediaFile.addFileType("DNG", android.media.MediaFile.FILE_TYPE_DNG, "image/x-adobe-dng", android.mtp.MtpConstants.FORMAT_DNG);
        android.media.MediaFile.addFileType("CR2", android.media.MediaFile.FILE_TYPE_CR2, "image/x-canon-cr2", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("NEF", android.media.MediaFile.FILE_TYPE_NEF, "image/x-nikon-nef", android.mtp.MtpConstants.FORMAT_TIFF_EP);
        android.media.MediaFile.addFileType("NRW", android.media.MediaFile.FILE_TYPE_NRW, "image/x-nikon-nrw", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("ARW", android.media.MediaFile.FILE_TYPE_ARW, "image/x-sony-arw", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("RW2", android.media.MediaFile.FILE_TYPE_RW2, "image/x-panasonic-rw2", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("ORF", android.media.MediaFile.FILE_TYPE_ORF, "image/x-olympus-orf", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("RAF", android.media.MediaFile.FILE_TYPE_RAF, "image/x-fuji-raf", android.mtp.MtpConstants.FORMAT_DEFINED);
        android.media.MediaFile.addFileType("PEF", android.media.MediaFile.FILE_TYPE_PEF, "image/x-pentax-pef", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("SRW", android.media.MediaFile.FILE_TYPE_SRW, "image/x-samsung-srw", android.mtp.MtpConstants.FORMAT_TIFF);
        android.media.MediaFile.addFileType("M3U", android.media.MediaFile.FILE_TYPE_M3U, "audio/x-mpegurl", android.mtp.MtpConstants.FORMAT_M3U_PLAYLIST);
        android.media.MediaFile.addFileType("M3U", android.media.MediaFile.FILE_TYPE_M3U, "application/x-mpegurl", android.mtp.MtpConstants.FORMAT_M3U_PLAYLIST);
        android.media.MediaFile.addFileType("PLS", android.media.MediaFile.FILE_TYPE_PLS, "audio/x-scpls", android.mtp.MtpConstants.FORMAT_PLS_PLAYLIST);
        android.media.MediaFile.addFileType("WPL", android.media.MediaFile.FILE_TYPE_WPL, "application/vnd.ms-wpl", android.mtp.MtpConstants.FORMAT_WPL_PLAYLIST);
        android.media.MediaFile.addFileType("M3U8", android.media.MediaFile.FILE_TYPE_HTTPLIVE, "application/vnd.apple.mpegurl");
        android.media.MediaFile.addFileType("M3U8", android.media.MediaFile.FILE_TYPE_HTTPLIVE, "audio/mpegurl");
        android.media.MediaFile.addFileType("M3U8", android.media.MediaFile.FILE_TYPE_HTTPLIVE, "audio/x-mpegurl");
        android.media.MediaFile.addFileType("FL", android.media.MediaFile.FILE_TYPE_FL, "application/x-android-drm-fl");
        android.media.MediaFile.addFileType("TXT", android.media.MediaFile.FILE_TYPE_TEXT, "text/plain", android.mtp.MtpConstants.FORMAT_TEXT);
        android.media.MediaFile.addFileType("HTM", android.media.MediaFile.FILE_TYPE_HTML, "text/html", android.mtp.MtpConstants.FORMAT_HTML);
        android.media.MediaFile.addFileType("HTML", android.media.MediaFile.FILE_TYPE_HTML, "text/html", android.mtp.MtpConstants.FORMAT_HTML);
        android.media.MediaFile.addFileType("PDF", android.media.MediaFile.FILE_TYPE_PDF, "application/pdf");
        android.media.MediaFile.addFileType("DOC", android.media.MediaFile.FILE_TYPE_MS_WORD, "application/msword", android.mtp.MtpConstants.FORMAT_MS_WORD_DOCUMENT);
        android.media.MediaFile.addFileType("XLS", android.media.MediaFile.FILE_TYPE_MS_EXCEL, "application/vnd.ms-excel", android.mtp.MtpConstants.FORMAT_MS_EXCEL_SPREADSHEET);
        android.media.MediaFile.addFileType("PPT", android.media.MediaFile.FILE_TYPE_MS_POWERPOINT, "application/mspowerpoint", android.mtp.MtpConstants.FORMAT_MS_POWERPOINT_PRESENTATION);
        android.media.MediaFile.addFileType("FLAC", android.media.MediaFile.FILE_TYPE_FLAC, "audio/flac", android.mtp.MtpConstants.FORMAT_FLAC);
        android.media.MediaFile.addFileType("ZIP", android.media.MediaFile.FILE_TYPE_ZIP, "application/zip");
        android.media.MediaFile.addFileType("MPG", android.media.MediaFile.FILE_TYPE_MP2PS, "video/mp2p");
        android.media.MediaFile.addFileType("MPEG", android.media.MediaFile.FILE_TYPE_MP2PS, "video/mp2p");
    }

    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= android.media.MediaFile.FIRST_AUDIO_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_AUDIO_FILE_TYPE)) || ((fileType >= android.media.MediaFile.FIRST_MIDI_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_MIDI_FILE_TYPE));
    }

    public static boolean isVideoFileType(int fileType) {
        return ((fileType >= android.media.MediaFile.FIRST_VIDEO_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_VIDEO_FILE_TYPE)) || ((fileType >= android.media.MediaFile.FIRST_VIDEO_FILE_TYPE2) && (fileType <= android.media.MediaFile.LAST_VIDEO_FILE_TYPE2));
    }

    public static boolean isImageFileType(int fileType) {
        return ((fileType >= android.media.MediaFile.FIRST_IMAGE_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_IMAGE_FILE_TYPE)) || ((fileType >= android.media.MediaFile.FIRST_RAW_IMAGE_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_RAW_IMAGE_FILE_TYPE));
    }

    public static boolean isRawImageFileType(int fileType) {
        return (fileType >= android.media.MediaFile.FIRST_RAW_IMAGE_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_RAW_IMAGE_FILE_TYPE);
    }

    public static boolean isPlayListFileType(int fileType) {
        return (fileType >= android.media.MediaFile.FIRST_PLAYLIST_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_PLAYLIST_FILE_TYPE);
    }

    public static boolean isDrmFileType(int fileType) {
        return (fileType >= android.media.MediaFile.FIRST_DRM_FILE_TYPE) && (fileType <= android.media.MediaFile.LAST_DRM_FILE_TYPE);
    }

    public static android.media.MediaFile.MediaFileType getFileType(java.lang.String path) {
        int lastDot = path.lastIndexOf('.');
        if (lastDot < 0)
            return null;

        return android.media.MediaFile.sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(java.util.Locale.ROOT));
    }

    public static boolean isMimeTypeMedia(java.lang.String mimeType) {
        int fileType = android.media.MediaFile.getFileTypeForMimeType(mimeType);
        return ((android.media.MediaFile.isAudioFileType(fileType) || android.media.MediaFile.isVideoFileType(fileType)) || android.media.MediaFile.isImageFileType(fileType)) || android.media.MediaFile.isPlayListFileType(fileType);
    }

    // generates a title based on file name
    public static java.lang.String getFileTitle(java.lang.String path) {
        // extract file name after last slash
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash >= 0) {
            lastSlash++;
            if (lastSlash < path.length()) {
                path = path.substring(lastSlash);
            }
        }
        // truncate the file extension (if any)
        int lastDot = path.lastIndexOf('.');
        if (lastDot > 0) {
            path = path.substring(0, lastDot);
        }
        return path;
    }

    public static int getFileTypeForMimeType(java.lang.String mimeType) {
        java.lang.Integer value = android.media.MediaFile.sMimeTypeMap.get(mimeType);
        return value == null ? 0 : value.intValue();
    }

    public static java.lang.String getMimeTypeForFile(java.lang.String path) {
        android.media.MediaFile.MediaFileType mediaFileType = android.media.MediaFile.getFileType(path);
        return mediaFileType == null ? null : mediaFileType.mimeType;
    }

    public static int getFormatCode(java.lang.String fileName, java.lang.String mimeType) {
        if (mimeType != null) {
            java.lang.Integer value = android.media.MediaFile.sMimeTypeToFormatMap.get(mimeType);
            if (value != null) {
                return value.intValue();
            }
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            java.lang.String extension = fileName.substring(lastDot + 1).toUpperCase(java.util.Locale.ROOT);
            java.lang.Integer value = android.media.MediaFile.sFileTypeToFormatMap.get(extension);
            if (value != null) {
                return value.intValue();
            }
        }
        return android.mtp.MtpConstants.FORMAT_UNDEFINED;
    }

    public static java.lang.String getMimeTypeForFormatCode(int formatCode) {
        return android.media.MediaFile.sFormatToMimeTypeMap.get(formatCode);
    }
}

