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
 * A FrameLayout with a rounded corner background and shadow.
 * <p>
 * CardView uses <code>elevation</code> property on Lollipop for shadows and falls back to a
 * custom emulated shadow implementation on older platforms.
 * <p>
 * Due to expensive nature of rounded corner clipping, on platforms before Lollipop, CardView does
 * not clip its children that intersect with rounded corners. Instead, it adds padding to avoid such
 * intersection (See {@link #setPreventCornerOverlap(boolean)} to change this behavior).
 * <p>
 * Before Lollipop, CardView adds padding to its content and draws shadows to that area. This
 * padding amount is equal to <code>maxCardElevation + (1 - cos45) * cornerRadius</code> on the
 * sides and <code>maxCardElevation * 1.5 + (1 - cos45) * cornerRadius</code> on top and bottom.
 * <p>
 * Since padding is used to offset content for shadows, you cannot set padding on CardView.
 * Instead, you can use content padding attributes in XML or
 * {@link #setContentPadding(int, int, int, int)} in code to set the padding between the edges of
 * the CardView and children of CardView.
 * <p>
 * Note that, if you specify exact dimensions for the CardView, because of the shadows, its content
 * area will be different between platforms before Lollipop and after Lollipop. By using api version
 * specific resource values, you can avoid these changes. Alternatively, If you want CardView to add
 * inner padding on platforms Lollipop and after as well, you can call
 * {@link #setUseCompatPadding(boolean)} and pass <code>true</code>.
 * <p>
 * To change CardView's elevation in a backward compatible way, use
 * {@link #setCardElevation(float)}. CardView will use elevation API on Lollipop and before
 * Lollipop, it will change the shadow size. To avoid moving the View while shadow size is changing,
 * shadow size is clamped by {@link #getMaxCardElevation()}. If you want to change elevation
 * dynamically, you should call {@link #setMaxCardElevation(float)} when CardView is initialized.
 *
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardBackgroundColor
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardCornerRadius
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardElevation
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardMaxElevation
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardUseCompatPadding
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardPreventCornerOverlap
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPadding
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingLeft
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingTop
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingRight
 * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingBottom
 */
public class CardView extends android.widget.FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = new int[]{ android.R.attr.colorBackground };

    private static final android.support.v7.widget.CardViewImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            IMPL = new android.support.v7.widget.CardViewApi21();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                IMPL = new android.support.v7.widget.CardViewJellybeanMr1();
            } else {
                IMPL = new android.support.v7.widget.CardViewGingerbread();
            }

        android.support.v7.widget.CardView.IMPL.initStatic();
    }

    private boolean mCompatPadding;

    private boolean mPreventCornerOverlap;

    int mUserSetMinWidth;

    /**
     * CardView requires to have a particular minimum size to draw shadows before API 21. If
     * developer also sets min width/height, they might be overridden.
     *
     * CardView works around this issue by recording user given parameters and using an internal
     * method to set them.
     */
    int mUserSetMinHeight;

    final android.graphics.Rect mContentPadding = new android.graphics.Rect();

    final android.graphics.Rect mShadowBounds = new android.graphics.Rect();

    public CardView(android.content.Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CardView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CardView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    @java.lang.Override
    public void setPadding(int left, int top, int right, int bottom) {
        // NO OP
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // NO OP
    }

    /**
     * Returns whether CardView will add inner padding on platforms Lollipop and after.
     *
     * @return <code>true</code> if CardView adds inner padding on platforms Lollipop and after to
    have same dimensions with platforms before Lollipop.
     */
    public boolean getUseCompatPadding() {
        return mCompatPadding;
    }

    /**
     * CardView adds additional padding to draw shadows on platforms before Lollipop.
     * <p>
     * This may cause Cards to have different sizes between Lollipop and before Lollipop. If you
     * need to align CardView with other Views, you may need api version specific dimension
     * resources to account for the changes.
     * As an alternative, you can set this flag to <code>true</code> and CardView will add the same
     * padding values on platforms Lollipop and after.
     * <p>
     * Since setting this flag to true adds unnecessary gaps in the UI, default value is
     * <code>false</code>.
     *
     * @param useCompatPadding
     * 		<code>true></code> if CardView should add padding for the shadows on
     * 		platforms Lollipop and above.
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardUseCompatPadding
     */
    public void setUseCompatPadding(boolean useCompatPadding) {
        if (mCompatPadding != useCompatPadding) {
            mCompatPadding = useCompatPadding;
            android.support.v7.widget.CardView.IMPL.onCompatPaddingChanged(mCardViewDelegate);
        }
    }

    /**
     * Sets the padding between the Card's edges and the children of CardView.
     * <p>
     * Depending on platform version or {@link #getUseCompatPadding()} settings, CardView may
     * update these values before calling {@link android.view.View#setPadding(int, int, int, int)}.
     *
     * @param left
     * 		The left padding in pixels
     * @param top
     * 		The top padding in pixels
     * @param right
     * 		The right padding in pixels
     * @param bottom
     * 		The bottom padding in pixels
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPadding
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingLeft
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingTop
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingRight
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_contentPaddingBottom
     */
    public void setContentPadding(int left, int top, int right, int bottom) {
        mContentPadding.set(left, top, right, bottom);
        android.support.v7.widget.CardView.IMPL.updatePadding(mCardViewDelegate);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!(android.support.v7.widget.CardView.IMPL instanceof android.support.v7.widget.CardViewApi21)) {
            final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
            switch (widthMode) {
                case android.view.View.MeasureSpec.EXACTLY :
                case android.view.View.MeasureSpec.AT_MOST :
                    final int minWidth = ((int) (java.lang.Math.ceil(android.support.v7.widget.CardView.IMPL.getMinWidth(mCardViewDelegate))));
                    widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.max(minWidth, android.view.View.MeasureSpec.getSize(widthMeasureSpec)), widthMode);
                    break;
            }
            final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
            switch (heightMode) {
                case android.view.View.MeasureSpec.EXACTLY :
                case android.view.View.MeasureSpec.AT_MOST :
                    final int minHeight = ((int) (java.lang.Math.ceil(android.support.v7.widget.CardView.IMPL.getMinHeight(mCardViewDelegate))));
                    heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.max(minHeight, android.view.View.MeasureSpec.getSize(heightMeasureSpec)), heightMode);
                    break;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initialize(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardView, defStyleAttr, R.style.CardView);
        android.content.res.ColorStateList backgroundColor;
        if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
            backgroundColor = a.getColorStateList(R.styleable.CardView_cardBackgroundColor);
        } else {
            // There isn't one set, so we'll compute one based on the theme
            final android.content.res.TypedArray aa = getContext().obtainStyledAttributes(android.support.v7.widget.CardView.COLOR_BACKGROUND_ATTR);
            final int themeColorBackground = aa.getColor(0, 0);
            aa.recycle();
            // If the theme colorBackground is light, use our own light color, otherwise dark
            final float[] hsv = new float[3];
            android.graphics.Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = android.content.res.ColorStateList.valueOf(hsv[2] > 0.5F ? getResources().getColor(R.color.cardview_light_background) : getResources().getColor(R.color.cardview_dark_background));
        }
        float radius = a.getDimension(R.styleable.CardView_cardCornerRadius, 0);
        float elevation = a.getDimension(R.styleable.CardView_cardElevation, 0);
        float maxElevation = a.getDimension(R.styleable.CardView_cardMaxElevation, 0);
        mCompatPadding = a.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
        mPreventCornerOverlap = a.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
        int defaultPadding = a.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
        mContentPadding.left = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft, defaultPadding);
        mContentPadding.top = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop, defaultPadding);
        mContentPadding.right = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight, defaultPadding);
        mContentPadding.bottom = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom, defaultPadding);
        if (elevation > maxElevation) {
            maxElevation = elevation;
        }
        mUserSetMinWidth = a.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
        mUserSetMinHeight = a.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
        a.recycle();
        android.support.v7.widget.CardView.IMPL.initialize(mCardViewDelegate, context, backgroundColor, radius, elevation, maxElevation);
    }

    @java.lang.Override
    public void setMinimumWidth(int minWidth) {
        mUserSetMinWidth = minWidth;
        super.setMinimumWidth(minWidth);
    }

    @java.lang.Override
    public void setMinimumHeight(int minHeight) {
        mUserSetMinHeight = minHeight;
        super.setMinimumHeight(minHeight);
    }

    /**
     * Updates the background color of the CardView
     *
     * @param color
     * 		The new color to set for the card background
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardBackgroundColor
     */
    public void setCardBackgroundColor(@android.support.annotation.ColorInt
    int color) {
        android.support.v7.widget.CardView.IMPL.setBackgroundColor(mCardViewDelegate, android.content.res.ColorStateList.valueOf(color));
    }

    /**
     * Updates the background ColorStateList of the CardView
     *
     * @param color
     * 		The new ColorStateList to set for the card background
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardBackgroundColor
     */
    public void setCardBackgroundColor(@android.support.annotation.Nullable
    android.content.res.ColorStateList color) {
        android.support.v7.widget.CardView.IMPL.setBackgroundColor(mCardViewDelegate, color);
    }

    /**
     * Returns the background color state list of the CardView.
     *
     * @return The background color state list of the CardView.
     */
    public android.content.res.ColorStateList getCardBackgroundColor() {
        return android.support.v7.widget.CardView.IMPL.getBackgroundColor(mCardViewDelegate);
    }

    /**
     * Returns the inner padding after the Card's left edge
     *
     * @return the inner padding after the Card's left edge
     */
    public int getContentPaddingLeft() {
        return mContentPadding.left;
    }

    /**
     * Returns the inner padding before the Card's right edge
     *
     * @return the inner padding before the Card's right edge
     */
    public int getContentPaddingRight() {
        return mContentPadding.right;
    }

    /**
     * Returns the inner padding after the Card's top edge
     *
     * @return the inner padding after the Card's top edge
     */
    public int getContentPaddingTop() {
        return mContentPadding.top;
    }

    /**
     * Returns the inner padding before the Card's bottom edge
     *
     * @return the inner padding before the Card's bottom edge
     */
    public int getContentPaddingBottom() {
        return mContentPadding.bottom;
    }

    /**
     * Updates the corner radius of the CardView.
     *
     * @param radius
     * 		The radius in pixels of the corners of the rectangle shape
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardCornerRadius
     * @see #setRadius(float)
     */
    public void setRadius(float radius) {
        android.support.v7.widget.CardView.IMPL.setRadius(mCardViewDelegate, radius);
    }

    /**
     * Returns the corner radius of the CardView.
     *
     * @return Corner radius of the CardView
     * @see #getRadius()
     */
    public float getRadius() {
        return android.support.v7.widget.CardView.IMPL.getRadius(mCardViewDelegate);
    }

    /**
     * Updates the backward compatible elevation of the CardView.
     *
     * @param elevation
     * 		The backward compatible elevation in pixels.
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardElevation
     * @see #getCardElevation()
     * @see #setMaxCardElevation(float)
     */
    public void setCardElevation(float elevation) {
        android.support.v7.widget.CardView.IMPL.setElevation(mCardViewDelegate, elevation);
    }

    /**
     * Returns the backward compatible elevation of the CardView.
     *
     * @return Elevation of the CardView
     * @see #setCardElevation(float)
     * @see #getMaxCardElevation()
     */
    public float getCardElevation() {
        return android.support.v7.widget.CardView.IMPL.getElevation(mCardViewDelegate);
    }

    /**
     * Updates the backward compatible maximum elevation of the CardView.
     * <p>
     * Calling this method has no effect if device OS version is Lollipop or newer and
     * {@link #getUseCompatPadding()} is <code>false</code>.
     *
     * @param maxElevation
     * 		The backward compatible maximum elevation in pixels.
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardMaxElevation
     * @see #setCardElevation(float)
     * @see #getMaxCardElevation()
     */
    public void setMaxCardElevation(float maxElevation) {
        android.support.v7.widget.CardView.IMPL.setMaxElevation(mCardViewDelegate, maxElevation);
    }

    /**
     * Returns the backward compatible maximum elevation of the CardView.
     *
     * @return Maximum elevation of the CardView
     * @see #setMaxCardElevation(float)
     * @see #getCardElevation()
     */
    public float getMaxCardElevation() {
        return android.support.v7.widget.CardView.IMPL.getMaxElevation(mCardViewDelegate);
    }

    /**
     * Returns whether CardView should add extra padding to content to avoid overlaps with rounded
     * corners on pre-Lollipop platforms.
     *
     * @return True if CardView prevents overlaps with rounded corners on platforms before Lollipop.
    Default value is <code>true</code>.
     */
    public boolean getPreventCornerOverlap() {
        return mPreventCornerOverlap;
    }

    /**
     * On pre-Lollipop platforms, CardView does not clip the bounds of the Card for the rounded
     * corners. Instead, it adds padding to content so that it won't overlap with the rounded
     * corners. You can disable this behavior by setting this field to <code>false</code>.
     * <p>
     * Setting this value on Lollipop and above does not have any effect unless you have enabled
     * compatibility padding.
     *
     * @param preventCornerOverlap
     * 		Whether CardView should add extra padding to content to avoid
     * 		overlaps with the CardView corners.
     * @unknown ref android.support.v7.cardview.R.styleable#CardView_cardPreventCornerOverlap
     * @see #setUseCompatPadding(boolean)
     */
    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        if (preventCornerOverlap != mPreventCornerOverlap) {
            mPreventCornerOverlap = preventCornerOverlap;
            android.support.v7.widget.CardView.IMPL.onPreventCornerOverlapChanged(mCardViewDelegate);
        }
    }

    private final android.support.v7.widget.CardViewDelegate mCardViewDelegate = new android.support.v7.widget.CardViewDelegate() {
        private android.graphics.drawable.Drawable mCardBackground;

        @java.lang.Override
        public void setCardBackground(android.graphics.drawable.Drawable drawable) {
            mCardBackground = drawable;
            setBackgroundDrawable(drawable);
        }

        @java.lang.Override
        public boolean getUseCompatPadding() {
            return android.support.v7.widget.CardView.this.getUseCompatPadding();
        }

        @java.lang.Override
        public boolean getPreventCornerOverlap() {
            return android.support.v7.widget.CardView.this.getPreventCornerOverlap();
        }

        @java.lang.Override
        public void setShadowPadding(int left, int top, int right, int bottom) {
            mShadowBounds.set(left, top, right, bottom);
            android.support.v7.widget.CardView.super.setPadding(left + mContentPadding.left, top + mContentPadding.top, right + mContentPadding.right, bottom + mContentPadding.bottom);
        }

        @java.lang.Override
        public void setMinWidthHeightInternal(int width, int height) {
            if (width > mUserSetMinWidth) {
                android.support.v7.widget.CardView.super.setMinimumWidth(width);
            }
            if (height > mUserSetMinHeight) {
                android.support.v7.widget.CardView.super.setMinimumHeight(height);
            }
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getCardBackground() {
            return mCardBackground;
        }

        @java.lang.Override
        public android.view.View getCardView() {
            return android.support.v7.widget.CardView.this;
        }
    };
}

