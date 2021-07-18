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
package android.graphics.drawable;


/**
 * This lets you create a drawable based on an XML vector graphic.
 * <p/>
 * <strong>Note:</strong> To optimize for the re-drawing performance, one bitmap cache is created
 * for each VectorDrawable. Therefore, referring to the same VectorDrawable means sharing the same
 * bitmap cache. If these references don't agree upon on the same size, the bitmap will be recreated
 * and redrawn every time size is changed. In other words, if a VectorDrawable is used for
 * different sizes, it is more efficient to create multiple VectorDrawables, one for each size.
 * <p/>
 * VectorDrawable can be defined in an XML file with the <code>&lt;vector></code> element.
 * <p/>
 * The vector drawable has the following elements:
 * <p/>
 * <dt><code>&lt;vector></code></dt>
 * <dl>
 * <dd>Used to define a vector drawable
 * <dl>
 * <dt><code>android:name</code></dt>
 * <dd>Defines the name of this vector drawable.</dd>
 * <dt><code>android:width</code></dt>
 * <dd>Used to define the intrinsic width of the drawable.
 * This support all the dimension units, normally specified with dp.</dd>
 * <dt><code>android:height</code></dt>
 * <dd>Used to define the intrinsic height the drawable.
 * This support all the dimension units, normally specified with dp.</dd>
 * <dt><code>android:viewportWidth</code></dt>
 * <dd>Used to define the width of the viewport space. Viewport is basically
 * the virtual canvas where the paths are drawn on.</dd>
 * <dt><code>android:viewportHeight</code></dt>
 * <dd>Used to define the height of the viewport space. Viewport is basically
 * the virtual canvas where the paths are drawn on.</dd>
 * <dt><code>android:tint</code></dt>
 * <dd>The color to apply to the drawable as a tint. By default, no tint is applied.</dd>
 * <dt><code>android:tintMode</code></dt>
 * <dd>The Porter-Duff blending mode for the tint color. Default is src_in.</dd>
 * <dt><code>android:autoMirrored</code></dt>
 * <dd>Indicates if the drawable needs to be mirrored when its layout direction is
 * RTL (right-to-left). Default is false.</dd>
 * <dt><code>android:alpha</code></dt>
 * <dd>The opacity of this drawable. Default is 1.0.</dd>
 * </dl></dd>
 * </dl>
 *
 * <dl>
 * <dt><code>&lt;group></code></dt>
 * <dd>Defines a group of paths or subgroups, plus transformation information.
 * The transformations are defined in the same coordinates as the viewport.
 * And the transformations are applied in the order of scale, rotate then translate.
 * <dl>
 * <dt><code>android:name</code></dt>
 * <dd>Defines the name of the group.</dd>
 * <dt><code>android:rotation</code></dt>
 * <dd>The degrees of rotation of the group. Default is 0.</dd>
 * <dt><code>android:pivotX</code></dt>
 * <dd>The X coordinate of the pivot for the scale and rotation of the group.
 * This is defined in the viewport space. Default is 0.</dd>
 * <dt><code>android:pivotY</code></dt>
 * <dd>The Y coordinate of the pivot for the scale and rotation of the group.
 * This is defined in the viewport space. Default is 0.</dd>
 * <dt><code>android:scaleX</code></dt>
 * <dd>The amount of scale on the X Coordinate. Default is 1.</dd>
 * <dt><code>android:scaleY</code></dt>
 * <dd>The amount of scale on the Y coordinate. Default is 1.</dd>
 * <dt><code>android:translateX</code></dt>
 * <dd>The amount of translation on the X coordinate.
 * This is defined in the viewport space. Default is 0.</dd>
 * <dt><code>android:translateY</code></dt>
 * <dd>The amount of translation on the Y coordinate.
 * This is defined in the viewport space. Default is 0.</dd>
 * </dl></dd>
 * </dl>
 *
 * <dl>
 * <dt><code>&lt;path></code></dt>
 * <dd>Defines paths to be drawn.
 * <dl>
 * <dt><code>android:name</code></dt>
 * <dd>Defines the name of the path.</dd>
 * <dt><code>android:pathData</code></dt>
 * <dd>Defines path data using exactly same format as "d" attribute
 * in the SVG's path data. This is defined in the viewport space.</dd>
 * <dt><code>android:fillColor</code></dt>
 * <dd>Specifies the color used to fill the path. May be a color or, for SDK 24+, a color state list
 * or a gradient color (See {@link android.R.styleable#GradientColor}
 * and {@link android.R.styleable#GradientColorItem}).
 * If this property is animated, any value set by the animation will override the original value.
 * No path fill is drawn if this property is not specified.</dd>
 * <dt><code>android:strokeColor</code></dt>
 * <dd>Specifies the color used to draw the path outline. May be a color or, for SDK 24+, a color
 * state list or a gradient color (See {@link android.R.styleable#GradientColor}
 * and {@link android.R.styleable#GradientColorItem}).
 * If this property is animated, any value set by the animation will override the original value.
 * No path outline is drawn if this property is not specified.</dd>
 * <dt><code>android:strokeWidth</code></dt>
 * <dd>The width a path stroke. Default is 0.</dd>
 * <dt><code>android:strokeAlpha</code></dt>
 * <dd>The opacity of a path stroke. Default is 1.</dd>
 * <dt><code>android:fillAlpha</code></dt>
 * <dd>The opacity to fill the path with. Default is 1.</dd>
 * <dt><code>android:trimPathStart</code></dt>
 * <dd>The fraction of the path to trim from the start, in the range from 0 to 1. Default is 0.</dd>
 * <dt><code>android:trimPathEnd</code></dt>
 * <dd>The fraction of the path to trim from the end, in the range from 0 to 1. Default is 1.</dd>
 * <dt><code>android:trimPathOffset</code></dt>
 * <dd>Shift trim region (allows showed region to include the start and end), in the range
 * from 0 to 1. Default is 0.</dd>
 * <dt><code>android:strokeLineCap</code></dt>
 * <dd>Sets the linecap for a stroked path: butt, round, square. Default is butt.</dd>
 * <dt><code>android:strokeLineJoin</code></dt>
 * <dd>Sets the lineJoin for a stroked path: miter,round,bevel. Default is miter.</dd>
 * <dt><code>android:strokeMiterLimit</code></dt>
 * <dd>Sets the Miter limit for a stroked path. Default is 4.</dd>
 * <dt><code>android:fillType</code></dt>
 * <dd>For SDK 24+, sets the fillType for a path. The types can be either "evenOdd" or "nonZero". They behave the
 * same as SVG's "fill-rule" properties. Default is nonZero. For more details, see
 * <a href="https://www.w3.org/TR/SVG/painting.html#FillRuleProperty">FillRuleProperty</a></dd>
 * </dl></dd>
 *
 * </dl>
 *
 * <dl>
 * <dt><code>&lt;clip-path></code></dt>
 * <dd>Defines path to be the current clip. Note that the clip path only apply to
 * the current group and its children.
 * <dl>
 * <dt><code>android:name</code></dt>
 * <dd>Defines the name of the clip path.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:pathData</code></dt>
 * <dd>Defines clip path using the same format as "d" attribute
 * in the SVG's path data.</dd>
 * <dd>Animatable : Yes.</dd>
 * </dl></dd>
 * </dl>
 * <li>Here is a simple VectorDrawable in this vectordrawable.xml file.
 * <pre>
 * &lt;vector xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;
 *     android:height=&quot;64dp&quot;
 *     android:width=&quot;64dp&quot;
 *     android:viewportHeight=&quot;600&quot;
 *     android:viewportWidth=&quot;600&quot; &gt;
 *     &lt;group
 *         android:name=&quot;rotationGroup&quot;
 *         android:pivotX=&quot;300.0&quot;
 *         android:pivotY=&quot;300.0&quot;
 *         android:rotation=&quot;45.0&quot; &gt;
 *         &lt;path
 *             android:name=&quot;v&quot;
 *             android:fillColor=&quot;#000000&quot;
 *             android:pathData=&quot;M300,70 l 0,-70 70,70 0,0 -70,70z&quot; /&gt;
 *     &lt;/group&gt;
 * &lt;/vector&gt;
 * </pre>
 * </li>
 * <h4>Gradient support</h4>
 * We support 3 types of gradients: {@link android.graphics.LinearGradient},
 * {@link android.graphics.RadialGradient}, or {@link android.graphics.SweepGradient}.
 * <p/>
 * And we support all of 3 types of tile modes {@link android.graphics.Shader.TileMode}:
 * CLAMP, REPEAT, MIRROR.
 * <p/>
 * All of the attributes are listed in {@link android.R.styleable#GradientColor}.
 * Note that different attributes are relevant for different types of gradient.
 * <table border="2" align="center" cellpadding="5">
 *     <thead>
 *         <tr>
 *             <th>LinearGradient</th>
 *             <th>RadialGradient</th>
 *             <th>SweepGradient</th>
 *         </tr>
 *     </thead>
 *     <tr>
 *         <td>startColor </td>
 *         <td>startColor</td>
 *         <td>startColor</td>
 *     </tr>
 *     <tr>
 *         <td>centerColor</td>
 *         <td>centerColor</td>
 *         <td>centerColor</td>
 *     </tr>
 *     <tr>
 *         <td>endColor</td>
 *         <td>endColor</td>
 *         <td>endColor</td>
 *     </tr>
 *     <tr>
 *         <td>type</td>
 *         <td>type</td>
 *         <td>type</td>
 *     </tr>
 *     <tr>
 *         <td>tileMode</td>
 *         <td>tileMode</td>
 *         <td>tileMode</td>
 *     </tr>
 *     <tr>
 *         <td>startX</td>
 *         <td>centerX</td>
 *         <td>centerX</td>
 *     </tr>
 *     <tr>
 *         <td>startY</td>
 *         <td>centerY</td>
 *         <td>centerY</td>
 *     </tr>
 *     <tr>
 *         <td>endX</td>
 *         <td>gradientRadius</td>
 *         <td></td>
 *     </tr>
 *     <tr>
 *         <td>endY</td>
 *         <td></td>
 *         <td></td>
 *     </tr>
 * </table>
 * <p/>
 * Also note that if any color item {@link android.R.styleable#GradientColorItem} is defined, then
 * startColor, centerColor and endColor will be ignored.
 * <p/>
 * See more details in {@link android.R.styleable#GradientColor} and
 * {@link android.R.styleable#GradientColorItem}.
 * <p/>
 * Here is a simple example that defines a linear gradient.
 * <pre>
 * &lt;gradient xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:startColor="?android:attr/colorPrimary"
 *     android:endColor="?android:attr/colorControlActivated"
 *     android:centerColor="#f00"
 *     android:startX="0"
 *     android:startY="0"
 *     android:endX="100"
 *     android:endY="100"
 *     android:type="linear"&gt;
 * &lt;/gradient&gt;
 * </pre>
 * And here is a simple example that defines a radial gradient using color items.
 * <pre>
 * &lt;gradient xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:centerX="300"
 *     android:centerY="300"
 *     android:gradientRadius="100"
 *     android:type="radial"&gt;
 *     &lt;item android:offset="0.1" android:color="#0ff"/&gt;
 *     &lt;item android:offset="0.4" android:color="#fff"/&gt;
 *     &lt;item android:offset="0.9" android:color="#ff0"/&gt;
 * &lt;/gradient&gt;
 * </pre>
 */
