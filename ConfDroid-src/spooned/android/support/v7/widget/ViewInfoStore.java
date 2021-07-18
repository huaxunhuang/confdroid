/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * This class abstracts all tracking for Views to run animations.
 */
class ViewInfoStore {
    private static final boolean DEBUG = false;

    /**
     * View data records for pre-layout
     */
    @android.support.annotation.VisibleForTesting
    final android.support.v4.util.ArrayMap<android.support.v7.widget.RecyclerView.ViewHolder, android.support.v7.widget.ViewInfoStore.InfoRecord> mLayoutHolderMap = new android.support.v4.util.ArrayMap<>();

    @android.support.annotation.VisibleForTesting
    final android.support.v4.util.LongSparseArray<android.support.v7.widget.RecyclerView.ViewHolder> mOldChangedHolders = new android.support.v4.util.LongSparseArray<>();

    /**
     * Clears the state and all existing tracking data
     */
    void clear() {
        mLayoutHolderMap.clear();
        mOldChangedHolders.clear();
    }

    /**
     * Adds the item information to the prelayout tracking
     *
     * @param holder
     * 		The ViewHolder whose information is being saved
     * @param info
     * 		The information to save
     */
    void addToPreLayout(android.support.v7.widget.RecyclerView.ViewHolder holder, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo info) {
        android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = android.support.v7.widget.ViewInfoStore.InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.preInfo = info;
        record.flags |= android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE;
    }

