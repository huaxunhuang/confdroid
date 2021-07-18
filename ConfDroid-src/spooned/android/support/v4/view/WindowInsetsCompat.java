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
package android.support.v4.view;


/**
 * Describes a set of insets for window content.
 *
 * <p>WindowInsetsCompats are immutable and may be expanded to include more inset types in the
 * future. To adjust insets, use one of the supplied clone methods to obtain a new
 * WindowInsetsCompat instance with the adjusted properties.</p>
 */
public class WindowInsetsCompat {
    private interface WindowInsetsCompatImpl {
        int getSystemWindowInsetLeft(java.lang.Object insets);

        int getSystemWindowInsetTop(java.lang.Object insets);

        int getSystemWindowInsetRight(java.lang.Object insets);

        int getSystemWindowInsetBottom(java.lang.Object insets);

        boolean hasSystemWindowInsets(java.lang.Object insets);

        boolean hasInsets(java.lang.Object insets);

        boolean isConsumed(java.lang.Object insets);

        boolean isRound(java.lang.Object insets);

        android.support.v4.view.WindowInsetsCompat consumeSystemWindowInsets(java.lang.Object insets);

        android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(java.lang.Object insets, int left, int top, int right, int bottom);

        android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(java.lang.Object insets, android.graphics.Rect systemWindowInsets);

        int getStableInsetTop(java.lang.Object insets);

        int getStableInsetLeft(java.lang.Object insets);

        int getStableInsetRight(java.lang.Object insets);

        int getStableInsetBottom(java.lang.Object insets);

        boolean hasStableInsets(java.lang.Object insets);

        android.support.v4.view.WindowInsetsCompat consumeStableInsets(java.lang.Object insets);

        java.lang.Object getSourceWindowInsets(java.lang.Object src);
    }

    private static class WindowInsetsCompatBaseImpl implements android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl {
        WindowInsetsCompatBaseImpl() {
        }

