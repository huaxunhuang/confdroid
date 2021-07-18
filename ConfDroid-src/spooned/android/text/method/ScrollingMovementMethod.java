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
package android.text.method;


/**
 * A movement method that interprets movement keys by scrolling the text buffer.
 */
public class ScrollingMovementMethod extends android.text.method.BaseMovementMethod implements android.text.method.MovementMethod {
    @java.lang.Override
    protected boolean left(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollLeft(widget, buffer, 1);
    }

    @java.lang.Override
    protected boolean right(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollRight(widget, buffer, 1);
    }

    @java.lang.Override
    protected boolean up(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollUp(widget, buffer, 1);
    }

    @java.lang.Override
    protected boolean down(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollDown(widget, buffer, 1);
    }

    @java.lang.Override
    protected boolean pageUp(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollPageUp(widget, buffer);
    }

    @java.lang.Override
    protected boolean pageDown(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollPageDown(widget, buffer);
    }

    @java.lang.Override
    protected boolean top(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollTop(widget, buffer);
    }

    @java.lang.Override
    protected boolean bottom(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollBottom(widget, buffer);
    }

    @java.lang.Override
    protected boolean lineStart(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollLineStart(widget, buffer);
    }

    @java.lang.Override
    protected boolean lineEnd(android.widget.TextView widget, android.text.Spannable buffer) {
        return scrollLineEnd(widget, buffer);
    }

    @java.lang.Override
    protected boolean home(android.widget.TextView widget, android.text.Spannable buffer) {
        return top(widget, buffer);
    }

    @java.lang.Override
    protected boolean end(android.widget.TextView widget, android.text.Spannable buffer) {
        return bottom(widget, buffer);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        return android.text.method.Touch.onTouchEvent(widget, buffer, event);
    }

    @java.lang.Override
    public void onTakeFocus(android.widget.TextView widget, android.text.Spannable text, int dir) {
        android.text.Layout layout = widget.getLayout();
        if ((layout != null) && ((dir & android.view.View.FOCUS_FORWARD) != 0)) {
            widget.scrollTo(widget.getScrollX(), layout.getLineTop(0));
        }
        if ((layout != null) && ((dir & android.view.View.FOCUS_BACKWARD) != 0)) {
            int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
            int line = layout.getLineCount() - 1;
            widget.scrollTo(widget.getScrollX(), layout.getLineTop(line + 1) - (widget.getHeight() - padding));
        }
    }

    public static android.text.method.MovementMethod getInstance() {
        if (android.text.method.ScrollingMovementMethod.sInstance == null)
            android.text.method.ScrollingMovementMethod.sInstance = new android.text.method.ScrollingMovementMethod();

        return android.text.method.ScrollingMovementMethod.sInstance;
    }

    private static android.text.method.ScrollingMovementMethod sInstance;
}

