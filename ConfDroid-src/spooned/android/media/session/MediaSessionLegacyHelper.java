/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.media.session;


/**
 * Helper for connecting existing APIs up to the new session APIs. This can be
 * used by RCC, AudioFocus, etc. to create a single session that translates to
 * all those components.
 *
 * @unknown 
 */
public class MediaSessionLegacyHelper {
    private static final java.lang.String TAG = "MediaSessionHelper";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.media.session.MediaSessionLegacyHelper.TAG, android.util.Log.DEBUG);

    private static final java.lang.Object sLock = new java.lang.Object();

    private static android.media.session.MediaSessionLegacyHelper sInstance;

    private android.content.Context mContext;

    private android.media.session.MediaSessionManager mSessionManager;

    private android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    // The legacy APIs use PendingIntents to register/unregister media button
    // receivers and these are associated with RCC.
    private android.util.ArrayMap<android.app.PendingIntent, android.media.session.MediaSessionLegacyHelper.SessionHolder> mSessions = new android.util.ArrayMap<android.app.PendingIntent, android.media.session.MediaSessionLegacyHelper.SessionHolder>();

    private MediaSessionLegacyHelper(android.content.Context context) {
        mContext = context;
        mSessionManager = ((android.media.session.MediaSessionManager) (context.getSystemService(android.content.Context.MEDIA_SESSION_SERVICE)));
    }

    public static android.media.session.MediaSessionLegacyHelper getHelper(android.content.Context context) {
        synchronized(android.media.session.MediaSessionLegacyHelper.sLock) {
            if (android.media.session.MediaSessionLegacyHelper.sInstance == null) {
                android.media.session.MediaSessionLegacyHelper.sInstance = new android.media.session.MediaSessionLegacyHelper(context.getApplicationContext());
            }
        }
        return android.media.session.MediaSessionLegacyHelper.sInstance;
    }

    public static android.os.Bundle getOldMetadata(android.media.MediaMetadata metadata, int artworkWidth, int artworkHeight) {
        boolean includeArtwork = (artworkWidth != (-1)) && (artworkHeight != (-1));
        android.os.Bundle oldMetadata = new android.os.Bundle();
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_ALBUM)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM), metadata.getString(android.media.MediaMetadata.METADATA_KEY_ALBUM));
        }
        if (includeArtwork && metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_ART)) {
            android.graphics.Bitmap art = metadata.getBitmap(android.media.MediaMetadata.METADATA_KEY_ART);
            oldMetadata.putParcelable(java.lang.String.valueOf(android.media.MediaMetadataEditor.BITMAP_KEY_ARTWORK), android.media.session.MediaSessionLegacyHelper.scaleBitmapIfTooBig(art, artworkWidth, artworkHeight));
        } else
            if (includeArtwork && metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_ALBUM_ART)) {
                // Fall back to album art if the track art wasn't available
                android.graphics.Bitmap art = metadata.getBitmap(android.media.MediaMetadata.METADATA_KEY_ALBUM_ART);
                oldMetadata.putParcelable(java.lang.String.valueOf(android.media.MediaMetadataEditor.BITMAP_KEY_ARTWORK), android.media.session.MediaSessionLegacyHelper.scaleBitmapIfTooBig(art, artworkWidth, artworkHeight));
            }

        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_ALBUM_ARTIST)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), metadata.getString(android.media.MediaMetadata.METADATA_KEY_ALBUM_ARTIST));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_ARTIST)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST), metadata.getString(android.media.MediaMetadata.METADATA_KEY_ARTIST));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_AUTHOR)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_AUTHOR), metadata.getString(android.media.MediaMetadata.METADATA_KEY_AUTHOR));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_COMPILATION)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_COMPILATION), metadata.getString(android.media.MediaMetadata.METADATA_KEY_COMPILATION));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_COMPOSER)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_COMPOSER), metadata.getString(android.media.MediaMetadata.METADATA_KEY_COMPOSER));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_DATE)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_DATE), metadata.getString(android.media.MediaMetadata.METADATA_KEY_DATE));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_DISC_NUMBER)) {
            oldMetadata.putLong(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER), metadata.getLong(android.media.MediaMetadata.METADATA_KEY_DISC_NUMBER));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_DURATION)) {
            oldMetadata.putLong(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION), metadata.getLong(android.media.MediaMetadata.METADATA_KEY_DURATION));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_GENRE)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_GENRE), metadata.getString(android.media.MediaMetadata.METADATA_KEY_GENRE));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_NUM_TRACKS)) {
            oldMetadata.putLong(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS), metadata.getLong(android.media.MediaMetadata.METADATA_KEY_NUM_TRACKS));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_RATING)) {
            oldMetadata.putParcelable(java.lang.String.valueOf(android.media.MediaMetadataEditor.RATING_KEY_BY_OTHERS), metadata.getRating(android.media.MediaMetadata.METADATA_KEY_RATING));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_USER_RATING)) {
            oldMetadata.putParcelable(java.lang.String.valueOf(android.media.MediaMetadataEditor.RATING_KEY_BY_USER), metadata.getRating(android.media.MediaMetadata.METADATA_KEY_USER_RATING));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_TITLE)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_TITLE), metadata.getString(android.media.MediaMetadata.METADATA_KEY_TITLE));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_TRACK_NUMBER)) {
            oldMetadata.putLong(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER), metadata.getLong(android.media.MediaMetadata.METADATA_KEY_TRACK_NUMBER));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_WRITER)) {
            oldMetadata.putString(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_WRITER), metadata.getString(android.media.MediaMetadata.METADATA_KEY_WRITER));
        }
        if (metadata.containsKey(android.media.MediaMetadata.METADATA_KEY_YEAR)) {
            oldMetadata.putLong(java.lang.String.valueOf(android.media.MediaMetadataRetriever.METADATA_KEY_YEAR), metadata.getLong(android.media.MediaMetadata.METADATA_KEY_YEAR));
        }
        return oldMetadata;
    }

    public android.media.session.MediaSession getSession(android.app.PendingIntent pi) {
        android.media.session.MediaSessionLegacyHelper.SessionHolder holder = mSessions.get(pi);
        return holder == null ? null : holder.mSession;
    }

    public void sendMediaButtonEvent(android.view.KeyEvent keyEvent, boolean needWakeLock) {
        if (keyEvent == null) {
            android.util.Log.w(android.media.session.MediaSessionLegacyHelper.TAG, "Tried to send a null key event. Ignoring.");
            return;
        }
        mSessionManager.dispatchMediaKeyEvent(keyEvent, needWakeLock);
        if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
            android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, "dispatched media key " + keyEvent);
        }
    }

    public void sendVolumeKeyEvent(android.view.KeyEvent keyEvent, boolean musicOnly) {
        if (keyEvent == null) {
            android.util.Log.w(android.media.session.MediaSessionLegacyHelper.TAG, "Tried to send a null key event. Ignoring.");
            return;
        }
        boolean down = keyEvent.getAction() == android.view.KeyEvent.ACTION_DOWN;
        boolean up = keyEvent.getAction() == android.view.KeyEvent.ACTION_UP;
        int direction = 0;
        boolean isMute = false;
        switch (keyEvent.getKeyCode()) {
            case android.view.KeyEvent.KEYCODE_VOLUME_UP :
                direction = android.media.AudioManager.ADJUST_RAISE;
                break;
            case android.view.KeyEvent.KEYCODE_VOLUME_DOWN :
                direction = android.media.AudioManager.ADJUST_LOWER;
                break;
            case android.view.KeyEvent.KEYCODE_VOLUME_MUTE :
                isMute = true;
                break;
        }
        if (down || up) {
            int flags = android.media.AudioManager.FLAG_FROM_KEY;
            if (musicOnly) {
                // This flag is used when the screen is off to only affect
                // active media
                flags |= android.media.AudioManager.FLAG_ACTIVE_MEDIA_ONLY;
            } else {
                // These flags are consistent with the home screen
                if (up) {
                    flags |= android.media.AudioManager.FLAG_PLAY_SOUND | android.media.AudioManager.FLAG_VIBRATE;
                } else {
                    flags |= android.media.AudioManager.FLAG_SHOW_UI | android.media.AudioManager.FLAG_VIBRATE;
                }
            }
            if (direction != 0) {
                // If this is action up we want to send a beep for non-music events
                if (up) {
                    direction = 0;
                }
                mSessionManager.dispatchAdjustVolume(android.media.AudioManager.USE_DEFAULT_STREAM_TYPE, direction, flags);
            } else
                if (isMute) {
                    if (down && (keyEvent.getRepeatCount() == 0)) {
                        mSessionManager.dispatchAdjustVolume(android.media.AudioManager.USE_DEFAULT_STREAM_TYPE, android.media.AudioManager.ADJUST_TOGGLE_MUTE, flags);
                    }
                }

        }
    }

    public void sendAdjustVolumeBy(int suggestedStream, int delta, int flags) {
        mSessionManager.dispatchAdjustVolume(suggestedStream, delta, flags);
        if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
            android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, "dispatched volume adjustment");
        }
    }

    public boolean isGlobalPriorityActive() {
        return mSessionManager.isGlobalPriorityActive();
    }

    public void addRccListener(android.app.PendingIntent pi, android.media.session.MediaSession.Callback listener) {
        if (pi == null) {
            android.util.Log.w(android.media.session.MediaSessionLegacyHelper.TAG, "Pending intent was null, can't add rcc listener.");
            return;
        }
        android.media.session.MediaSessionLegacyHelper.SessionHolder holder = getHolder(pi, true);
        if (holder == null) {
            return;
        }
        if (holder.mRccListener != null) {
            if (holder.mRccListener == listener) {
                if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
                    android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, "addRccListener listener already added.");
                }
                // This is already the registered listener, ignore
                return;
            }
        }
        holder.mRccListener = listener;
        holder.mFlags |= android.media.session.MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS;
        holder.mSession.setFlags(holder.mFlags);
        holder.update();
        if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
            android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, ("Added rcc listener for " + pi) + ".");
        }
    }

    public void removeRccListener(android.app.PendingIntent pi) {
        if (pi == null) {
            return;
        }
        android.media.session.MediaSessionLegacyHelper.SessionHolder holder = getHolder(pi, false);
        if ((holder != null) && (holder.mRccListener != null)) {
            holder.mRccListener = null;
            holder.mFlags &= ~android.media.session.MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS;
            holder.mSession.setFlags(holder.mFlags);
            holder.update();
            if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
                android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, ("Removed rcc listener for " + pi) + ".");
            }
        }
    }

    public void addMediaButtonListener(android.app.PendingIntent pi, android.content.ComponentName mbrComponent, android.content.Context context) {
        if (pi == null) {
            android.util.Log.w(android.media.session.MediaSessionLegacyHelper.TAG, "Pending intent was null, can't addMediaButtonListener.");
            return;
        }
        android.media.session.MediaSessionLegacyHelper.SessionHolder holder = getHolder(pi, true);
        if (holder == null) {
            return;
        }
        if (holder.mMediaButtonListener != null) {
            // Already have this listener registered
            if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
                android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, "addMediaButtonListener already added " + pi);
            }
        }
        holder.mMediaButtonListener = new android.media.session.MediaSessionLegacyHelper.MediaButtonListener(pi, context);
        // TODO determine if handling transport performer commands should also
        // set this flag
        holder.mFlags |= android.media.session.MediaSession.FLAG_HANDLES_MEDIA_BUTTONS;
        holder.mSession.setFlags(holder.mFlags);
        holder.mSession.setMediaButtonReceiver(pi);
        holder.update();
        if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
            android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, "addMediaButtonListener added " + pi);
        }
    }

    public void removeMediaButtonListener(android.app.PendingIntent pi) {
        if (pi == null) {
            return;
        }
        android.media.session.MediaSessionLegacyHelper.SessionHolder holder = getHolder(pi, false);
        if ((holder != null) && (holder.mMediaButtonListener != null)) {
            holder.mFlags &= ~android.media.session.MediaSession.FLAG_HANDLES_MEDIA_BUTTONS;
            holder.mSession.setFlags(holder.mFlags);
            holder.mMediaButtonListener = null;
            holder.update();
            if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
                android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, "removeMediaButtonListener removed " + pi);
            }
        }
    }

    /**
     * Scale a bitmap to fit the smallest dimension by uniformly scaling the
     * incoming bitmap. If the bitmap fits, then do nothing and return the
     * original.
     *
     * @param bitmap
     * 		
     * @param maxWidth
     * 		
     * @param maxHeight
     * 		
     * @return 
     */
    private static android.graphics.Bitmap scaleBitmapIfTooBig(android.graphics.Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap != null) {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            if ((width > maxWidth) || (height > maxHeight)) {
                float scale = java.lang.Math.min(((float) (maxWidth)) / width, ((float) (maxHeight)) / height);
                int newWidth = java.lang.Math.round(scale * width);
                int newHeight = java.lang.Math.round(scale * height);
                android.graphics.Bitmap.Config newConfig = bitmap.getConfig();
                if (newConfig == null) {
                    newConfig = android.graphics.Bitmap.Config.ARGB_8888;
                }
                android.graphics.Bitmap outBitmap = android.graphics.Bitmap.createBitmap(newWidth, newHeight, newConfig);
                android.graphics.Canvas canvas = new android.graphics.Canvas(outBitmap);
                android.graphics.Paint paint = new android.graphics.Paint();
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                canvas.drawBitmap(bitmap, null, new android.graphics.RectF(0, 0, outBitmap.getWidth(), outBitmap.getHeight()), paint);
                bitmap = outBitmap;
            }
        }
        return bitmap;
    }

    private android.media.session.MediaSessionLegacyHelper.SessionHolder getHolder(android.app.PendingIntent pi, boolean createIfMissing) {
        android.media.session.MediaSessionLegacyHelper.SessionHolder holder = mSessions.get(pi);
        if ((holder == null) && createIfMissing) {
            android.media.session.MediaSession session;
            session = new android.media.session.MediaSession(mContext, (android.media.session.MediaSessionLegacyHelper.TAG + "-") + pi.getCreatorPackage());
            session.setActive(true);
            holder = new android.media.session.MediaSessionLegacyHelper.SessionHolder(session, pi);
            mSessions.put(pi, holder);
        }
        return holder;
    }

    private static void sendKeyEvent(android.app.PendingIntent pi, android.content.Context context, android.content.Intent intent) {
        try {
            pi.send(context, 0, intent);
        } catch (android.app.PendingIntent.CanceledException e) {
            android.util.Log.e(android.media.session.MediaSessionLegacyHelper.TAG, "Error sending media key down event:", e);
            // Don't bother sending up if down failed
            return;
        }
    }

    private static final class MediaButtonListener extends android.media.session.MediaSession.Callback {
        private final android.app.PendingIntent mPendingIntent;

        private final android.content.Context mContext;

        public MediaButtonListener(android.app.PendingIntent pi, android.content.Context context) {
            mPendingIntent = pi;
            mContext = context;
        }

        @java.lang.Override
        public boolean onMediaButtonEvent(android.content.Intent mediaButtonIntent) {
            android.media.session.MediaSessionLegacyHelper.sendKeyEvent(mPendingIntent, mContext, mediaButtonIntent);
            return true;
        }

        @java.lang.Override
        public void onPlay() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_PLAY);
        }

        @java.lang.Override
        public void onPause() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_PAUSE);
        }

        @java.lang.Override
        public void onSkipToNext() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_NEXT);
        }

        @java.lang.Override
        public void onSkipToPrevious() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }

        @java.lang.Override
        public void onFastForward() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
        }

        @java.lang.Override
        public void onRewind() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_REWIND);
        }

        @java.lang.Override
        public void onStop() {
            sendKeyEvent(android.view.KeyEvent.KEYCODE_MEDIA_STOP);
        }

        private void sendKeyEvent(int keyCode) {
            android.view.KeyEvent ke = new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, keyCode);
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MEDIA_BUTTON);
            intent.addFlags(android.content.Intent.FLAG_RECEIVER_FOREGROUND);
            intent.putExtra(android.content.Intent.EXTRA_KEY_EVENT, ke);
            android.media.session.MediaSessionLegacyHelper.sendKeyEvent(mPendingIntent, mContext, intent);
            ke = new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, keyCode);
            intent.putExtra(android.content.Intent.EXTRA_KEY_EVENT, ke);
            android.media.session.MediaSessionLegacyHelper.sendKeyEvent(mPendingIntent, mContext, intent);
            if (android.media.session.MediaSessionLegacyHelper.DEBUG) {
                android.util.Log.d(android.media.session.MediaSessionLegacyHelper.TAG, (("Sent " + keyCode) + " to pending intent ") + mPendingIntent);
            }
        }
    }

    private class SessionHolder {
        public final android.media.session.MediaSession mSession;

        public final android.app.PendingIntent mPi;

        public android.media.session.MediaSessionLegacyHelper.MediaButtonListener mMediaButtonListener;

        public android.media.session.MediaSession.Callback mRccListener;

        public int mFlags;

        public android.media.session.MediaSessionLegacyHelper.SessionHolder.SessionCallback mCb;

        public SessionHolder(android.media.session.MediaSession session, android.app.PendingIntent pi) {
            mSession = session;
            mPi = pi;
        }

        public void update() {
            if ((mMediaButtonListener == null) && (mRccListener == null)) {
                mSession.setCallback(null);
                mSession.release();
                mCb = null;
                mSessions.remove(mPi);
            } else
                if (mCb == null) {
                    mCb = new android.media.session.MediaSessionLegacyHelper.SessionHolder.SessionCallback();
                    android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mSession.setCallback(mCb, handler);
                }

        }

        private class SessionCallback extends android.media.session.MediaSession.Callback {
            @java.lang.Override
            public boolean onMediaButtonEvent(android.content.Intent mediaButtonIntent) {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onMediaButtonEvent(mediaButtonIntent);
                }
                return true;
            }

            @java.lang.Override
            public void onPlay() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onPlay();
                }
            }

            @java.lang.Override
            public void onPause() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onPause();
                }
            }

            @java.lang.Override
            public void onSkipToNext() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onSkipToNext();
                }
            }

            @java.lang.Override
            public void onSkipToPrevious() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onSkipToPrevious();
                }
            }

            @java.lang.Override
            public void onFastForward() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onFastForward();
                }
            }

            @java.lang.Override
            public void onRewind() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onRewind();
                }
            }

            @java.lang.Override
            public void onStop() {
                if (mMediaButtonListener != null) {
                    mMediaButtonListener.onStop();
                }
            }

            @java.lang.Override
            public void onSeekTo(long pos) {
                if (mRccListener != null) {
                    mRccListener.onSeekTo(pos);
                }
            }

            @java.lang.Override
            public void onSetRating(android.media.Rating rating) {
                if (mRccListener != null) {
                    mRccListener.onSetRating(rating);
                }
            }
        }
    }
}

