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
package android.view;


/**
 * Contains methods to standard constants used in the UI for timeouts, sizes, and distances.
 */
public class ViewConfiguration {
    /**
     * Defines the width of the horizontal scrollbar and the height of the vertical scrollbar in
     * dips
     */
    private static final int SCROLL_BAR_SIZE = 4;

    /**
     * Duration of the fade when scrollbars fade away in milliseconds
     */
    private static final int SCROLL_BAR_FADE_DURATION = 250;

    /**
     * Default delay before the scrollbars fade in milliseconds
     */
    private static final int SCROLL_BAR_DEFAULT_DELAY = 300;

    /**
     * Defines the length of the fading edges in dips
     */
    private static final int FADING_EDGE_LENGTH = 12;

    /**
     * Defines the duration in milliseconds of the pressed state in child
     * components.
     */
    private static final int PRESSED_STATE_DURATION = 64;

    /**
     * Defines the default duration in milliseconds before a press turns into
     * a long press
     */
    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;

    /**
     * Defines the default duration in milliseconds between the first tap's up event and the second
     * tap's down event for an interaction to be considered part of the same multi-press.
     */
    private static final int DEFAULT_MULTI_PRESS_TIMEOUT = 300;

    /**
     * Defines the time between successive key repeats in milliseconds.
     */
    private static final int KEY_REPEAT_DELAY = 50;

    /**
     * Defines the duration in milliseconds a user needs to hold down the
     * appropriate button to bring up the global actions dialog (power off,
     * lock screen, etc).
     */
    private static final int GLOBAL_ACTIONS_KEY_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds a user needs to hold down the
     * appropriate buttons (power + volume down) to trigger the screenshot chord.
     */
    private static final int SCREENSHOT_CHORD_KEY_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds a user needs to hold down the
     * appropriate button to bring up the accessibility shortcut for the first time
     */
    private static final int A11Y_SHORTCUT_KEY_TIMEOUT = 3000;

    /**
     * Defines the duration in milliseconds a user needs to hold down the
     * appropriate button to enable the accessibility shortcut once it's configured.
     */
    private static final int A11Y_SHORTCUT_KEY_TIMEOUT_AFTER_CONFIRMATION = 1000;

    /**
     * Defines the duration in milliseconds we will wait to see if a touch event
     * is a tap or a scroll. If the user does not move within this interval, it is
     * considered to be a tap.
     */
    private static final int TAP_TIMEOUT = 100;

    /**
     * Defines the duration in milliseconds we will wait to see if a touch event
     * is a jump tap. If the user does not complete the jump tap within this interval, it is
     * considered to be a tap.
     */
    private static final int JUMP_TAP_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    private static final int DOUBLE_TAP_TIMEOUT = 300;

    /**
     * Defines the minimum duration in milliseconds between the first tap's up event and
     * the second tap's down event for an interaction to be considered a
     * double-tap.
     */
    private static final int DOUBLE_TAP_MIN_TIME = 40;

    /**
     * Defines the maximum duration in milliseconds between a touch pad
     * touch and release for a given touch to be considered a tap (click) as
     * opposed to a hover movement gesture.
     */
    private static final int HOVER_TAP_TIMEOUT = 150;

    /**
     * Defines the maximum distance in pixels that a touch pad touch can move
     * before being released for it to be considered a tap (click) as opposed
     * to a hover movement gesture.
     */
    private static final int HOVER_TAP_SLOP = 20;

    /**
     * Defines the duration in milliseconds we want to display zoom controls in response
     * to a user panning within an application.
     */
    private static final int ZOOM_CONTROLS_TIMEOUT = 3000;

    /**
     * Inset in dips to look for touchable content when the user touches the edge of the screen
     */
    private static final int EDGE_SLOP = 12;

    /**
     * Distance a touch can wander before we think the user is scrolling in dips.
     * Note that this value defined here is only used as a fallback by legacy/misbehaving
     * applications that do not provide a Context for determining density/configuration-dependent
     * values.
     *
     * To alter this value, see the configuration resource config_viewConfigurationTouchSlop
     * in frameworks/base/core/res/res/values/config.xml or the appropriate device resource overlay.
     * It may be appropriate to tweak this on a device-specific basis in an overlay based on
     * the characteristics of the touch panel and firmware.
     */
    private static final int TOUCH_SLOP = 8;

    /**
     * Defines the minimum size of the touch target for a scrollbar in dips
     */
    private static final int MIN_SCROLLBAR_TOUCH_TARGET = 48;

    /**
     * Distance the first touch can wander before we stop considering this event a double tap
     * (in dips)
     */
    private static final int DOUBLE_TAP_TOUCH_SLOP = android.view.ViewConfiguration.TOUCH_SLOP;

