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
 * This transition captures the layout bounds of target views before and after
 * the scene change and animates those changes during the transition.
 *
 * <p>A ChangeBounds transition can be described in a resource file by using the
 * tag <code>changeBounds</code>, using its attributes of
 * {@link android.R.styleable#ChangeBounds} along with the other standard
 * attributes of {@link android.R.styleable#Transition}.</p>
 */
public class ChangeBounds extends android.transition.Transition {
    private static final java.lang.String PROPNAME_BOUNDS = "android:changeBounds:bounds";

    private static final java.lang.String PROPNAME_CLIP = "android:changeBounds:clip";

    private static final java.lang.String PROPNAME_PARENT = "android:changeBounds:parent";

    private static final java.lang.String PROPNAME_WINDOW_X = "android:changeBounds:windowX";

    private static final java.lang.String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";

    private static final java.lang.String[] sTransitionProperties = new java.lang.String[]{ android.transition.ChangeBounds.PROPNAME_BOUNDS, android.transition.ChangeBounds.PROPNAME_CLIP, android.transition.ChangeBounds.PROPNAME_PARENT, android.transition.ChangeBounds.PROPNAME_WINDOW_X, android.transition.ChangeBounds.PROPNAME_WINDOW_Y };

    private static final android.util.Property<android.graphics.drawable.Drawable, android.graphics.PointF> DRAWABLE_ORIGIN_PROPERTY = new android.util.Property<android.graphics.drawable.Drawable, android.graphics.PointF>(android.graphics.PointF.class, "boundsOrigin") {
        private android.graphics.Rect mBounds = new android.graphics.Rect();

        @java.lang.Override
        public void set(android.graphics.drawable.Drawable object, android.graphics.PointF value) {
            object.copyBounds(mBounds);
            mBounds.offsetTo(java.lang.Math.round(value.x), java.lang.Math.round(value.y));
            object.setBounds(mBounds);
        }

        @java.lang.Override
        public android.graphics.PointF get(android.graphics.drawable.Drawable object) {
            object.copyBounds(mBounds);
            return new android.graphics.PointF(mBounds.left, mBounds.top);
        }
    };

    private static final android.util.Property<android.transition.ChangeBounds.ViewBounds, android.graphics.PointF> TOP_LEFT_PROPERTY = new android.util.Property<android.transition.ChangeBounds.ViewBounds, android.graphics.PointF>(android.graphics.PointF.class, "topLeft") {
        @java.lang.Override
        public void set(android.transition.ChangeBounds.ViewBounds viewBounds, android.graphics.PointF topLeft) {
            viewBounds.setTopLeft(topLeft);
        }

        @java.lang.Override
        public android.graphics.PointF get(android.transition.ChangeBounds.ViewBounds viewBounds) {
            return null;
        }
    };

    private static final android.util.Property<android.transition.ChangeBounds.ViewBounds, android.graphics.PointF> BOTTOM_RIGHT_PROPERTY = new android.util.Property<android.transition.ChangeBounds.ViewBounds, android.graphics.PointF>(android.graphics.PointF.class, "bottomRight") {
        @java.lang.Override
        public void set(android.transition.ChangeBounds.ViewBounds viewBounds, android.graphics.PointF bottomRight) {
            viewBounds.setBottomRight(bottomRight);
        }

        @java.lang.Override
        public android.graphics.PointF get(android.transition.ChangeBounds.ViewBounds viewBounds) {
            return null;
        }
    };

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private static final android.util.Property<android.view.View, android.graphics.PointF> BOTTOM_RIGHT_ONLY_PROPERTY = new android.util.Property<android.view.View, android.graphics.PointF>(android.graphics.PointF.class, "bottomRight") {
        @java.lang.Override
        public void set(android.view.View view, android.graphics.PointF bottomRight) {
            int left = view.getLeft();
            int top = view.getTop();
            int right = java.lang.Math.round(bottomRight.x);
            int bottom = java.lang.Math.round(bottomRight.y);
            view.setLeftTopRightBottom(left, top, right, bottom);
        }

        @java.lang.Override
        public android.graphics.PointF get(android.view.View view) {
            return null;
        }
    };

    private static final android.util.Property<android.view.View, android.graphics.PointF> TOP_LEFT_ONLY_PROPERTY = new android.util.Property<android.view.View, android.graphics.PointF>(android.graphics.PointF.class, "topLeft") {
        @java.lang.Override
        public void set(android.view.View view, android.graphics.PointF topLeft) {
            int left = java.lang.Math.round(topLeft.x);
            int top = java.lang.Math.round(topLeft.y);
            int right = view.getRight();
            int bottom = view.getBottom();
            view.setLeftTopRightBottom(left, top, right, bottom);
        }

        @java.lang.Override
        public android.graphics.PointF get(android.view.View view) {
            return null;
        }
    };

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private static final android.util.Property<android.view.View, android.graphics.PointF> POSITION_PROPERTY = new android.util.Property<android.view.View, android.graphics.PointF>(android.graphics.PointF.class, "position") {
        @java.lang.Override
        public void set(android.view.View view, android.graphics.PointF topLeft) {
            int left = java.lang.Math.round(topLeft.x);
            int top = java.lang.Math.round(topLeft.y);
            int right = left + view.getWidth();
            int bottom = top + view.getHeight();
            view.setLeftTopRightBottom(left, top, right, bottom);
        }

        @java.lang.Override
        public android.graphics.PointF get(android.view.View view) {
            return null;
        }
    };

    int[] tempLocation = new int[2];

    boolean mResizeClip = false;

    boolean mReparent = false;

    private static final java.lang.String LOG_TAG = "ChangeBounds";

    private static android.animation.RectEvaluator sRectEvaluator = new android.animation.RectEvaluator();

    public ChangeBounds() {
    }

    public ChangeBounds(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeBounds);
        boolean resizeClip = a.getBoolean(R.styleable.ChangeBounds_resizeClip, false);
        a.recycle();
        setResizeClip(resizeClip);
    }

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return android.transition.ChangeBounds.sTransitionProperties;
    }

    /**
     * When <code>resizeClip</code> is true, ChangeBounds resizes the view using the clipBounds
     * instead of changing the dimensions of the view during the animation. When
     * <code>resizeClip</code> is false, ChangeBounds resizes the View by changing its dimensions.
     *
     * <p>When resizeClip is set to true, the clip bounds is modified by ChangeBounds. Therefore,
     * {@link android.transition.ChangeClipBounds} is not compatible with ChangeBounds
     * in this mode.</p>
     *
     * @param resizeClip
     * 		Used to indicate whether the view bounds should be modified or the
     * 		clip bounds should be modified by ChangeBounds.
     * @see android.view.View#setClipBounds(android.graphics.Rect)
     * @unknown ref android.R.styleable#ChangeBounds_resizeClip
     */
    public void setResizeClip(boolean resizeClip) {
        mResizeClip = resizeClip;
    }

    /**
     * Returns true when the ChangeBounds will resize by changing the clip bounds during the
     * view animation or false when bounds are changed. The default value is false.
     *
     * @return true when the ChangeBounds will resize by changing the clip bounds during the
    view animation or false when bounds are changed. The default value is false.
     * @unknown ref android.R.styleable#ChangeBounds_resizeClip
     */
    public boolean getResizeClip() {
        return mResizeClip;
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
     * @deprecated Use {@link android.transition.ChangeTransform} to handle
    transitions between different parents.
     */
    @java.lang.Deprecated
    public void setReparent(boolean reparent) {
        mReparent = reparent;
    }

    private void captureValues(android.transition.TransitionValues values) {
        android.view.View view = values.view;
        if ((view.isLaidOut() || (view.getWidth() != 0)) || (view.getHeight() != 0)) {
            values.values.put(android.transition.ChangeBounds.PROPNAME_BOUNDS, new android.graphics.Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            values.values.put(android.transition.ChangeBounds.PROPNAME_PARENT, values.view.getParent());
            if (mReparent) {
                values.view.getLocationInWindow(tempLocation);
                values.values.put(android.transition.ChangeBounds.PROPNAME_WINDOW_X, tempLocation[0]);
                values.values.put(android.transition.ChangeBounds.PROPNAME_WINDOW_Y, tempLocation[1]);
            }
            if (mResizeClip) {
                values.values.put(android.transition.ChangeBounds.PROPNAME_CLIP, view.getClipBounds());
            }
        }
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private boolean parentMatches(android.view.View startParent, android.view.View endParent) {
        boolean parentMatches = true;
        if (mReparent) {
            android.transition.TransitionValues endValues = getMatchedTransitionValues(startParent, true);
            if (endValues == null) {
                parentMatches = startParent == endParent;
            } else {
                parentMatches = endParent == endValues.view;
            }
        }
        return parentMatches;
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(final android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        java.util.Map<java.lang.String, java.lang.Object> startParentVals = startValues.values;
        java.util.Map<java.lang.String, java.lang.Object> endParentVals = endValues.values;
        android.view.ViewGroup startParent = ((android.view.ViewGroup) (startParentVals.get(android.transition.ChangeBounds.PROPNAME_PARENT)));
        android.view.ViewGroup endParent = ((android.view.ViewGroup) (endParentVals.get(android.transition.ChangeBounds.PROPNAME_PARENT)));
        if ((startParent == null) || (endParent == null)) {
            return null;
        }
        final android.view.View view = endValues.view;
        if (parentMatches(startParent, endParent)) {
            android.graphics.Rect startBounds = ((android.graphics.Rect) (startValues.values.get(android.transition.ChangeBounds.PROPNAME_BOUNDS)));
            android.graphics.Rect endBounds = ((android.graphics.Rect) (endValues.values.get(android.transition.ChangeBounds.PROPNAME_BOUNDS)));
            final int startLeft = startBounds.left;
            final int endLeft = endBounds.left;
            final int startTop = startBounds.top;
            final int endTop = endBounds.top;
            final int startRight = startBounds.right;
            final int endRight = endBounds.right;
            final int startBottom = startBounds.bottom;
            final int endBottom = endBounds.bottom;
            final int startWidth = startRight - startLeft;
            final int startHeight = startBottom - startTop;
            final int endWidth = endRight - endLeft;
            final int endHeight = endBottom - endTop;
            android.graphics.Rect startClip = ((android.graphics.Rect) (startValues.values.get(android.transition.ChangeBounds.PROPNAME_CLIP)));
            android.graphics.Rect endClip = ((android.graphics.Rect) (endValues.values.get(android.transition.ChangeBounds.PROPNAME_CLIP)));
            int numChanges = 0;
            if (((startWidth != 0) && (startHeight != 0)) || ((endWidth != 0) && (endHeight != 0))) {
                if ((startLeft != endLeft) || (startTop != endTop))
                    ++numChanges;

                if ((startRight != endRight) || (startBottom != endBottom))
                    ++numChanges;

            }
            if (((startClip != null) && (!startClip.equals(endClip))) || ((startClip == null) && (endClip != null))) {
                ++numChanges;
            }
            if (numChanges > 0) {
                if (view.getParent() instanceof android.view.ViewGroup) {
                    final android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
                    parent.suppressLayout(true);
                    android.transition.Transition.TransitionListener transitionListener = new android.transition.TransitionListenerAdapter() {
                        boolean mCanceled = false;

                        @java.lang.Override
                        public void onTransitionCancel(android.transition.Transition transition) {
                            parent.suppressLayout(false);
                            mCanceled = true;
                        }

                        @java.lang.Override
                        public void onTransitionEnd(android.transition.Transition transition) {
                            if (!mCanceled) {
                                parent.suppressLayout(false);
                            }
                            transition.removeListener(this);
                        }

                        @java.lang.Override
                        public void onTransitionPause(android.transition.Transition transition) {
                            parent.suppressLayout(false);
                        }

                        @java.lang.Override
                        public void onTransitionResume(android.transition.Transition transition) {
                            parent.suppressLayout(true);
                        }
                    };
                    addListener(transitionListener);
                }
                android.animation.Animator anim;
                if (!mResizeClip) {
                    view.setLeftTopRightBottom(startLeft, startTop, startRight, startBottom);
                    if (numChanges == 2) {
                        if ((startWidth == endWidth) && (startHeight == endHeight)) {
                            android.graphics.Path topLeftPath = getPathMotion().getPath(startLeft, startTop, endLeft, endTop);
                            anim = android.animation.ObjectAnimator.ofObject(view, android.transition.ChangeBounds.POSITION_PROPERTY, null, topLeftPath);
                        } else {
                            final android.transition.ChangeBounds.ViewBounds viewBounds = new android.transition.ChangeBounds.ViewBounds(view);
                            android.graphics.Path topLeftPath = getPathMotion().getPath(startLeft, startTop, endLeft, endTop);
                            android.animation.ObjectAnimator topLeftAnimator = android.animation.ObjectAnimator.ofObject(viewBounds, android.transition.ChangeBounds.TOP_LEFT_PROPERTY, null, topLeftPath);
                            android.graphics.Path bottomRightPath = getPathMotion().getPath(startRight, startBottom, endRight, endBottom);
                            android.animation.ObjectAnimator bottomRightAnimator = android.animation.ObjectAnimator.ofObject(viewBounds, android.transition.ChangeBounds.BOTTOM_RIGHT_PROPERTY, null, bottomRightPath);
                            android.animation.AnimatorSet set = new android.animation.AnimatorSet();
                            set.playTogether(topLeftAnimator, bottomRightAnimator);
                            anim = set;
                            set.addListener(new android.animation.AnimatorListenerAdapter() {
                                // We need a strong reference to viewBounds until the
                                // animator ends.
                                private android.transition.ChangeBounds.ViewBounds mViewBounds = viewBounds;
                            });
                        }
                    } else
                        if ((startLeft != endLeft) || (startTop != endTop)) {
                            android.graphics.Path topLeftPath = getPathMotion().getPath(startLeft, startTop, endLeft, endTop);
                            anim = android.animation.ObjectAnimator.ofObject(view, android.transition.ChangeBounds.TOP_LEFT_ONLY_PROPERTY, null, topLeftPath);
                        } else {
                            android.graphics.Path bottomRight = getPathMotion().getPath(startRight, startBottom, endRight, endBottom);
                            anim = android.animation.ObjectAnimator.ofObject(view, android.transition.ChangeBounds.BOTTOM_RIGHT_ONLY_PROPERTY, null, bottomRight);
                        }

                } else {
                    int maxWidth = java.lang.Math.max(startWidth, endWidth);
                    int maxHeight = java.lang.Math.max(startHeight, endHeight);
                    view.setLeftTopRightBottom(startLeft, startTop, startLeft + maxWidth, startTop + maxHeight);
                    android.animation.ObjectAnimator positionAnimator = null;
                    if ((startLeft != endLeft) || (startTop != endTop)) {
                        android.graphics.Path topLeftPath = getPathMotion().getPath(startLeft, startTop, endLeft, endTop);
                        positionAnimator = android.animation.ObjectAnimator.ofObject(view, android.transition.ChangeBounds.POSITION_PROPERTY, null, topLeftPath);
                    }
                    final android.graphics.Rect finalClip = endClip;
                    if (startClip == null) {
                        startClip = new android.graphics.Rect(0, 0, startWidth, startHeight);
                    }
                    if (endClip == null) {
                        endClip = new android.graphics.Rect(0, 0, endWidth, endHeight);
                    }
                    android.animation.ObjectAnimator clipAnimator = null;
                    if (!startClip.equals(endClip)) {
                        view.setClipBounds(startClip);
                        clipAnimator = android.animation.ObjectAnimator.ofObject(view, "clipBounds", android.transition.ChangeBounds.sRectEvaluator, startClip, endClip);
                        clipAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                            private boolean mIsCanceled;

                            @java.lang.Override
                            public void onAnimationCancel(android.animation.Animator animation) {
                                mIsCanceled = true;
                            }

                            @java.lang.Override
                            public void onAnimationEnd(android.animation.Animator animation) {
                                if (!mIsCanceled) {
                                    view.setClipBounds(finalClip);
                                    view.setLeftTopRightBottom(endLeft, endTop, endRight, endBottom);
                                }
                            }
                        });
                    }
                    anim = android.transition.TransitionUtils.mergeAnimators(positionAnimator, clipAnimator);
                }
                return anim;
            }
        } else {
            sceneRoot.getLocationInWindow(tempLocation);
            int startX = ((java.lang.Integer) (startValues.values.get(android.transition.ChangeBounds.PROPNAME_WINDOW_X))) - tempLocation[0];
            int startY = ((java.lang.Integer) (startValues.values.get(android.transition.ChangeBounds.PROPNAME_WINDOW_Y))) - tempLocation[1];
            int endX = ((java.lang.Integer) (endValues.values.get(android.transition.ChangeBounds.PROPNAME_WINDOW_X))) - tempLocation[0];
            int endY = ((java.lang.Integer) (endValues.values.get(android.transition.ChangeBounds.PROPNAME_WINDOW_Y))) - tempLocation[1];
            // TODO: also handle size changes: check bounds and animate size changes
            if ((startX != endX) || (startY != endY)) {
                final int width = view.getWidth();
                final int height = view.getHeight();
                android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
                android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
                view.draw(canvas);
                final android.graphics.drawable.BitmapDrawable drawable = new android.graphics.drawable.BitmapDrawable(bitmap);
                drawable.setBounds(startX, startY, startX + width, startY + height);
                final float transitionAlpha = view.getTransitionAlpha();
                view.setTransitionAlpha(0);
                sceneRoot.getOverlay().add(drawable);
                android.graphics.Path topLeftPath = getPathMotion().getPath(startX, startY, endX, endY);
                android.animation.PropertyValuesHolder origin = android.animation.PropertyValuesHolder.ofObject(android.transition.ChangeBounds.DRAWABLE_ORIGIN_PROPERTY, null, topLeftPath);
                android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofPropertyValuesHolder(drawable, origin);
                anim.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        sceneRoot.getOverlay().remove(drawable);
                        view.setTransitionAlpha(transitionAlpha);
                    }
                });
                return anim;
            }
        }
        return null;
    }

    private static class ViewBounds {
        private int mLeft;

        private int mTop;

        private int mRight;

        private int mBottom;

        private android.view.View mView;

        private int mTopLeftCalls;

        private int mBottomRightCalls;

        public ViewBounds(android.view.View view) {
            mView = view;
        }

        public void setTopLeft(android.graphics.PointF topLeft) {
            mLeft = java.lang.Math.round(topLeft.x);
            mTop = java.lang.Math.round(topLeft.y);
            mTopLeftCalls++;
            if (mTopLeftCalls == mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }

        public void setBottomRight(android.graphics.PointF bottomRight) {
            mRight = java.lang.Math.round(bottomRight.x);
            mBottom = java.lang.Math.round(bottomRight.y);
            mBottomRightCalls++;
            if (mTopLeftCalls == mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }

        private void setLeftTopRightBottom() {
            mView.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
            mTopLeftCalls = 0;
            mBottomRightCalls = 0;
        }
    }
}

