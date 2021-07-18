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
package android.support.v4.graphics.drawable;


/**
 * Base implementation of drawable compatibility.
 */
class DrawableCompatBase {
    public static void setTint(android.graphics.drawable.Drawable drawable, int tint) {
        if (drawable instanceof android.support.v4.graphics.drawable.TintAwareDrawable) {
            ((android.support.v4.graphics.drawable.TintAwareDrawable) (drawable)).setTint(tint);
        }
    }

    public static void setTintList(android.graphics.drawable.Drawable drawable, android.content.res.ColorStateList tint) {
        if (drawable instanceof android.support.v4.graphics.drawable.TintAwareDrawable) {
            ((android.support.v4.graphics.drawable.TintAwareDrawable) (drawable)).setTintList(tint);
        }
    }

    public static void setTintMode(android.graphics.drawable.Drawable drawable, android.graphics.PorterDuff.Mode tintMode) {
        if (drawable instanceof android.support.v4.graphics.drawable.TintAwareDrawable) {
            ((android.support.v4.graphics.drawable.TintAwareDrawable) (drawable)).setTintMode(tintMode);
        }
    }

    public static android.graphics.drawable.Drawable wrapForTinting(android.graphics.drawable.Drawable drawable) {
        if (!(drawable instanceof android.support.v4.graphics.drawable.TintAwareDrawable)) {
            return new android.support.v4.graphics.drawable.DrawableWrapperGingerbread(drawable);
        }
        return drawable;
    }

    public static void inflate(android.graphics.drawable.Drawable drawable, android.content.res.Resources res, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme t) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        drawable.inflate(res, parser, attrs);
    }
}

