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
 * Exercises moving focus into the list from the side
 */
public class ListTakeFocusFromSide extends android.app.ListActivity {
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
            view.setText(mTitles[position]);
            return view;
        }
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_take_focus_from_side);
        setListAdapter(new android.widget.listview.ListTakeFocusFromSide.ThrashListAdapter(this));
    }
}

