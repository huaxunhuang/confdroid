/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Base classes for movement methods.
 */
public class BaseMovementMethod implements android.text.method.MovementMethod {
    @java.lang.Override
    public boolean canSelectArbitrarily() {
        return false;
    }

    @java.lang.Override
    public void initialize(android.widget.TextView widget, android.text.Spannable text) {
    }

    @java.lang.Override
    public boolean onKeyDown(android.widget.TextView widget, android.text.Spannable text, int keyCode, android.view.KeyEvent event) {
        final int movementMetaState = getMovementMetaState(text, event);
        boolean handled = handleMovementKey(widget, text, keyCode, movementMetaState, event);
        if (handled) {
            android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(text);
            android.text.method.MetaKeyKeyListener.resetLockedMeta(text);
        }
        return handled;
    }

    @java.lang.Override
    public boolean onKeyOther(android.widget.TextView widget, android.text.Spannable text, android.view.KeyEvent event) {
        final int movementMetaState = getMovementMetaState(text, event);
        final int keyCode = event.getKeyCode();
        if ((keyCode != android.view.KeyEvent.KEYCODE_UNKNOWN) && (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)) {
            final int repeat = event.getRepeatCount();
            boolean handled = false;
            for (int i = 0; i < repeat; i++) {
                if (!handleMovementKey(widget, text, keyCode, movementMetaState, event)) {
                    break;
                }
                handled = true;
            }
            if (handled) {
                android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(text);
                android.text.method.MetaKeyKeyListener.resetLockedMeta(text);
            }
            return handled;
        }
        return false;
    }

    @java.lang.Override
    public boolean onKeyUp(android.widget.TextView widget, android.text.Spannable text, int keyCode, android.view.KeyEvent event) {
        return false;
    }

    @java.lang.Override
    public void onTakeFocus(android.widget.TextView widget, android.text.Spannable text, int direction) {
    }

    @java.lang.Override
    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable text, android.view.MotionEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean onTrackballEvent(android.widget.TextView widget, android.text.Spannable text, android.view.MotionEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean onGenericMotionEvent(android.widget.TextView widget, android.text.Spannable text, android.view.MotionEvent event) {
        if ((event.getSource() & android.view.InputDevice.SOURCE_CLASS_POINTER) != 0) {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_SCROLL :
                    {
                        final float vscroll;
                        final float hscroll;
                        if ((event.getMetaState() & android.view.KeyEvent.META_SHIFT_ON) != 0) {
                            vscroll = 0;
                            hscroll = event.getAxisValue(android.view.MotionEvent.AXIS_VSCROLL);
                        } else {
                            vscroll = -event.getAxisValue(android.view.MotionEvent.AXIS_VSCROLL);
                            hscroll = event.getAxisValue(android.view.MotionEvent.AXIS_HSCROLL);
                        }
                        boolean handled = false;
                        if (hscroll < 0) {
                            handled |= scrollLeft(widget, text, ((int) (java.lang.Math.ceil(-hscroll))));
                        } else
                            if (hscroll > 0) {
                                handled |= scrollRight(widget, text, ((int) (java.lang.Math.ceil(hscroll))));
                            }

                        if (vscroll < 0) {
                            handled |= scrollUp(widget, text, ((int) (java.lang.Math.ceil(-vscroll))));
                        } else
                            if (vscroll > 0) {
                                handled |= scrollDown(widget, text, ((int) (java.lang.Math.ceil(vscroll))));
                            }

                        return handled;
                    }
            }
        }
        return false;
    }

    /**
     * Gets the meta state used for movement using the modifiers tracked by the text
     * buffer as well as those present in the key event.
     *
     * The movement meta state excludes the state of locked modifiers or the SHIFT key
     * since they are not used by movement actions (but they may be used for selection).
     *
     * @param buffer
     * 		The text buffer.
     * @param event
     * 		The key event.
     * @return The keyboard meta states used for movement.
     */
    protected int getMovementMetaState(android.text.Spannable buffer, android.view.KeyEvent event) {
        // We ignore locked modifiers and SHIFT.
        int metaState = android.text.method.MetaKeyKeyListener.getMetaState(buffer, event) & (~(android.text.method.MetaKeyKeyListener.META_ALT_LOCKED | android.text.method.MetaKeyKeyListener.META_SYM_LOCKED));
        return android.view.KeyEvent.normalizeMetaState(metaState) & (~android.view.KeyEvent.META_SHIFT_MASK);
    }

