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
 * MediaExtractor facilitates extraction of demuxed, typically encoded,  media data
 * from a data source.
 * <p>It is generally used like this:
 * <pre>
 * MediaExtractor extractor = new MediaExtractor();
 * extractor.setDataSource(...);
 * int numTracks = extractor.getTrackCount();
 * for (int i = 0; i &lt; numTracks; ++i) {
 *   MediaFormat format = extractor.getTrackFormat(i);
 *   String mime = format.getString(MediaFormat.KEY_MIME);
 *   if (weAreInterestedInThisTrack) {
 *     extractor.selectTrack(i);
 *   }
 * }
 * ByteBuffer inputBuffer = ByteBuffer.allocate(...)
 * while (extractor.readSampleData(inputBuffer, ...) &gt;= 0) {
 *   int trackIndex = extractor.getSampleTrackIndex();
 *   long presentationTimeUs = extractor.getSampleTime();
 *   ...
 *   extractor.advance();
 * }
 *
 * extractor.release();
 * extractor = null;
 * </pre>
 */
public final class MediaExtractor {
    public MediaExtractor() {
        native_setup();
    }

    /**
     * Sets the data source (MediaDataSource) to use.
     *
     * @param dataSource
     * 		the MediaDataSource for the media you want to extract from
     * @throws IllegalArgumentException
     * 		if dataSource is invalid.
     */
    public final native void setDataSource(@android.annotation.NonNull
    android.media.MediaDataSource dataSource) throws java.io.IOException;

    /**
     * Sets the data source as a content Uri.
     *
     * @param context
     * 		the Context to use when resolving the Uri
     * @param uri
     * 		the Content URI of the data you want to extract from.
     * @param headers
     * 		the headers to be sent together with the request for the data.
     * 		This can be {@code null} if no specific headers are to be sent with the
     * 		request.
     */
    public final void setDataSource(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        java.lang.String scheme = uri.getScheme();
        if ((scheme == null) || scheme.equals("file")) {
            setDataSource(uri.getPath());
            return;
        }
        android.content.res.AssetFileDescriptor fd = null;
        try {
            android.content.ContentResolver resolver = context.getContentResolver();
            fd = resolver.openAssetFileDescriptor(uri, "r");
            if (fd == null) {
                return;
            }
            // Note: using getDeclaredLength so that our behavior is the same
            // as previous versions when the content provider is returning
            // a full file.
            if (fd.getDeclaredLength() < 0) {
                setDataSource(fd.getFileDescriptor());
            } else {
                setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
            }
            return;
        } catch (java.lang.SecurityException ex) {
        } catch (java.io.IOException ex) {
        } finally {
            if (fd != null) {
                fd.close();
            }
        }
        setDataSource(uri.toString(), headers);
    }

