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
 * FrameLayout is designed to block out an area on the screen to display
 * a single item. Generally, FrameLayout should be used to hold a single child view, because it can
 * be difficult to organize child views in a way that's scalable to different screen sizes without
 * the children overlapping each other. You can, however, add multiple children to a FrameLayout
 * and control their position within the FrameLayout by assigning gravity to each child, using the
 * <a href="FrameLayout.LayoutParams.html#attr_android:layout_gravity">{@code android:layout_gravity}</a> attribute.
 * <p>Child views are drawn in a stack, with the most recently added child on top.
 * The size of the FrameLayout is the size of its largest child (plus padding), visible
 * or not (if the FrameLayout's parent permits). Views that are {@link android.view.View#GONE} are
 * used for sizing
 * only if {@link #setMeasureAllChildren(boolean) setConsiderGoneChildrenWhenMeasuring()}
 * is set to true.
 *
 * @unknown ref android.R.styleable#FrameLayout_measureAllChildren
 */
@android.widget.RemoteViews.RemoteView
public class FrameLayout extends android.view.ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = android.view.Gravity.TOP | android.view.Gravity.START;

    @android.view.ViewDebug.ExportedProperty(category = "measurement")
    @android.annotation.UnsupportedAppUsage
    boolean mMeasureAllChildren = false;

    @android.view.ViewDebug.ExportedProperty(category = "padding")
    @android.annotation.UnsupportedAppUsage
    private int mForegroundPaddingLeft = 0;

    @android.view.ViewDebug.ExportedProperty(category = "padding")
    @android.annotation.UnsupportedAppUsage
    private int mForegroundPaddingTop = 0;

    @android.view.ViewDebug.ExportedProperty(category = "padding")
    @android.annotation.UnsupportedAppUsage
    private int mForegroundPaddingRight = 0;

    @android.view.ViewDebug.ExportedProperty(category = "padding")
    @android.annotation.UnsupportedAppUsage
    private int mForegroundPaddingBottom = 0;

    private final java.util.ArrayList<android.view.View> mMatchParentChildren = new java.util.ArrayList<>(1);

    public FrameLayout(@android.annotation.NonNull
    android.content.Context context) {
        super(context);
    }

    public FrameLayout(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayout(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, @android.annotation.AttrRes
    int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FrameLayout(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FrameLayout, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.FrameLayout, attrs, a, defStyleAttr, defStyleRes);
        if (a.getBoolean(R.styleable.FrameLayout_measureAllChildren, false)) {
            setMeasureAllChildren(true);
        }
        a.recycle();
    }

    /**
     * Describes how the foreground is positioned. Defaults to START and TOP.
     *
     * @param foregroundGravity
     * 		See {@link android.view.Gravity}
     * @see #getForegroundGravity()
     * @unknown ref android.R.styleable#View_foregroundGravity
     */
    @android.view.RemotableViewMethod
    public void setForegroundGravity(int foregroundGravity) {
        if (getForegroundGravity() != foregroundGravity) {
            super.setForegroundGravity(foregroundGravity);
            // calling get* again here because the set above may apply default constraints
            final android.graphics.drawable.Drawable foreground = getForeground();
            if ((getForegroundGravity() == android.view.Gravity.FILL) && (foreground != null)) {
                android.graphics.Rect padding = new android.graphics.Rect();
                if (foreground.getPadding(padding)) {
                    mForegroundPaddingLeft = padding.left;
                    mForegroundPaddingTop = padding.top;
                    mForegroundPaddingRight = padding.right;
                    mForegroundPaddingBottom = padding.bottom;
                }
            } else {
                mForegroundPaddingLeft = 0;
                mForegroundPaddingTop = 0;
                mForegroundPaddingRight = 0;
                mForegroundPaddingBottom = 0;
            }
            requestLayout();
        }
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT},
     * and a height of {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}.
     */
    @java.lang.Override
    protected android.widget.FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.widget.FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
    }

    int getPaddingLeftWithForeground() {
        return isForegroundInsidePadding() ? java.lang.Math.max(mPaddingLeft, mForegroundPaddingLeft) : mPaddingLeft + mForegroundPaddingLeft;
    }

    int getPaddingRightWithForeground() {
        return isForegroundInsidePadding() ? java.lang.Math.max(mPaddingRight, mForegroundPaddingRight) : mPaddingRight + mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        return isForegroundInsidePadding() ? java.lang.Math.max(mPaddingTop, mForegroundPaddingTop) : mPaddingTop + mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        return isForegroundInsidePadding() ? java.lang.Math.max(mPaddingBottom, mForegroundPaddingBottom) : mPaddingBottom + mForegroundPaddingBottom;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        final boolean measureMatchParentChildren = (android.view.View.MeasureSpec.getMode(widthMeasureSpec) != android.view.View.MeasureSpec.EXACTLY) || (android.view.View.MeasureSpec.getMode(heightMeasureSpec) != android.view.View.MeasureSpec.EXACTLY);
        mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (mMeasureAllChildren || (child.getVisibility() != android.view.View.GONE)) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (child.getLayoutParams()));
                maxWidth = java.lang.Math.max(maxWidth, (child.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
                maxHeight = java.lang.Math.max(maxHeight, (child.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
                childState = android.view.View.combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if ((lp.width == android.widget.FrameLayout.LayoutParams.MATCH_PARENT) || (lp.height == android.widget.FrameLayout.LayoutParams.MATCH_PARENT)) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }
        // Account for padding too
        maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();
        // Check against our minimum height and width
        maxHeight = java.lang.Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = java.lang.Math.max(maxWidth, getSuggestedMinimumWidth());
        // Check against our foreground's minimum height and width
        final android.graphics.drawable.Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = java.lang.Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = java.lang.Math.max(maxWidth, drawable.getMinimumWidth());
        }
        setMeasuredDimension(android.view.View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState), android.view.View.resolveSizeAndState(maxHeight, heightMeasureSpec, childState << android.view.View.MEASURED_HEIGHT_STATE_SHIFT));
        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final android.view.View child = mMatchParentChildren.get(i);
                final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
                final int childWidthMeasureSpec;
                if (lp.width == android.widget.FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int width = java.lang.Math.max(0, (((getMeasuredWidth() - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - lp.leftMargin) - lp.rightMargin);
                    childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, ((getPaddingLeftWithForeground() + getPaddingRightWithForeground()) + lp.leftMargin) + lp.rightMargin, lp.width);
                }
                final int childHeightMeasureSpec;
                if (lp.height == android.widget.FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int height = java.lang.Math.max(0, (((getMeasuredHeight() - getPaddingTopWithForeground()) - getPaddingBottomWithForeground()) - lp.topMargin) - lp.bottomMargin);
                    childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(heightMeasureSpec, ((getPaddingTopWithForeground() + getPaddingBottomWithForeground()) + lp.topMargin) + lp.bottomMargin, lp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        /* no force left gravity */
        layoutChildren(left, top, right, bottom, false);
    }

    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        final int count = getChildCount();
        final int parentLeft = getPaddingLeftWithForeground();
        final int parentRight = (right - left) - getPaddingRightWithForeground();
        final int parentTop = getPaddingTopWithForeground();
        final int parentBottom = (bottom - top) - getPaddingBottomWithForeground();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (child.getLayoutParams()));
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int childLeft;
                int childTop;
                int gravity = lp.gravity;
                if (gravity == (-1)) {
                    gravity = android.widget.FrameLayout.DEFAULT_CHILD_GRAVITY;
                }
                final int layoutDirection = getLayoutDirection();
                final int absoluteGravity = android.view.Gravity.getAbsoluteGravity(gravity, layoutDirection);
                final int verticalGravity = gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
                switch (absoluteGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case android.view.Gravity.CENTER_HORIZONTAL :
                        childLeft = ((parentLeft + (((parentRight - parentLeft) - width) / 2)) + lp.leftMargin) - lp.rightMargin;
                        break;
                    case android.view.Gravity.RIGHT :
                        if (!forceLeftGravity) {
                            childLeft = (parentRight - width) - lp.rightMargin;
                            break;
                        }
                    case android.view.Gravity.LEFT :
                    default :
                        childLeft = parentLeft + lp.leftMargin;
                }
                switch (verticalGravity) {
                    case android.view.Gravity.TOP :
                        childTop = parentTop + lp.topMargin;
                        break;
                    case android.view.Gravity.CENTER_VERTICAL :
                        childTop = ((parentTop + (((parentBottom - parentTop) - height) / 2)) + lp.topMargin) - lp.bottomMargin;
                        break;
                    case android.view.Gravity.BOTTOM :
                        childTop = (parentBottom - height) - lp.bottomMargin;
                        break;
                    default :
                        childTop = parentTop + lp.topMargin;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    /**
     * Sets whether to consider all children, or just those in
     * the VISIBLE or INVISIBLE state, when measuring. Defaults to false.
     *
     * @param measureAll
     * 		true to consider children marked GONE, false otherwise.
     * 		Default value is false.
     * @unknown ref android.R.styleable#FrameLayout_measureAllChildren
     */
    @android.view.RemotableViewMethod
    public void setMeasureAllChildren(boolean measureAll) {
        mMeasureAllChildren = measureAll;
    }

    /**
     * Determines whether all children, or just those in the VISIBLE or
     * INVISIBLE state, are considered when measuring.
     *
     * @return Whether all children are considered when measuring.
     * @deprecated This method is deprecated in favor of
    {@link #getMeasureAllChildren() getMeasureAllChildren()}, which was
    renamed for consistency with
    {@link #setMeasureAllChildren(boolean) setMeasureAllChildren()}.
     */
    @java.lang.Deprecated
    public boolean getConsiderGoneChildrenWhenMeasuring() {
        return getMeasureAllChildren();
    }

    /**
     * Determines whether all children, or just those in the VISIBLE or
     * INVISIBLE state, are considered when measuring.
     *
     * @return Whether all children are considered when measuring.
     */
    @android.view.inspector.InspectableProperty
    public boolean getMeasureAllChildren() {
        return mMeasureAllChildren;
    }

    @java.lang.Override
    public android.widget.FrameLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.widget.FrameLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.widget.FrameLayout.LayoutParams;
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (android.view.View.sPreserveMarginParamsInLayoutParamConversion) {
            if (lp instanceof android.widget.FrameLayout.LayoutParams) {
                return new android.widget.FrameLayout.LayoutParams(((android.widget.FrameLayout.LayoutParams) (lp)));
            } else
                if (lp instanceof android.view.ViewGroup.MarginLayoutParams) {
                    return new android.widget.FrameLayout.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (lp)));
                }

        }
        return new android.widget.FrameLayout.LayoutParams(lp);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.FrameLayout.class.getName();
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
        encoder.addProperty("measurement:measureAllChildren", mMeasureAllChildren);
        encoder.addProperty("padding:foregroundPaddingLeft", mForegroundPaddingLeft);
        encoder.addProperty("padding:foregroundPaddingTop", mForegroundPaddingTop);
        encoder.addProperty("padding:foregroundPaddingRight", mForegroundPaddingRight);
        encoder.addProperty("padding:foregroundPaddingBottom", mForegroundPaddingBottom);
    }

    /**
     * Per-child layout information for layouts that support margins.
     * See {@link android.R.styleable#FrameLayout_Layout FrameLayout Layout Attributes}
     * for a list of all child view attributes that this class supports.
     *
     * @unknown ref android.R.styleable#FrameLayout_Layout_layout_gravity
     */
    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        /**
         * Value for {@link #gravity} indicating that a gravity has not been
         * explicitly specified.
         */
        public static final int UNSPECIFIED_GRAVITY = -1;

        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         * <p>
         * The default value is {@link #UNSPECIFIED_GRAVITY}, which is treated
         * by FrameLayout as {@code Gravity.TOP | Gravity.START}.
         *
         * @see android.view.Gravity
         * @unknown ref android.R.styleable#FrameLayout_Layout_layout_gravity
         */
        @android.view.inspector.InspectableProperty(name = "layout_gravity", valueType = android.view.inspector.InspectableProperty.ValueType.GRAVITY)
        public int gravity = android.widget.FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY;

        public LayoutParams(@android.annotation.NonNull
        android.content.Context c, @android.annotation.Nullable
        android.util.AttributeSet attrs) {
            super(c, attrs);
            final android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
            gravity = a.getInt(R.styleable.FrameLayout_Layout_layout_gravity, android.widget.FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
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
         * @param gravity
         * 		the gravity
         * @see android.view.Gravity
         */
        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(@android.annotation.NonNull
        android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@android.annotation.NonNull
        android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, and
         * gravity of the source.
         *
         * @param source
         * 		The layout params to copy from.
         */
        public LayoutParams(@android.annotation.NonNull
        android.widget.FrameLayout.LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }
    }
}

