/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.content.res;


/**
 * This class describes all device configuration information that can
 * impact the resources the application retrieves.  This includes both
 * user-specified configuration options (locale list and scaling) as well
 * as device configurations (such as input modes, screen size and screen orientation).
 * <p>You can acquire this object from {@link Resources}, using {@link Resources#getConfiguration}. Thus, from an activity, you can get it by chaining the request
 * with {@link android.app.Activity#getResources}:</p>
 * <pre>Configuration config = getResources().getConfiguration();</pre>
 */
public final class Configuration implements android.os.Parcelable , java.lang.Comparable<android.content.res.Configuration> {
    /**
     *
     *
     * @unknown 
     */
    public static final android.content.res.Configuration EMPTY = new android.content.res.Configuration();

    private static final java.lang.String TAG = "Configuration";

    /**
     * Current user preference for the scaling factor for fonts, relative
     * to the base density scaling.
     */
    public float fontScale;

    /**
     * IMSI MCC (Mobile Country Code), corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#MccQualifier">mcc</a>
     * resource qualifier.  0 if undefined.
     */
    public int mcc;

    /**
     * IMSI MNC (Mobile Network Code), corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#MccQualifier">mnc</a>
     * resource qualifier.  0 if undefined. Note that the actual MNC may be 0; in order to check
     * for this use the {@link #MNC_ZERO} symbol.
     */
    public int mnc;

    /**
     * Constant used to to represent MNC (Mobile Network Code) zero.
     * 0 cannot be used, since it is used to represent an undefined MNC.
     */
    public static final int MNC_ZERO = 0xffff;

    /**
     * Current user preference for the locale, corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#LocaleQualifier">locale</a>
     * resource qualifier.
     *
     * @deprecated Do not set or read this directly. Use {@link #getLocales()} and
    {@link #setLocales(LocaleList)}. If only the primary locale is needed,
    <code>getLocales().get(0)</code> is now the preferred accessor.
     */
    @java.lang.Deprecated
    public java.util.Locale locale;

    private android.os.LocaleList mLocaleList;

    /**
     * Locale should persist on setting.  This is hidden because it is really
     * questionable whether this is the right way to expose the functionality.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean userSetLocale;

    /**
     * Constant for {@link #colorMode}: bits that encode whether the screen is wide gamut.
     */
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_MASK = 0x3;

    /**
     * Constant for {@link #colorMode}: a {@link #COLOR_MODE_WIDE_COLOR_GAMUT_MASK} value
     * indicating that it is unknown whether or not the screen is wide gamut.
     */
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED = 0x0;

    /**
     * Constant for {@link #colorMode}: a {@link #COLOR_MODE_WIDE_COLOR_GAMUT_MASK} value
     * indicating that the screen is not wide gamut.
     * <p>Corresponds to the <code>-nowidecg</code> resource qualifier.</p>
     */
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_NO = 0x1;

    /**
     * Constant for {@link #colorMode}: a {@link #COLOR_MODE_WIDE_COLOR_GAMUT_MASK} value
     * indicating that the screen is wide gamut.
     * <p>Corresponds to the <code>-widecg</code> resource qualifier.</p>
     */
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_YES = 0x2;

    /**
     * Constant for {@link #colorMode}: bits that encode the dynamic range of the screen.
     */
    public static final int COLOR_MODE_HDR_MASK = 0xc;

    /**
     * Constant for {@link #colorMode}: bits shift to get the screen dynamic range.
     */
    public static final int COLOR_MODE_HDR_SHIFT = 2;

    /**
     * Constant for {@link #colorMode}: a {@link #COLOR_MODE_HDR_MASK} value
     * indicating that it is unknown whether or not the screen is HDR.
     */
    public static final int COLOR_MODE_HDR_UNDEFINED = 0x0;

    /**
     * Constant for {@link #colorMode}: a {@link #COLOR_MODE_HDR_MASK} value
     * indicating that the screen is not HDR (low/standard dynamic range).
     * <p>Corresponds to the <code>-lowdr</code> resource qualifier.</p>
     */
    public static final int COLOR_MODE_HDR_NO = 0x1 << android.content.res.Configuration.COLOR_MODE_HDR_SHIFT;

    /**
     * Constant for {@link #colorMode}: a {@link #COLOR_MODE_HDR_MASK} value
     * indicating that the screen is HDR (dynamic range).
     * <p>Corresponds to the <code>-highdr</code> resource qualifier.</p>
     */
    public static final int COLOR_MODE_HDR_YES = 0x2 << android.content.res.Configuration.COLOR_MODE_HDR_SHIFT;

    /**
     * Constant for {@link #colorMode}: a value indicating that the color mode is undefined
     */
    @java.lang.SuppressWarnings("PointlessBitwiseExpression")
    public static final int COLOR_MODE_UNDEFINED = android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED | android.content.res.Configuration.COLOR_MODE_HDR_UNDEFINED;

    /**
     * Bit mask of color capabilities of the screen. Currently there are two fields:
     * <p>The {@link #COLOR_MODE_WIDE_COLOR_GAMUT_MASK} bits define the color gamut of
     * the screen. They may be one of
     * {@link #COLOR_MODE_WIDE_COLOR_GAMUT_NO} or {@link #COLOR_MODE_WIDE_COLOR_GAMUT_YES}.</p>
     *
     * <p>The {@link #COLOR_MODE_HDR_MASK} defines the dynamic range of the screen. They may be
     * one of {@link #COLOR_MODE_HDR_NO} or {@link #COLOR_MODE_HDR_YES}.</p>
     *
     * <p>See <a href="{@docRoot }guide/practices/screens_support.html">Supporting
     * Multiple Screens</a> for more information.</p>
     */
    public int colorMode;

