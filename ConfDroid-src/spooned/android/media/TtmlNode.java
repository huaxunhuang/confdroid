package android.media;


/**
 * A container class which represents a node in TTML.
 *
 * @unknown 
 */
class TtmlNode {
    public final java.lang.String mName;

    public final java.lang.String mAttributes;

    public final android.media.TtmlNode mParent;

    public final java.lang.String mText;

    public final java.util.List<android.media.TtmlNode> mChildren = new java.util.ArrayList<android.media.TtmlNode>();

    public final long mRunId;

    public final long mStartTimeMs;

    public final long mEndTimeMs;

    public TtmlNode(java.lang.String name, java.lang.String attributes, java.lang.String text, long startTimeMs, long endTimeMs, android.media.TtmlNode parent, long runId) {
        this.mName = name;
        this.mAttributes = attributes;
        this.mText = text;
        this.mStartTimeMs = startTimeMs;
        this.mEndTimeMs = endTimeMs;
        this.mParent = parent;
        this.mRunId = runId;
    }

    /**
     * Check if this node is active in the given time range.
     *
     * @param startTimeMs
     * 		The start time of the range to check in microsecond.
     * @param endTimeMs
     * 		The end time of the range to check in microsecond.
     * @return return true if the given range overlaps the time range of this
    node.
     */
    public boolean isActive(long startTimeMs, long endTimeMs) {
        return (this.mEndTimeMs > startTimeMs) && (this.mStartTimeMs < endTimeMs);
    }
}

