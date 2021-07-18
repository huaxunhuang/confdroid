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
 * Exercises change notification in a list
 */
public class GridScrollListener extends android.app.Activity implements android.widget.AbsListView.OnScrollListener {
    android.os.Handler mHandler = new android.os.Handler();

    android.widget.TextView mText;

    android.widget.GridView mGridView;

    @java.lang.Override
    public void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.grid_scroll_listener);
        java.lang.String[] values = new java.lang.String[1000];
        int i = 0;
        for (i = 0; i < 1000; i++) {
            values[i] = ((java.lang.Integer) (i)).toString();
        }
        mText = ((android.widget.TextView) (findViewById(R.id.text)));
        mGridView = ((android.widget.GridView) (findViewById(R.id.grid)));
        mGridView.setAdapter(new android.widget.ArrayAdapter<java.lang.String>(this, android.R.layout.simple_list_item_1, values));
        mGridView.setOnScrollListener(this);
    }

    public android.widget.GridView getGridView() {
        return mGridView;
    }

    public void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = (firstVisibleItem + visibleItemCount) - 1;
        mText.setText((((("Showing " + firstVisibleItem) + "-") + lastItem) + "/") + totalItemCount);
    }

    public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
    }
}

