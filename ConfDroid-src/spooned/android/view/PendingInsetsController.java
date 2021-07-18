/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * An insets controller that keeps track of pending requests. This is such that an app can freely
 * use {@link WindowInsetsController} before the view root is attached during activity startup.
 *
 * @unknown 
 */
public class PendingInsetsController implements android.view.WindowInsetsController {
    private static final int KEEP_BEHAVIOR = -1;

    private final java.util.ArrayList<android.view.PendingInsetsController.PendingRequest> mRequests = new java.util.ArrayList<>();

    @android.view.WindowInsetsController.Appearance
    private int mAppearance;

    @android.view.WindowInsetsController.Appearance
    private int mAppearanceMask;

    @android.view.WindowInsetsController.Behavior
    private int mBehavior = android.view.PendingInsetsController.KEEP_BEHAVIOR;

    private boolean mAnimationsDisabled;

    private final android.view.InsetsState mDummyState = new android.view.InsetsState();

    private android.view.InsetsController mReplayedInsetsController;

    private java.util.ArrayList<android.view.WindowInsetsController.OnControllableInsetsChangedListener> mControllableInsetsChangedListeners = new java.util.ArrayList<>();

    private int mCaptionInsetsHeight = 0;

    @java.lang.Override
    public void show(int types) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.show(types);
        } else {
            mRequests.add(new android.view.PendingInsetsController.ShowRequest(types));
        }
    }

    @java.lang.Override
    public void hide(int types) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.hide(types);
        } else {
            mRequests.add(new android.view.PendingInsetsController.HideRequest(types));
        }
    }

    @java.lang.Override
    public void setSystemBarsAppearance(int appearance, int mask) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.setSystemBarsAppearance(appearance, mask);
        } else {
            mAppearance = (mAppearance & (~mask)) | (appearance & mask);
            mAppearanceMask |= mask;
        }
    }

    @java.lang.Override
    public int getSystemBarsAppearance() {
        if (mReplayedInsetsController != null) {
            return mReplayedInsetsController.getSystemBarsAppearance();
        }
        return mAppearance;
    }

    @java.lang.Override
    public void setCaptionInsetsHeight(int height) {
        mCaptionInsetsHeight = height;
    }

    @java.lang.Override
    public void setSystemBarsBehavior(int behavior) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.setSystemBarsBehavior(behavior);
        } else {
            mBehavior = behavior;
        }
    }

    @java.lang.Override
    public int getSystemBarsBehavior() {
        if (mReplayedInsetsController != null) {
            return mReplayedInsetsController.getSystemBarsBehavior();
        }
        return mBehavior;
    }

    @java.lang.Override
    public void setAnimationsDisabled(boolean disable) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.setAnimationsDisabled(disable);
        } else {
            mAnimationsDisabled = disable;
        }
    }

    @java.lang.Override
    public android.view.InsetsState getState() {
        return mDummyState;
    }

    @java.lang.Override
    public boolean isRequestedVisible(int type) {
        // Method is only used once real insets controller is attached, so no need to traverse
        // requests here.
        return android.view.InsetsState.getDefaultVisibility(type);
    }

    @java.lang.Override
    public void addOnControllableInsetsChangedListener(android.view.WindowInsetsController.OnControllableInsetsChangedListener listener) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.addOnControllableInsetsChangedListener(listener);
        } else {
            mControllableInsetsChangedListeners.add(listener);
            listener.onControllableInsetsChanged(this, 0);
        }
    }

    @java.lang.Override
    public void removeOnControllableInsetsChangedListener(android.view.WindowInsetsController.OnControllableInsetsChangedListener listener) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.removeOnControllableInsetsChangedListener(listener);
        } else {
            mControllableInsetsChangedListeners.remove(listener);
        }
    }

    /**
     * Replays the commands on {@code controller} and attaches it to this instance such that any
     * calls will be forwarded to the real instance in the future.
     */
    @com.android.internal.annotations.VisibleForTesting
    public void replayAndAttach(android.view.InsetsController controller) {
        if (mBehavior != android.view.PendingInsetsController.KEEP_BEHAVIOR) {
            controller.setSystemBarsBehavior(mBehavior);
        }
        if (mAppearanceMask != 0) {
            controller.setSystemBarsAppearance(mAppearance, mAppearanceMask);
        }
        if (mCaptionInsetsHeight != 0) {
            controller.setCaptionInsetsHeight(mCaptionInsetsHeight);
        }
        if (mAnimationsDisabled) {
            controller.setAnimationsDisabled(true);
        }
        int size = mRequests.size();
        for (int i = 0; i < size; i++) {
            mRequests.get(i).replay(controller);
        }
        size = mControllableInsetsChangedListeners.size();
        for (int i = 0; i < size; i++) {
            controller.addOnControllableInsetsChangedListener(mControllableInsetsChangedListeners.get(i));
        }
        // Reset all state so it doesn't get applied twice just in case
        mRequests.clear();
        mControllableInsetsChangedListeners.clear();
        mBehavior = android.view.PendingInsetsController.KEEP_BEHAVIOR;
        mAppearance = 0;
        mAppearanceMask = 0;
        mAnimationsDisabled = false;
        // After replaying, we forward everything directly to the replayed instance.
        mReplayedInsetsController = controller;
    }

    /**
     * Detaches the controller to no longer forward calls to the real instance.
     */
    @com.android.internal.annotations.VisibleForTesting
    public void detach() {
        mReplayedInsetsController = null;
    }

    @java.lang.Override
    public void controlWindowInsetsAnimation(@android.view.WindowInsets.Type.InsetsType
    int types, long durationMillis, @android.annotation.Nullable
    android.view.animation.Interpolator interpolator, android.os.CancellationSignal cancellationSignal, @android.annotation.NonNull
    android.view.WindowInsetsAnimationControlListener listener) {
        if (mReplayedInsetsController != null) {
            mReplayedInsetsController.controlWindowInsetsAnimation(types, durationMillis, interpolator, cancellationSignal, listener);
        } else {
            listener.onCancelled(null);
        }
    }

    private interface PendingRequest {
        void replay(android.view.InsetsController controller);
    }

    private static class ShowRequest implements android.view.PendingInsetsController.PendingRequest {
        @android.view.WindowInsets.Type.InsetsType
        private final int mTypes;

        public ShowRequest(int types) {
            mTypes = types;
        }

        @java.lang.Override
        public void replay(android.view.InsetsController controller) {
            controller.show(mTypes);
        }
    }

    private static class HideRequest implements android.view.PendingInsetsController.PendingRequest {
        @android.view.WindowInsets.Type.InsetsType
        private final int mTypes;

        public HideRequest(int types) {
            mTypes = types;
        }

        @java.lang.Override
        public void replay(android.view.InsetsController controller) {
            controller.hide(mTypes);
        }
    }
}

