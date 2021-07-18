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
package android.support.v7.widget;


class AppCompatSeekBarHelper extends android.support.v7.widget.AppCompatProgressBarHelper {
    private final android.widget.SeekBar mView;

    private android.graphics.drawable.Drawable mTickMark;

    private android.content.res.ColorStateList mTickMarkTintList = null;

    private android.graphics.PorterDuff.Mode mTickMarkTintMode = null;

    private boolean mHasTickMarkTint = false;

    private boolean mHasTickMarkTintMode = false;

    AppCompatSeekBarHelper(android.widget.SeekBar view) {
        super(view);
        mView = view;
    }

    @java.lang.Override
    void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        super.loadFromAttributes(attrs, defStyleAttr);
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mView.getContext(), attrs, R.styleable.AppCompatSeekBar, defStyleAttr, 0);
        final android.graphics.drawable.Drawable drawable = a.getDrawableIfKnown(R.styleable.AppCompatSeekBar_android_thumb);
        if (drawable != null) {
            mView.setThumb(drawable);
        }
        final android.graphics.drawable.Drawable tickMark = a.getDrawable(R.styleable.AppCompatSeekBar_tickMark);
        setTickMark(tickMark);
        if (a.hasValue(R.styleable.AppCompatSeekBar_tickMarkTintMode)) {
            mTickMarkTintMode = android.support.v7.widget.DrawableUtils.parseTintMode(a.getInt(R.styleable.AppCompatSeekBar_tickMarkTintMode, -1), mTickMarkTintMode);
            mHasTickMarkTintMode = true;
        }
        if (a.hasValue(R.styleable.AppCompatSeekBar_tickMarkTint)) {
            mTickMarkTintList = a.getColorStateList(R.styleable.AppCompatSeekBar_tickMarkTint);
            mHasTickMarkTint = true;
        }
        a.recycle();
        applyTickMarkTint();
    }

    void setTickMark(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable tickMark) {
        if (mTickMark != null) {
            mTickMark.setCallback(null);
        }
        mTickMark = tickMark;
        if (tickMark != null) {
            tickMark.setCallback(mView);
            android.support.v4.graphics.drawable.DrawableCompat.setLayoutDirection(tickMark, android.support.v4.view.ViewCompat.getLayoutDirection(mView));
            if (tickMark.isStateful()) {
                tickMark.setState(mView.getDrawableState());
            }
            applyTickMarkTint();
        }
        mView.invalidate();
    }

    @android.support.annotation.Nullable
    android.graphics.drawable.Drawable getTickMark() {
        return mTickMark;
    }

    void setTickMarkTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mTickMarkTintList = tint;
        mHasTickMarkTint = true;
        applyTickMarkTint();
    }

    @android.support.annotation.Nullable
    android.content.res.ColorStateList getTickMarkTintList() {
        return mTickMarkTintList;
    }

    void setTickMarkTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        mTickMarkTintMode = tintMode;
        mHasTickMarkTintMode = true;
        applyTickMarkTint();
    }

    @android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode getTickMarkTintMode() {
        return mTickMarkTintMode;
    }

    private void applyTickMarkTint() {
        if ((mTickMark != null) && (mHasTickMarkTint || mHasTickMarkTintMode)) {
            mTickMark = android.support.v4.graphics.drawable.DrawableCompat.wrap(mTickMark.mutate());
            if (mHasTickMarkTint) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintList(mTickMark, mTickMarkTintList);
            }
            if (mHasTickMarkTintMode) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mTickMark, mTickMarkTintMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mTickMark.isStateful()) {
                mTickMark.setState(mView.getDrawableState());
            }
        }
    }

    void jumpDrawablesToCurrentState() {
        if (mTickMark != null) {
            mTickMark.jumpToCurrentState();
        }
    }

    void drawableStateChanged() {
        final android.graphics.drawable.Drawable tickMark = mTickMark;
        if (((tickMark != null) && tickMark.isStateful()) && tickMark.setState(mView.getDrawableState())) {
            mView.invalidateDrawable(tickMark);
        }
    }

    /**
     * Draw the tick marks.
     */
    void drawTickMarks(android.graphics.Canvas canvas) {
        if (mTickMark != null) {
            final int count = mView.getMax();
            if (count > 1) {
                final int w = mTickMark.getIntrinsicWidth();
                final int h = mTickMark.getIntrinsicHeight();
                final int halfW = (w >= 0) ? w / 2 : 1;
                final int halfH = (h >= 0) ? h / 2 : 1;
                mTickMark.setBounds(-halfW, -halfH, halfW, halfH);
                final float spacing = ((mView.getWidth() - mView.getPaddingLeft()) - mView.getPaddingRight()) / ((float) (count));
                final int saveCount = canvas.save();
                canvas.translate(mView.getPaddingLeft(), mView.getHeight() / 2);
                for (int i = 0; i <= count; i++) {
                    mTickMark.draw(canvas);
                    canvas.translate(spacing, 0);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }
}

