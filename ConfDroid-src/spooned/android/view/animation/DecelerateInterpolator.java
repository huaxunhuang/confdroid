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
 * An interpolator where the rate of change starts out quickly and
 * and then decelerates.
 */
@com.android.internal.view.animation.HasNativeInterpolator
public class DecelerateInterpolator extends android.view.animation.BaseInterpolator implements com.android.internal.view.animation.NativeInterpolatorFactory {
    public DecelerateInterpolator() {
    }

    /**
     * Constructor
     *
     * @param factor
     * 		Degree to which the animation should be eased. Setting factor to 1.0f produces
     * 		an upside-down y=x^2 parabola. Increasing factor above 1.0f exaggerates the
     * 		ease-out effect (i.e., it starts even faster and ends evens slower).
     */
    public DecelerateInterpolator(float factor) {
        mFactor = factor;
    }

    public DecelerateInterpolator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    /**
     *
     *
     * @unknown 
     */
    public DecelerateInterpolator(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.DecelerateInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.DecelerateInterpolator);
        }
        mFactor = a.getFloat(R.styleable.DecelerateInterpolator_factor, 1.0F);
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    public float getInterpolation(float input) {
        float result;
        if (mFactor == 1.0F) {
            result = ((float) (1.0F - ((1.0F - input) * (1.0F - input))));
        } else {
            result = ((float) (1.0F - java.lang.Math.pow(1.0F - input, 2 * mFactor)));
        }
        return result;
    }

    private float mFactor = 1.0F;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public long createNativeInterpolator() {
        return com.android.internal.view.animation.NativeInterpolatorFactoryHelper.createDecelerateInterpolator(mFactor);
    }
}

