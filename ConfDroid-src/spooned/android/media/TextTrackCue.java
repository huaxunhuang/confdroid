package android.media;


/**
 *
 *
 * @unknown 
 */
class TextTrackCue extends android.media.SubtitleTrack.Cue {
    static final int WRITING_DIRECTION_HORIZONTAL = 100;

    static final int WRITING_DIRECTION_VERTICAL_RL = 101;

    static final int WRITING_DIRECTION_VERTICAL_LR = 102;

    static final int ALIGNMENT_MIDDLE = 200;

    static final int ALIGNMENT_START = 201;

    static final int ALIGNMENT_END = 202;

    static final int ALIGNMENT_LEFT = 203;

    static final int ALIGNMENT_RIGHT = 204;

    private static final java.lang.String TAG = "TTCue";

    java.lang.String mId;

    boolean mPauseOnExit;

    int mWritingDirection;

    java.lang.String mRegionId;

    boolean mSnapToLines;

    java.lang.Integer mLinePosition;// null means AUTO


    boolean mAutoLinePosition;

    int mTextPosition;

    int mSize;

    int mAlignment;

    // Vector<String> mText;
    java.lang.String[] mStrings;

    android.media.TextTrackCueSpan[][] mLines;

    android.media.TextTrackRegion mRegion;

    TextTrackCue() {
        mId = "";
        mPauseOnExit = false;
        mWritingDirection = android.media.TextTrackCue.WRITING_DIRECTION_HORIZONTAL;
        mRegionId = "";
        mSnapToLines = true;
        /* AUTO */
        mLinePosition = null;
        mTextPosition = 50;
        mSize = 100;
        mAlignment = android.media.TextTrackCue.ALIGNMENT_MIDDLE;
        mLines = null;
        mRegion = null;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.media.TextTrackCue)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        try {
            android.media.TextTrackCue cue = ((android.media.TextTrackCue) (o));
            boolean res = (((((((((mId.equals(cue.mId) && (mPauseOnExit == cue.mPauseOnExit)) && (mWritingDirection == cue.mWritingDirection)) && mRegionId.equals(cue.mRegionId)) && (mSnapToLines == cue.mSnapToLines)) && (mAutoLinePosition == cue.mAutoLinePosition)) && (mAutoLinePosition || (((mLinePosition != null) && mLinePosition.equals(cue.mLinePosition)) || ((mLinePosition == null) && (cue.mLinePosition == null))))) && (mTextPosition == cue.mTextPosition)) && (mSize == cue.mSize)) && (mAlignment == cue.mAlignment)) && (mLines.length == cue.mLines.length);
            if (res == true) {
                for (int line = 0; line < mLines.length; line++) {
                    if (!java.util.Arrays.equals(mLines[line], cue.mLines[line])) {
                        return false;
                    }
                }
            }
            return res;
        } catch (java.lang.IncompatibleClassChangeError e) {
            return false;
        }
    }

    public java.lang.StringBuilder appendStringsToBuilder(java.lang.StringBuilder builder) {
        if (mStrings == null) {
            builder.append("null");
        } else {
            builder.append("[");
            boolean first = true;
            for (java.lang.String s : mStrings) {
                if (!first) {
                    builder.append(", ");
                }
                if (s == null) {
                    builder.append("null");
                } else {
                    builder.append("\"");
                    builder.append(s);
                    builder.append("\"");
                }
                first = false;
            }
            builder.append("]");
        }
        return builder;
    }

    public java.lang.StringBuilder appendLinesToBuilder(java.lang.StringBuilder builder) {
        if (mLines == null) {
            builder.append("null");
        } else {
            builder.append("[");
            boolean first = true;
            for (android.media.TextTrackCueSpan[] spans : mLines) {
                if (!first) {
                    builder.append(", ");
                }
                if (spans == null) {
                    builder.append("null");
                } else {
                    builder.append("\"");
                    boolean innerFirst = true;
                    long lastTimestamp = -1;
                    for (android.media.TextTrackCueSpan span : spans) {
                        if (!innerFirst) {
                            builder.append(" ");
                        }
                        if (span.mTimestampMs != lastTimestamp) {
                            builder.append("<").append(android.media.WebVttParser.timeToString(span.mTimestampMs)).append(">");
                            lastTimestamp = span.mTimestampMs;
                        }
                        builder.append(span.mText);
                        innerFirst = false;
                    }
                    builder.append("\"");
                }
                first = false;
            }
            builder.append("]");
        }
        return builder;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder res = new java.lang.StringBuilder();
        res.append(android.media.WebVttParser.timeToString(mStartTimeMs)).append(" --> ").append(android.media.WebVttParser.timeToString(mEndTimeMs)).append(" {id:\"").append(mId).append("\", pauseOnExit:").append(mPauseOnExit).append(", direction:").append(mWritingDirection == android.media.TextTrackCue.WRITING_DIRECTION_HORIZONTAL ? "horizontal" : mWritingDirection == android.media.TextTrackCue.WRITING_DIRECTION_VERTICAL_LR ? "vertical_lr" : mWritingDirection == android.media.TextTrackCue.WRITING_DIRECTION_VERTICAL_RL ? "vertical_rl" : "INVALID").append(", regionId:\"").append(mRegionId).append("\", snapToLines:").append(mSnapToLines).append(", linePosition:").append(mAutoLinePosition ? "auto" : mLinePosition).append(", textPosition:").append(mTextPosition).append(", size:").append(mSize).append(", alignment:").append(mAlignment == android.media.TextTrackCue.ALIGNMENT_END ? "end" : mAlignment == android.media.TextTrackCue.ALIGNMENT_LEFT ? "left" : mAlignment == android.media.TextTrackCue.ALIGNMENT_MIDDLE ? "middle" : mAlignment == android.media.TextTrackCue.ALIGNMENT_RIGHT ? "right" : mAlignment == android.media.TextTrackCue.ALIGNMENT_START ? "start" : "INVALID").append(", text:");
        appendStringsToBuilder(res).append("}");
        return res.toString();
    }

    @java.lang.Override
    public int hashCode() {
        return toString().hashCode();
    }

    @java.lang.Override
    public void onTime(long timeMs) {
        for (android.media.TextTrackCueSpan[] line : mLines) {
            for (android.media.TextTrackCueSpan span : line) {
                span.mEnabled = timeMs >= span.mTimestampMs;
            }
        }
    }
}

