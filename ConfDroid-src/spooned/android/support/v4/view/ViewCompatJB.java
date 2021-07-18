/**
 * Copyright (C) 2012 The Android Open Source Project
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


/**
 * Jellybean-specific View API access
 */
class ViewCompatJB {
    public static boolean hasTransientState(android.view.View view) {
        return view.hasTransientState();
    }

    public static void setHasTransientState(android.view.View view, boolean hasTransientState) {
        view.setHasTransientState(hasTransientState);
    }

    public static void postInvalidateOnAnimation(android.view.View view) {
        view.postInvalidateOnAnimation();
    }

    public static void postInvalidateOnAnimation(android.view.View view, int left, int top, int right, int bottom) {
        view.postInvalidate(left, top, right, bottom);
    }

    public static void postOnAnimation(android.view.View view, java.lang.Runnable action) {
        view.postOnAnimation(action);
    }

    public static void postOnAnimationDelayed(android.view.View view, java.lang.Runnable action, long delayMillis) {
        view.postOnAnimationDelayed(action, delayMillis);
    }

    public static int getImportantForAccessibility(android.view.View view) {
        return view.getImportantForAccessibility();
    }

    public static void setImportantForAccessibility(android.view.View view, int mode) {
        view.setImportantForAccessibility(mode);
    }

    public static boolean performAccessibilityAction(android.view.View view, int action, android.os.Bundle arguments) {
        return view.performAccessibilityAction(action, arguments);
    }

    public static java.lang.Object getAccessibilityNodeProvider(android.view.View view) {
        return view.getAccessibilityNodeProvider();
    }

    public static android.view.ViewParent getParentForAccessibility(android.view.View view) {
        return view.getParentForAccessibility();
    }

    public static int getMinimumWidth(android.view.View view) {
        return view.getMinimumWidth();
    }

    public static int getMinimumHeight(android.view.View view) {
        return view.getMinimumHeight();
    }

    public static void requestApplyInsets(android.view.View view) {
        view.requestFitSystemWindows();
    }

    public static boolean getFitsSystemWindows(android.view.View view) {
        return view.getFitsSystemWindows();
    }

    public static boolean hasOverlappingRendering(android.view.View view) {
        return view.hasOverlappingRendering();
    }

    public static void setBackground(android.view.View view, android.graphics.drawable.Drawable drawable) {
        view.setBackground(drawable);
    }
}

