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


class ViewOverlay {
    /**
     * The actual container for the drawables (and views, if it's a ViewGroupOverlay).
     * All of the management and rendering details for the overlay are handled in
     * OverlayViewGroup.
     */
    protected android.support.transition.ViewOverlay.OverlayViewGroup mOverlayViewGroup;

    ViewOverlay(android.content.Context context, android.view.ViewGroup hostView, android.view.View requestingView) {
        mOverlayViewGroup = new android.support.transition.ViewOverlay.OverlayViewGroup(context, hostView, requestingView, this);
    }

    static android.view.ViewGroup getContentView(android.view.View view) {
        android.view.View parent = view;
        while (parent != null) {
            if ((parent.getId() == R.id.content) && (parent instanceof android.view.ViewGroup)) {
                return ((android.view.ViewGroup) (parent));
            }
            if (parent.getParent() instanceof android.view.ViewGroup) {
                parent = ((android.view.ViewGroup) (parent.getParent()));
            }
        } 
        return null;
    }

    public static android.support.transition.ViewOverlay createFrom(android.view.View view) {
        android.view.ViewGroup contentView = android.support.transition.ViewOverlay.getContentView(view);
        if (contentView != null) {
            final int numChildren = contentView.getChildCount();
            for (int i = 0; i < numChildren; ++i) {
                android.view.View child = contentView.getChildAt(i);
                if (child instanceof android.support.transition.ViewOverlay.OverlayViewGroup) {
                    return ((android.support.transition.ViewOverlay.OverlayViewGroup) (child)).mViewOverlay;
                }
            }
            return new android.support.transition.ViewGroupOverlay(contentView.getContext(), contentView, view);
        }
        return null;
    }

    /**
     * Used internally by View and ViewGroup to handle drawing and invalidation
     * of the overlay
     */
    android.view.ViewGroup getOverlayView() {
        return mOverlayViewGroup;
    }

    /**
     * Adds a Drawable to the overlay. The bounds of the drawable should be relative to
     * the host view. Any drawable added to the overlay should be removed when it is no longer
     * needed or no longer visible.
     *
     * @param drawable
     * 		The Drawable to be added to the overlay. This drawable will be
     * 		drawn when the view redraws its overlay.
     * @see #remove(Drawable)
     */
    public void add(android.graphics.drawable.Drawable drawable) {
        mOverlayViewGroup.add(drawable);
    }

    /**
     * Removes the specified Drawable from the overlay.
     *
     * @param drawable
     * 		The Drawable to be removed from the overlay.
     * @see #add(Drawable)
     */
    public void remove(android.graphics.drawable.Drawable drawable) {
        mOverlayViewGroup.remove(drawable);
    }

    /**
     * Removes all content from the overlay.
     */
    public void clear() {
        mOverlayViewGroup.clear();
    }

    boolean isEmpty() {
        return mOverlayViewGroup.isEmpty();
    }

    /**
     * OverlayViewGroup is a container that View and ViewGroup use to host
     * drawables and views added to their overlays  ({@link ViewOverlay} and
     * {@link ViewGroupOverlay}, respectively). Drawables are added to the overlay
     * via the add/remove methods in ViewOverlay, Views are added/removed via
     * ViewGroupOverlay. These drawable and view objects are
     * drawn whenever the view itself is drawn; first the view draws its own
     * content (and children, if it is a ViewGroup), then it draws its overlay
     * (if it has one).
     *
     * <p>Besides managing and drawing the list of drawables, this class serves
     * two purposes:
     * (1) it noops layout calls because children are absolutely positioned and
     * (2) it forwards all invalidation calls to its host view. The invalidation
     * redirect is necessary because the overlay is not a child of the host view
     * and invalidation cannot therefore follow the normal path up through the
     * parent hierarchy.</p>
     *
     * @see View#getOverlay()
     * @see ViewGroup#getOverlay()
     */
    static class OverlayViewGroup extends android.view.ViewGroup {
        static java.lang.reflect.Method sInvalidateChildInParentFastMethod;

