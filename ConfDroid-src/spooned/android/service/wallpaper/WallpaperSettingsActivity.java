/**
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.service.wallpaper;


/**
 * Base class for activities that will be used to configure the settings of
 * a wallpaper.  You should derive from this class to allow it to select the
 * proper theme of the activity depending on how it is being used.
 *
 * @unknown 
 */
public class WallpaperSettingsActivity extends android.preference.PreferenceActivity {
    /**
     * This boolean extra in the launch intent indicates that the settings
     * are being used while the wallpaper is in preview mode.
     */
    public static final java.lang.String EXTRA_PREVIEW_MODE = "android.service.wallpaper.PREVIEW_MODE";

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        if (false) {
            android.content.res.Resources.Theme theme = getTheme();
            if (getIntent().getBooleanExtra(android.service.wallpaper.WallpaperSettingsActivity.EXTRA_PREVIEW_MODE, false)) {
                theme.applyStyle(com.android.internal.R.style.PreviewWallpaperSettings, true);
            } else {
                theme.applyStyle(com.android.internal.R.style.ActiveWallpaperSettings, true);
            }
        }
        super.onCreate(icicle);
    }
}

