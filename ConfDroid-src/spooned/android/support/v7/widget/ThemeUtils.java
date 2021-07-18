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
package android.support.v7.widget;


class ThemeUtils {
    private static final java.lang.ThreadLocal<android.util.TypedValue> TL_TYPED_VALUE = new java.lang.ThreadLocal<>();

    static final int[] DISABLED_STATE_SET = new int[]{ -android.R.attr.state_enabled };

    static final int[] FOCUSED_STATE_SET = new int[]{ android.R.attr.state_focused };

    static final int[] ACTIVATED_STATE_SET = new int[]{ android.R.attr.state_activated };

    static final int[] PRESSED_STATE_SET = new int[]{ android.R.attr.state_pressed };

    static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    static final int[] SELECTED_STATE_SET = new int[]{ android.R.attr.state_selected };

    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[]{ -android.R.attr.state_pressed, -android.R.attr.state_focused };

    static final int[] EMPTY_STATE_SET = new int[0];

    private static final int[] TEMP_ARRAY = new int[1];

    public static android.content.res.ColorStateList createDisabledStateList(int textColor, int disabledTextColor) {
        // Now create a new ColorStateList with the default color, and the new disabled
        // color
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;
        // Disabled state
        states[i] = android.support.v7.widget.ThemeUtils.DISABLED_STATE_SET;
        colors[i] = disabledTextColor;
        i++;
        // Default state
        states[i] = android.support.v7.widget.ThemeUtils.EMPTY_STATE_SET;
        colors[i] = textColor;
        i++;
        return new android.content.res.ColorStateList(states, colors);
    }

    public static int getThemeAttrColor(android.content.Context context, int attr) {
        android.support.v7.widget.ThemeUtils.TEMP_ARRAY[0] = attr;
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, null, android.support.v7.widget.ThemeUtils.TEMP_ARRAY);
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    public static android.content.res.ColorStateList getThemeAttrColorStateList(android.content.Context context, int attr) {
        android.support.v7.widget.ThemeUtils.TEMP_ARRAY[0] = attr;
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, null, android.support.v7.widget.ThemeUtils.TEMP_ARRAY);
        try {
            return a.getColorStateList(0);
        } finally {
            a.recycle();
        }
    }

    public static int getDisabledThemeAttrColor(android.content.Context context, int attr) {
        final android.content.res.ColorStateList csl = android.support.v7.widget.ThemeUtils.getThemeAttrColorStateList(context, attr);
        if ((csl != null) && csl.isStateful()) {
            // If the CSL is stateful, we'll assume it has a disabled state and use it
            return csl.getColorForState(android.support.v7.widget.ThemeUtils.DISABLED_STATE_SET, csl.getDefaultColor());
        } else {
            // Else, we'll generate the color using disabledAlpha from the theme
            final android.util.TypedValue tv = android.support.v7.widget.ThemeUtils.getTypedValue();
            // Now retrieve the disabledAlpha value from the theme
            context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, tv, true);
            final float disabledAlpha = tv.getFloat();
            return android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, attr, disabledAlpha);
        }
    }

    private static android.util.TypedValue getTypedValue() {
        android.util.TypedValue typedValue = android.support.v7.widget.ThemeUtils.TL_TYPED_VALUE.get();
        if (typedValue == null) {
            typedValue = new android.util.TypedValue();
            android.support.v7.widget.ThemeUtils.TL_TYPED_VALUE.set(typedValue);
        }
        return typedValue;
    }

    static int getThemeAttrColor(android.content.Context context, int attr, float alpha) {
        final int color = android.support.v7.widget.ThemeUtils.getThemeAttrColor(context, attr);
        final int originalAlpha = android.graphics.Color.alpha(color);
        return android.support.v4.graphics.ColorUtils.setAlphaComponent(color, java.lang.Math.round(originalAlpha * alpha));
    }
}