        static {
            try {
                android.support.transition.ViewOverlay.OverlayViewGroup.sInvalidateChildInParentFastMethod = android.view.ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", int.class, int.class, android.graphics.Rect.class);
            } catch (java.lang.NoSuchMethodException e) {
            }
        }

        /**
         * The View for which this is an overlay. Invalidations of the overlay are redirected to
         * this host view.
         */
        android.view.ViewGroup mHostView;

        android.view.View mRequestingView;

        /**
         * The set of drawables to draw when the overlay is rendered.
         */
        java.util.ArrayList<android.graphics.drawable.Drawable> mDrawables = null;

        /**
         * Reference to the hosting overlay object
         */
        android.support.transition.ViewOverlay mViewOverlay;

        OverlayViewGroup(android.content.Context context, android.view.ViewGroup hostView, android.view.View requestingView, android.support.transition.ViewOverlay viewOverlay) {
            super(context);
            mHostView = hostView;
            mRequestingView = requestingView;
            setRight(hostView.getWidth());
            setBottom(hostView.getHeight());
            ((android.view.ViewGroup) (hostView)).addView(this);
            mViewOverlay = viewOverlay;
        }

        @java.lang.Override
        public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
            // Intercept and noop all touch events - overlays do not allow touch events
            return false;
        }

