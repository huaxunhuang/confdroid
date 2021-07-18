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


class AudioPlaybackHandler {
    private static final java.lang.String TAG = "TTS.AudioPlaybackHandler";

    private static final boolean DBG = false;

    private final java.util.concurrent.LinkedBlockingQueue<android.speech.tts.PlaybackQueueItem> mQueue = new java.util.concurrent.LinkedBlockingQueue<android.speech.tts.PlaybackQueueItem>();

    private final java.lang.Thread mHandlerThread;

    private volatile android.speech.tts.PlaybackQueueItem mCurrentWorkItem = null;

    AudioPlaybackHandler() {
        mHandlerThread = new java.lang.Thread(new android.speech.tts.AudioPlaybackHandler.MessageLoop(), "TTS.AudioPlaybackThread");
    }

    public void start() {
        mHandlerThread.start();
    }

    private void stop(android.speech.tts.PlaybackQueueItem item) {
        if (item == null) {
            return;
        }
        item.stop(android.speech.tts.TextToSpeech.STOPPED);
    }

    public void enqueue(android.speech.tts.PlaybackQueueItem item) {
        try {
            mQueue.put(item);
        } catch (java.lang.InterruptedException ie) {
            // This exception will never be thrown, since we allow our queue
            // to be have an unbounded size. put() will therefore never block.
        }
    }

    public void stopForApp(java.lang.Object callerIdentity) {
        if (android.speech.tts.AudioPlaybackHandler.DBG)
            android.util.Log.d(android.speech.tts.AudioPlaybackHandler.TAG, "Removing all callback items for : " + callerIdentity);

        removeWorkItemsFor(callerIdentity);
        final android.speech.tts.PlaybackQueueItem current = mCurrentWorkItem;
        if ((current != null) && (current.getCallerIdentity() == callerIdentity)) {
            stop(current);
        }
    }

    public void stop() {
        if (android.speech.tts.AudioPlaybackHandler.DBG)
            android.util.Log.d(android.speech.tts.AudioPlaybackHandler.TAG, "Stopping all items");

        removeAllMessages();
        stop(mCurrentWorkItem);
    }

    /**
     *
     *
     * @return false iff the queue is empty and no queue item is currently
    being handled, true otherwise.
     */
    public boolean isSpeaking() {
        return (mQueue.peek() != null) || (mCurrentWorkItem != null);
    }

    /**
     * Shut down the audio playback thread.
     */
    public void quit() {
        removeAllMessages();
        stop(mCurrentWorkItem);
        mHandlerThread.interrupt();
    }

    /* Atomically clear the queue of all messages. */
    private void removeAllMessages() {
        mQueue.clear();
    }

    /* Remove all messages that originate from a given calling app. */
    private void removeWorkItemsFor(java.lang.Object callerIdentity) {
        java.util.Iterator<android.speech.tts.PlaybackQueueItem> it = mQueue.iterator();
        while (it.hasNext()) {
            final android.speech.tts.PlaybackQueueItem item = it.next();
            if (item.getCallerIdentity() == callerIdentity) {
                it.remove();
            }
        } 
    }

    /* The MessageLoop is a handler like implementation that
    processes messages from a priority queue.
     */
    private final class MessageLoop implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            while (true) {
                android.speech.tts.PlaybackQueueItem item = null;
                try {
                    item = mQueue.take();
                } catch (java.lang.InterruptedException ie) {
                    if (android.speech.tts.AudioPlaybackHandler.DBG)
                        android.util.Log.d(android.speech.tts.AudioPlaybackHandler.TAG, "MessageLoop : Shutting down (interrupted)");

                    return;
                }
                // If stop() or stopForApp() are called between mQueue.take()
                // returning and mCurrentWorkItem being set, the current work item
                // will be run anyway.
                mCurrentWorkItem = item;
                item.run();
                mCurrentWorkItem = null;
            } 
        }
    }
}

