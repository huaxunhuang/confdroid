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


class ViewPropertyAnimatorCompatICS {
    public static void setDuration(android.view.View view, long value) {
        view.animate().setDuration(value);
    }

    public static void alpha(android.view.View view, float value) {
        view.animate().alpha(value);
    }

    public static void translationX(android.view.View view, float value) {
        view.animate().translationX(value);
    }

    public static void translationY(android.view.View view, float value) {
        view.animate().translationY(value);
    }

    public static long getDuration(android.view.View view) {
        return view.animate().getDuration();
    }

    public static void setInterpolator(android.view.View view, android.view.animation.Interpolator value) {
        view.animate().setInterpolator(value);
    }

    public static void setStartDelay(android.view.View view, long value) {
        view.animate().setStartDelay(value);
    }

    public static long getStartDelay(android.view.View view) {
        return view.animate().getStartDelay();
    }

    public static void alphaBy(android.view.View view, float value) {
        view.animate().alphaBy(value);
    }

    public static void rotation(android.view.View view, float value) {
        view.animate().rotation(value);
    }

    public static void rotationBy(android.view.View view, float value) {
        view.animate().rotationBy(value);
    }

    public static void rotationX(android.view.View view, float value) {
        view.animate().rotationX(value);
    }

    public static void rotationXBy(android.view.View view, float value) {
        view.animate().rotationXBy(value);
    }

    public static void rotationY(android.view.View view, float value) {
        view.animate().rotationY(value);
    }

    public static void rotationYBy(android.view.View view, float value) {
        view.animate().rotationYBy(value);
    }

    public static void scaleX(android.view.View view, float value) {
        view.animate().scaleX(value);
    }

    public static void scaleXBy(android.view.View view, float value) {
        view.animate().scaleXBy(value);
    }

    public static void scaleY(android.view.View view, float value) {
        view.animate().scaleY(value);
    }

    public static void scaleYBy(android.view.View view, float value) {
        view.animate().scaleYBy(value);
    }

    public static void cancel(android.view.View view) {
        view.animate().cancel();
    }

    public static void x(android.view.View view, float value) {
        view.animate().x(value);
    }

    public static void xBy(android.view.View view, float value) {
        view.animate().xBy(value);
    }

    public static void y(android.view.View view, float value) {
        view.animate().y(value);
    }

    public static void yBy(android.view.View view, float value) {
        view.animate().yBy(value);
    }

    public static void translationXBy(android.view.View view, float value) {
        view.animate().translationXBy(value);
    }

    public static void translationYBy(android.view.View view, float value) {
        view.animate().translationYBy(value);
    }

    public static void start(android.view.View view) {
        view.animate().start();
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

