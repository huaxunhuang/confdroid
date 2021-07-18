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
package android.view;


/**
 * Class for managing accessibility interactions initiated from the system
 * and targeting the view hierarchy. A *ClientThread method is to be
 * called from the interaction connection ViewAncestor gives the system to
 * talk to it and a corresponding *UiThread method that is executed on the
 * UI thread.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class AccessibilityInteractionController {
    private static final java.lang.String LOG_TAG = "AccessibilityInteractionController";

    // Debugging flag
    private static final boolean ENFORCE_NODE_TREE_CONSISTENT = false;

    // Constants for readability
    private static final boolean IGNORE_REQUEST_PREPARERS = true;

    private static final boolean CONSIDER_REQUEST_PREPARERS = false;

    // If an app holds off accessibility for longer than this, the hold-off is canceled to prevent
    // accessibility from hanging
    private static final long REQUEST_PREPARER_TIMEOUT_MS = 500;

    private final java.util.ArrayList<android.view.accessibility.AccessibilityNodeInfo> mTempAccessibilityNodeInfoList = new java.util.ArrayList<android.view.accessibility.AccessibilityNodeInfo>();

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.view.AccessibilityInteractionController.PrivateHandler mHandler;

    private final android.view.ViewRootImpl mViewRootImpl;

    private final android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher mPrefetcher;

    private final long mMyLooperThreadId;

    private final int mMyProcessId;

    private final android.view.accessibility.AccessibilityManager mA11yManager;

    private final java.util.ArrayList<android.view.View> mTempArrayList = new java.util.ArrayList<android.view.View>();

    private final android.graphics.Point mTempPoint = new android.graphics.Point();

    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    private final android.graphics.Rect mTempRect1 = new android.graphics.Rect();

    private final android.graphics.Rect mTempRect2 = new android.graphics.Rect();

    private android.view.AccessibilityInteractionController.AddNodeInfosForViewId mAddNodeInfosForViewId;

    @com.android.internal.annotations.GuardedBy("mLock")
    private int mNumActiveRequestPreparers;

    @com.android.internal.annotations.GuardedBy("mLock")
    private java.util.List<android.view.AccessibilityInteractionController.MessageHolder> mMessagesWaitingForRequestPreparer;

    @com.android.internal.annotations.GuardedBy("mLock")
    private int mActiveRequestPreparerId;

    public AccessibilityInteractionController(android.view.ViewRootImpl viewRootImpl) {
        android.os.Looper looper = viewRootImpl.mHandler.getLooper();
        mMyLooperThreadId = looper.getThread().getId();
        mMyProcessId = java.lang.Process.myPid();
        mHandler = new android.view.AccessibilityInteractionController.PrivateHandler(looper);
        mViewRootImpl = viewRootImpl;
        mPrefetcher = new android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher();
        mA11yManager = mViewRootImpl.mContext.getSystemService(android.view.accessibility.AccessibilityManager.class);
    }

    private void scheduleMessage(android.os.Message message, int interrogatingPid, long interrogatingTid, boolean ignoreRequestPreparers) {
        if (ignoreRequestPreparers || (!holdOffMessageIfNeeded(message, interrogatingPid, interrogatingTid))) {
            // If the interrogation is performed by the same thread as the main UI
            // thread in this process, set the message as a static reference so
            // after this call completes the same thread but in the interrogating
            // client can handle the message to generate the result.
            if (((interrogatingPid == mMyProcessId) && (interrogatingTid == mMyLooperThreadId)) && mHandler.hasAccessibilityCallback(message)) {
                android.view.accessibility.AccessibilityInteractionClient.getInstanceForThread(interrogatingTid).setSameThreadMessage(message);
            } else {
                // For messages without callback of interrogating client, just handle the
                // message immediately if this is UI thread.
                if ((!mHandler.hasAccessibilityCallback(message)) && (java.lang.Thread.currentThread().getId() == mMyLooperThreadId)) {
                    mHandler.handleMessage(message);
                } else {
                    mHandler.sendMessage(message);
                }
            }
        }
    }

    private boolean isShown(android.view.View view) {
        return (view != null) && ((view.getWindowVisibility() == android.view.View.VISIBLE) && view.isShown());
    }

    public void findAccessibilityNodeInfoByAccessibilityIdClientThread(long accessibilityNodeId, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec, android.os.Bundle arguments) {
        final android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID;
        message.arg1 = flags;
        final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.argi1 = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        args.argi2 = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.argi3 = interactionId;
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = interactiveRegion;
        args.arg4 = arguments;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    /**
     * Check if this message needs to be held off while the app prepares to meet either this
     * request, or a request ahead of it.
     *
     * @param originalMessage
     * 		The message to be processed
     * @param callingPid
     * 		The calling process id
     * @param callingTid
     * 		The calling thread id
     * @return {@code true} if the message is held off and will be processed later, {@code false} if
    the message should be posted.
     */
    private boolean holdOffMessageIfNeeded(android.os.Message originalMessage, int callingPid, long callingTid) {
        synchronized(mLock) {
            // If a request is already pending, queue this request for when it's finished
            if (mNumActiveRequestPreparers != 0) {
                queueMessageToHandleOncePrepared(originalMessage, callingPid, callingTid);
                return true;
            }
            // Currently the only message that can hold things off is findByA11yId with extra data.
            if (originalMessage.what != android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID) {
                return false;
            }
            com.android.internal.os.SomeArgs originalMessageArgs = ((com.android.internal.os.SomeArgs) (originalMessage.obj));
            android.os.Bundle requestArguments = ((android.os.Bundle) (originalMessageArgs.arg4));
            if (requestArguments == null) {
                return false;
            }
            // If nothing it registered for this view, nothing to do
            int accessibilityViewId = originalMessageArgs.argi1;
            final java.util.List<android.view.accessibility.AccessibilityRequestPreparer> preparers = mA11yManager.getRequestPreparersForAccessibilityId(accessibilityViewId);
            if (preparers == null) {
                return false;
            }
            // If the bundle doesn't request the extra data, nothing to do
            final java.lang.String extraDataKey = requestArguments.getString(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_REQUESTED_KEY);
            if (extraDataKey == null) {
                return false;
            }
            // Send the request to the AccessibilityRequestPreparers on the UI thread
            mNumActiveRequestPreparers = preparers.size();
            for (int i = 0; i < preparers.size(); i++) {
                final android.os.Message requestPreparerMessage = mHandler.obtainMessage(android.view.AccessibilityInteractionController.PrivateHandler.MSG_PREPARE_FOR_EXTRA_DATA_REQUEST);
                final com.android.internal.os.SomeArgs requestPreparerArgs = com.android.internal.os.SomeArgs.obtain();
                // virtualDescendentId
                requestPreparerArgs.argi1 = (originalMessageArgs.argi2 == android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID) ? android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID : originalMessageArgs.argi2;
                requestPreparerArgs.arg1 = preparers.get(i);
                requestPreparerArgs.arg2 = extraDataKey;
                requestPreparerArgs.arg3 = requestArguments;
                android.os.Message preparationFinishedMessage = mHandler.obtainMessage(android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_FINISHED);
                preparationFinishedMessage.arg1 = ++mActiveRequestPreparerId;
                requestPreparerArgs.arg4 = preparationFinishedMessage;
                requestPreparerMessage.obj = requestPreparerArgs;
                scheduleMessage(requestPreparerMessage, callingPid, callingTid, android.view.AccessibilityInteractionController.IGNORE_REQUEST_PREPARERS);
                mHandler.obtainMessage(android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_TIMEOUT);
                mHandler.sendEmptyMessageDelayed(android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_TIMEOUT, android.view.AccessibilityInteractionController.REQUEST_PREPARER_TIMEOUT_MS);
            }
            // Set the initial request aside
            queueMessageToHandleOncePrepared(originalMessage, callingPid, callingTid);
            return true;
        }
    }

    private void prepareForExtraDataRequestUiThread(android.os.Message message) {
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final int virtualDescendantId = args.argi1;
        final android.view.accessibility.AccessibilityRequestPreparer preparer = ((android.view.accessibility.AccessibilityRequestPreparer) (args.arg1));
        final java.lang.String extraDataKey = ((java.lang.String) (args.arg2));
        final android.os.Bundle requestArguments = ((android.os.Bundle) (args.arg3));
        final android.os.Message preparationFinishedMessage = ((android.os.Message) (args.arg4));
        preparer.onPrepareExtraData(virtualDescendantId, extraDataKey, requestArguments, preparationFinishedMessage);
    }

    private void queueMessageToHandleOncePrepared(android.os.Message message, int interrogatingPid, long interrogatingTid) {
        if (mMessagesWaitingForRequestPreparer == null) {
            mMessagesWaitingForRequestPreparer = new java.util.ArrayList<>(1);
        }
        android.view.AccessibilityInteractionController.MessageHolder messageHolder = new android.view.AccessibilityInteractionController.MessageHolder(message, interrogatingPid, interrogatingTid);
        mMessagesWaitingForRequestPreparer.add(messageHolder);
    }

    private void requestPreparerDoneUiThread(android.os.Message message) {
        synchronized(mLock) {
            if (message.arg1 != mActiveRequestPreparerId) {
                android.util.Slog.e(android.view.AccessibilityInteractionController.LOG_TAG, "Surprising AccessibilityRequestPreparer callback (likely late)");
                return;
            }
            mNumActiveRequestPreparers--;
            if (mNumActiveRequestPreparers <= 0) {
                mHandler.removeMessages(android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_TIMEOUT);
                scheduleAllMessagesWaitingForRequestPreparerLocked();
            }
        }
    }

    private void requestPreparerTimeoutUiThread() {
        synchronized(mLock) {
            android.util.Slog.e(android.view.AccessibilityInteractionController.LOG_TAG, "AccessibilityRequestPreparer timed out");
            scheduleAllMessagesWaitingForRequestPreparerLocked();
        }
    }

    @com.android.internal.annotations.GuardedBy("mLock")
    private void scheduleAllMessagesWaitingForRequestPreparerLocked() {
        int numMessages = mMessagesWaitingForRequestPreparer.size();
        for (int i = 0; i < numMessages; i++) {
            android.view.AccessibilityInteractionController.MessageHolder request = mMessagesWaitingForRequestPreparer.get(i);
            /* the app is ready for the first request */
            scheduleMessage(request.mMessage, request.mInterrogatingPid, request.mInterrogatingTid, i == 0);
        }
        mMessagesWaitingForRequestPreparer.clear();
        mNumActiveRequestPreparers = 0;// Just to be safe - should be unnecessary

        mActiveRequestPreparerId = -1;
    }

    private void findAccessibilityNodeInfoByAccessibilityIdUiThread(android.os.Message message) {
        final int flags = message.arg1;
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final int accessibilityViewId = args.argi1;
        final int virtualDescendantId = args.argi2;
        final int interactionId = args.argi3;
        final android.view.accessibility.IAccessibilityInteractionConnectionCallback callback = ((android.view.accessibility.IAccessibilityInteractionConnectionCallback) (args.arg1));
        final android.view.MagnificationSpec spec = ((android.view.MagnificationSpec) (args.arg2));
        final android.graphics.Region interactiveRegion = ((android.graphics.Region) (args.arg3));
        final android.os.Bundle arguments = ((android.os.Bundle) (args.arg4));
        args.recycle();
        java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos = mTempAccessibilityNodeInfoList;
        infos.clear();
        try {
            if ((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) {
                return;
            }
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
            final android.view.View root = findViewByAccessibilityId(accessibilityViewId);
            if ((root != null) && isShown(root)) {
                mPrefetcher.prefetchAccessibilityNodeInfos(root, virtualDescendantId, flags, infos, arguments);
            }
        } finally {
            updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void findAccessibilityNodeInfosByViewIdClientThread(long accessibilityNodeId, java.lang.String viewId, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
        android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID;
        message.arg1 = flags;
        message.arg2 = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.argi1 = interactionId;
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = viewId;
        args.arg4 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void findAccessibilityNodeInfosByViewIdUiThread(android.os.Message message) {
        final int flags = message.arg1;
        final int accessibilityViewId = message.arg2;
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final int interactionId = args.argi1;
        final android.view.accessibility.IAccessibilityInteractionConnectionCallback callback = ((android.view.accessibility.IAccessibilityInteractionConnectionCallback) (args.arg1));
        final android.view.MagnificationSpec spec = ((android.view.MagnificationSpec) (args.arg2));
        final java.lang.String viewId = ((java.lang.String) (args.arg3));
        final android.graphics.Region interactiveRegion = ((android.graphics.Region) (args.arg4));
        args.recycle();
        final java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos = mTempAccessibilityNodeInfoList;
        infos.clear();
        try {
            if ((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) {
                return;
            }
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
            final android.view.View root = findViewByAccessibilityId(accessibilityViewId);
            if (root != null) {
                final int resolvedViewId = root.getContext().getResources().getIdentifier(viewId, null, null);
                if (resolvedViewId <= 0) {
                    return;
                }
                if (mAddNodeInfosForViewId == null) {
                    mAddNodeInfosForViewId = new android.view.AccessibilityInteractionController.AddNodeInfosForViewId();
                }
                mAddNodeInfosForViewId.init(resolvedViewId, infos);
                root.findViewByPredicate(mAddNodeInfosForViewId);
                mAddNodeInfosForViewId.reset();
            }
        } finally {
            updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void findAccessibilityNodeInfosByTextClientThread(long accessibilityNodeId, java.lang.String text, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
        android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT;
        message.arg1 = flags;
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = text;
        args.arg2 = callback;
        args.arg3 = spec;
        args.argi1 = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        args.argi2 = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.argi3 = interactionId;
        args.arg4 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void findAccessibilityNodeInfosByTextUiThread(android.os.Message message) {
        final int flags = message.arg1;
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final java.lang.String text = ((java.lang.String) (args.arg1));
        final android.view.accessibility.IAccessibilityInteractionConnectionCallback callback = ((android.view.accessibility.IAccessibilityInteractionConnectionCallback) (args.arg2));
        final android.view.MagnificationSpec spec = ((android.view.MagnificationSpec) (args.arg3));
        final int accessibilityViewId = args.argi1;
        final int virtualDescendantId = args.argi2;
        final int interactionId = args.argi3;
        final android.graphics.Region interactiveRegion = ((android.graphics.Region) (args.arg4));
        args.recycle();
        java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos = null;
        try {
            if ((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) {
                return;
            }
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
            final android.view.View root = findViewByAccessibilityId(accessibilityViewId);
            if ((root != null) && isShown(root)) {
                android.view.accessibility.AccessibilityNodeProvider provider = root.getAccessibilityNodeProvider();
                if (provider != null) {
                    infos = provider.findAccessibilityNodeInfosByText(text, virtualDescendantId);
                } else
                    if (virtualDescendantId == android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID) {
                        java.util.ArrayList<android.view.View> foundViews = mTempArrayList;
                        foundViews.clear();
                        root.findViewsWithText(foundViews, text, (android.view.View.FIND_VIEWS_WITH_TEXT | android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION) | android.view.View.FIND_VIEWS_WITH_ACCESSIBILITY_NODE_PROVIDERS);
                        if (!foundViews.isEmpty()) {
                            infos = mTempAccessibilityNodeInfoList;
                            infos.clear();
                            final int viewCount = foundViews.size();
                            for (int i = 0; i < viewCount; i++) {
                                android.view.View foundView = foundViews.get(i);
                                if (isShown(foundView)) {
                                    provider = foundView.getAccessibilityNodeProvider();
                                    if (provider != null) {
                                        java.util.List<android.view.accessibility.AccessibilityNodeInfo> infosFromProvider = provider.findAccessibilityNodeInfosByText(text, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
                                        if (infosFromProvider != null) {
                                            infos.addAll(infosFromProvider);
                                        }
                                    } else {
                                        infos.add(foundView.createAccessibilityNodeInfo());
                                    }
                                }
                            }
                        }
                    }

            }
        } finally {
            updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void findFocusClientThread(long accessibilityNodeId, int focusType, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
        android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_FOCUS;
        message.arg1 = flags;
        message.arg2 = focusType;
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.argi1 = interactionId;
        args.argi2 = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        args.argi3 = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void findFocusUiThread(android.os.Message message) {
        final int flags = message.arg1;
        final int focusType = message.arg2;
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final int interactionId = args.argi1;
        final int accessibilityViewId = args.argi2;
        final int virtualDescendantId = args.argi3;
        final android.view.accessibility.IAccessibilityInteractionConnectionCallback callback = ((android.view.accessibility.IAccessibilityInteractionConnectionCallback) (args.arg1));
        final android.view.MagnificationSpec spec = ((android.view.MagnificationSpec) (args.arg2));
        final android.graphics.Region interactiveRegion = ((android.graphics.Region) (args.arg3));
        args.recycle();
        android.view.accessibility.AccessibilityNodeInfo focused = null;
        try {
            if ((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) {
                return;
            }
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
            final android.view.View root = findViewByAccessibilityId(accessibilityViewId);
            if ((root != null) && isShown(root)) {
                switch (focusType) {
                    case android.view.accessibility.AccessibilityNodeInfo.FOCUS_ACCESSIBILITY :
                        {
                            android.view.View host = mViewRootImpl.mAccessibilityFocusedHost;
                            // If there is no accessibility focus host or it is not a descendant
                            // of the root from which to start the search, then the search failed.
                            if ((host == null) || (!android.view.ViewRootImpl.isViewDescendantOf(host, root))) {
                                break;
                            }
                            // The focused view not shown, we failed.
                            if (!isShown(host)) {
                                break;
                            }
                            // If the host has a provider ask this provider to search for the
                            // focus instead fetching all provider nodes to do the search here.
                            android.view.accessibility.AccessibilityNodeProvider provider = host.getAccessibilityNodeProvider();
                            if (provider != null) {
                                if (mViewRootImpl.mAccessibilityFocusedVirtualView != null) {
                                    focused = android.view.accessibility.AccessibilityNodeInfo.obtain(mViewRootImpl.mAccessibilityFocusedVirtualView);
                                }
                            } else
                                if (virtualDescendantId == android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID) {
                                    focused = host.createAccessibilityNodeInfo();
                                }

                        }
                        break;
                    case android.view.accessibility.AccessibilityNodeInfo.FOCUS_INPUT :
                        {
                            android.view.View target = root.findFocus();
                            if (!isShown(target)) {
                                break;
                            }
                            android.view.accessibility.AccessibilityNodeProvider provider = target.getAccessibilityNodeProvider();
                            if (provider != null) {
                                focused = provider.findFocus(focusType);
                            }
                            if (focused == null) {
                                focused = target.createAccessibilityNodeInfo();
                            }
                        }
                        break;
                    default :
                        throw new java.lang.IllegalArgumentException("Unknown focus type: " + focusType);
                }
            }
        } finally {
            updateInfoForViewportAndReturnFindNodeResult(focused, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void focusSearchClientThread(long accessibilityNodeId, int direction, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
        android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_FOCUS_SEARCH;
        message.arg1 = flags;
        message.arg2 = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.argi2 = direction;
        args.argi3 = interactionId;
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void focusSearchUiThread(android.os.Message message) {
        final int flags = message.arg1;
        final int accessibilityViewId = message.arg2;
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final int direction = args.argi2;
        final int interactionId = args.argi3;
        final android.view.accessibility.IAccessibilityInteractionConnectionCallback callback = ((android.view.accessibility.IAccessibilityInteractionConnectionCallback) (args.arg1));
        final android.view.MagnificationSpec spec = ((android.view.MagnificationSpec) (args.arg2));
        final android.graphics.Region interactiveRegion = ((android.graphics.Region) (args.arg3));
        args.recycle();
        android.view.accessibility.AccessibilityNodeInfo next = null;
        try {
            if ((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) {
                return;
            }
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
            final android.view.View root = findViewByAccessibilityId(accessibilityViewId);
            if ((root != null) && isShown(root)) {
                android.view.View nextView = root.focusSearch(direction);
                if (nextView != null) {
                    next = nextView.createAccessibilityNodeInfo();
                }
            }
        } finally {
            updateInfoForViewportAndReturnFindNodeResult(next, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void performAccessibilityActionClientThread(long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) {
        android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_PERFORM_ACCESSIBILITY_ACTION;
        message.arg1 = flags;
        message.arg2 = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.argi1 = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.argi2 = action;
        args.argi3 = interactionId;
        args.arg1 = callback;
        args.arg2 = arguments;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void performAccessibilityActionUiThread(android.os.Message message) {
        final int flags = message.arg1;
        final int accessibilityViewId = message.arg2;
        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
        final int virtualDescendantId = args.argi1;
        final int action = args.argi2;
        final int interactionId = args.argi3;
        final android.view.accessibility.IAccessibilityInteractionConnectionCallback callback = ((android.view.accessibility.IAccessibilityInteractionConnectionCallback) (args.arg1));
        android.os.Bundle arguments = ((android.os.Bundle) (args.arg2));
        args.recycle();
        boolean succeeded = false;
        try {
            if ((((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) || mViewRootImpl.mStopped) || mViewRootImpl.mPausedForTransition) {
                return;
            }
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
            final android.view.View target = findViewByAccessibilityId(accessibilityViewId);
            if ((target != null) && isShown(target)) {
                if (action == R.id.accessibilityActionClickOnClickableSpan) {
                    // Handle this hidden action separately
                    succeeded = handleClickableSpanActionUiThread(target, virtualDescendantId, arguments);
                } else {
                    android.view.accessibility.AccessibilityNodeProvider provider = target.getAccessibilityNodeProvider();
                    if (provider != null) {
                        succeeded = provider.performAction(virtualDescendantId, action, arguments);
                    } else
                        if (virtualDescendantId == android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID) {
                            succeeded = target.performAccessibilityAction(action, arguments);
                        }

                }
            }
        } finally {
            try {
                mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
                callback.setPerformAccessibilityActionResult(succeeded, interactionId);
            } catch (android.os.RemoteException re) {
                /* ignore - the other side will time out */
            }
        }
    }

    /**
     * Finds the accessibility focused node in the root, and clears the accessibility focus.
     */
    public void clearAccessibilityFocusClientThread() {
        final android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_CLEAR_ACCESSIBILITY_FOCUS;
        // Don't care about pid and tid because there's no interrogating client for this message.
        scheduleMessage(message, 0, 0, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void clearAccessibilityFocusUiThread() {
        if ((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) {
            return;
        }
        try {
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = android.view.accessibility.AccessibilityNodeInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
            final android.view.View root = mViewRootImpl.mView;
            if ((root != null) && isShown(root)) {
                final android.view.View host = mViewRootImpl.mAccessibilityFocusedHost;
                // If there is no accessibility focus host or it is not a descendant
                // of the root from which to start the search, then the search failed.
                if ((host == null) || (!android.view.ViewRootImpl.isViewDescendantOf(host, root))) {
                    return;
                }
                final android.view.accessibility.AccessibilityNodeProvider provider = host.getAccessibilityNodeProvider();
                final android.view.accessibility.AccessibilityNodeInfo focusNode = mViewRootImpl.mAccessibilityFocusedVirtualView;
                if ((provider != null) && (focusNode != null)) {
                    final int virtualNodeId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(focusNode.getSourceNodeId());
                    provider.performAction(virtualNodeId, android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
                } else {
                    host.performAccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
                }
            }
        } finally {
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
        }
    }

    /**
     * Notify outside touch event to the target window.
     */
    public void notifyOutsideTouchClientThread() {
        final android.os.Message message = mHandler.obtainMessage();
        message.what = android.view.AccessibilityInteractionController.PrivateHandler.MSG_NOTIFY_OUTSIDE_TOUCH;
        // Don't care about pid and tid because there's no interrogating client for this message.
        scheduleMessage(message, 0, 0, android.view.AccessibilityInteractionController.CONSIDER_REQUEST_PREPARERS);
    }

    private void notifyOutsideTouchUiThread() {
        if ((((mViewRootImpl.mView == null) || (mViewRootImpl.mAttachInfo == null)) || mViewRootImpl.mStopped) || mViewRootImpl.mPausedForTransition) {
            return;
        }
        final android.view.View root = mViewRootImpl.mView;
        if ((root != null) && isShown(root)) {
            // trigger ACTION_OUTSIDE to notify windows
            final long now = android.os.SystemClock.uptimeMillis();
            final android.view.MotionEvent event = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_OUTSIDE, 0, 0, 0);
            event.setSource(android.view.InputDevice.SOURCE_TOUCHSCREEN);
            mViewRootImpl.dispatchInputEvent(event);
        }
    }

    private android.view.View findViewByAccessibilityId(int accessibilityId) {
        if (accessibilityId == android.view.accessibility.AccessibilityNodeInfo.ROOT_ITEM_ID) {
            return mViewRootImpl.mView;
        } else {
            return android.view.accessibility.AccessibilityNodeIdManager.getInstance().findView(accessibilityId);
        }
    }

    private void applyAppScaleAndMagnificationSpecIfNeeded(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, android.view.MagnificationSpec spec) {
        if (infos == null) {
            return;
        }
        final float applicationScale = mViewRootImpl.mAttachInfo.mApplicationScale;
        if (shouldApplyAppScaleAndMagnificationSpec(applicationScale, spec)) {
            final int infoCount = infos.size();
            for (int i = 0; i < infoCount; i++) {
                android.view.accessibility.AccessibilityNodeInfo info = infos.get(i);
                applyAppScaleAndMagnificationSpecIfNeeded(info, spec);
            }
        }
    }

    private void adjustIsVisibleToUserIfNeeded(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, android.graphics.Region interactiveRegion) {
        if ((interactiveRegion == null) || (infos == null)) {
            return;
        }
        final int infoCount = infos.size();
        for (int i = 0; i < infoCount; i++) {
            android.view.accessibility.AccessibilityNodeInfo info = infos.get(i);
            adjustIsVisibleToUserIfNeeded(info, interactiveRegion);
        }
    }

    private void adjustIsVisibleToUserIfNeeded(android.view.accessibility.AccessibilityNodeInfo info, android.graphics.Region interactiveRegion) {
        if ((interactiveRegion == null) || (info == null)) {
            return;
        }
        android.graphics.Rect boundsInScreen = mTempRect;
        info.getBoundsInScreen(boundsInScreen);
        if (interactiveRegion.quickReject(boundsInScreen) && (!shouldBypassAdjustIsVisible())) {
            info.setVisibleToUser(false);
        }
    }

    private boolean shouldBypassAdjustIsVisible() {
        final int windowType = mViewRootImpl.mOrigWindowType;
        if (windowType == android.view.WindowManager.LayoutParams.TYPE_INPUT_METHOD) {
            return true;
        }
        return false;
    }

    private void applyAppScaleAndMagnificationSpecIfNeeded(android.view.accessibility.AccessibilityNodeInfo info, android.view.MagnificationSpec spec) {
        if (info == null) {
            return;
        }
        final float applicationScale = mViewRootImpl.mAttachInfo.mApplicationScale;
        if (!shouldApplyAppScaleAndMagnificationSpec(applicationScale, spec)) {
            return;
        }
        android.graphics.Rect boundsInParent = mTempRect;
        android.graphics.Rect boundsInScreen = mTempRect1;
        info.getBoundsInParent(boundsInParent);
        info.getBoundsInScreen(boundsInScreen);
        if (applicationScale != 1.0F) {
            boundsInParent.scale(applicationScale);
            boundsInScreen.scale(applicationScale);
        }
        if (spec != null) {
            boundsInParent.scale(spec.scale);
            // boundsInParent must not be offset.
            boundsInScreen.scale(spec.scale);
            boundsInScreen.offset(((int) (spec.offsetX)), ((int) (spec.offsetY)));
        }
        info.setBoundsInParent(boundsInParent);
        info.setBoundsInScreen(boundsInScreen);
        // Scale text locations if they are present
        if (info.hasExtras()) {
            android.os.Bundle extras = info.getExtras();
            android.os.Parcelable[] textLocations = extras.getParcelableArray(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY);
            if (textLocations != null) {
                for (int i = 0; i < textLocations.length; i++) {
                    // Unchecked cast - an app that puts other objects in this bundle with this
                    // key will crash.
                    android.graphics.RectF textLocation = ((android.graphics.RectF) (textLocations[i]));
                    textLocation.scale(applicationScale);
                    if (spec != null) {
                        textLocation.scale(spec.scale);
                        textLocation.offset(spec.offsetX, spec.offsetY);
                    }
                }
            }
        }
        if (spec != null) {
            android.view.View.AttachInfo attachInfo = mViewRootImpl.mAttachInfo;
            if (attachInfo.mDisplay == null) {
                return;
            }
            final float scale = attachInfo.mApplicationScale * spec.scale;
            android.graphics.Rect visibleWinFrame = mTempRect1;
            visibleWinFrame.left = ((int) ((attachInfo.mWindowLeft * scale) + spec.offsetX));
            visibleWinFrame.top = ((int) ((attachInfo.mWindowTop * scale) + spec.offsetY));
            visibleWinFrame.right = ((int) (visibleWinFrame.left + (mViewRootImpl.mWidth * scale)));
            visibleWinFrame.bottom = ((int) (visibleWinFrame.top + (mViewRootImpl.mHeight * scale)));
            attachInfo.mDisplay.getRealSize(mTempPoint);
            final int displayWidth = mTempPoint.x;
            final int displayHeight = mTempPoint.y;
            android.graphics.Rect visibleDisplayFrame = mTempRect2;
            visibleDisplayFrame.set(0, 0, displayWidth, displayHeight);
            if (!visibleWinFrame.intersect(visibleDisplayFrame)) {
                // If there's no intersection with display, set visibleWinFrame empty.
                visibleDisplayFrame.setEmpty();
            }
            if (!visibleWinFrame.intersects(boundsInScreen.left, boundsInScreen.top, boundsInScreen.right, boundsInScreen.bottom)) {
                info.setVisibleToUser(false);
            }
        }
    }

    private boolean shouldApplyAppScaleAndMagnificationSpec(float appScale, android.view.MagnificationSpec spec) {
        return (appScale != 1.0F) || ((spec != null) && (!spec.isNop()));
    }

    private void updateInfosForViewportAndReturnFindNodeResult(java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int interactionId, android.view.MagnificationSpec spec, android.graphics.Region interactiveRegion) {
        try {
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
            applyAppScaleAndMagnificationSpecIfNeeded(infos, spec);
            adjustIsVisibleToUserIfNeeded(infos, interactiveRegion);
            callback.setFindAccessibilityNodeInfosResult(infos, interactionId);
            if (infos != null) {
                infos.clear();
            }
        } catch (android.os.RemoteException re) {
            /* ignore - the other side will time out */
        } finally {
            recycleMagnificationSpecAndRegionIfNeeded(spec, interactiveRegion);
        }
    }

    private void updateInfoForViewportAndReturnFindNodeResult(android.view.accessibility.AccessibilityNodeInfo info, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int interactionId, android.view.MagnificationSpec spec, android.graphics.Region interactiveRegion) {
        try {
            mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
            applyAppScaleAndMagnificationSpecIfNeeded(info, spec);
            adjustIsVisibleToUserIfNeeded(info, interactiveRegion);
            callback.setFindAccessibilityNodeInfoResult(info, interactionId);
        } catch (android.os.RemoteException re) {
            /* ignore - the other side will time out */
        } finally {
            recycleMagnificationSpecAndRegionIfNeeded(spec, interactiveRegion);
        }
    }

    private void recycleMagnificationSpecAndRegionIfNeeded(android.view.MagnificationSpec spec, android.graphics.Region region) {
        if (android.os.android.os.Process.myPid() != android.os.Binder.getCallingPid()) {
            // Specs are cached in the system process and obtained from a pool when read from
            // a parcel, so only recycle the spec if called from another process.
            if (spec != null) {
                spec.recycle();
            }
        } else {
            // Regions are obtained in the system process and instantiated when read from
            // a parcel, so only recycle the region if caled from the same process.
            if (region != null) {
                region.recycle();
            }
        }
    }

    private boolean handleClickableSpanActionUiThread(android.view.View view, int virtualDescendantId, android.os.Bundle arguments) {
        android.os.Parcelable span = arguments.getParcelable(android.view.accessibility.AccessibilityNodeInfo.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN);
        if (!(span instanceof android.text.style.AccessibilityClickableSpan)) {
            return false;
        }
        // Find the original ClickableSpan if it's still on the screen
        android.view.accessibility.AccessibilityNodeInfo infoWithSpan = null;
        android.view.accessibility.AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
        if (provider != null) {
            infoWithSpan = provider.createAccessibilityNodeInfo(virtualDescendantId);
        } else
            if (virtualDescendantId == android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID) {
                infoWithSpan = view.createAccessibilityNodeInfo();
            }

        if (infoWithSpan == null) {
            return false;
        }
        // Click on the corresponding span
        android.text.style.ClickableSpan clickableSpan = ((android.text.style.AccessibilityClickableSpan) (span)).findClickableSpan(infoWithSpan.getOriginalText());
        if (clickableSpan != null) {
            clickableSpan.onClick(view);
            return true;
        }
        return false;
    }

    /**
     * This class encapsulates a prefetching strategy for the accessibility APIs for
     * querying window content. It is responsible to prefetch a batch of
     * AccessibilityNodeInfos in addition to the one for a requested node.
     */
    private class AccessibilityNodePrefetcher {
        private static final int MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE = 50;

        private final java.util.ArrayList<android.view.View> mTempViewList = new java.util.ArrayList<android.view.View>();

        public void prefetchAccessibilityNodeInfos(android.view.View view, int virtualViewId, int fetchFlags, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos, android.os.Bundle arguments) {
            android.view.accessibility.AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
            // Determine if we'll be populating extra data
            final java.lang.String extraDataRequested = (arguments == null) ? null : arguments.getString(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_REQUESTED_KEY);
            if (provider == null) {
                android.view.accessibility.AccessibilityNodeInfo root = view.createAccessibilityNodeInfo();
                if (root != null) {
                    if (extraDataRequested != null) {
                        view.addExtraDataToAccessibilityNodeInfo(root, extraDataRequested, arguments);
                    }
                    outInfos.add(root);
                    if ((fetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_PREDECESSORS) != 0) {
                        prefetchPredecessorsOfRealNode(view, outInfos);
                    }
                    if ((fetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_SIBLINGS) != 0) {
                        prefetchSiblingsOfRealNode(view, outInfos);
                    }
                    if ((fetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS) != 0) {
                        prefetchDescendantsOfRealNode(view, outInfos);
                    }
                }
            } else {
                final android.view.accessibility.AccessibilityNodeInfo root = provider.createAccessibilityNodeInfo(virtualViewId);
                if (root != null) {
                    if (extraDataRequested != null) {
                        provider.addExtraDataToAccessibilityNodeInfo(virtualViewId, root, extraDataRequested, arguments);
                    }
                    outInfos.add(root);
                    if ((fetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_PREDECESSORS) != 0) {
                        prefetchPredecessorsOfVirtualNode(root, view, provider, outInfos);
                    }
                    if ((fetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_SIBLINGS) != 0) {
                        prefetchSiblingsOfVirtualNode(root, view, provider, outInfos);
                    }
                    if ((fetchFlags & android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS) != 0) {
                        prefetchDescendantsOfVirtualNode(root, provider, outInfos);
                    }
                }
            }
            if (android.view.AccessibilityInteractionController.ENFORCE_NODE_TREE_CONSISTENT) {
                enforceNodeTreeConsistent(outInfos);
            }
        }

        private void enforceNodeTreeConsistent(java.util.List<android.view.accessibility.AccessibilityNodeInfo> nodes) {
            android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo> nodeMap = new android.util.LongSparseArray<android.view.accessibility.AccessibilityNodeInfo>();
            final int nodeCount = nodes.size();
            for (int i = 0; i < nodeCount; i++) {
                android.view.accessibility.AccessibilityNodeInfo node = nodes.get(i);
                nodeMap.put(node.getSourceNodeId(), node);
            }
            // If the nodes are a tree it does not matter from
            // which node we start to search for the root.
            android.view.accessibility.AccessibilityNodeInfo root = nodeMap.valueAt(0);
            android.view.accessibility.AccessibilityNodeInfo parent = root;
            while (parent != null) {
                root = parent;
                parent = nodeMap.get(parent.getParentNodeId());
            } 
            // Traverse the tree and do some checks.
            android.view.accessibility.AccessibilityNodeInfo accessFocus = null;
            android.view.accessibility.AccessibilityNodeInfo inputFocus = null;
            java.util.HashSet<android.view.accessibility.AccessibilityNodeInfo> seen = new java.util.HashSet<android.view.accessibility.AccessibilityNodeInfo>();
            java.util.Queue<android.view.accessibility.AccessibilityNodeInfo> fringe = new java.util.LinkedList<android.view.accessibility.AccessibilityNodeInfo>();
            fringe.add(root);
            while (!fringe.isEmpty()) {
                android.view.accessibility.AccessibilityNodeInfo current = fringe.poll();
                // Check for duplicates
                if (!seen.add(current)) {
                    throw new java.lang.IllegalStateException((("Duplicate node: " + current) + " in window:") + mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
                }
                // Check for one accessibility focus.
                if (current.isAccessibilityFocused()) {
                    if (accessFocus != null) {
                        throw new java.lang.IllegalStateException((("Duplicate accessibility focus:" + current) + " in window:") + mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
                    } else {
                        accessFocus = current;
                    }
                }
                // Check for one input focus.
                if (current.isFocused()) {
                    if (inputFocus != null) {
                        throw new java.lang.IllegalStateException((("Duplicate input focus: " + current) + " in window:") + mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
                    } else {
                        inputFocus = current;
                    }
                }
                final int childCount = current.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    final long childId = current.getChildId(j);
                    final android.view.accessibility.AccessibilityNodeInfo child = nodeMap.get(childId);
                    if (child != null) {
                        fringe.add(child);
                    }
                }
            } 
            // Check for disconnected nodes.
            for (int j = nodeMap.size() - 1; j >= 0; j--) {
                android.view.accessibility.AccessibilityNodeInfo info = nodeMap.valueAt(j);
                if (!seen.contains(info)) {
                    throw new java.lang.IllegalStateException("Disconnected node: " + info);
                }
            }
        }

        private void prefetchPredecessorsOfRealNode(android.view.View view, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos) {
            android.view.ViewParent parent = view.getParentForAccessibility();
            while ((parent instanceof android.view.View) && (outInfos.size() < android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE)) {
                android.view.View parentView = ((android.view.View) (parent));
                android.view.accessibility.AccessibilityNodeInfo info = parentView.createAccessibilityNodeInfo();
                if (info != null) {
                    outInfos.add(info);
                }
                parent = parent.getParentForAccessibility();
            } 
        }

        private void prefetchSiblingsOfRealNode(android.view.View current, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos) {
            android.view.ViewParent parent = current.getParentForAccessibility();
            if (parent instanceof android.view.ViewGroup) {
                android.view.ViewGroup parentGroup = ((android.view.ViewGroup) (parent));
                java.util.ArrayList<android.view.View> children = mTempViewList;
                children.clear();
                try {
                    parentGroup.addChildrenForAccessibility(children);
                    final int childCount = children.size();
                    for (int i = 0; i < childCount; i++) {
                        if (outInfos.size() >= android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                            return;
                        }
                        android.view.View child = children.get(i);
                        if ((child.getAccessibilityViewId() != current.getAccessibilityViewId()) && isShown(child)) {
                            android.view.accessibility.AccessibilityNodeInfo info = null;
                            android.view.accessibility.AccessibilityNodeProvider provider = child.getAccessibilityNodeProvider();
                            if (provider == null) {
                                info = child.createAccessibilityNodeInfo();
                            } else {
                                info = provider.createAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
                            }
                            if (info != null) {
                                outInfos.add(info);
                            }
                        }
                    }
                } finally {
                    children.clear();
                }
            }
        }

        private void prefetchDescendantsOfRealNode(android.view.View root, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos) {
            if (!(root instanceof android.view.ViewGroup)) {
                return;
            }
            java.util.HashMap<android.view.View, android.view.accessibility.AccessibilityNodeInfo> addedChildren = new java.util.HashMap<android.view.View, android.view.accessibility.AccessibilityNodeInfo>();
            java.util.ArrayList<android.view.View> children = mTempViewList;
            children.clear();
            try {
                root.addChildrenForAccessibility(children);
                final int childCount = children.size();
                for (int i = 0; i < childCount; i++) {
                    if (outInfos.size() >= android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                        return;
                    }
                    android.view.View child = children.get(i);
                    if (isShown(child)) {
                        android.view.accessibility.AccessibilityNodeProvider provider = child.getAccessibilityNodeProvider();
                        if (provider == null) {
                            android.view.accessibility.AccessibilityNodeInfo info = child.createAccessibilityNodeInfo();
                            if (info != null) {
                                outInfos.add(info);
                                addedChildren.put(child, null);
                            }
                        } else {
                            android.view.accessibility.AccessibilityNodeInfo info = provider.createAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
                            if (info != null) {
                                outInfos.add(info);
                                addedChildren.put(child, info);
                            }
                        }
                    }
                }
            } finally {
                children.clear();
            }
            if (outInfos.size() < android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                for (java.util.Map.Entry<android.view.View, android.view.accessibility.AccessibilityNodeInfo> entry : addedChildren.entrySet()) {
                    android.view.View addedChild = entry.getKey();
                    android.view.accessibility.AccessibilityNodeInfo virtualRoot = entry.getValue();
                    if (virtualRoot == null) {
                        prefetchDescendantsOfRealNode(addedChild, outInfos);
                    } else {
                        android.view.accessibility.AccessibilityNodeProvider provider = addedChild.getAccessibilityNodeProvider();
                        prefetchDescendantsOfVirtualNode(virtualRoot, provider, outInfos);
                    }
                }
            }
        }

        private void prefetchPredecessorsOfVirtualNode(android.view.accessibility.AccessibilityNodeInfo root, android.view.View providerHost, android.view.accessibility.AccessibilityNodeProvider provider, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos) {
            final int initialResultSize = outInfos.size();
            long parentNodeId = root.getParentNodeId();
            int accessibilityViewId = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(parentNodeId);
            while (accessibilityViewId != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID) {
                if (outInfos.size() >= android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                    return;
                }
                final int virtualDescendantId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(parentNodeId);
                if ((virtualDescendantId != android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID) || (accessibilityViewId == providerHost.getAccessibilityViewId())) {
                    final android.view.accessibility.AccessibilityNodeInfo parent;
                    parent = provider.createAccessibilityNodeInfo(virtualDescendantId);
                    if (parent == null) {
                        // Going up the parent relation we found a null predecessor,
                        // so remove these disconnected nodes form the result.
                        final int currentResultSize = outInfos.size();
                        for (int i = currentResultSize - 1; i >= initialResultSize; i--) {
                            outInfos.remove(i);
                        }
                        // Couldn't obtain the parent, which means we have a
                        // disconnected sub-tree. Abort prefetch immediately.
                        return;
                    }
                    outInfos.add(parent);
                    parentNodeId = parent.getParentNodeId();
                    accessibilityViewId = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(parentNodeId);
                } else {
                    prefetchPredecessorsOfRealNode(providerHost, outInfos);
                    return;
                }
            } 
        }

        private void prefetchSiblingsOfVirtualNode(android.view.accessibility.AccessibilityNodeInfo current, android.view.View providerHost, android.view.accessibility.AccessibilityNodeProvider provider, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos) {
            final long parentNodeId = current.getParentNodeId();
            final int parentAccessibilityViewId = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(parentNodeId);
            final int parentVirtualDescendantId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(parentNodeId);
            if ((parentVirtualDescendantId != android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID) || (parentAccessibilityViewId == providerHost.getAccessibilityViewId())) {
                final android.view.accessibility.AccessibilityNodeInfo parent = provider.createAccessibilityNodeInfo(parentVirtualDescendantId);
                if (parent != null) {
                    final int childCount = parent.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        if (outInfos.size() >= android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                            return;
                        }
                        final long childNodeId = parent.getChildId(i);
                        if (childNodeId != current.getSourceNodeId()) {
                            final int childVirtualDescendantId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(childNodeId);
                            android.view.accessibility.AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(childVirtualDescendantId);
                            if (child != null) {
                                outInfos.add(child);
                            }
                        }
                    }
                }
            } else {
                prefetchSiblingsOfRealNode(providerHost, outInfos);
            }
        }

        private void prefetchDescendantsOfVirtualNode(android.view.accessibility.AccessibilityNodeInfo root, android.view.accessibility.AccessibilityNodeProvider provider, java.util.List<android.view.accessibility.AccessibilityNodeInfo> outInfos) {
            final int initialOutInfosSize = outInfos.size();
            final int childCount = root.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (outInfos.size() >= android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                    return;
                }
                final long childNodeId = root.getChildId(i);
                android.view.accessibility.AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(childNodeId));
                if (child != null) {
                    outInfos.add(child);
                }
            }
            if (outInfos.size() < android.view.AccessibilityInteractionController.AccessibilityNodePrefetcher.MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE) {
                final int addedChildCount = outInfos.size() - initialOutInfosSize;
                for (int i = 0; i < addedChildCount; i++) {
                    android.view.accessibility.AccessibilityNodeInfo child = outInfos.get(initialOutInfosSize + i);
                    prefetchDescendantsOfVirtualNode(child, provider, outInfos);
                }
            }
        }
    }

    private class PrivateHandler extends android.os.Handler {
        private static final int MSG_PERFORM_ACCESSIBILITY_ACTION = 1;

        private static final int MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID = 2;

        private static final int MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID = 3;

        private static final int MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT = 4;

        private static final int MSG_FIND_FOCUS = 5;

        private static final int MSG_FOCUS_SEARCH = 6;

        private static final int MSG_PREPARE_FOR_EXTRA_DATA_REQUEST = 7;

        private static final int MSG_APP_PREPARATION_FINISHED = 8;

        private static final int MSG_APP_PREPARATION_TIMEOUT = 9;

        // Uses FIRST_NO_ACCESSIBILITY_CALLBACK_MSG for messages that don't need to call back
        // results to interrogating client.
        private static final int FIRST_NO_ACCESSIBILITY_CALLBACK_MSG = 100;

        private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS = android.view.AccessibilityInteractionController.PrivateHandler.FIRST_NO_ACCESSIBILITY_CALLBACK_MSG + 1;

        private static final int MSG_NOTIFY_OUTSIDE_TOUCH = android.view.AccessibilityInteractionController.PrivateHandler.FIRST_NO_ACCESSIBILITY_CALLBACK_MSG + 2;

        public PrivateHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public java.lang.String getMessageName(android.os.Message message) {
            final int type = message.what;
            switch (type) {
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_PERFORM_ACCESSIBILITY_ACTION :
                    return "MSG_PERFORM_ACCESSIBILITY_ACTION";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID :
                    return "MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID :
                    return "MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT :
                    return "MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_FOCUS :
                    return "MSG_FIND_FOCUS";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FOCUS_SEARCH :
                    return "MSG_FOCUS_SEARCH";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_PREPARE_FOR_EXTRA_DATA_REQUEST :
                    return "MSG_PREPARE_FOR_EXTRA_DATA_REQUEST";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_FINISHED :
                    return "MSG_APP_PREPARATION_FINISHED";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_TIMEOUT :
                    return "MSG_APP_PREPARATION_TIMEOUT";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_CLEAR_ACCESSIBILITY_FOCUS :
                    return "MSG_CLEAR_ACCESSIBILITY_FOCUS";
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_NOTIFY_OUTSIDE_TOUCH :
                    return "MSG_NOTIFY_OUTSIDE_TOUCH";
                default :
                    throw new java.lang.IllegalArgumentException("Unknown message type: " + type);
            }
        }

        @java.lang.Override
        public void handleMessage(android.os.Message message) {
            final int type = message.what;
            switch (type) {
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID :
                    {
                        findAccessibilityNodeInfoByAccessibilityIdUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_PERFORM_ACCESSIBILITY_ACTION :
                    {
                        performAccessibilityActionUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID :
                    {
                        findAccessibilityNodeInfosByViewIdUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT :
                    {
                        findAccessibilityNodeInfosByTextUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FIND_FOCUS :
                    {
                        findFocusUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_FOCUS_SEARCH :
                    {
                        focusSearchUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_PREPARE_FOR_EXTRA_DATA_REQUEST :
                    {
                        prepareForExtraDataRequestUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_FINISHED :
                    {
                        requestPreparerDoneUiThread(message);
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_APP_PREPARATION_TIMEOUT :
                    {
                        requestPreparerTimeoutUiThread();
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_CLEAR_ACCESSIBILITY_FOCUS :
                    {
                        clearAccessibilityFocusUiThread();
                    }
                    break;
                case android.view.AccessibilityInteractionController.PrivateHandler.MSG_NOTIFY_OUTSIDE_TOUCH :
                    {
                        notifyOutsideTouchUiThread();
                    }
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown message type: " + type);
            }
        }

        boolean hasAccessibilityCallback(android.os.Message message) {
            return message.what < android.view.AccessibilityInteractionController.PrivateHandler.FIRST_NO_ACCESSIBILITY_CALLBACK_MSG ? true : false;
        }
    }

    private final class AddNodeInfosForViewId implements java.util.function.Predicate<android.view.View> {
        private int mViewId = android.view.View.NO_ID;

        private java.util.List<android.view.accessibility.AccessibilityNodeInfo> mInfos;

        public void init(int viewId, java.util.List<android.view.accessibility.AccessibilityNodeInfo> infos) {
            mViewId = viewId;
            mInfos = infos;
        }

        public void reset() {
            mViewId = android.view.View.NO_ID;
            mInfos = null;
        }

        @java.lang.Override
        public boolean test(android.view.View view) {
            if ((view.getId() == mViewId) && isShown(view)) {
                mInfos.add(view.createAccessibilityNodeInfo());
            }
            return false;
        }
    }

    private static final class MessageHolder {
        final android.os.Message mMessage;

        final int mInterrogatingPid;

        final long mInterrogatingTid;

        MessageHolder(android.os.Message message, int interrogatingPid, long interrogatingTid) {
            mMessage = message;
            mInterrogatingPid = interrogatingPid;
            mInterrogatingTid = interrogatingTid;
        }
    }
}

