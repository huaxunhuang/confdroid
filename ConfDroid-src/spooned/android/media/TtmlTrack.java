package android.media;


/**
 *
 *
 * @unknown 
 */
class TtmlTrack extends android.media.SubtitleTrack implements android.media.TtmlNodeListener {
    private static final java.lang.String TAG = "TtmlTrack";

    private final android.media.TtmlParser mParser = new android.media.TtmlParser(this);

    private final android.media.TtmlRenderingWidget mRenderingWidget;

    private java.lang.String mParsingData;

    private java.lang.Long mCurrentRunID;

    private final java.util.LinkedList<android.media.TtmlNode> mTtmlNodes;

    private final java.util.TreeSet<java.lang.Long> mTimeEvents;

    private android.media.TtmlNode mRootNode;

    TtmlTrack(android.media.TtmlRenderingWidget renderingWidget, android.media.MediaFormat format) {
        super(format);
        mTtmlNodes = new java.util.LinkedList<android.media.TtmlNode>();
        mTimeEvents = new java.util.TreeSet<java.lang.Long>();
        mRenderingWidget = renderingWidget;
        mParsingData = "";
    }

    @java.lang.Override
    public android.media.TtmlRenderingWidget getRenderingWidget() {
        return mRenderingWidget;
    }

    @java.lang.Override
    public void onData(byte[] data, boolean eos, long runID) {
        try {
            // TODO: handle UTF-8 conversion properly
            java.lang.String str = new java.lang.String(data, "UTF-8");
            // implement intermixing restriction for TTML.
            synchronized(mParser) {
                if ((mCurrentRunID != null) && (runID != mCurrentRunID)) {
                    throw new java.lang.IllegalStateException((("Run #" + mCurrentRunID) + " in progress.  Cannot process run #") + runID);
                }
                mCurrentRunID = runID;
                mParsingData += str;
                if (eos) {
                    try {
                        mParser.parse(mParsingData, mCurrentRunID);
                    } catch (org.xmlpull.v1.XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                    finishedRun(runID);
                    mParsingData = "";
                    mCurrentRunID = null;
                }
            }
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Log.w(android.media.TtmlTrack.TAG, "subtitle data is not UTF-8 encoded: " + e);
        }
    }

    @java.lang.Override
    public void onTtmlNodeParsed(android.media.TtmlNode node) {
        mTtmlNodes.addLast(node);
        addTimeEvents(node);
    }

    @java.lang.Override
    public void onRootNodeParsed(android.media.TtmlNode node) {
        mRootNode = node;
        android.media.TtmlCue cue = null;
        while ((cue = getNextResult()) != null) {
            addCue(cue);
        } 
        mRootNode = null;
        mTtmlNodes.clear();
        mTimeEvents.clear();
    }

    @java.lang.Override
    public void updateView(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues) {
        if (!mVisible) {
            // don't keep the state if we are not visible
            return;
        }
        if (DEBUG && (mTimeProvider != null)) {
            try {
                android.util.Log.d(android.media.TtmlTrack.TAG, ("at " + (mTimeProvider.getCurrentTimeUs(false, true) / 1000)) + " ms the active cues are:");
            } catch (java.lang.IllegalStateException e) {
                android.util.Log.d(android.media.TtmlTrack.TAG, "at (illegal state) the active cues are:");
            }
        }
        mRenderingWidget.setActiveCues(activeCues);
    }

    /**
     * Returns a {@link TtmlCue} in the presentation time order.
     * {@code null} is returned if there is no more timed text to show.
     */
    public android.media.TtmlCue getNextResult() {
        while (mTimeEvents.size() >= 2) {
            long start = mTimeEvents.pollFirst();
            long end = mTimeEvents.first();
            java.util.List<android.media.TtmlNode> activeCues = getActiveNodes(start, end);
            if (!activeCues.isEmpty()) {
                return new android.media.TtmlCue(start, end, android.media.TtmlUtils.applySpacePolicy(android.media.TtmlUtils.extractText(mRootNode, start, end), false), android.media.TtmlUtils.extractTtmlFragment(mRootNode, start, end));
            }
        } 
        return null;
    }

    private void addTimeEvents(android.media.TtmlNode node) {
        mTimeEvents.add(node.mStartTimeMs);
        mTimeEvents.add(node.mEndTimeMs);
        for (int i = 0; i < node.mChildren.size(); ++i) {
            addTimeEvents(node.mChildren.get(i));
        }
    }

    private java.util.List<android.media.TtmlNode> getActiveNodes(long startTimeUs, long endTimeUs) {
        java.util.List<android.media.TtmlNode> activeNodes = new java.util.ArrayList<android.media.TtmlNode>();
        for (int i = 0; i < mTtmlNodes.size(); ++i) {
            android.media.TtmlNode node = mTtmlNodes.get(i);
            if (node.isActive(startTimeUs, endTimeUs)) {
                activeNodes.add(node);
            }
        }
        return activeNodes;
    }
}

