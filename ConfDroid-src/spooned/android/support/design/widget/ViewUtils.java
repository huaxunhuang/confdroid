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


class ViewUtils {
    static final android.support.design.widget.ValueAnimatorCompat.Creator DEFAULT_ANIMATOR_CREATOR = new android.support.design.widget.ValueAnimatorCompat.Creator() {
        @java.lang.Override
        public android.support.design.widget.ValueAnimatorCompat createAnimator() {
            return new android.support.design.widget.ValueAnimatorCompat(android.os.Build.VERSION.SDK_INT >= 12 ? new android.support.design.widget.ValueAnimatorCompatImplHoneycombMr1() : new android.support.design.widget.ValueAnimatorCompatImplGingerbread());
        }
    };

    static android.support.design.widget.ValueAnimatorCompat createAnimator() {
        return android.support.design.widget.ViewUtils.DEFAULT_ANIMATOR_CREATOR.createAnimator();
    }

    static boolean objectEquals(java.lang.Object a, java.lang.Object b) {
        return (a == b) || ((a != null) && a.equals(b));
    }

    static android.graphics.PorterDuff.Mode parseTintMode(int value, android.graphics.PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3 :
                return android.graphics.PorterDuff.Mode.SRC_OVER;
            case 5 :
                return android.graphics.PorterDuff.Mode.SRC_IN;
            case 9 :
                return android.graphics.PorterDuff.Mode.SRC_ATOP;
            case 14 :
                return android.graphics.PorterDuff.Mode.MULTIPLY;
            case 15 :
                return android.graphics.PorterDuff.Mode.SCREEN;
            default :
                return defaultMode;
        }
    }
}

