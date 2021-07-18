/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.adapters;


class ObservableListAdapter<T> extends android.widget.BaseAdapter {
    private java.util.List<T> mList;

    private android.databinding.ObservableList.OnListChangedCallback mListChangedCallback;

    private final android.content.Context mContext;

    private final int mDropDownResourceId;

    private final int mResourceId;

    private final int mTextViewResourceId;

    private final android.view.LayoutInflater mLayoutInflater;

    public ObservableListAdapter(android.content.Context context, java.util.List<T> list, int resourceId, int dropDownResourceId, int textViewResourceId) {
        mContext = context;
        mResourceId = resourceId;
        mDropDownResourceId = dropDownResourceId;
        mTextViewResourceId = textViewResourceId;
        mLayoutInflater = (resourceId == 0) ? null : ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        setList(list);
    }

    public void setList(java.util.List<T> list) {
        if (mList == list) {
            return;
        }
        if (mList instanceof android.databinding.ObservableList) {
            ((android.databinding.ObservableList) (mList)).removeOnListChangedCallback(mListChangedCallback);
        }
        mList = list;
        if (mList instanceof android.databinding.ObservableList) {
            if (mListChangedCallback == null) {
                mListChangedCallback = new android.databinding.ObservableList.OnListChangedCallback() {
                    @java.lang.Override
                    public void onChanged(android.databinding.ObservableList observableList) {
                        notifyDataSetChanged();
                    }

                    @java.lang.Override
                    public void onItemRangeChanged(android.databinding.ObservableList observableList, int i, int i1) {
                        notifyDataSetChanged();
                    }

                    @java.lang.Override
                    public void onItemRangeInserted(android.databinding.ObservableList observableList, int i, int i1) {
                        notifyDataSetChanged();
                    }

                    @java.lang.Override
                    public void onItemRangeMoved(android.databinding.ObservableList observableList, int i, int i1, int i2) {
                        notifyDataSetChanged();
                    }

                    @java.lang.Override
                    public void onItemRangeRemoved(android.databinding.ObservableList observableList, int i, int i1) {
                        notifyDataSetChanged();
                    }
                };
            }
            ((android.databinding.ObservableList) (mList)).addOnListChangedCallback(mListChangedCallback);
        }
        notifyDataSetChanged();
    }

    @java.lang.Override
    public int getCount() {
        return mList.size();
    }

    @java.lang.Override
    public java.lang.Object getItem(int position) {
        return mList.get(position);
    }

    @java.lang.Override
    public long getItemId(int position) {
        return position;
    }

    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        return getViewForResource(mResourceId, position, convertView, parent);
    }

    @java.lang.Override
    public android.view.View getDropDownView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        return getViewForResource(mDropDownResourceId, position, convertView, parent);
    }

    public android.view.View getViewForResource(int resourceId, int position, android.view.View convertView, android.view.ViewGroup parent) {
        if (convertView == null) {
            if (resourceId == 0) {
                convertView = new android.widget.TextView(mContext);
            } else {
                convertView = mLayoutInflater.inflate(resourceId, parent, false);
            }
        }
        android.widget.TextView text = ((android.widget.TextView) ((mTextViewResourceId == 0) ? convertView : convertView.findViewById(mTextViewResourceId)));
        T item = mList.get(position);
        java.lang.CharSequence value;
        if (item instanceof java.lang.CharSequence) {
            value = ((java.lang.CharSequence) (item));
        } else {
            value = java.lang.String.valueOf(item);
        }
        text.setText(value);
        return convertView;
    }
}

