/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.speech.tts;


/**
 * Speech synthesis request that writes the audio to a WAV file.
 */
class FileSynthesisCallback extends android.speech.tts.AbstractSynthesisCallback {
    private static final java.lang.String TAG = "FileSynthesisRequest";

    private static final boolean DBG = false;

    private static final int MAX_AUDIO_BUFFER_SIZE = 8192;

    private static final int WAV_HEADER_LENGTH = 44;

    private static final short WAV_FORMAT_PCM = 0x1;

    private final java.lang.Object mStateLock = new java.lang.Object();

    private int mSampleRateInHz;

    private int mAudioFormat;

    private int mChannelCount;

    private java.nio.channels.FileChannel mFileChannel;

    private final android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher mDispatcher;

    private boolean mStarted = false;

    private boolean mDone = false;

    /**
     * Status code of synthesis
     */
    protected int mStatusCode;

    FileSynthesisCallback(@android.annotation.NonNull
    java.nio.channels.FileChannel fileChannel, @android.annotation.NonNull
    android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher dispatcher, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        mFileChannel = fileChannel;
        mDispatcher = dispatcher;
        mStatusCode = android.speech.tts.TextToSpeech.SUCCESS;
    }

    @java.lang.Override
    void stop() {
        synchronized(mStateLock) {
            if (mDone) {
                return;
            }
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                return;
            }
            mStatusCode = android.speech.tts.TextToSpeech.STOPPED;
            cleanUp();
            mDispatcher.dispatchOnStop();
        }
    }

    /**
     * Must be called while holding the monitor on {@link #mStateLock}.
     */
    private void cleanUp() {
        closeFile();
    }

    /**
     * Must be called while holding the monitor on {@link #mStateLock}.
     */
    private void closeFile() {
        // File will be closed by the SpeechItem in the speech service.
        mFileChannel = null;
    }

    @java.lang.Override
    public int getMaxBufferSize() {
        return android.speech.tts.FileSynthesisCallback.MAX_AUDIO_BUFFER_SIZE;
    }

    @java.lang.Override
    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        if (android.speech.tts.FileSynthesisCallback.DBG) {
            android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, ((((("FileSynthesisRequest.start(" + sampleRateInHz) + ",") + audioFormat) + ",") + channelCount) + ")");
        }
        if (((audioFormat != android.media.AudioFormat.ENCODING_PCM_8BIT) && (audioFormat != android.media.AudioFormat.ENCODING_PCM_16BIT)) && (audioFormat != android.media.AudioFormat.ENCODING_PCM_FLOAT)) {
            android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, ((("Audio format encoding " + audioFormat) + " not supported. Please use one ") + "of AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT or ") + "AudioFormat.ENCODING_PCM_FLOAT");
        }
        mDispatcher.dispatchOnBeginSynthesis(sampleRateInHz, audioFormat, channelCount);
        java.nio.channels.FileChannel fileChannel = null;
        synchronized(mStateLock) {
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                if (android.speech.tts.FileSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "Request has been aborted.");

                return errorCodeOnStop();
            }
            if (mStatusCode != android.speech.tts.TextToSpeech.SUCCESS) {
                if (android.speech.tts.FileSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "Error was raised");

                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mStarted) {
                android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "Start called twice");
                return android.speech.tts.TextToSpeech.ERROR;
            }
            mStarted = true;
            mSampleRateInHz = sampleRateInHz;
            mAudioFormat = audioFormat;
            mChannelCount = channelCount;
            mDispatcher.dispatchOnStart();
            fileChannel = mFileChannel;
        }
        try {
            fileChannel.write(java.nio.ByteBuffer.allocate(android.speech.tts.FileSynthesisCallback.WAV_HEADER_LENGTH));
            return android.speech.tts.TextToSpeech.SUCCESS;
        } catch (java.io.IOException ex) {
            android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "Failed to write wav header to output file descriptor", ex);
            synchronized(mStateLock) {
                cleanUp();
                mStatusCode = android.speech.tts.TextToSpeech.ERROR_OUTPUT;
            }
            return android.speech.tts.TextToSpeech.ERROR;
        }
    }

    @java.lang.Override
    public int audioAvailable(byte[] buffer, int offset, int length) {
        if (android.speech.tts.FileSynthesisCallback.DBG) {
            android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, ((((("FileSynthesisRequest.audioAvailable(" + buffer) + ",") + offset) + ",") + length) + ")");
        }
        java.nio.channels.FileChannel fileChannel = null;
        synchronized(mStateLock) {
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                if (android.speech.tts.FileSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "Request has been aborted.");

                return errorCodeOnStop();
            }
            if (mStatusCode != android.speech.tts.TextToSpeech.SUCCESS) {
                if (android.speech.tts.FileSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "Error was raised");

                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mFileChannel == null) {
                android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "File not open");
                mStatusCode = android.speech.tts.TextToSpeech.ERROR_OUTPUT;
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (!mStarted) {
                android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "Start method was not called");
                return android.speech.tts.TextToSpeech.ERROR;
            }
            fileChannel = mFileChannel;
        }
        final byte[] bufferCopy = new byte[length];
        java.lang.System.arraycopy(buffer, offset, bufferCopy, 0, length);
        mDispatcher.dispatchOnAudioAvailable(bufferCopy);
        try {
            fileChannel.write(java.nio.ByteBuffer.wrap(buffer, offset, length));
            return android.speech.tts.TextToSpeech.SUCCESS;
        } catch (java.io.IOException ex) {
            android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "Failed to write to output file descriptor", ex);
            synchronized(mStateLock) {
                cleanUp();
                mStatusCode = android.speech.tts.TextToSpeech.ERROR_OUTPUT;
            }
            return android.speech.tts.TextToSpeech.ERROR;
        }
    }

    @java.lang.Override
    public int done() {
        if (android.speech.tts.FileSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "FileSynthesisRequest.done()");

        java.nio.channels.FileChannel fileChannel = null;
        int sampleRateInHz = 0;
        int audioFormat = 0;
        int channelCount = 0;
        synchronized(mStateLock) {
            if (mDone) {
                android.util.Log.w(android.speech.tts.FileSynthesisCallback.TAG, "Duplicate call to done()");
                // This is not an error that would prevent synthesis. Hence no
                // setStatusCode is set.
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                if (android.speech.tts.FileSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "Request has been aborted.");

                return errorCodeOnStop();
            }
            if ((mStatusCode != android.speech.tts.TextToSpeech.SUCCESS) && (mStatusCode != android.speech.tts.TextToSpeech.STOPPED)) {
                mDispatcher.dispatchOnError(mStatusCode);
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mFileChannel == null) {
                android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "File not open");
                return android.speech.tts.TextToSpeech.ERROR;
            }
            mDone = true;
            fileChannel = mFileChannel;
            sampleRateInHz = mSampleRateInHz;
            audioFormat = mAudioFormat;
            channelCount = mChannelCount;
        }
        try {
            // Write WAV header at start of file
            fileChannel.position(0);
            int dataLength = ((int) (fileChannel.size() - android.speech.tts.FileSynthesisCallback.WAV_HEADER_LENGTH));
            fileChannel.write(makeWavHeader(sampleRateInHz, audioFormat, channelCount, dataLength));
            synchronized(mStateLock) {
                closeFile();
                mDispatcher.dispatchOnSuccess();
                return android.speech.tts.TextToSpeech.SUCCESS;
            }
        } catch (java.io.IOException ex) {
            android.util.Log.e(android.speech.tts.FileSynthesisCallback.TAG, "Failed to write to output file descriptor", ex);
            synchronized(mStateLock) {
                cleanUp();
            }
            return android.speech.tts.TextToSpeech.ERROR;
        }
    }

    @java.lang.Override
    public void error() {
        error(android.speech.tts.TextToSpeech.ERROR_SYNTHESIS);
    }

    @java.lang.Override
    public void error(int errorCode) {
        if (android.speech.tts.FileSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.FileSynthesisCallback.TAG, "FileSynthesisRequest.error()");

        synchronized(mStateLock) {
            if (mDone) {
                return;
            }
            cleanUp();
            mStatusCode = errorCode;
        }
    }

    @java.lang.Override
    public boolean hasStarted() {
        synchronized(mStateLock) {
            return mStarted;
        }
    }

    @java.lang.Override
    public boolean hasFinished() {
        synchronized(mStateLock) {
            return mDone;
        }
    }

    private java.nio.ByteBuffer makeWavHeader(int sampleRateInHz, int audioFormat, int channelCount, int dataLength) {
        int sampleSizeInBytes = android.media.AudioFormat.getBytesPerSample(audioFormat);
        int byteRate = (sampleRateInHz * sampleSizeInBytes) * channelCount;
        short blockAlign = ((short) (sampleSizeInBytes * channelCount));
        short bitsPerSample = ((short) (sampleSizeInBytes * 8));
        byte[] headerBuf = new byte[android.speech.tts.FileSynthesisCallback.WAV_HEADER_LENGTH];
        java.nio.ByteBuffer header = java.nio.ByteBuffer.wrap(headerBuf);
        header.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        header.put(new byte[]{ 'R', 'I', 'F', 'F' });
        header.putInt((dataLength + android.speech.tts.FileSynthesisCallback.WAV_HEADER_LENGTH) - 8);// RIFF chunk size

        header.put(new byte[]{ 'W', 'A', 'V', 'E' });
        header.put(new byte[]{ 'f', 'm', 't', ' ' });
        header.putInt(16);// size of fmt chunk

        header.putShort(android.speech.tts.FileSynthesisCallback.WAV_FORMAT_PCM);
        header.putShort(((short) (channelCount)));
        header.putInt(sampleRateInHz);
        header.putInt(byteRate);
        header.putShort(blockAlign);
        header.putShort(bitsPerSample);
        header.put(new byte[]{ 'd', 'a', 't', 'a' });
        header.putInt(dataLength);
        header.flip();
        return header;
    }
}

