package android.media;


/**
 *
 *
 * @unknown 
 */
interface WebVttCueListener {
    void onCueParsed(android.media.TextTrackCue cue);

    void onRegionParsed(android.media.TextTrackRegion region);
}

