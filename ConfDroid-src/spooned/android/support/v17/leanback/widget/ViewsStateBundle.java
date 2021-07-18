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
package android.support.v17.leanback.widget;


/**
 * Maintains a bundle of states for a group of views. Each view must have a unique id to identify
 * it. There are four different strategies {@link #SAVE_NO_CHILD} {@link #SAVE_ON_SCREEN_CHILD}
 * {@link #SAVE_LIMITED_CHILD} {@link #SAVE_ALL_CHILD}.
 * <p>
 * This class serves purpose of nested "listview" e.g.  a vertical list of horizontal list.
 * Vertical list maintains id->bundle mapping of all it's children (even the children is offscreen
 * and being pruned).
 * <p>
 * The class is currently used within {@link GridLayoutManager}, but it might be used by other
 * ViewGroup.
 */
class ViewsStateBundle {
    public static final int LIMIT_DEFAULT = 100;

    public static final int UNLIMITED = java.lang.Integer.MAX_VALUE;

    private int mSavePolicy;

    private int mLimitNumber;

    private android.support.v4.util.LruCache<java.lang.String, android.util.SparseArray<android.os.Parcelable>> mChildStates;

    public ViewsStateBundle() {
        mSavePolicy = android.support.v17.leanback.widget.BaseGridView.SAVE_NO_CHILD;
        mLimitNumber = android.support.v17.leanback.widget.ViewsStateBundle.LIMIT_DEFAULT;
    }

    public void clear() {
        if (mChildStates != null) {
            mChildStates.evictAll();
        }
    }

    public void remove(int id) {
        if ((mChildStates != null) && (mChildStates.size() != 0)) {
            mChildStates.remove(android.support.v17.leanback.widget.ViewsStateBundle.getSaveStatesKey(id));
        }
    }

    /**
     *
     *
     * @return the saved views states
     */
    public final android.os.Bundle saveAsBundle() {
        if ((mChildStates == null) || (mChildStates.size() == 0)) {
            return null;
        }
        java.util.Map<java.lang.String, android.util.SparseArray<android.os.Parcelable>> snapshot = mChildStates.snapshot();
        android.os.Bundle bundle = new android.os.Bundle();
        for (java.util.Iterator<java.util.Map.Entry<java.lang.String, android.util.SparseArray<android.os.Parcelable>>> i = snapshot.entrySet().iterator(); i.hasNext();) {
            java.util.Map.Entry<java.lang.String, android.util.SparseArray<android.os.Parcelable>> e = i.next();
            bundle.putSparseParcelableArray(e.getKey(), e.getValue());
        }
        return bundle;
    }

    public final void loadFromBundle(android.os.Bundle savedBundle) {
        if ((mChildStates != null) && (savedBundle != null)) {
            mChildStates.evictAll();
            for (java.util.Iterator<java.lang.String> i = savedBundle.keySet().iterator(); i.hasNext();) {
                java.lang.String key = i.next();
                mChildStates.put(key, savedBundle.getSparseParcelableArray(key));
            }
        }
    }

    /**
     *
     *
     * @return the savePolicy, see {@link #SAVE_NO_CHILD} {@link #SAVE_ON_SCREEN_CHILD}
    {@link #SAVE_LIMITED_CHILD} {@link #SAVE_ALL_CHILD}
     */
    public final int getSavePolicy() {
        return mSavePolicy;
    }

    /**
     *
     *
     * @return the limitNumber, only works when {@link #getSavePolicy()} is
    {@link #SAVE_LIMITED_CHILD}
     */
    public final int getLimitNumber() {
        return mLimitNumber;
    }

    /**
     *
     *
     * @see ViewsStateBundle#getSavePolicy()
     */
    public final void setSavePolicy(int savePolicy) {
        this.mSavePolicy = savePolicy;
        applyPolicyChanges();
    }

    /**
     *
     *
     * @see ViewsStateBundle#getLimitNumber()
     */
    public final void setLimitNumber(int limitNumber) {
        this.mLimitNumber = limitNumber;
        applyPolicyChanges();
    }

