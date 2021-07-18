/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.widget;


/**
 * This class contains the implementation of text segment iterators
 * for accessibility support.
 */
final class AccessibilityIterators {
    static class LineTextSegmentIterator extends android.view.AccessibilityIterators.AbstractTextSegmentIterator {
        private static android.widget.AccessibilityIterators.LineTextSegmentIterator sLineInstance;

        protected static final int DIRECTION_START = -1;

        protected static final int DIRECTION_END = 1;

        protected android.text.Layout mLayout;

        public static android.widget.AccessibilityIterators.LineTextSegmentIterator getInstance() {
            if (android.widget.AccessibilityIterators.LineTextSegmentIterator.sLineInstance == null) {
                android.widget.AccessibilityIterators.LineTextSegmentIterator.sLineInstance = new android.widget.AccessibilityIterators.LineTextSegmentIterator();
            }
            return android.widget.AccessibilityIterators.LineTextSegmentIterator.sLineInstance;
        }

        public void initialize(android.text.Spannable text, android.text.Layout layout) {
            mText = text.toString();
            mLayout = layout;
        }

        @java.lang.Override
        public int[] following(int offset) {
            final int textLegth = mText.length();
            if (textLegth <= 0) {
                return null;
            }
            if (offset >= mText.length()) {
                return null;
            }
            int nextLine;
            if (offset < 0) {
                nextLine = mLayout.getLineForOffset(0);
            } else {
                final int currentLine = mLayout.getLineForOffset(offset);
                if (getLineEdgeIndex(currentLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_START) == offset) {
                    nextLine = currentLine;
                } else {
                    nextLine = currentLine + 1;
                }
            }
            if (nextLine >= mLayout.getLineCount()) {
                return null;
            }
            final int start = getLineEdgeIndex(nextLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_START);
            final int end = getLineEdgeIndex(nextLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_END) + 1;
            return getRange(start, end);
        }

        @java.lang.Override
        public int[] preceding(int offset) {
            final int textLegth = mText.length();
            if (textLegth <= 0) {
                return null;
            }
            if (offset <= 0) {
                return null;
            }
            int previousLine;
            if (offset > mText.length()) {
                previousLine = mLayout.getLineForOffset(mText.length());
            } else {
                final int currentLine = mLayout.getLineForOffset(offset);
                if ((getLineEdgeIndex(currentLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_END) + 1) == offset) {
                    previousLine = currentLine;
                } else {
                    previousLine = currentLine - 1;
                }
            }
            if (previousLine < 0) {
                return null;
            }
            final int start = getLineEdgeIndex(previousLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_START);
            final int end = getLineEdgeIndex(previousLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_END) + 1;
            return getRange(start, end);
        }

        protected int getLineEdgeIndex(int lineNumber, int direction) {
            final int paragraphDirection = mLayout.getParagraphDirection(lineNumber);
            if ((direction * paragraphDirection) < 0) {
                return mLayout.getLineStart(lineNumber);
            } else {
                return mLayout.getLineEnd(lineNumber) - 1;
            }
        }
    }

    static class PageTextSegmentIterator extends android.widget.AccessibilityIterators.LineTextSegmentIterator {
        private static android.widget.AccessibilityIterators.PageTextSegmentIterator sPageInstance;

        private android.widget.TextView mView;

        private final android.graphics.Rect mTempRect = new android.graphics.Rect();

        public static android.widget.AccessibilityIterators.PageTextSegmentIterator getInstance() {
            if (android.widget.AccessibilityIterators.PageTextSegmentIterator.sPageInstance == null) {
                android.widget.AccessibilityIterators.PageTextSegmentIterator.sPageInstance = new android.widget.AccessibilityIterators.PageTextSegmentIterator();
            }
            return android.widget.AccessibilityIterators.PageTextSegmentIterator.sPageInstance;
        }

        public void initialize(android.widget.TextView view) {
            super.initialize(((android.text.Spannable) (view.getIterableTextForAccessibility())), view.getLayout());
            mView = view;
        }

        @java.lang.Override
        public int[] following(int offset) {
            final int textLength = mText.length();
            if (textLength <= 0) {
                return null;
            }
            if (offset >= mText.length()) {
                return null;
            }
            if (!mView.getGlobalVisibleRect(mTempRect)) {
                return null;
            }
            final int start = java.lang.Math.max(0, offset);
            final int currentLine = mLayout.getLineForOffset(start);
            final int currentLineTop = mLayout.getLineTop(currentLine);
            final int pageHeight = (mTempRect.height() - mView.getTotalPaddingTop()) - mView.getTotalPaddingBottom();
            final int nextPageStartY = currentLineTop + pageHeight;
            final int lastLineTop = mLayout.getLineTop(mLayout.getLineCount() - 1);
            final int currentPageEndLine = (nextPageStartY < lastLineTop) ? mLayout.getLineForVertical(nextPageStartY) - 1 : mLayout.getLineCount() - 1;
            final int end = getLineEdgeIndex(currentPageEndLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_END) + 1;
            return getRange(start, end);
        }

        @java.lang.Override
        public int[] preceding(int offset) {
            final int textLength = mText.length();
            if (textLength <= 0) {
                return null;
            }
            if (offset <= 0) {
                return null;
            }
            if (!mView.getGlobalVisibleRect(mTempRect)) {
                return null;
            }
            final int end = java.lang.Math.min(mText.length(), offset);
            final int currentLine = mLayout.getLineForOffset(end);
            final int currentLineTop = mLayout.getLineTop(currentLine);
            final int pageHeight = (mTempRect.height() - mView.getTotalPaddingTop()) - mView.getTotalPaddingBottom();
            final int previousPageEndY = currentLineTop - pageHeight;
            int currentPageStartLine = (previousPageEndY > 0) ? mLayout.getLineForVertical(previousPageEndY) : 0;
            // If we're at the end of text, we're at the end of the current line rather than the
            // start of the next line, so we should move up one fewer lines than we would otherwise.
            if ((end == mText.length()) && (currentPageStartLine < currentLine)) {
                currentPageStartLine += 1;
            }
            final int start = getLineEdgeIndex(currentPageStartLine, android.widget.AccessibilityIterators.LineTextSegmentIterator.DIRECTION_START);
            return getRange(start, end);
        }
    }
}

