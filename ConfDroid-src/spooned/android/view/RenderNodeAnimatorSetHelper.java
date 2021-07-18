/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.view;


/**
 * This is a helper class to get access to methods and fields needed for RenderNodeAnimatorSet
 * that are internal or package private to android.view package.
 *
 * @unknown 
 */
public class RenderNodeAnimatorSetHelper {
    /**
     * checkstyle @hide
     */
    public static android.graphics.RenderNode getTarget(android.graphics.RecordingCanvas recordingCanvas) {
        return recordingCanvas.mNode;
    }

    /**
     * checkstyle @hide
     */
    public static long createNativeInterpolator(android.animation.TimeInterpolator interpolator, long duration) {
        if (interpolator == null) {
            // create LinearInterpolator
            return com.android.internal.view.animation.NativeInterpolatorFactoryHelper.createLinearInterpolator();
        } else
            if (android.view.RenderNodeAnimator.isNativeInterpolator(interpolator)) {
                return createNativeInterpolator();
            } else {
                return com.android.internal.view.animation.FallbackLUTInterpolator.createNativeInterpolator(interpolator, duration);
            }

    }
}

