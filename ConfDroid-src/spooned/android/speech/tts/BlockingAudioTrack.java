/**
 * Copyright 2011 Google Inc. All Rights Reserved.
 */
package android.speech.tts;


/**
 * Exposes parts of the {@link AudioTrack} API by delegating calls to an
 * underlying {@link AudioTrack}. Additionally, provides methods like
 * {@link #waitAndRelease()} that will block until all audiotrack
 * data has been flushed to the mixer, and is estimated to have completed
 * playback.
 */
class BlockingAudioTrack {
    private static final java.lang.String TAG = "TTS.BlockingAudioTrack";

    private static final boolean DBG = false;

    /**
     * The minimum increment of time to wait for an AudioTrack to finish
     * playing.
     */
    private static final long MIN_SLEEP_TIME_MS = 20;

    /**
     * The maximum increment of time to sleep while waiting for an AudioTrack
     * to finish playing.
     */
    private static final long MAX_SLEEP_TIME_MS = 2500;

    /**
     * The maximum amount of time to wait for an audio track to make progress while
     * it remains in PLAYSTATE_PLAYING. This should never happen in normal usage, but
     * could happen in exceptional circumstances like a media_server crash.
     */
    private static final long MAX_PROGRESS_WAIT_MS = android.speech.tts.BlockingAudioTrack.MAX_SLEEP_TIME_MS;

    /**
     * Minimum size of the buffer of the underlying {@link android.media.AudioTrack}
     * we create.
     */
    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;

    private final android.speech.tts.TextToSpeechService.AudioOutputParams mAudioParams;

    private final int mSampleRateInHz;

    private final int mAudioFormat;

    private final int mChannelCount;

    private final int mBytesPerFrame;

    /**
     * A "short utterance" is one that uses less bytes than the audio
     * track buffer size (mAudioBufferSize). In this case, we need to call
     * {@link AudioTrack#stop()} to send pending buffers to the mixer, and slightly
     * different logic is required to wait for the track to finish.
     *
     * Not volatile, accessed only from the audio playback thread.
     */
    private boolean mIsShortUtterance;

    /**
     * Will be valid after a call to {@link #init()}.
     */
    private int mAudioBufferSize;

    private int mBytesWritten = 0;

    // Need to be seen by stop() which can be called from another thread. mAudioTrack will be
    // set to null only after waitAndRelease().
    private java.lang.Object mAudioTrackLock = new java.lang.Object();

    private android.media.AudioTrack mAudioTrack;

    private volatile boolean mStopped;

    private int mSessionId;

    BlockingAudioTrack(android.speech.tts.TextToSpeechService.AudioOutputParams audioParams, int sampleRate, int audioFormat, int channelCount) {
        mAudioParams = audioParams;
        mSampleRateInHz = sampleRate;
        mAudioFormat = audioFormat;
        mChannelCount = channelCount;
        mBytesPerFrame = android.media.AudioFormat.getBytesPerSample(mAudioFormat) * mChannelCount;
        mIsShortUtterance = false;
        mAudioBufferSize = 0;
        mBytesWritten = 0;
        mAudioTrack = null;
        mStopped = false;
    }

    public boolean init() {
        android.media.AudioTrack track = createStreamingAudioTrack();
        synchronized(mAudioTrackLock) {
            mAudioTrack = track;
        }
        if (track == null) {
            return false;
        } else {
            return true;
        }
    }

    public void stop() {
        synchronized(mAudioTrackLock) {
            if (mAudioTrack != null) {
                mAudioTrack.stop();
            }
            mStopped = true;
        }
    }

    public int write(byte[] data) {
        android.media.AudioTrack track = null;
        synchronized(mAudioTrackLock) {
            track = mAudioTrack;
        }
        if ((track == null) || mStopped) {
            return -1;
        }
        final int bytesWritten = android.speech.tts.BlockingAudioTrack.writeToAudioTrack(track, data);
        mBytesWritten += bytesWritten;
        return bytesWritten;
    }

