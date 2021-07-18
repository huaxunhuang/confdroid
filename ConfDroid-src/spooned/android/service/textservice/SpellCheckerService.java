/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.service.textservice;


/**
 * SpellCheckerService provides an abstract base class for a spell checker.
 * This class combines a service to the system with the spell checker service interface that
 * spell checker must implement.
 *
 * <p>In addition to the normal Service lifecycle methods, this class
 * introduces a new specific callback that subclasses should override
 * {@link #createSession()} to provide a spell checker session that is corresponding
 * to requested language and so on. The spell checker session returned by this method
 * should extend {@link SpellCheckerService.Session}.
 * </p>
 *
 * <h3>Returning spell check results</h3>
 *
 * <p>{@link SpellCheckerService.Session#onGetSuggestions(TextInfo, int)}
 * should return spell check results.
 * It receives {@link android.view.textservice.TextInfo} and returns
 * {@link android.view.textservice.SuggestionsInfo} for the input.
 * You may want to override
 * {@link SpellCheckerService.Session#onGetSuggestionsMultiple(TextInfo[], int, boolean)} for
 * better performance and quality.
 * </p>
 *
 * <p>Please note that {@link SpellCheckerService.Session#getLocale()} does not return a valid
 * locale before {@link SpellCheckerService.Session#onCreate()} </p>
 */
public abstract class SpellCheckerService extends android.app.Service {
    private static final java.lang.String TAG = android.service.textservice.SpellCheckerService.class.getSimpleName();

    private static final boolean DBG = false;

    public static final java.lang.String SERVICE_INTERFACE = "android.service.textservice.SpellCheckerService";

    private final android.service.textservice.SpellCheckerService.SpellCheckerServiceBinder mBinder = new android.service.textservice.SpellCheckerService.SpellCheckerServiceBinder(this);

    /**
     * Implement to return the implementation of the internal spell checker
     * service interface. Subclasses should not override.
     */
    @java.lang.Override
    public final android.os.IBinder onBind(final android.content.Intent intent) {
        if (android.service.textservice.SpellCheckerService.DBG) {
            android.util.Log.w(android.service.textservice.SpellCheckerService.TAG, "onBind");
        }
        return mBinder;
    }

    /**
     * Factory method to create a spell checker session impl
     *
     * @return SpellCheckerSessionImpl which should be overridden by a concrete implementation.
     */
    public abstract android.service.textservice.SpellCheckerService.Session createSession();

    /**
     * This abstract class should be overridden by a concrete implementation of a spell checker.
     */
    public static abstract class Session {
        private android.service.textservice.SpellCheckerService.InternalISpellCheckerSession mInternalSession;

        private volatile android.service.textservice.SpellCheckerService.SentenceLevelAdapter mSentenceLevelAdapter;

        /**
         *
         *
         * @unknown 
         */
        public final void setInternalISpellCheckerSession(android.service.textservice.SpellCheckerService.InternalISpellCheckerSession session) {
            mInternalSession = session;
        }

        /**
         * This is called after the class is initialized, at which point it knows it can call
         * getLocale() etc...
         */
        public abstract void onCreate();

        /**
         * Get suggestions for specified text in TextInfo.
         * This function will run on the incoming IPC thread.
         * So, this is not called on the main thread,
         * but will be called in series on another thread.
         *
         * @param textInfo
         * 		the text metadata
         * @param suggestionsLimit
         * 		the maximum number of suggestions to be returned
         * @return SuggestionsInfo which contains suggestions for textInfo
         */
        public abstract android.view.textservice.SuggestionsInfo onGetSuggestions(android.view.textservice.TextInfo textInfo, int suggestionsLimit);

        /**
         * A batch process of onGetSuggestions.
         * This function will run on the incoming IPC thread.
         * So, this is not called on the main thread,
         * but will be called in series on another thread.
         *
         * @param textInfos
         * 		an array of the text metadata
         * @param suggestionsLimit
         * 		the maximum number of suggestions to be returned
         * @param sequentialWords
         * 		true if textInfos can be treated as sequential words.
         * @return an array of {@link SentenceSuggestionsInfo} returned by
        {@link SpellCheckerService.Session#onGetSuggestions(TextInfo, int)}
         */
        public android.view.textservice.SuggestionsInfo[] onGetSuggestionsMultiple(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            final int length = textInfos.length;
            final android.view.textservice.SuggestionsInfo[] retval = new android.view.textservice.SuggestionsInfo[length];
            for (int i = 0; i < length; ++i) {
                retval[i] = onGetSuggestions(textInfos[i], suggestionsLimit);
                retval[i].setCookieAndSequence(textInfos[i].getCookie(), textInfos[i].getSequence());
            }
            return retval;
        }

