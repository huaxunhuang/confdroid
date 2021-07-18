package android.media;


/**
 *
 *
 * @unknown 
 */
class WebVttTrack extends android.media.SubtitleTrack implements android.media.WebVttCueListener {
    private static final java.lang.String TAG = "WebVttTrack";

    private final android.media.WebVttParser mParser = new android.media.WebVttParser(this);

    private final android.media.UnstyledTextExtractor mExtractor = new android.media.UnstyledTextExtractor();

    private final android.media.Tokenizer mTokenizer = new android.media.Tokenizer(mExtractor);

    private final java.util.Vector<java.lang.Long> mTimestamps = new java.util.Vector<java.lang.Long>();

    private final android.media.WebVttRenderingWidget mRenderingWidget;

    private final java.util.Map<java.lang.String, android.media.TextTrackRegion> mRegions = new java.util.HashMap<java.lang.String, android.media.TextTrackRegion>();

    private java.lang.Long mCurrentRunID;

    WebVttTrack(android.media.WebVttRenderingWidget renderingWidget, android.media.MediaFormat format) {
        super(format);
        mRenderingWidget = renderingWidget;
    }

    @java.lang.Override
    public android.media.WebVttRenderingWidget getRenderingWidget() {
        return mRenderingWidget;
    }

    @java.lang.Override
    public void onData(byte[] data, boolean eos, long runID) {
        try {
            java.lang.String str = new java.lang.String(data, "UTF-8");
            // implement intermixing restriction for WebVTT only for now
            synchronized(mParser) {
                if ((mCurrentRunID != null) && (runID != mCurrentRunID)) {
                    throw new java.lang.IllegalStateException((("Run #" + mCurrentRunID) + " in progress.  Cannot process run #") + runID);
                }
                mCurrentRunID = runID;
                mParser.parse(str);
                if (eos) {
                    finishedRun(runID);
                    mParser.eos();
                    mRegions.clear();
                    mCurrentRunID = null;
                }
            }
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Log.w(android.media.WebVttTrack.TAG, "subtitle data is not UTF-8 encoded: " + e);
        }
    }

    @java.lang.Override
    public void onCueParsed(android.media.TextTrackCue cue) {
        synchronized(mParser) {
            // resolve region
            if (cue.mRegionId.length() != 0) {
                cue.mRegion = mRegions.get(cue.mRegionId);
            }
            if (DEBUG)
                android.util.Log.v(android.media.WebVttTrack.TAG, "adding cue " + cue);

            // tokenize text track string-lines into lines of spans
            mTokenizer.reset();
            for (java.lang.String s : cue.mStrings) {
                mTokenizer.tokenize(s);
            }
            cue.mLines = mExtractor.getText();
            if (DEBUG)
                android.util.Log.v(android.media.WebVttTrack.TAG, cue.appendLinesToBuilder(cue.appendStringsToBuilder(new java.lang.StringBuilder()).append(" simplified to: ")).toString());

            // extract inner timestamps
            for (android.media.TextTrackCueSpan[] line : cue.mLines) {
                for (android.media.TextTrackCueSpan span : line) {
                    if (((span.mTimestampMs > cue.mStartTimeMs) && (span.mTimestampMs < cue.mEndTimeMs)) && (!mTimestamps.contains(span.mTimestampMs))) {
                        mTimestamps.add(span.mTimestampMs);
                    }
                }
            }
            if (mTimestamps.size() > 0) {
                cue.mInnerTimesMs = new long[mTimestamps.size()];
                for (int ix = 0; ix < mTimestamps.size(); ++ix) {
                    cue.mInnerTimesMs[ix] = mTimestamps.get(ix);
                }
                mTimestamps.clear();
            } else {
                cue.mInnerTimesMs = null;
            }
            cue.mRunID = mCurrentRunID;
        }
        addCue(cue);
    }

    @java.lang.Override
    public void onRegionParsed(android.media.TextTrackRegion region) {
        synchronized(mParser) {
            mRegions.put(region.mId, region);
        }
    }

    @java.lang.Override
    public void updateView(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues) {
        if (!mVisible) {
            // don't keep the state if we are not visible
            return;
        }
        if (DEBUG && (mTimeProvider != null)) {
            try {
                android.util.Log.d(android.media.WebVttTrack.TAG, ("at " + (mTimeProvider.getCurrentTimeUs(false, true) / 1000)) + " ms the active cues are:");
            } catch (java.lang.IllegalStateException e) {
                android.util.Log.d(android.media.WebVttTrack.TAG, "at (illegal state) the active cues are:");
            }
        }
        if (mRenderingWidget != null) {
            mRenderingWidget.setActiveCues(activeCues);
        }
    }
}

