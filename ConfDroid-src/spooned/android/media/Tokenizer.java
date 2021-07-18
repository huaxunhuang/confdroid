package android.media;


/**
 *
 *
 * @unknown Tokenizer tokenizes the WebVTT Cue Text into tags and data
 */
class Tokenizer {
    private static final java.lang.String TAG = "Tokenizer";

    private android.media.Tokenizer.TokenizerPhase mPhase;

    private android.media.Tokenizer.TokenizerPhase mDataTokenizer;

    private android.media.Tokenizer.TokenizerPhase mTagTokenizer;

    private android.media.Tokenizer.OnTokenListener mListener;

    private java.lang.String mLine;

    private int mHandledLen;

    interface TokenizerPhase {
        android.media.Tokenizer.TokenizerPhase start();

        void tokenize();
    }

    class DataTokenizer implements android.media.Tokenizer.TokenizerPhase {
        // includes both WebVTT data && escape state
        private java.lang.StringBuilder mData;

        public android.media.Tokenizer.TokenizerPhase start() {
            mData = new java.lang.StringBuilder();
            return this;
        }

        private boolean replaceEscape(java.lang.String escape, java.lang.String replacement, int pos) {
            if (mLine.startsWith(escape, pos)) {
                mData.append(mLine.substring(mHandledLen, pos));
                mData.append(replacement);
                mHandledLen = pos + escape.length();
                pos = mHandledLen - 1;
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void tokenize() {
            int end = mLine.length();
            for (int pos = mHandledLen; pos < mLine.length(); pos++) {
                if (mLine.charAt(pos) == '&') {
                    if (((((replaceEscape("&amp;", "&", pos) || replaceEscape("&lt;", "<", pos)) || replaceEscape("&gt;", ">", pos)) || replaceEscape("&lrm;", "\u200e", pos)) || replaceEscape("&rlm;", "\u200f", pos)) || replaceEscape("&nbsp;", "\u00a0", pos)) {
                        continue;
                    }
                } else
                    if (mLine.charAt(pos) == '<') {
                        end = pos;
                        mPhase = mTagTokenizer.start();
                        break;
                    }

            }
            mData.append(mLine.substring(mHandledLen, end));
            // yield mData
            mListener.onData(mData.toString());
            mData.delete(0, mData.length());
            mHandledLen = end;
        }
    }

    class TagTokenizer implements android.media.Tokenizer.TokenizerPhase {
        private boolean mAtAnnotation;

        private java.lang.String mName;

        private java.lang.String mAnnotation;

        public android.media.Tokenizer.TokenizerPhase start() {
            mName = mAnnotation = "";
            mAtAnnotation = false;
            return this;
        }

        @java.lang.Override
        public void tokenize() {
            if (!mAtAnnotation)
                mHandledLen++;

            if (mHandledLen < mLine.length()) {
                java.lang.String[] parts;
                /**
                 * Collect annotations and end-tags to closing >.  Collect tag
                 * name to closing bracket or next white-space.
                 */
                if (mAtAnnotation || (mLine.charAt(mHandledLen) == '/')) {
                    parts = mLine.substring(mHandledLen).split(">");
                } else {
                    parts = mLine.substring(mHandledLen).split("[\t\f >]");
                }
                java.lang.String part = mLine.substring(mHandledLen, mHandledLen + parts[0].length());
                mHandledLen += parts[0].length();
                if (mAtAnnotation) {
                    mAnnotation += " " + part;
                } else {
                    mName = part;
                }
            }
            mAtAnnotation = true;
            if ((mHandledLen < mLine.length()) && (mLine.charAt(mHandledLen) == '>')) {
                yield_tag();
                mPhase = mDataTokenizer.start();
                mHandledLen++;
            }
        }

        private void yield_tag() {
            if (mName.startsWith("/")) {
                mListener.onEnd(mName.substring(1));
            } else
                if ((mName.length() > 0) && java.lang.Character.isDigit(mName.charAt(0))) {
                    // timestamp
                    try {
                        long timestampMs = android.media.WebVttParser.parseTimestampMs(mName);
                        mListener.onTimeStamp(timestampMs);
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Log.d(android.media.Tokenizer.TAG, ("invalid timestamp tag: <" + mName) + ">");
                    }
                } else {
                    mAnnotation = mAnnotation.replaceAll("\\s+", " ");
                    if (mAnnotation.startsWith(" ")) {
                        mAnnotation = mAnnotation.substring(1);
                    }
                    if (mAnnotation.endsWith(" ")) {
                        mAnnotation = mAnnotation.substring(0, mAnnotation.length() - 1);
                    }
                    java.lang.String[] classes = null;
                    int dotAt = mName.indexOf('.');
                    if (dotAt >= 0) {
                        classes = mName.substring(dotAt + 1).split("\\.");
                        mName = mName.substring(0, dotAt);
                    }
                    mListener.onStart(mName, classes, mAnnotation);
                }

        }
    }

    Tokenizer(android.media.Tokenizer.OnTokenListener listener) {
        mDataTokenizer = new android.media.Tokenizer.DataTokenizer();
        mTagTokenizer = new android.media.Tokenizer.TagTokenizer();
        reset();
        mListener = listener;
    }

    void reset() {
        mPhase = mDataTokenizer.start();
    }

    void tokenize(java.lang.String s) {
        mHandledLen = 0;
        mLine = s;
        while (mHandledLen < mLine.length()) {
            mPhase.tokenize();
        } 
        /* we are finished with a line unless we are in the middle of a tag */
        if (!(mPhase instanceof android.media.Tokenizer.TagTokenizer)) {
            // yield END-OF-LINE
            mListener.onLineEnd();
        }
    }

    interface OnTokenListener {
        void onData(java.lang.String s);

        void onStart(java.lang.String tag, java.lang.String[] classes, java.lang.String annotation);

        void onEnd(java.lang.String tag);

        void onTimeStamp(long timestampMs);

        void onLineEnd();
    }
}

