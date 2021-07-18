/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * This transition captures bitmap representations of target views before and
 * after the scene change and fades between them.
 *
 * <p>Note: This transition is not compatible with {@link TextureView}
 * or {@link SurfaceView}.</p>
 *
 * @unknown 
 */
public class Crossfade extends android.transition.Transition {
    // TODO: Add a hook that lets a Transition call user code to query whether it should run on
    // a given target view. This would save bitmap comparisons in this transition, for example.
    private static final java.lang.String LOG_TAG = "Crossfade";

    private static final java.lang.String PROPNAME_BITMAP = "android:crossfade:bitmap";

    private static final java.lang.String PROPNAME_DRAWABLE = "android:crossfade:drawable";

    private static final java.lang.String PROPNAME_BOUNDS = "android:crossfade:bounds";

    private static android.animation.RectEvaluator sRectEvaluator = new android.animation.RectEvaluator();

    private int mFadeBehavior = android.transition.Crossfade.FADE_BEHAVIOR_REVEAL;

    private int mResizeBehavior = android.transition.Crossfade.RESIZE_BEHAVIOR_SCALE;

    /**
     * Flag specifying that the fading animation should cross-fade
     * between the old and new representation of all affected target
     * views. This means that the old representation will fade out
     * while the new one fades in. This effect may work well on views
     * without solid backgrounds, such as TextViews.
     *
     * @see #setFadeBehavior(int)
     */
    public static final int FADE_BEHAVIOR_CROSSFADE = 0;

    /**
     * Flag specifying that the fading animation should reveal the
     * new representation of all affected target views. This means
     * that the old representation will fade out, gradually
     * revealing the new representation, which remains opaque
     * the whole time. This effect may work well on views
     * with solid backgrounds, such as ImageViews.
     *
     * @see #setFadeBehavior(int)
     */
    public static final int FADE_BEHAVIOR_REVEAL = 1;

    /**
     * Flag specifying that the fading animation should first fade
     * out the original representation completely and then fade in the
     * new one. This effect may be more suitable than the other
     * fade behaviors for views with.
     *
     * @see #setFadeBehavior(int)
     */
    public static final int FADE_BEHAVIOR_OUT_IN = 2;

    /**
     * Flag specifying that the transition should not animate any
     * changes in size between the old and new target views.
     * This means that no scaling will take place as a result of
     * this transition
     *
     * @see #setResizeBehavior(int)
     */
    public static final int RESIZE_BEHAVIOR_NONE = 0;

    /**
     * Flag specifying that the transition should animate any
     * changes in size between the old and new target views.
     * This means that the animation will scale the start/end
     * representations of affected views from the starting size
     * to the ending size over the course of the animation.
     * This effect may work well on images, but is not recommended
     * for text.
     *
     * @see #setResizeBehavior(int)
     */
    public static final int RESIZE_BEHAVIOR_SCALE = 1;

    // TODO: Add fade/resize behaviors to xml resources
    /**
     * Sets the type of fading animation that will be run, one of
     * {@link #FADE_BEHAVIOR_CROSSFADE} and {@link #FADE_BEHAVIOR_REVEAL}.
     *
     * @param fadeBehavior
     * 		The type of fading animation to use when this
     * 		transition is run.
     */
    public android.transition.Crossfade setFadeBehavior(int fadeBehavior) {
        if ((fadeBehavior >= android.transition.Crossfade.FADE_BEHAVIOR_CROSSFADE) && (fadeBehavior <= android.transition.Crossfade.FADE_BEHAVIOR_OUT_IN)) {
            mFadeBehavior = fadeBehavior;
        }
        return this;
    }

    /**
     * Returns the fading behavior of the animation.
     *
     * @return This crossfade object.
     * @see #setFadeBehavior(int)
     */
    public int getFadeBehavior() {
        return mFadeBehavior;
    }

