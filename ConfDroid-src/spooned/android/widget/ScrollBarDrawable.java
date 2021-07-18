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
 * This is only used by View for displaying its scroll bars. It should probably
 * be moved in to the view package since it is used in that lower-level layer.
 * For now, we'll hide it so it can be cleaned up later.
 *
 * {@hide }
 */
public class ScrollBarDrawable extends android.graphics.drawable.Drawable implements android.graphics.drawable.Drawable.Callback {
    private android.graphics.drawable.Drawable mVerticalTrack;

    private android.graphics.drawable.Drawable mHorizontalTrack;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123768422)
    private android.graphics.drawable.Drawable mVerticalThumb;

    private android.graphics.drawable.Drawable mHorizontalThumb;

    private int mRange;

    private int mOffset;

    private int mExtent;

    private boolean mVertical;

    private boolean mBoundsChanged;

    private boolean mRangeChanged;

    private boolean mAlwaysDrawHorizontalTrack;

    private boolean mAlwaysDrawVerticalTrack;

    private boolean mMutated;

    private int mAlpha = 255;

    private boolean mHasSetAlpha;

    private android.graphics.ColorFilter mColorFilter;

    private boolean mHasSetColorFilter;

    /**
     * Indicate whether the horizontal scrollbar track should always be drawn
     * regardless of the extent. Defaults to false.
     *
     * @param alwaysDrawTrack
     * 		Whether the track should always be drawn
     * @see #getAlwaysDrawHorizontalTrack()
     */
    public void setAlwaysDrawHorizontalTrack(boolean alwaysDrawTrack) {
        mAlwaysDrawHorizontalTrack = alwaysDrawTrack;
    }

    /**
     * Indicate whether the vertical scrollbar track should always be drawn
     * regardless of the extent. Defaults to false.
     *
     * @param alwaysDrawTrack
     * 		Whether the track should always be drawn
     * @see #getAlwaysDrawVerticalTrack()
     */
    public void setAlwaysDrawVerticalTrack(boolean alwaysDrawTrack) {
        mAlwaysDrawVerticalTrack = alwaysDrawTrack;
    }

    /**
     *
     *
     * @return whether the vertical scrollbar track should always be drawn
    regardless of the extent.
     * @see #setAlwaysDrawVerticalTrack(boolean)
     */
    public boolean getAlwaysDrawVerticalTrack() {
        return mAlwaysDrawVerticalTrack;
    }

    /**
     *
     *
     * @return whether the horizontal scrollbar track should always be drawn
    regardless of the extent.
     * @see #setAlwaysDrawHorizontalTrack(boolean)
     */
    public boolean getAlwaysDrawHorizontalTrack() {
        return mAlwaysDrawHorizontalTrack;
    }

    public void setParameters(int range, int offset, int extent, boolean vertical) {
        if (mVertical != vertical) {
            mVertical = vertical;
            mBoundsChanged = true;
        }
        if (((mRange != range) || (mOffset != offset)) || (mExtent != extent)) {
            mRange = range;
            mOffset = offset;
            mExtent = extent;
            mRangeChanged = true;
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final boolean vertical = mVertical;
        final int extent = mExtent;
        final int range = mRange;
        boolean drawTrack = true;
        boolean drawThumb = true;
        if ((extent <= 0) || (range <= extent)) {
            drawTrack = (vertical) ? mAlwaysDrawVerticalTrack : mAlwaysDrawHorizontalTrack;
            drawThumb = false;
        }
        final android.graphics.Rect r = getBounds();
        if (canvas.quickReject(r.left, r.top, r.right, r.bottom, android.graphics.Canvas.EdgeType.AA)) {
            return;
        }
        if (drawTrack) {
            drawTrack(canvas, r, vertical);
        }
        if (drawThumb) {
            final int scrollBarLength = (vertical) ? r.height() : r.width();
            final int thickness = (vertical) ? r.width() : r.height();
            final int thumbLength = com.android.internal.widget.ScrollBarUtils.getThumbLength(scrollBarLength, thickness, extent, range);
            final int thumbOffset = com.android.internal.widget.ScrollBarUtils.getThumbOffset(scrollBarLength, thumbLength, extent, range, mOffset);
            drawThumb(canvas, r, thumbOffset, thumbLength, vertical);
        }
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        super.onBoundsChange(bounds);
        mBoundsChanged = true;
    }

    @java.lang.Override
    public boolean isStateful() {
        return (((((mVerticalTrack != null) && mVerticalTrack.isStateful()) || ((mVerticalThumb != null) && mVerticalThumb.isStateful())) || ((mHorizontalTrack != null) && mHorizontalTrack.isStateful())) || ((mHorizontalThumb != null) && mHorizontalThumb.isStateful())) || super.isStateful();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        boolean changed = super.onStateChange(state);
        if (mVerticalTrack != null) {
            changed |= mVerticalTrack.setState(state);
        }
        if (mVerticalThumb != null) {
            changed |= mVerticalThumb.setState(state);
        }
        if (mHorizontalTrack != null) {
            changed |= mHorizontalTrack.setState(state);
        }
        if (mHorizontalThumb != null) {
            changed |= mHorizontalThumb.setState(state);
        }
        return changed;
    }

    private void drawTrack(android.graphics.Canvas canvas, android.graphics.Rect bounds, boolean vertical) {
        final android.graphics.drawable.Drawable track;
        if (vertical) {
            track = mVerticalTrack;
        } else {
            track = mHorizontalTrack;
        }
        if (track != null) {
            if (mBoundsChanged) {
                track.setBounds(bounds);
            }
            track.draw(canvas);
        }
    }

    private void drawThumb(android.graphics.Canvas canvas, android.graphics.Rect bounds, int offset, int length, boolean vertical) {
        final boolean changed = mRangeChanged || mBoundsChanged;
        if (vertical) {
            if (mVerticalThumb != null) {
                final android.graphics.drawable.Drawable thumb = mVerticalThumb;
                if (changed) {
                    thumb.setBounds(bounds.left, bounds.top + offset, bounds.right, (bounds.top + offset) + length);
                }
                thumb.draw(canvas);
            }
        } else {
            if (mHorizontalThumb != null) {
                final android.graphics.drawable.Drawable thumb = mHorizontalThumb;
                if (changed) {
                    thumb.setBounds(bounds.left + offset, bounds.top, (bounds.left + offset) + length, bounds.bottom);
                }
                thumb.draw(canvas);
            }
        }
    }

    /**
     *
     *
     * @see android.view.View#setVerticalThumbDrawable(Drawable)
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    public void setVerticalThumbDrawable(android.graphics.drawable.Drawable thumb) {
        if (mVerticalThumb != null) {
            mVerticalThumb.setCallback(null);
        }
        propagateCurrentState(thumb);
        mVerticalThumb = thumb;
    }

    /**
     *
     *
     * @see View#getVerticalTrackDrawable()
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getVerticalTrackDrawable() {
        return mVerticalTrack;
    }

    /**
     *
     *
     * @see View#getVerticalThumbDrawable()
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getVerticalThumbDrawable() {
        return mVerticalThumb;
    }

    /**
     *
     *
     * @see View#getHorizontalTrackDrawable()
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getHorizontalTrackDrawable() {
        return mHorizontalTrack;
    }

    /**
     *
     *
     * @see View#getHorizontalThumbDrawable()
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getHorizontalThumbDrawable() {
        return mHorizontalThumb;
    }

    /**
     *
     *
     * @see android.view.View#setVerticalTrackDrawable(Drawable)
     */
    public void setVerticalTrackDrawable(android.graphics.drawable.Drawable track) {
        if (mVerticalTrack != null) {
            mVerticalTrack.setCallback(null);
        }
        propagateCurrentState(track);
        mVerticalTrack = track;
    }

    /**
     *
     *
     * @see android.view.View#setHorizontalThumbDrawable(Drawable)
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    public void setHorizontalThumbDrawable(android.graphics.drawable.Drawable thumb) {
        if (mHorizontalThumb != null) {
            mHorizontalThumb.setCallback(null);
        }
        propagateCurrentState(thumb);
        mHorizontalThumb = thumb;
    }

    public void setHorizontalTrackDrawable(android.graphics.drawable.Drawable track) {
        if (mHorizontalTrack != null) {
            mHorizontalTrack.setCallback(null);
        }
        propagateCurrentState(track);
        mHorizontalTrack = track;
    }

    private void propagateCurrentState(android.graphics.drawable.Drawable d) {
        if (d != null) {
            if (mMutated) {
                d.mutate();
            }
            d.setState(getState());
            d.setCallback(this);
            if (mHasSetAlpha) {
                d.setAlpha(mAlpha);
            }
            if (mHasSetColorFilter) {
                d.setColorFilter(mColorFilter);
            }
        }
    }

    public int getSize(boolean vertical) {
        if (vertical) {
            return mVerticalTrack != null ? mVerticalTrack.getIntrinsicWidth() : mVerticalThumb != null ? mVerticalThumb.getIntrinsicWidth() : 0;
        } else {
            return mHorizontalTrack != null ? mHorizontalTrack.getIntrinsicHeight() : mHorizontalThumb != null ? mHorizontalThumb.getIntrinsicHeight() : 0;
        }
    }

    @java.lang.Override
    public android.widget.ScrollBarDrawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            if (mVerticalTrack != null) {
                mVerticalTrack.mutate();
            }
            if (mVerticalThumb != null) {
                mVerticalThumb.mutate();
            }
            if (mHorizontalTrack != null) {
                mHorizontalTrack.mutate();
            }
            if (mHorizontalThumb != null) {
                mHorizontalThumb.mutate();
            }
            mMutated = true;
        }
        return this;
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        mHasSetAlpha = true;
        if (mVerticalTrack != null) {
            mVerticalTrack.setAlpha(alpha);
        }
        if (mVerticalThumb != null) {
            mVerticalThumb.setAlpha(alpha);
        }
        if (mHorizontalTrack != null) {
            mHorizontalTrack.setAlpha(alpha);
        }
        if (mHorizontalThumb != null) {
            mHorizontalThumb.setAlpha(alpha);
        }
    }

    @java.lang.Override
    public int getAlpha() {
        return mAlpha;
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mColorFilter = colorFilter;
        mHasSetColorFilter = true;
        if (mVerticalTrack != null) {
            mVerticalTrack.setColorFilter(colorFilter);
        }
        if (mVerticalThumb != null) {
            mVerticalThumb.setColorFilter(colorFilter);
        }
        if (mHorizontalTrack != null) {
            mHorizontalTrack.setColorFilter(colorFilter);
        }
        if (mHorizontalThumb != null) {
            mHorizontalThumb.setColorFilter(colorFilter);
        }
    }

    @java.lang.Override
    public android.graphics.ColorFilter getColorFilter() {
        return mColorFilter;
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        invalidateSelf();
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
    public java.lang.String toString() {
        return ((((("ScrollBarDrawable: range=" + mRange) + " offset=") + mOffset) + " extent=") + mExtent) + (mVertical ? " V" : " H");
    }
}

