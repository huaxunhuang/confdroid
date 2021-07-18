/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.app;


/**
 * A fragment for creating Leanback browse screens. It is composed of a
 * RowsFragment and a HeadersFragment.
 * <p>
 * A BrowseFragment renders the elements of its {@link ObjectAdapter} as a set
 * of rows in a vertical list. The elements in this adapter must be subclasses
 * of {@link Row}.
 * <p>
 * The HeadersFragment can be set to be either shown or hidden by default, or
 * may be disabled entirely. See {@link #setHeadersState} for details.
 * <p>
 * By default the BrowseFragment includes support for returning to the headers
 * when the user presses Back. For Activities that customize {@link android.app.Activity#onBackPressed()}, you must disable this default Back key support by
 * calling {@link #setHeadersTransitionOnBackEnabled(boolean)} with false and
 * use {@link BrowseFragment.BrowseTransitionListener} and
 * {@link #startHeadersTransition(boolean)}.
 * <p>
 * The recommended theme to use with a BrowseFragment is
 * {@link android.support.v17.leanback.R.style#Theme_Leanback_Browse}.
 * </p>
 */
public class BrowseFragment extends android.support.v17.leanback.app.BaseFragment {
    // BUNDLE attribute for saving header show/hide status when backstack is used:
    static final java.lang.String HEADER_STACK_INDEX = "headerStackIndex";

    // BUNDLE attribute for saving header show/hide status when backstack is not used:
    static final java.lang.String HEADER_SHOW = "headerShow";

    private static final java.lang.String IS_PAGE_ROW = "isPageRow";

    private static final java.lang.String CURRENT_SELECTED_POSITION = "currentSelectedPosition";

    final class BackStackListener implements android.app.FragmentManager.OnBackStackChangedListener {
        int mLastEntryCount;

        int mIndexOfHeadersBackStack;

        BackStackListener() {
            mLastEntryCount = getFragmentManager().getBackStackEntryCount();
            mIndexOfHeadersBackStack = -1;
        }

        void load(android.os.Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                mIndexOfHeadersBackStack = savedInstanceState.getInt(android.support.v17.leanback.app.BrowseFragment.HEADER_STACK_INDEX, -1);
                mShowingHeaders = mIndexOfHeadersBackStack == (-1);
            } else {
                if (!mShowingHeaders) {
                    getFragmentManager().beginTransaction().addToBackStack(mWithHeadersBackStackName).commit();
                }
            }
        }

        void save(android.os.Bundle outState) {
            outState.putInt(android.support.v17.leanback.app.BrowseFragment.HEADER_STACK_INDEX, mIndexOfHeadersBackStack);
        }

