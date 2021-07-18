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
package android.widget;


/**
 * Helper class for TextView. Bridge between the TextView and the Dictionary service.
 *
 * @unknown 
 */
public class SpellChecker implements android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener {
    private static final java.lang.String TAG = android.widget.SpellChecker.class.getSimpleName();

    private static final boolean DBG = false;

    // No more than this number of words will be parsed on each iteration to ensure a minimum
    // lock of the UI thread
    public static final int MAX_NUMBER_OF_WORDS = 50;

    // Rough estimate, such that the word iterator interval usually does not need to be shifted
    public static final int AVERAGE_WORD_LENGTH = 7;

    // When parsing, use a character window of that size. Will be shifted if needed
    public static final int WORD_ITERATOR_INTERVAL = android.widget.SpellChecker.AVERAGE_WORD_LENGTH * android.widget.SpellChecker.MAX_NUMBER_OF_WORDS;

    // Pause between each spell check to keep the UI smooth
    private static final int SPELL_PAUSE_DURATION = 400;// milliseconds


    private static final int MIN_SENTENCE_LENGTH = 50;

    private static final int USE_SPAN_RANGE = -1;

    private final android.widget.TextView mTextView;

    android.view.textservice.SpellCheckerSession mSpellCheckerSession;

    // We assume that the sentence level spell check will always provide better results than words.
    // Although word SC has a sequential option.
    private boolean mIsSentenceSpellCheckSupported;

    final int mCookie;

    // Paired arrays for the (id, spellCheckSpan) pair. A negative id means the associated
    // SpellCheckSpan has been recycled and can be-reused.
    // Contains null SpellCheckSpans after index mLength.
    private int[] mIds;

    private android.text.style.SpellCheckSpan[] mSpellCheckSpans;

    // The mLength first elements of the above arrays have been initialized
    private int mLength;

    // Parsers on chunk of text, cutting text into words that will be checked
    private android.widget.SpellChecker.SpellParser[] mSpellParsers = new android.widget.SpellChecker.SpellParser[0];

    private int mSpanSequenceCounter = 0;

    private java.util.Locale mCurrentLocale;

    // Shared by all SpellParsers. Cannot be shared with TextView since it may be used
    // concurrently due to the asynchronous nature of onGetSuggestions.
    private android.text.method.WordIterator mWordIterator;

    @android.annotation.Nullable
    private android.view.textservice.TextServicesManager mTextServicesManager;

    private java.lang.Runnable mSpellRunnable;

    private static final int SUGGESTION_SPAN_CACHE_SIZE = 10;

    private final android.util.LruCache<java.lang.Long, android.text.style.SuggestionSpan> mSuggestionSpanCache = new android.util.LruCache<java.lang.Long, android.text.style.SuggestionSpan>(android.widget.SpellChecker.SUGGESTION_SPAN_CACHE_SIZE);

    public SpellChecker(android.widget.TextView textView) {
        mTextView = textView;
        // Arbitrary: these arrays will automatically double their sizes on demand
        final int size = 1;
        mIds = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(size);
        mSpellCheckSpans = new android.text.style.SpellCheckSpan[mIds.length];
        setLocale(mTextView.getSpellCheckerLocale());
        mCookie = hashCode();
    }

    void resetSession() {
        closeSession();
        mTextServicesManager = mTextView.getTextServicesManagerForUser();
        if (((((mCurrentLocale == null) || (mTextServicesManager == null)) || (mTextView.length() == 0)) || (!mTextServicesManager.isSpellCheckerEnabled())) || (mTextServicesManager.getCurrentSpellCheckerSubtype(true) == null)) {
            mSpellCheckerSession = null;
        } else {
            mSpellCheckerSession = /* Bundle not currently used by the textServicesManager */
            /* means any available languages from current spell checker */
            mTextServicesManager.newSpellCheckerSession(null, mCurrentLocale, this, false);
            mIsSentenceSpellCheckSupported = true;
        }
        // Restore SpellCheckSpans in pool
        for (int i = 0; i < mLength; i++) {
            mIds[i] = -1;
        }
        mLength = 0;
        // Remove existing misspelled SuggestionSpans
        mTextView.removeMisspelledSpans(((android.text.Editable) (mTextView.getText())));
        mSuggestionSpanCache.evictAll();
    }

