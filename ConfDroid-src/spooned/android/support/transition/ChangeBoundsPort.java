/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.transition;


/**
 * This transition captures the layout bounds of target views before and after
 * the scene change and animates those changes during the transition.
 *
 * <p>A ChangeBounds transition can be described in a resource file by using the
 * tag <code>changeBounds</code>, along with the other standard
 * attributes of {@link android.R.styleable#Transition}.</p>
 */
class ChangeBoundsPort extends android.support.transition.TransitionPort {
    private static final java.lang.String PROPNAME_BOUNDS = "android:changeBounds:bounds";

    private static final java.lang.String PROPNAME_PARENT = "android:changeBounds:parent";

    private static final java.lang.String PROPNAME_WINDOW_X = "android:changeBounds:windowX";

    private static final java.lang.String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";

    private static final java.lang.String[] sTransitionProperties = new java.lang.String[]{ android.support.transition.ChangeBoundsPort.PROPNAME_BOUNDS, android.support.transition.ChangeBoundsPort.PROPNAME_PARENT, android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_X, android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_Y };

    private static final java.lang.String LOG_TAG = "ChangeBounds";

    private static android.support.transition.RectEvaluator sRectEvaluator = new android.support.transition.RectEvaluator();

    int[] tempLocation = new int[2];

    boolean mResizeClip = false;

