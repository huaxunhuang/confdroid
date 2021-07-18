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
package android.support.v13.app;


/**
 * Implementation of {@link android.support.v4.view.PagerAdapter} that
 * represents each page as a {@link android.app.Fragment} that is persistently
 * kept in the fragment manager as long as the user can return to the page.
 *
 * <p>This version of the pager is best for use when there are a handful of
 * typically more static fragments to be paged through, such as a set of tabs.
 * The fragment of each page the user visits will be kept in memory, though its
 * view hierarchy may be destroyed when not visible.  This can result in using
 * a significant amount of memory since fragment instances can hold on to an
 * arbitrary amount of state.  For larger sets of pages, consider
 * {@link FragmentStatePagerAdapter}.
 *
 * <p>When using FragmentPagerAdapter the host ViewPager must have a
 * valid ID set.</p>
 *
 * <p>Subclasses only need to implement {@link #getItem(int)}
 * and {@link #getCount()} to have a working adapter.
 *
 * <p>Here is an example implementation of a pager containing fragments of
 * lists:
 *
 * {@sample frameworks/support/samples/Support13Demos/src/com/example/android/supportv13/app/FragmentPagerSupport.java
 *      complete}
 *
 * <p>The <code>R.layout.fragment_pager</code> resource of the top-level fragment is:
 *
 * {@sample frameworks/support/samples/Support13Demos/res/layout/fragment_pager.xml
 *      complete}
 *
 * <p>The <code>R.layout.fragment_pager_list</code> resource containing each
 * individual fragment's layout is:
 *
 * {@sample frameworks/support/samples/Support13Demos/res/layout/fragment_pager_list.xml
 *      complete}
 */
public abstract class FragmentPagerAdapter extends android.support.v4.view.PagerAdapter {
    private static final java.lang.String TAG = "FragmentPagerAdapter";

    private static final boolean DEBUG = false;

    private final android.app.FragmentManager mFragmentManager;

    private android.app.FragmentTransaction mCurTransaction = null;

    private android.app.Fragment mCurrentPrimaryItem = null;

    public FragmentPagerAdapter(android.app.FragmentManager fm) {
        mFragmentManager = fm;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract android.app.Fragment getItem(int position);

    @java.lang.Override
    public void startUpdate(android.view.ViewGroup container) {
        if (container.getId() == android.view.View.NO_ID) {
            throw new java.lang.IllegalStateException(("ViewPager with adapter " + this) + " requires a view id");
        }
    }

    @java.lang.Override
    public java.lang.Object instantiateItem(android.view.ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        final long itemId = getItemId(position);
        // Do we already have this fragment?
        java.lang.String name = android.support.v13.app.FragmentPagerAdapter.makeFragmentName(container.getId(), itemId);
        android.app.Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (android.support.v13.app.FragmentPagerAdapter.DEBUG)
                android.util.Log.v(android.support.v13.app.FragmentPagerAdapter.TAG, (("Attaching item #" + itemId) + ": f=") + fragment);

            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (android.support.v13.app.FragmentPagerAdapter.DEBUG)
                android.util.Log.v(android.support.v13.app.FragmentPagerAdapter.TAG, (("Adding item #" + itemId) + ": f=") + fragment);

            mCurTransaction.add(container.getId(), fragment, android.support.v13.app.FragmentPagerAdapter.makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            android.support.v13.app.FragmentCompat.setMenuVisibility(fragment, false);
            android.support.v13.app.FragmentCompat.setUserVisibleHint(fragment, false);
        }
        return fragment;
    }

    @java.lang.Override
    public void destroyItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (android.support.v13.app.FragmentPagerAdapter.DEBUG)
            android.util.Log.v(android.support.v13.app.FragmentPagerAdapter.TAG, (((("Detaching item #" + getItemId(position)) + ": f=") + object) + " v=") + ((android.app.Fragment) (object)).getView());

        mCurTransaction.detach(((android.app.Fragment) (object)));
    }

    @java.lang.Override
    public void setPrimaryItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        android.app.Fragment fragment = ((android.app.Fragment) (object));
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                android.support.v13.app.FragmentCompat.setMenuVisibility(mCurrentPrimaryItem, false);
                android.support.v13.app.FragmentCompat.setUserVisibleHint(mCurrentPrimaryItem, false);
            }
            if (fragment != null) {
                android.support.v13.app.FragmentCompat.setMenuVisibility(fragment, true);
                android.support.v13.app.FragmentCompat.setUserVisibleHint(fragment, true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @java.lang.Override
    public void finishUpdate(android.view.ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @java.lang.Override
    public boolean isViewFromObject(android.view.View view, java.lang.Object object) {
        return ((android.app.Fragment) (object)).getView() == view;
    }

    @java.lang.Override
    public android.os.Parcelable saveState() {
        return null;
    }

    @java.lang.Override
    public void restoreState(android.os.Parcelable state, java.lang.ClassLoader loader) {
    }

    /**
     * Return a unique identifier for the item at the given position.
     *
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position
     * 		Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }

    private static java.lang.String makeFragmentName(int viewId, long id) {
        return (("android:switcher:" + viewId) + ":") + id;
    }
}

