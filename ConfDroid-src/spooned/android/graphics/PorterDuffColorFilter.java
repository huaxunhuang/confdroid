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
package android.graphics;


/**
 * A color filter that can be used to tint the source pixels using a single
 * color and a specific {@link PorterDuff Porter-Duff composite mode}.
 */
public class PorterDuffColorFilter extends android.graphics.ColorFilter {
    @android.annotation.ColorInt
    private int mColor;

    private android.graphics.PorterDuff.Mode mMode;

    /**
     * Create a color filter that uses the specified color and Porter-Duff mode.
     *
     * @param color
     * 		The ARGB source color used with the specified Porter-Duff mode
     * @param mode
     * 		The porter-duff mode that is applied
     * @see Color
     */
    public PorterDuffColorFilter(@android.annotation.ColorInt
    int color, @android.annotation.NonNull
    android.graphics.PorterDuff.Mode mode) {
        mColor = color;
        mMode = mode;
    }

    /**
     * Returns the ARGB color used to tint the source pixels when this filter
     * is applied.
     *
     * @see Color
     * @unknown 
     */
    @android.annotation.ColorInt
    @android.annotation.UnsupportedAppUsage
    public int getColor() {
        return mColor;
    }

    /**
     * Returns the Porter-Duff mode used to composite this color filter's
     * color with the source pixel when this filter is applied.
     *
     * @see PorterDuff
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.graphics.PorterDuff.Mode getMode() {
        return mMode;
    }

    @java.lang.Override
    long createNativeInstance() {
        return android.graphics.PorterDuffColorFilter.native_CreateBlendModeFilter(mColor, mMode.nativeInt);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        final android.graphics.PorterDuffColorFilter other = ((android.graphics.PorterDuffColorFilter) (object));
        return (mColor == other.mColor) && (mMode.nativeInt == other.mMode.nativeInt);
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * mMode.hashCode()) + mColor;
    }

    private static native long native_CreateBlendModeFilter(int srcColor, int blendmode);
}

