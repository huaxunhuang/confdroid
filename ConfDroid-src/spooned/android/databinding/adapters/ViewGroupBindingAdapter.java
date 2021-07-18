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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.view.ViewGroup.class, attribute = "android:alwaysDrawnWithCache", method = "setAlwaysDrawnWithCacheEnabled"), @android.databinding.BindingMethod(type = android.view.ViewGroup.class, attribute = "android:animationCache", method = "setAnimationCacheEnabled"), @android.databinding.BindingMethod(type = android.view.ViewGroup.class, attribute = "android:splitMotionEvents", method = "setMotionEventSplittingEnabled") })
public class ViewGroupBindingAdapter {
    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    @android.databinding.BindingAdapter({ "android:animateLayoutChanges" })
    public static void setAnimateLayoutChanges(android.view.ViewGroup view, boolean animate) {
        if (animate) {
            view.setLayoutTransition(new android.animation.LayoutTransition());
        } else {
            view.setLayoutTransition(null);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onChildViewAdded", "android:onChildViewRemoved" }, requireAll = false)
    public static void setListener(android.view.ViewGroup view, final android.databinding.adapters.ViewGroupBindingAdapter.OnChildViewAdded added, final android.databinding.adapters.ViewGroupBindingAdapter.OnChildViewRemoved removed) {
        if ((added == null) && (removed == null)) {
            view.setOnHierarchyChangeListener(null);
        } else {
            view.setOnHierarchyChangeListener(new android.view.ViewGroup.OnHierarchyChangeListener() {
                @java.lang.Override
                public void onChildViewAdded(android.view.View parent, android.view.View child) {
                    if (added != null) {
                        added.onChildViewAdded(parent, child);
                    }
                }

                @java.lang.Override
                public void onChildViewRemoved(android.view.View parent, android.view.View child) {
                    if (removed != null) {
                        removed.onChildViewRemoved(parent, child);
                    }
                }
            });
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onAnimationStart", "android:onAnimationEnd", "android:onAnimationRepeat" }, requireAll = false)
    public static void setListener(android.view.ViewGroup view, final android.databinding.adapters.ViewGroupBindingAdapter.OnAnimationStart start, final android.databinding.adapters.ViewGroupBindingAdapter.OnAnimationEnd end, final android.databinding.adapters.ViewGroupBindingAdapter.OnAnimationRepeat repeat) {
        if (((start == null) && (end == null)) && (repeat == null)) {
            view.setLayoutAnimationListener(null);
        } else {
            view.setLayoutAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @java.lang.Override
                public void onAnimationStart(android.view.animation.Animation animation) {
                    if (start != null) {
                        start.onAnimationStart(animation);
                    }
                }

                @java.lang.Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    if (end != null) {
                        end.onAnimationEnd(animation);
                    }
                }

                @java.lang.Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {
                    if (repeat != null) {
                        repeat.onAnimationRepeat(animation);
                    }
                }
            });
        }
    }

    public interface OnChildViewAdded {
        void onChildViewAdded(android.view.View parent, android.view.View child);
    }

    public interface OnChildViewRemoved {
        void onChildViewRemoved(android.view.View parent, android.view.View child);
    }

    public interface OnAnimationStart {
        void onAnimationStart(android.view.animation.Animation animation);
    }

    public interface OnAnimationEnd {
        void onAnimationEnd(android.view.animation.Animation animation);
    }

    public interface OnAnimationRepeat {
        void onAnimationRepeat(android.view.animation.Animation animation);
    }
}

