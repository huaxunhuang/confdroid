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
package android.support.design.widget;


/**
 * Floating action buttons are used for a special type of promoted action. They are distinguished
 * by a circled icon floating above the UI and have special motion behaviors related to morphing,
 * launching, and the transferring anchor point.
 *
 * <p>Floating action buttons come in two sizes: the default and the mini. The size can be
 * controlled with the {@code fabSize} attribute.</p>
 *
 * <p>As this class descends from {@link ImageView}, you can control the icon which is displayed
 * via {@link #setImageDrawable(Drawable)}.</p>
 *
 * <p>The background color of this view defaults to the your theme's {@code colorAccent}. If you
 * wish to change this at runtime then you can do so via
 * {@link #setBackgroundTintList(ColorStateList)}.</p>
 */
@android.support.design.widget.CoordinatorLayout.DefaultBehavior(android.support.design.widget.FloatingActionButton.Behavior.class)
public class FloatingActionButton extends android.support.design.widget.VisibilityAwareImageButton {
    private static final java.lang.String LOG_TAG = "FloatingActionButton";

    /**
     * Callback to be invoked when the visibility of a FloatingActionButton changes.
     */
    public static abstract class OnVisibilityChangedListener {
        /**
         * Called when a FloatingActionButton has been
         * {@link #show(OnVisibilityChangedListener) shown}.
         *
         * @param fab
         * 		the FloatingActionButton that was shown.
         */
        public void onShown(android.support.design.widget.FloatingActionButton fab) {
        }

        /**
         * Called when a FloatingActionButton has been
         * {@link #hide(OnVisibilityChangedListener) hidden}.
         *
         * @param fab
         * 		the FloatingActionButton that was hidden.
         */
        public void onHidden(android.support.design.widget.FloatingActionButton fab) {
        }
    }

    // These values must match those in the attrs declaration
    /**
     * The mini sized button. Will always been smaller than {@link #SIZE_NORMAL}.
     *
     * @see #setSize(int)
     */
    public static final int SIZE_MINI = 1;

    /**
     * The normal sized button. Will always been larger than {@link #SIZE_MINI}.
     *
     * @see #setSize(int)
     */
    public static final int SIZE_NORMAL = 0;

    /**
     * Size which will change based on the window size. For small sized windows
     * (largest screen dimension < 470dp) this will select a small sized button, and for
     * larger sized windows it will select a larger size.
     *
     * @see #setSize(int)
     */
    public static final int SIZE_AUTO = -1;

    /**
     * The switch point for the largest screen edge where SIZE_AUTO switches from mini to normal.
     */
    private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.support.annotation.IntDef({ android.support.design.widget.FloatingActionButton.SIZE_MINI, android.support.design.widget.FloatingActionButton.SIZE_NORMAL, android.support.design.widget.FloatingActionButton.SIZE_AUTO })
    public @interface Size {}

    private android.content.res.ColorStateList mBackgroundTint;

    private android.graphics.PorterDuff.Mode mBackgroundTintMode;

    private int mBorderWidth;

    private int mRippleColor;

    private int mSize;

    int mImagePadding;

    private int mMaxImageSize;

    boolean mCompatPadding;

    final android.graphics.Rect mShadowPadding = new android.graphics.Rect();

    private final android.graphics.Rect mTouchArea = new android.graphics.Rect();

    private android.support.v7.widget.AppCompatImageHelper mImageHelper;

    private android.support.design.widget.FloatingActionButtonImpl mImpl;

    public FloatingActionButton(android.content.Context context) {
        this(context, null);
    }

