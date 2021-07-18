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
package android.support.v4.graphics.drawable;


/**
 * Helper for accessing features in {@link android.graphics.drawable.Drawable}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class DrawableCompat {
    /**
     * Interface for the full API.
     */
    interface DrawableImpl {
        void jumpToCurrentState(android.graphics.drawable.Drawable drawable);

        void setAutoMirrored(android.graphics.drawable.Drawable drawable, boolean mirrored);

        boolean isAutoMirrored(android.graphics.drawable.Drawable drawable);

        void setHotspot(android.graphics.drawable.Drawable drawable, float x, float y);

        void setHotspotBounds(android.graphics.drawable.Drawable drawable, int left, int top, int right, int bottom);

        void setTint(android.graphics.drawable.Drawable drawable, int tint);

        void setTintList(android.graphics.drawable.Drawable drawable, android.content.res.ColorStateList tint);

        void setTintMode(android.graphics.drawable.Drawable drawable, android.graphics.PorterDuff.Mode tintMode);

        android.graphics.drawable.Drawable wrap(android.graphics.drawable.Drawable drawable);

        boolean setLayoutDirection(android.graphics.drawable.Drawable drawable, int layoutDirection);

        int getLayoutDirection(android.graphics.drawable.Drawable drawable);

        int getAlpha(android.graphics.drawable.Drawable drawable);

        void applyTheme(android.graphics.drawable.Drawable drawable, android.content.res.Resources.Theme t);

        boolean canApplyTheme(android.graphics.drawable.Drawable drawable);

        android.graphics.ColorFilter getColorFilter(android.graphics.drawable.Drawable drawable);

        void clearColorFilter(android.graphics.drawable.Drawable drawable);

        void inflate(android.graphics.drawable.Drawable drawable, android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme t) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException;
    }

    /**
     * Interface implementation that doesn't use anything about v4 APIs.
     */
    static class BaseDrawableImpl implements android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl {
        @java.lang.Override
        public void jumpToCurrentState(android.graphics.drawable.Drawable drawable) {
        }

        @java.lang.Override
        public void setAutoMirrored(android.graphics.drawable.Drawable drawable, boolean mirrored) {
        }

        @java.lang.Override
        public boolean isAutoMirrored(android.graphics.drawable.Drawable drawable) {
            return false;
        }

        @java.lang.Override
        public void setHotspot(android.graphics.drawable.Drawable drawable, float x, float y) {
        }

        @java.lang.Override
        public void setHotspotBounds(android.graphics.drawable.Drawable drawable, int left, int top, int right, int bottom) {
        }

        @java.lang.Override
        public void setTint(android.graphics.drawable.Drawable drawable, int tint) {
            android.support.v4.graphics.drawable.DrawableCompatBase.setTint(drawable, tint);
        }

        @java.lang.Override
        public void setTintList(android.graphics.drawable.Drawable drawable, android.content.res.ColorStateList tint) {
            android.support.v4.graphics.drawable.DrawableCompatBase.setTintList(drawable, tint);
        }

        @java.lang.Override
        public void setTintMode(android.graphics.drawable.Drawable drawable, android.graphics.PorterDuff.Mode tintMode) {
            android.support.v4.graphics.drawable.DrawableCompatBase.setTintMode(drawable, tintMode);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable wrap(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatBase.wrapForTinting(drawable);
        }

        @java.lang.Override
        public boolean setLayoutDirection(android.graphics.drawable.Drawable drawable, int layoutDirection) {
            // No op for API < 23
            return false;
        }

        @java.lang.Override
        public int getLayoutDirection(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
        }

        @java.lang.Override
        public int getAlpha(android.graphics.drawable.Drawable drawable) {
            return 0;
        }

        @java.lang.Override
        public void applyTheme(android.graphics.drawable.Drawable drawable, android.content.res.Resources.Theme t) {
        }

        @java.lang.Override
        public boolean canApplyTheme(android.graphics.drawable.Drawable drawable) {
            return false;
        }

        @java.lang.Override
        public android.graphics.ColorFilter getColorFilter(android.graphics.drawable.Drawable drawable) {
            return null;
        }

        @java.lang.Override
        public void clearColorFilter(android.graphics.drawable.Drawable drawable) {
            drawable.clearColorFilter();
        }

        @java.lang.Override
        public void inflate(android.graphics.drawable.Drawable drawable, android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme t) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            android.support.v4.graphics.drawable.DrawableCompatBase.inflate(drawable, res, parser, attrs, t);
        }
    }

    /**
     * Interface implementation for devices with at least v11 APIs.
     */
    static class HoneycombDrawableImpl extends android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl {
        @java.lang.Override
        public void jumpToCurrentState(android.graphics.drawable.Drawable drawable) {
            android.support.v4.graphics.drawable.DrawableCompatHoneycomb.jumpToCurrentState(drawable);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable wrap(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatHoneycomb.wrapForTinting(drawable);
        }
    }

    static class JellybeanMr1DrawableImpl extends android.support.v4.graphics.drawable.DrawableCompat.HoneycombDrawableImpl {
        @java.lang.Override
        public boolean setLayoutDirection(android.graphics.drawable.Drawable drawable, int layoutDirection) {
            return android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.setLayoutDirection(drawable, layoutDirection);
        }

        @java.lang.Override
        public int getLayoutDirection(android.graphics.drawable.Drawable drawable) {
            final int dir = android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.getLayoutDirection(drawable);
            return dir >= 0 ? dir : android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
        }
    }

    /**
     * Interface implementation for devices with at least KitKat APIs.
     */
    static class KitKatDrawableImpl extends android.support.v4.graphics.drawable.DrawableCompat.JellybeanMr1DrawableImpl {
        @java.lang.Override
        public void setAutoMirrored(android.graphics.drawable.Drawable drawable, boolean mirrored) {
            android.support.v4.graphics.drawable.DrawableCompatKitKat.setAutoMirrored(drawable, mirrored);
        }

        @java.lang.Override
        public boolean isAutoMirrored(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatKitKat.isAutoMirrored(drawable);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable wrap(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatKitKat.wrapForTinting(drawable);
        }

        @java.lang.Override
        public int getAlpha(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatKitKat.getAlpha(drawable);
        }
    }

    /**
     * Interface implementation for devices with at least L APIs.
     */
    static class LollipopDrawableImpl extends android.support.v4.graphics.drawable.DrawableCompat.KitKatDrawableImpl {
        @java.lang.Override
        public void setHotspot(android.graphics.drawable.Drawable drawable, float x, float y) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.setHotspot(drawable, x, y);
        }

        @java.lang.Override
        public void setHotspotBounds(android.graphics.drawable.Drawable drawable, int left, int top, int right, int bottom) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.setHotspotBounds(drawable, left, top, right, bottom);
        }

        @java.lang.Override
        public void setTint(android.graphics.drawable.Drawable drawable, int tint) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.setTint(drawable, tint);
        }

        @java.lang.Override
        public void setTintList(android.graphics.drawable.Drawable drawable, android.content.res.ColorStateList tint) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.setTintList(drawable, tint);
        }

        @java.lang.Override
        public void setTintMode(android.graphics.drawable.Drawable drawable, android.graphics.PorterDuff.Mode tintMode) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.setTintMode(drawable, tintMode);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable wrap(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatLollipop.wrapForTinting(drawable);
        }

        @java.lang.Override
        public void applyTheme(android.graphics.drawable.Drawable drawable, android.content.res.Resources.Theme t) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.applyTheme(drawable, t);
        }

        @java.lang.Override
        public boolean canApplyTheme(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatLollipop.canApplyTheme(drawable);
        }

        @java.lang.Override
        public android.graphics.ColorFilter getColorFilter(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatLollipop.getColorFilter(drawable);
        }

        @java.lang.Override
        public void clearColorFilter(android.graphics.drawable.Drawable drawable) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.clearColorFilter(drawable);
        }

        @java.lang.Override
        public void inflate(android.graphics.drawable.Drawable drawable, android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme t) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.inflate(drawable, res, parser, attrs, t);
        }
    }

    /**
     * Interface implementation for devices with at least M APIs.
     */
    static class MDrawableImpl extends android.support.v4.graphics.drawable.DrawableCompat.LollipopDrawableImpl {
        @java.lang.Override
        public boolean setLayoutDirection(android.graphics.drawable.Drawable drawable, int layoutDirection) {
            return android.support.v4.graphics.drawable.DrawableCompatApi23.setLayoutDirection(drawable, layoutDirection);
        }

        @java.lang.Override
        public int getLayoutDirection(android.graphics.drawable.Drawable drawable) {
            return android.support.v4.graphics.drawable.DrawableCompatApi23.getLayoutDirection(drawable);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable wrap(android.graphics.drawable.Drawable drawable) {
            // No need to wrap on M+
            return drawable;
        }

        @java.lang.Override
        public void clearColorFilter(android.graphics.drawable.Drawable drawable) {
            // We can use clearColorFilter() safely on M+
            drawable.clearColorFilter();
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new android.support.v4.graphics.drawable.DrawableCompat.MDrawableImpl();
        } else
            if (version >= 21) {
                IMPL = new android.support.v4.graphics.drawable.DrawableCompat.LollipopDrawableImpl();
            } else
                if (version >= 19) {
                    IMPL = new android.support.v4.graphics.drawable.DrawableCompat.KitKatDrawableImpl();
                } else
                    if (version >= 17) {
                        IMPL = new android.support.v4.graphics.drawable.DrawableCompat.JellybeanMr1DrawableImpl();
                    } else
                        if (version >= 11) {
                            IMPL = new android.support.v4.graphics.drawable.DrawableCompat.HoneycombDrawableImpl();
                        } else {
                            IMPL = new android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl();
                        }




    }

    /**
     * Call {@link Drawable#jumpToCurrentState() Drawable.jumpToCurrentState()}.
     * <p>
     * If running on a pre-{@link android.os.Build.VERSION_CODES#HONEYCOMB}
     * device this method does nothing.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     */
    public static void jumpToCurrentState(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.jumpToCurrentState(drawable);
    }

    /**
     * Set whether this Drawable is automatically mirrored when its layout
     * direction is RTL (right-to left). See
     * {@link android.util.LayoutDirection}.
     * <p>
     * If running on a pre-{@link android.os.Build.VERSION_CODES#KITKAT} device
     * this method does nothing.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     * @param mirrored
     * 		Set to true if the Drawable should be mirrored, false if
     * 		not.
     */
    public static void setAutoMirrored(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, boolean mirrored) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.setAutoMirrored(drawable, mirrored);
    }

    /**
     * Tells if this Drawable will be automatically mirrored when its layout
     * direction is RTL right-to-left. See {@link android.util.LayoutDirection}.
     * <p>
     * If running on a pre-{@link android.os.Build.VERSION_CODES#KITKAT} device
     * this method returns false.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     * @return boolean Returns true if this Drawable will be automatically
    mirrored.
     */
    public static boolean isAutoMirrored(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.isAutoMirrored(drawable);
    }

    /**
     * Specifies the hotspot's location within the drawable.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     * @param x
     * 		The X coordinate of the center of the hotspot
     * @param y
     * 		The Y coordinate of the center of the hotspot
     */
    public static void setHotspot(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, float x, float y) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.setHotspot(drawable, x, y);
    }

    /**
     * Sets the bounds to which the hotspot is constrained, if they should be
     * different from the drawable bounds.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     */
    public static void setHotspotBounds(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, int left, int top, int right, int bottom) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.setHotspotBounds(drawable, left, top, right, bottom);
    }

    /**
     * Specifies a tint for {@code drawable}.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     * @param tint
     * 		Color to use for tinting this drawable
     */
    public static void setTint(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, @android.support.annotation.ColorInt
    int tint) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.setTint(drawable, tint);
    }

    /**
     * Specifies a tint for {@code drawable} as a color state list.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     * @param tint
     * 		Color state list to use for tinting this drawable, or null to clear the tint
     */
    public static void setTintList(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, @android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.setTintList(drawable, tint);
    }

    /**
     * Specifies a tint blending mode for {@code drawable}.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     * @param tintMode
     * 		A Porter-Duff blending mode
     */
    public static void setTintMode(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, @android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.setTintMode(drawable, tintMode);
    }

    /**
     * Get the alpha value of the {@code drawable}.
     * 0 means fully transparent, 255 means fully opaque.
     *
     * @param drawable
     * 		The Drawable against which to invoke the method.
     */
    public static int getAlpha(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.getAlpha(drawable);
    }

    /**
     * Applies the specified theme to this Drawable and its children.
     */
    public static void applyTheme(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, @android.support.annotation.NonNull
    android.content.res.Resources.Theme t) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.applyTheme(drawable, t);
    }

    /**
     * Whether a theme can be applied to this Drawable and its children.
     */
    public static boolean canApplyTheme(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.canApplyTheme(drawable);
    }

    /**
     * Returns the current color filter, or {@code null} if none set.
     *
     * @return the current color filter, or {@code null} if none set
     */
    public static android.graphics.ColorFilter getColorFilter(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.getColorFilter(drawable);
    }

    /**
     * Removes the color filter from the given drawable.
     */
    public static void clearColorFilter(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.clearColorFilter(drawable);
    }

    /**
     * Inflate this Drawable from an XML resource optionally styled by a theme.
     *
     * @param res
     * 		Resources used to resolve attribute values
     * @param parser
     * 		XML parser from which to inflate this Drawable
     * @param attrs
     * 		Base set of attribute values
     * @param theme
     * 		Theme to apply, may be null
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    public static void inflate(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, @android.support.annotation.NonNull
    android.content.res.Resources res, @android.support.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.NonNull
    android.util.AttributeSet attrs, @android.support.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.support.v4.graphics.drawable.DrawableCompat.IMPL.inflate(drawable, res, parser, attrs, theme);
    }

    /**
     * Potentially wrap {@code drawable} so that it may be used for tinting across the
     * different API levels, via the tinting methods in this class.
     *
     * <p>If the given drawable is wrapped, we will copy over certain state over to the wrapped
     * drawable, such as its bounds, level, visibility and state.</p>
     *
     * <p>You must use the result of this call. If the given drawable is being used by a view
     * (as it's background for instance), you must replace the original drawable with
     * the result of this call:</p>
     *
     * <pre>
     * Drawable bg = DrawableCompat.wrap(view.getBackground());
     * // Need to set the background with the wrapped drawable
     * view.setBackground(bg);
     *
     * // You can now tint the drawable
     * DrawableCompat.setTint(bg, ...);
     * </pre>
     *
     * <p>If you need to get hold of the original {@link android.graphics.drawable.Drawable} again,
     * you can use the value returned from {@link #unwrap(Drawable)}.</p>
     *
     * @param drawable
     * 		The Drawable to process
     * @return A drawable capable of being tinted across all API levels.
     * @see #setTint(Drawable, int)
     * @see #setTintList(Drawable, ColorStateList)
     * @see #setTintMode(Drawable, PorterDuff.Mode)
     * @see #unwrap(Drawable)
     */
    public static android.graphics.drawable.Drawable wrap(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.wrap(drawable);
    }

    /**
     * Unwrap {@code drawable} if it is the result of a call to {@link #wrap(Drawable)}. If
     * the {@code drawable} is not the result of a call to {@link #wrap(Drawable)} then
     * {@code drawable} is returned as-is.
     *
     * @param drawable
     * 		The drawable to unwrap
     * @return the unwrapped {@link Drawable} or {@code drawable} if it hasn't been wrapped.
     * @see #wrap(Drawable)
     */
    public static <T extends android.graphics.drawable.Drawable> T unwrap(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
            return ((T) (((android.support.v4.graphics.drawable.DrawableWrapper) (drawable)).getWrappedDrawable()));
        }
        return ((T) (drawable));
    }

    /**
     * Set the layout direction for this drawable. Should be a resolved
     * layout direction, as the Drawable has no capacity to do the resolution on
     * its own.
     *
     * @param layoutDirection
     * 		the resolved layout direction for the drawable,
     * 		either {@link ViewCompat#LAYOUT_DIRECTION_LTR}
     * 		or {@link ViewCompat#LAYOUT_DIRECTION_RTL}
     * @return {@code true} if the layout direction change has caused the
    appearance of the drawable to change such that it needs to be
    re-drawn, {@code false} otherwise
     * @see #getLayoutDirection(Drawable)
     */
    public static boolean setLayoutDirection(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable, int layoutDirection) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.setLayoutDirection(drawable, layoutDirection);
    }

    /**
     * Returns the resolved layout direction for this Drawable.
     *
     * @return One of {@link ViewCompat#LAYOUT_DIRECTION_LTR},
    {@link ViewCompat#LAYOUT_DIRECTION_RTL}
     * @see #setLayoutDirection(Drawable, int)
     */
    public static int getLayoutDirection(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        return android.support.v4.graphics.drawable.DrawableCompat.IMPL.getLayoutDirection(drawable);
    }

    private DrawableCompat() {
    }
}

