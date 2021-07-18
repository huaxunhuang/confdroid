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
 * <p>A widget that draws a search affordance, represented by a round background and an icon.</p>
 *
 * The background color and icon can be customized.
 */
public class SearchOrbView extends android.widget.FrameLayout implements android.view.View.OnClickListener {
    private android.view.View.OnClickListener mListener;

    private android.view.View mRootView;

    private android.view.View mSearchOrbView;

    private android.widget.ImageView mIcon;

    private android.graphics.drawable.Drawable mIconDrawable;

    private android.support.v17.leanback.widget.SearchOrbView.Colors mColors;

    private final float mFocusedZoom;

    private final int mPulseDurationMs;

    private final int mScaleDurationMs;

    private final float mUnfocusedZ;

    private final float mFocusedZ;

    private android.animation.ValueAnimator mColorAnimator;

    private boolean mColorAnimationEnabled;

    private boolean mAttachedToWindow;

    /**
     * A set of colors used to display the search orb.
     */
    public static class Colors {
        private static final float sBrightnessAlpha = 0.15F;

        /**
         * Constructs a color set using the given color for the search orb.
         * Other colors are provided by the framework.
         *
         * @param color
         * 		The main search orb color.
         */
        public Colors(@android.support.annotation.ColorInt
        int color) {
            this(color, color);
        }

        /**
         * Constructs a color set using the given colors for the search orb.
         * Other colors are provided by the framework.
         *
         * @param color
         * 		The main search orb color.
         * @param brightColor
         * 		A brighter version of the search orb used for animation.
         */
        public Colors(@android.support.annotation.ColorInt
        int color, @android.support.annotation.ColorInt
        int brightColor) {
            this(color, brightColor, android.graphics.Color.TRANSPARENT);
        }

        /**
         * Constructs a color set using the given colors.
         *
         * @param color
         * 		The main search orb color.
         * @param brightColor
         * 		A brighter version of the search orb used for animation.
         * @param iconColor
         * 		A color used to tint the search orb icon.
         */
        public Colors(@android.support.annotation.ColorInt
        int color, @android.support.annotation.ColorInt
        int brightColor, @android.support.annotation.ColorInt
        int iconColor) {
            this.color = color;
            this.brightColor = (brightColor == color) ? android.support.v17.leanback.widget.SearchOrbView.Colors.getBrightColor(color) : brightColor;
            this.iconColor = iconColor;
        }

        /**
         * The main color of the search orb.
         */
        @android.support.annotation.ColorInt
        public int color;

        /**
         * A brighter version of the search orb used for animation.
         */
        @android.support.annotation.ColorInt
        public int brightColor;

        /**
         * A color used to tint the search orb icon.
         */
        @android.support.annotation.ColorInt
        public int iconColor;

        /**
         * Computes a default brighter version of the given color.
         */
        public static int getBrightColor(int color) {
            final float brightnessValue = 0xff * android.support.v17.leanback.widget.SearchOrbView.Colors.sBrightnessAlpha;
            int red = ((int) ((android.graphics.Color.red(color) * (1 - android.support.v17.leanback.widget.SearchOrbView.Colors.sBrightnessAlpha)) + brightnessValue));
            int green = ((int) ((android.graphics.Color.green(color) * (1 - android.support.v17.leanback.widget.SearchOrbView.Colors.sBrightnessAlpha)) + brightnessValue));
            int blue = ((int) ((android.graphics.Color.blue(color) * (1 - android.support.v17.leanback.widget.SearchOrbView.Colors.sBrightnessAlpha)) + brightnessValue));
            int alpha = ((int) ((android.graphics.Color.alpha(color) * (1 - android.support.v17.leanback.widget.SearchOrbView.Colors.sBrightnessAlpha)) + brightnessValue));
            return android.graphics.Color.argb(alpha, red, green, blue);
        }
    }

    private final android.animation.ArgbEvaluator mColorEvaluator = new android.animation.ArgbEvaluator();

