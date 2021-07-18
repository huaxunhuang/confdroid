/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.view;


/**
 * Helper for accessing features in <code>ScaleGestureDetector</code> introduced
 * after API level 19 (KitKat) in a backwards compatible fashion.
 */
public final class ScaleGestureDetectorCompat {
    static final android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl IMPL;

    interface ScaleGestureDetectorImpl {
        public void setQuickScaleEnabled(java.lang.Object o, boolean enabled);

        public boolean isQuickScaleEnabled(java.lang.Object o);
    }

    private static class BaseScaleGestureDetectorImpl implements android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl {
        BaseScaleGestureDetectorImpl() {
        }

        @java.lang.Override
        public void setQuickScaleEnabled(java.lang.Object o, boolean enabled) {
            // Intentionally blank
        }

        @java.lang.Override
        public boolean isQuickScaleEnabled(java.lang.Object o) {
            return false;
        }
    }

    private static class ScaleGestureDetectorCompatKitKatImpl implements android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl {
        ScaleGestureDetectorCompatKitKatImpl() {
        }

        @java.lang.Override
        public void setQuickScaleEnabled(java.lang.Object o, boolean enabled) {
            android.support.v4.view.ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(o, enabled);
        }

        @java.lang.Override
        public boolean isQuickScaleEnabled(java.lang.Object o) {
            return android.support.v4.view.ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(o);
        }
    }

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 19) {
            // KitKat
            IMPL = new android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorCompatKitKatImpl();
        } else {
            IMPL = new android.support.v4.view.ScaleGestureDetectorCompat.BaseScaleGestureDetectorImpl();
        }
    }

    private ScaleGestureDetectorCompat() {
    }

    /**
     * Set whether the associated <code>OnScaleGestureListener</code> should receive onScale
     * callbacks when the user performs a doubleTap followed by a swipe. Note that this is enabled
     * by default if the app targets API 19 and newer.
     *
     * @param enabled
     * 		true to enable quick scaling, false to disable
     */
    public static void setQuickScaleEnabled(java.lang.Object scaleGestureDetector, boolean enabled) {
        android.support.v4.view.ScaleGestureDetectorCompat.IMPL.setQuickScaleEnabled(scaleGestureDetector, enabled);
    }

    /**
     * Return whether the quick scale gesture, in which the user performs a double tap followed by a
     * swipe, should perform scaling. See <code>setQuickScaleEnabled(Object, boolean)<code>.
     */
    public static boolean isQuickScaleEnabled(java.lang.Object scaleGestureDetector) {
        return android.support.v4.view.ScaleGestureDetectorCompat.IMPL.isQuickScaleEnabled(scaleGestureDetector);// KitKat

    }
}

