/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.app;


final class MediaRouterThemeHelper {
    private static final float MIN_CONTRAST = 3.0F;

    @android.support.annotation.IntDef({ android.support.v7.app.MediaRouterThemeHelper.COLOR_DARK_ON_LIGHT_BACKGROUND, android.support.v7.app.MediaRouterThemeHelper.COLOR_WHITE_ON_DARK_BACKGROUND })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ControllerColorType {}

    static final int COLOR_DARK_ON_LIGHT_BACKGROUND = 0xde000000;/* Opacity of 87% */


    static final int COLOR_WHITE_ON_DARK_BACKGROUND = android.graphics.Color.WHITE;

    private MediaRouterThemeHelper() {
    }

    /**
     * Creates a themed context based on the explicit style resource or the parent context's default
     * theme.
     * <p>
     * The theme which will be applied on top of the parent {@code context}'s theme is determined
     * by the primary color defined in the given {@code style}, or in the parent {@code context}.
     *
     * @param context
     * 		the parent context
     * @param style
     * 		the resource ID of the style against which to inflate this context, or
     * 		{@code 0} to use the parent {@code context}'s default theme.
     * @return The themed context.
     */
    public static android.content.Context createThemedContext(android.content.Context context, int style) {
        int theme;
        if (android.support.v7.app.MediaRouterThemeHelper.isLightTheme(context)) {
            if (android.support.v7.app.MediaRouterThemeHelper.getControllerColor(context, style) == android.support.v7.app.MediaRouterThemeHelper.COLOR_DARK_ON_LIGHT_BACKGROUND) {
                theme = R.style.Theme_MediaRouter_Light;
            } else {
                theme = R.style.Theme_MediaRouter_Light_DarkControlPanel;
            }
        } else {
            if (android.support.v7.app.MediaRouterThemeHelper.getControllerColor(context, style) == android.support.v7.app.MediaRouterThemeHelper.COLOR_DARK_ON_LIGHT_BACKGROUND) {
                theme = R.style.Theme_MediaRouter_LightControlPanel;
            } else {
                theme = R.style.Theme_MediaRouter;
            }
        }
        return new android.view.ContextThemeWrapper(context, theme);
    }

    public static int getThemeResource(android.content.Context context, int attr) {
        android.util.TypedValue value = new android.util.TypedValue();
        return context.getTheme().resolveAttribute(attr, value, true) ? value.resourceId : 0;
    }

    public static float getDisabledAlpha(android.content.Context context) {
        android.util.TypedValue value = new android.util.TypedValue();
        return context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, value, true) ? value.getFloat() : 0.5F;
    }

    @android.support.v7.app.MediaRouterThemeHelper.ControllerColorType
    public static int getControllerColor(android.content.Context context, int style) {
        int primaryColor = android.support.v7.app.MediaRouterThemeHelper.getThemeColor(context, style, android.support.v7.appcompat.R.attr.colorPrimary);
        if (android.support.v4.graphics.ColorUtils.calculateContrast(android.support.v7.app.MediaRouterThemeHelper.COLOR_WHITE_ON_DARK_BACKGROUND, primaryColor) >= android.support.v7.app.MediaRouterThemeHelper.MIN_CONTRAST) {
            return android.support.v7.app.MediaRouterThemeHelper.COLOR_WHITE_ON_DARK_BACKGROUND;
        }
        return android.support.v7.app.MediaRouterThemeHelper.COLOR_DARK_ON_LIGHT_BACKGROUND;
    }

    public static int getButtonTextColor(android.content.Context context) {
        int primaryColor = android.support.v7.app.MediaRouterThemeHelper.getThemeColor(context, 0, android.support.v7.appcompat.R.attr.colorPrimary);
        int backgroundColor = android.support.v7.app.MediaRouterThemeHelper.getThemeColor(context, 0, android.R.attr.colorBackground);
        if (android.support.v4.graphics.ColorUtils.calculateContrast(primaryColor, backgroundColor) < android.support.v7.app.MediaRouterThemeHelper.MIN_CONTRAST) {
            // Default to colorAccent if the contrast ratio is low.
            return android.support.v7.app.MediaRouterThemeHelper.getThemeColor(context, 0, android.support.v7.appcompat.R.attr.colorAccent);
        }
        return primaryColor;
    }

    public static void setMediaControlsBackgroundColor(android.content.Context context, android.view.View mainControls, android.view.View groupControls, boolean hasGroup) {
        int primaryColor = android.support.v7.app.MediaRouterThemeHelper.getThemeColor(context, 0, android.support.v7.appcompat.R.attr.colorPrimary);
        int primaryDarkColor = android.support.v7.app.MediaRouterThemeHelper.getThemeColor(context, 0, android.support.v7.appcompat.R.attr.colorPrimaryDark);
        if (hasGroup && (android.support.v7.app.MediaRouterThemeHelper.getControllerColor(context, 0) == android.support.v7.app.MediaRouterThemeHelper.COLOR_DARK_ON_LIGHT_BACKGROUND)) {
            // Instead of showing dark controls in a possibly dark (i.e. the primary dark), model
            // the white dialog and use the primary color for the group controls.
            primaryDarkColor = primaryColor;
            primaryColor = android.graphics.Color.WHITE;
        }
        mainControls.setBackgroundColor(primaryColor);
        groupControls.setBackgroundColor(primaryDarkColor);
        // Also store the background colors to the view tags. They are used in
        // setVolumeSliderColor() below.
        mainControls.setTag(primaryColor);
        groupControls.setTag(primaryDarkColor);
    }

    public static void setVolumeSliderColor(android.content.Context context, android.support.v7.app.MediaRouteVolumeSlider volumeSlider, android.view.View backgroundView) {
        int controllerColor = android.support.v7.app.MediaRouterThemeHelper.getControllerColor(context, 0);
        if (android.graphics.Color.alpha(controllerColor) != 0xff) {
            // Composite with the background in order not to show the underlying progress bar
            // through the thumb.
            int backgroundColor = ((int) (backgroundView.getTag()));
            controllerColor = android.support.v4.graphics.ColorUtils.compositeColors(controllerColor, backgroundColor);
        }
        volumeSlider.setColor(controllerColor);
    }

    // This is copied from {@link AlertDialog#resolveDialogTheme} to pre-evaluate theme in advance.
    public static int getAlertDialogResolvedTheme(android.content.Context context, int themeResId) {
        if (themeResId >= 0x1000000) {
            // start of real resource IDs.
            return themeResId;
        } else {
            android.util.TypedValue outValue = new android.util.TypedValue();
            context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    private static boolean isLightTheme(android.content.Context context) {
        android.util.TypedValue value = new android.util.TypedValue();
        return context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.isLightTheme, value, true) && (value.data != 0);
    }

    private static int getThemeColor(android.content.Context context, int style, int attr) {
        if (style != 0) {
            int[] attrs = new int[]{ attr };
            android.content.res.TypedArray ta = context.obtainStyledAttributes(style, attrs);
            int color = ta.getColor(0, 0);
            ta.recycle();
            if (color != 0) {
                return color;
            }
        }
        android.util.TypedValue value = new android.util.TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        if (value.resourceId != 0) {
            return context.getResources().getColor(value.resourceId);
        }
        return value.data;
    }
}

