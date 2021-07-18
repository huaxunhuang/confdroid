/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.graphics.drawable;


/**
 * Drawable which delegates all calls to it's wrapped {@link Drawable}.
 * <p>
 * The wrapped {@link Drawable} <em>must</em> be fully released from any {@link View}
 * before wrapping, otherwise internal {@link Drawable.Callback} may be dropped.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class DrawableWrapper extends android.graphics.drawable.Drawable implements android.graphics.drawable.Drawable.Callback {
    private android.graphics.drawable.Drawable mDrawable;

    public DrawableWrapper(android.graphics.drawable.Drawable drawable) {
        setWrappedDrawable(drawable);
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        mDrawable.draw(canvas);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        mDrawable.setBounds(bounds);
    }

    @java.lang.Override
    public void setChangingConfigurations(int configs) {
        mDrawable.setChangingConfigurations(configs);
    }

    @java.lang.Override
    public int getChangingConfigurations() {
        return mDrawable.getChangingConfigurations();
    }

    @java.lang.Override
    public void setDither(boolean dither) {
        mDrawable.setDither(dither);
    }

    @java.lang.Override
    public void setFilterBitmap(boolean filter) {
        mDrawable.setFilterBitmap(filter);
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        mDrawable.setColorFilter(cf);
    }

    @java.lang.Override
    public boolean isStateful() {
        return mDrawable.isStateful();
    }

    @java.lang.Override
    public boolean setState(final int[] stateSet) {
        return mDrawable.setState(stateSet);
    }

    @java.lang.Override
    public int[] getState() {
        return mDrawable.getState();
    }

    public void jumpToCurrentState() {
        android.support.v4.graphics.drawable.DrawableCompat.jumpToCurrentState(mDrawable);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getCurrent() {
        return mDrawable.getCurrent();
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || mDrawable.setVisible(visible, restart);
    }

    @java.lang.Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    @java.lang.Override
    public android.graphics.Region getTransparentRegion() {
        return mDrawable.getTransparentRegion();
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @java.lang.Override
    public int getMinimumWidth() {
        return mDrawable.getMinimumWidth();
    }

    @java.lang.Override
    public int getMinimumHeight() {
        return mDrawable.getMinimumHeight();
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        return mDrawable.getPadding(padding);
    }

    /**
     * {@inheritDoc }
     */
    public void invalidateDrawable(android.graphics.drawable.Drawable who) {
        invalidateSelf();
    }

    /**
     * {@inheritDoc }
     */
    public void scheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what, long when) {
        scheduleSelf(what, when);
    }

    /**
     * {@inheritDoc }
     */
    public void unscheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what) {
        unscheduleSelf(what);
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        return mDrawable.setLevel(level);
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        android.support.v4.graphics.drawable.DrawableCompat.setAutoMirrored(mDrawable, mirrored);
    }

    @java.lang.Override
    public boolean isAutoMirrored() {
        return android.support.v4.graphics.drawable.DrawableCompat.isAutoMirrored(mDrawable);
    }

    @java.lang.Override
    public void setTint(int tint) {
        android.support.v4.graphics.drawable.DrawableCompat.setTint(mDrawable, tint);
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        android.support.v4.graphics.drawable.DrawableCompat.setTintList(mDrawable, tint);
    }

    @java.lang.Override
    public void setTintMode(android.graphics.PorterDuff.Mode tintMode) {
        android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mDrawable, tintMode);
    }

    @java.lang.Override
    public void setHotspot(float x, float y) {
        android.support.v4.graphics.drawable.DrawableCompat.setHotspot(mDrawable, x, y);
    }

    @java.lang.Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        android.support.v4.graphics.drawable.DrawableCompat.setHotspotBounds(mDrawable, left, top, right, bottom);
    }

    public android.graphics.drawable.Drawable getWrappedDrawable() {
        return mDrawable;
    }

    public void setWrappedDrawable(android.graphics.drawable.Drawable drawable) {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }
        mDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }
}

