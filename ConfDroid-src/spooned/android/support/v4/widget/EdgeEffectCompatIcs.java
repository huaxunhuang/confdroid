/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.widget;


/**
 * Stub implementation that contains a real EdgeEffect on ICS.
 *
 * This class is an implementation detail for EdgeEffectCompat
 * and should not be used directly.
 */
class EdgeEffectCompatIcs {
    public static java.lang.Object newEdgeEffect(android.content.Context context) {
        return new android.widget.EdgeEffect(context);
    }

    public static void setSize(java.lang.Object edgeEffect, int width, int height) {
        ((android.widget.EdgeEffect) (edgeEffect)).setSize(width, height);
    }

    public static boolean isFinished(java.lang.Object edgeEffect) {
        return ((android.widget.EdgeEffect) (edgeEffect)).isFinished();
    }

    public static void finish(java.lang.Object edgeEffect) {
        ((android.widget.EdgeEffect) (edgeEffect)).finish();
    }

    public static boolean onPull(java.lang.Object edgeEffect, float deltaDistance) {
        ((android.widget.EdgeEffect) (edgeEffect)).onPull(deltaDistance);
        return true;
    }

    public static boolean onRelease(java.lang.Object edgeEffect) {
        android.widget.EdgeEffect eff = ((android.widget.EdgeEffect) (edgeEffect));
        eff.onRelease();
        return eff.isFinished();
    }

    public static boolean onAbsorb(java.lang.Object edgeEffect, int velocity) {
        ((android.widget.EdgeEffect) (edgeEffect)).onAbsorb(velocity);
        return true;
    }

    public static boolean draw(java.lang.Object edgeEffect, android.graphics.Canvas canvas) {
        return ((android.widget.EdgeEffect) (edgeEffect)).draw(canvas);
    }
}

