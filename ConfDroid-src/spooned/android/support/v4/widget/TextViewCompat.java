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
package android.support.v4.widget;


/**
 * Helper for accessing features in {@link TextView} introduced after API level
 * 4 in a backwards compatible fashion.
 */
public final class TextViewCompat {
    // Hide constructor
    private TextViewCompat() {
    }

    interface TextViewCompatImpl {
        void setCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom);

        void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom);

        void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.DrawableRes
        int start, @android.support.annotation.DrawableRes
        int top, @android.support.annotation.DrawableRes
        int end, @android.support.annotation.DrawableRes
        int bottom);

        int getMaxLines(android.widget.TextView textView);

        int getMinLines(android.widget.TextView textView);

        void setTextAppearance(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.StyleRes
        int resId);

        android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView);
    }

    static class BaseTextViewCompatImpl implements android.support.v4.widget.TextViewCompat.TextViewCompatImpl {
        @java.lang.Override
        public void setCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom) {
            textView.setCompoundDrawables(start, top, end, bottom);
        }

        @java.lang.Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }

        @java.lang.Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.DrawableRes
        int start, @android.support.annotation.DrawableRes
        int top, @android.support.annotation.DrawableRes
        int end, @android.support.annotation.DrawableRes
        int bottom) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }

        @java.lang.Override
        public int getMaxLines(android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatGingerbread.getMaxLines(textView);
        }

        @java.lang.Override
        public int getMinLines(android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatGingerbread.getMinLines(textView);
        }

        @java.lang.Override
        public void setTextAppearance(android.widget.TextView textView, @android.support.annotation.StyleRes
        int resId) {
            android.support.v4.widget.TextViewCompatGingerbread.setTextAppearance(textView, resId);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatGingerbread.getCompoundDrawablesRelative(textView);
        }
    }

    static class JbTextViewCompatImpl extends android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl {
        @java.lang.Override
        public int getMaxLines(android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatJb.getMaxLines(textView);
        }

        @java.lang.Override
        public int getMinLines(android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatJb.getMinLines(textView);
        }
    }

    static class JbMr1TextViewCompatImpl extends android.support.v4.widget.TextViewCompat.JbTextViewCompatImpl {
        @java.lang.Override
        public void setCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom) {
            android.support.v4.widget.TextViewCompatJbMr1.setCompoundDrawablesRelative(textView, start, top, end, bottom);
        }

        @java.lang.Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom) {
            android.support.v4.widget.TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @java.lang.Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.DrawableRes
        int start, @android.support.annotation.DrawableRes
        int top, @android.support.annotation.DrawableRes
        int end, @android.support.annotation.DrawableRes
        int bottom) {
            android.support.v4.widget.TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatJbMr1.getCompoundDrawablesRelative(textView);
        }
    }

    static class JbMr2TextViewCompatImpl extends android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl {
        @java.lang.Override
        public void setCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom) {
            android.support.v4.widget.TextViewCompatJbMr2.setCompoundDrawablesRelative(textView, start, top, end, bottom);
        }

        @java.lang.Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
        android.graphics.drawable.Drawable bottom) {
            android.support.v4.widget.TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @java.lang.Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.DrawableRes
        int start, @android.support.annotation.DrawableRes
        int top, @android.support.annotation.DrawableRes
        int end, @android.support.annotation.DrawableRes
        int bottom) {
            android.support.v4.widget.TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
        android.widget.TextView textView) {
            return android.support.v4.widget.TextViewCompatJbMr2.getCompoundDrawablesRelative(textView);
        }
    }

    static class Api23TextViewCompatImpl extends android.support.v4.widget.TextViewCompat.JbMr2TextViewCompatImpl {
        @java.lang.Override
        public void setTextAppearance(@android.support.annotation.NonNull
        android.widget.TextView textView, @android.support.annotation.StyleRes
        int resId) {
            android.support.v4.widget.TextViewCompatApi23.setTextAppearance(textView, resId);
        }
    }

    static final android.support.v4.widget.TextViewCompat.TextViewCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new android.support.v4.widget.TextViewCompat.Api23TextViewCompatImpl();
        } else
            if (version >= 18) {
                IMPL = new android.support.v4.widget.TextViewCompat.JbMr2TextViewCompatImpl();
            } else
                if (version >= 17) {
                    IMPL = new android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl();
                } else
                    if (version >= 16) {
                        IMPL = new android.support.v4.widget.TextViewCompat.JbTextViewCompatImpl();
                    } else {
                        IMPL = new android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl();
                    }



    }

    /**
     * Sets the Drawables (if any) to appear to the start of, above, to the end
     * of, and below the text. Use {@code null} if you do not want a Drawable
     * there. The Drawables must already have had {@link Drawable#setBounds}
     * called.
     * <p/>
     * Calling this method will overwrite any Drawables previously set using
     * {@link TextView#setCompoundDrawables} or related methods.
     *
     * @param textView
     * 		The TextView against which to invoke the method.
     * @unknown name android:drawableStart
     * @unknown name android:drawableTop
     * @unknown name android:drawableEnd
     * @unknown name android:drawableBottom
     */
    public static void setCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        android.support.v4.widget.TextViewCompat.IMPL.setCompoundDrawablesRelative(textView, start, top, end, bottom);
    }

    /**
     * Sets the Drawables (if any) to appear to the start of, above, to the end
     * of, and below the text. Use {@code null} if you do not want a Drawable
     * there. The Drawables' bounds will be set to their intrinsic bounds.
     * <p/>
     * Calling this method will overwrite any Drawables previously set using
     * {@link TextView#setCompoundDrawables} or related methods.
     *
     * @param textView
     * 		The TextView against which to invoke the method.
     * @unknown name android:drawableStart
     * @unknown name android:drawableTop
     * @unknown name android:drawableEnd
     * @unknown name android:drawableBottom
     */
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        android.support.v4.widget.TextViewCompat.IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    /**
     * Sets the Drawables (if any) to appear to the start of, above, to the end
     * of, and below the text. Use 0 if you do not want a Drawable there. The
     * Drawables' bounds will be set to their intrinsic bounds.
     * <p/>
     * Calling this method will overwrite any Drawables previously set using
     * {@link TextView#setCompoundDrawables} or related methods.
     *
     * @param textView
     * 		The TextView against which to invoke the method.
     * @param start
     * 		Resource identifier of the start Drawable.
     * @param top
     * 		Resource identifier of the top Drawable.
     * @param end
     * 		Resource identifier of the end Drawable.
     * @param bottom
     * 		Resource identifier of the bottom Drawable.
     * @unknown name android:drawableStart
     * @unknown name android:drawableTop
     * @unknown name android:drawableEnd
     * @unknown name android:drawableBottom
     */
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.DrawableRes
    int start, @android.support.annotation.DrawableRes
    int top, @android.support.annotation.DrawableRes
    int end, @android.support.annotation.DrawableRes
    int bottom) {
        android.support.v4.widget.TextViewCompat.IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    /**
     * Returns the maximum number of lines displayed in the given TextView, or -1 if the maximum
     * height was set in pixels instead.
     */
    public static int getMaxLines(@android.support.annotation.NonNull
    android.widget.TextView textView) {
        return android.support.v4.widget.TextViewCompat.IMPL.getMaxLines(textView);
    }

    /**
     * Returns the minimum number of lines displayed in the given TextView, or -1 if the minimum
     * height was set in pixels instead.
     */
    public static int getMinLines(@android.support.annotation.NonNull
    android.widget.TextView textView) {
        return android.support.v4.widget.TextViewCompat.IMPL.getMinLines(textView);
    }

    /**
     * Sets the text appearance from the specified style resource.
     * <p>
     * Use a framework-defined {@code TextAppearance} style like
     * {@link android.R.style#TextAppearance_Material_Body1 @android:style/TextAppearance.Material.Body1}.
     *
     * @param textView
     * 		The TextView against which to invoke the method.
     * @param resId
     * 		The resource identifier of the style to apply.
     */
    public static void setTextAppearance(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.StyleRes
    int resId) {
        android.support.v4.widget.TextViewCompat.IMPL.setTextAppearance(textView, resId);
    }

    /**
     * Returns drawables for the start, top, end, and bottom borders from the given text view.
     */
    public static android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView) {
        return android.support.v4.widget.TextViewCompat.IMPL.getCompoundDrawablesRelative(textView);
    }
}

