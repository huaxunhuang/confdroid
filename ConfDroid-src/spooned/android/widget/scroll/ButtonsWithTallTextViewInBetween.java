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
 * Two buttons sandwiching a tall text view (good for testing panning across
 * before getting to next button).
 */
public class ButtonsWithTallTextViewInBetween extends android.util.ScrollViewScenario {
    public android.widget.Button getTopButton() {
        return getContentChildAt(0);
    }

    public android.widget.TextView getMiddleFiller() {
        return getContentChildAt(1);
    }

    public android.widget.Button getBottomButton() {
        android.widget.LinearLayout ll = getContentChildAt(2);
        return ((android.widget.Button) (ll.getChildAt(0)));
    }

    protected void init(android.widget.scroll.Params params) {
        params.addButton("top button", 0.2F).addTextView("middle filler", 1.51F).addVerticalLLOfButtons("bottom", 1, 0.2F);
    }
}

