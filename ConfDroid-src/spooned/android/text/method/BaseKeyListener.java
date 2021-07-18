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
 * Abstract base class for key listeners.
 *
 * Provides a basic foundation for entering and editing text.
 * Subclasses should override {@link #onKeyDown} and {@link #onKeyUp} to insert
 * characters as keys are pressed.
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public abstract class BaseKeyListener extends android.text.method.MetaKeyKeyListener implements android.text.method.KeyListener {
    /* package */
    static final java.lang.Object OLD_SEL_START = new android.text.NoCopySpan.Concrete();

    private static final int LINE_FEED = 0xa;

    private static final int CARRIAGE_RETURN = 0xd;

    private final java.lang.Object mLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mLock")
    static android.graphics.Paint sCachedPaint = null;

    /**
     * Performs the action that happens when you press the {@link KeyEvent#KEYCODE_DEL} key in
     * a {@link TextView}.  If there is a selection, deletes the selection; otherwise,
     * deletes the character before the cursor, if any; ALT+DEL deletes everything on
     * the line the cursor is on.
     *
     * @return true if anything was deleted; false otherwise.
     */
    public boolean backspace(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        return backspaceOrForwardDelete(view, content, keyCode, event, false);
    }

    /**
     * Performs the action that happens when you press the {@link KeyEvent#KEYCODE_FORWARD_DEL}
     * key in a {@link TextView}.  If there is a selection, deletes the selection; otherwise,
     * deletes the character before the cursor, if any; ALT+FORWARD_DEL deletes everything on
     * the line the cursor is on.
     *
     * @return true if anything was deleted; false otherwise.
     */
    public boolean forwardDelete(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        return backspaceOrForwardDelete(view, content, keyCode, event, true);
    }

    // Returns true if the given code point is a variation selector.
    private static boolean isVariationSelector(int codepoint) {
        return android.icu.lang.UCharacter.hasBinaryProperty(codepoint, UProperty.VARIATION_SELECTOR);
    }

    // Returns the offset of the replacement span edge if the offset is inside of the replacement
    // span.  Otherwise, does nothing and returns the input offset value.
    private static int adjustReplacementSpan(java.lang.CharSequence text, int offset, boolean moveToStart) {
        if (!(text instanceof android.text.Spanned)) {
            return offset;
        }
        android.text.style.ReplacementSpan[] spans = ((android.text.Spanned) (text)).getSpans(offset, offset, android.text.style.ReplacementSpan.class);
        for (int i = 0; i < spans.length; i++) {
            final int start = ((android.text.Spanned) (text)).getSpanStart(spans[i]);
            final int end = ((android.text.Spanned) (text)).getSpanEnd(spans[i]);
            if ((start < offset) && (end > offset)) {
                offset = (moveToStart) ? start : end;
            }
        }
        return offset;
    }

    // Returns the start offset to be deleted by a backspace key from the given offset.
    private static int getOffsetForBackspaceKey(java.lang.CharSequence text, int offset) {
        if (offset <= 1) {
            return 0;
        }
        // Initial state
        final int STATE_START = 0;
        // The offset is immediately before line feed.
        final int STATE_LF = 1;
        // The offset is immediately before a KEYCAP.
        final int STATE_BEFORE_KEYCAP = 2;
        // The offset is immediately before a variation selector and a KEYCAP.
        final int STATE_BEFORE_VS_AND_KEYCAP = 3;
        // The offset is immediately before an emoji modifier.
        final int STATE_BEFORE_EMOJI_MODIFIER = 4;
        // The offset is immediately before a variation selector and an emoji modifier.
        final int STATE_BEFORE_VS_AND_EMOJI_MODIFIER = 5;
        // The offset is immediately before a variation selector.
        final int STATE_BEFORE_VS = 6;
        // The offset is immediately before an emoji.
        final int STATE_BEFORE_EMOJI = 7;
        // The offset is immediately before a ZWJ that were seen before a ZWJ emoji.
        final int STATE_BEFORE_ZWJ = 8;
        // The offset is immediately before a variation selector and a ZWJ that were seen before a
        // ZWJ emoji.
        final int STATE_BEFORE_VS_AND_ZWJ = 9;
        // The number of following RIS code points is odd.
        final int STATE_ODD_NUMBERED_RIS = 10;
        // The number of following RIS code points is even.
        final int STATE_EVEN_NUMBERED_RIS = 11;
        // The state machine has been stopped.
        final int STATE_FINISHED = 12;
        int deleteCharCount = 0;// Char count to be deleted by backspace.

        int lastSeenVSCharCount = 0;// Char count of previous variation selector.

        int state = STATE_START;
        int tmpOffset = offset;
        do {
            final int codePoint = java.lang.Character.codePointBefore(text, tmpOffset);
            tmpOffset -= java.lang.Character.charCount(codePoint);
            switch (state) {
                case STATE_START :
                    deleteCharCount = java.lang.Character.charCount(codePoint);
                    if (codePoint == android.text.method.BaseKeyListener.LINE_FEED) {
                        state = STATE_LF;
                    } else
                        if (android.text.method.BaseKeyListener.isVariationSelector(codePoint)) {
                            state = STATE_BEFORE_VS;
                        } else
                            if (android.text.Emoji.isRegionalIndicatorSymbol(codePoint)) {
                                state = STATE_ODD_NUMBERED_RIS;
                            } else
                                if (android.text.Emoji.isEmojiModifier(codePoint)) {
                                    state = STATE_BEFORE_EMOJI_MODIFIER;
                                } else
                                    if (codePoint == android.text.Emoji.COMBINING_ENCLOSING_KEYCAP) {
                                        state = STATE_BEFORE_KEYCAP;
                                    } else
                                        if (android.text.Emoji.isEmoji(codePoint)) {
                                            state = STATE_BEFORE_EMOJI;
                                        } else {
                                            state = STATE_FINISHED;
                                        }





                    break;
                case STATE_LF :
                    if (codePoint == android.text.method.BaseKeyListener.CARRIAGE_RETURN) {
                        ++deleteCharCount;
                    }
                    state = STATE_FINISHED;
                case STATE_ODD_NUMBERED_RIS :
                    if (android.text.Emoji.isRegionalIndicatorSymbol(codePoint)) {
                        deleteCharCount += 2;/* Char count of RIS */

                        state = STATE_EVEN_NUMBERED_RIS;
                    } else {
                        state = STATE_FINISHED;
                    }
                    break;
                case STATE_EVEN_NUMBERED_RIS :
                    if (android.text.Emoji.isRegionalIndicatorSymbol(codePoint)) {
                        deleteCharCount -= 2;/* Char count of RIS */

                        state = STATE_ODD_NUMBERED_RIS;
                    } else {
                        state = STATE_FINISHED;
                    }
                    break;
                case STATE_BEFORE_KEYCAP :
                    if (android.text.method.BaseKeyListener.isVariationSelector(codePoint)) {
                        lastSeenVSCharCount = java.lang.Character.charCount(codePoint);
                        state = STATE_BEFORE_VS_AND_KEYCAP;
                        break;
                    }
                    if (android.text.Emoji.isKeycapBase(codePoint)) {
                        deleteCharCount += java.lang.Character.charCount(codePoint);
                    }
                    state = STATE_FINISHED;
                    break;
                case STATE_BEFORE_VS_AND_KEYCAP :
                    if (android.text.Emoji.isKeycapBase(codePoint)) {
                        deleteCharCount += lastSeenVSCharCount + java.lang.Character.charCount(codePoint);
                    }
                    state = STATE_FINISHED;
                    break;
                case STATE_BEFORE_EMOJI_MODIFIER :
                    if (android.text.method.BaseKeyListener.isVariationSelector(codePoint)) {
                        lastSeenVSCharCount = java.lang.Character.charCount(codePoint);
                        state = STATE_BEFORE_VS_AND_EMOJI_MODIFIER;
                        break;
                    } else
                        if (android.text.Emoji.isEmojiModifierBase(codePoint)) {
                            deleteCharCount += java.lang.Character.charCount(codePoint);
                        }

                    state = STATE_FINISHED;
                    break;
                case STATE_BEFORE_VS_AND_EMOJI_MODIFIER :
                    if (android.text.Emoji.isEmojiModifierBase(codePoint)) {
                        deleteCharCount += lastSeenVSCharCount + java.lang.Character.charCount(codePoint);
                    }
                    state = STATE_FINISHED;
                    break;
                case STATE_BEFORE_VS :
                    if (android.text.Emoji.isEmoji(codePoint)) {
                        deleteCharCount += java.lang.Character.charCount(codePoint);
                        state = STATE_BEFORE_EMOJI;
                        break;
                    }
                    if ((!android.text.method.BaseKeyListener.isVariationSelector(codePoint)) && (android.icu.lang.UCharacter.getCombiningClass(codePoint) == 0)) {
                        deleteCharCount += java.lang.Character.charCount(codePoint);
                    }
                    state = STATE_FINISHED;
                    break;
                case STATE_BEFORE_EMOJI :
                    if (codePoint == android.text.Emoji.ZERO_WIDTH_JOINER) {
                        state = STATE_BEFORE_ZWJ;
                    } else {
                        state = STATE_FINISHED;
                    }
                    break;
                case STATE_BEFORE_ZWJ :
                    if (android.text.Emoji.isEmoji(codePoint)) {
                        deleteCharCount += java.lang.Character.charCount(codePoint) + 1;// +1 for ZWJ.

                        state = (android.text.Emoji.isEmojiModifier(codePoint)) ? STATE_BEFORE_EMOJI_MODIFIER : STATE_BEFORE_EMOJI;
                    } else
                        if (android.text.method.BaseKeyListener.isVariationSelector(codePoint)) {
                            lastSeenVSCharCount = java.lang.Character.charCount(codePoint);
                            state = STATE_BEFORE_VS_AND_ZWJ;
                        } else {
                            state = STATE_FINISHED;
                        }

                    break;
                case STATE_BEFORE_VS_AND_ZWJ :
                    if (android.text.Emoji.isEmoji(codePoint)) {
                        // +1 for ZWJ.
                        deleteCharCount += (lastSeenVSCharCount + 1) + java.lang.Character.charCount(codePoint);
                        lastSeenVSCharCount = 0;
                        state = STATE_BEFORE_EMOJI;
                    } else {
                        state = STATE_FINISHED;
                    }
                    break;
                default :
                    throw new java.lang.IllegalArgumentException(("state " + state) + " is unknown");
            }
        } while ((tmpOffset > 0) && (state != STATE_FINISHED) );
        return /* move to the start */
        android.text.method.BaseKeyListener.adjustReplacementSpan(text, offset - deleteCharCount, true);
    }

    // Returns the end offset to be deleted by a forward delete key from the given offset.
    private static int getOffsetForForwardDeleteKey(java.lang.CharSequence text, int offset, android.graphics.Paint paint) {
        final int len = text.length();
        if (offset >= (len - 1)) {
            return len;
        }
        offset = /* not used */
        paint.getTextRunCursor(text, offset, len, android.graphics.Paint.DIRECTION_LTR, offset, android.graphics.Paint.CURSOR_AFTER);
        return /* move to the end */
        android.text.method.BaseKeyListener.adjustReplacementSpan(text, offset, false);
    }

    private boolean backspaceOrForwardDelete(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event, boolean isForwardDelete) {
        // Ensure the key event does not have modifiers except ALT or SHIFT or CTRL.
        if (!android.view.KeyEvent.metaStateHasNoModifiers(event.getMetaState() & (~((android.view.KeyEvent.META_SHIFT_MASK | android.view.KeyEvent.META_ALT_MASK) | android.view.KeyEvent.META_CTRL_MASK)))) {
            return false;
        }
        // If there is a current selection, delete it.
        if (deleteSelection(view, content)) {
            return true;
        }
        // MetaKeyKeyListener doesn't track control key state. Need to check the KeyEvent instead.
        boolean isCtrlActive = (event.getMetaState() & android.view.KeyEvent.META_CTRL_ON) != 0;
        boolean isShiftActive = android.text.method.MetaKeyKeyListener.getMetaState(content, android.text.method.MetaKeyKeyListener.META_SHIFT_ON, event) == 1;
        boolean isAltActive = android.text.method.MetaKeyKeyListener.getMetaState(content, android.text.method.MetaKeyKeyListener.META_ALT_ON, event) == 1;
        if (isCtrlActive) {
            if (isAltActive || isShiftActive) {
                // Ctrl+Alt, Ctrl+Shift, Ctrl+Alt+Shift should not delete any characters.
                return false;
            }
            return deleteUntilWordBoundary(view, content, isForwardDelete);
        }
        // Alt+Backspace or Alt+ForwardDelete deletes the current line, if possible.
        if (isAltActive && deleteLine(view, content)) {
            return true;
        }
        // Delete a character.
        final int start = android.text.Selection.getSelectionEnd(content);
        final int end;
        if (isForwardDelete) {
            final android.graphics.Paint paint;
            if (view instanceof android.widget.TextView) {
                paint = ((android.widget.TextView) (view)).getPaint();
            } else {
                synchronized(mLock) {
                    if (android.text.method.BaseKeyListener.sCachedPaint == null) {
                        android.text.method.BaseKeyListener.sCachedPaint = new android.graphics.Paint();
                    }
                    paint = android.text.method.BaseKeyListener.sCachedPaint;
                }
            }
            end = android.text.method.BaseKeyListener.getOffsetForForwardDeleteKey(content, start, paint);
        } else {
            end = android.text.method.BaseKeyListener.getOffsetForBackspaceKey(content, start);
        }
        if (start != end) {
            content.delete(java.lang.Math.min(start, end), java.lang.Math.max(start, end));
            return true;
        }
        return false;
    }

    private boolean deleteUntilWordBoundary(android.view.View view, android.text.Editable content, boolean isForwardDelete) {
        int currentCursorOffset = android.text.Selection.getSelectionStart(content);
        // If there is a selection, do nothing.
        if (currentCursorOffset != android.text.Selection.getSelectionEnd(content)) {
            return false;
        }
        // Early exit if there is no contents to delete.
        if (((!isForwardDelete) && (currentCursorOffset == 0)) || (isForwardDelete && (currentCursorOffset == content.length()))) {
            return false;
        }
        android.text.method.WordIterator wordIterator = null;
        if (view instanceof android.widget.TextView) {
            wordIterator = ((android.widget.TextView) (view)).getWordIterator();
        }
        if (wordIterator == null) {
            // Default locale is used for WordIterator since the appropriate locale is not clear
            // here.
            // TODO: Use appropriate locale for WordIterator.
            wordIterator = new android.text.method.WordIterator();
        }
        int deleteFrom;
        int deleteTo;
        if (isForwardDelete) {
            deleteFrom = currentCursorOffset;
            wordIterator.setCharSequence(content, deleteFrom, content.length());
            deleteTo = wordIterator.following(currentCursorOffset);
            if (deleteTo == java.text.BreakIterator.DONE) {
                deleteTo = content.length();
            }
        } else {
            deleteTo = currentCursorOffset;
            wordIterator.setCharSequence(content, 0, deleteTo);
            deleteFrom = wordIterator.preceding(currentCursorOffset);
            if (deleteFrom == java.text.BreakIterator.DONE) {
                deleteFrom = 0;
            }
        }
        content.delete(deleteFrom, deleteTo);
        return true;
    }

    private boolean deleteSelection(android.view.View view, android.text.Editable content) {
        int selectionStart = android.text.Selection.getSelectionStart(content);
        int selectionEnd = android.text.Selection.getSelectionEnd(content);
        if (selectionEnd < selectionStart) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }
        if (selectionStart != selectionEnd) {
            content.delete(selectionStart, selectionEnd);
            return true;
        }
        return false;
    }

    private boolean deleteLine(android.view.View view, android.text.Editable content) {
        if (view instanceof android.widget.TextView) {
            final android.text.Layout layout = ((android.widget.TextView) (view)).getLayout();
            if (layout != null) {
                final int line = layout.getLineForOffset(android.text.Selection.getSelectionStart(content));
                final int start = layout.getLineStart(line);
                final int end = layout.getLineEnd(line);
                if (end != start) {
                    content.delete(start, end);
                    return true;
                }
            }
        }
        return false;
    }

    static int makeTextContentType(android.text.method.TextKeyListener.Capitalize caps, boolean autoText) {
        int contentType = android.text.InputType.TYPE_CLASS_TEXT;
        switch (caps) {
            case CHARACTERS :
                contentType |= android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
                break;
            case WORDS :
                contentType |= android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;
                break;
            case SENTENCES :
                contentType |= android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
                break;
        }
        if (autoText) {
            contentType |= android.text.InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
        }
        return contentType;
    }

    public boolean onKeyDown(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        boolean handled;
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DEL :
                handled = backspace(view, content, keyCode, event);
                break;
            case android.view.KeyEvent.KEYCODE_FORWARD_DEL :
                handled = forwardDelete(view, content, keyCode, event);
                break;
            default :
                handled = false;
                break;
        }
        if (handled) {
            android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
            return true;
        }
        return super.onKeyDown(view, content, keyCode, event);
    }

    /**
     * Base implementation handles ACTION_MULTIPLE KEYCODE_UNKNOWN by inserting
     * the event's text into the content.
     */
    public boolean onKeyOther(android.view.View view, android.text.Editable content, android.view.KeyEvent event) {
        if ((event.getAction() != android.view.KeyEvent.ACTION_MULTIPLE) || (event.getKeyCode() != android.view.KeyEvent.KEYCODE_UNKNOWN)) {
            // Not something we are interested in.
            return false;
        }
        int selectionStart = android.text.Selection.getSelectionStart(content);
        int selectionEnd = android.text.Selection.getSelectionEnd(content);
        if (selectionEnd < selectionStart) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }
        java.lang.CharSequence text = event.getCharacters();
        if (text == null) {
            return false;
        }
        content.replace(selectionStart, selectionEnd, text);
        return true;
    }
}

