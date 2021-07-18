/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * An interpolator where the rate of change starts out slowly and
 * and then accelerates.
 */
@com.android.internal.view.animation.HasNativeInterpolator
public class AccelerateInterpolator extends android.view.animation.BaseInterpolator implements com.android.internal.view.animation.NativeInterpolatorFactory {
    private final float mFactor;

    private final double mDoubleFactor;

    public AccelerateInterpolator() {
        mFactor = 1.0F;
        mDoubleFactor = 2.0;
    }

    /**
     * Constructor
     *
     * @param factor
     * 		Degree to which the animation should be eased. Seting
     * 		factor to 1.0f produces a y=x^2 parabola. Increasing factor above
     * 		1.0f  exaggerates the ease-in effect (i.e., it starts even
     * 		slower and ends evens faster)
     */
    public AccelerateInterpolator(float factor) {
        mFactor = factor;
        mDoubleFactor = 2 * mFactor;
    }

    public AccelerateInterpolator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    /**
     *
     *
     * @unknown 
     */
    public AccelerateInterpolator(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.AccelerateInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.AccelerateInterpolator);
        }
        mFactor = a.getFloat(R.styleable.AccelerateInterpolator_factor, 1.0F);
        mDoubleFactor = 2 * mFactor;
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    public float getInterpolation(float input) {
        if (mFactor == 1.0F) {
            return input * input;
        } else {
            return ((float) (java.lang.Math.pow(input, mDoubleFactor)));
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public long createNativeInterpolator() {
        return com.android.internal.view.animation.NativeInterpolatorFactoryHelper.createAccelerateInterpolator(mFactor);
    }
}

