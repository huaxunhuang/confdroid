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


class MarginLayoutParamsCompatJellybeanMr1 {
    public static int getMarginStart(android.view.ViewGroup.MarginLayoutParams lp) {
        return lp.getMarginStart();
    }

    public static int getMarginEnd(android.view.ViewGroup.MarginLayoutParams lp) {
        return lp.getMarginEnd();
    }

    public static void setMarginStart(android.view.ViewGroup.MarginLayoutParams lp, int marginStart) {
        lp.setMarginStart(marginStart);
    }

    public static void setMarginEnd(android.view.ViewGroup.MarginLayoutParams lp, int marginEnd) {
        lp.setMarginEnd(marginEnd);
    }

    public static boolean isMarginRelative(android.view.ViewGroup.MarginLayoutParams lp) {
        return lp.isMarginRelative();
    }

    public static int getLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp) {
        return lp.getLayoutDirection();
    }

    public static void setLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        lp.setLayoutDirection(layoutDirection);
    }

    public static void resolveLayoutDirection(android.view.ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        lp.resolveLayoutDirection(layoutDirection);
    }
}

