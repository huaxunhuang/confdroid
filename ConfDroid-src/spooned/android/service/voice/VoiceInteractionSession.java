/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.service.voice;


/**
 * An active voice interaction session, providing a facility for the implementation
 * to interact with the user in the voice interaction layer.  The user interface is
 * initially shown by default, and can be created be overriding {@link #onCreateContentView()}
 * in which the UI can be built.
 *
 * <p>A voice interaction session can be self-contained, ultimately calling {@link #finish}
 * when done.  It can also initiate voice interactions with applications by calling
 * {@link #startVoiceActivity}</p>.
 */
public class VoiceInteractionSession implements android.content.ComponentCallbacks2 , android.view.KeyEvent.Callback {
    static final java.lang.String TAG = "VoiceInteractionSession";

    static final boolean DEBUG = false;

    /**
     * Flag received in {@link #onShow}: originator requested that the session be started with
     * assist data from the currently focused activity.
     */
    public static final int SHOW_WITH_ASSIST = 1 << 0;

    /**
     * Flag received in {@link #onShow}: originator requested that the session be started with
     * a screen shot of the currently focused activity.
     */
    public static final int SHOW_WITH_SCREENSHOT = 1 << 1;

    /**
     * Flag for use with {@link #onShow}: indicates that the session has been started from the
     * system assist gesture.
     */
    public static final int SHOW_SOURCE_ASSIST_GESTURE = 1 << 2;

    /**
     * Flag for use with {@link #onShow}: indicates that the application itself has invoked
     * the assistant.
     */
    public static final int SHOW_SOURCE_APPLICATION = 1 << 3;

    /**
     * Flag for use with {@link #onShow}: indicates that an Activity has invoked the voice
     * interaction service for a local interaction using
     * {@link Activity#startLocalVoiceInteraction(Bundle)}.
     */
    public static final int SHOW_SOURCE_ACTIVITY = 1 << 4;

    // Keys for Bundle values
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String KEY_DATA = "data";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String KEY_STRUCTURE = "structure";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String KEY_CONTENT = "content";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String KEY_RECEIVER_EXTRAS = "receiverExtras";

    final android.content.Context mContext;

    final com.android.internal.os.HandlerCaller mHandlerCaller;

    final android.view.KeyEvent.DispatcherState mDispatcherState = new android.view.KeyEvent.DispatcherState();

    com.android.internal.app.IVoiceInteractionManagerService mSystemService;

    android.os.IBinder mToken;

    int mTheme = 0;

    android.view.LayoutInflater mInflater;

    android.content.res.TypedArray mThemeAttrs;

    android.view.View mRootView;

    android.widget.FrameLayout mContentFrame;

    android.inputmethodservice.SoftInputWindow mWindow;

    boolean mInitialized;

    boolean mWindowAdded;

    boolean mWindowVisible;

    boolean mWindowWasVisible;

    boolean mInShowWindow;

    final android.util.ArrayMap<android.os.IBinder, android.service.voice.VoiceInteractionSession.Request> mActiveRequests = new android.util.ArrayMap<android.os.IBinder, android.service.voice.VoiceInteractionSession.Request>();

    final android.service.voice.VoiceInteractionSession.Insets mTmpInsets = new android.service.voice.VoiceInteractionSession.Insets();

    final java.lang.ref.WeakReference<android.service.voice.VoiceInteractionSession> mWeakRef = new java.lang.ref.WeakReference<android.service.voice.VoiceInteractionSession>(this);

