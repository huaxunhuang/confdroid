/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.text;


/**
 * DynamicLayout is a text layout that updates itself as the text is edited.
 * <p>This is used by widgets to control text layout. You should not need
 * to use this class directly unless you are implementing your own widget
 * or custom display object, or need to call
 * {@link android.graphics.Canvas#drawText(java.lang.CharSequence, int, int, float, float, android.graphics.Paint)
 *  Canvas.drawText()} directly.</p>
 */
public class DynamicLayout extends android.text.Layout {
    private static final int PRIORITY = 128;

    private static final int BLOCK_MINIMUM_CHARACTER_LENGTH = 400;

    /**
     * Make a layout for the specified text that will be updated as
     * the text is changed.
     */
    public DynamicLayout(java.lang.CharSequence base, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(base, base, paint, width, align, spacingmult, spacingadd, includepad);
    }

    /**
     * Make a layout for the transformed text (password transformation
     * being the primary example of a transformation)
     * that will be updated as the base text is changed.
     */
    public DynamicLayout(java.lang.CharSequence base, java.lang.CharSequence display, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(base, display, paint, width, align, spacingmult, spacingadd, includepad, null, 0);
    }

    /**
     * Make a layout for the transformed text (password transformation
     * being the primary example of a transformation)
     * that will be updated as the base text is changed.
     * If ellipsize is non-null, the Layout will ellipsize the text
     * down to ellipsizedWidth.
     */
    public DynamicLayout(java.lang.CharSequence base, java.lang.CharSequence display, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad, android.text.TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        this(base, display, paint, width, align, android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, android.text.StaticLayout.BREAK_STRATEGY_SIMPLE, android.text.StaticLayout.HYPHENATION_FREQUENCY_NONE, ellipsize, ellipsizedWidth);
    }

    /**
     * Make a layout for the transformed text (password transformation
     * being the primary example of a transformation)
     * that will be updated as the base text is changed.
     * If ellipsize is non-null, the Layout will ellipsize the text
     * down to ellipsizedWidth.
     * *
     * *@hide
     */
    public DynamicLayout(java.lang.CharSequence base, java.lang.CharSequence display, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, android.text.TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad, int breakStrategy, int hyphenationFrequency, android.text.TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super(ellipsize == null ? display : display instanceof android.text.Spanned ? new android.text.Layout.SpannedEllipsizer(display) : new android.text.Layout.Ellipsizer(display), paint, width, align, textDir, spacingmult, spacingadd);
        mBase = base;
        mDisplay = display;
        if (ellipsize != null) {
            mInts = new android.text.PackedIntVector(android.text.DynamicLayout.COLUMNS_ELLIPSIZE);
            mEllipsizedWidth = ellipsizedWidth;
            mEllipsizeAt = ellipsize;
        } else {
            mInts = new android.text.PackedIntVector(android.text.DynamicLayout.COLUMNS_NORMAL);
            mEllipsizedWidth = width;
            mEllipsizeAt = null;
        }
        mObjects = new android.text.PackedObjectVector<android.text.Layout.Directions>(1);
        mIncludePad = includepad;
        mBreakStrategy = breakStrategy;
        mHyphenationFrequency = hyphenationFrequency;
        /* This is annoying, but we can't refer to the layout until
        superclass construction is finished, and the superclass
        constructor wants the reference to the display text.

        This will break if the superclass constructor ever actually
        cares about the content instead of just holding the reference.
         */
        if (ellipsize != null) {
            android.text.Layout.Ellipsizer e = ((android.text.Layout.Ellipsizer) (getText()));
            e.mLayout = this;
            e.mWidth = ellipsizedWidth;
            e.mMethod = ellipsize;
            mEllipsize = true;
        }
        // Initial state is a single line with 0 characters (0 to 0),
        // with top at 0 and bottom at whatever is natural, and
        // undefined ellipsis.
        int[] start;
        if (ellipsize != null) {
            start = new int[android.text.DynamicLayout.COLUMNS_ELLIPSIZE];
            start[android.text.DynamicLayout.ELLIPSIS_START] = android.text.DynamicLayout.ELLIPSIS_UNDEFINED;
        } else {
            start = new int[android.text.DynamicLayout.COLUMNS_NORMAL];
        }
        android.text.Layout.Directions[] dirs = new android.text.Layout.Directions[]{ android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT };
        android.graphics.Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int asc = fm.ascent;
        int desc = fm.descent;
        start[android.text.DynamicLayout.DIR] = android.text.Layout.DIR_LEFT_TO_RIGHT << android.text.DynamicLayout.DIR_SHIFT;
        start[android.text.DynamicLayout.TOP] = 0;
        start[android.text.DynamicLayout.DESCENT] = desc;
        mInts.insertAt(0, start);
        start[android.text.DynamicLayout.TOP] = desc - asc;
        mInts.insertAt(1, start);
        mObjects.insertAt(0, dirs);
        // Update from 0 characters to whatever the real text is
        reflow(base, 0, 0, base.length());
        if (base instanceof android.text.Spannable) {
            if (mWatcher == null)
                mWatcher = new android.text.DynamicLayout.ChangeWatcher(this);

            // Strip out any watchers for other DynamicLayouts.
            android.text.Spannable sp = ((android.text.Spannable) (base));
            android.text.DynamicLayout.ChangeWatcher[] spans = sp.getSpans(0, sp.length(), android.text.DynamicLayout.ChangeWatcher.class);
            for (int i = 0; i < spans.length; i++)
                sp.removeSpan(spans[i]);

            sp.setSpan(mWatcher, 0, base.length(), android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE | (android.text.DynamicLayout.PRIORITY << android.text.Spannable.SPAN_PRIORITY_SHIFT));
        }
    }

