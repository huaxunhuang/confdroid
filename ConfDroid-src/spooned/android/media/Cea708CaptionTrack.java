package android.media;


/**
 *
 *
 * @unknown 
 */
class Cea708CaptionTrack extends android.media.SubtitleTrack {
    private final android.media.Cea708CCParser mCCParser;

    private final android.media.Cea708CCWidget mRenderingWidget;

    Cea708CaptionTrack(android.media.Cea708CCWidget renderingWidget, android.media.MediaFormat format) {
        super(format);
        mRenderingWidget = renderingWidget;
        mCCParser = new android.media.Cea708CCParser(mRenderingWidget);
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

