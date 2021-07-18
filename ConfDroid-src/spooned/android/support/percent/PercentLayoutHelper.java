/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.percent;


/**
 * Helper for layouts that want to support percentage based dimensions.
 *
 * <p>This class collects utility methods that are involved in extracting percentage based dimension
 * attributes and applying them to ViewGroup's children. If you would like to implement a layout
 * that supports percentage based dimensions, you need to take several steps:
 *
 * <ol>
 * <li> You need a {@link ViewGroup.LayoutParams} subclass in your ViewGroup that implements
 * {@link android.support.percent.PercentLayoutHelper.PercentLayoutParams}.
 * <li> In your {@code LayoutParams(Context c, AttributeSet attrs)} constructor create an instance
 * of {@link PercentLayoutHelper.PercentLayoutInfo} by calling
 * {@link PercentLayoutHelper#getPercentLayoutInfo(Context, AttributeSet)}. Return this
 * object from {@code public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo()}
 * method that you implemented for {@link android.support.percent.PercentLayoutHelper.PercentLayoutParams} interface.
 * <li> Override
 * {@link ViewGroup.LayoutParams#setBaseAttributes(TypedArray, int, int)}
 * with a single line implementation {@code PercentLayoutHelper.fetchWidthAndHeight(this, a,
 * widthAttr, heightAttr);}
 * <li> In your ViewGroup override {@link ViewGroup#generateLayoutParams(AttributeSet)} to return
 * your LayoutParams.
 * <li> In your {@link ViewGroup#onMeasure(int, int)} override, you need to implement following
 * pattern:
 * <pre class="prettyprint">
 * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 *     mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
 *     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 *     if (mHelper.handleMeasuredStateTooSmall()) {
 *         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 *     }
 * }
 * </pre>
 * <li>In your {@link ViewGroup#onLayout(boolean, int, int, int, int)} override, you need to
 * implement following pattern:
 * <pre class="prettyprint">
 * protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
 *     super.onLayout(changed, left, top, right, bottom);
 *     mHelper.restoreOriginalParams();
 * }
 * </pre>
 * </ol>
 */
public class PercentLayoutHelper {
    private static final java.lang.String TAG = "PercentLayout";

    private static final boolean DEBUG = false;

    private static final boolean VERBOSE = false;

    private final android.view.ViewGroup mHost;

    public PercentLayoutHelper(@android.support.annotation.NonNull
    android.view.ViewGroup host) {
        if (host == null) {
            throw new java.lang.IllegalArgumentException("host must be non-null");
        }
        mHost = host;
    }

