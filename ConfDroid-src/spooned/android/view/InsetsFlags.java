/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view;


/**
 * Contains the information about {@link Appearance} and {@link Behavior} of system windows which
 * can produce insets. This is for carrying the request from a client to the system server.
 *
 * @unknown 
 */
public class InsetsFlags {
    @android.view.ViewDebug.ExportedProperty(flagMapping = { @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.APPEARANCE_OPAQUE_STATUS_BARS, equals = android.view.WindowInsetsController.APPEARANCE_OPAQUE_STATUS_BARS, name = "OPAQUE_STATUS_BARS"), @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.APPEARANCE_OPAQUE_NAVIGATION_BARS, equals = android.view.WindowInsetsController.APPEARANCE_OPAQUE_NAVIGATION_BARS, name = "OPAQUE_NAVIGATION_BARS"), @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.APPEARANCE_LOW_PROFILE_BARS, equals = android.view.WindowInsetsController.APPEARANCE_LOW_PROFILE_BARS, name = "LOW_PROFILE_BARS"), @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, equals = android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, name = "LIGHT_STATUS_BARS"), @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, equals = android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, name = "LIGHT_NAVIGATION_BARS") })
    @android.view.WindowInsetsController.Appearance
    public int appearance;

    @android.view.ViewDebug.ExportedProperty(flagMapping = { @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE, equals = android.view.WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE, name = "SHOW_BARS_BY_SWIPE"), @android.view.ViewDebug.FlagToString(mask = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE, equals = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE, name = "SHOW_TRANSIENT_BARS_BY_SWIPE") })
    @android.view.WindowInsetsController.Behavior
    public int behavior;

    /**
     * Converts system UI visibility to appearance.
     *
     * @param systemUiVisibility
     * 		the system UI visibility to be converted.
     * @return the outcome {@link Appearance}
     */
    @android.view.WindowInsetsController.Appearance
    public static int getAppearance(int systemUiVisibility) {
        int appearance = 0;
        appearance |= android.view.InsetsFlags.convertFlag(systemUiVisibility, android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE, android.view.WindowInsetsController.APPEARANCE_LOW_PROFILE_BARS);
        appearance |= android.view.InsetsFlags.convertFlag(systemUiVisibility, android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        appearance |= android.view.InsetsFlags.convertFlag(systemUiVisibility, android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR, android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        appearance |= android.view.InsetsFlags.convertNoFlag(systemUiVisibility, android.view.View.STATUS_BAR_TRANSLUCENT | android.view.View.STATUS_BAR_TRANSPARENT, android.view.WindowInsetsController.APPEARANCE_OPAQUE_STATUS_BARS);
        appearance |= android.view.InsetsFlags.convertNoFlag(systemUiVisibility, android.view.View.NAVIGATION_BAR_TRANSLUCENT | android.view.View.NAVIGATION_BAR_TRANSPARENT, android.view.WindowInsetsController.APPEARANCE_OPAQUE_NAVIGATION_BARS);
        return appearance;
    }

    /**
     * Converts the system UI visibility into an appearance flag if the given visibility contains
     * the given system UI flag.
     */
    @android.view.WindowInsetsController.Appearance
    private static int convertFlag(int systemUiVisibility, int systemUiFlag, @android.view.WindowInsetsController.Appearance
    int appearance) {
        return (systemUiVisibility & systemUiFlag) != 0 ? appearance : 0;
    }

    /**
     * Converts the system UI visibility into an appearance flag if the given visibility doesn't
     * contains the given system UI flag.
     */
    @android.view.WindowInsetsController.Appearance
    private static int convertNoFlag(int systemUiVisibility, int systemUiFlag, @android.view.WindowInsetsController.Appearance
    int appearance) {
        return (systemUiVisibility & systemUiFlag) == 0 ? appearance : 0;
    }
}

