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


/**
 * Implements the internal IInputMethod interface to convert incoming calls
 * on to it back to calls on the public InputMethod interface, scheduling
 * them on the main thread of the process.
 */
class IInputMethodWrapper extends com.android.internal.view.IInputMethod.Stub implements com.android.internal.os.HandlerCaller.Callback {
    private static final java.lang.String TAG = "InputMethodWrapper";

    private static final int DO_DUMP = 1;

    private static final int DO_ATTACH_TOKEN = 10;

    private static final int DO_SET_INPUT_CONTEXT = 20;

    private static final int DO_UNSET_INPUT_CONTEXT = 30;

    private static final int DO_START_INPUT = 32;

    private static final int DO_RESTART_INPUT = 34;

    private static final int DO_CREATE_SESSION = 40;

    private static final int DO_SET_SESSION_ENABLED = 45;

    private static final int DO_REVOKE_SESSION = 50;

    private static final int DO_SHOW_SOFT_INPUT = 60;

    private static final int DO_HIDE_SOFT_INPUT = 70;

    private static final int DO_CHANGE_INPUTMETHOD_SUBTYPE = 80;

    final java.lang.ref.WeakReference<android.inputmethodservice.AbstractInputMethodService> mTarget;

    final android.content.Context mContext;

    final com.android.internal.os.HandlerCaller mCaller;

    final java.lang.ref.WeakReference<android.view.inputmethod.InputMethod> mInputMethod;

    final int mTargetSdkVersion;

    static class Notifier {
        boolean notified;
    }

    // NOTE: we should have a cache of these.
    static final class InputMethodSessionCallbackWrapper implements android.view.inputmethod.InputMethod.SessionCallback {
        final android.content.Context mContext;

        final android.view.InputChannel mChannel;

        final com.android.internal.view.IInputSessionCallback mCb;

        InputMethodSessionCallbackWrapper(android.content.Context context, android.view.InputChannel channel, com.android.internal.view.IInputSessionCallback cb) {
            mContext = context;
            mChannel = channel;
            mCb = cb;
        }

