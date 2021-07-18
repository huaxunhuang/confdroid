/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * The algorithm used for finding the next focusable view in a given direction
 * from a view that currently has focus.
 */
public class FocusFinder {
    private static final java.lang.ThreadLocal<android.view.FocusFinder> tlFocusFinder = new java.lang.ThreadLocal<android.view.FocusFinder>() {
        @java.lang.Override
        protected android.view.FocusFinder initialValue() {
            return new android.view.FocusFinder();
        }
    };

    /**
     * Get the focus finder for this thread.
     */
    public static android.view.FocusFinder getInstance() {
        return android.view.FocusFinder.tlFocusFinder.get();
    }

    final android.graphics.Rect mFocusedRect = new android.graphics.Rect();

    final android.graphics.Rect mOtherRect = new android.graphics.Rect();

    final android.graphics.Rect mBestCandidateRect = new android.graphics.Rect();

    private final android.view.FocusFinder.UserSpecifiedFocusComparator mUserSpecifiedFocusComparator = new android.view.FocusFinder.UserSpecifiedFocusComparator(( r, v) -> android.view.FocusFinder.isValidId(v.getNextFocusForwardId()) ? v.findUserSetNextFocus(r, android.view.View.FOCUS_FORWARD) : null);

    private final android.view.FocusFinder.UserSpecifiedFocusComparator mUserSpecifiedClusterComparator = new android.view.FocusFinder.UserSpecifiedFocusComparator(( r, v) -> android.view.FocusFinder.isValidId(v.getNextClusterForwardId()) ? v.findUserSetNextKeyboardNavigationCluster(r, android.view.View.FOCUS_FORWARD) : null);

    private final android.view.FocusFinder.FocusSorter mFocusSorter = new android.view.FocusFinder.FocusSorter();

    private final java.util.ArrayList<android.view.View> mTempList = new java.util.ArrayList<android.view.View>();

    // enforce thread local access
    private FocusFinder() {
    }

    /**
     * Find the next view to take focus in root's descendants, starting from the view
     * that currently is focused.
     *
     * @param root
     * 		Contains focused. Cannot be null.
     * @param focused
     * 		Has focus now.
     * @param direction
     * 		Direction to look.
     * @return The next focusable view, or null if none exists.
     */
    public final android.view.View findNextFocus(android.view.ViewGroup root, android.view.View focused, int direction) {
        return findNextFocus(root, focused, null, direction);
    }

    /**
     * Find the next view to take focus in root's descendants, searching from
     * a particular rectangle in root's coordinates.
     *
     * @param root
     * 		Contains focusedRect. Cannot be null.
     * @param focusedRect
     * 		The starting point of the search.
     * @param direction
     * 		Direction to look.
     * @return The next focusable view, or null if none exists.
     */
    public android.view.View findNextFocusFromRect(android.view.ViewGroup root, android.graphics.Rect focusedRect, int direction) {
        mFocusedRect.set(focusedRect);
        return findNextFocus(root, null, mFocusedRect, direction);
    }

    private android.view.View findNextFocus(android.view.ViewGroup root, android.view.View focused, android.graphics.Rect focusedRect, int direction) {
        android.view.View next = null;
        android.view.ViewGroup effectiveRoot = getEffectiveRoot(root, focused);
        if (focused != null) {
            next = findNextUserSpecifiedFocus(effectiveRoot, focused, direction);
        }
        if (next != null) {
            return next;
        }
        java.util.ArrayList<android.view.View> focusables = mTempList;
        try {
            focusables.clear();
            effectiveRoot.addFocusables(focusables, direction);
            if (!focusables.isEmpty()) {
                next = findNextFocus(effectiveRoot, focused, focusedRect, direction, focusables);
            }
        } finally {
            focusables.clear();
        }
        return next;
    }