    /**
     * Constant for {@link #screenLayout}: bits that encode the size.
     */
    public static final int SCREENLAYOUT_SIZE_MASK = 0xf;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_SIZE_MASK}
     * value indicating that no size has been set.
     */
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0x0;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_SIZE_MASK}
     * value indicating the screen is at least approximately 320x426 dp units,
     * corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenSizeQualifier">small</a>
     * resource qualifier.
     * See <a href="{@docRoot }guide/practices/screens_support.html">Supporting
     * Multiple Screens</a> for more information.
     */
    public static final int SCREENLAYOUT_SIZE_SMALL = 0x1;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_SIZE_MASK}
     * value indicating the screen is at least approximately 320x470 dp units,
     * corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenSizeQualifier">normal</a>
     * resource qualifier.
     * See <a href="{@docRoot }guide/practices/screens_support.html">Supporting
     * Multiple Screens</a> for more information.
     */
    public static final int SCREENLAYOUT_SIZE_NORMAL = 0x2;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_SIZE_MASK}
     * value indicating the screen is at least approximately 480x640 dp units,
     * corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenSizeQualifier">large</a>
     * resource qualifier.
     * See <a href="{@docRoot }guide/practices/screens_support.html">Supporting
     * Multiple Screens</a> for more information.
     */
    public static final int SCREENLAYOUT_SIZE_LARGE = 0x3;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_SIZE_MASK}
     * value indicating the screen is at least approximately 720x960 dp units,
     * corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenSizeQualifier">xlarge</a>
     * resource qualifier.
     * See <a href="{@docRoot }guide/practices/screens_support.html">Supporting
     * Multiple Screens</a> for more information.
     */
    public static final int SCREENLAYOUT_SIZE_XLARGE = 0x4;

    /**
     * Constant for {@link #screenLayout}: bits that encode the aspect ratio.
     */
    public static final int SCREENLAYOUT_LONG_MASK = 0x30;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_LONG_MASK}
     * value indicating that no size has been set.
     */
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0x0;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_LONG_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenAspectQualifier">notlong</a>
     * resource qualifier.
     */
    public static final int SCREENLAYOUT_LONG_NO = 0x10;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_LONG_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenAspectQualifier">long</a>
     * resource qualifier.
     */
    public static final int SCREENLAYOUT_LONG_YES = 0x20;

    /**
     * Constant for {@link #screenLayout}: bits that encode the layout direction.
     */
    public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 0xc0;

    /**
     * Constant for {@link #screenLayout}: bits shift to get the layout direction.
     */
    public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_LAYOUTDIR_MASK}
     * value indicating that no layout dir has been set.
     */
    public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0x0;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_LAYOUTDIR_MASK}
     * value indicating that a layout dir has been set to LTR.
     */
    public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 0x1 << android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_SHIFT;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_LAYOUTDIR_MASK}
     * value indicating that a layout dir has been set to RTL.
     */
    public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 0x2 << android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_SHIFT;

    /**
     * Constant for {@link #screenLayout}: bits that encode roundness of the screen.
     */
    public static final int SCREENLAYOUT_ROUND_MASK = 0x300;

    /**
     *
     *
     * @unknown Constant for {@link #screenLayout}: bit shift to get to screen roundness bits
     */
    public static final int SCREENLAYOUT_ROUND_SHIFT = 8;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_ROUND_MASK} value indicating
     * that it is unknown whether or not the screen has a round shape.
     */
    public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0x0;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_ROUND_MASK} value indicating
     * that the screen does not have a rounded shape.
     */
    public static final int SCREENLAYOUT_ROUND_NO = 0x1 << android.content.res.Configuration.SCREENLAYOUT_ROUND_SHIFT;

    /**
     * Constant for {@link #screenLayout}: a {@link #SCREENLAYOUT_ROUND_MASK} value indicating
     * that the screen has a rounded shape. Corners may not be visible to the user;
     * developers should pay special attention to the {@link android.view.WindowInsets} delivered
     * to views for more information about ensuring content is not obscured.
     *
     * <p>Corresponds to the <code>-round</code> resource qualifier.</p>
     */
    public static final int SCREENLAYOUT_ROUND_YES = 0x2 << android.content.res.Configuration.SCREENLAYOUT_ROUND_SHIFT;

    /**
     * Constant for {@link #screenLayout}: a value indicating that screenLayout is undefined
     */
    public static final int SCREENLAYOUT_UNDEFINED = ((android.content.res.Configuration.SCREENLAYOUT_SIZE_UNDEFINED | android.content.res.Configuration.SCREENLAYOUT_LONG_UNDEFINED) | android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_UNDEFINED) | android.content.res.Configuration.SCREENLAYOUT_ROUND_UNDEFINED;

    /**
     * Special flag we generate to indicate that the screen layout requires
     * us to use a compatibility mode for apps that are not modern layout
     * aware.
     *
     * @unknown 
     */
    public static final int SCREENLAYOUT_COMPAT_NEEDED = 0x10000000;

    /**
     * Bit mask of overall layout of the screen.  Currently there are four
     * fields:
     * <p>The {@link #SCREENLAYOUT_SIZE_MASK} bits define the overall size
     * of the screen.  They may be one of
     * {@link #SCREENLAYOUT_SIZE_SMALL}, {@link #SCREENLAYOUT_SIZE_NORMAL},
     * {@link #SCREENLAYOUT_SIZE_LARGE}, or {@link #SCREENLAYOUT_SIZE_XLARGE}.</p>
     *
     * <p>The {@link #SCREENLAYOUT_LONG_MASK} defines whether the screen
     * is wider/taller than normal.  They may be one of
     * {@link #SCREENLAYOUT_LONG_NO} or {@link #SCREENLAYOUT_LONG_YES}.</p>
     *
     * <p>The {@link #SCREENLAYOUT_LAYOUTDIR_MASK} defines whether the screen layout
     * is either LTR or RTL.  They may be one of
     * {@link #SCREENLAYOUT_LAYOUTDIR_LTR} or {@link #SCREENLAYOUT_LAYOUTDIR_RTL}.</p>
     *
     * <p>The {@link #SCREENLAYOUT_ROUND_MASK} defines whether the screen has a rounded
     * shape. They may be one of {@link #SCREENLAYOUT_ROUND_NO} or {@link #SCREENLAYOUT_ROUND_YES}.
     * </p>
     *
     * <p>See <a href="{@docRoot }guide/practices/screens_support.html">Supporting
     * Multiple Screens</a> for more information.</p>
     */
    public int screenLayout;

    /**
     * Configuration relating to the windowing state of the object associated with this
     * Configuration. Contents of this field are not intended to affect resources, but need to be
     * communicated and propagated at the same time as the rest of Configuration.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public final android.app.WindowConfiguration windowConfiguration = new android.app.WindowConfiguration();

    /**
     *
     *
     * @unknown 
     */
    public static int resetScreenLayout(int curLayout) {
        return (curLayout & (~((android.content.res.Configuration.SCREENLAYOUT_LONG_MASK | android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) | android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED))) | (android.content.res.Configuration.SCREENLAYOUT_LONG_YES | android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int reduceScreenLayout(int curLayout, int longSizeDp, int shortSizeDp) {
        int screenLayoutSize;
        boolean screenLayoutLong;
        boolean screenLayoutCompatNeeded;
        // These semi-magic numbers define our compatibility modes for
        // applications with different screens.  These are guarantees to
        // app developers about the space they can expect for a particular
        // configuration.  DO NOT CHANGE!
        if (longSizeDp < 470) {
            // This is shorter than an HVGA normal density screen (which
            // is 480 pixels on its long side).
            screenLayoutSize = android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
            screenLayoutLong = false;
            screenLayoutCompatNeeded = false;
        } else {
            // What size is this screen screen?
            if ((longSizeDp >= 960) && (shortSizeDp >= 720)) {
                // 1.5xVGA or larger screens at medium density are the point
                // at which we consider it to be an extra large screen.
                screenLayoutSize = android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
            } else
                if ((longSizeDp >= 640) && (shortSizeDp >= 480)) {
                    // VGA or larger screens at medium density are the point
                    // at which we consider it to be a large screen.
                    screenLayoutSize = android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
                } else {
                    screenLayoutSize = android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
                }

            // If this screen is wider than normal HVGA, or taller
            // than FWVGA, then for old apps we want to run in size
            // compatibility mode.
            if ((shortSizeDp > 321) || (longSizeDp > 570)) {
                screenLayoutCompatNeeded = true;
            } else {
                screenLayoutCompatNeeded = false;
            }
            // Is this a long screen?
            if (((longSizeDp * 3) / 5) >= (shortSizeDp - 1)) {
                // Anything wider than WVGA (5:3) is considering to be long.
                screenLayoutLong = true;
            } else {
                screenLayoutLong = false;
            }
        }
        // Now reduce the last screenLayout to not be better than what we
        // have found.
        if (!screenLayoutLong) {
            curLayout = (curLayout & (~android.content.res.Configuration.SCREENLAYOUT_LONG_MASK)) | android.content.res.Configuration.SCREENLAYOUT_LONG_NO;
        }
        if (screenLayoutCompatNeeded) {
            curLayout |= android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED;
        }
        int curSize = curLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenLayoutSize < curSize) {
            curLayout = (curLayout & (~android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) | screenLayoutSize;
        }
        return curLayout;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String configurationDiffToString(int diff) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        if ((diff & android.content.pm.ActivityInfo.CONFIG_MCC) != 0) {
            list.add("CONFIG_MCC");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_MNC) != 0) {
            list.add("CONFIG_MNC");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_LOCALE) != 0) {
            list.add("CONFIG_LOCALE");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_TOUCHSCREEN) != 0) {
            list.add("CONFIG_TOUCHSCREEN");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_KEYBOARD) != 0) {
            list.add("CONFIG_KEYBOARD");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN) != 0) {
            list.add("CONFIG_KEYBOARD_HIDDEN");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_NAVIGATION) != 0) {
            list.add("CONFIG_NAVIGATION");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_ORIENTATION) != 0) {
            list.add("CONFIG_ORIENTATION");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT) != 0) {
            list.add("CONFIG_SCREEN_LAYOUT");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_COLOR_MODE) != 0) {
            list.add("CONFIG_COLOR_MODE");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_UI_MODE) != 0) {
            list.add("CONFIG_UI_MODE");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE) != 0) {
            list.add("CONFIG_SCREEN_SIZE");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE) != 0) {
            list.add("CONFIG_SMALLEST_SCREEN_SIZE");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION) != 0) {
            list.add("CONFIG_LAYOUT_DIRECTION");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_FONT_SCALE) != 0) {
            list.add("CONFIG_FONT_SCALE");
        }
        if ((diff & android.content.pm.ActivityInfo.CONFIG_ASSETS_PATHS) != 0) {
            list.add("CONFIG_ASSETS_PATHS");
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder("{");
        for (int i = 0, n = list.size(); i < n; i++) {
            builder.append(list.get(i));
            if (i != (n - 1)) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * Check if the Configuration's current {@link #screenLayout} is at
     * least the given size.
     *
     * @param size
     * 		The desired size, either {@link #SCREENLAYOUT_SIZE_SMALL},
     * 		{@link #SCREENLAYOUT_SIZE_NORMAL}, {@link #SCREENLAYOUT_SIZE_LARGE}, or
     * 		{@link #SCREENLAYOUT_SIZE_XLARGE}.
     * @return Returns true if the current screen layout size is at least
    the given size.
     */
    public boolean isLayoutSizeAtLeast(int size) {
        int cur = screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
        if (cur == android.content.res.Configuration.SCREENLAYOUT_SIZE_UNDEFINED)
            return false;

        return cur >= size;
    }

    /**
     * Constant for {@link #touchscreen}: a value indicating that no value has been set.
     */
    public static final int TOUCHSCREEN_UNDEFINED = 0;

    /**
     * Constant for {@link #touchscreen}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#TouchscreenQualifier">notouch</a>
     * resource qualifier.
     */
    public static final int TOUCHSCREEN_NOTOUCH = 1;

    /**
     *
     *
     * @deprecated Not currently supported or used.
     */
    @java.lang.Deprecated
    public static final int TOUCHSCREEN_STYLUS = 2;

    /**
     * Constant for {@link #touchscreen}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#TouchscreenQualifier">finger</a>
     * resource qualifier.
     */
    public static final int TOUCHSCREEN_FINGER = 3;

    /**
     * The kind of touch screen attached to the device.
     * One of: {@link #TOUCHSCREEN_NOTOUCH}, {@link #TOUCHSCREEN_FINGER}.
     */
    public int touchscreen;

    /**
     * Constant for {@link #keyboard}: a value indicating that no value has been set.
     */
    public static final int KEYBOARD_UNDEFINED = 0;

    /**
     * Constant for {@link #keyboard}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ImeQualifier">nokeys</a>
     * resource qualifier.
     */
    public static final int KEYBOARD_NOKEYS = 1;

    /**
     * Constant for {@link #keyboard}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ImeQualifier">qwerty</a>
     * resource qualifier.
     */
    public static final int KEYBOARD_QWERTY = 2;

    /**
     * Constant for {@link #keyboard}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ImeQualifier">12key</a>
     * resource qualifier.
     */
    public static final int KEYBOARD_12KEY = 3;

    /**
     * The kind of keyboard attached to the device.
     * One of: {@link #KEYBOARD_NOKEYS}, {@link #KEYBOARD_QWERTY},
     * {@link #KEYBOARD_12KEY}.
     */
    public int keyboard;

    /**
     * Constant for {@link #keyboardHidden}: a value indicating that no value has been set.
     */
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;

    /**
     * Constant for {@link #keyboardHidden}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#KeyboardAvailQualifier">keysexposed</a>
     * resource qualifier.
     */
    public static final int KEYBOARDHIDDEN_NO = 1;

    /**
     * Constant for {@link #keyboardHidden}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#KeyboardAvailQualifier">keyshidden</a>
     * resource qualifier.
     */
    public static final int KEYBOARDHIDDEN_YES = 2;

    /**
     * Constant matching actual resource implementation. {@hide }
     */
    public static final int KEYBOARDHIDDEN_SOFT = 3;

    /**
     * A flag indicating whether any keyboard is available.  Unlike
     * {@link #hardKeyboardHidden}, this also takes into account a soft
     * keyboard, so if the hard keyboard is hidden but there is soft
     * keyboard available, it will be set to NO.  Value is one of:
     * {@link #KEYBOARDHIDDEN_NO}, {@link #KEYBOARDHIDDEN_YES}.
     */
    public int keyboardHidden;

    /**
     * Constant for {@link #hardKeyboardHidden}: a value indicating that no value has been set.
     */
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;

    /**
     * Constant for {@link #hardKeyboardHidden}, value corresponding to the
     * physical keyboard being exposed.
     */
    public static final int HARDKEYBOARDHIDDEN_NO = 1;

    /**
     * Constant for {@link #hardKeyboardHidden}, value corresponding to the
     * physical keyboard being hidden.
     */
    public static final int HARDKEYBOARDHIDDEN_YES = 2;

    /**
     * A flag indicating whether the hard keyboard has been hidden.  This will
     * be set on a device with a mechanism to hide the keyboard from the
     * user, when that mechanism is closed.  One of:
     * {@link #HARDKEYBOARDHIDDEN_NO}, {@link #HARDKEYBOARDHIDDEN_YES}.
     */
    public int hardKeyboardHidden;

    /**
     * Constant for {@link #navigation}: a value indicating that no value has been set.
     */
    public static final int NAVIGATION_UNDEFINED = 0;

    /**
     * Constant for {@link #navigation}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NavigationQualifier">nonav</a>
     * resource qualifier.
     */
    public static final int NAVIGATION_NONAV = 1;

    /**
     * Constant for {@link #navigation}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NavigationQualifier">dpad</a>
     * resource qualifier.
     */
    public static final int NAVIGATION_DPAD = 2;

    /**
     * Constant for {@link #navigation}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NavigationQualifier">trackball</a>
     * resource qualifier.
     */
    public static final int NAVIGATION_TRACKBALL = 3;

    /**
     * Constant for {@link #navigation}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NavigationQualifier">wheel</a>
     * resource qualifier.
     */
    public static final int NAVIGATION_WHEEL = 4;

    /**
     * The kind of navigation method available on the device.
     * One of: {@link #NAVIGATION_NONAV}, {@link #NAVIGATION_DPAD},
     * {@link #NAVIGATION_TRACKBALL}, {@link #NAVIGATION_WHEEL}.
     */
    public int navigation;

    /**
     * Constant for {@link #navigationHidden}: a value indicating that no value has been set.
     */
    public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;

    /**
     * Constant for {@link #navigationHidden}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NavAvailQualifier">navexposed</a>
     * resource qualifier.
     */
    public static final int NAVIGATIONHIDDEN_NO = 1;

    /**
     * Constant for {@link #navigationHidden}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NavAvailQualifier">navhidden</a>
     * resource qualifier.
     */
    public static final int NAVIGATIONHIDDEN_YES = 2;

    /**
     * A flag indicating whether any 5-way or DPAD navigation available.
     * This will be set on a device with a mechanism to hide the navigation
     * controls from the user, when that mechanism is closed.  One of:
     * {@link #NAVIGATIONHIDDEN_NO}, {@link #NAVIGATIONHIDDEN_YES}.
     */
    public int navigationHidden;

    /**
     * Constant for {@link #orientation}: a value indicating that no value has been set.
     */
    public static final int ORIENTATION_UNDEFINED = 0;

    /**
     * Constant for {@link #orientation}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#OrientationQualifier">port</a>
     * resource qualifier.
     */
    public static final int ORIENTATION_PORTRAIT = 1;

    /**
     * Constant for {@link #orientation}, value corresponding to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#OrientationQualifier">land</a>
     * resource qualifier.
     */
    public static final int ORIENTATION_LANDSCAPE = 2;

    /**
     *
     *
     * @deprecated Not currently supported or used.
     */
    @java.lang.Deprecated
    public static final int ORIENTATION_SQUARE = 3;

    /**
     * Overall orientation of the screen.  May be one of
     * {@link #ORIENTATION_LANDSCAPE}, {@link #ORIENTATION_PORTRAIT}.
     */
    public int orientation;

    /**
     * Constant for {@link #uiMode}: bits that encode the mode type.
     */
    public static final int UI_MODE_TYPE_MASK = 0xf;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value indicating that no mode type has been set.
     */
    public static final int UI_MODE_TYPE_UNDEFINED = 0x0;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">no
     * UI mode</a> resource qualifier specified.
     */
    public static final int UI_MODE_TYPE_NORMAL = 0x1;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">desk</a>
     * resource qualifier.
     */
    public static final int UI_MODE_TYPE_DESK = 0x2;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">car</a>
     * resource qualifier.
     */
    public static final int UI_MODE_TYPE_CAR = 0x3;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">television</a>
     * resource qualifier.
     */
    public static final int UI_MODE_TYPE_TELEVISION = 0x4;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">appliance</a>
     * resource qualifier.
     */
    public static final int UI_MODE_TYPE_APPLIANCE = 0x5;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">watch</a>
     * resource qualifier.
     */
    public static final int UI_MODE_TYPE_WATCH = 0x6;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_TYPE_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#UiModeQualifier">vrheadset</a>
     * resource qualifier.
     */
    public static final int UI_MODE_TYPE_VR_HEADSET = 0x7;

    /**
     * Constant for {@link #uiMode}: bits that encode the night mode.
     */
    public static final int UI_MODE_NIGHT_MASK = 0x30;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_NIGHT_MASK}
     * value indicating that no mode type has been set.
     */
    public static final int UI_MODE_NIGHT_UNDEFINED = 0x0;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_NIGHT_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NightQualifier">notnight</a>
     * resource qualifier.
     */
    public static final int UI_MODE_NIGHT_NO = 0x10;

    /**
     * Constant for {@link #uiMode}: a {@link #UI_MODE_NIGHT_MASK}
     * value that corresponds to the
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#NightQualifier">night</a>
     * resource qualifier.
     */
    public static final int UI_MODE_NIGHT_YES = 0x20;

    /**
     * Bit mask of the ui mode.  Currently there are two fields:
     * <p>The {@link #UI_MODE_TYPE_MASK} bits define the overall ui mode of the
     * device. They may be one of {@link #UI_MODE_TYPE_UNDEFINED},
     * {@link #UI_MODE_TYPE_NORMAL}, {@link #UI_MODE_TYPE_DESK},
     * {@link #UI_MODE_TYPE_CAR}, {@link #UI_MODE_TYPE_TELEVISION},
     * {@link #UI_MODE_TYPE_APPLIANCE}, {@link #UI_MODE_TYPE_WATCH},
     * or {@link #UI_MODE_TYPE_VR_HEADSET}.
     *
     * <p>The {@link #UI_MODE_NIGHT_MASK} defines whether the screen
     * is in a special mode. They may be one of {@link #UI_MODE_NIGHT_UNDEFINED},
     * {@link #UI_MODE_NIGHT_NO} or {@link #UI_MODE_NIGHT_YES}.
     */
    public int uiMode;

    /**
     * Default value for {@link #screenWidthDp} indicating that no width
     * has been specified.
     */
    public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;

    /**
     * The current width of the available screen space, in dp units,
     * corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenWidthQualifier">screen
     * width</a> resource qualifier.  Set to
     * {@link #SCREEN_WIDTH_DP_UNDEFINED} if no width is specified.
     */
    public int screenWidthDp;

    /**
     * Default value for {@link #screenHeightDp} indicating that no width
     * has been specified.
     */
    public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;

    /**
     * The current height of the available screen space, in dp units,
     * corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#ScreenHeightQualifier">screen
     * height</a> resource qualifier.  Set to
     * {@link #SCREEN_HEIGHT_DP_UNDEFINED} if no height is specified.
     */
    public int screenHeightDp;

    /**
     * Default value for {@link #smallestScreenWidthDp} indicating that no width
     * has been specified.
     */
    public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;

    /**
     * The smallest screen size an application will see in normal operation,
     * corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#SmallestScreenWidthQualifier">smallest
     * screen width</a> resource qualifier.
     * This is the smallest value of both screenWidthDp and screenHeightDp
     * in both portrait and landscape.  Set to
     * {@link #SMALLEST_SCREEN_WIDTH_DP_UNDEFINED} if no width is specified.
     */
    public int smallestScreenWidthDp;

    /**
     * Default value for {@link #densityDpi} indicating that no width
     * has been specified.
     */
    public static final int DENSITY_DPI_UNDEFINED = 0;

    /**
     * Value for {@link #densityDpi} for resources that scale to any density (vector drawables).
     * {@hide }
     */
    public static final int DENSITY_DPI_ANY = 0xfffe;

    /**
     * Value for {@link #densityDpi} for resources that are not meant to be scaled.
     * {@hide }
     */
    public static final int DENSITY_DPI_NONE = 0xffff;

    /**
     * The target screen density being rendered to,
     * corresponding to
     * <a href="{@docRoot }guide/topics/resources/providing-resources.html#DensityQualifier">density</a>
     * resource qualifier.  Set to
     * {@link #DENSITY_DPI_UNDEFINED} if no density is specified.
     */
    public int densityDpi;

    /**
     *
     *
     * @unknown Hack to get this information from WM to app running in compat mode.
     */
    public int compatScreenWidthDp;

    /**
     *
     *
     * @unknown Hack to get this information from WM to app running in compat mode.
     */
    public int compatScreenHeightDp;

    /**
     *
     *
     * @unknown Hack to get this information from WM to app running in compat mode.
     */
    public int compatSmallestScreenWidthDp;

    /**
     * An undefined assetsSeq. This will not override an existing assetsSeq.
     *
     * @unknown 
     */
    public static final int ASSETS_SEQ_UNDEFINED = 0;

    /**
     * Internal counter that allows us to piggyback off the configuration change mechanism to
     * signal to apps that the the assets for an Application have changed. A difference in these
     * between two Configurations will yield a diff flag of
     * {@link ActivityInfo#CONFIG_ASSETS_PATHS}.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public int assetsSeq;

    /**
     *
     *
     * @unknown Internal book-keeping.
     */
    @android.annotation.UnsupportedAppUsage
    public int seq;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "NATIVE_CONFIG_" }, value = { android.content.res.Configuration.NATIVE_CONFIG_MCC, android.content.res.Configuration.NATIVE_CONFIG_MNC, android.content.res.Configuration.NATIVE_CONFIG_LOCALE, android.content.res.Configuration.NATIVE_CONFIG_TOUCHSCREEN, android.content.res.Configuration.NATIVE_CONFIG_KEYBOARD, android.content.res.Configuration.NATIVE_CONFIG_KEYBOARD_HIDDEN, android.content.res.Configuration.NATIVE_CONFIG_NAVIGATION, android.content.res.Configuration.NATIVE_CONFIG_ORIENTATION, android.content.res.Configuration.NATIVE_CONFIG_DENSITY, android.content.res.Configuration.NATIVE_CONFIG_SCREEN_SIZE, android.content.res.Configuration.NATIVE_CONFIG_VERSION, android.content.res.Configuration.NATIVE_CONFIG_SCREEN_LAYOUT, android.content.res.Configuration.NATIVE_CONFIG_UI_MODE, android.content.res.Configuration.NATIVE_CONFIG_SMALLEST_SCREEN_SIZE, android.content.res.Configuration.NATIVE_CONFIG_LAYOUTDIR, android.content.res.Configuration.NATIVE_CONFIG_COLOR_MODE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface NativeConfig {}

    /**
     *
     *
     * @unknown Native-specific bit mask for MCC config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_MCC = 0x1;

    /**
     *
     *
     * @unknown Native-specific bit mask for MNC config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_MNC = 0x2;

    /**
     *
     *
     * @unknown Native-specific bit mask for LOCALE config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_LOCALE = 0x4;

    /**
     *
     *
     * @unknown Native-specific bit mask for TOUCHSCREEN config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_TOUCHSCREEN = 0x8;

    /**
     *
     *
     * @unknown Native-specific bit mask for KEYBOARD config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_KEYBOARD = 0x10;

    /**
     *
     *
     * @unknown Native-specific bit mask for KEYBOARD_HIDDEN config; DO NOT USE UNLESS YOU
    ARE SURE.
     */
    public static final int NATIVE_CONFIG_KEYBOARD_HIDDEN = 0x20;

    /**
     *
     *
     * @unknown Native-specific bit mask for NAVIGATION config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_NAVIGATION = 0x40;

    /**
     *
     *
     * @unknown Native-specific bit mask for ORIENTATION config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_ORIENTATION = 0x80;

    /**
     *
     *
     * @unknown Native-specific bit mask for DENSITY config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_DENSITY = 0x100;

    /**
     *
     *
     * @unknown Native-specific bit mask for SCREEN_SIZE config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_SCREEN_SIZE = 0x200;

    /**
     *
     *
     * @unknown Native-specific bit mask for VERSION config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_VERSION = 0x400;

    /**
     *
     *
     * @unknown Native-specific bit mask for SCREEN_LAYOUT config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_SCREEN_LAYOUT = 0x800;

    /**
     *
     *
     * @unknown Native-specific bit mask for UI_MODE config; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_UI_MODE = 0x1000;

    /**
     *
     *
     * @unknown Native-specific bit mask for SMALLEST_SCREEN_SIZE config; DO NOT USE UNLESS YOU
    ARE SURE.
     */
    public static final int NATIVE_CONFIG_SMALLEST_SCREEN_SIZE = 0x2000;

    /**
     *
     *
     * @unknown Native-specific bit mask for LAYOUTDIR config ; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_LAYOUTDIR = 0x4000;

    /**
     *
     *
     * @unknown Native-specific bit mask for COLOR_MODE config ; DO NOT USE UNLESS YOU ARE SURE.
     */
    public static final int NATIVE_CONFIG_COLOR_MODE = 0x10000;

    /**
     * <p>Construct an invalid Configuration. This state is only suitable for constructing a
     * Configuration delta that will be applied to some valid Configuration object. In order to
     * create a valid standalone Configuration, you must call {@link #setToDefaults}. </p>
     *
     * <p>Example:</p>
     * <pre class="prettyprint">
     *     Configuration validConfig = new Configuration();
     *     validConfig.setToDefaults();
     *
     *     Configuration deltaOnlyConfig = new Configuration();
     *     deltaOnlyConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
     *
     *     validConfig.updateFrom(deltaOnlyConfig);
     * </pre>
     */
    public Configuration() {
        unset();
    }

    /**
     * Makes a deep copy suitable for modification.
     */
    public Configuration(android.content.res.Configuration o) {
        setTo(o);
    }

    /* This brings mLocaleList in sync with locale in case a user of the older API who doesn't know
    about setLocales() has changed locale directly.
     */
    private void fixUpLocaleList() {
        if (((locale == null) && (!mLocaleList.isEmpty())) || ((locale != null) && (!locale.equals(mLocaleList.get(0))))) {
            mLocaleList = (locale == null) ? android.os.LocaleList.getEmptyLocaleList() : new android.os.LocaleList(locale);
        }
    }

    /**
     * Sets the fields in this object to those in the given Configuration.
     *
     * @param o
     * 		The Configuration object used to set the values of this Configuration's fields.
     */
    public void setTo(android.content.res.Configuration o) {
        fontScale = o.fontScale;
        mcc = o.mcc;
        mnc = o.mnc;
        locale = (o.locale == null) ? null : ((java.util.Locale) (o.locale.clone()));
        o.fixUpLocaleList();
        mLocaleList = o.mLocaleList;
        userSetLocale = o.userSetLocale;
        touchscreen = o.touchscreen;
        keyboard = o.keyboard;
        keyboardHidden = o.keyboardHidden;
        hardKeyboardHidden = o.hardKeyboardHidden;
        navigation = o.navigation;
        navigationHidden = o.navigationHidden;
        orientation = o.orientation;
        screenLayout = o.screenLayout;
        colorMode = o.colorMode;
        uiMode = o.uiMode;
        screenWidthDp = o.screenWidthDp;
        screenHeightDp = o.screenHeightDp;
        smallestScreenWidthDp = o.smallestScreenWidthDp;
        densityDpi = o.densityDpi;
        compatScreenWidthDp = o.compatScreenWidthDp;
        compatScreenHeightDp = o.compatScreenHeightDp;
        compatSmallestScreenWidthDp = o.compatSmallestScreenWidthDp;
        assetsSeq = o.assetsSeq;
        seq = o.seq;
        windowConfiguration.setTo(o.windowConfiguration);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("{");
        sb.append(fontScale);
        sb.append(" ");
        if (mcc != 0) {
            sb.append(mcc);
            sb.append("mcc");
        } else {
            sb.append("?mcc");
        }
        if (mnc != 0) {
            sb.append(mnc);
            sb.append("mnc");
        } else {
            sb.append("?mnc");
        }
        fixUpLocaleList();
        if (!mLocaleList.isEmpty()) {
            sb.append(" ");
            sb.append(mLocaleList);
        } else {
            sb.append(" ?localeList");
        }
        int layoutDir = screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK;
        switch (layoutDir) {
            case android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_UNDEFINED :
                sb.append(" ?layoutDir");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_LTR :
                sb.append(" ldltr");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_RTL :
                sb.append(" ldrtl");
                break;
            default :
                sb.append(" layoutDir=");
                sb.append(layoutDir >> android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_SHIFT);
                break;
        }
        if (smallestScreenWidthDp != android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
            sb.append(" sw");
            sb.append(smallestScreenWidthDp);
            sb.append("dp");
        } else {
            sb.append(" ?swdp");
        }
        if (screenWidthDp != android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
            sb.append(" w");
            sb.append(screenWidthDp);
            sb.append("dp");
        } else {
            sb.append(" ?wdp");
        }
        if (screenHeightDp != android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
            sb.append(" h");
            sb.append(screenHeightDp);
            sb.append("dp");
        } else {
            sb.append(" ?hdp");
        }
        if (densityDpi != android.content.res.Configuration.DENSITY_DPI_UNDEFINED) {
            sb.append(" ");
            sb.append(densityDpi);
            sb.append("dpi");
        } else {
            sb.append(" ?density");
        }
        switch (screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) {
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_UNDEFINED :
                sb.append(" ?lsize");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL :
                sb.append(" smll");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL :
                sb.append(" nrml");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE :
                sb.append(" lrg");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE :
                sb.append(" xlrg");
                break;
            default :
                sb.append(" layoutSize=");
                sb.append(screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK);
                break;
        }
        switch (screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK) {
            case android.content.res.Configuration.SCREENLAYOUT_LONG_UNDEFINED :
                sb.append(" ?long");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_LONG_NO :
                break;
            case android.content.res.Configuration.SCREENLAYOUT_LONG_YES :
                sb.append(" long");
                break;
            default :
                sb.append(" layoutLong=");
                sb.append(screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK);
                break;
        }
        switch (colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) {
            case android.content.res.Configuration.COLOR_MODE_HDR_UNDEFINED :
                sb.append(" ?ldr");
                break;// most likely not HDR

            case android.content.res.Configuration.COLOR_MODE_HDR_NO :
                break;
            case android.content.res.Configuration.COLOR_MODE_HDR_YES :
                sb.append(" hdr");
                break;
            default :
                sb.append(" dynamicRange=");
                sb.append(colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK);
                break;
        }
        switch (colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) {
            case android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED :
                sb.append(" ?wideColorGamut");
                break;
            case android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_NO :
                break;
            case android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_YES :
                sb.append(" widecg");
                break;
            default :
                sb.append(" wideColorGamut=");
                sb.append(colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK);
                break;
        }
        switch (orientation) {
            case android.content.res.Configuration.ORIENTATION_UNDEFINED :
                sb.append(" ?orien");
                break;
            case android.content.res.Configuration.ORIENTATION_LANDSCAPE :
                sb.append(" land");
                break;
            case android.content.res.Configuration.ORIENTATION_PORTRAIT :
                sb.append(" port");
                break;
            default :
                sb.append(" orien=");
                sb.append(orientation);
                break;
        }
        switch (uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK) {
            case android.content.res.Configuration.UI_MODE_TYPE_UNDEFINED :
                sb.append(" ?uimode");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_NORMAL :
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_DESK :
                sb.append(" desk");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_CAR :
                sb.append(" car");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_TELEVISION :
                sb.append(" television");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_APPLIANCE :
                sb.append(" appliance");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_WATCH :
                sb.append(" watch");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_VR_HEADSET :
                sb.append(" vrheadset");
                break;
            default :
                sb.append(" uimode=");
                sb.append(uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK);
                break;
        }
        switch (uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            case android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED :
                sb.append(" ?night");
                break;
            case android.content.res.Configuration.UI_MODE_NIGHT_NO :
                break;
            case android.content.res.Configuration.UI_MODE_NIGHT_YES :
                sb.append(" night");
                break;
            default :
                sb.append(" night=");
                sb.append(uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK);
                break;
        }
        switch (touchscreen) {
            case android.content.res.Configuration.TOUCHSCREEN_UNDEFINED :
                sb.append(" ?touch");
                break;
            case android.content.res.Configuration.TOUCHSCREEN_NOTOUCH :
                sb.append(" -touch");
                break;
            case android.content.res.Configuration.TOUCHSCREEN_STYLUS :
                sb.append(" stylus");
                break;
            case android.content.res.Configuration.TOUCHSCREEN_FINGER :
                sb.append(" finger");
                break;
            default :
                sb.append(" touch=");
                sb.append(touchscreen);
                break;
        }
        switch (keyboard) {
            case android.content.res.Configuration.KEYBOARD_UNDEFINED :
                sb.append(" ?keyb");
                break;
            case android.content.res.Configuration.KEYBOARD_NOKEYS :
                sb.append(" -keyb");
                break;
            case android.content.res.Configuration.KEYBOARD_QWERTY :
                sb.append(" qwerty");
                break;
            case android.content.res.Configuration.KEYBOARD_12KEY :
                sb.append(" 12key");
                break;
            default :
                sb.append(" keys=");
                sb.append(keyboard);
                break;
        }
        switch (keyboardHidden) {
            case android.content.res.Configuration.KEYBOARDHIDDEN_UNDEFINED :
                sb.append("/?");
                break;
            case android.content.res.Configuration.KEYBOARDHIDDEN_NO :
                sb.append("/v");
                break;
            case android.content.res.Configuration.KEYBOARDHIDDEN_YES :
                sb.append("/h");
                break;
            case android.content.res.Configuration.KEYBOARDHIDDEN_SOFT :
                sb.append("/s");
                break;
            default :
                sb.append("/");
                sb.append(keyboardHidden);
                break;
        }
        switch (hardKeyboardHidden) {
            case android.content.res.Configuration.HARDKEYBOARDHIDDEN_UNDEFINED :
                sb.append("/?");
                break;
            case android.content.res.Configuration.HARDKEYBOARDHIDDEN_NO :
                sb.append("/v");
                break;
            case android.content.res.Configuration.HARDKEYBOARDHIDDEN_YES :
                sb.append("/h");
                break;
            default :
                sb.append("/");
                sb.append(hardKeyboardHidden);
                break;
        }
        switch (navigation) {
            case android.content.res.Configuration.NAVIGATION_UNDEFINED :
                sb.append(" ?nav");
                break;
            case android.content.res.Configuration.NAVIGATION_NONAV :
                sb.append(" -nav");
                break;
            case android.content.res.Configuration.NAVIGATION_DPAD :
                sb.append(" dpad");
                break;
            case android.content.res.Configuration.NAVIGATION_TRACKBALL :
                sb.append(" tball");
                break;
            case android.content.res.Configuration.NAVIGATION_WHEEL :
                sb.append(" wheel");
                break;
            default :
                sb.append(" nav=");
                sb.append(navigation);
                break;
        }
        switch (navigationHidden) {
            case android.content.res.Configuration.NAVIGATIONHIDDEN_UNDEFINED :
                sb.append("/?");
                break;
            case android.content.res.Configuration.NAVIGATIONHIDDEN_NO :
                sb.append("/v");
                break;
            case android.content.res.Configuration.NAVIGATIONHIDDEN_YES :
                sb.append("/h");
                break;
            default :
                sb.append("/");
                sb.append(navigationHidden);
                break;
        }
        sb.append(" winConfig=");
        sb.append(windowConfiguration);
        if (assetsSeq != 0) {
            sb.append(" as.").append(assetsSeq);
        }
        if (seq != 0) {
            sb.append(" s.").append(seq);
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * Write to a protocol buffer output stream.
     * Protocol buffer message definition at {@link android.content.ConfigurationProto}
     * Has the option to ignore fields that don't need to be persisted to disk.
     *
     * @param protoOutputStream
     * 		Stream to write the Configuration object to.
     * @param fieldId
     * 		Field Id of the Configuration as defined in the parent message
     * @param persisted
     * 		Note if this proto will be persisted to disk
     * @param critical
     * 		If true, reduce amount of data written.
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream, long fieldId, boolean persisted, boolean critical) {
        final long token = protoOutputStream.start(fieldId);
        if (!critical) {
            protoOutputStream.write(android.content.ConfigurationProto.FONT_SCALE, fontScale);
            protoOutputStream.write(android.content.ConfigurationProto.MCC, mcc);
            protoOutputStream.write(android.content.ConfigurationProto.MNC, mnc);
            if (mLocaleList != null) {
                mLocaleList.writeToProto(protoOutputStream, android.content.ConfigurationProto.LOCALES);
            }
            protoOutputStream.write(android.content.ConfigurationProto.SCREEN_LAYOUT, screenLayout);
            protoOutputStream.write(android.content.ConfigurationProto.COLOR_MODE, colorMode);
            protoOutputStream.write(android.content.ConfigurationProto.TOUCHSCREEN, touchscreen);
            protoOutputStream.write(android.content.ConfigurationProto.KEYBOARD, keyboard);
            protoOutputStream.write(android.content.ConfigurationProto.KEYBOARD_HIDDEN, keyboardHidden);
            protoOutputStream.write(android.content.ConfigurationProto.HARD_KEYBOARD_HIDDEN, hardKeyboardHidden);
            protoOutputStream.write(android.content.ConfigurationProto.NAVIGATION, navigation);
            protoOutputStream.write(android.content.ConfigurationProto.NAVIGATION_HIDDEN, navigationHidden);
            protoOutputStream.write(android.content.ConfigurationProto.UI_MODE, uiMode);
            protoOutputStream.write(android.content.ConfigurationProto.SMALLEST_SCREEN_WIDTH_DP, smallestScreenWidthDp);
            protoOutputStream.write(android.content.ConfigurationProto.DENSITY_DPI, densityDpi);
            // For persistence, we do not care about window configuration
            if ((!persisted) && (windowConfiguration != null)) {
                windowConfiguration.writeToProto(protoOutputStream, android.content.ConfigurationProto.WINDOW_CONFIGURATION);
            }
        }
        protoOutputStream.write(android.content.ConfigurationProto.ORIENTATION, orientation);
        protoOutputStream.write(android.content.ConfigurationProto.SCREEN_WIDTH_DP, screenWidthDp);
        protoOutputStream.write(android.content.ConfigurationProto.SCREEN_HEIGHT_DP, screenHeightDp);
        protoOutputStream.end(token);
    }

    /**
     * Write to a protocol buffer output stream.
     * Protocol buffer message definition at {@link android.content.ConfigurationProto}
     *
     * @param protoOutputStream
     * 		Stream to write the Configuration object to.
     * @param fieldId
     * 		Field Id of the Configuration as defined in the parent message
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream, long fieldId) {
        /* persisted */
        /* critical */
        writeToProto(protoOutputStream, fieldId, false, false);
    }

    /**
     * Write to a protocol buffer output stream.
     * Protocol buffer message definition at {@link android.content.ConfigurationProto}
     *
     * @param protoOutputStream
     * 		Stream to write the Configuration object to.
     * @param fieldId
     * 		Field Id of the Configuration as defined in the parent message
     * @param critical
     * 		If true, reduce amount of data written.
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream, long fieldId, boolean critical) {
        /* persisted */
        writeToProto(protoOutputStream, fieldId, false, critical);
    }

    /**
     * Read from a protocol buffer output stream.
     * Protocol buffer message definition at {@link android.content.ConfigurationProto}
     *
     * @param protoInputStream
     * 		Stream to read the Configuration object from.
     * @param fieldId
     * 		Field Id of the Configuration as defined in the parent message
     * @unknown 
     */
    public void readFromProto(android.util.proto.ProtoInputStream protoInputStream, long fieldId) throws java.io.IOException {
        final long token = protoInputStream.start(fieldId);
        final java.util.List<java.util.Locale> list = new java.util.ArrayList();
        try {
            while (protoInputStream.nextField() != android.util.proto.ProtoInputStream.NO_MORE_FIELDS) {
                switch (protoInputStream.getFieldNumber()) {
                    case ((int) (android.content.ConfigurationProto.FONT_SCALE)) :
                        fontScale = protoInputStream.readFloat(android.content.ConfigurationProto.FONT_SCALE);
                        break;
                    case ((int) (android.content.ConfigurationProto.MCC)) :
                        mcc = protoInputStream.readInt(android.content.ConfigurationProto.MCC);
                        break;
                    case ((int) (android.content.ConfigurationProto.MNC)) :
                        mnc = protoInputStream.readInt(android.content.ConfigurationProto.MNC);
                        break;
                    case ((int) (android.content.ConfigurationProto.LOCALES)) :
                        // Parse the Locale here to handle all the repeated Locales
                        // The LocaleList will be created when the message is completed
                        final long localeToken = protoInputStream.start(android.content.ConfigurationProto.LOCALES);
                        java.lang.String language = "";
                        java.lang.String country = "";
                        java.lang.String variant = "";
                        java.lang.String script = "";
                        try {
                            while (protoInputStream.nextField() != android.util.proto.ProtoInputStream.NO_MORE_FIELDS) {
                                switch (protoInputStream.getFieldNumber()) {
                                    case ((int) (android.content.LocaleProto.LANGUAGE)) :
                                        language = protoInputStream.readString(LocaleProto.LANGUAGE);
                                        break;
                                    case ((int) (android.content.LocaleProto.COUNTRY)) :
                                        country = protoInputStream.readString(LocaleProto.COUNTRY);
                                        break;
                                    case ((int) (android.content.LocaleProto.VARIANT)) :
                                        variant = protoInputStream.readString(LocaleProto.VARIANT);
                                        break;
                                    case ((int) (android.content.LocaleProto.SCRIPT)) :
                                        script = protoInputStream.readString(LocaleProto.SCRIPT);
                                        break;
                                }
                            } 
                        } catch (android.util.proto.WireTypeMismatchException wtme) {
                            // rethrow for caller deal with
                            throw wtme;
                        } finally {
                            protoInputStream.end(localeToken);
                            try {
                                final java.util.Locale locale = new java.util.Locale.Builder().setLanguage(language).setRegion(country).setVariant(variant).setScript(script).build();
                                list.add(locale);
                            } catch (java.util.IllformedLocaleException e) {
                                android.util.Slog.e(android.content.res.Configuration.TAG, ((((((("readFromProto error building locale with: " + "language-") + language) + ";country-") + country) + ";variant-") + variant) + ";script-") + script);
                            }
                        }
                        break;
                    case ((int) (android.content.ConfigurationProto.SCREEN_LAYOUT)) :
                        screenLayout = protoInputStream.readInt(android.content.ConfigurationProto.SCREEN_LAYOUT);
                        break;
                    case ((int) (android.content.ConfigurationProto.COLOR_MODE)) :
                        colorMode = protoInputStream.readInt(android.content.ConfigurationProto.COLOR_MODE);
                        break;
                    case ((int) (android.content.ConfigurationProto.TOUCHSCREEN)) :
                        touchscreen = protoInputStream.readInt(android.content.ConfigurationProto.TOUCHSCREEN);
                        break;
                    case ((int) (android.content.ConfigurationProto.KEYBOARD)) :
                        keyboard = protoInputStream.readInt(android.content.ConfigurationProto.KEYBOARD);
                        break;
                    case ((int) (android.content.ConfigurationProto.KEYBOARD_HIDDEN)) :
                        keyboardHidden = protoInputStream.readInt(android.content.ConfigurationProto.KEYBOARD_HIDDEN);
                        break;
                    case ((int) (android.content.ConfigurationProto.HARD_KEYBOARD_HIDDEN)) :
                        hardKeyboardHidden = protoInputStream.readInt(android.content.ConfigurationProto.HARD_KEYBOARD_HIDDEN);
                        break;
                    case ((int) (android.content.ConfigurationProto.NAVIGATION)) :
                        navigation = protoInputStream.readInt(android.content.ConfigurationProto.NAVIGATION);
                        break;
                    case ((int) (android.content.ConfigurationProto.NAVIGATION_HIDDEN)) :
                        navigationHidden = protoInputStream.readInt(android.content.ConfigurationProto.NAVIGATION_HIDDEN);
                        break;
                    case ((int) (android.content.ConfigurationProto.ORIENTATION)) :
                        orientation = protoInputStream.readInt(android.content.ConfigurationProto.ORIENTATION);
                        break;
                    case ((int) (android.content.ConfigurationProto.UI_MODE)) :
                        uiMode = protoInputStream.readInt(android.content.ConfigurationProto.UI_MODE);
                        break;
                    case ((int) (android.content.ConfigurationProto.SCREEN_WIDTH_DP)) :
                        screenWidthDp = protoInputStream.readInt(android.content.ConfigurationProto.SCREEN_WIDTH_DP);
                        break;
                    case ((int) (android.content.ConfigurationProto.SCREEN_HEIGHT_DP)) :
                        screenHeightDp = protoInputStream.readInt(android.content.ConfigurationProto.SCREEN_HEIGHT_DP);
                        break;
                    case ((int) (android.content.ConfigurationProto.SMALLEST_SCREEN_WIDTH_DP)) :
                        smallestScreenWidthDp = protoInputStream.readInt(android.content.ConfigurationProto.SMALLEST_SCREEN_WIDTH_DP);
                        break;
                    case ((int) (android.content.ConfigurationProto.DENSITY_DPI)) :
                        densityDpi = protoInputStream.readInt(android.content.ConfigurationProto.DENSITY_DPI);
                        break;
                    case ((int) (android.content.ConfigurationProto.WINDOW_CONFIGURATION)) :
                        windowConfiguration.readFromProto(protoInputStream, android.content.ConfigurationProto.WINDOW_CONFIGURATION);
                        break;
                }
            } 
        } finally {
            // Let caller handle any exceptions
            if (list.size() > 0) {
                // Create the LocaleList from the collected Locales
                setLocales(new android.os.LocaleList(list.toArray(new java.util.Locale[list.size()])));
            }
            protoInputStream.end(token);
        }
    }

    /**
     * Write full {@link android.content.ResourcesConfigurationProto} to protocol buffer output
     * stream.
     *
     * @param protoOutputStream
     * 		Stream to write the Configuration object to.
     * @param fieldId
     * 		Field Id of the Configuration as defined in the parent message
     * @param metrics
     * 		Current display information
     * @unknown 
     */
    public void writeResConfigToProto(android.util.proto.ProtoOutputStream protoOutputStream, long fieldId, android.util.DisplayMetrics metrics) {
        final int width;
        final int height;
        if (metrics.widthPixels >= metrics.heightPixels) {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        } else {
            // noinspection SuspiciousNameCombination
            width = metrics.heightPixels;
            // noinspection SuspiciousNameCombination
            height = metrics.widthPixels;
        }
        final long token = protoOutputStream.start(fieldId);
        writeToProto(protoOutputStream, android.content.ResourcesConfigurationProto.CONFIGURATION);
        protoOutputStream.write(android.content.ResourcesConfigurationProto.SDK_VERSION, Build.VERSION.RESOURCES_SDK_INT);
        protoOutputStream.write(android.content.ResourcesConfigurationProto.SCREEN_WIDTH_PX, width);
        protoOutputStream.write(android.content.ResourcesConfigurationProto.SCREEN_HEIGHT_PX, height);
        protoOutputStream.end(token);
    }

    /**
     * Convert the UI mode to a human readable format.
     *
     * @unknown 
     */
    public static java.lang.String uiModeToString(int uiMode) {
        switch (uiMode) {
            case android.content.res.Configuration.UI_MODE_TYPE_UNDEFINED :
                return "UI_MODE_TYPE_UNDEFINED";
            case android.content.res.Configuration.UI_MODE_TYPE_NORMAL :
                return "UI_MODE_TYPE_NORMAL";
            case android.content.res.Configuration.UI_MODE_TYPE_DESK :
                return "UI_MODE_TYPE_DESK";
            case android.content.res.Configuration.UI_MODE_TYPE_CAR :
                return "UI_MODE_TYPE_CAR";
            case android.content.res.Configuration.UI_MODE_TYPE_TELEVISION :
                return "UI_MODE_TYPE_TELEVISION";
            case android.content.res.Configuration.UI_MODE_TYPE_APPLIANCE :
                return "UI_MODE_TYPE_APPLIANCE";
            case android.content.res.Configuration.UI_MODE_TYPE_WATCH :
                return "UI_MODE_TYPE_WATCH";
            case android.content.res.Configuration.UI_MODE_TYPE_VR_HEADSET :
                return "UI_MODE_TYPE_VR_HEADSET";
            default :
                return java.lang.Integer.toString(uiMode);
        }
    }

    /**
     * Set this object to the system defaults.
     */
    public void setToDefaults() {
        fontScale = 1;
        mcc = mnc = 0;
        mLocaleList = android.os.LocaleList.getEmptyLocaleList();
        locale = null;
        userSetLocale = false;
        touchscreen = android.content.res.Configuration.TOUCHSCREEN_UNDEFINED;
        keyboard = android.content.res.Configuration.KEYBOARD_UNDEFINED;
        keyboardHidden = android.content.res.Configuration.KEYBOARDHIDDEN_UNDEFINED;
        hardKeyboardHidden = android.content.res.Configuration.HARDKEYBOARDHIDDEN_UNDEFINED;
        navigation = android.content.res.Configuration.NAVIGATION_UNDEFINED;
        navigationHidden = android.content.res.Configuration.NAVIGATIONHIDDEN_UNDEFINED;
        orientation = android.content.res.Configuration.ORIENTATION_UNDEFINED;
        screenLayout = android.content.res.Configuration.SCREENLAYOUT_UNDEFINED;
        colorMode = android.content.res.Configuration.COLOR_MODE_UNDEFINED;
        uiMode = android.content.res.Configuration.UI_MODE_TYPE_UNDEFINED;
        screenWidthDp = compatScreenWidthDp = android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED;
        screenHeightDp = compatScreenHeightDp = android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED;
        smallestScreenWidthDp = compatSmallestScreenWidthDp = android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED;
        densityDpi = android.content.res.Configuration.DENSITY_DPI_UNDEFINED;
        assetsSeq = android.content.res.Configuration.ASSETS_SEQ_UNDEFINED;
        seq = 0;
        windowConfiguration.setToDefaults();
    }

    /**
     * Set this object to completely undefined.
     *
     * @unknown 
     */
    public void unset() {
        setToDefaults();
        fontScale = 0;
    }

    /**
     * {@hide }
     */
    @android.annotation.UnsupportedAppUsage
    @java.lang.Deprecated
    public void makeDefault() {
        setToDefaults();
    }

    /**
     * Copies the fields from delta into this Configuration object, keeping
     * track of which ones have changed. Any undefined fields in {@code delta}
     * are ignored and not copied in to the current Configuration.
     *
     * @return a bit mask of the changed fields, as per {@link #diff}
     */
    @android.content.pm.ActivityInfo.Config
    public int updateFrom(@android.annotation.NonNull
    android.content.res.Configuration delta) {
        int changed = 0;
        if ((delta.fontScale > 0) && (fontScale != delta.fontScale)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_FONT_SCALE;
            fontScale = delta.fontScale;
        }
        if ((delta.mcc != 0) && (mcc != delta.mcc)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_MCC;
            mcc = delta.mcc;
        }
        if ((delta.mnc != 0) && (mnc != delta.mnc)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_MNC;
            mnc = delta.mnc;
        }
        fixUpLocaleList();
        delta.fixUpLocaleList();
        if ((!delta.mLocaleList.isEmpty()) && (!mLocaleList.equals(delta.mLocaleList))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_LOCALE;
            mLocaleList = delta.mLocaleList;
            // delta.locale can't be null, since delta.mLocaleList is not empty.
            if (!delta.locale.equals(locale)) {
                locale = ((java.util.Locale) (delta.locale.clone()));
                // If locale has changed, then layout direction is also changed ...
                changed |= android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION;
                // ... and we need to update the layout direction (represented by the first
                // 2 most significant bits in screenLayout).
                setLayoutDirection(locale);
            }
        }
        final int deltaScreenLayoutDir = delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK;
        if ((deltaScreenLayoutDir != android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_UNDEFINED) && (deltaScreenLayoutDir != (screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK))) {
            screenLayout = (screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK)) | deltaScreenLayoutDir;
            changed |= android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION;
        }
        if (delta.userSetLocale && ((!userSetLocale) || ((changed & android.content.pm.ActivityInfo.CONFIG_LOCALE) != 0))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_LOCALE;
            userSetLocale = true;
        }
        if ((delta.touchscreen != android.content.res.Configuration.TOUCHSCREEN_UNDEFINED) && (touchscreen != delta.touchscreen)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_TOUCHSCREEN;
            touchscreen = delta.touchscreen;
        }
        if ((delta.keyboard != android.content.res.Configuration.KEYBOARD_UNDEFINED) && (keyboard != delta.keyboard)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD;
            keyboard = delta.keyboard;
        }
        if ((delta.keyboardHidden != android.content.res.Configuration.KEYBOARDHIDDEN_UNDEFINED) && (keyboardHidden != delta.keyboardHidden)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
            keyboardHidden = delta.keyboardHidden;
        }
        if ((delta.hardKeyboardHidden != android.content.res.Configuration.HARDKEYBOARDHIDDEN_UNDEFINED) && (hardKeyboardHidden != delta.hardKeyboardHidden)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
            hardKeyboardHidden = delta.hardKeyboardHidden;
        }
        if ((delta.navigation != android.content.res.Configuration.NAVIGATION_UNDEFINED) && (navigation != delta.navigation)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_NAVIGATION;
            navigation = delta.navigation;
        }
        if ((delta.navigationHidden != android.content.res.Configuration.NAVIGATIONHIDDEN_UNDEFINED) && (navigationHidden != delta.navigationHidden)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
            navigationHidden = delta.navigationHidden;
        }
        if ((delta.orientation != android.content.res.Configuration.ORIENTATION_UNDEFINED) && (orientation != delta.orientation)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_ORIENTATION;
            orientation = delta.orientation;
        }
        if (((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) != android.content.res.Configuration.SCREENLAYOUT_SIZE_UNDEFINED) && ((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) != (screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT;
            screenLayout = (screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) | (delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK);
        }
        if (((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK) != android.content.res.Configuration.SCREENLAYOUT_LONG_UNDEFINED) && ((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK) != (screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT;
            screenLayout = (screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_LONG_MASK)) | (delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK);
        }
        if (((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK) != android.content.res.Configuration.SCREENLAYOUT_ROUND_UNDEFINED) && ((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK) != (screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT;
            screenLayout = (screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK)) | (delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK);
        }
        if (((delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED) != (screenLayout & android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED)) && (delta.screenLayout != 0)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT;
            screenLayout = (screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED)) | (delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_COMPAT_NEEDED);
        }
        if (((delta.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) != android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED) && ((delta.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) != (colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_COLOR_MODE;
            colorMode = (colorMode & (~android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK)) | (delta.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK);
        }
        if (((delta.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) != android.content.res.Configuration.COLOR_MODE_HDR_UNDEFINED) && ((delta.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) != (colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_COLOR_MODE;
            colorMode = (colorMode & (~android.content.res.Configuration.COLOR_MODE_HDR_MASK)) | (delta.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK);
        }
        if ((delta.uiMode != (android.content.res.Configuration.UI_MODE_TYPE_UNDEFINED | android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED)) && (uiMode != delta.uiMode)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_UI_MODE;
            if ((delta.uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK) != android.content.res.Configuration.UI_MODE_TYPE_UNDEFINED) {
                uiMode = (uiMode & (~android.content.res.Configuration.UI_MODE_TYPE_MASK)) | (delta.uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK);
            }
            if ((delta.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) != android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED) {
                uiMode = (uiMode & (~android.content.res.Configuration.UI_MODE_NIGHT_MASK)) | (delta.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK);
            }
        }
        if ((delta.screenWidthDp != android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED) && (screenWidthDp != delta.screenWidthDp)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE;
            screenWidthDp = delta.screenWidthDp;
        }
        if ((delta.screenHeightDp != android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED) && (screenHeightDp != delta.screenHeightDp)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE;
            screenHeightDp = delta.screenHeightDp;
        }
        if ((delta.smallestScreenWidthDp != android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) && (smallestScreenWidthDp != delta.smallestScreenWidthDp)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE;
            smallestScreenWidthDp = delta.smallestScreenWidthDp;
        }
        if ((delta.densityDpi != android.content.res.Configuration.DENSITY_DPI_UNDEFINED) && (densityDpi != delta.densityDpi)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_DENSITY;
            densityDpi = delta.densityDpi;
        }
        if (delta.compatScreenWidthDp != android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
            compatScreenWidthDp = delta.compatScreenWidthDp;
        }
        if (delta.compatScreenHeightDp != android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
            compatScreenHeightDp = delta.compatScreenHeightDp;
        }
        if (delta.compatSmallestScreenWidthDp != android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
            compatSmallestScreenWidthDp = delta.compatSmallestScreenWidthDp;
        }
        if ((delta.assetsSeq != android.content.res.Configuration.ASSETS_SEQ_UNDEFINED) && (delta.assetsSeq != assetsSeq)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_ASSETS_PATHS;
            assetsSeq = delta.assetsSeq;
        }
        if (delta.seq != 0) {
            seq = delta.seq;
        }
        if (windowConfiguration.updateFrom(delta.windowConfiguration) != 0) {
            changed |= android.content.pm.ActivityInfo.CONFIG_WINDOW_CONFIGURATION;
        }
        return changed;
    }

    /**
     * Return a bit mask of the differences between this Configuration
     * object and the given one.  Does not change the values of either.  Any
     * undefined fields in <var>delta</var> are ignored.
     *
     * @return Returns a bit mask indicating which configuration
    values has changed, containing any combination of
    {@link android.content.pm.ActivityInfo#CONFIG_FONT_SCALE
    PackageManager.ActivityInfo.CONFIG_FONT_SCALE},
    {@link android.content.pm.ActivityInfo#CONFIG_MCC
    PackageManager.ActivityInfo.CONFIG_MCC},
    {@link android.content.pm.ActivityInfo#CONFIG_MNC
    PackageManager.ActivityInfo.CONFIG_MNC},
    {@link android.content.pm.ActivityInfo#CONFIG_LOCALE
    PackageManager.ActivityInfo.CONFIG_LOCALE},
    {@link android.content.pm.ActivityInfo#CONFIG_TOUCHSCREEN
    PackageManager.ActivityInfo.CONFIG_TOUCHSCREEN},
    {@link android.content.pm.ActivityInfo#CONFIG_KEYBOARD
    PackageManager.ActivityInfo.CONFIG_KEYBOARD},
    {@link android.content.pm.ActivityInfo#CONFIG_NAVIGATION
    PackageManager.ActivityInfo.CONFIG_NAVIGATION},
    {@link android.content.pm.ActivityInfo#CONFIG_ORIENTATION
    PackageManager.ActivityInfo.CONFIG_ORIENTATION},
    {@link android.content.pm.ActivityInfo#CONFIG_SCREEN_LAYOUT
    PackageManager.ActivityInfo.CONFIG_SCREEN_LAYOUT}, or
    {@link android.content.pm.ActivityInfo#CONFIG_SCREEN_SIZE
    PackageManager.ActivityInfo.CONFIG_SCREEN_SIZE}, or
    {@link android.content.pm.ActivityInfo#CONFIG_SMALLEST_SCREEN_SIZE
    PackageManager.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE}.
    {@link android.content.pm.ActivityInfo#CONFIG_LAYOUT_DIRECTION
    PackageManager.ActivityInfo.CONFIG_LAYOUT_DIRECTION}.
     */
    public int diff(android.content.res.Configuration delta) {
        return /* compareUndefined */
        /* publicOnly */
        diff(delta, false, false);
    }

    /**
     * Returns the diff against the provided {@link Configuration} excluding values that would
     * publicly be equivalent, such as appBounds.
     *
     * @param delta
     * 		{@link Configuration} to compare to.
     * 		
     * 		TODO(b/36812336): Remove once appBounds has been moved out of Configuration.
     * 		{@hide }
     */
    public int diffPublicOnly(android.content.res.Configuration delta) {
        return /* compareUndefined */
        /* publicOnly */
        diff(delta, false, true);
    }

    /**
     * Variation of {@link #diff(Configuration)} with an option to skip checks for undefined values.
     *
     * @unknown 
     */
    public int diff(android.content.res.Configuration delta, boolean compareUndefined, boolean publicOnly) {
        int changed = 0;
        if ((compareUndefined || (delta.fontScale > 0)) && (fontScale != delta.fontScale)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_FONT_SCALE;
        }
        if ((compareUndefined || (delta.mcc != 0)) && (mcc != delta.mcc)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_MCC;
        }
        if ((compareUndefined || (delta.mnc != 0)) && (mnc != delta.mnc)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_MNC;
        }
        fixUpLocaleList();
        delta.fixUpLocaleList();
        if ((compareUndefined || (!delta.mLocaleList.isEmpty())) && (!mLocaleList.equals(delta.mLocaleList))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_LOCALE;
            changed |= android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION;
        }
        final int deltaScreenLayoutDir = delta.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK;
        if ((compareUndefined || (deltaScreenLayoutDir != android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_UNDEFINED)) && (deltaScreenLayoutDir != (screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION;
        }
        if ((compareUndefined || (delta.touchscreen != android.content.res.Configuration.TOUCHSCREEN_UNDEFINED)) && (touchscreen != delta.touchscreen)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_TOUCHSCREEN;
        }
        if ((compareUndefined || (delta.keyboard != android.content.res.Configuration.KEYBOARD_UNDEFINED)) && (keyboard != delta.keyboard)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD;
        }
        if ((compareUndefined || (delta.keyboardHidden != android.content.res.Configuration.KEYBOARDHIDDEN_UNDEFINED)) && (keyboardHidden != delta.keyboardHidden)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
        }
        if ((compareUndefined || (delta.hardKeyboardHidden != android.content.res.Configuration.HARDKEYBOARDHIDDEN_UNDEFINED)) && (hardKeyboardHidden != delta.hardKeyboardHidden)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
        }
        if ((compareUndefined || (delta.navigation != android.content.res.Configuration.NAVIGATION_UNDEFINED)) && (navigation != delta.navigation)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_NAVIGATION;
        }
        if ((compareUndefined || (delta.navigationHidden != android.content.res.Configuration.NAVIGATIONHIDDEN_UNDEFINED)) && (navigationHidden != delta.navigationHidden)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
        }
        if ((compareUndefined || (delta.orientation != android.content.res.Configuration.ORIENTATION_UNDEFINED)) && (orientation != delta.orientation)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_ORIENTATION;
        }
        if ((compareUndefined || (android.content.res.Configuration.getScreenLayoutNoDirection(delta.screenLayout) != (android.content.res.Configuration.SCREENLAYOUT_SIZE_UNDEFINED | android.content.res.Configuration.SCREENLAYOUT_LONG_UNDEFINED))) && (android.content.res.Configuration.getScreenLayoutNoDirection(screenLayout) != android.content.res.Configuration.getScreenLayoutNoDirection(delta.screenLayout))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT;
        }
        if ((compareUndefined || ((delta.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) != android.content.res.Configuration.COLOR_MODE_HDR_UNDEFINED)) && ((colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) != (delta.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_COLOR_MODE;
        }
        if ((compareUndefined || ((delta.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) != android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED)) && ((colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) != (delta.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK))) {
            changed |= android.content.pm.ActivityInfo.CONFIG_COLOR_MODE;
        }
        if ((compareUndefined || (delta.uiMode != (android.content.res.Configuration.UI_MODE_TYPE_UNDEFINED | android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED))) && (uiMode != delta.uiMode)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_UI_MODE;
        }
        if ((compareUndefined || (delta.screenWidthDp != android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED)) && (screenWidthDp != delta.screenWidthDp)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE;
        }
        if ((compareUndefined || (delta.screenHeightDp != android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED)) && (screenHeightDp != delta.screenHeightDp)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE;
        }
        if ((compareUndefined || (delta.smallestScreenWidthDp != android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED)) && (smallestScreenWidthDp != delta.smallestScreenWidthDp)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE;
        }
        if ((compareUndefined || (delta.densityDpi != android.content.res.Configuration.DENSITY_DPI_UNDEFINED)) && (densityDpi != delta.densityDpi)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_DENSITY;
        }
        if ((compareUndefined || (delta.assetsSeq != android.content.res.Configuration.ASSETS_SEQ_UNDEFINED)) && (assetsSeq != delta.assetsSeq)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_ASSETS_PATHS;
        }
        // WindowConfiguration differences aren't considered public...
        if ((!publicOnly) && (windowConfiguration.diff(delta.windowConfiguration, compareUndefined) != 0)) {
            changed |= android.content.pm.ActivityInfo.CONFIG_WINDOW_CONFIGURATION;
        }
        return changed;
    }

    /**
     * Determines if a new resource needs to be loaded from the bit set of
     * configuration changes returned by {@link #updateFrom(Configuration)}.
     *
     * @param configChanges
     * 		the mask of changes configurations as returned by
     * 		{@link #updateFrom(Configuration)}
     * @param interestingChanges
     * 		the configuration changes that the resource
     * 		can handle as given in
     * 		{@link android.util.TypedValue#changingConfigurations}
     * @return {@code true} if the resource needs to be loaded, {@code false}
    otherwise
     */
    public static boolean needNewResources(@android.content.pm.ActivityInfo.Config
    int configChanges, @android.content.pm.ActivityInfo.Config
    int interestingChanges) {
        // CONFIG_ASSETS_PATHS and CONFIG_FONT_SCALE are higher level configuration changes that
        // all resources are subject to change with.
        interestingChanges = (interestingChanges | android.content.pm.ActivityInfo.CONFIG_ASSETS_PATHS) | android.content.pm.ActivityInfo.CONFIG_FONT_SCALE;
        return (configChanges & interestingChanges) != 0;
    }

    /**
     *
     *
     * @unknown Return true if the sequence of 'other' is better than this.  Assumes
    that 'this' is your current sequence and 'other' is a new one you have
    received some how and want to compare with what you have.
     */
    public boolean isOtherSeqNewer(android.content.res.Configuration other) {
        if (other == null) {
            // Sanity check.
            return false;
        }
        if (other.seq == 0) {
            // If the other sequence is not specified, then we must assume
            // it is newer since we don't know any better.
            return true;
        }
        if (seq == 0) {
            // If this sequence is not specified, then we also consider the
            // other is better.  Yes we have a preference for other.  Sue us.
            return true;
        }
        int diff = other.seq - seq;
        if (diff > 0x10000) {
            // If there has been a sufficiently large jump, assume the
            // sequence has wrapped around.
            return false;
        }
        return diff > 0;
    }

    /**
     * Parcelable methods
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeFloat(fontScale);
        dest.writeInt(mcc);
        dest.writeInt(mnc);
        fixUpLocaleList();
        dest.writeParcelable(mLocaleList, flags);
        if (userSetLocale) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(touchscreen);
        dest.writeInt(keyboard);
        dest.writeInt(keyboardHidden);
        dest.writeInt(hardKeyboardHidden);
        dest.writeInt(navigation);
        dest.writeInt(navigationHidden);
        dest.writeInt(orientation);
        dest.writeInt(screenLayout);
        dest.writeInt(colorMode);
        dest.writeInt(uiMode);
        dest.writeInt(screenWidthDp);
        dest.writeInt(screenHeightDp);
        dest.writeInt(smallestScreenWidthDp);
        dest.writeInt(densityDpi);
        dest.writeInt(compatScreenWidthDp);
        dest.writeInt(compatScreenHeightDp);
        dest.writeInt(compatSmallestScreenWidthDp);
        dest.writeValue(windowConfiguration);
        dest.writeInt(assetsSeq);
        dest.writeInt(seq);
    }

    public void readFromParcel(android.os.Parcel source) {
        fontScale = source.readFloat();
        mcc = source.readInt();
        mnc = source.readInt();
        mLocaleList = source.readParcelable(android.os.LocaleList.class.getClassLoader());
        locale = mLocaleList.get(0);
        userSetLocale = source.readInt() == 1;
        touchscreen = source.readInt();
        keyboard = source.readInt();
        keyboardHidden = source.readInt();
        hardKeyboardHidden = source.readInt();
        navigation = source.readInt();
        navigationHidden = source.readInt();
        orientation = source.readInt();
        screenLayout = source.readInt();
        colorMode = source.readInt();
        uiMode = source.readInt();
        screenWidthDp = source.readInt();
        screenHeightDp = source.readInt();
        smallestScreenWidthDp = source.readInt();
        densityDpi = source.readInt();
        compatScreenWidthDp = source.readInt();
        compatScreenHeightDp = source.readInt();
        compatSmallestScreenWidthDp = source.readInt();
        windowConfiguration.setTo(((android.app.WindowConfiguration) (source.readValue(null))));
        assetsSeq = source.readInt();
        seq = source.readInt();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.res.Configuration> CREATOR = new android.os.Parcelable.Creator<android.content.res.Configuration>() {
        public android.content.res.Configuration createFromParcel(android.os.Parcel source) {
            return new android.content.res.Configuration(source);
        }

        public android.content.res.Configuration[] newArray(int size) {
            return new android.content.res.Configuration[size];
        }
    };

    /**
     * Construct this Configuration object, reading from the Parcel.
     */
    private Configuration(android.os.Parcel source) {
        readFromParcel(source);
    }

    public int compareTo(android.content.res.Configuration that) {
        int n;
        float a = this.fontScale;
        float b = that.fontScale;
        if (a < b)
            return -1;

        if (a > b)
            return 1;

        n = this.mcc - that.mcc;
        if (n != 0)
            return n;

        n = this.mnc - that.mnc;
        if (n != 0)
            return n;

        fixUpLocaleList();
        that.fixUpLocaleList();
        // for backward compatibility, we consider an empty locale list to be greater
        // than any non-empty locale list.
        if (this.mLocaleList.isEmpty()) {
            if (!that.mLocaleList.isEmpty())
                return 1;

        } else
            if (that.mLocaleList.isEmpty()) {
                return -1;
            } else {
                final int minSize = java.lang.Math.min(this.mLocaleList.size(), that.mLocaleList.size());
                for (int i = 0; i < minSize; ++i) {
                    final java.util.Locale thisLocale = this.mLocaleList.get(i);
                    final java.util.Locale thatLocale = that.mLocaleList.get(i);
                    n = thisLocale.getLanguage().compareTo(thatLocale.getLanguage());
                    if (n != 0)
                        return n;

                    n = thisLocale.getCountry().compareTo(thatLocale.getCountry());
                    if (n != 0)
                        return n;

                    n = thisLocale.getVariant().compareTo(thatLocale.getVariant());
                    if (n != 0)
                        return n;

                    n = thisLocale.toLanguageTag().compareTo(thatLocale.toLanguageTag());
                    if (n != 0)
                        return n;

                }
                n = this.mLocaleList.size() - that.mLocaleList.size();
                if (n != 0)
                    return n;

            }

        n = this.touchscreen - that.touchscreen;
        if (n != 0)
            return n;

        n = this.keyboard - that.keyboard;
        if (n != 0)
            return n;

        n = this.keyboardHidden - that.keyboardHidden;
        if (n != 0)
            return n;

        n = this.hardKeyboardHidden - that.hardKeyboardHidden;
        if (n != 0)
            return n;

        n = this.navigation - that.navigation;
        if (n != 0)
            return n;

        n = this.navigationHidden - that.navigationHidden;
        if (n != 0)
            return n;

        n = this.orientation - that.orientation;
        if (n != 0)
            return n;

        n = this.colorMode - that.colorMode;
        if (n != 0)
            return n;

        n = this.screenLayout - that.screenLayout;
        if (n != 0)
            return n;

        n = this.uiMode - that.uiMode;
        if (n != 0)
            return n;

        n = this.screenWidthDp - that.screenWidthDp;
        if (n != 0)
            return n;

        n = this.screenHeightDp - that.screenHeightDp;
        if (n != 0)
            return n;

        n = this.smallestScreenWidthDp - that.smallestScreenWidthDp;
        if (n != 0)
            return n;

        n = this.densityDpi - that.densityDpi;
        if (n != 0)
            return n;

        n = this.assetsSeq - that.assetsSeq;
        if (n != 0)
            return n;

        n = windowConfiguration.compareTo(that.windowConfiguration);
        if (n != 0)
            return n;

        // if (n != 0) return n;
        return n;
    }

    public boolean equals(android.content.res.Configuration that) {
        if (that == null)
            return false;

        if (that == this)
            return true;

        return this.compareTo(that) == 0;
    }

    public boolean equals(java.lang.Object that) {
        try {
            return equals(((android.content.res.Configuration) (that)));
        } catch (java.lang.ClassCastException e) {
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        result = (31 * result) + java.lang.Float.floatToIntBits(fontScale);
        result = (31 * result) + mcc;
        result = (31 * result) + mnc;
        result = (31 * result) + mLocaleList.hashCode();
        result = (31 * result) + touchscreen;
        result = (31 * result) + keyboard;
        result = (31 * result) + keyboardHidden;
        result = (31 * result) + hardKeyboardHidden;
        result = (31 * result) + navigation;
        result = (31 * result) + navigationHidden;
        result = (31 * result) + orientation;
        result = (31 * result) + screenLayout;
        result = (31 * result) + colorMode;
        result = (31 * result) + uiMode;
        result = (31 * result) + screenWidthDp;
        result = (31 * result) + screenHeightDp;
        result = (31 * result) + smallestScreenWidthDp;
        result = (31 * result) + densityDpi;
        result = (31 * result) + assetsSeq;
        return result;
    }

    /**
     * Get the locale list. This is the preferred way for getting the locales (instead of using
     * the direct accessor to {@link #locale}, which would only provide the primary locale).
     *
     * @return The locale list.
     */
    @android.annotation.NonNull
    public android.os.LocaleList getLocales() {
        fixUpLocaleList();
        return mLocaleList;
    }

    /**
     * Set the locale list. This is the preferred way for setting up the locales (instead of using
     * the direct accessor or {@link #setLocale(Locale)}). This will also set the layout direction
     * according to the first locale in the list.
     *
     * Note that the layout direction will always come from the first locale in the locale list,
     * even if the locale is not supported by the resources (the resources may only support
     * another locale further down the list which has a different direction).
     *
     * @param locales
     * 		The locale list. If null, an empty LocaleList will be assigned.
     */
    public void setLocales(@android.annotation.Nullable
    android.os.LocaleList locales) {
        mLocaleList = (locales == null) ? android.os.LocaleList.getEmptyLocaleList() : locales;
        locale = mLocaleList.get(0);
        setLayoutDirection(locale);
    }

    /**
     * Set the locale list to a list of just one locale. This will also set the layout direction
     * according to the locale.
     *
     * Note that after this is run, calling <code>.equals()</code> on the input locale and the
     * {@link #locale} attribute would return <code>true</code> if they are not null, but there is
     * no guarantee that they would be the same object.
     *
     * See also the note about layout direction in {@link #setLocales(LocaleList)}.
     *
     * @param loc
     * 		The locale. Can be null.
     */
    public void setLocale(@android.annotation.Nullable
    java.util.Locale loc) {
        setLocales(loc == null ? android.os.LocaleList.getEmptyLocaleList() : new android.os.LocaleList(loc));
    }

    /**
     *
     *
     * @unknown Clears the locale without changing layout direction.
     */
    public void clearLocales() {
        mLocaleList = android.os.LocaleList.getEmptyLocaleList();
        locale = null;
    }

    /**
     * Return the layout direction. Will be either {@link View#LAYOUT_DIRECTION_LTR} or
     * {@link View#LAYOUT_DIRECTION_RTL}.
     *
     * @return Returns {@link View#LAYOUT_DIRECTION_RTL} if the configuration
    is {@link #SCREENLAYOUT_LAYOUTDIR_RTL}, otherwise {@link View#LAYOUT_DIRECTION_LTR}.
     */
    public int getLayoutDirection() {
        return (screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK) == android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_RTL ? android.view.View.LAYOUT_DIRECTION_RTL : android.view.View.LAYOUT_DIRECTION_LTR;
    }

    /**
     * Set the layout direction from a Locale.
     *
     * @param loc
     * 		The Locale. If null will set the layout direction to
     * 		{@link View#LAYOUT_DIRECTION_LTR}. If not null will set it to the layout direction
     * 		corresponding to the Locale.
     * @see View#LAYOUT_DIRECTION_LTR
     * @see View#LAYOUT_DIRECTION_RTL
     */
    public void setLayoutDirection(java.util.Locale loc) {
        // There is a "1" difference between the configuration values for
        // layout direction and View constants for layout direction, just add "1".
        final int layoutDirection = 1 + android.text.TextUtils.getLayoutDirectionFromLocale(loc);
        screenLayout = (screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK)) | (layoutDirection << android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_SHIFT);
    }

    private static int getScreenLayoutNoDirection(int screenLayout) {
        return screenLayout & (~android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK);
    }

    /**
     * Return whether the screen has a round shape. Apps may choose to change styling based
     * on this property, such as the alignment or layout of text or informational icons.
     *
     * @return true if the screen is rounded, false otherwise
     */
    public boolean isScreenRound() {
        return (screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK) == android.content.res.Configuration.SCREENLAYOUT_ROUND_YES;
    }

    /**
     * Return whether the screen has a wide color gamut and wide color gamut rendering
     * is supported by this device.
     *
     * When true, it implies the screen is colorspace aware but not
     * necessarily color-managed. The final colors may still be changed by the
     * screen depending on user settings.
     *
     * @return true if the screen has a wide color gamut and wide color gamut rendering
    is supported, false otherwise
     */
    public boolean isScreenWideColorGamut() {
        return (colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) == android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_YES;
    }

    /**
     * Return whether the screen has a high dynamic range.
     *
     * @return true if the screen has a high dynamic range, false otherwise
     */
    public boolean isScreenHdr() {
        return (colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) == android.content.res.Configuration.COLOR_MODE_HDR_YES;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String localesToResourceQualifier(android.os.LocaleList locs) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < locs.size(); i++) {
            final java.util.Locale loc = locs.get(i);
            final int l = loc.getLanguage().length();
            if (l == 0) {
                continue;
            }
            final int s = loc.getScript().length();
            final int c = loc.getCountry().length();
            final int v = loc.getVariant().length();
            // We ignore locale extensions, since they are not supported by AAPT
            if (sb.length() != 0) {
                sb.append(",");
            }
            if ((((l == 2) && (s == 0)) && ((c == 0) || (c == 2))) && (v == 0)) {
                // Traditional locale format: xx or xx-rYY
                sb.append(loc.getLanguage());
                if (c == 2) {
                    sb.append("-r").append(loc.getCountry());
                }
            } else {
                sb.append("b+");
                sb.append(loc.getLanguage());
                if (s != 0) {
                    sb.append("+");
                    sb.append(loc.getScript());
                }
                if (c != 0) {
                    sb.append("+");
                    sb.append(loc.getCountry());
                }
                if (v != 0) {
                    sb.append("+");
                    sb.append(loc.getVariant());
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the configuration that can be parsed
     * by build tools (like AAPT), without display metrics included
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static java.lang.String resourceQualifierString(android.content.res.Configuration config) {
        return android.content.res.Configuration.resourceQualifierString(config, null);
    }

    /**
     * Returns a string representation of the configuration that can be parsed
     * by build tools (like AAPT).
     *
     * @unknown 
     */
    public static java.lang.String resourceQualifierString(android.content.res.Configuration config, android.util.DisplayMetrics metrics) {
        java.util.ArrayList<java.lang.String> parts = new java.util.ArrayList<java.lang.String>();
        if (config.mcc != 0) {
            parts.add("mcc" + config.mcc);
            if (config.mnc != 0) {
                parts.add("mnc" + config.mnc);
            }
        }
        if (!config.mLocaleList.isEmpty()) {
            final java.lang.String resourceQualifier = android.content.res.Configuration.localesToResourceQualifier(config.mLocaleList);
            if (!resourceQualifier.isEmpty()) {
                parts.add(resourceQualifier);
            }
        }
        switch (config.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK) {
            case android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_LTR :
                parts.add("ldltr");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_RTL :
                parts.add("ldrtl");
                break;
            default :
                break;
        }
        if (config.smallestScreenWidthDp != 0) {
            parts.add(("sw" + config.smallestScreenWidthDp) + "dp");
        }
        if (config.screenWidthDp != 0) {
            parts.add(("w" + config.screenWidthDp) + "dp");
        }
        if (config.screenHeightDp != 0) {
            parts.add(("h" + config.screenHeightDp) + "dp");
        }
        switch (config.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) {
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL :
                parts.add("small");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL :
                parts.add("normal");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE :
                parts.add("large");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE :
                parts.add("xlarge");
                break;
            default :
                break;
        }
        switch (config.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK) {
            case android.content.res.Configuration.SCREENLAYOUT_LONG_YES :
                parts.add("long");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_LONG_NO :
                parts.add("notlong");
                break;
            default :
                break;
        }
        switch (config.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK) {
            case android.content.res.Configuration.SCREENLAYOUT_ROUND_YES :
                parts.add("round");
                break;
            case android.content.res.Configuration.SCREENLAYOUT_ROUND_NO :
                parts.add("notround");
                break;
            default :
                break;
        }
        switch (config.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) {
            case android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_YES :
                parts.add("widecg");
                break;
            case android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_NO :
                parts.add("nowidecg");
                break;
            default :
                break;
        }
        switch (config.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) {
            case android.content.res.Configuration.COLOR_MODE_HDR_YES :
                parts.add("highdr");
                break;
            case android.content.res.Configuration.COLOR_MODE_HDR_NO :
                parts.add("lowdr");
                break;
            default :
                break;
        }
        switch (config.orientation) {
            case android.content.res.Configuration.ORIENTATION_LANDSCAPE :
                parts.add("land");
                break;
            case android.content.res.Configuration.ORIENTATION_PORTRAIT :
                parts.add("port");
                break;
            default :
                break;
        }
        switch (config.uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK) {
            case android.content.res.Configuration.UI_MODE_TYPE_APPLIANCE :
                parts.add("appliance");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_DESK :
                parts.add("desk");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_TELEVISION :
                parts.add("television");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_CAR :
                parts.add("car");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_WATCH :
                parts.add("watch");
                break;
            case android.content.res.Configuration.UI_MODE_TYPE_VR_HEADSET :
                parts.add("vrheadset");
                break;
            default :
                break;
        }
        switch (config.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            case android.content.res.Configuration.UI_MODE_NIGHT_YES :
                parts.add("night");
                break;
            case android.content.res.Configuration.UI_MODE_NIGHT_NO :
                parts.add("notnight");
                break;
            default :
                break;
        }
        switch (config.densityDpi) {
            case android.content.res.Configuration.DENSITY_DPI_UNDEFINED :
                break;
            case 120 :
                parts.add("ldpi");
                break;
            case 160 :
                parts.add("mdpi");
                break;
            case 213 :
                parts.add("tvdpi");
                break;
            case 240 :
                parts.add("hdpi");
                break;
            case 320 :
                parts.add("xhdpi");
                break;
            case 480 :
                parts.add("xxhdpi");
                break;
            case 640 :
                parts.add("xxxhdpi");
                break;
            case android.content.res.Configuration.DENSITY_DPI_ANY :
                parts.add("anydpi");
                break;
            case android.content.res.Configuration.DENSITY_DPI_NONE :
                parts.add("nodpi");
                break;
            default :
                parts.add(config.densityDpi + "dpi");
                break;
        }
        switch (config.touchscreen) {
            case android.content.res.Configuration.TOUCHSCREEN_NOTOUCH :
                parts.add("notouch");
                break;
            case android.content.res.Configuration.TOUCHSCREEN_FINGER :
                parts.add("finger");
                break;
            default :
                break;
        }
        switch (config.keyboardHidden) {
            case android.content.res.Configuration.KEYBOARDHIDDEN_NO :
                parts.add("keysexposed");
                break;
            case android.content.res.Configuration.KEYBOARDHIDDEN_YES :
                parts.add("keyshidden");
                break;
            case android.content.res.Configuration.KEYBOARDHIDDEN_SOFT :
                parts.add("keyssoft");
                break;
            default :
                break;
        }
        switch (config.keyboard) {
            case android.content.res.Configuration.KEYBOARD_NOKEYS :
                parts.add("nokeys");
                break;
            case android.content.res.Configuration.KEYBOARD_QWERTY :
                parts.add("qwerty");
                break;
            case android.content.res.Configuration.KEYBOARD_12KEY :
                parts.add("12key");
                break;
            default :
                break;
        }
        switch (config.navigationHidden) {
            case android.content.res.Configuration.NAVIGATIONHIDDEN_NO :
                parts.add("navexposed");
                break;
            case android.content.res.Configuration.NAVIGATIONHIDDEN_YES :
                parts.add("navhidden");
                break;
            default :
                break;
        }
        switch (config.navigation) {
            case android.content.res.Configuration.NAVIGATION_NONAV :
                parts.add("nonav");
                break;
            case android.content.res.Configuration.NAVIGATION_DPAD :
                parts.add("dpad");
                break;
            case android.content.res.Configuration.NAVIGATION_TRACKBALL :
                parts.add("trackball");
                break;
            case android.content.res.Configuration.NAVIGATION_WHEEL :
                parts.add("wheel");
                break;
            default :
                break;
        }
        if (metrics != null) {
            final int width;
            final int height;
            if (metrics.widthPixels >= metrics.heightPixels) {
                width = metrics.widthPixels;
                height = metrics.heightPixels;
            } else {
                // noinspection SuspiciousNameCombination
                width = metrics.heightPixels;
                // noinspection SuspiciousNameCombination
                height = metrics.widthPixels;
            }
            parts.add((width + "x") + height);
        }
        parts.add("v" + Build.VERSION.RESOURCES_SDK_INT);
        return android.text.TextUtils.join("-", parts);
    }

    /**
     * Generate a delta Configuration between <code>base</code> and <code>change</code>. The
     * resulting delta can be used with {@link #updateFrom(Configuration)}.
     * <p />
     * Caveat: If the any of the Configuration's members becomes undefined, then
     * {@link #updateFrom(Configuration)} will treat it as a no-op and not update that member.
     *
     * This is fine for device configurations as no member is ever undefined.
     * {@hide }
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.res.Configuration generateDelta(android.content.res.Configuration base, android.content.res.Configuration change) {
        final android.content.res.Configuration delta = new android.content.res.Configuration();
        if (base.fontScale != change.fontScale) {
            delta.fontScale = change.fontScale;
        }
        if (base.mcc != change.mcc) {
            delta.mcc = change.mcc;
        }
        if (base.mnc != change.mnc) {
            delta.mnc = change.mnc;
        }
        base.fixUpLocaleList();
        change.fixUpLocaleList();
        if (!base.mLocaleList.equals(change.mLocaleList)) {
            delta.mLocaleList = change.mLocaleList;
            delta.locale = change.locale;
        }
        if (base.touchscreen != change.touchscreen) {
            delta.touchscreen = change.touchscreen;
        }
        if (base.keyboard != change.keyboard) {
            delta.keyboard = change.keyboard;
        }
        if (base.keyboardHidden != change.keyboardHidden) {
            delta.keyboardHidden = change.keyboardHidden;
        }
        if (base.navigation != change.navigation) {
            delta.navigation = change.navigation;
        }
        if (base.navigationHidden != change.navigationHidden) {
            delta.navigationHidden = change.navigationHidden;
        }
        if (base.orientation != change.orientation) {
            delta.orientation = change.orientation;
        }
        if ((base.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) != (change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) {
            delta.screenLayout |= change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
        }
        if ((base.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK) != (change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK)) {
            delta.screenLayout |= change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LAYOUTDIR_MASK;
        }
        if ((base.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK) != (change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK)) {
            delta.screenLayout |= change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_LONG_MASK;
        }
        if ((base.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK) != (change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK)) {
            delta.screenLayout |= change.screenLayout & android.content.res.Configuration.SCREENLAYOUT_ROUND_MASK;
        }
        if ((base.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK) != (change.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK)) {
            delta.colorMode |= change.colorMode & android.content.res.Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_MASK;
        }
        if ((base.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK) != (change.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK)) {
            delta.colorMode |= change.colorMode & android.content.res.Configuration.COLOR_MODE_HDR_MASK;
        }
        if ((base.uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK) != (change.uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK)) {
            delta.uiMode |= change.uiMode & android.content.res.Configuration.UI_MODE_TYPE_MASK;
        }
        if ((base.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) != (change.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)) {
            delta.uiMode |= change.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        }
        if (base.screenWidthDp != change.screenWidthDp) {
            delta.screenWidthDp = change.screenWidthDp;
        }
        if (base.screenHeightDp != change.screenHeightDp) {
            delta.screenHeightDp = change.screenHeightDp;
        }
        if (base.smallestScreenWidthDp != change.smallestScreenWidthDp) {
            delta.smallestScreenWidthDp = change.smallestScreenWidthDp;
        }
        if (base.densityDpi != change.densityDpi) {
            delta.densityDpi = change.densityDpi;
        }
        if (base.assetsSeq != change.assetsSeq) {
            delta.assetsSeq = change.assetsSeq;
        }
        if (!base.windowConfiguration.equals(change.windowConfiguration)) {
            delta.windowConfiguration.setTo(change.windowConfiguration);
        }
        return delta;
    }

    private static final java.lang.String XML_ATTR_FONT_SCALE = "fs";

    private static final java.lang.String XML_ATTR_MCC = "mcc";

    private static final java.lang.String XML_ATTR_MNC = "mnc";

    private static final java.lang.String XML_ATTR_LOCALES = "locales";

    private static final java.lang.String XML_ATTR_TOUCHSCREEN = "touch";

    private static final java.lang.String XML_ATTR_KEYBOARD = "key";

    private static final java.lang.String XML_ATTR_KEYBOARD_HIDDEN = "keyHid";

    private static final java.lang.String XML_ATTR_HARD_KEYBOARD_HIDDEN = "hardKeyHid";

    private static final java.lang.String XML_ATTR_NAVIGATION = "nav";

    private static final java.lang.String XML_ATTR_NAVIGATION_HIDDEN = "navHid";

    private static final java.lang.String XML_ATTR_ORIENTATION = "ori";

    private static final java.lang.String XML_ATTR_ROTATION = "rot";

    private static final java.lang.String XML_ATTR_SCREEN_LAYOUT = "scrLay";

    private static final java.lang.String XML_ATTR_COLOR_MODE = "clrMod";

    private static final java.lang.String XML_ATTR_UI_MODE = "ui";

    private static final java.lang.String XML_ATTR_SCREEN_WIDTH = "width";

    private static final java.lang.String XML_ATTR_SCREEN_HEIGHT = "height";

    private static final java.lang.String XML_ATTR_SMALLEST_WIDTH = "sw";

    private static final java.lang.String XML_ATTR_DENSITY = "density";

    private static final java.lang.String XML_ATTR_APP_BOUNDS = "app_bounds";

    /**
     * Reads the attributes corresponding to Configuration member fields from the Xml parser.
     * The parser is expected to be on a tag which has Configuration attributes.
     *
     * @param parser
     * 		The Xml parser from which to read attributes.
     * @param configOut
     * 		The Configuration to populate from the Xml attributes.
     * 		{@hide }
     */
    public static void readXmlAttrs(org.xmlpull.v1.XmlPullParser parser, android.content.res.Configuration configOut) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        configOut.fontScale = java.lang.Float.intBitsToFloat(com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_FONT_SCALE, 0));
        configOut.mcc = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_MCC, 0);
        configOut.mnc = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_MNC, 0);
        final java.lang.String localesStr = com.android.internal.util.XmlUtils.readStringAttribute(parser, android.content.res.Configuration.XML_ATTR_LOCALES);
        configOut.mLocaleList = android.os.LocaleList.forLanguageTags(localesStr);
        configOut.locale = configOut.mLocaleList.get(0);
        configOut.touchscreen = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_TOUCHSCREEN, android.content.res.Configuration.TOUCHSCREEN_UNDEFINED);
        configOut.keyboard = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_KEYBOARD, android.content.res.Configuration.KEYBOARD_UNDEFINED);
        configOut.keyboardHidden = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_KEYBOARD_HIDDEN, android.content.res.Configuration.KEYBOARDHIDDEN_UNDEFINED);
        configOut.hardKeyboardHidden = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_HARD_KEYBOARD_HIDDEN, android.content.res.Configuration.HARDKEYBOARDHIDDEN_UNDEFINED);
        configOut.navigation = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_NAVIGATION, android.content.res.Configuration.NAVIGATION_UNDEFINED);
        configOut.navigationHidden = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_NAVIGATION_HIDDEN, android.content.res.Configuration.NAVIGATIONHIDDEN_UNDEFINED);
        configOut.orientation = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_ORIENTATION, android.content.res.Configuration.ORIENTATION_UNDEFINED);
        configOut.screenLayout = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_SCREEN_LAYOUT, android.content.res.Configuration.SCREENLAYOUT_UNDEFINED);
        configOut.colorMode = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_COLOR_MODE, android.content.res.Configuration.COLOR_MODE_UNDEFINED);
        configOut.uiMode = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_UI_MODE, 0);
        configOut.screenWidthDp = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_SCREEN_WIDTH, android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED);
        configOut.screenHeightDp = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_SCREEN_HEIGHT, android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED);
        configOut.smallestScreenWidthDp = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_SMALLEST_WIDTH, android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED);
        configOut.densityDpi = com.android.internal.util.XmlUtils.readIntAttribute(parser, android.content.res.Configuration.XML_ATTR_DENSITY, android.content.res.Configuration.DENSITY_DPI_UNDEFINED);
        // For persistence, we don't care about assetsSeq and WindowConfiguration, so do not read it
        // out.
    }

    /**
     * Writes the Configuration's member fields as attributes into the XmlSerializer.
     * The serializer is expected to have already started a tag so that attributes can be
     * immediately written.
     *
     * @param xml
     * 		The serializer to which to write the attributes.
     * @param config
     * 		The Configuration whose member fields to write.
     * 		{@hide }
     */
    public static void writeXmlAttrs(org.xmlpull.v1.XmlSerializer xml, android.content.res.Configuration config) throws java.io.IOException {
        com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_FONT_SCALE, java.lang.Float.floatToIntBits(config.fontScale));
        if (config.mcc != 0) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_MCC, config.mcc);
        }
        if (config.mnc != 0) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_MNC, config.mnc);
        }
        config.fixUpLocaleList();
        if (!config.mLocaleList.isEmpty()) {
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, android.content.res.Configuration.XML_ATTR_LOCALES, config.mLocaleList.toLanguageTags());
        }
        if (config.touchscreen != android.content.res.Configuration.TOUCHSCREEN_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_TOUCHSCREEN, config.touchscreen);
        }
        if (config.keyboard != android.content.res.Configuration.KEYBOARD_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_KEYBOARD, config.keyboard);
        }
        if (config.keyboardHidden != android.content.res.Configuration.KEYBOARDHIDDEN_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_KEYBOARD_HIDDEN, config.keyboardHidden);
        }
        if (config.hardKeyboardHidden != android.content.res.Configuration.HARDKEYBOARDHIDDEN_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_HARD_KEYBOARD_HIDDEN, config.hardKeyboardHidden);
        }
        if (config.navigation != android.content.res.Configuration.NAVIGATION_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_NAVIGATION, config.navigation);
        }
        if (config.navigationHidden != android.content.res.Configuration.NAVIGATIONHIDDEN_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_NAVIGATION_HIDDEN, config.navigationHidden);
        }
        if (config.orientation != android.content.res.Configuration.ORIENTATION_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_ORIENTATION, config.orientation);
        }
        if (config.screenLayout != android.content.res.Configuration.SCREENLAYOUT_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_SCREEN_LAYOUT, config.screenLayout);
        }
        if (config.colorMode != android.content.res.Configuration.COLOR_MODE_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_COLOR_MODE, config.colorMode);
        }
        if (config.uiMode != 0) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_UI_MODE, config.uiMode);
        }
        if (config.screenWidthDp != android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_SCREEN_WIDTH, config.screenWidthDp);
        }
        if (config.screenHeightDp != android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_SCREEN_HEIGHT, config.screenHeightDp);
        }
        if (config.smallestScreenWidthDp != android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_SMALLEST_WIDTH, config.smallestScreenWidthDp);
        }
        if (config.densityDpi != android.content.res.Configuration.DENSITY_DPI_UNDEFINED) {
            com.android.internal.util.XmlUtils.writeIntAttribute(xml, android.content.res.Configuration.XML_ATTR_DENSITY, config.densityDpi);
        }
        // For persistence, we do not care about assetsSeq and window configuration, so do not write
        // it out.
    }
}

