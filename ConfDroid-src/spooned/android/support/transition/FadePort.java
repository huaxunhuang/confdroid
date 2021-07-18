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
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes and fades views in or out when they become visible
 * or non-visible. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy.
 *
 * <p>The ability of this transition to fade out a particular view, and the
 * way that that fading operation takes place, is based on
 * the situation of the view in the view hierarchy. For example, if a view was
 * simply removed from its parent, then the view will be added into a {@link android.view.ViewGroupOverlay} while fading. If a visible view is
 * changed to be {@link View#GONE} or {@link View#INVISIBLE}, then the
 * visibility will be changed to {@link View#VISIBLE} for the duration of
 * the animation. However, if a view is in a hierarchy which is also altering
 * its visibility, the situation can be more complicated. In general, if a
 * view that is no longer in the hierarchy in the end scene still has a
 * parent (so its parent hierarchy was removed, but it was not removed from
 * its parent), then it will be left alone to avoid side-effects from
 * improperly removing it from its parent. The only exception to this is if
 * the previous {@link android.transition.Scene} was
 * {@link ScenePort#getSceneForLayout(ViewGroup, int, android.content.Context)
 * created from a layout resource file}, then it is considered safe to un-parent
 * the starting scene view in order to fade it out.</p>
 *
 * <p>A Fade transition can be described in a resource file by using the
 * tag <code>fade</code>, along with the standard
 * attributes of {@link android.R.styleable#Fade} and
 * {@link android.R.styleable#Transition}.</p>
 */
class FadePort extends android.support.transition.VisibilityPort {
    /**
     * Fading mode used in {@link #FadePort(int)} to make the transition
     * operate on targets that are appearing. Maybe be combined with
     * {@link #OUT} to fade both in and out.
     */
    public static final int IN = 0x1;

    /**
     * Fading mode used in {@link #FadePort(int)} to make the transition
     * operate on targets that are disappearing. Maybe be combined with
     * {@link #IN} to fade both in and out.
     */
    public static final int OUT = 0x2;

    private static final java.lang.String LOG_TAG = "Fade";

    private static final java.lang.String PROPNAME_SCREEN_X = "android:fade:screenX";

    private static final java.lang.String PROPNAME_SCREEN_Y = "android:fade:screenY";

    private static boolean DBG = android.support.transition.TransitionPort.DBG && false;

    private int mFadingMode;

    /**
     * Constructs a Fade transition that will fade targets in and out.
     */
    public FadePort() {
        this(android.support.transition.FadePort.IN | android.support.transition.FadePort.OUT);
    }

    /**
     * Constructs a Fade transition that will fade targets in
     * and/or out, according to the value of fadingMode.
     *
     * @param fadingMode
     * 		The behavior of this transition, a combination of
     * 		{@link #IN} and {@link #OUT}.
     */
    public FadePort(int fadingMode) {
        mFadingMode = fadingMode;
    }

    /**
     * Utility method to handle creating and running the Animator.
     */
    private android.animation.Animator createAnimation(android.view.View view, float startAlpha, float endAlpha, android.animation.AnimatorListenerAdapter listener) {
        if (startAlpha == endAlpha) {
            // run listener if we're noop'ing the animation, to get the end-state results now
            if (listener != null) {
                listener.onAnimationEnd(null);
            }
            return null;
        }
        final android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
        if (android.support.transition.FadePort.DBG) {
            android.util.Log.d(android.support.transition.FadePort.LOG_TAG, "Created animator " + anim);
        }
        if (listener != null) {
            anim.addListener(listener);
        }
        return anim;
    }

    private void captureValues(android.support.transition.TransitionValues transitionValues) {
        int[] loc = new int[2];
        transitionValues.view.getLocationOnScreen(loc);
        transitionValues.values.put(android.support.transition.FadePort.PROPNAME_SCREEN_X, loc[0]);
        transitionValues.values.put(android.support.transition.FadePort.PROPNAME_SCREEN_Y, loc[1]);
    }

    @java.lang.Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        if (((mFadingMode & android.support.transition.FadePort.IN) != android.support.transition.FadePort.IN) || (endValues == null)) {
            return null;
        }
        final android.view.View endView = endValues.view;
        if (android.support.transition.FadePort.DBG) {
            android.view.View startView = (startValues != null) ? startValues.view : null;
            android.util.Log.d(android.support.transition.FadePort.LOG_TAG, (((((("Fade.onAppear: startView, startVis, endView, endVis = " + startView) + ", ") + startVisibility) + ", ") + endView) + ", ") + endVisibility);
        }
        endView.setAlpha(0);
        android.support.transition.TransitionPort.TransitionListener transitionListener = new android.support.transition.TransitionPort.TransitionListenerAdapter() {
            boolean mCanceled = false;

            float mPausedAlpha;

            @java.lang.Override
            public void onTransitionCancel(android.support.transition.TransitionPort transition) {
                endView.setAlpha(1);
                mCanceled = true;
            }

            @java.lang.Override
            public void onTransitionEnd(android.support.transition.TransitionPort transition) {
                if (!mCanceled) {
                    endView.setAlpha(1);
                }
            }

            @java.lang.Override
            public void onTransitionPause(android.support.transition.TransitionPort transition) {
                mPausedAlpha = endView.getAlpha();
                endView.setAlpha(1);
            }

            @java.lang.Override
            public void onTransitionResume(android.support.transition.TransitionPort transition) {
                endView.setAlpha(mPausedAlpha);
            }
        };
        addListener(transitionListener);
        return createAnimation(endView, 0, 1, null);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        if ((mFadingMode & android.support.transition.FadePort.OUT) != android.support.transition.FadePort.OUT) {
            return null;
        }
        android.view.View view = null;
        android.view.View startView = (startValues != null) ? startValues.view : null;
        android.view.View endView = (endValues != null) ? endValues.view : null;
        if (android.support.transition.FadePort.DBG) {
            android.util.Log.d(android.support.transition.FadePort.LOG_TAG, (((((("Fade.onDisappear: startView, startVis, endView, endVis = " + startView) + ", ") + startVisibility) + ", ") + endView) + ", ") + endVisibility);
        }
        android.view.View overlayView = null;
        android.view.View viewToKeep = null;
        if ((endView == null) || (endView.getParent() == null)) {
            if (endView != null) {
                // endView was removed from its parent - add it to the overlay
                view = overlayView = endView;
            } else
                if (startView != null) {
                    // endView does not exist. Use startView only under certain
                    // conditions, because placing a view in an overlay necessitates
                    // it being removed from its current parent
                    if (startView.getParent() == null) {
                        // no parent - safe to use
                        view = overlayView = startView;
                    } else
                        if ((startView.getParent() instanceof android.view.View) && (startView.getParent().getParent() == null)) {
                            android.view.View startParent = ((android.view.View) (startView.getParent()));
                            int id = startParent.getId();
                            if (((id != android.view.View.NO_ID) && (sceneRoot.findViewById(id) != null)) && mCanRemoveViews) {
                                // no parent, but its parent is unparented  but the parent
                                // hierarchy has been replaced by a new hierarchy with the same id
                                // and it is safe to un-parent startView
                                view = overlayView = startView;
                            }
                        }

                }

        } else {
            // visibility change
            if (endVisibility == android.view.View.INVISIBLE) {
                view = endView;
                viewToKeep = view;
            } else {
                // Becoming GONE
                if (startView == endView) {
                    view = endView;
                    viewToKeep = view;
                } else {
                    view = startView;
                    overlayView = view;
                }
            }
        }
        final int finalVisibility = endVisibility;
        // TODO: add automatic facility to Visibility superclass for keeping views around
        if (overlayView != null) {
            // TODO: Need to do this for general case of adding to overlay
            int screenX = ((java.lang.Integer) (startValues.values.get(android.support.transition.FadePort.PROPNAME_SCREEN_X)));
            int screenY = ((java.lang.Integer) (startValues.values.get(android.support.transition.FadePort.PROPNAME_SCREEN_Y)));
            int[] loc = new int[2];
            sceneRoot.getLocationOnScreen(loc);
            android.support.v4.view.ViewCompat.offsetLeftAndRight(overlayView, (screenX - loc[0]) - overlayView.getLeft());
            android.support.v4.view.ViewCompat.offsetTopAndBottom(overlayView, (screenY - loc[1]) - overlayView.getTop());
            android.support.transition.ViewGroupOverlay.createFrom(sceneRoot).add(overlayView);
            // sceneRoot.getOverlay().add(overlayView);
            // TODO: add automatic facility to Visibility superclass for keeping views around
            final float startAlpha = 1;
            float endAlpha = 0;
            final android.view.View finalView = view;
            final android.view.View finalOverlayView = overlayView;
            final android.view.View finalViewToKeep = viewToKeep;
            final android.view.ViewGroup finalSceneRoot = sceneRoot;
            final android.animation.AnimatorListenerAdapter endListener = new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    finalView.setAlpha(startAlpha);
                    // TODO: restore view offset from overlay repositioning
                    if (finalViewToKeep != null) {
                        finalViewToKeep.setVisibility(finalVisibility);
                    }
                    if (finalOverlayView != null) {
                        android.support.transition.ViewGroupOverlay.createFrom(finalSceneRoot).remove(finalOverlayView);
                        // finalSceneRoot.getOverlay().remove(finalOverlayView);
                    }
                }
            };
            return createAnimation(view, startAlpha, endAlpha, endListener);
        }
        if (viewToKeep != null) {
            // TODO: find a different way to do this, like just changing the view to be
            // VISIBLE for the duration of the transition
            viewToKeep.setVisibility(android.view.View.VISIBLE);
            // TODO: add automatic facility to Visibility superclass for keeping views around
            final float startAlpha = 1;
            float endAlpha = 0;
            final android.view.View finalView = view;
            final android.view.View finalOverlayView = overlayView;
            final android.view.View finalViewToKeep = viewToKeep;
            final android.view.ViewGroup finalSceneRoot = sceneRoot;
            final android.animation.AnimatorListenerAdapter endListener = new android.animation.AnimatorListenerAdapter() {
                boolean mCanceled = false;

                float mPausedAlpha = -1;

                // @Override
                // public void onAnimationPause(Animator animation) {
                // if (finalViewToKeep != null && !mCanceled) {
                // finalViewToKeep.setVisibility(finalVisibility);
                // }
                // mPausedAlpha = finalView.getAlpha();
                // finalView.setAlpha(startAlpha);
                // }
                // 
                // @Override
                // public void onAnimationResume(Animator animation) {
                // if (finalViewToKeep != null && !mCanceled) {
                // finalViewToKeep.setVisibility(View.VISIBLE);
                // }
                // finalView.setAlpha(mPausedAlpha);
                // }
                @java.lang.Override
                public void onAnimationCancel(android.animation.Animator animation) {
                    mCanceled = true;
                    if (mPausedAlpha >= 0) {
                        finalView.setAlpha(mPausedAlpha);
                    }
                }

                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    if (!mCanceled) {
                        finalView.setAlpha(startAlpha);
                    }
                    // TODO: restore view offset from overlay repositioning
                    if ((finalViewToKeep != null) && (!mCanceled)) {
                        finalViewToKeep.setVisibility(finalVisibility);
                    }
                    if (finalOverlayView != null) {
                        android.support.transition.ViewGroupOverlay.createFrom(finalSceneRoot).add(finalOverlayView);
                        // finalSceneRoot.getOverlay().remove(finalOverlayView);
                    }
                }
            };
            return createAnimation(view, startAlpha, endAlpha, endListener);
        }
        return null;
    }
}

