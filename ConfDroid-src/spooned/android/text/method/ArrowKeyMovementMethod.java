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
 * A movement method that provides cursor movement and selection.
 * Supports displaying the context menu on DPad Center.
 */
public class ArrowKeyMovementMethod extends android.text.method.BaseMovementMethod implements android.text.method.MovementMethod {
    private static boolean isSelecting(android.text.Spannable buffer) {
        return (android.text.method.MetaKeyKeyListener.getMetaState(buffer, android.text.method.MetaKeyKeyListener.META_SHIFT_ON) == 1) || (android.text.method.MetaKeyKeyListener.getMetaState(buffer, android.text.method.MetaKeyKeyListener.META_SELECTING) != 0);
    }

    private static int getCurrentLineTop(android.text.Spannable buffer, android.text.Layout layout) {
        return layout.getLineTop(layout.getLineForOffset(android.text.Selection.getSelectionEnd(buffer)));
    }

    private static int getPageHeight(android.widget.TextView widget) {
        // This calculation does not take into account the view transformations that
        // may have been applied to the child or its containers.  In case of scaling or
        // rotation, the calculated page height may be incorrect.
        final android.graphics.Rect rect = new android.graphics.Rect();
        return widget.getGlobalVisibleRect(rect) ? rect.height() : 0;
    }

    @java.lang.Override
    protected boolean handleMovementKey(android.widget.TextView widget, android.text.Spannable buffer, int keyCode, int movementMetaState, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                if (android.view.KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    if (((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) && (android.text.method.MetaKeyKeyListener.getMetaState(buffer, android.text.method.MetaKeyKeyListener.META_SELECTING, event) != 0)) {
                        return widget.showContextMenu();
                    }
                }
                break;
        }
        return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
    }