    private final android.animation.ValueAnimator.AnimatorUpdateListener mUpdateListener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
        @java.lang.Override
        public void onAnimationUpdate(android.animation.ValueAnimator animator) {
            java.lang.Integer color = ((java.lang.Integer) (animator.getAnimatedValue()));
            setOrbViewColor(color.intValue());
        }
    };

    private android.animation.ValueAnimator mShadowFocusAnimator;

    private final android.animation.ValueAnimator.AnimatorUpdateListener mFocusUpdateListener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
        @java.lang.Override
        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
            setSearchOrbZ(animation.getAnimatedFraction());
        }
    };

    void setSearchOrbZ(float fraction) {
        android.support.v17.leanback.widget.ShadowHelper.getInstance().setZ(mSearchOrbView, mUnfocusedZ + (fraction * (mFocusedZ - mUnfocusedZ)));
    }

    public SearchOrbView(android.content.Context context) {
        this(context, null);
    }

    public SearchOrbView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.searchOrbViewStyle);
    }

    public SearchOrbView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final android.content.res.Resources res = context.getResources();
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mRootView = inflater.inflate(getLayoutResourceId(), this, true);
        mSearchOrbView = mRootView.findViewById(R.id.search_orb);
        mIcon = ((android.widget.ImageView) (mRootView.findViewById(R.id.icon)));
        mFocusedZoom = context.getResources().getFraction(R.fraction.lb_search_orb_focused_zoom, 1, 1);
        mPulseDurationMs = context.getResources().getInteger(R.integer.lb_search_orb_pulse_duration_ms);
        mScaleDurationMs = context.getResources().getInteger(R.integer.lb_search_orb_scale_duration_ms);
        mFocusedZ = context.getResources().getDimensionPixelSize(R.dimen.lb_search_orb_focused_z);
        mUnfocusedZ = context.getResources().getDimensionPixelSize(R.dimen.lb_search_orb_unfocused_z);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbSearchOrbView, defStyleAttr, 0);
        android.graphics.drawable.Drawable img = a.getDrawable(R.styleable.lbSearchOrbView_searchOrbIcon);
        if (img == null) {
            img = res.getDrawable(R.drawable.lb_ic_in_app_search);
        }
        setOrbIcon(img);
        int defColor = res.getColor(R.color.lb_default_search_color);
        int color = a.getColor(R.styleable.lbSearchOrbView_searchOrbColor, defColor);
        int brightColor = a.getColor(R.styleable.lbSearchOrbView_searchOrbBrightColor, color);
        int iconColor = a.getColor(R.styleable.lbSearchOrbView_searchOrbIconColor, android.graphics.Color.TRANSPARENT);
        setOrbColors(new android.support.v17.leanback.widget.SearchOrbView.Colors(color, brightColor, iconColor));
        a.recycle();
        setFocusable(true);
        setClipChildren(false);
        setOnClickListener(this);
        setSoundEffectsEnabled(false);
        setSearchOrbZ(0);
        // Icon has no background, but must be on top of the search orb view
        android.support.v17.leanback.widget.ShadowHelper.getInstance().setZ(mIcon, mFocusedZ);
    }

    int getLayoutResourceId() {
        return R.layout.lb_search_orb;
    }

    void scaleOrbViewOnly(float scale) {
        mSearchOrbView.setScaleX(scale);
        mSearchOrbView.setScaleY(scale);
    }

    float getFocusedZoom() {
        return mFocusedZoom;
    }

    @java.lang.Override
    public void onClick(android.view.View view) {
        if (null != mListener) {
            mListener.onClick(view);
        }
    }

    private void startShadowFocusAnimation(boolean gainFocus, int duration) {
        if (mShadowFocusAnimator == null) {
            mShadowFocusAnimator = android.animation.ValueAnimator.ofFloat(0.0F, 1.0F);
            mShadowFocusAnimator.addUpdateListener(mFocusUpdateListener);
        }
        if (gainFocus) {
            mShadowFocusAnimator.start();
        } else {
            mShadowFocusAnimator.reverse();
        }
        mShadowFocusAnimator.setDuration(duration);
    }

    @java.lang.Override
    protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        animateOnFocus(gainFocus);
    }

    void animateOnFocus(boolean hasFocus) {
        final float zoom = (hasFocus) ? mFocusedZoom : 1.0F;
        mRootView.animate().scaleX(zoom).scaleY(zoom).setDuration(mScaleDurationMs).start();
        startShadowFocusAnimation(hasFocus, mScaleDurationMs);
        enableOrbColorAnimation(hasFocus);
    }

    /**
     * Sets the orb icon.
     *
     * @param icon
     * 		the drawable to be used as the icon
     */
    public void setOrbIcon(android.graphics.drawable.Drawable icon) {
        mIconDrawable = icon;
        mIcon.setImageDrawable(mIconDrawable);
    }

    /**
     * Returns the orb icon
     *
     * @return the drawable used as the icon
     */
    public android.graphics.drawable.Drawable getOrbIcon() {
        return mIconDrawable;
    }

    /**
     * Sets the on click listener for the orb.
     *
     * @param listener
     * 		The listener.
     */
    public void setOnOrbClickedListener(android.view.View.OnClickListener listener) {
        mListener = listener;
    }

    /**
     * Sets the background color of the search orb.
     * Other colors will be provided by the framework.
     *
     * @param color
     * 		the RGBA color
     */
    public void setOrbColor(int color) {
        setOrbColors(new android.support.v17.leanback.widget.SearchOrbView.Colors(color, color, android.graphics.Color.TRANSPARENT));
    }

    /**
     * Sets the search orb colors.
     * Other colors are provided by the framework.
     *
     * @deprecated Use {@link #setOrbColors(Colors)} instead.
     */
    @java.lang.Deprecated
    public void setOrbColor(@android.support.annotation.ColorInt
    int color, @android.support.annotation.ColorInt
    int brightColor) {
        setOrbColors(new android.support.v17.leanback.widget.SearchOrbView.Colors(color, brightColor, android.graphics.Color.TRANSPARENT));
    }

    /**
     * Returns the orb color
     *
     * @return the RGBA color
     */
    @android.support.annotation.ColorInt
    public int getOrbColor() {
        return mColors.color;
    }

    /**
     * Sets the {@link Colors} used to display the search orb.
     */
    public void setOrbColors(android.support.v17.leanback.widget.SearchOrbView.Colors colors) {
        mColors = colors;
        mIcon.setColorFilter(mColors.iconColor);
        if (mColorAnimator == null) {
            setOrbViewColor(mColors.color);
        } else {
            enableOrbColorAnimation(true);
        }
    }

    /**
     * Returns the {@link Colors} used to display the search orb.
     */
    public android.support.v17.leanback.widget.SearchOrbView.Colors getOrbColors() {
        return mColors;
    }

    /**
     * Enables or disables the orb color animation.
     *
     * <p>
     * Orb color animation is handled automatically when the orb is focused/unfocused,
     * however, an app may choose to override the current animation state, for example
     * when an activity is paused.
     * </p>
     */
    public void enableOrbColorAnimation(boolean enable) {
        mColorAnimationEnabled = enable;
        updateColorAnimator();
    }

    private void updateColorAnimator() {
        if (mColorAnimator != null) {
            mColorAnimator.end();
            mColorAnimator = null;
        }
        if (mColorAnimationEnabled && mAttachedToWindow) {
            // TODO: set interpolator (material if available)
            mColorAnimator = android.animation.ValueAnimator.ofObject(mColorEvaluator, mColors.color, mColors.brightColor, mColors.color);
            mColorAnimator.setRepeatCount(android.animation.ValueAnimator.INFINITE);
            mColorAnimator.setDuration(mPulseDurationMs * 2);
            mColorAnimator.addUpdateListener(mUpdateListener);
            mColorAnimator.start();
        }
    }

    void setOrbViewColor(int color) {
        if (mSearchOrbView.getBackground() instanceof android.graphics.drawable.GradientDrawable) {
            ((android.graphics.drawable.GradientDrawable) (mSearchOrbView.getBackground())).setColor(color);
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        updateColorAnimator();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        mAttachedToWindow = false;
        // Must stop infinite animation to prevent activity leak
        updateColorAnimator();
        super.onDetachedFromWindow();
    }
}

