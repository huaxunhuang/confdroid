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
 * {@hide }
 */
public class MtpDatabase implements java.lang.AutoCloseable {
    private static final java.lang.String TAG = "MtpDatabase";

    private final android.content.Context mContext;

    private final java.lang.String mPackageName;

    private final android.content.ContentProviderClient mMediaProvider;

    private final java.lang.String mVolumeName;

    private final android.net.Uri mObjectsUri;

    private final android.media.MediaScanner mMediaScanner;

    private final java.util.concurrent.atomic.AtomicBoolean mClosed = new java.util.concurrent.atomic.AtomicBoolean();

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    // path to primary storage
    private final java.lang.String mMediaStoragePath;

    // if not null, restrict all queries to these subdirectories
    private final java.lang.String[] mSubDirectories;

    // where clause for restricting queries to files in mSubDirectories
    private java.lang.String mSubDirectoriesWhere;

    // where arguments for restricting queries to files in mSubDirectories
    private java.lang.String[] mSubDirectoriesWhereArgs;

    private final java.util.HashMap<java.lang.String, android.mtp.MtpStorage> mStorageMap = new java.util.HashMap<java.lang.String, android.mtp.MtpStorage>();

    // cached property groups for single properties
    private final java.util.HashMap<java.lang.Integer, android.mtp.MtpPropertyGroup> mPropertyGroupsByProperty = new java.util.HashMap<java.lang.Integer, android.mtp.MtpPropertyGroup>();

    // cached property groups for all properties for a given format
    private final java.util.HashMap<java.lang.Integer, android.mtp.MtpPropertyGroup> mPropertyGroupsByFormat = new java.util.HashMap<java.lang.Integer, android.mtp.MtpPropertyGroup>();

    // true if the database has been modified in the current MTP session
    private boolean mDatabaseModified;

    // SharedPreferences for writable MTP device properties
    private android.content.SharedPreferences mDeviceProperties;

    private static final int DEVICE_PROPERTIES_DATABASE_VERSION = 1;

