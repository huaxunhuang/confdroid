/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * Helper class to apply surface transactions in sync with RenderThread.
 *
 * @unknown 
 */
public class SyncRtSurfaceTransactionApplier {
    private final android.view.Surface mTargetSurface;

    private final android.view.ViewRootImpl mTargetViewRootImpl;

    private final float[] mTmpFloat9 = new float[9];

    /**
     *
     *
     * @param targetView
     * 		The view in the surface that acts as synchronization anchor.
     */
    public SyncRtSurfaceTransactionApplier(android.view.View targetView) {
        mTargetViewRootImpl = (targetView != null) ? targetView.getViewRootImpl() : null;
        mTargetSurface = (mTargetViewRootImpl != null) ? mTargetViewRootImpl.mSurface : null;
    }

    /**
     * Schedules applying surface parameters on the next frame.
     *
     * @param params
     * 		The surface parameters to apply. DO NOT MODIFY the list after passing into
     * 		this method to avoid synchronization issues.
     */
    public void scheduleApply(final android.view.SyncRtSurfaceTransactionApplier.SurfaceParams... params) {
        if (mTargetViewRootImpl == null) {
            return;
        }
        mTargetViewRootImpl.registerRtFrameCallback(( frame) -> {
            if ((mTargetSurface == null) || (!mTargetSurface.isValid())) {
                return;
            }
            android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
            for (int i = params.length - 1; i >= 0; i--) {
                android.view.SyncRtSurfaceTransactionApplier.SurfaceParams surfaceParams = params[i];
                android.view.SurfaceControl surface = surfaceParams.surface;
                t.deferTransactionUntilSurface(surface, mTargetSurface, frame);
                android.view.SyncRtSurfaceTransactionApplier.applyParams(t, surfaceParams, mTmpFloat9);
            }
            t.setEarlyWakeup();
            t.apply();
        });
        // Make sure a frame gets scheduled.
        mTargetViewRootImpl.getView().invalidate();
    }

    public static void applyParams(android.view.SurfaceControl.Transaction t, android.view.SyncRtSurfaceTransactionApplier.SurfaceParams params, float[] tmpFloat9) {
        t.setMatrix(params.surface, params.matrix, tmpFloat9);
        t.setWindowCrop(params.surface, params.windowCrop);
        t.setAlpha(params.surface, params.alpha);
        t.setLayer(params.surface, params.layer);
        t.setCornerRadius(params.surface, params.cornerRadius);
        if (params.visible) {
            t.show(params.surface);
        } else {
            t.hide(params.surface);
        }
    }

    /**
     * Creates an instance of SyncRtSurfaceTransactionApplier, deferring until the target view is
     * attached if necessary.
     */
    public static void create(final android.view.View targetView, final java.util.function.Consumer<android.view.SyncRtSurfaceTransactionApplier> callback) {
        if (targetView == null) {
            // No target view, no applier
            callback.accept(null);
        } else
            if (targetView.getViewRootImpl() != null) {
                // Already attached, we're good to go
                callback.accept(new android.view.SyncRtSurfaceTransactionApplier(targetView));
            } else {
                // Haven't been attached before we can get the view root
                targetView.addOnAttachStateChangeListener(new android.view.View.OnAttachStateChangeListener() {
                    @java.lang.Override
                    public void onViewAttachedToWindow(android.view.View v) {
                        targetView.removeOnAttachStateChangeListener(this);
                        callback.accept(new android.view.SyncRtSurfaceTransactionApplier(targetView));
                    }

                    @java.lang.Override
                    public void onViewDetachedFromWindow(android.view.View v) {
                        // Do nothing
                    }
                });
            }

    }

    public static class SurfaceParams {
        /**
         * Constructs surface parameters to be applied when the current view state gets pushed to
         * RenderThread.
         *
         * @param surface
         * 		The surface to modify.
         * @param alpha
         * 		Alpha to apply.
         * @param matrix
         * 		Matrix to apply.
         * @param windowCrop
         * 		Crop to apply.
         */
        public SurfaceParams(android.view.SurfaceControl surface, float alpha, android.graphics.Matrix matrix, android.graphics.Rect windowCrop, int layer, float cornerRadius, boolean visible) {
            this.surface = surface;
            this.alpha = alpha;
            this.matrix = new android.graphics.Matrix(matrix);
            this.windowCrop = new android.graphics.Rect(windowCrop);
            this.layer = layer;
            this.cornerRadius = cornerRadius;
            this.visible = visible;
        }

        @com.android.internal.annotations.VisibleForTesting
        public final android.view.SurfaceControl surface;

        @com.android.internal.annotations.VisibleForTesting
        public final float alpha;

        @com.android.internal.annotations.VisibleForTesting
        final float cornerRadius;

        @com.android.internal.annotations.VisibleForTesting
        public final android.graphics.Matrix matrix;

        @com.android.internal.annotations.VisibleForTesting
        public final android.graphics.Rect windowCrop;

        @com.android.internal.annotations.VisibleForTesting
        public final int layer;

        public final boolean visible;
    }
}

