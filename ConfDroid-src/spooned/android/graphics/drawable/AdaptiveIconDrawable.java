/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.graphics.drawable;


/**
 * <p>This class can also be created via XML inflation using <code>&lt;adaptive-icon></code> tag
 * in addition to dynamic creation.
 *
 * <p>This drawable supports two drawable layers: foreground and background. The layers are clipped
 * when rendering using the mask defined in the device configuration.
 *
 * <ul>
 * <li>Both foreground and background layers should be sized at 108 x 108 dp.</li>
 * <li>The inner 72 x 72 dp  of the icon appears within the masked viewport.</li>
 * <li>The outer 18 dp on each of the 4 sides of the layers is reserved for use by the system UI
 * surfaces to create interesting visual effects, such as parallax or pulsing.</li>
 * </ul>
 *
 * Such motion effect is achieved by internally setting the bounds of the foreground and
 * background layer as following:
 * <pre>
 * Rect(getBounds().left - getBounds().getWidth() * #getExtraInsetFraction(),
 *      getBounds().top - getBounds().getHeight() * #getExtraInsetFraction(),
 *      getBounds().right + getBounds().getWidth() * #getExtraInsetFraction(),
 *      getBounds().bottom + getBounds().getHeight() * #getExtraInsetFraction())
 * </pre>
 */
public class AdaptiveIconDrawable extends android.graphics.drawable.Drawable implements android.graphics.drawable.Drawable.Callback {
    /**
     * Mask path is defined inside device configuration in following dimension: [100 x 100]
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static final float MASK_SIZE = 100.0F;

    /**
     * Launcher icons design guideline
     */
    private static final float SAFEZONE_SCALE = 66.0F / 72.0F;

    /**
     * All four sides of the layers are padded with extra inset so as to provide
     * extra content to reveal within the clip path when performing affine transformations on the
     * layers.
     *
     * Each layers will reserve 25% of it's width and height.
     *
     * As a result, the view port of the layers is smaller than their intrinsic width and height.
     */
    private static final float EXTRA_INSET_PERCENTAGE = 1 / 4.0F;

    private static final float DEFAULT_VIEW_PORT_SCALE = 1.0F / (1 + (2 * android.graphics.drawable.AdaptiveIconDrawable.EXTRA_INSET_PERCENTAGE));

    /**
     * Clip path defined in R.string.config_icon_mask.
     */
    private static android.graphics.Path sMask;

    /**
     * Scaled mask based on the view bounds.
     */
    private final android.graphics.Path mMask;

    private final android.graphics.Path mMaskScaleOnly;

    private final android.graphics.Matrix mMaskMatrix;

    private final android.graphics.Region mTransparentRegion;

    /**
     * Indices used to access {@link #mLayerState.mChildDrawable} array for foreground and
     * background layer.
     */
    private static final int BACKGROUND_ID = 0;

    private static final int FOREGROUND_ID = 1;

    /**
     * State variable that maintains the {@link ChildDrawable} array.
     */
    android.graphics.drawable.AdaptiveIconDrawable.LayerState mLayerState;

    private android.graphics.Shader mLayersShader;

    private android.graphics.Bitmap mLayersBitmap;

    private final android.graphics.Rect mTmpOutRect = new android.graphics.Rect();

    private android.graphics.Rect mHotspotBounds;

    private boolean mMutated;

    private boolean mSuspendChildInvalidation;

    private boolean mChildRequestedInvalidation;

    private final android.graphics.Canvas mCanvas;

    private android.graphics.Paint mPaint = new android.graphics.Paint((android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG) | android.graphics.Paint.FILTER_BITMAP_FLAG);

    /**
     * Constructor used for xml inflation.
     */
    AdaptiveIconDrawable() {
        this(((android.graphics.drawable.AdaptiveIconDrawable.LayerState) (null)), null);
    }

