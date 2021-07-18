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
package android.support.v7.util;


class MessageThreadUtil<T> implements android.support.v7.util.ThreadUtil<T> {
    @java.lang.Override
    public android.support.v7.util.ThreadUtil.MainThreadCallback<T> getMainThreadProxy(final android.support.v7.util.ThreadUtil.MainThreadCallback<T> callback) {
        return new android.support.v7.util.ThreadUtil.MainThreadCallback<T>() {
            final android.support.v7.util.MessageThreadUtil.MessageQueue mQueue = new android.support.v7.util.MessageThreadUtil.MessageQueue();

            private final android.os.Handler mMainThreadHandler = new android.os.Handler(android.os.Looper.getMainLooper());

            static final int UPDATE_ITEM_COUNT = 1;

            static final int ADD_TILE = 2;

            static final int REMOVE_TILE = 3;

            @java.lang.Override
            public void updateItemCount(int generation, int itemCount) {
                sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(UPDATE_ITEM_COUNT, generation, itemCount));
            }

            @java.lang.Override
            public void addTile(int generation, android.support.v7.util.TileList.Tile<T> tile) {
                sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(ADD_TILE, generation, tile));
            }

            @java.lang.Override
            public void removeTile(int generation, int position) {
                sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(REMOVE_TILE, generation, position));
            }

            private void sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem msg) {
                mQueue.sendMessage(msg);
                mMainThreadHandler.post(mMainThreadRunnable);
            }

            private java.lang.Runnable mMainThreadRunnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    android.support.v7.util.MessageThreadUtil.SyncQueueItem msg = mQueue.next();
                    while (msg != null) {
                        switch (msg.what) {
                            case UPDATE_ITEM_COUNT :
                                callback.updateItemCount(msg.arg1, msg.arg2);
                                break;
                            case ADD_TILE :
                                // noinspection unchecked
                                callback.addTile(msg.arg1, ((android.support.v7.util.TileList.Tile<T>) (msg.data)));
                                break;
                            case REMOVE_TILE :
                                callback.removeTile(msg.arg1, msg.arg2);
                                break;
                            default :
                                android.util.Log.e("ThreadUtil", "Unsupported message, what=" + msg.what);
                        }
                        msg = mQueue.next();
                    } 
                }
            };
        };
    }

    @java.lang.Override
    public android.support.v7.util.ThreadUtil.BackgroundCallback<T> getBackgroundProxy(final android.support.v7.util.ThreadUtil.BackgroundCallback<T> callback) {
        return new android.support.v7.util.ThreadUtil.BackgroundCallback<T>() {
            final android.support.v7.util.MessageThreadUtil.MessageQueue mQueue = new android.support.v7.util.MessageThreadUtil.MessageQueue();

            private final java.util.concurrent.Executor mExecutor = android.support.v4.content.ParallelExecutorCompat.getParallelExecutor();

            java.util.concurrent.atomic.AtomicBoolean mBackgroundRunning = new java.util.concurrent.atomic.AtomicBoolean(false);

            static final int REFRESH = 1;

            static final int UPDATE_RANGE = 2;

            static final int LOAD_TILE = 3;

            static final int RECYCLE_TILE = 4;

            @java.lang.Override
            public void refresh(int generation) {
                sendMessageAtFrontOfQueue(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(REFRESH, generation, null));
            }

            @java.lang.Override
            public void updateRange(int rangeStart, int rangeEnd, int extRangeStart, int extRangeEnd, int scrollHint) {
                sendMessageAtFrontOfQueue(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(UPDATE_RANGE, rangeStart, rangeEnd, extRangeStart, extRangeEnd, scrollHint, null));
            }

            @java.lang.Override
            public void loadTile(int position, int scrollHint) {
                sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(LOAD_TILE, position, scrollHint));
            }

            @java.lang.Override
            public void recycleTile(android.support.v7.util.TileList.Tile<T> tile) {
                sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(RECYCLE_TILE, 0, tile));
            }

            private void sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem msg) {
                mQueue.sendMessage(msg);
                maybeExecuteBackgroundRunnable();
            }

            private void sendMessageAtFrontOfQueue(android.support.v7.util.MessageThreadUtil.SyncQueueItem msg) {
                mQueue.sendMessageAtFrontOfQueue(msg);
                maybeExecuteBackgroundRunnable();
            }

            private void maybeExecuteBackgroundRunnable() {
                if (mBackgroundRunning.compareAndSet(false, true)) {
                    mExecutor.execute(mBackgroundRunnable);
                }
            }

            private java.lang.Runnable mBackgroundRunnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    while (true) {
                        android.support.v7.util.MessageThreadUtil.SyncQueueItem msg = mQueue.next();
                        if (msg == null) {
                            break;
                        }
                        switch (msg.what) {
                            case REFRESH :
                                mQueue.removeMessages(REFRESH);
                                callback.refresh(msg.arg1);
                                break;
                            case UPDATE_RANGE :
                                mQueue.removeMessages(UPDATE_RANGE);
                                mQueue.removeMessages(LOAD_TILE);
                                callback.updateRange(msg.arg1, msg.arg2, msg.arg3, msg.arg4, msg.arg5);
                                break;
                            case LOAD_TILE :
                                callback.loadTile(msg.arg1, msg.arg2);
                                break;
                            case RECYCLE_TILE :
                                // noinspection unchecked
                                callback.recycleTile(((android.support.v7.util.TileList.Tile<T>) (msg.data)));
                                break;
                            default :
                                android.util.Log.e("ThreadUtil", "Unsupported message, what=" + msg.what);
                        }
                    } 
                    mBackgroundRunning.set(false);
                }
            };
        };
    }

    /**
     * Replica of android.os.Message. Unfortunately, cannot use it without a Handler and don't want
     * to create a thread just for this component.
     */
    static class SyncQueueItem {
        private static android.support.v7.util.MessageThreadUtil.SyncQueueItem sPool;

        private static final java.lang.Object sPoolLock = new java.lang.Object();

        private android.support.v7.util.MessageThreadUtil.SyncQueueItem next;

        public int what;

        public int arg1;

        public int arg2;

        public int arg3;

        public int arg4;

        public int arg5;

        public java.lang.Object data;

        void recycle() {
            next = null;
            what = arg1 = arg2 = arg3 = arg4 = arg5 = 0;
            data = null;
            synchronized(android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPoolLock) {
                if (android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool != null) {
                    next = android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool;
                }
                android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool = this;
            }
        }

        static android.support.v7.util.MessageThreadUtil.SyncQueueItem obtainMessage(int what, int arg1, int arg2, int arg3, int arg4, int arg5, java.lang.Object data) {
            synchronized(android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPoolLock) {
                final android.support.v7.util.MessageThreadUtil.SyncQueueItem item;
                if (android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool == null) {
                    item = new android.support.v7.util.MessageThreadUtil.SyncQueueItem();
                } else {
                    item = android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool;
                    android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool = android.support.v7.util.MessageThreadUtil.SyncQueueItem.sPool.next;
                    item.next = null;
                }
                item.what = what;
                item.arg1 = arg1;
                item.arg2 = arg2;
                item.arg3 = arg3;
                item.arg4 = arg4;
                item.arg5 = arg5;
                item.data = data;
                return item;
            }
        }

        static android.support.v7.util.MessageThreadUtil.SyncQueueItem obtainMessage(int what, int arg1, int arg2) {
            return android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(what, arg1, arg2, 0, 0, 0, null);
        }

        static android.support.v7.util.MessageThreadUtil.SyncQueueItem obtainMessage(int what, int arg1, java.lang.Object data) {
            return android.support.v7.util.MessageThreadUtil.SyncQueueItem.obtainMessage(what, arg1, 0, 0, 0, 0, data);
        }
    }

    static class MessageQueue {
        private android.support.v7.util.MessageThreadUtil.SyncQueueItem mRoot;

        synchronized android.support.v7.util.MessageThreadUtil.SyncQueueItem next() {
            if (mRoot == null) {
                return null;
            }
            final android.support.v7.util.MessageThreadUtil.SyncQueueItem next = mRoot;
            mRoot = mRoot.next;
            return next;
        }

        synchronized void sendMessageAtFrontOfQueue(android.support.v7.util.MessageThreadUtil.SyncQueueItem item) {
            item.next = mRoot;
            mRoot = item;
        }

        synchronized void sendMessage(android.support.v7.util.MessageThreadUtil.SyncQueueItem item) {
            if (mRoot == null) {
                mRoot = item;
                return;
            }
            android.support.v7.util.MessageThreadUtil.SyncQueueItem last = mRoot;
            while (last.next != null) {
                last = last.next;
            } 
            last.next = item;
        }

        synchronized void removeMessages(int what) {
            while ((mRoot != null) && (mRoot.what == what)) {
                android.support.v7.util.MessageThreadUtil.SyncQueueItem item = mRoot;
                mRoot = mRoot.next;
                item.recycle();
            } 
            if (mRoot != null) {
                android.support.v7.util.MessageThreadUtil.SyncQueueItem prev = mRoot;
                android.support.v7.util.MessageThreadUtil.SyncQueueItem item = prev.next;
                while (item != null) {
                    android.support.v7.util.MessageThreadUtil.SyncQueueItem next = item.next;
                    if (item.what == what) {
                        prev.next = next;
                        item.recycle();
                    } else {
                        prev = item;
                    }
                    item = next;
                } 
            }
        }
    }
}

