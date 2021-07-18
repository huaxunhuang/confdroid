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
package android.support.v4.widget;


class PopupWindowCompatApi21 {
    private static final java.lang.String TAG = "PopupWindowCompatApi21";

    private static java.lang.reflect.Field sOverlapAnchorField;

    static {
        try {
            android.support.v4.widget.PopupWindowCompatApi21.sOverlapAnchorField = android.widget.PopupWindow.class.getDeclaredField("mOverlapAnchor");
            android.support.v4.widget.PopupWindowCompatApi21.sOverlapAnchorField.setAccessible(true);
        } catch (java.lang.NoSuchFieldException e) {
            android.util.Log.i(android.support.v4.widget.PopupWindowCompatApi21.TAG, "Could not fetch mOverlapAnchor field from PopupWindow", e);
        }
    }

    static void setOverlapAnchor(android.widget.PopupWindow popupWindow, boolean overlapAnchor) {
        if (android.support.v4.widget.PopupWindowCompatApi21.sOverlapAnchorField != null) {
            try {
                android.support.v4.widget.PopupWindowCompatApi21.sOverlapAnchorField.set(popupWindow, overlapAnchor);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.i(android.support.v4.widget.PopupWindowCompatApi21.TAG, "Could not set overlap anchor field in PopupWindow", e);
            }
        }
    }

    static boolean getOverlapAnchor(android.widget.PopupWindow popupWindow) {
        if (android.support.v4.widget.PopupWindowCompatApi21.sOverlapAnchorField != null) {
            try {
                return ((java.lang.Boolean) (android.support.v4.widget.PopupWindowCompatApi21.sOverlapAnchorField.get(popupWindow)));
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.i(android.support.v4.widget.PopupWindowCompatApi21.TAG, "Could not get overlap anchor field in PopupWindow", e);
            }
        }
        return false;
    }
}

