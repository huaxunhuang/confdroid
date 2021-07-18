/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.view;


/**
 * Delegate used to provide new implementation of a select few methods of {@link Choreographer}
 *
 * Through the layoutlib_create tool, the original  methods of Choreographer have been
 * replaced by calls to methods of the same name in this delegate class.
 */
public class Choreographer_Delegate {
    private static final java.util.concurrent.atomic.AtomicReference<android.view.Choreographer> mInstance = new java.util.concurrent.atomic.AtomicReference<android.view.Choreographer>();

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static android.view.Choreographer getInstance() {
        if (android.view.Choreographer_Delegate.mInstance.get() == null) {
            android.view.Choreographer_Delegate.mInstance.compareAndSet(null, android.view.Choreographer.getInstance_Original());
        }
        return android.view.Choreographer_Delegate.mInstance.get();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static float getRefreshRate() {
        return 60.0F;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void scheduleVsyncLocked(android.view.Choreographer thisChoreographer) {
        // do nothing
    }

    public static void doFrame(long frameTimeNanos) {
        android.view.Choreographer thisChoreographer = android.view.Choreographer.getInstance();
        android.view.animation.AnimationUtils.lockAnimationClock(frameTimeNanos / android.util.TimeUtils.NANOS_PER_MS);
        try {
            thisChoreographer.mLastFrameTimeNanos = frameTimeNanos - thisChoreographer.getFrameIntervalNanos();
            thisChoreographer.mFrameInfo.markInputHandlingStart();
            thisChoreographer.doCallbacks(android.view.Choreographer.CALLBACK_INPUT, frameTimeNanos);
            thisChoreographer.mFrameInfo.markAnimationsStart();
            thisChoreographer.doCallbacks(android.view.Choreographer.CALLBACK_ANIMATION, frameTimeNanos);
            thisChoreographer.mFrameInfo.markPerformTraversalsStart();
            thisChoreographer.doCallbacks(android.view.Choreographer.CALLBACK_TRAVERSAL, frameTimeNanos);
            thisChoreographer.doCallbacks(android.view.Choreographer.CALLBACK_COMMIT, frameTimeNanos);
        } finally {
            android.view.animation.AnimationUtils.unlockAnimationClock();
        }
    }

    public static void clearFrames() {
        android.view.Choreographer thisChoreographer = android.view.Choreographer.getInstance();
        thisChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_INPUT, null, null);
        thisChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_ANIMATION, null, null);
        thisChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_TRAVERSAL, null, null);
        thisChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_COMMIT, null, null);
        // Release animation handler instance since it holds references to the callbacks
        AnimationHandler.sAnimatorHandler.set(null);
    }

    public static void dispose() {
        android.view.Choreographer_Delegate.clearFrames();
        android.view.Choreographer.releaseInstance();
    }
}

