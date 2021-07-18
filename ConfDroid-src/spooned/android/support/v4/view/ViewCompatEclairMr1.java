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


class ViewCompatEclairMr1 {
    public static final java.lang.String TAG = "ViewCompat";

    private static java.lang.reflect.Method sChildrenDrawingOrderMethod;

    public static boolean isOpaque(android.view.View view) {
        return view.isOpaque();
    }

    public static void setChildrenDrawingOrderEnabled(android.view.ViewGroup viewGroup, boolean enabled) {
        if (android.support.v4.view.ViewCompatEclairMr1.sChildrenDrawingOrderMethod == null) {
            try {
                android.support.v4.view.ViewCompatEclairMr1.sChildrenDrawingOrderMethod = android.view.ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", boolean.class);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.e(android.support.v4.view.ViewCompatEclairMr1.TAG, "Unable to find childrenDrawingOrderEnabled", e);
            }
            android.support.v4.view.ViewCompatEclairMr1.sChildrenDrawingOrderMethod.setAccessible(true);
        }
        try {
            android.support.v4.view.ViewCompatEclairMr1.sChildrenDrawingOrderMethod.invoke(viewGroup, enabled);
        } catch (java.lang.IllegalAccessException e) {
            android.util.Log.e(android.support.v4.view.ViewCompatEclairMr1.TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(android.support.v4.view.ViewCompatEclairMr1.TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            android.util.Log.e(android.support.v4.view.ViewCompatEclairMr1.TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
        }
    }
}

