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
package android.support.v17.leanback.app;


/**
 * Supports background image continuity between multiple Activities.
 *
 * <p>An Activity should instantiate a BackgroundManager and {@link #attach}
 * to the Activity's window.  When the Activity is started, the background is
 * initialized to the current background values stored in a continuity service.
 * The background continuity service is updated as the background is updated.
 *
 * <p>At some point, for example when it is stopped, the Activity may release
 * its background state.
 *
 * <p>When an Activity is resumed, if the BackgroundManager has not been
 * released, the continuity service is updated from the BackgroundManager state.
 * If the BackgroundManager was released, the BackgroundManager inherits the
 * current state from the continuity service.
 *
 * <p>When the last Activity is destroyed, the background state is reset.
 *
 * <p>Backgrounds consist of several layers, from back to front:
 * <ul>
 *   <li>the background Drawable of the theme</li>
 *   <li>a solid color (set via {@link #setColor})</li>
 *   <li>two Drawables, previous and current (set via {@link #setBitmap} or
 *   {@link #setDrawable}), which may be in transition</li>
 * </ul>
 *
 * <p>BackgroundManager holds references to potentially large bitmap Drawables.
 * Call {@link #release} to release these references when the Activity is not
 * visible.
 */
// TODO: support for multiple app processes requires a proper android service
// instead of the shared memory "service" implemented here. Such a service could
// support continuity between fragments of different applications if desired.
public final class BackgroundManager {
    interface FragmentStateQueriable {
        public boolean isResumed();
    }

    static final java.lang.String TAG = "BackgroundManager";

    static final boolean DEBUG = false;

    static final int FULL_ALPHA = 255;

    private static final int DIM_ALPHA_ON_SOLID = ((int) (0.8F * android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA));

    private static final int CHANGE_BG_DELAY_MS = 500;

    private static final int FADE_DURATION = 500;

    /**
     * Using a separate window for backgrounds can improve graphics performance by
     * leveraging hardware display layers.
     * TODO: support a leanback configuration option.
     */
    private static final boolean USE_SEPARATE_WINDOW = false;

    private static final java.lang.String WINDOW_NAME = "BackgroundManager";

    private static final java.lang.String FRAGMENT_TAG = android.support.v17.leanback.app.BackgroundManager.class.getCanonicalName();

    android.content.Context mContext;

    android.os.Handler mHandler;

    private android.view.Window mWindow;

    private android.view.WindowManager mWindowManager;

    private android.view.View mBgView;

    private android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService mService;

    private int mThemeDrawableResourceId;

    private android.support.v17.leanback.app.BackgroundManager.FragmentStateQueriable mFragmentState;

    private int mHeightPx;

    private int mWidthPx;

    android.graphics.drawable.Drawable mBackgroundDrawable;

    private int mBackgroundColor;

    private boolean mAttached;

    private long mLastSetTime;

    private final android.view.animation.Interpolator mAccelerateInterpolator;

    private final android.view.animation.Interpolator mDecelerateInterpolator;

    private final android.animation.ValueAnimator mAnimator;

    private final android.animation.ValueAnimator mDimAnimator;

    private static class BitmapDrawable extends android.graphics.drawable.Drawable {
        static class ConstantState extends android.graphics.drawable.Drawable.ConstantState {
            android.graphics.Bitmap mBitmap;

            android.graphics.Matrix mMatrix;

            android.graphics.Paint mPaint;

            @java.lang.Override
            public android.graphics.drawable.Drawable newDrawable() {
                return new android.support.v17.leanback.app.BackgroundManager.BitmapDrawable(null, mBitmap, mMatrix);
            }

            @java.lang.Override
            public int getChangingConfigurations() {
                return 0;
            }
        }

        private android.support.v17.leanback.app.BackgroundManager.BitmapDrawable.ConstantState mState = new android.support.v17.leanback.app.BackgroundManager.BitmapDrawable.ConstantState();

        BitmapDrawable(android.content.res.Resources resources, android.graphics.Bitmap bitmap) {
            this(resources, bitmap, null);
        }

        BitmapDrawable(android.content.res.Resources resources, android.graphics.Bitmap bitmap, android.graphics.Matrix matrix) {
            mState.mBitmap = bitmap;
            mState.mMatrix = (matrix != null) ? matrix : new android.graphics.Matrix();
            mState.mPaint = new android.graphics.Paint();
            mState.mPaint.setFilterBitmap(true);
        }

        android.graphics.Bitmap getBitmap() {
            return mState.mBitmap;
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas) {
            if (mState.mBitmap == null) {
                return;
            }
            if ((mState.mPaint.getAlpha() < android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA) && (mState.mPaint.getColorFilter() != null)) {
                throw new java.lang.IllegalStateException("Can't draw with translucent alpha and color filter");
            }
            canvas.drawBitmap(mState.mBitmap, mState.mMatrix, mState.mPaint);
        }

        @java.lang.Override
        public int getOpacity() {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }

        @java.lang.Override
        public void setAlpha(int alpha) {
            if (mState.mPaint.getAlpha() != alpha) {
                mState.mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        /**
         * Does not invalidateSelf to avoid recursion issues.
         * Caller must ensure appropriate invalidation.
         */
        @java.lang.Override
        public void setColorFilter(android.graphics.ColorFilter cf) {
            mState.mPaint.setColorFilter(cf);
        }

        public android.graphics.ColorFilter getColorFilter() {
            return mState.mPaint.getColorFilter();
        }

        @java.lang.Override
        public android.support.v17.leanback.app.BackgroundManager.BitmapDrawable.ConstantState getConstantState() {
            return mState;
        }
    }

    static class DrawableWrapper {
        private int mAlpha = android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA;

        private android.graphics.drawable.Drawable mDrawable;

        private android.graphics.ColorFilter mColorFilter;

        public DrawableWrapper(android.graphics.drawable.Drawable drawable) {
            mDrawable = drawable;
            updateAlpha();
            updateColorFilter();
        }

        public DrawableWrapper(android.support.v17.leanback.app.BackgroundManager.DrawableWrapper wrapper, android.graphics.drawable.Drawable drawable) {
            mDrawable = drawable;
            mAlpha = wrapper.getAlpha();
            updateAlpha();
            mColorFilter = wrapper.getColorFilter();
            updateColorFilter();
        }

        public android.graphics.drawable.Drawable getDrawable() {
            return mDrawable;
        }

        public void setAlpha(int alpha) {
            mAlpha = alpha;
            updateAlpha();
        }

        public int getAlpha() {
            return mAlpha;
        }

        private void updateAlpha() {
            mDrawable.setAlpha(mAlpha);
        }

        public android.graphics.ColorFilter getColorFilter() {
            return mColorFilter;
        }

        public void setColorFilter(android.graphics.ColorFilter colorFilter) {
            mColorFilter = colorFilter;
            updateColorFilter();
        }

        private void updateColorFilter() {
            mDrawable.setColorFilter(mColorFilter);
        }

        public void setColor(int color) {
            ((android.graphics.drawable.ColorDrawable) (mDrawable)).setColor(color);
        }
    }

    static class TranslucentLayerDrawable extends android.graphics.drawable.LayerDrawable {
        private android.support.v17.leanback.app.BackgroundManager.DrawableWrapper[] mWrapper;

        private android.graphics.Paint mPaint = new android.graphics.Paint();

        public TranslucentLayerDrawable(android.graphics.drawable.Drawable[] drawables) {
            super(drawables);
            int count = drawables.length;
            mWrapper = new android.support.v17.leanback.app.BackgroundManager.DrawableWrapper[count];
            for (int i = 0; i < count; i++) {
                mWrapper[i] = new android.support.v17.leanback.app.BackgroundManager.DrawableWrapper(drawables[i]);
            }
        }

        @java.lang.Override
        public void setAlpha(int alpha) {
            if (mPaint.getAlpha() != alpha) {
                int previousAlpha = mPaint.getAlpha();
                mPaint.setAlpha(alpha);
                invalidateSelf();
                onAlphaChanged(previousAlpha, alpha);
            }
        }

        // Queried by system transitions
        public int getAlpha() {
            return mPaint.getAlpha();
        }

        protected void onAlphaChanged(int oldAlpha, int newAlpha) {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable mutate() {
            android.graphics.drawable.Drawable drawable = super.mutate();
            int count = getNumberOfLayers();
            for (int i = 0; i < count; i++) {
                if (mWrapper[i] != null) {
                    mWrapper[i] = new android.support.v17.leanback.app.BackgroundManager.DrawableWrapper(mWrapper[i], getDrawable(i));
                }
            }
            invalidateSelf();
            return drawable;
        }

        @java.lang.Override
        public int getOpacity() {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }

        @java.lang.Override
        public boolean setDrawableByLayerId(int id, android.graphics.drawable.Drawable drawable) {
            return updateDrawable(id, drawable) != null;
        }

        public android.support.v17.leanback.app.BackgroundManager.DrawableWrapper updateDrawable(int id, android.graphics.drawable.Drawable drawable) {
            super.setDrawableByLayerId(id, drawable);
            for (int i = 0; i < getNumberOfLayers(); i++) {
                if (getId(i) == id) {
                    mWrapper[i] = new android.support.v17.leanback.app.BackgroundManager.DrawableWrapper(drawable);
                    // Must come after mWrapper was updated so it can be seen by updateColorFilter
                    invalidateSelf();
                    return mWrapper[i];
                }
            }
            return null;
        }

        public void clearDrawable(int id, android.content.Context context) {
            for (int i = 0; i < getNumberOfLayers(); i++) {
                if (getId(i) == id) {
                    mWrapper[i] = null;
                    super.setDrawableByLayerId(id, android.support.v17.leanback.app.BackgroundManager.createEmptyDrawable(context));
                    break;
                }
            }
        }

        public android.support.v17.leanback.app.BackgroundManager.DrawableWrapper findWrapperById(int id) {
            for (int i = 0; i < getNumberOfLayers(); i++) {
                if (getId(i) == id) {
                    return mWrapper[i];
                }
            }
            return null;
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas) {
            if (mPaint.getAlpha() < android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA) {
                canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint, android.graphics.Canvas.ALL_SAVE_FLAG);
            }
            super.draw(canvas);
            if (mPaint.getAlpha() < android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA) {
                canvas.restore();
            }
        }
    }

    /**
     * Optimizes drawing when the dim drawable is an alpha-only color and imagein is opaque.
     * When the layer drawable is translucent (activity transition) then we can avoid the slow
     * saveLayer/restore draw path.
     */
    private class OptimizedTranslucentLayerDrawable extends android.support.v17.leanback.app.BackgroundManager.TranslucentLayerDrawable {
        private android.graphics.PorterDuffColorFilter mColorFilter;

        private boolean mUpdatingColorFilter;

        public OptimizedTranslucentLayerDrawable(android.graphics.drawable.Drawable[] drawables) {
            super(drawables);
        }

        @java.lang.Override
        protected void onAlphaChanged(int oldAlpha, int newAlpha) {
            if ((newAlpha == android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA) && (oldAlpha < android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA)) {
                if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "transition complete");

                postChangeRunnable();
            }
        }

        @java.lang.Override
        public void invalidateSelf() {
            super.invalidateSelf();
            updateColorFilter();
        }

        @java.lang.Override
        public void invalidateDrawable(android.graphics.drawable.Drawable who) {
            if (!mUpdatingColorFilter) {
                invalidateSelf();
            }
        }

        private void updateColorFilter() {
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper dimWrapper = findWrapperById(R.id.background_dim);
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageInWrapper = findWrapperById(R.id.background_imagein);
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageOutWrapper = findWrapperById(R.id.background_imageout);
            mColorFilter = null;
            if (((imageInWrapper != null) && (imageInWrapper.getAlpha() == android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA)) && (dimWrapper.getDrawable() instanceof android.graphics.drawable.ColorDrawable)) {
                int dimColor = ((android.graphics.drawable.ColorDrawable) (dimWrapper.getDrawable())).getColor();
                if (((android.graphics.Color.red(dimColor) == 0) && (android.graphics.Color.green(dimColor) == 0)) && (android.graphics.Color.blue(dimColor) == 0)) {
                    int dimAlpha = 255 - android.graphics.Color.alpha(dimColor);
                    int color = android.graphics.Color.argb(getAlpha(), dimAlpha, dimAlpha, dimAlpha);
                    mColorFilter = new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            }
            mUpdatingColorFilter = true;
            if (imageInWrapper != null) {
                imageInWrapper.setColorFilter(mColorFilter);
            }
            if (imageOutWrapper != null) {
                imageOutWrapper.setColorFilter(null);
            }
            mUpdatingColorFilter = false;
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas) {
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageInWrapper = findWrapperById(R.id.background_imagein);
            if (((imageInWrapper != null) && (imageInWrapper.getDrawable() != null)) && (imageInWrapper.getColorFilter() != null)) {
                imageInWrapper.getDrawable().draw(canvas);
            } else {
                super.draw(canvas);
            }
        }
    }

    private android.support.v17.leanback.app.BackgroundManager.TranslucentLayerDrawable createOptimizedTranslucentLayerDrawable(android.graphics.drawable.LayerDrawable layerDrawable) {
        int numChildren = layerDrawable.getNumberOfLayers();
        android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[numChildren];
        for (int i = 0; i < numChildren; i++) {
            drawables[i] = layerDrawable.getDrawable(i);
        }
        android.support.v17.leanback.app.BackgroundManager.TranslucentLayerDrawable result = new android.support.v17.leanback.app.BackgroundManager.OptimizedTranslucentLayerDrawable(drawables);
        for (int i = 0; i < numChildren; i++) {
            result.setId(i, layerDrawable.getId(i));
        }
        return result;
    }

    android.support.v17.leanback.app.BackgroundManager.TranslucentLayerDrawable mLayerDrawable;

    private android.graphics.drawable.Drawable mDimDrawable;

    android.support.v17.leanback.app.BackgroundManager.ChangeBackgroundRunnable mChangeRunnable;

    private boolean mChangeRunnablePending;

    private final android.animation.Animator.AnimatorListener mAnimationListener = new android.animation.Animator.AnimatorListener() {
        final java.lang.Runnable mRunnable = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                postChangeRunnable();
            }
        };

        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationRepeat(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            if (mLayerDrawable != null) {
                mLayerDrawable.clearDrawable(R.id.background_imageout, mContext);
            }
            mHandler.post(mRunnable);
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
        }
    };