    /**
     * Distance a touch can wander before we think the user is attempting a paged scroll
     * (in dips)
     *
     * Note that this value defined here is only used as a fallback by legacy/misbehaving
     * applications that do not provide a Context for determining density/configuration-dependent
     * values.
     *
     * See the note above on {@link #TOUCH_SLOP} regarding the dimen resource
     * config_viewConfigurationTouchSlop. ViewConfiguration will report a paging touch slop of
     * config_viewConfigurationTouchSlop * 2 when provided with a Context.
     */
    private static final int PAGING_TOUCH_SLOP = android.view.ViewConfiguration.TOUCH_SLOP * 2;

    /**
     * Distance in dips between the first touch and second touch to still be considered a double tap
     */
    private static final int DOUBLE_TAP_SLOP = 100;

    /**
     * Distance in dips a touch needs to be outside of a window's bounds for it to
     * count as outside for purposes of dismissing the window.
     */
    private static final int WINDOW_TOUCH_SLOP = 16;

    /**
     * Minimum velocity to initiate a fling, as measured in dips per second
     */
    private static final int MINIMUM_FLING_VELOCITY = 50;

    /**
     * Maximum velocity to initiate a fling, as measured in dips per second
     */
    private static final int MAXIMUM_FLING_VELOCITY = 8000;

    /**
     * Delay before dispatching a recurring accessibility event in milliseconds.
     * This delay guarantees that a recurring event will be send at most once
     * during the {@link #SEND_RECURRING_ACCESSIBILITY_EVENTS_INTERVAL_MILLIS} time
     * frame.
     */
    private static final long SEND_RECURRING_ACCESSIBILITY_EVENTS_INTERVAL_MILLIS = 100;

    /**
     * The maximum size of View's drawing cache, expressed in bytes. This size
     * should be at least equal to the size of the screen in ARGB888 format.
     */
    @java.lang.Deprecated
    private static final int MAXIMUM_DRAWING_CACHE_SIZE = (480 * 800) * 4;// ARGB8888


    /**
     * The coefficient of friction applied to flings/scrolls.
     */
    @android.annotation.UnsupportedAppUsage
    private static final float SCROLL_FRICTION = 0.015F;

    /**
     * Max distance in dips to overscroll for edge effects
     */
    private static final int OVERSCROLL_DISTANCE = 0;

    /**
     * Max distance in dips to overfling for edge effects
     */
    private static final int OVERFLING_DISTANCE = 6;

    /**
     * Amount to scroll in response to a horizontal {@link MotionEvent#ACTION_SCROLL} event,
     * in dips per axis value.
     */
    private static final float HORIZONTAL_SCROLL_FACTOR = 64;

    /**
     * Amount to scroll in response to a vertical {@link MotionEvent#ACTION_SCROLL} event,
     * in dips per axis value.
     */
    private static final float VERTICAL_SCROLL_FACTOR = 64;

    /**
     * Default duration to hide an action mode for.
     */
    private static final long ACTION_MODE_HIDE_DURATION_DEFAULT = 2000;

    /**
     * Defines the duration in milliseconds before an end of a long press causes a tooltip to be
     * hidden.
     */
    private static final int LONG_PRESS_TOOLTIP_HIDE_TIMEOUT = 1500;

    /**
     * Defines the duration in milliseconds before a hover event causes a tooltip to be shown.
     */
    private static final int HOVER_TOOLTIP_SHOW_TIMEOUT = 500;

    /**
     * Defines the duration in milliseconds before mouse inactivity causes a tooltip to be hidden.
     * (default variant to be used when {@link View#SYSTEM_UI_FLAG_LOW_PROFILE} is not set).
     */
    private static final int HOVER_TOOLTIP_HIDE_TIMEOUT = 15000;

    /**
     * Defines the duration in milliseconds before mouse inactivity causes a tooltip to be hidden
     * (short version to be used when {@link View#SYSTEM_UI_FLAG_LOW_PROFILE} is set).
     */
    private static final int HOVER_TOOLTIP_HIDE_SHORT_TIMEOUT = 3000;

    /**
     * Configuration values for overriding {@link #hasPermanentMenuKey()} behavior.
     * These constants must match the definition in res/values/config.xml.
     */
    private static final int HAS_PERMANENT_MENU_KEY_AUTODETECT = 0;

    private static final int HAS_PERMANENT_MENU_KEY_TRUE = 1;

    private static final int HAS_PERMANENT_MENU_KEY_FALSE = 2;

    /**
     * The multiplication factor for inhibiting default gestures.
     */
    private static final float AMBIGUOUS_GESTURE_MULTIPLIER = 2.0F;

    private final boolean mConstructedWithContext;

    private final int mEdgeSlop;

