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
 * Implements {@link WindowInsetsController} on the client.
 *
 * @unknown 
 */
public class InsetsController implements android.view.WindowInsetsController {
    private static final int ANIMATION_DURATION_SHOW_MS = 275;

    private static final int ANIMATION_DURATION_HIDE_MS = 340;

    private static final android.view.animation.Interpolator INTERPOLATOR = new android.view.animation.PathInterpolator(0.4F, 0.0F, 0.2F, 1.0F);

    private static final int DIRECTION_NONE = 0;

    private static final int DIRECTION_SHOW = 1;

    private static final int DIRECTION_HIDE = 2;

    @android.annotation.IntDef({ android.view.InsetsController.DIRECTION_NONE, android.view.InsetsController.DIRECTION_SHOW, android.view.InsetsController.DIRECTION_HIDE })
    private @interface AnimationDirection {}

    /**
     * Translation animation evaluator.
     */
    private static android.animation.TypeEvaluator<android.graphics.Insets> sEvaluator = ( fraction, startValue, endValue) -> android.graphics.Insets.of(0, ((int) (startValue.top + (fraction * (endValue.top - startValue.top)))), 0, ((int) (startValue.bottom + (fraction * (endValue.bottom - startValue.bottom)))));

    /**
     * Linear animation property
     */
    private static class InsetsProperty extends android.util.Property<android.view.WindowInsetsAnimationController, android.graphics.Insets> {
        InsetsProperty() {
            super(android.graphics.Insets.class, "Insets");
        }

        @java.lang.Override
        public android.graphics.Insets get(android.view.WindowInsetsAnimationController object) {
            return object.getCurrentInsets();
        }

        @java.lang.Override
        public void set(android.view.WindowInsetsAnimationController object, android.graphics.Insets value) {
            object.changeInsets(value);
        }
    }

    private final java.lang.String TAG = "InsetsControllerImpl";

    private final android.view.InsetsState mState = new android.view.InsetsState();

    private final android.view.InsetsState mTmpState = new android.view.InsetsState();

    private final android.graphics.Rect mFrame = new android.graphics.Rect();

    private final android.util.SparseArray<android.view.InsetsSourceConsumer> mSourceConsumers = new android.util.SparseArray();

    private final android.view.ViewRootImpl mViewRoot;

    private final android.util.SparseArray<android.view.InsetsSourceControl> mTmpControlArray = new android.util.SparseArray();

    private final java.util.ArrayList<android.view.InsetsAnimationControlImpl> mAnimationControls = new java.util.ArrayList<>();

    private final java.util.ArrayList<android.view.InsetsAnimationControlImpl> mTmpFinishedControls = new java.util.ArrayList<>();

    private android.view.WindowInsets mLastInsets;

    private boolean mAnimCallbackScheduled;

    private final java.lang.Runnable mAnimCallback;

    private final android.graphics.Rect mLastLegacyContentInsets = new android.graphics.Rect();

    private final android.graphics.Rect mLastLegacyStableInsets = new android.graphics.Rect();

    @android.view.InsetsController.AnimationDirection
    private int mAnimationDirection;

    private int mPendingTypesToShow;

    private int mLastLegacySoftInputMode;

    public InsetsController(android.view.ViewRootImpl viewRoot) {
        mViewRoot = viewRoot;
        mAnimCallback = () -> {
            mAnimCallbackScheduled = false;
            if (mAnimationControls.isEmpty()) {
                return;
            }
            mTmpFinishedControls.clear();
            android.view.InsetsState state = /* copySources */
            new android.view.InsetsState(mState, true);
            for (int i = mAnimationControls.size() - 1; i >= 0; i--) {
                android.view.InsetsAnimationControlImpl control = mAnimationControls.get(i);
                if (mAnimationControls.get(i).applyChangeInsets(state)) {
                    mTmpFinishedControls.add(control);
                }
            }
            android.view.WindowInsets insets = /* typeSideMap */
            state.calculateInsets(mFrame, mLastInsets.isRound(), mLastInsets.shouldAlwaysConsumeSystemBars(), mLastInsets.getDisplayCutout(), mLastLegacyContentInsets, mLastLegacyStableInsets, mLastLegacySoftInputMode, null);
            mViewRoot.mView.dispatchWindowInsetsAnimationProgress(insets);
            for (int i = mTmpFinishedControls.size() - 1; i >= 0; i--) {
                dispatchAnimationFinished(mTmpFinishedControls.get(i).getAnimation());
            }
        };
    }

