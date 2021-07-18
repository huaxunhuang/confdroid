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
package android.support.v7.widget;


/**
 * A widget that provides a user interface for the user to enter a search query and submit a request
 * to a search provider. Shows a list of query suggestions or results, if available, and allows the
 * user to pick a suggestion or result to launch into.
 *
 * <p class="note"><strong>Note:</strong> This class is included in the <a
 * href="{@docRoot }tools/extras/support-library.html">support library</a> for compatibility
 * with API level 7 and higher. If you're developing your app for API level 11 and higher
 * <em>only</em>, you should instead use the framework {@link android.widget.SearchView} class.</p>
 *
 * <p>
 * When the SearchView is used in an {@link android.support.v7.app.ActionBar}
 * as an action view, it's collapsed by default, so you must provide an icon for the action.
 * </p>
 * <p>
 * If you want the search field to always be visible, then call
 * {@link #setIconifiedByDefault(boolean) setIconifiedByDefault(false)}.
 * </p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using {@code SearchView}, read the
 * <a href="{@docRoot }guide/topics/search/index.html">Search</a> API guide.
 * Additional information about action views is also available in the <<a
 * href="{@docRoot }guide/topics/ui/actionbar.html#ActionView">Action Bar</a> API guide</p>
 * </div>
 *
 * @see android.support.v4.view.MenuItemCompat#SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
 */
public class SearchView extends android.support.v7.widget.LinearLayoutCompat implements android.support.v7.view.CollapsibleActionView {
    static final boolean DBG = false;

    static final java.lang.String LOG_TAG = "SearchView";

    /**
     * Private constant for removing the microphone in the keyboard.
     */
    private static final java.lang.String IME_OPTION_NO_MICROPHONE = "nm";

    final android.support.v7.widget.SearchView.SearchAutoComplete mSearchSrcTextView;

    private final android.view.View mSearchEditFrame;

    private final android.view.View mSearchPlate;

    private final android.view.View mSubmitArea;

    final android.widget.ImageView mSearchButton;

    final android.widget.ImageView mGoButton;

    final android.widget.ImageView mCloseButton;

    final android.widget.ImageView mVoiceButton;

    private final android.view.View mDropDownAnchor;

    private android.support.v7.widget.SearchView.UpdatableTouchDelegate mTouchDelegate;

    private android.graphics.Rect mSearchSrcTextViewBounds = new android.graphics.Rect();

    private android.graphics.Rect mSearchSrtTextViewBoundsExpanded = new android.graphics.Rect();

    private int[] mTemp = new int[2];

    private int[] mTemp2 = new int[2];

    /**
     * Icon optionally displayed when the SearchView is collapsed.
     */
    private final android.widget.ImageView mCollapsedIcon;

    /**
     * Drawable used as an EditText hint.
     */
    private final android.graphics.drawable.Drawable mSearchHintIcon;

    // Resources used by SuggestionsAdapter to display suggestions.
    private final int mSuggestionRowLayout;

    private final int mSuggestionCommitIconResId;

    // Intents used for voice searching.
    private final android.content.Intent mVoiceWebSearchIntent;

    private final android.content.Intent mVoiceAppSearchIntent;

    private final java.lang.CharSequence mDefaultQueryHint;

    private android.support.v7.widget.SearchView.OnQueryTextListener mOnQueryChangeListener;

    private android.support.v7.widget.SearchView.OnCloseListener mOnCloseListener;

    android.view.View.OnFocusChangeListener mOnQueryTextFocusChangeListener;

    private android.support.v7.widget.SearchView.OnSuggestionListener mOnSuggestionListener;

    private android.view.View.OnClickListener mOnSearchClickListener;

    private boolean mIconifiedByDefault;

    private boolean mIconified;

    android.support.v4.widget.CursorAdapter mSuggestionsAdapter;

    private boolean mSubmitButtonEnabled;

    private java.lang.CharSequence mQueryHint;

    private boolean mQueryRefinement;

    private boolean mClearingFocus;

    private int mMaxWidth;

    private boolean mVoiceButtonEnabled;

    private java.lang.CharSequence mOldQueryText;

    private java.lang.CharSequence mUserQuery;

    private boolean mExpandedInActionView;

    private int mCollapsedImeOptions;

    android.app.SearchableInfo mSearchable;

    private android.os.Bundle mAppSearchData;

    static final android.support.v7.widget.SearchView.AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new android.support.v7.widget.SearchView.AutoCompleteTextViewReflector();

