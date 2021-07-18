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
 * <p>
 * A user interface element that indicates the progress of an operation.
 * Progress bar supports two modes to represent progress: determinate, and indeterminate. For
 * a visual overview of the difference between determinate and indeterminate progress modes, see
 * <a href="https://material.io/guidelines/components/progress-activity.html#progress-activity-types-of-indicators">
 * Progress & activity</a>.
 * Display progress bars to a user in a non-interruptive way.
 * Show the progress bar in your app's user interface or in a notification
 * instead of within a dialog.
 * </p>
 * <h3>Indeterminate Progress</h3>
 * <p>
 * Use indeterminate mode for the progress bar when you do not know how long an
 * operation will take.
 * Indeterminate mode is the default for progress bar and shows a cyclic animation without a
 * specific amount of progress indicated.
 * The following example shows an indeterminate progress bar:
 * <pre>
 * &lt;ProgressBar
 *      android:id="@+id/indeterminateBar"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      /&gt;
 * </pre>
 * </p>
 * <h3>Determinate Progress</h3>
 * <p>
 * Use determinate mode for the progress bar when you want to show that a specific quantity of
 * progress has occurred.
 * For example, the percent remaining of a file being retrieved, the amount records in
 * a batch written to database, or the percent remaining of an audio file that is playing.
 * <p>
 * <p>
 * To indicate determinate progress, you set the style of the progress bar to
 * {@link android.R.style#Widget_ProgressBar_Horizontal} and set the amount of progress.
 * The following example shows a determinate progress bar that is 25% complete:
 * <pre>
 * &lt;ProgressBar
 *      android:id="@+id/determinateBar"
 *      style="@android:style/Widget.ProgressBar.Horizontal"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      android:progress="25"/&gt;
 * </pre>
 * You can update the percentage of progress displayed by using the
 * {@link #setProgress(int)} method, or by calling
 * {@link #incrementProgressBy(int)} to increase the current progress completed
 * by a specified amount.
 * By default, the progress bar is full when the progress value reaches 100.
 * You can adjust this default by setting the
 * {@link android.R.styleable#ProgressBar_max android:max} attribute.
 * </p>
 * <p>Other progress bar styles provided by the system include:</p>
 * <ul>
 * <li>{@link android.R.style#Widget_ProgressBar_Horizontal Widget.ProgressBar.Horizontal}</li>
 * <li>{@link android.R.style#Widget_ProgressBar_Small Widget.ProgressBar.Small}</li>
 * <li>{@link android.R.style#Widget_ProgressBar_Large Widget.ProgressBar.Large}</li>
 * <li>{@link android.R.style#Widget_ProgressBar_Inverse Widget.ProgressBar.Inverse}</li>
 * <li>{@link android.R.style#Widget_ProgressBar_Small_Inverse
 * Widget.ProgressBar.Small.Inverse}</li>
 * <li>{@link android.R.style#Widget_ProgressBar_Large_Inverse
 * Widget.ProgressBar.Large.Inverse}</li>
 * </ul>
 * <p>The "inverse" styles provide an inverse color scheme for the spinner, which may be necessary
 * if your application uses a light colored theme (a white background).</p>
 *
 * <p><strong>XML attributes</b></strong>
 * <p>
 * See {@link android.R.styleable#ProgressBar ProgressBar Attributes},
 * {@link android.R.styleable#View View Attributes}
 * </p>
 *
 * @unknown ref android.R.styleable#ProgressBar_animationResolution
 * @unknown ref android.R.styleable#ProgressBar_indeterminate
 * @unknown ref android.R.styleable#ProgressBar_indeterminateBehavior
 * @unknown ref android.R.styleable#ProgressBar_indeterminateDrawable
 * @unknown ref android.R.styleable#ProgressBar_indeterminateDuration
 * @unknown ref android.R.styleable#ProgressBar_indeterminateOnly
 * @unknown ref android.R.styleable#ProgressBar_interpolator
 * @unknown ref android.R.styleable#ProgressBar_min
 * @unknown ref android.R.styleable#ProgressBar_max
 * @unknown ref android.R.styleable#ProgressBar_maxHeight
 * @unknown ref android.R.styleable#ProgressBar_maxWidth
 * @unknown ref android.R.styleable#ProgressBar_minHeight
 * @unknown ref android.R.styleable#ProgressBar_minWidth
 * @unknown ref android.R.styleable#ProgressBar_mirrorForRtl
 * @unknown ref android.R.styleable#ProgressBar_progress
 * @unknown ref android.R.styleable#ProgressBar_progressDrawable
 * @unknown ref android.R.styleable#ProgressBar_secondaryProgress
 */
@android.widget.RemoteViews.RemoteView
public class ProgressBar extends android.view.View {
    private static final int MAX_LEVEL = 10000;

    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 200;

    /**
     * Interpolator used for smooth progress animations.
     */
    private static final android.view.animation.DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR = new android.view.animation.DecelerateInterpolator();

    /**
     * Duration of smooth progress animations.
     */
    private static final int PROGRESS_ANIM_DURATION = 80;

    /**
     * Outside the framework, please use {@link ProgressBar#getMinWidth()} and
     * {@link ProgressBar#setMinWidth(int)} instead of accessing these directly.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    int mMinWidth;

    int mMaxWidth;

    /**
     * Outside the framework, please use {@link ProgressBar#getMinHeight()} and
     * {@link ProgressBar#setMinHeight(int)} instead of accessing these directly.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    int mMinHeight;

    /**
     * Outside the framework, please use {@link ProgressBar#getMaxHeight()} ()} and
     * {@link ProgressBar#setMaxHeight(int)} (int)} instead of accessing these directly.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    int mMaxHeight;

    private int mProgress;

    private int mSecondaryProgress;

    private int mMin;

    private boolean mMinInitialized;

    private int mMax;

    private boolean mMaxInitialized;

    private int mBehavior;

    // Better to define a Drawable that implements Animatable if you want to modify animation
    // characteristics programatically.
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 124052713)
    private int mDuration;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private boolean mIndeterminate;

    @android.annotation.UnsupportedAppUsage(trackingBug = 124049927)
    private boolean mOnlyIndeterminate;

    private android.view.animation.Transformation mTransformation;

    private android.view.animation.AlphaAnimation mAnimation;

    private boolean mHasAnimation;

    private android.graphics.drawable.Drawable mIndeterminateDrawable;

    private android.graphics.drawable.Drawable mProgressDrawable;

    /**
     * Outside the framework, instead of accessing this directly, please use
     * {@link #getCurrentDrawable()}, {@link #setProgressDrawable(Drawable)},
     * {@link #setIndeterminateDrawable(Drawable)} and their tiled versions.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private android.graphics.drawable.Drawable mCurrentDrawable;

    private android.widget.ProgressBar.ProgressTintInfo mProgressTintInfo;

    int mSampleWidth = 0;

    private boolean mNoInvalidate;

    private android.view.animation.Interpolator mInterpolator;

    private android.widget.ProgressBar.RefreshProgressRunnable mRefreshProgressRunnable;

    private long mUiThreadId;

    private boolean mShouldStartAnimationDrawable;

    private boolean mInDrawing;

    private boolean mAttached;

    private boolean mRefreshIsPosted;

    /**
     * Value used to track progress animation, in the range [0...1].
     */
    private float mVisualProgress;

    @android.annotation.UnsupportedAppUsage
    boolean mMirrorForRtl = false;

    private boolean mAggregatedIsVisible;

    private final java.util.ArrayList<android.widget.ProgressBar.RefreshData> mRefreshData = new java.util.ArrayList<android.widget.ProgressBar.RefreshData>();

    private android.widget.ProgressBar.AccessibilityEventSender mAccessibilityEventSender;

    /**
     * Create a new progress bar with range 0...100 and initial progress of 0.
     *
     * @param context
     * 		the application environment
     */
    public ProgressBar(android.content.Context context) {
        this(context, null);
    }

    public ProgressBar(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.progressBarStyle);
    }

    public ProgressBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProgressBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mUiThreadId = java.lang.Thread.currentThread().getId();
        initProgressBar();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ProgressBar, attrs, a, defStyleAttr, defStyleRes);
        mNoInvalidate = true;
        final android.graphics.drawable.Drawable progressDrawable = a.getDrawable(R.styleable.ProgressBar_progressDrawable);
        if (progressDrawable != null) {
            // Calling setProgressDrawable can set mMaxHeight, so make sure the
            // corresponding XML attribute for mMaxHeight is read after calling
            // this method.
            if (android.widget.ProgressBar.needsTileify(progressDrawable)) {
                setProgressDrawableTiled(progressDrawable);
            } else {
                setProgressDrawable(progressDrawable);
            }
        }
        mDuration = a.getInt(R.styleable.ProgressBar_indeterminateDuration, mDuration);
        mMinWidth = a.getDimensionPixelSize(R.styleable.ProgressBar_minWidth, mMinWidth);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.ProgressBar_maxWidth, mMaxWidth);
        mMinHeight = a.getDimensionPixelSize(R.styleable.ProgressBar_minHeight, mMinHeight);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.ProgressBar_maxHeight, mMaxHeight);
        mBehavior = a.getInt(R.styleable.ProgressBar_indeterminateBehavior, mBehavior);
        final int resID = a.getResourceId(com.android.internal.R.styleable.ProgressBar_interpolator, android.R.anim.linear_interpolator);// default to linear interpolator

        if (resID > 0) {
            setInterpolator(context, resID);
        }
        setMin(a.getInt(R.styleable.ProgressBar_min, mMin));
        setMax(a.getInt(R.styleable.ProgressBar_max, mMax));
        setProgress(a.getInt(R.styleable.ProgressBar_progress, mProgress));
        setSecondaryProgress(a.getInt(R.styleable.ProgressBar_secondaryProgress, mSecondaryProgress));
        final android.graphics.drawable.Drawable indeterminateDrawable = a.getDrawable(R.styleable.ProgressBar_indeterminateDrawable);
        if (indeterminateDrawable != null) {
            if (android.widget.ProgressBar.needsTileify(indeterminateDrawable)) {
                setIndeterminateDrawableTiled(indeterminateDrawable);
            } else {
                setIndeterminateDrawable(indeterminateDrawable);
            }
        }
        mOnlyIndeterminate = a.getBoolean(R.styleable.ProgressBar_indeterminateOnly, mOnlyIndeterminate);
        mNoInvalidate = false;
        setIndeterminate(mOnlyIndeterminate || a.getBoolean(R.styleable.ProgressBar_indeterminate, mIndeterminate));
        mMirrorForRtl = a.getBoolean(R.styleable.ProgressBar_mirrorForRtl, mMirrorForRtl);
        if (a.hasValue(R.styleable.ProgressBar_progressTintMode)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mProgressBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.ProgressBar_progressTintMode, -1), null);
            mProgressTintInfo.mHasProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_progressTint)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mProgressTintList = a.getColorStateList(R.styleable.ProgressBar_progressTint);
            mProgressTintInfo.mHasProgressTint = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_progressBackgroundTintMode)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mProgressBackgroundBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.ProgressBar_progressBackgroundTintMode, -1), null);
            mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_progressBackgroundTint)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mProgressBackgroundTintList = a.getColorStateList(R.styleable.ProgressBar_progressBackgroundTint);
            mProgressTintInfo.mHasProgressBackgroundTint = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_secondaryProgressTintMode)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mSecondaryProgressBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.ProgressBar_secondaryProgressTintMode, -1), null);
            mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_secondaryProgressTint)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mSecondaryProgressTintList = a.getColorStateList(R.styleable.ProgressBar_secondaryProgressTint);
            mProgressTintInfo.mHasSecondaryProgressTint = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_indeterminateTintMode)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mIndeterminateBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.ProgressBar_indeterminateTintMode, -1), null);
            mProgressTintInfo.mHasIndeterminateTintMode = true;
        }
        if (a.hasValue(R.styleable.ProgressBar_indeterminateTint)) {
            if (mProgressTintInfo == null) {
                mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
            }
            mProgressTintInfo.mIndeterminateTintList = a.getColorStateList(R.styleable.ProgressBar_indeterminateTint);
            mProgressTintInfo.mHasIndeterminateTint = true;
        }
        a.recycle();
        applyProgressTints();
        applyIndeterminateTint();
        // If not explicitly specified this view is important for accessibility.
        if (getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    /**
     * Sets the minimum width the progress bar can have.
     *
     * @param minWidth
     * 		the minimum width to be set, in pixels
     * @unknown ref android.R.styleable#ProgressBar_minWidth
     */
    public void setMinWidth(@android.annotation.Px
    int minWidth) {
        mMinWidth = minWidth;
        requestLayout();
    }

    /**
     *
     *
     * @return the minimum width the progress bar can have, in pixels
     */
    @android.annotation.Px
    public int getMinWidth() {
        return mMinWidth;
    }

    /**
     * Sets the maximum width the progress bar can have.
     *
     * @param maxWidth
     * 		the maximum width to be set, in pixels
     * @unknown ref android.R.styleable#ProgressBar_maxWidth
     */
    public void setMaxWidth(@android.annotation.Px
    int maxWidth) {
        mMaxWidth = maxWidth;
        requestLayout();
    }

    /**
     *
     *
     * @return the maximum width the progress bar can have, in pixels
     */
    @android.annotation.Px
    public int getMaxWidth() {
        return mMaxWidth;
    }

    /**
     * Sets the minimum height the progress bar can have.
     *
     * @param minHeight
     * 		the minimum height to be set, in pixels
     * @unknown ref android.R.styleable#ProgressBar_minHeight
     */
    public void setMinHeight(@android.annotation.Px
    int minHeight) {
        mMinHeight = minHeight;
        requestLayout();
    }

    /**
     *
     *
     * @return the minimum height the progress bar can have, in pixels
     */
    @android.annotation.Px
    public int getMinHeight() {
        return mMinHeight;
    }

    /**
     * Sets the maximum height the progress bar can have.
     *
     * @param maxHeight
     * 		the maximum height to be set, in pixels
     * @unknown ref android.R.styleable#ProgressBar_maxHeight
     */
    public void setMaxHeight(@android.annotation.Px
    int maxHeight) {
        mMaxHeight = maxHeight;
        requestLayout();
    }

    /**
     *
     *
     * @return the maximum height the progress bar can have, in pixels
     */
    @android.annotation.Px
    public int getMaxHeight() {
        return mMaxHeight;
    }

    /**
     * Returns {@code true} if the target drawable needs to be tileified.
     *
     * @param dr
     * 		the drawable to check
     * @return {@code true} if the target drawable needs to be tileified,
    {@code false} otherwise
     */
    private static boolean needsTileify(android.graphics.drawable.Drawable dr) {
        if (dr instanceof android.graphics.drawable.LayerDrawable) {
            final android.graphics.drawable.LayerDrawable orig = ((android.graphics.drawable.LayerDrawable) (dr));
            final int N = orig.getNumberOfLayers();
            for (int i = 0; i < N; i++) {
                if (android.widget.ProgressBar.needsTileify(orig.getDrawable(i))) {
                    return true;
                }
            }
            return false;
        }
        if (dr instanceof android.graphics.drawable.StateListDrawable) {
            final android.graphics.drawable.StateListDrawable in = ((android.graphics.drawable.StateListDrawable) (dr));
            final int N = in.getStateCount();
            for (int i = 0; i < N; i++) {
                if (android.widget.ProgressBar.needsTileify(in.getStateDrawable(i))) {
                    return true;
                }
            }
            return false;
        }
        // If there's a bitmap that's not wrapped with a ClipDrawable or
        // ScaleDrawable, we'll need to wrap it and apply tiling.
        if (dr instanceof android.graphics.drawable.BitmapDrawable) {
            return true;
        }
        return false;
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable tileify(android.graphics.drawable.Drawable drawable, boolean clip) {
        // TODO: This is a terrible idea that potentially destroys any drawable
        // that extends any of these classes. We *really* need to remove this.
        if (drawable instanceof android.graphics.drawable.LayerDrawable) {
            final android.graphics.drawable.LayerDrawable orig = ((android.graphics.drawable.LayerDrawable) (drawable));
            final int N = orig.getNumberOfLayers();
            final android.graphics.drawable.Drawable[] outDrawables = new android.graphics.drawable.Drawable[N];
            for (int i = 0; i < N; i++) {
                final int id = orig.getId(i);
                outDrawables[i] = tileify(orig.getDrawable(i), (id == R.id.progress) || (id == R.id.secondaryProgress));
            }
            final android.graphics.drawable.LayerDrawable clone = new android.graphics.drawable.LayerDrawable(outDrawables);
            for (int i = 0; i < N; i++) {
                clone.setId(i, orig.getId(i));
                clone.setLayerGravity(i, orig.getLayerGravity(i));
                clone.setLayerWidth(i, orig.getLayerWidth(i));
                clone.setLayerHeight(i, orig.getLayerHeight(i));
                clone.setLayerInsetLeft(i, orig.getLayerInsetLeft(i));
                clone.setLayerInsetRight(i, orig.getLayerInsetRight(i));
                clone.setLayerInsetTop(i, orig.getLayerInsetTop(i));
                clone.setLayerInsetBottom(i, orig.getLayerInsetBottom(i));
                clone.setLayerInsetStart(i, orig.getLayerInsetStart(i));
                clone.setLayerInsetEnd(i, orig.getLayerInsetEnd(i));
            }
            return clone;
        }
        if (drawable instanceof android.graphics.drawable.StateListDrawable) {
            final android.graphics.drawable.StateListDrawable in = ((android.graphics.drawable.StateListDrawable) (drawable));
            final android.graphics.drawable.StateListDrawable out = new android.graphics.drawable.StateListDrawable();
            final int N = in.getStateCount();
            for (int i = 0; i < N; i++) {
                out.addState(in.getStateSet(i), tileify(in.getStateDrawable(i), clip));
            }
            return out;
        }
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            final android.graphics.drawable.Drawable.ConstantState cs = drawable.getConstantState();
            final android.graphics.drawable.BitmapDrawable clone = ((android.graphics.drawable.BitmapDrawable) (cs.newDrawable(getResources())));
            clone.setTileModeXY(android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.CLAMP);
            if (mSampleWidth <= 0) {
                mSampleWidth = clone.getIntrinsicWidth();
            }
            if (clip) {
                return new android.graphics.drawable.ClipDrawable(clone, android.view.Gravity.LEFT, android.graphics.drawable.ClipDrawable.HORIZONTAL);
            } else {
                return clone;
            }
        }
        return drawable;
    }

    android.graphics.drawable.shapes.Shape getDrawableShape() {
        final float[] roundedCorners = new float[]{ 5, 5, 5, 5, 5, 5, 5, 5 };
        return new android.graphics.drawable.shapes.RoundRectShape(roundedCorners, null, null);
    }

    /**
     * Convert a AnimationDrawable for use as a barberpole animation.
     * Each frame of the animation is wrapped in a ClipDrawable and
     * given a tiling BitmapShader.
     */
    private android.graphics.drawable.Drawable tileifyIndeterminate(android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.AnimationDrawable) {
            android.graphics.drawable.AnimationDrawable background = ((android.graphics.drawable.AnimationDrawable) (drawable));
            final int N = background.getNumberOfFrames();
            android.graphics.drawable.AnimationDrawable newBg = new android.graphics.drawable.AnimationDrawable();
            newBg.setOneShot(background.isOneShot());
            for (int i = 0; i < N; i++) {
                android.graphics.drawable.Drawable frame = tileify(background.getFrame(i), true);
                frame.setLevel(10000);
                newBg.addFrame(frame, background.getDuration(i));
            }
            newBg.setLevel(10000);
            drawable = newBg;
        }
        return drawable;
    }

    /**
     * <p>
     * Initialize the progress bar's default values:
     * </p>
     * <ul>
     * <li>progress = 0</li>
     * <li>max = 100</li>
     * <li>animation duration = 4000 ms</li>
     * <li>indeterminate = false</li>
     * <li>behavior = repeat</li>
     * </ul>
     */
    private void initProgressBar() {
        mMin = 0;
        mMax = 100;
        mProgress = 0;
        mSecondaryProgress = 0;
        mIndeterminate = false;
        mOnlyIndeterminate = false;
        mDuration = 4000;
        mBehavior = android.view.animation.AlphaAnimation.RESTART;
        mMinWidth = 24;
        mMaxWidth = 48;
        mMinHeight = 24;
        mMaxHeight = 48;
    }

    /**
     * <p>Indicate whether this progress bar is in indeterminate mode.</p>
     *
     * @return true if the progress bar is in indeterminate mode
     */
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty(category = "progress")
    public synchronized boolean isIndeterminate() {
        return mIndeterminate;
    }

    /**
     * <p>Change the indeterminate mode for this progress bar. In indeterminate
     * mode, the progress is ignored and the progress bar shows an infinite
     * animation instead.</p>
     *
     * If this progress bar's style only supports indeterminate mode (such as the circular
     * progress bars), then this will be ignored.
     *
     * @param indeterminate
     * 		true to enable the indeterminate mode
     */
    @android.view.RemotableViewMethod
    public synchronized void setIndeterminate(boolean indeterminate) {
        if (((!mOnlyIndeterminate) || (!mIndeterminate)) && (indeterminate != mIndeterminate)) {
            mIndeterminate = indeterminate;
            if (indeterminate) {
                // swap between indeterminate and regular backgrounds
                swapCurrentDrawable(mIndeterminateDrawable);
                startAnimation();
            } else {
                swapCurrentDrawable(mProgressDrawable);
                stopAnimation();
            }
        }
    }

    private void swapCurrentDrawable(android.graphics.drawable.Drawable newDrawable) {
        final android.graphics.drawable.Drawable oldDrawable = mCurrentDrawable;
        mCurrentDrawable = newDrawable;
        if (oldDrawable != mCurrentDrawable) {
            if (oldDrawable != null) {
                oldDrawable.setVisible(false, false);
            }
            if (mCurrentDrawable != null) {
                mCurrentDrawable.setVisible((getWindowVisibility() == android.view.View.VISIBLE) && isShown(), false);
            }
        }
    }

    /**
     * <p>Get the drawable used to draw the progress bar in
     * indeterminate mode.</p>
     *
     * @return a {@link android.graphics.drawable.Drawable} instance
     * @see #setIndeterminateDrawable(android.graphics.drawable.Drawable)
     * @see #setIndeterminate(boolean)
     */
    @android.view.inspector.InspectableProperty
    public android.graphics.drawable.Drawable getIndeterminateDrawable() {
        return mIndeterminateDrawable;
    }

    /**
     * Define the drawable used to draw the progress bar in indeterminate mode.
     *
     * <p>For the Drawable to animate, it must implement {@link Animatable}, or override
     * {@link Drawable#onLevelChange(int)}.  A Drawable that implements Animatable will be animated
     * via that interface and therefore provides the greatest amount of customization. A Drawable
     * that only overrides onLevelChange(int) is animated directly by ProgressBar and only the
     * animation {@link android.R.styleable#ProgressBar_indeterminateDuration duration},
     * {@link android.R.styleable#ProgressBar_indeterminateBehavior repeating behavior}, and
     * {@link #setInterpolator(Interpolator) interpolator} can be modified, and only before the
     * indeterminate animation begins.
     *
     * @param d
     * 		the new drawable
     * @unknown ref android.R.styleable#ProgressBar_indeterminateDrawable
     * @see #getIndeterminateDrawable()
     * @see #setIndeterminate(boolean)
     */
    public void setIndeterminateDrawable(android.graphics.drawable.Drawable d) {
        if (mIndeterminateDrawable != d) {
            if (mIndeterminateDrawable != null) {
                mIndeterminateDrawable.setCallback(null);
                unscheduleDrawable(mIndeterminateDrawable);
            }
            mIndeterminateDrawable = d;
            if (d != null) {
                d.setCallback(this);
                d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                applyIndeterminateTint();
            }
            if (mIndeterminate) {
                swapCurrentDrawable(d);
                postInvalidate();
            }
        }
    }

    /**
     * Applies a tint to the indeterminate drawable. Does not modify the
     * current tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setIndeterminateDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and
     * tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_indeterminateTint
     * @see #getIndeterminateTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    @android.view.RemotableViewMethod
    public void setIndeterminateTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mIndeterminateTintList = tint;
        mProgressTintInfo.mHasIndeterminateTint = true;
        applyIndeterminateTint();
    }

    /**
     *
     *
     * @return the tint applied to the indeterminate drawable
     * @unknown ref android.R.styleable#ProgressBar_indeterminateTint
     * @see #setIndeterminateTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "indeterminateTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getIndeterminateTintList() {
        return mProgressTintInfo != null ? mProgressTintInfo.mIndeterminateTintList : null;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setIndeterminateTintList(ColorStateList)} to the indeterminate
     * drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_indeterminateTintMode
     * @see #setIndeterminateTintList(ColorStateList)
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setIndeterminateTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setIndeterminateTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setIndeterminateTintList(ColorStateList)} to the indeterminate
     * drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_indeterminateTintMode
     * @see #setIndeterminateTintList(ColorStateList)
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setIndeterminateTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mIndeterminateBlendMode = blendMode;
        mProgressTintInfo.mHasIndeterminateTintMode = true;
        applyIndeterminateTint();
    }

    /**
     * Returns the blending mode used to apply the tint to the indeterminate
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the indeterminate
    drawable
     * @unknown ref android.R.styleable#ProgressBar_indeterminateTintMode
     * @see #setIndeterminateTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getIndeterminateTintMode() {
        android.graphics.BlendMode mode = getIndeterminateTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     * Returns the blending mode used to apply the tint to the indeterminate
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the indeterminate
    drawable
     * @unknown ref android.R.styleable#ProgressBar_indeterminateTintMode
     * @see #setIndeterminateTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(attributeId = R.styleable.ProgressBar_indeterminateTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getIndeterminateTintBlendMode() {
        return mProgressTintInfo != null ? mProgressTintInfo.mIndeterminateBlendMode : null;
    }

    private void applyIndeterminateTint() {
        if ((mIndeterminateDrawable != null) && (mProgressTintInfo != null)) {
            final android.widget.ProgressBar.ProgressTintInfo tintInfo = mProgressTintInfo;
            if (tintInfo.mHasIndeterminateTint || tintInfo.mHasIndeterminateTintMode) {
                mIndeterminateDrawable = mIndeterminateDrawable.mutate();
                if (tintInfo.mHasIndeterminateTint) {
                    mIndeterminateDrawable.setTintList(tintInfo.mIndeterminateTintList);
                }
                if (tintInfo.mHasIndeterminateTintMode) {
                    mIndeterminateDrawable.setTintBlendMode(tintInfo.mIndeterminateBlendMode);
                }
                // The drawable (or one of its children) may not have been
                // stateful before applying the tint, so let's try again.
                if (mIndeterminateDrawable.isStateful()) {
                    mIndeterminateDrawable.setState(getDrawableState());
                }
            }
        }
    }

    /**
     * Define the tileable drawable used to draw the progress bar in
     * indeterminate mode.
     * <p>
     * If the drawable is a BitmapDrawable or contains BitmapDrawables, a
     * tiled copy will be generated for display as a progress bar.
     *
     * @param d
     * 		the new drawable
     * @see #getIndeterminateDrawable()
     * @see #setIndeterminate(boolean)
     */
    public void setIndeterminateDrawableTiled(android.graphics.drawable.Drawable d) {
        if (d != null) {
            d = tileifyIndeterminate(d);
        }
        setIndeterminateDrawable(d);
    }

    /**
     * <p>Get the drawable used to draw the progress bar in
     * progress mode.</p>
     *
     * @return a {@link android.graphics.drawable.Drawable} instance
     * @see #setProgressDrawable(android.graphics.drawable.Drawable)
     * @see #setIndeterminate(boolean)
     */
    @android.view.inspector.InspectableProperty
    public android.graphics.drawable.Drawable getProgressDrawable() {
        return mProgressDrawable;
    }

    /**
     * Define the drawable used to draw the progress bar in progress mode.
     *
     * @param d
     * 		the new drawable
     * @see #getProgressDrawable()
     * @see #setIndeterminate(boolean)
     */
    public void setProgressDrawable(android.graphics.drawable.Drawable d) {
        if (mProgressDrawable != d) {
            if (mProgressDrawable != null) {
                mProgressDrawable.setCallback(null);
                unscheduleDrawable(mProgressDrawable);
            }
            mProgressDrawable = d;
            if (d != null) {
                d.setCallback(this);
                d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                // Make sure the ProgressBar is always tall enough
                int drawableHeight = d.getMinimumHeight();
                if (mMaxHeight < drawableHeight) {
                    mMaxHeight = drawableHeight;
                    requestLayout();
                }
                applyProgressTints();
            }
            if (!mIndeterminate) {
                swapCurrentDrawable(d);
                postInvalidate();
            }
            updateDrawableBounds(getWidth(), getHeight());
            updateDrawableState();
            doRefreshProgress(R.id.progress, mProgress, false, false, false);
            doRefreshProgress(R.id.secondaryProgress, mSecondaryProgress, false, false, false);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.view.inspector.InspectableProperty
    public boolean getMirrorForRtl() {
        return mMirrorForRtl;
    }

    /**
     * Applies the progress tints in order of increasing specificity.
     */
    private void applyProgressTints() {
        if ((mProgressDrawable != null) && (mProgressTintInfo != null)) {
            applyPrimaryProgressTint();
            applyProgressBackgroundTint();
            applySecondaryProgressTint();
        }
    }

    /**
     * Should only be called if we've already verified that mProgressDrawable
     * and mProgressTintInfo are non-null.
     */
    private void applyPrimaryProgressTint() {
        if (mProgressTintInfo.mHasProgressTint || mProgressTintInfo.mHasProgressTintMode) {
            final android.graphics.drawable.Drawable target = getTintTarget(R.id.progress, true);
            if (target != null) {
                if (mProgressTintInfo.mHasProgressTint) {
                    target.setTintList(mProgressTintInfo.mProgressTintList);
                }
                if (mProgressTintInfo.mHasProgressTintMode) {
                    target.setTintBlendMode(mProgressTintInfo.mProgressBlendMode);
                }
                // The drawable (or one of its children) may not have been
                // stateful before applying the tint, so let's try again.
                if (target.isStateful()) {
                    target.setState(getDrawableState());
                }
            }
        }
    }

    /**
     * Should only be called if we've already verified that mProgressDrawable
     * and mProgressTintInfo are non-null.
     */
    private void applyProgressBackgroundTint() {
        if (mProgressTintInfo.mHasProgressBackgroundTint || mProgressTintInfo.mHasProgressBackgroundTintMode) {
            final android.graphics.drawable.Drawable target = getTintTarget(R.id.background, false);
            if (target != null) {
                if (mProgressTintInfo.mHasProgressBackgroundTint) {
                    target.setTintList(mProgressTintInfo.mProgressBackgroundTintList);
                }
                if (mProgressTintInfo.mHasProgressBackgroundTintMode) {
                    target.setTintBlendMode(mProgressTintInfo.mProgressBackgroundBlendMode);
                }
                // The drawable (or one of its children) may not have been
                // stateful before applying the tint, so let's try again.
                if (target.isStateful()) {
                    target.setState(getDrawableState());
                }
            }
        }
    }

    /**
     * Should only be called if we've already verified that mProgressDrawable
     * and mProgressTintInfo are non-null.
     */
    private void applySecondaryProgressTint() {
        if (mProgressTintInfo.mHasSecondaryProgressTint || mProgressTintInfo.mHasSecondaryProgressTintMode) {
            final android.graphics.drawable.Drawable target = getTintTarget(R.id.secondaryProgress, false);
            if (target != null) {
                if (mProgressTintInfo.mHasSecondaryProgressTint) {
                    target.setTintList(mProgressTintInfo.mSecondaryProgressTintList);
                }
                if (mProgressTintInfo.mHasSecondaryProgressTintMode) {
                    target.setTintBlendMode(mProgressTintInfo.mSecondaryProgressBlendMode);
                }
                // The drawable (or one of its children) may not have been
                // stateful before applying the tint, so let's try again.
                if (target.isStateful()) {
                    target.setState(getDrawableState());
                }
            }
        }
    }

    /**
     * Applies a tint to the progress indicator, if one exists, or to the
     * entire progress drawable otherwise. Does not modify the current tint
     * mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * The progress indicator should be specified as a layer with
     * id {@link android.R.id#progress} in a {@link LayerDrawable}
     * used as the progress drawable.
     * <p>
     * Subsequent calls to {@link #setProgressDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and
     * tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_progressTint
     * @see #getProgressTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    @android.view.RemotableViewMethod
    public void setProgressTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mProgressTintList = tint;
        mProgressTintInfo.mHasProgressTint = true;
        if (mProgressDrawable != null) {
            applyPrimaryProgressTint();
        }
    }

    /**
     * Returns the tint applied to the progress drawable, if specified.
     *
     * @return the tint applied to the progress drawable
     * @unknown ref android.R.styleable#ProgressBar_progressTint
     * @see #setProgressTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "progressTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getProgressTintList() {
        return mProgressTintInfo != null ? mProgressTintInfo.mProgressTintList : null;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setProgressTintList(ColorStateList)}} to the progress
     * indicator. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_progressTintMode
     * @see #getProgressTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setProgressTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setProgressTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setProgressTintList(ColorStateList)}} to the progress
     * indicator. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_progressTintMode
     * @see #getProgressTintMode()
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setProgressTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mProgressBlendMode = blendMode;
        mProgressTintInfo.mHasProgressTintMode = true;
        if (mProgressDrawable != null) {
            applyPrimaryProgressTint();
        }
    }

    /**
     * Returns the blending mode used to apply the tint to the progress
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the progress
    drawable
     * @unknown ref android.R.styleable#ProgressBar_progressTintMode
     * @see #setProgressTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getProgressTintMode() {
        android.graphics.BlendMode mode = getProgressTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     * Returns the blending mode used to apply the tint to the progress
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the progress
    drawable
     * @unknown ref android.R.styleable#ProgressBar_progressTintMode
     * @see #setProgressTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(attributeId = android.R.styleable.ProgressBar_progressTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getProgressTintBlendMode() {
        return mProgressTintInfo != null ? mProgressTintInfo.mProgressBlendMode : null;
    }

    /**
     * Applies a tint to the progress background, if one exists. Does not
     * modify the current tint mode, which is
     * {@link PorterDuff.Mode#SRC_ATOP} by default.
     * <p>
     * The progress background must be specified as a layer with
     * id {@link android.R.id#background} in a {@link LayerDrawable}
     * used as the progress drawable.
     * <p>
     * Subsequent calls to {@link #setProgressDrawable(Drawable)} where the
     * drawable contains a progress background will automatically mutate the
     * drawable and apply the specified tint and tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_progressBackgroundTint
     * @see #getProgressBackgroundTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    @android.view.RemotableViewMethod
    public void setProgressBackgroundTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mProgressBackgroundTintList = tint;
        mProgressTintInfo.mHasProgressBackgroundTint = true;
        if (mProgressDrawable != null) {
            applyProgressBackgroundTint();
        }
    }

    /**
     * Returns the tint applied to the progress background, if specified.
     *
     * @return the tint applied to the progress background
     * @unknown ref android.R.styleable#ProgressBar_progressBackgroundTint
     * @see #setProgressBackgroundTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "progressBackgroundTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getProgressBackgroundTintList() {
        return mProgressTintInfo != null ? mProgressTintInfo.mProgressBackgroundTintList : null;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setProgressBackgroundTintList(ColorStateList)}} to the progress
     * background. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_progressBackgroundTintMode
     * @see #setProgressBackgroundTintList(ColorStateList)
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setProgressBackgroundTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setProgressBackgroundTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setProgressBackgroundTintList(ColorStateList)}} to the progress
     * background. The default mode is {@link BlendMode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_progressBackgroundTintMode
     * @see #setProgressBackgroundTintList(ColorStateList)
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setProgressBackgroundTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mProgressBackgroundBlendMode = blendMode;
        mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        if (mProgressDrawable != null) {
            applyProgressBackgroundTint();
        }
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the progress
    background
     * @unknown ref android.R.styleable#ProgressBar_progressBackgroundTintMode
     * @see #setProgressBackgroundTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getProgressBackgroundTintMode() {
        android.graphics.BlendMode mode = getProgressBackgroundTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the progress
    background
     * @unknown ref android.R.styleable#ProgressBar_progressBackgroundTintMode
     * @see #setProgressBackgroundTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(attributeId = R.styleable.ProgressBar_progressBackgroundTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getProgressBackgroundTintBlendMode() {
        return mProgressTintInfo != null ? mProgressTintInfo.mProgressBackgroundBlendMode : null;
    }

    /**
     * Applies a tint to the secondary progress indicator, if one exists.
     * Does not modify the current tint mode, which is
     * {@link PorterDuff.Mode#SRC_ATOP} by default.
     * <p>
     * The secondary progress indicator must be specified as a layer with
     * id {@link android.R.id#secondaryProgress} in a {@link LayerDrawable}
     * used as the progress drawable.
     * <p>
     * Subsequent calls to {@link #setProgressDrawable(Drawable)} where the
     * drawable contains a secondary progress indicator will automatically
     * mutate the drawable and apply the specified tint and tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_secondaryProgressTint
     * @see #getSecondaryProgressTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setSecondaryProgressTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mSecondaryProgressTintList = tint;
        mProgressTintInfo.mHasSecondaryProgressTint = true;
        if (mProgressDrawable != null) {
            applySecondaryProgressTint();
        }
    }

    /**
     * Returns the tint applied to the secondary progress drawable, if
     * specified.
     *
     * @return the tint applied to the secondary progress drawable
     * @unknown ref android.R.styleable#ProgressBar_secondaryProgressTint
     * @see #setSecondaryProgressTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "secondaryProgressTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getSecondaryProgressTintList() {
        return mProgressTintInfo != null ? mProgressTintInfo.mSecondaryProgressTintList : null;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setSecondaryProgressTintList(ColorStateList)}} to the secondary
     * progress indicator. The default mode is
     * {@link PorterDuff.Mode#SRC_ATOP}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_secondaryProgressTintMode
     * @see #setSecondaryProgressTintList(ColorStateList)
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setSecondaryProgressTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setSecondaryProgressTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setSecondaryProgressTintList(ColorStateList)}} to the secondary
     * progress indicator. The default mode is
     * {@link PorterDuff.Mode#SRC_ATOP}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ProgressBar_secondaryProgressTintMode
     * @see #setSecondaryProgressTintList(ColorStateList)
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setSecondaryProgressTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        if (mProgressTintInfo == null) {
            mProgressTintInfo = new android.widget.ProgressBar.ProgressTintInfo();
        }
        mProgressTintInfo.mSecondaryProgressBlendMode = blendMode;
        mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        if (mProgressDrawable != null) {
            applySecondaryProgressTint();
        }
    }

    /**
     * Returns the blending mode used to apply the tint to the secondary
     * progress drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the secondary
    progress drawable
     * @unknown ref android.R.styleable#ProgressBar_secondaryProgressTintMode
     * @see #setSecondaryProgressTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getSecondaryProgressTintMode() {
        android.graphics.BlendMode mode = getSecondaryProgressTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     * Returns the blending mode used to apply the tint to the secondary
     * progress drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the secondary
    progress drawable
     * @unknown ref android.R.styleable#ProgressBar_secondaryProgressTintMode
     * @see #setSecondaryProgressTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(attributeId = android.R.styleable.ProgressBar_secondaryProgressTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getSecondaryProgressTintBlendMode() {
        return mProgressTintInfo != null ? mProgressTintInfo.mSecondaryProgressBlendMode : null;
    }

    /**
     * Returns the drawable to which a tint or tint mode should be applied.
     *
     * @param layerId
     * 		id of the layer to modify
     * @param shouldFallback
     * 		whether the base drawable should be returned
     * 		if the id does not exist
     * @return the drawable to modify
     */
    @android.annotation.Nullable
    private android.graphics.drawable.Drawable getTintTarget(int layerId, boolean shouldFallback) {
        android.graphics.drawable.Drawable layer = null;
        final android.graphics.drawable.Drawable d = mProgressDrawable;
        if (d != null) {
            mProgressDrawable = d.mutate();
            if (d instanceof android.graphics.drawable.LayerDrawable) {
                layer = ((android.graphics.drawable.LayerDrawable) (d)).findDrawableByLayerId(layerId);
            }
            if (shouldFallback && (layer == null)) {
                layer = d;
            }
        }
        return layer;
    }

    /**
     * Define the tileable drawable used to draw the progress bar in
     * progress mode.
     * <p>
     * If the drawable is a BitmapDrawable or contains BitmapDrawables, a
     * tiled copy will be generated for display as a progress bar.
     *
     * @param d
     * 		the new drawable
     * @see #getProgressDrawable()
     * @see #setIndeterminate(boolean)
     */
    public void setProgressDrawableTiled(android.graphics.drawable.Drawable d) {
        if (d != null) {
            d = tileify(d, false);
        }
        setProgressDrawable(d);
    }

    /**
     * Returns the drawable currently used to draw the progress bar. This will be
     * either {@link #getProgressDrawable()} or {@link #getIndeterminateDrawable()}
     * depending on whether the progress bar is in determinate or indeterminate mode.
     *
     * @return the drawable currently used to draw the progress bar
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getCurrentDrawable() {
        return mCurrentDrawable;
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        return ((who == mProgressDrawable) || (who == mIndeterminateDrawable)) || super.verifyDrawable(who);
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mProgressDrawable != null)
            mProgressDrawable.jumpToCurrentState();

        if (mIndeterminateDrawable != null)
            mIndeterminateDrawable.jumpToCurrentState();

    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onResolveDrawables(int layoutDirection) {
        final android.graphics.drawable.Drawable d = mCurrentDrawable;
        if (d != null) {
            d.setLayoutDirection(layoutDirection);
        }
        if (mIndeterminateDrawable != null) {
            mIndeterminateDrawable.setLayoutDirection(layoutDirection);
        }
        if (mProgressDrawable != null) {
            mProgressDrawable.setLayoutDirection(layoutDirection);
        }
    }

    @java.lang.Override
    public void postInvalidate() {
        if (!mNoInvalidate) {
            super.postInvalidate();
        }
    }

    private class RefreshProgressRunnable implements java.lang.Runnable {
        public void run() {
            synchronized(android.widget.ProgressBar.this) {
                final int count = mRefreshData.size();
                for (int i = 0; i < count; i++) {
                    final android.widget.ProgressBar.RefreshData rd = mRefreshData.get(i);
                    doRefreshProgress(rd.id, rd.progress, rd.fromUser, true, rd.animate);
                    rd.recycle();
                }
                mRefreshData.clear();
                mRefreshIsPosted = false;
            }
        }
    }

    private static class RefreshData {
        private static final int POOL_MAX = 24;

        private static final android.util.Pools.SynchronizedPool<android.widget.ProgressBar.RefreshData> sPool = new android.util.Pools.SynchronizedPool<android.widget.ProgressBar.RefreshData>(android.widget.ProgressBar.RefreshData.POOL_MAX);

        public int id;

        public int progress;

        public boolean fromUser;

        public boolean animate;

        public static android.widget.ProgressBar.RefreshData obtain(int id, int progress, boolean fromUser, boolean animate) {
            android.widget.ProgressBar.RefreshData rd = android.widget.ProgressBar.RefreshData.sPool.acquire();
            if (rd == null) {
                rd = new android.widget.ProgressBar.RefreshData();
            }
            rd.id = id;
            rd.progress = progress;
            rd.fromUser = fromUser;
            rd.animate = animate;
            return rd;
        }

        public void recycle() {
            android.widget.ProgressBar.RefreshData.sPool.release(this);
        }
    }

    private synchronized void doRefreshProgress(int id, int progress, boolean fromUser, boolean callBackToApp, boolean animate) {
        int range = mMax - mMin;
        final float scale = (range > 0) ? (progress - mMin) / ((float) (range)) : 0;
        final boolean isPrimary = id == R.id.progress;
        if (isPrimary && animate) {
            final android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(this, VISUAL_PROGRESS, scale);
            animator.setAutoCancel(true);
            animator.setDuration(android.widget.ProgressBar.PROGRESS_ANIM_DURATION);
            animator.setInterpolator(android.widget.ProgressBar.PROGRESS_ANIM_INTERPOLATOR);
            animator.start();
        } else {
            setVisualProgress(id, scale);
        }
        if (isPrimary && callBackToApp) {
            onProgressRefresh(scale, fromUser, progress);
        }
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        if (android.view.accessibility.AccessibilityManager.getInstance(mContext).isEnabled()) {
            scheduleAccessibilityEventSender();
        }
    }

    /**
     * Sets the visual state of a progress indicator.
     *
     * @param id
     * 		the identifier of the progress indicator
     * @param progress
     * 		the visual progress in the range [0...1]
     */
    private void setVisualProgress(int id, float progress) {
        mVisualProgress = progress;
        android.graphics.drawable.Drawable d = mCurrentDrawable;
        if (d instanceof android.graphics.drawable.LayerDrawable) {
            d = ((android.graphics.drawable.LayerDrawable) (d)).findDrawableByLayerId(id);
            if (d == null) {
                // If we can't find the requested layer, fall back to setting
                // the level of the entire drawable. This will break if
                // progress is set on multiple elements, but the theme-default
                // drawable will always have all layer IDs present.
                d = mCurrentDrawable;
            }
        }
        if (d != null) {
            final int level = ((int) (progress * android.widget.ProgressBar.MAX_LEVEL));
            d.setLevel(level);
        } else {
            invalidate();
        }
        onVisualProgressChanged(id, progress);
    }

    /**
     * Called when the visual state of a progress indicator changes.
     *
     * @param id
     * 		the identifier of the progress indicator
     * @param progress
     * 		the visual progress in the range [0...1]
     */
    void onVisualProgressChanged(int id, float progress) {
        // Stub method.
    }

    @android.annotation.UnsupportedAppUsage
    private synchronized void refreshProgress(int id, int progress, boolean fromUser, boolean animate) {
        if (mUiThreadId == java.lang.Thread.currentThread().getId()) {
            doRefreshProgress(id, progress, fromUser, true, animate);
        } else {
            if (mRefreshProgressRunnable == null) {
                mRefreshProgressRunnable = new android.widget.ProgressBar.RefreshProgressRunnable();
            }
            final android.widget.ProgressBar.RefreshData rd = android.widget.ProgressBar.RefreshData.obtain(id, progress, fromUser, animate);
            mRefreshData.add(rd);
            if (mAttached && (!mRefreshIsPosted)) {
                post(mRefreshProgressRunnable);
                mRefreshIsPosted = true;
            }
        }
    }

    /**
     * Sets the current progress to the specified value. Does not do anything
     * if the progress bar is in indeterminate mode.
     * <p>
     * This method will immediately update the visual position of the progress
     * indicator. To animate the visual position to the target value, use
     * {@link #setProgress(int, boolean)}}.
     *
     * @param progress
     * 		the new progress, between {@link #getMin()} and {@link #getMax()}
     * @see #setIndeterminate(boolean)
     * @see #isIndeterminate()
     * @see #getProgress()
     * @see #incrementProgressBy(int)
     */
    @android.view.RemotableViewMethod
    public synchronized void setProgress(int progress) {
        setProgressInternal(progress, false, false);
    }

    /**
     * Sets the current progress to the specified value, optionally animating
     * the visual position between the current and target values.
     * <p>
     * Animation does not affect the result of {@link #getProgress()}, which
     * will return the target value immediately after this method is called.
     *
     * @param progress
     * 		the new progress value, between {@link #getMin()} and {@link #getMax()}
     * @param animate
     * 		{@code true} to animate between the current and target
     * 		values or {@code false} to not animate
     */
    public void setProgress(int progress, boolean animate) {
        setProgressInternal(progress, false, animate);
    }

    @android.view.RemotableViewMethod
    @android.annotation.UnsupportedAppUsage
    synchronized boolean setProgressInternal(int progress, boolean fromUser, boolean animate) {
        if (mIndeterminate) {
            // Not applicable.
            return false;
        }
        progress = android.util.MathUtils.constrain(progress, mMin, mMax);
        if (progress == mProgress) {
            // No change from current.
            return false;
        }
        mProgress = progress;
        refreshProgress(R.id.progress, mProgress, fromUser, animate);
        return true;
    }

    /**
     * <p>
     * Set the current secondary progress to the specified value. Does not do
     * anything if the progress bar is in indeterminate mode.
     * </p>
     *
     * @param secondaryProgress
     * 		the new secondary progress, between {@link #getMin()} and
     * 		{@link #getMax()}
     * @see #setIndeterminate(boolean)
     * @see #isIndeterminate()
     * @see #getSecondaryProgress()
     * @see #incrementSecondaryProgressBy(int)
     */
    @android.view.RemotableViewMethod
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (mIndeterminate) {
            return;
        }
        if (secondaryProgress < mMin) {
            secondaryProgress = mMin;
        }
        if (secondaryProgress > mMax) {
            secondaryProgress = mMax;
        }
        if (secondaryProgress != mSecondaryProgress) {
            mSecondaryProgress = secondaryProgress;
            refreshProgress(R.id.secondaryProgress, mSecondaryProgress, false, false);
        }
    }

    /**
     * <p>Get the progress bar's current level of progress. Return 0 when the
     * progress bar is in indeterminate mode.</p>
     *
     * @return the current progress, between {@link #getMin()} and {@link #getMax()}
     * @see #setIndeterminate(boolean)
     * @see #isIndeterminate()
     * @see #setProgress(int)
     * @see #setMax(int)
     * @see #getMax()
     */
    @android.view.ViewDebug.ExportedProperty(category = "progress")
    @android.view.inspector.InspectableProperty
    public synchronized int getProgress() {
        return mIndeterminate ? 0 : mProgress;
    }

    /**
     * <p>Get the progress bar's current level of secondary progress. Return 0 when the
     * progress bar is in indeterminate mode.</p>
     *
     * @return the current secondary progress, between {@link #getMin()} and {@link #getMax()}
     * @see #setIndeterminate(boolean)
     * @see #isIndeterminate()
     * @see #setSecondaryProgress(int)
     * @see #setMax(int)
     * @see #getMax()
     */
    @android.view.ViewDebug.ExportedProperty(category = "progress")
    @android.view.inspector.InspectableProperty
    public synchronized int getSecondaryProgress() {
        return mIndeterminate ? 0 : mSecondaryProgress;
    }

    /**
     * <p>Return the lower limit of this progress bar's range.</p>
     *
     * @return a positive integer
     * @see #setMin(int)
     * @see #getProgress()
     * @see #getSecondaryProgress()
     */
    @android.view.ViewDebug.ExportedProperty(category = "progress")
    @android.view.inspector.InspectableProperty
    public synchronized int getMin() {
        return mMin;
    }

    /**
     * <p>Return the upper limit of this progress bar's range.</p>
     *
     * @return a positive integer
     * @see #setMax(int)
     * @see #getProgress()
     * @see #getSecondaryProgress()
     */
    @android.view.ViewDebug.ExportedProperty(category = "progress")
    @android.view.inspector.InspectableProperty
    public synchronized int getMax() {
        return mMax;
    }

    /**
     * <p>Set the lower range of the progress bar to <tt>min</tt>.</p>
     *
     * @param min
     * 		the lower range of this progress bar
     * @see #getMin()
     * @see #setProgress(int)
     * @see #setSecondaryProgress(int)
     */
    @android.view.RemotableViewMethod
    public synchronized void setMin(int min) {
        if (mMaxInitialized) {
            if (min > mMax) {
                min = mMax;
            }
        }
        mMinInitialized = true;
        if (mMaxInitialized && (min != mMin)) {
            mMin = min;
            postInvalidate();
            if (mProgress < min) {
                mProgress = min;
            }
            refreshProgress(R.id.progress, mProgress, false, false);
        } else {
            mMin = min;
        }
    }

    /**
     * <p>Set the upper range of the progress bar <tt>max</tt>.</p>
     *
     * @param max
     * 		the upper range of this progress bar
     * @see #getMax()
     * @see #setProgress(int)
     * @see #setSecondaryProgress(int)
     */
    @android.view.RemotableViewMethod
    public synchronized void setMax(int max) {
        if (mMinInitialized) {
            if (max < mMin) {
                max = mMin;
            }
        }
        mMaxInitialized = true;
        if (mMinInitialized && (max != mMax)) {
            mMax = max;
            postInvalidate();
            if (mProgress > max) {
                mProgress = max;
            }
            refreshProgress(R.id.progress, mProgress, false, false);
        } else {
            mMax = max;
        }
    }

    /**
     * <p>Increase the progress bar's progress by the specified amount.</p>
     *
     * @param diff
     * 		the amount by which the progress must be increased
     * @see #setProgress(int)
     */
    public final synchronized void incrementProgressBy(int diff) {
        setProgress(mProgress + diff);
    }

    /**
     * <p>Increase the progress bar's secondary progress by the specified amount.</p>
     *
     * @param diff
     * 		the amount by which the secondary progress must be increased
     * @see #setSecondaryProgress(int)
     */
    public final synchronized void incrementSecondaryProgressBy(int diff) {
        setSecondaryProgress(mSecondaryProgress + diff);
    }

    /**
     * <p>Start the indeterminate progress animation.</p>
     */
    @android.annotation.UnsupportedAppUsage
    void startAnimation() {
        if ((getVisibility() != android.view.View.VISIBLE) || (getWindowVisibility() != android.view.View.VISIBLE)) {
            return;
        }
        if (mIndeterminateDrawable instanceof android.graphics.drawable.Animatable) {
            mShouldStartAnimationDrawable = true;
            mHasAnimation = false;
        } else {
            mHasAnimation = true;
            if (mInterpolator == null) {
                mInterpolator = new android.view.animation.LinearInterpolator();
            }
            if (mTransformation == null) {
                mTransformation = new android.view.animation.Transformation();
            } else {
                mTransformation.clear();
            }
            if (mAnimation == null) {
                mAnimation = new android.view.animation.AlphaAnimation(0.0F, 1.0F);
            } else {
                mAnimation.reset();
            }
            mAnimation.setRepeatMode(mBehavior);
            mAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
            mAnimation.setDuration(mDuration);
            mAnimation.setInterpolator(mInterpolator);
            mAnimation.setStartTime(android.view.animation.Animation.START_ON_FIRST_FRAME);
        }
        postInvalidate();
    }

    /**
     * <p>Stop the indeterminate progress animation.</p>
     */
    @android.annotation.UnsupportedAppUsage
    void stopAnimation() {
        mHasAnimation = false;
        if (mIndeterminateDrawable instanceof android.graphics.drawable.Animatable) {
            ((android.graphics.drawable.Animatable) (mIndeterminateDrawable)).stop();
            mShouldStartAnimationDrawable = false;
        }
        postInvalidate();
    }

    /**
     * Sets the acceleration curve for the indeterminate animation.
     *
     * <p>The interpolator is loaded as a resource from the specified context. Defaults to a linear
     * interpolation.
     *
     * <p>The interpolator only affects the indeterminate animation if the
     * {@link #setIndeterminateDrawable(Drawable) supplied indeterminate drawable} does not
     * implement {@link Animatable}.
     *
     * <p>This call must be made before the indeterminate animation starts for it to have an affect.
     *
     * @param context
     * 		The application environment
     * @param resID
     * 		The resource identifier of the interpolator to load
     * @unknown ref android.R.styleable#ProgressBar_interpolator
     * @see #setInterpolator(Interpolator)
     * @see #getInterpolator()
     */
    public void setInterpolator(android.content.Context context, @android.annotation.InterpolatorRes
    int resID) {
        setInterpolator(android.view.animation.AnimationUtils.loadInterpolator(context, resID));
    }

    /**
     * Sets the acceleration curve for the indeterminate animation.
     * Defaults to a linear interpolation.
     *
     * <p>The interpolator only affects the indeterminate animation if the
     * {@link #setIndeterminateDrawable(Drawable) supplied indeterminate drawable} does not
     * implement {@link Animatable}.
     *
     * <p>This call must be made before the indeterminate animation starts for it to have
     * an affect.
     *
     * @param interpolator
     * 		The interpolator which defines the acceleration curve
     * @unknown ref android.R.styleable#ProgressBar_interpolator
     * @see #setInterpolator(Context, int)
     * @see #getInterpolator()
     */
    public void setInterpolator(android.view.animation.Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    /**
     * Gets the acceleration curve type for the indeterminate animation.
     *
     * @return the {@link Interpolator} associated to this animation
     * @unknown ref android.R.styleable#ProgressBar_interpolator
     * @see #setInterpolator(Context, int)
     * @see #setInterpolator(Interpolator)
     */
    @android.view.inspector.InspectableProperty
    public android.view.animation.Interpolator getInterpolator() {
        return mInterpolator;
    }

    @java.lang.Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (isVisible != mAggregatedIsVisible) {
            mAggregatedIsVisible = isVisible;
            if (mIndeterminate) {
                // let's be nice with the UI thread
                if (isVisible) {
                    startAnimation();
                } else {
                    stopAnimation();
                }
            }
            if (mCurrentDrawable != null) {
                mCurrentDrawable.setVisible(isVisible, false);
            }
        }
    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable dr) {
        if (!mInDrawing) {
            if (verifyDrawable(dr)) {
                final android.graphics.Rect dirty = dr.getBounds();
                final int scrollX = mScrollX + mPaddingLeft;
                final int scrollY = mScrollY + mPaddingTop;
                invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            } else {
                super.invalidateDrawable(dr);
            }
        }
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        w -= mPaddingRight + mPaddingLeft;
        h -= mPaddingTop + mPaddingBottom;
        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;
        if (mIndeterminateDrawable != null) {
            // Aspect ratio logic does not apply to AnimationDrawables
            if (mOnlyIndeterminate && (!(mIndeterminateDrawable instanceof android.graphics.drawable.AnimationDrawable))) {
                // Maintain aspect ratio. Certain kinds of animated drawables
                // get very confused otherwise.
                final int intrinsicWidth = mIndeterminateDrawable.getIntrinsicWidth();
                final int intrinsicHeight = mIndeterminateDrawable.getIntrinsicHeight();
                final float intrinsicAspect = ((float) (intrinsicWidth)) / intrinsicHeight;
                final float boundAspect = ((float) (w)) / h;
                if (intrinsicAspect != boundAspect) {
                    if (boundAspect > intrinsicAspect) {
                        // New width is larger. Make it smaller to match height.
                        final int width = ((int) (h * intrinsicAspect));
                        left = (w - width) / 2;
                        right = left + width;
                    } else {
                        // New height is larger. Make it smaller to match width.
                        final int height = ((int) (w * (1 / intrinsicAspect)));
                        top = (h - height) / 2;
                        bottom = top + height;
                    }
                }
            }
            if (isLayoutRtl() && mMirrorForRtl) {
                int tempLeft = left;
                left = w - right;
                right = w - tempLeft;
            }
            mIndeterminateDrawable.setBounds(left, top, right, bottom);
        }
        if (mProgressDrawable != null) {
            mProgressDrawable.setBounds(0, 0, right, bottom);
        }
    }

    @java.lang.Override
    protected synchronized void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    /**
     * Draws the progress bar track.
     */
    void drawTrack(android.graphics.Canvas canvas) {
        final android.graphics.drawable.Drawable d = mCurrentDrawable;
        if (d != null) {
            // Translate canvas so a indeterminate circular progress bar with padding
            // rotates properly in its animation
            final int saveCount = canvas.save();
            if (isLayoutRtl() && mMirrorForRtl) {
                canvas.translate(getWidth() - mPaddingRight, mPaddingTop);
                canvas.scale(-1.0F, 1.0F);
            } else {
                canvas.translate(mPaddingLeft, mPaddingTop);
            }
            final long time = getDrawingTime();
            if (mHasAnimation) {
                mAnimation.getTransformation(time, mTransformation);
                final float scale = mTransformation.getAlpha();
                try {
                    mInDrawing = true;
                    d.setLevel(((int) (scale * android.widget.ProgressBar.MAX_LEVEL)));
                } finally {
                    mInDrawing = false;
                }
                postInvalidateOnAnimation();
            }
            d.draw(canvas);
            canvas.restoreToCount(saveCount);
            if (mShouldStartAnimationDrawable && (d instanceof android.graphics.drawable.Animatable)) {
                ((android.graphics.drawable.Animatable) (d)).start();
                mShouldStartAnimationDrawable = false;
            }
        }
    }

    @java.lang.Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;
        final android.graphics.drawable.Drawable d = mCurrentDrawable;
        if (d != null) {
            dw = java.lang.Math.max(mMinWidth, java.lang.Math.min(mMaxWidth, d.getIntrinsicWidth()));
            dh = java.lang.Math.max(mMinHeight, java.lang.Math.min(mMaxHeight, d.getIntrinsicHeight()));
        }
        updateDrawableState();
        dw += mPaddingLeft + mPaddingRight;
        dh += mPaddingTop + mPaddingBottom;
        final int measuredWidth = android.view.View.resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = android.view.View.resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    private void updateDrawableState() {
        final int[] state = getDrawableState();
        boolean changed = false;
        final android.graphics.drawable.Drawable progressDrawable = mProgressDrawable;
        if ((progressDrawable != null) && progressDrawable.isStateful()) {
            changed |= progressDrawable.setState(state);
        }
        final android.graphics.drawable.Drawable indeterminateDrawable = mIndeterminateDrawable;
        if ((indeterminateDrawable != null) && indeterminateDrawable.isStateful()) {
            changed |= indeterminateDrawable.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mProgressDrawable != null) {
            mProgressDrawable.setHotspot(x, y);
        }
        if (mIndeterminateDrawable != null) {
            mIndeterminateDrawable.setHotspot(x, y);
        }
    }

    static class SavedState extends android.view.View.BaseSavedState {
        int progress;

        int secondaryProgress;

        /**
         * Constructor called from {@link ProgressBar#onSaveInstanceState()}
         */
        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(android.os.Parcel in) {
            super(in);
            progress = in.readInt();
            secondaryProgress = in.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
            out.writeInt(secondaryProgress);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.ProgressBar.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.ProgressBar.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.widget.ProgressBar.SavedState ss = new android.widget.ProgressBar.SavedState(superState);
        ss.progress = mProgress;
        ss.secondaryProgress = mSecondaryProgress;
        return ss;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.ProgressBar.SavedState ss = ((android.widget.ProgressBar.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
        setSecondaryProgress(ss.secondaryProgress);
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIndeterminate) {
            startAnimation();
        }
        if (mRefreshData != null) {
            synchronized(this) {
                final int count = mRefreshData.size();
                for (int i = 0; i < count; i++) {
                    final android.widget.ProgressBar.RefreshData rd = mRefreshData.get(i);
                    doRefreshProgress(rd.id, rd.progress, rd.fromUser, true, rd.animate);
                    rd.recycle();
                }
                mRefreshData.clear();
            }
        }
        mAttached = true;
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        if (mIndeterminate) {
            stopAnimation();
        }
        if (mRefreshProgressRunnable != null) {
            removeCallbacks(mRefreshProgressRunnable);
            mRefreshIsPosted = false;
        }
        if (mAccessibilityEventSender != null) {
            removeCallbacks(mAccessibilityEventSender);
        }
        // This should come after stopAnimation(), otherwise an invalidate message remains in the
        // queue, which can prevent the entire view hierarchy from being GC'ed during a rotation
        super.onDetachedFromWindow();
        mAttached = false;
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ProgressBar.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setItemCount(mMax - mMin);
        event.setCurrentItemIndex(mProgress);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (!isIndeterminate()) {
            android.view.accessibility.AccessibilityNodeInfo.RangeInfo rangeInfo = android.view.accessibility.AccessibilityNodeInfo.RangeInfo.obtain(android.view.accessibility.AccessibilityNodeInfo.RangeInfo.RANGE_TYPE_INT, getMin(), getMax(), getProgress());
            info.setRangeInfo(rangeInfo);
        }
    }

    /**
     * Schedule a command for sending an accessibility event.
     * </br>
     * Note: A command is used to ensure that accessibility events
     *       are sent at most one in a given time frame to save
     *       system resources while the progress changes quickly.
     */
    private void scheduleAccessibilityEventSender() {
        if (mAccessibilityEventSender == null) {
            mAccessibilityEventSender = new android.widget.ProgressBar.AccessibilityEventSender();
        } else {
            removeCallbacks(mAccessibilityEventSender);
        }
        postDelayed(mAccessibilityEventSender, android.widget.ProgressBar.TIMEOUT_SEND_ACCESSIBILITY_EVENT);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void encodeProperties(@android.annotation.NonNull
    android.view.ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("progress:max", getMax());
        stream.addProperty("progress:progress", getProgress());
        stream.addProperty("progress:secondaryProgress", getSecondaryProgress());
        stream.addProperty("progress:indeterminate", isIndeterminate());
    }

    /**
     * Returns whether the ProgressBar is animating or not. This is essentially the same
     * as whether the ProgressBar is {@link #isIndeterminate() indeterminate} and visible,
     * as indeterminate ProgressBars are always animating, and non-indeterminate
     * ProgressBars are not animating.
     *
     * @return true if the ProgressBar is animating, false otherwise.
     */
    public boolean isAnimating() {
        return (isIndeterminate() && (getWindowVisibility() == android.view.View.VISIBLE)) && isShown();
    }

    /**
     * Command for sending an accessibility event.
     */
    private class AccessibilityEventSender implements java.lang.Runnable {
        public void run() {
            sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED);
        }
    }

    private static class ProgressTintInfo {
        android.content.res.ColorStateList mIndeterminateTintList;

        android.graphics.BlendMode mIndeterminateBlendMode;

        boolean mHasIndeterminateTint;

        boolean mHasIndeterminateTintMode;

        android.content.res.ColorStateList mProgressTintList;

        android.graphics.BlendMode mProgressBlendMode;

        boolean mHasProgressTint;

        boolean mHasProgressTintMode;

        android.content.res.ColorStateList mProgressBackgroundTintList;

        android.graphics.BlendMode mProgressBackgroundBlendMode;

        boolean mHasProgressBackgroundTint;

        boolean mHasProgressBackgroundTintMode;

        android.content.res.ColorStateList mSecondaryProgressTintList;

        android.graphics.BlendMode mSecondaryProgressBlendMode;

        boolean mHasSecondaryProgressTint;

        boolean mHasSecondaryProgressTintMode;
    }

    /**
     * Property wrapper around the visual state of the {@code progress} functionality
     * handled by the {@link ProgressBar#setProgress(int, boolean)} method. This does
     * not correspond directly to the actual progress -- only the visual state.
     */
    private final android.util.FloatProperty<android.widget.ProgressBar> VISUAL_PROGRESS = new android.util.FloatProperty<android.widget.ProgressBar>("visual_progress") {
        @java.lang.Override
        public void setValue(android.widget.ProgressBar object, float value) {
            object.setVisualProgress(R.id.progress, value);
            object.mVisualProgress = value;
        }

        @java.lang.Override
        public java.lang.Float get(android.widget.ProgressBar object) {
            return object.mVisualProgress;
        }
    };
}

