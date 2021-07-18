/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Helper for accessing newer features in View introduced in ICS.
 */
class ViewCompatICS {
    public static boolean canScrollHorizontally(android.view.View v, int direction) {
        return v.canScrollHorizontally(direction);
    }

    public static boolean canScrollVertically(android.view.View v, int direction) {
        return v.canScrollVertically(direction);
    }

    public static void setAccessibilityDelegate(android.view.View v, @android.support.annotation.Nullable
    java.lang.Object delegate) {
        v.setAccessibilityDelegate(((android.view.View.AccessibilityDelegate) (delegate)));
    }

    public static void onPopulateAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
        v.onPopulateAccessibilityEvent(event);
    }

    public static void onInitializeAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
        v.onInitializeAccessibilityEvent(event);
    }

    public static void onInitializeAccessibilityNodeInfo(android.view.View v, java.lang.Object info) {
        v.onInitializeAccessibilityNodeInfo(((android.view.accessibility.AccessibilityNodeInfo) (info)));
    }

    public static void setFitsSystemWindows(android.view.View view, boolean fitSystemWindows) {
        view.setFitsSystemWindows(fitSystemWindows);
    }
}

