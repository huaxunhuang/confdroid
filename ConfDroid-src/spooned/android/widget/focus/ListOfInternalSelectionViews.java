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


/**
 * A list of {@link InternalSelectionView}s paramatarized by the number of items,
 * how many rows in each item, and how tall each item is.
 */
public class ListOfInternalSelectionViews extends android.app.Activity {
    private android.widget.ListView mListView;

    // keys for initializing via Intent params
    public static final java.lang.String BUNDLE_PARAM_NUM_ITEMS = "com.google.test.numItems";

    public static final java.lang.String BUNDLE_PARAM_NUM_ROWS_PER_ITEM = "com.google.test.numRowsPerItem";

    public static final java.lang.String BUNDLE_PARAM_ITEM_SCREEN_HEIGHT_FACTOR = "com.google.test.itemScreenHeightFactor";

    private int mScreenHeight;

    private int mNumItems = 5;

    private int mNumRowsPerItem = 4;

    private double mItemScreenSizeFactor = 5 / 4;

    public android.widget.ListView getListView() {
        return mListView;
    }

    /**
     * Each item is screen height * this factor tall.
     */
    public double getItemScreenSizeFactor() {
        return mItemScreenSizeFactor;
    }

    /**
     *
     *
     * @return The number of rows per item.
     */
    public int getNumRowsPerItem() {
        return mNumRowsPerItem;
    }

    /**
     *
     *
     * @return The number of items in the list.
     */
    public int getNumItems() {
        return mNumItems;
    }

    /**
     *
     *
     * @param position
     * 		The position
     * @return The label (closest thing to a value) for the item at position
     */
    public java.lang.String getLabelForPosition(int position) {
        return "position " + position;
    }

    /**
     * Get the currently selected view.
     */
    public android.util.InternalSelectionView getSelectedView() {
        return ((android.util.InternalSelectionView) (getListView().getSelectedView()));
    }

    /**
     * Get the screen height.
     */
    public int getScreenHeight() {
        return mScreenHeight;
    }

    /**
     * Initialize a bundle suitable for sending as the params of the intent that
     * launches this activity.
     *
     * @param numItems
     * 		The number of items in the list.
     * @param numRowsPerItem
     * 		The number of rows per item.
     * @param itemScreenHeightFactor
     * 		see {@link #getScreenHeight()}
     * @return the intialized bundle.
     */
    public static android.os.Bundle getBundleFor(int numItems, int numRowsPerItem, double itemScreenHeightFactor) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putInt(android.widget.focus.ListOfInternalSelectionViews.BUNDLE_PARAM_NUM_ITEMS, numItems);
        bundle.putInt(android.widget.focus.ListOfInternalSelectionViews.BUNDLE_PARAM_NUM_ROWS_PER_ITEM, numRowsPerItem);
        bundle.putDouble(android.widget.focus.ListOfInternalSelectionViews.BUNDLE_PARAM_ITEM_SCREEN_HEIGHT_FACTOR, itemScreenHeightFactor);
        return bundle;
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        android.graphics.Point size = new android.graphics.Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        mScreenHeight = size.y;
        android.os.Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initFromBundle(extras);
        }
        mListView = new android.widget.ListView(this);
        mListView.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mListView.setDrawSelectorOnTop(false);
        mListView.setAdapter(new android.widget.focus.ListOfInternalSelectionViews.MyAdapter());
        mListView.setItemsCanFocus(true);
        setContentView(mListView);
    }

    private void initFromBundle(android.os.Bundle icicle) {
        int numItems = icicle.getInt(android.widget.focus.ListOfInternalSelectionViews.BUNDLE_PARAM_NUM_ITEMS, -1);
        if (numItems != (-1)) {
            mNumItems = numItems;
        }
        int numRowsPerItem = icicle.getInt(android.widget.focus.ListOfInternalSelectionViews.BUNDLE_PARAM_NUM_ROWS_PER_ITEM, -1);
        if (numRowsPerItem != (-1)) {
            mNumRowsPerItem = numRowsPerItem;
        }
        double screenHeightFactor = icicle.getDouble(android.widget.focus.ListOfInternalSelectionViews.BUNDLE_PARAM_ITEM_SCREEN_HEIGHT_FACTOR, -1.0);
        if (screenHeightFactor > 0) {
            mItemScreenSizeFactor = screenHeightFactor;
        }
    }

    private class MyAdapter extends android.widget.BaseAdapter {
        public int getCount() {
            return mNumItems;
        }

        public java.lang.Object getItem(int position) {
            return getLabelForPosition(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.util.InternalSelectionView item = new android.util.InternalSelectionView(parent.getContext(), mNumRowsPerItem, getLabelForPosition(position));
            item.setDesiredHeight(((int) (mScreenHeight * mItemScreenSizeFactor)));
            return item;
        }
    }
}

