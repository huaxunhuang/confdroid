/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.transition;


/**
 * This Transition captures scale and rotation for Views before and after the
 * scene change and animates those changes during the transition.
 *
 * A change in parent is handled as well by capturing the transforms from
 * the parent before and after the scene change and animating those during the
 * transition.
 */
public class ChangeTransform extends android.transition.Transition {
    private static final java.lang.String TAG = "ChangeTransform";

    private static final java.lang.String PROPNAME_MATRIX = "android:changeTransform:matrix";

    private static final java.lang.String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";

    private static final java.lang.String PROPNAME_PARENT = "android:changeTransform:parent";

    private static final java.lang.String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";

    private static final java.lang.String PROPNAME_INTERMEDIATE_PARENT_MATRIX = "android:changeTransform:intermediateParentMatrix";

    private static final java.lang.String PROPNAME_INTERMEDIATE_MATRIX = "android:changeTransform:intermediateMatrix";

    private static final java.lang.String[] sTransitionProperties = new java.lang.String[]{ android.transition.ChangeTransform.PROPNAME_MATRIX, android.transition.ChangeTransform.PROPNAME_TRANSFORMS, android.transition.ChangeTransform.PROPNAME_PARENT_MATRIX };

    /**
     * This property sets the animation matrix properties that are not translations.
     */
    private static final android.util.Property<android.transition.ChangeTransform.PathAnimatorMatrix, float[]> NON_TRANSLATIONS_PROPERTY = new android.util.Property<android.transition.ChangeTransform.PathAnimatorMatrix, float[]>(float[].class, "nonTranslations") {
        @java.lang.Override
        public float[] get(android.transition.ChangeTransform.PathAnimatorMatrix object) {
            return null;
        }

        @java.lang.Override
        public void set(android.transition.ChangeTransform.PathAnimatorMatrix object, float[] value) {
            object.setValues(value);
        }
    };

    /**
     * This property sets the translation animation matrix properties.
     */
    private static final android.util.Property<android.transition.ChangeTransform.PathAnimatorMatrix, android.graphics.PointF> TRANSLATIONS_PROPERTY = new android.util.Property<android.transition.ChangeTransform.PathAnimatorMatrix, android.graphics.PointF>(android.graphics.PointF.class, "translations") {
        @java.lang.Override
        public android.graphics.PointF get(android.transition.ChangeTransform.PathAnimatorMatrix object) {
            return null;
        }

        @java.lang.Override
        public void set(android.transition.ChangeTransform.PathAnimatorMatrix object, android.graphics.PointF value) {
            object.setTranslation(value);
        }
    };

    private boolean mUseOverlay = true;

    private boolean mReparent = true;

    private android.graphics.Matrix mTempMatrix = new android.graphics.Matrix();

    public ChangeTransform() {
    }