    @com.android.internal.annotations.VisibleForTesting
    public void onFrameChanged(android.graphics.Rect frame) {
        if (mFrame.equals(frame)) {
            return;
        }
        mViewRoot.notifyInsetsChanged();
        mFrame.set(frame);
    }

    public android.view.InsetsState getState() {
        return mState;
    }

    boolean onStateChanged(android.view.InsetsState state) {
        if (mState.equals(state)) {
            return false;
        }
        mState.set(state);
        /* copySources */
        mTmpState.set(state, true);
        applyLocalVisibilityOverride();
        mViewRoot.notifyInsetsChanged();
        if (!mState.equals(mTmpState)) {
            sendStateToWindowManager();
        }
        return true;
    }

    /**
     *
     *
     * @see InsetsState#calculateInsets
     */
    @com.android.internal.annotations.VisibleForTesting
    public android.view.WindowInsets calculateInsets(boolean isScreenRound, boolean alwaysConsumeSystemBars, android.view.DisplayCutout cutout, android.graphics.Rect legacyContentInsets, android.graphics.Rect legacyStableInsets, int legacySoftInputMode) {
        mLastLegacyContentInsets.set(legacyContentInsets);
        mLastLegacyStableInsets.set(legacyStableInsets);
        mLastLegacySoftInputMode = legacySoftInputMode;
        mLastInsets = /* typeSideMap */
        mState.calculateInsets(mFrame, isScreenRound, alwaysConsumeSystemBars, cutout, legacyContentInsets, legacyStableInsets, legacySoftInputMode, null);
        return mLastInsets;
    }

    /**
     * Called when the server has dispatched us a new set of inset controls.
     */
    public void onControlsChanged(android.view.InsetsSourceControl[] activeControls) {
        if (activeControls != null) {
            for (android.view.InsetsSourceControl activeControl : activeControls) {
                if (activeControl != null) {
                    // TODO(b/122982984): Figure out why it can be null.
                    mTmpControlArray.put(activeControl.getType(), activeControl);
                }
            }
        }
        // Ensure to update all existing source consumers
        for (int i = mSourceConsumers.size() - 1; i >= 0; i--) {
            final android.view.InsetsSourceConsumer consumer = mSourceConsumers.valueAt(i);
            final android.view.InsetsSourceControl control = mTmpControlArray.get(consumer.getType());
            // control may be null, but we still need to update the control to null if it got
            // revoked.
            consumer.setControl(control);
        }
        // Ensure to create source consumers if not available yet.
        for (int i = mTmpControlArray.size() - 1; i >= 0; i--) {
            final android.view.InsetsSourceControl control = mTmpControlArray.valueAt(i);
            getSourceConsumer(control.getType()).setControl(control);
        }
        mTmpControlArray.clear();
    }

    @java.lang.Override
    public void show(@android.view.WindowInsets.Type.InsetType
    int types) {
        /* fromIme */
        show(types, false);
    }

