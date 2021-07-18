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
package android.support.v7.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class DrawableUtils {
    private static final java.lang.String TAG = "DrawableUtils";

    public static final android.graphics.Rect INSETS_NONE = new android.graphics.Rect();

    private static java.lang.Class<?> sInsetsClazz;

    private static final java.lang.String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";

    static {
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            try {
                android.support.v7.widget.DrawableUtils.sInsetsClazz = java.lang.Class.forName("android.graphics.Insets");
            } catch (java.lang.ClassNotFoundException e) {
                // Oh well...
            }
        }
    }

    private DrawableUtils() {
    }

    /**
     * Allows us to get the optical insets for a {@link Drawable}. Since this is hidden we need to
     * use reflection. Since the {@code Insets} class is hidden also, we return a Rect instead.
     */
    public static android.graphics.Rect getOpticalBounds(android.graphics.drawable.Drawable drawable) {
        if (android.support.v7.widget.DrawableUtils.sInsetsClazz != null) {
            try {
                // If the Drawable is wrapped, we need to manually unwrap it and process
                // the wrapped drawable.
                drawable = android.support.v4.graphics.drawable.DrawableCompat.unwrap(drawable);
                final java.lang.reflect.Method getOpticalInsetsMethod = drawable.getClass().getMethod("getOpticalInsets");
                final java.lang.Object insets = getOpticalInsetsMethod.invoke(drawable);
                if (insets != null) {
                    // If the drawable has some optical insets, let's copy them into a Rect
                    final android.graphics.Rect result = new android.graphics.Rect();
                    for (java.lang.reflect.Field field : android.support.v7.widget.DrawableUtils.sInsetsClazz.getFields()) {
                        switch (field.getName()) {
                            case "left" :
                                result.left = field.getInt(insets);
                                break;
                            case "top" :
                                result.top = field.getInt(insets);
                                break;
                            case "right" :
                                result.right = field.getInt(insets);
                                break;
                            case "bottom" :
                                result.bottom = field.getInt(insets);
                                break;
                        }
                    }
                    return result;
                }
            } catch (java.lang.Exception e) {
                // Eugh, we hit some kind of reflection issue...
                android.util.Log.e(android.support.v7.widget.DrawableUtils.TAG, "Couldn't obtain the optical insets. Ignoring.");
            }
        }
        // If we reach here, either we're running on a device pre-v18, the Drawable didn't have
        // any optical insets, or a reflection issue, so we'll just return an empty rect
        return android.support.v7.widget.DrawableUtils.INSETS_NONE;
    }

    /**
     * Attempt the fix any issues in the given drawable, usually caused by platform bugs in the
     * implementation. This method should be call after retrieval from
     * {@link android.content.res.Resources} or a {@link android.content.res.TypedArray}.
     */
    static void fixDrawable(@android.support.annotation.NonNull
    final android.graphics.drawable.Drawable drawable) {
        if ((android.os.Build.VERSION.SDK_INT == 21) && android.support.v7.widget.DrawableUtils.VECTOR_DRAWABLE_CLAZZ_NAME.equals(drawable.getClass().getName())) {
            android.support.v7.widget.DrawableUtils.fixVectorDrawableTinting(drawable);
        }
    }

    /**
     * Some drawable implementations have problems with mutation. This method returns false if
     * there is a known issue in the given drawable's implementation.
     */
    public static boolean canSafelyMutateDrawable(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        if ((android.os.Build.VERSION.SDK_INT < 15) && (drawable instanceof android.graphics.drawable.InsetDrawable)) {
            return false;
        } else
            if ((android.os.Build.VERSION.SDK_INT < 15) && (drawable instanceof android.graphics.drawable.GradientDrawable)) {
                // GradientDrawable has a bug pre-ICS which results in mutate() resulting
                // in loss of color
                return false;
            } else
                if ((android.os.Build.VERSION.SDK_INT < 17) && (drawable instanceof android.graphics.drawable.LayerDrawable)) {
                    return false;
                }


        if (drawable instanceof android.graphics.drawable.DrawableContainer) {
            // If we have a DrawableContainer, let's traverse it's child array
            final android.graphics.drawable.Drawable.ConstantState state = drawable.getConstantState();
            if (state instanceof android.graphics.drawable.DrawableContainer.DrawableContainerState) {
                final android.graphics.drawable.DrawableContainer.DrawableContainerState containerState = ((android.graphics.drawable.DrawableContainer.DrawableContainerState) (state));
                for (final android.graphics.drawable.Drawable child : containerState.getChildren()) {
                    if (!android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(child)) {
                        return false;
                    }
                }
            }
        } else
            if (drawable instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
                return android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(((android.support.v4.graphics.drawable.DrawableWrapper) (drawable)).getWrappedDrawable());
            } else
                if (drawable instanceof android.support.v7.graphics.drawable.DrawableWrapper) {
                    return android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(((android.support.v7.graphics.drawable.DrawableWrapper) (drawable)).getWrappedDrawable());
                } else
                    if (drawable instanceof android.graphics.drawable.ScaleDrawable) {
                        return android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(((android.graphics.drawable.ScaleDrawable) (drawable)).getDrawable());
                    }



        return true;
    }

    /**
     * VectorDrawable has an issue on API 21 where it sometimes doesn't create its tint filter.
     * Fixed by toggling it's state to force a filter creation.
     */
    private static void fixVectorDrawableTinting(final android.graphics.drawable.Drawable drawable) {
        final int[] originalState = drawable.getState();
        if ((originalState == null) || (originalState.length == 0)) {
            // The drawable doesn't have a state, so set it to be checked
            drawable.setState(android.support.v7.widget.ThemeUtils.CHECKED_STATE_SET);
        } else {
            // Else the drawable does have a state, so clear it
            drawable.setState(android.support.v7.widget.ThemeUtils.EMPTY_STATE_SET);
        }
        // Now set the original state
        drawable.setState(originalState);
    }

    static android.graphics.PorterDuff.Mode parseTintMode(int value, android.graphics.PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3 :
                return android.graphics.PorterDuff.Mode.SRC_OVER;
            case 5 :
                return android.graphics.PorterDuff.Mode.SRC_IN;
            case 9 :
                return android.graphics.PorterDuff.Mode.SRC_ATOP;
            case 14 :
                return android.graphics.PorterDuff.Mode.MULTIPLY;
            case 15 :
                return android.graphics.PorterDuff.Mode.SCREEN;
            case 16 :
                return android.os.Build.VERSION.SDK_INT >= 11 ? android.graphics.PorterDuff.Mode.valueOf("ADD") : defaultMode;
            default :
                return defaultMode;
        }
    }
}

