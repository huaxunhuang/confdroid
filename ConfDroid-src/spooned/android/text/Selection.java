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
 * Utility class for manipulating cursors and selections in CharSequences.
 * A cursor is a selection where the start and end are at the same offset.
 */
public class Selection {
    private Selection() {
        /* cannot be instantiated */
    }

    /* Retrieving the selection */
    /**
     * Return the offset of the selection anchor or cursor, or -1 if
     * there is no selection or cursor.
     */
    public static final int getSelectionStart(java.lang.CharSequence text) {
        if (text instanceof android.text.Spanned)
            return ((android.text.Spanned) (text)).getSpanStart(android.text.Selection.SELECTION_START);
        else
            return -1;

    }

    /**
     * Return the offset of the selection edge or cursor, or -1 if
     * there is no selection or cursor.
     */
    public static final int getSelectionEnd(java.lang.CharSequence text) {
        if (text instanceof android.text.Spanned)
            return ((android.text.Spanned) (text)).getSpanStart(android.text.Selection.SELECTION_END);
        else
            return -1;

    }

    /* Setting the selection */
    // private static int pin(int value, int min, int max) {
    // return value < min ? 0 : (value > max ? max : value);
    // }
    /**
     * Set the selection anchor to <code>start</code> and the selection edge
     * to <code>stop</code>.
     */
    public static void setSelection(android.text.Spannable text, int start, int stop) {
        // int len = text.length();
        // start = pin(start, 0, len);  XXX remove unless we really need it
        // stop = pin(stop, 0, len);
        int ostart = android.text.Selection.getSelectionStart(text);
        int oend = android.text.Selection.getSelectionEnd(text);
        if ((ostart != start) || (oend != stop)) {
            text.setSpan(android.text.Selection.SELECTION_START, start, start, android.text.Spanned.SPAN_POINT_POINT | android.text.Spanned.SPAN_INTERMEDIATE);
            text.setSpan(android.text.Selection.SELECTION_END, stop, stop, android.text.Spanned.SPAN_POINT_POINT);
        }
    }

    /**
     * Move the cursor to offset <code>index</code>.
     */
    public static final void setSelection(android.text.Spannable text, int index) {
        android.text.Selection.setSelection(text, index, index);
    }

    /**
     * Select the entire text.
     */
    public static final void selectAll(android.text.Spannable text) {
        android.text.Selection.setSelection(text, 0, text.length());
    }

    /**
     * Move the selection edge to offset <code>index</code>.
     */
    public static final void extendSelection(android.text.Spannable text, int index) {
        if (text.getSpanStart(android.text.Selection.SELECTION_END) != index)
            text.setSpan(android.text.Selection.SELECTION_END, index, index, android.text.Spanned.SPAN_POINT_POINT);

    }

    /**
     * Remove the selection or cursor, if any, from the text.
     */
    public static final void removeSelection(android.text.Spannable text) {
        text.removeSpan(android.text.Selection.SELECTION_START);
        text.removeSpan(android.text.Selection.SELECTION_END);
    }

    /* Moving the selection within the layout */
    /**
     * Move the cursor to the buffer offset physically above the current
     * offset, to the beginning if it is on the top line but not at the
     * start, or return false if the cursor is already on the top line.
     */
    public static boolean moveUp(android.text.Spannable text, android.text.Layout layout) {
        int start = android.text.Selection.getSelectionStart(text);
        int end = android.text.Selection.getSelectionEnd(text);
        if (start != end) {
            int min = java.lang.Math.min(start, end);
            int max = java.lang.Math.max(start, end);
            android.text.Selection.setSelection(text, min);
            if ((min == 0) && (max == text.length())) {
                return false;
            }
            return true;
        } else {
            int line = layout.getLineForOffset(end);
            if (line > 0) {
                int move;
                if (layout.getParagraphDirection(line) == layout.getParagraphDirection(line - 1)) {
                    float h = layout.getPrimaryHorizontal(end);
                    move = layout.getOffsetForHorizontal(line - 1, h);
                } else {
                    move = layout.getLineStart(line - 1);
                }
                android.text.Selection.setSelection(text, move);
                return true;
            } else
                if (end != 0) {
                    android.text.Selection.setSelection(text, 0);
                    return true;
                }

        }
        return false;
    }

