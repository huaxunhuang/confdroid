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
package android.app;


/**
 * Helper class for showing "bread crumbs" representing the fragment
 * stack in an activity.  This is intended to be used with
 * {@link ActionBar#setCustomView(View)
 * ActionBar.setCustomView(View)} to place the bread crumbs in
 * the action bar.
 *
 * <p>The default style for this view is
 * {@link android.R.style#Widget_FragmentBreadCrumbs}.
 *
 * @deprecated This widget is no longer supported.
 */
@java.lang.Deprecated
public class FragmentBreadCrumbs extends android.view.ViewGroup implements android.app.FragmentManager.OnBackStackChangedListener {
    android.app.Activity mActivity;

    android.view.LayoutInflater mInflater;

    android.widget.LinearLayout mContainer;

    int mMaxVisible = -1;

    // Hahah
    android.app.BackStackRecord mTopEntry;

    android.app.BackStackRecord mParentEntry;

    /**
     * Listener to inform when a parent entry is clicked
     */
    private android.view.View.OnClickListener mParentClickListener;

    private android.app.FragmentBreadCrumbs.OnBreadCrumbClickListener mOnBreadCrumbClickListener;

    private int mGravity;

    private int mLayoutResId;

    private int mTextColor;

    private static final int DEFAULT_GRAVITY = android.view.Gravity.START | android.view.Gravity.CENTER_VERTICAL;

    /**
     * Interface to intercept clicks on the bread crumbs.
     */
    public interface OnBreadCrumbClickListener {
        /**
         * Called when a bread crumb is clicked.
         *
         * @param backStack
         * 		The BackStackEntry whose bread crumb was clicked.
         * 		May be null, if this bread crumb is for the root of the back stack.
         * @param flags
         * 		Additional information about the entry.  Currently
         * 		always 0.
         * @return Return true to consume this click.  Return to false to allow
        the default action (popping back stack to this entry) to occur.
         */
        public boolean onBreadCrumbClick(android.app.FragmentManager.BackStackEntry backStack, int flags);
    }

    public FragmentBreadCrumbs(android.content.Context context) {
        this(context, null);
    }

