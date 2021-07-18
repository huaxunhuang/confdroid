package android.media;


/**
 *
 *
 * @unknown Extract all text without style, but with timestamp spans.
 */
class UnstyledTextExtractor implements android.media.Tokenizer.OnTokenListener {
    java.lang.StringBuilder mLine = new java.lang.StringBuilder();

    java.util.Vector<android.media.TextTrackCueSpan[]> mLines = new java.util.Vector<android.media.TextTrackCueSpan[]>();

    java.util.Vector<android.media.TextTrackCueSpan> mCurrentLine = new java.util.Vector<android.media.TextTrackCueSpan>();

    long mLastTimestamp;

    UnstyledTextExtractor() {
        init();
    }

    private void init() {
        mLine.delete(0, mLine.length());
        mLines.clear();
        mCurrentLine.clear();
        mLastTimestamp = -1;
    }

    @java.lang.Override
    public void onData(java.lang.String s) {
        mLine.append(s);
    }

    @java.lang.Override
    public void onStart(java.lang.String tag, java.lang.String[] classes, java.lang.String annotation) {
    }

    @java.lang.Override
    public void onEnd(java.lang.String tag) {
    }

    @java.lang.Override
    public void onTimeStamp(long timestampMs) {
        // finish any prior span
        if ((mLine.length() > 0) && (timestampMs != mLastTimestamp)) {
            mCurrentLine.add(new android.media.TextTrackCueSpan(mLine.toString(), mLastTimestamp));
            mLine.delete(0, mLine.length());
        }
        mLastTimestamp = timestampMs;
    }

    @java.lang.Override
    public void onLineEnd() {
        // finish any pending span
        if (mLine.length() > 0) {
            mCurrentLine.add(new android.media.TextTrackCueSpan(mLine.toString(), mLastTimestamp));
            mLine.delete(0, mLine.length());
        }
        android.media.TextTrackCueSpan[] spans = new android.media.TextTrackCueSpan[mCurrentLine.size()];
        mCurrentLine.toArray(spans);
        mCurrentLine.clear();
        mLines.add(spans);
    }

    public android.media.TextTrackCueSpan[][] getText() {
        // for politeness, finish last cue-line if it ends abruptly
        if ((mLine.length() > 0) || (mCurrentLine.size() > 0)) {
            onLineEnd();
        }
        android.media.TextTrackCueSpan[][] lines = new android.media.TextTrackCueSpan[mLines.size()][];
        mLines.toArray(lines);
        init();
        return lines;
    }
}