    @java.lang.Override
    protected boolean left(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            return android.text.Selection.extendLeft(buffer, layout);
        } else {
            return android.text.Selection.moveLeft(buffer, layout);
        }
    }

    @java.lang.Override
    protected boolean right(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            return android.text.Selection.extendRight(buffer, layout);
        } else {
            return android.text.Selection.moveRight(buffer, layout);
        }
    }

    @java.lang.Override
    protected boolean up(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            return android.text.Selection.extendUp(buffer, layout);
        } else {
            return android.text.Selection.moveUp(buffer, layout);
        }
    }

    @java.lang.Override
    protected boolean down(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            return android.text.Selection.extendDown(buffer, layout);
        } else {
            return android.text.Selection.moveDown(buffer, layout);
        }
    }

    @java.lang.Override
    protected boolean pageUp(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        final boolean selecting = android.text.method.ArrowKeyMovementMethod.isSelecting(buffer);
        final int targetY = android.text.method.ArrowKeyMovementMethod.getCurrentLineTop(buffer, layout) - android.text.method.ArrowKeyMovementMethod.getPageHeight(widget);
        boolean handled = false;
        for (; ;) {
            final int previousSelectionEnd = android.text.Selection.getSelectionEnd(buffer);
            if (selecting) {
                android.text.Selection.extendUp(buffer, layout);
            } else {
                android.text.Selection.moveUp(buffer, layout);
            }
            if (android.text.Selection.getSelectionEnd(buffer) == previousSelectionEnd) {
                break;
            }
            handled = true;
            if (android.text.method.ArrowKeyMovementMethod.getCurrentLineTop(buffer, layout) <= targetY) {
                break;
            }
        }
        return handled;
    }

    @java.lang.Override
    protected boolean pageDown(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        final boolean selecting = android.text.method.ArrowKeyMovementMethod.isSelecting(buffer);
        final int targetY = android.text.method.ArrowKeyMovementMethod.getCurrentLineTop(buffer, layout) + android.text.method.ArrowKeyMovementMethod.getPageHeight(widget);
        boolean handled = false;
        for (; ;) {
            final int previousSelectionEnd = android.text.Selection.getSelectionEnd(buffer);
            if (selecting) {
                android.text.Selection.extendDown(buffer, layout);
            } else {
                android.text.Selection.moveDown(buffer, layout);
            }
            if (android.text.Selection.getSelectionEnd(buffer) == previousSelectionEnd) {
                break;
            }
            handled = true;
            if (android.text.method.ArrowKeyMovementMethod.getCurrentLineTop(buffer, layout) >= targetY) {
                break;
            }
        }
        return handled;
    }

    @java.lang.Override
    protected boolean top(android.widget.TextView widget, android.text.Spannable buffer) {
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            android.text.Selection.extendSelection(buffer, 0);
        } else {
            android.text.Selection.setSelection(buffer, 0);
        }
        return true;
    }

    @java.lang.Override
    protected boolean bottom(android.widget.TextView widget, android.text.Spannable buffer) {
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            android.text.Selection.extendSelection(buffer, buffer.length());
        } else {
            android.text.Selection.setSelection(buffer, buffer.length());
        }
        return true;
    }

    @java.lang.Override
    protected boolean lineStart(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            return android.text.Selection.extendToLeftEdge(buffer, layout);
        } else {
            return android.text.Selection.moveToLeftEdge(buffer, layout);
        }
    }

    @java.lang.Override
    protected boolean lineEnd(android.widget.TextView widget, android.text.Spannable buffer) {
        final android.text.Layout layout = widget.getLayout();
        if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
            return android.text.Selection.extendToRightEdge(buffer, layout);
        } else {
            return android.text.Selection.moveToRightEdge(buffer, layout);
        }
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    protected boolean leftWord(android.widget.TextView widget, android.text.Spannable buffer) {
        final int selectionEnd = widget.getSelectionEnd();
        final android.text.method.WordIterator wordIterator = widget.getWordIterator();
        wordIterator.setCharSequence(buffer, selectionEnd, selectionEnd);
        return android.text.Selection.moveToPreceding(buffer, wordIterator, android.text.method.ArrowKeyMovementMethod.isSelecting(buffer));
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    protected boolean rightWord(android.widget.TextView widget, android.text.Spannable buffer) {
        final int selectionEnd = widget.getSelectionEnd();
        final android.text.method.WordIterator wordIterator = widget.getWordIterator();
        wordIterator.setCharSequence(buffer, selectionEnd, selectionEnd);
        return android.text.Selection.moveToFollowing(buffer, wordIterator, android.text.method.ArrowKeyMovementMethod.isSelecting(buffer));
    }

    @java.lang.Override
    protected boolean home(android.widget.TextView widget, android.text.Spannable buffer) {
        return lineStart(widget, buffer);
    }

    @java.lang.Override
    protected boolean end(android.widget.TextView widget, android.text.Spannable buffer) {
        return lineEnd(widget, buffer);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        int initialScrollX = -1;
        int initialScrollY = -1;
        final int action = event.getAction();
        if (action == android.view.MotionEvent.ACTION_UP) {
            initialScrollX = android.text.method.Touch.getInitialScrollX(widget, buffer);
            initialScrollY = android.text.method.Touch.getInitialScrollY(widget, buffer);
        }
        boolean wasTouchSelecting = android.text.method.ArrowKeyMovementMethod.isSelecting(buffer);
        boolean handled = android.text.method.Touch.onTouchEvent(widget, buffer, event);
        if (widget.didTouchFocusSelect()) {
            return handled;
        }
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            // For touch events, the code should run only when selection is active.
            if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer)) {
                if (!widget.isFocused()) {
                    if (!widget.requestFocus()) {
                        return handled;
                    }
                }
                int offset = widget.getOffsetForPosition(event.getX(), event.getY());
                buffer.setSpan(android.text.method.ArrowKeyMovementMethod.LAST_TAP_DOWN, offset, offset, android.text.Spannable.SPAN_POINT_POINT);
                // Disallow intercepting of the touch events, so that
                // users can scroll and select at the same time.
                // without this, users would get booted out of select
                // mode once the view detected it needed to scroll.
                widget.getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else
            if (widget.isFocused()) {
                if (action == android.view.MotionEvent.ACTION_MOVE) {
                    if (android.text.method.ArrowKeyMovementMethod.isSelecting(buffer) && handled) {
                        final int startOffset = buffer.getSpanStart(android.text.method.ArrowKeyMovementMethod.LAST_TAP_DOWN);
                        // Before selecting, make sure we've moved out of the "slop".
                        // handled will be true, if we're in select mode AND we're
                        // OUT of the slop
                        // Turn long press off while we're selecting. User needs to
                        // re-tap on the selection to enable long press
                        widget.cancelLongPress();
                        // Update selection as we're moving the selection area.
                        // Get the current touch position
                        final int offset = widget.getOffsetForPosition(event.getX(), event.getY());
                        android.text.Selection.setSelection(buffer, java.lang.Math.min(startOffset, offset), java.lang.Math.max(startOffset, offset));
                        return true;
                    }
                } else
                    if (action == android.view.MotionEvent.ACTION_UP) {
                        // If we have scrolled, then the up shouldn't move the cursor,
                        // but we do need to make sure the cursor is still visible at
                        // the current scroll offset to avoid the scroll jumping later
                        // to show it.
                        if (((initialScrollY >= 0) && (initialScrollY != widget.getScrollY())) || ((initialScrollX >= 0) && (initialScrollX != widget.getScrollX()))) {
                            widget.moveCursorToVisibleOffset();
                            return true;
                        }
                        if (wasTouchSelecting) {
                            final int startOffset = buffer.getSpanStart(android.text.method.ArrowKeyMovementMethod.LAST_TAP_DOWN);
                            final int endOffset = widget.getOffsetForPosition(event.getX(), event.getY());
                            android.text.Selection.setSelection(buffer, java.lang.Math.min(startOffset, endOffset), java.lang.Math.max(startOffset, endOffset));
                            buffer.removeSpan(android.text.method.ArrowKeyMovementMethod.LAST_TAP_DOWN);
                        }
                        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(buffer);
                        android.text.method.MetaKeyKeyListener.resetLockedMeta(buffer);
                        return true;
                    }

            }

        return handled;
    }

    @java.lang.Override
    public boolean canSelectArbitrarily() {
        return true;
    }

    @java.lang.Override
    public void initialize(android.widget.TextView widget, android.text.Spannable text) {
        android.text.Selection.setSelection(text, 0);
    }

    @java.lang.Override
    public void onTakeFocus(android.widget.TextView view, android.text.Spannable text, int dir) {
        if ((dir & (android.view.View.FOCUS_FORWARD | android.view.View.FOCUS_DOWN)) != 0) {
            if (view.getLayout() == null) {
                // This shouldn't be null, but do something sensible if it is.
                android.text.Selection.setSelection(text, text.length());
            }
        } else {
            android.text.Selection.setSelection(text, text.length());
        }
    }

    public static android.text.method.MovementMethod getInstance() {
        if (android.text.method.ArrowKeyMovementMethod.sInstance == null) {
            android.text.method.ArrowKeyMovementMethod.sInstance = new android.text.method.ArrowKeyMovementMethod();
        }
        return android.text.method.ArrowKeyMovementMethod.sInstance;
    }

    private static final java.lang.Object LAST_TAP_DOWN = new java.lang.Object();

    private static android.text.method.ArrowKeyMovementMethod sInstance;
}

