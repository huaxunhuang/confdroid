/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.view.inputmethod;


/**
 * <p>Wrapper class for proxying calls to another InputConnection.  Subclass and have fun!
 */
public class InputConnectionWrapper implements android.view.inputmethod.InputConnection {
    private android.view.inputmethod.InputConnection mTarget;

    final boolean mMutable;

    @android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    private int mMissingMethodFlags;

    /**
     * Initializes a wrapper.
     *
     * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
     * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
     * has {@code null} in {@code target}.</p>
     *
     * @param target
     * 		the {@link InputConnection} to be proxied.
     * @param mutable
     * 		set {@code true} to protect this object from being reconfigured to target
     * 		another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
     */
    public InputConnectionWrapper(android.view.inputmethod.InputConnection target, boolean mutable) {
        mMutable = mutable;
        mTarget = target;
        mMissingMethodFlags = android.view.inputmethod.InputConnectionInspector.getMissingMethodFlags(target);
    }

    /**
     * Change the target of the input connection.
     *
     * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
     * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
     * has {@code null} in {@code target}.</p>
     *
     * @param target
     * 		the {@link InputConnection} to be proxied.
     * @throws SecurityException
     * 		when this wrapper has non-null target and is immutable.
     */
    public void setTarget(android.view.inputmethod.InputConnection target) {
        if ((mTarget != null) && (!mMutable)) {
            throw new java.lang.SecurityException("not mutable");
        }
        mTarget = target;
        mMissingMethodFlags = android.view.inputmethod.InputConnectionInspector.getMissingMethodFlags(target);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    public int getMissingMethodFlags() {
        return mMissingMethodFlags;
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public java.lang.CharSequence getTextBeforeCursor(int n, int flags) {
        return mTarget.getTextBeforeCursor(n, flags);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public java.lang.CharSequence getTextAfterCursor(int n, int flags) {
        return mTarget.getTextAfterCursor(n, flags);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public java.lang.CharSequence getSelectedText(int flags) {
        return mTarget.getSelectedText(flags);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public int getCursorCapsMode(int reqModes) {
        return mTarget.getCursorCapsMode(reqModes);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public android.view.inputmethod.ExtractedText getExtractedText(android.view.inputmethod.ExtractedTextRequest request, int flags) {
        return mTarget.getExtractedText(request, flags);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        return mTarget.deleteSurroundingTextInCodePoints(beforeLength, afterLength);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        return mTarget.deleteSurroundingText(beforeLength, afterLength);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean setComposingText(java.lang.CharSequence text, int newCursorPosition) {
        return mTarget.setComposingText(text, newCursorPosition);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean setComposingRegion(int start, int end) {
        return mTarget.setComposingRegion(start, end);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean finishComposingText() {
        return mTarget.finishComposingText();
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean commitText(java.lang.CharSequence text, int newCursorPosition) {
        return mTarget.commitText(text, newCursorPosition);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean commitCompletion(android.view.inputmethod.CompletionInfo text) {
        return mTarget.commitCompletion(text);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean commitCorrection(android.view.inputmethod.CorrectionInfo correctionInfo) {
        return mTarget.commitCorrection(correctionInfo);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean setSelection(int start, int end) {
        return mTarget.setSelection(start, end);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean performEditorAction(int editorAction) {
        return mTarget.performEditorAction(editorAction);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean performContextMenuAction(int id) {
        return mTarget.performContextMenuAction(id);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean beginBatchEdit() {
        return mTarget.beginBatchEdit();
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean endBatchEdit() {
        return mTarget.endBatchEdit();
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean sendKeyEvent(android.view.KeyEvent event) {
        return mTarget.sendKeyEvent(event);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean clearMetaKeyStates(int states) {
        return mTarget.clearMetaKeyStates(states);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean reportFullscreenMode(boolean enabled) {
        return mTarget.reportFullscreenMode(enabled);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean performPrivateCommand(java.lang.String action, android.os.Bundle data) {
        return mTarget.performPrivateCommand(action, data);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean requestCursorUpdates(int cursorUpdateMode) {
        return mTarget.requestCursorUpdates(cursorUpdateMode);
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public android.os.Handler getHandler() {
        return mTarget.getHandler();
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public void closeConnection() {
        mTarget.closeConnection();
    }

    /**
     * {@inheritDoc }
     *
     * @throws NullPointerException
     * 		if the target is {@code null}.
     */
    @java.lang.Override
    public boolean commitContent(android.view.inputmethod.InputContentInfo inputContentInfo, int flags, android.os.Bundle opts) {
        return mTarget.commitContent(inputContentInfo, flags, opts);
    }
}

