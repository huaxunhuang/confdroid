/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * A Layout where the positions of the children can be described in relation to each other or to the
 * parent.
 *
 * <p>
 * Note that you cannot have a circular dependency between the size of the RelativeLayout and the
 * position of its children. For example, you cannot have a RelativeLayout whose height is set to
 * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT WRAP_CONTENT} and a child set to
 * {@link #ALIGN_PARENT_BOTTOM}.
 * </p>
 *
 * <p><strong>Note:</strong> In platform version 17 and lower, RelativeLayout was affected by
 * a measurement bug that could cause child views to be measured with incorrect
 * {@link android.view.View.MeasureSpec MeasureSpec} values. (See
 * {@link android.view.View.MeasureSpec#makeMeasureSpec(int, int) MeasureSpec.makeMeasureSpec}
 * for more details.) This was triggered when a RelativeLayout container was placed in
 * a scrolling container, such as a ScrollView or HorizontalScrollView. If a custom view
 * not equipped to properly measure with the MeasureSpec mode
 * {@link android.view.View.MeasureSpec#UNSPECIFIED UNSPECIFIED} was placed in a RelativeLayout,
 * this would silently work anyway as RelativeLayout would pass a very large
 * {@link android.view.View.MeasureSpec#AT_MOST AT_MOST} MeasureSpec instead.</p>
 *
 * <p>This behavior has been preserved for apps that set <code>android:targetSdkVersion="17"</code>
 * or older in their manifest's <code>uses-sdk</code> tag for compatibility. Apps targeting SDK
 * version 18 or newer will receive the correct behavior</p>
 *
 * <p>See the <a href="{@docRoot }guide/topics/ui/layout/relative.html">Relative
 * Layout</a> guide.</p>
 *
 * <p>
 * Also see {@link android.widget.RelativeLayout.LayoutParams RelativeLayout.LayoutParams} for
 * layout attributes
 * </p>
 *
 * @unknown ref android.R.styleable#RelativeLayout_gravity
 * @unknown ref android.R.styleable#RelativeLayout_ignoreGravity
 */
@android.widget.RemoteViews.RemoteView
public class RelativeLayout extends android.view.ViewGroup {
    public static final int TRUE = -1;

    /**
     * Rule that aligns a child's right edge with another child's left edge.
     */
    public static final int LEFT_OF = 0;

    /**
     * Rule that aligns a child's left edge with another child's right edge.
     */
    public static final int RIGHT_OF = 1;

    /**
     * Rule that aligns a child's bottom edge with another child's top edge.
     */
    public static final int ABOVE = 2;

    /**
     * Rule that aligns a child's top edge with another child's bottom edge.
     */
    public static final int BELOW = 3;

    /**
     * Rule that aligns a child's baseline with another child's baseline.
     */
    public static final int ALIGN_BASELINE = 4;

    /**
     * Rule that aligns a child's left edge with another child's left edge.
     */
    public static final int ALIGN_LEFT = 5;

    /**
     * Rule that aligns a child's top edge with another child's top edge.
     */
    public static final int ALIGN_TOP = 6;

    /**
     * Rule that aligns a child's right edge with another child's right edge.
     */
    public static final int ALIGN_RIGHT = 7;

    /**
     * Rule that aligns a child's bottom edge with another child's bottom edge.
     */
    public static final int ALIGN_BOTTOM = 8;

    /**
     * Rule that aligns the child's left edge with its RelativeLayout
     * parent's left edge.
     */
    public static final int ALIGN_PARENT_LEFT = 9;

    /**
     * Rule that aligns the child's top edge with its RelativeLayout
     * parent's top edge.
     */
    public static final int ALIGN_PARENT_TOP = 10;

    /**
     * Rule that aligns the child's right edge with its RelativeLayout
     * parent's right edge.
     */
    public static final int ALIGN_PARENT_RIGHT = 11;

    /**
     * Rule that aligns the child's bottom edge with its RelativeLayout
     * parent's bottom edge.
     */
    public static final int ALIGN_PARENT_BOTTOM = 12;

    /**
     * Rule that centers the child with respect to the bounds of its
     * RelativeLayout parent.
     */
    public static final int CENTER_IN_PARENT = 13;

    /**
     * Rule that centers the child horizontally with respect to the
     * bounds of its RelativeLayout parent.
     */
    public static final int CENTER_HORIZONTAL = 14;

    /**
     * Rule that centers the child vertically with respect to the
     * bounds of its RelativeLayout parent.
     */
    public static final int CENTER_VERTICAL = 15;

    /**
     * Rule that aligns a child's end edge with another child's start edge.
     */
    public static final int START_OF = 16;

    /**
     * Rule that aligns a child's start edge with another child's end edge.
     */
    public static final int END_OF = 17;

    /**
     * Rule that aligns a child's start edge with another child's start edge.
     */
    public static final int ALIGN_START = 18;

    /**
     * Rule that aligns a child's end edge with another child's end edge.
     */
    public static final int ALIGN_END = 19;

    /**
     * Rule that aligns the child's start edge with its RelativeLayout
     * parent's start edge.
     */
    public static final int ALIGN_PARENT_START = 20;

    /**
     * Rule that aligns the child's end edge with its RelativeLayout
     * parent's end edge.
     */
    public static final int ALIGN_PARENT_END = 21;

    private static final int VERB_COUNT = 22;

    private static final int[] RULES_VERTICAL = new int[]{ android.widget.RelativeLayout.ABOVE, android.widget.RelativeLayout.BELOW, android.widget.RelativeLayout.ALIGN_BASELINE, android.widget.RelativeLayout.ALIGN_TOP, android.widget.RelativeLayout.ALIGN_BOTTOM };

    private static final int[] RULES_HORIZONTAL = new int[]{ android.widget.RelativeLayout.LEFT_OF, android.widget.RelativeLayout.RIGHT_OF, android.widget.RelativeLayout.ALIGN_LEFT, android.widget.RelativeLayout.ALIGN_RIGHT, android.widget.RelativeLayout.START_OF, android.widget.RelativeLayout.END_OF, android.widget.RelativeLayout.ALIGN_START, android.widget.RelativeLayout.ALIGN_END };

    /**
     * Used to indicate left/right/top/bottom should be inferred from constraints
     */
    private static final int VALUE_NOT_SET = java.lang.Integer.MIN_VALUE;

    private android.view.View mBaselineView = null;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private int mGravity = android.view.Gravity.START | android.view.Gravity.TOP;

    private final android.graphics.Rect mContentBounds = new android.graphics.Rect();

    private final android.graphics.Rect mSelfBounds = new android.graphics.Rect();

    private int mIgnoreGravity;

    private java.util.SortedSet<android.view.View> mTopToBottomLeftToRightSet = null;

    private boolean mDirtyHierarchy;

    private android.view.View[] mSortedHorizontalChildren;

    private android.view.View[] mSortedVerticalChildren;

    private final android.widget.RelativeLayout.DependencyGraph mGraph = new android.widget.RelativeLayout.DependencyGraph();

    // Compatibility hack. Old versions of the platform had problems
    // with MeasureSpec value overflow and RelativeLayout was one source of them.
    // Some apps came to rely on them. :(
    private boolean mAllowBrokenMeasureSpecs = false;

    // Compatibility hack. Old versions of the platform would not take
    // margins and padding into account when generating the height measure spec
    // for children during the horizontal measure pass.
    private boolean mMeasureVerticalWithPaddingMargin = false;

    // A default width used for RTL measure pass
    /**
     * Value reduced so as not to interfere with View's measurement spec. flags. See:
     * {@link View#MEASURED_SIZE_MASK}.
     * {@link View#MEASURED_STATE_TOO_SMALL}.
     */
    private static final int DEFAULT_WIDTH = 0x10000;

    public RelativeLayout(android.content.Context context) {
        this(context, null);
    }

    public RelativeLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RelativeLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
        queryCompatibilityModes(context);
    }

    private void initFromAttributes(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayout, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.RelativeLayout, attrs, a, defStyleAttr, defStyleRes);
        mIgnoreGravity = a.getResourceId(R.styleable.RelativeLayout_ignoreGravity, android.view.View.NO_ID);
        mGravity = a.getInt(R.styleable.RelativeLayout_gravity, mGravity);
        a.recycle();
    }

    private void queryCompatibilityModes(android.content.Context context) {
        int version = context.getApplicationInfo().targetSdkVersion;
        mAllowBrokenMeasureSpecs = version <= Build.VERSION_CODES.JELLY_BEAN_MR1;
        mMeasureVerticalWithPaddingMargin = version >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Defines which View is ignored when the gravity is applied. This setting has no
     * effect if the gravity is <code>Gravity.START | Gravity.TOP</code>.
     *
     * @param viewId
     * 		The id of the View to be ignored by gravity, or 0 if no View
     * 		should be ignored.
     * @see #setGravity(int)
     * @unknown ref android.R.styleable#RelativeLayout_ignoreGravity
     */
    @android.view.RemotableViewMethod
    public void setIgnoreGravity(int viewId) {
        mIgnoreGravity = viewId;
    }

    /**
     * Get the id of the View to be ignored by gravity
     *
     * @unknown ref android.R.styleable#RelativeLayout_ignoreGravity
     */
    @android.view.inspector.InspectableProperty
    public int getIgnoreGravity() {
        return mIgnoreGravity;
    }

    /**
     * Describes how the child views are positioned.
     *
     * @return the gravity.
     * @see #setGravity(int)
     * @see android.view.Gravity
     * @unknown ref android.R.styleable#RelativeLayout_gravity
     */
    @android.view.inspector.InspectableProperty(valueType = android.view.inspector.InspectableProperty.ValueType.GRAVITY)
    public int getGravity() {
        return mGravity;
    }

    /**
     * Describes how the child views are positioned. Defaults to
     * <code>Gravity.START | Gravity.TOP</code>.
     *
     * <p>Note that since RelativeLayout considers the positioning of each child
     * relative to one another to be significant, setting gravity will affect
     * the positioning of all children as a single unit within the parent.
     * This happens after children have been relatively positioned.</p>
     *
     * @param gravity
     * 		See {@link android.view.Gravity}
     * @see #setHorizontalGravity(int)
     * @see #setVerticalGravity(int)
     * @unknown ref android.R.styleable#RelativeLayout_gravity
     */
    @android.view.RemotableViewMethod
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= android.view.Gravity.START;
            }
            if ((gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= android.view.Gravity.TOP;
            }
            mGravity = gravity;
            requestLayout();
        }
    }

    @android.view.RemotableViewMethod
    public void setHorizontalGravity(int horizontalGravity) {
        final int gravity = horizontalGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((mGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & (~android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK)) | gravity;
            requestLayout();
        }
    }

    @android.view.RemotableViewMethod
    public void setVerticalGravity(int verticalGravity) {
        final int gravity = verticalGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & (~android.view.Gravity.VERTICAL_GRAVITY_MASK)) | gravity;
            requestLayout();
        }
    }

    @java.lang.Override
    public int getBaseline() {
        return mBaselineView != null ? mBaselineView.getBaseline() : super.getBaseline();
    }

    @java.lang.Override
    public void requestLayout() {
        super.requestLayout();
        mDirtyHierarchy = true;
    }

    private void sortChildren() {
        final int count = getChildCount();
        if ((mSortedVerticalChildren == null) || (mSortedVerticalChildren.length != count)) {
            mSortedVerticalChildren = new android.view.View[count];
        }
        if ((mSortedHorizontalChildren == null) || (mSortedHorizontalChildren.length != count)) {
            mSortedHorizontalChildren = new android.view.View[count];
        }
        final android.widget.RelativeLayout.DependencyGraph graph = mGraph;
        graph.clear();
        for (int i = 0; i < count; i++) {
            graph.add(getChildAt(i));
        }
        graph.getSortedViews(mSortedVerticalChildren, android.widget.RelativeLayout.RULES_VERTICAL);
        graph.getSortedViews(mSortedHorizontalChildren, android.widget.RelativeLayout.RULES_HORIZONTAL);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDirtyHierarchy) {
            mDirtyHierarchy = false;
            sortChildren();
        }
        int myWidth = -1;
        int myHeight = -1;
        int width = 0;
        int height = 0;
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        // Record our dimensions if they are known;
        if (widthMode != android.view.View.MeasureSpec.UNSPECIFIED) {
            myWidth = widthSize;
        }
        if (heightMode != android.view.View.MeasureSpec.UNSPECIFIED) {
            myHeight = heightSize;
        }
        if (widthMode == android.view.View.MeasureSpec.EXACTLY) {
            width = myWidth;
        }
        if (heightMode == android.view.View.MeasureSpec.EXACTLY) {
            height = myHeight;
        }
        android.view.View ignore = null;
        int gravity = mGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        final boolean horizontalGravity = (gravity != android.view.Gravity.START) && (gravity != 0);
        gravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        final boolean verticalGravity = (gravity != android.view.Gravity.TOP) && (gravity != 0);
        int left = java.lang.Integer.MAX_VALUE;
        int top = java.lang.Integer.MAX_VALUE;
        int right = java.lang.Integer.MIN_VALUE;
        int bottom = java.lang.Integer.MIN_VALUE;
        boolean offsetHorizontalAxis = false;
        boolean offsetVerticalAxis = false;
        if ((horizontalGravity || verticalGravity) && (mIgnoreGravity != android.view.View.NO_ID)) {
            ignore = findViewById(mIgnoreGravity);
        }
        final boolean isWrapContentWidth = widthMode != android.view.View.MeasureSpec.EXACTLY;
        final boolean isWrapContentHeight = heightMode != android.view.View.MeasureSpec.EXACTLY;
        // We need to know our size for doing the correct computation of children positioning in RTL
        // mode but there is no practical way to get it instead of running the code below.
        // So, instead of running the code twice, we just set the width to a "default display width"
        // before the computation and then, as a last pass, we will update their real position with
        // an offset equals to "DEFAULT_WIDTH - width".
        final int layoutDirection = getLayoutDirection();
        if (isLayoutRtl() && (myWidth == (-1))) {
            myWidth = android.widget.RelativeLayout.DEFAULT_WIDTH;
        }
        android.view.View[] views = mSortedHorizontalChildren;
        int count = views.length;
        for (int i = 0; i < count; i++) {
            android.view.View child = views[i];
            if (child.getVisibility() != android.view.View.GONE) {
                android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                int[] rules = params.getRules(layoutDirection);
                applyHorizontalSizeRules(params, myWidth, rules);
                measureChildHorizontal(child, params, myWidth, myHeight);
                if (positionChildHorizontal(child, params, myWidth, isWrapContentWidth)) {
                    offsetHorizontalAxis = true;
                }
            }
        }
        views = mSortedVerticalChildren;
        count = views.length;
        final int targetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
        for (int i = 0; i < count; i++) {
            final android.view.View child = views[i];
            if (child.getVisibility() != android.view.View.GONE) {
                final android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                applyVerticalSizeRules(params, myHeight, child.getBaseline());
                measureChild(child, params, myWidth, myHeight);
                if (positionChildVertical(child, params, myHeight, isWrapContentHeight)) {
                    offsetVerticalAxis = true;
                }
                if (isWrapContentWidth) {
                    if (isLayoutRtl()) {
                        if (targetSdkVersion < Build.VERSION_CODES.KITKAT) {
                            width = java.lang.Math.max(width, myWidth - params.mLeft);
                        } else {
                            width = java.lang.Math.max(width, (myWidth - params.mLeft) + params.leftMargin);
                        }
                    } else {
                        if (targetSdkVersion < Build.VERSION_CODES.KITKAT) {
                            width = java.lang.Math.max(width, params.mRight);
                        } else {
                            width = java.lang.Math.max(width, params.mRight + params.rightMargin);
                        }
                    }
                }
                if (isWrapContentHeight) {
                    if (targetSdkVersion < Build.VERSION_CODES.KITKAT) {
                        height = java.lang.Math.max(height, params.mBottom);
                    } else {
                        height = java.lang.Math.max(height, params.mBottom + params.bottomMargin);
                    }
                }
                if ((child != ignore) || verticalGravity) {
                    left = java.lang.Math.min(left, params.mLeft - params.leftMargin);
                    top = java.lang.Math.min(top, params.mTop - params.topMargin);
                }
                if ((child != ignore) || horizontalGravity) {
                    right = java.lang.Math.max(right, params.mRight + params.rightMargin);
                    bottom = java.lang.Math.max(bottom, params.mBottom + params.bottomMargin);
                }
            }
        }
        // Use the top-start-most laid out view as the baseline. RTL offsets are
        // applied later, so we can use the left-most edge as the starting edge.
        android.view.View baselineView = null;
        android.widget.RelativeLayout.LayoutParams baselineParams = null;
        for (int i = 0; i < count; i++) {
            final android.view.View child = views[i];
            if (child.getVisibility() != android.view.View.GONE) {
                final android.widget.RelativeLayout.LayoutParams childParams = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                if (((baselineView == null) || (baselineParams == null)) || (compareLayoutPosition(childParams, baselineParams) < 0)) {
                    baselineView = child;
                    baselineParams = childParams;
                }
            }
        }
        mBaselineView = baselineView;
        if (isWrapContentWidth) {
            // Width already has left padding in it since it was calculated by looking at
            // the right of each child view
            width += mPaddingRight;
            if ((mLayoutParams != null) && (mLayoutParams.width >= 0)) {
                width = java.lang.Math.max(width, mLayoutParams.width);
            }
            width = java.lang.Math.max(width, getSuggestedMinimumWidth());
            width = android.view.View.resolveSize(width, widthMeasureSpec);
            if (offsetHorizontalAxis) {
                for (int i = 0; i < count; i++) {
                    final android.view.View child = views[i];
                    if (child.getVisibility() != android.view.View.GONE) {
                        final android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                        final int[] rules = params.getRules(layoutDirection);
                        if ((rules[android.widget.RelativeLayout.CENTER_IN_PARENT] != 0) || (rules[android.widget.RelativeLayout.CENTER_HORIZONTAL] != 0)) {
                            android.widget.RelativeLayout.centerHorizontal(child, params, width);
                        } else
                            if (rules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] != 0) {
                                final int childWidth = child.getMeasuredWidth();
                                params.mLeft = (width - mPaddingRight) - childWidth;
                                params.mRight = params.mLeft + childWidth;
                            }

                    }
                }
            }
        }
        if (isWrapContentHeight) {
            // Height already has top padding in it since it was calculated by looking at
            // the bottom of each child view
            height += mPaddingBottom;
            if ((mLayoutParams != null) && (mLayoutParams.height >= 0)) {
                height = java.lang.Math.max(height, mLayoutParams.height);
            }
            height = java.lang.Math.max(height, getSuggestedMinimumHeight());
            height = android.view.View.resolveSize(height, heightMeasureSpec);
            if (offsetVerticalAxis) {
                for (int i = 0; i < count; i++) {
                    final android.view.View child = views[i];
                    if (child.getVisibility() != android.view.View.GONE) {
                        final android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                        final int[] rules = params.getRules(layoutDirection);
                        if ((rules[android.widget.RelativeLayout.CENTER_IN_PARENT] != 0) || (rules[android.widget.RelativeLayout.CENTER_VERTICAL] != 0)) {
                            android.widget.RelativeLayout.centerVertical(child, params, height);
                        } else
                            if (rules[android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM] != 0) {
                                final int childHeight = child.getMeasuredHeight();
                                params.mTop = (height - mPaddingBottom) - childHeight;
                                params.mBottom = params.mTop + childHeight;
                            }

                    }
                }
            }
        }
        if (horizontalGravity || verticalGravity) {
            final android.graphics.Rect selfBounds = mSelfBounds;
            selfBounds.set(mPaddingLeft, mPaddingTop, width - mPaddingRight, height - mPaddingBottom);
            final android.graphics.Rect contentBounds = mContentBounds;
            android.view.Gravity.apply(mGravity, right - left, bottom - top, selfBounds, contentBounds, layoutDirection);
            final int horizontalOffset = contentBounds.left - left;
            final int verticalOffset = contentBounds.top - top;
            if ((horizontalOffset != 0) || (verticalOffset != 0)) {
                for (int i = 0; i < count; i++) {
                    final android.view.View child = views[i];
                    if ((child.getVisibility() != android.view.View.GONE) && (child != ignore)) {
                        final android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                        if (horizontalGravity) {
                            params.mLeft += horizontalOffset;
                            params.mRight += horizontalOffset;
                        }
                        if (verticalGravity) {
                            params.mTop += verticalOffset;
                            params.mBottom += verticalOffset;
                        }
                    }
                }
            }
        }
        if (isLayoutRtl()) {
            final int offsetWidth = myWidth - width;
            for (int i = 0; i < count; i++) {
                final android.view.View child = views[i];
                if (child.getVisibility() != android.view.View.GONE) {
                    final android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                    params.mLeft -= offsetWidth;
                    params.mRight -= offsetWidth;
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    /**
     *
     *
     * @return a negative number if the top of {@code p1} is above the top of
    {@code p2} or if they have identical top values and the left of
    {@code p1} is to the left of {@code p2}, or a positive number
    otherwise
     */
    private int compareLayoutPosition(android.widget.RelativeLayout.LayoutParams p1, android.widget.RelativeLayout.LayoutParams p2) {
        final int topDiff = p1.mTop - p2.mTop;
        if (topDiff != 0) {
            return topDiff;
        }
        return p1.mLeft - p2.mLeft;
    }

    /**
     * Measure a child. The child should have left, top, right and bottom information
     * stored in its LayoutParams. If any of these values is VALUE_NOT_SET it means
     * that the view can extend up to the corresponding edge.
     *
     * @param child
     * 		Child to measure
     * @param params
     * 		LayoutParams associated with child
     * @param myWidth
     * 		Width of the the RelativeLayout
     * @param myHeight
     * 		Height of the RelativeLayout
     */
    private void measureChild(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myWidth, int myHeight) {
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, mPaddingLeft, mPaddingRight, myWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(params.mTop, params.mBottom, params.height, params.topMargin, params.bottomMargin, mPaddingTop, mPaddingBottom, myHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private void measureChildHorizontal(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myWidth, int myHeight) {
        final int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, mPaddingLeft, mPaddingRight, myWidth);
        final int childHeightMeasureSpec;
        if ((myHeight < 0) && (!mAllowBrokenMeasureSpecs)) {
            if (params.height >= 0) {
                childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(params.height, android.view.View.MeasureSpec.EXACTLY);
            } else {
                // Negative values in a mySize/myWidth/myWidth value in
                // RelativeLayout measurement is code for, "we got an
                // unspecified mode in the RelativeLayout's measure spec."
                // Carry it forward.
                childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
            }
        } else {
            final int maxHeight;
            if (mMeasureVerticalWithPaddingMargin) {
                maxHeight = java.lang.Math.max(0, (((myHeight - mPaddingTop) - mPaddingBottom) - params.topMargin) - params.bottomMargin);
            } else {
                maxHeight = java.lang.Math.max(0, myHeight);
            }
            final int heightMode;
            if (params.height == android.widget.RelativeLayout.LayoutParams.MATCH_PARENT) {
                heightMode = android.view.View.MeasureSpec.EXACTLY;
            } else {
                heightMode = android.view.View.MeasureSpec.AT_MOST;
            }
            childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxHeight, heightMode);
        }
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * Get a measure spec that accounts for all of the constraints on this view.
     * This includes size constraints imposed by the RelativeLayout as well as
     * the View's desired dimension.
     *
     * @param childStart
     * 		The left or top field of the child's layout params
     * @param childEnd
     * 		The right or bottom field of the child's layout params
     * @param childSize
     * 		The child's desired size (the width or height field of
     * 		the child's layout params)
     * @param startMargin
     * 		The left or top margin
     * @param endMargin
     * 		The right or bottom margin
     * @param startPadding
     * 		mPaddingLeft or mPaddingTop
     * @param endPadding
     * 		mPaddingRight or mPaddingBottom
     * @param mySize
     * 		The width or height of this view (the RelativeLayout)
     * @return MeasureSpec for the child
     */
    private int getChildMeasureSpec(int childStart, int childEnd, int childSize, int startMargin, int endMargin, int startPadding, int endPadding, int mySize) {
        int childSpecMode = 0;
        int childSpecSize = 0;
        // Negative values in a mySize value in RelativeLayout
        // measurement is code for, "we got an unspecified mode in the
        // RelativeLayout's measure spec."
        final boolean isUnspecified = mySize < 0;
        if (isUnspecified && (!mAllowBrokenMeasureSpecs)) {
            if ((childStart != android.widget.RelativeLayout.VALUE_NOT_SET) && (childEnd != android.widget.RelativeLayout.VALUE_NOT_SET)) {
                // Constraints fixed both edges, so child has an exact size.
                childSpecSize = java.lang.Math.max(0, childEnd - childStart);
                childSpecMode = android.view.View.MeasureSpec.EXACTLY;
            } else
                if (childSize >= 0) {
                    // The child specified an exact size.
                    childSpecSize = childSize;
                    childSpecMode = android.view.View.MeasureSpec.EXACTLY;
                } else {
                    // Allow the child to be whatever size it wants.
                    childSpecSize = 0;
                    childSpecMode = android.view.View.MeasureSpec.UNSPECIFIED;
                }

            return android.view.View.MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
        }
        // Figure out start and end bounds.
        int tempStart = childStart;
        int tempEnd = childEnd;
        // If the view did not express a layout constraint for an edge, use
        // view's margins and our padding
        if (tempStart == android.widget.RelativeLayout.VALUE_NOT_SET) {
            tempStart = startPadding + startMargin;
        }
        if (tempEnd == android.widget.RelativeLayout.VALUE_NOT_SET) {
            tempEnd = (mySize - endPadding) - endMargin;
        }
        // Figure out maximum size available to this view
        final int maxAvailable = tempEnd - tempStart;
        if ((childStart != android.widget.RelativeLayout.VALUE_NOT_SET) && (childEnd != android.widget.RelativeLayout.VALUE_NOT_SET)) {
            // Constraints fixed both edges, so child must be an exact size.
            childSpecMode = (isUnspecified) ? android.view.View.MeasureSpec.UNSPECIFIED : android.view.View.MeasureSpec.EXACTLY;
            childSpecSize = java.lang.Math.max(0, maxAvailable);
        } else {
            if (childSize >= 0) {
                // Child wanted an exact size. Give as much as possible.
                childSpecMode = android.view.View.MeasureSpec.EXACTLY;
                if (maxAvailable >= 0) {
                    // We have a maximum size in this dimension.
                    childSpecSize = java.lang.Math.min(maxAvailable, childSize);
                } else {
                    // We can grow in this dimension.
                    childSpecSize = childSize;
                }
            } else
                if (childSize == android.widget.RelativeLayout.LayoutParams.MATCH_PARENT) {
                    // Child wanted to be as big as possible. Give all available
                    // space.
                    childSpecMode = (isUnspecified) ? android.view.View.MeasureSpec.UNSPECIFIED : android.view.View.MeasureSpec.EXACTLY;
                    childSpecSize = java.lang.Math.max(0, maxAvailable);
                } else
                    if (childSize == android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT) {
                        // Child wants to wrap content. Use AT_MOST to communicate
                        // available space if we know our max size.
                        if (maxAvailable >= 0) {
                            // We have a maximum size in this dimension.
                            childSpecMode = android.view.View.MeasureSpec.AT_MOST;
                            childSpecSize = maxAvailable;
                        } else {
                            // We can grow in this dimension. Child can be as big as it
                            // wants.
                            childSpecMode = android.view.View.MeasureSpec.UNSPECIFIED;
                            childSpecSize = 0;
                        }
                    }


        }
        return android.view.View.MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
    }

    private boolean positionChildHorizontal(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myWidth, boolean wrapContent) {
        final int layoutDirection = getLayoutDirection();
        int[] rules = params.getRules(layoutDirection);
        if ((params.mLeft == android.widget.RelativeLayout.VALUE_NOT_SET) && (params.mRight != android.widget.RelativeLayout.VALUE_NOT_SET)) {
            // Right is fixed, but left varies
            params.mLeft = params.mRight - child.getMeasuredWidth();
        } else
            if ((params.mLeft != android.widget.RelativeLayout.VALUE_NOT_SET) && (params.mRight == android.widget.RelativeLayout.VALUE_NOT_SET)) {
                // Left is fixed, but right varies
                params.mRight = params.mLeft + child.getMeasuredWidth();
            } else
                if ((params.mLeft == android.widget.RelativeLayout.VALUE_NOT_SET) && (params.mRight == android.widget.RelativeLayout.VALUE_NOT_SET)) {
                    // Both left and right vary
                    if ((rules[android.widget.RelativeLayout.CENTER_IN_PARENT] != 0) || (rules[android.widget.RelativeLayout.CENTER_HORIZONTAL] != 0)) {
                        if (!wrapContent) {
                            android.widget.RelativeLayout.centerHorizontal(child, params, myWidth);
                        } else {
                            positionAtEdge(child, params, myWidth);
                        }
                        return true;
                    } else {
                        // This is the default case. For RTL we start from the right and for LTR we start
                        // from the left. This will give LEFT/TOP for LTR and RIGHT/TOP for RTL.
                        positionAtEdge(child, params, myWidth);
                    }
                }


        return rules[android.widget.RelativeLayout.ALIGN_PARENT_END] != 0;
    }

    private void positionAtEdge(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myWidth) {
        if (isLayoutRtl()) {
            params.mRight = (myWidth - mPaddingRight) - params.rightMargin;
            params.mLeft = params.mRight - child.getMeasuredWidth();
        } else {
            params.mLeft = mPaddingLeft + params.leftMargin;
            params.mRight = params.mLeft + child.getMeasuredWidth();
        }
    }

    private boolean positionChildVertical(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myHeight, boolean wrapContent) {
        int[] rules = params.getRules();
        if ((params.mTop == android.widget.RelativeLayout.VALUE_NOT_SET) && (params.mBottom != android.widget.RelativeLayout.VALUE_NOT_SET)) {
            // Bottom is fixed, but top varies
            params.mTop = params.mBottom - child.getMeasuredHeight();
        } else
            if ((params.mTop != android.widget.RelativeLayout.VALUE_NOT_SET) && (params.mBottom == android.widget.RelativeLayout.VALUE_NOT_SET)) {
                // Top is fixed, but bottom varies
                params.mBottom = params.mTop + child.getMeasuredHeight();
            } else
                if ((params.mTop == android.widget.RelativeLayout.VALUE_NOT_SET) && (params.mBottom == android.widget.RelativeLayout.VALUE_NOT_SET)) {
                    // Both top and bottom vary
                    if ((rules[android.widget.RelativeLayout.CENTER_IN_PARENT] != 0) || (rules[android.widget.RelativeLayout.CENTER_VERTICAL] != 0)) {
                        if (!wrapContent) {
                            android.widget.RelativeLayout.centerVertical(child, params, myHeight);
                        } else {
                            params.mTop = mPaddingTop + params.topMargin;
                            params.mBottom = params.mTop + child.getMeasuredHeight();
                        }
                        return true;
                    } else {
                        params.mTop = mPaddingTop + params.topMargin;
                        params.mBottom = params.mTop + child.getMeasuredHeight();
                    }
                }


        return rules[android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM] != 0;
    }

    private void applyHorizontalSizeRules(android.widget.RelativeLayout.LayoutParams childParams, int myWidth, int[] rules) {
        android.widget.RelativeLayout.LayoutParams anchorParams;
        // VALUE_NOT_SET indicates a "soft requirement" in that direction. For example:
        // left=10, right=VALUE_NOT_SET means the view must start at 10, but can go as far as it
        // wants to the right
        // left=VALUE_NOT_SET, right=10 means the view must end at 10, but can go as far as it
        // wants to the left
        // left=10, right=20 means the left and right ends are both fixed
        childParams.mLeft = android.widget.RelativeLayout.VALUE_NOT_SET;
        childParams.mRight = android.widget.RelativeLayout.VALUE_NOT_SET;
        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.LEFT_OF);
        if (anchorParams != null) {
            childParams.mRight = anchorParams.mLeft - (anchorParams.leftMargin + childParams.rightMargin);
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.LEFT_OF] != 0)) {
                if (myWidth >= 0) {
                    childParams.mRight = (myWidth - mPaddingRight) - childParams.rightMargin;
                }
            }

        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.RIGHT_OF);
        if (anchorParams != null) {
            childParams.mLeft = anchorParams.mRight + (anchorParams.rightMargin + childParams.leftMargin);
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.RIGHT_OF] != 0)) {
                childParams.mLeft = mPaddingLeft + childParams.leftMargin;
            }

        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.ALIGN_LEFT);
        if (anchorParams != null) {
            childParams.mLeft = anchorParams.mLeft + childParams.leftMargin;
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.ALIGN_LEFT] != 0)) {
                childParams.mLeft = mPaddingLeft + childParams.leftMargin;
            }

        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.ALIGN_RIGHT);
        if (anchorParams != null) {
            childParams.mRight = anchorParams.mRight - childParams.rightMargin;
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.ALIGN_RIGHT] != 0)) {
                if (myWidth >= 0) {
                    childParams.mRight = (myWidth - mPaddingRight) - childParams.rightMargin;
                }
            }

        if (0 != rules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT]) {
            childParams.mLeft = mPaddingLeft + childParams.leftMargin;
        }
        if (0 != rules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT]) {
            if (myWidth >= 0) {
                childParams.mRight = (myWidth - mPaddingRight) - childParams.rightMargin;
            }
        }
    }

    private void applyVerticalSizeRules(android.widget.RelativeLayout.LayoutParams childParams, int myHeight, int myBaseline) {
        final int[] rules = childParams.getRules();
        // Baseline alignment overrides any explicitly specified top or bottom.
        int baselineOffset = getRelatedViewBaselineOffset(rules);
        if (baselineOffset != (-1)) {
            if (myBaseline != (-1)) {
                baselineOffset -= myBaseline;
            }
            childParams.mTop = baselineOffset;
            childParams.mBottom = android.widget.RelativeLayout.VALUE_NOT_SET;
            return;
        }
        android.widget.RelativeLayout.LayoutParams anchorParams;
        childParams.mTop = android.widget.RelativeLayout.VALUE_NOT_SET;
        childParams.mBottom = android.widget.RelativeLayout.VALUE_NOT_SET;
        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.ABOVE);
        if (anchorParams != null) {
            childParams.mBottom = anchorParams.mTop - (anchorParams.topMargin + childParams.bottomMargin);
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.ABOVE] != 0)) {
                if (myHeight >= 0) {
                    childParams.mBottom = (myHeight - mPaddingBottom) - childParams.bottomMargin;
                }
            }

        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.BELOW);
        if (anchorParams != null) {
            childParams.mTop = anchorParams.mBottom + (anchorParams.bottomMargin + childParams.topMargin);
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.BELOW] != 0)) {
                childParams.mTop = mPaddingTop + childParams.topMargin;
            }

        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.ALIGN_TOP);
        if (anchorParams != null) {
            childParams.mTop = anchorParams.mTop + childParams.topMargin;
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.ALIGN_TOP] != 0)) {
                childParams.mTop = mPaddingTop + childParams.topMargin;
            }

        anchorParams = getRelatedViewParams(rules, android.widget.RelativeLayout.ALIGN_BOTTOM);
        if (anchorParams != null) {
            childParams.mBottom = anchorParams.mBottom - childParams.bottomMargin;
        } else
            if (childParams.alignWithParent && (rules[android.widget.RelativeLayout.ALIGN_BOTTOM] != 0)) {
                if (myHeight >= 0) {
                    childParams.mBottom = (myHeight - mPaddingBottom) - childParams.bottomMargin;
                }
            }

        if (0 != rules[android.widget.RelativeLayout.ALIGN_PARENT_TOP]) {
            childParams.mTop = mPaddingTop + childParams.topMargin;
        }
        if (0 != rules[android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM]) {
            if (myHeight >= 0) {
                childParams.mBottom = (myHeight - mPaddingBottom) - childParams.bottomMargin;
            }
        }
    }

    private android.view.View getRelatedView(int[] rules, int relation) {
        int id = rules[relation];
        if (id != 0) {
            android.widget.RelativeLayout.DependencyGraph.Node node = mGraph.mKeyNodes.get(id);
            if (node == null)
                return null;

            android.view.View v = node.view;
            // Find the first non-GONE view up the chain
            while (v.getVisibility() == android.view.View.GONE) {
                rules = ((android.widget.RelativeLayout.LayoutParams) (v.getLayoutParams())).getRules(v.getLayoutDirection());
                node = mGraph.mKeyNodes.get(rules[relation]);
                // ignore self dependency. for more info look in git commit: da3003
                if ((node == null) || (v == node.view))
                    return null;

                v = node.view;
            } 
            return v;
        }
        return null;
    }

    private android.widget.RelativeLayout.LayoutParams getRelatedViewParams(int[] rules, int relation) {
        android.view.View v = getRelatedView(rules, relation);
        if (v != null) {
            android.view.ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params instanceof android.widget.RelativeLayout.LayoutParams) {
                return ((android.widget.RelativeLayout.LayoutParams) (v.getLayoutParams()));
            }
        }
        return null;
    }

    private int getRelatedViewBaselineOffset(int[] rules) {
        final android.view.View v = getRelatedView(rules, android.widget.RelativeLayout.ALIGN_BASELINE);
        if (v != null) {
            final int baseline = v.getBaseline();
            if (baseline != (-1)) {
                final android.view.ViewGroup.LayoutParams params = v.getLayoutParams();
                if (params instanceof android.widget.RelativeLayout.LayoutParams) {
                    final android.widget.RelativeLayout.LayoutParams anchorParams = ((android.widget.RelativeLayout.LayoutParams) (v.getLayoutParams()));
                    return anchorParams.mTop + baseline;
                }
            }
        }
        return -1;
    }

    private static void centerHorizontal(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myWidth) {
        int childWidth = child.getMeasuredWidth();
        int left = (myWidth - childWidth) / 2;
        params.mLeft = left;
        params.mRight = left + childWidth;
    }

    private static void centerVertical(android.view.View child, android.widget.RelativeLayout.LayoutParams params, int myHeight) {
        int childHeight = child.getMeasuredHeight();
        int top = (myHeight - childHeight) / 2;
        params.mTop = top;
        params.mBottom = top + childHeight;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // The layout has actually already been performed and the positions
        // cached.  Apply the cached values to the children.
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            android.view.View child = getChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                android.widget.RelativeLayout.LayoutParams st = ((android.widget.RelativeLayout.LayoutParams) (child.getLayoutParams()));
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
    }

    @java.lang.Override
    public android.widget.RelativeLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.widget.RelativeLayout.LayoutParams(getContext(), attrs);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT},
     * a height of {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and no spanning.
     */
    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    // Override to allow type-checking of LayoutParams.
    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.widget.RelativeLayout.LayoutParams;
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (android.view.View.sPreserveMarginParamsInLayoutParamConversion) {
            if (lp instanceof android.widget.RelativeLayout.LayoutParams) {
                return new android.widget.RelativeLayout.LayoutParams(((android.widget.RelativeLayout.LayoutParams) (lp)));
            } else
                if (lp instanceof android.view.ViewGroup.MarginLayoutParams) {
                    return new android.widget.RelativeLayout.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (lp)));
                }

        }
        return new android.widget.RelativeLayout.LayoutParams(lp);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        if (mTopToBottomLeftToRightSet == null) {
            mTopToBottomLeftToRightSet = new java.util.TreeSet<android.view.View>(new android.widget.RelativeLayout.TopToBottomLeftToRightComparator());
        }
        // sort children top-to-bottom and left-to-right
        for (int i = 0, count = getChildCount(); i < count; i++) {
            mTopToBottomLeftToRightSet.add(getChildAt(i));
        }
        for (android.view.View view : mTopToBottomLeftToRightSet) {
            if ((view.getVisibility() == android.view.View.VISIBLE) && view.dispatchPopulateAccessibilityEvent(event)) {
                mTopToBottomLeftToRightSet.clear();
                return true;
            }
        }
        mTopToBottomLeftToRightSet.clear();
        return false;
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.RelativeLayout.class.getName();
    }

    /**
     * Compares two views in left-to-right and top-to-bottom fashion.
     */
    private class TopToBottomLeftToRightComparator implements java.util.Comparator<android.view.View> {
        public int compare(android.view.View first, android.view.View second) {
            // top - bottom
            int topDifference = first.getTop() - second.getTop();
            if (topDifference != 0) {
                return topDifference;
            }
            // left - right
            int leftDifference = first.getLeft() - second.getLeft();
            if (leftDifference != 0) {
                return leftDifference;
            }
            // break tie by height
            int heightDiference = first.getHeight() - second.getHeight();
            if (heightDiference != 0) {
                return heightDiference;
            }
            // break tie by width
            int widthDiference = first.getWidth() - second.getWidth();
            if (widthDiference != 0) {
                return widthDiference;
            }
            return 0;
        }
    }

    /**
     * Specifies how a view is positioned within a {@link RelativeLayout}.
     * The relative layout containing the view uses the value of these layout parameters to
     * determine where to position the view on the screen.  If the view is not contained
     * within a relative layout, these attributes are ignored.
     *
     * See the <a href="/guide/topics/ui/layout/relative.html">
     * Relative Layout</a> guide for example code demonstrating how to use relative layout’s
     * layout parameters in a layout XML.
     *
     * To learn more about layout parameters and how they differ from typical view attributes,
     * see the <a href="/guide/topics/ui/declaring-layout.html#attributes">
     *     Layouts guide</a>.
     *
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignWithParentIfMissing
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_toLeftOf
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_toRightOf
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_above
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_below
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignBaseline
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignLeft
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignTop
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignRight
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignBottom
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignParentLeft
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignParentTop
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignParentRight
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignParentBottom
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_centerInParent
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_centerHorizontal
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_centerVertical
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_toStartOf
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_toEndOf
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignStart
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignEnd
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignParentStart
     * @unknown ref android.R.styleable#RelativeLayout_Layout_layout_alignParentEnd
     */
    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        @android.view.ViewDebug.ExportedProperty(category = "layout", resolveId = true, indexMapping = { @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ABOVE, to = "above"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_BASELINE, to = "alignBaseline"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_BOTTOM, to = "alignBottom"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_LEFT, to = "alignLeft"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM, to = "alignParentBottom"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_PARENT_LEFT, to = "alignParentLeft"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_PARENT_RIGHT, to = "alignParentRight"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_PARENT_TOP, to = "alignParentTop"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_RIGHT, to = "alignRight"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_TOP, to = "alignTop"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.BELOW, to = "below"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.CENTER_HORIZONTAL, to = "centerHorizontal"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.CENTER_IN_PARENT, to = "center"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.CENTER_VERTICAL, to = "centerVertical"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.LEFT_OF, to = "leftOf"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.RIGHT_OF, to = "rightOf"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_START, to = "alignStart"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_END, to = "alignEnd"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_PARENT_START, to = "alignParentStart"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.ALIGN_PARENT_END, to = "alignParentEnd"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.START_OF, to = "startOf"), @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.END_OF, to = "endOf") }, mapping = { @android.view.ViewDebug.IntToString(from = android.widget.RelativeLayout.TRUE, to = "true"), @android.view.ViewDebug.IntToString(from = 0, to = "false/NO_ID") })
        private int[] mRules = new int[android.widget.RelativeLayout.VERB_COUNT];

        private int[] mInitialRules = new int[android.widget.RelativeLayout.VERB_COUNT];

        @android.annotation.UnsupportedAppUsage
        private int mLeft;

        @android.annotation.UnsupportedAppUsage
        private int mTop;

        @android.annotation.UnsupportedAppUsage
        private int mRight;

        @android.annotation.UnsupportedAppUsage
        private int mBottom;

        /**
         * Whether this view had any relative rules modified following the most
         * recent resolution of layout direction.
         */
        private boolean mNeedsLayoutResolution;

        private boolean mRulesChanged = false;

        private boolean mIsRtlCompatibilityMode = false;

        /**
         * When true, uses the parent as the anchor if the anchor doesn't exist or if
         * the anchor's visibility is GONE.
         */
        @android.view.ViewDebug.ExportedProperty(category = "layout")
        public boolean alignWithParent;

        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, com.android.internal.R.styleable.RelativeLayout_Layout);
            final int targetSdkVersion = c.getApplicationInfo().targetSdkVersion;
            mIsRtlCompatibilityMode = (targetSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) || (!c.getApplicationInfo().hasRtlSupport());
            final int[] rules = mRules;
            // noinspection MismatchedReadAndWriteOfArray
            final int[] initialRules = mInitialRules;
            final int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignWithParentIfMissing :
                        alignWithParent = a.getBoolean(attr, false);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_toLeftOf :
                        rules[android.widget.RelativeLayout.LEFT_OF] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_toRightOf :
                        rules[android.widget.RelativeLayout.RIGHT_OF] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_above :
                        rules[android.widget.RelativeLayout.ABOVE] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_below :
                        rules[android.widget.RelativeLayout.BELOW] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignBaseline :
                        rules[android.widget.RelativeLayout.ALIGN_BASELINE] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignLeft :
                        rules[android.widget.RelativeLayout.ALIGN_LEFT] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignTop :
                        rules[android.widget.RelativeLayout.ALIGN_TOP] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignRight :
                        rules[android.widget.RelativeLayout.ALIGN_RIGHT] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignBottom :
                        rules[android.widget.RelativeLayout.ALIGN_BOTTOM] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignParentLeft :
                        rules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignParentTop :
                        rules[android.widget.RelativeLayout.ALIGN_PARENT_TOP] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignParentRight :
                        rules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignParentBottom :
                        rules[android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_centerInParent :
                        rules[android.widget.RelativeLayout.CENTER_IN_PARENT] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_centerHorizontal :
                        rules[android.widget.RelativeLayout.CENTER_HORIZONTAL] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_centerVertical :
                        rules[android.widget.RelativeLayout.CENTER_VERTICAL] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_toStartOf :
                        rules[android.widget.RelativeLayout.START_OF] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_toEndOf :
                        rules[android.widget.RelativeLayout.END_OF] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignStart :
                        rules[android.widget.RelativeLayout.ALIGN_START] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignEnd :
                        rules[android.widget.RelativeLayout.ALIGN_END] = a.getResourceId(attr, 0);
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignParentStart :
                        rules[android.widget.RelativeLayout.ALIGN_PARENT_START] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                    case com.android.internal.R.styleable.RelativeLayout_Layout_layout_alignParentEnd :
                        rules[android.widget.RelativeLayout.ALIGN_PARENT_END] = (a.getBoolean(attr, false)) ? android.widget.RelativeLayout.TRUE : 0;
                        break;
                }
            }
            mRulesChanged = true;
            java.lang.System.arraycopy(rules, android.widget.RelativeLayout.LEFT_OF, initialRules, android.widget.RelativeLayout.LEFT_OF, android.widget.RelativeLayout.VERB_COUNT);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, and rules
         * of the source.
         *
         * @param source
         * 		The layout params to copy from.
         */
        public LayoutParams(android.widget.RelativeLayout.LayoutParams source) {
            super(source);
            this.mIsRtlCompatibilityMode = source.mIsRtlCompatibilityMode;
            this.mRulesChanged = source.mRulesChanged;
            this.alignWithParent = source.alignWithParent;
            java.lang.System.arraycopy(source.mRules, android.widget.RelativeLayout.LEFT_OF, this.mRules, android.widget.RelativeLayout.LEFT_OF, android.widget.RelativeLayout.VERB_COUNT);
            java.lang.System.arraycopy(source.mInitialRules, android.widget.RelativeLayout.LEFT_OF, this.mInitialRules, android.widget.RelativeLayout.LEFT_OF, android.widget.RelativeLayout.VERB_COUNT);
        }

        @java.lang.Override
        public java.lang.String debug(java.lang.String output) {
            return ((((output + "ViewGroup.LayoutParams={ width=") + android.view.ViewGroup.LayoutParams.sizeToString(width)) + ", height=") + android.view.ViewGroup.LayoutParams.sizeToString(height)) + " }";
        }

        /**
         * Adds a layout rule to be interpreted by the RelativeLayout.
         * <p>
         * This method should only be used for verbs that don't refer to a
         * sibling (ex. {@link #ALIGN_RIGHT}) or take a boolean
         * value ({@link #TRUE} for true or 0 for false). To
         * specify a verb that takes a subject, use {@link #addRule(int, int)}.
         * <p>
         * If the rule is relative to the layout direction (ex.
         * {@link #ALIGN_PARENT_START}), then the layout direction must be
         * resolved using {@link #resolveLayoutDirection(int)} before calling
         * {@link #getRule(int)} an absolute rule (ex.
         * {@link #ALIGN_PARENT_LEFT}.
         *
         * @param verb
         * 		a layout verb, such as {@link #ALIGN_PARENT_LEFT}
         * @see #addRule(int, int)
         * @see #removeRule(int)
         * @see #getRule(int)
         */
        public void addRule(int verb) {
            addRule(verb, android.widget.RelativeLayout.TRUE);
        }

        /**
         * Adds a layout rule to be interpreted by the RelativeLayout.
         * <p>
         * Use this for verbs that refer to a sibling (ex.
         * {@link #ALIGN_RIGHT}) or take a boolean value (ex.
         * {@link #CENTER_IN_PARENT}).
         * <p>
         * If the rule is relative to the layout direction (ex.
         * {@link #START_OF}), then the layout direction must be resolved using
         * {@link #resolveLayoutDirection(int)} before calling
         * {@link #getRule(int)} with an absolute rule (ex. {@link #LEFT_OF}.
         *
         * @param verb
         * 		a layout verb, such as {@link #ALIGN_RIGHT}
         * @param subject
         * 		the ID of another view to use as an anchor, or a
         * 		boolean value (represented as {@link #TRUE} for true
         * 		or 0 for false)
         * @see #addRule(int)
         * @see #removeRule(int)
         * @see #getRule(int)
         */
        public void addRule(int verb, int subject) {
            // If we're removing a relative rule, we'll need to force layout
            // resolution the next time it's requested.
            if ((((!mNeedsLayoutResolution) && isRelativeRule(verb)) && (mInitialRules[verb] != 0)) && (subject == 0)) {
                mNeedsLayoutResolution = true;
            }
            mRules[verb] = subject;
            mInitialRules[verb] = subject;
            mRulesChanged = true;
        }

        /**
         * Removes a layout rule to be interpreted by the RelativeLayout.
         * <p>
         * If the rule is relative to the layout direction (ex.
         * {@link #START_OF}, {@link #ALIGN_PARENT_START}, etc.) then the
         * layout direction must be resolved using
         * {@link #resolveLayoutDirection(int)} before before calling
         * {@link #getRule(int)} with an absolute rule (ex. {@link #LEFT_OF}.
         *
         * @param verb
         * 		One of the verbs defined by
         * 		{@link android.widget.RelativeLayout RelativeLayout}, such as
         * 		ALIGN_WITH_PARENT_LEFT.
         * @see #addRule(int)
         * @see #addRule(int, int)
         * @see #getRule(int)
         */
        public void removeRule(int verb) {
            addRule(verb, 0);
        }

        /**
         * Returns the layout rule associated with a specific verb.
         *
         * @param verb
         * 		one of the verbs defined by {@link RelativeLayout}, such
         * 		as ALIGN_WITH_PARENT_LEFT
         * @return the id of another view to use as an anchor, a boolean value
        (represented as {@link RelativeLayout#TRUE} for true
        or 0 for false), or -1 for verbs that don't refer to another
        sibling (for example, ALIGN_WITH_PARENT_BOTTOM)
         * @see #addRule(int)
         * @see #addRule(int, int)
         */
        public int getRule(int verb) {
            return mRules[verb];
        }

        private boolean hasRelativeRules() {
            return (((((mInitialRules[android.widget.RelativeLayout.START_OF] != 0) || (mInitialRules[android.widget.RelativeLayout.END_OF] != 0)) || (mInitialRules[android.widget.RelativeLayout.ALIGN_START] != 0)) || (mInitialRules[android.widget.RelativeLayout.ALIGN_END] != 0)) || (mInitialRules[android.widget.RelativeLayout.ALIGN_PARENT_START] != 0)) || (mInitialRules[android.widget.RelativeLayout.ALIGN_PARENT_END] != 0);
        }

        private boolean isRelativeRule(int rule) {
            return (((((rule == android.widget.RelativeLayout.START_OF) || (rule == android.widget.RelativeLayout.END_OF)) || (rule == android.widget.RelativeLayout.ALIGN_START)) || (rule == android.widget.RelativeLayout.ALIGN_END)) || (rule == android.widget.RelativeLayout.ALIGN_PARENT_START)) || (rule == android.widget.RelativeLayout.ALIGN_PARENT_END);
        }

        // The way we are resolving rules depends on the layout direction and if we are pre JB MR1
        // or not.
        // 
        // If we are pre JB MR1 (said as "RTL compatibility mode"), "left"/"right" rules are having
        // predominance over any "start/end" rules that could have been defined. A special case:
        // if no "left"/"right" rule has been defined and "start"/"end" rules are defined then we
        // resolve those "start"/"end" rules to "left"/"right" respectively.
        // 
        // If we are JB MR1+, then "start"/"end" rules are having predominance over "left"/"right"
        // rules. If no "start"/"end" rule is defined then we use "left"/"right" rules.
        // 
        // In all cases, the result of the resolution should clear the "start"/"end" rules to leave
        // only the "left"/"right" rules at the end.
        private void resolveRules(int layoutDirection) {
            final boolean isLayoutRtl = layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL;
            // Reset to initial state
            java.lang.System.arraycopy(mInitialRules, android.widget.RelativeLayout.LEFT_OF, mRules, android.widget.RelativeLayout.LEFT_OF, android.widget.RelativeLayout.VERB_COUNT);
            // Apply rules depending on direction and if we are in RTL compatibility mode
            if (mIsRtlCompatibilityMode) {
                if (mRules[android.widget.RelativeLayout.ALIGN_START] != 0) {
                    if (mRules[android.widget.RelativeLayout.ALIGN_LEFT] == 0) {
                        // "left" rule is not defined but "start" rule is: use the "start" rule as
                        // the "left" rule
                        mRules[android.widget.RelativeLayout.ALIGN_LEFT] = mRules[android.widget.RelativeLayout.ALIGN_START];
                    }
                    mRules[android.widget.RelativeLayout.ALIGN_START] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_END] != 0) {
                    if (mRules[android.widget.RelativeLayout.ALIGN_RIGHT] == 0) {
                        // "right" rule is not defined but "end" rule is: use the "end" rule as the
                        // "right" rule
                        mRules[android.widget.RelativeLayout.ALIGN_RIGHT] = mRules[android.widget.RelativeLayout.ALIGN_END];
                    }
                    mRules[android.widget.RelativeLayout.ALIGN_END] = 0;
                }
                if (mRules[android.widget.RelativeLayout.START_OF] != 0) {
                    if (mRules[android.widget.RelativeLayout.LEFT_OF] == 0) {
                        // "left" rule is not defined but "start" rule is: use the "start" rule as
                        // the "left" rule
                        mRules[android.widget.RelativeLayout.LEFT_OF] = mRules[android.widget.RelativeLayout.START_OF];
                    }
                    mRules[android.widget.RelativeLayout.START_OF] = 0;
                }
                if (mRules[android.widget.RelativeLayout.END_OF] != 0) {
                    if (mRules[android.widget.RelativeLayout.RIGHT_OF] == 0) {
                        // "right" rule is not defined but "end" rule is: use the "end" rule as the
                        // "right" rule
                        mRules[android.widget.RelativeLayout.RIGHT_OF] = mRules[android.widget.RelativeLayout.END_OF];
                    }
                    mRules[android.widget.RelativeLayout.END_OF] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_PARENT_START] != 0) {
                    if (mRules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT] == 0) {
                        // "left" rule is not defined but "start" rule is: use the "start" rule as
                        // the "left" rule
                        mRules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT] = mRules[android.widget.RelativeLayout.ALIGN_PARENT_START];
                    }
                    mRules[android.widget.RelativeLayout.ALIGN_PARENT_START] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_PARENT_END] != 0) {
                    if (mRules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] == 0) {
                        // "right" rule is not defined but "end" rule is: use the "end" rule as the
                        // "right" rule
                        mRules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] = mRules[android.widget.RelativeLayout.ALIGN_PARENT_END];
                    }
                    mRules[android.widget.RelativeLayout.ALIGN_PARENT_END] = 0;
                }
            } else {
                // JB MR1+ case
                if (((mRules[android.widget.RelativeLayout.ALIGN_START] != 0) || (mRules[android.widget.RelativeLayout.ALIGN_END] != 0)) && ((mRules[android.widget.RelativeLayout.ALIGN_LEFT] != 0) || (mRules[android.widget.RelativeLayout.ALIGN_RIGHT] != 0))) {
                    // "start"/"end" rules take precedence over "left"/"right" rules
                    mRules[android.widget.RelativeLayout.ALIGN_LEFT] = 0;
                    mRules[android.widget.RelativeLayout.ALIGN_RIGHT] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_START] != 0) {
                    // "start" rule resolved to "left" or "right" depending on the direction
                    mRules[isLayoutRtl ? android.widget.RelativeLayout.ALIGN_RIGHT : android.widget.RelativeLayout.ALIGN_LEFT] = mRules[android.widget.RelativeLayout.ALIGN_START];
                    mRules[android.widget.RelativeLayout.ALIGN_START] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_END] != 0) {
                    // "end" rule resolved to "left" or "right" depending on the direction
                    mRules[isLayoutRtl ? android.widget.RelativeLayout.ALIGN_LEFT : android.widget.RelativeLayout.ALIGN_RIGHT] = mRules[android.widget.RelativeLayout.ALIGN_END];
                    mRules[android.widget.RelativeLayout.ALIGN_END] = 0;
                }
                if (((mRules[android.widget.RelativeLayout.START_OF] != 0) || (mRules[android.widget.RelativeLayout.END_OF] != 0)) && ((mRules[android.widget.RelativeLayout.LEFT_OF] != 0) || (mRules[android.widget.RelativeLayout.RIGHT_OF] != 0))) {
                    // "start"/"end" rules take precedence over "left"/"right" rules
                    mRules[android.widget.RelativeLayout.LEFT_OF] = 0;
                    mRules[android.widget.RelativeLayout.RIGHT_OF] = 0;
                }
                if (mRules[android.widget.RelativeLayout.START_OF] != 0) {
                    // "start" rule resolved to "left" or "right" depending on the direction
                    mRules[isLayoutRtl ? android.widget.RelativeLayout.RIGHT_OF : android.widget.RelativeLayout.LEFT_OF] = mRules[android.widget.RelativeLayout.START_OF];
                    mRules[android.widget.RelativeLayout.START_OF] = 0;
                }
                if (mRules[android.widget.RelativeLayout.END_OF] != 0) {
                    // "end" rule resolved to "left" or "right" depending on the direction
                    mRules[isLayoutRtl ? android.widget.RelativeLayout.LEFT_OF : android.widget.RelativeLayout.RIGHT_OF] = mRules[android.widget.RelativeLayout.END_OF];
                    mRules[android.widget.RelativeLayout.END_OF] = 0;
                }
                if (((mRules[android.widget.RelativeLayout.ALIGN_PARENT_START] != 0) || (mRules[android.widget.RelativeLayout.ALIGN_PARENT_END] != 0)) && ((mRules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT] != 0) || (mRules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] != 0))) {
                    // "start"/"end" rules take precedence over "left"/"right" rules
                    mRules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT] = 0;
                    mRules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_PARENT_START] != 0) {
                    // "start" rule resolved to "left" or "right" depending on the direction
                    mRules[isLayoutRtl ? android.widget.RelativeLayout.ALIGN_PARENT_RIGHT : android.widget.RelativeLayout.ALIGN_PARENT_LEFT] = mRules[android.widget.RelativeLayout.ALIGN_PARENT_START];
                    mRules[android.widget.RelativeLayout.ALIGN_PARENT_START] = 0;
                }
                if (mRules[android.widget.RelativeLayout.ALIGN_PARENT_END] != 0) {
                    // "end" rule resolved to "left" or "right" depending on the direction
                    mRules[isLayoutRtl ? android.widget.RelativeLayout.ALIGN_PARENT_LEFT : android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] = mRules[android.widget.RelativeLayout.ALIGN_PARENT_END];
                    mRules[android.widget.RelativeLayout.ALIGN_PARENT_END] = 0;
                }
            }
            mRulesChanged = false;
            mNeedsLayoutResolution = false;
        }

        /**
         * Retrieves a complete list of all supported rules, where the index is the rule
         * verb, and the element value is the value specified, or "false" if it was never
         * set. If there are relative rules defined (*_START / *_END), they will be resolved
         * depending on the layout direction.
         *
         * @param layoutDirection
         * 		the direction of the layout.
         * 		Should be either {@link View#LAYOUT_DIRECTION_LTR}
         * 		or {@link View#LAYOUT_DIRECTION_RTL}
         * @return the supported rules
         * @see #addRule(int, int)
         * @unknown 
         */
        public int[] getRules(int layoutDirection) {
            resolveLayoutDirection(layoutDirection);
            return mRules;
        }

        /**
         * Retrieves a complete list of all supported rules, where the index is the rule
         * verb, and the element value is the value specified, or "false" if it was never
         * set. There will be no resolution of relative rules done.
         *
         * @return the supported rules
         * @see #addRule(int, int)
         */
        public int[] getRules() {
            return mRules;
        }

        /**
         * This will be called by {@link android.view.View#requestLayout()} to
         * resolve layout parameters that are relative to the layout direction.
         * <p>
         * After this method is called, any rules using layout-relative verbs
         * (ex. {@link #START_OF}) previously added via {@link #addRule(int)}
         * may only be accessed via their resolved absolute verbs (ex.
         * {@link #LEFT_OF}).
         */
        @java.lang.Override
        public void resolveLayoutDirection(int layoutDirection) {
            if (shouldResolveLayoutDirection(layoutDirection)) {
                resolveRules(layoutDirection);
            }
            // This will set the layout direction.
            super.resolveLayoutDirection(layoutDirection);
        }

        private boolean shouldResolveLayoutDirection(int layoutDirection) {
            return (mNeedsLayoutResolution || hasRelativeRules()) && (mRulesChanged || (layoutDirection != getLayoutDirection()));
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        protected void encodeProperties(@android.annotation.NonNull
        android.view.ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:alignWithParent", alignWithParent);
        }

        /**
         *
         *
         * @unknown 
         */
        public static final class InspectionCompanion implements android.view.inspector.InspectionCompanion<android.widget.RelativeLayout.LayoutParams> {
            private boolean mPropertiesMapped;

            private int mAboveId;

            private int mAlignBaselineId;

            private int mAlignBottomId;

            private int mAlignEndId;

            private int mAlignLeftId;

            private int mAlignParentBottomId;

            private int mAlignParentEndId;

            private int mAlignParentLeftId;

            private int mAlignParentRightId;

            private int mAlignParentStartId;

            private int mAlignParentTopId;

            private int mAlignRightId;

            private int mAlignStartId;

            private int mAlignTopId;

            private int mAlignWithParentIfMissingId;

            private int mBelowId;

            private int mCenterHorizontalId;

            private int mCenterInParentId;

            private int mCenterVerticalId;

            private int mToEndOfId;

            private int mToLeftOfId;

            private int mToRightOfId;

            private int mToStartOfId;

            @java.lang.Override
            public void mapProperties(@android.annotation.NonNull
            android.view.inspector.PropertyMapper propertyMapper) {
                mPropertiesMapped = true;
                mAboveId = propertyMapper.mapResourceId("layout_above", R.attr.layout_above);
                mAlignBaselineId = propertyMapper.mapResourceId("layout_alignBaseline", R.attr.layout_alignBaseline);
                mAlignBottomId = propertyMapper.mapResourceId("layout_alignBottom", R.attr.layout_alignBottom);
                mAlignEndId = propertyMapper.mapResourceId("layout_alignEnd", R.attr.layout_alignEnd);
                mAlignLeftId = propertyMapper.mapResourceId("layout_alignLeft", R.attr.layout_alignLeft);
                mAlignParentBottomId = propertyMapper.mapBoolean("layout_alignParentBottom", R.attr.layout_alignParentBottom);
                mAlignParentEndId = propertyMapper.mapBoolean("layout_alignParentEnd", R.attr.layout_alignParentEnd);
                mAlignParentLeftId = propertyMapper.mapBoolean("layout_alignParentLeft", R.attr.layout_alignParentLeft);
                mAlignParentRightId = propertyMapper.mapBoolean("layout_alignParentRight", R.attr.layout_alignParentRight);
                mAlignParentStartId = propertyMapper.mapBoolean("layout_alignParentStart", R.attr.layout_alignParentStart);
                mAlignParentTopId = propertyMapper.mapBoolean("layout_alignParentTop", R.attr.layout_alignParentTop);
                mAlignRightId = propertyMapper.mapResourceId("layout_alignRight", R.attr.layout_alignRight);
                mAlignStartId = propertyMapper.mapResourceId("layout_alignStart", R.attr.layout_alignStart);
                mAlignTopId = propertyMapper.mapResourceId("layout_alignTop", R.attr.layout_alignTop);
                mAlignWithParentIfMissingId = propertyMapper.mapBoolean("layout_alignWithParentIfMissing", R.attr.layout_alignWithParentIfMissing);
                mBelowId = propertyMapper.mapResourceId("layout_below", R.attr.layout_below);
                mCenterHorizontalId = propertyMapper.mapBoolean("layout_centerHorizontal", R.attr.layout_centerHorizontal);
                mCenterInParentId = propertyMapper.mapBoolean("layout_centerInParent", R.attr.layout_centerInParent);
                mCenterVerticalId = propertyMapper.mapBoolean("layout_centerVertical", R.attr.layout_centerVertical);
                mToEndOfId = propertyMapper.mapResourceId("layout_toEndOf", R.attr.layout_toEndOf);
                mToLeftOfId = propertyMapper.mapResourceId("layout_toLeftOf", R.attr.layout_toLeftOf);
                mToRightOfId = propertyMapper.mapResourceId("layout_toRightOf", R.attr.layout_toRightOf);
                mToStartOfId = propertyMapper.mapResourceId("layout_toStartOf", R.attr.layout_toStartOf);
            }

            @java.lang.Override
            public void readProperties(@android.annotation.NonNull
            android.widget.RelativeLayout.LayoutParams node, @android.annotation.NonNull
            android.view.inspector.PropertyReader propertyReader) {
                if (!mPropertiesMapped) {
                    throw new android.view.inspector.InspectionCompanion.UninitializedPropertyMapException();
                }
                final int[] rules = node.getRules();
                propertyReader.readResourceId(mAboveId, rules[android.widget.RelativeLayout.ABOVE]);
                propertyReader.readResourceId(mAlignBaselineId, rules[android.widget.RelativeLayout.ALIGN_BASELINE]);
                propertyReader.readResourceId(mAlignBottomId, rules[android.widget.RelativeLayout.ALIGN_BOTTOM]);
                propertyReader.readResourceId(mAlignEndId, rules[android.widget.RelativeLayout.ALIGN_END]);
                propertyReader.readResourceId(mAlignLeftId, rules[android.widget.RelativeLayout.ALIGN_LEFT]);
                propertyReader.readBoolean(mAlignParentBottomId, rules[android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mAlignParentEndId, rules[android.widget.RelativeLayout.ALIGN_PARENT_END] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mAlignParentLeftId, rules[android.widget.RelativeLayout.ALIGN_PARENT_LEFT] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mAlignParentRightId, rules[android.widget.RelativeLayout.ALIGN_PARENT_RIGHT] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mAlignParentStartId, rules[android.widget.RelativeLayout.ALIGN_PARENT_START] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mAlignParentTopId, rules[android.widget.RelativeLayout.ALIGN_PARENT_TOP] == android.widget.RelativeLayout.TRUE);
                propertyReader.readResourceId(mAlignRightId, rules[android.widget.RelativeLayout.ALIGN_RIGHT]);
                propertyReader.readResourceId(mAlignStartId, rules[android.widget.RelativeLayout.ALIGN_START]);
                propertyReader.readResourceId(mAlignTopId, rules[android.widget.RelativeLayout.ALIGN_TOP]);
                propertyReader.readBoolean(mAlignWithParentIfMissingId, node.alignWithParent);
                propertyReader.readResourceId(mBelowId, rules[android.widget.RelativeLayout.BELOW]);
                propertyReader.readBoolean(mCenterHorizontalId, rules[android.widget.RelativeLayout.CENTER_HORIZONTAL] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mCenterInParentId, rules[android.widget.RelativeLayout.CENTER_IN_PARENT] == android.widget.RelativeLayout.TRUE);
                propertyReader.readBoolean(mCenterVerticalId, rules[android.widget.RelativeLayout.CENTER_VERTICAL] == android.widget.RelativeLayout.TRUE);
                propertyReader.readResourceId(mToEndOfId, rules[android.widget.RelativeLayout.END_OF]);
                propertyReader.readResourceId(mToLeftOfId, rules[android.widget.RelativeLayout.LEFT_OF]);
                propertyReader.readResourceId(mToRightOfId, rules[android.widget.RelativeLayout.RIGHT_OF]);
                propertyReader.readResourceId(mToStartOfId, rules[android.widget.RelativeLayout.START_OF]);
            }
        }
    }

    private static class DependencyGraph {
        /**
         * List of all views in the graph.
         */
        private java.util.ArrayList<android.widget.RelativeLayout.DependencyGraph.Node> mNodes = new java.util.ArrayList<android.widget.RelativeLayout.DependencyGraph.Node>();

        /**
         * List of nodes in the graph. Each node is identified by its
         * view id (see View#getId()).
         */
        private android.util.SparseArray<android.widget.RelativeLayout.DependencyGraph.Node> mKeyNodes = new android.util.SparseArray<android.widget.RelativeLayout.DependencyGraph.Node>();

        /**
         * Temporary data structure used to build the list of roots
         * for this graph.
         */
        private java.util.ArrayDeque<android.widget.RelativeLayout.DependencyGraph.Node> mRoots = new java.util.ArrayDeque<android.widget.RelativeLayout.DependencyGraph.Node>();

        /**
         * Clears the graph.
         */
        void clear() {
            final java.util.ArrayList<android.widget.RelativeLayout.DependencyGraph.Node> nodes = mNodes;
            final int count = nodes.size();
            for (int i = 0; i < count; i++) {
                nodes.get(i).release();
            }
            nodes.clear();
            mKeyNodes.clear();
            mRoots.clear();
        }

        /**
         * Adds a view to the graph.
         *
         * @param view
         * 		The view to be added as a node to the graph.
         */
        void add(android.view.View view) {
            final int id = view.getId();
            final android.widget.RelativeLayout.DependencyGraph.Node node = android.widget.RelativeLayout.DependencyGraph.Node.acquire(view);
            if (id != android.view.View.NO_ID) {
                mKeyNodes.put(id, node);
            }
            mNodes.add(node);
        }

        /**
         * Builds a sorted list of views. The sorting order depends on the dependencies
         * between the view. For instance, if view C needs view A to be processed first
         * and view A needs view B to be processed first, the dependency graph
         * is: B -> A -> C. The sorted array will contain views B, A and C in this order.
         *
         * @param sorted
         * 		The sorted list of views. The length of this array must
         * 		be equal to getChildCount().
         * @param rules
         * 		The list of rules to take into account.
         */
        void getSortedViews(android.view.View[] sorted, int... rules) {
            final java.util.ArrayDeque<android.widget.RelativeLayout.DependencyGraph.Node> roots = findRoots(rules);
            int index = 0;
            android.widget.RelativeLayout.DependencyGraph.Node node;
            while ((node = roots.pollLast()) != null) {
                final android.view.View view = node.view;
                final int key = view.getId();
                sorted[index++] = view;
                final android.util.ArrayMap<android.widget.RelativeLayout.DependencyGraph.Node, android.widget.RelativeLayout.DependencyGraph> dependents = node.dependents;
                final int count = dependents.size();
                for (int i = 0; i < count; i++) {
                    final android.widget.RelativeLayout.DependencyGraph.Node dependent = dependents.keyAt(i);
                    final android.util.SparseArray<android.widget.RelativeLayout.DependencyGraph.Node> dependencies = dependent.dependencies;
                    dependencies.remove(key);
                    if (dependencies.size() == 0) {
                        roots.add(dependent);
                    }
                }
            } 
            if (index < sorted.length) {
                throw new java.lang.IllegalStateException("Circular dependencies cannot exist" + " in RelativeLayout");
            }
        }

        /**
         * Finds the roots of the graph. A root is a node with no dependency and
         * with [0..n] dependents.
         *
         * @param rulesFilter
         * 		The list of rules to consider when building the
         * 		dependencies
         * @return A list of node, each being a root of the graph
         */
        private java.util.ArrayDeque<android.widget.RelativeLayout.DependencyGraph.Node> findRoots(int[] rulesFilter) {
            final android.util.SparseArray<android.widget.RelativeLayout.DependencyGraph.Node> keyNodes = mKeyNodes;
            final java.util.ArrayList<android.widget.RelativeLayout.DependencyGraph.Node> nodes = mNodes;
            final int count = nodes.size();
            // Find roots can be invoked several times, so make sure to clear
            // all dependents and dependencies before running the algorithm
            for (int i = 0; i < count; i++) {
                final android.widget.RelativeLayout.DependencyGraph.Node node = nodes.get(i);
                node.dependents.clear();
                node.dependencies.clear();
            }
            // Builds up the dependents and dependencies for each node of the graph
            for (int i = 0; i < count; i++) {
                final android.widget.RelativeLayout.DependencyGraph.Node node = nodes.get(i);
                final android.widget.RelativeLayout.LayoutParams layoutParams = ((android.widget.RelativeLayout.LayoutParams) (node.view.getLayoutParams()));
                final int[] rules = layoutParams.mRules;
                final int rulesCount = rulesFilter.length;
                // Look only the the rules passed in parameter, this way we build only the
                // dependencies for a specific set of rules
                for (int j = 0; j < rulesCount; j++) {
                    final int rule = rules[rulesFilter[j]];
                    if ((rule > 0) || android.content.res.ResourceId.isValid(rule)) {
                        // The node this node depends on
                        final android.widget.RelativeLayout.DependencyGraph.Node dependency = keyNodes.get(rule);
                        // Skip unknowns and self dependencies
                        if ((dependency == null) || (dependency == node)) {
                            continue;
                        }
                        // Add the current node as a dependent
                        dependency.dependents.put(node, this);
                        // Add a dependency to the current node
                        node.dependencies.put(rule, dependency);
                    }
                }
            }
            final java.util.ArrayDeque<android.widget.RelativeLayout.DependencyGraph.Node> roots = mRoots;
            roots.clear();
            // Finds all the roots in the graph: all nodes with no dependencies
            for (int i = 0; i < count; i++) {
                final android.widget.RelativeLayout.DependencyGraph.Node node = nodes.get(i);
                if (node.dependencies.size() == 0)
                    roots.addLast(node);

            }
            return roots;
        }

        /**
         * A node in the dependency graph. A node is a view, its list of dependencies
         * and its list of dependents.
         *
         * A node with no dependent is considered a root of the graph.
         */
        /* END POOL IMPLEMENTATION */
        static class Node {
            /**
             * The view representing this node in the layout.
             */
            android.view.View view;

            /**
             * The list of dependents for this node; a dependent is a node
             * that needs this node to be processed first.
             */
            final android.util.ArrayMap<android.widget.RelativeLayout.DependencyGraph.Node, android.widget.RelativeLayout.DependencyGraph> dependents = new android.util.ArrayMap<android.widget.RelativeLayout.DependencyGraph.Node, android.widget.RelativeLayout.DependencyGraph>();

            /**
             * The list of dependencies for this node.
             */
            final android.util.SparseArray<android.widget.RelativeLayout.DependencyGraph.Node> dependencies = new android.util.SparseArray<android.widget.RelativeLayout.DependencyGraph.Node>();

            /* START POOL IMPLEMENTATION */
            // The pool is static, so all nodes instances are shared across
            // activities, that's why we give it a rather high limit
            private static final int POOL_LIMIT = 100;

            private static final android.util.Pools.SynchronizedPool<android.widget.RelativeLayout.DependencyGraph.Node> sPool = new android.util.Pools.SynchronizedPool<android.widget.RelativeLayout.DependencyGraph.Node>(android.widget.RelativeLayout.DependencyGraph.Node.POOL_LIMIT);

            static android.widget.RelativeLayout.DependencyGraph.Node acquire(android.view.View view) {
                android.widget.RelativeLayout.DependencyGraph.Node node = android.widget.RelativeLayout.DependencyGraph.Node.sPool.acquire();
                if (node == null) {
                    node = new android.widget.RelativeLayout.DependencyGraph.Node();
                }
                node.view = view;
                return node;
            }

            void release() {
                view = null;
                dependents.clear();
                dependencies.clear();
                android.widget.RelativeLayout.DependencyGraph.Node.sPool.release(this);
            }
        }
    }
}