        @java.lang.Override
        public void sessionCreated(android.view.inputmethod.InputMethodSession session) {
            try {
                if (session != null) {
                    android.inputmethodservice.IInputMethodSessionWrapper wrap = new android.inputmethodservice.IInputMethodSessionWrapper(mContext, session, mChannel);
                    mCb.sessionCreated(wrap);
                } else {
                    if (mChannel != null) {
                        mChannel.dispose();
                    }
                    mCb.sessionCreated(null);
                }
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public IInputMethodWrapper(android.inputmethodservice.AbstractInputMethodService context, android.view.inputmethod.InputMethod inputMethod) {
        mTarget = new java.lang.ref.WeakReference<android.inputmethodservice.AbstractInputMethodService>(context);
        mContext = context.getApplicationContext();
        mCaller = /* asyncHandler */
        new com.android.internal.os.HandlerCaller(mContext, null, this, true);
        mInputMethod = new java.lang.ref.WeakReference<android.view.inputmethod.InputMethod>(inputMethod);
        mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
    }

    public android.view.inputmethod.InputMethod getInternalInputMethod() {
        return mInputMethod.get();
    }

    @java.lang.Override
    public void executeMessage(android.os.Message msg) {
        android.view.inputmethod.InputMethod inputMethod = mInputMethod.get();
        // Need a valid reference to the inputMethod for everything except a dump.
        if ((inputMethod == null) && (msg.what != android.inputmethodservice.IInputMethodWrapper.DO_DUMP)) {
            android.util.Log.w(android.inputmethodservice.IInputMethodWrapper.TAG, "Input method reference was null, ignoring message: " + msg.what);
            return;
        }
        switch (msg.what) {
            case android.inputmethodservice.IInputMethodWrapper.DO_DUMP :
                {
                    android.inputmethodservice.AbstractInputMethodService target = mTarget.get();
                    if (target == null) {
                        return;
                    }
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    try {
                        target.dump(((java.io.FileDescriptor) (args.arg1)), ((java.io.PrintWriter) (args.arg2)), ((java.lang.String[]) (args.arg3)));
                    } catch (java.lang.RuntimeException e) {
                        ((java.io.PrintWriter) (args.arg2)).println("Exception: " + e);
                    }
                    synchronized(args.arg4) {
                        ((java.util.concurrent.CountDownLatch) (args.arg4)).countDown();
                    }
                    args.recycle();
                    return;
                }
            case android.inputmethodservice.IInputMethodWrapper.DO_ATTACH_TOKEN :
                {
                    inputMethod.attachToken(((android.os.IBinder) (msg.obj)));
                    return;
                }
            case android.inputmethodservice.IInputMethodWrapper.DO_SET_INPUT_CONTEXT :
                {
                    inputMethod.bindInput(((android.view.inputmethod.InputBinding) (msg.obj)));
                    return;
                }
            case android.inputmethodservice.IInputMethodWrapper.DO_UNSET_INPUT_CONTEXT :
                inputMethod.unbindInput();
                return;
            case android.inputmethodservice.IInputMethodWrapper.DO_START_INPUT :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    int missingMethods = msg.arg1;
                    com.android.internal.view.IInputContext inputContext = ((com.android.internal.view.IInputContext) (args.arg1));
                    android.view.inputmethod.InputConnection ic = (inputContext != null) ? new com.android.internal.view.InputConnectionWrapper(mTarget, inputContext, missingMethods) : null;
                    android.view.inputmethod.EditorInfo info = ((android.view.inputmethod.EditorInfo) (args.arg2));
                    info.makeCompatible(mTargetSdkVersion);
                    inputMethod.startInput(ic, info);
                    args.recycle();
                    return;
                }
            case android.inputmethodservice.IInputMethodWrapper.DO_RESTART_INPUT :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    int missingMethods = msg.arg1;
                    com.android.internal.view.IInputContext inputContext = ((com.android.internal.view.IInputContext) (args.arg1));
                    android.view.inputmethod.InputConnection ic = (inputContext != null) ? new com.android.internal.view.InputConnectionWrapper(mTarget, inputContext, missingMethods) : null;
                    android.view.inputmethod.EditorInfo info = ((android.view.inputmethod.EditorInfo) (args.arg2));
                    info.makeCompatible(mTargetSdkVersion);
                    inputMethod.restartInput(ic, info);
                    args.recycle();
                    return;
                }
            case android.inputmethodservice.IInputMethodWrapper.DO_CREATE_SESSION :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    inputMethod.createSession(new android.inputmethodservice.IInputMethodWrapper.InputMethodSessionCallbackWrapper(mContext, ((android.view.InputChannel) (args.arg1)), ((com.android.internal.view.IInputSessionCallback) (args.arg2))));
                    args.recycle();
                    return;
                }
            case android.inputmethodservice.IInputMethodWrapper.DO_SET_SESSION_ENABLED :
                inputMethod.setSessionEnabled(((android.view.inputmethod.InputMethodSession) (msg.obj)), msg.arg1 != 0);
                return;
            case android.inputmethodservice.IInputMethodWrapper.DO_REVOKE_SESSION :
                inputMethod.revokeSession(((android.view.inputmethod.InputMethodSession) (msg.obj)));
                return;
            case android.inputmethodservice.IInputMethodWrapper.DO_SHOW_SOFT_INPUT :
                inputMethod.showSoftInput(msg.arg1, ((android.os.ResultReceiver) (msg.obj)));
                return;
            case android.inputmethodservice.IInputMethodWrapper.DO_HIDE_SOFT_INPUT :
                inputMethod.hideSoftInput(msg.arg1, ((android.os.ResultReceiver) (msg.obj)));
                return;
            case android.inputmethodservice.IInputMethodWrapper.DO_CHANGE_INPUTMETHOD_SUBTYPE :
                inputMethod.changeInputMethodSubtype(((android.view.inputmethod.InputMethodSubtype) (msg.obj)));
                return;
        }
        android.util.Log.w(android.inputmethodservice.IInputMethodWrapper.TAG, "Unhandled message code: " + msg.what);
    }

