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
package android.support.v7.app;


/**
 * Chevron/Caret button to expand/collapse group volume list with animation.
 */
class MediaRouteExpandCollapseButton extends android.widget.ImageButton {
    final android.graphics.drawable.AnimationDrawable mExpandAnimationDrawable;

    final android.graphics.drawable.AnimationDrawable mCollapseAnimationDrawable;

    final java.lang.String mExpandGroupDescription;

    final java.lang.String mCollapseGroupDescription;

    boolean mIsGroupExpanded;

    android.view.View.OnClickListener mListener;

    public MediaRouteExpandCollapseButton(android.content.Context context) {
        this(context, null);
    }

    public MediaRouteExpandCollapseButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaRouteExpandCollapseButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mExpandAnimationDrawable = ((android.graphics.drawable.AnimationDrawable) (android.support.v4.content.ContextCompat.getDrawable(context, R.drawable.mr_group_expand)));
        mCollapseAnimationDrawable = ((android.graphics.drawable.AnimationDrawable) (android.support.v4.content.ContextCompat.getDrawable(context, R.drawable.mr_group_collapse)));
        android.graphics.ColorFilter filter = new android.graphics.PorterDuffColorFilter(android.support.v7.app.MediaRouterThemeHelper.getControllerColor(context, defStyleAttr), android.graphics.PorterDuff.Mode.SRC_IN);
        mExpandAnimationDrawable.setColorFilter(filter);
        mCollapseAnimationDrawable.setColorFilter(filter);
        mExpandGroupDescription = context.getString(R.string.mr_controller_expand_group);
        mCollapseGroupDescription = context.getString(R.string.mr_controller_collapse_group);
        setImageDrawable(mExpandAnimationDrawable.getFrame(0));
        setContentDescription(mExpandGroupDescription);
        super.setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View view) {
                mIsGroupExpanded = !mIsGroupExpanded;
                if (mIsGroupExpanded) {
                    setImageDrawable(mExpandAnimationDrawable);
                    mExpandAnimationDrawable.start();
                    setContentDescription(mCollapseGroupDescription);
                } else {
                    setImageDrawable(mCollapseAnimationDrawable);
                    mCollapseAnimationDrawable.start();
                    setContentDescription(mExpandGroupDescription);
                }
                if (mListener != null) {
                    mListener.onClick(view);
                }
            }
        });
    }

    @java.lang.Override
    public void setOnClickListener(android.view.View.OnClickListener listener) {
        mListener = listener;
    }
}