    public FragmentBreadCrumbs(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.fragmentBreadCrumbsStyle);
    }

    public FragmentBreadCrumbs(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     *
     *
     * @unknown 
     */
    public FragmentBreadCrumbs(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.FragmentBreadCrumbs, defStyleAttr, defStyleRes);
        mGravity = a.getInt(com.android.internal.R.styleable.FragmentBreadCrumbs_gravity, android.app.FragmentBreadCrumbs.DEFAULT_GRAVITY);
        mLayoutResId = a.getResourceId(com.android.internal.R.styleable.FragmentBreadCrumbs_itemLayout, com.android.internal.R.layout.fragment_bread_crumb_item);
        mTextColor = a.getColor(com.android.internal.R.styleable.FragmentBreadCrumbs_itemColor, 0);
        a.recycle();
    }

    /**
     * Attach the bread crumbs to their activity.  This must be called once
     * when creating the bread crumbs.
     */
    public void setActivity(android.app.Activity a) {
        mActivity = a;
        mInflater = ((android.view.LayoutInflater) (a.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mContainer = ((android.widget.LinearLayout) (mInflater.inflate(com.android.internal.R.layout.fragment_bread_crumbs, this, false)));
        addView(mContainer);
        a.getFragmentManager().addOnBackStackChangedListener(this);
        updateCrumbs();
        setLayoutTransition(new android.animation.LayoutTransition());
    }

    /**
     * The maximum number of breadcrumbs to show. Older fragment headers will be hidden from view.
     *
     * @param visibleCrumbs
     * 		the number of visible breadcrumbs. This should be greater than zero.
     */
    public void setMaxVisible(int visibleCrumbs) {
        if (visibleCrumbs < 1) {
            throw new java.lang.IllegalArgumentException("visibleCrumbs must be greater than zero");
        }
        mMaxVisible = visibleCrumbs;
    }

    /**
     * Inserts an optional parent entry at the first position in the breadcrumbs. Selecting this
     * entry will result in a call to the specified listener's
     * {@link android.view.View.OnClickListener#onClick(View)}
     * method.
     *
     * @param title
     * 		the title for the parent entry
     * @param shortTitle
     * 		the short title for the parent entry
     * @param listener
     * 		the {@link android.view.View.OnClickListener} to be called when clicked.
     * 		A null will result in no action being taken when the parent entry is clicked.
     */
    public void setParentTitle(java.lang.CharSequence title, java.lang.CharSequence shortTitle, android.view.View.OnClickListener listener) {
        mParentEntry = createBackStackEntry(title, shortTitle);
        mParentClickListener = listener;
        updateCrumbs();
    }

    /**
     * Sets a listener for clicks on the bread crumbs.  This will be called before
     * the default click action is performed.
     *
     * @param listener
     * 		The new listener to set.  Replaces any existing listener.
     */
    public void setOnBreadCrumbClickListener(android.app.FragmentBreadCrumbs.OnBreadCrumbClickListener listener) {
        mOnBreadCrumbClickListener = listener;
    }

    private android.app.BackStackRecord createBackStackEntry(java.lang.CharSequence title, java.lang.CharSequence shortTitle) {
        if (title == null)
            return null;

        final android.app.BackStackRecord entry = new android.app.BackStackRecord(((android.app.FragmentManagerImpl) (mActivity.getFragmentManager())));
        entry.setBreadCrumbTitle(title);
        entry.setBreadCrumbShortTitle(shortTitle);
        return entry;
    }

    /**
     * Set a custom title for the bread crumbs.  This will be the first entry
     * shown at the left, representing the root of the bread crumbs.  If the
     * title is null, it will not be shown.
     */
    public void setTitle(java.lang.CharSequence title, java.lang.CharSequence shortTitle) {
        mTopEntry = createBackStackEntry(title, shortTitle);
        updateCrumbs();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Eventually we should implement our own layout of the views, rather than relying on
        // a single linear layout.
        final int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        final android.view.View child = getChildAt(0);
        final int childTop = mPaddingTop;
        final int childBottom = (mPaddingTop + child.getMeasuredHeight()) - mPaddingBottom;
        int childLeft;
        int childRight;
        final int layoutDirection = getLayoutDirection();
        final int horizontalGravity = mGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        switch (android.view.Gravity.getAbsoluteGravity(horizontalGravity, layoutDirection)) {
            case android.view.Gravity.RIGHT :
                childRight = (mRight - mLeft) - mPaddingRight;
                childLeft = childRight - child.getMeasuredWidth();
                break;
            case android.view.Gravity.CENTER_HORIZONTAL :
                childLeft = mPaddingLeft + (((mRight - mLeft) - child.getMeasuredWidth()) / 2);
                childRight = childLeft + child.getMeasuredWidth();
                break;
            case android.view.Gravity.LEFT :
            default :
                childLeft = mPaddingLeft;
                childRight = childLeft + child.getMeasuredWidth();
                break;
        }
        if (childLeft < mPaddingLeft) {
            childLeft = mPaddingLeft;
        }
        if (childRight > ((mRight - mLeft) - mPaddingRight)) {
            childRight = (mRight - mLeft) - mPaddingRight;
        }
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int measuredChildState = 0;
        // Find rightmost and bottom-most child
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = java.lang.Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = java.lang.Math.max(maxHeight, child.getMeasuredHeight());
                measuredChildState = android.view.View.combineMeasuredStates(measuredChildState, child.getMeasuredState());
            }
        }
        // Account for padding too
        maxWidth += mPaddingLeft + mPaddingRight;
        maxHeight += mPaddingTop + mPaddingBottom;
        // Check against our minimum height and width
        maxHeight = java.lang.Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = java.lang.Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(android.view.View.resolveSizeAndState(maxWidth, widthMeasureSpec, measuredChildState), android.view.View.resolveSizeAndState(maxHeight, heightMeasureSpec, measuredChildState << android.view.View.MEASURED_HEIGHT_STATE_SHIFT));
    }

    @java.lang.Override
    public void onBackStackChanged() {
        updateCrumbs();
    }

    /**
     * Returns the number of entries before the backstack, including the title of the current
     * fragment and any custom parent title that was set.
     */
    private int getPreEntryCount() {
        return (mTopEntry != null ? 1 : 0) + (mParentEntry != null ? 1 : 0);
    }

    /**
     * Returns the pre-entry corresponding to the index. If there is a parent and a top entry
     * set, parent has an index of zero and top entry has an index of 1. Returns null if the
     * specified index doesn't exist or is null.
     *
     * @param index
     * 		should not be more than {@link #getPreEntryCount()} - 1
     */
    private android.app.FragmentManager.BackStackEntry getPreEntry(int index) {
        // If there's a parent entry, then return that for zero'th item, else top entry.
        if (mParentEntry != null) {
            return index == 0 ? mParentEntry : mTopEntry;
        } else {
            return mTopEntry;
        }
    }

    void updateCrumbs() {
        android.app.FragmentManager fm = mActivity.getFragmentManager();
        int numEntries = fm.getBackStackEntryCount();
        int numPreEntries = getPreEntryCount();
        int numViews = mContainer.getChildCount();
        for (int i = 0; i < (numEntries + numPreEntries); i++) {
            android.app.FragmentManager.BackStackEntry bse = (i < numPreEntries) ? getPreEntry(i) : fm.getBackStackEntryAt(i - numPreEntries);
            if (i < numViews) {
                android.view.View v = mContainer.getChildAt(i);
                java.lang.Object tag = v.getTag();
                if (tag != bse) {
                    for (int j = i; j < numViews; j++) {
                        mContainer.removeViewAt(i);
                    }
                    numViews = i;
                }
            }
            if (i >= numViews) {
                final android.view.View item = mInflater.inflate(mLayoutResId, this, false);
                final android.widget.TextView text = ((android.widget.TextView) (item.findViewById(com.android.internal.R.id.title)));
                text.setText(bse.getBreadCrumbTitle());
                text.setTag(bse);
                text.setTextColor(mTextColor);
                if (i == 0) {
                    item.findViewById(com.android.internal.R.id.left_icon).setVisibility(android.view.View.GONE);
                }
                mContainer.addView(item);
                text.setOnClickListener(mOnClickListener);
            }
        }
        int viewI = numEntries + numPreEntries;
        numViews = mContainer.getChildCount();
        while (numViews > viewI) {
            mContainer.removeViewAt(numViews - 1);
            numViews--;
        } 
        // Adjust the visibility and availability of the bread crumbs and divider
        for (int i = 0; i < numViews; i++) {
            final android.view.View child = mContainer.getChildAt(i);
            // Disable the last one
            child.findViewById(com.android.internal.R.id.title).setEnabled(i < (numViews - 1));
            if (mMaxVisible > 0) {
                // Make only the last mMaxVisible crumbs visible
                child.setVisibility(i < (numViews - mMaxVisible) ? android.view.View.GONE : android.view.View.VISIBLE);
                final android.view.View leftIcon = child.findViewById(com.android.internal.R.id.left_icon);
                // Remove the divider for all but the last mMaxVisible - 1
                leftIcon.setVisibility((i > (numViews - mMaxVisible)) && (i != 0) ? android.view.View.VISIBLE : android.view.View.GONE);
            }
        }
    }

    private android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            if (v.getTag() instanceof android.app.FragmentManager.BackStackEntry) {
                android.app.FragmentManager.BackStackEntry bse = ((android.app.FragmentManager.BackStackEntry) (v.getTag()));
                if (bse == mParentEntry) {
                    if (mParentClickListener != null) {
                        mParentClickListener.onClick(v);
                    }
                } else {
                    if (mOnBreadCrumbClickListener != null) {
                        if (mOnBreadCrumbClickListener.onBreadCrumbClick(bse == mTopEntry ? null : bse, 0)) {
                            return;
                        }
                    }
                    if (bse == mTopEntry) {
                        // Pop everything off the back stack.
                        mActivity.getFragmentManager().popBackStack();
                    } else {
                        mActivity.getFragmentManager().popBackStack(bse.getId(), 0);
                    }
                }
            }
        }
    };
}

