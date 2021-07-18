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
 * Jellybean MR1 - specific View API access.
 */
class ViewCompatJellybeanMr1 {
    public static int getLabelFor(android.view.View view) {
        return view.getLabelFor();
    }

    public static void setLabelFor(android.view.View view, int id) {
        view.setLabelFor(id);
    }

    public static void setLayerPaint(android.view.View view, android.graphics.Paint paint) {
        view.setLayerPaint(paint);
    }

    public static int getLayoutDirection(android.view.View view) {
        return view.getLayoutDirection();
    }

    public static void setLayoutDirection(android.view.View view, int layoutDirection) {
        view.setLayoutDirection(layoutDirection);
    }

    public static int getPaddingStart(android.view.View view) {
        return view.getPaddingStart();
    }

    public static int getPaddingEnd(android.view.View view) {
        return view.getPaddingEnd();
    }

    public static void setPaddingRelative(android.view.View view, int start, int top, int end, int bottom) {
        view.setPaddingRelative(start, top, end, bottom);
    }

    public static int getWindowSystemUiVisibility(android.view.View view) {
        return view.getWindowSystemUiVisibility();
    }

    public static boolean isPaddingRelative(android.view.View view) {
        return view.isPaddingRelative();
    }

    public static android.view.Display getDisplay(android.view.View view) {
        return view.getDisplay();
    }
}

