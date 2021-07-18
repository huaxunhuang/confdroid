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
package android.support.v17.leanback.widget;


/**
 * A card style layout that responds to certain state changes. It arranges its
 * children in a vertical column, with different regions becoming visible at
 * different times.
 *
 * <p>
 * A BaseCardView will draw its children based on its type, the region
 * visibilities of the child types, and the state of the widget. A child may be
 * marked as belonging to one of three regions: main, info, or extra. The main
 * region is always visible, while the info and extra regions can be set to
 * display based on the activated or selected state of the View. The card states
 * are set by calling {@link #setActivated(boolean) setActivated} and
 * {@link #setSelected(boolean) setSelected}.
 * <p>
 * See {@link BaseCardView.LayoutParams} for layout attributes.
 * </p>
 */
public class BaseCardView extends android.widget.FrameLayout {
    private static final java.lang.String TAG = "BaseCardView";

    private static final boolean DEBUG = false;

    /**
     * A simple card type with a single layout area. This card type does not
     * change its layout or size as it transitions between
     * Activated/Not-Activated or Selected/Unselected states.
     *
     * @see #getCardType()
     */
    public static final int CARD_TYPE_MAIN_ONLY = 0;

    /**
     * A Card type with 2 layout areas: A main area which is always visible, and
     * an info area that fades in over the main area when it is visible.
     * The card height will not change.
     *
     * @see #getCardType()
     */
    public static final int CARD_TYPE_INFO_OVER = 1;

    /**
     * A Card type with 2 layout areas: A main area which is always visible, and
     * an info area that appears below the main area. When the info area is visible
     * the total card height will change.
     *
     * @see #getCardType()
     */
    public static final int CARD_TYPE_INFO_UNDER = 2;

    /**
     * A Card type with 3 layout areas: A main area which is always visible; an
     * info area which will appear below the main area, and an extra area that
     * only appears after a short delay. The info area appears below the main
     * area, causing the total card height to change. The extra area animates in
     * at the bottom of the card, shifting up the info view without affecting
     * the card height.
     *
     * @see #getCardType()
     */
    public static final int CARD_TYPE_INFO_UNDER_WITH_EXTRA = 3;

    /**
     * Indicates that a card region is always visible.
     */
    public static final int CARD_REGION_VISIBLE_ALWAYS = 0;

    /**
     * Indicates that a card region is visible when the card is activated.
     */
    public static final int CARD_REGION_VISIBLE_ACTIVATED = 1;

    /**
     * Indicates that a card region is visible when the card is selected.
     */
    public static final int CARD_REGION_VISIBLE_SELECTED = 2;

    private static final int CARD_TYPE_INVALID = 4;

    private int mCardType;

    private int mInfoVisibility;

    private int mExtraVisibility;

    private java.util.ArrayList<android.view.View> mMainViewList;

    java.util.ArrayList<android.view.View> mInfoViewList;

    java.util.ArrayList<android.view.View> mExtraViewList;

    private int mMeasuredWidth;

    private int mMeasuredHeight;

    private boolean mDelaySelectedAnim;

    private int mSelectedAnimationDelay;

    private final int mActivatedAnimDuration;

    private final int mSelectedAnimDuration;

    float mInfoOffset;

    float mInfoVisFraction;

    float mInfoAlpha = 1.0F;

    private android.view.animation.Animation mAnim;

    private static final int[] LB_PRESSED_STATE_SET = new int[]{ android.R.attr.state_pressed };

