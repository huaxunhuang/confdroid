/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * A widget that provides a user interface for the user to enter a search query and submit a request
 * to a search provider. Shows a list of query suggestions or results, if available, and allows the
 * user to pick a suggestion or result to launch into.
 *
 * <p>
 * When the SearchView is used in an ActionBar as an action view for a collapsible menu item, it
 * needs to be set to iconified by default using {@link #setIconifiedByDefault(boolean)
 * setIconifiedByDefault(true)}. This is the default, so nothing needs to be done.
 * </p>
 * <p>
 * If you want the search field to always be visible, then call setIconifiedByDefault(false).
 * </p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using {@code SearchView}, read the
 * <a href="{@docRoot }guide/topics/search/index.html">Search</a> developer guide.</p>
 * </div>
 *
 * @see android.view.MenuItem#SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
 * @unknown ref android.R.styleable#SearchView_iconifiedByDefault
 * @unknown ref android.R.styleable#SearchView_imeOptions
 * @unknown ref android.R.styleable#SearchView_inputType
 * @unknown ref android.R.styleable#SearchView_maxWidth
 * @unknown ref android.R.styleable#SearchView_queryHint
 */
public class SearchView extends android.widget.LinearLayout implements android.view.CollapsibleActionView {
    private static final boolean DBG = false;

    private static final java.lang.String LOG_TAG = "SearchView";

    /**
     * Private constant for removing the microphone in the keyboard.
     */
    private static final java.lang.String IME_OPTION_NO_MICROPHONE = "nm";

    @android.annotation.UnsupportedAppUsage
    private final android.widget.SearchView.SearchAutoComplete mSearchSrcTextView;

    @android.annotation.UnsupportedAppUsage
    private final android.view.View mSearchEditFrame;

    @android.annotation.UnsupportedAppUsage
    private final android.view.View mSearchPlate;

    @android.annotation.UnsupportedAppUsage
    private final android.view.View mSubmitArea;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.ImageView mSearchButton;

    private final android.widget.ImageView mGoButton;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.ImageView mCloseButton;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.ImageView mVoiceButton;

    private final android.view.View mDropDownAnchor;

    private android.widget.SearchView.UpdatableTouchDelegate mTouchDelegate;

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
    @android.annotation.UnsupportedAppUsage
    private final android.graphics.drawable.Drawable mSearchHintIcon;

    // Resources used by SuggestionsAdapter to display suggestions.
    private final int mSuggestionRowLayout;

    private final int mSuggestionCommitIconResId;

    // Intents used for voice searching.
    private final android.content.Intent mVoiceWebSearchIntent;

    private final android.content.Intent mVoiceAppSearchIntent;

    private final java.lang.CharSequence mDefaultQueryHint;

    @android.annotation.UnsupportedAppUsage
    private android.widget.SearchView.OnQueryTextListener mOnQueryChangeListener;

    private android.widget.SearchView.OnCloseListener mOnCloseListener;

    private android.view.View.OnFocusChangeListener mOnQueryTextFocusChangeListener;

    private android.widget.SearchView.OnSuggestionListener mOnSuggestionListener;

    private android.view.View.OnClickListener mOnSearchClickListener;

    @android.annotation.UnsupportedAppUsage
    private boolean mIconifiedByDefault;

    @android.annotation.UnsupportedAppUsage
    private boolean mIconified;

    @android.annotation.UnsupportedAppUsage
    private android.widget.CursorAdapter mSuggestionsAdapter;

    private boolean mSubmitButtonEnabled;

    private java.lang.CharSequence mQueryHint;

    private boolean mQueryRefinement;

    @android.annotation.UnsupportedAppUsage
    private boolean mClearingFocus;

    private int mMaxWidth;

    @android.annotation.UnsupportedAppUsage
    private boolean mVoiceButtonEnabled;

    private java.lang.CharSequence mOldQueryText;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mUserQuery;

