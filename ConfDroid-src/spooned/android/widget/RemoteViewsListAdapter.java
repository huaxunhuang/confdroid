/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.widget;


/**
 *
 *
 * @unknown 
 */
public class RemoteViewsListAdapter extends android.widget.BaseAdapter {
    private android.content.Context mContext;

    private java.util.ArrayList<android.widget.RemoteViews> mRemoteViewsList;

    private java.util.ArrayList<java.lang.Integer> mViewTypes = new java.util.ArrayList<java.lang.Integer>();

    private int mViewTypeCount;

    public RemoteViewsListAdapter(android.content.Context context, java.util.ArrayList<android.widget.RemoteViews> remoteViews, int viewTypeCount) {
        mContext = context;
        mRemoteViewsList = remoteViews;
        mViewTypeCount = viewTypeCount;
        init();
    }

    public void setViewsList(java.util.ArrayList<android.widget.RemoteViews> remoteViews) {
        mRemoteViewsList = remoteViews;
        init();
        notifyDataSetChanged();
    }

    private void init() {
        if (mRemoteViewsList == null)
            return;

        mViewTypes.clear();
        for (android.widget.RemoteViews rv : mRemoteViewsList) {
            if (!mViewTypes.contains(rv.getLayoutId())) {
                mViewTypes.add(rv.getLayoutId());
            }
        }
        if ((mViewTypes.size() > mViewTypeCount) || (mViewTypeCount < 1)) {
            throw new java.lang.RuntimeException("Invalid view type count -- view type count must be >= 1" + "and must be as large as the total number of distinct view types");
        }
    }

    @java.lang.Override
    public int getCount() {
        if (mRemoteViewsList != null) {
            return mRemoteViewsList.size();
        } else {
            return 0;
        }
    }

    @java.lang.Override
    public java.lang.Object getItem(int position) {
        return null;
    }

    @java.lang.Override
    public long getItemId(int position) {
        return position;
    }

    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        if (position < getCount()) {
            android.widget.RemoteViews rv = mRemoteViewsList.get(position);
            rv.addFlags(android.widget.RemoteViews.FLAG_WIDGET_IS_COLLECTION_CHILD);
            android.view.View v;
            if (((convertView != null) && (rv != null)) && (convertView.getId() == rv.getLayoutId())) {
                v = convertView;
                rv.reapply(mContext, v);
            } else {
                v = rv.apply(mContext, parent);
            }
            return v;
        } else {
            return null;
        }
    }

    @java.lang.Override
    public int getItemViewType(int position) {
        if (position < getCount()) {
            int layoutId = mRemoteViewsList.get(position).getLayoutId();
            return mViewTypes.indexOf(layoutId);
        } else {
            return 0;
        }
    }

    public int getViewTypeCount() {
        return mViewTypeCount;
    }

    @java.lang.Override
    public boolean hasStableIds() {
        return false;
    }
}

