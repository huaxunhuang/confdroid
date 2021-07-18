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
 * Implements {@link WindowInsetsAnimationController}
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting
public class InsetsAnimationControlImpl implements android.view.WindowInsetsAnimationController {
    private final android.graphics.Rect mTmpFrame = new android.graphics.Rect();

    private final android.view.WindowInsetsAnimationControlListener mListener;

    private final android.util.SparseArray<android.view.InsetsSourceConsumer> mConsumers;

    private final android.util.SparseIntArray mTypeSideMap = new android.util.SparseIntArray();

    private final android.util.SparseSetArray<android.view.InsetsSourceConsumer> mSideSourceMap = new android.util.SparseSetArray();

    /**
     *
     *
     * @see WindowInsetsAnimationController#getHiddenStateInsets
     */
    private final android.graphics.Insets mHiddenInsets;

    /**
     *
     *
     * @see WindowInsetsAnimationController#getShownStateInsets
     */
    private final android.graphics.Insets mShownInsets;

    private final android.graphics.Matrix mTmpMatrix = new android.graphics.Matrix();

    private final android.view.InsetsState mInitialInsetsState;

    @android.view.WindowInsets.Type.InsetType
    private final int mTypes;

    private final java.util.function.Supplier<android.view.SyncRtSurfaceTransactionApplier> mTransactionApplierSupplier;

    private final android.view.InsetsController mController;

    private final android.view.WindowInsetsAnimationListener.InsetsAnimation mAnimation;

    private final android.graphics.Rect mFrame;

    private android.graphics.Insets mCurrentInsets;

    private android.graphics.Insets mPendingInsets;

    private boolean mFinished;

    private boolean mCancelled;

    private int mFinishedShownTypes;

    @com.android.internal.annotations.VisibleForTesting
    public InsetsAnimationControlImpl(android.util.SparseArray<android.view.InsetsSourceConsumer> consumers, android.graphics.Rect frame, android.view.InsetsState state, android.view.WindowInsetsAnimationControlListener listener, @android.view.WindowInsets.Type.InsetType
    int types, java.util.function.Supplier<android.view.SyncRtSurfaceTransactionApplier> transactionApplierSupplier, android.view.InsetsController controller) {
        mConsumers = consumers;
        mListener = listener;
        mTypes = types;
        mTransactionApplierSupplier = transactionApplierSupplier;
        mController = controller;
        mInitialInsetsState = /* copySources */
        new android.view.InsetsState(state, true);
        mCurrentInsets = /* typeSideMap */
        getInsetsFromState(mInitialInsetsState, frame, null);
        mHiddenInsets = /* shown */
        /* typeSideMap */
        calculateInsets(mInitialInsetsState, frame, consumers, false, null);
        mShownInsets = /* shown */
        calculateInsets(mInitialInsetsState, frame, consumers, true, mTypeSideMap);
        mFrame = new android.graphics.Rect(frame);
        android.view.InsetsAnimationControlImpl.buildTypeSourcesMap(mTypeSideMap, mSideSourceMap, mConsumers);
        // TODO: Check for controllability first and wait for IME if needed.
        listener.onReady(this, types);
        mAnimation = new android.view.WindowInsetsAnimationListener.InsetsAnimation(mTypes, mHiddenInsets, mShownInsets);
        mController.dispatchAnimationStarted(mAnimation);
    }

    @java.lang.Override
    public android.graphics.Insets getHiddenStateInsets() {
        return mHiddenInsets;
    }

    @java.lang.Override
    public android.graphics.Insets getShownStateInsets() {
        return mShownInsets;
    }

    @java.lang.Override
    public android.graphics.Insets getCurrentInsets() {
        return mCurrentInsets;
    }

    @java.lang.Override
    @android.view.WindowInsets.Type.InsetType
    public int getTypes() {
        return mTypes;
    }

    @java.lang.Override
    public void changeInsets(android.graphics.Insets insets) {
        if (mFinished) {
            throw new java.lang.IllegalStateException("Can't change insets on an animation that is finished.");
        }
        if (mCancelled) {
            throw new java.lang.IllegalStateException("Can't change insets on an animation that is cancelled.");
        }
        mPendingInsets = sanitize(insets);
        mController.scheduleApplyChangeInsets();
    }

    /**
     *
     *
     * @return Whether the finish callback of this animation should be invoked.
     */
    @com.android.internal.annotations.VisibleForTesting
    public boolean applyChangeInsets(android.view.InsetsState state) {
        if (mCancelled) {
            return false;
        }
        final android.graphics.Insets offset = android.graphics.Insets.subtract(mShownInsets, mPendingInsets);
        java.util.ArrayList<android.view.SyncRtSurfaceTransactionApplier.SurfaceParams> params = new java.util.ArrayList<>();
        if (offset.left != 0) {
            updateLeashesForSide(android.view.InsetsState.INSET_SIDE_LEFT, offset.left, mPendingInsets.left, params, state);
        }
        if (offset.top != 0) {
            updateLeashesForSide(android.view.InsetsState.INSET_SIDE_TOP, offset.top, mPendingInsets.top, params, state);
        }
        if (offset.right != 0) {
            updateLeashesForSide(android.view.InsetsState.INSET_SIDE_RIGHT, offset.right, mPendingInsets.right, params, state);
        }
        if (offset.bottom != 0) {
            updateLeashesForSide(android.view.InsetsState.INSET_SIDE_BOTTOM, offset.bottom, mPendingInsets.bottom, params, state);
        }
        android.view.SyncRtSurfaceTransactionApplier applier = mTransactionApplierSupplier.get();
        applier.scheduleApply(params.toArray(new android.view.SyncRtSurfaceTransactionApplier.SurfaceParams[params.size()]));
        mCurrentInsets = mPendingInsets;
        if (mFinished) {
            mController.notifyFinished(this, mFinishedShownTypes);
        }
        return mFinished;
    }

