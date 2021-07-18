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
package android.widget.focus;


public class ListWithFooterViewAndNewLabels extends android.app.ListActivity {
    private android.widget.focus.ListWithFooterViewAndNewLabels.MyAdapter mMyAdapter;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_with_button_above);
        android.widget.Button footerButton = new android.widget.Button(this);
        footerButton.setText("hi");
        footerButton.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        getListView().addFooterView(footerButton);
        mMyAdapter = new android.widget.focus.ListWithFooterViewAndNewLabels.MyAdapter(this);
        setListAdapter(mMyAdapter);
        // not in list
        android.widget.Button topButton = ((android.widget.Button) (findViewById(R.id.button)));
        topButton.setText("click to add new item");
        topButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mMyAdapter.addLabel("yo");
            }
        });
        mMyAdapter.addLabel("first");
    }

    /**
     * An adapter that can take new string labels.
     */
    static class MyAdapter extends android.widget.BaseAdapter {
        private final android.content.Context mContext;

        private java.util.List<java.lang.String> mLabels = com.google.android.collect.Lists.newArrayList();

        public MyAdapter(android.content.Context context) {
            mContext = context;
        }

        public int getCount() {
            return mLabels.size();
        }

        public java.lang.Object getItem(int position) {
            return mLabels.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            java.lang.String label = mLabels.get(position);
            android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            android.widget.TextView tv = ((android.widget.TextView) (inflater.inflate(android.R.layout.simple_list_item_1, null)));
            tv.setText(label);
            return tv;
        }

        public void addLabel(java.lang.String s) {
            mLabels.add(s + mLabels.size());
            notifyDataSetChanged();
        }
    }
}