    /**
     * Performs a movement key action.
     * The default implementation decodes the key down and invokes movement actions
     * such as {@link #down} and {@link #up}.
     * {@link #onKeyDown(TextView, Spannable, int, KeyEvent)} calls this method once
     * to handle an {@link KeyEvent#ACTION_DOWN}.
     * {@link #onKeyOther(TextView, Spannable, KeyEvent)} calls this method repeatedly
     * to handle each repetition of an {@link KeyEvent#ACTION_MULTIPLE}.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @param event
     * 		The key event.
     * @param keyCode
     * 		The key code.
     * @param movementMetaState
     * 		The keyboard meta states used for movement.
     * @param event
     * 		The key event.
     * @return True if the event was handled.
     */
    protected boolean handleMovementKey(android.widget.TextView widget, android.text.Spannable buffer, int keyCode, int movementMetaState, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return left(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_CTRL_ON)) {
                        return leftWord(widget, buffer);
                    } else
                        if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_ALT_ON)) {
                            return lineStart(widget, buffer);
                        }


                break;
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return right(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_CTRL_ON)) {
                        return rightWord(widget, buffer);
                    } else
                        if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_ALT_ON)) {
                            return lineEnd(widget, buffer);
                        }


                break;
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return up(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_ALT_ON)) {
                        return top(widget, buffer);
                    }

                break;
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return down(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_ALT_ON)) {
                        return bottom(widget, buffer);
                    }

                break;
            case android.view.KeyEvent.KEYCODE_PAGE_UP :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return pageUp(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_ALT_ON)) {
                        return top(widget, buffer);
                    }

                break;
            case android.view.KeyEvent.KEYCODE_PAGE_DOWN :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return pageDown(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_ALT_ON)) {
                        return bottom(widget, buffer);
                    }

                break;
            case android.view.KeyEvent.KEYCODE_MOVE_HOME :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return home(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_CTRL_ON)) {
                        return top(widget, buffer);
                    }

                break;
            case android.view.KeyEvent.KEYCODE_MOVE_END :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return end(widget, buffer);
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(movementMetaState, android.view.KeyEvent.META_CTRL_ON)) {
                        return bottom(widget, buffer);
                    }

                break;
        }
        return false;
    }

    /**
     * Performs a left movement action.
     * Moves the cursor or scrolls left by one character.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean left(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a right movement action.
     * Moves the cursor or scrolls right by one character.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean right(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs an up movement action.
     * Moves the cursor or scrolls up by one line.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean up(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a down movement action.
     * Moves the cursor or scrolls down by one line.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean down(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a page-up movement action.
     * Moves the cursor or scrolls up by one page.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean pageUp(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a page-down movement action.
     * Moves the cursor or scrolls down by one page.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean pageDown(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a top movement action.
     * Moves the cursor or scrolls to the top of the buffer.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean top(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a bottom movement action.
     * Moves the cursor or scrolls to the bottom of the buffer.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean bottom(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a line-start movement action.
     * Moves the cursor or scrolls to the start of the line.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean lineStart(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a line-end movement action.
     * Moves the cursor or scrolls to the end of the line.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean lineEnd(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * {@hide }
     */
    protected boolean leftWord(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * {@hide }
     */
    protected boolean rightWord(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs a home movement action.
     * Moves the cursor or scrolls to the start of the line or to the top of the
     * document depending on whether the insertion point is being moved or
     * the document is being scrolled.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean home(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    /**
     * Performs an end movement action.
     * Moves the cursor or scrolls to the start of the line or to the top of the
     * document depending on whether the insertion point is being moved or
     * the document is being scrolled.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     */
    protected boolean end(android.widget.TextView widget, android.text.Spannable buffer) {
        return false;
    }

    private int getTopLine(android.widget.TextView widget) {
        return widget.getLayout().getLineForVertical(widget.getScrollY());
    }

    private int getBottomLine(android.widget.TextView widget) {
        return widget.getLayout().getLineForVertical(widget.getScrollY() + getInnerHeight(widget));
    }

    private int getInnerWidth(android.widget.TextView widget) {
        return (widget.getWidth() - widget.getTotalPaddingLeft()) - widget.getTotalPaddingRight();
    }

    private int getInnerHeight(android.widget.TextView widget) {
        return (widget.getHeight() - widget.getTotalPaddingTop()) - widget.getTotalPaddingBottom();
    }

    private int getCharacterWidth(android.widget.TextView widget) {
        return ((int) (java.lang.Math.ceil(widget.getPaint().getFontSpacing())));
    }

    private int getScrollBoundsLeft(android.widget.TextView widget) {
        final android.text.Layout layout = widget.getLayout();
        final int topLine = getTopLine(widget);
        final int bottomLine = getBottomLine(widget);
        if (topLine > bottomLine) {
            return 0;
        }
        int left = java.lang.Integer.MAX_VALUE;
        for (int line = topLine; line <= bottomLine; line++) {
            final int lineLeft = ((int) (java.lang.Math.floor(layout.getLineLeft(line))));
            if (lineLeft < left) {
                left = lineLeft;
            }
        }
        return left;
    }

    private int getScrollBoundsRight(android.widget.TextView widget) {
        final android.text.Layout layout = widget.getLayout();
        final int topLine = getTopLine(widget);
        final int bottomLine = getBottomLine(widget);
        if (topLine > bottomLine) {
            return 0;
        }
        int right = java.lang.Integer.MIN_VALUE;
        for (int line = topLine; line <= bottomLine; line++) {
            final int lineRight = ((int) (java.lang.Math.ceil(layout.getLineRight(line))));
            if (lineRight > right) {
                right = lineRight;
            }
        }
        return right;
    }

    /**
     * Performs a scroll left action.
     * Scrolls left by the specified number of characters.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @param amount
     * 		The number of characters to scroll by.  Must be at least 1.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollLeft(android.widget.TextView widget, android.text.Spannable buffer, int amount) {
        final int minScrollX = getScrollBoundsLeft(widget);
        int scrollX = widget.getScrollX();
        if (scrollX > minScrollX) {
            scrollX = java.lang.Math.max(scrollX - (getCharacterWidth(widget) * amount), minScrollX);
            widget.scrollTo(scrollX, widget.getScrollY());
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll right action.
     * Scrolls right by the specified number of characters.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @param amount
     * 		The number of characters to scroll by.  Must be at least 1.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollRight(android.widget.TextView widget, android.text.Spannable buffer, int amount) {
        final int maxScrollX = getScrollBoundsRight(widget) - getInnerWidth(widget);
        int scrollX = widget.getScrollX();
        if (scrollX < maxScrollX) {
            scrollX = java.lang.Math.min(scrollX + (getCharacterWidth(widget) * amount), maxScrollX);
            widget.scrollTo(scrollX, widget.getScrollY());
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll up action.
     * Scrolls up by the specified number of lines.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @param amount
     * 		The number of lines to scroll by.  Must be at least 1.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollUp(android.widget.TextView widget, android.text.Spannable buffer, int amount) {
        final android.text.Layout layout = widget.getLayout();
        final int top = widget.getScrollY();
        int topLine = layout.getLineForVertical(top);
        if (layout.getLineTop(topLine) == top) {
            // If the top line is partially visible, bring it all the way
            // into view; otherwise, bring the previous line into view.
            topLine -= 1;
        }
        if (topLine >= 0) {
            topLine = java.lang.Math.max((topLine - amount) + 1, 0);
            android.text.method.Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(topLine));
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll down action.
     * Scrolls down by the specified number of lines.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @param amount
     * 		The number of lines to scroll by.  Must be at least 1.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollDown(android.widget.TextView widget, android.text.Spannable buffer, int amount) {
        final android.text.Layout layout = widget.getLayout();
        final int innerHeight = getInnerHeight(widget);
        final int bottom = widget.getScrollY() + innerHeight;
        int bottomLine = layout.getLineForVertical(bottom);
        if (layout.getLineTop(bottomLine + 1) < (bottom + 1)) {
            // Less than a pixel of this line is out of view,
            // so we must have tried to make it entirely in view
            // and now want the next line to be in view instead.
            bottomLine += 1;
        }
        final int limit = layout.getLineCount() - 1;
        if (bottomLine <= limit) {
            bottomLine = java.lang.Math.min((bottomLine + amount) - 1, limit);
            android.text.method.Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(bottomLine + 1) - innerHeight);
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll page up action.
     * Scrolls up by one page.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollPageUp(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        final int top = widget.getScrollY() - getInnerHeight(widget);
        int topLine = layout.getLineForVertical(top);
        if (topLine >= 0) {
            android.text.method.Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(topLine));
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll page up action.
     * Scrolls down by one page.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollPageDown(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        final int innerHeight = getInnerHeight(widget);
        final int bottom = (widget.getScrollY() + innerHeight) + innerHeight;
        int bottomLine = layout.getLineForVertical(bottom);
        if (bottomLine <= (layout.getLineCount() - 1)) {
            android.text.method.Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(bottomLine + 1) - innerHeight);
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll to top action.
     * Scrolls to the top of the document.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollTop(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (getTopLine(widget) >= 0) {
            android.text.method.Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(0));
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll to bottom action.
     * Scrolls to the bottom of the document.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollBottom(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        final int lineCount = layout.getLineCount();
        if (getBottomLine(widget) <= (lineCount - 1)) {
            android.text.method.Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(lineCount) - getInnerHeight(widget));
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll to line start action.
     * Scrolls to the start of the line.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollLineStart(android.widget.TextView widget, android.text.Spannable buffer) {
        final int minScrollX = getScrollBoundsLeft(widget);
        int scrollX = widget.getScrollX();
        if (scrollX > minScrollX) {
            widget.scrollTo(minScrollX, widget.getScrollY());
            return true;
        }
        return false;
    }

    /**
     * Performs a scroll to line end action.
     * Scrolls to the end of the line.
     *
     * @param widget
     * 		The text view.
     * @param buffer
     * 		The text buffer.
     * @return True if the event was handled.
     * @unknown 
     */
    protected boolean scrollLineEnd(android.widget.TextView widget, android.text.Spannable buffer) {
        final int maxScrollX = getScrollBoundsRight(widget) - getInnerWidth(widget);
        int scrollX = widget.getScrollX();
        if (scrollX < maxScrollX) {
            widget.scrollTo(maxScrollX, widget.getScrollY());
            return true;
        }
        return false;
    }
}

