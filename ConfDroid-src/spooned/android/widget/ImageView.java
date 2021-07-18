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
 * Displays image resources, for example {@link android.graphics.Bitmap}
 * or {@link android.graphics.drawable.Drawable} resources.
 * ImageView is also commonly used to {@link #setImageTintMode(PorterDuff.Mode)
 * apply tints to an image} and handle {@link #setScaleType(ScaleType) image scaling}.
 *
 * <p>
 * The following XML snippet is a common example of using an ImageView to display an image resource:
 * </p>
 * <pre>
 * &lt;LinearLayout
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"&gt;
 *     &lt;ImageView
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         android:src="@mipmap/ic_launcher"
 *         /&gt;
 * &lt;/LinearLayout&gt;
 * </pre>
 *
 * <p>
 * To learn more about Drawables, see: <a href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.
 * To learn more about working with Bitmaps, see: <a href="{@docRoot }topic/performance/graphics/index.html">Handling Bitmaps</a>.
 * </p>
 *
 * @unknown ref android.R.styleable#ImageView_adjustViewBounds
 * @unknown ref android.R.styleable#ImageView_src
 * @unknown ref android.R.styleable#ImageView_maxWidth
 * @unknown ref android.R.styleable#ImageView_maxHeight
 * @unknown ref android.R.styleable#ImageView_tint
 * @unknown ref android.R.styleable#ImageView_scaleType
 * @unknown ref android.R.styleable#ImageView_cropToPadding
 */
@android.widget.RemoteViews.RemoteView
public class ImageView extends android.view.View {
    private static final java.lang.String LOG_TAG = "ImageView";

    // settable by the client
    @android.annotation.UnsupportedAppUsage
    private android.net.Uri mUri;

    @android.annotation.UnsupportedAppUsage
    private int mResource = 0;

    private android.graphics.Matrix mMatrix;

    private android.widget.ImageView.ScaleType mScaleType;

    private boolean mHaveFrame = false;

    @android.annotation.UnsupportedAppUsage
    private boolean mAdjustViewBounds = false;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private int mMaxWidth = java.lang.Integer.MAX_VALUE;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private int mMaxHeight = java.lang.Integer.MAX_VALUE;

    // these are applied to the drawable
    private android.graphics.ColorFilter mColorFilter = null;

    private boolean mHasColorFilter = false;

    private android.graphics.Xfermode mXfermode;

    private boolean mHasXfermode = false;

    @android.annotation.UnsupportedAppUsage
    private int mAlpha = 255;

    private boolean mHasAlpha = false;

    private final int mViewAlphaScale = 256;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mDrawable = null;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.BitmapDrawable mRecycleableBitmapDrawable = null;

    private android.content.res.ColorStateList mDrawableTintList = null;

    private android.graphics.BlendMode mDrawableBlendMode = null;

    private boolean mHasDrawableTint = false;

    private boolean mHasDrawableBlendMode = false;

    private int[] mState = null;

    private boolean mMergeState = false;

    private int mLevel = 0;

    @android.annotation.UnsupportedAppUsage
    private int mDrawableWidth;

    @android.annotation.UnsupportedAppUsage
    private int mDrawableHeight;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 124051687)
    private android.graphics.Matrix mDrawMatrix = null;

    // Avoid allocations...
    private final android.graphics.RectF mTempSrc = new android.graphics.RectF();

    private final android.graphics.RectF mTempDst = new android.graphics.RectF();

    @android.annotation.UnsupportedAppUsage
    private boolean mCropToPadding;

    private int mBaseline = -1;

    private boolean mBaselineAlignBottom = false;

    /**
     * Compatibility modes dependent on targetSdkVersion of the app.
     */
    private static boolean sCompatDone;

    /**
     * AdjustViewBounds behavior will be in compatibility mode for older apps.
     */
    private static boolean sCompatAdjustViewBounds;

    /**
     * Whether to pass Resources when creating the source from a stream.
     */
    private static boolean sCompatUseCorrectStreamDensity;

    /**
     * Whether to use pre-Nougat drawable visibility dispatching conditions.
     */
    private static boolean sCompatDrawableVisibilityDispatch;

    private static final android.widget.ImageView.ScaleType[] sScaleTypeArray = new android.widget.ImageView.ScaleType[]{ android.widget.ImageView.ScaleType.MATRIX, android.widget.ImageView.ScaleType.FIT_XY, android.widget.ImageView.ScaleType.FIT_START, android.widget.ImageView.ScaleType.FIT_CENTER, android.widget.ImageView.ScaleType.FIT_END, android.widget.ImageView.ScaleType.CENTER, android.widget.ImageView.ScaleType.CENTER_CROP, android.widget.ImageView.ScaleType.CENTER_INSIDE };

    public ImageView(android.content.Context context) {
        super(context);
        initImageView();
    }

    public ImageView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImageView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initImageView();
        // ImageView is not important by default, unless app developer overrode attribute.
        if (getImportantForAutofill() == android.view.View.IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(android.view.View.IMPORTANT_FOR_AUTOFILL_NO);
        }
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ImageView, attrs, a, defStyleAttr, defStyleRes);
        final android.graphics.drawable.Drawable d = a.getDrawable(R.styleable.ImageView_src);
        if (d != null) {
            setImageDrawable(d);
        }
        mBaselineAlignBottom = a.getBoolean(R.styleable.ImageView_baselineAlignBottom, false);
        mBaseline = a.getDimensionPixelSize(R.styleable.ImageView_baseline, -1);
        setAdjustViewBounds(a.getBoolean(R.styleable.ImageView_adjustViewBounds, false));
        setMaxWidth(a.getDimensionPixelSize(R.styleable.ImageView_maxWidth, java.lang.Integer.MAX_VALUE));
        setMaxHeight(a.getDimensionPixelSize(R.styleable.ImageView_maxHeight, java.lang.Integer.MAX_VALUE));
        final int index = a.getInt(R.styleable.ImageView_scaleType, -1);
        if (index >= 0) {
            setScaleType(android.widget.ImageView.sScaleTypeArray[index]);
        }
        if (a.hasValue(R.styleable.ImageView_tint)) {
            mDrawableTintList = a.getColorStateList(R.styleable.ImageView_tint);
            mHasDrawableTint = true;
            // Prior to L, this attribute would always set a color filter with
            // blending mode SRC_ATOP. Preserve that default behavior.
            mDrawableBlendMode = android.graphics.BlendMode.SRC_ATOP;
            mHasDrawableBlendMode = true;
        }
        if (a.hasValue(R.styleable.ImageView_tintMode)) {
            mDrawableBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.ImageView_tintMode, -1), mDrawableBlendMode);
            mHasDrawableBlendMode = true;
        }
        applyImageTint();
        final int alpha = a.getInt(R.styleable.ImageView_drawableAlpha, 255);
        if (alpha != 255) {
            setImageAlpha(alpha);
        }
        mCropToPadding = a.getBoolean(R.styleable.ImageView_cropToPadding, false);
        a.recycle();
        // need inflate syntax/reader for matrix
    }

    private void initImageView() {
        mMatrix = new android.graphics.Matrix();
        mScaleType = android.widget.ImageView.ScaleType.FIT_CENTER;
        if (!android.widget.ImageView.sCompatDone) {
            final int targetSdkVersion = mContext.getApplicationInfo().targetSdkVersion;
            android.widget.ImageView.sCompatAdjustViewBounds = targetSdkVersion <= Build.VERSION_CODES.JELLY_BEAN_MR1;
            android.widget.ImageView.sCompatUseCorrectStreamDensity = targetSdkVersion > Build.VERSION_CODES.M;
            android.widget.ImageView.sCompatDrawableVisibilityDispatch = targetSdkVersion < Build.VERSION_CODES.N;
            android.widget.ImageView.sCompatDone = true;
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable dr) {
        return (mDrawable == dr) || super.verifyDrawable(dr);
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mDrawable != null)
            mDrawable.jumpToCurrentState();

    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable dr) {
        if (dr == mDrawable) {
            if (dr != null) {
                // update cached drawable dimensions if they've changed
                final int w = dr.getIntrinsicWidth();
                final int h = dr.getIntrinsicHeight();
                if ((w != mDrawableWidth) || (h != mDrawableHeight)) {
                    mDrawableWidth = w;
                    mDrawableHeight = h;
                    // updates the matrix, which is dependent on the bounds
                    configureBounds();
                }
            }
            /* we invalidate the whole view in this case because it's very
            hard to know where the drawable actually is. This is made
            complicated because of the offsets and transformations that
            can be applied. In theory we could get the drawable's bounds
            and run them through the transformation and offsets, but this
            is probably not worth the effort.
             */
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @java.lang.Override
    public boolean hasOverlappingRendering() {
        return (getBackground() != null) && (getBackground().getCurrent() != null);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onPopulateAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        final java.lang.CharSequence contentDescription = getContentDescription();
        if (!android.text.TextUtils.isEmpty(contentDescription)) {
            event.getText().add(contentDescription);
        }
    }

    /**
     * True when ImageView is adjusting its bounds
     * to preserve the aspect ratio of its drawable
     *
     * @return whether to adjust the bounds of this view
    to preserve the original aspect ratio of the drawable
     * @see #setAdjustViewBounds(boolean)
     * @unknown ref android.R.styleable#ImageView_adjustViewBounds
     */
    @android.view.inspector.InspectableProperty
    public boolean getAdjustViewBounds() {
        return mAdjustViewBounds;
    }

    /**
     * Set this to true if you want the ImageView to adjust its bounds
     * to preserve the aspect ratio of its drawable.
     *
     * <p><strong>Note:</strong> If the application targets API level 17 or lower,
     * adjustViewBounds will allow the drawable to shrink the view bounds, but not grow
     * to fill available measured space in all cases. This is for compatibility with
     * legacy {@link android.view.View.MeasureSpec MeasureSpec} and
     * {@link android.widget.RelativeLayout RelativeLayout} behavior.</p>
     *
     * @param adjustViewBounds
     * 		Whether to adjust the bounds of this view
     * 		to preserve the original aspect ratio of the drawable.
     * @see #getAdjustViewBounds()
     * @unknown ref android.R.styleable#ImageView_adjustViewBounds
     */
    @android.view.RemotableViewMethod
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        mAdjustViewBounds = adjustViewBounds;
        if (adjustViewBounds) {
            setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
        }
    }

    /**
     * The maximum width of this view.
     *
     * @return The maximum width of this view
     * @see #setMaxWidth(int)
     * @unknown ref android.R.styleable#ImageView_maxWidth
     */
    @android.view.inspector.InspectableProperty
    public int getMaxWidth() {
        return mMaxWidth;
    }

    /**
     * An optional argument to supply a maximum width for this view. Only valid if
     * {@link #setAdjustViewBounds(boolean)} has been set to true. To set an image to be a maximum
     * of 100 x 100 while preserving the original aspect ratio, do the following: 1) set
     * adjustViewBounds to true 2) set maxWidth and maxHeight to 100 3) set the height and width
     * layout params to WRAP_CONTENT.
     *
     * <p>
     * Note that this view could be still smaller than 100 x 100 using this approach if the original
     * image is small. To set an image to a fixed size, specify that size in the layout params and
     * then use {@link #setScaleType(android.widget.ImageView.ScaleType)} to determine how to fit
     * the image within the bounds.
     * </p>
     *
     * @param maxWidth
     * 		maximum width for this view
     * @see #getMaxWidth()
     * @unknown ref android.R.styleable#ImageView_maxWidth
     */
    @android.view.RemotableViewMethod
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
    }

    /**
     * The maximum height of this view.
     *
     * @return The maximum height of this view
     * @see #setMaxHeight(int)
     * @unknown ref android.R.styleable#ImageView_maxHeight
     */
    @android.view.inspector.InspectableProperty
    public int getMaxHeight() {
        return mMaxHeight;
    }

    /**
     * An optional argument to supply a maximum height for this view. Only valid if
     * {@link #setAdjustViewBounds(boolean)} has been set to true. To set an image to be a
     * maximum of 100 x 100 while preserving the original aspect ratio, do the following: 1) set
     * adjustViewBounds to true 2) set maxWidth and maxHeight to 100 3) set the height and width
     * layout params to WRAP_CONTENT.
     *
     * <p>
     * Note that this view could be still smaller than 100 x 100 using this approach if the original
     * image is small. To set an image to a fixed size, specify that size in the layout params and
     * then use {@link #setScaleType(android.widget.ImageView.ScaleType)} to determine how to fit
     * the image within the bounds.
     * </p>
     *
     * @param maxHeight
     * 		maximum height for this view
     * @see #getMaxHeight()
     * @unknown ref android.R.styleable#ImageView_maxHeight
     */
    @android.view.RemotableViewMethod
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
    }

    /**
     * Gets the current Drawable, or null if no Drawable has been
     * assigned.
     *
     * @return the view's drawable, or null if no drawable has been
    assigned.
     */
    @android.view.inspector.InspectableProperty(name = "src")
    public android.graphics.drawable.Drawable getDrawable() {
        if (mDrawable == mRecycleableBitmapDrawable) {
            // Consider our cached version dirty since app code now has a reference to it
            mRecycleableBitmapDrawable = null;
        }
        return mDrawable;
    }

    private class ImageDrawableCallback implements java.lang.Runnable {
        private final android.graphics.drawable.Drawable drawable;

        private final android.net.Uri uri;

        private final int resource;

        ImageDrawableCallback(android.graphics.drawable.Drawable drawable, android.net.Uri uri, int resource) {
            this.drawable = drawable;
            this.uri = uri;
            this.resource = resource;
        }

        @java.lang.Override
        public void run() {
            setImageDrawable(drawable);
            mUri = uri;
            mResource = resource;
        }
    }

    /**
     * Sets a drawable as the content of this ImageView.
     * <p class="note">This does Bitmap reading and decoding on the UI
     * thread, which can cause a latency hiccup.  If that's a concern,
     * consider using {@link #setImageDrawable(android.graphics.drawable.Drawable)} or
     * {@link #setImageBitmap(android.graphics.Bitmap)} and
     * {@link android.graphics.BitmapFactory} instead.</p>
     *
     * @param resId
     * 		the resource identifier of the drawable
     * @unknown ref android.R.styleable#ImageView_src
     */
    @android.view.RemotableViewMethod(asyncImpl = "setImageResourceAsync")
    public void setImageResource(@android.annotation.DrawableRes
    int resId) {
        // The resource configuration may have changed, so we should always
        // try to load the resource even if the resId hasn't changed.
        final int oldWidth = mDrawableWidth;
        final int oldHeight = mDrawableHeight;
        updateDrawable(null);
        mResource = resId;
        mUri = null;
        resolveUri();
        if ((oldWidth != mDrawableWidth) || (oldHeight != mDrawableHeight)) {
            requestLayout();
        }
        invalidate();
    }

    /**
     *
     *
     * @unknown *
     */
    @android.annotation.UnsupportedAppUsage
    public java.lang.Runnable setImageResourceAsync(@android.annotation.DrawableRes
    int resId) {
        android.graphics.drawable.Drawable d = null;
        if (resId != 0) {
            try {
                d = getContext().getDrawable(resId);
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.widget.ImageView.LOG_TAG, "Unable to find resource: " + resId, e);
                resId = 0;
            }
        }
        return new android.widget.ImageView.ImageDrawableCallback(d, null, resId);
    }

    /**
     * Sets the content of this ImageView to the specified Uri.
     * Note that you use this method to load images from a local Uri only.
     * <p/>
     * To learn how to display images from a remote Uri see: <a href="https://developer.android.com/topic/performance/graphics/index.html">Handling Bitmaps</a>
     * <p/>
     * <p class="note">This does Bitmap reading and decoding on the UI
     * thread, which can cause a latency hiccup.  If that's a concern,
     * consider using {@link #setImageDrawable(Drawable)} or
     * {@link #setImageBitmap(android.graphics.Bitmap)} and
     * {@link android.graphics.BitmapFactory} instead.</p>
     *
     * <p class="note">On devices running SDK < 24, this method will fail to
     * apply correct density scaling to images loaded from
     * {@link ContentResolver#SCHEME_CONTENT content} and
     * {@link ContentResolver#SCHEME_FILE file} schemes. Applications running
     * on devices with SDK >= 24 <strong>MUST</strong> specify the
     * {@code targetSdkVersion} in their manifest as 24 or above for density
     * scaling to be applied to images loaded from these schemes.</p>
     *
     * @param uri
     * 		the Uri of an image, or {@code null} to clear the content
     */
    @android.view.RemotableViewMethod(asyncImpl = "setImageURIAsync")
    public void setImageURI(@android.annotation.Nullable
    android.net.Uri uri) {
        if ((mResource != 0) || ((mUri != uri) && (((uri == null) || (mUri == null)) || (!uri.equals(mUri))))) {
            updateDrawable(null);
            mResource = 0;
            mUri = uri;
            final int oldWidth = mDrawableWidth;
            final int oldHeight = mDrawableHeight;
            resolveUri();
            if ((oldWidth != mDrawableWidth) || (oldHeight != mDrawableHeight)) {
                requestLayout();
            }
            invalidate();
        }
    }

    /**
     *
     *
     * @unknown *
     */
    @android.annotation.UnsupportedAppUsage
    public java.lang.Runnable setImageURIAsync(@android.annotation.Nullable
    android.net.Uri uri) {
        if ((mResource != 0) || ((mUri != uri) && (((uri == null) || (mUri == null)) || (!uri.equals(mUri))))) {
            android.graphics.drawable.Drawable d = (uri == null) ? null : getDrawableFromUri(uri);
            if (d == null) {
                // Do not set the URI if the drawable couldn't be loaded.
                uri = null;
            }
            return new android.widget.ImageView.ImageDrawableCallback(d, uri, 0);
        }
        return null;
    }

    /**
     * Sets a drawable as the content of this ImageView.
     *
     * @param drawable
     * 		the Drawable to set, or {@code null} to clear the
     * 		content
     */
    public void setImageDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable drawable) {
        if (mDrawable != drawable) {
            mResource = 0;
            mUri = null;
            final int oldWidth = mDrawableWidth;
            final int oldHeight = mDrawableHeight;
            updateDrawable(drawable);
            if ((oldWidth != mDrawableWidth) || (oldHeight != mDrawableHeight)) {
                requestLayout();
            }
            invalidate();
        }
    }

    /**
     * Sets the content of this ImageView to the specified Icon.
     *
     * <p class="note">Depending on the Icon type, this may do Bitmap reading
     * and decoding on the UI thread, which can cause UI jank.  If that's a
     * concern, consider using
     * {@link Icon#loadDrawableAsync(Context, Icon.OnDrawableLoadedListener, Handler)}
     * and then {@link #setImageDrawable(android.graphics.drawable.Drawable)}
     * instead.</p>
     *
     * @param icon
     * 		an Icon holding the desired image, or {@code null} to clear
     * 		the content
     */
    @android.view.RemotableViewMethod(asyncImpl = "setImageIconAsync")
    public void setImageIcon(@android.annotation.Nullable
    android.graphics.drawable.Icon icon) {
        setImageDrawable(icon == null ? null : icon.loadDrawable(mContext));
    }

    /**
     *
     *
     * @unknown *
     */
    public java.lang.Runnable setImageIconAsync(@android.annotation.Nullable
    android.graphics.drawable.Icon icon) {
        return new android.widget.ImageView.ImageDrawableCallback(icon == null ? null : icon.loadDrawable(mContext), null, 0);
    }

    /**
     * Applies a tint to the image drawable. Does not modify the current tint
     * mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setImageDrawable(Drawable)} will automatically
     * mutate the drawable and apply the specified tint and tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     * <p>
     * <em>Note:</em> The default tint mode used by this setter is NOT
     * consistent with the default tint mode used by the
     * {@link android.R.styleable#ImageView_tint android:tint}
     * attribute. If the {@code android:tint} attribute is specified, the
     * default tint mode will be set to {@link PorterDuff.Mode#SRC_ATOP} to
     * ensure consistency with earlier versions of the platform.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#ImageView_tint
     * @see #getImageTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setImageTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mDrawableTintList = tint;
        mHasDrawableTint = true;
        applyImageTint();
    }

    /**
     * Get the current {@link android.content.res.ColorStateList} used to tint the image Drawable,
     * or null if no tint is applied.
     *
     * @return the tint applied to the image drawable
     * @unknown ref android.R.styleable#ImageView_tint
     * @see #setImageTintList(ColorStateList)
     */
    @android.annotation.Nullable
    @android.view.inspector.InspectableProperty(name = "tint")
    public android.content.res.ColorStateList getImageTintList() {
        return mDrawableTintList;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setImageTintList(ColorStateList)}} to the image drawable. The default
     * mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ImageView_tintMode
     * @see #getImageTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setImageTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setImageTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setImageTintList(ColorStateList)}} to the image drawable. The default
     * mode is {@link BlendMode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#ImageView_tintMode
     * @see #getImageTintMode()
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setImageTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        mDrawableBlendMode = blendMode;
        mHasDrawableBlendMode = true;
        applyImageTint();
    }

    /**
     * Gets the blending mode used to apply the tint to the image Drawable
     *
     * @return the blending mode used to apply the tint to the image Drawable
     * @unknown ref android.R.styleable#ImageView_tintMode
     * @see #setImageTintMode(PorterDuff.Mode)
     */
    @android.annotation.Nullable
    @android.view.inspector.InspectableProperty(name = "tintMode")
    public android.graphics.PorterDuff.Mode getImageTintMode() {
        return mDrawableBlendMode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mDrawableBlendMode) : null;
    }

    /**
     * Gets the blending mode used to apply the tint to the image Drawable
     *
     * @return the blending mode used to apply the tint to the image Drawable
     * @unknown ref android.R.styleable#ImageView_tintMode
     * @see #setImageTintBlendMode(BlendMode)
     */
    @android.annotation.Nullable
    @android.view.inspector.InspectableProperty(name = "blendMode", attributeId = android.R.styleable.ImageView_tintMode)
    public android.graphics.BlendMode getImageTintBlendMode() {
        return mDrawableBlendMode;
    }

    private void applyImageTint() {
        if ((mDrawable != null) && (mHasDrawableTint || mHasDrawableBlendMode)) {
            mDrawable = mDrawable.mutate();
            if (mHasDrawableTint) {
                mDrawable.setTintList(mDrawableTintList);
            }
            if (mHasDrawableBlendMode) {
                mDrawable.setTintBlendMode(mDrawableBlendMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mDrawable.isStateful()) {
                mDrawable.setState(getDrawableState());
            }
        }
    }

    /**
     * Sets a Bitmap as the content of this ImageView.
     *
     * @param bm
     * 		The bitmap to set
     */
    @android.view.RemotableViewMethod
    public void setImageBitmap(android.graphics.Bitmap bm) {
        // Hacky fix to force setImageDrawable to do a full setImageDrawable
        // instead of doing an object reference comparison
        mDrawable = null;
        if (mRecycleableBitmapDrawable == null) {
            mRecycleableBitmapDrawable = new android.graphics.drawable.BitmapDrawable(mContext.getResources(), bm);
        } else {
            mRecycleableBitmapDrawable.setBitmap(bm);
        }
        setImageDrawable(mRecycleableBitmapDrawable);
    }

    /**
     * Set the state of the current {@link android.graphics.drawable.StateListDrawable}.
     * For more information about State List Drawables, see: <a href="https://developer.android.com/guide/topics/resources/drawable-resource.html#StateList">the Drawable Resource Guide</a>.
     *
     * @param state
     * 		the state to set for the StateListDrawable
     * @param merge
     * 		if true, merges the state values for the state you specify into the current state
     */
    public void setImageState(int[] state, boolean merge) {
        mState = state;
        mMergeState = merge;
        if (mDrawable != null) {
            refreshDrawableState();
            resizeFromDrawable();
        }
    }

    @java.lang.Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        resizeFromDrawable();
    }

    /**
     * Sets the image level, when it is constructed from a
     * {@link android.graphics.drawable.LevelListDrawable}.
     *
     * @param level
     * 		The new level for the image.
     */
    @android.view.RemotableViewMethod
    public void setImageLevel(int level) {
        mLevel = level;
        if (mDrawable != null) {
            mDrawable.setLevel(level);
            resizeFromDrawable();
        }
    }

    /**
     * Options for scaling the bounds of an image to the bounds of this view.
     */
    public enum ScaleType {

        /**
         * Scale using the image matrix when drawing. The image matrix can be set using
         * {@link ImageView#setImageMatrix(Matrix)}. From XML, use this syntax:
         * <code>android:scaleType="matrix"</code>.
         */
        MATRIX(0),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#FILL}.
         * From XML, use this syntax: <code>android:scaleType="fitXY"</code>.
         */
        FIT_XY(1),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#START}.
         * From XML, use this syntax: <code>android:scaleType="fitStart"</code>.
         */
        FIT_START(2),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#CENTER}.
         * From XML, use this syntax:
         * <code>android:scaleType="fitCenter"</code>.
         */
        FIT_CENTER(3),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#END}.
         * From XML, use this syntax: <code>android:scaleType="fitEnd"</code>.
         */
        FIT_END(4),
        /**
         * Center the image in the view, but perform no scaling.
         * From XML, use this syntax: <code>android:scaleType="center"</code>.
         */
        CENTER(5),
        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so
         * that both dimensions (width and height) of the image will be equal
         * to or larger than the corresponding dimension of the view
         * (minus padding). The image is then centered in the view.
         * From XML, use this syntax: <code>android:scaleType="centerCrop"</code>.
         */
        CENTER_CROP(6),
        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so
         * that both dimensions (width and height) of the image will be equal
         * to or less than the corresponding dimension of the view
         * (minus padding). The image is then centered in the view.
         * From XML, use this syntax: <code>android:scaleType="centerInside"</code>.
         */
        CENTER_INSIDE(7);
        ScaleType(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }

    /**
     * Controls how the image should be resized or moved to match the size
     * of this ImageView.
     *
     * @param scaleType
     * 		The desired scaling mode.
     * @unknown ref android.R.styleable#ImageView_scaleType
     */
    public void setScaleType(android.widget.ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            throw new java.lang.NullPointerException();
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            requestLayout();
            invalidate();
        }
    }

    /**
     * Returns the current ScaleType that is used to scale the bounds of an image to the bounds of the ImageView.
     *
     * @return The ScaleType used to scale the image.
     * @see ImageView.ScaleType
     * @unknown ref android.R.styleable#ImageView_scaleType
     */
    @android.view.inspector.InspectableProperty
    public android.widget.ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * Returns the view's optional matrix. This is applied to the
     * view's drawable when it is drawn. If there is no matrix,
     * this method will return an identity matrix.
     * Do not change this matrix in place but make a copy.
     * If you want a different matrix applied to the drawable,
     * be sure to call setImageMatrix().
     */
    public android.graphics.Matrix getImageMatrix() {
        if (mDrawMatrix == null) {
            return new android.graphics.Matrix(android.graphics.Matrix.IDENTITY_MATRIX);
        }
        return mDrawMatrix;
    }

    /**
     * Adds a transformation {@link Matrix} that is applied
     * to the view's drawable when it is drawn.  Allows custom scaling,
     * translation, and perspective distortion.
     *
     * @param matrix
     * 		The transformation parameters in matrix form.
     */
    public void setImageMatrix(android.graphics.Matrix matrix) {
        // collapse null and identity to just null
        if ((matrix != null) && matrix.isIdentity()) {
            matrix = null;
        }
        // don't invalidate unless we're actually changing our matrix
        if (((matrix == null) && (!mMatrix.isIdentity())) || ((matrix != null) && (!mMatrix.equals(matrix)))) {
            mMatrix.set(matrix);
            configureBounds();
            invalidate();
        }
    }

    /**
     * Return whether this ImageView crops to padding.
     *
     * @return whether this ImageView crops to padding
     * @see #setCropToPadding(boolean)
     * @unknown ref android.R.styleable#ImageView_cropToPadding
     */
    @android.view.inspector.InspectableProperty
    public boolean getCropToPadding() {
        return mCropToPadding;
    }

    /**
     * Sets whether this ImageView will crop to padding.
     *
     * @param cropToPadding
     * 		whether this ImageView will crop to padding
     * @see #getCropToPadding()
     * @unknown ref android.R.styleable#ImageView_cropToPadding
     */
    public void setCropToPadding(boolean cropToPadding) {
        if (mCropToPadding != cropToPadding) {
            mCropToPadding = cropToPadding;
            requestLayout();
            invalidate();
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void resolveUri() {
        if (mDrawable != null) {
            return;
        }
        if (getResources() == null) {
            return;
        }
        android.graphics.drawable.Drawable d = null;
        if (mResource != 0) {
            try {
                d = mContext.getDrawable(mResource);
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.widget.ImageView.LOG_TAG, "Unable to find resource: " + mResource, e);
                // Don't try again.
                mResource = 0;
            }
        } else
            if (mUri != null) {
                d = getDrawableFromUri(mUri);
                if (d == null) {
                    android.util.Log.w(android.widget.ImageView.LOG_TAG, "resolveUri failed on bad bitmap uri: " + mUri);
                    // Don't try again.
                    mUri = null;
                }
            } else {
                return;
            }

        updateDrawable(d);
    }

    private android.graphics.drawable.Drawable getDrawableFromUri(android.net.Uri uri) {
        final java.lang.String scheme = uri.getScheme();
        if (android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            try {
                // Load drawable through Resources, to get the source density information
                android.content.ContentResolver.OpenResourceIdResult r = mContext.getContentResolver().getResourceId(uri);
                return r.r.getDrawable(r.id, mContext.getTheme());
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.widget.ImageView.LOG_TAG, "Unable to open content: " + uri, e);
            }
        } else
            if (android.content.ContentResolver.SCHEME_CONTENT.equals(scheme) || android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                try {
                    android.content.res.Resources res = (android.widget.ImageView.sCompatUseCorrectStreamDensity) ? getResources() : null;
                    android.graphics.ImageDecoder.Source src = android.graphics.ImageDecoder.createSource(mContext.getContentResolver(), uri, res);
                    return android.graphics.ImageDecoder.decodeDrawable(src, ( decoder, info, s) -> {
                        decoder.setAllocator(android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
                    });
                } catch (java.io.IOException e) {
                    android.util.Log.w(android.widget.ImageView.LOG_TAG, "Unable to open content: " + uri, e);
                }
            } else {
                return android.graphics.drawable.Drawable.createFromPath(uri.toString());
            }

        return null;
    }

    @java.lang.Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (mState == null) {
            return super.onCreateDrawableState(extraSpace);
        } else
            if (!mMergeState) {
                return mState;
            } else {
                return android.view.View.mergeDrawableStates(super.onCreateDrawableState(extraSpace + mState.length), mState);
            }

    }

    @android.annotation.UnsupportedAppUsage
    private void updateDrawable(android.graphics.drawable.Drawable d) {
        if ((d != mRecycleableBitmapDrawable) && (mRecycleableBitmapDrawable != null)) {
            mRecycleableBitmapDrawable.setBitmap(null);
        }
        boolean sameDrawable = false;
        if (mDrawable != null) {
            sameDrawable = mDrawable == d;
            mDrawable.setCallback(null);
            unscheduleDrawable(mDrawable);
            if (((!android.widget.ImageView.sCompatDrawableVisibilityDispatch) && (!sameDrawable)) && isAttachedToWindow()) {
                mDrawable.setVisible(false, false);
            }
        }
        mDrawable = d;
        if (d != null) {
            d.setCallback(this);
            d.setLayoutDirection(getLayoutDirection());
            if (d.isStateful()) {
                d.setState(getDrawableState());
            }
            if ((!sameDrawable) || android.widget.ImageView.sCompatDrawableVisibilityDispatch) {
                final boolean visible = (android.widget.ImageView.sCompatDrawableVisibilityDispatch) ? getVisibility() == android.view.View.VISIBLE : (isAttachedToWindow() && (getWindowVisibility() == android.view.View.VISIBLE)) && isShown();
                d.setVisible(visible, true);
            }
            d.setLevel(mLevel);
            mDrawableWidth = d.getIntrinsicWidth();
            mDrawableHeight = d.getIntrinsicHeight();
            applyImageTint();
            applyColorFilter();
            applyAlpha();
            applyXfermode();
            configureBounds();
        } else {
            mDrawableWidth = mDrawableHeight = -1;
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void resizeFromDrawable() {
        final android.graphics.drawable.Drawable d = mDrawable;
        if (d != null) {
            int w = d.getIntrinsicWidth();
            if (w < 0)
                w = mDrawableWidth;

            int h = d.getIntrinsicHeight();
            if (h < 0)
                h = mDrawableHeight;

            if ((w != mDrawableWidth) || (h != mDrawableHeight)) {
                mDrawableWidth = w;
                mDrawableHeight = h;
                requestLayout();
            }
        }
    }

    @java.lang.Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (mDrawable != null) {
            mDrawable.setLayoutDirection(layoutDirection);
        }
    }

    private static final android.graphics.Matrix.ScaleToFit[] sS2FArray = new android.graphics.Matrix.ScaleToFit[]{ android.graphics.Matrix.ScaleToFit.FILL, android.graphics.Matrix.ScaleToFit.START, android.graphics.Matrix.ScaleToFit.CENTER, android.graphics.Matrix.ScaleToFit.END };

    @android.annotation.UnsupportedAppUsage
    private static android.graphics.Matrix.ScaleToFit scaleTypeToScaleToFit(android.widget.ImageView.ScaleType st) {
        // ScaleToFit enum to their corresponding Matrix.ScaleToFit values
        return android.widget.ImageView.sS2FArray[st.nativeInt - 1];
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        resolveUri();
        int w;
        int h;
        // Desired aspect ratio of the view's contents (not including padding)
        float desiredAspect = 0.0F;
        // We are allowed to change the view's width
        boolean resizeWidth = false;
        // We are allowed to change the view's height
        boolean resizeHeight = false;
        final int widthSpecMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        if (mDrawable == null) {
            // If no drawable, its intrinsic size is 0.
            mDrawableWidth = -1;
            mDrawableHeight = -1;
            w = h = 0;
        } else {
            w = mDrawableWidth;
            h = mDrawableHeight;
            if (w <= 0)
                w = 1;

            if (h <= 0)
                h = 1;

            // We are supposed to adjust view bounds to match the aspect
            // ratio of our drawable. See if that is possible.
            if (mAdjustViewBounds) {
                resizeWidth = widthSpecMode != android.view.View.MeasureSpec.EXACTLY;
                resizeHeight = heightSpecMode != android.view.View.MeasureSpec.EXACTLY;
                desiredAspect = ((float) (w)) / ((float) (h));
            }
        }
        final int pleft = mPaddingLeft;
        final int pright = mPaddingRight;
        final int ptop = mPaddingTop;
        final int pbottom = mPaddingBottom;
        int widthSize;
        int heightSize;
        if (resizeWidth || resizeHeight) {
            /* If we get here, it means we want to resize to match the
            drawables aspect ratio, and we have the freedom to change at
            least one dimension.
             */
            // Get the max possible width given our constraints
            widthSize = resolveAdjustedSize((w + pleft) + pright, mMaxWidth, widthMeasureSpec);
            // Get the max possible height given our constraints
            heightSize = resolveAdjustedSize((h + ptop) + pbottom, mMaxHeight, heightMeasureSpec);
            if (desiredAspect != 0.0F) {
                // See what our actual aspect ratio is
                final float actualAspect = ((float) ((widthSize - pleft) - pright)) / ((heightSize - ptop) - pbottom);
                if (java.lang.Math.abs(actualAspect - desiredAspect) > 1.0E-7) {
                    boolean done = false;
                    // Try adjusting width to be proportional to height
                    if (resizeWidth) {
                        int newWidth = (((int) (desiredAspect * ((heightSize - ptop) - pbottom))) + pleft) + pright;
                        // Allow the width to outgrow its original estimate if height is fixed.
                        if ((!resizeHeight) && (!android.widget.ImageView.sCompatAdjustViewBounds)) {
                            widthSize = resolveAdjustedSize(newWidth, mMaxWidth, widthMeasureSpec);
                        }
                        if (newWidth <= widthSize) {
                            widthSize = newWidth;
                            done = true;
                        }
                    }
                    // Try adjusting height to be proportional to width
                    if ((!done) && resizeHeight) {
                        int newHeight = (((int) (((widthSize - pleft) - pright) / desiredAspect)) + ptop) + pbottom;
                        // Allow the height to outgrow its original estimate if width is fixed.
                        if ((!resizeWidth) && (!android.widget.ImageView.sCompatAdjustViewBounds)) {
                            heightSize = resolveAdjustedSize(newHeight, mMaxHeight, heightMeasureSpec);
                        }
                        if (newHeight <= heightSize) {
                            heightSize = newHeight;
                        }
                    }
                }
            }
        } else {
            /* We are either don't want to preserve the drawables aspect ratio,
            or we are not allowed to change view dimensions. Just measure in
            the normal way.
             */
            w += pleft + pright;
            h += ptop + pbottom;
            w = java.lang.Math.max(w, getSuggestedMinimumWidth());
            h = java.lang.Math.max(h, getSuggestedMinimumHeight());
            widthSize = android.view.View.resolveSizeAndState(w, widthMeasureSpec, 0);
            heightSize = android.view.View.resolveSizeAndState(h, heightMeasureSpec, 0);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec) {
        int result = desiredSize;
        final int specMode = android.view.View.MeasureSpec.getMode(measureSpec);
        final int specSize = android.view.View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case android.view.View.MeasureSpec.UNSPECIFIED :
                /* Parent says we can be as big as we want. Just don't be larger
                than max size imposed on ourselves.
                 */
                result = java.lang.Math.min(desiredSize, maxSize);
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = java.lang.Math.min(java.lang.Math.min(desiredSize, specSize), maxSize);
                break;
            case android.view.View.MeasureSpec.EXACTLY :
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @java.lang.Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean changed = super.setFrame(l, t, r, b);
        mHaveFrame = true;
        configureBounds();
        return changed;
    }

    private void configureBounds() {
        if ((mDrawable == null) || (!mHaveFrame)) {
            return;
        }
        final int dwidth = mDrawableWidth;
        final int dheight = mDrawableHeight;
        final int vwidth = (getWidth() - mPaddingLeft) - mPaddingRight;
        final int vheight = (getHeight() - mPaddingTop) - mPaddingBottom;
        final boolean fits = ((dwidth < 0) || (vwidth == dwidth)) && ((dheight < 0) || (vheight == dheight));
        if (((dwidth <= 0) || (dheight <= 0)) || (android.widget.ImageView.ScaleType.FIT_XY == mScaleType)) {
            /* If the drawable has no intrinsic size, or we're told to
            scaletofit, then we just fill our entire view.
             */
            mDrawable.setBounds(0, 0, vwidth, vheight);
            mDrawMatrix = null;
        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            mDrawable.setBounds(0, 0, dwidth, dheight);
            if (android.widget.ImageView.ScaleType.MATRIX == mScaleType) {
                // Use the specified matrix as-is.
                if (mMatrix.isIdentity()) {
                    mDrawMatrix = null;
                } else {
                    mDrawMatrix = mMatrix;
                }
            } else
                if (fits) {
                    // The bitmap fits exactly, no transform needed.
                    mDrawMatrix = null;
                } else
                    if (android.widget.ImageView.ScaleType.CENTER == mScaleType) {
                        // Center bitmap in view, no scaling.
                        mDrawMatrix = mMatrix;
                        mDrawMatrix.setTranslate(java.lang.Math.round((vwidth - dwidth) * 0.5F), java.lang.Math.round((vheight - dheight) * 0.5F));
                    } else
                        if (android.widget.ImageView.ScaleType.CENTER_CROP == mScaleType) {
                            mDrawMatrix = mMatrix;
                            float scale;
                            float dx = 0;
                            float dy = 0;
                            if ((dwidth * vheight) > (vwidth * dheight)) {
                                scale = ((float) (vheight)) / ((float) (dheight));
                                dx = (vwidth - (dwidth * scale)) * 0.5F;
                            } else {
                                scale = ((float) (vwidth)) / ((float) (dwidth));
                                dy = (vheight - (dheight * scale)) * 0.5F;
                            }
                            mDrawMatrix.setScale(scale, scale);
                            mDrawMatrix.postTranslate(java.lang.Math.round(dx), java.lang.Math.round(dy));
                        } else
                            if (android.widget.ImageView.ScaleType.CENTER_INSIDE == mScaleType) {
                                mDrawMatrix = mMatrix;
                                float scale;
                                float dx;
                                float dy;
                                if ((dwidth <= vwidth) && (dheight <= vheight)) {
                                    scale = 1.0F;
                                } else {
                                    scale = java.lang.Math.min(((float) (vwidth)) / ((float) (dwidth)), ((float) (vheight)) / ((float) (dheight)));
                                }
                                dx = java.lang.Math.round((vwidth - (dwidth * scale)) * 0.5F);
                                dy = java.lang.Math.round((vheight - (dheight * scale)) * 0.5F);
                                mDrawMatrix.setScale(scale, scale);
                                mDrawMatrix.postTranslate(dx, dy);
                            } else {
                                // Generate the required transform.
                                mTempSrc.set(0, 0, dwidth, dheight);
                                mTempDst.set(0, 0, vwidth, vheight);
                                mDrawMatrix = mMatrix;
                                mDrawMatrix.setRectToRect(mTempSrc, mTempDst, android.widget.ImageView.scaleTypeToScaleToFit(mScaleType));
                            }




        }
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final android.graphics.drawable.Drawable drawable = mDrawable;
        if (((drawable != null) && drawable.isStateful()) && drawable.setState(getDrawableState())) {
            invalidateDrawable(drawable);
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mDrawable != null) {
            mDrawable.setHotspot(x, y);
        }
    }

    /**
     * Applies a temporary transformation {@link Matrix} to the view's drawable when it is drawn.
     * Allows custom scaling, translation, and perspective distortion during an animation.
     *
     * This method is a lightweight analogue of {@link ImageView#setImageMatrix(Matrix)} to use
     * only during animations as this matrix will be cleared after the next drawable
     * update or view's bounds change.
     *
     * @param matrix
     * 		The transformation parameters in matrix form.
     */
    public void animateTransform(@android.annotation.Nullable
    android.graphics.Matrix matrix) {
        if (mDrawable == null) {
            return;
        }
        if (matrix == null) {
            final int vwidth = (getWidth() - mPaddingLeft) - mPaddingRight;
            final int vheight = (getHeight() - mPaddingTop) - mPaddingBottom;
            mDrawable.setBounds(0, 0, vwidth, vheight);
            mDrawMatrix = null;
        } else {
            mDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
            if (mDrawMatrix == null) {
                mDrawMatrix = new android.graphics.Matrix();
            }
            mDrawMatrix.set(matrix);
        }
        invalidate();
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null) {
            return;// couldn't resolve the URI

        }
        if ((mDrawableWidth == 0) || (mDrawableHeight == 0)) {
            return;// nothing to draw (empty bounds)

        }
        if (((mDrawMatrix == null) && (mPaddingTop == 0)) && (mPaddingLeft == 0)) {
            mDrawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();
            if (mCropToPadding) {
                final int scrollX = mScrollX;
                final int scrollY = mScrollY;
                canvas.clipRect(scrollX + mPaddingLeft, scrollY + mPaddingTop, ((scrollX + mRight) - mLeft) - mPaddingRight, ((scrollY + mBottom) - mTop) - mPaddingBottom);
            }
            canvas.translate(mPaddingLeft, mPaddingTop);
            if (mDrawMatrix != null) {
                canvas.concat(mDrawMatrix);
            }
            mDrawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * <p>Return the offset of the widget's text baseline from the widget's top
     * boundary. </p>
     *
     * @return the offset of the baseline within the widget's bounds or -1
    if baseline alignment is not supported.
     */
    @java.lang.Override
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty(category = "layout")
    public int getBaseline() {
        if (mBaselineAlignBottom) {
            return getMeasuredHeight();
        } else {
            return mBaseline;
        }
    }

    /**
     * <p>Set the offset of the widget's text baseline from the widget's top
     * boundary.  This value is overridden by the {@link #setBaselineAlignBottom(boolean)}
     * property.</p>
     *
     * @param baseline
     * 		The baseline to use, or -1 if none is to be provided.
     * @see #setBaseline(int)
     * @unknown ref android.R.styleable#ImageView_baseline
     */
    public void setBaseline(int baseline) {
        if (mBaseline != baseline) {
            mBaseline = baseline;
            requestLayout();
        }
    }

    /**
     * Sets whether the baseline of this view to the bottom of the view.
     * Setting this value overrides any calls to setBaseline.
     *
     * @param aligned
     * 		If true, the image view will be baseline aligned by its bottom edge.
     * @unknown ref android.R.styleable#ImageView_baselineAlignBottom
     */
    public void setBaselineAlignBottom(boolean aligned) {
        if (mBaselineAlignBottom != aligned) {
            mBaselineAlignBottom = aligned;
            requestLayout();
        }
    }

    /**
     * Checks whether this view's baseline is considered the bottom of the view.
     *
     * @return True if the ImageView's baseline is considered the bottom of the view, false if otherwise.
     * @see #setBaselineAlignBottom(boolean)
     */
    @android.view.inspector.InspectableProperty
    public boolean getBaselineAlignBottom() {
        return mBaselineAlignBottom;
    }

    /**
     * Sets a tinting option for the image.
     *
     * @param color
     * 		Color tint to apply.
     * @param mode
     * 		How to apply the color.  The standard mode is
     * 		{@link PorterDuff.Mode#SRC_ATOP}
     * @unknown ref android.R.styleable#ImageView_tint
     */
    public final void setColorFilter(int color, android.graphics.PorterDuff.Mode mode) {
        setColorFilter(new android.graphics.PorterDuffColorFilter(color, mode));
    }

    /**
     * Set a tinting option for the image. Assumes
     * {@link PorterDuff.Mode#SRC_ATOP} blending mode.
     *
     * @param color
     * 		Color tint to apply.
     * @unknown ref android.R.styleable#ImageView_tint
     */
    @android.view.RemotableViewMethod
    public final void setColorFilter(int color) {
        setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Removes the image's {@link android.graphics.ColorFilter}.
     *
     * @see #setColorFilter(int)
     * @see #getColorFilter()
     */
    public final void clearColorFilter() {
        setColorFilter(null);
    }

    /**
     *
     *
     * @unknown Candidate for future API inclusion
     */
    public final void setXfermode(android.graphics.Xfermode mode) {
        if (mXfermode != mode) {
            mXfermode = mode;
            mHasXfermode = true;
            applyXfermode();
            invalidate();
        }
    }

    /**
     * Returns the active color filter for this ImageView.
     *
     * @return the active color filter for this ImageView
     * @see #setColorFilter(android.graphics.ColorFilter)
     */
    public android.graphics.ColorFilter getColorFilter() {
        return mColorFilter;
    }

    /**
     * Apply an arbitrary colorfilter to the image.
     *
     * @param cf
     * 		the colorfilter to apply (may be null)
     * @see #getColorFilter()
     */
    public void setColorFilter(android.graphics.ColorFilter cf) {
        if (mColorFilter != cf) {
            mColorFilter = cf;
            mHasColorFilter = true;
            applyColorFilter();
            invalidate();
        }
    }

    /**
     * Returns the alpha that will be applied to the drawable of this ImageView.
     *
     * @return the alpha value that will be applied to the drawable of this
    ImageView (between 0 and 255 inclusive, with 0 being transparent and
    255 being opaque)
     * @see #setImageAlpha(int)
     */
    public int getImageAlpha() {
        return mAlpha;
    }

    /**
     * Sets the alpha value that should be applied to the image.
     *
     * @param alpha
     * 		the alpha value that should be applied to the image (between
     * 		0 and 255 inclusive, with 0 being transparent and 255 being opaque)
     * @see #getImageAlpha()
     */
    @android.view.RemotableViewMethod
    public void setImageAlpha(int alpha) {
        setAlpha(alpha);
    }

    /**
     * Sets the alpha value that should be applied to the image.
     *
     * @param alpha
     * 		the alpha value that should be applied to the image
     * @deprecated use #setImageAlpha(int) instead
     */
    @java.lang.Deprecated
    @android.view.RemotableViewMethod
    public void setAlpha(int alpha) {
        alpha &= 0xff;
        // keep it legal
        if (mAlpha != alpha) {
            mAlpha = alpha;
            mHasAlpha = true;
            applyAlpha();
            invalidate();
        }
    }

    private void applyXfermode() {
        if ((mDrawable != null) && mHasXfermode) {
            mDrawable = mDrawable.mutate();
            mDrawable.setXfermode(mXfermode);
        }
    }

    private void applyColorFilter() {
        if ((mDrawable != null) && mHasColorFilter) {
            mDrawable = mDrawable.mutate();
            mDrawable.setColorFilter(mColorFilter);
        }
    }

    private void applyAlpha() {
        if ((mDrawable != null) && mHasAlpha) {
            mDrawable = mDrawable.mutate();
            mDrawable.setAlpha((mAlpha * mViewAlphaScale) >> 8);
        }
    }

    @java.lang.Override
    public boolean isOpaque() {
        return super.isOpaque() || (((((mDrawable != null) && (mXfermode == null)) && (mDrawable.getOpacity() == android.graphics.PixelFormat.OPAQUE)) && (((mAlpha * mViewAlphaScale) >> 8) == 255)) && isFilledByImage());
    }

    private boolean isFilledByImage() {
        if (mDrawable == null) {
            return false;
        }
        final android.graphics.Rect bounds = mDrawable.getBounds();
        final android.graphics.Matrix matrix = mDrawMatrix;
        if (matrix == null) {
            return (((bounds.left <= 0) && (bounds.top <= 0)) && (bounds.right >= getWidth())) && (bounds.bottom >= getHeight());
        } else
            if (matrix.rectStaysRect()) {
                final android.graphics.RectF boundsSrc = mTempSrc;
                final android.graphics.RectF boundsDst = mTempDst;
                boundsSrc.set(bounds);
                matrix.mapRect(boundsDst, boundsSrc);
                return (((boundsDst.left <= 0) && (boundsDst.top <= 0)) && (boundsDst.right >= getWidth())) && (boundsDst.bottom >= getHeight());
            } else {
                // If the matrix doesn't map to a rectangle, assume the worst.
                return false;
            }

    }

    @java.lang.Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        // Only do this for new apps post-Nougat
        if ((mDrawable != null) && (!android.widget.ImageView.sCompatDrawableVisibilityDispatch)) {
            mDrawable.setVisible(isVisible, false);
        }
    }

    @android.view.RemotableViewMethod
    @java.lang.Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        // Only do this for old apps pre-Nougat; new apps use onVisibilityAggregated
        if ((mDrawable != null) && android.widget.ImageView.sCompatDrawableVisibilityDispatch) {
            mDrawable.setVisible(visibility == android.view.View.VISIBLE, false);
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Only do this for old apps pre-Nougat; new apps use onVisibilityAggregated
        if ((mDrawable != null) && android.widget.ImageView.sCompatDrawableVisibilityDispatch) {
            mDrawable.setVisible(getVisibility() == android.view.View.VISIBLE, false);
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Only do this for old apps pre-Nougat; new apps use onVisibilityAggregated
        if ((mDrawable != null) && android.widget.ImageView.sCompatDrawableVisibilityDispatch) {
            mDrawable.setVisible(false, false);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ImageView.class.getName();
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
        stream.addProperty("layout:baseline", getBaseline());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public boolean isDefaultFocusHighlightNeeded(android.graphics.drawable.Drawable background, android.graphics.drawable.Drawable foreground) {
        final boolean lackFocusState = ((mDrawable == null) || (!mDrawable.isStateful())) || (!mDrawable.hasFocusStateSpecified());
        return super.isDefaultFocusHighlightNeeded(background, foreground) && lackFocusState;
    }
}

