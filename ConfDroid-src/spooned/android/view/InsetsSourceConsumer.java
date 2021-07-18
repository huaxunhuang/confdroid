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
 * limitations under the License
 */
package android.view;


/**
 * Controls the visibility and animations of a single window insets source.
 *
 * @unknown 
 */
public class InsetsSourceConsumer {
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.InsetsSourceConsumer.ShowResult.SHOW_IMMEDIATELY, android.view.InsetsSourceConsumer.ShowResult.SHOW_DELAYED, android.view.InsetsSourceConsumer.ShowResult.SHOW_FAILED })
    @interface ShowResult {
        /**
         * Window type is ready to be shown, will be shown immidiately.
         */
        int SHOW_IMMEDIATELY = 0;

        /**
         * Result will be delayed. Window needs to be prepared or request is not from controller.
         * Request will be delegated to controller and may or may not be shown.
         */
        int SHOW_DELAYED = 1;

        /**
         * Window will not be shown because one of the conditions couldn't be met.
         * (e.g. in IME's case, when no editor is focused.)
         */
        int SHOW_FAILED = 2;
    }

    protected final android.view.InsetsController mController;

    protected boolean mVisible;

    private final java.util.function.Supplier<android.view.SurfaceControl.Transaction> mTransactionSupplier;

    @android.view.InsetsState.InternalInsetType
    private final int mType;

    private final android.view.InsetsState mState;

    @android.annotation.Nullable
    private android.view.InsetsSourceControl mSourceControl;

    public InsetsSourceConsumer(@android.view.InsetsState.InternalInsetType
    int type, android.view.InsetsState state, java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionSupplier, android.view.InsetsController controller) {
        mType = type;
        mState = state;
        mTransactionSupplier = transactionSupplier;
        mController = controller;
        mVisible = android.view.InsetsState.getDefaultVisibility(type);
    }

    public void setControl(@android.annotation.Nullable
    android.view.InsetsSourceControl control) {
        if (mSourceControl == control) {
            return;
        }
        mSourceControl = control;
        applyHiddenToControl();
        if (applyLocalVisibilityOverride()) {
            mController.notifyVisibilityChanged();
        }
        if (mSourceControl == null) {
            mController.notifyControlRevoked(this);
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    public android.view.InsetsSourceControl getControl() {
        return mSourceControl;
    }

    int getType() {
        return mType;
    }

    @com.android.internal.annotations.VisibleForTesting
    public void show() {
        setVisible(true);
    }

    @com.android.internal.annotations.VisibleForTesting
    public void hide() {
        setVisible(false);
    }

    /**
     * Called when current window gains focus
     */
    public void onWindowFocusGained() {
    }

    /**
     * Called when current window loses focus.
     */
    public void onWindowFocusLost() {
    }

    boolean applyLocalVisibilityOverride() {
        // If we don't have control, we are not able to change the visibility.
        if (mSourceControl == null) {
            return false;
        }
        if (mState.getSource(mType).isVisible() == mVisible) {
            return false;
        }
        mState.getSource(mType).setVisible(mVisible);
        return true;
    }

    @com.android.internal.annotations.VisibleForTesting
    public boolean isVisible() {
        return mVisible;
    }

    /**
     * Request to show current window type.
     *
     * @param fromController
     * 		{@code true} if request is coming from controller.
     * 		(e.g. in IME case, controller is
     * 		{@link android.inputmethodservice.InputMethodService}).
     * @return @see {@link ShowResult}.
     */
    @android.view.InsetsSourceConsumer.ShowResult
    int requestShow(boolean fromController) {
        return android.view.InsetsSourceConsumer.ShowResult.SHOW_IMMEDIATELY;
    }

    /**
     * Notify listeners that window is now hidden.
     */
    void notifyHidden() {
        // no-op for types that always return ShowResult#SHOW_IMMEDIATELY.
    }

    private void setVisible(boolean visible) {
        if (mVisible == visible) {
            return;
        }
        mVisible = visible;
        applyHiddenToControl();
        applyLocalVisibilityOverride();
        mController.notifyVisibilityChanged();
    }

    private void applyHiddenToControl() {
        if (mSourceControl == null) {
            return;
        }
        final android.view.SurfaceControl.Transaction t = mTransactionSupplier.get();
        if (mVisible) {
            t.show(mSourceControl.getLeash());
        } else {
            t.hide(mSourceControl.getLeash());
        }
        t.apply();
    }
}

