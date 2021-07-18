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
 * A Layout that arranges its children in a single column or a single row. The direction of
 * the row can be set by calling {@link #setOrientation(int) setOrientation()}.
 * You can also specify gravity, which specifies the alignment of all the child elements by
 * calling {@link #setGravity(int) setGravity()} or specify that specific children
 * grow to fill up any remaining space in the layout by setting the <em>weight</em> member of
 * {@link LinearLayoutCompat.LayoutParams LinearLayoutCompat.LayoutParams}.
 * The default orientation is horizontal.
 *
 * <p>See the <a href="{@docRoot }guide/topics/ui/layout/linear.html">Linear Layout</a>
 * guide.</p>
 *
 * <p>
 * Also see {@link LinearLayoutCompat.LayoutParams} for layout attributes </p>
 */
public class LinearLayoutCompat extends android.view.ViewGroup {
    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.v7.widget.LinearLayoutCompat.HORIZONTAL, android.support.v7.widget.LinearLayoutCompat.VERTICAL })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface OrientationMode {}

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef(flag = true, value = { android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_NONE, android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_BEGINNING, android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_MIDDLE, android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_END })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DividerMode {}

    /**
     * Don't show any dividers.
     */
    public static final int SHOW_DIVIDER_NONE = 0;

    /**
     * Show a divider at the beginning of the group.
     */
    public static final int SHOW_DIVIDER_BEGINNING = 1;

    /**
     * Show dividers between each item in the group.
     */
    public static final int SHOW_DIVIDER_MIDDLE = 2;

    /**
     * Show a divider at the end of the group.
     */
    public static final int SHOW_DIVIDER_END = 4;

    /**
     * Whether the children of this layout are baseline aligned.  Only applicable
     * if {@link #mOrientation} is horizontal.
     */
    private boolean mBaselineAligned = true;

    /**
     * If this layout is part of another layout that is baseline aligned,
     * use the child at this index as the baseline.
     *
     * Note: this is orthogonal to {@link #mBaselineAligned}, which is concerned
     * with whether the children of this layout are baseline aligned.
     */
    private int mBaselineAlignedChildIndex = -1;

    /**
     * The additional offset to the child's baseline.
     * We'll calculate the baseline of this layout as we measure vertically; for
     * horizontal linear layouts, the offset of 0 is appropriate.
     */
    private int mBaselineChildTop = 0;

    private int mOrientation;

    private int mGravity = android.support.v4.view.GravityCompat.START | android.view.Gravity.TOP;

    private int mTotalLength;

    private float mWeightSum;

    private boolean mUseLargestChild;

    private int[] mMaxAscent;

    private int[] mMaxDescent;

    private static final int VERTICAL_GRAVITY_COUNT = 4;

    private static final int INDEX_CENTER_VERTICAL = 0;

    private static final int INDEX_TOP = 1;

    private static final int INDEX_BOTTOM = 2;

    private static final int INDEX_FILL = 3;

    private android.graphics.drawable.Drawable mDivider;

    private int mDividerWidth;

    private int mDividerHeight;

    private int mShowDividers;

    private int mDividerPadding;

    public LinearLayoutCompat(android.content.Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutCompat(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.LinearLayoutCompat, defStyleAttr, 0);
        int index = a.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }
        index = a.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (index >= 0) {
            setGravity(index);
        }
        boolean baselineAligned = a.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!baselineAligned) {
            setBaselineAligned(baselineAligned);
        }
        mWeightSum = a.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
        mBaselineAlignedChildIndex = a.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        mUseLargestChild = a.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(a.getDrawable(R.styleable.LinearLayoutCompat_divider));
        mShowDividers = a.getInt(R.styleable.LinearLayoutCompat_showDividers, android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_NONE);
        mDividerPadding = a.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        a.recycle();
    }

    /**
     * Set how dividers should be shown between items in this layout
     *
     * @param showDividers
     * 		One or more of {@link #SHOW_DIVIDER_BEGINNING},
     * 		{@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
     * 		or {@link #SHOW_DIVIDER_NONE} to show no dividers.
     */
    public void setShowDividers(@android.support.v7.widget.LinearLayoutCompat.DividerMode
    int showDividers) {
        if (showDividers != mShowDividers) {
            requestLayout();
        }
        mShowDividers = showDividers;
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     *
     *
     * @return A flag set indicating how dividers should be shown around items.
     * @see #setShowDividers(int)
     */
    @android.support.v7.widget.LinearLayoutCompat.DividerMode
    public int getShowDividers() {
        return mShowDividers;
    }

    /**
     *
     *
     * @return the divider Drawable that will divide each item.
     * @see #setDividerDrawable(Drawable)
     */
    public android.graphics.drawable.Drawable getDividerDrawable() {
        return mDivider;
    }

    /**
     * Set a drawable to be used as a divider between items.
     *
     * @param divider
     * 		Drawable that will divide each item.
     * @see #setShowDividers(int)
     */
    public void setDividerDrawable(android.graphics.drawable.Drawable divider) {
        if (divider == mDivider) {
            return;
        }
        mDivider = divider;
        if (divider != null) {
            mDividerWidth = divider.getIntrinsicWidth();
            mDividerHeight = divider.getIntrinsicHeight();
        } else {
            mDividerWidth = 0;
            mDividerHeight = 0;
        }
        setWillNotDraw(divider == null);
        requestLayout();
    }

    /**
     * Set padding displayed on both ends of dividers.
     *
     * @param padding
     * 		Padding value in pixels that will be applied to each end
     * @see #setShowDividers(int)
     * @see #setDividerDrawable(Drawable)
     * @see #getDividerPadding()
     */
    public void setDividerPadding(int padding) {
        mDividerPadding = padding;
    }

    /**
     * Get the padding size used to inset dividers in pixels
     *
     * @see #setShowDividers(int)
     * @see #setDividerDrawable(Drawable)
     * @see #setDividerPadding(int)
     */
    public int getDividerPadding() {
        return mDividerPadding;
    }

    /**
     * Get the width of the current divider drawable.
     *
     * @unknown Used internally by framework.
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public int getDividerWidth() {
        return mDividerWidth;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        if (mDivider == null) {
            return;
        }
        if (mOrientation == android.support.v7.widget.LinearLayoutCompat.VERTICAL) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    void drawDividersVertical(android.graphics.Canvas canvas) {
        final int count = getVirtualChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getVirtualChildAt(i);
            if ((child != null) && (child.getVisibility() != android.view.View.GONE)) {
                if (hasDividerBeforeChildAt(i)) {
                    final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                    final int top = (child.getTop() - lp.topMargin) - mDividerHeight;
                    drawHorizontalDivider(canvas, top);
                }
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            final android.view.View child = getVirtualChildAt(count - 1);
            int bottom = 0;
            if (child == null) {
                bottom = (getHeight() - getPaddingBottom()) - mDividerHeight;
            } else {
                final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                bottom = child.getBottom() + lp.bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    void drawDividersHorizontal(android.graphics.Canvas canvas) {
        final int count = getVirtualChildCount();
        final boolean isLayoutRtl = android.support.v7.widget.ViewUtils.isLayoutRtl(this);
        for (int i = 0; i < count; i++) {
            final android.view.View child = getVirtualChildAt(i);
            if ((child != null) && (child.getVisibility() != android.view.View.GONE)) {
                if (hasDividerBeforeChildAt(i)) {
                    final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                    final int position;
                    if (isLayoutRtl) {
                        position = child.getRight() + lp.rightMargin;
                    } else {
                        position = (child.getLeft() - lp.leftMargin) - mDividerWidth;
                    }
                    drawVerticalDivider(canvas, position);
                }
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            final android.view.View child = getVirtualChildAt(count - 1);
            int position;
            if (child == null) {
                if (isLayoutRtl) {
                    position = getPaddingLeft();
                } else {
                    position = (getWidth() - getPaddingRight()) - mDividerWidth;
                }
            } else {
                final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                if (isLayoutRtl) {
                    position = (child.getLeft() - lp.leftMargin) - mDividerWidth;
                } else {
                    position = child.getRight() + lp.rightMargin;
                }
            }
            drawVerticalDivider(canvas, position);
        }
    }

    void drawHorizontalDivider(android.graphics.Canvas canvas, int top) {
        mDivider.setBounds(getPaddingLeft() + mDividerPadding, top, (getWidth() - getPaddingRight()) - mDividerPadding, top + mDividerHeight);
        mDivider.draw(canvas);
    }

    void drawVerticalDivider(android.graphics.Canvas canvas, int left) {
        mDivider.setBounds(left, getPaddingTop() + mDividerPadding, left + mDividerWidth, (getHeight() - getPaddingBottom()) - mDividerPadding);
        mDivider.draw(canvas);
    }

    /**
     * <p>Indicates whether widgets contained within this layout are aligned
     * on their baseline or not.</p>
     *
     * @return true when widgets are baseline-aligned, false otherwise
     */
    public boolean isBaselineAligned() {
        return mBaselineAligned;
    }

    /**
     * <p>Defines whether widgets contained in this layout are
     * baseline-aligned or not.</p>
     *
     * @param baselineAligned
     * 		true to align widgets on their baseline,
     * 		false otherwise
     */
    public void setBaselineAligned(boolean baselineAligned) {
        mBaselineAligned = baselineAligned;
    }

    /**
     * When true, all children with a weight will be considered having
     * the minimum size of the largest child. If false, all children are
     * measured normally.
     *
     * @return True to measure children with a weight using the minimum
    size of the largest child, false otherwise.
     */
    public boolean isMeasureWithLargestChildEnabled() {
        return mUseLargestChild;
    }

    /**
     * When set to true, all children with a weight will be considered having
     * the minimum size of the largest child. If false, all children are
     * measured normally.
     *
     * Disabled by default.
     *
     * @param enabled
     * 		True to measure children with a weight using the
     * 		minimum size of the largest child, false otherwise.
     */
    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        mUseLargestChild = enabled;
    }

    @java.lang.Override
    public int getBaseline() {
        if (mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        if (getChildCount() <= mBaselineAlignedChildIndex) {
            throw new java.lang.RuntimeException("mBaselineAlignedChildIndex of LinearLayout " + "set to an index that is out of bounds.");
        }
        final android.view.View child = getChildAt(mBaselineAlignedChildIndex);
        final int childBaseline = child.getBaseline();
        if (childBaseline == (-1)) {
            if (mBaselineAlignedChildIndex == 0) {
                // this is just the default case, safe to return -1
                return -1;
            }
            // the user picked an index that points to something that doesn't
            // know how to calculate its baseline.
            throw new java.lang.RuntimeException("mBaselineAlignedChildIndex of LinearLayout " + "points to a View that doesn't know how to get its baseline.");
        }
        // TODO: This should try to take into account the virtual offsets
        // (See getNextLocationOffset and getLocationOffset)
        // We should add to childTop:
        // sum([getNextLocationOffset(getChildAt(i)) / i < mBaselineAlignedChildIndex])
        // and also add:
        // getLocationOffset(child)
        int childTop = mBaselineChildTop;
        if (mOrientation == android.support.v7.widget.LinearLayoutCompat.VERTICAL) {
            final int majorGravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
            if (majorGravity != android.view.Gravity.TOP) {
                switch (majorGravity) {
                    case android.view.Gravity.BOTTOM :
                        childTop = ((getBottom() - getTop()) - getPaddingBottom()) - mTotalLength;
                        break;
                    case android.view.Gravity.CENTER_VERTICAL :
                        childTop += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - mTotalLength) / 2;
                        break;
                }
            }
        }
        android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
        return (childTop + lp.topMargin) + childBaseline;
    }

    /**
     *
     *
     * @return The index of the child that will be used if this layout is
    part of a larger layout that is baseline aligned, or -1 if none has
    been set.
     */
    public int getBaselineAlignedChildIndex() {
        return mBaselineAlignedChildIndex;
    }

    /**
     *
     *
     * @param i
     * 		The index of the child that will be used if this layout is
     * 		part of a larger layout that is baseline aligned.
     */
    public void setBaselineAlignedChildIndex(int i) {
        if ((i < 0) || (i >= getChildCount())) {
            throw new java.lang.IllegalArgumentException((("base aligned child index out " + "of range (0, ") + getChildCount()) + ")");
        }
        mBaselineAlignedChildIndex = i;
    }

    /**
     * <p>Returns the view at the specified index. This method can be overridden
     * to take into account virtual children. Refer to
     * {@link android.widget.TableLayout} and {@link android.widget.TableRow}
     * for an example.</p>
     *
     * @param index
     * 		the child's index
     * @return the child at the specified index
     */
    android.view.View getVirtualChildAt(int index) {
        return getChildAt(index);
    }

    /**
     * <p>Returns the virtual number of children. This number might be different
     * than the actual number of children if the layout can hold virtual
     * children. Refer to
     * {@link android.widget.TableLayout} and {@link android.widget.TableRow}
     * for an example.</p>
     *
     * @return the virtual number of children
     */
    int getVirtualChildCount() {
        return getChildCount();
    }

    /**
     * Returns the desired weights sum.
     *
     * @return A number greater than 0.0f if the weight sum is defined, or
    a number lower than or equals to 0.0f if not weight sum is
    to be used.
     */
    public float getWeightSum() {
        return mWeightSum;
    }

    /**
     * Defines the desired weights sum. If unspecified the weights sum is computed
     * at layout time by adding the layout_weight of each child.
     *
     * This can be used for instance to give a single child 50% of the total
     * available space by giving it a layout_weight of 0.5 and setting the
     * weightSum to 1.0.
     *
     * @param weightSum
     * 		a number greater than 0.0f, or a number lower than or equals
     * 		to 0.0f if the weight sum should be computed from the children's
     * 		layout_weight
     */
    public void setWeightSum(float weightSum) {
        mWeightSum = java.lang.Math.max(0.0F, weightSum);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == android.support.v7.widget.LinearLayoutCompat.VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Determines where to position dividers between children.
     *
     * @param childIndex
     * 		Index of child to check for preceding divider
     * @return true if there should be a divider before the child at childIndex
     * @unknown Pending API consideration. Currently only used internally by the system.
     */
    protected boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return (mShowDividers & android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_BEGINNING) != 0;
        } else
            if (childIndex == getChildCount()) {
                return (mShowDividers & android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_END) != 0;
            } else
                if ((mShowDividers & android.support.v7.widget.LinearLayoutCompat.SHOW_DIVIDER_MIDDLE) != 0) {
                    boolean hasVisibleViewBefore = false;
                    for (int i = childIndex - 1; i >= 0; i--) {
                        if (getChildAt(i).getVisibility() != android.view.View.GONE) {
                            hasVisibleViewBefore = true;
                            break;
                        }
                    }
                    return hasVisibleViewBefore;
                }


        return false;
    }

    /**
     * Measures the children when the orientation of this LinearLayout is set
     * to {@link #VERTICAL}.
     *
     * @param widthMeasureSpec
     * 		Horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec
     * 		Vertical space requirements as imposed by the parent.
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @see #onMeasure(int, int)
     */
    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;
        int maxWidth = 0;
        int childState = 0;
        int alternativeMaxWidth = 0;
        int weightedMaxWidth = 0;
        boolean allFillParent = true;
        float totalWeight = 0;
        final int count = getVirtualChildCount();
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        boolean matchWidth = false;
        boolean skippedMeasure = false;
        final int baselineChildIndex = mBaselineAlignedChildIndex;
        final boolean useLargestChild = mUseLargestChild;
        int largestChildHeight = java.lang.Integer.MIN_VALUE;
        // See how tall everyone is. Also remember max width.
        for (int i = 0; i < count; ++i) {
            final android.view.View child = getVirtualChildAt(i);
            if (child == null) {
                mTotalLength += measureNullChild(i);
                continue;
            }
            if (child.getVisibility() == android.view.View.GONE) {
                i += getChildrenSkipCount(child, i);
                continue;
            }
            if (hasDividerBeforeChildAt(i)) {
                mTotalLength += mDividerHeight;
            }
            android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
            totalWeight += lp.weight;
            if (((heightMode == android.view.View.MeasureSpec.EXACTLY) && (lp.height == 0)) && (lp.weight > 0)) {
                // Optimization: don't bother measuring children who are going to use
                // leftover space. These views will get measured again down below if
                // there is any leftover space.
                final int totalLength = mTotalLength;
                mTotalLength = java.lang.Math.max(totalLength, (totalLength + lp.topMargin) + lp.bottomMargin);
                skippedMeasure = true;
            } else {
                int oldHeight = java.lang.Integer.MIN_VALUE;
                if ((lp.height == 0) && (lp.weight > 0)) {
                    // heightMode is either UNSPECIFIED or AT_MOST, and this
                    // child wanted to stretch to fill available space.
                    // Translate that to WRAP_CONTENT so that it does not end up
                    // with a height of 0
                    oldHeight = 0;
                    lp.height = android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                }
                // Determine how big this child would like to be. If this or
                // previous children have given a weight, then we allow it to
                // use all available space (and we will shrink things later
                // if needed).
                measureChildBeforeLayout(child, i, widthMeasureSpec, 0, heightMeasureSpec, totalWeight == 0 ? mTotalLength : 0);
                if (oldHeight != java.lang.Integer.MIN_VALUE) {
                    lp.height = oldHeight;
                }
                final int childHeight = child.getMeasuredHeight();
                final int totalLength = mTotalLength;
                mTotalLength = java.lang.Math.max(totalLength, (((totalLength + childHeight) + lp.topMargin) + lp.bottomMargin) + getNextLocationOffset(child));
                if (useLargestChild) {
                    largestChildHeight = java.lang.Math.max(childHeight, largestChildHeight);
                }
            }
            /**
             * If applicable, compute the additional offset to the child's baseline
             * we'll need later when asked {@link #getBaseline}.
             */
            if ((baselineChildIndex >= 0) && (baselineChildIndex == (i + 1))) {
                mBaselineChildTop = mTotalLength;
            }
            // if we are trying to use a child index for our baseline, the above
            // book keeping only works if there are no children above it with
            // weight.  fail fast to aid the developer.
            if ((i < baselineChildIndex) && (lp.weight > 0)) {
                throw new java.lang.RuntimeException("A child of LinearLayout with index " + (("less than mBaselineAlignedChildIndex has weight > 0, which " + "won't work.  Either remove the weight, or don't set ") + "mBaselineAlignedChildIndex."));
            }
            boolean matchWidthLocally = false;
            if ((widthMode != android.view.View.MeasureSpec.EXACTLY) && (lp.width == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT)) {
                // The width of the linear layout will scale, and at least one
                // child said it wanted to match our width. Set a flag
                // indicating that we need to remeasure at least that view when
                // we know our width.
                matchWidth = true;
                matchWidthLocally = true;
            }
            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getMeasuredWidth() + margin;
            maxWidth = java.lang.Math.max(maxWidth, measuredWidth);
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child));
            allFillParent = allFillParent && (lp.width == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            if (lp.weight > 0) {
                /* Widths of weighted Views are bogus if we end up
                remeasuring, so keep them separate.
                 */
                weightedMaxWidth = java.lang.Math.max(weightedMaxWidth, matchWidthLocally ? margin : measuredWidth);
            } else {
                alternativeMaxWidth = java.lang.Math.max(alternativeMaxWidth, matchWidthLocally ? margin : measuredWidth);
            }
            i += getChildrenSkipCount(child, i);
        }
        if ((mTotalLength > 0) && hasDividerBeforeChildAt(count)) {
            mTotalLength += mDividerHeight;
        }
        if (useLargestChild && ((heightMode == android.view.View.MeasureSpec.AT_MOST) || (heightMode == android.view.View.MeasureSpec.UNSPECIFIED))) {
            mTotalLength = 0;
            for (int i = 0; i < count; ++i) {
                final android.view.View child = getVirtualChildAt(i);
                if (child == null) {
                    mTotalLength += measureNullChild(i);
                    continue;
                }
                if (child.getVisibility() == android.view.View.GONE) {
                    i += getChildrenSkipCount(child, i);
                    continue;
                }
                final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                // Account for negative margins
                final int totalLength = mTotalLength;
                mTotalLength = java.lang.Math.max(totalLength, (((totalLength + largestChildHeight) + lp.topMargin) + lp.bottomMargin) + getNextLocationOffset(child));
            }
        }
        // Add in our padding
        mTotalLength += getPaddingTop() + getPaddingBottom();
        int heightSize = mTotalLength;
        // Check against our minimum height
        heightSize = java.lang.Math.max(heightSize, getSuggestedMinimumHeight());
        // Reconcile our calculated size with the heightMeasureSpec
        int heightSizeAndState = android.support.v4.view.ViewCompat.resolveSizeAndState(heightSize, heightMeasureSpec, 0);
        heightSize = heightSizeAndState & android.support.v4.view.ViewCompat.MEASURED_SIZE_MASK;
        // Either expand children with weight to take up available space or
        // shrink them if they extend beyond our current bounds. If we skipped
        // measurement on any children, we need to measure them now.
        int delta = heightSize - mTotalLength;
        if (skippedMeasure || ((delta != 0) && (totalWeight > 0.0F))) {
            float weightSum = (mWeightSum > 0.0F) ? mWeightSum : totalWeight;
            mTotalLength = 0;
            for (int i = 0; i < count; ++i) {
                final android.view.View child = getVirtualChildAt(i);
                if (child.getVisibility() == android.view.View.GONE) {
                    continue;
                }
                android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                float childExtra = lp.weight;
                if (childExtra > 0) {
                    // Child said it could absorb extra space -- give him his share
                    int share = ((int) ((childExtra * delta) / weightSum));
                    weightSum -= childExtra;
                    delta -= share;
                    final int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, ((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin, lp.width);
                    // TODO: Use a field like lp.isMeasured to figure out if this
                    // child has been previously measured
                    if ((lp.height != 0) || (heightMode != android.view.View.MeasureSpec.EXACTLY)) {
                        // child was measured once already above...
                        // base new measurement on stored values
                        int childHeight = child.getMeasuredHeight() + share;
                        if (childHeight < 0) {
                            childHeight = 0;
                        }
                        child.measure(childWidthMeasureSpec, android.view.View.MeasureSpec.makeMeasureSpec(childHeight, android.view.View.MeasureSpec.EXACTLY));
                    } else {
                        // child was skipped in the loop above.
                        // Measure for this first time here
                        child.measure(childWidthMeasureSpec, android.view.View.MeasureSpec.makeMeasureSpec(share > 0 ? share : 0, android.view.View.MeasureSpec.EXACTLY));
                    }
                    // Child may now not fit in vertical dimension.
                    childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child) & (android.support.v4.view.ViewCompat.MEASURED_STATE_MASK >> android.support.v4.view.ViewCompat.MEASURED_HEIGHT_STATE_SHIFT));
                }
                final int margin = lp.leftMargin + lp.rightMargin;
                final int measuredWidth = child.getMeasuredWidth() + margin;
                maxWidth = java.lang.Math.max(maxWidth, measuredWidth);
                boolean matchWidthLocally = (widthMode != android.view.View.MeasureSpec.EXACTLY) && (lp.width == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                alternativeMaxWidth = java.lang.Math.max(alternativeMaxWidth, matchWidthLocally ? margin : measuredWidth);
                allFillParent = allFillParent && (lp.width == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                final int totalLength = mTotalLength;
                mTotalLength = java.lang.Math.max(totalLength, (((totalLength + child.getMeasuredHeight()) + lp.topMargin) + lp.bottomMargin) + getNextLocationOffset(child));
            }
            // Add in our padding
            mTotalLength += getPaddingTop() + getPaddingBottom();
            // TODO: Should we recompute the heightSpec based on the new total length?
        } else {
            alternativeMaxWidth = java.lang.Math.max(alternativeMaxWidth, weightedMaxWidth);
            // We have no limit, so make all weighted views as tall as the largest child.
            // Children will have already been measured once.
            if (useLargestChild && (heightMode != android.view.View.MeasureSpec.EXACTLY)) {
                for (int i = 0; i < count; i++) {
                    final android.view.View child = getVirtualChildAt(i);
                    if ((child == null) || (child.getVisibility() == android.view.View.GONE)) {
                        continue;
                    }
                    final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                    float childExtra = lp.weight;
                    if (childExtra > 0) {
                        child.measure(android.view.View.MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(largestChildHeight, android.view.View.MeasureSpec.EXACTLY));
                    }
                }
            }
        }
        if ((!allFillParent) && (widthMode != android.view.View.MeasureSpec.EXACTLY)) {
            maxWidth = alternativeMaxWidth;
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        // Check against our minimum width
        maxWidth = java.lang.Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(android.support.v4.view.ViewCompat.resolveSizeAndState(maxWidth, widthMeasureSpec, childState), heightSizeAndState);
        if (matchWidth) {
            forceUniformWidth(count, heightMeasureSpec);
        }
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        // Pretend that the linear layout has an exact size.
        int uniformMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), android.view.View.MeasureSpec.EXACTLY);
        for (int i = 0; i < count; ++i) {
            final android.view.View child = getVirtualChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                if (lp.width == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured height
                    // FIXME: this may not be right for something like wrapping text?
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    // Remeasue with new dimensions
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    /**
     * Measures the children when the orientation of this LinearLayout is set
     * to {@link #HORIZONTAL}.
     *
     * @param widthMeasureSpec
     * 		Horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec
     * 		Vertical space requirements as imposed by the parent.
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @see #onMeasure(int, int)
     */
    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;
        int maxHeight = 0;
        int childState = 0;
        int alternativeMaxHeight = 0;
        int weightedMaxHeight = 0;
        boolean allFillParent = true;
        float totalWeight = 0;
        final int count = getVirtualChildCount();
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        boolean matchHeight = false;
        boolean skippedMeasure = false;
        if ((mMaxAscent == null) || (mMaxDescent == null)) {
            mMaxAscent = new int[android.support.v7.widget.LinearLayoutCompat.VERTICAL_GRAVITY_COUNT];
            mMaxDescent = new int[android.support.v7.widget.LinearLayoutCompat.VERTICAL_GRAVITY_COUNT];
        }
        final int[] maxAscent = mMaxAscent;
        final int[] maxDescent = mMaxDescent;
        maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
        maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
        final boolean baselineAligned = mBaselineAligned;
        final boolean useLargestChild = mUseLargestChild;
        final boolean isExactly = widthMode == android.view.View.MeasureSpec.EXACTLY;
        int largestChildWidth = java.lang.Integer.MIN_VALUE;
        // See how wide everyone is. Also remember max height.
        for (int i = 0; i < count; ++i) {
            final android.view.View child = getVirtualChildAt(i);
            if (child == null) {
                mTotalLength += measureNullChild(i);
                continue;
            }
            if (child.getVisibility() == android.view.View.GONE) {
                i += getChildrenSkipCount(child, i);
                continue;
            }
            if (hasDividerBeforeChildAt(i)) {
                mTotalLength += mDividerWidth;
            }
            final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
            totalWeight += lp.weight;
            if (((widthMode == android.view.View.MeasureSpec.EXACTLY) && (lp.width == 0)) && (lp.weight > 0)) {
                // Optimization: don't bother measuring children who are going to use
                // leftover space. These views will get measured again down below if
                // there is any leftover space.
                if (isExactly) {
                    mTotalLength += lp.leftMargin + lp.rightMargin;
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = java.lang.Math.max(totalLength, (totalLength + lp.leftMargin) + lp.rightMargin);
                }
                // Baseline alignment requires to measure widgets to obtain the
                // baseline offset (in particular for TextViews). The following
                // defeats the optimization mentioned above. Allow the child to
                // use as much space as it wants because we can shrink things
                // later (and re-measure).
                if (baselineAligned) {
                    final int freeSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
                    child.measure(freeSpec, freeSpec);
                } else {
                    skippedMeasure = true;
                }
            } else {
                int oldWidth = java.lang.Integer.MIN_VALUE;
                if ((lp.width == 0) && (lp.weight > 0)) {
                    // widthMode is either UNSPECIFIED or AT_MOST, and this
                    // child
                    // wanted to stretch to fill available space. Translate that to
                    // WRAP_CONTENT so that it does not end up with a width of 0
                    oldWidth = 0;
                    lp.width = android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                }
                // Determine how big this child would like to be. If this or
                // previous children have given a weight, then we allow it to
                // use all available space (and we will shrink things later
                // if needed).
                measureChildBeforeLayout(child, i, widthMeasureSpec, totalWeight == 0 ? mTotalLength : 0, heightMeasureSpec, 0);
                if (oldWidth != java.lang.Integer.MIN_VALUE) {
                    lp.width = oldWidth;
                }
                final int childWidth = child.getMeasuredWidth();
                if (isExactly) {
                    mTotalLength += ((childWidth + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child);
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = java.lang.Math.max(totalLength, (((totalLength + childWidth) + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child));
                }
                if (useLargestChild) {
                    largestChildWidth = java.lang.Math.max(childWidth, largestChildWidth);
                }
            }
            boolean matchHeightLocally = false;
            if ((heightMode != android.view.View.MeasureSpec.EXACTLY) && (lp.height == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT)) {
                // The height of the linear layout will scale, and at least one
                // child said it wanted to match our height. Set a flag indicating that
                // we need to remeasure at least that view when we know our height.
                matchHeight = true;
                matchHeightLocally = true;
            }
            final int margin = lp.topMargin + lp.bottomMargin;
            final int childHeight = child.getMeasuredHeight() + margin;
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child));
            if (baselineAligned) {
                final int childBaseline = child.getBaseline();
                if (childBaseline != (-1)) {
                    // Translates the child's vertical gravity into an index
                    // in the range 0..VERTICAL_GRAVITY_COUNT
                    final int gravity = (lp.gravity < 0 ? mGravity : lp.gravity) & android.view.Gravity.VERTICAL_GRAVITY_MASK;
                    final int index = ((gravity >> android.view.Gravity.AXIS_Y_SHIFT) & (~android.view.Gravity.AXIS_SPECIFIED)) >> 1;
                    maxAscent[index] = java.lang.Math.max(maxAscent[index], childBaseline);
                    maxDescent[index] = java.lang.Math.max(maxDescent[index], childHeight - childBaseline);
                }
            }
            maxHeight = java.lang.Math.max(maxHeight, childHeight);
            allFillParent = allFillParent && (lp.height == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            if (lp.weight > 0) {
                /* Heights of weighted Views are bogus if we end up
                remeasuring, so keep them separate.
                 */
                weightedMaxHeight = java.lang.Math.max(weightedMaxHeight, matchHeightLocally ? margin : childHeight);
            } else {
                alternativeMaxHeight = java.lang.Math.max(alternativeMaxHeight, matchHeightLocally ? margin : childHeight);
            }
            i += getChildrenSkipCount(child, i);
        }
        if ((mTotalLength > 0) && hasDividerBeforeChildAt(count)) {
            mTotalLength += mDividerWidth;
        }
        // Check mMaxAscent[INDEX_TOP] first because it maps to Gravity.TOP,
        // the most common case
        if ((((maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP] != (-1)) || (maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_CENTER_VERTICAL] != (-1))) || (maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM] != (-1))) || (maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_FILL] != (-1))) {
            final int ascent = java.lang.Math.max(maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_FILL], java.lang.Math.max(maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_CENTER_VERTICAL], java.lang.Math.max(maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP], maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM])));
            final int descent = java.lang.Math.max(maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_FILL], java.lang.Math.max(maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_CENTER_VERTICAL], java.lang.Math.max(maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP], maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM])));
            maxHeight = java.lang.Math.max(maxHeight, ascent + descent);
        }
        if (useLargestChild && ((widthMode == android.view.View.MeasureSpec.AT_MOST) || (widthMode == android.view.View.MeasureSpec.UNSPECIFIED))) {
            mTotalLength = 0;
            for (int i = 0; i < count; ++i) {
                final android.view.View child = getVirtualChildAt(i);
                if (child == null) {
                    mTotalLength += measureNullChild(i);
                    continue;
                }
                if (child.getVisibility() == android.view.View.GONE) {
                    i += getChildrenSkipCount(child, i);
                    continue;
                }
                final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                if (isExactly) {
                    mTotalLength += ((largestChildWidth + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child);
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = java.lang.Math.max(totalLength, (((totalLength + largestChildWidth) + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child));
                }
            }
        }
        // Add in our padding
        mTotalLength += getPaddingLeft() + getPaddingRight();
        int widthSize = mTotalLength;
        // Check against our minimum width
        widthSize = java.lang.Math.max(widthSize, getSuggestedMinimumWidth());
        // Reconcile our calculated size with the widthMeasureSpec
        int widthSizeAndState = android.support.v4.view.ViewCompat.resolveSizeAndState(widthSize, widthMeasureSpec, 0);
        widthSize = widthSizeAndState & android.support.v4.view.ViewCompat.MEASURED_SIZE_MASK;
        // Either expand children with weight to take up available space or
        // shrink them if they extend beyond our current bounds. If we skipped
        // measurement on any children, we need to measure them now.
        int delta = widthSize - mTotalLength;
        if (skippedMeasure || ((delta != 0) && (totalWeight > 0.0F))) {
            float weightSum = (mWeightSum > 0.0F) ? mWeightSum : totalWeight;
            maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
            maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
            maxHeight = -1;
            mTotalLength = 0;
            for (int i = 0; i < count; ++i) {
                final android.view.View child = getVirtualChildAt(i);
                if ((child == null) || (child.getVisibility() == android.view.View.GONE)) {
                    continue;
                }
                final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                float childExtra = lp.weight;
                if (childExtra > 0) {
                    // Child said it could absorb extra space -- give him his share
                    int share = ((int) ((childExtra * delta) / weightSum));
                    weightSum -= childExtra;
                    delta -= share;
                    final int childHeightMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(heightMeasureSpec, ((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin, lp.height);
                    // TODO: Use a field like lp.isMeasured to figure out if this
                    // child has been previously measured
                    if ((lp.width != 0) || (widthMode != android.view.View.MeasureSpec.EXACTLY)) {
                        // child was measured once already above ... base new measurement
                        // on stored values
                        int childWidth = child.getMeasuredWidth() + share;
                        if (childWidth < 0) {
                            childWidth = 0;
                        }
                        child.measure(android.view.View.MeasureSpec.makeMeasureSpec(childWidth, android.view.View.MeasureSpec.EXACTLY), childHeightMeasureSpec);
                    } else {
                        // child was skipped in the loop above. Measure for this first time here
                        child.measure(android.view.View.MeasureSpec.makeMeasureSpec(share > 0 ? share : 0, android.view.View.MeasureSpec.EXACTLY), childHeightMeasureSpec);
                    }
                    // Child may now not fit in horizontal dimension.
                    childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child) & android.support.v4.view.ViewCompat.MEASURED_STATE_MASK);
                }
                if (isExactly) {
                    mTotalLength += ((child.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child);
                } else {
                    final int totalLength = mTotalLength;
                    mTotalLength = java.lang.Math.max(totalLength, (((totalLength + child.getMeasuredWidth()) + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child));
                }
                boolean matchHeightLocally = (heightMode != android.view.View.MeasureSpec.EXACTLY) && (lp.height == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                final int margin = lp.topMargin + lp.bottomMargin;
                int childHeight = child.getMeasuredHeight() + margin;
                maxHeight = java.lang.Math.max(maxHeight, childHeight);
                alternativeMaxHeight = java.lang.Math.max(alternativeMaxHeight, matchHeightLocally ? margin : childHeight);
                allFillParent = allFillParent && (lp.height == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                if (baselineAligned) {
                    final int childBaseline = child.getBaseline();
                    if (childBaseline != (-1)) {
                        // Translates the child's vertical gravity into an index in the range 0..2
                        final int gravity = (lp.gravity < 0 ? mGravity : lp.gravity) & android.view.Gravity.VERTICAL_GRAVITY_MASK;
                        final int index = ((gravity >> android.view.Gravity.AXIS_Y_SHIFT) & (~android.view.Gravity.AXIS_SPECIFIED)) >> 1;
                        maxAscent[index] = java.lang.Math.max(maxAscent[index], childBaseline);
                        maxDescent[index] = java.lang.Math.max(maxDescent[index], childHeight - childBaseline);
                    }
                }
            }
            // Add in our padding
            mTotalLength += getPaddingLeft() + getPaddingRight();
            // TODO: Should we update widthSize with the new total length?
            // Check mMaxAscent[INDEX_TOP] first because it maps to Gravity.TOP,
            // the most common case
            if ((((maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP] != (-1)) || (maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_CENTER_VERTICAL] != (-1))) || (maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM] != (-1))) || (maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_FILL] != (-1))) {
                final int ascent = java.lang.Math.max(maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_FILL], java.lang.Math.max(maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_CENTER_VERTICAL], java.lang.Math.max(maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP], maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM])));
                final int descent = java.lang.Math.max(maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_FILL], java.lang.Math.max(maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_CENTER_VERTICAL], java.lang.Math.max(maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP], maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM])));
                maxHeight = java.lang.Math.max(maxHeight, ascent + descent);
            }
        } else {
            alternativeMaxHeight = java.lang.Math.max(alternativeMaxHeight, weightedMaxHeight);
            // We have no limit, so make all weighted views as wide as the largest child.
            // Children will have already been measured once.
            if (useLargestChild && (widthMode != android.view.View.MeasureSpec.EXACTLY)) {
                for (int i = 0; i < count; i++) {
                    final android.view.View child = getVirtualChildAt(i);
                    if ((child == null) || (child.getVisibility() == android.view.View.GONE)) {
                        continue;
                    }
                    final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                    float childExtra = lp.weight;
                    if (childExtra > 0) {
                        child.measure(android.view.View.MeasureSpec.makeMeasureSpec(largestChildWidth, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), android.view.View.MeasureSpec.EXACTLY));
                    }
                }
            }
        }
        if ((!allFillParent) && (heightMode != android.view.View.MeasureSpec.EXACTLY)) {
            maxHeight = alternativeMaxHeight;
        }
        maxHeight += getPaddingTop() + getPaddingBottom();
        // Check against our minimum height
        maxHeight = java.lang.Math.max(maxHeight, getSuggestedMinimumHeight());
        setMeasuredDimension(widthSizeAndState | (childState & android.support.v4.view.ViewCompat.MEASURED_STATE_MASK), android.support.v4.view.ViewCompat.resolveSizeAndState(maxHeight, heightMeasureSpec, childState << android.support.v4.view.ViewCompat.MEASURED_HEIGHT_STATE_SHIFT));
        if (matchHeight) {
            forceUniformHeight(count, widthMeasureSpec);
        }
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), android.view.View.MeasureSpec.EXACTLY);
        for (int i = 0; i < count; ++i) {
            final android.view.View child = getVirtualChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                if (lp.height == android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    /**
     * <p>Returns the number of children to skip after measuring/laying out
     * the specified child.</p>
     *
     * @param child
     * 		the child after which we want to skip children
     * @param index
     * 		the index of the child after which we want to skip children
     * @return the number of children to skip, 0 by default
     */
    int getChildrenSkipCount(android.view.View child, int index) {
        return 0;
    }

    /**
     * <p>Returns the size (width or height) that should be occupied by a null
     * child.</p>
     *
     * @param childIndex
     * 		the index of the null child
     * @return the width or height of the child depending on the orientation
     */
    int measureNullChild(int childIndex) {
        return 0;
    }

    /**
     * <p>Measure the child according to the parent's measure specs. This
     * method should be overridden by subclasses to force the sizing of
     * children. This method is called by {@link #measureVertical(int, int)} and
     * {@link #measureHorizontal(int, int)}.</p>
     *
     * @param child
     * 		the child to measure
     * @param childIndex
     * 		the index of the child in this view
     * @param widthMeasureSpec
     * 		horizontal space requirements as imposed by the parent
     * @param totalWidth
     * 		extra space that has been used up by the parent horizontally
     * @param heightMeasureSpec
     * 		vertical space requirements as imposed by the parent
     * @param totalHeight
     * 		extra space that has been used up by the parent vertically
     */
    void measureChildBeforeLayout(android.view.View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    /**
     * <p>Return the location offset of the specified child. This can be used
     * by subclasses to change the location of a given widget.</p>
     *
     * @param child
     * 		the child for which to obtain the location offset
     * @return the location offset in pixels
     */
    int getLocationOffset(android.view.View child) {
        return 0;
    }

    /**
     * <p>Return the size offset of the next sibling of the specified child.
     * This can be used by subclasses to change the location of the widget
     * following <code>child</code>.</p>
     *
     * @param child
     * 		the child whose next sibling will be moved
     * @return the location offset of the next child in pixels
     */
    int getNextLocationOffset(android.view.View child) {
        return 0;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == android.support.v7.widget.LinearLayoutCompat.VERTICAL) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
    }

    /**
     * Position the children during a layout pass if the orientation of this
     * LinearLayout is set to {@link #VERTICAL}.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @see #onLayout(boolean, int, int, int, int)
     * @param left
     * 		
     * @param top
     * 		
     * @param right
     * 		
     * @param bottom
     * 		
     */
    void layoutVertical(int left, int top, int right, int bottom) {
        final int paddingLeft = getPaddingLeft();
        int childTop;
        int childLeft;
        // Where right end of child should go
        final int width = right - left;
        int childRight = width - getPaddingRight();
        // Space available for child
        int childSpace = (width - paddingLeft) - getPaddingRight();
        final int count = getVirtualChildCount();
        final int majorGravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        final int minorGravity = mGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        switch (majorGravity) {
            case android.view.Gravity.BOTTOM :
                // mTotalLength contains the padding already
                childTop = ((getPaddingTop() + bottom) - top) - mTotalLength;
                break;
                // mTotalLength contains the padding already
            case android.view.Gravity.CENTER_VERTICAL :
                childTop = getPaddingTop() + (((bottom - top) - mTotalLength) / 2);
                break;
            case android.view.Gravity.TOP :
            default :
                childTop = getPaddingTop();
                break;
        }
        for (int i = 0; i < count; i++) {
            final android.view.View child = getVirtualChildAt(i);
            if (child == null) {
                childTop += measureNullChild(i);
            } else
                if (child.getVisibility() != android.view.View.GONE) {
                    final int childWidth = child.getMeasuredWidth();
                    final int childHeight = child.getMeasuredHeight();
                    final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                    int gravity = lp.gravity;
                    if (gravity < 0) {
                        gravity = minorGravity;
                    }
                    final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
                    final int absoluteGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, layoutDirection);
                    switch (absoluteGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case android.view.Gravity.CENTER_HORIZONTAL :
                            childLeft = ((paddingLeft + ((childSpace - childWidth) / 2)) + lp.leftMargin) - lp.rightMargin;
                            break;
                        case android.view.Gravity.RIGHT :
                            childLeft = (childRight - childWidth) - lp.rightMargin;
                            break;
                        case android.view.Gravity.LEFT :
                        default :
                            childLeft = paddingLeft + lp.leftMargin;
                            break;
                    }
                    if (hasDividerBeforeChildAt(i)) {
                        childTop += mDividerHeight;
                    }
                    childTop += lp.topMargin;
                    setChildFrame(child, childLeft, childTop + getLocationOffset(child), childWidth, childHeight);
                    childTop += (childHeight + lp.bottomMargin) + getNextLocationOffset(child);
                    i += getChildrenSkipCount(child, i);
                }

        }
    }

    /**
     * Position the children during a layout pass if the orientation of this
     * LinearLayout is set to {@link #HORIZONTAL}.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @see #onLayout(boolean, int, int, int, int)
     * @param left
     * 		
     * @param top
     * 		
     * @param right
     * 		
     * @param bottom
     * 		
     */
    void layoutHorizontal(int left, int top, int right, int bottom) {
        final boolean isLayoutRtl = android.support.v7.widget.ViewUtils.isLayoutRtl(this);
        final int paddingTop = getPaddingTop();
        int childTop;
        int childLeft;
        // Where bottom of child should go
        final int height = bottom - top;
        int childBottom = height - getPaddingBottom();
        // Space available for child
        int childSpace = (height - paddingTop) - getPaddingBottom();
        final int count = getVirtualChildCount();
        final int majorGravity = mGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        final int minorGravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        final boolean baselineAligned = mBaselineAligned;
        final int[] maxAscent = mMaxAscent;
        final int[] maxDescent = mMaxDescent;
        final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        switch (android.support.v4.view.GravityCompat.getAbsoluteGravity(majorGravity, layoutDirection)) {
            case android.view.Gravity.RIGHT :
                // mTotalLength contains the padding already
                childLeft = ((getPaddingLeft() + right) - left) - mTotalLength;
                break;
            case android.view.Gravity.CENTER_HORIZONTAL :
                // mTotalLength contains the padding already
                childLeft = getPaddingLeft() + (((right - left) - mTotalLength) / 2);
                break;
            case android.view.Gravity.LEFT :
            default :
                childLeft = getPaddingLeft();
                break;
        }
        int start = 0;
        int dir = 1;
        // In case of RTL, start drawing from the last child.
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }
        for (int i = 0; i < count; i++) {
            int childIndex = start + (dir * i);
            final android.view.View child = getVirtualChildAt(childIndex);
            if (child == null) {
                childLeft += measureNullChild(childIndex);
            } else
                if (child.getVisibility() != android.view.View.GONE) {
                    final int childWidth = child.getMeasuredWidth();
                    final int childHeight = child.getMeasuredHeight();
                    int childBaseline = -1;
                    final android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (child.getLayoutParams()));
                    if (baselineAligned && (lp.height != android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT)) {
                        childBaseline = child.getBaseline();
                    }
                    int gravity = lp.gravity;
                    if (gravity < 0) {
                        gravity = minorGravity;
                    }
                    switch (gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
                        case android.view.Gravity.TOP :
                            childTop = paddingTop + lp.topMargin;
                            if (childBaseline != (-1)) {
                                childTop += maxAscent[android.support.v7.widget.LinearLayoutCompat.INDEX_TOP] - childBaseline;
                            }
                            break;
                        case android.view.Gravity.CENTER_VERTICAL :
                            // Removed support for baseline alignment when layout_gravity or
                            // gravity == center_vertical. See bug #1038483.
                            // Keep the code around if we need to re-enable this feature
                            // if (childBaseline != -1) {
                            // // Align baselines vertically only if the child is smaller than us
                            // if (childSpace - childHeight > 0) {
                            // childTop = paddingTop + (childSpace / 2) - childBaseline;
                            // } else {
                            // childTop = paddingTop + (childSpace - childHeight) / 2;
                            // }
                            // } else {
                            childTop = ((paddingTop + ((childSpace - childHeight) / 2)) + lp.topMargin) - lp.bottomMargin;
                            break;
                        case android.view.Gravity.BOTTOM :
                            childTop = (childBottom - childHeight) - lp.bottomMargin;
                            if (childBaseline != (-1)) {
                                int descent = child.getMeasuredHeight() - childBaseline;
                                childTop -= maxDescent[android.support.v7.widget.LinearLayoutCompat.INDEX_BOTTOM] - descent;
                            }
                            break;
                        default :
                            childTop = paddingTop;
                            break;
                    }
                    if (hasDividerBeforeChildAt(childIndex)) {
                        childLeft += mDividerWidth;
                    }
                    childLeft += lp.leftMargin;
                    setChildFrame(child, childLeft + getLocationOffset(child), childTop, childWidth, childHeight);
                    childLeft += (childWidth + lp.rightMargin) + getNextLocationOffset(child);
                    i += getChildrenSkipCount(child, childIndex);
                }

        }
    }

    private void setChildFrame(android.view.View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    /**
     * Should the layout be a column or a row.
     *
     * @param orientation
     * 		Pass {@link #HORIZONTAL} or {@link #VERTICAL}. Default
     * 		value is {@link #HORIZONTAL}.
     */
    public void setOrientation(@android.support.v7.widget.LinearLayoutCompat.OrientationMode
    int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    /**
     * Returns the current orientation.
     *
     * @return either {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    @android.support.v7.widget.LinearLayoutCompat.OrientationMode
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Describes how the child views are positioned. Defaults to GRAVITY_TOP. If
     * this layout has a VERTICAL orientation, this controls where all the child
     * views are placed if there is extra vertical space. If this layout has a
     * HORIZONTAL orientation, this controls the alignment of the children.
     *
     * @param gravity
     * 		See {@link android.view.Gravity}
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= android.support.v4.view.GravityCompat.START;
            }
            if ((gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= android.view.Gravity.TOP;
            }
            mGravity = gravity;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int horizontalGravity) {
        final int gravity = horizontalGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((mGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & (~android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK)) | gravity;
            requestLayout();
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        final int gravity = verticalGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & (~android.view.Gravity.VERTICAL_GRAVITY_MASK)) | gravity;
            requestLayout();
        }
    }

    @java.lang.Override
    public android.support.v7.widget.LinearLayoutCompat.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.v7.widget.LinearLayoutCompat.LayoutParams(getContext(), attrs);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
     * and a height of {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
     * when the layout's orientation is {@link #VERTICAL}. When the orientation is
     * {@link #HORIZONTAL}, the width is set to {@link LayoutParams#WRAP_CONTENT}
     * and the height to {@link LayoutParams#WRAP_CONTENT}.
     */
    @java.lang.Override
    protected android.support.v7.widget.LinearLayoutCompat.LayoutParams generateDefaultLayoutParams() {
        if (mOrientation == android.support.v7.widget.LinearLayoutCompat.HORIZONTAL) {
            return new android.support.v7.widget.LinearLayoutCompat.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT, android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        } else
            if (mOrientation == android.support.v7.widget.LinearLayoutCompat.VERTICAL) {
                return new android.support.v7.widget.LinearLayoutCompat.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT, android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            }

        return null;
    }

    @java.lang.Override
    protected android.support.v7.widget.LinearLayoutCompat.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new android.support.v7.widget.LinearLayoutCompat.LayoutParams(p);
    }

    // Override to allow type-checking of LayoutParams.
    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.support.v7.widget.LinearLayoutCompat.LayoutParams;
    }

    public void onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(event);
            event.setClassName(android.support.v7.widget.LinearLayoutCompat.class.getName());
        }
    }

    public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(android.support.v7.widget.LinearLayoutCompat.class.getName());
        }
    }

    /**
     * Per-child layout information associated with ViewLinearLayout.
     */
    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        /**
         * Indicates how much of the extra space in the LinearLayout will be
         * allocated to the view associated with these LayoutParams. Specify
         * 0 if the view should not be stretched. Otherwise the extra pixels
         * will be pro-rated among all views whose weight is greater than 0.
         */
        public float weight;

        /**
         * Gravity for the view associated with these LayoutParams.
         *
         * @see android.view.Gravity
         */
        public int gravity = -1;

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LinearLayoutCompat_Layout);
            weight = a.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0);
            gravity = a.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            a.recycle();
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(int width, int height) {
            super(width, height);
            weight = 0;
        }

        /**
         * Creates a new set of layout parameters with the specified width, height
         * and weight.
         *
         * @param width
         * 		the width, either {@link #MATCH_PARENT},
         * 		{@link #WRAP_CONTENT} or a fixed size in pixels
         * @param height
         * 		the height, either {@link #MATCH_PARENT},
         * 		{@link #WRAP_CONTENT} or a fixed size in pixels
         * @param weight
         * 		the weight
         */
        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.weight = weight;
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, weight,
         * and gravity of the source.
         *
         * @param source
         * 		The layout params to copy from.
         */
        public LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams source) {
            super(source);
            this.weight = source.weight;
            this.gravity = source.gravity;
        }
    }
}

