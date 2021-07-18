/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.view.accessibility;


/**
 * Cache for AccessibilityWindowInfos and AccessibilityNodeInfos.
 * It is updated when windows change or nodes change.
 *
 * @unknown 
 */
public class AccessibilityCache {
    private static final java.lang.String LOG_TAG = "AccessibilityCache";

    private static final boolean DEBUG = false;

    private static final boolean CHECK_INTEGRITY = android.os.Build.IS_ENG;

    /**
     * {@link AccessibilityEvent} types that are critical for the cache to stay up to date
     *
     * When adding new event types in {@link #onAccessibilityEvent}, please add it here also, to
     * make sure that the events are delivered to cache regardless of
     * {@link android.accessibilityservice.AccessibilityServiceInfo#eventTypes}
     */
    public static final int CACHE_CRITICAL_EVENTS_MASK = (((((((((android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED) | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED) | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED) | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) | android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) | android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED) | android.view.accessibility.AccessibilityEvent.TYPE_WINDOWS_CHANGED) | android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.view.accessibility.AccessibilityCache.AccessibilityNodeRefresher mAccessibilityNodeRefresher;

    private long mAccessibilityFocus = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;

    private long mInputFocus = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;

    private boolean mIsAllWindowsCached;

    private final android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> mWindowCache = new android.util.SparseArray();

    private final android.util.SparseArray<android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo>> mNodeCache = new android.util.SparseArray();

    private final android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> mTempWindowArray = new android.util.SparseArray();

    public AccessibilityCache(android.view.accessibility.AccessibilityCache.AccessibilityNodeRefresher nodeRefresher) {
        mAccessibilityNodeRefresher = nodeRefresher;
    }

    public void setWindows(java.util.List<android.view.accessibility.AccessibilityWindowInfo> windows) {
        synchronized(mLock) {
            if (android.view.accessibility.AccessibilityCache.DEBUG) {
                android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, "Set windows");
            }
            clearWindowCache();
            if (windows == null) {
                return;
            }
            final int windowCount = windows.size();
            for (int i = 0; i < windowCount; i++) {
                final android.view.accessibility.AccessibilityWindowInfo window = windows.get(i);
                addWindow(window);
            }
            mIsAllWindowsCached = true;
        }
    }

    public void addWindow(android.view.accessibility.AccessibilityWindowInfo window) {
        synchronized(mLock) {
            if (android.view.accessibility.AccessibilityCache.DEBUG) {
                android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, "Caching window: " + window.getId());
            }
            final int windowId = window.getId();
            mWindowCache.put(windowId, new android.view.accessibility.AccessibilityWindowInfo(window));
        }
    }

