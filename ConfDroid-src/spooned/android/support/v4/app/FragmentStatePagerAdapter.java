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
 * Implementation of {@link PagerAdapter} that
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
 * {@sample frameworks/support/samples/Support13Demos/src/com/example/android/supportv13/app/FragmentStatePagerSupport.java
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
public abstract class FragmentStatePagerAdapter extends android.support.v4.view.PagerAdapter {
    private static final java.lang.String TAG = "FragmentStatePagerAdapter";

    private static final boolean DEBUG = false;

    private final android.support.v4.app.FragmentManager mFragmentManager;

    private android.support.v4.app.FragmentTransaction mCurTransaction = null;

    private java.util.ArrayList<android.support.v4.app.Fragment.SavedState> mSavedState = new java.util.ArrayList<android.support.v4.app.Fragment.SavedState>();

    private java.util.ArrayList<android.support.v4.app.Fragment> mFragments = new java.util.ArrayList<android.support.v4.app.Fragment>();

    private android.support.v4.app.Fragment mCurrentPrimaryItem = null;

    public FragmentStatePagerAdapter(android.support.v4.app.FragmentManager fm) {
        mFragmentManager = fm;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract android.support.v4.app.Fragment getItem(int position);

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
            android.support.v4.app.Fragment f = mFragments.get(position);
            if (f != null) {
                return f;
            }
        }
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        android.support.v4.app.Fragment fragment = getItem(position);
        if (android.support.v4.app.FragmentStatePagerAdapter.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentStatePagerAdapter.TAG, (("Adding item #" + position) + ": f=") + fragment);

        if (mSavedState.size() > position) {
            android.support.v4.app.Fragment.SavedState fss = mSavedState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }
        while (mFragments.size() <= position) {
            mFragments.add(null);
        } 
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        mFragments.set(position, fragment);
        mCurTransaction.add(container.getId(), fragment);
        return fragment;
    }

    @java.lang.Override
    public void destroyItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        android.support.v4.app.Fragment fragment = ((android.support.v4.app.Fragment) (object));
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (android.support.v4.app.FragmentStatePagerAdapter.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentStatePagerAdapter.TAG, (((("Removing item #" + position) + ": f=") + object) + " v=") + ((android.support.v4.app.Fragment) (object)).getView());

        while (mSavedState.size() <= position) {
            mSavedState.add(null);
        } 
        mSavedState.set(position, fragment.isAdded() ? mFragmentManager.saveFragmentInstanceState(fragment) : null);
        mFragments.set(position, null);
        mCurTransaction.remove(fragment);
    }

    @java.lang.Override
    public void setPrimaryItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        android.support.v4.app.Fragment fragment = ((android.support.v4.app.Fragment) (object));
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @java.lang.Override
    public void finishUpdate(android.view.ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @java.lang.Override
    public boolean isViewFromObject(android.view.View view, java.lang.Object object) {
        return ((android.support.v4.app.Fragment) (object)).getView() == view;
    }

    @java.lang.Override
    public android.os.Parcelable saveState() {
        android.os.Bundle state = null;
        if (mSavedState.size() > 0) {
            state = new android.os.Bundle();
            android.support.v4.app.Fragment.SavedState[] fss = new android.support.v4.app.Fragment.SavedState[mSavedState.size()];
            mSavedState.toArray(fss);
            state.putParcelableArray("states", fss);
        }
        for (int i = 0; i < mFragments.size(); i++) {
            android.support.v4.app.Fragment f = mFragments.get(i);
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
                    mSavedState.add(((android.support.v4.app.Fragment.SavedState) (fss[i])));
                }
            }
            java.lang.Iterable<java.lang.String> keys = bundle.keySet();
            for (java.lang.String key : keys) {
                if (key.startsWith("f")) {
                    int index = java.lang.Integer.parseInt(key.substring(1));
                    android.support.v4.app.Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        } 
                        f.setMenuVisibility(false);
                        mFragments.set(index, f);
                    } else {
                        android.util.Log.w(android.support.v4.app.FragmentStatePagerAdapter.TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}

