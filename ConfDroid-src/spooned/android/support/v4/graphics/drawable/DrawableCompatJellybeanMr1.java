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
 * Implementation of drawable compatibility that can call Jellybean MR1 APIs.
 */
class DrawableCompatJellybeanMr1 {
    private static final java.lang.String TAG = "DrawableCompatJellybeanMr1";

    private static java.lang.reflect.Method sSetLayoutDirectionMethod;

    private static boolean sSetLayoutDirectionMethodFetched;

    private static java.lang.reflect.Method sGetLayoutDirectionMethod;

    private static boolean sGetLayoutDirectionMethodFetched;

    public static boolean setLayoutDirection(android.graphics.drawable.Drawable drawable, int layoutDirection) {
        if (!android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched) {
            try {
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod = android.graphics.drawable.Drawable.class.getDeclaredMethod("setLayoutDirection", int.class);
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.i(android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.TAG, "Failed to retrieve setLayoutDirection(int) method", e);
            }
            android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched = true;
        }
        if (android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod != null) {
            try {
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod.invoke(drawable, layoutDirection);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Log.i(android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.TAG, "Failed to invoke setLayoutDirection(int) via reflection", e);
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod = null;
            }
        }
        return false;
    }

    public static int getLayoutDirection(android.graphics.drawable.Drawable drawable) {
        if (!android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched) {
            try {
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod = android.graphics.drawable.Drawable.class.getDeclaredMethod("getLayoutDirection");
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.i(android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.TAG, "Failed to retrieve getLayoutDirection() method", e);
            }
            android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched = true;
        }
        if (android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod != null) {
            try {
                return ((int) (android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod.invoke(drawable)));
            } catch (java.lang.Exception e) {
                android.util.Log.i(android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.TAG, "Failed to invoke getLayoutDirection() via reflection", e);
                android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod = null;
            }
        }
        return -1;
    }
}