    @java.lang.Override
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        android.inputmethodservice.AbstractInputMethodService target = mTarget.get();
        if (target == null) {
            return;
        }
        if (target.checkCallingOrSelfPermission(android.Manifest.permission.DUMP) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            fout.println((("Permission Denial: can't dump InputMethodManager from from pid=" + android.os.Binder.getCallingPid()) + ", uid=") + android.os.Binder.getCallingUid());
            return;
        }
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        mCaller.executeOrSendMessage(mCaller.obtainMessageOOOO(android.inputmethodservice.IInputMethodWrapper.DO_DUMP, fd, fout, args, latch));
        try {
            if (!latch.await(5, java.util.concurrent.TimeUnit.SECONDS)) {
                fout.println("Timeout waiting for dump");
            }
        } catch (java.lang.InterruptedException e) {
            fout.println("Interrupted waiting for dump");
        }
    }

    @java.lang.Override
    public void attachToken(android.os.IBinder token) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodWrapper.DO_ATTACH_TOKEN, token));
    }

    @java.lang.Override
    public void bindInput(android.view.inputmethod.InputBinding binding) {
        // This IInputContext is guaranteed to implement all the methods.
        final int missingMethodFlags = 0;
        android.view.inputmethod.InputConnection ic = new com.android.internal.view.InputConnectionWrapper(mTarget, IInputContext.Stub.asInterface(binding.getConnectionToken()), missingMethodFlags);
        android.view.inputmethod.InputBinding nu = new android.view.inputmethod.InputBinding(ic, binding);
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodWrapper.DO_SET_INPUT_CONTEXT, nu));
    }

    @java.lang.Override
    public void unbindInput() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.inputmethodservice.IInputMethodWrapper.DO_UNSET_INPUT_CONTEXT));
    }

    @java.lang.Override
    public void startInput(com.android.internal.view.IInputContext inputContext, @android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    final int missingMethods, android.view.inputmethod.EditorInfo attribute) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIOO(android.inputmethodservice.IInputMethodWrapper.DO_START_INPUT, missingMethods, inputContext, attribute));
    }

    @java.lang.Override
    public void restartInput(com.android.internal.view.IInputContext inputContext, @android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    final int missingMethods, android.view.inputmethod.EditorInfo attribute) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIOO(android.inputmethodservice.IInputMethodWrapper.DO_RESTART_INPUT, missingMethods, inputContext, attribute));
    }

    @java.lang.Override
    public void createSession(android.view.InputChannel channel, com.android.internal.view.IInputSessionCallback callback) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageOO(android.inputmethodservice.IInputMethodWrapper.DO_CREATE_SESSION, channel, callback));
    }

    @java.lang.Override
    public void setSessionEnabled(com.android.internal.view.IInputMethodSession session, boolean enabled) {
        try {
            android.view.inputmethod.InputMethodSession ls = ((android.inputmethodservice.IInputMethodSessionWrapper) (session)).getInternalInputMethodSession();
            if (ls == null) {
                android.util.Log.w(android.inputmethodservice.IInputMethodWrapper.TAG, "Session is already finished: " + session);
                return;
            }
            mCaller.executeOrSendMessage(mCaller.obtainMessageIO(android.inputmethodservice.IInputMethodWrapper.DO_SET_SESSION_ENABLED, enabled ? 1 : 0, ls));
        } catch (java.lang.ClassCastException e) {
            android.util.Log.w(android.inputmethodservice.IInputMethodWrapper.TAG, "Incoming session not of correct type: " + session, e);
        }
    }

    @java.lang.Override
    public void revokeSession(com.android.internal.view.IInputMethodSession session) {
        try {
            android.view.inputmethod.InputMethodSession ls = ((android.inputmethodservice.IInputMethodSessionWrapper) (session)).getInternalInputMethodSession();
            if (ls == null) {
                android.util.Log.w(android.inputmethodservice.IInputMethodWrapper.TAG, "Session is already finished: " + session);
                return;
            }
            mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodWrapper.DO_REVOKE_SESSION, ls));
        } catch (java.lang.ClassCastException e) {
            android.util.Log.w(android.inputmethodservice.IInputMethodWrapper.TAG, "Incoming session not of correct type: " + session, e);
        }
    }

    @java.lang.Override
    public void showSoftInput(int flags, android.os.ResultReceiver resultReceiver) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIO(android.inputmethodservice.IInputMethodWrapper.DO_SHOW_SOFT_INPUT, flags, resultReceiver));
    }

    @java.lang.Override
    public void hideSoftInput(int flags, android.os.ResultReceiver resultReceiver) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIO(android.inputmethodservice.IInputMethodWrapper.DO_HIDE_SOFT_INPUT, flags, resultReceiver));
    }

    @java.lang.Override
    public void changeInputMethodSubtype(android.view.inputmethod.InputMethodSubtype subtype) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.inputmethodservice.IInputMethodWrapper.DO_CHANGE_INPUTMETHOD_SUBTYPE, subtype));
    }
}

