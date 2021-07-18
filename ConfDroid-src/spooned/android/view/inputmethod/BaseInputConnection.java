package android.view.inputmethod;


/**
 * Base class for implementors of the InputConnection interface, taking care
 * of most of the common behavior for providing a connection to an Editable.
 * Implementors of this class will want to be sure to implement
 * {@link #getEditable} to provide access to their own editable object, and
 * to refer to the documentation in {@link InputConnection}.
 */
public class BaseInputConnection implements android.view.inputmethod.InputConnection {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "BaseInputConnection";

    static final java.lang.Object COMPOSING = new android.view.inputmethod.ComposingText();

    /**
     *
     *
     * @unknown 
     */
    protected final android.view.inputmethod.InputMethodManager mIMM;

    final android.view.View mTargetView;

    final boolean mDummyMode;

    private java.lang.Object[] mDefaultComposingSpans;

    android.text.Editable mEditable;

    android.view.KeyCharacterMap mKeyCharacterMap;

    BaseInputConnection(android.view.inputmethod.InputMethodManager mgr, boolean fullEditor) {
        mIMM = mgr;
        mTargetView = null;
        mDummyMode = !fullEditor;
    }

    public BaseInputConnection(android.view.View targetView, boolean fullEditor) {
        mIMM = ((android.view.inputmethod.InputMethodManager) (targetView.getContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)));
        mTargetView = targetView;
        mDummyMode = !fullEditor;
    }

    public static final void removeComposingSpans(android.text.Spannable text) {
        text.removeSpan(android.view.inputmethod.BaseInputConnection.COMPOSING);
        java.lang.Object[] sps = text.getSpans(0, text.length(), java.lang.Object.class);
        if (sps != null) {
            for (int i = sps.length - 1; i >= 0; i--) {
                java.lang.Object o = sps[i];
                if ((text.getSpanFlags(o) & android.text.Spanned.SPAN_COMPOSING) != 0) {
                    text.removeSpan(o);
                }
            }
        }
    }

    public static void setComposingSpans(android.text.Spannable text) {
        android.view.inputmethod.BaseInputConnection.setComposingSpans(text, 0, text.length());
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setComposingSpans(android.text.Spannable text, int start, int end) {
        final java.lang.Object[] sps = text.getSpans(start, end, java.lang.Object.class);
        if (sps != null) {
            for (int i = sps.length - 1; i >= 0; i--) {
                final java.lang.Object o = sps[i];
                if (o == android.view.inputmethod.BaseInputConnection.COMPOSING) {
                    text.removeSpan(o);
                    continue;
                }
                final int fl = text.getSpanFlags(o);
                if ((fl & (android.text.Spanned.SPAN_COMPOSING | android.text.Spanned.SPAN_POINT_MARK_MASK)) != (android.text.Spanned.SPAN_COMPOSING | android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)) {
                    text.setSpan(o, text.getSpanStart(o), text.getSpanEnd(o), ((fl & (~android.text.Spanned.SPAN_POINT_MARK_MASK)) | android.text.Spanned.SPAN_COMPOSING) | android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        text.setSpan(android.view.inputmethod.BaseInputConnection.COMPOSING, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE | android.text.Spanned.SPAN_COMPOSING);
    }

    public static int getComposingSpanStart(android.text.Spannable text) {
        return text.getSpanStart(android.view.inputmethod.BaseInputConnection.COMPOSING);
    }

    public static int getComposingSpanEnd(android.text.Spannable text) {
        return text.getSpanEnd(android.view.inputmethod.BaseInputConnection.COMPOSING);
    }

    /**
     * Return the target of edit operations.  The default implementation
     * returns its own fake editable that is just used for composing text;
     * subclasses that are real text editors should override this and
     * supply their own.
     */
    public android.text.Editable getEditable() {
        if (mEditable == null) {
            mEditable = Editable.Factory.getInstance().newEditable("");
            android.text.Selection.setSelection(mEditable, 0);
        }
        return mEditable;
    }

    /**
     * Default implementation does nothing.
     */
    public boolean beginBatchEdit() {
        return false;
    }

    /**
     * Default implementation does nothing.
     */
    public boolean endBatchEdit() {
        return false;
    }

    /**
     * Default implementation calls {@link #finishComposingText()}.
     */
    @android.annotation.CallSuper
    public void closeConnection() {
        finishComposingText();
    }

    /**
     * Default implementation uses
     * {@link MetaKeyKeyListener#clearMetaKeyState(long, int)
     * MetaKeyKeyListener.clearMetaKeyState(long, int)} to clear the state.
     */
    public boolean clearMetaKeyStates(int states) {
        final android.text.Editable content = getEditable();
        if (content == null)
            return false;

        android.text.method.MetaKeyKeyListener.clearMetaKeyState(content, states);
        return true;
    }

    /**
     * Default implementation does nothing and returns false.
     */
    public boolean commitCompletion(android.view.inputmethod.CompletionInfo text) {
        return false;
    }

    /**
     * Default implementation does nothing and returns false.
     */
    public boolean commitCorrection(android.view.inputmethod.CorrectionInfo correctionInfo) {
        return false;
    }

    /**
     * Default implementation replaces any existing composing text with
     * the given text.  In addition, only if dummy mode, a key event is
     * sent for the new text and the current editable buffer cleared.
     */
    public boolean commitText(java.lang.CharSequence text, int newCursorPosition) {
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, "commitText " + text);

        replaceText(text, newCursorPosition, false);
        sendCurrentText();
        return true;
    }

    /**
     * The default implementation performs the deletion around the current selection position of the
     * editable text.
     *
     * @param beforeLength
     * 		The number of characters before the cursor to be deleted, in code unit.
     * 		If this is greater than the number of existing characters between the beginning of the
     * 		text and the cursor, then this method does not fail but deletes all the characters in
     * 		that range.
     * @param afterLength
     * 		The number of characters after the cursor to be deleted, in code unit.
     * 		If this is greater than the number of existing characters between the cursor and
     * 		the end of the text, then this method does not fail but deletes all the characters in
     * 		that range.
     */
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, (("deleteSurroundingText " + beforeLength) + " / ") + afterLength);

        final android.text.Editable content = getEditable();
        if (content == null)
            return false;

        beginBatchEdit();
        int a = android.text.Selection.getSelectionStart(content);
        int b = android.text.Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        // Ignore the composing text.
        int ca = android.view.inputmethod.BaseInputConnection.getComposingSpanStart(content);
        int cb = android.view.inputmethod.BaseInputConnection.getComposingSpanEnd(content);
        if (cb < ca) {
            int tmp = ca;
            ca = cb;
            cb = tmp;
        }
        if ((ca != (-1)) && (cb != (-1))) {
            if (ca < a)
                a = ca;

            if (cb > b)
                b = cb;

        }
        int deleted = 0;
        if (beforeLength > 0) {
            int start = a - beforeLength;
            if (start < 0)
                start = 0;

            content.delete(start, a);
            deleted = a - start;
        }
        if (afterLength > 0) {
            b = b - deleted;
            int end = b + afterLength;
            if (end > content.length())
                end = content.length();

            content.delete(b, end);
        }
        endBatchEdit();
        return true;
    }

    private static int INVALID_INDEX = -1;

    private static int findIndexBackward(final java.lang.CharSequence cs, final int from, final int numCodePoints) {
        int currentIndex = from;
        boolean waitingHighSurrogate = false;
        final int N = cs.length();
        if ((currentIndex < 0) || (N < currentIndex)) {
            return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// The starting point is out of range.

        }
        if (numCodePoints < 0) {
            return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// Basically this should not happen.

        }
        int remainingCodePoints = numCodePoints;
        while (true) {
            if (remainingCodePoints == 0) {
                return currentIndex;// Reached to the requested length in code points.

            }
            --currentIndex;
            if (currentIndex < 0) {
                if (waitingHighSurrogate) {
                    return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// An invalid surrogate pair is found.

                }
                return 0;// Reached to the beginning of the text w/o any invalid surrogate pair.

            }
            final char c = cs.charAt(currentIndex);
            if (waitingHighSurrogate) {
                if (!java.lang.Character.isHighSurrogate(c)) {
                    return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// An invalid surrogate pair is found.

                }
                waitingHighSurrogate = false;
                --remainingCodePoints;
                continue;
            }
            if (!java.lang.Character.isSurrogate(c)) {
                --remainingCodePoints;
                continue;
            }
            if (java.lang.Character.isHighSurrogate(c)) {
                return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// A invalid surrogate pair is found.

            }
            waitingHighSurrogate = true;
        } 
    }

    private static int findIndexForward(final java.lang.CharSequence cs, final int from, final int numCodePoints) {
        int currentIndex = from;
        boolean waitingLowSurrogate = false;
        final int N = cs.length();
        if ((currentIndex < 0) || (N < currentIndex)) {
            return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// The starting point is out of range.

        }
        if (numCodePoints < 0) {
            return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// Basically this should not happen.

        }
        int remainingCodePoints = numCodePoints;
        while (true) {
            if (remainingCodePoints == 0) {
                return currentIndex;// Reached to the requested length in code points.

            }
            if (currentIndex >= N) {
                if (waitingLowSurrogate) {
                    return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// An invalid surrogate pair is found.

                }
                return N;// Reached to the end of the text w/o any invalid surrogate pair.

            }
            final char c = cs.charAt(currentIndex);
            if (waitingLowSurrogate) {
                if (!java.lang.Character.isLowSurrogate(c)) {
                    return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// An invalid surrogate pair is found.

                }
                --remainingCodePoints;
                waitingLowSurrogate = false;
                ++currentIndex;
                continue;
            }
            if (!java.lang.Character.isSurrogate(c)) {
                --remainingCodePoints;
                ++currentIndex;
                continue;
            }
            if (java.lang.Character.isLowSurrogate(c)) {
                return android.view.inputmethod.BaseInputConnection.INVALID_INDEX;// A invalid surrogate pair is found.

            }
            waitingLowSurrogate = true;
            ++currentIndex;
        } 
    }

    /**
     * The default implementation performs the deletion around the current selection position of the
     * editable text.
     *
     * @param beforeLength
     * 		The number of characters before the cursor to be deleted, in code points.
     * 		If this is greater than the number of existing characters between the beginning of the
     * 		text and the cursor, then this method does not fail but deletes all the characters in
     * 		that range.
     * @param afterLength
     * 		The number of characters after the cursor to be deleted, in code points.
     * 		If this is greater than the number of existing characters between the cursor and
     * 		the end of the text, then this method does not fail but deletes all the characters in
     * 		that range.
     */
    public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, (("deleteSurroundingText " + beforeLength) + " / ") + afterLength);

        final android.text.Editable content = getEditable();
        if (content == null)
            return false;

        beginBatchEdit();
        int a = android.text.Selection.getSelectionStart(content);
        int b = android.text.Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        // Ignore the composing text.
        int ca = android.view.inputmethod.BaseInputConnection.getComposingSpanStart(content);
        int cb = android.view.inputmethod.BaseInputConnection.getComposingSpanEnd(content);
        if (cb < ca) {
            int tmp = ca;
            ca = cb;
            cb = tmp;
        }
        if ((ca != (-1)) && (cb != (-1))) {
            if (ca < a)
                a = ca;

            if (cb > b)
                b = cb;

        }
        if ((a >= 0) && (b >= 0)) {
            final int start = android.view.inputmethod.BaseInputConnection.findIndexBackward(content, a, java.lang.Math.max(beforeLength, 0));
            if (start != android.view.inputmethod.BaseInputConnection.INVALID_INDEX) {
                final int end = android.view.inputmethod.BaseInputConnection.findIndexForward(content, b, java.lang.Math.max(afterLength, 0));
                if (end != android.view.inputmethod.BaseInputConnection.INVALID_INDEX) {
                    final int numDeleteBefore = a - start;
                    if (numDeleteBefore > 0) {
                        content.delete(start, a);
                    }
                    final int numDeleteAfter = end - b;
                    if (numDeleteAfter > 0) {
                        content.delete(b - numDeleteBefore, end - numDeleteBefore);
                    }
                }
            }
            // NOTE: You may think we should return false here if start and/or end is INVALID_INDEX,
            // but the truth is that IInputConnectionWrapper running in the middle of IPC calls
            // always returns true to the IME without waiting for the completion of this method as
            // IInputConnectionWrapper#isAtive() returns true.  This is actually why some methods
            // including this method look like asynchronous calls from the IME.
        }
        endBatchEdit();
        return true;
    }

    /**
     * The default implementation removes the composing state from the
     * current editable text.  In addition, only if dummy mode, a key event is
     * sent for the new text and the current editable buffer cleared.
     */
    public boolean finishComposingText() {
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, "finishComposingText");

        final android.text.Editable content = getEditable();
        if (content != null) {
            beginBatchEdit();
            android.view.inputmethod.BaseInputConnection.removeComposingSpans(content);
            // Note: sendCurrentText does nothing unless mDummyMode is set
            sendCurrentText();
            endBatchEdit();
        }
        return true;
    }

    /**
     * The default implementation uses TextUtils.getCapsMode to get the
     * cursor caps mode for the current selection position in the editable
     * text, unless in dummy mode in which case 0 is always returned.
     */
    public int getCursorCapsMode(int reqModes) {
        if (mDummyMode)
            return 0;

        final android.text.Editable content = getEditable();
        if (content == null)
            return 0;

        int a = android.text.Selection.getSelectionStart(content);
        int b = android.text.Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return android.text.TextUtils.getCapsMode(content, a, reqModes);
    }

    /**
     * The default implementation always returns null.
     */
    public android.view.inputmethod.ExtractedText getExtractedText(android.view.inputmethod.ExtractedTextRequest request, int flags) {
        return null;
    }

    /**
     * The default implementation returns the given amount of text from the
     * current cursor position in the buffer.
     */
    public java.lang.CharSequence getTextBeforeCursor(int length, int flags) {
        final android.text.Editable content = getEditable();
        if (content == null)
            return null;

        int a = android.text.Selection.getSelectionStart(content);
        int b = android.text.Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (a <= 0) {
            return "";
        }
        if (length > a) {
            length = a;
        }
        if ((flags & android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES) != 0) {
            return content.subSequence(a - length, a);
        }
        return android.text.TextUtils.substring(content, a - length, a);
    }

    /**
     * The default implementation returns the text currently selected, or null if none is
     * selected.
     */
    public java.lang.CharSequence getSelectedText(int flags) {
        final android.text.Editable content = getEditable();
        if (content == null)
            return null;

        int a = android.text.Selection.getSelectionStart(content);
        int b = android.text.Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if ((a == b) || (a < 0))
            return null;

        if ((flags & android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES) != 0) {
            return content.subSequence(a, b);
        }
        return android.text.TextUtils.substring(content, a, b);
    }

    /**
     * The default implementation returns the given amount of text from the
     * current cursor position in the buffer.
     */
    public java.lang.CharSequence getTextAfterCursor(int length, int flags) {
        final android.text.Editable content = getEditable();
        if (content == null)
            return null;

        int a = android.text.Selection.getSelectionStart(content);
        int b = android.text.Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        // Guard against the case where the cursor has not been positioned yet.
        if (b < 0) {
            b = 0;
        }
        if ((b + length) > content.length()) {
            length = content.length() - b;
        }
        if ((flags & android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES) != 0) {
            return content.subSequence(b, b + length);
        }
        return android.text.TextUtils.substring(content, b, b + length);
    }

    /**
     * The default implementation turns this into the enter key.
     */
    public boolean performEditorAction(int actionCode) {
        long eventTime = android.os.SystemClock.uptimeMillis();
        sendKeyEvent(new android.view.KeyEvent(eventTime, eventTime, android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER, 0, 0, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, (android.view.KeyEvent.FLAG_SOFT_KEYBOARD | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE) | android.view.KeyEvent.FLAG_EDITOR_ACTION));
        sendKeyEvent(new android.view.KeyEvent(android.os.SystemClock.uptimeMillis(), eventTime, android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER, 0, 0, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, (android.view.KeyEvent.FLAG_SOFT_KEYBOARD | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE) | android.view.KeyEvent.FLAG_EDITOR_ACTION));
        return true;
    }

    /**
     * The default implementation does nothing.
     */
    public boolean performContextMenuAction(int id) {
        return false;
    }

    /**
     * The default implementation does nothing.
     */
    public boolean performPrivateCommand(java.lang.String action, android.os.Bundle data) {
        return false;
    }

    /**
     * The default implementation does nothing.
     */
    public boolean requestCursorUpdates(int cursorUpdateMode) {
        return false;
    }

    public android.os.Handler getHandler() {
        return null;
    }

    /**
     * The default implementation places the given text into the editable,
     * replacing any existing composing text.  The new text is marked as
     * in a composing state with the composing style.
     */
    public boolean setComposingText(java.lang.CharSequence text, int newCursorPosition) {
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, "setComposingText " + text);

        replaceText(text, newCursorPosition, true);
        return true;
    }

    public boolean setComposingRegion(int start, int end) {
        final android.text.Editable content = getEditable();
        if (content != null) {
            beginBatchEdit();
            android.view.inputmethod.BaseInputConnection.removeComposingSpans(content);
            int a = start;
            int b = end;
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            // Clip the end points to be within the content bounds.
            final int length = content.length();
            if (a < 0)
                a = 0;

            if (b < 0)
                b = 0;

            if (a > length)
                a = length;

            if (b > length)
                b = length;

            ensureDefaultComposingSpans();
            if (mDefaultComposingSpans != null) {
                for (int i = 0; i < mDefaultComposingSpans.length; ++i) {
                    content.setSpan(mDefaultComposingSpans[i], a, b, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE | android.text.Spanned.SPAN_COMPOSING);
                }
            }
            content.setSpan(android.view.inputmethod.BaseInputConnection.COMPOSING, a, b, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE | android.text.Spanned.SPAN_COMPOSING);
            // Note: sendCurrentText does nothing unless mDummyMode is set
            sendCurrentText();
            endBatchEdit();
        }
        return true;
    }

    /**
     * The default implementation changes the selection position in the
     * current editable text.
     */
    public boolean setSelection(int start, int end) {
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, (("setSelection " + start) + ", ") + end);

        final android.text.Editable content = getEditable();
        if (content == null)
            return false;

        int len = content.length();
        if ((((start > len) || (end > len)) || (start < 0)) || (end < 0)) {
            // If the given selection is out of bounds, just ignore it.
            // Most likely the text was changed out from under the IME,
            // and the IME is going to have to update all of its state
            // anyway.
            return true;
        }
        if ((start == end) && (android.text.method.MetaKeyKeyListener.getMetaState(content, MetaKeyKeyListener.META_SELECTING) != 0)) {
            // If we are in selection mode, then we want to extend the
            // selection instead of replacing it.
            android.text.Selection.extendSelection(content, start);
        } else {
            android.text.Selection.setSelection(content, start, end);
        }
        return true;
    }

    /**
     * Provides standard implementation for sending a key event to the window
     * attached to the input connection's view.
     */
    public boolean sendKeyEvent(android.view.KeyEvent event) {
        mIMM.dispatchKeyEventFromInputMethod(mTargetView, event);
        return false;
    }

    /**
     * Updates InputMethodManager with the current fullscreen mode.
     */
    public boolean reportFullscreenMode(boolean enabled) {
        return true;
    }

    private void sendCurrentText() {
        if (!mDummyMode) {
            return;
        }
        android.text.Editable content = getEditable();
        if (content != null) {
            final int N = content.length();
            if (N == 0) {
                return;
            }
            if (N == 1) {
                // If it's 1 character, we have a chance of being
                // able to generate normal key events...
                if (mKeyCharacterMap == null) {
                    mKeyCharacterMap = android.view.KeyCharacterMap.load(android.view.KeyCharacterMap.VIRTUAL_KEYBOARD);
                }
                char[] chars = new char[1];
                content.getChars(0, 1, chars, 0);
                android.view.KeyEvent[] events = mKeyCharacterMap.getEvents(chars);
                if (events != null) {
                    for (int i = 0; i < events.length; i++) {
                        if (android.view.inputmethod.BaseInputConnection.DEBUG)
                            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, "Sending: " + events[i]);

                        sendKeyEvent(events[i]);
                    }
                    content.clear();
                    return;
                }
            }
            // Otherwise, revert to the special key event containing
            // the actual characters.
            android.view.KeyEvent event = new android.view.KeyEvent(android.os.SystemClock.uptimeMillis(), content.toString(), android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0);
            sendKeyEvent(event);
            content.clear();
        }
    }

    private void ensureDefaultComposingSpans() {
        if (mDefaultComposingSpans == null) {
            android.content.Context context;
            if (mTargetView != null) {
                context = mTargetView.getContext();
            } else
                if (mIMM.mServedView != null) {
                    context = mIMM.mServedView.getContext();
                } else {
                    context = null;
                }

            if (context != null) {
                android.content.res.TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{ com.android.internal.R.attr.candidatesTextStyleSpans });
                java.lang.CharSequence style = ta.getText(0);
                ta.recycle();
                if ((style != null) && (style instanceof android.text.Spanned)) {
                    mDefaultComposingSpans = ((android.text.Spanned) (style)).getSpans(0, style.length(), java.lang.Object.class);
                }
            }
        }
    }

    private void replaceText(java.lang.CharSequence text, int newCursorPosition, boolean composing) {
        final android.text.Editable content = getEditable();
        if (content == null) {
            return;
        }
        beginBatchEdit();
        // delete composing text set previously.
        int a = android.view.inputmethod.BaseInputConnection.getComposingSpanStart(content);
        int b = android.view.inputmethod.BaseInputConnection.getComposingSpanEnd(content);
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, (("Composing span: " + a) + " to ") + b);

        if (b < a) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if ((a != (-1)) && (b != (-1))) {
            android.view.inputmethod.BaseInputConnection.removeComposingSpans(content);
        } else {
            a = android.text.Selection.getSelectionStart(content);
            b = android.text.Selection.getSelectionEnd(content);
            if (a < 0)
                a = 0;

            if (b < 0)
                b = 0;

            if (b < a) {
                int tmp = a;
                a = b;
                b = tmp;
            }
        }
        if (composing) {
            android.text.Spannable sp = null;
            if (!(text instanceof android.text.Spannable)) {
                sp = new android.text.SpannableStringBuilder(text);
                text = sp;
                ensureDefaultComposingSpans();
                if (mDefaultComposingSpans != null) {
                    for (int i = 0; i < mDefaultComposingSpans.length; ++i) {
                        sp.setSpan(mDefaultComposingSpans[i], 0, sp.length(), android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE | android.text.Spanned.SPAN_COMPOSING);
                    }
                }
            } else {
                sp = ((android.text.Spannable) (text));
            }
            android.view.inputmethod.BaseInputConnection.setComposingSpans(sp);
        }
        if (android.view.inputmethod.BaseInputConnection.DEBUG)
            android.util.Log.v(android.view.inputmethod.BaseInputConnection.TAG, (((((((("Replacing from " + a) + " to ") + b) + " with \"") + text) + "\", composing=") + composing) + ", type=") + text.getClass().getCanonicalName());

        if (android.view.inputmethod.BaseInputConnection.DEBUG) {
            android.util.LogPrinter lp = new android.util.LogPrinter(android.util.Log.VERBOSE, android.view.inputmethod.BaseInputConnection.TAG);
            lp.println("Current text:");
            android.text.TextUtils.dumpSpans(content, lp, "  ");
            lp.println("Composing text:");
            android.text.TextUtils.dumpSpans(text, lp, "  ");
        }
        // Position the cursor appropriately, so that after replacing the
        // desired range of text it will be located in the correct spot.
        // This allows us to deal with filters performing edits on the text
        // we are providing here.
        if (newCursorPosition > 0) {
            newCursorPosition += b - 1;
        } else {
            newCursorPosition += a;
        }
        if (newCursorPosition < 0)
            newCursorPosition = 0;

        if (newCursorPosition > content.length())
            newCursorPosition = content.length();

        android.text.Selection.setSelection(content, newCursorPosition);
        content.replace(a, b, text);
        if (android.view.inputmethod.BaseInputConnection.DEBUG) {
            android.util.LogPrinter lp = new android.util.LogPrinter(android.util.Log.VERBOSE, android.view.inputmethod.BaseInputConnection.TAG);
            lp.println("Final text:");
            android.text.TextUtils.dumpSpans(content, lp, "  ");
        }
        endBatchEdit();
    }

    /**
     * The default implementation does nothing.
     */
    public boolean commitContent(android.view.inputmethod.InputContentInfo inputContentInfo, int flags, android.os.Bundle opts) {
        return false;
    }
}