    private void reflow(java.lang.CharSequence s, int where, int before, int after) {
        if (s != mBase)
            return;

        java.lang.CharSequence text = mDisplay;
        int len = text.length();
        // seek back to the start of the paragraph
        int find = android.text.TextUtils.lastIndexOf(text, '\n', where - 1);
        if (find < 0)
            find = 0;
        else
            find = find + 1;

        {
            int diff = where - find;
            before += diff;
            after += diff;
            where -= diff;
        }
        // seek forward to the end of the paragraph
        int look = android.text.TextUtils.indexOf(text, '\n', where + after);
        if (look < 0)
            look = len;
        // we want the index after the \n
        else
            look++;
        // we want the index after the \n

        int change = look - (where + after);
        before += change;
        after += change;
        // seek further out to cover anything that is forced to wrap together
        if (text instanceof android.text.Spanned) {
            android.text.Spanned sp = ((android.text.Spanned) (text));
            boolean again;
            do {
                again = false;
                java.lang.Object[] force = sp.getSpans(where, where + after, android.text.style.WrapTogetherSpan.class);
                for (int i = 0; i < force.length; i++) {
                    int st = sp.getSpanStart(force[i]);
                    int en = sp.getSpanEnd(force[i]);
                    if (st < where) {
                        again = true;
                        int diff = where - st;
                        before += diff;
                        after += diff;
                        where -= diff;
                    }
                    if (en > (where + after)) {
                        again = true;
                        int diff = en - (where + after);
                        before += diff;
                        after += diff;
                    }
                }
            } while (again );
        }
        // find affected region of old layout
        int startline = getLineForOffset(where);
        int startv = getLineTop(startline);
        int endline = getLineForOffset(where + before);
        if ((where + after) == len)
            endline = getLineCount();

        int endv = getLineTop(endline);
        boolean islast = endline == getLineCount();
        // generate new layout for affected text
        android.text.StaticLayout reflowed;
        android.text.StaticLayout.Builder b;
        synchronized(android.text.DynamicLayout.sLock) {
            reflowed = android.text.DynamicLayout.sStaticLayout;
            b = android.text.DynamicLayout.sBuilder;
            android.text.DynamicLayout.sStaticLayout = null;
            android.text.DynamicLayout.sBuilder = null;
        }
        if (reflowed == null) {
            reflowed = new android.text.StaticLayout(null);
            b = android.text.StaticLayout.Builder.obtain(text, where, where + after, getPaint(), getWidth());
        }
        b.setText(text, where, where + after).setPaint(getPaint()).setWidth(getWidth()).setTextDirection(getTextDirectionHeuristic()).setLineSpacing(getSpacingAdd(), getSpacingMultiplier()).setEllipsizedWidth(mEllipsizedWidth).setEllipsize(mEllipsizeAt).setBreakStrategy(mBreakStrategy).setHyphenationFrequency(mHyphenationFrequency);
        reflowed.generate(b, false, true);
        int n = reflowed.getLineCount();
        // If the new layout has a blank line at the end, but it is not
        // the very end of the buffer, then we already have a line that
        // starts there, so disregard the blank line.
        if (((where + after) != len) && (reflowed.getLineStart(n - 1) == (where + after)))
            n--;

        // remove affected lines from old layout
        mInts.deleteAt(startline, endline - startline);
        mObjects.deleteAt(startline, endline - startline);
        // adjust offsets in layout for new height and offsets
        int ht = reflowed.getLineTop(n);
        int toppad = 0;
        int botpad = 0;
        if (mIncludePad && (startline == 0)) {
            toppad = reflowed.getTopPadding();
            mTopPadding = toppad;
            ht -= toppad;
        }
        if (mIncludePad && islast) {
            botpad = reflowed.getBottomPadding();
            mBottomPadding = botpad;
            ht += botpad;
        }
        mInts.adjustValuesBelow(startline, android.text.DynamicLayout.START, after - before);
        mInts.adjustValuesBelow(startline, android.text.DynamicLayout.TOP, (startv - endv) + ht);
        // insert new layout
        int[] ints;
        if (mEllipsize) {
            ints = new int[android.text.DynamicLayout.COLUMNS_ELLIPSIZE];
            ints[android.text.DynamicLayout.ELLIPSIS_START] = android.text.DynamicLayout.ELLIPSIS_UNDEFINED;
        } else {
            ints = new int[android.text.DynamicLayout.COLUMNS_NORMAL];
        }
        android.text.Layout.Directions[] objects = new android.text.Layout.Directions[1];
        for (int i = 0; i < n; i++) {
            ints[android.text.DynamicLayout.START] = (reflowed.getLineStart(i) | (reflowed.getParagraphDirection(i) << android.text.DynamicLayout.DIR_SHIFT)) | (reflowed.getLineContainsTab(i) ? android.text.DynamicLayout.TAB_MASK : 0);
            int top = reflowed.getLineTop(i) + startv;
            if (i > 0)
                top -= toppad;

            ints[android.text.DynamicLayout.TOP] = top;
            int desc = reflowed.getLineDescent(i);
            if (i == (n - 1))
                desc += botpad;

            ints[android.text.DynamicLayout.DESCENT] = desc;
            objects[0] = reflowed.getLineDirections(i);
            ints[android.text.DynamicLayout.HYPHEN] = reflowed.getHyphen(i);
            if (mEllipsize) {
                ints[android.text.DynamicLayout.ELLIPSIS_START] = reflowed.getEllipsisStart(i);
                ints[android.text.DynamicLayout.ELLIPSIS_COUNT] = reflowed.getEllipsisCount(i);
            }
            mInts.insertAt(startline + i, ints);
            mObjects.insertAt(startline + i, objects);
        }
        updateBlocks(startline, endline - 1, n);
        b.finish();
        synchronized(android.text.DynamicLayout.sLock) {
            android.text.DynamicLayout.sStaticLayout = reflowed;
            android.text.DynamicLayout.sBuilder = b;
        }
    }

