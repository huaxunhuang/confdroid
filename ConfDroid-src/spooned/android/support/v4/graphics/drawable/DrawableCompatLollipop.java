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
package android.support.v4.graphics.drawable;


/**
 * Implementation of drawable compatibility that can call L APIs.
 */
class DrawableCompatLollipop {
    public static void setHotspot(android.graphics.drawable.Drawable drawable, float x, float y) {
        drawable.setHotspot(x, y);
    }

    public static void setHotspotBounds(android.graphics.drawable.Drawable drawable, int left, int top, int right, int bottom) {
        drawable.setHotspotBounds(left, top, right, bottom);
    }

    public static void setTint(android.graphics.drawable.Drawable drawable, int tint) {
        drawable.setTint(tint);
    }

    public static void setTintList(android.graphics.drawable.Drawable drawable, android.content.res.ColorStateList tint) {
        drawable.setTintList(tint);
    }

    public static void setTintMode(android.graphics.drawable.Drawable drawable, android.graphics.PorterDuff.Mode tintMode) {
        drawable.setTintMode(tintMode);
    }

    public static android.graphics.drawable.Drawable wrapForTinting(final android.graphics.drawable.Drawable drawable) {
        if (!(drawable instanceof android.support.v4.graphics.drawable.TintAwareDrawable)) {
            return new android.support.v4.graphics.drawable.DrawableWrapperLollipop(drawable);
        }
        return drawable;
    }

    public static void applyTheme(android.graphics.drawable.Drawable drawable, android.content.res.Resources.Theme t) {
        drawable.applyTheme(t);
    }

    public static boolean canApplyTheme(android.graphics.drawable.Drawable drawable) {
        return drawable.canApplyTheme();
    }

    public static android.graphics.ColorFilter getColorFilter(android.graphics.drawable.Drawable drawable) {
        return drawable.getColorFilter();
    }

    public static void clearColorFilter(android.graphics.drawable.Drawable drawable) {
        drawable.clearColorFilter();
        // API 21 + 22 have an issue where clearing a color filter on a DrawableContainer
        // will not propagate to all of its children. To workaround this we unwrap the drawable
        // to find any DrawableContainers, and then unwrap those to clear the filter on its
        // children manually
        if (drawable instanceof android.graphics.drawable.InsetDrawable) {
            android.support.v4.graphics.drawable.DrawableCompatLollipop.clearColorFilter(((android.graphics.drawable.InsetDrawable) (drawable)).getDrawable());
        } else
            if (drawable instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
                android.support.v4.graphics.drawable.DrawableCompatLollipop.clearColorFilter(((android.support.v4.graphics.drawable.DrawableWrapper) (drawable)).getWrappedDrawable());
            } else
                if (drawable instanceof android.graphics.drawable.DrawableContainer) {
                    final android.graphics.drawable.DrawableContainer container = ((android.graphics.drawable.DrawableContainer) (drawable));
                    final android.graphics.drawable.DrawableContainer.DrawableContainerState state = ((android.graphics.drawable.DrawableContainer.DrawableContainerState) (container.getConstantState()));
                    if (state != null) {
                        android.graphics.drawable.Drawable child;
                        for (int i = 0, count = state.getChildCount(); i < count; i++) {
                            child = state.getChild(i);
                            if (child != null) {
                                android.support.v4.graphics.drawable.DrawableCompatLollipop.clearColorFilter(child);
                            }
                        }
                    }
                }


    }

    public static void inflate(android.graphics.drawable.Drawable drawable, android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme t) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        drawable.inflate(res, parser, attrs, t);
    }
}

