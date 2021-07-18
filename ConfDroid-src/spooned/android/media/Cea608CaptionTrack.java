package android.media;


/**
 *
 *
 * @unknown 
 */
class Cea608CaptionTrack extends android.media.SubtitleTrack {
    private final android.media.Cea608CCParser mCCParser;

    private final android.media.Cea608CCWidget mRenderingWidget;

    Cea608CaptionTrack(android.media.Cea608CCWidget renderingWidget, android.media.MediaFormat format) {
        super(format);
        mRenderingWidget = renderingWidget;
        mCCParser = new android.media.Cea608CCParser(mRenderingWidget);
    }

    @java.lang.Override
    public void onData(byte[] data, boolean eos, long runID) {
        mCCParser.parse(data);
    }

    @java.lang.Override
    public android.media.SubtitleTrack.RenderingWidget getRenderingWidget() {
        return mRenderingWidget;
    }

    @java.lang.Override
    public void updateView(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues) {
        // Overriding with NO-OP, CC rendering by-passes this
    }
}

