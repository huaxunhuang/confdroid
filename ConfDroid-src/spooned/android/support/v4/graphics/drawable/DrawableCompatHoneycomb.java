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
 * Implementation of drawable compatibility that can call Honeycomb APIs.
 */
class DrawableCompatHoneycomb {
    public static void jumpToCurrentState(android.graphics.drawable.Drawable drawable) {
        drawable.jumpToCurrentState();
    }

    public static android.graphics.drawable.Drawable wrapForTinting(android.graphics.drawable.Drawable drawable) {
        if (!(drawable instanceof android.support.v4.graphics.drawable.TintAwareDrawable)) {
            return new android.support.v4.graphics.drawable.DrawableWrapperHoneycomb(drawable);
        }
        return drawable;
    }
}

