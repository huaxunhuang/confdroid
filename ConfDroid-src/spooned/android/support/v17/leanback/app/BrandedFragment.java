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
 * Fragment class for managing search and branding using a view that implements
 * {@link TitleViewAdapter.Provider}.
 */
public class BrandedFragment extends android.app.Fragment {
    // BUNDLE attribute for title is showing
    private static final java.lang.String TITLE_SHOW = "titleShow";

    private boolean mShowingTitle = true;

    private java.lang.CharSequence mTitle;

    private android.graphics.drawable.Drawable mBadgeDrawable;

    private android.view.View mTitleView;

    private android.support.v17.leanback.widget.TitleViewAdapter mTitleViewAdapter;

    private android.support.v17.leanback.widget.SearchOrbView.Colors mSearchAffordanceColors;

    private boolean mSearchAffordanceColorSet;

    private android.view.View.OnClickListener mExternalOnSearchClickedListener;

    private android.support.v17.leanback.widget.TitleHelper mTitleHelper;

    /**
     * Called by {@link #installTitleView(LayoutInflater, ViewGroup, Bundle)} to inflate
     * title view.  Default implementation uses layout file lb_browse_title.
     * Subclass may override and use its own layout, the layout must have a descendant with id
     * browse_title_group that implements {@link TitleViewAdapter.Provider}. Subclass may return
     * null if no title is needed.
     *
     * @param inflater
     * 		The LayoutInflater object that can be used to inflate
     * 		any views in the fragment,
     * @param parent
     * 		Parent of title view.
     * @param savedInstanceState
     * 		If non-null, this fragment is being re-constructed
     * 		from a previous saved state as given here.
     * @return Title view which must have a descendant with id browse_title_group that implements
    {@link TitleViewAdapter.Provider}, or null for no title view.
     */
    public android.view.View onInflateTitleView(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.os.Bundle savedInstanceState) {
        android.util.TypedValue typedValue = new android.util.TypedValue();
        boolean found = parent.getContext().getTheme().resolveAttribute(R.attr.browseTitleViewLayout, typedValue, true);
        return inflater.inflate(found ? typedValue.resourceId : R.layout.lb_browse_title, parent, false);
    }

    /**
     * Inflate title view and add to parent.  This method should be called in
     * {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * @param inflater
     * 		The LayoutInflater object that can be used to inflate
     * 		any views in the fragment,
     * @param parent
     * 		Parent of title view.
     * @param savedInstanceState
     * 		If non-null, this fragment is being re-constructed
     * 		from a previous saved state as given here.
     */
    public void installTitleView(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.os.Bundle savedInstanceState) {
        android.view.View titleLayoutRoot = onInflateTitleView(inflater, parent, savedInstanceState);
        if (titleLayoutRoot != null) {
            parent.addView(titleLayoutRoot);
            setTitleView(titleLayoutRoot.findViewById(R.id.browse_title_group));
        } else {
            setTitleView(null);
        }
    }

    /**
     * Sets the view that implemented {@link TitleViewAdapter}.
     *
     * @param titleView
     * 		The view that implemented {@link TitleViewAdapter.Provider}.
     */
    public void setTitleView(android.view.View titleView) {
        mTitleView = titleView;
        if (mTitleView == null) {
            mTitleViewAdapter = null;
            mTitleHelper = null;
        } else {
            mTitleViewAdapter = ((android.support.v17.leanback.widget.TitleViewAdapter.Provider) (mTitleView)).getTitleViewAdapter();
            mTitleViewAdapter.setTitle(mTitle);
            mTitleViewAdapter.setBadgeDrawable(mBadgeDrawable);
            if (mSearchAffordanceColorSet) {
                mTitleViewAdapter.setSearchAffordanceColors(mSearchAffordanceColors);
            }
            if (mExternalOnSearchClickedListener != null) {
                setOnSearchClickedListener(mExternalOnSearchClickedListener);
            }
            if (getView() instanceof android.view.ViewGroup) {
                mTitleHelper = new android.support.v17.leanback.widget.TitleHelper(((android.view.ViewGroup) (getView())), mTitleView);
            }
        }
    }

    /**
     * Returns the view that implements {@link TitleViewAdapter.Provider}.
     *
     * @return The view that implements {@link TitleViewAdapter.Provider}.
     */
    public android.view.View getTitleView() {
        return mTitleView;
    }

    /**
     * Returns the {@link TitleViewAdapter} implemented by title view.
     *
     * @return The {@link TitleViewAdapter} implemented by title view.
     */
    public android.support.v17.leanback.widget.TitleViewAdapter getTitleViewAdapter() {
        return mTitleViewAdapter;
    }

