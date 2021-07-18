/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v17.leanback.app;


/**
 * Utility class used by GuidedStepFragment to disable focus out left/right.
 */
class GuidedStepRootLayout extends android.widget.LinearLayout {
    private boolean mFocusOutStart = false;

    private boolean mFocusOutEnd = false;

    public GuidedStepRootLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public GuidedStepRootLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFocusOutStart(boolean focusOutStart) {
        mFocusOutStart = focusOutStart;
    }

    public void setFocusOutEnd(boolean focusOutEnd) {
        mFocusOutEnd = focusOutEnd;
    }

    @java.lang.Override
    public android.view.View focusSearch(android.view.View focused, int direction) {
        android.view.View newFocus = super.focusSearch(focused, direction);
        if ((direction == android.view.View.FOCUS_LEFT) || (direction == android.view.View.FOCUS_RIGHT)) {
            if (android.support.v17.leanback.widget.Util.isDescendant(this, newFocus)) {
                return newFocus;
            }
            if (getLayoutDirection() == android.view.ViewGroup.LAYOUT_DIRECTION_LTR ? direction == android.view.View.FOCUS_LEFT : direction == android.view.View.FOCUS_RIGHT) {
                if (!mFocusOutStart) {
                    return focused;
                }
            } else {
                if (!mFocusOutEnd) {
                    return focused;
                }
            }
        }
        return newFocus;
    }
}

