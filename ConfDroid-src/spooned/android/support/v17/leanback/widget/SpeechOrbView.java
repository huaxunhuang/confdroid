package android.support.v17.leanback.widget;


/**
 * A subclass of {@link SearchOrbView} that visualizes the state of an ongoing speech recognition.
 */
public class SpeechOrbView extends android.support.v17.leanback.widget.SearchOrbView {
    private final float mSoundLevelMaxZoom;

    private final android.support.v17.leanback.widget.SearchOrbView.Colors mListeningOrbColors;

    private final android.support.v17.leanback.widget.SearchOrbView.Colors mNotListeningOrbColors;

    private int mCurrentLevel = 0;

    private boolean mListening = false;

    public SpeechOrbView(android.content.Context context) {
        this(context, null);
    }

    public SpeechOrbView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeechOrbView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.content.res.Resources resources = context.getResources();
        mSoundLevelMaxZoom = resources.getFraction(R.fraction.lb_search_bar_speech_orb_max_level_zoom, 1, 1);
        mNotListeningOrbColors = new android.support.v17.leanback.widget.SearchOrbView.Colors(resources.getColor(R.color.lb_speech_orb_not_recording), resources.getColor(R.color.lb_speech_orb_not_recording_pulsed), resources.getColor(R.color.lb_speech_orb_not_recording_icon));
        mListeningOrbColors = new android.support.v17.leanback.widget.SearchOrbView.Colors(resources.getColor(R.color.lb_speech_orb_recording), resources.getColor(R.color.lb_speech_orb_recording), android.graphics.Color.TRANSPARENT);
        showNotListening();
    }

    @java.lang.Override
    int getLayoutResourceId() {
        return R.layout.lb_speech_orb;
    }

    /**
     * Sets the view to display listening state.
     */
    public void showListening() {
        setOrbColors(mListeningOrbColors);
        setOrbIcon(getResources().getDrawable(R.drawable.lb_ic_search_mic));
        // Assume focused
        animateOnFocus(true);
        enableOrbColorAnimation(false);
        scaleOrbViewOnly(1.0F);
        mCurrentLevel = 0;
        mListening = true;
    }

    /**
     * Sets the view to display the not-listening state.
     */
    public void showNotListening() {
        setOrbColors(mNotListeningOrbColors);
        setOrbIcon(getResources().getDrawable(R.drawable.lb_ic_search_mic_out));
        animateOnFocus(hasFocus());
        scaleOrbViewOnly(1.0F);
        mListening = false;
    }

    /**
     * Sets the sound level while listening to speech.
     */
    public void setSoundLevel(int level) {
        if (!mListening)
            return;

        // Either ease towards the target level, or decay away from it depending on whether
        // its higher or lower than the current.
        if (level > mCurrentLevel) {
            mCurrentLevel = mCurrentLevel + ((level - mCurrentLevel) / 2);
        } else {
            mCurrentLevel = ((int) (mCurrentLevel * 0.7F));
        }
        float zoom = 1.0F + (((mSoundLevelMaxZoom - getFocusedZoom()) * mCurrentLevel) / 100);
        scaleOrbViewOnly(zoom);
    }
}