    public ChangeTransform(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeTransform);
        mUseOverlay = a.getBoolean(R.styleable.ChangeTransform_reparentWithOverlay, true);
        mReparent = a.getBoolean(R.styleable.ChangeTransform_reparent, true);
        a.recycle();
    }

    /**
     * Returns whether changes to parent should use an overlay or not. When the parent
     * change doesn't use an overlay, it affects the transforms of the child. The
     * default value is <code>true</code>.
     *
     * <p>Note: when Overlays are not used when a parent changes, a view can be clipped when
     * it moves outside the bounds of its parent. Setting
     * {@link android.view.ViewGroup#setClipChildren(boolean)} and
     * {@link android.view.ViewGroup#setClipToPadding(boolean)} can help. Also, when
     * Overlays are not used and the parent is animating its location, the position of the
     * child view will be relative to its parent's final position, so it may appear to "jump"
     * at the beginning.</p>
     *
     * @return <code>true</code> when a changed parent should execute the transition
    inside the scene root's overlay or <code>false</code> if a parent change only
    affects the transform of the transitioning view.
     * @unknown ref android.R.styleable#ChangeTransform_reparentWithOverlay
     */
    public boolean getReparentWithOverlay() {
        return mUseOverlay;
    }

    /**
     * Sets whether changes to parent should use an overlay or not. When the parent
     * change doesn't use an overlay, it affects the transforms of the child. The
     * default value is <code>true</code>.
     *
     * <p>Note: when Overlays are not used when a parent changes, a view can be clipped when
     * it moves outside the bounds of its parent. Setting
     * {@link android.view.ViewGroup#setClipChildren(boolean)} and
     * {@link android.view.ViewGroup#setClipToPadding(boolean)} can help. Also, when
     * Overlays are not used and the parent is animating its location, the position of the
     * child view will be relative to its parent's final position, so it may appear to "jump"
     * at the beginning.</p>
     *
     * @param reparentWithOverlay
     * 		<code>true</code> when a changed parent should execute the
     * 		transition inside the scene root's overlay or <code>false</code>
     * 		if a parent change only affects the transform of the transitioning
     * 		view.
     * @unknown ref android.R.styleable#ChangeTransform_reparentWithOverlay
     */
    public void setReparentWithOverlay(boolean reparentWithOverlay) {
        mUseOverlay = reparentWithOverlay;
    }

    /**
     * Returns whether parent changes will be tracked by the ChangeTransform. If parent
     * changes are tracked, then the transform will adjust to the transforms of the
     * different parents. If they aren't tracked, only the transforms of the transitioning
     * view will be tracked. Default is true.
     *
     * @return whether parent changes will be tracked by the ChangeTransform.
     * @unknown ref android.R.styleable#ChangeTransform_reparent
     */
    public boolean getReparent() {
        return mReparent;
    }

    /**
     * Sets whether parent changes will be tracked by the ChangeTransform. If parent
     * changes are tracked, then the transform will adjust to the transforms of the
     * different parents. If they aren't tracked, only the transforms of the transitioning
     * view will be tracked. Default is true.
     *
     * @param reparent
     * 		Set to true to track parent changes or false to only track changes
     * 		of the transitioning view without considering the parent change.
     * @unknown ref android.R.styleable#ChangeTransform_reparent
     */
    public void setReparent(boolean reparent) {
        mReparent = reparent;
    }

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return android.transition.ChangeTransform.sTransitionProperties;
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        android.view.View view = transitionValues.view;
        if (view.getVisibility() == android.view.View.GONE) {
            return;
        }
        transitionValues.values.put(android.transition.ChangeTransform.PROPNAME_PARENT, view.getParent());
        android.transition.ChangeTransform.Transforms transforms = new android.transition.ChangeTransform.Transforms(view);
        transitionValues.values.put(android.transition.ChangeTransform.PROPNAME_TRANSFORMS, transforms);
        android.graphics.Matrix matrix = view.getMatrix();
        if ((matrix == null) || matrix.isIdentity()) {
            matrix = null;
        } else {
            matrix = new android.graphics.Matrix(matrix);
        }
        transitionValues.values.put(android.transition.ChangeTransform.PROPNAME_MATRIX, matrix);
        if (mReparent) {
            android.graphics.Matrix parentMatrix = new android.graphics.Matrix();
            android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
            parent.transformMatrixToGlobal(parentMatrix);
            parentMatrix.preTranslate(-parent.getScrollX(), -parent.getScrollY());
            transitionValues.values.put(android.transition.ChangeTransform.PROPNAME_PARENT_MATRIX, parentMatrix);
            transitionValues.values.put(android.transition.ChangeTransform.PROPNAME_INTERMEDIATE_MATRIX, view.getTag(R.id.transitionTransform));
            transitionValues.values.put(android.transition.ChangeTransform.PROPNAME_INTERMEDIATE_PARENT_MATRIX, view.getTag(R.id.parentMatrix));
        }
        return;
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((((startValues == null) || (endValues == null)) || (!startValues.values.containsKey(android.transition.ChangeTransform.PROPNAME_PARENT))) || (!endValues.values.containsKey(android.transition.ChangeTransform.PROPNAME_PARENT))) {
            return null;
        }
        android.view.ViewGroup startParent = ((android.view.ViewGroup) (startValues.values.get(android.transition.ChangeTransform.PROPNAME_PARENT)));
        android.view.ViewGroup endParent = ((android.view.ViewGroup) (endValues.values.get(android.transition.ChangeTransform.PROPNAME_PARENT)));
        boolean handleParentChange = mReparent && (!parentsMatch(startParent, endParent));
        android.graphics.Matrix startMatrix = ((android.graphics.Matrix) (startValues.values.get(android.transition.ChangeTransform.PROPNAME_INTERMEDIATE_MATRIX)));
        if (startMatrix != null) {
            startValues.values.put(android.transition.ChangeTransform.PROPNAME_MATRIX, startMatrix);
        }
        android.graphics.Matrix startParentMatrix = ((android.graphics.Matrix) (startValues.values.get(android.transition.ChangeTransform.PROPNAME_INTERMEDIATE_PARENT_MATRIX)));
        if (startParentMatrix != null) {
            startValues.values.put(android.transition.ChangeTransform.PROPNAME_PARENT_MATRIX, startParentMatrix);
        }
        // First handle the parent change:
        if (handleParentChange) {
            setMatricesForParent(startValues, endValues);
        }
        // Next handle the normal matrix transform:
        android.animation.ObjectAnimator transformAnimator = createTransformAnimator(startValues, endValues, handleParentChange);
        if ((handleParentChange && (transformAnimator != null)) && mUseOverlay) {
            createGhostView(sceneRoot, startValues, endValues);
        }
        return transformAnimator;
    }

    private android.animation.ObjectAnimator createTransformAnimator(android.transition.TransitionValues startValues, android.transition.TransitionValues endValues, final boolean handleParentChange) {
        android.graphics.Matrix startMatrix = ((android.graphics.Matrix) (startValues.values.get(android.transition.ChangeTransform.PROPNAME_MATRIX)));
        android.graphics.Matrix endMatrix = ((android.graphics.Matrix) (endValues.values.get(android.transition.ChangeTransform.PROPNAME_MATRIX)));
        if (startMatrix == null) {
            startMatrix = android.graphics.Matrix.IDENTITY_MATRIX;
        }
        if (endMatrix == null) {
            endMatrix = android.graphics.Matrix.IDENTITY_MATRIX;
        }
        if (startMatrix.equals(endMatrix)) {
            return null;
        }
        final android.transition.ChangeTransform.Transforms transforms = ((android.transition.ChangeTransform.Transforms) (endValues.values.get(android.transition.ChangeTransform.PROPNAME_TRANSFORMS)));
        // clear the transform properties so that we can use the animation matrix instead
        final android.view.View view = endValues.view;
        android.transition.ChangeTransform.setIdentityTransforms(view);
        final float[] startMatrixValues = new float[9];
        startMatrix.getValues(startMatrixValues);
        final float[] endMatrixValues = new float[9];
        endMatrix.getValues(endMatrixValues);
        final android.transition.ChangeTransform.PathAnimatorMatrix pathAnimatorMatrix = new android.transition.ChangeTransform.PathAnimatorMatrix(view, startMatrixValues);
        android.animation.PropertyValuesHolder valuesProperty = android.animation.PropertyValuesHolder.ofObject(android.transition.ChangeTransform.NON_TRANSLATIONS_PROPERTY, new android.animation.FloatArrayEvaluator(new float[9]), startMatrixValues, endMatrixValues);
        android.graphics.Path path = getPathMotion().getPath(startMatrixValues[android.graphics.Matrix.MTRANS_X], startMatrixValues[android.graphics.Matrix.MTRANS_Y], endMatrixValues[android.graphics.Matrix.MTRANS_X], endMatrixValues[android.graphics.Matrix.MTRANS_Y]);
        android.animation.PropertyValuesHolder translationProperty = android.animation.PropertyValuesHolder.ofObject(android.transition.ChangeTransform.TRANSLATIONS_PROPERTY, null, path);
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofPropertyValuesHolder(pathAnimatorMatrix, valuesProperty, translationProperty);
        final android.graphics.Matrix finalEndMatrix = endMatrix;
        android.animation.AnimatorListenerAdapter listener = new android.animation.AnimatorListenerAdapter() {
            private boolean mIsCanceled;

            private android.graphics.Matrix mTempMatrix = new android.graphics.Matrix();

            @java.lang.Override
            public void onAnimationCancel(android.animation.Animator animation) {
                mIsCanceled = true;
            }

            @java.lang.Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (!mIsCanceled) {
                    if (handleParentChange && mUseOverlay) {
                        setCurrentMatrix(finalEndMatrix);
                    } else {
                        view.setTagInternal(R.id.transitionTransform, null);
                        view.setTagInternal(R.id.parentMatrix, null);
                    }
                }
                view.setAnimationMatrix(null);
                transforms.restore(view);
            }

            @java.lang.Override
            public void onAnimationPause(android.animation.Animator animation) {
                android.graphics.Matrix currentMatrix = pathAnimatorMatrix.getMatrix();
                setCurrentMatrix(currentMatrix);
            }

            @java.lang.Override
            public void onAnimationResume(android.animation.Animator animation) {
                android.transition.ChangeTransform.setIdentityTransforms(view);
            }

            private void setCurrentMatrix(android.graphics.Matrix currentMatrix) {
                mTempMatrix.set(currentMatrix);
                view.setTagInternal(R.id.transitionTransform, mTempMatrix);
                transforms.restore(view);
            }
        };
        animator.addListener(listener);
        animator.addPauseListener(listener);
        return animator;
    }

    private boolean parentsMatch(android.view.ViewGroup startParent, android.view.ViewGroup endParent) {
        boolean parentsMatch = false;
        if ((!isValidTarget(startParent)) || (!isValidTarget(endParent))) {
            parentsMatch = startParent == endParent;
        } else {
            android.transition.TransitionValues endValues = getMatchedTransitionValues(startParent, true);
            if (endValues != null) {
                parentsMatch = endParent == endValues.view;
            }
        }
        return parentsMatch;
    }

    private void createGhostView(final android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        android.view.View view = endValues.view;
        android.graphics.Matrix endMatrix = ((android.graphics.Matrix) (endValues.values.get(android.transition.ChangeTransform.PROPNAME_PARENT_MATRIX)));
        android.graphics.Matrix localEndMatrix = new android.graphics.Matrix(endMatrix);
        sceneRoot.transformMatrixToLocal(localEndMatrix);
        android.view.GhostView ghostView = android.view.GhostView.addGhost(view, sceneRoot, localEndMatrix);
        android.transition.Transition outerTransition = this;
        while (outerTransition.mParent != null) {
            outerTransition = outerTransition.mParent;
        } 
        android.transition.ChangeTransform.GhostListener listener = new android.transition.ChangeTransform.GhostListener(view, startValues.view, ghostView);
        outerTransition.addListener(listener);
        if (startValues.view != endValues.view) {
            startValues.view.setTransitionAlpha(0);
        }
        view.setTransitionAlpha(1);
    }

    private void setMatricesForParent(android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        android.graphics.Matrix endParentMatrix = ((android.graphics.Matrix) (endValues.values.get(android.transition.ChangeTransform.PROPNAME_PARENT_MATRIX)));
        endValues.view.setTagInternal(R.id.parentMatrix, endParentMatrix);
        android.graphics.Matrix toLocal = mTempMatrix;
        toLocal.reset();
        endParentMatrix.invert(toLocal);
        android.graphics.Matrix startLocal = ((android.graphics.Matrix) (startValues.values.get(android.transition.ChangeTransform.PROPNAME_MATRIX)));
        if (startLocal == null) {
            startLocal = new android.graphics.Matrix();
            startValues.values.put(android.transition.ChangeTransform.PROPNAME_MATRIX, startLocal);
        }
        android.graphics.Matrix startParentMatrix = ((android.graphics.Matrix) (startValues.values.get(android.transition.ChangeTransform.PROPNAME_PARENT_MATRIX)));
        startLocal.postConcat(startParentMatrix);
        startLocal.postConcat(toLocal);
    }

    private static void setIdentityTransforms(android.view.View view) {
        android.transition.ChangeTransform.setTransforms(view, 0, 0, 0, 1, 1, 0, 0, 0);
    }

    private static void setTransforms(android.view.View view, float translationX, float translationY, float translationZ, float scaleX, float scaleY, float rotationX, float rotationY, float rotationZ) {
        view.setTranslationX(translationX);
        view.setTranslationY(translationY);
        view.setTranslationZ(translationZ);
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        view.setRotationX(rotationX);
        view.setRotationY(rotationY);
        view.setRotation(rotationZ);
    }

    private static class Transforms {
        public final float translationX;

        public final float translationY;

        public final float translationZ;

        public final float scaleX;

        public final float scaleY;

        public final float rotationX;

        public final float rotationY;

        public final float rotationZ;

        public Transforms(android.view.View view) {
            translationX = view.getTranslationX();
            translationY = view.getTranslationY();
            translationZ = view.getTranslationZ();
            scaleX = view.getScaleX();
            scaleY = view.getScaleY();
            rotationX = view.getRotationX();
            rotationY = view.getRotationY();
            rotationZ = view.getRotation();
        }

        public void restore(android.view.View view) {
            android.transition.ChangeTransform.setTransforms(view, translationX, translationY, translationZ, scaleX, scaleY, rotationX, rotationY, rotationZ);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object that) {
            if (!(that instanceof android.transition.ChangeTransform.Transforms)) {
                return false;
            }
            android.transition.ChangeTransform.Transforms thatTransform = ((android.transition.ChangeTransform.Transforms) (that));
            return (((((((thatTransform.translationX == translationX) && (thatTransform.translationY == translationY)) && (thatTransform.translationZ == translationZ)) && (thatTransform.scaleX == scaleX)) && (thatTransform.scaleY == scaleY)) && (thatTransform.rotationX == rotationX)) && (thatTransform.rotationY == rotationY)) && (thatTransform.rotationZ == rotationZ);
        }
    }

    private static class GhostListener extends android.transition.TransitionListenerAdapter {
        private android.view.View mView;

        private android.view.View mStartView;

        private android.view.GhostView mGhostView;

        public GhostListener(android.view.View view, android.view.View startView, android.view.GhostView ghostView) {
            mView = view;
            mStartView = startView;
            mGhostView = ghostView;
        }

        @java.lang.Override
        public void onTransitionEnd(android.transition.Transition transition) {
            transition.removeListener(this);
            android.view.GhostView.removeGhost(mView);
            mView.setTagInternal(R.id.transitionTransform, null);
            mView.setTagInternal(R.id.parentMatrix, null);
            mStartView.setTransitionAlpha(1);
        }

        @java.lang.Override
        public void onTransitionPause(android.transition.Transition transition) {
            mGhostView.setVisibility(android.view.View.INVISIBLE);
        }

        @java.lang.Override
        public void onTransitionResume(android.transition.Transition transition) {
            mGhostView.setVisibility(android.view.View.VISIBLE);
        }
    }

    /**
     * PathAnimatorMatrix allows the translations and the rest of the matrix to be set
     * separately. This allows the PathMotion to affect the translations while scale
     * and rotation are evaluated separately.
     */
    private static class PathAnimatorMatrix {
        private final android.graphics.Matrix mMatrix = new android.graphics.Matrix();

        private final android.view.View mView;

        private final float[] mValues;

        private float mTranslationX;

        private float mTranslationY;

        public PathAnimatorMatrix(android.view.View view, float[] values) {
            mView = view;
            mValues = values.clone();
            mTranslationX = mValues[android.graphics.Matrix.MTRANS_X];
            mTranslationY = mValues[android.graphics.Matrix.MTRANS_Y];
            setAnimationMatrix();
        }

        public void setValues(float[] values) {
            java.lang.System.arraycopy(values, 0, mValues, 0, values.length);
            setAnimationMatrix();
        }

        public void setTranslation(android.graphics.PointF translation) {
            mTranslationX = translation.x;
            mTranslationY = translation.y;
            setAnimationMatrix();
        }

        private void setAnimationMatrix() {
            mValues[android.graphics.Matrix.MTRANS_X] = mTranslationX;
            mValues[android.graphics.Matrix.MTRANS_Y] = mTranslationY;
            mMatrix.setValues(mValues);
            mView.setAnimationMatrix(mMatrix);
        }

        public android.graphics.Matrix getMatrix() {
            return mMatrix;
        }
    }
}