    private final java.lang.Runnable mAnimationTrigger = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            animateInfoOffset(true);
        }
    };

    public BaseCardView(android.content.Context context) {
        this(context, null);
    }

    public BaseCardView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.baseCardViewStyle);
    }

    public BaseCardView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbBaseCardView, defStyleAttr, 0);
        try {
            mCardType = a.getInteger(R.styleable.lbBaseCardView_cardType, android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_MAIN_ONLY);
            android.graphics.drawable.Drawable cardForeground = a.getDrawable(R.styleable.lbBaseCardView_cardForeground);
            if (cardForeground != null) {
                setForeground(cardForeground);
            }
            android.graphics.drawable.Drawable cardBackground = a.getDrawable(R.styleable.lbBaseCardView_cardBackground);
            if (cardBackground != null) {
                setBackground(cardBackground);
            }
            mInfoVisibility = a.getInteger(R.styleable.lbBaseCardView_infoVisibility, android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
            mExtraVisibility = a.getInteger(R.styleable.lbBaseCardView_extraVisibility, android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED);
            // Extra region should never show before info region.
            if (mExtraVisibility < mInfoVisibility) {
                mExtraVisibility = mInfoVisibility;
            }
            mSelectedAnimationDelay = a.getInteger(R.styleable.lbBaseCardView_selectedAnimationDelay, getResources().getInteger(R.integer.lb_card_selected_animation_delay));
            mSelectedAnimDuration = a.getInteger(R.styleable.lbBaseCardView_selectedAnimationDuration, getResources().getInteger(R.integer.lb_card_selected_animation_duration));
            mActivatedAnimDuration = a.getInteger(R.styleable.lbBaseCardView_activatedAnimationDuration, getResources().getInteger(R.integer.lb_card_activated_animation_duration));
        } finally {
            a.recycle();
        }
        mDelaySelectedAnim = true;
        mMainViewList = new java.util.ArrayList<android.view.View>();
        mInfoViewList = new java.util.ArrayList<android.view.View>();
        mExtraViewList = new java.util.ArrayList<android.view.View>();
        mInfoOffset = 0.0F;
        mInfoVisFraction = 0.0F;
    }

    /**
     * Sets a flag indicating if the Selected animation (if the selected card
     * type implements one) should run immediately after the card is selected,
     * or if it should be delayed. The default behavior is to delay this
     * animation. This is a one-shot override. If set to false, after the card
     * is selected and the selected animation is triggered, this flag is
     * automatically reset to true. This is useful when you want to change the
     * default behavior, and have the selected animation run immediately. One
     * such case could be when focus moves from one row to the other, when
     * instead of delaying the selected animation until the user pauses on a
     * card, it may be desirable to trigger the animation for that card
     * immediately.
     *
     * @param delay
     * 		True (default) if the selected animation should be delayed
     * 		after the card is selected, or false if the animation should
     * 		run immediately the next time the card is Selected.
     */
    public void setSelectedAnimationDelayed(boolean delay) {
        mDelaySelectedAnim = delay;
    }

    /**
     * Returns a boolean indicating if the selected animation will run
     * immediately or be delayed the next time the card is Selected.
     *
     * @return true if this card is set to delay the selected animation the next
    time it is selected, or false if the selected animation will run
    immediately the next time the card is selected.
     */
    public boolean isSelectedAnimationDelayed() {
        return mDelaySelectedAnim;
    }

    /**
     * Sets the type of this Card.
     *
     * @param type
     * 		The desired card type.
     */
    public void setCardType(int type) {
        if (mCardType != type) {
            if ((type >= android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_MAIN_ONLY) && (type < android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INVALID)) {
                // Valid card type
                mCardType = type;
            } else {
                android.util.Log.e(android.support.v17.leanback.widget.BaseCardView.TAG, ("Invalid card type specified: " + type) + ". Defaulting to type CARD_TYPE_MAIN_ONLY.");
                mCardType = android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_MAIN_ONLY;
            }
            requestLayout();
        }
    }

    /**
     * Returns the type of this Card.
     *
     * @return The type of this card.
     */
    public int getCardType() {
        return mCardType;
    }

    /**
     * Sets the visibility of the info region of the card.
     *
     * @param visibility
     * 		The region visibility to use for the info region. Must
     * 		be one of {@link #CARD_REGION_VISIBLE_ALWAYS},
     * 		{@link #CARD_REGION_VISIBLE_SELECTED}, or
     * 		{@link #CARD_REGION_VISIBLE_ACTIVATED}.
     */
    public void setInfoVisibility(int visibility) {
        if (mInfoVisibility != visibility) {
            mInfoVisibility = visibility;
            if ((mInfoVisibility == android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED) && isSelected()) {
                mInfoVisFraction = 1.0F;
            } else {
                mInfoVisFraction = 0.0F;
            }
            requestLayout();
        }
    }

    /**
     * Returns the visibility of the info region of the card.
     */
    public int getInfoVisibility() {
        return mInfoVisibility;
    }

    /**
     * Sets the visibility of the extra region of the card.
     *
     * @param visibility
     * 		The region visibility to use for the extra region. Must
     * 		be one of {@link #CARD_REGION_VISIBLE_ALWAYS},
     * 		{@link #CARD_REGION_VISIBLE_SELECTED}, or
     * 		{@link #CARD_REGION_VISIBLE_ACTIVATED}.
     */
    public void setExtraVisibility(int visibility) {
        if (mExtraVisibility != visibility) {
            mExtraVisibility = visibility;
            requestLayout();
        }
    }

    /**
     * Returns the visibility of the extra region of the card.
     */
    public int getExtraVisibility() {
        return mExtraVisibility;
    }

    /**
     * Sets the Activated state of this Card. This can trigger changes in the
     * card layout, resulting in views to become visible or hidden. A card is
     * normally set to Activated state when its parent container (like a Row)
     * receives focus, and then activates all of its children.
     *
     * @param activated
     * 		True if the card is ACTIVE, or false if INACTIVE.
     * @see #isActivated()
     */
    @java.lang.Override
    public void setActivated(boolean activated) {
        if (activated != isActivated()) {
            super.setActivated(activated);
            applyActiveState(isActivated());
        }
    }

    /**
     * Sets the Selected state of this Card. This can trigger changes in the
     * card layout, resulting in views to become visible or hidden. A card is
     * normally set to Selected state when it receives input focus.
     *
     * @param selected
     * 		True if the card is Selected, or false otherwise.
     * @see #isSelected()
     */
    @java.lang.Override
    public void setSelected(boolean selected) {
        if (selected != isSelected()) {
            super.setSelected(selected);
            applySelectedState(isSelected());
        }
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasuredWidth = 0;
        mMeasuredHeight = 0;
        int state = 0;
        int mainHeight = 0;
        int infoHeight = 0;
        int extraHeight = 0;
        findChildrenViews();
        final int unspecifiedSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        // MAIN is always present
        for (int i = 0; i < mMainViewList.size(); i++) {
            android.view.View mainView = mMainViewList.get(i);
            if (mainView.getVisibility() != android.view.View.GONE) {
                measureChild(mainView, unspecifiedSpec, unspecifiedSpec);
                mMeasuredWidth = java.lang.Math.max(mMeasuredWidth, mainView.getMeasuredWidth());
                mainHeight += mainView.getMeasuredHeight();
                state = android.view.View.combineMeasuredStates(state, mainView.getMeasuredState());
            }
        }
        setPivotX(mMeasuredWidth / 2);
        setPivotY(mainHeight / 2);
        // The MAIN area determines the card width
        int cardWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(mMeasuredWidth, android.view.View.MeasureSpec.EXACTLY);
        if (hasInfoRegion()) {
            for (int i = 0; i < mInfoViewList.size(); i++) {
                android.view.View infoView = mInfoViewList.get(i);
                if (infoView.getVisibility() != android.view.View.GONE) {
                    measureChild(infoView, cardWidthMeasureSpec, unspecifiedSpec);
                    if (mCardType != android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_OVER) {
                        infoHeight += infoView.getMeasuredHeight();
                    }
                    state = android.view.View.combineMeasuredStates(state, infoView.getMeasuredState());
                }
            }
            if (hasExtraRegion()) {
                for (int i = 0; i < mExtraViewList.size(); i++) {
                    android.view.View extraView = mExtraViewList.get(i);
                    if (extraView.getVisibility() != android.view.View.GONE) {
                        measureChild(extraView, cardWidthMeasureSpec, unspecifiedSpec);
                        extraHeight += extraView.getMeasuredHeight();
                        state = android.view.View.combineMeasuredStates(state, extraView.getMeasuredState());
                    }
                }
            }
        }
        boolean infoAnimating = hasInfoRegion() && (mInfoVisibility == android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED);
        mMeasuredHeight = ((int) (((mainHeight + (infoAnimating ? infoHeight * mInfoVisFraction : infoHeight)) + extraHeight) - (infoAnimating ? 0 : mInfoOffset)));
        // Report our final dimensions.
        setMeasuredDimension(android.view.View.resolveSizeAndState((mMeasuredWidth + getPaddingLeft()) + getPaddingRight(), widthMeasureSpec, state), android.view.View.resolveSizeAndState((mMeasuredHeight + getPaddingTop()) + getPaddingBottom(), heightMeasureSpec, state << android.view.View.MEASURED_HEIGHT_STATE_SHIFT));
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float currBottom = getPaddingTop();
        // MAIN is always present
        for (int i = 0; i < mMainViewList.size(); i++) {
            android.view.View mainView = mMainViewList.get(i);
            if (mainView.getVisibility() != android.view.View.GONE) {
                mainView.layout(getPaddingLeft(), ((int) (currBottom)), mMeasuredWidth + getPaddingLeft(), ((int) (currBottom + mainView.getMeasuredHeight())));
                currBottom += mainView.getMeasuredHeight();
            }
        }
        if (hasInfoRegion()) {
            float infoHeight = 0.0F;
            for (int i = 0; i < mInfoViewList.size(); i++) {
                infoHeight += mInfoViewList.get(i).getMeasuredHeight();
            }
            if (mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_OVER) {
                // retract currBottom to overlap the info views on top of main
                currBottom -= infoHeight;
                if (currBottom < 0) {
                    currBottom = 0;
                }
            } else
                if (mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER) {
                    if (mInfoVisibility == android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED) {
                        infoHeight = infoHeight * mInfoVisFraction;
                    }
                } else {
                    currBottom -= mInfoOffset;
                }

            for (int i = 0; i < mInfoViewList.size(); i++) {
                android.view.View infoView = mInfoViewList.get(i);
                if (infoView.getVisibility() != android.view.View.GONE) {
                    int viewHeight = infoView.getMeasuredHeight();
                    if (viewHeight > infoHeight) {
                        viewHeight = ((int) (infoHeight));
                    }
                    infoView.layout(getPaddingLeft(), ((int) (currBottom)), mMeasuredWidth + getPaddingLeft(), ((int) (currBottom + viewHeight)));
                    currBottom += viewHeight;
                    infoHeight -= viewHeight;
                    if (infoHeight <= 0) {
                        break;
                    }
                }
            }
            if (hasExtraRegion()) {
                for (int i = 0; i < mExtraViewList.size(); i++) {
                    android.view.View extraView = mExtraViewList.get(i);
                    if (extraView.getVisibility() != android.view.View.GONE) {
                        extraView.layout(getPaddingLeft(), ((int) (currBottom)), mMeasuredWidth + getPaddingLeft(), ((int) (currBottom + extraView.getMeasuredHeight())));
                        currBottom += extraView.getMeasuredHeight();
                    }
                }
            }
        }
        // Force update drawable bounds.
        onSizeChanged(0, 0, right - left, bottom - top);
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mAnimationTrigger);
        cancelAnimations();
        mInfoOffset = 0.0F;
        mInfoVisFraction = 0.0F;
    }

    private boolean hasInfoRegion() {
        return mCardType != android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_MAIN_ONLY;
    }

    private boolean hasExtraRegion() {
        return mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA;
    }

    private boolean isRegionVisible(int regionVisibility) {
        switch (regionVisibility) {
            case android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_ALWAYS :
                return true;
            case android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_ACTIVATED :
                return isActivated();
            case android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED :
                return isActivated() && isSelected();
            default :
                if (android.support.v17.leanback.widget.BaseCardView.DEBUG)
                    android.util.Log.e(android.support.v17.leanback.widget.BaseCardView.TAG, "invalid region visibility state: " + regionVisibility);

                return false;
        }
    }

    private void findChildrenViews() {
        mMainViewList.clear();
        mInfoViewList.clear();
        mExtraViewList.clear();
        final int count = getChildCount();
        boolean infoVisible = isRegionVisible(mInfoVisibility);
        boolean extraVisible = hasExtraRegion() && (mInfoOffset > 0.0F);
        if ((mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER) && (mInfoVisibility == android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED)) {
            infoVisible = infoVisible && (mInfoVisFraction > 0.0F);
        }
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            android.support.v17.leanback.widget.BaseCardView.LayoutParams lp = ((android.support.v17.leanback.widget.BaseCardView.LayoutParams) (child.getLayoutParams()));
            if (lp.viewType == android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_INFO) {
                mInfoViewList.add(child);
                child.setVisibility(infoVisible ? android.view.View.VISIBLE : android.view.View.GONE);
            } else
                if (lp.viewType == android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_EXTRA) {
                    mExtraViewList.add(child);
                    child.setVisibility(extraVisible ? android.view.View.VISIBLE : android.view.View.GONE);
                } else {
                    // Default to MAIN
                    mMainViewList.add(child);
                    child.setVisibility(android.view.View.VISIBLE);
                }

        }
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        // filter out focus states,  since leanback does not fade foreground on focus.
        final int[] s = super.onCreateDrawableState(extraSpace);
        final int N = s.length;
        boolean pressed = false;
        boolean enabled = false;
        for (int i = 0; i < N; i++) {
            if (s[i] == android.R.attr.state_pressed) {
                pressed = true;
            }
            if (s[i] == android.R.attr.state_enabled) {
                enabled = true;
            }
        }
        if (pressed && enabled) {
            return android.view.View.PRESSED_ENABLED_STATE_SET;
        } else
            if (pressed) {
                return android.support.v17.leanback.widget.BaseCardView.LB_PRESSED_STATE_SET;
            } else
                if (enabled) {
                    return android.view.View.ENABLED_STATE_SET;
                } else {
                    return android.view.View.EMPTY_STATE_SET;
                }


    }

    private void applyActiveState(boolean active) {
        if (hasInfoRegion() && (mInfoVisibility <= android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_ACTIVATED)) {
            setInfoViewVisibility(active);
        }
        if (hasExtraRegion() && (mExtraVisibility <= android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_ACTIVATED)) {
            // setExtraVisibility(active);
        }
    }

    private void setInfoViewVisibility(boolean visible) {
        if (mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA) {
            // Active state changes for card type
            // CARD_TYPE_INFO_UNDER_WITH_EXTRA
            if (visible) {
                for (int i = 0; i < mInfoViewList.size(); i++) {
                    mInfoViewList.get(i).setVisibility(android.view.View.VISIBLE);
                }
            } else {
                for (int i = 0; i < mInfoViewList.size(); i++) {
                    mInfoViewList.get(i).setVisibility(android.view.View.GONE);
                }
                for (int i = 0; i < mExtraViewList.size(); i++) {
                    mExtraViewList.get(i).setVisibility(android.view.View.GONE);
                }
                mInfoOffset = 0.0F;
            }
        } else
            if (mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER) {
                // Active state changes for card type CARD_TYPE_INFO_UNDER
                if (mInfoVisibility == android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED) {
                    animateInfoHeight(visible);
                } else {
                    for (int i = 0; i < mInfoViewList.size(); i++) {
                        mInfoViewList.get(i).setVisibility(visible ? android.view.View.VISIBLE : android.view.View.GONE);
                    }
                }
            } else
                if (mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_OVER) {
                    // Active state changes for card type CARD_TYPE_INFO_OVER
                    animateInfoAlpha(visible);
                }


    }

    private void applySelectedState(boolean focused) {
        removeCallbacks(mAnimationTrigger);
        if (mCardType == android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA) {
            // Focus changes for card type CARD_TYPE_INFO_UNDER_WITH_EXTRA
            if (focused) {
                if (!mDelaySelectedAnim) {
                    post(mAnimationTrigger);
                    mDelaySelectedAnim = true;
                } else {
                    postDelayed(mAnimationTrigger, mSelectedAnimationDelay);
                }
            } else {
                animateInfoOffset(false);
            }
        } else
            if (mInfoVisibility == android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED) {
                setInfoViewVisibility(focused);
            }

    }

    private void cancelAnimations() {
        if (mAnim != null) {
            mAnim.cancel();
            mAnim = null;
        }
    }

    // This animation changes the Y offset of the info and extra views,
    // so that they animate UP to make the extra info area visible when a
    // card is selected.
    void animateInfoOffset(boolean shown) {
        cancelAnimations();
        int extraHeight = 0;
        if (shown) {
            int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(mMeasuredWidth, android.view.View.MeasureSpec.EXACTLY);
            int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mExtraViewList.size(); i++) {
                android.view.View extraView = mExtraViewList.get(i);
                extraView.setVisibility(android.view.View.VISIBLE);
                extraView.measure(widthSpec, heightSpec);
                extraHeight = java.lang.Math.max(extraHeight, extraView.getMeasuredHeight());
            }
        }
        mAnim = new android.support.v17.leanback.widget.BaseCardView.InfoOffsetAnimation(mInfoOffset, shown ? extraHeight : 0);
        mAnim.setDuration(mSelectedAnimDuration);
        mAnim.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        mAnim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @java.lang.Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                if (mInfoOffset == 0.0F) {
                    for (int i = 0; i < mExtraViewList.size(); i++) {
                        mExtraViewList.get(i).setVisibility(android.view.View.GONE);
                    }
                }
            }

            @java.lang.Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }
        });
        startAnimation(mAnim);
    }

    // This animation changes the visible height of the info views,
    // so that they animate in and out of view.
    private void animateInfoHeight(boolean shown) {
        cancelAnimations();
        int extraHeight = 0;
        if (shown) {
            int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(mMeasuredWidth, android.view.View.MeasureSpec.EXACTLY);
            int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mExtraViewList.size(); i++) {
                android.view.View extraView = mExtraViewList.get(i);
                extraView.setVisibility(android.view.View.VISIBLE);
                extraView.measure(widthSpec, heightSpec);
                extraHeight = java.lang.Math.max(extraHeight, extraView.getMeasuredHeight());
            }
        }
        mAnim = new android.support.v17.leanback.widget.BaseCardView.InfoHeightAnimation(mInfoVisFraction, shown ? 1.0F : 0.0F);
        mAnim.setDuration(mSelectedAnimDuration);
        mAnim.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        mAnim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @java.lang.Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                if (mInfoOffset == 0.0F) {
                    for (int i = 0; i < mExtraViewList.size(); i++) {
                        mExtraViewList.get(i).setVisibility(android.view.View.GONE);
                    }
                }
            }

            @java.lang.Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }
        });
        startAnimation(mAnim);
    }

    // This animation changes the alpha of the info views, so they animate in
    // and out. It's meant to be used when the info views are overlaid on top of
    // the main view area. It gets triggered by a change in the Active state of
    // the card.
    private void animateInfoAlpha(boolean shown) {
        cancelAnimations();
        if (shown) {
            for (int i = 0; i < mInfoViewList.size(); i++) {
                mInfoViewList.get(i).setVisibility(android.view.View.VISIBLE);
            }
        }
        mAnim = new android.support.v17.leanback.widget.BaseCardView.InfoAlphaAnimation(mInfoAlpha, shown ? 1.0F : 0.0F);
        mAnim.setDuration(mActivatedAnimDuration);
        mAnim.setInterpolator(new android.view.animation.DecelerateInterpolator());
        mAnim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @java.lang.Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                if (mInfoAlpha == 0.0) {
                    for (int i = 0; i < mInfoViewList.size(); i++) {
                        mInfoViewList.get(i).setVisibility(android.view.View.GONE);
                    }
                }
            }

            @java.lang.Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }
        });
        startAnimation(mAnim);
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.BaseCardView.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.v17.leanback.widget.BaseCardView.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.BaseCardView.LayoutParams generateDefaultLayoutParams() {
        return new android.support.v17.leanback.widget.BaseCardView.LayoutParams(android.support.v17.leanback.widget.BaseCardView.LayoutParams.WRAP_CONTENT, android.support.v17.leanback.widget.BaseCardView.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.BaseCardView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (lp instanceof android.support.v17.leanback.widget.BaseCardView.LayoutParams) {
            return new android.support.v17.leanback.widget.BaseCardView.LayoutParams(((android.support.v17.leanback.widget.BaseCardView.LayoutParams) (lp)));
        } else {
            return new android.support.v17.leanback.widget.BaseCardView.LayoutParams(lp);
        }
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.support.v17.leanback.widget.BaseCardView.LayoutParams;
    }

    /**
     * Per-child layout information associated with BaseCardView.
     */
    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        public static final int VIEW_TYPE_MAIN = 0;

        public static final int VIEW_TYPE_INFO = 1;

        public static final int VIEW_TYPE_EXTRA = 2;

        /**
         * Card component type for the view associated with these LayoutParams.
         */
        @android.view.ViewDebug.ExportedProperty(category = "layout", mapping = { @android.view.ViewDebug.IntToString(from = android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_MAIN, to = "MAIN"), @android.view.ViewDebug.IntToString(from = android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_INFO, to = "INFO"), @android.view.ViewDebug.IntToString(from = android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_EXTRA, to = "EXTRA") })
        public int viewType = android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_MAIN;

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.lbBaseCardView_Layout);
            viewType = a.getInt(R.styleable.lbBaseCardView_Layout_layout_viewType, android.support.v17.leanback.widget.BaseCardView.LayoutParams.VIEW_TYPE_MAIN);
            a.recycle();
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * Copy constructor. Clones the width, height, and View Type of the
         * source.
         *
         * @param source
         * 		The layout params to copy from.
         */
        public LayoutParams(android.support.v17.leanback.widget.BaseCardView.LayoutParams source) {
            super(source);
            this.viewType = source.viewType;
        }
    }

    // Helper animation class used in the animation of the info and extra
    // fields vertically within the card
    private class InfoOffsetAnimation extends android.view.animation.Animation {
        private float mStartValue;

        private float mDelta;

        public InfoOffsetAnimation(float start, float end) {
            mStartValue = start;
            mDelta = end - start;
        }

        @java.lang.Override
        protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
            mInfoOffset = mStartValue + (interpolatedTime * mDelta);
            requestLayout();
        }
    }

    // Helper animation class used in the animation of the visible height
    // for the info fields.
    private class InfoHeightAnimation extends android.view.animation.Animation {
        private float mStartValue;

        private float mDelta;

        public InfoHeightAnimation(float start, float end) {
            mStartValue = start;
            mDelta = end - start;
        }

        @java.lang.Override
        protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
            mInfoVisFraction = mStartValue + (interpolatedTime * mDelta);
            requestLayout();
        }
    }

    // Helper animation class used to animate the alpha for the info views
    // when they are fading in or out of view.
    private class InfoAlphaAnimation extends android.view.animation.Animation {
        private float mStartValue;

        private float mDelta;

        public InfoAlphaAnimation(float start, float end) {
            mStartValue = start;
            mDelta = end - start;
        }

        @java.lang.Override
        protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
            mInfoAlpha = mStartValue + (interpolatedTime * mDelta);
            for (int i = 0; i < mInfoViewList.size(); i++) {
                mInfoViewList.get(i).setAlpha(mInfoAlpha);
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (android.support.v17.leanback.widget.BaseCardView.DEBUG) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(this.getClass().getSimpleName()).append(" : ");
            sb.append("cardType=");
            switch (mCardType) {
                case android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_MAIN_ONLY :
                    sb.append("MAIN_ONLY");
                    break;
                case android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_OVER :
                    sb.append("INFO_OVER");
                    break;
                case android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER :
                    sb.append("INFO_UNDER");
                    break;
                case android.support.v17.leanback.widget.BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA :
                    sb.append("INFO_UNDER_WITH_EXTRA");
                    break;
                default :
                    sb.append("INVALID");
                    break;
            }
            sb.append(" : ");
            sb.append(mMainViewList.size()).append(" main views, ");
            sb.append(mInfoViewList.size()).append(" info views, ");
            sb.append(mExtraViewList.size()).append(" extra views : ");
            sb.append("infoVisibility=").append(mInfoVisibility).append(" ");
            sb.append("extraVisibility=").append(mExtraVisibility).append(" ");
            sb.append("isActivated=").append(isActivated());
            sb.append(" : ");
            sb.append("isSelected=").append(isSelected());
            return sb.toString();
        } else {
            return super.toString();
        }
    }
}

