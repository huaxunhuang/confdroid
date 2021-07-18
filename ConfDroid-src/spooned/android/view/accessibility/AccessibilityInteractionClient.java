/**
 * * Copyright 2011, The Android Open Source Project
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * *     http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 */
package android.view.accessibility;


/**
 * This class is a singleton that performs accessibility interaction
 * which is it queries remote view hierarchies about snapshots of their
 * views as well requests from these hierarchies to perform certain
 * actions on their views.
 *
 * Rationale: The content retrieval APIs are synchronous from a client's
 *     perspective but internally they are asynchronous. The client thread
 *     calls into the system requesting an action and providing a callback
 *     to receive the result after which it waits up to a timeout for that
 *     result. The system enforces security and the delegates the request
 *     to a given view hierarchy where a message is posted (from a binder
 *     thread) describing what to be performed by the main UI thread the
 *     result of which it delivered via the mentioned callback. However,
 *     the blocked client thread and the main UI thread of the target view
 *     hierarchy can be the same thread, for example an accessibility service
 *     and an activity run in the same process, thus they are executed on the
 *     same main thread. In such a case the retrieval will fail since the UI
 *     thread that has to process the message describing the work to be done
 *     is blocked waiting for a result is has to compute! To avoid this scenario
 *     when making a call the client also passes its process and thread ids so
 *     the accessed view hierarchy can detect if the client making the request
 *     is running in its main UI thread. In such a case the view hierarchy,
 *     specifically the binder thread performing the IPC to it, does not post a
 *     message to be run on the UI thread but passes it to the singleton
 *     interaction client through which all interactions occur and the latter is
 *     responsible to execute the message before starting to wait for the
 *     asynchronous result delivered via the callback. In this case the expected
 *     result is already received so no waiting is performed.
 *
 * @unknown 
 */
public final class AccessibilityInteractionClient extends android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub {
    public static final int NO_ID = -1;

    private static final java.lang.String LOG_TAG = "AccessibilityInteractionClient";

    private static final boolean DEBUG = false;

    private static final boolean CHECK_INTEGRITY = true;

    private static final long TIMEOUT_INTERACTION_MILLIS = 5000;

    private static final java.lang.Object sStaticLock = new java.lang.Object();

    private static final android.util.LongSparseArray<android.view.accessibility.AccessibilityInteractionClient> sClients = new android.util.LongSparseArray();

    private static final android.util.SparseArray<android.accessibilityservice.IAccessibilityServiceConnection> sConnectionCache = new android.util.SparseArray();

    private static android.view.accessibility.AccessibilityCache sAccessibilityCache = new android.view.accessibility.AccessibilityCache(new android.view.accessibility.AccessibilityCache.AccessibilityNodeRefresher());

    private final java.util.concurrent.atomic.AtomicInteger mInteractionIdCounter = new java.util.concurrent.atomic.AtomicInteger();

    private final java.lang.Object mInstanceLock = new java.lang.Object();

    private volatile int mInteractionId = -1;

    private android.view.accessibility.AccessibilityNodeInfo mFindAccessibilityNodeInfoResult;

    private java.util.List<android.view.accessibility.AccessibilityNodeInfo> mFindAccessibilityNodeInfosResult;

    private boolean mPerformAccessibilityActionResult;

    private android.os.Message mSameThreadMessage;

    /**
     *
     *
     * @return The client for the current thread.
     */
    @android.annotation.UnsupportedAppUsage
    public static android.view.accessibility.AccessibilityInteractionClient getInstance() {
        final long threadId = java.lang.Thread.currentThread().getId();
        return android.view.accessibility.AccessibilityInteractionClient.getInstanceForThread(threadId);
    }

    /**
     * <strong>Note:</strong> We keep one instance per interrogating thread since
     * the instance contains state which can lead to undesired thread interleavings.
     * We do not have a thread local variable since other threads should be able to
     * look up the correct client knowing a thread id. See ViewRootImpl for details.
     *
     * @return The client for a given <code>threadId</code>.
     */
    public static android.view.accessibility.AccessibilityInteractionClient getInstanceForThread(long threadId) {
        synchronized(android.view.accessibility.AccessibilityInteractionClient.sStaticLock) {
            android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.sClients.get(threadId);
            if (client == null) {
                client = new android.view.accessibility.AccessibilityInteractionClient();
                android.view.accessibility.AccessibilityInteractionClient.sClients.put(threadId, client);
            }
            return client;
        }
    }

