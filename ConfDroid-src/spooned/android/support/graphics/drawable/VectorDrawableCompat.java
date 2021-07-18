/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.graphics.drawable;


/**
 * For API 24 and above, this class is delegating to the framework's {@link VectorDrawable}.
 * For older API version, this class lets you create a drawable based on an XML vector graphic.
 * <p/>
 * You can always create a VectorDrawableCompat object and use it as a Drawable by the Java API.
 * In order to refer to VectorDrawableCompat inside a XML file,  you can use app:srcCompat attribute
 * in AppCompat library's ImageButton or ImageView.
 * <p/>
 * <strong>Note:</strong> To optimize for the re-drawing performance, one bitmap cache is created
 * for each VectorDrawableCompat. Therefore, referring to the same VectorDrawableCompat means
 * sharing the same bitmap cache. If these references don't agree upon on the same size, the bitmap
 * will be recreated and redrawn every time size is changed. In other words, if a VectorDrawable is
 * used for different sizes, it is more efficient to create multiple VectorDrawables, one for each
 * size.
 * <p/>
 * VectorDrawableCompat can be defined in an XML file with the <code>&lt;vector></code> element.
 * <p/>
 * The VectorDrawableCompat has the following elements:
 * <p/>
 * <dt><code>&lt;vector></code></dt>
 * <dl>
 * <dd>Used to define a vector drawable
 * <dl>
 * <dt><code>android:name</code></dt>
 * <dd>Defines the name of this vector drawable.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:width</code></dt>
 * <dd>Used to define the intrinsic width of the drawable.
 * This support all the dimension units, normally specified with dp.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:height</code></dt>
 * <dd>Used to define the intrinsic height the drawable.
 * This support all the dimension units, normally specified with dp.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:viewportWidth</code></dt>
 * <dd>Used to define the width of the viewport space. Viewport is basically
 * the virtual canvas where the paths are drawn on.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:viewportHeight</code></dt>
 * <dd>Used to define the height of the viewport space. Viewport is basically
 * the virtual canvas where the paths are drawn on.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:tint</code></dt>
 * <dd>The color to apply to the drawable as a tint. By default, no tint is applied.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:tintMode</code></dt>
 * <dd>The Porter-Duff blending mode for the tint color. The default value is src_in.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:autoMirrored</code></dt>
 * <dd>Indicates if the drawable needs to be mirrored when its layout direction is
 * RTL (right-to-left).</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:alpha</code></dt>
 * <dd>The opacity of this drawable.</dd>
 * <dd>Animatable : Yes.</dd>
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
 * <dd>Animatable : No.</dd>
 * <dt><code>android:rotation</code></dt>
 * <dd>The degrees of rotation of the group.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:pivotX</code></dt>
 * <dd>The X coordinate of the pivot for the scale and rotation of the group.
 * This is defined in the viewport space.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:pivotY</code></dt>
 * <dd>The Y coordinate of the pivot for the scale and rotation of the group.
 * This is defined in the viewport space.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:scaleX</code></dt>
 * <dd>The amount of scale on the X Coordinate.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:scaleY</code></dt>
 * <dd>The amount of scale on the Y coordinate.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:translateX</code></dt>
 * <dd>The amount of translation on the X coordinate.
 * This is defined in the viewport space.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:translateY</code></dt>
 * <dd>The amount of translation on the Y coordinate.
 * This is defined in the viewport space.</dd>
 * <dd>Animatable : Yes.</dd>
 * </dl></dd>
 * </dl>
 *
 * <dl>
 * <dt><code>&lt;path></code></dt>
 * <dd>Defines paths to be drawn.
 * <dl>
 * <dt><code>android:name</code></dt>
 * <dd>Defines the name of the path.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:pathData</code></dt>
 * <dd>Defines path data using exactly same format as "d" attribute
 * in the SVG's path data. This is defined in the viewport space.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:fillColor</code></dt>
 * <dd>Specifies the color used to fill the path.
 * If this property is animated, any value set by the animation will override the original value.
 * No path fill is drawn if this property is not specified.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:strokeColor</code></dt>
 * <dd>Specifies the color used to draw the path outline.
 * If this property is animated, any value set by the animation will override the original value.
 * No path outline is drawn if this property is not specified.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:strokeWidth</code></dt>
 * <dd>The width a path stroke.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:strokeAlpha</code></dt>
 * <dd>The opacity of a path stroke.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:fillAlpha</code></dt>
 * <dd>The opacity to fill the path with.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:trimPathStart</code></dt>
 * <dd>The fraction of the path to trim from the start, in the range from 0 to 1.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:trimPathEnd</code></dt>
 * <dd>The fraction of the path to trim from the end, in the range from 0 to 1.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:trimPathOffset</code></dt>
 * <dd>Shift trim region (allows showed region to include the start and end), in the range
 * from 0 to 1.</dd>
 * <dd>Animatable : Yes.</dd>
 * <dt><code>android:strokeLineCap</code></dt>
 * <dd>Sets the linecap for a stroked path: butt, round, square.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:strokeLineJoin</code></dt>
 * <dd>Sets the lineJoin for a stroked path: miter,round,bevel.</dd>
 * <dd>Animatable : No.</dd>
 * <dt><code>android:strokeMiterLimit</code></dt>
 * <dd>Sets the Miter limit for a stroked path.</dd>
 * <dd>Animatable : No.</dd>
 * </dl></dd>
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
 */
@android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
public class VectorDrawableCompat extends android.support.graphics.drawable.VectorDrawableCommon {
    static final java.lang.String LOGTAG = "VectorDrawableCompat";

    static final android.graphics.PorterDuff.Mode DEFAULT_TINT_MODE = android.graphics.PorterDuff.Mode.SRC_IN;

    private static final java.lang.String SHAPE_CLIP_PATH = "clip-path";

    private static final java.lang.String SHAPE_GROUP = "group";

    private static final java.lang.String SHAPE_PATH = "path";

    private static final java.lang.String SHAPE_VECTOR = "vector";

    private static final int LINECAP_BUTT = 0;

    private static final int LINECAP_ROUND = 1;

    private static final int LINECAP_SQUARE = 2;

    private static final int LINEJOIN_MITER = 0;

    private static final int LINEJOIN_ROUND = 1;

    private static final int LINEJOIN_BEVEL = 2;

    // Cap the bitmap size, such that it won't hurt the performance too much
    // and it won't crash due to a very large scale.
    // The drawable will look blurry above this size.
    private static final int MAX_CACHED_BITMAP_SIZE = 2048;

    private static final boolean DBG_VECTOR_DRAWABLE = false;

    private android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState mVectorState;

    private android.graphics.PorterDuffColorFilter mTintFilter;

    private android.graphics.ColorFilter mColorFilter;

    private boolean mMutated;

    // AnimatedVectorDrawable needs to turn off the cache all the time, otherwise,
    // caching the bitmap by default is allowed.
    private boolean mAllowCaching = true;

    // The Constant state associated with the <code>mDelegateDrawable</code>.
    private android.graphics.drawable.Drawable.ConstantState mCachedConstantStateDelegate;

    // Temp variable, only for saving "new" operation at the draw() time.
    private final float[] mTmpFloats = new float[9];

    private final android.graphics.Matrix mTmpMatrix = new android.graphics.Matrix();

    private final android.graphics.Rect mTmpBounds = new android.graphics.Rect();

    VectorDrawableCompat() {
        mVectorState = new android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState();
    }

