/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v4.content.res;


class ConfigurationHelperGingerbread {
    static int getScreenHeightDp(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        final android.util.DisplayMetrics metrics = resources.getDisplayMetrics();
        return ((int) (metrics.heightPixels / metrics.density));
    }

    static int getScreenWidthDp(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        final android.util.DisplayMetrics metrics = resources.getDisplayMetrics();
        return ((int) (metrics.widthPixels / metrics.density));
    }

    static int getSmallestScreenWidthDp(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        // Not perfect, but close enough
        return java.lang.Math.min(android.support.v4.content.res.ConfigurationHelperGingerbread.getScreenWidthDp(resources), android.support.v4.content.res.ConfigurationHelperGingerbread.getScreenHeightDp(resources));
    }

    static int getDensityDpi(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        return resources.getDisplayMetrics().densityDpi;
    }
}

