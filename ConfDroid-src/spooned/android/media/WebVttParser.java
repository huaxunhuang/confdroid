package android.media;


/**
 * Supporting July 10 2013 draft version
 *
 * @unknown 
 */
class WebVttParser {
    private static final java.lang.String TAG = "WebVttParser";

    private android.media.WebVttParser.Phase mPhase;

    private android.media.TextTrackCue mCue;

    private java.util.Vector<java.lang.String> mCueTexts;

    private android.media.WebVttCueListener mListener;

    private java.lang.String mBuffer;

    WebVttParser(android.media.WebVttCueListener listener) {
        mPhase = mParseStart;
        mBuffer = "";/* mBuffer contains up to 1 incomplete line */

        mListener = listener;
        mCueTexts = new java.util.Vector<java.lang.String>();
    }

    /* parsePercentageString */
    public static float parseFloatPercentage(java.lang.String s) throws java.lang.NumberFormatException {
        if (!s.endsWith("%")) {
            throw new java.lang.NumberFormatException("does not end in %");
        }
        s = s.substring(0, s.length() - 1);
        // parseFloat allows an exponent or a sign
        if (s.matches(".*[^0-9.].*")) {
            throw new java.lang.NumberFormatException("contains an invalid character");
        }
        try {
            float value = java.lang.Float.parseFloat(s);
            if ((value < 0.0F) || (value > 100.0F)) {
                throw new java.lang.NumberFormatException("is out of range");
            }
            return value;
        } catch (java.lang.NumberFormatException e) {
            throw new java.lang.NumberFormatException("is not a number");
        }
    }

    public static int parseIntPercentage(java.lang.String s) throws java.lang.NumberFormatException {
        if (!s.endsWith("%")) {
            throw new java.lang.NumberFormatException("does not end in %");
        }
        s = s.substring(0, s.length() - 1);
        // parseInt allows "-0" that returns 0, so check for non-digits
        if (s.matches(".*[^0-9].*")) {
            throw new java.lang.NumberFormatException("contains an invalid character");
        }
        try {
            int value = java.lang.Integer.parseInt(s);
            if ((value < 0) || (value > 100)) {
                throw new java.lang.NumberFormatException("is out of range");
            }
            return value;
        } catch (java.lang.NumberFormatException e) {
            throw new java.lang.NumberFormatException("is not a number");
        }
    }

    public static long parseTimestampMs(java.lang.String s) throws java.lang.NumberFormatException {
        if (!s.matches("(\\d+:)?[0-5]\\d:[0-5]\\d\\.\\d{3}")) {
            throw new java.lang.NumberFormatException("has invalid format");
        }
        java.lang.String[] parts = s.split("\\.", 2);
        long value = 0;
        for (java.lang.String group : parts[0].split(":")) {
            value = (value * 60) + java.lang.Long.parseLong(group);
        }
        return (value * 1000) + java.lang.Long.parseLong(parts[1]);
    }

    public static java.lang.String timeToString(long timeMs) {
        return java.lang.String.format("%d:%02d:%02d.%03d", timeMs / 3600000, (timeMs / 60000) % 60, (timeMs / 1000) % 60, timeMs % 1000);
    }

    public void parse(java.lang.String s) {
        boolean trailingCR = false;
        mBuffer = (mBuffer + s.replace("\u0000", "\ufffd")).replace("\r\n", "\n");
        /* keep trailing '\r' in case matching '\n' arrives in next packet */
        if (mBuffer.endsWith("\r")) {
            trailingCR = true;
            mBuffer = mBuffer.substring(0, mBuffer.length() - 1);
        }
        java.lang.String[] lines = mBuffer.split("[\r\n]");
        for (int i = 0; i < (lines.length - 1); i++) {
            mPhase.parse(lines[i]);
        }
        mBuffer = lines[lines.length - 1];
        if (trailingCR)
            mBuffer += "\r";

    }

    public void eos() {
        if (mBuffer.endsWith("\r")) {
            mBuffer = mBuffer.substring(0, mBuffer.length() - 1);
        }
        mPhase.parse(mBuffer);
        mBuffer = "";
        yieldCue();
        mPhase = mParseStart;
    }

    public void yieldCue() {
        if ((mCue != null) && (mCueTexts.size() > 0)) {
            mCue.mStrings = new java.lang.String[mCueTexts.size()];
            mCueTexts.toArray(mCue.mStrings);
            mCueTexts.clear();
            mListener.onCueParsed(mCue);
        }
        mCue = null;
    }

    interface Phase {
        void parse(java.lang.String line);
    }

    private final android.media.WebVttParser.Phase mSkipRest = new android.media.WebVttParser.Phase() {
        @java.lang.Override
        public void parse(java.lang.String line) {
        }
    };

    private final android.media.WebVttParser.Phase mParseStart = new android.media.WebVttParser.Phase() {
        // 5-9
        @java.lang.Override
        public void parse(java.lang.String line) {
            if (line.startsWith("\ufeff")) {
                line = line.substring(1);
            }
            if (((!line.equals("WEBVTT")) && (!line.startsWith("WEBVTT "))) && (!line.startsWith("WEBVTT\t"))) {
                log_warning("Not a WEBVTT header", line);
                mPhase = mSkipRest;
            } else {
                mPhase = mParseHeader;
            }
        }
    };

