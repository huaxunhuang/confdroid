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
 * The SoundPool class manages and plays audio resources for applications.
 *
 * <p>A SoundPool is a collection of samples that can be loaded into memory
 * from a resource inside the APK or from a file in the file system. The
 * SoundPool library uses the MediaPlayer service to decode the audio
 * into a raw 16-bit PCM mono or stereo stream. This allows applications
 * to ship with compressed streams without having to suffer the CPU load
 * and latency of decompressing during playback.</p>
 *
 * <p>In addition to low-latency playback, SoundPool can also manage the number
 * of audio streams being rendered at once. When the SoundPool object is
 * constructed, the maxStreams parameter sets the maximum number of streams
 * that can be played at a time from this single SoundPool. SoundPool tracks
 * the number of active streams. If the maximum number of streams is exceeded,
 * SoundPool will automatically stop a previously playing stream based first
 * on priority and then by age within that priority. Limiting the maximum
 * number of streams helps to cap CPU loading and reducing the likelihood that
 * audio mixing will impact visuals or UI performance.</p>
 *
 * <p>Sounds can be looped by setting a non-zero loop value. A value of -1
 * causes the sound to loop forever. In this case, the application must
 * explicitly call the stop() function to stop the sound. Any other non-zero
 * value will cause the sound to repeat the specified number of times, e.g.
 * a value of 3 causes the sound to play a total of 4 times.</p>
 *
 * <p>The playback rate can also be changed. A playback rate of 1.0 causes
 * the sound to play at its original frequency (resampled, if necessary,
 * to the hardware output frequency). A playback rate of 2.0 causes the
 * sound to play at twice its original frequency, and a playback rate of
 * 0.5 causes it to play at half its original frequency. The playback
 * rate range is 0.5 to 2.0.</p>
 *
 * <p>Priority runs low to high, i.e. higher numbers are higher priority.
 * Priority is used when a call to play() would cause the number of active
 * streams to exceed the value established by the maxStreams parameter when
 * the SoundPool was created. In this case, the stream allocator will stop
 * the lowest priority stream. If there are multiple streams with the same
 * low priority, it will choose the oldest stream to stop. In the case
 * where the priority of the new stream is lower than all the active
 * streams, the new sound will not play and the play() function will return
 * a streamID of zero.</p>
 *
 * <p>Let's examine a typical use case: A game consists of several levels of
 * play. For each level, there is a set of unique sounds that are used only
 * by that level. In this case, the game logic should create a new SoundPool
 * object when the first level is loaded. The level data itself might contain
 * the list of sounds to be used by this level. The loading logic iterates
 * through the list of sounds calling the appropriate SoundPool.load()
 * function. This should typically be done early in the process to allow time
 * for decompressing the audio to raw PCM format before they are needed for
 * playback.</p>
 *
 * <p>Once the sounds are loaded and play has started, the application can
 * trigger sounds by calling SoundPool.play(). Playing streams can be
 * paused or resumed, and the application can also alter the pitch by
 * adjusting the playback rate in real-time for doppler or synthesis
 * effects.</p>
 *
 * <p>Note that since streams can be stopped due to resource constraints, the
 * streamID is a reference to a particular instance of a stream. If the stream
 * is stopped to allow a higher priority stream to play, the stream is no
 * longer be valid. However, the application is allowed to call methods on
 * the streamID without error. This may help simplify program logic since
 * the application need not concern itself with the stream lifecycle.</p>
 *
 * <p>In our example, when the player has completed the level, the game
 * logic should call SoundPool.release() to release all the native resources
 * in use and then set the SoundPool reference to null. If the player starts
 * another level, a new SoundPool is created, sounds are loaded, and play
 * resumes.</p>
 */
public class SoundPool {
    static {
        java.lang.System.loadLibrary("soundpool");
    }

    // SoundPool messages
    // 
    // must match SoundPool.h
    private static final int SAMPLE_LOADED = 1;