    /**
     * Sets the type of resizing behavior that will be used during the
     * transition animation, one of {@link #RESIZE_BEHAVIOR_NONE} and
     * {@link #RESIZE_BEHAVIOR_SCALE}.
     *
     * @param resizeBehavior
     * 		The type of resizing behavior to use when this
     * 		transition is run.
     */
    public android.transition.Crossfade setResizeBehavior(int resizeBehavior) {
        if ((resizeBehavior >= android.transition.Crossfade.RESIZE_BEHAVIOR_NONE) && (resizeBehavior <= android.transition.Crossfade.RESIZE_BEHAVIOR_SCALE)) {
            mResizeBehavior = resizeBehavior;
        }
        return this;
    }

    /**
     * Returns the resizing behavior of the animation.
     *
     * @return This crossfade object.
     * @see #setResizeBehavior(int)
     */
    public int getResizeBehavior() {
        return mResizeBehavior;
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        final boolean useParentOverlay = mFadeBehavior != android.transition.Crossfade.FADE_BEHAVIOR_REVEAL;
        final android.view.View view = endValues.view;
        java.util.Map<java.lang.String, java.lang.Object> startVals = startValues.values;
        java.util.Map<java.lang.String, java.lang.Object> endVals = endValues.values;
        android.graphics.Rect startBounds = ((android.graphics.Rect) (startVals.get(android.transition.Crossfade.PROPNAME_BOUNDS)));
        android.graphics.Rect endBounds = ((android.graphics.Rect) (endVals.get(android.transition.Crossfade.PROPNAME_BOUNDS)));
        android.graphics.Bitmap startBitmap = ((android.graphics.Bitmap) (startVals.get(android.transition.Crossfade.PROPNAME_BITMAP)));
        android.graphics.Bitmap endBitmap = ((android.graphics.Bitmap) (endVals.get(android.transition.Crossfade.PROPNAME_BITMAP)));
        final android.graphics.drawable.BitmapDrawable startDrawable = ((android.graphics.drawable.BitmapDrawable) (startVals.get(android.transition.Crossfade.PROPNAME_DRAWABLE)));
        final android.graphics.drawable.BitmapDrawable endDrawable = ((android.graphics.drawable.BitmapDrawable) (endVals.get(android.transition.Crossfade.PROPNAME_DRAWABLE)));
        if (android.transition.Transition.DBG) {
            android.util.Log.d(android.transition.Crossfade.LOG_TAG, (((("StartBitmap.sameAs(endBitmap) = " + startBitmap.sameAs(endBitmap)) + " for start, end: ") + startBitmap) + ", ") + endBitmap);
        }
        if (((startDrawable != null) && (endDrawable != null)) && (!startBitmap.sameAs(endBitmap))) {
            android.view.ViewOverlay overlay = (useParentOverlay) ? ((android.view.ViewGroup) (view.getParent())).getOverlay() : view.getOverlay();
            if (mFadeBehavior == android.transition.Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.add(endDrawable);
            }
            overlay.add(startDrawable);
            // The transition works by placing the end drawable under the start drawable and
            // gradually fading out the start drawable. So it's not really a cross-fade, but rather
            // a reveal of the end scene over time. Also, animate the bounds of both drawables
            // to mimic the change in the size of the view itself between scenes.
            android.animation.ObjectAnimator anim;
            if (mFadeBehavior == android.transition.Crossfade.FADE_BEHAVIOR_OUT_IN) {
                // Fade out completely halfway through the transition
                anim = android.animation.ObjectAnimator.ofInt(startDrawable, "alpha", 255, 0, 0);
            } else {
                anim = android.animation.ObjectAnimator.ofInt(startDrawable, "alpha", 0);
            }
            anim.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                @java.lang.Override
                public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                    // TODO: some way to auto-invalidate views based on drawable changes? callbacks?
                    view.invalidate(startDrawable.getBounds());
                }
            });
            android.animation.ObjectAnimator anim1 = null;
            if (mFadeBehavior == android.transition.Crossfade.FADE_BEHAVIOR_OUT_IN) {
                // start fading in halfway through the transition
                anim1 = android.animation.ObjectAnimator.ofFloat(view, android.view.View.ALPHA, 0, 0, 1);
            } else
                if (mFadeBehavior == android.transition.Crossfade.FADE_BEHAVIOR_CROSSFADE) {
                    anim1 = android.animation.ObjectAnimator.ofFloat(view, android.view.View.ALPHA, 0, 1);
                }

            if (android.transition.Transition.DBG) {
                android.util.Log.d(android.transition.Crossfade.LOG_TAG, (((("Crossfade: created anim " + anim) + " for start, end values ") + startValues) + ", ") + endValues);
            }
            anim.addListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    android.view.ViewOverlay overlay = (useParentOverlay) ? ((android.view.ViewGroup) (view.getParent())).getOverlay() : view.getOverlay();
                    overlay.remove(startDrawable);
                    if (mFadeBehavior == android.transition.Crossfade.FADE_BEHAVIOR_REVEAL) {
                        overlay.remove(endDrawable);
                    }
                }
            });
            android.animation.AnimatorSet set = new android.animation.AnimatorSet();
            set.playTogether(anim);
            if (anim1 != null) {
                set.playTogether(anim1);
            }
            if ((mResizeBehavior == android.transition.Crossfade.RESIZE_BEHAVIOR_SCALE) && (!startBounds.equals(endBounds))) {
                if (android.transition.Transition.DBG) {
                    android.util.Log.d(android.transition.Crossfade.LOG_TAG, (("animating from startBounds to endBounds: " + startBounds) + ", ") + endBounds);
                }
                android.animation.Animator anim2 = android.animation.ObjectAnimator.ofObject(startDrawable, "bounds", android.transition.Crossfade.sRectEvaluator, startBounds, endBounds);
                set.playTogether(anim2);
                if (mResizeBehavior == android.transition.Crossfade.RESIZE_BEHAVIOR_SCALE) {
                    // TODO: How to handle resizing with a CROSSFADE (vs. REVEAL) effect
                    // when we are animating the view directly?
                    android.animation.Animator anim3 = android.animation.ObjectAnimator.ofObject(endDrawable, "bounds", android.transition.Crossfade.sRectEvaluator, startBounds, endBounds);
                    set.playTogether(anim3);
                }
            }
            return set;
        } else {
            return null;
        }
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        android.view.View view = transitionValues.view;
        android.graphics.Rect bounds = new android.graphics.Rect(0, 0, view.getWidth(), view.getHeight());
        if (mFadeBehavior != android.transition.Crossfade.FADE_BEHAVIOR_REVEAL) {
            bounds.offset(view.getLeft(), view.getTop());
        }
        transitionValues.values.put(android.transition.Crossfade.PROPNAME_BOUNDS, bounds);
        if (android.transition.Transition.DBG) {
            android.util.Log.d(android.transition.Crossfade.LOG_TAG, "Captured bounds " + transitionValues.values.get(android.transition.Crossfade.PROPNAME_BOUNDS));
        }
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(view.getWidth(), view.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        if (view instanceof android.view.TextureView) {
            bitmap = ((android.view.TextureView) (view)).getBitmap();
        } else {
            android.graphics.Canvas c = new android.graphics.Canvas(bitmap);
            view.draw(c);
        }
        transitionValues.values.put(android.transition.Crossfade.PROPNAME_BITMAP, bitmap);
        // TODO: I don't have resources, can't call the non-deprecated method?
        android.graphics.drawable.BitmapDrawable drawable = new android.graphics.drawable.BitmapDrawable(bitmap);
        // TODO: lrtb will be wrong if the view has transXY set
        drawable.setBounds(bounds);
        transitionValues.values.put(android.transition.Crossfade.PROPNAME_DRAWABLE, drawable);
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }
}