        @java.lang.Override
        public void onBackStackChanged() {
            if (getFragmentManager() == null) {
                android.util.Log.w(android.support.v17.leanback.app.BrowseFragment.TAG, "getFragmentManager() is null, stack:", new java.lang.Exception());
                return;
            }
            int count = getFragmentManager().getBackStackEntryCount();
            // if backstack is growing and last pushed entry is "headers" backstack,
            // remember the index of the entry.
            if (count > mLastEntryCount) {
                android.app.FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(count - 1);
                if (mWithHeadersBackStackName.equals(entry.getName())) {
                    mIndexOfHeadersBackStack = count - 1;
                }
            } else
                if (count < mLastEntryCount) {
                    // if popped "headers" backstack, initiate the show header transition if needed
                    if (mIndexOfHeadersBackStack >= count) {
                        if (!isHeadersDataReady()) {
                            // if main fragment was restored first before BrowseFragment's adapter gets
                            // restored: don't start header transition, but add the entry back.
                            getFragmentManager().beginTransaction().addToBackStack(mWithHeadersBackStackName).commit();
                            return;
                        }
                        mIndexOfHeadersBackStack = -1;
                        if (!mShowingHeaders) {
                            startHeadersTransitionInternal(true);
                        }
                    }
                }

            mLastEntryCount = count;
        }
    }

    /**
     * Listener for transitions between browse headers and rows.
     */
    public static class BrowseTransitionListener {
        /**
         * Callback when headers transition starts.
         *
         * @param withHeaders
         * 		True if the transition will result in headers
         * 		being shown, false otherwise.
         */
        public void onHeadersTransitionStart(boolean withHeaders) {
        }

        /**
         * Callback when headers transition stops.
         *
         * @param withHeaders
         * 		True if the transition will result in headers
         * 		being shown, false otherwise.
         */
        public void onHeadersTransitionStop(boolean withHeaders) {
        }
    }

    private class SetSelectionRunnable implements java.lang.Runnable {
        static final int TYPE_INVALID = -1;

        static final int TYPE_INTERNAL_SYNC = 0;

        static final int TYPE_USER_REQUEST = 1;

        private int mPosition;

        private int mType;

        private boolean mSmooth;

        SetSelectionRunnable() {
            reset();
        }

        void post(int position, int type, boolean smooth) {
            // Posting the set selection, rather than calling it immediately, prevents an issue
            // with adapter changes.  Example: a row is added before the current selected row;
            // first the fast lane view updates its selection, then the rows fragment has that
            // new selection propagated immediately; THEN the rows view processes the same adapter
            // change and moves the selection again.
            if (type >= mType) {
                mPosition = position;
                mType = type;
                mSmooth = smooth;
                mBrowseFrame.removeCallbacks(this);
                mBrowseFrame.post(this);
            }
        }

        @java.lang.Override
        public void run() {
            setSelection(mPosition, mSmooth);
            reset();
        }

        private void reset() {
            mPosition = -1;
            mType = android.support.v17.leanback.app.BrowseFragment.SetSelectionRunnable.TYPE_INVALID;
            mSmooth = false;
        }
    }

    /**
     * Possible set of actions that {@link BrowseFragment} exposes to clients. Custom
     * fragments can interact with {@link BrowseFragment} using this interface.
     */
    public interface FragmentHost {
        /**
         * Fragments are required to invoke this callback once their view is created
         * inside {@link Fragment#onViewCreated} method. {@link BrowseFragment} starts the entrance
         * animation only after receiving this callback. Failure to invoke this method
         * will lead to fragment not showing up.
         *
         * @param fragmentAdapter
         * 		{@link MainFragmentAdapter} used by the current fragment.
         */
        void notifyViewCreated(android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter fragmentAdapter);

        /**
         * Fragments mapped to {@link PageRow} are required to invoke this callback once their data
         * is created for transition, the entrance animation only after receiving this callback.
         * Failure to invoke this method will lead to fragment not showing up.
         *
         * @param fragmentAdapter
         * 		{@link MainFragmentAdapter} used by the current fragment.
         */
        void notifyDataReady(android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter fragmentAdapter);

        /**
         * Show or hide title view in {@link BrowseFragment} for fragments mapped to
         * {@link PageRow}.  Otherwise the request is ignored, in that case BrowseFragment is fully
         * in control of showing/hiding title view.
         * <p>
         * When HeadersFragment is visible, BrowseFragment will hide search affordance view if
         * there are other focusable rows above currently focused row.
         *
         * @param show
         * 		Boolean indicating whether or not to show the title view.
         */
        void showTitleView(boolean show);
    }

    /**
     * Default implementation of {@link FragmentHost} that is used only by
     * {@link BrowseFragment}.
     */
    private final class FragmentHostImpl implements android.support.v17.leanback.app.BrowseFragment.FragmentHost {
        boolean mShowTitleView = true;

        boolean mDataReady = false;

        FragmentHostImpl() {
        }

        @java.lang.Override
        public void notifyViewCreated(android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter fragmentAdapter) {
            performPendingStates();
        }

        @java.lang.Override
        public void notifyDataReady(android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter fragmentAdapter) {
            mDataReady = true;
            // If fragment host is not the currently active fragment (in BrowseFragment), then
            // ignore the request.
            if ((mMainFragmentAdapter == null) || (mMainFragmentAdapter.getFragmentHost() != this)) {
                return;
            }
            // We only honor showTitle request for PageRows.
            if (!mIsPageRow) {
                return;
            }
            performPendingStates();
        }

        @java.lang.Override
        public void showTitleView(boolean show) {
            mShowTitleView = show;
            // If fragment host is not the currently active fragment (in BrowseFragment), then
            // ignore the request.
            if ((mMainFragmentAdapter == null) || (mMainFragmentAdapter.getFragmentHost() != this)) {
                return;
            }
            // We only honor showTitle request for PageRows.
            if (!mIsPageRow) {
                return;
            }
            updateTitleViewVisibility();
        }
    }

    /**
     * Interface that defines the interaction between {@link BrowseFragment} and it's main
     * content fragment. The key method is {@link MainFragmentAdapter#getFragment()},
     * it will be used to get the fragment to be shown in the content section. Clients can
     * provide any implementation of fragment and customize it's interaction with
     * {@link BrowseFragment} by overriding the necessary methods.
     *
     * <p>
     * Clients are expected to provide
     * an instance of {@link MainFragmentAdapterRegistry} which will be responsible for providing
     * implementations of {@link MainFragmentAdapter} for given content types. Currently
     * we support different types of content - {@link ListRow}, {@link PageRow} or any subtype
     * of {@link Row}. We provide an out of the box adapter implementation for any rows other than
     * {@link PageRow} - {@link android.support.v17.leanback.app.RowsFragment.MainFragmentAdapter}.
     *
     * <p>
     * {@link PageRow} is intended to give full flexibility to developers in terms of Fragment
     * design. Users will have to provide an implementation of {@link MainFragmentAdapter}
     * and provide that through {@link MainFragmentAdapterRegistry}.
     * {@link MainFragmentAdapter} implementation can supply any fragment and override
     * just those interactions that makes sense.
     */
    public static class MainFragmentAdapter<T extends android.app.Fragment> {
        private boolean mScalingEnabled;

        private final T mFragment;

        android.support.v17.leanback.app.BrowseFragment.FragmentHostImpl mFragmentHost;

        public MainFragmentAdapter(T fragment) {
            this.mFragment = fragment;
        }

        public final T getFragment() {
            return mFragment;
        }

        /**
         * Returns whether its scrolling.
         */
        public boolean isScrolling() {
            return false;
        }

        /**
         * Set the visibility of titles/hovercard of browse rows.
         */
        public void setExpand(boolean expand) {
        }

        /**
         * For rows that willing to participate entrance transition,  this function
         * hide views if afterTransition is true,  show views if afterTransition is false.
         */
        public void setEntranceTransitionState(boolean state) {
        }

        /**
         * Sets the window alignment and also the pivots for scale operation.
         */
        public void setAlignment(int windowAlignOffsetFromTop) {
        }

        /**
         * Callback indicating transition prepare start.
         */
        public boolean onTransitionPrepare() {
            return false;
        }

        /**
         * Callback indicating transition start.
         */
        public void onTransitionStart() {
        }

        /**
         * Callback indicating transition end.
         */
        public void onTransitionEnd() {
        }

        /**
         * Returns whether row scaling is enabled.
         */
        public boolean isScalingEnabled() {
            return mScalingEnabled;
        }

        /**
         * Sets the row scaling property.
         */
        public void setScalingEnabled(boolean scalingEnabled) {
            this.mScalingEnabled = scalingEnabled;
        }

        /**
         * Returns the current host interface so that main fragment can interact with
         * {@link BrowseFragment}.
         */
        public final android.support.v17.leanback.app.BrowseFragment.FragmentHost getFragmentHost() {
            return mFragmentHost;
        }

        void setFragmentHost(android.support.v17.leanback.app.BrowseFragment.FragmentHostImpl fragmentHost) {
            this.mFragmentHost = fragmentHost;
        }
    }

    /**
     * Interface to be implemented by all fragments for providing an instance of
     * {@link MainFragmentAdapter}. Both {@link RowsFragment} and custom fragment provided
     * against {@link PageRow} will need to implement this interface.
     */
    public interface MainFragmentAdapterProvider {
        /**
         * Returns an instance of {@link MainFragmentAdapter} that {@link BrowseFragment}
         * would use to communicate with the target fragment.
         */
        android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter getMainFragmentAdapter();
    }

    /**
     * Interface to be implemented by {@link RowsFragment} and it's subclasses for providing
     * an instance of {@link MainFragmentRowsAdapter}.
     */
    public interface MainFragmentRowsAdapterProvider {
        /**
         * Returns an instance of {@link MainFragmentRowsAdapter} that {@link BrowseFragment}
         * would use to communicate with the target fragment.
         */
        android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapter getMainFragmentRowsAdapter();
    }

    /**
     * This is used to pass information to {@link RowsFragment} or its subclasses.
     * {@link BrowseFragment} uses this interface to pass row based interaction events to
     * the target fragment.
     */
    public static class MainFragmentRowsAdapter<T extends android.app.Fragment> {
        private final T mFragment;

        public MainFragmentRowsAdapter(T fragment) {
            if (fragment == null) {
                throw new java.lang.IllegalArgumentException("Fragment can't be null");
            }
            this.mFragment = fragment;
        }

        public final T getFragment() {
            return mFragment;
        }

        /**
         * Set the visibility titles/hover of browse rows.
         */
        public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        }

        /**
         * Sets an item clicked listener on the fragment.
         */
        public void setOnItemViewClickedListener(android.support.v17.leanback.widget.OnItemViewClickedListener listener) {
        }

        /**
         * Sets an item selection listener.
         */
        public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.OnItemViewSelectedListener listener) {
        }

        /**
         * Selects a Row and perform an optional task on the Row.
         */
        public void setSelectedPosition(int rowPosition, boolean smooth, final android.support.v17.leanback.widget.Presenter.ViewHolderTask rowHolderTask) {
        }

        /**
         * Selects a Row.
         */
        public void setSelectedPosition(int rowPosition, boolean smooth) {
        }

        /**
         * Returns the selected position.
         */
        public int getSelectedPosition() {
            return 0;
        }
    }

    private boolean createMainFragment(android.support.v17.leanback.widget.ObjectAdapter adapter, int position) {
        java.lang.Object item = null;
        if ((adapter == null) || (adapter.size() == 0)) {
            return false;
        } else {
            if (position < 0) {
                position = 0;
            } else
                if (position >= adapter.size()) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid position %d requested", position));
                }

            item = adapter.get(position);
        }
        mSelectedPosition = position;
        boolean oldIsPageRow = mIsPageRow;
        mIsPageRow = item instanceof android.support.v17.leanback.widget.PageRow;
        boolean swap;
        if (mMainFragment == null) {
            swap = true;
        } else {
            if (oldIsPageRow) {
                swap = true;
            } else {
                swap = mIsPageRow;
            }
        }
        if (swap) {
            mMainFragment = mMainFragmentAdapterRegistry.createFragment(item);
            if (!(mMainFragment instanceof android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterProvider)) {
                throw new java.lang.IllegalArgumentException("Fragment must implement MainFragmentAdapterProvider");
            }
            mMainFragmentAdapter = ((android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterProvider) (mMainFragment)).getMainFragmentAdapter();
            mMainFragmentAdapter.setFragmentHost(new android.support.v17.leanback.app.BrowseFragment.FragmentHostImpl());
            if (!mIsPageRow) {
                if (mMainFragment instanceof android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapterProvider) {
                    mMainFragmentRowsAdapter = ((android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapterProvider) (mMainFragment)).getMainFragmentRowsAdapter();
                } else {
                    mMainFragmentRowsAdapter = null;
                }
                mIsPageRow = mMainFragmentRowsAdapter == null;
            } else {
                mMainFragmentRowsAdapter = null;
            }
        }
        return swap;
    }

    /**
     * Factory class responsible for creating fragment given the current item. {@link ListRow}
     * should returns {@link RowsFragment} or it's subclass whereas {@link PageRow}
     * can return any fragment class.
     */
    public static abstract class FragmentFactory<T extends android.app.Fragment> {
        public abstract T createFragment(java.lang.Object row);
    }

    /**
     * FragmentFactory implementation for {@link ListRow}.
     */
    public static class ListRowFragmentFactory extends android.support.v17.leanback.app.BrowseFragment.FragmentFactory<android.support.v17.leanback.app.RowsFragment> {
        @java.lang.Override
        public android.support.v17.leanback.app.RowsFragment createFragment(java.lang.Object row) {
            return new android.support.v17.leanback.app.RowsFragment();
        }
    }

    /**
     * Registry class maintaining the mapping of {@link Row} subclasses to {@link FragmentFactory}.
     * BrowseRowFragment automatically registers {@link ListRowFragmentFactory} for
     * handling {@link ListRow}. Developers can override that and also if they want to
     * use custom fragment, they can register a custom {@link FragmentFactory}
     * against {@link PageRow}.
     */
    public static final class MainFragmentAdapterRegistry {
        private final java.util.Map<java.lang.Class, android.support.v17.leanback.app.BrowseFragment.FragmentFactory> mItemToFragmentFactoryMapping = new java.util.HashMap();

        private static final android.support.v17.leanback.app.BrowseFragment.FragmentFactory sDefaultFragmentFactory = new android.support.v17.leanback.app.BrowseFragment.ListRowFragmentFactory();

        public MainFragmentAdapterRegistry() {
            registerFragment(android.support.v17.leanback.widget.ListRow.class, android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterRegistry.sDefaultFragmentFactory);
        }

        public void registerFragment(java.lang.Class rowClass, android.support.v17.leanback.app.BrowseFragment.FragmentFactory factory) {
            mItemToFragmentFactoryMapping.put(rowClass, factory);
        }

        public android.app.Fragment createFragment(java.lang.Object item) {
            if (item == null) {
                throw new java.lang.IllegalArgumentException("Item can't be null");
            }
            android.support.v17.leanback.app.BrowseFragment.FragmentFactory fragmentFactory = mItemToFragmentFactoryMapping.get(item.getClass());
            if ((fragmentFactory == null) && (!(item instanceof android.support.v17.leanback.widget.PageRow))) {
                fragmentFactory = android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterRegistry.sDefaultFragmentFactory;
            }
            return fragmentFactory.createFragment(item);
        }
    }

    static final java.lang.String TAG = "BrowseFragment";

    private static final java.lang.String LB_HEADERS_BACKSTACK = "lbHeadersBackStack_";

    static boolean DEBUG = false;

    /**
     * The headers fragment is enabled and shown by default.
     */
    public static final int HEADERS_ENABLED = 1;

    /**
     * The headers fragment is enabled and hidden by default.
     */
    public static final int HEADERS_HIDDEN = 2;

    /**
     * The headers fragment is disabled and will never be shown.
     */
    public static final int HEADERS_DISABLED = 3;

    private android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterRegistry mMainFragmentAdapterRegistry = new android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterRegistry();

    android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter mMainFragmentAdapter;

    android.app.Fragment mMainFragment;

    android.support.v17.leanback.app.HeadersFragment mHeadersFragment;

    private android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapter mMainFragmentRowsAdapter;

    private android.support.v17.leanback.widget.ObjectAdapter mAdapter;

    private android.support.v17.leanback.widget.PresenterSelector mAdapterPresenter;

    private android.support.v17.leanback.widget.PresenterSelector mWrappingPresenterSelector;

    private int mHeadersState = android.support.v17.leanback.app.BrowseFragment.HEADERS_ENABLED;

    private int mBrandColor = android.graphics.Color.TRANSPARENT;

    private boolean mBrandColorSet;

    android.support.v17.leanback.widget.BrowseFrameLayout mBrowseFrame;

    private android.support.v17.leanback.widget.ScaleFrameLayout mScaleFrameLayout;

    boolean mHeadersBackStackEnabled = true;

    java.lang.String mWithHeadersBackStackName;

    boolean mShowingHeaders = true;

    boolean mCanShowHeaders = true;

    private int mContainerListMarginStart;

    private int mContainerListAlignTop;

    private boolean mMainFragmentScaleEnabled = true;

    android.support.v17.leanback.widget.OnItemViewSelectedListener mExternalOnItemViewSelectedListener;

    private android.support.v17.leanback.widget.OnItemViewClickedListener mOnItemViewClickedListener;

    private int mSelectedPosition = -1;

    private float mScaleFactor;

    boolean mIsPageRow;

    private android.support.v17.leanback.widget.PresenterSelector mHeaderPresenterSelector;

    private final android.support.v17.leanback.app.BrowseFragment.SetSelectionRunnable mSetSelectionRunnable = new android.support.v17.leanback.app.BrowseFragment.SetSelectionRunnable();

    // transition related:
    java.lang.Object mSceneWithHeaders;

    java.lang.Object mSceneWithoutHeaders;

    private java.lang.Object mSceneAfterEntranceTransition;

    java.lang.Object mHeadersTransition;

    android.support.v17.leanback.app.BrowseFragment.BackStackListener mBackStackChangedListener;

    android.support.v17.leanback.app.BrowseFragment.BrowseTransitionListener mBrowseTransitionListener;

    private static final java.lang.String ARG_TITLE = android.support.v17.leanback.app.BrowseFragment.class.getCanonicalName() + ".title";

    private static final java.lang.String ARG_HEADERS_STATE = android.support.v17.leanback.app.BrowseFragment.class.getCanonicalName() + ".headersState";

    /**
     * Creates arguments for a browse fragment.
     *
     * @param args
     * 		The Bundle to place arguments into, or null if the method
     * 		should return a new Bundle.
     * @param title
     * 		The title of the BrowseFragment.
     * @param headersState
     * 		The initial state of the headers of the
     * 		BrowseFragment. Must be one of {@link #HEADERS_ENABLED}, {@link #HEADERS_HIDDEN}, or {@link #HEADERS_DISABLED}.
     * @return A Bundle with the given arguments for creating a BrowseFragment.
     */
    public static android.os.Bundle createArgs(android.os.Bundle args, java.lang.String title, int headersState) {
        if (args == null) {
            args = new android.os.Bundle();
        }
        args.putString(android.support.v17.leanback.app.BrowseFragment.ARG_TITLE, title);
        args.putInt(android.support.v17.leanback.app.BrowseFragment.ARG_HEADERS_STATE, headersState);
        return args;
    }

    /**
     * Sets the brand color for the browse fragment. The brand color is used as
     * the primary color for UI elements in the browse fragment. For example,
     * the background color of the headers fragment uses the brand color.
     *
     * @param color
     * 		The color to use as the brand color of the fragment.
     */
    public void setBrandColor(@android.support.annotation.ColorInt
    int color) {
        mBrandColor = color;
        mBrandColorSet = true;
        if (mHeadersFragment != null) {
            mHeadersFragment.setBackgroundColor(mBrandColor);
        }
    }

    /**
     * Returns the brand color for the browse fragment.
     * The default is transparent.
     */
    @android.support.annotation.ColorInt
    public int getBrandColor() {
        return mBrandColor;
    }

    /**
     * Wrapping app provided PresenterSelector to support InvisibleRowPresenter for SectionRow
     * DividerRow and PageRow.
     */
    private void createAndSetWrapperPresenter() {
        final android.support.v17.leanback.widget.PresenterSelector adapterPresenter = mAdapter.getPresenterSelector();
        if (adapterPresenter == null) {
            throw new java.lang.IllegalArgumentException("Adapter.getPresenterSelector() is null");
        }
        if (adapterPresenter == mAdapterPresenter) {
            return;
        }
        mAdapterPresenter = adapterPresenter;
        android.support.v17.leanback.widget.Presenter[] presenters = adapterPresenter.getPresenters();
        final android.support.v17.leanback.widget.Presenter invisibleRowPresenter = new android.support.v17.leanback.widget.InvisibleRowPresenter();
        final android.support.v17.leanback.widget.Presenter[] allPresenters = new android.support.v17.leanback.widget.Presenter[presenters.length + 1];
        java.lang.System.arraycopy(allPresenters, 0, presenters, 0, presenters.length);
        allPresenters[allPresenters.length - 1] = invisibleRowPresenter;
        mAdapter.setPresenterSelector(new android.support.v17.leanback.widget.PresenterSelector() {
            @java.lang.Override
            public android.support.v17.leanback.widget.Presenter getPresenter(java.lang.Object item) {
                android.support.v17.leanback.widget.Row row = ((android.support.v17.leanback.widget.Row) (item));
                if (row.isRenderedAsRowView()) {
                    return adapterPresenter.getPresenter(item);
                } else {
                    return invisibleRowPresenter;
                }
            }

            @java.lang.Override
            public android.support.v17.leanback.widget.Presenter[] getPresenters() {
                return allPresenters;
            }
        });
    }

    /**
     * Sets the adapter containing the rows for the fragment.
     *
     * <p>The items referenced by the adapter must be be derived from
     * {@link Row}. These rows will be used by the rows fragment and the headers
     * fragment (if not disabled) to render the browse rows.
     *
     * @param adapter
     * 		An ObjectAdapter for the browse rows. All items must
     * 		derive from {@link Row}.
     */
    public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        mAdapter = adapter;
        createAndSetWrapperPresenter();
        if (getView() == null) {
            return;
        }
        replaceMainFragment(mSelectedPosition);
        if (adapter != null) {
            if (mMainFragmentRowsAdapter != null) {
                mMainFragmentRowsAdapter.setAdapter(new android.support.v17.leanback.app.ListRowDataAdapter(adapter));
            }
            mHeadersFragment.setAdapter(adapter);
        }
    }

    public final android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterRegistry getMainFragmentRegistry() {
        return mMainFragmentAdapterRegistry;
    }

    /**
     * Returns the adapter containing the rows for the fragment.
     */
    public android.support.v17.leanback.widget.ObjectAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.OnItemViewSelectedListener listener) {
        mExternalOnItemViewSelectedListener = listener;
    }

    /**
     * Returns an item selection listener.
     */
    public android.support.v17.leanback.widget.OnItemViewSelectedListener getOnItemViewSelectedListener() {
        return mExternalOnItemViewSelectedListener;
    }

    /**
     * Get RowsFragment if it's bound to BrowseFragment or null if either BrowseFragment has
     * not been created yet or a different fragment is bound to it.
     *
     * @return RowsFragment if it's bound to BrowseFragment or null otherwise.
     */
    public android.support.v17.leanback.app.RowsFragment getRowsFragment() {
        if (mMainFragment instanceof android.support.v17.leanback.app.RowsFragment) {
            return ((android.support.v17.leanback.app.RowsFragment) (mMainFragment));
        }
        return null;
    }

    /**
     * Get currently bound HeadersFragment or null if HeadersFragment has not been created yet.
     *
     * @return Currently bound HeadersFragment or null if HeadersFragment has not been created yet.
     */
    public android.support.v17.leanback.app.HeadersFragment getHeadersFragment() {
        return mHeadersFragment;
    }

    /**
     * Sets an item clicked listener on the fragment.
     * OnItemViewClickedListener will override {@link View.OnClickListener} that
     * item presenter sets during {@link Presenter#onCreateViewHolder(ViewGroup)}.
     * So in general, developer should choose one of the listeners but not both.
     */
    public void setOnItemViewClickedListener(android.support.v17.leanback.widget.OnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
        if (mMainFragmentRowsAdapter != null) {
            mMainFragmentRowsAdapter.setOnItemViewClickedListener(listener);
        }
    }

    /**
     * Returns the item Clicked listener.
     */
    public android.support.v17.leanback.widget.OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    /**
     * Starts a headers transition.
     *
     * <p>This method will begin a transition to either show or hide the
     * headers, depending on the value of withHeaders. If headers are disabled
     * for this browse fragment, this method will throw an exception.
     *
     * @param withHeaders
     * 		True if the headers should transition to being shown,
     * 		false if the transition should result in headers being hidden.
     */
    public void startHeadersTransition(boolean withHeaders) {
        if (!mCanShowHeaders) {
            throw new java.lang.IllegalStateException("Cannot start headers transition");
        }
        if (isInHeadersTransition() || (mShowingHeaders == withHeaders)) {
            return;
        }
        startHeadersTransitionInternal(withHeaders);
    }

    /**
     * Returns true if the headers transition is currently running.
     */
    public boolean isInHeadersTransition() {
        return mHeadersTransition != null;
    }

    /**
     * Returns true if headers are shown.
     */
    public boolean isShowingHeaders() {
        return mShowingHeaders;
    }

    /**
     * Sets a listener for browse fragment transitions.
     *
     * @param listener
     * 		The listener to call when a browse headers transition
     * 		begins or ends.
     */
    public void setBrowseTransitionListener(android.support.v17.leanback.app.BrowseFragment.BrowseTransitionListener listener) {
        mBrowseTransitionListener = listener;
    }

    /**
     *
     *
     * @deprecated use {@link BrowseFragment#enableMainFragmentScaling(boolean)} instead.
     * @param enable
     * 		true to enable row scaling
     */
    @java.lang.Deprecated
    public void enableRowScaling(boolean enable) {
        enableMainFragmentScaling(enable);
    }

    /**
     * Enables scaling of main fragment when headers are present. For the page/row fragment,
     * scaling is enabled only when both this method and
     * {@link MainFragmentAdapter#isScalingEnabled()} are enabled.
     *
     * @param enable
     * 		true to enable row scaling
     */
    public void enableMainFragmentScaling(boolean enable) {
        mMainFragmentScaleEnabled = enable;
    }

    void startHeadersTransitionInternal(final boolean withHeaders) {
        if (getFragmentManager().isDestroyed()) {
            return;
        }
        if (!isHeadersDataReady()) {
            return;
        }
        mShowingHeaders = withHeaders;
        mMainFragmentAdapter.onTransitionPrepare();
        mMainFragmentAdapter.onTransitionStart();
        onExpandTransitionStart(!withHeaders, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mHeadersFragment.onTransitionPrepare();
                mHeadersFragment.onTransitionStart();
                createHeadersTransition();
                if (mBrowseTransitionListener != null) {
                    mBrowseTransitionListener.onHeadersTransitionStart(withHeaders);
                }
                android.support.v17.leanback.transition.TransitionHelper.runTransition(withHeaders ? mSceneWithHeaders : mSceneWithoutHeaders, mHeadersTransition);
                if (mHeadersBackStackEnabled) {
                    if (!withHeaders) {
                        getFragmentManager().beginTransaction().addToBackStack(mWithHeadersBackStackName).commit();
                    } else {
                        int index = mBackStackChangedListener.mIndexOfHeadersBackStack;
                        if (index >= 0) {
                            android.app.FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(index);
                            getFragmentManager().popBackStackImmediate(entry.getId(), android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }
                }
            }
        });
    }

    boolean isVerticalScrolling() {
        // don't run transition
        return mHeadersFragment.isScrolling() || mMainFragmentAdapter.isScrolling();
    }

    private final android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener = new android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener() {
        @java.lang.Override
        public android.view.View onFocusSearch(android.view.View focused, int direction) {
            // if headers is running transition,  focus stays
            if (mCanShowHeaders && isInHeadersTransition()) {
                return focused;
            }
            if (android.support.v17.leanback.app.BrowseFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BrowseFragment.TAG, (("onFocusSearch focused " + focused) + " + direction ") + direction);

            if (((getTitleView() != null) && (focused != getTitleView())) && (direction == android.view.View.FOCUS_UP)) {
                return getTitleView();
            }
            if (((getTitleView() != null) && getTitleView().hasFocus()) && (direction == android.view.View.FOCUS_DOWN)) {
                return mCanShowHeaders && mShowingHeaders ? mHeadersFragment.getVerticalGridView() : mMainFragment.getView();
            }
            boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(focused) == android.view.View.LAYOUT_DIRECTION_RTL;
            int towardStart = (isRtl) ? android.view.View.FOCUS_RIGHT : android.view.View.FOCUS_LEFT;
            int towardEnd = (isRtl) ? android.view.View.FOCUS_LEFT : android.view.View.FOCUS_RIGHT;
            if (mCanShowHeaders && (direction == towardStart)) {
                if ((isVerticalScrolling() || mShowingHeaders) || (!isHeadersDataReady())) {
                    return focused;
                }
                return mHeadersFragment.getVerticalGridView();
            } else
                if (direction == towardEnd) {
                    if (isVerticalScrolling()) {
                        return focused;
                    } else
                        if ((mMainFragment != null) && (mMainFragment.getView() != null)) {
                            return mMainFragment.getView();
                        }

                    return focused;
                } else
                    if ((direction == android.view.View.FOCUS_DOWN) && mShowingHeaders) {
                        // disable focus_down moving into PageFragment.
                        return focused;
                    } else {
                        return null;
                    }


        }
    };

    final boolean isHeadersDataReady() {
        return (mAdapter != null) && (mAdapter.size() != 0);
    }

    private final android.support.v17.leanback.widget.BrowseFrameLayout.OnChildFocusListener mOnChildFocusListener = new android.support.v17.leanback.widget.BrowseFrameLayout.OnChildFocusListener() {
        @java.lang.Override
        public boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect) {
            if (getChildFragmentManager().isDestroyed()) {
                return true;
            }
            // Make sure not changing focus when requestFocus() is called.
            if (mCanShowHeaders && mShowingHeaders) {
                if (((mHeadersFragment != null) && (mHeadersFragment.getView() != null)) && mHeadersFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
            if (((mMainFragment != null) && (mMainFragment.getView() != null)) && mMainFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
            return (getTitleView() != null) && getTitleView().requestFocus(direction, previouslyFocusedRect);
        }

        @java.lang.Override
        public void onRequestChildFocus(android.view.View child, android.view.View focused) {
            if (getChildFragmentManager().isDestroyed()) {
                return;
            }
            if ((!mCanShowHeaders) || isInHeadersTransition())
                return;

            int childId = child.getId();
            if ((childId == R.id.browse_container_dock) && mShowingHeaders) {
                startHeadersTransitionInternal(false);
            } else
                if ((childId == R.id.browse_headers_dock) && (!mShowingHeaders)) {
                    startHeadersTransitionInternal(true);
                }

        }
    };

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(android.support.v17.leanback.app.BrowseFragment.CURRENT_SELECTED_POSITION, mSelectedPosition);
        outState.putBoolean(android.support.v17.leanback.app.BrowseFragment.IS_PAGE_ROW, mIsPageRow);
        if (mBackStackChangedListener != null) {
            mBackStackChangedListener.save(outState);
        } else {
            outState.putBoolean(android.support.v17.leanback.app.BrowseFragment.HEADER_SHOW, mShowingHeaders);
        }
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.res.TypedArray ta = getActivity().obtainStyledAttributes(R.styleable.LeanbackTheme);
        mContainerListMarginStart = ((int) (ta.getDimension(R.styleable.LeanbackTheme_browseRowsMarginStart, getActivity().getResources().getDimensionPixelSize(R.dimen.lb_browse_rows_margin_start))));
        mContainerListAlignTop = ((int) (ta.getDimension(R.styleable.LeanbackTheme_browseRowsMarginTop, getActivity().getResources().getDimensionPixelSize(R.dimen.lb_browse_rows_margin_top))));
        ta.recycle();
        readArguments(getArguments());
        if (mCanShowHeaders) {
            if (mHeadersBackStackEnabled) {
                mWithHeadersBackStackName = android.support.v17.leanback.app.BrowseFragment.LB_HEADERS_BACKSTACK + this;
                mBackStackChangedListener = new android.support.v17.leanback.app.BrowseFragment.BackStackListener();
                getFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
                mBackStackChangedListener.load(savedInstanceState);
            } else {
                if (savedInstanceState != null) {
                    mShowingHeaders = savedInstanceState.getBoolean(android.support.v17.leanback.app.BrowseFragment.HEADER_SHOW);
                }
            }
        }
        mScaleFactor = getResources().getFraction(R.fraction.lb_browse_rows_scale, 1, 1);
    }

    @java.lang.Override
    public void onDestroyView() {
        mMainFragmentRowsAdapter = null;
        mMainFragmentAdapter = null;
        mMainFragment = null;
        mHeadersFragment = null;
        super.onDestroyView();
    }

    @java.lang.Override
    public void onDestroy() {
        if (mBackStackChangedListener != null) {
            getFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
        }
        super.onDestroy();
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        if (getChildFragmentManager().findFragmentById(R.id.scale_frame) == null) {
            mHeadersFragment = new android.support.v17.leanback.app.HeadersFragment();
            createMainFragment(mAdapter, mSelectedPosition);
            android.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction().replace(R.id.browse_headers_dock, mHeadersFragment);
            if (mMainFragment != null) {
                ft.replace(R.id.scale_frame, mMainFragment);
            } else {
                // Empty adapter used to guard against lazy adapter loading. When this
                // fragment is instantiated, mAdapter might not have the data or might not
                // have been set. In either of those cases mFragmentAdapter will be null.
                // This way we can maintain the invariant that mMainFragmentAdapter is never
                // null and it avoids doing null checks all over the code.
                mMainFragmentAdapter = new android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter(null);
                mMainFragmentAdapter.setFragmentHost(new android.support.v17.leanback.app.BrowseFragment.FragmentHostImpl());
            }
            ft.commit();
        } else {
            mHeadersFragment = ((android.support.v17.leanback.app.HeadersFragment) (getChildFragmentManager().findFragmentById(R.id.browse_headers_dock)));
            mMainFragment = getChildFragmentManager().findFragmentById(R.id.scale_frame);
            mMainFragmentAdapter = ((android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapterProvider) (mMainFragment)).getMainFragmentAdapter();
            mMainFragmentAdapter.setFragmentHost(new android.support.v17.leanback.app.BrowseFragment.FragmentHostImpl());
            mIsPageRow = (savedInstanceState != null) && savedInstanceState.getBoolean(android.support.v17.leanback.app.BrowseFragment.IS_PAGE_ROW, false);
            mSelectedPosition = (savedInstanceState != null) ? savedInstanceState.getInt(android.support.v17.leanback.app.BrowseFragment.CURRENT_SELECTED_POSITION, 0) : 0;
            if (!mIsPageRow) {
                if (mMainFragment instanceof android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapterProvider) {
                    mMainFragmentRowsAdapter = ((android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapterProvider) (mMainFragment)).getMainFragmentRowsAdapter();
                } else {
                    mMainFragmentRowsAdapter = null;
                }
            } else {
                mMainFragmentRowsAdapter = null;
            }
        }
        mHeadersFragment.setHeadersGone(!mCanShowHeaders);
        if (mHeaderPresenterSelector != null) {
            mHeadersFragment.setPresenterSelector(mHeaderPresenterSelector);
        }
        mHeadersFragment.setAdapter(mAdapter);
        mHeadersFragment.setOnHeaderViewSelectedListener(mHeaderViewSelectedListener);
        mHeadersFragment.setOnHeaderClickedListener(mHeaderClickedListener);
        android.view.View root = inflater.inflate(R.layout.lb_browse_fragment, container, false);
        getProgressBarManager().setRootView(((android.view.ViewGroup) (root)));
        mBrowseFrame = ((android.support.v17.leanback.widget.BrowseFrameLayout) (root.findViewById(R.id.browse_frame)));
        mBrowseFrame.setOnChildFocusListener(mOnChildFocusListener);
        mBrowseFrame.setOnFocusSearchListener(mOnFocusSearchListener);
        installTitleView(inflater, mBrowseFrame, savedInstanceState);
        mScaleFrameLayout = ((android.support.v17.leanback.widget.ScaleFrameLayout) (root.findViewById(R.id.scale_frame)));
        mScaleFrameLayout.setPivotX(0);
        mScaleFrameLayout.setPivotY(mContainerListAlignTop);
        setupMainFragment();
        if (mBrandColorSet) {
            mHeadersFragment.setBackgroundColor(mBrandColor);
        }
        mSceneWithHeaders = android.support.v17.leanback.transition.TransitionHelper.createScene(mBrowseFrame, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                showHeaders(true);
            }
        });
        mSceneWithoutHeaders = android.support.v17.leanback.transition.TransitionHelper.createScene(mBrowseFrame, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                showHeaders(false);
            }
        });
        mSceneAfterEntranceTransition = android.support.v17.leanback.transition.TransitionHelper.createScene(mBrowseFrame, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                setEntranceTransitionEndState();
            }
        });
        return root;
    }

    private void setupMainFragment() {
        if (mMainFragmentRowsAdapter != null) {
            if (mAdapter != null) {
                mMainFragmentRowsAdapter.setAdapter(new android.support.v17.leanback.app.ListRowDataAdapter(mAdapter));
            }
            mMainFragmentRowsAdapter.setOnItemViewSelectedListener(new android.support.v17.leanback.app.BrowseFragment.MainFragmentItemViewSelectedListener(mMainFragmentRowsAdapter));
            mMainFragmentRowsAdapter.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
    }

    @java.lang.Override
    boolean isReadyForPrepareEntranceTransition() {
        return (mMainFragment != null) && (mMainFragment.getView() != null);
    }

    @java.lang.Override
    boolean isReadyForStartEntranceTransition() {
        return ((mMainFragment != null) && (mMainFragment.getView() != null)) && ((!mIsPageRow) || mMainFragmentAdapter.mFragmentHost.mDataReady);
    }

    void createHeadersTransition() {
        mHeadersTransition = android.support.v17.leanback.transition.TransitionHelper.loadTransition(getActivity(), mShowingHeaders ? R.transition.lb_browse_headers_in : R.transition.lb_browse_headers_out);
        android.support.v17.leanback.transition.TransitionHelper.addTransitionListener(mHeadersTransition, new android.support.v17.leanback.transition.TransitionListener() {
            @java.lang.Override
            public void onTransitionStart(java.lang.Object transition) {
            }

            @java.lang.Override
            public void onTransitionEnd(java.lang.Object transition) {
                mHeadersTransition = null;
                if (mMainFragmentAdapter != null) {
                    mMainFragmentAdapter.onTransitionEnd();
                    if ((!mShowingHeaders) && (mMainFragment != null)) {
                        android.view.View mainFragmentView = mMainFragment.getView();
                        if ((mainFragmentView != null) && (!mainFragmentView.hasFocus())) {
                            mainFragmentView.requestFocus();
                        }
                    }
                }
                if (mHeadersFragment != null) {
                    mHeadersFragment.onTransitionEnd();
                    if (mShowingHeaders) {
                        android.support.v17.leanback.widget.VerticalGridView headerGridView = mHeadersFragment.getVerticalGridView();
                        if ((headerGridView != null) && (!headerGridView.hasFocus())) {
                            headerGridView.requestFocus();
                        }
                    }
                }
                // Animate TitleView once header animation is complete.
                updateTitleViewVisibility();
                if (mBrowseTransitionListener != null) {
                    mBrowseTransitionListener.onHeadersTransitionStop(mShowingHeaders);
                }
            }
        });
    }

    void updateTitleViewVisibility() {
        if (!mShowingHeaders) {
            boolean showTitleView;
            if (mIsPageRow && (mMainFragmentAdapter != null)) {
                // page fragment case:
                showTitleView = mMainFragmentAdapter.mFragmentHost.mShowTitleView;
            } else {
                // regular row view case:
                showTitleView = isFirstRowWithContent(mSelectedPosition);
            }
            if (showTitleView) {
                showTitle(android.support.v17.leanback.widget.TitleViewAdapter.FULL_VIEW_VISIBLE);
            } else {
                showTitle(false);
            }
        } else {
            // when HeaderFragment is showing,  showBranding and showSearch are slightly different
            boolean showBranding;
            boolean showSearch;
            if (mIsPageRow && (mMainFragmentAdapter != null)) {
                showBranding = mMainFragmentAdapter.mFragmentHost.mShowTitleView;
            } else {
                showBranding = isFirstRowWithContent(mSelectedPosition);
            }
            showSearch = isFirstRowWithContentOrPageRow(mSelectedPosition);
            int flags = 0;
            if (showBranding)
                flags |= android.support.v17.leanback.widget.TitleViewAdapter.BRANDING_VIEW_VISIBLE;

            if (showSearch)
                flags |= android.support.v17.leanback.widget.TitleViewAdapter.SEARCH_VIEW_VISIBLE;

            if (flags != 0) {
                showTitle(flags);
            } else {
                showTitle(false);
            }
        }
    }

    boolean isFirstRowWithContentOrPageRow(int rowPosition) {
        if ((mAdapter == null) || (mAdapter.size() == 0)) {
            return true;
        }
        for (int i = 0; i < mAdapter.size(); i++) {
            final android.support.v17.leanback.widget.Row row = ((android.support.v17.leanback.widget.Row) (mAdapter.get(i)));
            if (row.isRenderedAsRowView() || (row instanceof android.support.v17.leanback.widget.PageRow)) {
                return rowPosition == i;
            }
        }
        return true;
    }

    boolean isFirstRowWithContent(int rowPosition) {
        if ((mAdapter == null) || (mAdapter.size() == 0)) {
            return true;
        }
        for (int i = 0; i < mAdapter.size(); i++) {
            final android.support.v17.leanback.widget.Row row = ((android.support.v17.leanback.widget.Row) (mAdapter.get(i)));
            if (row.isRenderedAsRowView()) {
                return rowPosition == i;
            }
        }
        return true;
    }

    /**
     * Sets the {@link PresenterSelector} used to render the row headers.
     *
     * @param headerPresenterSelector
     * 		The PresenterSelector that will determine
     * 		the Presenter for each row header.
     */
    public void setHeaderPresenterSelector(android.support.v17.leanback.widget.PresenterSelector headerPresenterSelector) {
        mHeaderPresenterSelector = headerPresenterSelector;
        if (mHeadersFragment != null) {
            mHeadersFragment.setPresenterSelector(mHeaderPresenterSelector);
        }
    }

    private void setHeadersOnScreen(boolean onScreen) {
        android.view.ViewGroup.MarginLayoutParams lp;
        android.view.View containerList;
        containerList = mHeadersFragment.getView();
        lp = ((android.view.ViewGroup.MarginLayoutParams) (containerList.getLayoutParams()));
        lp.setMarginStart(onScreen ? 0 : -mContainerListMarginStart);
        containerList.setLayoutParams(lp);
    }

    void showHeaders(boolean show) {
        if (android.support.v17.leanback.app.BrowseFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BrowseFragment.TAG, "showHeaders " + show);

        mHeadersFragment.setHeadersEnabled(show);
        setHeadersOnScreen(show);
        expandMainFragment(!show);
    }

    private void expandMainFragment(boolean expand) {
        android.view.ViewGroup.MarginLayoutParams params = ((android.view.ViewGroup.MarginLayoutParams) (mScaleFrameLayout.getLayoutParams()));
        params.setMarginStart(!expand ? mContainerListMarginStart : 0);
        mScaleFrameLayout.setLayoutParams(params);
        mMainFragmentAdapter.setExpand(expand);
        setMainFragmentAlignment();
        final float scaleFactor = (((!expand) && mMainFragmentScaleEnabled) && mMainFragmentAdapter.isScalingEnabled()) ? mScaleFactor : 1;
        mScaleFrameLayout.setLayoutScaleY(scaleFactor);
        mScaleFrameLayout.setChildScale(scaleFactor);
    }

    private android.support.v17.leanback.app.HeadersFragment.OnHeaderClickedListener mHeaderClickedListener = new android.support.v17.leanback.app.HeadersFragment.OnHeaderClickedListener() {
        @java.lang.Override
        public void onHeaderClicked(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder viewHolder, android.support.v17.leanback.widget.Row row) {
            if (((!mCanShowHeaders) || (!mShowingHeaders)) || isInHeadersTransition()) {
                return;
            }
            startHeadersTransitionInternal(false);
            mMainFragment.getView().requestFocus();
        }
    };

    class MainFragmentItemViewSelectedListener implements android.support.v17.leanback.widget.OnItemViewSelectedListener {
        android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapter mMainFragmentRowsAdapter;

        public MainFragmentItemViewSelectedListener(android.support.v17.leanback.app.BrowseFragment.MainFragmentRowsAdapter fragmentRowsAdapter) {
            mMainFragmentRowsAdapter = fragmentRowsAdapter;
        }

        @java.lang.Override
        public void onItemSelected(android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder, java.lang.Object item, android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder, android.support.v17.leanback.widget.Row row) {
            int position = mMainFragmentRowsAdapter.getSelectedPosition();
            if (android.support.v17.leanback.app.BrowseFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BrowseFragment.TAG, "row selected position " + position);

            onRowSelected(position);
            if (mExternalOnItemViewSelectedListener != null) {
                mExternalOnItemViewSelectedListener.onItemSelected(itemViewHolder, item, rowViewHolder, row);
            }
        }
    }

    private android.support.v17.leanback.app.HeadersFragment.OnHeaderViewSelectedListener mHeaderViewSelectedListener = new android.support.v17.leanback.app.HeadersFragment.OnHeaderViewSelectedListener() {
        @java.lang.Override
        public void onHeaderSelected(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder viewHolder, android.support.v17.leanback.widget.Row row) {
            int position = mHeadersFragment.getSelectedPosition();
            if (android.support.v17.leanback.app.BrowseFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.BrowseFragment.TAG, "header selected position " + position);

            onRowSelected(position);
        }
    };

    void onRowSelected(int position) {
        if (position != mSelectedPosition) {
            mSetSelectionRunnable.post(position, android.support.v17.leanback.app.BrowseFragment.SetSelectionRunnable.TYPE_INTERNAL_SYNC, true);
        }
    }

    void setSelection(int position, boolean smooth) {
        if (position == android.support.v7.widget.RecyclerView.NO_POSITION) {
            return;
        }
        mHeadersFragment.setSelectedPosition(position, smooth);
        replaceMainFragment(position);
        if (mMainFragmentRowsAdapter != null) {
            mMainFragmentRowsAdapter.setSelectedPosition(position, smooth);
        }
        mSelectedPosition = position;
        updateTitleViewVisibility();
    }

    private void replaceMainFragment(int position) {
        if (createMainFragment(mAdapter, position)) {
            swapToMainFragment();
            expandMainFragment(!(mCanShowHeaders && mShowingHeaders));
            setupMainFragment();
            performPendingStates();
        }
    }

    private void swapToMainFragment() {
        final android.support.v17.leanback.widget.VerticalGridView gridView = mHeadersFragment.getVerticalGridView();
        if ((isShowingHeaders() && (gridView != null)) && (gridView.getScrollState() != android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE)) {
            // if user is scrolling HeadersFragment,  swap to empty fragment and wait scrolling
            // finishes.
            getChildFragmentManager().beginTransaction().replace(R.id.scale_frame, new android.app.Fragment()).commit();
            gridView.addOnScrollListener(new android.support.v7.widget.RecyclerView.OnScrollListener() {
                @java.lang.Override
                public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
                    if (newState == android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE) {
                        gridView.removeOnScrollListener(this);
                        android.app.FragmentManager fm = getChildFragmentManager();
                        android.app.Fragment currentFragment = fm.findFragmentById(R.id.scale_frame);
                        if (currentFragment != mMainFragment) {
                            fm.beginTransaction().replace(R.id.scale_frame, mMainFragment).commit();
                        }
                    }
                }
            });
        } else {
            // Otherwise swap immediately
            getChildFragmentManager().beginTransaction().replace(R.id.scale_frame, mMainFragment).commit();
        }
    }

    /**
     * Sets the selected row position with smooth animation.
     */
    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    /**
     * Gets position of currently selected row.
     *
     * @return Position of currently selected row.
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * Sets the selected row position.
     */
    public void setSelectedPosition(int position, boolean smooth) {
        mSetSelectionRunnable.post(position, android.support.v17.leanback.app.BrowseFragment.SetSelectionRunnable.TYPE_USER_REQUEST, smooth);
    }

    /**
     * Selects a Row and perform an optional task on the Row. For example
     * <code>setSelectedPosition(10, true, new ListRowPresenterSelectItemViewHolderTask(5))</code>
     * scrolls to 11th row and selects 6th item on that row.  The method will be ignored if
     * RowsFragment has not been created (i.e. before {@link #onCreateView(LayoutInflater,
     * ViewGroup, Bundle)}).
     *
     * @param rowPosition
     * 		Which row to select.
     * @param smooth
     * 		True to scroll to the row, false for no animation.
     * @param rowHolderTask
     * 		Optional task to perform on the Row.  When the task is not null, headers
     * 		fragment will be collapsed.
     */
    public void setSelectedPosition(int rowPosition, boolean smooth, final android.support.v17.leanback.widget.Presenter.ViewHolderTask rowHolderTask) {
        if (mMainFragmentAdapterRegistry == null) {
            return;
        }
        if (rowHolderTask != null) {
            startHeadersTransition(false);
        }
        if (mMainFragmentRowsAdapter != null) {
            mMainFragmentRowsAdapter.setSelectedPosition(rowPosition, smooth, rowHolderTask);
        }
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        mHeadersFragment.setAlignment(mContainerListAlignTop);
        setMainFragmentAlignment();
        if ((mCanShowHeaders && mShowingHeaders) && (mHeadersFragment.getView() != null)) {
            mHeadersFragment.getView().requestFocus();
        } else
            if (((!mCanShowHeaders) || (!mShowingHeaders)) && (mMainFragment.getView() != null)) {
                mMainFragment.getView().requestFocus();
            }

        if (mCanShowHeaders) {
            showHeaders(mShowingHeaders);
        }
        if (isEntranceTransitionEnabled()) {
            setEntranceTransitionStartState();
        }
    }

    private void onExpandTransitionStart(boolean expand, final java.lang.Runnable callback) {
        if (expand) {
            callback.run();
            return;
        }
        // Run a "pre" layout when we go non-expand, in order to get the initial
        // positions of added rows.
        new android.support.v17.leanback.app.BrowseFragment.ExpandPreLayout(callback, mMainFragmentAdapter, getView()).execute();
    }

    private void setMainFragmentAlignment() {
        int alignOffset = mContainerListAlignTop;
        if ((mMainFragmentScaleEnabled && mMainFragmentAdapter.isScalingEnabled()) && mShowingHeaders) {
            alignOffset = ((int) ((alignOffset / mScaleFactor) + 0.5F));
        }
        mMainFragmentAdapter.setAlignment(alignOffset);
    }

    /**
     * Enables/disables headers transition on back key support. This is enabled by
     * default. The BrowseFragment will add a back stack entry when headers are
     * showing. Running a headers transition when the back key is pressed only
     * works when the headers state is {@link #HEADERS_ENABLED} or
     * {@link #HEADERS_HIDDEN}.
     * <p>
     * NOTE: If an Activity has its own onBackPressed() handling, you must
     * disable this feature. You may use {@link #startHeadersTransition(boolean)}
     * and {@link BrowseTransitionListener} in your own back stack handling.
     */
    public final void setHeadersTransitionOnBackEnabled(boolean headersBackStackEnabled) {
        mHeadersBackStackEnabled = headersBackStackEnabled;
    }

    /**
     * Returns true if headers transition on back key support is enabled.
     */
    public final boolean isHeadersTransitionOnBackEnabled() {
        return mHeadersBackStackEnabled;
    }

    private void readArguments(android.os.Bundle args) {
        if (args == null) {
            return;
        }
        if (args.containsKey(android.support.v17.leanback.app.BrowseFragment.ARG_TITLE)) {
            setTitle(args.getString(android.support.v17.leanback.app.BrowseFragment.ARG_TITLE));
        }
        if (args.containsKey(android.support.v17.leanback.app.BrowseFragment.ARG_HEADERS_STATE)) {
            setHeadersState(args.getInt(android.support.v17.leanback.app.BrowseFragment.ARG_HEADERS_STATE));
        }
    }

    /**
     * Sets the state for the headers column in the browse fragment. Must be one
     * of {@link #HEADERS_ENABLED}, {@link #HEADERS_HIDDEN}, or
     * {@link #HEADERS_DISABLED}.
     *
     * @param headersState
     * 		The state of the headers for the browse fragment.
     */
    public void setHeadersState(int headersState) {
        if ((headersState < android.support.v17.leanback.app.BrowseFragment.HEADERS_ENABLED) || (headersState > android.support.v17.leanback.app.BrowseFragment.HEADERS_DISABLED)) {
            throw new java.lang.IllegalArgumentException("Invalid headers state: " + headersState);
        }
        if (android.support.v17.leanback.app.BrowseFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.BrowseFragment.TAG, "setHeadersState " + headersState);

        if (headersState != mHeadersState) {
            mHeadersState = headersState;
            switch (headersState) {
                case android.support.v17.leanback.app.BrowseFragment.HEADERS_ENABLED :
                    mCanShowHeaders = true;
                    mShowingHeaders = true;
                    break;
                case android.support.v17.leanback.app.BrowseFragment.HEADERS_HIDDEN :
                    mCanShowHeaders = true;
                    mShowingHeaders = false;
                    break;
                case android.support.v17.leanback.app.BrowseFragment.HEADERS_DISABLED :
                    mCanShowHeaders = false;
                    mShowingHeaders = false;
                    break;
                default :
                    android.util.Log.w(android.support.v17.leanback.app.BrowseFragment.TAG, "Unknown headers state: " + headersState);
                    break;
            }
            if (mHeadersFragment != null) {
                mHeadersFragment.setHeadersGone(!mCanShowHeaders);
            }
        }
    }

    /**
     * Returns the state of the headers column in the browse fragment.
     */
    public int getHeadersState() {
        return mHeadersState;
    }

    @java.lang.Override
    protected java.lang.Object createEntranceTransition() {
        return android.support.v17.leanback.transition.TransitionHelper.loadTransition(getActivity(), R.transition.lb_browse_entrance_transition);
    }

    @java.lang.Override
    protected void runEntranceTransition(java.lang.Object entranceTransition) {
        android.support.v17.leanback.transition.TransitionHelper.runTransition(mSceneAfterEntranceTransition, entranceTransition);
    }

    @java.lang.Override
    protected void onEntranceTransitionPrepare() {
        mHeadersFragment.onTransitionPrepare();
        // setEntranceTransitionStartState() might be called when mMainFragment is null,
        // make sure it is called.
        mMainFragmentAdapter.setEntranceTransitionState(false);
        mMainFragmentAdapter.onTransitionPrepare();
    }

    @java.lang.Override
    protected void onEntranceTransitionStart() {
        mHeadersFragment.onTransitionStart();
        mMainFragmentAdapter.onTransitionStart();
    }

    @java.lang.Override
    protected void onEntranceTransitionEnd() {
        if (mMainFragmentAdapter != null) {
            mMainFragmentAdapter.onTransitionEnd();
        }
        if (mHeadersFragment != null) {
            mHeadersFragment.onTransitionEnd();
        }
    }

    void setSearchOrbViewOnScreen(boolean onScreen) {
        android.view.View searchOrbView = getTitleViewAdapter().getSearchAffordanceView();
        if (searchOrbView != null) {
            android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (searchOrbView.getLayoutParams()));
            lp.setMarginStart(onScreen ? 0 : -mContainerListMarginStart);
            searchOrbView.setLayoutParams(lp);
        }
    }

    void setEntranceTransitionStartState() {
        setHeadersOnScreen(false);
        setSearchOrbViewOnScreen(false);
        mMainFragmentAdapter.setEntranceTransitionState(false);
    }

    void setEntranceTransitionEndState() {
        setHeadersOnScreen(mShowingHeaders);
        setSearchOrbViewOnScreen(true);
        mMainFragmentAdapter.setEntranceTransitionState(true);
    }

    private class ExpandPreLayout implements android.view.ViewTreeObserver.OnPreDrawListener {
        private final android.view.View mView;

        private final java.lang.Runnable mCallback;

        private int mState;

        private android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter mainFragmentAdapter;

        static final int STATE_INIT = 0;

        static final int STATE_FIRST_DRAW = 1;

        static final int STATE_SECOND_DRAW = 2;

        ExpandPreLayout(java.lang.Runnable callback, android.support.v17.leanback.app.BrowseFragment.MainFragmentAdapter adapter, android.view.View view) {
            mView = view;
            mCallback = callback;
            mainFragmentAdapter = adapter;
        }

        void execute() {
            mView.getViewTreeObserver().addOnPreDrawListener(this);
            mainFragmentAdapter.setExpand(false);
            // always trigger onPreDraw even adapter setExpand() does nothing.
            mView.invalidate();
            mState = android.support.v17.leanback.app.BrowseFragment.ExpandPreLayout.STATE_INIT;
        }

        @java.lang.Override
        public boolean onPreDraw() {
            if ((getView() == null) || (getActivity() == null)) {
                mView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
            if (mState == android.support.v17.leanback.app.BrowseFragment.ExpandPreLayout.STATE_INIT) {
                mainFragmentAdapter.setExpand(true);
                // always trigger onPreDraw even adapter setExpand() does nothing.
                mView.invalidate();
                mState = android.support.v17.leanback.app.BrowseFragment.ExpandPreLayout.STATE_FIRST_DRAW;
            } else
                if (mState == android.support.v17.leanback.app.BrowseFragment.ExpandPreLayout.STATE_FIRST_DRAW) {
                    mCallback.run();
                    mView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mState = android.support.v17.leanback.app.BrowseFragment.ExpandPreLayout.STATE_SECOND_DRAW;
                }

            return false;
        }
    }
}