    /**
     * Sets the data source (file-path or http URL) to use.
     *
     * @param path
     * 		the path of the file, or the http URL
     * @param headers
     * 		the headers associated with the http request for the stream you want to play.
     * 		This can be {@code null} if no specific headers are to be sent with the
     * 		request.
     */
    public final void setDataSource(@android.annotation.NonNull
    java.lang.String path, @android.annotation.Nullable
    java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        java.lang.String[] keys = null;
        java.lang.String[] values = null;
        if (headers != null) {
            keys = new java.lang.String[headers.size()];
            values = new java.lang.String[headers.size()];
            int i = 0;
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                ++i;
            }
        }
        nativeSetDataSource(android.media.MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, keys, values);
    }

    private final native void nativeSetDataSource(@android.annotation.NonNull
    android.os.IBinder httpServiceBinder, @android.annotation.NonNull
    java.lang.String path, @android.annotation.Nullable
    java.lang.String[] keys, @android.annotation.Nullable
    java.lang.String[] values) throws java.io.IOException;

    /**
     * Sets the data source (file-path or http URL) to use.
     *
     * @param path
     * 		the path of the file, or the http URL of the stream
     * 		
     * 		<p>When <code>path</code> refers to a local file, the file may actually be opened by a
     * 		process other than the calling application.  This implies that the pathname
     * 		should be an absolute path (as any other process runs with unspecified current working
     * 		directory), and that the pathname should reference a world-readable file.
     * 		As an alternative, the application could first open the file for reading,
     * 		and then use the file descriptor form {@link #setDataSource(FileDescriptor)}.
     */
    public final void setDataSource(@android.annotation.NonNull
    java.lang.String path) throws java.io.IOException {
        nativeSetDataSource(android.media.MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, null, null);
    }

    /**
     * Sets the data source (AssetFileDescriptor) to use. It is the caller's
     * responsibility to close the file descriptor. It is safe to do so as soon
     * as this call returns.
     *
     * @param afd
     * 		the AssetFileDescriptor for the file you want to extract from.
     */
    public final void setDataSource(@android.annotation.NonNull
    android.content.res.AssetFileDescriptor afd) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        com.android.internal.util.Preconditions.checkNotNull(afd);
        // Note: using getDeclaredLength so that our behavior is the same
        // as previous versions when the content provider is returning
        // a full file.
        if (afd.getDeclaredLength() < 0) {
            setDataSource(afd.getFileDescriptor());
        } else {
            setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
        }
    }

    /**
     * Sets the data source (FileDescriptor) to use. It is the caller's responsibility
     * to close the file descriptor. It is safe to do so as soon as this call returns.
     *
     * @param fd
     * 		the FileDescriptor for the file you want to extract from.
     */
    public final void setDataSource(@android.annotation.NonNull
    java.io.FileDescriptor fd) throws java.io.IOException {
        setDataSource(fd, 0, 0x7ffffffffffffffL);
    }

    /**
     * Sets the data source (FileDescriptor) to use.  The FileDescriptor must be
     * seekable (N.B. a LocalSocket is not seekable). It is the caller's responsibility
     * to close the file descriptor. It is safe to do so as soon as this call returns.
     *
     * @param fd
     * 		the FileDescriptor for the file you want to extract from.
     * @param offset
     * 		the offset into the file where the data to be extracted starts, in bytes
     * @param length
     * 		the length in bytes of the data to be extracted
     */
    public final native void setDataSource(@android.annotation.NonNull
    java.io.FileDescriptor fd, long offset, long length) throws java.io.IOException;

    @java.lang.Override
    protected void finalize() {
        native_finalize();
    }

    /**
     * Make sure you call this when you're done to free up any resources
     * instead of relying on the garbage collector to do this for you at
     * some point in the future.
     */
    public final native void release();

    /**
     * Count the number of tracks found in the data source.
     */
    public final native int getTrackCount();

    /**
     * Extract DRM initialization data if it exists
     *
     * @return DRM initialization data in the content, or {@code null}
    if no recognizable DRM format is found;
     * @see DrmInitData
     */
    public android.media.DrmInitData getDrmInitData() {
        java.util.Map<java.lang.String, java.lang.Object> formatMap = getFileFormatNative();
        if (formatMap == null) {
            return null;
        }
        if (formatMap.containsKey("pssh")) {
            java.util.Map<java.util.UUID, byte[]> psshMap = getPsshInfo();
            final java.util.Map<java.util.UUID, android.media.DrmInitData.SchemeInitData> initDataMap = new java.util.HashMap<java.util.UUID, android.media.DrmInitData.SchemeInitData>();
            for (java.util.Map.Entry<java.util.UUID, byte[]> e : psshMap.entrySet()) {
                java.util.UUID uuid = e.getKey();
                byte[] data = e.getValue();
                initDataMap.put(uuid, new android.media.DrmInitData.SchemeInitData("cenc", data));
            }
            return new android.media.DrmInitData() {
                public android.media.DrmInitData.SchemeInitData get(java.util.UUID schemeUuid) {
                    return initDataMap.get(schemeUuid);
                }
            };
        } else {
            int numTracks = getTrackCount();
            for (int i = 0; i < numTracks; ++i) {
                java.util.Map<java.lang.String, java.lang.Object> trackFormatMap = getTrackFormatNative(i);
                if (!trackFormatMap.containsKey("crypto-key")) {
                    continue;
                }
                java.nio.ByteBuffer buf = ((java.nio.ByteBuffer) (trackFormatMap.get("crypto-key")));
                buf.rewind();
                final byte[] data = new byte[buf.remaining()];
                buf.get(data);
                return new android.media.DrmInitData() {
                    public android.media.DrmInitData.SchemeInitData get(java.util.UUID schemeUuid) {
                        return new android.media.DrmInitData.SchemeInitData("webm", data);
                    }
                };
            }
        }
        return null;
    }

    /**
     * Get the PSSH info if present.
     *
     * @return a map of uuid-to-bytes, with the uuid specifying
    the crypto scheme, and the bytes being the data specific to that scheme.
    This can be {@code null} if the source does not contain PSSH info.
     */
    @android.annotation.Nullable
    public java.util.Map<java.util.UUID, byte[]> getPsshInfo() {
        java.util.Map<java.util.UUID, byte[]> psshMap = null;
        java.util.Map<java.lang.String, java.lang.Object> formatMap = getFileFormatNative();
        if ((formatMap != null) && formatMap.containsKey("pssh")) {
            java.nio.ByteBuffer rawpssh = ((java.nio.ByteBuffer) (formatMap.get("pssh")));
            rawpssh.order(java.nio.ByteOrder.nativeOrder());
            rawpssh.rewind();
            formatMap.remove("pssh");
            // parse the flat pssh bytebuffer into something more manageable
            psshMap = new java.util.HashMap<java.util.UUID, byte[]>();
            while (rawpssh.remaining() > 0) {
                rawpssh.order(java.nio.ByteOrder.BIG_ENDIAN);
                long msb = rawpssh.getLong();
                long lsb = rawpssh.getLong();
                java.util.UUID uuid = new java.util.UUID(msb, lsb);
                rawpssh.order(java.nio.ByteOrder.nativeOrder());
                int datalen = rawpssh.getInt();
                byte[] psshdata = new byte[datalen];
                rawpssh.get(psshdata);
                psshMap.put(uuid, psshdata);
            } 
        }
        return psshMap;
    }

    @android.annotation.NonNull
    private native java.util.Map<java.lang.String, java.lang.Object> getFileFormatNative();

    /**
     * Get the track format at the specified index.
     *
     * More detail on the representation can be found at {@link android.media.MediaCodec}
     * <p>
     * The following table summarizes support for format keys across android releases:
     *
     * <table style="width: 0%">
     *  <thead>
     *   <tr>
     *    <th rowspan=2>OS Version(s)</th>
     *    <td colspan=3>{@code MediaFormat} keys used for</th>
     *   </tr><tr>
     *    <th>All Tracks</th>
     *    <th>Audio Tracks</th>
     *    <th>Video Tracks</th>
     *   </tr>
     *  </thead>
     *  <tbody>
     *   <tr>
     *    <td>{@link android.os.Build.VERSION_CODES#JELLY_BEAN}</td>
     *    <td rowspan=8>{@link MediaFormat#KEY_MIME},<br>
     *        {@link MediaFormat#KEY_DURATION},<br>
     *        {@link MediaFormat#KEY_MAX_INPUT_SIZE}</td>
     *    <td rowspan=5>{@link MediaFormat#KEY_SAMPLE_RATE},<br>
     *        {@link MediaFormat#KEY_CHANNEL_COUNT},<br>
     *        {@link MediaFormat#KEY_CHANNEL_MASK},<br>
     *        gapless playback information<sup>.mp3, .mp4</sup>,<br>
     *        {@link MediaFormat#KEY_IS_ADTS}<sup>AAC if streaming</sup>,<br>
     *        codec-specific data<sup>AAC, Vorbis</sup></td>
     *    <td rowspan=2>{@link MediaFormat#KEY_WIDTH},<br>
     *        {@link MediaFormat#KEY_HEIGHT},<br>
     *        codec-specific data<sup>AVC, MPEG4</sup></td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR1}</td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}</td>
     *    <td rowspan=3>as above, plus<br>
     *        Pixel aspect ratio information<sup>AVC, *</sup></td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#KITKAT}</td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#KITKAT_WATCH}</td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#LOLLIPOP}</td>
     *    <td rowspan=2>as above, plus<br>
     *        {@link MediaFormat#KEY_BIT_RATE}<sup>AAC</sup>,<br>
     *        codec-specific data<sup>Opus</sup></td>
     *    <td rowspan=2>as above, plus<br>
     *        {@link MediaFormat#KEY_ROTATION}<sup>.mp4</sup>,<br>
     *        {@link MediaFormat#KEY_BIT_RATE}<sup>MPEG4</sup>,<br>
     *        codec-specific data<sup>HEVC</sup></td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#LOLLIPOP_MR1}</td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#M}</td>
     *    <td>as above, plus<br>
     *        gapless playback information<sup>Opus</sup></td>
     *    <td>as above, plus<br>
     *        {@link MediaFormat#KEY_FRAME_RATE} (integer)</td>
     *   </tr><tr>
     *    <td>{@link android.os.Build.VERSION_CODES#N}</td>
     *    <td>as above, plus<br>
     *        {@link MediaFormat#KEY_TRACK_ID},<br>
     *        <!-- {link MediaFormat#KEY_MAX_BIT_RATE}<sup>#, .mp4</sup>,<br> -->
     *        {@link MediaFormat#KEY_BIT_RATE}<sup>#, .mp4</sup></td>
     *    <td>as above, plus<br>
     *        {@link MediaFormat#KEY_PCM_ENCODING},<br>
     *        {@link MediaFormat#KEY_PROFILE}<sup>AAC</sup></td>
     *    <td>as above, plus<br>
     *        {@link MediaFormat#KEY_HDR_STATIC_INFO}<sup>#, .webm</sup>,<br>
     *        {@link MediaFormat#KEY_COLOR_STANDARD}<sup>#</sup>,<br>
     *        {@link MediaFormat#KEY_COLOR_TRANSFER}<sup>#</sup>,<br>
     *        {@link MediaFormat#KEY_COLOR_RANGE}<sup>#</sup>,<br>
     *        {@link MediaFormat#KEY_PROFILE}<sup>MPEG2, H.263, MPEG4, AVC, HEVC, VP9</sup>,<br>
     *        {@link MediaFormat#KEY_LEVEL}<sup>H.263, MPEG4, AVC, HEVC, VP9</sup>,<br>
     *        codec-specific data<sup>VP9</sup></td>
     *   </tr>
     *   <tr>
     *    <td colspan=4>
     *     <p class=note><strong>Notes:</strong><br>
     *      #: container-specified value only.<br>
     *      .mp4, .webm&hellip;: for listed containers<br>
     *      MPEG4, AAC&hellip;: for listed codecs
     *    </td>
     *   </tr><tr>
     *    <td colspan=4>
     *     <p class=note>Note that that level information contained in the container many times
     *     does not match the level of the actual bitstream. You may want to clear the level using
     *     {@code MediaFormat.setString(KEY_LEVEL, null)} before using the track format to find a
     *     decoder that can play back a particular track.
     *    </td>
     *   </tr><tr>
     *    <td colspan=4>
     *     <p class=note><strong>*Pixel (sample) aspect ratio</strong> is returned in the following
     *     keys. The display width can be calculated for example as:
     *     <p align=center>
     *     display-width = display-height * crop-width / crop-height * sar-width / sar-height
     *    </td>
     *   </tr><tr>
     *    <th>Format Key</th><th>Value Type</th><th colspan=2>Description</th>
     *   </tr><tr>
     *    <td>{@code "sar-width"}</td><td>Integer</td><td colspan=2>Pixel aspect ratio width</td>
     *   </tr><tr>
     *    <td>{@code "sar-height"}</td><td>Integer</td><td colspan=2>Pixel aspect ratio height</td>
     *   </tr>
     *  </tbody>
     * </table>
     */
    @android.annotation.NonNull
    public android.media.MediaFormat getTrackFormat(int index) {
        return new android.media.MediaFormat(getTrackFormatNative(index));
    }

    @android.annotation.NonNull
    private native java.util.Map<java.lang.String, java.lang.Object> getTrackFormatNative(int index);

    /**
     * Subsequent calls to {@link #readSampleData}, {@link #getSampleTrackIndex} and
     * {@link #getSampleTime} only retrieve information for the subset of tracks
     * selected.
     * Selecting the same track multiple times has no effect, the track is
     * only selected once.
     */
    public native void selectTrack(int index);

    /**
     * Subsequent calls to {@link #readSampleData}, {@link #getSampleTrackIndex} and
     * {@link #getSampleTime} only retrieve information for the subset of tracks
     * selected.
     */
    public native void unselectTrack(int index);

    /**
     * If possible, seek to a sync sample at or before the specified time
     */
    public static final int SEEK_TO_PREVIOUS_SYNC = 0;

    /**
     * If possible, seek to a sync sample at or after the specified time
     */
    public static final int SEEK_TO_NEXT_SYNC = 1;

    /**
     * If possible, seek to the sync sample closest to the specified time
     */
    public static final int SEEK_TO_CLOSEST_SYNC = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC, android.media.MediaExtractor.SEEK_TO_NEXT_SYNC, android.media.MediaExtractor.SEEK_TO_CLOSEST_SYNC })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SeekMode {}

    /**
     * All selected tracks seek near the requested time according to the
     * specified mode.
     */
    public native void seekTo(long timeUs, @android.media.MediaExtractor.SeekMode
    int mode);

    /**
     * Advance to the next sample. Returns false if no more sample data
     * is available (end of stream).
     *
     * When extracting a local file, the behaviors of {@link #advance} and
     * {@link #readSampleData} are undefined in presence of concurrent
     * writes to the same local file; more specifically, end of stream
     * could be signalled earlier than expected.
     */
    public native boolean advance();

    /**
     * Retrieve the current encoded sample and store it in the byte buffer
     * starting at the given offset.
     * <p>
     * <b>Note:</b>As of API 21, on success the position and limit of
     * {@code byteBuf} is updated to point to the data just read.
     *
     * @param byteBuf
     * 		the destination byte buffer
     * @return the sample size (or -1 if no more samples are available).
     */
    public native int readSampleData(@android.annotation.NonNull
    java.nio.ByteBuffer byteBuf, int offset);

    /**
     * Returns the track index the current sample originates from (or -1
     * if no more samples are available)
     */
    public native int getSampleTrackIndex();

    /**
     * Returns the current sample's presentation time in microseconds.
     * or -1 if no more samples are available.
     */
    public native long getSampleTime();

    // Keep these in sync with their equivalents in NuMediaExtractor.h
    /**
     * The sample is a sync sample (or in {@link MediaCodec}'s terminology
     * it is a key frame.)
     *
     * @see MediaCodec#BUFFER_FLAG_KEY_FRAME
     */
    public static final int SAMPLE_FLAG_SYNC = 1;

    /**
     * The sample is (at least partially) encrypted, see also the documentation
     * for {@link android.media.MediaCodec#queueSecureInputBuffer}
     */
    public static final int SAMPLE_FLAG_ENCRYPTED = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, value = { android.media.MediaExtractor.SAMPLE_FLAG_SYNC, android.media.MediaExtractor.SAMPLE_FLAG_ENCRYPTED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SampleFlag {}

    /**
     * Returns the current sample's flags.
     */
    @android.media.MediaExtractor.SampleFlag
    public native int getSampleFlags();

    /**
     * If the sample flags indicate that the current sample is at least
     * partially encrypted, this call returns relevant information about
     * the structure of the sample data required for decryption.
     *
     * @param info
     * 		The android.media.MediaCodec.CryptoInfo structure
     * 		to be filled in.
     * @return true iff the sample flags contain {@link #SAMPLE_FLAG_ENCRYPTED}
     */
    public native boolean getSampleCryptoInfo(@android.annotation.NonNull
    android.media.MediaCodec.CryptoInfo info);

    /**
     * Returns an estimate of how much data is presently cached in memory
     * expressed in microseconds. Returns -1 if that information is unavailable
     * or not applicable (no cache).
     */
    public native long getCachedDuration();

    /**
     * Returns true iff we are caching data and the cache has reached the
     * end of the data stream (for now, a future seek may of course restart
     * the fetching of data).
     * This API only returns a meaningful result if {@link #getCachedDuration}
     * indicates the presence of a cache, i.e. does NOT return -1.
     */
    public native boolean hasCacheReachedEndOfStream();

    private static final native void native_init();

    private final native void native_setup();

    private final native void native_finalize();

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaExtractor.native_init();
    }

    private long mNativeContext;
}

