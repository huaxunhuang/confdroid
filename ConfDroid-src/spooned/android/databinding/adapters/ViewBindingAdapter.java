/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.adapters;


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:backgroundTint", method = "setBackgroundTintList"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:fadeScrollbars", method = "setScrollbarFadingEnabled"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:getOutline", method = "setOutlineProvider"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:nextFocusForward", method = "setNextFocusForwardId"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:nextFocusLeft", method = "setNextFocusLeftId"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:nextFocusRight", method = "setNextFocusRightId"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:nextFocusUp", method = "setNextFocusUpId"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:nextFocusDown", method = "setNextFocusDownId"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:requiresFadingEdge", method = "setVerticalFadingEdgeEnabled"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:scrollbarDefaultDelayBeforeFade", method = "setScrollBarDefaultDelayBeforeFade"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:scrollbarFadeDuration", method = "setScrollBarFadeDuration"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:scrollbarSize", method = "setScrollBarSize"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:scrollbarStyle", method = "setScrollBarStyle"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:transformPivotX", method = "setPivotX"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:transformPivotY", method = "setPivotY"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onDrag", method = "setOnDragListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onClick", method = "setOnClickListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onApplyWindowInsets", method = "setOnApplyWindowInsetsListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onCreateContextMenu", method = "setOnCreateContextMenuListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onFocusChange", method = "setOnFocusChangeListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onGenericMotion", method = "setOnGenericMotionListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onHover", method = "setOnHoverListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onKey", method = "setOnKeyListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onLongClick", method = "setOnLongClickListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onSystemUiVisibilityChange", method = "setOnSystemUiVisibilityChangeListener"), @android.databinding.BindingMethod(type = android.view.View.class, attribute = "android:onTouch", method = "setOnTouchListener") })
public class ViewBindingAdapter {
    public static int FADING_EDGE_NONE = 0;

    public static int FADING_EDGE_HORIZONTAL = 1;

    public static int FADING_EDGE_VERTICAL = 2;

    @android.databinding.BindingAdapter({ "android:padding" })
    public static void setPadding(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        view.setPadding(padding, padding, padding, padding);
    }

    @android.databinding.BindingAdapter({ "android:paddingBottom" })
    public static void setPaddingBottom(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    @android.databinding.BindingAdapter({ "android:paddingEnd" })
    public static void setPaddingEnd(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop(), padding, view.getPaddingBottom());
        } else {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
        }
    }

    @android.databinding.BindingAdapter({ "android:paddingLeft" })
    public static void setPaddingLeft(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    @android.databinding.BindingAdapter({ "android:paddingRight" })
    public static void setPaddingRight(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
    }

    @android.databinding.BindingAdapter({ "android:paddingStart" })
    public static void setPaddingStart(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setPaddingRelative(padding, view.getPaddingTop(), view.getPaddingEnd(), view.getPaddingBottom());
        } else {
            view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    @android.databinding.BindingAdapter({ "android:paddingTop" })
    public static void setPaddingTop(android.view.View view, float paddingFloat) {
        final int padding = android.databinding.adapters.ViewBindingAdapter.pixelsToDimensionPixelSize(paddingFloat);
        view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
    }

    @android.databinding.BindingAdapter({ "android:requiresFadingEdge" })
    public static void setRequiresFadingEdge(android.view.View view, int value) {
        final boolean vertical = (value & android.databinding.adapters.ViewBindingAdapter.FADING_EDGE_VERTICAL) != 0;
        final boolean horizontal = (value & android.databinding.adapters.ViewBindingAdapter.FADING_EDGE_HORIZONTAL) != 0;
        view.setVerticalFadingEdgeEnabled(vertical);
        view.setHorizontalFadingEdgeEnabled(horizontal);
    }

    @android.databinding.BindingAdapter({ "android:onClickListener", "android:clickable" })
    public static void setClickListener(android.view.View view, android.view.View.OnClickListener clickListener, boolean clickable) {
        view.setOnClickListener(clickListener);
        view.setClickable(clickable);
    }

    @android.databinding.BindingAdapter({ "android:onClick", "android:clickable" })
    public static void setOnClick(android.view.View view, android.view.View.OnClickListener clickListener, boolean clickable) {
        view.setOnClickListener(clickListener);
        view.setClickable(clickable);
    }

    @android.databinding.BindingAdapter({ "android:onLongClickListener", "android:longClickable" })
    public static void setOnLongClickListener(android.view.View view, android.view.View.OnLongClickListener clickListener, boolean clickable) {
        view.setOnLongClickListener(clickListener);
        view.setLongClickable(clickable);
    }

    @android.databinding.BindingAdapter({ "android:onLongClick", "android:longClickable" })
    public static void setOnLongClick(android.view.View view, android.view.View.OnLongClickListener clickListener, boolean clickable) {
        view.setOnLongClickListener(clickListener);
        view.setLongClickable(clickable);
    }

    @android.databinding.BindingAdapter(value = { "android:onViewDetachedFromWindow", "android:onViewAttachedToWindow" }, requireAll = false)
    public static void setOnAttachStateChangeListener(android.view.View view, final android.databinding.adapters.ViewBindingAdapter.OnViewDetachedFromWindow detach, final android.databinding.adapters.ViewBindingAdapter.OnViewAttachedToWindow attach) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
            final android.view.View.OnAttachStateChangeListener newListener;
            if ((detach == null) && (attach == null)) {
                newListener = null;
            } else {
                newListener = new android.view.View.OnAttachStateChangeListener() {
                    @java.lang.Override
                    public void onViewAttachedToWindow(android.view.View v) {
                        if (attach != null) {
                            attach.onViewAttachedToWindow(v);
                        }
                    }

                    @java.lang.Override
                    public void onViewDetachedFromWindow(android.view.View v) {
                        if (detach != null) {
                            detach.onViewDetachedFromWindow(v);
                        }
                    }
                };
            }
            final android.view.View.OnAttachStateChangeListener oldListener = android.databinding.adapters.ListenerUtil.trackListener(view, newListener, R.id.onAttachStateChangeListener);
            if (oldListener != null) {
                view.removeOnAttachStateChangeListener(oldListener);
            }
            if (newListener != null) {
                view.addOnAttachStateChangeListener(newListener);
            }
        }
    }

    @android.databinding.BindingAdapter("android:onLayoutChange")
    public static void setOnLayoutChangeListener(android.view.View view, android.view.View.OnLayoutChangeListener oldValue, android.view.View.OnLayoutChangeListener newValue) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if (oldValue != null) {
                view.removeOnLayoutChangeListener(oldValue);
            }
            if (newValue != null) {
                view.addOnLayoutChangeListener(newValue);
            }
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    @android.databinding.BindingAdapter("android:background")
    public static void setBackground(android.view.View view, android.graphics.drawable.Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    // Follows the same conversion mechanism as in TypedValue.complexToDimensionPixelSize as used
    // when setting padding. It rounds off the float value unless the value is < 1.
    // When a value is between 0 and 1, it is set to 1. A value less than 0 is set to -1.
    private static int pixelsToDimensionPixelSize(float pixels) {
        final int result = ((int) (pixels + 0.5F));
        if (result != 0) {
            return result;
        } else
            if (pixels == 0) {
                return 0;
            } else
                if (pixels > 0) {
                    return 1;
                } else {
                    return -1;
                }


    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
    public interface OnViewDetachedFromWindow {
        void onViewDetachedFromWindow(android.view.View v);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
    public interface OnViewAttachedToWindow {
        void onViewAttachedToWindow(android.view.View v);
    }
}

