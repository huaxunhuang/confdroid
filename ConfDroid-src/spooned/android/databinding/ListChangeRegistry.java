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
package android.databinding;


/**
 * Utility class for managing ObservableList callbacks.
 */
public class ListChangeRegistry extends android.databinding.CallbackRegistry<android.databinding.ObservableList.OnListChangedCallback, android.databinding.ObservableList, android.databinding.ListChangeRegistry.ListChanges> {
    private static final android.support.v4.util.Pools.SynchronizedPool<android.databinding.ListChangeRegistry.ListChanges> sListChanges = new android.support.v4.util.Pools.SynchronizedPool<android.databinding.ListChangeRegistry.ListChanges>(10);

    private static final int ALL = 0;

    private static final int CHANGED = 1;

    private static final int INSERTED = 2;

    private static final int MOVED = 3;

    private static final int REMOVED = 4;

    private static final android.databinding.CallbackRegistry.NotifierCallback<android.databinding.ObservableList.OnListChangedCallback, android.databinding.ObservableList, android.databinding.ListChangeRegistry.ListChanges> NOTIFIER_CALLBACK = new android.databinding.CallbackRegistry.NotifierCallback<android.databinding.ObservableList.OnListChangedCallback, android.databinding.ObservableList, android.databinding.ListChangeRegistry.ListChanges>() {
        @java.lang.Override
        public void onNotifyCallback(android.databinding.ObservableList.OnListChangedCallback callback, android.databinding.ObservableList sender, int notificationType, android.databinding.ListChangeRegistry.ListChanges listChanges) {
            switch (notificationType) {
                case android.databinding.ListChangeRegistry.CHANGED :
                    callback.onItemRangeChanged(sender, listChanges.start, listChanges.count);
                    break;
                case android.databinding.ListChangeRegistry.INSERTED :
                    callback.onItemRangeInserted(sender, listChanges.start, listChanges.count);
                    break;
                case android.databinding.ListChangeRegistry.MOVED :
                    callback.onItemRangeMoved(sender, listChanges.start, listChanges.to, listChanges.count);
                    break;
                case android.databinding.ListChangeRegistry.REMOVED :
                    callback.onItemRangeRemoved(sender, listChanges.start, listChanges.count);
                    break;
                default :
                    callback.onChanged(sender);
                    break;
            }
        }
    };

    /**
     * Notify registered callbacks that there was an unknown or whole-list change.
     *
     * @param list
     * 		The list that changed.
     */
    public void notifyChanged(android.databinding.ObservableList list) {
        notifyCallbacks(list, android.databinding.ListChangeRegistry.ALL, null);
    }

    /**
     * Notify registered callbacks that some elements have changed.
     *
     * @param list
     * 		The list that changed.
     * @param start
     * 		The index of the first changed element.
     * @param count
     * 		The number of changed elements.
     */
    public void notifyChanged(android.databinding.ObservableList list, int start, int count) {
        android.databinding.ListChangeRegistry.ListChanges listChanges = android.databinding.ListChangeRegistry.acquire(start, 0, count);
        notifyCallbacks(list, android.databinding.ListChangeRegistry.CHANGED, listChanges);
    }

    /**
     * Notify registered callbacks that elements were inserted.
     *
     * @param list
     * 		The list that changed.
     * @param start
     * 		The index where the elements were inserted.
     * @param count
     * 		The number of elements that were inserted.
     */
    public void notifyInserted(android.databinding.ObservableList list, int start, int count) {
        android.databinding.ListChangeRegistry.ListChanges listChanges = android.databinding.ListChangeRegistry.acquire(start, 0, count);
        notifyCallbacks(list, android.databinding.ListChangeRegistry.INSERTED, listChanges);
    }

    /**
     * Notify registered callbacks that elements were moved.
     *
     * @param list
     * 		The list that changed.
     * @param from
     * 		The index of the first element moved.
     * @param to
     * 		The index of where the element was moved to.
     * @param count
     * 		The number of elements moved.
     */
    public void notifyMoved(android.databinding.ObservableList list, int from, int to, int count) {
        android.databinding.ListChangeRegistry.ListChanges listChanges = android.databinding.ListChangeRegistry.acquire(from, to, count);
        notifyCallbacks(list, android.databinding.ListChangeRegistry.MOVED, listChanges);
    }

    /**
     * Notify registered callbacks that elements were deleted.
     *
     * @param list
     * 		The list that changed.
     * @param start
     * 		The index of the first element to be removed.
     * @param count
     * 		The number of elements removed.
     */
    public void notifyRemoved(android.databinding.ObservableList list, int start, int count) {
        android.databinding.ListChangeRegistry.ListChanges listChanges = android.databinding.ListChangeRegistry.acquire(start, 0, count);
        notifyCallbacks(list, android.databinding.ListChangeRegistry.REMOVED, listChanges);
    }

    private static android.databinding.ListChangeRegistry.ListChanges acquire(int start, int to, int count) {
        android.databinding.ListChangeRegistry.ListChanges listChanges = android.databinding.ListChangeRegistry.sListChanges.acquire();
        if (listChanges == null) {
            listChanges = new android.databinding.ListChangeRegistry.ListChanges();
        }
        listChanges.start = start;
        listChanges.to = to;
        listChanges.count = count;
        return listChanges;
    }

    @java.lang.Override
    public synchronized void notifyCallbacks(android.databinding.ObservableList sender, int notificationType, android.databinding.ListChangeRegistry.ListChanges listChanges) {
        super.notifyCallbacks(sender, notificationType, listChanges);
        if (listChanges != null) {
            android.databinding.ListChangeRegistry.sListChanges.release(listChanges);
        }
    }

    public ListChangeRegistry() {
        super(android.databinding.ListChangeRegistry.NOTIFIER_CALLBACK);
    }

    static class ListChanges {
        public int start;

        public int count;

        public int to;
    }
}

