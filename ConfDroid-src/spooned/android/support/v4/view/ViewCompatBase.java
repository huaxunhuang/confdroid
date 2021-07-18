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


class ViewCompatBase {
    private static final java.lang.String TAG = "ViewCompatBase";

    private static java.lang.reflect.Field sMinWidthField;

    private static boolean sMinWidthFieldFetched;

    private static java.lang.reflect.Field sMinHeightField;

    private static boolean sMinHeightFieldFetched;

    static android.content.res.ColorStateList getBackgroundTintList(android.view.View view) {
        return view instanceof android.support.v4.view.TintableBackgroundView ? ((android.support.v4.view.TintableBackgroundView) (view)).getSupportBackgroundTintList() : null;
    }

    static void setBackgroundTintList(android.view.View view, android.content.res.ColorStateList tintList) {
        if (view instanceof android.support.v4.view.TintableBackgroundView) {
            ((android.support.v4.view.TintableBackgroundView) (view)).setSupportBackgroundTintList(tintList);
        }
    }

    static android.graphics.PorterDuff.Mode getBackgroundTintMode(android.view.View view) {
        return view instanceof android.support.v4.view.TintableBackgroundView ? ((android.support.v4.view.TintableBackgroundView) (view)).getSupportBackgroundTintMode() : null;
    }

    static void setBackgroundTintMode(android.view.View view, android.graphics.PorterDuff.Mode mode) {
        if (view instanceof android.support.v4.view.TintableBackgroundView) {
            ((android.support.v4.view.TintableBackgroundView) (view)).setSupportBackgroundTintMode(mode);
        }
    }

    static boolean isLaidOut(android.view.View view) {
        return (view.getWidth() > 0) && (view.getHeight() > 0);
    }

    static int getMinimumWidth(android.view.View view) {
        if (!android.support.v4.view.ViewCompatBase.sMinWidthFieldFetched) {
            try {
                android.support.v4.view.ViewCompatBase.sMinWidthField = android.view.View.class.getDeclaredField("mMinWidth");
                android.support.v4.view.ViewCompatBase.sMinWidthField.setAccessible(true);
            } catch (java.lang.NoSuchFieldException e) {
                // Couldn't find the field. Abort!
            }
            android.support.v4.view.ViewCompatBase.sMinWidthFieldFetched = true;
        }
        if (android.support.v4.view.ViewCompatBase.sMinWidthField != null) {
            try {
                return ((int) (android.support.v4.view.ViewCompatBase.sMinWidthField.get(view)));
            } catch (java.lang.Exception e) {
                // Field get failed. Oh well...
            }
        }
        // We failed, return 0
        return 0;
    }

    static int getMinimumHeight(android.view.View view) {
        if (!android.support.v4.view.ViewCompatBase.sMinHeightFieldFetched) {
            try {
                android.support.v4.view.ViewCompatBase.sMinHeightField = android.view.View.class.getDeclaredField("mMinHeight");
                android.support.v4.view.ViewCompatBase.sMinHeightField.setAccessible(true);
            } catch (java.lang.NoSuchFieldException e) {
                // Couldn't find the field. Abort!
            }
            android.support.v4.view.ViewCompatBase.sMinHeightFieldFetched = true;
        }
        if (android.support.v4.view.ViewCompatBase.sMinHeightField != null) {
            try {
                return ((int) (android.support.v4.view.ViewCompatBase.sMinHeightField.get(view)));
            } catch (java.lang.Exception e) {
                // Field get failed. Oh well...
            }
        }
        // We failed, return 0
        return 0;
    }

    static boolean isAttachedToWindow(android.view.View view) {
        return view.getWindowToken() != null;
    }

    static void offsetTopAndBottom(android.view.View view, int offset) {
        final int currentTop = view.getTop();
        view.offsetTopAndBottom(offset);
        if (offset != 0) {
            // We need to manually invalidate pre-honeycomb
            final android.view.ViewParent parent = view.getParent();
            if (parent instanceof android.view.View) {
                final int absOffset = java.lang.Math.abs(offset);
                ((android.view.View) (parent)).invalidate(view.getLeft(), currentTop - absOffset, view.getRight(), (currentTop + view.getHeight()) + absOffset);
            } else {
                view.invalidate();
            }
        }
    }

    static void offsetLeftAndRight(android.view.View view, int offset) {
        final int currentLeft = view.getLeft();
        view.offsetLeftAndRight(offset);
        if (offset != 0) {
            // We need to manually invalidate pre-honeycomb
            final android.view.ViewParent parent = view.getParent();
            if (parent instanceof android.view.View) {
                final int absOffset = java.lang.Math.abs(offset);
                ((android.view.View) (parent)).invalidate(currentLeft - absOffset, view.getTop(), (currentLeft + view.getWidth()) + absOffset, view.getBottom());
            } else {
                view.invalidate();
            }
        }
    }

    static android.view.Display getDisplay(android.view.View view) {
        if (android.support.v4.view.ViewCompatBase.isAttachedToWindow(view)) {
            final android.view.WindowManager wm = ((android.view.WindowManager) (view.getContext().getSystemService(android.content.Context.WINDOW_SERVICE)));
            return wm.getDefaultDisplay();
        }
        return null;
    }
}

