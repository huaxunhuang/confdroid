/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * Describes a set of insets for window content.
 *
 * <p>WindowInsets are immutable and may be expanded to include more inset types in the future.
 * To adjust insets, use one of the supplied clone methods to obtain a new WindowInsets instance
 * with the adjusted properties.</p>
 *
 * <p>Note: Before {@link android.os.Build.VERSION_CODES#P P}, WindowInsets instances were only
 * immutable during a single layout pass (i.e. would return the same values between
 * {@link View#onApplyWindowInsets} and {@link View#onLayout}, but could return other values
 * otherwise). Starting with {@link android.os.Build.VERSION_CODES#P P}, WindowInsets are
 * always immutable and implement equality.
 *
 * @see View.OnApplyWindowInsetsListener
 * @see View#onApplyWindowInsets(WindowInsets)
 */
public final class WindowInsets {
    private final android.graphics.Insets[] mTypeInsetsMap;

    private final android.graphics.Insets[] mTypeMaxInsetsMap;

    private final boolean[] mTypeVisibilityMap;

    @android.annotation.Nullable
    private android.graphics.Rect mTempRect;

    private final boolean mIsRound;

    @android.annotation.Nullable
    private final android.view.DisplayCutout mDisplayCutout;

    /**
     * In multi-window we force show the navigation bar. Because we don't want that the surface size
     * changes in this mode, we instead have a flag whether the navigation bar size should always
     * be consumed, so the app is treated like there is no virtual navigation bar at all.
     */
    private final boolean mAlwaysConsumeSystemBars;

    private final boolean mSystemWindowInsetsConsumed;

    private final boolean mStableInsetsConsumed;

    private final boolean mDisplayCutoutConsumed;

    /**
     * Since new insets may be added in the future that existing apps couldn't
     * know about, this fully empty constant shouldn't be made available to apps
     * since it would allow them to inadvertently consume unknown insets by returning it.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static final android.view.WindowInsets CONSUMED;

    static {
        CONSUMED = new android.view.WindowInsets(((android.graphics.Rect) (null)), null, false, false, null);
    }

    /**
     * Construct a new WindowInsets from individual insets.
     *
     * A {@code null} inset indicates that the respective inset is consumed.
     *
     * @unknown 
     * @deprecated Use {@link WindowInsets(SparseArray, SparseArray, boolean, boolean, DisplayCutout)}
     */
    public WindowInsets(android.graphics.Rect systemWindowInsetsRect, android.graphics.Rect stableInsetsRect, boolean isRound, boolean alwaysConsumeSystemBars, android.view.DisplayCutout displayCutout) {
        this(android.view.WindowInsets.createCompatTypeMap(systemWindowInsetsRect), android.view.WindowInsets.createCompatTypeMap(stableInsetsRect), android.view.WindowInsets.createCompatVisibilityMap(android.view.WindowInsets.createCompatTypeMap(systemWindowInsetsRect)), isRound, alwaysConsumeSystemBars, displayCutout);
    }

    /**
     * Construct a new WindowInsets from individual insets.
     *
     * {@code typeInsetsMap} and {@code typeMaxInsetsMap} are a map of indexOf(type) -> insets that
     * contain the information what kind of system bars causes how much insets. The insets in this
     * map are non-additive; i.e. they have the same origin. In other words: If two system bars
     * overlap on one side, the insets of the larger bar will also include the insets of the smaller
     * bar.
     *
     * {@code null} type inset map indicates that the respective inset is fully consumed.
     *
     * @unknown 
     */
    public WindowInsets(@android.annotation.Nullable
    android.graphics.Insets[] typeInsetsMap, @android.annotation.Nullable
    android.graphics.Insets[] typeMaxInsetsMap, boolean[] typeVisibilityMap, boolean isRound, boolean alwaysConsumeSystemBars, android.view.DisplayCutout displayCutout) {
        mSystemWindowInsetsConsumed = typeInsetsMap == null;
        mTypeInsetsMap = (mSystemWindowInsetsConsumed) ? new android.graphics.Insets[android.view.WindowInsets.Type.SIZE] : typeInsetsMap.clone();
        mStableInsetsConsumed = typeMaxInsetsMap == null;
        mTypeMaxInsetsMap = (mStableInsetsConsumed) ? new android.graphics.Insets[android.view.WindowInsets.Type.SIZE] : typeMaxInsetsMap.clone();
        mTypeVisibilityMap = typeVisibilityMap;
        mIsRound = isRound;
        mAlwaysConsumeSystemBars = alwaysConsumeSystemBars;
        mDisplayCutoutConsumed = displayCutout == null;
        mDisplayCutout = (mDisplayCutoutConsumed || displayCutout.isEmpty()) ? null : displayCutout;
    }

    /**
     * Construct a new WindowInsets, copying all values from a source WindowInsets.
     *
     * @param src
     * 		Source to copy insets from
     */
    public WindowInsets(android.view.WindowInsets src) {
        this(src.mSystemWindowInsetsConsumed ? null : src.mTypeInsetsMap, src.mStableInsetsConsumed ? null : src.mTypeMaxInsetsMap, src.mTypeVisibilityMap, src.mIsRound, src.mAlwaysConsumeSystemBars, android.view.WindowInsets.displayCutoutCopyConstructorArgument(src));
    }

    private static android.view.DisplayCutout displayCutoutCopyConstructorArgument(android.view.WindowInsets w) {
        if (w.mDisplayCutoutConsumed) {
            return null;
        } else
            if (w.mDisplayCutout == null) {
                return android.view.DisplayCutout.NO_CUTOUT;
            } else {
                return w.mDisplayCutout;
            }

    }

    /**
     *
     *
     * @return The insets that include system bars indicated by {@code typeMask}, taken from
    {@code typeInsetMap}.
     */
    private static android.graphics.Insets getInsets(android.graphics.Insets[] typeInsetsMap, @android.view.WindowInsets.Type.InsetType
    int typeMask) {
        android.graphics.Insets result = null;
        for (int i = android.view.WindowInsets.Type.FIRST; i <= android.view.WindowInsets.Type.LAST; i = i << 1) {
            if ((typeMask & i) == 0) {
                continue;
            }
            android.graphics.Insets insets = typeInsetsMap[android.view.WindowInsets.Type.indexOf(i)];
            if (insets == null) {
                continue;
            }
            if (result == null) {
                result = insets;
            } else {
                result = android.graphics.Insets.max(result, insets);
            }
        }
        return result == null ? android.graphics.Insets.NONE : result;
    }

    /**
     * Sets all entries in {@code typeInsetsMap} that belong to {@code typeMask} to {@code insets},
     */
    private static void setInsets(android.graphics.Insets[] typeInsetsMap, @android.view.WindowInsets.Type.InsetType
    int typeMask, android.graphics.Insets insets) {
        for (int i = android.view.WindowInsets.Type.FIRST; i <= android.view.WindowInsets.Type.LAST; i = i << 1) {
            if ((typeMask & i) == 0) {
                continue;
            }
            typeInsetsMap[android.view.WindowInsets.Type.indexOf(i)] = insets;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public WindowInsets(android.graphics.Rect systemWindowInsets) {
        this(android.view.WindowInsets.createCompatTypeMap(systemWindowInsets), null, new boolean[android.view.WindowInsets.Type.SIZE], false, false, null);
    }

    /**
     * Creates a indexOf(type) -> inset map for which the {@code insets} is just mapped to
     * {@link InsetType#topBar()} and {@link InsetType#sideBars()}, depending on the location of the
     * inset.
     */
    private static android.graphics.Insets[] createCompatTypeMap(@android.annotation.Nullable
    android.graphics.Rect insets) {
        if (insets == null) {
            return null;
        }
        android.graphics.Insets[] typeInsetMap = new android.graphics.Insets[android.view.WindowInsets.Type.SIZE];
        android.view.WindowInsets.assignCompatInsets(typeInsetMap, insets);
        return typeInsetMap;
    }

    /**
     *
     *
     * @unknown 
     */
    static void assignCompatInsets(android.graphics.Insets[] typeInsetMap, android.graphics.Rect insets) {
        typeInsetMap[android.view.WindowInsets.Type.indexOf(android.view.WindowInsets.Type.TOP_BAR)] = android.graphics.Insets.of(0, insets.top, 0, 0);
        typeInsetMap[android.view.WindowInsets.Type.indexOf(android.view.WindowInsets.Type.SIDE_BARS)] = android.graphics.Insets.of(insets.left, 0, insets.right, insets.bottom);
    }

    private static boolean[] createCompatVisibilityMap(@android.annotation.Nullable
    android.graphics.Insets[] typeInsetMap) {
        boolean[] typeVisibilityMap = new boolean[android.view.WindowInsets.Type.SIZE];
        if (typeInsetMap == null) {
            return typeVisibilityMap;
        }
        for (int i = android.view.WindowInsets.Type.FIRST; i <= android.view.WindowInsets.Type.LAST; i = i << 1) {
            int index = android.view.WindowInsets.Type.indexOf(i);
            if (!android.graphics.Insets.NONE.equals(typeInsetMap[index])) {
                typeVisibilityMap[index] = true;
            }
        }
        return typeVisibilityMap;
    }

    /**
     * Used to provide a safe copy of the system window insets to pass through
     * to the existing fitSystemWindows method and other similar internals.
     *
     * @unknown 
     * @deprecated use {@link #getSystemWindowInsets()} instead.
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public android.graphics.Rect getSystemWindowInsetsAsRect() {
        if (mTempRect == null) {
            mTempRect = new android.graphics.Rect();
        }
        android.graphics.Insets insets = getSystemWindowInsets();
        mTempRect.set(insets.left, insets.top, insets.right, insets.bottom);
        return mTempRect;
    }

    /**
     * Returns the system window insets in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The system window insets
     */
    @android.annotation.NonNull
    public android.graphics.Insets getSystemWindowInsets() {
        return android.view.WindowInsets.getInsets(mTypeInsetsMap, android.view.WindowInsets.Type.compatSystemInsets());
    }

    /**
     * Returns the insets of a specific set of windows causing insets, denoted by the
     * {@code typeMask} bit mask of {@link InsetType}s.
     *
     * @param typeMask
     * 		Bit mask of {@link InsetType}s to query the insets for.
     * @return The insets.
     * @unknown pending unhide
     */
    public android.graphics.Insets getInsets(@android.view.WindowInsets.Type.InsetType
    int typeMask) {
        return android.view.WindowInsets.getInsets(mTypeInsetsMap, typeMask);
    }

    /**
     * Returns the maximum amount of insets a specific set of windows can cause, denoted by the
     * {@code typeMask} bit mask of {@link InsetType}s.
     *
     * <p>The maximum insets represents the area of a a window that that <b>may</b> be partially
     * or fully obscured by the system window identified by {@code type}. This value does not
     * change based on the visibility state of those elements. for example, if the status bar is
     * normally shown, but temporarily hidden, the maximum inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @param typeMask
     * 		Bit mask of {@link InsetType}s to query the insets for.
     * @return The insets.
     * @throws IllegalArgumentException
     * 		If the caller tries to query {@link Type#ime()}. Maximum
     * 		insets are not available for this type as the height of the
     * 		IME is dynamic depending on the {@link EditorInfo} of the
     * 		currently focused view, as well as the UI state of the IME.
     * @unknown pending unhide
     */
    public android.graphics.Insets getMaxInsets(@android.view.WindowInsets.Type.InsetType
    int typeMask) throws java.lang.IllegalArgumentException {
        if ((typeMask & android.view.WindowInsets.Type.IME) != 0) {
            throw new java.lang.IllegalArgumentException("Unable to query the maximum insets for IME");
        }
        return android.view.WindowInsets.getInsets(mTypeMaxInsetsMap, typeMask);
    }

    /**
     * Returns whether a set of windows that may cause insets is currently visible on screen,
     * regardless of whether it actually overlaps with this window.
     *
     * @param typeMask
     * 		Bit mask of {@link InsetType}s to query visibility status.
     * @return {@code true} if and only if all windows included in {@code typeMask} are currently
    visible on screen.
     * @unknown pending unhide
     */
    public boolean isVisible(@android.view.WindowInsets.Type.InsetType
    int typeMask) {
        for (int i = android.view.WindowInsets.Type.FIRST; i <= android.view.WindowInsets.Type.LAST; i = i << 1) {
            if ((typeMask & i) == 0) {
                continue;
            }
            if (!mTypeVisibilityMap[android.view.WindowInsets.Type.indexOf(i)]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the left system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The left system window inset
     */
    public int getSystemWindowInsetLeft() {
        return getSystemWindowInsets().left;
    }

    /**
     * Returns the top system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The top system window inset
     */
    public int getSystemWindowInsetTop() {
        return getSystemWindowInsets().top;
    }

    /**
     * Returns the right system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The right system window inset
     */
    public int getSystemWindowInsetRight() {
        return getSystemWindowInsets().right;
    }

    /**
     * Returns the bottom system window inset in pixels.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return The bottom system window inset
     */
    public int getSystemWindowInsetBottom() {
        return getSystemWindowInsets().bottom;
    }

    /**
     * Returns true if this WindowInsets has nonzero system window insets.
     *
     * <p>The system window inset represents the area of a full-screen window that is
     * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
     * </p>
     *
     * @return true if any of the system window inset values are nonzero
     */
    public boolean hasSystemWindowInsets() {
        return !getSystemWindowInsets().equals(android.graphics.Insets.NONE);
    }

    /**
     * Returns true if this WindowInsets has any nonzero insets.
     *
     * @return true if any inset values are nonzero
     */
    public boolean hasInsets() {
        return ((!android.view.WindowInsets.getInsets(mTypeInsetsMap, android.view.WindowInsets.Type.all()).equals(android.graphics.Insets.NONE)) || (!android.view.WindowInsets.getInsets(mTypeMaxInsetsMap, android.view.WindowInsets.Type.all()).equals(android.graphics.Insets.NONE))) || (mDisplayCutout != null);
    }

    /**
     * Returns the display cutout if there is one.
     *
     * @return the display cutout or null if there is none
     * @see DisplayCutout
     */
    @android.annotation.Nullable
    public android.view.DisplayCutout getDisplayCutout() {
        return mDisplayCutout;
    }

    /**
     * Returns a copy of this WindowInsets with the cutout fully consumed.
     *
     * @return A modified copy of this WindowInsets
     */
    @android.annotation.NonNull
    public android.view.WindowInsets consumeDisplayCutout() {
        return /* displayCutout */
        new android.view.WindowInsets(mSystemWindowInsetsConsumed ? null : mTypeInsetsMap, mStableInsetsConsumed ? null : mTypeMaxInsetsMap, mTypeVisibilityMap, mIsRound, mAlwaysConsumeSystemBars, null);
    }

    /**
     * Check if these insets have been fully consumed.
     *
     * <p>Insets are considered "consumed" if the applicable <code>consume*</code> methods
     * have been called such that all insets have been set to zero. This affects propagation of
     * insets through the view hierarchy; insets that have not been fully consumed will continue
     * to propagate down to child views.</p>
     *
     * <p>The result of this method is equivalent to the return value of
     * {@link View#fitSystemWindows(android.graphics.Rect)}.</p>
     *
     * @return true if the insets have been fully consumed.
     */
    public boolean isConsumed() {
        return (mSystemWindowInsetsConsumed && mStableInsetsConsumed) && mDisplayCutoutConsumed;
    }

    /**
     * Returns true if the associated window has a round shape.
     *
     * <p>A round window's left, top, right and bottom edges reach all the way to the
     * associated edges of the window but the corners may not be visible. Views responding
     * to round insets should take care to not lay out critical elements within the corners
     * where they may not be accessible.</p>
     *
     * @return True if the window is round
     */
    public boolean isRound() {
        return mIsRound;
    }

    /**
     * Returns a copy of this WindowInsets with the system window insets fully consumed.
     *
     * @return A modified copy of this WindowInsets
     */
    @android.annotation.NonNull
    public android.view.WindowInsets consumeSystemWindowInsets() {
        return new android.view.WindowInsets(null, mStableInsetsConsumed ? null : mTypeMaxInsetsMap, mTypeVisibilityMap, mIsRound, mAlwaysConsumeSystemBars, android.view.WindowInsets.displayCutoutCopyConstructorArgument(this));
    }

    // TODO(b/119190588): replace @code with @link below
    /**
     * Returns a copy of this WindowInsets with selected system window insets replaced
     * with new values.
     *
     * <p>Note: If the system window insets are already consumed, this method will return them
     * unchanged on {@link android.os.Build.VERSION_CODES#Q Q} and later. Prior to
     * {@link android.os.Build.VERSION_CODES#Q Q}, the new values were applied regardless of
     * whether they were consumed, and this method returns invalid non-zero consumed insets.
     *
     * @param left
     * 		New left inset in pixels
     * @param top
     * 		New top inset in pixels
     * @param right
     * 		New right inset in pixels
     * @param bottom
     * 		New bottom inset in pixels
     * @return A modified copy of this WindowInsets
     * @deprecated use {@code Builder#Builder(WindowInsets)} with
    {@link Builder#setSystemWindowInsets(Insets)} instead.
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public android.view.WindowInsets replaceSystemWindowInsets(int left, int top, int right, int bottom) {
        // Compat edge case: what should this do if the insets have already been consumed?
        // On platforms prior to Q, the behavior was to override the insets with non-zero values,
        // but leave them consumed, which is invalid (consumed insets must be zero).
        // The behavior is now keeping them consumed and discarding the new insets.
        if (mSystemWindowInsetsConsumed) {
            return this;
        }
        return new android.view.WindowInsets.Builder(this).setSystemWindowInsets(android.graphics.Insets.of(left, top, right, bottom)).build();
    }

    // TODO(b/119190588): replace @code with @link below
    /**
     * Returns a copy of this WindowInsets with selected system window insets replaced
     * with new values.
     *
     * <p>Note: If the system window insets are already consumed, this method will return them
     * unchanged on {@link android.os.Build.VERSION_CODES#Q Q} and later. Prior to
     * {@link android.os.Build.VERSION_CODES#Q Q}, the new values were applied regardless of
     * whether they were consumed, and this method returns invalid non-zero consumed insets.
     *
     * @param systemWindowInsets
     * 		New system window insets. Each field is the inset in pixels
     * 		for that edge
     * @return A modified copy of this WindowInsets
     * @deprecated use {@code Builder#Builder(WindowInsets)} with
    {@link Builder#setSystemWindowInsets(Insets)} instead.
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public android.view.WindowInsets replaceSystemWindowInsets(android.graphics.Rect systemWindowInsets) {
        return replaceSystemWindowInsets(systemWindowInsets.left, systemWindowInsets.top, systemWindowInsets.right, systemWindowInsets.bottom);
    }

    /**
     * Returns the stable insets in pixels.
     *
     * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
     * partially or fully obscured by the system UI elements.  This value does not change
     * based on the visibility state of those elements; for example, if the status bar is
     * normally shown, but temporarily hidden, the stable inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @return The stable insets
     */
    @android.annotation.NonNull
    public android.graphics.Insets getStableInsets() {
        return android.view.WindowInsets.getInsets(mTypeMaxInsetsMap, android.view.WindowInsets.Type.compatSystemInsets());
    }

    /**
     * Returns the top stable inset in pixels.
     *
     * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
     * partially or fully obscured by the system UI elements.  This value does not change
     * based on the visibility state of those elements; for example, if the status bar is
     * normally shown, but temporarily hidden, the stable inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @return The top stable inset
     */
    public int getStableInsetTop() {
        return getStableInsets().top;
    }

    /**
     * Returns the left stable inset in pixels.
     *
     * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
     * partially or fully obscured by the system UI elements.  This value does not change
     * based on the visibility state of those elements; for example, if the status bar is
     * normally shown, but temporarily hidden, the stable inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @return The left stable inset
     */
    public int getStableInsetLeft() {
        return getStableInsets().left;
    }

    /**
     * Returns the right stable inset in pixels.
     *
     * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
     * partially or fully obscured by the system UI elements.  This value does not change
     * based on the visibility state of those elements; for example, if the status bar is
     * normally shown, but temporarily hidden, the stable inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @return The right stable inset
     */
    public int getStableInsetRight() {
        return getStableInsets().right;
    }

    /**
     * Returns the bottom stable inset in pixels.
     *
     * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
     * partially or fully obscured by the system UI elements.  This value does not change
     * based on the visibility state of those elements; for example, if the status bar is
     * normally shown, but temporarily hidden, the stable inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @return The bottom stable inset
     */
    public int getStableInsetBottom() {
        return getStableInsets().bottom;
    }

    /**
     * Returns true if this WindowInsets has nonzero stable insets.
     *
     * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
     * partially or fully obscured by the system UI elements.  This value does not change
     * based on the visibility state of those elements; for example, if the status bar is
     * normally shown, but temporarily hidden, the stable inset will still provide the inset
     * associated with the status bar being shown.</p>
     *
     * @return true if any of the stable inset values are nonzero
     */
    public boolean hasStableInsets() {
        return !getStableInsets().equals(android.graphics.Insets.NONE);
    }

    /**
     * Returns the system gesture insets.
     *
     * <p>The system gesture insets represent the area of a window where system gestures have
     * priority and may consume some or all touch input, e.g. due to the a system bar
     * occupying it, or it being reserved for touch-only gestures.
     *
     * <p>An app can declare priority over system gestures with
     * {@link View#setSystemGestureExclusionRects} outside of the
     * {@link #getMandatorySystemGestureInsets() mandatory system gesture insets}.
     *
     * <p>Simple taps are guaranteed to reach the window even within the system gesture insets,
     * as long as they are outside the {@link #getTappableElementInsets() system window insets}.
     *
     * <p>When {@link View#SYSTEM_UI_FLAG_LAYOUT_STABLE} is requested, an inset will be returned
     * even when the system gestures are inactive due to
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN} or
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION}.
     *
     * <p>This inset is consumed together with the {@link #getSystemWindowInsets()
     * system window insets} by {@link #consumeSystemWindowInsets()}.
     *
     * @see #getMandatorySystemGestureInsets
     */
    @android.annotation.NonNull
    public android.graphics.Insets getSystemGestureInsets() {
        return android.view.WindowInsets.getInsets(mTypeInsetsMap, android.view.WindowInsets.Type.SYSTEM_GESTURES);
    }

    /**
     * Returns the mandatory system gesture insets.
     *
     * <p>The mandatory system gesture insets represent the area of a window where mandatory system
     * gestures have priority and may consume some or all touch input, e.g. due to the a system bar
     * occupying it, or it being reserved for touch-only gestures.
     *
     * <p>In contrast to {@link #getSystemGestureInsets regular system gestures}, <b>mandatory</b>
     * system gestures cannot be overriden by {@link View#setSystemGestureExclusionRects}.
     *
     * <p>Simple taps are guaranteed to reach the window even within the system gesture insets,
     * as long as they are outside the {@link #getTappableElementInsets() system window insets}.
     *
     * <p>When {@link View#SYSTEM_UI_FLAG_LAYOUT_STABLE} is requested, an inset will be returned
     * even when the system gestures are inactive due to
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN} or
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION}.
     *
     * <p>This inset is consumed together with the {@link #getSystemWindowInsets()
     * system window insets} by {@link #consumeSystemWindowInsets()}.
     *
     * @see #getSystemGestureInsets
     */
    @android.annotation.NonNull
    public android.graphics.Insets getMandatorySystemGestureInsets() {
        return android.view.WindowInsets.getInsets(mTypeInsetsMap, android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES);
    }

    /**
     * Returns the tappable element insets.
     *
     * <p>The tappable element insets represent how much tappable elements <b>must at least</b> be
     * inset to remain both tappable and visually unobstructed by persistent system windows.
     *
     * <p>This may be smaller than {@link #getSystemWindowInsets()} if the system window is
     * largely transparent and lets through simple taps (but not necessarily more complex gestures).
     *
     * <p>Note that generally, tappable elements <strong>should</strong> be aligned with the
     * {@link #getSystemWindowInsets() system window insets} instead to avoid overlapping with the
     * system bars.
     *
     * <p>When {@link View#SYSTEM_UI_FLAG_LAYOUT_STABLE} is requested, an inset will be returned
     * even when the area covered by the inset would be tappable due to
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN} or
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION}.
     *
     * <p>This inset is consumed together with the {@link #getSystemWindowInsets()
     * system window insets} by {@link #consumeSystemWindowInsets()}.
     */
    @android.annotation.NonNull
    public android.graphics.Insets getTappableElementInsets() {
        return android.view.WindowInsets.getInsets(mTypeInsetsMap, android.view.WindowInsets.Type.TAPPABLE_ELEMENT);
    }

    /**
     * Returns a copy of this WindowInsets with the stable insets fully consumed.
     *
     * @return A modified copy of this WindowInsets
     */
    @android.annotation.NonNull
    public android.view.WindowInsets consumeStableInsets() {
        return new android.view.WindowInsets(mSystemWindowInsetsConsumed ? null : mTypeInsetsMap, null, mTypeVisibilityMap, mIsRound, mAlwaysConsumeSystemBars, android.view.WindowInsets.displayCutoutCopyConstructorArgument(this));
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean shouldAlwaysConsumeSystemBars() {
        return mAlwaysConsumeSystemBars;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((("WindowInsets{systemWindowInsets=" + getSystemWindowInsets()) + " stableInsets=") + getStableInsets()) + " sysGestureInsets=") + getSystemGestureInsets()) + (mDisplayCutout != null ? " cutout=" + mDisplayCutout : "")) + (isRound() ? " round" : "")) + "}";
    }

    /**
     * Returns a copy of this instance inset in the given directions.
     *
     * @see #inset(int, int, int, int)
     * @deprecated use {@link #inset(Insets)}
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public android.view.WindowInsets inset(android.graphics.Rect r) {
        return inset(r.left, r.top, r.right, r.bottom);
    }

    /**
     * Returns a copy of this instance inset in the given directions.
     *
     * @see #inset(int, int, int, int)
     * @unknown 
     */
    @android.annotation.NonNull
    public android.view.WindowInsets inset(android.graphics.Insets insets) {
        return inset(insets.left, insets.top, insets.right, insets.bottom);
    }

    /**
     * Returns a copy of this instance inset in the given directions.
     *
     * This is intended for dispatching insets to areas of the window that are smaller than the
     * current area.
     *
     * <p>Example:
     * <pre>
     * childView.dispatchApplyWindowInsets(insets.inset(
     *         childMarginLeft, childMarginTop, childMarginBottom, childMarginRight));
     * </pre>
     *
     * @param left
     * 		the amount of insets to remove from the left. Must be non-negative.
     * @param top
     * 		the amount of insets to remove from the top. Must be non-negative.
     * @param right
     * 		the amount of insets to remove from the right. Must be non-negative.
     * @param bottom
     * 		the amount of insets to remove from the bottom. Must be non-negative.
     * @return the inset insets
     */
    @android.annotation.NonNull
    public android.view.WindowInsets inset(@android.annotation.IntRange(from = 0)
    int left, @android.annotation.IntRange(from = 0)
    int top, @android.annotation.IntRange(from = 0)
    int right, @android.annotation.IntRange(from = 0)
    int bottom) {
        com.android.internal.util.Preconditions.checkArgumentNonnegative(left);
        com.android.internal.util.Preconditions.checkArgumentNonnegative(top);
        com.android.internal.util.Preconditions.checkArgumentNonnegative(right);
        com.android.internal.util.Preconditions.checkArgumentNonnegative(bottom);
        return new android.view.WindowInsets(mSystemWindowInsetsConsumed ? null : android.view.WindowInsets.insetInsets(mTypeInsetsMap, left, top, right, bottom), mStableInsetsConsumed ? null : android.view.WindowInsets.insetInsets(mTypeMaxInsetsMap, left, top, right, bottom), mTypeVisibilityMap, mIsRound, mAlwaysConsumeSystemBars, mDisplayCutoutConsumed ? null : mDisplayCutout == null ? android.view.DisplayCutout.NO_CUTOUT : mDisplayCutout.inset(left, top, right, bottom));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (!(o instanceof android.view.WindowInsets)))
            return false;

        android.view.WindowInsets that = ((android.view.WindowInsets) (o));
        return ((((((((mIsRound == that.mIsRound) && (mAlwaysConsumeSystemBars == that.mAlwaysConsumeSystemBars)) && (mSystemWindowInsetsConsumed == that.mSystemWindowInsetsConsumed)) && (mStableInsetsConsumed == that.mStableInsetsConsumed)) && (mDisplayCutoutConsumed == that.mDisplayCutoutConsumed)) && java.util.Arrays.equals(mTypeInsetsMap, that.mTypeInsetsMap)) && java.util.Arrays.equals(mTypeMaxInsetsMap, that.mTypeMaxInsetsMap)) && java.util.Arrays.equals(mTypeVisibilityMap, that.mTypeVisibilityMap)) && java.util.Objects.equals(mDisplayCutout, that.mDisplayCutout);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(java.util.Arrays.hashCode(mTypeInsetsMap), java.util.Arrays.hashCode(mTypeMaxInsetsMap), java.util.Arrays.hashCode(mTypeVisibilityMap), mIsRound, mDisplayCutout, mAlwaysConsumeSystemBars, mSystemWindowInsetsConsumed, mStableInsetsConsumed, mDisplayCutoutConsumed);
    }

    /**
     * Insets every inset in {@code typeInsetsMap} by the specified left, top, right, bottom.
     *
     * @return {@code typeInsetsMap} if no inset was modified; a copy of the map with the modified
    insets otherwise.
     */
    private static android.graphics.Insets[] insetInsets(android.graphics.Insets[] typeInsetsMap, int left, int top, int right, int bottom) {
        boolean cloned = false;
        for (int i = 0; i < android.view.WindowInsets.Type.SIZE; i++) {
            android.graphics.Insets insets = typeInsetsMap[i];
            if (insets == null) {
                continue;
            }
            android.graphics.Insets insetInsets = android.view.WindowInsets.insetInsets(insets, left, top, right, bottom);
            if (insetInsets != insets) {
                if (!cloned) {
                    typeInsetsMap = typeInsetsMap.clone();
                    cloned = true;
                }
                typeInsetsMap[i] = insetInsets;
            }
        }
        return typeInsetsMap;
    }

    private static android.graphics.Insets insetInsets(android.graphics.Insets insets, int left, int top, int right, int bottom) {
        int newLeft = java.lang.Math.max(0, insets.left - left);
        int newTop = java.lang.Math.max(0, insets.top - top);
        int newRight = java.lang.Math.max(0, insets.right - right);
        int newBottom = java.lang.Math.max(0, insets.bottom - bottom);
        if ((((newLeft == left) && (newTop == top)) && (newRight == right)) && (newBottom == bottom)) {
            return insets;
        }
        return android.graphics.Insets.of(newLeft, newTop, newRight, newBottom);
    }

    /**
     *
     *
     * @return whether system window insets have been consumed.
     */
    boolean isSystemWindowInsetsConsumed() {
        return mSystemWindowInsetsConsumed;
    }

    /**
     * Builder for WindowInsets.
     */
    public static final class Builder {
        private final android.graphics.Insets[] mTypeInsetsMap;

        private final android.graphics.Insets[] mTypeMaxInsetsMap;

        private final boolean[] mTypeVisibilityMap;

        private boolean mSystemInsetsConsumed = true;

        private boolean mStableInsetsConsumed = true;

        private android.view.DisplayCutout mDisplayCutout;

        private boolean mIsRound;

        private boolean mAlwaysConsumeSystemBars;

        /**
         * Creates a builder where all insets are initially consumed.
         */
        public Builder() {
            mTypeInsetsMap = new android.graphics.Insets[android.view.WindowInsets.Type.SIZE];
            mTypeMaxInsetsMap = new android.graphics.Insets[android.view.WindowInsets.Type.SIZE];
            mTypeVisibilityMap = new boolean[android.view.WindowInsets.Type.SIZE];
        }

        /**
         * Creates a builder where all insets are initialized from {@link WindowInsets}.
         *
         * @param insets
         * 		the instance to initialize from.
         */
        public Builder(@android.annotation.NonNull
        android.view.WindowInsets insets) {
            mTypeInsetsMap = insets.mTypeInsetsMap.clone();
            mTypeMaxInsetsMap = insets.mTypeMaxInsetsMap.clone();
            mTypeVisibilityMap = insets.mTypeVisibilityMap.clone();
            mSystemInsetsConsumed = insets.mSystemWindowInsetsConsumed;
            mStableInsetsConsumed = insets.mStableInsetsConsumed;
            mDisplayCutout = android.view.WindowInsets.displayCutoutCopyConstructorArgument(insets);
            mIsRound = insets.mIsRound;
            mAlwaysConsumeSystemBars = insets.mAlwaysConsumeSystemBars;
        }

        /**
         * Sets system window insets in pixels.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system
         * windows.</p>
         *
         * @see #getSystemWindowInsets()
         * @return itself
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setSystemWindowInsets(@android.annotation.NonNull
        android.graphics.Insets systemWindowInsets) {
            com.android.internal.util.Preconditions.checkNotNull(systemWindowInsets);
            android.view.WindowInsets.assignCompatInsets(mTypeInsetsMap, systemWindowInsets.toRect());
            mSystemInsetsConsumed = false;
            return this;
        }

        /**
         * Sets system gesture insets in pixels.
         *
         * <p>The system gesture insets represent the area of a window where system gestures have
         * priority and may consume some or all touch input, e.g. due to the a system bar
         * occupying it, or it being reserved for touch-only gestures.
         *
         * @see #getSystemGestureInsets()
         * @return itself
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setSystemGestureInsets(@android.annotation.NonNull
        android.graphics.Insets insets) {
            android.view.WindowInsets.setInsets(mTypeInsetsMap, android.view.WindowInsets.Type.SYSTEM_GESTURES, insets);
            return this;
        }

        /**
         * Sets mandatory system gesture insets in pixels.
         *
         * <p>The mandatory system gesture insets represent the area of a window where mandatory
         * system gestures have priority and may consume some or all touch input, e.g. due to the a
         * system bar occupying it, or it being reserved for touch-only gestures.
         *
         * <p>In contrast to {@link #setSystemGestureInsets regular system gestures},
         * <b>mandatory</b> system gestures cannot be overriden by
         * {@link View#setSystemGestureExclusionRects}.
         *
         * @see #getMandatorySystemGestureInsets()
         * @return itself
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setMandatorySystemGestureInsets(@android.annotation.NonNull
        android.graphics.Insets insets) {
            android.view.WindowInsets.setInsets(mTypeInsetsMap, android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES, insets);
            return this;
        }

        /**
         * Sets tappable element insets in pixels.
         *
         * <p>The tappable element insets represent how much tappable elements <b>must at least</b>
         * be inset to remain both tappable and visually unobstructed by persistent system windows.
         *
         * @see #getTappableElementInsets()
         * @return itself
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setTappableElementInsets(@android.annotation.NonNull
        android.graphics.Insets insets) {
            android.view.WindowInsets.setInsets(mTypeInsetsMap, android.view.WindowInsets.Type.TAPPABLE_ELEMENT, insets);
            return this;
        }

        /**
         * Sets the insets of a specific window type in pixels.
         *
         * <p>The insets represents the area of a a window that is partially or fully obscured by
         * the system windows identified by {@code typeMask}.
         * </p>
         *
         * @see #getInsets(int)
         * @param typeMask
         * 		The bitmask of {@link InsetType} to set the insets for.
         * @param insets
         * 		The insets to set.
         * @return itself
         * @unknown pending unhide
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setInsets(@android.view.WindowInsets.Type.InsetType
        int typeMask, @android.annotation.NonNull
        android.graphics.Insets insets) {
            com.android.internal.util.Preconditions.checkNotNull(insets);
            android.view.WindowInsets.setInsets(mTypeInsetsMap, typeMask, insets);
            mSystemInsetsConsumed = false;
            return this;
        }

        /**
         * Sets the maximum amount of insets a specific window type in pixels.
         *
         * <p>The maximum insets represents the area of a a window that that <b>may</b> be partially
         * or fully obscured by the system windows identified by {@code typeMask}. This value does
         * not change based on the visibility state of those elements. for example, if the status
         * bar is normally shown, but temporarily hidden, the maximum inset will still provide the
         * inset associated with the status bar being shown.</p>
         *
         * @see #getMaxInsets(int)
         * @param typeMask
         * 		The bitmask of {@link InsetType} to set the insets for.
         * @param insets
         * 		The insets to set.
         * @return itself
         * @throws IllegalArgumentException
         * 		If {@code typeMask} contains {@link Type#ime()}. Maximum
         * 		insets are not available for this type as the height of
         * 		the IME is dynamic depending on the {@link EditorInfo}
         * 		of the currently focused view, as well as the UI
         * 		state of the IME.
         * @unknown pending unhide
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setMaxInsets(@android.view.WindowInsets.Type.InsetType
        int typeMask, @android.annotation.NonNull
        android.graphics.Insets insets) throws java.lang.IllegalArgumentException {
            if (typeMask == android.view.WindowInsets.Type.IME) {
                throw new java.lang.IllegalArgumentException("Maximum inset not available for IME");
            }
            com.android.internal.util.Preconditions.checkNotNull(insets);
            android.view.WindowInsets.setInsets(mTypeMaxInsetsMap, typeMask, insets);
            mStableInsetsConsumed = false;
            return this;
        }

        /**
         * Sets whether windows that can cause insets are currently visible on screen.
         *
         * @see #isVisible(int)
         * @param typeMask
         * 		The bitmask of {@link InsetType} to set the visibility for.
         * @param visible
         * 		Whether to mark the windows as visible or not.
         * @return itself
         * @unknown pending unhide
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setVisible(@android.view.WindowInsets.Type.InsetType
        int typeMask, boolean visible) {
            for (int i = android.view.WindowInsets.Type.FIRST; i <= android.view.WindowInsets.Type.LAST; i = i << 1) {
                if ((typeMask & i) == 0) {
                    continue;
                }
                mTypeVisibilityMap[android.view.WindowInsets.Type.indexOf(i)] = visible;
            }
            return this;
        }

        /**
         * Sets the stable insets in pixels.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * @see #getStableInsets()
         * @return itself
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setStableInsets(@android.annotation.NonNull
        android.graphics.Insets stableInsets) {
            com.android.internal.util.Preconditions.checkNotNull(stableInsets);
            android.view.WindowInsets.assignCompatInsets(mTypeMaxInsetsMap, stableInsets.toRect());
            mStableInsetsConsumed = false;
            return this;
        }

        /**
         * Sets the display cutout.
         *
         * @see #getDisplayCutout()
         * @param displayCutout
         * 		the display cutout or null if there is none
         * @return itself
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setDisplayCutout(@android.annotation.Nullable
        android.view.DisplayCutout displayCutout) {
            mDisplayCutout = (displayCutout != null) ? displayCutout : android.view.DisplayCutout.NO_CUTOUT;
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setRound(boolean round) {
            mIsRound = round;
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.WindowInsets.Builder setAlwaysConsumeSystemBars(boolean alwaysConsumeSystemBars) {
            mAlwaysConsumeSystemBars = alwaysConsumeSystemBars;
            return this;
        }

        /**
         * Builds a {@link WindowInsets} instance.
         *
         * @return the {@link WindowInsets} instance.
         */
        @android.annotation.NonNull
        public android.view.WindowInsets build() {
            return new android.view.WindowInsets(mSystemInsetsConsumed ? null : mTypeInsetsMap, mStableInsetsConsumed ? null : mTypeMaxInsetsMap, mTypeVisibilityMap, mIsRound, mAlwaysConsumeSystemBars, mDisplayCutout);
        }
    }

    /**
     * Class that defines different types of sources causing window insets.
     *
     * @unknown pending unhide
     */
    public static final class Type {
        static final int FIRST = 1 << 0;

        static final int TOP_BAR = android.view.WindowInsets.Type.FIRST;

        static final int IME = 1 << 1;

        static final int SIDE_BARS = 1 << 2;

        static final int SYSTEM_GESTURES = 1 << 3;

        static final int MANDATORY_SYSTEM_GESTURES = 1 << 4;

        static final int TAPPABLE_ELEMENT = 1 << 5;

        static final int LAST = 1 << 6;

        static final int SIZE = 7;

        static final int WINDOW_DECOR = android.view.WindowInsets.Type.LAST;

        static int indexOf(@android.view.WindowInsets.Type.InsetType
        int type) {
            switch (type) {
                case android.view.WindowInsets.Type.TOP_BAR :
                    return 0;
                case android.view.WindowInsets.Type.IME :
                    return 1;
                case android.view.WindowInsets.Type.SIDE_BARS :
                    return 2;
                case android.view.WindowInsets.Type.SYSTEM_GESTURES :
                    return 3;
                case android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES :
                    return 4;
                case android.view.WindowInsets.Type.TAPPABLE_ELEMENT :
                    return 5;
                case android.view.WindowInsets.Type.WINDOW_DECOR :
                    return 6;
                default :
                    throw new java.lang.IllegalArgumentException(("type needs to be >= FIRST and <= LAST," + " type=") + type);
            }
        }

        private Type() {
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef(flag = true, value = { android.view.WindowInsets.Type.TOP_BAR, android.view.WindowInsets.Type.IME, android.view.WindowInsets.Type.SIDE_BARS, android.view.WindowInsets.Type.WINDOW_DECOR, android.view.WindowInsets.Type.SYSTEM_GESTURES, android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES, android.view.WindowInsets.Type.TAPPABLE_ELEMENT })
        public @interface InsetType {}

        /**
         *
         *
         * @return An inset type representing the top bar of a window, which can be the status
        bar on handheld-like devices as well as a caption bar.
         */
        @android.view.WindowInsets.Type.InsetType
        public static int topBar() {
            return android.view.WindowInsets.Type.TOP_BAR;
        }

        /**
         *
         *
         * @return An inset type representing the window of an {@link InputMethod}.
         */
        @android.view.WindowInsets.Type.InsetType
        public static int ime() {
            return android.view.WindowInsets.Type.IME;
        }

        /**
         *
         *
         * @return An inset type representing any system bars that are not {@link #topBar()}.
         */
        @android.view.WindowInsets.Type.InsetType
        public static int sideBars() {
            return android.view.WindowInsets.Type.SIDE_BARS;
        }

        /**
         *
         *
         * @return An inset type representing decor that is being app-controlled.
         */
        @android.view.WindowInsets.Type.InsetType
        public static int windowDecor() {
            return android.view.WindowInsets.Type.WINDOW_DECOR;
        }

        /**
         * Returns an inset type representing the system gesture insets.
         *
         * <p>The system gesture insets represent the area of a window where system gestures have
         * priority and may consume some or all touch input, e.g. due to the a system bar
         * occupying it, or it being reserved for touch-only gestures.
         *
         * <p>Simple taps are guaranteed to reach the window even within the system gesture insets,
         * as long as they are outside the {@link #getSystemWindowInsets() system window insets}.
         *
         * <p>When {@link View#SYSTEM_UI_FLAG_LAYOUT_STABLE} is requested, an inset will be returned
         * even when the system gestures are inactive due to
         * {@link View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN} or
         * {@link View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION}.
         *
         * @see #getSystemGestureInsets()
         */
        @android.view.WindowInsets.Type.InsetType
        public static int systemGestures() {
            return android.view.WindowInsets.Type.SYSTEM_GESTURES;
        }

        /**
         *
         *
         * @see #getMandatorySystemGestureInsets
         */
        @android.view.WindowInsets.Type.InsetType
        public static int mandatorySystemGestures() {
            return android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES;
        }

        /**
         *
         *
         * @see #getTappableElementInsets
         */
        @android.view.WindowInsets.Type.InsetType
        public static int tappableElement() {
            return android.view.WindowInsets.Type.TAPPABLE_ELEMENT;
        }

        /**
         *
         *
         * @return All system bars. Includes {@link #topBar()} as well as {@link #sideBars()}, but
        not {@link #ime()}.
         */
        @android.view.WindowInsets.Type.InsetType
        public static int systemBars() {
            return android.view.WindowInsets.Type.TOP_BAR | android.view.WindowInsets.Type.SIDE_BARS;
        }

        /**
         *
         *
         * @return Inset types representing the list of bars that traditionally were denoted as
        system insets.
         * @unknown 
         */
        @android.view.WindowInsets.Type.InsetType
        static int compatSystemInsets() {
            return (android.view.WindowInsets.Type.TOP_BAR | android.view.WindowInsets.Type.SIDE_BARS) | android.view.WindowInsets.Type.IME;
        }

        /**
         *
         *
         * @return All inset types combined.

        TODO: Figure out if this makes sense at all, mixing e.g {@link #systemGestures()} and
        {@link #ime()} does not seem very useful.
         */
        @android.view.WindowInsets.Type.InsetType
        public static int all() {
            return 0xffffffff;
        }
    }
}

