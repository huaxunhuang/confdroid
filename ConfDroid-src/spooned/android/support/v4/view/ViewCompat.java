/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.view;


/**
 * Helper for accessing features in {@link View} introduced after API
 * level 4 in a backwards compatible fashion.
 */
public class ViewCompat {
    private static final java.lang.String TAG = "ViewCompat";

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.view.View.FOCUS_LEFT, android.view.View.FOCUS_UP, android.view.View.FOCUS_RIGHT, android.view.View.FOCUS_DOWN, android.view.View.FOCUS_FORWARD, android.view.View.FOCUS_BACKWARD })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FocusDirection {}

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.view.View.FOCUS_LEFT, android.view.View.FOCUS_UP, android.view.View.FOCUS_RIGHT, android.view.View.FOCUS_DOWN })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {}

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.view.View.FOCUS_FORWARD, android.view.View.FOCUS_BACKWARD })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FocusRelativeDirection {}

    @android.support.annotation.IntDef({ android.support.v4.view.ViewCompat.OVER_SCROLL_ALWAYS, android.support.v4.view.ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS, android.support.v4.view.ViewCompat.OVER_SCROLL_NEVER })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface OverScroll {}

    /**
     * Always allow a user to over-scroll this view, provided it is a
     * view that can scroll.
     *
     * @deprecated Use {@link View#OVER_SCROLL_ALWAYS} directly. This constant will be removed in
    a future release.
     */
    @java.lang.Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;

    /**
     * Allow a user to over-scroll this view only if the content is large
     * enough to meaningfully scroll, provided it is a view that can scroll.
     *
     * @deprecated Use {@link View#OVER_SCROLL_IF_CONTENT_SCROLLS} directly. This constant will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;

    /**
     * Never allow a user to over-scroll this view.
     *
     * @deprecated Use {@link View#OVER_SCROLL_NEVER} directly. This constant will be removed in
    a future release.
     */
    @java.lang.Deprecated
    public static final int OVER_SCROLL_NEVER = 2;

    private static final long FAKE_FRAME_TIME = 10;

    @android.support.annotation.IntDef({ android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ImportantForAccessibility {}

    /**
     * Automatically determine whether a view is important for accessibility.
     */
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0x0;

    /**
     * The view is important for accessibility.
     */
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 0x1;

    /**
     * The view is not important for accessibility.
     */
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 0x2;

    /**
     * The view is not important for accessibility, nor are any of its
     * descendant views.
     */
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 0x4;

    @android.support.annotation.IntDef({ android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_NONE, android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE, android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_ASSERTIVE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface AccessibilityLiveRegion {}

    /**
     * Live region mode specifying that accessibility services should not
     * automatically announce changes to this view. This is the default live
     * region mode for most views.
     * <p>
     * Use with {@link ViewCompat#setAccessibilityLiveRegion(View, int)}.
     */
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0x0;

    /**
     * Live region mode specifying that accessibility services should announce
     * changes to this view.
     * <p>
     * Use with {@link ViewCompat#setAccessibilityLiveRegion(View, int)}.
     */
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 0x1;

    /**
     * Live region mode specifying that accessibility services should interrupt
     * ongoing speech to immediately announce changes to this view.
     * <p>
     * Use with {@link ViewCompat#setAccessibilityLiveRegion(View, int)}.
     */
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 0x2;

    @android.support.annotation.IntDef({ android.support.v4.view.ViewCompat.LAYER_TYPE_NONE, android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE, android.support.v4.view.ViewCompat.LAYER_TYPE_HARDWARE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface LayerType {}

    /**
     * Indicates that the view does not have a layer.
     */
    public static final int LAYER_TYPE_NONE = 0;

    /**
     * <p>Indicates that the view has a software layer. A software layer is backed
     * by a bitmap and causes the view to be rendered using Android's software
     * rendering pipeline, even if hardware acceleration is enabled.</p>
     *
     * <p>Software layers have various usages:</p>
     * <p>When the application is not using hardware acceleration, a software layer
     * is useful to apply a specific color filter and/or blending mode and/or
     * translucency to a view and all its children.</p>
     * <p>When the application is using hardware acceleration, a software layer
     * is useful to render drawing primitives not supported by the hardware
     * accelerated pipeline. It can also be used to cache a complex view tree
     * into a texture and reduce the complexity of drawing operations. For instance,
     * when animating a complex view tree with a translation, a software layer can
     * be used to render the view tree only once.</p>
     * <p>Software layers should be avoided when the affected view tree updates
     * often. Every update will require to re-render the software layer, which can
     * potentially be slow (particularly when hardware acceleration is turned on
     * since the layer will have to be uploaded into a hardware texture after every
     * update.)</p>
     */
    public static final int LAYER_TYPE_SOFTWARE = 1;

    /**
     * <p>Indicates that the view has a hardware layer. A hardware layer is backed
     * by a hardware specific texture (generally Frame Buffer Objects or FBO on
     * OpenGL hardware) and causes the view to be rendered using Android's hardware
     * rendering pipeline, but only if hardware acceleration is turned on for the
     * view hierarchy. When hardware acceleration is turned off, hardware layers
     * behave exactly as {@link #LAYER_TYPE_SOFTWARE software layers}.</p>
     *
     * <p>A hardware layer is useful to apply a specific color filter and/or
     * blending mode and/or translucency to a view and all its children.</p>
     * <p>A hardware layer can be used to cache a complex view tree into a
     * texture and reduce the complexity of drawing operations. For instance,
     * when animating a complex view tree with a translation, a hardware layer can
     * be used to render the view tree only once.</p>
     * <p>A hardware layer can also be used to increase the rendering quality when
     * rotation transformations are applied on a view. It can also be used to
     * prevent potential clipping issues when applying 3D transforms on a view.</p>
     */
    public static final int LAYER_TYPE_HARDWARE = 2;

    @android.support.annotation.IntDef({ android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR, android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL, android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_INHERIT, android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LOCALE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface LayoutDirectionMode {}

    @android.support.annotation.IntDef({ android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR, android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ResolvedLayoutDirectionMode {}

    /**
     * Horizontal layout direction of this view is from Left to Right.
     */
    public static final int LAYOUT_DIRECTION_LTR = 0;

    /**
     * Horizontal layout direction of this view is from Right to Left.
     */
    public static final int LAYOUT_DIRECTION_RTL = 1;

    /**
     * Horizontal layout direction of this view is inherited from its parent.
     * Use with {@link #setLayoutDirection}.
     */
    public static final int LAYOUT_DIRECTION_INHERIT = 2;

    /**
     * Horizontal layout direction of this view is from deduced from the default language
     * script for the locale. Use with {@link #setLayoutDirection}.
     */
    public static final int LAYOUT_DIRECTION_LOCALE = 3;

    /**
     * Bits of {@link #getMeasuredWidthAndState} and
     * {@link #getMeasuredWidthAndState} that provide the actual measured size.
     */
    public static final int MEASURED_SIZE_MASK = 0xffffff;

    /**
     * Bits of {@link #getMeasuredWidthAndState} and
     * {@link #getMeasuredWidthAndState} that provide the additional state bits.
     */
    public static final int MEASURED_STATE_MASK = 0xff000000;

    /**
     * Bit shift of {@link #MEASURED_STATE_MASK} to get to the height bits
     * for functions that combine both width and height into a single int,
     * such as {@link #getMeasuredState} and the childState argument of
     * {@link #resolveSizeAndState(int, int, int)}.
     */
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;

    /**
     * Bit of {@link #getMeasuredWidthAndState} and
     * {@link #getMeasuredWidthAndState} that indicates the measured size
     * is smaller that the space the view would like to have.
     */
    public static final int MEASURED_STATE_TOO_SMALL = 0x1000000;

    /**
     * Indicates no axis of view scrolling.
     */
    public static final int SCROLL_AXIS_NONE = 0;

    /**
     * Indicates scrolling along the horizontal axis.
     */
    public static final int SCROLL_AXIS_HORIZONTAL = 1 << 0;

    /**
     * Indicates scrolling along the vertical axis.
     */
    public static final int SCROLL_AXIS_VERTICAL = 1 << 1;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.support.annotation.IntDef(flag = true, value = { android.support.v4.view.ViewCompat.SCROLL_INDICATOR_TOP, android.support.v4.view.ViewCompat.SCROLL_INDICATOR_BOTTOM, android.support.v4.view.ViewCompat.SCROLL_INDICATOR_LEFT, android.support.v4.view.ViewCompat.SCROLL_INDICATOR_RIGHT, android.support.v4.view.ViewCompat.SCROLL_INDICATOR_START, android.support.v4.view.ViewCompat.SCROLL_INDICATOR_END })
    public @interface ScrollIndicators {}

    /**
     * Scroll indicator direction for the top edge of the view.
     *
     * @see #setScrollIndicators(View, int)
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static final int SCROLL_INDICATOR_TOP = 0x1;

    /**
     * Scroll indicator direction for the bottom edge of the view.
     *
     * @see #setScrollIndicators(View, int)
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static final int SCROLL_INDICATOR_BOTTOM = 0x2;

    /**
     * Scroll indicator direction for the left edge of the view.
     *
     * @see #setScrollIndicators(View, int)
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static final int SCROLL_INDICATOR_LEFT = 0x4;

    /**
     * Scroll indicator direction for the right edge of the view.
     *
     * @see #setScrollIndicators(View, int)
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static final int SCROLL_INDICATOR_RIGHT = 0x8;

    /**
     * Scroll indicator direction for the starting edge of the view.
     *
     * @see #setScrollIndicators(View, int)
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static final int SCROLL_INDICATOR_START = 0x10;

    /**
     * Scroll indicator direction for the ending edge of the view.
     *
     * @see #setScrollIndicators(View, int)
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static final int SCROLL_INDICATOR_END = 0x20;

    interface ViewCompatImpl {
        boolean canScrollHorizontally(android.view.View v, int direction);

        boolean canScrollVertically(android.view.View v, int direction);

        void onInitializeAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event);

        void onPopulateAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event);

        void onInitializeAccessibilityNodeInfo(android.view.View v, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info);

        void setAccessibilityDelegate(android.view.View v, @android.support.annotation.Nullable
        android.support.v4.view.AccessibilityDelegateCompat delegate);

        boolean hasAccessibilityDelegate(android.view.View v);

        boolean hasTransientState(android.view.View view);

        void setHasTransientState(android.view.View view, boolean hasTransientState);

        void postInvalidateOnAnimation(android.view.View view);

        void postInvalidateOnAnimation(android.view.View view, int left, int top, int right, int bottom);

        void postOnAnimation(android.view.View view, java.lang.Runnable action);

        void postOnAnimationDelayed(android.view.View view, java.lang.Runnable action, long delayMillis);

        int getImportantForAccessibility(android.view.View view);

        void setImportantForAccessibility(android.view.View view, int mode);

        boolean isImportantForAccessibility(android.view.View view);

        boolean performAccessibilityAction(android.view.View view, int action, android.os.Bundle arguments);

        android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(android.view.View view);

        float getAlpha(android.view.View view);

        void setLayerType(android.view.View view, int layerType, android.graphics.Paint paint);

        int getLayerType(android.view.View view);

        int getLabelFor(android.view.View view);

        void setLabelFor(android.view.View view, int id);

        void setLayerPaint(android.view.View view, android.graphics.Paint paint);

        int getLayoutDirection(android.view.View view);

        void setLayoutDirection(android.view.View view, int layoutDirection);

        android.view.ViewParent getParentForAccessibility(android.view.View view);

        int resolveSizeAndState(int size, int measureSpec, int childMeasuredState);

        int getMeasuredWidthAndState(android.view.View view);

        int getMeasuredHeightAndState(android.view.View view);

        int getMeasuredState(android.view.View view);

        int getAccessibilityLiveRegion(android.view.View view);

        void setAccessibilityLiveRegion(android.view.View view, int mode);

        int getPaddingStart(android.view.View view);

        int getPaddingEnd(android.view.View view);

        void setPaddingRelative(android.view.View view, int start, int top, int end, int bottom);

        void dispatchStartTemporaryDetach(android.view.View view);

        void dispatchFinishTemporaryDetach(android.view.View view);

        float getX(android.view.View view);

        float getY(android.view.View view);

        float getRotation(android.view.View view);

        float getRotationX(android.view.View view);

        float getRotationY(android.view.View view);

        float getScaleX(android.view.View view);

        float getScaleY(android.view.View view);

        float getTranslationX(android.view.View view);

        float getTranslationY(android.view.View view);

        @android.support.annotation.Nullable
        android.graphics.Matrix getMatrix(android.view.View view);

        int getMinimumWidth(android.view.View view);

        int getMinimumHeight(android.view.View view);

        android.support.v4.view.ViewPropertyAnimatorCompat animate(android.view.View view);

        void setRotation(android.view.View view, float value);

        void setRotationX(android.view.View view, float value);

        void setRotationY(android.view.View view, float value);

        void setScaleX(android.view.View view, float value);

        void setScaleY(android.view.View view, float value);

        void setTranslationX(android.view.View view, float value);

        void setTranslationY(android.view.View view, float value);

        void setX(android.view.View view, float value);

        void setY(android.view.View view, float value);

        void setAlpha(android.view.View view, float value);

        void setPivotX(android.view.View view, float value);

        void setPivotY(android.view.View view, float value);

        float getPivotX(android.view.View view);

        float getPivotY(android.view.View view);

        void setElevation(android.view.View view, float elevation);

        float getElevation(android.view.View view);

        void setTranslationZ(android.view.View view, float translationZ);

        float getTranslationZ(android.view.View view);

        void setClipBounds(android.view.View view, android.graphics.Rect clipBounds);

        android.graphics.Rect getClipBounds(android.view.View view);

        void setTransitionName(android.view.View view, java.lang.String transitionName);

        java.lang.String getTransitionName(android.view.View view);

        int getWindowSystemUiVisibility(android.view.View view);

        void requestApplyInsets(android.view.View view);

        void setChildrenDrawingOrderEnabled(android.view.ViewGroup viewGroup, boolean enabled);

        boolean getFitsSystemWindows(android.view.View view);

        boolean hasOverlappingRendering(android.view.View view);

        void setFitsSystemWindows(android.view.View view, boolean fitSystemWindows);

        void jumpDrawablesToCurrentState(android.view.View v);

        void setOnApplyWindowInsetsListener(android.view.View view, android.support.v4.view.OnApplyWindowInsetsListener listener);

        android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets);

        android.support.v4.view.WindowInsetsCompat dispatchApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets);

        void setSaveFromParentEnabled(android.view.View view, boolean enabled);

        void setActivated(android.view.View view, boolean activated);

        boolean isPaddingRelative(android.view.View view);

        void setBackground(android.view.View view, android.graphics.drawable.Drawable background);

        android.content.res.ColorStateList getBackgroundTintList(android.view.View view);

        void setBackgroundTintList(android.view.View view, android.content.res.ColorStateList tintList);

        android.graphics.PorterDuff.Mode getBackgroundTintMode(android.view.View view);

        void setBackgroundTintMode(android.view.View view, android.graphics.PorterDuff.Mode mode);

        void setNestedScrollingEnabled(android.view.View view, boolean enabled);

        boolean isNestedScrollingEnabled(android.view.View view);

        boolean startNestedScroll(android.view.View view, int axes);

        void stopNestedScroll(android.view.View view);

        boolean hasNestedScrollingParent(android.view.View view);

        boolean dispatchNestedScroll(android.view.View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);

        boolean dispatchNestedPreScroll(android.view.View view, int dx, int dy, int[] consumed, int[] offsetInWindow);

        boolean dispatchNestedFling(android.view.View view, float velocityX, float velocityY, boolean consumed);

        boolean dispatchNestedPreFling(android.view.View view, float velocityX, float velocityY);

        boolean isInLayout(android.view.View view);

        boolean isLaidOut(android.view.View view);

        boolean isLayoutDirectionResolved(android.view.View view);

        int combineMeasuredStates(int curState, int newState);

        float getZ(android.view.View view);

        void setZ(android.view.View view, float z);

        boolean isAttachedToWindow(android.view.View view);

        boolean hasOnClickListeners(android.view.View view);

        void setScrollIndicators(android.view.View view, int indicators);

        void setScrollIndicators(android.view.View view, int indicators, int mask);

        int getScrollIndicators(android.view.View view);

        void offsetTopAndBottom(android.view.View view, int offset);

        void offsetLeftAndRight(android.view.View view, int offset);

        void setPointerIcon(android.view.View view, android.support.v4.view.PointerIconCompat pointerIcon);

        android.view.Display getDisplay(android.view.View view);
    }

    static class BaseViewCompatImpl implements android.support.v4.view.ViewCompat.ViewCompatImpl {
        private java.lang.reflect.Method mDispatchStartTemporaryDetach;

        private java.lang.reflect.Method mDispatchFinishTemporaryDetach;

        private boolean mTempDetachBound;

        java.util.WeakHashMap<android.view.View, android.support.v4.view.ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap = null;

        private static java.lang.reflect.Method sChildrenDrawingOrderMethod;

        @java.lang.Override
        public boolean canScrollHorizontally(android.view.View v, int direction) {
            return (v instanceof android.support.v4.view.ScrollingView) && canScrollingViewScrollHorizontally(((android.support.v4.view.ScrollingView) (v)), direction);
        }

        @java.lang.Override
        public boolean canScrollVertically(android.view.View v, int direction) {
            return (v instanceof android.support.v4.view.ScrollingView) && canScrollingViewScrollVertically(((android.support.v4.view.ScrollingView) (v)), direction);
        }

        @java.lang.Override
        public void setAccessibilityDelegate(android.view.View v, android.support.v4.view.AccessibilityDelegateCompat delegate) {
            // Do nothing; API doesn't exist
        }

        @java.lang.Override
        public boolean hasAccessibilityDelegate(android.view.View v) {
            return false;
        }

        @java.lang.Override
        public void onPopulateAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
            // Do nothing; API doesn't exist
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
            // Do nothing; API doesn't exist
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View v, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            // Do nothing; API doesn't exist
        }

        @java.lang.Override
        public boolean hasTransientState(android.view.View view) {
            // A view can't have transient state if transient state wasn't supported.
            return false;
        }

        @java.lang.Override
        public void setHasTransientState(android.view.View view, boolean hasTransientState) {
            // Do nothing; API doesn't exist
        }

        @java.lang.Override
        public void postInvalidateOnAnimation(android.view.View view) {
            view.invalidate();
        }

        @java.lang.Override
        public void postInvalidateOnAnimation(android.view.View view, int left, int top, int right, int bottom) {
            view.invalidate(left, top, right, bottom);
        }

        @java.lang.Override
        public void postOnAnimation(android.view.View view, java.lang.Runnable action) {
            view.postDelayed(action, getFrameTime());
        }

        @java.lang.Override
        public void postOnAnimationDelayed(android.view.View view, java.lang.Runnable action, long delayMillis) {
            view.postDelayed(action, getFrameTime() + delayMillis);
        }

        long getFrameTime() {
            return android.support.v4.view.ViewCompat.FAKE_FRAME_TIME;
        }

        @java.lang.Override
        public int getImportantForAccessibility(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void setImportantForAccessibility(android.view.View view, int mode) {
        }

        @java.lang.Override
        public boolean isImportantForAccessibility(android.view.View view) {
            return true;
        }

        @java.lang.Override
        public boolean performAccessibilityAction(android.view.View view, int action, android.os.Bundle arguments) {
            return false;
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(android.view.View view) {
            return null;
        }

        @java.lang.Override
        public float getAlpha(android.view.View view) {
            return 1.0F;
        }

        @java.lang.Override
        public void setLayerType(android.view.View view, int layerType, android.graphics.Paint paint) {
            // No-op until layers became available (HC)
        }

        @java.lang.Override
        public int getLayerType(android.view.View view) {
            return android.support.v4.view.ViewCompat.LAYER_TYPE_NONE;
        }

        @java.lang.Override
        public int getLabelFor(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void setLabelFor(android.view.View view, int id) {
        }

        @java.lang.Override
        public void setLayerPaint(android.view.View view, android.graphics.Paint p) {
            // No-op until layers became available (HC)
        }

        @java.lang.Override
        public int getLayoutDirection(android.view.View view) {
            return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
        }

        @java.lang.Override
        public void setLayoutDirection(android.view.View view, int layoutDirection) {
            // No-op
        }

        @java.lang.Override
        public android.view.ViewParent getParentForAccessibility(android.view.View view) {
            return view.getParent();
        }

        @java.lang.Override
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
            return android.view.View.resolveSize(size, measureSpec);
        }

        @java.lang.Override
        public int getMeasuredWidthAndState(android.view.View view) {
            return view.getMeasuredWidth();
        }

        @java.lang.Override
        public int getMeasuredHeightAndState(android.view.View view) {
            return view.getMeasuredHeight();
        }

        @java.lang.Override
        public int getMeasuredState(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public int getAccessibilityLiveRegion(android.view.View view) {
            return android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_NONE;
        }

        @java.lang.Override
        public void setAccessibilityLiveRegion(android.view.View view, int mode) {
            // No-op
        }

        @java.lang.Override
        public int getPaddingStart(android.view.View view) {
            return view.getPaddingLeft();
        }

        @java.lang.Override
        public int getPaddingEnd(android.view.View view) {
            return view.getPaddingRight();
        }

        @java.lang.Override
        public void setPaddingRelative(android.view.View view, int start, int top, int end, int bottom) {
            view.setPadding(start, top, end, bottom);
        }

        @java.lang.Override
        public void dispatchStartTemporaryDetach(android.view.View view) {
            if (!mTempDetachBound) {
                bindTempDetach();
            }
            if (mDispatchStartTemporaryDetach != null) {
                try {
                    mDispatchStartTemporaryDetach.invoke(view);
                } catch (java.lang.Exception e) {
                    android.util.Log.d(android.support.v4.view.ViewCompat.TAG, "Error calling dispatchStartTemporaryDetach", e);
                }
            } else {
                // Try this instead
                view.onStartTemporaryDetach();
            }
        }

        @java.lang.Override
        public void dispatchFinishTemporaryDetach(android.view.View view) {
            if (!mTempDetachBound) {
                bindTempDetach();
            }
            if (mDispatchFinishTemporaryDetach != null) {
                try {
                    mDispatchFinishTemporaryDetach.invoke(view);
                } catch (java.lang.Exception e) {
                    android.util.Log.d(android.support.v4.view.ViewCompat.TAG, "Error calling dispatchFinishTemporaryDetach", e);
                }
            } else {
                // Try this instead
                view.onFinishTemporaryDetach();
            }
        }

        @java.lang.Override
        public boolean hasOverlappingRendering(android.view.View view) {
            return true;
        }

        private void bindTempDetach() {
            try {
                mDispatchStartTemporaryDetach = android.view.View.class.getDeclaredMethod("dispatchStartTemporaryDetach");
                mDispatchFinishTemporaryDetach = android.view.View.class.getDeclaredMethod("dispatchFinishTemporaryDetach");
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.e(android.support.v4.view.ViewCompat.TAG, "Couldn't find method", e);
            }
            mTempDetachBound = true;
        }

        @java.lang.Override
        public float getTranslationX(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getTranslationY(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getX(android.view.View view) {
            return view.getLeft();
        }

        @java.lang.Override
        public float getY(android.view.View view) {
            return view.getTop();
        }

        @java.lang.Override
        public float getRotation(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getRotationX(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getRotationY(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getScaleX(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getScaleY(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public android.graphics.Matrix getMatrix(android.view.View view) {
            return null;
        }

        @java.lang.Override
        public int getMinimumWidth(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.getMinimumWidth(view);
        }

        @java.lang.Override
        public int getMinimumHeight(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.getMinimumHeight(view);
        }

        @java.lang.Override
        public android.support.v4.view.ViewPropertyAnimatorCompat animate(android.view.View view) {
            return new android.support.v4.view.ViewPropertyAnimatorCompat(view);
        }

        @java.lang.Override
        public void setRotation(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setTranslationX(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setTranslationY(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setAlpha(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setRotationX(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setRotationY(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setScaleX(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setScaleY(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setX(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setY(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setPivotX(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public void setPivotY(android.view.View view, float value) {
            // noop
        }

        @java.lang.Override
        public float getPivotX(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public float getPivotY(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void setTransitionName(android.view.View view, java.lang.String transitionName) {
        }

        @java.lang.Override
        public java.lang.String getTransitionName(android.view.View view) {
            return null;
        }

        @java.lang.Override
        public int getWindowSystemUiVisibility(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void requestApplyInsets(android.view.View view) {
        }

        @java.lang.Override
        public void setElevation(android.view.View view, float elevation) {
        }

        @java.lang.Override
        public float getElevation(android.view.View view) {
            return 0.0F;
        }

        @java.lang.Override
        public void setTranslationZ(android.view.View view, float translationZ) {
        }

        @java.lang.Override
        public float getTranslationZ(android.view.View view) {
            return 0.0F;
        }

        @java.lang.Override
        public void setClipBounds(android.view.View view, android.graphics.Rect clipBounds) {
        }

        @java.lang.Override
        public android.graphics.Rect getClipBounds(android.view.View view) {
            return null;
        }

        @java.lang.Override
        public void setChildrenDrawingOrderEnabled(android.view.ViewGroup viewGroup, boolean enabled) {
            if (android.support.v4.view.ViewCompat.BaseViewCompatImpl.sChildrenDrawingOrderMethod == null) {
                try {
                    android.support.v4.view.ViewCompat.BaseViewCompatImpl.sChildrenDrawingOrderMethod = android.view.ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", boolean.class);
                } catch (java.lang.NoSuchMethodException e) {
                    android.util.Log.e(android.support.v4.view.ViewCompat.TAG, "Unable to find childrenDrawingOrderEnabled", e);
                }
                android.support.v4.view.ViewCompat.BaseViewCompatImpl.sChildrenDrawingOrderMethod.setAccessible(true);
            }
            try {
                android.support.v4.view.ViewCompat.BaseViewCompatImpl.sChildrenDrawingOrderMethod.invoke(viewGroup, enabled);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e(android.support.v4.view.ViewCompat.TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(android.support.v4.view.ViewCompat.TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                android.util.Log.e(android.support.v4.view.ViewCompat.TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
            }
        }

        @java.lang.Override
        public boolean getFitsSystemWindows(android.view.View view) {
            return false;
        }

        @java.lang.Override
        public void setFitsSystemWindows(android.view.View view, boolean fitSystemWindows) {
            // noop
        }

        @java.lang.Override
        public void jumpDrawablesToCurrentState(android.view.View view) {
            // Do nothing; API didn't exist.
        }

        @java.lang.Override
        public void setOnApplyWindowInsetsListener(android.view.View view, android.support.v4.view.OnApplyWindowInsetsListener listener) {
            // noop
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
            return insets;
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat dispatchApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
            return insets;
        }

        @java.lang.Override
        public void setSaveFromParentEnabled(android.view.View v, boolean enabled) {
            // noop
        }

        @java.lang.Override
        public void setActivated(android.view.View view, boolean activated) {
            // noop
        }

        @java.lang.Override
        public boolean isPaddingRelative(android.view.View view) {
            return false;
        }

        @java.lang.Override
        public void setNestedScrollingEnabled(android.view.View view, boolean enabled) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                ((android.support.v4.view.NestedScrollingChild) (view)).setNestedScrollingEnabled(enabled);
            }
        }

        @java.lang.Override
        public boolean isNestedScrollingEnabled(android.view.View view) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).isNestedScrollingEnabled();
            }
            return false;
        }

        @java.lang.Override
        public void setBackground(android.view.View view, android.graphics.drawable.Drawable background) {
            view.setBackgroundDrawable(background);
        }

        @java.lang.Override
        public android.content.res.ColorStateList getBackgroundTintList(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.getBackgroundTintList(view);
        }

        @java.lang.Override
        public void setBackgroundTintList(android.view.View view, android.content.res.ColorStateList tintList) {
            android.support.v4.view.ViewCompatBase.setBackgroundTintList(view, tintList);
        }

        @java.lang.Override
        public void setBackgroundTintMode(android.view.View view, android.graphics.PorterDuff.Mode mode) {
            android.support.v4.view.ViewCompatBase.setBackgroundTintMode(view, mode);
        }

        @java.lang.Override
        public android.graphics.PorterDuff.Mode getBackgroundTintMode(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.getBackgroundTintMode(view);
        }

        private boolean canScrollingViewScrollHorizontally(android.support.v4.view.ScrollingView view, int direction) {
            final int offset = view.computeHorizontalScrollOffset();
            final int range = view.computeHorizontalScrollRange() - view.computeHorizontalScrollExtent();
            if (range == 0)
                return false;

            if (direction < 0) {
                return offset > 0;
            } else {
                return offset < (range - 1);
            }
        }

        private boolean canScrollingViewScrollVertically(android.support.v4.view.ScrollingView view, int direction) {
            final int offset = view.computeVerticalScrollOffset();
            final int range = view.computeVerticalScrollRange() - view.computeVerticalScrollExtent();
            if (range == 0)
                return false;

            if (direction < 0) {
                return offset > 0;
            } else {
                return offset < (range - 1);
            }
        }

        @java.lang.Override
        public boolean startNestedScroll(android.view.View view, int axes) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).startNestedScroll(axes);
            }
            return false;
        }

        @java.lang.Override
        public void stopNestedScroll(android.view.View view) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                ((android.support.v4.view.NestedScrollingChild) (view)).stopNestedScroll();
            }
        }

        @java.lang.Override
        public boolean hasNestedScrollingParent(android.view.View view) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).hasNestedScrollingParent();
            }
            return false;
        }

        @java.lang.Override
        public boolean dispatchNestedScroll(android.view.View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
            }
            return false;
        }

        @java.lang.Override
        public boolean dispatchNestedPreScroll(android.view.View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
            }
            return false;
        }

        @java.lang.Override
        public boolean dispatchNestedFling(android.view.View view, float velocityX, float velocityY, boolean consumed) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).dispatchNestedFling(velocityX, velocityY, consumed);
            }
            return false;
        }

        @java.lang.Override
        public boolean dispatchNestedPreFling(android.view.View view, float velocityX, float velocityY) {
            if (view instanceof android.support.v4.view.NestedScrollingChild) {
                return ((android.support.v4.view.NestedScrollingChild) (view)).dispatchNestedPreFling(velocityX, velocityY);
            }
            return false;
        }

        @java.lang.Override
        public boolean isInLayout(android.view.View view) {
            return false;
        }

        @java.lang.Override
        public boolean isLaidOut(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.isLaidOut(view);
        }

        @java.lang.Override
        public boolean isLayoutDirectionResolved(android.view.View view) {
            return false;
        }

        @java.lang.Override
        public int combineMeasuredStates(int curState, int newState) {
            return curState | newState;
        }

        @java.lang.Override
        public float getZ(android.view.View view) {
            return getTranslationZ(view) + getElevation(view);
        }

        @java.lang.Override
        public void setZ(android.view.View view, float z) {
            // no-op
        }

        @java.lang.Override
        public boolean isAttachedToWindow(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.isAttachedToWindow(view);
        }

        @java.lang.Override
        public boolean hasOnClickListeners(android.view.View view) {
            return false;
        }

        @java.lang.Override
        public int getScrollIndicators(android.view.View view) {
            return 0;
        }

        @java.lang.Override
        public void setScrollIndicators(android.view.View view, int indicators) {
            // no-op
        }

        @java.lang.Override
        public void setScrollIndicators(android.view.View view, int indicators, int mask) {
            // no-op
        }

        @java.lang.Override
        public void offsetLeftAndRight(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatBase.offsetLeftAndRight(view, offset);
        }

        @java.lang.Override
        public void offsetTopAndBottom(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatBase.offsetTopAndBottom(view, offset);
        }

        @java.lang.Override
        public void setPointerIcon(android.view.View view, android.support.v4.view.PointerIconCompat pointerIcon) {
            // no-op
        }

        @java.lang.Override
        public android.view.Display getDisplay(android.view.View view) {
            return android.support.v4.view.ViewCompatBase.getDisplay(view);
        }
    }

    static class HCViewCompatImpl extends android.support.v4.view.ViewCompat.BaseViewCompatImpl {
        @java.lang.Override
        long getFrameTime() {
            return android.support.v4.view.ViewCompatHC.getFrameTime();
        }

        @java.lang.Override
        public float getAlpha(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getAlpha(view);
        }

        @java.lang.Override
        public void setLayerType(android.view.View view, int layerType, android.graphics.Paint paint) {
            android.support.v4.view.ViewCompatHC.setLayerType(view, layerType, paint);
        }

        @java.lang.Override
        public int getLayerType(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getLayerType(view);
        }

        @java.lang.Override
        public void setLayerPaint(android.view.View view, android.graphics.Paint paint) {
            // Make sure the paint is correct; this will be cheap if it's the same
            // instance as was used to call setLayerType earlier.
            setLayerType(view, getLayerType(view), paint);
            // This is expensive, but the only way to accomplish this before JB-MR1.
            view.invalidate();
        }

        @java.lang.Override
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
            return android.support.v4.view.ViewCompatHC.resolveSizeAndState(size, measureSpec, childMeasuredState);
        }

        @java.lang.Override
        public int getMeasuredWidthAndState(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getMeasuredWidthAndState(view);
        }

        @java.lang.Override
        public int getMeasuredHeightAndState(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getMeasuredHeightAndState(view);
        }

        @java.lang.Override
        public int getMeasuredState(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getMeasuredState(view);
        }

        @java.lang.Override
        public float getTranslationX(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getTranslationX(view);
        }

        @java.lang.Override
        public float getTranslationY(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getTranslationY(view);
        }

        @java.lang.Override
        public android.graphics.Matrix getMatrix(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getMatrix(view);
        }

        @java.lang.Override
        public void setTranslationX(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setTranslationX(view, value);
        }

        @java.lang.Override
        public void setTranslationY(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setTranslationY(view, value);
        }

        @java.lang.Override
        public void setAlpha(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setAlpha(view, value);
        }

        @java.lang.Override
        public void setX(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setX(view, value);
        }

        @java.lang.Override
        public void setY(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setY(view, value);
        }

        @java.lang.Override
        public void setRotation(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setRotation(view, value);
        }

        @java.lang.Override
        public void setRotationX(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setRotationX(view, value);
        }

        @java.lang.Override
        public void setRotationY(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setRotationY(view, value);
        }

        @java.lang.Override
        public void setScaleX(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setScaleX(view, value);
        }

        @java.lang.Override
        public void setScaleY(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setScaleY(view, value);
        }

        @java.lang.Override
        public void setPivotX(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setPivotX(view, value);
        }

        @java.lang.Override
        public void setPivotY(android.view.View view, float value) {
            android.support.v4.view.ViewCompatHC.setPivotY(view, value);
        }

        @java.lang.Override
        public float getX(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getX(view);
        }

        @java.lang.Override
        public float getY(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getY(view);
        }

        @java.lang.Override
        public float getRotation(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getRotation(view);
        }

        @java.lang.Override
        public float getRotationX(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getRotationX(view);
        }

        @java.lang.Override
        public float getRotationY(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getRotationY(view);
        }

        @java.lang.Override
        public float getScaleX(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getScaleX(view);
        }

        @java.lang.Override
        public float getScaleY(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getScaleY(view);
        }

        @java.lang.Override
        public float getPivotX(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getPivotX(view);
        }

        @java.lang.Override
        public float getPivotY(android.view.View view) {
            return android.support.v4.view.ViewCompatHC.getPivotY(view);
        }

        @java.lang.Override
        public void jumpDrawablesToCurrentState(android.view.View view) {
            android.support.v4.view.ViewCompatHC.jumpDrawablesToCurrentState(view);
        }

        @java.lang.Override
        public void setSaveFromParentEnabled(android.view.View view, boolean enabled) {
            android.support.v4.view.ViewCompatHC.setSaveFromParentEnabled(view, enabled);
        }

        @java.lang.Override
        public void setActivated(android.view.View view, boolean activated) {
            android.support.v4.view.ViewCompatHC.setActivated(view, activated);
        }

        @java.lang.Override
        public int combineMeasuredStates(int curState, int newState) {
            return android.support.v4.view.ViewCompatHC.combineMeasuredStates(curState, newState);
        }

        @java.lang.Override
        public void offsetLeftAndRight(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatHC.offsetLeftAndRight(view, offset);
        }

        @java.lang.Override
        public void offsetTopAndBottom(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatHC.offsetTopAndBottom(view, offset);
        }
    }

    static class ICSViewCompatImpl extends android.support.v4.view.ViewCompat.HCViewCompatImpl {
        static java.lang.reflect.Field mAccessibilityDelegateField;

        static boolean accessibilityDelegateCheckFailed = false;

        @java.lang.Override
        public boolean canScrollHorizontally(android.view.View v, int direction) {
            return android.support.v4.view.ViewCompatICS.canScrollHorizontally(v, direction);
        }

        @java.lang.Override
        public boolean canScrollVertically(android.view.View v, int direction) {
            return android.support.v4.view.ViewCompatICS.canScrollVertically(v, direction);
        }

        @java.lang.Override
        public void onPopulateAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
            android.support.v4.view.ViewCompatICS.onPopulateAccessibilityEvent(v, event);
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
            android.support.v4.view.ViewCompatICS.onInitializeAccessibilityEvent(v, event);
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View v, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            android.support.v4.view.ViewCompatICS.onInitializeAccessibilityNodeInfo(v, info.getInfo());
        }

        @java.lang.Override
        public void setAccessibilityDelegate(android.view.View v, @android.support.annotation.Nullable
        android.support.v4.view.AccessibilityDelegateCompat delegate) {
            android.support.v4.view.ViewCompatICS.setAccessibilityDelegate(v, delegate == null ? null : delegate.getBridge());
        }

        @java.lang.Override
        public boolean hasAccessibilityDelegate(android.view.View v) {
            if (android.support.v4.view.ViewCompat.ICSViewCompatImpl.accessibilityDelegateCheckFailed) {
                return false;// View implementation might have changed.

            }
            if (android.support.v4.view.ViewCompat.ICSViewCompatImpl.mAccessibilityDelegateField == null) {
                try {
                    android.support.v4.view.ViewCompat.ICSViewCompatImpl.mAccessibilityDelegateField = android.view.View.class.getDeclaredField("mAccessibilityDelegate");
                    android.support.v4.view.ViewCompat.ICSViewCompatImpl.mAccessibilityDelegateField.setAccessible(true);
                } catch (java.lang.Throwable t) {
                    android.support.v4.view.ViewCompat.ICSViewCompatImpl.accessibilityDelegateCheckFailed = true;
                    return false;
                }
            }
            try {
                return android.support.v4.view.ViewCompat.ICSViewCompatImpl.mAccessibilityDelegateField.get(v) != null;
            } catch (java.lang.Throwable t) {
                android.support.v4.view.ViewCompat.ICSViewCompatImpl.accessibilityDelegateCheckFailed = true;
                return false;
            }
        }

        @java.lang.Override
        public android.support.v4.view.ViewPropertyAnimatorCompat animate(android.view.View view) {
            if (mViewPropertyAnimatorCompatMap == null) {
                mViewPropertyAnimatorCompatMap = new java.util.WeakHashMap<>();
            }
            android.support.v4.view.ViewPropertyAnimatorCompat vpa = mViewPropertyAnimatorCompatMap.get(view);
            if (vpa == null) {
                vpa = new android.support.v4.view.ViewPropertyAnimatorCompat(view);
                mViewPropertyAnimatorCompatMap.put(view, vpa);
            }
            return vpa;
        }

        @java.lang.Override
        public void setFitsSystemWindows(android.view.View view, boolean fitSystemWindows) {
            android.support.v4.view.ViewCompatICS.setFitsSystemWindows(view, fitSystemWindows);
        }
    }

    static class ICSMr1ViewCompatImpl extends android.support.v4.view.ViewCompat.ICSViewCompatImpl {
        @java.lang.Override
        public boolean hasOnClickListeners(android.view.View view) {
            return android.support.v4.view.ViewCompatICSMr1.hasOnClickListeners(view);
        }
    }

    static class JBViewCompatImpl extends android.support.v4.view.ViewCompat.ICSMr1ViewCompatImpl {
        @java.lang.Override
        public boolean hasTransientState(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.hasTransientState(view);
        }

        @java.lang.Override
        public void setHasTransientState(android.view.View view, boolean hasTransientState) {
            android.support.v4.view.ViewCompatJB.setHasTransientState(view, hasTransientState);
        }

        @java.lang.Override
        public void postInvalidateOnAnimation(android.view.View view) {
            android.support.v4.view.ViewCompatJB.postInvalidateOnAnimation(view);
        }

        @java.lang.Override
        public void postInvalidateOnAnimation(android.view.View view, int left, int top, int right, int bottom) {
            android.support.v4.view.ViewCompatJB.postInvalidateOnAnimation(view, left, top, right, bottom);
        }

        @java.lang.Override
        public void postOnAnimation(android.view.View view, java.lang.Runnable action) {
            android.support.v4.view.ViewCompatJB.postOnAnimation(view, action);
        }

        @java.lang.Override
        public void postOnAnimationDelayed(android.view.View view, java.lang.Runnable action, long delayMillis) {
            android.support.v4.view.ViewCompatJB.postOnAnimationDelayed(view, action, delayMillis);
        }

        @java.lang.Override
        public int getImportantForAccessibility(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.getImportantForAccessibility(view);
        }

        @java.lang.Override
        public void setImportantForAccessibility(android.view.View view, int mode) {
            // IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS is not available
            // on this platform so replace with IMPORTANT_FOR_ACCESSIBILITY_NO
            // which is closer semantically.
            if (mode == android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS) {
                mode = android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO;
            }
            android.support.v4.view.ViewCompatJB.setImportantForAccessibility(view, mode);
        }

        @java.lang.Override
        public boolean performAccessibilityAction(android.view.View view, int action, android.os.Bundle arguments) {
            return android.support.v4.view.ViewCompatJB.performAccessibilityAction(view, action, arguments);
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(android.view.View view) {
            java.lang.Object compat = android.support.v4.view.ViewCompatJB.getAccessibilityNodeProvider(view);
            if (compat != null) {
                return new android.support.v4.view.accessibility.AccessibilityNodeProviderCompat(compat);
            }
            return null;
        }

        @java.lang.Override
        public android.view.ViewParent getParentForAccessibility(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.getParentForAccessibility(view);
        }

        @java.lang.Override
        public int getMinimumWidth(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.getMinimumWidth(view);
        }

        @java.lang.Override
        public int getMinimumHeight(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.getMinimumHeight(view);
        }

        @java.lang.Override
        public void requestApplyInsets(android.view.View view) {
            android.support.v4.view.ViewCompatJB.requestApplyInsets(view);
        }

        @java.lang.Override
        public boolean getFitsSystemWindows(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.getFitsSystemWindows(view);
        }

        @java.lang.Override
        public boolean hasOverlappingRendering(android.view.View view) {
            return android.support.v4.view.ViewCompatJB.hasOverlappingRendering(view);
        }

        @java.lang.Override
        public void setBackground(android.view.View view, android.graphics.drawable.Drawable background) {
            android.support.v4.view.ViewCompatJB.setBackground(view, background);
        }
    }

    static class JbMr1ViewCompatImpl extends android.support.v4.view.ViewCompat.JBViewCompatImpl {
        @java.lang.Override
        public int getLabelFor(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.getLabelFor(view);
        }

        @java.lang.Override
        public void setLabelFor(android.view.View view, int id) {
            android.support.v4.view.ViewCompatJellybeanMr1.setLabelFor(view, id);
        }

        @java.lang.Override
        public void setLayerPaint(android.view.View view, android.graphics.Paint paint) {
            android.support.v4.view.ViewCompatJellybeanMr1.setLayerPaint(view, paint);
        }

        @java.lang.Override
        public int getLayoutDirection(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.getLayoutDirection(view);
        }

        @java.lang.Override
        public void setLayoutDirection(android.view.View view, int layoutDirection) {
            android.support.v4.view.ViewCompatJellybeanMr1.setLayoutDirection(view, layoutDirection);
        }

        @java.lang.Override
        public int getPaddingStart(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.getPaddingStart(view);
        }

        @java.lang.Override
        public int getPaddingEnd(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.getPaddingEnd(view);
        }

        @java.lang.Override
        public void setPaddingRelative(android.view.View view, int start, int top, int end, int bottom) {
            android.support.v4.view.ViewCompatJellybeanMr1.setPaddingRelative(view, start, top, end, bottom);
        }

        @java.lang.Override
        public int getWindowSystemUiVisibility(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.getWindowSystemUiVisibility(view);
        }

        @java.lang.Override
        public boolean isPaddingRelative(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.isPaddingRelative(view);
        }

        @java.lang.Override
        public android.view.Display getDisplay(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr1.getDisplay(view);
        }
    }

    static class JbMr2ViewCompatImpl extends android.support.v4.view.ViewCompat.JbMr1ViewCompatImpl {
        @java.lang.Override
        public void setClipBounds(android.view.View view, android.graphics.Rect clipBounds) {
            android.support.v4.view.ViewCompatJellybeanMr2.setClipBounds(view, clipBounds);
        }

        @java.lang.Override
        public android.graphics.Rect getClipBounds(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr2.getClipBounds(view);
        }

        @java.lang.Override
        public boolean isInLayout(android.view.View view) {
            return android.support.v4.view.ViewCompatJellybeanMr2.isInLayout(view);
        }
    }

    static class KitKatViewCompatImpl extends android.support.v4.view.ViewCompat.JbMr2ViewCompatImpl {
        @java.lang.Override
        public int getAccessibilityLiveRegion(android.view.View view) {
            return android.support.v4.view.ViewCompatKitKat.getAccessibilityLiveRegion(view);
        }

        @java.lang.Override
        public void setAccessibilityLiveRegion(android.view.View view, int mode) {
            android.support.v4.view.ViewCompatKitKat.setAccessibilityLiveRegion(view, mode);
        }

        @java.lang.Override
        public void setImportantForAccessibility(android.view.View view, int mode) {
            android.support.v4.view.ViewCompatJB.setImportantForAccessibility(view, mode);
        }

        @java.lang.Override
        public boolean isLaidOut(android.view.View view) {
            return android.support.v4.view.ViewCompatKitKat.isLaidOut(view);
        }

        @java.lang.Override
        public boolean isLayoutDirectionResolved(android.view.View view) {
            return android.support.v4.view.ViewCompatKitKat.isLayoutDirectionResolved(view);
        }

        @java.lang.Override
        public boolean isAttachedToWindow(android.view.View view) {
            return android.support.v4.view.ViewCompatKitKat.isAttachedToWindow(view);
        }
    }

    static class LollipopViewCompatImpl extends android.support.v4.view.ViewCompat.KitKatViewCompatImpl {
        @java.lang.Override
        public void setTransitionName(android.view.View view, java.lang.String transitionName) {
            android.support.v4.view.ViewCompatLollipop.setTransitionName(view, transitionName);
        }

        @java.lang.Override
        public java.lang.String getTransitionName(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.getTransitionName(view);
        }

        @java.lang.Override
        public void requestApplyInsets(android.view.View view) {
            android.support.v4.view.ViewCompatLollipop.requestApplyInsets(view);
        }

        @java.lang.Override
        public void setElevation(android.view.View view, float elevation) {
            android.support.v4.view.ViewCompatLollipop.setElevation(view, elevation);
        }

        @java.lang.Override
        public float getElevation(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.getElevation(view);
        }

        @java.lang.Override
        public void setTranslationZ(android.view.View view, float translationZ) {
            android.support.v4.view.ViewCompatLollipop.setTranslationZ(view, translationZ);
        }

        @java.lang.Override
        public float getTranslationZ(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.getTranslationZ(view);
        }

        @java.lang.Override
        public void setOnApplyWindowInsetsListener(android.view.View view, final android.support.v4.view.OnApplyWindowInsetsListener listener) {
            if (listener == null) {
                android.support.v4.view.ViewCompatLollipop.setOnApplyWindowInsetsListener(view, null);
                return;
            }
            android.support.v4.view.ViewCompatLollipop.OnApplyWindowInsetsListenerBridge bridge = new android.support.v4.view.ViewCompatLollipop.OnApplyWindowInsetsListenerBridge() {
                @java.lang.Override
                public java.lang.Object onApplyWindowInsets(android.view.View v, java.lang.Object insets) {
                    android.support.v4.view.WindowInsetsCompat compatInsets = android.support.v4.view.WindowInsetsCompat.wrap(insets);
                    compatInsets = listener.onApplyWindowInsets(v, compatInsets);
                    return android.support.v4.view.WindowInsetsCompat.unwrap(compatInsets);
                }
            };
            android.support.v4.view.ViewCompatLollipop.setOnApplyWindowInsetsListener(view, bridge);
        }

        @java.lang.Override
        public void setNestedScrollingEnabled(android.view.View view, boolean enabled) {
            android.support.v4.view.ViewCompatLollipop.setNestedScrollingEnabled(view, enabled);
        }

        @java.lang.Override
        public boolean isNestedScrollingEnabled(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.isNestedScrollingEnabled(view);
        }

        @java.lang.Override
        public boolean startNestedScroll(android.view.View view, int axes) {
            return android.support.v4.view.ViewCompatLollipop.startNestedScroll(view, axes);
        }

        @java.lang.Override
        public void stopNestedScroll(android.view.View view) {
            android.support.v4.view.ViewCompatLollipop.stopNestedScroll(view);
        }

        @java.lang.Override
        public boolean hasNestedScrollingParent(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.hasNestedScrollingParent(view);
        }

        @java.lang.Override
        public boolean dispatchNestedScroll(android.view.View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            return android.support.v4.view.ViewCompatLollipop.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }

        @java.lang.Override
        public boolean dispatchNestedPreScroll(android.view.View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
            return android.support.v4.view.ViewCompatLollipop.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
        }

        @java.lang.Override
        public boolean dispatchNestedFling(android.view.View view, float velocityX, float velocityY, boolean consumed) {
            return android.support.v4.view.ViewCompatLollipop.dispatchNestedFling(view, velocityX, velocityY, consumed);
        }

        @java.lang.Override
        public boolean dispatchNestedPreFling(android.view.View view, float velocityX, float velocityY) {
            return android.support.v4.view.ViewCompatLollipop.dispatchNestedPreFling(view, velocityX, velocityY);
        }

        @java.lang.Override
        public boolean isImportantForAccessibility(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.isImportantForAccessibility(view);
        }

        @java.lang.Override
        public android.content.res.ColorStateList getBackgroundTintList(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.getBackgroundTintList(view);
        }

        @java.lang.Override
        public void setBackgroundTintList(android.view.View view, android.content.res.ColorStateList tintList) {
            android.support.v4.view.ViewCompatLollipop.setBackgroundTintList(view, tintList);
        }

        @java.lang.Override
        public void setBackgroundTintMode(android.view.View view, android.graphics.PorterDuff.Mode mode) {
            android.support.v4.view.ViewCompatLollipop.setBackgroundTintMode(view, mode);
        }

        @java.lang.Override
        public android.graphics.PorterDuff.Mode getBackgroundTintMode(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.getBackgroundTintMode(view);
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
            return android.support.v4.view.WindowInsetsCompat.wrap(android.support.v4.view.ViewCompatLollipop.onApplyWindowInsets(v, android.support.v4.view.WindowInsetsCompat.unwrap(insets)));
        }

        @java.lang.Override
        public android.support.v4.view.WindowInsetsCompat dispatchApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
            return android.support.v4.view.WindowInsetsCompat.wrap(android.support.v4.view.ViewCompatLollipop.dispatchApplyWindowInsets(v, android.support.v4.view.WindowInsetsCompat.unwrap(insets)));
        }

        @java.lang.Override
        public float getZ(android.view.View view) {
            return android.support.v4.view.ViewCompatLollipop.getZ(view);
        }

        @java.lang.Override
        public void setZ(android.view.View view, float z) {
            android.support.v4.view.ViewCompatLollipop.setZ(view, z);
        }

        @java.lang.Override
        public void offsetLeftAndRight(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatLollipop.offsetLeftAndRight(view, offset);
        }

        @java.lang.Override
        public void offsetTopAndBottom(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatLollipop.offsetTopAndBottom(view, offset);
        }
    }

    static class MarshmallowViewCompatImpl extends android.support.v4.view.ViewCompat.LollipopViewCompatImpl {
        @java.lang.Override
        public void setScrollIndicators(android.view.View view, int indicators) {
            android.support.v4.view.ViewCompatMarshmallow.setScrollIndicators(view, indicators);
        }

        @java.lang.Override
        public void setScrollIndicators(android.view.View view, int indicators, int mask) {
            android.support.v4.view.ViewCompatMarshmallow.setScrollIndicators(view, indicators, mask);
        }

        @java.lang.Override
        public int getScrollIndicators(android.view.View view) {
            return android.support.v4.view.ViewCompatMarshmallow.getScrollIndicators(view);
        }

        @java.lang.Override
        public void offsetLeftAndRight(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatMarshmallow.offsetLeftAndRight(view, offset);
        }

        @java.lang.Override
        public void offsetTopAndBottom(android.view.View view, int offset) {
            android.support.v4.view.ViewCompatMarshmallow.offsetTopAndBottom(view, offset);
        }
    }

    static class Api24ViewCompatImpl extends android.support.v4.view.ViewCompat.MarshmallowViewCompatImpl {
        @java.lang.Override
        public void setPointerIcon(android.view.View view, android.support.v4.view.PointerIconCompat pointerIconCompat) {
            android.support.v4.view.ViewCompatApi24.setPointerIcon(view, pointerIconCompat.getPointerIcon());
        }
    }

    static final android.support.v4.view.ViewCompat.ViewCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (android.support.v4.os.BuildCompat.isAtLeastN()) {
            IMPL = new android.support.v4.view.ViewCompat.Api24ViewCompatImpl();
        } else
            if (version >= 23) {
                IMPL = new android.support.v4.view.ViewCompat.MarshmallowViewCompatImpl();
            } else
                if (version >= 21) {
                    IMPL = new android.support.v4.view.ViewCompat.LollipopViewCompatImpl();
                } else
                    if (version >= 19) {
                        IMPL = new android.support.v4.view.ViewCompat.KitKatViewCompatImpl();
                    } else
                        if (version >= 18) {
                            IMPL = new android.support.v4.view.ViewCompat.JbMr2ViewCompatImpl();
                        } else
                            if (version >= 17) {
                                IMPL = new android.support.v4.view.ViewCompat.JbMr1ViewCompatImpl();
                            } else
                                if (version >= 16) {
                                    IMPL = new android.support.v4.view.ViewCompat.JBViewCompatImpl();
                                } else
                                    if (version >= 15) {
                                        IMPL = new android.support.v4.view.ViewCompat.ICSMr1ViewCompatImpl();
                                    } else
                                        if (version >= 14) {
                                            IMPL = new android.support.v4.view.ViewCompat.ICSViewCompatImpl();
                                        } else
                                            if (version >= 11) {
                                                IMPL = new android.support.v4.view.ViewCompat.HCViewCompatImpl();
                                            } else {
                                                IMPL = new android.support.v4.view.ViewCompat.BaseViewCompatImpl();
                                            }









    }

    /**
     * Check if this view can be scrolled horizontally in a certain direction.
     *
     * @param v
     * 		The View against which to invoke the method.
     * @param direction
     * 		Negative to check scrolling left, positive to check scrolling right.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public static boolean canScrollHorizontally(android.view.View v, int direction) {
        return android.support.v4.view.ViewCompat.IMPL.canScrollHorizontally(v, direction);
    }

    /**
     * Check if this view can be scrolled vertically in a certain direction.
     *
     * @param v
     * 		The View against which to invoke the method.
     * @param direction
     * 		Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public static boolean canScrollVertically(android.view.View v, int direction) {
        return android.support.v4.view.ViewCompat.IMPL.canScrollVertically(v, direction);
    }

    /**
     * Returns the over-scroll mode for this view. The result will be
     * one of {@link #OVER_SCROLL_ALWAYS} (default), {@link #OVER_SCROLL_IF_CONTENT_SCROLLS}
     * (allow over-scrolling only if the view content is larger than the container),
     * or {@link #OVER_SCROLL_NEVER}.
     *
     * @param v
     * 		The View against which to invoke the method.
     * @return This view's over-scroll mode.
     * @deprecated Call {@link View#getOverScrollMode()} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    @android.support.v4.view.ViewCompat.OverScroll
    public static int getOverScrollMode(android.view.View v) {
        // noinspection ResourceType
        return v.getOverScrollMode();
    }

    /**
     * Set the over-scroll mode for this view. Valid over-scroll modes are
     * {@link #OVER_SCROLL_ALWAYS} (default), {@link #OVER_SCROLL_IF_CONTENT_SCROLLS}
     * (allow over-scrolling only if the view content is larger than the container),
     * or {@link #OVER_SCROLL_NEVER}.
     *
     * Setting the over-scroll mode of a view will have an effect only if the
     * view is capable of scrolling.
     *
     * @param v
     * 		The View against which to invoke the method.
     * @param overScrollMode
     * 		The new over-scroll mode for this view.
     * @deprecated Call {@link View#setOverScrollMode(int)} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static void setOverScrollMode(android.view.View v, @android.support.v4.view.ViewCompat.OverScroll
    int overScrollMode) {
        v.setOverScrollMode(overScrollMode);
    }

    /**
     * Called from {@link View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)}
     * giving a chance to this View to populate the accessibility event with its
     * text content. While this method is free to modify event
     * attributes other than text content, doing so should normally be performed in
     * {@link View#onInitializeAccessibilityEvent(AccessibilityEvent)}.
     * <p>
     * Example: Adding formatted date string to an accessibility event in addition
     *          to the text added by the super implementation:
     * <pre> public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
     *     super.onPopulateAccessibilityEvent(event);
     *     final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY;
     *     String selectedDateUtterance = DateUtils.formatDateTime(mContext,
     *         mCurrentDate.getTimeInMillis(), flags);
     *     event.getText().add(selectedDateUtterance);
     * }</pre>
     * <p>
     * If an {@link AccessibilityDelegateCompat} has been specified via calling
     * {@link ViewCompat#setAccessibilityDelegate(View, AccessibilityDelegateCompat)} its
     * {@link AccessibilityDelegateCompat#onPopulateAccessibilityEvent(View, AccessibilityEvent)}
     * is responsible for handling this call.
     * </p>
     * <p class="note"><strong>Note:</strong> Always call the super implementation before adding
     * information to the event, in case the default implementation has basic information to add.
     * </p>
     *
     * @param v
     * 		The View against which to invoke the method.
     * @param event
     * 		The accessibility event which to populate.
     * @see View#sendAccessibilityEvent(int)
     * @see View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)
     */
    public static void onPopulateAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
        android.support.v4.view.ViewCompat.IMPL.onPopulateAccessibilityEvent(v, event);
    }

    /**
     * Initializes an {@link AccessibilityEvent} with information about
     * this View which is the event source. In other words, the source of
     * an accessibility event is the view whose state change triggered firing
     * the event.
     * <p>
     * Example: Setting the password property of an event in addition
     *          to properties set by the super implementation:
     * <pre> public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
     *     super.onInitializeAccessibilityEvent(event);
     *     event.setPassword(true);
     * }</pre>
     * <p>
     * If an {@link AccessibilityDelegateCompat} has been specified via calling
     * {@link ViewCompat#setAccessibilityDelegate(View, AccessibilityDelegateCompat)}, its
     * {@link AccessibilityDelegateCompat#onInitializeAccessibilityEvent(View, AccessibilityEvent)}
     * is responsible for handling this call.
     *
     * @param v
     * 		The View against which to invoke the method.
     * @param event
     * 		The event to initialize.
     * @see View#sendAccessibilityEvent(int)
     * @see View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)
     */
    public static void onInitializeAccessibilityEvent(android.view.View v, android.view.accessibility.AccessibilityEvent event) {
        android.support.v4.view.ViewCompat.IMPL.onInitializeAccessibilityEvent(v, event);
    }

    /**
     * Initializes an {@link AccessibilityNodeInfoCompat} with information
     * about this view. The base implementation sets:
     * <ul>
     * <li>{@link AccessibilityNodeInfoCompat#setParent(View)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setBoundsInParent(Rect)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setBoundsInScreen(Rect)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setPackageName(CharSequence)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setClassName(CharSequence)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setContentDescription(CharSequence)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setEnabled(boolean)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setClickable(boolean)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setFocusable(boolean)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setFocused(boolean)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setLongClickable(boolean)},</li>
     * <li>{@link AccessibilityNodeInfoCompat#setSelected(boolean)},</li>
     * </ul>
     * <p>
     * If an {@link AccessibilityDelegateCompat} has been specified via calling
     * {@link ViewCompat#setAccessibilityDelegate(View, AccessibilityDelegateCompat)}, its
     * {@link AccessibilityDelegateCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)}
     * method is responsible for handling this call.
     *
     * @param v
     * 		The View against which to invoke the method.
     * @param info
     * 		The instance to initialize.
     */
    public static void onInitializeAccessibilityNodeInfo(android.view.View v, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        android.support.v4.view.ViewCompat.IMPL.onInitializeAccessibilityNodeInfo(v, info);
    }

    /**
     * Sets a delegate for implementing accessibility support via composition
     * (as opposed to inheritance). For more details, see
     * {@link AccessibilityDelegateCompat}.
     * <p>
     * On platform versions prior to API 14, this method is a no-op.
     * <p>
     * <strong>Note:</strong> On platform versions prior to
     * {@link android.os.Build.VERSION_CODES#M API 23}, delegate methods on
     * views in the {@code android.widget.*} package are called <i>before</i>
     * host methods. This prevents certain properties such as class name from
     * being modified by overriding
     * {@link AccessibilityDelegateCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)},
     * as any changes will be overwritten by the host class.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#M API 23}, delegate
     * methods are called <i>after</i> host methods, which all properties to be
     * modified without being overwritten by the host class.
     *
     * @param delegate
     * 		the object to which accessibility method calls should be
     * 		delegated
     * @see AccessibilityDelegateCompat
     */
    public static void setAccessibilityDelegate(android.view.View v, android.support.v4.view.AccessibilityDelegateCompat delegate) {
        android.support.v4.view.ViewCompat.IMPL.setAccessibilityDelegate(v, delegate);
    }

    /**
     * Checks whether provided View has an accessibility delegate attached to it.
     *
     * @param v
     * 		The View instance to check
     * @return True if the View has an accessibility delegate
     */
    public static boolean hasAccessibilityDelegate(android.view.View v) {
        return android.support.v4.view.ViewCompat.IMPL.hasAccessibilityDelegate(v);
    }

    /**
     * Indicates whether the view is currently tracking transient state that the
     * app should not need to concern itself with saving and restoring, but that
     * the framework should take special note to preserve when possible.
     *
     * @param view
     * 		View to check for transient state
     * @return true if the view has transient state
     */
    public static boolean hasTransientState(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.hasTransientState(view);
    }

    /**
     * Set whether this view is currently tracking transient state that the
     * framework should attempt to preserve when possible.
     *
     * @param view
     * 		View tracking transient state
     * @param hasTransientState
     * 		true if this view has transient state
     */
    public static void setHasTransientState(android.view.View view, boolean hasTransientState) {
        android.support.v4.view.ViewCompat.IMPL.setHasTransientState(view, hasTransientState);
    }

    /**
     * <p>Cause an invalidate to happen on the next animation time step, typically the
     * next display frame.</p>
     *
     * <p>This method can be invoked from outside of the UI thread
     * only when this View is attached to a window.</p>
     *
     * @param view
     * 		View to invalidate
     */
    public static void postInvalidateOnAnimation(android.view.View view) {
        android.support.v4.view.ViewCompat.IMPL.postInvalidateOnAnimation(view);
    }

    /**
     * <p>Cause an invalidate of the specified area to happen on the next animation
     * time step, typically the next display frame.</p>
     *
     * <p>This method can be invoked from outside of the UI thread
     * only when this View is attached to a window.</p>
     *
     * @param view
     * 		View to invalidate
     * @param left
     * 		The left coordinate of the rectangle to invalidate.
     * @param top
     * 		The top coordinate of the rectangle to invalidate.
     * @param right
     * 		The right coordinate of the rectangle to invalidate.
     * @param bottom
     * 		The bottom coordinate of the rectangle to invalidate.
     */
    public static void postInvalidateOnAnimation(android.view.View view, int left, int top, int right, int bottom) {
        android.support.v4.view.ViewCompat.IMPL.postInvalidateOnAnimation(view, left, top, right, bottom);
    }

    /**
     * <p>Causes the Runnable to execute on the next animation time step.
     * The runnable will be run on the user interface thread.</p>
     *
     * <p>This method can be invoked from outside of the UI thread
     * only when this View is attached to a window.</p>
     *
     * @param view
     * 		View to post this Runnable to
     * @param action
     * 		The Runnable that will be executed.
     */
    public static void postOnAnimation(android.view.View view, java.lang.Runnable action) {
        android.support.v4.view.ViewCompat.IMPL.postOnAnimation(view, action);
    }

    /**
     * <p>Causes the Runnable to execute on the next animation time step,
     * after the specified amount of time elapses.
     * The runnable will be run on the user interface thread.</p>
     *
     * <p>This method can be invoked from outside of the UI thread
     * only when this View is attached to a window.</p>
     *
     * @param view
     * 		The view to post this Runnable to
     * @param action
     * 		The Runnable that will be executed.
     * @param delayMillis
     * 		The delay (in milliseconds) until the Runnable
     * 		will be executed.
     */
    public static void postOnAnimationDelayed(android.view.View view, java.lang.Runnable action, long delayMillis) {
        android.support.v4.view.ViewCompat.IMPL.postOnAnimationDelayed(view, action, delayMillis);
    }

    /**
     * Gets the mode for determining whether this View is important for accessibility
     * which is if it fires accessibility events and if it is reported to
     * accessibility services that query the screen.
     *
     * @param view
     * 		The view whose property to get.
     * @return The mode for determining whether a View is important for accessibility.
     * @see #IMPORTANT_FOR_ACCESSIBILITY_YES
     * @see #IMPORTANT_FOR_ACCESSIBILITY_NO
     * @see #IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
     * @see #IMPORTANT_FOR_ACCESSIBILITY_AUTO
     */
    @android.support.v4.view.ViewCompat.ImportantForAccessibility
    public static int getImportantForAccessibility(android.view.View view) {
        // noinspection ResourceType
        return android.support.v4.view.ViewCompat.IMPL.getImportantForAccessibility(view);
    }

    /**
     * Sets how to determine whether this view is important for accessibility
     * which is if it fires accessibility events and if it is reported to
     * accessibility services that query the screen.
     * <p>
     * <em>Note:</em> If the current platform version does not support the
     *  {@link #IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS} mode, then
     *  {@link #IMPORTANT_FOR_ACCESSIBILITY_NO} will be used as it is the
     *  closest terms of semantics.
     * </p>
     *
     * @param view
     * 		The view whose property to set.
     * @param mode
     * 		How to determine whether this view is important for accessibility.
     * @see #IMPORTANT_FOR_ACCESSIBILITY_YES
     * @see #IMPORTANT_FOR_ACCESSIBILITY_NO
     * @see #IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
     * @see #IMPORTANT_FOR_ACCESSIBILITY_AUTO
     */
    public static void setImportantForAccessibility(android.view.View view, @android.support.v4.view.ViewCompat.ImportantForAccessibility
    int mode) {
        android.support.v4.view.ViewCompat.IMPL.setImportantForAccessibility(view, mode);
    }

    /**
     * Computes whether this view should be exposed for accessibility. In
     * general, views that are interactive or provide information are exposed
     * while views that serve only as containers are hidden.
     * <p>
     * If an ancestor of this view has importance
     * {@link #IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS}, this method
     * returns <code>false</code>.
     * <p>
     * Otherwise, the value is computed according to the view's
     * {@link #getImportantForAccessibility(View)} value:
     * <ol>
     * <li>{@link #IMPORTANT_FOR_ACCESSIBILITY_NO} or
     * {@link #IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS}, return <code>false
     * </code>
     * <li>{@link #IMPORTANT_FOR_ACCESSIBILITY_YES}, return <code>true</code>
     * <li>{@link #IMPORTANT_FOR_ACCESSIBILITY_AUTO}, return <code>true</code> if
     * view satisfies any of the following:
     * <ul>
     * <li>Is actionable, e.g. {@link View#isClickable()},
     * {@link View#isLongClickable()}, or {@link View#isFocusable()}
     * <li>Has an {@link AccessibilityDelegateCompat}
     * <li>Has an interaction listener, e.g. {@link View.OnTouchListener},
     * {@link View.OnKeyListener}, etc.
     * <li>Is an accessibility live region, e.g.
     * {@link #getAccessibilityLiveRegion(View)} is not
     * {@link #ACCESSIBILITY_LIVE_REGION_NONE}.
     * </ul>
     * </ol>
     * <p>
     * <em>Note:</em> Prior to API 21, this method will always return {@code true}.
     *
     * @return Whether the view is exposed for accessibility.
     * @see #setImportantForAccessibility(View, int)
     * @see #getImportantForAccessibility(View)
     */
    public static boolean isImportantForAccessibility(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isImportantForAccessibility(view);
    }

    /**
     * Performs the specified accessibility action on the view. For
     * possible accessibility actions look at {@link AccessibilityNodeInfoCompat}.
     * <p>
     * If an {@link AccessibilityDelegateCompat} has been specified via calling
     * {@link #setAccessibilityDelegate(View, AccessibilityDelegateCompat)} its
     * {@link AccessibilityDelegateCompat#performAccessibilityAction(View, int, Bundle)}
     * is responsible for handling this call.
     * </p>
     *
     * @param action
     * 		The action to perform.
     * @param arguments
     * 		Optional action arguments.
     * @return Whether the action was performed.
     */
    public static boolean performAccessibilityAction(android.view.View view, int action, android.os.Bundle arguments) {
        return android.support.v4.view.ViewCompat.IMPL.performAccessibilityAction(view, action, arguments);
    }

    /**
     * Gets the provider for managing a virtual view hierarchy rooted at this View
     * and reported to {@link android.accessibilityservice.AccessibilityService}s
     * that explore the window content.
     * <p>
     * If this method returns an instance, this instance is responsible for managing
     * {@link AccessibilityNodeInfoCompat}s describing the virtual sub-tree rooted at
     * this View including the one representing the View itself. Similarly the returned
     * instance is responsible for performing accessibility actions on any virtual
     * view or the root view itself.
     * </p>
     * <p>
     * If an {@link AccessibilityDelegateCompat} has been specified via calling
     * {@link #setAccessibilityDelegate(View, AccessibilityDelegateCompat)} its
     * {@link AccessibilityDelegateCompat#getAccessibilityNodeProvider(View)}
     * is responsible for handling this call.
     * </p>
     *
     * @param view
     * 		The view whose property to get.
     * @return The provider.
     * @see AccessibilityNodeProviderCompat
     */
    public static android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getAccessibilityNodeProvider(view);
    }

    /**
     * The opacity of the view. This is a value from 0 to 1, where 0 means the view is
     * completely transparent and 1 means the view is completely opaque.
     *
     * <p>By default this is 1.0f. Prior to API 11, the returned value is always 1.0f.
     *
     * @return The opacity of the view.
     */
    public static float getAlpha(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getAlpha(view);
    }

    /**
     * <p>Specifies the type of layer backing this view. The layer can be
     * {@link #LAYER_TYPE_NONE disabled}, {@link #LAYER_TYPE_SOFTWARE software} or
     * {@link #LAYER_TYPE_HARDWARE hardware}.</p>
     *
     * <p>A layer is associated with an optional {@link android.graphics.Paint}
     * instance that controls how the layer is composed on screen. The following
     * properties of the paint are taken into account when composing the layer:</p>
     * <ul>
     * <li>{@link android.graphics.Paint#getAlpha() Translucency (alpha)}</li>
     * <li>{@link android.graphics.Paint#getXfermode() Blending mode}</li>
     * <li>{@link android.graphics.Paint#getColorFilter() Color filter}</li>
     * </ul>
     *
     * <p>If this view has an alpha value set to < 1.0 by calling
     * setAlpha(float), the alpha value of the layer's paint is replaced by
     * this view's alpha value. Calling setAlpha(float) is therefore
     * equivalent to setting a hardware layer on this view and providing a paint with
     * the desired alpha value.<p>
     *
     * <p>Refer to the documentation of {@link #LAYER_TYPE_NONE disabled},
     * {@link #LAYER_TYPE_SOFTWARE software} and {@link #LAYER_TYPE_HARDWARE hardware}
     * for more information on when and how to use layers.</p>
     *
     * @param view
     * 		View to set the layer type for
     * @param layerType
     * 		The type of layer to use with this view, must be one of
     * 		{@link #LAYER_TYPE_NONE}, {@link #LAYER_TYPE_SOFTWARE} or
     * 		{@link #LAYER_TYPE_HARDWARE}
     * @param paint
     * 		The paint used to compose the layer. This argument is optional
     * 		and can be null. It is ignored when the layer type is
     * 		{@link #LAYER_TYPE_NONE}
     */
    public static void setLayerType(android.view.View view, @android.support.v4.view.ViewCompat.LayerType
    int layerType, android.graphics.Paint paint) {
        android.support.v4.view.ViewCompat.IMPL.setLayerType(view, layerType, paint);
    }

    /**
     * Indicates what type of layer is currently associated with this view. By default
     * a view does not have a layer, and the layer type is {@link #LAYER_TYPE_NONE}.
     * Refer to the documentation of
     * {@link #setLayerType(android.view.View, int, android.graphics.Paint)}
     * for more information on the different types of layers.
     *
     * @param view
     * 		The view to fetch the layer type from
     * @return {@link #LAYER_TYPE_NONE}, {@link #LAYER_TYPE_SOFTWARE} or
    {@link #LAYER_TYPE_HARDWARE}
     * @see #setLayerType(android.view.View, int, android.graphics.Paint)
     * @see #LAYER_TYPE_NONE
     * @see #LAYER_TYPE_SOFTWARE
     * @see #LAYER_TYPE_HARDWARE
     */
    @android.support.v4.view.ViewCompat.LayerType
    public static int getLayerType(android.view.View view) {
        // noinspection ResourceType
        return android.support.v4.view.ViewCompat.IMPL.getLayerType(view);
    }

    /**
     * Gets the id of a view for which a given view serves as a label for
     * accessibility purposes.
     *
     * @param view
     * 		The view on which to invoke the corresponding method.
     * @return The labeled view id.
     */
    public static int getLabelFor(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getLabelFor(view);
    }

    /**
     * Sets the id of a view for which a given view serves as a label for
     * accessibility purposes.
     *
     * @param view
     * 		The view on which to invoke the corresponding method.
     * @param labeledId
     * 		The labeled view id.
     */
    public static void setLabelFor(android.view.View view, @android.support.annotation.IdRes
    int labeledId) {
        android.support.v4.view.ViewCompat.IMPL.setLabelFor(view, labeledId);
    }

    /**
     * Updates the {@link Paint} object used with the current layer (used only if the current
     * layer type is not set to {@link #LAYER_TYPE_NONE}). Changed properties of the Paint
     * provided to {@link #setLayerType(android.view.View, int, android.graphics.Paint)}
     * will be used the next time the View is redrawn, but
     * {@link #setLayerPaint(android.view.View, android.graphics.Paint)}
     * must be called to ensure that the view gets redrawn immediately.
     *
     * <p>A layer is associated with an optional {@link android.graphics.Paint}
     * instance that controls how the layer is composed on screen. The following
     * properties of the paint are taken into account when composing the layer:</p>
     * <ul>
     * <li>{@link android.graphics.Paint#getAlpha() Translucency (alpha)}</li>
     * <li>{@link android.graphics.Paint#getXfermode() Blending mode}</li>
     * <li>{@link android.graphics.Paint#getColorFilter() Color filter}</li>
     * </ul>
     *
     * <p>If this view has an alpha value set to < 1.0 by calling
     * View#setAlpha(float), the alpha value of the layer's paint is replaced by
     * this view's alpha value. Calling View#setAlpha(float) is therefore
     * equivalent to setting a hardware layer on this view and providing a paint with
     * the desired alpha value.</p>
     *
     * @param view
     * 		View to set a layer paint for
     * @param paint
     * 		The paint used to compose the layer. This argument is optional
     * 		and can be null. It is ignored when the layer type is
     * 		{@link #LAYER_TYPE_NONE}
     * @see #setLayerType(View, int, android.graphics.Paint)
     */
    public static void setLayerPaint(android.view.View view, android.graphics.Paint paint) {
        android.support.v4.view.ViewCompat.IMPL.setLayerPaint(view, paint);
    }

    /**
     * Returns the resolved layout direction for this view.
     *
     * @param view
     * 		View to get layout direction for
     * @return {@link #LAYOUT_DIRECTION_RTL} if the layout direction is RTL or returns
    {@link #LAYOUT_DIRECTION_LTR} if the layout direction is not RTL.

    For compatibility, this will return {@link #LAYOUT_DIRECTION_LTR} if API version
    is lower than Jellybean MR1 (API 17)
     */
    @android.support.v4.view.ViewCompat.ResolvedLayoutDirectionMode
    public static int getLayoutDirection(android.view.View view) {
        // noinspection ResourceType
        return android.support.v4.view.ViewCompat.IMPL.getLayoutDirection(view);
    }

    /**
     * Set the layout direction for this view. This will propagate a reset of layout direction
     * resolution to the view's children and resolve layout direction for this view.
     *
     * @param view
     * 		View to set layout direction for
     * @param layoutDirection
     * 		the layout direction to set. Should be one of:
     * 		
     * 		{@link #LAYOUT_DIRECTION_LTR},
     * 		{@link #LAYOUT_DIRECTION_RTL},
     * 		{@link #LAYOUT_DIRECTION_INHERIT},
     * 		{@link #LAYOUT_DIRECTION_LOCALE}.
     * 		
     * 		Resolution will be done if the value is set to LAYOUT_DIRECTION_INHERIT. The resolution
     * 		proceeds up the parent chain of the view to get the value. If there is no parent, then it
     * 		will return the default {@link #LAYOUT_DIRECTION_LTR}.
     */
    public static void setLayoutDirection(android.view.View view, @android.support.v4.view.ViewCompat.LayoutDirectionMode
    int layoutDirection) {
        android.support.v4.view.ViewCompat.IMPL.setLayoutDirection(view, layoutDirection);
    }

    /**
     * Gets the parent for accessibility purposes. Note that the parent for
     * accessibility is not necessary the immediate parent. It is the first
     * predecessor that is important for accessibility.
     *
     * @param view
     * 		View to retrieve parent for
     * @return The parent for use in accessibility inspection
     */
    public static android.view.ViewParent getParentForAccessibility(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getParentForAccessibility(view);
    }

    /**
     * Indicates whether this View is opaque. An opaque View guarantees that it will
     * draw all the pixels overlapping its bounds using a fully opaque color.
     *
     * @return True if this View is guaranteed to be fully opaque, false otherwise.
     * @deprecated Use {@link View#isOpaque()} directly. This method will be
    removed in a future release.
     */
    @java.lang.Deprecated
    public static boolean isOpaque(android.view.View view) {
        return view.isOpaque();
    }

    /**
     * Utility to reconcile a desired size and state, with constraints imposed
     * by a MeasureSpec.  Will take the desired size, unless a different size
     * is imposed by the constraints.  The returned value is a compound integer,
     * with the resolved size in the {@link #MEASURED_SIZE_MASK} bits and
     * optionally the bit {@link #MEASURED_STATE_TOO_SMALL} set if the resulting
     * size is smaller than the size the view wants to be.
     *
     * @param size
     * 		How big the view wants to be
     * @param measureSpec
     * 		Constraints imposed by the parent
     * @return Size information bit mask as defined by
    {@link #MEASURED_SIZE_MASK} and {@link #MEASURED_STATE_TOO_SMALL}.
     */
    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        return android.support.v4.view.ViewCompat.IMPL.resolveSizeAndState(size, measureSpec, childMeasuredState);
    }

    /**
     * Return the full width measurement information for this view as computed
     * by the most recent call to {@link android.view.View#measure(int, int)}.
     * This result is a bit mask as defined by {@link #MEASURED_SIZE_MASK} and
     * {@link #MEASURED_STATE_TOO_SMALL}.
     * This should be used during measurement and layout calculations only. Use
     * {@link android.view.View#getWidth()} to see how wide a view is after layout.
     *
     * @return The measured width of this view as a bit mask.
     */
    public static int getMeasuredWidthAndState(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getMeasuredWidthAndState(view);
    }

    /**
     * Return the full height measurement information for this view as computed
     * by the most recent call to {@link android.view.View#measure(int, int)}.
     * This result is a bit mask as defined by {@link #MEASURED_SIZE_MASK} and
     * {@link #MEASURED_STATE_TOO_SMALL}.
     * This should be used during measurement and layout calculations only. Use
     * {@link android.view.View#getHeight()} to see how wide a view is after layout.
     *
     * @return The measured width of this view as a bit mask.
     */
    public static int getMeasuredHeightAndState(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getMeasuredHeightAndState(view);
    }

    /**
     * Return only the state bits of {@link #getMeasuredWidthAndState}
     * and {@link #getMeasuredHeightAndState}, combined into one integer.
     * The width component is in the regular bits {@link #MEASURED_STATE_MASK}
     * and the height component is at the shifted bits
     * {@link #MEASURED_HEIGHT_STATE_SHIFT}>>{@link #MEASURED_STATE_MASK}.
     */
    public static int getMeasuredState(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getMeasuredState(view);
    }

    /**
     * Merge two states as returned by {@link #getMeasuredState(View)}.
     *
     * @param curState
     * 		The current state as returned from a view or the result
     * 		of combining multiple views.
     * @param newState
     * 		The new view state to combine.
     * @return Returns a new integer reflecting the combination of the two
    states.
     */
    public static int combineMeasuredStates(int curState, int newState) {
        return android.support.v4.view.ViewCompat.IMPL.combineMeasuredStates(curState, newState);
    }

    /**
     * Gets the live region mode for the specified View.
     *
     * @param view
     * 		The view from which to obtain the live region mode
     * @return The live region mode for the view.
     * @see ViewCompat#setAccessibilityLiveRegion(View, int)
     */
    @android.support.v4.view.ViewCompat.AccessibilityLiveRegion
    public static int getAccessibilityLiveRegion(android.view.View view) {
        // noinspection ResourceType
        return android.support.v4.view.ViewCompat.IMPL.getAccessibilityLiveRegion(view);
    }

    /**
     * Sets the live region mode for the specified view. This indicates to
     * accessibility services whether they should automatically notify the user
     * about changes to the view's content description or text, or to the
     * content descriptions or text of the view's children (where applicable).
     * <p>
     * For example, in a login screen with a TextView that displays an "incorrect
     * password" notification, that view should be marked as a live region with
     * mode {@link #ACCESSIBILITY_LIVE_REGION_POLITE}.
     * <p>
     * To disable change notifications for this view, use
     * {@link #ACCESSIBILITY_LIVE_REGION_NONE}. This is the default live region
     * mode for most views.
     * <p>
     * To indicate that the user should be notified of changes, use
     * {@link #ACCESSIBILITY_LIVE_REGION_POLITE}.
     * <p>
     * If the view's changes should interrupt ongoing speech and notify the user
     * immediately, use {@link #ACCESSIBILITY_LIVE_REGION_ASSERTIVE}.
     *
     * @param view
     * 		The view on which to set the live region mode
     * @param mode
     * 		The live region mode for this view, one of:
     * 		<ul>
     * 		<li>{@link #ACCESSIBILITY_LIVE_REGION_NONE}
     * 		<li>{@link #ACCESSIBILITY_LIVE_REGION_POLITE}
     * 		<li>{@link #ACCESSIBILITY_LIVE_REGION_ASSERTIVE}
     * 		</ul>
     */
    public static void setAccessibilityLiveRegion(android.view.View view, @android.support.v4.view.ViewCompat.AccessibilityLiveRegion
    int mode) {
        android.support.v4.view.ViewCompat.IMPL.setAccessibilityLiveRegion(view, mode);
    }

    /**
     * Returns the start padding of the specified view depending on its resolved layout direction.
     * If there are inset and enabled scrollbars, this value may include the space
     * required to display the scrollbars as well.
     *
     * @param view
     * 		The view to get padding for
     * @return the start padding in pixels
     */
    public static int getPaddingStart(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getPaddingStart(view);
    }

    /**
     * Returns the end padding of the specified view depending on its resolved layout direction.
     * If there are inset and enabled scrollbars, this value may include the space
     * required to display the scrollbars as well.
     *
     * @param view
     * 		The view to get padding for
     * @return the end padding in pixels
     */
    public static int getPaddingEnd(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getPaddingEnd(view);
    }

    /**
     * Sets the relative padding. The view may add on the space required to display
     * the scrollbars, depending on the style and visibility of the scrollbars.
     * So the values returned from {@link #getPaddingStart}, {@link View#getPaddingTop},
     * {@link #getPaddingEnd} and {@link View#getPaddingBottom} may be different
     * from the values set in this call.
     *
     * @param view
     * 		The view on which to set relative padding
     * @param start
     * 		the start padding in pixels
     * @param top
     * 		the top padding in pixels
     * @param end
     * 		the end padding in pixels
     * @param bottom
     * 		the bottom padding in pixels
     */
    public static void setPaddingRelative(android.view.View view, int start, int top, int end, int bottom) {
        android.support.v4.view.ViewCompat.IMPL.setPaddingRelative(view, start, top, end, bottom);
    }

    /**
     * Notify a view that it is being temporarily detached.
     */
    public static void dispatchStartTemporaryDetach(android.view.View view) {
        android.support.v4.view.ViewCompat.IMPL.dispatchStartTemporaryDetach(view);
    }

    /**
     * Notify a view that its temporary detach has ended; the view is now reattached.
     */
    public static void dispatchFinishTemporaryDetach(android.view.View view) {
        android.support.v4.view.ViewCompat.IMPL.dispatchFinishTemporaryDetach(view);
    }

    /**
     * The horizontal location of this view relative to its {@link View#getLeft() left} position.
     * This position is post-layout, in addition to wherever the object's
     * layout placed it.
     *
     * <p>Prior to API 11 this will return 0.</p>
     *
     * @return The horizontal position of this view relative to its left position, in pixels.
     */
    public static float getTranslationX(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getTranslationX(view);
    }

    /**
     * The vertical location of this view relative to its {@link View#getTop() top} position.
     * This position is post-layout, in addition to wherever the object's
     * layout placed it.
     *
     * <p>Prior to API 11 this will return 0.</p>
     *
     * @return The vertical position of this view relative to its top position, in pixels.
     */
    public static float getTranslationY(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getTranslationY(view);
    }

    /**
     * The transform matrix of this view, which is calculated based on the current
     * rotation, scale, and pivot properties.
     * <p>
     * Prior to 11, this method will return {@code null}.
     *
     * @param view
     * 		The view whose Matrix will be returned
     * @return The current transform matrix for the view
     * @see #getRotation(View)
     * @see #getScaleX(View)
     * @see #getScaleY(View)
     * @see #getPivotX(View)
     * @see #getPivotY(View)
     */
    @android.support.annotation.Nullable
    public static android.graphics.Matrix getMatrix(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getMatrix(view);
    }

    /**
     * Returns the minimum width of the view.
     *
     * <p>Prior to API 16 this will return 0.</p>
     *
     * @return the minimum width the view will try to be.
     */
    public static int getMinimumWidth(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getMinimumWidth(view);
    }

    /**
     * Returns the minimum height of the view.
     *
     * <p>Prior to API 16 this will return 0.</p>
     *
     * @return the minimum height the view will try to be.
     */
    public static int getMinimumHeight(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getMinimumHeight(view);
    }

    /**
     * This method returns a ViewPropertyAnimator object, which can be used to animate
     * specific properties on this View.
     *
     * <p>Prior to API 14, this method will do nothing.</p>
     *
     * @return ViewPropertyAnimator The ViewPropertyAnimator associated with this View.
     */
    public static android.support.v4.view.ViewPropertyAnimatorCompat animate(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.animate(view);
    }

    /**
     * Sets the horizontal location of this view relative to its left position.
     * This effectively positions the object post-layout, in addition to wherever the object's
     * layout placed it.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The horizontal position of this view relative to its left position,
     * 		in pixels.
     */
    public static void setTranslationX(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setTranslationX(view, value);
    }

    /**
     * Sets the vertical location of this view relative to its top position.
     * This effectively positions the object post-layout, in addition to wherever the object's
     * layout placed it.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The vertical position of this view relative to its top position,
     * 		in pixels.
     * @unknown name android:translationY
     */
    public static void setTranslationY(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setTranslationY(view, value);
    }

    /**
     * <p>Sets the opacity of the view. This is a value from 0 to 1, where 0 means the view is
     * completely transparent and 1 means the view is completely opaque.</p>
     *
     * <p> Note that setting alpha to a translucent value (0 < alpha < 1) can have significant
     * performance implications, especially for large views. It is best to use the alpha property
     * sparingly and transiently, as in the case of fading animations.</p>
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The opacity of the view.
     */
    public static void setAlpha(android.view.View view, @android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    float value) {
        android.support.v4.view.ViewCompat.IMPL.setAlpha(view, value);
    }

    /**
     * Sets the visual x position of this view, in pixels. This is equivalent to setting the
     * {@link #setTranslationX(View, float) translationX} property to be the difference between
     * the x value passed in and the current left property of the view as determined
     * by the layout bounds.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The visual x position of this view, in pixels.
     */
    public static void setX(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setX(view, value);
    }

    /**
     * Sets the visual y position of this view, in pixels. This is equivalent to setting the
     * {@link #setTranslationY(View, float) translationY} property to be the difference between
     * the y value passed in and the current top property of the view as determined by the
     * layout bounds.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The visual y position of this view, in pixels.
     */
    public static void setY(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setY(view, value);
    }

    /**
     * Sets the degrees that the view is rotated around the pivot point. Increasing values
     * result in clockwise rotation.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The degrees of rotation.
     */
    public static void setRotation(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setRotation(view, value);
    }

    /**
     * Sets the degrees that the view is rotated around the horizontal axis through the pivot point.
     * Increasing values result in clockwise rotation from the viewpoint of looking down the
     * x axis.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The degrees of X rotation.
     */
    public static void setRotationX(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setRotationX(view, value);
    }

    /**
     * Sets the degrees that the view is rotated around the vertical axis through the pivot point.
     * Increasing values result in counter-clockwise rotation from the viewpoint of looking
     * down the y axis.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The degrees of Y rotation.
     */
    public static void setRotationY(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setRotationY(view, value);
    }

    /**
     * Sets the amount that the view is scaled in x around the pivot point, as a proportion of
     * the view's unscaled width. A value of 1 means that no scaling is applied.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The scaling factor.
     */
    public static void setScaleX(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setScaleX(view, value);
    }

    /**
     * Sets the amount that the view is scaled in Y around the pivot point, as a proportion of
     * the view's unscaled width. A value of 1 means that no scaling is applied.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The scaling factor.
     */
    public static void setScaleY(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setScaleY(view, value);
    }

    /**
     * The x location of the point around which the view is
     * {@link #setRotation(View, float) rotated} and {@link #setScaleX(View, float) scaled}.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     */
    public static float getPivotX(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getPivotX(view);
    }

    /**
     * Sets the x location of the point around which the view is
     * {@link #setRotation(View, float) rotated} and {@link #setScaleX(View, float) scaled}.
     * By default, the pivot point is centered on the object.
     * Setting this property disables this behavior and causes the view to use only the
     * explicitly set pivotX and pivotY values.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The x location of the pivot point.
     */
    public static void setPivotX(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setPivotX(view, value);
    }

    /**
     * The y location of the point around which the view is {@link #setRotation(View,
     * float) rotated} and {@link #setScaleY(View, float) scaled}.
     *
     * <p>Prior to API 11 this will return 0.</p>
     *
     * @return The y location of the pivot point.
     */
    public static float getPivotY(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getPivotY(view);
    }

    /**
     * Sets the y location of the point around which the view is
     * {@link #setRotation(View, float) rotated} and {@link #setScaleY(View, float) scaled}.
     * By default, the pivot point is centered on the object.
     * Setting this property disables this behavior and causes the view to use only the
     * explicitly set pivotX and pivotY values.
     *
     * <p>Prior to API 11 this will have no effect.</p>
     *
     * @param value
     * 		The y location of the pivot point.
     */
    public static void setPivotY(android.view.View view, float value) {
        android.support.v4.view.ViewCompat.IMPL.setPivotY(view, value);
    }

    public static float getRotation(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getRotation(view);
    }

    public static float getRotationX(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getRotationX(view);
    }

    public static float getRotationY(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getRotationY(view);
    }

    public static float getScaleX(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getScaleX(view);
    }

    public static float getScaleY(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getScaleY(view);
    }

    public static float getX(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getX(view);
    }

    public static float getY(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getY(view);
    }

    /**
     * Sets the base elevation of this view, in pixels.
     */
    public static void setElevation(android.view.View view, float elevation) {
        android.support.v4.view.ViewCompat.IMPL.setElevation(view, elevation);
    }

    /**
     * The base elevation of this view relative to its parent, in pixels.
     *
     * @return The base depth position of the view, in pixels.
     */
    public static float getElevation(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getElevation(view);
    }

    /**
     * Sets the depth location of this view relative to its {@link #getElevation(View) elevation}.
     */
    public static void setTranslationZ(android.view.View view, float translationZ) {
        android.support.v4.view.ViewCompat.IMPL.setTranslationZ(view, translationZ);
    }

    /**
     * The depth location of this view relative to its {@link #getElevation(View) elevation}.
     *
     * @return The depth of this view relative to its elevation.
     */
    public static float getTranslationZ(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getTranslationZ(view);
    }

    /**
     * Sets the name of the View to be used to identify Views in Transitions.
     * Names should be unique in the View hierarchy.
     *
     * @param view
     * 		The View against which to invoke the method.
     * @param transitionName
     * 		The name of the View to uniquely identify it for Transitions.
     */
    public static void setTransitionName(android.view.View view, java.lang.String transitionName) {
        android.support.v4.view.ViewCompat.IMPL.setTransitionName(view, transitionName);
    }

    /**
     * Returns the name of the View to be used to identify Views in Transitions.
     * Names should be unique in the View hierarchy.
     *
     * <p>This returns null if the View has not been given a name.</p>
     *
     * @param view
     * 		The View against which to invoke the method.
     * @return The name used of the View to be used to identify Views in Transitions or null
    if no name has been given.
     */
    public static java.lang.String getTransitionName(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getTransitionName(view);
    }

    /**
     * Returns the current system UI visibility that is currently set for the entire window.
     */
    public static int getWindowSystemUiVisibility(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getWindowSystemUiVisibility(view);
    }

    /**
     * Ask that a new dispatch of {@code View.onApplyWindowInsets(WindowInsets)} be performed. This
     * falls back to {@code View.requestFitSystemWindows()} where available.
     */
    public static void requestApplyInsets(android.view.View view) {
        android.support.v4.view.ViewCompat.IMPL.requestApplyInsets(view);
    }

    /**
     * Tells the ViewGroup whether to draw its children in the order defined by the method
     * {@code ViewGroup.getChildDrawingOrder(int, int)}.
     *
     * @param enabled
     * 		true if the order of the children when drawing is determined by
     * 		{@link ViewGroup#getChildDrawingOrder(int, int)}, false otherwise
     * 		
     * 		<p>Prior to API 7 this will have no effect.</p>
     */
    public static void setChildrenDrawingOrderEnabled(android.view.ViewGroup viewGroup, boolean enabled) {
        android.support.v4.view.ViewCompat.IMPL.setChildrenDrawingOrderEnabled(viewGroup, enabled);
    }

    /**
     * Returns true if this view should adapt to fit system window insets. This method will always
     * return false before API 16 (Jellybean).
     */
    public static boolean getFitsSystemWindows(android.view.View v) {
        return android.support.v4.view.ViewCompat.IMPL.getFitsSystemWindows(v);
    }

    /**
     * Sets whether or not this view should account for system screen decorations
     * such as the status bar and inset its content; that is, controlling whether
     * the default implementation of {@link View#fitSystemWindows(Rect)} will be
     * executed. See that method for more details.
     */
    public static void setFitsSystemWindows(android.view.View view, boolean fitSystemWindows) {
        android.support.v4.view.ViewCompat.IMPL.setFitsSystemWindows(view, fitSystemWindows);
    }

    /**
     * On API 11 devices and above, call <code>Drawable.jumpToCurrentState()</code>
     * on all Drawable objects associated with this view.
     * <p>
     * On API 21 and above, also calls <code>StateListAnimator#jumpToCurrentState()</code>
     * if there is a StateListAnimator attached to this view.
     */
    public static void jumpDrawablesToCurrentState(android.view.View v) {
        android.support.v4.view.ViewCompat.IMPL.jumpDrawablesToCurrentState(v);
    }

    /**
     * Set an {@link OnApplyWindowInsetsListener} to take over the policy for applying
     * window insets to this view. This will only take effect on devices with API 21 or above.
     */
    public static void setOnApplyWindowInsetsListener(android.view.View v, android.support.v4.view.OnApplyWindowInsetsListener listener) {
        android.support.v4.view.ViewCompat.IMPL.setOnApplyWindowInsetsListener(v, listener);
    }

    /**
     * Called when the view should apply {@link WindowInsetsCompat} according to its internal policy.
     *
     * <p>Clients may supply an {@link OnApplyWindowInsetsListener} to a view. If one is set
     * it will be called during dispatch instead of this method. The listener may optionally
     * call this method from its own implementation if it wishes to apply the view's default
     * insets policy in addition to its own.</p>
     *
     * @param view
     * 		The View against which to invoke the method.
     * @param insets
     * 		Insets to apply
     * @return The supplied insets with any applied insets consumed
     */
    public static android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View view, android.support.v4.view.WindowInsetsCompat insets) {
        return android.support.v4.view.ViewCompat.IMPL.onApplyWindowInsets(view, insets);
    }

    /**
     * Request to apply the given window insets to this view or another view in its subtree.
     *
     * <p>This method should be called by clients wishing to apply insets corresponding to areas
     * obscured by window decorations or overlays. This can include the status and navigation bars,
     * action bars, input methods and more. New inset categories may be added in the future.
     * The method returns the insets provided minus any that were applied by this view or its
     * children.</p>
     *
     * @param insets
     * 		Insets to apply
     * @return The provided insets minus the insets that were consumed
     */
    public static android.support.v4.view.WindowInsetsCompat dispatchApplyWindowInsets(android.view.View view, android.support.v4.view.WindowInsetsCompat insets) {
        return android.support.v4.view.ViewCompat.IMPL.dispatchApplyWindowInsets(view, insets);
    }

    /**
     * Controls whether the entire hierarchy under this view will save its
     * state when a state saving traversal occurs from its parent.
     *
     * @param enabled
     * 		Set to false to <em>disable</em> state saving, or true
     * 		(the default) to allow it.
     */
    public static void setSaveFromParentEnabled(android.view.View v, boolean enabled) {
        android.support.v4.view.ViewCompat.IMPL.setSaveFromParentEnabled(v, enabled);
    }

    /**
     * Changes the activated state of this view. A view can be activated or not.
     * Note that activation is not the same as selection.  Selection is
     * a transient property, representing the view (hierarchy) the user is
     * currently interacting with.  Activation is a longer-term state that the
     * user can move views in and out of.
     *
     * @param activated
     * 		true if the view must be activated, false otherwise
     */
    public static void setActivated(android.view.View view, boolean activated) {
        android.support.v4.view.ViewCompat.IMPL.setActivated(view, activated);
    }

    /**
     * Returns whether this View has content which overlaps.
     *
     * <p>This function, intended to be overridden by specific View types, is an optimization when
     * alpha is set on a view. If rendering overlaps in a view with alpha < 1, that view is drawn to
     * an offscreen buffer and then composited into place, which can be expensive. If the view has
     * no overlapping rendering, the view can draw each primitive with the appropriate alpha value
     * directly. An example of overlapping rendering is a TextView with a background image, such as
     * a Button. An example of non-overlapping rendering is a TextView with no background, or an
     * ImageView with only the foreground image. The default implementation returns true; subclasses
     * should override if they have cases which can be optimized.</p>
     *
     * @return true if the content in this view might overlap, false otherwise.
     */
    public static boolean hasOverlappingRendering(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.hasOverlappingRendering(view);
    }

    /**
     * Return if the padding as been set through relative values
     * {@code View.setPaddingRelative(int, int, int, int)} or thru
     *
     * @return true if the padding is relative or false if it is not.
     */
    public static boolean isPaddingRelative(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isPaddingRelative(view);
    }

    /**
     * Set the background of the {@code view} to a given Drawable, or remove the background. If the
     * background has padding, {@code view}'s padding is set to the background's padding. However,
     * when a background is removed, this View's padding isn't touched. If setting the padding is
     * desired, please use{@code setPadding(int, int, int, int)}.
     */
    public static void setBackground(android.view.View view, android.graphics.drawable.Drawable background) {
        android.support.v4.view.ViewCompat.IMPL.setBackground(view, background);
    }

    /**
     * Return the tint applied to the background drawable, if specified.
     * <p>
     * Only returns meaningful info when running on API v21 or newer, or if {@code view}
     * implements the {@code TintableBackgroundView} interface.
     */
    public static android.content.res.ColorStateList getBackgroundTintList(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getBackgroundTintList(view);
    }

    /**
     * Applies a tint to the background drawable.
     * <p>
     * This will always take effect when running on API v21 or newer. When running on platforms
     * previous to API v21, it will only take effect if {@code view} implement the
     * {@code TintableBackgroundView} interface.
     */
    public static void setBackgroundTintList(android.view.View view, android.content.res.ColorStateList tintList) {
        android.support.v4.view.ViewCompat.IMPL.setBackgroundTintList(view, tintList);
    }

    /**
     * Return the blending mode used to apply the tint to the background
     * drawable, if specified.
     * <p>
     * Only returns meaningful info when running on API v21 or newer, or if {@code view}
     * implements the {@code TintableBackgroundView} interface.
     */
    public static android.graphics.PorterDuff.Mode getBackgroundTintMode(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getBackgroundTintMode(view);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setBackgroundTintList(android.view.View, android.content.res.ColorStateList)} to
     * the background drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     * <p>
     * This will always take effect when running on API v21 or newer. When running on platforms
     * previous to API v21, it will only take effect if {@code view} implement the
     * {@code TintableBackgroundView} interface.
     */
    public static void setBackgroundTintMode(android.view.View view, android.graphics.PorterDuff.Mode mode) {
        android.support.v4.view.ViewCompat.IMPL.setBackgroundTintMode(view, mode);
    }

    // TODO: getters for various view properties (rotation, etc)
    /**
     * Enable or disable nested scrolling for this view.
     *
     * <p>If this property is set to true the view will be permitted to initiate nested
     * scrolling operations with a compatible parent view in the current hierarchy. If this
     * view does not implement nested scrolling this will have no effect. Disabling nested scrolling
     * while a nested scroll is in progress has the effect of
     * {@link #stopNestedScroll(View) stopping} the nested scroll.</p>
     *
     * @param enabled
     * 		true to enable nested scrolling, false to disable
     * @see #isNestedScrollingEnabled(View)
     */
    public static void setNestedScrollingEnabled(android.view.View view, boolean enabled) {
        android.support.v4.view.ViewCompat.IMPL.setNestedScrollingEnabled(view, enabled);
    }

    /**
     * Returns true if nested scrolling is enabled for this view.
     *
     * <p>If nested scrolling is enabled and this View class implementation supports it,
     * this view will act as a nested scrolling child view when applicable, forwarding data
     * about the scroll operation in progress to a compatible and cooperating nested scrolling
     * parent.</p>
     *
     * @return true if nested scrolling is enabled
     * @see #setNestedScrollingEnabled(View, boolean)
     */
    public static boolean isNestedScrollingEnabled(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isNestedScrollingEnabled(view);
    }

    /**
     * Begin a nestable scroll operation along the given axes.
     *
     * <p>A view starting a nested scroll promises to abide by the following contract:</p>
     *
     * <p>The view will call startNestedScroll upon initiating a scroll operation. In the case
     * of a touch scroll this corresponds to the initial {@link MotionEvent#ACTION_DOWN}.
     * In the case of touch scrolling the nested scroll will be terminated automatically in
     * the same manner as {@link ViewParent#requestDisallowInterceptTouchEvent(boolean)}.
     * In the event of programmatic scrolling the caller must explicitly call
     * {@link #stopNestedScroll(View)} to indicate the end of the nested scroll.</p>
     *
     * <p>If <code>startNestedScroll</code> returns true, a cooperative parent was found.
     * If it returns false the caller may ignore the rest of this contract until the next scroll.
     * Calling startNestedScroll while a nested scroll is already in progress will return true.</p>
     *
     * <p>At each incremental step of the scroll the caller should invoke
     * {@link #dispatchNestedPreScroll(View, int, int, int[], int[]) dispatchNestedPreScroll}
     * once it has calculated the requested scrolling delta. If it returns true the nested scrolling
     * parent at least partially consumed the scroll and the caller should adjust the amount it
     * scrolls by.</p>
     *
     * <p>After applying the remainder of the scroll delta the caller should invoke
     * {@link #dispatchNestedScroll(View, int, int, int, int, int[]) dispatchNestedScroll}, passing
     * both the delta consumed and the delta unconsumed. A nested scrolling parent may treat
     * these values differently. See
     * {@link NestedScrollingParent#onNestedScroll(View, int, int, int, int)}.
     * </p>
     *
     * @param axes
     * 		Flags consisting of a combination of {@link ViewCompat#SCROLL_AXIS_HORIZONTAL}
     * 		and/or {@link ViewCompat#SCROLL_AXIS_VERTICAL}.
     * @return true if a cooperative parent was found and nested scrolling has been enabled for
    the current gesture.
     * @see #stopNestedScroll(View)
     * @see #dispatchNestedPreScroll(View, int, int, int[], int[])
     * @see #dispatchNestedScroll(View, int, int, int, int, int[])
     */
    public static boolean startNestedScroll(android.view.View view, int axes) {
        return android.support.v4.view.ViewCompat.IMPL.startNestedScroll(view, axes);
    }

    /**
     * Stop a nested scroll in progress.
     *
     * <p>Calling this method when a nested scroll is not currently in progress is harmless.</p>
     *
     * @see #startNestedScroll(View, int)
     */
    public static void stopNestedScroll(android.view.View view) {
        android.support.v4.view.ViewCompat.IMPL.stopNestedScroll(view);
    }

    /**
     * Returns true if this view has a nested scrolling parent.
     *
     * <p>The presence of a nested scrolling parent indicates that this view has initiated
     * a nested scroll and it was accepted by an ancestor view further up the view hierarchy.</p>
     *
     * @return whether this view has a nested scrolling parent
     */
    public static boolean hasNestedScrollingParent(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.hasNestedScrollingParent(view);
    }

    /**
     * Dispatch one step of a nested scroll in progress.
     *
     * <p>Implementations of views that support nested scrolling should call this to report
     * info about a scroll in progress to the current nested scrolling parent. If a nested scroll
     * is not currently in progress or nested scrolling is not
     * {@link #isNestedScrollingEnabled(View) enabled} for this view this method does nothing.</p>
     *
     * <p>Compatible View implementations should also call
     * {@link #dispatchNestedPreScroll(View, int, int, int[], int[]) dispatchNestedPreScroll} before
     * consuming a component of the scroll event themselves.</p>
     *
     * @param dxConsumed
     * 		Horizontal distance in pixels consumed by this view during this scroll step
     * @param dyConsumed
     * 		Vertical distance in pixels consumed by this view during this scroll step
     * @param dxUnconsumed
     * 		Horizontal scroll distance in pixels not consumed by this view
     * @param dyUnconsumed
     * 		Horizontal scroll distance in pixels not consumed by this view
     * @param offsetInWindow
     * 		Optional. If not null, on return this will contain the offset
     * 		in local view coordinates of this view from before this operation
     * 		to after it completes. View implementations may use this to adjust
     * 		expected input coordinate tracking.
     * @return true if the event was dispatched, false if it could not be dispatched.
     * @see #dispatchNestedPreScroll(View, int, int, int[], int[])
     */
    public static boolean dispatchNestedScroll(android.view.View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return android.support.v4.view.ViewCompat.IMPL.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * Dispatch one step of a nested scroll in progress before this view consumes any portion of it.
     *
     * <p>Nested pre-scroll events are to nested scroll events what touch intercept is to touch.
     * <code>dispatchNestedPreScroll</code> offers an opportunity for the parent view in a nested
     * scrolling operation to consume some or all of the scroll operation before the child view
     * consumes it.</p>
     *
     * @param dx
     * 		Horizontal scroll distance in pixels
     * @param dy
     * 		Vertical scroll distance in pixels
     * @param consumed
     * 		Output. If not null, consumed[0] will contain the consumed component of dx
     * 		and consumed[1] the consumed dy.
     * @param offsetInWindow
     * 		Optional. If not null, on return this will contain the offset
     * 		in local view coordinates of this view from before this operation
     * 		to after it completes. View implementations may use this to adjust
     * 		expected input coordinate tracking.
     * @return true if the parent consumed some or all of the scroll delta
     * @see #dispatchNestedScroll(View, int, int, int, int, int[])
     */
    public static boolean dispatchNestedPreScroll(android.view.View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return android.support.v4.view.ViewCompat.IMPL.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
    }

    /**
     * Dispatch a fling to a nested scrolling parent.
     *
     * <p>This method should be used to indicate that a nested scrolling child has detected
     * suitable conditions for a fling. Generally this means that a touch scroll has ended with a
     * {@link VelocityTracker velocity} in the direction of scrolling that meets or exceeds
     * the {@link ViewConfiguration#getScaledMinimumFlingVelocity() minimum fling velocity}
     * along a scrollable axis.</p>
     *
     * <p>If a nested scrolling child view would normally fling but it is at the edge of
     * its own content, it can use this method to delegate the fling to its nested scrolling
     * parent instead. The parent may optionally consume the fling or observe a child fling.</p>
     *
     * @param velocityX
     * 		Horizontal fling velocity in pixels per second
     * @param velocityY
     * 		Vertical fling velocity in pixels per second
     * @param consumed
     * 		true if the child consumed the fling, false otherwise
     * @return true if the nested scrolling parent consumed or otherwise reacted to the fling
     */
    public static boolean dispatchNestedFling(android.view.View view, float velocityX, float velocityY, boolean consumed) {
        return android.support.v4.view.ViewCompat.IMPL.dispatchNestedFling(view, velocityX, velocityY, consumed);
    }

    /**
     * Dispatch a fling to a nested scrolling parent before it is processed by this view.
     *
     * <p>Nested pre-fling events are to nested fling events what touch intercept is to touch
     * and what nested pre-scroll is to nested scroll. <code>dispatchNestedPreFling</code>
     * offsets an opportunity for the parent view in a nested fling to fully consume the fling
     * before the child view consumes it. If this method returns <code>true</code>, a nested
     * parent view consumed the fling and this view should not scroll as a result.</p>
     *
     * <p>For a better user experience, only one view in a nested scrolling chain should consume
     * the fling at a time. If a parent view consumed the fling this method will return false.
     * Custom view implementations should account for this in two ways:</p>
     *
     * <ul>
     *     <li>If a custom view is paged and needs to settle to a fixed page-point, do not
     *     call <code>dispatchNestedPreFling</code>; consume the fling and settle to a valid
     *     position regardless.</li>
     *     <li>If a nested parent does consume the fling, this view should not scroll at all,
     *     even to settle back to a valid idle position.</li>
     * </ul>
     *
     * <p>Views should also not offer fling velocities to nested parent views along an axis
     * where scrolling is not currently supported; a {@link android.widget.ScrollView ScrollView}
     * should not offer a horizontal fling velocity to its parents since scrolling along that
     * axis is not permitted and carrying velocity along that motion does not make sense.</p>
     *
     * @param velocityX
     * 		Horizontal fling velocity in pixels per second
     * @param velocityY
     * 		Vertical fling velocity in pixels per second
     * @return true if a nested scrolling parent consumed the fling
     */
    public static boolean dispatchNestedPreFling(android.view.View view, float velocityX, float velocityY) {
        return android.support.v4.view.ViewCompat.IMPL.dispatchNestedPreFling(view, velocityX, velocityY);
    }

    /**
     * Returns whether the view hierarchy is currently undergoing a layout pass. This
     * information is useful to avoid situations such as calling {@link View#requestLayout()}
     * during a layout pass.
     * <p>
     * Compatibility:
     * <ul>
     *     <li>API &lt; 18: Always returns {@code false}</li>
     * </ul>
     *
     * @return whether the view hierarchy is currently undergoing a layout pass
     */
    public static boolean isInLayout(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isInLayout(view);
    }

    /**
     * Returns true if {@code view} has been through at least one layout since it
     * was last attached to or detached from a window.
     */
    public static boolean isLaidOut(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isLaidOut(view);
    }

    /**
     * Returns whether layout direction has been resolved.
     * <p>
     * Compatibility:
     * <ul>
     *     <li>API &lt; 19: Always returns {@code false}</li>
     * </ul>
     *
     * @return true if layout direction has been resolved.
     */
    public static boolean isLayoutDirectionResolved(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isLayoutDirectionResolved(view);
    }

    /**
     * The visual z position of this view, in pixels. This is equivalent to the
     * {@link #setTranslationZ(View, float) translationZ} property plus the current
     * {@link #getElevation(View) elevation} property.
     *
     * @return The visual z position of this view, in pixels.
     */
    public static float getZ(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getZ(view);
    }

    /**
     * Sets the visual z position of this view, in pixels. This is equivalent to setting the
     * {@link #setTranslationZ(View, float) translationZ} property to be the difference between
     * the x value passed in and the current {@link #getElevation(View) elevation} property.
     * <p>
     * Compatibility:
     * <ul>
     *     <li>API &lt; 21: No-op
     * </ul>
     *
     * @param z
     * 		The visual z position of this view, in pixels.
     */
    public static void setZ(android.view.View view, float z) {
        android.support.v4.view.ViewCompat.IMPL.setZ(view, z);
    }

    /**
     * Offset this view's vertical location by the specified number of pixels.
     *
     * @param offset
     * 		the number of pixels to offset the view by
     */
    public static void offsetTopAndBottom(android.view.View view, int offset) {
        android.support.v4.view.ViewCompat.IMPL.offsetTopAndBottom(view, offset);
    }

    /**
     * Offset this view's horizontal location by the specified amount of pixels.
     *
     * @param offset
     * 		the number of pixels to offset the view by
     */
    public static void offsetLeftAndRight(android.view.View view, int offset) {
        android.support.v4.view.ViewCompat.IMPL.offsetLeftAndRight(view, offset);
    }

    /**
     * Sets a rectangular area on this view to which the view will be clipped
     * when it is drawn. Setting the value to null will remove the clip bounds
     * and the view will draw normally, using its full bounds.
     *
     * <p>Prior to API 18 this does nothing.</p>
     *
     * @param view
     * 		The view to set clipBounds.
     * @param clipBounds
     * 		The rectangular area, in the local coordinates of
     * 		this view, to which future drawing operations will be clipped.
     */
    public static void setClipBounds(android.view.View view, android.graphics.Rect clipBounds) {
        android.support.v4.view.ViewCompat.IMPL.setClipBounds(view, clipBounds);
    }

    /**
     * Returns a copy of the current {@link #setClipBounds(View, Rect)}.
     *
     * <p>Prior to API 18 this will return null.</p>
     *
     * @return A copy of the current clip bounds if clip bounds are set,
    otherwise null.
     */
    public static android.graphics.Rect getClipBounds(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getClipBounds(view);
    }

    /**
     * Returns true if the provided view is currently attached to a window.
     */
    public static boolean isAttachedToWindow(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.isAttachedToWindow(view);
    }

    /**
     * Returns whether the provided view has an attached {@link View.OnClickListener}.
     *
     * @return true if there is a listener, false if there is none.
     */
    public static boolean hasOnClickListeners(android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.hasOnClickListeners(view);
    }

    /**
     * Sets the state of all scroll indicators.
     * <p>
     * See {@link #setScrollIndicators(View, int, int)} for usage information.
     *
     * @param indicators
     * 		a bitmask of indicators that should be enabled, or
     * 		{@code 0} to disable all indicators
     * @see #setScrollIndicators(View, int, int)
     * @see #getScrollIndicators(View)
     */
    public static void setScrollIndicators(@android.support.annotation.NonNull
    android.view.View view, @android.support.v4.view.ViewCompat.ScrollIndicators
    int indicators) {
        android.support.v4.view.ViewCompat.IMPL.setScrollIndicators(view, indicators);
    }

    /**
     * Sets the state of the scroll indicators specified by the mask. To change
     * all scroll indicators at once, see {@link #setScrollIndicators(View, int)}.
     * <p>
     * When a scroll indicator is enabled, it will be displayed if the view
     * can scroll in the direction of the indicator.
     * <p>
     * Multiple indicator types may be enabled or disabled by passing the
     * logical OR of the desired types. If multiple types are specified, they
     * will all be set to the same enabled state.
     * <p>
     * For example, to enable the top scroll indicatorExample: {@code setScrollIndicators}
     *
     * @param indicators
     * 		the indicator direction, or the logical OR of multiple
     * 		indicator directions. One or more of:
     * 		<ul>
     * 		<li>{@link #SCROLL_INDICATOR_TOP}</li>
     * 		<li>{@link #SCROLL_INDICATOR_BOTTOM}</li>
     * 		<li>{@link #SCROLL_INDICATOR_LEFT}</li>
     * 		<li>{@link #SCROLL_INDICATOR_RIGHT}</li>
     * 		<li>{@link #SCROLL_INDICATOR_START}</li>
     * 		<li>{@link #SCROLL_INDICATOR_END}</li>
     * 		</ul>
     * @see #setScrollIndicators(View, int)
     * @see #getScrollIndicators(View)
     */
    public static void setScrollIndicators(@android.support.annotation.NonNull
    android.view.View view, @android.support.v4.view.ViewCompat.ScrollIndicators
    int indicators, @android.support.v4.view.ViewCompat.ScrollIndicators
    int mask) {
        android.support.v4.view.ViewCompat.IMPL.setScrollIndicators(view, indicators, mask);
    }

    /**
     * Returns a bitmask representing the enabled scroll indicators.
     * <p>
     * For example, if the top and left scroll indicators are enabled and all
     * other indicators are disabled, the return value will be
     * {@code ViewCompat.SCROLL_INDICATOR_TOP | ViewCompat.SCROLL_INDICATOR_LEFT}.
     * <p>
     * To check whether the bottom scroll indicator is enabled, use the value
     * of {@code (ViewCompat.getScrollIndicators(view) & ViewCompat.SCROLL_INDICATOR_BOTTOM) != 0}.
     *
     * @return a bitmask representing the enabled scroll indicators
     */
    public static int getScrollIndicators(@android.support.annotation.NonNull
    android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getScrollIndicators(view);
    }

    /**
     * Set the pointer icon for the current view.
     *
     * @param pointerIcon
     * 		A PointerIconCompat instance which will be shown when the mouse hovers.
     */
    public static void setPointerIcon(@android.support.annotation.NonNull
    android.view.View view, android.support.v4.view.PointerIconCompat pointerIcon) {
        android.support.v4.view.ViewCompat.IMPL.setPointerIcon(view, pointerIcon);
    }

    /**
     * Gets the logical display to which the view's window has been attached.
     * <p>
     * Compatibility:
     * <ul>
     * <li>API &lt; 17: Returns the default display when the view is attached. Otherwise, null.
     * </ul>
     *
     * @return The logical display, or null if the view is not currently attached to a window.
     */
    public static android.view.Display getDisplay(@android.support.annotation.NonNull
    android.view.View view) {
        return android.support.v4.view.ViewCompat.IMPL.getDisplay(view);
    }

    protected ViewCompat() {
    }
}

