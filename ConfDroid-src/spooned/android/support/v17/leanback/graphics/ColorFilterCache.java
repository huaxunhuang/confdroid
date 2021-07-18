/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v17.leanback.graphics;


/**
 * Cache of {@link ColorFilter}s for a given color at different alpha levels.
 */
public final class ColorFilterCache {
    private static final android.util.SparseArray<android.support.v17.leanback.graphics.ColorFilterCache> sColorToFiltersMap = new android.util.SparseArray<android.support.v17.leanback.graphics.ColorFilterCache>();

    private final android.graphics.PorterDuffColorFilter[] mFilters = new android.graphics.PorterDuffColorFilter[0x100];

    /**
     * Get a ColorDimmer for a given color.  Only the RGB values are used; the
     * alpha channel is ignored in color. Subsequent calls to this method
     * with the same color value will return the same cache.
     *
     * @param color
     * 		The color to use for the color filters.
     * @return A cache of ColorFilters at different alpha levels for the color.
     */
    public static android.support.v17.leanback.graphics.ColorFilterCache getColorFilterCache(int color) {
        final int r = android.graphics.Color.red(color);
        final int g = android.graphics.Color.green(color);
        final int b = android.graphics.Color.blue(color);
        color = android.graphics.Color.rgb(r, g, b);
        android.support.v17.leanback.graphics.ColorFilterCache filters = android.support.v17.leanback.graphics.ColorFilterCache.sColorToFiltersMap.get(color);
        if (filters == null) {
            filters = new android.support.v17.leanback.graphics.ColorFilterCache(r, g, b);
            android.support.v17.leanback.graphics.ColorFilterCache.sColorToFiltersMap.put(color, filters);
        }
        return filters;
    }

    private ColorFilterCache(int r, int g, int b) {
        // Pre cache all 256 filter levels
        for (int i = 0x0; i <= 0xff; i++) {
            int color = android.graphics.Color.argb(i, r, g, b);
            mFilters[i] = new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * Returns a ColorFilter for a given alpha level between 0 and 1.0.
     *
     * @param level
     * 		The alpha level the filter should apply.
     * @return A ColorFilter at the alpha level for the color represented by the
    cache.
     */
    public android.graphics.ColorFilter getFilterForLevel(float level) {
        if ((level >= 0) && (level <= 1.0)) {
            int filterIndex = ((int) (0xff * level));
            return mFilters[filterIndex];
        } else {
            return null;
        }
    }
}