    /**
     * Notifies the cache that the something in the UI changed. As a result
     * the cache will either refresh some nodes or evict some nodes.
     *
     * Note: any event that ends up affecting the cache should also be present in
     * {@link #CACHE_CRITICAL_EVENTS_MASK}
     *
     * @param event
     * 		An event.
     */
    public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        synchronized(mLock) {
            final int eventType = event.getEventType();
            switch (eventType) {
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED :
                    {
                        if (mAccessibilityFocus != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID) {
                            refreshCachedNodeLocked(event.getWindowId(), mAccessibilityFocus);
                        }
                        mAccessibilityFocus = event.getSourceNodeId();
                        refreshCachedNodeLocked(event.getWindowId(), mAccessibilityFocus);
                    }
                    break;
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED :
                    {
                        if (mAccessibilityFocus == event.getSourceNodeId()) {
                            refreshCachedNodeLocked(event.getWindowId(), mAccessibilityFocus);
                            mAccessibilityFocus = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
                        }
                    }
                    break;
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED :
                    {
                        if (mInputFocus != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID) {
                            refreshCachedNodeLocked(event.getWindowId(), mInputFocus);
                        }
                        mInputFocus = event.getSourceNodeId();
                        refreshCachedNodeLocked(event.getWindowId(), mInputFocus);
                    }
                    break;
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED :
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED :
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED :
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED :
                    {
                        refreshCachedNodeLocked(event.getWindowId(), event.getSourceNodeId());
                    }
                    break;
                case android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED :
                    {
                        synchronized(mLock) {
                            final int windowId = event.getWindowId();
                            final long sourceId = event.getSourceNodeId();
                            if ((event.getContentChangeTypes() & android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) != 0) {
                                clearSubTreeLocked(windowId, sourceId);
                            } else {
                                refreshCachedNodeLocked(windowId, sourceId);
                            }
                        }
                    }
                    break;
                case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED :
                    {
                        clearSubTreeLocked(event.getWindowId(), event.getSourceNodeId());
                    }
                    break;
                case android.view.accessibility.AccessibilityEvent.TYPE_WINDOWS_CHANGED :
                case android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED :
                    {
                        clear();
                    }
                    break;
            }
        }
        if (android.view.accessibility.AccessibilityCache.CHECK_INTEGRITY) {
            checkIntegrity();
        }
    }

    private void refreshCachedNodeLocked(int windowId, long sourceId) {
        if (android.view.accessibility.AccessibilityCache.DEBUG) {
            android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, "Refreshing cached node.");
        }
        android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes = mNodeCache.get(windowId);
        if (nodes == null) {
            return;
        }
        android.view.accessibility.AccessibilityNodeInfo cachedInfo = nodes.get(sourceId);
        // If the source is not in the cache - nothing to do.
        if (cachedInfo == null) {
            return;
        }
        // The node changed so we will just refresh it right now.
        if (mAccessibilityNodeRefresher.refreshNode(cachedInfo, true)) {
            return;
        }
        // Weird, we could not refresh. Just evict the entire sub-tree.
        clearSubTreeLocked(windowId, sourceId);
    }

    /**
     * Gets a cached {@link AccessibilityNodeInfo} given the id of the hosting
     * window and the accessibility id of the node.
     *
     * @param windowId
     * 		The id of the window hosting the node.
     * @param accessibilityNodeId
     * 		The info accessibility node id.
     * @return The cached {@link AccessibilityNodeInfo} or null if such not found.
     */
    public android.view.accessibility.AccessibilityNodeInfo getNode(int windowId, long accessibilityNodeId) {
        synchronized(mLock) {
            android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes = mNodeCache.get(windowId);
            if (nodes == null) {
                return null;
            }
            android.view.accessibility.AccessibilityNodeInfo info = nodes.get(accessibilityNodeId);
            if (info != null) {
                // Return a copy since the client calls to AccessibilityNodeInfo#recycle()
                // will wipe the data of the cached info.
                info = new android.view.accessibility.AccessibilityNodeInfo(info);
            }
            if (android.view.accessibility.AccessibilityCache.DEBUG) {
                android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, (("get(" + accessibilityNodeId) + ") = ") + info);
            }
            return info;
        }
    }

    public java.util.List<android.view.accessibility.AccessibilityWindowInfo> getWindows() {
        synchronized(mLock) {
            if (!mIsAllWindowsCached) {
                return null;
            }
            final int windowCount = mWindowCache.size();
            if (windowCount > 0) {
                // Careful to return the windows in a decreasing layer order.
                android.util.SparseArray<android.view.accessibility.AccessibilityWindowInfo> sortedWindows = mTempWindowArray;
                sortedWindows.clear();
                for (int i = 0; i < windowCount; i++) {
                    android.view.accessibility.AccessibilityWindowInfo window = mWindowCache.valueAt(i);
                    sortedWindows.put(window.getLayer(), window);
                }
                // It's possible in transient conditions for two windows to share the same
                // layer, which results in sortedWindows being smaller than mWindowCache
                final int sortedWindowCount = sortedWindows.size();
                java.util.List<android.view.accessibility.AccessibilityWindowInfo> windows = new java.util.ArrayList<>(sortedWindowCount);
                for (int i = sortedWindowCount - 1; i >= 0; i--) {
                    android.view.accessibility.AccessibilityWindowInfo window = sortedWindows.valueAt(i);
                    windows.add(new android.view.accessibility.AccessibilityWindowInfo(window));
                    sortedWindows.removeAt(i);
                }
                return windows;
            }
            return null;
        }
    }

    public android.view.accessibility.AccessibilityWindowInfo getWindow(int windowId) {
        synchronized(mLock) {
            android.view.accessibility.AccessibilityWindowInfo window = mWindowCache.get(windowId);
            if (window != null) {
                return new android.view.accessibility.AccessibilityWindowInfo(window);
            }
            return null;
        }
    }

    /**
     * Caches an {@link AccessibilityNodeInfo}.
     *
     * @param info
     * 		The node to cache.
     */
    public void add(android.view.accessibility.AccessibilityNodeInfo info) {
        synchronized(mLock) {
            if (android.view.accessibility.AccessibilityCache.DEBUG) {
                android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, ("add(" + info) + ")");
            }
            final int windowId = info.getWindowId();
            android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes = mNodeCache.get(windowId);
            if (nodes == null) {
                nodes = new android.util.LongSparseArray();
                mNodeCache.put(windowId, nodes);
            }
            final long sourceId = info.getSourceNodeId();
            android.view.accessibility.AccessibilityNodeInfo oldInfo = nodes.get(sourceId);
            if (oldInfo != null) {
                // If the added node is in the cache we have to be careful if
                // the new one represents a source state where some of the
                // children have been removed to remove the descendants that
                // are no longer present.
                final android.util.LongArray newChildrenIds = info.getChildNodeIds();
                final int oldChildCount = oldInfo.getChildCount();
                for (int i = 0; i < oldChildCount; i++) {
                    final long oldChildId = oldInfo.getChildId(i);
                    // If the child is no longer present, remove the sub-tree.
                    if ((newChildrenIds == null) || (newChildrenIds.indexOf(oldChildId) < 0)) {
                        clearSubTreeLocked(windowId, oldChildId);
                    }
                    if (nodes.get(sourceId) == null) {
                        // We've removed (and thus recycled) this node because it was its own
                        // ancestor (the app gave us bad data), we can't continue using it.
                        // Clear the cache for this window and give up on adding the node.
                        clearNodesForWindowLocked(windowId);
                        return;
                    }
                }
                // Also be careful if the parent has changed since the new
                // parent may be a predecessor of the old parent which will
                // add cycles to the cache.
                final long oldParentId = oldInfo.getParentNodeId();
                if (info.getParentNodeId() != oldParentId) {
                    clearSubTreeLocked(windowId, oldParentId);
                }
            }
            // Cache a copy since the client calls to AccessibilityNodeInfo#recycle()
            // will wipe the data of the cached info.
            android.view.accessibility.AccessibilityNodeInfo clone = new android.view.accessibility.AccessibilityNodeInfo(info);
            nodes.put(sourceId, clone);
            if (clone.isAccessibilityFocused()) {
                if ((mAccessibilityFocus != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID) && (mAccessibilityFocus != sourceId)) {
                    refreshCachedNodeLocked(windowId, mAccessibilityFocus);
                }
                mAccessibilityFocus = sourceId;
            } else
                if (mAccessibilityFocus == sourceId) {
                    mAccessibilityFocus = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
                }

            if (clone.isFocused()) {
                mInputFocus = sourceId;
            }
        }
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        synchronized(mLock) {
            if (android.view.accessibility.AccessibilityCache.DEBUG) {
                android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, "clear()");
            }
            clearWindowCache();
            final int nodesForWindowCount = mNodeCache.size();
            for (int i = nodesForWindowCount - 1; i >= 0; i--) {
                final int windowId = mNodeCache.keyAt(i);
                clearNodesForWindowLocked(windowId);
            }
            mAccessibilityFocus = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
            mInputFocus = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        }
    }

    private void clearWindowCache() {
        mWindowCache.clear();
        mIsAllWindowsCached = false;
    }

    /**
     * Clears nodes for the window with the given id
     */
    private void clearNodesForWindowLocked(int windowId) {
        if (android.view.accessibility.AccessibilityCache.DEBUG) {
            android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, ("clearNodesForWindowLocked(" + windowId) + ")");
        }
        android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes = mNodeCache.get(windowId);
        if (nodes == null) {
            return;
        }
        mNodeCache.remove(windowId);
    }

    /**
     * Clears a subtree rooted at the node with the given id that is
     * hosted in a given window.
     *
     * @param windowId
     * 		The id of the hosting window.
     * @param rootNodeId
     * 		The root id.
     */
    private void clearSubTreeLocked(int windowId, long rootNodeId) {
        if (android.view.accessibility.AccessibilityCache.DEBUG) {
            android.util.Log.i(android.view.accessibility.AccessibilityCache.LOG_TAG, "Clearing cached subtree.");
        }
        android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes = mNodeCache.get(windowId);
        if (nodes != null) {
            clearSubTreeRecursiveLocked(nodes, rootNodeId);
        }
    }

    /**
     * Clears a subtree given a pointer to the root id and the nodes
     * in the hosting window.
     *
     * @param nodes
     * 		The nodes in the hosting window.
     * @param rootNodeId
     * 		The id of the root to evict.
     * @return {@code true} if the cache was cleared
     */
    private boolean clearSubTreeRecursiveLocked(android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes, long rootNodeId) {
        android.view.accessibility.AccessibilityNodeInfo current = nodes.get(rootNodeId);
        if (current == null) {
            // The node isn't in the cache, but its descendents might be.
            clear();
            return true;
        }
        nodes.remove(rootNodeId);
        final int childCount = current.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final long childNodeId = current.getChildId(i);
            if (clearSubTreeRecursiveLocked(nodes, childNodeId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the integrity of the cache which is nodes from different windows
     * are not mixed, there is a single active window, there is a single focused
     * window, for every window there are no duplicates nodes, all nodes for a
     * window are connected, for every window there is a single input focused
     * node, and for every window there is a single accessibility focused node.
     */
    public void checkIntegrity() {
        synchronized(mLock) {
            // Get the root.
            if ((mWindowCache.size() <= 0) && (mNodeCache.size() == 0)) {
                return;
            }
            android.view.accessibility.AccessibilityWindowInfo focusedWindow = null;
            android.view.accessibility.AccessibilityWindowInfo activeWindow = null;
            final int windowCount = mWindowCache.size();
            for (int i = 0; i < windowCount; i++) {
                android.view.accessibility.AccessibilityWindowInfo window = mWindowCache.valueAt(i);
                // Check for one active window.
                if (window.isActive()) {
                    if (activeWindow != null) {
                        android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, "Duplicate active window:" + window);
                    } else {
                        activeWindow = window;
                    }
                }
                // Check for one focused window.
                if (window.isFocused()) {
                    if (focusedWindow != null) {
                        android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, "Duplicate focused window:" + window);
                    } else {
                        focusedWindow = window;
                    }
                }
            }
            // Traverse the tree and do some checks.
            android.view.accessibility.AccessibilityNodeInfo accessFocus = null;
            android.view.accessibility.AccessibilityNodeInfo inputFocus = null;
            final int nodesForWindowCount = mNodeCache.size();
            for (int i = 0; i < nodesForWindowCount; i++) {
                android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodes = mNodeCache.valueAt(i);
                if (nodes.size() <= 0) {
                    continue;
                }
                android.util.ArraySet<android.view.accessibility.AccessibilityNodeInfo> seen = new android.util.ArraySet();
                final int windowId = mNodeCache.keyAt(i);
                final int nodeCount = nodes.size();
                for (int j = 0; j < nodeCount; j++) {
                    android.view.accessibility.AccessibilityNodeInfo node = nodes.valueAt(j);
                    // Check for duplicates
                    if (!seen.add(node)) {
                        android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, (("Duplicate node: " + node) + " in window:") + windowId);
                        // Stop now as we potentially found a loop.
                        continue;
                    }
                    // Check for one accessibility focus.
                    if (node.isAccessibilityFocused()) {
                        if (accessFocus != null) {
                            android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, (("Duplicate accessibility focus:" + node) + " in window:") + windowId);
                        } else {
                            accessFocus = node;
                        }
                    }
                    // Check for one input focus.
                    if (node.isFocused()) {
                        if (inputFocus != null) {
                            android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, (("Duplicate input focus: " + node) + " in window:") + windowId);
                        } else {
                            inputFocus = node;
                        }
                    }
                    // The node should be a child of its parent if we have the parent.
                    android.view.accessibility.AccessibilityNodeInfo nodeParent = nodes.get(node.getParentNodeId());
                    if (nodeParent != null) {
                        boolean childOfItsParent = false;
                        final int childCount = nodeParent.getChildCount();
                        for (int k = 0; k < childCount; k++) {
                            android.view.accessibility.AccessibilityNodeInfo child = nodes.get(nodeParent.getChildId(k));
                            if (child == node) {
                                childOfItsParent = true;
                                break;
                            }
                        }
                        if (!childOfItsParent) {
                            android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, (("Invalid parent-child relation between parent: " + nodeParent) + " and child: ") + node);
                        }
                    }
                    // The node should be the parent of its child if we have the child.
                    final int childCount = node.getChildCount();
                    for (int k = 0; k < childCount; k++) {
                        android.view.accessibility.AccessibilityNodeInfo child = nodes.get(node.getChildId(k));
                        if (child != null) {
                            android.view.accessibility.AccessibilityNodeInfo parent = nodes.get(child.getParentNodeId());
                            if (parent != node) {
                                android.util.Log.e(android.view.accessibility.AccessibilityCache.LOG_TAG, (("Invalid child-parent relation between child: " + node) + " and parent: ") + nodeParent);
                            }
                        }
                    }
                }
            }
        }
    }

    // Layer of indirection included to break dependency chain for testing
    public static class AccessibilityNodeRefresher {
        public boolean refreshNode(android.view.accessibility.AccessibilityNodeInfo info, boolean bypassCache) {
            return info.refresh(null, bypassCache);
        }
    }
}