        /**
         * Get sentence suggestions for specified texts in an array of TextInfo.
         * The default implementation splits the input text to words and returns
         * {@link SentenceSuggestionsInfo} which contains suggestions for each word.
         * This function will run on the incoming IPC thread.
         * So, this is not called on the main thread,
         * but will be called in series on another thread.
         * When you override this method, make sure that suggestionsLimit is applied to suggestions
         * that share the same start position and length.
         *
         * @param textInfos
         * 		an array of the text metadata
         * @param suggestionsLimit
         * 		the maximum number of suggestions to be returned
         * @return an array of {@link SentenceSuggestionsInfo} returned by
        {@link SpellCheckerService.Session#onGetSuggestions(TextInfo, int)}
         */
        public android.view.textservice.SentenceSuggestionsInfo[] onGetSentenceSuggestionsMultiple(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit) {
            if ((textInfos == null) || (textInfos.length == 0)) {
                return android.service.textservice.SpellCheckerService.SentenceLevelAdapter.EMPTY_SENTENCE_SUGGESTIONS_INFOS;
            }
            if (android.service.textservice.SpellCheckerService.DBG) {
                android.util.Log.d(android.service.textservice.SpellCheckerService.TAG, (("onGetSentenceSuggestionsMultiple: + " + textInfos.length) + ", ") + suggestionsLimit);
            }
            if (mSentenceLevelAdapter == null) {
                synchronized(this) {
                    if (mSentenceLevelAdapter == null) {
                        final java.lang.String localeStr = getLocale();
                        if (!android.text.TextUtils.isEmpty(localeStr)) {
                            mSentenceLevelAdapter = new android.service.textservice.SpellCheckerService.SentenceLevelAdapter(new java.util.Locale(localeStr));
                        }
                    }
                }
            }
            if (mSentenceLevelAdapter == null) {
                return android.service.textservice.SpellCheckerService.SentenceLevelAdapter.EMPTY_SENTENCE_SUGGESTIONS_INFOS;
            }
            final int infosSize = textInfos.length;
            final android.view.textservice.SentenceSuggestionsInfo[] retval = new android.view.textservice.SentenceSuggestionsInfo[infosSize];
            for (int i = 0; i < infosSize; ++i) {
                final android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceTextInfoParams textInfoParams = mSentenceLevelAdapter.getSplitWords(textInfos[i]);
                final java.util.ArrayList<android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem> mItems = textInfoParams.mItems;
                final int itemsSize = mItems.size();
                final android.view.textservice.TextInfo[] splitTextInfos = new android.view.textservice.TextInfo[itemsSize];
                for (int j = 0; j < itemsSize; ++j) {
                    splitTextInfos[j] = mItems.get(j).mTextInfo;
                }
                retval[i] = android.service.textservice.SpellCheckerService.SentenceLevelAdapter.reconstructSuggestions(textInfoParams, onGetSuggestionsMultiple(splitTextInfos, suggestionsLimit, true));
            }
            return retval;
        }

        /**
         * Request to abort all tasks executed in SpellChecker.
         * This function will run on the incoming IPC thread.
         * So, this is not called on the main thread,
         * but will be called in series on another thread.
         */
        public void onCancel() {
        }

        /**
         * Request to close this session.
         * This function will run on the incoming IPC thread.
         * So, this is not called on the main thread,
         * but will be called in series on another thread.
         */
        public void onClose() {
        }

        /**
         *
         *
         * @return Locale for this session
         */
        public java.lang.String getLocale() {
            return mInternalSession.getLocale();
        }

        /**
         *
         *
         * @return Bundle for this session
         */
        public android.os.Bundle getBundle() {
            return mInternalSession.getBundle();
        }
    }

    // Preventing from exposing ISpellCheckerSession.aidl, create an internal class.
    private static class InternalISpellCheckerSession extends com.android.internal.textservice.ISpellCheckerSession.Stub {
        private com.android.internal.textservice.ISpellCheckerSessionListener mListener;

        private final android.service.textservice.SpellCheckerService.Session mSession;

        private final java.lang.String mLocale;

        private final android.os.Bundle mBundle;

        public InternalISpellCheckerSession(java.lang.String locale, com.android.internal.textservice.ISpellCheckerSessionListener listener, android.os.Bundle bundle, android.service.textservice.SpellCheckerService.Session session) {
            mListener = listener;
            mSession = session;
            mLocale = locale;
            mBundle = bundle;
            session.setInternalISpellCheckerSession(this);
        }