    @java.lang.Override
    public void finish(int shownTypes) {
        if (mCancelled) {
            return;
        }
        android.view.InsetsState state = new android.view.InsetsState(mController.getState());
        for (int i = mConsumers.size() - 1; i >= 0; i--) {
            android.view.InsetsSourceConsumer consumer = mConsumers.valueAt(i);
            boolean visible = (shownTypes & android.view.InsetsState.toPublicType(consumer.getType())) != 0;
            state.getSource(consumer.getType()).setVisible(visible);
        }
        android.graphics.Insets insets = /* typeSideMap */
        getInsetsFromState(state, mFrame, null);
        changeInsets(insets);
        mFinished = true;
        mFinishedShownTypes = shownTypes;
    }

    @com.android.internal.annotations.VisibleForTesting
    public void onCancelled() {
        if (mFinished) {
            return;
        }
        mCancelled = true;
        mListener.onCancelled();
    }

    android.view.WindowInsetsAnimationListener.InsetsAnimation getAnimation() {
        return mAnimation;
    }

    private android.graphics.Insets calculateInsets(android.view.InsetsState state, android.graphics.Rect frame, android.util.SparseArray<android.view.InsetsSourceConsumer> consumers, boolean shown, @android.annotation.Nullable
    @android.view.InsetsState.InsetSide
    android.util.SparseIntArray typeSideMap) {
        for (int i = consumers.size() - 1; i >= 0; i--) {
            state.getSource(consumers.valueAt(i).getType()).setVisible(shown);
        }
        return getInsetsFromState(state, frame, typeSideMap);
    }

    private android.graphics.Insets getInsetsFromState(android.view.InsetsState state, android.graphics.Rect frame, @android.annotation.Nullable
    @android.view.InsetsState.InsetSide
    android.util.SparseIntArray typeSideMap) {
        return /* isScreenRound */
        /* alwaysConsumerNavBar */
        /* displayCutout */
        /* legacyContentInsets */
        /* legacyStableInsets */
        /* legacySoftInputMode */
        state.calculateInsets(frame, false, false, null, null, null, android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE, typeSideMap).getInsets(mTypes);
    }

    private android.graphics.Insets sanitize(android.graphics.Insets insets) {
        return android.graphics.Insets.max(android.graphics.Insets.min(insets, mShownInsets), mHiddenInsets);
    }

    private void updateLeashesForSide(@android.view.InsetsState.InsetSide
    int side, int offset, int inset, java.util.ArrayList<android.view.SyncRtSurfaceTransactionApplier.SurfaceParams> surfaceParams, android.view.InsetsState state) {
        android.util.ArraySet<android.view.InsetsSourceConsumer> items = mSideSourceMap.get(side);
        // TODO: Implement behavior when inset spans over multiple types
        for (int i = items.size() - 1; i >= 0; i--) {
            final android.view.InsetsSourceConsumer consumer = items.valueAt(i);
            final android.view.InsetsSource source = mInitialInsetsState.getSource(consumer.getType());
            final android.view.InsetsSourceControl control = consumer.getControl();
            final android.view.SurfaceControl leash = consumer.getControl().getLeash();
            mTmpMatrix.setTranslate(control.getSurfacePosition().x, control.getSurfacePosition().y);
            mTmpFrame.set(source.getFrame());
            addTranslationToMatrix(side, offset, mTmpMatrix, mTmpFrame);
            state.getSource(source.getType()).setFrame(mTmpFrame);
            surfaceParams.add(new android.view.SyncRtSurfaceTransactionApplier.SurfaceParams(leash, 1.0F, mTmpMatrix, null, 0, 0.0F, inset != 0));
        }
    }

    private void addTranslationToMatrix(@android.view.InsetsState.InsetSide
    int side, int inset, android.graphics.Matrix m, android.graphics.Rect frame) {
        switch (side) {
            case android.view.InsetsState.INSET_SIDE_LEFT :
                m.postTranslate(-inset, 0);
                frame.offset(-inset, 0);
                break;
            case android.view.InsetsState.INSET_SIDE_TOP :
                m.postTranslate(0, -inset);
                frame.offset(0, -inset);
                break;
            case android.view.InsetsState.INSET_SIDE_RIGHT :
                m.postTranslate(inset, 0);
                frame.offset(inset, 0);
                break;
            case android.view.InsetsState.INSET_SIDE_BOTTOM :
                m.postTranslate(0, inset);
                frame.offset(0, inset);
                break;
        }
    }

    private static void buildTypeSourcesMap(android.util.SparseIntArray typeSideMap, android.util.SparseSetArray<android.view.InsetsSourceConsumer> sideSourcesMap, android.util.SparseArray<android.view.InsetsSourceConsumer> consumers) {
        for (int i = typeSideMap.size() - 1; i >= 0; i--) {
            int type = typeSideMap.keyAt(i);
            int side = typeSideMap.valueAt(i);
            sideSourcesMap.add(side, consumers.get(type));
        }
    }
}

