/**
 * This file is auto-generated from SearchFragment.java.  DO NOT MODIFY.
 */
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
package android.support.v17.leanback.app;


/**
 * A fragment to handle searches. An application will supply an implementation
 * of the {@link SearchResultProvider} interface to handle the search and return
 * an {@link ObjectAdapter} containing the results. The results are rendered
 * into a {@link RowsSupportFragment}, in the same way that they are in a {@link BrowseSupportFragment}.
 *
 * <p>If you do not supply a callback via
 * {@link #setSpeechRecognitionCallback(SpeechRecognitionCallback)}, an internal speech
 * recognizer will be used for which your application will need to declare
 * android.permission.RECORD_AUDIO in AndroidManifest file. If app's target version is >= 23 and
 * the device version is >= 23, a permission dialog will show first time using speech recognition.
 * 0 will be used as requestCode in requestPermissions() call.
 * </p>
 * <p>
 * Speech recognition is automatically started when fragment is created, but
 * not when fragment is restored from an instance state.  Activity may manually
 * call {@link #startRecognition()}, typically in onNewIntent().
 * </p>
 */
public class SearchSupportFragment extends android.support.v4.app.Fragment {
    static final java.lang.String TAG = android.support.v17.leanback.app.SearchSupportFragment.class.getSimpleName();

    static final boolean DEBUG = false;

    private static final java.lang.String EXTRA_LEANBACK_BADGE_PRESENT = "LEANBACK_BADGE_PRESENT";

    private static final java.lang.String ARG_PREFIX = android.support.v17.leanback.app.SearchSupportFragment.class.getCanonicalName();

    private static final java.lang.String ARG_QUERY = android.support.v17.leanback.app.SearchSupportFragment.ARG_PREFIX + ".query";

    private static final java.lang.String ARG_TITLE = android.support.v17.leanback.app.SearchSupportFragment.ARG_PREFIX + ".title";

    static final long SPEECH_RECOGNITION_DELAY_MS = 300;

    static final int RESULTS_CHANGED = 0x1;

    static final int QUERY_COMPLETE = 0x2;

    static final int AUDIO_PERMISSION_REQUEST_CODE = 0;

    /**
     * Search API to be provided by the application.
     */
    public static interface SearchResultProvider {
        /**
         * <p>Method invoked some time prior to the first call to onQueryTextChange to retrieve
         * an ObjectAdapter that will contain the results to future updates of the search query.</p>
         *
         * <p>As results are retrieved, the application should use the data set notification methods
         * on the ObjectAdapter to instruct the SearchSupportFragment to update the results.</p>
         *
         * @return ObjectAdapter The result object adapter.
         */
        public android.support.v17.leanback.widget.ObjectAdapter getResultsAdapter();

        /**
         * <p>Method invoked when the search query is updated.</p>
         *
         * <p>This is called as soon as the query changes; it is up to the application to add a
         * delay before actually executing the queries if needed.
         *
         * <p>This method might not always be called before onQueryTextSubmit gets called, in
         * particular for voice input.
         *
         * @param newQuery
         * 		The current search query.
         * @return whether the results changed as a result of the new query.
         */
        public boolean onQueryTextChange(java.lang.String newQuery);

        /**
         * Method invoked when the search query is submitted, either by dismissing the keyboard,
         * pressing search or next on the keyboard or when voice has detected the end of the query.
         *
         * @param query
         * 		The query entered.
         * @return whether the results changed as a result of the query.
         */
        public boolean onQueryTextSubmit(java.lang.String query);
    }

    final android.support.v17.leanback.widget.ObjectAdapter.DataObserver mAdapterObserver = new android.support.v17.leanback.widget.ObjectAdapter.DataObserver() {
        @java.lang.Override
        public void onChanged() {
            // onChanged() may be called multiple times e.g. the provider add
            // rows to ArrayObjectAdapter one by one.
            mHandler.removeCallbacks(mResultsChangedCallback);
            mHandler.post(mResultsChangedCallback);
        }
    };

    final android.os.Handler mHandler = new android.os.Handler();

