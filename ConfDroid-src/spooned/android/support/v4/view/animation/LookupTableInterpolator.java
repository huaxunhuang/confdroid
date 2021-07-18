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
package android.support.v4.view.animation;


/**
 * An {@link Interpolator} that uses a lookup table to compute an interpolation based on a
 * given input.
 */
abstract class LookupTableInterpolator implements android.view.animation.Interpolator {
    private final float[] mValues;

    private final float mStepSize;

    public LookupTableInterpolator(float[] values) {
        mValues = values;
        mStepSize = 1.0F / (mValues.length - 1);
    }

    @java.lang.Override
    public float getInterpolation(float input) {
        if (input >= 1.0F) {
            return 1.0F;
        }
        if (input <= 0.0F) {
            return 0.0F;
        }
        // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
        // we lerp (linearly interpolate) in the return statement
        int position = java.lang.Math.min(((int) (input * (mValues.length - 1))), mValues.length - 2);
        // Calculate values to account for small offsets as the lookup table has discrete values
        float quantized = position * mStepSize;
        float diff = input - quantized;
        float weight = diff / mStepSize;
        // Linearly interpolate between the table values
        return mValues[position] + (weight * (mValues[position + 1] - mValues[position]));
    }
}

