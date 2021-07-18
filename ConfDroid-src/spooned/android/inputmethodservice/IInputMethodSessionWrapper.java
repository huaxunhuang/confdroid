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
package android.inputmethodservice;


class IInputMethodSessionWrapper extends com.android.internal.view.IInputMethodSession.Stub implements com.android.internal.os.HandlerCaller.Callback {
    private static final java.lang.String TAG = "InputMethodWrapper";

    private static final int DO_FINISH_INPUT = 60;

    private static final int DO_DISPLAY_COMPLETIONS = 65;

    private static final int DO_UPDATE_EXTRACTED_TEXT = 67;

    private static final int DO_UPDATE_SELECTION = 90;

    private static final int DO_UPDATE_CURSOR = 95;

    private static final int DO_UPDATE_CURSOR_ANCHOR_INFO = 99;

    private static final int DO_APP_PRIVATE_COMMAND = 100;

    private static final int DO_TOGGLE_SOFT_INPUT = 105;

    private static final int DO_FINISH_SESSION = 110;

    private static final int DO_VIEW_CLICKED = 115;

    com.android.internal.os.HandlerCaller mCaller;

    android.view.inputmethod.InputMethodSession mInputMethodSession;

    android.view.InputChannel mChannel;

    android.inputmethodservice.IInputMethodSessionWrapper.ImeInputEventReceiver mReceiver;

    public IInputMethodSessionWrapper(android.content.Context context, android.view.inputmethod.InputMethodSession inputMethodSession, android.view.InputChannel channel) {
        mCaller = /* asyncHandler */
        new com.android.internal.os.HandlerCaller(context, null, this, true);
        mInputMethodSession = inputMethodSession;
        mChannel = channel;
        if (channel != null) {
            mReceiver = new android.inputmethodservice.IInputMethodSessionWrapper.ImeInputEventReceiver(channel, context.getMainLooper());
        }
    }

    public android.view.inputmethod.InputMethodSession getInternalInputMethodSession() {
        return mInputMethodSession;
    }

