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
 * RingtoneManager provides access to ringtones, notification, and other types
 * of sounds. It manages querying the different media providers and combines the
 * results into a single cursor. It also provides a {@link Ringtone} for each
 * ringtone. We generically call these sounds ringtones, however the
 * {@link #TYPE_RINGTONE} refers to the type of sounds that are suitable for the
 * phone ringer.
 * <p>
 * To show a ringtone picker to the user, use the
 * {@link #ACTION_RINGTONE_PICKER} intent to launch the picker as a subactivity.
 *
 * @see Ringtone
 */
public class RingtoneManager {
    private static final java.lang.String TAG = "RingtoneManager";

    // Make sure these are in sync with attrs.xml:
    // <attr name="ringtoneType">
    /**
     * Type that refers to sounds that are used for the phone ringer.
     */
    public static final int TYPE_RINGTONE = 1;

    /**
     * Type that refers to sounds that are used for notifications.
     */
    public static final int TYPE_NOTIFICATION = 2;

    /**
     * Type that refers to sounds that are used for the alarm.
     */
    public static final int TYPE_ALARM = 4;

    /**
     * All types of sounds.
     */
    public static final int TYPE_ALL = (android.media.RingtoneManager.TYPE_RINGTONE | android.media.RingtoneManager.TYPE_NOTIFICATION) | android.media.RingtoneManager.TYPE_ALARM;

    // </attr>
    /**
     * Activity Action: Shows a ringtone picker.
     * <p>
     * Input: {@link #EXTRA_RINGTONE_EXISTING_URI},
     * {@link #EXTRA_RINGTONE_SHOW_DEFAULT},
     * {@link #EXTRA_RINGTONE_SHOW_SILENT}, {@link #EXTRA_RINGTONE_TYPE},
     * {@link #EXTRA_RINGTONE_DEFAULT_URI}, {@link #EXTRA_RINGTONE_TITLE},
     * <p>
     * Output: {@link #EXTRA_RINGTONE_PICKED_URI}.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_RINGTONE_PICKER = "android.intent.action.RINGTONE_PICKER";

    /**
     * Given to the ringtone picker as a boolean. Whether to show an item for
     * "Default".
     *
     * @see #ACTION_RINGTONE_PICKER
     */
    public static final java.lang.String EXTRA_RINGTONE_SHOW_DEFAULT = "android.intent.extra.ringtone.SHOW_DEFAULT";

    /**
     * Given to the ringtone picker as a boolean. Whether to show an item for
     * "Silent". If the "Silent" item is picked,
     * {@link #EXTRA_RINGTONE_PICKED_URI} will be null.
     *
     * @see #ACTION_RINGTONE_PICKER
     */
    public static final java.lang.String EXTRA_RINGTONE_SHOW_SILENT = "android.intent.extra.ringtone.SHOW_SILENT";

    /**
     * Given to the ringtone picker as a boolean. Whether to include DRM ringtones.
     *
     * @deprecated DRM ringtones are no longer supported
     */
    @java.lang.Deprecated
    public static final java.lang.String EXTRA_RINGTONE_INCLUDE_DRM = "android.intent.extra.ringtone.INCLUDE_DRM";

    /**
     * Given to the ringtone picker as a {@link Uri}. The {@link Uri} of the
     * current ringtone, which will be used to show a checkmark next to the item
     * for this {@link Uri}. If showing an item for "Default" (@see
     * {@link #EXTRA_RINGTONE_SHOW_DEFAULT}), this can also be one of
     * {@link System#DEFAULT_RINGTONE_URI},
     * {@link System#DEFAULT_NOTIFICATION_URI}, or
     * {@link System#DEFAULT_ALARM_ALERT_URI} to have the "Default" item
     * checked.
     *
     * @see #ACTION_RINGTONE_PICKER
     */
    public static final java.lang.String EXTRA_RINGTONE_EXISTING_URI = "android.intent.extra.ringtone.EXISTING_URI";

    /**
     * Given to the ringtone picker as a {@link Uri}. The {@link Uri} of the
     * ringtone to play when the user attempts to preview the "Default"
     * ringtone. This can be one of {@link System#DEFAULT_RINGTONE_URI},
     * {@link System#DEFAULT_NOTIFICATION_URI}, or
     * {@link System#DEFAULT_ALARM_ALERT_URI} to have the "Default" point to
     * the current sound for the given default sound type. If you are showing a
     * ringtone picker for some other type of sound, you are free to provide any
     * {@link Uri} here.
     */
    public static final java.lang.String EXTRA_RINGTONE_DEFAULT_URI = "android.intent.extra.ringtone.DEFAULT_URI";

    /**
     * Given to the ringtone picker as an int. Specifies which ringtone type(s) should be
     * shown in the picker. One or more of {@link #TYPE_RINGTONE},
     * {@link #TYPE_NOTIFICATION}, {@link #TYPE_ALARM}, or {@link #TYPE_ALL}
     * (bitwise-ored together).
     */
    public static final java.lang.String EXTRA_RINGTONE_TYPE = "android.intent.extra.ringtone.TYPE";

    /**
     * Given to the ringtone picker as a {@link CharSequence}. The title to
     * show for the ringtone picker. This has a default value that is suitable
     * in most cases.
     */
    public static final java.lang.String EXTRA_RINGTONE_TITLE = "android.intent.extra.ringtone.TITLE";

    /**
     *
     *
     * @unknown Given to the ringtone picker as an int. Additional AudioAttributes flags to use
    when playing the ringtone in the picker.
     * @see #ACTION_RINGTONE_PICKER
     */
    public static final java.lang.String EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS = "android.intent.extra.ringtone.AUDIO_ATTRIBUTES_FLAGS";

    /**
     * Returned from the ringtone picker as a {@link Uri}.
     * <p>
     * It will be one of:
     * <li> the picked ringtone,
     * <li> a {@link Uri} that equals {@link System#DEFAULT_RINGTONE_URI},
     * {@link System#DEFAULT_NOTIFICATION_URI}, or
     * {@link System#DEFAULT_ALARM_ALERT_URI} if the default was chosen,
     * <li> null if the "Silent" item was picked.
     *
     * @see #ACTION_RINGTONE_PICKER
     */
    public static final java.lang.String EXTRA_RINGTONE_PICKED_URI = "android.intent.extra.ringtone.PICKED_URI";

    // Make sure the column ordering and then ..._COLUMN_INDEX are in sync
    private static final java.lang.String[] INTERNAL_COLUMNS = new java.lang.String[]{ android.provider.MediaStore.Audio.Media._ID, android.provider.MediaStore.Audio.Media.TITLE, ("\"" + android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI) + "\"", android.provider.MediaStore.Audio.Media.TITLE_KEY };

    private static final java.lang.String[] MEDIA_COLUMNS = new java.lang.String[]{ android.provider.MediaStore.Audio.Media._ID, android.provider.MediaStore.Audio.Media.TITLE, ("\"" + android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) + "\"", android.provider.MediaStore.Audio.Media.TITLE_KEY };

    /**
     * The column index (in the cursor returned by {@link #getCursor()} for the
     * row ID.
     */
    public static final int ID_COLUMN_INDEX = 0;

    /**
     * The column index (in the cursor returned by {@link #getCursor()} for the
     * title.
     */
    public static final int TITLE_COLUMN_INDEX = 1;

    /**
     * The column index (in the cursor returned by {@link #getCursor()} for the
     * media provider's URI.
     */
    public static final int URI_COLUMN_INDEX = 2;

    private final android.app.Activity mActivity;

    private final android.content.Context mContext;

    private android.database.Cursor mCursor;

    private int mType = android.media.RingtoneManager.TYPE_RINGTONE;

    /**
     * If a column (item from this list) exists in the Cursor, its value must
     * be true (value of 1) for the row to be returned.
     */
    private final java.util.List<java.lang.String> mFilterColumns = new java.util.ArrayList<java.lang.String>();

    private boolean mStopPreviousRingtone = true;

    private android.media.Ringtone mPreviousRingtone;

    /**
     * Constructs a RingtoneManager. This constructor is recommended as its
     * constructed instance manages cursor(s).
     *
     * @param activity
     * 		The activity used to get a managed cursor.
     */
    public RingtoneManager(android.app.Activity activity) {
        mActivity = activity;
        mContext = activity;
        setType(mType);
    }

    /**
     * Constructs a RingtoneManager. The instance constructed by this
     * constructor will not manage the cursor(s), so the client should handle
     * this itself.
     *
     * @param context
     * 		The context to used to get a cursor.
     */
    public RingtoneManager(android.content.Context context) {
        mActivity = null;
        mContext = context;
        setType(mType);
    }

    /**
     * Sets which type(s) of ringtones will be listed by this.
     *
     * @param type
     * 		The type(s), one or more of {@link #TYPE_RINGTONE},
     * 		{@link #TYPE_NOTIFICATION}, {@link #TYPE_ALARM},
     * 		{@link #TYPE_ALL}.
     * @see #EXTRA_RINGTONE_TYPE
     */
    public void setType(int type) {
        if (mCursor != null) {
            throw new java.lang.IllegalStateException("Setting filter columns should be done before querying for ringtones.");
        }
        mType = type;
        setFilterColumnsList(type);
    }

    /**
     * Infers the playback stream type based on what type of ringtones this
     * manager is returning.
     *
     * @return The stream type.
     */
    public int inferStreamType() {
        switch (mType) {
            case android.media.RingtoneManager.TYPE_ALARM :
                return android.media.AudioManager.STREAM_ALARM;
            case android.media.RingtoneManager.TYPE_NOTIFICATION :
                return android.media.AudioManager.STREAM_NOTIFICATION;
            default :
                return android.media.AudioManager.STREAM_RING;
        }
    }

    /**
     * Whether retrieving another {@link Ringtone} will stop playing the
     * previously retrieved {@link Ringtone}.
     * <p>
     * If this is false, make sure to {@link Ringtone#stop()} any previous
     * ringtones to free resources.
     *
     * @param stopPreviousRingtone
     * 		If true, the previously retrieved
     * 		{@link Ringtone} will be stopped.
     */
    public void setStopPreviousRingtone(boolean stopPreviousRingtone) {
        mStopPreviousRingtone = stopPreviousRingtone;
    }

    /**
     *
     *
     * @see #setStopPreviousRingtone(boolean)
     */
    public boolean getStopPreviousRingtone() {
        return mStopPreviousRingtone;
    }

    /**
     * Stops playing the last {@link Ringtone} retrieved from this.
     */
    public void stopPreviousRingtone() {
        if (mPreviousRingtone != null) {
            mPreviousRingtone.stop();
        }
    }

    /**
     * Returns whether DRM ringtones will be included.
     *
     * @return Whether DRM ringtones will be included.
     * @see #setIncludeDrm(boolean)
    Obsolete - always returns false
     * @deprecated DRM ringtones are no longer supported
     */
    @java.lang.Deprecated
    public boolean getIncludeDrm() {
        return false;
    }

    /**
     * Sets whether to include DRM ringtones.
     *
     * @param includeDrm
     * 		Whether to include DRM ringtones.
     * 		Obsolete - no longer has any effect
     * @deprecated DRM ringtones are no longer supported
     */
    @java.lang.Deprecated
    public void setIncludeDrm(boolean includeDrm) {
        if (includeDrm) {
            android.util.Log.w(android.media.RingtoneManager.TAG, "setIncludeDrm no longer supported");
        }
    }

    /**
     * Returns a {@link Cursor} of all the ringtones available. The returned
     * cursor will be the same cursor returned each time this method is called,
     * so do not {@link Cursor#close()} the cursor. The cursor can be
     * {@link Cursor#deactivate()} safely.
     * <p>
     * If {@link RingtoneManager#RingtoneManager(Activity)} was not used, the
     * caller should manage the returned cursor through its activity's life
     * cycle to prevent leaking the cursor.
     * <p>
     * Note that the list of ringtones available will differ depending on whether the caller
     * has the {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} permission.
     *
     * @return A {@link Cursor} of all the ringtones available.
     * @see #ID_COLUMN_INDEX
     * @see #TITLE_COLUMN_INDEX
     * @see #URI_COLUMN_INDEX
     */
    public android.database.Cursor getCursor() {
        if ((mCursor != null) && mCursor.requery()) {
            return mCursor;
        }
        final android.database.Cursor internalCursor = getInternalRingtones();
        final android.database.Cursor mediaCursor = getMediaRingtones();
        return mCursor = new com.android.internal.database.SortCursor(new android.database.Cursor[]{ internalCursor, mediaCursor }, android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    /**
     * Gets a {@link Ringtone} for the ringtone at the given position in the
     * {@link Cursor}.
     *
     * @param position
     * 		The position (in the {@link Cursor}) of the ringtone.
     * @return A {@link Ringtone} pointing to the ringtone.
     */
    public android.media.Ringtone getRingtone(int position) {
        if (mStopPreviousRingtone && (mPreviousRingtone != null)) {
            mPreviousRingtone.stop();
        }
        mPreviousRingtone = android.media.RingtoneManager.getRingtone(mContext, getRingtoneUri(position), inferStreamType());
        return mPreviousRingtone;
    }

    /**
     * Gets a {@link Uri} for the ringtone at the given position in the {@link Cursor}.
     *
     * @param position
     * 		The position (in the {@link Cursor}) of the ringtone.
     * @return A {@link Uri} pointing to the ringtone.
     */
    public android.net.Uri getRingtoneUri(int position) {
        // use cursor directly instead of requerying it, which could easily
        // cause position to shuffle.
        if ((mCursor == null) || (!mCursor.moveToPosition(position))) {
            return null;
        }
        return android.media.RingtoneManager.getUriFromCursor(mCursor);
    }

    private static android.net.Uri getUriFromCursor(android.database.Cursor cursor) {
        return android.content.ContentUris.withAppendedId(android.net.Uri.parse(cursor.getString(android.media.RingtoneManager.URI_COLUMN_INDEX)), cursor.getLong(android.media.RingtoneManager.ID_COLUMN_INDEX));
    }

    /**
     * Gets the position of a {@link Uri} within this {@link RingtoneManager}.
     *
     * @param ringtoneUri
     * 		The {@link Uri} to retreive the position of.
     * @return The position of the {@link Uri}, or -1 if it cannot be found.
     */
    public int getRingtonePosition(android.net.Uri ringtoneUri) {
        if (ringtoneUri == null)
            return -1;

        final android.database.Cursor cursor = getCursor();
        final int cursorCount = cursor.getCount();
        if (!cursor.moveToFirst()) {
            return -1;
        }
        // Only create Uri objects when the actual URI changes
        android.net.Uri currentUri = null;
        java.lang.String previousUriString = null;
        for (int i = 0; i < cursorCount; i++) {
            java.lang.String uriString = cursor.getString(android.media.RingtoneManager.URI_COLUMN_INDEX);
            if ((currentUri == null) || (!uriString.equals(previousUriString))) {
                currentUri = android.net.Uri.parse(uriString);
            }
            if (ringtoneUri.equals(android.content.ContentUris.withAppendedId(currentUri, cursor.getLong(android.media.RingtoneManager.ID_COLUMN_INDEX)))) {
                return i;
            }
            cursor.move(1);
            previousUriString = uriString;
        }
        return -1;
    }

    /**
     * Returns a valid ringtone URI. No guarantees on which it returns. If it
     * cannot find one, returns null. If it can only find one on external storage and the caller
     * doesn't have the {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} permission,
     * returns null.
     *
     * @param context
     * 		The context to use for querying.
     * @return A ringtone URI, or null if one cannot be found.
     */
    public static android.net.Uri getValidRingtoneUri(android.content.Context context) {
        final android.media.RingtoneManager rm = new android.media.RingtoneManager(context);
        android.net.Uri uri = android.media.RingtoneManager.getValidRingtoneUriFromCursorAndClose(context, rm.getInternalRingtones());
        if (uri == null) {
            uri = android.media.RingtoneManager.getValidRingtoneUriFromCursorAndClose(context, rm.getMediaRingtones());
        }
        return uri;
    }

    private static android.net.Uri getValidRingtoneUriFromCursorAndClose(android.content.Context context, android.database.Cursor cursor) {
        if (cursor != null) {
            android.net.Uri uri = null;
            if (cursor.moveToFirst()) {
                uri = android.media.RingtoneManager.getUriFromCursor(cursor);
            }
            cursor.close();
            return uri;
        } else {
            return null;
        }
    }

    private android.database.Cursor getInternalRingtones() {
        return query(android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI, android.media.RingtoneManager.INTERNAL_COLUMNS, android.media.RingtoneManager.constructBooleanTrueWhereClause(mFilterColumns), null, android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    private android.database.Cursor getMediaRingtones() {
        if (android.content.pm.PackageManager.PERMISSION_GRANTED != mContext.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.os.Process.myPid(), android.os.Process.myUid())) {
            android.util.Log.w(android.media.RingtoneManager.TAG, "No READ_EXTERNAL_STORAGE permission, ignoring ringtones on ext storage");
            return null;
        }
        // Get the external media cursor. First check to see if it is mounted.
        final java.lang.String status = android.os.Environment.getExternalStorageState();
        return status.equals(android.os.Environment.MEDIA_MOUNTED) || status.equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY) ? query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, android.media.RingtoneManager.MEDIA_COLUMNS, android.media.RingtoneManager.constructBooleanTrueWhereClause(mFilterColumns), null, android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER) : null;
    }

    private void setFilterColumnsList(int type) {
        java.util.List<java.lang.String> columns = mFilterColumns;
        columns.clear();
        if ((type & android.media.RingtoneManager.TYPE_RINGTONE) != 0) {
            columns.add(android.provider.MediaStore.Audio.AudioColumns.IS_RINGTONE);
        }
        if ((type & android.media.RingtoneManager.TYPE_NOTIFICATION) != 0) {
            columns.add(android.provider.MediaStore.Audio.AudioColumns.IS_NOTIFICATION);
        }
        if ((type & android.media.RingtoneManager.TYPE_ALARM) != 0) {
            columns.add(android.provider.MediaStore.Audio.AudioColumns.IS_ALARM);
        }
    }

    /**
     * Constructs a where clause that consists of at least one column being 1
     * (true). This is used to find all matching sounds for the given sound
     * types (ringtone, notifications, etc.)
     *
     * @param columns
     * 		The columns that must be true.
     * @return The where clause.
     */
    private static java.lang.String constructBooleanTrueWhereClause(java.util.List<java.lang.String> columns) {
        if (columns == null)
            return null;

        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("(");
        for (int i = columns.size() - 1; i >= 0; i--) {
            sb.append(columns.get(i)).append("=1 or ");
        }
        if (columns.size() > 0) {
            // Remove last ' or '
            sb.setLength(sb.length() - 4);
        }
        sb.append(")");
        return sb.toString();
    }

    private android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        if (mActivity != null) {
            return mActivity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
        } else {
            return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        }
    }

    /**
     * Returns a {@link Ringtone} for a given sound URI.
     * <p>
     * If the given URI cannot be opened for any reason, this method will
     * attempt to fallback on another sound. If it cannot find any, it will
     * return null.
     *
     * @param context
     * 		A context used to query.
     * @param ringtoneUri
     * 		The {@link Uri} of a sound or ringtone.
     * @return A {@link Ringtone} for the given URI, or null.
     */
    public static android.media.Ringtone getRingtone(final android.content.Context context, android.net.Uri ringtoneUri) {
        // Don't set the stream type
        return android.media.RingtoneManager.getRingtone(context, ringtoneUri, -1);
    }

    /**
     * Returns a {@link Ringtone} for a given sound URI on the given stream
     * type. Normally, if you change the stream type on the returned
     * {@link Ringtone}, it will re-create the {@link MediaPlayer}. This is just
     * an optimized route to avoid that.
     *
     * @param streamType
     * 		The stream type for the ringtone, or -1 if it should
     * 		not be set (and the default used instead).
     * @see #getRingtone(Context, Uri)
     */
    private static android.media.Ringtone getRingtone(final android.content.Context context, android.net.Uri ringtoneUri, int streamType) {
        try {
            final android.media.Ringtone r = new android.media.Ringtone(context, true);
            if (streamType >= 0) {
                r.setStreamType(streamType);
            }
            r.setUri(ringtoneUri);
            return r;
        } catch (java.lang.Exception ex) {
            android.util.Log.e(android.media.RingtoneManager.TAG, (("Failed to open ringtone " + ringtoneUri) + ": ") + ex);
        }
        return null;
    }

    /**
     * Gets the current default sound's {@link Uri}. This will give the actual
     * sound {@link Uri}, instead of using this, most clients can use
     * {@link System#DEFAULT_RINGTONE_URI}.
     *
     * @param context
     * 		A context used for querying.
     * @param type
     * 		The type whose default sound should be returned. One of
     * 		{@link #TYPE_RINGTONE}, {@link #TYPE_NOTIFICATION}, or
     * 		{@link #TYPE_ALARM}.
     * @return A {@link Uri} pointing to the default sound for the sound type.
     * @see #setActualDefaultRingtoneUri(Context, int, Uri)
     */
    public static android.net.Uri getActualDefaultRingtoneUri(android.content.Context context, int type) {
        java.lang.String setting = android.media.RingtoneManager.getSettingForType(type);
        if (setting == null)
            return null;

        final java.lang.String uriString = android.provider.Settings.System.getStringForUser(context.getContentResolver(), setting, context.getUserId());
        return uriString != null ? android.net.Uri.parse(uriString) : null;
    }

    /**
     * Sets the {@link Uri} of the default sound for a given sound type.
     *
     * @param context
     * 		A context used for querying.
     * @param type
     * 		The type whose default sound should be set. One of
     * 		{@link #TYPE_RINGTONE}, {@link #TYPE_NOTIFICATION}, or
     * 		{@link #TYPE_ALARM}.
     * @param ringtoneUri
     * 		A {@link Uri} pointing to the default sound to set.
     * @see #getActualDefaultRingtoneUri(Context, int)
     */
    public static void setActualDefaultRingtoneUri(android.content.Context context, int type, android.net.Uri ringtoneUri) {
        final android.content.ContentResolver resolver = context.getContentResolver();
        java.lang.String setting = android.media.RingtoneManager.getSettingForType(type);
        if (setting == null)
            return;

        android.provider.Settings.System.putStringForUser(resolver, setting, ringtoneUri != null ? ringtoneUri.toString() : null, context.getUserId());
        // Stream selected ringtone into cache so it's available for playback
        // when CE storage is still locked
        if (ringtoneUri != null) {
            final android.net.Uri cacheUri = android.media.RingtoneManager.getCacheForType(type);
            try (java.io.InputStream in = android.media.RingtoneManager.openRingtone(context, ringtoneUri);java.io.OutputStream out = resolver.openOutputStream(cacheUri)) {
                libcore.io.Streams.copy(in, out);
            } catch (java.io.IOException e) {
                android.util.Log.w(android.media.RingtoneManager.TAG, "Failed to cache ringtone: " + e);
            }
        }
    }

    /**
     * Try opening the given ringtone locally first, but failover to
     * {@link IRingtonePlayer} if we can't access it directly. Typically happens
     * when process doesn't hold
     * {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}.
     */
    private static java.io.InputStream openRingtone(android.content.Context context, android.net.Uri uri) throws java.io.IOException {
        final android.content.ContentResolver resolver = context.getContentResolver();
        try {
            return resolver.openInputStream(uri);
        } catch (java.lang.SecurityException | java.io.IOException e) {
            android.util.Log.w(android.media.RingtoneManager.TAG, "Failed to open directly; attempting failover: " + e);
            final android.media.IRingtonePlayer player = context.getSystemService(android.media.AudioManager.class).getRingtonePlayer();
            try {
                return new android.os.ParcelFileDescriptor.AutoCloseInputStream(player.openRingtone(uri));
            } catch (java.lang.Exception e2) {
                throw new java.io.IOException(e2);
            }
        }
    }

    private static java.lang.String getSettingForType(int type) {
        if ((type & android.media.RingtoneManager.TYPE_RINGTONE) != 0) {
            return android.provider.Settings.System.RINGTONE;
        } else
            if ((type & android.media.RingtoneManager.TYPE_NOTIFICATION) != 0) {
                return android.provider.Settings.System.NOTIFICATION_SOUND;
            } else
                if ((type & android.media.RingtoneManager.TYPE_ALARM) != 0) {
                    return android.provider.Settings.System.ALARM_ALERT;
                } else {
                    return null;
                }


    }

    /**
     * {@hide }
     */
    public static android.net.Uri getCacheForType(int type) {
        if ((type & android.media.RingtoneManager.TYPE_RINGTONE) != 0) {
            return android.provider.Settings.System.RINGTONE_CACHE_URI;
        } else
            if ((type & android.media.RingtoneManager.TYPE_NOTIFICATION) != 0) {
                return android.provider.Settings.System.NOTIFICATION_SOUND_CACHE_URI;
            } else
                if ((type & android.media.RingtoneManager.TYPE_ALARM) != 0) {
                    return android.provider.Settings.System.ALARM_ALERT_CACHE_URI;
                } else {
                    return null;
                }


    }

    /**
     * Returns whether the given {@link Uri} is one of the default ringtones.
     *
     * @param ringtoneUri
     * 		The ringtone {@link Uri} to be checked.
     * @return Whether the {@link Uri} is a default.
     */
    public static boolean isDefault(android.net.Uri ringtoneUri) {
        return android.media.RingtoneManager.getDefaultType(ringtoneUri) != (-1);
    }

    /**
     * Returns the type of a default {@link Uri}.
     *
     * @param defaultRingtoneUri
     * 		The default {@link Uri}. For example,
     * 		{@link System#DEFAULT_RINGTONE_URI},
     * 		{@link System#DEFAULT_NOTIFICATION_URI}, or
     * 		{@link System#DEFAULT_ALARM_ALERT_URI}.
     * @return The type of the defaultRingtoneUri, or -1.
     */
    public static int getDefaultType(android.net.Uri defaultRingtoneUri) {
        if (defaultRingtoneUri == null) {
            return -1;
        } else
            if (defaultRingtoneUri.equals(android.provider.Settings.System.DEFAULT_RINGTONE_URI)) {
                return android.media.RingtoneManager.TYPE_RINGTONE;
            } else
                if (defaultRingtoneUri.equals(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)) {
                    return android.media.RingtoneManager.TYPE_NOTIFICATION;
                } else
                    if (defaultRingtoneUri.equals(android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI)) {
                        return android.media.RingtoneManager.TYPE_ALARM;
                    } else {
                        return -1;
                    }



    }

    /**
     * Returns the {@link Uri} for the default ringtone of a particular type.
     * Rather than returning the actual ringtone's sound {@link Uri}, this will
     * return the symbolic {@link Uri} which will resolved to the actual sound
     * when played.
     *
     * @param type
     * 		The ringtone type whose default should be returned.
     * @return The {@link Uri} of the default ringtone for the given type.
     */
    public static android.net.Uri getDefaultUri(int type) {
        if ((type & android.media.RingtoneManager.TYPE_RINGTONE) != 0) {
            return android.provider.Settings.System.DEFAULT_RINGTONE_URI;
        } else
            if ((type & android.media.RingtoneManager.TYPE_NOTIFICATION) != 0) {
                return android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
            } else
                if ((type & android.media.RingtoneManager.TYPE_ALARM) != 0) {
                    return android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI;
                } else {
                    return null;
                }


    }
}

