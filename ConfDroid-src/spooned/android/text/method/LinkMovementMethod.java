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
 * A movement method that traverses links in the text buffer and scrolls if necessary.
 * Supports clicking on links with DPad Center or Enter.
 */
public class LinkMovementMethod extends android.text.method.ScrollingMovementMethod {
    private static final int CLICK = 1;

    private static final int UP = 2;

    private static final int DOWN = 3;

    @java.lang.Override
    public boolean canSelectArbitrarily() {
        return true;
    }

    @java.lang.Override
    protected boolean handleMovementKey(android.widget.TextView widget, android.text.Spannable buffer, int keyCode, int movementMetaState, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
            case android.view.KeyEvent.KEYCODE_ENTER :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    if (((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) && action(android.text.method.LinkMovementMethod.CLICK, widget, buffer)) {
                        return true;
                    }
                }
                break;
        }
        return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
    }

    @java.lang.Override
    protected boolean up(android.widget.TextView widget, android.text.Spannable buffer) {
        if (action(android.text.method.LinkMovementMethod.UP, widget, buffer)) {
            return true;
        }
        return super.up(widget, buffer);
    }

    @java.lang.Override
    protected boolean down(android.widget.TextView widget, android.text.Spannable buffer) {
        if (action(android.text.method.LinkMovementMethod.DOWN, widget, buffer)) {
            return true;
        }
        return super.down(widget, buffer);
    }

    @java.lang.Override
    protected boolean left(android.widget.TextView widget, android.text.Spannable buffer) {
        if (action(android.text.method.LinkMovementMethod.UP, widget, buffer)) {
            return true;
        }
        return super.left(widget, buffer);
    }

    @java.lang.Override
    protected boolean right(android.widget.TextView widget, android.text.Spannable buffer) {
        if (action(android.text.method.LinkMovementMethod.DOWN, widget, buffer)) {
            return true;
        }
        return super.right(widget, buffer);
    }

    private boolean action(int what, android.widget.TextView widget, android.text.Spannable buffer) {
        android.text.Layout layout = widget.getLayout();
        int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
        int areatop = widget.getScrollY();
        int areabot = (areatop + widget.getHeight()) - padding;
        int linetop = layout.getLineForVertical(areatop);
        int linebot = layout.getLineForVertical(areabot);
        int first = layout.getLineStart(linetop);
        int last = layout.getLineEnd(linebot);
        android.text.style.ClickableSpan[] candidates = buffer.getSpans(first, last, android.text.style.ClickableSpan.class);
        int a = android.text.Selection.getSelectionStart(buffer);
        int b = android.text.Selection.getSelectionEnd(buffer);
        int selStart = java.lang.Math.min(a, b);
        int selEnd = java.lang.Math.max(a, b);
        if (selStart < 0) {
            if (buffer.getSpanStart(android.text.method.LinkMovementMethod.FROM_BELOW) >= 0) {
                selStart = selEnd = buffer.length();
            }
        }
        if (selStart > last)
            selStart = selEnd = java.lang.Integer.MAX_VALUE;

        if (selEnd < first)
            selStart = selEnd = -1;

        switch (what) {
            case android.text.method.LinkMovementMethod.CLICK :
                if (selStart == selEnd) {
                    return false;
                }
                android.text.style.ClickableSpan[] link = buffer.getSpans(selStart, selEnd, android.text.style.ClickableSpan.class);
                if (link.length != 1)
                    return false;

                link[0].onClick(widget);
                break;
            case android.text.method.LinkMovementMethod.UP :
                int beststart;
                int bestend;
                beststart = -1;
                bestend = -1;
                for (int i = 0; i < candidates.length; i++) {
                    int end = buffer.getSpanEnd(candidates[i]);
                    if ((end < selEnd) || (selStart == selEnd)) {
                        if (end > bestend) {
                            beststart = buffer.getSpanStart(candidates[i]);
                            bestend = end;
                        }
                    }
                }
                if (beststart >= 0) {
                    android.text.Selection.setSelection(buffer, bestend, beststart);
                    return true;
                }
                break;
            case android.text.method.LinkMovementMethod.DOWN :
                beststart = java.lang.Integer.MAX_VALUE;
                bestend = java.lang.Integer.MAX_VALUE;
                for (int i = 0; i < candidates.length; i++) {
                    int start = buffer.getSpanStart(candidates[i]);
                    if ((start > selStart) || (selStart == selEnd)) {
                        if (start < beststart) {
                            beststart = start;
                            bestend = buffer.getSpanEnd(candidates[i]);
                        }
                    }
                }
                if (bestend < java.lang.Integer.MAX_VALUE) {
                    android.text.Selection.setSelection(buffer, beststart, bestend);
                    return true;
                }
                break;
        }
        return false;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        int action = event.getAction();
        if ((action == android.view.MotionEvent.ACTION_UP) || (action == android.view.MotionEvent.ACTION_DOWN)) {
            int x = ((int) (event.getX()));
            int y = ((int) (event.getY()));
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            android.text.Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            android.text.style.ClickableSpan[] link = buffer.getSpans(off, off, android.text.style.ClickableSpan.class);
            if (link.length != 0) {
                if (action == android.view.MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else
                    if (action == android.view.MotionEvent.ACTION_DOWN) {
                        android.text.Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }

                return true;
            } else {
                android.text.Selection.removeSelection(buffer);
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    @java.lang.Override
    public void initialize(android.widget.TextView widget, android.text.Spannable text) {
        android.text.Selection.removeSelection(text);
        text.removeSpan(android.text.method.LinkMovementMethod.FROM_BELOW);
    }

    @java.lang.Override
    public void onTakeFocus(android.widget.TextView view, android.text.Spannable text, int dir) {
        android.text.Selection.removeSelection(text);
        if ((dir & android.view.View.FOCUS_BACKWARD) != 0) {
            text.setSpan(android.text.method.LinkMovementMethod.FROM_BELOW, 0, 0, android.text.Spannable.SPAN_POINT_POINT);
        } else {
            text.removeSpan(android.text.method.LinkMovementMethod.FROM_BELOW);
        }
    }

    public static android.text.method.MovementMethod getInstance() {
        if (android.text.method.LinkMovementMethod.sInstance == null)
            android.text.method.LinkMovementMethod.sInstance = new android.text.method.LinkMovementMethod();

        return android.text.method.LinkMovementMethod.sInstance;
    }

    private static android.text.method.LinkMovementMethod sInstance;

    private static java.lang.Object FROM_BELOW = new android.text.NoCopySpan.Concrete();
}

