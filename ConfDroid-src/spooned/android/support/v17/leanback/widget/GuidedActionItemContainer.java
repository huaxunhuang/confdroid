/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * Root view of GuidedAction item, it supports a foreground drawable and can disable focus out
 * of view.
 */
class GuidedActionItemContainer extends android.support.v17.leanback.widget.NonOverlappingLinearLayoutWithForeground {
    private boolean mFocusOutAllowed = true;

    public GuidedActionItemContainer(android.content.Context context) {
        this(context, null);
    }

    public GuidedActionItemContainer(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidedActionItemContainer(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @java.lang.Override
    public android.view.View focusSearch(android.view.View focused, int direction) {
        if (mFocusOutAllowed || (!android.support.v17.leanback.widget.Util.isDescendant(this, focused))) {
            return super.focusSearch(focused, direction);
        }
        android.view.View view = super.focusSearch(focused, direction);
        if (android.support.v17.leanback.widget.Util.isDescendant(this, view)) {
            return view;
        }
        return null;
    }

    public void setFocusOutAllowed(boolean focusOutAllowed) {
        mFocusOutAllowed = focusOutAllowed;
    }
}

