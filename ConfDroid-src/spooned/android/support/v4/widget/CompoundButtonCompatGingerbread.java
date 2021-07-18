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


class CompoundButtonCompatGingerbread {
    private static final java.lang.String TAG = "CompoundButtonCompatGingerbread";

    private static java.lang.reflect.Field sButtonDrawableField;

    private static boolean sButtonDrawableFieldFetched;

    static void setButtonTintList(android.widget.CompoundButton button, android.content.res.ColorStateList tint) {
        if (button instanceof android.support.v4.widget.TintableCompoundButton) {
            ((android.support.v4.widget.TintableCompoundButton) (button)).setSupportButtonTintList(tint);
        }
    }

    static android.content.res.ColorStateList getButtonTintList(android.widget.CompoundButton button) {
        if (button instanceof android.support.v4.widget.TintableCompoundButton) {
            return ((android.support.v4.widget.TintableCompoundButton) (button)).getSupportButtonTintList();
        }
        return null;
    }

    static void setButtonTintMode(android.widget.CompoundButton button, android.graphics.PorterDuff.Mode tintMode) {
        if (button instanceof android.support.v4.widget.TintableCompoundButton) {
            ((android.support.v4.widget.TintableCompoundButton) (button)).setSupportButtonTintMode(tintMode);
        }
    }

    static android.graphics.PorterDuff.Mode getButtonTintMode(android.widget.CompoundButton button) {
        if (button instanceof android.support.v4.widget.TintableCompoundButton) {
            return ((android.support.v4.widget.TintableCompoundButton) (button)).getSupportButtonTintMode();
        }
        return null;
    }

    static android.graphics.drawable.Drawable getButtonDrawable(android.widget.CompoundButton button) {
        if (!android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableFieldFetched) {
            try {
                android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableField = android.widget.CompoundButton.class.getDeclaredField("mButtonDrawable");
                android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableField.setAccessible(true);
            } catch (java.lang.NoSuchFieldException e) {
                android.util.Log.i(android.support.v4.widget.CompoundButtonCompatGingerbread.TAG, "Failed to retrieve mButtonDrawable field", e);
            }
            android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableFieldFetched = true;
        }
        if (android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableField != null) {
            try {
                return ((android.graphics.drawable.Drawable) (android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableField.get(button)));
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.i(android.support.v4.widget.CompoundButtonCompatGingerbread.TAG, "Failed to get button drawable via reflection", e);
                android.support.v4.widget.CompoundButtonCompatGingerbread.sButtonDrawableField = null;
            }
        }
        return null;
    }
}