    public FloatingActionButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(context);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, R.style.Widget_Design_FloatingActionButton);
        mBackgroundTint = a.getColorStateList(R.styleable.FloatingActionButton_backgroundTint);
        mBackgroundTintMode = android.support.design.widget.ViewUtils.parseTintMode(a.getInt(R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        mRippleColor = a.getColor(R.styleable.FloatingActionButton_rippleColor, 0);
        mSize = a.getInt(R.styleable.FloatingActionButton_fabSize, android.support.design.widget.FloatingActionButton.SIZE_AUTO);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.FloatingActionButton_borderWidth, 0);
        final float elevation = a.getDimension(R.styleable.FloatingActionButton_elevation, 0.0F);
        final float pressedTranslationZ = a.getDimension(R.styleable.FloatingActionButton_pressedTranslationZ, 0.0F);
        mCompatPadding = a.getBoolean(R.styleable.FloatingActionButton_useCompatPadding, false);
        a.recycle();
        mImageHelper = new android.support.v7.widget.AppCompatImageHelper(this);
        mImageHelper.loadFromAttributes(attrs, defStyleAttr);
        mMaxImageSize = ((int) (getResources().getDimension(R.dimen.design_fab_image_size)));
        getImpl().setBackgroundDrawable(mBackgroundTint, mBackgroundTintMode, mRippleColor, mBorderWidth);
        getImpl().setElevation(elevation);
        getImpl().setPressedTranslationZ(pressedTranslationZ);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int preferredSize = getSizeDimension();
        mImagePadding = (preferredSize - mMaxImageSize) / 2;
        getImpl().updatePadding();
        final int w = android.support.design.widget.FloatingActionButton.resolveAdjustedSize(preferredSize, widthMeasureSpec);
        final int h = android.support.design.widget.FloatingActionButton.resolveAdjustedSize(preferredSize, heightMeasureSpec);
        // As we want to stay circular, we set both dimensions to be the
        // smallest resolved dimension
        final int d = java.lang.Math.min(w, h);
        // We add the shadow's padding to the measured dimension
        setMeasuredDimension((d + mShadowPadding.left) + mShadowPadding.right, (d + mShadowPadding.top) + mShadowPadding.bottom);
    }

    /**
     * Returns the ripple color for this button.
     *
     * @return the ARGB color used for the ripple
     * @see #setRippleColor(int)
     */
    @android.support.annotation.ColorInt
    public int getRippleColor() {
        return mRippleColor;
    }

    /**
     * Sets the ripple color for this button.
     *
     * <p>When running on devices with KitKat or below, we draw this color as a filled circle
     * rather than a ripple.</p>
     *
     * @param color
     * 		ARGB color to use for the ripple
     * @unknown ref android.support.design.R.styleable#FloatingActionButton_rippleColor
     * @see #getRippleColor()
     */
    public void setRippleColor(@android.support.annotation.ColorInt
    int color) {
        if (mRippleColor != color) {
            mRippleColor = color;
            getImpl().setRippleColor(color);
        }
    }

    /**
     * Returns the tint applied to the background drawable, if specified.
     *
     * @return the tint applied to the background drawable
     * @see #setBackgroundTintList(ColorStateList)
     */
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.content.res.ColorStateList getBackgroundTintList() {
        return mBackgroundTint;
    }

    /**
     * Applies a tint to the background drawable. Does not modify the current tint
     * mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     */
    public void setBackgroundTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mBackgroundTint != tint) {
            mBackgroundTint = tint;
            getImpl().setBackgroundTintList(tint);
        }
    }

    /**
     * Returns the blending mode used to apply the tint to the background
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the background
    drawable
     * @see #setBackgroundTintMode(PorterDuff.Mode)
     */
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.graphics.PorterDuff.Mode getBackgroundTintMode() {
        return mBackgroundTintMode;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setBackgroundTintList(ColorStateList)}} to the background
     * drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     */
    public void setBackgroundTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        if (mBackgroundTintMode != tintMode) {
            mBackgroundTintMode = tintMode;
            getImpl().setBackgroundTintMode(tintMode);
        }
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
        android.util.Log.i(android.support.design.widget.FloatingActionButton.LOG_TAG, "Setting a custom background is not supported.");
    }

    @java.lang.Override
    public void setBackgroundResource(int resid) {
        android.util.Log.i(android.support.design.widget.FloatingActionButton.LOG_TAG, "Setting a custom background is not supported.");
    }

    @java.lang.Override
    public void setBackgroundColor(int color) {
        android.util.Log.i(android.support.design.widget.FloatingActionButton.LOG_TAG, "Setting a custom background is not supported.");
    }

    @java.lang.Override
    public void setImageResource(@android.support.annotation.DrawableRes
    int resId) {
        // Intercept this call and instead retrieve the Drawable via the image helper
        mImageHelper.setImageResource(resId);
    }

    /**
     * Shows the button.
     * <p>This method will animate the button show if the view has already been laid out.</p>
     */
    public void show() {
        show(null);
    }

    /**
     * Shows the button.
     * <p>This method will animate the button show if the view has already been laid out.</p>
     *
     * @param listener
     * 		the listener to notify when this view is shown
     */
    public void show(@android.support.annotation.Nullable
    final android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener listener) {
        show(listener, true);
    }

    void show(android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener listener, boolean fromUser) {
        getImpl().show(wrapOnVisibilityChangedListener(listener), fromUser);
    }

    /**
     * Hides the button.
     * <p>This method will animate the button hide if the view has already been laid out.</p>
     */
    public void hide() {
        hide(null);
    }

    /**
     * Hides the button.
     * <p>This method will animate the button hide if the view has already been laid out.</p>
     *
     * @param listener
     * 		the listener to notify when this view is hidden
     */
    public void hide(@android.support.annotation.Nullable
    android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener listener) {
        hide(listener, true);
    }

    void hide(@android.support.annotation.Nullable
    android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener listener, boolean fromUser) {
        getImpl().hide(wrapOnVisibilityChangedListener(listener), fromUser);
    }

    /**
     * Set whether FloatingActionButton should add inner padding on platforms Lollipop and after,
     * to ensure consistent dimensions on all platforms.
     *
     * @param useCompatPadding
     * 		true if FloatingActionButton is adding inner padding on platforms
     * 		Lollipop and after, to ensure consistent dimensions on all platforms.
     * @unknown ref android.support.design.R.styleable#FloatingActionButton_useCompatPadding
     * @see #getUseCompatPadding()
     */
    public void setUseCompatPadding(boolean useCompatPadding) {
        if (mCompatPadding != useCompatPadding) {
            mCompatPadding = useCompatPadding;
            getImpl().onCompatShadowChanged();
        }
    }

    /**
     * Returns whether FloatingActionButton will add inner padding on platforms Lollipop and after.
     *
     * @return true if FloatingActionButton is adding inner padding on platforms Lollipop and after,
    to ensure consistent dimensions on all platforms.
     * @unknown ref android.support.design.R.styleable#FloatingActionButton_useCompatPadding
     * @see #setUseCompatPadding(boolean)
     */
    public boolean getUseCompatPadding() {
        return mCompatPadding;
    }

    /**
     * Sets the size of the button.
     *
     * <p>The options relate to the options available on the material design specification.
     * {@link #SIZE_NORMAL} is larger than {@link #SIZE_MINI}. {@link #SIZE_AUTO} will choose
     * an appropriate size based on the screen size.</p>
     *
     * @param size
     * 		one of {@link #SIZE_NORMAL}, {@link #SIZE_MINI} or {@link #SIZE_AUTO}
     * @unknown ref android.support.design.R.styleable#FloatingActionButton_fabSize
     */
    public void setSize(@android.support.design.widget.FloatingActionButton.Size
    int size) {
        if (size != mSize) {
            mSize = size;
            requestLayout();
        }
    }

    /**
     * Returns the chosen size for this button.
     *
     * @return one of {@link #SIZE_NORMAL}, {@link #SIZE_MINI} or {@link #SIZE_AUTO}
     * @see #setSize(int)
     */
    @android.support.design.widget.FloatingActionButton.Size
    public int getSize() {
        return mSize;
    }

    @android.support.annotation.Nullable
    private android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(@android.support.annotation.Nullable
    final android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener listener) {
        if (listener == null) {
            return null;
        }
        return new android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener() {
            @java.lang.Override
            public void onShown() {
                listener.onShown(android.support.design.widget.FloatingActionButton.this);
            }

            @java.lang.Override
            public void onHidden() {
                listener.onHidden(android.support.design.widget.FloatingActionButton.this);
            }
        };
    }

    int getSizeDimension() {
        return getSizeDimension(mSize);
    }

    private int getSizeDimension(@android.support.design.widget.FloatingActionButton.Size
    final int size) {
        final android.content.res.Resources res = getResources();
        switch (size) {
            case android.support.design.widget.FloatingActionButton.SIZE_AUTO :
                // If we're set to auto, grab the size from resources and refresh
                final int width = android.support.v4.content.res.ConfigurationHelper.getScreenWidthDp(res);
                final int height = android.support.v4.content.res.ConfigurationHelper.getScreenHeightDp(res);
                return java.lang.Math.max(width, height) < android.support.design.widget.FloatingActionButton.AUTO_MINI_LARGEST_SCREEN_WIDTH ? getSizeDimension(android.support.design.widget.FloatingActionButton.SIZE_MINI) : getSizeDimension(android.support.design.widget.FloatingActionButton.SIZE_NORMAL);
            case android.support.design.widget.FloatingActionButton.SIZE_MINI :
                return res.getDimensionPixelSize(R.dimen.design_fab_size_mini);
            case android.support.design.widget.FloatingActionButton.SIZE_NORMAL :
            default :
                return res.getDimensionPixelSize(R.dimen.design_fab_size_normal);
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getImpl().onAttachedToWindow();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getImpl().onDetachedFromWindow();
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().onDrawableStateChanged(getDrawableState());
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        getImpl().jumpDrawableToCurrentState();
    }

    /**
     * Return in {@code rect} the bounds of the actual floating action button content in view-local
     * coordinates. This is defined as anything within any visible shadow.
     *
     * @return true if this view actually has been laid out and has a content rect, else false.
     */
    public boolean getContentRect(@android.support.annotation.NonNull
    android.graphics.Rect rect) {
        if (android.support.v4.view.ViewCompat.isLaidOut(this)) {
            rect.set(0, 0, getWidth(), getHeight());
            rect.left += mShadowPadding.left;
            rect.top += mShadowPadding.top;
            rect.right -= mShadowPadding.right;
            rect.bottom -= mShadowPadding.bottom;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the FloatingActionButton's background, minus any compatible shadow implementation.
     */
    @android.support.annotation.NonNull
    public android.graphics.drawable.Drawable getContentBackground() {
        return getImpl().getContentBackground();
    }

    private static int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int result = desiredSize;
        int specMode = android.view.View.MeasureSpec.getMode(measureSpec);
        int specSize = android.view.View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case android.view.View.MeasureSpec.UNSPECIFIED :
                // Parent says we can be as big as we want. Just don't be larger
                // than max size imposed on ourselves.
                result = desiredSize;
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = java.lang.Math.min(desiredSize, specSize);
                break;
            case android.view.View.MeasureSpec.EXACTLY :
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (getContentRect(mTouchArea) && (!mTouchArea.contains(((int) (ev.getX())), ((int) (ev.getY()))))) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * Behavior designed for use with {@link FloatingActionButton} instances. Its main function
     * is to move {@link FloatingActionButton} views so that any displayed {@link Snackbar}s do
     * not cover them.
     */
    public static class Behavior extends android.support.design.widget.CoordinatorLayout.Behavior<android.support.design.widget.FloatingActionButton> {
        private static final boolean AUTO_HIDE_DEFAULT = true;

        private android.graphics.Rect mTmpRect;

        private android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener mInternalAutoHideListener;

        private boolean mAutoHideEnabled;

        public Behavior() {
            super();
            mAutoHideEnabled = android.support.design.widget.FloatingActionButton.Behavior.AUTO_HIDE_DEFAULT;
        }

        public Behavior(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton_Behavior_Layout);
            mAutoHideEnabled = a.getBoolean(R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, android.support.design.widget.FloatingActionButton.Behavior.AUTO_HIDE_DEFAULT);
            a.recycle();
        }

        /**
         * Sets whether the associated FloatingActionButton automatically hides when there is
         * not enough space to be displayed. This works with {@link AppBarLayout}
         * and {@link BottomSheetBehavior}.
         *
         * @unknown ref android.support.design.R.styleable#FloatingActionButton_Behavior_Layout_behavior_autoHide
         * @param autoHide
         * 		true to enable automatic hiding
         */
        public void setAutoHideEnabled(boolean autoHide) {
            mAutoHideEnabled = autoHide;
        }

        /**
         * Returns whether the associated FloatingActionButton automatically hides when there is
         * not enough space to be displayed.
         *
         * @unknown ref android.support.design.R.styleable#FloatingActionButton_Behavior_Layout_behavior_autoHide
         * @return true if enabled
         */
        public boolean isAutoHideEnabled() {
            return mAutoHideEnabled;
        }

        @java.lang.Override
        public void onAttachedToLayoutParams(@android.support.annotation.NonNull
        android.support.design.widget.CoordinatorLayout.LayoutParams lp) {
            if (lp.dodgeInsetEdges == android.view.Gravity.NO_GRAVITY) {
                // If the developer hasn't set dodgeInsetEdges, lets set it to BOTTOM so that
                // we dodge any Snackbars
                lp.dodgeInsetEdges = android.view.Gravity.BOTTOM;
            }
        }

        @java.lang.Override
        public boolean onDependentViewChanged(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.FloatingActionButton child, android.view.View dependency) {
            if (dependency instanceof android.support.design.widget.AppBarLayout) {
                // If we're depending on an AppBarLayout we will show/hide it automatically
                // if the FAB is anchored to the AppBarLayout
                updateFabVisibilityForAppBarLayout(parent, ((android.support.design.widget.AppBarLayout) (dependency)), child);
            } else
                if (android.support.design.widget.FloatingActionButton.Behavior.isBottomSheet(dependency)) {
                    updateFabVisibilityForBottomSheet(dependency, child);
                }

            return false;
        }

        private static boolean isBottomSheet(@android.support.annotation.NonNull
        android.view.View view) {
            final android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof android.support.design.widget.CoordinatorLayout.LayoutParams) {
                return ((android.support.design.widget.CoordinatorLayout.LayoutParams) (lp)).getBehavior() instanceof android.support.design.widget.BottomSheetBehavior;
            }
            return false;
        }

        @android.support.annotation.VisibleForTesting
        void setInternalAutoHideListener(android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener listener) {
            mInternalAutoHideListener = listener;
        }

        private boolean shouldUpdateVisibility(android.view.View dependency, android.support.design.widget.FloatingActionButton child) {
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            if (!mAutoHideEnabled) {
                return false;
            }
            if (lp.getAnchorId() != dependency.getId()) {
                // The anchor ID doesn't match the dependency, so we won't automatically
                // show/hide the FAB
                return false;
            }
            // noinspection RedundantIfStatement
            if (child.getUserSetVisibility() != android.view.View.VISIBLE) {
                // The view isn't set to be visible so skip changing its visibility
                return false;
            }
            return true;
        }

        private boolean updateFabVisibilityForAppBarLayout(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout appBarLayout, android.support.design.widget.FloatingActionButton child) {
            if (!shouldUpdateVisibility(appBarLayout, child)) {
                return false;
            }
            if (mTmpRect == null) {
                mTmpRect = new android.graphics.Rect();
            }
            // First, let's get the visible rect of the dependency
            final android.graphics.Rect rect = mTmpRect;
            android.support.design.widget.ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                // If the anchor's bottom is below the seam, we'll animate our FAB out
                child.hide(mInternalAutoHideListener, false);
            } else {
                // Else, we'll animate our FAB back in
                child.show(mInternalAutoHideListener, false);
            }
            return true;
        }

        private boolean updateFabVisibilityForBottomSheet(android.view.View bottomSheet, android.support.design.widget.FloatingActionButton child) {
            if (!shouldUpdateVisibility(bottomSheet, child)) {
                return false;
            }
            android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            if (bottomSheet.getTop() < ((child.getHeight() / 2) + lp.topMargin)) {
                child.hide(mInternalAutoHideListener, false);
            } else {
                child.show(mInternalAutoHideListener, false);
            }
            return true;
        }

        @java.lang.Override
        public boolean onLayoutChild(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.FloatingActionButton child, int layoutDirection) {
            // First, let's make sure that the visibility of the FAB is consistent
            final java.util.List<android.view.View> dependencies = parent.getDependencies(child);
            for (int i = 0, count = dependencies.size(); i < count; i++) {
                final android.view.View dependency = dependencies.get(i);
                if (dependency instanceof android.support.design.widget.AppBarLayout) {
                    if (updateFabVisibilityForAppBarLayout(parent, ((android.support.design.widget.AppBarLayout) (dependency)), child)) {
                        break;
                    }
                } else
                    if (android.support.design.widget.FloatingActionButton.Behavior.isBottomSheet(dependency)) {
                        if (updateFabVisibilityForBottomSheet(dependency, child)) {
                            break;
                        }
                    }

            }
            // Now let the CoordinatorLayout lay out the FAB
            parent.onLayoutChild(child, layoutDirection);
            // Now offset it if needed
            offsetIfNeeded(parent, child);
            return true;
        }

        @java.lang.Override
        public boolean getInsetDodgeRect(@android.support.annotation.NonNull
        android.support.design.widget.CoordinatorLayout parent, @android.support.annotation.NonNull
        android.support.design.widget.FloatingActionButton child, @android.support.annotation.NonNull
        android.graphics.Rect rect) {
            // Since we offset so that any internal shadow padding isn't shown, we need to make
            // sure that the shadow isn't used for any dodge inset calculations
            final android.graphics.Rect shadowPadding = child.mShadowPadding;
            rect.set(child.getLeft() + shadowPadding.left, child.getTop() + shadowPadding.top, child.getRight() - shadowPadding.right, child.getBottom() - shadowPadding.bottom);
            return true;
        }

        /**
         * Pre-Lollipop we use padding so that the shadow has enough space to be drawn. This method
         * offsets our layout position so that we're positioned correctly if we're on one of
         * our parent's edges.
         */
        private void offsetIfNeeded(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.FloatingActionButton fab) {
            final android.graphics.Rect padding = fab.mShadowPadding;
            if (((padding != null) && (padding.centerX() > 0)) && (padding.centerY() > 0)) {
                final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (fab.getLayoutParams()));
                int offsetTB = 0;
                int offsetLR = 0;
                if (fab.getRight() >= (parent.getWidth() - lp.rightMargin)) {
                    // If we're on the right edge, shift it the right
                    offsetLR = padding.right;
                } else
                    if (fab.getLeft() <= lp.leftMargin) {
                        // If we're on the left edge, shift it the left
                        offsetLR = -padding.left;
                    }

                if (fab.getBottom() >= (parent.getHeight() - lp.bottomMargin)) {
                    // If we're on the bottom edge, shift it down
                    offsetTB = padding.bottom;
                } else
                    if (fab.getTop() <= lp.topMargin) {
                        // If we're on the top edge, shift it up
                        offsetTB = -padding.top;
                    }

                if (offsetTB != 0) {
                    android.support.v4.view.ViewCompat.offsetTopAndBottom(fab, offsetTB);
                }
                if (offsetLR != 0) {
                    android.support.v4.view.ViewCompat.offsetLeftAndRight(fab, offsetLR);
                }
            }
        }
    }

    /**
     * Returns the backward compatible elevation of the FloatingActionButton.
     *
     * @return the backward compatible elevation in pixels.
     * @unknown ref android.support.design.R.styleable#FloatingActionButton_elevation
     * @see #setCompatElevation(float)
     */
    public float getCompatElevation() {
        return getImpl().getElevation();
    }

    /**
     * Updates the backward compatible elevation of the FloatingActionButton.
     *
     * @param elevation
     * 		The backward compatible elevation in pixels.
     * @unknown ref android.support.design.R.styleable#FloatingActionButton_elevation
     * @see #getCompatElevation()
     * @see #setUseCompatPadding(boolean)
     */
    public void setCompatElevation(float elevation) {
        getImpl().setElevation(elevation);
    }

    private android.support.design.widget.FloatingActionButtonImpl getImpl() {
        if (mImpl == null) {
            mImpl = createImpl();
        }
        return mImpl;
    }

    private android.support.design.widget.FloatingActionButtonImpl createImpl() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= 21) {
            return new android.support.design.widget.FloatingActionButtonLollipop(this, new android.support.design.widget.FloatingActionButton.ShadowDelegateImpl(), android.support.design.widget.ViewUtils.DEFAULT_ANIMATOR_CREATOR);
        } else
            if (sdk >= 14) {
                return new android.support.design.widget.FloatingActionButtonIcs(this, new android.support.design.widget.FloatingActionButton.ShadowDelegateImpl(), android.support.design.widget.ViewUtils.DEFAULT_ANIMATOR_CREATOR);
            } else {
                return new android.support.design.widget.FloatingActionButtonGingerbread(this, new android.support.design.widget.FloatingActionButton.ShadowDelegateImpl(), android.support.design.widget.ViewUtils.DEFAULT_ANIMATOR_CREATOR);
            }

    }

    private class ShadowDelegateImpl implements android.support.design.widget.ShadowViewDelegate {
        ShadowDelegateImpl() {
        }

        @java.lang.Override
        public float getRadius() {
            return getSizeDimension() / 2.0F;
        }

        @java.lang.Override
        public void setShadowPadding(int left, int top, int right, int bottom) {
            mShadowPadding.set(left, top, right, bottom);
            setPadding(left + mImagePadding, top + mImagePadding, right + mImagePadding, bottom + mImagePadding);
        }

        @java.lang.Override
        public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
            android.support.design.widget.FloatingActionButton.super.setBackgroundDrawable(background);
        }

        @java.lang.Override
        public boolean isCompatPaddingEnabled() {
            return mCompatPadding;
        }
    }
}

