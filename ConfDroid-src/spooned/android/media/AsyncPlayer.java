/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Plays a series of audio URIs, but does all the hard work on another thread
 * so that any slowness with preparing or loading doesn't block the calling thread.
 */
public class AsyncPlayer {
    private static final int PLAY = 1;

    private static final int STOP = 2;

    private static final boolean mDebug = false;

    private static final class Command {
        int code;

        android.content.Context context;

        android.net.Uri uri;

        boolean looping;

        android.media.AudioAttributes attributes;

        long requestTime;

        public java.lang.String toString() {
            return ((((((("{ code=" + code) + " looping=") + looping) + " attr=") + attributes) + " uri=") + uri) + " }";
        }
    }

    private final java.util.LinkedList<android.media.AsyncPlayer.Command> mCmdQueue = new java.util.LinkedList();

    private void startSound(android.media.AsyncPlayer.Command cmd) {
        // Preparing can be slow, so if there is something else
        // is playing, let it continue until we're done, so there
        // is less of a glitch.
        try {
            if (android.media.AsyncPlayer.mDebug)
                android.util.Log.d(mTag, "Starting playback");

            android.media.MediaPlayer player = new android.media.MediaPlayer();
            player.setAudioAttributes(cmd.attributes);
            player.setDataSource(cmd.context, cmd.uri);
            player.setLooping(cmd.looping);
            player.prepare();
            player.start();
            if (mPlayer != null) {
                mPlayer.release();
            }
            mPlayer = player;
            long delay = android.os.SystemClock.uptimeMillis() - cmd.requestTime;
            if (delay > 1000) {
                android.util.Log.w(mTag, ("Notification sound delayed by " + delay) + "msecs");
            }
        } catch (java.lang.Exception e) {
            android.util.Log.w(mTag, "error loading sound for " + cmd.uri, e);
        }
    }

    private final class Thread extends java.lang.Thread {
        Thread() {
            super("AsyncPlayer-" + mTag);
        }

        public void run() {
            while (true) {
                android.media.AsyncPlayer.Command cmd = null;
                synchronized(mCmdQueue) {
                    if (android.media.AsyncPlayer.mDebug)
                        android.util.Log.d(mTag, "RemoveFirst");

                    cmd = mCmdQueue.removeFirst();
                }
                switch (cmd.code) {
                    case android.media.AsyncPlayer.PLAY :
                        if (android.media.AsyncPlayer.mDebug)
                            android.util.Log.d(mTag, "PLAY");

                        startSound(cmd);
                        break;
                    case android.media.AsyncPlayer.STOP :
                        if (android.media.AsyncPlayer.mDebug)
                            android.util.Log.d(mTag, "STOP");

                        if (mPlayer != null) {
                            long delay = android.os.SystemClock.uptimeMillis() - cmd.requestTime;
                            if (delay > 1000) {
                                android.util.Log.w(mTag, ("Notification stop delayed by " + delay) + "msecs");
                            }
                            mPlayer.stop();
                            mPlayer.release();
                            mPlayer = null;
                        } else {
                            android.util.Log.w(mTag, "STOP command without a player");
                        }
                        break;
                }
                synchronized(mCmdQueue) {
                    if (mCmdQueue.size() == 0) {
                        // nothing left to do, quit
                        // doing this check after we're done prevents the case where they
                        // added it during the operation from spawning two threads and
                        // trying to do them in parallel.
                        mThread = null;
                        releaseWakeLock();
                        return;
                    }
                }
            } 
        }
    }

    private java.lang.String mTag;

    private android.media.AsyncPlayer.Thread mThread;

    private android.media.MediaPlayer mPlayer;

    private android.os.PowerManager.WakeLock mWakeLock;

    // The current state according to the caller.  Reality lags behind
    // because of the asynchronous nature of this class.
    private int mState = android.media.AsyncPlayer.STOP;

    /**
     * Construct an AsyncPlayer object.
     *
     * @param tag
     * 		a string to use for debugging
     */
    public AsyncPlayer(java.lang.String tag) {
        if (tag != null) {
            mTag = tag;
        } else {
            mTag = "AsyncPlayer";
        }
    }

