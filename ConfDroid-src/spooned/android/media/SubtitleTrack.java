/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * A subtitle track abstract base class that is responsible for parsing and displaying
 * an instance of a particular type of subtitle.
 *
 * @unknown 
 */
public abstract class SubtitleTrack implements android.media.MediaTimeProvider.OnMediaTimeListener {
    private static final java.lang.String TAG = "SubtitleTrack";

    private long mLastUpdateTimeMs;

    private long mLastTimeMs;

    private java.lang.Runnable mRunnable;

    /**
     *
     *
     * @unknown TODO private
     */
    protected final android.util.LongSparseArray<android.media.SubtitleTrack.Run> mRunsByEndTime = new android.util.LongSparseArray<android.media.SubtitleTrack.Run>();

    /**
     *
     *
     * @unknown TODO private
     */
    protected final android.util.LongSparseArray<android.media.SubtitleTrack.Run> mRunsByID = new android.util.LongSparseArray<android.media.SubtitleTrack.Run>();

    /**
     *
     *
     * @unknown TODO private
     */
    protected android.media.SubtitleTrack.CueList mCues;

    /**
     *
     *
     * @unknown TODO private
     */
    protected final java.util.Vector<android.media.SubtitleTrack.Cue> mActiveCues = new java.util.Vector<android.media.SubtitleTrack.Cue>();

    /**
     *
     *
     * @unknown 
     */
    protected boolean mVisible;

    /**
     *
     *
     * @unknown 
     */
    public boolean DEBUG = false;

    /**
     *
     *
     * @unknown 
     */
    protected android.os.Handler mHandler = new android.os.Handler();

    private android.media.MediaFormat mFormat;

    public SubtitleTrack(android.media.MediaFormat format) {
        mFormat = format;
        mCues = new android.media.SubtitleTrack.CueList();
        clearActiveCues();
        mLastTimeMs = -1;
    }

    /**
     *
     *
     * @unknown 
     */
    public final android.media.MediaFormat getFormat() {
        return mFormat;
    }

    private long mNextScheduledTimeMs = -1;

    protected void onData(android.media.SubtitleData data) {
        long runID = data.getStartTimeUs() + 1;
        /* eos */
        onData(data.getData(), true, runID);
        setRunDiscardTimeMs(runID, (data.getStartTimeUs() + data.getDurationUs()) / 1000);
    }

    /**
     * Called when there is input data for the subtitle track.  The
     * complete subtitle for a track can include multiple whole units
     * (runs).  Each of these units can have multiple sections.  The
     * contents of a run are submitted in sequential order, with eos
     * indicating the last section of the run.  Calls from different
     * runs must not be intermixed.
     *
     * @param data
     * 		subtitle data byte buffer
     * @param eos
     * 		true if this is the last section of the run.
     * @param runID
     * 		mostly-unique ID for this run of data.  Subtitle cues
     * 		with runID of 0 are discarded immediately after
     * 		display.  Cues with runID of ~0 are discarded
     * 		only at the deletion of the track object.  Cues
     * 		with other runID-s are discarded at the end of the
     * 		run, which defaults to the latest timestamp of
     * 		any of its cues (with this runID).
     */
    public abstract void onData(byte[] data, boolean eos, long runID);

    /**
     * Called when adding the subtitle rendering widget to the view hierarchy,
     * as well as when showing or hiding the subtitle track, or when the video
     * surface position has changed.
     *
     * @return the widget that renders this subtitle track. For most renderers
    there should be a single shared instance that is used for all
    tracks supported by that renderer, as at most one subtitle track
    is visible at one time.
     */
    public abstract android.media.SubtitleTrack.RenderingWidget getRenderingWidget();

    /**
     * Called when the active cues have changed, and the contents of the subtitle
     * view should be updated.
     *
     * @unknown 
     */
    public abstract void updateView(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues);