    /* SearchView can be set expanded before the IME is ready to be shown during
    initial UI setup. The show operation is asynchronous to account for this.
     */
    private java.lang.Runnable mShowImeRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.view.inputmethod.InputMethodManager imm = ((android.view.inputmethod.InputMethodManager) (getContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)));
            if (imm != null) {
                android.support.v7.widget.SearchView.HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(imm, android.support.v7.widget.SearchView.this, 0);
            }
        }
    };

    private final java.lang.Runnable mUpdateDrawableStateRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            updateFocusedState();
        }
    };

    private java.lang.Runnable mReleaseCursorRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if ((mSuggestionsAdapter != null) && (mSuggestionsAdapter instanceof android.support.v7.widget.SuggestionsAdapter)) {
                mSuggestionsAdapter.changeCursor(null);
            }
        }
    };

    // A weak map of drawables we've gotten from other packages, so we don't load them
    // more than once.
    private final java.util.WeakHashMap<java.lang.String, android.graphics.drawable.Drawable.ConstantState> mOutsideDrawablesCache = new java.util.WeakHashMap<java.lang.String, android.graphics.drawable.Drawable.ConstantState>();

    /**
     * Callbacks for changes to the query text.
     */
    public interface OnQueryTextListener {
        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query
         * 		the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
        SearchView perform the default action.
         */
        boolean onQueryTextSubmit(java.lang.String query);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText
         * 		the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
        suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(java.lang.String newText);
    }

    public interface OnCloseListener {
        /**
         * The user is attempting to close the SearchView.
         *
         * @return true if the listener wants to override the default behavior of clearing the
        text field and dismissing it, false otherwise.
         */
        boolean onClose();
    }

    /**
     * Callback interface for selection events on suggestions. These callbacks
     * are only relevant when a SearchableInfo has been specified by {@link #setSearchableInfo}.
     */
    public interface OnSuggestionListener {
        /**
         * Called when a suggestion was selected by navigating to it.
         *
         * @param position
         * 		the absolute position in the list of suggestions.
         * @return true if the listener handles the event and wants to override the default
        behavior of possibly rewriting the query based on the selected item, false otherwise.
         */
        boolean onSuggestionSelect(int position);

        /**
         * Called when a suggestion was clicked.
         *
         * @param position
         * 		the absolute position of the clicked item in the list of suggestions.
         * @return true if the listener handles the event and wants to override the default
        behavior of launching any intent or submitting a search query specified on that item.
        Return false otherwise.
         */
        boolean onSuggestionClick(int position);
    }

    public SearchView(android.content.Context context) {
        this(context, null);
    }

    public SearchView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }

    public SearchView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SearchView, defStyleAttr, 0);
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        final int layoutResId = a.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view);
        inflater.inflate(layoutResId, this, true);
        mSearchSrcTextView = ((android.support.v7.widget.SearchView.SearchAutoComplete) (findViewById(R.id.search_src_text)));
        mSearchSrcTextView.setSearchView(this);
        mSearchEditFrame = findViewById(R.id.search_edit_frame);
        mSearchPlate = findViewById(R.id.search_plate);
        mSubmitArea = findViewById(R.id.submit_area);
        mSearchButton = ((android.widget.ImageView) (findViewById(R.id.search_button)));
        mGoButton = ((android.widget.ImageView) (findViewById(R.id.search_go_btn)));
        mCloseButton = ((android.widget.ImageView) (findViewById(R.id.search_close_btn)));
        mVoiceButton = ((android.widget.ImageView) (findViewById(R.id.search_voice_btn)));
        mCollapsedIcon = ((android.widget.ImageView) (findViewById(R.id.search_mag_icon)));
        // Set up icons and backgrounds.
        android.support.v4.view.ViewCompat.setBackground(mSearchPlate, a.getDrawable(R.styleable.SearchView_queryBackground));
        android.support.v4.view.ViewCompat.setBackground(mSubmitArea, a.getDrawable(R.styleable.SearchView_submitBackground));
        mSearchButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
        mGoButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_goIcon));
        mCloseButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_closeIcon));
        mVoiceButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_voiceIcon));
        mCollapsedIcon.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
        mSearchHintIcon = a.getDrawable(R.styleable.SearchView_searchHintIcon);
        // Extract dropdown layout resource IDs for later use.
        mSuggestionRowLayout = a.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
        mSuggestionCommitIconResId = a.getResourceId(R.styleable.SearchView_commitIcon, 0);
        mSearchButton.setOnClickListener(mOnClickListener);
        mCloseButton.setOnClickListener(mOnClickListener);
        mGoButton.setOnClickListener(mOnClickListener);
        mVoiceButton.setOnClickListener(mOnClickListener);
        mSearchSrcTextView.setOnClickListener(mOnClickListener);
        mSearchSrcTextView.addTextChangedListener(mTextWatcher);
        mSearchSrcTextView.setOnEditorActionListener(mOnEditorActionListener);
        mSearchSrcTextView.setOnItemClickListener(mOnItemClickListener);
        mSearchSrcTextView.setOnItemSelectedListener(mOnItemSelectedListener);
        mSearchSrcTextView.setOnKeyListener(mTextKeyListener);
        // Inform any listener of focus changes
        mSearchSrcTextView.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @java.lang.Override
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (mOnQueryTextFocusChangeListener != null) {
                    mOnQueryTextFocusChangeListener.onFocusChange(android.support.v7.widget.SearchView.this, hasFocus);
                }
            }
        });
        setIconifiedByDefault(a.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
        final int maxWidth = a.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
        if (maxWidth != (-1)) {
            setMaxWidth(maxWidth);
        }
        mDefaultQueryHint = a.getText(R.styleable.SearchView_defaultQueryHint);
        mQueryHint = a.getText(R.styleable.SearchView_queryHint);
        final int imeOptions = a.getInt(R.styleable.SearchView_android_imeOptions, -1);
        if (imeOptions != (-1)) {
            setImeOptions(imeOptions);
        }
        final int inputType = a.getInt(R.styleable.SearchView_android_inputType, -1);
        if (inputType != (-1)) {
            setInputType(inputType);
        }
        boolean focusable = true;
        focusable = a.getBoolean(R.styleable.SearchView_android_focusable, focusable);
        setFocusable(focusable);
        a.recycle();
        // Save voice intent for later queries/launching
        mVoiceWebSearchIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_WEB_SEARCH);
        mVoiceWebSearchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        mVoiceWebSearchIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mVoiceAppSearchIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mVoiceAppSearchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        mDropDownAnchor = findViewById(mSearchSrcTextView.getDropDownAnchor());
        if (mDropDownAnchor != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                addOnLayoutChangeListenerToDropDownAnchorSDK11();
            } else {
                addOnLayoutChangeListenerToDropDownAnchorBase();
            }
        }
        updateViewsVisibility(mIconifiedByDefault);
        updateQueryHint();
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
        mDropDownAnchor.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() {
            @java.lang.Override
            public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                adjustDropDownSizeAndPosition();
            }
        });
    }

    private void addOnLayoutChangeListenerToDropDownAnchorBase() {
        mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
            @java.lang.Override
            public void onGlobalLayout() {
                adjustDropDownSizeAndPosition();
            }
        });
    }

    int getSuggestionRowLayout() {
        return mSuggestionRowLayout;
    }

    int getSuggestionCommitIconResId() {
        return mSuggestionCommitIconResId;
    }

    /**
     * Sets the SearchableInfo for this SearchView. Properties in the SearchableInfo are used
     * to display labels, hints, suggestions, create intents for launching search results screens
     * and controlling other affordances such as a voice button.
     *
     * @param searchable
     * 		a SearchableInfo can be retrieved from the SearchManager, for a specific
     * 		activity or a global search provider.
     */
    public void setSearchableInfo(android.app.SearchableInfo searchable) {
        mSearchable = searchable;
        if (mSearchable != null) {
            updateSearchAutoComplete();
            updateQueryHint();
        }
        // Cache the voice search capability
        mVoiceButtonEnabled = hasVoiceSearch();
        if (mVoiceButtonEnabled) {
            // Disable the microphone on the keyboard, as a mic is displayed near the text box
            // TODO: use imeOptions to disable voice input when the new API will be available
            mSearchSrcTextView.setPrivateImeOptions(android.support.v7.widget.SearchView.IME_OPTION_NO_MICROPHONE);
        }
        updateViewsVisibility(isIconified());
    }

    /**
     * Sets the APP_DATA for legacy SearchDialog use.
     *
     * @param appSearchData
     * 		bundle provided by the app when launching the search dialog
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setAppSearchData(android.os.Bundle appSearchData) {
        mAppSearchData = appSearchData;
    }

    /**
     * Sets the IME options on the query text field.
     *
     * @see TextView#setImeOptions(int)
     * @param imeOptions
     * 		the options to set on the query text field
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_android_imeOptions
     */
    public void setImeOptions(int imeOptions) {
        mSearchSrcTextView.setImeOptions(imeOptions);
    }

    /**
     * Returns the IME options set on the query text field.
     *
     * @return the ime options
     * @see TextView#setImeOptions(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_android_imeOptions
     */
    public int getImeOptions() {
        return mSearchSrcTextView.getImeOptions();
    }

    /**
     * Sets the input type on the query text field.
     *
     * @see TextView#setInputType(int)
     * @param inputType
     * 		the input type to set on the query text field
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_android_inputType
     */
    public void setInputType(int inputType) {
        mSearchSrcTextView.setInputType(inputType);
    }

    /**
     * Returns the input type set on the query text field.
     *
     * @return the input type
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_android_inputType
     */
    public int getInputType() {
        return mSearchSrcTextView.getInputType();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public boolean requestFocus(int direction, android.graphics.Rect previouslyFocusedRect) {
        // Don't accept focus if in the middle of clearing focus
        if (mClearingFocus)
            return false;

        // Check if SearchView is focusable.
        if (!isFocusable())
            return false;

        // If it is not iconified, then give the focus to the text field
        if (!isIconified()) {
            boolean result = mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
            if (result) {
                updateViewsVisibility(false);
            }
            return result;
        } else {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void clearFocus() {
        mClearingFocus = true;
        setImeVisibility(false);
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mClearingFocus = false;
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param listener
     * 		the listener object that receives callbacks when the user performs
     * 		actions in the SearchView such as clicking on buttons or typing a query.
     */
    public void setOnQueryTextListener(android.support.v7.widget.SearchView.OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    /**
     * Sets a listener to inform when the user closes the SearchView.
     *
     * @param listener
     * 		the listener to call when the user closes the SearchView.
     */
    public void setOnCloseListener(android.support.v7.widget.SearchView.OnCloseListener listener) {
        mOnCloseListener = listener;
    }

    /**
     * Sets a listener to inform when the focus of the query text field changes.
     *
     * @param listener
     * 		the listener to inform of focus changes.
     */
    public void setOnQueryTextFocusChangeListener(android.view.View.OnFocusChangeListener listener) {
        mOnQueryTextFocusChangeListener = listener;
    }

    /**
     * Sets a listener to inform when a suggestion is focused or clicked.
     *
     * @param listener
     * 		the listener to inform of suggestion selection events.
     */
    public void setOnSuggestionListener(android.support.v7.widget.SearchView.OnSuggestionListener listener) {
        mOnSuggestionListener = listener;
    }

    /**
     * Sets a listener to inform when the search button is pressed. This is only
     * relevant when the text field is not visible by default. Calling {@link #setIconified
     * setIconified(false)} can also cause this listener to be informed.
     *
     * @param listener
     * 		the listener to inform when the search button is clicked or
     * 		the text field is programmatically de-iconified.
     */
    public void setOnSearchClickListener(android.view.View.OnClickListener listener) {
        mOnSearchClickListener = listener;
    }

    /**
     * Returns the query string currently in the text field.
     *
     * @return the query string
     */
    public java.lang.CharSequence getQuery() {
        return mSearchSrcTextView.getText();
    }

    /**
     * Sets a query string in the text field and optionally submits the query as well.
     *
     * @param query
     * 		the query string. This replaces any query text already present in the
     * 		text field.
     * @param submit
     * 		whether to submit the query right now or only update the contents of
     * 		text field.
     */
    public void setQuery(java.lang.CharSequence query, boolean submit) {
        mSearchSrcTextView.setText(query);
        if (query != null) {
            mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
            mUserQuery = query;
        }
        // If the query is not empty and submit is requested, submit the query
        if (submit && (!android.text.TextUtils.isEmpty(query))) {
            onSubmitQuery();
        }
    }

    /**
     * Sets the hint text to display in the query text field. This overrides
     * any hint specified in the {@link SearchableInfo}.
     * <p>
     * This value may be specified as an empty string to prevent any query hint
     * from being displayed.
     *
     * @param hint
     * 		the hint text to display or {@code null} to clear
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_queryHint
     */
    public void setQueryHint(@android.support.annotation.Nullable
    java.lang.CharSequence hint) {
        mQueryHint = hint;
        updateQueryHint();
    }

    /**
     * Returns the hint text that will be displayed in the query text field.
     * <p>
     * The displayed query hint is chosen in the following order:
     * <ol>
     * <li>Non-null value set with {@link #setQueryHint(CharSequence)}
     * <li>Value specified in XML using {@code app:queryHint}
     * <li>Valid string resource ID exposed by the {@link SearchableInfo} via
     *     {@link SearchableInfo#getHintId()}
     * <li>Default hint provided by the theme against which the view was
     *     inflated
     * </ol>
     *
     * @return the displayed query hint text, or {@code null} if none set
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_queryHint
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getQueryHint() {
        final java.lang.CharSequence hint;
        if (mQueryHint != null) {
            hint = mQueryHint;
        } else
            if ((mSearchable != null) && (mSearchable.getHintId() != 0)) {
                hint = getContext().getText(mSearchable.getHintId());
            } else {
                hint = mDefaultQueryHint;
            }

        return hint;
    }

    /**
     * Sets the default or resting state of the search field. If true, a single search icon is
     * shown by default and expands to show the text field and other buttons when pressed. Also,
     * if the default state is iconified, then it collapses to that state when the close button
     * is pressed. Changes to this property will take effect immediately.
     *
     * <p>The default value is true.</p>
     *
     * @param iconified
     * 		whether the search field should be iconified by default
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_iconifiedByDefault
     */
    public void setIconifiedByDefault(boolean iconified) {
        if (mIconifiedByDefault == iconified)
            return;

        mIconifiedByDefault = iconified;
        updateViewsVisibility(iconified);
        updateQueryHint();
    }

    /**
     * Returns the default iconified state of the search field.
     *
     * @return 
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_iconifiedByDefault
     */
    public boolean isIconfiedByDefault() {
        return mIconifiedByDefault;
    }

    /**
     * Iconifies or expands the SearchView. Any query text is cleared when iconified. This is
     * a temporary state and does not override the default iconified state set by
     * {@link #setIconifiedByDefault(boolean)}. If the default state is iconified, then
     * a false here will only be valid until the user closes the field. And if the default
     * state is expanded, then a true here will only clear the text field and not close it.
     *
     * @param iconify
     * 		a true value will collapse the SearchView to an icon, while a false will
     * 		expand it.
     */
    public void setIconified(boolean iconify) {
        if (iconify) {
            onCloseClicked();
        } else {
            onSearchClicked();
        }
    }

    /**
     * Returns the current iconified state of the SearchView.
     *
     * @return true if the SearchView is currently iconified, false if the search field is
    fully visible.
     */
    public boolean isIconified() {
        return mIconified;
    }

    /**
     * Enables showing a submit button when the query is non-empty. In cases where the SearchView
     * is being used to filter the contents of the current activity and doesn't launch a separate
     * results activity, then the submit button should be disabled.
     *
     * @param enabled
     * 		true to show a submit button for submitting queries, false if a submit
     * 		button is not required.
     */
    public void setSubmitButtonEnabled(boolean enabled) {
        mSubmitButtonEnabled = enabled;
        updateViewsVisibility(isIconified());
    }

    /**
     * Returns whether the submit button is enabled when necessary or never displayed.
     *
     * @return whether the submit button is enabled automatically when necessary
     */
    public boolean isSubmitButtonEnabled() {
        return mSubmitButtonEnabled;
    }

    /**
     * Specifies if a query refinement button should be displayed alongside each suggestion
     * or if it should depend on the flags set in the individual items retrieved from the
     * suggestions provider. Clicking on the query refinement button will replace the text
     * in the query text field with the text from the suggestion. This flag only takes effect
     * if a SearchableInfo has been specified with {@link #setSearchableInfo(SearchableInfo)}
     * and not when using a custom adapter.
     *
     * @param enable
     * 		true if all items should have a query refinement button, false if only
     * 		those items that have a query refinement flag set should have the button.
     * @see SearchManager#SUGGEST_COLUMN_FLAGS
     * @see SearchManager#FLAG_QUERY_REFINEMENT
     */
    public void setQueryRefinementEnabled(boolean enable) {
        mQueryRefinement = enable;
        if (mSuggestionsAdapter instanceof android.support.v7.widget.SuggestionsAdapter) {
            ((android.support.v7.widget.SuggestionsAdapter) (mSuggestionsAdapter)).setQueryRefinement(enable ? android.support.v7.widget.SuggestionsAdapter.REFINE_ALL : android.support.v7.widget.SuggestionsAdapter.REFINE_BY_ENTRY);
        }
    }

    /**
     * Returns whether query refinement is enabled for all items or only specific ones.
     *
     * @return true if enabled for all items, false otherwise.
     */
    public boolean isQueryRefinementEnabled() {
        return mQueryRefinement;
    }

    /**
     * You can set a custom adapter if you wish. Otherwise the default adapter is used to
     * display the suggestions from the suggestions provider associated with the SearchableInfo.
     *
     * @see #setSearchableInfo(SearchableInfo)
     */
    public void setSuggestionsAdapter(android.support.v4.widget.CursorAdapter adapter) {
        mSuggestionsAdapter = adapter;
        mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
    }

    /**
     * Returns the adapter used for suggestions, if any.
     *
     * @return the suggestions adapter
     */
    public android.support.v4.widget.CursorAdapter getSuggestionsAdapter() {
        return mSuggestionsAdapter;
    }

    /**
     * Makes the view at most this many pixels wide
     *
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_android_maxWidth
     */
    public void setMaxWidth(int maxpixels) {
        mMaxWidth = maxpixels;
        requestLayout();
    }

    /**
     * Gets the specified maximum width in pixels, if set. Returns zero if
     * no maximum width was specified.
     *
     * @return the maximum width of the view
     * @unknown ref android.support.v7.appcompat.R.styleable#SearchView_android_maxWidth
     */
    public int getMaxWidth() {
        return mMaxWidth;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Let the standard measurements take effect in iconified state.
        if (isIconified()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int width = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode) {
            case android.view.View.MeasureSpec.AT_MOST :
                // If there is an upper limit, don't exceed maximum width (explicit or implicit)
                if (mMaxWidth > 0) {
                    width = java.lang.Math.min(mMaxWidth, width);
                } else {
                    width = java.lang.Math.min(getPreferredWidth(), width);
                }
                break;
            case android.view.View.MeasureSpec.EXACTLY :
                // If an exact width is specified, still don't exceed any specified maximum width
                if (mMaxWidth > 0) {
                    width = java.lang.Math.min(mMaxWidth, width);
                }
                break;
            case android.view.View.MeasureSpec.UNSPECIFIED :
                // Use maximum width, if specified, else preferred width
                width = (mMaxWidth > 0) ? mMaxWidth : getPreferredWidth();
                break;
        }
        widthMode = android.view.View.MeasureSpec.EXACTLY;
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int height = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode) {
            case android.view.View.MeasureSpec.AT_MOST :
                height = java.lang.Math.min(getPreferredHeight(), height);
                break;
            case android.view.View.MeasureSpec.UNSPECIFIED :
                height = getPreferredHeight();
                break;
        }
        heightMode = android.view.View.MeasureSpec.EXACTLY;
        super.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(width, widthMode), android.view.View.MeasureSpec.makeMeasureSpec(height, heightMode));
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            // Expand mSearchSrcTextView touch target to be the height of the parent in order to
            // allow it to be up to 48dp.
            getChildBoundsWithinSearchView(mSearchSrcTextView, mSearchSrcTextViewBounds);
            mSearchSrtTextViewBoundsExpanded.set(mSearchSrcTextViewBounds.left, 0, mSearchSrcTextViewBounds.right, bottom - top);
            if (mTouchDelegate == null) {
                mTouchDelegate = new android.support.v7.widget.SearchView.UpdatableTouchDelegate(mSearchSrtTextViewBoundsExpanded, mSearchSrcTextViewBounds, mSearchSrcTextView);
                setTouchDelegate(mTouchDelegate);
            } else {
                mTouchDelegate.setBounds(mSearchSrtTextViewBoundsExpanded, mSearchSrcTextViewBounds);
            }
        }
    }

    private void getChildBoundsWithinSearchView(android.view.View view, android.graphics.Rect rect) {
        view.getLocationInWindow(mTemp);
        getLocationInWindow(mTemp2);
        final int top = mTemp[1] - mTemp2[1];
        final int left = mTemp[0] - mTemp2[0];
        rect.set(left, top, left + view.getWidth(), top + view.getHeight());
    }

    private int getPreferredWidth() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
    }

    private void updateViewsVisibility(final boolean collapsed) {
        mIconified = collapsed;
        // Visibility of views that are visible when collapsed
        final int visCollapsed = (collapsed) ? android.view.View.VISIBLE : android.view.View.GONE;
        // Is there text in the query
        final boolean hasText = !android.text.TextUtils.isEmpty(mSearchSrcTextView.getText());
        mSearchButton.setVisibility(visCollapsed);
        updateSubmitButton(hasText);
        mSearchEditFrame.setVisibility(collapsed ? android.view.View.GONE : android.view.View.VISIBLE);
        final int iconVisibility;
        if ((mCollapsedIcon.getDrawable() == null) || mIconifiedByDefault) {
            iconVisibility = android.view.View.GONE;
        } else {
            iconVisibility = android.view.View.VISIBLE;
        }
        mCollapsedIcon.setVisibility(iconVisibility);
        updateCloseButton();
        updateVoiceButton(!hasText);
        updateSubmitArea();
    }

    private boolean hasVoiceSearch() {
        if ((mSearchable != null) && mSearchable.getVoiceSearchEnabled()) {
            android.content.Intent testIntent = null;
            if (mSearchable.getVoiceSearchLaunchWebSearch()) {
                testIntent = mVoiceWebSearchIntent;
            } else
                if (mSearchable.getVoiceSearchLaunchRecognizer()) {
                    testIntent = mVoiceAppSearchIntent;
                }

            if (testIntent != null) {
                android.content.pm.ResolveInfo ri = getContext().getPackageManager().resolveActivity(testIntent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
                return ri != null;
            }
        }
        return false;
    }

    private boolean isSubmitAreaEnabled() {
        return (mSubmitButtonEnabled || mVoiceButtonEnabled) && (!isIconified());
    }

    private void updateSubmitButton(boolean hasText) {
        int visibility = android.view.View.GONE;
        if (((mSubmitButtonEnabled && isSubmitAreaEnabled()) && hasFocus()) && (hasText || (!mVoiceButtonEnabled))) {
            visibility = android.view.View.VISIBLE;
        }
        mGoButton.setVisibility(visibility);
    }

    private void updateSubmitArea() {
        int visibility = android.view.View.GONE;
        if (isSubmitAreaEnabled() && ((mGoButton.getVisibility() == android.view.View.VISIBLE) || (mVoiceButton.getVisibility() == android.view.View.VISIBLE))) {
            visibility = android.view.View.VISIBLE;
        }
        mSubmitArea.setVisibility(visibility);
    }

    private void updateCloseButton() {
        final boolean hasText = !android.text.TextUtils.isEmpty(mSearchSrcTextView.getText());
        // Should we show the close button? It is not shown if there's no focus,
        // field is not iconified by default and there is no text in it.
        final boolean showClose = hasText || (mIconifiedByDefault && (!mExpandedInActionView));
        mCloseButton.setVisibility(showClose ? android.view.View.VISIBLE : android.view.View.GONE);
        final android.graphics.drawable.Drawable closeButtonImg = mCloseButton.getDrawable();
        if (closeButtonImg != null) {
            closeButtonImg.setState(hasText ? android.view.View.ENABLED_STATE_SET : android.view.View.EMPTY_STATE_SET);
        }
    }

    private void postUpdateFocusedState() {
        post(mUpdateDrawableStateRunnable);
    }

    void updateFocusedState() {
        final boolean focused = mSearchSrcTextView.hasFocus();
        final int[] stateSet = (focused) ? android.view.View.FOCUSED_STATE_SET : android.view.View.EMPTY_STATE_SET;
        final android.graphics.drawable.Drawable searchPlateBg = mSearchPlate.getBackground();
        if (searchPlateBg != null) {
            searchPlateBg.setState(stateSet);
        }
        final android.graphics.drawable.Drawable submitAreaBg = mSubmitArea.getBackground();
        if (submitAreaBg != null) {
            submitAreaBg.setState(stateSet);
        }
        invalidate();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        removeCallbacks(mUpdateDrawableStateRunnable);
        post(mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }

    void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            android.view.inputmethod.InputMethodManager imm = ((android.view.inputmethod.InputMethodManager) (getContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)));
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    /**
     * Called by the SuggestionsAdapter
     */
    void onQueryRefine(java.lang.CharSequence queryText) {
        setQuery(queryText);
    }

    private final android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            if (v == mSearchButton) {
                onSearchClicked();
            } else
                if (v == mCloseButton) {
                    onCloseClicked();
                } else
                    if (v == mGoButton) {
                        onSubmitQuery();
                    } else
                        if (v == mVoiceButton) {
                            onVoiceClicked();
                        } else
                            if (v == mSearchSrcTextView) {
                                forceSuggestionQuery();
                            }




        }
    };

    /**
     * React to the user typing "enter" or other hardwired keys while typing in
     * the search box. This handles these special keys while the edit box has
     * focus.
     */
    android.view.View.OnKeyListener mTextKeyListener = new android.view.View.OnKeyListener() {
        @java.lang.Override
        public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
            // guard against possible race conditions
            if (mSearchable == null) {
                return false;
            }
            if (android.support.v7.widget.SearchView.DBG) {
                android.util.Log.d(android.support.v7.widget.SearchView.LOG_TAG, (((("mTextListener.onKey(" + keyCode) + ",") + event) + "), selection: ") + mSearchSrcTextView.getListSelection());
            }
            // If a suggestion is selected, handle enter, search key, and action keys
            // as presses on the selected suggestion
            if (mSearchSrcTextView.isPopupShowing() && (mSearchSrcTextView.getListSelection() != android.widget.ListView.INVALID_POSITION)) {
                return onSuggestionsKey(v, keyCode, event);
            }
            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if ((!mSearchSrcTextView.isEmpty()) && android.support.v4.view.KeyEventCompat.hasNoModifiers(event)) {
                if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                    if (keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();
                        // Launch as a regular search.
                        launchQuerySearch(android.view.KeyEvent.KEYCODE_UNKNOWN, null, mSearchSrcTextView.getText().toString());
                        return true;
                    }
                }
            }
            return false;
        }
    };

    /**
     * React to the user typing while in the suggestions list. First, check for
     * action keys. If not handled, try refocusing regular characters into the
     * EditText.
     */
    boolean onSuggestionsKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        // guard against possible race conditions (late arrival after dismiss)
        if (mSearchable == null) {
            return false;
        }
        if (mSuggestionsAdapter == null) {
            return false;
        }
        if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && android.support.v4.view.KeyEventCompat.hasNoModifiers(event)) {
            // First, check for enter or search (both of which we'll treat as a
            // "click")
            if (((keyCode == android.view.KeyEvent.KEYCODE_ENTER) || (keyCode == android.view.KeyEvent.KEYCODE_SEARCH)) || (keyCode == android.view.KeyEvent.KEYCODE_TAB)) {
                int position = mSearchSrcTextView.getListSelection();
                return onItemClicked(position, android.view.KeyEvent.KEYCODE_UNKNOWN, null);
            }
            // Next, check for left/right moves, which we use to "return" the
            // user to the edit view
            if ((keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT)) {
                // give "focus" to text editor, with cursor at the beginning if
                // left key, at end if right key
                // TODO: Reverse left/right for right-to-left languages, e.g.
                // Arabic
                int selPoint = (keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) ? 0 : mSearchSrcTextView.length();
                mSearchSrcTextView.setSelection(selPoint);
                mSearchSrcTextView.setListSelection(0);
                mSearchSrcTextView.clearListSelection();
                android.support.v7.widget.SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(mSearchSrcTextView, true);
                return true;
            }
            // Next, check for an "up and out" move
            if ((keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) && (0 == mSearchSrcTextView.getListSelection())) {
                // TODO: restoreUserQuery();
                // let ACTV complete the move
                return false;
            }
        }
        return false;
    }

    private java.lang.CharSequence getDecoratedHint(java.lang.CharSequence hintText) {
        // If the field is always expanded or we don't have a search hint icon,
        // then don't add the search icon to the hint.
        if ((!mIconifiedByDefault) || (mSearchHintIcon == null)) {
            return hintText;
        }
        final int textSize = ((int) (mSearchSrcTextView.getTextSize() * 1.25));
        mSearchHintIcon.setBounds(0, 0, textSize, textSize);
        final android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder("   ");
        ssb.setSpan(new android.text.style.ImageSpan(mSearchHintIcon), 1, 2, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(hintText);
        return ssb;
    }

    private void updateQueryHint() {
        final java.lang.CharSequence hint = getQueryHint();
        mSearchSrcTextView.setHint(getDecoratedHint(hint == null ? "" : hint));
    }

    /**
     * Updates the auto-complete text view.
     */
    private void updateSearchAutoComplete() {
        mSearchSrcTextView.setThreshold(mSearchable.getSuggestThreshold());
        mSearchSrcTextView.setImeOptions(mSearchable.getImeOptions());
        int inputType = mSearchable.getInputType();
        // We only touch this if the input type is set up for text (which it almost certainly
        // should be, in the case of search!)
        if ((inputType & android.text.InputType.TYPE_MASK_CLASS) == android.text.InputType.TYPE_CLASS_TEXT) {
            // The existence of a suggestions authority is the proxy for "suggestions
            // are available here"
            inputType &= ~android.text.InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
            if (mSearchable.getSuggestAuthority() != null) {
                inputType |= android.text.InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
                // TYPE_TEXT_FLAG_AUTO_COMPLETE means that the text editor is performing
                // auto-completion based on its own semantics, which it will present to the user
                // as they type. This generally means that the input method should not show its
                // own candidates, and the spell checker should not be in action. The text editor
                // supplies its candidates by calling InputMethodManager.displayCompletions(),
                // which in turn will call InputMethodSession.displayCompletions().
                inputType |= android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
            }
        }
        mSearchSrcTextView.setInputType(inputType);
        if (mSuggestionsAdapter != null) {
            mSuggestionsAdapter.changeCursor(null);
        }
        // attach the suggestions adapter, if suggestions are available
        // The existence of a suggestions authority is the proxy for "suggestions available here"
        if (mSearchable.getSuggestAuthority() != null) {
            mSuggestionsAdapter = new android.support.v7.widget.SuggestionsAdapter(getContext(), this, mSearchable, mOutsideDrawablesCache);
            mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
            ((android.support.v7.widget.SuggestionsAdapter) (mSuggestionsAdapter)).setQueryRefinement(mQueryRefinement ? android.support.v7.widget.SuggestionsAdapter.REFINE_ALL : android.support.v7.widget.SuggestionsAdapter.REFINE_BY_ENTRY);
        }
    }

    /**
     * Update the visibility of the voice button.  There are actually two voice search modes,
     * either of which will activate the button.
     *
     * @param empty
     * 		whether the search query text field is empty. If it is, then the other
     * 		criteria apply to make the voice button visible.
     */
    private void updateVoiceButton(boolean empty) {
        int visibility = android.view.View.GONE;
        if ((mVoiceButtonEnabled && (!isIconified())) && empty) {
            visibility = android.view.View.VISIBLE;
            mGoButton.setVisibility(android.view.View.GONE);
        }
        mVoiceButton.setVisibility(visibility);
    }

    private final android.widget.TextView.OnEditorActionListener mOnEditorActionListener = new android.widget.TextView.OnEditorActionListener() {
        /**
         * Called when the input method default action key is pressed.
         */
        @java.lang.Override
        public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
            onSubmitQuery();
            return true;
        }
    };

    void onTextChanged(java.lang.CharSequence newText) {
        java.lang.CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = text;
        boolean hasText = !android.text.TextUtils.isEmpty(text);
        updateSubmitButton(hasText);
        updateVoiceButton(!hasText);
        updateCloseButton();
        updateSubmitArea();
        if ((mOnQueryChangeListener != null) && (!android.text.TextUtils.equals(newText, mOldQueryText))) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    void onSubmitQuery() {
        java.lang.CharSequence query = mSearchSrcTextView.getText();
        if ((query != null) && (android.text.TextUtils.getTrimmedLength(query) > 0)) {
            if ((mOnQueryChangeListener == null) || (!mOnQueryChangeListener.onQueryTextSubmit(query.toString()))) {
                if (mSearchable != null) {
                    launchQuerySearch(android.view.KeyEvent.KEYCODE_UNKNOWN, null, query.toString());
                }
                setImeVisibility(false);
                dismissSuggestions();
            }
        }
    }

    private void dismissSuggestions() {
        mSearchSrcTextView.dismissDropDown();
    }

    void onCloseClicked() {
        java.lang.CharSequence text = mSearchSrcTextView.getText();
        if (android.text.TextUtils.isEmpty(text)) {
            if (mIconifiedByDefault) {
                // If the app doesn't override the close behavior
                if ((mOnCloseListener == null) || (!mOnCloseListener.onClose())) {
                    // hide the keyboard and remove focus
                    clearFocus();
                    // collapse the search field
                    updateViewsVisibility(true);
                }
            }
        } else {
            mSearchSrcTextView.setText("");
            mSearchSrcTextView.requestFocus();
            setImeVisibility(true);
        }
    }

    void onSearchClicked() {
        updateViewsVisibility(false);
        mSearchSrcTextView.requestFocus();
        setImeVisibility(true);
        if (mOnSearchClickListener != null) {
            mOnSearchClickListener.onClick(this);
        }
    }

    void onVoiceClicked() {
        // guard against possible race conditions
        if (mSearchable == null) {
            return;
        }
        android.app.SearchableInfo searchable = mSearchable;
        try {
            if (searchable.getVoiceSearchLaunchWebSearch()) {
                android.content.Intent webSearchIntent = createVoiceWebSearchIntent(mVoiceWebSearchIntent, searchable);
                getContext().startActivity(webSearchIntent);
            } else
                if (searchable.getVoiceSearchLaunchRecognizer()) {
                    android.content.Intent appSearchIntent = createVoiceAppSearchIntent(mVoiceAppSearchIntent, searchable);
                    getContext().startActivity(appSearchIntent);
                }

        } catch (android.content.ActivityNotFoundException e) {
            // Should not happen, since we check the availability of
            // voice search before showing the button. But just in case...
            android.util.Log.w(android.support.v7.widget.SearchView.LOG_TAG, "Could not find voice search activity");
        }
    }

    void onTextFocusChanged() {
        updateViewsVisibility(isIconified());
        // Delayed update to make sure that the focus has settled down and window focus changes
        // don't affect it. A synchronous update was not working.
        postUpdateFocusedState();
        if (mSearchSrcTextView.hasFocus()) {
            forceSuggestionQuery();
        }
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        postUpdateFocusedState();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void onActionViewCollapsed() {
        setQuery("", false);
        clearFocus();
        updateViewsVisibility(true);
        mSearchSrcTextView.setImeOptions(mCollapsedImeOptions);
        mExpandedInActionView = false;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void onActionViewExpanded() {
        if (mExpandedInActionView)
            return;

        mExpandedInActionView = true;
        mCollapsedImeOptions = mSearchSrcTextView.getImeOptions();
        mSearchSrcTextView.setImeOptions(mCollapsedImeOptions | android.view.inputmethod.EditorInfo.IME_FLAG_NO_FULLSCREEN);
        mSearchSrcTextView.setText("");
        setIconified(false);
    }

    static class SavedState extends android.support.v4.view.AbsSavedState {
        boolean isIconified;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        public SavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
            super(source, loader);
            isIconified = ((java.lang.Boolean) (source.readValue(null)));
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeValue(isIconified);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("SearchView.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " isIconified=") + isIconified) + "}";
        }

        public static final android.os.Parcelable.Creator<android.support.v7.widget.SearchView.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.v7.widget.SearchView.SavedState>() {
            @java.lang.Override
            public android.support.v7.widget.SearchView.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.v7.widget.SearchView.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.v7.widget.SearchView.SavedState[] newArray(int size) {
                return new android.support.v7.widget.SearchView.SavedState[size];
            }
        });
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.support.v7.widget.SearchView.SavedState ss = new android.support.v7.widget.SearchView.SavedState(superState);
        ss.isIconified = isIconified();
        return ss;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v7.widget.SearchView.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v7.widget.SearchView.SavedState ss = ((android.support.v7.widget.SearchView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        updateViewsVisibility(ss.isIconified);
        requestLayout();
    }

    void adjustDropDownSizeAndPosition() {
        if (mDropDownAnchor.getWidth() > 1) {
            android.content.res.Resources res = getContext().getResources();
            int anchorPadding = mSearchPlate.getPaddingLeft();
            android.graphics.Rect dropDownPadding = new android.graphics.Rect();
            final boolean isLayoutRtl = android.support.v7.widget.ViewUtils.isLayoutRtl(this);
            int iconOffset = (mIconifiedByDefault) ? res.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + res.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left) : 0;
            mSearchSrcTextView.getDropDownBackground().getPadding(dropDownPadding);
            int offset;
            if (isLayoutRtl) {
                offset = -dropDownPadding.left;
            } else {
                offset = anchorPadding - (dropDownPadding.left + iconOffset);
            }
            mSearchSrcTextView.setDropDownHorizontalOffset(offset);
            final int width = (((mDropDownAnchor.getWidth() + dropDownPadding.left) + dropDownPadding.right) + iconOffset) - anchorPadding;
            mSearchSrcTextView.setDropDownWidth(width);
        }
    }

    boolean onItemClicked(int position, int actionKey, java.lang.String actionMsg) {
        if ((mOnSuggestionListener == null) || (!mOnSuggestionListener.onSuggestionClick(position))) {
            launchSuggestion(position, android.view.KeyEvent.KEYCODE_UNKNOWN, null);
            setImeVisibility(false);
            dismissSuggestions();
            return true;
        }
        return false;
    }

    boolean onItemSelected(int position) {
        if ((mOnSuggestionListener == null) || (!mOnSuggestionListener.onSuggestionSelect(position))) {
            rewriteQueryFromSuggestion(position);
            return true;
        }
        return false;
    }

    private final android.widget.AdapterView.OnItemClickListener mOnItemClickListener = new android.widget.AdapterView.OnItemClickListener() {
        /**
         * Implements OnItemClickListener
         */
        @java.lang.Override
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            if (android.support.v7.widget.SearchView.DBG)
                android.util.Log.d(android.support.v7.widget.SearchView.LOG_TAG, "onItemClick() position " + position);

            onItemClicked(position, android.view.KeyEvent.KEYCODE_UNKNOWN, null);
        }
    };

    private final android.widget.AdapterView.OnItemSelectedListener mOnItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {
        /**
         * Implements OnItemSelectedListener
         */
        @java.lang.Override
        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            if (android.support.v7.widget.SearchView.DBG)
                android.util.Log.d(android.support.v7.widget.SearchView.LOG_TAG, "onItemSelected() position " + position);

            android.support.v7.widget.SearchView.this.onItemSelected(position);
        }

        /**
         * Implements OnItemSelectedListener
         */
        @java.lang.Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            if (android.support.v7.widget.SearchView.DBG)
                android.util.Log.d(android.support.v7.widget.SearchView.LOG_TAG, "onNothingSelected()");

        }
    };

    /**
     * Query rewriting.
     */
    private void rewriteQueryFromSuggestion(int position) {
        java.lang.CharSequence oldQuery = mSearchSrcTextView.getText();
        android.database.Cursor c = mSuggestionsAdapter.getCursor();
        if (c == null) {
            return;
        }
        if (c.moveToPosition(position)) {
            // Get the new query from the suggestion.
            java.lang.CharSequence newQuery = mSuggestionsAdapter.convertToString(c);
            if (newQuery != null) {
                // The suggestion rewrites the query.
                // Update the text field, without getting new suggestions.
                setQuery(newQuery);
            } else {
                // The suggestion does not rewrite the query, restore the user's query.
                setQuery(oldQuery);
            }
        } else {
            // We got a bad position, restore the user's query.
            setQuery(oldQuery);
        }
    }

    /**
     * Launches an intent based on a suggestion.
     *
     * @param position
     * 		The index of the suggestion to create the intent from.
     * @param actionKey
     * 		The key code of the action key that was pressed,
     * 		or {@link KeyEvent#KEYCODE_UNKNOWN} if none.
     * @param actionMsg
     * 		The message for the action key that was pressed,
     * 		or <code>null</code> if none.
     * @return true if a successful launch, false if could not (e.g. bad position).
     */
    private boolean launchSuggestion(int position, int actionKey, java.lang.String actionMsg) {
        android.database.Cursor c = mSuggestionsAdapter.getCursor();
        if ((c != null) && c.moveToPosition(position)) {
            android.content.Intent intent = createIntentFromSuggestion(c, actionKey, actionMsg);
            // launch the intent
            launchIntent(intent);
            return true;
        }
        return false;
    }

    /**
     * Launches an intent, including any special intent handling.
     */
    private void launchIntent(android.content.Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            // If the intent was created from a suggestion, it will always have an explicit
            // component here.
            getContext().startActivity(intent);
        } catch (java.lang.RuntimeException ex) {
            android.util.Log.e(android.support.v7.widget.SearchView.LOG_TAG, "Failed launch activity: " + intent, ex);
        }
    }

    /**
     * Sets the text in the query box, without updating the suggestions.
     */
    private void setQuery(java.lang.CharSequence query) {
        mSearchSrcTextView.setText(query);
        // Move the cursor to the end
        mSearchSrcTextView.setSelection(android.text.TextUtils.isEmpty(query) ? 0 : query.length());
    }

    void launchQuerySearch(int actionKey, java.lang.String actionMsg, java.lang.String query) {
        java.lang.String action = android.content.Intent.ACTION_SEARCH;
        android.content.Intent intent = createIntent(action, null, null, query, actionKey, actionMsg);
        getContext().startActivity(intent);
    }

    /**
     * Constructs an intent from the given information and the search dialog state.
     *
     * @param action
     * 		Intent action.
     * @param data
     * 		Intent data, or <code>null</code>.
     * @param extraData
     * 		Data for {@link SearchManager#EXTRA_DATA_KEY} or <code>null</code>.
     * @param query
     * 		Intent query, or <code>null</code>.
     * @param actionKey
     * 		The key code of the action key that was pressed,
     * 		or {@link KeyEvent#KEYCODE_UNKNOWN} if none.
     * @param actionMsg
     * 		The message for the action key that was pressed,
     * 		or <code>null</code> if none.
     * @return The intent.
     */
    private android.content.Intent createIntent(java.lang.String action, android.net.Uri data, java.lang.String extraData, java.lang.String query, int actionKey, java.lang.String actionMsg) {
        // Now build the Intent
        android.content.Intent intent = new android.content.Intent(action);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        // We need CLEAR_TOP to avoid reusing an old task that has other activities
        // on top of the one we want. We don't want to do this in in-app search though,
        // as it can be destructive to the activity stack.
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra(android.app.SearchManager.USER_QUERY, mUserQuery);
        if (query != null) {
            intent.putExtra(android.app.SearchManager.QUERY, query);
        }
        if (extraData != null) {
            intent.putExtra(android.app.SearchManager.EXTRA_DATA_KEY, extraData);
        }
        if (mAppSearchData != null) {
            intent.putExtra(android.app.SearchManager.APP_DATA, mAppSearchData);
        }
        if (actionKey != android.view.KeyEvent.KEYCODE_UNKNOWN) {
            intent.putExtra(android.app.SearchManager.ACTION_KEY, actionKey);
            intent.putExtra(android.app.SearchManager.ACTION_MSG, actionMsg);
        }
        intent.setComponent(mSearchable.getSearchActivity());
        return intent;
    }

    /**
     * Create and return an Intent that can launch the voice search activity for web search.
     */
    private android.content.Intent createVoiceWebSearchIntent(android.content.Intent baseIntent, android.app.SearchableInfo searchable) {
        android.content.Intent voiceIntent = new android.content.Intent(baseIntent);
        android.content.ComponentName searchActivity = searchable.getSearchActivity();
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_CALLING_PACKAGE, searchActivity == null ? null : searchActivity.flattenToShortString());
        return voiceIntent;
    }

    /**
     * Create and return an Intent that can launch the voice search activity, perform a specific
     * voice transcription, and forward the results to the searchable activity.
     *
     * @param baseIntent
     * 		The voice app search intent to start from
     * @return A completely-configured intent ready to send to the voice search activity
     */
    private android.content.Intent createVoiceAppSearchIntent(android.content.Intent baseIntent, android.app.SearchableInfo searchable) {
        android.content.ComponentName searchActivity = searchable.getSearchActivity();
        // create the necessary intent to set up a search-and-forward operation
        // in the voice search system.   We have to keep the bundle separate,
        // because it becomes immutable once it enters the PendingIntent
        android.content.Intent queryIntent = new android.content.Intent(android.content.Intent.ACTION_SEARCH);
        queryIntent.setComponent(searchActivity);
        android.app.PendingIntent pending = android.app.PendingIntent.getActivity(getContext(), 0, queryIntent, android.app.PendingIntent.FLAG_ONE_SHOT);
        // Now set up the bundle that will be inserted into the pending intent
        // when it's time to do the search.  We always build it here (even if empty)
        // because the voice search activity will always need to insert "QUERY" into
        // it anyway.
        android.os.Bundle queryExtras = new android.os.Bundle();
        if (mAppSearchData != null) {
            queryExtras.putParcelable(android.app.SearchManager.APP_DATA, mAppSearchData);
        }
        // Now build the intent to launch the voice search.  Add all necessary
        // extras to launch the voice recognizer, and then all the necessary extras
        // to forward the results to the searchable activity
        android.content.Intent voiceIntent = new android.content.Intent(baseIntent);
        // Add all of the configuration options supplied by the searchable's metadata
        java.lang.String languageModel = android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
        java.lang.String prompt = null;
        java.lang.String language = null;
        int maxResults = 1;
        android.content.res.Resources resources = getResources();
        if (searchable.getVoiceLanguageModeId() != 0) {
            languageModel = resources.getString(searchable.getVoiceLanguageModeId());
        }
        if (searchable.getVoicePromptTextId() != 0) {
            prompt = resources.getString(searchable.getVoicePromptTextId());
        }
        if (searchable.getVoiceLanguageId() != 0) {
            language = resources.getString(searchable.getVoiceLanguageId());
        }
        if (searchable.getVoiceMaxResults() != 0) {
            maxResults = searchable.getVoiceMaxResults();
        }
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, prompt);
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, language);
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_CALLING_PACKAGE, searchActivity == null ? null : searchActivity.flattenToShortString());
        // Add the values that configure forwarding the results
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, pending);
        voiceIntent.putExtra(android.speech.RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT_BUNDLE, queryExtras);
        return voiceIntent;
    }

    /**
     * When a particular suggestion has been selected, perform the various lookups required
     * to use the suggestion.  This includes checking the cursor for suggestion-specific data,
     * and/or falling back to the XML for defaults;  It also creates REST style Uri data when
     * the suggestion includes a data id.
     *
     * @param c
     * 		The suggestions cursor, moved to the row of the user's selection
     * @param actionKey
     * 		The key code of the action key that was pressed,
     * 		or {@link KeyEvent#KEYCODE_UNKNOWN} if none.
     * @param actionMsg
     * 		The message for the action key that was pressed,
     * 		or <code>null</code> if none.
     * @return An intent for the suggestion at the cursor's position.
     */
    private android.content.Intent createIntentFromSuggestion(android.database.Cursor c, int actionKey, java.lang.String actionMsg) {
        try {
            // use specific action if supplied, or default action if supplied, or fixed default
            java.lang.String action = android.support.v7.widget.SuggestionsAdapter.getColumnString(c, android.app.SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
            if (action == null) {
                action = mSearchable.getSuggestIntentAction();
            }
            if (action == null) {
                action = android.content.Intent.ACTION_SEARCH;
            }
            // use specific data if supplied, or default data if supplied
            java.lang.String data = android.support.v7.widget.SuggestionsAdapter.getColumnString(c, android.app.SearchManager.SUGGEST_COLUMN_INTENT_DATA);
            if (data == null) {
                data = mSearchable.getSuggestIntentData();
            }
            // then, if an ID was provided, append it.
            if (data != null) {
                java.lang.String id = android.support.v7.widget.SuggestionsAdapter.getColumnString(c, android.app.SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
                if (id != null) {
                    data = (data + "/") + android.net.Uri.encode(id);
                }
            }
            android.net.Uri dataUri = (data == null) ? null : android.net.Uri.parse(data);
            java.lang.String query = android.support.v7.widget.SuggestionsAdapter.getColumnString(c, android.app.SearchManager.SUGGEST_COLUMN_QUERY);
            java.lang.String extraData = android.support.v7.widget.SuggestionsAdapter.getColumnString(c, android.app.SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
            return createIntent(action, dataUri, extraData, query, actionKey, actionMsg);
        } catch (java.lang.RuntimeException e) {
            int rowNum;
            try {
                // be really paranoid now
                rowNum = c.getPosition();
            } catch (java.lang.RuntimeException e2) {
                rowNum = -1;
            }
            android.util.Log.w(android.support.v7.widget.SearchView.LOG_TAG, ("Search suggestions cursor at row " + rowNum) + " returned exception.", e);
            return null;
        }
    }

    void forceSuggestionQuery() {
        android.support.v7.widget.SearchView.HIDDEN_METHOD_INVOKER.doBeforeTextChanged(mSearchSrcTextView);
        android.support.v7.widget.SearchView.HIDDEN_METHOD_INVOKER.doAfterTextChanged(mSearchSrcTextView);
    }

    static boolean isLandscapeMode(android.content.Context context) {
        return context.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Callback to watch the text field for empty/non-empty
     */
    private android.text.TextWatcher mTextWatcher = new android.text.TextWatcher() {
        @java.lang.Override
        public void beforeTextChanged(java.lang.CharSequence s, int start, int before, int after) {
        }

        @java.lang.Override
        public void onTextChanged(java.lang.CharSequence s, int start, int before, int after) {
            android.support.v7.widget.SearchView.this.onTextChanged(s);
        }

        @java.lang.Override
        public void afterTextChanged(android.text.Editable s) {
        }
    };

    private static class UpdatableTouchDelegate extends android.view.TouchDelegate {
        /**
         * View that should receive forwarded touch events
         */
        private final android.view.View mDelegateView;

        /**
         * Bounds in local coordinates of the containing view that should be mapped to the delegate
         * view. This rect is used for initial hit testing.
         */
        private final android.graphics.Rect mTargetBounds;

        /**
         * Bounds in local coordinates of the containing view that are actual bounds of the delegate
         * view. This rect is used for event coordinate mapping.
         */
        private final android.graphics.Rect mActualBounds;

        /**
         * mTargetBounds inflated to include some slop. This rect is to track whether the motion events
         * should be considered to be be within the delegate view.
         */
        private final android.graphics.Rect mSlopBounds;

        private final int mSlop;

        /**
         * True if the delegate had been targeted on a down event (intersected mTargetBounds).
         */
        private boolean mDelegateTargeted;

        public UpdatableTouchDelegate(android.graphics.Rect targetBounds, android.graphics.Rect actualBounds, android.view.View delegateView) {
            super(targetBounds, delegateView);
            mSlop = android.view.ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
            mTargetBounds = new android.graphics.Rect();
            mSlopBounds = new android.graphics.Rect();
            mActualBounds = new android.graphics.Rect();
            setBounds(targetBounds, actualBounds);
            mDelegateView = delegateView;
        }

        public void setBounds(android.graphics.Rect desiredBounds, android.graphics.Rect actualBounds) {
            mTargetBounds.set(desiredBounds);
            mSlopBounds.set(desiredBounds);
            mSlopBounds.inset(-mSlop, -mSlop);
            mActualBounds.set(actualBounds);
        }

        @java.lang.Override
        public boolean onTouchEvent(android.view.MotionEvent event) {
            final int x = ((int) (event.getX()));
            final int y = ((int) (event.getY()));
            boolean sendToDelegate = false;
            boolean hit = true;
            boolean handled = false;
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN :
                    if (mTargetBounds.contains(x, y)) {
                        mDelegateTargeted = true;
                        sendToDelegate = true;
                    }
                    break;
                case android.view.MotionEvent.ACTION_UP :
                case android.view.MotionEvent.ACTION_MOVE :
                    sendToDelegate = mDelegateTargeted;
                    if (sendToDelegate) {
                        if (!mSlopBounds.contains(x, y)) {
                            hit = false;
                        }
                    }
                    break;
                case android.view.MotionEvent.ACTION_CANCEL :
                    sendToDelegate = mDelegateTargeted;
                    mDelegateTargeted = false;
                    break;
            }
            if (sendToDelegate) {
                if (hit && (!mActualBounds.contains(x, y))) {
                    // Offset event coordinates to be in the center of the target view since we
                    // are within the targetBounds, but not inside the actual bounds of
                    // mDelegateView
                    event.setLocation(mDelegateView.getWidth() / 2, mDelegateView.getHeight() / 2);
                } else {
                    // Offset event coordinates to the target view coordinates.
                    event.setLocation(x - mActualBounds.left, y - mActualBounds.top);
                }
                handled = mDelegateView.dispatchTouchEvent(event);
            }
            return handled;
        }
    }

    /**
     * Local subclass for AutoCompleteTextView.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class SearchAutoComplete extends android.support.v7.widget.AppCompatAutoCompleteTextView {
        private int mThreshold;

        private android.support.v7.widget.SearchView mSearchView;

        public SearchAutoComplete(android.content.Context context) {
            this(context, null);
        }

        public SearchAutoComplete(android.content.Context context, android.util.AttributeSet attrs) {
            this(context, attrs, R.attr.autoCompleteTextViewStyle);
        }

        public SearchAutoComplete(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            mThreshold = getThreshold();
        }

        @java.lang.Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
            setMinWidth(((int) (android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, getSearchViewTextMinWidthDp(), metrics))));
        }

        void setSearchView(android.support.v7.widget.SearchView searchView) {
            mSearchView = searchView;
        }

        @java.lang.Override
        public void setThreshold(int threshold) {
            super.setThreshold(threshold);
            mThreshold = threshold;
        }

        /**
         * Returns true if the text field is empty, or contains only whitespace.
         */
        private boolean isEmpty() {
            return android.text.TextUtils.getTrimmedLength(getText()) == 0;
        }

        /**
         * We override this method to avoid replacing the query box text when a
         * suggestion is clicked.
         */
        @java.lang.Override
        protected void replaceText(java.lang.CharSequence text) {
        }

        /**
         * We override this method to avoid an extra onItemClick being called on
         * the drop-down's OnItemClickListener by
         * {@link AutoCompleteTextView#onKeyUp(int, KeyEvent)} when an item is
         * clicked with the trackball.
         */
        @java.lang.Override
        public void performCompletion() {
        }

        /**
         * We override this method to be sure and show the soft keyboard if
         * appropriate when the TextView has focus.
         */
        @java.lang.Override
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
            if ((hasWindowFocus && mSearchView.hasFocus()) && (getVisibility() == android.view.View.VISIBLE)) {
                android.view.inputmethod.InputMethodManager inputManager = ((android.view.inputmethod.InputMethodManager) (getContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)));
                inputManager.showSoftInput(this, 0);
                // If in landscape mode, then make sure that
                // the ime is in front of the dropdown.
                if (android.support.v7.widget.SearchView.isLandscapeMode(getContext())) {
                    android.support.v7.widget.SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }

        @java.lang.Override
        protected void onFocusChanged(boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            mSearchView.onTextFocusChanged();
        }

        /**
         * We override this method so that we can allow a threshold of zero,
         * which ACTV does not.
         */
        @java.lang.Override
        public boolean enoughToFilter() {
            return (mThreshold <= 0) || super.enoughToFilter();
        }

        @java.lang.Override
        public boolean onKeyPreIme(int keyCode, android.view.KeyEvent event) {
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                // special case for the back key, we do not even try to send it
                // to the drop down list but instead, consume it immediately
                if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) {
                    android.view.KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else
                    if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                        android.view.KeyEvent.DispatcherState state = getKeyDispatcherState();
                        if (state != null) {
                            state.handleUpEvent(event);
                        }
                        if (event.isTracking() && (!event.isCanceled())) {
                            mSearchView.clearFocus();
                            mSearchView.setImeVisibility(false);
                            return true;
                        }
                    }

            }
            return super.onKeyPreIme(keyCode, event);
        }

        /**
         * Get minimum width of the search view text entry area.
         */
        private int getSearchViewTextMinWidthDp() {
            final android.content.res.Configuration config = getResources().getConfiguration();
            final int widthDp = android.support.v4.content.res.ConfigurationHelper.getScreenWidthDp(getResources());
            final int heightDp = android.support.v4.content.res.ConfigurationHelper.getScreenHeightDp(getResources());
            if (((widthDp >= 960) && (heightDp >= 720)) && (config.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE)) {
                return 256;
            } else
                if ((widthDp >= 600) || ((widthDp >= 640) && (heightDp >= 480))) {
                    return 192;
                }

            return 160;
        }
    }

    private static class AutoCompleteTextViewReflector {
        private java.lang.reflect.Method doBeforeTextChanged;

        private java.lang.reflect.Method doAfterTextChanged;

        private java.lang.reflect.Method ensureImeVisible;

        private java.lang.reflect.Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                doBeforeTextChanged = android.widget.AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged");
                doBeforeTextChanged.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                // Ah well.
            }
            try {
                doAfterTextChanged = android.widget.AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged");
                doAfterTextChanged.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                // Ah well.
            }
            try {
                ensureImeVisible = android.widget.AutoCompleteTextView.class.getMethod("ensureImeVisible", boolean.class);
                ensureImeVisible.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                // Ah well.
            }
            try {
                showSoftInputUnchecked = android.view.inputmethod.InputMethodManager.class.getMethod("showSoftInputUnchecked", int.class, android.os.ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                // Ah well.
            }
        }

        void doBeforeTextChanged(android.widget.AutoCompleteTextView view) {
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(view);
                } catch (java.lang.Exception e) {
                }
            }
        }

        void doAfterTextChanged(android.widget.AutoCompleteTextView view) {
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(view);
                } catch (java.lang.Exception e) {
                }
            }
        }

        void ensureImeVisible(android.widget.AutoCompleteTextView view, boolean visible) {
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(view, visible);
                } catch (java.lang.Exception e) {
                }
            }
        }

        void showSoftInputUnchecked(android.view.inputmethod.InputMethodManager imm, android.view.View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (java.lang.Exception e) {
                }
            }
            // Hidden method failed, call public version instead
            imm.showSoftInput(view, flags);
        }
    }
}

