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
 * Exercises change notification in a list
 */
public class ListThrasher extends android.app.ListActivity implements android.widget.AdapterView.OnItemSelectedListener {
    android.os.Handler mHandler = new android.os.Handler();

    android.widget.listview.ListThrasher.ThrashListAdapter mAdapter;

    java.util.Random mRandomizer = new java.util.Random();

    android.widget.TextView mText;

    java.lang.Runnable mThrash = new java.lang.Runnable() {
        public void run() {
            mAdapter.bumpVersion();
            mHandler.postDelayed(mThrash, 500);
        }
    };

    private class ThrashListAdapter extends android.widget.BaseAdapter {
        private android.view.LayoutInflater mInflater;

        /**
         * Our data, part 1.
         */
        private java.lang.String[] mTitles = new java.lang.String[100];

        /**
         * Our data, part 2.
         */
        private int[] mVersion = new int[100];

        public ThrashListAdapter(android.content.Context context) {
            mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            mTitles = new java.lang.String[100];
            mVersion = new int[100];
            int i;
            for (i = 0; i < 100; i++) {
                mTitles[i] = ("[" + i) + "]";
                mVersion[i] = 0;
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
            view.setText((mTitles[position] + " ") + mVersion[position]);
            return view;
        }

        public void bumpVersion() {
            int position = mRandomizer.nextInt(getCount());
            mVersion[position]++;
            notifyDataSetChanged();
        }
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_thrasher);
        mText = ((android.widget.TextView) (findViewById(R.id.text)));
        mAdapter = new android.widget.listview.ListThrasher.ThrashListAdapter(this);
        setListAdapter(mAdapter);
        mHandler.postDelayed(mThrash, 5000);
        getListView().setOnItemSelectedListener(this);
    }

    public void onItemSelected(android.widget.AdapterView parent, android.view.View v, int position, long id) {
        mText.setText("Position " + position);
    }

    public void onNothingSelected(android.widget.AdapterView parent) {
        mText.setText("Nothing");
    }
}