    @android.annotation.UnsupportedAppUsage
    private boolean mExpandedInActionView;

    @android.annotation.UnsupportedAppUsage
    private int mCollapsedImeOptions;

    private android.app.SearchableInfo mSearchable;

    private android.os.Bundle mAppSearchData;

    private java.lang.Runnable mUpdateDrawableStateRunnable = new java.lang.Runnable() {
        public void run() {
            updateFocusedState();
        }
    };

    private java.lang.Runnable mReleaseCursorRunnable = new java.lang.Runnable() {
        public void run() {
            if ((mSuggestionsAdapter != null) && (mSuggestionsAdapter instanceof android.widget.SuggestionsAdapter)) {
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
        this(context, attrs, defStyleAttr, 0);
    }

    public SearchView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.SearchView, attrs, a, defStyleAttr, defStyleRes);
        final android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        final int layoutResId = a.getResourceId(R.styleable.SearchView_layout, R.layout.search_view);
        inflater.inflate(layoutResId, this, true);
        mSearchSrcTextView = ((android.widget.SearchView.SearchAutoComplete) (findViewById(R.id.search_src_text)));
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
        mSearchPlate.setBackground(a.getDrawable(R.styleable.SearchView_queryBackground));
        mSubmitArea.setBackground(a.getDrawable(R.styleable.SearchView_submitBackground));
        mSearchButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
        mGoButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_goIcon));
        mCloseButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_closeIcon));
        mVoiceButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_voiceIcon));
        mCollapsedIcon.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
        // Prior to L MR1, the search hint icon defaulted to searchIcon. If the
        // style does not have an explicit value set, fall back to that.
        if (a.hasValueOrEmpty(R.styleable.SearchView_searchHintIcon)) {
            mSearchHintIcon = a.getDrawable(R.styleable.SearchView_searchHintIcon);
        } else {
            mSearchHintIcon = a.getDrawable(R.styleable.SearchView_searchIcon);
        }
        // Extract dropdown layout resource IDs for later use.
        mSuggestionRowLayout = a.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.search_dropdown_item_icons_2line);
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
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (mOnQueryTextFocusChangeListener != null) {
                    mOnQueryTextFocusChangeListener.onFocusChange(android.widget.SearchView.this, hasFocus);
                }
            }
        });
        setIconifiedByDefault(a.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
        final int maxWidth = a.getDimensionPixelSize(R.styleable.SearchView_maxWidth, -1);
        if (maxWidth != (-1)) {
            setMaxWidth(maxWidth);
        }
        mDefaultQueryHint = a.getText(R.styleable.SearchView_defaultQueryHint);
        mQueryHint = a.getText(R.styleable.SearchView_queryHint);
        final int imeOptions = a.getInt(R.styleable.SearchView_imeOptions, -1);
        if (imeOptions != (-1)) {
            setImeOptions(imeOptions);
        }
        final int inputType = a.getInt(R.styleable.SearchView_inputType, -1);
        if (inputType != (-1)) {
            setInputType(inputType);
        }
        if (getFocusable() == android.view.View.FOCUSABLE_AUTO) {
            setFocusable(android.view.View.FOCUSABLE);
        }
        a.recycle();
        // Save voice intent for later queries/launching
        mVoiceWebSearchIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_WEB_SEARCH);
        mVoiceWebSearchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        mVoiceWebSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mVoiceAppSearchIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mVoiceAppSearchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        mDropDownAnchor = findViewById(mSearchSrcTextView.getDropDownAnchor());
        if (mDropDownAnchor != null) {
            mDropDownAnchor.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() {
                @java.lang.Override
                public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    adjustDropDownSizeAndPosition();
                }
            });
        }
        updateViewsVisibility(mIconifiedByDefault);
        updateQueryHint();
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
            mSearchSrcTextView.setPrivateImeOptions(android.widget.SearchView.IME_OPTION_NO_MICROPHONE);
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
    public void setAppSearchData(android.os.Bundle appSearchData) {
        mAppSearchData = appSearchData;
    }

    /**
     * Sets the IME options on the query text field.
     *
     * @see TextView#setImeOptions(int)
     * @param imeOptions
     * 		the options to set on the query text field
     * @unknown ref android.R.styleable#SearchView_imeOptions
     */
    public void setImeOptions(int imeOptions) {
        mSearchSrcTextView.setImeOptions(imeOptions);
    }

    /**
     * Returns the IME options set on the query text field.
     *
     * @return the ime options
     * @see TextView#setImeOptions(int)
     * @unknown ref android.R.styleable#SearchView_imeOptions
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
     * @unknown ref android.R.styleable#SearchView_inputType
     */
    public void setInputType(int inputType) {
        mSearchSrcTextView.setInputType(inputType);
    }

    /**
     * Returns the input type set on the query text field.
     *
     * @return the input type
     * @unknown ref android.R.styleable#SearchView_inputType
     */
    public int getInputType() {
        return mSearchSrcTextView.getInputType();
    }

    /**
     *
     *
     * @unknown 
     */
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
    @java.lang.Override
    public void clearFocus() {
        mClearingFocus = true;
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mSearchSrcTextView.setImeVisibility(false);
        mClearingFocus = false;
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param listener
     * 		the listener object that receives callbacks when the user performs
     * 		actions in the SearchView such as clicking on buttons or typing a query.
     */
    public void setOnQueryTextListener(android.widget.SearchView.OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    /**
     * Sets a listener to inform when the user closes the SearchView.
     *
     * @param listener
     * 		the listener to call when the user closes the SearchView.
     */
    public void setOnCloseListener(android.widget.SearchView.OnCloseListener listener) {
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
    public void setOnSuggestionListener(android.widget.SearchView.OnSuggestionListener listener) {
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
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
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
     * @unknown ref android.R.styleable#SearchView_queryHint
     */
    public void setQueryHint(@android.annotation.Nullable
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
     * <li>Value specified in XML using
     *     {@link android.R.styleable#SearchView_queryHint android:queryHint}
     * <li>Valid string resource ID exposed by the {@link SearchableInfo} via
     *     {@link SearchableInfo#getHintId()}
     * <li>Default hint provided by the theme against which the view was
     *     inflated
     * </ol>
     *
     * @return the displayed query hint text, or {@code null} if none set
     * @unknown ref android.R.styleable#SearchView_queryHint
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
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
     * @unknown ref android.R.styleable#SearchView_iconifiedByDefault
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
     * @deprecated use {@link #isIconifiedByDefault()}
     * @unknown ref android.R.styleable#SearchView_iconifiedByDefault
     */
    @java.lang.Deprecated
    public boolean isIconfiedByDefault() {
        return mIconifiedByDefault;
    }

    /**
     * Returns the default iconified state of the search field.
     *
     * @unknown ref android.R.styleable#SearchView_iconifiedByDefault
     */
    @android.view.inspector.InspectableProperty
    public boolean isIconifiedByDefault() {
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
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
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
        if (mSuggestionsAdapter instanceof android.widget.SuggestionsAdapter) {
            ((android.widget.SuggestionsAdapter) (mSuggestionsAdapter)).setQueryRefinement(enable ? android.widget.SuggestionsAdapter.REFINE_ALL : android.widget.SuggestionsAdapter.REFINE_BY_ENTRY);
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
    public void setSuggestionsAdapter(android.widget.CursorAdapter adapter) {
        mSuggestionsAdapter = adapter;
        mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
    }

    /**
     * Returns the adapter used for suggestions, if any.
     *
     * @return the suggestions adapter
     */
    public android.widget.CursorAdapter getSuggestionsAdapter() {
        return mSuggestionsAdapter;
    }

    /**
     * Makes the view at most this many pixels wide
     *
     * @unknown ref android.R.styleable#SearchView_maxWidth
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
     * @unknown ref android.R.styleable#SearchView_maxWidth
     */
    @android.view.inspector.InspectableProperty
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
                mTouchDelegate = new android.widget.SearchView.UpdatableTouchDelegate(mSearchSrtTextViewBoundsExpanded, mSearchSrcTextViewBounds, mSearchSrcTextView);
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
        return getContext().getResources().getDimensionPixelSize(R.dimen.search_view_preferred_width);
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.search_view_preferred_height);
    }

    @android.annotation.UnsupportedAppUsage
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

    @android.annotation.UnsupportedAppUsage
    private void updateSubmitButton(boolean hasText) {
        int visibility = android.view.View.GONE;
        if (((mSubmitButtonEnabled && isSubmitAreaEnabled()) && hasFocus()) && (hasText || (!mVoiceButtonEnabled))) {
            visibility = android.view.View.VISIBLE;
        }
        mGoButton.setVisibility(visibility);
    }

    @android.annotation.UnsupportedAppUsage
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

    private void updateFocusedState() {
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

    /**
     * Called by the SuggestionsAdapter
     *
     * @unknown 
     */
    /* package */
    void onQueryRefine(java.lang.CharSequence queryText) {
        setQuery(queryText);
    }

    @android.annotation.UnsupportedAppUsage
    private final android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
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
     * Handles the key down event for dealing with action keys.
     *
     * @param keyCode
     * 		This is the keycode of the typed key, and is the same value as
     * 		found in the KeyEvent parameter.
     * @param event
     * 		The complete event record for the typed key
     * @return true if the event was handled here, or false if not.
     */
    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (mSearchable == null) {
            return false;
        }
        // if it's an action specified by the searchable activity, launch the
        // entered query with the action key
        android.app.SearchableInfo.ActionKeyInfo actionKey = mSearchable.findActionKey(keyCode);
        if ((actionKey != null) && (actionKey.getQueryActionMsg() != null)) {
            launchQuerySearch(keyCode, actionKey.getQueryActionMsg(), mSearchSrcTextView.getText().toString());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * React to the user typing "enter" or other hardwired keys while typing in
     * the search box. This handles these special keys while the edit box has
     * focus.
     */
    android.view.View.OnKeyListener mTextKeyListener = new android.view.View.OnKeyListener() {
        public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
            // guard against possible race conditions
            if (mSearchable == null) {
                return false;
            }
            if (android.widget.SearchView.DBG) {
                android.util.Log.d(android.widget.SearchView.LOG_TAG, (((("mTextListener.onKey(" + keyCode) + ",") + event) + "), selection: ") + mSearchSrcTextView.getListSelection());
            }
            // If a suggestion is selected, handle enter, search key, and action keys
            // as presses on the selected suggestion
            if (mSearchSrcTextView.isPopupShowing() && (mSearchSrcTextView.getListSelection() != android.widget.ListView.INVALID_POSITION)) {
                return onSuggestionsKey(v, keyCode, event);
            }
            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if ((!mSearchSrcTextView.isEmpty()) && event.hasNoModifiers()) {
                if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                    if (keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();
                        // Launch as a regular search.
                        launchQuerySearch(android.view.KeyEvent.KEYCODE_UNKNOWN, null, mSearchSrcTextView.getText().toString());
                        return true;
                    }
                }
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    android.app.SearchableInfo.ActionKeyInfo actionKey = mSearchable.findActionKey(keyCode);
                    if ((actionKey != null) && (actionKey.getQueryActionMsg() != null)) {
                        launchQuerySearch(keyCode, actionKey.getQueryActionMsg(), mSearchSrcTextView.getText().toString());
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
    private boolean onSuggestionsKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        // guard against possible race conditions (late arrival after dismiss)
        if (mSearchable == null) {
            return false;
        }
        if (mSuggestionsAdapter == null) {
            return false;
        }
        if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && event.hasNoModifiers()) {
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
                mSearchSrcTextView.ensureImeVisible(true);
                return true;
            }
            // Next, check for an "up and out" move
            if ((keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) && (0 == mSearchSrcTextView.getListSelection())) {
                // TODO: restoreUserQuery();
                // let ACTV complete the move
                return false;
            }
            // Next, check for an "action key"
            android.app.SearchableInfo.ActionKeyInfo actionKey = mSearchable.findActionKey(keyCode);
            if ((actionKey != null) && ((actionKey.getSuggestActionMsg() != null) || (actionKey.getSuggestActionMsgColumn() != null))) {
                // launch suggestion using action key column
                int position = mSearchSrcTextView.getListSelection();
                if (position != android.widget.ListView.INVALID_POSITION) {
                    android.database.Cursor c = mSuggestionsAdapter.getCursor();
                    if (c.moveToPosition(position)) {
                        final java.lang.String actionMsg = android.widget.SearchView.getActionKeyMessage(c, actionKey);
                        if ((actionMsg != null) && (actionMsg.length() > 0)) {
                            return onItemClicked(position, keyCode, actionMsg);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * For a given suggestion and a given cursor row, get the action message. If
     * not provided by the specific row/column, also check for a single
     * definition (for the action key).
     *
     * @param c
     * 		The cursor providing suggestions
     * @param actionKey
     * 		The actionkey record being examined
     * @return Returns a string, or null if no action key message for this
    suggestion
     */
    private static java.lang.String getActionKeyMessage(android.database.Cursor c, android.app.SearchableInfo.ActionKeyInfo actionKey) {
        java.lang.String result = null;
        // check first in the cursor data, for a suggestion-specific message
        final java.lang.String column = actionKey.getSuggestActionMsgColumn();
        if (column != null) {
            result = android.widget.SuggestionsAdapter.getColumnString(c, column);
        }
        // If the cursor didn't give us a message, see if there's a single
        // message defined
        // for the actionkey (for all suggestions)
        if (result == null) {
            result = actionKey.getSuggestActionMsg();
        }
        return result;
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
        ssb.setSpan(new android.text.style.ImageSpan(mSearchHintIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        mSearchSrcTextView.setDropDownAnimationStyle(0);// no animation

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
            mSuggestionsAdapter = new android.widget.SuggestionsAdapter(getContext(), this, mSearchable, mOutsideDrawablesCache);
            mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
            ((android.widget.SuggestionsAdapter) (mSuggestionsAdapter)).setQueryRefinement(mQueryRefinement ? android.widget.SuggestionsAdapter.REFINE_ALL : android.widget.SuggestionsAdapter.REFINE_BY_ENTRY);
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
        public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
            onSubmitQuery();
            return true;
        }
    };

    private void onTextChanged(java.lang.CharSequence newText) {
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

    private void onSubmitQuery() {
        java.lang.CharSequence query = mSearchSrcTextView.getText();
        if ((query != null) && (android.text.TextUtils.getTrimmedLength(query) > 0)) {
            if ((mOnQueryChangeListener == null) || (!mOnQueryChangeListener.onQueryTextSubmit(query.toString()))) {
                if (mSearchable != null) {
                    launchQuerySearch(android.view.KeyEvent.KEYCODE_UNKNOWN, null, query.toString());
                }
                mSearchSrcTextView.setImeVisibility(false);
                dismissSuggestions();
            }
        }
    }

    private void dismissSuggestions() {
        mSearchSrcTextView.dismissDropDown();
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private void onCloseClicked() {
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
            mSearchSrcTextView.setImeVisibility(true);
        }
    }

    private void onSearchClicked() {
        updateViewsVisibility(false);
        mSearchSrcTextView.requestFocus();
        mSearchSrcTextView.setImeVisibility(true);
        if (mOnSearchClickListener != null) {
            mOnSearchClickListener.onClick(this);
        }
    }

    private void onVoiceClicked() {
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
            android.util.Log.w(android.widget.SearchView.LOG_TAG, "Could not find voice search activity");
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

    static class SavedState extends android.view.View.BaseSavedState {
        boolean isIconified;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        public SavedState(android.os.Parcel source) {
            super(source);
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

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.SearchView.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.SearchView.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.widget.SearchView.SavedState ss = new android.widget.SearchView.SavedState(superState);
        ss.isIconified = isIconified();
        return ss;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.SearchView.SavedState ss = ((android.widget.SearchView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        updateViewsVisibility(ss.isIconified);
        requestLayout();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.SearchView.class.getName();
    }

    private void adjustDropDownSizeAndPosition() {
        if (mDropDownAnchor.getWidth() > 1) {
            android.content.res.Resources res = getContext().getResources();
            int anchorPadding = mSearchPlate.getPaddingLeft();
            android.graphics.Rect dropDownPadding = new android.graphics.Rect();
            final boolean isLayoutRtl = isLayoutRtl();
            int iconOffset = (mIconifiedByDefault) ? res.getDimensionPixelSize(R.dimen.dropdownitem_icon_width) + res.getDimensionPixelSize(R.dimen.dropdownitem_text_padding_left) : 0;
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

    private boolean onItemClicked(int position, int actionKey, java.lang.String actionMsg) {
        if ((mOnSuggestionListener == null) || (!mOnSuggestionListener.onSuggestionClick(position))) {
            launchSuggestion(position, android.view.KeyEvent.KEYCODE_UNKNOWN, null);
            mSearchSrcTextView.setImeVisibility(false);
            dismissSuggestions();
            return true;
        }
        return false;
    }

    private boolean onItemSelected(int position) {
        if ((mOnSuggestionListener == null) || (!mOnSuggestionListener.onSuggestionSelect(position))) {
            rewriteQueryFromSuggestion(position);
            return true;
        }
        return false;
    }

    @android.annotation.UnsupportedAppUsage
    private final android.widget.AdapterView.OnItemClickListener mOnItemClickListener = new android.widget.AdapterView.OnItemClickListener() {
        /**
         * Implements OnItemClickListener
         */
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            if (android.widget.SearchView.DBG)
                android.util.Log.d(android.widget.SearchView.LOG_TAG, "onItemClick() position " + position);

            onItemClicked(position, android.view.KeyEvent.KEYCODE_UNKNOWN, null);
        }
    };

    private final android.widget.AdapterView.OnItemSelectedListener mOnItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {
        /**
         * Implements OnItemSelectedListener
         */
        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            if (android.widget.SearchView.DBG)
                android.util.Log.d(android.widget.SearchView.LOG_TAG, "onItemSelected() position " + position);

            android.widget.SearchView.this.onItemSelected(position);
        }

        /**
         * Implements OnItemSelectedListener
         */
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            if (android.widget.SearchView.DBG)
                android.util.Log.d(android.widget.SearchView.LOG_TAG, "onNothingSelected()");

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
            android.util.Log.e(android.widget.SearchView.LOG_TAG, "Failed launch activity: " + intent, ex);
        }
    }

    /**
     * Sets the text in the query box, without updating the suggestions.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private void setQuery(java.lang.CharSequence query) {
        mSearchSrcTextView.setText(query, true);
        // Move the cursor to the end
        mSearchSrcTextView.setSelection(android.text.TextUtils.isEmpty(query) ? 0 : query.length());
    }

    private void launchQuerySearch(int actionKey, java.lang.String actionMsg, java.lang.String query) {
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
     * @param mode
     * 		The search mode, one of the acceptable values for
     * 		{@link SearchManager#SEARCH_MODE}, or {@code null}.
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
        intent.putExtra(SearchManager.USER_QUERY, mUserQuery);
        if (query != null) {
            intent.putExtra(SearchManager.QUERY, query);
        }
        if (extraData != null) {
            intent.putExtra(SearchManager.EXTRA_DATA_KEY, extraData);
        }
        if (mAppSearchData != null) {
            intent.putExtra(SearchManager.APP_DATA, mAppSearchData);
        }
        if (actionKey != android.view.KeyEvent.KEYCODE_UNKNOWN) {
            intent.putExtra(SearchManager.ACTION_KEY, actionKey);
            intent.putExtra(SearchManager.ACTION_MSG, actionMsg);
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
        voiceIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, searchActivity == null ? null : searchActivity.flattenToShortString());
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
        android.app.PendingIntent pending = android.app.PendingIntent.getActivity(getContext(), 0, queryIntent, PendingIntent.FLAG_ONE_SHOT);
        // Now set up the bundle that will be inserted into the pending intent
        // when it's time to do the search.  We always build it here (even if empty)
        // because the voice search activity will always need to insert "QUERY" into
        // it anyway.
        android.os.Bundle queryExtras = new android.os.Bundle();
        if (mAppSearchData != null) {
            queryExtras.putParcelable(SearchManager.APP_DATA, mAppSearchData);
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
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, searchActivity == null ? null : searchActivity.flattenToShortString());
        // Add the values that configure forwarding the results
        voiceIntent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, pending);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT_BUNDLE, queryExtras);
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
            java.lang.String action = android.widget.SuggestionsAdapter.getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
            if (action == null) {
                action = mSearchable.getSuggestIntentAction();
            }
            if (action == null) {
                action = android.content.Intent.ACTION_SEARCH;
            }
            // use specific data if supplied, or default data if supplied
            java.lang.String data = android.widget.SuggestionsAdapter.getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_DATA);
            if (data == null) {
                data = mSearchable.getSuggestIntentData();
            }
            // then, if an ID was provided, append it.
            if (data != null) {
                java.lang.String id = android.widget.SuggestionsAdapter.getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
                if (id != null) {
                    data = (data + "/") + android.net.Uri.encode(id);
                }
            }
            android.net.Uri dataUri = (data == null) ? null : android.net.Uri.parse(data);
            java.lang.String query = android.widget.SuggestionsAdapter.getColumnString(c, SearchManager.SUGGEST_COLUMN_QUERY);
            java.lang.String extraData = android.widget.SuggestionsAdapter.getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
            return createIntent(action, dataUri, extraData, query, actionKey, actionMsg);
        } catch (java.lang.RuntimeException e) {
            int rowNum;
            try {
                // be really paranoid now
                rowNum = c.getPosition();
            } catch (java.lang.RuntimeException e2) {
                rowNum = -1;
            }
            android.util.Log.w(android.widget.SearchView.LOG_TAG, ("Search suggestions cursor at row " + rowNum) + " returned exception.", e);
            return null;
        }
    }

    private void forceSuggestionQuery() {
        mSearchSrcTextView.doBeforeTextChanged();
        mSearchSrcTextView.doAfterTextChanged();
    }

    static boolean isLandscapeMode(android.content.Context context) {
        return context.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Callback to watch the text field for empty/non-empty
     */
    private android.text.TextWatcher mTextWatcher = new android.text.TextWatcher() {
        public void beforeTextChanged(java.lang.CharSequence s, int start, int before, int after) {
        }

        public void onTextChanged(java.lang.CharSequence s, int start, int before, int after) {
            android.widget.SearchView.this.onTextChanged(s);
        }

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
    public static class SearchAutoComplete extends android.widget.AutoCompleteTextView {
        private int mThreshold;

        private android.widget.SearchView mSearchView;

        private boolean mHasPendingShowSoftInputRequest;

        final java.lang.Runnable mRunShowSoftInputIfNecessary = () -> showSoftInputIfNecessary();

        public SearchAutoComplete(android.content.Context context) {
            super(context);
            mThreshold = getThreshold();
        }

        @android.annotation.UnsupportedAppUsage
        public SearchAutoComplete(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            mThreshold = getThreshold();
        }

        public SearchAutoComplete(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttrs) {
            super(context, attrs, defStyleAttrs);
            mThreshold = getThreshold();
        }

        public SearchAutoComplete(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttrs, int defStyleRes) {
            super(context, attrs, defStyleAttrs, defStyleRes);
            mThreshold = getThreshold();
        }

        @java.lang.Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
            setMinWidth(((int) (android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getSearchViewTextMinWidthDp(), metrics))));
        }

        void setSearchView(android.widget.SearchView searchView) {
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
                // Since InputMethodManager#onPostWindowFocus() will be called after this callback,
                // it is a bit too early to call InputMethodManager#showSoftInput() here. We still
                // need to wait until the system calls back onCreateInputConnection() to call
                // InputMethodManager#showSoftInput().
                mHasPendingShowSoftInputRequest = true;
                // If in landscape mode, then make sure that the ime is in front of the dropdown.
                if (android.widget.SearchView.isLandscapeMode(getContext())) {
                    ensureImeVisible(true);
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
            final boolean consume = super.onKeyPreIme(keyCode, event);
            if ((consume && (keyCode == android.view.KeyEvent.KEYCODE_BACK)) && (event.getAction() == android.view.KeyEvent.ACTION_UP)) {
                // If AutoCompleteTextView closed its pop-up, it will return true, in which case
                // we should also close the IME. Otherwise, the popup is already closed and we can
                // leave the BACK event alone.
                setImeVisibility(false);
            }
            return consume;
        }

        /**
         * Get minimum width of the search view text entry area.
         */
        private int getSearchViewTextMinWidthDp() {
            final android.content.res.Configuration configuration = getResources().getConfiguration();
            final int width = configuration.screenWidthDp;
            final int height = configuration.screenHeightDp;
            final int orientation = configuration.orientation;
            if (((width >= 960) && (height >= 720)) && (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE)) {
                return 256;
            } else
                if ((width >= 600) || ((width >= 640) && (height >= 480))) {
                    return 192;
                }

            return 160;
        }

        /**
         * We override {@link View#onCreateInputConnection(EditorInfo)} as a signal to schedule a
         * pending {@link InputMethodManager#showSoftInput(View, int)} request (if any).
         */
        @java.lang.Override
        public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo editorInfo) {
            final android.view.inputmethod.InputConnection ic = super.onCreateInputConnection(editorInfo);
            if (mHasPendingShowSoftInputRequest) {
                removeCallbacks(mRunShowSoftInputIfNecessary);
                post(mRunShowSoftInputIfNecessary);
            }
            return ic;
        }

        @java.lang.Override
        public boolean checkInputConnectionProxy(android.view.View view) {
            return view == mSearchView;
        }

        private void showSoftInputIfNecessary() {
            if (mHasPendingShowSoftInputRequest) {
                final android.view.inputmethod.InputMethodManager imm = getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
                imm.showSoftInput(this, 0);
                mHasPendingShowSoftInputRequest = false;
            }
        }

        private void setImeVisibility(final boolean visible) {
            final android.view.inputmethod.InputMethodManager imm = getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
            if (!visible) {
                mHasPendingShowSoftInputRequest = false;
                removeCallbacks(mRunShowSoftInputIfNecessary);
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
                return;
            }
            if (imm.isActive(this)) {
                // This means that SearchAutoComplete is already connected to the IME.
                // InputMethodManager#showSoftInput() is guaranteed to pass client-side focus check.
                mHasPendingShowSoftInputRequest = false;
                removeCallbacks(mRunShowSoftInputIfNecessary);
                imm.showSoftInput(this, 0);
                return;
            }
            // Otherwise, InputMethodManager#showSoftInput() should be deferred after
            // onCreateInputConnection().
            mHasPendingShowSoftInputRequest = true;
        }
    }
}

