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
package android.view;


/**
 * This view draws another View in an Overlay without changing the parent. It will not be drawn
 * by its parent because its visibility is set to INVISIBLE, but will be drawn
 * here using its render node. When the GhostView is set to INVISIBLE, the View it is
 * shadowing will become VISIBLE and when the GhostView becomes VISIBLE, the shadowed
 * view becomes INVISIBLE.
 *
 * @unknown 
 */
public class GhostView extends android.view.View {
    private final android.view.View mView;

    private int mReferences;

    private boolean mBeingMoved;

    private GhostView(android.view.View view) {
        super(view.getContext());
        mView = view;
        mView.mGhostView = this;
        final android.view.ViewGroup parent = ((android.view.ViewGroup) (mView.getParent()));
        mView.setTransitionVisibility(android.view.View.INVISIBLE);
        parent.invalidate();
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        if (canvas instanceof android.graphics.RecordingCanvas) {
            android.graphics.RecordingCanvas dlCanvas = ((android.graphics.RecordingCanvas) (canvas));
            mView.mRecreateDisplayList = true;
            android.graphics.RenderNode renderNode = mView.updateDisplayListIfDirty();
            if (renderNode.hasDisplayList()) {
                dlCanvas.insertReorderBarrier();// enable shadow for this rendernode

                dlCanvas.drawRenderNode(renderNode);
                dlCanvas.insertInorderBarrier();// re-disable reordering/shadows

            }
        }
    }

    public void setMatrix(android.graphics.Matrix matrix) {
        mRenderNode.setAnimationMatrix(matrix);
    }

    @java.lang.Override
    public void setVisibility(@android.view.View.Visibility
    int visibility) {
        super.setVisibility(visibility);
        if (mView.mGhostView == this) {
            int inverseVisibility = (visibility == android.view.View.VISIBLE) ? android.view.View.INVISIBLE : android.view.View.VISIBLE;
            mView.setTransitionVisibility(inverseVisibility);
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!mBeingMoved) {
            mView.setTransitionVisibility(android.view.View.VISIBLE);
            mView.mGhostView = null;
            final android.view.ViewGroup parent = ((android.view.ViewGroup) (mView.getParent()));
            if (parent != null) {
                parent.invalidate();
            }
        }
    }

    public static void calculateMatrix(android.view.View view, android.view.ViewGroup host, android.graphics.Matrix matrix) {
        android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
        matrix.reset();
        parent.transformMatrixToGlobal(matrix);
        matrix.preTranslate(-parent.getScrollX(), -parent.getScrollY());
        host.transformMatrixToLocal(matrix);
    }

