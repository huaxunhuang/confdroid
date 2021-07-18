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
 * Repeats the animation for a specified number of cycles. The
 * rate of change follows a sinusoidal pattern.
 */
@com.android.internal.view.animation.HasNativeInterpolator
public class CycleInterpolator extends android.view.animation.BaseInterpolator implements com.android.internal.view.animation.NativeInterpolatorFactory {
    public CycleInterpolator(float cycles) {
        mCycles = cycles;
    }

    public CycleInterpolator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    /**
     *
     *
     * @unknown 
     */
    public CycleInterpolator(android.content.res.Resources resources, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.CycleInterpolator, 0, 0);
        } else {
            a = resources.obtainAttributes(attrs, R.styleable.CycleInterpolator);
        }
        mCycles = a.getFloat(R.styleable.CycleInterpolator_cycles, 1.0F);
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    public float getInterpolation(float input) {
        return ((float) (java.lang.Math.sin(((2 * mCycles) * java.lang.Math.PI) * input)));
    }

    private float mCycles;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public long createNativeInterpolator() {
        return com.android.internal.view.animation.NativeInterpolatorFactoryHelper.createCycleInterpolator(mCycles);
    }
}