    @java.lang.Override
    public void executeMessage(android.os.Message msg) {
        if (mInputMethodSession == null) {
            // The session has been finished. Args needs to be recycled
            // for cases below.
            switch (msg.what) {
                case android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_SELECTION :
                case android.inputmethodservice.IInputMethodSessionWrapper.DO_APP_PRIVATE_COMMAND :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        args.recycle();
                    }
            }
            return;
        }
        switch (msg.what) {
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_FINISH_INPUT :
                mInputMethodSession.finishInput();
                return;
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_DISPLAY_COMPLETIONS :
                mInputMethodSession.displayCompletions(((android.view.inputmethod.CompletionInfo[]) (msg.obj)));
                return;
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_EXTRACTED_TEXT :
                mInputMethodSession.updateExtractedText(msg.arg1, ((android.view.inputmethod.ExtractedText) (msg.obj)));
                return;
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_SELECTION :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    mInputMethodSession.updateSelection(args.argi1, args.argi2, args.argi3, args.argi4, args.argi5, args.argi6);
                    args.recycle();
                    return;
                }
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_CURSOR :
                {
                    mInputMethodSession.updateCursor(((android.graphics.Rect) (msg.obj)));
                    return;
                }
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_CURSOR_ANCHOR_INFO :
                {
                    mInputMethodSession.updateCursorAnchorInfo(((android.view.inputmethod.CursorAnchorInfo) (msg.obj)));
                    return;
                }
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_APP_PRIVATE_COMMAND :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    mInputMethodSession.appPrivateCommand(((java.lang.String) (args.arg1)), ((android.os.Bundle) (args.arg2)));
                    args.recycle();
                    return;
                }
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_TOGGLE_SOFT_INPUT :
                {
                    mInputMethodSession.toggleSoftInput(msg.arg1, msg.arg2);
                    return;
                }
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_FINISH_SESSION :
                {
                    doFinishSession();
                    return;
                }
            case android.inputmethodservice.IInputMethodSessionWrapper.DO_VIEW_CLICKED :
                {
                    mInputMethodSession.viewClicked(msg.arg1 == 1);
                    return;
                }
        }
        android.util.Log.w(android.inputmethodservice.IInputMethodSessionWrapper.TAG, "Unhandled message code: " + msg.what);
    }

    private void doFinishSession() {
        mInputMethodSession = null;
        if (mReceiver != null) {
            mReceiver.dispose();
            mReceiver = null;
        }
        if (mChannel != null) {
            mChannel.dispose();
            mChannel = null;
        }
    }

    @java.lang.Override
    public void finishInput() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.inputmethodservice.IInputMethodSessionWrapper.DO_FINISH_INPUT));
    }

    @java.lang.Override
    public void displayCompletions(android.view.inputmethod.CompletionInfo[] completions) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodSessionWrapper.DO_DISPLAY_COMPLETIONS, completions));
    }

    @java.lang.Override
    public void updateExtractedText(int token, android.view.inputmethod.ExtractedText text) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIO(android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_EXTRACTED_TEXT, token, text));
    }

    @java.lang.Override
    public void updateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIIIIII(android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_SELECTION, oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd));
    }

    @java.lang.Override
    public void viewClicked(boolean focusChanged) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageI(android.inputmethodservice.IInputMethodSessionWrapper.DO_VIEW_CLICKED, focusChanged ? 1 : 0));
    }

    @java.lang.Override
    public void updateCursor(android.graphics.Rect newCursor) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_CURSOR, newCursor));
    }

    @java.lang.Override
    public void updateCursorAnchorInfo(android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodSessionWrapper.DO_UPDATE_CURSOR_ANCHOR_INFO, cursorAnchorInfo));
    }

    @java.lang.Override
    public void appPrivateCommand(java.lang.String action, android.os.Bundle data) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageOO(android.inputmethodservice.IInputMethodSessionWrapper.DO_APP_PRIVATE_COMMAND, action, data));
    }

    @java.lang.Override
    public void toggleSoftInput(int showFlags, int hideFlags) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageII(android.inputmethodservice.IInputMethodSessionWrapper.DO_TOGGLE_SOFT_INPUT, showFlags, hideFlags));
    }

    @java.lang.Override
    public void finishSession() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.inputmethodservice.IInputMethodSessionWrapper.DO_FINISH_SESSION));
    }

    private final class ImeInputEventReceiver extends android.view.InputEventReceiver implements android.view.inputmethod.InputMethodSession.EventCallback {
        private final android.util.SparseArray<android.view.InputEvent> mPendingEvents = new android.util.SparseArray<android.view.InputEvent>();

        public ImeInputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper) {
            super(inputChannel, looper);
        }

        @java.lang.Override
        public void onInputEvent(android.view.InputEvent event) {
            if (mInputMethodSession == null) {
                // The session has been finished.
                finishInputEvent(event, false);
                return;
            }
            final int seq = event.getSequenceNumber();
            mPendingEvents.put(seq, event);
            if (event instanceof android.view.KeyEvent) {
                android.view.KeyEvent keyEvent = ((android.view.KeyEvent) (event));
                mInputMethodSession.dispatchKeyEvent(seq, keyEvent, this);
            } else {
                android.view.MotionEvent motionEvent = ((android.view.MotionEvent) (event));
                if (motionEvent.isFromSource(android.view.InputDevice.SOURCE_CLASS_TRACKBALL)) {
                    mInputMethodSession.dispatchTrackballEvent(seq, motionEvent, this);
                } else {
                    mInputMethodSession.dispatchGenericMotionEvent(seq, motionEvent, this);
                }
            }
        }

        @java.lang.Override
        public void finishedEvent(int seq, boolean handled) {
            int index = mPendingEvents.indexOfKey(seq);
            if (index >= 0) {
                android.view.InputEvent event = mPendingEvents.valueAt(index);
                mPendingEvents.removeAt(index);
                finishInputEvent(event, handled);
            }
        }
    }
}