    /**
     * Start playing the sound.  It will actually start playing at some
     * point in the future.  There are no guarantees about latency here.
     * Calling this before another audio file is done playing will stop
     * that one and start the new one.
     *
     * @param context
     * 		Your application's context.
     * @param uri
     * 		The URI to play.  (see {@link MediaPlayer#setDataSource(Context, Uri)})
     * @param looping
     * 		Whether the audio should loop forever.
     * 		(see {@link MediaPlayer#setLooping(boolean)})
     * @param stream
     * 		the AudioStream to use.
     * 		(see {@link MediaPlayer#setAudioStreamType(int)})
     * @deprecated use {@link #play(Context, Uri, boolean, AudioAttributes)} instead
     */
    public void play(android.content.Context context, android.net.Uri uri, boolean looping, int stream) {
        if ((context == null) || (uri == null)) {
            return;
        }
        try {
            play(context, uri, looping, new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(stream).build());
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(mTag, "Call to deprecated AsyncPlayer.play() method caused:", e);
        }
    }

    /**
     * Start playing the sound.  It will actually start playing at some
     * point in the future.  There are no guarantees about latency here.
     * Calling this before another audio file is done playing will stop
     * that one and start the new one.
     *
     * @param context
     * 		the non-null application's context.
     * @param uri
     * 		the non-null URI to play.  (see {@link MediaPlayer#setDataSource(Context, Uri)})
     * @param looping
     * 		whether the audio should loop forever.
     * 		(see {@link MediaPlayer#setLooping(boolean)})
     * @param attributes
     * 		the non-null {@link AudioAttributes} to use.
     * 		(see {@link MediaPlayer#setAudioAttributes(AudioAttributes)})
     * @throws IllegalArgumentException
     * 		
     */
    public void play(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.net.Uri uri, boolean looping, @android.annotation.NonNull
    android.media.AudioAttributes attributes) throws java.lang.IllegalArgumentException {
        if (((context == null) || (uri == null)) || (attributes == null)) {
            throw new java.lang.IllegalArgumentException("Illegal null AsyncPlayer.play() argument");
        }
        android.media.AsyncPlayer.Command cmd = new android.media.AsyncPlayer.Command();
        cmd.requestTime = android.os.SystemClock.uptimeMillis();
        cmd.code = android.media.AsyncPlayer.PLAY;
        cmd.context = context;
        cmd.uri = uri;
        cmd.looping = looping;
        cmd.attributes = attributes;
        synchronized(mCmdQueue) {
            enqueueLocked(cmd);
            mState = android.media.AsyncPlayer.PLAY;
        }
    }

    /**
     * Stop a previously played sound.  It can't be played again or unpaused
     * at this point.  Calling this multiple times has no ill effects.
     */
    public void stop() {
        synchronized(mCmdQueue) {
            // This check allows stop to be called multiple times without starting
            // a thread that ends up doing nothing.
            if (mState != android.media.AsyncPlayer.STOP) {
                android.media.AsyncPlayer.Command cmd = new android.media.AsyncPlayer.Command();
                cmd.requestTime = android.os.SystemClock.uptimeMillis();
                cmd.code = android.media.AsyncPlayer.STOP;
                enqueueLocked(cmd);
                mState = android.media.AsyncPlayer.STOP;
            }
        }
    }

    private void enqueueLocked(android.media.AsyncPlayer.Command cmd) {
        mCmdQueue.add(cmd);
        if (mThread == null) {
            acquireWakeLock();
            mThread = new android.media.AsyncPlayer.Thread();
            mThread.start();
        }
    }

    /**
     * We want to hold a wake lock while we do the prepare and play.  The stop probably is
     * optional, but it won't hurt to have it too.  The problem is that if you start a sound
     * while you're holding a wake lock (e.g. an alarm starting a notification), you want the
     * sound to play, but if the CPU turns off before mThread gets to work, it won't.  The
     * simplest way to deal with this is to make it so there is a wake lock held while the
     * thread is starting or running.  You're going to need the WAKE_LOCK permission if you're
     * going to call this.
     *
     * This must be called before the first time play is called.
     *
     * @unknown 
     */
    public void setUsesWakeLock(android.content.Context context) {
        if ((mWakeLock != null) || (mThread != null)) {
            // if either of these has happened, we've already played something.
            // and our releases will be out of sync.
            throw new java.lang.RuntimeException((("assertion failed mWakeLock=" + mWakeLock) + " mThread=") + mThread);
        }
        android.os.PowerManager pm = ((android.os.PowerManager) (context.getSystemService(android.content.Context.POWER_SERVICE)));
        mWakeLock = pm.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, mTag);
    }

    private void acquireWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }
    }

    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
        }
    }
}

