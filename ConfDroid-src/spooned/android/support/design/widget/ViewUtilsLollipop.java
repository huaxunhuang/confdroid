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
package android.support.design.widget;


class ViewUtilsLollipop {
    private static final int[] STATE_LIST_ANIM_ATTRS = new int[]{ android.R.attr.stateListAnimator };

    static void setBoundsViewOutlineProvider(android.view.View view) {
        view.setOutlineProvider(android.view.ViewOutlineProvider.BOUNDS);
    }

    static void setStateListAnimatorFromAttrs(android.view.View view, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final android.content.Context context = view.getContext();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.design.widget.ViewUtilsLollipop.STATE_LIST_ANIM_ATTRS, defStyleAttr, defStyleRes);
        try {
            if (a.hasValue(0)) {
                android.animation.StateListAnimator sla = android.animation.AnimatorInflater.loadStateListAnimator(context, a.getResourceId(0, 0));
                view.setStateListAnimator(sla);
            }
        } finally {
            a.recycle();
        }
    }

    /**
     * Creates and sets a {@link StateListAnimator} with a custom elevation value
     */
    static void setDefaultAppBarLayoutStateListAnimator(final android.view.View view, final float elevation) {
        final int dur = view.getResources().getInteger(R.integer.app_bar_elevation_anim_duration);
        final android.animation.StateListAnimator sla = new android.animation.StateListAnimator();
        // Enabled and collapsible, but not collapsed means not elevated
        sla.addState(new int[]{ android.R.attr.enabled, R.attr.state_collapsible, -R.attr.state_collapsed }, android.animation.ObjectAnimator.ofFloat(view, "elevation", 0.0F).setDuration(dur));
        // Default enabled state
        sla.addState(new int[]{ android.R.attr.enabled }, android.animation.ObjectAnimator.ofFloat(view, "elevation", elevation).setDuration(dur));
        // Disabled state
        sla.addState(new int[0], android.animation.ObjectAnimator.ofFloat(view, "elevation", 0).setDuration(0));
        view.setStateListAnimator(sla);
    }
}