    final java.lang.Runnable mResultsChangedCallback = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, "results changed, new size " + mResultAdapter.size());

            if ((mRowsSupportFragment != null) && (mRowsSupportFragment.getAdapter() != mResultAdapter)) {
                if (!((mRowsSupportFragment.getAdapter() == null) && (mResultAdapter.size() == 0))) {
                    mRowsSupportFragment.setAdapter(mResultAdapter);
                    mRowsSupportFragment.setSelectedPosition(0);
                }
            }
            updateSearchBarVisibility();
            mStatus |= android.support.v17.leanback.app.SearchSupportFragment.RESULTS_CHANGED;
            if ((mStatus & android.support.v17.leanback.app.SearchSupportFragment.QUERY_COMPLETE) != 0) {
                updateFocus();
            }
            updateSearchBarNextFocusId();
        }
    };

    /**
     * Runs when a new provider is set AND when the fragment view is created.
     */
    private final java.lang.Runnable mSetSearchResultProvider = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if (mRowsSupportFragment == null) {
                // We'll retry once we have a rows fragment
                return;
            }
            // Retrieve the result adapter
            android.support.v17.leanback.widget.ObjectAdapter adapter = mProvider.getResultsAdapter();
            if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, "Got results adapter " + adapter);

            if (adapter != mResultAdapter) {
                boolean firstTime = mResultAdapter == null;
                releaseAdapter();
                mResultAdapter = adapter;
                if (mResultAdapter != null) {
                    mResultAdapter.registerObserver(mAdapterObserver);
                }
                if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, (("mResultAdapter " + mResultAdapter) + " size ") + (mResultAdapter == null ? 0 : mResultAdapter.size()));

                // delay the first time to avoid setting a empty result adapter
                // until we got first onChange() from the provider
                if (!(firstTime && ((mResultAdapter == null) || (mResultAdapter.size() == 0)))) {
                    mRowsSupportFragment.setAdapter(mResultAdapter);
                }
                executePendingQuery();
            }
            updateSearchBarNextFocusId();
            if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, (((("mAutoStartRecognition " + mAutoStartRecognition) + " mResultAdapter ") + mResultAdapter) + " adapter ") + mRowsSupportFragment.getAdapter());

            if (mAutoStartRecognition) {
                mHandler.removeCallbacks(mStartRecognitionRunnable);
                mHandler.postDelayed(mStartRecognitionRunnable, android.support.v17.leanback.app.SearchSupportFragment.SPEECH_RECOGNITION_DELAY_MS);
            } else {
                updateFocus();
            }
        }
    };

    final java.lang.Runnable mStartRecognitionRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            mAutoStartRecognition = false;
            mSearchBar.startRecognition();
        }
    };

    android.support.v17.leanback.app.RowsSupportFragment mRowsSupportFragment;

    android.support.v17.leanback.widget.SearchBar mSearchBar;

    android.support.v17.leanback.app.SearchSupportFragment.SearchResultProvider mProvider;

    java.lang.String mPendingQuery = null;

    android.support.v17.leanback.widget.OnItemViewSelectedListener mOnItemViewSelectedListener;

    private android.support.v17.leanback.widget.OnItemViewClickedListener mOnItemViewClickedListener;

    android.support.v17.leanback.widget.ObjectAdapter mResultAdapter;

    private android.support.v17.leanback.widget.SpeechRecognitionCallback mSpeechRecognitionCallback;

    private java.lang.String mTitle;

    private android.graphics.drawable.Drawable mBadgeDrawable;

    private android.support.v17.leanback.app.SearchSupportFragment.ExternalQuery mExternalQuery;

    private android.speech.SpeechRecognizer mSpeechRecognizer;

    int mStatus;

    boolean mAutoStartRecognition = true;

    private boolean mIsPaused;

    private boolean mPendingStartRecognitionWhenPaused;

    private android.support.v17.leanback.widget.SearchBar.SearchBarPermissionListener mPermissionListener = new android.support.v17.leanback.widget.SearchBar.SearchBarPermissionListener() {
        @java.lang.Override
        public void requestAudioPermission() {
            android.support.v17.leanback.app.PermissionHelper.requestPermissions(android.support.v17.leanback.app.SearchSupportFragment.this, new java.lang.String[]{ Manifest.permission.RECORD_AUDIO }, android.support.v17.leanback.app.SearchSupportFragment.AUDIO_PERMISSION_REQUEST_CODE);
        }
    };

    public void onRequestPermissionsResult(int requestCode, java.lang.String[] permissions, int[] grantResults) {
        if ((requestCode == android.support.v17.leanback.app.SearchSupportFragment.AUDIO_PERMISSION_REQUEST_CODE) && (permissions.length > 0)) {
            if (permissions[0].equals(Manifest.permission.RECORD_AUDIO) && (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)) {
                startRecognition();
            }
        }
    }

    /**
     *
     *
     * @param args
     * 		Bundle to use for the arguments, if null a new Bundle will be created.
     */
    public static android.os.Bundle createArgs(android.os.Bundle args, java.lang.String query) {
        return android.support.v17.leanback.app.SearchSupportFragment.createArgs(args, query, null);
    }

    public static android.os.Bundle createArgs(android.os.Bundle args, java.lang.String query, java.lang.String title) {
        if (args == null) {
            args = new android.os.Bundle();
        }
        args.putString(android.support.v17.leanback.app.SearchSupportFragment.ARG_QUERY, query);
        args.putString(android.support.v17.leanback.app.SearchSupportFragment.ARG_TITLE, title);
        return args;
    }

    /**
     * Creates a search fragment with a given search query.
     *
     * <p>You should only use this if you need to start the search fragment with a
     * pre-filled query.
     *
     * @param query
     * 		The search query to begin with.
     * @return A new SearchSupportFragment.
     */
    public static android.support.v17.leanback.app.SearchSupportFragment newInstance(java.lang.String query) {
        android.support.v17.leanback.app.SearchSupportFragment fragment = new android.support.v17.leanback.app.SearchSupportFragment();
        android.os.Bundle args = android.support.v17.leanback.app.SearchSupportFragment.createArgs(null, query);
        fragment.setArguments(args);
        return fragment;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        if (mAutoStartRecognition) {
            mAutoStartRecognition = savedInstanceState == null;
        }
        super.onCreate(savedInstanceState);
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.lb_search_fragment, container, false);
        android.widget.FrameLayout searchFrame = ((android.widget.FrameLayout) (root.findViewById(R.id.lb_search_frame)));
        mSearchBar = ((android.support.v17.leanback.widget.SearchBar) (searchFrame.findViewById(R.id.lb_search_bar)));
        mSearchBar.setSearchBarListener(new android.support.v17.leanback.widget.SearchBar.SearchBarListener() {
            @java.lang.Override
            public void onSearchQueryChange(java.lang.String query) {
                if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, java.lang.String.format("onSearchQueryChange %s %s", query, null == mProvider ? "(null)" : mProvider));

                if (null != mProvider) {
                    retrieveResults(query);
                } else {
                    mPendingQuery = query;
                }
            }

            @java.lang.Override
            public void onSearchQuerySubmit(java.lang.String query) {
                if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, java.lang.String.format("onSearchQuerySubmit %s", query));

                submitQuery(query);
            }

            @java.lang.Override
            public void onKeyboardDismiss(java.lang.String query) {
                if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, java.lang.String.format("onKeyboardDismiss %s", query));

                queryComplete();
            }
        });
        mSearchBar.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        mSearchBar.setPermissionListener(mPermissionListener);
        applyExternalQuery();
        readArguments(getArguments());
        if (null != mBadgeDrawable) {
            setBadgeDrawable(mBadgeDrawable);
        }
        if (null != mTitle) {
            setTitle(mTitle);
        }
        // Inject the RowsSupportFragment in the results container
        if (getChildFragmentManager().findFragmentById(R.id.lb_results_frame) == null) {
            mRowsSupportFragment = new android.support.v17.leanback.app.RowsSupportFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.lb_results_frame, mRowsSupportFragment).commit();
        } else {
            mRowsSupportFragment = ((android.support.v17.leanback.app.RowsSupportFragment) (getChildFragmentManager().findFragmentById(R.id.lb_results_frame)));
        }
        mRowsSupportFragment.setOnItemViewSelectedListener(new android.support.v17.leanback.widget.OnItemViewSelectedListener() {
            @java.lang.Override
            public void onItemSelected(android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder, java.lang.Object item, android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder, android.support.v17.leanback.widget.Row row) {
                if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG) {
                    int position = mRowsSupportFragment.getSelectedPosition();
                    android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, java.lang.String.format("onItemSelected %d", position));
                }
                updateSearchBarVisibility();
                if (null != mOnItemViewSelectedListener) {
                    mOnItemViewSelectedListener.onItemSelected(itemViewHolder, item, rowViewHolder, row);
                }
            }
        });
        mRowsSupportFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);
        mRowsSupportFragment.setExpand(true);
        if (null != mProvider) {
            onSetSearchResultProvider();
        }
        return root;
    }

    private void resultsAvailable() {
        if ((mStatus & android.support.v17.leanback.app.SearchSupportFragment.QUERY_COMPLETE) != 0) {
            focusOnResults();
        }
        updateSearchBarNextFocusId();
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        android.support.v17.leanback.widget.VerticalGridView list = mRowsSupportFragment.getVerticalGridView();
        int mContainerListAlignTop = getResources().getDimensionPixelSize(R.dimen.lb_search_browse_rows_align_top);
        list.setItemAlignmentOffset(0);
        list.setItemAlignmentOffsetPercent(android.support.v17.leanback.widget.VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
        list.setWindowAlignmentOffset(mContainerListAlignTop);
        list.setWindowAlignmentOffsetPercent(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
        list.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_NO_EDGE);
        // VerticalGridView should not be focusable (see b/26894680 for details).
        list.setFocusable(false);
        list.setFocusableInTouchMode(false);
    }

    @java.lang.Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        if ((mSpeechRecognitionCallback == null) && (null == mSpeechRecognizer)) {
            mSpeechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(getActivity());
            mSearchBar.setSpeechRecognizer(mSpeechRecognizer);
        }
        if (mPendingStartRecognitionWhenPaused) {
            mPendingStartRecognitionWhenPaused = false;
            mSearchBar.startRecognition();
        } else {
            // Ensure search bar state consistency when using external recognizer
            mSearchBar.stopRecognition();
        }
    }

    @java.lang.Override
    public void onPause() {
        releaseRecognizer();
        mIsPaused = true;
        super.onPause();
    }

    @java.lang.Override
    public void onDestroy() {
        releaseAdapter();
        super.onDestroy();
    }

    private void releaseRecognizer() {
        if (null != mSpeechRecognizer) {
            mSearchBar.setSpeechRecognizer(null);
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }
    }

    /**
     * Starts speech recognition.  Typical use case is that
     * activity receives onNewIntent() call when user clicks a MIC button.
     * Note that SearchSupportFragment automatically starts speech recognition
     * at first time created, there is no need to call startRecognition()
     * when fragment is created.
     */
    public void startRecognition() {
        if (mIsPaused) {
            mPendingStartRecognitionWhenPaused = true;
        } else {
            mSearchBar.startRecognition();
        }
    }

    /**
     * Sets the search provider that is responsible for returning results for the
     * search query.
     */
    public void setSearchResultProvider(android.support.v17.leanback.app.SearchSupportFragment.SearchResultProvider searchResultProvider) {
        if (mProvider != searchResultProvider) {
            mProvider = searchResultProvider;
            onSetSearchResultProvider();
        }
    }

    /**
     * Sets an item selection listener for the results.
     *
     * @param listener
     * 		The item selection listener to be invoked when an item in
     * 		the search results is selected.
     */
    public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.OnItemViewSelectedListener listener) {
        mOnItemViewSelectedListener = listener;
    }

    /**
     * Sets an item clicked listener for the results.
     *
     * @param listener
     * 		The item clicked listener to be invoked when an item in
     * 		the search results is clicked.
     */
    public void setOnItemViewClickedListener(android.support.v17.leanback.widget.OnItemViewClickedListener listener) {
        if (listener != mOnItemViewClickedListener) {
            mOnItemViewClickedListener = listener;
            if (mRowsSupportFragment != null) {
                mRowsSupportFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);
            }
        }
    }

    /**
     * Sets the title string to be be shown in an empty search bar. The title
     * may be placed in a call-to-action, such as "Search <i>title</i>" or
     * "Speak to search <i>title</i>".
     */
    public void setTitle(java.lang.String title) {
        mTitle = title;
        if (null != mSearchBar) {
            mSearchBar.setTitle(title);
        }
    }

    /**
     * Returns the title set in the search bar.
     */
    public java.lang.String getTitle() {
        if (null != mSearchBar) {
            return mSearchBar.getTitle();
        }
        return null;
    }

    /**
     * Sets the badge drawable that will be shown inside the search bar next to
     * the title.
     */
    public void setBadgeDrawable(android.graphics.drawable.Drawable drawable) {
        mBadgeDrawable = drawable;
        if (null != mSearchBar) {
            mSearchBar.setBadgeDrawable(drawable);
        }
    }

    /**
     * Returns the badge drawable in the search bar.
     */
    public android.graphics.drawable.Drawable getBadgeDrawable() {
        if (null != mSearchBar) {
            return mSearchBar.getBadgeDrawable();
        }
        return null;
    }

    /**
     * Displays the completions shown by the IME. An application may provide
     * a list of query completions that the system will show in the IME.
     *
     * @param completions
     * 		A list of completions to show in the IME. Setting to
     * 		null or empty will clear the list.
     */
    public void displayCompletions(java.util.List<java.lang.String> completions) {
        mSearchBar.displayCompletions(completions);
    }

    /**
     * Displays the completions shown by the IME. An application may provide
     * a list of query completions that the system will show in the IME.
     *
     * @param completions
     * 		A list of completions to show in the IME. Setting to
     * 		null or empty will clear the list.
     */
    public void displayCompletions(android.view.inputmethod.CompletionInfo[] completions) {
        mSearchBar.displayCompletions(completions);
    }

    /**
     * Sets this callback to have the fragment pass speech recognition requests
     * to the activity rather than using an internal recognizer.
     */
    public void setSpeechRecognitionCallback(android.support.v17.leanback.widget.SpeechRecognitionCallback callback) {
        mSpeechRecognitionCallback = callback;
        if (mSearchBar != null) {
            mSearchBar.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        }
        if (callback != null) {
            releaseRecognizer();
        }
    }

    /**
     * Sets the text of the search query and optionally submits the query. Either
     * {@link SearchResultProvider#onQueryTextChange onQueryTextChange} or
     * {@link SearchResultProvider#onQueryTextSubmit onQueryTextSubmit} will be
     * called on the provider if it is set.
     *
     * @param query
     * 		The search query to set.
     * @param submit
     * 		Whether to submit the query.
     */
    public void setSearchQuery(java.lang.String query, boolean submit) {
        if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, (("setSearchQuery " + query) + " submit ") + submit);

        if (query == null) {
            return;
        }
        mExternalQuery = new android.support.v17.leanback.app.SearchSupportFragment.ExternalQuery(query, submit);
        applyExternalQuery();
        if (mAutoStartRecognition) {
            mAutoStartRecognition = false;
            mHandler.removeCallbacks(mStartRecognitionRunnable);
        }
    }

    /**
     * Sets the text of the search query based on the {@link RecognizerIntent#EXTRA_RESULTS} in
     * the given intent, and optionally submit the query.  If more than one result is present
     * in the results list, the first will be used.
     *
     * @param intent
     * 		Intent received from a speech recognition service.
     * @param submit
     * 		Whether to submit the query.
     */
    public void setSearchQuery(android.content.Intent intent, boolean submit) {
        java.util.ArrayList<java.lang.String> matches = intent.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS);
        if ((matches != null) && (matches.size() > 0)) {
            setSearchQuery(matches.get(0), submit);
        }
    }

    /**
     * Returns an intent that can be used to request speech recognition.
     * Built from the base {@link RecognizerIntent#ACTION_RECOGNIZE_SPEECH} plus
     * extras:
     *
     * <ul>
     * <li>{@link RecognizerIntent#EXTRA_LANGUAGE_MODEL} set to
     * {@link RecognizerIntent#LANGUAGE_MODEL_FREE_FORM}</li>
     * <li>{@link RecognizerIntent#EXTRA_PARTIAL_RESULTS} set to true</li>
     * <li>{@link RecognizerIntent#EXTRA_PROMPT} set to the search bar hint text</li>
     * </ul>
     *
     * For handling the intent returned from the service, see
     * {@link #setSearchQuery(Intent, boolean)}.
     */
    public android.content.Intent getRecognizerIntent() {
        android.content.Intent recognizerIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        if ((mSearchBar != null) && (mSearchBar.getHint() != null)) {
            recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, mSearchBar.getHint());
        }
        recognizerIntent.putExtra(android.support.v17.leanback.app.SearchSupportFragment.EXTRA_LEANBACK_BADGE_PRESENT, mBadgeDrawable != null);
        return recognizerIntent;
    }

    void retrieveResults(java.lang.String searchQuery) {
        if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, "retrieveResults " + searchQuery);

        if (mProvider.onQueryTextChange(searchQuery)) {
            mStatus &= ~android.support.v17.leanback.app.SearchSupportFragment.QUERY_COMPLETE;
        }
    }

    void submitQuery(java.lang.String query) {
        queryComplete();
        if (null != mProvider) {
            mProvider.onQueryTextSubmit(query);
        }
    }

    void queryComplete() {
        if (android.support.v17.leanback.app.SearchSupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.SearchSupportFragment.TAG, "queryComplete");

        mStatus |= android.support.v17.leanback.app.SearchSupportFragment.QUERY_COMPLETE;
        focusOnResults();
    }

    void updateSearchBarVisibility() {
        int position = (mRowsSupportFragment != null) ? mRowsSupportFragment.getSelectedPosition() : -1;
        mSearchBar.setVisibility(((position <= 0) || (mResultAdapter == null)) || (mResultAdapter.size() == 0) ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    void updateSearchBarNextFocusId() {
        if ((mSearchBar == null) || (mResultAdapter == null)) {
            return;
        }
        final int viewId = (((mResultAdapter.size() == 0) || (mRowsSupportFragment == null)) || (mRowsSupportFragment.getVerticalGridView() == null)) ? 0 : mRowsSupportFragment.getVerticalGridView().getId();
        mSearchBar.setNextFocusDownId(viewId);
    }

    void updateFocus() {
        if ((((mResultAdapter != null) && (mResultAdapter.size() > 0)) && (mRowsSupportFragment != null)) && (mRowsSupportFragment.getAdapter() == mResultAdapter)) {
            focusOnResults();
        } else {
            mSearchBar.requestFocus();
        }
    }

    private void focusOnResults() {
        if (((mRowsSupportFragment == null) || (mRowsSupportFragment.getVerticalGridView() == null)) || (mResultAdapter.size() == 0)) {
            return;
        }
        if (mRowsSupportFragment.getVerticalGridView().requestFocus()) {
            mStatus &= ~android.support.v17.leanback.app.SearchSupportFragment.RESULTS_CHANGED;
        }
    }

    private void onSetSearchResultProvider() {
        mHandler.removeCallbacks(mSetSearchResultProvider);
        mHandler.post(mSetSearchResultProvider);
    }

    void releaseAdapter() {
        if (mResultAdapter != null) {
            mResultAdapter.unregisterObserver(mAdapterObserver);
            mResultAdapter = null;
        }
    }

    void executePendingQuery() {
        if ((null != mPendingQuery) && (null != mResultAdapter)) {
            java.lang.String query = mPendingQuery;
            mPendingQuery = null;
            retrieveResults(query);
        }
    }

    private void applyExternalQuery() {
        if ((mExternalQuery == null) || (mSearchBar == null)) {
            return;
        }
        mSearchBar.setSearchQuery(mExternalQuery.mQuery);
        if (mExternalQuery.mSubmit) {
            submitQuery(mExternalQuery.mQuery);
        }
        mExternalQuery = null;
    }

    private void readArguments(android.os.Bundle args) {
        if (null == args) {
            return;
        }
        if (args.containsKey(android.support.v17.leanback.app.SearchSupportFragment.ARG_QUERY)) {
            setSearchQuery(args.getString(android.support.v17.leanback.app.SearchSupportFragment.ARG_QUERY));
        }
        if (args.containsKey(android.support.v17.leanback.app.SearchSupportFragment.ARG_TITLE)) {
            setTitle(args.getString(android.support.v17.leanback.app.SearchSupportFragment.ARG_TITLE));
        }
    }

    private void setSearchQuery(java.lang.String query) {
        mSearchBar.setSearchQuery(query);
    }

    static class ExternalQuery {
        java.lang.String mQuery;

        boolean mSubmit;

        ExternalQuery(java.lang.String query, boolean submit) {
            mQuery = query;
            mSubmit = submit;
        }
    }
}

