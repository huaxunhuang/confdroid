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
public class ViewUtils {
    private static final java.lang.String TAG = "ViewUtils";

    private static java.lang.reflect.Method sComputeFitSystemWindowsMethod;

    static {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                android.support.v7.widget.ViewUtils.sComputeFitSystemWindowsMethod = android.view.View.class.getDeclaredMethod("computeFitSystemWindows", android.graphics.Rect.class, android.graphics.Rect.class);
                if (!android.support.v7.widget.ViewUtils.sComputeFitSystemWindowsMethod.isAccessible()) {
                    android.support.v7.widget.ViewUtils.sComputeFitSystemWindowsMethod.setAccessible(true);
                }
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.d(android.support.v7.widget.ViewUtils.TAG, "Could not find method computeFitSystemWindows. Oh well.");
            }
        }
    }

    private ViewUtils() {
    }

    public static boolean isLayoutRtl(android.view.View view) {
        return android.support.v4.view.ViewCompat.getLayoutDirection(view) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Merge two states as returned by {@link ViewCompat#getMeasuredState(android.view.View)} ()}.
     *
     * @param curState
     * 		The current state as returned from a view or the result
     * 		of combining multiple views.
     * @param newState
     * 		The new view state to combine.
     * @return Returns a new integer reflecting the combination of the two
    states.
     */
    public static int combineMeasuredStates(int curState, int newState) {
        return curState | newState;
    }

    /**
     * Allow calling the hidden method {@code computeFitSystemWindows(Rect, Rect)} through
     * reflection on {@code view}.
     */
    public static void computeFitSystemWindows(android.view.View view, android.graphics.Rect inoutInsets, android.graphics.Rect outLocalInsets) {
        if (android.support.v7.widget.ViewUtils.sComputeFitSystemWindowsMethod != null) {
            try {
                android.support.v7.widget.ViewUtils.sComputeFitSystemWindowsMethod.invoke(view, inoutInsets, outLocalInsets);
            } catch (java.lang.Exception e) {
                android.util.Log.d(android.support.v7.widget.ViewUtils.TAG, "Could not invoke computeFitSystemWindows", e);
            }
        }
    }

    /**
     * Allow calling the hidden method {@code makeOptionalFitsSystem()} through reflection on
     * {@code view}.
     */
    public static void makeOptionalFitsSystemWindows(android.view.View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            try {
                // We need to use getMethod() for makeOptionalFitsSystemWindows since both View
                // and ViewGroup implement the method
                java.lang.reflect.Method method = view.getClass().getMethod("makeOptionalFitsSystemWindows");
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(view);
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.d(android.support.v7.widget.ViewUtils.TAG, "Could not find method makeOptionalFitsSystemWindows. Oh well...");
            } catch (java.lang.reflect.InvocationTargetException e) {
                android.util.Log.d(android.support.v7.widget.ViewUtils.TAG, "Could not invoke makeOptionalFitsSystemWindows", e);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.d(android.support.v7.widget.ViewUtils.TAG, "Could not invoke makeOptionalFitsSystemWindows", e);
            }
        }
    }
}

