/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


class ControlBar extends android.widget.LinearLayout {
    public interface OnChildFocusedListener {
        public void onChildFocusedListener(android.view.View child, android.view.View focused);
    }

    private int mChildMarginFromCenter;

    private android.support.v17.leanback.widget.ControlBar.OnChildFocusedListener mOnChildFocusedListener;

    public ControlBar(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlBar(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @java.lang.Override
    public boolean requestFocus(int direction, android.graphics.Rect previouslyFocusedRect) {
        if (getChildCount() > 0) {
            if (getChildAt(getChildCount() / 2).requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public void setOnChildFocusedListener(android.support.v17.leanback.widget.ControlBar.OnChildFocusedListener listener) {
        mOnChildFocusedListener = listener;
    }

    public void setChildMarginFromCenter(int marginFromCenter) {
        mChildMarginFromCenter = marginFromCenter;
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        super.requestChildFocus(child, focused);
        if (mOnChildFocusedListener != null) {
            mOnChildFocusedListener.onChildFocusedListener(child, focused);
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mChildMarginFromCenter <= 0) {
            return;
        }
        int totalExtraMargin = 0;
        for (int i = 0; i < (getChildCount() - 1); i++) {
            android.view.View first = getChildAt(i);
            android.view.View second = getChildAt(i + 1);
            int measuredWidth = first.getMeasuredWidth() + second.getMeasuredWidth();
            int marginStart = mChildMarginFromCenter - (measuredWidth / 2);
            android.widget.LinearLayout.LayoutParams lp = ((android.widget.LinearLayout.LayoutParams) (second.getLayoutParams()));
            int extraMargin = marginStart - lp.getMarginStart();
            lp.setMarginStart(marginStart);
            second.setLayoutParams(lp);
            totalExtraMargin += extraMargin;
        }
        setMeasuredDimension(getMeasuredWidth() + totalExtraMargin, getMeasuredHeight());
    }
}