    /**
     * Create the initial block structure, cutting the text into blocks of at least
     * BLOCK_MINIMUM_CHARACTER_SIZE characters, aligned on the ends of paragraphs.
     */
    private void createBlocks() {
        int offset = android.text.DynamicLayout.BLOCK_MINIMUM_CHARACTER_LENGTH;
        mNumberOfBlocks = 0;
        final java.lang.CharSequence text = mDisplay;
        while (true) {
            offset = android.text.TextUtils.indexOf(text, '\n', offset);
            if (offset < 0) {
                addBlockAtOffset(text.length());
                break;
            } else {
                addBlockAtOffset(offset);
                offset += android.text.DynamicLayout.BLOCK_MINIMUM_CHARACTER_LENGTH;
            }
        } 
        // mBlockIndices and mBlockEndLines should have the same length
        mBlockIndices = new int[mBlockEndLines.length];
        for (int i = 0; i < mBlockEndLines.length; i++) {
            mBlockIndices[i] = android.text.DynamicLayout.INVALID_BLOCK_INDEX;
        }
    }

    /**
     * Create a new block, ending at the specified character offset.
     * A block will actually be created only if has at least one line, i.e. this offset is
     * not on the end line of the previous block.
     */
    private void addBlockAtOffset(int offset) {
        final int line = getLineForOffset(offset);
        if (mBlockEndLines == null) {
            // Initial creation of the array, no test on previous block ending line
            mBlockEndLines = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(1);
            mBlockEndLines[mNumberOfBlocks] = line;
            mNumberOfBlocks++;
            return;
        }
        final int previousBlockEndLine = mBlockEndLines[mNumberOfBlocks - 1];
        if (line > previousBlockEndLine) {
            mBlockEndLines = com.android.internal.util.GrowingArrayUtils.append(mBlockEndLines, mNumberOfBlocks, line);
            mNumberOfBlocks++;
        }
    }

