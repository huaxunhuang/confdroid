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
 * List that has different view types
 */
public class ListHeterogeneous extends android.util.ListScenario {
    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setNumItems(50).setItemScreenSizeFactor(1.0 / 8).setItemsFocusable(true).setHeaderViewCount(3).setFooterViewCount(2);
    }

    @java.lang.Override
    protected android.view.View createView(int position, android.view.ViewGroup parent, int desiredHeight) {
        switch (position % 3) {
            case 0 :
                return android.util.ListItemFactory.text(position, parent.getContext(), getValueAtPosition(position), desiredHeight);
            case 1 :
                return android.util.ListItemFactory.button(position, parent.getContext(), getValueAtPosition(position), desiredHeight);
            case 2 :
                return android.util.ListItemFactory.doubleText(position, parent.getContext(), getValueAtPosition(position), desiredHeight);
        }
        return null;
    }

    @java.lang.Override
    public android.view.View convertView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        switch (position % 3) {
            case 0 :
                return android.util.ListItemFactory.convertText(convertView, getValueAtPosition(position), position);
            case 1 :
                return android.util.ListItemFactory.convertButton(convertView, getValueAtPosition(position), position);
            case 2 :
                return android.util.ListItemFactory.convertDoubleText(convertView, getValueAtPosition(position), position);
        }
        return null;
    }

    @java.lang.Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @java.lang.Override
    public int getViewTypeCount() {
        return 3;
    }
}

