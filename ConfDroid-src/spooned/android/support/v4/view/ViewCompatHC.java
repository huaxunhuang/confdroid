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


class ViewCompatHC {
    static long getFrameTime() {
        return android.animation.ValueAnimator.getFrameDelay();
    }

    public static float getAlpha(android.view.View view) {
        return view.getAlpha();
    }

    public static void setLayerType(android.view.View view, int layerType, android.graphics.Paint paint) {
        view.setLayerType(layerType, paint);
    }

    public static int getLayerType(android.view.View view) {
        return view.getLayerType();
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        return android.view.View.resolveSizeAndState(size, measureSpec, childMeasuredState);
    }

    public static int getMeasuredWidthAndState(android.view.View view) {
        return view.getMeasuredWidthAndState();
    }

    public static int getMeasuredHeightAndState(android.view.View view) {
        return view.getMeasuredHeightAndState();
    }

    public static int getMeasuredState(android.view.View view) {
        return view.getMeasuredState();
    }

    public static float getTranslationX(android.view.View view) {
        return view.getTranslationX();
    }

    public static float getTranslationY(android.view.View view) {
        return view.getTranslationY();
    }

    public static float getX(android.view.View view) {
        return view.getX();
    }

    public static float getY(android.view.View view) {
        return view.getY();
    }

    public static float getRotation(android.view.View view) {
        return view.getRotation();
    }

    public static float getRotationX(android.view.View view) {
        return view.getRotationX();
    }

    public static float getRotationY(android.view.View view) {
        return view.getRotationY();
    }

    public static float getScaleX(android.view.View view) {
        return view.getScaleX();
    }

    public static float getScaleY(android.view.View view) {
        return view.getScaleY();
    }

    public static void setTranslationX(android.view.View view, float value) {
        view.setTranslationX(value);
    }

    public static void setTranslationY(android.view.View view, float value) {
        view.setTranslationY(value);
    }

    public static android.graphics.Matrix getMatrix(android.view.View view) {
        return view.getMatrix();
    }

    public static void setAlpha(android.view.View view, float value) {
        view.setAlpha(value);
    }

    public static void setX(android.view.View view, float value) {
        view.setX(value);
    }

    public static void setY(android.view.View view, float value) {
        view.setY(value);
    }

    public static void setRotation(android.view.View view, float value) {
        view.setRotation(value);
    }

    public static void setRotationX(android.view.View view, float value) {
        view.setRotationX(value);
    }

    public static void setRotationY(android.view.View view, float value) {
        view.setRotationY(value);
    }

    public static void setScaleX(android.view.View view, float value) {
        view.setScaleX(value);
    }

    public static void setScaleY(android.view.View view, float value) {
        view.setScaleY(value);
    }

    public static void setPivotX(android.view.View view, float value) {
        view.setPivotX(value);
    }

    public static void setPivotY(android.view.View view, float value) {
        view.setPivotY(value);
    }

    public static float getPivotX(android.view.View view) {
        return view.getPivotX();
    }

    public static float getPivotY(android.view.View view) {
        return view.getPivotY();
    }

    public static void jumpDrawablesToCurrentState(android.view.View view) {
        view.jumpDrawablesToCurrentState();
    }

    public static void setSaveFromParentEnabled(android.view.View view, boolean enabled) {
        view.setSaveFromParentEnabled(enabled);
    }

    public static void setActivated(android.view.View view, boolean activated) {
        view.setActivated(activated);
    }

    public static int combineMeasuredStates(int curState, int newState) {
        return android.view.View.combineMeasuredStates(curState, newState);
    }

    static void offsetTopAndBottom(android.view.View view, int offset) {
        view.offsetTopAndBottom(offset);
        if (view.getVisibility() == android.view.View.VISIBLE) {
            android.support.v4.view.ViewCompatHC.tickleInvalidationFlag(view);
            android.view.ViewParent parent = view.getParent();
            if (parent instanceof android.view.View) {
                android.support.v4.view.ViewCompatHC.tickleInvalidationFlag(((android.view.View) (parent)));
            }
        }
    }

    static void offsetLeftAndRight(android.view.View view, int offset) {
        view.offsetLeftAndRight(offset);
        if (view.getVisibility() == android.view.View.VISIBLE) {
            android.support.v4.view.ViewCompatHC.tickleInvalidationFlag(view);
            android.view.ViewParent parent = view.getParent();
            if (parent instanceof android.view.View) {
                android.support.v4.view.ViewCompatHC.tickleInvalidationFlag(((android.view.View) (parent)));
            }
        }
    }

    private static void tickleInvalidationFlag(android.view.View view) {
        final float y = view.getTranslationY();
        view.setTranslationY(y + 1);
        view.setTranslationY(y);
    }
}

