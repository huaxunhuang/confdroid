/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * An interpolator where the change flings forward and overshoots the last value
 * then comes back.
 */
@com.android.internal.view.animation.HasNativeInterpolator
public class OvershootInterpolator extends android.view.animation.BaseInterpolator implements com.android.internal.view.animation.NativeInterpolatorFactory {
    private final float mTension;

    public OvershootInterpolator() {
        mTension = 2.0F;
    }

    /**
     *
     *
     * @param tension
     * 		Amount of overshoot. When tension equals 0.0f, there is
     * 		no overshoot and the interpolator becomes a simple
     * 		deceleration interpolator.
     */
    public OvershootInterpolator(float tension) {
        mTension = tension;
    }

    public OvershootInterpolator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    /**
     *
     *
     * @unknown 
     */
    public OvershootInterpolator(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.OvershootInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.OvershootInterpolator);
        }
        mTension = a.getFloat(R.styleable.OvershootInterpolator_tension, 2.0F);
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    public float getInterpolation(float t) {
        // _o(t) = t * t * ((tension + 1) * t + tension)
        // o(t) = _o(t - 1) + 1
        t -= 1.0F;
        return ((t * t) * (((mTension + 1) * t) + mTension)) + 1.0F;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public long createNativeInterpolator() {
        return com.android.internal.view.animation.NativeInterpolatorFactoryHelper.createOvershootInterpolator(mTension);
    }
}

