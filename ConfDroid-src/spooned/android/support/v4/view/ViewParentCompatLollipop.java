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
package android.support.v4.view;


class ViewParentCompatLollipop {
    private static final java.lang.String TAG = "ViewParentCompat";

    public static boolean onStartNestedScroll(android.view.ViewParent parent, android.view.View child, android.view.View target, int nestedScrollAxes) {
        try {
            return parent.onStartNestedScroll(child, target, nestedScrollAxes);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onStartNestedScroll", e);
            return false;
        }
    }

    public static void onNestedScrollAccepted(android.view.ViewParent parent, android.view.View child, android.view.View target, int nestedScrollAxes) {
        try {
            parent.onNestedScrollAccepted(child, target, nestedScrollAxes);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onNestedScrollAccepted", e);
        }
    }

    public static void onStopNestedScroll(android.view.ViewParent parent, android.view.View target) {
        try {
            parent.onStopNestedScroll(target);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onStopNestedScroll", e);
        }
    }

    public static void onNestedScroll(android.view.ViewParent parent, android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        try {
            parent.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onNestedScroll", e);
        }
    }

    public static void onNestedPreScroll(android.view.ViewParent parent, android.view.View target, int dx, int dy, int[] consumed) {
        try {
            parent.onNestedPreScroll(target, dx, dy, consumed);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onNestedPreScroll", e);
        }
    }

    public static boolean onNestedFling(android.view.ViewParent parent, android.view.View target, float velocityX, float velocityY, boolean consumed) {
        try {
            return parent.onNestedFling(target, velocityX, velocityY, consumed);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onNestedFling", e);
            return false;
        }
    }

    public static boolean onNestedPreFling(android.view.ViewParent parent, android.view.View target, float velocityX, float velocityY) {
        try {
            return parent.onNestedPreFling(target, velocityX, velocityY);
        } catch (java.lang.AbstractMethodError e) {
            android.util.Log.e(android.support.v4.view.ViewParentCompatLollipop.TAG, (("ViewParent " + parent) + " does not implement interface ") + "method onNestedPreFling", e);
            return false;
        }
    }
}

