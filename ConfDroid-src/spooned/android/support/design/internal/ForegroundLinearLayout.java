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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ForegroundLinearLayout extends android.support.v7.widget.LinearLayoutCompat {
    private android.graphics.drawable.Drawable mForeground;

    private final android.graphics.Rect mSelfBounds = new android.graphics.Rect();

    private final android.graphics.Rect mOverlayBounds = new android.graphics.Rect();

    private int mForegroundGravity = android.view.Gravity.FILL;

    protected boolean mForegroundInPadding = true;

    boolean mForegroundBoundsChanged = false;

    public ForegroundLinearLayout(android.content.Context context) {
        this(context, null);
    }

    public ForegroundLinearLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ForegroundLinearLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundLinearLayout, defStyle, 0);
        mForegroundGravity = a.getInt(R.styleable.ForegroundLinearLayout_android_foregroundGravity, mForegroundGravity);
        final android.graphics.drawable.Drawable d = a.getDrawable(R.styleable.ForegroundLinearLayout_android_foreground);
        if (d != null) {
            setForeground(d);
        }
        mForegroundInPadding = a.getBoolean(R.styleable.ForegroundLinearLayout_foregroundInsidePadding, true);
        a.recycle();
    }

    /**
     * Describes how the foreground is positioned.
     *
     * @return foreground gravity.
     * @see #setForegroundGravity(int)
     */
    public int getForegroundGravity() {
        return mForegroundGravity;
    }

    /**
     * Describes how the foreground is positioned. Defaults to START and TOP.
     *
     * @param foregroundGravity
     * 		See {@link android.view.Gravity}
     * @see #getForegroundGravity()
     */
    public void setForegroundGravity(int foregroundGravity) {
        if (mForegroundGravity != foregroundGravity) {
            if ((foregroundGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                foregroundGravity |= android.view.Gravity.START;
            }
            if ((foregroundGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                foregroundGravity |= android.view.Gravity.TOP;
            }
            mForegroundGravity = foregroundGravity;
            if ((mForegroundGravity == android.view.Gravity.FILL) && (mForeground != null)) {
                android.graphics.Rect padding = new android.graphics.Rect();
                mForeground.getPadding(padding);
            }
            requestLayout();
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(android.graphics.drawable.Drawable who) {
        return super.verifyDrawable(who) || (who == mForeground);
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mForeground != null) {
            mForeground.jumpToCurrentState();
        }
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if ((mForeground != null) && mForeground.isStateful()) {
            mForeground.setState(getDrawableState());
        }
    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in the frame layout.  Any padding in the Drawable will be taken
     * into account by ensuring that the children are inset to be placed
     * inside of the padding area.
     *
     * @param drawable
     * 		The Drawable to be drawn on top of the children.
     */
    public void setForeground(android.graphics.drawable.Drawable drawable) {
        if (mForeground != drawable) {
            if (mForeground != null) {
                mForeground.setCallback(null);
                unscheduleDrawable(mForeground);
            }
            mForeground = drawable;
            if (drawable != null) {
                setWillNotDraw(false);
                drawable.setCallback(this);
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                if (mForegroundGravity == android.view.Gravity.FILL) {
                    android.graphics.Rect padding = new android.graphics.Rect();
                    drawable.getPadding(padding);
                }
            } else {
                setWillNotDraw(true);
            }
            requestLayout();
            invalidate();
        }
    }

    /**
     * Returns the drawable used as the foreground of this FrameLayout. The
     * foreground drawable, if non-null, is always drawn on top of the children.
     *
     * @return A Drawable or null if no foreground was set.
     */
    public android.graphics.drawable.Drawable getForeground() {
        return mForeground;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mForegroundBoundsChanged |= changed;
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mForegroundBoundsChanged = true;
    }

    @java.lang.Override
    public void draw(@android.support.annotation.NonNull
    android.graphics.Canvas canvas) {
        super.draw(canvas);
        if (mForeground != null) {
            final android.graphics.drawable.Drawable foreground = mForeground;
            if (mForegroundBoundsChanged) {
                mForegroundBoundsChanged = false;
                final android.graphics.Rect selfBounds = mSelfBounds;
                final android.graphics.Rect overlayBounds = mOverlayBounds;
                final int w = getRight() - getLeft();
                final int h = getBottom() - getTop();
                if (mForegroundInPadding) {
                    selfBounds.set(0, 0, w, h);
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
                }
                android.view.Gravity.apply(mForegroundGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds);
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mForeground != null) {
            mForeground.setHotspot(x, y);
        }
    }
}

