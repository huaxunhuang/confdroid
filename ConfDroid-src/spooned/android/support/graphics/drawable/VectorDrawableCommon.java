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
 * Internal common delegation shared by VectorDrawableCompat and AnimatedVectorDrawableCompat
 */
@android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
abstract class VectorDrawableCommon extends android.graphics.drawable.Drawable implements android.support.v4.graphics.drawable.TintAwareDrawable {
    /**
     * Obtains styled attributes from the theme, if available, or unstyled
     * resources if the theme is null.
     */
    static android.content.res.TypedArray obtainAttributes(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    // Drawable delegation for Lollipop and above.
    android.graphics.drawable.Drawable mDelegateDrawable;

    @java.lang.Override
    public void setColorFilter(int color, android.graphics.PorterDuff.Mode mode) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setColorFilter(color, mode);
            return;
        }
        super.setColorFilter(color, mode);
    }

    @java.lang.Override
    public android.graphics.ColorFilter getColorFilter() {
        if (mDelegateDrawable != null) {
            return android.support.v4.graphics.drawable.DrawableCompat.getColorFilter(mDelegateDrawable);
        }
        return null;
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setLevel(level);
        }
        return super.onLevelChange(level);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setBounds(bounds);
            return;
        }
        super.onBoundsChange(bounds);
    }

    @java.lang.Override
    public void setHotspot(float x, float y) {
        // API >= 21 only.
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setHotspot(mDelegateDrawable, x, y);
        }
        return;
    }

    @java.lang.Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setHotspotBounds(mDelegateDrawable, left, top, right, bottom);
            return;
        }
    }

    @java.lang.Override
    public void setFilterBitmap(boolean filter) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setFilterBitmap(filter);
            return;
        }
    }

    @java.lang.Override
    public void jumpToCurrentState() {
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.jumpToCurrentState(mDelegateDrawable);
            return;
        }
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme t) {
        // API >= 21 only.
        if (mDelegateDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.applyTheme(mDelegateDrawable, t);
            return;
        }
    }

    @java.lang.Override
    public void clearColorFilter() {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.clearColorFilter();
            return;
        }
        super.clearColorFilter();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getCurrent() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getCurrent();
        }
        return super.getCurrent();
    }

    @java.lang.Override
    public int getMinimumWidth() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getMinimumWidth();
        }
        return super.getMinimumWidth();
    }

    @java.lang.Override
    public int getMinimumHeight() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getMinimumHeight();
        }
        return super.getMinimumHeight();
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getPadding(padding);
        }
        return super.getPadding(padding);
    }

    @java.lang.Override
    public int[] getState() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getState();
        }
        return super.getState();
    }

    @java.lang.Override
    public android.graphics.Region getTransparentRegion() {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.getTransparentRegion();
        }
        return super.getTransparentRegion();
    }

    @java.lang.Override
    public void setChangingConfigurations(int configs) {
        if (mDelegateDrawable != null) {
            mDelegateDrawable.setChangingConfigurations(configs);
            return;
        }
        super.setChangingConfigurations(configs);
    }

    @java.lang.Override
    public boolean setState(int[] stateSet) {
        if (mDelegateDrawable != null) {
            return mDelegateDrawable.setState(stateSet);
        }
        return super.setState(stateSet);
    }
}

