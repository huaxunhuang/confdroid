/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.app;


/**
 * Static library support version of the framework's {@link android.app.ListFragment}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 */
public class ListFragment extends android.support.v4.app.Fragment {
    static final int INTERNAL_EMPTY_ID = 0xff0001;

    static final int INTERNAL_PROGRESS_CONTAINER_ID = 0xff0002;

    static final int INTERNAL_LIST_CONTAINER_ID = 0xff0003;

    private final android.os.Handler mHandler = new android.os.Handler();

    private final java.lang.Runnable mRequestFocus = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    private final android.widget.AdapterView.OnItemClickListener mOnClickListener = new android.widget.AdapterView.OnItemClickListener() {
        @java.lang.Override
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
            onListItemClick(((android.widget.ListView) (parent)), v, position, id);
        }
    };

    android.widget.ListAdapter mAdapter;

    android.widget.ListView mList;

    android.view.View mEmptyView;

    android.widget.TextView mStandardEmptyView;

    android.view.View mProgressContainer;

    android.view.View mListContainer;

    java.lang.CharSequence mEmptyText;

    boolean mListShown;

    public ListFragment() {
    }

    /**
     * Provide default implementation to return a simple list view.  Subclasses
     * can override to replace with their own layout.  If doing so, the
     * returned view hierarchy <em>must</em> have a ListView whose id
     * is {@link android.R.id#list android.R.id.list} and can optionally
     * have a sibling view id {@link android.R.id#empty android.R.id.empty}
     * that is to be shown when the list is empty.
     *
     * <p>If you are overriding this method with your own custom content,
     * consider including the standard layout {@link android.R.layout#list_content}
     * in your layout file, so that you continue to retain all of the standard
     * behavior of ListFragment.  In particular, this is currently the only
     * way to have the built-in indeterminant progress state be shown.
     */
    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        final android.content.Context context = getContext();
        android.widget.FrameLayout root = new android.widget.FrameLayout(context);
        // ------------------------------------------------------------------
        android.widget.LinearLayout pframe = new android.widget.LinearLayout(context);
        pframe.setId(android.support.v4.app.ListFragment.INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(android.widget.LinearLayout.VERTICAL);
        pframe.setVisibility(android.view.View.GONE);
        pframe.setGravity(android.view.Gravity.CENTER);
        android.widget.ProgressBar progress = new android.widget.ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        pframe.addView(progress, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(pframe, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        // ------------------------------------------------------------------
        android.widget.FrameLayout lframe = new android.widget.FrameLayout(context);
        lframe.setId(android.support.v4.app.ListFragment.INTERNAL_LIST_CONTAINER_ID);
        android.widget.TextView tv = new android.widget.TextView(context);
        tv.setId(android.support.v4.app.ListFragment.INTERNAL_EMPTY_ID);
        tv.setGravity(android.view.Gravity.CENTER);
        lframe.addView(tv, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        android.widget.ListView lv = new android.widget.ListView(context);
        lv.setId(android.R.id.list);
        lv.setDrawSelectorOnTop(false);
        lframe.addView(lv, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(lframe, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        // ------------------------------------------------------------------
        root.setLayoutParams(new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        return root;
    }

    /**
     * Attach to list view once the view hierarchy has been created.
     */
    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureList();
    }

    /**
     * Detach from list view.
     */
    @java.lang.Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mRequestFocus);
        mList = null;
        mListShown = false;
        mEmptyView = mProgressContainer = mListContainer = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param l
     * 		The ListView where the click happened
     * @param v
     * 		The view that was clicked within the ListView
     * @param position
     * 		The position of the view in the list
     * @param id
     * 		The row id of the item that was clicked
     */
    public void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
    }

    /**
     * Provide the cursor for the list view.
     */
    public void setListAdapter(android.widget.ListAdapter adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mList != null) {
            mList.setAdapter(adapter);
            if ((!mListShown) && (!hadAdapter)) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }
        }
    }

    /**
     * Set the currently selected list item to the specified
     * position with the adapter's data
     *
     * @param position
     * 		
     */
    public void setSelection(int position) {
        ensureList();
        mList.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        ensureList();
        return mList.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        ensureList();
        return mList.getSelectedItemId();
    }

    /**
     * Get the fragment's list view widget.
     */
    public android.widget.ListView getListView() {
        ensureList();
        return mList;
    }

    /**
     * The default content for a ListFragment has a TextView that can
     * be shown when the list is empty.  If you would like to have it
     * shown, call this method to supply the text it should use.
     */
    public void setEmptyText(java.lang.CharSequence text) {
        ensureList();
        if (mStandardEmptyView == null) {
            throw new java.lang.IllegalStateException("Can't be used with a custom content view");
        }
        mStandardEmptyView.setText(text);
        if (mEmptyText == null) {
            mList.setEmptyView(mStandardEmptyView);
        }
        mEmptyText = text;
    }

    /**
     * Control whether the list is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     *
     * <p>Applications do not normally need to use this themselves.  The default
     * behavior of ListFragment is to start with the list not being shown, only
     * showing it once an adapter is given with {@link #setListAdapter(ListAdapter)}.
     * If the list at that point had not been shown, when it does get shown
     * it will be do without the user ever seeing the hidden state.
     *
     * @param shown
     * 		If true, the list view is shown; if false, the progress
     * 		indicator.  The initial value is true.
     */
    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    /**
     * Like {@link #setListShown(boolean)}, but no animation is used when
     * transitioning from the previous state.
     */
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    /**
     * Control whether the list is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     *
     * @param shown
     * 		If true, the list view is shown; if false, the progress
     * 		indicator.  The initial value is true.
     * @param animate
     * 		If true, an animation will be used to transition to the
     * 		new state.
     */
    private void setListShown(boolean shown, boolean animate) {
        ensureList();
        if (mProgressContainer == null) {
            throw new java.lang.IllegalStateException("Can't be used with a custom content view");
        }
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(android.view.animation.AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                mListContainer.startAnimation(android.view.animation.AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(android.view.View.GONE);
            mListContainer.setVisibility(android.view.View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(android.view.animation.AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                mListContainer.startAnimation(android.view.animation.AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(android.view.View.VISIBLE);
            mListContainer.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Get the ListAdapter associated with this fragment's ListView.
     */
    public android.widget.ListAdapter getListAdapter() {
        return mAdapter;
    }

    private void ensureList() {
        if (mList != null) {
            return;
        }
        android.view.View root = getView();
        if (root == null) {
            throw new java.lang.IllegalStateException("Content view not yet created");
        }
        if (root instanceof android.widget.ListView) {
            mList = ((android.widget.ListView) (root));
        } else {
            mStandardEmptyView = ((android.widget.TextView) (root.findViewById(android.support.v4.app.ListFragment.INTERNAL_EMPTY_ID)));
            if (mStandardEmptyView == null) {
                mEmptyView = root.findViewById(android.R.id.empty);
            } else {
                mStandardEmptyView.setVisibility(android.view.View.GONE);
            }
            mProgressContainer = root.findViewById(android.support.v4.app.ListFragment.INTERNAL_PROGRESS_CONTAINER_ID);
            mListContainer = root.findViewById(android.support.v4.app.ListFragment.INTERNAL_LIST_CONTAINER_ID);
            android.view.View rawListView = root.findViewById(android.R.id.list);
            if (!(rawListView instanceof android.widget.ListView)) {
                if (rawListView == null) {
                    throw new java.lang.RuntimeException("Your content must have a ListView whose id attribute is " + "'android.R.id.list'");
                }
                throw new java.lang.RuntimeException("Content has view with id attribute 'android.R.id.list' " + "that is not a ListView class");
            }
            mList = ((android.widget.ListView) (rawListView));
            if (mEmptyView != null) {
                mList.setEmptyView(mEmptyView);
            } else
                if (mEmptyText != null) {
                    mStandardEmptyView.setText(mEmptyText);
                    mList.setEmptyView(mStandardEmptyView);
                }

        }
        mListShown = true;
        mList.setOnItemClickListener(mOnClickListener);
        if (mAdapter != null) {
            android.widget.ListAdapter adapter = mAdapter;
            mAdapter = null;
            setListAdapter(adapter);
        } else {
            // We are starting without an adapter, so assume we won't
            // have our data right away and start with the progress indicator.
            if (mProgressContainer != null) {
                setListShown(false, false);
            }
        }
        mHandler.post(mRequestFocus);
    }
}

