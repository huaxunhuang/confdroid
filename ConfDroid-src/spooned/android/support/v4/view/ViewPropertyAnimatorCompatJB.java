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
package android.support.v4.view;


class ViewPropertyAnimatorCompatJB {
    public static void withStartAction(android.view.View view, java.lang.Runnable runnable) {
        view.animate().withStartAction(runnable);
    }

    public static void withEndAction(android.view.View view, java.lang.Runnable runnable) {
        view.animate().withEndAction(runnable);
    }

    public static void withLayer(android.view.View view) {
        view.animate().withLayer();
    }

    public static void setListener(final android.view.View view, final android.support.v4.view.ViewPropertyAnimatorListener listener) {
        if (listener != null) {
            view.animate().setListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationCancel(android.animation.Animator animation) {
                    listener.onAnimationCancel(view);
                }

                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    listener.onAnimationEnd(view);
                }

                @java.lang.Override
                public void onAnimationStart(android.animation.Animator animation) {
                    listener.onAnimationStart(view);
                }
            });
        } else {
            view.animate().setListener(null);
        }
    }
}