        @java.lang.Override
        public void onGetSuggestionsMultiple(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            int pri = android.os.Process.getThreadPriority(android.os.Process.myTid());
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                mListener.onGetSuggestions(mSession.onGetSuggestionsMultiple(textInfos, suggestionsLimit, sequentialWords));
            } catch (android.os.RemoteException e) {
            } finally {
                android.os.Process.setThreadPriority(pri);
            }
        }

        @java.lang.Override
        public void onGetSentenceSuggestionsMultiple(android.view.textservice.TextInfo[] textInfos, int suggestionsLimit) {
            try {
                mListener.onGetSentenceSuggestions(mSession.onGetSentenceSuggestionsMultiple(textInfos, suggestionsLimit));
            } catch (android.os.RemoteException e) {
            }
        }

        @java.lang.Override
        public void onCancel() {
            int pri = android.os.Process.getThreadPriority(android.os.Process.myTid());
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                mSession.onCancel();
            } finally {
                android.os.Process.setThreadPriority(pri);
            }
        }

        @java.lang.Override
        public void onClose() {
            int pri = android.os.Process.getThreadPriority(android.os.Process.myTid());
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                mSession.onClose();
            } finally {
                android.os.Process.setThreadPriority(pri);
                mListener = null;
            }
        }

        public java.lang.String getLocale() {
            return mLocale;
        }

        public android.os.Bundle getBundle() {
            return mBundle;
        }
    }

    private static class SpellCheckerServiceBinder extends com.android.internal.textservice.ISpellCheckerService.Stub {
        private final java.lang.ref.WeakReference<android.service.textservice.SpellCheckerService> mInternalServiceRef;

        public SpellCheckerServiceBinder(android.service.textservice.SpellCheckerService service) {
            mInternalServiceRef = new java.lang.ref.WeakReference<android.service.textservice.SpellCheckerService>(service);
        }

        @java.lang.Override
        public com.android.internal.textservice.ISpellCheckerSession getISpellCheckerSession(java.lang.String locale, com.android.internal.textservice.ISpellCheckerSessionListener listener, android.os.Bundle bundle) {
            final android.service.textservice.SpellCheckerService service = mInternalServiceRef.get();
            if (service == null)
                return null;

            final android.service.textservice.SpellCheckerService.Session session = service.createSession();
            final android.service.textservice.SpellCheckerService.InternalISpellCheckerSession internalSession = new android.service.textservice.SpellCheckerService.InternalISpellCheckerSession(locale, listener, bundle, session);
            session.onCreate();
            return internalSession;
        }
    }

    /**
     * Adapter class to accommodate word level spell checking APIs to sentence level spell checking
     * APIs used in
     * {@link SpellCheckerService.Session#onGetSuggestionsMultiple(TextInfo[], int, boolean)}
     */
    private static class SentenceLevelAdapter {
        public static final android.view.textservice.SentenceSuggestionsInfo[] EMPTY_SENTENCE_SUGGESTIONS_INFOS = new android.view.textservice.SentenceSuggestionsInfo[]{  };

        private static final android.view.textservice.SuggestionsInfo EMPTY_SUGGESTIONS_INFO = new android.view.textservice.SuggestionsInfo(0, null);

        /**
         * Container for split TextInfo parameters
         */
        public static class SentenceWordItem {
            public final android.view.textservice.TextInfo mTextInfo;

            public final int mStart;

            public final int mLength;

            public SentenceWordItem(android.view.textservice.TextInfo ti, int start, int end) {
                mTextInfo = ti;
                mStart = start;
                mLength = end - start;
            }
        }

        /**
         * Container for originally queried TextInfo and parameters
         */
        public static class SentenceTextInfoParams {
            final android.view.textservice.TextInfo mOriginalTextInfo;

            final java.util.ArrayList<android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem> mItems;

            final int mSize;

            public SentenceTextInfoParams(android.view.textservice.TextInfo ti, java.util.ArrayList<android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem> items) {
                mOriginalTextInfo = ti;
                mItems = items;
                mSize = items.size();
            }
        }

        private final android.text.method.WordIterator mWordIterator;

        public SentenceLevelAdapter(java.util.Locale locale) {
            mWordIterator = new android.text.method.WordIterator(locale);
        }

        private android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceTextInfoParams getSplitWords(android.view.textservice.TextInfo originalTextInfo) {
            final android.text.method.WordIterator wordIterator = mWordIterator;
            final java.lang.CharSequence originalText = originalTextInfo.getText();
            final int cookie = originalTextInfo.getCookie();
            final int start = 0;
            final int end = originalText.length();
            final java.util.ArrayList<android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem> wordItems = new java.util.ArrayList<android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem>();
            wordIterator.setCharSequence(originalText, 0, originalText.length());
            int wordEnd = wordIterator.following(start);
            int wordStart = wordIterator.getBeginning(wordEnd);
            if (android.service.textservice.SpellCheckerService.DBG) {
                android.util.Log.d(android.service.textservice.SpellCheckerService.TAG, (((("iterator: break: ---- 1st word start = " + wordStart) + ", end = ") + wordEnd) + "\n") + originalText);
            }
            while (((wordStart <= end) && (wordEnd != java.text.BreakIterator.DONE)) && (wordStart != java.text.BreakIterator.DONE)) {
                if ((wordEnd >= start) && (wordEnd > wordStart)) {
                    final java.lang.CharSequence query = originalText.subSequence(wordStart, wordEnd);
                    final android.view.textservice.TextInfo ti = new android.view.textservice.TextInfo(query, 0, query.length(), cookie, query.hashCode());
                    wordItems.add(new android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem(ti, wordStart, wordEnd));
                    if (android.service.textservice.SpellCheckerService.DBG) {
                        android.util.Log.d(android.service.textservice.SpellCheckerService.TAG, (("Adapter: word (" + (wordItems.size() - 1)) + ") ") + query);
                    }
                }
                wordEnd = wordIterator.following(wordEnd);
                if (wordEnd == java.text.BreakIterator.DONE) {
                    break;
                }
                wordStart = wordIterator.getBeginning(wordEnd);
            } 
            return new android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceTextInfoParams(originalTextInfo, wordItems);
        }

        public static android.view.textservice.SentenceSuggestionsInfo reconstructSuggestions(android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceTextInfoParams originalTextInfoParams, android.view.textservice.SuggestionsInfo[] results) {
            if ((results == null) || (results.length == 0)) {
                return null;
            }
            if (android.service.textservice.SpellCheckerService.DBG) {
                android.util.Log.w(android.service.textservice.SpellCheckerService.TAG, "Adapter: onGetSuggestions: got " + results.length);
            }
            if (originalTextInfoParams == null) {
                if (android.service.textservice.SpellCheckerService.DBG) {
                    android.util.Log.w(android.service.textservice.SpellCheckerService.TAG, "Adapter: originalTextInfoParams is null.");
                }
                return null;
            }
            final int originalCookie = originalTextInfoParams.mOriginalTextInfo.getCookie();
            final int originalSequence = originalTextInfoParams.mOriginalTextInfo.getSequence();
            final int querySize = originalTextInfoParams.mSize;
            final int[] offsets = new int[querySize];
            final int[] lengths = new int[querySize];
            final android.view.textservice.SuggestionsInfo[] reconstructedSuggestions = new android.view.textservice.SuggestionsInfo[querySize];
            for (int i = 0; i < querySize; ++i) {
                final android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem item = originalTextInfoParams.mItems.get(i);
                android.view.textservice.SuggestionsInfo result = null;
                for (int j = 0; j < results.length; ++j) {
                    final android.view.textservice.SuggestionsInfo cur = results[j];
                    if ((cur != null) && (cur.getSequence() == item.mTextInfo.getSequence())) {
                        result = cur;
                        result.setCookieAndSequence(originalCookie, originalSequence);
                        break;
                    }
                }
                offsets[i] = item.mStart;
                lengths[i] = item.mLength;
                reconstructedSuggestions[i] = (result != null) ? result : android.service.textservice.SpellCheckerService.SentenceLevelAdapter.EMPTY_SUGGESTIONS_INFO;
                if (android.service.textservice.SpellCheckerService.DBG) {
                    final int size = reconstructedSuggestions[i].getSuggestionsCount();
                    android.util.Log.w(android.service.textservice.SpellCheckerService.TAG, (((((((("reconstructedSuggestions(" + i) + ")") + size) + ", first = ") + (size > 0 ? reconstructedSuggestions[i].getSuggestionAt(0) : "<none>")) + ", offset = ") + offsets[i]) + ", length = ") + lengths[i]);
                }
            }
            return new android.view.textservice.SentenceSuggestionsInfo(reconstructedSuggestions, offsets, lengths);
        }
    }
}

