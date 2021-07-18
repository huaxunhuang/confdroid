package android.media;


/**
 *
 *
 * @unknown CCParser processes CEA-608 closed caption data.

It calls back into OnDisplayChangedListener upon
display change with styled text for rendering.
 */
class Cea608CCParser {
    public static final int MAX_ROWS = 15;

    public static final int MAX_COLS = 32;

    private static final java.lang.String TAG = "Cea608CCParser";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.media.Cea608CCParser.TAG, android.util.Log.DEBUG);

    private static final int INVALID = -1;

    // EIA-CEA-608: Table 70 - Control Codes
    private static final int RCL = 0x20;

    private static final int BS = 0x21;

    private static final int AOF = 0x22;

    private static final int AON = 0x23;

    private static final int DER = 0x24;

    private static final int RU2 = 0x25;

    private static final int RU3 = 0x26;

    private static final int RU4 = 0x27;

    private static final int FON = 0x28;

    private static final int RDC = 0x29;

    private static final int TR = 0x2a;

    private static final int RTD = 0x2b;

    private static final int EDM = 0x2c;

    private static final int CR = 0x2d;

    private static final int ENM = 0x2e;

    private static final int EOC = 0x2f;

    // Transparent Space
    private static final char TS = '\u00a0';

    // Captioning Modes
    private static final int MODE_UNKNOWN = 0;

    private static final int MODE_PAINT_ON = 1;

    private static final int MODE_ROLL_UP = 2;

    private static final int MODE_POP_ON = 3;

    private static final int MODE_TEXT = 4;

    private final android.media.Cea608CCParser.DisplayListener mListener;

    private int mMode = android.media.Cea608CCParser.MODE_PAINT_ON;

    private int mRollUpSize = 4;

    private int mPrevCtrlCode = android.media.Cea608CCParser.INVALID;

    private android.media.Cea608CCParser.CCMemory mDisplay = new android.media.Cea608CCParser.CCMemory();

    private android.media.Cea608CCParser.CCMemory mNonDisplay = new android.media.Cea608CCParser.CCMemory();

    private android.media.Cea608CCParser.CCMemory mTextMem = new android.media.Cea608CCParser.CCMemory();

    Cea608CCParser(android.media.Cea608CCParser.DisplayListener listener) {
        mListener = listener;
    }

    public void parse(byte[] data) {
        android.media.Cea608CCParser.CCData[] ccData = android.media.Cea608CCParser.CCData.fromByteArray(data);
        for (int i = 0; i < ccData.length; i++) {
            if (android.media.Cea608CCParser.DEBUG) {
                android.util.Log.d(android.media.Cea608CCParser.TAG, ccData[i].toString());
            }
            if (((handleCtrlCode(ccData[i]) || handleTabOffsets(ccData[i])) || handlePACCode(ccData[i])) || handleMidRowCode(ccData[i])) {
                continue;
            }
            handleDisplayableChars(ccData[i]);
        }
    }

    interface DisplayListener {
        void onDisplayChanged(android.text.SpannableStringBuilder[] styledTexts);

        android.view.accessibility.CaptioningManager.CaptionStyle getCaptionStyle();
    }

    private android.media.Cea608CCParser.CCMemory getMemory() {
        // get the CC memory to operate on for current mode
        switch (mMode) {
            case android.media.Cea608CCParser.MODE_POP_ON :
                return mNonDisplay;
            case android.media.Cea608CCParser.MODE_TEXT :
                // TODO(chz): support only caption mode for now,
                // in text mode, dump everything to text mem.
                return mTextMem;
            case android.media.Cea608CCParser.MODE_PAINT_ON :
            case android.media.Cea608CCParser.MODE_ROLL_UP :
                return mDisplay;
            default :
                android.util.Log.w(android.media.Cea608CCParser.TAG, "unrecoginized mode: " + mMode);
        }
        return mDisplay;
    }

    private boolean handleDisplayableChars(android.media.Cea608CCParser.CCData ccData) {
        if (!ccData.isDisplayableChar()) {
            return false;
        }
        // Extended char includes 1 automatic backspace
        if (ccData.isExtendedChar()) {
            getMemory().bs();
        }
        getMemory().writeText(ccData.getDisplayText());
        if ((mMode == android.media.Cea608CCParser.MODE_PAINT_ON) || (mMode == android.media.Cea608CCParser.MODE_ROLL_UP)) {
            updateDisplay();
        }
        return true;
    }

    private boolean handleMidRowCode(android.media.Cea608CCParser.CCData ccData) {
        android.media.Cea608CCParser.StyleCode m = ccData.getMidRow();
        if (m != null) {
            getMemory().writeMidRowCode(m);
            return true;
        }
        return false;
    }

    private boolean handlePACCode(android.media.Cea608CCParser.CCData ccData) {
        android.media.Cea608CCParser.PAC pac = ccData.getPAC();
        if (pac != null) {
            if (mMode == android.media.Cea608CCParser.MODE_ROLL_UP) {
                getMemory().moveBaselineTo(pac.getRow(), mRollUpSize);
            }
            getMemory().writePAC(pac);
            return true;
        }
        return false;
    }

    private boolean handleTabOffsets(android.media.Cea608CCParser.CCData ccData) {
        int tabs = ccData.getTabOffset();
        if (tabs > 0) {
            getMemory().tab(tabs);
            return true;
        }
        return false;
    }

    private boolean handleCtrlCode(android.media.Cea608CCParser.CCData ccData) {
        int ctrlCode = ccData.getCtrlCode();
        if ((mPrevCtrlCode != android.media.Cea608CCParser.INVALID) && (mPrevCtrlCode == ctrlCode)) {
            // discard double ctrl codes (but if there's a 3rd one, we still take that)
            mPrevCtrlCode = android.media.Cea608CCParser.INVALID;
            return true;
        }
        switch (ctrlCode) {
            case android.media.Cea608CCParser.RCL :
                // select pop-on style
                mMode = android.media.Cea608CCParser.MODE_POP_ON;
                break;
            case android.media.Cea608CCParser.BS :
                getMemory().bs();
                break;
            case android.media.Cea608CCParser.DER :
                getMemory().der();
                break;
            case android.media.Cea608CCParser.RU2 :
            case android.media.Cea608CCParser.RU3 :
            case android.media.Cea608CCParser.RU4 :
                mRollUpSize = ctrlCode - 0x23;
                // erase memory if currently in other style
                if (mMode != android.media.Cea608CCParser.MODE_ROLL_UP) {
                    mDisplay.erase();
                    mNonDisplay.erase();
                }
                // select roll-up style
                mMode = android.media.Cea608CCParser.MODE_ROLL_UP;
                break;
            case android.media.Cea608CCParser.FON :
                android.util.Log.i(android.media.Cea608CCParser.TAG, "Flash On");
                break;
            case android.media.Cea608CCParser.RDC :
                // select paint-on style
                mMode = android.media.Cea608CCParser.MODE_PAINT_ON;
                break;
            case android.media.Cea608CCParser.TR :
                mMode = android.media.Cea608CCParser.MODE_TEXT;
                mTextMem.erase();
                break;
            case android.media.Cea608CCParser.RTD :
                mMode = android.media.Cea608CCParser.MODE_TEXT;
                break;
            case android.media.Cea608CCParser.EDM :
                // erase display memory
                mDisplay.erase();
                updateDisplay();
                break;
            case android.media.Cea608CCParser.CR :
                if (mMode == android.media.Cea608CCParser.MODE_ROLL_UP) {
                    getMemory().rollUp(mRollUpSize);
                } else {
                    getMemory().cr();
                }
                if (mMode == android.media.Cea608CCParser.MODE_ROLL_UP) {
                    updateDisplay();
                }
                break;
            case android.media.Cea608CCParser.ENM :
                // erase non-display memory
                mNonDisplay.erase();
                break;
            case android.media.Cea608CCParser.EOC :
                // swap display/non-display memory
                swapMemory();
                // switch to pop-on style
                mMode = android.media.Cea608CCParser.MODE_POP_ON;
                updateDisplay();
                break;
            case android.media.Cea608CCParser.INVALID :
            default :
                mPrevCtrlCode = android.media.Cea608CCParser.INVALID;
                return false;
        }
        mPrevCtrlCode = ctrlCode;
        // handled
        return true;
    }

    private void updateDisplay() {
        if (mListener != null) {
            android.view.accessibility.CaptioningManager.CaptionStyle captionStyle = mListener.getCaptionStyle();
            mListener.onDisplayChanged(mDisplay.getStyledText(captionStyle));
        }
    }

    private void swapMemory() {
        android.media.Cea608CCParser.CCMemory temp = mDisplay;
        mDisplay = mNonDisplay;
        mNonDisplay = temp;
    }

    private static class StyleCode {
        static final int COLOR_WHITE = 0;

        static final int COLOR_GREEN = 1;

        static final int COLOR_BLUE = 2;

        static final int COLOR_CYAN = 3;

        static final int COLOR_RED = 4;

        static final int COLOR_YELLOW = 5;

        static final int COLOR_MAGENTA = 6;

        static final int COLOR_INVALID = 7;

        static final int STYLE_ITALICS = 0x1;

        static final int STYLE_UNDERLINE = 0x2;

        static final java.lang.String[] mColorMap = new java.lang.String[]{ "WHITE", "GREEN", "BLUE", "CYAN", "RED", "YELLOW", "MAGENTA", "INVALID" };

        final int mStyle;

        final int mColor;

        static android.media.Cea608CCParser.StyleCode fromByte(byte data2) {
            int style = 0;
            int color = (data2 >> 1) & 0x7;
            if ((data2 & 0x1) != 0) {
                style |= android.media.Cea608CCParser.StyleCode.STYLE_UNDERLINE;
            }
            if (color == android.media.Cea608CCParser.StyleCode.COLOR_INVALID) {
                // WHITE ITALICS
                color = android.media.Cea608CCParser.StyleCode.COLOR_WHITE;
                style |= android.media.Cea608CCParser.StyleCode.STYLE_ITALICS;
            }
            return new android.media.Cea608CCParser.StyleCode(style, color);
        }

        StyleCode(int style, int color) {
            mStyle = style;
            mColor = color;
        }

        boolean isItalics() {
            return (mStyle & android.media.Cea608CCParser.StyleCode.STYLE_ITALICS) != 0;
        }

        boolean isUnderline() {
            return (mStyle & android.media.Cea608CCParser.StyleCode.STYLE_UNDERLINE) != 0;
        }

        int getColor() {
            return mColor;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder str = new java.lang.StringBuilder();
            str.append("{");
            str.append(android.media.Cea608CCParser.StyleCode.mColorMap[mColor]);
            if ((mStyle & android.media.Cea608CCParser.StyleCode.STYLE_ITALICS) != 0) {
                str.append(", ITALICS");
            }
            if ((mStyle & android.media.Cea608CCParser.StyleCode.STYLE_UNDERLINE) != 0) {
                str.append(", UNDERLINE");
            }
            str.append("}");
            return str.toString();
        }
    }

    private static class PAC extends android.media.Cea608CCParser.StyleCode {
        final int mRow;

        final int mCol;

        static android.media.Cea608CCParser.PAC fromBytes(byte data1, byte data2) {
            int[] rowTable = new int[]{ 11, 1, 3, 12, 14, 5, 7, 9 };
            int row = rowTable[data1 & 0x7] + ((data2 & 0x20) >> 5);
            int style = 0;
            if ((data2 & 1) != 0) {
                style |= android.media.Cea608CCParser.StyleCode.STYLE_UNDERLINE;
            }
            if ((data2 & 0x10) != 0) {
                // indent code
                int indent = (data2 >> 1) & 0x7;
                return new android.media.Cea608CCParser.PAC(row, indent * 4, style, android.media.Cea608CCParser.StyleCode.COLOR_WHITE);
            } else {
                // style code
                int color = (data2 >> 1) & 0x7;
                if (color == android.media.Cea608CCParser.StyleCode.COLOR_INVALID) {
                    // WHITE ITALICS
                    color = android.media.Cea608CCParser.StyleCode.COLOR_WHITE;
                    style |= android.media.Cea608CCParser.StyleCode.STYLE_ITALICS;
                }
                return new android.media.Cea608CCParser.PAC(row, -1, style, color);
            }
        }

        PAC(int row, int col, int style, int color) {
            super(style, color);
            mRow = row;
            mCol = col;
        }

        boolean isIndentPAC() {
            return mCol >= 0;
        }

        int getRow() {
            return mRow;
        }

        int getCol() {
            return mCol;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return java.lang.String.format("{%d, %d}, %s", mRow, mCol, super.toString());
        }
    }

    /**
     * Mutable version of BackgroundSpan to facilitate text rendering with edge styles.
     *
     * @unknown 
     */
    public static class MutableBackgroundColorSpan extends android.text.style.CharacterStyle implements android.text.style.UpdateAppearance {
        private int mColor;

        public MutableBackgroundColorSpan(int color) {
            mColor = color;
        }

        public void setBackgroundColor(int color) {
            mColor = color;
        }

        public int getBackgroundColor() {
            return mColor;
        }

        @java.lang.Override
        public void updateDrawState(android.text.TextPaint ds) {
            ds.bgColor = mColor;
        }
    }

    /* CCLineBuilder keeps track of displayable chars, as well as
    MidRow styles and PACs, for a single line of CC memory.

    It generates styled text via getStyledText() method.
     */
    private static class CCLineBuilder {
        private final java.lang.StringBuilder mDisplayChars;

        private final android.media.Cea608CCParser.StyleCode[] mMidRowStyles;

        private final android.media.Cea608CCParser.StyleCode[] mPACStyles;

        CCLineBuilder(java.lang.String str) {
            mDisplayChars = new java.lang.StringBuilder(str);
            mMidRowStyles = new android.media.Cea608CCParser.StyleCode[mDisplayChars.length()];
            mPACStyles = new android.media.Cea608CCParser.StyleCode[mDisplayChars.length()];
        }

        void setCharAt(int index, char ch) {
            mDisplayChars.setCharAt(index, ch);
            mMidRowStyles[index] = null;
        }

        void setMidRowAt(int index, android.media.Cea608CCParser.StyleCode m) {
            mDisplayChars.setCharAt(index, ' ');
            mMidRowStyles[index] = m;
        }

        void setPACAt(int index, android.media.Cea608CCParser.PAC pac) {
            mPACStyles[index] = pac;
        }

        char charAt(int index) {
            return mDisplayChars.charAt(index);
        }

        int length() {
            return mDisplayChars.length();
        }

        void applyStyleSpan(android.text.SpannableStringBuilder styledText, android.media.Cea608CCParser.StyleCode s, int start, int end) {
            if (s.isItalics()) {
                styledText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), start, end, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (s.isUnderline()) {
                styledText.setSpan(new android.text.style.UnderlineSpan(), start, end, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        android.text.SpannableStringBuilder getStyledText(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle) {
            android.text.SpannableStringBuilder styledText = new android.text.SpannableStringBuilder(mDisplayChars);
            int start = -1;
            int next = 0;
            int styleStart = -1;
            android.media.Cea608CCParser.StyleCode curStyle = null;
            while (next < mDisplayChars.length()) {
                android.media.Cea608CCParser.StyleCode newStyle = null;
                if (mMidRowStyles[next] != null) {
                    // apply mid-row style change
                    newStyle = mMidRowStyles[next];
                } else
                    if ((mPACStyles[next] != null) && ((styleStart < 0) || (start < 0))) {
                        // apply PAC style change, only if:
                        // 1. no style set, or
                        // 2. style set, but prev char is none-displayable
                        newStyle = mPACStyles[next];
                    }

                if (newStyle != null) {
                    curStyle = newStyle;
                    if ((styleStart >= 0) && (start >= 0)) {
                        applyStyleSpan(styledText, newStyle, styleStart, next);
                    }
                    styleStart = next;
                }
                if (mDisplayChars.charAt(next) != android.media.Cea608CCParser.TS) {
                    if (start < 0) {
                        start = next;
                    }
                } else
                    if (start >= 0) {
                        int expandedStart = (mDisplayChars.charAt(start) == ' ') ? start : start - 1;
                        int expandedEnd = (mDisplayChars.charAt(next - 1) == ' ') ? next : next + 1;
                        styledText.setSpan(new android.media.Cea608CCParser.MutableBackgroundColorSpan(captionStyle.backgroundColor), expandedStart, expandedEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (styleStart >= 0) {
                            applyStyleSpan(styledText, curStyle, styleStart, expandedEnd);
                        }
                        start = -1;
                    }

                next++;
            } 
            return styledText;
        }
    }

    /* CCMemory models a console-style display. */
    private static class CCMemory {
        private final java.lang.String mBlankLine;

        private final android.media.Cea608CCParser.CCLineBuilder[] mLines = new android.media.Cea608CCParser.CCLineBuilder[android.media.Cea608CCParser.MAX_ROWS + 2];

        private int mRow;

        private int mCol;

        CCMemory() {
            char[] blank = new char[android.media.Cea608CCParser.MAX_COLS + 2];
            java.util.Arrays.fill(blank, android.media.Cea608CCParser.TS);
            mBlankLine = new java.lang.String(blank);
        }

        void erase() {
            // erase all lines
            for (int i = 0; i < mLines.length; i++) {
                mLines[i] = null;
            }
            mRow = android.media.Cea608CCParser.MAX_ROWS;
            mCol = 1;
        }

        void der() {
            if (mLines[mRow] != null) {
                for (int i = 0; i < mCol; i++) {
                    if (mLines[mRow].charAt(i) != android.media.Cea608CCParser.TS) {
                        for (int j = mCol; j < mLines[mRow].length(); j++) {
                            mLines[j].setCharAt(j, android.media.Cea608CCParser.TS);
                        }
                        return;
                    }
                }
                mLines[mRow] = null;
            }
        }

        void tab(int tabs) {
            moveCursorByCol(tabs);
        }

        void bs() {
            moveCursorByCol(-1);
            if (mLines[mRow] != null) {
                mLines[mRow].setCharAt(mCol, android.media.Cea608CCParser.TS);
                if (mCol == (android.media.Cea608CCParser.MAX_COLS - 1)) {
                    // Spec recommendation:
                    // if cursor was at col 32, move cursor
                    // back to col 31 and erase both col 31&32
                    mLines[mRow].setCharAt(android.media.Cea608CCParser.MAX_COLS, android.media.Cea608CCParser.TS);
                }
            }
        }

        void cr() {
            moveCursorTo(mRow + 1, 1);
        }

        void rollUp(int windowSize) {
            int i;
            for (i = 0; i <= (mRow - windowSize); i++) {
                mLines[i] = null;
            }
            int startRow = (mRow - windowSize) + 1;
            if (startRow < 1) {
                startRow = 1;
            }
            for (i = startRow; i < mRow; i++) {
                mLines[i] = mLines[i + 1];
            }
            for (i = mRow; i < mLines.length; i++) {
                // clear base row
                mLines[i] = null;
            }
            // default to col 1, in case PAC is not sent
            mCol = 1;
        }

        void writeText(java.lang.String text) {
            for (int i = 0; i < text.length(); i++) {
                getLineBuffer(mRow).setCharAt(mCol, text.charAt(i));
                moveCursorByCol(1);
            }
        }

        void writeMidRowCode(android.media.Cea608CCParser.StyleCode m) {
            getLineBuffer(mRow).setMidRowAt(mCol, m);
            moveCursorByCol(1);
        }

        void writePAC(android.media.Cea608CCParser.PAC pac) {
            if (pac.isIndentPAC()) {
                moveCursorTo(pac.getRow(), pac.getCol());
            } else {
                moveCursorTo(pac.getRow(), 1);
            }
            getLineBuffer(mRow).setPACAt(mCol, pac);
        }

        android.text.SpannableStringBuilder[] getStyledText(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle) {
            java.util.ArrayList<android.text.SpannableStringBuilder> rows = new java.util.ArrayList<>(android.media.Cea608CCParser.MAX_ROWS);
            for (int i = 1; i <= android.media.Cea608CCParser.MAX_ROWS; i++) {
                rows.add(mLines[i] != null ? mLines[i].getStyledText(captionStyle) : null);
            }
            return rows.toArray(new android.text.SpannableStringBuilder[android.media.Cea608CCParser.MAX_ROWS]);
        }

        private static int clamp(int x, int min, int max) {
            return x < min ? min : x > max ? max : x;
        }

        private void moveCursorTo(int row, int col) {
            mRow = android.media.Cea608CCParser.CCMemory.clamp(row, 1, android.media.Cea608CCParser.MAX_ROWS);
            mCol = android.media.Cea608CCParser.CCMemory.clamp(col, 1, android.media.Cea608CCParser.MAX_COLS);
        }

        private void moveCursorToRow(int row) {
            mRow = android.media.Cea608CCParser.CCMemory.clamp(row, 1, android.media.Cea608CCParser.MAX_ROWS);
        }

        private void moveCursorByCol(int col) {
            mCol = android.media.Cea608CCParser.CCMemory.clamp(mCol + col, 1, android.media.Cea608CCParser.MAX_COLS);
        }

        private void moveBaselineTo(int baseRow, int windowSize) {
            if (mRow == baseRow) {
                return;
            }
            int actualWindowSize = windowSize;
            if (baseRow < actualWindowSize) {
                actualWindowSize = baseRow;
            }
            if (mRow < actualWindowSize) {
                actualWindowSize = mRow;
            }
            int i;
            if (baseRow < mRow) {
                // copy from bottom to top row
                for (i = actualWindowSize - 1; i >= 0; i--) {
                    mLines[baseRow - i] = mLines[mRow - i];
                }
            } else {
                // copy from top to bottom row
                for (i = 0; i < actualWindowSize; i++) {
                    mLines[baseRow - i] = mLines[mRow - i];
                }
            }
            // clear rest of the rows
            for (i = 0; i <= (baseRow - windowSize); i++) {
                mLines[i] = null;
            }
            for (i = baseRow + 1; i < mLines.length; i++) {
                mLines[i] = null;
            }
        }

        private android.media.Cea608CCParser.CCLineBuilder getLineBuffer(int row) {
            if (mLines[row] == null) {
                mLines[row] = new android.media.Cea608CCParser.CCLineBuilder(mBlankLine);
            }
            return mLines[row];
        }
    }

    /* CCData parses the raw CC byte pair into displayable chars,
    misc control codes, Mid-Row or Preamble Address Codes.
     */
    private static class CCData {
        private final byte mType;

        private final byte mData1;

        private final byte mData2;

        private static final java.lang.String[] mCtrlCodeMap = new java.lang.String[]{ "RCL", "BS", "AOF", "AON", "DER", "RU2", "RU3", "RU4", "FON", "RDC", "TR", "RTD", "EDM", "CR", "ENM", "EOC" };

        private static final java.lang.String[] mSpecialCharMap = new java.lang.String[]{ "\u00ae", "\u00b0", "\u00bd", "\u00bf", "\u2122", "\u00a2", "\u00a3", "\u266a"// Eighth note
        , "\u00e0", "\u00a0"// Transparent space
        , "\u00e8", "\u00e2", "\u00ea", "\u00ee", "\u00f4", "\u00fb" };

        private static final java.lang.String[] mSpanishCharMap = new java.lang.String[]{ // Spanish and misc chars
        "\u00c1"// A
        , "\u00c9"// E
        , "\u00d3"// I
        , "\u00da"// O
        , "\u00dc"// U
        , "\u00fc"// u
        , "\u2018"// opening single quote
        , "\u00a1"// inverted exclamation mark
        , "*", "'", "\u2014"// em dash
        , "\u00a9"// Copyright
        , "\u2120"// Servicemark
        , "\u2022"// round bullet
        , "\u201c"// opening double quote
        , "\u201d"// closing double quote
        , // French
        "\u00c0", "\u00c2", "\u00c7", "\u00c8", "\u00ca", "\u00cb", "\u00eb", "\u00ce", "\u00cf", "\u00ef", "\u00d4", "\u00d9", "\u00f9", "\u00db", "\u00ab", "\u00bb" };

        private static final java.lang.String[] mProtugueseCharMap = new java.lang.String[]{ // Portuguese
        "\u00c3", "\u00e3", "\u00cd", "\u00cc", "\u00ec", "\u00d2", "\u00f2", "\u00d5", "\u00f5", "{", "}", "\\", "^", "_", "|", "~", // German and misc chars
        "\u00c4", "\u00e4", "\u00d6", "\u00f6", "\u00df", "\u00a5", "\u00a4", "\u2502"// vertical bar
        , "\u00c5", "\u00e5", "\u00d8", "\u00f8", "\u250c"// top-left corner
        , "\u2510"// top-right corner
        , "\u2514"// lower-left corner
        , "\u2518"// lower-right corner
         };

        static android.media.Cea608CCParser.CCData[] fromByteArray(byte[] data) {
            android.media.Cea608CCParser.CCData[] ccData = new android.media.Cea608CCParser.CCData[data.length / 3];
            for (int i = 0; i < ccData.length; i++) {
                ccData[i] = new android.media.Cea608CCParser.CCData(data[i * 3], data[(i * 3) + 1], data[(i * 3) + 2]);
            }
            return ccData;
        }

        CCData(byte type, byte data1, byte data2) {
            mType = type;
            mData1 = data1;
            mData2 = data2;
        }

        int getCtrlCode() {
            if ((((mData1 == 0x14) || (mData1 == 0x1c)) && (mData2 >= 0x20)) && (mData2 <= 0x2f)) {
                return mData2;
            }
            return android.media.Cea608CCParser.INVALID;
        }

        android.media.Cea608CCParser.StyleCode getMidRow() {
            // only support standard Mid-row codes, ignore
            // optional background/foreground mid-row codes
            if ((((mData1 == 0x11) || (mData1 == 0x19)) && (mData2 >= 0x20)) && (mData2 <= 0x2f)) {
                return android.media.Cea608CCParser.StyleCode.fromByte(mData2);
            }
            return null;
        }

        android.media.Cea608CCParser.PAC getPAC() {
            if ((((mData1 & 0x70) == 0x10) && ((mData2 & 0x40) == 0x40)) && (((mData1 & 0x7) != 0) || ((mData2 & 0x20) == 0))) {
                return android.media.Cea608CCParser.PAC.fromBytes(mData1, mData2);
            }
            return null;
        }

        int getTabOffset() {
            if ((((mData1 == 0x17) || (mData1 == 0x1f)) && (mData2 >= 0x21)) && (mData2 <= 0x23)) {
                return mData2 & 0x3;
            }
            return 0;
        }

        boolean isDisplayableChar() {
            return (isBasicChar() || isSpecialChar()) || isExtendedChar();
        }

        java.lang.String getDisplayText() {
            java.lang.String str = getBasicChars();
            if (str == null) {
                str = getSpecialChar();
                if (str == null) {
                    str = getExtendedChar();
                }
            }
            return str;
        }

        private java.lang.String ctrlCodeToString(int ctrlCode) {
            return android.media.Cea608CCParser.CCData.mCtrlCodeMap[ctrlCode - 0x20];
        }

        private boolean isBasicChar() {
            return (mData1 >= 0x20) && (mData1 <= 0x7f);
        }

        private boolean isSpecialChar() {
            return (((mData1 == 0x11) || (mData1 == 0x19)) && (mData2 >= 0x30)) && (mData2 <= 0x3f);
        }

        private boolean isExtendedChar() {
            return (((((mData1 == 0x12) || (mData1 == 0x1a)) || (mData1 == 0x13)) || (mData1 == 0x1b)) && (mData2 >= 0x20)) && (mData2 <= 0x3f);
        }

        private char getBasicChar(byte data) {
            char c;
            // replace the non-ASCII ones
            switch (data) {
                case 0x2a :
                    c = '\u00e1';
                    break;
                case 0x5c :
                    c = '\u00e9';
                    break;
                case 0x5e :
                    c = '\u00ed';
                    break;
                case 0x5f :
                    c = '\u00f3';
                    break;
                case 0x60 :
                    c = '\u00fa';
                    break;
                case 0x7b :
                    c = '\u00e7';
                    break;
                case 0x7c :
                    c = '\u00f7';
                    break;
                case 0x7d :
                    c = '\u00d1';
                    break;
                case 0x7e :
                    c = '\u00f1';
                    break;
                case 0x7f :
                    c = '\u2588';
                    break;// Full block

                default :
                    c = ((char) (data));
                    break;
            }
            return c;
        }

        private java.lang.String getBasicChars() {
            if ((mData1 >= 0x20) && (mData1 <= 0x7f)) {
                java.lang.StringBuilder builder = new java.lang.StringBuilder(2);
                builder.append(getBasicChar(mData1));
                if ((mData2 >= 0x20) && (mData2 <= 0x7f)) {
                    builder.append(getBasicChar(mData2));
                }
                return builder.toString();
            }
            return null;
        }

        private java.lang.String getSpecialChar() {
            if ((((mData1 == 0x11) || (mData1 == 0x19)) && (mData2 >= 0x30)) && (mData2 <= 0x3f)) {
                return android.media.Cea608CCParser.CCData.mSpecialCharMap[mData2 - 0x30];
            }
            return null;
        }

        private java.lang.String getExtendedChar() {
            if ((((mData1 == 0x12) || (mData1 == 0x1a)) && (mData2 >= 0x20)) && (mData2 <= 0x3f)) {
                // 1 Spanish/French char
                return android.media.Cea608CCParser.CCData.mSpanishCharMap[mData2 - 0x20];
            } else
                if ((((mData1 == 0x13) || (mData1 == 0x1b)) && (mData2 >= 0x20)) && (mData2 <= 0x3f)) {
                    // 1 Portuguese/German/Danish char
                    return android.media.Cea608CCParser.CCData.mProtugueseCharMap[mData2 - 0x20];
                }

            return null;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String str;
            if ((mData1 < 0x10) && (mData2 < 0x10)) {
                // Null Pad, ignore
                return java.lang.String.format("[%d]Null: %02x %02x", mType, mData1, mData2);
            }
            int ctrlCode = getCtrlCode();
            if (ctrlCode != android.media.Cea608CCParser.INVALID) {
                return java.lang.String.format("[%d]%s", mType, ctrlCodeToString(ctrlCode));
            }
            int tabOffset = getTabOffset();
            if (tabOffset > 0) {
                return java.lang.String.format("[%d]Tab%d", mType, tabOffset);
            }
            android.media.Cea608CCParser.PAC pac = getPAC();
            if (pac != null) {
                return java.lang.String.format("[%d]PAC: %s", mType, pac.toString());
            }
            android.media.Cea608CCParser.StyleCode m = getMidRow();
            if (m != null) {
                return java.lang.String.format("[%d]Mid-row: %s", mType, m.toString());
            }
            if (isDisplayableChar()) {
                return java.lang.String.format("[%d]Displayable: %s (%02x %02x)", mType, getDisplayText(), mData1, mData2);
            }
            return java.lang.String.format("[%d]Invalid: %02x %02x", mType, mData1, mData2);
        }
    }
}

