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
package android.support.v4.graphics.drawable;


class DrawableWrapperLollipop extends android.support.v4.graphics.drawable.DrawableWrapperKitKat {
    DrawableWrapperLollipop(android.graphics.drawable.Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperLollipop(android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState state, android.content.res.Resources resources) {
        super(state, resources);
    }

    @java.lang.Override
    public void setHotspot(float x, float y) {
        mDrawable.setHotspot(x, y);
    }

    @java.lang.Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        mDrawable.setHotspotBounds(left, top, right, bottom);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        mDrawable.getOutline(outline);
    }

    @java.lang.Override
    public android.graphics.Rect getDirtyBounds() {
        return mDrawable.getDirtyBounds();
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        if (isCompatTintEnabled()) {
            super.setTintList(tint);
        } else {
            mDrawable.setTintList(tint);
        }
    }

    @java.lang.Override
    public void setTint(int tintColor) {
        if (isCompatTintEnabled()) {
            super.setTint(tintColor);
        } else {
            mDrawable.setTint(tintColor);
        }
    }

    @java.lang.Override
    public void setTintMode(android.graphics.PorterDuff.Mode tintMode) {
        if (isCompatTintEnabled()) {
            super.setTintMode(tintMode);
        } else {
            mDrawable.setTintMode(tintMode);
        }
    }

    @java.lang.Override
    public boolean setState(int[] stateSet) {
        if (super.setState(stateSet)) {
            // Manually invalidate because the framework doesn't currently force an invalidation
            // on a state change
            invalidateSelf();
            return true;
        }
        return false;
    }

    @java.lang.Override
    protected boolean isCompatTintEnabled() {
        if (android.os.Build.VERSION.SDK_INT == 21) {
            final android.graphics.drawable.Drawable drawable = mDrawable;
            return ((drawable instanceof android.graphics.drawable.GradientDrawable) || (drawable instanceof android.graphics.drawable.DrawableContainer)) || (drawable instanceof android.graphics.drawable.InsetDrawable);
        }
        return false;
    }

    @android.support.annotation.NonNull
    @java.lang.Override
    android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new android.support.v4.graphics.drawable.DrawableWrapperLollipop.DrawableWrapperStateLollipop(mState, null);
    }

    private static class DrawableWrapperStateLollipop extends android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateLollipop(@android.support.annotation.Nullable
        android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState orig, @android.support.annotation.Nullable
        android.content.res.Resources res) {
            super(orig, res);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(@android.support.annotation.Nullable
        android.content.res.Resources res) {
            return new android.support.v4.graphics.drawable.DrawableWrapperLollipop(this, res);
        }
    }
}