    final com.android.internal.app.IVoiceInteractor mInteractor = new com.android.internal.app.IVoiceInteractor.Stub() {
        @java.lang.Override
        public com.android.internal.app.IVoiceInteractorRequest startConfirmation(java.lang.String callingPackage, com.android.internal.app.IVoiceInteractorCallback callback, android.app.VoiceInteractor.Prompt prompt, android.os.Bundle extras) {
            android.service.voice.VoiceInteractionSession.ConfirmationRequest request = new android.service.voice.VoiceInteractionSession.ConfirmationRequest(callingPackage, android.os.Binder.getCallingUid(), callback, android.service.voice.VoiceInteractionSession.this, prompt, extras);
            addRequest(request);
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_START_CONFIRMATION, request));
            return request.mInterface;
        }

        @java.lang.Override
        public com.android.internal.app.IVoiceInteractorRequest startPickOption(java.lang.String callingPackage, com.android.internal.app.IVoiceInteractorCallback callback, android.app.VoiceInteractor.Prompt prompt, android.app.VoiceInteractor.PickOptionRequest.Option[] options, android.os.Bundle extras) {
            android.service.voice.VoiceInteractionSession.PickOptionRequest request = new android.service.voice.VoiceInteractionSession.PickOptionRequest(callingPackage, android.os.Binder.getCallingUid(), callback, android.service.voice.VoiceInteractionSession.this, prompt, options, extras);
            addRequest(request);
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_START_PICK_OPTION, request));
            return request.mInterface;
        }

        @java.lang.Override
        public com.android.internal.app.IVoiceInteractorRequest startCompleteVoice(java.lang.String callingPackage, com.android.internal.app.IVoiceInteractorCallback callback, android.app.VoiceInteractor.Prompt message, android.os.Bundle extras) {
            android.service.voice.VoiceInteractionSession.CompleteVoiceRequest request = new android.service.voice.VoiceInteractionSession.CompleteVoiceRequest(callingPackage, android.os.Binder.getCallingUid(), callback, android.service.voice.VoiceInteractionSession.this, message, extras);
            addRequest(request);
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_START_COMPLETE_VOICE, request));
            return request.mInterface;
        }

        @java.lang.Override
        public com.android.internal.app.IVoiceInteractorRequest startAbortVoice(java.lang.String callingPackage, com.android.internal.app.IVoiceInteractorCallback callback, android.app.VoiceInteractor.Prompt message, android.os.Bundle extras) {
            android.service.voice.VoiceInteractionSession.AbortVoiceRequest request = new android.service.voice.VoiceInteractionSession.AbortVoiceRequest(callingPackage, android.os.Binder.getCallingUid(), callback, android.service.voice.VoiceInteractionSession.this, message, extras);
            addRequest(request);
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_START_ABORT_VOICE, request));
            return request.mInterface;
        }

        @java.lang.Override
        public com.android.internal.app.IVoiceInteractorRequest startCommand(java.lang.String callingPackage, com.android.internal.app.IVoiceInteractorCallback callback, java.lang.String command, android.os.Bundle extras) {
            android.service.voice.VoiceInteractionSession.CommandRequest request = new android.service.voice.VoiceInteractionSession.CommandRequest(callingPackage, android.os.Binder.getCallingUid(), callback, android.service.voice.VoiceInteractionSession.this, command, extras);
            addRequest(request);
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_START_COMMAND, request));
            return request.mInterface;
        }

        @java.lang.Override
        public boolean[] supportsCommands(java.lang.String callingPackage, java.lang.String[] commands) {
            android.os.Message msg = mHandlerCaller.obtainMessageIOO(android.service.voice.VoiceInteractionSession.MSG_SUPPORTS_COMMANDS, 0, commands, null);
            com.android.internal.os.SomeArgs args = mHandlerCaller.sendMessageAndWait(msg);
            if (args != null) {
                boolean[] res = ((boolean[]) (args.arg1));
                args.recycle();
                return res;
            }
            return new boolean[commands.length];
        }
    };

    final android.service.voice.IVoiceInteractionSession mSession = new android.service.voice.IVoiceInteractionSession.Stub() {
        @java.lang.Override
        public void show(android.os.Bundle sessionArgs, int flags, com.android.internal.app.IVoiceInteractionSessionShowCallback showCallback) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOO(android.service.voice.VoiceInteractionSession.MSG_SHOW, flags, sessionArgs, showCallback));
        }

        @java.lang.Override
        public void hide() {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(android.service.voice.VoiceInteractionSession.MSG_HIDE));
        }

        @java.lang.Override
        public void handleAssist(final android.os.Bundle data, final android.app.assist.AssistStructure structure, final android.app.assist.AssistContent content, final int index, final int count) {
            // We want to pre-warm the AssistStructure before handing it off to the main
            // thread.  We also want to do this on a separate thread, so that if the app
            // is for some reason slow (due to slow filling in of async children in the
            // structure), we don't block other incoming IPCs (such as the screenshot) to
            // us (since we are a oneway interface, they get serialized).  (Okay?)
            java.lang.Thread retriever = new java.lang.Thread("AssistStructure retriever") {
                @java.lang.Override
                public void run() {
                    java.lang.Throwable failure = null;
                    if (structure != null) {
                        try {
                            structure.ensureData();
                        } catch (java.lang.Throwable e) {
                            android.util.Log.w(android.service.voice.VoiceInteractionSession.TAG, "Failure retrieving AssistStructure", e);
                            failure = e;
                        }
                    }
                    mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageOOOOII(android.service.voice.VoiceInteractionSession.MSG_HANDLE_ASSIST, data, failure == null ? structure : null, failure, content, index, count));
                }
            };
            retriever.start();
        }

        @java.lang.Override
        public void handleScreenshot(android.graphics.Bitmap screenshot) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_HANDLE_SCREENSHOT, screenshot));
        }

        @java.lang.Override
        public void taskStarted(android.content.Intent intent, int taskId) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIO(android.service.voice.VoiceInteractionSession.MSG_TASK_STARTED, taskId, intent));
        }

        @java.lang.Override
        public void taskFinished(android.content.Intent intent, int taskId) {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIO(android.service.voice.VoiceInteractionSession.MSG_TASK_FINISHED, taskId, intent));
        }

        @java.lang.Override
        public void closeSystemDialogs() {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(android.service.voice.VoiceInteractionSession.MSG_CLOSE_SYSTEM_DIALOGS));
        }

        @java.lang.Override
        public void onLockscreenShown() {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(android.service.voice.VoiceInteractionSession.MSG_ON_LOCKSCREEN_SHOWN));
        }

        @java.lang.Override
        public void destroy() {
            mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(android.service.voice.VoiceInteractionSession.MSG_DESTROY));
        }
    };

    /**
     * Base class representing a request from a voice-driver app to perform a particular
     * voice operation with the user.  See related subclasses for the types of requests
     * that are possible.
     */
    public static class Request {
        final com.android.internal.app.IVoiceInteractorRequest mInterface = new com.android.internal.app.IVoiceInteractorRequest.Stub() {
            @java.lang.Override
            public void cancel() throws android.os.RemoteException {
                android.service.voice.VoiceInteractionSession session = mSession.get();
                if (session != null) {
                    session.mHandlerCaller.sendMessage(session.mHandlerCaller.obtainMessageO(android.service.voice.VoiceInteractionSession.MSG_CANCEL, android.service.voice.VoiceInteractionSession.Request.this));
                }
            }
        };

        final java.lang.String mCallingPackage;

        final int mCallingUid;

        final com.android.internal.app.IVoiceInteractorCallback mCallback;

        final java.lang.ref.WeakReference<android.service.voice.VoiceInteractionSession> mSession;

        final android.os.Bundle mExtras;

        Request(java.lang.String packageName, int uid, com.android.internal.app.IVoiceInteractorCallback callback, android.service.voice.VoiceInteractionSession session, android.os.Bundle extras) {
            mCallingPackage = packageName;
            mCallingUid = uid;
            mCallback = callback;
            mSession = session.mWeakRef;
            mExtras = extras;
        }

        /**
         * Return the uid of the application that initiated the request.
         */
        public int getCallingUid() {
            return mCallingUid;
        }

        /**
         * Return the package name of the application that initiated the request.
         */
        public java.lang.String getCallingPackage() {
            return mCallingPackage;
        }

        /**
         * Return any additional extra information that was supplied as part of the request.
         */
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Check whether this request is currently active.  A request becomes inactive after
         * calling {@link #cancel} or a final result method that completes the request.  After
         * this point, further interactions with the request will result in
         * {@link java.lang.IllegalStateException} errors; you should not catch these errors,
         * but can use this method if you need to determine the state of the request.  Returns
         * true if the request is still active.
         */
        public boolean isActive() {
            android.service.voice.VoiceInteractionSession session = mSession.get();
            if (session == null) {
                return false;
            }
            return session.isRequestActive(mInterface.asBinder());
        }

        void finishRequest() {
            android.service.voice.VoiceInteractionSession session = mSession.get();
            if (session == null) {
                throw new java.lang.IllegalStateException("VoiceInteractionSession has been destroyed");
            }
            android.service.voice.VoiceInteractionSession.Request req = session.removeRequest(mInterface.asBinder());
            if (req == null) {
                throw new java.lang.IllegalStateException("Request not active: " + this);
            } else
                if (req != this) {
                    throw new java.lang.IllegalStateException((("Current active request " + req) + " not same as calling request ") + this);
                }

        }

        /**
         * Ask the app to cancel this current request.
         * This also finishes the request (it is no longer active).
         */
        public void cancel() {
            try {
                if (android.service.voice.VoiceInteractionSession.DEBUG)
                    android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "sendCancelResult: req=" + mInterface);

                finishRequest();
                mCallback.deliverCancel(mInterface);
            } catch (android.os.RemoteException e) {
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            android.util.DebugUtils.buildShortClassTag(this, sb);
            sb.append(" ");
            sb.append(mInterface.asBinder());
            sb.append(" pkg=");
            sb.append(mCallingPackage);
            sb.append(" uid=");
            android.os.UserHandle.formatUid(sb, mCallingUid);
            sb.append('}');
            return sb.toString();
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            writer.print(prefix);
            writer.print("mInterface=");
            writer.println(mInterface.asBinder());
            writer.print(prefix);
            writer.print("mCallingPackage=");
            writer.print(mCallingPackage);
            writer.print(" mCallingUid=");
            android.os.UserHandle.formatUid(writer, mCallingUid);
            writer.println();
            writer.print(prefix);
            writer.print("mCallback=");
            writer.println(mCallback.asBinder());
            if (mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(mExtras);
            }
        }
    }

    /**
     * A request for confirmation from the user of an operation, as per
     * {@link android.app.VoiceInteractor.ConfirmationRequest
     * VoiceInteractor.ConfirmationRequest}.
     */
    public static final class ConfirmationRequest extends android.service.voice.VoiceInteractionSession.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        ConfirmationRequest(java.lang.String packageName, int uid, com.android.internal.app.IVoiceInteractorCallback callback, android.service.voice.VoiceInteractionSession session, android.app.VoiceInteractor.Prompt prompt, android.os.Bundle extras) {
            super(packageName, uid, callback, session, extras);
            mPrompt = prompt;
        }

        /**
         * Return the prompt informing the user of what will happen, as per
         * {@link android.app.VoiceInteractor.ConfirmationRequest
         * VoiceInteractor.ConfirmationRequest}.
         */
        @android.annotation.Nullable
        public android.app.VoiceInteractor.Prompt getVoicePrompt() {
            return mPrompt;
        }

        /**
         * Return the prompt informing the user of what will happen, as per
         * {@link android.app.VoiceInteractor.ConfirmationRequest
         * VoiceInteractor.ConfirmationRequest}.
         *
         * @deprecated Prefer {@link #getVoicePrompt()} which allows multiple voice prompts.
         */
        @android.annotation.Nullable
        public java.lang.CharSequence getPrompt() {
            return mPrompt != null ? mPrompt.getVoicePromptAt(0) : null;
        }

        /**
         * Report that the voice interactor has confirmed the operation with the user, resulting
         * in a call to
         * {@link android.app.VoiceInteractor.ConfirmationRequest#onConfirmationResult
         * VoiceInteractor.ConfirmationRequest.onConfirmationResult}.
         * This finishes the request (it is no longer active).
         */
        public void sendConfirmationResult(boolean confirmed, android.os.Bundle result) {
            try {
                if (android.service.voice.VoiceInteractionSession.DEBUG)
                    android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (((("sendConfirmationResult: req=" + mInterface) + " confirmed=") + confirmed) + " result=") + result);

                finishRequest();
                mCallback.deliverConfirmationResult(mInterface, confirmed, result);
            } catch (android.os.RemoteException e) {
            }
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
        }
    }

    /**
     * A request for the user to pick from a set of option, as per
     * {@link android.app.VoiceInteractor.PickOptionRequest VoiceInteractor.PickOptionRequest}.
     */
    public static final class PickOptionRequest extends android.service.voice.VoiceInteractionSession.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        final android.app.VoiceInteractor.PickOptionRequest.Option[] mOptions;

        PickOptionRequest(java.lang.String packageName, int uid, com.android.internal.app.IVoiceInteractorCallback callback, android.service.voice.VoiceInteractionSession session, android.app.VoiceInteractor.Prompt prompt, android.app.VoiceInteractor.PickOptionRequest.Option[] options, android.os.Bundle extras) {
            super(packageName, uid, callback, session, extras);
            mPrompt = prompt;
            mOptions = options;
        }

        /**
         * Return the prompt informing the user of what they are picking, as per
         * {@link android.app.VoiceInteractor.PickOptionRequest VoiceInteractor.PickOptionRequest}.
         */
        @android.annotation.Nullable
        public android.app.VoiceInteractor.Prompt getVoicePrompt() {
            return mPrompt;
        }

        /**
         * Return the prompt informing the user of what they are picking, as per
         * {@link android.app.VoiceInteractor.PickOptionRequest VoiceInteractor.PickOptionRequest}.
         *
         * @deprecated Prefer {@link #getVoicePrompt()} which allows multiple voice prompts.
         */
        @android.annotation.Nullable
        public java.lang.CharSequence getPrompt() {
            return mPrompt != null ? mPrompt.getVoicePromptAt(0) : null;
        }

        /**
         * Return the set of options the user is picking from, as per
         * {@link android.app.VoiceInteractor.PickOptionRequest VoiceInteractor.PickOptionRequest}.
         */
        public android.app.VoiceInteractor.PickOptionRequest.Option[] getOptions() {
            return mOptions;
        }

        void sendPickOptionResult(boolean finished, android.app.VoiceInteractor.PickOptionRequest.Option[] selections, android.os.Bundle result) {
            try {
                if (android.service.voice.VoiceInteractionSession.DEBUG)
                    android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (((((("sendPickOptionResult: req=" + mInterface) + " finished=") + finished) + " selections=") + selections) + " result=") + result);

                if (finished) {
                    finishRequest();
                }
                mCallback.deliverPickOptionResult(mInterface, finished, selections, result);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Report an intermediate option selection from the request, without completing it (the
         * request is still active and the app is waiting for the final option selection),
         * resulting in a call to
         * {@link android.app.VoiceInteractor.PickOptionRequest#onPickOptionResult
         * VoiceInteractor.PickOptionRequest.onPickOptionResult} with false for finished.
         */
        public void sendIntermediatePickOptionResult(android.app.VoiceInteractor.PickOptionRequest.Option[] selections, android.os.Bundle result) {
            sendPickOptionResult(false, selections, result);
        }

        /**
         * Report the final option selection for the request, completing the request
         * and resulting in a call to
         * {@link android.app.VoiceInteractor.PickOptionRequest#onPickOptionResult
         * VoiceInteractor.PickOptionRequest.onPickOptionResult} with false for finished.
         * This finishes the request (it is no longer active).
         */
        public void sendPickOptionResult(android.app.VoiceInteractor.PickOptionRequest.Option[] selections, android.os.Bundle result) {
            sendPickOptionResult(true, selections, result);
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
            if (mOptions != null) {
                writer.print(prefix);
                writer.println("Options:");
                for (int i = 0; i < mOptions.length; i++) {
                    android.app.VoiceInteractor.PickOptionRequest.Option op = mOptions[i];
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.println(":");
                    writer.print(prefix);
                    writer.print("    mLabel=");
                    writer.println(op.getLabel());
                    writer.print(prefix);
                    writer.print("    mIndex=");
                    writer.println(op.getIndex());
                    if (op.countSynonyms() > 0) {
                        writer.print(prefix);
                        writer.println("    Synonyms:");
                        for (int j = 0; j < op.countSynonyms(); j++) {
                            writer.print(prefix);
                            writer.print("      #");
                            writer.print(j);
                            writer.print(": ");
                            writer.println(op.getSynonymAt(j));
                        }
                    }
                    if (op.getExtras() != null) {
                        writer.print(prefix);
                        writer.print("    mExtras=");
                        writer.println(op.getExtras());
                    }
                }
            }
        }
    }

    /**
     * A request to simply inform the user that the voice operation has completed, as per
     * {@link android.app.VoiceInteractor.CompleteVoiceRequest
     * VoiceInteractor.CompleteVoiceRequest}.
     */
    public static final class CompleteVoiceRequest extends android.service.voice.VoiceInteractionSession.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        CompleteVoiceRequest(java.lang.String packageName, int uid, com.android.internal.app.IVoiceInteractorCallback callback, android.service.voice.VoiceInteractionSession session, android.app.VoiceInteractor.Prompt prompt, android.os.Bundle extras) {
            super(packageName, uid, callback, session, extras);
            mPrompt = prompt;
        }

        /**
         * Return the message informing the user of the completion, as per
         * {@link android.app.VoiceInteractor.CompleteVoiceRequest
         * VoiceInteractor.CompleteVoiceRequest}.
         */
        @android.annotation.Nullable
        public android.app.VoiceInteractor.Prompt getVoicePrompt() {
            return mPrompt;
        }

        /**
         * Return the message informing the user of the completion, as per
         * {@link android.app.VoiceInteractor.CompleteVoiceRequest
         * VoiceInteractor.CompleteVoiceRequest}.
         *
         * @deprecated Prefer {@link #getVoicePrompt()} which allows a separate visual message.
         */
        @android.annotation.Nullable
        public java.lang.CharSequence getMessage() {
            return mPrompt != null ? mPrompt.getVoicePromptAt(0) : null;
        }

        /**
         * Report that the voice interactor has finished completing the voice operation, resulting
         * in a call to
         * {@link android.app.VoiceInteractor.CompleteVoiceRequest#onCompleteResult
         * VoiceInteractor.CompleteVoiceRequest.onCompleteResult}.
         * This finishes the request (it is no longer active).
         */
        public void sendCompleteResult(android.os.Bundle result) {
            try {
                if (android.service.voice.VoiceInteractionSession.DEBUG)
                    android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (("sendCompleteVoiceResult: req=" + mInterface) + " result=") + result);

                finishRequest();
                mCallback.deliverCompleteVoiceResult(mInterface, result);
            } catch (android.os.RemoteException e) {
            }
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
        }
    }

    /**
     * A request to report that the current user interaction can not be completed with voice, as per
     * {@link android.app.VoiceInteractor.AbortVoiceRequest VoiceInteractor.AbortVoiceRequest}.
     */
    public static final class AbortVoiceRequest extends android.service.voice.VoiceInteractionSession.Request {
        final android.app.VoiceInteractor.Prompt mPrompt;

        AbortVoiceRequest(java.lang.String packageName, int uid, com.android.internal.app.IVoiceInteractorCallback callback, android.service.voice.VoiceInteractionSession session, android.app.VoiceInteractor.Prompt prompt, android.os.Bundle extras) {
            super(packageName, uid, callback, session, extras);
            mPrompt = prompt;
        }

        /**
         * Return the message informing the user of the problem, as per
         * {@link android.app.VoiceInteractor.AbortVoiceRequest VoiceInteractor.AbortVoiceRequest}.
         */
        @android.annotation.Nullable
        public android.app.VoiceInteractor.Prompt getVoicePrompt() {
            return mPrompt;
        }

        /**
         * Return the message informing the user of the problem, as per
         * {@link android.app.VoiceInteractor.AbortVoiceRequest VoiceInteractor.AbortVoiceRequest}.
         *
         * @deprecated Prefer {@link #getVoicePrompt()} which allows a separate visual message.
         */
        @android.annotation.Nullable
        public java.lang.CharSequence getMessage() {
            return mPrompt != null ? mPrompt.getVoicePromptAt(0) : null;
        }

        /**
         * Report that the voice interactor has finished aborting the voice operation, resulting
         * in a call to
         * {@link android.app.VoiceInteractor.AbortVoiceRequest#onAbortResult
         * VoiceInteractor.AbortVoiceRequest.onAbortResult}.  This finishes the request (it
         * is no longer active).
         */
        public void sendAbortResult(android.os.Bundle result) {
            try {
                if (android.service.voice.VoiceInteractionSession.DEBUG)
                    android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (("sendConfirmResult: req=" + mInterface) + " result=") + result);

                finishRequest();
                mCallback.deliverAbortVoiceResult(mInterface, result);
            } catch (android.os.RemoteException e) {
            }
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(mPrompt);
        }
    }

    /**
     * A generic vendor-specific request, as per
     * {@link android.app.VoiceInteractor.CommandRequest VoiceInteractor.CommandRequest}.
     */
    public static final class CommandRequest extends android.service.voice.VoiceInteractionSession.Request {
        final java.lang.String mCommand;

        CommandRequest(java.lang.String packageName, int uid, com.android.internal.app.IVoiceInteractorCallback callback, android.service.voice.VoiceInteractionSession session, java.lang.String command, android.os.Bundle extras) {
            super(packageName, uid, callback, session, extras);
            mCommand = command;
        }

        /**
         * Return the command that is being executed, as per
         * {@link android.app.VoiceInteractor.CommandRequest VoiceInteractor.CommandRequest}.
         */
        public java.lang.String getCommand() {
            return mCommand;
        }

        void sendCommandResult(boolean finished, android.os.Bundle result) {
            try {
                if (android.service.voice.VoiceInteractionSession.DEBUG)
                    android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (("sendCommandResult: req=" + mInterface) + " result=") + result);

                if (finished) {
                    finishRequest();
                }
                mCallback.deliverCommandResult(mInterface, finished, result);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Report an intermediate result of the request, without completing it (the request
         * is still active and the app is waiting for the final result), resulting in a call to
         * {@link android.app.VoiceInteractor.CommandRequest#onCommandResult
         * VoiceInteractor.CommandRequest.onCommandResult} with false for isCompleted.
         */
        public void sendIntermediateResult(android.os.Bundle result) {
            sendCommandResult(false, result);
        }

        /**
         * Report the final result of the request, completing the request and resulting in a call to
         * {@link android.app.VoiceInteractor.CommandRequest#onCommandResult
         * VoiceInteractor.CommandRequest.onCommandResult} with true for isCompleted.
         * This finishes the request (it is no longer active).
         */
        public void sendResult(android.os.Bundle result) {
            sendCommandResult(true, result);
        }

        void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mCommand=");
            writer.println(mCommand);
        }
    }

    static final int MSG_START_CONFIRMATION = 1;

    static final int MSG_START_PICK_OPTION = 2;

    static final int MSG_START_COMPLETE_VOICE = 3;

    static final int MSG_START_ABORT_VOICE = 4;

    static final int MSG_START_COMMAND = 5;

    static final int MSG_SUPPORTS_COMMANDS = 6;

    static final int MSG_CANCEL = 7;

    static final int MSG_TASK_STARTED = 100;

    static final int MSG_TASK_FINISHED = 101;

    static final int MSG_CLOSE_SYSTEM_DIALOGS = 102;

    static final int MSG_DESTROY = 103;

    static final int MSG_HANDLE_ASSIST = 104;

    static final int MSG_HANDLE_SCREENSHOT = 105;

    static final int MSG_SHOW = 106;

    static final int MSG_HIDE = 107;

    static final int MSG_ON_LOCKSCREEN_SHOWN = 108;

    class MyCallbacks implements android.inputmethodservice.SoftInputWindow.Callback , com.android.internal.os.HandlerCaller.Callback {
        @java.lang.Override
        public void executeMessage(android.os.Message msg) {
            com.android.internal.os.SomeArgs args = null;
            switch (msg.what) {
                case android.service.voice.VoiceInteractionSession.MSG_START_CONFIRMATION :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onConfirm: req=" + msg.obj);

                    onRequestConfirmation(((android.service.voice.VoiceInteractionSession.ConfirmationRequest) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_START_PICK_OPTION :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onPickOption: req=" + msg.obj);

                    onRequestPickOption(((android.service.voice.VoiceInteractionSession.PickOptionRequest) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_START_COMPLETE_VOICE :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onCompleteVoice: req=" + msg.obj);

                    onRequestCompleteVoice(((android.service.voice.VoiceInteractionSession.CompleteVoiceRequest) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_START_ABORT_VOICE :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onAbortVoice: req=" + msg.obj);

                    onRequestAbortVoice(((android.service.voice.VoiceInteractionSession.AbortVoiceRequest) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_START_COMMAND :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onCommand: req=" + msg.obj);

                    onRequestCommand(((android.service.voice.VoiceInteractionSession.CommandRequest) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_SUPPORTS_COMMANDS :
                    args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onGetSupportedCommands: cmds=" + args.arg1);

                    args.arg1 = onGetSupportedCommands(((java.lang.String[]) (args.arg1)));
                    args.complete();
                    args = null;
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_CANCEL :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onCancel: req=" + ((android.service.voice.VoiceInteractionSession.Request) (msg.obj)));

                    onCancelRequest(((android.service.voice.VoiceInteractionSession.Request) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_TASK_STARTED :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (("onTaskStarted: intent=" + msg.obj) + " taskId=") + msg.arg1);

                    onTaskStarted(((android.content.Intent) (msg.obj)), msg.arg1);
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_TASK_FINISHED :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (("onTaskFinished: intent=" + msg.obj) + " taskId=") + msg.arg1);

                    onTaskFinished(((android.content.Intent) (msg.obj)), msg.arg1);
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_CLOSE_SYSTEM_DIALOGS :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onCloseSystemDialogs");

                    onCloseSystemDialogs();
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_DESTROY :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "doDestroy");

                    doDestroy();
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_HANDLE_ASSIST :
                    args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (((((((("onHandleAssist: data=" + args.arg1) + " structure=") + args.arg2) + " content=") + args.arg3) + " activityIndex=") + args.argi5) + " activityCount=") + args.argi6);

                    if (args.argi5 == 0) {
                        doOnHandleAssist(((android.os.Bundle) (args.arg1)), ((android.app.assist.AssistStructure) (args.arg2)), ((java.lang.Throwable) (args.arg3)), ((android.app.assist.AssistContent) (args.arg4)));
                    } else {
                        doOnHandleAssistSecondary(((android.os.Bundle) (args.arg1)), ((android.app.assist.AssistStructure) (args.arg2)), ((java.lang.Throwable) (args.arg3)), ((android.app.assist.AssistContent) (args.arg4)), args.argi5, args.argi6);
                    }
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_HANDLE_SCREENSHOT :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onHandleScreenshot: " + msg.obj);

                    onHandleScreenshot(((android.graphics.Bitmap) (msg.obj)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_SHOW :
                    args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, (((("doShow: args=" + args.arg1) + " flags=") + msg.arg1) + " showCallback=") + args.arg2);

                    doShow(((android.os.Bundle) (args.arg1)), msg.arg1, ((com.android.internal.app.IVoiceInteractionSessionShowCallback) (args.arg2)));
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_HIDE :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "doHide");

                    doHide();
                    break;
                case android.service.voice.VoiceInteractionSession.MSG_ON_LOCKSCREEN_SHOWN :
                    if (android.service.voice.VoiceInteractionSession.DEBUG)
                        android.util.Log.d(android.service.voice.VoiceInteractionSession.TAG, "onLockscreenShown");

                    onLockscreenShown();
                    break;
            }
            if (args != null) {
                args.recycle();
            }
        }

        @java.lang.Override
        public void onBackPressed() {
            android.service.voice.VoiceInteractionSession.this.onBackPressed();
        }
    }

    final android.service.voice.VoiceInteractionSession.MyCallbacks mCallbacks = new android.service.voice.VoiceInteractionSession.MyCallbacks();

    /**
     * Information about where interesting parts of the input method UI appear.
     */
    public static final class Insets {
        /**
         * This is the part of the UI that is the main content.  It is
         * used to determine the basic space needed, to resize/pan the
         * application behind.  It is assumed that this inset does not
         * change very much, since any change will cause a full resize/pan
         * of the application behind.  This value is relative to the top edge
         * of the input method window.
         */
        public final android.graphics.Rect contentInsets = new android.graphics.Rect();

        /**
         * This is the region of the UI that is touchable.  It is used when
         * {@link #touchableInsets} is set to {@link #TOUCHABLE_INSETS_REGION}.
         * The region should be specified relative to the origin of the window frame.
         */
        public final android.graphics.Region touchableRegion = new android.graphics.Region();

        /**
         * Option for {@link #touchableInsets}: the entire window frame
         * can be touched.
         */
        public static final int TOUCHABLE_INSETS_FRAME = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_FRAME;

        /**
         * Option for {@link #touchableInsets}: the area inside of
         * the content insets can be touched.
         */
        public static final int TOUCHABLE_INSETS_CONTENT = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_CONTENT;

        /**
         * Option for {@link #touchableInsets}: the region specified by
         * {@link #touchableRegion} can be touched.
         */
        public static final int TOUCHABLE_INSETS_REGION = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_REGION;

        /**
         * Determine which area of the window is touchable by the user.  May
         * be one of: {@link #TOUCHABLE_INSETS_FRAME},
         * {@link #TOUCHABLE_INSETS_CONTENT}, or {@link #TOUCHABLE_INSETS_REGION}.
         */
        public int touchableInsets;
    }

    final android.view.ViewTreeObserver.OnComputeInternalInsetsListener mInsetsComputer = new android.view.ViewTreeObserver.OnComputeInternalInsetsListener() {
        public void onComputeInternalInsets(android.view.ViewTreeObserver.InternalInsetsInfo info) {
            onComputeInsets(mTmpInsets);
            info.contentInsets.set(mTmpInsets.contentInsets);
            info.visibleInsets.set(mTmpInsets.contentInsets);
            info.touchableRegion.set(mTmpInsets.touchableRegion);
            info.setTouchableInsets(mTmpInsets.touchableInsets);
        }
    };

    public VoiceInteractionSession(android.content.Context context) {
        this(context, new android.os.Handler());
    }

    public VoiceInteractionSession(android.content.Context context, android.os.Handler handler) {
        mContext = context;
        mHandlerCaller = new com.android.internal.os.HandlerCaller(context, handler.getLooper(), mCallbacks, true);
    }

    public android.content.Context getContext() {
        return mContext;
    }

    void addRequest(android.service.voice.VoiceInteractionSession.Request req) {
        synchronized(this) {
            mActiveRequests.put(req.mInterface.asBinder(), req);
        }
    }

    boolean isRequestActive(android.os.IBinder reqInterface) {
        synchronized(this) {
            return mActiveRequests.containsKey(reqInterface);
        }
    }

    android.service.voice.VoiceInteractionSession.Request removeRequest(android.os.IBinder reqInterface) {
        synchronized(this) {
            return mActiveRequests.remove(reqInterface);
        }
    }

    void doCreate(com.android.internal.app.IVoiceInteractionManagerService service, android.os.IBinder token) {
        mSystemService = service;
        mToken = token;
        onCreate();
    }

    void doShow(android.os.Bundle args, int flags, final com.android.internal.app.IVoiceInteractionSessionShowCallback showCallback) {
        if (android.service.voice.VoiceInteractionSession.DEBUG)
            android.util.Log.v(android.service.voice.VoiceInteractionSession.TAG, (("Showing window: mWindowAdded=" + mWindowAdded) + " mWindowVisible=") + mWindowVisible);

        if (mInShowWindow) {
            android.util.Log.w(android.service.voice.VoiceInteractionSession.TAG, "Re-entrance in to showWindow");
            return;
        }
        try {
            mInShowWindow = true;
            if (!mWindowVisible) {
                if (!mWindowAdded) {
                    mWindowAdded = true;
                    android.view.View v = onCreateContentView();
                    if (v != null) {
                        setContentView(v);
                    }
                }
            }
            onShow(args, flags);
            if (!mWindowVisible) {
                mWindowVisible = true;
                mWindow.show();
            }
            if (showCallback != null) {
                mRootView.invalidate();
                mRootView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                    @java.lang.Override
                    public boolean onPreDraw() {
                        mRootView.getViewTreeObserver().removeOnPreDrawListener(this);
                        try {
                            showCallback.onShown();
                        } catch (android.os.RemoteException e) {
                            android.util.Log.w(android.service.voice.VoiceInteractionSession.TAG, "Error calling onShown", e);
                        }
                        return true;
                    }
                });
            }
        } finally {
            mWindowWasVisible = true;
            mInShowWindow = false;
        }
    }

    void doHide() {
        if (mWindowVisible) {
            mWindow.hide();
            mWindowVisible = false;
            onHide();
        }
    }

    void doDestroy() {
        onDestroy();
        if (mInitialized) {
            mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(mInsetsComputer);
            if (mWindowAdded) {
                mWindow.dismiss();
                mWindowAdded = false;
            }
            mInitialized = false;
        }
    }

    void initViews() {
        mInitialized = true;
        mThemeAttrs = mContext.obtainStyledAttributes(android.R.styleable.VoiceInteractionSession);
        mRootView = mInflater.inflate(com.android.internal.R.layout.voice_interaction_session, null);
        mRootView.setSystemUiVisibility((android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mWindow.setContentView(mRootView);
        mRootView.getViewTreeObserver().addOnComputeInternalInsetsListener(mInsetsComputer);
        mContentFrame = ((android.widget.FrameLayout) (mRootView.findViewById(android.R.id.content)));
    }

    /**
     * Equivalent to {@link VoiceInteractionService#setDisabledShowContext
     * VoiceInteractionService.setDisabledShowContext(int)}.
     */
    public void setDisabledShowContext(int flags) {
        try {
            mSystemService.setDisabledShowContext(flags);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Equivalent to {@link VoiceInteractionService#getDisabledShowContext
     * VoiceInteractionService.getDisabledShowContext}.
     */
    public int getDisabledShowContext() {
        try {
            return mSystemService.getDisabledShowContext();
        } catch (android.os.RemoteException e) {
            return 0;
        }
    }

    /**
     * Return which show context flags have been disabled by the user through the system
     * settings UI, so the session will never get this data.  Returned flags are any combination of
     * {@link VoiceInteractionSession#SHOW_WITH_ASSIST VoiceInteractionSession.SHOW_WITH_ASSIST} and
     * {@link VoiceInteractionSession#SHOW_WITH_SCREENSHOT
     * VoiceInteractionSession.SHOW_WITH_SCREENSHOT}.  Note that this only tells you about
     * global user settings, not about restrictions that may be applied contextual based on
     * the current application the user is in or other transient states.
     */
    public int getUserDisabledShowContext() {
        try {
            return mSystemService.getUserDisabledShowContext();
        } catch (android.os.RemoteException e) {
            return 0;
        }
    }

    /**
     * Show the UI for this session.  This asks the system to go through the process of showing
     * your UI, which will eventually culminate in {@link #onShow}.  This is similar to calling
     * {@link VoiceInteractionService#showSession VoiceInteractionService.showSession}.
     *
     * @param args
     * 		Arbitrary arguments that will be propagated {@link #onShow}.
     * @param flags
     * 		Indicates additional optional behavior that should be performed.  May
     * 		be any combination of
     * 		{@link VoiceInteractionSession#SHOW_WITH_ASSIST VoiceInteractionSession.SHOW_WITH_ASSIST} and
     * 		{@link VoiceInteractionSession#SHOW_WITH_SCREENSHOT
     * 		VoiceInteractionSession.SHOW_WITH_SCREENSHOT}
     * 		to request that the system generate and deliver assist data on the current foreground
     * 		app as part of showing the session UI.
     */
    public void show(android.os.Bundle args, int flags) {
        if (mToken == null) {
            throw new java.lang.IllegalStateException("Can't call before onCreate()");
        }
        try {
            mSystemService.showSessionFromSession(mToken, args, flags);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Hide the session's UI, if currently shown.  Call this when you are done with your
     * user interaction.
     */
    public void hide() {
        if (mToken == null) {
            throw new java.lang.IllegalStateException("Can't call before onCreate()");
        }
        try {
            mSystemService.hideSessionFromSession(mToken);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * You can call this to customize the theme used by your IME's window.
     * This must be set before {@link #onCreate}, so you
     * will typically call it in your constructor with the resource ID
     * of your custom theme.
     */
    public void setTheme(int theme) {
        if (mWindow != null) {
            throw new java.lang.IllegalStateException("Must be called before onCreate()");
        }
        mTheme = theme;
    }

    /**
     * Ask that a new activity be started for voice interaction.  This will create a
     * new dedicated task in the activity manager for this voice interaction session;
     * this means that {@link Intent#FLAG_ACTIVITY_NEW_TASK Intent.FLAG_ACTIVITY_NEW_TASK}
     * will be set for you to make it a new task.
     *
     * <p>The newly started activity will be displayed to the user in a special way, as
     * a layer under the voice interaction UI.</p>
     *
     * <p>As the voice activity runs, it can retrieve a {@link android.app.VoiceInteractor}
     * through which it can perform voice interactions through your session.  These requests
     * for voice interactions will appear as callbacks on {@link #onGetSupportedCommands},
     * {@link #onRequestConfirmation}, {@link #onRequestPickOption},
     * {@link #onRequestCompleteVoice}, {@link #onRequestAbortVoice},
     * or {@link #onRequestCommand}
     *
     * <p>You will receive a call to {@link #onTaskStarted} when the task starts up
     * and {@link #onTaskFinished} when the last activity has finished.
     *
     * @param intent
     * 		The Intent to start this voice interaction.  The given Intent will
     * 		always have {@link Intent#CATEGORY_VOICE Intent.CATEGORY_VOICE} added to it, since
     * 		this is part of a voice interaction.
     */
    public void startVoiceActivity(android.content.Intent intent) {
        if (mToken == null) {
            throw new java.lang.IllegalStateException("Can't call before onCreate()");
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(mContext);
            int res = mSystemService.startVoiceActivity(mToken, intent, intent.resolveType(mContext.getContentResolver()));
            android.app.Instrumentation.checkStartActivityResult(res, intent);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Set whether this session will keep the device awake while it is running a voice
     * activity.  By default, the system holds a wake lock for it while in this state,
     * so that it can work even if the screen is off.  Setting this to false removes that
     * wake lock, allowing the CPU to go to sleep.  This is typically used if the
     * session decides it has been waiting too long for a response from the user and
     * doesn't want to let this continue to drain the battery.
     *
     * <p>Passing false here will release the wake lock, and you can call later with
     * true to re-acquire it.  It will also be automatically re-acquired for you each
     * time you start a new voice activity task -- that is when you call
     * {@link #startVoiceActivity}.</p>
     */
    public void setKeepAwake(boolean keepAwake) {
        if (mToken == null) {
            throw new java.lang.IllegalStateException("Can't call before onCreate()");
        }
        try {
            mSystemService.setKeepAwake(mToken, keepAwake);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Request that all system dialogs (and status bar shade etc) be closed, allowing
     * access to the session's UI.  This will <em>not</em> cause the lock screen to be
     * dismissed.
     */
    public void closeSystemDialogs() {
        if (mToken == null) {
            throw new java.lang.IllegalStateException("Can't call before onCreate()");
        }
        try {
            mSystemService.closeSystemDialogs(mToken);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Convenience for inflating views.
     */
    public android.view.LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    /**
     * Retrieve the window being used to show the session's UI.
     */
    public android.app.Dialog getWindow() {
        return mWindow;
    }

    /**
     * Finish the session.  This completely destroys the session -- the next time it is shown,
     * an entirely new one will be created.  You do not normally call this function; instead,
     * use {@link #hide} and allow the system to destroy your session if it needs its RAM.
     */
    public void finish() {
        if (mToken == null) {
            throw new java.lang.IllegalStateException("Can't call before onCreate()");
        }
        try {
            mSystemService.finish(mToken);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Initiatize a new session.  At this point you don't know exactly what this
     * session will be used for; you will find that out in {@link #onShow}.
     */
    public void onCreate() {
        doOnCreate();
    }

    private void doOnCreate() {
        mTheme = (mTheme != 0) ? mTheme : com.android.internal.R.style.Theme_DeviceDefault_VoiceInteractionSession;
        mInflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mWindow = new android.inputmethodservice.SoftInputWindow(mContext, "VoiceInteractionSession", mTheme, mCallbacks, this, mDispatcherState, android.view.WindowManager.LayoutParams.TYPE_VOICE_INTERACTION, android.view.Gravity.BOTTOM, true);
        mWindow.getWindow().addFlags((android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN) | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        initViews();
        mWindow.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setToken(mToken);
    }

    /**
     * Called when the session UI is going to be shown.  This is called after
     * {@link #onCreateContentView} (if the session's content UI needed to be created) and
     * immediately prior to the window being shown.  This may be called while the window
     * is already shown, if a show request has come in while it is shown, to allow you to
     * update the UI to match the new show arguments.
     *
     * @param args
     * 		The arguments that were supplied to
     * 		{@link VoiceInteractionService#showSession VoiceInteractionService.showSession}.
     * @param showFlags
     * 		The show flags originally provided to
     * 		{@link VoiceInteractionService#showSession VoiceInteractionService.showSession}.
     */
    public void onShow(android.os.Bundle args, int showFlags) {
    }

    /**
     * Called immediately after stopping to show the session UI.
     */
    public void onHide() {
    }

    /**
     * Last callback to the session as it is being finished.
     */
    public void onDestroy() {
    }

    /**
     * Hook in which to create the session's UI.
     */
    public android.view.View onCreateContentView() {
        return null;
    }

    public void setContentView(android.view.View view) {
        mContentFrame.removeAllViews();
        mContentFrame.addView(view, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mContentFrame.requestApplyInsets();
    }

    void doOnHandleAssist(android.os.Bundle data, android.app.assist.AssistStructure structure, java.lang.Throwable failure, android.app.assist.AssistContent content) {
        if (failure != null) {
            onAssistStructureFailure(failure);
        }
        onHandleAssist(data, structure, content);
    }

    void doOnHandleAssistSecondary(android.os.Bundle data, android.app.assist.AssistStructure structure, java.lang.Throwable failure, android.app.assist.AssistContent content, int index, int count) {
        if (failure != null) {
            onAssistStructureFailure(failure);
        }
        onHandleAssistSecondary(data, structure, content, index, count);
    }

    /**
     * Called when there has been a failure transferring the {@link AssistStructure} to
     * the assistant.  This may happen, for example, if the data is too large and results
     * in an out of memory exception, or the client has provided corrupt data.  This will
     * be called immediately before {@link #onHandleAssist} and the AssistStructure supplied
     * there afterwards will be null.
     *
     * @param failure
     * 		The failure exception that was thrown when building the
     * 		{@link AssistStructure}.
     */
    public void onAssistStructureFailure(java.lang.Throwable failure) {
    }

    /**
     * Called to receive data from the application that the user was currently viewing when
     * an assist session is started.  If the original show request did not specify
     * {@link #SHOW_WITH_ASSIST}, this method will not be called.
     *
     * @param data
     * 		Arbitrary data supplied by the app through
     * 		{@link android.app.Activity#onProvideAssistData Activity.onProvideAssistData}.
     * 		May be null if assist data has been disabled by the user or device policy.
     * @param structure
     * 		If available, the structure definition of all windows currently
     * 		displayed by the app.  May be null if assist data has been disabled by the user
     * 		or device policy; will be an empty stub if the application has disabled assist
     * 		by marking its window as secure.
     * @param content
     * 		Additional content data supplied by the app through
     * 		{@link android.app.Activity#onProvideAssistContent Activity.onProvideAssistContent}.
     * 		May be null if assist data has been disabled by the user or device policy; will
     * 		not be automatically filled in with data from the app if the app has marked its
     * 		window as secure.
     */
    public void onHandleAssist(@android.annotation.Nullable
    android.os.Bundle data, @android.annotation.Nullable
    android.app.assist.AssistStructure structure, @android.annotation.Nullable
    android.app.assist.AssistContent content) {
    }

    /**
     * Called to receive data from other applications that the user was or is interacting with,
     * that are currently on the screen in a multi-window display environment, not including the
     * currently focused activity. This could be
     * a free-form window, a picture-in-picture window, or another window in a split-screen display.
     * <p>
     * This method is very similar to
     * {@link #onHandleAssist} except that it is called
     * for additional non-focused activities along with an index and count that indicates
     * which additional activity the data is for. {@code index} will be between 1 and
     * {@code count}-1 and this method is called once for each additional window, in no particular
     * order. The {@code count} indicates how many windows to expect assist data for, including the
     * top focused activity, which continues to be returned via {@link #onHandleAssist}.
     * <p>
     * To be responsive to assist requests, process assist data as soon as it is received,
     * without waiting for all queued activities to return assist data.
     *
     * @param data
     * 		Arbitrary data supplied by the app through
     * 		{@link android.app.Activity#onProvideAssistData Activity.onProvideAssistData}.
     * 		May be null if assist data has been disabled by the user or device policy.
     * @param structure
     * 		If available, the structure definition of all windows currently
     * 		displayed by the app.  May be null if assist data has been disabled by the user
     * 		or device policy; will be an empty stub if the application has disabled assist
     * 		by marking its window as secure.
     * @param content
     * 		Additional content data supplied by the app through
     * 		{@link android.app.Activity#onProvideAssistContent Activity.onProvideAssistContent}.
     * 		May be null if assist data has been disabled by the user or device policy; will
     * 		not be automatically filled in with data from the app if the app has marked its
     * 		window as secure.
     * @param index
     * 		the index of the additional activity that this data
     * 		is for.
     * @param count
     * 		the total number of additional activities for which the assist data is being
     * 		returned, including the focused activity that is returned via
     * 		{@link #onHandleAssist}.
     */
    public void onHandleAssistSecondary(@android.annotation.Nullable
    android.os.Bundle data, @android.annotation.Nullable
    android.app.assist.AssistStructure structure, @android.annotation.Nullable
    android.app.assist.AssistContent content, int index, int count) {
    }

    /**
     * Called to receive a screenshot of what the user was currently viewing when an assist
     * session is started.  May be null if screenshots are disabled by the user, policy,
     * or application.  If the original show request did not specify
     * {@link #SHOW_WITH_SCREENSHOT}, this method will not be called.
     */
    public void onHandleScreenshot(@android.annotation.Nullable
    android.graphics.Bitmap screenshot) {
    }

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        return false;
    }

    public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int count, android.view.KeyEvent event) {
        return false;
    }

    /**
     * Called when the user presses the back button while focus is in the session UI.  Note
     * that this will only happen if the session UI has requested input focus in its window;
     * otherwise, the back key will go to whatever window has focus and do whatever behavior
     * it normally has there.  The default implementation simply calls {@link #hide}.
     */
    public void onBackPressed() {
        hide();
    }

    /**
     * Sessions automatically watch for requests that all system UI be closed (such as when
     * the user presses HOME), which will appear here.  The default implementation always
     * calls {@link #hide}.
     */
    public void onCloseSystemDialogs() {
        hide();
    }

    /**
     * Called when the lockscreen was shown.
     */
    public void onLockscreenShown() {
        hide();
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    }

    @java.lang.Override
    public void onLowMemory() {
    }

    @java.lang.Override
    public void onTrimMemory(int level) {
    }

    /**
     * Compute the interesting insets into your UI.  The default implementation
     * sets {@link Insets#contentInsets outInsets.contentInsets.top} to the height
     * of the window, meaning it should not adjust content underneath.  The default touchable
     * insets are {@link Insets#TOUCHABLE_INSETS_FRAME}, meaning it consumes all touch
     * events within its window frame.
     *
     * @param outInsets
     * 		Fill in with the current UI insets.
     */
    public void onComputeInsets(android.service.voice.VoiceInteractionSession.Insets outInsets) {
        outInsets.contentInsets.left = 0;
        outInsets.contentInsets.bottom = 0;
        outInsets.contentInsets.right = 0;
        android.view.View decor = getWindow().getWindow().getDecorView();
        outInsets.contentInsets.top = decor.getHeight();
        outInsets.touchableInsets = android.service.voice.VoiceInteractionSession.Insets.TOUCHABLE_INSETS_FRAME;
        outInsets.touchableRegion.setEmpty();
    }

    /**
     * Called when a task initiated by {@link #startVoiceActivity(android.content.Intent)}
     * has actually started.
     *
     * @param intent
     * 		The original {@link Intent} supplied to
     * 		{@link #startVoiceActivity(android.content.Intent)}.
     * @param taskId
     * 		Unique ID of the now running task.
     */
    public void onTaskStarted(android.content.Intent intent, int taskId) {
    }

    /**
     * Called when the last activity of a task initiated by
     * {@link #startVoiceActivity(android.content.Intent)} has finished.  The default
     * implementation calls {@link #finish()} on the assumption that this represents
     * the completion of a voice action.  You can override the implementation if you would
     * like a different behavior.
     *
     * @param intent
     * 		The original {@link Intent} supplied to
     * 		{@link #startVoiceActivity(android.content.Intent)}.
     * @param taskId
     * 		Unique ID of the finished task.
     */
    public void onTaskFinished(android.content.Intent intent, int taskId) {
        hide();
    }

    /**
     * Request to query for what extended commands the session supports.
     *
     * @param commands
     * 		An array of commands that are being queried.
     * @return Return an array of booleans indicating which of each entry in the
    command array is supported.  A true entry in the array indicates the command
    is supported; false indicates it is not.  The default implementation returns
    an array of all false entries.
     */
    public boolean[] onGetSupportedCommands(java.lang.String[] commands) {
        return new boolean[commands.length];
    }

    /**
     * Request to confirm with the user before proceeding with an unrecoverable operation,
     * corresponding to a {@link android.app.VoiceInteractor.ConfirmationRequest
     * VoiceInteractor.ConfirmationRequest}.
     *
     * @param request
     * 		The active request.
     */
    public void onRequestConfirmation(android.service.voice.VoiceInteractionSession.ConfirmationRequest request) {
    }

    /**
     * Request for the user to pick one of N options, corresponding to a
     * {@link android.app.VoiceInteractor.PickOptionRequest VoiceInteractor.PickOptionRequest}.
     *
     * @param request
     * 		The active request.
     */
    public void onRequestPickOption(android.service.voice.VoiceInteractionSession.PickOptionRequest request) {
    }

    /**
     * Request to complete the voice interaction session because the voice activity successfully
     * completed its interaction using voice.  Corresponds to
     * {@link android.app.VoiceInteractor.CompleteVoiceRequest
     * VoiceInteractor.CompleteVoiceRequest}.  The default implementation just sends an empty
     * confirmation back to allow the activity to exit.
     *
     * @param request
     * 		The active request.
     */
    public void onRequestCompleteVoice(android.service.voice.VoiceInteractionSession.CompleteVoiceRequest request) {
    }

    /**
     * Request to abort the voice interaction session because the voice activity can not
     * complete its interaction using voice.  Corresponds to
     * {@link android.app.VoiceInteractor.AbortVoiceRequest
     * VoiceInteractor.AbortVoiceRequest}.  The default implementation just sends an empty
     * confirmation back to allow the activity to exit.
     *
     * @param request
     * 		The active request.
     */
    public void onRequestAbortVoice(android.service.voice.VoiceInteractionSession.AbortVoiceRequest request) {
    }

    /**
     * Process an arbitrary extended command from the caller,
     * corresponding to a {@link android.app.VoiceInteractor.CommandRequest
     * VoiceInteractor.CommandRequest}.
     *
     * @param request
     * 		The active request.
     */
    public void onRequestCommand(android.service.voice.VoiceInteractionSession.CommandRequest request) {
    }

    /**
     * Called when the {@link android.app.VoiceInteractor} has asked to cancel a {@link Request}
     * that was previously delivered to {@link #onRequestConfirmation},
     * {@link #onRequestPickOption}, {@link #onRequestCompleteVoice}, {@link #onRequestAbortVoice},
     * or {@link #onRequestCommand}.
     *
     * @param request
     * 		The request that is being canceled.
     */
    public void onCancelRequest(android.service.voice.VoiceInteractionSession.Request request) {
    }

    /**
     * Print the Service's state into the given stream.  This gets invoked by
     * {@link VoiceInteractionSessionService} when its Service
     * {@link android.app.Service#dump} method is called.
     *
     * @param prefix
     * 		Text to print at the front of each line.
     * @param fd
     * 		The raw file descriptor that the dump is being sent to.
     * @param writer
     * 		The PrintWriter to which you should dump your state.  This will be
     * 		closed for you after you return.
     * @param args
     * 		additional arguments to the dump request.
     */
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        writer.print(prefix);
        writer.print("mToken=");
        writer.println(mToken);
        writer.print(prefix);
        writer.print("mTheme=#");
        writer.println(java.lang.Integer.toHexString(mTheme));
        writer.print(prefix);
        writer.print("mInitialized=");
        writer.println(mInitialized);
        writer.print(prefix);
        writer.print("mWindowAdded=");
        writer.print(mWindowAdded);
        writer.print(" mWindowVisible=");
        writer.println(mWindowVisible);
        writer.print(prefix);
        writer.print("mWindowWasVisible=");
        writer.print(mWindowWasVisible);
        writer.print(" mInShowWindow=");
        writer.println(mInShowWindow);
        if (mActiveRequests.size() > 0) {
            writer.print(prefix);
            writer.println("Active requests:");
            java.lang.String innerPrefix = prefix + "    ";
            for (int i = 0; i < mActiveRequests.size(); i++) {
                android.service.voice.VoiceInteractionSession.Request req = mActiveRequests.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(req);
                req.dump(innerPrefix, fd, writer, args);
            }
        }
    }
}

