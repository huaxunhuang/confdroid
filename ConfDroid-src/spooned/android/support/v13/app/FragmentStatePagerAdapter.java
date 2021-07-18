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
 * uses a {@link Fragment} to manage each page. This class also handles
 * saving and restoring of fragment's state.
 *
 * <p>This version of the pager is more useful when there are a large number
 * of pages, working more like a list view.  When pages are not visible to
 * the user, their entire fragment may be destroyed, only keeping the saved
 * state of that fragment.  This allows the pager to hold on to much less
 * memory associated with each visited page as compared to
 * {@link FragmentPagerAdapter} at the cost of potentially more overhead when
 * switching between pages.
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
 * {@sample frameworks/support/samples/Support4Demos/src/com/example/android/supportv4/app/FragmentStatePagerSupport.java
 *      complete}
 *
 * <p>The <code>R.layout.fragment_pager</code> resource of the top-level fragment is:
 *
 * {@sample frameworks/support/samples/Support4Demos/res/layout/fragment_pager.xml
 *      complete}
 *
 * <p>The <code>R.layout.fragment_pager_list</code> resource containing each
 * individual fragment's layout is:
 *
 * {@sample frameworks/support/samples/Support4Demos/res/layout/fragment_pager_list.xml
 *      complete}
 */
public abstract class FragmentStatePagerAdapter extends android.support.v4.view.PagerAdapter {
    private static final java.lang.String TAG = "FragmentStatePagerAdapter";

    private static final boolean DEBUG = false;

    private final android.app.FragmentManager mFragmentManager;

    private android.app.FragmentTransaction mCurTransaction = null;

    private java.util.ArrayList<android.app.Fragment.SavedState> mSavedState = new java.util.ArrayList<android.app.Fragment.SavedState>();

    private java.util.ArrayList<android.app.Fragment> mFragments = new java.util.ArrayList<android.app.Fragment>();

    private android.app.Fragment mCurrentPrimaryItem = null;

    public FragmentStatePagerAdapter(android.app.FragmentManager fm) {
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
        // If we already have this item instantiated, there is nothing
        // to do.  This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        if (mFragments.size() > position) {
            android.app.Fragment f = mFragments.get(position);
            if (f != null) {
                return f;
            }
        }
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        android.app.Fragment fragment = getItem(position);
        if (android.support.v13.app.FragmentStatePagerAdapter.DEBUG)
            android.util.Log.v(android.support.v13.app.FragmentStatePagerAdapter.TAG, (("Adding item #" + position) + ": f=") + fragment);

        if (mSavedState.size() > position) {
            android.app.Fragment.SavedState fss = mSavedState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }
        while (mFragments.size() <= position) {
            mFragments.add(null);
        } 
        android.support.v13.app.FragmentCompat.setMenuVisibility(fragment, false);
        android.support.v13.app.FragmentCompat.setUserVisibleHint(fragment, false);
        mFragments.set(position, fragment);
        mCurTransaction.add(container.getId(), fragment);
        return fragment;
    }

    @java.lang.Override
    public void destroyItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        android.app.Fragment fragment = ((android.app.Fragment) (object));
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (android.support.v13.app.FragmentStatePagerAdapter.DEBUG)
            android.util.Log.v(android.support.v13.app.FragmentStatePagerAdapter.TAG, (((("Removing item #" + position) + ": f=") + object) + " v=") + ((android.app.Fragment) (object)).getView());

        while (mSavedState.size() <= position) {
            mSavedState.add(null);
        } 
        mSavedState.set(position, fragment.isAdded() ? mFragmentManager.saveFragmentInstanceState(fragment) : null);
        mFragments.set(position, null);
        mCurTransaction.remove(fragment);
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
        android.os.Bundle state = null;
        if (mSavedState.size() > 0) {
            state = new android.os.Bundle();
            android.app.Fragment.SavedState[] fss = new android.app.Fragment.SavedState[mSavedState.size()];
            mSavedState.toArray(fss);
            state.putParcelableArray("states", fss);
        }
        for (int i = 0; i < mFragments.size(); i++) {
            android.app.Fragment f = mFragments.get(i);
            if ((f != null) && f.isAdded()) {
                if (state == null) {
                    state = new android.os.Bundle();
                }
                java.lang.String key = "f" + i;
                mFragmentManager.putFragment(state, key, f);
            }
        }
        return state;
    }

    @java.lang.Override
    public void restoreState(android.os.Parcelable state, java.lang.ClassLoader loader) {
        if (state != null) {
            android.os.Bundle bundle = ((android.os.Bundle) (state));
            bundle.setClassLoader(loader);
            android.os.Parcelable[] fss = bundle.getParcelableArray("states");
            mSavedState.clear();
            mFragments.clear();
            if (fss != null) {
                for (int i = 0; i < fss.length; i++) {
                    mSavedState.add(((android.app.Fragment.SavedState) (fss[i])));
                }
            }
            java.lang.Iterable<java.lang.String> keys = bundle.keySet();
            for (java.lang.String key : keys) {
                if (key.startsWith("f")) {
                    int index = java.lang.Integer.parseInt(key.substring(1));
                    android.app.Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        } 
                        android.support.v13.app.FragmentCompat.setMenuVisibility(f, false);
                        mFragments.set(index, f);
                    } else {
                        android.util.Log.w(android.support.v13.app.FragmentStatePagerAdapter.TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}

