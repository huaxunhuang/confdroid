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
package android.support.v4.widget;


class ScrollerCompatGingerbread {
    public static java.lang.Object createScroller(android.content.Context context, android.view.animation.Interpolator interpolator) {
        return interpolator != null ? new android.widget.OverScroller(context, interpolator) : new android.widget.OverScroller(context);
    }

    public static boolean isFinished(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).isFinished();
    }

    public static int getCurrX(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).getCurrX();
    }

    public static int getCurrY(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).getCurrY();
    }

    public static boolean computeScrollOffset(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).computeScrollOffset();
    }

    public static void startScroll(java.lang.Object scroller, int startX, int startY, int dx, int dy) {
        ((android.widget.OverScroller) (scroller)).startScroll(startX, startY, dx, dy);
    }

    public static void startScroll(java.lang.Object scroller, int startX, int startY, int dx, int dy, int duration) {
        ((android.widget.OverScroller) (scroller)).startScroll(startX, startY, dx, dy, duration);
    }

    public static void fling(java.lang.Object scroller, int startX, int startY, int velX, int velY, int minX, int maxX, int minY, int maxY) {
        ((android.widget.OverScroller) (scroller)).fling(startX, startY, velX, velY, minX, maxX, minY, maxY);
    }

    public static void fling(java.lang.Object scroller, int startX, int startY, int velX, int velY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        ((android.widget.OverScroller) (scroller)).fling(startX, startY, velX, velY, minX, maxX, minY, maxY, overX, overY);
    }

    public static void abortAnimation(java.lang.Object scroller) {
        ((android.widget.OverScroller) (scroller)).abortAnimation();
    }

    public static void notifyHorizontalEdgeReached(java.lang.Object scroller, int startX, int finalX, int overX) {
        ((android.widget.OverScroller) (scroller)).notifyHorizontalEdgeReached(startX, finalX, overX);
    }

    public static void notifyVerticalEdgeReached(java.lang.Object scroller, int startY, int finalY, int overY) {
        ((android.widget.OverScroller) (scroller)).notifyVerticalEdgeReached(startY, finalY, overY);
    }

    public static boolean isOverScrolled(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).isOverScrolled();
    }

    public static int getFinalX(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).getFinalX();
    }

    public static int getFinalY(java.lang.Object scroller) {
        return ((android.widget.OverScroller) (scroller)).getFinalY();
    }

    public static boolean springBack(java.lang.Object scroller, int startX, int startY, int minX, int maxX, int minY, int maxY) {
        return ((android.widget.OverScroller) (scroller)).springBack(startX, startY, minX, maxX, minY, maxY);
    }
}