    /**
     * Move the cursor to the buffer offset physically below the current
     * offset, to the end of the buffer if it is on the bottom line but
     * not at the end, or return false if the cursor is already at the
     * end of the buffer.
     */
    public static boolean moveDown(android.text.Spannable text, android.text.Layout layout) {
        int start = android.text.Selection.getSelectionStart(text);
        int end = android.text.Selection.getSelectionEnd(text);
        if (start != end) {
            int min = java.lang.Math.min(start, end);
            int max = java.lang.Math.max(start, end);
            android.text.Selection.setSelection(text, max);
            if ((min == 0) && (max == text.length())) {
                return false;
            }
            return true;
        } else {
            int line = layout.getLineForOffset(end);
            if (line < (layout.getLineCount() - 1)) {
                int move;
                if (layout.getParagraphDirection(line) == layout.getParagraphDirection(line + 1)) {
                    float h = layout.getPrimaryHorizontal(end);
                    move = layout.getOffsetForHorizontal(line + 1, h);
                } else {
                    move = layout.getLineStart(line + 1);
                }
                android.text.Selection.setSelection(text, move);
                return true;
            } else
                if (end != text.length()) {
                    android.text.Selection.setSelection(text, text.length());
                    return true;
                }

        }
        return false;
    }

    /**
     * Move the cursor to the buffer offset physically to the left of
     * the current offset, or return false if the cursor is already
     * at the left edge of the line and there is not another line to move it to.
     */
    public static boolean moveLeft(android.text.Spannable text, android.text.Layout layout) {
        int start = android.text.Selection.getSelectionStart(text);
        int end = android.text.Selection.getSelectionEnd(text);
        if (start != end) {
            android.text.Selection.setSelection(text, android.text.Selection.chooseHorizontal(layout, -1, start, end));
            return true;
        } else {
            int to = layout.getOffsetToLeftOf(end);
            if (to != end) {
                android.text.Selection.setSelection(text, to);
                return true;
            }
        }
        return false;
    }

    /**
     * Move the cursor to the buffer offset physically to the right of
     * the current offset, or return false if the cursor is already at
     * at the right edge of the line and there is not another line
     * to move it to.
     */
    public static boolean moveRight(android.text.Spannable text, android.text.Layout layout) {
        int start = android.text.Selection.getSelectionStart(text);
        int end = android.text.Selection.getSelectionEnd(text);
        if (start != end) {
            android.text.Selection.setSelection(text, android.text.Selection.chooseHorizontal(layout, 1, start, end));
            return true;
        } else {
            int to = layout.getOffsetToRightOf(end);
            if (to != end) {
                android.text.Selection.setSelection(text, to);
                return true;
            }
        }
        return false;
    }

    /**
     * Move the selection end to the buffer offset physically above
     * the current selection end.
     */
    public static boolean extendUp(android.text.Spannable text, android.text.Layout layout) {
        int end = android.text.Selection.getSelectionEnd(text);
        int line = layout.getLineForOffset(end);
        if (line > 0) {
            int move;
            if (layout.getParagraphDirection(line) == layout.getParagraphDirection(line - 1)) {
                float h = layout.getPrimaryHorizontal(end);
                move = layout.getOffsetForHorizontal(line - 1, h);
            } else {
                move = layout.getLineStart(line - 1);
            }
            android.text.Selection.extendSelection(text, move);
            return true;
        } else
            if (end != 0) {
                android.text.Selection.extendSelection(text, 0);
                return true;
            }

        return true;
    }

    /**
     * Move the selection end to the buffer offset physically below
     * the current selection end.
     */
    public static boolean extendDown(android.text.Spannable text, android.text.Layout layout) {
        int end = android.text.Selection.getSelectionEnd(text);
        int line = layout.getLineForOffset(end);
        if (line < (layout.getLineCount() - 1)) {
            int move;
            if (layout.getParagraphDirection(line) == layout.getParagraphDirection(line + 1)) {
                float h = layout.getPrimaryHorizontal(end);
                move = layout.getOffsetForHorizontal(line + 1, h);
            } else {
                move = layout.getLineStart(line + 1);
            }
            android.text.Selection.extendSelection(text, move);
            return true;
        } else
            if (end != text.length()) {
                android.text.Selection.extendSelection(text, text.length());
                return true;
            }

        return true;
    }

    /**
     * Move the selection end to the buffer offset physically to the left of
     * the current selection end.
     */
    public static boolean extendLeft(android.text.Spannable text, android.text.Layout layout) {
        int end = android.text.Selection.getSelectionEnd(text);
        int to = layout.getOffsetToLeftOf(end);
        if (to != end) {
            android.text.Selection.extendSelection(text, to);
            return true;
        }
        return true;
    }