    private final int mFadingEdgeLength;

    private final int mMinimumFlingVelocity;

    private final int mMaximumFlingVelocity;

    private final int mScrollbarSize;

    private final int mTouchSlop;

    private final int mMinScalingSpan;

    private final int mHoverSlop;

    private final int mMinScrollbarTouchTarget;

    private final int mDoubleTapTouchSlop;

    private final int mPagingTouchSlop;

    private final int mDoubleTapSlop;

    private final int mWindowTouchSlop;

    private final int mMaximumDrawingCacheSize;

    private final int mOverscrollDistance;

    private final int mOverflingDistance;

    @android.annotation.UnsupportedAppUsage
    private final boolean mFadingMarqueeEnabled;

    private final long mGlobalActionsKeyTimeout;

    private final float mVerticalScrollFactor;

    private final float mHorizontalScrollFactor;

    private final boolean mShowMenuShortcutsWhenKeyboardPresent;

    private final long mScreenshotChordKeyTimeout;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123768915)
    private boolean sHasPermanentMenuKey;

    @android.annotation.UnsupportedAppUsage
    private boolean sHasPermanentMenuKeySet;

    @android.annotation.UnsupportedAppUsage
    static final android.util.SparseArray<android.view.ViewConfiguration> sConfigurations = new android.util.SparseArray<android.view.ViewConfiguration>(2);

    /**
     *
     *
     * @deprecated Use {@link android.view.ViewConfiguration#get(android.content.Context)} instead.
     */
    @java.lang.Deprecated
    public ViewConfiguration() {
        mConstructedWithContext = false;
        mEdgeSlop = android.view.ViewConfiguration.EDGE_SLOP;
        mFadingEdgeLength = android.view.ViewConfiguration.FADING_EDGE_LENGTH;
        mMinimumFlingVelocity = android.view.ViewConfiguration.MINIMUM_FLING_VELOCITY;
        mMaximumFlingVelocity = android.view.ViewConfiguration.MAXIMUM_FLING_VELOCITY;
        mScrollbarSize = android.view.ViewConfiguration.SCROLL_BAR_SIZE;
        mTouchSlop = android.view.ViewConfiguration.TOUCH_SLOP;
        mHoverSlop = android.view.ViewConfiguration.TOUCH_SLOP / 2;
        mMinScrollbarTouchTarget = android.view.ViewConfiguration.MIN_SCROLLBAR_TOUCH_TARGET;
        mDoubleTapTouchSlop = android.view.ViewConfiguration.DOUBLE_TAP_TOUCH_SLOP;
        mPagingTouchSlop = android.view.ViewConfiguration.PAGING_TOUCH_SLOP;
        mDoubleTapSlop = android.view.ViewConfiguration.DOUBLE_TAP_SLOP;
        mWindowTouchSlop = android.view.ViewConfiguration.WINDOW_TOUCH_SLOP;
        // noinspection deprecation
        mMaximumDrawingCacheSize = android.view.ViewConfiguration.MAXIMUM_DRAWING_CACHE_SIZE;
        mOverscrollDistance = android.view.ViewConfiguration.OVERSCROLL_DISTANCE;
        mOverflingDistance = android.view.ViewConfiguration.OVERFLING_DISTANCE;
        mFadingMarqueeEnabled = true;
        mGlobalActionsKeyTimeout = android.view.ViewConfiguration.GLOBAL_ACTIONS_KEY_TIMEOUT;
        mHorizontalScrollFactor = android.view.ViewConfiguration.HORIZONTAL_SCROLL_FACTOR;
        mVerticalScrollFactor = android.view.ViewConfiguration.VERTICAL_SCROLL_FACTOR;
        mShowMenuShortcutsWhenKeyboardPresent = false;
        mScreenshotChordKeyTimeout = android.view.ViewConfiguration.SCREENSHOT_CHORD_KEY_TIMEOUT;
        // Getter throws if mConstructedWithContext is false so doesn't matter what
        // this value is.
        mMinScalingSpan = 0;
    }

    /**
     * Creates a new configuration for the specified context. The configuration depends on
     * various parameters of the context, like the dimension of the display or the density
     * of the display.
     *
     * @param context
     * 		The application context used to initialize this view configuration.
     * @see #get(android.content.Context)
     * @see android.util.DisplayMetrics
     */
    private ViewConfiguration(android.content.Context context) {
        mConstructedWithContext = true;
        final android.content.res.Resources res = context.getResources();
        final android.util.DisplayMetrics metrics = res.getDisplayMetrics();
        final android.content.res.Configuration config = res.getConfiguration();
        final float density = metrics.density;
        final float sizeAndDensity;
        if (config.isLayoutSizeAtLeast(android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
            sizeAndDensity = density * 1.5F;
        } else {
            sizeAndDensity = density;
        }
        mEdgeSlop = ((int) ((sizeAndDensity * android.view.ViewConfiguration.EDGE_SLOP) + 0.5F));
        mFadingEdgeLength = ((int) ((sizeAndDensity * android.view.ViewConfiguration.FADING_EDGE_LENGTH) + 0.5F));
        mScrollbarSize = res.getDimensionPixelSize(com.android.internal.R.dimen.config_scrollbarSize);
        mDoubleTapSlop = ((int) ((sizeAndDensity * android.view.ViewConfiguration.DOUBLE_TAP_SLOP) + 0.5F));
        mWindowTouchSlop = ((int) ((sizeAndDensity * android.view.ViewConfiguration.WINDOW_TOUCH_SLOP) + 0.5F));
        // Size of the screen in bytes, in ARGB_8888 format
        final android.view.WindowManager win = ((android.view.WindowManager) (context.getSystemService(android.content.Context.WINDOW_SERVICE)));
        final android.view.Display display = win.getDefaultDisplay();
        final android.graphics.Point size = new android.graphics.Point();
        display.getRealSize(size);
        mMaximumDrawingCacheSize = (4 * size.x) * size.y;
        mOverscrollDistance = ((int) ((sizeAndDensity * android.view.ViewConfiguration.OVERSCROLL_DISTANCE) + 0.5F));
        mOverflingDistance = ((int) ((sizeAndDensity * android.view.ViewConfiguration.OVERFLING_DISTANCE) + 0.5F));
        if (!sHasPermanentMenuKeySet) {
            final int configVal = res.getInteger(com.android.internal.R.integer.config_overrideHasPermanentMenuKey);
            switch (configVal) {
                default :
                case android.view.ViewConfiguration.HAS_PERMANENT_MENU_KEY_AUTODETECT :
                    {
                        android.view.IWindowManager wm = android.view.WindowManagerGlobal.getWindowManagerService();
                        try {
                            sHasPermanentMenuKey = !wm.hasNavigationBar(context.getDisplayId());
                            sHasPermanentMenuKeySet = true;
                        } catch (android.os.RemoteException ex) {
                            sHasPermanentMenuKey = false;
                        }
                    }
                    break;
                case android.view.ViewConfiguration.HAS_PERMANENT_MENU_KEY_TRUE :
                    sHasPermanentMenuKey = true;
                    sHasPermanentMenuKeySet = true;
                    break;
                case android.view.ViewConfiguration.HAS_PERMANENT_MENU_KEY_FALSE :
                    sHasPermanentMenuKey = false;
                    sHasPermanentMenuKeySet = true;
                    break;
            }
        }
        mFadingMarqueeEnabled = res.getBoolean(com.android.internal.R.bool.config_ui_enableFadingMarquee);
        mTouchSlop = res.getDimensionPixelSize(com.android.internal.R.dimen.config_viewConfigurationTouchSlop);
        mHoverSlop = res.getDimensionPixelSize(com.android.internal.R.dimen.config_viewConfigurationHoverSlop);
        mMinScrollbarTouchTarget = res.getDimensionPixelSize(com.android.internal.R.dimen.config_minScrollbarTouchTarget);
        mPagingTouchSlop = mTouchSlop * 2;
        mDoubleTapTouchSlop = mTouchSlop;
        mMinimumFlingVelocity = res.getDimensionPixelSize(com.android.internal.R.dimen.config_viewMinFlingVelocity);
        mMaximumFlingVelocity = res.getDimensionPixelSize(com.android.internal.R.dimen.config_viewMaxFlingVelocity);
        mGlobalActionsKeyTimeout = res.getInteger(com.android.internal.R.integer.config_globalActionsKeyTimeout);
        mHorizontalScrollFactor = res.getDimensionPixelSize(com.android.internal.R.dimen.config_horizontalScrollFactor);
        mVerticalScrollFactor = res.getDimensionPixelSize(com.android.internal.R.dimen.config_verticalScrollFactor);
        mShowMenuShortcutsWhenKeyboardPresent = res.getBoolean(com.android.internal.R.bool.config_showMenuShortcutsWhenKeyboardPresent);
        mMinScalingSpan = res.getDimensionPixelSize(com.android.internal.R.dimen.config_minScalingSpan);
        mScreenshotChordKeyTimeout = res.getInteger(com.android.internal.R.integer.config_screenshotChordKeyTimeout);
    }

    /**
     * Returns a configuration for the specified context. The configuration depends on
     * various parameters of the context, like the dimension of the display or the
     * density of the display.
     *
     * @param context
     * 		The application context used to initialize the view configuration.
     */
    public static android.view.ViewConfiguration get(android.content.Context context) {
        final android.util.DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final int density = ((int) (100.0F * metrics.density));
        android.view.ViewConfiguration configuration = android.view.ViewConfiguration.sConfigurations.get(density);
        if (configuration == null) {
            configuration = new android.view.ViewConfiguration(context);
            android.view.ViewConfiguration.sConfigurations.put(density, configuration);
        }
        return configuration;
    }

    /**
     *
     *
     * @return The width of the horizontal scrollbar and the height of the vertical
    scrollbar in dips
     * @deprecated Use {@link #getScaledScrollBarSize()} instead.
     */
    @java.lang.Deprecated
    public static int getScrollBarSize() {
        return android.view.ViewConfiguration.SCROLL_BAR_SIZE;
    }

    /**
     *
     *
     * @return The width of the horizontal scrollbar and the height of the vertical
    scrollbar in pixels
     */
    public int getScaledScrollBarSize() {
        return mScrollbarSize;
    }

    /**
     *
     *
     * @return the minimum size of the scrollbar thumb's touch target in pixels
     * @unknown 
     */
    public int getScaledMinScrollbarTouchTarget() {
        return mMinScrollbarTouchTarget;
    }

    /**
     *
     *
     * @return Duration of the fade when scrollbars fade away in milliseconds
     */
    public static int getScrollBarFadeDuration() {
        return android.view.ViewConfiguration.SCROLL_BAR_FADE_DURATION;
    }

    /**
     *
     *
     * @return Default delay before the scrollbars fade in milliseconds
     */
    public static int getScrollDefaultDelay() {
        return android.view.ViewConfiguration.SCROLL_BAR_DEFAULT_DELAY;
    }

    /**
     *
     *
     * @return the length of the fading edges in dips
     * @deprecated Use {@link #getScaledFadingEdgeLength()} instead.
     */
    @java.lang.Deprecated
    public static int getFadingEdgeLength() {
        return android.view.ViewConfiguration.FADING_EDGE_LENGTH;
    }

    /**
     *
     *
     * @return the length of the fading edges in pixels
     */
    public int getScaledFadingEdgeLength() {
        return mFadingEdgeLength;
    }

    /**
     *
     *
     * @return the duration in milliseconds of the pressed state in child
    components.
     */
    public static int getPressedStateDuration() {
        return android.view.ViewConfiguration.PRESSED_STATE_DURATION;
    }

    /**
     *
     *
     * @return the duration in milliseconds before a press turns into
    a long press
     */
    public static int getLongPressTimeout() {
        return android.app.AppGlobals.getIntCoreSetting(Settings.Secure.LONG_PRESS_TIMEOUT, android.view.ViewConfiguration.DEFAULT_LONG_PRESS_TIMEOUT);
    }

    /**
     *
     *
     * @return the duration in milliseconds between the first tap's up event and the second tap's
    down event for an interaction to be considered part of the same multi-press.
     * @unknown 
     */
    public static int getMultiPressTimeout() {
        return android.app.AppGlobals.getIntCoreSetting(Settings.Secure.MULTI_PRESS_TIMEOUT, android.view.ViewConfiguration.DEFAULT_MULTI_PRESS_TIMEOUT);
    }

    /**
     *
     *
     * @return the time before the first key repeat in milliseconds.
     */
    public static int getKeyRepeatTimeout() {
        return android.view.ViewConfiguration.getLongPressTimeout();
    }

    /**
     *
     *
     * @return the time between successive key repeats in milliseconds.
     */
    public static int getKeyRepeatDelay() {
        return android.view.ViewConfiguration.KEY_REPEAT_DELAY;
    }

    /**
     *
     *
     * @return the duration in milliseconds we will wait to see if a touch event
    is a tap or a scroll. If the user does not move within this interval, it is
    considered to be a tap.
     */
    public static int getTapTimeout() {
        return android.view.ViewConfiguration.TAP_TIMEOUT;
    }

    /**
     *
     *
     * @return the duration in milliseconds we will wait to see if a touch event
    is a jump tap. If the user does not move within this interval, it is
    considered to be a tap.
     */
    public static int getJumpTapTimeout() {
        return android.view.ViewConfiguration.JUMP_TAP_TIMEOUT;
    }

    /**
     *
     *
     * @return the duration in milliseconds between the first tap's up event and
    the second tap's down event for an interaction to be considered a
    double-tap.
     */
    public static int getDoubleTapTimeout() {
        return android.view.ViewConfiguration.DOUBLE_TAP_TIMEOUT;
    }

    /**
     *
     *
     * @return the minimum duration in milliseconds between the first tap's
    up event and the second tap's down event for an interaction to be considered a
    double-tap.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static int getDoubleTapMinTime() {
        return android.view.ViewConfiguration.DOUBLE_TAP_MIN_TIME;
    }

    /**
     *
     *
     * @return the maximum duration in milliseconds between a touch pad
    touch and release for a given touch to be considered a tap (click) as
    opposed to a hover movement gesture.
     * @unknown 
     */
    public static int getHoverTapTimeout() {
        return android.view.ViewConfiguration.HOVER_TAP_TIMEOUT;
    }

    /**
     *
     *
     * @return the maximum distance in pixels that a touch pad touch can move
    before being released for it to be considered a tap (click) as opposed
    to a hover movement gesture.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static int getHoverTapSlop() {
        return android.view.ViewConfiguration.HOVER_TAP_SLOP;
    }

    /**
     *
     *
     * @return Inset in dips to look for touchable content when the user touches the edge of the
    screen
     * @deprecated Use {@link #getScaledEdgeSlop()} instead.
     */
    @java.lang.Deprecated
    public static int getEdgeSlop() {
        return android.view.ViewConfiguration.EDGE_SLOP;
    }

    /**
     *
     *
     * @return Inset in pixels to look for touchable content when the user touches the edge of the
    screen
     */
    public int getScaledEdgeSlop() {
        return mEdgeSlop;
    }

    /**
     *
     *
     * @return Distance in dips a touch can wander before we think the user is scrolling
     * @deprecated Use {@link #getScaledTouchSlop()} instead.
     */
    @java.lang.Deprecated
    public static int getTouchSlop() {
        return android.view.ViewConfiguration.TOUCH_SLOP;
    }

    /**
     *
     *
     * @return Distance in pixels a touch can wander before we think the user is scrolling
     */
    public int getScaledTouchSlop() {
        return mTouchSlop;
    }

    /**
     *
     *
     * @return Distance in pixels a hover can wander while it is still considered "stationary".
     */
    public int getScaledHoverSlop() {
        return mHoverSlop;
    }

    /**
     *
     *
     * @return Distance in pixels the first touch can wander before we do not consider this a
    potential double tap event
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public int getScaledDoubleTapTouchSlop() {
        return mDoubleTapTouchSlop;
    }

    /**
     *
     *
     * @return Distance in pixels a touch can wander before we think the user is scrolling a full
    page
     */
    public int getScaledPagingTouchSlop() {
        return mPagingTouchSlop;
    }

    /**
     *
     *
     * @return Distance in dips between the first touch and second touch to still be
    considered a double tap
     * @deprecated Use {@link #getScaledDoubleTapSlop()} instead.
     * @unknown The only client of this should be GestureDetector, which needs this
    for clients that still use its deprecated constructor.
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public static int getDoubleTapSlop() {
        return android.view.ViewConfiguration.DOUBLE_TAP_SLOP;
    }

    /**
     *
     *
     * @return Distance in pixels between the first touch and second touch to still be
    considered a double tap
     */
    public int getScaledDoubleTapSlop() {
        return mDoubleTapSlop;
    }

    /**
     * Interval for dispatching a recurring accessibility event in milliseconds.
     * This interval guarantees that a recurring event will be send at most once
     * during the {@link #getSendRecurringAccessibilityEventsInterval()} time frame.
     *
     * @return The delay in milliseconds.
     * @unknown 
     */
    public static long getSendRecurringAccessibilityEventsInterval() {
        return android.view.ViewConfiguration.SEND_RECURRING_ACCESSIBILITY_EVENTS_INTERVAL_MILLIS;
    }

    /**
     *
     *
     * @return Distance in dips a touch must be outside the bounds of a window for it
    to be counted as outside the window for purposes of dismissing that
    window.
     * @deprecated Use {@link #getScaledWindowTouchSlop()} instead.
     */
    @java.lang.Deprecated
    public static int getWindowTouchSlop() {
        return android.view.ViewConfiguration.WINDOW_TOUCH_SLOP;
    }

    /**
     *
     *
     * @return Distance in pixels a touch must be outside the bounds of a window for it
    to be counted as outside the window for purposes of dismissing that window.
     */
    public int getScaledWindowTouchSlop() {
        return mWindowTouchSlop;
    }

    /**
     *
     *
     * @return Minimum velocity to initiate a fling, as measured in dips per second.
     * @deprecated Use {@link #getScaledMinimumFlingVelocity()} instead.
     */
    @java.lang.Deprecated
    public static int getMinimumFlingVelocity() {
        return android.view.ViewConfiguration.MINIMUM_FLING_VELOCITY;
    }

    /**
     *
     *
     * @return Minimum velocity to initiate a fling, as measured in pixels per second.
     */
    public int getScaledMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    /**
     *
     *
     * @return Maximum velocity to initiate a fling, as measured in dips per second.
     * @deprecated Use {@link #getScaledMaximumFlingVelocity()} instead.
     */
    @java.lang.Deprecated
    public static int getMaximumFlingVelocity() {
        return android.view.ViewConfiguration.MAXIMUM_FLING_VELOCITY;
    }

    /**
     *
     *
     * @return Maximum velocity to initiate a fling, as measured in pixels per second.
     */
    public int getScaledMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

    /**
     *
     *
     * @return Amount to scroll in response to a {@link MotionEvent#ACTION_SCROLL} event. Multiply
    this by the event's axis value to obtain the number of pixels to be scrolled.
     * @unknown 
     */
    public int getScaledScrollFactor() {
        return ((int) (mVerticalScrollFactor));
    }

    /**
     *
     *
     * @return Amount to scroll in response to a horizontal {@link MotionEvent#ACTION_SCROLL} event.
    Multiply this by the event's axis value to obtain the number of pixels to be scrolled.
     */
    public float getScaledHorizontalScrollFactor() {
        return mHorizontalScrollFactor;
    }

    /**
     *
     *
     * @return Amount to scroll in response to a vertical {@link MotionEvent#ACTION_SCROLL} event.
    Multiply this by the event's axis value to obtain the number of pixels to be scrolled.
     */
    public float getScaledVerticalScrollFactor() {
        return mVerticalScrollFactor;
    }

    /**
     * The maximum drawing cache size expressed in bytes.
     *
     * @return the maximum size of View's drawing cache expressed in bytes
     * @deprecated Use {@link #getScaledMaximumDrawingCacheSize()} instead.
     */
    @java.lang.Deprecated
    public static int getMaximumDrawingCacheSize() {
        // noinspection deprecation
        return android.view.ViewConfiguration.MAXIMUM_DRAWING_CACHE_SIZE;
    }

    /**
     * The maximum drawing cache size expressed in bytes.
     *
     * @return the maximum size of View's drawing cache expressed in bytes
     */
    public int getScaledMaximumDrawingCacheSize() {
        return mMaximumDrawingCacheSize;
    }

    /**
     *
     *
     * @return The maximum distance a View should overscroll by when showing edge effects (in
    pixels).
     */
    public int getScaledOverscrollDistance() {
        return mOverscrollDistance;
    }

    /**
     *
     *
     * @return The maximum distance a View should overfling by when showing edge effects (in
    pixels).
     */
    public int getScaledOverflingDistance() {
        return mOverflingDistance;
    }

    /**
     * The amount of time that the zoom controls should be
     * displayed on the screen expressed in milliseconds.
     *
     * @return the time the zoom controls should be visible expressed
    in milliseconds.
     */
    public static long getZoomControlsTimeout() {
        return android.view.ViewConfiguration.ZOOM_CONTROLS_TIMEOUT;
    }

    /**
     * The amount of time a user needs to press the relevant key to bring up
     * the global actions dialog.
     *
     * @return how long a user needs to press the relevant key to bring up
    the global actions dialog.
     * @deprecated This timeout should not be used by applications
     */
    @java.lang.Deprecated
    public static long getGlobalActionKeyTimeout() {
        return android.view.ViewConfiguration.GLOBAL_ACTIONS_KEY_TIMEOUT;
    }

    /**
     * The amount of time a user needs to press the relevant key to bring up
     * the global actions dialog.
     *
     * @return how long a user needs to press the relevant key to bring up
    the global actions dialog.
     * @unknown 
     */
    @android.annotation.TestApi
    public long getDeviceGlobalActionKeyTimeout() {
        return mGlobalActionsKeyTimeout;
    }

    /**
     * The amount of time a user needs to press the relevant keys to trigger
     * the screenshot chord.
     *
     * @return how long a user needs to press the relevant keys to trigger
    the screenshot chord.
     * @unknown 
     */
    public long getScreenshotChordKeyTimeout() {
        return mScreenshotChordKeyTimeout;
    }

    /**
     * The amount of time a user needs to press the relevant keys to activate the accessibility
     * shortcut.
     *
     * @return how long a user needs to press the relevant keys to activate the accessibility
    shortcut.
     * @unknown 
     */
    public long getAccessibilityShortcutKeyTimeout() {
        return android.view.ViewConfiguration.A11Y_SHORTCUT_KEY_TIMEOUT;
    }

    /**
     *
     *
     * @return The amount of time a user needs to press the relevant keys to activate the
    accessibility shortcut after it's confirmed that accessibility shortcut is used.
     * @unknown 
     */
    public long getAccessibilityShortcutKeyTimeoutAfterConfirmation() {
        return android.view.ViewConfiguration.A11Y_SHORTCUT_KEY_TIMEOUT_AFTER_CONFIRMATION;
    }

    /**
     * The amount of friction applied to scrolls and flings.
     *
     * @return A scalar dimensionless value representing the coefficient of
    friction.
     */
    public static float getScrollFriction() {
        return android.view.ViewConfiguration.SCROLL_FRICTION;
    }

    /**
     *
     *
     * @return the default duration in milliseconds for {@link ActionMode#hide(long)}.
     */
    public static long getDefaultActionModeHideDuration() {
        return android.view.ViewConfiguration.ACTION_MODE_HIDE_DURATION_DEFAULT;
    }

    /**
     * If a MotionEvent has {@link android.view.MotionEvent#CLASSIFICATION_AMBIGUOUS_GESTURE} set,
     * then certain actions, such as scrolling, will be inhibited.
     * However, to account for the possibility of incorrect classification,
     * the default scrolling will only be inhibited if the pointer travels less than
     * (getScaledTouchSlop() * this factor).
     * Likewise, the default long press timeout will be increased by this factor for some situations
     * where the default behaviour is to cancel it.
     *
     * @return The multiplication factor for inhibiting default gestures.
     */
    @android.annotation.FloatRange(from = 1.0)
    public static float getAmbiguousGestureMultiplier() {
        return android.view.ViewConfiguration.AMBIGUOUS_GESTURE_MULTIPLIER;
    }

    /**
     * Report if the device has a permanent menu key available to the user.
     *
     * <p>As of Android 3.0, devices may not have a permanent menu key available.
     * Apps should use the action bar to present menu options to users.
     * However, there are some apps where the action bar is inappropriate
     * or undesirable. This method may be used to detect if a menu key is present.
     * If not, applications should provide another on-screen affordance to access
     * functionality.
     *
     * @return true if a permanent menu key is present, false otherwise.
     */
    public boolean hasPermanentMenuKey() {
        return sHasPermanentMenuKey;
    }

    /**
     * Check if shortcuts should be displayed in menus.
     *
     * @return {@code True} if shortcuts should be displayed in menus.
     */
    public boolean shouldShowMenuShortcutsWhenKeyboardPresent() {
        return mShowMenuShortcutsWhenKeyboardPresent;
    }

    /**
     * Retrieves the distance in pixels between touches that must be reached for a gesture to be
     * interpreted as scaling.
     *
     * In general, scaling shouldn't start until this distance has been met or surpassed, and
     * scaling should end when the distance in pixels between touches drops below this distance.
     *
     * @return The distance in pixels
     * @throws IllegalStateException
     * 		if this method is called on a ViewConfiguration that was
     * 		instantiated using a constructor with no Context parameter.
     */
    public int getScaledMinimumScalingSpan() {
        if (!mConstructedWithContext) {
            throw new java.lang.IllegalStateException("Min scaling span cannot be determined when this " + ("method is called on a ViewConfiguration that was instantiated using a " + "constructor with no Context parameter"));
        }
        return mMinScalingSpan;
    }

    /**
     *
     *
     * @unknown 
     * @return Whether or not marquee should use fading edges.
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isFadingMarqueeEnabled() {
        return mFadingMarqueeEnabled;
    }

    /**
     *
     *
     * @return the duration in milliseconds before an end of a long press causes a tooltip to be
    hidden
     * @unknown 
     */
    @android.annotation.TestApi
    public static int getLongPressTooltipHideTimeout() {
        return android.view.ViewConfiguration.LONG_PRESS_TOOLTIP_HIDE_TIMEOUT;
    }

    /**
     *
     *
     * @return the duration in milliseconds before a hover event causes a tooltip to be shown
     * @unknown 
     */
    @android.annotation.TestApi
    public static int getHoverTooltipShowTimeout() {
        return android.view.ViewConfiguration.HOVER_TOOLTIP_SHOW_TIMEOUT;
    }

    /**
     *
     *
     * @return the duration in milliseconds before mouse inactivity causes a tooltip to be hidden
    (default variant to be used when {@link View#SYSTEM_UI_FLAG_LOW_PROFILE} is not set).
     * @unknown 
     */
    @android.annotation.TestApi
    public static int getHoverTooltipHideTimeout() {
        return android.view.ViewConfiguration.HOVER_TOOLTIP_HIDE_TIMEOUT;
    }

    /**
     *
     *
     * @return the duration in milliseconds before mouse inactivity causes a tooltip to be hidden
    (shorter variant to be used when {@link View#SYSTEM_UI_FLAG_LOW_PROFILE} is set).
     * @unknown 
     */
    @android.annotation.TestApi
    public static int getHoverTooltipHideShortTimeout() {
        return android.view.ViewConfiguration.HOVER_TOOLTIP_HIDE_SHORT_TIMEOUT;
    }
}

