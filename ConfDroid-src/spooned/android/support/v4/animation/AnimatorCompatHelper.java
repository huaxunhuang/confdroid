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
package android.support.v4.animation;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class AnimatorCompatHelper {
    private static final android.support.v4.animation.AnimatorProvider IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 12) {
            IMPL = new android.support.v4.animation.HoneycombMr1AnimatorCompatProvider();
        } else {
            IMPL = new android.support.v4.animation.GingerbreadAnimatorCompatProvider();
        }
    }

    public static android.support.v4.animation.ValueAnimatorCompat emptyValueAnimator() {
        return android.support.v4.animation.AnimatorCompatHelper.IMPL.emptyValueAnimator();
    }

    private AnimatorCompatHelper() {
    }

    public static void clearInterpolator(android.view.View view) {
        android.support.v4.animation.AnimatorCompatHelper.IMPL.clearInterpolator(view);
    }
}