    protected void applyPolicyChanges() {
        if (mSavePolicy == android.support.v17.leanback.widget.BaseGridView.SAVE_LIMITED_CHILD) {
            if (mLimitNumber <= 0) {
                throw new java.lang.IllegalArgumentException();
            }
            if ((mChildStates == null) || (mChildStates.maxSize() != mLimitNumber)) {
                mChildStates = new android.support.v4.util.LruCache<java.lang.String, android.util.SparseArray<android.os.Parcelable>>(mLimitNumber);
            }
        } else
            if ((mSavePolicy == android.support.v17.leanback.widget.BaseGridView.SAVE_ALL_CHILD) || (mSavePolicy == android.support.v17.leanback.widget.BaseGridView.SAVE_ON_SCREEN_CHILD)) {
                if ((mChildStates == null) || (mChildStates.maxSize() != android.support.v17.leanback.widget.ViewsStateBundle.UNLIMITED)) {
                    mChildStates = new android.support.v4.util.LruCache<java.lang.String, android.util.SparseArray<android.os.Parcelable>>(android.support.v17.leanback.widget.ViewsStateBundle.UNLIMITED);
                }
            } else {
                mChildStates = null;
            }

    }

    /**
     * Load view from states, it's none operation if the there is no state associated with the id.
     *
     * @param view
     * 		view where loads into
     * @param id
     * 		unique id for the view within this ViewsStateBundle
     */
    public final void loadView(android.view.View view, int id) {
        if (mChildStates != null) {
            java.lang.String key = android.support.v17.leanback.widget.ViewsStateBundle.getSaveStatesKey(id);
            // Once loaded the state, do not keep the state of child. The child state will
            // be saved again either when child is offscreen or when the parent is saved.
            android.util.SparseArray<android.os.Parcelable> container = mChildStates.remove(key);
            if (container != null) {
                view.restoreHierarchyState(container);
            }
        }
    }

    /**
     * Save views regardless what's the current policy is.
     *
     * @param view
     * 		view to save
     * @param id
     * 		unique id for the view within this ViewsStateBundle
     */
    protected final void saveViewUnchecked(android.view.View view, int id) {
        if (mChildStates != null) {
            java.lang.String key = android.support.v17.leanback.widget.ViewsStateBundle.getSaveStatesKey(id);
            android.util.SparseArray<android.os.Parcelable> container = new android.util.SparseArray<android.os.Parcelable>();
            view.saveHierarchyState(container);
            mChildStates.put(key, container);
        }
    }

    /**
     * The on screen view is saved when policy is not {@link #SAVE_NO_CHILD}.
     *
     * @param bundle
     * 		Bundle where we save the on screen view state.  If null,
     * 		a new Bundle is created and returned.
     * @param view
     * 		The view to save.
     * @param id
     * 		Id of the view.
     */
    public final android.os.Bundle saveOnScreenView(android.os.Bundle bundle, android.view.View view, int id) {
        if (mSavePolicy != android.support.v17.leanback.widget.BaseGridView.SAVE_NO_CHILD) {
            java.lang.String key = android.support.v17.leanback.widget.ViewsStateBundle.getSaveStatesKey(id);
            android.util.SparseArray<android.os.Parcelable> container = new android.util.SparseArray<android.os.Parcelable>();
            view.saveHierarchyState(container);
            if (bundle == null) {
                bundle = new android.os.Bundle();
            }
            bundle.putSparseParcelableArray(key, container);
        }
        return bundle;
    }

    /**
     * Save off screen views according to policy.
     *
     * @param view
     * 		view to save
     * @param id
     * 		unique id for the view within this ViewsStateBundle
     */
    public final void saveOffscreenView(android.view.View view, int id) {
        switch (mSavePolicy) {
            case android.support.v17.leanback.widget.BaseGridView.SAVE_LIMITED_CHILD :
            case android.support.v17.leanback.widget.BaseGridView.SAVE_ALL_CHILD :
                saveViewUnchecked(view, id);
                break;
            case android.support.v17.leanback.widget.BaseGridView.SAVE_ON_SCREEN_CHILD :
                remove(id);
                break;
            default :
                break;
        }
    }

    static java.lang.String getSaveStatesKey(int id) {
        return java.lang.Integer.toString(id);
    }
}