    /**
     * Gets a cached accessibility service connection.
     *
     * @param connectionId
     * 		The connection id.
     * @return The cached connection if such.
     */
    public static android.accessibilityservice.IAccessibilityServiceConnection getConnection(int connectionId) {
        synchronized(android.view.accessibility.AccessibilityInteractionClient.sConnectionCache) {
            return android.view.accessibility.AccessibilityInteractionClient.sConnectionCache.get(connectionId);
        }
    }

    /**
     * Adds a cached accessibility service connection.
     *
     * @param connectionId
     * 		The connection id.
     * @param connection
     * 		The connection.
     */
    public static void addConnection(int connectionId, android.accessibilityservice.IAccessibilityServiceConnection connection) {
        synchronized(android.view.accessibility.AccessibilityInteractionClient.sConnectionCache) {
            android.view.accessibility.AccessibilityInteractionClient.sConnectionCache.put(connectionId, connection);
        }
    }

    /**
     * Removes a cached accessibility service connection.
     *
     * @param connectionId
     * 		The connection id.
     */
    public static void removeConnection(int connectionId) {
        synchronized(android.view.accessibility.AccessibilityInteractionClient.sConnectionCache) {
            android.view.accessibility.AccessibilityInteractionClient.sConnectionCache.remove(connectionId);
        }
    }

    /**
     * This method is only for testing. Replacing the cache is a generally terrible idea, but
     * tests need to be able to verify this class's interactions with the cache
     */
    @com.android.internal.annotations.VisibleForTesting
    public static void setCache(android.view.accessibility.AccessibilityCache cache) {
        android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache = cache;
    }

    private AccessibilityInteractionClient() {
        /* reducing constructor visibility */
    }