public class VectorDrawable extends android.graphics.drawable.Drawable {
    private static final java.lang.String LOGTAG = android.graphics.drawable.VectorDrawable.class.getSimpleName();

    private static final java.lang.String SHAPE_CLIP_PATH = "clip-path";

    private static final java.lang.String SHAPE_GROUP = "group";

    private static final java.lang.String SHAPE_PATH = "path";

    private static final java.lang.String SHAPE_VECTOR = "vector";

    private android.graphics.drawable.VectorDrawable.VectorDrawableState mVectorState;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.PorterDuffColorFilter mTintFilter;

    private android.graphics.BlendModeColorFilter mBlendModeColorFilter;

    private android.graphics.ColorFilter mColorFilter;

    private boolean mMutated;

    /**
     * The density of the display on which this drawable will be rendered.
     */
    private int mTargetDensity;

    // Given the virtual display setup, the dpi can be different than the inflation's dpi.
    // Therefore, we need to scale the values we got from the getDimension*().
    private int mDpiScaledWidth = 0;

    private int mDpiScaledHeight = 0;

    private android.graphics.Insets mDpiScaledInsets = android.graphics.Insets.NONE;

    /**
     * Whether DPI-scaled width, height, and insets need to be updated.
     */
    private boolean mDpiScaledDirty = true;

    // Temp variable, only for saving "new" operation at the draw() time.
    private final android.graphics.Rect mTmpBounds = new android.graphics.Rect();

    public VectorDrawable() {
        this(new android.graphics.drawable.VectorDrawable.VectorDrawableState(null), null);
    }

    /**
     * The one constructor to rule them all. This is called by all public
     * constructors to set the state and initialize local properties.
     */
    private VectorDrawable(@android.annotation.NonNull
    android.graphics.drawable.VectorDrawable.VectorDrawableState state, @android.annotation.Nullable
    android.content.res.Resources res) {
        mVectorState = state;
        updateLocalState(res);
    }

    /**
     * Initializes local dynamic properties from state. This should be called
     * after significant state changes, e.g. from the One True Constructor and
     * after inflating or applying a theme.
     *
     * @param res
     * 		resources of the context in which the drawable will be
     * 		displayed, or {@code null} to use the constant state defaults
     */
    private void updateLocalState(android.content.res.Resources res) {
        final int density = android.graphics.drawable.Drawable.resolveDensity(res, mVectorState.mDensity);
        if (mTargetDensity != density) {
            mTargetDensity = density;
            mDpiScaledDirty = true;
        }
        updateColorFilters(mVectorState.mBlendMode, mVectorState.mTint);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mVectorState = new android.graphics.drawable.VectorDrawable.VectorDrawableState(mVectorState);
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
        mMutated = false;
    }

    @android.annotation.UnsupportedAppUsage
    java.lang.Object getTargetByName(java.lang.String name) {
        return mVectorState.mVGTargetsMap.get(name);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        mVectorState.mChangingConfigurations = getChangingConfigurations();
        return mVectorState;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        // We will offset the bounds for drawBitmap, so copyBounds() here instead
        // of getBounds().
        copyBounds(mTmpBounds);
        if ((mTmpBounds.width() <= 0) || (mTmpBounds.height() <= 0)) {
            // Nothing to draw
            return;
        }
        // Color filters always override tint filters.
        final android.graphics.ColorFilter colorFilter = (mColorFilter == null) ? mBlendModeColorFilter : mColorFilter;
        final long colorFilterNativeInstance = (colorFilter == null) ? 0 : colorFilter.getNativeInstance();
        boolean canReuseCache = mVectorState.canReuseCache();
        int pixelCount = android.graphics.drawable.VectorDrawable.nDraw(mVectorState.getNativeRenderer(), canvas.getNativeCanvasWrapper(), colorFilterNativeInstance, mTmpBounds, needMirroring(), canReuseCache);
        if (pixelCount == 0) {
            // Invalid canvas matrix or drawable bounds. This would not affect existing bitmap
            // cache, if any.
            return;
        }
        int deltaInBytes;
        // Track different bitmap cache based whether the canvas is hw accelerated. By doing so,
        // we don't over count bitmap cache allocation: if the input canvas is always of the same
        // type, only one bitmap cache is allocated.
        if (canvas.isHardwareAccelerated()) {
            // Each pixel takes 4 bytes.
            deltaInBytes = (pixelCount - mVectorState.mLastHWCachePixelCount) * 4;
            mVectorState.mLastHWCachePixelCount = pixelCount;
        } else {
            // Each pixel takes 4 bytes.
            deltaInBytes = (pixelCount - mVectorState.mLastSWCachePixelCount) * 4;
            mVectorState.mLastSWCachePixelCount = pixelCount;
        }
        if (deltaInBytes > 0) {
            dalvik.system.VMRuntime.getRuntime().registerNativeAllocation(deltaInBytes);
        } else
            if (deltaInBytes < 0) {
                dalvik.system.VMRuntime.getRuntime().registerNativeFree(-deltaInBytes);
            }

    }

