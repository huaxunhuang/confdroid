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
package android.support.v4.view;


/**
 * Helper for accessing features in {@link VelocityTracker}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class VelocityTrackerCompat {
    /**
     * Interface for the full API.
     */
    interface VelocityTrackerVersionImpl {
        public float getXVelocity(android.view.VelocityTracker tracker, int pointerId);

        public float getYVelocity(android.view.VelocityTracker tracker, int pointerId);
    }

    /**
     * Interface implementation that doesn't use anything about v4 APIs.
     */
    static class BaseVelocityTrackerVersionImpl implements android.support.v4.view.VelocityTrackerCompat.VelocityTrackerVersionImpl {
        @java.lang.Override
        public float getXVelocity(android.view.VelocityTracker tracker, int pointerId) {
            return tracker.getXVelocity();
        }

        @java.lang.Override
        public float getYVelocity(android.view.VelocityTracker tracker, int pointerId) {
            return tracker.getYVelocity();
        }
    }

    /**
     * Interface implementation for devices with at least v11 APIs.
     */
    static class HoneycombVelocityTrackerVersionImpl implements android.support.v4.view.VelocityTrackerCompat.VelocityTrackerVersionImpl {
        @java.lang.Override
        public float getXVelocity(android.view.VelocityTracker tracker, int pointerId) {
            return android.support.v4.view.VelocityTrackerCompatHoneycomb.getXVelocity(tracker, pointerId);
        }

        @java.lang.Override
        public float getYVelocity(android.view.VelocityTracker tracker, int pointerId) {
            return android.support.v4.view.VelocityTrackerCompatHoneycomb.getYVelocity(tracker, pointerId);
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.view.VelocityTrackerCompat.VelocityTrackerVersionImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            IMPL = new android.support.v4.view.VelocityTrackerCompat.HoneycombVelocityTrackerVersionImpl();
        } else {
            IMPL = new android.support.v4.view.VelocityTrackerCompat.BaseVelocityTrackerVersionImpl();
        }
    }

    // -------------------------------------------------------------------
    /**
     * Call {@link VelocityTracker#getXVelocity(int)}.
     * If running on a pre-{@link android.os.Build.VERSION_CODES#HONEYCOMB} device,
     * returns {@link VelocityTracker#getXVelocity()}.
     */
    public static float getXVelocity(android.view.VelocityTracker tracker, int pointerId) {
        return android.support.v4.view.VelocityTrackerCompat.IMPL.getXVelocity(tracker, pointerId);
    }

    /**
     * Call {@link VelocityTracker#getYVelocity(int)}.
     * If running on a pre-{@link android.os.Build.VERSION_CODES#HONEYCOMB} device,
     * returns {@link VelocityTracker#getYVelocity()}.
     */
    public static float getYVelocity(android.view.VelocityTracker tracker, int pointerId) {
        return android.support.v4.view.VelocityTrackerCompat.IMPL.getYVelocity(tracker, pointerId);
    }

    private VelocityTrackerCompat() {
    }
}

