package android.media;


/**
 *
 *
 * @unknown 
 */
class TextTrackRegion {
    static final int SCROLL_VALUE_NONE = 300;

    static final int SCROLL_VALUE_SCROLL_UP = 301;

    java.lang.String mId;

    float mWidth;

    int mLines;

    float mAnchorPointX;

    float mAnchorPointY;

    float mViewportAnchorPointX;

    float mViewportAnchorPointY;

    int mScrollValue;

    TextTrackRegion() {
        mId = "";
        mWidth = 100;
        mLines = 3;
        mAnchorPointX = mViewportAnchorPointX = 0.0F;
        mAnchorPointY = mViewportAnchorPointY = 100.0F;
        mScrollValue = android.media.TextTrackRegion.SCROLL_VALUE_NONE;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder res = new java.lang.StringBuilder(" {id:\"").append(mId).append("\", width:").append(mWidth).append(", lines:").append(mLines).append(", anchorPoint:(").append(mAnchorPointX).append(", ").append(mAnchorPointY).append("), viewportAnchorPoints:").append(mViewportAnchorPointX).append(", ").append(mViewportAnchorPointY).append("), scrollValue:").append(mScrollValue == android.media.TextTrackRegion.SCROLL_VALUE_NONE ? "none" : mScrollValue == android.media.TextTrackRegion.SCROLL_VALUE_SCROLL_UP ? "scroll_up" : "INVALID").append("}");
        return res.toString();
    }
}