    /**
     * Sets the message to be processed if the interacted view hierarchy
     * and the interacting client are running in the same thread.
     *
     * @param message
     * 		The message.
     */
    @android.annotation.UnsupportedAppUsage
    public void setSameThreadMessage(android.os.Message message) {
        synchronized(mInstanceLock) {
            mSameThreadMessage = message;
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Gets the root {@link AccessibilityNodeInfo} in the currently active window.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @return The root {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public android.view.accessibility.AccessibilityNodeInfo getRootInActiveWindow(int connectionId) {
        return findAccessibilityNodeInfoByAccessibilityId(connectionId, android.view.accessibility.AccessibilityWindowInfo.ACTIVE_WINDOW_ID, android.view.accessibility.AccessibilityNodeInfo.ROOT_NODE_ID, false, android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS, null);
    }

    /**
     * Gets the info for a window.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @return The {@link AccessibilityWindowInfo}.
     */
    public android.view.accessibility.AccessibilityWindowInfo getWindow(int connectionId, int accessibilityWindowId) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                android.view.accessibility.AccessibilityWindowInfo window = android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.getWindow(accessibilityWindowId);
                if (window != null) {
                    if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                        android.util.Log.i(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Window cache hit");
                    }
                    return window;
                }
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.i(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Window cache miss");
                }
                final long identityToken = android.os.Binder.clearCallingIdentity();
                try {
                    window = connection.getWindow(accessibilityWindowId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (window != null) {
                    android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.addWindow(window);
                    return window;
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote getWindow", re);
        }
        return null;
    }

    /**
     * Gets the info for all windows.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @return The {@link AccessibilityWindowInfo} list.
     */
    public java.util.List<android.view.accessibility.AccessibilityWindowInfo> getWindows(int connectionId) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                java.util.List<android.view.accessibility.AccessibilityWindowInfo> windows = android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.getWindows();
                if (windows != null) {
                    if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                        android.util.Log.i(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Windows cache hit");
                    }
                    return windows;
                }
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.i(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Windows cache miss");
                }
                final long identityToken = android.os.Binder.clearCallingIdentity();
                try {
                    windows = connection.getWindows();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (windows != null) {
                    android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.setWindows(windows);
                    return windows;
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote getWindows", re);
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Finds an {@link AccessibilityNodeInfo} by accessibility id.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @param accessibilityNodeId
     * 		A unique view id or virtual descendant id from
     * 		where to start the search. Use
     * 		{@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     * 		to start from the root.
     * @param bypassCache
     * 		Whether to bypass the cache while looking for the node.
     * @param prefetchFlags
     * 		flags to guide prefetching.
     * @return An {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public android.view.accessibility.AccessibilityNodeInfo findAccessibilityNodeInfoByAccessibilityId(int connectionId, int accessibilityWindowId, long accessibilityNodeId, boolean bypassCache, int prefetchFlags, android.os.Bundle arguments) {
        if (((prefetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_SIBLINGS) != 0) && ((prefetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_PREDECESSORS) == 0)) {
            throw new java.lang.IllegalArgumentException("FLAG_PREFETCH_SIBLINGS" + " requires FLAG_PREFETCH_PREDECESSORS");
        }
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                if (!bypassCache) {
                    android.view.accessibility.AccessibilityNodeInfo cachedInfo = android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.getNode(accessibilityWindowId, accessibilityNodeId);
                    if (cachedInfo != null) {
                        if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                            android.util.Log.i(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Node cache hit for " + android.view.accessibility.AccessibilityInteractionClient.idToString(accessibilityWindowId, accessibilityNodeId));
                        }
                        return cachedInfo;
                    }
                    if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                        android.util.Log.i(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Node cache miss for " + android.view.accessibility.AccessibilityInteractionClient.idToString(accessibilityWindowId, accessibilityNodeId));
                    }
                }
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final long identityToken = android.os.Binder.clearCallingIdentity();
                final java.lang.String[] packageNames;
                try {
                    packageNames = connection.findAccessibilityNodeInfoByAccessibilityId(accessibilityWindowId, accessibilityNodeId, interactionId, this, prefetchFlags, java.lang.Thread.currentThread().getId(), arguments);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (packageNames != null) {
                    java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(interactionId);
                    finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, bypassCache, packageNames);
                    if ((infos != null) && (!infos.isEmpty())) {
                        for (int i = 1; i < infos.size(); i++) {
                            infos.get(i).recycle();
                        }
                        return infos.get(0);
                    }
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote" + " findAccessibilityNodeInfoByAccessibilityId", re);
        }
        return null;
    }

    private static java.lang.String idToString(int accessibilityWindowId, long accessibilityNodeId) {
        return (accessibilityWindowId + "/") + android.view.accessibility.AccessibilityNodeInfo.idToString(accessibilityNodeId);
    }

    /**
     * Finds an {@link AccessibilityNodeInfo} by View id. The search is performed in
     * the window whose id is specified and starts from the node whose accessibility
     * id is specified.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @param accessibilityNodeId
     * 		A unique view id or virtual descendant id from
     * 		where to start the search. Use
     * 		{@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     * 		to start from the root.
     * @param viewId
     * 		The fully qualified resource name of the view id to find.
     * @return An list of {@link AccessibilityNodeInfo} if found, empty list otherwise.
     */
    public java.util.List<android.view.accessibility.AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(int connectionId, int accessibilityWindowId, long accessibilityNodeId, java.lang.String viewId) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final long identityToken = android.os.Binder.clearCallingIdentity();
                final java.lang.String[] packageNames;
                try {
                    packageNames = connection.findAccessibilityNodeInfosByViewId(accessibilityWindowId, accessibilityNodeId, viewId, interactionId, this, java.lang.Thread.currentThread().getId());
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (packageNames != null) {
                    java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(interactionId);
                    if (infos != null) {
                        finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, false, packageNames);
                        return infos;
                    }
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote" + " findAccessibilityNodeInfoByViewIdInActiveWindow", re);
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Finds {@link AccessibilityNodeInfo}s by View text. The match is case
     * insensitive containment. The search is performed in the window whose
     * id is specified and starts from the node whose accessibility id is
     * specified.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @param accessibilityNodeId
     * 		A unique view id or virtual descendant id from
     * 		where to start the search. Use
     * 		{@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     * 		to start from the root.
     * @param text
     * 		The searched text.
     * @return A list of found {@link AccessibilityNodeInfo}s.
     */
    public java.util.List<android.view.accessibility.AccessibilityNodeInfo> findAccessibilityNodeInfosByText(int connectionId, int accessibilityWindowId, long accessibilityNodeId, java.lang.String text) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final long identityToken = android.os.Binder.clearCallingIdentity();
                final java.lang.String[] packageNames;
                try {
                    packageNames = connection.findAccessibilityNodeInfosByText(accessibilityWindowId, accessibilityNodeId, text, interactionId, this, java.lang.Thread.currentThread().getId());
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (packageNames != null) {
                    java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(interactionId);
                    if (infos != null) {
                        finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, false, packageNames);
                        return infos;
                    }
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote" + " findAccessibilityNodeInfosByViewText", re);
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Finds the {@link android.view.accessibility.AccessibilityNodeInfo} that has the
     * specified focus type. The search is performed in the window whose id is specified
     * and starts from the node whose accessibility id is specified.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @param accessibilityNodeId
     * 		A unique view id or virtual descendant id from
     * 		where to start the search. Use
     * 		{@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     * 		to start from the root.
     * @param focusType
     * 		The focus type.
     * @return The accessibility focused {@link AccessibilityNodeInfo}.
     */
    public android.view.accessibility.AccessibilityNodeInfo findFocus(int connectionId, int accessibilityWindowId, long accessibilityNodeId, int focusType) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final long identityToken = android.os.Binder.clearCallingIdentity();
                final java.lang.String[] packageNames;
                try {
                    packageNames = connection.findFocus(accessibilityWindowId, accessibilityNodeId, focusType, interactionId, this, java.lang.Thread.currentThread().getId());
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (packageNames != null) {
                    android.view.accessibility.AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(interactionId);
                    finalizeAndCacheAccessibilityNodeInfo(info, connectionId, false, packageNames);
                    return info;
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote findFocus", re);
        }
        return null;
    }

    /**
     * Finds the accessibility focused {@link android.view.accessibility.AccessibilityNodeInfo}.
     * The search is performed in the window whose id is specified and starts from the
     * node whose accessibility id is specified.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @param accessibilityNodeId
     * 		A unique view id or virtual descendant id from
     * 		where to start the search. Use
     * 		{@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     * 		to start from the root.
     * @param direction
     * 		The direction in which to search for focusable.
     * @return The accessibility focused {@link AccessibilityNodeInfo}.
     */
    public android.view.accessibility.AccessibilityNodeInfo focusSearch(int connectionId, int accessibilityWindowId, long accessibilityNodeId, int direction) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final long identityToken = android.os.Binder.clearCallingIdentity();
                final java.lang.String[] packageNames;
                try {
                    packageNames = connection.focusSearch(accessibilityWindowId, accessibilityNodeId, direction, interactionId, this, java.lang.Thread.currentThread().getId());
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (packageNames != null) {
                    android.view.accessibility.AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(interactionId);
                    finalizeAndCacheAccessibilityNodeInfo(info, connectionId, false, packageNames);
                    return info;
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote accessibilityFocusSearch", re);
        }
        return null;
    }

    /**
     * Performs an accessibility action on an {@link AccessibilityNodeInfo}.
     *
     * @param connectionId
     * 		The id of a connection for interacting with the system.
     * @param accessibilityWindowId
     * 		A unique window id. Use
     * 		{@link android.view.accessibility.AccessibilityWindowInfo#ACTIVE_WINDOW_ID}
     * 		to query the currently active window.
     * @param accessibilityNodeId
     * 		A unique view id or virtual descendant id from
     * 		where to start the search. Use
     * 		{@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     * 		to start from the root.
     * @param action
     * 		The action to perform.
     * @param arguments
     * 		Optional action arguments.
     * @return Whether the action was performed.
     */
    public boolean performAccessibilityAction(int connectionId, int accessibilityWindowId, long accessibilityNodeId, int action, android.os.Bundle arguments) {
        try {
            android.accessibilityservice.IAccessibilityServiceConnection connection = android.view.accessibility.AccessibilityInteractionClient.getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final long identityToken = android.os.Binder.clearCallingIdentity();
                final boolean success;
                try {
                    success = connection.performAccessibilityAction(accessibilityWindowId, accessibilityNodeId, action, arguments, interactionId, this, java.lang.Thread.currentThread().getId());
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
                if (success) {
                    return getPerformAccessibilityActionResultAndClear(interactionId);
                }
            } else {
                if (android.view.accessibility.AccessibilityInteractionClient.DEBUG) {
                    android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.w(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Error while calling remote performAccessibilityAction", re);
        }
        return false;
    }

    @android.annotation.UnsupportedAppUsage
    public void clearCache() {
        android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.clear();
    }

    public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.onAccessibilityEvent(event);
    }

    /**
     * Gets the the result of an async request that returns an {@link AccessibilityNodeInfo}.
     *
     * @param interactionId
     * 		The interaction id to match the result with the request.
     * @return The result {@link AccessibilityNodeInfo}.
     */
    private android.view.accessibility.AccessibilityNodeInfo getFindAccessibilityNodeInfoResultAndClear(int interactionId) {
        synchronized(mInstanceLock) {
            final boolean success = waitForResultTimedLocked(interactionId);
            android.view.accessibility.AccessibilityNodeInfo result = (success) ? mFindAccessibilityNodeInfoResult : null;
            clearResultLocked();
            return result;
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setFindAccessibilityNodeInfoResult(android.view.accessibility.AccessibilityNodeInfo info, int interactionId) {
        synchronized(mInstanceLock) {
            if (interactionId > mInteractionId) {
                mFindAccessibilityNodeInfoResult = info;
                mInteractionId = interactionId;
            }
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Gets the the result of an async request that returns {@link AccessibilityNodeInfo}s.
     *
     * @param interactionId
     * 		The interaction id to match the result with the request.
     * @return The result {@link AccessibilityNodeInfo}s.
     */
    private java.util.List<android.view.accessibility.AccessibilityNodeInfo> getFindAccessibilityNodeInfosResultAndClear(int interactionId) {
        synchronized(mInstanceLock) {
            final boolean success = waitForResultTimedLocked(interactionId);
            final java.util.List<android.view.accessibility.AccessibilityNodeInfo> result;
            if (success) {
                result = mFindAccessibilityNodeInfosResult;
            } else {
                result = java.util.Collections.emptyList();
            }
            clearResultLocked();
            if (android.os.Build.IS_DEBUGGABLE && android.view.accessibility.AccessibilityInteractionClient.CHECK_INTEGRITY) {
                checkFindAccessibilityNodeInfoResultIntegrity(result);
            }
            return result;
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setFindAccessibilityNodeInfosResult(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, int interactionId) {
        synchronized(mInstanceLock) {
            if (interactionId > mInteractionId) {
                if (infos != null) {
                    // If the call is not an IPC, i.e. it is made from the same process, we need to
                    // instantiate new result list to avoid passing internal instances to clients.
                    final boolean isIpcCall = android.os.Binder.getCallingPid() != java.lang.Process.myPid();
                    if (!isIpcCall) {
                        mFindAccessibilityNodeInfosResult = new java.util.ArrayList<>(infos);
                    } else {
                        mFindAccessibilityNodeInfosResult = infos;
                    }
                } else {
                    mFindAccessibilityNodeInfosResult = java.util.Collections.emptyList();
                }
                mInteractionId = interactionId;
            }
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Gets the result of a request to perform an accessibility action.
     *
     * @param interactionId
     * 		The interaction id to match the result with the request.
     * @return Whether the action was performed.
     */
    private boolean getPerformAccessibilityActionResultAndClear(int interactionId) {
        synchronized(mInstanceLock) {
            final boolean success = waitForResultTimedLocked(interactionId);
            final boolean result = (success) ? mPerformAccessibilityActionResult : false;
            clearResultLocked();
            return result;
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setPerformAccessibilityActionResult(boolean succeeded, int interactionId) {
        synchronized(mInstanceLock) {
            if (interactionId > mInteractionId) {
                mPerformAccessibilityActionResult = succeeded;
                mInteractionId = interactionId;
            }
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Clears the result state.
     */
    private void clearResultLocked() {
        mInteractionId = -1;
        mFindAccessibilityNodeInfoResult = null;
        mFindAccessibilityNodeInfosResult = null;
        mPerformAccessibilityActionResult = false;
    }

    /**
     * Waits up to a given bound for a result of a request and returns it.
     *
     * @param interactionId
     * 		The interaction id to match the result with the request.
     * @return Whether the result was received.
     */
    private boolean waitForResultTimedLocked(int interactionId) {
        long waitTimeMillis = android.view.accessibility.AccessibilityInteractionClient.TIMEOUT_INTERACTION_MILLIS;
        final long startTimeMillis = android.os.SystemClock.uptimeMillis();
        while (true) {
            try {
                android.os.Message sameProcessMessage = getSameProcessMessageAndClear();
                if (sameProcessMessage != null) {
                    sameProcessMessage.getTarget().handleMessage(sameProcessMessage);
                }
                if (mInteractionId == interactionId) {
                    return true;
                }
                if (mInteractionId > interactionId) {
                    return false;
                }
                final long elapsedTimeMillis = android.os.SystemClock.uptimeMillis() - startTimeMillis;
                waitTimeMillis = android.view.accessibility.AccessibilityInteractionClient.TIMEOUT_INTERACTION_MILLIS - elapsedTimeMillis;
                if (waitTimeMillis <= 0) {
                    return false;
                }
                mInstanceLock.wait(waitTimeMillis);
            } catch (java.lang.InterruptedException ie) {
                /* ignore */
            }
        } 
    }

    /**
     * Finalize an {@link AccessibilityNodeInfo} before passing it to the client.
     *
     * @param info
     * 		The info.
     * @param connectionId
     * 		The id of the connection to the system.
     * @param bypassCache
     * 		Whether or not to bypass the cache. The node is added to the cache if
     * 		this value is {@code false}
     * @param packageNames
     * 		The valid package names a node can come from.
     */
    private void finalizeAndCacheAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info, int connectionId, boolean bypassCache, java.lang.String[] packageNames) {
        if (info != null) {
            info.setConnectionId(connectionId);
            // Empty array means any package name is Okay
            if (!com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
                java.lang.CharSequence packageName = info.getPackageName();
                if ((packageName == null) || (!com.android.internal.util.ArrayUtils.contains(packageNames, packageName.toString()))) {
                    // If the node package not one of the valid ones, pick the top one - this
                    // is one of the packages running in the introspected UID.
                    info.setPackageName(packageNames[0]);
                }
            }
            info.setSealed(true);
            if (!bypassCache) {
                android.view.accessibility.AccessibilityInteractionClient.sAccessibilityCache.add(info);
            }
        }
    }

    /**
     * Finalize {@link AccessibilityNodeInfo}s before passing them to the client.
     *
     * @param infos
     * 		The {@link AccessibilityNodeInfo}s.
     * @param connectionId
     * 		The id of the connection to the system.
     * @param bypassCache
     * 		Whether or not to bypass the cache. The nodes are added to the cache if
     * 		this value is {@code false}
     * @param packageNames
     * 		The valid package names a node can come from.
     */
    private void finalizeAndCacheAccessibilityNodeInfos(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, int connectionId, boolean bypassCache, java.lang.String[] packageNames) {
        if (infos != null) {
            final int infosCount = infos.size();
            for (int i = 0; i < infosCount; i++) {
                android.view.accessibility.AccessibilityNodeInfo info = infos.get(i);
                finalizeAndCacheAccessibilityNodeInfo(info, connectionId, bypassCache, packageNames);
            }
        }
    }

    /**
     * Gets the message stored if the interacted and interacting
     * threads are the same.
     *
     * @return The message.
     */
    private android.os.Message getSameProcessMessageAndClear() {
        synchronized(mInstanceLock) {
            android.os.Message result = mSameThreadMessage;
            mSameThreadMessage = null;
            return result;
        }
    }

    /**
     * Checks whether the infos are a fully connected tree with no duplicates.
     *
     * @param infos
     * 		The result list to check.
     */
    private void checkFindAccessibilityNodeInfoResultIntegrity(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos) {
        if (infos.size() == 0) {
            return;
        }
        // Find the root node.
        android.view.accessibility.AccessibilityNodeInfo root = infos.get(0);
        final int infoCount = infos.size();
        for (int i = 1; i < infoCount; i++) {
            for (int j = i; j < infoCount; j++) {
                android.view.accessibility.AccessibilityNodeInfo candidate = infos.get(j);
                if (root.getParentNodeId() == candidate.getSourceNodeId()) {
                    root = candidate;
                    break;
                }
            }
        }
        if (root == null) {
            android.util.Log.e(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "No root.");
        }
        // Check for duplicates.
        java.util.HashSet<android.view.accessibility.AccessibilityNodeInfo> seen = new java.util.HashSet<>();
        java.util.Queue<android.view.accessibility.AccessibilityNodeInfo> fringe = new java.util.LinkedList<>();
        fringe.add(root);
        while (!fringe.isEmpty()) {
            android.view.accessibility.AccessibilityNodeInfo current = fringe.poll();
            if (!seen.add(current)) {
                android.util.Log.e(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, "Duplicate node.");
                return;
            }
            final int childCount = current.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final long childId = current.getChildId(i);
                for (int j = 0; j < infoCount; j++) {
                    android.view.accessibility.AccessibilityNodeInfo child = infos.get(j);
                    if (child.getSourceNodeId() == childId) {
                        fringe.add(child);
                    }
                }
            }
        } 
        final int disconnectedCount = infos.size() - seen.size();
        if (disconnectedCount > 0) {
            android.util.Log.e(android.view.accessibility.AccessibilityInteractionClient.LOG_TAG, disconnectedCount + " Disconnected nodes.");
        }
    }
}

