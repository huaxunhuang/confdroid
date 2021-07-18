/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.animation;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class LogAccelerateInterpolator implements android.animation.TimeInterpolator {
    int mBase;

    int mDrift;

    final float mLogScale;

    public LogAccelerateInterpolator(int base, int drift) {
        mBase = base;
        mDrift = drift;
        mLogScale = 1.0F / android.support.v17.leanback.animation.LogAccelerateInterpolator.computeLog(1, mBase, mDrift);
    }

    static float computeLog(float t, int base, int drift) {
        return (((float) (-java.lang.Math.pow(base, -t))) + 1) + (drift * t);
    }

    @java.lang.Override
    public float getInterpolation(float t) {
        return 1 - (android.support.v17.leanback.animation.LogAccelerateInterpolator.computeLog(1 - t, mBase, mDrift) * mLogScale);
    }
}

