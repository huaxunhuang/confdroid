package android.media;


class SRTTrack extends android.media.WebVttTrack {
    private static final int MEDIA_TIMED_TEXT = 99;// MediaPlayer.MEDIA_TIMED_TEXT


    private static final int KEY_STRUCT_TEXT = 16;// TimedText.KEY_STRUCT_TEXT


    private static final int KEY_START_TIME = 7;// TimedText.KEY_START_TIME


    private static final int KEY_LOCAL_SETTING = 102;// TimedText.KEY_START_TIME


    private static final java.lang.String TAG = "SRTTrack";

    private final android.os.Handler mEventHandler;

    SRTTrack(android.media.WebVttRenderingWidget renderingWidget, android.media.MediaFormat format) {
        super(renderingWidget, format);
        mEventHandler = null;
    }

    SRTTrack(android.os.Handler eventHandler, android.media.MediaFormat format) {
        super(null, format);
        mEventHandler = eventHandler;
    }

    @java.lang.Override
    protected void onData(android.media.SubtitleData data) {
        try {
            android.media.TextTrackCue cue = new android.media.TextTrackCue();
            cue.mStartTimeMs = data.getStartTimeUs() / 1000;
            cue.mEndTimeMs = (data.getStartTimeUs() + data.getDurationUs()) / 1000;
            java.lang.String paragraph;
            paragraph = new java.lang.String(data.getData(), "UTF-8");
            java.lang.String[] lines = paragraph.split("\\r?\\n");
            cue.mLines = new android.media.TextTrackCueSpan[lines.length][];
            int i = 0;
            for (java.lang.String line : lines) {
                android.media.TextTrackCueSpan[] span = new android.media.TextTrackCueSpan[]{ new android.media.TextTrackCueSpan(line, -1) };
                cue.mLines[i++] = span;
            }
            addCue(cue);
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Log.w(android.media.SRTTrack.TAG, "subtitle data is not UTF-8 encoded: " + e);
        }
    }

    @java.lang.Override
    public void onData(byte[] data, boolean eos, long runID) {
        // TODO make reentrant
        try {
            java.io.Reader r = new java.io.InputStreamReader(new java.io.ByteArrayInputStream(data), "UTF-8");
            java.io.BufferedReader br = new java.io.BufferedReader(r);
            java.lang.String header;
            while ((header = br.readLine()) != null) {
                // discard subtitle number
                header = br.readLine();
                if (header == null) {
                    break;
                }
                android.media.TextTrackCue cue = new android.media.TextTrackCue();
                java.lang.String[] startEnd = header.split("-->");
                cue.mStartTimeMs = android.media.SRTTrack.parseMs(startEnd[0]);
                cue.mEndTimeMs = android.media.SRTTrack.parseMs(startEnd[1]);
                java.lang.String s;
                java.util.List<java.lang.String> paragraph = new java.util.ArrayList<java.lang.String>();
                while (!(((s = br.readLine()) == null) || s.trim().equals(""))) {
                    paragraph.add(s);
                } 
                int i = 0;
                cue.mLines = new android.media.TextTrackCueSpan[paragraph.size()][];
                cue.mStrings = paragraph.toArray(new java.lang.String[0]);
                for (java.lang.String line : paragraph) {
                    android.media.TextTrackCueSpan[] span = new android.media.TextTrackCueSpan[]{ new android.media.TextTrackCueSpan(line, -1) };
                    cue.mStrings[i] = line;
                    cue.mLines[i++] = span;
                }
                addCue(cue);
            } 
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Log.w(android.media.SRTTrack.TAG, "subtitle data is not UTF-8 encoded: " + e);
        } catch (java.io.IOException ioe) {
            // shouldn't happen
            android.util.Log.e(android.media.SRTTrack.TAG, ioe.getMessage(), ioe);
        }
    }

    @java.lang.Override
    public void updateView(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues) {
        if (getRenderingWidget() != null) {
            super.updateView(activeCues);
            return;
        }
        if (mEventHandler == null) {
            return;
        }
        for (android.media.SubtitleTrack.Cue cue : activeCues) {
            android.media.TextTrackCue ttc = ((android.media.TextTrackCue) (cue));
            android.os.Parcel parcel = android.os.Parcel.obtain();
            parcel.writeInt(android.media.SRTTrack.KEY_LOCAL_SETTING);
            parcel.writeInt(android.media.SRTTrack.KEY_START_TIME);
            parcel.writeInt(((int) (cue.mStartTimeMs)));
            parcel.writeInt(android.media.SRTTrack.KEY_STRUCT_TEXT);
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (java.lang.String line : ttc.mStrings) {
                sb.append(line).append('\n');
            }
            byte[] buf = sb.toString().getBytes();
            parcel.writeInt(buf.length);
            parcel.writeByteArray(buf);
            android.os.Message msg = /* arg1 */
            /* arg2 */
            mEventHandler.obtainMessage(android.media.SRTTrack.MEDIA_TIMED_TEXT, 0, 0, parcel);
            mEventHandler.sendMessage(msg);
        }
        activeCues.clear();
    }

    private static long parseMs(java.lang.String in) {
        long hours = java.lang.Long.parseLong(in.split(":")[0].trim());
        long minutes = java.lang.Long.parseLong(in.split(":")[1].trim());
        long seconds = java.lang.Long.parseLong(in.split(":")[2].split(",")[0].trim());
        long millies = java.lang.Long.parseLong(in.split(":")[2].split(",")[1].trim());
        return (((((hours * 60) * 60) * 1000) + ((minutes * 60) * 1000)) + (seconds * 1000)) + millies;
    }
}