    boolean mReparent = false;

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return android.support.transition.ChangeBoundsPort.sTransitionProperties;
    }

    public void setResizeClip(boolean resizeClip) {
        mResizeClip = resizeClip;
    }

    /**
     * Setting this flag tells ChangeBounds to track the before/after parent
     * of every view using this transition. The flag is not enabled by
     * default because it requires the parent instances to be the same
     * in the two scenes or else all parents must use ids to allow
     * the transition to determine which parents are the same.
     *
     * @param reparent
     * 		true if the transition should track the parent
     * 		container of target views and animate parent changes.
     */
    public void setReparent(boolean reparent) {
        mReparent = reparent;
    }

    private void captureValues(android.support.transition.TransitionValues values) {
        android.view.View view = values.view;
        values.values.put(android.support.transition.ChangeBoundsPort.PROPNAME_BOUNDS, new android.graphics.Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        values.values.put(android.support.transition.ChangeBoundsPort.PROPNAME_PARENT, values.view.getParent());
        values.view.getLocationInWindow(tempLocation);
        values.values.put(android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_X, tempLocation[0]);
        values.values.put(android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_Y, tempLocation[1]);
    }

    @java.lang.Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.support.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(final android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, android.support.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        java.util.Map<java.lang.String, java.lang.Object> startParentVals = startValues.values;
        java.util.Map<java.lang.String, java.lang.Object> endParentVals = endValues.values;
        android.view.ViewGroup startParent = ((android.view.ViewGroup) (startParentVals.get(android.support.transition.ChangeBoundsPort.PROPNAME_PARENT)));
        android.view.ViewGroup endParent = ((android.view.ViewGroup) (endParentVals.get(android.support.transition.ChangeBoundsPort.PROPNAME_PARENT)));
        if ((startParent == null) || (endParent == null)) {
            return null;
        }
        final android.view.View view = endValues.view;
        boolean parentsEqual = (startParent == endParent) || (startParent.getId() == endParent.getId());
        // TODO: Might want reparenting to be separate/subclass transition, or at least
        // triggered by a property on ChangeBounds. Otherwise, we're forcing the requirement that
        // all parents in layouts have IDs to avoid layout-inflation resulting in a side-effect
        // of reparenting the views.
        if ((!mReparent) || parentsEqual) {
            android.graphics.Rect startBounds = ((android.graphics.Rect) (startValues.values.get(android.support.transition.ChangeBoundsPort.PROPNAME_BOUNDS)));
            android.graphics.Rect endBounds = ((android.graphics.Rect) (endValues.values.get(android.support.transition.ChangeBoundsPort.PROPNAME_BOUNDS)));
            int startLeft = startBounds.left;
            int endLeft = endBounds.left;
            int startTop = startBounds.top;
            int endTop = endBounds.top;
            int startRight = startBounds.right;
            int endRight = endBounds.right;
            int startBottom = startBounds.bottom;
            int endBottom = endBounds.bottom;
            int startWidth = startRight - startLeft;
            int startHeight = startBottom - startTop;
            int endWidth = endRight - endLeft;
            int endHeight = endBottom - endTop;
            int numChanges = 0;
            if ((((startWidth != 0) && (startHeight != 0)) && (endWidth != 0)) && (endHeight != 0)) {
                if (startLeft != endLeft) {
                    ++numChanges;
                }
                if (startTop != endTop) {
                    ++numChanges;
                }
                if (startRight != endRight) {
                    ++numChanges;
                }
                if (startBottom != endBottom) {
                    ++numChanges;
                }
            }
            if (numChanges > 0) {
                if (!mResizeClip) {
                    android.animation.PropertyValuesHolder[] pvh = new android.animation.PropertyValuesHolder[numChanges];
                    int pvhIndex = 0;
                    if (startLeft != endLeft) {
                        view.setLeft(startLeft);
                    }
                    if (startTop != endTop) {
                        view.setTop(startTop);
                    }
                    if (startRight != endRight) {
                        view.setRight(startRight);
                    }
                    if (startBottom != endBottom) {
                        view.setBottom(startBottom);
                    }
                    if (startLeft != endLeft) {
                        pvh[pvhIndex++] = android.animation.PropertyValuesHolder.ofInt("left", startLeft, endLeft);
                    }
                    if (startTop != endTop) {
                        pvh[pvhIndex++] = android.animation.PropertyValuesHolder.ofInt("top", startTop, endTop);
                    }
                    if (startRight != endRight) {
                        pvh[pvhIndex++] = android.animation.PropertyValuesHolder.ofInt("right", startRight, endRight);
                    }
                    if (startBottom != endBottom) {
                        pvh[pvhIndex++] = android.animation.PropertyValuesHolder.ofInt("bottom", startBottom, endBottom);
                    }
                    android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofPropertyValuesHolder(view, pvh);
                    if (view.getParent() instanceof android.view.ViewGroup) {
                        final android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
                        // parent.suppressLayout(true);
                        android.support.transition.TransitionPort.TransitionListener transitionListener = new android.support.transition.TransitionPort.TransitionListenerAdapter() {
                            boolean mCanceled = false;

                            @java.lang.Override
                            public void onTransitionCancel(android.support.transition.TransitionPort transition) {
                                // parent.suppressLayout(false);
                                mCanceled = true;
                            }

                            @java.lang.Override
                            public void onTransitionEnd(android.support.transition.TransitionPort transition) {
                                if (!mCanceled) {
                                    // parent.suppressLayout(false);
                                }
                            }

                            @java.lang.Override
                            public void onTransitionPause(android.support.transition.TransitionPort transition) {
                                // parent.suppressLayout(false);
                            }

                            @java.lang.Override
                            public void onTransitionResume(android.support.transition.TransitionPort transition) {
                                // parent.suppressLayout(true);
                            }
                        };
                        addListener(transitionListener);
                    }
                    return anim;
                } else {
                    if (startWidth != endWidth) {
                        view.setRight(endLeft + java.lang.Math.max(startWidth, endWidth));
                    }
                    if (startHeight != endHeight) {
                        view.setBottom(endTop + java.lang.Math.max(startHeight, endHeight));
                    }
                    // TODO: don't clobber TX/TY
                    if (startLeft != endLeft) {
                        view.setTranslationX(startLeft - endLeft);
                    }
                    if (startTop != endTop) {
                        view.setTranslationY(startTop - endTop);
                    }
                    // Animate location with translationX/Y and size with clip bounds
                    float transXDelta = endLeft - startLeft;
                    float transYDelta = endTop - startTop;
                    int widthDelta = endWidth - startWidth;
                    int heightDelta = endHeight - startHeight;
                    numChanges = 0;
                    if (transXDelta != 0) {
                        numChanges++;
                    }
                    if (transYDelta != 0) {
                        numChanges++;
                    }
                    if ((widthDelta != 0) || (heightDelta != 0)) {
                        numChanges++;
                    }
                    android.animation.PropertyValuesHolder[] pvh = new android.animation.PropertyValuesHolder[numChanges];
                    int pvhIndex = 0;
                    if (transXDelta != 0) {
                        pvh[pvhIndex++] = android.animation.PropertyValuesHolder.ofFloat("translationX", view.getTranslationX(), 0);
                    }
                    if (transYDelta != 0) {
                        pvh[pvhIndex++] = android.animation.PropertyValuesHolder.ofFloat("translationY", view.getTranslationY(), 0);
                    }
                    if ((widthDelta != 0) || (heightDelta != 0)) {
                        android.graphics.Rect tempStartBounds = new android.graphics.Rect(0, 0, startWidth, startHeight);
                        android.graphics.Rect tempEndBounds = new android.graphics.Rect(0, 0, endWidth, endHeight);
                        // pvh[pvhIndex++] = PropertyValuesHolder.ofObject("clipBounds",
                        // sRectEvaluator, tempStartBounds, tempEndBounds);
                    }
                    android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofPropertyValuesHolder(view, pvh);
                    if (view.getParent() instanceof android.view.ViewGroup) {
                        final android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
                        // parent.suppressLayout(true);
                        android.support.transition.TransitionPort.TransitionListener transitionListener = new android.support.transition.TransitionPort.TransitionListenerAdapter() {
                            boolean mCanceled = false;

                            @java.lang.Override
                            public void onTransitionCancel(android.support.transition.TransitionPort transition) {
                                // parent.suppressLayout(false);
                                mCanceled = true;
                            }

                            @java.lang.Override
                            public void onTransitionEnd(android.support.transition.TransitionPort transition) {
                                if (!mCanceled) {
                                    // parent.suppressLayout(false);
                                }
                            }

                            @java.lang.Override
                            public void onTransitionPause(android.support.transition.TransitionPort transition) {
                                // parent.suppressLayout(false);
                            }

                            @java.lang.Override
                            public void onTransitionResume(android.support.transition.TransitionPort transition) {
                                // parent.suppressLayout(true);
                            }
                        };
                        addListener(transitionListener);
                    }
                    anim.addListener(new android.animation.AnimatorListenerAdapter() {
                        @java.lang.Override
                        public void onAnimationEnd(android.animation.Animator animation) {
                            // view.setClipBounds(null);
                        }
                    });
                    return anim;
                }
            }
        } else {
            int startX = ((java.lang.Integer) (startValues.values.get(android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_X)));
            int startY = ((java.lang.Integer) (startValues.values.get(android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_Y)));
            int endX = ((java.lang.Integer) (endValues.values.get(android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_X)));
            int endY = ((java.lang.Integer) (endValues.values.get(android.support.transition.ChangeBoundsPort.PROPNAME_WINDOW_Y)));
            // TODO: also handle size changes: check bounds and animate size changes
            if ((startX != endX) || (startY != endY)) {
                sceneRoot.getLocationInWindow(tempLocation);
                android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(view.getWidth(), view.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
                android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
                view.draw(canvas);
                final android.graphics.drawable.BitmapDrawable drawable = new android.graphics.drawable.BitmapDrawable(bitmap);
                view.setVisibility(android.view.View.INVISIBLE);
                android.support.transition.ViewOverlay.createFrom(sceneRoot).add(drawable);
                // sceneRoot.getOverlay().add(drawable);
                android.graphics.Rect startBounds1 = new android.graphics.Rect(startX - tempLocation[0], startY - tempLocation[1], (startX - tempLocation[0]) + view.getWidth(), (startY - tempLocation[1]) + view.getHeight());
                android.graphics.Rect endBounds1 = new android.graphics.Rect(endX - tempLocation[0], endY - tempLocation[1], (endX - tempLocation[0]) + view.getWidth(), (endY - tempLocation[1]) + view.getHeight());
                android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofObject(drawable, "bounds", android.support.transition.ChangeBoundsPort.sRectEvaluator, startBounds1, endBounds1);
                anim.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        android.support.transition.ViewOverlay.createFrom(sceneRoot).remove(drawable);
                        // sceneRoot.getOverlay().remove(drawable);
                        view.setVisibility(android.view.View.VISIBLE);
                    }
                });
                return anim;
            }
        }
        return null;
    }
}

