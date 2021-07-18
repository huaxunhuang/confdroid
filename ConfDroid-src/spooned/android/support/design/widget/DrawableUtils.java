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
package android.support.design.widget;


/**
 * Caution. Gross hacks ahead.
 */
class DrawableUtils {
    private static final java.lang.String LOG_TAG = "DrawableUtils";

    private static java.lang.reflect.Method sSetConstantStateMethod;

    private static boolean sSetConstantStateMethodFetched;

    private DrawableUtils() {
    }

    static boolean setContainerConstantState(android.graphics.drawable.DrawableContainer drawable, android.graphics.drawable.Drawable.ConstantState constantState) {
        // We can use getDeclaredMethod() on v9+
        return android.support.design.widget.DrawableUtils.setContainerConstantStateV9(drawable, constantState);
    }

    private static boolean setContainerConstantStateV9(android.graphics.drawable.DrawableContainer drawable, android.graphics.drawable.Drawable.ConstantState constantState) {
        if (!android.support.design.widget.DrawableUtils.sSetConstantStateMethodFetched) {
            try {
                android.support.design.widget.DrawableUtils.sSetConstantStateMethod = android.graphics.drawable.DrawableContainer.class.getDeclaredMethod("setConstantState", android.graphics.drawable.DrawableContainer.DrawableContainerState.class);
                android.support.design.widget.DrawableUtils.sSetConstantStateMethod.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.e(android.support.design.widget.DrawableUtils.LOG_TAG, "Could not fetch setConstantState(). Oh well.");
            }
            android.support.design.widget.DrawableUtils.sSetConstantStateMethodFetched = true;
        }
        if (android.support.design.widget.DrawableUtils.sSetConstantStateMethod != null) {
            try {
                android.support.design.widget.DrawableUtils.sSetConstantStateMethod.invoke(drawable, constantState);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.support.design.widget.DrawableUtils.LOG_TAG, "Could not invoke setConstantState(). Oh well.");
            }
        }
        return false;
    }
}

