package android.media;


/**
 *
 *
 * @unknown 
 */
class TextTrackCueSpan {
    long mTimestampMs;

    boolean mEnabled;

    java.lang.String mText;

    TextTrackCueSpan(java.lang.String text, long timestamp) {
        mTimestampMs = timestamp;
        mText = text;
        // spans with timestamp will be enabled by Cue.onTime
        mEnabled = mTimestampMs < 0;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.media.TextTrackCueSpan)) {
            return false;
        }
        android.media.TextTrackCueSpan span = ((android.media.TextTrackCueSpan) (o));
        return (mTimestampMs == span.mTimestampMs) && mText.equals(span.mText);
    }
}

