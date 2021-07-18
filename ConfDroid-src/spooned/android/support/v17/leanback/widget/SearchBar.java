/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A search widget containing a search orb and a text entry view.
 *
 * <p>
 * Note: When {@link SpeechRecognitionCallback} is not used, i.e. using {@link SpeechRecognizer},
 * your application will need to declare android.permission.RECORD_AUDIO in manifest file.
 * If your application target >= 23 and the device is running >= 23, it needs implement
 * {@link SearchBarPermissionListener} where requests runtime permission.
 * </p>
 */
public class SearchBar extends android.widget.RelativeLayout {
    static final java.lang.String TAG = android.support.v17.leanback.widget.SearchBar.class.getSimpleName();

    static final boolean DEBUG = false;

    static final float FULL_LEFT_VOLUME = 1.0F;

    static final float FULL_RIGHT_VOLUME = 1.0F;

    static final int DEFAULT_PRIORITY = 1;

    static final int DO_NOT_LOOP = 0;

    static final float DEFAULT_RATE = 1.0F;

    /**
     * Interface for receiving notification of search query changes.
     */
    public interface SearchBarListener {
        /**
         * Method invoked when the search bar detects a change in the query.
         *
         * @param query
         * 		The current full query.
         */
        public void onSearchQueryChange(java.lang.String query);

        /**
         * <p>Method invoked when the search query is submitted.</p>
         *
         * <p>This method can be called without a preceeding onSearchQueryChange,
         * in particular in the case of a voice input.</p>
         *
         * @param query
         * 		The query being submitted.
         */
        public void onSearchQuerySubmit(java.lang.String query);

        /**
         * Method invoked when the IME is being dismissed.
         *
         * @param query
         * 		The query set in the search bar at the time the IME is being dismissed.
         */
        public void onKeyboardDismiss(java.lang.String query);
    }

    /**
     * Interface that handles runtime permissions requests. App sets listener on SearchBar via
     * {@link #setPermissionListener(SearchBarPermissionListener)}.
     */
    public interface SearchBarPermissionListener {
        /**
         * Method invoked when SearchBar asks for "android.permission.RECORD_AUDIO" runtime
         * permission.
         */
        void requestAudioPermission();
    }

    private android.media.AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new android.media.AudioManager.OnAudioFocusChangeListener() {
        @java.lang.Override
        public void onAudioFocusChange(int focusChange) {
            stopRecognition();
        }
    };

    android.support.v17.leanback.widget.SearchBar.SearchBarListener mSearchBarListener;

    android.support.v17.leanback.widget.SearchEditText mSearchTextEditor;

    android.support.v17.leanback.widget.SpeechOrbView mSpeechOrbView;

    private android.widget.ImageView mBadgeView;

    java.lang.String mSearchQuery;

    private java.lang.String mHint;

    private java.lang.String mTitle;

    private android.graphics.drawable.Drawable mBadgeDrawable;

    final android.os.Handler mHandler = new android.os.Handler();

    private final android.view.inputmethod.InputMethodManager mInputMethodManager;

    boolean mAutoStartRecognition = false;

    private android.graphics.drawable.Drawable mBarBackground;

    private final int mTextColor;

    private final int mTextColorSpeechMode;

    private final int mTextHintColor;

    private final int mTextHintColorSpeechMode;

    private int mBackgroundAlpha;

    private int mBackgroundSpeechAlpha;

    private int mBarHeight;

    private android.speech.SpeechRecognizer mSpeechRecognizer;

    private android.support.v17.leanback.widget.SpeechRecognitionCallback mSpeechRecognitionCallback;

    private boolean mListening;

    android.media.SoundPool mSoundPool;

    android.util.SparseIntArray mSoundMap = new android.util.SparseIntArray();

    boolean mRecognizing = false;

    private final android.content.Context mContext;

    private android.media.AudioManager mAudioManager;

    private android.support.v17.leanback.widget.SearchBar.SearchBarPermissionListener mPermissionListener;

    public SearchBar(android.content.Context context) {
        this(context, null);
    }

