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
 * Implements {@link InsetsController.Host} for {@link ViewRootImpl}s.
 *
 * @unknown 
 */
public class ViewRootInsetsControllerHost implements android.view.InsetsController.Host {
    private final java.lang.String TAG = "VRInsetsControllerHost";

    private final android.view.ViewRootImpl mViewRoot;

    private android.view.SyncRtSurfaceTransactionApplier mApplier;

    public ViewRootInsetsControllerHost(android.view.ViewRootImpl viewRoot) {
        mViewRoot = viewRoot;
    }

    @java.lang.Override
    public android.os.Handler getHandler() {
        return mViewRoot.mHandler;
    }

    @java.lang.Override
    public void notifyInsetsChanged() {
        mViewRoot.notifyInsetsChanged();
    }

    @java.lang.Override
    public void addOnPreDrawRunnable(java.lang.Runnable r) {
        if (mViewRoot.mView == null) {
            return;
        }
        mViewRoot.mView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
            @java.lang.Override
            public boolean onPreDraw() {
                mViewRoot.mView.getViewTreeObserver().removeOnPreDrawListener(this);
                r.run();
                return true;
            }
        });
        mViewRoot.mView.invalidate();
    }

    @java.lang.Override
    public void dispatchWindowInsetsAnimationPrepare(@android.annotation.NonNull
    android.view.WindowInsetsAnimation animation) {
        if (mViewRoot.mView == null) {
            return;
        }
        mViewRoot.mView.dispatchWindowInsetsAnimationPrepare(animation);
    }

    @java.lang.Override
    public android.view.WindowInsetsAnimation.Bounds dispatchWindowInsetsAnimationStart(@android.annotation.NonNull
    android.view.WindowInsetsAnimation animation, @android.annotation.NonNull
    android.view.WindowInsetsAnimation.Bounds bounds) {
        if (mViewRoot.mView == null) {
            return null;
        }
        if (android.view.InsetsController.DEBUG)
            android.util.Log.d(TAG, "windowInsetsAnimation started");

        return mViewRoot.mView.dispatchWindowInsetsAnimationStart(animation, bounds);
    }

    @java.lang.Override
    public android.view.WindowInsets dispatchWindowInsetsAnimationProgress(@android.annotation.NonNull
    android.view.WindowInsets insets, @android.annotation.NonNull
    java.util.List<android.view.WindowInsetsAnimation> runningAnimations) {
        if (mViewRoot.mView == null) {
            // The view has already detached from window.
            return null;
        }
        if (android.view.InsetsController.DEBUG) {
            for (android.view.WindowInsetsAnimation anim : runningAnimations) {
                android.util.Log.d(TAG, "windowInsetsAnimation progress: " + anim.getInterpolatedFraction());
            }
        }
        return mViewRoot.mView.dispatchWindowInsetsAnimationProgress(insets, runningAnimations);
    }

    @java.lang.Override
    public void dispatchWindowInsetsAnimationEnd(@android.annotation.NonNull
    android.view.WindowInsetsAnimation animation) {
        if (android.view.InsetsController.DEBUG)
            android.util.Log.d(TAG, "windowInsetsAnimation ended");

        mViewRoot.mView.dispatchWindowInsetsAnimationEnd(animation);
    }

    @java.lang.Override
    public void applySurfaceParams(android.view.SyncRtSurfaceTransactionApplier.SurfaceParams... params) {
        if (mViewRoot.mView == null) {
            throw new java.lang.IllegalStateException("View of the ViewRootImpl is not initiated.");
        }
        if (mApplier == null) {
            mApplier = new android.view.SyncRtSurfaceTransactionApplier(mViewRoot.mView);
        }
        if (mViewRoot.mView.isHardwareAccelerated()) {
            mApplier.scheduleApply(params);
        } else {
            // Window doesn't support hardware acceleration, no synchronization for now.
            // TODO(b/149342281): use mViewRoot.mSurface.getNextFrameNumber() to sync on every
            // frame instead.
            /* frame */
            mApplier.applyParams(new android.view.SurfaceControl.Transaction(), -1, params);
        }
    }

    @java.lang.Override
    public void postInsetsAnimationCallback(java.lang.Runnable r) {
        /* token */
        mViewRoot.mChoreographer.postCallback(android.view.Choreographer.CALLBACK_INSETS_ANIMATION, r, null);
    }

    @java.lang.Override
    public void updateCompatSysUiVisibility(int type, boolean visible, boolean hasControl) {
        mViewRoot.updateCompatSysUiVisibility(type, visible, hasControl);
    }

    @java.lang.Override
    public void onInsetsModified(android.view.InsetsState insetsState) {
        try {
            mViewRoot.mWindowSession.insetsModified(mViewRoot.mWindow, insetsState);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to call insetsModified", e);
        }
    }

    @java.lang.Override
    public boolean hasAnimationCallbacks() {
        if (mViewRoot.mView == null) {
            return false;
        }
        return mViewRoot.mView.hasWindowInsetsAnimationCallback();
    }

    @java.lang.Override
    public void setSystemBarsAppearance(int appearance, int mask) {
        mViewRoot.mWindowAttributes.privateFlags |= android.view.WindowManager.LayoutParams.PRIVATE_FLAG_APPEARANCE_CONTROLLED;
        final android.view.InsetsFlags insetsFlags = mViewRoot.mWindowAttributes.insetsFlags;
        if (insetsFlags.appearance != appearance) {
            insetsFlags.appearance = (insetsFlags.appearance & (~mask)) | (appearance & mask);
            mViewRoot.mWindowAttributesChanged = true;
            mViewRoot.scheduleTraversals();
        }
    }

    @java.lang.Override
    public int getSystemBarsAppearance() {
        if ((mViewRoot.mWindowAttributes.privateFlags & android.view.WindowManager.LayoutParams.PRIVATE_FLAG_APPEARANCE_CONTROLLED) == 0) {
            // We only return the requested appearance, not the implied one.
            return 0;
        }
        return mViewRoot.mWindowAttributes.insetsFlags.appearance;
    }

    @java.lang.Override
    public void setSystemBarsBehavior(int behavior) {
        mViewRoot.mWindowAttributes.privateFlags |= android.view.WindowManager.LayoutParams.PRIVATE_FLAG_BEHAVIOR_CONTROLLED;
        if (mViewRoot.mWindowAttributes.insetsFlags.behavior != behavior) {
            mViewRoot.mWindowAttributes.insetsFlags.behavior = behavior;
            mViewRoot.mWindowAttributesChanged = true;
            mViewRoot.scheduleTraversals();
        }
    }

    @java.lang.Override
    public int getSystemBarsBehavior() {
        if ((mViewRoot.mWindowAttributes.privateFlags & android.view.WindowManager.LayoutParams.PRIVATE_FLAG_BEHAVIOR_CONTROLLED) == 0) {
            // We only return the requested behavior, not the implied one.
            return 0;
        }
        return mViewRoot.mWindowAttributes.insetsFlags.behavior;
    }

    @java.lang.Override
    public void releaseSurfaceControlFromRt(android.view.SurfaceControl surfaceControl) {
        // At the time we receive new leashes (e.g. InsetsSourceConsumer is processing
        // setControl) we need to release the old leash. But we may have already scheduled
        // a SyncRtSurfaceTransaction applier to use it from the RenderThread. To avoid
        // synchronization issues we also release from the RenderThread so this release
        // happens after any existing items on the work queue.
        if ((mViewRoot.mView != null) && mViewRoot.mView.isHardwareAccelerated()) {
            mViewRoot.registerRtFrameCallback(( frame) -> {
                surfaceControl.release();
            });
            // Make sure a frame gets scheduled.
            mViewRoot.mView.invalidate();
        } else {
            surfaceControl.release();
        }
    }

    @java.lang.Override
    public android.view.inputmethod.InputMethodManager getInputMethodManager() {
        return mViewRoot.mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
    }

    @java.lang.Override
    public java.lang.String getRootViewTitle() {
        if (mViewRoot == null) {
            return null;
        }
        return mViewRoot.getTitle().toString();
    }

    @java.lang.Override
    public int dipToPx(int dips) {
        if (mViewRoot != null) {
            return mViewRoot.dipToPx(dips);
        }
        return 0;
    }

    @java.lang.Override
    public android.os.IBinder getWindowToken() {
        if (mViewRoot == null) {
            return null;
        }
        final android.view.View view = mViewRoot.getView();
        if (view == null) {
            return null;
        }
        return view.getWindowToken();
    }
}

