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
 * Title view for a leanback fragment.
 */
public class TitleView extends android.widget.FrameLayout implements android.support.v17.leanback.widget.TitleViewAdapter.Provider {
    private android.widget.ImageView mBadgeView;

    private android.widget.TextView mTextView;

    private android.support.v17.leanback.widget.SearchOrbView mSearchOrbView;

    private int flags = android.support.v17.leanback.widget.TitleViewAdapter.FULL_VIEW_VISIBLE;

    private boolean mHasSearchListener = false;

    private final android.support.v17.leanback.widget.TitleViewAdapter mTitleViewAdapter = new android.support.v17.leanback.widget.TitleViewAdapter() {
        @java.lang.Override
        public android.view.View getSearchAffordanceView() {
            return android.support.v17.leanback.widget.TitleView.this.getSearchAffordanceView();
        }

        @java.lang.Override
        public void setOnSearchClickedListener(android.view.View.OnClickListener listener) {
            android.support.v17.leanback.widget.TitleView.this.setOnSearchClickedListener(listener);
        }

        @java.lang.Override
        public void setAnimationEnabled(boolean enable) {
            android.support.v17.leanback.widget.TitleView.this.enableAnimation(enable);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getBadgeDrawable() {
            return android.support.v17.leanback.widget.TitleView.this.getBadgeDrawable();
        }

        @java.lang.Override
        public android.support.v17.leanback.widget.SearchOrbView.Colors getSearchAffordanceColors() {
            return android.support.v17.leanback.widget.TitleView.this.getSearchAffordanceColors();
        }

        @java.lang.Override
        public java.lang.CharSequence getTitle() {
            return android.support.v17.leanback.widget.TitleView.this.getTitle();
        }

        @java.lang.Override
        public void setBadgeDrawable(android.graphics.drawable.Drawable drawable) {
            android.support.v17.leanback.widget.TitleView.this.setBadgeDrawable(drawable);
        }

        @java.lang.Override
        public void setSearchAffordanceColors(android.support.v17.leanback.widget.SearchOrbView.Colors colors) {
            android.support.v17.leanback.widget.TitleView.this.setSearchAffordanceColors(colors);
        }

        @java.lang.Override
        public void setTitle(java.lang.CharSequence titleText) {
            android.support.v17.leanback.widget.TitleView.this.setTitle(titleText);
        }

        @java.lang.Override
        public void updateComponentsVisibility(int flags) {
            android.support.v17.leanback.widget.TitleView.this.updateComponentsVisibility(flags);
        }
    };

    public TitleView(android.content.Context context) {
        this(context, null);
    }

    public TitleView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.browseTitleViewStyle);
    }

    public TitleView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        android.view.View rootView = inflater.inflate(R.layout.lb_title_view, this);
        mBadgeView = ((android.widget.ImageView) (rootView.findViewById(R.id.title_badge)));
        mTextView = ((android.widget.TextView) (rootView.findViewById(R.id.title_text)));
        mSearchOrbView = ((android.support.v17.leanback.widget.SearchOrbView) (rootView.findViewById(R.id.title_orb)));
        setClipToPadding(false);
        setClipChildren(false);
    }

    /**
     * Sets the title text.
     */
    public void setTitle(java.lang.CharSequence titleText) {
        mTextView.setText(titleText);
        updateBadgeVisibility();
    }

    /**
     * Returns the title text.
     */
    public java.lang.CharSequence getTitle() {
        return mTextView.getText();
    }

    /**
     * Sets the badge drawable.
     * If non-null, the drawable is displayed instead of the title text.
     */
    public void setBadgeDrawable(android.graphics.drawable.Drawable drawable) {
        mBadgeView.setImageDrawable(drawable);
        updateBadgeVisibility();
    }

    /**
     * Returns the badge drawable.
     */
    public android.graphics.drawable.Drawable getBadgeDrawable() {
        return mBadgeView.getDrawable();
    }

    /**
     * Sets the listener to be called when the search affordance is clicked.
     */
    public void setOnSearchClickedListener(android.view.View.OnClickListener listener) {
        mHasSearchListener = listener != null;
        mSearchOrbView.setOnOrbClickedListener(listener);
        updateSearchOrbViewVisiblity();
    }

    /**
     * Returns the view for the search affordance.
     */
    public android.view.View getSearchAffordanceView() {
        return mSearchOrbView;
    }

    /**
     * Sets the {@link SearchOrbView.Colors} used to draw the search affordance.
     */
    public void setSearchAffordanceColors(android.support.v17.leanback.widget.SearchOrbView.Colors colors) {
        mSearchOrbView.setOrbColors(colors);
    }

    /**
     * Returns the {@link SearchOrbView.Colors} used to draw the search affordance.
     */
    public android.support.v17.leanback.widget.SearchOrbView.Colors getSearchAffordanceColors() {
        return mSearchOrbView.getOrbColors();
    }

    /**
     * Enables or disables any view animations.
     */
    public void enableAnimation(boolean enable) {
        mSearchOrbView.enableOrbColorAnimation(enable && mSearchOrbView.hasFocus());
    }

    /**
     * Based on the flag, it updates the visibility of the individual components -
     * BadgeView, TextView and SearchView.
     *
     * @param flags
     * 		integer representing the visibility of TitleView components.
     * @see TitleViewAdapter#SEARCH_VIEW_VISIBLE
     * @see TitleViewAdapter#BRANDING_VIEW_VISIBLE
     * @see TitleViewAdapter#FULL_VIEW_VISIBLE
     */
    public void updateComponentsVisibility(int flags) {
        this.flags = flags;
        if ((flags & android.support.v17.leanback.widget.TitleViewAdapter.BRANDING_VIEW_VISIBLE) == android.support.v17.leanback.widget.TitleViewAdapter.BRANDING_VIEW_VISIBLE) {
            updateBadgeVisibility();
        } else {
            mBadgeView.setVisibility(android.view.View.GONE);
            mTextView.setVisibility(android.view.View.GONE);
        }
        updateSearchOrbViewVisiblity();
    }

    private void updateSearchOrbViewVisiblity() {
        int visibility = (mHasSearchListener && ((flags & android.support.v17.leanback.widget.TitleViewAdapter.SEARCH_VIEW_VISIBLE) == android.support.v17.leanback.widget.TitleViewAdapter.SEARCH_VIEW_VISIBLE)) ? android.view.View.VISIBLE : android.view.View.INVISIBLE;
        mSearchOrbView.setVisibility(visibility);
    }

    private void updateBadgeVisibility() {
        android.graphics.drawable.Drawable drawable = mBadgeView.getDrawable();
        if (drawable != null) {
            mBadgeView.setVisibility(android.view.View.VISIBLE);
            mTextView.setVisibility(android.view.View.GONE);
        } else {
            mBadgeView.setVisibility(android.view.View.GONE);
            mTextView.setVisibility(android.view.View.VISIBLE);
        }
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.TitleViewAdapter getTitleViewAdapter() {
        return mTitleViewAdapter;
    }
}