        public void add(android.graphics.drawable.Drawable drawable) {
            if (mDrawables == null) {
                mDrawables = new java.util.ArrayList<android.graphics.drawable.Drawable>();
            }
            if (!mDrawables.contains(drawable)) {
                // Make each drawable unique in the overlay; can't add it more than once
                mDrawables.add(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback(this);
            }
        }

        public void remove(android.graphics.drawable.Drawable drawable) {
            if (mDrawables != null) {
                mDrawables.remove(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback(null);
            }
        }

        @java.lang.Override
        protected boolean verifyDrawable(android.graphics.drawable.Drawable who) {
            return super.verifyDrawable(who) || ((mDrawables != null) && mDrawables.contains(who));
        }

        public void add(android.view.View child) {
            if (child.getParent() instanceof android.view.ViewGroup) {
                android.view.ViewGroup parent = ((android.view.ViewGroup) (child.getParent()));
                if ((parent != mHostView) && (parent.getParent() != null)) {
                    // &&
                    // parent.isAttachedToWindow()) {
                    // Moving to different container; figure out how to position child such that
                    // it is in the same location on the screen
                    int[] parentLocation = new int[2];
                    int[] hostViewLocation = new int[2];
                    parent.getLocationOnScreen(parentLocation);
                    mHostView.getLocationOnScreen(hostViewLocation);
                    android.support.v4.view.ViewCompat.offsetLeftAndRight(child, parentLocation[0] - hostViewLocation[0]);
                    android.support.v4.view.ViewCompat.offsetTopAndBottom(child, parentLocation[1] - hostViewLocation[1]);
                }
                parent.removeView(child);
                // if (parent.getLayoutTransition() != null) {
                // // LayoutTransition will cause the child to delay removal - cancel it
                // parent.getLayoutTransition().cancel(LayoutTransition.DISAPPEARING);
                // }
                // fail-safe if view is still attached for any reason
                if (child.getParent() != null) {
                    parent.removeView(child);
                }
            }
            super.addView(child, getChildCount() - 1);
        }

        public void remove(android.view.View view) {
            super.removeView(view);
            if (isEmpty()) {
                mHostView.removeView(this);
            }
        }

        public void clear() {
            removeAllViews();
            if (mDrawables != null) {
                mDrawables.clear();
            }
        }

        boolean isEmpty() {
            if ((getChildCount() == 0) && ((mDrawables == null) || (mDrawables.size() == 0))) {
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void invalidateDrawable(android.graphics.drawable.Drawable drawable) {
            invalidate(drawable.getBounds());
        }

        @java.lang.Override
        protected void dispatchDraw(android.graphics.Canvas canvas) {
            int[] contentViewLocation = new int[2];
            int[] hostViewLocation = new int[2];
            android.view.ViewGroup parent = ((android.view.ViewGroup) (getParent()));
            mHostView.getLocationOnScreen(contentViewLocation);
            mRequestingView.getLocationOnScreen(hostViewLocation);
            canvas.translate(hostViewLocation[0] - contentViewLocation[0], hostViewLocation[1] - contentViewLocation[1]);
            canvas.clipRect(new android.graphics.Rect(0, 0, mRequestingView.getWidth(), mRequestingView.getHeight()));
            super.dispatchDraw(canvas);
            final int numDrawables = (mDrawables == null) ? 0 : mDrawables.size();
            for (int i = 0; i < numDrawables; ++i) {
                mDrawables.get(i).draw(canvas);
            }
        }

        @java.lang.Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            // Noop: children are positioned absolutely
        }

        /* The following invalidation overrides exist for the purpose of redirecting invalidation to
        the host view. The overlay is not parented to the host view (since a View cannot be a
        parent), so the invalidation cannot proceed through the normal parent hierarchy.
        There is a built-in assumption that the overlay exactly covers the host view, therefore
        the invalidation rectangles received do not need to be adjusted when forwarded to
        the host view.
         */
        private void getOffset(int[] offset) {
            int[] contentViewLocation = new int[2];
            int[] hostViewLocation = new int[2];
            android.view.ViewGroup parent = ((android.view.ViewGroup) (getParent()));
            mHostView.getLocationOnScreen(contentViewLocation);
            mRequestingView.getLocationOnScreen(hostViewLocation);
            offset[0] = hostViewLocation[0] - contentViewLocation[0];
            offset[1] = hostViewLocation[1] - contentViewLocation[1];
        }

        public void invalidateChildFast(android.view.View child, final android.graphics.Rect dirty) {
            if (mHostView != null) {
                // Note: This is not a "fast" invalidation. Would be nice to instead invalidate
                // using DisplayList properties and a dirty rect instead of causing a real
                // invalidation of the host view
                int left = child.getLeft();
                int top = child.getTop();
                int[] offset = new int[2];
                getOffset(offset);
                // TODO: implement transforms
                // if (!child.getMatrix().isIdentity()) {
                // child.transformRect(dirty);
                // }
                dirty.offset(left + offset[0], top + offset[1]);
                mHostView.invalidate(dirty);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        protected android.view.ViewParent invalidateChildInParentFast(int left, int top, android.graphics.Rect dirty) {
            if ((mHostView instanceof android.view.ViewGroup) && (android.support.transition.ViewOverlay.OverlayViewGroup.sInvalidateChildInParentFastMethod != null)) {
                try {
                    int[] offset = new int[2];
                    getOffset(offset);
                    android.support.transition.ViewOverlay.OverlayViewGroup.sInvalidateChildInParentFastMethod.invoke(mHostView, left, top, dirty);
                } catch (java.lang.IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.reflect.InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @java.lang.Override
        public android.view.ViewParent invalidateChildInParent(int[] location, android.graphics.Rect dirty) {
            if (mHostView != null) {
                dirty.offset(location[0], location[1]);
                if (mHostView instanceof android.view.ViewGroup) {
                    location[0] = 0;
                    location[1] = 0;
                    int[] offset = new int[2];
                    getOffset(offset);
                    dirty.offset(offset[0], offset[1]);
                    return super.invalidateChildInParent(location, dirty);
                    // return ((ViewGroup) mHostView).invalidateChildInParent(location, dirty);
                } else {
                    invalidate(dirty);
                }
            }
            return null;
        }

        static class TouchInterceptor extends android.view.View {
            TouchInterceptor(android.content.Context context) {
                super(context);
            }
        }
    }
}