    /**
     * This method is called every time the layout is reflowed after an edition.
     * It updates the internal block data structure. The text is split in blocks
     * of contiguous lines, with at least one block for the entire text.
     * When a range of lines is edited, new blocks (from 0 to 3 depending on the
     * overlap structure) will replace the set of overlapping blocks.
     * Blocks are listed in order and are represented by their ending line number.
     * An index is associated to each block (which will be used by display lists),
     * this class simply invalidates the index of blocks overlapping a modification.
     *
     * This method is package private and not private so that it can be tested.
     *
     * @param startLine
     * 		the first line of the range of modified lines
     * @param endLine
     * 		the last line of the range, possibly equal to startLine, lower
     * 		than getLineCount()
     * @param newLineCount
     * 		the number of lines that will replace the range, possibly 0
     * @unknown 
     */
    void updateBlocks(int startLine, int endLine, int newLineCount) {
        if (mBlockEndLines == null) {
            createBlocks();
            return;
        }
        int firstBlock = -1;
        int lastBlock = -1;
        for (int i = 0; i < mNumberOfBlocks; i++) {
            if (mBlockEndLines[i] >= startLine) {
                firstBlock = i;
                break;
            }
        }
        for (int i = firstBlock; i < mNumberOfBlocks; i++) {
            if (mBlockEndLines[i] >= endLine) {
                lastBlock = i;
                break;
            }
        }
        final int lastBlockEndLine = mBlockEndLines[lastBlock];
        boolean createBlockBefore = startLine > (firstBlock == 0 ? 0 : mBlockEndLines[firstBlock - 1] + 1);
        boolean createBlock = newLineCount > 0;
        boolean createBlockAfter = endLine < mBlockEndLines[lastBlock];
        int numAddedBlocks = 0;
        if (createBlockBefore)
            numAddedBlocks++;

        if (createBlock)
            numAddedBlocks++;

        if (createBlockAfter)
            numAddedBlocks++;

        final int numRemovedBlocks = (lastBlock - firstBlock) + 1;
        final int newNumberOfBlocks = (mNumberOfBlocks + numAddedBlocks) - numRemovedBlocks;
        if (newNumberOfBlocks == 0) {
            // Even when text is empty, there is actually one line and hence one block
            mBlockEndLines[0] = 0;
            mBlockIndices[0] = android.text.DynamicLayout.INVALID_BLOCK_INDEX;
            mNumberOfBlocks = 1;
            return;
        }
        if (newNumberOfBlocks > mBlockEndLines.length) {
            int[] blockEndLines = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(java.lang.Math.max(mBlockEndLines.length * 2, newNumberOfBlocks));
            int[] blockIndices = new int[blockEndLines.length];
            java.lang.System.arraycopy(mBlockEndLines, 0, blockEndLines, 0, firstBlock);
            java.lang.System.arraycopy(mBlockIndices, 0, blockIndices, 0, firstBlock);
            java.lang.System.arraycopy(mBlockEndLines, lastBlock + 1, blockEndLines, firstBlock + numAddedBlocks, (mNumberOfBlocks - lastBlock) - 1);
            java.lang.System.arraycopy(mBlockIndices, lastBlock + 1, blockIndices, firstBlock + numAddedBlocks, (mNumberOfBlocks - lastBlock) - 1);
            mBlockEndLines = blockEndLines;
            mBlockIndices = blockIndices;
        } else {
            java.lang.System.arraycopy(mBlockEndLines, lastBlock + 1, mBlockEndLines, firstBlock + numAddedBlocks, (mNumberOfBlocks - lastBlock) - 1);
            java.lang.System.arraycopy(mBlockIndices, lastBlock + 1, mBlockIndices, firstBlock + numAddedBlocks, (mNumberOfBlocks - lastBlock) - 1);
        }
        mNumberOfBlocks = newNumberOfBlocks;
        int newFirstChangedBlock;
        final int deltaLines = newLineCount - ((endLine - startLine) + 1);
        if (deltaLines != 0) {
            // Display list whose index is >= mIndexFirstChangedBlock is valid
            // but it needs to update its drawing location.
            newFirstChangedBlock = firstBlock + numAddedBlocks;
            for (int i = newFirstChangedBlock; i < mNumberOfBlocks; i++) {
                mBlockEndLines[i] += deltaLines;
            }
        } else {
            newFirstChangedBlock = mNumberOfBlocks;
        }
        mIndexFirstChangedBlock = java.lang.Math.min(mIndexFirstChangedBlock, newFirstChangedBlock);
        int blockIndex = firstBlock;
        if (createBlockBefore) {
            mBlockEndLines[blockIndex] = startLine - 1;
            mBlockIndices[blockIndex] = android.text.DynamicLayout.INVALID_BLOCK_INDEX;
            blockIndex++;
        }
        if (createBlock) {
            mBlockEndLines[blockIndex] = (startLine + newLineCount) - 1;
            mBlockIndices[blockIndex] = android.text.DynamicLayout.INVALID_BLOCK_INDEX;
            blockIndex++;
        }
        if (createBlockAfter) {
            mBlockEndLines[blockIndex] = lastBlockEndLine + deltaLines;
            mBlockIndices[blockIndex] = android.text.DynamicLayout.INVALID_BLOCK_INDEX;
        }
    }