    private static final java.lang.String TAG = "SoundPool";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.media.SoundPool.TAG, android.util.Log.DEBUG);

    private long mNativeContext;// accessed by native methods


    private android.media.SoundPool.EventHandler mEventHandler;

    private android.media.SoundPool.OnLoadCompleteListener mOnLoadCompleteListener;

    private boolean mHasAppOpsPlayAudio;

    private final java.lang.Object mLock;

    private final android.media.AudioAttributes mAttributes;

    private final com.android.internal.app.IAppOpsService mAppOps;

    private final com.android.internal.app.IAppOpsCallback mAppOpsCallback;

    private static android.media.IAudioService sService;

    /**
     * Constructor. Constructs a SoundPool object with the following
     * characteristics:
     *
     * @param maxStreams
     * 		the maximum number of simultaneous streams for this
     * 		SoundPool object
     * @param streamType
     * 		the audio stream type as described in AudioManager
     * 		For example, game applications will normally use
     * 		{@link AudioManager#STREAM_MUSIC}.
     * @param srcQuality
     * 		the sample-rate converter quality. Currently has no
     * 		effect. Use 0 for the default.
     * @return a SoundPool object, or null if creation failed
     * @deprecated use {@link SoundPool.Builder} instead to create and configure a
    SoundPool instance
     */
    public SoundPool(int maxStreams, int streamType, int srcQuality) {
        this(maxStreams, new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build());
    }

    private SoundPool(int maxStreams, android.media.AudioAttributes attributes) {
        // do native setup
        if (native_setup(new java.lang.ref.WeakReference<android.media.SoundPool>(this), maxStreams, attributes) != 0) {
            throw new java.lang.RuntimeException("Native setup failed");
        }
        mLock = new java.lang.Object();
        mAttributes = attributes;
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.APP_OPS_SERVICE);
        mAppOps = IAppOpsService.Stub.asInterface(b);
        // initialize mHasAppOpsPlayAudio
        updateAppOpsPlayAudio();
        // register a callback to monitor whether the OP_PLAY_AUDIO is still allowed
        mAppOpsCallback = new com.android.internal.app.IAppOpsCallback.Stub() {
            public void opChanged(int op, int uid, java.lang.String packageName) {
                synchronized(mLock) {
                    if (op == android.app.AppOpsManager.OP_PLAY_AUDIO) {
                        updateAppOpsPlayAudio();
                    }
                }
            }
        };
        try {
            mAppOps.startWatchingMode(android.app.AppOpsManager.OP_PLAY_AUDIO, android.app.ActivityThread.currentPackageName(), mAppOpsCallback);
        } catch (android.os.RemoteException e) {
            mHasAppOpsPlayAudio = false;
        }
    }

    /**
     * Release the SoundPool resources.
     *
     * Release all memory and native resources used by the SoundPool
     * object. The SoundPool can no longer be used and the reference
     * should be set to null.
     */
    public final void release() {
        try {
            mAppOps.stopWatchingMode(mAppOpsCallback);
        } catch (android.os.RemoteException e) {
            // nothing to do here, the SoundPool is being released anyway
        }
        native_release();
    }

    private final native void native_release();

    protected void finalize() {
        release();
    }

    /**
     * Load the sound from the specified path.
     *
     * @param path
     * 		the path to the audio file
     * @param priority
     * 		the priority of the sound. Currently has no effect. Use
     * 		a value of 1 for future compatibility.
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(java.lang.String path, int priority) {
        int id = 0;
        try {
            java.io.File f = new java.io.File(path);
            android.os.ParcelFileDescriptor fd = android.os.ParcelFileDescriptor.open(f, android.os.ParcelFileDescriptor.MODE_READ_ONLY);
            if (fd != null) {
                id = _load(fd.getFileDescriptor(), 0, f.length(), priority);
                fd.close();
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(android.media.SoundPool.TAG, "error loading " + path);
        }
        return id;
    }

    /**
     * Load the sound from the specified APK resource.
     *
     * Note that the extension is dropped. For example, if you want to load
     * a sound from the raw resource file "explosion.mp3", you would specify
     * "R.raw.explosion" as the resource ID. Note that this means you cannot
     * have both an "explosion.wav" and an "explosion.mp3" in the res/raw
     * directory.
     *
     * @param context
     * 		the application context
     * @param resId
     * 		the resource ID
     * @param priority
     * 		the priority of the sound. Currently has no effect. Use
     * 		a value of 1 for future compatibility.
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(android.content.Context context, int resId, int priority) {
        android.content.res.AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
        int id = 0;
        if (afd != null) {
            id = _load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(), priority);
            try {
                afd.close();
            } catch (java.io.IOException ex) {
                // Log.d(TAG, "close failed:", ex);
            }
        }
        return id;
    }

    /**
     * Load the sound from an asset file descriptor.
     *
     * @param afd
     * 		an asset file descriptor
     * @param priority
     * 		the priority of the sound. Currently has no effect. Use
     * 		a value of 1 for future compatibility.
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(android.content.res.AssetFileDescriptor afd, int priority) {
        if (afd != null) {
            long len = afd.getLength();
            if (len < 0) {
                throw new android.util.AndroidRuntimeException("no length for fd");
            }
            return _load(afd.getFileDescriptor(), afd.getStartOffset(), len, priority);
        } else {
            return 0;
        }
    }

    /**
     * Load the sound from a FileDescriptor.
     *
     * This version is useful if you store multiple sounds in a single
     * binary. The offset specifies the offset from the start of the file
     * and the length specifies the length of the sound within the file.
     *
     * @param fd
     * 		a FileDescriptor object
     * @param offset
     * 		offset to the start of the sound
     * @param length
     * 		length of the sound
     * @param priority
     * 		the priority of the sound. Currently has no effect. Use
     * 		a value of 1 for future compatibility.
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(java.io.FileDescriptor fd, long offset, long length, int priority) {
        return _load(fd, offset, length, priority);
    }

    /**
     * Unload a sound from a sound ID.
     *
     * Unloads the sound specified by the soundID. This is the value
     * returned by the load() function. Returns true if the sound is
     * successfully unloaded, false if the sound was already unloaded.
     *
     * @param soundID
     * 		a soundID returned by the load() function
     * @return true if just unloaded, false if previously unloaded
     */
    public final native boolean unload(int soundID);

    /**
     * Play a sound from a sound ID.
     *
     * Play the sound specified by the soundID. This is the value
     * returned by the load() function. Returns a non-zero streamID
     * if successful, zero if it fails. The streamID can be used to
     * further control playback. Note that calling play() may cause
     * another sound to stop playing if the maximum number of active
     * streams is exceeded. A loop value of -1 means loop forever,
     * a value of 0 means don't loop, other values indicate the
     * number of repeats, e.g. a value of 1 plays the audio twice.
     * The playback rate allows the application to vary the playback
     * rate (pitch) of the sound. A value of 1.0 means play back at
     * the original frequency. A value of 2.0 means play back twice
     * as fast, and a value of 0.5 means playback at half speed.
     *
     * @param soundID
     * 		a soundID returned by the load() function
     * @param leftVolume
     * 		left volume value (range = 0.0 to 1.0)
     * @param rightVolume
     * 		right volume value (range = 0.0 to 1.0)
     * @param priority
     * 		stream priority (0 = lowest priority)
     * @param loop
     * 		loop mode (0 = no loop, -1 = loop forever)
     * @param rate
     * 		playback rate (1.0 = normal playback, range 0.5 to 2.0)
     * @return non-zero streamID if successful, zero if failed
     */
    public final int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) {
        if (isRestricted()) {
            leftVolume = rightVolume = 0;
        }
        return _play(soundID, leftVolume, rightVolume, priority, loop, rate);
    }

    /**
     * Pause a playback stream.
     *
     * Pause the stream specified by the streamID. This is the
     * value returned by the play() function. If the stream is
     * playing, it will be paused. If the stream is not playing
     * (e.g. is stopped or was previously paused), calling this
     * function will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     */
    public final native void pause(int streamID);

    /**
     * Resume a playback stream.
     *
     * Resume the stream specified by the streamID. This
     * is the value returned by the play() function. If the stream
     * is paused, this will resume playback. If the stream was not
     * previously paused, calling this function will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     */
    public final native void resume(int streamID);

    /**
     * Pause all active streams.
     *
     * Pause all streams that are currently playing. This function
     * iterates through all the active streams and pauses any that
     * are playing. It also sets a flag so that any streams that
     * are playing can be resumed by calling autoResume().
     */
    public final native void autoPause();

    /**
     * Resume all previously active streams.
     *
     * Automatically resumes all streams that were paused in previous
     * calls to autoPause().
     */
    public final native void autoResume();

    /**
     * Stop a playback stream.
     *
     * Stop the stream specified by the streamID. This
     * is the value returned by the play() function. If the stream
     * is playing, it will be stopped. It also releases any native
     * resources associated with this stream. If the stream is not
     * playing, it will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     */
    public final native void stop(int streamID);

    /**
     * Set stream volume.
     *
     * Sets the volume on the stream specified by the streamID.
     * This is the value returned by the play() function. The
     * value must be in the range of 0.0 to 1.0. If the stream does
     * not exist, it will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     * @param leftVolume
     * 		left volume value (range = 0.0 to 1.0)
     * @param rightVolume
     * 		right volume value (range = 0.0 to 1.0)
     */
    public final void setVolume(int streamID, float leftVolume, float rightVolume) {
        if (isRestricted()) {
            return;
        }
        _setVolume(streamID, leftVolume, rightVolume);
    }

    /**
     * Similar, except set volume of all channels to same value.
     *
     * @unknown 
     */
    public void setVolume(int streamID, float volume) {
        setVolume(streamID, volume, volume);
    }

    /**
     * Change stream priority.
     *
     * Change the priority of the stream specified by the streamID.
     * This is the value returned by the play() function. Affects the
     * order in which streams are re-used to play new sounds. If the
     * stream does not exist, it will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     */
    public final native void setPriority(int streamID, int priority);

    /**
     * Set loop mode.
     *
     * Change the loop mode. A loop value of -1 means loop forever,
     * a value of 0 means don't loop, other values indicate the
     * number of repeats, e.g. a value of 1 plays the audio twice.
     * If the stream does not exist, it will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     * @param loop
     * 		loop mode (0 = no loop, -1 = loop forever)
     */
    public final native void setLoop(int streamID, int loop);

    /**
     * Change playback rate.
     *
     * The playback rate allows the application to vary the playback
     * rate (pitch) of the sound. A value of 1.0 means playback at
     * the original frequency. A value of 2.0 means playback twice
     * as fast, and a value of 0.5 means playback at half speed.
     * If the stream does not exist, it will have no effect.
     *
     * @param streamID
     * 		a streamID returned by the play() function
     * @param rate
     * 		playback rate (1.0 = normal playback, range 0.5 to 2.0)
     */
    public final native void setRate(int streamID, float rate);

    public interface OnLoadCompleteListener {
        /**
         * Called when a sound has completed loading.
         *
         * @param soundPool
         * 		SoundPool object from the load() method
         * @param sampleId
         * 		the sample ID of the sound loaded.
         * @param status
         * 		the status of the load operation (0 = success)
         */
        public void onLoadComplete(android.media.SoundPool soundPool, int sampleId, int status);
    }

    /**
     * Sets the callback hook for the OnLoadCompleteListener.
     */
    public void setOnLoadCompleteListener(android.media.SoundPool.OnLoadCompleteListener listener) {
        synchronized(mLock) {
            if (listener != null) {
                // setup message handler
                android.os.Looper looper;
                if ((looper = android.os.Looper.myLooper()) != null) {
                    mEventHandler = new android.media.SoundPool.EventHandler(looper);
                } else
                    if ((looper = android.os.Looper.getMainLooper()) != null) {
                        mEventHandler = new android.media.SoundPool.EventHandler(looper);
                    } else {
                        mEventHandler = null;
                    }

            } else {
                mEventHandler = null;
            }
            mOnLoadCompleteListener = listener;
        }
    }

    private static android.media.IAudioService getService() {
        if (android.media.SoundPool.sService != null) {
            return android.media.SoundPool.sService;
        }
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.AUDIO_SERVICE);
        android.media.SoundPool.sService = IAudioService.Stub.asInterface(b);
        return android.media.SoundPool.sService;
    }

    private boolean isRestricted() {
        // check app ops
        if (mHasAppOpsPlayAudio) {
            return false;
        }
        // check bypass flag
        if ((mAttributes.getAllFlags() & android.media.AudioAttributes.FLAG_BYPASS_INTERRUPTION_POLICY) != 0) {
            return false;
        }
        // check force audibility flag and camera restriction
        if ((mAttributes.getAllFlags() & android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED) != 0) {
            // FIXME: should also check usage when set properly by camera app
            // && (mAttributes.getUsage() == AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            boolean cameraSoundForced = false;
            try {
                cameraSoundForced = android.media.SoundPool.getService().isCameraSoundForced();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.SoundPool.TAG, "Cannot access AudioService in isRestricted()");
            } catch (java.lang.NullPointerException e) {
                android.util.Log.e(android.media.SoundPool.TAG, "Null AudioService in isRestricted()");
            }
            if (cameraSoundForced) {
                return false;
            }
        }
        return true;
    }

    private void updateAppOpsPlayAudio() {
        try {
            final int mode = mAppOps.checkAudioOperation(android.app.AppOpsManager.OP_PLAY_AUDIO, mAttributes.getUsage(), android.os.Process.myUid(), android.app.ActivityThread.currentPackageName());
            mHasAppOpsPlayAudio = mode == android.app.AppOpsManager.MODE_ALLOWED;
        } catch (android.os.RemoteException e) {
            mHasAppOpsPlayAudio = false;
        }
    }

    private final native int _load(java.io.FileDescriptor fd, long offset, long length, int priority);

    private final native int native_setup(java.lang.Object weakRef, int maxStreams, /* AudioAttributes */
    java.lang.Object attributes);

    private final native int _play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate);

    private final native void _setVolume(int streamID, float leftVolume, float rightVolume);

    // post event from native code to message handler
    @java.lang.SuppressWarnings("unchecked")
    private static void postEventFromNative(java.lang.Object ref, int msg, int arg1, int arg2, java.lang.Object obj) {
        android.media.SoundPool soundPool = ((java.lang.ref.WeakReference<android.media.SoundPool>) (ref)).get();
        if (soundPool == null)
            return;

        if (soundPool.mEventHandler != null) {
            android.os.Message m = soundPool.mEventHandler.obtainMessage(msg, arg1, arg2, obj);
            soundPool.mEventHandler.sendMessage(m);
        }
    }

    private final class EventHandler extends android.os.Handler {
        public EventHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.media.SoundPool.SAMPLE_LOADED :
                    if (android.media.SoundPool.DEBUG)
                        android.util.Log.d(android.media.SoundPool.TAG, ("Sample " + msg.arg1) + " loaded");

                    synchronized(mLock) {
                        if (mOnLoadCompleteListener != null) {
                            mOnLoadCompleteListener.onLoadComplete(android.media.SoundPool.this, msg.arg1, msg.arg2);
                        }
                    }
                    break;
                default :
                    android.util.Log.e(android.media.SoundPool.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    /**
     * Builder class for {@link SoundPool} objects.
     */
    public static class Builder {
        private int mMaxStreams = 1;

        private android.media.AudioAttributes mAudioAttributes;

        /**
         * Constructs a new Builder with the defaults format values.
         * If not provided, the maximum number of streams is 1 (see {@link #setMaxStreams(int)} to
         * change it), and the audio attributes have a usage value of
         * {@link AudioAttributes#USAGE_MEDIA} (see {@link #setAudioAttributes(AudioAttributes)} to
         * change them).
         */
        public Builder() {
        }

        /**
         * Sets the maximum of number of simultaneous streams that can be played simultaneously.
         *
         * @param maxStreams
         * 		a value equal to 1 or greater.
         * @return the same Builder instance
         * @throws IllegalArgumentException
         * 		
         */
        public android.media.SoundPool.Builder setMaxStreams(int maxStreams) throws java.lang.IllegalArgumentException {
            if (maxStreams <= 0) {
                throw new java.lang.IllegalArgumentException("Strictly positive value required for the maximum number of streams");
            }
            mMaxStreams = maxStreams;
            return this;
        }

        /**
         * Sets the {@link AudioAttributes}. For examples, game applications will use attributes
         * built with usage information set to {@link AudioAttributes#USAGE_GAME}.
         *
         * @param attributes
         * 		a non-null
         * @return 
         */
        public android.media.SoundPool.Builder setAudioAttributes(android.media.AudioAttributes attributes) throws java.lang.IllegalArgumentException {
            if (attributes == null) {
                throw new java.lang.IllegalArgumentException("Invalid null AudioAttributes");
            }
            mAudioAttributes = attributes;
            return this;
        }

        public android.media.SoundPool build() {
            if (mAudioAttributes == null) {
                mAudioAttributes = new android.media.AudioAttributes.Builder().setUsage(android.media.AudioAttributes.USAGE_MEDIA).build();
            }
            return new android.media.SoundPool(mMaxStreams, mAudioAttributes);
        }
    }
}

