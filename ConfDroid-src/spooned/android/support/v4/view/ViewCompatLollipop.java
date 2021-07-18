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
package android.support.v4.view;


class ViewCompatLollipop {
    public interface OnApplyWindowInsetsListenerBridge {
        java.lang.Object onApplyWindowInsets(android.view.View v, java.lang.Object insets);
    }

    private static java.lang.ThreadLocal<android.graphics.Rect> sThreadLocalRect;

    public static void setTransitionName(android.view.View view, java.lang.String transitionName) {
        view.setTransitionName(transitionName);
    }

    public static java.lang.String getTransitionName(android.view.View view) {
        return view.getTransitionName();
    }

    public static void requestApplyInsets(android.view.View view) {
        view.requestApplyInsets();
    }

    public static void setElevation(android.view.View view, float elevation) {
        view.setElevation(elevation);
    }

    public static float getElevation(android.view.View view) {
        return view.getElevation();
    }

    public static void setTranslationZ(android.view.View view, float translationZ) {
        view.setTranslationZ(translationZ);
    }

    public static float getTranslationZ(android.view.View view) {
        return view.getTranslationZ();
    }

    public static void setOnApplyWindowInsetsListener(android.view.View view, final android.support.v4.view.ViewCompatLollipop.OnApplyWindowInsetsListenerBridge bridge) {
        if (bridge == null) {
            view.setOnApplyWindowInsetsListener(null);
        } else {
            view.setOnApplyWindowInsetsListener(new android.view.View.OnApplyWindowInsetsListener() {
                @java.lang.Override
                public android.view.WindowInsets onApplyWindowInsets(android.view.View view, android.view.WindowInsets insets) {
                    return ((android.view.WindowInsets) (bridge.onApplyWindowInsets(view, insets)));
                }
            });
        }
    }

    public static boolean isImportantForAccessibility(android.view.View view) {
        return view.isImportantForAccessibility();
    }

    static android.content.res.ColorStateList getBackgroundTintList(android.view.View view) {
        return view.getBackgroundTintList();
    }

    static void setBackgroundTintList(android.view.View view, android.content.res.ColorStateList tintList) {
        view.setBackgroundTintList(tintList);
        if (android.os.Build.VERSION.SDK_INT == 21) {
            // Work around a bug in L that did not update the state of the background
            // after applying the tint
            android.graphics.drawable.Drawable background = view.getBackground();
            boolean hasTint = (view.getBackgroundTintList() != null) && (view.getBackgroundTintMode() != null);
            if ((background != null) && hasTint) {
                if (background.isStateful()) {
                    background.setState(view.getDrawableState());
                }
                view.setBackground(background);
            }
        }
    }

    static android.graphics.PorterDuff.Mode getBackgroundTintMode(android.view.View view) {
        return view.getBackgroundTintMode();
    }

    static void setBackgroundTintMode(android.view.View view, android.graphics.PorterDuff.Mode mode) {
        view.setBackgroundTintMode(mode);
        if (android.os.Build.VERSION.SDK_INT == 21) {
            // Work around a bug in L that did not update the state of the background
            // after applying the tint
            android.graphics.drawable.Drawable background = view.getBackground();
            boolean hasTint = (view.getBackgroundTintList() != null) && (view.getBackgroundTintMode() != null);
            if ((background != null) && hasTint) {
                if (background.isStateful()) {
                    background.setState(view.getDrawableState());
                }
                view.setBackground(background);
            }
        }
    }

    public static java.lang.Object onApplyWindowInsets(android.view.View v, java.lang.Object insets) {
        android.view.WindowInsets unwrapped = ((android.view.WindowInsets) (insets));
        android.view.WindowInsets result = v.onApplyWindowInsets(unwrapped);
        if (result != unwrapped) {
            insets = new android.view.WindowInsets(result);
        }
        return insets;
    }

    public static java.lang.Object dispatchApplyWindowInsets(android.view.View v, java.lang.Object insets) {
        android.view.WindowInsets unwrapped = ((android.view.WindowInsets) (insets));
        android.view.WindowInsets result = v.dispatchApplyWindowInsets(unwrapped);
        if (result != unwrapped) {
            insets = new android.view.WindowInsets(result);
        }
        return insets;
    }