    private final android.media.WebVttParser.Phase mParseHeader = new android.media.WebVttParser.Phase() {
        // 10-13
        android.media.TextTrackRegion parseRegion(java.lang.String s) {
            android.media.TextTrackRegion region = new android.media.TextTrackRegion();
            for (java.lang.String setting : s.split(" +")) {
                int equalAt = setting.indexOf('=');
                if ((equalAt <= 0) || (equalAt == (setting.length() - 1))) {
                    continue;
                }
                java.lang.String name = setting.substring(0, equalAt);
                java.lang.String value = setting.substring(equalAt + 1);
                if (name.equals("id")) {
                    region.mId = value;
                } else
                    if (name.equals("width")) {
                        try {
                            region.mWidth = android.media.WebVttParser.parseFloatPercentage(value);
                        } catch (java.lang.NumberFormatException e) {
                            log_warning("region setting", name, "has invalid value", e.getMessage(), value);
                        }
                    } else
                        if (name.equals("lines")) {
                            if (value.matches(".*[^0-9].*")) {
                                log_warning("lines", name, "contains an invalid character", value);
                            } else {
                                try {
                                    region.mLines = java.lang.Integer.parseInt(value);
                                    assert region.mLines >= 0;// lines contains only digits

                                } catch (java.lang.NumberFormatException e) {
                                    log_warning("region setting", name, "is not numeric", value);
                                }
                            }
                        } else
                            if (name.equals("regionanchor") || name.equals("viewportanchor")) {
                                int commaAt = value.indexOf(",");
                                if (commaAt < 0) {
                                    log_warning("region setting", name, "contains no comma", value);
                                    continue;
                                }
                                java.lang.String anchorX = value.substring(0, commaAt);
                                java.lang.String anchorY = value.substring(commaAt + 1);
                                float x;
                                float y;
                                try {
                                    x = android.media.WebVttParser.parseFloatPercentage(anchorX);
                                } catch (java.lang.NumberFormatException e) {
                                    log_warning("region setting", name, "has invalid x component", e.getMessage(), anchorX);
                                    continue;
                                }
                                try {
                                    y = android.media.WebVttParser.parseFloatPercentage(anchorY);
                                } catch (java.lang.NumberFormatException e) {
                                    log_warning("region setting", name, "has invalid y component", e.getMessage(), anchorY);
                                    continue;
                                }
                                if (name.charAt(0) == 'r') {
                                    region.mAnchorPointX = x;
                                    region.mAnchorPointY = y;
                                } else {
                                    region.mViewportAnchorPointX = x;
                                    region.mViewportAnchorPointY = y;
                                }
                            } else
                                if (name.equals("scroll")) {
                                    if (value.equals("up")) {
                                        region.mScrollValue = android.media.TextTrackRegion.SCROLL_VALUE_SCROLL_UP;
                                    } else {
                                        log_warning("region setting", name, "has invalid value", value);
                                    }
                                }




            }
            return region;
        }

        @java.lang.Override
        public void parse(java.lang.String line) {
            if (line.length() == 0) {
                mPhase = mParseCueId;
            } else
                if (line.contains("-->")) {
                    mPhase = mParseCueTime;
                    mPhase.parse(line);
                } else {
                    int colonAt = line.indexOf(':');
                    if ((colonAt <= 0) || (colonAt >= (line.length() - 1))) {
                        log_warning("meta data header has invalid format", line);
                    }
                    java.lang.String name = line.substring(0, colonAt);
                    java.lang.String value = line.substring(colonAt + 1);
                    if (name.equals("Region")) {
                        android.media.TextTrackRegion region = parseRegion(value);
                        mListener.onRegionParsed(region);
                    }
                }

        }
    };

    private final android.media.WebVttParser.Phase mParseCueId = new android.media.WebVttParser.Phase() {
        @java.lang.Override
        public void parse(java.lang.String line) {
            if (line.length() == 0) {
                return;
            }
            assert mCue == null;
            if (line.equals("NOTE") || line.startsWith("NOTE ")) {
                mPhase = mParseCueText;
            }
            mCue = new android.media.TextTrackCue();
            mCueTexts.clear();
            mPhase = mParseCueTime;
            if (line.contains("-->")) {
                mPhase.parse(line);
            } else {
                mCue.mId = line;
            }
        }
    };