    /**
     * Helper method to be called from {@link ViewGroup.LayoutParams#setBaseAttributes} override
     * that reads layout_width and layout_height attribute values without throwing an exception if
     * they aren't present.
     */
    public static void fetchWidthAndHeight(android.view.ViewGroup.LayoutParams params, android.content.res.TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    /**
     * Iterates over children and changes their width and height to one calculated from percentage
     * values.
     *
     * @param widthMeasureSpec
     * 		Width MeasureSpec of the parent ViewGroup.
     * @param heightMeasureSpec
     * 		Height MeasureSpec of the parent ViewGroup.
     */
    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        if (android.support.percent.PercentLayoutHelper.DEBUG) {
            android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, (((("adjustChildren: " + mHost) + " widthMeasureSpec: ") + android.view.View.MeasureSpec.toString(widthMeasureSpec)) + " heightMeasureSpec: ") + android.view.View.MeasureSpec.toString(heightMeasureSpec));
        }
        // Calculate available space, accounting for host's paddings
        int widthHint = (android.view.View.MeasureSpec.getSize(widthMeasureSpec) - mHost.getPaddingLeft()) - mHost.getPaddingRight();
        int heightHint = (android.view.View.MeasureSpec.getSize(heightMeasureSpec) - mHost.getPaddingTop()) - mHost.getPaddingBottom();
        for (int i = 0, N = mHost.getChildCount(); i < N; i++) {
            android.view.View view = mHost.getChildAt(i);
            android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
            if (android.support.percent.PercentLayoutHelper.DEBUG) {
                android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, (("should adjust " + view) + " ") + params);
            }
            if (params instanceof android.support.percent.PercentLayoutHelper.PercentLayoutParams) {
                android.support.percent.PercentLayoutHelper.PercentLayoutInfo info = ((android.support.percent.PercentLayoutHelper.PercentLayoutParams) (params)).getPercentLayoutInfo();
                if (android.support.percent.PercentLayoutHelper.DEBUG) {
                    android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof android.view.ViewGroup.MarginLayoutParams) {
                        info.fillMarginLayoutParams(view, ((android.view.ViewGroup.MarginLayoutParams) (params)), widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    /**
     * Constructs a PercentLayoutInfo from attributes associated with a View. Call this method from
     * {@code LayoutParams(Context c, AttributeSet attrs)} constructor.
     */
    public static android.support.percent.PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo(android.content.Context context, android.util.AttributeSet attrs) {
        android.support.percent.PercentLayoutHelper.PercentLayoutInfo info = null;
        android.content.res.TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout_Layout);
        float value = array.getFraction(R.styleable.PercentLayout_Layout_layout_widthPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent width: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.widthPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_heightPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent height: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.heightPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.leftMarginPercent = value;
            info.topMarginPercent = value;
            info.rightMarginPercent = value;
            info.bottomMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginLeftPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent left margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.leftMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginTopPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent top margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.topMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginRightPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent right margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.rightMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginBottomPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent bottom margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.bottomMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent start margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.startMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "percent end margin: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.endMarginPercent = value;
        }
        value = array.getFraction(R.styleable.PercentLayout_Layout_layout_aspectRatio, 1, 1, -1.0F);
        if (value != (-1.0F)) {
            if (android.support.percent.PercentLayoutHelper.VERBOSE) {
                android.util.Log.v(android.support.percent.PercentLayoutHelper.TAG, "aspect ratio: " + value);
            }
            info = (info != null) ? info : new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            info.aspectRatio = value;
        }
        array.recycle();
        if (android.support.percent.PercentLayoutHelper.DEBUG) {
            android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, "constructed: " + info);
        }
        return info;
    }

    /**
     * Iterates over children and restores their original dimensions that were changed for
     * percentage values. Calling this method only makes sense if you previously called
     * {@link PercentLayoutHelper#adjustChildren(int, int)}.
     */
    public void restoreOriginalParams() {
        for (int i = 0, N = mHost.getChildCount(); i < N; i++) {
            android.view.View view = mHost.getChildAt(i);
            android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
            if (android.support.percent.PercentLayoutHelper.DEBUG) {
                android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, (("should restore " + view) + " ") + params);
            }
            if (params instanceof android.support.percent.PercentLayoutHelper.PercentLayoutParams) {
                android.support.percent.PercentLayoutHelper.PercentLayoutInfo info = ((android.support.percent.PercentLayoutHelper.PercentLayoutParams) (params)).getPercentLayoutInfo();
                if (android.support.percent.PercentLayoutHelper.DEBUG) {
                    android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof android.view.ViewGroup.MarginLayoutParams) {
                        info.restoreMarginLayoutParams(((android.view.ViewGroup.MarginLayoutParams) (params)));
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    /**
     * Iterates over children and checks if any of them would like to get more space than it
     * received through the percentage dimension.
     *
     * If you are building a layout that supports percentage dimensions you are encouraged to take
     * advantage of this method. The developer should be able to specify that a child should be
     * remeasured by adding normal dimension attribute with {@code wrap_content} value. For example
     * he might specify child's attributes as {@code app:layout_widthPercent="60%p"} and
     * {@code android:layout_width="wrap_content"}. In this case if the child receives too little
     * space, it will be remeasured with width set to {@code WRAP_CONTENT}.
     *
     * @return True if the measure phase needs to be rerun because one of the children would like
    to receive more space.
     */
    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        for (int i = 0, N = mHost.getChildCount(); i < N; i++) {
            android.view.View view = mHost.getChildAt(i);
            android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
            if (android.support.percent.PercentLayoutHelper.DEBUG) {
                android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, (("should handle measured state too small " + view) + " ") + params);
            }
            if (params instanceof android.support.percent.PercentLayoutHelper.PercentLayoutParams) {
                android.support.percent.PercentLayoutHelper.PercentLayoutInfo info = ((android.support.percent.PercentLayoutHelper.PercentLayoutParams) (params)).getPercentLayoutInfo();
                if (info != null) {
                    if (android.support.percent.PercentLayoutHelper.shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    if (android.support.percent.PercentLayoutHelper.shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }
        }
        if (android.support.percent.PercentLayoutHelper.DEBUG) {
            android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, "should trigger second measure pass: " + needsSecondMeasure);
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(android.view.View view, android.support.percent.PercentLayoutHelper.PercentLayoutInfo info) {
        int state = android.support.v4.view.ViewCompat.getMeasuredWidthAndState(view) & android.support.v4.view.ViewCompat.MEASURED_STATE_MASK;
        return ((state == android.support.v4.view.ViewCompat.MEASURED_STATE_TOO_SMALL) && (info.widthPercent >= 0)) && (info.mPreservedParams.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(android.view.View view, android.support.percent.PercentLayoutHelper.PercentLayoutInfo info) {
        int state = android.support.v4.view.ViewCompat.getMeasuredHeightAndState(view) & android.support.v4.view.ViewCompat.MEASURED_STATE_MASK;
        return ((state == android.support.v4.view.ViewCompat.MEASURED_STATE_TOO_SMALL) && (info.heightPercent >= 0)) && (info.mPreservedParams.height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /* package */
    static class PercentMarginLayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        // These two flags keep track of whether we're computing the LayoutParams width and height
        // in the fill pass based on the aspect ratio. This allows the fill pass to be re-entrant
        // as the framework code can call onMeasure() multiple times before the onLayout() is
        // called. Those multiple invocations of onMeasure() are not guaranteed to be called with
        // the same set of width / height.
        private boolean mIsHeightComputedFromAspectRatio;

        private boolean mIsWidthComputedFromAspectRatio;

        public PercentMarginLayoutParams(int width, int height) {
            super(width, height);
        }
    }

    /**
     * Container for information about percentage dimensions and margins. It acts as an extension
     * for {@code LayoutParams}.
     */
    public static class PercentLayoutInfo {
        /**
         * The decimal value of the percentage-based width.
         */
        public float widthPercent;

        /**
         * The decimal value of the percentage-based height.
         */
        public float heightPercent;

        /**
         * The decimal value of the percentage-based left margin.
         */
        public float leftMarginPercent;

        /**
         * The decimal value of the percentage-based top margin.
         */
        public float topMarginPercent;

        /**
         * The decimal value of the percentage-based right margin.
         */
        public float rightMarginPercent;

        /**
         * The decimal value of the percentage-based bottom margin.
         */
        public float bottomMarginPercent;

        /**
         * The decimal value of the percentage-based start margin.
         */
        public float startMarginPercent;

        /**
         * The decimal value of the percentage-based end margin.
         */
        public float endMarginPercent;

        /**
         * The decimal value of the percentage-based aspect ratio.
         */
        public float aspectRatio;

        /* package */
        final android.support.percent.PercentLayoutHelper.PercentMarginLayoutParams mPreservedParams;

        public PercentLayoutInfo() {
            widthPercent = -1.0F;
            heightPercent = -1.0F;
            leftMarginPercent = -1.0F;
            topMarginPercent = -1.0F;
            rightMarginPercent = -1.0F;
            bottomMarginPercent = -1.0F;
            startMarginPercent = -1.0F;
            endMarginPercent = -1.0F;
            mPreservedParams = new android.support.percent.PercentLayoutHelper.PercentMarginLayoutParams(0, 0);
        }

        /**
         * Fills the {@link ViewGroup.LayoutParams#width} and {@link ViewGroup.LayoutParams#height}
         * fields of the passed {@link ViewGroup.LayoutParams} object based on currently set
         * percentage values.
         */
        public void fillLayoutParams(android.view.ViewGroup.LayoutParams params, int widthHint, int heightHint) {
            // Preserve the original layout params, so we can restore them after the measure step.
            mPreservedParams.width = params.width;
            mPreservedParams.height = params.height;
            // We assume that width/height set to 0 means that value was unset. This might not
            // necessarily be true, as the user might explicitly set it to 0. However, we use this
            // information only for the aspect ratio. If the user set the aspect ratio attribute,
            // it means they accept or soon discover that it will be disregarded.
            final boolean widthNotSet = (mPreservedParams.mIsWidthComputedFromAspectRatio || (mPreservedParams.width == 0)) && (widthPercent < 0);
            final boolean heightNotSet = (mPreservedParams.mIsHeightComputedFromAspectRatio || (mPreservedParams.height == 0)) && (heightPercent < 0);
            if (widthPercent >= 0) {
                params.width = java.lang.Math.round(widthHint * widthPercent);
            }
            if (heightPercent >= 0) {
                params.height = java.lang.Math.round(heightHint * heightPercent);
            }
            if (aspectRatio >= 0) {
                if (widthNotSet) {
                    params.width = java.lang.Math.round(params.height * aspectRatio);
                    // Keep track that we've filled the width based on the height and aspect ratio.
                    mPreservedParams.mIsWidthComputedFromAspectRatio = true;
                }
                if (heightNotSet) {
                    params.height = java.lang.Math.round(params.width / aspectRatio);
                    // Keep track that we've filled the height based on the width and aspect ratio.
                    mPreservedParams.mIsHeightComputedFromAspectRatio = true;
                }
            }
            if (android.support.percent.PercentLayoutHelper.DEBUG) {
                android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, ((("after fillLayoutParams: (" + params.width) + ", ") + params.height) + ")");
            }
        }

        /**
         *
         *
         * @deprecated Use
        {@link #fillMarginLayoutParams(View, ViewGroup.MarginLayoutParams, int, int)}
        for proper RTL support.
         */
        @java.lang.Deprecated
        public void fillMarginLayoutParams(android.view.ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillMarginLayoutParams(null, params, widthHint, heightHint);
        }

        /**
         * Fills the margin fields of the passed {@link ViewGroup.MarginLayoutParams} object based
         * on currently set percentage values and the current layout direction of the passed
         * {@link View}.
         */
        public void fillMarginLayoutParams(android.view.View view, android.view.ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);
            // Preserve the original margins, so we can restore them after the measure step.
            mPreservedParams.leftMargin = params.leftMargin;
            mPreservedParams.topMargin = params.topMargin;
            mPreservedParams.rightMargin = params.rightMargin;
            mPreservedParams.bottomMargin = params.bottomMargin;
            android.support.v4.view.MarginLayoutParamsCompat.setMarginStart(mPreservedParams, android.support.v4.view.MarginLayoutParamsCompat.getMarginStart(params));
            android.support.v4.view.MarginLayoutParamsCompat.setMarginEnd(mPreservedParams, android.support.v4.view.MarginLayoutParamsCompat.getMarginEnd(params));
            if (leftMarginPercent >= 0) {
                params.leftMargin = java.lang.Math.round(widthHint * leftMarginPercent);
            }
            if (topMarginPercent >= 0) {
                params.topMargin = java.lang.Math.round(heightHint * topMarginPercent);
            }
            if (rightMarginPercent >= 0) {
                params.rightMargin = java.lang.Math.round(widthHint * rightMarginPercent);
            }
            if (bottomMarginPercent >= 0) {
                params.bottomMargin = java.lang.Math.round(heightHint * bottomMarginPercent);
            }
            boolean shouldResolveLayoutDirection = false;
            if (startMarginPercent >= 0) {
                android.support.v4.view.MarginLayoutParamsCompat.setMarginStart(params, java.lang.Math.round(widthHint * startMarginPercent));
                shouldResolveLayoutDirection = true;
            }
            if (endMarginPercent >= 0) {
                android.support.v4.view.MarginLayoutParamsCompat.setMarginEnd(params, java.lang.Math.round(widthHint * endMarginPercent));
                shouldResolveLayoutDirection = true;
            }
            if (shouldResolveLayoutDirection && (view != null)) {
                // Force the resolve pass so that start / end margins are propagated to the
                // matching left / right fields
                android.support.v4.view.MarginLayoutParamsCompat.resolveLayoutDirection(params, android.support.v4.view.ViewCompat.getLayoutDirection(view));
            }
            if (android.support.percent.PercentLayoutHelper.DEBUG) {
                android.util.Log.d(android.support.percent.PercentLayoutHelper.TAG, ((("after fillMarginLayoutParams: (" + params.width) + ", ") + params.height) + ")");
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return java.lang.String.format("PercentLayoutInformation width: %f height %f, margins (%f, %f, " + " %f, %f, %f, %f)", widthPercent, heightPercent, leftMarginPercent, topMarginPercent, rightMarginPercent, bottomMarginPercent, startMarginPercent, endMarginPercent);
        }

        /**
         * Restores the original dimensions and margins after they were changed for percentage based
         * values. You should call this method only if you previously called
         * {@link PercentLayoutHelper.PercentLayoutInfo#fillMarginLayoutParams(View, ViewGroup.MarginLayoutParams, int, int)}.
         */
        public void restoreMarginLayoutParams(android.view.ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = mPreservedParams.leftMargin;
            params.topMargin = mPreservedParams.topMargin;
            params.rightMargin = mPreservedParams.rightMargin;
            params.bottomMargin = mPreservedParams.bottomMargin;
            android.support.v4.view.MarginLayoutParamsCompat.setMarginStart(params, android.support.v4.view.MarginLayoutParamsCompat.getMarginStart(mPreservedParams));
            android.support.v4.view.MarginLayoutParamsCompat.setMarginEnd(params, android.support.v4.view.MarginLayoutParamsCompat.getMarginEnd(mPreservedParams));
        }

        /**
         * Restores original dimensions after they were changed for percentage based values.
         * You should call this method only if you previously called
         * {@link PercentLayoutHelper.PercentLayoutInfo#fillLayoutParams(ViewGroup.LayoutParams, int, int)}.
         */
        public void restoreLayoutParams(android.view.ViewGroup.LayoutParams params) {
            if (!mPreservedParams.mIsWidthComputedFromAspectRatio) {
                // Only restore the width if we didn't compute it based on the height and
                // aspect ratio in the fill pass.
                params.width = mPreservedParams.width;
            }
            if (!mPreservedParams.mIsHeightComputedFromAspectRatio) {
                // Only restore the height if we didn't compute it based on the width and
                // aspect ratio in the fill pass.
                params.height = mPreservedParams.height;
            }
            // Reset the tracking flags.
            mPreservedParams.mIsWidthComputedFromAspectRatio = false;
            mPreservedParams.mIsHeightComputedFromAspectRatio = false;
        }
    }

    /**
     * If a layout wants to support percentage based dimensions and use this helper class, its
     * {@code LayoutParams} subclass must implement this interface.
     *
     * Your {@code LayoutParams} subclass should contain an instance of {@code PercentLayoutInfo}
     * and the implementation of this interface should be a simple accessor.
     */
    public interface PercentLayoutParams {
        android.support.percent.PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo();
    }
}

