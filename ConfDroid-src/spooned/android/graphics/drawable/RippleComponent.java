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
package android.graphics.drawable;


/**
 * Abstract class that handles size & positioning common to the ripple & focus states.
 */
abstract class RippleComponent {
    protected final android.graphics.drawable.RippleDrawable mOwner;

    /**
     * Bounds used for computing max radius. May be modified by the owner.
     */
    protected final android.graphics.Rect mBounds;

    /**
     * Whether we have an explicit maximum radius.
     */
    private boolean mHasMaxRadius;

    /**
     * How big this ripple should be when fully entered.
     */
    protected float mTargetRadius;

    /**
     * Screen density used to adjust pixel-based constants.
     */
    protected float mDensityScale;

    public RippleComponent(android.graphics.drawable.RippleDrawable owner, android.graphics.Rect bounds) {
        mOwner = owner;
        mBounds = bounds;
    }

    public void onBoundsChange() {
        if (!mHasMaxRadius) {
            mTargetRadius = android.graphics.drawable.RippleComponent.getTargetRadius(mBounds);
            onTargetRadiusChanged(mTargetRadius);
        }
    }

    public final void setup(float maxRadius, int densityDpi) {
        if (maxRadius >= 0) {
            mHasMaxRadius = true;
            mTargetRadius = maxRadius;
        } else {
            mTargetRadius = android.graphics.drawable.RippleComponent.getTargetRadius(mBounds);
        }
        mDensityScale = densityDpi * android.util.DisplayMetrics.DENSITY_DEFAULT_SCALE;
        onTargetRadiusChanged(mTargetRadius);
    }

    private static float getTargetRadius(android.graphics.Rect bounds) {
        final float halfWidth = bounds.width() / 2.0F;
        final float halfHeight = bounds.height() / 2.0F;
        return ((float) (java.lang.Math.sqrt((halfWidth * halfWidth) + (halfHeight * halfHeight))));
    }

    /**
     * Populates {@code bounds} with the maximum drawing bounds of the ripple
     * relative to its center. The resulting bounds should be translated into
     * parent drawable coordinates before use.
     *
     * @param bounds
     * 		the rect to populate with drawing bounds
     */
    public void getBounds(android.graphics.Rect bounds) {
        final int r = ((int) (java.lang.Math.ceil(mTargetRadius)));
        bounds.set(-r, -r, r, r);
    }

    protected final void invalidateSelf() {
        mOwner.invalidateSelf(false);
    }

    protected final void onHotspotBoundsChanged() {
        if (!mHasMaxRadius) {
            mTargetRadius = android.graphics.drawable.RippleComponent.getTargetRadius(mBounds);
            onTargetRadiusChanged(mTargetRadius);
        }
    }

    /**
     * Called when the target radius changes.
     *
     * @param targetRadius
     * 		the new target radius
     */
    protected void onTargetRadiusChanged(float targetRadius) {
        // Stub.
    }
}