    boolean isDisappearing(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        final android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(holder);
        return (record != null) && ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED) != 0);
    }

    /**
     * Finds the ItemHolderInfo for the given ViewHolder in preLayout list and removes it.
     *
     * @param vh
     * 		The ViewHolder whose information is being queried
     * @return The ItemHolderInfo for the given ViewHolder or null if it does not exist
     */
    @android.support.annotation.Nullable
    android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(android.support.v7.widget.RecyclerView.ViewHolder vh) {
        return popFromLayoutStep(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE);
    }

    /**
     * Finds the ItemHolderInfo for the given ViewHolder in postLayout list and removes it.
     *
     * @param vh
     * 		The ViewHolder whose information is being queried
     * @return The ItemHolderInfo for the given ViewHolder or null if it does not exist
     */
    @android.support.annotation.Nullable
    android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(android.support.v7.widget.RecyclerView.ViewHolder vh) {
        return popFromLayoutStep(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST);
    }

    private android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(android.support.v7.widget.RecyclerView.ViewHolder vh, int flag) {
        int index = mLayoutHolderMap.indexOfKey(vh);
        if (index < 0) {
            return null;
        }
        final android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.valueAt(index);
        if ((record != null) && ((record.flags & flag) != 0)) {
            record.flags &= ~flag;
            final android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo info;
            if (flag == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE) {
                info = record.preInfo;
            } else
                if (flag == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST) {
                    info = record.postInfo;
                } else {
                    throw new java.lang.IllegalArgumentException("Must provide flag PRE or POST");
                }

            // if not pre-post flag is left, clear.
            if ((record.flags & (android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE | android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST)) == 0) {
                mLayoutHolderMap.removeAt(index);
                android.support.v7.widget.ViewInfoStore.InfoRecord.recycle(record);
            }
            return info;
        }
        return null;
    }

    /**
     * Adds the given ViewHolder to the oldChangeHolders list
     *
     * @param key
     * 		The key to identify the ViewHolder.
     * @param holder
     * 		The ViewHolder to store
     */
    void addToOldChangeHolders(long key, android.support.v7.widget.RecyclerView.ViewHolder holder) {
        mOldChangedHolders.put(key, holder);
    }

    /**
     * Adds the given ViewHolder to the appeared in pre layout list. These are Views added by the
     * LayoutManager during a pre-layout pass. We distinguish them from other views that were
     * already in the pre-layout so that ItemAnimator can choose to run a different animation for
     * them.
     *
     * @param holder
     * 		The ViewHolder to store
     * @param info
     * 		The information to save
     */
    void addToAppearedInPreLayoutHolders(android.support.v7.widget.RecyclerView.ViewHolder holder, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo info) {
        android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = android.support.v7.widget.ViewInfoStore.InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.flags |= android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR;
        record.preInfo = info;
    }

    /**
     * Checks whether the given ViewHolder is in preLayout list
     *
     * @param viewHolder
     * 		The ViewHolder to query
     * @return True if the ViewHolder is present in preLayout, false otherwise
     */
    boolean isInPreLayout(android.support.v7.widget.RecyclerView.ViewHolder viewHolder) {
        final android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(viewHolder);
        return (record != null) && ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE) != 0);
    }

    /**
     * Queries the oldChangeHolder list for the given key. If they are not tracked, simply returns
     * null.
     *
     * @param key
     * 		The key to be used to find the ViewHolder.
     * @return A ViewHolder if exists or null if it does not exist.
     */
    android.support.v7.widget.RecyclerView.ViewHolder getFromOldChangeHolders(long key) {
        return mOldChangedHolders.get(key);
    }

    /**
     * Adds the item information to the post layout list
     *
     * @param holder
     * 		The ViewHolder whose information is being saved
     * @param info
     * 		The information to save
     */
    void addToPostLayout(android.support.v7.widget.RecyclerView.ViewHolder holder, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo info) {
        android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = android.support.v7.widget.ViewInfoStore.InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.postInfo = info;
        record.flags |= android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST;
    }

    /**
     * A ViewHolder might be added by the LayoutManager just to animate its disappearance.
     * This list holds such items so that we can animate / recycle these ViewHolders properly.
     *
     * @param holder
     * 		The ViewHolder which disappeared during a layout.
     */
    void addToDisappearedInLayout(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = android.support.v7.widget.ViewInfoStore.InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.flags |= android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED;
    }

    /**
     * Removes a ViewHolder from disappearing list.
     *
     * @param holder
     * 		The ViewHolder to be removed from the disappearing list.
     */
    void removeFromDisappearedInLayout(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            return;
        }
        record.flags &= ~android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED;
    }

    void process(android.support.v7.widget.ViewInfoStore.ProcessCallback callback) {
        for (int index = mLayoutHolderMap.size() - 1; index >= 0; index--) {
            final android.support.v7.widget.RecyclerView.ViewHolder viewHolder = mLayoutHolderMap.keyAt(index);
            final android.support.v7.widget.ViewInfoStore.InfoRecord record = mLayoutHolderMap.removeAt(index);
            if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR_AND_DISAPPEAR) == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR_AND_DISAPPEAR) {
                // Appeared then disappeared. Not useful for animations.
                callback.unused(viewHolder);
            } else
                if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED) != 0) {
                    // Set as "disappeared" by the LayoutManager (addDisappearingView)
                    if (record.preInfo == null) {
                        // similar to appear disappear but happened between different layout passes.
                        // this can happen when the layout manager is using auto-measure
                        callback.unused(viewHolder);
                    } else {
                        callback.processDisappeared(viewHolder, record.preInfo, record.postInfo);
                    }
                } else
                    if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR_PRE_AND_POST) == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR_PRE_AND_POST) {
                        // Appeared in the layout but not in the adapter (e.g. entered the viewport)
                        callback.processAppeared(viewHolder, record.preInfo, record.postInfo);
                    } else
                        if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE_AND_POST) == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE_AND_POST) {
                            // Persistent in both passes. Animate persistence
                            callback.processPersistent(viewHolder, record.preInfo, record.postInfo);
                        } else
                            if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE) != 0) {
                                // Was in pre-layout, never been added to post layout
                                callback.processDisappeared(viewHolder, record.preInfo, null);
                            } else
                                if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST) != 0) {
                                    // Was not in pre-layout, been added to post layout
                                    callback.processAppeared(viewHolder, record.preInfo, record.postInfo);
                                } else
                                    if ((record.flags & android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR) != 0) {
                                        // Scrap view. RecyclerView will handle removing/recycling this.
                                    } else
                                        if (android.support.v7.widget.ViewInfoStore.DEBUG) {
                                            throw new java.lang.IllegalStateException("record without any reasonable flag combination:/");
                                        }







            android.support.v7.widget.ViewInfoStore.InfoRecord.recycle(record);
        }
    }

    /**
     * Removes the ViewHolder from all list
     *
     * @param holder
     * 		The ViewHolder which we should stop tracking
     */
    void removeViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        for (int i = mOldChangedHolders.size() - 1; i >= 0; i--) {
            if (holder == mOldChangedHolders.valueAt(i)) {
                mOldChangedHolders.removeAt(i);
                break;
            }
        }
        final android.support.v7.widget.ViewInfoStore.InfoRecord info = mLayoutHolderMap.remove(holder);
        if (info != null) {
            android.support.v7.widget.ViewInfoStore.InfoRecord.recycle(info);
        }
    }

    void onDetach() {
        android.support.v7.widget.ViewInfoStore.InfoRecord.drainCache();
    }

    public void onViewDetached(android.support.v7.widget.RecyclerView.ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }

    interface ProcessCallback {
        void processDisappeared(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, @android.support.annotation.NonNull
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo, @android.support.annotation.Nullable
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo postInfo);

        void processAppeared(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, @android.support.annotation.Nullable
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo postInfo);

        void processPersistent(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, @android.support.annotation.NonNull
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo, @android.support.annotation.NonNull
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo postInfo);

        void unused(android.support.v7.widget.RecyclerView.ViewHolder holder);
    }

    static class InfoRecord {
        // disappearing list
        static final int FLAG_DISAPPEARED = 1;

        // appear in pre layout list
        static final int FLAG_APPEAR = 1 << 1;

        // pre layout, this is necessary to distinguish null item info
        static final int FLAG_PRE = 1 << 2;

        // post layout, this is necessary to distinguish null item info
        static final int FLAG_POST = 1 << 3;

        static final int FLAG_APPEAR_AND_DISAPPEAR = android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR | android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED;

        static final int FLAG_PRE_AND_POST = android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE | android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST;

        static final int FLAG_APPEAR_PRE_AND_POST = (android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR | android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE) | android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST;

        int flags;

        @android.support.annotation.Nullable
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

        @android.support.annotation.Nullable
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo postInfo;

        static android.support.v4.util.Pools.Pool<android.support.v7.widget.ViewInfoStore.InfoRecord> sPool = new android.support.v4.util.Pools.SimplePool<>(20);

        private InfoRecord() {
        }

        static android.support.v7.widget.ViewInfoStore.InfoRecord obtain() {
            android.support.v7.widget.ViewInfoStore.InfoRecord record = android.support.v7.widget.ViewInfoStore.InfoRecord.sPool.acquire();
            return record == null ? new android.support.v7.widget.ViewInfoStore.InfoRecord() : record;
        }

        static void recycle(android.support.v7.widget.ViewInfoStore.InfoRecord record) {
            record.flags = 0;
            record.preInfo = null;
            record.postInfo = null;
            android.support.v7.widget.ViewInfoStore.InfoRecord.sPool.release(record);
        }

        static void drainCache() {
            // noinspection StatementWithEmptyBody
            while (android.support.v7.widget.ViewInfoStore.InfoRecord.sPool.acquire() != null);
        }
    }
}

