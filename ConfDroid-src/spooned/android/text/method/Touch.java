/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.text.method;


public class Touch {
    private Touch() {
    }

    /**
     * Scrolls the specified widget to the specified coordinates, except
     * constrains the X scrolling position to the horizontal regions of
     * the text that will be visible after scrolling to the specified
     * Y position.
     */
    public static void scrollTo(android.widget.TextView widget, android.text.Layout layout, int x, int y) {
        final int horizontalPadding = widget.getTotalPaddingLeft() + widget.getTotalPaddingRight();
        final int availableWidth = widget.getWidth() - horizontalPadding;
        final int top = layout.getLineForVertical(y);
        android.text.Layout.Alignment a = layout.getParagraphAlignment(top);
        boolean ltr = layout.getParagraphDirection(top) > 0;
        int left;
        int right;
        if (widget.getHorizontallyScrolling()) {
            final int verticalPadding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
            final int bottom = layout.getLineForVertical((y + widget.getHeight()) - verticalPadding);
            left = java.lang.Integer.MAX_VALUE;
            right = 0;
            for (int i = top; i <= bottom; i++) {
                left = ((int) (java.lang.Math.min(left, layout.getLineLeft(i))));
                right = ((int) (java.lang.Math.max(right, layout.getLineRight(i))));
            }
        } else {
            left = 0;
            right = availableWidth;
        }
        final int actualWidth = right - left;
        if (actualWidth < availableWidth) {
            if (a == android.text.Layout.Alignment.ALIGN_CENTER) {
                x = left - ((availableWidth - actualWidth) / 2);
            } else
                if (((ltr && (a == android.text.Layout.Alignment.ALIGN_OPPOSITE)) || ((!ltr) && (a == android.text.Layout.Alignment.ALIGN_NORMAL))) || (a == android.text.Layout.Alignment.ALIGN_RIGHT)) {
                    // align_opposite does NOT mean align_right, we need the paragraph
                    // direction to resolve it to left or right
                    x = left - (availableWidth - actualWidth);
                } else {
                    x = left;
                }

        } else {
            x = java.lang.Math.min(x, right - availableWidth);
            x = java.lang.Math.max(x, left);
        }
        widget.scrollTo(x, y);
    }

    /**
     * Handles touch events for dragging.  You may want to do other actions
     * like moving the cursor on touch as well.
     */
    public static boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        android.text.method.Touch.DragState[] ds;
        switch (event.getActionMasked()) {
            case android.view.MotionEvent.ACTION_DOWN :
                ds = buffer.getSpans(0, buffer.length(), android.text.method.Touch.DragState.class);
                for (int i = 0; i < ds.length; i++) {
                    buffer.removeSpan(ds[i]);
                }
                buffer.setSpan(new android.text.method.Touch.DragState(event.getX(), event.getY(), widget.getScrollX(), widget.getScrollY()), 0, 0, android.text.Spannable.SPAN_MARK_MARK);
                return true;
            case android.view.MotionEvent.ACTION_UP :
                ds = buffer.getSpans(0, buffer.length(), android.text.method.Touch.DragState.class);
                for (int i = 0; i < ds.length; i++) {
                    buffer.removeSpan(ds[i]);
                }
                if ((ds.length > 0) && ds[0].mUsed) {
                    return true;
                } else {
                    return false;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                ds = buffer.getSpans(0, buffer.length(), android.text.method.Touch.DragState.class);
                if (ds.length > 0) {
                    if (ds[0].mFarEnough == false) {
                        int slop = android.view.ViewConfiguration.get(widget.getContext()).getScaledTouchSlop();
                        if ((java.lang.Math.abs(event.getX() - ds[0].mX) >= slop) || (java.lang.Math.abs(event.getY() - ds[0].mY) >= slop)) {
                            ds[0].mFarEnough = true;
                        }
                    }
                    if (ds[0].mFarEnough) {
                        ds[0].mUsed = true;
                        boolean cap = (((event.getMetaState() & android.view.KeyEvent.META_SHIFT_ON) != 0) || (android.text.method.MetaKeyKeyListener.getMetaState(buffer, android.text.method.MetaKeyKeyListener.META_SHIFT_ON) == 1)) || (android.text.method.MetaKeyKeyListener.getMetaState(buffer, android.text.method.MetaKeyKeyListener.META_SELECTING) != 0);
                        float dx;
                        float dy;
                        if (cap) {
                            // if we're selecting, we want the scroll to go in
                            // the direction of the drag
                            dx = event.getX() - ds[0].mX;
                            dy = event.getY() - ds[0].mY;
                        } else {
                            dx = ds[0].mX - event.getX();
                            dy = ds[0].mY - event.getY();
                        }
                        ds[0].mX = event.getX();
                        ds[0].mY = event.getY();
                        int nx = widget.getScrollX() + ((int) (dx));
                        int ny = widget.getScrollY() + ((int) (dy));
                        int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
                        android.text.Layout layout = widget.getLayout();
                        ny = java.lang.Math.min(ny, layout.getHeight() - (widget.getHeight() - padding));
                        ny = java.lang.Math.max(ny, 0);
                        int oldX = widget.getScrollX();
                        int oldY = widget.getScrollY();
                        android.text.method.Touch.scrollTo(widget, layout, nx, ny);
                        // If we actually scrolled, then cancel the up action.
                        if ((oldX != widget.getScrollX()) || (oldY != widget.getScrollY())) {
                            widget.cancelLongPress();
                        }
                        return true;
                    }
                }
        }
        return false;
    }

    /**
     *
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     */
    public static int getInitialScrollX(android.widget.TextView widget, android.text.Spannable buffer) {
        android.text.method.Touch.DragState[] ds = buffer.getSpans(0, buffer.length(), android.text.method.Touch.DragState.class);
        return ds.length > 0 ? ds[0].mScrollX : -1;
    }

    /**
     *
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     */
    public static int getInitialScrollY(android.widget.TextView widget, android.text.Spannable buffer) {
        android.text.method.Touch.DragState[] ds = buffer.getSpans(0, buffer.length(), android.text.method.Touch.DragState.class);
        return ds.length > 0 ? ds[0].mScrollY : -1;
    }

    private static class DragState implements android.text.NoCopySpan {
        public float mX;

        public float mY;

        public int mScrollX;

        public int mScrollY;

        public boolean mFarEnough;

        public boolean mUsed;

        public DragState(float x, float y, int scrollX, int scrollY) {
            mX = x;
            mY = y;
            mScrollX = scrollX;
            mScrollY = scrollY;
        }
    }
}

