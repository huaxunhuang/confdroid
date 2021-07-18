/**
 * Copyright (C) 2020 The Android Open Source Project
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
 * Insets animation runner that uses {@link InsetsAnimationThread} to run the animation off from the
 * main thread.
 *
 * @unknown 
 */
public class InsetsAnimationThreadControlRunner implements android.view.InsetsAnimationControlRunner {
    private static final java.lang.String TAG = "InsetsAnimThreadRunner";

    private final android.view.InsetsAnimationControlImpl mControl;

    private final android.view.InsetsAnimationControlCallbacks mOuterCallbacks;

    private final android.os.Handler mMainThreadHandler;

    private final android.view.InsetsState mState = new android.view.InsetsState();

    private final android.view.InsetsAnimationControlCallbacks mCallbacks = new android.view.InsetsAnimationControlCallbacks() {
        private final float[] mTmpFloat9 = new float[9];

        @java.lang.Override
        @android.annotation.UiThread
        public void startAnimation(android.view.InsetsAnimationControlImpl controller, android.view.WindowInsetsAnimationControlListener listener, int types, android.view.WindowInsetsAnimation animation, android.view.WindowInsetsAnimation.Bounds bounds) {
            // Animation will be started in constructor already.
        }

        @java.lang.Override
        public void scheduleApplyChangeInsets(android.view.InsetsAnimationControlRunner runner) {
            mControl.applyChangeInsets(mState);
        }

        @java.lang.Override
        public void notifyFinished(android.view.InsetsAnimationControlRunner runner, boolean shown) {
            android.os.Trace.asyncTraceEnd(Trace.TRACE_TAG_VIEW, "InsetsAsyncAnimation: " + android.view.WindowInsets.Type.toString(runner.getTypes()), runner.getTypes());
            releaseControls(mControl.getControls());
            mMainThreadHandler.post(() -> mOuterCallbacks.notifyFinished(android.view.InsetsAnimationThreadControlRunner.this, shown));
        }

        @java.lang.Override
        public void applySurfaceParams(android.view.SyncRtSurfaceTransactionApplier.SurfaceParams... params) {
            if (android.view.InsetsController.DEBUG)
                android.util.Log.d(android.view.InsetsAnimationThreadControlRunner.TAG, "applySurfaceParams");

            android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
            for (int i = params.length - 1; i >= 0; i--) {
                android.view.SyncRtSurfaceTransactionApplier.SurfaceParams surfaceParams = params[i];
                android.view.SyncRtSurfaceTransactionApplier.applyParams(t, surfaceParams, mTmpFloat9);
            }
            t.apply();
            t.close();
        }

        @java.lang.Override
        public void releaseSurfaceControlFromRt(android.view.SurfaceControl sc) {
            if (android.view.InsetsController.DEBUG)
                android.util.Log.d(android.view.InsetsAnimationThreadControlRunner.TAG, "releaseSurfaceControlFromRt");

            // Since we don't push the SurfaceParams to the RT we can release directly
            sc.release();
        }

        @java.lang.Override
        public void reportPerceptible(int types, boolean perceptible) {
            mMainThreadHandler.post(() -> mOuterCallbacks.reportPerceptible(types, perceptible));
        }
    };

    @android.annotation.UiThread
    public InsetsAnimationThreadControlRunner(android.util.SparseArray<android.view.InsetsSourceControl> controls, android.graphics.Rect frame, android.view.InsetsState state, android.view.WindowInsetsAnimationControlListener listener, @android.view.WindowInsets.Type.InsetsType
    int types, android.view.InsetsAnimationControlCallbacks controller, long durationMs, android.view.animation.Interpolator interpolator, @android.view.InsetsController.AnimationType
    int animationType, android.os.Handler mainThreadHandler) {
        mMainThreadHandler = mainThreadHandler;
        mOuterCallbacks = controller;
        mControl = new android.view.InsetsAnimationControlImpl(controls, frame, state, listener, types, mCallbacks, durationMs, interpolator, animationType);
        android.view.InsetsAnimationThread.getHandler().post(() -> {
            android.os.Trace.asyncTraceBegin(Trace.TRACE_TAG_VIEW, "InsetsAsyncAnimation: " + WindowInsets.Type.toString(types), types);
            listener.onReady(mControl, types);
        });
    }

    private void releaseControls(android.util.SparseArray<android.view.InsetsSourceControl> controls) {
        for (int i = controls.size() - 1; i >= 0; i--) {
            controls.valueAt(i).release(android.view.SurfaceControl::release);
        }
    }

    @java.lang.Override
    @android.annotation.UiThread
    public int getTypes() {
        return mControl.getTypes();
    }

    @java.lang.Override
    @android.annotation.UiThread
    public void cancel() {
        android.view.InsetsAnimationThread.getHandler().post(mControl::cancel);
    }

    @java.lang.Override
    @android.annotation.UiThread
    public android.view.WindowInsetsAnimation getAnimation() {
        return mControl.getAnimation();
    }

    @java.lang.Override
    public int getAnimationType() {
        return mControl.getAnimationType();
    }
}

