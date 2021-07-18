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
 * A button above a tall internal selection view, wrapped in a scroll view.
 */
public class ButtonAboveTallInternalSelectionView extends android.util.ScrollViewScenario {
    private final int mNumRowsInIsv = 5;

    public android.widget.Button getButtonAbove() {
        return getContentChildAt(0);
    }

    public android.util.InternalSelectionView getIsv() {
        return getContentChildAt(1);
    }

    protected void init(android.widget.scroll.Params params) {
        params.addButton("howdy", 0.1F).addInternalSelectionView(mNumRowsInIsv, 1.1F).addButton("below", 0.1F);
    }
}

