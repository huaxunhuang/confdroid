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


/**
 * Customized FrameLayout excludes margin of child from calculating the child size.
 * So we can change left margin of rows while keep the width of rows unchanged without
 * using hardcoded DIPS.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class BrowseRowsFrameLayout extends android.widget.FrameLayout {
    public BrowseRowsFrameLayout(android.content.Context context) {
        this(context, null);
    }

    public BrowseRowsFrameLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrowseRowsFrameLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @java.lang.Override
    protected void measureChildWithMargins(android.view.View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
        final int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, (getPaddingLeft() + getPaddingRight()) + widthUsed, lp.width);
        final int childHeightMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, (getPaddingTop() + getPaddingBottom()) + heightUsed, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
}

