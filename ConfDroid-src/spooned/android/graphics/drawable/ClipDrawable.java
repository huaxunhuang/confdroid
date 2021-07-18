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
package android.graphics.drawable;


/**
 * A Drawable that clips another Drawable based on this Drawable's current
 * level value.  You can control how much the child Drawable gets clipped in width
 * and height based on the level, as well as a gravity to control where it is
 * placed in its overall container.  Most often used to implement things like
 * progress bars, by increasing the drawable's level with {@link android.graphics.drawable.Drawable#setLevel(int) setLevel()}.
 * <p class="note"><strong>Note:</strong> The drawable is clipped completely and not visible when
 * the level is 0 and fully revealed when the level is 10,000.</p>
 *
 * <p>It can be defined in an XML file with the <code>&lt;clip></code> element.  For more
 * information, see the guide to <a
 * href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @unknown ref android.R.styleable#ClipDrawable_clipOrientation
 * @unknown ref android.R.styleable#ClipDrawable_gravity
 * @unknown ref android.R.styleable#ClipDrawable_drawable
 */
public class ClipDrawable extends android.graphics.drawable.DrawableWrapper {
    public static final int HORIZONTAL = 1;

    public static final int VERTICAL = 2;

    private static final int MAX_LEVEL = 10000;

    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.ClipDrawable.ClipState mState;

    ClipDrawable() {
        this(new android.graphics.drawable.ClipDrawable.ClipState(null, null), null);
    }

    /**
     * Creates a new clip drawable with the specified gravity and orientation.
     *
     * @param drawable
     * 		the drawable to clip
     * @param gravity
     * 		gravity constant (see {@link Gravity} used to position
     * 		the clipped drawable within the parent container
     * @param orientation
     * 		bitwise-or of {@link #HORIZONTAL} and/or
     * 		{@link #VERTICAL}
     */
    public ClipDrawable(android.graphics.drawable.Drawable drawable, int gravity, int orientation) {
        this(new android.graphics.drawable.ClipDrawable.ClipState(null, null), null);
        mState.mGravity = gravity;
        mState.mOrientation = orientation;
        setDrawable(drawable);
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.ClipDrawable);
        // Inflation will advance the XmlPullParser and AttributeSet.
        super.inflate(r, parser, attrs, theme);
        updateStateFromTypedArray(a);
        verifyRequiredAttributes(a);
        a.recycle();
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.ClipDrawable.ClipState state = mState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.ClipDrawable);
            try {
                updateStateFromTypedArray(a);
                verifyRequiredAttributes(a);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.graphics.drawable.Drawable.rethrowAsRuntimeException(e);
            } finally {
                a.recycle();
            }
        }
    }

    private void verifyRequiredAttributes(@android.annotation.NonNull
    android.content.res.TypedArray a) throws org.xmlpull.v1.XmlPullParserException {
        // If we're not waiting on a theme, verify required attributes.
        if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[R.styleable.ClipDrawable_drawable] == 0))) {
            throw new org.xmlpull.v1.XmlPullParserException((a.getPositionDescription() + ": <clip> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
        }
    }

    private void updateStateFromTypedArray(@android.annotation.NonNull
    android.content.res.TypedArray a) {
        final android.graphics.drawable.ClipDrawable.ClipState state = mState;
        if (state == null) {
            return;
        }
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mOrientation = a.getInt(R.styleable.ClipDrawable_clipOrientation, state.mOrientation);
        state.mGravity = a.getInt(R.styleable.ClipDrawable_gravity, state.mGravity);
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        super.onLevelChange(level);
        invalidateSelf();
        return true;
    }

    @java.lang.Override
    public int getOpacity() {
        final android.graphics.drawable.Drawable dr = getDrawable();
        final int opacity = dr.getOpacity();
        if ((opacity == android.graphics.PixelFormat.TRANSPARENT) || (dr.getLevel() == 0)) {
            return android.graphics.PixelFormat.TRANSPARENT;
        }
        final int level = getLevel();
        if (level >= android.graphics.drawable.ClipDrawable.MAX_LEVEL) {
            return dr.getOpacity();
        }
        // Some portion of non-transparent drawable is showing.
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.drawable.Drawable dr = getDrawable();
        if (dr.getLevel() == 0) {
            return;
        }
        final android.graphics.Rect r = mTmpRect;
        final android.graphics.Rect bounds = getBounds();
        final int level = getLevel();
        int w = bounds.width();
        final int iw = 0;// mState.mDrawable.getIntrinsicWidth();

        if ((mState.mOrientation & android.graphics.drawable.ClipDrawable.HORIZONTAL) != 0) {
            w -= ((w - iw) * (android.graphics.drawable.ClipDrawable.MAX_LEVEL - level)) / android.graphics.drawable.ClipDrawable.MAX_LEVEL;
        }
        int h = bounds.height();
        final int ih = 0;// mState.mDrawable.getIntrinsicHeight();

        if ((mState.mOrientation & android.graphics.drawable.ClipDrawable.VERTICAL) != 0) {
            h -= ((h - ih) * (android.graphics.drawable.ClipDrawable.MAX_LEVEL - level)) / android.graphics.drawable.ClipDrawable.MAX_LEVEL;
        }
        final int layoutDirection = getLayoutDirection();
        android.view.Gravity.apply(mState.mGravity, w, h, bounds, r, layoutDirection);
        if ((w > 0) && (h > 0)) {
            canvas.save();
            canvas.clipRect(r);
            dr.draw(canvas);
            canvas.restore();
        }
    }

    @java.lang.Override
    android.graphics.drawable.DrawableWrapper.DrawableWrapperState mutateConstantState() {
        mState = new android.graphics.drawable.ClipDrawable.ClipState(mState, null);
        return mState;
    }

    static final class ClipState extends android.graphics.drawable.DrawableWrapper.DrawableWrapperState {
        private int[] mThemeAttrs;

        int mOrientation = android.graphics.drawable.ClipDrawable.HORIZONTAL;

        int mGravity = android.view.Gravity.LEFT;

        ClipState(android.graphics.drawable.ClipDrawable.ClipState orig, android.content.res.Resources res) {
            super(orig, res);
            if (orig != null) {
                mOrientation = orig.mOrientation;
                mGravity = orig.mGravity;
            }
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.ClipDrawable(this, res);
        }
    }

    private ClipDrawable(android.graphics.drawable.ClipDrawable.ClipState state, android.content.res.Resources res) {
        super(state, res);
        mState = state;
    }
}

