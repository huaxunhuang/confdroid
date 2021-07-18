package android.media;


/**
 *
 *
 * @unknown 
 */
public class SRTRenderer extends android.media.SubtitleController.Renderer {
    private final android.content.Context mContext;

    private final boolean mRender;

    private final android.os.Handler mEventHandler;

    private android.media.WebVttRenderingWidget mRenderingWidget;

    public SRTRenderer(android.content.Context context) {
        this(context, null);
    }

    SRTRenderer(android.content.Context mContext, android.os.Handler mEventHandler) {
        this.mContext = mContext;
        this.mRender = mEventHandler == null;
        this.mEventHandler = mEventHandler;
    }

    @java.lang.Override
    public boolean supports(android.media.MediaFormat format) {
        if (format.containsKey(android.media.MediaFormat.KEY_MIME)) {
            if (!format.getString(android.media.MediaFormat.KEY_MIME).equals(android.media.MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP)) {
                return false;
            }
            return mRender == (format.getInteger(android.media.MediaFormat.KEY_IS_TIMED_TEXT, 0) == 0);
        }
        return false;
    }

    @java.lang.Override
    public android.media.SubtitleTrack createTrack(android.media.MediaFormat format) {
        if (mRender && (mRenderingWidget == null)) {
            mRenderingWidget = new android.media.WebVttRenderingWidget(mContext);
        }
        if (mRender) {
            return new android.media.SRTTrack(mRenderingWidget, format);
        } else {
            return new android.media.SRTTrack(mEventHandler, format);
        }
    }
}