    /**
     * Move the selection end to the buffer offset physically to the right of
     * the current selection end.
     */
    public static boolean extendRight(android.text.Spannable text, android.text.Layout layout) {
        int end = android.text.Selection.getSelectionEnd(text);
        int to = layout.getOffsetToRightOf(end);
        if (to != end) {
            android.text.Selection.extendSelection(text, to);
            return true;
        }
        return true;
    }

    public static boolean extendToLeftEdge(android.text.Spannable text, android.text.Layout layout) {
        int where = android.text.Selection.findEdge(text, layout, -1);
        android.text.Selection.extendSelection(text, where);
        return true;
    }

    public static boolean extendToRightEdge(android.text.Spannable text, android.text.Layout layout) {
        int where = android.text.Selection.findEdge(text, layout, 1);
        android.text.Selection.extendSelection(text, where);
        return true;
    }

    public static boolean moveToLeftEdge(android.text.Spannable text, android.text.Layout layout) {
        int where = android.text.Selection.findEdge(text, layout, -1);
        android.text.Selection.setSelection(text, where);
        return true;
    }

    public static boolean moveToRightEdge(android.text.Spannable text, android.text.Layout layout) {
        int where = android.text.Selection.findEdge(text, layout, 1);
        android.text.Selection.setSelection(text, where);
        return true;
    }

    /**
     * {@hide }
     */
    public static interface PositionIterator {
        public static final int DONE = java.text.BreakIterator.DONE;

        public int preceding(int position);

        public int following(int position);
    }

    /**
     * {@hide }
     */
    public static boolean moveToPreceding(android.text.Spannable text, android.text.Selection.PositionIterator iter, boolean extendSelection) {
        final int offset = iter.preceding(android.text.Selection.getSelectionEnd(text));
        if (offset != android.text.Selection.PositionIterator.DONE) {
            if (extendSelection) {
                android.text.Selection.extendSelection(text, offset);
            } else {
                android.text.Selection.setSelection(text, offset);
            }
        }
        return true;
    }

    /**
     * {@hide }
     */
    public static boolean moveToFollowing(android.text.Spannable text, android.text.Selection.PositionIterator iter, boolean extendSelection) {
        final int offset = iter.following(android.text.Selection.getSelectionEnd(text));
        if (offset != android.text.Selection.PositionIterator.DONE) {
            if (extendSelection) {
                android.text.Selection.extendSelection(text, offset);
            } else {
                android.text.Selection.setSelection(text, offset);
            }
        }
        return true;
    }

    private static int findEdge(android.text.Spannable text, android.text.Layout layout, int dir) {
        int pt = android.text.Selection.getSelectionEnd(text);
        int line = layout.getLineForOffset(pt);
        int pdir = layout.getParagraphDirection(line);
        if ((dir * pdir) < 0) {
            return layout.getLineStart(line);
        } else {
            int end = layout.getLineEnd(line);
            if (line == (layout.getLineCount() - 1))
                return end;
            else
                return end - 1;

        }
    }

    private static int chooseHorizontal(android.text.Layout layout, int direction, int off1, int off2) {
        int line1 = layout.getLineForOffset(off1);
        int line2 = layout.getLineForOffset(off2);
        if (line1 == line2) {
            // same line, so it goes by pure physical direction
            float h1 = layout.getPrimaryHorizontal(off1);
            float h2 = layout.getPrimaryHorizontal(off2);
            if (direction < 0) {
                // to left
                if (h1 < h2)
                    return off1;
                else
                    return off2;

            } else {
                // to right
                if (h1 > h2)
                    return off1;
                else
                    return off2;

            }
        } else {
            // different line, so which line is "left" and which is "right"
            // depends upon the directionality of the text
            // This only checks at one end, but it's not clear what the
            // right thing to do is if the ends don't agree.  Even if it
            // is wrong it should still not be too bad.
            int line = layout.getLineForOffset(off1);
            int textdir = layout.getParagraphDirection(line);
            if (textdir == direction)
                return java.lang.Math.max(off1, off2);
            else
                return java.lang.Math.min(off1, off2);

        }
    }

    private static final class START implements android.text.NoCopySpan {}

    private static final class END implements android.text.NoCopySpan {}

    /* Public constants */
    public static final java.lang.Object SELECTION_START = new android.text.Selection.START();

    public static final java.lang.Object SELECTION_END = new android.text.Selection.END();
}

