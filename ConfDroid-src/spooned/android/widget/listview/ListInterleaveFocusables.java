/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget.listview;


/**
 * List that interleaves focusable items.
 */
public class ListInterleaveFocusables extends android.util.ListScenario {
    private java.util.Set<java.lang.Integer> mFocusablePositions = com.google.android.collect.Sets.newHashSet(1, 3, 6);

    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setNumItems(7).setItemScreenSizeFactor(1.0 / 8).setItemsFocusable(true).setMustFillScreen(false);
    }

    @java.lang.Override
    protected android.view.View createView(int position, android.view.ViewGroup parent, int desiredHeight) {
        if (mFocusablePositions.contains(position)) {
            return android.util.ListItemFactory.button(position, parent.getContext(), getValueAtPosition(position), desiredHeight);
        } else {
            return createView(position, parent, desiredHeight);
        }
    }

    @java.lang.Override
    public int getItemViewType(int position) {
        return mFocusablePositions.contains(position) ? 0 : 1;
    }

    @java.lang.Override
    public int getViewTypeCount() {
        return 2;
    }
}