    private static final java.lang.String[] ID_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID// 0
     };

    private static final java.lang.String[] PATH_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID// 0
    , android.provider.MediaStore.Files.FileColumns.DATA// 1
     };

    private static final java.lang.String[] FORMAT_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID// 0
    , android.provider.MediaStore.Files.FileColumns.FORMAT// 1
     };

    private static final java.lang.String[] PATH_FORMAT_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID// 0
    , android.provider.MediaStore.Files.FileColumns.DATA// 1
    , android.provider.MediaStore.Files.FileColumns.FORMAT// 2
     };

    private static final java.lang.String[] OBJECT_INFO_PROJECTION = new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID// 0
    , android.provider.MediaStore.Files.FileColumns.STORAGE_ID// 1
    , android.provider.MediaStore.Files.FileColumns.FORMAT// 2
    , android.provider.MediaStore.Files.FileColumns.PARENT// 3
    , android.provider.MediaStore.Files.FileColumns.DATA// 4
    , android.provider.MediaStore.Files.FileColumns.DATE_ADDED// 5
    , android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED// 6
     };

    private static final java.lang.String ID_WHERE = android.provider.MediaStore.Files.FileColumns._ID + "=?";

    private static final java.lang.String PATH_WHERE = android.provider.MediaStore.Files.FileColumns.DATA + "=?";

    private static final java.lang.String STORAGE_WHERE = android.provider.MediaStore.Files.FileColumns.STORAGE_ID + "=?";

    private static final java.lang.String FORMAT_WHERE = android.provider.MediaStore.Files.FileColumns.FORMAT + "=?";

    private static final java.lang.String PARENT_WHERE = android.provider.MediaStore.Files.FileColumns.PARENT + "=?";

    private static final java.lang.String STORAGE_FORMAT_WHERE = ((android.mtp.MtpDatabase.STORAGE_WHERE + " AND ") + android.provider.MediaStore.Files.FileColumns.FORMAT) + "=?";

    private static final java.lang.String STORAGE_PARENT_WHERE = ((android.mtp.MtpDatabase.STORAGE_WHERE + " AND ") + android.provider.MediaStore.Files.FileColumns.PARENT) + "=?";

    private static final java.lang.String FORMAT_PARENT_WHERE = ((android.mtp.MtpDatabase.FORMAT_WHERE + " AND ") + android.provider.MediaStore.Files.FileColumns.PARENT) + "=?";

    private static final java.lang.String STORAGE_FORMAT_PARENT_WHERE = ((android.mtp.MtpDatabase.STORAGE_FORMAT_WHERE + " AND ") + android.provider.MediaStore.Files.FileColumns.PARENT) + "=?";

    private android.mtp.MtpServer mServer;

    // read from native code
    private int mBatteryLevel;

    private int mBatteryScale;

    static {
        java.lang.System.loadLibrary("media_jni");
    }

    private android.content.BroadcastReceiver mBatteryReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (action.equals(android.content.Intent.ACTION_BATTERY_CHANGED)) {
                mBatteryScale = intent.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, 0);
                int newLevel = intent.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, 0);
                if (newLevel != mBatteryLevel) {
                    mBatteryLevel = newLevel;
                    if (mServer != null) {
                        // send device property changed event
                        mServer.sendDevicePropertyChanged(android.mtp.MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL);
                    }
                }
            }
        }
    };

    public MtpDatabase(android.content.Context context, java.lang.String volumeName, java.lang.String storagePath, java.lang.String[] subDirectories) {
        native_setup();
        mContext = context;
        mPackageName = context.getPackageName();
        mMediaProvider = context.getContentResolver().acquireContentProviderClient(android.provider.MediaStore.AUTHORITY);
        mVolumeName = volumeName;
        mMediaStoragePath = storagePath;
        mObjectsUri = android.provider.MediaStore.Files.getMtpObjectsUri(volumeName);
        mMediaScanner = new android.media.MediaScanner(context, mVolumeName);
        mSubDirectories = subDirectories;
        if (subDirectories != null) {
            // Compute "where" string for restricting queries to subdirectories
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("(");
            int count = subDirectories.length;
            for (int i = 0; i < count; i++) {
                builder.append(((android.provider.MediaStore.Files.FileColumns.DATA + "=? OR ") + android.provider.MediaStore.Files.FileColumns.DATA) + " LIKE ?");
                if (i != (count - 1)) {
                    builder.append(" OR ");
                }
            }
            builder.append(")");
            mSubDirectoriesWhere = builder.toString();
            // Compute "where" arguments for restricting queries to subdirectories
            mSubDirectoriesWhereArgs = new java.lang.String[count * 2];
            for (int i = 0, j = 0; i < count; i++) {
                java.lang.String path = subDirectories[i];
                mSubDirectoriesWhereArgs[j++] = path;
                mSubDirectoriesWhereArgs[j++] = path + "/%";
            }
        }
        initDeviceProperties(context);
        mCloseGuard.open("close");
    }

    public void setServer(android.mtp.MtpServer server) {
        mServer = server;
        // always unregister before registering
        try {
            mContext.unregisterReceiver(mBatteryReceiver);
        } catch (java.lang.IllegalArgumentException e) {
            // wasn't previously registered, ignore
        }
        // register for battery notifications when we are connected
        if (server != null) {
            mContext.registerReceiver(mBatteryReceiver, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED));
        }
    }

    @java.lang.Override
    public void close() {
        mCloseGuard.close();
        if (mClosed.compareAndSet(false, true)) {
            mMediaScanner.close();
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

    public void addStorage(android.mtp.MtpStorage storage) {
        mStorageMap.put(storage.getPath(), storage);
    }

    public void removeStorage(android.mtp.MtpStorage storage) {
        mStorageMap.remove(storage.getPath());
    }

    private void initDeviceProperties(android.content.Context context) {
        final java.lang.String devicePropertiesName = "device-properties";
        mDeviceProperties = context.getSharedPreferences(devicePropertiesName, android.content.Context.MODE_PRIVATE);
        java.io.File databaseFile = context.getDatabasePath(devicePropertiesName);
        if (databaseFile.exists()) {
            // for backward compatibility - read device properties from sqlite database
            // and migrate them to shared prefs
            android.database.sqlite.SQLiteDatabase db = null;
            android.database.Cursor c = null;
            try {
                db = context.openOrCreateDatabase("device-properties", android.content.Context.MODE_PRIVATE, null);
                if (db != null) {
                    c = db.query("properties", new java.lang.String[]{ "_id", "code", "value" }, null, null, null, null, null);
                    if (c != null) {
                        android.content.SharedPreferences.Editor e = mDeviceProperties.edit();
                        while (c.moveToNext()) {
                            java.lang.String name = c.getString(1);
                            java.lang.String value = c.getString(2);
                            e.putString(name, value);
                        } 
                        e.commit();
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.mtp.MtpDatabase.TAG, "failed to migrate device properties", e);
            } finally {
                if (c != null)
                    c.close();

                if (db != null)
                    db.close();

            }
            context.deleteDatabase(devicePropertiesName);
        }
    }

    // check to see if the path is contained in one of our storage subdirectories
    // returns true if we have no special subdirectories
    private boolean inStorageSubDirectory(java.lang.String path) {
        if (mSubDirectories == null)
            return true;

        if (path == null)
            return false;

        boolean allowed = false;
        int pathLength = path.length();
        for (int i = 0; (i < mSubDirectories.length) && (!allowed); i++) {
            java.lang.String subdir = mSubDirectories[i];
            int subdirLength = subdir.length();
            if (((subdirLength < pathLength) && (path.charAt(subdirLength) == '/')) && path.startsWith(subdir)) {
                allowed = true;
            }
        }
        return allowed;
    }

    // check to see if the path matches one of our storage subdirectories
    // returns true if we have no special subdirectories
    private boolean isStorageSubDirectory(java.lang.String path) {
        if (mSubDirectories == null)
            return false;

        for (int i = 0; i < mSubDirectories.length; i++) {
            if (path.equals(mSubDirectories[i])) {
                return true;
            }
        }
        return false;
    }

    // returns true if the path is in the storage root
    private boolean inStorageRoot(java.lang.String path) {
        try {
            java.io.File f = new java.io.File(path);
            java.lang.String canonical = f.getCanonicalPath();
            for (java.lang.String root : mStorageMap.keySet()) {
                if (canonical.startsWith(root)) {
                    return true;
                }
            }
        } catch (java.io.IOException e) {
            // ignore
        }
        return false;
    }

    private int beginSendObject(java.lang.String path, int format, int parent, int storageId, long size, long modified) {
        // if the path is outside of the storage root, do not allow access
        if (!inStorageRoot(path)) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "attempt to put file outside of storage area: " + path);
            return -1;
        }
        // if mSubDirectories is not null, do not allow copying files to any other locations
        if (!inStorageSubDirectory(path))
            return -1;

        // make sure the object does not exist
        if (path != null) {
            android.database.Cursor c = null;
            try {
                c = mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.ID_PROJECTION, android.mtp.MtpDatabase.PATH_WHERE, new java.lang.String[]{ path }, null, null);
                if ((c != null) && (c.getCount() > 0)) {
                    android.util.Log.w(android.mtp.MtpDatabase.TAG, "file already exists in beginSendObject: " + path);
                    return -1;
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in beginSendObject", e);
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        mDatabaseModified = true;
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(android.provider.MediaStore.Files.FileColumns.DATA, path);
        values.put(android.provider.MediaStore.Files.FileColumns.FORMAT, format);
        values.put(android.provider.MediaStore.Files.FileColumns.PARENT, parent);
        values.put(android.provider.MediaStore.Files.FileColumns.STORAGE_ID, storageId);
        values.put(android.provider.MediaStore.Files.FileColumns.SIZE, size);
        values.put(android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED, modified);
        try {
            android.net.Uri uri = mMediaProvider.insert(mObjectsUri, values);
            if (uri != null) {
                return java.lang.Integer.parseInt(uri.getPathSegments().get(2));
            } else {
                return -1;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in beginSendObject", e);
            return -1;
        }
    }

    private void endSendObject(java.lang.String path, int handle, int format, boolean succeeded) {
        if (succeeded) {
            // handle abstract playlists separately
            // they do not exist in the file system so don't use the media scanner here
            if (format == android.mtp.MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST) {
                // extract name from path
                java.lang.String name = path;
                int lastSlash = name.lastIndexOf('/');
                if (lastSlash >= 0) {
                    name = name.substring(lastSlash + 1);
                }
                // strip trailing ".pla" from the name
                if (name.endsWith(".pla")) {
                    name = name.substring(0, name.length() - 4);
                }
                android.content.ContentValues values = new android.content.ContentValues(1);
                values.put(android.provider.MediaStore.Audio.Playlists.DATA, path);
                values.put(android.provider.MediaStore.Audio.Playlists.NAME, name);
                values.put(android.provider.MediaStore.Files.FileColumns.FORMAT, format);
                values.put(android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED, java.lang.System.currentTimeMillis() / 1000);
                values.put(android.provider.MediaStore.MediaColumns.MEDIA_SCANNER_NEW_OBJECT_ID, handle);
                try {
                    android.net.Uri uri = mMediaProvider.insert(android.provider.MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in endSendObject", e);
                }
            } else {
                mMediaScanner.scanMtpFile(path, handle, format);
            }
        } else {
            deleteFile(handle);
        }
    }

    private android.database.Cursor createObjectQuery(int storageID, int format, int parent) throws android.os.RemoteException {
        java.lang.String where;
        java.lang.String[] whereArgs;
        if (storageID == 0xffffffff) {
            // query all stores
            if (format == 0) {
                // query all formats
                if (parent == 0) {
                    // query all objects
                    where = null;
                    whereArgs = null;
                } else {
                    if (parent == 0xffffffff) {
                        // all objects in root of store
                        parent = 0;
                    }
                    where = android.mtp.MtpDatabase.PARENT_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(parent) };
                }
            } else {
                // query specific format
                if (parent == 0) {
                    // query all objects
                    where = android.mtp.MtpDatabase.FORMAT_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(format) };
                } else {
                    if (parent == 0xffffffff) {
                        // all objects in root of store
                        parent = 0;
                    }
                    where = android.mtp.MtpDatabase.FORMAT_PARENT_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(format), java.lang.Integer.toString(parent) };
                }
            }
        } else {
            // query specific store
            if (format == 0) {
                // query all formats
                if (parent == 0) {
                    // query all objects
                    where = android.mtp.MtpDatabase.STORAGE_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(storageID) };
                } else {
                    if (parent == 0xffffffff) {
                        // all objects in root of store
                        parent = 0;
                    }
                    where = android.mtp.MtpDatabase.STORAGE_PARENT_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(storageID), java.lang.Integer.toString(parent) };
                }
            } else {
                // query specific format
                if (parent == 0) {
                    // query all objects
                    where = android.mtp.MtpDatabase.STORAGE_FORMAT_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(storageID), java.lang.Integer.toString(format) };
                } else {
                    if (parent == 0xffffffff) {
                        // all objects in root of store
                        parent = 0;
                    }
                    where = android.mtp.MtpDatabase.STORAGE_FORMAT_PARENT_WHERE;
                    whereArgs = new java.lang.String[]{ java.lang.Integer.toString(storageID), java.lang.Integer.toString(format), java.lang.Integer.toString(parent) };
                }
            }
        }
        // if we are restricting queries to mSubDirectories, we need to add the restriction
        // onto our "where" arguments
        if (mSubDirectoriesWhere != null) {
            if (where == null) {
                where = mSubDirectoriesWhere;
                whereArgs = mSubDirectoriesWhereArgs;
            } else {
                where = (where + " AND ") + mSubDirectoriesWhere;
                // create new array to hold whereArgs and mSubDirectoriesWhereArgs
                java.lang.String[] newWhereArgs = new java.lang.String[whereArgs.length + mSubDirectoriesWhereArgs.length];
                int i;
                int j;
                for (i = 0; i < whereArgs.length; i++) {
                    newWhereArgs[i] = whereArgs[i];
                }
                for (j = 0; j < mSubDirectoriesWhereArgs.length; i++ , j++) {
                    newWhereArgs[i] = mSubDirectoriesWhereArgs[j];
                }
                whereArgs = newWhereArgs;
            }
        }
        return mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.ID_PROJECTION, where, whereArgs, null, null);
    }

    private int[] getObjectList(int storageID, int format, int parent) {
        android.database.Cursor c = null;
        try {
            c = createObjectQuery(storageID, format, parent);
            if (c == null) {
                return null;
            }
            int count = c.getCount();
            if (count > 0) {
                int[] result = new int[count];
                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    result[i] = c.getInt(0);
                }
                return result;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getObjectList", e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    private int getNumObjects(int storageID, int format, int parent) {
        android.database.Cursor c = null;
        try {
            c = createObjectQuery(storageID, format, parent);
            if (c != null) {
                return c.getCount();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getNumObjects", e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return -1;
    }

    private int[] getSupportedPlaybackFormats() {
        return new int[]{ // allow transfering arbitrary files
        android.mtp.MtpConstants.FORMAT_UNDEFINED, android.mtp.MtpConstants.FORMAT_ASSOCIATION, android.mtp.MtpConstants.FORMAT_TEXT, android.mtp.MtpConstants.FORMAT_HTML, android.mtp.MtpConstants.FORMAT_WAV, android.mtp.MtpConstants.FORMAT_MP3, android.mtp.MtpConstants.FORMAT_MPEG, android.mtp.MtpConstants.FORMAT_EXIF_JPEG, android.mtp.MtpConstants.FORMAT_TIFF_EP, android.mtp.MtpConstants.FORMAT_BMP, android.mtp.MtpConstants.FORMAT_GIF, android.mtp.MtpConstants.FORMAT_JFIF, android.mtp.MtpConstants.FORMAT_PNG, android.mtp.MtpConstants.FORMAT_TIFF, android.mtp.MtpConstants.FORMAT_WMA, android.mtp.MtpConstants.FORMAT_OGG, android.mtp.MtpConstants.FORMAT_AAC, android.mtp.MtpConstants.FORMAT_MP4_CONTAINER, android.mtp.MtpConstants.FORMAT_MP2, android.mtp.MtpConstants.FORMAT_3GP_CONTAINER, android.mtp.MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST, android.mtp.MtpConstants.FORMAT_WPL_PLAYLIST, android.mtp.MtpConstants.FORMAT_M3U_PLAYLIST, android.mtp.MtpConstants.FORMAT_PLS_PLAYLIST, android.mtp.MtpConstants.FORMAT_XML_DOCUMENT, android.mtp.MtpConstants.FORMAT_FLAC, android.mtp.MtpConstants.FORMAT_DNG };
    }

    private int[] getSupportedCaptureFormats() {
        // no capture formats yet
        return null;
    }

    static final int[] FILE_PROPERTIES = new int[]{ // NOTE must match beginning of AUDIO_PROPERTIES, VIDEO_PROPERTIES
    // and IMAGE_PROPERTIES below
    android.mtp.MtpConstants.PROPERTY_STORAGE_ID, android.mtp.MtpConstants.PROPERTY_OBJECT_FORMAT, android.mtp.MtpConstants.PROPERTY_PROTECTION_STATUS, android.mtp.MtpConstants.PROPERTY_OBJECT_SIZE, android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME, android.mtp.MtpConstants.PROPERTY_DATE_MODIFIED, android.mtp.MtpConstants.PROPERTY_PARENT_OBJECT, android.mtp.MtpConstants.PROPERTY_PERSISTENT_UID, android.mtp.MtpConstants.PROPERTY_NAME, android.mtp.MtpConstants.PROPERTY_DISPLAY_NAME, android.mtp.MtpConstants.PROPERTY_DATE_ADDED };

    static final int[] AUDIO_PROPERTIES = new int[]{ // NOTE must match FILE_PROPERTIES above
    android.mtp.MtpConstants.PROPERTY_STORAGE_ID, android.mtp.MtpConstants.PROPERTY_OBJECT_FORMAT, android.mtp.MtpConstants.PROPERTY_PROTECTION_STATUS, android.mtp.MtpConstants.PROPERTY_OBJECT_SIZE, android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME, android.mtp.MtpConstants.PROPERTY_DATE_MODIFIED, android.mtp.MtpConstants.PROPERTY_PARENT_OBJECT, android.mtp.MtpConstants.PROPERTY_PERSISTENT_UID, android.mtp.MtpConstants.PROPERTY_NAME, android.mtp.MtpConstants.PROPERTY_DISPLAY_NAME, android.mtp.MtpConstants.PROPERTY_DATE_ADDED, // audio specific properties
    android.mtp.MtpConstants.PROPERTY_ARTIST, android.mtp.MtpConstants.PROPERTY_ALBUM_NAME, android.mtp.MtpConstants.PROPERTY_ALBUM_ARTIST, android.mtp.MtpConstants.PROPERTY_TRACK, android.mtp.MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE, android.mtp.MtpConstants.PROPERTY_DURATION, android.mtp.MtpConstants.PROPERTY_GENRE, android.mtp.MtpConstants.PROPERTY_COMPOSER, android.mtp.MtpConstants.PROPERTY_AUDIO_WAVE_CODEC, android.mtp.MtpConstants.PROPERTY_BITRATE_TYPE, android.mtp.MtpConstants.PROPERTY_AUDIO_BITRATE, android.mtp.MtpConstants.PROPERTY_NUMBER_OF_CHANNELS, android.mtp.MtpConstants.PROPERTY_SAMPLE_RATE };

    static final int[] VIDEO_PROPERTIES = new int[]{ // NOTE must match FILE_PROPERTIES above
    android.mtp.MtpConstants.PROPERTY_STORAGE_ID, android.mtp.MtpConstants.PROPERTY_OBJECT_FORMAT, android.mtp.MtpConstants.PROPERTY_PROTECTION_STATUS, android.mtp.MtpConstants.PROPERTY_OBJECT_SIZE, android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME, android.mtp.MtpConstants.PROPERTY_DATE_MODIFIED, android.mtp.MtpConstants.PROPERTY_PARENT_OBJECT, android.mtp.MtpConstants.PROPERTY_PERSISTENT_UID, android.mtp.MtpConstants.PROPERTY_NAME, android.mtp.MtpConstants.PROPERTY_DISPLAY_NAME, android.mtp.MtpConstants.PROPERTY_DATE_ADDED, // video specific properties
    android.mtp.MtpConstants.PROPERTY_ARTIST, android.mtp.MtpConstants.PROPERTY_ALBUM_NAME, android.mtp.MtpConstants.PROPERTY_DURATION, android.mtp.MtpConstants.PROPERTY_DESCRIPTION };

    static final int[] IMAGE_PROPERTIES = new int[]{ // NOTE must match FILE_PROPERTIES above
    android.mtp.MtpConstants.PROPERTY_STORAGE_ID, android.mtp.MtpConstants.PROPERTY_OBJECT_FORMAT, android.mtp.MtpConstants.PROPERTY_PROTECTION_STATUS, android.mtp.MtpConstants.PROPERTY_OBJECT_SIZE, android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME, android.mtp.MtpConstants.PROPERTY_DATE_MODIFIED, android.mtp.MtpConstants.PROPERTY_PARENT_OBJECT, android.mtp.MtpConstants.PROPERTY_PERSISTENT_UID, android.mtp.MtpConstants.PROPERTY_NAME, android.mtp.MtpConstants.PROPERTY_DISPLAY_NAME, android.mtp.MtpConstants.PROPERTY_DATE_ADDED, // image specific properties
    android.mtp.MtpConstants.PROPERTY_DESCRIPTION };

    private int[] getSupportedObjectProperties(int format) {
        switch (format) {
            case android.mtp.MtpConstants.FORMAT_MP3 :
            case android.mtp.MtpConstants.FORMAT_WAV :
            case android.mtp.MtpConstants.FORMAT_WMA :
            case android.mtp.MtpConstants.FORMAT_OGG :
            case android.mtp.MtpConstants.FORMAT_AAC :
                return android.mtp.MtpDatabase.AUDIO_PROPERTIES;
            case android.mtp.MtpConstants.FORMAT_MPEG :
            case android.mtp.MtpConstants.FORMAT_3GP_CONTAINER :
            case android.mtp.MtpConstants.FORMAT_WMV :
                return android.mtp.MtpDatabase.VIDEO_PROPERTIES;
            case android.mtp.MtpConstants.FORMAT_EXIF_JPEG :
            case android.mtp.MtpConstants.FORMAT_GIF :
            case android.mtp.MtpConstants.FORMAT_PNG :
            case android.mtp.MtpConstants.FORMAT_BMP :
            case android.mtp.MtpConstants.FORMAT_DNG :
                return android.mtp.MtpDatabase.IMAGE_PROPERTIES;
            default :
                return android.mtp.MtpDatabase.FILE_PROPERTIES;
        }
    }

    private int[] getSupportedDeviceProperties() {
        return new int[]{ android.mtp.MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER, android.mtp.MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME, android.mtp.MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE, android.mtp.MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL };
    }

    private android.mtp.MtpPropertyList getObjectPropertyList(int handle, int format, int property, int groupCode, int depth) {
        // FIXME - implement group support
        if (groupCode != 0) {
            return new android.mtp.MtpPropertyList(0, android.mtp.MtpConstants.RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED);
        }
        android.mtp.MtpPropertyGroup propertyGroup;
        if (property == 0xffffffff) {
            if (((format == 0) && (handle != 0)) && (handle != 0xffffffff)) {
                // return properties based on the object's format
                format = getObjectFormat(handle);
            }
            propertyGroup = mPropertyGroupsByFormat.get(format);
            if (propertyGroup == null) {
                int[] propertyList = getSupportedObjectProperties(format);
                propertyGroup = new android.mtp.MtpPropertyGroup(this, mMediaProvider, mVolumeName, propertyList);
                mPropertyGroupsByFormat.put(format, propertyGroup);
            }
        } else {
            propertyGroup = mPropertyGroupsByProperty.get(property);
            if (propertyGroup == null) {
                final int[] propertyList = new int[]{ property };
                propertyGroup = new android.mtp.MtpPropertyGroup(this, mMediaProvider, mVolumeName, propertyList);
                mPropertyGroupsByProperty.put(property, propertyGroup);
            }
        }
        return propertyGroup.getPropertyList(handle, format, depth);
    }

    private int renameFile(int handle, java.lang.String newName) {
        android.database.Cursor c = null;
        // first compute current path
        java.lang.String path = null;
        java.lang.String[] whereArgs = new java.lang.String[]{ java.lang.Integer.toString(handle) };
        try {
            c = mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.PATH_PROJECTION, android.mtp.MtpDatabase.ID_WHERE, whereArgs, null, null);
            if ((c != null) && c.moveToNext()) {
                path = c.getString(1);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getObjectFilePath", e);
            return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        } finally {
            if (c != null) {
                c.close();
            }
        }
        if (path == null) {
            return android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        // do not allow renaming any of the special subdirectories
        if (isStorageSubDirectory(path)) {
            return android.mtp.MtpConstants.RESPONSE_OBJECT_WRITE_PROTECTED;
        }
        // now rename the file.  make sure this succeeds before updating database
        java.io.File oldFile = new java.io.File(path);
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash <= 1) {
            return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        }
        java.lang.String newPath = path.substring(0, lastSlash + 1) + newName;
        java.io.File newFile = new java.io.File(newPath);
        boolean success = oldFile.renameTo(newFile);
        if (!success) {
            android.util.Log.w(android.mtp.MtpDatabase.TAG, ((("renaming " + path) + " to ") + newPath) + " failed");
            return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        }
        // finally update database
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(android.provider.MediaStore.Files.FileColumns.DATA, newPath);
        int updated = 0;
        try {
            // note - we are relying on a special case in MediaProvider.update() to update
            // the paths for all children in the case where this is a directory.
            updated = mMediaProvider.update(mObjectsUri, values, android.mtp.MtpDatabase.ID_WHERE, whereArgs);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in mMediaProvider.update", e);
        }
        if (updated == 0) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, (("Unable to update path for " + path) + " to ") + newPath);
            // this shouldn't happen, but if it does we need to rename the file to its original name
            newFile.renameTo(oldFile);
            return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        }
        // check if nomedia status changed
        if (newFile.isDirectory()) {
            // for directories, check if renamed from something hidden to something non-hidden
            if (oldFile.getName().startsWith(".") && (!newPath.startsWith("."))) {
                // directory was unhidden
                try {
                    mMediaProvider.call(android.provider.MediaStore.UNHIDE_CALL, newPath, null);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.mtp.MtpDatabase.TAG, "failed to unhide/rescan for " + newPath);
                }
            }
        } else {
            // for files, check if renamed from .nomedia to something else
            if (oldFile.getName().toLowerCase(java.util.Locale.US).equals(".nomedia") && (!newPath.toLowerCase(java.util.Locale.US).equals(".nomedia"))) {
                try {
                    mMediaProvider.call(android.provider.MediaStore.UNHIDE_CALL, oldFile.getParent(), null);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.mtp.MtpDatabase.TAG, "failed to unhide/rescan for " + newPath);
                }
            }
        }
        return android.mtp.MtpConstants.RESPONSE_OK;
    }

    private int setObjectProperty(int handle, int property, long intValue, java.lang.String stringValue) {
        switch (property) {
            case android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME :
                return renameFile(handle, stringValue);
            default :
                return android.mtp.MtpConstants.RESPONSE_OBJECT_PROP_NOT_SUPPORTED;
        }
    }

    private int getDeviceProperty(int property, long[] outIntValue, char[] outStringValue) {
        switch (property) {
            case android.mtp.MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER :
            case android.mtp.MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME :
                // writable string properties kept in shared preferences
                java.lang.String value = mDeviceProperties.getString(java.lang.Integer.toString(property), "");
                int length = value.length();
                if (length > 255) {
                    length = 255;
                }
                value.getChars(0, length, outStringValue, 0);
                outStringValue[length] = 0;
                return android.mtp.MtpConstants.RESPONSE_OK;
            case android.mtp.MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE :
                // use screen size as max image size
                android.view.Display display = ((android.view.WindowManager) (mContext.getSystemService(android.content.Context.WINDOW_SERVICE))).getDefaultDisplay();
                int width = display.getMaximumSizeDimension();
                int height = display.getMaximumSizeDimension();
                java.lang.String imageSize = (java.lang.Integer.toString(width) + "x") + java.lang.Integer.toString(height);
                imageSize.getChars(0, imageSize.length(), outStringValue, 0);
                outStringValue[imageSize.length()] = 0;
                return android.mtp.MtpConstants.RESPONSE_OK;
                // DEVICE_PROPERTY_BATTERY_LEVEL is implemented in the JNI code
            default :
                return android.mtp.MtpConstants.RESPONSE_DEVICE_PROP_NOT_SUPPORTED;
        }
    }

    private int setDeviceProperty(int property, long intValue, java.lang.String stringValue) {
        switch (property) {
            case android.mtp.MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER :
            case android.mtp.MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME :
                // writable string properties kept in shared prefs
                android.content.SharedPreferences.Editor e = mDeviceProperties.edit();
                e.putString(java.lang.Integer.toString(property), stringValue);
                return e.commit() ? android.mtp.MtpConstants.RESPONSE_OK : android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        }
        return android.mtp.MtpConstants.RESPONSE_DEVICE_PROP_NOT_SUPPORTED;
    }

    private boolean getObjectInfo(int handle, int[] outStorageFormatParent, char[] outName, long[] outCreatedModified) {
        android.database.Cursor c = null;
        try {
            c = mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.OBJECT_INFO_PROJECTION, android.mtp.MtpDatabase.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(handle) }, null, null);
            if ((c != null) && c.moveToNext()) {
                outStorageFormatParent[0] = c.getInt(1);
                outStorageFormatParent[1] = c.getInt(2);
                outStorageFormatParent[2] = c.getInt(3);
                // extract name from path
                java.lang.String path = c.getString(4);
                int lastSlash = path.lastIndexOf('/');
                int start = (lastSlash >= 0) ? lastSlash + 1 : 0;
                int end = path.length();
                if ((end - start) > 255) {
                    end = start + 255;
                }
                path.getChars(start, end, outName, 0);
                outName[end - start] = 0;
                outCreatedModified[0] = c.getLong(5);
                outCreatedModified[1] = c.getLong(6);
                // use modification date as creation date if date added is not set
                if (outCreatedModified[0] == 0) {
                    outCreatedModified[0] = outCreatedModified[1];
                }
                return true;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getObjectInfo", e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;
    }

    private int getObjectFilePath(int handle, char[] outFilePath, long[] outFileLengthFormat) {
        if (handle == 0) {
            // special case root directory
            mMediaStoragePath.getChars(0, mMediaStoragePath.length(), outFilePath, 0);
            outFilePath[mMediaStoragePath.length()] = 0;
            outFileLengthFormat[0] = 0;
            outFileLengthFormat[1] = android.mtp.MtpConstants.FORMAT_ASSOCIATION;
            return android.mtp.MtpConstants.RESPONSE_OK;
        }
        android.database.Cursor c = null;
        try {
            c = mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.PATH_FORMAT_PROJECTION, android.mtp.MtpDatabase.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(handle) }, null, null);
            if ((c != null) && c.moveToNext()) {
                java.lang.String path = c.getString(1);
                path.getChars(0, path.length(), outFilePath, 0);
                outFilePath[path.length()] = 0;
                // File transfers from device to host will likely fail if the size is incorrect.
                // So to be safe, use the actual file size here.
                outFileLengthFormat[0] = new java.io.File(path).length();
                outFileLengthFormat[1] = c.getLong(2);
                return android.mtp.MtpConstants.RESPONSE_OK;
            } else {
                return android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getObjectFilePath", e);
            return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private int getObjectFormat(int handle) {
        android.database.Cursor c = null;
        try {
            c = mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.FORMAT_PROJECTION, android.mtp.MtpDatabase.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(handle) }, null, null);
            if ((c != null) && c.moveToNext()) {
                return c.getInt(1);
            } else {
                return -1;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getObjectFilePath", e);
            return -1;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private int deleteFile(int handle) {
        mDatabaseModified = true;
        java.lang.String path = null;
        int format = 0;
        android.database.Cursor c = null;
        try {
            c = mMediaProvider.query(mObjectsUri, android.mtp.MtpDatabase.PATH_FORMAT_PROJECTION, android.mtp.MtpDatabase.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(handle) }, null, null);
            if ((c != null) && c.moveToNext()) {
                // don't convert to media path here, since we will be matching
                // against paths in the database matching /data/media
                path = c.getString(1);
                format = c.getInt(2);
            } else {
                return android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
            }
            if ((path == null) || (format == 0)) {
                return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
            }
            // do not allow deleting any of the special subdirectories
            if (isStorageSubDirectory(path)) {
                return android.mtp.MtpConstants.RESPONSE_OBJECT_WRITE_PROTECTED;
            }
            if (format == android.mtp.MtpConstants.FORMAT_ASSOCIATION) {
                // recursive case - delete all children first
                android.net.Uri uri = android.provider.MediaStore.Files.getMtpObjectsUri(mVolumeName);
                int count = // the 'like' makes it use the index, the 'lower()' makes it correct
                // when the path contains sqlite wildcard characters
                mMediaProvider.delete(uri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new java.lang.String[]{ path + "/%", java.lang.Integer.toString(path.length() + 1), path + "/" });
            }
            android.net.Uri uri = android.provider.MediaStore.Files.getMtpObjectsUri(mVolumeName, handle);
            if (mMediaProvider.delete(uri, null, null) > 0) {
                if ((format != android.mtp.MtpConstants.FORMAT_ASSOCIATION) && path.toLowerCase(java.util.Locale.US).endsWith("/.nomedia")) {
                    try {
                        java.lang.String parentPath = path.substring(0, path.lastIndexOf("/"));
                        mMediaProvider.call(android.provider.MediaStore.UNHIDE_CALL, parentPath, null);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(android.mtp.MtpDatabase.TAG, "failed to unhide/rescan for " + path);
                    }
                }
                return android.mtp.MtpConstants.RESPONSE_OK;
            } else {
                return android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in deleteFile", e);
            return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private int[] getObjectReferences(int handle) {
        android.net.Uri uri = android.provider.MediaStore.Files.getMtpReferencesUri(mVolumeName, handle);
        android.database.Cursor c = null;
        try {
            c = mMediaProvider.query(uri, android.mtp.MtpDatabase.ID_PROJECTION, null, null, null, null);
            if (c == null) {
                return null;
            }
            int count = c.getCount();
            if (count > 0) {
                int[] result = new int[count];
                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    result[i] = c.getInt(0);
                }
                return result;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in getObjectList", e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    private int setObjectReferences(int handle, int[] references) {
        mDatabaseModified = true;
        android.net.Uri uri = android.provider.MediaStore.Files.getMtpReferencesUri(mVolumeName, handle);
        int count = references.length;
        android.content.ContentValues[] valuesList = new android.content.ContentValues[count];
        for (int i = 0; i < count; i++) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.MediaStore.Files.FileColumns._ID, references[i]);
            valuesList[i] = values;
        }
        try {
            if (mMediaProvider.bulkInsert(uri, valuesList) > 0) {
                return android.mtp.MtpConstants.RESPONSE_OK;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.mtp.MtpDatabase.TAG, "RemoteException in setObjectReferences", e);
        }
        return android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR;
    }

    private void sessionStarted() {
        mDatabaseModified = false;
    }

    private void sessionEnded() {
        if (mDatabaseModified) {
            mContext.sendBroadcast(new android.content.Intent(android.provider.MediaStore.ACTION_MTP_SESSION_END));
            mDatabaseModified = false;
        }
    }

    // used by the JNI code
    private long mNativeContext;

    private final native void native_setup();

    private final native void native_finalize();
}

