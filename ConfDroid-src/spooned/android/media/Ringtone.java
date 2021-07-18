/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * Ringtone provides a quick method for playing a ringtone, notification, or
 * other similar types of sounds.
 * <p>
 * For ways of retrieving {@link Ringtone} objects or to show a ringtone
 * picker, see {@link RingtoneManager}.
 *
 * @see RingtoneManager
 */
public class Ringtone {
    private static final java.lang.String TAG = "Ringtone";

    private static final boolean LOGD = true;

    private static final java.lang.String[] MEDIA_COLUMNS = new java.lang.String[]{ android.provider.MediaStore.Audio.Media._ID, android.provider.MediaStore.Audio.Media.DATA, android.provider.MediaStore.Audio.Media.TITLE };

    /**
     * Selection that limits query results to just audio files
     */
    private static final java.lang.String MEDIA_SELECTION = ((android.provider.MediaStore.MediaColumns.MIME_TYPE + " LIKE 'audio/%' OR ") + android.provider.MediaStore.MediaColumns.MIME_TYPE) + " IN ('application/ogg', 'application/x-flac')";

    // keep references on active Ringtones until stopped or completion listener called.
    private static final java.util.ArrayList<android.media.Ringtone> sActiveRingtones = new java.util.ArrayList<android.media.Ringtone>();

    private final android.content.Context mContext;

    private final android.media.AudioManager mAudioManager;

    /**
     * Flag indicating if we're allowed to fall back to remote playback using
     * {@link #mRemotePlayer}. Typically this is false when we're the remote
     * player and there is nobody else to delegate to.
     */
    private final boolean mAllowRemote;

    private final android.media.IRingtonePlayer mRemotePlayer;

    private final android.os.Binder mRemoteToken;

    private android.media.MediaPlayer mLocalPlayer;

    private final android.media.Ringtone.MyOnCompletionListener mCompletionListener = new android.media.Ringtone.MyOnCompletionListener();

    private android.net.Uri mUri;

    private java.lang.String mTitle;