    /**
     * Returns the {@link TitleHelper}.
     */
    android.support.v17.leanback.widget.TitleHelper getTitleHelper() {
        return mTitleHelper;
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(android.support.v17.leanback.app.BrandedFragment.TITLE_SHOW, mShowingTitle);
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mShowingTitle = savedInstanceState.getBoolean(android.support.v17.leanback.app.BrandedFragment.TITLE_SHOW);
        }
        if ((mTitleView != null) && (view instanceof android.view.ViewGroup)) {
            mTitleHelper = new android.support.v17.leanback.widget.TitleHelper(((android.view.ViewGroup) (view)), mTitleView);
        }
    }

    @java.lang.Override
    public void onDestroyView() {
        super.onDestroyView();
        mTitleHelper = null;
    }

    /**
     * Shows or hides the title view.
     *
     * @param show
     * 		True to show title view, false to hide title view.
     */
    public void showTitle(boolean show) {
        // TODO: handle interruptions?
        if (show == mShowingTitle) {
            return;
        }
        mShowingTitle = show;
        if (mTitleHelper != null) {
            mTitleHelper.showTitle(show);
        }
    }

    /**
     * Changes title view's components visibility and shows title.
     *
     * @param flags
     * 		Flags representing the visibility of components inside title view.
     * @see TitleViewAdapter#SEARCH_VIEW_VISIBLE
     * @see TitleViewAdapter#BRANDING_VIEW_VISIBLE
     * @see TitleViewAdapter#FULL_VIEW_VISIBLE
     * @see TitleViewAdapter#updateComponentsVisibility(int)
     */
    public void showTitle(int flags) {
        if (mTitleViewAdapter != null) {
            mTitleViewAdapter.updateComponentsVisibility(flags);
        }
        showTitle(true);
    }

    /**
     * Sets the drawable displayed in the fragment title.
     *
     * @param drawable
     * 		The Drawable to display in the fragment title.
     */
    public void setBadgeDrawable(android.graphics.drawable.Drawable drawable) {
        if (mBadgeDrawable != drawable) {
            mBadgeDrawable = drawable;
            if (mTitleViewAdapter != null) {
                mTitleViewAdapter.setBadgeDrawable(drawable);
            }
        }
    }

    /**
     * Returns the badge drawable used in the fragment title.
     *
     * @return The badge drawable used in the fragment title.
     */
    public android.graphics.drawable.Drawable getBadgeDrawable() {
        return mBadgeDrawable;
    }

    /**
     * Sets title text for the fragment.
     *
     * @param title
     * 		The title text of the fragment.
     */
    public void setTitle(java.lang.CharSequence title) {
        mTitle = title;
        if (mTitleViewAdapter != null) {
            mTitleViewAdapter.setTitle(title);
        }
    }

    /**
     * Returns the title text for the fragment.
     *
     * @return Title text for the fragment.
     */
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Sets a click listener for the search affordance.
     *
     * <p>The presence of a listener will change the visibility of the search
     * affordance in the fragment title. When set to non-null, the title will
     * contain an element that a user may click to begin a search.
     *
     * <p>The listener's {@link View.OnClickListener#onClick onClick} method
     * will be invoked when the user clicks on the search element.
     *
     * @param listener
     * 		The listener to call when the search element is clicked.
     */
    public void setOnSearchClickedListener(android.view.View.OnClickListener listener) {
        mExternalOnSearchClickedListener = listener;
        if (mTitleViewAdapter != null) {
            mTitleViewAdapter.setOnSearchClickedListener(listener);
        }
    }

    /**
     * Sets the {@link android.support.v17.leanback.widget.SearchOrbView.Colors} used to draw the
     * search affordance.
     *
     * @param colors
     * 		Colors used to draw search affordance.
     */
    public void setSearchAffordanceColors(android.support.v17.leanback.widget.SearchOrbView.Colors colors) {
        mSearchAffordanceColors = colors;
        mSearchAffordanceColorSet = true;
        if (mTitleViewAdapter != null) {
            mTitleViewAdapter.setSearchAffordanceColors(mSearchAffordanceColors);
        }
    }

    /**
     * Returns the {@link android.support.v17.leanback.widget.SearchOrbView.Colors}
     * used to draw the search affordance.
     */
    public android.support.v17.leanback.widget.SearchOrbView.Colors getSearchAffordanceColors() {
        if (mSearchAffordanceColorSet) {
            return mSearchAffordanceColors;
        }
        if (mTitleViewAdapter == null) {
            throw new java.lang.IllegalStateException("Fragment views not yet created");
        }
        return mTitleViewAdapter.getSearchAffordanceColors();
    }

    /**
     * Sets the color used to draw the search affordance.
     * A default brighter color will be set by the framework.
     *
     * @param color
     * 		The color to use for the search affordance.
     */
    public void setSearchAffordanceColor(int color) {
        setSearchAffordanceColors(new android.support.v17.leanback.widget.SearchOrbView.Colors(color));
    }

    /**
     * Returns the color used to draw the search affordance.
     */
    public int getSearchAffordanceColor() {
        return getSearchAffordanceColors().color;
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        if (mTitleViewAdapter != null) {
            showTitle(mShowingTitle);
            mTitleViewAdapter.setAnimationEnabled(true);
        }
    }

    @java.lang.Override
    public void onPause() {
        if (mTitleViewAdapter != null) {
            mTitleViewAdapter.setAnimationEnabled(false);
        }
        super.onPause();
    }

    @java.lang.Override
    public void onResume() {
        super.onResume();
        if (mTitleViewAdapter != null) {
            mTitleViewAdapter.setAnimationEnabled(true);
        }
    }

    /**
     * Returns true/false to indicate the visibility of TitleView.
     *
     * @return boolean to indicate whether or not it's showing the title.
     */
    public final boolean isShowingTitle() {
        return mShowingTitle;
    }
}