    public SearchBar(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        android.content.res.Resources r = getResources();
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(getContext());
        inflater.inflate(R.layout.lb_search_bar, this, true);
        mBarHeight = getResources().getDimensionPixelSize(R.dimen.lb_search_bar_height);
        android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, mBarHeight);
        params.addRule(android.widget.RelativeLayout.ALIGN_PARENT_TOP, android.widget.RelativeLayout.TRUE);
        setLayoutParams(params);
        setBackgroundColor(android.graphics.Color.TRANSPARENT);
        setClipChildren(false);
        mSearchQuery = "";
        mInputMethodManager = ((android.view.inputmethod.InputMethodManager) (context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)));
        mTextColorSpeechMode = r.getColor(R.color.lb_search_bar_text_speech_mode);
        mTextColor = r.getColor(R.color.lb_search_bar_text);
        mBackgroundSpeechAlpha = r.getInteger(R.integer.lb_search_bar_speech_mode_background_alpha);
        mBackgroundAlpha = r.getInteger(R.integer.lb_search_bar_text_mode_background_alpha);
        mTextHintColorSpeechMode = r.getColor(R.color.lb_search_bar_hint_speech_mode);
        mTextHintColor = r.getColor(R.color.lb_search_bar_hint);
        mAudioManager = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        android.widget.RelativeLayout items = ((android.widget.RelativeLayout) (findViewById(R.id.lb_search_bar_items)));
        mBarBackground = items.getBackground();
        mSearchTextEditor = ((android.support.v17.leanback.widget.SearchEditText) (findViewById(R.id.lb_search_text_editor)));
        mBadgeView = ((android.widget.ImageView) (findViewById(R.id.lb_search_bar_badge)));
        if (null != mBadgeDrawable) {
            mBadgeView.setImageDrawable(mBadgeDrawable);
        }
        mSearchTextEditor.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @java.lang.Override
            public void onFocusChange(android.view.View view, boolean hasFocus) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "EditText.onFocusChange " + hasFocus);

                if (hasFocus) {
                    showNativeKeyboard();
                }
                updateUi(hasFocus);
            }
        });
        final java.lang.Runnable mOnTextChangedRunnable = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                setSearchQueryInternal(mSearchTextEditor.getText().toString());
            }
        };
        mSearchTextEditor.addTextChangedListener(new android.text.TextWatcher() {
            @java.lang.Override
            public void beforeTextChanged(java.lang.CharSequence charSequence, int i, int i2, int i3) {
            }

            @java.lang.Override
            public void onTextChanged(java.lang.CharSequence charSequence, int i, int i2, int i3) {
                // don't propagate event during speech recognition.
                if (mRecognizing) {
                    return;
                }
                // while IME opens,  text editor becomes "" then restores to current value
                mHandler.removeCallbacks(mOnTextChangedRunnable);
                mHandler.post(mOnTextChangedRunnable);
            }

            @java.lang.Override
            public void afterTextChanged(android.text.Editable editable) {
            }
        });
        mSearchTextEditor.setOnKeyboardDismissListener(new android.support.v17.leanback.widget.SearchEditText.OnKeyboardDismissListener() {
            @java.lang.Override
            public void onKeyboardDismiss() {
                if (null != mSearchBarListener) {
                    mSearchBarListener.onKeyboardDismiss(mSearchQuery);
                }
            }
        });
        mSearchTextEditor.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @java.lang.Override
            public boolean onEditorAction(android.widget.TextView textView, int action, android.view.KeyEvent keyEvent) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, (("onEditorAction: " + action) + " event: ") + keyEvent);

                boolean handled = true;
                if (((android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH == action) || (android.view.inputmethod.EditorInfo.IME_NULL == action)) && (null != mSearchBarListener)) {
                    if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                        android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Action or enter pressed");

                    hideNativeKeyboard();
                    mHandler.postDelayed(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                                android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Delayed action handling (search)");

                            submitQuery();
                        }
                    }, 500);
                } else
                    if ((android.view.inputmethod.EditorInfo.IME_ACTION_NONE == action) && (null != mSearchBarListener)) {
                        if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                            android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Escaped North");

                        hideNativeKeyboard();
                        mHandler.postDelayed(new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Delayed action handling (escape_north)");

                                mSearchBarListener.onKeyboardDismiss(mSearchQuery);
                            }
                        }, 500);
                    } else
                        if (android.view.inputmethod.EditorInfo.IME_ACTION_GO == action) {
                            if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                                android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Voice Clicked");

                            hideNativeKeyboard();
                            mHandler.postDelayed(new java.lang.Runnable() {
                                @java.lang.Override
                                public void run() {
                                    if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                                        android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Delayed action handling (voice_mode)");

                                    mAutoStartRecognition = true;
                                    mSpeechOrbView.requestFocus();
                                }
                            }, 500);
                        } else {
                            handled = false;
                        }


                return handled;
            }
        });
        mSearchTextEditor.setPrivateImeOptions("EscapeNorth=1;VoiceDismiss=1;");
        mSpeechOrbView = ((android.support.v17.leanback.widget.SpeechOrbView) (findViewById(R.id.lb_search_bar_speech_orb)));
        mSpeechOrbView.setOnOrbClickedListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View view) {
                toggleRecognition();
            }
        });
        mSpeechOrbView.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @java.lang.Override
            public void onFocusChange(android.view.View view, boolean hasFocus) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "SpeechOrb.onFocusChange " + hasFocus);

                if (hasFocus) {
                    hideNativeKeyboard();
                    if (mAutoStartRecognition) {
                        startRecognition();
                        mAutoStartRecognition = false;
                    }
                } else {
                    stopRecognition();
                }
                updateUi(hasFocus);
            }
        });
        updateUi(hasFocus());
        updateHint();
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (android.support.v17.leanback.widget.SearchBar.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Loading soundPool");

        mSoundPool = new android.media.SoundPool(2, android.media.AudioManager.STREAM_SYSTEM, 0);
        loadSounds(mContext);
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        stopRecognition();
        if (android.support.v17.leanback.widget.SearchBar.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Releasing SoundPool");

        mSoundPool.release();
        super.onDetachedFromWindow();
    }

    /**
     * Sets a listener for when the term search changes
     *
     * @param listener
     * 		
     */
    public void setSearchBarListener(android.support.v17.leanback.widget.SearchBar.SearchBarListener listener) {
        mSearchBarListener = listener;
    }

    /**
     * Sets the search query
     *
     * @param query
     * 		the search query to use
     */
    public void setSearchQuery(java.lang.String query) {
        stopRecognition();
        mSearchTextEditor.setText(query);
        setSearchQueryInternal(query);
    }

    void setSearchQueryInternal(java.lang.String query) {
        if (android.support.v17.leanback.widget.SearchBar.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "setSearchQueryInternal " + query);

        if (android.text.TextUtils.equals(mSearchQuery, query)) {
            return;
        }
        mSearchQuery = query;
        if (null != mSearchBarListener) {
            mSearchBarListener.onSearchQueryChange(mSearchQuery);
        }
    }

    /**
     * Sets the title text used in the hint shown in the search bar.
     *
     * @param title
     * 		The hint to use.
     */
    public void setTitle(java.lang.String title) {
        mTitle = title;
        updateHint();
    }

    /**
     * Returns the current title
     */
    public java.lang.String getTitle() {
        return mTitle;
    }

    /**
     * Returns the current search bar hint text.
     */
    public java.lang.CharSequence getHint() {
        return mHint;
    }

    /**
     * Sets the badge drawable showing inside the search bar.
     *
     * @param drawable
     * 		The drawable to be used in the search bar.
     */
    public void setBadgeDrawable(android.graphics.drawable.Drawable drawable) {
        mBadgeDrawable = drawable;
        if (null != mBadgeView) {
            mBadgeView.setImageDrawable(drawable);
            if (null != drawable) {
                mBadgeView.setVisibility(android.view.View.VISIBLE);
            } else {
                mBadgeView.setVisibility(android.view.View.GONE);
            }
        }
    }

    /**
     * Returns the badge drawable
     */
    public android.graphics.drawable.Drawable getBadgeDrawable() {
        return mBadgeDrawable;
    }

    /**
     * Updates the completion list shown by the IME
     *
     * @param completions
     * 		list of completions shown in the IME, can be null or empty to clear them
     */
    public void displayCompletions(java.util.List<java.lang.String> completions) {
        java.util.List<android.view.inputmethod.CompletionInfo> infos = new java.util.ArrayList<>();
        if (null != completions) {
            for (java.lang.String completion : completions) {
                infos.add(new android.view.inputmethod.CompletionInfo(infos.size(), infos.size(), completion));
            }
        }
        android.view.inputmethod.CompletionInfo[] array = new android.view.inputmethod.CompletionInfo[infos.size()];
        displayCompletions(infos.toArray(array));
    }

    /**
     * Updates the completion list shown by the IME
     *
     * @param completions
     * 		list of completions shown in the IME, can be null or empty to clear them
     */
    public void displayCompletions(android.view.inputmethod.CompletionInfo[] completions) {
        mInputMethodManager.displayCompletions(mSearchTextEditor, completions);
    }

    /**
     * Sets the speech recognizer to be used when doing voice search. The Activity/Fragment is in
     * charge of creating and destroying the recognizer with its own lifecycle.
     *
     * @param recognizer
     * 		a SpeechRecognizer
     */
    public void setSpeechRecognizer(android.speech.SpeechRecognizer recognizer) {
        stopRecognition();
        if (null != mSpeechRecognizer) {
            mSpeechRecognizer.setRecognitionListener(null);
            if (mListening) {
                mSpeechRecognizer.cancel();
                mListening = false;
            }
        }
        mSpeechRecognizer = recognizer;
        if ((mSpeechRecognitionCallback != null) && (mSpeechRecognizer != null)) {
            throw new java.lang.IllegalStateException("Can't have speech recognizer and request");
        }
    }

    /**
     * Sets the speech recognition callback.
     */
    public void setSpeechRecognitionCallback(android.support.v17.leanback.widget.SpeechRecognitionCallback request) {
        mSpeechRecognitionCallback = request;
        if ((mSpeechRecognitionCallback != null) && (mSpeechRecognizer != null)) {
            throw new java.lang.IllegalStateException("Can't have speech recognizer and request");
        }
    }

    void hideNativeKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(mSearchTextEditor.getWindowToken(), android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    void showNativeKeyboard() {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mSearchTextEditor.requestFocusFromTouch();
                mSearchTextEditor.dispatchTouchEvent(android.view.MotionEvent.obtain(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.uptimeMillis(), android.view.MotionEvent.ACTION_DOWN, mSearchTextEditor.getWidth(), mSearchTextEditor.getHeight(), 0));
                mSearchTextEditor.dispatchTouchEvent(android.view.MotionEvent.obtain(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.uptimeMillis(), android.view.MotionEvent.ACTION_UP, mSearchTextEditor.getWidth(), mSearchTextEditor.getHeight(), 0));
            }
        });
    }

    /**
     * This will update the hint for the search bar properly depending on state and provided title
     */
    private void updateHint() {
        java.lang.String title = getResources().getString(R.string.lb_search_bar_hint);
        if (!android.text.TextUtils.isEmpty(mTitle)) {
            if (isVoiceMode()) {
                title = getResources().getString(R.string.lb_search_bar_hint_with_title_speech, mTitle);
            } else {
                title = getResources().getString(R.string.lb_search_bar_hint_with_title, mTitle);
            }
        } else
            if (isVoiceMode()) {
                title = getResources().getString(R.string.lb_search_bar_hint_speech);
            }

        mHint = title;
        if (mSearchTextEditor != null) {
            mSearchTextEditor.setHint(mHint);
        }
    }

    void toggleRecognition() {
        if (mRecognizing) {
            stopRecognition();
        } else {
            startRecognition();
        }
    }

    /**
     * Returns true if is not running Recognizer, false otherwise.
     *
     * @return True if is not running Recognizer, false otherwise.
     */
    public boolean isRecognizing() {
        return mRecognizing;
    }

    /**
     * Stops the speech recognition, if already started.
     */
    public void stopRecognition() {
        if (android.support.v17.leanback.widget.SearchBar.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, java.lang.String.format("stopRecognition (listening: %s, recognizing: %s)", mListening, mRecognizing));

        if (!mRecognizing)
            return;

        // Edit text content was cleared when starting recognition; ensure the content is restored
        // in error cases
        mSearchTextEditor.setText(mSearchQuery);
        mSearchTextEditor.setHint(mHint);
        mRecognizing = false;
        if ((mSpeechRecognitionCallback != null) || (null == mSpeechRecognizer))
            return;

        mSpeechOrbView.showNotListening();
        if (mListening) {
            mSpeechRecognizer.cancel();
            mListening = false;
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
        mSpeechRecognizer.setRecognitionListener(null);
    }

    /**
     * Sets listener that handles runtime permission requests.
     *
     * @param listener
     * 		Listener that handles runtime permission requests.
     */
    public void setPermissionListener(android.support.v17.leanback.widget.SearchBar.SearchBarPermissionListener listener) {
        mPermissionListener = listener;
    }

    public void startRecognition() {
        if (android.support.v17.leanback.widget.SearchBar.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, java.lang.String.format("startRecognition (listening: %s, recognizing: %s)", mListening, mRecognizing));

        if (mRecognizing)
            return;

        if (!hasFocus()) {
            requestFocus();
        }
        if (mSpeechRecognitionCallback != null) {
            mSearchTextEditor.setText("");
            mSearchTextEditor.setHint("");
            mSpeechRecognitionCallback.recognizeSpeech();
            mRecognizing = true;
            return;
        }
        if (null == mSpeechRecognizer)
            return;

        int res = getContext().checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO);
        if (android.content.pm.PackageManager.PERMISSION_GRANTED != res) {
            if ((android.os.Build.VERSION.SDK_INT >= 23) && (mPermissionListener != null)) {
                mPermissionListener.requestAudioPermission();
                return;
            } else {
                throw new java.lang.IllegalStateException(Manifest.permission.RECORD_AUDIO + " required for search");
            }
        }
        mRecognizing = true;
        // Request audio focus
        int result = // Use the music stream.
        // Request exclusive transient focus.
        mAudioManager.requestAudioFocus(mAudioFocusChangeListener, android.media.AudioManager.STREAM_MUSIC, android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        if (result != android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "Could not get audio focus");
        }
        mSearchTextEditor.setText("");
        android.content.Intent recognizerIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mSpeechRecognizer.setRecognitionListener(new android.speech.RecognitionListener() {
            @java.lang.Override
            public void onReadyForSpeech(android.os.Bundle bundle) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onReadyForSpeech");

                mSpeechOrbView.showListening();
                playSearchOpen();
            }

            @java.lang.Override
            public void onBeginningOfSpeech() {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onBeginningOfSpeech");

            }

            @java.lang.Override
            public void onRmsChanged(float rmsdB) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onRmsChanged " + rmsdB);

                int level = (rmsdB < 0) ? 0 : ((int) (10 * rmsdB));
                mSpeechOrbView.setSoundLevel(level);
            }

            @java.lang.Override
            public void onBufferReceived(byte[] bytes) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onBufferReceived " + bytes.length);

            }

            @java.lang.Override
            public void onEndOfSpeech() {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onEndOfSpeech");

            }

            @java.lang.Override
            public void onError(int error) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onError " + error);

                switch (error) {
                    case android.speech.SpeechRecognizer.ERROR_NETWORK_TIMEOUT :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer network timeout");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_NETWORK :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer network error");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_AUDIO :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer audio error");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_SERVER :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer server error");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_CLIENT :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer client error");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer speech timeout");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_NO_MATCH :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer no match");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer busy");
                        break;
                    case android.speech.SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS :
                        android.util.Log.w(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer insufficient permissions");
                        break;
                    default :
                        android.util.Log.d(android.support.v17.leanback.widget.SearchBar.TAG, "recognizer other error");
                        break;
                }
                stopRecognition();
                playSearchFailure();
            }

            @java.lang.Override
            public void onResults(android.os.Bundle bundle) {
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onResults");

                final java.util.ArrayList<java.lang.String> matches = bundle.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                        android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "Got results" + matches);

                    mSearchQuery = matches.get(0);
                    mSearchTextEditor.setText(mSearchQuery);
                    submitQuery();
                }
                stopRecognition();
                playSearchSuccess();
            }

            @java.lang.Override
            public void onPartialResults(android.os.Bundle bundle) {
                java.util.ArrayList<java.lang.String> results = bundle.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, (("onPartialResults " + bundle) + " results ") + (results == null ? results : results.size()));

                if ((results == null) || (results.size() == 0)) {
                    return;
                }
                // stableText: high confidence text from PartialResults, if any.
                // Otherwise, existing stable text.
                final java.lang.String stableText = results.get(0);
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onPartialResults stableText " + stableText);

                // pendingText: low confidence text from PartialResults, if any.
                // Otherwise, empty string.
                final java.lang.String pendingText = (results.size() > 1) ? results.get(1) : null;
                if (android.support.v17.leanback.widget.SearchBar.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.SearchBar.TAG, "onPartialResults pendingText " + pendingText);

                mSearchTextEditor.updateRecognizedText(stableText, pendingText);
            }

            @java.lang.Override
            public void onEvent(int i, android.os.Bundle bundle) {
            }
        });
        mListening = true;
        mSpeechRecognizer.startListening(recognizerIntent);
    }

    void updateUi(boolean hasFocus) {
        if (hasFocus) {
            mBarBackground.setAlpha(mBackgroundSpeechAlpha);
            if (isVoiceMode()) {
                mSearchTextEditor.setTextColor(mTextHintColorSpeechMode);
                mSearchTextEditor.setHintTextColor(mTextHintColorSpeechMode);
            } else {
                mSearchTextEditor.setTextColor(mTextColorSpeechMode);
                mSearchTextEditor.setHintTextColor(mTextHintColorSpeechMode);
            }
        } else {
            mBarBackground.setAlpha(mBackgroundAlpha);
            mSearchTextEditor.setTextColor(mTextColor);
            mSearchTextEditor.setHintTextColor(mTextHintColor);
        }
        updateHint();
    }

    private boolean isVoiceMode() {
        return mSpeechOrbView.isFocused();
    }

    void submitQuery() {
        if ((!android.text.TextUtils.isEmpty(mSearchQuery)) && (null != mSearchBarListener)) {
            mSearchBarListener.onSearchQuerySubmit(mSearchQuery);
        }
    }

    private void loadSounds(android.content.Context context) {
        int[] sounds = new int[]{ R.raw.lb_voice_failure, R.raw.lb_voice_open, R.raw.lb_voice_no_input, R.raw.lb_voice_success };
        for (int sound : sounds) {
            mSoundMap.put(sound, mSoundPool.load(context, sound, 1));
        }
    }

    private void play(final int resId) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                int sound = mSoundMap.get(resId);
                mSoundPool.play(sound, android.support.v17.leanback.widget.SearchBar.FULL_LEFT_VOLUME, android.support.v17.leanback.widget.SearchBar.FULL_RIGHT_VOLUME, android.support.v17.leanback.widget.SearchBar.DEFAULT_PRIORITY, android.support.v17.leanback.widget.SearchBar.DO_NOT_LOOP, android.support.v17.leanback.widget.SearchBar.DEFAULT_RATE);
            }
        });
    }

    void playSearchOpen() {
        play(R.raw.lb_voice_open);
    }

    void playSearchFailure() {
        play(R.raw.lb_voice_failure);
    }

    private void playSearchNoInput() {
        play(R.raw.lb_voice_no_input);
    }

    void playSearchSuccess() {
        play(R.raw.lb_voice_success);
    }

    @java.lang.Override
    public void setNextFocusDownId(int viewId) {
        mSpeechOrbView.setNextFocusDownId(viewId);
        mSearchTextEditor.setNextFocusDownId(viewId);
    }
}