    @java.lang.Override
    public int getAlpha() {
        return ((int) (mVectorState.getAlpha() * 255));
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        if (mVectorState.setAlpha(alpha / 255.0F)) {
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mColorFilter = colorFilter;
        invalidateSelf();
    }

    @java.lang.Override
    public android.graphics.ColorFilter getColorFilter() {
        return mColorFilter;
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
        if (state.mTint != tint) {
            state.mTint = tint;
            updateColorFilters(mVectorState.mBlendMode, tint);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
        if (state.mBlendMode != blendMode) {
            state.mBlendMode = blendMode;
            updateColorFilters(state.mBlendMode, state.mTint);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public boolean isStateful() {
        return super.isStateful() || ((mVectorState != null) && mVectorState.isStateful());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return (mVectorState != null) && mVectorState.hasFocusStateSpecified();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        boolean changed = false;
        // When the VD is stateful, we need to mutate the drawable such that we don't share the
        // cache bitmap with others. Such that the state change only affect this new cached bitmap.
        if (isStateful()) {
            mutate();
        }
        final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
        if (state.onStateChange(stateSet)) {
            changed = true;
            state.mCacheDirty = true;
        }
        if ((state.mTint != null) && (state.mBlendMode != null)) {
            android.graphics.BlendMode blendMode = state.mBlendMode;
            android.content.res.ColorStateList tint = state.mTint;
            updateColorFilters(blendMode, tint);
            changed = true;
        }
        return changed;
    }

    private void updateColorFilters(@android.annotation.Nullable
    android.graphics.BlendMode blendMode, android.content.res.ColorStateList tint) {
        android.graphics.PorterDuff.Mode mode = android.graphics.BlendMode.blendModeToPorterDuffMode(blendMode);
        mTintFilter = updateTintFilter(mTintFilter, tint, mode);
        mBlendModeColorFilter = updateBlendModeFilter(mBlendModeColorFilter, tint, blendMode);
    }

    @java.lang.Override
    public int getOpacity() {
        // We can't tell whether the drawable is fully opaque unless we examine all the pixels,
        // but we could tell it is transparent if the root alpha is 0.
        return getAlpha() == 0 ? android.graphics.PixelFormat.TRANSPARENT : android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        if (mDpiScaledDirty) {
            computeVectorSize();
        }
        return mDpiScaledWidth;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        if (mDpiScaledDirty) {
            computeVectorSize();
        }
        return mDpiScaledHeight;
    }

    @java.lang.Override
    public android.graphics.Insets getOpticalInsets() {
        if (mDpiScaledDirty) {
            computeVectorSize();
        }
        return mDpiScaledInsets;
    }

    /* Update local dimensions to adjust for a target density that may differ
    from the source density against which the constant state was loaded.
     */
    void computeVectorSize() {
        final android.graphics.Insets opticalInsets = mVectorState.mOpticalInsets;
        final int sourceDensity = mVectorState.mDensity;
        final int targetDensity = mTargetDensity;
        if (targetDensity != sourceDensity) {
            mDpiScaledWidth = android.graphics.drawable.Drawable.scaleFromDensity(mVectorState.mBaseWidth, sourceDensity, targetDensity, true);
            mDpiScaledHeight = android.graphics.drawable.Drawable.scaleFromDensity(mVectorState.mBaseHeight, sourceDensity, targetDensity, true);
            final int left = android.graphics.drawable.Drawable.scaleFromDensity(opticalInsets.left, sourceDensity, targetDensity, false);
            final int right = android.graphics.drawable.Drawable.scaleFromDensity(opticalInsets.right, sourceDensity, targetDensity, false);
            final int top = android.graphics.drawable.Drawable.scaleFromDensity(opticalInsets.top, sourceDensity, targetDensity, false);
            final int bottom = android.graphics.drawable.Drawable.scaleFromDensity(opticalInsets.bottom, sourceDensity, targetDensity, false);
            mDpiScaledInsets = android.graphics.Insets.of(left, top, right, bottom);
        } else {
            mDpiScaledWidth = mVectorState.mBaseWidth;
            mDpiScaledHeight = mVectorState.mBaseHeight;
            mDpiScaledInsets = opticalInsets;
        }
        mDpiScaledDirty = false;
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return ((mVectorState != null) && mVectorState.canApplyTheme()) || super.canApplyTheme();
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
        if (state == null) {
            return;
        }
        final boolean changedDensity = mVectorState.setDensity(android.graphics.drawable.Drawable.resolveDensity(t.getResources(), 0));
        mDpiScaledDirty |= changedDensity;
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.VectorDrawable);
            try {
                state.mCacheDirty = true;
                updateStateFromTypedArray(a);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                throw new java.lang.RuntimeException(e);
            } finally {
                a.recycle();
            }
            // May have changed size.
            mDpiScaledDirty = true;
        }
        // Apply theme to contained color state list.
        if ((state.mTint != null) && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.obtainForTheme(t);
        }
        if ((mVectorState != null) && mVectorState.canApplyTheme()) {
            mVectorState.applyTheme(t);
        }
        // Update local properties.
        updateLocalState(t.getResources());
    }

    /**
     * The size of a pixel when scaled from the intrinsic dimension to the viewport dimension.
     * This is used to calculate the path animation accuracy.
     *
     * @unknown 
     */
    public float getPixelSize() {
        if (((((mVectorState == null) || (mVectorState.mBaseWidth == 0)) || (mVectorState.mBaseHeight == 0)) || (mVectorState.mViewportHeight == 0)) || (mVectorState.mViewportWidth == 0)) {
            return 1;// fall back to 1:1 pixel mapping.

        }
        float intrinsicWidth = mVectorState.mBaseWidth;
        float intrinsicHeight = mVectorState.mBaseHeight;
        float viewportWidth = mVectorState.mViewportWidth;
        float viewportHeight = mVectorState.mViewportHeight;
        float scaleX = viewportWidth / intrinsicWidth;
        float scaleY = viewportHeight / intrinsicHeight;
        return java.lang.Math.min(scaleX, scaleY);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.graphics.drawable.VectorDrawable create(android.content.res.Resources resources, int rid) {
        try {
            final org.xmlpull.v1.XmlPullParser parser = resources.getXml(rid);
            final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                // Empty loop
            } 
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
            }
            final android.graphics.drawable.VectorDrawable drawable = new android.graphics.drawable.VectorDrawable();
            drawable.inflate(resources, parser, attrs);
            return drawable;
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.graphics.drawable.VectorDrawable.LOGTAG, "parser error", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.graphics.drawable.VectorDrawable.LOGTAG, "parser error", e);
        }
        return null;
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        try {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, "VectorDrawable#inflate");
            if ((mVectorState.mRootGroup != null) || (mVectorState.mNativeTree != null)) {
                // This VD has been used to display other VD resource content, clean up.
                if (mVectorState.mRootGroup != null) {
                    // Subtract the native allocation for all the nodes.
                    dalvik.system.VMRuntime.getRuntime().registerNativeFree(mVectorState.mRootGroup.getNativeSize());
                    // Remove child nodes' reference to tree
                    mVectorState.mRootGroup.setTree(null);
                }
                mVectorState.mRootGroup = new android.graphics.drawable.VectorDrawable.VGroup();
                if (mVectorState.mNativeTree != null) {
                    // Subtract the native allocation for the tree wrapper, which contains root node
                    // as well as rendering related data.
                    dalvik.system.VMRuntime.getRuntime().registerNativeFree(mVectorState.NATIVE_ALLOCATION_SIZE);
                    mVectorState.mNativeTree.release();
                }
                mVectorState.createNativeTree(mVectorState.mRootGroup);
            }
            final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
            state.setDensity(android.graphics.drawable.Drawable.resolveDensity(r, 0));
            final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawable);
            updateStateFromTypedArray(a);
            a.recycle();
            mDpiScaledDirty = true;
            state.mCacheDirty = true;
            inflateChildElements(r, parser, attrs, theme);
            state.onTreeConstructionFinished();
            // Update local properties.
            updateLocalState(r);
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        }
    }

    private void updateStateFromTypedArray(android.content.res.TypedArray a) throws org.xmlpull.v1.XmlPullParserException {
        final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        final int tintMode = a.getInt(R.styleable.VectorDrawable_tintMode, -1);
        if (tintMode != (-1)) {
            state.mBlendMode = android.graphics.drawable.Drawable.parseBlendMode(tintMode, android.graphics.BlendMode.SRC_IN);
        }
        final android.content.res.ColorStateList tint = a.getColorStateList(R.styleable.VectorDrawable_tint);
        if (tint != null) {
            state.mTint = tint;
        }
        state.mAutoMirrored = a.getBoolean(R.styleable.VectorDrawable_autoMirrored, state.mAutoMirrored);
        float viewportWidth = a.getFloat(R.styleable.VectorDrawable_viewportWidth, state.mViewportWidth);
        float viewportHeight = a.getFloat(R.styleable.VectorDrawable_viewportHeight, state.mViewportHeight);
        state.setViewportSize(viewportWidth, viewportHeight);
        if (state.mViewportWidth <= 0) {
            throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        } else
            if (state.mViewportHeight <= 0) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
            }

        state.mBaseWidth = a.getDimensionPixelSize(R.styleable.VectorDrawable_width, state.mBaseWidth);
        state.mBaseHeight = a.getDimensionPixelSize(R.styleable.VectorDrawable_height, state.mBaseHeight);
        if (state.mBaseWidth <= 0) {
            throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires width > 0");
        } else
            if (state.mBaseHeight <= 0) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires height > 0");
            }

        final int insetLeft = a.getDimensionPixelOffset(R.styleable.VectorDrawable_opticalInsetLeft, state.mOpticalInsets.left);
        final int insetTop = a.getDimensionPixelOffset(R.styleable.VectorDrawable_opticalInsetTop, state.mOpticalInsets.top);
        final int insetRight = a.getDimensionPixelOffset(R.styleable.VectorDrawable_opticalInsetRight, state.mOpticalInsets.right);
        final int insetBottom = a.getDimensionPixelOffset(R.styleable.VectorDrawable_opticalInsetBottom, state.mOpticalInsets.bottom);
        state.mOpticalInsets = android.graphics.Insets.of(insetLeft, insetTop, insetRight, insetBottom);
        final float alphaInFloat = a.getFloat(R.styleable.VectorDrawable_alpha, state.getAlpha());
        state.setAlpha(alphaInFloat);
        final java.lang.String name = a.getString(R.styleable.VectorDrawable_name);
        if (name != null) {
            state.mRootName = name;
            state.mVGTargetsMap.put(name, state);
        }
    }

    private void inflateChildElements(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.graphics.drawable.VectorDrawable.VectorDrawableState state = mVectorState;
        boolean noPathTag = true;
        // Use a stack to help to build the group tree.
        // The top of the stack is always the current group.
        final java.util.Stack<android.graphics.drawable.VectorDrawable.VGroup> groupStack = new java.util.Stack<android.graphics.drawable.VectorDrawable.VGroup>();
        groupStack.push(state.mRootGroup);
        int eventType = parser.getEventType();
        final int innerDepth = parser.getDepth() + 1;
        // Parse everything until the end of the vector element.
        while ((eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((parser.getDepth() >= innerDepth) || (eventType != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                final java.lang.String tagName = parser.getName();
                final android.graphics.drawable.VectorDrawable.VGroup currentGroup = groupStack.peek();
                if (android.graphics.drawable.VectorDrawable.SHAPE_PATH.equals(tagName)) {
                    final android.graphics.drawable.VectorDrawable.VFullPath path = new android.graphics.drawable.VectorDrawable.VFullPath();
                    path.inflate(res, attrs, theme);
                    currentGroup.addChild(path);
                    if (path.getPathName() != null) {
                        state.mVGTargetsMap.put(path.getPathName(), path);
                    }
                    noPathTag = false;
                    state.mChangingConfigurations |= path.mChangingConfigurations;
                } else
                    if (android.graphics.drawable.VectorDrawable.SHAPE_CLIP_PATH.equals(tagName)) {
                        final android.graphics.drawable.VectorDrawable.VClipPath path = new android.graphics.drawable.VectorDrawable.VClipPath();
                        path.inflate(res, attrs, theme);
                        currentGroup.addChild(path);
                        if (path.getPathName() != null) {
                            state.mVGTargetsMap.put(path.getPathName(), path);
                        }
                        state.mChangingConfigurations |= path.mChangingConfigurations;
                    } else
                        if (android.graphics.drawable.VectorDrawable.SHAPE_GROUP.equals(tagName)) {
                            android.graphics.drawable.VectorDrawable.VGroup newChildGroup = new android.graphics.drawable.VectorDrawable.VGroup();
                            newChildGroup.inflate(res, attrs, theme);
                            currentGroup.addChild(newChildGroup);
                            groupStack.push(newChildGroup);
                            if (newChildGroup.getGroupName() != null) {
                                state.mVGTargetsMap.put(newChildGroup.getGroupName(), newChildGroup);
                            }
                            state.mChangingConfigurations |= newChildGroup.mChangingConfigurations;
                        }


            } else
                if (eventType == org.xmlpull.v1.XmlPullParser.END_TAG) {
                    final java.lang.String tagName = parser.getName();
                    if (android.graphics.drawable.VectorDrawable.SHAPE_GROUP.equals(tagName)) {
                        groupStack.pop();
                    }
                }

            eventType = parser.next();
        } 
        if (noPathTag) {
            final java.lang.StringBuffer tag = new java.lang.StringBuffer();
            if (tag.length() > 0) {
                tag.append(" or ");
            }
            tag.append(android.graphics.drawable.VectorDrawable.SHAPE_PATH);
            throw new org.xmlpull.v1.XmlPullParserException(("no " + tag) + " defined");
        }
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mVectorState.getChangingConfigurations();
    }

    @android.annotation.UnsupportedAppUsage
    void setAllowCaching(boolean allowCaching) {
        android.graphics.drawable.VectorDrawable.nSetAllowCaching(mVectorState.getNativeRenderer(), allowCaching);
    }

    private boolean needMirroring() {
        return isAutoMirrored() && (getLayoutDirection() == android.util.LayoutDirection.RTL);
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        if (mVectorState.mAutoMirrored != mirrored) {
            mVectorState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    @java.lang.Override
    public boolean isAutoMirrored() {
        return mVectorState.mAutoMirrored;
    }

    /**
     *
     *
     * @unknown 
     */
    public long getNativeTree() {
        return mVectorState.getNativeRenderer();
    }

    /**
     *
     *
     * @unknown 
     */
    public void setAntiAlias(boolean aa) {
        android.graphics.drawable.VectorDrawable.nSetAntiAlias(mVectorState.mNativeTree.get(), aa);
    }

    static class VectorDrawableState extends android.graphics.drawable.Drawable.ConstantState {
        // Variables below need to be copied (deep copy if applicable) for mutation.
        int[] mThemeAttrs;

        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        android.content.res.ColorStateList mTint = null;

        android.graphics.BlendMode mBlendMode = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;

        boolean mAutoMirrored;

        int mBaseWidth = 0;

        int mBaseHeight = 0;

        float mViewportWidth = 0;

        float mViewportHeight = 0;

        android.graphics.Insets mOpticalInsets = android.graphics.Insets.NONE;

        java.lang.String mRootName = null;

        android.graphics.drawable.VectorDrawable.VGroup mRootGroup;

        com.android.internal.util.VirtualRefBasePtr mNativeTree = null;

        int mDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;

        final android.util.ArrayMap<java.lang.String, java.lang.Object> mVGTargetsMap = new android.util.ArrayMap();

        // Fields for cache
        int[] mCachedThemeAttrs;

        android.content.res.ColorStateList mCachedTint;

        android.graphics.BlendMode mCachedBlendMode;

        boolean mCachedAutoMirrored;

        boolean mCacheDirty;

        // Since sw canvas and hw canvas uses different bitmap caches, we track the allocation of
        // these bitmaps separately.
        int mLastSWCachePixelCount = 0;

        int mLastHWCachePixelCount = 0;

        static final android.util.Property<android.graphics.drawable.VectorDrawable.VectorDrawableState, java.lang.Float> ALPHA = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VectorDrawableState>("alpha") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VectorDrawableState state, float value) {
                state.setAlpha(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VectorDrawableState state) {
                return state.getAlpha();
            }
        };

        android.util.Property getProperty(java.lang.String propertyName) {
            if (android.graphics.drawable.VectorDrawable.VectorDrawableState.ALPHA.getName().equals(propertyName)) {
                return android.graphics.drawable.VectorDrawable.VectorDrawableState.ALPHA;
            }
            return null;
        }

        // This tracks the total native allocation for all the nodes.
        private int mAllocationOfAllNodes = 0;

        private static final int NATIVE_ALLOCATION_SIZE = 316;

        // If copy is not null, deep copy the given VectorDrawableState. Otherwise, create a
        // native vector drawable tree with an empty root group.
        public VectorDrawableState(android.graphics.drawable.VectorDrawable.VectorDrawableState copy) {
            if (copy != null) {
                mThemeAttrs = copy.mThemeAttrs;
                mChangingConfigurations = copy.mChangingConfigurations;
                mTint = copy.mTint;
                mBlendMode = copy.mBlendMode;
                mAutoMirrored = copy.mAutoMirrored;
                mRootGroup = new android.graphics.drawable.VectorDrawable.VGroup(copy.mRootGroup, mVGTargetsMap);
                createNativeTreeFromCopy(copy, mRootGroup);
                mBaseWidth = copy.mBaseWidth;
                mBaseHeight = copy.mBaseHeight;
                setViewportSize(copy.mViewportWidth, copy.mViewportHeight);
                mOpticalInsets = copy.mOpticalInsets;
                mRootName = copy.mRootName;
                mDensity = copy.mDensity;
                if (copy.mRootName != null) {
                    mVGTargetsMap.put(copy.mRootName, this);
                }
            } else {
                mRootGroup = new android.graphics.drawable.VectorDrawable.VGroup();
                createNativeTree(mRootGroup);
            }
            onTreeConstructionFinished();
        }

        private void createNativeTree(android.graphics.drawable.VectorDrawable.VGroup rootGroup) {
            mNativeTree = new com.android.internal.util.VirtualRefBasePtr(android.graphics.drawable.VectorDrawable.nCreateTree(rootGroup.mNativePtr));
            // Register tree size
            dalvik.system.VMRuntime.getRuntime().registerNativeAllocation(android.graphics.drawable.VectorDrawable.VectorDrawableState.NATIVE_ALLOCATION_SIZE);
        }

        // Create a new native tree with the given root group, and copy the properties from the
        // given VectorDrawableState's native tree.
        private void createNativeTreeFromCopy(android.graphics.drawable.VectorDrawable.VectorDrawableState copy, android.graphics.drawable.VectorDrawable.VGroup rootGroup) {
            mNativeTree = new com.android.internal.util.VirtualRefBasePtr(android.graphics.drawable.VectorDrawable.nCreateTreeFromCopy(copy.mNativeTree.get(), rootGroup.mNativePtr));
            // Register tree size
            dalvik.system.VMRuntime.getRuntime().registerNativeAllocation(android.graphics.drawable.VectorDrawable.VectorDrawableState.NATIVE_ALLOCATION_SIZE);
        }

        // This should be called every time after a new RootGroup and all its subtrees are created
        // (i.e. in constructors of VectorDrawableState and in inflate).
        void onTreeConstructionFinished() {
            mRootGroup.setTree(mNativeTree);
            mAllocationOfAllNodes = mRootGroup.getNativeSize();
            dalvik.system.VMRuntime.getRuntime().registerNativeAllocation(mAllocationOfAllNodes);
        }

        long getNativeRenderer() {
            if (mNativeTree == null) {
                return 0;
            }
            return mNativeTree.get();
        }

        public boolean canReuseCache() {
            if (((((!mCacheDirty) && (mCachedThemeAttrs == mThemeAttrs)) && (mCachedTint == mTint)) && (mCachedBlendMode == mBlendMode)) && (mCachedAutoMirrored == mAutoMirrored)) {
                return true;
            }
            updateCacheStates();
            return false;
        }

        public void updateCacheStates() {
            // Use shallow copy here and shallow comparison in canReuseCache(),
            // likely hit cache miss more, but practically not much difference.
            mCachedThemeAttrs = mThemeAttrs;
            mCachedTint = mTint;
            mCachedBlendMode = mBlendMode;
            mCachedAutoMirrored = mAutoMirrored;
            mCacheDirty = false;
        }

        public void applyTheme(android.content.res.Resources.Theme t) {
            mRootGroup.applyTheme(t);
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return (((mThemeAttrs != null) || ((mRootGroup != null) && mRootGroup.canApplyTheme())) || ((mTint != null) && mTint.canApplyTheme())) || super.canApplyTheme();
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.VectorDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.VectorDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations | (mTint != null ? mTint.getChangingConfigurations() : 0);
        }

        public boolean isStateful() {
            return ((mTint != null) && mTint.isStateful()) || ((mRootGroup != null) && mRootGroup.isStateful());
        }

        public boolean hasFocusStateSpecified() {
            return ((mTint != null) && mTint.hasFocusStateSpecified()) || ((mRootGroup != null) && mRootGroup.hasFocusStateSpecified());
        }

        void setViewportSize(float viewportWidth, float viewportHeight) {
            mViewportWidth = viewportWidth;
            mViewportHeight = viewportHeight;
            android.graphics.drawable.VectorDrawable.nSetRendererViewportSize(getNativeRenderer(), viewportWidth, viewportHeight);
        }

        public final boolean setDensity(int targetDensity) {
            if (mDensity != targetDensity) {
                final int sourceDensity = mDensity;
                mDensity = targetDensity;
                applyDensityScaling(sourceDensity, targetDensity);
                return true;
            }
            return false;
        }

        private void applyDensityScaling(int sourceDensity, int targetDensity) {
            mBaseWidth = android.graphics.drawable.Drawable.scaleFromDensity(mBaseWidth, sourceDensity, targetDensity, true);
            mBaseHeight = android.graphics.drawable.Drawable.scaleFromDensity(mBaseHeight, sourceDensity, targetDensity, true);
            final int insetLeft = android.graphics.drawable.Drawable.scaleFromDensity(mOpticalInsets.left, sourceDensity, targetDensity, false);
            final int insetTop = android.graphics.drawable.Drawable.scaleFromDensity(mOpticalInsets.top, sourceDensity, targetDensity, false);
            final int insetRight = android.graphics.drawable.Drawable.scaleFromDensity(mOpticalInsets.right, sourceDensity, targetDensity, false);
            final int insetBottom = android.graphics.drawable.Drawable.scaleFromDensity(mOpticalInsets.bottom, sourceDensity, targetDensity, false);
            mOpticalInsets = android.graphics.Insets.of(insetLeft, insetTop, insetRight, insetBottom);
        }

        public boolean onStateChange(int[] stateSet) {
            return mRootGroup.onStateChange(stateSet);
        }

        @java.lang.Override
        public void finalize() throws java.lang.Throwable {
            super.finalize();
            int bitmapCacheSize = (mLastHWCachePixelCount * 4) + (mLastSWCachePixelCount * 4);
            dalvik.system.VMRuntime.getRuntime().registerNativeFree((android.graphics.drawable.VectorDrawable.VectorDrawableState.NATIVE_ALLOCATION_SIZE + mAllocationOfAllNodes) + bitmapCacheSize);
        }

        /**
         * setAlpha() and getAlpha() are used mostly for animation purpose. Return true if alpha
         * has changed.
         */
        public boolean setAlpha(float alpha) {
            return android.graphics.drawable.VectorDrawable.nSetRootAlpha(mNativeTree.get(), alpha);
        }

        @java.lang.SuppressWarnings("unused")
        public float getAlpha() {
            return android.graphics.drawable.VectorDrawable.nGetRootAlpha(mNativeTree.get());
        }
    }

    static class VGroup extends android.graphics.drawable.VectorDrawable.VObject {
        private static final int ROTATION_INDEX = 0;

        private static final int PIVOT_X_INDEX = 1;

        private static final int PIVOT_Y_INDEX = 2;

        private static final int SCALE_X_INDEX = 3;

        private static final int SCALE_Y_INDEX = 4;

        private static final int TRANSLATE_X_INDEX = 5;

        private static final int TRANSLATE_Y_INDEX = 6;

        private static final int TRANSFORM_PROPERTY_COUNT = 7;

        private static final int NATIVE_ALLOCATION_SIZE = 100;

        private static final java.util.HashMap<java.lang.String, java.lang.Integer> sPropertyIndexMap = new java.util.HashMap<java.lang.String, java.lang.Integer>() {
            {
                put("translateX", android.graphics.drawable.VectorDrawable.VGroup.TRANSLATE_X_INDEX);
                put("translateY", android.graphics.drawable.VectorDrawable.VGroup.TRANSLATE_Y_INDEX);
                put("scaleX", android.graphics.drawable.VectorDrawable.VGroup.SCALE_X_INDEX);
                put("scaleY", android.graphics.drawable.VectorDrawable.VGroup.SCALE_Y_INDEX);
                put("pivotX", android.graphics.drawable.VectorDrawable.VGroup.PIVOT_X_INDEX);
                put("pivotY", android.graphics.drawable.VectorDrawable.VGroup.PIVOT_Y_INDEX);
                put("rotation", android.graphics.drawable.VectorDrawable.VGroup.ROTATION_INDEX);
            }
        };

        static int getPropertyIndex(java.lang.String propertyName) {
            if (android.graphics.drawable.VectorDrawable.VGroup.sPropertyIndexMap.containsKey(propertyName)) {
                return android.graphics.drawable.VectorDrawable.VGroup.sPropertyIndexMap.get(propertyName);
            } else {
                // property not found
                return -1;
            }
        }

        // Below are the Properties that wrap the setters to avoid reflection overhead in animations
        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> TRANSLATE_X = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("translateX") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setTranslateX(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getTranslateX();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> TRANSLATE_Y = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("translateY") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setTranslateY(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getTranslateY();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> SCALE_X = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("scaleX") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setScaleX(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getScaleX();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> SCALE_Y = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("scaleY") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setScaleY(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getScaleY();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> PIVOT_X = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("pivotX") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setPivotX(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getPivotX();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> PIVOT_Y = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("pivotY") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setPivotY(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getPivotY();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VGroup, java.lang.Float> ROTATION = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VGroup>("rotation") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VGroup object, float value) {
                object.setRotation(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VGroup object) {
                return object.getRotation();
            }
        };

        private static final java.util.HashMap<java.lang.String, android.util.Property> sPropertyMap = new java.util.HashMap<java.lang.String, android.util.Property>() {
            {
                put("translateX", android.graphics.drawable.VectorDrawable.VGroup.TRANSLATE_X);
                put("translateY", android.graphics.drawable.VectorDrawable.VGroup.TRANSLATE_Y);
                put("scaleX", android.graphics.drawable.VectorDrawable.VGroup.SCALE_X);
                put("scaleY", android.graphics.drawable.VectorDrawable.VGroup.SCALE_Y);
                put("pivotX", android.graphics.drawable.VectorDrawable.VGroup.PIVOT_X);
                put("pivotY", android.graphics.drawable.VectorDrawable.VGroup.PIVOT_Y);
                put("rotation", android.graphics.drawable.VectorDrawable.VGroup.ROTATION);
            }
        };

        // Temp array to store transform values obtained from native.
        private float[] mTransform;

        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        private final java.util.ArrayList<android.graphics.drawable.VectorDrawable.VObject> mChildren = new java.util.ArrayList<>();

        private boolean mIsStateful;

        // mLocalMatrix is updated based on the update of transformation information,
        // either parsed from the XML or by animation.
        @android.content.pm.ActivityInfo.Config
        private int mChangingConfigurations;

        private int[] mThemeAttrs;

        private java.lang.String mGroupName = null;

        // The native object will be created in the constructor and will be destroyed in native
        // when the neither java nor native has ref to the tree. This pointer should be valid
        // throughout this VGroup Java object's life.
        private final long mNativePtr;

        public VGroup(android.graphics.drawable.VectorDrawable.VGroup copy, android.util.ArrayMap<java.lang.String, java.lang.Object> targetsMap) {
            mIsStateful = copy.mIsStateful;
            mThemeAttrs = copy.mThemeAttrs;
            mGroupName = copy.mGroupName;
            mChangingConfigurations = copy.mChangingConfigurations;
            if (mGroupName != null) {
                targetsMap.put(mGroupName, this);
            }
            mNativePtr = android.graphics.drawable.VectorDrawable.nCreateGroup(copy.mNativePtr);
            final java.util.ArrayList<android.graphics.drawable.VectorDrawable.VObject> children = copy.mChildren;
            for (int i = 0; i < children.size(); i++) {
                final android.graphics.drawable.VectorDrawable.VObject copyChild = children.get(i);
                if (copyChild instanceof android.graphics.drawable.VectorDrawable.VGroup) {
                    final android.graphics.drawable.VectorDrawable.VGroup copyGroup = ((android.graphics.drawable.VectorDrawable.VGroup) (copyChild));
                    addChild(new android.graphics.drawable.VectorDrawable.VGroup(copyGroup, targetsMap));
                } else {
                    final android.graphics.drawable.VectorDrawable.VPath newPath;
                    if (copyChild instanceof android.graphics.drawable.VectorDrawable.VFullPath) {
                        newPath = new android.graphics.drawable.VectorDrawable.VFullPath(((android.graphics.drawable.VectorDrawable.VFullPath) (copyChild)));
                    } else
                        if (copyChild instanceof android.graphics.drawable.VectorDrawable.VClipPath) {
                            newPath = new android.graphics.drawable.VectorDrawable.VClipPath(((android.graphics.drawable.VectorDrawable.VClipPath) (copyChild)));
                        } else {
                            throw new java.lang.IllegalStateException("Unknown object in the tree!");
                        }

                    addChild(newPath);
                    if (newPath.mPathName != null) {
                        targetsMap.put(newPath.mPathName, newPath);
                    }
                }
            }
        }

        public VGroup() {
            mNativePtr = android.graphics.drawable.VectorDrawable.nCreateGroup();
        }

        android.util.Property getProperty(java.lang.String propertyName) {
            if (android.graphics.drawable.VectorDrawable.VGroup.sPropertyMap.containsKey(propertyName)) {
                return android.graphics.drawable.VectorDrawable.VGroup.sPropertyMap.get(propertyName);
            } else {
                // property not found
                return null;
            }
        }

        public java.lang.String getGroupName() {
            return mGroupName;
        }

        public void addChild(android.graphics.drawable.VectorDrawable.VObject child) {
            android.graphics.drawable.VectorDrawable.nAddChild(mNativePtr, child.getNativePtr());
            mChildren.add(child);
            mIsStateful |= child.isStateful();
        }

        @java.lang.Override
        public void setTree(com.android.internal.util.VirtualRefBasePtr treeRoot) {
            super.setTree(treeRoot);
            for (int i = 0; i < mChildren.size(); i++) {
                mChildren.get(i).setTree(treeRoot);
            }
        }

        @java.lang.Override
        public long getNativePtr() {
            return mNativePtr;
        }

        @java.lang.Override
        public void inflate(android.content.res.Resources res, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) {
            final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(res, theme, attrs, R.styleable.VectorDrawableGroup);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        void updateStateFromTypedArray(android.content.res.TypedArray a) {
            // Account for any configuration changes.
            mChangingConfigurations |= a.getChangingConfigurations();
            // Extract the theme attributes, if any.
            mThemeAttrs = a.extractThemeAttrs();
            if (mTransform == null) {
                // Lazy initialization: If the group is created through copy constructor, this may
                // never get called.
                mTransform = new float[android.graphics.drawable.VectorDrawable.VGroup.TRANSFORM_PROPERTY_COUNT];
            }
            boolean success = android.graphics.drawable.VectorDrawable.nGetGroupProperties(mNativePtr, mTransform, android.graphics.drawable.VectorDrawable.VGroup.TRANSFORM_PROPERTY_COUNT);
            if (!success) {
                throw new java.lang.RuntimeException("Error: inconsistent property count");
            }
            float rotate = a.getFloat(R.styleable.VectorDrawableGroup_rotation, mTransform[android.graphics.drawable.VectorDrawable.VGroup.ROTATION_INDEX]);
            float pivotX = a.getFloat(R.styleable.VectorDrawableGroup_pivotX, mTransform[android.graphics.drawable.VectorDrawable.VGroup.PIVOT_X_INDEX]);
            float pivotY = a.getFloat(R.styleable.VectorDrawableGroup_pivotY, mTransform[android.graphics.drawable.VectorDrawable.VGroup.PIVOT_Y_INDEX]);
            float scaleX = a.getFloat(R.styleable.VectorDrawableGroup_scaleX, mTransform[android.graphics.drawable.VectorDrawable.VGroup.SCALE_X_INDEX]);
            float scaleY = a.getFloat(R.styleable.VectorDrawableGroup_scaleY, mTransform[android.graphics.drawable.VectorDrawable.VGroup.SCALE_Y_INDEX]);
            float translateX = a.getFloat(R.styleable.VectorDrawableGroup_translateX, mTransform[android.graphics.drawable.VectorDrawable.VGroup.TRANSLATE_X_INDEX]);
            float translateY = a.getFloat(R.styleable.VectorDrawableGroup_translateY, mTransform[android.graphics.drawable.VectorDrawable.VGroup.TRANSLATE_Y_INDEX]);
            final java.lang.String groupName = a.getString(R.styleable.VectorDrawableGroup_name);
            if (groupName != null) {
                mGroupName = groupName;
                android.graphics.drawable.VectorDrawable.nSetName(mNativePtr, mGroupName);
            }
            android.graphics.drawable.VectorDrawable.nUpdateGroupProperties(mNativePtr, rotate, pivotX, pivotY, scaleX, scaleY, translateX, translateY);
        }

        @java.lang.Override
        public boolean onStateChange(int[] stateSet) {
            boolean changed = false;
            final java.util.ArrayList<android.graphics.drawable.VectorDrawable.VObject> children = mChildren;
            for (int i = 0, count = children.size(); i < count; i++) {
                final android.graphics.drawable.VectorDrawable.VObject child = children.get(i);
                if (child.isStateful()) {
                    changed |= child.onStateChange(stateSet);
                }
            }
            return changed;
        }

        @java.lang.Override
        public boolean isStateful() {
            return mIsStateful;
        }

        @java.lang.Override
        public boolean hasFocusStateSpecified() {
            boolean result = false;
            final java.util.ArrayList<android.graphics.drawable.VectorDrawable.VObject> children = mChildren;
            for (int i = 0, count = children.size(); i < count; i++) {
                final android.graphics.drawable.VectorDrawable.VObject child = children.get(i);
                if (child.isStateful()) {
                    result |= child.hasFocusStateSpecified();
                }
            }
            return result;
        }

        @java.lang.Override
        int getNativeSize() {
            // Return the native allocation needed for the subtree.
            int size = android.graphics.drawable.VectorDrawable.VGroup.NATIVE_ALLOCATION_SIZE;
            for (int i = 0; i < mChildren.size(); i++) {
                size += mChildren.get(i).getNativeSize();
            }
            return size;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            if (mThemeAttrs != null) {
                return true;
            }
            final java.util.ArrayList<android.graphics.drawable.VectorDrawable.VObject> children = mChildren;
            for (int i = 0, count = children.size(); i < count; i++) {
                final android.graphics.drawable.VectorDrawable.VObject child = children.get(i);
                if (child.canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        @java.lang.Override
        public void applyTheme(android.content.res.Resources.Theme t) {
            if (mThemeAttrs != null) {
                final android.content.res.TypedArray a = t.resolveAttributes(mThemeAttrs, R.styleable.VectorDrawableGroup);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            final java.util.ArrayList<android.graphics.drawable.VectorDrawable.VObject> children = mChildren;
            for (int i = 0, count = children.size(); i < count; i++) {
                final android.graphics.drawable.VectorDrawable.VObject child = children.get(i);
                if (child.canApplyTheme()) {
                    child.applyTheme(t);
                    // Applying a theme may have made the child stateful.
                    mIsStateful |= child.isStateful();
                }
            }
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        @java.lang.SuppressWarnings("unused")
        public float getRotation() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetRotation(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        @android.annotation.UnsupportedAppUsage
        public void setRotation(float rotation) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetRotation(mNativePtr, rotation);
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getPivotX() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetPivotX(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        @android.annotation.UnsupportedAppUsage
        public void setPivotX(float pivotX) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetPivotX(mNativePtr, pivotX);
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getPivotY() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetPivotY(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        @android.annotation.UnsupportedAppUsage
        public void setPivotY(float pivotY) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetPivotY(mNativePtr, pivotY);
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getScaleX() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetScaleX(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        public void setScaleX(float scaleX) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetScaleX(mNativePtr, scaleX);
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getScaleY() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetScaleY(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        public void setScaleY(float scaleY) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetScaleY(mNativePtr, scaleY);
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getTranslateX() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetTranslateX(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        @android.annotation.UnsupportedAppUsage
        public void setTranslateX(float translateX) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetTranslateX(mNativePtr, translateX);
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getTranslateY() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetTranslateY(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        @android.annotation.UnsupportedAppUsage
        public void setTranslateY(float translateY) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetTranslateY(mNativePtr, translateY);
            }
        }
    }

    /**
     * Common Path information for clip path and normal path.
     */
    static abstract class VPath extends android.graphics.drawable.VectorDrawable.VObject {
        protected PathParser.PathData mPathData = null;

        java.lang.String mPathName;

        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VPath, android.util.PathParser.PathData> PATH_DATA = new android.util.Property<android.graphics.drawable.VectorDrawable.VPath, android.util.PathParser.PathData>(PathParser.PathData.class, "pathData") {
            @java.lang.Override
            public void set(android.graphics.drawable.VectorDrawable.VPath object, android.util.PathParser.PathData data) {
                object.setPathData(data);
            }

            @java.lang.Override
            public PathParser.PathData get(android.graphics.drawable.VectorDrawable.VPath object) {
                return object.getPathData();
            }
        };

        android.util.Property getProperty(java.lang.String propertyName) {
            if (android.graphics.drawable.VectorDrawable.VPath.PATH_DATA.getName().equals(propertyName)) {
                return android.graphics.drawable.VectorDrawable.VPath.PATH_DATA;
            }
            // property not found
            return null;
        }

        public VPath() {
            // Empty constructor.
        }

        public VPath(android.graphics.drawable.VectorDrawable.VPath copy) {
            mPathName = copy.mPathName;
            mChangingConfigurations = copy.mChangingConfigurations;
            mPathData = (copy.mPathData == null) ? null : new android.util.PathParser.PathData(copy.mPathData);
        }

        public java.lang.String getPathName() {
            return mPathName;
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        @java.lang.SuppressWarnings("unused")
        public PathParser.PathData getPathData() {
            return mPathData;
        }

        // TODO: Move the PathEvaluator and this setter and the getter above into native.
        @java.lang.SuppressWarnings("unused")
        public void setPathData(android.util.PathParser.PathData pathData) {
            mPathData.setPathData(pathData);
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetPathData(getNativePtr(), mPathData.getNativePtr());
            }
        }
    }

    /**
     * Clip path, which only has name and pathData.
     */
    private static class VClipPath extends android.graphics.drawable.VectorDrawable.VPath {
        private final long mNativePtr;

        private static final int NATIVE_ALLOCATION_SIZE = 120;

        public VClipPath() {
            mNativePtr = android.graphics.drawable.VectorDrawable.nCreateClipPath();
        }

        public VClipPath(android.graphics.drawable.VectorDrawable.VClipPath copy) {
            super(copy);
            mNativePtr = android.graphics.drawable.VectorDrawable.nCreateClipPath(copy.mNativePtr);
        }

        @java.lang.Override
        public long getNativePtr() {
            return mNativePtr;
        }

        @java.lang.Override
        public void inflate(android.content.res.Resources r, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) {
            final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawableClipPath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return false;
        }

        @java.lang.Override
        public void applyTheme(android.content.res.Resources.Theme theme) {
            // No-op.
        }

        @java.lang.Override
        public boolean onStateChange(int[] stateSet) {
            return false;
        }

        @java.lang.Override
        public boolean isStateful() {
            return false;
        }

        @java.lang.Override
        public boolean hasFocusStateSpecified() {
            return false;
        }

        @java.lang.Override
        int getNativeSize() {
            return android.graphics.drawable.VectorDrawable.VClipPath.NATIVE_ALLOCATION_SIZE;
        }

        private void updateStateFromTypedArray(android.content.res.TypedArray a) {
            // Account for any configuration changes.
            mChangingConfigurations |= a.getChangingConfigurations();
            final java.lang.String pathName = a.getString(R.styleable.VectorDrawableClipPath_name);
            if (pathName != null) {
                mPathName = pathName;
                android.graphics.drawable.VectorDrawable.nSetName(mNativePtr, mPathName);
            }
            final java.lang.String pathDataString = a.getString(R.styleable.VectorDrawableClipPath_pathData);
            if (pathDataString != null) {
                mPathData = new android.util.PathParser.PathData(pathDataString);
                android.graphics.drawable.VectorDrawable.nSetPathString(mNativePtr, pathDataString, pathDataString.length());
            }
        }
    }

    /**
     * Normal path, which contains all the fill / paint information.
     */
    static class VFullPath extends android.graphics.drawable.VectorDrawable.VPath {
        private static final int STROKE_WIDTH_INDEX = 0;

        private static final int STROKE_COLOR_INDEX = 1;

        private static final int STROKE_ALPHA_INDEX = 2;

        private static final int FILL_COLOR_INDEX = 3;

        private static final int FILL_ALPHA_INDEX = 4;

        private static final int TRIM_PATH_START_INDEX = 5;

        private static final int TRIM_PATH_END_INDEX = 6;

        private static final int TRIM_PATH_OFFSET_INDEX = 7;

        private static final int STROKE_LINE_CAP_INDEX = 8;

        private static final int STROKE_LINE_JOIN_INDEX = 9;

        private static final int STROKE_MITER_LIMIT_INDEX = 10;

        private static final int FILL_TYPE_INDEX = 11;

        private static final int TOTAL_PROPERTY_COUNT = 12;

        private static final int NATIVE_ALLOCATION_SIZE = 264;

        // Property map for animatable attributes.
        private static final java.util.HashMap<java.lang.String, java.lang.Integer> sPropertyIndexMap = new java.util.HashMap<java.lang.String, java.lang.Integer>() {
            {
                put("strokeWidth", android.graphics.drawable.VectorDrawable.VFullPath.STROKE_WIDTH_INDEX);
                put("strokeColor", android.graphics.drawable.VectorDrawable.VFullPath.STROKE_COLOR_INDEX);
                put("strokeAlpha", android.graphics.drawable.VectorDrawable.VFullPath.STROKE_ALPHA_INDEX);
                put("fillColor", android.graphics.drawable.VectorDrawable.VFullPath.FILL_COLOR_INDEX);
                put("fillAlpha", android.graphics.drawable.VectorDrawable.VFullPath.FILL_ALPHA_INDEX);
                put("trimPathStart", android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_START_INDEX);
                put("trimPathEnd", android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_END_INDEX);
                put("trimPathOffset", android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_OFFSET_INDEX);
            }
        };

        // Below are the Properties that wrap the setters to avoid reflection overhead in animations
        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Float> STROKE_WIDTH = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VFullPath>("strokeWidth") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, float value) {
                object.setStrokeWidth(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getStrokeWidth();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Integer> STROKE_COLOR = new android.util.IntProperty<android.graphics.drawable.VectorDrawable.VFullPath>("strokeColor") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, int value) {
                object.setStrokeColor(value);
            }

            @java.lang.Override
            public java.lang.Integer get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getStrokeColor();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Float> STROKE_ALPHA = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VFullPath>("strokeAlpha") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, float value) {
                object.setStrokeAlpha(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getStrokeAlpha();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Integer> FILL_COLOR = new android.util.IntProperty<android.graphics.drawable.VectorDrawable.VFullPath>("fillColor") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, int value) {
                object.setFillColor(value);
            }

            @java.lang.Override
            public java.lang.Integer get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getFillColor();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Float> FILL_ALPHA = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VFullPath>("fillAlpha") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, float value) {
                object.setFillAlpha(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getFillAlpha();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Float> TRIM_PATH_START = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VFullPath>("trimPathStart") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, float value) {
                object.setTrimPathStart(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getTrimPathStart();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Float> TRIM_PATH_END = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VFullPath>("trimPathEnd") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, float value) {
                object.setTrimPathEnd(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getTrimPathEnd();
            }
        };

        private static final android.util.Property<android.graphics.drawable.VectorDrawable.VFullPath, java.lang.Float> TRIM_PATH_OFFSET = new android.util.FloatProperty<android.graphics.drawable.VectorDrawable.VFullPath>("trimPathOffset") {
            @java.lang.Override
            public void setValue(android.graphics.drawable.VectorDrawable.VFullPath object, float value) {
                object.setTrimPathOffset(value);
            }

            @java.lang.Override
            public java.lang.Float get(android.graphics.drawable.VectorDrawable.VFullPath object) {
                return object.getTrimPathOffset();
            }
        };

        private static final java.util.HashMap<java.lang.String, android.util.Property> sPropertyMap = new java.util.HashMap<java.lang.String, android.util.Property>() {
            {
                put("strokeWidth", android.graphics.drawable.VectorDrawable.VFullPath.STROKE_WIDTH);
                put("strokeColor", android.graphics.drawable.VectorDrawable.VFullPath.STROKE_COLOR);
                put("strokeAlpha", android.graphics.drawable.VectorDrawable.VFullPath.STROKE_ALPHA);
                put("fillColor", android.graphics.drawable.VectorDrawable.VFullPath.FILL_COLOR);
                put("fillAlpha", android.graphics.drawable.VectorDrawable.VFullPath.FILL_ALPHA);
                put("trimPathStart", android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_START);
                put("trimPathEnd", android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_END);
                put("trimPathOffset", android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_OFFSET);
            }
        };

        // Temp array to store property data obtained from native getter.
        private byte[] mPropertyData;

        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        private int[] mThemeAttrs;

        android.content.res.ComplexColor mStrokeColors = null;

        android.content.res.ComplexColor mFillColors = null;

        private final long mNativePtr;

        public VFullPath() {
            mNativePtr = android.graphics.drawable.VectorDrawable.nCreateFullPath();
        }

        public VFullPath(android.graphics.drawable.VectorDrawable.VFullPath copy) {
            super(copy);
            mNativePtr = android.graphics.drawable.VectorDrawable.nCreateFullPath(copy.mNativePtr);
            mThemeAttrs = copy.mThemeAttrs;
            mStrokeColors = copy.mStrokeColors;
            mFillColors = copy.mFillColors;
        }

        android.util.Property getProperty(java.lang.String propertyName) {
            android.util.Property p = super.getProperty(propertyName);
            if (p != null) {
                return p;
            }
            if (android.graphics.drawable.VectorDrawable.VFullPath.sPropertyMap.containsKey(propertyName)) {
                return android.graphics.drawable.VectorDrawable.VFullPath.sPropertyMap.get(propertyName);
            } else {
                // property not found
                return null;
            }
        }

        int getPropertyIndex(java.lang.String propertyName) {
            if (!android.graphics.drawable.VectorDrawable.VFullPath.sPropertyIndexMap.containsKey(propertyName)) {
                return -1;
            } else {
                return android.graphics.drawable.VectorDrawable.VFullPath.sPropertyIndexMap.get(propertyName);
            }
        }

        @java.lang.Override
        public boolean onStateChange(int[] stateSet) {
            boolean changed = false;
            if ((mStrokeColors != null) && (mStrokeColors instanceof android.content.res.ColorStateList)) {
                final int oldStrokeColor = getStrokeColor();
                final int newStrokeColor = ((android.content.res.ColorStateList) (mStrokeColors)).getColorForState(stateSet, oldStrokeColor);
                changed |= oldStrokeColor != newStrokeColor;
                if (oldStrokeColor != newStrokeColor) {
                    android.graphics.drawable.VectorDrawable.nSetStrokeColor(mNativePtr, newStrokeColor);
                }
            }
            if ((mFillColors != null) && (mFillColors instanceof android.content.res.ColorStateList)) {
                final int oldFillColor = getFillColor();
                final int newFillColor = ((android.content.res.ColorStateList) (mFillColors)).getColorForState(stateSet, oldFillColor);
                changed |= oldFillColor != newFillColor;
                if (oldFillColor != newFillColor) {
                    android.graphics.drawable.VectorDrawable.nSetFillColor(mNativePtr, newFillColor);
                }
            }
            return changed;
        }

        @java.lang.Override
        public boolean isStateful() {
            return (mStrokeColors != null) || (mFillColors != null);
        }

        @java.lang.Override
        public boolean hasFocusStateSpecified() {
            return (((mStrokeColors != null) && (mStrokeColors instanceof android.content.res.ColorStateList)) && ((android.content.res.ColorStateList) (mStrokeColors)).hasFocusStateSpecified()) && (((mFillColors != null) && (mFillColors instanceof android.content.res.ColorStateList)) && ((android.content.res.ColorStateList) (mFillColors)).hasFocusStateSpecified());
        }

        @java.lang.Override
        int getNativeSize() {
            return android.graphics.drawable.VectorDrawable.VFullPath.NATIVE_ALLOCATION_SIZE;
        }

        @java.lang.Override
        public long getNativePtr() {
            return mNativePtr;
        }

        @java.lang.Override
        public void inflate(android.content.res.Resources r, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) {
            final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawablePath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        private void updateStateFromTypedArray(android.content.res.TypedArray a) {
            int byteCount = android.graphics.drawable.VectorDrawable.VFullPath.TOTAL_PROPERTY_COUNT * 4;
            if (mPropertyData == null) {
                // Lazy initialization: If the path is created through copy constructor, this may
                // never get called.
                mPropertyData = new byte[byteCount];
            }
            // The bulk getters/setters of property data (e.g. stroke width, color, etc) allows us
            // to pull current values from native and store modifications with only two methods,
            // minimizing JNI overhead.
            boolean success = android.graphics.drawable.VectorDrawable.nGetFullPathProperties(mNativePtr, mPropertyData, byteCount);
            if (!success) {
                throw new java.lang.RuntimeException("Error: inconsistent property count");
            }
            java.nio.ByteBuffer properties = java.nio.ByteBuffer.wrap(mPropertyData);
            properties.order(java.nio.ByteOrder.nativeOrder());
            float strokeWidth = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.STROKE_WIDTH_INDEX * 4);
            int strokeColor = properties.getInt(android.graphics.drawable.VectorDrawable.VFullPath.STROKE_COLOR_INDEX * 4);
            float strokeAlpha = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.STROKE_ALPHA_INDEX * 4);
            int fillColor = properties.getInt(android.graphics.drawable.VectorDrawable.VFullPath.FILL_COLOR_INDEX * 4);
            float fillAlpha = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.FILL_ALPHA_INDEX * 4);
            float trimPathStart = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_START_INDEX * 4);
            float trimPathEnd = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_END_INDEX * 4);
            float trimPathOffset = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.TRIM_PATH_OFFSET_INDEX * 4);
            int strokeLineCap = properties.getInt(android.graphics.drawable.VectorDrawable.VFullPath.STROKE_LINE_CAP_INDEX * 4);
            int strokeLineJoin = properties.getInt(android.graphics.drawable.VectorDrawable.VFullPath.STROKE_LINE_JOIN_INDEX * 4);
            float strokeMiterLimit = properties.getFloat(android.graphics.drawable.VectorDrawable.VFullPath.STROKE_MITER_LIMIT_INDEX * 4);
            int fillType = properties.getInt(android.graphics.drawable.VectorDrawable.VFullPath.FILL_TYPE_INDEX * 4);
            android.graphics.Shader fillGradient = null;
            android.graphics.Shader strokeGradient = null;
            // Account for any configuration changes.
            mChangingConfigurations |= a.getChangingConfigurations();
            // Extract the theme attributes, if any.
            mThemeAttrs = a.extractThemeAttrs();
            final java.lang.String pathName = a.getString(R.styleable.VectorDrawablePath_name);
            if (pathName != null) {
                mPathName = pathName;
                android.graphics.drawable.VectorDrawable.nSetName(mNativePtr, mPathName);
            }
            final java.lang.String pathString = a.getString(R.styleable.VectorDrawablePath_pathData);
            if (pathString != null) {
                mPathData = new android.util.PathParser.PathData(pathString);
                android.graphics.drawable.VectorDrawable.nSetPathString(mNativePtr, pathString, pathString.length());
            }
            final android.content.res.ComplexColor fillColors = a.getComplexColor(R.styleable.VectorDrawablePath_fillColor);
            if (fillColors != null) {
                // If the colors is a gradient color, or the color state list is stateful, keep the
                // colors information. Otherwise, discard the colors and keep the default color.
                if (fillColors instanceof android.content.res.GradientColor) {
                    mFillColors = fillColors;
                    fillGradient = ((android.content.res.GradientColor) (fillColors)).getShader();
                } else
                    if (fillColors.isStateful() || fillColors.canApplyTheme()) {
                        mFillColors = fillColors;
                    } else {
                        mFillColors = null;
                    }

                fillColor = fillColors.getDefaultColor();
            }
            final android.content.res.ComplexColor strokeColors = a.getComplexColor(R.styleable.VectorDrawablePath_strokeColor);
            if (strokeColors != null) {
                // If the colors is a gradient color, or the color state list is stateful, keep the
                // colors information. Otherwise, discard the colors and keep the default color.
                if (strokeColors instanceof android.content.res.GradientColor) {
                    mStrokeColors = strokeColors;
                    strokeGradient = ((android.content.res.GradientColor) (strokeColors)).getShader();
                } else
                    if (strokeColors.isStateful() || strokeColors.canApplyTheme()) {
                        mStrokeColors = strokeColors;
                    } else {
                        mStrokeColors = null;
                    }

                strokeColor = strokeColors.getDefaultColor();
            }
            // Update the gradient info, even if the gradiet is null.
            android.graphics.drawable.VectorDrawable.nUpdateFullPathFillGradient(mNativePtr, fillGradient != null ? fillGradient.getNativeInstance() : 0);
            android.graphics.drawable.VectorDrawable.nUpdateFullPathStrokeGradient(mNativePtr, strokeGradient != null ? strokeGradient.getNativeInstance() : 0);
            fillAlpha = a.getFloat(R.styleable.VectorDrawablePath_fillAlpha, fillAlpha);
            strokeLineCap = a.getInt(R.styleable.VectorDrawablePath_strokeLineCap, strokeLineCap);
            strokeLineJoin = a.getInt(R.styleable.VectorDrawablePath_strokeLineJoin, strokeLineJoin);
            strokeMiterLimit = a.getFloat(R.styleable.VectorDrawablePath_strokeMiterLimit, strokeMiterLimit);
            strokeAlpha = a.getFloat(R.styleable.VectorDrawablePath_strokeAlpha, strokeAlpha);
            strokeWidth = a.getFloat(R.styleable.VectorDrawablePath_strokeWidth, strokeWidth);
            trimPathEnd = a.getFloat(R.styleable.VectorDrawablePath_trimPathEnd, trimPathEnd);
            trimPathOffset = a.getFloat(R.styleable.VectorDrawablePath_trimPathOffset, trimPathOffset);
            trimPathStart = a.getFloat(R.styleable.VectorDrawablePath_trimPathStart, trimPathStart);
            fillType = a.getInt(R.styleable.VectorDrawablePath_fillType, fillType);
            android.graphics.drawable.VectorDrawable.nUpdateFullPathProperties(mNativePtr, strokeWidth, strokeColor, strokeAlpha, fillColor, fillAlpha, trimPathStart, trimPathEnd, trimPathOffset, strokeMiterLimit, strokeLineCap, strokeLineJoin, fillType);
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            if (mThemeAttrs != null) {
                return true;
            }
            boolean fillCanApplyTheme = canComplexColorApplyTheme(mFillColors);
            boolean strokeCanApplyTheme = canComplexColorApplyTheme(mStrokeColors);
            if (fillCanApplyTheme || strokeCanApplyTheme) {
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void applyTheme(android.content.res.Resources.Theme t) {
            // Resolve the theme attributes directly referred by the VectorDrawable.
            if (mThemeAttrs != null) {
                final android.content.res.TypedArray a = t.resolveAttributes(mThemeAttrs, R.styleable.VectorDrawablePath);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            // Resolve the theme attributes in-directly referred by the VectorDrawable, for example,
            // fillColor can refer to a color state list which itself needs to apply theme.
            // And this is the reason we still want to keep partial update for the path's properties.
            boolean fillCanApplyTheme = canComplexColorApplyTheme(mFillColors);
            boolean strokeCanApplyTheme = canComplexColorApplyTheme(mStrokeColors);
            if (fillCanApplyTheme) {
                mFillColors = mFillColors.obtainForTheme(t);
                if (mFillColors instanceof android.content.res.GradientColor) {
                    android.graphics.drawable.VectorDrawable.nUpdateFullPathFillGradient(mNativePtr, ((android.content.res.GradientColor) (mFillColors)).getShader().getNativeInstance());
                } else
                    if (mFillColors instanceof android.content.res.ColorStateList) {
                        android.graphics.drawable.VectorDrawable.nSetFillColor(mNativePtr, mFillColors.getDefaultColor());
                    }

            }
            if (strokeCanApplyTheme) {
                mStrokeColors = mStrokeColors.obtainForTheme(t);
                if (mStrokeColors instanceof android.content.res.GradientColor) {
                    android.graphics.drawable.VectorDrawable.nUpdateFullPathStrokeGradient(mNativePtr, ((android.content.res.GradientColor) (mStrokeColors)).getShader().getNativeInstance());
                } else
                    if (mStrokeColors instanceof android.content.res.ColorStateList) {
                        android.graphics.drawable.VectorDrawable.nSetStrokeColor(mNativePtr, mStrokeColors.getDefaultColor());
                    }

            }
        }

        private boolean canComplexColorApplyTheme(android.content.res.ComplexColor complexColor) {
            return (complexColor != null) && complexColor.canApplyTheme();
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        @java.lang.SuppressWarnings("unused")
        int getStrokeColor() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetStrokeColor(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setStrokeColor(int strokeColor) {
            mStrokeColors = null;
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetStrokeColor(mNativePtr, strokeColor);
            }
        }

        @java.lang.SuppressWarnings("unused")
        float getStrokeWidth() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetStrokeWidth(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setStrokeWidth(float strokeWidth) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetStrokeWidth(mNativePtr, strokeWidth);
            }
        }

        @java.lang.SuppressWarnings("unused")
        float getStrokeAlpha() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetStrokeAlpha(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setStrokeAlpha(float strokeAlpha) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetStrokeAlpha(mNativePtr, strokeAlpha);
            }
        }

        @java.lang.SuppressWarnings("unused")
        int getFillColor() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetFillColor(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setFillColor(int fillColor) {
            mFillColors = null;
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetFillColor(mNativePtr, fillColor);
            }
        }

        @java.lang.SuppressWarnings("unused")
        float getFillAlpha() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetFillAlpha(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setFillAlpha(float fillAlpha) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetFillAlpha(mNativePtr, fillAlpha);
            }
        }

        @java.lang.SuppressWarnings("unused")
        float getTrimPathStart() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetTrimPathStart(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setTrimPathStart(float trimPathStart) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetTrimPathStart(mNativePtr, trimPathStart);
            }
        }

        @java.lang.SuppressWarnings("unused")
        float getTrimPathEnd() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetTrimPathEnd(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setTrimPathEnd(float trimPathEnd) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetTrimPathEnd(mNativePtr, trimPathEnd);
            }
        }

        @java.lang.SuppressWarnings("unused")
        float getTrimPathOffset() {
            return isTreeValid() ? android.graphics.drawable.VectorDrawable.nGetTrimPathOffset(mNativePtr) : 0;
        }

        @java.lang.SuppressWarnings("unused")
        void setTrimPathOffset(float trimPathOffset) {
            if (isTreeValid()) {
                android.graphics.drawable.VectorDrawable.nSetTrimPathOffset(mNativePtr, trimPathOffset);
            }
        }
    }

    static abstract class VObject {
        com.android.internal.util.VirtualRefBasePtr mTreePtr = null;

        boolean isTreeValid() {
            return (mTreePtr != null) && (mTreePtr.get() != 0);
        }

        void setTree(com.android.internal.util.VirtualRefBasePtr ptr) {
            mTreePtr = ptr;
        }

        abstract long getNativePtr();

        abstract void inflate(android.content.res.Resources r, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme);

        abstract boolean canApplyTheme();

        abstract void applyTheme(android.content.res.Resources.Theme t);

        abstract boolean onStateChange(int[] state);

        abstract boolean isStateful();

        abstract boolean hasFocusStateSpecified();

        abstract int getNativeSize();

        abstract android.util.Property getProperty(java.lang.String propertyName);
    }

    private static native int nDraw(long rendererPtr, long canvasWrapperPtr, long colorFilterPtr, android.graphics.Rect bounds, boolean needsMirroring, boolean canReuseCache);

    private static native boolean nGetFullPathProperties(long pathPtr, byte[] properties, int length);

    private static native void nSetName(long nodePtr, java.lang.String name);

    private static native boolean nGetGroupProperties(long groupPtr, float[] properties, int length);

    private static native void nSetPathString(long pathPtr, java.lang.String pathString, int length);

    // ------------- @FastNative ------------------
    @dalvik.annotation.optimization.FastNative
    private static native long nCreateTree(long rootGroupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateTreeFromCopy(long treeToCopy, long rootGroupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetRendererViewportSize(long rendererPtr, float viewportWidth, float viewportHeight);

    @dalvik.annotation.optimization.FastNative
    private static native boolean nSetRootAlpha(long rendererPtr, float alpha);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetRootAlpha(long rendererPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetAntiAlias(long rendererPtr, boolean aa);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetAllowCaching(long rendererPtr, boolean allowCaching);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateFullPath();

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateFullPath(long nativeFullPathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nUpdateFullPathProperties(long pathPtr, float strokeWidth, int strokeColor, float strokeAlpha, int fillColor, float fillAlpha, float trimPathStart, float trimPathEnd, float trimPathOffset, float strokeMiterLimit, int strokeLineCap, int strokeLineJoin, int fillType);

    @dalvik.annotation.optimization.FastNative
    private static native void nUpdateFullPathFillGradient(long pathPtr, long fillGradientPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nUpdateFullPathStrokeGradient(long pathPtr, long strokeGradientPtr);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateClipPath();

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateClipPath(long clipPathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateGroup();

    @dalvik.annotation.optimization.FastNative
    private static native long nCreateGroup(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nUpdateGroupProperties(long groupPtr, float rotate, float pivotX, float pivotY, float scaleX, float scaleY, float translateX, float translateY);

    @dalvik.annotation.optimization.FastNative
    private static native void nAddChild(long groupPtr, long nodePtr);

    /**
     * The setters and getters below for paths and groups are here temporarily, and will be
     * removed once the animation in AVD is replaced with RenderNodeAnimator, in which case the
     * animation will modify these properties in native. By then no JNI hopping would be necessary
     * for VD during animation, and these setters and getters will be obsolete.
     */
    // Setters and getters during animation.
    @dalvik.annotation.optimization.FastNative
    private static native float nGetRotation(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetRotation(long groupPtr, float rotation);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetPivotX(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetPivotX(long groupPtr, float pivotX);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetPivotY(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetPivotY(long groupPtr, float pivotY);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetScaleX(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetScaleX(long groupPtr, float scaleX);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetScaleY(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetScaleY(long groupPtr, float scaleY);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetTranslateX(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetTranslateX(long groupPtr, float translateX);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetTranslateY(long groupPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetTranslateY(long groupPtr, float translateY);

    // Setters and getters for VPath during animation.
    @dalvik.annotation.optimization.FastNative
    private static native void nSetPathData(long pathPtr, long pathDataPtr);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetStrokeWidth(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetStrokeWidth(long pathPtr, float width);

    @dalvik.annotation.optimization.FastNative
    private static native int nGetStrokeColor(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetStrokeColor(long pathPtr, int strokeColor);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetStrokeAlpha(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetStrokeAlpha(long pathPtr, float alpha);

    @dalvik.annotation.optimization.FastNative
    private static native int nGetFillColor(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetFillColor(long pathPtr, int fillColor);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetFillAlpha(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetFillAlpha(long pathPtr, float fillAlpha);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetTrimPathStart(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetTrimPathStart(long pathPtr, float trimPathStart);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetTrimPathEnd(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetTrimPathEnd(long pathPtr, float trimPathEnd);

    @dalvik.annotation.optimization.FastNative
    private static native float nGetTrimPathOffset(long pathPtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetTrimPathOffset(long pathPtr, float trimPathOffset);
}