    private final android.animation.ValueAnimator.AnimatorUpdateListener mAnimationUpdateListener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
        @java.lang.Override
        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
            int fadeInAlpha = ((java.lang.Integer) (animation.getAnimatedValue()));
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageInWrapper = getImageInWrapper();
            if (imageInWrapper != null) {
                imageInWrapper.setAlpha(fadeInAlpha);
            } else {
                android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageOutWrapper = getImageOutWrapper();
                if (imageOutWrapper != null) {
                    imageOutWrapper.setAlpha(255 - fadeInAlpha);
                }
            }
        }
    };

    private final android.animation.ValueAnimator.AnimatorUpdateListener mDimUpdateListener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
        @java.lang.Override
        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper dimWrapper = getDimWrapper();
            if (dimWrapper != null) {
                dimWrapper.setAlpha(((java.lang.Integer) (animation.getAnimatedValue())));
            }
        }
    };

    /**
     * Shared memory continuity service.
     */
    private static class BackgroundContinuityService {
        private static final java.lang.String TAG = "BackgroundContinuityService";

        private static boolean DEBUG = android.support.v17.leanback.app.BackgroundManager.DEBUG;

        private static android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService sService = new android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService();

        private int mColor;

        private android.graphics.drawable.Drawable mDrawable;

        private int mCount;

        /**
         * Single cache of theme drawable
         */
        private int mLastThemeDrawableId;

        private java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState> mLastThemeDrawableState;

        private BackgroundContinuityService() {
            reset();
        }

        private void reset() {
            mColor = android.graphics.Color.TRANSPARENT;
            mDrawable = null;
        }

        public static android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService getInstance() {
            final int count = android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.sService.mCount++;
            if (android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.TAG, "Returning instance with new count " + count);

            return android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.sService;
        }

        public void unref() {
            if (mCount <= 0)
                throw new java.lang.IllegalStateException("Can't unref, count " + mCount);

            if ((--mCount) == 0) {
                if (android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.TAG, "mCount is zero, resetting");

                reset();
            }
        }

        public int getColor() {
            return mColor;
        }

        public android.graphics.drawable.Drawable getDrawable() {
            return mDrawable;
        }

        public void setColor(int color) {
            mColor = color;
        }

        public void setDrawable(android.graphics.drawable.Drawable drawable) {
            mDrawable = drawable;
        }

        public android.graphics.drawable.Drawable getThemeDrawable(android.content.Context context, int themeDrawableId) {
            android.graphics.drawable.Drawable drawable = null;
            if ((mLastThemeDrawableState != null) && (mLastThemeDrawableId == themeDrawableId)) {
                android.graphics.drawable.Drawable.ConstantState drawableState = mLastThemeDrawableState.get();
                if (android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.TAG, "got cached theme drawable state " + drawableState);

                if (drawableState != null) {
                    drawable = drawableState.newDrawable();
                }
            }
            if (drawable == null) {
                drawable = android.support.v4.content.ContextCompat.getDrawable(context, themeDrawableId);
                if (android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.TAG, "loaded theme drawable " + drawable);

                mLastThemeDrawableState = new java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>(drawable.getConstantState());
                mLastThemeDrawableId = themeDrawableId;
            }
            // No mutate required because this drawable is never manipulated.
            return drawable;
        }
    }

    private android.graphics.drawable.Drawable getThemeDrawable() {
        android.graphics.drawable.Drawable drawable = null;
        if (mThemeDrawableResourceId != (-1)) {
            drawable = mService.getThemeDrawable(mContext, mThemeDrawableResourceId);
        }
        if (drawable == null) {
            drawable = android.support.v17.leanback.app.BackgroundManager.createEmptyDrawable(mContext);
        }
        return drawable;
    }

    /**
     * Returns the BackgroundManager associated with the given Activity.
     * <p>
     * The BackgroundManager will be created on-demand for each individual
     * Activity. Subsequent calls will return the same BackgroundManager created
     * for this Activity.
     */
    public static android.support.v17.leanback.app.BackgroundManager getInstance(android.app.Activity activity) {
        if (activity instanceof android.support.v4.app.FragmentActivity) {
            return android.support.v17.leanback.app.BackgroundManager.getSupportInstance(((android.support.v4.app.FragmentActivity) (activity)));
        }
        android.support.v17.leanback.app.BackgroundFragment fragment = ((android.support.v17.leanback.app.BackgroundFragment) (activity.getFragmentManager().findFragmentByTag(android.support.v17.leanback.app.BackgroundManager.FRAGMENT_TAG)));
        if (fragment != null) {
            android.support.v17.leanback.app.BackgroundManager manager = fragment.getBackgroundManager();
            if (manager != null) {
                return manager;
            }
            // manager is null: this is a fragment restored by FragmentManager,
            // fall through to create a BackgroundManager attach to it.
        }
        return new android.support.v17.leanback.app.BackgroundManager(activity, false);
    }

    private static android.support.v17.leanback.app.BackgroundManager getSupportInstance(android.support.v4.app.FragmentActivity activity) {
        android.support.v17.leanback.app.BackgroundSupportFragment fragment = ((android.support.v17.leanback.app.BackgroundSupportFragment) (activity.getSupportFragmentManager().findFragmentByTag(android.support.v17.leanback.app.BackgroundManager.FRAGMENT_TAG)));
        if (fragment != null) {
            android.support.v17.leanback.app.BackgroundManager manager = fragment.getBackgroundManager();
            if (manager != null) {
                return manager;
            }
            // manager is null: this is a fragment restored by FragmentManager,
            // fall through to create a BackgroundManager attach to it.
        }
        return new android.support.v17.leanback.app.BackgroundManager(activity, true);
    }

    private BackgroundManager(android.app.Activity activity, boolean isSupportFragmentActivity) {
        mContext = activity;
        mService = android.support.v17.leanback.app.BackgroundManager.BackgroundContinuityService.getInstance();
        mHeightPx = mContext.getResources().getDisplayMetrics().heightPixels;
        mWidthPx = mContext.getResources().getDisplayMetrics().widthPixels;
        mHandler = new android.os.Handler();
        android.view.animation.Interpolator defaultInterpolator = new android.support.v4.view.animation.FastOutLinearInInterpolator();
        mAccelerateInterpolator = android.view.animation.AnimationUtils.loadInterpolator(mContext, android.R.anim.accelerate_interpolator);
        mDecelerateInterpolator = android.view.animation.AnimationUtils.loadInterpolator(mContext, android.R.anim.decelerate_interpolator);
        mAnimator = android.animation.ValueAnimator.ofInt(0, android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA);
        mAnimator.addListener(mAnimationListener);
        mAnimator.addUpdateListener(mAnimationUpdateListener);
        mAnimator.setInterpolator(defaultInterpolator);
        mDimAnimator = new android.animation.ValueAnimator();
        mDimAnimator.addUpdateListener(mDimUpdateListener);
        android.content.res.TypedArray ta = activity.getTheme().obtainStyledAttributes(new int[]{ android.R.attr.windowBackground });
        mThemeDrawableResourceId = ta.getResourceId(0, -1);
        if (mThemeDrawableResourceId < 0) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "BackgroundManager no window background resource!");

        }
        ta.recycle();
        if (isSupportFragmentActivity) {
            createSupportFragment(((android.support.v4.app.FragmentActivity) (activity)));
        } else {
            createFragment(activity);
        }
    }

    private void createFragment(android.app.Activity activity) {
        // Use a fragment to ensure the background manager gets detached properly.
        android.support.v17.leanback.app.BackgroundFragment fragment = ((android.support.v17.leanback.app.BackgroundFragment) (activity.getFragmentManager().findFragmentByTag(android.support.v17.leanback.app.BackgroundManager.FRAGMENT_TAG)));
        if (fragment == null) {
            fragment = new android.support.v17.leanback.app.BackgroundFragment();
            activity.getFragmentManager().beginTransaction().add(fragment, android.support.v17.leanback.app.BackgroundManager.FRAGMENT_TAG).commit();
        } else {
            if (fragment.getBackgroundManager() != null) {
                throw new java.lang.IllegalStateException("Created duplicated BackgroundManager for same " + "activity, please use getInstance() instead");
            }
        }
        fragment.setBackgroundManager(this);
        mFragmentState = fragment;
    }

    private void createSupportFragment(android.support.v4.app.FragmentActivity activity) {
        // Use a fragment to ensure the background manager gets detached properly.
        android.support.v17.leanback.app.BackgroundSupportFragment fragment = ((android.support.v17.leanback.app.BackgroundSupportFragment) (activity.getSupportFragmentManager().findFragmentByTag(android.support.v17.leanback.app.BackgroundManager.FRAGMENT_TAG)));
        if (fragment == null) {
            fragment = new android.support.v17.leanback.app.BackgroundSupportFragment();
            activity.getSupportFragmentManager().beginTransaction().add(fragment, android.support.v17.leanback.app.BackgroundManager.FRAGMENT_TAG).commit();
        } else {
            if (fragment.getBackgroundManager() != null) {
                throw new java.lang.IllegalStateException("Created duplicated BackgroundManager for same " + "activity, please use getInstance() instead");
            }
        }
        fragment.setBackgroundManager(this);
        mFragmentState = fragment;
    }

    android.support.v17.leanback.app.BackgroundManager.DrawableWrapper getImageInWrapper() {
        return mLayerDrawable == null ? null : mLayerDrawable.findWrapperById(R.id.background_imagein);
    }

    android.support.v17.leanback.app.BackgroundManager.DrawableWrapper getImageOutWrapper() {
        return mLayerDrawable == null ? null : mLayerDrawable.findWrapperById(R.id.background_imageout);
    }

    android.support.v17.leanback.app.BackgroundManager.DrawableWrapper getDimWrapper() {
        return mLayerDrawable == null ? null : mLayerDrawable.findWrapperById(R.id.background_dim);
    }

    private android.support.v17.leanback.app.BackgroundManager.DrawableWrapper getColorWrapper() {
        return mLayerDrawable == null ? null : mLayerDrawable.findWrapperById(R.id.background_color);
    }

    /**
     * Synchronizes state when the owning Activity is started.
     * At that point the view becomes visible.
     */
    void onActivityStart() {
        if (mService == null) {
            return;
        }
        if (mLayerDrawable == null) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, ("onActivityStart " + this) + " released state, syncing with service");

            syncWithService();
        } else {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, (((("onActivityStart " + this) + " updating service color ") + mBackgroundColor) + " drawable ") + mBackgroundDrawable);

            mService.setColor(mBackgroundColor);
            mService.setDrawable(mBackgroundDrawable);
        }
    }

    void onResume() {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "onResume " + this);

        postChangeRunnable();
    }

    private void syncWithService() {
        int color = mService.getColor();
        android.graphics.drawable.Drawable drawable = mService.getDrawable();
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, (("syncWithService color " + java.lang.Integer.toHexString(color)) + " drawable ") + drawable);

        mBackgroundColor = color;
        mBackgroundDrawable = (drawable == null) ? null : drawable.getConstantState().newDrawable().mutate();
        updateImmediate();
    }

    private void lazyInit() {
        if (mLayerDrawable != null) {
            return;
        }
        android.graphics.drawable.LayerDrawable layerDrawable = ((android.graphics.drawable.LayerDrawable) (mutate()));
        mLayerDrawable = createOptimizedTranslucentLayerDrawable(layerDrawable);
        android.support.v17.leanback.widget.BackgroundHelper.setBackgroundPreservingAlpha(mBgView, mLayerDrawable);
        mLayerDrawable.clearDrawable(R.id.background_imageout, mContext);
        mLayerDrawable.updateDrawable(R.id.background_theme, getThemeDrawable());
        updateDimWrapper();
    }

    private void updateDimWrapper() {
        if (mDimDrawable == null) {
            mDimDrawable = getDefaultDimLayer();
        }
        android.graphics.drawable.Drawable dimDrawable = mDimDrawable.getConstantState().newDrawable(mContext.getResources()).mutate();
        if (mLayerDrawable != null) {
            mLayerDrawable.updateDrawable(R.id.background_dim, dimDrawable);
        }
    }

    /**
     * Makes the background visible on the given Window.  The background manager must be attached
     * when the background is set.
     */
    public void attach(android.view.Window window) {
        if (android.support.v17.leanback.app.BackgroundManager.USE_SEPARATE_WINDOW) {
            attachBehindWindow(window);
        } else {
            attachToView(window.getDecorView());
        }
    }

    /**
     * Sets the resource id for the drawable to be shown when there is no background set.
     * Overrides the window background drawable from the theme. This should
     * be called before attaching.
     */
    public void setThemeDrawableResourceId(int resourceId) {
        mThemeDrawableResourceId = resourceId;
    }

    private void attachBehindWindow(android.view.Window window) {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "attachBehindWindow " + window);

        mWindow = window;
        mWindowManager = window.getWindowManager();
        android.view.WindowManager.LayoutParams params = // Media window sits behind the main application window
        // Avoid default to software format RGBA
        new android.view.WindowManager.LayoutParams(android.view.WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA, android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, android.graphics.PixelFormat.TRANSLUCENT);
        params.setTitle(android.support.v17.leanback.app.BackgroundManager.WINDOW_NAME);
        params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        android.view.View backgroundView = android.view.LayoutInflater.from(mContext).inflate(R.layout.lb_background_window, null);
        mWindowManager.addView(backgroundView, params);
        attachToView(backgroundView);
    }

    private void attachToView(android.view.View sceneRoot) {
        mBgView = sceneRoot;
        mAttached = true;
        syncWithService();
    }

    /**
     * Returns true if the background manager is currently attached; false otherwise.
     */
    public boolean isAttached() {
        return mAttached;
    }

    /**
     * Release references to Drawables and put the BackgroundManager into the
     * detached state. Called when the associated Activity is destroyed.
     */
    void detach() {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "detach " + this);

        release();
        if ((mWindowManager != null) && (mBgView != null)) {
            mWindowManager.removeViewImmediate(mBgView);
        }
        mWindowManager = null;
        mWindow = null;
        mBgView = null;
        mAttached = false;
        if (mService != null) {
            mService.unref();
            mService = null;
        }
    }

    /**
     * Release references to Drawables. Typically called to reduce memory
     * overhead when not visible.
     * <p>
     * When an Activity is started, if the BackgroundManager has not been
     * released, the continuity service is updated from the BackgroundManager
     * state. If the BackgroundManager was released, the BackgroundManager
     * inherits the current state from the continuity service.
     */
    public void release() {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "release " + this);

        if (mLayerDrawable != null) {
            mLayerDrawable.clearDrawable(R.id.background_imagein, mContext);
            mLayerDrawable.clearDrawable(R.id.background_imageout, mContext);
            mLayerDrawable.clearDrawable(R.id.background_theme, mContext);
            mLayerDrawable = null;
        }
        if (mChangeRunnable != null) {
            mHandler.removeCallbacks(mChangeRunnable);
            mChangeRunnable = null;
        }
        releaseBackgroundBitmap();
    }

    void releaseBackgroundBitmap() {
        mBackgroundDrawable = null;
    }

    void setBackgroundDrawable(android.graphics.drawable.Drawable drawable) {
        mBackgroundDrawable = drawable;
        mService.setDrawable(mBackgroundDrawable);
    }

    /**
     * Sets the drawable used as a dim layer.
     */
    public void setDimLayer(android.graphics.drawable.Drawable drawable) {
        mDimDrawable = drawable;
        updateDimWrapper();
    }

    /**
     * Returns the drawable used as a dim layer.
     */
    public android.graphics.drawable.Drawable getDimLayer() {
        return mDimDrawable;
    }

    /**
     * Returns the default drawable used as a dim layer.
     */
    public android.graphics.drawable.Drawable getDefaultDimLayer() {
        return android.support.v4.content.ContextCompat.getDrawable(mContext, R.color.lb_background_protection);
    }

    void postChangeRunnable() {
        if ((mChangeRunnable == null) || (!mChangeRunnablePending)) {
            return;
        }
        // Postpone a pending change runnable until: no existing change animation in progress &&
        // activity is resumed (in the foreground) && layerdrawable fully opaque.
        // If the layerdrawable is translucent then an activity transition is in progress
        // and we want to use the optimized drawing path for performance reasons (see
        // OptimizedTranslucentLayerDrawable).
        if (mAnimator.isStarted()) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "animation in progress");

        } else
            if (!mFragmentState.isResumed()) {
                if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "not resumed");

            } else
                if (mLayerDrawable.getAlpha() < android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA) {
                    if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                        android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "in transition, alpha " + mLayerDrawable.getAlpha());

                } else {
                    long delayMs = getRunnableDelay();
                    if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                        android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "posting runnable delayMs " + delayMs);

                    mLastSetTime = java.lang.System.currentTimeMillis();
                    mHandler.postDelayed(mChangeRunnable, delayMs);
                    mChangeRunnablePending = false;
                }


    }

    private void updateImmediate() {
        lazyInit();
        android.support.v17.leanback.app.BackgroundManager.DrawableWrapper colorWrapper = getColorWrapper();
        if (colorWrapper != null) {
            colorWrapper.setColor(mBackgroundColor);
        }
        android.support.v17.leanback.app.BackgroundManager.DrawableWrapper dimWrapper = getDimWrapper();
        if (dimWrapper != null) {
            dimWrapper.setAlpha(mBackgroundColor == android.graphics.Color.TRANSPARENT ? 0 : android.support.v17.leanback.app.BackgroundManager.DIM_ALPHA_ON_SOLID);
        }
        showWallpaper(mBackgroundColor == android.graphics.Color.TRANSPARENT);
        if (mBackgroundDrawable == null) {
            mLayerDrawable.clearDrawable(R.id.background_imagein, mContext);
        } else {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "Background drawable is available");

            mLayerDrawable.updateDrawable(R.id.background_imagein, mBackgroundDrawable);
            if (dimWrapper != null) {
                dimWrapper.setAlpha(android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA);
            }
        }
    }

    /**
     * Sets the background to the given color. The timing for when this becomes
     * visible in the app is undefined and may take place after a small delay.
     */
    public void setColor(@android.support.annotation.ColorInt
    int color) {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "setColor " + java.lang.Integer.toHexString(color));

        mBackgroundColor = color;
        mService.setColor(mBackgroundColor);
        android.support.v17.leanback.app.BackgroundManager.DrawableWrapper colorWrapper = getColorWrapper();
        if (colorWrapper != null) {
            colorWrapper.setColor(mBackgroundColor);
        }
    }

    /**
     * Sets the given drawable into the background. The provided Drawable will be
     * used unmodified as the background, without any scaling or cropping
     * applied to it. The timing for when this becomes visible in the app is
     * undefined and may take place after a small delay.
     */
    public void setDrawable(android.graphics.drawable.Drawable drawable) {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "setBackgroundDrawable " + drawable);

        setDrawableInternal(drawable);
    }

    private void setDrawableInternal(android.graphics.drawable.Drawable drawable) {
        if (!mAttached) {
            throw new java.lang.IllegalStateException("Must attach before setting background drawable");
        }
        if (mChangeRunnable != null) {
            if (sameDrawable(drawable, mChangeRunnable.mDrawable)) {
                if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "new drawable same as pending");

                return;
            }
            mHandler.removeCallbacks(mChangeRunnable);
            mChangeRunnable = null;
        }
        // If layer drawable is null then the activity hasn't started yet.
        // If the layer drawable alpha is zero then the activity transition hasn't started yet.
        // In these cases we can update the background immediately and let activity transition
        // fade it in.
        if ((mLayerDrawable == null) || (mLayerDrawable.getAlpha() == 0)) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "setDrawableInternal null or alpha is zero");

            setBackgroundDrawable(drawable);
            updateImmediate();
            return;
        }
        mChangeRunnable = new android.support.v17.leanback.app.BackgroundManager.ChangeBackgroundRunnable(drawable);
        mChangeRunnablePending = true;
        postChangeRunnable();
    }

    private long getRunnableDelay() {
        return java.lang.Math.max(0, (mLastSetTime + android.support.v17.leanback.app.BackgroundManager.CHANGE_BG_DELAY_MS) - java.lang.System.currentTimeMillis());
    }

    /**
     * Sets the given bitmap into the background. When using setBitmap to set the
     * background, the provided bitmap will be scaled and cropped to correctly
     * fit within the dimensions of the view. The timing for when this becomes
     * visible in the app is undefined and may take place after a small delay.
     */
    public void setBitmap(android.graphics.Bitmap bitmap) {
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG) {
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "setBitmap " + bitmap);
        }
        if (bitmap == null) {
            setDrawableInternal(null);
            return;
        }
        if ((bitmap.getWidth() <= 0) || (bitmap.getHeight() <= 0)) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG) {
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "invalid bitmap width or height");
            }
            return;
        }
        android.graphics.Matrix matrix = null;
        if ((bitmap.getWidth() != mWidthPx) || (bitmap.getHeight() != mHeightPx)) {
            int dwidth = bitmap.getWidth();
            int dheight = bitmap.getHeight();
            float scale;
            // Scale proportionately to fit width and height.
            if ((dwidth * mHeightPx) > (mWidthPx * dheight)) {
                scale = ((float) (mHeightPx)) / ((float) (dheight));
            } else {
                scale = ((float) (mWidthPx)) / ((float) (dwidth));
            }
            int subX = java.lang.Math.min(((int) (mWidthPx / scale)), dwidth);
            int dx = java.lang.Math.max(0, (dwidth - subX) / 2);
            matrix = new android.graphics.Matrix();
            matrix.setScale(scale, scale);
            matrix.preTranslate(-dx, 0);
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, (((((("original image size " + bitmap.getWidth()) + "x") + bitmap.getHeight()) + " scale ") + scale) + " dx ") + dx);

        }
        android.support.v17.leanback.app.BackgroundManager.BitmapDrawable bitmapDrawable = new android.support.v17.leanback.app.BackgroundManager.BitmapDrawable(mContext.getResources(), bitmap, matrix);
        setDrawableInternal(bitmapDrawable);
    }

    void applyBackgroundChanges() {
        if (!mAttached) {
            return;
        }
        if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "applyBackgroundChanges drawable " + mBackgroundDrawable);

        int dimAlpha = -1;
        if (getImageOutWrapper() != null) {
            dimAlpha = (mBackgroundColor == android.graphics.Color.TRANSPARENT) ? 0 : android.support.v17.leanback.app.BackgroundManager.DIM_ALPHA_ON_SOLID;
        }
        android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageInWrapper = getImageInWrapper();
        if ((imageInWrapper == null) && (mBackgroundDrawable != null)) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "creating new imagein drawable");

            imageInWrapper = mLayerDrawable.updateDrawable(R.id.background_imagein, mBackgroundDrawable);
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "imageInWrapper animation starting");

            imageInWrapper.setAlpha(0);
            dimAlpha = android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA;
        }
        mAnimator.setDuration(android.support.v17.leanback.app.BackgroundManager.FADE_DURATION);
        mAnimator.start();
        android.support.v17.leanback.app.BackgroundManager.DrawableWrapper dimWrapper = getDimWrapper();
        if ((dimWrapper != null) && (dimAlpha >= 0)) {
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "dimwrapper animation starting to " + dimAlpha);

            mDimAnimator.cancel();
            mDimAnimator.setIntValues(dimWrapper.getAlpha(), dimAlpha);
            mDimAnimator.setDuration(android.support.v17.leanback.app.BackgroundManager.FADE_DURATION);
            mDimAnimator.setInterpolator(dimAlpha == android.support.v17.leanback.app.BackgroundManager.FULL_ALPHA ? mDecelerateInterpolator : mAccelerateInterpolator);
            mDimAnimator.start();
        }
    }

    /**
     * Returns the current background color.
     */
    @android.support.annotation.ColorInt
    public final int getColor() {
        return mBackgroundColor;
    }

    /**
     * Returns the current background {@link Drawable}.
     */
    public android.graphics.drawable.Drawable getDrawable() {
        return mBackgroundDrawable;
    }

    boolean sameDrawable(android.graphics.drawable.Drawable first, android.graphics.drawable.Drawable second) {
        if ((first == null) || (second == null)) {
            return false;
        }
        if (first == second) {
            return true;
        }
        if ((first instanceof android.support.v17.leanback.app.BackgroundManager.BitmapDrawable) && (second instanceof android.support.v17.leanback.app.BackgroundManager.BitmapDrawable)) {
            if (((android.support.v17.leanback.app.BackgroundManager.BitmapDrawable) (first)).getBitmap().sameAs(((android.support.v17.leanback.app.BackgroundManager.BitmapDrawable) (second)).getBitmap())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Task which changes the background.
     */
    class ChangeBackgroundRunnable implements java.lang.Runnable {
        android.graphics.drawable.Drawable mDrawable;

        ChangeBackgroundRunnable(android.graphics.drawable.Drawable drawable) {
            mDrawable = drawable;
        }

        @java.lang.Override
        public void run() {
            runTask();
            mChangeRunnable = null;
        }

        private void runTask() {
            if (mLayerDrawable == null) {
                if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "runTask while released - should not happen");

                return;
            }
            if (sameDrawable(mDrawable, mBackgroundDrawable)) {
                if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "new drawable same as current");

                return;
            }
            releaseBackgroundBitmap();
            android.support.v17.leanback.app.BackgroundManager.DrawableWrapper imageInWrapper = getImageInWrapper();
            if (imageInWrapper != null) {
                if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "moving image in to image out");

                // Order is important! Setting a drawable "removes" the
                // previous one from the view
                mLayerDrawable.clearDrawable(R.id.background_imagein, mContext);
                mLayerDrawable.updateDrawable(R.id.background_imageout, imageInWrapper.getDrawable());
            }
            setBackgroundDrawable(mDrawable);
            applyBackgroundChanges();
        }
    }

    static android.graphics.drawable.Drawable createEmptyDrawable(android.content.Context context) {
        android.graphics.Bitmap bitmap = null;
        return new android.support.v17.leanback.app.BackgroundManager.BitmapDrawable(context.getResources(), bitmap);
    }

    private void showWallpaper(boolean show) {
        if (mWindow == null) {
            return;
        }
        android.view.WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
        if (show) {
            if ((layoutParams.flags & android.view.WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER) != 0) {
                return;
            }
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "showing wallpaper");

            layoutParams.flags |= android.view.WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
        } else {
            if ((layoutParams.flags & android.view.WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER) == 0) {
                return;
            }
            if (android.support.v17.leanback.app.BackgroundManager.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BackgroundManager.TAG, "hiding wallpaper");

            layoutParams.flags &= ~android.view.WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
        }
        mWindow.setAttributes(layoutParams);
    }
}

