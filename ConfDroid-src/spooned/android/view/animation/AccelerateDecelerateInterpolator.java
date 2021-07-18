/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.view.animation;


/**
 * An interpolator where the rate of change starts and ends slowly but
 * accelerates through the middle.
 */
@com.android.internal.view.animation.HasNativeInterpolator
public class AccelerateDecelerateInterpolator extends android.view.animation.BaseInterpolator implements com.android.internal.view.animation.NativeInterpolatorFactory {
    public AccelerateDecelerateInterpolator() {
    }

    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    public AccelerateDecelerateInterpolator(android.content.Context context, android.util.AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return ((float) (java.lang.Math.cos((input + 1) * java.lang.Math.PI) / 2.0F)) + 0.5F;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public long createNativeInterpolator() {
        return com.android.internal.view.animation.NativeInterpolatorFactoryHelper.createAccelerateDecelerateInterpolator();
    }
}

