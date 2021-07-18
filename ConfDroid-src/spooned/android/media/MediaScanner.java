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
 * Internal service helper that no-one should use directly.
 *
 * The way the scan currently works is:
 * - The Java MediaScannerService creates a MediaScanner (this class), and calls
 *   MediaScanner.scanDirectories on it.
 * - scanDirectories() calls the native processDirectory() for each of the specified directories.
 * - the processDirectory() JNI method wraps the provided mediascanner client in a native
 *   'MyMediaScannerClient' class, then calls processDirectory() on the native MediaScanner
 *   object (which got created when the Java MediaScanner was created).
 * - native MediaScanner.processDirectory() calls
 *   doProcessDirectory(), which recurses over the folder, and calls
 *   native MyMediaScannerClient.scanFile() for every file whose extension matches.
 * - native MyMediaScannerClient.scanFile() calls back on Java MediaScannerClient.scanFile,
 *   which calls doScanFile, which after some setup calls back down to native code, calling
 *   MediaScanner.processFile().
 * - MediaScanner.processFile() calls one of several methods, depending on the type of the
 *   file: parseMP3, parseMP4, parseMidi, parseOgg or parseWMA.
 * - each of these methods gets metadata key/value pairs from the file, and repeatedly
 *   calls native MyMediaScannerClient.handleStringTag, which calls back up to its Java
 *   counterparts in this file.
 * - Java handleStringTag() gathers the key/value pairs that it's interested in.
 * - once processFile returns and we're back in Java code in doScanFile(), it calls
 *   Java MyMediaScannerClient.endFile(), which takes all the data that's been
 *   gathered and inserts an entry in to the database.
 *
 * In summary:
 * Java MediaScannerService calls
 * Java MediaScanner scanDirectories, which calls
 * Java MediaScanner processDirectory (native method), which calls
 * native MediaScanner processDirectory, which calls
 * native MyMediaScannerClient scanFile, which calls
 * Java MyMediaScannerClient scanFile, which calls
 * Java MediaScannerClient doScanFile, which calls
 * Java MediaScanner processFile (native method), which calls
 * native MediaScanner processFile, which calls
 * native parseMP3, parseMP4, parseMidi, parseOgg or parseWMA, which calls
 * native MyMediaScanner handleStringTag, which calls
 * Java MyMediaScanner handleStringTag.
 * Once MediaScanner processFile returns, an entry is inserted in to the database.
 *
 * The MediaScanner class is not thread-safe, so it should only be used in a single threaded manner.
 *
 * {@hide }
 */
