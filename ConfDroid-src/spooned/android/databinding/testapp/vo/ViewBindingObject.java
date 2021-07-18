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
package android.databinding.testapp.vo;


public class ViewBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private int mBackgroundTint = 0xff00ff00;

    @android.databinding.Bindable
    private boolean mFadeScrollbars = true;

    @android.databinding.Bindable
    private int mNextFocusForward = R.id.padding;

    @android.databinding.Bindable
    private int mNextFocusLeft = R.id.paddingStartEnd;

    @android.databinding.Bindable
    private int mNextFocusRight = R.id.paddingTopBottom;

    @android.databinding.Bindable
    private int mNextFocusUp = R.id.backgroundTint;

    @android.databinding.Bindable
    private int mNextFocusDown = R.id.fadeScrollbars;

    @android.databinding.Bindable
    private int mRequiresFadingEdge = android.databinding.adapters.ViewBindingAdapter.FADING_EDGE_VERTICAL;

    @android.databinding.Bindable
    private int mScrollbarDefaultDelayBeforeFade = 300;

    @android.databinding.Bindable
    private int mScrollbarFadeDuration = 400;

    @android.databinding.Bindable
    private int mScrollbarSize = 10;

    @android.databinding.Bindable
    private int mScrollbarStyle = android.view.View.SCROLLBARS_INSIDE_OVERLAY;

    @android.databinding.Bindable
    private float mTransformPivotX = 9;

    @android.databinding.Bindable
    private float mTransformPivotY = 8;

    @android.databinding.Bindable
    private int mPadding = 11;

    @android.databinding.Bindable
    private int mPaddingBottom = 12;

    @android.databinding.Bindable
    private int mPaddingTop = 13;

    @android.databinding.Bindable
    private int mPaddingLeft = 14;

    @android.databinding.Bindable
    private int mPaddingRight = 15;

    @android.databinding.Bindable
    private int mPaddingStart = 16;

    @android.databinding.Bindable
    private int mPaddingEnd = 17;

    @android.databinding.Bindable
    private boolean mClickable = true;

    public int getBackgroundTint() {
        return mBackgroundTint;
    }

    public int getScrollbarFadeDuration() {
        return mScrollbarFadeDuration;
    }

    public boolean getFadeScrollbars() {
        return mFadeScrollbars;
    }

    public int getNextFocusDown() {
        return mNextFocusDown;
    }

    public int getNextFocusForward() {
        return mNextFocusForward;
    }

    public int getNextFocusLeft() {
        return mNextFocusLeft;
    }

    public int getNextFocusRight() {
        return mNextFocusRight;
    }

    public int getNextFocusUp() {
        return mNextFocusUp;
    }

    public int getRequiresFadingEdge() {
        return mRequiresFadingEdge;
    }

    public int getScrollbarDefaultDelayBeforeFade() {
        return mScrollbarDefaultDelayBeforeFade;
    }

    public int getScrollbarSize() {
        return mScrollbarSize;
    }

    public int getScrollbarStyle() {
        return mScrollbarStyle;
    }

    public float getTransformPivotX() {
        return mTransformPivotX;
    }

    public float getTransformPivotY() {
        return mTransformPivotY;
    }

    public int getPadding() {
        return mPadding;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public int getPaddingEnd() {
        return mPaddingEnd;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public int getPaddingStart() {
        return mPaddingStart;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public boolean getClickable() {
        return mClickable;
    }

    public void changeValues() {
        mBackgroundTint = 0xffff0000;
        mFadeScrollbars = false;
        mNextFocusForward = R.id.paddingStartEnd;
        mNextFocusLeft = R.id.paddingTopBottom;
        mNextFocusRight = R.id.backgroundTint;
        mNextFocusUp = R.id.fadeScrollbars;
        mNextFocusDown = R.id.padding;
        mRequiresFadingEdge = android.databinding.adapters.ViewBindingAdapter.FADING_EDGE_HORIZONTAL;
        mScrollbarDefaultDelayBeforeFade = 400;
        mScrollbarFadeDuration = 500;
        mScrollbarSize = 11;
        mScrollbarStyle = android.view.View.SCROLLBARS_INSIDE_INSET;
        mTransformPivotX = 7;
        mTransformPivotY = 6;
        mPadding = 110;
        mPaddingBottom = 120;
        mPaddingTop = 130;
        mPaddingLeft = 140;
        mPaddingRight = 150;
        mPaddingStart = 160;
        mPaddingEnd = 170;
        mClickable = false;
        notifyChange();
    }
}

