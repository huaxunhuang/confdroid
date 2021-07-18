/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * Used by {@link ViewRootImpl} to track system gesture exclusion rects reported by views.
 */
class GestureExclusionTracker {
    private boolean mGestureExclusionViewsChanged = false;

    private boolean mRootGestureExclusionRectsChanged = false;

    private java.util.List<android.graphics.Rect> mRootGestureExclusionRects = java.util.Collections.emptyList();

    private java.util.List<android.view.GestureExclusionTracker.GestureExclusionViewInfo> mGestureExclusionViewInfos = new java.util.ArrayList<>();

    private java.util.List<android.graphics.Rect> mGestureExclusionRects = java.util.Collections.emptyList();

    public void updateRectsForView(@android.annotation.NonNull
    android.view.View view) {
        boolean found = false;
        final java.util.Iterator<android.view.GestureExclusionTracker.GestureExclusionViewInfo> i = mGestureExclusionViewInfos.iterator();
        while (i.hasNext()) {
            final android.view.GestureExclusionTracker.GestureExclusionViewInfo info = i.next();
            final android.view.View v = info.getView();
            if ((v == null) || (!v.isAttachedToWindow())) {
                mGestureExclusionViewsChanged = true;
                i.remove();
                continue;
            }
            if (v == view) {
                found = true;
                info.mDirty = true;
                break;
            }
        } 
        if ((!found) && view.isAttachedToWindow()) {
            mGestureExclusionViewInfos.add(new android.view.GestureExclusionTracker.GestureExclusionViewInfo(view));
            mGestureExclusionViewsChanged = true;
        }
    }

    @android.annotation.Nullable
    public java.util.List<android.graphics.Rect> computeChangedRects() {
        boolean changed = mRootGestureExclusionRectsChanged;
        final java.util.Iterator<android.view.GestureExclusionTracker.GestureExclusionViewInfo> i = mGestureExclusionViewInfos.iterator();
        final java.util.List<android.graphics.Rect> rects = new java.util.ArrayList<>(mRootGestureExclusionRects);
        while (i.hasNext()) {
            final android.view.GestureExclusionTracker.GestureExclusionViewInfo info = i.next();
            switch (info.update()) {
                case android.view.GestureExclusionTracker.GestureExclusionViewInfo.CHANGED :
                    changed = true;
                    // Deliberate fall-through
                case android.view.GestureExclusionTracker.GestureExclusionViewInfo.UNCHANGED :
                    rects.addAll(info.mExclusionRects);
                    break;
                case android.view.GestureExclusionTracker.GestureExclusionViewInfo.GONE :
                    mGestureExclusionViewsChanged = true;
                    i.remove();
                    break;
            }
        } 
        if (changed || mGestureExclusionViewsChanged) {
            mGestureExclusionViewsChanged = false;
            mRootGestureExclusionRectsChanged = false;
            if (!mGestureExclusionRects.equals(rects)) {
                mGestureExclusionRects = rects;
                return rects;
            }
        }
        return null;
    }

    public void setRootSystemGestureExclusionRects(@android.annotation.NonNull
    java.util.List<android.graphics.Rect> rects) {
        com.android.internal.util.Preconditions.checkNotNull(rects, "rects must not be null");
        mRootGestureExclusionRects = rects;
        mRootGestureExclusionRectsChanged = true;
    }

    @android.annotation.NonNull
    public java.util.List<android.graphics.Rect> getRootSystemGestureExclusionRects() {
        return mRootGestureExclusionRects;
    }

    private static class GestureExclusionViewInfo {
        public static final int CHANGED = 0;

        public static final int UNCHANGED = 1;

        public static final int GONE = 2;

        private final java.lang.ref.WeakReference<android.view.View> mView;

        boolean mDirty = true;

        java.util.List<android.graphics.Rect> mExclusionRects = java.util.Collections.emptyList();

        GestureExclusionViewInfo(android.view.View view) {
            mView = new java.lang.ref.WeakReference<>(view);
        }

        public android.view.View getView() {
            return mView.get();
        }

        public int update() {
            final android.view.View excludedView = getView();
            if ((excludedView == null) || (!excludedView.isAttachedToWindow()))
                return android.view.GestureExclusionTracker.GestureExclusionViewInfo.GONE;

            final java.util.List<android.graphics.Rect> localRects = excludedView.getSystemGestureExclusionRects();
            final java.util.List<android.graphics.Rect> newRects = new java.util.ArrayList<>(localRects.size());
            for (android.graphics.Rect src : localRects) {
                android.graphics.Rect mappedRect = new android.graphics.Rect(src);
                android.view.ViewParent p = excludedView.getParent();
                if ((p != null) && p.getChildVisibleRect(excludedView, mappedRect, null)) {
                    newRects.add(mappedRect);
                }
            }
            if (mExclusionRects.equals(localRects))
                return android.view.GestureExclusionTracker.GestureExclusionViewInfo.UNCHANGED;

            mExclusionRects = newRects;
            return android.view.GestureExclusionTracker.GestureExclusionViewInfo.CHANGED;
        }
    }
}

