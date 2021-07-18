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
package android.support.v7.widget;


class ActionBarBackgroundDrawable extends android.graphics.drawable.Drawable {
    final android.support.v7.widget.ActionBarContainer mContainer;

    public ActionBarBackgroundDrawable(android.support.v7.widget.ActionBarContainer container) {
        mContainer = container;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mContainer.mIsSplit) {
            if (mContainer.mSplitBackground != null) {
                mContainer.mSplitBackground.draw(canvas);
            }
        } else {
            if (mContainer.mBackground != null) {
                mContainer.mBackground.draw(canvas);
            }
            if ((mContainer.mStackedBackground != null) && mContainer.mIsStacked) {
                mContainer.mStackedBackground.draw(canvas);
            }
        }
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
    }

    @java.lang.Override
    public int getOpacity() {
        return 0;
    }
}

