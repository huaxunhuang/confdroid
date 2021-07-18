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
 * Each list item has two focusables that are close enough together that
 * it shouldn't require panning to move focus.
 */
public class ListItemFocusablesClose extends android.util.ListScenario {
    /**
     * Get the child of a list item.
     *
     * @param listIndex
     * 		The index of the currently visible items
     * @param index
     * 		The index of the child.
     */
    public android.view.View getChildOfItem(int listIndex, int index) {
        return ((android.view.ViewGroup) (getListView().getChildAt(listIndex))).getChildAt(index);
    }

    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setItemsFocusable(true).setNumItems(2).setItemScreenSizeFactor(0.55);
    }

    @java.lang.Override
    protected android.view.View createView(int position, android.view.ViewGroup parent, int desiredHeight) {
        return android.util.ListItemFactory.twoButtonsSeparatedByFiller(position, parent.getContext(), desiredHeight);
    }
}

