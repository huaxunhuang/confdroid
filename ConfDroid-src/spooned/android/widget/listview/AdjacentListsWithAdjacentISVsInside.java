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
 * Most bodacious scenario yet!
 */
public class AdjacentListsWithAdjacentISVsInside extends android.app.Activity {
    private android.widget.ListView mLeftListView;

    private android.widget.ListView mRightListView;

    public android.widget.ListView getLeftListView() {
        return mLeftListView;
    }

    public android.widget.ListView getRightListView() {
        return mRightListView;
    }

    public android.util.InternalSelectionView getLeftIsv() {
        return ((android.util.InternalSelectionView) (((android.view.ViewGroup) (mLeftListView.getChildAt(0))).getChildAt(0)));
    }

    public android.util.InternalSelectionView getLeftMiddleIsv() {
        return ((android.util.InternalSelectionView) (((android.view.ViewGroup) (mLeftListView.getChildAt(0))).getChildAt(1)));
    }

    public android.util.InternalSelectionView getRightMiddleIsv() {
        return ((android.util.InternalSelectionView) (((android.view.ViewGroup) (mRightListView.getChildAt(0))).getChildAt(0)));
    }

    public android.util.InternalSelectionView getRightIsv() {
        return ((android.util.InternalSelectionView) (((android.view.ViewGroup) (mRightListView.getChildAt(0))).getChildAt(1)));
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int desiredHeight = ((int) (0.8 * getWindowManager().getDefaultDisplay().getHeight()));
        mLeftListView = new android.widget.ListView(this);
        mLeftListView.setAdapter(new android.widget.listview.AdjacentListsWithAdjacentISVsInside.AdjacentISVAdapter(desiredHeight));
        mLeftListView.setItemsCanFocus(true);
        mRightListView = new android.widget.ListView(this);
        mRightListView.setAdapter(new android.widget.listview.AdjacentListsWithAdjacentISVsInside.AdjacentISVAdapter(desiredHeight));
        mRightListView.setItemsCanFocus(true);
        setContentView(android.widget.listview.AdjacentListsWithAdjacentISVsInside.combineAdjacent(mLeftListView, mRightListView));
    }

    private static android.view.View combineAdjacent(android.view.View... views) {
        if (views.length < 2) {
            throw new java.lang.IllegalArgumentException("you should pass at least 2 views in");
        }
        final android.widget.LinearLayout ll = new android.widget.LinearLayout(views[0].getContext());
        ll.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        final android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F);
        for (android.view.View view : views) {
            ll.addView(view, lp);
        }
        return ll;
    }

    static class AdjacentISVAdapter extends android.widget.BaseAdapter {
        private final int mItemHeight;

        AdjacentISVAdapter(int itemHeight) {
            mItemHeight = itemHeight;
        }

        public int getCount() {
            return 1;
        }

        public java.lang.Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            final android.util.InternalSelectionView isvLeft = new android.util.InternalSelectionView(parent.getContext(), 5, "isv left");
            isvLeft.setDesiredHeight(mItemHeight);
            final android.util.InternalSelectionView isvRight = new android.util.InternalSelectionView(parent.getContext(), 5, "isv right");
            isvRight.setDesiredHeight(mItemHeight);
            return android.widget.listview.AdjacentListsWithAdjacentISVsInside.combineAdjacent(isvLeft, isvRight);
        }
    }
}