    /**
     * The one constructor to rule them all. This is called by all public
     * constructors to set the state and initialize local properties.
     */
    AdaptiveIconDrawable(@android.annotation.Nullable
    android.graphics.drawable.AdaptiveIconDrawable.LayerState state, @android.annotation.Nullable
    android.content.res.Resources res) {
        mLayerState = createConstantState(state, res);
        // config_icon_mask from context bound resource may have been chaged using
        // OverlayManager. Read that one first.
        android.content.res.Resources r = (android.app.ActivityThread.currentActivityThread() == null) ? android.content.res.Resources.getSystem() : android.app.ActivityThread.currentActivityThread().getApplication().getResources();
        // TODO: either make sMask update only when config_icon_mask changes OR
        // get rid of it all-together in layoutlib
        android.graphics.drawable.AdaptiveIconDrawable.sMask = android.util.PathParser.createPathFromPathData(r.getString(R.string.config_icon_mask));
        mMask = new android.graphics.Path(android.graphics.drawable.AdaptiveIconDrawable.sMask);
        mMaskScaleOnly = new android.graphics.Path(mMask);
        mMaskMatrix = new android.graphics.Matrix();
        mCanvas = new android.graphics.Canvas();
        mTransparentRegion = new android.graphics.Region();
    }

    private android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable createChildDrawable(android.graphics.drawable.Drawable drawable) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer = new android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable(mLayerState.mDensity);
        layer.mDrawable = drawable;
        layer.mDrawable.setCallback(this);
        mLayerState.mChildrenChangingConfigurations |= layer.mDrawable.getChangingConfigurations();
        return layer;
    }

    android.graphics.drawable.AdaptiveIconDrawable.LayerState createConstantState(@android.annotation.Nullable
    android.graphics.drawable.AdaptiveIconDrawable.LayerState state, @android.annotation.Nullable
    android.content.res.Resources res) {
        return new android.graphics.drawable.AdaptiveIconDrawable.LayerState(state, this, res);
    }

    /**
     * Constructor used to dynamically create this drawable.
     *
     * @param backgroundDrawable
     * 		drawable that should be rendered in the background
     * @param foregroundDrawable
     * 		drawable that should be rendered in the foreground
     */
    public AdaptiveIconDrawable(android.graphics.drawable.Drawable backgroundDrawable, android.graphics.drawable.Drawable foregroundDrawable) {
        this(((android.graphics.drawable.AdaptiveIconDrawable.LayerState) (null)), null);
        if (backgroundDrawable != null) {
            addLayer(android.graphics.drawable.AdaptiveIconDrawable.BACKGROUND_ID, createChildDrawable(backgroundDrawable));
        }
        if (foregroundDrawable != null) {
            addLayer(android.graphics.drawable.AdaptiveIconDrawable.FOREGROUND_ID, createChildDrawable(foregroundDrawable));
        }
    }

    /**
     * Sets the layer to the {@param index} and invalidates cache.
     *
     * @param index
     * 		The index of the layer.
     * @param layer
     * 		The layer to add.
     */
    private void addLayer(int index, @android.annotation.NonNull
    android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer) {
        mLayerState.mChildren[index] = layer;
        mLayerState.invalidateCache();
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final android.graphics.drawable.AdaptiveIconDrawable.LayerState state = mLayerState;
        if (state == null) {
            return;
        }
        // The density may have changed since the last update. This will
        // apply scaling to any existing constant state properties.
        final int deviceDensity = android.graphics.drawable.Drawable.resolveDensity(r, 0);
        state.setDensity(deviceDensity);
        state.mSrcDensityOverride = mSrcDensityOverride;
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = state.mChildren;
        for (int i = 0; i < state.mChildren.length; i++) {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer = array[i];
            layer.setDensity(deviceDensity);
        }
        inflateLayers(r, parser, attrs, theme);
    }

    /**
     * All four sides of the layers are padded with extra inset so as to provide
     * extra content to reveal within the clip path when performing affine transformations on the
     * layers.
     *
     * @see #getForeground() and #getBackground() for more info on how this value is used
     */
    public static float getExtraInsetFraction() {
        return android.graphics.drawable.AdaptiveIconDrawable.EXTRA_INSET_PERCENTAGE;
    }

    /**
     *
     *
     * @unknown 
     */
    public static float getExtraInsetPercentage() {
        return android.graphics.drawable.AdaptiveIconDrawable.EXTRA_INSET_PERCENTAGE;
    }

    /**
     * When called before the bound is set, the returned path is identical to
     * R.string.config_icon_mask. After the bound is set, the
     * returned path's computed bound is same as the #getBounds().
     *
     * @return the mask path object used to clip the drawable
     */
    public android.graphics.Path getIconMask() {
        return mMask;
    }

    /**
     * Returns the foreground drawable managed by this class. The bound of this drawable is
     * extended by {@link #getExtraInsetFraction()} * getBounds().width on left/right sides and by
     * {@link #getExtraInsetFraction()} * getBounds().height on top/bottom sides.
     *
     * @return the foreground drawable managed by this drawable
     */
    public android.graphics.drawable.Drawable getForeground() {
        return mLayerState.mChildren[android.graphics.drawable.AdaptiveIconDrawable.FOREGROUND_ID].mDrawable;
    }

    /**
     * Returns the foreground drawable managed by this class. The bound of this drawable is
     * extended by {@link #getExtraInsetFraction()} * getBounds().width on left/right sides and by
     * {@link #getExtraInsetFraction()} * getBounds().height on top/bottom sides.
     *
     * @return the background drawable managed by this drawable
     */
    public android.graphics.drawable.Drawable getBackground() {
        return mLayerState.mChildren[android.graphics.drawable.AdaptiveIconDrawable.BACKGROUND_ID].mDrawable;
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        if (bounds.isEmpty()) {
            return;
        }
        updateLayerBounds(bounds);
    }

    private void updateLayerBounds(android.graphics.Rect bounds) {
        if (bounds.isEmpty()) {
            return;
        }
        try {
            suspendChildInvalidation();
            updateLayerBoundsInternal(bounds);
            updateMaskBoundsInternal(bounds);
        } finally {
            resumeChildInvalidation();
        }
    }

    /**
     * Set the child layer bounds bigger than the view port size by {@link #DEFAULT_VIEW_PORT_SCALE}
     */
    private void updateLayerBoundsInternal(android.graphics.Rect bounds) {
        int cX = bounds.width() / 2;
        int cY = bounds.height() / 2;
        for (int i = 0, count = mLayerState.N_CHILDREN; i < count; i++) {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable r = mLayerState.mChildren[i];
            if (r == null) {
                continue;
            }
            final android.graphics.drawable.Drawable d = r.mDrawable;
            if (d == null) {
                continue;
            }
            int insetWidth = ((int) (bounds.width() / (android.graphics.drawable.AdaptiveIconDrawable.DEFAULT_VIEW_PORT_SCALE * 2)));
            int insetHeight = ((int) (bounds.height() / (android.graphics.drawable.AdaptiveIconDrawable.DEFAULT_VIEW_PORT_SCALE * 2)));
            final android.graphics.Rect outRect = mTmpOutRect;
            outRect.set(cX - insetWidth, cY - insetHeight, cX + insetWidth, cY + insetHeight);
            d.setBounds(outRect);
        }
    }

    private void updateMaskBoundsInternal(android.graphics.Rect b) {
        // reset everything that depends on the view bounds
        mMaskMatrix.setScale(b.width() / android.graphics.drawable.AdaptiveIconDrawable.MASK_SIZE, b.height() / android.graphics.drawable.AdaptiveIconDrawable.MASK_SIZE);
        android.graphics.drawable.AdaptiveIconDrawable.sMask.transform(mMaskMatrix, mMaskScaleOnly);
        mMaskMatrix.postTranslate(b.left, b.top);
        android.graphics.drawable.AdaptiveIconDrawable.sMask.transform(mMaskMatrix, mMask);
        if (((mLayersBitmap == null) || (mLayersBitmap.getWidth() != b.width())) || (mLayersBitmap.getHeight() != b.height())) {
            mLayersBitmap = android.graphics.Bitmap.createBitmap(b.width(), b.height(), android.graphics.Bitmap.Config.ARGB_8888);
        }
        mPaint.setShader(null);
        mTransparentRegion.setEmpty();
        mLayersShader = null;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mLayersBitmap == null) {
            return;
        }
        if (mLayersShader == null) {
            mCanvas.setBitmap(mLayersBitmap);
            mCanvas.drawColor(android.graphics.Color.BLACK);
            for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
                if (mLayerState.mChildren[i] == null) {
                    continue;
                }
                final android.graphics.drawable.Drawable dr = mLayerState.mChildren[i].mDrawable;
                if (dr != null) {
                    dr.draw(mCanvas);
                }
            }
            mLayersShader = new android.graphics.BitmapShader(mLayersBitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
            mPaint.setShader(mLayersShader);
        }
        if (mMaskScaleOnly != null) {
            android.graphics.Rect bounds = getBounds();
            canvas.translate(bounds.left, bounds.top);
            canvas.drawPath(mMaskScaleOnly, mPaint);
            canvas.translate(-bounds.left, -bounds.top);
        }
    }

    @java.lang.Override
    public void invalidateSelf() {
        mLayersShader = null;
        super.invalidateSelf();
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        outline.setConvexPath(mMask);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public android.graphics.Region getSafeZone() {
        mMaskMatrix.reset();
        mMaskMatrix.setScale(android.graphics.drawable.AdaptiveIconDrawable.SAFEZONE_SCALE, android.graphics.drawable.AdaptiveIconDrawable.SAFEZONE_SCALE, getBounds().centerX(), getBounds().centerY());
        android.graphics.Path p = new android.graphics.Path();
        mMask.transform(mMaskMatrix, p);
        android.graphics.Region safezoneRegion = new android.graphics.Region(getBounds());
        safezoneRegion.setPath(p, safezoneRegion);
        return safezoneRegion;
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.Region getTransparentRegion() {
        if (mTransparentRegion.isEmpty()) {
            mMask.toggleInverseFillType();
            mTransparentRegion.set(getBounds());
            mTransparentRegion.setPath(mMask, mTransparentRegion);
            mMask.toggleInverseFillType();
        }
        return mTransparentRegion;
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.AdaptiveIconDrawable.LayerState state = mLayerState;
        if (state == null) {
            return;
        }
        final int density = android.graphics.drawable.Drawable.resolveDensity(t.getResources(), 0);
        state.setDensity(density);
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = state.mChildren;
        for (int i = 0; i < state.N_CHILDREN; i++) {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer = array[i];
            layer.setDensity(density);
            if (layer.mThemeAttrs != null) {
                final android.content.res.TypedArray a = t.resolveAttributes(layer.mThemeAttrs, R.styleable.AdaptiveIconDrawableLayer);
                updateLayerFromTypedArray(layer, a);
                a.recycle();
            }
            final android.graphics.drawable.Drawable d = layer.mDrawable;
            if ((d != null) && d.canApplyTheme()) {
                d.applyTheme(t);
                // Update cached mask of child changing configurations.
                state.mChildrenChangingConfigurations |= d.getChangingConfigurations();
            }
        }
    }

    /**
     * Inflates child layers using the specified parser.
     */
    private void inflateLayers(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.graphics.drawable.AdaptiveIconDrawable.LayerState state = mLayerState;
        final int innerDepth = parser.getDepth() + 1;
        int type;
        int depth;
        int childIndex = 0;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (((depth = parser.getDepth()) >= innerDepth) || (type != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            if (depth > innerDepth) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals("background")) {
                childIndex = android.graphics.drawable.AdaptiveIconDrawable.BACKGROUND_ID;
            } else
                if (tagName.equals("foreground")) {
                    childIndex = android.graphics.drawable.AdaptiveIconDrawable.FOREGROUND_ID;
                } else {
                    continue;
                }

            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer = new android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable(state.mDensity);
            final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.AdaptiveIconDrawableLayer);
            updateLayerFromTypedArray(layer, a);
            a.recycle();
            // If the layer doesn't have a drawable or unresolved theme
            // attribute for a drawable, attempt to parse one from the child
            // element. If multiple child elements exist, we'll only use the
            // first one.
            if ((layer.mDrawable == null) && (layer.mThemeAttrs == null)) {
                while ((type = parser.next()) == org.xmlpull.v1.XmlPullParser.TEXT) {
                } 
                if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                    throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": <foreground> or <background> tag requires a 'drawable'") + "attribute or child tag defining a drawable");
                }
                // We found a child drawable. Take ownership.
                layer.mDrawable = android.graphics.drawable.Drawable.createFromXmlInnerForDensity(r, parser, attrs, mLayerState.mSrcDensityOverride, theme);
                layer.mDrawable.setCallback(this);
                state.mChildrenChangingConfigurations |= layer.mDrawable.getChangingConfigurations();
            }
            addLayer(childIndex, layer);
        } 
    }

    private void updateLayerFromTypedArray(@android.annotation.NonNull
    android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer, @android.annotation.NonNull
    android.content.res.TypedArray a) {
        final android.graphics.drawable.AdaptiveIconDrawable.LayerState state = mLayerState;
        // Account for any configuration changes.
        state.mChildrenChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        layer.mThemeAttrs = a.extractThemeAttrs();
        android.graphics.drawable.Drawable dr = a.getDrawableForDensity(R.styleable.AdaptiveIconDrawableLayer_drawable, state.mSrcDensityOverride);
        if (dr != null) {
            if (layer.mDrawable != null) {
                // It's possible that a drawable was already set, in which case
                // we should clear the callback. We may have also integrated the
                // drawable's changing configurations, but we don't have enough
                // information to revert that change.
                layer.mDrawable.setCallback(null);
            }
            // Take ownership of the new drawable.
            layer.mDrawable = dr;
            layer.mDrawable.setCallback(this);
            state.mChildrenChangingConfigurations |= layer.mDrawable.getChangingConfigurations();
        }
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return ((mLayerState != null) && mLayerState.canApplyTheme()) || super.canApplyTheme();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isProjected() {
        if (super.isProjected()) {
            return true;
        }
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] layers = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            if ((layers[i].mDrawable != null) && layers[i].mDrawable.isProjected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Temporarily suspends child invalidation.
     *
     * @see #resumeChildInvalidation()
     */
    private void suspendChildInvalidation() {
        mSuspendChildInvalidation = true;
    }

    /**
     * Resumes child invalidation after suspension, immediately performing an
     * invalidation if one was requested by a child during suspension.
     *
     * @see #suspendChildInvalidation()
     */
    private void resumeChildInvalidation() {
        mSuspendChildInvalidation = false;
        if (mChildRequestedInvalidation) {
            mChildRequestedInvalidation = false;
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        if (mSuspendChildInvalidation) {
            mChildRequestedInvalidation = true;
        } else {
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void scheduleDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who, @android.annotation.NonNull
    java.lang.Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @java.lang.Override
    public void unscheduleDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who, @android.annotation.NonNull
    java.lang.Runnable what) {
        unscheduleSelf(what);
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mLayerState.getChangingConfigurations();
    }

    @java.lang.Override
    public void setHotspot(float x, float y) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspot(x, y);
            }
        }
    }

    @java.lang.Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspotBounds(left, top, right, bottom);
            }
        }
        if (mHotspotBounds == null) {
            mHotspotBounds = new android.graphics.Rect(left, top, right, bottom);
        } else {
            mHotspotBounds.set(left, top, right, bottom);
        }
    }

    @java.lang.Override
    public void getHotspotBounds(android.graphics.Rect outRect) {
        if (mHotspotBounds != null) {
            outRect.set(mHotspotBounds);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        final boolean changed = super.setVisible(visible, restart);
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setVisible(visible, restart);
            }
        }
        return changed;
    }

    @java.lang.Override
    public void setDither(boolean dither) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setDither(dither);
            }
        }
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @java.lang.Override
    public int getAlpha() {
        return mPaint.getAlpha();
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setColorFilter(colorFilter);
            }
        }
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        final int N = mLayerState.N_CHILDREN;
        for (int i = 0; i < N; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintList(tint);
            }
        }
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        final int N = mLayerState.N_CHILDREN;
        for (int i = 0; i < N; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintBlendMode(blendMode);
            }
        }
    }

    public void setOpacity(int opacity) {
        mLayerState.mOpacityOverride = opacity;
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        mLayerState.mAutoMirrored = mirrored;
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setAutoMirrored(mirrored);
            }
        }
    }

    @java.lang.Override
    public boolean isAutoMirrored() {
        return mLayerState.mAutoMirrored;
    }

    @java.lang.Override
    public void jumpToCurrentState() {
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.jumpToCurrentState();
            }
        }
    }

    @java.lang.Override
    public boolean isStateful() {
        return mLayerState.isStateful();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return mLayerState.hasFocusStateSpecified();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        boolean changed = false;
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (((dr != null) && dr.isStateful()) && dr.setState(state)) {
                changed = true;
            }
        }
        if (changed) {
            updateLayerBounds(getBounds());
        }
        return changed;
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        boolean changed = false;
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if ((dr != null) && dr.setLevel(level)) {
                changed = true;
            }
        }
        if (changed) {
            updateLayerBounds(getBounds());
        }
        return changed;
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return ((int) (getMaxIntrinsicWidth() * android.graphics.drawable.AdaptiveIconDrawable.DEFAULT_VIEW_PORT_SCALE));
    }

    private int getMaxIntrinsicWidth() {
        int width = -1;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable r = mLayerState.mChildren[i];
            if (r.mDrawable == null) {
                continue;
            }
            final int w = r.mDrawable.getIntrinsicWidth();
            if (w > width) {
                width = w;
            }
        }
        return width;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return ((int) (getMaxIntrinsicHeight() * android.graphics.drawable.AdaptiveIconDrawable.DEFAULT_VIEW_PORT_SCALE));
    }

    private int getMaxIntrinsicHeight() {
        int height = -1;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable r = mLayerState.mChildren[i];
            if (r.mDrawable == null) {
                continue;
            }
            final int h = r.mDrawable.getIntrinsicHeight();
            if (h > height) {
                height = h;
            }
        }
        return height;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        if (mLayerState.canConstantState()) {
            mLayerState.mChangingConfigurations = getChangingConfigurations();
            return mLayerState;
        }
        return null;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mLayerState = createConstantState(mLayerState, null);
            for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
                final android.graphics.drawable.Drawable dr = mLayerState.mChildren[i].mDrawable;
                if (dr != null) {
                    dr.mutate();
                }
            }
            mMutated = true;
        }
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearMutated() {
        super.clearMutated();
        final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mLayerState.mChildren;
        for (int i = 0; i < mLayerState.N_CHILDREN; i++) {
            final android.graphics.drawable.Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.clearMutated();
            }
        }
        mMutated = false;
    }

    static class ChildDrawable {
        public android.graphics.drawable.Drawable mDrawable;

        public int[] mThemeAttrs;

        public int mDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;

        ChildDrawable(int density) {
            mDensity = density;
        }

        ChildDrawable(@android.annotation.NonNull
        android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable orig, @android.annotation.NonNull
        android.graphics.drawable.AdaptiveIconDrawable owner, @android.annotation.Nullable
        android.content.res.Resources res) {
            final android.graphics.drawable.Drawable dr = orig.mDrawable;
            final android.graphics.drawable.Drawable clone;
            if (dr != null) {
                final android.graphics.drawable.Drawable.ConstantState cs = dr.getConstantState();
                if (cs == null) {
                    clone = dr;
                } else
                    if (res != null) {
                        clone = cs.newDrawable(res);
                    } else {
                        clone = cs.newDrawable();
                    }

                clone.setCallback(owner);
                clone.setBounds(dr.getBounds());
                clone.setLevel(dr.getLevel());
            } else {
                clone = null;
            }
            mDrawable = clone;
            mThemeAttrs = orig.mThemeAttrs;
            mDensity = android.graphics.drawable.Drawable.resolveDensity(res, orig.mDensity);
        }

        public boolean canApplyTheme() {
            return (mThemeAttrs != null) || ((mDrawable != null) && mDrawable.canApplyTheme());
        }

        public final void setDensity(int targetDensity) {
            if (mDensity != targetDensity) {
                mDensity = targetDensity;
            }
        }
    }

    static class LayerState extends android.graphics.drawable.Drawable.ConstantState {
        private int[] mThemeAttrs;

        static final int N_CHILDREN = 2;

        android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] mChildren;

        // The density at which to render the drawable and its children.
        int mDensity;

        // The density to use when inflating/looking up the children drawables. A value of 0 means
        // use the system's density.
        int mSrcDensityOverride = 0;

        int mOpacityOverride = android.graphics.PixelFormat.UNKNOWN;

        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        @android.content.pm.ActivityInfo.Config
        int mChildrenChangingConfigurations;

        private boolean mCheckedOpacity;

        private int mOpacity;

        private boolean mCheckedStateful;

        private boolean mIsStateful;

        private boolean mAutoMirrored = false;

        LayerState(@android.annotation.Nullable
        android.graphics.drawable.AdaptiveIconDrawable.LayerState orig, @android.annotation.NonNull
        android.graphics.drawable.AdaptiveIconDrawable owner, @android.annotation.Nullable
        android.content.res.Resources res) {
            mDensity = android.graphics.drawable.Drawable.resolveDensity(res, orig != null ? orig.mDensity : 0);
            mChildren = new android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN];
            if (orig != null) {
                final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] origChildDrawable = orig.mChildren;
                mChangingConfigurations = orig.mChangingConfigurations;
                mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
                for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                    final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable or = origChildDrawable[i];
                    mChildren[i] = new android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable(or, owner, res);
                }
                mCheckedOpacity = orig.mCheckedOpacity;
                mOpacity = orig.mOpacity;
                mCheckedStateful = orig.mCheckedStateful;
                mIsStateful = orig.mIsStateful;
                mAutoMirrored = orig.mAutoMirrored;
                mThemeAttrs = orig.mThemeAttrs;
                mOpacityOverride = orig.mOpacityOverride;
                mSrcDensityOverride = orig.mSrcDensityOverride;
            } else {
                for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                    mChildren[i] = new android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable(mDensity);
                }
            }
        }

        public final void setDensity(int targetDensity) {
            if (mDensity != targetDensity) {
                mDensity = targetDensity;
            }
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            if ((mThemeAttrs != null) || super.canApplyTheme()) {
                return true;
            }
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mChildren;
            for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable layer = array[i];
                if (layer.canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.AdaptiveIconDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(@android.annotation.Nullable
        android.content.res.Resources res) {
            return new android.graphics.drawable.AdaptiveIconDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations | mChildrenChangingConfigurations;
        }

        public final int getOpacity() {
            if (mCheckedOpacity) {
                return mOpacity;
            }
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mChildren;
            // Seek to the first non-null drawable.
            int firstIndex = -1;
            for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                if (array[i].mDrawable != null) {
                    firstIndex = i;
                    break;
                }
            }
            int op;
            if (firstIndex >= 0) {
                op = array[firstIndex].mDrawable.getOpacity();
            } else {
                op = android.graphics.PixelFormat.TRANSPARENT;
            }
            // Merge all remaining non-null drawables.
            for (int i = firstIndex + 1; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                final android.graphics.drawable.Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    op = android.graphics.drawable.Drawable.resolveOpacity(op, dr.getOpacity());
                }
            }
            mOpacity = op;
            mCheckedOpacity = true;
            return op;
        }

        public final boolean isStateful() {
            if (mCheckedStateful) {
                return mIsStateful;
            }
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mChildren;
            boolean isStateful = false;
            for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                final android.graphics.drawable.Drawable dr = array[i].mDrawable;
                if ((dr != null) && dr.isStateful()) {
                    isStateful = true;
                    break;
                }
            }
            mIsStateful = isStateful;
            mCheckedStateful = true;
            return isStateful;
        }

        public final boolean hasFocusStateSpecified() {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mChildren;
            for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                final android.graphics.drawable.Drawable dr = array[i].mDrawable;
                if ((dr != null) && dr.hasFocusStateSpecified()) {
                    return true;
                }
            }
            return false;
        }

        public final boolean canConstantState() {
            final android.graphics.drawable.AdaptiveIconDrawable.ChildDrawable[] array = mChildren;
            for (int i = 0; i < android.graphics.drawable.AdaptiveIconDrawable.LayerState.N_CHILDREN; i++) {
                final android.graphics.drawable.Drawable dr = array[i].mDrawable;
                if ((dr != null) && (dr.getConstantState() == null)) {
                    return false;
                }
            }
            // Don't cache the result, this method is not called very often.
            return true;
        }

        public void invalidateCache() {
            mCheckedOpacity = false;
            mCheckedStateful = false;
        }
    }
}