    VectorDrawableCompat(@android.support.annotation.NonNull
    android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state) {
        mVectorState = state;
        mTintFilter = updateTintFilter(mTintFilter, state.mTint, state.mTintMode);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.mutate();
            return this;
        }
        if ((!mMutated) && (super.mutate() == this)) {
            mVectorState = new android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState(mVectorState);
            mMutated = true;
        }
        return this;
    }

    java.lang.Object getTargetByName(java.lang.String name) {
        return mVectorState.mVPathRenderer.mVGTargetsMap.get(name);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        if (mDelegateDrawable != null) {
            // Such that the configuration can be refreshed.
            return new android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableDelegateState(mDelegateDrawable.getConstantState());
        }
        mVectorState.mChangingConfigurations = getChangingConfigurations();
        return mVectorState;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.draw(canvas);
            return;
        }
        // We will offset the bounds for drawBitmap, so copyBounds() here instead
        // of getBounds().
        copyBounds(mTmpBounds);
        if ((mTmpBounds.width() <= 0) || (mTmpBounds.height() <= 0)) {
            // Nothing to draw
            return;
        }
        // Color filters always override tint filters.
        final android.graphics.ColorFilter colorFilter = (mColorFilter == null) ? mTintFilter : mColorFilter;
        // The imageView can scale the canvas in different ways, in order to
        // avoid blurry scaling, we have to draw into a bitmap with exact pixel
        // size first. This bitmap size is determined by the bounds and the
        // canvas scale.
        canvas.getMatrix(mTmpMatrix);
        mTmpMatrix.getValues(mTmpFloats);
        float canvasScaleX = java.lang.Math.abs(mTmpFloats[android.graphics.Matrix.MSCALE_X]);
        float canvasScaleY = java.lang.Math.abs(mTmpFloats[android.graphics.Matrix.MSCALE_Y]);
        float canvasSkewX = java.lang.Math.abs(mTmpFloats[android.graphics.Matrix.MSKEW_X]);
        float canvasSkewY = java.lang.Math.abs(mTmpFloats[android.graphics.Matrix.MSKEW_Y]);
        // When there is any rotation / skew, then the scale value is not valid.
        if ((canvasSkewX != 0) || (canvasSkewY != 0)) {
            canvasScaleX = 1.0F;
            canvasScaleY = 1.0F;
        }
        int scaledWidth = ((int) (mTmpBounds.width() * canvasScaleX));
        int scaledHeight = ((int) (mTmpBounds.height() * canvasScaleY));
        scaledWidth = java.lang.Math.min(android.support.graphics.drawable.VectorDrawableCompat.MAX_CACHED_BITMAP_SIZE, scaledWidth);
        scaledHeight = java.lang.Math.min(android.support.graphics.drawable.VectorDrawableCompat.MAX_CACHED_BITMAP_SIZE, scaledHeight);
        if ((scaledWidth <= 0) || (scaledHeight <= 0)) {
            return;
        }
        final int saveCount = canvas.save();
        canvas.translate(mTmpBounds.left, mTmpBounds.top);
        // Handle RTL mirroring.
        final boolean needMirroring = needMirroring();
        if (needMirroring) {
            canvas.translate(mTmpBounds.width(), 0);
            canvas.scale(-1.0F, 1.0F);
        }
        // At this point, canvas has been translated to the right position.
        // And we use this bound for the destination rect for the drawBitmap, so
        // we offset to (0, 0);
        mTmpBounds.offsetTo(0, 0);
        mVectorState.createCachedBitmapIfNeeded(scaledWidth, scaledHeight);
        if (!mAllowCaching) {
            mVectorState.updateCachedBitmap(scaledWidth, scaledHeight);
        } else {
            if (!mVectorState.canReuseCache()) {
                mVectorState.updateCachedBitmap(scaledWidth, scaledHeight);
                mVectorState.updateCacheStates();
            }
        }
        mVectorState.drawCachedBitmapWithRootAlpha(canvas, colorFilter, mTmpBounds);
        canvas.restoreToCount(saveCount);
    }

    @java.lang.Override
    public int getAlpha() {
        if (mDelegateDrawable != null) {
            return android.support.v4.graphics.drawable.DrawableCompat.getAlpha(mDelegateDrawable);
        }
        return mVectorState.mVPathRenderer.getRootAlpha();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setAlpha(alpha);
            return;
        }
        if (mVectorState.mVPathRenderer.getRootAlpha() != alpha) {
            mVectorState.mVPathRenderer.setRootAlpha(alpha);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        mColorFilter = colorFilter;
        invalidateSelf();
    }

    /**
     * Ensures the tint filter is consistent with the current tint color and
     * mode.
     */
    android.graphics.PorterDuffColorFilter updateTintFilter(android.graphics.PorterDuffColorFilter tintFilter, android.content.res.ColorStateList tint, android.graphics.PorterDuff.Mode tintMode) {
        if ((tint == null) || (tintMode == null)) {
            return null;
        }
        // setMode, setColor of PorterDuffColorFilter are not public method in SDK v7.
        // Therefore we create a new one all the time here. Don't expect this is called often.
        final int color = tint.getColorForState(getState(), android.graphics.Color.TRANSPARENT);
        return new android.graphics.PorterDuffColorFilter(color, tintMode);
    }

    @java.lang.Override
    public void setTint(int tint) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTint(mDelegateDrawable, tint);
            return;
        }
        setTintList(android.content.res.ColorStateList.valueOf(tint));
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(mDelegateDrawable, tint);
            return;
        }
        final android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state = mVectorState;
        if (state.mTint != tint) {
            state.mTint = tint;
            mTintFilter = updateTintFilter(mTintFilter, tint, state.mTintMode);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setTintMode(android.graphics.PorterDuff.Mode tintMode) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mDelegateDrawable, tintMode);
            return;
        }
        final android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state = mVectorState;
        if (state.mTintMode != tintMode) {
            state.mTintMode = tintMode;
            mTintFilter = updateTintFilter(mTintFilter, state.mTint, tintMode);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public boolean isStateful() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.isStateful();
        }
        return super.isStateful() || (((mVectorState != null) && (mVectorState.mTint != null)) && mVectorState.mTint.isStateful());
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setState(stateSet);
        }
        final android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state = mVectorState;
        if ((state.mTint != null) && (state.mTintMode != null)) {
            mTintFilter = updateTintFilter(mTintFilter, state.mTint, state.mTintMode);
            invalidateSelf();
            return true;
        }
        return false;
    }

    @java.lang.Override
    public int getOpacity() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getOpacity();
        }
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getIntrinsicWidth();
        }
        return ((int) (mVectorState.mVPathRenderer.mBaseWidth));
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getIntrinsicHeight();
        }
        return ((int) (mVectorState.mVPathRenderer.mBaseHeight));
    }

    // Don't support re-applying themes. The initial theme loading is working.
    @java.lang.Override
    public boolean canApplyTheme() {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.canApplyTheme(mDelegateDrawable);
        }
        return false;
    }

    @java.lang.Override
    public boolean isAutoMirrored() {
        if (mDelegateDrawable != null) {
            return android.support.v4.graphics.drawable.DrawableCompat.isAutoMirrored(mDelegateDrawable);
        }
        return mVectorState.mAutoMirrored;
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setAutoMirrored(mDelegateDrawable, mirrored);
            return;
        }
        mVectorState.mAutoMirrored = mirrored;
    }

    /**
     * The size of a pixel when scaled from the intrinsic dimension to the viewport dimension. This
     * is used to calculate the path animation accuracy.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public float getPixelSize() {
        if ((((((mVectorState == null) && (mVectorState.mVPathRenderer == null)) || (mVectorState.mVPathRenderer.mBaseWidth == 0)) || (mVectorState.mVPathRenderer.mBaseHeight == 0)) || (mVectorState.mVPathRenderer.mViewportHeight == 0)) || (mVectorState.mVPathRenderer.mViewportWidth == 0)) {
            return 1;// fall back to 1:1 pixel mapping.

        }
        float intrinsicWidth = mVectorState.mVPathRenderer.mBaseWidth;
        float intrinsicHeight = mVectorState.mVPathRenderer.mBaseHeight;
        float viewportWidth = mVectorState.mVPathRenderer.mViewportWidth;
        float viewportHeight = mVectorState.mVPathRenderer.mViewportHeight;
        float scaleX = viewportWidth / intrinsicWidth;
        float scaleY = viewportHeight / intrinsicHeight;
        return java.lang.Math.min(scaleX, scaleY);
    }

    /**
     * Create a VectorDrawableCompat object.
     *
     * @param res
     * 		the resources.
     * @param resId
     * 		the resource ID for VectorDrawableCompat object.
     * @param theme
     * 		the theme of this vector drawable, it can be null.
     * @return a new VectorDrawableCompat or null if parsing error is found.
     */
    @android.support.annotation.Nullable
    public static android.support.graphics.drawable.VectorDrawableCompat create(@android.support.annotation.NonNull
    android.content.res.Resources res, @android.support.annotation.DrawableRes
    int resId, @android.support.annotation.Nullable
    android.content.res.Resources.Theme theme) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            final android.support.graphics.drawable.VectorDrawableCompat drawable = new android.support.graphics.drawable.VectorDrawableCompat();
            drawable.mDelegateDrawable = android.support.v4.content.res.ResourcesCompat.getDrawable(res, resId, theme);
            drawable.mCachedConstantStateDelegate = new android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableDelegateState(drawable.mDelegateDrawable.getConstantState());
            return drawable;
        }
        try {
            final org.xmlpull.v1.XmlPullParser parser = res.getXml(resId);
            final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                // Empty loop
            } 
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
            }
            return android.support.graphics.drawable.VectorDrawableCompat.createFromXmlInner(res, parser, attrs, theme);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.support.graphics.drawable.VectorDrawableCompat.LOGTAG, "parser error", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.support.graphics.drawable.VectorDrawableCompat.LOGTAG, "parser error", e);
        }
        return null;
    }

    /**
     * Create a VectorDrawableCompat from inside an XML document using an optional
     * {@link Theme}. Called on a parser positioned at a tag in an XML
     * document, tries to create a Drawable from that tag. Returns {@code null}
     * if the tag is not a valid drawable.
     */
    public static android.support.graphics.drawable.VectorDrawableCompat createFromXmlInner(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.support.graphics.drawable.VectorDrawableCompat drawable = new android.support.graphics.drawable.VectorDrawableCompat();
        drawable.inflate(r, parser, attrs, theme);
        return drawable;
    }

    static int applyAlpha(int color, float alpha) {
        int alphaBytes = android.graphics.Color.alpha(color);
        color &= 0xffffff;
        color |= ((int) (alphaBytes * alpha)) << 24;
        return color;
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.inflate(res, parser, attrs);
            return;
        }
        inflate(res, parser, attrs, null);
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.inflate(mDelegateDrawable, res, parser, attrs, theme);
            return;
        }
        final android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state = mVectorState;
        final android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer pathRenderer = new android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer();
        state.mVPathRenderer = pathRenderer;
        final android.content.res.TypedArray a = android.support.graphics.drawable.VectorDrawableCommon.obtainAttributes(res, theme, attrs, android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableTypeArray);
        updateStateFromTypedArray(a, parser);
        a.recycle();
        state.mChangingConfigurations = getChangingConfigurations();
        state.mCacheDirty = true;
        inflateInternal(res, parser, attrs, theme);
        mTintFilter = updateTintFilter(mTintFilter, state.mTint, state.mTintMode);
    }

    /**
     * Parses a {@link android.graphics.PorterDuff.Mode} from a tintMode
     * attribute's enum value.
     */
    private static android.graphics.PorterDuff.Mode parseTintModeCompat(int value, android.graphics.PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3 :
                return android.graphics.PorterDuff.Mode.SRC_OVER;
            case 5 :
                return android.graphics.PorterDuff.Mode.SRC_IN;
            case 9 :
                return android.graphics.PorterDuff.Mode.SRC_ATOP;
            case 14 :
                return android.graphics.PorterDuff.Mode.MULTIPLY;
            case 15 :
                return android.graphics.PorterDuff.Mode.SCREEN;
            case 16 :
                return android.graphics.PorterDuff.Mode.ADD;
            default :
                return defaultMode;
        }
    }

    private void updateStateFromTypedArray(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
        final android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state = mVectorState;
        final android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer pathRenderer = state.mVPathRenderer;
        // Account for any configuration changes.
        // state.mChangingConfigurations |= Utils.getChangingConfigurations(a);
        final int mode = android.support.graphics.drawable.TypedArrayUtils.getNamedInt(a, parser, "tintMode", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_tintMode, -1);
        state.mTintMode = android.support.graphics.drawable.VectorDrawableCompat.parseTintModeCompat(mode, android.graphics.PorterDuff.Mode.SRC_IN);
        final android.content.res.ColorStateList tint = a.getColorStateList(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_tint);
        if (tint != null) {
            state.mTint = tint;
        }
        state.mAutoMirrored = android.support.graphics.drawable.TypedArrayUtils.getNamedBoolean(a, parser, "autoMirrored", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_autoMirrored, state.mAutoMirrored);
        pathRenderer.mViewportWidth = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "viewportWidth", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_viewportWidth, pathRenderer.mViewportWidth);
        pathRenderer.mViewportHeight = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "viewportHeight", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_viewportHeight, pathRenderer.mViewportHeight);
        if (pathRenderer.mViewportWidth <= 0) {
            throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        } else
            if (pathRenderer.mViewportHeight <= 0) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
            }

        pathRenderer.mBaseWidth = a.getDimension(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_width, pathRenderer.mBaseWidth);
        pathRenderer.mBaseHeight = a.getDimension(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_height, pathRenderer.mBaseHeight);
        if (pathRenderer.mBaseWidth <= 0) {
            throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires width > 0");
        } else
            if (pathRenderer.mBaseHeight <= 0) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + "<vector> tag requires height > 0");
            }

        // shown up from API 11.
        final float alphaInFloat = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "alpha", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_alpha, pathRenderer.getAlpha());
        pathRenderer.setAlpha(alphaInFloat);
        final java.lang.String name = a.getString(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawable_name);
        if (name != null) {
            pathRenderer.mRootName = name;
            pathRenderer.mVGTargetsMap.put(name, pathRenderer);
        }
    }

    private void inflateInternal(android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState state = mVectorState;
        final android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer pathRenderer = state.mVPathRenderer;
        boolean noPathTag = true;
        // Use a stack to help to build the group tree.
        // The top of the stack is always the current group.
        final java.util.Stack<android.support.graphics.drawable.VectorDrawableCompat.VGroup> groupStack = new java.util.Stack<android.support.graphics.drawable.VectorDrawableCompat.VGroup>();
        groupStack.push(pathRenderer.mRootGroup);
        int eventType = parser.getEventType();
        final int innerDepth = parser.getDepth() + 1;
        // Parse everything until the end of the vector element.
        while ((eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((parser.getDepth() >= innerDepth) || (eventType != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                final java.lang.String tagName = parser.getName();
                final android.support.graphics.drawable.VectorDrawableCompat.VGroup currentGroup = groupStack.peek();
                if (android.support.graphics.drawable.VectorDrawableCompat.SHAPE_PATH.equals(tagName)) {
                    final android.support.graphics.drawable.VectorDrawableCompat.VFullPath path = new android.support.graphics.drawable.VectorDrawableCompat.VFullPath();
                    path.inflate(res, attrs, theme, parser);
                    currentGroup.mChildren.add(path);
                    if (path.getPathName() != null) {
                        pathRenderer.mVGTargetsMap.put(path.getPathName(), path);
                    }
                    noPathTag = false;
                    state.mChangingConfigurations |= path.mChangingConfigurations;
                } else
                    if (android.support.graphics.drawable.VectorDrawableCompat.SHAPE_CLIP_PATH.equals(tagName)) {
                        final android.support.graphics.drawable.VectorDrawableCompat.VClipPath path = new android.support.graphics.drawable.VectorDrawableCompat.VClipPath();
                        path.inflate(res, attrs, theme, parser);
                        currentGroup.mChildren.add(path);
                        if (path.getPathName() != null) {
                            pathRenderer.mVGTargetsMap.put(path.getPathName(), path);
                        }
                        state.mChangingConfigurations |= path.mChangingConfigurations;
                    } else
                        if (android.support.graphics.drawable.VectorDrawableCompat.SHAPE_GROUP.equals(tagName)) {
                            android.support.graphics.drawable.VectorDrawableCompat.VGroup newChildGroup = new android.support.graphics.drawable.VectorDrawableCompat.VGroup();
                            newChildGroup.inflate(res, attrs, theme, parser);
                            currentGroup.mChildren.add(newChildGroup);
                            groupStack.push(newChildGroup);
                            if (newChildGroup.getGroupName() != null) {
                                pathRenderer.mVGTargetsMap.put(newChildGroup.getGroupName(), newChildGroup);
                            }
                            state.mChangingConfigurations |= newChildGroup.mChangingConfigurations;
                        }


            } else
                if (eventType == org.xmlpull.v1.XmlPullParser.END_TAG) {
                    final java.lang.String tagName = parser.getName();
                    if (android.support.graphics.drawable.VectorDrawableCompat.SHAPE_GROUP.equals(tagName)) {
                        groupStack.pop();
                    }
                }

            eventType = parser.next();
        } 
        // Print the tree out for debug.
        if (android.support.graphics.drawable.VectorDrawableCompat.DBG_VECTOR_DRAWABLE) {
            printGroupTree(pathRenderer.mRootGroup, 0);
        }
        if (noPathTag) {
            final java.lang.StringBuffer tag = new java.lang.StringBuffer();
            if (tag.length() > 0) {
                tag.append(" or ");
            }
            tag.append(android.support.graphics.drawable.VectorDrawableCompat.SHAPE_PATH);
            throw new org.xmlpull.v1.XmlPullParserException(("no " + tag) + " defined");
        }
    }

    private void printGroupTree(android.support.graphics.drawable.VectorDrawableCompat.VGroup currentGroup, int level) {
        java.lang.String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "    ";
        }
        // Print the current node
        android.util.Log.v(android.support.graphics.drawable.VectorDrawableCompat.LOGTAG, (((indent + "current group is :") + currentGroup.getGroupName()) + " rotation is ") + currentGroup.mRotate);
        android.util.Log.v(android.support.graphics.drawable.VectorDrawableCompat.LOGTAG, (indent + "matrix is :") + currentGroup.getLocalMatrix().toString());
        // Then print all the children groups
        for (int i = 0; i < currentGroup.mChildren.size(); i++) {
            java.lang.Object child = currentGroup.mChildren.get(i);
            if (child instanceof android.support.graphics.drawable.VectorDrawableCompat.VGroup) {
                printGroupTree(((android.support.graphics.drawable.VectorDrawableCompat.VGroup) (child)), level + 1);
            } else {
                ((android.support.graphics.drawable.VectorDrawableCompat.VPath) (child)).printVPath(level + 1);
            }
        }
    }

    void setAllowCaching(boolean allowCaching) {
        mAllowCaching = allowCaching;
    }

    // We don't support RTL auto mirroring since the getLayoutDirection() is for API 17+.
    private boolean needMirroring() {
        if (android.os.Build.VERSION.SDK_INT < 17) {
            return false;
        } else {
            return isAutoMirrored() && (getLayoutDirection() == android.util.LayoutDirection.RTL);
        }
    }

    // Extra override functions for delegation for SDK >= 7.
    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setBounds(bounds);
        }
    }

    @java.lang.Override
    public int getChangingConfigurations() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | mVectorState.getChangingConfigurations();
    }

    @java.lang.Override
    public void invalidateSelf() {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.invalidateSelf();
            return;
        }
        super.invalidateSelf();
    }

    @java.lang.Override
    public void scheduleSelf(java.lang.Runnable what, long when) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.scheduleSelf(what, when);
            return;
        }
        super.scheduleSelf(what, when);
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setVisible(visible, restart);
        }
        return super.setVisible(visible, restart);
    }

    @java.lang.Override
    public void unscheduleSelf(java.lang.Runnable what) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.unscheduleSelf(what);
            return;
        }
        super.unscheduleSelf(what);
    }

    /**
     * Constant state for delegating the creating drawable job for SDK >= 24.
     * Instead of creating a VectorDrawable, create a VectorDrawableCompat instance which contains
     * a delegated VectorDrawable instance.
     */
    private static class VectorDrawableDelegateState extends android.graphics.drawable.Drawable.ConstantState {
        private final android.graphics.drawable.Drawable.ConstantState mDelegateState;

        public VectorDrawableDelegateState(android.graphics.drawable.Drawable.ConstantState state) {
            mDelegateState = state;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            android.support.graphics.drawable.VectorDrawableCompat drawableCompat = new android.support.graphics.drawable.VectorDrawableCompat();
            drawableCompat.mDelegateDrawable = ((android.graphics.drawable.VectorDrawable) (mDelegateState.newDrawable()));
            return drawableCompat;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            android.support.graphics.drawable.VectorDrawableCompat drawableCompat = new android.support.graphics.drawable.VectorDrawableCompat();
            drawableCompat.mDelegateDrawable = ((android.graphics.drawable.VectorDrawable) (mDelegateState.newDrawable(res)));
            return drawableCompat;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res, android.content.res.Resources.Theme theme) {
            android.support.graphics.drawable.VectorDrawableCompat drawableCompat = new android.support.graphics.drawable.VectorDrawableCompat();
            drawableCompat.mDelegateDrawable = ((android.graphics.drawable.VectorDrawable) (mDelegateState.newDrawable(res, theme)));
            return drawableCompat;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return mDelegateState.canApplyTheme();
        }

        @java.lang.Override
        public int getChangingConfigurations() {
            return mDelegateState.getChangingConfigurations();
        }
    }

    private static class VectorDrawableCompatState extends android.graphics.drawable.Drawable.ConstantState {
        int mChangingConfigurations;

        android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer mVPathRenderer;

        android.content.res.ColorStateList mTint = null;

        android.graphics.PorterDuff.Mode mTintMode = android.support.graphics.drawable.VectorDrawableCompat.DEFAULT_TINT_MODE;

        boolean mAutoMirrored;

        android.graphics.Bitmap mCachedBitmap;

        int[] mCachedThemeAttrs;

        android.content.res.ColorStateList mCachedTint;

        android.graphics.PorterDuff.Mode mCachedTintMode;

        int mCachedRootAlpha;

        boolean mCachedAutoMirrored;

        boolean mCacheDirty;

        /**
         * Temporary paint object used to draw cached bitmaps.
         */
        android.graphics.Paint mTempPaint;

        // Deep copy for mutate() or implicitly mutate.
        public VectorDrawableCompatState(android.support.graphics.drawable.VectorDrawableCompat.VectorDrawableCompatState copy) {
            if (copy != null) {
                mChangingConfigurations = copy.mChangingConfigurations;
                mVPathRenderer = new android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer(copy.mVPathRenderer);
                if (copy.mVPathRenderer.mFillPaint != null) {
                    mVPathRenderer.mFillPaint = new android.graphics.Paint(copy.mVPathRenderer.mFillPaint);
                }
                if (copy.mVPathRenderer.mStrokePaint != null) {
                    mVPathRenderer.mStrokePaint = new android.graphics.Paint(copy.mVPathRenderer.mStrokePaint);
                }
                mTint = copy.mTint;
                mTintMode = copy.mTintMode;
                mAutoMirrored = copy.mAutoMirrored;
            }
        }

        public void drawCachedBitmapWithRootAlpha(android.graphics.Canvas canvas, android.graphics.ColorFilter filter, android.graphics.Rect originalBounds) {
            // The bitmap's size is the same as the bounds.
            final android.graphics.Paint p = getPaint(filter);
            canvas.drawBitmap(mCachedBitmap, null, originalBounds, p);
        }

        public boolean hasTranslucentRoot() {
            return mVPathRenderer.getRootAlpha() < 255;
        }

        /**
         *
         *
         * @return null when there is no need for alpha paint.
         */
        public android.graphics.Paint getPaint(android.graphics.ColorFilter filter) {
            if ((!hasTranslucentRoot()) && (filter == null)) {
                return null;
            }
            if (mTempPaint == null) {
                mTempPaint = new android.graphics.Paint();
                mTempPaint.setFilterBitmap(true);
            }
            mTempPaint.setAlpha(mVPathRenderer.getRootAlpha());
            mTempPaint.setColorFilter(filter);
            return mTempPaint;
        }

        public void updateCachedBitmap(int width, int height) {
            mCachedBitmap.eraseColor(android.graphics.Color.TRANSPARENT);
            android.graphics.Canvas tmpCanvas = new android.graphics.Canvas(mCachedBitmap);
            mVPathRenderer.draw(tmpCanvas, width, height, null);
        }

        public void createCachedBitmapIfNeeded(int width, int height) {
            if ((mCachedBitmap == null) || (!canReuseBitmap(width, height))) {
                mCachedBitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
                mCacheDirty = true;
            }
        }

        public boolean canReuseBitmap(int width, int height) {
            if ((width == mCachedBitmap.getWidth()) && (height == mCachedBitmap.getHeight())) {
                return true;
            }
            return false;
        }

        public boolean canReuseCache() {
            if (((((!mCacheDirty) && (mCachedTint == mTint)) && (mCachedTintMode == mTintMode)) && (mCachedAutoMirrored == mAutoMirrored)) && (mCachedRootAlpha == mVPathRenderer.getRootAlpha())) {
                return true;
            }
            return false;
        }

        public void updateCacheStates() {
            // Use shallow copy here and shallow comparison in canReuseCache(),
            // likely hit cache miss more, but practically not much difference.
            mCachedTint = mTint;
            mCachedTintMode = mTintMode;
            mCachedRootAlpha = mVPathRenderer.getRootAlpha();
            mCachedAutoMirrored = mAutoMirrored;
            mCacheDirty = false;
        }

        public VectorDrawableCompatState() {
            mVPathRenderer = new android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer();
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.support.graphics.drawable.VectorDrawableCompat(this);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.support.graphics.drawable.VectorDrawableCompat(this);
        }

        @java.lang.Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }

    private static class VPathRenderer {
        /* Right now the internal data structure is organized as a tree.
        Each node can be a group node, or a path.
        A group node can have groups or paths as children, but a path node has
        no children.
        One example can be:
                        Root Group
                       /    |     \
                  Group    Path    Group
                 /     \             |
                Path   Path         Path
         */
        // Variables that only used temporarily inside the draw() call, so there
        // is no need for deep copying.
        private final android.graphics.Path mPath;

        private final android.graphics.Path mRenderPath;

        private static final android.graphics.Matrix IDENTITY_MATRIX = new android.graphics.Matrix();

        private final android.graphics.Matrix mFinalPathMatrix = new android.graphics.Matrix();

        private android.graphics.Paint mStrokePaint;

        private android.graphics.Paint mFillPaint;

        private android.graphics.PathMeasure mPathMeasure;

        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        private int mChangingConfigurations;

        final android.support.graphics.drawable.VectorDrawableCompat.VGroup mRootGroup;

        float mBaseWidth = 0;

        float mBaseHeight = 0;

        float mViewportWidth = 0;

        float mViewportHeight = 0;

        int mRootAlpha = 0xff;

        java.lang.String mRootName = null;

        final android.support.v4.util.ArrayMap<java.lang.String, java.lang.Object> mVGTargetsMap = new android.support.v4.util.ArrayMap<java.lang.String, java.lang.Object>();

        public VPathRenderer() {
            mRootGroup = new android.support.graphics.drawable.VectorDrawableCompat.VGroup();
            mPath = new android.graphics.Path();
            mRenderPath = new android.graphics.Path();
        }

        public void setRootAlpha(int alpha) {
            mRootAlpha = alpha;
        }

        public int getRootAlpha() {
            return mRootAlpha;
        }

        // setAlpha() and getAlpha() are used mostly for animation purpose, since
        // Animator like to use alpha from 0 to 1.
        public void setAlpha(float alpha) {
            setRootAlpha(((int) (alpha * 255)));
        }

        @java.lang.SuppressWarnings("unused")
        public float getAlpha() {
            return getRootAlpha() / 255.0F;
        }

        public VPathRenderer(android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer copy) {
            mRootGroup = new android.support.graphics.drawable.VectorDrawableCompat.VGroup(copy.mRootGroup, mVGTargetsMap);
            mPath = new android.graphics.Path(copy.mPath);
            mRenderPath = new android.graphics.Path(copy.mRenderPath);
            mBaseWidth = copy.mBaseWidth;
            mBaseHeight = copy.mBaseHeight;
            mViewportWidth = copy.mViewportWidth;
            mViewportHeight = copy.mViewportHeight;
            mChangingConfigurations = copy.mChangingConfigurations;
            mRootAlpha = copy.mRootAlpha;
            mRootName = copy.mRootName;
            if (copy.mRootName != null) {
                mVGTargetsMap.put(copy.mRootName, this);
            }
        }

        private void drawGroupTree(android.support.graphics.drawable.VectorDrawableCompat.VGroup currentGroup, android.graphics.Matrix currentMatrix, android.graphics.Canvas canvas, int w, int h, android.graphics.ColorFilter filter) {
            // Calculate current group's matrix by preConcat the parent's and
            // and the current one on the top of the stack.
            // Basically the Mfinal = Mviewport * M0 * M1 * M2;
            // Mi the local matrix at level i of the group tree.
            currentGroup.mStackedMatrix.set(currentMatrix);
            currentGroup.mStackedMatrix.preConcat(currentGroup.mLocalMatrix);
            // Save the current clip information, which is local to this group.
            canvas.save();
            // Draw the group tree in the same order as the XML file.
            for (int i = 0; i < currentGroup.mChildren.size(); i++) {
                java.lang.Object child = currentGroup.mChildren.get(i);
                if (child instanceof android.support.graphics.drawable.VectorDrawableCompat.VGroup) {
                    android.support.graphics.drawable.VectorDrawableCompat.VGroup childGroup = ((android.support.graphics.drawable.VectorDrawableCompat.VGroup) (child));
                    drawGroupTree(childGroup, currentGroup.mStackedMatrix, canvas, w, h, filter);
                } else
                    if (child instanceof android.support.graphics.drawable.VectorDrawableCompat.VPath) {
                        android.support.graphics.drawable.VectorDrawableCompat.VPath childPath = ((android.support.graphics.drawable.VectorDrawableCompat.VPath) (child));
                        drawPath(currentGroup, childPath, canvas, w, h, filter);
                    }

            }
            canvas.restore();
        }

        public void draw(android.graphics.Canvas canvas, int w, int h, android.graphics.ColorFilter filter) {
            // Traverse the tree in pre-order to draw.
            drawGroupTree(mRootGroup, android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer.IDENTITY_MATRIX, canvas, w, h, filter);
        }

        private void drawPath(android.support.graphics.drawable.VectorDrawableCompat.VGroup vGroup, android.support.graphics.drawable.VectorDrawableCompat.VPath vPath, android.graphics.Canvas canvas, int w, int h, android.graphics.ColorFilter filter) {
            final float scaleX = w / mViewportWidth;
            final float scaleY = h / mViewportHeight;
            final float minScale = java.lang.Math.min(scaleX, scaleY);
            final android.graphics.Matrix groupStackedMatrix = vGroup.mStackedMatrix;
            mFinalPathMatrix.set(groupStackedMatrix);
            mFinalPathMatrix.postScale(scaleX, scaleY);
            final float matrixScale = getMatrixScale(groupStackedMatrix);
            if (matrixScale == 0) {
                // When either x or y is scaled to 0, we don't need to draw anything.
                return;
            }
            vPath.toPath(mPath);
            final android.graphics.Path path = mPath;
            mRenderPath.reset();
            if (vPath.isClipPath()) {
                mRenderPath.addPath(path, mFinalPathMatrix);
                canvas.clipPath(mRenderPath);
            } else {
                android.support.graphics.drawable.VectorDrawableCompat.VFullPath fullPath = ((android.support.graphics.drawable.VectorDrawableCompat.VFullPath) (vPath));
                if ((fullPath.mTrimPathStart != 0.0F) || (fullPath.mTrimPathEnd != 1.0F)) {
                    float start = (fullPath.mTrimPathStart + fullPath.mTrimPathOffset) % 1.0F;
                    float end = (fullPath.mTrimPathEnd + fullPath.mTrimPathOffset) % 1.0F;
                    if (mPathMeasure == null) {
                        mPathMeasure = new android.graphics.PathMeasure();
                    }
                    mPathMeasure.setPath(mPath, false);
                    float len = mPathMeasure.getLength();
                    start = start * len;
                    end = end * len;
                    path.reset();
                    if (start > end) {
                        mPathMeasure.getSegment(start, len, path, true);
                        mPathMeasure.getSegment(0.0F, end, path, true);
                    } else {
                        mPathMeasure.getSegment(start, end, path, true);
                    }
                    path.rLineTo(0, 0);// fix bug in measure

                }
                mRenderPath.addPath(path, mFinalPathMatrix);
                if (fullPath.mFillColor != android.graphics.Color.TRANSPARENT) {
                    if (mFillPaint == null) {
                        mFillPaint = new android.graphics.Paint();
                        mFillPaint.setStyle(android.graphics.Paint.Style.FILL);
                        mFillPaint.setAntiAlias(true);
                    }
                    final android.graphics.Paint fillPaint = mFillPaint;
                    fillPaint.setColor(android.support.graphics.drawable.VectorDrawableCompat.applyAlpha(fullPath.mFillColor, fullPath.mFillAlpha));
                    fillPaint.setColorFilter(filter);
                    canvas.drawPath(mRenderPath, fillPaint);
                }
                if (fullPath.mStrokeColor != android.graphics.Color.TRANSPARENT) {
                    if (mStrokePaint == null) {
                        mStrokePaint = new android.graphics.Paint();
                        mStrokePaint.setStyle(android.graphics.Paint.Style.STROKE);
                        mStrokePaint.setAntiAlias(true);
                    }
                    final android.graphics.Paint strokePaint = mStrokePaint;
                    if (fullPath.mStrokeLineJoin != null) {
                        strokePaint.setStrokeJoin(fullPath.mStrokeLineJoin);
                    }
                    if (fullPath.mStrokeLineCap != null) {
                        strokePaint.setStrokeCap(fullPath.mStrokeLineCap);
                    }
                    strokePaint.setStrokeMiter(fullPath.mStrokeMiterlimit);
                    strokePaint.setColor(android.support.graphics.drawable.VectorDrawableCompat.applyAlpha(fullPath.mStrokeColor, fullPath.mStrokeAlpha));
                    strokePaint.setColorFilter(filter);
                    final float finalStrokeScale = minScale * matrixScale;
                    strokePaint.setStrokeWidth(fullPath.mStrokeWidth * finalStrokeScale);
                    canvas.drawPath(mRenderPath, strokePaint);
                }
            }
        }

        private static float cross(float v1x, float v1y, float v2x, float v2y) {
            return (v1x * v2y) - (v1y * v2x);
        }

        private float getMatrixScale(android.graphics.Matrix groupStackedMatrix) {
            // Given unit vectors A = (0, 1) and B = (1, 0).
            // After matrix mapping, we got A' and B'. Let theta = the angel b/t A' and B'.
            // Therefore, the final scale we want is min(|A'| * sin(theta), |B'| * sin(theta)),
            // which is (|A'| * |B'| * sin(theta)) / max (|A'|, |B'|);
            // If  max (|A'|, |B'|) = 0, that means either x or y has a scale of 0.
            // 
            // For non-skew case, which is most of the cases, matrix scale is computing exactly the
            // scale on x and y axis, and take the minimal of these two.
            // For skew case, an unit square will mapped to a parallelogram. And this function will
            // return the minimal height of the 2 bases.
            float[] unitVectors = new float[]{ 0, 1, 1, 0 };
            groupStackedMatrix.mapVectors(unitVectors);
            float scaleX = ((float) (java.lang.Math.hypot(unitVectors[0], unitVectors[1])));
            float scaleY = ((float) (java.lang.Math.hypot(unitVectors[2], unitVectors[3])));
            float crossProduct = android.support.graphics.drawable.VectorDrawableCompat.VPathRenderer.cross(unitVectors[0], unitVectors[1], unitVectors[2], unitVectors[3]);
            float maxScale = java.lang.Math.max(scaleX, scaleY);
            float matrixScale = 0;
            if (maxScale > 0) {
                matrixScale = java.lang.Math.abs(crossProduct) / maxScale;
            }
            if (android.support.graphics.drawable.VectorDrawableCompat.DBG_VECTOR_DRAWABLE) {
                android.util.Log.d(android.support.graphics.drawable.VectorDrawableCompat.LOGTAG, (((("Scale x " + scaleX) + " y ") + scaleY) + " final ") + matrixScale);
            }
            return matrixScale;
        }
    }

    private static class VGroup {
        // mStackedMatrix is only used temporarily when drawing, it combines all
        // the parents' local matrices with the current one.
        private final android.graphics.Matrix mStackedMatrix = new android.graphics.Matrix();

        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        final java.util.ArrayList<java.lang.Object> mChildren = new java.util.ArrayList<java.lang.Object>();

        float mRotate = 0;

        private float mPivotX = 0;

        private float mPivotY = 0;

        private float mScaleX = 1;

        private float mScaleY = 1;

        private float mTranslateX = 0;

        private float mTranslateY = 0;

        // mLocalMatrix is updated based on the update of transformation information,
        // either parsed from the XML or by animation.
        private final android.graphics.Matrix mLocalMatrix = new android.graphics.Matrix();

        int mChangingConfigurations;

        private int[] mThemeAttrs;

        private java.lang.String mGroupName = null;

        public VGroup(android.support.graphics.drawable.VectorDrawableCompat.VGroup copy, android.support.v4.util.ArrayMap<java.lang.String, java.lang.Object> targetsMap) {
            mRotate = copy.mRotate;
            mPivotX = copy.mPivotX;
            mPivotY = copy.mPivotY;
            mScaleX = copy.mScaleX;
            mScaleY = copy.mScaleY;
            mTranslateX = copy.mTranslateX;
            mTranslateY = copy.mTranslateY;
            mThemeAttrs = copy.mThemeAttrs;
            mGroupName = copy.mGroupName;
            mChangingConfigurations = copy.mChangingConfigurations;
            if (mGroupName != null) {
                targetsMap.put(mGroupName, this);
            }
            mLocalMatrix.set(copy.mLocalMatrix);
            final java.util.ArrayList<java.lang.Object> children = copy.mChildren;
            for (int i = 0; i < children.size(); i++) {
                java.lang.Object copyChild = children.get(i);
                if (copyChild instanceof android.support.graphics.drawable.VectorDrawableCompat.VGroup) {
                    android.support.graphics.drawable.VectorDrawableCompat.VGroup copyGroup = ((android.support.graphics.drawable.VectorDrawableCompat.VGroup) (copyChild));
                    mChildren.add(new android.support.graphics.drawable.VectorDrawableCompat.VGroup(copyGroup, targetsMap));
                } else {
                    android.support.graphics.drawable.VectorDrawableCompat.VPath newPath = null;
                    if (copyChild instanceof android.support.graphics.drawable.VectorDrawableCompat.VFullPath) {
                        newPath = new android.support.graphics.drawable.VectorDrawableCompat.VFullPath(((android.support.graphics.drawable.VectorDrawableCompat.VFullPath) (copyChild)));
                    } else
                        if (copyChild instanceof android.support.graphics.drawable.VectorDrawableCompat.VClipPath) {
                            newPath = new android.support.graphics.drawable.VectorDrawableCompat.VClipPath(((android.support.graphics.drawable.VectorDrawableCompat.VClipPath) (copyChild)));
                        } else {
                            throw new java.lang.IllegalStateException("Unknown object in the tree!");
                        }

                    mChildren.add(newPath);
                    if (newPath.mPathName != null) {
                        targetsMap.put(newPath.mPathName, newPath);
                    }
                }
            }
        }

        public VGroup() {
        }

        public java.lang.String getGroupName() {
            return mGroupName;
        }

        public android.graphics.Matrix getLocalMatrix() {
            return mLocalMatrix;
        }

        public void inflate(android.content.res.Resources res, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser) {
            final android.content.res.TypedArray a = android.support.graphics.drawable.VectorDrawableCommon.obtainAttributes(res, theme, attrs, android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup);
            updateStateFromTypedArray(a, parser);
            a.recycle();
        }

        private void updateStateFromTypedArray(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser) {
            // Account for any configuration changes.
            // mChangingConfigurations |= Utils.getChangingConfigurations(a);
            // Extract the theme attributes, if any.
            mThemeAttrs = null;// TODO TINT THEME Not supported yet a.extractThemeAttrs();

            // This is added in API 11
            mRotate = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "rotation", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_rotation, mRotate);
            mPivotX = a.getFloat(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_pivotX, mPivotX);
            mPivotY = a.getFloat(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_pivotY, mPivotY);
            // This is added in API 11
            mScaleX = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "scaleX", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_scaleX, mScaleX);
            // This is added in API 11
            mScaleY = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "scaleY", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_scaleY, mScaleY);
            mTranslateX = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "translateX", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_translateX, mTranslateX);
            mTranslateY = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "translateY", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_translateY, mTranslateY);
            final java.lang.String groupName = a.getString(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableGroup_name);
            if (groupName != null) {
                mGroupName = groupName;
            }
            updateLocalMatrix();
        }

        private void updateLocalMatrix() {
            // The order we apply is the same as the
            // RenderNode.cpp::applyViewPropertyTransforms().
            mLocalMatrix.reset();
            mLocalMatrix.postTranslate(-mPivotX, -mPivotY);
            mLocalMatrix.postScale(mScaleX, mScaleY);
            mLocalMatrix.postRotate(mRotate, 0, 0);
            mLocalMatrix.postTranslate(mTranslateX + mPivotX, mTranslateY + mPivotY);
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        @java.lang.SuppressWarnings("unused")
        public float getRotation() {
            return mRotate;
        }

        @java.lang.SuppressWarnings("unused")
        public void setRotation(float rotation) {
            if (rotation != mRotate) {
                mRotate = rotation;
                updateLocalMatrix();
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getPivotX() {
            return mPivotX;
        }

        @java.lang.SuppressWarnings("unused")
        public void setPivotX(float pivotX) {
            if (pivotX != mPivotX) {
                mPivotX = pivotX;
                updateLocalMatrix();
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getPivotY() {
            return mPivotY;
        }

        @java.lang.SuppressWarnings("unused")
        public void setPivotY(float pivotY) {
            if (pivotY != mPivotY) {
                mPivotY = pivotY;
                updateLocalMatrix();
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getScaleX() {
            return mScaleX;
        }

        @java.lang.SuppressWarnings("unused")
        public void setScaleX(float scaleX) {
            if (scaleX != mScaleX) {
                mScaleX = scaleX;
                updateLocalMatrix();
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getScaleY() {
            return mScaleY;
        }

        @java.lang.SuppressWarnings("unused")
        public void setScaleY(float scaleY) {
            if (scaleY != mScaleY) {
                mScaleY = scaleY;
                updateLocalMatrix();
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getTranslateX() {
            return mTranslateX;
        }

        @java.lang.SuppressWarnings("unused")
        public void setTranslateX(float translateX) {
            if (translateX != mTranslateX) {
                mTranslateX = translateX;
                updateLocalMatrix();
            }
        }

        @java.lang.SuppressWarnings("unused")
        public float getTranslateY() {
            return mTranslateY;
        }

        @java.lang.SuppressWarnings("unused")
        public void setTranslateY(float translateY) {
            if (translateY != mTranslateY) {
                mTranslateY = translateY;
                updateLocalMatrix();
            }
        }
    }

    /**
     * Common Path information for clip path and normal path.
     */
    private static class VPath {
        protected android.support.graphics.drawable.PathParser.PathDataNode[] mNodes = null;

        java.lang.String mPathName;

        int mChangingConfigurations;

        public VPath() {
            // Empty constructor.
        }

        public void printVPath(int level) {
            java.lang.String indent = "";
            for (int i = 0; i < level; i++) {
                indent += "    ";
            }
            android.util.Log.v(android.support.graphics.drawable.VectorDrawableCompat.LOGTAG, (((indent + "current path is :") + mPathName) + " pathData is ") + NodesToString(mNodes));
        }

        public java.lang.String NodesToString(android.support.graphics.drawable.PathParser.PathDataNode[] nodes) {
            java.lang.String result = " ";
            for (int i = 0; i < nodes.length; i++) {
                result += nodes[i].type + ":";
                float[] params = nodes[i].params;
                for (int j = 0; j < params.length; j++) {
                    result += params[j] + ",";
                }
            }
            return result;
        }

        public VPath(android.support.graphics.drawable.VectorDrawableCompat.VPath copy) {
            mPathName = copy.mPathName;
            mChangingConfigurations = copy.mChangingConfigurations;
            mNodes = android.support.graphics.drawable.PathParser.deepCopyNodes(copy.mNodes);
        }

        public void toPath(android.graphics.Path path) {
            path.reset();
            if (mNodes != null) {
                android.support.graphics.drawable.PathParser.PathDataNode.nodesToPath(mNodes, path);
            }
        }

        public java.lang.String getPathName() {
            return mPathName;
        }

        public boolean canApplyTheme() {
            return false;
        }

        public void applyTheme(android.content.res.Resources.Theme t) {
        }

        public boolean isClipPath() {
            return false;
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        @java.lang.SuppressWarnings("unused")
        public android.support.graphics.drawable.PathParser.PathDataNode[] getPathData() {
            return mNodes;
        }

        @java.lang.SuppressWarnings("unused")
        public void setPathData(android.support.graphics.drawable.PathParser.PathDataNode[] nodes) {
            if (!android.support.graphics.drawable.PathParser.canMorph(mNodes, nodes)) {
                // This should not happen in the middle of animation.
                mNodes = android.support.graphics.drawable.PathParser.deepCopyNodes(nodes);
            } else {
                android.support.graphics.drawable.PathParser.updateNodes(mNodes, nodes);
            }
        }
    }

    /**
     * Clip path, which only has name and pathData.
     */
    private static class VClipPath extends android.support.graphics.drawable.VectorDrawableCompat.VPath {
        public VClipPath() {
            // Empty constructor.
        }

        public VClipPath(android.support.graphics.drawable.VectorDrawableCompat.VClipPath copy) {
            super(copy);
        }

        public void inflate(android.content.res.Resources r, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser) {
            // TODO TINT THEME Not supported yet
            final boolean hasPathData = android.support.graphics.drawable.TypedArrayUtils.hasAttribute(parser, "pathData");
            if (!hasPathData) {
                return;
            }
            final android.content.res.TypedArray a = android.support.graphics.drawable.VectorDrawableCommon.obtainAttributes(r, theme, attrs, android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableClipPath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        private void updateStateFromTypedArray(android.content.res.TypedArray a) {
            // Account for any configuration changes.
            // mChangingConfigurations |= Utils.getChangingConfigurations(a);;
            final java.lang.String pathName = a.getString(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableClipPath_name);
            if (pathName != null) {
                mPathName = pathName;
            }
            final java.lang.String pathData = a.getString(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawableClipPath_pathData);
            if (pathData != null) {
                mNodes = android.support.graphics.drawable.PathParser.createNodesFromPathData(pathData);
            }
        }

        @java.lang.Override
        public boolean isClipPath() {
            return true;
        }
    }

    /**
     * Normal path, which contains all the fill / paint information.
     */
    private static class VFullPath extends android.support.graphics.drawable.VectorDrawableCompat.VPath {
        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        private int[] mThemeAttrs;

        int mStrokeColor = android.graphics.Color.TRANSPARENT;

        float mStrokeWidth = 0;

        int mFillColor = android.graphics.Color.TRANSPARENT;

        float mStrokeAlpha = 1.0F;

        int mFillRule;

        float mFillAlpha = 1.0F;

        float mTrimPathStart = 0;

        float mTrimPathEnd = 1;

        float mTrimPathOffset = 0;

        android.graphics.Paint.Cap mStrokeLineCap = android.graphics.Paint.Cap.BUTT;

        android.graphics.Paint.Join mStrokeLineJoin = android.graphics.Paint.Join.MITER;

        float mStrokeMiterlimit = 4;

        public VFullPath() {
            // Empty constructor.
        }

        public VFullPath(android.support.graphics.drawable.VectorDrawableCompat.VFullPath copy) {
            super(copy);
            mThemeAttrs = copy.mThemeAttrs;
            mStrokeColor = copy.mStrokeColor;
            mStrokeWidth = copy.mStrokeWidth;
            mStrokeAlpha = copy.mStrokeAlpha;
            mFillColor = copy.mFillColor;
            mFillRule = copy.mFillRule;
            mFillAlpha = copy.mFillAlpha;
            mTrimPathStart = copy.mTrimPathStart;
            mTrimPathEnd = copy.mTrimPathEnd;
            mTrimPathOffset = copy.mTrimPathOffset;
            mStrokeLineCap = copy.mStrokeLineCap;
            mStrokeLineJoin = copy.mStrokeLineJoin;
            mStrokeMiterlimit = copy.mStrokeMiterlimit;
        }

        private android.graphics.Paint.Cap getStrokeLineCap(int id, android.graphics.Paint.Cap defValue) {
            switch (id) {
                case android.support.graphics.drawable.VectorDrawableCompat.LINECAP_BUTT :
                    return android.graphics.Paint.Cap.BUTT;
                case android.support.graphics.drawable.VectorDrawableCompat.LINECAP_ROUND :
                    return android.graphics.Paint.Cap.ROUND;
                case android.support.graphics.drawable.VectorDrawableCompat.LINECAP_SQUARE :
                    return android.graphics.Paint.Cap.SQUARE;
                default :
                    return defValue;
            }
        }

        private android.graphics.Paint.Join getStrokeLineJoin(int id, android.graphics.Paint.Join defValue) {
            switch (id) {
                case android.support.graphics.drawable.VectorDrawableCompat.LINEJOIN_MITER :
                    return android.graphics.Paint.Join.MITER;
                case android.support.graphics.drawable.VectorDrawableCompat.LINEJOIN_ROUND :
                    return android.graphics.Paint.Join.ROUND;
                case android.support.graphics.drawable.VectorDrawableCompat.LINEJOIN_BEVEL :
                    return android.graphics.Paint.Join.BEVEL;
                default :
                    return defValue;
            }
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return mThemeAttrs != null;
        }

        public void inflate(android.content.res.Resources r, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser) {
            final android.content.res.TypedArray a = android.support.graphics.drawable.VectorDrawableCommon.obtainAttributes(r, theme, attrs, android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath);
            updateStateFromTypedArray(a, parser);
            a.recycle();
        }

        private void updateStateFromTypedArray(android.content.res.TypedArray a, org.xmlpull.v1.XmlPullParser parser) {
            // Account for any configuration changes.
            // mChangingConfigurations |= Utils.getChangingConfigurations(a);
            // Extract the theme attributes, if any.
            mThemeAttrs = null;// TODO TINT THEME Not supported yet a.extractThemeAttrs();

            // In order to work around the conflicting id issue, we need to double check the
            // existence of the attribute.
            // B/c if the attribute existed in the compiled XML, then calling TypedArray will be
            // safe since the framework will look up in the XML first.
            // Note that each getAttributeValue take roughly 0.03ms, it is a price we have to pay.
            final boolean hasPathData = android.support.graphics.drawable.TypedArrayUtils.hasAttribute(parser, "pathData");
            if (!hasPathData) {
                // If there is no pathData in the <path> tag, then this is an empty path,
                // nothing need to be drawn.
                return;
            }
            final java.lang.String pathName = a.getString(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_name);
            if (pathName != null) {
                mPathName = pathName;
            }
            final java.lang.String pathData = a.getString(android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_pathData);
            if (pathData != null) {
                mNodes = android.support.graphics.drawable.PathParser.createNodesFromPathData(pathData);
            }
            mFillColor = android.support.graphics.drawable.TypedArrayUtils.getNamedColor(a, parser, "fillColor", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_fillColor, mFillColor);
            mFillAlpha = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "fillAlpha", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_fillAlpha, mFillAlpha);
            final int lineCap = android.support.graphics.drawable.TypedArrayUtils.getNamedInt(a, parser, "strokeLineCap", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_strokeLineCap, -1);
            mStrokeLineCap = getStrokeLineCap(lineCap, mStrokeLineCap);
            final int lineJoin = android.support.graphics.drawable.TypedArrayUtils.getNamedInt(a, parser, "strokeLineJoin", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_strokeLineJoin, -1);
            mStrokeLineJoin = getStrokeLineJoin(lineJoin, mStrokeLineJoin);
            mStrokeMiterlimit = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "strokeMiterLimit", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_strokeMiterLimit, mStrokeMiterlimit);
            mStrokeColor = android.support.graphics.drawable.TypedArrayUtils.getNamedColor(a, parser, "strokeColor", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_strokeColor, mStrokeColor);
            mStrokeAlpha = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "strokeAlpha", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_strokeAlpha, mStrokeAlpha);
            mStrokeWidth = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "strokeWidth", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_strokeWidth, mStrokeWidth);
            mTrimPathEnd = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "trimPathEnd", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_trimPathEnd, mTrimPathEnd);
            mTrimPathOffset = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "trimPathOffset", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_trimPathOffset, mTrimPathOffset);
            mTrimPathStart = android.support.graphics.drawable.TypedArrayUtils.getNamedFloat(a, parser, "trimPathStart", android.support.graphics.drawable.AndroidResources.styleable_VectorDrawablePath_trimPathStart, mTrimPathStart);
        }

        @java.lang.Override
        public void applyTheme(android.content.res.Resources.Theme t) {
            if (mThemeAttrs == null) {
                return;
            }
            /* TODO TINT THEME Not supported yet final TypedArray a =
            t.resolveAttributes(mThemeAttrs, styleable_VectorDrawablePath);
            updateStateFromTypedArray(a); a.recycle();
             */
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        @java.lang.SuppressWarnings("unused")
        int getStrokeColor() {
            return mStrokeColor;
        }

        @java.lang.SuppressWarnings("unused")
        void setStrokeColor(int strokeColor) {
            mStrokeColor = strokeColor;
        }

        @java.lang.SuppressWarnings("unused")
        float getStrokeWidth() {
            return mStrokeWidth;
        }

        @java.lang.SuppressWarnings("unused")
        void setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
        }

        @java.lang.SuppressWarnings("unused")
        float getStrokeAlpha() {
            return mStrokeAlpha;
        }

        @java.lang.SuppressWarnings("unused")
        void setStrokeAlpha(float strokeAlpha) {
            mStrokeAlpha = strokeAlpha;
        }

        @java.lang.SuppressWarnings("unused")
        int getFillColor() {
            return mFillColor;
        }

        @java.lang.SuppressWarnings("unused")
        void setFillColor(int fillColor) {
            mFillColor = fillColor;
        }

        @java.lang.SuppressWarnings("unused")
        float getFillAlpha() {
            return mFillAlpha;
        }

        @java.lang.SuppressWarnings("unused")
        void setFillAlpha(float fillAlpha) {
            mFillAlpha = fillAlpha;
        }

        @java.lang.SuppressWarnings("unused")
        float getTrimPathStart() {
            return mTrimPathStart;
        }

        @java.lang.SuppressWarnings("unused")
        void setTrimPathStart(float trimPathStart) {
            mTrimPathStart = trimPathStart;
        }

        @java.lang.SuppressWarnings("unused")
        float getTrimPathEnd() {
            return mTrimPathEnd;
        }

        @java.lang.SuppressWarnings("unused")
        void setTrimPathEnd(float trimPathEnd) {
            mTrimPathEnd = trimPathEnd;
        }

        @java.lang.SuppressWarnings("unused")
        float getTrimPathOffset() {
            return mTrimPathOffset;
        }

        @java.lang.SuppressWarnings("unused")
        void setTrimPathOffset(float trimPathOffset) {
            mTrimPathOffset = trimPathOffset;
        }
    }
}

