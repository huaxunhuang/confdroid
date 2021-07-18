/**
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.media;


/**
 * The subtitle controller provides the architecture to display subtitles for a
 * media source.  It allows specifying which tracks to display, on which anchor
 * to display them, and also allows adding external, out-of-band subtitle tracks.
 *
 * @unknown 
 */
public class SubtitleController {
    private android.media.MediaTimeProvider mTimeProvider;

    private java.util.Vector<android.media.SubtitleController.Renderer> mRenderers;

    private java.util.Vector<android.media.SubtitleTrack> mTracks;

    private android.media.SubtitleTrack mSelectedTrack;

    private boolean mShowing;

    private android.view.accessibility.CaptioningManager mCaptioningManager;

    private android.os.Handler mHandler;

    private static final int WHAT_SHOW = 1;

    private static final int WHAT_HIDE = 2;

    private static final int WHAT_SELECT_TRACK = 3;

    private static final int WHAT_SELECT_DEFAULT_TRACK = 4;

    private final android.os.Handler.Callback mCallback = new android.os.Handler.Callback() {
        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.media.SubtitleController.WHAT_SHOW :
                    doShow();
                    return true;
                case android.media.SubtitleController.WHAT_HIDE :
                    doHide();
                    return true;
                case android.media.SubtitleController.WHAT_SELECT_TRACK :
                    doSelectTrack(((android.media.SubtitleTrack) (msg.obj)));
                    return true;
                case android.media.SubtitleController.WHAT_SELECT_DEFAULT_TRACK :
                    doSelectDefaultTrack();
                    return true;
                default :
                    return false;
            }
        }
    };

    private android.view.accessibility.CaptioningManager.CaptioningChangeListener mCaptioningChangeListener = new android.view.accessibility.CaptioningManager.CaptioningChangeListener() {
        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onEnabledChanged(boolean enabled) {
            selectDefaultTrack();
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onLocaleChanged(java.util.Locale locale) {
            selectDefaultTrack();
        }
    };

    /**
     * Creates a subtitle controller for a media playback object that implements
     * the MediaTimeProvider interface.
     *
     * @param timeProvider
     * 		
     */
    public SubtitleController(android.content.Context context, android.media.MediaTimeProvider timeProvider, android.media.SubtitleController.Listener listener) {
        mTimeProvider = timeProvider;
        mListener = listener;
        mRenderers = new java.util.Vector<android.media.SubtitleController.Renderer>();
        mShowing = false;
        mTracks = new java.util.Vector<android.media.SubtitleTrack>();
        mCaptioningManager = ((android.view.accessibility.CaptioningManager) (context.getSystemService(android.content.Context.CAPTIONING_SERVICE)));
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        mCaptioningManager.removeCaptioningChangeListener(mCaptioningChangeListener);
        super.finalize();
    }

    /**
     *
     *
     * @return the available subtitle tracks for this media. These include
    the tracks found by {@link MediaPlayer} as well as any tracks added
    manually via {@link #addTrack}.
     */
    public android.media.SubtitleTrack[] getTracks() {
        synchronized(mTracks) {
            android.media.SubtitleTrack[] tracks = new android.media.SubtitleTrack[mTracks.size()];
            mTracks.toArray(tracks);
            return tracks;
        }
    }

    /**
     *
     *
     * @return the currently selected subtitle track
     */
    public android.media.SubtitleTrack getSelectedTrack() {
        return mSelectedTrack;
    }

    private android.media.SubtitleTrack.RenderingWidget getRenderingWidget() {
        if (mSelectedTrack == null) {
            return null;
        }
        return mSelectedTrack.getRenderingWidget();
    }

    /**
     * Selects a subtitle track.  As a result, this track will receive
     * in-band data from the {@link MediaPlayer}.  However, this does
     * not change the subtitle visibility.
     *
     * Should be called from the anchor's (UI) thread. {@see #Anchor.getSubtitleLooper}
     *
     * @param track
     * 		The subtitle track to select.  This must be one of the
     * 		tracks in {@link #getTracks}.
     * @return true if the track was successfully selected.
     */
    public boolean selectTrack(android.media.SubtitleTrack track) {
        if ((track != null) && (!mTracks.contains(track))) {
            return false;
        }
        processOnAnchor(mHandler.obtainMessage(android.media.SubtitleController.WHAT_SELECT_TRACK, track));
        return true;
    }

    private void doSelectTrack(android.media.SubtitleTrack track) {
        mTrackIsExplicit = true;
        if (mSelectedTrack == track) {
            return;
        }
        if (mSelectedTrack != null) {
            mSelectedTrack.hide();
            mSelectedTrack.setTimeProvider(null);
        }
        mSelectedTrack = track;
        if (mAnchor != null) {
            mAnchor.setSubtitleWidget(getRenderingWidget());
        }
        if (mSelectedTrack != null) {
            mSelectedTrack.setTimeProvider(mTimeProvider);
            mSelectedTrack.show();
        }
        if (mListener != null) {
            mListener.onSubtitleTrackSelected(track);
        }
    }

    /**
     *
     *
     * @return the default subtitle track based on system preferences, or null,
    if no such track exists in this manager.

    Supports HLS-flags: AUTOSELECT, FORCED & DEFAULT.

    1. If captioning is disabled, only consider FORCED tracks. Otherwise,
    consider all tracks, but prefer non-FORCED ones.
    2. If user selected "Default" caption language:
    a. If there is a considered track with DEFAULT=yes, returns that track
    (favor the first one in the current language if there are more than
    one default tracks, or the first in general if none of them are in
    the current language).
    b. Otherwise, if there is a track with AUTOSELECT=yes in the current
    language, return that one.
    c. If there are no default tracks, and no autoselectable tracks in the
    current language, return null.
    3. If there is a track with the caption language, select that one.  Prefer
    the one with AUTOSELECT=no.

    The default values for these flags are DEFAULT=no, AUTOSELECT=yes
    and FORCED=no.
     */
    public android.media.SubtitleTrack getDefaultTrack() {
        android.media.SubtitleTrack bestTrack = null;
        int bestScore = -1;
        java.util.Locale selectedLocale = mCaptioningManager.getLocale();
        java.util.Locale locale = selectedLocale;
        if (locale == null) {
            locale = java.util.Locale.getDefault();
        }
        boolean selectForced = !mCaptioningManager.isEnabled();
        synchronized(mTracks) {
            for (android.media.SubtitleTrack track : mTracks) {
                android.media.MediaFormat format = track.getFormat();
                java.lang.String language = format.getString(android.media.MediaFormat.KEY_LANGUAGE);
                boolean forced = format.getInteger(android.media.MediaFormat.KEY_IS_FORCED_SUBTITLE, 0) != 0;
                boolean autoselect = format.getInteger(android.media.MediaFormat.KEY_IS_AUTOSELECT, 1) != 0;
                boolean is_default = format.getInteger(android.media.MediaFormat.KEY_IS_DEFAULT, 0) != 0;
                boolean languageMatches = (((locale == null) || locale.getLanguage().equals("")) || locale.getISO3Language().equals(language)) || locale.getLanguage().equals(language);
                // is_default is meaningless unless caption language is 'default'
                int score = (((forced ? 0 : 8) + ((selectedLocale == null) && is_default ? 4 : 0)) + (autoselect ? 0 : 2)) + (languageMatches ? 1 : 0);
                if (selectForced && (!forced)) {
                    continue;
                }
                // we treat null locale/language as matching any language
                if (((selectedLocale == null) && is_default) || (languageMatches && ((autoselect || forced) || (selectedLocale != null)))) {
                    if (score > bestScore) {
                        bestScore = score;
                        bestTrack = track;
                    }
                }
            }
        }
        return bestTrack;
    }

    private boolean mTrackIsExplicit = false;

    private boolean mVisibilityIsExplicit = false;

    /**
     *
     *
     * @unknown - should be called from anchor thread
     */
    public void selectDefaultTrack() {
        processOnAnchor(mHandler.obtainMessage(android.media.SubtitleController.WHAT_SELECT_DEFAULT_TRACK));
    }

    private void doSelectDefaultTrack() {
        if (mTrackIsExplicit) {
            // If track selection is explicit, but visibility
            // is not, it falls back to the captioning setting
            if (!mVisibilityIsExplicit) {
                if (mCaptioningManager.isEnabled() || ((mSelectedTrack != null) && (mSelectedTrack.getFormat().getInteger(android.media.MediaFormat.KEY_IS_FORCED_SUBTITLE, 0) != 0))) {
                    show();
                } else
                    if ((mSelectedTrack != null) && (mSelectedTrack.getTrackType() == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE)) {
                        hide();
                    }

                mVisibilityIsExplicit = false;
            }
            return;
        }
        // We can have a default (forced) track even if captioning
        // is not enabled.  This is handled by getDefaultTrack().
        // Show this track unless subtitles were explicitly hidden.
        android.media.SubtitleTrack track = getDefaultTrack();
        if (track != null) {
            selectTrack(track);
            mTrackIsExplicit = false;
            if (!mVisibilityIsExplicit) {
                show();
                mVisibilityIsExplicit = false;
            }
        }
    }

    /**
     *
     *
     * @unknown - must be called from anchor thread
     */
    public void reset() {
        checkAnchorLooper();
        hide();
        selectTrack(null);
        mTracks.clear();
        mTrackIsExplicit = false;
        mVisibilityIsExplicit = false;
        mCaptioningManager.removeCaptioningChangeListener(mCaptioningChangeListener);
    }

    /**
     * Adds a new, external subtitle track to the manager.
     *
     * @param format
     * 		the format of the track that will include at least
     * 		the MIME type {@link MediaFormat@KEY_MIME}.
     * @return the created {@link SubtitleTrack} object
     */
    public android.media.SubtitleTrack addTrack(android.media.MediaFormat format) {
        synchronized(mRenderers) {
            for (android.media.SubtitleController.Renderer renderer : mRenderers) {
                if (renderer.supports(format)) {
                    android.media.SubtitleTrack track = renderer.createTrack(format);
                    if (track != null) {
                        synchronized(mTracks) {
                            if (mTracks.size() == 0) {
                                mCaptioningManager.addCaptioningChangeListener(mCaptioningChangeListener);
                            }
                            mTracks.add(track);
                        }
                        return track;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Show the selected (or default) subtitle track.
     *
     * Should be called from the anchor's (UI) thread. {@see #Anchor.getSubtitleLooper}
     */
    public void show() {
        processOnAnchor(mHandler.obtainMessage(android.media.SubtitleController.WHAT_SHOW));
    }

    private void doShow() {
        mShowing = true;
        mVisibilityIsExplicit = true;
        if (mSelectedTrack != null) {
            mSelectedTrack.show();
        }
    }

    /**
     * Hide the selected (or default) subtitle track.
     *
     * Should be called from the anchor's (UI) thread. {@see #Anchor.getSubtitleLooper}
     */
    public void hide() {
        processOnAnchor(mHandler.obtainMessage(android.media.SubtitleController.WHAT_HIDE));
    }

    private void doHide() {
        mVisibilityIsExplicit = true;
        if (mSelectedTrack != null) {
            mSelectedTrack.hide();
        }
        mShowing = false;
    }

    /**
     * Interface for supporting a single or multiple subtitle types in {@link MediaPlayer}.
     */
    public static abstract class Renderer {
        /**
         * Called by {@link MediaPlayer}'s {@link SubtitleController} when a new
         * subtitle track is detected, to see if it should use this object to
         * parse and display this subtitle track.
         *
         * @param format
         * 		the format of the track that will include at least
         * 		the MIME type {@link MediaFormat@KEY_MIME}.
         * @return true if and only if the track format is supported by this
        renderer
         */
        public abstract boolean supports(android.media.MediaFormat format);

        /**
         * Called by {@link MediaPlayer}'s {@link SubtitleController} for each
         * subtitle track that was detected and is supported by this object to
         * create a {@link SubtitleTrack} object.  This object will be created
         * for each track that was found.  If the track is selected for display,
         * this object will be used to parse and display the track data.
         *
         * @param format
         * 		the format of the track that will include at least
         * 		the MIME type {@link MediaFormat@KEY_MIME}.
         * @return a {@link SubtitleTrack} object that will be used to parse
        and render the subtitle track.
         */
        public abstract android.media.SubtitleTrack createTrack(android.media.MediaFormat format);
    }

    /**
     * Add support for a subtitle format in {@link MediaPlayer}.
     *
     * @param renderer
     * 		a {@link SubtitleController.Renderer} object that adds
     * 		support for a subtitle format.
     */
    public void registerRenderer(android.media.SubtitleController.Renderer renderer) {
        synchronized(mRenderers) {
            // TODO how to get available renderers in the system
            if (!mRenderers.contains(renderer)) {
                // TODO should added renderers override existing ones (to allow replacing?)
                mRenderers.add(renderer);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasRendererFor(android.media.MediaFormat format) {
        synchronized(mRenderers) {
            // TODO how to get available renderers in the system
            for (android.media.SubtitleController.Renderer renderer : mRenderers) {
                if (renderer.supports(format)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Subtitle anchor, an object that is able to display a subtitle renderer,
     * e.g. a VideoView.
     */
    public interface Anchor {
        /**
         * Anchor should use the supplied subtitle rendering widget, or
         * none if it is null.
         *
         * @unknown 
         */
        public void setSubtitleWidget(android.media.SubtitleTrack.RenderingWidget subtitleWidget);

        /**
         * Anchors provide the looper on which all track visibility changes
         * (track.show/hide, setSubtitleWidget) will take place.
         *
         * @unknown 
         */
        public android.os.Looper getSubtitleLooper();
    }

    private android.media.SubtitleController.Anchor mAnchor;

    /**
     *
     *
     * @unknown - called from anchor's looper (if any, both when unsetting and
    setting)
     */
    public void setAnchor(android.media.SubtitleController.Anchor anchor) {
        if (mAnchor == anchor) {
            return;
        }
        if (mAnchor != null) {
            checkAnchorLooper();
            mAnchor.setSubtitleWidget(null);
        }
        mAnchor = anchor;
        mHandler = null;
        if (mAnchor != null) {
            mHandler = new android.os.Handler(mAnchor.getSubtitleLooper(), mCallback);
            checkAnchorLooper();
            mAnchor.setSubtitleWidget(getRenderingWidget());
        }
    }

    private void checkAnchorLooper() {
        assert mHandler != null : "Should have a looper already";
        assert android.os.Looper.myLooper() == mHandler.getLooper() : "Must be called from the anchor's looper";
    }

    private void processOnAnchor(android.os.Message m) {
        assert mHandler != null : "Should have a looper already";
        if (android.os.Looper.myLooper() == mHandler.getLooper()) {
            mHandler.dispatchMessage(m);
        } else {
            mHandler.sendMessage(m);
        }
    }

    public interface Listener {
        /**
         * Called when a subtitle track has been selected.
         *
         * @param track
         * 		selected subtitle track or null
         * @unknown 
         */
        public void onSubtitleTrackSelected(android.media.SubtitleTrack track);
    }

    private android.media.SubtitleController.Listener mListener;
}

