package android.media;


/**
 * A container class which represents a cue in TTML.
 *
 * @unknown 
 */
class TtmlCue extends android.media.SubtitleTrack.Cue {
    public java.lang.String mText;

    public java.lang.String mTtmlFragment;

    public TtmlCue(long startTimeMs, long endTimeMs, java.lang.String text, java.lang.String ttmlFragment) {
        this.mStartTimeMs = startTimeMs;
        this.mEndTimeMs = endTimeMs;
        this.mText = text;
        this.mTtmlFragment = ttmlFragment;
    }
}

