/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.view.textservice;


/**
 * The SpellCheckerSession interface provides the per client functionality of SpellCheckerService.
 *
 *
 * <a name="Applications"></a>
 * <h3>Applications</h3>
 *
 * <p>In most cases, applications that are using the standard
 * {@link android.widget.TextView} or its subclasses will have little they need
 * to do to work well with spell checker services.  The main things you need to
 * be aware of are:</p>
 *
 * <ul>
 * <li> Properly set the {@link android.R.attr#inputType} in your editable
 * text views, so that the spell checker will have enough context to help the
 * user in editing text in them.
 * </ul>
 *
 * <p>For the rare people amongst us writing client applications that use the spell checker service
 * directly, you will need to use {@link #getSuggestions(TextInfo, int)} or
 * {@link #getSuggestions(TextInfo[], int, boolean)} for obtaining results from the spell checker
 * service by yourself.</p>
 *
 * <h3>Security</h3>
 *
 * <p>There are a lot of security issues associated with spell checkers,
 * since they could monitor all the text being sent to them
 * through, for instance, {@link android.widget.TextView}.
 * The Android spell checker framework also allows
 * arbitrary third party spell checkers, so care must be taken to restrict their
 * selection and interactions.</p>
 *
 * <p>Here are some key points about the security architecture behind the
 * spell checker framework:</p>
 *
 * <ul>
 * <li>Only the system is allowed to directly access a spell checker framework's
 * {@link android.service.textservice.SpellCheckerService} interface, via the
 * {@link android.Manifest.permission#BIND_TEXT_SERVICE} permission.  This is
 * enforced in the system by not binding to a spell checker service that does
 * not require this permission.
 *
 * <li>The user must explicitly enable a new spell checker in settings before
 * they can be enabled, to confirm with the system that they know about it
 * and want to make it available for use.
 * </ul>
 */
public class SpellCheckerSession {
    private static final java.lang.String TAG = android.view.textservice.SpellCheckerSession.class.getSimpleName();

    private static final boolean DBG = false;

    /**
     * Name under which a SpellChecker service component publishes information about itself.
     * This meta-data must reference an XML resource.
     */
    public static final java.lang.String SERVICE_META_DATA = "android.view.textservice.scs";

    private static final int MSG_ON_GET_SUGGESTION_MULTIPLE = 1;

    private static final int MSG_ON_GET_SUGGESTION_MULTIPLE_FOR_SENTENCE = 2;

    private final android.view.textservice.SpellCheckerSession.InternalListener mInternalListener;

    private final android.view.textservice.TextServicesManager mTextServicesManager;

    private final android.view.textservice.SpellCheckerInfo mSpellCheckerInfo;

    @android.annotation.UnsupportedAppUsage
    private final android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener mSpellCheckerSessionListener;