    public static void setNestedScrollingEnabled(android.view.View view, boolean enabled) {
        view.setNestedScrollingEnabled(enabled);
    }

    public static boolean isNestedScrollingEnabled(android.view.View view) {
        return view.isNestedScrollingEnabled();
    }

    public static boolean startNestedScroll(android.view.View view, int axes) {
        return view.startNestedScroll(axes);
    }

    public static void stopNestedScroll(android.view.View view) {
        view.stopNestedScroll();
    }

    public static boolean hasNestedScrollingParent(android.view.View view) {
        return view.hasNestedScrollingParent();
    }

    public static boolean dispatchNestedScroll(android.view.View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return view.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public static boolean dispatchNestedPreScroll(android.view.View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return view.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public static boolean dispatchNestedFling(android.view.View view, float velocityX, float velocityY, boolean consumed) {
        return view.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public static boolean dispatchNestedPreFling(android.view.View view, float velocityX, float velocityY) {
        return view.dispatchNestedPreFling(velocityX, velocityY);
    }

    public static float getZ(android.view.View view) {
        return view.getZ();
    }

    public static void setZ(android.view.View view, float z) {
        view.setZ(z);
    }

    static void offsetTopAndBottom(final android.view.View view, final int offset) {
        final android.graphics.Rect parentRect = android.support.v4.view.ViewCompatLollipop.getEmptyTempRect();
        boolean needInvalidateWorkaround = false;
        final android.view.ViewParent parent = view.getParent();
        if (parent instanceof android.view.View) {
            final android.view.View p = ((android.view.View) (parent));
            parentRect.set(p.getLeft(), p.getTop(), p.getRight(), p.getBottom());
            // If the view currently does not currently intersect the parent (and is therefore
            // not displayed) we may need need to invalidate
            needInvalidateWorkaround = !parentRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
        // Now offset, invoking the API 11+ implementation (which contains it's own workarounds)
        android.support.v4.view.ViewCompatHC.offsetTopAndBottom(view, offset);
        // The view has now been offset, so let's intersect the Rect and invalidate where
        // the View is now displayed
        if (needInvalidateWorkaround && parentRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
            ((android.view.View) (parent)).invalidate(parentRect);
        }
    }

    static void offsetLeftAndRight(final android.view.View view, final int offset) {
        final android.graphics.Rect parentRect = android.support.v4.view.ViewCompatLollipop.getEmptyTempRect();
        boolean needInvalidateWorkaround = false;
        final android.view.ViewParent parent = view.getParent();
        if (parent instanceof android.view.View) {
            final android.view.View p = ((android.view.View) (parent));
            parentRect.set(p.getLeft(), p.getTop(), p.getRight(), p.getBottom());
            // If the view currently does not currently intersect the parent (and is therefore
            // not displayed) we may need need to invalidate
            needInvalidateWorkaround = !parentRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
        // Now offset, invoking the API 11+ implementation (which contains it's own workarounds)
        android.support.v4.view.ViewCompatHC.offsetLeftAndRight(view, offset);
        // The view has now been offset, so let's intersect the Rect and invalidate where
        // the View is now displayed
        if (needInvalidateWorkaround && parentRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
            ((android.view.View) (parent)).invalidate(parentRect);
        }
    }

    private static android.graphics.Rect getEmptyTempRect() {
        if (android.support.v4.view.ViewCompatLollipop.sThreadLocalRect == null) {
            android.support.v4.view.ViewCompatLollipop.sThreadLocalRect = new java.lang.ThreadLocal<>();
        }
        android.graphics.Rect rect = android.support.v4.view.ViewCompatLollipop.sThreadLocalRect.get();
        if (rect == null) {
            rect = new android.graphics.Rect();
            android.support.v4.view.ViewCompatLollipop.sThreadLocalRect.set(rect);
        }
        rect.setEmpty();
        return rect;
    }
}

