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
package android.support.v17.leanback.widget;


/**
 * Implements foreground drawable before M and falls back to M's foreground implementation.
 */
class NonOverlappingLinearLayoutWithForeground extends android.widget.LinearLayout {
    private static final int VERSION_M = 23;

    private android.graphics.drawable.Drawable mForeground;

    private boolean mForegroundBoundsChanged;

    private final android.graphics.Rect mSelfBounds = new android.graphics.Rect();

    public NonOverlappingLinearLayoutWithForeground(android.content.Context context) {
        this(context, null);
    }

    public NonOverlappingLinearLayoutWithForeground(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NonOverlappingLinearLayoutWithForeground(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if ((context.getApplicationInfo().targetSdkVersion >= android.support.v17.leanback.widget.NonOverlappingLinearLayoutWithForeground.VERSION_M) && (android.os.Build.VERSION.SDK_INT >= android.support.v17.leanback.widget.NonOverlappingLinearLayoutWithForeground.VERSION_M)) {
            // don't need do anything, base View constructor >=M already reads the foreground if
            // targetSDK is >= M.
        } else {
            // in other cases, including M but targetSDK is less than M, we need setForeground in
            // code.
            android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, new int[]{ android.R.attr.foreground });
            android.graphics.drawable.Drawable d = a.getDrawable(0);
            if (d != null) {
                setForegroundCompat(d);
            }
            a.recycle();
        }
    }

    public void setForegroundCompat(android.graphics.drawable.Drawable d) {
        if (android.os.Build.VERSION.SDK_INT >= android.support.v17.leanback.widget.NonOverlappingLinearLayoutWithForeground.VERSION_M) {
            // From M,  foreground is naturally supported.
            android.support.v17.leanback.widget.ForegroundHelper.getInstance().setForeground(this, d);
        } else {
            // before M, do our own customized foreground draw.
            if (mForeground != d) {
                mForeground = d;
                mForegroundBoundsChanged = true;
                setWillNotDraw(false);
                mForeground.setCallback(this);
                if (mForeground.isStateful()) {
                    mForeground.setState(getDrawableState());
                }
            }
        }
    }

    public android.graphics.drawable.Drawable getForegroundCompat() {
        if (android.os.Build.VERSION.SDK_INT >= android.support.v17.leanback.widget.NonOverlappingLinearLayoutWithForeground.VERSION_M) {
            return android.support.v17.leanback.widget.ForegroundHelper.getInstance().getForeground(this);
        } else {
            return mForeground;
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        super.draw(canvas);
        if (mForeground != null) {
            final android.graphics.drawable.Drawable foreground = mForeground;
            if (mForegroundBoundsChanged) {
                mForegroundBoundsChanged = false;
                final android.graphics.Rect selfBounds = mSelfBounds;
                final int w = getRight() - getLeft();
                final int h = getBottom() - getTop();
                selfBounds.set(0, 0, w, h);
                foreground.setBounds(selfBounds);
            }
            foreground.draw(canvas);
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mForegroundBoundsChanged |= changed;
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
     * Avoids creating a hardware layer when animating alpha.
     */
    @java.lang.Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}