    private void setLocale(java.util.Locale locale) {
        mCurrentLocale = locale;
        resetSession();
        if (locale != null) {
            // Change SpellParsers' wordIterator locale
            mWordIterator = new android.text.method.WordIterator(locale);
        }
        // This class is the listener for locale change: warn other locale-aware objects
        mTextView.onLocaleChanged();
    }

    /**
     *
     *
     * @return true if a spell checker session has successfully been created. Returns false if not,
    for instance when spell checking has been disabled in settings.
     */
    private boolean isSessionActive() {
        return mSpellCheckerSession != null;
    }

    public void closeSession() {
        if (mSpellCheckerSession != null) {
            mSpellCheckerSession.close();
        }
        final int length = mSpellParsers.length;
        for (int i = 0; i < length; i++) {
            mSpellParsers[i].stop();
        }
        if (mSpellRunnable != null) {
            mTextView.removeCallbacks(mSpellRunnable);
        }
    }

    private int nextSpellCheckSpanIndex() {
        for (int i = 0; i < mLength; i++) {
            if (mIds[i] < 0)
                return i;

        }
        mIds = com.android.internal.util.GrowingArrayUtils.append(mIds, mLength, 0);
        mSpellCheckSpans = com.android.internal.util.GrowingArrayUtils.append(mSpellCheckSpans, mLength, new android.text.style.SpellCheckSpan());
        mLength++;
        return mLength - 1;
    }