    /**
     * Returns the "effective" root of a view. The "effective" root is the closest ancestor
     * within-which focus should cycle.
     * <p>
     * For example: normal focus navigation would stay within a ViewGroup marked as
     * touchscreenBlocksFocus and keyboardNavigationCluster until a cluster-jump out.
     *
     * @return the "effective" root of {@param focused}
     */
    private android.view.ViewGroup getEffectiveRoot(android.view.ViewGroup root, android.view.View focused) {
        if ((focused == null) || (focused == root)) {
            return root;
        }
        android.view.ViewGroup effective = null;
        android.view.ViewParent nextParent = focused.getParent();
        do {
            if (nextParent == root) {
                return effective != null ? effective : root;
            }
            android.view.ViewGroup vg = ((android.view.ViewGroup) (nextParent));
            if ((vg.getTouchscreenBlocksFocus() && focused.getContext().getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_TOUCHSCREEN)) && vg.isKeyboardNavigationCluster()) {
                // Don't stop and return here because the cluster could be nested and we only
                // care about the top-most one.
                effective = vg;
            }
            nextParent = nextParent.getParent();
        } while (nextParent instanceof android.view.ViewGroup );
        return root;
    }

    /**
     * Find the root of the next keyboard navigation cluster after the current one.
     *
     * @param root
     * 		The view tree to look inside. Cannot be null
     * @param currentCluster
     * 		The starting point of the search. Null means the default cluster
     * @param direction
     * 		Direction to look
     * @return The next cluster, or null if none exists
     */
    public android.view.View findNextKeyboardNavigationCluster(@android.annotation.NonNull
    android.view.View root, @android.annotation.Nullable
    android.view.View currentCluster, @android.view.View.FocusDirection
    int direction) {
        android.view.View next = null;
        if (currentCluster != null) {
            next = findNextUserSpecifiedKeyboardNavigationCluster(root, currentCluster, direction);
            if (next != null) {
                return next;
            }
        }
        final java.util.ArrayList<android.view.View> clusters = mTempList;
        try {
            clusters.clear();
            root.addKeyboardNavigationClusters(clusters, direction);
            if (!clusters.isEmpty()) {
                next = findNextKeyboardNavigationCluster(root, currentCluster, clusters, direction);
            }
        } finally {
            clusters.clear();
        }
        return next;
    }

    private android.view.View findNextUserSpecifiedKeyboardNavigationCluster(android.view.View root, android.view.View currentCluster, int direction) {
        android.view.View userSetNextCluster = currentCluster.findUserSetNextKeyboardNavigationCluster(root, direction);
        if ((userSetNextCluster != null) && userSetNextCluster.hasFocusable()) {
            return userSetNextCluster;
        }
        return null;
    }

    private android.view.View findNextUserSpecifiedFocus(android.view.ViewGroup root, android.view.View focused, int direction) {
        // check for user specified next focus
        android.view.View userSetNextFocus = focused.findUserSetNextFocus(root, direction);
        android.view.View cycleCheck = userSetNextFocus;
        boolean cycleStep = true;// we want the first toggle to yield false

        while (userSetNextFocus != null) {
            if ((userSetNextFocus.isFocusable() && (userSetNextFocus.getVisibility() == android.view.View.VISIBLE)) && ((!userSetNextFocus.isInTouchMode()) || userSetNextFocus.isFocusableInTouchMode())) {
                return userSetNextFocus;
            }
            userSetNextFocus = userSetNextFocus.findUserSetNextFocus(root, direction);
            if (cycleStep = !cycleStep) {
                cycleCheck = cycleCheck.findUserSetNextFocus(root, direction);
                if (cycleCheck == userSetNextFocus) {
                    // found a cycle, user-specified focus forms a loop and none of the views
                    // are currently focusable.
                    break;
                }
            }
        } 
        return null;
    }

    private android.view.View findNextFocus(android.view.ViewGroup root, android.view.View focused, android.graphics.Rect focusedRect, int direction, java.util.ArrayList<android.view.View> focusables) {
        if (focused != null) {
            if (focusedRect == null) {
                focusedRect = mFocusedRect;
            }
            // fill in interesting rect from focused
            focused.getFocusedRect(focusedRect);
            root.offsetDescendantRectToMyCoords(focused, focusedRect);
        } else {
            if (focusedRect == null) {
                focusedRect = mFocusedRect;
                // make up a rect at top left or bottom right of root
                switch (direction) {
                    case android.view.View.FOCUS_RIGHT :
                    case android.view.View.FOCUS_DOWN :
                        setFocusTopLeft(root, focusedRect);
                        break;
                    case android.view.View.FOCUS_FORWARD :
                        if (root.isLayoutRtl()) {
                            setFocusBottomRight(root, focusedRect);
                        } else {
                            setFocusTopLeft(root, focusedRect);
                        }
                        break;
                    case android.view.View.FOCUS_LEFT :
                    case android.view.View.FOCUS_UP :
                        setFocusBottomRight(root, focusedRect);
                        break;
                    case android.view.View.FOCUS_BACKWARD :
                        if (root.isLayoutRtl()) {
                            setFocusTopLeft(root, focusedRect);
                        } else {
                            setFocusBottomRight(root, focusedRect);
                            break;
                        }
                }
            }
        }
        switch (direction) {
            case android.view.View.FOCUS_FORWARD :
            case android.view.View.FOCUS_BACKWARD :
                return findNextFocusInRelativeDirection(focusables, root, focused, focusedRect, direction);
            case android.view.View.FOCUS_UP :
            case android.view.View.FOCUS_DOWN :
            case android.view.View.FOCUS_LEFT :
            case android.view.View.FOCUS_RIGHT :
                return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect, direction);
            default :
                throw new java.lang.IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private android.view.View findNextKeyboardNavigationCluster(android.view.View root, android.view.View currentCluster, java.util.List<android.view.View> clusters, @android.view.View.FocusDirection
    int direction) {
        try {
            // Note: This sort is stable.
            mUserSpecifiedClusterComparator.setFocusables(clusters, root);
            java.util.Collections.sort(clusters, mUserSpecifiedClusterComparator);
        } finally {
            mUserSpecifiedClusterComparator.recycle();
        }
        final int count = clusters.size();
        switch (direction) {
            case android.view.View.FOCUS_FORWARD :
            case android.view.View.FOCUS_DOWN :
            case android.view.View.FOCUS_RIGHT :
                return android.view.FocusFinder.getNextKeyboardNavigationCluster(root, currentCluster, clusters, count);
            case android.view.View.FOCUS_BACKWARD :
            case android.view.View.FOCUS_UP :
            case android.view.View.FOCUS_LEFT :
                return android.view.FocusFinder.getPreviousKeyboardNavigationCluster(root, currentCluster, clusters, count);
            default :
                throw new java.lang.IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private android.view.View findNextFocusInRelativeDirection(java.util.ArrayList<android.view.View> focusables, android.view.ViewGroup root, android.view.View focused, android.graphics.Rect focusedRect, int direction) {
        try {
            // Note: This sort is stable.
            mUserSpecifiedFocusComparator.setFocusables(focusables, root);
            java.util.Collections.sort(focusables, mUserSpecifiedFocusComparator);
        } finally {
            mUserSpecifiedFocusComparator.recycle();
        }
        final int count = focusables.size();
        switch (direction) {
            case android.view.View.FOCUS_FORWARD :
                return android.view.FocusFinder.getNextFocusable(focused, focusables, count);
            case android.view.View.FOCUS_BACKWARD :
                return android.view.FocusFinder.getPreviousFocusable(focused, focusables, count);
        }
        return focusables.get(count - 1);
    }

    private void setFocusBottomRight(android.view.ViewGroup root, android.graphics.Rect focusedRect) {
        final int rootBottom = root.getScrollY() + root.getHeight();
        final int rootRight = root.getScrollX() + root.getWidth();
        focusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
    }

    private void setFocusTopLeft(android.view.ViewGroup root, android.graphics.Rect focusedRect) {
        final int rootTop = root.getScrollY();
        final int rootLeft = root.getScrollX();
        focusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
    }

    android.view.View findNextFocusInAbsoluteDirection(java.util.ArrayList<android.view.View> focusables, android.view.ViewGroup root, android.view.View focused, android.graphics.Rect focusedRect, int direction) {
        // initialize the best candidate to something impossible
        // (so the first plausible view will become the best choice)
        mBestCandidateRect.set(focusedRect);
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
                mBestCandidateRect.offset(focusedRect.width() + 1, 0);
                break;
            case android.view.View.FOCUS_RIGHT :
                mBestCandidateRect.offset(-(focusedRect.width() + 1), 0);
                break;
            case android.view.View.FOCUS_UP :
                mBestCandidateRect.offset(0, focusedRect.height() + 1);
                break;
            case android.view.View.FOCUS_DOWN :
                mBestCandidateRect.offset(0, -(focusedRect.height() + 1));
        }
        android.view.View closest = null;
        int numFocusables = focusables.size();
        for (int i = 0; i < numFocusables; i++) {
            android.view.View focusable = focusables.get(i);
            // only interested in other non-root views
            if ((focusable == focused) || (focusable == root))
                continue;

            // get focus bounds of other view in same coordinate system
            focusable.getFocusedRect(mOtherRect);
            root.offsetDescendantRectToMyCoords(focusable, mOtherRect);
            if (isBetterCandidate(direction, focusedRect, mOtherRect, mBestCandidateRect)) {
                mBestCandidateRect.set(mOtherRect);
                closest = focusable;
            }
        }
        return closest;
    }

    private static android.view.View getNextFocusable(android.view.View focused, java.util.ArrayList<android.view.View> focusables, int count) {
        if (focused != null) {
            int position = focusables.lastIndexOf(focused);
            if ((position >= 0) && ((position + 1) < count)) {
                return focusables.get(position + 1);
            }
        }
        if (!focusables.isEmpty()) {
            return focusables.get(0);
        }
        return null;
    }

    private static android.view.View getPreviousFocusable(android.view.View focused, java.util.ArrayList<android.view.View> focusables, int count) {
        if (focused != null) {
            int position = focusables.indexOf(focused);
            if (position > 0) {
                return focusables.get(position - 1);
            }
        }
        if (!focusables.isEmpty()) {
            return focusables.get(count - 1);
        }
        return null;
    }

    private static android.view.View getNextKeyboardNavigationCluster(android.view.View root, android.view.View currentCluster, java.util.List<android.view.View> clusters, int count) {
        if (currentCluster == null) {
            // The current cluster is the default one.
            // The next cluster after the default one is the first one.
            // Note that the caller guarantees that 'clusters' is not empty.
            return clusters.get(0);
        }
        final int position = clusters.lastIndexOf(currentCluster);
        if ((position >= 0) && ((position + 1) < count)) {
            // Return the next non-default cluster if we can find it.
            return clusters.get(position + 1);
        }
        // The current cluster is the last one. The next one is the default one, i.e. the
        // root.
        return root;
    }

    private static android.view.View getPreviousKeyboardNavigationCluster(android.view.View root, android.view.View currentCluster, java.util.List<android.view.View> clusters, int count) {
        if (currentCluster == null) {
            // The current cluster is the default one.
            // The previous cluster before the default one is the last one.
            // Note that the caller guarantees that 'clusters' is not empty.
            return clusters.get(count - 1);
        }
        final int position = clusters.indexOf(currentCluster);
        if (position > 0) {
            // Return the previous non-default cluster if we can find it.
            return clusters.get(position - 1);
        }
        // The current cluster is the first one. The previous one is the default one, i.e.
        // the root.
        return root;
    }

    /**
     * Is rect1 a better candidate than rect2 for a focus search in a particular
     * direction from a source rect?  This is the core routine that determines
     * the order of focus searching.
     *
     * @param direction
     * 		the direction (up, down, left, right)
     * @param source
     * 		The source we are searching from
     * @param rect1
     * 		The candidate rectangle
     * @param rect2
     * 		The current best candidate.
     * @return Whether the candidate is the new best.
     */
    boolean isBetterCandidate(int direction, android.graphics.Rect source, android.graphics.Rect rect1, android.graphics.Rect rect2) {
        // to be a better candidate, need to at least be a candidate in the first
        // place :)
        if (!isCandidate(source, rect1, direction)) {
            return false;
        }
        // we know that rect1 is a candidate.. if rect2 is not a candidate,
        // rect1 is better
        if (!isCandidate(source, rect2, direction)) {
            return true;
        }
        // if rect1 is better by beam, it wins
        if (beamBeats(direction, source, rect1, rect2)) {
            return true;
        }
        // if rect2 is better, then rect1 cant' be :)
        if (beamBeats(direction, source, rect2, rect1)) {
            return false;
        }
        // otherwise, do fudge-tastic comparison of the major and minor axis
        return getWeightedDistanceFor(android.view.FocusFinder.majorAxisDistance(direction, source, rect1), android.view.FocusFinder.minorAxisDistance(direction, source, rect1)) < getWeightedDistanceFor(android.view.FocusFinder.majorAxisDistance(direction, source, rect2), android.view.FocusFinder.minorAxisDistance(direction, source, rect2));
    }

    /**
     * One rectangle may be another candidate than another by virtue of being
     * exclusively in the beam of the source rect.
     *
     * @return Whether rect1 is a better candidate than rect2 by virtue of it being in src's
    beam
     */
    boolean beamBeats(int direction, android.graphics.Rect source, android.graphics.Rect rect1, android.graphics.Rect rect2) {
        final boolean rect1InSrcBeam = beamsOverlap(direction, source, rect1);
        final boolean rect2InSrcBeam = beamsOverlap(direction, source, rect2);
        // if rect1 isn't exclusively in the src beam, it doesn't win
        if (rect2InSrcBeam || (!rect1InSrcBeam)) {
            return false;
        }
        // we know rect1 is in the beam, and rect2 is not
        // if rect1 is to the direction of, and rect2 is not, rect1 wins.
        // for example, for direction left, if rect1 is to the left of the source
        // and rect2 is below, then we always prefer the in beam rect1, since rect2
        // could be reached by going down.
        if (!isToDirectionOf(direction, source, rect2)) {
            return true;
        }
        // for horizontal directions, being exclusively in beam always wins
        if ((direction == android.view.View.FOCUS_LEFT) || (direction == android.view.View.FOCUS_RIGHT)) {
            return true;
        }
        // for vertical directions, beams only beat up to a point:
        // now, as long as rect2 isn't completely closer, rect1 wins
        // e.g for direction down, completely closer means for rect2's top
        // edge to be closer to the source's top edge than rect1's bottom edge.
        return android.view.FocusFinder.majorAxisDistance(direction, source, rect1) < android.view.FocusFinder.majorAxisDistanceToFarEdge(direction, source, rect2);
    }

    /**
     * Fudge-factor opportunity: how to calculate distance given major and minor
     * axis distances.  Warning: this fudge factor is finely tuned, be sure to
     * run all focus tests if you dare tweak it.
     */
    long getWeightedDistanceFor(long majorAxisDistance, long minorAxisDistance) {
        return ((13 * majorAxisDistance) * majorAxisDistance) + (minorAxisDistance * minorAxisDistance);
    }

    /**
     * Is destRect a candidate for the next focus given the direction?  This
     * checks whether the dest is at least partially to the direction of (e.g left of)
     * from source.
     *
     * Includes an edge case for an empty rect (which is used in some cases when
     * searching from a point on the screen).
     */
    boolean isCandidate(android.graphics.Rect srcRect, android.graphics.Rect destRect, int direction) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
                return ((srcRect.right > destRect.right) || (srcRect.left >= destRect.right)) && (srcRect.left > destRect.left);
            case android.view.View.FOCUS_RIGHT :
                return ((srcRect.left < destRect.left) || (srcRect.right <= destRect.left)) && (srcRect.right < destRect.right);
            case android.view.View.FOCUS_UP :
                return ((srcRect.bottom > destRect.bottom) || (srcRect.top >= destRect.bottom)) && (srcRect.top > destRect.top);
            case android.view.View.FOCUS_DOWN :
                return ((srcRect.top < destRect.top) || (srcRect.bottom <= destRect.top)) && (srcRect.bottom < destRect.bottom);
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    /**
     * Do the "beams" w.r.t the given direction's axis of rect1 and rect2 overlap?
     *
     * @param direction
     * 		the direction (up, down, left, right)
     * @param rect1
     * 		The first rectangle
     * @param rect2
     * 		The second rectangle
     * @return whether the beams overlap
     */
    boolean beamsOverlap(int direction, android.graphics.Rect rect1, android.graphics.Rect rect2) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
            case android.view.View.FOCUS_RIGHT :
                return (rect2.bottom > rect1.top) && (rect2.top < rect1.bottom);
            case android.view.View.FOCUS_UP :
            case android.view.View.FOCUS_DOWN :
                return (rect2.right > rect1.left) && (rect2.left < rect1.right);
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    /**
     * e.g for left, is 'to left of'
     */
    boolean isToDirectionOf(int direction, android.graphics.Rect src, android.graphics.Rect dest) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
                return src.left >= dest.right;
            case android.view.View.FOCUS_RIGHT :
                return src.right <= dest.left;
            case android.view.View.FOCUS_UP :
                return src.top >= dest.bottom;
            case android.view.View.FOCUS_DOWN :
                return src.bottom <= dest.top;
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    /**
     *
     *
     * @return The distance from the edge furthest in the given direction
    of source to the edge nearest in the given direction of dest.  If the
    dest is not in the direction from source, return 0.
     */
    static int majorAxisDistance(int direction, android.graphics.Rect source, android.graphics.Rect dest) {
        return java.lang.Math.max(0, android.view.FocusFinder.majorAxisDistanceRaw(direction, source, dest));
    }

    static int majorAxisDistanceRaw(int direction, android.graphics.Rect source, android.graphics.Rect dest) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
                return source.left - dest.right;
            case android.view.View.FOCUS_RIGHT :
                return dest.left - source.right;
            case android.view.View.FOCUS_UP :
                return source.top - dest.bottom;
            case android.view.View.FOCUS_DOWN :
                return dest.top - source.bottom;
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    /**
     *
     *
     * @return The distance along the major axis w.r.t the direction from the
    edge of source to the far edge of dest. If the
    dest is not in the direction from source, return 1 (to break ties with
    {@link #majorAxisDistance}).
     */
    static int majorAxisDistanceToFarEdge(int direction, android.graphics.Rect source, android.graphics.Rect dest) {
        return java.lang.Math.max(1, android.view.FocusFinder.majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static int majorAxisDistanceToFarEdgeRaw(int direction, android.graphics.Rect source, android.graphics.Rect dest) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
                return source.left - dest.left;
            case android.view.View.FOCUS_RIGHT :
                return dest.right - source.right;
            case android.view.View.FOCUS_UP :
                return source.top - dest.top;
            case android.view.View.FOCUS_DOWN :
                return dest.bottom - source.bottom;
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    /**
     * Find the distance on the minor axis w.r.t the direction to the nearest
     * edge of the destination rectangle.
     *
     * @param direction
     * 		the direction (up, down, left, right)
     * @param source
     * 		The source rect.
     * @param dest
     * 		The destination rect.
     * @return The distance.
     */
    static int minorAxisDistance(int direction, android.graphics.Rect source, android.graphics.Rect dest) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
            case android.view.View.FOCUS_RIGHT :
                // the distance between the center verticals
                return java.lang.Math.abs((source.top + (source.height() / 2)) - (dest.top + (dest.height() / 2)));
            case android.view.View.FOCUS_UP :
            case android.view.View.FOCUS_DOWN :
                // the distance between the center horizontals
                return java.lang.Math.abs((source.left + (source.width() / 2)) - (dest.left + (dest.width() / 2)));
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    /**
     * Find the nearest touchable view to the specified view.
     *
     * @param root
     * 		The root of the tree in which to search
     * @param x
     * 		X coordinate from which to start the search
     * @param y
     * 		Y coordinate from which to start the search
     * @param direction
     * 		Direction to look
     * @param deltas
     * 		Offset from the <x, y> to the edge of the nearest view. Note that this array
     * 		may already be populated with values.
     * @return The nearest touchable view, or null if none exists.
     */
    public android.view.View findNearestTouchable(android.view.ViewGroup root, int x, int y, int direction, int[] deltas) {
        java.util.ArrayList<android.view.View> touchables = root.getTouchables();
        int minDistance = java.lang.Integer.MAX_VALUE;
        android.view.View closest = null;
        int numTouchables = touchables.size();
        int edgeSlop = android.view.ViewConfiguration.get(root.mContext).getScaledEdgeSlop();
        android.graphics.Rect closestBounds = new android.graphics.Rect();
        android.graphics.Rect touchableBounds = mOtherRect;
        for (int i = 0; i < numTouchables; i++) {
            android.view.View touchable = touchables.get(i);
            // get visible bounds of other view in same coordinate system
            touchable.getDrawingRect(touchableBounds);
            root.offsetRectBetweenParentAndChild(touchable, touchableBounds, true, true);
            if (!isTouchCandidate(x, y, touchableBounds, direction)) {
                continue;
            }
            int distance = java.lang.Integer.MAX_VALUE;
            switch (direction) {
                case android.view.View.FOCUS_LEFT :
                    distance = (x - touchableBounds.right) + 1;
                    break;
                case android.view.View.FOCUS_RIGHT :
                    distance = touchableBounds.left;
                    break;
                case android.view.View.FOCUS_UP :
                    distance = (y - touchableBounds.bottom) + 1;
                    break;
                case android.view.View.FOCUS_DOWN :
                    distance = touchableBounds.top;
                    break;
            }
            if (distance < edgeSlop) {
                // Give preference to innermost views
                if (((closest == null) || closestBounds.contains(touchableBounds)) || ((!touchableBounds.contains(closestBounds)) && (distance < minDistance))) {
                    minDistance = distance;
                    closest = touchable;
                    closestBounds.set(touchableBounds);
                    switch (direction) {
                        case android.view.View.FOCUS_LEFT :
                            deltas[0] = -distance;
                            break;
                        case android.view.View.FOCUS_RIGHT :
                            deltas[0] = distance;
                            break;
                        case android.view.View.FOCUS_UP :
                            deltas[1] = -distance;
                            break;
                        case android.view.View.FOCUS_DOWN :
                            deltas[1] = distance;
                            break;
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Is destRect a candidate for the next touch given the direction?
     */
    private boolean isTouchCandidate(int x, int y, android.graphics.Rect destRect, int direction) {
        switch (direction) {
            case android.view.View.FOCUS_LEFT :
                return ((destRect.left <= x) && (destRect.top <= y)) && (y <= destRect.bottom);
            case android.view.View.FOCUS_RIGHT :
                return ((destRect.left >= x) && (destRect.top <= y)) && (y <= destRect.bottom);
            case android.view.View.FOCUS_UP :
                return ((destRect.top <= y) && (destRect.left <= x)) && (x <= destRect.right);
            case android.view.View.FOCUS_DOWN :
                return ((destRect.top >= y) && (destRect.left <= x)) && (x <= destRect.right);
        }
        throw new java.lang.IllegalArgumentException("direction must be one of " + "{FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    private static final boolean isValidId(final int id) {
        return (id != 0) && (id != android.view.View.NO_ID);
    }

    static final class FocusSorter {
        private java.util.ArrayList<android.graphics.Rect> mRectPool = new java.util.ArrayList<>();

        private int mLastPoolRect;

        private int mRtlMult;

        private java.util.HashMap<android.view.View, android.graphics.Rect> mRectByView = null;

        private java.util.Comparator<android.view.View> mTopsComparator = ( first, second) -> {
            if (first == second) {
                return 0;
            }
            android.graphics.Rect firstRect = mRectByView.get(first);
            android.graphics.Rect secondRect = mRectByView.get(second);
            int result = firstRect.top - secondRect.top;
            if (result == 0) {
                return firstRect.bottom - secondRect.bottom;
            }
            return result;
        };

        private java.util.Comparator<android.view.View> mSidesComparator = ( first, second) -> {
            if (first == second) {
                return 0;
            }
            android.graphics.Rect firstRect = mRectByView.get(first);
            android.graphics.Rect secondRect = mRectByView.get(second);
            int result = firstRect.left - secondRect.left;
            if (result == 0) {
                return firstRect.right - secondRect.right;
            }
            return mRtlMult * result;
        };

        public void sort(android.view.View[] views, int start, int end, android.view.ViewGroup root, boolean isRtl) {
            int count = end - start;
            if (count < 2) {
                return;
            }
            if (mRectByView == null) {
                mRectByView = new java.util.HashMap<>();
            }
            mRtlMult = (isRtl) ? -1 : 1;
            for (int i = mRectPool.size(); i < count; ++i) {
                mRectPool.add(new android.graphics.Rect());
            }
            for (int i = start; i < end; ++i) {
                android.graphics.Rect next = mRectPool.get(mLastPoolRect++);
                views[i].getDrawingRect(next);
                root.offsetDescendantRectToMyCoords(views[i], next);
                mRectByView.put(views[i], next);
            }
            // Sort top-to-bottom
            java.util.Arrays.sort(views, start, count, mTopsComparator);
            // Sweep top-to-bottom to identify rows
            int sweepBottom = mRectByView.get(views[start]).bottom;
            int rowStart = start;
            int sweepIdx = start + 1;
            for (; sweepIdx < end; ++sweepIdx) {
                android.graphics.Rect currRect = mRectByView.get(views[sweepIdx]);
                if (currRect.top >= sweepBottom) {
                    // Next view is on a new row, sort the row we've just finished left-to-right.
                    if ((sweepIdx - rowStart) > 1) {
                        java.util.Arrays.sort(views, rowStart, sweepIdx, mSidesComparator);
                    }
                    sweepBottom = currRect.bottom;
                    rowStart = sweepIdx;
                } else {
                    // Next view vertically overlaps, we need to extend our "row height"
                    sweepBottom = java.lang.Math.max(sweepBottom, currRect.bottom);
                }
            }
            // Sort whatever's left (final row) left-to-right
            if ((sweepIdx - rowStart) > 1) {
                java.util.Arrays.sort(views, rowStart, sweepIdx, mSidesComparator);
            }
            mLastPoolRect = 0;
            mRectByView.clear();
        }
    }

    /**
     * Public for testing.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static void sort(android.view.View[] views, int start, int end, android.view.ViewGroup root, boolean isRtl) {
        android.view.FocusFinder.getInstance().mFocusSorter.sort(views, start, end, root, isRtl);
    }

    /**
     * Sorts views according to any explicitly-specified focus-chains. If there are no explicitly
     * specified focus chains (eg. no nextFocusForward attributes defined), this should be a no-op.
     */
    private static final class UserSpecifiedFocusComparator implements java.util.Comparator<android.view.View> {
        private final android.util.ArrayMap<android.view.View, android.view.View> mNextFoci = new android.util.ArrayMap();

        private final android.util.ArraySet<android.view.View> mIsConnectedTo = new android.util.ArraySet();

        private final android.util.ArrayMap<android.view.View, android.view.View> mHeadsOfChains = new android.util.ArrayMap<android.view.View, android.view.View>();

        private final android.util.ArrayMap<android.view.View, java.lang.Integer> mOriginalOrdinal = new android.util.ArrayMap();

        private final android.view.FocusFinder.UserSpecifiedFocusComparator.NextFocusGetter mNextFocusGetter;

        private android.view.View mRoot;

        public interface NextFocusGetter {
            android.view.View get(android.view.View root, android.view.View view);
        }

        UserSpecifiedFocusComparator(android.view.FocusFinder.UserSpecifiedFocusComparator.NextFocusGetter nextFocusGetter) {
            mNextFocusGetter = nextFocusGetter;
        }

        public void recycle() {
            mRoot = null;
            mHeadsOfChains.clear();
            mIsConnectedTo.clear();
            mOriginalOrdinal.clear();
            mNextFoci.clear();
        }

        public void setFocusables(java.util.List<android.view.View> focusables, android.view.View root) {
            mRoot = root;
            for (int i = 0; i < focusables.size(); ++i) {
                mOriginalOrdinal.put(focusables.get(i), i);
            }
            for (int i = focusables.size() - 1; i >= 0; i--) {
                final android.view.View view = focusables.get(i);
                final android.view.View next = mNextFocusGetter.get(mRoot, view);
                if ((next != null) && mOriginalOrdinal.containsKey(next)) {
                    mNextFoci.put(view, next);
                    mIsConnectedTo.add(next);
                }
            }
            for (int i = focusables.size() - 1; i >= 0; i--) {
                final android.view.View view = focusables.get(i);
                final android.view.View next = mNextFoci.get(view);
                if ((next != null) && (!mIsConnectedTo.contains(view))) {
                    setHeadOfChain(view);
                }
            }
        }

        private void setHeadOfChain(android.view.View head) {
            for (android.view.View view = head; view != null; view = mNextFoci.get(view)) {
                final android.view.View otherHead = mHeadsOfChains.get(view);
                if (otherHead != null) {
                    if (otherHead == head) {
                        return;// This view has already had its head set properly

                    }
                    // A hydra -- multi-headed focus chain (e.g. A->C and B->C)
                    // Use the one we've already chosen instead and reset this chain.
                    view = head;
                    head = otherHead;
                }
                mHeadsOfChains.put(view, head);
            }
        }

        public int compare(android.view.View first, android.view.View second) {
            if (first == second) {
                return 0;
            }
            // Order between views within a chain is immaterial -- next/previous is
            // within a chain is handled elsewhere.
            android.view.View firstHead = mHeadsOfChains.get(first);
            android.view.View secondHead = mHeadsOfChains.get(second);
            if ((firstHead == secondHead) && (firstHead != null)) {
                if (first == firstHead) {
                    return -1;// first is the head, it should be first

                } else
                    if (second == firstHead) {
                        return 1;// second is the head, it should be first

                    } else
                        if (mNextFoci.get(first) != null) {
                            return -1;// first is not the end of the chain

                        } else {
                            return 1;// first is end of chain

                        }


            }
            boolean involvesChain = false;
            if (firstHead != null) {
                first = firstHead;
                involvesChain = true;
            }
            if (secondHead != null) {
                second = secondHead;
                involvesChain = true;
            }
            if (involvesChain) {
                // keep original order between chains
                return mOriginalOrdinal.get(first) < mOriginalOrdinal.get(second) ? -1 : 1;
            } else {
                return 0;
            }
        }
    }
}