    /**
     *
     *
     * @unknown 
     */
    protected synchronized void updateActiveCues(boolean rebuild, long timeMs) {
        // out-of-order times mean seeking or new active cues being added
        // (during their own timespan)
        if (rebuild || (mLastUpdateTimeMs > timeMs)) {
            clearActiveCues();
        }
        for (java.util.Iterator<android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue>> it = mCues.entriesBetween(mLastUpdateTimeMs, timeMs).iterator(); it.hasNext();) {
            android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue> event = it.next();
            android.media.SubtitleTrack.Cue cue = event.second;
            if (cue.mEndTimeMs == event.first) {
                // remove past cues
                if (DEBUG)
                    android.util.Log.v(android.media.SubtitleTrack.TAG, "Removing " + cue);

                mActiveCues.remove(cue);
                if (cue.mRunID == 0) {
                    it.remove();
                }
            } else
                if (cue.mStartTimeMs == event.first) {
                    // add new cues
                    // TRICKY: this will happen in start order
                    if (DEBUG)
                        android.util.Log.v(android.media.SubtitleTrack.TAG, "Adding " + cue);

                    if (cue.mInnerTimesMs != null) {
                        cue.onTime(timeMs);
                    }
                    mActiveCues.add(cue);
                } else
                    if (cue.mInnerTimesMs != null) {
                        // cue is modified
                        cue.onTime(timeMs);
                    }


        }
        /* complete any runs */
        while ((mRunsByEndTime.size() > 0) && (mRunsByEndTime.keyAt(0) <= timeMs)) {
            removeRunsByEndTimeIndex(0);// removes element

        } 
        mLastUpdateTimeMs = timeMs;
    }

    private void removeRunsByEndTimeIndex(int ix) {
        android.media.SubtitleTrack.Run run = mRunsByEndTime.valueAt(ix);
        while (run != null) {
            android.media.SubtitleTrack.Cue cue = run.mFirstCue;
            while (cue != null) {
                mCues.remove(cue);
                android.media.SubtitleTrack.Cue nextCue = cue.mNextInRun;
                cue.mNextInRun = null;
                cue = nextCue;
            } 
            mRunsByID.remove(run.mRunID);
            android.media.SubtitleTrack.Run nextRun = run.mNextRunAtEndTimeMs;
            run.mPrevRunAtEndTimeMs = null;
            run.mNextRunAtEndTimeMs = null;
            run = nextRun;
        } 
        mRunsByEndTime.removeAt(ix);
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        /* remove all cues (untangle all cross-links) */
        int size = mRunsByEndTime.size();
        for (int ix = size - 1; ix >= 0; ix--) {
            removeRunsByEndTimeIndex(ix);
        }
        super.finalize();
    }

    private synchronized void takeTime(long timeMs) {
        mLastTimeMs = timeMs;
    }