        @java.lang.Override
        public int getSystemWindowInsetLeft(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public int getSystemWindowInsetTop(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public int getSystemWindowInsetRight(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public int getSystemWindowInsetBottom(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public boolean hasSystemWindowInsets(java.lang.Object insets) {
            return false;
        }

        @java.lang.Override
        public boolean hasInsets(java.lang.Object insets) {
            return false;
        }

        @java.lang.Override
        public boolean isConsumed(java.lang.Object insets) {
            return false;
        }

        @java.lang.Override
        public boolean isRound(java.lang.Object insets) {
            return false;
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat consumeSystemWindowInsets(java.lang.Object insets) {
            return null;
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(java.lang.Object insets, int left, int top, int right, int bottom) {
            return null;
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(java.lang.Object insets, android.graphics.Rect systemWindowInsets) {
            return null;
        }

        @java.lang.Override
        public int getStableInsetTop(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public int getStableInsetLeft(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public int getStableInsetRight(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public int getStableInsetBottom(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public boolean hasStableInsets(java.lang.Object insets) {
            return false;
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat consumeStableInsets(java.lang.Object insets) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getSourceWindowInsets(java.lang.Object src) {
            return null;
        }
    }

    private static class WindowInsetsCompatApi20Impl extends android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl {
        WindowInsetsCompatApi20Impl() {
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat consumeSystemWindowInsets(java.lang.Object insets) {
            return new android.support.v4.view.WindowInsetsCompat(android.support.v4.view.WindowInsetsCompatApi20.consumeSystemWindowInsets(insets));
        }

        @java.lang.Override
        public int getSystemWindowInsetBottom(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.getSystemWindowInsetBottom(insets);
        }

        @java.lang.Override
        public int getSystemWindowInsetLeft(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.getSystemWindowInsetLeft(insets);
        }

        @java.lang.Override
        public int getSystemWindowInsetRight(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.getSystemWindowInsetRight(insets);
        }

        @java.lang.Override
        public int getSystemWindowInsetTop(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.getSystemWindowInsetTop(insets);
        }

        @java.lang.Override
        public boolean hasInsets(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.hasInsets(insets);
        }

        @java.lang.Override
        public boolean hasSystemWindowInsets(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.hasSystemWindowInsets(insets);
        }

        @java.lang.Override
        public boolean isRound(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi20.isRound(insets);
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(java.lang.Object insets, int left, int top, int right, int bottom) {
            return new android.support.v4.view.WindowInsetsCompat(android.support.v4.view.WindowInsetsCompatApi20.replaceSystemWindowInsets(insets, left, top, right, bottom));
        }

        @java.lang.Override
        public java.lang.Object getSourceWindowInsets(java.lang.Object src) {
            return android.support.v4.view.WindowInsetsCompatApi20.getSourceWindowInsets(src);
        }
    }

    private static class WindowInsetsCompatApi21Impl extends android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatApi20Impl {
        WindowInsetsCompatApi21Impl() {
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat consumeStableInsets(java.lang.Object insets) {
            return new android.support.v4.view.WindowInsetsCompat(android.support.v4.view.WindowInsetsCompatApi21.consumeStableInsets(insets));
        }

        @java.lang.Override
        public int getStableInsetBottom(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi21.getStableInsetBottom(insets);
        }

        @java.lang.Override
        public int getStableInsetLeft(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi21.getStableInsetLeft(insets);
        }

        @java.lang.Override
        public int getStableInsetRight(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi21.getStableInsetRight(insets);
        }

        @java.lang.Override
        public int getStableInsetTop(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi21.getStableInsetTop(insets);
        }

        @java.lang.Override
        public boolean hasStableInsets(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi21.hasStableInsets(insets);
        }

        @java.lang.Override
        public boolean isConsumed(java.lang.Object insets) {
            return android.support.v4.view.WindowInsetsCompatApi21.isConsumed(insets);
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(java.lang.Object insets, android.graphics.Rect systemWindowInsets) {
            return new android.support.v4.view.WindowInsetsCompat(android.support.v4.view.WindowInsetsCompatApi21.replaceSystemWindowInsets(insets, systemWindowInsets));
        }
    }

    private static final android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatApi21Impl();
        } else
            if (version >= 20) {
                IMPL = new android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatApi20Impl();
            } else {
                IMPL = new android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl();
            }

    }

    private final java.lang.Object mInsets;

    WindowInsetsCompat(java.lang.Object insets) {
        mInsets = insets;
    }

    /**
     * Constructs a new WindowInsetsCompat, copying all values from a source WindowInsetsCompat.
     *
     * @param src
     * 		source from which values are copied
     */
    public WindowInsetsCompat(android.support.v4.view.WindowInsetsCompat src) {
        mInsets = (src == null) ? null : android.support.v4.view.WindowInsetsCompat.IMPL.getSourceWindowInsets(src.mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getSystemWindowInsetLeft(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getSystemWindowInsetTop(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getSystemWindowInsetRight(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getSystemWindowInsetBottom(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.hasSystemWindowInsets(mInsets);
    }

    /**
     * Returns true if this WindowInsets has any nonzero insets.
     *
     * @return true if any inset values are nonzero
     */
    public boolean hasInsets() {
        return android.support.v4.view.WindowInsetsCompat.IMPL.hasInsets(mInsets);
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
     * {@link android.view.View#fitSystemWindows(android.graphics.Rect)}.</p>
     *
     * @return true if the insets have been fully consumed.
     */
    public boolean isConsumed() {
        return android.support.v4.view.WindowInsetsCompat.IMPL.isConsumed(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.isRound(mInsets);
    }

    /**
     * Returns a copy of this WindowInsets with the system window insets fully consumed.
     *
     * @return A modified copy of this WindowInsets
     */
    public android.support.v4.view.WindowInsetsCompat consumeSystemWindowInsets() {
        return android.support.v4.view.WindowInsetsCompat.IMPL.consumeSystemWindowInsets(mInsets);
    }

    /**
     * Returns a copy of this WindowInsets with selected system window insets replaced
     * with new values.
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
     */
    public android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(int left, int top, int right, int bottom) {
        return android.support.v4.view.WindowInsetsCompat.IMPL.replaceSystemWindowInsets(mInsets, left, top, right, bottom);
    }

    /**
     * Returns a copy of this WindowInsets with selected system window insets replaced
     * with new values.
     *
     * @param systemWindowInsets
     * 		New system window insets. Each field is the inset in pixels
     * 		for that edge
     * @return A modified copy of this WindowInsets
     */
    public android.support.v4.view.WindowInsetsCompat replaceSystemWindowInsets(android.graphics.Rect systemWindowInsets) {
        return android.support.v4.view.WindowInsetsCompat.IMPL.replaceSystemWindowInsets(mInsets, systemWindowInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getStableInsetTop(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getStableInsetLeft(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getStableInsetRight(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.getStableInsetBottom(mInsets);
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
        return android.support.v4.view.WindowInsetsCompat.IMPL.hasStableInsets(mInsets);
    }

    /**
     * Returns a copy of this WindowInsets with the stable insets fully consumed.
     *
     * @return A modified copy of this WindowInsetsCompat
     */
    public android.support.v4.view.WindowInsetsCompat consumeStableInsets() {
        return android.support.v4.view.WindowInsetsCompat.IMPL.consumeStableInsets(mInsets);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.support.v4.view.WindowInsetsCompat other = ((android.support.v4.view.WindowInsetsCompat) (o));
        return mInsets == null ? other.mInsets == null : mInsets.equals(other.mInsets);
    }

    @java.lang.Override
    public int hashCode() {
        return mInsets == null ? 0 : mInsets.hashCode();
    }

    static android.support.v4.view.WindowInsetsCompat wrap(java.lang.Object insets) {
        return insets == null ? null : new android.support.v4.view.WindowInsetsCompat(insets);
    }

    static java.lang.Object unwrap(android.support.v4.view.WindowInsetsCompat insets) {
        return insets == null ? null : insets.mInsets;
    }
}

