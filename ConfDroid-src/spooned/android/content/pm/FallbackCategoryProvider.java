/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.content.pm;


/**
 * Class that provides fallback values for {@link ApplicationInfo#category}.
 *
 * @unknown 
 */
public class FallbackCategoryProvider {
    private static final java.lang.String TAG = "FallbackCategoryProvider";

    private static final android.util.ArrayMap<java.lang.String, java.lang.Integer> sFallbacks = new android.util.ArrayMap();

    public static void loadFallbacks() {
        android.content.pm.FallbackCategoryProvider.sFallbacks.clear();
        if (android.os.SystemProperties.getBoolean("fw.ignore_fb_categories", false)) {
            android.util.Log.d(android.content.pm.FallbackCategoryProvider.TAG, "Ignoring fallback categories");
            return;
        }
        final android.content.res.AssetManager assets = new android.content.res.AssetManager();
        assets.addAssetPath("/system/framework/framework-res.apk");
        final android.content.res.Resources res = new android.content.res.Resources(assets, null, null);
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(res.openRawResource(com.android.internal.R.raw.fallback_categories)))) {
            java.lang.String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == '#')
                    continue;

                final java.lang.String[] split = line.split(",");
                if (split.length == 2) {
                    android.content.pm.FallbackCategoryProvider.sFallbacks.put(split[0], java.lang.Integer.parseInt(split[1]));
                }
            } 
            android.util.Log.d(android.content.pm.FallbackCategoryProvider.TAG, ("Found " + android.content.pm.FallbackCategoryProvider.sFallbacks.size()) + " fallback categories");
        } catch (java.io.IOException | java.lang.NumberFormatException e) {
            android.util.Log.w(android.content.pm.FallbackCategoryProvider.TAG, "Failed to read fallback categories", e);
        }
    }

    public static int getFallbackCategory(java.lang.String packageName) {
        return android.content.pm.FallbackCategoryProvider.sFallbacks.getOrDefault(packageName, android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED);
    }
}