    /**
     *
     *
     * @unknown 
     */
    protected synchronized void clearActiveCues() {
        if (DEBUG)
            android.util.Log.v(android.media.SubtitleTrack.TAG, ("Clearing " + mActiveCues.size()) + " active cues");

        mActiveCues.clear();
        mLastUpdateTimeMs = -1;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void scheduleTimedEvents() {
        /* get times for the next event */
        if (mTimeProvider != null) {
            mNextScheduledTimeMs = mCues.nextTimeAfter(mLastTimeMs);
            if (DEBUG)
                android.util.Log.d(android.media.SubtitleTrack.TAG, (("sched @" + mNextScheduledTimeMs) + " after ") + mLastTimeMs);

            mTimeProvider.notifyAt(mNextScheduledTimeMs >= 0 ? mNextScheduledTimeMs * 1000 : android.media.MediaTimeProvider.NO_TIME, this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onTimedEvent(long timeUs) {
        if (DEBUG)
            android.util.Log.d(android.media.SubtitleTrack.TAG, "onTimedEvent " + timeUs);

        synchronized(this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(false, timeMs);
            takeTime(timeMs);
        }
        updateView(mActiveCues);
        scheduleTimedEvents();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onSeek(long timeUs) {
        if (DEBUG)
            android.util.Log.d(android.media.SubtitleTrack.TAG, "onSeek " + timeUs);

        synchronized(this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(true, timeMs);
            takeTime(timeMs);
        }
        updateView(mActiveCues);
        scheduleTimedEvents();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onStop() {
        synchronized(this) {
            if (DEBUG)
                android.util.Log.d(android.media.SubtitleTrack.TAG, "onStop");

            clearActiveCues();
            mLastTimeMs = -1;
        }
        updateView(mActiveCues);
        mNextScheduledTimeMs = -1;
        mTimeProvider.notifyAt(android.media.MediaTimeProvider.NO_TIME, this);
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.media.MediaTimeProvider mTimeProvider;

    /**
     *
     *
     * @unknown 
     */
    public void show() {
        if (mVisible) {
            return;
        }
        mVisible = true;
        android.media.SubtitleTrack.RenderingWidget renderingWidget = getRenderingWidget();
        if (renderingWidget != null) {
            renderingWidget.setVisible(true);
        }
        if (mTimeProvider != null) {
            mTimeProvider.scheduleUpdate(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void hide() {
        if (!mVisible) {
            return;
        }
        if (mTimeProvider != null) {
            mTimeProvider.cancelNotifications(this);
        }
        android.media.SubtitleTrack.RenderingWidget renderingWidget = getRenderingWidget();
        if (renderingWidget != null) {
            renderingWidget.setVisible(false);
        }
        mVisible = false;
    }

    /**
     *
     *
     * @unknown 
     */
    protected synchronized boolean addCue(android.media.SubtitleTrack.Cue cue) {
        mCues.add(cue);
        if (cue.mRunID != 0) {
            android.media.SubtitleTrack.Run run = mRunsByID.get(cue.mRunID);
            if (run == null) {
                run = new android.media.SubtitleTrack.Run();
                mRunsByID.put(cue.mRunID, run);
                run.mEndTimeMs = cue.mEndTimeMs;
            } else
                if (run.mEndTimeMs < cue.mEndTimeMs) {
                    run.mEndTimeMs = cue.mEndTimeMs;
                }

            // link-up cues in the same run
            cue.mNextInRun = run.mFirstCue;
            run.mFirstCue = cue;
        }
        // if a cue is added that should be visible, need to refresh view
        long nowMs = -1;
        if (mTimeProvider != null) {
            try {
                nowMs = /* precise */
                /* monotonic */
                mTimeProvider.getCurrentTimeUs(false, true) / 1000;
            } catch (java.lang.IllegalStateException e) {
                // handle as it we are not playing
            }
        }
        if (DEBUG)
            android.util.Log.v(android.media.SubtitleTrack.TAG, (((((((("mVisible=" + mVisible) + ", ") + cue.mStartTimeMs) + " <= ") + nowMs) + ", ") + cue.mEndTimeMs) + " >= ") + mLastTimeMs);

        if ((mVisible && (cue.mStartTimeMs <= nowMs)) && // we don't trust nowMs, so check any cue since last callback
        (cue.mEndTimeMs >= mLastTimeMs)) {
            if (mRunnable != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            final android.media.SubtitleTrack track = this;
            final long thenMs = nowMs;
            mRunnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    // even with synchronized, it is possible that we are going
                    // to do multiple updates as the runnable could be already
                    // running.
                    synchronized(track) {
                        mRunnable = null;
                        updateActiveCues(true, thenMs);
                        updateView(mActiveCues);
                    }
                }
            };
            // delay update so we don't update view on every cue.  TODO why 10?
            if (/* delay */
            mHandler.postDelayed(mRunnable, 10)) {
                if (DEBUG)
                    android.util.Log.v(android.media.SubtitleTrack.TAG, "scheduling update");

            } else {
                if (DEBUG)
                    android.util.Log.w(android.media.SubtitleTrack.TAG, "failed to schedule subtitle view update");

            }
            return true;
        }
        if ((mVisible && (cue.mEndTimeMs >= mLastTimeMs)) && ((cue.mStartTimeMs < mNextScheduledTimeMs) || (mNextScheduledTimeMs < 0))) {
            scheduleTimedEvents();
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public synchronized void setTimeProvider(android.media.MediaTimeProvider timeProvider) {
        if (mTimeProvider == timeProvider) {
            return;
        }
        if (mTimeProvider != null) {
            mTimeProvider.cancelNotifications(this);
        }
        mTimeProvider = timeProvider;
        if (mTimeProvider != null) {
            mTimeProvider.scheduleUpdate(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    static class CueList {
        private static final java.lang.String TAG = "CueList";

        // simplistic, inefficient implementation
        private java.util.SortedMap<java.lang.Long, java.util.Vector<android.media.SubtitleTrack.Cue>> mCues;

        public boolean DEBUG = false;

        private boolean addEvent(android.media.SubtitleTrack.Cue cue, long timeMs) {
            java.util.Vector<android.media.SubtitleTrack.Cue> cues = mCues.get(timeMs);
            if (cues == null) {
                cues = new java.util.Vector<android.media.SubtitleTrack.Cue>(2);
                mCues.put(timeMs, cues);
            } else
                if (cues.contains(cue)) {
                    // do not duplicate cues
                    return false;
                }

            cues.add(cue);
            return true;
        }

        private void removeEvent(android.media.SubtitleTrack.Cue cue, long timeMs) {
            java.util.Vector<android.media.SubtitleTrack.Cue> cues = mCues.get(timeMs);
            if (cues != null) {
                cues.remove(cue);
                if (cues.size() == 0) {
                    mCues.remove(timeMs);
                }
            }
        }

        public void add(android.media.SubtitleTrack.Cue cue) {
            // ignore non-positive-duration cues
            if (cue.mStartTimeMs >= cue.mEndTimeMs)
                return;

            if (!addEvent(cue, cue.mStartTimeMs)) {
                return;
            }
            long lastTimeMs = cue.mStartTimeMs;
            if (cue.mInnerTimesMs != null) {
                for (long timeMs : cue.mInnerTimesMs) {
                    if ((timeMs > lastTimeMs) && (timeMs < cue.mEndTimeMs)) {
                        addEvent(cue, timeMs);
                        lastTimeMs = timeMs;
                    }
                }
            }
            addEvent(cue, cue.mEndTimeMs);
        }

        public void remove(android.media.SubtitleTrack.Cue cue) {
            removeEvent(cue, cue.mStartTimeMs);
            if (cue.mInnerTimesMs != null) {
                for (long timeMs : cue.mInnerTimesMs) {
                    removeEvent(cue, timeMs);
                }
            }
            removeEvent(cue, cue.mEndTimeMs);
        }

        public java.lang.Iterable<android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue>> entriesBetween(final long lastTimeMs, final long timeMs) {
            return new java.lang.Iterable<android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue>>() {
                @java.lang.Override
                public java.util.Iterator<android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue>> iterator() {
                    if (DEBUG)
                        android.util.Log.d(android.media.SubtitleTrack.CueList.TAG, ((("slice (" + lastTimeMs) + ", ") + timeMs) + "]=");

                    try {
                        return new android.media.SubtitleTrack.CueList.EntryIterator(mCues.subMap(lastTimeMs + 1, timeMs + 1));
                    } catch (java.lang.IllegalArgumentException e) {
                        return new android.media.SubtitleTrack.CueList.EntryIterator(null);
                    }
                }
            };
        }

        public long nextTimeAfter(long timeMs) {
            java.util.SortedMap<java.lang.Long, java.util.Vector<android.media.SubtitleTrack.Cue>> tail = null;
            try {
                tail = mCues.tailMap(timeMs + 1);
                if (tail != null) {
                    return tail.firstKey();
                } else {
                    return -1;
                }
            } catch (java.lang.IllegalArgumentException e) {
                return -1;
            } catch (java.util.NoSuchElementException e) {
                return -1;
            }
        }

        class EntryIterator implements java.util.Iterator<android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue>> {
            @java.lang.Override
            public boolean hasNext() {
                return !mDone;
            }

            @java.lang.Override
            public android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue> next() {
                if (mDone) {
                    throw new java.util.NoSuchElementException("");
                }
                mLastEntry = new android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue>(mCurrentTimeMs, mListIterator.next());
                mLastListIterator = mListIterator;
                if (!mListIterator.hasNext()) {
                    nextKey();
                }
                return mLastEntry;
            }

            @java.lang.Override
            public void remove() {
                // only allow removing end tags
                if ((mLastListIterator == null) || (mLastEntry.second.mEndTimeMs != mLastEntry.first)) {
                    throw new java.lang.IllegalStateException("");
                }
                // remove end-cue
                mLastListIterator.remove();
                mLastListIterator = null;
                if (mCues.get(mLastEntry.first).size() == 0) {
                    mCues.remove(mLastEntry.first);
                }
                // remove rest of the cues
                android.media.SubtitleTrack.Cue cue = mLastEntry.second;
                removeEvent(cue, cue.mStartTimeMs);
                if (cue.mInnerTimesMs != null) {
                    for (long timeMs : cue.mInnerTimesMs) {
                        removeEvent(cue, timeMs);
                    }
                }
            }

            public EntryIterator(java.util.SortedMap<java.lang.Long, java.util.Vector<android.media.SubtitleTrack.Cue>> cues) {
                if (DEBUG)
                    android.util.Log.v(android.media.SubtitleTrack.CueList.TAG, cues + "");

                mRemainingCues = cues;
                mLastListIterator = null;
                nextKey();
            }

            private void nextKey() {
                do {
                    try {
                        if (mRemainingCues == null) {
                            throw new java.util.NoSuchElementException("");
                        }
                        mCurrentTimeMs = mRemainingCues.firstKey();
                        mListIterator = mRemainingCues.get(mCurrentTimeMs).iterator();
                        try {
                            mRemainingCues = mRemainingCues.tailMap(mCurrentTimeMs + 1);
                        } catch (java.lang.IllegalArgumentException e) {
                            mRemainingCues = null;
                        }
                        mDone = false;
                    } catch (java.util.NoSuchElementException e) {
                        mDone = true;
                        mRemainingCues = null;
                        mListIterator = null;
                        return;
                    }
                } while (!mListIterator.hasNext() );
            }

            private long mCurrentTimeMs;

            private java.util.Iterator<android.media.SubtitleTrack.Cue> mListIterator;

            private boolean mDone;

            private java.util.SortedMap<java.lang.Long, java.util.Vector<android.media.SubtitleTrack.Cue>> mRemainingCues;

            private java.util.Iterator<android.media.SubtitleTrack.Cue> mLastListIterator;

            private android.util.Pair<java.lang.Long, android.media.SubtitleTrack.Cue> mLastEntry;
        }

        CueList() {
            mCues = new java.util.TreeMap<java.lang.Long, java.util.Vector<android.media.SubtitleTrack.Cue>>();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static class Cue {
        public long mStartTimeMs;

        public long mEndTimeMs;

        public long[] mInnerTimesMs;

        public long mRunID;

        /**
         *
         *
         * @unknown 
         */
        public android.media.SubtitleTrack.Cue mNextInRun;

        public void onTime(long timeMs) {
        }
    }

    /**
     *
     *
     * @unknown update mRunsByEndTime (with default end time)
     */
    protected void finishedRun(long runID) {
        if ((runID != 0) && (runID != (~0))) {
            android.media.SubtitleTrack.Run run = mRunsByID.get(runID);
            if (run != null) {
                run.storeByEndTimeMs(mRunsByEndTime);
            }
        }
    }

    /**
     *
     *
     * @unknown update mRunsByEndTime with given end time
     */
    public void setRunDiscardTimeMs(long runID, long timeMs) {
        if ((runID != 0) && (runID != (~0))) {
            android.media.SubtitleTrack.Run run = mRunsByID.get(runID);
            if (run != null) {
                run.mEndTimeMs = timeMs;
                run.storeByEndTimeMs(mRunsByEndTime);
            }
        }
    }

    /**
     *
     *
     * @unknown whether this is a text track who fires events instead getting rendered
     */
    public int getTrackType() {
        return getRenderingWidget() == null ? android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT : android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE;
    }

    /**
     *
     *
     * @unknown 
     */
    private static class Run {
        public android.media.SubtitleTrack.Cue mFirstCue;

        public android.media.SubtitleTrack.Run mNextRunAtEndTimeMs;

        public android.media.SubtitleTrack.Run mPrevRunAtEndTimeMs;

        public long mEndTimeMs = -1;

        public long mRunID = 0;

        private long mStoredEndTimeMs = -1;

        public void storeByEndTimeMs(android.util.LongSparseArray<android.media.SubtitleTrack.Run> runsByEndTime) {
            // remove old value if any
            int ix = runsByEndTime.indexOfKey(mStoredEndTimeMs);
            if (ix >= 0) {
                if (mPrevRunAtEndTimeMs == null) {
                    assert this == runsByEndTime.valueAt(ix);
                    if (mNextRunAtEndTimeMs == null) {
                        runsByEndTime.removeAt(ix);
                    } else {
                        runsByEndTime.setValueAt(ix, mNextRunAtEndTimeMs);
                    }
                }
                removeAtEndTimeMs();
            }
            // add new value
            if (mEndTimeMs >= 0) {
                mPrevRunAtEndTimeMs = null;
                mNextRunAtEndTimeMs = runsByEndTime.get(mEndTimeMs);
                if (mNextRunAtEndTimeMs != null) {
                    mNextRunAtEndTimeMs.mPrevRunAtEndTimeMs = this;
                }
                runsByEndTime.put(mEndTimeMs, this);
                mStoredEndTimeMs = mEndTimeMs;
            }
        }

        public void removeAtEndTimeMs() {
            android.media.SubtitleTrack.Run prev = mPrevRunAtEndTimeMs;
            if (mPrevRunAtEndTimeMs != null) {
                mPrevRunAtEndTimeMs.mNextRunAtEndTimeMs = mNextRunAtEndTimeMs;
                mPrevRunAtEndTimeMs = null;
            }
            if (mNextRunAtEndTimeMs != null) {
                mNextRunAtEndTimeMs.mPrevRunAtEndTimeMs = prev;
                mNextRunAtEndTimeMs = null;
            }
        }
    }

    /**
     * Interface for rendering subtitles onto a Canvas.
     */
    public interface RenderingWidget {
        /**
         * Sets the widget's callback, which is used to send updates when the
         * rendered data has changed.
         *
         * @param callback
         * 		update callback
         */
        public void setOnChangedListener(android.media.SubtitleTrack.RenderingWidget.OnChangedListener callback);

        /**
         * Sets the widget's size.
         *
         * @param width
         * 		width in pixels
         * @param height
         * 		height in pixels
         */
        public void setSize(int width, int height);

        /**
         * Sets whether the widget should draw subtitles.
         *
         * @param visible
         * 		true if subtitles should be drawn, false otherwise
         */
        public void setVisible(boolean visible);

        /**
         * Renders subtitles onto a {@link Canvas}.
         *
         * @param c
         * 		canvas on which to render subtitles
         */
        public void draw(android.graphics.Canvas c);

        /**
         * Called when the widget is attached to a window.
         */
        public void onAttachedToWindow();

        /**
         * Called when the widget is detached from a window.
         */
        public void onDetachedFromWindow();

        /**
         * Callback used to send updates about changes to rendering data.
         */
        public interface OnChangedListener {
            /**
             * Called when the rendering data has changed.
             *
             * @param renderingWidget
             * 		the widget whose data has changed
             */
            public void onChanged(android.media.SubtitleTrack.RenderingWidget renderingWidget);
        }
    }
}