    @android.annotation.UnsupportedAppUsage
    public static android.view.GhostView addGhost(android.view.View view, android.view.ViewGroup viewGroup, android.graphics.Matrix matrix) {
        if (!(view.getParent() instanceof android.view.ViewGroup)) {
            throw new java.lang.IllegalArgumentException("Ghosted views must be parented by a ViewGroup");
        }
        android.view.ViewGroupOverlay overlay = viewGroup.getOverlay();
        android.view.ViewOverlay.OverlayViewGroup overlayViewGroup = overlay.mOverlayViewGroup;
        android.view.GhostView ghostView = view.mGhostView;
        int previousRefCount = 0;
        if (ghostView != null) {
            android.view.View oldParent = ((android.view.View) (ghostView.getParent()));
            android.view.ViewGroup oldGrandParent = ((android.view.ViewGroup) (oldParent.getParent()));
            if (oldGrandParent != overlayViewGroup) {
                previousRefCount = ghostView.mReferences;
                oldGrandParent.removeView(oldParent);
                ghostView = null;
            }
        }
        if (ghostView == null) {
            if (matrix == null) {
                matrix = new android.graphics.Matrix();
                android.view.GhostView.calculateMatrix(view, viewGroup, matrix);
            }
            ghostView = new android.view.GhostView(view);
            ghostView.setMatrix(matrix);
            android.widget.FrameLayout parent = new android.widget.FrameLayout(view.getContext());
            parent.setClipChildren(false);
            android.view.GhostView.copySize(viewGroup, parent);
            android.view.GhostView.copySize(viewGroup, ghostView);
            parent.addView(ghostView);
            java.util.ArrayList<android.view.View> tempViews = new java.util.ArrayList<android.view.View>();
            int firstGhost = android.view.GhostView.moveGhostViewsToTop(overlay.mOverlayViewGroup, tempViews);
            android.view.GhostView.insertIntoOverlay(overlay.mOverlayViewGroup, parent, ghostView, tempViews, firstGhost);
            ghostView.mReferences = previousRefCount;
        } else
            if (matrix != null) {
                ghostView.setMatrix(matrix);
            }

        ghostView.mReferences++;
        return ghostView;
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    public static android.view.GhostView addGhost(android.view.View view, android.view.ViewGroup viewGroup) {
        return android.view.GhostView.addGhost(view, viewGroup, null);
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    public static void removeGhost(android.view.View view) {
        android.view.GhostView ghostView = view.mGhostView;
        if (ghostView != null) {
            ghostView.mReferences--;
            if (ghostView.mReferences == 0) {
                android.view.ViewGroup parent = ((android.view.ViewGroup) (ghostView.getParent()));
                android.view.ViewGroup grandParent = ((android.view.ViewGroup) (parent.getParent()));
                grandParent.removeView(parent);
            }
        }
    }

    public static android.view.GhostView getGhost(android.view.View view) {
        return view.mGhostView;
    }

    private static void copySize(android.view.View from, android.view.View to) {
        to.setLeft(0);
        to.setTop(0);
        to.setRight(from.getWidth());
        to.setBottom(from.getHeight());
    }

    /**
     * Move the GhostViews to the end so that they are on top of other views and it is easier
     * to do binary search for the correct location for the GhostViews in insertIntoOverlay.
     *
     * @return The index of the first GhostView or -1 if no GhostView is in the ViewGroup
     */
    private static int moveGhostViewsToTop(android.view.ViewGroup viewGroup, java.util.ArrayList<android.view.View> tempViews) {
        final int numChildren = viewGroup.getChildCount();
        if (numChildren == 0) {
            return -1;
        } else
            if (android.view.GhostView.isGhostWrapper(viewGroup.getChildAt(numChildren - 1))) {
                // GhostViews are already at the end
                int firstGhost = numChildren - 1;
                for (int i = numChildren - 2; i >= 0; i--) {
                    if (!android.view.GhostView.isGhostWrapper(viewGroup.getChildAt(i))) {
                        break;
                    }
                    firstGhost = i;
                }
                return firstGhost;
            }

        // Remove all GhostViews from the middle
        for (int i = numChildren - 2; i >= 0; i--) {
            android.view.View child = viewGroup.getChildAt(i);
            if (android.view.GhostView.isGhostWrapper(child)) {
                tempViews.add(child);
                android.view.GhostView ghostView = ((android.view.GhostView) (((android.view.ViewGroup) (child)).getChildAt(0)));
                ghostView.mBeingMoved = true;
                viewGroup.removeViewAt(i);
                ghostView.mBeingMoved = false;
            }
        }
        final int firstGhost;
        if (tempViews.isEmpty()) {
            firstGhost = -1;
        } else {
            firstGhost = viewGroup.getChildCount();
            // Add the GhostViews to the end
            for (int i = tempViews.size() - 1; i >= 0; i--) {
                viewGroup.addView(tempViews.get(i));
            }
            tempViews.clear();
        }
        return firstGhost;
    }

    /**
     * Inserts a GhostView into the overlay's ViewGroup in the order in which they
     * should be displayed by the UI.
     */
    private static void insertIntoOverlay(android.view.ViewGroup viewGroup, android.view.ViewGroup wrapper, android.view.GhostView ghostView, java.util.ArrayList<android.view.View> tempParents, int firstGhost) {
        if (firstGhost == (-1)) {
            viewGroup.addView(wrapper);
        } else {
            java.util.ArrayList<android.view.View> viewParents = new java.util.ArrayList<android.view.View>();
            android.view.GhostView.getParents(ghostView.mView, viewParents);
            int index = android.view.GhostView.getInsertIndex(viewGroup, viewParents, tempParents, firstGhost);
            if ((index < 0) || (index >= viewGroup.getChildCount())) {
                viewGroup.addView(wrapper);
            } else {
                viewGroup.addView(wrapper, index);
            }
        }
    }

    /**
     * Find the index into the overlay to insert the GhostView based on the order that the
     * views should be drawn. This keeps GhostViews layered in the same order
     * that they are ordered in the UI.
     */
    private static int getInsertIndex(android.view.ViewGroup overlayViewGroup, java.util.ArrayList<android.view.View> viewParents, java.util.ArrayList<android.view.View> tempParents, int firstGhost) {
        int low = firstGhost;
        int high = overlayViewGroup.getChildCount() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            android.view.ViewGroup wrapper = ((android.view.ViewGroup) (overlayViewGroup.getChildAt(mid)));
            android.view.GhostView midView = ((android.view.GhostView) (wrapper.getChildAt(0)));
            android.view.GhostView.getParents(midView.mView, tempParents);
            if (android.view.GhostView.isOnTop(viewParents, tempParents)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
            tempParents.clear();
        } 
        return low;
    }

    /**
     * Returns true if view is a GhostView's FrameLayout wrapper.
     */
    private static boolean isGhostWrapper(android.view.View view) {
        if (view instanceof android.widget.FrameLayout) {
            android.widget.FrameLayout frameLayout = ((android.widget.FrameLayout) (view));
            if (frameLayout.getChildCount() == 1) {
                android.view.View child = frameLayout.getChildAt(0);
                return child instanceof android.view.GhostView;
            }
        }
        return false;
    }

    /**
     * Returns true if viewParents is from a View that is on top of the comparedWith's view.
     * The ArrayLists contain the ancestors of views in order from top most grandparent, to
     * the view itself, in order. The goal is to find the first matching parent and then
     * compare the draw order of the siblings.
     */
    private static boolean isOnTop(java.util.ArrayList<android.view.View> viewParents, java.util.ArrayList<android.view.View> comparedWith) {
        if ((viewParents.isEmpty() || comparedWith.isEmpty()) || (viewParents.get(0) != comparedWith.get(0))) {
            // Not the same decorView -- arbitrary ordering
            return true;
        }
        int depth = java.lang.Math.min(viewParents.size(), comparedWith.size());
        for (int i = 1; i < depth; i++) {
            android.view.View viewParent = viewParents.get(i);
            android.view.View comparedWithParent = comparedWith.get(i);
            if (viewParent != comparedWithParent) {
                // i - 1 is the same parent, but these are different children.
                return android.view.GhostView.isOnTop(viewParent, comparedWithParent);
            }
        }
        // one of these is the parent of the other
        boolean isComparedWithTheParent = comparedWith.size() == depth;
        return isComparedWithTheParent;
    }

    /**
     * Adds all the parents, grandparents, etc. of view to parents.
     */
    private static void getParents(android.view.View view, java.util.ArrayList<android.view.View> parents) {
        android.view.ViewParent parent = view.getParent();
        if ((parent != null) && (parent instanceof android.view.ViewGroup)) {
            android.view.GhostView.getParents(((android.view.View) (parent)), parents);
        }
        parents.add(view);
    }

    /**
     * Returns true if view would be drawn on top of comparedWith or false otherwise.
     * view and comparedWith are siblings with the same parent. This uses the logic
     * that dispatchDraw uses to determine which View should be drawn first.
     */
    private static boolean isOnTop(android.view.View view, android.view.View comparedWith) {
        android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
        final int childrenCount = parent.getChildCount();
        final java.util.ArrayList<android.view.View> preorderedList = parent.buildOrderedChildList();
        final boolean customOrder = (preorderedList == null) && parent.isChildrenDrawingOrderEnabled();
        // This default value shouldn't be used because both view and comparedWith
        // should be in the list. If there is an error, then just return an arbitrary
        // view is on top.
        boolean isOnTop = true;
        for (int i = 0; i < childrenCount; i++) {
            int childIndex = (customOrder) ? parent.getChildDrawingOrder(childrenCount, i) : i;
            final android.view.View child = (preorderedList == null) ? parent.getChildAt(childIndex) : preorderedList.get(childIndex);
            if (child == view) {
                isOnTop = false;
                break;
            } else
                if (child == comparedWith) {
                    isOnTop = true;
                    break;
                }

        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        return isOnTop;
    }
}

