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
package android.graphics.animation;


/**
 * Delegate used to provide new implementation of a select few methods of {@link NativeInterpolatorFactory}
 * <p>
 * Through the layoutlib_create tool, the original  methods of NativeInterpolatorFactory have
 * been replaced by calls to methods of the same name in this delegate class.
 */
@java.lang.SuppressWarnings("unused")
public class NativeInterpolatorFactory_Delegate {
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.view.animation.Interpolator> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.view.animation.Interpolator.class);

    public static android.view.animation.Interpolator getDelegate(long nativePtr) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.getDelegate(nativePtr);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createAccelerateDecelerateInterpolator() {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.AccelerateDecelerateInterpolator());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createAccelerateInterpolator(float factor) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.AccelerateInterpolator(factor));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createAnticipateInterpolator(float tension) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.AnticipateInterpolator(tension));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createAnticipateOvershootInterpolator(float tension) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.AnticipateOvershootInterpolator(tension));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createBounceInterpolator() {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.BounceInterpolator());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createCycleInterpolator(float cycles) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.CycleInterpolator(cycles));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createDecelerateInterpolator(float factor) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.DecelerateInterpolator(factor));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createLinearInterpolator() {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.LinearInterpolator());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createOvershootInterpolator(float tension) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.OvershootInterpolator(tension));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createPathInterpolator(float[] x, float[] y) {
        android.graphics.Path path = new android.graphics.Path();
        path.moveTo(x[0], y[0]);
        for (int i = 1; i < x.length; i++) {
            path.lineTo(x[i], y[i]);
        }
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.view.animation.PathInterpolator(path));
    }

    private static class LutInterpolator extends android.view.animation.BaseInterpolator {
        private final float[] mValues;

        private final int mSize;

        private LutInterpolator(float[] values) {
            mValues = values;
            mSize = mValues.length;
        }

        @java.lang.Override
        public float getInterpolation(float input) {
            float lutpos = input * (mSize - 1);
            if (lutpos >= (mSize - 1)) {
                return mValues[mSize - 1];
            }
            int ipart = ((int) (lutpos));
            float weight = lutpos - ipart;
            int i1 = ipart;
            int i2 = java.lang.Math.min(i1 + 1, mSize - 1);
            assert (i1 >= 0) && (i2 >= 0) : "Negatives in the interpolation";
            return android.util.MathUtils.lerp(mValues[i1], mValues[i2], weight);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long createLutInterpolator(float[] values) {
        return android.graphics.animation.NativeInterpolatorFactory_Delegate.sManager.addNewDelegate(new android.graphics.animation.NativeInterpolatorFactory_Delegate.LutInterpolator(values));
    }
}

