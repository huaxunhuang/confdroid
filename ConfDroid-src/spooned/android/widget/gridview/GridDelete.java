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
package android.widget.gridview;


/**
 * A grid with vertical spacing between rows
 */
public class GridDelete extends android.util.GridScenario {
    @java.lang.Override
    protected void init(android.widget.gridview.Params params) {
        params.setStartingSelectionPosition(-1).setMustFillScreen(false).setNumItems(1001).setNumColumns(4).setItemScreenSizeFactor(0.2).setVerticalSpacing(20);
    }

    @java.lang.Override
    protected android.widget.ListAdapter createAdapter() {
        return new android.widget.gridview.GridDelete.DeleteAdapter(getInitialNumItems());
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_DEL) {
            android.widget.GridView g = getGridView();
            ((android.widget.gridview.GridDelete.DeleteAdapter) (g.getAdapter())).deletePosition(g.getSelectedItemPosition());
            return true;
        } else {
            return onKeyDown(keyCode, event);
        }
    }

    private class DeleteAdapter extends android.widget.BaseAdapter {
        private java.util.ArrayList<java.lang.Integer> mData;

        public DeleteAdapter(int initialNumItems) {
            super();
            mData = new java.util.ArrayList<java.lang.Integer>(initialNumItems);
            int i;
            for (i = 0; i < initialNumItems; ++i) {
                mData.add(new java.lang.Integer(10000 + i));
            }
        }

        public void deletePosition(int selectedItemPosition) {
            if ((selectedItemPosition >= 0) && (selectedItemPosition < mData.size())) {
                mData.remove(selectedItemPosition);
                notifyDataSetChanged();
            }
        }

        public int getCount() {
            return mData.size();
        }

        public java.lang.Object getItem(int position) {
            return mData.get(position);
        }

        public long getItemId(int position) {
            return mData.get(position);
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            int desiredHeight = getDesiredItemHeight();
            return createView(mData.get(position), parent, desiredHeight);
        }
    }
}

