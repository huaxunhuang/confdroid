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
 * A list where each item expands by 1.5 when selected.
 */
public class ListItemsExpandOnSelection extends android.util.ListScenario {
    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setNumItems(10).setItemScreenSizeFactor(1.0 / 5);
    }

    @java.lang.Override
    protected android.view.View createView(int position, android.view.ViewGroup parent, int desiredHeight) {
        android.widget.TextView result = new android.widget.listview.ListItemsExpandOnSelection.ExpandWhenSelectedView(parent.getContext(), desiredHeight);
        result.setHeight(desiredHeight);
        result.setFocusable(mItemsFocusable);
        result.setText(getValueAtPosition(position));
        final android.widget.AbsListView.LayoutParams lp = new android.widget.AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        result.setLayoutParams(lp);
        return result;
    }

    @java.lang.Override
    public android.view.View convertView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        ((android.widget.listview.ListItemsExpandOnSelection.ExpandWhenSelectedView) (convertView)).setText(getValueAtPosition(position));
        return convertView;
    }

    private static class ExpandWhenSelectedView extends android.widget.TextView {
        private final int mDesiredHeight;

        public ExpandWhenSelectedView(android.content.Context context, int desiredHeight) {
            super(context);
            mDesiredHeight = desiredHeight;
        }

        @java.lang.Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            if (selected) {
                setHeight(((int) (mDesiredHeight * 1.5)));
            } else {
                setHeight(mDesiredHeight);
            }
        }
    }
}

