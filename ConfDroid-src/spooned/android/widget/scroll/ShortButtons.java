/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.widget.scroll;


/**
 * A series of short buttons, some of which are embedded within another
 * layout.
 */
public class ShortButtons extends android.util.ScrollViewScenario {
    private final int mNumButtons = 10;

    protected final float mButtonHeightFactor = 0.2F;

    public int getNumButtons() {
        return mNumButtons;
    }

    public android.widget.Button getButtonAt(int index) {
        if (index < 3) {
            return getContentChildAt(index);
        } else {
            android.widget.LinearLayout ll = getContentChildAt(3);
            return ((android.widget.Button) (ll.getChildAt(index - 3)));
        }
    }

    @java.lang.Override
    protected void init(android.widget.scroll.Params params) {
        final int numButtonsInSubLayout = getNumButtons() - 3;
        params.addButtons(3, "top-level", mButtonHeightFactor).addVerticalLLOfButtons("embedded", numButtonsInSubLayout, numButtonsInSubLayout * mButtonHeightFactor);
    }
}

