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
package android.view;


/**
 * Exercises context menus in lists
 */
public class ListContextMenu extends android.app.ListActivity implements android.view.View.OnCreateContextMenuListener {
    static final java.lang.String TAG = "ListContextMenu";

    android.view.ListContextMenu.ThrashListAdapter mAdapter;

    private class ThrashListAdapter extends android.widget.BaseAdapter {
        private android.view.LayoutInflater mInflater;

        private java.lang.String[] mTitles = new java.lang.String[100];

        public ThrashListAdapter(android.content.Context context) {
            mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            mTitles = new java.lang.String[100];
            int i;
            for (i = 0; i < 100; i++) {
                mTitles[i] = ("[" + i) + "]";
            }
        }

        public int getCount() {
            return mTitles.length;
        }

        public java.lang.Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.widget.TextView view;
            if (convertView == null) {
                view = ((android.widget.TextView) (mInflater.inflate(android.R.layout.simple_list_item_1, null)));
            } else {
                view = ((android.widget.TextView) (convertView));
            }
            view.setText("List item " + mTitles[position]);
            return view;
        }
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mAdapter = new android.view.ListContextMenu.ThrashListAdapter(this);
        getListView().setOnCreateContextMenuListener(this);
        setListAdapter(mAdapter);
    }

    @java.lang.Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        android.view.MenuItem item = menu.add(0, 0, 0, "Really long menu item name");
        item.setTitleCondensed("Long name");
        item.setIcon(R.drawable.black_square);
        android.view.SubMenu sm = menu.addSubMenu(0, 0, 0, "The 2nd item, a sub menu").setIcon(R.drawable.black_square_stretchable);
        item = sm.getItem();
        item.setTitleCondensed("Sub menu");
        sm.add(1, 0, 0, "Subitem 1");
        sm.add(1, 0, 0, "Subitem 2");
        sm.add(1, 0, 0, "Subitem 3");
        sm.setGroupCheckable(1, true, true);
        menu.add(0, 0, 0, "Item 3");
        menu.add(0, 0, 0, "Item 4");
        menu.add(0, 0, 0, "Item 5");
        menu.add(0, 0, 0, "Item 6");
        menu.add(0, 0, 0, "Item 7");
        menu.add(0, 0, 0, "Item 8");
        menu.add(0, 0, 0, "Item 9");
        sm = menu.addSubMenu(0, 0, 0, "Item 10 SM");
        sm.add(0, 0, 0, "Subitem 1");
        sm.add(0, 0, 0, "Subitem 2");
        sm.add(0, 0, 0, "Subitem 3");
        sm.add(0, 0, 0, "Subitem 4");
        sm.add(0, 0, 0, "Subitem 5");
        sm.add(0, 0, 0, "Subitem 6");
        sm.add(0, 0, 0, "Subitem 7");
        sm.add(0, 0, 0, "Subitem 8");
        return true;
    }

    public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        android.widget.AdapterView.AdapterContextMenuInfo info = ((android.widget.AdapterView.AdapterContextMenuInfo) (menuInfo));
        java.lang.String text = ((android.widget.TextView) (info.targetView)).getText().toString();
        if (text.contains("[0]")) {
            menu.setHeaderTitle("This is a test of the title and the icon").setHeaderIcon(android.R.drawable.sym_def_app_icon);
        } else
            if (text.contains("[1]")) {
                menu.setHeaderTitle("This is a test of just the title");
            } else {
                android.widget.TextView textView = new android.widget.TextView(this);
                textView.setText("This is a test of a custom View");
                menu.setHeaderView(textView);
            }

        menu.add(0, 0, 0, "Test 1");
        android.view.SubMenu sm = menu.addSubMenu(0, 0, 0, "Test 1.5 SM");
        sm.add(0, 0, 0, "CM Subitem 1");
        sm.add(0, 0, 0, "CM Subitem 2");
        sm.add(0, 0, 0, "CM Subitem 3");
        menu.add(0, 0, 0, "Test 2");
        menu.add(0, 0, 0, "Test 3");
        menu.add(0, 0, 0, "Test 4");
        menu.add(0, 0, 0, "Test 5");
        menu.add(0, 0, 0, "Test 6");
        menu.add(0, 0, 0, "Test 7");
        menu.add(0, 0, 0, "Test 8");
        menu.add(0, 0, 0, "Test 9");
        menu.add(0, 0, 0, "Test 10");
        menu.add(0, 0, 0, "Test 11");
        menu.add(0, 0, 0, "Test 12");
        menu.add(0, 0, 0, "Test 13");
        menu.add(0, 0, 0, "Test 14");
        menu.add(0, 0, 0, "Test 15");
        menu.add(0, 0, 0, "Test 16");
        menu.add(0, 0, 0, "Test 17");
        menu.add(0, 0, 0, "Test 18");
        menu.add(0, 0, 0, "Test 19");
        menu.add(0, 0, 0, "Test 20");
        menu.add(0, 0, 0, "Test 21");
        menu.add(0, 0, 0, "Test 22");
        menu.add(0, 0, 0, "Test 23");
        menu.add(0, 0, 0, "Test 24");
    }

    @java.lang.Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        android.util.Log.i(android.view.ListContextMenu.TAG, ("Options item " + item.toString()) + " selected.");
        return onOptionsItemSelected(item);
    }

    @java.lang.Override
    public void onOptionsMenuClosed(android.view.Menu menu) {
        android.util.Log.i(android.view.ListContextMenu.TAG, "Options menu closed");
    }

    @java.lang.Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        android.util.Log.i(android.view.ListContextMenu.TAG, ("Context item " + item.toString()) + " selected.");
        return onContextItemSelected(item);
    }

    @java.lang.Override
    public void onContextMenuClosed(android.view.Menu menu) {
        android.util.Log.i(android.view.ListContextMenu.TAG, "Context menu closed");
    }
}