    private android.media.AudioAttributes mAudioAttributes = new android.media.AudioAttributes.Builder().setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE).setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION).build();

    // playback properties, use synchronized with mPlaybackSettingsLock
    private boolean mIsLooping = false;

    private float mVolume = 1.0F;

    private final java.lang.Object mPlaybackSettingsLock = new java.lang.Object();

    /**
     * {@hide }
     */
    public Ringtone(android.content.Context context, boolean allowRemote) {
        mContext = context;
        mAudioManager = ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE)));
        mAllowRemote = allowRemote;
        mRemotePlayer = (allowRemote) ? mAudioManager.getRingtonePlayer() : null;
        mRemoteToken = (allowRemote) ? new android.os.Binder() : null;
    }

    /**
     * Sets the stream type where this ringtone will be played.
     *
     * @param streamType
     * 		The stream, see {@link AudioManager}.
     * @deprecated use {@link #setAudioAttributes(AudioAttributes)}
     */
    @java.lang.Deprecated
    public void setStreamType(int streamType) {
        setAudioAttributes(new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build());
    }

    /**
     * Gets the stream type where this ringtone will be played.
     *
     * @return The stream type, see {@link AudioManager}.
     * @deprecated use of stream types is deprecated, see
    {@link #setAudioAttributes(AudioAttributes)}
     */
    @java.lang.Deprecated
    public int getStreamType() {
        return android.media.AudioAttributes.toLegacyStreamType(mAudioAttributes);
    }

    /**
     * Sets the {@link AudioAttributes} for this ringtone.
     *
     * @param attributes
     * 		the non-null attributes characterizing this ringtone.
     */
    public void setAudioAttributes(android.media.AudioAttributes attributes) throws java.lang.IllegalArgumentException {
        if (attributes == null) {
            throw new java.lang.IllegalArgumentException("Invalid null AudioAttributes for Ringtone");
        }
        mAudioAttributes = attributes;
        // The audio attributes have to be set before the media player is prepared.
        // Re-initialize it.
        setUri(mUri);
    }

    /**
     * Returns the {@link AudioAttributes} used by this object.
     *
     * @return the {@link AudioAttributes} that were set with
    {@link #setAudioAttributes(AudioAttributes)} or the default attributes if none were set.
     */
    public android.media.AudioAttributes getAudioAttributes() {
        return mAudioAttributes;
    }

    /**
     *
     *
     * @unknown Sets the player to be looping or non-looping.
     * @param looping
     * 		whether to loop or not
     */
    public void setLooping(boolean looping) {
        synchronized(mPlaybackSettingsLock) {
            mIsLooping = looping;
            applyPlaybackProperties_sync();
        }
    }

    /**
     *
     *
     * @unknown Sets the volume on this player.
     * @param volume
     * 		a raw scalar in range 0.0 to 1.0, where 0.0 mutes this player, and 1.0
     * 		corresponds to no attenuation being applied.
     */
    public void setVolume(float volume) {
        synchronized(mPlaybackSettingsLock) {
            if (volume < 0.0F) {
                volume = 0.0F;
            }
            if (volume > 1.0F) {
                volume = 1.0F;
            }
            mVolume = volume;
            applyPlaybackProperties_sync();
        }
    }

    /**
     * Must be called synchronized on mPlaybackSettingsLock
     */
    private void applyPlaybackProperties_sync() {
        if (mLocalPlayer != null) {
            mLocalPlayer.setVolume(mVolume);
            mLocalPlayer.setLooping(mIsLooping);
        } else
            if (mAllowRemote && (mRemotePlayer != null)) {
                try {
                    mRemotePlayer.setPlaybackProperties(mRemoteToken, mVolume, mIsLooping);
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(android.media.Ringtone.TAG, "Problem setting playback properties: ", e);
                }
            } else {
                android.util.Log.w(android.media.Ringtone.TAG, "Neither local nor remote player available when applying playback properties");
            }

    }

    /**
     * Returns a human-presentable title for ringtone. Looks in media
     * content provider. If not in either, uses the filename
     *
     * @param context
     * 		A context used for querying.
     */
    public java.lang.String getTitle(android.content.Context context) {
        if (mTitle != null)
            return mTitle;

        return mTitle = /* followSettingsUri */
        android.media.Ringtone.getTitle(context, mUri, true, mAllowRemote);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getTitle(android.content.Context context, android.net.Uri uri, boolean followSettingsUri, boolean allowRemote) {
        android.content.ContentResolver res = context.getContentResolver();
        java.lang.String title = null;
        if (uri != null) {
            java.lang.String authority = uri.getAuthority();
            if (android.provider.Settings.AUTHORITY.equals(authority)) {
                if (followSettingsUri) {
                    android.net.Uri actualUri = android.media.RingtoneManager.getActualDefaultRingtoneUri(context, android.media.RingtoneManager.getDefaultType(uri));
                    java.lang.String actualTitle = /* followSettingsUri */
                    android.media.Ringtone.getTitle(context, actualUri, false, allowRemote);
                    title = context.getString(com.android.internal.R.string.ringtone_default_with_actual, actualTitle);
                }
            } else {
                android.database.Cursor cursor = null;
                try {
                    if (android.provider.MediaStore.AUTHORITY.equals(authority)) {
                        final java.lang.String mediaSelection = (allowRemote) ? null : android.media.Ringtone.MEDIA_SELECTION;
                        cursor = res.query(uri, android.media.Ringtone.MEDIA_COLUMNS, mediaSelection, null, null);
                        if ((cursor != null) && (cursor.getCount() == 1)) {
                            cursor.moveToFirst();
                            return cursor.getString(2);
                        }
                        // missing cursor is handled below
                    }
                } catch (java.lang.SecurityException e) {
                    android.media.IRingtonePlayer mRemotePlayer = null;
                    if (allowRemote) {
                        android.media.AudioManager audioManager = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
                        mRemotePlayer = audioManager.getRingtonePlayer();
                    }
                    if (mRemotePlayer != null) {
                        try {
                            title = mRemotePlayer.getTitle(uri);
                        } catch (android.os.RemoteException re) {
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    cursor = null;
                }
                if (title == null) {
                    title = uri.getLastPathSegment();
                }
            }
        }
        if (title == null) {
            title = context.getString(com.android.internal.R.string.ringtone_unknown);
            if (title == null) {
                title = "";
            }
        }
        return title;
    }

    /**
     * Set {@link Uri} to be used for ringtone playback. Attempts to open
     * locally, otherwise will delegate playback to remote
     * {@link IRingtonePlayer}.
     *
     * @unknown 
     */
    public void setUri(android.net.Uri uri) {
        destroyLocalPlayer();
        mUri = uri;
        if (mUri == null) {
            return;
        }
        // TODO: detect READ_EXTERNAL and specific content provider case, instead of relying on throwing
        // try opening uri locally before delegating to remote player
        mLocalPlayer = new android.media.MediaPlayer();
        try {
            mLocalPlayer.setDataSource(mContext, mUri);
            mLocalPlayer.setAudioAttributes(mAudioAttributes);
            synchronized(mPlaybackSettingsLock) {
                applyPlaybackProperties_sync();
            }
            mLocalPlayer.prepare();
        } catch (java.lang.SecurityException | java.io.IOException e) {
            destroyLocalPlayer();
            if (!mAllowRemote) {
                android.util.Log.w(android.media.Ringtone.TAG, "Remote playback not allowed: " + e);
            }
        }
        if (android.media.Ringtone.LOGD) {
            if (mLocalPlayer != null) {
                android.util.Log.d(android.media.Ringtone.TAG, "Successfully created local player");
            } else {
                android.util.Log.d(android.media.Ringtone.TAG, "Problem opening; delegating to remote player");
            }
        }
    }

    /**
     * {@hide }
     */
    public android.net.Uri getUri() {
        return mUri;
    }

    /**
     * Plays the ringtone.
     */
    public void play() {
        if (mLocalPlayer != null) {
            // do not play ringtones if stream volume is 0
            // (typically because ringer mode is silent).
            if (mAudioManager.getStreamVolume(android.media.AudioAttributes.toLegacyStreamType(mAudioAttributes)) != 0) {
                startLocalPlayer();
            }
        } else
            if (mAllowRemote && (mRemotePlayer != null)) {
                final android.net.Uri canonicalUri = mUri.getCanonicalUri();
                final boolean looping;
                final float volume;
                synchronized(mPlaybackSettingsLock) {
                    looping = mIsLooping;
                    volume = mVolume;
                }
                try {
                    mRemotePlayer.play(mRemoteToken, canonicalUri, mAudioAttributes, volume, looping);
                } catch (android.os.RemoteException e) {
                    if (!playFallbackRingtone()) {
                        android.util.Log.w(android.media.Ringtone.TAG, "Problem playing ringtone: " + e);
                    }
                }
            } else {
                if (!playFallbackRingtone()) {
                    android.util.Log.w(android.media.Ringtone.TAG, "Neither local nor remote playback available");
                }
            }

    }

    /**
     * Stops a playing ringtone.
     */
    public void stop() {
        if (mLocalPlayer != null) {
            destroyLocalPlayer();
        } else
            if (mAllowRemote && (mRemotePlayer != null)) {
                try {
                    mRemotePlayer.stop(mRemoteToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(android.media.Ringtone.TAG, "Problem stopping ringtone: " + e);
                }
            }

    }

    private void destroyLocalPlayer() {
        if (mLocalPlayer != null) {
            mLocalPlayer.reset();
            mLocalPlayer.release();
            mLocalPlayer = null;
            synchronized(android.media.Ringtone.sActiveRingtones) {
                android.media.Ringtone.sActiveRingtones.remove(this);
            }
        }
    }

    private void startLocalPlayer() {
        if (mLocalPlayer == null) {
            return;
        }
        synchronized(android.media.Ringtone.sActiveRingtones) {
            android.media.Ringtone.sActiveRingtones.add(this);
        }
        mLocalPlayer.setOnCompletionListener(mCompletionListener);
        mLocalPlayer.start();
    }

    /**
     * Whether this ringtone is currently playing.
     *
     * @return True if playing, false otherwise.
     */
    public boolean isPlaying() {
        if (mLocalPlayer != null) {
            return mLocalPlayer.isPlaying();
        } else
            if (mAllowRemote && (mRemotePlayer != null)) {
                try {
                    return mRemotePlayer.isPlaying(mRemoteToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(android.media.Ringtone.TAG, "Problem checking ringtone: " + e);
                    return false;
                }
            } else {
                android.util.Log.w(android.media.Ringtone.TAG, "Neither local nor remote playback available");
                return false;
            }

    }

    private boolean playFallbackRingtone() {
        if (mAudioManager.getStreamVolume(android.media.AudioAttributes.toLegacyStreamType(mAudioAttributes)) != 0) {
            int ringtoneType = android.media.RingtoneManager.getDefaultType(mUri);
            if ((ringtoneType == (-1)) || (android.media.RingtoneManager.getActualDefaultRingtoneUri(mContext, ringtoneType) != null)) {
                // Default ringtone, try fallback ringtone.
                try {
                    android.content.res.AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(com.android.internal.R.raw.fallbackring);
                    if (afd != null) {
                        mLocalPlayer = new android.media.MediaPlayer();
                        if (afd.getDeclaredLength() < 0) {
                            mLocalPlayer.setDataSource(afd.getFileDescriptor());
                        } else {
                            mLocalPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                        }
                        mLocalPlayer.setAudioAttributes(mAudioAttributes);
                        synchronized(mPlaybackSettingsLock) {
                            applyPlaybackProperties_sync();
                        }
                        mLocalPlayer.prepare();
                        startLocalPlayer();
                        afd.close();
                        return true;
                    } else {
                        android.util.Log.e(android.media.Ringtone.TAG, "Could not load fallback ringtone");
                    }
                } catch (java.io.IOException ioe) {
                    destroyLocalPlayer();
                    android.util.Log.e(android.media.Ringtone.TAG, "Failed to open fallback ringtone");
                } catch (android.content.res.Resources.NotFoundException nfe) {
                    android.util.Log.e(android.media.Ringtone.TAG, "Fallback ringtone does not exist");
                }
            } else {
                android.util.Log.w(android.media.Ringtone.TAG, "not playing fallback for " + mUri);
            }
        }
        return false;
    }

    void setTitle(java.lang.String title) {
        mTitle = title;
    }

    @java.lang.Override
    protected void finalize() {
        if (mLocalPlayer != null) {
            mLocalPlayer.release();
        }
    }

    class MyOnCompletionListener implements android.media.MediaPlayer.OnCompletionListener {
        public void onCompletion(android.media.MediaPlayer mp) {
            synchronized(android.media.Ringtone.sActiveRingtones) {
                android.media.Ringtone.sActiveRingtones.remove(android.media.Ringtone.this);
            }
            mp.setOnCompletionListener(null);// Help the Java GC: break the refcount cycle.

        }
    }
}

