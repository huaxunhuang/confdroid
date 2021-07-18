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
package android.widget.listview;


/**
 * Tests using an empty view with a list
 */
public class ListWithEmptyView extends android.app.ListActivity {
    private class CarefulAdapter<T> extends android.widget.ArrayAdapter<T> {
        public CarefulAdapter(android.content.Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            // TODO Auto-generated constructor stub
        }

        @java.lang.Override
        public long getItemId(int position) {
            if ((position < 0) || (position >= this.getCount())) {
                throw new java.lang.ArrayIndexOutOfBoundsException();
            }
            return super.getItemId(position);
        }
    }

    public static final int MENU_ADD = android.view.Menu.FIRST + 1;

    public static final int MENU_REMOVE = android.view.Menu.FIRST + 2;

    private android.widget.listview.ListWithEmptyView.CarefulAdapter<java.lang.String> mAdapter;

    private int mNextItem = 0;

    private android.view.View mEmptyView;

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new android.widget.listview.ListWithEmptyView.CarefulAdapter<java.lang.String>(this, android.R.layout.simple_list_item_1);
        setContentView(R.layout.list_with_empty_view);
        setListAdapter(mAdapter);
        mEmptyView = findViewById(R.id.empty);
        getListView().setEmptyView(mEmptyView);
    }

    @java.lang.Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        onCreateOptionsMenu(menu);
        menu.add(0, android.widget.listview.ListWithEmptyView.MENU_ADD, 0, R.string.menu_add).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, android.widget.listview.ListWithEmptyView.MENU_REMOVE, 0, R.string.menu_remove).setIcon(android.R.drawable.ic_menu_delete);
        return true;
    }

    @java.lang.Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.widget.listview.ListWithEmptyView.MENU_ADD :
                java.lang.String str = "Item + " + (mNextItem++);
                mAdapter.add(str);
                return true;
            case android.widget.listview.ListWithEmptyView.MENU_REMOVE :
                if (mAdapter.getCount() > 0) {
                    mAdapter.remove(mAdapter.getItem(0));
                }
                return true;
        }
        return onOptionsItemSelected(item);
    }

    public android.view.View getEmptyView() {
        return mEmptyView;
    }
}