    private final android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl mSpellCheckerSessionListenerImpl;

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    /**
     * Handler that will execute the main tasks
     */
    private final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.view.textservice.SpellCheckerSession.MSG_ON_GET_SUGGESTION_MULTIPLE :
                    handleOnGetSuggestionsMultiple(((android.view.textservice.SuggestionsInfo[]) (msg.obj)));
                    break;
                case android.view.textservice.SpellCheckerSession.MSG_ON_GET_SUGGESTION_MULTIPLE_FOR_SENTENCE :
                    handleOnGetSentenceSuggestionsMultiple(((android.view.textservice.SentenceSuggestionsInfo[]) (msg.obj)));
                    break;
            }
        }
    };

    /**
     * Constructor
     *
     * @unknown 
     */
    public SpellCheckerSession(android.view.textservice.SpellCheckerInfo info, android.view.textservice.TextServicesManager tsm, android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener listener) {
        if (((info == null) || (listener == null)) || (tsm == null)) {
            throw new java.lang.NullPointerException();
        }
        mSpellCheckerInfo = info;
        mSpellCheckerSessionListenerImpl = new android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl(mHandler);
        mInternalListener = new android.view.textservice.SpellCheckerSession.InternalListener(mSpellCheckerSessionListenerImpl);
        mTextServicesManager = tsm;
        mSpellCheckerSessionListener = listener;
        mGuard.open("finishSession");
    }

    /**
     *
     *
     * @return true if the connection to a text service of this session is disconnected and not
    alive.
     */
    public boolean isSessionDisconnected() {
        return mSpellCheckerSessionListenerImpl.isDisconnected();
    }

    /**
     * Get the spell checker service info this spell checker session has.
     *
     * @return SpellCheckerInfo for the specified locale.
     */
    public android.view.textservice.SpellCheckerInfo getSpellChecker() {
        return mSpellCheckerInfo;
    }

    /**
     * Cancel pending and running spell check tasks
     */
    public void cancel() {
        mSpellCheckerSessionListenerImpl.cancel();
    }

    /**
     * Finish this session and allow TextServicesManagerService to disconnect the bound spell
     * checker.
     */
    public void close() {
        mGuard.close();
        mSpellCheckerSessionListenerImpl.close();
        mTextServicesManager.finishSpellCheckerService(mSpellCheckerSessionListenerImpl);
    }

    /**
     * Get suggestions from the specified sentences
     *
     * @param textInfos
     * 		an array of text metadata for a spell checker
     * @param suggestionsLimit
     * 		the maximum number of suggestions that will be returned
     */
    public void getSentenceSuggestions(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit) {
        mSpellCheckerSessionListenerImpl.getSentenceSuggestionsMultiple(textInfos, suggestionsLimit);
    }

    /**
     * Get candidate strings for a substring of the specified text.
     *
     * @param textInfo
     * 		text metadata for a spell checker
     * @param suggestionsLimit
     * 		the maximum number of suggestions that will be returned
     * @deprecated use {@link SpellCheckerSession#getSentenceSuggestions(TextInfo[], int)} instead
     */
    @java.lang.Deprecated
    public void getSuggestions(android.view.textservice.TextInfo textInfo, int suggestionsLimit) {
        getSuggestions(new android.view.textservice.TextInfo[]{ textInfo }, suggestionsLimit, false);
    }

    /**
     * A batch process of getSuggestions
     *
     * @param textInfos
     * 		an array of text metadata for a spell checker
     * @param suggestionsLimit
     * 		the maximum number of suggestions that will be returned
     * @param sequentialWords
     * 		true if textInfos can be treated as sequential words.
     * @deprecated use {@link SpellCheckerSession#getSentenceSuggestions(TextInfo[], int)} instead
     */
    @java.lang.Deprecated
    public void getSuggestions(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
        if (android.view.textservice.SpellCheckerSession.DBG) {
            android.util.Log.w(android.view.textservice.SpellCheckerSession.TAG, "getSuggestions from " + mSpellCheckerInfo.getId());
        }
        mSpellCheckerSessionListenerImpl.getSuggestionsMultiple(textInfos, suggestionsLimit, sequentialWords);
    }

    private void handleOnGetSuggestionsMultiple(android.view.textservice.SuggestionsInfo[] suggestionInfos) {
        mSpellCheckerSessionListener.onGetSuggestions(suggestionInfos);
    }

    private void handleOnGetSentenceSuggestionsMultiple(android.view.textservice.SentenceSuggestionsInfo[] suggestionInfos) {
        mSpellCheckerSessionListener.onGetSentenceSuggestions(suggestionInfos);
    }

    private static final class SpellCheckerSessionListenerImpl extends com.android.internal.textservice.ISpellCheckerSessionListener.Stub {
        private static final int TASK_CANCEL = 1;

        private static final int TASK_GET_SUGGESTIONS_MULTIPLE = 2;

        private static final int TASK_CLOSE = 3;

        private static final int TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE = 4;

        private static java.lang.String taskToString(int task) {
            switch (task) {
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CANCEL :
                    return "TASK_CANCEL";
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_GET_SUGGESTIONS_MULTIPLE :
                    return "TASK_GET_SUGGESTIONS_MULTIPLE";
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE :
                    return "TASK_CLOSE";
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE :
                    return "TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE";
                default :
                    return "Unexpected task=" + task;
            }
        }

        private final java.util.Queue<android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams> mPendingTasks = new java.util.LinkedList<>();

        private android.os.Handler mHandler;

        private static final int STATE_WAIT_CONNECTION = 0;

        private static final int STATE_CONNECTED = 1;

        private static final int STATE_CLOSED_AFTER_CONNECTION = 2;

        private static final int STATE_CLOSED_BEFORE_CONNECTION = 3;

        private static java.lang.String stateToString(int state) {
            switch (state) {
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_WAIT_CONNECTION :
                    return "STATE_WAIT_CONNECTION";
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CONNECTED :
                    return "STATE_CONNECTED";
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_AFTER_CONNECTION :
                    return "STATE_CLOSED_AFTER_CONNECTION";
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_BEFORE_CONNECTION :
                    return "STATE_CLOSED_BEFORE_CONNECTION";
                default :
                    return "Unexpected state=" + state;
            }
        }

        private int mState = android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_WAIT_CONNECTION;

        private com.android.internal.textservice.ISpellCheckerSession mISpellCheckerSession;

        private android.os.HandlerThread mThread;

        private android.os.Handler mAsyncHandler;

        public SpellCheckerSessionListenerImpl(android.os.Handler handler) {
            mHandler = handler;
        }

        private static class SpellCheckerParams {
            public final int mWhat;

            public final android.view.textservice.TextInfo[] mTextInfos;

            public final int mSuggestionsLimit;

            public final boolean mSequentialWords;

            public com.android.internal.textservice.ISpellCheckerSession mSession;

            public SpellCheckerParams(int what, android.view.textservice.TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
                mWhat = what;
                mTextInfos = textInfos;
                mSuggestionsLimit = suggestionsLimit;
                mSequentialWords = sequentialWords;
            }
        }

        private void processTask(com.android.internal.textservice.ISpellCheckerSession session, android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams scp, boolean async) {
            if (android.view.textservice.SpellCheckerSession.DBG) {
                synchronized(this) {
                    android.util.Log.d(android.view.textservice.SpellCheckerSession.TAG, ((((((((("entering processTask:" + " session.hashCode()=#") + java.lang.Integer.toHexString(session.hashCode())) + " scp.mWhat=") + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.taskToString(scp.mWhat)) + " async=") + async) + " mAsyncHandler=") + mAsyncHandler) + " mState=") + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.stateToString(mState));
                }
            }
            if (async || (mAsyncHandler == null)) {
                switch (scp.mWhat) {
                    case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CANCEL :
                        try {
                            session.onCancel();
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "Failed to cancel " + e);
                        }
                        break;
                    case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_GET_SUGGESTIONS_MULTIPLE :
                        try {
                            session.onGetSuggestionsMultiple(scp.mTextInfos, scp.mSuggestionsLimit, scp.mSequentialWords);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "Failed to get suggestions " + e);
                        }
                        break;
                    case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE :
                        try {
                            session.onGetSentenceSuggestionsMultiple(scp.mTextInfos, scp.mSuggestionsLimit);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "Failed to get suggestions " + e);
                        }
                        break;
                    case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE :
                        try {
                            session.onClose();
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "Failed to close " + e);
                        }
                        break;
                }
            } else {
                // The interface is to a local object, so need to execute it
                // asynchronously.
                scp.mSession = session;
                mAsyncHandler.sendMessage(android.os.Message.obtain(mAsyncHandler, 1, scp));
            }
            if (scp.mWhat == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE) {
                // If we are closing, we want to clean up our state now even
                // if it is pending as an async operation.
                synchronized(this) {
                    processCloseLocked();
                }
            }
        }

        private void processCloseLocked() {
            if (android.view.textservice.SpellCheckerSession.DBG)
                android.util.Log.d(android.view.textservice.SpellCheckerSession.TAG, ((("entering processCloseLocked:" + " session") + (mISpellCheckerSession != null ? ".hashCode()=#" + java.lang.Integer.toHexString(mISpellCheckerSession.hashCode()) : "=null")) + " mState=") + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.stateToString(mState));

            mISpellCheckerSession = null;
            if (mThread != null) {
                mThread.quit();
            }
            mHandler = null;
            mPendingTasks.clear();
            mThread = null;
            mAsyncHandler = null;
            switch (mState) {
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_WAIT_CONNECTION :
                    mState = android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_BEFORE_CONNECTION;
                    break;
                case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CONNECTED :
                    mState = android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_AFTER_CONNECTION;
                    break;
                default :
                    android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "processCloseLocked is called unexpectedly. mState=" + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.stateToString(mState));
                    break;
            }
        }

        public void onServiceConnected(com.android.internal.textservice.ISpellCheckerSession session) {
            synchronized(this) {
                switch (mState) {
                    case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_WAIT_CONNECTION :
                        // OK, go ahead.
                        break;
                    case android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_BEFORE_CONNECTION :
                        // This is possible, and not an error.  The client no longer is interested
                        // in this connection. OK to ignore.
                        if (android.view.textservice.SpellCheckerSession.DBG)
                            android.util.Log.i(android.view.textservice.SpellCheckerSession.TAG, "ignoring onServiceConnected since the session is" + " already closed.");

                        return;
                    default :
                        android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "ignoring onServiceConnected due to unexpected mState=" + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.stateToString(mState));
                        return;
                }
                if (session == null) {
                    android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, "ignoring onServiceConnected due to session=null");
                    return;
                }
                mISpellCheckerSession = session;
                if ((session.asBinder() instanceof android.os.Binder) && (mThread == null)) {
                    if (android.view.textservice.SpellCheckerSession.DBG)
                        android.util.Log.d(android.view.textservice.SpellCheckerSession.TAG, "starting HandlerThread in onServiceConnected.");

                    // If this is a local object, we need to do our own threading
                    // to make sure we handle it asynchronously.
                    mThread = new android.os.HandlerThread("SpellCheckerSession", THREAD_PRIORITY_BACKGROUND);
                    mThread.start();
                    mAsyncHandler = new android.os.Handler(mThread.getLooper()) {
                        @java.lang.Override
                        public void handleMessage(android.os.Message msg) {
                            android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams scp = ((android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams) (msg.obj));
                            processTask(scp.mSession, scp, true);
                        }
                    };
                }
                mState = android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CONNECTED;
                if (android.view.textservice.SpellCheckerSession.DBG) {
                    android.util.Log.d(android.view.textservice.SpellCheckerSession.TAG, (("processed onServiceConnected: mISpellCheckerSession.hashCode()=#" + java.lang.Integer.toHexString(mISpellCheckerSession.hashCode())) + " mPendingTasks.size()=") + mPendingTasks.size());
                }
                while (!mPendingTasks.isEmpty()) {
                    processTask(session, mPendingTasks.poll(), false);
                } 
            }
        }

        public void cancel() {
            processOrEnqueueTask(new android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CANCEL, null, 0, false));
        }

        public void getSuggestionsMultiple(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            processOrEnqueueTask(new android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_GET_SUGGESTIONS_MULTIPLE, textInfos, suggestionsLimit, sequentialWords));
        }

        public void getSentenceSuggestionsMultiple(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit) {
            processOrEnqueueTask(new android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE, textInfos, suggestionsLimit, false));
        }

        public void close() {
            processOrEnqueueTask(new android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE, null, 0, false));
        }

        public boolean isDisconnected() {
            synchronized(this) {
                return mState != android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CONNECTED;
            }
        }

        private void processOrEnqueueTask(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams scp) {
            com.android.internal.textservice.ISpellCheckerSession session;
            synchronized(this) {
                if ((scp.mWhat == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE) && ((mState == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_AFTER_CONNECTION) || (mState == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CLOSED_BEFORE_CONNECTION))) {
                    // It is OK to call SpellCheckerSession#close() multiple times.
                    // Don't output confusing/misleading warning messages.
                    return;
                }
                if ((mState != android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_WAIT_CONNECTION) && (mState != android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_CONNECTED)) {
                    android.util.Log.e(android.view.textservice.SpellCheckerSession.TAG, (("ignoring processOrEnqueueTask due to unexpected mState=" + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.stateToString(mState)) + " scp.mWhat=") + android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.taskToString(scp.mWhat));
                    return;
                }
                if (mState == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.STATE_WAIT_CONNECTION) {
                    // If we are still waiting for the connection. Need to pay special attention.
                    if (scp.mWhat == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE) {
                        processCloseLocked();
                        return;
                    }
                    // Enqueue the task to task queue.
                    android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams closeTask = null;
                    if (scp.mWhat == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CANCEL) {
                        if (android.view.textservice.SpellCheckerSession.DBG)
                            android.util.Log.d(android.view.textservice.SpellCheckerSession.TAG, "canceling pending tasks in processOrEnqueueTask.");

                        while (!mPendingTasks.isEmpty()) {
                            final android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams tmp = mPendingTasks.poll();
                            if (tmp.mWhat == android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.TASK_CLOSE) {
                                // Only one close task should be processed, while we need to remove
                                // all close tasks from the queue
                                closeTask = tmp;
                            }
                        } 
                    }
                    mPendingTasks.offer(scp);
                    if (closeTask != null) {
                        mPendingTasks.offer(closeTask);
                    }
                    if (android.view.textservice.SpellCheckerSession.DBG)
                        android.util.Log.d(android.view.textservice.SpellCheckerSession.TAG, ("queueing tasks in processOrEnqueueTask since the" + (" connection is not established." + " mPendingTasks.size()=")) + mPendingTasks.size());

                    return;
                }
                session = mISpellCheckerSession;
            }
            // session must never be null here.
            processTask(session, scp, false);
        }

        @java.lang.Override
        public void onGetSuggestions(android.view.textservice.SuggestionsInfo[] results) {
            synchronized(this) {
                if (mHandler != null) {
                    mHandler.sendMessage(android.os.Message.obtain(mHandler, android.view.textservice.SpellCheckerSession.MSG_ON_GET_SUGGESTION_MULTIPLE, results));
                }
            }
        }

        @java.lang.Override
        public void onGetSentenceSuggestions(android.view.textservice.SentenceSuggestionsInfo[] results) {
            synchronized(this) {
                if (mHandler != null) {
                    mHandler.sendMessage(android.os.Message.obtain(mHandler, android.view.textservice.SpellCheckerSession.MSG_ON_GET_SUGGESTION_MULTIPLE_FOR_SENTENCE, results));
                }
            }
        }
    }

    /**
     * Callback for getting results from text services
     */
    public interface SpellCheckerSessionListener {
        /**
         * Callback for {@link SpellCheckerSession#getSuggestions(TextInfo, int)}
         * and {@link SpellCheckerSession#getSuggestions(TextInfo[], int, boolean)}
         *
         * @param results
         * 		an array of {@link SuggestionsInfo}s.
         * 		These results are suggestions for {@link TextInfo}s queried by
         * 		{@link SpellCheckerSession#getSuggestions(TextInfo, int)} or
         * 		{@link SpellCheckerSession#getSuggestions(TextInfo[], int, boolean)}
         */
        public void onGetSuggestions(android.view.textservice.SuggestionsInfo[] results);

        /**
         * Callback for {@link SpellCheckerSession#getSentenceSuggestions(TextInfo[], int)}
         *
         * @param results
         * 		an array of {@link SentenceSuggestionsInfo}s.
         * 		These results are suggestions for {@link TextInfo}s
         * 		queried by {@link SpellCheckerSession#getSentenceSuggestions(TextInfo[], int)}.
         */
        public void onGetSentenceSuggestions(android.view.textservice.SentenceSuggestionsInfo[] results);
    }

    private static final class InternalListener extends com.android.internal.textservice.ITextServicesSessionListener.Stub {
        private final android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl mParentSpellCheckerSessionListenerImpl;

        public InternalListener(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl spellCheckerSessionListenerImpl) {
            mParentSpellCheckerSessionListenerImpl = spellCheckerSessionListenerImpl;
        }

        @java.lang.Override
        public void onServiceConnected(com.android.internal.textservice.ISpellCheckerSession session) {
            mParentSpellCheckerSessionListenerImpl.onServiceConnected(session);
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            // Note that mGuard will be null if the constructor threw.
            if (mGuard != null) {
                mGuard.warnIfOpen();
                close();
            }
        } finally {
            super.finalize();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public com.android.internal.textservice.ITextServicesSessionListener getTextServicesSessionListener() {
        return mInternalListener;
    }

    /**
     *
     *
     * @unknown 
     */
    public com.android.internal.textservice.ISpellCheckerSessionListener getSpellCheckerSessionListener() {
        return mSpellCheckerSessionListenerImpl;
    }
}