    /**
     * This package private method is used for test purposes only
     *
     * @unknown 
     */
    void setBlocksDataForTest(int[] blockEndLines, int[] blockIndices, int numberOfBlocks) {
        mBlockEndLines = new int[blockEndLines.length];
        mBlockIndices = new int[blockIndices.length];
        java.lang.System.arraycopy(blockEndLines, 0, mBlockEndLines, 0, blockEndLines.length);
        java.lang.System.arraycopy(blockIndices, 0, mBlockIndices, 0, blockIndices.length);
        mNumberOfBlocks = numberOfBlocks;
    }

    /**
     *
     *
     * @unknown 
     */
    public int[] getBlockEndLines() {
        return mBlockEndLines;
    }

    /**
     *
     *
     * @unknown 
     */
    public int[] getBlockIndices() {
        return mBlockIndices;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getNumberOfBlocks() {
        return mNumberOfBlocks;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getIndexFirstChangedBlock() {
        return mIndexFirstChangedBlock;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setIndexFirstChangedBlock(int i) {
        mIndexFirstChangedBlock = i;
    }

    @java.lang.Override
    public int getLineCount() {
        return mInts.size() - 1;
    }

    @java.lang.Override
    public int getLineTop(int line) {
        return mInts.getValue(line, android.text.DynamicLayout.TOP);
    }

    @java.lang.Override
    public int getLineDescent(int line) {
        return mInts.getValue(line, android.text.DynamicLayout.DESCENT);
    }

    @java.lang.Override
    public int getLineStart(int line) {
        return mInts.getValue(line, android.text.DynamicLayout.START) & android.text.DynamicLayout.START_MASK;
    }

    @java.lang.Override
    public boolean getLineContainsTab(int line) {
        return (mInts.getValue(line, android.text.DynamicLayout.TAB) & android.text.DynamicLayout.TAB_MASK) != 0;
    }

    @java.lang.Override
    public int getParagraphDirection(int line) {
        return mInts.getValue(line, android.text.DynamicLayout.DIR) >> android.text.DynamicLayout.DIR_SHIFT;
    }

    @java.lang.Override
    public final android.text.Layout.Directions getLineDirections(int line) {
        return mObjects.getValue(line, 0);
    }

    @java.lang.Override
    public int getTopPadding() {
        return mTopPadding;
    }

    @java.lang.Override
    public int getBottomPadding() {
        return mBottomPadding;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getHyphen(int line) {
        return mInts.getValue(line, android.text.DynamicLayout.HYPHEN);
    }

    @java.lang.Override
    public int getEllipsizedWidth() {
        return mEllipsizedWidth;
    }

    private static class ChangeWatcher implements android.text.SpanWatcher , android.text.TextWatcher {
        public ChangeWatcher(android.text.DynamicLayout layout) {
            mLayout = new java.lang.ref.WeakReference<android.text.DynamicLayout>(layout);
        }

        private void reflow(java.lang.CharSequence s, int where, int before, int after) {
            android.text.DynamicLayout ml = mLayout.get();
            if (ml != null)
                ml.reflow(s, where, before, after);
            else
                if (s instanceof android.text.Spannable)
                    ((android.text.Spannable) (s)).removeSpan(this);


        }

        public void beforeTextChanged(java.lang.CharSequence s, int where, int before, int after) {
            // Intentionally empty
        }

        public void onTextChanged(java.lang.CharSequence s, int where, int before, int after) {
            reflow(s, where, before, after);
        }

        public void afterTextChanged(android.text.Editable s) {
            // Intentionally empty
        }

        public void onSpanAdded(android.text.Spannable s, java.lang.Object o, int start, int end) {
            if (o instanceof android.text.style.UpdateLayout)
                reflow(s, start, end - start, end - start);

        }

        public void onSpanRemoved(android.text.Spannable s, java.lang.Object o, int start, int end) {
            if (o instanceof android.text.style.UpdateLayout)
                reflow(s, start, end - start, end - start);

        }

        public void onSpanChanged(android.text.Spannable s, java.lang.Object o, int start, int end, int nstart, int nend) {
            if (o instanceof android.text.style.UpdateLayout) {
                reflow(s, start, end - start, end - start);
                reflow(s, nstart, nend - nstart, nend - nstart);
            }
        }

        private java.lang.ref.WeakReference<android.text.DynamicLayout> mLayout;
    }

    @java.lang.Override
    public int getEllipsisStart(int line) {
        if (mEllipsizeAt == null) {
            return 0;
        }
        return mInts.getValue(line, android.text.DynamicLayout.ELLIPSIS_START);
    }

    @java.lang.Override
    public int getEllipsisCount(int line) {
        if (mEllipsizeAt == null) {
            return 0;
        }
        return mInts.getValue(line, android.text.DynamicLayout.ELLIPSIS_COUNT);
    }

    private java.lang.CharSequence mBase;

    private java.lang.CharSequence mDisplay;

    private android.text.DynamicLayout.ChangeWatcher mWatcher;

    private boolean mIncludePad;

    private boolean mEllipsize;

    private int mEllipsizedWidth;

    private android.text.TextUtils.TruncateAt mEllipsizeAt;

    private int mBreakStrategy;

    private int mHyphenationFrequency;

    private android.text.PackedIntVector mInts;

    private android.text.PackedObjectVector<android.text.Layout.Directions> mObjects;

    /**
     * Value used in mBlockIndices when a block has been created or recycled and indicating that its
     * display list needs to be re-created.
     *
     * @unknown 
     */
    public static final int INVALID_BLOCK_INDEX = -1;

    // Stores the line numbers of the last line of each block (inclusive)
    private int[] mBlockEndLines;

    // The indices of this block's display list in TextView's internal display list array or
    // INVALID_BLOCK_INDEX if this block has been invalidated during an edition
    private int[] mBlockIndices;

    // Number of items actually currently being used in the above 2 arrays
    private int mNumberOfBlocks;

    // The first index of the blocks whose locations are changed
    private int mIndexFirstChangedBlock;

    private int mTopPadding;

    private int mBottomPadding;

    private static android.text.StaticLayout sStaticLayout = null;

    private static android.text.StaticLayout.Builder sBuilder = null;

    private static final java.lang.Object[] sLock = new java.lang.Object[0];

    private static final int START = 0;

    private static final int DIR = android.text.DynamicLayout.START;

    private static final int TAB = android.text.DynamicLayout.START;

    private static final int TOP = 1;

    private static final int DESCENT = 2;

    private static final int HYPHEN = 3;

    private static final int COLUMNS_NORMAL = 4;

    private static final int ELLIPSIS_START = 4;

    private static final int ELLIPSIS_COUNT = 5;

    private static final int COLUMNS_ELLIPSIZE = 6;

    private static final int START_MASK = 0x1fffffff;

    private static final int DIR_SHIFT = 30;

    private static final int TAB_MASK = 0x20000000;

    private static final int ELLIPSIS_UNDEFINED = 0x80000000;
}