    private final android.media.WebVttParser.Phase mParseCueTime = new android.media.WebVttParser.Phase() {
        @java.lang.Override
        public void parse(java.lang.String line) {
            int arrowAt = line.indexOf("-->");
            if (arrowAt < 0) {
                mCue = null;
                mPhase = mParseCueId;
                return;
            }
            java.lang.String start = line.substring(0, arrowAt).trim();
            // convert only initial and first other white-space to space
            java.lang.String rest = line.substring(arrowAt + 3).replaceFirst("^\\s+", "").replaceFirst("\\s+", " ");
            int spaceAt = rest.indexOf(' ');
            java.lang.String end = (spaceAt > 0) ? rest.substring(0, spaceAt) : rest;
            rest = (spaceAt > 0) ? rest.substring(spaceAt + 1) : "";
            mCue.mStartTimeMs = android.media.WebVttParser.parseTimestampMs(start);
            mCue.mEndTimeMs = android.media.WebVttParser.parseTimestampMs(end);
            for (java.lang.String setting : rest.split(" +")) {
                int colonAt = setting.indexOf(':');
                if ((colonAt <= 0) || (colonAt == (setting.length() - 1))) {
                    continue;
                }
                java.lang.String name = setting.substring(0, colonAt);
                java.lang.String value = setting.substring(colonAt + 1);
                if (name.equals("region")) {
                    mCue.mRegionId = value;
                } else
                    if (name.equals("vertical")) {
                        if (value.equals("rl")) {
                            mCue.mWritingDirection = android.media.TextTrackCue.WRITING_DIRECTION_VERTICAL_RL;
                        } else
                            if (value.equals("lr")) {
                                mCue.mWritingDirection = android.media.TextTrackCue.WRITING_DIRECTION_VERTICAL_LR;
                            } else {
                                log_warning("cue setting", name, "has invalid value", value);
                            }

                    } else
                        if (name.equals("line")) {
                            try {
                                /* TRICKY: we know that there are no spaces in value */
                                assert value.indexOf(' ') < 0;
                                if (value.endsWith("%")) {
                                    mCue.mSnapToLines = false;
                                    mCue.mLinePosition = android.media.WebVttParser.parseIntPercentage(value);
                                } else
                                    if (value.matches(".*[^0-9].*")) {
                                        log_warning("cue setting", name, "contains an invalid character", value);
                                    } else {
                                        mCue.mSnapToLines = true;
                                        mCue.mLinePosition = java.lang.Integer.parseInt(value);
                                    }

                            } catch (java.lang.NumberFormatException e) {
                                log_warning("cue setting", name, "is not numeric or percentage", value);
                            }
                            // TODO: add support for optional alignment value [,start|middle|end]
                        } else
                            if (name.equals("position")) {
                                try {
                                    mCue.mTextPosition = android.media.WebVttParser.parseIntPercentage(value);
                                } catch (java.lang.NumberFormatException e) {
                                    log_warning("cue setting", name, "is not numeric or percentage", value);
                                }
                            } else
                                if (name.equals("size")) {
                                    try {
                                        mCue.mSize = android.media.WebVttParser.parseIntPercentage(value);
                                    } catch (java.lang.NumberFormatException e) {
                                        log_warning("cue setting", name, "is not numeric or percentage", value);
                                    }
                                } else
                                    if (name.equals("align")) {
                                        if (value.equals("start")) {
                                            mCue.mAlignment = android.media.TextTrackCue.ALIGNMENT_START;
                                        } else
                                            if (value.equals("middle")) {
                                                mCue.mAlignment = android.media.TextTrackCue.ALIGNMENT_MIDDLE;
                                            } else
                                                if (value.equals("end")) {
                                                    mCue.mAlignment = android.media.TextTrackCue.ALIGNMENT_END;
                                                } else
                                                    if (value.equals("left")) {
                                                        mCue.mAlignment = android.media.TextTrackCue.ALIGNMENT_LEFT;
                                                    } else
                                                        if (value.equals("right")) {
                                                            mCue.mAlignment = android.media.TextTrackCue.ALIGNMENT_RIGHT;
                                                        } else {
                                                            log_warning("cue setting", name, "has invalid value", value);
                                                            continue;
                                                        }




                                    }





            }
            if (((mCue.mLinePosition != null) || (mCue.mSize != 100)) || (mCue.mWritingDirection != android.media.TextTrackCue.WRITING_DIRECTION_HORIZONTAL)) {
                mCue.mRegionId = "";
            }
            mPhase = mParseCueText;
        }
    };

    /* also used for notes */
    private final android.media.WebVttParser.Phase mParseCueText = new android.media.WebVttParser.Phase() {
        @java.lang.Override
        public void parse(java.lang.String line) {
            if (line.length() == 0) {
                yieldCue();
                mPhase = mParseCueId;
                return;
            } else
                if (mCue != null) {
                    mCueTexts.add(line);
                }

        }
    };

    private void log_warning(java.lang.String nameType, java.lang.String name, java.lang.String message, java.lang.String subMessage, java.lang.String value) {
        android.util.Log.w(this.getClass().getName(), ((((((((nameType + " '") + name) + "' ") + message) + " ('") + value) + "' ") + subMessage) + ")");
    }

    private void log_warning(java.lang.String nameType, java.lang.String name, java.lang.String message, java.lang.String value) {
        android.util.Log.w(this.getClass().getName(), ((((((nameType + " '") + name) + "' ") + message) + " ('") + value) + "')");
    }

    private void log_warning(java.lang.String message, java.lang.String value) {
        android.util.Log.w(this.getClass().getName(), ((message + " ('") + value) + "')");
    }
}