    public void waitAndRelease() {
        android.media.AudioTrack track = null;
        synchronized(mAudioTrackLock) {
            track = mAudioTrack;
        }
        if (track == null) {
            if (android.speech.tts.BlockingAudioTrack.DBG)
                android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, "Audio track null [duplicate call to waitAndRelease ?]");

            return;
        }
        // For "small" audio tracks, we have to stop() them to make them mixable,
        // else the audio subsystem will wait indefinitely for us to fill the buffer
        // before rendering the track mixable.
        // 
        // If mStopped is true, the track would already have been stopped, so not
        // much point not doing that again.
        if ((mBytesWritten < mAudioBufferSize) && (!mStopped)) {
            if (android.speech.tts.BlockingAudioTrack.DBG) {
                android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, (("Stopping audio track to flush audio, state was : " + track.getPlayState()) + ",stopped= ") + mStopped);
            }
            mIsShortUtterance = true;
            track.stop();
        }
        // Block until the audio track is done only if we haven't stopped yet.
        if (!mStopped) {
            if (android.speech.tts.BlockingAudioTrack.DBG)
                android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, "Waiting for audio track to complete : " + mAudioTrack.hashCode());

            blockUntilDone(mAudioTrack);
        }
        // The last call to AudioTrack.write( ) will return only after
        // all data from the audioTrack has been sent to the mixer, so
        // it's safe to release at this point.
        if (android.speech.tts.BlockingAudioTrack.DBG)
            android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, ("Releasing audio track [" + track.hashCode()) + "]");

        synchronized(mAudioTrackLock) {
            mAudioTrack = null;
        }
        track.release();
    }

    static int getChannelConfig(int channelCount) {
        if (channelCount == 1) {
            return android.media.AudioFormat.CHANNEL_OUT_MONO;
        } else
            if (channelCount == 2) {
                return android.media.AudioFormat.CHANNEL_OUT_STEREO;
            }

        return 0;
    }

    long getAudioLengthMs(int numBytes) {
        final int unconsumedFrames = numBytes / mBytesPerFrame;
        final long estimatedTimeMs = (unconsumedFrames * 1000) / mSampleRateInHz;
        return estimatedTimeMs;
    }

    private static int writeToAudioTrack(android.media.AudioTrack audioTrack, byte[] bytes) {
        if (audioTrack.getPlayState() != android.media.AudioTrack.PLAYSTATE_PLAYING) {
            if (android.speech.tts.BlockingAudioTrack.DBG)
                android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, "AudioTrack not playing, restarting : " + audioTrack.hashCode());

            audioTrack.play();
        }
        int count = 0;
        while (count < bytes.length) {
            // Note that we don't take bufferCopy.mOffset into account because
            // it is guaranteed to be 0.
            int written = audioTrack.write(bytes, count, bytes.length);
            if (written <= 0) {
                break;
            }
            count += written;
        } 
        return count;
    }

    private android.media.AudioTrack createStreamingAudioTrack() {
        final int channelConfig = android.speech.tts.BlockingAudioTrack.getChannelConfig(mChannelCount);
        int minBufferSizeInBytes = android.media.AudioTrack.getMinBufferSize(mSampleRateInHz, channelConfig, mAudioFormat);
        int bufferSizeInBytes = java.lang.Math.max(android.speech.tts.BlockingAudioTrack.MIN_AUDIO_BUFFER_SIZE, minBufferSizeInBytes);
        android.media.AudioFormat audioFormat = new android.media.AudioFormat.Builder().setChannelMask(channelConfig).setEncoding(mAudioFormat).setSampleRate(mSampleRateInHz).build();
        android.media.AudioTrack audioTrack = new android.media.AudioTrack(mAudioParams.mAudioAttributes, audioFormat, bufferSizeInBytes, android.media.AudioTrack.MODE_STREAM, mAudioParams.mSessionId);
        if (audioTrack.getState() != android.media.AudioTrack.STATE_INITIALIZED) {
            android.util.Log.w(android.speech.tts.BlockingAudioTrack.TAG, "Unable to create audio track.");
            audioTrack.release();
            return null;
        }
        mAudioBufferSize = bufferSizeInBytes;
        android.speech.tts.BlockingAudioTrack.setupVolume(audioTrack, mAudioParams.mVolume, mAudioParams.mPan);
        return audioTrack;
    }

    private void blockUntilDone(android.media.AudioTrack audioTrack) {
        if (mBytesWritten <= 0) {
            return;
        }
        if (mIsShortUtterance) {
            // In this case we would have called AudioTrack#stop() to flush
            // buffers to the mixer. This makes the playback head position
            // unobservable and notification markers do not work reliably. We
            // have no option but to wait until we think the track would finish
            // playing and release it after.
            // 
            // This isn't as bad as it looks because (a) We won't end up waiting
            // for much longer than we should because even at 4khz mono, a short
            // utterance weighs in at about 2 seconds, and (b) such short utterances
            // are expected to be relatively infrequent and in a stream of utterances
            // this shows up as a slightly longer pause.
            blockUntilEstimatedCompletion();
        } else {
            blockUntilCompletion(audioTrack);
        }
    }

    private void blockUntilEstimatedCompletion() {
        final int lengthInFrames = mBytesWritten / mBytesPerFrame;
        final long estimatedTimeMs = (lengthInFrames * 1000) / mSampleRateInHz;
        if (android.speech.tts.BlockingAudioTrack.DBG)
            android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, ("About to sleep for: " + estimatedTimeMs) + "ms for a short utterance");

        try {
            java.lang.Thread.sleep(estimatedTimeMs);
        } catch (java.lang.InterruptedException ie) {
            // Do nothing.
        }
    }

    private void blockUntilCompletion(android.media.AudioTrack audioTrack) {
        final int lengthInFrames = mBytesWritten / mBytesPerFrame;
        int previousPosition = -1;
        int currentPosition = 0;
        long blockedTimeMs = 0;
        while ((((currentPosition = audioTrack.getPlaybackHeadPosition()) < lengthInFrames) && (audioTrack.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING)) && (!mStopped)) {
            final long estimatedTimeMs = ((lengthInFrames - currentPosition) * 1000) / audioTrack.getSampleRate();
            final long sleepTimeMs = android.speech.tts.BlockingAudioTrack.clip(estimatedTimeMs, android.speech.tts.BlockingAudioTrack.MIN_SLEEP_TIME_MS, android.speech.tts.BlockingAudioTrack.MAX_SLEEP_TIME_MS);
            // Check if the audio track has made progress since the last loop
            // iteration. We should then add in the amount of time that was
            // spent sleeping in the last iteration.
            if (currentPosition == previousPosition) {
                // This works only because the sleep time that would have been calculated
                // would be the same in the previous iteration too.
                blockedTimeMs += sleepTimeMs;
                // If we've taken too long to make progress, bail.
                if (blockedTimeMs > android.speech.tts.BlockingAudioTrack.MAX_PROGRESS_WAIT_MS) {
                    android.util.Log.w(android.speech.tts.BlockingAudioTrack.TAG, (("Waited unsuccessfully for " + android.speech.tts.BlockingAudioTrack.MAX_PROGRESS_WAIT_MS) + "ms ") + "for AudioTrack to make progress, Aborting");
                    break;
                }
            } else {
                blockedTimeMs = 0;
            }
            previousPosition = currentPosition;
            if (android.speech.tts.BlockingAudioTrack.DBG) {
                android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, ((((("About to sleep for : " + sleepTimeMs) + " ms,") + " Playback position : ") + currentPosition) + ", Length in frames : ") + lengthInFrames);
            }
            try {
                java.lang.Thread.sleep(sleepTimeMs);
            } catch (java.lang.InterruptedException ie) {
                break;
            }
        } 
    }

    private static void setupVolume(android.media.AudioTrack audioTrack, float volume, float pan) {
        final float vol = android.speech.tts.BlockingAudioTrack.clip(volume, 0.0F, 1.0F);
        final float panning = android.speech.tts.BlockingAudioTrack.clip(pan, -1.0F, 1.0F);
        float volLeft = vol;
        float volRight = vol;
        if (panning > 0.0F) {
            volLeft *= 1.0F - panning;
        } else
            if (panning < 0.0F) {
                volRight *= 1.0F + panning;
            }

        if (android.speech.tts.BlockingAudioTrack.DBG)
            android.util.Log.d(android.speech.tts.BlockingAudioTrack.TAG, (("volLeft=" + volLeft) + ",volRight=") + volRight);

        if (audioTrack.setStereoVolume(volLeft, volRight) != android.media.AudioTrack.SUCCESS) {
            android.util.Log.e(android.speech.tts.BlockingAudioTrack.TAG, "Failed to set volume");
        }
    }

    private static final long clip(long value, long min, long max) {
        return value < min ? min : value < max ? value : max;
    }

    private static final float clip(float value, float min, float max) {
        return value < min ? min : value < max ? value : max;
    }
}