    private void show(@android.view.WindowInsets.Type.InsetType
    int types, boolean fromIme) {
        // TODO: Support a ResultReceiver for IME.
        // TODO(b/123718661): Make show() work for multi-session IME.
        int typesReady = 0;
        final android.util.ArraySet<java.lang.Integer> internalTypes = android.view.InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            android.view.InsetsSourceConsumer consumer = getSourceConsumer(internalTypes.valueAt(i));
            if (mAnimationDirection == android.view.InsetsController.DIRECTION_HIDE) {
                // Only one animator (with multiple InsetType) can run at a time.
                // previous one should be cancelled for simplicity.
                cancelExistingAnimation();
            } else
                if (consumer.isVisible() && ((mAnimationDirection == android.view.InsetsController.DIRECTION_NONE) || (mAnimationDirection == android.view.InsetsController.DIRECTION_HIDE))) {
                    // no-op: already shown or animating in (because window visibility is
                    // applied before starting animation).
                    // TODO: When we have more than one types: handle specific case when
                    // show animation is going on, but the current type is not becoming visible.
                    continue;
                }

            typesReady |= android.view.InsetsState.toPublicType(consumer.getType());
        }
        /* show */
        applyAnimation(typesReady, true, fromIme);
    }

    @java.lang.Override
    public void hide(@android.view.WindowInsets.Type.InsetType
    int types) {
        int typesReady = 0;
        final android.util.ArraySet<java.lang.Integer> internalTypes = android.view.InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            android.view.InsetsSourceConsumer consumer = getSourceConsumer(internalTypes.valueAt(i));
            if (mAnimationDirection == android.view.InsetsController.DIRECTION_SHOW) {
                cancelExistingAnimation();
            } else
                if ((!consumer.isVisible()) && ((mAnimationDirection == android.view.InsetsController.DIRECTION_NONE) || (mAnimationDirection == android.view.InsetsController.DIRECTION_HIDE))) {
                    // no-op: already hidden or animating out.
                    continue;
                }

            typesReady |= android.view.InsetsState.toPublicType(consumer.getType());
        }
        /* show */
        /* fromIme */
        applyAnimation(typesReady, false, false);
    }

    @java.lang.Override
    public void controlWindowInsetsAnimation(@android.view.WindowInsets.Type.InsetType
    int types, android.view.WindowInsetsAnimationControlListener listener) {
        /* fromIme */
        controlWindowInsetsAnimation(types, listener, false);
    }

    private void controlWindowInsetsAnimation(@android.view.WindowInsets.Type.InsetType
    int types, android.view.WindowInsetsAnimationControlListener listener, boolean fromIme) {
        // If the frame of our window doesn't span the entire display, the control API makes very
        // little sense, as we don't deal with negative insets. So just cancel immediately.
        if (!mState.getDisplayFrame().equals(mFrame)) {
            listener.onCancelled();
            return;
        }
        controlAnimationUnchecked(types, listener, mFrame, fromIme);
    }

    private void controlAnimationUnchecked(@android.view.WindowInsets.Type.InsetType
    int types, android.view.WindowInsetsAnimationControlListener listener, android.graphics.Rect frame, boolean fromIme) {
        if (types == 0) {
            // nothing to animate.
            return;
        }
        cancelExistingControllers(types);
        final android.util.ArraySet<java.lang.Integer> internalTypes = mState.toInternalType(types);
        final android.util.SparseArray<android.view.InsetsSourceConsumer> consumers = new android.util.SparseArray();
        android.util.Pair<java.lang.Integer, java.lang.Boolean> typesReadyPair = collectConsumers(fromIme, internalTypes, consumers);
        int typesReady = typesReadyPair.first;
        boolean isReady = typesReadyPair.second;
        if (!isReady) {
            // IME isn't ready, all requested types would be shown once IME is ready.
            mPendingTypesToShow = typesReady;
            // TODO: listener for pending types.
            return;
        }
        // pending types from previous request.
        typesReady = collectPendingConsumers(typesReady, consumers);
        if (typesReady == 0) {
            listener.onCancelled();
            return;
        }
        final android.view.InsetsAnimationControlImpl controller = new android.view.InsetsAnimationControlImpl(consumers, frame, mState, listener, typesReady, () -> new android.view.SyncRtSurfaceTransactionApplier(mViewRoot.mView), this);
        mAnimationControls.add(controller);
    }

    /**
     *
     *
     * @return Pair of (types ready to animate, is ready to animate).
     */
    private android.util.Pair<java.lang.Integer, java.lang.Boolean> collectConsumers(boolean fromIme, android.util.ArraySet<java.lang.Integer> internalTypes, android.util.SparseArray<android.view.InsetsSourceConsumer> consumers) {
        int typesReady = 0;
        boolean isReady = true;
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            android.view.InsetsSourceConsumer consumer = getSourceConsumer(internalTypes.valueAt(i));
            if (consumer.getControl() != null) {
                if (!consumer.isVisible()) {
                    // Show request
                    switch (consumer.requestShow(fromIme)) {
                        case android.view.InsetsSourceConsumer.ShowResult.SHOW_IMMEDIATELY :
                            typesReady |= android.view.InsetsState.toPublicType(consumer.getType());
                            break;
                        case android.view.InsetsSourceConsumer.ShowResult.SHOW_DELAYED :
                            isReady = false;
                            break;
                        case android.view.InsetsSourceConsumer.ShowResult.SHOW_FAILED :
                            // IME cannot be shown (since it didn't have focus), proceed
                            // with animation of other types.
                            if (mPendingTypesToShow != 0) {
                                // remove IME from pending because view no longer has focus.
                                mPendingTypesToShow &= ~android.view.InsetsState.toPublicType(android.view.InsetsState.TYPE_IME);
                            }
                            break;
                    }
                } else {
                    // Hide request
                    // TODO: Move notifyHidden() to beginning of the hide animation
                    // (when visibility actually changes using hideDirectly()).
                    consumer.notifyHidden();
                    typesReady |= android.view.InsetsState.toPublicType(consumer.getType());
                }
                consumers.put(consumer.getType(), consumer);
            } else {
                // TODO: Let calling app know it's not possible, or wait
                // TODO: Remove it from types
            }
        }
        return new android.util.Pair(typesReady, isReady);
    }

    private int collectPendingConsumers(@android.view.WindowInsets.Type.InsetType
    int typesReady, android.util.SparseArray<android.view.InsetsSourceConsumer> consumers) {
        if (mPendingTypesToShow != 0) {
            typesReady |= mPendingTypesToShow;
            final android.util.ArraySet<java.lang.Integer> internalTypes = mState.toInternalType(mPendingTypesToShow);
            for (int i = internalTypes.size() - 1; i >= 0; i--) {
                android.view.InsetsSourceConsumer consumer = getSourceConsumer(internalTypes.valueAt(i));
                consumers.put(consumer.getType(), consumer);
            }
            mPendingTypesToShow = 0;
        }
        return typesReady;
    }

    private void cancelExistingControllers(@android.view.WindowInsets.Type.InsetType
    int types) {
        for (int i = mAnimationControls.size() - 1; i >= 0; i--) {
            android.view.InsetsAnimationControlImpl control = mAnimationControls.get(i);
            if ((control.getTypes() & types) != 0) {
                cancelAnimation(control);
            }
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    public void notifyFinished(android.view.InsetsAnimationControlImpl controller, int shownTypes) {
        mAnimationControls.remove(controller);
        hideDirectly(controller.getTypes() & (~shownTypes));
        showDirectly(controller.getTypes() & shownTypes);
    }

    void notifyControlRevoked(android.view.InsetsSourceConsumer consumer) {
        for (int i = mAnimationControls.size() - 1; i >= 0; i--) {
            android.view.InsetsAnimationControlImpl control = mAnimationControls.get(i);
            if ((control.getTypes() & android.view.InsetsState.toPublicType(consumer.getType())) != 0) {
                cancelAnimation(control);
            }
        }
    }

    private void cancelAnimation(android.view.InsetsAnimationControlImpl control) {
        control.onCancelled();
        mAnimationControls.remove(control);
    }

    private void applyLocalVisibilityOverride() {
        for (int i = mSourceConsumers.size() - 1; i >= 0; i--) {
            final android.view.InsetsSourceConsumer controller = mSourceConsumers.valueAt(i);
            controller.applyLocalVisibilityOverride();
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.NonNull
    public android.view.InsetsSourceConsumer getSourceConsumer(@android.view.InsetsState.InternalInsetType
    int type) {
        android.view.InsetsSourceConsumer controller = mSourceConsumers.get(type);
        if (controller != null) {
            return controller;
        }
        controller = createConsumerOfType(type);
        mSourceConsumers.put(type, controller);
        return controller;
    }

    @com.android.internal.annotations.VisibleForTesting
    public void notifyVisibilityChanged() {
        mViewRoot.notifyInsetsChanged();
        sendStateToWindowManager();
    }

    /**
     * Called when current window gains focus.
     */
    public void onWindowFocusGained() {
        getSourceConsumer(android.view.InsetsState.TYPE_IME).onWindowFocusGained();
    }

    /**
     * Called when current window loses focus.
     */
    public void onWindowFocusLost() {
        getSourceConsumer(android.view.InsetsState.TYPE_IME).onWindowFocusLost();
    }

    android.view.ViewRootImpl getViewRoot() {
        return mViewRoot;
    }

    /**
     * Used by {@link ImeInsetsSourceConsumer} when IME decides to be shown/hidden.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public void applyImeVisibility(boolean setVisible) {
        if (setVisible) {
            /* fromIme */
            show(android.view.WindowInsets.Type.IME, true);
        } else {
            hide(android.view.WindowInsets.Type.IME);
        }
    }

    private android.view.InsetsSourceConsumer createConsumerOfType(int type) {
        if (type == android.view.InsetsState.TYPE_IME) {
            return new android.view.ImeInsetsSourceConsumer(mState, android.view.SurfaceControl.Transaction::new, this);
        } else {
            return new android.view.InsetsSourceConsumer(type, mState, android.view.SurfaceControl.Transaction::new, this);
        }
    }

    /**
     * Sends the local visibility state back to window manager.
     */
    private void sendStateToWindowManager() {
        android.view.InsetsState tmpState = new android.view.InsetsState();
        for (int i = mSourceConsumers.size() - 1; i >= 0; i--) {
            final android.view.InsetsSourceConsumer consumer = mSourceConsumers.valueAt(i);
            if (consumer.getControl() != null) {
                tmpState.addSource(mState.getSource(consumer.getType()));
            }
        }
        // TODO: Put this on a dispatcher thread.
        try {
            mViewRoot.mWindowSession.insetsModified(mViewRoot.mWindow, tmpState);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to call insetsModified", e);
        }
    }

    private void applyAnimation(@android.view.WindowInsets.Type.InsetType
    final int types, boolean show, boolean fromIme) {
        if (types == 0) {
            // nothing to animate.
            return;
        }
        android.view.WindowInsetsAnimationControlListener listener = new android.view.WindowInsetsAnimationControlListener() {
            private android.view.WindowInsetsAnimationController mController;

            private android.animation.ObjectAnimator mAnimator;

            @java.lang.Override
            public void onReady(android.view.WindowInsetsAnimationController controller, int types) {
                mController = controller;
                if (show) {
                    showDirectly(types);
                } else {
                    hideDirectly(types);
                }
                mAnimator = android.animation.ObjectAnimator.ofObject(controller, new android.view.InsetsController.InsetsProperty(), android.view.InsetsController.sEvaluator, show ? controller.getHiddenStateInsets() : controller.getShownStateInsets(), show ? controller.getShownStateInsets() : controller.getHiddenStateInsets());
                mAnimator.setDuration(show ? android.view.InsetsController.ANIMATION_DURATION_SHOW_MS : android.view.InsetsController.ANIMATION_DURATION_HIDE_MS);
                mAnimator.setInterpolator(android.view.InsetsController.INTERPOLATOR);
                mAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        onAnimationFinish();
                    }
                });
                mAnimator.start();
            }

            @java.lang.Override
            public void onCancelled() {
                mAnimator.cancel();
            }

            private void onAnimationFinish() {
                mAnimationDirection = android.view.InsetsController.DIRECTION_NONE;
                mController.finish(show ? types : 0);
            }
        };
        // Show/hide animations always need to be relative to the display frame, in order that shown
        // and hidden state insets are correct.
        controlAnimationUnchecked(types, listener, mState.getDisplayFrame(), fromIme);
    }

    private void hideDirectly(@android.view.WindowInsets.Type.InsetType
    int types) {
        final android.util.ArraySet<java.lang.Integer> internalTypes = android.view.InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            hide();
        }
    }

    private void showDirectly(@android.view.WindowInsets.Type.InsetType
    int types) {
        final android.util.ArraySet<java.lang.Integer> internalTypes = android.view.InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            show();
        }
    }

    /**
     * Cancel on-going animation to show/hide {@link InsetType}.
     */
    @com.android.internal.annotations.VisibleForTesting
    public void cancelExistingAnimation() {
        cancelExistingControllers(android.view.WindowInsets.Type.all());
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix);
        pw.println("InsetsController:");
        mState.dump(prefix + "  ", pw);
    }

    @com.android.internal.annotations.VisibleForTesting
    public void dispatchAnimationStarted(android.view.WindowInsetsAnimationListener.InsetsAnimation animation) {
        mViewRoot.mView.dispatchWindowInsetsAnimationStarted(animation);
    }

    @com.android.internal.annotations.VisibleForTesting
    public void dispatchAnimationFinished(android.view.WindowInsetsAnimationListener.InsetsAnimation animation) {
        mViewRoot.mView.dispatchWindowInsetsAnimationFinished(animation);
    }

    @com.android.internal.annotations.VisibleForTesting
    public void scheduleApplyChangeInsets() {
        if (!mAnimCallbackScheduled) {
            /* token */
            mViewRoot.mChoreographer.postCallback(android.view.Choreographer.CALLBACK_INSETS_ANIMATION, mAnimCallback, null);
            mAnimCallbackScheduled = true;
        }
    }
}