    private void addSpellCheckSpan(android.text.Editable editable, int start, int end) {
        final int index = nextSpellCheckSpanIndex();
        android.text.style.SpellCheckSpan spellCheckSpan = mSpellCheckSpans[index];
        editable.setSpan(spellCheckSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spellCheckSpan.setSpellCheckInProgress(false);
        mIds[index] = mSpanSequenceCounter++;
    }

    public void onSpellCheckSpanRemoved(android.text.style.SpellCheckSpan spellCheckSpan) {
        // Recycle any removed SpellCheckSpan (from this code or during text edition)
        for (int i = 0; i < mLength; i++) {
            if (mSpellCheckSpans[i] == spellCheckSpan) {
                mIds[i] = -1;
                return;
            }
        }
    }

    public void onSelectionChanged() {
        spellCheck();
    }

    public void spellCheck(int start, int end) {
        if (android.widget.SpellChecker.DBG) {
            android.util.Log.d(android.widget.SpellChecker.TAG, (("Start spell-checking: " + start) + ", ") + end);
        }
        final java.util.Locale locale = mTextView.getSpellCheckerLocale();
        final boolean isSessionActive = isSessionActive();
        if (((locale == null) || (mCurrentLocale == null)) || (!mCurrentLocale.equals(locale))) {
            setLocale(locale);
            // Re-check the entire text
            start = 0;
            end = mTextView.getText().length();
        } else {
            final boolean spellCheckerActivated = (mTextServicesManager != null) && mTextServicesManager.isSpellCheckerEnabled();
            if (isSessionActive != spellCheckerActivated) {
                // Spell checker has been turned of or off since last spellCheck
                resetSession();
            }
        }
        if (!isSessionActive)
            return;

        // Find first available SpellParser from pool
        final int length = mSpellParsers.length;
        for (int i = 0; i < length; i++) {
            final android.widget.SpellChecker.SpellParser spellParser = mSpellParsers[i];
            if (spellParser.isFinished()) {
                spellParser.parse(start, end);
                return;
            }
        }
        if (android.widget.SpellChecker.DBG) {
            android.util.Log.d(android.widget.SpellChecker.TAG, "new spell parser.");
        }
        // No available parser found in pool, create a new one
        android.widget.SpellChecker.SpellParser[] newSpellParsers = new android.widget.SpellChecker.SpellParser[length + 1];
        java.lang.System.arraycopy(mSpellParsers, 0, newSpellParsers, 0, length);
        mSpellParsers = newSpellParsers;
        android.widget.SpellChecker.SpellParser spellParser = new android.widget.SpellChecker.SpellParser();
        mSpellParsers[length] = spellParser;
        spellParser.parse(start, end);
    }

    private void spellCheck() {
        if (mSpellCheckerSession == null)
            return;

        android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
        final int selectionStart = android.text.Selection.getSelectionStart(editable);
        final int selectionEnd = android.text.Selection.getSelectionEnd(editable);
        android.view.textservice.TextInfo[] textInfos = new android.view.textservice.TextInfo[mLength];
        int textInfosCount = 0;
        for (int i = 0; i < mLength; i++) {
            final android.text.style.SpellCheckSpan spellCheckSpan = mSpellCheckSpans[i];
            if ((mIds[i] < 0) || spellCheckSpan.isSpellCheckInProgress())
                continue;

            final int start = editable.getSpanStart(spellCheckSpan);
            final int end = editable.getSpanEnd(spellCheckSpan);
            // Do not check this word if the user is currently editing it
            final boolean isEditing;
            // Defer spell check when typing a word ending with a punctuation like an apostrophe
            // which could end up being a mid-word punctuation.
            if ((selectionStart == (end + 1)) && android.text.method.WordIterator.isMidWordPunctuation(mCurrentLocale, java.lang.Character.codePointBefore(editable, end + 1))) {
                isEditing = false;
            } else
                if (mIsSentenceSpellCheckSupported) {
                    // Allow the overlap of the cursor and the first boundary of the spell check span
                    // no to skip the spell check of the following word because the
                    // following word will never be spell-checked even if the user finishes composing
                    isEditing = (selectionEnd <= start) || (selectionStart > end);
                } else {
                    isEditing = (selectionEnd < start) || (selectionStart > end);
                }

            if (((start >= 0) && (end > start)) && isEditing) {
                spellCheckSpan.setSpellCheckInProgress(true);
                final android.view.textservice.TextInfo textInfo = new android.view.textservice.TextInfo(editable, start, end, mCookie, mIds[i]);
                textInfos[textInfosCount++] = textInfo;
                if (android.widget.SpellChecker.DBG) {
                    android.util.Log.d(android.widget.SpellChecker.TAG, (((((((((((((((("create TextInfo: (" + i) + "/") + mLength) + ") text = ") + textInfo.getSequence()) + ", cookie = ") + mCookie) + ", seq = ") + mIds[i]) + ", sel start = ") + selectionStart) + ", sel end = ") + selectionEnd) + ", start = ") + start) + ", end = ") + end);
                }
            }
        }
        if (textInfosCount > 0) {
            if (textInfosCount < textInfos.length) {
                android.view.textservice.TextInfo[] textInfosCopy = new android.view.textservice.TextInfo[textInfosCount];
                java.lang.System.arraycopy(textInfos, 0, textInfosCopy, 0, textInfosCount);
                textInfos = textInfosCopy;
            }
            if (mIsSentenceSpellCheckSupported) {
                mSpellCheckerSession.getSentenceSuggestions(textInfos, SuggestionSpan.SUGGESTIONS_MAX_SIZE);
            } else {
                /* TODO Set sequentialWords to true for initial spell check */
                mSpellCheckerSession.getSuggestions(textInfos, SuggestionSpan.SUGGESTIONS_MAX_SIZE, false);
            }
        }
    }

    private android.text.style.SpellCheckSpan onGetSuggestionsInternal(android.view.textservice.SuggestionsInfo suggestionsInfo, int offset, int length) {
        if ((suggestionsInfo == null) || (suggestionsInfo.getCookie() != mCookie)) {
            return null;
        }
        final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
        final int sequenceNumber = suggestionsInfo.getSequence();
        for (int k = 0; k < mLength; ++k) {
            if (sequenceNumber == mIds[k]) {
                final int attributes = suggestionsInfo.getSuggestionsAttributes();
                final boolean isInDictionary = (attributes & android.view.textservice.SuggestionsInfo.RESULT_ATTR_IN_THE_DICTIONARY) > 0;
                final boolean looksLikeTypo = (attributes & android.view.textservice.SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) > 0;
                final android.text.style.SpellCheckSpan spellCheckSpan = mSpellCheckSpans[k];
                // TODO: we need to change that rule for results from a sentence-level spell
                // checker that will probably be in dictionary.
                if ((!isInDictionary) && looksLikeTypo) {
                    createMisspelledSuggestionSpan(editable, suggestionsInfo, spellCheckSpan, offset, length);
                } else {
                    // Valid word -- isInDictionary || !looksLikeTypo
                    if (mIsSentenceSpellCheckSupported) {
                        // Allow the spell checker to remove existing misspelled span by
                        // overwriting the span over the same place
                        final int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
                        final int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
                        final int start;
                        final int end;
                        if ((offset != android.widget.SpellChecker.USE_SPAN_RANGE) && (length != android.widget.SpellChecker.USE_SPAN_RANGE)) {
                            start = spellCheckSpanStart + offset;
                            end = start + length;
                        } else {
                            start = spellCheckSpanStart;
                            end = spellCheckSpanEnd;
                        }
                        if (((spellCheckSpanStart >= 0) && (spellCheckSpanEnd > spellCheckSpanStart)) && (end > start)) {
                            final java.lang.Long key = java.lang.Long.valueOf(android.text.TextUtils.packRangeInLong(start, end));
                            final android.text.style.SuggestionSpan tempSuggestionSpan = mSuggestionSpanCache.get(key);
                            if (tempSuggestionSpan != null) {
                                if (android.widget.SpellChecker.DBG) {
                                    android.util.Log.i(android.widget.SpellChecker.TAG, "Remove existing misspelled span. " + editable.subSequence(start, end));
                                }
                                editable.removeSpan(tempSuggestionSpan);
                                mSuggestionSpanCache.remove(key);
                            }
                        }
                    }
                }
                return spellCheckSpan;
            }
        }
        return null;
    }

    @java.lang.Override
    public void onGetSuggestions(android.view.textservice.SuggestionsInfo[] results) {
        final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
        for (int i = 0; i < results.length; ++i) {
            final android.text.style.SpellCheckSpan spellCheckSpan = onGetSuggestionsInternal(results[i], android.widget.SpellChecker.USE_SPAN_RANGE, android.widget.SpellChecker.USE_SPAN_RANGE);
            if (spellCheckSpan != null) {
                // onSpellCheckSpanRemoved will recycle this span in the pool
                editable.removeSpan(spellCheckSpan);
            }
        }
        scheduleNewSpellCheck();
    }

    @java.lang.Override
    public void onGetSentenceSuggestions(android.view.textservice.SentenceSuggestionsInfo[] results) {
        final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
        for (int i = 0; i < results.length; ++i) {
            final android.view.textservice.SentenceSuggestionsInfo ssi = results[i];
            if (ssi == null) {
                continue;
            }
            android.text.style.SpellCheckSpan spellCheckSpan = null;
            for (int j = 0; j < ssi.getSuggestionsCount(); ++j) {
                final android.view.textservice.SuggestionsInfo suggestionsInfo = ssi.getSuggestionsInfoAt(j);
                if (suggestionsInfo == null) {
                    continue;
                }
                final int offset = ssi.getOffsetAt(j);
                final int length = ssi.getLengthAt(j);
                final android.text.style.SpellCheckSpan scs = onGetSuggestionsInternal(suggestionsInfo, offset, length);
                if ((spellCheckSpan == null) && (scs != null)) {
                    // the spellCheckSpan is shared by all the "SuggestionsInfo"s in the same
                    // SentenceSuggestionsInfo. Removal is deferred after this loop.
                    spellCheckSpan = scs;
                }
            }
            if (spellCheckSpan != null) {
                // onSpellCheckSpanRemoved will recycle this span in the pool
                editable.removeSpan(spellCheckSpan);
            }
        }
        scheduleNewSpellCheck();
    }

    private void scheduleNewSpellCheck() {
        if (android.widget.SpellChecker.DBG) {
            android.util.Log.i(android.widget.SpellChecker.TAG, "schedule new spell check.");
        }
        if (mSpellRunnable == null) {
            mSpellRunnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final int length = mSpellParsers.length;
                    for (int i = 0; i < length; i++) {
                        final android.widget.SpellChecker.SpellParser spellParser = mSpellParsers[i];
                        if (!spellParser.isFinished()) {
                            spellParser.parse();
                            break;// run one spell parser at a time to bound running time

                        }
                    }
                }
            };
        } else {
            mTextView.removeCallbacks(mSpellRunnable);
        }
        mTextView.postDelayed(mSpellRunnable, android.widget.SpellChecker.SPELL_PAUSE_DURATION);
    }

    private void createMisspelledSuggestionSpan(android.text.Editable editable, android.view.textservice.SuggestionsInfo suggestionsInfo, android.text.style.SpellCheckSpan spellCheckSpan, int offset, int length) {
        final int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
        final int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
        if ((spellCheckSpanStart < 0) || (spellCheckSpanEnd <= spellCheckSpanStart))
            return;
        // span was removed in the meantime

        final int start;
        final int end;
        if ((offset != android.widget.SpellChecker.USE_SPAN_RANGE) && (length != android.widget.SpellChecker.USE_SPAN_RANGE)) {
            start = spellCheckSpanStart + offset;
            end = start + length;
        } else {
            start = spellCheckSpanStart;
            end = spellCheckSpanEnd;
        }
        final int suggestionsCount = suggestionsInfo.getSuggestionsCount();
        java.lang.String[] suggestions;
        if (suggestionsCount > 0) {
            suggestions = new java.lang.String[suggestionsCount];
            for (int i = 0; i < suggestionsCount; i++) {
                suggestions[i] = suggestionsInfo.getSuggestionAt(i);
            }
        } else {
            suggestions = com.android.internal.util.ArrayUtils.emptyArray(java.lang.String.class);
        }
        android.text.style.SuggestionSpan suggestionSpan = new android.text.style.SuggestionSpan(mTextView.getContext(), suggestions, android.text.style.SuggestionSpan.FLAG_EASY_CORRECT | android.text.style.SuggestionSpan.FLAG_MISSPELLED);
        // TODO: Remove mIsSentenceSpellCheckSupported by extracting an interface
        // to share the logic of word level spell checker and sentence level spell checker
        if (mIsSentenceSpellCheckSupported) {
            final java.lang.Long key = java.lang.Long.valueOf(android.text.TextUtils.packRangeInLong(start, end));
            final android.text.style.SuggestionSpan tempSuggestionSpan = mSuggestionSpanCache.get(key);
            if (tempSuggestionSpan != null) {
                if (android.widget.SpellChecker.DBG) {
                    android.util.Log.i(android.widget.SpellChecker.TAG, "Cached span on the same position is cleard. " + editable.subSequence(start, end));
                }
                editable.removeSpan(tempSuggestionSpan);
            }
            mSuggestionSpanCache.put(key, suggestionSpan);
        }
        editable.setSpan(suggestionSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        /* No cursor involved */
        mTextView.invalidateRegion(start, end, false);
    }

    private class SpellParser {
        private java.lang.Object mRange = new java.lang.Object();

        public void parse(int start, int end) {
            final int max = mTextView.length();
            final int parseEnd;
            if (end > max) {
                android.util.Log.w(android.widget.SpellChecker.TAG, (("Parse invalid region, from " + start) + " to ") + end);
                parseEnd = max;
            } else {
                parseEnd = end;
            }
            if (parseEnd > start) {
                setRangeSpan(((android.text.Editable) (mTextView.getText())), start, parseEnd);
                parse();
            }
        }

        public boolean isFinished() {
            return ((android.text.Editable) (mTextView.getText())).getSpanStart(mRange) < 0;
        }

        public void stop() {
            removeRangeSpan(((android.text.Editable) (mTextView.getText())));
        }

        private void setRangeSpan(android.text.Editable editable, int start, int end) {
            if (android.widget.SpellChecker.DBG) {
                android.util.Log.d(android.widget.SpellChecker.TAG, (("set next range span: " + start) + ", ") + end);
            }
            editable.setSpan(mRange, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        private void removeRangeSpan(android.text.Editable editable) {
            if (android.widget.SpellChecker.DBG) {
                android.util.Log.d(android.widget.SpellChecker.TAG, ("Remove range span." + editable.getSpanStart(editable)) + editable.getSpanEnd(editable));
            }
            editable.removeSpan(mRange);
        }

        public void parse() {
            android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
            // Iterate over the newly added text and schedule new SpellCheckSpans
            final int start;
            if (mIsSentenceSpellCheckSupported) {
                // TODO: Find the start position of the sentence.
                // Set span with the context
                start = java.lang.Math.max(0, editable.getSpanStart(mRange) - android.widget.SpellChecker.MIN_SENTENCE_LENGTH);
            } else {
                start = editable.getSpanStart(mRange);
            }
            final int end = editable.getSpanEnd(mRange);
            int wordIteratorWindowEnd = java.lang.Math.min(end, start + android.widget.SpellChecker.WORD_ITERATOR_INTERVAL);
            mWordIterator.setCharSequence(editable, start, wordIteratorWindowEnd);
            // Move back to the beginning of the current word, if any
            int wordStart = mWordIterator.preceding(start);
            int wordEnd;
            if (wordStart == java.text.BreakIterator.DONE) {
                wordEnd = mWordIterator.following(start);
                if (wordEnd != java.text.BreakIterator.DONE) {
                    wordStart = mWordIterator.getBeginning(wordEnd);
                }
            } else {
                wordEnd = mWordIterator.getEnd(wordStart);
            }
            if (wordEnd == java.text.BreakIterator.DONE) {
                if (android.widget.SpellChecker.DBG) {
                    android.util.Log.i(android.widget.SpellChecker.TAG, "No more spell check.");
                }
                removeRangeSpan(editable);
                return;
            }
            // We need to expand by one character because we want to include the spans that
            // end/start at position start/end respectively.
            android.text.style.SpellCheckSpan[] spellCheckSpans = editable.getSpans(start - 1, end + 1, android.text.style.SpellCheckSpan.class);
            android.text.style.SuggestionSpan[] suggestionSpans = editable.getSpans(start - 1, end + 1, android.text.style.SuggestionSpan.class);
            int wordCount = 0;
            boolean scheduleOtherSpellCheck = false;
            if (mIsSentenceSpellCheckSupported) {
                if (wordIteratorWindowEnd < end) {
                    if (android.widget.SpellChecker.DBG) {
                        android.util.Log.i(android.widget.SpellChecker.TAG, "schedule other spell check.");
                    }
                    // Several batches needed on that region. Cut after last previous word
                    scheduleOtherSpellCheck = true;
                }
                int spellCheckEnd = mWordIterator.preceding(wordIteratorWindowEnd);
                boolean correct = spellCheckEnd != java.text.BreakIterator.DONE;
                if (correct) {
                    spellCheckEnd = mWordIterator.getEnd(spellCheckEnd);
                    correct = spellCheckEnd != java.text.BreakIterator.DONE;
                }
                if (!correct) {
                    if (android.widget.SpellChecker.DBG) {
                        android.util.Log.i(android.widget.SpellChecker.TAG, "Incorrect range span.");
                    }
                    removeRangeSpan(editable);
                    return;
                }
                do {
                    // TODO: Find the start position of the sentence.
                    int spellCheckStart = wordStart;
                    boolean createSpellCheckSpan = true;
                    // Cancel or merge overlapped spell check spans
                    for (int i = 0; i < mLength; ++i) {
                        final android.text.style.SpellCheckSpan spellCheckSpan = mSpellCheckSpans[i];
                        if ((mIds[i] < 0) || spellCheckSpan.isSpellCheckInProgress()) {
                            continue;
                        }
                        final int spanStart = editable.getSpanStart(spellCheckSpan);
                        final int spanEnd = editable.getSpanEnd(spellCheckSpan);
                        if ((spanEnd < spellCheckStart) || (spellCheckEnd < spanStart)) {
                            // No need to merge
                            continue;
                        }
                        if ((spanStart <= spellCheckStart) && (spellCheckEnd <= spanEnd)) {
                            // There is a completely overlapped spell check span
                            // skip this span
                            createSpellCheckSpan = false;
                            if (android.widget.SpellChecker.DBG) {
                                android.util.Log.i(android.widget.SpellChecker.TAG, "The range is overrapped. Skip spell check.");
                            }
                            break;
                        }
                        // This spellCheckSpan is replaced by the one we are creating
                        editable.removeSpan(spellCheckSpan);
                        spellCheckStart = java.lang.Math.min(spanStart, spellCheckStart);
                        spellCheckEnd = java.lang.Math.max(spanEnd, spellCheckEnd);
                    }
                    if (android.widget.SpellChecker.DBG) {
                        android.util.Log.d(android.widget.SpellChecker.TAG, ((((((("addSpellCheckSpan: " + ", End = ") + spellCheckEnd) + ", Start = ") + spellCheckStart) + ", next = ") + scheduleOtherSpellCheck) + "\n") + editable.subSequence(spellCheckStart, spellCheckEnd));
                    }
                    // Stop spell checking when there are no characters in the range.
                    if (spellCheckEnd < start) {
                        break;
                    }
                    if (spellCheckEnd <= spellCheckStart) {
                        android.util.Log.w(android.widget.SpellChecker.TAG, (("Trying to spellcheck invalid region, from " + start) + " to ") + end);
                        break;
                    }
                    if (createSpellCheckSpan) {
                        addSpellCheckSpan(editable, spellCheckStart, spellCheckEnd);
                    }
                } while (false );
                wordStart = spellCheckEnd;
            } else {
                while (wordStart <= end) {
                    if ((wordEnd >= start) && (wordEnd > wordStart)) {
                        if (wordCount >= android.widget.SpellChecker.MAX_NUMBER_OF_WORDS) {
                            scheduleOtherSpellCheck = true;
                            break;
                        }
                        // A new word has been created across the interval boundaries with this
                        // edit. The previous spans (that ended on start / started on end) are
                        // not valid anymore and must be removed.
                        if ((wordStart < start) && (wordEnd > start)) {
                            removeSpansAt(editable, start, spellCheckSpans);
                            removeSpansAt(editable, start, suggestionSpans);
                        }
                        if ((wordStart < end) && (wordEnd > end)) {
                            removeSpansAt(editable, end, spellCheckSpans);
                            removeSpansAt(editable, end, suggestionSpans);
                        }
                        // Do not create new boundary spans if they already exist
                        boolean createSpellCheckSpan = true;
                        if (wordEnd == start) {
                            for (int i = 0; i < spellCheckSpans.length; i++) {
                                final int spanEnd = editable.getSpanEnd(spellCheckSpans[i]);
                                if (spanEnd == start) {
                                    createSpellCheckSpan = false;
                                    break;
                                }
                            }
                        }
                        if (wordStart == end) {
                            for (int i = 0; i < spellCheckSpans.length; i++) {
                                final int spanStart = editable.getSpanStart(spellCheckSpans[i]);
                                if (spanStart == end) {
                                    createSpellCheckSpan = false;
                                    break;
                                }
                            }
                        }
                        if (createSpellCheckSpan) {
                            addSpellCheckSpan(editable, wordStart, wordEnd);
                        }
                        wordCount++;
                    }
                    // iterate word by word
                    int originalWordEnd = wordEnd;
                    wordEnd = mWordIterator.following(wordEnd);
                    if ((wordIteratorWindowEnd < end) && ((wordEnd == java.text.BreakIterator.DONE) || (wordEnd >= wordIteratorWindowEnd))) {
                        wordIteratorWindowEnd = java.lang.Math.min(end, originalWordEnd + android.widget.SpellChecker.WORD_ITERATOR_INTERVAL);
                        mWordIterator.setCharSequence(editable, originalWordEnd, wordIteratorWindowEnd);
                        wordEnd = mWordIterator.following(originalWordEnd);
                    }
                    if (wordEnd == java.text.BreakIterator.DONE)
                        break;

                    wordStart = mWordIterator.getBeginning(wordEnd);
                    if (wordStart == java.text.BreakIterator.DONE) {
                        break;
                    }
                } 
            }
            if ((scheduleOtherSpellCheck && (wordStart != java.text.BreakIterator.DONE)) && (wordStart <= end)) {
                // Update range span: start new spell check from last wordStart
                setRangeSpan(editable, wordStart, end);
            } else {
                removeRangeSpan(editable);
            }
            spellCheck();
        }

        private <T> void removeSpansAt(android.text.Editable editable, int offset, T[] spans) {
            final int length = spans.length;
            for (int i = 0; i < length; i++) {
                final T span = spans[i];
                final int start = editable.getSpanStart(span);
                if (start > offset)
                    continue;

                final int end = editable.getSpanEnd(span);
                if (end < offset)
                    continue;

                editable.removeSpan(span);
            }
        }
    }

    public static boolean haveWordBoundariesChanged(final android.text.Editable editable, final int start, final int end, final int spanStart, final int spanEnd) {
        final boolean haveWordBoundariesChanged;
        if ((spanEnd != start) && (spanStart != end)) {
            haveWordBoundariesChanged = true;
            if (android.widget.SpellChecker.DBG) {
                android.util.Log.d(android.widget.SpellChecker.TAG, "(1) Text inside the span has been modified. Remove.");
            }
        } else
            if ((spanEnd == start) && (start < editable.length())) {
                final int codePoint = java.lang.Character.codePointAt(editable, start);
                haveWordBoundariesChanged = java.lang.Character.isLetterOrDigit(codePoint);
                if (android.widget.SpellChecker.DBG) {
                    android.util.Log.d(android.widget.SpellChecker.TAG, ((((((("(2) Characters have been appended to the spanned text. " + (haveWordBoundariesChanged ? "Remove.<" : "Keep. <")) + ((char) (codePoint))) + ">, ") + editable) + ", ") + editable.subSequence(spanStart, spanEnd)) + ", ") + start);
                }
            } else
                if ((spanStart == end) && (end > 0)) {
                    final int codePoint = java.lang.Character.codePointBefore(editable, end);
                    haveWordBoundariesChanged = java.lang.Character.isLetterOrDigit(codePoint);
                    if (android.widget.SpellChecker.DBG) {
                        android.util.Log.d(android.widget.SpellChecker.TAG, ((((((("(3) Characters have been prepended to the spanned text. " + (haveWordBoundariesChanged ? "Remove.<" : "Keep.<")) + ((char) (codePoint))) + ">, ") + editable) + ", ") + editable.subSequence(spanStart, spanEnd)) + ", ") + end);
                    }
                } else {
                    if (android.widget.SpellChecker.DBG) {
                        android.util.Log.d(android.widget.SpellChecker.TAG, "(4) Characters adjacent to the spanned text were deleted. Keep.");
                    }
                    haveWordBoundariesChanged = false;
                }


        return haveWordBoundariesChanged;
    }
}

