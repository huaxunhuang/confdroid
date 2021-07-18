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
 * Helper for accessing {@link android.widget.CompoundButton} methods introduced after
 * API level 4 in a backwards compatible fashion.
 */
public final class CompoundButtonCompat {
    private static final android.support.v4.widget.CompoundButtonCompat.CompoundButtonCompatImpl IMPL;

    static {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= 23) {
            IMPL = new android.support.v4.widget.CompoundButtonCompat.Api23CompoundButtonImpl();
        } else
            if (sdk >= 21) {
                IMPL = new android.support.v4.widget.CompoundButtonCompat.LollipopCompoundButtonImpl();
            } else {
                IMPL = new android.support.v4.widget.CompoundButtonCompat.BaseCompoundButtonCompat();
            }

    }

    interface CompoundButtonCompatImpl {
        void setButtonTintList(android.widget.CompoundButton button, android.content.res.ColorStateList tint);

        android.content.res.ColorStateList getButtonTintList(android.widget.CompoundButton button);

        void setButtonTintMode(android.widget.CompoundButton button, android.graphics.PorterDuff.Mode tintMode);

        android.graphics.PorterDuff.Mode getButtonTintMode(android.widget.CompoundButton button);

        android.graphics.drawable.Drawable getButtonDrawable(android.widget.CompoundButton button);
    }

    static class BaseCompoundButtonCompat implements android.support.v4.widget.CompoundButtonCompat.CompoundButtonCompatImpl {
        @java.lang.Override
        public void setButtonTintList(android.widget.CompoundButton button, android.content.res.ColorStateList tint) {
            android.support.v4.widget.CompoundButtonCompatGingerbread.setButtonTintList(button, tint);
        }

        @java.lang.Override
        public android.content.res.ColorStateList getButtonTintList(android.widget.CompoundButton button) {
            return android.support.v4.widget.CompoundButtonCompatGingerbread.getButtonTintList(button);
        }

        @java.lang.Override
        public void setButtonTintMode(android.widget.CompoundButton button, android.graphics.PorterDuff.Mode tintMode) {
            android.support.v4.widget.CompoundButtonCompatGingerbread.setButtonTintMode(button, tintMode);
        }

        @java.lang.Override
        public android.graphics.PorterDuff.Mode getButtonTintMode(android.widget.CompoundButton button) {
            return android.support.v4.widget.CompoundButtonCompatGingerbread.getButtonTintMode(button);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getButtonDrawable(android.widget.CompoundButton button) {
            return android.support.v4.widget.CompoundButtonCompatGingerbread.getButtonDrawable(button);
        }
    }

    static class LollipopCompoundButtonImpl extends android.support.v4.widget.CompoundButtonCompat.BaseCompoundButtonCompat {
        @java.lang.Override
        public void setButtonTintList(android.widget.CompoundButton button, android.content.res.ColorStateList tint) {
            android.support.v4.widget.CompoundButtonCompatLollipop.setButtonTintList(button, tint);
        }

        @java.lang.Override
        public android.content.res.ColorStateList getButtonTintList(android.widget.CompoundButton button) {
            return android.support.v4.widget.CompoundButtonCompatLollipop.getButtonTintList(button);
        }

        @java.lang.Override
        public void setButtonTintMode(android.widget.CompoundButton button, android.graphics.PorterDuff.Mode tintMode) {
            android.support.v4.widget.CompoundButtonCompatLollipop.setButtonTintMode(button, tintMode);
        }

        @java.lang.Override
        public android.graphics.PorterDuff.Mode getButtonTintMode(android.widget.CompoundButton button) {
            return android.support.v4.widget.CompoundButtonCompatLollipop.getButtonTintMode(button);
        }
    }

    static class Api23CompoundButtonImpl extends android.support.v4.widget.CompoundButtonCompat.LollipopCompoundButtonImpl {
        @java.lang.Override
        public android.graphics.drawable.Drawable getButtonDrawable(android.widget.CompoundButton button) {
            return android.support.v4.widget.CompoundButtonCompatApi23.getButtonDrawable(button);
        }
    }

    private CompoundButtonCompat() {
    }

    /**
     * Applies a tint to the button drawable. Does not modify the current tint
     * mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link CompoundButton#setButtonDrawable(Drawable)} should
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using {@link DrawableCompat#setTintList(Drawable, ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @see #setButtonTintList(CompoundButton, ColorStateList)
     */
    public static void setButtonTintList(@android.support.annotation.NonNull
    android.widget.CompoundButton button, @android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        android.support.v4.widget.CompoundButtonCompat.IMPL.setButtonTintList(button, tint);
    }

    /**
     * Returns the tint applied to the button drawable
     *
     * @see #setButtonTintList(CompoundButton, ColorStateList)
     */
    @android.support.annotation.Nullable
    public static android.content.res.ColorStateList getButtonTintList(@android.support.annotation.NonNull
    android.widget.CompoundButton button) {
        return android.support.v4.widget.CompoundButtonCompat.IMPL.getButtonTintList(button);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setButtonTintList(CompoundButton, ColorStateList)}} to the button drawable. The
     * default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @see #getButtonTintMode(CompoundButton)
     * @see DrawableCompat#setTintMode(Drawable, PorterDuff.Mode)
     */
    public static void setButtonTintMode(@android.support.annotation.NonNull
    android.widget.CompoundButton button, @android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        android.support.v4.widget.CompoundButtonCompat.IMPL.setButtonTintMode(button, tintMode);
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the button drawable
     * @unknown name android:buttonTintMode
     * @see #setButtonTintMode(PorterDuff.Mode)
     */
    @android.support.annotation.Nullable
    public static android.graphics.PorterDuff.Mode getButtonTintMode(@android.support.annotation.NonNull
    android.widget.CompoundButton button) {
        return android.support.v4.widget.CompoundButtonCompat.IMPL.getButtonTintMode(button);
    }

    /**
     * Returns the drawable used as the compound button image
     *
     * @see CompoundButton#setButtonDrawable(Drawable)
     */
    @android.support.annotation.Nullable
    public static android.graphics.drawable.Drawable getButtonDrawable(@android.support.annotation.NonNull
    android.widget.CompoundButton button) {
        return android.support.v4.widget.CompoundButtonCompat.IMPL.getButtonDrawable(button);
    }
}