public class MediaScanner implements java.lang.AutoCloseable {
    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaScanner.native_init();
    }

    private static final java.lang.String TAG = "MediaScanner";

    private static final java.lang.String[] FILES_PRESCAN_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID// 0
    , android.provider.MediaStore.Files.FileColumns.DATA// 1
    , android.provider.MediaStore.Files.FileColumns.FORMAT// 2
    , android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED// 3
     };

    private static final java.lang.String[] ID_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID };

    private static final int FILES_PRESCAN_ID_COLUMN_INDEX = 0;

    private static final int FILES_PRESCAN_PATH_COLUMN_INDEX = 1;

    private static final int FILES_PRESCAN_FORMAT_COLUMN_INDEX = 2;

    private static final int FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX = 3;

    private static final java.lang.String[] PLAYLIST_MEMBERS_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Audio.Playlists.Members.PLAYLIST_ID// 0
     };

    private static final int ID_PLAYLISTS_COLUMN_INDEX = 0;

    private static final int PATH_PLAYLISTS_COLUMN_INDEX = 1;

    private static final int DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX = 2;

    private static final java.lang.String RINGTONES_DIR = "/ringtones/";

    private static final java.lang.String NOTIFICATIONS_DIR = "/notifications/";

    private static final java.lang.String ALARMS_DIR = "/alarms/";

    private static final java.lang.String MUSIC_DIR = "/music/";

    private static final java.lang.String PODCAST_DIR = "/podcasts/";

    public static final java.lang.String SCANNED_BUILD_PREFS_NAME = "MediaScanBuild";

    public static final java.lang.String LAST_INTERNAL_SCAN_FINGERPRINT = "lastScanFingerprint";

    private static final java.lang.String SYSTEM_SOUNDS_DIR = "/system/media/audio";

    private static java.lang.String sLastInternalScanFingerprint;

    private static final java.lang.String[] ID3_GENRES = // 148 and up don't seem to have been defined yet.
    new java.lang.String[]{ // ID3v1 Genres
    "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", // The following genres are Winamp extensions
    "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", // The following ones seem to be fairly widely supported as well
    "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "Britpop", null, "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop" }// 148 and up don't seem to have been defined yet.
    ;

    private long mNativeContext;

    private final android.content.Context mContext;

    private final java.lang.String mPackageName;

    private final java.lang.String mVolumeName;

    private final android.content.ContentProviderClient mMediaProvider;

    private final android.net.Uri mAudioUri;

    private final android.net.Uri mVideoUri;

    private final android.net.Uri mImagesUri;

    private final android.net.Uri mThumbsUri;

    private final android.net.Uri mPlaylistsUri;

    private final android.net.Uri mFilesUri;

    private final android.net.Uri mFilesUriNoNotify;

    private final boolean mProcessPlaylists;

    private final boolean mProcessGenres;

    private int mMtpObjectHandle;

    private final java.util.concurrent.atomic.AtomicBoolean mClosed = new java.util.concurrent.atomic.AtomicBoolean();

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    /**
     * whether to use bulk inserts or individual inserts for each item
     */
    private static final boolean ENABLE_BULK_INSERTS = true;

    // used when scanning the image database so we know whether we have to prune
    // old thumbnail files
    private int mOriginalCount;

    /**
     * Whether the scanner has set a default sound for the ringer ringtone.
     */
    private boolean mDefaultRingtoneSet;

    /**
     * Whether the scanner has set a default sound for the notification ringtone.
     */
    private boolean mDefaultNotificationSet;

    /**
     * Whether the scanner has set a default sound for the alarm ringtone.
     */
    private boolean mDefaultAlarmSet;

    /**
     * The filename for the default sound for the ringer ringtone.
     */
    private java.lang.String mDefaultRingtoneFilename;

    /**
     * The filename for the default sound for the notification ringtone.
     */
    private java.lang.String mDefaultNotificationFilename;

    /**
     * The filename for the default sound for the alarm ringtone.
     */
    private java.lang.String mDefaultAlarmAlertFilename;

    /**
     * The prefix for system properties that define the default sound for
     * ringtones. Concatenate the name of the setting from Settings
     * to get the full system property.
     */
    private static final java.lang.String DEFAULT_RINGTONE_PROPERTY_PREFIX = "ro.config.";

    private final android.graphics.BitmapFactory.Options mBitmapOptions = new android.graphics.BitmapFactory.Options();

    private static class FileEntry {
        long mRowId;

        java.lang.String mPath;

        long mLastModified;

        int mFormat;

        boolean mLastModifiedChanged;

        FileEntry(long rowId, java.lang.String path, long lastModified, int format) {
            mRowId = rowId;
            mPath = path;
            mLastModified = lastModified;
            mFormat = format;
            mLastModifiedChanged = false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (mPath + " mRowId: ") + mRowId;
        }
    }

    private static class PlaylistEntry {
        java.lang.String path;

        long bestmatchid;

        int bestmatchlevel;
    }

    private final java.util.ArrayList<android.media.MediaScanner.PlaylistEntry> mPlaylistEntries = new java.util.ArrayList<>();

    private final java.util.ArrayList<android.media.MediaScanner.FileEntry> mPlayLists = new java.util.ArrayList<>();

    private android.media.MediaInserter mMediaInserter;

    private android.drm.DrmManagerClient mDrmManagerClient = null;

    public MediaScanner(android.content.Context c, java.lang.String volumeName) {
        native_setup();
        mContext = c;
        mPackageName = c.getPackageName();
        mVolumeName = volumeName;
        mBitmapOptions.inSampleSize = 1;
        mBitmapOptions.inJustDecodeBounds = true;
        setDefaultRingtoneFileNames();
        mMediaProvider = mContext.getContentResolver().acquireContentProviderClient(android.provider.MediaStore.AUTHORITY);
        if (android.media.MediaScanner.sLastInternalScanFingerprint == null) {
            final android.content.SharedPreferences scanSettings = mContext.getSharedPreferences(android.media.MediaScanner.SCANNED_BUILD_PREFS_NAME, android.content.Context.MODE_PRIVATE);
            android.media.MediaScanner.sLastInternalScanFingerprint = scanSettings.getString(android.media.MediaScanner.LAST_INTERNAL_SCAN_FINGERPRINT, new java.lang.String());
        }
        mAudioUri = android.provider.MediaStore.Audio.Media.getContentUri(volumeName);
        mVideoUri = android.provider.MediaStore.Video.Media.getContentUri(volumeName);
        mImagesUri = android.provider.MediaStore.Images.Media.getContentUri(volumeName);
        mThumbsUri = android.provider.MediaStore.Images.Thumbnails.getContentUri(volumeName);
        mFilesUri = android.provider.MediaStore.Files.getContentUri(volumeName);
        mFilesUriNoNotify = mFilesUri.buildUpon().appendQueryParameter("nonotify", "1").build();
        if (!volumeName.equals("internal")) {
            // we only support playlists on external media
            mProcessPlaylists = true;
            mProcessGenres = true;
            mPlaylistsUri = android.provider.MediaStore.Audio.Playlists.getContentUri(volumeName);
        } else {
            mProcessPlaylists = false;
            mProcessGenres = false;
            mPlaylistsUri = null;
        }
        final java.util.Locale locale = mContext.getResources().getConfiguration().locale;
        if (locale != null) {
            java.lang.String language = locale.getLanguage();
            java.lang.String country = locale.getCountry();
            if (language != null) {
                if (country != null) {
                    setLocale((language + "_") + country);
                } else {
                    setLocale(language);
                }
            }
        }
        mCloseGuard.open("close");
    }

    private void setDefaultRingtoneFileNames() {
        mDefaultRingtoneFilename = android.os.SystemProperties.get(android.media.MediaScanner.DEFAULT_RINGTONE_PROPERTY_PREFIX + android.provider.Settings.System.RINGTONE);
        mDefaultNotificationFilename = android.os.SystemProperties.get(android.media.MediaScanner.DEFAULT_RINGTONE_PROPERTY_PREFIX + android.provider.Settings.System.NOTIFICATION_SOUND);
        mDefaultAlarmAlertFilename = android.os.SystemProperties.get(android.media.MediaScanner.DEFAULT_RINGTONE_PROPERTY_PREFIX + android.provider.Settings.System.ALARM_ALERT);
    }

    private final android.media.MediaScanner.MyMediaScannerClient mClient = new android.media.MediaScanner.MyMediaScannerClient();

    private boolean isDrmEnabled() {
        java.lang.String prop = android.os.SystemProperties.get("drm.service.enabled");
        return (prop != null) && prop.equals("true");
    }

    private class MyMediaScannerClient implements android.media.MediaScannerClient {
        private final java.text.SimpleDateFormat mDateFormatter;

        private java.lang.String mArtist;

        private java.lang.String mAlbumArtist;// use this if mArtist is missing


        private java.lang.String mAlbum;

        private java.lang.String mTitle;

        private java.lang.String mComposer;

        private java.lang.String mGenre;

        private java.lang.String mMimeType;

        private int mFileType;

        private int mTrack;

        private int mYear;

        private int mDuration;

        private java.lang.String mPath;

        private long mDate;

        private long mLastModified;

        private long mFileSize;

        private java.lang.String mWriter;

        private int mCompilation;

        private boolean mIsDrm;

        private boolean mNoMedia;// flag to suppress file from appearing in media tables


        private int mWidth;

        private int mHeight;

        public MyMediaScannerClient() {
            mDateFormatter = new java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss");
            mDateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        }

        public android.media.MediaScanner.FileEntry beginFile(java.lang.String path, java.lang.String mimeType, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            mMimeType = mimeType;
            mFileType = 0;
            mFileSize = fileSize;
            mIsDrm = false;
            if (!isDirectory) {
                if ((!noMedia) && android.media.MediaScanner.isNoMediaFile(path)) {
                    noMedia = true;
                }
                mNoMedia = noMedia;
                // try mimeType first, if it is specified
                if (mimeType != null) {
                    mFileType = android.media.MediaFile.getFileTypeForMimeType(mimeType);
                }
                // if mimeType was not specified, compute file type based on file extension.
                if (mFileType == 0) {
                    android.media.MediaFile.MediaFileType mediaFileType = android.media.MediaFile.getFileType(path);
                    if (mediaFileType != null) {
                        mFileType = mediaFileType.fileType;
                        if (mMimeType == null) {
                            mMimeType = mediaFileType.mimeType;
                        }
                    }
                }
                if (isDrmEnabled() && android.media.MediaFile.isDrmFileType(mFileType)) {
                    mFileType = getFileTypeFromDrm(path);
                }
            }
            android.media.MediaScanner.FileEntry entry = makeEntryFor(path);
            // add some slack to avoid a rounding error
            long delta = (entry != null) ? lastModified - entry.mLastModified : 0;
            boolean wasModified = (delta > 1) || (delta < (-1));
            if ((entry == null) || wasModified) {
                if (wasModified) {
                    entry.mLastModified = lastModified;
                } else {
                    entry = new android.media.MediaScanner.FileEntry(0, path, lastModified, isDirectory ? android.mtp.MtpConstants.FORMAT_ASSOCIATION : 0);
                }
                entry.mLastModifiedChanged = true;
            }
            if (mProcessPlaylists && android.media.MediaFile.isPlayListFileType(mFileType)) {
                mPlayLists.add(entry);
                // we don't process playlists in the main scan, so return null
                return null;
            }
            // clear all the metadata
            mArtist = null;
            mAlbumArtist = null;
            mAlbum = null;
            mTitle = null;
            mComposer = null;
            mGenre = null;
            mTrack = 0;
            mYear = 0;
            mDuration = 0;
            mPath = path;
            mDate = 0;
            mLastModified = lastModified;
            mWriter = null;
            mCompilation = 0;
            mWidth = 0;
            mHeight = 0;
            return entry;
        }

        @java.lang.Override
        public void scanFile(java.lang.String path, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            // This is the callback funtion from native codes.
            // Log.v(TAG, "scanFile: "+path);
            doScanFile(path, null, lastModified, fileSize, isDirectory, false, noMedia);
        }

        public android.net.Uri doScanFile(java.lang.String path, java.lang.String mimeType, long lastModified, long fileSize, boolean isDirectory, boolean scanAlways, boolean noMedia) {
            android.net.Uri result = null;
            // long t1 = System.currentTimeMillis();
            try {
                android.media.MediaScanner.FileEntry entry = beginFile(path, mimeType, lastModified, fileSize, isDirectory, noMedia);
                if (entry == null) {
                    return null;
                }
                // if this file was just inserted via mtp, set the rowid to zero
                // (even though it already exists in the database), to trigger
                // the correct code path for updating its entry
                if (mMtpObjectHandle != 0) {
                    entry.mRowId = 0;
                }
                if (entry.mPath != null) {
                    if ((((!mDefaultNotificationSet) && doesPathHaveFilename(entry.mPath, mDefaultNotificationFilename)) || ((!mDefaultRingtoneSet) && doesPathHaveFilename(entry.mPath, mDefaultRingtoneFilename))) || ((!mDefaultAlarmSet) && doesPathHaveFilename(entry.mPath, mDefaultAlarmAlertFilename))) {
                        android.util.Log.w(android.media.MediaScanner.TAG, ("forcing rescan of " + entry.mPath) + "since ringtone setting didn't finish");
                        scanAlways = true;
                    } else
                        if (android.media.MediaScanner.isSystemSoundWithMetadata(entry.mPath) && (!android.os.Build.FINGERPRINT.equals(android.media.MediaScanner.sLastInternalScanFingerprint))) {
                            // file is located on the system partition where the date cannot be trusted:
                            // rescan if the build fingerprint has changed since the last scan.
                            android.util.Log.i(android.media.MediaScanner.TAG, ("forcing rescan of " + entry.mPath) + " since build fingerprint changed");
                            scanAlways = true;
                        }

                }
                // rescan for metadata if file was modified since last scan
                if ((entry != null) && (entry.mLastModifiedChanged || scanAlways)) {
                    if (noMedia) {
                        result = endFile(entry, false, false, false, false, false);
                    } else {
                        java.lang.String lowpath = path.toLowerCase(java.util.Locale.ROOT);
                        boolean ringtones = lowpath.indexOf(android.media.MediaScanner.RINGTONES_DIR) > 0;
                        boolean notifications = lowpath.indexOf(android.media.MediaScanner.NOTIFICATIONS_DIR) > 0;
                        boolean alarms = lowpath.indexOf(android.media.MediaScanner.ALARMS_DIR) > 0;
                        boolean podcasts = lowpath.indexOf(android.media.MediaScanner.PODCAST_DIR) > 0;
                        boolean music = (lowpath.indexOf(android.media.MediaScanner.MUSIC_DIR) > 0) || ((((!ringtones) && (!notifications)) && (!alarms)) && (!podcasts));
                        boolean isaudio = android.media.MediaFile.isAudioFileType(mFileType);
                        boolean isvideo = android.media.MediaFile.isVideoFileType(mFileType);
                        boolean isimage = android.media.MediaFile.isImageFileType(mFileType);
                        if ((isaudio || isvideo) || isimage) {
                            path = android.os.Environment.maybeTranslateEmulatedPathToInternal(new java.io.File(path)).getAbsolutePath();
                        }
                        // we only extract metadata for audio and video files
                        if (isaudio || isvideo) {
                            processFile(path, mimeType, this);
                        }
                        if (isimage) {
                            processImageFile(path);
                        }
                        result = endFile(entry, ringtones, notifications, alarms, music, podcasts);
                    }
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.MediaScanner.TAG, "RemoteException in MediaScanner.scanFile()", e);
            }
            // long t2 = System.currentTimeMillis();
            // Log.v(TAG, "scanFile: " + path + " took " + (t2-t1));
            return result;
        }

        private long parseDate(java.lang.String date) {
            try {
                return mDateFormatter.parse(date).getTime();
            } catch (java.text.ParseException e) {
                return 0;
            }
        }

        private int parseSubstring(java.lang.String s, int start, int defaultValue) {
            int length = s.length();
            if (start == length)
                return defaultValue;

            char ch = s.charAt(start++);
            // return defaultValue if we have no integer at all
            if ((ch < '0') || (ch > '9'))
                return defaultValue;

            int result = ch - '0';
            while (start < length) {
                ch = s.charAt(start++);
                if ((ch < '0') || (ch > '9'))
                    return result;

                result = (result * 10) + (ch - '0');
            } 
            return result;
        }

        public void handleStringTag(java.lang.String name, java.lang.String value) {
            if (name.equalsIgnoreCase("title") || name.startsWith("title;")) {
                // Don't trim() here, to preserve the special \001 character
                // used to force sorting. The media provider will trim() before
                // inserting the title in to the database.
                mTitle = value;
            } else
                if (name.equalsIgnoreCase("artist") || name.startsWith("artist;")) {
                    mArtist = value.trim();
                } else
                    if (((name.equalsIgnoreCase("albumartist") || name.startsWith("albumartist;")) || name.equalsIgnoreCase("band")) || name.startsWith("band;")) {
                        mAlbumArtist = value.trim();
                    } else
                        if (name.equalsIgnoreCase("album") || name.startsWith("album;")) {
                            mAlbum = value.trim();
                        } else
                            if (name.equalsIgnoreCase("composer") || name.startsWith("composer;")) {
                                mComposer = value.trim();
                            } else
                                if (mProcessGenres && (name.equalsIgnoreCase("genre") || name.startsWith("genre;"))) {
                                    mGenre = getGenreName(value);
                                } else
                                    if (name.equalsIgnoreCase("year") || name.startsWith("year;")) {
                                        mYear = parseSubstring(value, 0, 0);
                                    } else
                                        if (name.equalsIgnoreCase("tracknumber") || name.startsWith("tracknumber;")) {
                                            // track number might be of the form "2/12"
                                            // we just read the number before the slash
                                            int num = parseSubstring(value, 0, 0);
                                            mTrack = ((mTrack / 1000) * 1000) + num;
                                        } else
                                            if ((name.equalsIgnoreCase("discnumber") || name.equals("set")) || name.startsWith("set;")) {
                                                // set number might be of the form "1/3"
                                                // we just read the number before the slash
                                                int num = parseSubstring(value, 0, 0);
                                                mTrack = (num * 1000) + (mTrack % 1000);
                                            } else
                                                if (name.equalsIgnoreCase("duration")) {
                                                    mDuration = parseSubstring(value, 0, 0);
                                                } else
                                                    if (name.equalsIgnoreCase("writer") || name.startsWith("writer;")) {
                                                        mWriter = value.trim();
                                                    } else
                                                        if (name.equalsIgnoreCase("compilation")) {
                                                            mCompilation = parseSubstring(value, 0, 0);
                                                        } else
                                                            if (name.equalsIgnoreCase("isdrm")) {
                                                                mIsDrm = parseSubstring(value, 0, 0) == 1;
                                                            } else
                                                                if (name.equalsIgnoreCase("date")) {
                                                                    mDate = parseDate(value);
                                                                } else
                                                                    if (name.equalsIgnoreCase("width")) {
                                                                        mWidth = parseSubstring(value, 0, 0);
                                                                    } else
                                                                        if (name.equalsIgnoreCase("height")) {
                                                                            mHeight = parseSubstring(value, 0, 0);
                                                                        } else {
                                                                            // Log.v(TAG, "unknown tag: " + name + " (" + mProcessGenres + ")");
                                                                        }















        }

        private boolean convertGenreCode(java.lang.String input, java.lang.String expected) {
            java.lang.String output = getGenreName(input);
            if (output.equals(expected)) {
                return true;
            } else {
                android.util.Log.d(android.media.MediaScanner.TAG, ((((("'" + input) + "' -> '") + output) + "', expected '") + expected) + "'");
                return false;
            }
        }

        private void testGenreNameConverter() {
            convertGenreCode("2", "Country");
            convertGenreCode("(2)", "Country");
            convertGenreCode("(2", "(2");
            convertGenreCode("2 Foo", "Country");
            convertGenreCode("(2) Foo", "Country");
            convertGenreCode("(2 Foo", "(2 Foo");
            convertGenreCode("2Foo", "2Foo");
            convertGenreCode("(2)Foo", "Country");
            convertGenreCode("200 Foo", "Foo");
            convertGenreCode("(200) Foo", "Foo");
            convertGenreCode("200Foo", "200Foo");
            convertGenreCode("(200)Foo", "Foo");
            convertGenreCode("200)Foo", "200)Foo");
            convertGenreCode("200) Foo", "200) Foo");
        }

        public java.lang.String getGenreName(java.lang.String genreTagValue) {
            if (genreTagValue == null) {
                return null;
            }
            final int length = genreTagValue.length();
            if (length > 0) {
                boolean parenthesized = false;
                java.lang.StringBuffer number = new java.lang.StringBuffer();
                int i = 0;
                for (; i < length; ++i) {
                    char c = genreTagValue.charAt(i);
                    if ((i == 0) && (c == '(')) {
                        parenthesized = true;
                    } else
                        if (java.lang.Character.isDigit(c)) {
                            number.append(c);
                        } else {
                            break;
                        }

                }
                char charAfterNumber = (i < length) ? genreTagValue.charAt(i) : ' ';
                if ((parenthesized && (charAfterNumber == ')')) || ((!parenthesized) && java.lang.Character.isWhitespace(charAfterNumber))) {
                    try {
                        short genreIndex = java.lang.Short.parseShort(number.toString());
                        if (genreIndex >= 0) {
                            if ((genreIndex < android.media.MediaScanner.ID3_GENRES.length) && (android.media.MediaScanner.ID3_GENRES[genreIndex] != null)) {
                                return android.media.MediaScanner.ID3_GENRES[genreIndex];
                            } else
                                if (genreIndex == 0xff) {
                                    return null;
                                } else
                                    if ((genreIndex < 0xff) && ((i + 1) < length)) {
                                        // genre is valid but unknown,
                                        // if there is a string after the value we take it
                                        if (parenthesized && (charAfterNumber == ')')) {
                                            i++;
                                        }
                                        java.lang.String ret = genreTagValue.substring(i).trim();
                                        if (ret.length() != 0) {
                                            return ret;
                                        }
                                    } else {
                                        // else return the number, without parentheses
                                        return number.toString();
                                    }


                        }
                    } catch (java.lang.NumberFormatException e) {
                    }
                }
            }
            return genreTagValue;
        }

        private void processImageFile(java.lang.String path) {
            try {
                mBitmapOptions.outWidth = 0;
                mBitmapOptions.outHeight = 0;
                android.graphics.BitmapFactory.decodeFile(path, mBitmapOptions);
                mWidth = mBitmapOptions.outWidth;
                mHeight = mBitmapOptions.outHeight;
            } catch (java.lang.Throwable th) {
                // ignore;
            }
        }

        public void setMimeType(java.lang.String mimeType) {
            if ("audio/mp4".equals(mMimeType) && mimeType.startsWith("video")) {
                // for feature parity with Donut, we force m4a files to keep the
                // audio/mp4 mimetype, even if they are really "enhanced podcasts"
                // with a video track
                return;
            }
            mMimeType = mimeType;
            mFileType = android.media.MediaFile.getFileTypeForMimeType(mimeType);
        }

        /**
         * Formats the data into a values array suitable for use with the Media
         * Content Provider.
         *
         * @return a map of values
         */
        private android.content.ContentValues toValues() {
            android.content.ContentValues map = new android.content.ContentValues();
            map.put(android.provider.MediaStore.MediaColumns.DATA, mPath);
            map.put(android.provider.MediaStore.MediaColumns.TITLE, mTitle);
            map.put(android.provider.MediaStore.MediaColumns.DATE_MODIFIED, mLastModified);
            map.put(android.provider.MediaStore.MediaColumns.SIZE, mFileSize);
            map.put(android.provider.MediaStore.MediaColumns.MIME_TYPE, mMimeType);
            map.put(android.provider.MediaStore.MediaColumns.IS_DRM, mIsDrm);
            java.lang.String resolution = null;
            if ((mWidth > 0) && (mHeight > 0)) {
                map.put(android.provider.MediaStore.MediaColumns.WIDTH, mWidth);
                map.put(android.provider.MediaStore.MediaColumns.HEIGHT, mHeight);
                resolution = (mWidth + "x") + mHeight;
            }
            if (!mNoMedia) {
                if (android.media.MediaFile.isVideoFileType(mFileType)) {
                    map.put(android.provider.MediaStore.Video.Media.ARTIST, (mArtist != null) && (mArtist.length() > 0) ? mArtist : android.provider.MediaStore.UNKNOWN_STRING);
                    map.put(android.provider.MediaStore.Video.Media.ALBUM, (mAlbum != null) && (mAlbum.length() > 0) ? mAlbum : android.provider.MediaStore.UNKNOWN_STRING);
                    map.put(android.provider.MediaStore.Video.Media.DURATION, mDuration);
                    if (resolution != null) {
                        map.put(android.provider.MediaStore.Video.Media.RESOLUTION, resolution);
                    }
                    if (mDate > 0) {
                        map.put(android.provider.MediaStore.Video.Media.DATE_TAKEN, mDate);
                    }
                } else
                    if (android.media.MediaFile.isImageFileType(mFileType)) {
                        // FIXME - add DESCRIPTION
                    } else
                        if (android.media.MediaFile.isAudioFileType(mFileType)) {
                            map.put(android.provider.MediaStore.Audio.Media.ARTIST, (mArtist != null) && (mArtist.length() > 0) ? mArtist : android.provider.MediaStore.UNKNOWN_STRING);
                            map.put(android.provider.MediaStore.Audio.Media.ALBUM_ARTIST, (mAlbumArtist != null) && (mAlbumArtist.length() > 0) ? mAlbumArtist : null);
                            map.put(android.provider.MediaStore.Audio.Media.ALBUM, (mAlbum != null) && (mAlbum.length() > 0) ? mAlbum : android.provider.MediaStore.UNKNOWN_STRING);
                            map.put(android.provider.MediaStore.Audio.Media.COMPOSER, mComposer);
                            map.put(android.provider.MediaStore.Audio.Media.GENRE, mGenre);
                            if (mYear != 0) {
                                map.put(android.provider.MediaStore.Audio.Media.YEAR, mYear);
                            }
                            map.put(android.provider.MediaStore.Audio.Media.TRACK, mTrack);
                            map.put(android.provider.MediaStore.Audio.Media.DURATION, mDuration);
                            map.put(android.provider.MediaStore.Audio.Media.COMPILATION, mCompilation);
                        }


            }
            return map;
        }

        private android.net.Uri endFile(android.media.MediaScanner.FileEntry entry, boolean ringtones, boolean notifications, boolean alarms, boolean music, boolean podcasts) throws android.os.RemoteException {
            // update database
            // use album artist if artist is missing
            if ((mArtist == null) || (mArtist.length() == 0)) {
                mArtist = mAlbumArtist;
            }
            android.content.ContentValues values = toValues();
            java.lang.String title = values.getAsString(android.provider.MediaStore.MediaColumns.TITLE);
            if ((title == null) || android.text.TextUtils.isEmpty(title.trim())) {
                title = android.media.MediaFile.getFileTitle(values.getAsString(android.provider.MediaStore.MediaColumns.DATA));
                values.put(android.provider.MediaStore.MediaColumns.TITLE, title);
            }
            java.lang.String album = values.getAsString(android.provider.MediaStore.Audio.Media.ALBUM);
            if (android.provider.MediaStore.UNKNOWN_STRING.equals(album)) {
                album = values.getAsString(android.provider.MediaStore.MediaColumns.DATA);
                // extract last path segment before file name
                int lastSlash = album.lastIndexOf('/');
                if (lastSlash >= 0) {
                    int previousSlash = 0;
                    while (true) {
                        int idx = album.indexOf('/', previousSlash + 1);
                        if ((idx < 0) || (idx >= lastSlash)) {
                            break;
                        }
                        previousSlash = idx;
                    } 
                    if (previousSlash != 0) {
                        album = album.substring(previousSlash + 1, lastSlash);
                        values.put(android.provider.MediaStore.Audio.Media.ALBUM, album);
                    }
                }
            }
            long rowId = entry.mRowId;
            if (android.media.MediaFile.isAudioFileType(mFileType) && ((rowId == 0) || (mMtpObjectHandle != 0))) {
                // Only set these for new entries. For existing entries, they
                // may have been modified later, and we want to keep the current
                // values so that custom ringtones still show up in the ringtone
                // picker.
                values.put(android.provider.MediaStore.Audio.Media.IS_RINGTONE, ringtones);
                values.put(android.provider.MediaStore.Audio.Media.IS_NOTIFICATION, notifications);
                values.put(android.provider.MediaStore.Audio.Media.IS_ALARM, alarms);
                values.put(android.provider.MediaStore.Audio.Media.IS_MUSIC, music);
                values.put(android.provider.MediaStore.Audio.Media.IS_PODCAST, podcasts);
            } else
                if (((mFileType == android.media.MediaFile.FILE_TYPE_JPEG) || android.media.MediaFile.isRawImageFileType(mFileType)) && (!mNoMedia)) {
                    android.media.ExifInterface exif = null;
                    try {
                        exif = new android.media.ExifInterface(entry.mPath);
                    } catch (java.io.IOException ex) {
                        // exif is null
                    }
                    if (exif != null) {
                        float[] latlng = new float[2];
                        if (exif.getLatLong(latlng)) {
                            values.put(android.provider.MediaStore.Images.Media.LATITUDE, latlng[0]);
                            values.put(android.provider.MediaStore.Images.Media.LONGITUDE, latlng[1]);
                        }
                        long time = exif.getGpsDateTime();
                        if (time != (-1)) {
                            values.put(android.provider.MediaStore.Images.Media.DATE_TAKEN, time);
                        } else {
                            // If no time zone information is available, we should consider using
                            // EXIF local time as taken time if the difference between file time
                            // and EXIF local time is not less than 1 Day, otherwise MediaProvider
                            // will use file time as taken time.
                            time = exif.getDateTime();
                            if ((time != (-1)) && (java.lang.Math.abs((mLastModified * 1000) - time) >= 86400000)) {
                                values.put(android.provider.MediaStore.Images.Media.DATE_TAKEN, time);
                            }
                        }
                        int orientation = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, -1);
                        if (orientation != (-1)) {
                            // We only recognize a subset of orientation tag values.
                            int degree;
                            switch (orientation) {
                                case android.media.ExifInterface.ORIENTATION_ROTATE_90 :
                                    degree = 90;
                                    break;
                                case android.media.ExifInterface.ORIENTATION_ROTATE_180 :
                                    degree = 180;
                                    break;
                                case android.media.ExifInterface.ORIENTATION_ROTATE_270 :
                                    degree = 270;
                                    break;
                                default :
                                    degree = 0;
                                    break;
                            }
                            values.put(android.provider.MediaStore.Images.Media.ORIENTATION, degree);
                        }
                    }
                }

            android.net.Uri tableUri = mFilesUri;
            android.media.MediaInserter inserter = mMediaInserter;
            if (!mNoMedia) {
                if (android.media.MediaFile.isVideoFileType(mFileType)) {
                    tableUri = mVideoUri;
                } else
                    if (android.media.MediaFile.isImageFileType(mFileType)) {
                        tableUri = mImagesUri;
                    } else
                        if (android.media.MediaFile.isAudioFileType(mFileType)) {
                            tableUri = mAudioUri;
                        }


            }
            android.net.Uri result = null;
            boolean needToSetSettings = false;
            // Setting a flag in order not to use bulk insert for the file related with
            // notifications, ringtones, and alarms, because the rowId of the inserted file is
            // needed.
            if (notifications && (!mDefaultNotificationSet)) {
                if (android.text.TextUtils.isEmpty(mDefaultNotificationFilename) || doesPathHaveFilename(entry.mPath, mDefaultNotificationFilename)) {
                    needToSetSettings = true;
                }
            } else
                if (ringtones && (!mDefaultRingtoneSet)) {
                    if (android.text.TextUtils.isEmpty(mDefaultRingtoneFilename) || doesPathHaveFilename(entry.mPath, mDefaultRingtoneFilename)) {
                        needToSetSettings = true;
                    }
                } else
                    if (alarms && (!mDefaultAlarmSet)) {
                        if (android.text.TextUtils.isEmpty(mDefaultAlarmAlertFilename) || doesPathHaveFilename(entry.mPath, mDefaultAlarmAlertFilename)) {
                            needToSetSettings = true;
                        }
                    }


            if (rowId == 0) {
                if (mMtpObjectHandle != 0) {
                    values.put(android.provider.MediaStore.MediaColumns.MEDIA_SCANNER_NEW_OBJECT_ID, mMtpObjectHandle);
                }
                if (tableUri == mFilesUri) {
                    int format = entry.mFormat;
                    if (format == 0) {
                        format = android.media.MediaFile.getFormatCode(entry.mPath, mMimeType);
                    }
                    values.put(android.provider.MediaStore.Files.FileColumns.FORMAT, format);
                }
                // New file, insert it.
                // Directories need to be inserted before the files they contain, so they
                // get priority when bulk inserting.
                // If the rowId of the inserted file is needed, it gets inserted immediately,
                // bypassing the bulk inserter.
                if ((inserter == null) || needToSetSettings) {
                    if (inserter != null) {
                        inserter.flushAll();
                    }
                    result = mMediaProvider.insert(tableUri, values);
                } else
                    if (entry.mFormat == android.mtp.MtpConstants.FORMAT_ASSOCIATION) {
                        inserter.insertwithPriority(tableUri, values);
                    } else {
                        inserter.insert(tableUri, values);
                    }

                if (result != null) {
                    rowId = android.content.ContentUris.parseId(result);
                    entry.mRowId = rowId;
                }
            } else {
                // updated file
                result = android.content.ContentUris.withAppendedId(tableUri, rowId);
                // path should never change, and we want to avoid replacing mixed cased paths
                // with squashed lower case paths
                values.remove(android.provider.MediaStore.MediaColumns.DATA);
                int mediaType = 0;
                if (!android.media.MediaScanner.isNoMediaPath(entry.mPath)) {
                    int fileType = android.media.MediaFile.getFileTypeForMimeType(mMimeType);
                    if (android.media.MediaFile.isAudioFileType(fileType)) {
                        mediaType = android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
                    } else
                        if (android.media.MediaFile.isVideoFileType(fileType)) {
                            mediaType = android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                        } else
                            if (android.media.MediaFile.isImageFileType(fileType)) {
                                mediaType = android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
                            } else
                                if (android.media.MediaFile.isPlayListFileType(fileType)) {
                                    mediaType = android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_PLAYLIST;
                                }



                    values.put(android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE, mediaType);
                }
                mMediaProvider.update(result, values, null, null);
            }
            if (needToSetSettings) {
                if (notifications) {
                    setRingtoneIfNotSet(android.provider.Settings.System.NOTIFICATION_SOUND, tableUri, rowId);
                    mDefaultNotificationSet = true;
                } else
                    if (ringtones) {
                        setRingtoneIfNotSet(android.provider.Settings.System.RINGTONE, tableUri, rowId);
                        mDefaultRingtoneSet = true;
                    } else
                        if (alarms) {
                            setRingtoneIfNotSet(android.provider.Settings.System.ALARM_ALERT, tableUri, rowId);
                            mDefaultAlarmSet = true;
                        }


            }
            return result;
        }

        private boolean doesPathHaveFilename(java.lang.String path, java.lang.String filename) {
            int pathFilenameStart = path.lastIndexOf(java.io.File.separatorChar) + 1;
            int filenameLength = filename.length();
            return path.regionMatches(pathFilenameStart, filename, 0, filenameLength) && ((pathFilenameStart + filenameLength) == path.length());
        }

        private void setRingtoneIfNotSet(java.lang.String settingName, android.net.Uri uri, long rowId) {
            if (wasRingtoneAlreadySet(settingName)) {
                return;
            }
            android.content.ContentResolver cr = mContext.getContentResolver();
            java.lang.String existingSettingValue = android.provider.Settings.System.getString(cr, settingName);
            if (android.text.TextUtils.isEmpty(existingSettingValue)) {
                final android.net.Uri settingUri = android.provider.Settings.System.getUriFor(settingName);
                final android.net.Uri ringtoneUri = android.content.ContentUris.withAppendedId(uri, rowId);
                android.media.RingtoneManager.setActualDefaultRingtoneUri(mContext, android.media.RingtoneManager.getDefaultType(settingUri), ringtoneUri);
            }
            android.provider.Settings.System.putInt(cr, settingSetIndicatorName(settingName), 1);
        }

        private int getFileTypeFromDrm(java.lang.String path) {
            if (!isDrmEnabled()) {
                return 0;
            }
            int resultFileType = 0;
            if (mDrmManagerClient == null) {
                mDrmManagerClient = new android.drm.DrmManagerClient(mContext);
            }
            if (mDrmManagerClient.canHandle(path, null)) {
                mIsDrm = true;
                java.lang.String drmMimetype = mDrmManagerClient.getOriginalMimeType(path);
                if (drmMimetype != null) {
                    mMimeType = drmMimetype;
                    resultFileType = android.media.MediaFile.getFileTypeForMimeType(drmMimetype);
                }
            }
            return resultFileType;
        }
    }

    // end of anonymous MediaScannerClient instance
    private static boolean isSystemSoundWithMetadata(java.lang.String path) {
        if ((path.startsWith(android.media.MediaScanner.SYSTEM_SOUNDS_DIR + android.media.MediaScanner.ALARMS_DIR) || path.startsWith(android.media.MediaScanner.SYSTEM_SOUNDS_DIR + android.media.MediaScanner.RINGTONES_DIR)) || path.startsWith(android.media.MediaScanner.SYSTEM_SOUNDS_DIR + android.media.MediaScanner.NOTIFICATIONS_DIR)) {
            return true;
        }
        return false;
    }

    private java.lang.String settingSetIndicatorName(java.lang.String base) {
        return base + "_set";
    }

    private boolean wasRingtoneAlreadySet(java.lang.String name) {
        android.content.ContentResolver cr = mContext.getContentResolver();
        java.lang.String indicatorName = settingSetIndicatorName(name);
        try {
            return android.provider.Settings.System.getInt(cr, indicatorName) != 0;
        } catch (android.provider.Settings.SettingNotFoundException e) {
            return false;
        }
    }

    private void prescan(java.lang.String filePath, boolean prescanFiles) throws android.os.RemoteException {
        android.database.Cursor c = null;
        java.lang.String where = null;
        java.lang.String[] selectionArgs = null;
        mPlayLists.clear();
        if (filePath != null) {
            // query for only one file
            where = (((android.provider.MediaStore.Files.FileColumns._ID + ">?") + " AND ") + android.provider.MediaStore.Files.FileColumns.DATA) + "=?";
            selectionArgs = new java.lang.String[]{ "", filePath };
        } else {
            where = android.provider.MediaStore.Files.FileColumns._ID + ">?";
            selectionArgs = new java.lang.String[]{ "" };
        }
        mDefaultRingtoneSet = wasRingtoneAlreadySet(android.provider.Settings.System.RINGTONE);
        mDefaultNotificationSet = wasRingtoneAlreadySet(android.provider.Settings.System.NOTIFICATION_SOUND);
        mDefaultAlarmSet = wasRingtoneAlreadySet(android.provider.Settings.System.ALARM_ALERT);
        // Tell the provider to not delete the file.
        // If the file is truly gone the delete is unnecessary, and we want to avoid
        // accidentally deleting files that are really there (this may happen if the
        // filesystem is mounted and unmounted while the scanner is running).
        android.net.Uri.Builder builder = mFilesUri.buildUpon();
        builder.appendQueryParameter(android.provider.MediaStore.PARAM_DELETE_DATA, "false");
        android.media.MediaScanner.MediaBulkDeleter deleter = new android.media.MediaScanner.MediaBulkDeleter(mMediaProvider, builder.build());
        // Build the list of files from the content provider
        try {
            if (prescanFiles) {
                // First read existing files from the files table.
                // Because we'll be deleting entries for missing files as we go,
                // we need to query the database in small batches, to avoid problems
                // with CursorWindow positioning.
                long lastId = java.lang.Long.MIN_VALUE;
                android.net.Uri limitUri = mFilesUri.buildUpon().appendQueryParameter("limit", "1000").build();
                while (true) {
                    selectionArgs[0] = "" + lastId;
                    if (c != null) {
                        c.close();
                        c = null;
                    }
                    c = mMediaProvider.query(limitUri, android.media.MediaScanner.FILES_PRESCAN_PROJECTION, where, selectionArgs, android.provider.MediaStore.Files.FileColumns._ID, null);
                    if (c == null) {
                        break;
                    }
                    int num = c.getCount();
                    if (num == 0) {
                        break;
                    }
                    while (c.moveToNext()) {
                        long rowId = c.getLong(android.media.MediaScanner.FILES_PRESCAN_ID_COLUMN_INDEX);
                        java.lang.String path = c.getString(android.media.MediaScanner.FILES_PRESCAN_PATH_COLUMN_INDEX);
                        int format = c.getInt(android.media.MediaScanner.FILES_PRESCAN_FORMAT_COLUMN_INDEX);
                        long lastModified = c.getLong(android.media.MediaScanner.FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX);
                        lastId = rowId;
                        // Only consider entries with absolute path names.
                        // This allows storing URIs in the database without the
                        // media scanner removing them.
                        if ((path != null) && path.startsWith("/")) {
                            boolean exists = false;
                            try {
                                exists = android.system.Os.access(path, android.system.OsConstants.F_OK);
                            } catch (android.system.ErrnoException e1) {
                            }
                            if ((!exists) && (!android.mtp.MtpConstants.isAbstractObject(format))) {
                                // do not delete missing playlists, since they may have been
                                // modified by the user.
                                // The user can delete them in the media player instead.
                                // instead, clear the path and lastModified fields in the row
                                android.media.MediaFile.MediaFileType mediaFileType = android.media.MediaFile.getFileType(path);
                                int fileType = (mediaFileType == null) ? 0 : mediaFileType.fileType;
                                if (!android.media.MediaFile.isPlayListFileType(fileType)) {
                                    deleter.delete(rowId);
                                    if (path.toLowerCase(java.util.Locale.US).endsWith("/.nomedia")) {
                                        deleter.flush();
                                        java.lang.String parent = new java.io.File(path).getParent();
                                        mMediaProvider.call(android.provider.MediaStore.UNHIDE_CALL, parent, null);
                                    }
                                }
                            }
                        }
                    } 
                } 
            }
        } finally {
            if (c != null) {
                c.close();
            }
            deleter.flush();
        }
        // compute original size of images
        mOriginalCount = 0;
        c = mMediaProvider.query(mImagesUri, android.media.MediaScanner.ID_PROJECTION, null, null, null, null);
        if (c != null) {
            mOriginalCount = c.getCount();
            c.close();
        }
    }

    private void pruneDeadThumbnailFiles() {
        java.util.HashSet<java.lang.String> existingFiles = new java.util.HashSet<java.lang.String>();
        java.lang.String directory = "/sdcard/DCIM/.thumbnails";
        java.lang.String[] files = new java.io.File(directory).list();
        android.database.Cursor c = null;
        if (files == null)
            files = new java.lang.String[0];

        for (int i = 0; i < files.length; i++) {
            java.lang.String fullPathString = (directory + "/") + files[i];
            existingFiles.add(fullPathString);
        }
        try {
            c = mMediaProvider.query(mThumbsUri, new java.lang.String[]{ "_data" }, null, null, null, null);
            android.util.Log.v(android.media.MediaScanner.TAG, "pruneDeadThumbnailFiles... " + c);
            if ((c != null) && c.moveToFirst()) {
                do {
                    java.lang.String fullPathString = c.getString(0);
                    existingFiles.remove(fullPathString);
                } while (c.moveToNext() );
            }
            for (java.lang.String fileToDelete : existingFiles) {
                if (false)
                    android.util.Log.v(android.media.MediaScanner.TAG, "fileToDelete is " + fileToDelete);

                try {
                    new java.io.File(fileToDelete).delete();
                } catch (java.lang.SecurityException ex) {
                }
            }
            android.util.Log.v(android.media.MediaScanner.TAG, "/pruneDeadThumbnailFiles... " + c);
        } catch (android.os.RemoteException e) {
            // We will soon be killed...
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    static class MediaBulkDeleter {
        java.lang.StringBuilder whereClause = new java.lang.StringBuilder();

        java.util.ArrayList<java.lang.String> whereArgs = new java.util.ArrayList<java.lang.String>(100);

        final android.content.ContentProviderClient mProvider;

        final android.net.Uri mBaseUri;

        public MediaBulkDeleter(android.content.ContentProviderClient provider, android.net.Uri baseUri) {
            mProvider = provider;
            mBaseUri = baseUri;
        }

        public void delete(long id) throws android.os.RemoteException {
            if (whereClause.length() != 0) {
                whereClause.append(",");
            }
            whereClause.append("?");
            whereArgs.add("" + id);
            if (whereArgs.size() > 100) {
                flush();
            }
        }

        public void flush() throws android.os.RemoteException {
            int size = whereArgs.size();
            if (size > 0) {
                java.lang.String[] foo = new java.lang.String[size];
                foo = whereArgs.toArray(foo);
                int numrows = mProvider.delete(mBaseUri, ((android.provider.MediaStore.MediaColumns._ID + " IN (") + whereClause.toString()) + ")", foo);
                // Log.i("@@@@@@@@@", "rows deleted: " + numrows);
                whereClause.setLength(0);
                whereArgs.clear();
            }
        }
    }

    private void postscan(final java.lang.String[] directories) throws android.os.RemoteException {
        // handle playlists last, after we know what media files are on the storage.
        if (mProcessPlaylists) {
            processPlayLists();
        }
        if ((mOriginalCount == 0) && mImagesUri.equals(android.provider.MediaStore.Images.Media.getContentUri("external")))
            pruneDeadThumbnailFiles();

        // allow GC to clean up
        mPlayLists.clear();
    }

    private void releaseResources() {
        // release the DrmManagerClient resources
        if (mDrmManagerClient != null) {
            mDrmManagerClient.close();
            mDrmManagerClient = null;
        }
    }

    public void scanDirectories(java.lang.String[] directories) {
        try {
            long start = java.lang.System.currentTimeMillis();
            prescan(null, true);
            long prescan = java.lang.System.currentTimeMillis();
            if (android.media.MediaScanner.ENABLE_BULK_INSERTS) {
                // create MediaInserter for bulk inserts
                mMediaInserter = new android.media.MediaInserter(mMediaProvider, 500);
            }
            for (int i = 0; i < directories.length; i++) {
                processDirectory(directories[i], mClient);
            }
            if (android.media.MediaScanner.ENABLE_BULK_INSERTS) {
                // flush remaining inserts
                mMediaInserter.flushAll();
                mMediaInserter = null;
            }
            long scan = java.lang.System.currentTimeMillis();
            postscan(directories);
            long end = java.lang.System.currentTimeMillis();
            if (false) {
                android.util.Log.d(android.media.MediaScanner.TAG, (" prescan time: " + (prescan - start)) + "ms\n");
                android.util.Log.d(android.media.MediaScanner.TAG, ("    scan time: " + (scan - prescan)) + "ms\n");
                android.util.Log.d(android.media.MediaScanner.TAG, ("postscan time: " + (end - scan)) + "ms\n");
                android.util.Log.d(android.media.MediaScanner.TAG, ("   total time: " + (end - start)) + "ms\n");
            }
        } catch (android.database.SQLException e) {
            // this might happen if the SD card is removed while the media scanner is running
            android.util.Log.e(android.media.MediaScanner.TAG, "SQLException in MediaScanner.scan()", e);
        } catch (java.lang.UnsupportedOperationException e) {
            // this might happen if the SD card is removed while the media scanner is running
            android.util.Log.e(android.media.MediaScanner.TAG, "UnsupportedOperationException in MediaScanner.scan()", e);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.MediaScanner.TAG, "RemoteException in MediaScanner.scan()", e);
        } finally {
            releaseResources();
        }
    }

    // this function is used to scan a single file
    public android.net.Uri scanSingleFile(java.lang.String path, java.lang.String mimeType) {
        try {
            prescan(path, true);
            java.io.File file = new java.io.File(path);
            if (!file.exists()) {
                return null;
            }
            // lastModified is in milliseconds on Files.
            long lastModifiedSeconds = file.lastModified() / 1000;
            // always scan the file, so we can return the content://media Uri for existing files
            return mClient.doScanFile(path, mimeType, lastModifiedSeconds, file.length(), false, true, android.media.MediaScanner.isNoMediaPath(path));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.MediaScanner.TAG, "RemoteException in MediaScanner.scanFile()", e);
            return null;
        } finally {
            releaseResources();
        }
    }

    private static boolean isNoMediaFile(java.lang.String path) {
        java.io.File file = new java.io.File(path);
        if (file.isDirectory())
            return false;

        // special case certain file names
        // I use regionMatches() instead of substring() below
        // to avoid memory allocation
        int lastSlash = path.lastIndexOf('/');
        if ((lastSlash >= 0) && ((lastSlash + 2) < path.length())) {
            // ignore those ._* files created by MacOS
            if (path.regionMatches(lastSlash + 1, "._", 0, 2)) {
                return true;
            }
            // ignore album art files created by Windows Media Player:
            // Folder.jpg, AlbumArtSmall.jpg, AlbumArt_{...}_Large.jpg
            // and AlbumArt_{...}_Small.jpg
            if (path.regionMatches(true, path.length() - 4, ".jpg", 0, 4)) {
                if (path.regionMatches(true, lastSlash + 1, "AlbumArt_{", 0, 10) || path.regionMatches(true, lastSlash + 1, "AlbumArt.", 0, 9)) {
                    return true;
                }
                int length = (path.length() - lastSlash) - 1;
                if (((length == 17) && path.regionMatches(true, lastSlash + 1, "AlbumArtSmall", 0, 13)) || ((length == 10) && path.regionMatches(true, lastSlash + 1, "Folder", 0, 6))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static java.util.HashMap<java.lang.String, java.lang.String> mNoMediaPaths = new java.util.HashMap<java.lang.String, java.lang.String>();

    private static java.util.HashMap<java.lang.String, java.lang.String> mMediaPaths = new java.util.HashMap<java.lang.String, java.lang.String>();

    /* MediaProvider calls this when a .nomedia file is added or removed */
    public static void clearMediaPathCache(boolean clearMediaPaths, boolean clearNoMediaPaths) {
        synchronized(android.media.MediaScanner.class) {
            if (clearMediaPaths) {
                android.media.MediaScanner.mMediaPaths.clear();
            }
            if (clearNoMediaPaths) {
                android.media.MediaScanner.mNoMediaPaths.clear();
            }
        }
    }

    public static boolean isNoMediaPath(java.lang.String path) {
        if (path == null) {
            return false;
        }
        // return true if file or any parent directory has name starting with a dot
        if (path.indexOf("/.") >= 0) {
            return true;
        }
        int firstSlash = path.lastIndexOf('/');
        if (firstSlash <= 0) {
            return false;
        }
        java.lang.String parent = path.substring(0, firstSlash);
        synchronized(android.media.MediaScanner.class) {
            if (android.media.MediaScanner.mNoMediaPaths.containsKey(parent)) {
                return true;
            } else
                if (!android.media.MediaScanner.mMediaPaths.containsKey(parent)) {
                    // check to see if any parent directories have a ".nomedia" file
                    // start from 1 so we don't bother checking in the root directory
                    int offset = 1;
                    while (offset >= 0) {
                        int slashIndex = path.indexOf('/', offset);
                        if (slashIndex > offset) {
                            slashIndex++;// move past slash

                            java.io.File file = new java.io.File(path.substring(0, slashIndex) + ".nomedia");
                            if (file.exists()) {
                                // we have a .nomedia in one of the parent directories
                                android.media.MediaScanner.mNoMediaPaths.put(parent, "");
                                return true;
                            }
                        }
                        offset = slashIndex;
                    } 
                    android.media.MediaScanner.mMediaPaths.put(parent, "");
                }

        }
        return android.media.MediaScanner.isNoMediaFile(path);
    }

    public void scanMtpFile(java.lang.String path, int objectHandle, int format) {
        android.media.MediaFile.MediaFileType mediaFileType = android.media.MediaFile.getFileType(path);
        int fileType = (mediaFileType == null) ? 0 : mediaFileType.fileType;
        java.io.File file = new java.io.File(path);
        long lastModifiedSeconds = file.lastModified() / 1000;
        if (((((!android.media.MediaFile.isAudioFileType(fileType)) && (!android.media.MediaFile.isVideoFileType(fileType))) && (!android.media.MediaFile.isImageFileType(fileType))) && (!android.media.MediaFile.isPlayListFileType(fileType))) && (!android.media.MediaFile.isDrmFileType(fileType))) {
            // no need to use the media scanner, but we need to update last modified and file size
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.MediaStore.Files.FileColumns.SIZE, file.length());
            values.put(android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED, lastModifiedSeconds);
            try {
                java.lang.String[] whereArgs = new java.lang.String[]{ java.lang.Integer.toString(objectHandle) };
                mMediaProvider.update(android.provider.MediaStore.Files.getMtpObjectsUri(mVolumeName), values, "_id=?", whereArgs);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.MediaScanner.TAG, "RemoteException in scanMtpFile", e);
            }
            return;
        }
        mMtpObjectHandle = objectHandle;
        android.database.Cursor fileList = null;
        try {
            if (android.media.MediaFile.isPlayListFileType(fileType)) {
                // build file cache so we can look up tracks in the playlist
                prescan(null, true);
                android.media.MediaScanner.FileEntry entry = makeEntryFor(path);
                if (entry != null) {
                    fileList = mMediaProvider.query(mFilesUri, android.media.MediaScanner.FILES_PRESCAN_PROJECTION, null, null, null, null);
                    processPlayList(entry, fileList);
                }
            } else {
                // MTP will create a file entry for us so we don't want to do it in prescan
                prescan(path, false);
                // always scan the file, so we can return the content://media Uri for existing files
                mClient.doScanFile(path, mediaFileType.mimeType, lastModifiedSeconds, file.length(), format == android.mtp.MtpConstants.FORMAT_ASSOCIATION, true, android.media.MediaScanner.isNoMediaPath(path));
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.MediaScanner.TAG, "RemoteException in MediaScanner.scanFile()", e);
        } finally {
            mMtpObjectHandle = 0;
            if (fileList != null) {
                fileList.close();
            }
            releaseResources();
        }
    }

    android.media.MediaScanner.FileEntry makeEntryFor(java.lang.String path) {
        java.lang.String where;
        java.lang.String[] selectionArgs;
        android.database.Cursor c = null;
        try {
            where = android.provider.MediaStore.Files.FileColumns.DATA + "=?";
            selectionArgs = new java.lang.String[]{ path };
            c = mMediaProvider.query(mFilesUriNoNotify, android.media.MediaScanner.FILES_PRESCAN_PROJECTION, where, selectionArgs, null, null);
            if (c.moveToFirst()) {
                long rowId = c.getLong(android.media.MediaScanner.FILES_PRESCAN_ID_COLUMN_INDEX);
                int format = c.getInt(android.media.MediaScanner.FILES_PRESCAN_FORMAT_COLUMN_INDEX);
                long lastModified = c.getLong(android.media.MediaScanner.FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX);
                return new android.media.MediaScanner.FileEntry(rowId, path, lastModified, format);
            }
        } catch (android.os.RemoteException e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    // returns the number of matching file/directory names, starting from the right
    private int matchPaths(java.lang.String path1, java.lang.String path2) {
        int result = 0;
        int end1 = path1.length();
        int end2 = path2.length();
        while ((end1 > 0) && (end2 > 0)) {
            int slash1 = path1.lastIndexOf('/', end1 - 1);
            int slash2 = path2.lastIndexOf('/', end2 - 1);
            int backSlash1 = path1.lastIndexOf('\\', end1 - 1);
            int backSlash2 = path2.lastIndexOf('\\', end2 - 1);
            int start1 = (slash1 > backSlash1) ? slash1 : backSlash1;
            int start2 = (slash2 > backSlash2) ? slash2 : backSlash2;
            if (start1 < 0)
                start1 = 0;
            else
                start1++;

            if (start2 < 0)
                start2 = 0;
            else
                start2++;

            int length = end1 - start1;
            if ((end2 - start2) != length)
                break;

            if (path1.regionMatches(true, start1, path2, start2, length)) {
                result++;
                end1 = start1 - 1;
                end2 = start2 - 1;
            } else
                break;

        } 
        return result;
    }

    private boolean matchEntries(long rowId, java.lang.String data) {
        int len = mPlaylistEntries.size();
        boolean done = true;
        for (int i = 0; i < len; i++) {
            android.media.MediaScanner.PlaylistEntry entry = mPlaylistEntries.get(i);
            if (entry.bestmatchlevel == java.lang.Integer.MAX_VALUE) {
                continue;// this entry has been matched already

            }
            done = false;
            if (data.equalsIgnoreCase(entry.path)) {
                entry.bestmatchid = rowId;
                entry.bestmatchlevel = java.lang.Integer.MAX_VALUE;
                continue;// no need for path matching

            }
            int matchLength = matchPaths(data, entry.path);
            if (matchLength > entry.bestmatchlevel) {
                entry.bestmatchid = rowId;
                entry.bestmatchlevel = matchLength;
            }
        }
        return done;
    }

    private void cachePlaylistEntry(java.lang.String line, java.lang.String playListDirectory) {
        android.media.MediaScanner.PlaylistEntry entry = new android.media.MediaScanner.PlaylistEntry();
        // watch for trailing whitespace
        int entryLength = line.length();
        while ((entryLength > 0) && java.lang.Character.isWhitespace(line.charAt(entryLength - 1)))
            entryLength--;

        // path should be longer than 3 characters.
        // avoid index out of bounds errors below by returning here.
        if (entryLength < 3)
            return;

        if (entryLength < line.length())
            line = line.substring(0, entryLength);

        // does entry appear to be an absolute path?
        // look for Unix or DOS absolute paths
        char ch1 = line.charAt(0);
        boolean fullPath = (ch1 == '/') || ((java.lang.Character.isLetter(ch1) && (line.charAt(1) == ':')) && (line.charAt(2) == '\\'));
        // if we have a relative path, combine entry with playListDirectory
        if (!fullPath)
            line = playListDirectory + line;

        entry.path = line;
        // FIXME - should we look for "../" within the path?
        mPlaylistEntries.add(entry);
    }

    private void processCachedPlaylist(android.database.Cursor fileList, android.content.ContentValues values, android.net.Uri playlistUri) {
        fileList.moveToPosition(-1);
        while (fileList.moveToNext()) {
            long rowId = fileList.getLong(android.media.MediaScanner.FILES_PRESCAN_ID_COLUMN_INDEX);
            java.lang.String data = fileList.getString(android.media.MediaScanner.FILES_PRESCAN_PATH_COLUMN_INDEX);
            if (matchEntries(rowId, data)) {
                break;
            }
        } 
        int len = mPlaylistEntries.size();
        int index = 0;
        for (int i = 0; i < len; i++) {
            android.media.MediaScanner.PlaylistEntry entry = mPlaylistEntries.get(i);
            if (entry.bestmatchlevel > 0) {
                try {
                    values.clear();
                    values.put(android.provider.MediaStore.Audio.Playlists.Members.PLAY_ORDER, java.lang.Integer.valueOf(index));
                    values.put(android.provider.MediaStore.Audio.Playlists.Members.AUDIO_ID, java.lang.Long.valueOf(entry.bestmatchid));
                    mMediaProvider.insert(playlistUri, values);
                    index++;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.MediaScanner.TAG, "RemoteException in MediaScanner.processCachedPlaylist()", e);
                    return;
                }
            }
        }
        mPlaylistEntries.clear();
    }

    private void processM3uPlayList(java.lang.String path, java.lang.String playListDirectory, android.net.Uri uri, android.content.ContentValues values, android.database.Cursor fileList) {
        java.io.BufferedReader reader = null;
        try {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(f)), 8192);
                java.lang.String line = reader.readLine();
                mPlaylistEntries.clear();
                while (line != null) {
                    // ignore comment lines, which begin with '#'
                    if ((line.length() > 0) && (line.charAt(0) != '#')) {
                        cachePlaylistEntry(line, playListDirectory);
                    }
                    line = reader.readLine();
                } 
                processCachedPlaylist(fileList, values, uri);
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(android.media.MediaScanner.TAG, "IOException in MediaScanner.processM3uPlayList()", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();

            } catch (java.io.IOException e) {
                android.util.Log.e(android.media.MediaScanner.TAG, "IOException in MediaScanner.processM3uPlayList()", e);
            }
        }
    }

    private void processPlsPlayList(java.lang.String path, java.lang.String playListDirectory, android.net.Uri uri, android.content.ContentValues values, android.database.Cursor fileList) {
        java.io.BufferedReader reader = null;
        try {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(f)), 8192);
                java.lang.String line = reader.readLine();
                mPlaylistEntries.clear();
                while (line != null) {
                    // ignore comment lines, which begin with '#'
                    if (line.startsWith("File")) {
                        int equals = line.indexOf('=');
                        if (equals > 0) {
                            cachePlaylistEntry(line.substring(equals + 1), playListDirectory);
                        }
                    }
                    line = reader.readLine();
                } 
                processCachedPlaylist(fileList, values, uri);
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(android.media.MediaScanner.TAG, "IOException in MediaScanner.processPlsPlayList()", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();

            } catch (java.io.IOException e) {
                android.util.Log.e(android.media.MediaScanner.TAG, "IOException in MediaScanner.processPlsPlayList()", e);
            }
        }
    }

    class WplHandler implements android.sax.ElementListener {
        final org.xml.sax.ContentHandler handler;

        java.lang.String playListDirectory;

        public WplHandler(java.lang.String playListDirectory, android.net.Uri uri, android.database.Cursor fileList) {
            this.playListDirectory = playListDirectory;
            android.sax.RootElement root = new android.sax.RootElement("smil");
            android.sax.Element body = root.getChild("body");
            android.sax.Element seq = body.getChild("seq");
            android.sax.Element media = seq.getChild("media");
            media.setElementListener(this);
            this.handler = root.getContentHandler();
        }

        @java.lang.Override
        public void start(org.xml.sax.Attributes attributes) {
            java.lang.String path = attributes.getValue("", "src");
            if (path != null) {
                cachePlaylistEntry(path, playListDirectory);
            }
        }

        @java.lang.Override
        public void end() {
        }

        org.xml.sax.ContentHandler getContentHandler() {
            return handler;
        }
    }

    private void processWplPlayList(java.lang.String path, java.lang.String playListDirectory, android.net.Uri uri, android.content.ContentValues values, android.database.Cursor fileList) {
        java.io.FileInputStream fis = null;
        try {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                fis = new java.io.FileInputStream(f);
                mPlaylistEntries.clear();
                android.util.Xml.parse(fis, android.util.Xml.findEncodingByName("UTF-8"), new android.media.MediaScanner.WplHandler(playListDirectory, uri, fileList).getContentHandler());
                processCachedPlaylist(fileList, values, uri);
            }
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();

            } catch (java.io.IOException e) {
                android.util.Log.e(android.media.MediaScanner.TAG, "IOException in MediaScanner.processWplPlayList()", e);
            }
        }
    }

    private void processPlayList(android.media.MediaScanner.FileEntry entry, android.database.Cursor fileList) throws android.os.RemoteException {
        java.lang.String path = entry.mPath;
        android.content.ContentValues values = new android.content.ContentValues();
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash < 0)
            throw new java.lang.IllegalArgumentException("bad path " + path);

        android.net.Uri uri;
        android.net.Uri membersUri;
        long rowId = entry.mRowId;
        // make sure we have a name
        java.lang.String name = values.getAsString(android.provider.MediaStore.Audio.Playlists.NAME);
        if (name == null) {
            name = values.getAsString(android.provider.MediaStore.MediaColumns.TITLE);
            if (name == null) {
                // extract name from file name
                int lastDot = path.lastIndexOf('.');
                name = (lastDot < 0) ? path.substring(lastSlash + 1) : path.substring(lastSlash + 1, lastDot);
            }
        }
        values.put(android.provider.MediaStore.Audio.Playlists.NAME, name);
        values.put(android.provider.MediaStore.Audio.Playlists.DATE_MODIFIED, entry.mLastModified);
        if (rowId == 0) {
            values.put(android.provider.MediaStore.Audio.Playlists.DATA, path);
            uri = mMediaProvider.insert(mPlaylistsUri, values);
            rowId = android.content.ContentUris.parseId(uri);
            membersUri = android.net.Uri.withAppendedPath(uri, android.provider.MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);
        } else {
            uri = android.content.ContentUris.withAppendedId(mPlaylistsUri, rowId);
            mMediaProvider.update(uri, values, null, null);
            // delete members of existing playlist
            membersUri = android.net.Uri.withAppendedPath(uri, android.provider.MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);
            mMediaProvider.delete(membersUri, null, null);
        }
        java.lang.String playListDirectory = path.substring(0, lastSlash + 1);
        android.media.MediaFile.MediaFileType mediaFileType = android.media.MediaFile.getFileType(path);
        int fileType = (mediaFileType == null) ? 0 : mediaFileType.fileType;
        if (fileType == android.media.MediaFile.FILE_TYPE_M3U) {
            processM3uPlayList(path, playListDirectory, membersUri, values, fileList);
        } else
            if (fileType == android.media.MediaFile.FILE_TYPE_PLS) {
                processPlsPlayList(path, playListDirectory, membersUri, values, fileList);
            } else
                if (fileType == android.media.MediaFile.FILE_TYPE_WPL) {
                    processWplPlayList(path, playListDirectory, membersUri, values, fileList);
                }


    }

    private void processPlayLists() throws android.os.RemoteException {
        java.util.Iterator<android.media.MediaScanner.FileEntry> iterator = mPlayLists.iterator();
        android.database.Cursor fileList = null;
        try {
            // use the files uri and projection because we need the format column,
            // but restrict the query to just audio files
            fileList = mMediaProvider.query(mFilesUri, android.media.MediaScanner.FILES_PRESCAN_PROJECTION, "media_type=2", null, null, null);
            while (iterator.hasNext()) {
                android.media.MediaScanner.FileEntry entry = iterator.next();
                // only process playlist files if they are new or have been modified since the last scan
                if (entry.mLastModifiedChanged) {
                    processPlayList(entry, fileList);
                }
            } 
        } catch (android.os.RemoteException e1) {
        } finally {
            if (fileList != null) {
                fileList.close();
            }
        }
    }

    private native void processDirectory(java.lang.String path, android.media.MediaScannerClient client);

    private native void processFile(java.lang.String path, java.lang.String mimeType, android.media.MediaScannerClient client);

    private native void setLocale(java.lang.String locale);

    public native byte[] extractAlbumArt(java.io.FileDescriptor fd);

    private static final native void native_init();

    private final native void native_setup();

    private final native void native_finalize();

    @java.lang.Override
    public void close() {
        mCloseGuard.close();
        if (mClosed.compareAndSet(false, true)) {
            mMediaProvider.close();
            native_finalize();
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            mCloseGuard.warnIfOpen();
            close();
        } finally {
            super.finalize();
        }
    }
}

