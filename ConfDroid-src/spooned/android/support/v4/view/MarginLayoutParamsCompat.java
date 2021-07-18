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
package android.support.v4.view;


/**
 * Helper for accessing API features in
 * {@link android.view.ViewGroup.MarginLayoutParams MarginLayoutParams} added after API 4.
 */
public final class MarginLayoutParamsCompat {
    interface MarginLayoutParamsCompatImpl {
        int getMarginStart(android.view.ViewGroup.MarginLayoutParams lp);

        int getMarginEnd(android.view.ViewGroup.MarginLayoutParams lp);

        void setMarginStart(android.view.ViewGroup.MarginLayoutParams lp, int marginStart);

        void setMarginEnd(android.view.ViewGroup.MarginLayoutParams lp, int marginEnd);

        boolean isMarginRelative(android.view.ViewGroup.MarginLayoutParams lp);

        int getLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp);

        void setLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection);

        void resolveLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection);
    }

    static class MarginLayoutParamsCompatImplBase implements android.support.v4.view.MarginLayoutParamsCompat.MarginLayoutParamsCompatImpl {
        @java.lang.Override
        public int getMarginStart(android.view.ViewGroup.MarginLayoutParams lp) {
            return lp.leftMargin;
        }

        @java.lang.Override
        public int getMarginEnd(android.view.ViewGroup.MarginLayoutParams lp) {
            return lp.rightMargin;
        }

        @java.lang.Override
        public void setMarginStart(android.view.ViewGroup.MarginLayoutParams lp, int marginStart) {
            lp.leftMargin = marginStart;
        }

        @java.lang.Override
        public void setMarginEnd(android.view.ViewGroup.MarginLayoutParams lp, int marginEnd) {
            lp.rightMargin = marginEnd;
        }

        @java.lang.Override
        public boolean isMarginRelative(android.view.ViewGroup.MarginLayoutParams lp) {
            return false;
        }

        @java.lang.Override
        public int getLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp) {
            return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
        }

        @java.lang.Override
        public void setLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
            // No-op
        }

        @java.lang.Override
        public void resolveLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
            // No-op
        }
    }

    static class MarginLayoutParamsCompatImplJbMr1 implements android.support.v4.view.MarginLayoutParamsCompat.MarginLayoutParamsCompatImpl {
        @java.lang.Override
        public int getMarginStart(android.view.ViewGroup.MarginLayoutParams lp) {
            return android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.getMarginStart(lp);
        }

        @java.lang.Override
        public int getMarginEnd(android.view.ViewGroup.MarginLayoutParams lp) {
            return android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.getMarginEnd(lp);
        }

        @java.lang.Override
        public void setMarginStart(android.view.ViewGroup.MarginLayoutParams lp, int marginStart) {
            android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.setMarginStart(lp, marginStart);
        }

        @java.lang.Override
        public void setMarginEnd(android.view.ViewGroup.MarginLayoutParams lp, int marginEnd) {
            android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.setMarginEnd(lp, marginEnd);
        }

        @java.lang.Override
        public boolean isMarginRelative(android.view.ViewGroup.MarginLayoutParams lp) {
            return android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.isMarginRelative(lp);
        }

        @java.lang.Override
        public int getLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp) {
            return android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.getLayoutDirection(lp);
        }

        @java.lang.Override
        public void setLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
            android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.setLayoutDirection(lp, layoutDirection);
        }

        @java.lang.Override
        public void resolveLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
            android.support.v4.view.MarginLayoutParamsCompatJellybeanMr1.resolveLayoutDirection(lp, layoutDirection);
        }
    }

    static final android.support.v4.view.MarginLayoutParamsCompat.MarginLayoutParamsCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 17) {
            // jb-mr1
            IMPL = new android.support.v4.view.MarginLayoutParamsCompat.MarginLayoutParamsCompatImplJbMr1();
        } else {
            IMPL = new android.support.v4.view.MarginLayoutParamsCompat.MarginLayoutParamsCompatImplBase();
        }
    }

    /**
     * Get the relative starting margin that was set.
     *
     * <p>On platform versions supporting bidirectional text and layouts
     * this value will be resolved into the LayoutParams object's left or right
     * margin as appropriate when the associated View is attached to a window
     * or when the layout direction of that view changes.</p>
     *
     * @param lp
     * 		LayoutParams to query
     * @return the margin along the starting edge in pixels
     */
    public static int getMarginStart(android.view.ViewGroup.MarginLayoutParams lp) {
        return android.support.v4.view.MarginLayoutParamsCompat.IMPL.getMarginStart(lp);
    }

    /**
     * Get the relative ending margin that was set.
     *
     * <p>On platform versions supporting bidirectional text and layouts
     * this value will be resolved into the LayoutParams object's left or right
     * margin as appropriate when the associated View is attached to a window
     * or when the layout direction of that view changes.</p>
     *
     * @param lp
     * 		LayoutParams to query
     * @return the margin along the ending edge in pixels
     */
    public static int getMarginEnd(android.view.ViewGroup.MarginLayoutParams lp) {
        return android.support.v4.view.MarginLayoutParamsCompat.IMPL.getMarginEnd(lp);
    }

    /**
     * Set the relative start margin.
     *
     * <p>On platform versions supporting bidirectional text and layouts
     * this value will be resolved into the LayoutParams object's left or right
     * margin as appropriate when the associated View is attached to a window
     * or when the layout direction of that view changes.</p>
     *
     * @param lp
     * 		LayoutParams to query
     * @param marginStart
     * 		the desired start margin in pixels
     */
    public static void setMarginStart(android.view.ViewGroup.MarginLayoutParams lp, int marginStart) {
        android.support.v4.view.MarginLayoutParamsCompat.IMPL.setMarginStart(lp, marginStart);
    }

    /**
     * Set the relative end margin.
     *
     * <p>On platform versions supporting bidirectional text and layouts
     * this value will be resolved into the LayoutParams object's left or right
     * margin as appropriate when the associated View is attached to a window
     * or when the layout direction of that view changes.</p>
     *
     * @param lp
     * 		LayoutParams to query
     * @param marginEnd
     * 		the desired end margin in pixels
     */
    public static void setMarginEnd(android.view.ViewGroup.MarginLayoutParams lp, int marginEnd) {
        android.support.v4.view.MarginLayoutParamsCompat.IMPL.setMarginEnd(lp, marginEnd);
    }

    /**
     * Check if margins are relative.
     *
     * @return true if either marginStart or marginEnd has been set.
     */
    public static boolean isMarginRelative(android.view.ViewGroup.MarginLayoutParams lp) {
        return android.support.v4.view.MarginLayoutParamsCompat.IMPL.isMarginRelative(lp);
    }

    /**
     * Returns the layout direction. Can be either {@link ViewCompat#LAYOUT_DIRECTION_LTR} or
     * {@link ViewCompat#LAYOUT_DIRECTION_RTL}.
     *
     * @return the layout direction.
     */
    public static int getLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp) {
        int result = android.support.v4.view.MarginLayoutParamsCompat.IMPL.getLayoutDirection(lp);
        if ((result != android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) && (result != android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL)) {
            // This can happen on older platform releases where the default (unset) layout direction
            // is -1
            result = android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
        }
        return result;
    }

    /**
     * Set the layout direction.
     *
     * @param layoutDirection
     * 		the layout direction.
     * 		Should be either {@link ViewCompat#LAYOUT_DIRECTION_LTR}
     * 		or {@link ViewCompat#LAYOUT_DIRECTION_RTL}.
     */
    public static void setLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        android.support.v4.view.MarginLayoutParamsCompat.IMPL.setLayoutDirection(lp, layoutDirection);
    }

    /**
     * This will be called by {@link android.view.View#requestLayout()}. Left and Right margins
     * may be overridden depending on layout direction.
     */
    public static void resolveLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        android.support.v4.view.MarginLayoutParamsCompat.IMPL.resolveLayoutDirection(lp, layoutDirection);
    }

    private MarginLayoutParamsCompat() {
    }
}

